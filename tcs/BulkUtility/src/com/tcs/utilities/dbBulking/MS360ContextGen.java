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
public class MS360ContextGen extends DBBulking 
{
	/**
	 * @param args
	 */
	public static void main(String[] args) 
	{
		System.out.println("MS360 Context Generation");
		String relativePath = readRelativePath("Enter the relative path :");

		MS360ContextGen ms360ContextGen = new MS360ContextGen();
		ms360ContextGen.generateMS360Context(relativePath);
	}

	public void generateMS360Context(String relativePath)
	{
		try 
		{
			//Get the connection...
			DBUtils dbutils = new DBUtils();
			conn = dbutils.getConnection();

			List<String> uniqueRacfIds = getUniqueRACFIDList();
			if(!uniqueRacfIds.isEmpty()) 
			{
				for(String racfId:uniqueRacfIds) 
				{
					List<HashMap> linkedAcnts = getLinkedAccounts_per_RACF_ID(racfId);
					if(!linkedAcnts.isEmpty())
					{
						File  file=new File(relativePath+"/"+racfId+".xml");
						OutputStream  fout = new FileOutputStream(file);
						Writer out = new OutputStreamWriter(fout, "UTF-8");  

						out.write("<?xml version=\"1.0\" encoding=\"utf-8\"?>\r\n");  
						out.write("<MMContext>\r\n");
						out.write("<UserInfo>\r\n");
						out.write("<RACFId>"+racfId+"</RACFId>\r\n");
						out.write("<FirstName>"+"MS360-"+racfId+"</FirstName>\r\n");
						out.write("<MiddleInitial></MiddleInitial>\r\n");						      
						out.write("<LastName></LastName>\r\n");						      
						out.write("</UserInfo>\r\n"); 						      
						out.write("<Accounts>\r\n"); 	

						for(HashMap acntDtls:linkedAcnts) 
						{
							String acnt_number = ((String)acntDtls.get("ACCOUNT_NUMBER"));
							String office = ((String)acntDtls.get("ACCOUNT_NUMBER")).substring(0, 3);
							String acnt_base = ((String)acntDtls.get("ACCOUNT_NUMBER")).substring(3, 9);
							String fa = ((String)acntDtls.get("ACCOUNT_NUMBER")).substring(9, 12);
							String key_acnt = ((String)acntDtls.get("KEY_ACCOUNT_NUMBER"));
							String key_client = ((String)acntDtls.get("KEYCLIENT_ID"));

							out.write("    <Account>\r\n"); 
							out.write("        <AccountName>"+"Acnt-"+acnt_number+"</AccountName>\r\n");
							out.write("        <AccountNo>"+acnt_base+"</AccountNo>\r\n");
							out.write("        <OfficeNo>"+office +"</OfficeNo>\r\n"); 
							out.write("        <FANo>"+fa +"</FANo>\r\n");
							out.write("        <KeyAccount>"+key_acnt +"</KeyAccount>\r\n");
							out.write("        <PrimaryAccountFlag>false</PrimaryAccountFlag>\r\n"); 
							out.write("        <AnchorFlag>false</AnchorFlag>\r\n");
							out.write("        <AuthorizedEntities>\r\n");
							out.write("        <AuthorizedEntity>\r\n");
							out.write("            <Name>"+"Acnt-"+acnt_number+"</Name>\r\n"); 
							out.write("            <HashedUniqueID>"+key_client+"</HashedUniqueID>\r\n"); 						      
							out.write("            <MaskedUniqueID>"+key_client+"</MaskedUniqueID>\r\n"); 						      
							out.write("        </AuthorizedEntity>\r\n"); 
							out.write("        </AuthorizedEntities>\r\n");
							out.write("    </Account>\r\n");

						}
						out.write("</Accounts>\r\n"); 	
						out.write("</MMContext>\r\n");
						out.close();
					}
				}
				System.out.println("Context generated successfully...");
			}
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
		finally{
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
