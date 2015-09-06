package com.tcs.utilities.dbBulking;

import java.io.Serializable;

/**
 * @author 224703
 *
 */
public class MS_ACH_Accounts implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String key_client_id = null;
	private String external_acnt_ref_id = null;

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
	 * @return the external_acnt_ref_id
	 */
	public String getExternal_acnt_ref_id() {
		return external_acnt_ref_id;
	}
	/**
	 * @param external_acnt_ref_id the external_acnt_ref_id to set
	 */
	public void setExternal_acnt_ref_id(String external_acnt_ref_id) {
		this.external_acnt_ref_id = external_acnt_ref_id;
	}
}
