package com.tcs.utilities.dbBulking;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

/**
 * @author 224703
 *
 */
public class ClientServContextGen extends DBBulking 
{
	/**
	 * @param args
	 */
	public static void main(String[] args) 
	{
		System.out.println("ClientServ Context Generation");
		String relativePath = readRelativePath("Enter the relative path :");

		ClientServContextGen csContextGen = new ClientServContextGen();
		csContextGen.generateClientServContext(relativePath);
	}

	public void generateClientServContext(String relativePath)
	{
		try 
		{
			//Get the connection...
			DBUtils dbutils = new DBUtils();
			conn = dbutils.getConnection();

			List<HashMap> uniqueUUIDs = getUniqueUUIDList();
			if(!uniqueUUIDs.isEmpty()) 
			{
				for(HashMap clients:uniqueUUIDs) 
				{
					String uuid = ((String)clients.get("UUID"));
					String key_client = ((String)clients.get("KEYCLIENT_ID"));

					List<HashMap> linkedAcnts = getAccounts_per_client(uuid);
					if(!linkedAcnts.isEmpty())
					{
						File  file=new File(relativePath+"/"+uuid+".xml");
						OutputStream  fout = new FileOutputStream(file);
						Writer out = new OutputStreamWriter(fout, "UTF-8");  

						out.write("<?xml version=\"1.0\" encoding=\"utf-8\"?>\r\n");  
						out.write("<MMContext>\r\n");
						out.write("<LoginId>morgan</LoginId>\r\n");
						out.write("<UUID>"+uuid+"</UUID>\r\n");
						out.write("<FirstName>"+"CS-"+uuid+"</FirstName> \r\n");
						out.write("<MiddleInitial/>\r\n");						      
						out.write("<LastName></LastName>\r\n");						      
						out.write("<FAIndicator>false</FAIndicator>\r\n"); 						      
						out.write("<DevicePrint>DevicePrint</DevicePrint>\r\n"); 	
						out.write("<DeviceCookie>DeviceCookie</DeviceCookie>\r\n"); 						      
						out.write("<DeviceFSO>DeviceFSO</DeviceFSO>\r\n");			      
						out.write("<CSSessionID>CSSessionId</CSSessionID>\r\n"); 						      
						out.write("<ClientIPAddress>172.19.108.46</ClientIPAddress>\r\n"); 			      
						out.write("<LastAccountOpenDate>12/23/2009</LastAccountOpenDate>\r\n"); 						      
						out.write("<LastOnlineServicePasswordChangeDate>12/23/2009</LastOnlineServicePasswordChangeDate>\r\n");
						out.write("<OnlineServiceEnrollDate>12/23/2009</OnlineServiceEnrollDate>\r\n"); 						      
						out.write("<LastAddressChangeDate>12/23/2009</LastAddressChangeDate>\r\n"); 			
						out.write("<LastPhoneChangeDate>12/23/2009</LastPhoneChangeDate>\r\n"); 						      
						out.write("<LastEmailChangeDate>12/23/2009</LastEmailChangeDate>\r\n"); 
						out.write("<ClientIdentifier>"+key_client+"</ClientIdentifier>\r\n");
						out.write("<MaskedClientIdentifier>"+key_client+"</MaskedClientIdentifier>\r\n");
						out.write("<Accounts>\r\n");	

						for(HashMap acntDtls:linkedAcnts) 
						{
							String acnt_number = ((String)acntDtls.get("ACCOUNT_NUMBER"));
							String office = ((String)acntDtls.get("ACCOUNT_NUMBER")).substring(0, 3);
							String acnt_base = ((String)acntDtls.get("ACCOUNT_NUMBER")).substring(3, 9);
							String fa = ((String)acntDtls.get("ACCOUNT_NUMBER")).substring(9, 12);
							String key_acnt = ((String)acntDtls.get("KEY_ACCOUNT_NUMBER"));

							out.write("<Account>\r\n"); 
							out.write("    <AccountNo>"+acnt_base+"</AccountNo>\r\n");
							out.write("    <OfficeNo>"+office +"</OfficeNo>\r\n"); 
							out.write("    <FANo>"+fa +"</FANo>\r\n");
							out.write("    <KeyAccount>"+key_acnt +"</KeyAccount>\r\n");
							out.write("    <NickName>"+"Acnt-"+acnt_number+"</NickName>\r\n"); 
							out.write("    <FriendlyName>"+"Acnt-"+acnt_number+"</FriendlyName>\r\n");
							out.write("    <ViewTransactionFlag>false</ViewTransactionFlag>\r\n");
							out.write("    <AccountStatus>0</AccountStatus>\r\n");
							out.write("    <AccountClass>accountclass</AccountClass>\r\n"); 
							out.write("    <NovusSubProduct>subproduct</NovusSubProduct>\r\n"); 						      
							out.write("    <DivPay>3</DivPay>\r\n"); 						      
							out.write("    <ClientCategory>gold</ClientCategory>\r\n"); 
							out.write("    <ChoiceFundCode>somecode</ChoiceFundCode>\r\n"); 						      
							out.write("    <IRACode>iracode</IRACode>\r\n"); 
							out.write("    <TradeControl>tradeCon</TradeControl>\r\n"); 
							out.write("    <AccountCategory>acctcode</AccountCategory>\r\n"); 						      
							out.write("    <CollateralAcctInd>collInd</CollateralAcctInd>\r\n");
							out.write("</Account>\r\n");
						}
						out.write("</Accounts>\r\n"); 	
						out.write("</MMContext>\r\n");
						out.close();
					}
				}
				System.out.println("Context generated successfully...");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally
		{
			try {
				if(conn != null) 
					conn.close();
			} catch (SQLException sqle) {
				sqle.printStackTrace();
			}
		}
	}

	public static String readRelativePath (String prompt)
	{
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
