package com.bosch.tmp.integration.util;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import org.slf4j.Logger; import org.slf4j.LoggerFactory;

/**
 * This class is used to create an error object with error code and error message.
 * @author gtk1pal
 */
public class Error
{

    public static final Logger logger = LoggerFactory.getLogger(Error.class);

    private List<Error> errorList;

    private String faultInternalCode = null;

    private String faultInternalMessage = null;

    private String faultExternalMessage = null;

    private String faultExternalCode = null;

    private String segment = null;

    private String fieldNumber = null;

    private String fieldComponentNumber = null;

    public String getFieldComponentNumber()
    {
        return fieldComponentNumber;
    }

    public void setFieldComponentNumber(String fieldComponentNumber)
    {
        this.fieldComponentNumber = fieldComponentNumber;
    }

    public String getFieldNumber()
    {
        return fieldNumber;
    }

    public void setFieldNumber(String fieldNumber)
    {
        this.fieldNumber = fieldNumber;
    }

    public String getSegment()
    {
        return segment;
    }

    public void setSegment(String segment)
    {
        this.segment = segment;
    }

    public String getFaultExternalCode()
    {
        return faultExternalCode;
    }

    public void setFaultExternalCode(String faultExternalCode)
    {
        this.faultExternalCode = faultExternalCode;
    }

    public String getFaultExternalMessage()
    {
        return faultExternalMessage;
    }

    public void setFaultExternalMessage(String faultExternalMessage)
    {
        this.faultExternalMessage = faultExternalMessage;
    }

    public String getFaultInternalCode()
    {
        return faultInternalCode;
    }

    public void setFaultInternalCode(String faultInternalCode)
    {
        this.faultInternalCode = faultInternalCode;
    }

    public String getFaultInternalMessage()
    {
        return faultInternalMessage;
    }

    public void setFaultInternalMessage(String faultInternalMessage)
    {
        this.faultInternalMessage = faultInternalMessage;
    }

    /**
     * @return the errorList
     */
    public List<Error> getErrorList()
    {
        if (errorList == null)
        {
            errorList = new ArrayList<Error>();
        }
        return errorList;
    }

    /**
     * @param aErrorList the errorList to set
     */
    public void setErrorList(List<Error> aErrorList)
    {
        errorList = aErrorList;
    }

    /**
     * This method checks if the error list has any errors.
     * @return boolean true if errors exist else false
     */
    public boolean hasErrors()
    {
        boolean hasErrors = false;
        List<Error> errors = getErrorList();
        if (errors != null && !errors.isEmpty())
        {
            hasErrors = true;
        }
        return hasErrors;
    }

    /**
     * this method returns the first error from the error list.
     * @return Error from error list
     */
    public Error getError()
    {
        Error error = null;
        List<Error> errors = getErrorList();
        ListIterator listItr = errors.listIterator();
        while (listItr.hasNext())
        {
            error = (Error) listItr.next();
            break;
        }
        return error;
    }
}
