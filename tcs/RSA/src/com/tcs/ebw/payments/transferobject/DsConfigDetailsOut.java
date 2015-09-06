/**
 * 
 */
package com.tcs.ebw.payments.transferobject;

import java.util.Date;

import com.tcs.ebw.transferobject.EBWTransferObject;

/**
 * @author : 224703
 * **************   Revision History ************************
 * Modified-By |  Modified-Date |  Project-Phase  | Changes
 *
 * **********************************************************
 */
public class DsConfigDetailsOut extends EBWTransferObject {
  
	private Date business_date;
	private String fa_id;
	private String branch_id;

	public Date getBusiness_date() {
		return business_date;
	}

	public void setBusiness_date(Date business_date) {
		this.business_date = business_date;
	}

	public String getFa_id() {
		return fa_id;
	}

	public void setFa_id(String fa_id) {
		this.fa_id = fa_id;
	}

	public String getBranch_id() {
		return branch_id;
	}

	public void setBranch_id(String branch_id) {
		this.branch_id = branch_id;
	}
}
