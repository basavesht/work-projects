/**
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
public class DAPChkInputDetails  extends EBWTransferObject implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5106279532954912146L;
	private String life_user_id = null;
	private String external_ref_id = null;
	private String external_routing_num = null;
	private String[] key_client_id = null;
	
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
	 * @return the external_ref_id
	 */
	public String getExternal_ref_id() {
		return external_ref_id;
	}
	/**
	 * @param external_ref_id the external_ref_id to set
	 */
	public void setExternal_ref_id(String external_ref_id) {
		this.external_ref_id = external_ref_id;
	}
	/**
	 * @return the external_routing_num
	 */
	public String getExternal_routing_num() {
		return external_routing_num;
	}
	/**
	 * @param external_routing_num the external_routing_num to set
	 */
	public void setExternal_routing_num(String external_routing_num) {
		this.external_routing_num = external_routing_num;
	}
	/**
	 * @return the key_client_id
	 */
	public String[] getKey_client_id() {
		return key_client_id;
	}
	/**
	 * @param key_client_id the key_client_id to set
	 */
	public void setKey_client_id(String[] key_client_id) {
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
	    
	    retValue = "DAPChkInputDetails ( "
	        + super.toString() + TAB
	        + "life_user_id = " + this.life_user_id + TAB
	        + "external_ref_id = " + this.external_ref_id + TAB
	        + "external_routing_num = " + this.external_routing_num + TAB
	        + "key_client_id = " + this.key_client_id + TAB
	        + " )";
	    return retValue;
	}
}