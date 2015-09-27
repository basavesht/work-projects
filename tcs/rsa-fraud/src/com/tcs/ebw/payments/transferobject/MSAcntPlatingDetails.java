package com.tcs.ebw.payments.transferobject;

import java.io.Serializable;

/**
 * @author : 224703
 * **************   Revision History ************************
 * Modified-By |  Modified-Date |  Project-Phase  | Changes
 *
 * **********************************************************
 */
public class MSAcntPlatingDetails implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8715777028575909790L;
	private String appl_id = null;
	private String server_ip = null;
	private String user_id =null;
	private String account_number = null;
	private String fa = null;
	private String office =null;

	/**
	 * @return the appl_id
	 */
	public String getAppl_id() {
		return appl_id;
	}
	/**
	 * @param appl_id the appl_id to set
	 */
	public void setAppl_id(String appl_id) {
		this.appl_id = appl_id;
	}
	/**
	 * @return the server_ip
	 */
	public String getServer_ip() {
		return server_ip;
	}
	/**
	 * @param server_ip the server_ip to set
	 */
	public void setServer_ip(String server_ip) {
		this.server_ip = server_ip;
	}
	/**
	 * @return the user_id
	 */
	public String getUser_id() {
		return user_id;
	}
	/**
	 * @param user_id the user_id to set
	 */
	public void setUser_id(String user_id) {
		this.user_id = user_id;
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
	 * @return the fa
	 */
	public String getFa() {
		return fa;
	}
	/**
	 * @param fa the fa to set
	 */
	public void setFa(String fa) {
		this.fa = fa;
	}
	/**
	 * @return the office
	 */
	public String getOffice() {
		return office;
	}
	/**
	 * @param office the office to set
	 */
	public void setOffice(String office) {
		this.office = office;
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

		retValue = "MSAccountDetails ( "
			+ super.toString() + TAB
			+ "appl_id = " + this.appl_id + TAB
			+ "server_ip = " + this.server_ip + TAB
			+ "user_id = " + this.user_id + TAB
			+ "account_number = " + this.account_number + TAB
			+ "fa = " + this.fa + TAB
			+ "office = " + this.office + TAB
			+ " )";

		return retValue;
	}


}
