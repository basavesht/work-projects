/*
 * Created on Oct 20, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.tcs.ebw.util.connect;

import com.tcs.ebw.util.connect.db.*;
/**
 * @author 152699
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class ConnectionFactory {
	public final static String DB_TYPE = "DB";
	public final static String DB_EXCEL = "EXCEL";
	
	public static ConnectionImpl createConnection(String conType, String servType)  throws Exception {
		ConnectionImpl con = null;
		
		if (conType.equals(DB_TYPE)) {
			if (servType.equals(DB_EXCEL)) {
				con = new DBConnection();
			}
		}
		return con;
	}
}
