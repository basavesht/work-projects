/*
 * Created on Wed Jun 03 16:06:16 IST 2009
 *
 * Copyright Tata Consultancy Services. All rights reserved.
 * 
 */

package com.tcs.ebw.payments.transferobject;

import java.math.BigDecimal;
import java.sql.Timestamp;

import com.tcs.ebw.transferobject.EBWTransferObject;

/**
 * @author : 224703
 * **************   Revision History ************************
 * Modified-By |  Modified-Date |  Project-Phase  | Changes
 *    224703       10-05-2011        2               CR 215 
 *    224703       29-08-2011        3-B             CR 193
 * **********************************************************
 */
public class TrxnDetailsOutputTO extends EBWTransferObject implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6141760808454129211L;
	private String confirmationNo = null;
	private String trxnType = null;
	private BigDecimal trxnAmount = null;
	private String frequencyType = null;
	private String accTierFromAcc = null;
	private String accTierToAcc = null;
	private String accNoFromAcc = null;
	private String accNoToAcc = null;
	private String routingNoFromAcc = null;
	private String routingNoToAcc = null;
	private String ownerFromAcc = null;
	private String ownerToAcc = null;
	private String accHeldAtFromAcc = null;
	private String accHeldAtToAcc = null;
	private String accClassFromAcc = null;
	private String accClassToAcc = null;
	private String accSubClassFromAcc = null;
	private String accSubClassToAcc = null;
	private String accNameFromAcc = null;
	private String accNameToAcc = null;
	private String accDtlsFromAcc = null;
	private String accDtlsToAcc = null;
	private String faIdFromAcc = null;
	private String faIdToAcc = null;
	private String keyAccNoFromAcc = null;
	private String keyAccNoToAcc = null;
	private String officeAccNoFromAcc = null;
	private String officeAccNoToAcc = null;
	private String accTypeFromAcc = null;
	private String accTypeToAcc = null;
	private String branchIdFromAcc=null;
	private String branchIdToAcc=null;
	private String status = null;
	private String paybatchref = null;
	private String statusCode=null;
	private String txnTypeCode = null;
	private Double versionnum=null;
	private Double batversionnum = null;
	private Double partxnversionnum = null;
	private Timestamp txnEntryDate = null;
	private Timestamp txnInitiationDate = null;
	private Timestamp txnSettleDate = null;
	private java.util.ArrayList actionsList = null;
	private java.util.ArrayList reasonCodesList = null;
	private java.util.ArrayList authDetailsList = null;
	private java.util.ArrayList signedAuthDetailsList = null;
	private String auth_mode=null;
	private String recurringFlag=null;
	private String recurringActiveFlg=null;
	private Timestamp business_Date=null;
	private String retirement_TxnTypeDesc = null;
	private String qualifier = null;
	private String reverse_qualifier = null;
	private String retirement_mnemonic = null;
	private BigDecimal untilDollarLimit = null;

	//Check transactions mappings details..
	private String certifiedFlag = null;
	private String checkNo = null;
	private String trackingId = null;	
	private String payeeToName = null;	
	private String printingBranch = null;	
	private String printMemoOn = null;
	private String deliveredToType = null;
	private String deliveredToName = null;
	private String typeOfId = null;
	private String idNumber = null;
	private String thirdPartyReason = null;
	private String deliveryAddress = null;	
	private String checkMemo = null;
	private String foreignAddressFlag = null;
	private String defaultAddressFlag = null;
	private String printMemoCheckFlag = null;
	private String printMemoStubFlag = null;
	private String sameNameFlag = null;
	private String deliveryOption = null;
	private String fee = null;
	private String requestedPrintDate = null;
	private String ofac_case_id = null;
	private String display_qualifier = null;

	/**
	 * @return the confirmationNo
	 */
	public String getConfirmationNo() {
		return confirmationNo;
	}
	/**
	 * @param confirmationNo the confirmationNo to set
	 */
	public void setConfirmationNo(String confirmationNo) {
		this.confirmationNo = confirmationNo;
	}
	/**
	 * @return the trxnType
	 */
	public String getTrxnType() {
		return trxnType;
	}
	/**
	 * @param trxnType the trxnType to set
	 */
	public void setTrxnType(String trxnType) {
		this.trxnType = trxnType;
	}
	/**
	 * @return the trxnAmount
	 */
	public BigDecimal getTrxnAmount() {
		return trxnAmount;
	}
	/**
	 * @param trxnAmount the trxnAmount to set
	 */
	public void setTrxnAmount(BigDecimal trxnAmount) {
		this.trxnAmount = trxnAmount;
	}
	/**
	 * @return the frequencyType
	 */
	public String getFrequencyType() {
		return frequencyType;
	}
	/**
	 * @param frequencyType the frequencyType to set
	 */
	public void setFrequencyType(String frequencyType) {
		this.frequencyType = frequencyType;
	}
	/**
	 * @return the accTierFromAcc
	 */
	public String getAccTierFromAcc() {
		return accTierFromAcc;
	}
	/**
	 * @param accTierFromAcc the accTierFromAcc to set
	 */
	public void setAccTierFromAcc(String accTierFromAcc) {
		this.accTierFromAcc = accTierFromAcc;
	}
	/**
	 * @return the accTierToAcc
	 */
	public String getAccTierToAcc() {
		return accTierToAcc;
	}
	/**
	 * @param accTierToAcc the accTierToAcc to set
	 */
	public void setAccTierToAcc(String accTierToAcc) {
		this.accTierToAcc = accTierToAcc;
	}
	/**
	 * @return the accNoFromAcc
	 */
	public String getAccNoFromAcc() {
		return accNoFromAcc;
	}
	/**
	 * @param accNoFromAcc the accNoFromAcc to set
	 */
	public void setAccNoFromAcc(String accNoFromAcc) {
		this.accNoFromAcc = accNoFromAcc;
	}
	/**
	 * @return the accNoToAcc
	 */
	public String getAccNoToAcc() {
		return accNoToAcc;
	}
	/**
	 * @param accNoToAcc the accNoToAcc to set
	 */
	public void setAccNoToAcc(String accNoToAcc) {
		this.accNoToAcc = accNoToAcc;
	}
	/**
	 * @return the routingNoFromAcc
	 */
	public String getRoutingNoFromAcc() {
		return routingNoFromAcc;
	}
	/**
	 * @param routingNoFromAcc the routingNoFromAcc to set
	 */
	public void setRoutingNoFromAcc(String routingNoFromAcc) {
		this.routingNoFromAcc = routingNoFromAcc;
	}
	/**
	 * @return the routingNoToAcc
	 */
	public String getRoutingNoToAcc() {
		return routingNoToAcc;
	}
	/**
	 * @param routingNoToAcc the routingNoToAcc to set
	 */
	public void setRoutingNoToAcc(String routingNoToAcc) {
		this.routingNoToAcc = routingNoToAcc;
	}
	/**
	 * @return the ownerFromAcc
	 */
	public String getOwnerFromAcc() {
		return ownerFromAcc;
	}
	/**
	 * @param ownerFromAcc the ownerFromAcc to set
	 */
	public void setOwnerFromAcc(String ownerFromAcc) {
		this.ownerFromAcc = ownerFromAcc;
	}
	/**
	 * @return the ownerToAcc
	 */
	public String getOwnerToAcc() {
		return ownerToAcc;
	}
	/**
	 * @param ownerToAcc the ownerToAcc to set
	 */
	public void setOwnerToAcc(String ownerToAcc) {
		this.ownerToAcc = ownerToAcc;
	}
	/**
	 * @return the accHeldAtFromAcc
	 */
	public String getAccHeldAtFromAcc() {
		return accHeldAtFromAcc;
	}
	/**
	 * @param accHeldAtFromAcc the accHeldAtFromAcc to set
	 */
	public void setAccHeldAtFromAcc(String accHeldAtFromAcc) {
		this.accHeldAtFromAcc = accHeldAtFromAcc;
	}
	/**
	 * @return the accHeldAtToAcc
	 */
	public String getAccHeldAtToAcc() {
		return accHeldAtToAcc;
	}
	/**
	 * @param accHeldAtToAcc the accHeldAtToAcc to set
	 */
	public void setAccHeldAtToAcc(String accHeldAtToAcc) {
		this.accHeldAtToAcc = accHeldAtToAcc;
	}
	/**
	 * @return the accClassFromAcc
	 */
	public String getAccClassFromAcc() {
		return accClassFromAcc;
	}
	/**
	 * @param accClassFromAcc the accClassFromAcc to set
	 */
	public void setAccClassFromAcc(String accClassFromAcc) {
		this.accClassFromAcc = accClassFromAcc;
	}
	/**
	 * @return the accClassToAcc
	 */
	public String getAccClassToAcc() {
		return accClassToAcc;
	}
	/**
	 * @param accClassToAcc the accClassToAcc to set
	 */
	public void setAccClassToAcc(String accClassToAcc) {
		this.accClassToAcc = accClassToAcc;
	}
	/**
	 * @return the accSubClassFromAcc
	 */
	public String getAccSubClassFromAcc() {
		return accSubClassFromAcc;
	}
	/**
	 * @param accSubClassFromAcc the accSubClassFromAcc to set
	 */
	public void setAccSubClassFromAcc(String accSubClassFromAcc) {
		this.accSubClassFromAcc = accSubClassFromAcc;
	}
	/**
	 * @return the accSubClassToAcc
	 */
	public String getAccSubClassToAcc() {
		return accSubClassToAcc;
	}
	/**
	 * @param accSubClassToAcc the accSubClassToAcc to set
	 */
	public void setAccSubClassToAcc(String accSubClassToAcc) {
		this.accSubClassToAcc = accSubClassToAcc;
	}
	/**
	 * @return the accNameFromAcc
	 */
	public String getAccNameFromAcc() {
		return accNameFromAcc;
	}
	/**
	 * @param accNameFromAcc the accNameFromAcc to set
	 */
	public void setAccNameFromAcc(String accNameFromAcc) {
		this.accNameFromAcc = accNameFromAcc;
	}
	/**
	 * @return the accNameToAcc
	 */
	public String getAccNameToAcc() {
		return accNameToAcc;
	}
	/**
	 * @param accNameToAcc the accNameToAcc to set
	 */
	public void setAccNameToAcc(String accNameToAcc) {
		this.accNameToAcc = accNameToAcc;
	}
	/**
	 * @return the accDtlsFromAcc
	 */
	public String getAccDtlsFromAcc() {
		return accDtlsFromAcc;
	}
	/**
	 * @param accDtlsFromAcc the accDtlsFromAcc to set
	 */
	public void setAccDtlsFromAcc(String accDtlsFromAcc) {
		this.accDtlsFromAcc = accDtlsFromAcc;
	}
	/**
	 * @return the accDtlsToAcc
	 */
	public String getAccDtlsToAcc() {
		return accDtlsToAcc;
	}
	/**
	 * @param accDtlsToAcc the accDtlsToAcc to set
	 */
	public void setAccDtlsToAcc(String accDtlsToAcc) {
		this.accDtlsToAcc = accDtlsToAcc;
	}
	/**
	 * @return the faIdFromAcc
	 */
	public String getFaIdFromAcc() {
		return faIdFromAcc;
	}
	/**
	 * @param faIdFromAcc the faIdFromAcc to set
	 */
	public void setFaIdFromAcc(String faIdFromAcc) {
		this.faIdFromAcc = faIdFromAcc;
	}
	/**
	 * @return the faIdToAcc
	 */
	public String getFaIdToAcc() {
		return faIdToAcc;
	}
	/**
	 * @param faIdToAcc the faIdToAcc to set
	 */
	public void setFaIdToAcc(String faIdToAcc) {
		this.faIdToAcc = faIdToAcc;
	}
	/**
	 * @return the keyAccNoFromAcc
	 */
	public String getKeyAccNoFromAcc() {
		return keyAccNoFromAcc;
	}
	/**
	 * @param keyAccNoFromAcc the keyAccNoFromAcc to set
	 */
	public void setKeyAccNoFromAcc(String keyAccNoFromAcc) {
		this.keyAccNoFromAcc = keyAccNoFromAcc;
	}
	/**
	 * @return the keyAccNoToAcc
	 */
	public String getKeyAccNoToAcc() {
		return keyAccNoToAcc;
	}
	/**
	 * @param keyAccNoToAcc the keyAccNoToAcc to set
	 */
	public void setKeyAccNoToAcc(String keyAccNoToAcc) {
		this.keyAccNoToAcc = keyAccNoToAcc;
	}
	/**
	 * @return the officeAccNoFromAcc
	 */
	public String getOfficeAccNoFromAcc() {
		return officeAccNoFromAcc;
	}
	/**
	 * @param officeAccNoFromAcc the officeAccNoFromAcc to set
	 */
	public void setOfficeAccNoFromAcc(String officeAccNoFromAcc) {
		this.officeAccNoFromAcc = officeAccNoFromAcc;
	}
	/**
	 * @return the officeAccNoToAcc
	 */
	public String getOfficeAccNoToAcc() {
		return officeAccNoToAcc;
	}
	/**
	 * @param officeAccNoToAcc the officeAccNoToAcc to set
	 */
	public void setOfficeAccNoToAcc(String officeAccNoToAcc) {
		this.officeAccNoToAcc = officeAccNoToAcc;
	}
	/**
	 * @return the accTypeFromAcc
	 */
	public String getAccTypeFromAcc() {
		return accTypeFromAcc;
	}
	/**
	 * @param accTypeFromAcc the accTypeFromAcc to set
	 */
	public void setAccTypeFromAcc(String accTypeFromAcc) {
		this.accTypeFromAcc = accTypeFromAcc;
	}
	/**
	 * @return the accTypeToAcc
	 */
	public String getAccTypeToAcc() {
		return accTypeToAcc;
	}
	/**
	 * @param accTypeToAcc the accTypeToAcc to set
	 */
	public void setAccTypeToAcc(String accTypeToAcc) {
		this.accTypeToAcc = accTypeToAcc;
	}
	/**
	 * @return the branchIdFromAcc
	 */
	public String getBranchIdFromAcc() {
		return branchIdFromAcc;
	}
	/**
	 * @param branchIdFromAcc the branchIdFromAcc to set
	 */
	public void setBranchIdFromAcc(String branchIdFromAcc) {
		this.branchIdFromAcc = branchIdFromAcc;
	}
	/**
	 * @return the branchIdToAcc
	 */
	public String getBranchIdToAcc() {
		return branchIdToAcc;
	}
	/**
	 * @param branchIdToAcc the branchIdToAcc to set
	 */
	public void setBranchIdToAcc(String branchIdToAcc) {
		this.branchIdToAcc = branchIdToAcc;
	}
	/**
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}
	/**
	 * @param status the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
	}
	/**
	 * @return the paybatchref
	 */
	public String getPaybatchref() {
		return paybatchref;
	}
	/**
	 * @param paybatchref the paybatchref to set
	 */
	public void setPaybatchref(String paybatchref) {
		this.paybatchref = paybatchref;
	}
	/**
	 * @return the statusCode
	 */
	public String getStatusCode() {
		return statusCode;
	}
	/**
	 * @param statusCode the statusCode to set
	 */
	public void setStatusCode(String statusCode) {
		this.statusCode = statusCode;
	}
	/**
	 * @return the txnTypeCode
	 */
	public String getTxnTypeCode() {
		return txnTypeCode;
	}
	/**
	 * @param txnTypeCode the txnTypeCode to set
	 */
	public void setTxnTypeCode(String txnTypeCode) {
		this.txnTypeCode = txnTypeCode;
	}
	/**
	 * @return the versionnum
	 */
	public Double getVersionnum() {
		return versionnum;
	}
	/**
	 * @param versionnum the versionnum to set
	 */
	public void setVersionnum(Double versionnum) {
		this.versionnum = versionnum;
	}
	/**
	 * @return the batversionnum
	 */
	public Double getBatversionnum() {
		return batversionnum;
	}
	/**
	 * @param batversionnum the batversionnum to set
	 */
	public void setBatversionnum(Double batversionnum) {
		this.batversionnum = batversionnum;
	}
	/**
	 * @return the partxnversionnum
	 */
	public Double getPartxnversionnum() {
		return partxnversionnum;
	}
	/**
	 * @param partxnversionnum the partxnversionnum to set
	 */
	public void setPartxnversionnum(Double partxnversionnum) {
		this.partxnversionnum = partxnversionnum;
	}
	/**
	 * @return the txnEntryDate
	 */
	public Timestamp getTxnEntryDate() {
		return txnEntryDate;
	}
	/**
	 * @param txnEntryDate the txnEntryDate to set
	 */
	public void setTxnEntryDate(Timestamp txnEntryDate) {
		this.txnEntryDate = txnEntryDate;
	}
	/**
	 * @return the txnInitiationDate
	 */
	public Timestamp getTxnInitiationDate() {
		return txnInitiationDate;
	}
	/**
	 * @param txnInitiationDate the txnInitiationDate to set
	 */
	public void setTxnInitiationDate(Timestamp txnInitiationDate) {
		this.txnInitiationDate = txnInitiationDate;
	}
	/**
	 * @return the actionsList
	 */
	public java.util.ArrayList getActionsList() {
		return actionsList;
	}
	/**
	 * @param actionsList the actionsList to set
	 */
	public void setActionsList(java.util.ArrayList actionsList) {
		this.actionsList = actionsList;
	}
	/**
	 * @return the reasonCodesList
	 */
	public java.util.ArrayList getReasonCodesList() {
		return reasonCodesList;
	}
	/**
	 * @param reasonCodesList the reasonCodesList to set
	 */
	public void setReasonCodesList(java.util.ArrayList reasonCodesList) {
		this.reasonCodesList = reasonCodesList;
	}
	/**
	 * @return the authDetailsList
	 */
	public java.util.ArrayList getAuthDetailsList() {
		return authDetailsList;
	}
	/**
	 * @param authDetailsList the authDetailsList to set
	 */
	public void setAuthDetailsList(java.util.ArrayList authDetailsList) {
		this.authDetailsList = authDetailsList;
	}
	/**
	 * @return the auth_mode
	 */
	public String getAuth_mode() {
		return auth_mode;
	}
	/**
	 * @param auth_mode the auth_mode to set
	 */
	public void setAuth_mode(String auth_mode) {
		this.auth_mode = auth_mode;
	}
	/**
	 * @return the recurringFlag
	 */
	public String getRecurringFlag() {
		return recurringFlag;
	}
	/**
	 * @param recurringFlag the recurringFlag to set
	 */
	public void setRecurringFlag(String recurringFlag) {
		this.recurringFlag = recurringFlag;
	}
	/**
	 * @return the recurringActiveFlg
	 */
	public String getRecurringActiveFlg() {
		return recurringActiveFlg;
	}
	/**
	 * @param recurringActiveFlg the recurringActiveFlg to set
	 */
	public void setRecurringActiveFlg(String recurringActiveFlg) {
		this.recurringActiveFlg = recurringActiveFlg;
	}
	/**
	 * @return the business_Date
	 */
	public Timestamp getBusiness_Date() {
		return business_Date;
	}
	/**
	 * @param business_Date the business_Date to set
	 */
	public void setBusiness_Date(Timestamp business_Date) {
		this.business_Date = business_Date;
	}
	/**
	 * @return the retirement_TxnTypeDesc
	 */
	public String getRetirement_TxnTypeDesc() {
		return retirement_TxnTypeDesc;
	}
	/**
	 * @param retirement_TxnTypeDesc the retirement_TxnTypeDesc to set
	 */
	public void setRetirement_TxnTypeDesc(String retirement_TxnTypeDesc) {
		this.retirement_TxnTypeDesc = retirement_TxnTypeDesc;
	}
	/**
	 * @return the qualifier
	 */
	public String getQualifier() {
		return qualifier;
	}
	/**
	 * @param qualifier the qualifier to set
	 */
	public void setQualifier(String qualifier) {
		this.qualifier = qualifier;
	}
	/**
	 * @return the reverse_qualifier
	 */
	public String getReverse_qualifier() {
		return reverse_qualifier;
	}
	/**
	 * @param reverse_qualifier the reverse_qualifier to set
	 */
	public void setReverse_qualifier(String reverse_qualifier) {
		this.reverse_qualifier = reverse_qualifier;
	}
	/**
	 * @return the untilDollarLimit
	 */
	public BigDecimal getUntilDollarLimit() {
		return untilDollarLimit;
	}
	/**
	 * @param untilDollarLimit the untilDollarLimit to set
	 */
	public void setUntilDollarLimit(BigDecimal untilDollarLimit) {
		this.untilDollarLimit = untilDollarLimit;
	}
	/**
	 * @return the certifiedFlag
	 */
	public String getCertifiedFlag() {
		return certifiedFlag;
	}
	/**
	 * @param certifiedFlag the certifiedFlag to set
	 */
	public void setCertifiedFlag(String certifiedFlag) {
		this.certifiedFlag = certifiedFlag;
	}
	/**
	 * @return the checkNo
	 */
	public String getCheckNo() {
		return checkNo;
	}
	/**
	 * @param checkNo the checkNo to set
	 */
	public void setCheckNo(String checkNo) {
		this.checkNo = checkNo;
	}
	/**
	 * @return the trackingId
	 */
	public String getTrackingId() {
		return trackingId;
	}
	/**
	 * @param trackingId the trackingId to set
	 */
	public void setTrackingId(String trackingId) {
		this.trackingId = trackingId;
	}
	/**
	 * @return the payeeToName
	 */
	public String getPayeeToName() {
		return payeeToName;
	}
	/**
	 * @param payeeToName the payeeToName to set
	 */
	public void setPayeeToName(String payeeToName) {
		this.payeeToName = payeeToName;
	}
	/**
	 * @return the printingBranch
	 */
	public String getPrintingBranch() {
		return printingBranch;
	}
	/**
	 * @param printingBranch the printingBranch to set
	 */
	public void setPrintingBranch(String printingBranch) {
		this.printingBranch = printingBranch;
	}
	/**
	 * @return the printMemoOn
	 */
	public String getPrintMemoOn() {
		return printMemoOn;
	}
	/**
	 * @param printMemoOn the printMemoOn to set
	 */
	public void setPrintMemoOn(String printMemoOn) {
		this.printMemoOn = printMemoOn;
	}
	/**
	 * @return the deliveredToType
	 */
	public String getDeliveredToType() {
		return deliveredToType;
	}
	/**
	 * @param deliveredToType the deliveredToType to set
	 */
	public void setDeliveredToType(String deliveredToType) {
		this.deliveredToType = deliveredToType;
	}
	/**
	 * @return the deliveredToName
	 */
	public String getDeliveredToName() {
		return deliveredToName;
	}
	/**
	 * @param deliveredToName the deliveredToName to set
	 */
	public void setDeliveredToName(String deliveredToName) {
		this.deliveredToName = deliveredToName;
	}
	/**
	 * @return the typeOfId
	 */
	public String getTypeOfId() {
		return typeOfId;
	}
	/**
	 * @param typeOfId the typeOfId to set
	 */
	public void setTypeOfId(String typeOfId) {
		this.typeOfId = typeOfId;
	}
	/**
	 * @return the idNumber
	 */
	public String getIdNumber() {
		return idNumber;
	}
	/**
	 * @param idNumber the idNumber to set
	 */
	public void setIdNumber(String idNumber) {
		this.idNumber = idNumber;
	}
	/**
	 * @return the thirdPartyReason
	 */
	public String getThirdPartyReason() {
		return thirdPartyReason;
	}
	/**
	 * @param thirdPartyReason the thirdPartyReason to set
	 */
	public void setThirdPartyReason(String thirdPartyReason) {
		this.thirdPartyReason = thirdPartyReason;
	}
	/**
	 * @return the deliveryAddress
	 */
	public String getDeliveryAddress() {
		return deliveryAddress;
	}
	/**
	 * @param deliveryAddress the deliveryAddress to set
	 */
	public void setDeliveryAddress(String deliveryAddress) {
		this.deliveryAddress = deliveryAddress;
	}
	/**
	 * @return the checkMemo
	 */
	public String getCheckMemo() {
		return checkMemo;
	}
	/**
	 * @param checkMemo the checkMemo to set
	 */
	public void setCheckMemo(String checkMemo) {
		this.checkMemo = checkMemo;
	}
	/**
	 * @return the foreignAddressFlag
	 */
	public String getForeignAddressFlag() {
		return foreignAddressFlag;
	}
	/**
	 * @param foreignAddressFlag the foreignAddressFlag to set
	 */
	public void setForeignAddressFlag(String foreignAddressFlag) {
		this.foreignAddressFlag = foreignAddressFlag;
	}
	/**
	 * @return the defaultAddressFlag
	 */
	public String getDefaultAddressFlag() {
		return defaultAddressFlag;
	}
	/**
	 * @param defaultAddressFlag the defaultAddressFlag to set
	 */
	public void setDefaultAddressFlag(String defaultAddressFlag) {
		this.defaultAddressFlag = defaultAddressFlag;
	}
	/**
	 * @return the printMemoCheckFlag
	 */
	public String getPrintMemoCheckFlag() {
		return printMemoCheckFlag;
	}
	/**
	 * @param printMemoCheckFlag the printMemoCheckFlag to set
	 */
	public void setPrintMemoCheckFlag(String printMemoCheckFlag) {
		this.printMemoCheckFlag = printMemoCheckFlag;
	}
	/**
	 * @return the printMemoStubFlag
	 */
	public String getPrintMemoStubFlag() {
		return printMemoStubFlag;
	}
	/**
	 * @param printMemoStubFlag the printMemoStubFlag to set
	 */
	public void setPrintMemoStubFlag(String printMemoStubFlag) {
		this.printMemoStubFlag = printMemoStubFlag;
	}
	/**
	 * @return the sameNameFlag
	 */
	public String getSameNameFlag() {
		return sameNameFlag;
	}
	/**
	 * @param sameNameFlag the sameNameFlag to set
	 */
	public void setSameNameFlag(String sameNameFlag) {
		this.sameNameFlag = sameNameFlag;
	}
	/**
	 * @return the deliveryOption
	 */
	public String getDeliveryOption() {
		return deliveryOption;
	}
	/**
	 * @param deliveryOption the deliveryOption to set
	 */
	public void setDeliveryOption(String deliveryOption) {
		this.deliveryOption = deliveryOption;
	}
	/**
	 * @return the fee
	 */
	public String getFee() {
		return fee;
	}
	/**
	 * @param fee the fee to set
	 */
	public void setFee(String fee) {
		this.fee = fee;
	}
	/**
	 * @return the requestedPrintDate
	 */
	public String getRequestedPrintDate() {
		return requestedPrintDate;
	}
	/**
	 * @param requestedPrintDate the requestedPrintDate to set
	 */
	public void setRequestedPrintDate(String requestedPrintDate) {
		this.requestedPrintDate = requestedPrintDate;
	}
	/**
	 * @return the retirement_mnemonic
	 */
	public String getRetirement_mnemonic() {
		return retirement_mnemonic;
	}
	/**
	 * @param retirement_mnemonic the retirement_mnemonic to set
	 */
	public void setRetirement_mnemonic(String retirement_mnemonic) {
		this.retirement_mnemonic = retirement_mnemonic;
	}
	/**
	 * @return the signedAuthDetailsList
	 */
	public java.util.ArrayList getSignedAuthDetailsList() {
		return signedAuthDetailsList;
	}
	/**
	 * @param signedAuthDetailsList the signedAuthDetailsList to set
	 */
	public void setSignedAuthDetailsList(java.util.ArrayList signedAuthDetailsList) {
		this.signedAuthDetailsList = signedAuthDetailsList;
	}
	/**
	 * @return the txnSettleDate
	 */
	public Timestamp getTxnSettleDate() {
		return txnSettleDate;
	}
	/**
	 * @param txnSettleDate the txnSettleDate to set
	 */
	public void setTxnSettleDate(Timestamp txnSettleDate) {
		this.txnSettleDate = txnSettleDate;
	}
	/**
	 * @return the ofac_case_id
	 */
	public String getOfac_case_id() {
		return ofac_case_id;
	}
	/**
	 * @param ofac_case_id the ofac_case_id to set
	 */
	public void setOfac_case_id(String ofac_case_id) {
		this.ofac_case_id = ofac_case_id;
	}
	/**
	 * @return the display_qualifier
	 */
	public String getDisplay_qualifier() {
		return display_qualifier;
	}
	/**
	 * @param display_qualifier the display_qualifier to set
	 */
	public void setDisplay_qualifier(String display_qualifier) {
		this.display_qualifier = display_qualifier;
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

		retValue = "TrxnDetailsOutputTO ( "
			+ super.toString() + TAB
			+ "confirmationNo = " + this.confirmationNo + TAB
			+ "trxnType = " + this.trxnType + TAB
			+ "trxnAmount = " + this.trxnAmount + TAB
			+ "frequencyType = " + this.frequencyType + TAB
			+ "accTierFromAcc = " + this.accTierFromAcc + TAB
			+ "accTierToAcc = " + this.accTierToAcc + TAB
			+ "accNoFromAcc = " + this.accNoFromAcc + TAB
			+ "accNoToAcc = " + this.accNoToAcc + TAB
			+ "routingNoFromAcc = " + this.routingNoFromAcc + TAB
			+ "routingNoToAcc = " + this.routingNoToAcc + TAB
			+ "ownerFromAcc = " + this.ownerFromAcc + TAB
			+ "ownerToAcc = " + this.ownerToAcc + TAB
			+ "accHeldAtFromAcc = " + this.accHeldAtFromAcc + TAB
			+ "accHeldAtToAcc = " + this.accHeldAtToAcc + TAB
			+ "accClassFromAcc = " + this.accClassFromAcc + TAB
			+ "accClassToAcc = " + this.accClassToAcc + TAB
			+ "accSubClassFromAcc = " + this.accSubClassFromAcc + TAB
			+ "accSubClassToAcc = " + this.accSubClassToAcc + TAB
			+ "accNameFromAcc = " + this.accNameFromAcc + TAB
			+ "accNameToAcc = " + this.accNameToAcc + TAB
			+ "accDtlsFromAcc = " + this.accDtlsFromAcc + TAB
			+ "accDtlsToAcc = " + this.accDtlsToAcc + TAB
			+ "faIdFromAcc = " + this.faIdFromAcc + TAB
			+ "faIdToAcc = " + this.faIdToAcc + TAB
			+ "keyAccNoFromAcc = " + this.keyAccNoFromAcc + TAB
			+ "keyAccNoToAcc = " + this.keyAccNoToAcc + TAB
			+ "officeAccNoFromAcc = " + this.officeAccNoFromAcc + TAB
			+ "officeAccNoToAcc = " + this.officeAccNoToAcc + TAB
			+ "accTypeFromAcc = " + this.accTypeFromAcc + TAB
			+ "accTypeToAcc = " + this.accTypeToAcc + TAB
			+ "branchIdFromAcc = " + this.branchIdFromAcc + TAB
			+ "branchIdToAcc = " + this.branchIdToAcc + TAB
			+ "status = " + this.status + TAB
			+ "paybatchref = " + this.paybatchref + TAB
			+ "statusCode = " + this.statusCode + TAB
			+ "txnTypeCode = " + this.txnTypeCode + TAB
			+ "versionnum = " + this.versionnum + TAB
			+ "batversionnum = " + this.batversionnum + TAB
			+ "partxnversionnum = " + this.partxnversionnum + TAB
			+ "txnEntryDate = " + this.txnEntryDate + TAB
			+ "txnInitiationDate = " + this.txnInitiationDate + TAB
			+ "txnSettleDate = " + this.txnSettleDate + TAB
			+ "actionsList = " + this.actionsList + TAB
			+ "reasonCodesList = " + this.reasonCodesList + TAB
			+ "authDetailsList = " + this.authDetailsList + TAB
			+ "signedAuthDetailsList = " + this.signedAuthDetailsList + TAB
			+ "auth_mode = " + this.auth_mode + TAB
			+ "recurringFlag = " + this.recurringFlag + TAB
			+ "recurringActiveFlg = " + this.recurringActiveFlg + TAB
			+ "business_Date = " + this.business_Date + TAB
			+ "retirement_TxnTypeDesc = " + this.retirement_TxnTypeDesc + TAB
			+ "qualifier = " + this.qualifier + TAB
			+ "reverse_qualifier = " + this.reverse_qualifier + TAB
			+ "retirement_mnemonic = " + this.retirement_mnemonic + TAB
			+ "untilDollarLimit = " + this.untilDollarLimit + TAB
			+ "certifiedFlag = " + this.certifiedFlag + TAB
			+ "checkNo = " + this.checkNo + TAB
			+ "trackingId = " + this.trackingId + TAB
			+ "payeeToName = " + this.payeeToName + TAB
			+ "printingBranch = " + this.printingBranch + TAB
			+ "printMemoOn = " + this.printMemoOn + TAB
			+ "deliveredToType = " + this.deliveredToType + TAB
			+ "deliveredToName = " + this.deliveredToName + TAB
			+ "typeOfId = " + this.typeOfId + TAB
			+ "idNumber = " + this.idNumber + TAB
			+ "thirdPartyReason = " + this.thirdPartyReason + TAB
			+ "deliveryAddress = " + this.deliveryAddress + TAB
			+ "checkMemo = " + this.checkMemo + TAB
			+ "foreignAddressFlag = " + this.foreignAddressFlag + TAB
			+ "defaultAddressFlag = " + this.defaultAddressFlag + TAB
			+ "printMemoCheckFlag = " + this.printMemoCheckFlag + TAB
			+ "printMemoStubFlag = " + this.printMemoStubFlag + TAB
			+ "sameNameFlag = " + this.sameNameFlag + TAB
			+ "deliveryOption = " + this.deliveryOption + TAB
			+ "fee = " + this.fee + TAB
			+ "requestedPrintDate = " + this.requestedPrintDate + TAB
			+ "ofac_case_id = " + this.ofac_case_id + TAB
			+ "display_qualifier = " + this.display_qualifier + TAB
			+ " )";

		return retValue;
	}
}