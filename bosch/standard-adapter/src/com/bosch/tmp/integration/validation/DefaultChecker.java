package com.bosch.tmp.integration.validation;

/**
 * This is an abstract class which implements the Checker.All the Checker Classes should extent this class and
 * implement the abstract method which actually contains checker logic.
 *
 * @author gtk1pal
 */
public abstract class DefaultChecker implements Checker
{

    public abstract boolean isChecked(Object inMsgValue, Validation validation);

    public String getFaultId()
    {
        return null;
    }
}
