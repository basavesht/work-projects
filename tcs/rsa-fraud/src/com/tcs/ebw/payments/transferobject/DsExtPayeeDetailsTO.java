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
public class DsExtPayeeDetailsTO extends EBWTransferObject implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -561339934570414200L;
	private String life_user_id = null;
	private String extAcc_Nickname = null;
	private String externalPayeeId = null;
	private String key_Client_Id = null;

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
	 * @return the extAcc_Nickname
	 */
	public String getExtAcc_Nickname() {
		return extAcc_Nickname;
	}
	/**
	 * @param extAcc_Nickname the extAcc_Nickname to set
	 */
	public void setExtAcc_Nickname(String extAcc_Nickname) {
		this.extAcc_Nickname = extAcc_Nickname;
	}
	/**
	 * @return the externalPayeeId
	 */
	public String getExternalPayeeId() {
		return externalPayeeId;
	}
	/**
	 * @param externalPayeeId the externalPayeeId to set
	 */
	public void setExternalPayeeId(String externalPayeeId) {
		this.externalPayeeId = externalPayeeId;
	}
	/**
	 * @return the key_Client_Id
	 */
	public String getKey_Client_Id() {
		return key_Client_Id;
	}
	/**
	 * @param key_Client_Id the key_Client_Id to set
	 */
	public void setKey_Client_Id(String key_Client_Id) {
		this.key_Client_Id = key_Client_Id;
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
	    
	    retValue = "DsExtPayeeDetailsTO ( "
	        + super.toString() + TAB
	        + "life_user_id = " + this.life_user_id + TAB
	        + "extAcc_Nickname = " + this.extAcc_Nickname + TAB
	        + "externalPayeeId = " + this.externalPayeeId + TAB
	        + "key_Client_Id = " + this.key_Client_Id + TAB
	        + " )";
	
	    return retValue;
	}

}