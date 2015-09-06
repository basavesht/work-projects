package com.bosch.tmp.integration.validation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This is the factory Class for Checker classes. It lets the subclasses decide which checker class to instantiate.
 * The Factory method getChecker lets a class defer instantiation to subclasses
 *
 * @author gtk1pal
 */
public class CheckerFactory
{

    public static final Logger logger = LoggerFactory.getLogger(CheckerFactory.class);

    public static Checker getChecker(Type checkType)
    {
        Checker checker = null;
        switch (checkType)
        {
            case MANDATORY:
                checker = new MandatoryChecker();
                break;
            case VALID_VALUE:
                checker = new ValidValueChecker();
                break;
            case DATE_FORMAT:
                checker = new DateFormatChecker();
                break;
            case MISSING_IDENTIFIER:
                checker = new MissingIdentifierChecker();
                break;
            case PATTERN:
                checker = new PatternChecker();
                break;
            case UNSUPPORTED_TRIGGER:
                checker = new UnsupportedTriggerChecker();
                break;
            case FIELDLENGTH:
                checker = new FieldLengthChecker();
                break;
            case DATE_CHECKER:
                checker = new DateChecker();
                break;
            case QUERYTYPE:
                checker = new QueryTypeChecker();
                break;
            case QUERYCODE:
                checker = new QueryCodeChecker();
                break;
            default:
                logger.debug("Invalid Checker");
                break;
        }
        return checker;
    }
}
