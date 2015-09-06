/**
 * 
 */
package com.tcs.ebw.serverside.jaas.struts.actions;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.Set;

import javax.security.auth.Subject;
import javax.security.auth.login.LoginContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.tcs.ebw.common.util.EBWLogger;
import com.tcs.ebw.common.util.PropertyFileReader;
import com.tcs.ebw.exception.EbwException;
import com.tcs.ebw.mvc.action.EbwAction;
import com.tcs.ebw.serverside.factory.EBWServiceFactory;
import com.tcs.ebw.serverside.factory.IEBWService;
import com.tcs.ebw.serverside.jaas.auth.Auth;
import com.tcs.ebw.serverside.jaas.auth.EBWCallbackHandler;
import com.tcs.ebw.serverside.jaas.customLoginModules.CustomLoginFactory;
import com.tcs.ebw.serverside.jaas.customLoginModules.CustomLoginModule;
import com.tcs.ebw.serverside.jaas.principal.UserPrincipal;
import com.tcs.ebw.serverside.jaas.struts.forms.LogonFormBean;
/**
 * @author 197172
 *
 */
public class LogonAction extends EbwAction {

	/**
	 * Form used to load information from user screen to this class.
	 */
	
	private LogonFormBean logonFormBean;

	/**
	 * LoginContext object which is used to find the jaas configuration file and
	 * instantiate the specified login module. It also forwards the handle to the
	 * specified callback handler. @see com.tcs.ebw.jaas.auth.EBWCallbackHandler for more details on callbacks.
	 */
	private LoginContext lc;

	/**
	 * String Objects used to store the credentials got from the user screen. This will
	 * be used to verify against the back end system by JAAS modules. 
	 */
	private String username,password;

	/**
	 * Logging output for this plug in instance.
	 */
	protected Log log = LogFactory.getLog(this.getClass());

	/**
	 * Variable to store the resultant action
	 */
	private String result ;
	/**
	 * @see org.apache.struts.action.Action#execute(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */


	private UserPrincipal principal; 
	private String miscinfo;

	private IEBWService service =null;
	private boolean byPassAuthentication;
	private boolean isJAASImpl = true;
	/**
	 *  This method is used by the application to call the appropriate authentication
	 *  mechanism to be followed for authentication based on the connection type. 
	 */
	public ActionForward performTask(ActionMapping mapping, ActionForm actionForm,
			HttpServletRequest request, HttpServletResponse response) throws Exception,EbwException {
				
		try{
		    boolean isAuthenticated=false;
			boolean ssoLogin =false;
			String userid_header_key=null;
			String temp[] = null;
			
			EBWLogger.trace(this,"Starting method public ActionForward performTask(ActionMapping mapping, ActionForm actionForm," +
			"HttpServletRequest request, HttpServletResponse response)");

			if(actionForm!=null && actionForm instanceof ActionForm) {
				EBWLogger.logDebug(this,"actionForm is not null");
				logonFormBean = (LogonFormBean) actionForm;
				username = logonFormBean.getUsername();
				password = logonFormBean.getPassword();
				
				try {
					ssoLogin = new Boolean(PropertyFileReader.getProperty("SSOLOGIN")).booleanValue();
				} catch(MissingResourceException mre){
					EBWLogger.logDebug(this, "WARNING:No configuration found for key -'SSOLOGIN'. Continuing with default ");
					ssoLogin = false;
				}catch(Exception e){
					EBWLogger.logDebug(this,"WARNING: No configuration found for key -'SSOLOGIN'. Continuing with default");
					ssoLogin = false;
				}
								
				if(ssoLogin) {
					try {
						userid_header_key = PropertyFileReader.getProperty("SSO_USERID_HEADER_KEY");
						EBWLogger.logDebug(this, "SSO_USERID_HEADER_KEY="+ userid_header_key);
						if (userid_header_key != null || !userid_header_key.equals("")) {
							String tempuid = request.getHeader(userid_header_key);
							if (tempuid != null || !tempuid.equals("")) {
								username = tempuid;
							}
						}
					} catch (MissingResourceException mre) {
						EBWLogger.logDebug(this,"WARNING: 'SSO_USERID_HEADER_KEY' not configured...");
					} catch (NullPointerException npe) {
						EBWLogger.logError(this, "SSO Token-'"+ userid_header_key + "' is null");
					}
				}
								
				EBWLogger.logDebug(this,"username :"+username);
				EBWLogger.logDebug(this,"password :"+password);
				
				try{
					String getURLparams=PropertyFileReader.getProperty("MISC_INFO_KEY");
					temp=getURLparams.split(",");
					miscinfo="";
					for (int i=0;i<temp.length;i++){
						miscinfo=miscinfo+request.getParameter(temp[i]);
						if(i!=(temp.length-1))
								miscinfo=miscinfo+" ";
					}					
				}catch(MissingResourceException mre){
					EBWLogger.logDebug(this, "WARNING:No configuration found for key -'MISC_INFO_KEY'. Continuing with default ");
				}catch(Exception e){
					EBWLogger.logDebug(this,"WARNING: 'miscinfo' not present in URL. Continuing with default");
				}
			}
			else {
				EBWLogger.trace(this,"Action form is null");
				request.setAttribute("error","Authentication failed...Username or Password is invalid..");
				return mapping.findForward("failure");
			}

			EBWLogger.trace(this,"created JAASAbstractAuth (fa) object");
			
			try {
				EBWLogger.logDebug(this, "ssologin :" + ssoLogin);
				if (ssoLogin) {
					String ssoAction = PropertyFileReader.getProperty("SSOLOGIN.action");
					String ssoState = PropertyFileReader.getProperty("SSOLOGIN.state");

					request.getSession().setAttribute("action", ssoAction);
					request.getSession().setAttribute("state", ssoState);

					Map map = request.getParameterMap();
					EBWLogger.logDebug(this, "Map from requestparameter:"+ map);
					Set keys = map.keySet();
					Iterator keyIterator = keys.iterator();
					for (int i = 0; i < map.size(); i++) {
						String attrName = (String) keyIterator.next();
						request.getSession().setAttribute(attrName,map.get(attrName));
						request.setAttribute(attrName, map.get(attrName)); 
						response.addHeader(attrName, ((String[]) map.get(attrName))[0]);
						EBWLogger.logDebug(this, "Setting request attribute for SSO :"+ attrName + " ,length "+ attrName.length() + " ,value:"+ map.get(attrName));
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				EBWLogger.trace(this,"SSOLOGIN property is not configured, hence continuing without single signon");
			}

			byPassAuthentication = false;			
			try{
		    	byPassAuthentication = new Boolean(PropertyFileReader.getProperty("byPassAuthentication")).booleanValue();
			}catch(Exception e){
			    EBWLogger.logDebug(this,"ByPassAuthentication is disabled");			    
			}
				
			if(!byPassAuthentication && !isAuthenticated){
			    				//javax.security.auth.callback.CallbackHandler ebwch = new EBWCallbackHandler(username,password);
				System.out.println("Into JAAS------------");
				javax.security.auth.callback.CallbackHandler ebwch = new EBWCallbackHandler(username,password,miscinfo);
				Set sets = null;
				try {
					isJAASImpl = new Boolean(PropertyFileReader.getProperty("isJAASImpl")).booleanValue();
				} catch(Exception e){
					EBWLogger.logDebug(this,"WARNING: No configuration found for key -'isJAASImpl'. Continuing with default JAAS Implementation");
					isJAASImpl = true;
				}
				
				EBWLogger.logDebug(this, "Is it JAAS Implementaion:"+isJAASImpl);
				System.out.println("Is it JAAS Implementaion:"+isJAASImpl);
				
				HttpSession sess = request.getSession();
				if(isJAASImpl == true) {
					lc = new LoginContext("EBWLoginModules",ebwch);
					lc.login();
					EBWLogger.logDebug(this,"Login context created.."+lc.getClass()+" lcnull"+(lc==null));
					isAuthenticated=(lc.getSubject()!=null);
//					result = new Boolean(isAuthenticated).toString();
					result = isAuthenticated == true ? "success" : "failure";
					EBWLogger.logDebug(this, "JAAS authentication result:"+result);
					
					sets = lc.getSubject().getPrincipals();
					sess.setAttribute(Auth.SUBJECT_SESSION_KEY, lc.getSubject());
				} else  {
					String customLoginModuleName = null;
					try {
						customLoginModuleName = PropertyFileReader.getProperty("CustomLoginModule");
						System.out.println("Custom Login module name being invoked:"+customLoginModuleName);
						EBWLogger.logDebug(this, "Custom Login module name being invoked:"+customLoginModuleName);
					} catch(Exception e){
						EBWLogger.logDebug(this,"WARNING: No Custom Login Module configuration found for key -'CustomLoginModule'.");
						throw new EbwException("SYS0014");
					}
					CustomLoginModule customLoginModule = (CustomLoginModule)CustomLoginFactory.getCustomLoginImpl(customLoginModuleName);
					result = customLoginModule.login(ebwch);
					System.out.println("CUSTOM Login module result:"+result);
					EBWLogger.logDebug(this, "CUSTOM Login module result:"+result);
					isAuthenticated=(result.startsWith("success") ? true : false);
					
					if(isAuthenticated) {
						Subject subject = new Subject();
					    HttpSession session = request.getSession();
					    session.setAttribute(Auth.SUBJECT_SESSION_KEY, subject);
					    						    
					    if(username!=null && username.trim().length() > 1){
					        principal = new UserPrincipal(username);
					        principal.setUsruserid(username);
					        subject.getPrincipals().add(principal);
//					        principal.setUsrgrpid("RJMAIN");
//					        principal.setUsruserid("CARTSDOE");
					        request.getSession().setAttribute("subject",subject);
					    }
					    else{
					    	String defaultPrincipal = "defaultPrincipalName";
					    	try {					    		
								defaultPrincipal = PropertyFileReader.getProperty("defaultPrincipalName");
							} catch(Exception e){
								EBWLogger.logDebug(this,"WARNING: Name of the default Principal is missing. Using default name for the Principal");
							}
					        principal = new UserPrincipal(defaultPrincipal);
					        principal.setUsruserid(defaultPrincipal);
					        subject.getPrincipals().add(principal);
//					        principal.setUsruserid("CARTSDOE");
//					        principal.setUsrgrpid("RJMAIN");
					        request.getSession().setAttribute("subject",subject);
					    }
					   
					    principal.setSessionId(request.getSession().getId());
					    principal.setApplContext(request.getContextPath().substring(1));
					    sess.setAttribute(Auth.SUBJECT_SESSION_KEY, subject);
					} else {
						System.out.println("Custom Login Module: Login module failed");
						EBWLogger.logDebug(this, "Custom Login Module: Login module failed");
						return mapping.findForward(result);
					}
					Subject subject = (Subject)request.getSession().getAttribute("subject");
					sets = subject.getPrincipals();
				}
				
				Iterator it = sets.iterator();
				principal= (UserPrincipal)it.next();
				principal.setApplContext(request.getContextPath().substring(1));
				
				 for(int i=0;i<temp.length;i++) {
				    	if(request.getParameter(temp[i]) != null) {
				    		String var = "set"+Character.toUpperCase(temp[i].charAt(0))+temp[i].substring(1);
				    		Class []types = new Class[] { String.class };
				    		Method method = principal.getClass().getMethod(var, types);
				    		method.invoke(principal, new Object[] { new String(request.getParameter(temp[i]))});
				    		EBWLogger.logDebug(this, "Invoking method:"+var+" with value:"+request.getParameter(temp[i]));
				    		System.out.println("Invoking method:"+var+" with value:"+request.getParameter(temp[i]));
				    	}
				    }
				 
				//principal.setMMSAMLAssertion(new String(request.getParameter("MMSAMLAssertion")));
				setPrincipal(request,principal);
		
				try {
					if(new Boolean(PropertyFileReader.getProperty("getFapInfo")).booleanValue() == true) {
						EBWLogger.logDebug(this,"Loading fap dap information");
						loadFapDapInfo(request);
					}
				} catch(MissingResourceException mre){
					EBWLogger.logDebug(this, "WARNING:No configuration found for key -'getFapInfo'. Continuing with default ");
				}catch(Exception e){
					EBWLogger.logDebug(this,"WARNING: No configuration found for key -'getFapInfo'. Continuing with default");
				}
				
				try {
					if(new Boolean(PropertyFileReader.getProperty("checkSessionDuplication")).booleanValue() == true) {
						// Code for checking if duplicate session exists
						boolean isSessionDuplicated = checkDuplicateSessions(principal);
						// code ends
						if (!isSessionDuplicated) {
							EBWLogger.trace(this, "Loadng populateLoginInfo..");
							String populateLoginStr = "true";
							try {
								populateLoginStr = PropertyFileReader.getProperty("PopulateLoginInfo");
							} catch (Exception e) {
								EBWLogger.logDebug(this,"Key PopulateLoginInfo Not found, So populating login info by default.");
							}
							if (populateLoginStr.equalsIgnoreCase("true"))
								populateLoginInfo(principal);
						}
					}
				} catch(MissingResourceException mre){
					EBWLogger.logDebug(this, "WARNING:No configuration found for key -'checkSessionDuplication'. Continuing with default ");
				}catch(Exception e){
					EBWLogger.logDebug(this,"WARNING: No configuration found for key -'checkSessionDuplication'. Continuing with default");
				}		
			}else if(byPassAuthentication){			   
			    principal = new UserPrincipal("CARTSDOE");			    
				setPrincipal(request,principal);
				EBWLogger.logDebug(this,"Loading fap dap information");
//				if(lc!=null)
//				    loadFapDapInfo(request);
//				EBWLogger.trace(this,"Loadng populateLoginInfo..");
				
				if(lc!=null) {
					try {
						if(new Boolean(PropertyFileReader.getProperty("getFapInfo")).booleanValue() == true) {
							EBWLogger.logDebug(this,"Loading fap dap information inside byPassAuthentication");
							loadFapDapInfo(request);
						}
					} catch(MissingResourceException mre){
						EBWLogger.logDebug(this, "WARNING:No configuration found for key -'getFapInfo'. Continuing with default ");
					}catch(Exception e){
						EBWLogger.logDebug(this,"WARNING: No configuration found for key -'getFapInfo'. Continuing with default");
					}
				}

				String populateLoginStr ="true";
				try{
				    populateLoginStr = PropertyFileReader.getProperty("PopulateLoginInfo");
				}catch(Exception e){
				    EBWLogger.logDebug(this,"Key PopulateLoginInfo Not found, So populating login info by default.");
				  
				}
				if(populateLoginStr.equalsIgnoreCase("true"))
				    populateLoginInfo(principal);
			}
			else{
				request.setAttribute("error","Authentication failed...Username or Password is invalid..");
				result="failure";
			}
		}catch(Throwable throwable){
			result = "failure";
//			request.setAttribute("error","Authentication failed...Username or Password is invalid..");
			request.setAttribute("error",throwable.getMessage());
			EBWLogger.logError(this,throwable.getMessage());
			throwable.printStackTrace();
			mapping.findForward(result);
		}
		try {
		if (PropertyFileReader.getProperty("isPwdExpEnabled").toString().toUpperCase().equals("TRUE") && !result.equals("failure")) {
				EBWLogger.logDebug(this, "RESULT:" + result);
				String[] expParams;
				expParams = setPwdExpParameters();
				String Pwdwarnperiod = (expParams[0]);
				String Pwdexpperiod = (expParams[1]);
				String Expdays = (expParams[2]);

				request.getSession().setAttribute("Pwdexpperiod", Pwdexpperiod);
				request.getSession().setAttribute("Pwdwarnperiod", Pwdwarnperiod);
				request.getSession().setAttribute("Expdays", Expdays);

				if (Integer.parseInt(Expdays) > Integer.parseInt(Pwdexpperiod)) {
					throw new EbwException("SYS0015");
				}
			} 
		} catch(EbwException ebw){
			 saveErrorMessage(request, ebw);
			 result="pwdexpired";
			 EBWLogger.logError(this,"Password has expired");		 
		}catch(MissingResourceException mre){
			EBWLogger.logDebug(this, "Key - isPwdExpEnabled not present...Continuing with default");
		}catch(Exception e){
			EBWLogger.logDebug(this, "##ERROR - while getting Password Expiry information:");
		}
		
		System.out.println("Returning mapping.findforward:"+result);
		return mapping.findForward(result);
	}
	
	
	private String[] setPwdExpParameters(){
		EBWLogger.trace(this, "Into setPwdExpParameters()");
		String expParams[]=null;
		Object retVal=new Object();
		try {
		 Class cls1[] = {String.class,Object.class,Boolean.class};
		 Object obj1[] = {"getPwdExpInfo",principal,new Boolean(false)};
		 
		 IEBWService getPwdExpService;
		 getPwdExpService= EBWServiceFactory.create("getPwdExpInfo");
		 retVal=getPwdExpService.execute(cls1,obj1);
		 int size=(((ArrayList)((ArrayList)retVal).get(1)).size());
		 expParams=new String[size];
		 for(int i=0;i<size;i++)
			{
				expParams[i]=(((ArrayList)((ArrayList)retVal).get(1)).get(i).toString());	
				System.out.println("PwdExp Parameters["+i+"]:"+expParams[i]);
			}
		 
//		 String Pwdwarnperiod=(expParams[0]);
//		 String Pwdexpperiod=(expParams[1]);
//		 String Expdays=(expParams[2]);
//		 
//
//		 request.getSession().setAttribute("Pwdexpperiod",Pwdexpperiod);
//		 request.getSession().setAttribute("Pwdwarnperiod", Pwdwarnperiod);
//		 request.getSession().setAttribute("Expdays", Expdays);
		 
		 EBWLogger.trace(this, "Exiting setPwdExpParameters()");		 
	
		 
		}
		catch(Exception e){
			EBWLogger.logError(this, "ERROR in getting Password Expiry Information");
		}
	
		return expParams;
	
}
	
	/**
 * This method is used for getting the information from request parameters and setting it in the 
 * UserPrincipal for the user.
 * @param request - Object of HttpServletRequest
 * @param principal - Object of UserPrincipal
 * @return An object of Userprincipal
 * @throws Exception
 */
	private UserPrincipal setPrincipal(HttpServletRequest request,UserPrincipal principal) throws Exception{
		HttpSession sess = request.getSession();
		UserPrincipal user =null;
		if(lc!=null){
//			sess.setAttribute(Auth.SUBJECT_SESSION_KEY, lc.getSubject());
//			Set sets = lc.getSubject().getPrincipals();
			Set sets = ((Subject)request.getSession().getAttribute(Auth.SUBJECT_SESSION_KEY)).getPrincipals();
			Iterator it = sets.iterator();
			user = (UserPrincipal)it.next();
			user.setSessionId(request.getSession().getId());
		}
		else if(byPassAuthentication){
		    Subject subject = new Subject();
		    
		    sess.setAttribute(Auth.SUBJECT_SESSION_KEY, subject);
		    
		    if(request.getSession().getAttribute("username")!=null){ // username from URL Access 
		        principal = new UserPrincipal(((String[])request.getSession().getAttribute("username"))[0]);
		        principal.setUsruserid(((String[])request.getSession().getAttribute("username"))[0]);
		        principal.setUsrgrpid("RJMAIN");
		        principal.setUsruserid("CARTSDOE");
		        request.getSession().setAttribute("subject",subject);
		    }
		    else{
		        principal = new UserPrincipal("CARTSDOE");
		        principal.setUsruserid("CARTSDOE");
		        principal.setUsrgrpid("RJMAIN");
		    }
		    
		    principal.setSessionId(request.getSession().getId());
		    
		}
//		result= "success";
		return user;
	}
/**
 * This method is used for loading the FAP/DAP information based on request parameters passed from ActionServlet.
 * @param request - Object of HttpServletRequest
 * @throws Exception
 */
	private void loadFapDapInfo(HttpServletRequest request) throws Exception{
		EBWLogger.trace(this,"Starting method private void loadFapDapInfo(HttpServletRequest request)");
		EBWLogger.trace(this,"Start Time "+new java.util.Date().getTime());
		IEBWService service =null;
		try{
		    HttpSession sess = request.getSession();
		    
//		    	sess.setAttribute(Auth.SUBJECT_SESSION_KEY, lc.getSubject());
//				Set sets = lc.getSubject().getPrincipals();
		    Set sets = ((Subject)request.getSession().getAttribute(Auth.SUBJECT_SESSION_KEY)).getPrincipals();
		    
				Iterator it = sets.iterator();
				UserPrincipal user = (UserPrincipal)it.next();
	
				EBWLogger.logDebug(this,"GRPID : "+user.getUsrgrpid());
				result= "success";
				executeService("getFapInfo",user);
				executeService("getMenuFapInfo",user);
				executeService("getDapInfo",user);
				executeService("getLanuageInfo",user);
//				System.out.println("Populating FAP Information..");
//				//Get the user Fap info and set in ServletContext as Attribute
//				service = EBWServiceFactory.create("FapService");
//				Class paramType[] = {String.class,Object.class};
//				Object paramObj[] ={"getFapInfo",user};
//				EBWLogger.logDebug(this,"Calling execute for FAPInfo:"+result);
//				Object result = service.execute(paramType,paramObj);
//				EBWLogger.logDebug(this,"result is :"+result);
//				HashMap userFap =null;
//	
//				if(result!=null)
//					userFap = (HashMap)result;
//	
//				EBWLogger.logDebug(this,"Fap info for user"+user.getUsruserid()+"and having role ID:"+user.getUsrgrpid()+" has been populated successfully"+userFap);
//				user.setUserFap(userFap);
//				System.out.println("FAP info population complete");
//				
//				System.out.println("Populating MenuFap Information..");
//				//Get the user Menu Fap info and set in ServletContext as Attribute
//				service = EBWServiceFactory.create("MenuFapService");
//				paramObj[0] ="getMenuFapInfo";
//				paramObj[1] = user;
//				HashMap userMenuFap = (HashMap)service.execute(paramType,paramObj);
//				EBWLogger.logDebug(this,"Menu Fap info for user"+user.getUsruserid()+"and having role ID:"+user.getUsrgrpid()+" has been populated successfully"+userMenuFap);
//				user.setUserMenuFap(userMenuFap);
//	
//				System.out.println("Populating DAP Information..");
//				//Get the user Fap info and set in ServletContext as Attribute
//				service = EBWServiceFactory.create("DapService");
//				paramObj[0] = "getDapInfo";
//				paramObj[1] = user;
//				HashMap userDap =null;
//				result = service.execute(paramType,paramObj);
//				if(result!=null)
//					userDap = (HashMap)result;
//				EBWLogger.logDebug(this,"Dap info for user"+user.getUsruserid()+"and having role ID:"+user.getUsrgrpid()+" has been populated successfully"+userDap);
//				user.setUserDap(userDap);
				
				user.setSessionId(sess.getId());
	
				// IP address of the user.
				user.setIpAddr(request.getRemoteAddr());
				String langCode=null,countryCode=null;
				if(user.getLangCode()!=null && user.getCountryCode()!=null){
				langCode 	= user.getLangCode();
				countryCode = user.getCountryCode();
				
				
//				System.out.println("Populating Language Service Information..");
//				//Get the user Language info and set in Session as Attribute
//				service = EBWServiceFactory.create("LanguageService");
//				paramObj[0] = "getLanguageInfo";
//				paramObj[1] = user;
//				result = service.execute(paramType,paramObj);
//				ArrayList userLang =null;
//				if(result!=null)
//					userLang = (ArrayList)userLang;
//	
//				EBWLogger.logDebug(this,"Language info for user"+user.getUsruserid()+"and having role ID:"+user.getUsrgrpid()+" has been populated successfully"+userLang);
//				String langCode="en", countryCode="US";
//				if (userLang != null && userLang.size() > 1) {
//					ArrayList langConf = (ArrayList) userLang.get(1);
//					if (langConf!=null && langConf.size() > 0) {
//						String strConf = (String) langConf.get(0);
//						if (strConf!=null && strConf.length() > 0) {
//							langCode=strConf;
//						}
//						strConf = (String) langConf.get(1);
//						if (strConf!=null && strConf.length() > 0) {
//							countryCode=strConf;
//						}
//					}
//				}
				request.getSession().setAttribute(org.apache.struts.Globals.LOCALE_KEY, new java.util.Locale(langCode,countryCode));
				}
				sess.getServletContext().setAttribute(user.getUsrgrpid()+"."+user.getUsruserid()+"."+sess.getId(),user);
				
		}catch(Throwable th){
			EBWLogger.logError(this,"Error getting FADDAPInfo :"+th.getMessage());
			saveErrorMessage(request, th);
			th.printStackTrace();
		}
		/*finally{
	        service.close();
	    }
		 */
		EBWLogger.trace(this,"End Time "+new java.util.Date().getTime());
		EBWLogger.trace(this,"Finished method private void loadFapDapInfo(HttpServletRequest request)");
	}

	private ArrayList getFANumbers(UserPrincipal principal) throws Exception{
		EBWLogger.trace(this, "Into method getFANumbers(UserPrincipal principal)");
		ArrayList fanums=new ArrayList();
		ArrayList acctids=new ArrayList();
		ArrayList Fainfo=new ArrayList();
		Object retVal=new Object();
			try {
		Class cls1[] = {String.class,Object.class};
		Object obj1[] = {"getFANumbers",principal};
		IEBWService getfaService;
		getfaService = EBWServiceFactory.create("getFANumbers");
		retVal=getfaService.execute(cls1,obj1);
		int columns=0;
		columns=((ArrayList)((ArrayList)retVal).get(0)).size();
		//System.out.println("COL:"+columns);
			for(int i=1;i<((ArrayList)retVal).size();i++)
			{
			EBWLogger.logDebug(this,"FA NUMB:"+((ArrayList)((ArrayList)retVal).get(i)).get(0).toString());
			fanums.add(((ArrayList)((ArrayList)retVal).get(i)).get(0).toString())	;
			if(columns>1)
				acctids.add(((ArrayList)((ArrayList)retVal).get(i)).get(1).toString());
			}
				
			Fainfo.add(fanums);
			if(columns>1){
				Fainfo.add(acctids);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			EBWLogger.logError(this,"##ERROR: in populating FA Numbers\n");
			throw e;
		}
		EBWLogger.trace(this, "Exiting method getFANumbers(UserPrincipal principal)");
		
		
		return Fainfo;
	}
	
	
	
	private boolean checkDuplicateSessions(UserPrincipal principal) throws Exception{
			EBWLogger.trace(this, "Into method Check Duplicate Sessions(UserPrincipal principal)");
			boolean retVal;
			ArrayList objOutput;
			Class cls1[] = {String.class,Object.class};
			Object obj1[] = {"checkDuplicateSessions",principal};
			
			try{
			IEBWService dupSessService = EBWServiceFactory.create("checkDuplicateSessions");
			objOutput=(ArrayList)dupSessService.execute(cls1,obj1);
			
			if (Integer.parseInt(((ArrayList)objOutput.get(1)).get(0).toString())>0)
				retVal=true;
			else
				retVal=false;
			}catch(Exception e){
				EBWLogger.logError(this,"##ERROR: checking Duplicate Sessions \n");
				throw e;
			}
			EBWLogger.trace(this, "Exiting method Check Duplicate Sessions(UserPrincipal principal)");
			return retVal;
	}
	
	/**
	 * This method is called after all authentication and authorzation processes are completed. This method calls a service
	 *  'populateLoginInfo' which populates the login information for current user into Login Tables in Database.
	 * @param principal - An object of UserPrincipal
	 */
	private void populateLoginInfo(UserPrincipal principal) throws Exception{
		EBWLogger.trace(this,"Starting populateLoginInfo..");
		try{

			ArrayList acctids=new ArrayList();
			ArrayList fanums=new ArrayList();
			ArrayList Fainfo=getFANumbers(principal);
			
			fanums=(ArrayList)(Fainfo.get(0));
			if(Fainfo.size()>1)
				acctids=(ArrayList)(Fainfo.get(1));
			
			System.out.println("Returning FA Numbers for "+principal.getUsruserid());
			
			//ArrayList fanums = principal.getFanums();
			System.out.println("Fanumbers are :"+fanums);
			if(Fainfo.size()>1)
				System.out.println("Accnumbers are :"+acctids);
			//processing FA Numbers for RIS user
		
			String tmpuserid = principal.getUsruserid();
		   	ArrayList objOutput;
	    	if( fanums!=null && ( fanums.contains("ALLFA") || fanums.contains("FAALL") )){
			    	    EBWLogger.logDebug(this,"RIS USER ");
			    	    principal.setUsrusername(tmpuserid);
			    	    
			    	    //code for RIS populating % STARTS
			    	    if(fanums.contains("ALLFA"))
			    	    		principal.setFanum("ALLFA");
			    	    else if(fanums.contains("FAALL"))
			    	    		principal.setFanum("FAALL");
			    	    //code for RIS populating % ENDS
			    	    
//			    	    Class cls1[] = {String.class,Object.class,Boolean.class};
//			    		Object obj1[] = {"getAuthenticationDetailsRIS",principal,new Boolean(false)};
//			    		IEBWService getRISfaService;
//			    		getRISfaService = EBWServiceFactory.create("getAuthenticationDetailsRIS");
//			    		objOutput=(ArrayList)getRISfaService.execute(cls1,obj1);
//			    			
//			    	    fanums = new ArrayList();
//			    	    acctids=new ArrayList();
//			    	    if(objOutput!=null)
//			    	        for(int h=1;h<objOutput.size();h++){
//			    	            ArrayList datarow =(ArrayList)objOutput.get(h);
//			    	            String fadata = (String)datarow.get(1);
//			    	            String accdata=null;
//			    	            if(Fainfo.size()>1){ 
//			    	            	accdata=(String)datarow.get(2);
//			    	            	EBWLogger.logDebug(this,"Account Numbers from Query Output - data :"+accdata);
//			    	            }
//			    	            EBWLogger.logDebug(this,"FA Numbers from Query Output - data :"+fadata);
//			    	            
//			    	            if(!( fadata.trim().equalsIgnoreCase("ALLFA") || fadata.trim().equalsIgnoreCase("FAALL"))){
//			    	                EBWLogger.logDebug(this,"Adding FA Numbers :"+fadata+", length :"+fadata.length());
//			    	                fanums.add((String)datarow.get(1));
//			    	                if(Fainfo.size()>1){ 
//			    	                	EBWLogger.logDebug(this,"Adding Account Numbers :"+accdata+", length :"+accdata.length());
//			    	                	fanums.add(accdata);
//			    	                }
//			    	            }
//			    	        }
			    	    
			    	    //code for RIS populating % STARTS
			    
			    	    
			    	    fanums.add("%");
			    	    //code for RIS populating % ENDS			    	    
			    	}
			    	principal.setName(tmpuserid);
			    	
			    	//RIS ends
			    	
			
			Class cls1[] = {Object[].class,Boolean.class};
			Object obj1[] = new Object[2];
			UserPrincipal principals[] = new UserPrincipal[fanums.size()];
			IEBWService faService = EBWServiceFactory.create("populateLoginInfo");
			
			for(int i=0;i<fanums.size();i++){
				UserPrincipal tmpPrincipal =new UserPrincipal();
				tmpPrincipal.setUsruserid(principal.getUsruserid());
				tmpPrincipal.setUsrgrpid(principal.getUsrgrpid());
				EBWLogger.logDebug(this,"Userid in Principal is :"+tmpPrincipal.getUsruserid());
				tmpPrincipal.setSessionId(principal.getSessionId());
				//tmpPrincipal.setFanums(principal.getFanums());
				tmpPrincipal.setFanums(fanums);
				tmpPrincipal.setFanum((String)fanums.get(i));
				
				tmpPrincipal.setExtsessionid(principal.getExtsessionid());
				tmpPrincipal.setExtuserid(principal.getExtuserid());
				
				if(Fainfo.size()>1){
					tmpPrincipal.setUsracctids(acctids);
					tmpPrincipal.setUsracctid((String)acctids.get(i));
				}
				principals[i] =tmpPrincipal;
				
			}
			
			obj1[0]=principals;
			obj1[1] = new Boolean(false);
			
			faService.execute(cls1,obj1);// Its a batch call now, because of TO Array. 
			faService.close();
			
			

		}catch(Exception e){
			e.printStackTrace();
			throw e;
		}
		EBWLogger.logDebug(this,"Finished populateLoginInfo..");
	}
	
private void executeService(String serviceId,UserPrincipal user){
		
		String loadInfo ="false";
		Object result =null;
		try{
			loadInfo = PropertyFileReader.getProperty(serviceId);
		}catch(Exception e){
			EBWLogger.logDebug(this, "Service "+serviceId +" is not loaded during login.");
		}
		
		if(loadInfo.equalsIgnoreCase("true")){
			try{
				//Get the user Fap info and set in ServletContext as Attribute
				if(service==null && loadInfo.equalsIgnoreCase("true")){
					EBWLogger.logDebug(this,"Creating FapService to load FAPDAp Informations..");
					service = EBWServiceFactory.create("FapService");
					EBWLogger.logDebug(this,"FapService created..");
				}
				
				Class paramType[] = {String.class,Object.class,Boolean.class};
				Object paramObj[] ={serviceId,user,new Boolean(false)};
				EBWLogger.logDebug(this,"Calling execute for "+serviceId);
				result = service.execute(paramType,paramObj);
				EBWLogger.logDebug(this,"result is :"+result);
				
				if(result!=null){
					if(serviceId.equalsIgnoreCase("getFapInfo"))
						user.setUserFap((HashMap)result);
					else if(serviceId.equalsIgnoreCase("getMenuFapInfo"))
						user.setUserMenuFap((HashMap)result);
					else if(serviceId.equalsIgnoreCase("getDapInfo"))
						user.setUserDap((HashMap)result);
					else if(serviceId.equalsIgnoreCase("getLanguageInfo")){
						ArrayList userLang =null;
						if(result!=null)
							userLang = (ArrayList)result;
			
						EBWLogger.logDebug(this,"Language info for user"+user.getUsruserid()+"and having role ID:"+user.getUsrgrpid()+" has been populated successfully"+userLang);
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
						user.setLangCode(langCode);
						user.setCountryCode(countryCode);
					}
				}
			}catch(Exception e){
				EBWLogger.logDebug(this,serviceId+" not loaded..");
				EBWLogger.logError(this,new EbwException("SYS0017").getMessage());
				
			}
		}
		
	}
}
