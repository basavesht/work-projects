package com.tcs.ebw.payments.action;

import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.tcs.Payments.ms360Utils.ChkReqConstants;
import com.tcs.Payments.ms360Utils.MSSystemDefaults;
import com.tcs.Payments.ms360Utils.TxnTypeCode;
import com.tcs.ebw.common.util.EBWLogger;
import com.tcs.ebw.mvc.action.EbwAction;
import com.tcs.ebw.serverside.jaas.principal.UserPrincipal;
import com.tcs.ebw.serverside.services.channelPaymentServices.AccountBalance;
import com.tcs.ebw.serverside.services.channelPaymentServices.AccountPlating;
import com.tcs.ebw.serverside.services.channelPaymentServices.PortfolioLoansService;
import com.tcs.ebw.payments.businessdelegate.GetAccountDetailsAjaxDelegate;
import com.tcs.ebw.payments.transferobject.AjaxAttributes;
import com.tcs.ebw.payments.transferobject.PortfolioLoanAccount;

/**
 * @author : 224703
 * **************   Revision History ************************
 * Modified-By |  Modified-Date |  Project-Phase  | Changes
 *  224703          23-09-2011      P3-B             PLA   
 * **********************************************************
 */
public class GetAccountDetailsAjaxAction extends EbwAction
{
	/**
	 * Logger for this class
	 */
	private static final Logger performanceLogger = Logger.getLogger("MSPerformanceLogger"); //$NON-NLS-1$

	/**
	 * performTask method of CreatePaymentsAction class. 
	 */ 
	public ActionForward performTask (ActionMapping mapping, ActionForm form,HttpServletRequest request, HttpServletResponse response) throws Exception 
	{
		long startTime = System.currentTimeMillis();		
		long elapsedTime=0; 
		String sessionId ="";
		if (request.getSession()!=null){
			sessionId=request.getSession().getId();
		}	
		if (performanceLogger.isInfoEnabled()) {
			performanceLogger.info("IP Address : " + request.getRemoteAddr() + " - Thread Id : " + Thread.currentThread().getId() + " - Session Id : " + sessionId + " - " + this.getClass().getName() + " - performTask(ActionMapping, ActionForm, HttpServletRequest, HttpServletResponse) - Start" ); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
		}

		EBWLogger.trace(this, "Starting performTask()"); 
		UserPrincipal objUserPrincipal = getUserPrincipal(request);
		String action = request.getParameter("action");
		String transferType = request.getParameter("transferType");
		String selectedAccRefID = request.getParameter("selectedAccRefID");
		String isRTABReq = request.getParameter("isRTABReq");
		String isAcntPlatingReq = request.getParameter("isAcntPlatingReq");
		String debitCreditInd = request.getParameter("debitCreditInd"); //DR or CR flag...

		//Edit on load required attributes..
		String fromAccountRefID = request.getParameter("fromAccountRefID");
		String toAccountRefID = request.getParameter("toAccountRefID");
		String frm_office_account_fa = request.getParameter("frmOfficeAccFa");
		String to_office_account_fa = request.getParameter("toOfficeAccFa");

		HttpSession session = request.getSession(); 
		String outputAccList="";
		try 
		{
			if(action.equals("INIT")) 
			{
				outputAccList = callAccountList(session,objUserPrincipal,transferType);
				response.getWriter().write(outputAccList);
			}
			else if(action.equals("frmAcntSelection") || action.equals("toAcntSelection"))
			{
				//Attributes initialization..
				String outputAcntDetails = "";
				HashMap<String, JSONArray> accBalPlatingDtls = new HashMap<String, JSONArray>();
				JSONObject jsonReturnObj = new JSONObject();

				//Ajax attributes Mappings...
				AjaxAttributes ajaxParams = new AjaxAttributes();
				ajaxParams.setAccountRefID(selectedAccRefID);
				ajaxParams.setTransferType(transferType);
				ajaxParams.setDebitCreditInd(debitCreditInd);

				//RTAB call..
				if(isRTABReq!=null && isRTABReq.equalsIgnoreCase("Y")){
					ajaxParams.setJsonAccBalMapStr("AccountBalance");
					callAccountBalance(session,objUserPrincipal,accBalPlatingDtls,ajaxParams);
				}

				//Account plating call...
				if(isAcntPlatingReq!=null && isAcntPlatingReq.equalsIgnoreCase("Y")){
					ajaxParams.setJsonAcntPayeeMapStr("AccountPayeeTo");
					ajaxParams.setJsonAcntPlateMapStr("AccountPlating");
					callAccountPlating(session,objUserPrincipal,accBalPlatingDtls,ajaxParams);
				}

				//Account Details call for Internal MS Accounts..
				getAccountDetails(objUserPrincipal, accBalPlatingDtls, ajaxParams);

				//IRA Account check flag for the transactions other than Internal...
				if(transferType!=null && (!transferType.equalsIgnoreCase(TxnTypeCode.INTERNAL) || !transferType.equalsIgnoreCase(TxnTypeCode.PORTFOLIO_LOAN))){
					getIRATxnTypeDesc(objUserPrincipal,accBalPlatingDtls,ajaxParams);
				}

				//Forming the JSON Object ...
				jsonReturnObj=JSONObject.fromObject(accBalPlatingDtls);
				outputAcntDetails =jsonReturnObj.toString();
				response.getWriter().write(outputAcntDetails);
			}
			else if(action.equals("editOnLoadDetails")) 
			{
				//Attributes initialization..
				String outputAcntDetails = "";
				HashMap<String, JSONArray> accBalPlatingDtls = new HashMap<String, JSONArray>();
				JSONObject jsonReturnObj = new JSONObject();

				//Ajax attributes From account Mappings...
				AjaxAttributes ajaxFrmAccParams = new AjaxAttributes();
				ajaxFrmAccParams.setAccountRefID(fromAccountRefID);
				ajaxFrmAccParams.setOffice_account_fa(frm_office_account_fa);
				ajaxFrmAccParams.setTransferType(transferType);
				ajaxFrmAccParams.setAccountType(getFrom_AccType(transferType));

				//Ajax attributes To account Mappings...
				AjaxAttributes ajaxToAccParams = new AjaxAttributes();
				ajaxToAccParams.setAccountRefID(toAccountRefID);
				ajaxToAccParams.setOffice_account_fa(to_office_account_fa);
				ajaxToAccParams.setTransferType(transferType);
				ajaxToAccParams.setAccountType(getTo_AccType(transferType));

				//RTAB call..
				if(isRTABReq!=null && isRTABReq.equalsIgnoreCase("Y")){
					ajaxFrmAccParams.setJsonAccBalMapStr("AccountBalance");
					callAccountBalance(session,objUserPrincipal,accBalPlatingDtls,ajaxFrmAccParams);
				}

				//Account plating call...
				if(isAcntPlatingReq!=null && isAcntPlatingReq.equalsIgnoreCase("Y")){
					//Debit account details call...
					ajaxFrmAccParams.setJsonAcntPayeeMapStr("DebitAccountPayeeTo");
					ajaxFrmAccParams.setJsonAcntPlateMapStr("DebitAccountPlating");
					callAccountPlating(session,objUserPrincipal,accBalPlatingDtls,ajaxFrmAccParams);

					//Credit account details call only in case of INT and ACH Transfers...
					if(transferType!=null && !transferType.startsWith(ChkReqConstants.CHK)){
						ajaxToAccParams.setJsonAcntPayeeMapStr("CreditAccountPayeeTo");
						ajaxToAccParams.setJsonAcntPlateMapStr("CreditAccountPlating");
						callAccountPlating(session,objUserPrincipal,accBalPlatingDtls,ajaxToAccParams);
					}
				}

				//Account Details call for Internal MS Accounts..
				getAccountDetails(objUserPrincipal, accBalPlatingDtls, ajaxFrmAccParams);

				//Forming the JSON Object ...
				jsonReturnObj = JSONObject.fromObject(accBalPlatingDtls);
				outputAcntDetails = jsonReturnObj.toString();
				response.getWriter().write(outputAcntDetails);
			}
			else if(action.equals("otherMSAccountDetails")) 
			{
				//Attributes initialization..
				String outputAcntDetails = "";
				HashMap<String, JSONArray> accBalPlatingDtls = new HashMap<String, JSONArray>();
				JSONObject jsonReturnObj = new JSONObject();

				//Ajax attributes To account Mappings...
				AjaxAttributes ajaxToAccParams = new AjaxAttributes();
				ajaxToAccParams.setAccountRefID(toAccountRefID);
				ajaxToAccParams.setOffice_account_fa(to_office_account_fa);
				ajaxToAccParams.setTransferType(transferType);
				ajaxToAccParams.setAccountType(getTo_AccType(transferType));

				if(isAcntPlatingReq!=null && isAcntPlatingReq.equalsIgnoreCase("Y")) {
					//Credit account details call only in case of INT and ACH Transfers...
					ajaxToAccParams.setJsonAcntPayeeMapStr("CreditAccountPayeeTo");
					ajaxToAccParams.setJsonAcntPlateMapStr("CreditAccountPlating");
					callAccountPlating(session,objUserPrincipal,accBalPlatingDtls,ajaxToAccParams);
				}

				//Forming the JSON Object ...
				jsonReturnObj=JSONObject.fromObject(accBalPlatingDtls);
				outputAcntDetails =jsonReturnObj.toString();
				response.getWriter().write(outputAcntDetails);
			}
		}
		catch (Throwable objThrow) {
			objThrow.printStackTrace();
			// In case system encounters a technical failure on selection of Account , then user should be allowed to proceed..
			if(action.equals("INIT")){
				response.setStatus(response.SC_ACCEPTED ); //Setting the response to the 202 , if there are any Errors coming up
			}
		}

		elapsedTime = System.currentTimeMillis() - startTime;	
		if (performanceLogger.isInfoEnabled()) {
			performanceLogger.info("IP Address : " + request.getRemoteAddr() + " - Thread Id : " + Thread.currentThread().getId() + " - Session Id : " + sessionId + " - " + this.getClass().getName() + " - performTask(ActionMapping, ActionForm, HttpServletRequest, HttpServletResponse) - End - ElapsedTime : " + elapsedTime); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
		}
		return null;
	}

	/**
	 * Call to execute blocked display account list...
	 * @param session
	 * @param objUserPrincipal
	 * @param transferType
	 * @return
	 * @throws Exception
	 */
	public String callAccountList(HttpSession session,UserPrincipal objUserPrincipal,String transferType) throws Exception
	{
		try 
		{
			//Attributes initialization..
			ArrayList filteredAccounts = new ArrayList();
			JSONArray objFromAcc = new JSONArray();
			JSONArray objExternalAcc = new JSONArray();
			JSONArray objThirdPartyExtAcc = new JSONArray();
			JSONArray objLoanAcc = new JSONArray();
			JSONArray objToAcc = new JSONArray();
			PortfolioLoansService loanAcntDetails = null;
			ArrayList loansAcnts = new ArrayList();
			String outputAccList = "";
			HashMap<String, JSONArray> completeFilteredAccsList = new HashMap<String, JSONArray>();
			HashMap<String, JSONArray> completeMSAccsList = new HashMap<String, JSONArray>();
			GetAccountDetailsAjaxDelegate objGetAccountDetailsAjaxDelegate = new GetAccountDetailsAjaxDelegate(objUserPrincipal);

			Object accListInSession = session.getAttribute(transferType+"~MSAccounts");
			if(accListInSession==null)
			{
				filteredAccounts = objGetAccountDetailsAjaxDelegate.getFilteredAccList(transferType);

				//Setting INT From Accounts in session after converting to JSON Array..
				if(!((JSONArray)filteredAccounts.get(0)).isEmpty()){
					objFromAcc=(JSONArray)filteredAccounts.get(0); 
				}
				if(!((JSONArray)filteredAccounts.get(1)).isEmpty()){	
					objToAcc=(JSONArray)filteredAccounts.get(1); 
				}
				if(!((JSONArray)filteredAccounts.get(2)).isEmpty()){
					objExternalAcc=(JSONArray)filteredAccounts.get(2);  
				}
				if(!((JSONArray)filteredAccounts.get(3)).isEmpty()){
					objThirdPartyExtAcc=(JSONArray)filteredAccounts.get(3);  
				}
				if(!((JSONArray)filteredAccounts.get(4)).isEmpty()){
					objLoanAcc=(JSONArray)filteredAccounts.get(4);  
				}
				if((PortfolioLoansService)filteredAccounts.get(5)!=null){
					loanAcntDetails =(PortfolioLoansService)filteredAccounts.get(5); 
					loansAcnts = loanAcntDetails.getLoanAcntsList();
				}

				//Adding all the details in the Hash Map and setting the same in the output response..
				completeFilteredAccsList.put("fromAccounts",objFromAcc);
				completeFilteredAccsList.put("toAccounts",objToAcc);
				completeFilteredAccsList.put("externalAccounts",objExternalAcc);
				completeFilteredAccsList.put("thirdPartyExtAccounts",objThirdPartyExtAcc);
				completeFilteredAccsList.put("loanAccounts",objLoanAcc);

				JSONObject jsonCompleteAccsObject = JSONObject.fromObject(completeFilteredAccsList);  
				outputAccList=jsonCompleteAccsObject.toString();

				//Adding only the internal account details in the session Hash Map so as to maintain consistency..
				completeMSAccsList.put("fromAccounts",objFromAcc);
				completeMSAccsList.put("toAccounts",objToAcc);
				completeMSAccsList.put("loanAccounts",objLoanAcc);

				//Storing the Internal MSSB + PLA accounts in the session....
				if(transferType!=null && transferType.equalsIgnoreCase(TxnTypeCode.ACH_TYPE)){
					session.setAttribute(transferType+"~MSAccounts",completeMSAccsList);
				}
				else {
					session.setAttribute(transferType+"~MSAccounts",completeFilteredAccsList);
				}

				//Storing the PLA response object containing the Outstanding Balance in the session ..
				if(loansAcnts!=null && !loansAcnts.isEmpty()) {
					for(int a=0; a < loansAcnts.size(); a++) {
						PortfolioLoanAccount loanAcnt = (PortfolioLoanAccount)loansAcnts.get(a);
						if(loanAcnt!=null) {
							session.setAttribute("PLADetails~"+loanAcnt.getLoanAccount(),loanAcnt);
						}
					}
				}
			}
			else 
			{
				//Attributes initialization..
				JSONObject objAccountList= new  JSONObject();
				if(transferType!=null && transferType.equalsIgnoreCase(TxnTypeCode.ACH_TYPE))
				{
					if(accListInSession!=null && accListInSession instanceof HashMap)
					{
						HashMap<String, JSONArray> sessionMSAccMap = (HashMap)accListInSession;
						completeFilteredAccsList = (HashMap)sessionMSAccMap.clone(); // Cloning is done to maintain the session data from any change...

						//Extracting all accounts...
						filteredAccounts = objGetAccountDetailsAjaxDelegate.getExtAccountsList(); //Getting only the external accounts newly....
						if(!((JSONArray)filteredAccounts.get(0)).isEmpty()){
							objExternalAcc=(JSONArray)filteredAccounts.get(0);  
						}
						if(!((JSONArray)filteredAccounts.get(1)).isEmpty()){
							objThirdPartyExtAcc=(JSONArray)filteredAccounts.get(1);  
						}
						completeFilteredAccsList.put("externalAccounts",objExternalAcc);
						completeFilteredAccsList.put("thirdPartyExtAccounts",objThirdPartyExtAcc);

						objAccountList = JSONObject.fromObject(completeFilteredAccsList);  
						outputAccList = objAccountList.toString();
					}
				}
				else {
					if(accListInSession!=null && accListInSession instanceof HashMap){
						objAccountList = JSONObject.fromObject(accListInSession); 
					}
					outputAccList=(String)objAccountList.toString();
				}
			}
			return outputAccList;
		} 
		catch (Exception exception) {
			exception.printStackTrace();
			//Throw and catch the exception since in case of the Account filter BR failure , user can't proceed..
			throw exception;
		}

	}
	/**
	 * Call to execute the account balance....
	 * @param session
	 * @param account
	 * @param accBalPlatingDtls
	 * @param objUserPrincipal
	 * @throws Exception
	 */
	public void callAccountBalance(HttpSession session,UserPrincipal objUserPrincipal,HashMap accBalPlatingDtls,AjaxAttributes ajaxAccParams) throws Exception
	{
		try 
		{
			GetAccountDetailsAjaxDelegate objAccDetails = new GetAccountDetailsAjaxDelegate(objUserPrincipal);
			Object objOutput = new Object();
			String account = ajaxAccParams.getAccountRefID();
			String jsonAccBalMapStr = ajaxAccParams.getJsonAccBalMapStr();

			Object isRTABOutInSession = session.getAttribute("RTAB~"+account);
			if(isRTABOutInSession==null)
			{
				objOutput = objAccDetails.getAccountBalance(ajaxAccParams);
				if(objOutput!=null)
				{
					ArrayList rtabOutDetails = (ArrayList)objOutput;
					if(!(rtabOutDetails).isEmpty())
					{
						HashMap txnOutDetails = (HashMap)rtabOutDetails.get(1);
						AccountBalance objAccBalance = new AccountBalance();
						if(txnOutDetails.containsKey("RTABOutputDetails")){
							objAccBalance = (AccountBalance)txnOutDetails.get("RTABOutputDetails");
						}

						JSONArray jsonRTABOutDetails = new JSONArray();
						jsonRTABOutDetails = JSONArray.fromObject(objAccBalance.getInfoRTAB());
						accBalPlatingDtls.put(jsonAccBalMapStr, jsonRTABOutDetails);
					}
				}
			}
			else {
				JSONArray jsonRTABOutDetails = (JSONArray)isRTABOutInSession; 
				accBalPlatingDtls.put(jsonAccBalMapStr, jsonRTABOutDetails);
			}
		} 
		catch (Exception exception) {
			//No need to block the user even in case of failure , hence no need to throw the exception back..
			exception.printStackTrace();
		}
	}

	/**
	 * Call to execute the account plating details ...
	 * @param session
	 * @param account
	 * @param accBalPlatingDtls
	 * @param objUserPrincipal
	 * @throws Exception 
	 */
	public void callAccountPlating(HttpSession session,UserPrincipal objUserPrincipal,HashMap accBalPlatingDtls,AjaxAttributes ajaxAccParams) throws Exception 
	{
		try 
		{
			GetAccountDetailsAjaxDelegate objAccDetails = new GetAccountDetailsAjaxDelegate(objUserPrincipal);
			Object objOutput = new Object();
			String account = ajaxAccParams.getAccountRefID();
			String jsonAcntPlateMapStr = ajaxAccParams.getJsonAcntPlateMapStr();
			String jsonAcntPayeeMapStr = ajaxAccParams.getJsonAcntPayeeMapStr();

			//Setting the PLA Response attributes in the ajaxAccParam Input (If any)..
			if(account!=null && account.startsWith(MSSystemDefaults.LOAN_ACC_IND_REGX)){
				String loanAcnt = account.split(MSSystemDefaults.LOAN_ACC_IND_REGX)[1];
				Object loanDetails = session.getAttribute("PLADetails~"+loanAcnt);
				ajaxAccParams.setAcntDetails(loanDetails);
			}

			//Service invoke ..
			Object accPlatingAttrInSession = session.getAttribute("Acnt_Plating~"+account);
			Object accPayeeAttrInSession = session.getAttribute("Acnt_Payee~"+account);
			if(accPlatingAttrInSession==null || accPayeeAttrInSession==null)
			{
				objOutput=objAccDetails.getAccPlatingAttributes(ajaxAccParams);
				if(objOutput!=null)
				{
					ArrayList onSelectAccPlatingDtls = (ArrayList)objOutput;
					if(!(onSelectAccPlatingDtls).isEmpty())
					{
						JSONArray jsonAcntPlatingDetails = new JSONArray();
						JSONArray jsonAcntPayeeDetails = new JSONArray();

						//Internal Account plating details if applicable..
						HashMap txnOutDetails = (HashMap)onSelectAccPlatingDtls.get(1);
						AccountPlating acntPlating = new AccountPlating();
						if(txnOutDetails.containsKey("AcntPlatingOutputDetails")){
							acntPlating = (AccountPlating)txnOutDetails.get("AcntPlatingOutputDetails");
							jsonAcntPlatingDetails = JSONArray.fromObject(acntPlating.getAcntPlatingMap());
							jsonAcntPayeeDetails = JSONArray.fromObject(acntPlating.getAcntPayeeMap());
						}

						//External account plating details if applicable..
						ArrayList extAcntPlating = new ArrayList();
						if(txnOutDetails.containsKey("ExtAcntPlatingOutputDetails")){
							extAcntPlating = (ArrayList)txnOutDetails.get("ExtAcntPlatingOutputDetails");
							jsonAcntPlatingDetails = JSONArray.fromObject(extAcntPlating);
						}

						//Loan account plating details if applicable..
						ArrayList loanAcntPlating = new ArrayList();
						if(txnOutDetails.containsKey("LoanAcntPlatingOutputDetails")){
							loanAcntPlating = (ArrayList)txnOutDetails.get("LoanAcntPlatingOutputDetails");
							jsonAcntPlatingDetails = JSONArray.fromObject(loanAcntPlating);
						}

						//Populating the HashMap..
						accBalPlatingDtls.put(jsonAcntPlateMapStr, jsonAcntPlatingDetails);
						accBalPlatingDtls.put(jsonAcntPayeeMapStr, jsonAcntPayeeDetails);

						//Storing in the session..
						session.setAttribute("Acnt_Plating~"+account,jsonAcntPlatingDetails);
						session.setAttribute("Acnt_Payee~"+account,jsonAcntPayeeDetails);
						session.setAttribute("Acnt_Plating_Object~"+account, acntPlating);
					}
				}
			}
			else  {
				JSONArray jsonAcntPlatingDetails = (JSONArray)accPlatingAttrInSession;
				JSONArray jsonAcntPayeeDetails = (JSONArray)accPayeeAttrInSession;

				accBalPlatingDtls.put(jsonAcntPlateMapStr, jsonAcntPlatingDetails);
				accBalPlatingDtls.put(jsonAcntPayeeMapStr, jsonAcntPayeeDetails);
			}
		} 
		catch (Exception exception) {
			//No need to block the user even in case of failure , hence no need to throw the exception back..
			exception.printStackTrace();
		}
	}

	/**
	 * Gets the Account Details for the selected account in case transfer type is Check (From Merlin response)...
	 * @param objUserPrincipal
	 * @param accBalPlatingDtls
	 * @param ajaxAccParams
	 * @throws Exception 
	 */
	public void getAccountDetails(UserPrincipal objUserPrincipal,HashMap accBalPlatingDtls,AjaxAttributes ajaxAccParams) throws Exception
	{
		try 
		{
			GetAccountDetailsAjaxDelegate objAccDetails = new GetAccountDetailsAjaxDelegate(objUserPrincipal);
			JSONObject jsonAccDefaultAdd = new JSONObject();
			JSONArray jsonAccPlatingAdd = new JSONArray();
			JSONObject jsonClientDtls = new JSONObject();

			ArrayList accountInfo = objAccDetails.populateAccountInfo(ajaxAccParams);
			if(accountInfo!=null)
			{
				//Default client Address...
				HashMap defaultAccAddrs = (HashMap)accountInfo.get(0);
				if(defaultAccAddrs!=null && !defaultAccAddrs.isEmpty()){
					jsonAccDefaultAdd = JSONObject.fromObject(defaultAccAddrs);
					accBalPlatingDtls.put("AcntDefaultAddress", jsonAccDefaultAdd);
				}

				//Account Plating address...
				ArrayList acntPlatingList = (ArrayList)accountInfo.get(1);
				if(acntPlatingList!=null && !acntPlatingList.isEmpty()){
					jsonAccPlatingAdd = JSONArray.fromObject(acntPlatingList);
					accBalPlatingDtls.put("AcntPlatingAddress", jsonAccPlatingAdd);
				}

				//Client Details...
				HashMap clientDtls = (HashMap)accountInfo.get(2);
				if(clientDtls!=null && !clientDtls.isEmpty()){
					jsonClientDtls = JSONObject.fromObject(clientDtls);
					accBalPlatingDtls.put("ClientDetails", jsonClientDtls);
				}

				//Account Category...
				String acntCategory = (String)accountInfo.get(3);
				if(acntCategory!=null){
					accBalPlatingDtls.put("AcntCategory", acntCategory);
				}
			}
		} 
		catch (Exception exception) {
			//No need to block the user even in case of failure , hence no need to throw the exception back..
			exception.printStackTrace();
		}
	}

	/**
	 * Gets the IRA Account flag for the selected account...
	 * @param objUserPrincipal
	 * @param accBalPlatingDtls
	 * @param ajaxAccParams
	 * @throws Exception 
	 */
	public void getIRATxnTypeDesc(UserPrincipal objUserPrincipal,HashMap accBalPlatingDtls,AjaxAttributes ajaxAccParams) throws Exception
	{
		try 
		{
			GetAccountDetailsAjaxDelegate objAccDetails = new GetAccountDetailsAjaxDelegate(objUserPrincipal);
			JSONArray iraTransferTypes = objAccDetails.getIRATransferType(ajaxAccParams);
			if(iraTransferTypes!=null && !iraTransferTypes.isEmpty()){
				accBalPlatingDtls.put("IRATransferTypes", iraTransferTypes);
			}
		} 
		catch (Exception exception) {
			//No need to block the user even in case of failure , hence no need to throw the exception back..
			exception.printStackTrace();
		}
	}

	/**
	 * Gets the From account type of the account involved in transaction...
	 * @param objPaymentDetails
	 * @return
	 */
	public static String getFrom_AccType(String transferType)
	{
		String frm_AccType = "";
		if(transferType!=null && (transferType.equalsIgnoreCase(TxnTypeCode.INTERNAL) || transferType.startsWith(ChkReqConstants.CHK))){
			frm_AccType = TxnTypeCode.INTERNAL;
		}
		else if(transferType!=null && transferType.equalsIgnoreCase(TxnTypeCode.ACH_WITHDRAWAL)){
			frm_AccType = TxnTypeCode.INTERNAL;
		}
		else if(transferType!=null && transferType.equalsIgnoreCase(TxnTypeCode.ACH_DEPOSIT)){
			frm_AccType = "EXT";
		}
		else if(transferType!=null && transferType.equalsIgnoreCase(TxnTypeCode.PORTFOLIO_LOAN)){
			frm_AccType = TxnTypeCode.INTERNAL;
		}
		return frm_AccType;
	}

	/**
	 * Gets the To account type of the account involved in transaction...
	 * @param objPaymentDetails
	 * @return
	 */
	public static String getTo_AccType(String transferType)	
	{
		String to_AccType = "";
		if(transferType!=null && (transferType.equalsIgnoreCase(TxnTypeCode.INTERNAL))){
			to_AccType = TxnTypeCode.INTERNAL;
		}
		else if(transferType!=null && transferType.equalsIgnoreCase(TxnTypeCode.ACH_WITHDRAWAL)){
			to_AccType = "EXT";
		}
		else if(transferType!=null && transferType.equalsIgnoreCase(TxnTypeCode.ACH_DEPOSIT)){
			to_AccType = TxnTypeCode.INTERNAL;
		}
		else if(transferType!=null && transferType.equalsIgnoreCase(TxnTypeCode.PORTFOLIO_LOAN)){
			to_AccType = TxnTypeCode.PORTFOLIO_LOAN;
		}
		return to_AccType;
	}
}
