package com.tcs.Payments.ms360Utils;

import java.util.Arrays;
import java.util.List;


/**
 * @author : 224703
 * **************   Revision History ************************
 * Modified-By |  Modified-Date |  Project-Phase  | Changes
 *
 * **********************************************************
 */
public class MSSystemDefaults {

	public final static String MSACC_HELD_AT          = "MSSB";
	public final static String NOT_APPLICABLE         = "N/A";
	public final static String DEFAULT_ACC_TEXT       = "Select Account";
	public final static String DEFAULT_SPOKE_TO_TEXT  = "Select person"; 
	public final static String DEFAULT_DATE_TXT       = "MM/DD/YYYY"; 

	//Page Id's for ClientServ Screens ...
	public final static String INTERNAL_TRANSFERS     = "6401"; 
	public final static String EXTERNAL_TRANSFERS     = "6402"; 
	public final static String PENDING_TRANSFERS      = "6404"; 
	public final static String TRANSFE_HISTORY        = "6405"; 

	//External account sign up mode ...
	public final static String PAPER_MODE             = "Paper"; 
	public final static String ONLINE_MODE            = "Online"; 

	//Enable/Disable MS360 24*7 Functionality based on transaction type...
	public final static boolean ENABLE_24X7    = true;
	public final static List    PermittedTxnsList_24x7  = Arrays.asList(TxnTypeCode.INTERNAL);

	//Third Party Indicator flag...
	public final static String THRD_PARTY_Y    = "Yes";
	public final static String THRD_PARTY_N    = "No";

	//PLA Display Text
	public final static String LOAN_ACC_PREFIX_TEXT    = "PLA";
	public final static String LOAN_ACC_IND_REGX       = "PLA~";
	public final static String LOANACC_DTLS            = "PLA";
}
