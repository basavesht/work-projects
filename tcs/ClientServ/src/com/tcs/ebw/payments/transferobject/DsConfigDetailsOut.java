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
public class DsConfigDetailsOut extends EBWTransferObject implements java.io.Serializable {
  
	/**
	 * 
	 */
	private static final long serialVersionUID = 806825819122461379L;
	Date business_date;

	public Date getBusiness_date() {
		return business_date;
	}

	public void setBusiness_date(Date business_date) {
		this.business_date = business_date;
	}

}
