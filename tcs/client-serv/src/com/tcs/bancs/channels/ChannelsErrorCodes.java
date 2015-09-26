package com.tcs.bancs.channels;

/**
 * @author : 224703
 * **************   Revision History ************************
 * Modified-By |  Modified-Date |  Project-Phase  | Changes
 *    224703       15-06-2011        3               External Account Rule
 * **********************************************************
 */
public abstract class ChannelsErrorCodes {

	//Internal Errors (Prefix 999)
	public static final int INTERNAL_ERROR  = 999001;
	public static final int STATUS_CONSISTENCY_FAILURE  = 999002;
	public static final int INT_ACC_DAP_FAILURE  = 999003;
	public static final int VALIDATION_ERROR = 999004;
	public static final int EXT_ACC_DAP_FAILURE  = 999005;
	public static final int ACCESS_ERROR = 999006;
	public static final int INVALID_ROTUING_NUM  = 999007;
	public static final int MS_ACNT_NOT_FOUND = 9990008;
	public static final int EXT_ACC_OWNER_FAILURE = 9990009;
	public static final int EXT_ACC_OWNER_FAILURE_EDIT = 9990010;
	public static final int EXT_TXN_RULES_VIOLATION  = 9990015;
	public static final int THIRD_PARTY_EXT_TXN_RULES_VIOLATION  = 9990016;
	public static final int PLA_COLLATERAL_ACNTS_EMPTY  = 9990018;

	//Business Errors -- Payments (Prefix 100)
	public static final int BUSSINESS_ERROR = 100001;
	public static final int BUSSINESS_WARNING = 100002;
	public static final int BUSSINESS_INFORMATION = 100003;
	public static final int BUSSINESS_RISK = 100008;
	public static final int LIMIT_VIOLATION = 100004;
	public static final int CUT_OFF_TIME_EXCEEDED = 100005;
	public static final int VERSION_MISMATCH_ERROR = 100006;
	public static final int BUSINESS_HOLIDAY = 100007;
}
