package com.bosch.tmp.integration.validation;

import java.util.ArrayList;
import java.util.List;
import org.hl7.v2xml.CX8CONTENT;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.hl7.v2xml.PID3CONTENT;
import org.hl7.v2xml.PIDCONTENT;

/**
 * Missing Identifier checker checks if configured fault id's (Identifier types like PS,SS) exist
 * It will not check for the presence of corresponding ID values

 */
public class MissingIdentifierChecker extends DefaultChecker
{

    public static final Logger logger = LoggerFactory.getLogger(MissingIdentifierChecker.class);

    private String faultId;

    public boolean isChecked(Object obj, Validation validation)
    {
        logger.debug("****************************MISSING_IDENTIFIER_CHECKER*********************************");
        if (validation == null)
        {
            logger.info("validation is not enabled");
            return true;
        }
        Object configValue;
        if (validation.getValue() == null)
        {
            configValue = validation.getValues().getValue();
        }
        else
        {
            configValue = validation.getValue();
        }
        // If config value is not available or not valid do nothing
        if ((configValue == null) ||
                !(configValue instanceof String) ||
                (!configValue.toString().equalsIgnoreCase("true") && !configValue.toString().equalsIgnoreCase("false")))
        {
            logger.info("config value missing");
            return true;
        }
        if(configValue.toString().equalsIgnoreCase("false")){
           return true;
        }
        if (obj == null)
        {
            return true;
        }
        if (obj instanceof List)
        {
            List<PIDCONTENT> pidList = (List<PIDCONTENT>) obj;
            for (PIDCONTENT pid : pidList)
            {
                obj = pid;
                break;
            }
        }
        if (!(obj instanceof PIDCONTENT))
        {
            logger.info("obj is not an instance of PID");
            return true;
        }
        logger.debug("Validating PID3 field");
        PIDCONTENT pid = (PIDCONTENT) obj;
        List<PID3CONTENT> pid3List = pid.getPID3();
        List<PID3CONTENT> newPid3List = new ArrayList<PID3CONTENT>();
        // Filter PID3 list.
        for (PID3CONTENT pid3 : pid3List)
        {
            // Ignore entry with null idType (CX5).
            if (pid3.getCX5() != null)
            {
                String idType = pid3.getCX5().getValue();
                if (idType != null || idType.length() != 0)
                {
                    // Ignore entry whose idType is not one of the defined faults
                    // for the validation.
                    Fault fault = ConfigurationLoader.getFaultById(validation, idType);
                    if (fault != null)
                    {
                        // Ignore entry with expiry date.
                        CX8CONTENT cx8content = pid3.getCX8();
                        if (cx8content != null)
                        {
                            String expiryDate = cx8content.getValue();
                            if (expiryDate == null || expiryDate.length() == 0)
                            {
                                newPid3List.add(pid3);
                            }
                        }
                        else
                        {
                            newPid3List.add(pid3);
                        }
                    }
                    else
                    {
                        newPid3List.add(pid3);
                    }
                }
            }
        }
        // Make sure all IDs defined in the list of Faults exist in the list of PID3s.
        boolean isAllIdsExist = true;
        if (validation.getFaults() != null && validation.getFaults().getFault() != null)
        {
            for (Fault fault : validation.getFaults().getFault())
            {
                boolean isIdExist = false;
                for (PID3CONTENT pid3 : newPid3List)
                {
                    if (fault.getId().equalsIgnoreCase(pid3.getCX5().getValue().trim()))
                    {
                        if (pid3.getCX1() != null && pid3.getCX1().getValue().trim().length() != 0 && !pid3.getCX1().
                                getValue().trim().equals("\"\""))
                        {
                            isIdExist = true;
                        }
                        else
                        {
                            isIdExist = false;
                            break;
                        }
                    }
                }
                isAllIdsExist = isAllIdsExist && isIdExist;
                if (!isAllIdsExist)
                {
                    setFaultId(fault.getId());
                    break;
                }
            }
            if (!isAllIdsExist)
            {
                return false;
            }
        }
        pid.getPID3().clear();
        pid.getPID3().addAll(newPid3List);
        return true;
    }

    public String getFaultId()
    {
        return faultId;
    }

    public void setFaultId(String faultId)
    {
        this.faultId = faultId;
    }
}

