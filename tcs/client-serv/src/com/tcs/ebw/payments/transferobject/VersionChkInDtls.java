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

	/**
	 * 
	 */
	private static final long serialVersionUID = 1804720143609493477L;
	private int currentVersion ; // Holds the current version of the record(Transaction or Account).
	private int currentBatVersion ; // Holds the current batch version of the record(Transaction or Account).
	private int currentParVersion ; // Holds the current parent txn version of the record(Transaction or Account).
	private String txnReferenceId = null; // Holds the unique reference number(Id) either for transaction //Paypayref
	private String txnBatchRefId = null; // Holds the unique reference number 2(Id) either for transaction //PaybatchRef
	private String versionChkType = null; // Holds the type of version check type( Values for the same are Account or Transaction).
	private String accReferenceId = null; // Holds the account reference Id ( Needs to be set in case of account version check only )
	private String accRefNumber = null; // Holds the account number ( Needs to be set in case of account version check only )

	public int getCurrentVersion() {
		return currentVersion;
	}
	public void setCurrentVersion(int currentVersion) {
		this.currentVersion = currentVersion;
	}
	public String getTxnReferenceId() {
		return txnReferenceId;
	}
	public void setTxnReferenceId(String txnReferenceId) {
		this.txnReferenceId = txnReferenceId;
	}
	public String getTxnBatchRefId() {
		return txnBatchRefId;
	}
	public void setTxnBatchRefId(String txnBatchRefId) {
		this.txnBatchRefId = txnBatchRefId;
	}
	public String getVersionChkType() {
		return versionChkType;
	}
	public void setVersionChkType(String versionChkType) {
		this.versionChkType = versionChkType;
	}
	public String getAccReferenceId() {
		return accReferenceId;
	}
	public void setAccReferenceId(String accReferenceId) {
		this.accReferenceId = accReferenceId;
	}
	public String getAccRefNumber() {
		return accRefNumber;
	}
	public void setAccRefNumber(String accRefNumber) {
		this.accRefNumber = accRefNumber;
	}
	public int getCurrentBatVersion() {
		return currentBatVersion;
	}
	public void setCurrentBatVersion(int currentBatVersion) {
		this.currentBatVersion = currentBatVersion;
	}
	public int getCurrentParVersion() {
		return currentParVersion;
	}
	public void setCurrentParVersion(int currentParVersion) {
		this.currentParVersion = currentParVersion;
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
	        + "txnReferenceId = " + this.txnReferenceId + TAB
	        + "txnBatchRefId = " + this.txnBatchRefId + TAB
	        + "versionChkType = " + this.versionChkType + TAB
	        + "accReferenceId = " + this.accReferenceId + TAB
	        + "accRefNumber = " + this.accRefNumber + TAB
	        + " )";
	
	    return retValue;
	}
}
