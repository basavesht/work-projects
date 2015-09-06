package com.bosch.tmp.integration.validation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.slf4j.Logger; import org.slf4j.LoggerFactory;

/**
 * Valid value checker checks if the given value in the incoming message is same as the configured value if the value is present.
 *
 * @author gtk1pal
 */
public class ValidValueChecker extends DefaultChecker
{
    
    public static final Logger logger = LoggerFactory.getLogger(ValidValueChecker.class);

    public boolean isChecked(Object obj, Validation validation)
    {
		logger.debug("**********************************VALID_VALUE_CHECKER******************************");

		Object configValue = null;
		if (validation.getValue() == null) {
			configValue = validation.getValues().getValue();
		}
		else {
			configValue = validation.getValue();
		}

		// If config value is not available or not valid do nothing
		if (configValue == null || !(configValue instanceof String)) {
			logger.debug("Config value is not available or not valid");
			return true;
		}
		if (obj == null) {
			logger.debug("Object passed to this validation is null");
			return true;
		}

		// Using Java reflection to get the value for that specified object.
		Object value = null;
		try {
			value = obj.getClass().getDeclaredMethod("getValue").invoke(obj);
		}
		catch (Exception ex){
			// leave value as null if 'getValue' can't be invoked by 'obj' or if 'obj' is null.
			logger.debug("Exception raised while trying to get the value using reflexion ");
		}

		//Create list of valid values allowed.
		List<String> configValuesList = null;
		if (configValue instanceof String && !((String) configValue).trim().equals("")) {
			configValuesList = new ArrayList<String>();
			if(configValue!=null && ((String)configValue).indexOf(",")!=-1){
				String[] configArr = ((String)configValue).split(",");
				List<String> configArrList = Arrays.asList(configArr);
				configValuesList.addAll(configArrList);
			}
			else {
				configValuesList.add((String) configValue);
			}
		}
		else {
			configValuesList = (List) configValue;
		}

		//Perform check..
		if (value != null && value instanceof String && !value.toString().trim().equals("")) {
			for (String configVal : configValuesList) {
				if (value.toString().equalsIgnoreCase(configVal.toString())) {
					return true;
				}
			}
			return false;
		}
		// otherwise, do nothing and return true
		return true;
	}
}
