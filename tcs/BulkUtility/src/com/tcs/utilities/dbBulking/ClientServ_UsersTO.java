package com.tcs.utilities.dbBulking;

import java.io.Serializable;

/**
 * @author 224703
 *
 */
public class ClientServ_UsersTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String uuid = null;
	private String key_client_id = null;

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


}
