/*
 * Created on Jan 31, 2006
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.tcs.ebw.serverside.query;

import com.tcs.ebw.transferobject.SellOrderTO;
/**
 * @author ashokvijayakumar
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class SQLJExecutor {

	
	public  void creatSQLJFile(String fileName,String query){
		SellOrderTO sellOrderTO = new SellOrderTO();
		sellOrderTO.setSellOrderId("S001");
		QueryExecutor queryExecutor = new QueryExecutor();
		//String query = queryExecutor.getQueryString("getSellOrder.Query", sellOrderTO);
		
	}
}
