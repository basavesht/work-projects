package com.tcs.Payments.ms360Utils;

/**
 * @author : 224703
 * **************   Revision History ************************
 * Modified-By |  Modified-Date |  Project-Phase  | Changes
 *
 * **********************************************************
 */
public class Bus_Rule_Input_Desc {

	//Business Rule event description....
	public final static String Create_PreConfirm       = "Create-PreConfirm";
	public final static String Create_Confirm          = "Create-Confirm";
	public final static String Modify_PreConfirm       = "Modify-PreConfirm";
	public final static String Modify_Confirm          = "Modify-Confirm";
	public final static String Approve_PreConfirm      = "Approve-PreConfirm";
	public final static String Approve_Confirm         = "Approve-Confirm";
	public final static String Cancel                  = "Cancel";
	public final static String Confirm                 = "Confirm";
	public final static String Create                  = "Create";

	//Business Rule type...
	public final static String RULE_TYPE_TXN           = "Transaction";
	public final static String RULE_TYPE_ACNT          = "AccountRule";
	public final static String RULE_TYPE_BLCK_DISP     = "Block display";

	//Business Rule state description...
	public final static String STATE_FUTURE            = "Future";
	public final static String STATE_CURRENT           = "Current";

	//Business rule type description..
	public final static String TYPE_INT                = "InternalTransfer";
	public final static String TYPE_ACH                = "ACH";
	public final static String TYPE_PORTFOLIO_LOAN     = "PLA";

	//Business rule extra string attribute...
	public final static String EXT_STR_CHILD           = "Child";

	//Business rule frequency_desc type....
	public final static String ONE_TIME = "One-Time";
	public final static String REC_W    = "Weekly";
	public final static String REC_OW   = "EveryOtherWeek";
	public final static String REC_M    = "Monthly";
	public final static String REC_FBD  = "FirstBusDayOfMonth";
	public final static String REC_LBD  = "LastBusDayOfMonth";
	public final static String REC_Q    = "Every3Months";
	public final static String REC_H    = "Every6Months";
	public final static String REC_Y    = "Anually";

	//Business Rule Page Type desc...
	public final static String CS_INT       = "CSInternalTransfer";
	public final static String CS_ACH       = "CSACH";
	public final static String MS360_INT    = "MS360InternalTransfer";
	public final static String MS360_ACH    = "MS360ACH";
	public final static String MS360_CHECK  = "MS360Check";

	//Business Rule transaction sub type..
	public final static String MS_INCOMING    = "Incoming";
	public final static String MS_OUTGOING    = "Outgoing";

	//Business Rule transaction Resides desc...
	public final static String RESIDES_IN    = "In";
	public final static String RESIDES_OUT    = "Out";

	//Business Rule External account type desc...
	public final static String EXT_ACC_CHECKING    = "CHECKING";
	public final static String EXT_ACC_SAVINGS     = "SAVINGS";

	//Business Rule External account sub type desc...
	public final static String EXT_ACC_SUB_TYPE_PERSONAL   = "PERSONAL";
	public final static String EXT_ACC_SUB_TYPE_BUSINESS    = "BUSINESS";

	//Txn Repeat descriptions..
	public final static String UNTIL_CANCEL  = "UntilCancelled";
	public final static String UNTIL_XENDDATE  = "UntilXEndDate";
	public final static String UNTIL_XDOLLARLIMIT  = "UntilXDollarLimit";
	public final static String UNTIL_XTRANSFERS  = "UntilXTransfers";

	//Business Mode type...
	public final static String ONLINE_MODE           = "Online";
	public final static String BATCH_MODE            = "Batch";
}
