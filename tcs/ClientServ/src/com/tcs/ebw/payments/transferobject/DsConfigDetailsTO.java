package com.tcs.ebw.payments.transferobject;

import com.tcs.ebw.transferobject.EBWTransferObject;

/**
 * @author : 224703
 * **************   Revision History ************************
 * Modified-By |  Modified-Date |  Project-Phase  | Changes
 *
 * **********************************************************
 */
public class DsConfigDetailsTO extends EBWTransferObject implements java.io.Serializable {
   
	/**
	 * 
	 */
	private static final long serialVersionUID = 1983078785719168870L;
	private String branch_code ;

	public String getBranch_code() {
		return branch_code;
	}

	public void setBranch_code(String branch_code) {
		this.branch_code = branch_code;
	}


}
