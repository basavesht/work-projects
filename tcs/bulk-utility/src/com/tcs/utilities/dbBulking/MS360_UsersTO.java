package com.tcs.utilities.dbBulking;

import java.io.Serializable;

/**
 * @author 224703
 *
 */
public class MS360_UsersTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String racf_id = null;
	private String branch = null;
	private String initiator_role = null;
	private String approver_role = null;

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
	 * @return the branch
	 */
	public String getBranch() {
		return branch;
	}
	/**
	 * @param branch the branch to set
	 */
	public void setBranch(String branch) {
		this.branch = branch;
	}
	/**
	 * @return the initiator_role
	 */
	public String getInitiator_role() {
		return initiator_role;
	}
	/**
	 * @param initiator_role the initiator_role to set
	 */
	public void setInitiator_role(String initiator_role) {
		this.initiator_role = initiator_role;
	}
	/**
	 * @return the approver_role
	 */
	public String getApprover_role() {
		return approver_role;
	}
	/**
	 * @param approver_role the approver_role to set
	 */
	public void setApprover_role(String approver_role) {
		this.approver_role = approver_role;
	}

}
