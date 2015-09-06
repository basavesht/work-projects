package com.tcs.bancs.channels;

/**
 * @author : 224703
 * **************   Revision History ************************
 * Modified-By |  Modified-Date |  Project-Phase  | Changes
 * 
 * **********************************************************
 */
public abstract class ChannelsErrorCodes {

	//Internal Errors (Prefix 999)
	public static final int INTERNAL_ERROR  = 999001;
	public static final int STATUS_CONSISTENCY_FAILURE  = 999002;
	public static final int INT_ACC_DAP_FAILURE  = 999003;
	public static final int VALIDATION_ERROR = 999004;
	public static final int ACCESS_ERROR = 999005;
	public static final int EXT_ACC_DAP_FAILURE  = 999006;
	public static final int IRA_TXN_FAILURE  = 999007;
	public static final int PRINT_ACCESS_ERROR = 999008;
	public static final int INVALID_ROTUING_NUM  = 999009;
	public static final int NO_ANCHOR_ACCOUNT_SET = 9990010;
	public static final int MS_ACNT_NOT_FOUND = 9990011;
	public static final int EXT_ACC_OWNER_FAILURE = 9990012;
	public static final int EXT_ACC_SPOKE_TO_FAILURE  = 9990013;

	//Business Rule Errors -- Payments (Prefix 100)
	public static final int BUSSINESS_ERROR = 100001;
	public static final int BUSSINESS_WARNING = 100002;
	public static final int BUSSINESS_INFORMATION = 100003;
	public static final int LIMIT_VIOLATION = 100004;
	public static final int CUT_OFF_TIME_EXCEEDED = 100005;
	public static final int VERSION_MISMATCH_ERROR = 100006;
	public static final int BUSINESS_HOLIDAY = 100007;
	public static final int APPROVER_OWNER_FAILURE = 100008;
	public static final int APPROVER_ROLE_FAILUE = 100009;

	//RSA CallBack Error codes..
	public static final int TXN_NOT_IDENTIFIED = 200001;
	public static final int RSA_REQUEST_VALIDATE_ERR = 200002;
	public static final int INVALID_RSA_CASE_STATUS = 200003;
	public static final int BT_REQUEST_VALIDATE_ERR = 200004;


}
