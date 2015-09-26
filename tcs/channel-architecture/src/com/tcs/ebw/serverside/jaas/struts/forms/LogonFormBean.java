
/* Created on Dec 20, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.tcs.ebw.serverside.jaas.struts.forms;

import javax.servlet.http.HttpServletRequest;
import com.tcs.ebw.mvc.validator.EbwForm;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

/**
 * This Class is a FormBean class for the Logon Form used for Logging.
 */
public class LogonFormBean extends ActionForm{
	
	private String username;
	private String password;
	private int expdays;
	private int pwdwarnperiod;
	private int pwdexpperiod;
	private String logout;
	private String IMRSSessionId;
	private String IMRSUserId;

	/**
	 * @see org.apache.struts.action.ActionForm#reset(org.apache.struts.action.ActionMapping, javax.servlet.http.HttpServletRequest)
	 */
	public void reset(ActionMapping arg0, HttpServletRequest arg1) {
		username="";
		password="";
		
	}
	
	/**
	 * @return Returns the password.
	 */
	public String getPassword() {
		return password;
	}
	/**
	 * @param password The password to set.
	 */
	public void setPassword(String password) {
		this.password = password;
	}
	/**
	 * @return Returns the username.
	 */
	public String getPwdwarnperiod() {
		return username;
	}
	/**
	 * @param username The username to set.
	 */
	public void setPwdwarnperiod(int pwdwarnperiod) {
		this.pwdwarnperiod = pwdwarnperiod;
	}
	/**
	 * @return Returns the username.
	 */
	public int getPwdexpperiod() {
		return pwdexpperiod;
	}
	/**
	 * @param username The username to set.
	 */
	public void setPwdexpperiod(int pwdexpperiod) {
		this.pwdexpperiod = pwdexpperiod;
	}
	/**
	 * @return Returns the username.
	 */
	public int getExpdays() {
		return expdays;
	}
	/**
	 * @param username The username to set.
	 */
	public void setExpdays(int expdays) {
		this.expdays= expdays;
	}
	/**
	 * @return Returns the username.
	 */
	public String getUsername() {
		return username;
	}
	/**
	 * @param username The username to set.
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	public String getIMRSSessionId() {
		return IMRSSessionId;
	}

	public void setIMRSSessionId(String sessionId) {
		IMRSSessionId = sessionId;
	}

	public String getIMRSUserId() {
		return IMRSUserId;
	}

	public void setIMRSUserId(String userId) {
		IMRSUserId = userId;
	}

	public String getLogout() {
		return logout;
	}

	public void setLogout(String logout) {
		this.logout = logout;
	}
	

}
