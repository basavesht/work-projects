package com.bosch.tmp.integration.validation;

import com.bosch.tmp.integration.util.MessageConverter;
import com.bosch.tmp.integration.util.Utils;
import java.io.FileInputStream;
import java.io.InputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 * This class reads the runtime configuration file and provides values for each configuration.
 *
 * @author tis1pal
 */
public class ConfigurationLoader
{

    public static final Logger logger = LoggerFactory.getLogger(ConfigurationLoader.class);

    private static Configuration configuration;

    static
    {
        initialize();
    }

    /**
     * This method is used to load the Configuration file which is specific to customer. 
     */
    public static void initialize()
    {
        try
        {
            logger.debug("Start initializing the config file.");
            InputStream in = null;
            String configLocation = System.getProperty("SIAConfig");
            logger.debug("Config file location:-   " + configLocation);
            if (configLocation == null || configLocation.isEmpty())
            {
                configLocation = "StandardAdapterConfiguration.xml";
                in = ConfigurationLoader.class.getClassLoader().getResourceAsStream(
                        configLocation);
            }
            else
            {
                in = new FileInputStream(configLocation);
            }

            String xmlConfig = Utils.convertInputStreamToString(in);
            configuration = (Configuration) MessageConverter.convertXmlToObject(xmlConfig,
                    "com.bosch.tmp.integration.validation");
            logger.debug("Config file Root element is: " + configuration);
        }
        catch (Exception ex)
        {
            logger.error(ex.toString());
        }
    }

    /**
     * This method returns configured customer id as CustomerId object
     * @return
     */
    public static Object getCustomerId()
    {
        if (configuration == null)
        {
            return null;
        }
        if (configuration.getApplication() != null)
        {
            return configuration.getApplication().getCustomerId();
        }
        else
        {
            return null;
        }
    }

    /**
     * Get the application level configuration as key value pairs in a hashMap
     * @param id
     * @return key value pairs
     */
    public static HashMap<String, String> getApplicationConfig(String id)
    {
        if (configuration == null)
        {
            return null;
        }
        if (configuration.getApplication() == null)
        {
            return null;
        }
        if ((configuration.getApplication().getConfigValues() == null) && (configuration.getApplication().getConfigValue() == null))
        {
            return null;
        }
        if (id == null)
        {
            return null;
        }

        HashMap<String, String> map = new HashMap<String, String>();

        // Puts all the config values from ConfigValues tags matching the id in the map
        for (ConfigValues values : configuration.getApplication().getConfigValues())
        {
            if (id != null && id.equals(values.getId()))
            {
                for (ConfigValue configValue : values.getConfigValue())
                {
                    map.put(configValue.getId(), configValue.getValue());
                }
            }
        }
        // Puts all the config values from ConfigValue tags matching the id in the map
        for (ConfigValue value : configuration.getApplication().getConfigValue())
        {
            if (id.equals(value.getId()))
            {
                map.put(value.getId(), value.getValue());
            }
        }
        return map;
    }

    /**
     * Get the application level configuration values as list
     * @param id
     * @return list of values
     */
    public static List<String> getApplicationConfigValuesById(String id)
    {
        HashMap<String, String> map = getApplicationConfig(id);
        List<String> configValuesList = new ArrayList<String>(map.values());
        configValuesList.removeAll(Collections.singletonList(null));
        return configValuesList;
    }

    /**
     * Get the application level configuration by Id
     * @param id
     * @return
     */
    public static String getApplicationConfigValueById(String id)
    {
        HashMap<String, String> map = getApplicationConfig(id);

        if (map != null && map.containsKey(id))
        {
            return map.get(id);
        }
        return null;
    }

    /**
     * This method is used to get the application level faults when we pass the id
     * @param id
     * @return Fault
     */
    public static Fault getApplicationFaultById(String id)
    {
        if (configuration == null)
        {
            return null;
        }
        if (configuration.getApplication() == null)
        {
            return null;
        }
        if (configuration.getApplication().getFaults() == null)
        {
            return null;
        }
        if (configuration.getApplication().getFaults().getFault() == null)
        {
            return null;
        }
        if (id == null)
        {
            return null;
        }
        try
        {
            for (Fault fault : configuration.getApplication().getFaults().getFault())
            {
                if (id.equals(fault.getId()))
                {
                    return fault;
                }
            }
        }
        catch (Exception e)
        {
            logger.error("Error While getting the Application Level Fault by Id" + e);
        }
        return null;
    }

    /**
     * This method is used to get the message type when we pass the id
     * @param id
     * @return Message
     */
    public static Message getMessageById(String messageId)
    {
        if (messageId == null)
        {
            return null;
        }
        if (configuration == null)
        {
            return null;
        }
        if (configuration.getMessages() == null)
        {
            return null;
        }
        if (configuration.getMessages().getMessage() == null)
        {
            return null;
        }
        try
        {
            for (Message msg : configuration.getMessages().getMessage())
            {
                if (messageId.equals(msg.getId()))
                {
                    return msg;
                }
            }
        }
        catch (Exception e)
        {
            logger.error("Exception raised while trying to get the message by id" + e);
        }
        return null;
    }

    /**
     * Method is used to get the configured segment of a message when message and segment id is passed
     * @param parentMsg
     * @param id
     * @return Segment
     */
    public static Segment getSegmentById(Message parentMsg, String segmentId)
    {
        if (segmentId == null)
        {
            return null;
        }
        if (parentMsg == null)
        {
            return null;
        }
        if (parentMsg.getSegments() == null)
        {
            return null;
        }
        if (parentMsg.getSegments().getSegment() == null)
        {
            return null;
        }
        try
        {
            for (Segment segment : parentMsg.getSegments().getSegment())
            {
                if (segmentId.equals(segment.getId()))
                {
                    return segment;
                }
            }
        }
        catch (Exception e)
        {
            logger.error("Exception raised while trying to get the segment from message" + e);
        }
        return null;
    }

    /**
     * This methods returns the segment when segment id is passed.
     * @param id
     * @return Segment
     */
    public static Segment getSegmentById(String id)
    {
        if (id == null)
        {
            return null;
        }
        if (configuration == null)
        {
            return null;
        }
        if (configuration.getSegments() == null)
        {
            return null;
        }
        if (configuration.getSegments().getSegment() == null)
        {
            return null;
        }
        try
        {
            for (Segment segment : configuration.getSegments().getSegment())
            {
                if (id.equals(segment.getId()))
                {
                    return segment;
                }
            }
        }
        catch (Exception e)
        {
            logger.error("Exception raised while trying to get the segment by Id" + e);
        }
        return null;
    }

    /**
     * Get the Field By Id
     * @param id
     * @return Field
     */
    public static Field getFieldById(String fieldId)
    {
        if (fieldId == null)
        {
            return null;
        }
        if (configuration == null)
        {
            return null;
        }
        if (configuration.getFields() == null)
        {
            return null;
        }
        if (configuration.getFields().getField() == null)
        {
            return null;
        }
        try
        {
            for (Field field : configuration.getFields().getField())
            {
                if (fieldId.equals(field.getId()))
                {
                    return field;
                }
            }
        }
        catch (Exception e)
        {
            logger.error("Exception raised while trying to get the field by Id" + e);
        }
        return null;
    }

    /**
     * get the datatype when fieldid and datatTypeid is passed
     * @param dataTypeId
     * @return DataType
     */
    public static DataType getDatatypeByFieldId(String fieldId, String dataTypeId)
    {
        if (fieldId == null)
        {
            return null;
        }
        if (configuration == null)
        {
            return null;
        }
        if (configuration.getFields() == null)
        {
            return null;
        }
        if (configuration.getFields().getField() == null)
        {
            return null;
        }
        if (configuration.getFields().getField() == null)
        {
            return null;
        }
        try
        {
            for (Field field : configuration.getFields().getField())
            {
                if (fieldId.equals(field.getId()))
                {
                    if (dataTypeId == null)
                    {
                        return null;
                    }
                    if (field.getDataTypes() == null)
                    {
                        return null;
                    }
                    if (field.getDataTypes().getDataType() == null)
                    {
                        return null;
                    }
                    for (DataType dataType : field.getDataTypes().getDataType())
                    {
                        if (dataTypeId.equals(dataType.getId()))
                        {
                            return dataType;
                        }
                    }
                    return null;
                }
            }
        }
        catch (Exception e)
        {
            logger.error("Exception raised while trying to get the dataType by Id" + e);
        }
        return null;
    }

    /**
     * @param Gets the Field enclosed in a Segment
     * @param fieldId
     * @return Field
     */
    public static Field getFieldById(Segment segment, String fieldId)
    {
        if (fieldId == null)
        {
            return null;
        }
        if (segment == null)
        {
            return null;
        }
        if (segment.getFields() == null)
        {
            return null;
        }
        if (segment.getFields().getField() == null)
        {
            return null;
        }
        try
        {
            for (Field field : segment.getFields().getField())
            {
                if (fieldId.equals(field.getId()))
                {
                    return field;
                }
            }
        }
        catch (Exception e)
        {
            logger.error("Exception raised while trying to get the field of segment by Id" + e);
        }
        return null;
    }

    /**
     * get the datatype when field id and datatTypeid is passed
     * @param dataTypeId
     * @return DataType
     */
    public static DataType getDataTypeById(Field field, String dataTypeId)
    {
        if (dataTypeId == null)
        {
            return null;
        }
        if (field == null)
        {
            return null;
        }
        if (field.getDataTypes() == null)
        {
            return null;
        }
        if (field.getDataTypes().getDataType() == null)
        {
            return null;
        }
        try
        {
            for (DataType dataType : field.getDataTypes().getDataType())
            {
                if (dataTypeId.equals(dataType.getId()))
                {
                    return dataType;
                }
            }
        }
        catch (Exception e)
        {
            logger.error("Exception raised while trying to get the datatype of field by Id" + e);
        }
        return null;
    }

    /**
     * @param Get the DataType by passing dataTypeId
     * @param dataTypeId
     * @return DataType
     */
    public static DataType getDataTypeById(String dataTypeId)
    {
        if (dataTypeId == null)
        {
            return null;
        }
        if (configuration == null)
        {
            return null;
        }
        if (configuration.getDataTypes() == null)
        {
            return null;
        }
        if (configuration.getDataTypes().getDataType() == null)
        {
            return null;
        }
        try
        {
            for (DataType dataType : configuration.getDataTypes().getDataType())
            {
                if (dataTypeId.equals(dataType.getId()))
                {
                    return dataType;
                }
            }
        }
        catch (Exception e)
        {
            logger.error("Exception raised while trying to get the dataTyoe of Field by Id" + e);
        }
        return null;
    }

    /**
     * @param Gets the Fault of a Validation by faultId.
     * If there are multiple faults associated then get the fault with that particular ID.
     * @param faultId
     * @return Fault
     */
    public static Fault getFaultById(Validation validation, String faultId)
    {
        if (validation == null)
        {
            return null;
        }
        if (faultId == null)
        {
            return null;
        }
        if (validation.getFaults() == null)
        {
            return null;
        }
        if (validation.getFaults().getFault() == null)
        {
            return null;
        }
        for (Fault fault : validation.getFaults().getFault())
        {
            if (faultId.equalsIgnoreCase(fault.getId()))
            {
                return fault;
            }
        }
        if (validation.getFault() == null)
        {
            return null;
        }
        if (faultId.equalsIgnoreCase(validation.getFault().getId()))
        {
            return validation.getFault();
        }
        return null;
    }

    /**
     * Get the segment level validation by validation type
     * @param segment
     * @param id
     * @return
     */
    public static Validation getSegmentValidationByType(String messageId, String segmentId, Type type)
    {
        List<Validation> validations = getSegmentValidations(messageId, segmentId);
        if (validations == null)
        {
            return null;
        }
        if (type == null)
        {
            return null;
        }
        try
        {
            if (validations != null && validations.size() > 0)
            {
                for (Validation validation : validations)
                {
                    if (type == validation.getType())
                    {
                        return validation;
                    }
                }
            }
        }
        catch (Exception e)
        {
            logger.error("Exception Raised while trying to get segment level validation by type");
        }
        return null;
    }

    /**
     * Get the field level validations if the validation has an id
     * @param validationId
     * @return Validation
     */
    public static Validation getFieldValidationById(String messageId, String segmentId, String fieldId,
            String validationId)
    {
        List<Validation> validations = getFieldValidations(messageId, segmentId, fieldId);
        if (validations == null)
        {
            return null;
        }
        if (validationId == null)
        {
            return null;
        }
        try
        {
            for (Validation validation : validations)
            {
                if (validationId.equals(validation.getId()))
                {
                    return validation;
                }
            }
        }
        catch (Exception e)
        {
            logger.error("Exception Raised while trying to get Field level validation by validation ID");
        }
        return null;
    }

    /**
     * get the Field Level Validation by Type.
     * @param messageId,SegmentId,fieldId,type
     * @return Validation
     */
    public static Validation getFieldValidationByType(String messageId, String segmentId, String fieldId, Type type)
    {
        List<Validation> validations = getFieldValidations(messageId, segmentId, fieldId);
        if (validations == null)
        {
            return null;
        }
        if (type == null)
        {
            return null;
        }
        for (Validation validation : validations)
        {
            if (type == validation.getType())
            {
                return validation;
            }
        }
        return null;
    }

    /**
     * Get the Validation by validation type and ID
     * @return validation
     */
    public static Validation getFieldValidationsByTypeAndID(String messageId, String segmentId, String fieldId,
            String validationId, Type type)
    {
        List<Validation> validations = getFieldValidations(messageId, segmentId, fieldId);
        if (validations == null)
        {
            return null;
        }
        if (type == null)
        {
            return null;
        }
        if (validationId == null)
        {
            return null;
        }
        for (Validation validation : validations)
        {
            if (type == validation.getType() && validationId.equalsIgnoreCase(validation.getId()))
            {
                return validation;
            }
        }
        return null;
    }

    /**
     * Get the Datatype validation by validation type.
     * @params messageId,segmentId,fieldId,dataTypeId,type
     * @return Validation
     */
    public static Validation getDataTypeValidationByType(String messageId, String segmentId, String fieldId,
            String dataTypeId, Type type)
    {
        List<Validation> validations = getDataTypeValidations(messageId, segmentId, fieldId, dataTypeId);
        if (validations == null)
        {
            return null;
        }
        if (type == null)
        {
            return null;
        }
        for (Validation validation : validations)
        {
            if (type == validation.getType())
            {
                return validation;
            }
        }
        return null;
    }

    /**
     * Get the message Level validations by validation Type
     * @params messageId,type
     * @return Validation
     */
    public static Validation getMessageValidationsbyType(String messageId, Type type)
    {
        if (configuration == null)
        {
            return null;
        }
        if (type == null)
        {
            return null;
        }
        List<Validation> validations = getMessageValidations(messageId);
        if (validations == null)
        {
            return null;
        }

        for (Validation validation : validations)
        {
            if (type == validation.getType())
            {
                return validation;
            }
        }
        return null;
    }

    /**
     * Get the message Level validations by validation Type and Id
     * @params messageId,type
     * @return Validation
     */
    public static Validation getMessageValidationsbyTypeAndID(String messageId, String validationId, Type type)
    {
        List<Validation> validations = getMessageValidations(messageId);
        if (validations == null)
        {
            return null;
        }
        if (type == null)
        {
            return null;
        }
        if (validationId == null)
        {
            return null;
        }
        for (Validation validation : validations)
        {
            if (type == validation.getType() && validationId.equalsIgnoreCase(validation.getId()))
            {
                return validation;
            }
        }
        return null;
    }

    /**
     * This method is used to get all message level validations
     * @param messageId
     * @return List
     */
    public static List<Validation> getMessageValidations(String messageId)
    {
        logger.debug("Getting Validations for Message: " + messageId);
        if (configuration == null)
        {
            return null;
        }
        if (messageId == null)
        {
            return null;
        }
        if (configuration.getMessages() == null)
        {
            return null;
        }
        if (configuration.getMessages().getMessage() == null)
        {
            return null;
        }
        List<Validation> validations = null;
        // Getting the top-level (common) Message validations
        for (Message message : configuration.getMessages().getMessage())
        {
            if (messageId.equals(message.getId()))
            {
                if (message.getValidations() != null)
                {
                    validations = message.getValidations().getValidation();
                    break;
                }
            }
        }
        return validations;
    }
//

    /**
     * This method is used to get all segment validations. This includes top level segment validations
     * and message level segment validations and if there are any common validations defined for the same segment
     * at both message and segment level the validation at the segment level needs to be overridden by message level validations
     * @param segmentId
     * @param messageId
     * @return List<Validation>
     */
    public static List<Validation> getSegmentValidations(String messageId, String segmentId)
    {
        Segment segment = getSegmentById(segmentId);
        List<Validation> validations = null;

        // Getting the top-level (common) segment validations
        if (segment != null && segment.getValidations() != null)
        {
            validations = segment.getValidations().getValidation();
        }

        // Only use the top-level segment validations in case the messageId is null
        if (messageId == null)
        {
            return validations;
        }

        Message message = getMessageById(messageId);
        Segment segmentByMessage = getSegmentById(message, segmentId);

        // Getting the message-level segment validations, and override the common validation if any
        if (validations != null)
        {
            if (segmentByMessage != null && segmentByMessage.getValidations() != null && segmentByMessage.getValidations().
                    getValidation() != null)
            {
                validations = updateValidationsList(segmentByMessage.getValidations().getValidation(), validations);
            }
        }
        else
        {
            if (segmentByMessage != null && segmentByMessage.getValidations() != null)
            {
                validations = segmentByMessage.getValidations().getValidation();
            }
        }
        return validations;
    }

    /**
     * This method is used to get all field validations. This include top level, segment level
     * and message level field validations and if they are any common validations defined for the same filed
     * at message,segment and field level the validation at the field and segment level needs to be overridden
     * @param messageId
     * @param segmentId
     * @param fieldId
     * @return List<Validation>
     */
    public static List<Validation> getFieldValidations(String messageId, String segmentId, String fieldId)
    {
        logger.debug("getFieldValidations begin ");
        List<Validation> validations = null;

        Field field = getFieldById(fieldId);
        // Getting the top-level (common) field validations
        if (field != null && field.getValidations() != null && field.getValidations().getValidation() != null)
        {
            validations = field.getValidations().getValidation();
        }
        if(validations!= null && !validations.isEmpty()){
          logger.debug("Validations for FieldID: " + fieldId);
          logger.debug("Size of the Validation for  " + fieldId +" is :-" + validations.size());
          for(Validation validation:validations){
              logger.debug("Validation Type is :-" + validation.getType());
          }
          logger.debug("Size of the Validation for  " + fieldId +" is :-" + validations.size());
        }else{
          logger.debug( " no validations for FiedlID  "+fieldId );
        }


        // Only use the top-level field validations in case the segmentId is null
        if (segmentId == null)
        {
            return validations;
        }
        Segment segment = getSegmentById(segmentId);
        if (segment != null && segment.getFields() != null && segment.getFields().getField() != null)
        {
            validations = getFieldValidations(segment, fieldId, validations);
        }
        if(validations!= null && !validations.isEmpty()){
          logger.debug("Validations for Segment Level: " + segmentId);
          logger.debug("Size of the Validation for Segment " + segmentId+" is :-" +validations.size());
          for(Validation validation:validations){
              logger.debug("Validation Type is :-" + validation.getType());
          }
        }else{
             logger.debug("no validations for Segment: "+ segmentId);
        }


        // Only use the segment level field validations in case the messageId is null
        if (messageId == null)
        {
            return validations;
        }
        // Getting the message-level field validations, and override the common validation if any
        segment = getSegmentById(getMessageById(messageId), segmentId);
        if (segment != null && segment.getFields() != null && segment.getFields().getField() != null)
        {
            validations = getFieldValidations(segment, fieldId, validations);
        }
         if(validations!= null && !validations.isEmpty()){
          logger.debug("Validations for Message Level: " + messageId);
          logger.debug("Size of the Validation for Message " + messageId+" is :-" +validations.size());
          for(Validation validation:validations){
              logger.debug("Validation Type is :-" + validation.getType());
          }
        }else{
             logger.debug("no validations for Message Level: "+ messageId.toString());
        }
        logger.debug("getFieldValidations ends ");
        return validations;
    }

    /**
     * This method is used to get all datatype validations. This includes top level ,segment level,field level
     * and message level datatype validations and if there are any common validations defined for the same datatype
     * at message,segment,field and datatype level the validation at the field ,segment and datatype level needs to be overridden
     * @param messageId
     * @param segmentId
     * @param fieldId
     * @return List<Validation>
     */
    public static List<Validation> getDataTypeValidations(String messageId, String segmentId, String fieldId,
            String dataTypeId)
    {
        logger.debug("getDataTypeValidations begin ");
        if (configuration == null)
        {
            return null;
        }
        if (dataTypeId == null)
        {
            return null;
        }

        List<Validation> validations = null;
        // Getting the top-level (common) DataType validations
        DataType dataType = getDataTypeById(dataTypeId);
        if (dataType != null && dataType.getValidations() != null && dataType.getValidations().getValidation() != null)
        {
            validations = dataType.getValidations().getValidation();
        }
        if(validations!= null &&!validations.isEmpty()){
          logger.debug("Validations for dataType: " + dataTypeId);
          logger.debug("Size of the Validation for  " + dataTypeId+" is :-" +validations.size());
          for(Validation validation:validations){
              logger.debug("Validation Type is :-" + validation.getType());
          }
        }else{
          logger.debug("no validations for dataType  "+dataTypeId );
        }




        // Only use the top-level dataType validations in case the fieldId is null
        if (fieldId == null)
        {
            return validations;
        }
        Field field = getFieldById(fieldId);
        if (field != null && field.getDataTypes() != null && field.getDataTypes().getDataType() != null)
        {
            // Getting the field-level data type validations, and override the common validation if any
            validations = getDataTypeValidations(field, dataTypeId, validations);
        }

        if(validations!= null && !validations.isEmpty()){
          logger.debug("Validations for field: " + fieldId);
          logger.debug("Size of the Validation for  " + fieldId+" is :-" +validations.size());
          for(Validation validation:validations){
              logger.debug("Validation Type is :-" + validation.getType());
          }
        }else{
          logger.debug("no validations for field:  "+fieldId );
        }


        // Only use the field level data type validations in case the segmentId is null
        if (segmentId == null)
        {
            return validations;
        }
        // Getting the segment-level field's data type validations, and override the common validation if any
        field = getFieldById(getSegmentById(segmentId), fieldId);
        if (field != null && field.getDataTypes() != null && field.getDataTypes().getDataType() != null)
        {
            validations = getDataTypeValidations(field, dataTypeId, validations);
        }

        if(validations!= null && !validations.isEmpty()){
          logger.debug("Validations for segment: " + segmentId);
          logger.debug("Size of the Validation for  " + segmentId+" is :-" +validations.size());
          for(Validation validation:validations){
              logger.debug("Validation Type is :-" + validation.getType());
          }
        }else{
          logger.debug("no validations for segment:  "+segmentId);
        }


        // Only use the segment level data type validations in case the messageId is null
        if (messageId == null)
        {
            return validations;
        }
        // Getting the message-level field validations, and override the common validation if any
        field = getFieldById(getSegmentById(getMessageById(messageId), segmentId), fieldId);
        if (field != null && field.getDataTypes() != null && field.getDataTypes().getDataType() != null)
        {
            validations = getDataTypeValidations(field, dataTypeId, validations);
        }

         if(validations!= null && !validations.isEmpty()){
          logger.debug("Validations for message: " + messageId);
          logger.debug("Size of the Validation for  " + messageId +" is :-" +validations.size());
          for(Validation validation:validations){
              logger.debug("Validation Type is :-" + validation.getType());
          }
        }else{
          logger.debug("no validations for message:  "+messageId );
        }
        logger.debug("getDataTypeValidations ends ");
        return validations;

    }

    /**
     * This method is used to get all segment Configurations.
     * @param segmentId
     * @param messageId
     * @return object
     */
    public static Object getSegmentConfigurations(String messageId, String segmentId)
    {
        if (configuration == null)
        {
            return null;
        }

        if (segmentId == null)
        {
            return null;
        }

        Segment segment = getSegmentById(segmentId);
        Object object = getSegmentConfigValues(segment);

        // Only use the top-level segment validations in case the messageId is null
        if (messageId == null)
        {
            return object;
        }

        // Getting the message-level segment validations, and override the common validation if any
        Message message = getMessageById(messageId);
        object = getSegmentConfigValues(getSegmentById(message, segmentId));

        return object;
    }

    /**
     * This method is used to get all segment Configurations given a segment.
     * @param segment
     * @return object
     */
    public static Object getSegmentConfigValues(Segment segment)
    {
        Object object = null;
        if (segment != null && segment.getConfigValue() != null)
        {
            object = segment.getConfigValue().getValue();
        }
        else if (segment != null && segment.getConfigValues() != null)
        {
            List<ConfigValue> configValuesList = segment.getConfigValues().getConfigValue();
            List<String> list = new ArrayList<String>();
            for (ConfigValue configValue : configValuesList)
            {
                list.add(configValue.getValue());
            }
            object = list;
        }
        return object;
    }

    /**
     * This method is used to get field configuration for a particular field id.
     * @param messageId
     * @param segmentId
     * @param fieldId
     * @return List<Validation>
     */
    public static Object getFieldConfigurations(String messageId, String segmentId, String fieldId)
    {
        if (configuration == null)
        {
            return null;
        }
        if (fieldId == null)
        {
            return null;
        }

        Object object = null;
        Field field = getFieldById(fieldId);

        // Getting the top-level (common) field configurations
        if (field != null && field.getConfigValue() != null)
        {
            object = field.getConfigValue().getValue();
        }
        else if (field != null && field.getConfigValues() != null)
        {
            List<ConfigValue> configValuesList = field.getConfigValues().getConfigValue();
            List<String> list = new ArrayList<String>();
            for (ConfigValue configValue : configValuesList)
            {
                list.add(configValue.getValue());
            }
            object = list;
        }

        // Only use the top-level field configurations in case the segmentId is null
        if (segmentId == null)
        {
            return object;
        }
        Segment segment = getSegmentById(segmentId);
        if (segment != null && segment.getFields() != null && segment.getFields().getField() != null)
        {
            // Getting the segment-level field Configurations
            object = getFieldConfigValues(segment, fieldId, object);
        }
        // Only use the segment level field Configurations values in case the messageId is null
        if (messageId == null)
        {
            return object;
        }
        // Getting the message-level field Configurations, and replace the common other configurations
        segment = getSegmentById(getMessageById(messageId), segmentId);
        if (segment != null && segment.getFields() != null && segment.getFields().getField() != null)
        {
            object = getFieldConfigValues(segment, fieldId, object);
        }
        return object;
    }

    /**
     * This method is used to get all datatype configuration for a particular datatype id.
     * The configvalue for the datatype described at message level takes precedence over the one
     * defined at field level and datatype level.
     * @param messageId
     * @param segmentId
     * @param fieldId
     * @return object
     */
    public static Object getDataTypeConfigurations(String messageId, String segmentId, String fieldId, String dataTypeId)
    {
        if (configuration == null)
        {
            return null;
        }

        if (dataTypeId == null)
        {
            return null;
        }
        Object object = null;
        DataType dataType = getDataTypeById(dataTypeId);

        // Getting the top-level (common) DataType validations
        if (dataType != null && dataType.getConfigValue() != null)
        {
            object = dataType.getConfigValue().getValue();
        }
        else if (dataType != null && dataType.getConfigValues() != null)
        {
            List<ConfigValue> configValuesList = dataType.getConfigValues().getConfigValue();
            List<String> list = new ArrayList<String>();
            for (ConfigValue configValue : configValuesList)
            {
                list.add(configValue.getValue());
            }
            object = list;
        }

        // Only use the top-level dataType validations in case the fieldId is null
        if (fieldId == null)
        {
            return object;
        }
        Field field = getFieldById(fieldId);
        if (field != null && field.getDataTypes() != null && field.getDataTypes().getDataType() != null)
        {
            object = getDataTypeConfigValues(field, dataTypeId, object);
        }
        // Only use the segment level field configurations in case the segmentId is null
        if (segmentId == null)
        {
            return object;
        }
        // Getting the message-level field configurations
        field = getFieldById(getSegmentById(segmentId), fieldId);
        if (field != null && field.getDataTypes() != null && field.getDataTypes().getDataType() != null)
        {
            object = getDataTypeConfigValues(field, dataTypeId, object);
        }
        // Only use the segment level field configurations in case the messageId is null
        if (messageId == null)
        {
            return object;
        }
        // Getting the message-level field configurations
        field = getFieldById(getSegmentById(getMessageById(messageId), segmentId), fieldId);
        if (field != null && field.getDataTypes() != null && field.getDataTypes().getDataType() != null)
        {
            object = getDataTypeConfigValues(field, dataTypeId, object);
        }
        return object;
    }

    /**
     * This method is used to get DataType configurations for a field.
     * @params field dataTypeId object
     * @return Object
     */
    private static Object getDataTypeConfigValues(Field field, String dataTypeId, Object object)
    {
        for (DataType dataType : field.getDataTypes().getDataType())
        {
            if (dataTypeId.equalsIgnoreCase(dataType.getId()))
            {
                if (dataType.getConfigValue() != null)
                {
                    object = dataType.getConfigValue().getValue();
                }
                else if (dataType.getConfigValues() != null)
                {
                    List<ConfigValue> configValuesList = field.getConfigValues().getConfigValue();
                    List<String> list = new ArrayList<String>();
                    for (ConfigValue configValue : configValuesList)
                    {
                        list.add(configValue.getValue());
                    }
                    object = list;
                }
                break;
            }
        }
        return object;
    }

    /**
     * This method is used to get Field configurations for a segment.
     * @params segment fieldId object
     * @return Object
     */
    public static Object getFieldConfigValues(Segment segment, String fieldId, Object object)
    {
        for (Field field : segment.getFields().getField())
        {
            if (fieldId.equalsIgnoreCase(field.getId()))
            {
                if (field.getConfigValue() != null)
                {
                    object = field.getConfigValue().getValue();
                }
                else if (field.getConfigValues() != null)
                {
                    List<ConfigValue> configValuesList = field.getConfigValues().getConfigValue();
                    List<String> list = new ArrayList<String>();
                    for (ConfigValue configValue : configValuesList)
                    {
                        list.add(configValue.getValue());
                    }
                    object = list;
                }
                break;
            }
        }
        return object;
    }

    /**
     * This method is used to get FieldLevel DataType validations.
     * @params field dataTypeId validations
     * @return List<Validation>
     */
    private static List<Validation> getDataTypeValidations(Field field, String dataTypeId, List<Validation> validations)
    {
        DataType dataType = getDataTypeById(field, dataTypeId);

        if (validations != null)
        {
            if (dataType != null && dataType.getValidations() != null && dataType.getValidations().getValidation() != null)
            {
                validations = updateValidationsList(dataType.getValidations().getValidation(), validations);
            }
        }
        else
        {
            if (dataType != null && dataType.getValidations() != null)
            {
                validations = dataType.getValidations().getValidation();
            }
        }
        return validations;
    }

    private static List<Validation> getFieldValidations(Segment segment, String fieldId,
            List<Validation> validations)
    {
        Field field = getFieldById(segment, fieldId);
        // Getting the segment-level field validations, and override the common validation if any
        if (validations != null)
        {
            if (field != null && field.getValidations() != null && field.getValidations().getValidation() != null)
            {
                validations = updateValidationsList(field.getValidations().getValidation(), validations);
            }
        }
        else
        {
            if (field != null && field.getValidations() != null)
            {
                validations = field.getValidations().getValidation();
            }
        }

        return validations;
    }

    private static boolean isValidationNew(Validation validationToCompare, List<Validation> validations)
    {
        for (Validation validation : validations)
        {
            if (validationToCompare.getType().equals(validation.getType()))
            {
                return false;
            }
            else
            {
                return true;
            }
        }
        return true;
    }

    private static List<Validation> updateValidationsList(List<Validation> upperLevelValidationsList,
            List<Validation> lowerLevelValidationsList)
    {
        List<Validation> removeValidationList = new ArrayList<Validation>();
        List<Validation> upperLevelRealValidationList = (ArrayList<Validation>) ((ArrayList<Validation>) upperLevelValidationsList).clone();
        for (Validation upperLevelValidation : upperLevelValidationsList)
        {
            for (Validation lowerLevelValidation : lowerLevelValidationsList)
            {
                if ((upperLevelValidation.getType().equals(lowerLevelValidation.getType())))
                {
                    removeValidationList.add(upperLevelValidation);
                    break;
                }
            }
            if (!removeValidationList.isEmpty())
            {
                lowerLevelValidationsList.removeAll(removeValidationList);
                removeValidationList.clear();
            }
        }
        upperLevelRealValidationList.addAll(lowerLevelValidationsList);
        lowerLevelValidationsList = null;
        removeValidationList = null;
        return upperLevelRealValidationList;
    }
}

