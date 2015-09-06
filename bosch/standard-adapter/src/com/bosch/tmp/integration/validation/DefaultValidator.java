package com.bosch.tmp.integration.validation;

import com.bosch.tmp.integration.util.Error;
import com.bosch.tmp.integration.util.MessageUtils;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.hl7.v2xml.ACKBATCHCONTENT;
import org.hl7.v2xml.ADTA01CONTENT;
import org.hl7.v2xml.ADTA03CONTENT;
import org.hl7.v2xml.ADTA05CONTENT;
import org.hl7.v2xml.AnyHL7SegmentTYPE;
import org.hl7.v2xml.BHS7CONTENT;
import org.hl7.v2xml.BHSCONTENT;
import org.hl7.v2xml.BTSCONTENT;
import org.hl7.v2xml.EVNCONTENT;
import org.hl7.v2xml.MSACONTENT;
import org.hl7.v2xml.MSH11CONTENT;
import org.hl7.v2xml.MSH12CONTENT;
import org.hl7.v2xml.MSH3CONTENT;
import org.hl7.v2xml.MSH4CONTENT;
import org.hl7.v2xml.MSH5CONTENT;
import org.hl7.v2xml.MSH6CONTENT;
import org.hl7.v2xml.MSH7CONTENT;
import org.hl7.v2xml.MSH9CONTENT;
import org.hl7.v2xml.MSHCONTENT;
import org.hl7.v2xml.PID3CONTENT;
import org.hl7.v2xml.PID5CONTENT;
import org.hl7.v2xml.PID7CONTENT;
import org.hl7.v2xml.PID11CONTENT;
import org.hl7.v2xml.PID13CONTENT;
import org.hl7.v2xml.PIDCONTENT;
import org.hl7.v2xml.PV1CONTENT;
import org.hl7.v2xml.QVRQ17CONTENT;
import org.hl7.v2xml.UAC2CONTENT;
import org.hl7.v2xml.UACCONTENT;
import org.hl7.v2xml.QPD9CONTENT;
import org.hl7.v2xml.QPDCONTENT;
import java.util.ArrayList;
import org.hl7.v2xml.ORUR01BATCHCONTENT;
import org.hl7.v2xml.ORUR30BATCHCONTENT;
import com.bosch.tmp.integration.validation.QueryCodeChecker;

/**
 * DefaultValidator performs all the generic validations. The validation approach is data driven.
 * For all the incoming messages datatypes, fields, segments are validated and finally message level validations are performed.
 *
 * @author tis1pal
 */
public class DefaultValidator implements Validator
{

    public static final Logger logger = LoggerFactory.getLogger(DefaultValidator.class);
    
    private Error error = null;
    
    @Override
	public void setErrorObject(Error error) {
		this.error = error;
		
	}

    public void validateMessage(Object message) throws ValidationException
    {
        if (message == null)
        {
            return;
        }
        //This method is used to do the first level validations For MSH and MSH9
        preValidate(message);
        String messageType = MessageUtils.getMessageNameByTriggerEvent(message);
        if (messageType == null)
        {
            return;
        }
        if (message instanceof ADTA01CONTENT)
        {
            ADTA01CONTENT msg = (ADTA01CONTENT) message;
            logger.info("****************Message type is ADT_A01***********" + msg);
            if (messageType.equalsIgnoreCase("ADT_A04"))
            {
                logger.info("****************Message trigger is A04***********" + messageType);
                List<Validation> mshValidation = ConfigurationLoader.getSegmentValidations(messageType, "MSH");
                validateSegment(messageType, msg.getMSH(), mshValidation);
                mshValidation = null;

                List<PIDCONTENT> pidList = msg.getPID();
                List<Validation> pidValidation = ConfigurationLoader.getSegmentValidations(messageType, "PID");
                if (pidList.isEmpty())
                {
                    validateSegment(messageType, pidList, pidValidation);
                    pidValidation = null;
                }
                for (PIDCONTENT pid : pidList)
                {
                    validateSegment(messageType, pid, pidValidation);
                    pidValidation = null;
                    break;
                }
                List<EVNCONTENT> evnList = msg.getEVN();
                List<Validation> evnValidation = ConfigurationLoader.getSegmentValidations(messageType, "EVN");
                if (evnList.isEmpty())
                {
                    validateSegment(messageType, evnList, evnValidation);
                    evnValidation = null;
                }
                for (EVNCONTENT evn : evnList)
                {

                    validateSegment(messageType, evn, evnValidation);
                    evnValidation = null;
                    break;
                }
                List<PV1CONTENT> pv1List = msg.getPV1();
                List<Validation> pv1Validation = ConfigurationLoader.getSegmentValidations(messageType, "PV1");
                if (pv1List.isEmpty())
                {
                    validateSegment(messageType, pv1List, pv1Validation);
                    pv1Validation = null;
                }
                for (PV1CONTENT pv1 : pv1List)
                {
                    validateSegment(messageType, pv1, pv1Validation);
                    pv1Validation = null;
                    break;
                }
                List<Validation> uacValidation = ConfigurationLoader.getSegmentValidations(messageType, "UAC");
                validateSegment(messageType, msg.getUAC(), uacValidation);
                uacValidation = null;
            }
            else if (messageType.equalsIgnoreCase("ADT_A08"))
            {
                logger.info("****************Message trigger is A08***********" + messageType);
                List<Validation> mshValidation = ConfigurationLoader.getSegmentValidations(messageType, "MSH");
                validateSegment(messageType, msg.getMSH(), mshValidation);
                mshValidation = null;

                List<PIDCONTENT> pidList = msg.getPID();
                List<Validation> pidValidation = ConfigurationLoader.getSegmentValidations(messageType, "PID");
                if (pidList.isEmpty())
                {
                    validateSegment(messageType, pidList, pidValidation);
                    pidValidation = null;
                }
                for (PIDCONTENT pid : pidList)
                {
                    validateSegment(messageType, pid, pidValidation);
                    pidValidation = null;
                    break;
                }
                List<EVNCONTENT> evnList = msg.getEVN();
                List<Validation> evnValidation = ConfigurationLoader.getSegmentValidations(messageType, "EVN");
                if (evnList.isEmpty())
                {
                    validateSegment(messageType, evnList, evnValidation);
                    evnValidation = null;
                }
                for (EVNCONTENT evn : evnList)
                {

                    validateSegment(messageType, evn, evnValidation);
                    evnValidation = null;
                    break;
                }
                List<Validation> uacValidation = ConfigurationLoader.getSegmentValidations(messageType, "UAC");
                validateSegment(messageType, msg.getUAC(), uacValidation);
                uacValidation = null;
            }
        }
        else if (message instanceof ADTA05CONTENT)
        {
            ADTA05CONTENT msg = (ADTA05CONTENT) message;
            logger.info("****************Message type is ADT_A05***********" + msg);
            if (messageType.equalsIgnoreCase("ADT_A31"))
            {
                logger.info("****************Message trigger is A31***********" + messageType);
                List<Validation> mshValidation = ConfigurationLoader.getSegmentValidations(messageType, "MSH");
                validateSegment(messageType, msg.getMSH(), mshValidation);
                mshValidation = null;

                List<Validation> pidValidation = ConfigurationLoader.getSegmentValidations(messageType, "PID");
                validateSegment(messageType, msg.getPID(), pidValidation);
                pidValidation = null;

                List<Validation> evnValidation = ConfigurationLoader.getSegmentValidations(messageType, "EVN");
                validateSegment(messageType, msg.getEVN(), evnValidation);
                evnValidation = null;

                List<Validation> uacValidation = ConfigurationLoader.getSegmentValidations(messageType, "UAC");
                validateSegment(messageType, msg.getUAC(), uacValidation);
                uacValidation = null;
            }
        }
        else if (message instanceof ADTA03CONTENT)
        {
            ADTA03CONTENT msg = (ADTA03CONTENT) message;
            logger.info("****************Message type is ADT_A03***********" + msg);
            if (messageType.equalsIgnoreCase("ADT_A03"))
            {
                logger.info("****************Message trigger is A31***********" + messageType);
                List<Validation> mshValidation = ConfigurationLoader.getSegmentValidations(messageType, "MSH");
                validateSegment(messageType, msg.getMSH(), mshValidation);
                mshValidation = null;

                List<PIDCONTENT> pidList = msg.getPID();
                List<Validation> pidValidation = ConfigurationLoader.getSegmentValidations(messageType, "PID");
                if (pidList.isEmpty())
                {
                    validateSegment(messageType, pidList, pidValidation);
                    pidValidation = null;
                }
                for (PIDCONTENT pid : pidList)
                {
                    validateSegment(messageType, pid, pidValidation);
                    pidValidation = null;
                    break;
                }

                List<EVNCONTENT> evnList = msg.getEVN();
                List<Validation> evnValidation = ConfigurationLoader.getSegmentValidations(messageType, "EVN");
                if (evnList.isEmpty())
                {
                    validateSegment(messageType, evnList, evnValidation);
                    evnValidation = null;
                }
                for (EVNCONTENT evn : evnList)
                {

                    validateSegment(messageType, evn, evnValidation);
                    evnValidation = null;
                    break;
                }






                List<PV1CONTENT> pv1List = msg.getPV1();

                List<Validation> pv1Validation = ConfigurationLoader.getSegmentValidations(messageType, "PV1");
                if (pv1List.isEmpty())
                {
                    validateSegment(messageType, pv1List, pv1Validation);
                    pv1Validation = null;
                }
                for (PV1CONTENT pv1 : pv1List)
                {

                    validateSegment(messageType, pv1, pv1Validation);
                    pv1Validation = null;
                    break;
                }

                List<Validation> uacValidation = ConfigurationLoader.getSegmentValidations(messageType, "UAC");
                validateSegment(messageType, msg.getUAC(), uacValidation);
                uacValidation = null;
            }
        }
        else if (message instanceof QVRQ17CONTENT)
        {
            QVRQ17CONTENT msg = (QVRQ17CONTENT) message;
            logger.info("****************Message type is QVR_Q17***********" + msg);
            if (messageType.equalsIgnoreCase("QVR_Q17"))
            {
                logger.info("****************Message trigger is Q17***********" + messageType);

                List<Validation> mshValidation = ConfigurationLoader.getSegmentValidations(messageType, "MSH");
                validateSegment(messageType, msg.getMSH(), mshValidation);
                mshValidation = null;


                List<Validation> uacValidation = ConfigurationLoader.getSegmentValidations(messageType, "UAC");
                validateSegment(messageType, msg.getUAC(), uacValidation);
                uacValidation = null;

                List<Validation> rcpValidation = ConfigurationLoader.getSegmentValidations(messageType, "RCP");
                validateSegment(messageType, msg.getRCP(), rcpValidation);
                rcpValidation = null;


                List<Validation> qpdValidation = ConfigurationLoader.getSegmentValidations(messageType, "QPD");
                validateSegment(messageType, msg.getQPD(), qpdValidation);
                qpdValidation = null;
            }
        }
        else if (message instanceof ACKBATCHCONTENT)
        {
            //TODO need to know how to validate ACK messages. Rethink ACK Batch validation and messagetype method might need to change.
            ACKBATCHCONTENT msg = (ACKBATCHCONTENT) message;
            logger.info("****************Message type is ACK_BATCH***********" + msg);
            if (messageType.equalsIgnoreCase("ACK"))
            {
                messageType = "ACK";
                logger.info("****************Message trigger is ACK***********" + messageType);

                List<Validation> bhsValidation = ConfigurationLoader.getSegmentValidations(messageType, "BHS");
                validateSegment(messageType, msg.getBHS(), bhsValidation);
                bhsValidation = null;

                List<Validation> btsValidation = ConfigurationLoader.getSegmentValidations(messageType, "BTS");
                validateSegment(messageType, msg.getBHS(), btsValidation);
                btsValidation = null;
            }
        }
        else if (message instanceof AnyHL7SegmentTYPE)
        {

            AnyHL7SegmentTYPE msg = (AnyHL7SegmentTYPE) message;
            //TODO: Add Error and throw ValidationException here.
            throw new ValidationException("Unsupported message type");
        }
        if (error.hasErrors())
        {
            return;
        }
        List<Validation> validations = ConfigurationLoader.getMessageValidations(messageType);
        if (validations == null)
        {
            return;
        }
        logger.debug("Validating Message with MessageType: " + messageType);
        performValidations(message, validations);
        //Nullifying the list so the heap space with be saved
        validations = null;
    }

    public void validateSegment(String messageType, Object segment, List<Validation> validations) throws
            ValidationException
    {
        if (error.hasErrors())
        {
            return;
        }
        logger.debug("Validating Segment with messageType: " + messageType + " SegmentType:  " + segment);
        if (segment != null)
        {
            if (segment instanceof MSHCONTENT)
            {
                MSHCONTENT seg = (MSHCONTENT) segment;
                logger.debug("Validating MSH Segment: " + seg);
                validateField(messageType, "MSH", seg.getMSH1(), ConfigurationLoader.getFieldValidations(messageType,
                        "MSH", "MSH1"));
                validateField(messageType, "MSH", seg.getMSH2(), ConfigurationLoader.getFieldValidations(messageType,
                        "MSH", "MSH2"));
                validateField(messageType, "MSH", seg.getMSH3(), ConfigurationLoader.getFieldValidations(messageType,
                        "MSH", "MSH3"));
                validateField(messageType, "MSH", seg.getMSH4(), ConfigurationLoader.getFieldValidations(messageType,
                        "MSH", "MSH4"));
                validateField(messageType, "MSH", seg.getMSH5(), ConfigurationLoader.getFieldValidations(messageType,
                        "MSH", "MSH5"));
                validateField(messageType, "MSH", seg.getMSH6(), ConfigurationLoader.getFieldValidations(messageType,
                        "MSH", "MSH6"));
                validateField(messageType, "MSH", seg.getMSH7(), ConfigurationLoader.getFieldValidations(messageType,
                        "MSH", "MSH7"));
//                validateField(messageType, "MSH", seg.getMSH9(), ConfigurationLoader.getFieldValidations(messageType,
//                        "MSH", "MSH9"));
                validateField(messageType, "MSH", seg.getMSH10(), ConfigurationLoader.getFieldValidations(messageType,
                        "MSH", "MSH10"));
                validateField(messageType, "MSH", seg.getMSH11(), ConfigurationLoader.getFieldValidations(messageType,
                        "MSH", "MSH11"));
                validateField(messageType, "MSH", seg.getMSH12(), ConfigurationLoader.getFieldValidations(messageType,
                        "MSH", "MSH12"));
                validateField(messageType, "MSH", seg.getMSH16(), ConfigurationLoader.getFieldValidations(messageType,
                        "MSH", "MSH16"));
            }
            else if (segment instanceof PIDCONTENT)
            {
                PIDCONTENT seg = (PIDCONTENT) segment;
                logger.debug("Validating PID Segement: " + seg);
                List<PID3CONTENT> pid3List = seg.getPID3();
                if (pid3List.isEmpty())
                {
                    validateField(messageType, "PID", null, ConfigurationLoader.getFieldValidations(messageType, "PID",
                            "PID3"));
                }
                //Invoking MissingIdentifier Checker
                Validation missingIdval = ConfigurationLoader.getSegmentValidationByType(messageType, "PID",
                        Type.MISSING_IDENTIFIER);
                if (missingIdval != null)
                {
                    performValidation(seg, missingIdval);
                }
                //This is an exception.We only invoke pattern checker in case of PID3

                List<String> patternAlreadyValidated = new ArrayList<String>();
               
                for (PID3CONTENT pid3 : pid3List)
                {
                    boolean  notValidated = true;
                    if (pid3.getCX5() != null && pid3.getCX5().getValue() != null && pid3.getCX1() != null && pid3.
                            getCX1().getValue() != null && !pid3.getCX1().getValue().isEmpty())
                    {
                        //INT562 --it should validate only for the first instance of Identification type
                        if (patternAlreadyValidated != null && !patternAlreadyValidated.isEmpty())
                        {
                            for (String validatedPattern : patternAlreadyValidated)
                            {
                                if (validatedPattern.equals(pid3.getCX5().getValue()))
                                {
                                    //int 562
                                    //set False if the same pattern(e.g PS or SS) is already validated
                                    notValidated=false;
                                    break;
                                }
                            }
                        }
                        Validation patternVal = ConfigurationLoader.getFieldValidationsByTypeAndID(messageType, "PID",
                                "PID3", pid3.getCX5().getValue(), Type.PATTERN);
                        if (patternVal != null && notValidated)
                        {
                            performValidation(pid3.getCX1(), patternVal);
                            patternAlreadyValidated.add((String)pid3.getCX5().getValue());
                        }
                    }
                }
                pid3List = null;
                List<PID5CONTENT> pid5List = seg.getPID5();
                if (pid5List.isEmpty())
                {
                    validateField(messageType, "PID", null, ConfigurationLoader.getFieldValidations(messageType,
                            "PID", "PID5"));
                }
                // In SA first occurance of the PID5 (last name) is validated and rest of the repeating fields are ignored.
                for (PID5CONTENT pid5 : pid5List)
                {
                    validateField(messageType, "PID", pid5, ConfigurationLoader.getFieldValidations(messageType, "PID",
                            "PID5"));
                    break;
                }
                pid5List = null;
                validateField(messageType, "PID", seg.getPID7(), ConfigurationLoader.getFieldValidations(messageType,
                        "PID", "PID7"));

                List<PID11CONTENT> pid11List = seg.getPID11();
                if (pid11List.isEmpty())
                {
                    validateField(messageType, "PID", seg.getPID11(), ConfigurationLoader.getFieldValidations(
                            messageType,
                            "PID", "PID11"));
                }
                for (PID11CONTENT pid11 : pid11List)
                {
                    validateField(messageType, "PID", pid11, ConfigurationLoader.getFieldValidations(messageType, "PID",
                            "PID11"));
                    break;
                }
                pid11List = null;

                List<PID13CONTENT> pid13List = seg.getPID13();
                if (pid13List.isEmpty())
                {
                    validateField(messageType, "PID", seg.getPID13(), ConfigurationLoader.getFieldValidations(
                            messageType,
                            "PID", "PID13"));
                }
                for (PID13CONTENT pid13 : pid13List)
                {
                    validateField(messageType, "PID", pid13, ConfigurationLoader.getFieldValidations(messageType, "PID",
                            "PID13"));
                    break;
                }
                pid13List = null;
            }
            else if (segment instanceof UACCONTENT)
            {
                UACCONTENT seg = (UACCONTENT) segment;
                logger.debug("Validating UAC Segement: " + seg);
                validateField(messageType, "UAC", seg.getUAC2(), ConfigurationLoader.getFieldValidations(messageType,
                        "UAC", "UAC2"));
            }
            else if (segment instanceof MSACONTENT)
            {
                MSACONTENT seg = (MSACONTENT) segment;
                logger.debug("Validating MSA Segement: " + seg);
                validateField(messageType, "MSA", seg.getMSA1(), ConfigurationLoader.getFieldValidations(messageType,
                        "MSA", "MSA1"));
                validateField(messageType, "MSA", seg.getMSA2(), ConfigurationLoader.getFieldValidations(messageType,
                        "MSA", "MSA2"));
                validateField(messageType, "MSA", seg.getMSA3(), ConfigurationLoader.getFieldValidations(messageType,
                        "MSA", "MSA3"));
            }
            else if (segment instanceof BHSCONTENT)
            {
                BHSCONTENT seg = (BHSCONTENT) segment;
                logger.debug("Validating BHS Segement: " + seg);
                validateField(messageType, "BHS", seg.getBHS1(), ConfigurationLoader.getFieldValidations(messageType,
                        "BHS", "BHS1"));
                validateField(messageType, "BHS", seg.getBHS2(), ConfigurationLoader.getFieldValidations(messageType,
                        "BHS", "BHS2"));
                validateField(messageType, "BHS", seg.getBHS3(), ConfigurationLoader.getFieldValidations(messageType,
                        "BHS", "BHS3"));
                validateField(messageType, "BHS", seg.getBHS4(), ConfigurationLoader.getFieldValidations(messageType,
                        "BHS", "BHS4"));
                validateField(messageType, "BHS", seg.getBHS5(), ConfigurationLoader.getFieldValidations(messageType,
                        "BHS", "BHS5"));
                validateField(messageType, "BHS", seg.getBHS6(), ConfigurationLoader.getFieldValidations(messageType,
                        "BHS", "BHS6"));
                validateField(messageType, "BHS", seg.getBHS7(), ConfigurationLoader.getFieldValidations(messageType,
                        "BHS", "BHS7"));
                validateField(messageType, "BHS", seg.getBHS9(), ConfigurationLoader.getFieldValidations(messageType,
                        "BHS", "BHS9"));
                validateField(messageType, "BHS", seg.getBHS11(), ConfigurationLoader.getFieldValidations(messageType,
                        "BHS", "BHS11"));
                validateField(messageType, "BHS", seg.getBHS12(), ConfigurationLoader.getFieldValidations(messageType,
                        "BHS", "BHS12"));
            }
            else if (segment instanceof BTSCONTENT)
            {
                BTSCONTENT seg = (BTSCONTENT) segment;
                logger.debug("Validating BTS Segement: " + seg);
                validateField(messageType, "BTS", seg.getBTS1(), ConfigurationLoader.getFieldValidations(messageType,
                        "BTS", "BTS1"));
            }
            else if (segment instanceof QPDCONTENT)
            {
                QPDCONTENT seg = (QPDCONTENT)segment;
                logger.debug("Validating QPD Segement: " + seg);
                List<QPD9CONTENT> qpd9List = seg.getQPD9();
                if (qpd9List.isEmpty())
                {
                    validateField(messageType, "QPD", null, ConfigurationLoader.getFieldValidations(messageType,
                            "QPD", "QPD9"));
                }
                // In SA first occurance of the PID5 (last name) is validated and rest of the repeating fields are ignored.
                for (QPD9CONTENT qpd9 : qpd9List)
                {
                    validateField(messageType, "QPD", qpd9, ConfigurationLoader.getFieldValidations(messageType, "QPD",
                            "QPD9"));
                    break;
                }
                qpd9List = null;
            }
            
        }
        if (validations != null)
        {
            performValidations(segment, validations);
        }

    }

    public void validateDataType(String messageType, String segmentType, String fieldType, Object dataType,
            List<Validation> validations) throws ValidationException
    {
        if (error.hasErrors())
        {
            return;
        }
        if (validations == null)
        {
            return;
        }
        logger.debug(
                "Validating DataType with MessageType: " + messageType + "SegmentType:  " + segmentType + " fieldType: " + fieldType + "dataType: " + dataType);
        performValidations(dataType, validations);
    }

    public void validateField(String messageType, String segmentType, Object field, List<Validation> validations) throws
            ValidationException
    {
        if (error.hasErrors())
        {
            return;
        }

        logger.debug(
                "Validating Field with MessageType: " + messageType + "SegmentType:  " + segmentType + " field: " + field);
        if (field != null)
        {
            if (field instanceof MSH3CONTENT)
            {
                MSH3CONTENT fld = (MSH3CONTENT) field;
                logger.debug("Validating MSH3 Field: " + fld);
                validateDataType(messageType, segmentType, "MSH3", fld.getHD1(), ConfigurationLoader.
                        getDataTypeValidations(
                        messageType, segmentType, "MSH3", "HD1"));
            }
            if (field instanceof MSH4CONTENT)
            {
                MSH4CONTENT fld = (MSH4CONTENT) field;
                logger.debug("Validating MSH4 Field: " + fld);
                validateDataType(messageType, segmentType, "MSH4", fld.getHD1(), ConfigurationLoader.
                        getDataTypeValidations(
                        messageType, segmentType, "MSH4", "HD1"));
            }
            if (field instanceof MSH5CONTENT)
            {
                MSH5CONTENT fld = (MSH5CONTENT) field;
                logger.debug("Validating MSH5 Field: " + fld);
                validateDataType(messageType, segmentType, "MSH5", fld.getHD1(), ConfigurationLoader.
                        getDataTypeValidations(
                        messageType, segmentType, "MSH5", "HD1"));
            }
            if (field instanceof MSH6CONTENT)
            {
                MSH6CONTENT fld = (MSH6CONTENT) field;
                logger.debug("Validating MSH6 Field: " + fld);
                validateDataType(messageType, segmentType, "MSH6", fld.getHD1(), ConfigurationLoader.
                        getDataTypeValidations(
                        messageType, segmentType, "MSH6", "HD1"));
            }
            if (field instanceof MSH7CONTENT)
            {
                MSH7CONTENT fld = (MSH7CONTENT) field;
                logger.debug("Validating MSH7 Field: " + fld);
                validateDataType(messageType, segmentType, "MSH7", fld.getTS1(), ConfigurationLoader.
                        getDataTypeValidations(
                        messageType, segmentType, "MSH7", "TS1"));
            }
            if (field instanceof MSH9CONTENT)
            {
                MSH9CONTENT fld = (MSH9CONTENT) field;
                logger.debug("Validating MSH9 Field: " + fld);
                validateDataType(messageType, segmentType, "MSH9", fld.getMSG1(), ConfigurationLoader.
                        getDataTypeValidations(
                        messageType, segmentType, "MSH9", "MSG1"));
                validateDataType(messageType, segmentType, "MSH9", fld.getMSG2(), ConfigurationLoader.
                        getDataTypeValidations(
                        messageType, segmentType, "MSH9", "MSG2"));
                validateDataType(messageType, segmentType, "MSH9", fld.getMSG3(), ConfigurationLoader.
                        getDataTypeValidations(
                        messageType, segmentType, "MSH9", "MSG3"));
            }
            if (field instanceof MSH11CONTENT)
            {
                MSH11CONTENT fld = (MSH11CONTENT) field;
                logger.debug("Validating MSH11 Field: " + fld);
                validateDataType(messageType, segmentType, "MSH11", fld.getPT1(),
                        ConfigurationLoader.getDataTypeValidations(messageType, segmentType, "MSH11", "PT1"));
            }
            if (field instanceof MSH12CONTENT)
            {
                MSH12CONTENT fld = (MSH12CONTENT) field;
                logger.debug("Validating MSH12 Field: " + fld);
                validateDataType(messageType, segmentType, "MSH12", fld.getVID1(), ConfigurationLoader.
                        getDataTypeValidations(messageType, segmentType, "MSH12", "VID1"));
            }
            if (field instanceof PID5CONTENT)
            {
                PID5CONTENT fld = (PID5CONTENT) field;
                logger.debug("Validating PID5 Field: " + fld);
                if(fld.getXPN1()!=null && fld.getXPN1().getFN1()!=null){
                    logger.debug("Validating Last Name Field of PID5: " + fld);
                    validateDataType(messageType, segmentType, "PID5", fld.getXPN1().getFN1(), ConfigurationLoader.
                        getDataTypeValidations(messageType, segmentType, "PID5", "XPN1_FN1"));
                }else{
                    validateDataType(messageType, segmentType, "PID5", null, ConfigurationLoader.
                        getDataTypeValidations(messageType, segmentType, "PID5", "XPN1_FN1"));
                }

               // this method process the Frist Name Validation
               processFirstName(messageType,segmentType,fld);
               // this method process the Middle Name Validation
               processMiddleName(messageType,segmentType,fld);
                
              
            }
            if (field instanceof PID7CONTENT)
            {
                PID7CONTENT fld = (PID7CONTENT) field;
                logger.debug("Validating PID7 Field: " + fld);
                List<Validation> validationforMe=ConfigurationLoader.getDataTypeValidations(messageType, segmentType, "PID7", "TS1");
                validateDataType(messageType, segmentType, "PID7", fld.getTS1(), ConfigurationLoader.
                        getDataTypeValidations(
                        messageType, segmentType, "PID7", "TS1"));
            }
            if (field instanceof PID11CONTENT)
            {
                PID11CONTENT fld = (PID11CONTENT) field;
                logger.debug("Validating PID11 Field: " + fld);
                if (fld != null && fld.getXAD5() != null && fld.getXAD5().getValue().trim().length() > 0 && !fld.getXAD5().
                        getValue().equals("\"\""))
                {
                    validateDataType(messageType, segmentType, "PID11", fld.getXAD5(), ConfigurationLoader.
                            getDataTypeValidations(
                            messageType, segmentType, "PID11", "XAD5"));
                }
            }
            if (field instanceof PID13CONTENT)
            {
                PID13CONTENT fld = (PID13CONTENT) field;
                logger.debug("Validating PID13 Field: " + fld);
                if (fld != null && fld.getXTN1() != null && fld.getXTN1().getValue().trim().length() > 0 && !fld.getXTN1().
                        getValue().equals("\"\""))
                {
                    validateDataType(messageType, segmentType, "PID13", fld.getXTN1(), ConfigurationLoader.
                            getDataTypeValidations(
                            messageType, segmentType, "PID13", "XTN1"));
                }
            }
            if (field instanceof UAC2CONTENT)
            {
                UAC2CONTENT fld = (UAC2CONTENT) field;
                logger.debug("Validating UAC2 Field: " + fld);
                validateDataType(messageType, segmentType, "UAC2", fld.getED5(), ConfigurationLoader.
                        getDataTypeValidations(
                        messageType, segmentType, "UAC2", "ED5"));
                validateDataType(messageType, segmentType, "UAC2", fld.getED5().getUNT1(), ConfigurationLoader.
                        getDataTypeValidations(
                        messageType, segmentType, "UAC2", "ED5_UNT1"));
                validateDataType(messageType, segmentType, "UAC2", fld.getED5().getUNT2(), ConfigurationLoader.
                        getDataTypeValidations(
                        messageType, segmentType, "UAC2", "ED5_UNT2"));
            }
            if (field instanceof BHS7CONTENT)
            {
                BHS7CONTENT fld = (BHS7CONTENT) field;
                logger.debug("Validating BHS7 Field: " + fld);
                validateDataType(messageType, segmentType, "BHS7", fld.getTS1(), ConfigurationLoader.
                        getDataTypeValidations(
                        messageType, segmentType, "BHS7", "TS1"));
            }
            if (field instanceof BHS7CONTENT)
            {
                BHS7CONTENT fld = (BHS7CONTENT) field;
                logger.debug("Validating BHS7 Field: " + fld);
                validateDataType(messageType, segmentType, "BHS7", fld.getTS1(), ConfigurationLoader.
                        getDataTypeValidations(
                        messageType, segmentType, "BHS7", "TS1"));
            }
            if (field instanceof QPD9CONTENT)
            {
                QPD9CONTENT fld = (QPD9CONTENT) field;
                logger.debug("Validating QPD9 Field: " + fld);
                 validateDataType(messageType, segmentType, "QPD9", fld.getCX1(), ConfigurationLoader.
                        getDataTypeValidations(messageType, segmentType, "QPD9", "CX1"));
                validateDataType(messageType, segmentType, "QPD9", fld.getCX5(), ConfigurationLoader.
                        getDataTypeValidations(messageType, segmentType, "QPD9", "CX5"));

              

            }
        }
        if (validations != null)
        {
            performValidations(field, validations);
        }

    }

    /**
     * This method invokes performValidation for the list of all the validations
     * @param obj message
     * @param validations list of validations
     * @throws ValidationException
     */
    protected void performValidations(Object obj, List<Validation> validations) throws ValidationException
    {
        try
        {
            for (Validation validation : validations)
            {
                // ignore any validation with 'id' and with multiple faults
                if (validation.getId() != null || validation.getFaults() != null)
                {
                    continue;
                }
                performValidation(obj, validation);
            }
        }
        finally
        {
            validations = null;
        }
    }

    /**
     * This method invokes appropriate checker and creates the error list
     */
    protected void performValidation(Object obj, Validation validation) throws ValidationException
    {
        Checker checker = CheckerFactory.getChecker(validation.getType());
        boolean checked = checker.isChecked(obj, validation);
        if (((validation.getFault() != null) || (validation.getFaults() != null)) && !checked)
        {
            error.getErrorList().add(createError(validation, checker.getFaultId()));
            logger.error("Validation error: " + error.getError().getFaultInternalMessage());
            throw new ValidationException("Validation error: " + error.getError().getFaultInternalMessage());
        }
    }

    /**
     * This method is for Validation faults.
     * All the Message validation related faults needs to be accessed using this method
     */
    protected Error createError(Validation validation, String faultId)
    {
        Error error = new Error();
        if (validation.getFaults() != null)
        {
            if (faultId == null)
            {
                Fault fault = validation.getFaults().getFault().get(0);
                error = updateError(error, fault);
                logger.error(validation.getType() + ": " + "1:FaultExternalCode: " + fault.getFaultExternalCode() + ": FaultExternalMessage: " + fault.
                        getFaultExternalMessage());
                logger.error(validation.getType() + ": " + "1:FaultInternalCode: " + fault.getFaultInternalCode() + ": FaultInternalMessage: " + fault.
                        getFaultInternalMessage());
            }
            else
            {
                for (Fault fault : validation.getFaults().getFault())
                {
                    if (fault.getId().equals(faultId))
                    {
                        logger.error("Extracting Fault information from config file");
                        error = updateError(error, fault);
                        logger.error(validation.getType() + ": " + "2:FaultExternalCode: " + fault.getFaultExternalCode() + ": FaultExternalMessage: " + fault.
                                getFaultExternalMessage());
                        logger.error(validation.getType() + ": " + "2:FaultInternalCode: " + fault.getFaultInternalCode() + ": FaultInternalMessage: " + fault.
                                getFaultInternalMessage());
                    }
                }
            }
        }
        else if (validation.getFault() != null)
        {
            Fault fault = validation.getFault();
            error = updateError(error, fault);
            logger.error(validation.getType() + ": " + "3:FaultExternalCode: " + fault.getFaultExternalCode() + ": FaultExternalMessage: " + fault.
                    getFaultExternalMessage());
            logger.error(validation.getType() + ": " + "3:FaultInternalCode: " + fault.getFaultInternalCode() + ": FaultInternalMessage: " + fault.
                    getFaultInternalMessage());
        }
        return error;
    }
     /**
     * This method is for PreValidation of MSH segment for its presence .
     * Also message types will be validated here
     */
    private void preValidate(Object message) throws ValidationException
    {
		try
		{
			// Check whether the MSH/BHS segment is there otherwise throw error
			Object msgHeader = MessageUtils.getHL7MessageHeader(message);
			if (msgHeader == null) {
				if (message instanceof ACKBATCHCONTENT || message instanceof ORUR30BATCHCONTENT || message instanceof ORUR01BATCHCONTENT) {
					Validation missingBHS = ConfigurationLoader.getSegmentValidationByType(null, "BHS", Type.MANDATORY);
					if (missingBHS != null) {
						performValidation(null, missingBHS);
					}
				}
				else {
					Validation missingMSH = ConfigurationLoader.getSegmentValidationByType(null, "MSH", Type.MANDATORY);
					if (missingMSH != null) {
						performValidation(null, missingMSH);
					}
				}
			}

			MSHCONTENT mshContent = null;
			if (msgHeader instanceof MSHCONTENT){
				mshContent = (MSHCONTENT) msgHeader;
			}

			if(mshContent!=null) {
				// Using Java reflection to get the MSH for that specified object.
				Validation msg9MandatoryValidation = ConfigurationLoader.getFieldValidationByType(null, "MSH", "MSH9",Type.MANDATORY);
				if (msg9MandatoryValidation != null){
					performValidation(mshContent.getMSH9(), msg9MandatoryValidation);
				}

				Validation msg1MandatoryValidation = ConfigurationLoader.getDataTypeValidationByType(null, "MSH", "MSH9","MSG1", Type.MANDATORY);
				if (msg1MandatoryValidation != null){
					performValidation(mshContent.getMSH9().getMSG1(), msg1MandatoryValidation);
				}

				Validation msg2MandatoryValidation = ConfigurationLoader.getDataTypeValidationByType(null, "MSH", "MSH9","MSG2", Type.MANDATORY);
				if (msg2MandatoryValidation != null){
					performValidation(mshContent.getMSH9().getMSG2(), msg2MandatoryValidation);
				}

				Validation msg3MandatoryValidation = ConfigurationLoader.getDataTypeValidationByType(null, "MSH", "MSH9","MSG3", Type.MANDATORY);
				if (msg1MandatoryValidation != null){
					performValidation(mshContent.getMSH9().getMSG3(), msg3MandatoryValidation);
				}

				Validation unsupportedTriggervalidation = ConfigurationLoader.getFieldValidationByType(null, "MSH", "MSH9",Type.UNSUPPORTED_TRIGGER);
				if (unsupportedTriggervalidation != null){
					performValidation(mshContent.getMSH9(), unsupportedTriggervalidation);
				}
			}
		}
		catch (Exception ex)
		{
			logger.debug("Exception while getting the MSH ", ex);
			throw new ValidationException("Exception raised while doing pre-validation");
		}
	}

    private Error updateError(Error error, Fault fault)
    {
        logger.error("Extracting Fault information from config file");
        error.setFaultExternalMessage(fault.getFaultExternalMessage());
        error.setFaultInternalMessage(fault.getFaultInternalMessage());
        error.setFaultExternalCode(fault.getFaultExternalCode());
        error.setFaultInternalCode(fault.getFaultInternalCode());
        error.setFieldNumber(fault.getFieldNumber());
        error.setFieldComponentNumber(fault.getComponentNumber());
        error.setSegment(fault.getSegmentName());

        return error;
    }


    /*processFirstName ---this MEthod is to process the Validation depending on
     * Mandatory and Non Manadatory option set in Standard Adapter Configuration file
     * Especially when Null String ("") or <></> no vlaues are entered
     *
     *
    */
    public void processFirstName(String messageType, String segmentType,Object field) throws
            ValidationException{
         PID5CONTENT fld = (PID5CONTENT) field;
         logger.debug("Validating First Name Field of PID5: " + fld);
         //check if there is mandatory validation for First Name,
         //if yes then First Name block is required and <urn:XPN.2>""</urn:XPN.2> is not allowed.
         List<Validation> validationList = ConfigurationLoader.getDataTypeValidations(messageType, segmentType,"PID5",
                 "XPN2");
         //process the List to check for Mandatory
         boolean isFirstNameMandatory = false;
         if (!validationList.isEmpty())
         {
             for (Validation validation : validationList)
             {
                 if (validation.getType().equals(Type.MANDATORY))
                 {
                     isFirstNameMandatory = true;
                     break;
                 }
             }
         }
         boolean isDoublequoteCharacter =false;

         if(fld.getXPN2()== null && isFirstNameMandatory){
           validateDataType(messageType, segmentType, "PID5", fld.getXPN2(), validationList);
         }

         if(fld.getXPN2() != null && fld.getXPN2().getValue().equals("\"\"")){
             isDoublequoteCharacter=true;
         }
         if(isDoublequoteCharacter && isFirstNameMandatory){
              validateDataType(messageType, segmentType, "PID5", fld.getXPN2(), validationList);
         }
         if(!isDoublequoteCharacter){
              validateDataType(messageType, segmentType, "PID5", fld.getXPN2(), validationList);
         }
     }

      /*processMiddleName ---this MEthod is to process the Validation depending on
     * Mandatory and Non Manadatory option set in Standard Adapter Configuration file
     * Especially when Null String ("") or <></> no values are entered
     *
     */

      public void processMiddleName(String messageType, String segmentType,Object field) throws
            ValidationException{
         PID5CONTENT fld = (PID5CONTENT) field;
         logger.debug("Validating Middle Name Field of PID5: " + fld);
         //check if there is mandatory validation for First Name,
         //if yes then First Name block is required and <urn:XPN.2>""</urn:XPN.2> is not allowed.
         List<Validation> validationList = ConfigurationLoader.getDataTypeValidations(messageType, segmentType,"PID5",
                 "XPN3");
         //process the List to check for Mandatory
         boolean isMiddleNameMandatory = false;
         if (!validationList.isEmpty())
         {
             for (Validation validation : validationList)
             {
                 if (validation.getType().equals(Type.MANDATORY))
                 {
                     isMiddleNameMandatory = true;
                     break;
                 }
             }
         }
         boolean isDoublequoteCharacter =false;

         if(fld.getXPN3()== null &&  isMiddleNameMandatory){
           validateDataType(messageType, segmentType, "PID5", fld.getXPN3(), validationList);
         }

         if(fld.getXPN3() != null && fld.getXPN3().getValue().equals("\"\"")){
             isDoublequoteCharacter=true;
         }
         if(isDoublequoteCharacter && isMiddleNameMandatory){
              validateDataType(messageType, segmentType, "PID5", fld.getXPN3(), validationList);
         }
         if(!isDoublequoteCharacter){
              validateDataType(messageType, segmentType, "PID5", fld.getXPN3(), validationList);
         }



     }

	
}
