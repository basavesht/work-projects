package com.tcs.ebw.payments.transferobject;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

import com.tcs.ebw.transferobject.EBWTransferObject;

/**
 * @author : 224703
 * **************   Revision History ************************
 * Modified-By |  Modified-Date |  Project-Phase  | Changes
 *
 * **********************************************************
 */
public class CheckRequestTO extends EBWTransferObject implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1150845429120932063L;

	private String confirmationNo = null;	
	private String payeeName = null;	
	private Double thirdPartyReason = null;
	private String certifiedFlag = null;
	private BigDecimal fee = new BigDecimal(0D);
	private String chargedTo = null;
	private BigDecimal prevFee = new BigDecimal(0D);
	private String prevChargedTo = null;
	private Double pickUpOption = null;	
	private String defaultAddressFlag = null;
	private String foreignAddressFlag = null;
	private String printingBranch = null;	
	private String deliveryAddrLine1 = null;	
	private String deliveryAddrLine2 = null;	
	private String deliveryAddrLine3 = null;	
	private String deliveryAddrLine4 = null;	
	private String printAddressFlag = null;
	private String memoLine1 = null;	
	private String memoLine2 = null;
	private String printMemoCheckFlag = null;
	private String printMemoStubFlag = null;
	private String deliveredToType = null;
	private String deliveredToName = null;
	private Double typeOfId = null;
	private String idNumber = null;
	private String checkNo = null;
	private String trackingId = null;
	private Timestamp estPickupTime = null;
	private boolean thirdPartyflag = false;
	private String printerName = null;
	private Double chktxnversionnum = null;	
	private Double payeeType = null;	

	public Double getPayeeType() {
		return payeeType;
	}
	public void setPayeeType(Double payeeType) {
		this.payeeType = payeeType;
	}
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
	 * @return the payeeName
	 */
	public String getPayeeName() {
		return payeeName;
	}
	/**
	 * @param payeeName the payeeName to set
	 */
	public void setPayeeName(String payeeName) {
		this.payeeName = payeeName;
	}
	/**
	 * @return the thirdPartyReason
	 */
	public Double getThirdPartyReason() {
		return thirdPartyReason;
	}
	/**
	 * @param thirdPartyReason the thirdPartyReason to set
	 */
	public void setThirdPartyReason(Double thirdPartyReason) {
		this.thirdPartyReason = thirdPartyReason;
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
	 * @return the fee
	 */
	public BigDecimal getFee() {
		return fee;
	}
	/**
	 * @param fee the fee to set
	 */
	public void setFee(BigDecimal fee) {
		this.fee = fee;
	}
	/**
	 * @return the chargedTo
	 */
	public String getChargedTo() {
		return chargedTo;
	}
	/**
	 * @param chargedTo the chargedTo to set
	 */
	public void setChargedTo(String chargedTo) {
		this.chargedTo = chargedTo;
	}
	/**
	 * @return the prevFee
	 */
	public BigDecimal getPrevFee() {
		return prevFee;
	}
	/**
	 * @param prevFee the prevFee to set
	 */
	public void setPrevFee(BigDecimal prevFee) {
		this.prevFee = prevFee;
	}
	/**
	 * @return the prevChargedTo
	 */
	public String getPrevChargedTo() {
		return prevChargedTo;
	}
	/**
	 * @param prevChargedTo the prevChargedTo to set
	 */
	public void setPrevChargedTo(String prevChargedTo) {
		this.prevChargedTo = prevChargedTo;
	}
	/**
	 * @return the pickUpOption
	 */
	public Double getPickUpOption() {
		return pickUpOption;
	}
	/**
	 * @param pickUpOption the pickUpOption to set
	 */
	public void setPickUpOption(Double pickUpOption) {
		this.pickUpOption = pickUpOption;
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
	 * @return the deliveryAddrLine1
	 */
	public String getDeliveryAddrLine1() {
		return deliveryAddrLine1;
	}
	/**
	 * @param deliveryAddrLine1 the deliveryAddrLine1 to set
	 */
	public void setDeliveryAddrLine1(String deliveryAddrLine1) {
		this.deliveryAddrLine1 = deliveryAddrLine1;
	}
	/**
	 * @return the deliveryAddrLine2
	 */
	public String getDeliveryAddrLine2() {
		return deliveryAddrLine2;
	}
	/**
	 * @param deliveryAddrLine2 the deliveryAddrLine2 to set
	 */
	public void setDeliveryAddrLine2(String deliveryAddrLine2) {
		this.deliveryAddrLine2 = deliveryAddrLine2;
	}
	/**
	 * @return the deliveryAddrLine3
	 */
	public String getDeliveryAddrLine3() {
		return deliveryAddrLine3;
	}
	/**
	 * @param deliveryAddrLine3 the deliveryAddrLine3 to set
	 */
	public void setDeliveryAddrLine3(String deliveryAddrLine3) {
		this.deliveryAddrLine3 = deliveryAddrLine3;
	}
	/**
	 * @return the deliveryAddrLine4
	 */
	public String getDeliveryAddrLine4() {
		return deliveryAddrLine4;
	}
	/**
	 * @param deliveryAddrLine4 the deliveryAddrLine4 to set
	 */
	public void setDeliveryAddrLine4(String deliveryAddrLine4) {
		this.deliveryAddrLine4 = deliveryAddrLine4;
	}
	/**
	 * @return the printAddressFlag
	 */
	public String getPrintAddressFlag() {
		return printAddressFlag;
	}
	/**
	 * @param printAddressFlag the printAddressFlag to set
	 */
	public void setPrintAddressFlag(String printAddressFlag) {
		this.printAddressFlag = printAddressFlag;
	}
	/**
	 * @return the memoLine1
	 */
	public String getMemoLine1() {
		return memoLine1;
	}
	/**
	 * @param memoLine1 the memoLine1 to set
	 */
	public void setMemoLine1(String memoLine1) {
		this.memoLine1 = memoLine1;
	}
	/**
	 * @return the memoLine2
	 */
	public String getMemoLine2() {
		return memoLine2;
	}
	/**
	 * @param memoLine2 the memoLine2 to set
	 */
	public void setMemoLine2(String memoLine2) {
		this.memoLine2 = memoLine2;
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
	public Double getTypeOfId() {
		return typeOfId;
	}
	/**
	 * @param typeOfId the typeOfId to set
	 */
	public void setTypeOfId(Double typeOfId) {
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
	 * @return the estPickupTime
	 */
	public Timestamp getEstPickupTime() {
		return estPickupTime;
	}
	/**
	 * @param estPickupTime the estPickupTime to set
	 */
	public void setEstPickupTime(Timestamp estPickupTime) {
		this.estPickupTime = estPickupTime;
	}
	/**
	 * @return the thirdPartyflag
	 */
	public boolean isThirdPartyflag() {
		return thirdPartyflag;
	}
	/**
	 * @param thirdPartyflag the thirdPartyflag to set
	 */
	public void setThirdPartyflag(boolean thirdPartyflag) {
		this.thirdPartyflag = thirdPartyflag;
	}
	/**
	 * @return the printerName
	 */
	public String getPrinterName() {
		return printerName;
	}
	/**
	 * @param printerName the printerName to set
	 */
	public void setPrinterName(String printerName) {
		this.printerName = printerName;
	}

	public Double getChktxnversionnum() {
		return chktxnversionnum;
	}
	public void setChktxnversionnum(Double chktxnversionnum) {
		this.chktxnversionnum = chktxnversionnum;
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
		final String TAB = "    ";

		String retValue = "";

		retValue = "CheckRequestTO ( "
			+ super.toString() + TAB
			+ "confirmationNo = " + this.confirmationNo + TAB
			+ "payeeName = " + this.payeeName + TAB
			+ "thirdPartyReason = " + this.thirdPartyReason + TAB
			+ "certifiedFlag = " + this.certifiedFlag + TAB
			+ "fee = " + this.fee + TAB
			+ "chargedTo = " + this.chargedTo + TAB
			+ "prevFee = " + this.prevFee + TAB
			+ "prevChargedTo = " + this.prevChargedTo + TAB
			+ "pickUpOption = " + this.pickUpOption + TAB
			+ "defaultAddressFlag = " + this.defaultAddressFlag + TAB
			+ "foreignAddressFlag = " + this.foreignAddressFlag + TAB
			+ "printingBranch = " + this.printingBranch + TAB
			+ "deliveryAddrLine1 = " + this.deliveryAddrLine1 + TAB
			+ "deliveryAddrLine2 = " + this.deliveryAddrLine2 + TAB
			+ "deliveryAddrLine3 = " + this.deliveryAddrLine3 + TAB
			+ "deliveryAddrLine4 = " + this.deliveryAddrLine4 + TAB
			+ "printAddressFlag = " + this.printAddressFlag + TAB
			+ "memoLine1 = " + this.memoLine1 + TAB
			+ "memoLine2 = " + this.memoLine2 + TAB
			+ "printMemoCheckFlag = " + this.printMemoCheckFlag + TAB
			+ "printMemoStubFlag = " + this.printMemoStubFlag + TAB
			+ "deliveredToType = " + this.deliveredToType + TAB
			+ "deliveredToName = " + this.deliveredToName + TAB
			+ "typeOfId = " + this.typeOfId + TAB
			+ "idNumber = " + this.idNumber + TAB
			+ "checkNo = " + this.checkNo + TAB
			+ "trackingId = " + this.trackingId + TAB
			+ "estPickupTime = " + this.estPickupTime + TAB
			+ "thirdPartyflag = " + this.thirdPartyflag + TAB
			+ "printerName = " + this.printerName + TAB
			+ "chktxnversionnum = " + this.chktxnversionnum + TAB
			+ "payeeType = " + this.payeeType + TAB
			+ " )";

		return retValue;
	}

}