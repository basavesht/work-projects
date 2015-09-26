/*
 * Created on Sat Apr 11 09:28:46 IST 2009
 *
 * Copyright Tata Consultancy Services. All rights reserved.
 * 
 */

package com.tcs.ebw.payments.transferobject;

import com.tcs.ebw.transferobject.EBWTransferObject;

/**
 * @author : 224703
 * **************   Revision History ************************
 * Modified-By |  Modified-Date |  Project-Phase  | Changes
 *
 * **********************************************************
 */
public class DsOnloadAccDetailsTO extends EBWTransferObject implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6815349819692164213L;
	private String life_user_id = null;
	private String key_client_id = null;
	
	/**
	 * @return the life_user_id
	 */
	public String getLife_user_id() {
		return life_user_id;
	}
	/**
	 * @param life_user_id the life_user_id to set
	 */
	public void setLife_user_id(String life_user_id) {
		this.life_user_id = life_user_id;
	}
	/**
	 * @return the key_client_id
	 */
	public String getKey_client_id() {
		return key_client_id;
	}
	/**
	 * @param key_client_id the key_client_id to set
	 */
	public void setKey_client_id(String key_client_id) {
		this.key_client_id = key_client_id;
	}
	
	/**
	 * Constructs a <code>String</code> with all attributes
	 * in name = value format.
	 *
	 * @return a <code>String</code> representation 
	 * of this object.
	 */
	public String toString()
	{
	    final String TAB = "\r\n";
	    
	    String retValue = "";
	    
	    retValue = "DsOnloadAccDetailsTO ( "
	        + super.toString() + TAB
	        + "life_user_id = " + this.life_user_id + TAB
	        + "key_client_id = " + this.key_client_id + TAB
	        + " )";
	
	    return retValue;
	}
}