package com.bosch.tmp.integration.validation;

/**
 * This is the base interface implemented by Default Checker
 * 
 * @author gtk1pal
 */
public interface Checker
{
    public boolean isChecked(Object inMsgValue, Validation validation);
    public String getFaultId();
}
