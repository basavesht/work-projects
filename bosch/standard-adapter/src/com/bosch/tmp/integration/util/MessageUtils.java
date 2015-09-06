package com.bosch.tmp.integration.util;

import com.bosch.th.externalorganization.OrganizationType;
import com.bosch.th.integrationpatient.AddressType;
import com.bosch.th.integrationpatient.GenderType;
import com.bosch.th.integrationpatient.IntegrationPatientType;
import com.bosch.th.integrationpatient.PhoneNumberType;
import com.bosch.tmp.cl.basictypes.ExternalIdentifierType;
import com.bosch.tmp.integration.process.ProcessException;
import com.bosch.tmp.integration.validation.ConfigurationLoader;
import com.bosch.tmp.integration.validation.Fault;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import javax.xml.datatype.XMLGregorianCalendar;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.hl7.v2xml.ACKBATCHCONTENT;
import org.hl7.v2xml.ACKCONTENT;
import org.hl7.v2xml.ADTA01CONTENT;
import org.hl7.v2xml.ADTA03CONTENT;
import org.hl7.v2xml.ADTA05CONTENT;
import org.hl7.v2xml.BHSCONTENT;
import org.hl7.v2xml.MSHCONTENT;
import org.hl7.v2xml.ORUR01BATCHCONTENT;
import org.hl7.v2xml.ORUR30BATCHCONTENT;
import org.hl7.v2xml.PID11CONTENT;
import org.hl7.v2xml.PID13CONTENT;
import org.hl7.v2xml.PID3CONTENT;
import org.hl7.v2xml.PID5CONTENT;
import org.hl7.v2xml.PIDCONTENT;
import org.hl7.v2xml.QVRQ17CONTENT;
import org.hl7.v2xml.UACCONTENT;
import org.hl7.v2xml.XTN1CONTENT;
import org.hl7.v2xml.RCPCONTENT;

/**
 *
 * @author tis1pal
 */
public class MessageUtils
{

    public static final Logger logger = LoggerFactory.getLogger(MessageUtils.class);

    public static String getMessageName(Object object)
    {
        if (object instanceof ACKCONTENT)
        {
            return "ACK";
        }
        else if (object instanceof ORUR30BATCHCONTENT)
        {
            return "ORU_R30";
        }
        else if (object instanceof ACKBATCHCONTENT)
        {
            return "ACK_BATCH";
        }
        else if (object instanceof ADTA01CONTENT)
        {
            return "ADT_A01";
        }
        else if (object instanceof ADTA03CONTENT)
        {
            return "ADT_A03";
        }
        else if (object instanceof ADTA05CONTENT)
        {
            return "ADT_A05";
        }
        else if (object instanceof QVRQ17CONTENT)
        {
            return "QVR_Q17";
        }
        else if (object instanceof ORUR01BATCHCONTENT)
        {
            return "ORU_R01";
        }
        else
        {
            // Message type not supported
            logger.debug("Message class not supported: " + object.getClass().getCanonicalName());
            return "anyHL7Segment";
        }
    }

    public static String getMessageNameByTriggerEvent(Object message)
    {
        MSHCONTENT mshcontent = null;
        try
        {
            mshcontent = MessageUtils.getMSH(message);
        }
        catch (Exception ex)
        {
            logger.error("Exception occured while getting msh from the message using Java reflection");
            return null;
        }

        String messageNameByTriggerEvent = null;
        if (mshcontent != null)
        {
            try
            {
                String messageTrigger = MessageUtils.getTriggerEvent(message);
                String messageType = MessageUtils.getMessageType(message);
                if (messageType != null && messageType.length() > 0)
                {
                    if (messageTrigger != null && messageTrigger.length() > 0)
                    {
                        if (messageTrigger.equalsIgnoreCase("A04") && messageType.equalsIgnoreCase("ADT"))
                        {
                            messageNameByTriggerEvent = "ADT_A04";
                        }
                        else if (messageTrigger.equalsIgnoreCase("A08") && messageType.equalsIgnoreCase("ADT"))
                        {
                            messageNameByTriggerEvent = "ADT_A08";
                        }
                        else if (messageTrigger.equalsIgnoreCase("A31") && messageType.equalsIgnoreCase("ADT"))
                        {
                            messageNameByTriggerEvent = "ADT_A31";
                        }
                        else if (messageTrigger.equalsIgnoreCase("A03") && messageType.equalsIgnoreCase("ADT"))
                        {
                            messageNameByTriggerEvent = "ADT_A03";
                        }
                        else if (messageTrigger.equalsIgnoreCase("Q17") && messageType.equalsIgnoreCase("QVR"))
                        {
                            messageNameByTriggerEvent = "QVR_Q17";
                        }
                        else if (messageTrigger.equalsIgnoreCase("A24") && messageType.equalsIgnoreCase("ADT"))
                        {
                            messageNameByTriggerEvent = "ADT_A24";
                        }
                    }
                    else if (messageType.equalsIgnoreCase("ACK"))
                    {
                        messageNameByTriggerEvent = "ACK_BATCH";
                    }
                }
            }
            catch (Exception ex)
            {
                logger.error("Exception occured while trying to get the message type or tigger event from msh");
            }
        }
        return messageNameByTriggerEvent;
    }

    /**
     * Method to create Error object given a faultId
     * @param faultId used to get fault info from config file
     * @param params runtime parameters that needs to be added to the fault external message
     * @return Error object
     */
    public static Error createError(String faultId)
    {
        Error error = new Error();
        Fault fault = ConfigurationLoader.getApplicationFaultById(faultId);
        if (fault != null)
        {
            logger.error("Extracting Fault information from config file for fault ID: " + faultId);
            error.setFaultExternalMessage(fault.getFaultExternalMessage());
            error.setFaultInternalMessage(fault.getFaultInternalMessage());
            error.setFaultExternalCode(fault.getFaultExternalCode());
            error.setFaultInternalCode(fault.getFaultInternalCode());
            error.setSegment(fault.getSegmentName());
            error.setFieldNumber(fault.getFieldNumber());
            error.setFieldComponentNumber(fault.getComponentNumber());
            logger.error("FaultExternalMessage: " + fault.getFaultExternalMessage() + "FaultExternalCode: " + fault.
                    getFaultExternalCode());
            logger.error("FaultInternalMessage: " + fault.getFaultInternalMessage() + "FaultInternalCode: " + fault.
                    getFaultInternalCode());
        }
        return error;
    }

    /**
     * According to HL7 standard a field with null or zero length value should be ignored,
     * whereas a field with explicit empty string ("") should be overridden and set to null in DB.
     * According to CIA null field value will be ignored whereas non null field will be updated accordingly.
     * This method converts the HL7 standard field value to CIA standard value.
     * @param fieldValue
     * @return String "" or null
     */
    private static String processFieldValue(String fieldValue)
    {
        logger.debug("Field Value before processing to CIA standard is: " + fieldValue);
        if (fieldValue == null || fieldValue.trim().length() == 0)
        {
            fieldValue = null;
        }
        else if (fieldValue.equals("\"\""))
        {
            fieldValue = "";/* this is empty string not """"*/
        }
        logger.debug("Field Value after processing to CIA standard is: " + fieldValue);
        return fieldValue;
    }

    /******************MSH Helper Methods*************************/
    public static Object getHL7MessageHeader(Object message) throws Exception
    {
        Object requestHeaderData = null;
        if (message != null)
        {
            if (message instanceof ACKBATCHCONTENT || message instanceof ORUR30BATCHCONTENT || message instanceof ORUR01BATCHCONTENT )
            {
                requestHeaderData = getBHS(message);
            }
            else
            {
                requestHeaderData = getMSH(message);
            }
        }
        return requestHeaderData;
    }

    public static MSHCONTENT getMSH(Object message) throws Exception
    {
        MSHCONTENT mshContent = null;
        Method method = message.getClass().getMethod("getMSH");
        if (method != null)
        {
            mshContent = (MSHCONTENT) method.invoke(message);
        }
        return mshContent;
    }

    public static RCPCONTENT getRCP(Object message) throws Exception
    {
    	RCPCONTENT rcpContent = null;
        Method method = message.getClass().getMethod("getRCP");
        if (method != null)
        {
        	rcpContent = (RCPCONTENT) method.invoke(message);
        }
        return rcpContent;
    }

    public static BHSCONTENT getBHS(Object message) throws Exception
    {
        BHSCONTENT bhsContent = null;
        Method method = message.getClass().getMethod("getBHS");
        if (method != null)
        {
            bhsContent = (BHSCONTENT) method.invoke(message);
        }
        return bhsContent;
    }

    /*
     * Get ACK Request type either from the MSH or BHS segment of the incoming/outgoing message...
     */
    public static String getHL7ACKRequestType(Object requestHeaderData) throws Exception
    {
        String ackRequestType = null;
        if (requestHeaderData != null)
        {
            if (requestHeaderData instanceof MSHCONTENT)
            {
                MSHCONTENT mshContent = (MSHCONTENT) requestHeaderData;
                ackRequestType = mshContent.getMSH16().getValue();
            }
        }

        //Mapping default value in case of the following condition..
        if (ackRequestType == null || ackRequestType.trim().equals(""))
        {
            ackRequestType = AckRequestCodeEnum.AL.toString();
        }
        logger.debug("HL7 Message ACK Request Type: " + ackRequestType);
        return ackRequestType;
    }

    /*
     * Get Message Control number either from the MSH or BHS segment of the incoming/outgoing message...
     */
    public static String getHL7MessageControlNumber(Object requestHeaderData)
    {
        String msgCntrlNumber = null;
        if (requestHeaderData != null)
        {
            if (requestHeaderData instanceof MSHCONTENT)
            {
                MSHCONTENT mshContent = (MSHCONTENT) requestHeaderData;
                msgCntrlNumber = mshContent.getMSH10().getValue();
            }
            else if (requestHeaderData instanceof BHSCONTENT)
            {
                BHSCONTENT bhsContent = (BHSCONTENT) requestHeaderData;
                msgCntrlNumber = bhsContent.getBHS11().getValue();
            }
        }
        logger.debug("HL7 Message ACK Request Type: " + msgCntrlNumber);
        return msgCntrlNumber;
    }

    /*
     * Get Message Processing number either from the MSH or BHS segment of the incoming/outgoing message...
     */
    public static String getHL7MessageProcessingId(Object requestHeaderData) throws Exception
    {
        String processingId = null;
        if (requestHeaderData != null)
        {
            if (requestHeaderData instanceof MSHCONTENT)
            {
                MSHCONTENT mshContent = (MSHCONTENT) requestHeaderData;
                processingId = mshContent.getMSH11().getPT1().getValue();
            }
            else if (requestHeaderData instanceof BHSCONTENT)
            {
                //get the processing id from the config file
                Object msh11Pt1ForAckConfig = ConfigurationLoader.getDataTypeConfigurations("ACK", "MSH", "MSH11", "PT1");
                if (msh11Pt1ForAckConfig != null && msh11Pt1ForAckConfig instanceof String)
                {
                    processingId = msh11Pt1ForAckConfig.toString();
                    logger.debug("MSH11PT1 Configuration for ACK Message: " + msh11Pt1ForAckConfig.toString());
                }

            }
        }
        logger.debug("HL7 Message Processing ID: " + processingId);
        return processingId;
    }

    /*
     * Get Message Version Number either from the MSH or BHS segment of the incoming/outgoing message...
     */
    public static String getHL7VersionNumber(Object requestHeaderData)
    {
        String hl7VersionNumber = null;
        if (requestHeaderData != null)
        {
            if (requestHeaderData instanceof MSHCONTENT)
            {
                MSHCONTENT mshContent = (MSHCONTENT) requestHeaderData;
                if (mshContent.getMSH12() != null && mshContent.getMSH12().getVID1() != null)
                {
                    hl7VersionNumber = mshContent.getMSH12().getVID1().getValue();
                }
            }
            else if (requestHeaderData instanceof BHSCONTENT)
            {
                //get the HL7 version from the config file
                Object msh12Vid1Config = ConfigurationLoader.getDataTypeConfigurations("ACK", "MSH", "MSH12", "VID1");
                if (msh12Vid1Config != null && msh12Vid1Config instanceof String)
                {
                    hl7VersionNumber = msh12Vid1Config.toString();
                }
            }
        }
        logger.debug("HL7 Message Version Number: " + hl7VersionNumber);
        return hl7VersionNumber;
    }

    /*
     * Get HL7 Message sent timestamp either from the MSH or BHS segment of the incoming/outgoing message...
     */
    public static String getHL7MessageSentTimestamp(Object requestHeaderData) throws Exception
    {
        String msgCntrlNumber = null;
        if (requestHeaderData != null)
        {
            if (requestHeaderData instanceof MSHCONTENT)
            {
                MSHCONTENT mshContent = (MSHCONTENT) requestHeaderData;
                msgCntrlNumber = mshContent.getMSH7().getTS1().getValue();
            }
            else if (requestHeaderData instanceof BHSCONTENT)
            {
                BHSCONTENT bhsContent = (BHSCONTENT) requestHeaderData;
                msgCntrlNumber = bhsContent.getBHS7().getTS1().getValue();
            }
        }
        logger.debug("HL7 Message sent timestamp: " + msgCntrlNumber);
        return msgCntrlNumber;
    }

    /*
     * Get HL7 Message Sending facility either from the MSH or BHS segment of the incoming/outgoing message...
     */
    public static String getHL7MessageSendingFacility(Object requestHeaderData, String messageType)
    {
        String msgSendingFacility = null;
        if (requestHeaderData != null)
        {
            if (requestHeaderData instanceof MSHCONTENT)
            {
                MSHCONTENT mshContent = (MSHCONTENT) requestHeaderData;
                msgSendingFacility = mshContent.getMSH4().getHD1().getValue();
            }
            else if (requestHeaderData instanceof BHSCONTENT)
            {
                BHSCONTENT bhsContent = (BHSCONTENT) requestHeaderData;
                if (messageType != null && messageType.equalsIgnoreCase("ORU_R30"))
                {
                    if (bhsContent.getBHS6() != null && bhsContent.getBHS6() != null)
                    {
                        msgSendingFacility = bhsContent.getBHS6().getValue();
                    }
                }
                else
                {
                    if (bhsContent.getBHS4() != null && bhsContent.getBHS4() != null)
                    {
                        msgSendingFacility = bhsContent.getBHS4().getValue();
                    }
                }
            }
        }
        logger.debug("HL7 Message Sending facility: " + msgSendingFacility);
        return msgSendingFacility;
    }

    /*
     * Get HL7 Message Sending facility either from the MSH or BHS segment of the incoming/outgoing message...
     */
    public static String getHL7MessageType(Object requestHeaderData) throws Exception
    {
        String msgType = null;
        if (requestHeaderData != null)
        {
            if (requestHeaderData instanceof MSHCONTENT)
            {
                MSHCONTENT mshContent = (MSHCONTENT) requestHeaderData;
                if (mshContent.getMSH9() != null && mshContent.getMSH9().getMSG2() != null)
                {
                    msgType = mshContent.getMSH9().getMSG2().getValue();
                }
            }
            else if (requestHeaderData instanceof BHSCONTENT)
            {
                BHSCONTENT bhsContent = (BHSCONTENT) requestHeaderData;
                if (bhsContent.getBHS9() != null)
                {
                    msgType = bhsContent.getBHS9().getValue();
                }
            }
        }
        logger.debug("HL7 Message Type: " + msgType);
        return msgType;
    }

    public static String getTriggerEvent(Object message) throws Exception
    {
        MSHCONTENT mshContent = getMSH(message);
        String triggerEvent = null;
        if (mshContent != null && mshContent.getMSH9() != null && mshContent.getMSH9().getMSG2() != null)
        {
            triggerEvent = mshContent.getMSH9().getMSG2().getValue();
        }
        logger.debug("Trigger Event of incoming message is: " + triggerEvent);
        return triggerEvent;
    }

    public static String getMessageType(Object message) throws Exception
    {
        MSHCONTENT mshContent = getMSH(message);
        String messageType = null;
        if (mshContent != null && mshContent.getMSH9() != null && mshContent.getMSH9().getMSG1() != null)
        {
            messageType = mshContent.getMSH9().getMSG1().getValue();
        }
        logger.debug("Message Type of incoming message is: " + messageType);
        return messageType;
    }

    public static String getOrganizationId(Object message) throws Exception
    {
    	Object requestHeaderData = getHL7MessageHeader(message);
        String organizationId = null;
        if (requestHeaderData != null)
        {
            if (requestHeaderData instanceof MSHCONTENT) {
                MSHCONTENT mshContent = (MSHCONTENT) requestHeaderData;
                if(mshContent.getMSH4()!=null && mshContent.getMSH4().getHD1()!=null && mshContent.getMSH4().getHD1().getValue()!=null) {
                  organizationId = mshContent.getMSH4().getHD1().getValue();
                }
            }
            else if (requestHeaderData instanceof BHSCONTENT) {
                BHSCONTENT bhsContent = (BHSCONTENT) requestHeaderData;
                if(bhsContent.getBHS4()!=null && bhsContent.getBHS4().getValue()!=null) {
                    organizationId = bhsContent.getBHS4().getValue();
                }
            }
        }
        logger.debug("Organization Id in the incoming message is: " + organizationId);
        return organizationId;
    }

    /******************PID Helper Methods*************************/
    public static PIDCONTENT getPID(Object message) throws Exception
    {
        List<PIDCONTENT> pidContentList = null;
        PIDCONTENT pidContent = null;
        Method method = message.getClass().getMethod("getPID");
        if (method != null)
        {
            if (method.invoke(message) instanceof List)
            {
                pidContentList = (List<PIDCONTENT>) method.invoke(message);
            }
            else
            {
                pidContent = (PIDCONTENT) method.invoke(message);
            }
        }
        if (pidContentList != null && !pidContentList.isEmpty())
        {
            pidContent = pidContentList.get(0);
        }
        return pidContent;
    }

    /**
     * This method parses the incoming message to get all the identifiers like PS, SS from PID3 segment.
     * This method takes the first PID3 segment when multiple PID3s exist.
     * @param message
     * @return list of external ids
     * @throws Exception
     */
    public static List<ExternalIdentifierType> getIdentifiers(Object message) throws Exception
    {
        List<ExternalIdentifierType> identifiers = new ArrayList<ExternalIdentifierType>();
        boolean foundSSN = false;
        boolean foundPSId = false;
        PIDCONTENT pidContent = getPID(message);
        List<PID3CONTENT> pid3s = null;

        if (pidContent != null)
        {
            pid3s = pidContent.getPID3();
        }
        for (PID3CONTENT pid3 : pid3s)
        {
            ExternalIdentifierType identifier = new ExternalIdentifierType();
            String idType = pid3.getCX5().getValue();
            String idValue = pid3.getCX1().getValue();

            if (foundPSId && foundSSN)
            {
                break;
            }
            else if (!foundPSId && idType != null && idType.equalsIgnoreCase(ConfigurationLoader.
                    getApplicationConfigValueById(ConfigKeyEnum.PAT_ID.toString())))
            {
                logger.info(
                        "Patient System Id in the incoming message is:\nID Type: " + idType + " ID Value: " + idValue);
                identifier.setName(idType);
                identifier.setValue(idValue);
                identifiers.add(identifier);
                foundPSId = true;
            }
            else if (!foundSSN && idType != null && idType.equalsIgnoreCase(ConfigurationLoader.
                    getApplicationConfigValueById(ConfigKeyEnum.SSN.toString())))
            {
                logger.info(
                        "Patient SSN in the incoming message is:\nID Type: " + idType + " ID Value: XXXXXXXXXX");
                identifier.setName(idType);
                if (idValue != null && !idValue.trim().isEmpty())
                {
                    identifier.setValue(getOnlyNumeric(idValue));
                }
                else
                {
                    idValue = null;
                }
                identifiers.add(identifier);
                foundSSN = true;
            }
        }
        return identifiers;
    }

    public static ExternalIdentifierType getPatId(Object message) throws Exception
    {
        List<ExternalIdentifierType> identifiers = getIdentifiers(message);
        ExternalIdentifierType eit = null;
        for (ExternalIdentifierType id : identifiers)
        {
            if (id.getName().equalsIgnoreCase(ConfigurationLoader.getApplicationConfigValueById(ConfigKeyEnum.PAT_ID.
                    toString())))
            {
                eit = id;
            }
        }
        return eit;
    }

    /**
     * This method parses the incoming message to get the patient first name from PID5 segment.
     * This method takes the first PID5 segment when multiple PID5s exist.
     * @param message
     * @return patient first name
     * @throws Exception
     */
    public static String getPatientFirstName(Object message) throws Exception
    {
        PIDCONTENT pidContent = getPID(message);
        List<PID5CONTENT> pid5s = null;

        if (pidContent != null)
        {
            pid5s = pidContent.getPID5();
        }

        String firstName = null;
        for (PID5CONTENT pid5 : pid5s)
        {
            if (pid5.getXPN2() != null)
            {
                firstName = pid5.getXPN2().getValue();
            }
            break;
        }

        firstName = processFieldValue(firstName);
        logger.debug("Patient First Name in incoming message is: " + firstName);
        int firstNameLength = Integer.parseInt(ConfigurationLoader.getApplicationConfigValueById(ConfigKeyEnum.FIRST_NAME_LENGTH.
                toString()));
        if (firstName != null)
        {
            firstName = firstName.length() > firstNameLength ? firstName.substring(0, firstNameLength) : firstName;
            logger.info(
                    "Patient First Name in incoming message exceeds configured length and hence trimming it: " + firstName);
        }
        return firstName;
    }

    /**
     * This method parses the incoming message to get the patient last name from PID5 segment.
     * This method takes the first PID5 segment when multiple PID5s exist.
     * @param message
     * @return patient's last name
     * @throws Exception
     */
    public static String getPatientLastName(Object message) throws Exception
    {
        PIDCONTENT pidContent = getPID(message);
        List<PID5CONTENT> pid5s = null;

        if (pidContent != null)
        {
            pid5s = pidContent.getPID5();
        }

        String lastName = null;
        for (PID5CONTENT pid5 : pid5s)
        {
            if (pid5.getXPN1() != null && pid5.getXPN1().getFN1() != null)
            {
                lastName = pid5.getXPN1().getFN1().getValue();
            }
            break;
        }
        logger.debug("Patient Last Name in incoming message is: " + lastName);
        lastName = processFieldValue(lastName);
        int lastNameLength = Integer.parseInt(ConfigurationLoader.getApplicationConfigValueById(ConfigKeyEnum.LAST_NAME_LENGTH.
                toString()));

        if (lastName != null && lastName.trim().length() > 0)
        {
            lastName = lastName.length() > lastNameLength ? lastName.substring(0, lastNameLength) : lastName.trim();
            logger.info(
                    "Patient Last Name in incoming message exceeds configured length and hence trimming it: " + lastName);
        }
        return lastName;
    }

    /**
     * This method parses the incoming message to get the patient middle name from PID5 segment.
     * This method takes the first PID5 segment when multiple PID5s exist.
     * @param message
     * @return patient's middle name
     * @throws Exception
     */
    public static String getPatientMiddleName(Object message) throws Exception
    {
        PIDCONTENT pidContent = getPID(message);
        List<PID5CONTENT> pid5s = null;

        if (pidContent != null)
        {
            pid5s = pidContent.getPID5();
        }

        String middleName = null;
        for (PID5CONTENT pid5 : pid5s)
        {
            if (pid5.getXPN3() != null)
            {
                middleName = pid5.getXPN3().getValue();
            }
            break;
        }

        middleName = processFieldValue(middleName);
        logger.debug("Patient Middle Name in incoming message is: " + middleName);
        //handling lenght of the middle name as it is handle for the FN and LN
        int middleNameLength = Integer.parseInt(ConfigurationLoader.getApplicationConfigValueById(ConfigKeyEnum.FIRST_NAME_LENGTH.
                toString()));
        if (middleName != null)
        {
            middleName = middleName.length() > middleNameLength ? middleName.substring(0, middleNameLength) : middleName;
            logger.debug(
                    "Patient Middle Name in incoming message exceeds configured length and hence trimming it: " + middleName);
        }
        return middleName;
    }

    /**
     * This method parses the incoming message to get the patient DOB from PID7 segment.
     * @param message
     * @return patient DOB
     * @throws Exception
     */
    public static XMLGregorianCalendar getPatientBirthDate(Object message) throws Exception
    {
        PIDCONTENT pidContent = getPID(message);
        String birthDate = null;

        if (pidContent != null && pidContent.getPID7() != null && pidContent.getPID7().getTS1() != null)
        {
            birthDate = pidContent.getPID7().getTS1().getValue();
        }
        logger.debug("Patient DOB in incoming message is: " + birthDate);
        if (birthDate == null || birthDate.trim().isEmpty() || birthDate.equals("\"\""))
        {
            return null;
        }
        else
        {
            return Utils.convertStringToXMLGregorianCalendar(birthDate,ConfigurationLoader.getFieldConfigurations(
                  null,null,"PID7").toString());
        }
    }

     /**
     * This method parses the incoming message to get the patient's gender from PID8 segment.
     * @param message
     * @return String
     * @throws Exception
     * For A08/A31 if Gender passed is  <urn:PID.8></urn:PID.8> then retain the existing value in the database if exist
     * and if the value passed is  <urn:PID.8>""</urn:PID.8> then update the database with null.
     */

    public static String getGenderInformationFromMessage(Object message) throws Exception{
        PIDCONTENT pidContent = getPID(message);
        String gender = null;
        GenderType genderType = null;
        if (pidContent != null && pidContent.getPID8() != null)
        {
            gender = pidContent.getPID8().getValue();
        }
        return gender = processFieldValue(gender);

    }

    /**
     * This method parses the incoming message to get the patient's gender from PID8 segment.
     * @param message
     * @return patient's gender
     * @throws Exception
     */
    public static GenderType getPatientGender(Object message) throws Exception
    {
        PIDCONTENT pidContent = getPID(message);
        String gender = null;
        GenderType genderType = null;

        if (pidContent != null && pidContent.getPID8() != null)
        {
            gender = pidContent.getPID8().getValue();
        }

        gender = processFieldValue(gender);

        if (gender != null && gender.trim().length() > 0 && !gender.equals("\"\""))
        {
            try
            {

                if (gender.equalsIgnoreCase(ConfigurationLoader.getApplicationConfigValueById(ConfigKeyEnum.FEMALE.
                        toString())))
                {
                    genderType = GenderType.fromValue(GenderType.FEMALE.value());
                }
                else if (gender.equalsIgnoreCase(ConfigurationLoader.getApplicationConfigValueById(ConfigKeyEnum.MALE.
                        toString())))
                {
                    genderType = GenderType.fromValue(GenderType.MALE.value());
                }
            }
            catch (IllegalArgumentException ex)
            {
                genderType = null;
                logger.debug("Patient Gender in incoming message is not Male/Female. Gender will not be persisted.");
            }
        }
        else
        {
            genderType = null;
        }
        logger.debug("Patient Gender in incoming message is: " + gender);
        return genderType;
    }

    /**
     * This method parses the incoming message to get patient's address.
     * This method takes the first PID11 segment when multiple PID11s exist.
     * @param message
     * @return address type
     * @throws Exception
     */
    public static AddressType getPatientAddress(Object message) throws Exception
    {
        AddressType address = new AddressType();
        PIDCONTENT pidContent = getPID(message);
        List<PID11CONTENT> pid11s = null;

        if (pidContent != null)
        {
            pid11s = pidContent.getPID11();
        }

        for (PID11CONTENT pid11 : pid11s)
        {
            // House number is ignored as address cannot be parsed. address 1 and 2 are added to street list
            address.getStreet().add(getAddress1(pid11));
            address.getStreet().add(getAddress2(pid11));
            address.setCity(getCity(pid11));
            address.setState(getState(pid11));
            address.setZipCode(getZipCode(pid11));
            address.setCountry(getCountry(pid11));

            break;
        }
        logger.debug("Patient Address Type is created.");
        return address;
    }

    private static String getAddress1(PID11CONTENT pid11)
    {
        String address1 = null;
        if (pid11.getXAD1() != null && pid11.getXAD1().getSAD1() != null) {
            address1 = pid11.getXAD1().getSAD1().getValue().isEmpty()?null:pid11.getXAD1().getSAD1().getValue();
        }

        if (address1 != null && !address1.trim().equals("")) {
            address1 = processFieldValue(address1);
            logger.debug("Patient Address1 is : " + address1);
            int addressLength = Integer.parseInt(ConfigurationLoader.getApplicationConfigValueById(ConfigKeyEnum.ADDRESS_LENGTH.
                    toString()));
            if (address1 != null)
            {
                address1 = address1.length() > addressLength ? address1.substring(0, addressLength) : address1;
            }
             logger.debug("Patient Address1 after adjusting length: " + address1);
        }
        return address1;
    }

    private static String getAddress2(PID11CONTENT pid11)
    {
        String address2 = null;
        if (pid11.getXAD2() != null) {
            address2 = pid11.getXAD2().getValue().isEmpty()?null:pid11.getXAD2().getValue();
        }

        if (address2 != null && !address2.trim().equals(""))
        {
            address2 = processFieldValue(address2);
            logger.debug("Patient Address2 is : " + address2);
            int addressLength = Integer.parseInt(ConfigurationLoader.getApplicationConfigValueById(ConfigKeyEnum.ADDRESS_LENGTH.
                    toString()));
            if (address2 != null)
            {
                address2 = address2.length() > addressLength ? address2.substring(0, addressLength) : address2;
            }
            logger.debug("Patient Address2 after adjusting length: " + address2);
        }
        return address2;
    }

    private static String getCity(PID11CONTENT pid11)
    {
        String city = null;
        if (pid11.getXAD3() != null)
        {
            city = pid11.getXAD3().getValue();
        }

        city = processFieldValue(city);
        logger.debug("Patient City is: " + city);
        int addressLength = Integer.parseInt(ConfigurationLoader.getApplicationConfigValueById(ConfigKeyEnum.CITY_LENGTH.
                toString()));
        if (city != null)
        {
            city = city.length() > addressLength ? city.substring(0, addressLength) : city;
            logger.info("Patient city exceeds the configured length and hence trimming it : " + city);
        }
        return city;
    }

    private static String getState(PID11CONTENT pid11)
    {
        String state = null;
        if (pid11.getXAD4() != null)
        {
            state = pid11.getXAD4().getValue();
        }

        state = processFieldValue(state);
        logger.debug("Patient State is: " + state);
        return state;
    }

    private static String getZipCode(PID11CONTENT pid11)
    {
        String zipCode = null;
        if (pid11.getXAD5() != null)
        {
            zipCode = pid11.getXAD5().getValue();
        }

        zipCode = processFieldValue(zipCode);
        logger.debug("Patient Zipcode is: " + zipCode);
        return zipCode;
    }

    private static String getCountry(PID11CONTENT pid11)
    {
        String country = null;
        if (pid11.getXAD6() != null)
        {
            country = pid11.getXAD6().getValue();
        }

        country = processFieldValue(country);
        logger.debug("Patient Country is: " + country);
        return country;
    }

    /**
     * This method parses the incoming message to get patient phone number from pid13 segment.
     * This method takes the first PID13 segment when multiple PID13s exist.
     * @param message
     * @return patient phone number
     * @throws Exception
     */
    public static PhoneNumberType getPatientPhoneNumber(Object message) throws Exception
    {
        PhoneNumberType phoneNumberType = new PhoneNumberType();
        PIDCONTENT pidContent = getPID(message);
        List<PID13CONTENT> pid13s = null;

        if (pidContent != null)
        {
            pid13s = pidContent.getPID13();
        }

        String phoneNumber = null;

        for (PID13CONTENT pid13 : pid13s)
        {
            XTN1CONTENT xtn1Content = pid13.getXTN1();
            if (xtn1Content != null)
            {
                phoneNumber = xtn1Content.getValue();
            }
            //fix for INT-554--- only first number to accept
            break;
        }

        phoneNumber = processFieldValue(phoneNumber);
        logger.debug("Patient Phone Number is: " + phoneNumber);
        phoneNumberType.setNumber(phoneNumber);
        return phoneNumberType;
    }

    /******************UAC Helper Methods*************************/
    public static UACCONTENT getUAC(Object message) throws Exception
    {
        UACCONTENT uacContent = null;
        Method method = message.getClass().getMethod("getUAC");
        if (method != null)
        {
            uacContent = (UACCONTENT) method.invoke(message);
        }
        return uacContent;
    }

    public static String getUserName(Object message) throws Exception
    {
        UACCONTENT uacContent = getUAC(message);
        String userName = null;
        if (uacContent != null && uacContent.getUAC2() != null && uacContent.getUAC2().getED5() != null && uacContent.
                getUAC2().getED5().getUNT1() != null)
        {
            userName = uacContent.getUAC2().getED5().getUNT1().getValue();
        }
        logger.debug("User Name in incoming message is: " + userName);
        return userName;
    }

    public static String getPassword(Object message) throws Exception
    {
        UACCONTENT uacContent = getUAC(message);
        String password = null;
        if (uacContent != null && uacContent.getUAC2() != null && uacContent.getUAC2().getED5() != null && uacContent.
                getUAC2().getED5().getUNT2() != null)
        {
            password = uacContent.getUAC2().getED5().getUNT2().getValue();
        }
        return password;
    }

    public static Long getInternalOrganizationList(OrganizationType signupOrg) throws ProcessException
    {
        Long internalIdentifier = null;
        try
        {
            String internalOrgId = null;
            if (signupOrg != null)
            {
                if (signupOrg.getInternalOrganization() != null)
                {
                    internalOrgId = signupOrg.getInternalOrganization().get(0).getOrganizationIdentifier();
                    internalIdentifier = Long.parseLong(internalOrgId);
                }
            }
        }
        catch (Exception ex)
        {
            logger.error("Exception occurred while extracting the internal organisation id", ex);
            throw new ProcessException("Exception occurred while extracting the internal organisation id", ex);
        }
        return internalIdentifier;
    }

    public static String getExistingPatIdentifier(IntegrationPatientType existingPatient, String patId) {
		String patValue = null;
		if(existingPatient!=null){
			List<ExternalIdentifierType> extIdentifiers = existingPatient.getIdentifier().getExternalIdentifier();
			for (ExternalIdentifierType identifier : extIdentifiers) {
				if (identifier.getName().equalsIgnoreCase(patId)) {
					patValue = identifier.getValue();
					break;
				}
			}
		}
		return patValue;
	}

	public static String getMessagePatIdentifier(List<ExternalIdentifierType> messageIdentifiers, String patId) {
		String patValue = null;
		if(messageIdentifiers!=null && !messageIdentifiers.isEmpty()){
			for (ExternalIdentifierType identifier : messageIdentifiers) {
				if (identifier.getName().equalsIgnoreCase(patId)) {
					patValue = identifier.getValue();
					break;
				}
			}
		}
		return patValue;
	}

    public  static String getOnlyNumeric(String ssn){
        StringBuffer numberString = new StringBuffer();
        char c;
        if(ssn!=null && !ssn.trim().isEmpty()){
            for(int i=0;i<ssn.length();i++){
                 c = ssn.charAt(i);
                 if(Character.isDigit(c)){
                     numberString.append(c);
                 }
            }

        }

        return numberString.toString();

    }
}
