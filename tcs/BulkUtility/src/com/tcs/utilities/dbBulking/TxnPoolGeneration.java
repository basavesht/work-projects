package com.tcs.utilities.dbBulking;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

/**
 * @author 224703
 *
 */
public class TxnPoolGeneration extends DBBulking
{
	//Transaction distribution per day per branch between MS360 and ClientServ...
	private final static int INT_TXN_PER_BRANCH_PER_DAY_MS360 = 5 ;
	private final static int INT_TXN_PER_BRANCH_PER_DAY_CS = 1 ;

	private final static int ACH_OUT_TXN_PER_BRANCH_PER_DAY_MS360 = 17 ;
	private final static int ACH_OUT_TXN_PER_BRANCH_PER_DAY_CS = 1 ;

	private final static int ACH_IN_TXN_PER_BRANCH_PER_DAY_MS360 = 7 ;
	private final static int ACH_IN_TXN_PER_BRANCH_PER_DAY_CS = 1 ;

	private final static int CHK_REG_TXN_PER_BRANCH_PER_DAY = 3 ;
	private final static int CHK_LOC_TXN_PER_BRANCH_PER_DAY = 1 ;

	//Transaction distribution per day per branch...
	private final static int INT_TXN_PER_BRANCH_PER_DAY = INT_TXN_PER_BRANCH_PER_DAY_MS360 + INT_TXN_PER_BRANCH_PER_DAY_CS ;
	private final static int ACH_OUT_TXN_PER_BRANCH_PER_DAY = ACH_OUT_TXN_PER_BRANCH_PER_DAY_MS360 + ACH_OUT_TXN_PER_BRANCH_PER_DAY_CS ;
	private final static int ACH_IN_TXN_PER_BRANCH_PER_DAY = ACH_IN_TXN_PER_BRANCH_PER_DAY_MS360 + ACH_IN_TXN_PER_BRANCH_PER_DAY_CS;
	private final static int TXN_PER_BRANCH_PER_DAY = INT_TXN_PER_BRANCH_PER_DAY + ACH_OUT_TXN_PER_BRANCH_PER_DAY + ACH_IN_TXN_PER_BRANCH_PER_DAY + CHK_REG_TXN_PER_BRANCH_PER_DAY + CHK_LOC_TXN_PER_BRANCH_PER_DAY;

	private Random random_number = new Random();

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws Exception 
	{
		try 
		{
			System.out.println("Transaction Data Pool Creation");
			int bulk_period_in_days = Integer.parseInt(readBulkDate("Enter Data Bulk Period (in days): "));
			String bulk_date = readBulkDate("Enter Data Bulk Start Date (dd/MM/yyyy): ");
			if(bulk_date!= null && bulk_period_in_days!=0 && !bulk_date.trim().equalsIgnoreCase("")) 
			{
				int date = Integer.parseInt(bulk_date.substring(0,2));
				int month = Integer.parseInt(bulk_date.substring(3,5));
				int year =  Integer.parseInt(bulk_date.substring(6,10));
				Calendar bulk_start_date = getBulkStartDate(date,month,year);

				TxnPoolGeneration txnPool = new TxnPoolGeneration();
				txnPool.createTransactionPool(bulk_start_date,bulk_period_in_days);
			}
		} catch (RuntimeException e) {
			System.out.println("Invalid Input entered!!");
		}
	}

	/**
	 * 
	 * @throws Exception
	 */
	public void createTransactionPool(Calendar bulk_start_date,int bulk_period_in_days) throws Exception
	{
		try
		{
			Calendar txn_date = null;
			int date_count = 0 ;
			HashMap ms_acnt_1 = new HashMap();
			HashMap ms_acnt_2 = new HashMap();

			//Get the connection...
			DBUtils dbutils = new DBUtils();
			conn = dbutils.getConnection();

			//Transfer Date...
			txn_date = bulk_start_date;

			while (date_count < bulk_period_in_days ) 
			{
				//Get a new connection for each branch Bulking...
				if(conn == null) {
					conn = dbutils.getConnection();
				}

				List<String> uniqueBranches = getUniqueBranchesList();
				if(!uniqueBranches.isEmpty()) 
				{
					for(String branch:uniqueBranches) 
					{
						ArrayList uniqueRACF_ID_per_branch = getRACF_Id_per_branch(branch);
						if(!uniqueRACF_ID_per_branch.isEmpty())
						{
							int txn_per_branch_per_day_count = 0 ;
							int int_txn_per_branch_per_day_count = 0 ;
							int ach_out_per_branch_per_day_count = 0 ;
							int ach_in_per_branch_per_day_count = 0 ;
							int chk_reg_per_branch_per_day_count = 0 ;
							int chk_loc_per_branch_per_day_count = 0 ;
							while(txn_per_branch_per_day_count < TXN_PER_BRANCH_PER_DAY ) 
							{
								//Pick randomly  a FA user (RACF_ID) belonging to a selected branch...
								HashMap msUser = (HashMap)uniqueRACF_ID_per_branch.get(random_number.nextInt(uniqueRACF_ID_per_branch.size()));
								ArrayList uniqueLinkedAcnts_per_RACF_ID = getLinkedAccounts_per_RACF_ID ((String)msUser.get("RACFID"));

								//Pick randomly 2 different MS internal accounts details belonging to a particular FA..
								boolean isSameAcnts = false;
								do {
									int random1 = random_number.nextInt(uniqueLinkedAcnts_per_RACF_ID.size());
									int random2 = random_number.nextInt(uniqueLinkedAcnts_per_RACF_ID.size());
									if(random1 != random2)
									{
										ms_acnt_1 = (HashMap)uniqueLinkedAcnts_per_RACF_ID.get(random1);
										ms_acnt_2 = (HashMap)uniqueLinkedAcnts_per_RACF_ID.get(random2);
										break;
									}
									else {
										isSameAcnts = true;
									}
								} while (isSameAcnts);

								//Internal Transfers..
								if(int_txn_per_branch_per_day_count < INT_TXN_PER_BRANCH_PER_DAY) 
								{
									//Bulk internal transfers...
									PaymentDetails paymentDtls = new PaymentDetails();
									paymentDtls.setMs_Acnt_1(ms_acnt_1);
									paymentDtls.setMs_Acnt_2(ms_acnt_2);
									paymentDtls.setTxn_date(convertToTimestamp(txn_date.getTime()));
									paymentDtls.setTxn_type("INT");
									paymentDtls.setTxn_amount(new BigDecimal(100D));
									if(int_txn_per_branch_per_day_count < INT_TXN_PER_BRANCH_PER_DAY_MS360) {
										paymentDtls.setTxn_initiated_id("MS360");
									}
									else {
										paymentDtls.setTxn_initiated_id("CS");
									}

									bulkJAWS_Temp(paymentDtls,msUser);

									//Increment count..
									int_txn_per_branch_per_day_count ++ ;
									txn_per_branch_per_day_count ++ ;
								}
								//ACH-OUT Transfers..
								else if(ach_out_per_branch_per_day_count < ACH_OUT_TXN_PER_BRANCH_PER_DAY)
								{
									//Bulk ACH-OUT transfers...
									PaymentDetails paymentDtls = new PaymentDetails();
									paymentDtls.setMs_Acnt_1(ms_acnt_1);
									paymentDtls.setMs_Acnt_2(ms_acnt_2);
									paymentDtls.setTxn_date(convertToTimestamp(txn_date.getTime()));
									paymentDtls.setTxn_type("ACH-OUT");
									paymentDtls.setTxn_amount(new BigDecimal(100D));
									if(ach_out_per_branch_per_day_count < ACH_OUT_TXN_PER_BRANCH_PER_DAY_MS360) {
										paymentDtls.setTxn_initiated_id("MS360");
									}
									else {
										paymentDtls.setTxn_initiated_id("CS");
									}

									bulkFTS_Temp(paymentDtls,msUser);

									//Increment count...
									ach_out_per_branch_per_day_count ++ ;
									txn_per_branch_per_day_count ++ ;
								}
								//ACH-IN Transfers..
								else if(ach_in_per_branch_per_day_count < ACH_IN_TXN_PER_BRANCH_PER_DAY)
								{
									//Bulk ACH-IN transfers...
									PaymentDetails paymentDtls = new PaymentDetails();
									paymentDtls.setMs_Acnt_1(ms_acnt_1);
									paymentDtls.setMs_Acnt_2(ms_acnt_2);
									paymentDtls.setTxn_date(convertToTimestamp(txn_date.getTime()));
									paymentDtls.setTxn_type("ACH-IN");
									paymentDtls.setTxn_amount(new BigDecimal(100D));
									if(ach_in_per_branch_per_day_count < ACH_IN_TXN_PER_BRANCH_PER_DAY_MS360) {
										paymentDtls.setTxn_initiated_id("MS360");
									}
									else {
										paymentDtls.setTxn_initiated_id("CS");
									}

									bulkFTS_Temp(paymentDtls,msUser);

									//Increment count...
									ach_in_per_branch_per_day_count ++ ;
									txn_per_branch_per_day_count ++ ;
								}
								//CHK-REG Transfers..
								else if(chk_reg_per_branch_per_day_count < CHK_REG_TXN_PER_BRANCH_PER_DAY) 
								{
									//Bulk CHK-REG transfers...
									PaymentDetails paymentDtls = new PaymentDetails();
									paymentDtls.setMs_Acnt_1(ms_acnt_1);
									paymentDtls.setTxn_date(convertToTimestamp(txn_date.getTime()));
									paymentDtls.setTxn_type("CHK-REG");
									paymentDtls.setTxn_amount(new BigDecimal(100D));
									paymentDtls.setTxn_initiated_id("MS360");

									bulkCHK_Temp(paymentDtls,msUser);

									//Increment count...
									chk_reg_per_branch_per_day_count ++ ;
									txn_per_branch_per_day_count ++ ;
								}
								//CHK-LOC Transfers..
								else if(chk_loc_per_branch_per_day_count < CHK_LOC_TXN_PER_BRANCH_PER_DAY) 
								{

									//Bulk CHK-LOC transfers...
									PaymentDetails paymentDtls = new PaymentDetails();
									paymentDtls.setMs_Acnt_1(ms_acnt_1);
									paymentDtls.setTxn_date(convertToTimestamp(txn_date.getTime()));
									paymentDtls.setTxn_type("CHK-LOC");
									paymentDtls.setTxn_amount(new BigDecimal(100D));
									paymentDtls.setTxn_initiated_id("MS360");

									bulkCHK_Temp(paymentDtls,msUser);

									//Increment count...
									chk_loc_per_branch_per_day_count ++ ;
									txn_per_branch_per_day_count ++ ;
								}
							}
							commitTxnPool(txn_date,branch);
						}
						else {
							System.out.println("No FA's found in the branch - " + branch);
						}
					}
				}
				else {
					System.out.println("No Branches found");
					break;
				}
				txn_date = getNextFutureBusinessDay(bulk_start_date);
				bulk_start_date = txn_date ;
				date_count ++;
			}
		} 
		catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		finally {
			try {
				if(conn != null) 
					conn.close();
			} catch (SQLException sqle) {
				sqle.printStackTrace();
			}
		}
	}

	public static Calendar getNextFutureBusinessDay(Calendar cal) throws Exception {
		boolean isWeekend = false;
		do {
			cal.add(Calendar.DATE, 1); // Add one date to the current date (PostPone)
			isWeekend = checkWeekendDate(cal);
		}
		while(isWeekend == true);
		return cal;	
	}

	public static boolean checkWeekendDate(Calendar cal) {
		boolean isWeekend = false;
		if (cal.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY || cal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
			isWeekend = true;
		}
		return isWeekend;	
	}

	public static Timestamp convertToTimestamp(java.util.Date dtDate) {
		Timestamp objTimestamp = null;
		try {                      
			if(dtDate==null)
				objTimestamp=null;
			else{
				objTimestamp = new Timestamp((dtDate).getTime());
			}     
		} catch (Exception exc) {
			exc.printStackTrace();
		}
		return objTimestamp;
	} 

	public static Calendar getBulkStartDate (int date, int month , int year) {
		Calendar bulk_start_date = Calendar.getInstance();
		bulk_start_date.set(Calendar.YEAR , year );   
		bulk_start_date.set(Calendar.MONTH , month-1);   
		bulk_start_date.set(Calendar.DAY_OF_MONTH, date);  
		return bulk_start_date ;
	}

	public static String readBulkDate (String prompt) {
		System.out.print(prompt);
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		String date = "";
		try {
			date = in.readLine();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
		return date;
	}
}
