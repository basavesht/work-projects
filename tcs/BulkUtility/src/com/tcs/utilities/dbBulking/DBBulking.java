package com.tcs.utilities.dbBulking;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

/**
 * @author 224703
 *
 */
public class DBBulking {

	public transient Connection conn = null;
	private Random random_number = new Random();

	/**
	 * Bulk MS360_Users table...
	 * @param ms_users
	 */
	public void bulk_MS360_Users(MS360_UsersTO ms_users) 
	{
		PreparedStatement  preStmt_ms360_Users = null;
		try  
		{

			String createMS360_Users = "INSERT INTO MS360_USERS (RACFID,BRANCH,INITIATOR_ROLE,APPROVER_ROLE) VALUES (?,?,?,?)";
			preStmt_ms360_Users = conn.prepareStatement(createMS360_Users);

			preStmt_ms360_Users.setString(1, ms_users.getRacf_id());
			preStmt_ms360_Users.setString(2, ms_users.getBranch());
			preStmt_ms360_Users.setString(3, ms_users.getInitiator_role());
			preStmt_ms360_Users.setString(4, ms_users.getApprover_role());
			preStmt_ms360_Users.execute();
		} 
		catch (SQLException sqle) {
			sqle.printStackTrace();
		} 
		finally 
		{
			try {
				if(preStmt_ms360_Users != null)
					preStmt_ms360_Users.close();
			} catch (SQLException sqle) {
				sqle.printStackTrace();
			}
		}
	}

	/**
	 * Bulk ClientServ_Users Table..
	 * @param clientServ_Users
	 */
	public void bulk_ClientServ_Users(ClientServ_UsersTO clientServ_Users)
	{
		PreparedStatement  preStmt_clientServ_Users = null;
		try  
		{
			String createClientServ_Users = "INSERT INTO CLIENTSERV_USERS (UUID,KEYCLIENT_ID) VALUES (?,?)";
			preStmt_clientServ_Users = conn.prepareStatement(createClientServ_Users);

			preStmt_clientServ_Users.setString(1, clientServ_Users.getUuid());
			preStmt_clientServ_Users.setString(2, clientServ_Users.getKey_client_id());
			preStmt_clientServ_Users.execute();
		} 
		catch (SQLException sqle) {
			sqle.printStackTrace();
		} 
		finally 
		{
			try {
				if(preStmt_clientServ_Users != null)
					preStmt_clientServ_Users.close();
			} catch (SQLException sqle) {
				sqle.printStackTrace();
			}
		}
	}

	/**
	 * Bulk Internal Accounts Table...
	 * @param ms_internal_accounts
	 */
	public void bulk_MS_Internal_Accounts(MS_Internal_Accounts ms_internal_accounts)
	{
		PreparedStatement  preStmt_internal_accounts = null;
		try  
		{
			String createInternal_Accounts = "INSERT INTO MS_INTERNAL_ACCOUNTS (UUID,KEYCLIENT_ID,ACCOUNT_NUMBER,KEY_ACCOUNT_NUMBER,MS360_LINKED_ACCOUNT_IND,RACFID) VALUES (?,?,?,?,?,?)";
			preStmt_internal_accounts = conn.prepareStatement(createInternal_Accounts);

			preStmt_internal_accounts.setString(1, ms_internal_accounts.getUuid());
			preStmt_internal_accounts.setString(2, ms_internal_accounts.getKey_client_id());
			preStmt_internal_accounts.setString(3, ms_internal_accounts.getAccount_number());
			preStmt_internal_accounts.setString(4, ms_internal_accounts.getKey_account_number());
			preStmt_internal_accounts.setString(5, ms_internal_accounts.getMs360_linked_account());
			preStmt_internal_accounts.setString(6, ms_internal_accounts.getRacf_id());
			preStmt_internal_accounts.execute();
		} 
		catch (SQLException sqle) {
			sqle.printStackTrace();
		} 
		finally 
		{
			try {
				if(preStmt_internal_accounts != null)
					preStmt_internal_accounts.close();
			} catch (SQLException sqle) {
				sqle.printStackTrace();
			}
		}
	}

	/**
	 * Bulk ACH Accounts Table..
	 * @param ms_ach_accounts
	 */
	public void bulk_MS_ACH_Accounts(MS_ACH_Accounts ms_ach_accounts) 
	{
		PreparedStatement  preStmt = null;
		PreparedStatement  preStmt_ach_accounts = null;
		ResultSet rs = null;
		try  
		{
			preStmt = conn.prepareStatement("select nextval for EXTERNAL_ACNT_REF_ID_SEQ from sysibm.sysdummy1");
			rs = preStmt.executeQuery();
			String ext_ref_id = null;
			if(rs.next()){
				ext_ref_id = "ACHPAYEEID"+rs.getString(1);
			}

			String createExternal_Accounts = "INSERT INTO MS_ACH_ACCOUNTS (KEYCLIENT_ID,EXTERNAL_ACNT_REF_ID) VALUES (?,?)";
			preStmt_ach_accounts = conn.prepareStatement(createExternal_Accounts);

			preStmt_ach_accounts.setString(1, ms_ach_accounts.getKey_client_id());
			preStmt_ach_accounts.setString(2, ext_ref_id.toString());
			preStmt_ach_accounts.execute();
		} 
		catch (SQLException sqle) {
			sqle.printStackTrace();
		} 
		finally 
		{
			try {
				if(preStmt != null)
					preStmt.close();
			} catch (SQLException sqle) {
				sqle.printStackTrace();
			}

			try {
				if(preStmt_ach_accounts != null)
					preStmt_ach_accounts.close();
			} catch (SQLException sqle) {
				sqle.printStackTrace();
			}

			try {
				if(rs != null)
					rs.close();
			} catch (SQLException sqle) {
				sqle.printStackTrace();
			}

		}
	}

	/**
	 * Get Unique RACF_ID...
	 * @return
	 */
	public String getUniqueRACF_ID_SEQ() 
	{
		PreparedStatement  preStmt = null;
		ResultSet rs = null;
		String racf_id = null;
		try  
		{
			preStmt = conn.prepareStatement("select nextval for RACFID_SEQ as col1 from sysibm.sysdummy1");
			rs = preStmt.executeQuery();
			if(rs.next()){
				racf_id = "RACFID"+rs.getString(1);
			}
		} 
		catch (SQLException sqle) {
			sqle.printStackTrace();
		} 
		finally 
		{
			try {
				if(preStmt != null)
					preStmt.close();
			} catch (SQLException sqle) {
				sqle.printStackTrace();
			}

			try {
				if(rs != null)
					rs.close();
			} catch (SQLException sqle) {
				sqle.printStackTrace();
			}

		}
		return racf_id.toString();
	}

	/**
	 * Get Unique KeyClient Indentifier...
	 * @return
	 */
	public String getUniqueKeyClientIdentifier_SEQ() 
	{
		PreparedStatement  preStmt = null;
		ResultSet rs = null;
		String keyClient = null;
		try  
		{
			preStmt = conn.prepareStatement("select nextval for KEY_CLIENT_IDENTFIER_SEQ as col1 from sysibm.sysdummy1");
			rs = preStmt.executeQuery();
			if(rs.next()){
				keyClient = "KEYCLIENT"+rs.getString(1);
			}
		} 
		catch (SQLException sqle) {
			sqle.printStackTrace();
		} 
		finally 
		{
			try {
				if(preStmt != null)
					preStmt.close();
			} catch (SQLException sqle) {
				sqle.printStackTrace();
			}

			try {
				if(rs != null)
					rs.close();
			} catch (SQLException sqle) {
				sqle.printStackTrace();
			}

		}
		return keyClient.toString();
	}

	/**
	 * Get Unique UUI
	 * @return
	 */
	public String getUniqueUUID_SEQ() 
	{
		PreparedStatement  preStmt = null;
		ResultSet rs = null;
		String uuid = null;
		try  
		{
			preStmt = conn.prepareStatement("select nextval for UUID_SEQ as col1 from sysibm.sysdummy1");
			rs = preStmt.executeQuery();
			if(rs.next()){
				uuid = "UUID"+rs.getString(1);
			}
		} 
		catch (SQLException sqle) {
			sqle.printStackTrace();
		} 
		finally 
		{
			try {
				if(preStmt != null)
					preStmt.close();
			} catch (SQLException sqle) {
				sqle.printStackTrace();
			}

			try {
				if(rs != null)
					rs.close();
			} catch (SQLException sqle) {
				sqle.printStackTrace();
			}

		}
		return uuid.toString();
	}

	/**
	 * Get Unique KeyAccount Number...
	 * @return
	 */
	public String getUniqueKeyAccountNumber_SEQ() 
	{
		PreparedStatement  preStmt = null;
		ResultSet rs = null;
		String keyAccount = null;
		try  
		{
			preStmt = conn.prepareStatement("select nextval for KEY_ACCOUNT_NUMBER_SEQ as col1 from sysibm.sysdummy1");
			rs = preStmt.executeQuery();
			if(rs.next()){
				keyAccount = "KEYACNT"+rs.getString(1);
			}
		} 
		catch (SQLException sqle) {
			sqle.printStackTrace();
		} 
		finally 
		{
			try {
				if(preStmt != null)
					preStmt.close();
			} catch (SQLException sqle) {
				sqle.printStackTrace();
			}

			try {
				if(rs != null)
					rs.close();
			} catch (SQLException sqle) {
				sqle.printStackTrace();
			}

		}
		return keyAccount.toString();
	}

	/**
	 * Get Next Branch value...
	 * @return
	 */
	public String getNextBranch_SEQ() 
	{
		PreparedStatement  preStmt = null;
		ResultSet rs = null;
		String ms_branch = null;
		try  
		{
			preStmt = conn.prepareStatement("select nextval for MS_BRANCH_SEQ as col1 from sysibm.sysdummy1");
			rs = preStmt.executeQuery();
			if(rs.next()){
				ms_branch = rs.getString(1);
			}
		} 
		catch (SQLException sqle) {
			sqle.printStackTrace();
		} 
		finally 
		{
			try {
				if(preStmt != null)
					preStmt.close();
			} catch (SQLException sqle) {
				sqle.printStackTrace();
			}

			try {
				if(rs != null)
					rs.close();
			} catch (SQLException sqle) {
				sqle.printStackTrace();
			}

		}
		return ms_branch.toString();
	}


	/**
	 * Get distinct branches..
	 * @return
	 * @throws SQLException
	 */
	public ArrayList getUniqueBranchesList() throws SQLException
	{
		PreparedStatement  preStmt = null;
		ResultSet rs = null;
		ArrayList<String> unique_Branches = new ArrayList<String>();
		try 
		{
			//Query the database
			preStmt = conn.prepareStatement("SELECT distinct BRANCH FROM MS360_USERS");
			rs = preStmt.executeQuery();
			while(rs.next()){
				String branch_no = rs.getString("BRANCH");
				unique_Branches.add(branch_no);
			}
		} 
		catch (SQLException sqle) {
			sqle.printStackTrace();
			throw sqle ;
		} 
		finally 
		{
			try {
				if(preStmt != null)
					preStmt.close();
			} catch (SQLException sqle) {
				sqle.printStackTrace();
			}

			try {
				if(rs != null)
					rs.close();
			} catch (SQLException sqle) {
				sqle.printStackTrace();
			}
		}
		return unique_Branches;
	}

	/**
	 * Get distinct branches..
	 * @return
	 * @throws SQLException
	 */
	public ArrayList getUniqueRACFIDList() throws SQLException
	{
		PreparedStatement  preStmt = null;
		ResultSet rs = null;
		ArrayList<String> unique_RacfIds = new ArrayList<String>();
		try 
		{
			//Query the database
			preStmt = conn.prepareStatement("SELECT distinct RACFID FROM MS360_USERS");
			rs = preStmt.executeQuery();
			while(rs.next()){
				String racfids = rs.getString("RACFID");
				unique_RacfIds.add(racfids);
			}
		} 
		catch (SQLException sqle) {
			sqle.printStackTrace();
			throw sqle ;
		} 
		finally 
		{
			try {
				if(preStmt != null)
					preStmt.close();
			} catch (SQLException sqle) {
				sqle.printStackTrace();
			}

			try {
				if(rs != null)
					rs.close();
			} catch (SQLException sqle) {
				sqle.printStackTrace();
			}
		}
		return unique_RacfIds;
	}

	/**
	 * Get distinct KeyClient..
	 * @return
	 * @throws SQLException
	 */
	public ArrayList getUniqueKeyClientList() throws SQLException
	{
		PreparedStatement  preStmt = null;
		ResultSet rs = null;
		ArrayList<String> unique_KeyClients = new ArrayList<String>();
		try 
		{
			//Query the database
			preStmt = conn.prepareStatement("SELECT distinct KEYCLIENT_ID FROM MS_ACH_ACCOUNTS");
			rs = preStmt.executeQuery();
			while(rs.next()){
				String keyClient = rs.getString("KEYCLIENT_ID");
				unique_KeyClients.add(keyClient);
			}
		} 
		catch (SQLException sqle) {
			sqle.printStackTrace();
			throw sqle ;
		} 
		finally 
		{
			try {
				if(preStmt != null)
					preStmt.close();
			} catch (SQLException sqle) {
				sqle.printStackTrace();
			}

			try {
				if(rs != null)
					rs.close();
			} catch (SQLException sqle) {
				sqle.printStackTrace();
			}
		}
		return unique_KeyClients;
	}

	/**
	 * Get distinct branches..
	 * @return
	 * @throws SQLException
	 */
	public ArrayList getUniqueUUIDList() throws SQLException
	{
		PreparedStatement  preStmt = null;
		ResultSet rs = null;
		ArrayList<HashMap> unique_UUIDs = new ArrayList<HashMap>();
		try 
		{
			//Query the database
			preStmt = conn.prepareStatement("SELECT distinct UUID,KEYCLIENT_ID FROM CLIENTSERV_USERS");
			rs = preStmt.executeQuery();
			while(rs.next())
			{
				HashMap distinct_client = new HashMap();
				String uuid = rs.getString("UUID");
				String keyclient = rs.getString("KEYCLIENT_ID");

				distinct_client.put("UUID", uuid);
				distinct_client.put("KEYCLIENT_ID", keyclient);

				unique_UUIDs.add(distinct_client);
			}
		} 
		catch (SQLException sqle) {
			sqle.printStackTrace();
			throw sqle ;
		} 
		finally 
		{
			try {
				if(preStmt != null)
					preStmt.close();
			} catch (SQLException sqle) {
				sqle.printStackTrace();
			}

			try {
				if(rs != null)
					rs.close();
			} catch (SQLException sqle) {
				sqle.printStackTrace();
			}
		}
		return unique_UUIDs;
	}

	/**
	 * Get distinct RACF_Id per branch...
	 * @param branchNumber
	 * @return
	 * @throws SQLException
	 */
	public ArrayList getRACF_Id_per_branch(String branchNumber) throws SQLException
	{
		PreparedStatement  preStmt = null;
		ResultSet rs = null;
		ArrayList unique_RACF_Ids = new ArrayList();
		try 
		{
			//Query the database
			preStmt = conn.prepareStatement("SELECT distinct RACFID,INITIATOR_ROLE,APPROVER_ROLE FROM MS360_USERS Where BRANCH = ? ");
			preStmt.setString(1, branchNumber);
			rs = preStmt.executeQuery();
			while(rs.next()) {
				HashMap distinct_racf_id = new HashMap();
				String racf_Id = rs.getString("RACFID");
				distinct_racf_id.put("RACFID",racf_Id);

				String initiator = rs.getString("INITIATOR_ROLE");
				distinct_racf_id.put("INITIATOR_ROLE",initiator);

				String approver = rs.getString("APPROVER_ROLE");
				distinct_racf_id.put("APPROVER_ROLE",approver);

				unique_RACF_Ids.add(distinct_racf_id);
			}
		} 
		catch (SQLException sqle) {
			sqle.printStackTrace();
			throw sqle ;
		} 
		finally 
		{
			try {
				if(preStmt != null)
					preStmt.close();
			} catch (SQLException sqle) {
				sqle.printStackTrace();
			}

			try {
				if(rs != null)
					rs.close();
			} catch (SQLException sqle) {
				sqle.printStackTrace();
			}
		}
		return unique_RACF_Ids;
	}

	/**
	 * Get distinct linked accounts per RACF_ID..
	 * @param racf_id
	 * @return
	 * @throws SQLException
	 */
	public ArrayList getLinkedAccounts_per_RACF_ID(String racf_id) throws SQLException
	{
		PreparedStatement  preStmt = null;
		ResultSet rs = null;
		ArrayList<HashMap> unique_LinkedAcnts = new ArrayList<HashMap>();
		try 
		{
			//Query the database
			preStmt = conn.prepareStatement("SELECT UUID,KEYCLIENT_ID,ACCOUNT_NUMBER,KEY_ACCOUNT_NUMBER,MS360_LINKED_ACCOUNT_IND,RACFID FROM MS_INTERNAL_ACCOUNTS Where RACFID = ? ");
			preStmt.setString(1, racf_id);
			rs = preStmt.executeQuery();
			while(rs.next()) 
			{
				HashMap distinct_account = new HashMap();
				String uuid = rs.getString("UUID");
				String key_client_id = rs.getString("KEYCLIENT_ID");
				String account_number = rs.getString("ACCOUNT_NUMBER");
				String key_account_number = rs.getString("KEY_ACCOUNT_NUMBER");
				String ms360_linked_account_ind = rs.getString("MS360_LINKED_ACCOUNT_IND");
				String acnt_racf_id = rs.getString("RACFID");

				distinct_account.put("UUID",uuid);
				distinct_account.put("KEYCLIENT_ID",key_client_id);
				distinct_account.put("ACCOUNT_NUMBER",account_number);
				distinct_account.put("KEY_ACCOUNT_NUMBER",key_account_number);
				distinct_account.put("MS360_LINKED_ACCOUNT_IND",ms360_linked_account_ind);
				distinct_account.put("RACFID",acnt_racf_id);

				unique_LinkedAcnts.add(distinct_account);
			}
		} 
		catch (SQLException sqle) {
			sqle.printStackTrace();
			throw sqle ;
		} 
		finally 
		{
			try {
				if(preStmt != null)
					preStmt.close();
			} catch (SQLException sqle) {
				sqle.printStackTrace();
			}

			try {
				if(rs != null)
					rs.close();
			} catch (SQLException sqle) {
				sqle.printStackTrace();
			}
		}
		return unique_LinkedAcnts;
	}

	/**
	 * Get distinct linked accounts per UUID..
	 * @param racf_id
	 * @return
	 * @throws SQLException
	 */
	public ArrayList getAccounts_per_client(String uuid) throws SQLException
	{
		PreparedStatement  preStmt = null;
		ResultSet rs = null;
		ArrayList<HashMap> unique_LinkedAcnts = new ArrayList<HashMap>();
		try 
		{
			//Query the database
			preStmt = conn.prepareStatement("SELECT UUID,KEYCLIENT_ID,ACCOUNT_NUMBER,KEY_ACCOUNT_NUMBER,MS360_LINKED_ACCOUNT_IND,RACFID FROM MS_INTERNAL_ACCOUNTS Where UUID = ? ");
			preStmt.setString(1, uuid);
			rs = preStmt.executeQuery();
			while(rs.next()) 
			{
				HashMap distinct_account = new HashMap();
				String key_client_id = rs.getString("KEYCLIENT_ID");
				String account_number = rs.getString("ACCOUNT_NUMBER");
				String key_account_number = rs.getString("KEY_ACCOUNT_NUMBER");
				String ms360_linked_account_ind = rs.getString("MS360_LINKED_ACCOUNT_IND");

				distinct_account.put("KEYCLIENT_ID",key_client_id);
				distinct_account.put("ACCOUNT_NUMBER",account_number);
				distinct_account.put("KEY_ACCOUNT_NUMBER",key_account_number);
				distinct_account.put("MS360_LINKED_ACCOUNT_IND",ms360_linked_account_ind);

				unique_LinkedAcnts.add(distinct_account);
			}
		} 
		catch (SQLException sqle) {
			sqle.printStackTrace();
			throw sqle ;
		} 
		finally 
		{
			try {
				if(preStmt != null)
					preStmt.close();
			} catch (SQLException sqle) {
				sqle.printStackTrace();
			}

			try {
				if(rs != null)
					rs.close();
			} catch (SQLException sqle) {
				sqle.printStackTrace();
			}
		}
		return unique_LinkedAcnts;
	}

	/**
	 * Get Unique ACH Accounts per client..
	 * @param key_client
	 * @return
	 * @throws SQLException
	 */
	public ArrayList getACHAccounts_per_client(String key_client) throws SQLException
	{
		PreparedStatement  preStmt = null;
		ResultSet rs = null;
		ArrayList<HashMap> unique_ach_Accounts = new ArrayList<HashMap>();
		try 
		{
			//Query the database
			preStmt = conn.prepareStatement("SELECT KEYCLIENT_ID,EXTERNAL_ACNT_REF_ID FROM MS_ACH_ACCOUNTS Where KEYCLIENT_ID = ? ");
			preStmt.setString(1, key_client);
			rs = preStmt.executeQuery();
			while(rs.next()) 
			{
				HashMap distinct_ach_accounts = new HashMap();

				String key_client_id = rs.getString("KEYCLIENT_ID");
				String ach_account_ref_ids = rs.getString("EXTERNAL_ACNT_REF_ID");
				distinct_ach_accounts.put("KEYCLIENT_ID" , key_client_id);
				distinct_ach_accounts.put("EXTERNAL_ACNT_REF_ID" , ach_account_ref_ids);

				unique_ach_Accounts.add(distinct_ach_accounts);
			}
		} 
		catch (SQLException sqle) {
			sqle.printStackTrace();
			throw sqle ;
		} 
		finally 
		{
			try {
				if(preStmt != null)
					preStmt.close();
			} catch (SQLException sqle) {
				sqle.printStackTrace();
			}

			try {
				if(rs != null)
					rs.close();
			} catch (SQLException sqle) {
				sqle.printStackTrace();
			}
		}
		return unique_ach_Accounts;
	}

	/**
	 * Bulk JAWS Temp table...
	 * @param ms_users
	 * @throws SQLException 
	 */
	public void bulkJAWS_Temp(PaymentDetails paymentDtls,HashMap msUser) throws SQLException 
	{
		PreparedStatement  preStmt_jaws_mgrn_temp = null;
		try  
		{
			String createMS360_Users = "INSERT INTO JAWS_MGRN_TEMP (FROM_ACT_OFFICE , FROM_ACT_BASEE , FROM_ACT_FA ,FROM_KEY_ACT ,INIT_RACF_ID ,INIT_NAME ,TRANS_AMNT ,SHDLD_TRNSFR_DATE ,RCRNG_OPTN ,TRANSACTION_TYPE ,COMMENTS ,TO_ACCT_OFFICE ,TO_ACCT_BASE ,TO_ACCT_FA ,TO_ACCT_KEY_ACT ,TXN_STATUS,INIT_ROLE,APPROVER_ROLE,INTFID,MBRSEQNO,SEQNO,SRC_SYST) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,next value for MSJAWSMGRI,?)";
			preStmt_jaws_mgrn_temp = conn.prepareStatement(createMS360_Users);

			//Input preparation...

			//User Details..
			String racf_id = ((String)msUser.get("RACFID"));
			String initiator_role = ((String)msUser.get("INITIATOR_ROLE"));
			String approver_role = ((String)msUser.get("APPROVER_ROLE"));

			//From acnt Details...
			HashMap frm_acnt_dtls = paymentDtls.getMs_Acnt_1();
			String frm_office = ((String)frm_acnt_dtls.get("ACCOUNT_NUMBER")).substring(0, 3);
			String frm_acnt_base = ((String)frm_acnt_dtls.get("ACCOUNT_NUMBER")).substring(3, 9);
			String frm_fa = ((String)frm_acnt_dtls.get("ACCOUNT_NUMBER")).substring(9, 12);
			String frm_key_acnt = ((String)frm_acnt_dtls.get("KEY_ACCOUNT_NUMBER"));
			String uuid = ((String)frm_acnt_dtls.get("UUID"));

			//To acnt Details...
			HashMap to_acnt_dtls = paymentDtls.getMs_Acnt_2();
			String to_office = ((String)to_acnt_dtls.get("ACCOUNT_NUMBER")).substring(0, 3);
			String to_acnt_base = ((String)to_acnt_dtls.get("ACCOUNT_NUMBER")).substring(3, 9);
			String to_fa = ((String)to_acnt_dtls.get("ACCOUNT_NUMBER")).substring(9, 12);
			String to_key_acnt = ((String)to_acnt_dtls.get("KEY_ACCOUNT_NUMBER"));

			//Initiator Details..
			String initiator_id = null;
			String initiator_name = null;
			if(paymentDtls.getTxn_initiated_id().equalsIgnoreCase("MS360")){
				initiator_id = racf_id;
				initiator_name = "MS360-"+initiator_id ;
			}
			else {
				initiator_id = uuid;
				initiator_name = "Client-"+initiator_id;
				initiator_role = "Client";
			}

			preStmt_jaws_mgrn_temp.setString(1, frm_office);
			preStmt_jaws_mgrn_temp.setString(2, frm_acnt_base);
			preStmt_jaws_mgrn_temp.setString(3, frm_fa);
			preStmt_jaws_mgrn_temp.setString(4, frm_key_acnt);
			preStmt_jaws_mgrn_temp.setString(5, initiator_id);
			preStmt_jaws_mgrn_temp.setString(6, initiator_name);
			preStmt_jaws_mgrn_temp.setBigDecimal(7, paymentDtls.getTxn_amount());
			preStmt_jaws_mgrn_temp.setDate(8, new Date(paymentDtls.getTxn_date().getTime()));
			preStmt_jaws_mgrn_temp.setString(9, "N");
			preStmt_jaws_mgrn_temp.setString(10, "I");
			preStmt_jaws_mgrn_temp.setString(11, "Performance_Upload");
			preStmt_jaws_mgrn_temp.setString(12, to_office);
			preStmt_jaws_mgrn_temp.setString(13, to_acnt_base);
			preStmt_jaws_mgrn_temp.setString(14, to_fa);
			preStmt_jaws_mgrn_temp.setString(15, to_key_acnt);
			preStmt_jaws_mgrn_temp.setString(16, null);
			preStmt_jaws_mgrn_temp.setString(17, initiator_role);
			preStmt_jaws_mgrn_temp.setString(18, approver_role);
			preStmt_jaws_mgrn_temp.setString(19, "MSJAWSMGRI");
			preStmt_jaws_mgrn_temp.setDouble(20, new Double(1000));
			preStmt_jaws_mgrn_temp.setString(21, "PerfJaws");

			preStmt_jaws_mgrn_temp.execute();
		} 
		catch (SQLException sqle) {
			sqle.printStackTrace();
			throw sqle;
		} 
		finally 
		{
			try {
				if(preStmt_jaws_mgrn_temp != null)
					preStmt_jaws_mgrn_temp.close();
			} catch (SQLException sqle) {
				sqle.printStackTrace();
			}
		}
	}

	/**
	 * Bulk FTS Temp table...
	 * @param ms_users
	 * @throws SQLException 
	 */
	public void bulkFTS_Temp(PaymentDetails paymentDtls,HashMap msUser) throws SQLException 
	{
		PreparedStatement  preStmt_fts_mgrn_temp = null;
		try  
		{
			String createMS360_Users = "INSERT INTO FTSTXN_MGRN_TEMP (FROM_ACT_OFFICE , FROM_ACT_BASEE , FROM_ACT_FA ,FROM_KEY_ACT ,INIT_RACF_ID ,INIT_NAME ,TRANS_AMNT ,SHDLD_TRNSFR_DATE ,RCRNG_OPTN ,TRANSACTION_TYPE ,COMMENTS ,TO_ACT_OFFICE ,TO_ACT_BASEE ,TO_ACT_FA ,TO_KEY_ACT,HASH_CLNT_ID,EXT_ACC_NUM,TXN_STATUS,INIT_ROLE,APPROVER_ROLE,INTFID,MBRSEQNO,SEQNO,SRC_SYST) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,next value for MSFTSTXNMGRI,?)";
			preStmt_fts_mgrn_temp = conn.prepareStatement(createMS360_Users);

			//Input preparation...

			//User Details..
			String racf_id = ((String)msUser.get("RACFID"));
			String initiator_role = ((String)msUser.get("INITIATOR_ROLE"));
			String approver_role = ((String)msUser.get("APPROVER_ROLE"));
			String txn_type = paymentDtls.getTxn_type();

			//From acnt Details...
			String frm_office = null;
			String frm_acnt_base = null;
			String frm_fa = null;
			String frm_key_acnt = null;
			String frm_key_client = null;
			String frm_uuid = null ;
			if(txn_type.equalsIgnoreCase("ACH-OUT")) {
				HashMap frm_acnt_dtls = paymentDtls.getMs_Acnt_1();
				frm_office = ((String)frm_acnt_dtls.get("ACCOUNT_NUMBER")).substring(0, 3);
				frm_acnt_base = ((String)frm_acnt_dtls.get("ACCOUNT_NUMBER")).substring(3, 9);
				frm_fa = ((String)frm_acnt_dtls.get("ACCOUNT_NUMBER")).substring(9, 12);
				frm_key_acnt = ((String)frm_acnt_dtls.get("KEY_ACCOUNT_NUMBER"));
				frm_key_client = ((String)frm_acnt_dtls.get("KEYCLIENT_ID"));
				frm_uuid = ((String)frm_acnt_dtls.get("UUID"));
			}

			//To acnt Details...
			String to_office = null;
			String to_acnt_base = null;
			String to_fa = null;
			String to_key_acnt = null;
			String to_key_client = null;
			String to_uuid = null ;
			if(txn_type.equalsIgnoreCase("ACH-IN")) {
				HashMap to_acnt_dtls = paymentDtls.getMs_Acnt_2();
				to_office = ((String)to_acnt_dtls.get("ACCOUNT_NUMBER")).substring(0, 3);
				to_acnt_base = ((String)to_acnt_dtls.get("ACCOUNT_NUMBER")).substring(3, 9);
				to_fa = ((String)to_acnt_dtls.get("ACCOUNT_NUMBER")).substring(9, 12);
				to_key_acnt = ((String)to_acnt_dtls.get("KEY_ACCOUNT_NUMBER"));
				to_key_client = ((String)to_acnt_dtls.get("KEYCLIENT_ID"));
				to_uuid = ((String)to_acnt_dtls.get("UUID"));
			}

			//Initiator Details..
			String initiator_id = null;
			String initiator_name = null;
			if(paymentDtls.getTxn_initiated_id().equalsIgnoreCase("MS360")){
				initiator_id = racf_id;
				initiator_name = "MS360-"+initiator_id ;
			}
			else {
				initiator_id = frm_uuid != null ? frm_uuid : to_uuid;
				initiator_name = "Client-"+initiator_id;
				initiator_role = "Client";
			}

			//ACH Account Details..
			String ach_key_client = frm_key_client != null ? frm_key_client : to_key_client;
			ArrayList ach_accounts = getACHAccounts_per_client(ach_key_client);
			HashMap ach_acnt_dtls = (HashMap)ach_accounts.get(random_number.nextInt(ach_accounts.size()));

			preStmt_fts_mgrn_temp.setString(1, frm_office);
			preStmt_fts_mgrn_temp.setString(2, frm_acnt_base);
			preStmt_fts_mgrn_temp.setString(3, frm_fa);
			preStmt_fts_mgrn_temp.setString(4, frm_key_acnt);
			preStmt_fts_mgrn_temp.setString(5, initiator_id);
			preStmt_fts_mgrn_temp.setString(6, initiator_name);
			preStmt_fts_mgrn_temp.setBigDecimal(7, paymentDtls.getTxn_amount());
			preStmt_fts_mgrn_temp.setDate(8, new Date(paymentDtls.getTxn_date().getTime()));
			preStmt_fts_mgrn_temp.setString(9, "N");
			preStmt_fts_mgrn_temp.setString(10, "E");
			preStmt_fts_mgrn_temp.setString(11, "Performance_Upload");
			preStmt_fts_mgrn_temp.setString(12, to_office);
			preStmt_fts_mgrn_temp.setString(13, to_acnt_base);
			preStmt_fts_mgrn_temp.setString(14, to_fa);
			preStmt_fts_mgrn_temp.setString(15, to_key_acnt);
			preStmt_fts_mgrn_temp.setString(16, (String)ach_acnt_dtls.get("KEYCLIENT_ID"));
			preStmt_fts_mgrn_temp.setString(17, (String)ach_acnt_dtls.get("EXTERNAL_ACNT_REF_ID"));
			preStmt_fts_mgrn_temp.setString(18, null);
			preStmt_fts_mgrn_temp.setString(19, initiator_role);
			preStmt_fts_mgrn_temp.setString(20, approver_role);
			preStmt_fts_mgrn_temp.setString(21, "MSFTSTXNMGRI");
			preStmt_fts_mgrn_temp.setDouble(22, new Double(2000));
			preStmt_fts_mgrn_temp.setString(23, "PerfFTS");

			preStmt_fts_mgrn_temp.execute();
		} 
		catch (SQLException sqle) {
			sqle.printStackTrace();
			throw sqle;
		} 
		finally 
		{
			try {
				if(preStmt_fts_mgrn_temp != null)
					preStmt_fts_mgrn_temp.close();
			} catch (SQLException sqle) {
				sqle.printStackTrace();
			}
		}
	}

	/**
	 * Bulk CHK Temp table...
	 * @param ms_users
	 * @throws SQLException 
	 */
	public void bulkCHK_Temp(PaymentDetails paymentDtls,HashMap msUser) throws SQLException 
	{
		PreparedStatement  preStmt_chk_mgrn_temp = null;
		try  
		{
			String createMS360_Users = "INSERT INTO CHK_MGRN_TEMP (FROM_ACT_OFFICE , FROM_ACT_BASEE , FROM_ACT_FA ,FROM_KEY_ACT ,INIT_RACF_ID ,INIT_NAME ,TRANS_AMNT ,SHDLD_TRNSFR_DATE ,RCRNG_OPTN ,TRANSACTION_TYPE ,COMMENTS , PAYEE_NAME ,CERTIFIED ,CHK_PCKP_OPTN ,PCKP_ADRS_DMCL ,DFLT_ADRS_INDC ,ADRS_LINE1 ,ADRS_LINE4 ,PRNT_BRNCH ,EST_PCKP_TIME ,TXN_STATUS,INIT_ROLE,APPROVER_ROLE,INTFID,MBRSEQNO,SEQNO,SRC_SYST) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,next value for MSCHKMGRI,?)";
			preStmt_chk_mgrn_temp = conn.prepareStatement(createMS360_Users);

			//Input preparation...
			String racf_id = ((String)msUser.get("RACFID"));
			String initiator_role = ((String)msUser.get("INITIATOR_ROLE"));
			String approver_role = ((String)msUser.get("APPROVER_ROLE"));
			String txn_type = paymentDtls.getTxn_type();

			//From acnt Details...
			HashMap frm_acnt_dtls = paymentDtls.getMs_Acnt_1();
			String frm_office = ((String)frm_acnt_dtls.get("ACCOUNT_NUMBER")).substring(0, 3);
			String frm_acnt_base = ((String)frm_acnt_dtls.get("ACCOUNT_NUMBER")).substring(3, 9);
			String frm_fa = ((String)frm_acnt_dtls.get("ACCOUNT_NUMBER")).substring(9, 12);
			String frm_key_acnt = ((String)frm_acnt_dtls.get("KEY_ACCOUNT_NUMBER"));
			String frm_key_client = ((String)frm_acnt_dtls.get("KEYCLIENT_ID"));

			//Initiator Details...
			String initiator_id = null;
			String initiator_name = null;
			if(paymentDtls.getTxn_initiated_id().equalsIgnoreCase("MS360")){
				initiator_id = racf_id;
				initiator_name = "MS360-"+initiator_id ;
			}

			String payee_name = "Payee-"+frm_key_client ;
			String certified = "N";
			String chk_pckp_optn = null;
			String pckp_adrs_dmcl  = null;
			String dflt_adrs_indc = null;
			String adrs_line1 = null;
			String adrs_line4 = null;
			String prnt_brnch = null;
			String est_pckp_time = new java.util.Date().toString();
			if(txn_type.equalsIgnoreCase("CHK-REG")) {
				chk_pckp_optn = "1";
				pckp_adrs_dmcl = "Y";
				dflt_adrs_indc = "Y";
				adrs_line1 = "7358 , Austin";
				adrs_line4 = "Texas , US";
			}
			else if(txn_type.equalsIgnoreCase("CHK-LOC")) {
				chk_pckp_optn = "3";
				pckp_adrs_dmcl = "Y";
				dflt_adrs_indc = "Y";
				adrs_line1 = "7358 , Austin";
				adrs_line4 = "Texas , US";
				prnt_brnch = frm_office ;
			}

			preStmt_chk_mgrn_temp.setString(1, frm_office);
			preStmt_chk_mgrn_temp.setString(2, frm_acnt_base);
			preStmt_chk_mgrn_temp.setString(3, frm_fa);
			preStmt_chk_mgrn_temp.setString(4, frm_key_acnt);
			preStmt_chk_mgrn_temp.setString(5, initiator_id);
			preStmt_chk_mgrn_temp.setString(6, initiator_name);
			preStmt_chk_mgrn_temp.setBigDecimal(7, paymentDtls.getTxn_amount());
			preStmt_chk_mgrn_temp.setDate(8, new Date(paymentDtls.getTxn_date().getTime()));
			preStmt_chk_mgrn_temp.setString(9, "N");
			preStmt_chk_mgrn_temp.setString(10, "C");
			preStmt_chk_mgrn_temp.setString(11, "Performance_Upload");
			preStmt_chk_mgrn_temp.setString(12, payee_name);
			preStmt_chk_mgrn_temp.setString(13, certified);
			preStmt_chk_mgrn_temp.setString(14, chk_pckp_optn);
			preStmt_chk_mgrn_temp.setString(15, pckp_adrs_dmcl);
			preStmt_chk_mgrn_temp.setString(16, dflt_adrs_indc);
			preStmt_chk_mgrn_temp.setString(17, adrs_line1);
			preStmt_chk_mgrn_temp.setString(18, adrs_line4);
			preStmt_chk_mgrn_temp.setString(19, prnt_brnch);
			preStmt_chk_mgrn_temp.setString(20, est_pckp_time);
			preStmt_chk_mgrn_temp.setString(21, null);
			preStmt_chk_mgrn_temp.setString(22, initiator_role);
			preStmt_chk_mgrn_temp.setString(23, approver_role);
			preStmt_chk_mgrn_temp.setString(24, "MSCHKMGRI");
			preStmt_chk_mgrn_temp.setDouble(25, new Double(3000));
			preStmt_chk_mgrn_temp.setString(26, "PerfChk");

			preStmt_chk_mgrn_temp.execute();
		} 
		catch (SQLException sqle) {
			sqle.printStackTrace();
			throw sqle;
		} 
		finally 
		{
			try {
				if(preStmt_chk_mgrn_temp != null)
					preStmt_chk_mgrn_temp.close();
			} catch (SQLException sqle) {
				sqle.printStackTrace();
			}
		}
	}

	/**
	 * Bulk FTS ExtAccount Temp table...
	 * @param ms_users
	 * @throws SQLException 
	 */
	public void bulkFTSEXT_Temp(AccountDetails acntInfo) throws SQLException 
	{
		PreparedStatement  preStmt = null;
		PreparedStatement  preStmt_ftsExtAcnt_mgrn_temp = null;
		ResultSet rs = null;
		try  
		{
			preStmt = conn.prepareStatement("select nextval for FTSEXTACC_NUM_SEQ from sysibm.sysdummy1");
			rs = preStmt.executeQuery();
			String ext_acnt_num = null;
			if(rs.next()){
				ext_acnt_num = rs.getString(1);
			}

			String createExtAccounts = "INSERT INTO FTSEXT_MGR_TEMP (HASH_CLNT_ID ,ACT_NICK_NAME ,ACT_TYPE ,SRC_SYST ,ACT_HLDR_NAME ,THRD_PRTY_INDC,COMMENTS ,INTFID,MBRSEQNO,ABA_NUM ,EXT_ACNT_REF_ID,SEQNO,EXT_ACC_NUM) VALUES (?,?,?,?,?,?,?,?,?,?,?,next value for MSFTSEXTMGRI,?)";
			preStmt_ftsExtAcnt_mgrn_temp = conn.prepareStatement(createExtAccounts);

			preStmt_ftsExtAcnt_mgrn_temp.setString(1, acntInfo.getKey_client());
			preStmt_ftsExtAcnt_mgrn_temp.setString(2, acntInfo.getNick_name());
			preStmt_ftsExtAcnt_mgrn_temp.setString(3, acntInfo.getAct_type());
			preStmt_ftsExtAcnt_mgrn_temp.setString(4, "PerfEXT");
			preStmt_ftsExtAcnt_mgrn_temp.setString(5, acntInfo.getAct_hldr_name());
			preStmt_ftsExtAcnt_mgrn_temp.setString(6, acntInfo.getThrd_prty_indc());
			preStmt_ftsExtAcnt_mgrn_temp.setString(7,"Performance_Upload");
			preStmt_ftsExtAcnt_mgrn_temp.setString(8, "MSFTSEXTMGRI");
			preStmt_ftsExtAcnt_mgrn_temp.setDouble(9, new Double(4000));
			preStmt_ftsExtAcnt_mgrn_temp.setString(10, acntInfo.getAba_num());
			preStmt_ftsExtAcnt_mgrn_temp.setString(11, acntInfo.getExt_acnt_ref_id());
			preStmt_ftsExtAcnt_mgrn_temp.setString(12, ext_acnt_num);

			preStmt_ftsExtAcnt_mgrn_temp.execute();
		} 
		catch (SQLException sqle) {
			sqle.printStackTrace();
			throw sqle;
		} 
		finally 
		{
			try {
				if(preStmt_ftsExtAcnt_mgrn_temp != null)
					preStmt_ftsExtAcnt_mgrn_temp.close();
			} catch (SQLException sqle) {
				sqle.printStackTrace();
			}

			try {
				if(preStmt != null)
					preStmt.close();
			} catch (SQLException sqle) {
				sqle.printStackTrace();
			}
		}
	}

	/**
	 * Commit data...
	 * @param branchValue
	 * @throws SQLException
	 */
	public void commitDataPool(String branchValue) throws SQLException
	{
		try 
		{
			if(conn!=null) {
				conn.commit();
				System.out.println("Data Committed  for MS Branch No : "+ branchValue);
			}
		} 
		catch (SQLException sqle)
		{
			sqle.printStackTrace();
			if(conn!=null){
				conn.rollback();
				System.out.println("Data Rollback  for MS Branch No : "+ branchValue);
			}
		} 
		finally 
		{

		}
	}

	/**
	 * Commit data...
	 * @param branchValue
	 * @throws SQLException
	 */
	public void commitTxnPool(Calendar txn_date,String branch) throws SQLException
	{
		try 
		{
			if(conn!=null) {
				conn.commit();
				System.out.println("Txns Committed  for branch : "+ branch +" for date : "+ txn_date.getTime().toString());
			}
		} 
		catch (SQLException sqle)
		{
			sqle.printStackTrace();
			if(conn!=null){
				conn.rollback();
				System.out.println("Txns Rolledback  for branch : "+ branch +" for date : "+ txn_date.getTime().toString());
			}
		} 
		finally 
		{

		}
	}

	/**
	 * Commit data...
	 * @param branchValue
	 * @throws SQLException
	 */
	public void commitExternalAcntPool(String keyClient) throws SQLException
	{
		try 
		{
			if(conn!=null) {
				conn.commit();
				System.out.println("Acnt committed for client - "+keyClient);
			}
		} 
		catch (SQLException sqle)
		{
			sqle.printStackTrace();
			if(conn!=null){
				conn.rollback();
				System.out.println("Acnt rolledback for client - "+keyClient);
			}
		} 
		finally 
		{

		}
	}

	/**
	 * Get Total Transaction Count...
	 * @return
	 */
	public Double getTotal_PastTxn_Count(String txn_type) throws Exception
	{
		PreparedStatement  preStmt = null;
		ResultSet rs = null;
		Double txn_count = null;
		try  
		{
			String statement = "";
			if(txn_type!=null && txn_type.startsWith("INT")){
				statement = "SELECT count(*) FROM JAWS_MGRN_TEMP WHERE SHDLD_TRNSFR_DATE < CURRENT_DATE";
			}
			else if (txn_type!=null && txn_type.equalsIgnoreCase("ACH-OUT")){
				statement = "SELECT count(*) FROM FTSTXN_MGRN_TEMP WHERE SHDLD_TRNSFR_DATE < CURRENT_DATE AND FROM_KEY_ACT IS NOT NULL";
			}
			else if (txn_type!=null && txn_type.equalsIgnoreCase("ACH-IN")){
				statement = "SELECT count(*) FROM FTSTXN_MGRN_TEMP WHERE SHDLD_TRNSFR_DATE < CURRENT_DATE AND FROM_KEY_ACT IS NULL";
			}
			else if (txn_type!=null && txn_type.equalsIgnoreCase("CHK-LOC")){
				statement = "SELECT count(*) FROM CHK_MGRN_TEMP WHERE SHDLD_TRNSFR_DATE < CURRENT_DATE AND CHK_PCKP_OPTN = '3'";
			}
			else if (txn_type!=null && txn_type.equalsIgnoreCase("CHK-REG")){
				statement = "SELECT count(*) FROM CHK_MGRN_TEMP WHERE SHDLD_TRNSFR_DATE < CURRENT_DATE AND CHK_PCKP_OPTN = '1'";
			}
			preStmt = conn.prepareStatement(statement);
			rs = preStmt.executeQuery();
			if(rs.next()){
				txn_count = Double.parseDouble(rs.getString(1));
			}
		} 
		catch (SQLException sqle) {
			sqle.printStackTrace();
		} 
		finally 
		{
			try {
				if(preStmt != null)
					preStmt.close();
			} catch (SQLException sqle) {
				sqle.printStackTrace();
			}

			try {
				if(rs != null)
					rs.close();
			} catch (SQLException sqle) {
				sqle.printStackTrace();
			}

		}
		return txn_count;
	}

	/**
	 * Update Transaction status for past dated txns..
	 * @return
	 */
	public void updatePastDatedTxns(List<ArrayList> txnCountList,String txn_type) throws Exception
	{
		PreparedStatement  preStmt = null;
		ResultSet rs = null;
		try  
		{
			for(ArrayList txn_count : txnCountList) 
			{
				String statement = "";
				String txn_status = (String)txn_count.get(0);
				Integer txn_update_count = (Integer)txn_count.get(1);

				if(txn_type!=null && txn_type.startsWith("INT")){
					statement = "UPDATE JAWS_MGRN_TEMP SET TXN_STATUS = ? WHERE SEQNO IN (SELECT SEQNO FROM JAWS_MGRN_TEMP WHERE SHDLD_TRNSFR_DATE < CURRENT_DATE AND TXN_STATUS IS NULL ORDER BY RAND() FETCH FIRST "+txn_update_count+" ROWS ONLY)";
				}
				else if (txn_type!=null && txn_type.equalsIgnoreCase("ACH-OUT")){
					statement = "UPDATE FTSTXN_MGRN_TEMP SET TXN_STATUS = ? WHERE SEQNO IN (SELECT SEQNO FROM FTSTXN_MGRN_TEMP WHERE SHDLD_TRNSFR_DATE < CURRENT_DATE AND TXN_STATUS IS NULL AND FROM_KEY_ACT IS NOT NULL ORDER BY RAND() FETCH FIRST "+txn_update_count+" ROWS ONLY)";
				}
				else if (txn_type!=null && txn_type.equalsIgnoreCase("ACH-IN")){
					statement = "UPDATE FTSTXN_MGRN_TEMP SET TXN_STATUS = ? WHERE SEQNO IN (SELECT SEQNO FROM FTSTXN_MGRN_TEMP WHERE SHDLD_TRNSFR_DATE < CURRENT_DATE AND TXN_STATUS IS NULL AND FROM_KEY_ACT IS NULL ORDER BY RAND() FETCH FIRST "+txn_update_count+" ROWS ONLY)";
				}
				else if (txn_type!=null && txn_type.equalsIgnoreCase("CHK-LOC")){
					statement = "UPDATE CHK_MGRN_TEMP SET TXN_STATUS = ? WHERE SEQNO IN (SELECT SEQNO FROM CHK_MGRN_TEMP WHERE SHDLD_TRNSFR_DATE < CURRENT_DATE AND TXN_STATUS IS NULL AND CHK_PCKP_OPTN = '3' ORDER BY RAND() FETCH FIRST "+txn_update_count+" ROWS ONLY)";
				}
				else if (txn_type!=null && txn_type.equalsIgnoreCase("CHK-REG")){
					statement = "UPDATE CHK_MGRN_TEMP SET TXN_STATUS = ? WHERE SEQNO IN (SELECT SEQNO FROM CHK_MGRN_TEMP WHERE SHDLD_TRNSFR_DATE < CURRENT_DATE AND TXN_STATUS IS NULL AND CHK_PCKP_OPTN = '1' ORDER BY RAND() FETCH FIRST "+txn_update_count+" ROWS ONLY)";
				}

				preStmt = conn.prepareStatement(statement);
				preStmt.setString(1, txn_status);

				preStmt.executeUpdate();
			}
		} 
		catch (SQLException sqle) {
			sqle.printStackTrace();
		} 
		finally 
		{
			try {
				if(preStmt != null)
					preStmt.close();
			} catch (SQLException sqle) {
				sqle.printStackTrace();
			}

			try {
				if(rs != null)
					rs.close();
			} catch (SQLException sqle) {
				sqle.printStackTrace();
			}
		}
	}

	/**
	 * Get Total Transaction Count...
	 * @return
	 */
	public Double getTotal_PresentFutureTxn_Count(String txn_type) throws Exception
	{
		PreparedStatement  preStmt = null;
		ResultSet rs = null;
		Double txn_count = null;
		try  
		{
			String statement = "";
			if(txn_type!=null && txn_type.startsWith("INT")){
				statement = "SELECT count(*) FROM JAWS_MGRN_TEMP WHERE SHDLD_TRNSFR_DATE >= CURRENT_DATE";
			}
			else if (txn_type!=null && txn_type.equalsIgnoreCase("ACH-OUT")){
				statement = "SELECT count(*) FROM FTSTXN_MGRN_TEMP WHERE SHDLD_TRNSFR_DATE >= CURRENT_DATE AND FROM_KEY_ACT IS NOT NULL";
			}
			else if (txn_type!=null && txn_type.equalsIgnoreCase("ACH-IN")){
				statement = "SELECT count(*) FROM FTSTXN_MGRN_TEMP WHERE SHDLD_TRNSFR_DATE >= CURRENT_DATE AND FROM_KEY_ACT IS NULL";
			}
			else if (txn_type!=null && txn_type.equalsIgnoreCase("CHK-LOC")){
				statement = "SELECT count(*) FROM CHK_MGRN_TEMP WHERE SHDLD_TRNSFR_DATE >= CURRENT_DATE AND CHK_PCKP_OPTN = '3'";
			}
			else if (txn_type!=null && txn_type.equalsIgnoreCase("CHK-REG")){
				statement = "SELECT count(*) FROM CHK_MGRN_TEMP WHERE SHDLD_TRNSFR_DATE >= CURRENT_DATE AND CHK_PCKP_OPTN = '1'";
			}
			preStmt = conn.prepareStatement(statement);
			rs = preStmt.executeQuery();
			if(rs.next()){
				txn_count = Double.parseDouble(rs.getString(1));
			}
		} 
		catch (SQLException sqle) {
			sqle.printStackTrace();
		} 
		finally 
		{
			try {
				if(preStmt != null)
					preStmt.close();
			} catch (SQLException sqle) {
				sqle.printStackTrace();
			}

			try {
				if(rs != null)
					rs.close();
			} catch (SQLException sqle) {
				sqle.printStackTrace();
			}

		}
		return txn_count;
	}

	/**
	 * Update Transaction status for future dated txns..
	 * @return
	 */
	public void updatePresentFutureDatedTxns(List<ArrayList> txnCountList,String txn_type) throws Exception
	{
		PreparedStatement  preStmt = null;
		ResultSet rs = null;
		try  
		{
			for(ArrayList txn_count : txnCountList) 
			{
				String statement = "";
				String txn_status = (String)txn_count.get(0);
				Integer txn_update_count = (Integer)txn_count.get(1);

				if(txn_type!=null && txn_type.startsWith("INT")){
					if(txn_status.equalsIgnoreCase("4")) {
						statement = "UPDATE JAWS_MGRN_TEMP SET TXN_STATUS = ? WHERE SEQNO IN (SELECT SEQNO FROM JAWS_MGRN_TEMP WHERE SHDLD_TRNSFR_DATE = CURRENT_DATE AND TXN_STATUS IS NULL ORDER BY RAND() FETCH FIRST "+txn_update_count+" ROWS ONLY)";
					}
					else if(txn_status.equalsIgnoreCase("6")) {
						statement = "UPDATE JAWS_MGRN_TEMP SET TXN_STATUS = ? WHERE SEQNO IN (SELECT SEQNO FROM JAWS_MGRN_TEMP WHERE SHDLD_TRNSFR_DATE > CURRENT_DATE AND TXN_STATUS IS NULL ORDER BY RAND() FETCH FIRST "+txn_update_count+" ROWS ONLY)";
					}
					else  {
						statement = "UPDATE JAWS_MGRN_TEMP SET TXN_STATUS = ? WHERE SEQNO IN (SELECT SEQNO FROM JAWS_MGRN_TEMP WHERE SHDLD_TRNSFR_DATE >= CURRENT_DATE AND TXN_STATUS IS NULL ORDER BY RAND() FETCH FIRST "+txn_update_count+" ROWS ONLY)";
					}
				}
				else if (txn_type!=null && txn_type.equalsIgnoreCase("ACH-OUT")){
					if(txn_status.equalsIgnoreCase("4")) {
						statement = "UPDATE FTSTXN_MGRN_TEMP SET TXN_STATUS = ? WHERE SEQNO IN (SELECT SEQNO FROM FTSTXN_MGRN_TEMP WHERE SHDLD_TRNSFR_DATE = CURRENT_DATE AND TXN_STATUS IS NULL AND FROM_KEY_ACT IS NOT NULL ORDER BY RAND() FETCH FIRST "+txn_update_count+" ROWS ONLY)";
					}
					else if(txn_status.equalsIgnoreCase("6")) {
						statement = "UPDATE FTSTXN_MGRN_TEMP SET TXN_STATUS = ? WHERE SEQNO IN (SELECT SEQNO FROM FTSTXN_MGRN_TEMP WHERE SHDLD_TRNSFR_DATE > CURRENT_DATE AND TXN_STATUS IS NULL AND FROM_KEY_ACT IS NOT NULL ORDER BY RAND() FETCH FIRST "+txn_update_count+" ROWS ONLY)";
					}
					else {
						statement = "UPDATE FTSTXN_MGRN_TEMP SET TXN_STATUS = ? WHERE SEQNO IN (SELECT SEQNO FROM FTSTXN_MGRN_TEMP WHERE SHDLD_TRNSFR_DATE >= CURRENT_DATE AND TXN_STATUS IS NULL AND FROM_KEY_ACT IS NOT NULL ORDER BY RAND() FETCH FIRST "+txn_update_count+" ROWS ONLY)";
					}
				}
				else if (txn_type!=null && txn_type.equalsIgnoreCase("ACH-IN")){
					if(txn_status.equalsIgnoreCase("4")) {
						statement = "UPDATE FTSTXN_MGRN_TEMP SET TXN_STATUS = ? WHERE SEQNO IN (SELECT SEQNO FROM FTSTXN_MGRN_TEMP WHERE SHDLD_TRNSFR_DATE = CURRENT_DATE AND TXN_STATUS IS NULL AND FROM_KEY_ACT IS NULL ORDER BY RAND() FETCH FIRST "+txn_update_count+" ROWS ONLY)";
					}
					else if(txn_status.equalsIgnoreCase("6")) {
						statement = "UPDATE FTSTXN_MGRN_TEMP SET TXN_STATUS = ? WHERE SEQNO IN (SELECT SEQNO FROM FTSTXN_MGRN_TEMP WHERE SHDLD_TRNSFR_DATE > CURRENT_DATE AND TXN_STATUS IS NULL AND FROM_KEY_ACT IS NULL ORDER BY RAND() FETCH FIRST "+txn_update_count+" ROWS ONLY)";
					}
					else {
						statement = "UPDATE FTSTXN_MGRN_TEMP SET TXN_STATUS = ? WHERE SEQNO IN (SELECT SEQNO FROM FTSTXN_MGRN_TEMP WHERE SHDLD_TRNSFR_DATE >= CURRENT_DATE AND TXN_STATUS IS NULL AND FROM_KEY_ACT IS NULL ORDER BY RAND() FETCH FIRST "+txn_update_count+" ROWS ONLY)";
					}
				}
				else if (txn_type!=null && txn_type.equalsIgnoreCase("CHK-LOC")){
					if(txn_status.equalsIgnoreCase("103")) {
						statement = "UPDATE CHK_MGRN_TEMP SET TXN_STATUS = ? WHERE SEQNO IN (SELECT SEQNO FROM CHK_MGRN_TEMP WHERE SHDLD_TRNSFR_DATE = CURRENT_DATE AND TXN_STATUS IS NULL AND CHK_PCKP_OPTN = '3' ORDER BY RAND() FETCH FIRST "+txn_update_count+" ROWS ONLY)";
					}
					else if(txn_status.equalsIgnoreCase("6")) {
						statement = "UPDATE CHK_MGRN_TEMP SET TXN_STATUS = ? WHERE SEQNO IN (SELECT SEQNO FROM CHK_MGRN_TEMP WHERE SHDLD_TRNSFR_DATE > CURRENT_DATE AND TXN_STATUS IS NULL AND CHK_PCKP_OPTN = '3' ORDER BY RAND() FETCH FIRST "+txn_update_count+" ROWS ONLY)";
					}
					else {
						statement = "UPDATE CHK_MGRN_TEMP SET TXN_STATUS = ? WHERE SEQNO IN (SELECT SEQNO FROM CHK_MGRN_TEMP WHERE SHDLD_TRNSFR_DATE >= CURRENT_DATE AND TXN_STATUS IS NULL AND CHK_PCKP_OPTN = '3' ORDER BY RAND() FETCH FIRST "+txn_update_count+" ROWS ONLY)";
					}
				}
				else if (txn_type!=null && txn_type.equalsIgnoreCase("CHK-REG")){
					if(txn_status.equalsIgnoreCase("4")) {
						statement = "UPDATE CHK_MGRN_TEMP SET TXN_STATUS = ? WHERE SEQNO IN (SELECT SEQNO FROM CHK_MGRN_TEMP WHERE SHDLD_TRNSFR_DATE = CURRENT_DATE AND TXN_STATUS IS NULL AND CHK_PCKP_OPTN = '1' ORDER BY RAND() FETCH FIRST "+txn_update_count+" ROWS ONLY)";
					}
					else if(txn_status.equalsIgnoreCase("6")) {
						statement = "UPDATE CHK_MGRN_TEMP SET TXN_STATUS = ? WHERE SEQNO IN (SELECT SEQNO FROM CHK_MGRN_TEMP WHERE SHDLD_TRNSFR_DATE > CURRENT_DATE AND TXN_STATUS IS NULL AND CHK_PCKP_OPTN = '1' ORDER BY RAND() FETCH FIRST "+txn_update_count+" ROWS ONLY)";
					}
					else {
						statement = "UPDATE CHK_MGRN_TEMP SET TXN_STATUS = ? WHERE SEQNO IN (SELECT SEQNO FROM CHK_MGRN_TEMP WHERE SHDLD_TRNSFR_DATE >= CURRENT_DATE AND TXN_STATUS IS NULL AND CHK_PCKP_OPTN = '1' ORDER BY RAND() FETCH FIRST "+txn_update_count+" ROWS ONLY)";
					}
				}

				preStmt = conn.prepareStatement(statement);
				preStmt.setString(1, txn_status);
				preStmt.executeUpdate();
			}
		} 
		catch (SQLException sqle) {
			sqle.printStackTrace();
		} 
		finally 
		{
			try {
				if(preStmt != null)
					preStmt.close();
			} catch (SQLException sqle) {
				sqle.printStackTrace();
			}

			try {
				if(rs != null)
					rs.close();
			} catch (SQLException sqle) {
				sqle.printStackTrace();
			}
		}
	}
}

