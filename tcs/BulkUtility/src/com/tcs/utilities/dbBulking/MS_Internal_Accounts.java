package com.tcs.utilities.dbBulking;

import java.io.Serializable;

/**
 * @author 224703
 *
 */
public class MS_Internal_Accounts implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String uuid = null;
	private String key_client_id = null;
	private String account_number = null;
	private String key_account_number = null;
	private String ms360_linked_account = null;
	private String racf_id = null;

	/**
	 * @return the uuid
	 */
	public String getUuid() {
		return uuid;
	}
	/**
	 * @param uuid the uuid to set
	 */
	public void setUuid(String uuid) {
		this.uuid = uuid;
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
	 * @return the account_number
	 */
	public String getAccount_number() {
		return account_number;
	}
	/**
	 * @param account_number the account_number to set
	 */
	public void setAccount_number(String account_number) {
		this.account_number = account_number;
	}
	/**
	 * @return the key_account_number
	 */
	public String getKey_account_number() {
		return key_account_number;
	}
	/**
	 * @param key_account_number the key_account_number to set
	 */
	public void setKey_account_number(String key_account_number) {
		this.key_account_number = key_account_number;
	}
	/**
	 * @return the racf_id
	 */
	public String getRacf_id() {
		return racf_id;
	}
	/**
	 * @param racf_id the racf_id to set
	 */
	public void setRacf_id(String racf_id) {
		this.racf_id = racf_id;
	}
	/**
	 * @return the ms360_linked_account
	 */
	public String getMs360_linked_account() {
		return ms360_linked_account;
	}
	/**
	 * @param ms360_linked_account the ms360_linked_account to set
	 */
	public void setMs360_linked_account(String ms360_linked_account) {
		this.ms360_linked_account = ms360_linked_account;
	}


}
