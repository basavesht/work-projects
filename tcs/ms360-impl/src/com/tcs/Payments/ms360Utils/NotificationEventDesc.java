package com.tcs.Payments.ms360Utils;

/**
 * @author : 224703
 * **************   Revision History ************************
 * Modified-By |  Modified-Date |  Project-Phase  | Changes
 *
 * **********************************************************
 */
public class NotificationEventDesc {

	//Until Value..
	public final static String UNTILVALUE_CANCEL            = "cancelled";
	public final static String UNTILVALUE_DATE              = "";
	public final static String UNTILVALUE_DOLLARLIMIT       = " is reached";
	public final static String UNTILVALUE_NOOFTRANSFERS     = " transfers are completed"; 
	public final static String UNTILVALUE_NOOFTRANSFERS_CHK = " transactions are completed";

	//Txn_Type...
	public final static String MSInternal     		="10";
	public final static String ACHDeposit    	    ="11";
	public final static String ACHWithdrawal        ="12";
	public final static String ACHDepRevrsl         ="13";
	public final static String ACHWithdwlRvsl       ="14";
	public final static String ACHRejectFee         ="16";
	public final static String ACHTrialDeposit      ="15";
	public final static String ACHTrialDepositRvsl  ="17";
	public final static String EXTAccountTxn        ="18";
	public final static String CHKLOC               ="19";
	public final static String CHKREG               ="20";

	//Application Id
	public final static String APPLICATION_ID         = "MS360";
}
