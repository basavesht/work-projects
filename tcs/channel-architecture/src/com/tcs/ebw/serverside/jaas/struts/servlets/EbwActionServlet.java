//Decompiled by DJ v3.5.5.77 Copyright 2003 Atanas Neshkov  Date: 12/20/2006 11:36:09 AM
//Home Page : http://members.fortunecity.com/neshkov/dj.html  - Check often for new version!
//Decompiler options: packimports(3) deadcode 
//Source File Name:   EbwActionServlet.java

package com.tcs.ebw.serverside.jaas.struts.servlets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.MissingResourceException;
import java.util.NoSuchElementException;
import java.util.ResourceBundle;
import java.util.Set;

import javax.security.auth.Subject;
import javax.security.auth.login.LoginContext;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionServlet;

//import com.tcs.Bancs.HostConn.StatusCheck;
import com.tcs.ebw.common.util.EBWLogger;
import com.tcs.ebw.common.util.PropertyFileReader;
import com.tcs.ebw.exception.EbwException;
import com.tcs.ebw.serverside.factory.EBWServiceFactory;
import com.tcs.ebw.serverside.factory.IEBWService;
import com.tcs.ebw.serverside.jaas.auth.Auth;
import com.tcs.ebw.serverside.jaas.principal.UserPrincipal;
/**
 * 
 * This is an ActionServlet class which starts the Authorization/Authentication process for the user logging in.
 *
 */

public class EbwActionServlet extends ActionServlet
{

	private Auth fa;
	private String result;
	/**
	 * A boolean variable that signifies whether SingleSignOn is activated or not.
	 */
	private boolean ssoLogin; 
	/**
	 * a LoginContext variable used for Login.
	 */
	private LoginContext lc=null;
	private static boolean hostStatusReqd = false;
//	private StatusCheck statusCheck = null;

	public EbwActionServlet()
	{

	}

	public void init() throws ServletException {		
		super.init();
		try {
			hostStatusReqd = new Boolean(PropertyFileReader.getProperty("HostConnStatusCheck")).booleanValue();
		} catch(Exception e){
			//EBWLogger.logDebug(this,"WARNING: No configuration found for key -'hostStatusReqd'. Continuing with default");
			hostStatusReqd = false;
		}
		//EBWLogger.logDebug(this,"EBW Action servlet: init() hostStatusReqd-"+hostStatusReqd);

		if(hostStatusReqd) {
			//EBWLogger.logDebug(this,"EBW Action servlet: init() method is called");
			//statusCheck = new StatusCheck();
			//statusCheck.startMe();
			//EBWLogger.logDebug(this, "Continuing with EBW Actions servlet");
		}
	}

	public void destroy() {		
		try {
			hostStatusReqd = new Boolean(PropertyFileReader.getProperty("HostConnStatusCheck")).booleanValue();
		} catch(Exception e){
			//EBWLogger.logDebug(this,"WARNING: No configuration found for key -'hostStatusReqd'. Continuing with default");
			hostStatusReqd = false;
		}
		//EBWLogger.logDebug(this,"EBW Action servlet: destroy() hostStatusReqd-"+hostStatusReqd);

		if(hostStatusReqd) {
			try {
				//EBWLogger.logDebug(this,"EBW Action servlet: destroy() method is called");
				//statusCheck.stopMe();
			} catch (Exception e) {
				//EBWLogger.logDebug(this,"Exception in EBW Action servlet: destroy() method");
				e.printStackTrace();
			}
		}		
		super.destroy();		
	}
	/**
	 * This method processes the HTTP requests and responses.
	 * @throws ServletException
	 * @throws IOException
	 */
	protected void process(HttpServletRequest request, HttpServletResponse response)
	throws  ServletException,IOException
	{
		//To remove cache
		System.out.println("inside EBW action servlet...");
		response.setDateHeader ("Expires", 0);
		response.setHeader("Pragma", "no-cache"); 
		response.setHeader("Cache-Control", "no-store");
		response.setDateHeader("max-age", 0); 
		response.setDateHeader("Expires", 0); 

		/*EBWLogger.logDebug(this,"Header username is :"+request.getHeader("username"));
     EBWLogger.logDebug(this,"Request username is :"+request.getAttribute("username"));
     EBWLogger.logDebug(this,"Request getParameter username is :"+request.getParameter("username"));*/

		String loginPage = request.getContextPath() + "/LoginNew.do";
		String logoutPage = request.getContextPath() + "/logout.do";
		String errorPage = request.getContextPath() + "/Error.do";
		String sessionErrorPage = request.getContextPath() + "/SessionError.do";
		//EBWLogger.logDebug(this,"Login page is " + loginPage);
		String pageReq = request.getRequestURI();
		//EBWLogger.logDebug(this,"Page request is 1" + pageReq);
		//java.security.Permission perm = PermissionFactory.getInstance().getPermission(pageReq);
		//EBWLogger.logDebug(this,"Got Permission " + perm);

		Subject subject = (Subject)request.getSession().getAttribute("subject");
		//EBWLogger.logDebug(this,"Subject is " + subject + " session id  " + request.getParameter("jsessionid"));
		//EBWLogger.logDebug(this,"session id  " + request.getSession().getId());


		if( (!request.isRequestedSessionIdValid() || subject == null) && !(request.getRequestURI().indexOf("Login")>-1) && !request.getRequestURI().equals(errorPage) && request.getRequestURI().indexOf("CAAction.do") <= -1 && request.getRequestURI().indexOf("ExecLoginNew.do") <= -1 && request.getRequestURI().indexOf("LoginNew.jsp") <= -1 && !request.getRequestURI().equals(sessionErrorPage))
		{
			//EBWLogger.logDebug(this,"First condition");
			request.getRequestDispatcher("/SessionError.do").forward(request, response);
		} else if(subject == null && (request.getRequestURI().indexOf("CAAction.do") > -1 || request.getRequestURI().indexOf("ExecLoginNew.do") > -1 || request.getRequestURI().indexOf("LoginNew.jsp") > -1))
		{
			//EBWLogger.logDebug(this,"Second condition..");
			//------------------Code for Creating Non-Persistent Cookie-------------------
			/*EBWLogger.logDebug(this,"Second condition..Request getParameter username is :"+request.getParameter("username"));
         EBWLogger.logDebug(this, "Second condition..Logout request in Servlet is logout:"+request.getParameter("logout"));
         EBWLogger.logDebug(this, "Second condition..Logout request in Servlet is imrssessionid:"+request.getParameter("imrssessionid"));
         EBWLogger.logDebug(this, "Second condition..Logout request in Servlet is imrsuserid:"+request.getParameter("imrsuserid"));*/
			request.getSession().setAttribute("logout", request.getParameter("logout"));
			request.getSession().setAttribute("imrssessionid", request.getParameter("imrssessionid"));
			request.getSession().setAttribute("imrsuserid", request.getParameter("imrsuserid"));
			try{
				if (PropertyFileReader.getProperty("CreateCookie").equalsIgnoreCase("true")){
					if (PropertyFileReader.getProperty("SSOLOGIN").equalsIgnoreCase("true")){

						cookieOperations(request, "create", response);
					}
					try{
						if(request.getParameter("logout").equalsIgnoreCase("true")  )
							cookieOperations(request, "delete", response);
					}catch(NullPointerException npe){
						//EBWLogger.logDebug(this,"logout not true");
					}


				}
			}catch(MissingResourceException mre){
				//EBWLogger.logDebug(this, "Into MissingResourceException:Key 'CreateCookie' not set. Continuing...");
			}catch(Exception e){
				// EBWLogger.logDebug(this, "Into Exception:Key 'CreateCookie' not set. Continuing...");


			}
			//------------------Cookie code ends------------------------------------------
			super.process(request, response);
			/*if(request.getParameter("username")!=null && request.getAttribute("username")!=null && request.getAttribute("username").equals(request.getParameter("username"))){
         //  		loadPrincipalInfo(request);
         			super.process(request, response);
         }else
                request.getRequestDispatcher("/Error.do").forward(request, response);
			 */
		} else {
			//EBWLogger.logDebug(this,"Subject is not null, value : " +subject );
			/*if(!AuthUtils.permitted(subject, perm))
         {
             EBWLogger.logDebug(this,"Not permitted..redirecting to error page..");
             request.setAttribute("error", "Access Denied...");
         } else
         {*/
			Subject subject1 = (Subject)request.getSession().getAttribute("subject");
			Set sets1 = subject1.getPrincipals();
			Iterator it1 = sets1.iterator();
			UserPrincipal userPrincipal1 = (UserPrincipal)it1.next();



			boolean fapAccessFlag = false;
			try
			{
				//if(Boolean.valueOf(PropertyFileReader.getProperty("getMenuFapInfo")))
				if(Boolean.valueOf(PropertyFileReader.getProperty("isServerLevelFabRequired")))
				{
					if(request.getRequestURI().indexOf("CAAction.do") == -1 && request.getRequestURI().indexOf("logout") == -1 && request.getRequestURI().indexOf("MainPage.do") == -1 && request.getRequestURI().indexOf("SessionError") == -1 && request.getRequestURI().indexOf("AccessError") == -1)
					{
						fapAccessFlag = checkUserFAPAccess(request, userPrincipal1);
					}
				}
			}
			catch(Exception e)
			{
				e.printStackTrace();
				EBWLogger.logDebug(this, "caught an exception while performing user fap access operations..");
			}



			/*  boolean fapAccessFlag = true;
         if(request.getRequestURI().indexOf("CAAction.do") == -1 && request.getRequestURI().indexOf("Logout") == -1 && request.getRequestURI().indexOf("MainPage.do") == -1 && request.getRequestURI().indexOf("SessionError") == -1 && request.getRequestURI().indexOf("AccessError") == -1)
         {
             fapAccessFlag = checkUserFAPAccess(request, userPrincipal1);
         }*/
			if(!fapAccessFlag)
			{
				request.getSession().setAttribute("url1",request.getRequestURI());
				response.sendRedirect((new StringBuilder(String.valueOf(request.getContextPath()))).append("/payments/AccessError.jsp").toString());
				return;
			}
			else
			{
				try{
					if (PropertyFileReader.getProperty("CreateCookie").equalsIgnoreCase("true")){
						if (PropertyFileReader.getProperty("SSOLOGIN").equalsIgnoreCase("true")){
							try{
								if(request.getParameter("logout").equalsIgnoreCase("true")  )
								{ 
									cookieOperations(request, "delete", response);
									//EBWLogger.logDebug(this,"Logging out through external log off link");
								}
							}catch(NullPointerException npe){
								//EBWLogger.logDebug(this,"logout not true");
							}
							try{
								if(request.getParameter("logoffaction").equalsIgnoreCase("true")  )
								{
									//EBWLogger.logDebug(this,"Logging out through CARTS log off link");
									cookieOperations(request, "delete", response);
								}
							}catch(NullPointerException npe){
								//EBWLogger.logDebug(this,"logoffaction is not true");
							}

						}
					}
				}catch(MissingResourceException mre){
					//EBWLogger.logDebug(this, "Into MissingResourceException:Key 'CreateCookie' not set. Continuing...");
				}catch(Exception e){
					// EBWLogger.logDebug(this, "Into Exception:Key 'CreateCookie' not set. Continuing...");


				}
				//EBWLogger.logDebug(this,"Authorization granted1...");
				String userid = request.getParameter("username");
				EBWLogger.logDebug(this,"Userid is ..." + userid);
				String sessionid = request.getSession().getId();
				//EBWLogger.logDebug(this,"sessionid - " + sessionid);
				request.getSession().setAttribute("userid", userid);
				try{

					//UserPrincipal userPrincipal = setPrincipal(request,(UserPrincipal)request.getUserPrincipal());

					// EBWLogger.logDebug(this,"subject is :"+subject);
					// UserPrincipal userPrincipal =(UserPrincipal)request.getUserPrincipal();

					Set sets =subject.getPrincipals();				 
					UserPrincipal userPrincipal = null;
					Iterator it = null ;

					// To verify the principle details            	 
					synchronized(sets){
						it= sets.iterator();
					}

					if (!it.hasNext()){
						System.out.println("User Prinicipal Iterator has value ? : "  +it.hasNext());            	 
						// Exception excep= new Exception("NoSuchElementException");
						//throw new EbwException(this,excep);
					}else{
						userPrincipal = (UserPrincipal)it.next();
						userPrincipal.setSessionId(sessionid);
						userPrincipal.setApplContext(request.getContextPath().substring(1));         		 
						subject.getPrincipals().clear();
						subject.getPrincipals().add(userPrincipal);
						request.getSession().setAttribute("subject",subject);
						request.getSession().setAttribute(Auth.SUBJECT_SESSION_KEY, subject);
						//EBWLogger.logDebug(this,"UserPrincipal Sessionid is :"+((UserPrincipal)request.getUserPrincipal()).getSessionId());	               	 
					}
				}catch(Exception e){
					e.printStackTrace();
				}
				super.process(request, response);
				//}
			}
		}
	}

	private void cookieOperations(HttpServletRequest request,String mode,HttpServletResponse response ){
//		Cookie t[]=request.getCookies();
//		System.out.println("----------------------------------------");
//		for(int i =0;i<t.length;i++){
//		System.out.println(t[i].getName());}
//		System.out.println("----------------------------------------");
		if (mode.equalsIgnoreCase("create")){

			//EBWLogger.logDebug(this, "Creating Cookie");
			Cookie c=new Cookie("CARTS~"+request.getParameter("imrsuserid"),request.getParameter("imrssessionid"));
			c.setMaxAge(365*24*60*60);
			response.addCookie(c);
		}
		else if (mode.equalsIgnoreCase("delete")){
			//EBWLogger.logDebug(this, "Deleting Cookie.....");
			Cookie c=new Cookie("CARTS~"+request.getParameter("imrsuserid"),request.getParameter("imrssessionid"));
			c.setMaxAge(0);
			response.addCookie(c);
		}

//		t=request.getCookies();
//		System.out.println("----------------------------------------");
//		for(int i =0;i<t.length;i++){
//		System.out.println(t[i].getName());}
//		System.out.println("----------------------------------------");

	}

	/**
	 * This method is used to set the Userprincipal by getting information from HTTP request parameters
	 * @param request - HTTP request Parameter
	 * @param principal - An object of UserPrincipal
	 * @return - UserPrincipal populated with user information
	 * @throws Exception
	 */
	private UserPrincipal setPrincipal(HttpServletRequest request,UserPrincipal principal) throws Exception{
		HttpSession sess = request.getSession();
		sess.setAttribute(Auth.SUBJECT_SESSION_KEY, lc.getSubject());
		Set sets = lc.getSubject().getPrincipals();
		Iterator it = sets.iterator();
		UserPrincipal user = (UserPrincipal)it.next();
		user.setSessionId(request.getSession().getId());

		result= "success";
		return user;
	}

	/**
	 * TThis method is used to set the Userprincipal by getting information from HTTP request parameters
	 * @param request - HTTP request Parameter
	 * @return - UserPrincipal populated with user information
	 * @throws Exception
	 */
	private UserPrincipal setPrincipal(HttpServletRequest request) throws Exception
	{
		HttpSession sess = request.getSession();
		//EBWLogger.logDebug(this,"Session object is :"+sess);
		sess.setAttribute("subject", fa.getSubject());
		Set sets = fa.getSubject().getPrincipals();
		Iterator it = sets.iterator();
		UserPrincipal user = (UserPrincipal)it.next();
		user.setSessionId(request.getSession().getId());
		result = "success";
		return user;
	}
	/**
	 * This method is used for loading the FAP/DAP information into FAP/DAP tables in database
	 * @param request - HTTP request Parameter
	 * @throws Exception
	 */
	private void loadFapDapInfo(HttpServletRequest request) throws Exception{
		//EBWLogger.trace(this,"Starting method private void loadFapDapInfo(HttpServletRequest request)");
		IEBWService service =null;
		try{
			HttpSession sess = request.getSession();
			sess.setAttribute(Auth.SUBJECT_SESSION_KEY, lc.getSubject());
			Set sets = lc.getSubject().getPrincipals();

			Iterator it = sets.iterator();
			UserPrincipal user = (UserPrincipal)it.next();

			//EBWLogger.logDebug(this,"GRPID : "+user.getUsrgrpid());
			result= "success";

			//Get the user Fap info and set in ServletContext as Attribute
			service = EBWServiceFactory.create("FapService");
			Class paramType[] = {String.class,Object.class};
			Object paramObj[] ={"getFapInfo",user};
			//EBWLogger.logDebug(this,"Calling execute for FAPInfo:"+result);
			Object result = service.execute(paramType,paramObj);
			//EBWLogger.logDebug(this,"result is :"+result);
			HashMap userFap =null;

			if(result!=null)
				userFap = (HashMap)result;

			//EBWLogger.logDebug(this,"Fap info for user"+user.getUsruserid()+"and having role ID:"+user.getUsrgrpid()+" has been populated successfully"+userFap);
			user.setUserFap((LinkedHashMap)userFap);


			//Get the user Menu Fap info and set in ServletContext as Attribute
			service = EBWServiceFactory.create("MenuFapService");
			paramObj[0] ="getMenuFapInfo";
			paramObj[1] = user;
			HashMap userMenuFap = (HashMap)service.execute(paramType,paramObj);
			//EBWLogger.logDebug(this,"Menu Fap info for user"+user.getUsruserid()+"and having role ID:"+user.getUsrgrpid()+" has been populated successfully"+userMenuFap);
			user.setUserMenuFap((LinkedHashMap)userMenuFap);

			//Get the user Fap info and set in ServletContext as Attribute
			service = EBWServiceFactory.create("DapService");
			paramObj[0] = "getDapInfo";
			paramObj[1] = user;
			HashMap userDap =null;
			result = service.execute(paramType,paramObj);
			if(result!=null)
				userDap = (HashMap)result;
			//EBWLogger.logDebug(this,"Dap info for user"+user.getUsruserid()+"and having role ID:"+user.getUsrgrpid()+" has been populated successfully"+userDap);
			user.setUserDap((LinkedHashMap)userDap);
			user.setSessionId(sess.getId());

			// IP address of the user.
			user.setIpAddr(request.getRemoteAddr());

			//Get the user Language info and set in Session as Attribute
			service = EBWServiceFactory.create("LanguageService");
			paramObj[0] = "getLanguageInfo";
			paramObj[1] = user;
			result = service.execute(paramType,paramObj);
			ArrayList userLang =null;
			if(result!=null)
				userLang = (ArrayList)userLang;

			//EBWLogger.logDebug(this,"Language info for user"+user.getUsruserid()+"and having role ID:"+user.getUsrgrpid()+" has been populated successfully"+userLang);
			String langCode="en", countryCode="US";
			if (userLang != null && userLang.size() > 1) {
				ArrayList langConf = (ArrayList) userLang.get(1);
				if (langConf!=null && langConf.size() > 0) {
					String strConf = (String) langConf.get(0);
					if (strConf!=null && strConf.length() > 0) {
						langCode=strConf;
					}
					strConf = (String) langConf.get(1);
					if (strConf!=null && strConf.length() > 0) {
						countryCode=strConf;
					}
				}
			}
			request.getSession().setAttribute(org.apache.struts.Globals.LOCALE_KEY, new java.util.Locale(langCode,countryCode));

			sess.getServletContext().setAttribute(user.getUsrgrpid()+"."+user.getUsruserid()+"."+sess.getId(),user);
		}catch(Throwable th){
			//EBWLogger.logError(this,"Error getting FADDAPInfo :"+th.getMessage());
			th.printStackTrace();
		}
		//EBWLogger.trace(this,"Finished method private void loadFapDapInfo(HttpServletRequest request)");
	}

	/**
	 * This method is called after all authentication and authorzation processes are completed. This method calls a service
	 *  'populateLoginInfo' which populates the login information for current user into Login Tables in Database.
	 * @param principal - An object of UserPrincipal
	 *
 		private void populateLoginInfo(UserPrincipal principal) {
			EBWLogger.trace(this,"Starting populateLoginInfo..");
			try{
				//IEBWService faService = EBWServiceFactory.create("getFANumbers");

				ArrayList fanums = principal.getFanums();
				EBWLogger.logDebug(this,"Fanumbers got in principal are :"+fanums);


				Class cls1[] = {Object.class,Boolean.class};
				Object obj1[] = new Object[2];


				for(int i=0;i<fanums.size();i++){
					principal.setFanum((String)fanums.get(i));
					obj1[0]=principal;
					obj1[1] = new Boolean(false);
					EBWLogger.logDebug(this,"Storing logininfo with fanum "+principal.getFanum());
					IEBWService faService = EBWServiceFactory.create("populateLoginInfo");
					faService.execute(cls1,obj1);
				}

			}catch(Exception e){
				e.printStackTrace();
			}
			EBWLogger.logDebug(this,"Finished populateLoginInfo..");
		}
	 */
	public boolean checkValidUser(HttpServletRequest request,HttpServletResponse response)throws Exception
	{
		EBWLogger.logDebug(this, " Into checkValidUser method -- ");

		boolean accessFlag = false;

		Subject subject = (Subject)request.getSession().getAttribute("subject");
		Set sets =subject.getPrincipals();
		Iterator it = sets.iterator();
		UserPrincipal userPrincipal = (UserPrincipal)it.next();

		Class[] clsparams = {Object.class,Boolean.class};
		Object[] objparams = {userPrincipal,Boolean.valueOf(false)};

		IEBWService service = EBWServiceFactory.create("checkValidUser");
		Object objoutput = service.execute(clsparams, objparams);

		EBWLogger.logDebug(this, " The object output from checkValidUser method is "+objoutput);

		ArrayList arr = (ArrayList)objoutput;

		int val = Integer.parseInt(((ArrayList)arr.get(1)).get(0).toString());

		accessFlag =(val>=1?true :false);

		//System.out.println(" The access value is------------------ "+accessFlag);

		EBWLogger.logDebug(this, "Exiting from checkValidUser Method with the accessFalg as "+accessFlag);

		return accessFlag;
	}	   

	public boolean checkUserFAPAccess(HttpServletRequest request, UserPrincipal principal)
	{
		boolean returnFlag = false;
		EBWLogger.logDebug(this, "The user request URI is "+request.getRequestURI().toString());
		EBWLogger.logDebug(this, " The user request URL is "+request.getRequestURL().toString());

		String uri = request.getRequestURL().toString();
		HashMap map = principal.getUserMenuFap();

		//HashMap fapMap = principal.getUserFap();	        
		String action = request.getParameter("action");
		if(action!=null){
			action = action.toUpperCase().toString();
		}

		String state = request.getParameter("state");
		if(state!=null){
			state = state.toUpperCase().toString();
		}

		if(uri!=null)
		{
			uri = uri.substring(uri.lastIndexOf("/"), uri.indexOf(".do"));       	        
			if(uri.startsWith("/Exec"))
			{
				uri = uri.replace("/Exec", "");
			}
			else if(uri.startsWith("/"))
			{
				uri = uri.replace("/", "");
			}
		}
		if(map !=null &&(map.containsKey(uri)))
		{
			ArrayList actionArray = new ArrayList();
			ArrayList stateArray = new ArrayList();
			if(map.get(uri).toString().indexOf("@") > -1)
			{
				String[] atArray=map.get(uri).toString().split("@");
				for (int i = 0; i < atArray.length; i++) {
					String completeString = atArray[i];
					String amperString[] = completeString.substring(completeString.indexOf("?")+1,completeString.indexOf("~")).split("&");
					String actionString= amperString[0].substring(7,amperString[0].length());
					String stateString= amperString[1].substring(6,amperString[1].length()); 
					actionArray.add(actionString.toUpperCase());
					stateArray.add(stateString.toUpperCase());
				}
			}else{
				String completeString = map.get(uri).toString();
				String amperString[] = completeString.substring(completeString.indexOf("?")+1,completeString.indexOf("~")).split("&");
				String actionString= amperString[0].substring(7,amperString[0].length());
				String stateString= amperString[1].substring(6,amperString[1].length()); 
				actionArray.add(actionString.toUpperCase());
				stateArray.add(stateString.toUpperCase());
			}
			if(!actionArray.isEmpty() && !stateArray.isEmpty() && actionArray.contains(action) && stateArray.contains(state) ){
				returnFlag = true;
			}
		}
		else
		{	 
			ResourceBundle samlRB =  ResourceBundle.getBundle("SAML");
			String TmpRoleStr = samlRB.getString("FTMPROLE");
			if(TmpRoleStr.indexOf(",")!=-1){
				String[] tempRoleStrArr = TmpRoleStr.split(",");
				for (int i = 0; i < tempRoleStrArr.length; i++) {
					if(tempRoleStrArr[i].toString().equalsIgnoreCase(uri)){
						returnFlag = true;
					}
				}
			}
			else {
				if(TmpRoleStr.toString().equalsIgnoreCase(uri)){
					returnFlag = true;
				}
			}
		}
		return returnFlag;
	}
}