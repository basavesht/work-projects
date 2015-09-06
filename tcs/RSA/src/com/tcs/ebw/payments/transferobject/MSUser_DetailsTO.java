package com.tcs.ebw.payments.transferobject;

import java.io.Serializable;
import java.util.ArrayList;

import com.tcs.bancs.ms360.integration.AccessibleAccount;
import com.tcs.bancs.ms360.integration.UserAction;

/**
 * @author : 224703
 * **************   Revision History ************************
 * Modified-By |  Modified-Date |  Project-Phase  | Changes
 *
 * **********************************************************
 */
public class MSUser_DetailsTO implements Serializable {

	private static final long serialVersionUID = 3742173250866470131L;

	//MS360 related attributes...
	private String firstName =null;
	private String middleName =null;
	private String lastName =null;
	private String rcafId =null;
	private String initiatorRole =null; 
	private String[] approveRole = new String[10]; 
	private ArrayList<UserAction> userActions = new ArrayList<UserAction>();
	private ArrayList<AccessibleAccount> accessibleAccounts = new ArrayList<AccessibleAccount>(); 
	private ArrayList functionalRoleList = new ArrayList(); 
	private ArrayList contextAccounts = new ArrayList();

	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getMiddleName() {
		return middleName;
	}
	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getRcafId() {
		return rcafId;
	}
	public void setRcafId(String rcafId) {
		this.rcafId = rcafId;
	}
	public String getInitiatorRole() {
		return initiatorRole;
	}
	public void setInitiatorRole(String initiatorRole) {
		this.initiatorRole = initiatorRole;
	}
	public String[] getApproveRole() {
		return approveRole;
	}
	public void setApproveRole(String[] approveRole) {
		this.approveRole = approveRole;
	}
	public ArrayList<UserAction> getUserActions() {
		return userActions;
	}
	public void setUserActions(ArrayList<UserAction> userActions) {
		this.userActions = userActions;
	}
	public ArrayList<AccessibleAccount> getAccessibleAccounts() {
		return accessibleAccounts;
	}
	public void setAccessibleAccounts(
			ArrayList<AccessibleAccount> accessibleAccounts) {
		this.accessibleAccounts = accessibleAccounts;
	}
	public ArrayList getFunctionalRoleList() {
		return functionalRoleList;
	}
	public void setFunctionalRoleList(ArrayList functionalRoleList) {
		this.functionalRoleList = functionalRoleList;
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

		retValue = "MSUser_DetailsTO ( "
			+ super.toString() + TAB
			+ "firstName = " + this.firstName + TAB
			+ "middleName = " + this.middleName + TAB
			+ "lastName = " + this.lastName + TAB
			+ "rcafId = " + this.rcafId + TAB
			+ "initiatorRole = " + this.initiatorRole + TAB
			+ "approveRole = " + this.approveRole + TAB
			+ "userActions = " + this.userActions + TAB
			+ "accessibleAccounts = " + this.accessibleAccounts + TAB
			+ "functionalRoleList = " + this.functionalRoleList + TAB
			+ " )";

		return retValue;
	}
	/**
	 * @return the contextAccounts
	 */
	public ArrayList getContextAccounts() {
		return contextAccounts;
	}
	/**
	 * @param contextAccounts the contextAccounts to set
	 */
	public void setContextAccounts(ArrayList contextAccounts) {
		this.contextAccounts = contextAccounts;
	}
}