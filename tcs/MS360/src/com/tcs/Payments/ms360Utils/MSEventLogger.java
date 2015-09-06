package com.tcs.Payments.ms360Utils;

import javax.servlet.http.HttpServletRequest;
import com.tcs.ebw.serverside.jaas.principal.UserPrincipal;

/**
 * @author : 224703
 * **************   Revision History ************************
 * Modified-By |  Modified-Date |  Project-Phase  | Changes
 *  224703         15-05-2011        P3               Performance Logging
 * **********************************************************
 */
public class MSEventLogger {

	private String userName = null;
	private String ipAddress = null;
	private String threadId = null;
	private String httpSessionId = null;
	private long startTime = 0;
	private long endTime = 0 ;
	
	/**
	 * Performance Logger at Servlet level...
	 * @param request
	 * @param objUserPrincipal
	 */
	public static void logServletEntry(HttpServletRequest request,UserPrincipal objUserPrincipal) {
         
	}

	/**
	 * Performance Logger at EJBLookUp level...
	 * @param objUserPrincipal
	 */
	public static void logEJBLookUpEntry(UserPrincipal objUserPrincipal) {

	}

	/**
	 * Performance Logger at DBLookUp Level...
	 * @param objUserPrincipal
	 */
	public static void logDBLookUpEntry(UserPrincipal objUserPrincipal) {

	}

	/**
	 * Performance Logger at EJB Bean Level...
	 * @param objUserPrincipal
	 */
	public static void logEJBBeanEntry(UserPrincipal objUserPrincipal) {

	}

	/**
	 * Performance Logger at BusinesMethod Level...
	 * @param objUserPrincipal
	 */
	public static void logBusinessMethodEntry(UserPrincipal objUserPrincipal) {

	}
}
