package com.tcs.ebw.payments.transferobject;

import com.tcs.ebw.transferobject.EBWTransferObject;

/**
 * @author : 224703
 * **************   Revision History ************************
 * Modified-By |  Modified-Date |  Project-Phase  | Changes
 *
 * **********************************************************
 */
public class DsUserDetails extends EBWTransferObject implements java.io.Serializable 
{
	private static final long serialVersionUID = -1549468000867629399L;
	private String usruserid = null;
	private String usrusername = null; 
	private String roletype= null;
	private String firstname= null;
	private String lastname= null;
	private String middlename= null;

	public String getUsruserid() {
		return usruserid;
	}
	public void setUsruserid(String usruserid) {
		this.usruserid = usruserid;
	}
	public String getUsrusername() {
		return usrusername;
	}
	public void setUsrusername(String usrusername) {
		this.usrusername = usrusername;
	}
	public String getRoletype() {
		return roletype;
	}
	public void setRoletype(String roletype) {
		this.roletype = roletype;
	}
	public String getFirstname() {
		return firstname;
	}
	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}
	public String getLastname() {
		return lastname;
	}
	public void setLastname(String lastname) {
		this.lastname = lastname;
	}
	public String getMiddlename() {
		return middlename;
	}
	public void setMiddlename(String middlename) {
		this.middlename = middlename;
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

		retValue = "DsUserDetails ( "
			+ super.toString() + TAB
			+ "usruserid = " + this.usruserid + TAB
			+ "usrusername = " + this.usrusername + TAB
			+ "roletype = " + this.roletype + TAB
			+ "firstname = " + this.firstname + TAB
			+ "lastname = " + this.lastname + TAB
			+ "middlename = " + this.middlename + TAB
			+ " )";

		return retValue;
	}

}
