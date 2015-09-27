package com.tcs.ebw.payments.transferobject;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;

/**
 * @author : 224703
 * **************   Revision History ************************
 * Modified-By |  Modified-Date |  Project-Phase  | Changes
 *    224703       01-07-2011        3               CR 262
 * **********************************************************
 */
public class PaymentDetailsTO implements Serializable 
{

	private static final long serialVersionUID = 7383722129410673746L;

	//Payment ID related Attributes...
	private String transactionId = null;
	private String transactionVersion =null; //Delegate
	private String chkTxnVersion =null; //Delegate
	private String txnBatchId =null;
	private String txnBatchVersion =null; //Delegate
	private String recParentTxnId =null;
	private String recParentTxnVersion =null; //Delegate
	private String paymentHubTxnId =null; // Hub confirmation number for the current transaction..
	private String nextpaymentHubTxnId =null; //Hub confirmation number for the child spawned in case of HUB Invoke for future enhancements.

	//Payment Screen related Attributes...
	//1. From Account Details...
	private String from_Account= null;  //Delegate
	private String from_KeyAccount= null; //Delegate

	//2. To Account Attributes..
	private String to_Account= null; //Delegate
	private String to_KeyAccount= null; //Delegate

	private String extAccount_Number= null;
	private String extAcc_NickName= null;
	private String extAccount_RefId= null;
	private String extAccount_Version= null; //Delegate

	//3. Transfer Amount and Date Attributes..
	private String transfer_Amount= null;
	private String transfer_Currency= null;
	private String fxRate= null;
	private String initiation_Date= null;
	private String requestedDate= null;
	private String estimatedArrivalDate= null;
	private String actualExecDate= null;
	private String txnExpiryDate= null;

	//4. Recurring Transfer Attributes..
	private String frequency_Type= null; //1:One-Time 2:Recurring
	private String frequency_DurationDesc= null; //W,Y,M....etc
	private String frequency_DurationValue= null; // 1,2,3,4
	private String duration_EndDate= null;
	private String duration_AmountLimit= null;
	private String duration_NoOfTransfers= null;

	//5. Transfer Additional Attributes...
	private String business_Date= null; //Delegate
	private String transfer_Type= null; //Delegate
	private String screen_Type= null; //Delegate
	private String msBranchId= null; // Branch OU_ID
	private String applicationId= null; //Delegate

	private boolean isTxnModified =false; //Delegate
	private boolean isTxnCancelled =false; //Delegate
	private boolean isTxnApproved =false; //Delegate
	private boolean isTxnInitiated = false; //Delegate
	private boolean isTxnResumed = false; //Delegate
	private boolean isParentTxnSuspended =false; //Delegate
	private boolean isCheckPrinted =false; //Delegate
	private boolean isChildTxnCreated =false; //Service..
	private boolean isAccounEligible = false; //Service..
	private boolean isStatusChkReq = false; //Service..
	private boolean isVersionChkReq = true; //Service..

	private Object requestHeaderInfo = null; //Delegate
	private String rsa_Review_Flag= null;
	private String trial_depo= null; //Delegate

	private String versionChkId= null; //Values :Transaction/Account  //Delegate..
	private String statusChkId = null;//Values : TXNSTATUS/ACNTSTATUS  //Delegate..
	private String statusChkEventDesc = null;

	private String loggingAction= null; //Values : Created/Modified/Canceled/Approved/Rejected
	private String mMSystemDesc= null; //Values : Created/Modified/Canceled/Approved/Rejected
	private boolean isFrmAcc_InContext=false; // Checks if the From Key account no is in the context . True : present False : Not present 
	private boolean isToAcc_InContext=false;  // Checks if the From Key account no is in the context . True : present False : Not present 
	private boolean isAcntDataInSession = false;

	//6. Payment Event,Action, Status attributes...
	private String eventDesc = null; //Business Rule event description. //Delegate
	private String txnPrevStatusCode = null; // Old status of the transaction before transaction modification..
	private String txnCurrentStatusCode = null; // New or the current status of the transaction in case of creation , modification etc.
	private String prevAction = null; //Values: Create/Modify/Cancel/Approve/Reject    //For DAP Check only Delegate

	//7. Previous Transaction Details before Modification for edit case only...
	private String prevTxnAmount =null;
	private String prevTxnDate =null;

	//8. Next Child transaction details in case the same is created for recurring transfers...
	private String childTxnRequestedExecDate =null;
	private String childTransactionId =null;
	private String childTxnBatchId =null;
	private String childloggingAction= null; //Values : Created/Modified/Canceled/Approved/Rejected
	private String childTxnStatusCode =null;
	private String childTxnAmount =null; //Stores the child transaction amount , will be same as the transfer amount . Only in case of the Unitl dollar limit , we will update the amount to the remaininig amount . 

	//9. MS360 new attributes....
	private String created_by_role = null; //Initiator Role..
	private String createdby_name = null; //Initiator Name...
	private String createdby = null; //Initiator Id...
	private String createdDate = null; //Created Id...
	private String life_user_id = null;
	private String dont_spawn_flag =null;
	private String same_name_flag =null;
	private String key_client =null;
	private String key_link =null;
	private String key_clientId =null;
	private String auth_mode = null;
	private String auth_info_reciever =null;
	private String verbal_auth_client_name =null;
	private String verbal_auth_date =null;
	private String verbal_auth_time =null;
	private String client_verification_desc =null;
	private String auth_confirmed_by =null;
	private BigDecimal utilizedAmount = new BigDecimal(0D);

	//10. Transaction owner and initiator details...
	private String initiator_role = null; //Initiator Role...
	private String initiator_id =  null; //Initiator Id...
	private String current_owner_role = null; //Current Owner Role..
	private String current_owner_id =null; //Current Owner Id..
	private String current_owner_name =null; //Current Owner Name...
	private String current_approver_role =null; //Stores the current approver of transaction..
	private String previous_owner_role = null; //Current Owner Role..
	private String created_by_comments =null; //Stores the current approver of transaction..

	//11. EPR Screen,action and state details...
	private String screen= null;
	private String state= null;
	private String action= null;
	private ArrayList userFuncRoleList = null;

	//12.To check if there are any warnings coming up in the Pre-Confirm of approve....
	private boolean isNextApproval_Req = false;
	private String  next_approver_role = null;

	//13. Txn_Parent attributes mapping for checking the recurring criteria, will be updated on update_txn_parent execute...
	private Double     accumulated_transfers = new Double(0D);
	private BigDecimal accumulated_amount = new BigDecimal(0D);
	private Timestamp  prefered_next_txn_dt = null; //Not considering holiday from txn_parent..
	private Timestamp  next_txn_dt = null; //Considering holiday from txn_parent..
	private Timestamp  parTxnRequestDate = null; //Parent Transaction request date...
	private String     originating_parent_conf_no = null; //Considering holiday from txn_parent..

	//13. Storing the RTA booked-in flag...
	private String  rta_booked_flag = null; //RTA Booked In Value will be stored here if available for the transactions..
	private Double  rta_Action_key = null; //RTA action key..
	private Double  rta_Action_Key_Fee = null; //RTA action key for fee amount..
	private String  current_rta_booked_flag = null; //RTA updated booked in flag after the action...
	private boolean reverseRTAFlag = false; //Reserve RTA flag...
	private boolean cancelRTAFlag = false; //Cancel RTA flag...

	//14. Fee applicable for transaction if any...
	private BigDecimal txn_Fee_Charge = new BigDecimal(0D);
	private BigDecimal prev_txn_Fee_Charge = new BigDecimal(0D);
	private boolean isFeeApplicable = false; //Set this in case fee applicable for the transaction...

	//15.IRA Mappings...
	private String retirement_mnemonic = null;
	private String retirement_mnemonic_desc = null;
	private String qualifier = null;
	private String reverse_qualifier = null;
	private String iraTxnFlag = "N";

	//16.Limit Mappings...
	private int create_limit = 0;
	private int update_limit = 0;
	private int delete_limit = 0;
	private String limit_Action = null;
	private String case_status = null;

	//17.OFAC Case Id
	private String ofac_case_id = null;

	/**
	 * @return the transactionId
	 */
	public String getTransactionId() {
		return transactionId;
	}
	/**
	 * @param transactionId the transactionId to set
	 */
	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}
	/**
	 * @return the transactionVersion
	 */
	public String getTransactionVersion() {
		return transactionVersion;
	}
	/**
	 * @param transactionVersion the transactionVersion to set
	 */
	public void setTransactionVersion(String transactionVersion) {
		this.transactionVersion = transactionVersion;
	}
	/**
	 * @return the chkTxnVersion
	 */
	public String getChkTxnVersion() {
		return chkTxnVersion;
	}
	/**
	 * @param chkTxnVersion the chkTxnVersion to set
	 */
	public void setChkTxnVersion(String chkTxnVersion) {
		this.chkTxnVersion = chkTxnVersion;
	}
	/**
	 * @return the txnBatchId
	 */
	public String getTxnBatchId() {
		return txnBatchId;
	}
	/**
	 * @param txnBatchId the txnBatchId to set
	 */
	public void setTxnBatchId(String txnBatchId) {
		this.txnBatchId = txnBatchId;
	}
	/**
	 * @return the txnBatchVersion
	 */
	public String getTxnBatchVersion() {
		return txnBatchVersion;
	}
	/**
	 * @param txnBatchVersion the txnBatchVersion to set
	 */
	public void setTxnBatchVersion(String txnBatchVersion) {
		this.txnBatchVersion = txnBatchVersion;
	}
	/**
	 * @return the recParentTxnId
	 */
	public String getRecParentTxnId() {
		return recParentTxnId;
	}
	/**
	 * @param recParentTxnId the recParentTxnId to set
	 */
	public void setRecParentTxnId(String recParentTxnId) {
		this.recParentTxnId = recParentTxnId;
	}
	/**
	 * @return the recParentTxnVersion
	 */
	public String getRecParentTxnVersion() {
		return recParentTxnVersion;
	}
	/**
	 * @param recParentTxnVersion the recParentTxnVersion to set
	 */
	public void setRecParentTxnVersion(String recParentTxnVersion) {
		this.recParentTxnVersion = recParentTxnVersion;
	}
	/**
	 * @return the paymentHubTxnId
	 */
	public String getPaymentHubTxnId() {
		return paymentHubTxnId;
	}
	/**
	 * @param paymentHubTxnId the paymentHubTxnId to set
	 */
	public void setPaymentHubTxnId(String paymentHubTxnId) {
		this.paymentHubTxnId = paymentHubTxnId;
	}
	/**
	 * @return the nextpaymentHubTxnId
	 */
	public String getNextpaymentHubTxnId() {
		return nextpaymentHubTxnId;
	}
	/**
	 * @param nextpaymentHubTxnId the nextpaymentHubTxnId to set
	 */
	public void setNextpaymentHubTxnId(String nextpaymentHubTxnId) {
		this.nextpaymentHubTxnId = nextpaymentHubTxnId;
	}
	/**
	 * @return the from_Account
	 */
	public String getFrom_Account() {
		return from_Account;
	}
	/**
	 * @param from_Account the from_Account to set
	 */
	public void setFrom_Account(String from_Account) {
		this.from_Account = from_Account;
	}
	/**
	 * @return the from_KeyAccount
	 */
	public String getFrom_KeyAccount() {
		return from_KeyAccount;
	}
	/**
	 * @param from_KeyAccount the from_KeyAccount to set
	 */
	public void setFrom_KeyAccount(String from_KeyAccount) {
		this.from_KeyAccount = from_KeyAccount;
	}
	/**
	 * @return the to_Account
	 */
	public String getTo_Account() {
		return to_Account;
	}
	/**
	 * @param to_Account the to_Account to set
	 */
	public void setTo_Account(String to_Account) {
		this.to_Account = to_Account;
	}
	/**
	 * @return the to_KeyAccount
	 */
	public String getTo_KeyAccount() {
		return to_KeyAccount;
	}
	/**
	 * @param to_KeyAccount the to_KeyAccount to set
	 */
	public void setTo_KeyAccount(String to_KeyAccount) {
		this.to_KeyAccount = to_KeyAccount;
	}
	/**
	 * @return the extAccount_Number
	 */
	public String getExtAccount_Number() {
		return extAccount_Number;
	}
	/**
	 * @param extAccount_Number the extAccount_Number to set
	 */
	public void setExtAccount_Number(String extAccount_Number) {
		this.extAccount_Number = extAccount_Number;
	}
	/**
	 * @return the extAcc_NickName
	 */
	public String getExtAcc_NickName() {
		return extAcc_NickName;
	}
	/**
	 * @param extAcc_NickName the extAcc_NickName to set
	 */
	public void setExtAcc_NickName(String extAcc_NickName) {
		this.extAcc_NickName = extAcc_NickName;
	}
	/**
	 * @return the extAccount_RefId
	 */
	public String getExtAccount_RefId() {
		return extAccount_RefId;
	}
	/**
	 * @param extAccount_RefId the extAccount_RefId to set
	 */
	public void setExtAccount_RefId(String extAccount_RefId) {
		this.extAccount_RefId = extAccount_RefId;
	}
	/**
	 * @return the extAccount_Version
	 */
	public String getExtAccount_Version() {
		return extAccount_Version;
	}
	/**
	 * @param extAccount_Version the extAccount_Version to set
	 */
	public void setExtAccount_Version(String extAccount_Version) {
		this.extAccount_Version = extAccount_Version;
	}
	/**
	 * @return the transfer_Amount
	 */
	public String getTransfer_Amount() {
		return transfer_Amount;
	}
	/**
	 * @param transfer_Amount the transfer_Amount to set
	 */
	public void setTransfer_Amount(String transfer_Amount) {
		this.transfer_Amount = transfer_Amount;
	}
	/**
	 * @return the transfer_Currency
	 */
	public String getTransfer_Currency() {
		return transfer_Currency;
	}
	/**
	 * @param transfer_Currency the transfer_Currency to set
	 */
	public void setTransfer_Currency(String transfer_Currency) {
		this.transfer_Currency = transfer_Currency;
	}
	/**
	 * @return the fxRate
	 */
	public String getFxRate() {
		return fxRate;
	}
	/**
	 * @param fxRate the fxRate to set
	 */
	public void setFxRate(String fxRate) {
		this.fxRate = fxRate;
	}
	/**
	 * @return the initiation_Date
	 */
	public String getInitiation_Date() {
		return initiation_Date;
	}
	/**
	 * @param initiation_Date the initiation_Date to set
	 */
	public void setInitiation_Date(String initiation_Date) {
		this.initiation_Date = initiation_Date;
	}
	/**
	 * @return the requestedDate
	 */
	public String getRequestedDate() {
		return requestedDate;
	}
	/**
	 * @param requestedDate the requestedDate to set
	 */
	public void setRequestedDate(String requestedDate) {
		this.requestedDate = requestedDate;
	}
	/**
	 * @return the estimatedArrivalDate
	 */
	public String getEstimatedArrivalDate() {
		return estimatedArrivalDate;
	}
	/**
	 * @param estimatedArrivalDate the estimatedArrivalDate to set
	 */
	public void setEstimatedArrivalDate(String estimatedArrivalDate) {
		this.estimatedArrivalDate = estimatedArrivalDate;
	}
	/**
	 * @return the actualExecDate
	 */
	public String getActualExecDate() {
		return actualExecDate;
	}
	/**
	 * @param actualExecDate the actualExecDate to set
	 */
	public void setActualExecDate(String actualExecDate) {
		this.actualExecDate = actualExecDate;
	}
	/**
	 * @return the txnExpiryDate
	 */
	public String getTxnExpiryDate() {
		return txnExpiryDate;
	}
	/**
	 * @param txnExpiryDate the txnExpiryDate to set
	 */
	public void setTxnExpiryDate(String txnExpiryDate) {
		this.txnExpiryDate = txnExpiryDate;
	}
	/**
	 * @return the frequency_Type
	 */
	public String getFrequency_Type() {
		return frequency_Type;
	}
	/**
	 * @param frequency_Type the frequency_Type to set
	 */
	public void setFrequency_Type(String frequency_Type) {
		this.frequency_Type = frequency_Type;
	}
	/**
	 * @return the frequency_DurationDesc
	 */
	public String getFrequency_DurationDesc() {
		return frequency_DurationDesc;
	}
	/**
	 * @param frequency_DurationDesc the frequency_DurationDesc to set
	 */
	public void setFrequency_DurationDesc(String frequency_DurationDesc) {
		this.frequency_DurationDesc = frequency_DurationDesc;
	}
	/**
	 * @return the frequency_DurationValue
	 */
	public String getFrequency_DurationValue() {
		return frequency_DurationValue;
	}
	/**
	 * @param frequency_DurationValue the frequency_DurationValue to set
	 */
	public void setFrequency_DurationValue(String frequency_DurationValue) {
		this.frequency_DurationValue = frequency_DurationValue;
	}
	/**
	 * @return the duration_EndDate
	 */
	public String getDuration_EndDate() {
		return duration_EndDate;
	}
	/**
	 * @param duration_EndDate the duration_EndDate to set
	 */
	public void setDuration_EndDate(String duration_EndDate) {
		this.duration_EndDate = duration_EndDate;
	}
	/**
	 * @return the duration_AmountLimit
	 */
	public String getDuration_AmountLimit() {
		return duration_AmountLimit;
	}
	/**
	 * @param duration_AmountLimit the duration_AmountLimit to set
	 */
	public void setDuration_AmountLimit(String duration_AmountLimit) {
		this.duration_AmountLimit = duration_AmountLimit;
	}
	/**
	 * @return the duration_NoOfTransfers
	 */
	public String getDuration_NoOfTransfers() {
		return duration_NoOfTransfers;
	}
	/**
	 * @param duration_NoOfTransfers the duration_NoOfTransfers to set
	 */
	public void setDuration_NoOfTransfers(String duration_NoOfTransfers) {
		this.duration_NoOfTransfers = duration_NoOfTransfers;
	}
	/**
	 * @return the business_Date
	 */
	public String getBusiness_Date() {
		return business_Date;
	}
	/**
	 * @param business_Date the business_Date to set
	 */
	public void setBusiness_Date(String business_Date) {
		this.business_Date = business_Date;
	}
	/**
	 * @return the transfer_Type
	 */
	public String getTransfer_Type() {
		return transfer_Type;
	}
	/**
	 * @param transfer_Type the transfer_Type to set
	 */
	public void setTransfer_Type(String transfer_Type) {
		this.transfer_Type = transfer_Type;
	}
	/**
	 * @return the screen_Type
	 */
	public String getScreen_Type() {
		return screen_Type;
	}
	/**
	 * @param screen_Type the screen_Type to set
	 */
	public void setScreen_Type(String screen_Type) {
		this.screen_Type = screen_Type;
	}
	/**
	 * @return the msBranchId
	 */
	public String getMsBranchId() {
		return msBranchId;
	}
	/**
	 * @param msBranchId the msBranchId to set
	 */
	public void setMsBranchId(String msBranchId) {
		this.msBranchId = msBranchId;
	}
	/**
	 * @return the applicationId
	 */
	public String getApplicationId() {
		return applicationId;
	}
	/**
	 * @param applicationId the applicationId to set
	 */
	public void setApplicationId(String applicationId) {
		this.applicationId = applicationId;
	}
	/**
	 * @return the isTxnModified
	 */
	public boolean isTxnModified() {
		return isTxnModified;
	}
	/**
	 * @param isTxnModified the isTxnModified to set
	 */
	public void setTxnModified(boolean isTxnModified) {
		this.isTxnModified = isTxnModified;
	}
	/**
	 * @return the isTxnCancelled
	 */
	public boolean isTxnCancelled() {
		return isTxnCancelled;
	}
	/**
	 * @param isTxnCancelled the isTxnCancelled to set
	 */
	public void setTxnCancelled(boolean isTxnCancelled) {
		this.isTxnCancelled = isTxnCancelled;
	}
	/**
	 * @return the isTxnApproved
	 */
	public boolean isTxnApproved() {
		return isTxnApproved;
	}
	/**
	 * @param isTxnApproved the isTxnApproved to set
	 */
	public void setTxnApproved(boolean isTxnApproved) {
		this.isTxnApproved = isTxnApproved;
	}
	/**
	 * @return the isTxnInitiated
	 */
	public boolean isTxnInitiated() {
		return isTxnInitiated;
	}
	/**
	 * @param isTxnInitiated the isTxnInitiated to set
	 */
	public void setTxnInitiated(boolean isTxnInitiated) {
		this.isTxnInitiated = isTxnInitiated;
	}
	/**
	 * @return the isTxnResumed
	 */
	public boolean isTxnResumed() {
		return isTxnResumed;
	}
	/**
	 * @param isTxnResumed the isTxnResumed to set
	 */
	public void setTxnResumed(boolean isTxnResumed) {
		this.isTxnResumed = isTxnResumed;
	}
	/**
	 * @return the isParentTxnSuspended
	 */
	public boolean isParentTxnSuspended() {
		return isParentTxnSuspended;
	}
	/**
	 * @param isParentTxnSuspended the isParentTxnSuspended to set
	 */
	public void setParentTxnSuspended(boolean isParentTxnSuspended) {
		this.isParentTxnSuspended = isParentTxnSuspended;
	}
	/**
	 * @return the isCheckPrinted
	 */
	public boolean isCheckPrinted() {
		return isCheckPrinted;
	}
	/**
	 * @param isCheckPrinted the isCheckPrinted to set
	 */
	public void setCheckPrinted(boolean isCheckPrinted) {
		this.isCheckPrinted = isCheckPrinted;
	}
	/**
	 * @return the isChildTxnCreated
	 */
	public boolean isChildTxnCreated() {
		return isChildTxnCreated;
	}
	/**
	 * @param isChildTxnCreated the isChildTxnCreated to set
	 */
	public void setChildTxnCreated(boolean isChildTxnCreated) {
		this.isChildTxnCreated = isChildTxnCreated;
	}
	/**
	 * @return the isAccounEligible
	 */
	public boolean isAccounEligible() {
		return isAccounEligible;
	}
	/**
	 * @param isAccounEligible the isAccounEligible to set
	 */
	public void setAccounEligible(boolean isAccounEligible) {
		this.isAccounEligible = isAccounEligible;
	}
	/**
	 * @return the isStatusChkReq
	 */
	public boolean isStatusChkReq() {
		return isStatusChkReq;
	}
	/**
	 * @param isStatusChkReq the isStatusChkReq to set
	 */
	public void setStatusChkReq(boolean isStatusChkReq) {
		this.isStatusChkReq = isStatusChkReq;
	}
	/**
	 * @return the isVersionChkReq
	 */
	public boolean isVersionChkReq() {
		return isVersionChkReq;
	}
	/**
	 * @param isVersionChkReq the isVersionChkReq to set
	 */
	public void setVersionChkReq(boolean isVersionChkReq) {
		this.isVersionChkReq = isVersionChkReq;
	}
	/**
	 * @return the requestHeaderInfo
	 */
	public Object getRequestHeaderInfo() {
		return requestHeaderInfo;
	}
	/**
	 * @param requestHeaderInfo the requestHeaderInfo to set
	 */
	public void setRequestHeaderInfo(Object requestHeaderInfo) {
		this.requestHeaderInfo = requestHeaderInfo;
	}
	/**
	 * @return the rsa_Review_Flag
	 */
	public String getRsa_Review_Flag() {
		return rsa_Review_Flag;
	}
	/**
	 * @param rsa_Review_Flag the rsa_Review_Flag to set
	 */
	public void setRsa_Review_Flag(String rsa_Review_Flag) {
		this.rsa_Review_Flag = rsa_Review_Flag;
	}
	/**
	 * @return the trial_depo
	 */
	public String getTrial_depo() {
		return trial_depo;
	}
	/**
	 * @param trial_depo the trial_depo to set
	 */
	public void setTrial_depo(String trial_depo) {
		this.trial_depo = trial_depo;
	}
	/**
	 * @return the versionChkId
	 */
	public String getVersionChkId() {
		return versionChkId;
	}
	/**
	 * @param versionChkId the versionChkId to set
	 */
	public void setVersionChkId(String versionChkId) {
		this.versionChkId = versionChkId;
	}
	/**
	 * @return the statusChkId
	 */
	public String getStatusChkId() {
		return statusChkId;
	}
	/**
	 * @param statusChkId the statusChkId to set
	 */
	public void setStatusChkId(String statusChkId) {
		this.statusChkId = statusChkId;
	}
	/**
	 * @return the statusChkEventDesc
	 */
	public String getStatusChkEventDesc() {
		return statusChkEventDesc;
	}
	/**
	 * @param statusChkEventDesc the statusChkEventDesc to set
	 */
	public void setStatusChkEventDesc(String statusChkEventDesc) {
		this.statusChkEventDesc = statusChkEventDesc;
	}
	/**
	 * @return the loggingAction
	 */
	public String getLoggingAction() {
		return loggingAction;
	}
	/**
	 * @param loggingAction the loggingAction to set
	 */
	public void setLoggingAction(String loggingAction) {
		this.loggingAction = loggingAction;
	}
	/**
	 * @return the mMSystemDesc
	 */
	public String getMMSystemDesc() {
		return mMSystemDesc;
	}
	/**
	 * @param systemDesc the mMSystemDesc to set
	 */
	public void setMMSystemDesc(String systemDesc) {
		mMSystemDesc = systemDesc;
	}
	/**
	 * @return the isFrmAcc_InContext
	 */
	public boolean isFrmAcc_InContext() {
		return isFrmAcc_InContext;
	}
	/**
	 * @param isFrmAcc_InContext the isFrmAcc_InContext to set
	 */
	public void setFrmAcc_InContext(boolean isFrmAcc_InContext) {
		this.isFrmAcc_InContext = isFrmAcc_InContext;
	}
	/**
	 * @return the isToAcc_InContext
	 */
	public boolean isToAcc_InContext() {
		return isToAcc_InContext;
	}
	/**
	 * @param isToAcc_InContext the isToAcc_InContext to set
	 */
	public void setToAcc_InContext(boolean isToAcc_InContext) {
		this.isToAcc_InContext = isToAcc_InContext;
	}
	/**
	 * @return the isAcntDataInSession
	 */
	public boolean isAcntDataInSession() {
		return isAcntDataInSession;
	}
	/**
	 * @param isAcntDataInSession the isAcntDataInSession to set
	 */
	public void setAcntDataInSession(boolean isAcntDataInSession) {
		this.isAcntDataInSession = isAcntDataInSession;
	}
	/**
	 * @return the eventDesc
	 */
	public String getEventDesc() {
		return eventDesc;
	}
	/**
	 * @param eventDesc the eventDesc to set
	 */
	public void setEventDesc(String eventDesc) {
		this.eventDesc = eventDesc;
	}
	/**
	 * @return the txnPrevStatusCode
	 */
	public String getTxnPrevStatusCode() {
		return txnPrevStatusCode;
	}
	/**
	 * @param txnPrevStatusCode the txnPrevStatusCode to set
	 */
	public void setTxnPrevStatusCode(String txnPrevStatusCode) {
		this.txnPrevStatusCode = txnPrevStatusCode;
	}
	/**
	 * @return the txnCurrentStatusCode
	 */
	public String getTxnCurrentStatusCode() {
		return txnCurrentStatusCode;
	}
	/**
	 * @param txnCurrentStatusCode the txnCurrentStatusCode to set
	 */
	public void setTxnCurrentStatusCode(String txnCurrentStatusCode) {
		this.txnCurrentStatusCode = txnCurrentStatusCode;
	}
	/**
	 * @return the prevAction
	 */
	public String getPrevAction() {
		return prevAction;
	}
	/**
	 * @param prevAction the prevAction to set
	 */
	public void setPrevAction(String prevAction) {
		this.prevAction = prevAction;
	}
	/**
	 * @return the prevTxnAmount
	 */
	public String getPrevTxnAmount() {
		return prevTxnAmount;
	}
	/**
	 * @param prevTxnAmount the prevTxnAmount to set
	 */
	public void setPrevTxnAmount(String prevTxnAmount) {
		this.prevTxnAmount = prevTxnAmount;
	}
	/**
	 * @return the prevTxnDate
	 */
	public String getPrevTxnDate() {
		return prevTxnDate;
	}
	/**
	 * @param prevTxnDate the prevTxnDate to set
	 */
	public void setPrevTxnDate(String prevTxnDate) {
		this.prevTxnDate = prevTxnDate;
	}
	/**
	 * @return the childTxnRequestedExecDate
	 */
	public String getChildTxnRequestedExecDate() {
		return childTxnRequestedExecDate;
	}
	/**
	 * @param childTxnRequestedExecDate the childTxnRequestedExecDate to set
	 */
	public void setChildTxnRequestedExecDate(String childTxnRequestedExecDate) {
		this.childTxnRequestedExecDate = childTxnRequestedExecDate;
	}
	/**
	 * @return the childTransactionId
	 */
	public String getChildTransactionId() {
		return childTransactionId;
	}
	/**
	 * @param childTransactionId the childTransactionId to set
	 */
	public void setChildTransactionId(String childTransactionId) {
		this.childTransactionId = childTransactionId;
	}
	/**
	 * @return the childTxnBatchId
	 */
	public String getChildTxnBatchId() {
		return childTxnBatchId;
	}
	/**
	 * @param childTxnBatchId the childTxnBatchId to set
	 */
	public void setChildTxnBatchId(String childTxnBatchId) {
		this.childTxnBatchId = childTxnBatchId;
	}
	/**
	 * @return the childloggingAction
	 */
	public String getChildloggingAction() {
		return childloggingAction;
	}
	/**
	 * @param childloggingAction the childloggingAction to set
	 */
	public void setChildloggingAction(String childloggingAction) {
		this.childloggingAction = childloggingAction;
	}
	/**
	 * @return the childTxnStatusCode
	 */
	public String getChildTxnStatusCode() {
		return childTxnStatusCode;
	}
	/**
	 * @param childTxnStatusCode the childTxnStatusCode to set
	 */
	public void setChildTxnStatusCode(String childTxnStatusCode) {
		this.childTxnStatusCode = childTxnStatusCode;
	}
	/**
	 * @return the childTxnAmount
	 */
	public String getChildTxnAmount() {
		return childTxnAmount;
	}
	/**
	 * @param childTxnAmount the childTxnAmount to set
	 */
	public void setChildTxnAmount(String childTxnAmount) {
		this.childTxnAmount = childTxnAmount;
	}
	/**
	 * @return the created_by_role
	 */
	public String getCreated_by_role() {
		return created_by_role;
	}
	/**
	 * @param created_by_role the created_by_role to set
	 */
	public void setCreated_by_role(String created_by_role) {
		this.created_by_role = created_by_role;
	}
	/**
	 * @return the createdby_name
	 */
	public String getCreatedby_name() {
		return createdby_name;
	}
	/**
	 * @param createdby_name the createdby_name to set
	 */
	public void setCreatedby_name(String createdby_name) {
		this.createdby_name = createdby_name;
	}
	/**
	 * @return the createdby
	 */
	public String getCreatedby() {
		return createdby;
	}
	/**
	 * @param createdby the createdby to set
	 */
	public void setCreatedby(String createdby) {
		this.createdby = createdby;
	}
	/**
	 * @return the createdDate
	 */
	public String getCreatedDate() {
		return createdDate;
	}
	/**
	 * @param createdDate the createdDate to set
	 */
	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}
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
	 * @return the dont_spawn_flag
	 */
	public String getDont_spawn_flag() {
		return dont_spawn_flag;
	}
	/**
	 * @param dont_spawn_flag the dont_spawn_flag to set
	 */
	public void setDont_spawn_flag(String dont_spawn_flag) {
		this.dont_spawn_flag = dont_spawn_flag;
	}
	/**
	 * @return the same_name_flag
	 */
	public String getSame_name_flag() {
		return same_name_flag;
	}
	/**
	 * @param same_name_flag the same_name_flag to set
	 */
	public void setSame_name_flag(String same_name_flag) {
		this.same_name_flag = same_name_flag;
	}
	/**
	 * @return the key_client
	 */
	public String getKey_client() {
		return key_client;
	}
	/**
	 * @param key_client the key_client to set
	 */
	public void setKey_client(String key_client) {
		this.key_client = key_client;
	}
	/**
	 * @return the key_link
	 */
	public String getKey_link() {
		return key_link;
	}
	/**
	 * @param key_link the key_link to set
	 */
	public void setKey_link(String key_link) {
		this.key_link = key_link;
	}
	/**
	 * @return the key_clientId
	 */
	public String getKey_clientId() {
		return key_clientId;
	}
	/**
	 * @param key_clientId the key_clientId to set
	 */
	public void setKey_clientId(String key_clientId) {
		this.key_clientId = key_clientId;
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
	 * @return the auth_info_reciever
	 */
	public String getAuth_info_reciever() {
		return auth_info_reciever;
	}
	/**
	 * @param auth_info_reciever the auth_info_reciever to set
	 */
	public void setAuth_info_reciever(String auth_info_reciever) {
		this.auth_info_reciever = auth_info_reciever;
	}
	/**
	 * @return the verbal_auth_client_name
	 */
	public String getVerbal_auth_client_name() {
		return verbal_auth_client_name;
	}
	/**
	 * @param verbal_auth_client_name the verbal_auth_client_name to set
	 */
	public void setVerbal_auth_client_name(String verbal_auth_client_name) {
		this.verbal_auth_client_name = verbal_auth_client_name;
	}
	/**
	 * @return the verbal_auth_date
	 */
	public String getVerbal_auth_date() {
		return verbal_auth_date;
	}
	/**
	 * @param verbal_auth_date the verbal_auth_date to set
	 */
	public void setVerbal_auth_date(String verbal_auth_date) {
		this.verbal_auth_date = verbal_auth_date;
	}
	/**
	 * @return the verbal_auth_time
	 */
	public String getVerbal_auth_time() {
		return verbal_auth_time;
	}
	/**
	 * @param verbal_auth_time the verbal_auth_time to set
	 */
	public void setVerbal_auth_time(String verbal_auth_time) {
		this.verbal_auth_time = verbal_auth_time;
	}
	/**
	 * @return the client_verification_desc
	 */
	public String getClient_verification_desc() {
		return client_verification_desc;
	}
	/**
	 * @param client_verification_desc the client_verification_desc to set
	 */
	public void setClient_verification_desc(String client_verification_desc) {
		this.client_verification_desc = client_verification_desc;
	}
	/**
	 * @return the auth_confirmed_by
	 */
	public String getAuth_confirmed_by() {
		return auth_confirmed_by;
	}
	/**
	 * @param auth_confirmed_by the auth_confirmed_by to set
	 */
	public void setAuth_confirmed_by(String auth_confirmed_by) {
		this.auth_confirmed_by = auth_confirmed_by;
	}
	/**
	 * @return the utilizedAmount
	 */
	public BigDecimal getUtilizedAmount() {
		return utilizedAmount;
	}
	/**
	 * @param utilizedAmount the utilizedAmount to set
	 */
	public void setUtilizedAmount(BigDecimal utilizedAmount) {
		this.utilizedAmount = utilizedAmount;
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
	 * @return the initiator_id
	 */
	public String getInitiator_id() {
		return initiator_id;
	}
	/**
	 * @param initiator_id the initiator_id to set
	 */
	public void setInitiator_id(String initiator_id) {
		this.initiator_id = initiator_id;
	}
	/**
	 * @return the current_owner_role
	 */
	public String getCurrent_owner_role() {
		return current_owner_role;
	}
	/**
	 * @param current_owner_role the current_owner_role to set
	 */
	public void setCurrent_owner_role(String current_owner_role) {
		this.current_owner_role = current_owner_role;
	}
	/**
	 * @return the current_owner_id
	 */
	public String getCurrent_owner_id() {
		return current_owner_id;
	}
	/**
	 * @param current_owner_id the current_owner_id to set
	 */
	public void setCurrent_owner_id(String current_owner_id) {
		this.current_owner_id = current_owner_id;
	}
	/**
	 * @return the current_owner_name
	 */
	public String getCurrent_owner_name() {
		return current_owner_name;
	}
	/**
	 * @param current_owner_name the current_owner_name to set
	 */
	public void setCurrent_owner_name(String current_owner_name) {
		this.current_owner_name = current_owner_name;
	}
	/**
	 * @return the current_approver_role
	 */
	public String getCurrent_approver_role() {
		return current_approver_role;
	}
	/**
	 * @param current_approver_role the current_approver_role to set
	 */
	public void setCurrent_approver_role(String current_approver_role) {
		this.current_approver_role = current_approver_role;
	}
	/**
	 * @return the previous_owner_role
	 */
	public String getPrevious_owner_role() {
		return previous_owner_role;
	}
	/**
	 * @param previous_owner_role the previous_owner_role to set
	 */
	public void setPrevious_owner_role(String previous_owner_role) {
		this.previous_owner_role = previous_owner_role;
	}
	/**
	 * @return the created_by_comments
	 */
	public String getCreated_by_comments() {
		return created_by_comments;
	}
	/**
	 * @param created_by_comments the created_by_comments to set
	 */
	public void setCreated_by_comments(String created_by_comments) {
		this.created_by_comments = created_by_comments;
	}
	/**
	 * @return the screen
	 */
	public String getScreen() {
		return screen;
	}
	/**
	 * @param screen the screen to set
	 */
	public void setScreen(String screen) {
		this.screen = screen;
	}
	/**
	 * @return the state
	 */
	public String getState() {
		return state;
	}
	/**
	 * @param state the state to set
	 */
	public void setState(String state) {
		this.state = state;
	}
	/**
	 * @return the action
	 */
	public String getAction() {
		return action;
	}
	/**
	 * @param action the action to set
	 */
	public void setAction(String action) {
		this.action = action;
	}
	/**
	 * @return the userFuncRoleList
	 */
	public ArrayList getUserFuncRoleList() {
		return userFuncRoleList;
	}
	/**
	 * @param userFuncRoleList the userFuncRoleList to set
	 */
	public void setUserFuncRoleList(ArrayList userFuncRoleList) {
		this.userFuncRoleList = userFuncRoleList;
	}
	/**
	 * @return the isNextApproval_Req
	 */
	public boolean isNextApproval_Req() {
		return isNextApproval_Req;
	}
	/**
	 * @param isNextApproval_Req the isNextApproval_Req to set
	 */
	public void setNextApproval_Req(boolean isNextApproval_Req) {
		this.isNextApproval_Req = isNextApproval_Req;
	}
	/**
	 * @return the next_approver_role
	 */
	public String getNext_approver_role() {
		return next_approver_role;
	}
	/**
	 * @param next_approver_role the next_approver_role to set
	 */
	public void setNext_approver_role(String next_approver_role) {
		this.next_approver_role = next_approver_role;
	}
	/**
	 * @return the accumulated_transfers
	 */
	public Double getAccumulated_transfers() {
		return accumulated_transfers;
	}
	/**
	 * @param accumulated_transfers the accumulated_transfers to set
	 */
	public void setAccumulated_transfers(Double accumulated_transfers) {
		this.accumulated_transfers = accumulated_transfers;
	}
	/**
	 * @return the accumulated_amount
	 */
	public BigDecimal getAccumulated_amount() {
		return accumulated_amount;
	}
	/**
	 * @param accumulated_amount the accumulated_amount to set
	 */
	public void setAccumulated_amount(BigDecimal accumulated_amount) {
		this.accumulated_amount = accumulated_amount;
	}
	/**
	 * @return the prefered_next_txn_dt
	 */
	public Timestamp getPrefered_next_txn_dt() {
		return prefered_next_txn_dt;
	}
	/**
	 * @param prefered_next_txn_dt the prefered_next_txn_dt to set
	 */
	public void setPrefered_next_txn_dt(Timestamp prefered_next_txn_dt) {
		this.prefered_next_txn_dt = prefered_next_txn_dt;
	}
	/**
	 * @return the next_txn_dt
	 */
	public Timestamp getNext_txn_dt() {
		return next_txn_dt;
	}
	/**
	 * @param next_txn_dt the next_txn_dt to set
	 */
	public void setNext_txn_dt(Timestamp next_txn_dt) {
		this.next_txn_dt = next_txn_dt;
	}
	/**
	 * @return the originating_parent_conf_no
	 */
	public String getOriginating_parent_conf_no() {
		return originating_parent_conf_no;
	}
	/**
	 * @param originating_parent_conf_no the originating_parent_conf_no to set
	 */
	public void setOriginating_parent_conf_no(String originating_parent_conf_no) {
		this.originating_parent_conf_no = originating_parent_conf_no;
	}
	/**
	 * @return the rta_booked_flag
	 */
	public String getRta_booked_flag() {
		return rta_booked_flag;
	}
	/**
	 * @param rta_booked_flag the rta_booked_flag to set
	 */
	public void setRta_booked_flag(String rta_booked_flag) {
		this.rta_booked_flag = rta_booked_flag;
	}
	/**
	 * @return the rta_Action_key
	 */
	public Double getRta_Action_key() {
		return rta_Action_key;
	}
	/**
	 * @param rta_Action_key the rta_Action_key to set
	 */
	public void setRta_Action_key(Double rta_Action_key) {
		this.rta_Action_key = rta_Action_key;
	}
	/**
	 * @return the rta_Action_Key_Fee
	 */
	public Double getRta_Action_Key_Fee() {
		return rta_Action_Key_Fee;
	}
	/**
	 * @param rta_Action_Key_Fee the rta_Action_Key_Fee to set
	 */
	public void setRta_Action_Key_Fee(Double rta_Action_Key_Fee) {
		this.rta_Action_Key_Fee = rta_Action_Key_Fee;
	}
	/**
	 * @return the current_rta_booked_flag
	 */
	public String getCurrent_rta_booked_flag() {
		return current_rta_booked_flag;
	}
	/**
	 * @param current_rta_booked_flag the current_rta_booked_flag to set
	 */
	public void setCurrent_rta_booked_flag(String current_rta_booked_flag) {
		this.current_rta_booked_flag = current_rta_booked_flag;
	}
	/**
	 * @return the reverseRTAFlag
	 */
	public boolean isReverseRTAFlag() {
		return reverseRTAFlag;
	}
	/**
	 * @param reverseRTAFlag the reverseRTAFlag to set
	 */
	public void setReverseRTAFlag(boolean reverseRTAFlag) {
		this.reverseRTAFlag = reverseRTAFlag;
	}
	/**
	 * @return the txn_Fee_Charge
	 */
	public BigDecimal getTxn_Fee_Charge() {
		return txn_Fee_Charge;
	}
	/**
	 * @param txn_Fee_Charge the txn_Fee_Charge to set
	 */
	public void setTxn_Fee_Charge(BigDecimal txn_Fee_Charge) {
		this.txn_Fee_Charge = txn_Fee_Charge;
	}
	/**
	 * @return the prev_txn_Fee_Charge
	 */
	public BigDecimal getPrev_txn_Fee_Charge() {
		return prev_txn_Fee_Charge;
	}
	/**
	 * @param prev_txn_Fee_Charge the prev_txn_Fee_Charge to set
	 */
	public void setPrev_txn_Fee_Charge(BigDecimal prev_txn_Fee_Charge) {
		this.prev_txn_Fee_Charge = prev_txn_Fee_Charge;
	}
	/**
	 * @return the isFeeApplicable
	 */
	public boolean isFeeApplicable() {
		return isFeeApplicable;
	}
	/**
	 * @param isFeeApplicable the isFeeApplicable to set
	 */
	public void setFeeApplicable(boolean isFeeApplicable) {
		this.isFeeApplicable = isFeeApplicable;
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
	 * @return the retirement_mnemonic_desc
	 */
	public String getRetirement_mnemonic_desc() {
		return retirement_mnemonic_desc;
	}
	/**
	 * @param retirement_mnemonic_desc the retirement_mnemonic_desc to set
	 */
	public void setRetirement_mnemonic_desc(String retirement_mnemonic_desc) {
		this.retirement_mnemonic_desc = retirement_mnemonic_desc;
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
	 * @return the iraTxnFlag
	 */
	public String getIraTxnFlag() {
		return iraTxnFlag;
	}
	/**
	 * @param iraTxnFlag the iraTxnFlag to set
	 */
	public void setIraTxnFlag(String iraTxnFlag) {
		this.iraTxnFlag = iraTxnFlag;
	}
	/**
	 * @return the create_limit
	 */
	public int getCreate_limit() {
		return create_limit;
	}
	/**
	 * @param create_limit the create_limit to set
	 */
	public void setCreate_limit(int create_limit) {
		this.create_limit = create_limit;
	}
	/**
	 * @return the update_limit
	 */
	public int getUpdate_limit() {
		return update_limit;
	}
	/**
	 * @param update_limit the update_limit to set
	 */
	public void setUpdate_limit(int update_limit) {
		this.update_limit = update_limit;
	}
	/**
	 * @return the delete_limit
	 */
	public int getDelete_limit() {
		return delete_limit;
	}
	/**
	 * @param delete_limit the delete_limit to set
	 */
	public void setDelete_limit(int delete_limit) {
		this.delete_limit = delete_limit;
	}
	/**
	 * @return the limit_Action
	 */
	public String getLimit_Action() {
		return limit_Action;
	}
	/**
	 * @param limit_Action the limit_Action to set
	 */
	public void setLimit_Action(String limit_Action) {
		this.limit_Action = limit_Action;
	}
	/**
	 * @return the parTxnRequestDate
	 */
	public Timestamp getParTxnRequestDate() {
		return parTxnRequestDate;
	}
	/**
	 * @param parTxnRequestDate the parTxnRequestDate to set
	 */
	public void setParTxnRequestDate(Timestamp parTxnRequestDate) {
		this.parTxnRequestDate = parTxnRequestDate;
	}
	/**
	 * @return the case_status
	 */
	public String getCase_status() {
		return case_status;
	}
	/**
	 * @param case_status the case_status to set
	 */
	public void setCase_status(String case_status) {
		this.case_status = case_status;
	}
	/**
	 * @return the cancelRTAFlag
	 */
	public boolean isCancelRTAFlag() {
		return cancelRTAFlag;
	}
	/**
	 * @param cancelRTAFlag the cancelRTAFlag to set
	 */
	public void setCancelRTAFlag(boolean cancelRTAFlag) {
		this.cancelRTAFlag = cancelRTAFlag;
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

		retValue = "PaymentDetailsTO ( "
			+ super.toString() + TAB
			+ "transactionId = " + this.transactionId + TAB
			+ "transactionVersion = " + this.transactionVersion + TAB
			+ "chkTxnVersion = " + this.chkTxnVersion + TAB
			+ "txnBatchId = " + this.txnBatchId + TAB
			+ "txnBatchVersion = " + this.txnBatchVersion + TAB
			+ "recParentTxnId = " + this.recParentTxnId + TAB
			+ "recParentTxnVersion = " + this.recParentTxnVersion + TAB
			+ "paymentHubTxnId = " + this.paymentHubTxnId + TAB
			+ "nextpaymentHubTxnId = " + this.nextpaymentHubTxnId + TAB
			+ "from_Account = " + this.from_Account + TAB
			+ "from_KeyAccount = " + this.from_KeyAccount + TAB
			+ "to_Account = " + this.to_Account + TAB
			+ "to_KeyAccount = " + this.to_KeyAccount + TAB
			+ "extAccount_Number = " + this.extAccount_Number + TAB
			+ "extAcc_NickName = " + this.extAcc_NickName + TAB
			+ "extAccount_RefId = " + this.extAccount_RefId + TAB
			+ "extAccount_Version = " + this.extAccount_Version + TAB
			+ "transfer_Amount = " + this.transfer_Amount + TAB
			+ "transfer_Currency = " + this.transfer_Currency + TAB
			+ "fxRate = " + this.fxRate + TAB
			+ "initiation_Date = " + this.initiation_Date + TAB
			+ "requestedDate = " + this.requestedDate + TAB
			+ "estimatedArrivalDate = " + this.estimatedArrivalDate + TAB
			+ "actualExecDate = " + this.actualExecDate + TAB
			+ "txnExpiryDate = " + this.txnExpiryDate + TAB
			+ "frequency_Type = " + this.frequency_Type + TAB
			+ "frequency_DurationDesc = " + this.frequency_DurationDesc + TAB
			+ "frequency_DurationValue = " + this.frequency_DurationValue + TAB
			+ "duration_EndDate = " + this.duration_EndDate + TAB
			+ "duration_AmountLimit = " + this.duration_AmountLimit + TAB
			+ "duration_NoOfTransfers = " + this.duration_NoOfTransfers + TAB
			+ "business_Date = " + this.business_Date + TAB
			+ "transfer_Type = " + this.transfer_Type + TAB
			+ "screen_Type = " + this.screen_Type + TAB
			+ "msBranchId = " + this.msBranchId + TAB
			+ "applicationId = " + this.applicationId + TAB
			+ "isTxnModified = " + this.isTxnModified + TAB
			+ "isTxnCancelled = " + this.isTxnCancelled + TAB
			+ "isTxnApproved = " + this.isTxnApproved + TAB
			+ "isTxnInitiated = " + this.isTxnInitiated + TAB
			+ "isTxnResumed = " + this.isTxnResumed + TAB
			+ "isParentTxnSuspended = " + this.isParentTxnSuspended + TAB
			+ "isCheckPrinted = " + this.isCheckPrinted + TAB
			+ "isChildTxnCreated = " + this.isChildTxnCreated + TAB
			+ "isAccounEligible = " + this.isAccounEligible + TAB
			+ "isStatusChkReq = " + this.isStatusChkReq + TAB
			+ "isVersionChkReq = " + this.isVersionChkReq + TAB
			+ "requestHeaderInfo = " + this.requestHeaderInfo + TAB
			+ "rsa_Review_Flag = " + this.rsa_Review_Flag + TAB
			+ "trial_depo = " + this.trial_depo + TAB
			+ "versionChkId = " + this.versionChkId + TAB
			+ "statusChkId = " + this.statusChkId + TAB
			+ "statusChkEventDesc = " + this.statusChkEventDesc + TAB
			+ "loggingAction = " + this.loggingAction + TAB
			+ "mMSystemDesc = " + this.mMSystemDesc + TAB
			+ "isFrmAcc_InContext = " + this.isFrmAcc_InContext + TAB
			+ "isToAcc_InContext = " + this.isToAcc_InContext + TAB
			+ "isAcntDataInSession = " + this.isAcntDataInSession + TAB
			+ "eventDesc = " + this.eventDesc + TAB
			+ "txnPrevStatusCode = " + this.txnPrevStatusCode + TAB
			+ "txnCurrentStatusCode = " + this.txnCurrentStatusCode + TAB
			+ "prevAction = " + this.prevAction + TAB
			+ "prevTxnAmount = " + this.prevTxnAmount + TAB
			+ "prevTxnDate = " + this.prevTxnDate + TAB
			+ "childTxnRequestedExecDate = " + this.childTxnRequestedExecDate + TAB
			+ "childTransactionId = " + this.childTransactionId + TAB
			+ "childTxnBatchId = " + this.childTxnBatchId + TAB
			+ "childloggingAction = " + this.childloggingAction + TAB
			+ "childTxnStatusCode = " + this.childTxnStatusCode + TAB
			+ "childTxnAmount = " + this.childTxnAmount + TAB
			+ "created_by_role = " + this.created_by_role + TAB
			+ "createdby_name = " + this.createdby_name + TAB
			+ "createdby = " + this.createdby + TAB
			+ "createdDate = " + this.createdDate + TAB
			+ "life_user_id = " + this.life_user_id + TAB
			+ "dont_spawn_flag = " + this.dont_spawn_flag + TAB
			+ "same_name_flag = " + this.same_name_flag + TAB
			+ "key_client = " + this.key_client + TAB
			+ "key_link = " + this.key_link + TAB
			+ "key_clientId = " + this.key_clientId + TAB
			+ "auth_mode = " + this.auth_mode + TAB
			+ "auth_info_reciever = " + this.auth_info_reciever + TAB
			+ "verbal_auth_client_name = " + this.verbal_auth_client_name + TAB
			+ "verbal_auth_date = " + this.verbal_auth_date + TAB
			+ "verbal_auth_time = " + this.verbal_auth_time + TAB
			+ "client_verification_desc = " + this.client_verification_desc + TAB
			+ "auth_confirmed_by = " + this.auth_confirmed_by + TAB
			+ "utilizedAmount = " + this.utilizedAmount + TAB
			+ "initiator_role = " + this.initiator_role + TAB
			+ "initiator_id = " + this.initiator_id + TAB
			+ "current_owner_role = " + this.current_owner_role + TAB
			+ "current_owner_id = " + this.current_owner_id + TAB
			+ "current_owner_name = " + this.current_owner_name + TAB
			+ "current_approver_role = " + this.current_approver_role + TAB
			+ "previous_owner_role = " + this.previous_owner_role + TAB
			+ "created_by_comments = " + this.created_by_comments + TAB
			+ "screen = " + this.screen + TAB
			+ "state = " + this.state + TAB
			+ "action = " + this.action + TAB
			+ "userFuncRoleList = " + this.userFuncRoleList + TAB
			+ "isNextApproval_Req = " + this.isNextApproval_Req + TAB
			+ "next_approver_role = " + this.next_approver_role + TAB
			+ "accumulated_transfers = " + this.accumulated_transfers + TAB
			+ "accumulated_amount = " + this.accumulated_amount + TAB
			+ "prefered_next_txn_dt = " + this.prefered_next_txn_dt + TAB
			+ "next_txn_dt = " + this.next_txn_dt + TAB
			+ "parTxnRequestDate = " + this.parTxnRequestDate + TAB
			+ "originating_parent_conf_no = " + this.originating_parent_conf_no + TAB
			+ "rta_booked_flag = " + this.rta_booked_flag + TAB
			+ "rta_Action_key = " + this.rta_Action_key + TAB
			+ "rta_Action_Key_Fee = " + this.rta_Action_Key_Fee + TAB
			+ "current_rta_booked_flag = " + this.current_rta_booked_flag + TAB
			+ "reverseRTAFlag = " + this.reverseRTAFlag + TAB
			+ "cancelRTAFlag = " + this.cancelRTAFlag + TAB
			+ "txn_Fee_Charge = " + this.txn_Fee_Charge + TAB
			+ "prev_txn_Fee_Charge = " + this.prev_txn_Fee_Charge + TAB
			+ "isFeeApplicable = " + this.isFeeApplicable + TAB
			+ "retirement_mnemonic = " + this.retirement_mnemonic + TAB
			+ "retirement_mnemonic_desc = " + this.retirement_mnemonic_desc + TAB
			+ "qualifier = " + this.qualifier + TAB
			+ "reverse_qualifier = " + this.reverse_qualifier + TAB
			+ "iraTxnFlag = " + this.iraTxnFlag + TAB
			+ "create_limit = " + this.create_limit + TAB
			+ "update_limit = " + this.update_limit + TAB
			+ "delete_limit = " + this.delete_limit + TAB
			+ "limit_Action = " + this.limit_Action + TAB
			+ "case_status = " + this.case_status + TAB
			+ "ofac_case_id = " + this.ofac_case_id + TAB
			+ " )";

		return retValue;
	}

}