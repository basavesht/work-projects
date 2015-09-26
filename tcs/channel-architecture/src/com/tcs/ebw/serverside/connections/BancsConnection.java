package com.tcs.ebw.serverside.connections;

import java.util.LinkedHashMap;

//import com.tcs.Bancs.HostConn.ConnectionPool;
import com.tcs.ebw.serverside.factory.IEBWConnection;

/**
 * @author 231259
 * @year 2008
 */
public class BancsConnection implements IEBWConnection {

	public Object connect(LinkedHashMap systemInfo) throws Exception {		
//		EBWLogger.logDebug("************ Socket Connection ********","Socket CONNECTION - START");
//		
//		String socketServer = (String)systemInfo.get("IP_ADDRESS");
//		int socketPort = new Integer((String)systemInfo.get("PORT_NO")).intValue();		
////		Socket commSocket = new Socket(socketServer, socketPort);	
//		
//		EBWLogger.logDebug("************ Socket Info: ********",commSocket.toString());
//		EBWLogger.logDebug("************ Socket Connection ********","Socket CONNECTION - END");
//		return commSocket;
		return null;
	}

	public Object disconnect() throws Exception {		
	//	ConnectionPool connPool = (ConnectionPool) (System.getProperties().get("host.pool"));
	//	connPool.drainConnection(1);		
		return null;		
	}

}
