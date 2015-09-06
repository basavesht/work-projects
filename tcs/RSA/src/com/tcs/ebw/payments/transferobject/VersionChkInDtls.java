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
public class VersionChkInDtls extends EBWTransferObject implements java.io.Serializable {

	private static final long serialVersionUID = 1804720143609493477L;
	private int currentVersion ; 
	private int currentBatVersion ; 
	private int currentParVersion ; 
	private int currentChkReqVersion ; 
	private String txnReferenceId = null; 
	private String txnBatchRefId = null; 
	private String versionChkType = null; 
	private String accReferenceId = null; 
	private String accRefNumber = null;

	/**
	 * @return the currentVersion
	 */
	public int getCurrentVersion() {
		return currentVersion;
	}
	/**
	 * @param currentVersion the currentVersion to set
	 */
	public void setCurrentVersion(int currentVersion) {
		this.currentVersion = currentVersion;
	}
	/**
	 * @return the currentBatVersion
	 */
	public int getCurrentBatVersion() {
		return currentBatVersion;
	}
	/**
	 * @param currentBatVersion the currentBatVersion to set
	 */
	public void setCurrentBatVersion(int currentBatVersion) {
		this.currentBatVersion = currentBatVersion;
	}
	/**
	 * @return the currentParVersion
	 */
	public int getCurrentParVersion() {
		return currentParVersion;
	}
	/**
	 * @param currentParVersion the currentParVersion to set
	 */
	public void setCurrentParVersion(int currentParVersion) {
		this.currentParVersion = currentParVersion;
	}
	/**
	 * @return the currentChkReqVersion
	 */
	public int getCurrentChkReqVersion() {
		return currentChkReqVersion;
	}
	/**
	 * @param currentChkReqVersion the currentChkReqVersion to set
	 */
	public void setCurrentChkReqVersion(int currentChkReqVersion) {
		this.currentChkReqVersion = currentChkReqVersion;
	}
	/**
	 * @return the txnReferenceId
	 */
	public String getTxnReferenceId() {
		return txnReferenceId;
	}
	/**
	 * @param txnReferenceId the txnReferenceId to set
	 */
	public void setTxnReferenceId(String txnReferenceId) {
		this.txnReferenceId = txnReferenceId;
	}
	/**
	 * @return the txnBatchRefId
	 */
	public String getTxnBatchRefId() {
		return txnBatchRefId;
	}
	/**
	 * @param txnBatchRefId the txnBatchRefId to set
	 */
	public void setTxnBatchRefId(String txnBatchRefId) {
		this.txnBatchRefId = txnBatchRefId;
	}
	/**
	 * @return the versionChkType
	 */
	public String getVersionChkType() {
		return versionChkType;
	}
	/**
	 * @param versionChkType the versionChkType to set
	 */
	public void setVersionChkType(String versionChkType) {
		this.versionChkType = versionChkType;
	}
	/**
	 * @return the accReferenceId
	 */
	public String getAccReferenceId() {
		return accReferenceId;
	}
	/**
	 * @param accReferenceId the accReferenceId to set
	 */
	public void setAccReferenceId(String accReferenceId) {
		this.accReferenceId = accReferenceId;
	}
	/**
	 * @return the accRefNumber
	 */
	public String getAccRefNumber() {
		return accRefNumber;
	}
	/**
	 * @param accRefNumber the accRefNumber to set
	 */
	public void setAccRefNumber(String accRefNumber) {
		this.accRefNumber = accRefNumber;
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

		retValue = "VersionChkInDtls ( "
			+ super.toString() + TAB
			+ "currentVersion = " + this.currentVersion + TAB
			+ "currentBatVersion = " + this.currentBatVersion + TAB
			+ "currentParVersion = " + this.currentParVersion + TAB
			+ "currentChkReqVersion = " + this.currentChkReqVersion + TAB
			+ "txnReferenceId = " + this.txnReferenceId + TAB
			+ "txnBatchRefId = " + this.txnBatchRefId + TAB
			+ "versionChkType = " + this.versionChkType + TAB
			+ "accReferenceId = " + this.accReferenceId + TAB
			+ "accRefNumber = " + this.accRefNumber + TAB
			+ " )";

		return retValue;
	} 

}