package com.bosch.tmp.integration.validation;

import java.util.List;
import com.bosch.tmp.integration.util.Error;

/**
 *
 * @author gtk1pal
 */
public interface Validator
{
    public void validateMessage(Object message) throws ValidationException;
    public void validateSegment(String messageType, Object segment, List<Validation> validations) throws ValidationException;   
    public void validateField(String messageType, String segmentType, Object field, List<Validation> validations) throws ValidationException;
    public void validateDataType(String messageType, String segmentType, String fieldType, Object dataType, List<Validation> validations) throws ValidationException;
    public void setErrorObject(Error error);
}
