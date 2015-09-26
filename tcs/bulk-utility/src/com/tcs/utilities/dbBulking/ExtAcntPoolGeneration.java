package com.tcs.utilities.dbBulking;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

/**
 * @author 224703
 *
 */
public class ExtAcntPoolGeneration extends DBBulking
{

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws Exception 
	{
		try {
			System.out.println("External Account Pool Creation");
			ExtAcntPoolGeneration acntPool = new ExtAcntPoolGeneration();
			acntPool.createAccountPool();
		} 
		catch (Exception e) {
			throw e;
		}
	}

	/**
	 * 
	 * @throws Exception
	 */
	public void createAccountPool() throws Exception
	{
		try
		{
			//Get the connection...
			DBUtils dbutils = new DBUtils();
			conn = dbutils.getConnection();

			List<String> uniquekeyClientIds = getUniqueKeyClientList();
			if(!uniquekeyClientIds.isEmpty()) 
			{
				for(String keyclient:uniquekeyClientIds) 
				{
					List<HashMap> uniqueACHAccounts = getACHAccounts_per_client(keyclient);
					if(!uniqueACHAccounts.isEmpty())
					{
						for(HashMap acntDtls:uniqueACHAccounts) 
						{
							String ext_acnt_ref_id = ((String)acntDtls.get("EXTERNAL_ACNT_REF_ID"));

							AccountDetails acntInfo = new AccountDetails();
							acntInfo.setAba_num("021000322");
							acntInfo.setExt_acnt_ref_id(ext_acnt_ref_id);
							acntInfo.setKey_client(keyclient);
							acntInfo.setAct_hldr_name("Owner-"+keyclient.substring((keyclient.length()-4), keyclient.length()));
							acntInfo.setAct_type("S");
							acntInfo.setNick_name("ExtAcntNick-"+keyclient.substring((keyclient.length()-4), keyclient.length()));
							acntInfo.setThrd_prty_indc("N");

							bulkFTSEXT_Temp(acntInfo);
						}
					}
					commitExternalAcntPool(keyclient);
				}
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
}
