package com.tcs.Payments.ms360Utils;

/**
 * @author : 224703
 * **************   Revision History ************************
 * Modified-By |  Modified-Date |  Project-Phase  | Changes
 *
 * **********************************************************
 */
public class ChkReqConstants {

	public final static String STANDARD_MAIL  = "1";
	public final static String OVERNIGHT_MAIL  = "2";
	public final static String PRINT_AT_MY_BRANCH = "3";
	public final static String PRINT_AT_ANOTHER_BRANCH = "4";

	public final static String PICK_UP_OPTION_1 = "Standard Mail";
	public final static String PICK_UP_OPTION_2 = "Overnight Mail";
	public final static String PICK_UP_OPTION_3 = "Print at my branch";
	public final static String PICK_UP_OPTION_4 = "Print at another branch";

	public final static String THIRD_PARTY = "Third Party";

	public final static String ACCOUNT_PLATING = "Account Plating";

	public final static String CHK = "CHK";
	public final static String CHK_LOC = "CHK-LOC";
	public final static String CHK_REG = "CHK-REG";	

	public final static String MEMO_CHECK = "Check";
	public final static String MEMO_STUB  = "Stub";

	public final static String CERTIFIED_Y  = "Yes";
	public final static String CERTIFIED_N  = "No";

	public final static String THIRD_PARTY_REASON_1  = "Bill / Loan Payment";
	public final static String THIRD_PARTY_REASON_2  = "Money Transfer";
	public final static String THIRD_PARTY_REASON_3  = "Bank Broker";
	public final static String THIRD_PARTY_REASON_4  = "Charitable / Gift";
	public final static String THIRD_PARTY_REASON_5  = "Beneficiary";
	public final static String THIRD_PARTY_REASON_6  = "Other";

	public final static String TYPE_OF_ID_1  = "Known Client";
	public final static String TYPE_OF_ID_2  = "Drivers License";
	public final static String TYPE_OF_ID_3  = "Non-Drivers License";
	public final static String TYPE_OF_ID_4  = "Passport";
	public final static String TYPE_OF_ID_5  = "Military Card";
	public final static String TYPE_OF_ID_6  = "Messenger";
	public final static String TYPE_OF_ID_7  = "Other";

	public final static String YES  = "Y";
	public final static String NO  = "N";
	public final static String ET  = " ET";

	public final static String DEFAULT_ADDR  = "(Default Address) ";
	public final static String FORIEGN_ADDR  = "(Foreign Address) ";
	public final static String NOT_DEFAULT  = "(not default) ";

	public final static int STATUS_AWAITING_PRINT  = 103;
	public final static int STATUS_PRINTED  = 101;
	public final static int STATUS_SENT_TO_PRINTER  = 102;
	public final static int STATUS_PRINT_FAILED  = 110;

	public final static String PRINT_ON_CHECK  = "(Print on check)";
	public final static String PRINT_ON_STUB  = "(Print on stub)";
	public final static String PRINT_ON_CHECK_STUB  = "(Print on check and stub)";

	//Check type for the check number interface...
	public final static String CHECKTYPE_CERTIFIED  = "C";
	public final static String CHECKTYPE_OVERNIGHT  = "O";

	//Check type for the print number interface...
	public final static String PRINTCHECKTYPE_CERTIFIED      = "Certified";
	public final static String PRINTCHECKTYPE_NON_CERTIFIED  = "Non-Certified";

	//Check Void Drop Down Descriptions......
	public final static String VOIDCHECK_111 = "Check did Not Print (Update to Print Failed so I can re-print for client)";
	public final static String VOIDCHECK_112 = "Check Printed - Poor Quality (Update to Print Failed so I can re-print for client)";
	public final static String VOIDCHECK_113 = "Check did Not Print (Update to Cancelled; funds will be returned to client and I will not be able to reprint)";
	public final static String VOIDCHECK_114 = "Check Printed - Poor Quality (Updated to Cancelled; funds will be returned to client and I will not be able to reprint)";
	public final static String VOIDCHECK_115 = "Check Printed - Client did not Want (Updated to Cancelled; funds will be returned to client and I will not be able to reprint)";

	public final static String PRINT_SUCCESS      = "SUCCESS";
	public final static String PRINT_FAILED      = "FAILED";
	public final static String CHECK_ACTIVE      = "A";
	public final static String CHECK_VOID     = "V";
	public final static String DELIVERED_TO_CLIENT     = "Client";
	public final static String DELIVERED_TO_THIRD_PARTY     = "Third Party";

	public final static String CHARGE_TO_CLIENT  = "Client";
	public final static String CHARGE_TO_BRANCH  = "Branch";
	public final static String FEE_WARRNING_BRANCH = "Charged To Branch";
	public final static String FEE_WARRNING_CLIENT = "(Overnight Fee)";

	public final static String PAYEE_TYPE_1 = "1";
	public final static String PAYEE_TYPE_2 = "2";
	public final static String PAYEE_TYPE_3 = "3";	
}

