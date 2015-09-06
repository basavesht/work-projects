/*
 * Created on Oct 20, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.tcs.ebw.util.connect;

import java.util.Map;

/**
 * @author 152699
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public interface ConnectionImpl {
	public void connect(String driver, String url, String userName, String pwd) throws Exception;
	public Map execute (Map mapObj) throws Exception;
	public Map executeQuery (Map mapObj) throws Exception;
	public void close() throws Exception;
}
