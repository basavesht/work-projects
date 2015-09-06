package com.bosch.tmp.integration.creation;

import com.bosch.th.integration.PushResults;
import com.bosch.th.integrationpatient.AddressType;
import com.bosch.th.integrationpatient.IntegrationPatientType;
import com.bosch.th.integrationpatient.PhoneNumberType;
import com.bosch.th.patientrecord.AssessedVitalSign;
import com.bosch.th.patientrecord.Device;
import com.bosch.th.patientrecord.DeviceReportedMode;
import com.bosch.th.patientrecord.MeterMeasurement;
import com.bosch.th.patientrecord.Mode;
import com.bosch.th.patientrecord.QuestionResponse;
import com.bosch.th.patientrecord.SurveyResponse;
import com.bosch.th.patientrecord.VitalSignValue;
import com.bosch.tmp.cl.basictypes.ExternalIdentifierType;
import com.bosch.tmp.integration.persistence.ControlNumber;
import com.bosch.tmp.integration.util.ConfigKeyEnum;
import com.bosch.tmp.integration.util.SegmentUtils;
import com.bosch.tmp.integration.util.Utils;
import com.bosch.tmp.integration.validation.ConfigValue;
import com.bosch.tmp.integration.validation.ConfigurationLoader;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import javax.xml.datatype.XMLGregorianCalendar;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.hl7.v2xml.ACKCONTENT;
import org.hl7.v2xml.BHS10CONTENT;
import org.hl7.v2xml.BHSCONTENT;
import org.hl7.v2xml.BTSCONTENT;
import org.hl7.v2xml.MSHCONTENT;
import org.hl7.v2xml.OBRCONTENT;
import org.hl7.v2xml.OBXCONTENT;
import org.hl7.v2xml.ORCCONTENT;
import org.hl7.v2xml.ORUR30BATCHCONTENT;
import org.hl7.v2xml.ORUR30CONTENT;
import org.hl7.v2xml.ORUR30ORDERCONTENT;
import org.hl7.v2xml.ORUR30PATIENTINFORMATIONCONTENT;
import org.hl7.v2xml.ORUR30RESULTSCONTENT;
import org.hl7.v2xml.PIDCONTENT;
import org.hl7.v2xml.RCPCONTENT;

/**
 * This is the default builder of a result message (ORU_R30).
 *
 * @author tis1pal *
 */
public class DefaultResultBuilder implements Builder
{

    public static final Logger logger = LoggerFactory.getLogger(DefaultResultBuilder.class);

    protected Map<String, List<String>> msgToResultsMap = new HashMap<String, List<String>>();

    // Set configuration values to default, in case the values cannot be retrieved from the ConfigurationLoader.
    protected int resultsCountPerBatchMessage = 25;

    // Set the value that comes as RCP in QVR Q17 message
    protected int resultsCountFromRCP = 25;

    protected int numOfOBRs = 10;

    protected int numOfOBXs = 25;

    public DefaultResultBuilder()
    {
        // Get the following values from ConfigurationLoader.
        try
        {
            this.resultsCountPerBatchMessage = Integer.parseInt(ConfigurationLoader.getApplicationConfigValueById(ConfigKeyEnum.RESULTS_COUNT_PER_MESSAGE.
                    toString()));
            this.numOfOBRs = Integer.parseInt(ConfigurationLoader.getApplicationConfigValueById(ConfigKeyEnum.NUMBER_OF_OBR_SEGMENTS.
                    toString()));
            this.numOfOBXs = Integer.parseInt(ConfigurationLoader.getApplicationConfigValueById(ConfigKeyEnum.NUMBER_OF_OBX_SEGMENTS.
                    toString()));
        }
        catch (Exception ex)
        {
            // do nothing (keep default value)
        }
    }

    public Object buildMessage(Object... data) throws CreationException
    {
        if (data.length < 3)
        {
            logger.debug("Error creating results response message. Invalid number of arguments.");
            throw new CreationException("Error creating results response message. Invalid number of arguments.");
        }
        return createResultsResponse(data[0], data[1], data[2]);
    }

    public Map<String, List<String>> getMsgToResultsMap()
    {
        return msgToResultsMap;
    }

    private ORUR30BATCHCONTENT createResultsResponse(Object message, Object messageMSH, Object messageRCP)
    {
        List<PushResults> results = new ArrayList<PushResults>();
        ACKCONTENT ack = null;
        if (message instanceof List)
        {
            try
            {
                results = (List<PushResults>) message;
            }
            catch (Exception ex)
            {
                logger.error("Could not cast message to type List<PushResults>.");
            }
        }
        else if (message instanceof ACKCONTENT)
        {
            try
            {
                ack = (ACKCONTENT) message;
            }
            catch (Exception ex)
            {
                logger.error("Could not cast message to type ACKCONTENT.");
            }
        }

        MSHCONTENT incomingMSH = null;
        try
        {
            incomingMSH = (MSHCONTENT) messageMSH;
        }
        catch (Exception ex)
        {
            logger.error("Could not cast messageMSH to type MSHCONTENT.");
        }

        RCPCONTENT incomingRCP = null;
        try
        {
            incomingRCP = (RCPCONTENT) messageRCP;
        }
        catch (Exception ex)
        {
            logger.error("Could not cast messageRCP to type RCPCONTENT.");
        }
        //get the RCP value which tell max number of oru_r30 to be shown in the response
        if (null != incomingRCP.getRCP2() && null != incomingRCP.getRCP2().getCQ1())
        {
            String value = incomingRCP.getRCP2().getCQ1().getValue();

            if (null != value && value.length() > 0)
            {
                try
                {
                    resultsCountFromRCP = Integer.parseInt(value);
                }
                catch (Exception e)
                {
                    //doing nothing as of now, since no proper validation messages are defined for RCP validation cases
                    logger.debug(
                            "Wrong RCP value is being sent in the Q17, Hence using the default value of RCP. i.e, 25");
                }

            }
        }
        int resultsCount = 0;
        //consider the smaller value between config value and the RCP value in Q17
        if (resultsCountFromRCP < resultsCountPerBatchMessage)
        {
            resultsCount = resultsCountFromRCP;
        }
        else
        {
            resultsCount = resultsCountPerBatchMessage;
        }

        ORUR30BATCHCONTENT oruR30Batch = new ORUR30BATCHCONTENT();
        // Batch control number needs to be generated upfront as it is used in MSH segment as well.
        String batchControlNumber = generateBatchControlNumber();

        if (ack != null)
        {
            // The message type in BHS 9 is specified based on if its a rejection/success msg.
            oruR30Batch.setBHS(buildBHS(incomingMSH, "ACK", batchControlNumber));
            oruR30Batch.getACK().add(ack);
            oruR30Batch.setBTS(buildBTS(oruR30Batch.getORUR30().size()));
        }
        else
        {
            // Add BHS and BTS segments to the oru batch message
            //oruR30Batch.setBTS(buildBTS(oruR30Batch.getORUR30().size()));
            oruR30Batch.setBHS(buildBHS(incomingMSH, "ORU_R30", batchControlNumber));
            //adding BHS.10 when there is no results to be displayed
            if (null == results || results.size() == 0)
            {
                BHSCONTENT bhs = oruR30Batch.getBHS();
                BHS10CONTENT bhs10 = new BHS10CONTENT();
                bhs10.setValue(ConfigurationLoader.getApplicationConfigValueById(ConfigKeyEnum.RESULTS_LAST_BATCH_MESSAGE.
                        toString()));
                bhs.setBHS10(bhs10);
                oruR30Batch.setBHS(bhs);
            }
            boolean maxCountReached = false;
            for (PushResults result : results)
            {
                // If the ORU_R30 count within the batch message reaches the configured limit,
                // then no further processing should be done and the response should be returned.
                if (oruR30Batch.getORUR30().size() == resultsCount)
                {
                    maxCountReached=true;
                    break;
                }
                oruR30Batch.getORUR30().addAll(buildORUR30(result, incomingMSH, batchControlNumber));
            }
            //set the BHS10
            if( oruR30Batch.getORUR30().size() < resultsCount || ! maxCountReached){
                 BHSCONTENT bhs = oruR30Batch.getBHS();
                BHS10CONTENT bhs10 = new BHS10CONTENT();
                bhs10.setValue(ConfigurationLoader.getApplicationConfigValueById(ConfigKeyEnum.RESULTS_LAST_BATCH_MESSAGE.
                        toString()));
                bhs.setBHS10(bhs10);
                oruR30Batch.setBHS(bhs);
            }
            oruR30Batch.setBTS(buildBTS(oruR30Batch.getORUR30().size()));
        }
        return oruR30Batch;
    }

    private List<ORUR30CONTENT> buildORUR30(PushResults result, MSHCONTENT incomingMSH, String batchControlNumber)
    {
        List<ORUR30CONTENT> oruR30s = new ArrayList<ORUR30CONTENT>();

        ORUR30CONTENT oruR30 = null;

        int messageControlNumber = oruR30s.size() + 1;
        int oruR30Count = 0;
        int resultsCount = 0;
         //consider the smaller value between config value and the RCP value in Q17
        if (resultsCountFromRCP < resultsCountPerBatchMessage)
        {
            resultsCount = resultsCountFromRCP;
        }
        else
        {
            resultsCount = resultsCountPerBatchMessage;
        }
        // New MSH, PID and ORC are created per patient.
        MSHCONTENT msh = buildMSH(incomingMSH, batchControlNumber + "_" + messageControlNumber, "ORU_R30");
        PIDCONTENT pid = buildPID(result.getIntegrationPatient());
        ORCCONTENT orc = buildORC();

        List<SurveyResponse> sessions = result.getSurveyResponse();
        for (SurveyResponse session : sessions)
        {
            // New OBR is created per session.
            OBRCONTENT obr = buildOBR(session.getResponseDate(), session.getReceivedDate());

            List<QuestionResponse> responses = session.getQuestionResponse();
            for (QuestionResponse response : responses)
            {
                List<MeterMeasurement> measurements = response.getMeterMeasurement();
                for (MeterMeasurement measurement : measurements)
                {
                    Mode collectionMode = measurement.getMode();
                    Device device = measurement.getCollectionType().getInterface();
                    for (AssessedVitalSign assesVitalSign : measurement.getVitalSign())
                    {
                        if (assesVitalSign != null && assesVitalSign.getId() != null)
                        {
                            boolean isMultipleVitalValuePresent = false;
                            int vitalValueCount = 0;
                            if (assesVitalSign.getNumericValue() != null && assesVitalSign.getNumericValue().size() > 1)
                            {
                                isMultipleVitalValuePresent = true;
                                vitalValueCount = 1;
                            }

                            for (VitalSignValue vitalSignValue : assesVitalSign.getNumericValue())
                            {
                                String vitalSignID = Long.toString(assesVitalSign.getId());
                                if (isMultipleVitalValuePresent)
                                {
                                    vitalSignID = vitalSignID + "_V" + vitalValueCount;
                                }

                                // New OBX is created per vital sign measurement.
                                OBXCONTENT obx = buildOBX(collectionMode, device, vitalSignValue, vitalSignID);
                                if (oruR30 == null)
                                {
                                    // Create the first ORU_R30
                                    oruR30 = buildORUR30(msh, pid, orc, obr, obx);
                                    oruR30Count++;
                                    //obrCount--;
                                }
                                else if (oruR30.getORUR30ORDER().get(oruR30.getORUR30ORDER().size() - 1).
                                        getORUR30RESULTS().
                                        size() == numOfOBXs && oruR30.getORUR30ORDER().size() != numOfOBRs)
                                {
                                    // Create new OBR and add OBX to it.
                                    oruR30 = buildORUR30(oruR30, orc, obr, obx);
                                    //obrCount--;
                                    //obxCount = numOfOBXs;
                                }
                                else if (oruR30.getORUR30ORDER().get(oruR30.getORUR30ORDER().size() - 1).
                                        getORUR30RESULTS().
                                        size() == numOfOBXs && oruR30.getORUR30ORDER().size() == numOfOBRs)
                                {
                                    // Create new ORU_R30
                                    // First add the previously created oru r30 once its full and then replace with the new one.
                                    oruR30s.add(oruR30);
                                   //check whether the max number of result count is reached
                                     if (oruR30s.size() == resultsCount)
                                     {
                                         return oruR30s;
                                     }
                                    oruR30 = buildORUR30(msh, pid, orc, obr, obx);
                                    oruR30Count++;
                                    //obxCount = numOfOBXs;
                                    //obrCount = numOfOBRs;
                                    //obrCount--;
                                }
                                else
                                {
                                    // Add OBX
                                    oruR30 = buildORUR30(oruR30, obx);
                                }
                                //obxCount--;
                                vitalValueCount++;
                            }
                        }
                    }
                }
            }
        }

        List<MeterMeasurement> adhocmeasurements = result.getMeterMeasurement();
        for (MeterMeasurement measurement : adhocmeasurements)
        {
            // New OBR is created per measurement.
            // OBRCONTENT obr = buildOBR(measurement.getResponseDate(), measurement.getReceivedDate());
            Mode collectionMode = measurement.getMode();
            Device device = measurement.getCollectionType().getInterface();
            XMLGregorianCalendar collectionTimeFlag = null;
            OBRCONTENT obr = null;
            if (collectionTimeFlag == null || !collectionTimeFlag.equals(measurement.getReceivedDate()))
            {
                obr = buildOBR(measurement.getResponseDate(), measurement.getReceivedDate());
                collectionTimeFlag = measurement.getReceivedDate();
            }
            for (AssessedVitalSign assesVitalSign : measurement.getVitalSign())
            {
                //Creating a unique vital sign id..
                if (assesVitalSign != null && assesVitalSign.getId() != null)
                {
                    boolean isMultipleVitalValuePresent = false;
                    int vitalValueCount = 0;
                    if (assesVitalSign.getNumericValue() != null && assesVitalSign.getNumericValue().size() > 1)
                    {
                        isMultipleVitalValuePresent = true;
                        vitalValueCount = 1;
                    }

                    for (VitalSignValue vitalSignValue : assesVitalSign.getNumericValue())
                    {

                        String vitalSignID = Long.toString(assesVitalSign.getId());
                        if (isMultipleVitalValuePresent)
                        {
                            vitalSignID = vitalSignID + "_V" + vitalValueCount;
                        }
                        // New OBX is created per vital sign measurement.
                        OBXCONTENT obx = buildOBX(collectionMode, device, vitalSignValue, vitalSignID);
                        if (oruR30 == null)
                        {
                            // Create the first ORU_R30
                            oruR30 = buildORUR30(msh, pid, orc, obr, obx);
                            oruR30Count++;
                            //obrCount--;
                        }
                        else if (oruR30.getORUR30ORDER().get(oruR30.getORUR30ORDER().size() - 1).getORUR30RESULTS().size() == numOfOBXs && oruR30.
                                getORUR30ORDER().size() != numOfOBRs)
                        {
                            // Create new OBR and add OBX to it.
                            oruR30 = buildORUR30(oruR30, orc, obr, obx);
                            //obrCount--;
                            //obxCount = numOfOBXs;
                            }
                        else if (oruR30.getORUR30ORDER().get(oruR30.getORUR30ORDER().size() - 1).getORUR30RESULTS().size() == numOfOBXs && oruR30.
                                getORUR30ORDER().size() == numOfOBRs)
                        {
                            // Create new ORU_R30
                            // First add the previously created oru r30 once its full and then replace with the new one.
                            oruR30s.add(oruR30);
							//check whether the max number of result count is reached
                            if (oruR30s.size() == resultsCount)
                            {
                                return oruR30s;
                             }
                            oruR30 = buildORUR30(msh, pid, orc, obr, obx);
                            oruR30Count++;
                            //obxCount = numOfOBXs;
                            //obrCount = numOfOBRs;
                            //obrCount--;
                            }
                        else
                        {
                            // Add OBX
                            oruR30 = buildORUR30(oruR30, obx);
                        }
                        //obxCount--;

                        vitalValueCount++;
                    }
                }
                //obxCount--;
            }
        }
        // Add the last oru r30 to the list and return.
        oruR30s.add(oruR30);
        return oruR30s;

    }

    private ORUR30CONTENT buildORUR30(MSHCONTENT msh, PIDCONTENT pid, ORCCONTENT orc, OBRCONTENT obr, OBXCONTENT obx)
    {
        ORUR30CONTENT oruR30 = new ORUR30CONTENT();

        // set msh
        oruR30.setMSH(msh);
        // set pid
        ORUR30PATIENTINFORMATIONCONTENT patientInfo = new ORUR30PATIENTINFORMATIONCONTENT();
        patientInfo.setPID(pid);
        oruR30.setORUR30PATIENTINFORMATION(patientInfo);
        // set orc and obr
        ORUR30ORDERCONTENT orderInfo = new ORUR30ORDERCONTENT();
        orderInfo.setORC(orc);
        orderInfo.setOBR(obr);
        // set obx
        ORUR30RESULTSCONTENT resultInfo = new ORUR30RESULTSCONTENT();
        resultInfo.setOBX(obx);
        orderInfo.getORUR30RESULTS().add(resultInfo);

        oruR30.getORUR30ORDER().add(orderInfo);

        return oruR30;
    }

    private ORUR30CONTENT buildORUR30(ORUR30CONTENT oruR30, ORCCONTENT orc, OBRCONTENT obr, OBXCONTENT obx)
    {
        // set orc and obr
        ORUR30ORDERCONTENT orderInfo = new ORUR30ORDERCONTENT();
        orderInfo.setORC(orc);
        orderInfo.setOBR(obr);
        // set obx
        ORUR30RESULTSCONTENT resultInfo = new ORUR30RESULTSCONTENT();
        resultInfo.setOBX(obx);
        orderInfo.getORUR30RESULTS().add(resultInfo);

        oruR30.getORUR30ORDER().add(orderInfo);

        return oruR30;
    }

    private ORUR30CONTENT buildORUR30(ORUR30CONTENT oruR30, OBXCONTENT obx)
    {
        // set obx
        ORUR30RESULTSCONTENT resultInfo = new ORUR30RESULTSCONTENT();
        resultInfo.setOBX(obx);
        oruR30.getORUR30ORDER().get(oruR30.getORUR30ORDER().size() - 1).getORUR30RESULTS().add(resultInfo);

        return oruR30;
    }

    private MSHCONTENT buildMSH(MSHCONTENT incomingMSH, String messageControlNumber, String messageType)
    {
        return SegmentUtils.buildMSH(incomingMSH, messageControlNumber, messageType);
    }

    private PIDCONTENT buildPID(IntegrationPatientType patient)
    {
        List<ExternalIdentifierType> ids = patient.getIdentifier().getExternalIdentifier();
        String patId = null, ssn = null;
        for (ExternalIdentifierType id : ids)
        {
            if (id != null && id.getName() != null && id.getName().equalsIgnoreCase(ConfigurationLoader.
                    getApplicationConfigValueById(ConfigKeyEnum.PAT_ID.toString())))
            {
                patId = id.getValue();
            }
            else if (id != null && id.getName() != null && id.getName().equalsIgnoreCase(ConfigurationLoader.
                    getApplicationConfigValueById(ConfigKeyEnum.SSN.toString())))
            {
                ssn = id.getValue();
            }
        }

        String lastName = patient.getLastName();
        String firstName = patient.getFirstName();
        String middleInitial = patient.getMiddleInitial();
        String dob = Utils.convertXMLGregorianCalendarToString(patient.getBirthDate(), ConfigurationLoader.
                getFieldConfigurations(
                null, null, "PID7").toString());

        String gender = patient.getGender() != null ? patient.getGender().toString() : null;

        String address1 = null, address2 = null, zipCode = null, city = null, state = null, country = null;
        try
        {
            AddressType address = !patient.getAddress().isEmpty() ? patient.getAddress().get(0) : new AddressType();

            List<String> streets = address.getStreet();
            if (streets.size() == 1)
            {
                address1 = address.getStreet().get(0);
            }
            else if (streets.size() > 1)
            {
                address1 = address.getStreet().get(0);
                address2 = address.getStreet().get(1);
            }
            zipCode = address.getZipCode();
            city = address.getCity();
            state = address.getState();
            country = address.getCountry();
        }
        catch (Exception e)
        {
            // do nothing
        }

        String phoneNumber = null;
        try
        {
            PhoneNumberType phone = !patient.getPhoneNumber().isEmpty() ? patient.getPhoneNumber().get(0) : new PhoneNumberType();
            phoneNumber = phone.getNumber();
        }
        catch (Exception e)
        {
            // do nothing
        }

        PIDCONTENT pid = SegmentUtils.buildPID(patId, ssn, lastName, firstName, middleInitial, dob, gender,
                address1, address2, city, state, zipCode, country, phoneNumber);
        return pid;
    }

    private ORCCONTENT buildORC()
    {
        // Generate order control number.
        // This is same for all the results belonging to a patient.
        String orderControlNumber = null;
        try
        {
            ControlNumber controlNumber = new ControlNumber();
            controlNumber.persist();
            orderControlNumber = controlNumber.getId().toString();
        }
        catch (Exception ex)
        {
            logger.error("Cannot persist control number, setting it to empty string.");
            orderControlNumber = "";
        }
        logger.debug("ORC2.1 Observation order control number generated is " + orderControlNumber);

        String sendingFacility = null;
        ConfigValue obj = ConfigurationLoader.getFieldById("ORC2_EI2").getConfigValue();
        if (obj != null)
        {
            sendingFacility = obj.getValue();
        }
        return SegmentUtils.buildORC(orderControlNumber, sendingFacility);
    }

    private OBRCONTENT buildOBR(XMLGregorianCalendar responseDate, XMLGregorianCalendar receivedDate)
    {
        return SegmentUtils.buildOBR(responseDate, receivedDate);
    }

    private OBXCONTENT buildOBX(MeterMeasurement measurement)
    {
        // Set id is constant for all the results.
        int setId = 1;
        try
        {
            Mode collectionMode = measurement.getMode();
            Device device = measurement.getCollectionType().getInterface();
            VitalSignValue vitalSignValue = measurement.getVitalSign().get(0).getNumericValue().get(0);
            String loincCode = vitalSignValue.getLoincCode();
            BigDecimal vitalReading = vitalSignValue.getValue();
            String uom = vitalSignValue.getUnit().value();
            XMLGregorianCalendar collectionTime = vitalSignValue.getCollectionTime();

            String vitalCollectionMode = collectionMode instanceof DeviceReportedMode ? "device" : "self";

            String resultId = Long.toString(measurement.getId());

            return SegmentUtils.buildOBX(setId, loincCode, vitalReading, uom, collectionTime,
                    vitalCollectionMode, device,
                    resultId);
        }
        catch (Exception ex)
        {
            // if no value can be retrieved, return null
            return null;
        }
    }

    public BTSCONTENT buildBTS(int numOfMessages)
    {
        return SegmentUtils.buildBTS(numOfMessages);
    }

    public BHSCONTENT buildBHS(MSHCONTENT incomingMSH, String batchMessageType, String batchControlNumber)
    {
        // Sending facility code in incoming msh becomes the receiving facility code of the outgoing message.
        if (null == incomingMSH)
        {
            return null;
        }
        String receivingFacilityCode = null;
        if (null != incomingMSH.getMSH4())
        {
            receivingFacilityCode = incomingMSH.getMSH4().getHD1().getValue();
        }
        String referenceMessageControlNumber = null;
        if (null != incomingMSH.getMSH10() && null != incomingMSH.getMSH4().getHD1())
        {
            referenceMessageControlNumber = incomingMSH.getMSH10().getValue();
        }
        return SegmentUtils.buildBHS(receivingFacilityCode, batchMessageType, batchControlNumber,
                referenceMessageControlNumber);
    }

    private String generateBatchControlNumber()
    {
        // Generate batch control number.
        String batchControlNumber = null;
        try
        {
            ControlNumber controlNumber = new ControlNumber();
            controlNumber.persist();
            batchControlNumber = controlNumber.getId().toString();
        }
        catch (Exception ex)
        {
            logger.error("Cannot persist control number, setting it to empty string.");
            batchControlNumber = "";
        }
        logger.debug("BHS.11 batch control number generated is " + batchControlNumber);
        return batchControlNumber;
    }

    private OBXCONTENT buildOBX(Mode collectionMode, Device device, VitalSignValue vitalSignValue, String resultId)
    {
        try
        {
            int setId = 1;
            String loincCode = vitalSignValue.getLoincCode();
            BigDecimal vitalReading = vitalSignValue.getValue();
            String uom = vitalSignValue.getUnit().value();
            XMLGregorianCalendar collectionTime = vitalSignValue.getCollectionTime();
            String vitalCollectionMode = collectionMode instanceof DeviceReportedMode ? "device" : "self";
            return SegmentUtils.buildOBX(setId, loincCode, vitalReading, uom, collectionTime,
                    vitalCollectionMode, device,
                    resultId);
        }
        catch (Exception ex)
        {
            // if no value can be retrieved, return null
            return null;
        }
    }
}
