/*
 * Created on Fri Jun 09 16:20:28 IST 2006
 *
 * Copyright Tata Consultancy Services. All rights reserved.
 * 
 */

package com.tcs.ebw.taglib.formbean;

import com.tcs.ebw.mvc.validator.EbwForm;

public class MenuForm extends EbwForm implements java.io.Serializable {
	private String hiddenValue = null;
	/**
	 * @param validationFailed the validationFailed to set.
	 */
	public void setHiddenValue(String hiddenValue) {
		this.hiddenValue=hiddenValue;
	}
	/**
	 * @return Returns the validationFailed.
	 */
	public String getHiddenValue() {
		return this.hiddenValue;
	}
}