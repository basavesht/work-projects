package com.bosch.tmp.integration.validation;


/**
 *
 * @author gtk1pal
 */
public class ValidatorFactory {

	public static Validator getValidator(CustomerId customerId) {
		Validator validator = null;

		switch (customerId) {
		case SA:
			validator = new DefaultValidator();
			break;
		default:
			validator = new DefaultValidator();
			break;
		}

		return validator;
	}
}
