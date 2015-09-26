package com.tcs.utilities.dbBulking;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * @author 224703
 *
 */
public class DataPoolGeneration extends DBBulking 
{
	//Constants ....
	private final static Integer FA_PER_BRANCH = 20;

	private final static Integer MS_CLIENTS_PER_FA = 5;

	private final static Integer INT_ACNT_PER_CLIENT = 2;
	private final static Integer EXT_ACNT_PER_CLIENT = 4;

	private final static Integer FA_START_RANGE = 100 ;
	private final static Integer INT_ACNT_START_RANGE = 000001 ;

	private final static List APPROVER_ROLE = Arrays.asList("APRVRISK","APRVBRAN","APRVRSSO","APRVNBOP","APRVMARP","APRVMAMG","APRVMAVP") ;
	private final static List INITIATOR_ROLE = Arrays.asList("INITBRAN","INITNBOP") ;

	private Random random_number = new Random();

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws Exception
	{
		try 
		{
			System.out.println("User Data Pool Creation");
			Integer total_branches = Integer.parseInt(readTotalBranch("Enter total number of MS branches : "));
			if(total_branches!= null && total_branches!=0) {
				DataPoolGeneration dataPool = new DataPoolGeneration();
				dataPool.generateDataPool(total_branches);
			}
			else {
				System.out.println("Please enter a valid number!!");
			}
		} catch (RuntimeException e) {
			e.printStackTrace();
		}
	}

	private void generateDataPool(Integer total_branches) throws Exception 
	{
		try 
		{
			int no_of_branches = 0 ;
			String ms_acnt_number = "";
			String ms_key_account_number = "";
			String ms_key_client_id = "";
			String racf_id = "";
			String UUID = "";
			MS360_UsersTO ms_users = new MS360_UsersTO();
			MS_Internal_Accounts ms_internal_accounts = new MS_Internal_Accounts();
			MS_ACH_Accounts ms_ACH_Accounts = new MS_ACH_Accounts();
			ClientServ_UsersTO clientServ_Users = new ClientServ_UsersTO();

			//Get the connection..
			DBUtils dbutils = new DBUtils();
			conn = dbutils.getConnection();

			while(no_of_branches < total_branches)
			{
				//Get a new connection for each branch Bulking...
				if(conn == null) {
					conn = dbutils.getConnection();
				}

				String branch_value = getNextBranch();
				Integer fa_value = FA_START_RANGE ;

				int j = 0;
				while (j < FA_PER_BRANCH)
				{
					Integer acnt_number = INT_ACNT_START_RANGE ;

					//Insert into MS360_Users....
					racf_id = getUniqueRACF_ID();
					ms_users.setRacf_id(racf_id);
					ms_users.setBranch(formatBranch(branch_value));
					ms_users.setInitiator_role((String)INITIATOR_ROLE.get(random_number.nextInt(INITIATOR_ROLE.size())));
					if(Integer.parseInt(branch_value) % 10 == 0) {
						ms_users.setApprover_role((String)APPROVER_ROLE.get(random_number.nextInt(APPROVER_ROLE.size())));
					}
					bulk_MS360_Users (ms_users);

					int ms_clients_per_fa = 0 ;
					while (ms_clients_per_fa < MS_CLIENTS_PER_FA) 
					{
						ms_key_client_id = getUniqueKeyClientIdentifier();
						UUID = getUniqueUUID();

						//Insert into ClientServ_Users...
						clientServ_Users.setKey_client_id(ms_key_client_id);
						clientServ_Users.setUuid(UUID);
						bulk_ClientServ_Users(clientServ_Users);

						int int_acnt_per_client = 0 ;
						while (int_acnt_per_client < INT_ACNT_PER_CLIENT) 
						{
							ms_acnt_number = (formatBranch(branch_value) + formatAccount(acnt_number) + formatFA(fa_value));
							ms_key_account_number = getUniqueKeyAccountNumber();

							//Insert into MS_Internal_Accounts...
							ms_internal_accounts.setAccount_number(ms_acnt_number);
							ms_internal_accounts.setKey_account_number(ms_key_account_number);
							ms_internal_accounts.setKey_client_id(ms_key_client_id);
							ms_internal_accounts.setUuid(UUID);
							ms_internal_accounts.setMs360_linked_account("Y");
							ms_internal_accounts.setRacf_id(racf_id);
							bulk_MS_Internal_Accounts(ms_internal_accounts);

							int_acnt_per_client ++ ;
							acnt_number ++ ;
						}

						int ext_acnt_per_client = 0 ;
						while (ext_acnt_per_client < EXT_ACNT_PER_CLIENT)
						{
							//Insert into MS_ACH_Accounts...
							ms_ACH_Accounts.setKey_client_id(ms_key_client_id);
							bulk_MS_ACH_Accounts(ms_ACH_Accounts);

							ext_acnt_per_client ++ ;
						}
						ms_clients_per_fa ++ ;
					}
					fa_value ++ ;
					j++ ;
				}
				commitDataPool(branch_value);
				no_of_branches ++ ;
			}
		}
		catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		finally 
		{
			try {
				if(conn != null) 
					conn.close();
			} catch (SQLException sqle) {
				sqle.printStackTrace();
			}
		}
	}

	public static String formatBranch(String branch)
	{
		//Formatting Branch number ...
		String officeAcc = branch.toString();
		if(officeAcc!=null) {
			do{
				if(officeAcc.length()>=3){
					break;
				}
				else {
					officeAcc="0"+officeAcc;
				}
			}
			while(officeAcc.length()!=3);
		}
		return officeAcc;
	}

	public static String formatFA(Integer fa)
	{
		//Formatting FA number ...
		String officeAcc = fa.toString();
		if(officeAcc!=null) {
			do{
				if(officeAcc.length()>=3){
					break;
				}
				else {
					officeAcc="0"+officeAcc;
				}
			}
			while(officeAcc.length()!=3);
		}
		return officeAcc;
	}

	public static String formatAccount(Integer account)
	{
		//Formatting Office number ...
		String officeAcc = account.toString();
		if(officeAcc!=null) {
			do{
				if(officeAcc.length()>=6){
					break;
				}
				else {
					officeAcc="0"+officeAcc;
				}
			}
			while(officeAcc.length()!=6);
		}
		return officeAcc;
	}

	public String getUniqueRACF_ID(){
		return getUniqueRACF_ID_SEQ();
	}

	public String getUniqueKeyClientIdentifier(){
		return getUniqueKeyClientIdentifier_SEQ();
	}

	public String getUniqueUUID(){
		return getUniqueUUID_SEQ();
	}

	public String getUniqueKeyAccountNumber() {
		return getUniqueKeyAccountNumber_SEQ();
	}

	public String getNextBranch(){
		return getNextBranch_SEQ();
	}

	public static String readTotalBranch (String prompt) {
		System.out.print(prompt);
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		String no_of_branches = "";
		try {
			no_of_branches = in.readLine();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
		return no_of_branches;
	}
}
