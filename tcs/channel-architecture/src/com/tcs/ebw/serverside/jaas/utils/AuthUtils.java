/**
 * Created on Dec 21, 2005 
 * @author 152820
 *
 * This class is used as a utility which will check for permissions granted
 * on a Subject. 
 */
package com.tcs.ebw.serverside.jaas.utils;

	import java.security.*;
	import javax.security.auth.*;
	/**
	 * This class is used for checking the what permission the current login user has according to the
	 * subject.
	 * 
	 *
	 */
	
	public class AuthUtils {
	
		/**
		 * 
		 * @param subj - Subject which has to be checked for Permissions.
		 * @param p    - Permission object to be varified against.
		 * @return     - true if subject has the given permission or else 
		 * 				 return false.
		 */
	    static public boolean permitted(Subject subj, final Permission p) {
	        final SecurityManager sm;
	        if (System.getSecurityManager() == null) {
	            sm = new SecurityManager();
	            System.out.println("Creating new Security Manager");
	        } else {
	            System.out.println("Using system security Manager");
	            sm = System.getSecurityManager();
	        }
	        try {
	            Subject.doAsPrivileged(subj, new PrivilegedExceptionAction() {
	                public Object run() { 
	                    	
	                   sm.checkPermission(p);
	                    System.out.println("Permission checking over for "+p.getName());
	                    return null;
	                    //the 'null' tells the SecurityManager to consider this resource access
	            		//in an isolated context, ignoring the permissions of code currently
	            		//on the execution stack.
	            		//For further information, see chapter 5 of Java Security
	                }
	                },null);
	            return true;
	        } catch (AccessControlException ace) {
	        	ace.printStackTrace();
	            return false;
	        } catch (PrivilegedActionException pae) {
	        	pae.printStackTrace();
	            return false;
	        }
	    }
	}

