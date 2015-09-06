package com.tcs.ebw.payments.businessdelegate;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Vector;

import net.sf.json.JSONArray;

import org.apache.struts.util.LabelValueBean;
import com.tcs.Payments.EAITO.BUS_RULE_INP_CRTL;
import com.tcs.Payments.EAITO.BUS_RUL_TXN_INP;
import com.tcs.Payments.EAITO.MS_ACCOUNT_OUT_DTL;
import com.tcs.Payments.ms360Utils.Bus_Rule_Input_Desc;
import com.tcs.Payments.ms360Utils.InitiatorRoleDesc;
import com.tcs.Payments.ms360Utils.MSCommonUtils;
import com.tcs.Payments.ms360Utils.MSSystemDefaults;
import com.tcs.Payments.ms360Utils.TxnTypeCode;
import com.tcs.bancs.channels.context.MessageType;
import com.tcs.bancs.channels.context.ServiceContext;
import com.tcs.bancs.channels.integration.MMAccount;
import com.tcs.bancs.channels.integration.MMContext;
import com.tcs.ebw.businessdelegate.EbwBusinessDelegate;
import com.tcs.ebw.common.context.EBWAppContext;
import com.tcs.ebw.common.util.EBWLogger;
import com.tcs.ebw.common.util.SessionUtil;
import com.tcs.ebw.payments.transferobject.AjaxAttributes;
import com.tcs.ebw.payments.transferobject.FromMSAcc_DetailsTO;
import com.tcs.ebw.payments.transferobject.MSUser_DetailsTO;
import com.tcs.ebw.payments.transferobject.PaymentDetailsTO;
import com.tcs.ebw.payments.transferobject.PortfolioLoanAccount;
import com.tcs.ebw.serverside.factory.EBWServiceFactory;
import com.tcs.ebw.serverside.factory.IEBWService;
import com.tcs.ebw.serverside.jaas.principal.UserPrincipal;
import com.tcs.ebw.serverside.services.channelPaymentServices.BusinessRulesService;
import com.tcs.ebw.serverside.services.channelPaymentServices.PortfolioLoansService;
import com.tcs.ebw.serverside.services.channelPaymentServices.WSDefaultInputsMap;

/**
 * @author : 224703
 * **************   Revision History ************************
 * Modified-By |  Modified-Date |  Project-Phase  | Changes
 *
 * **********************************************************
 */
public class GetAccountDetailsAjaxDelegate extends EbwBusinessDelegate{

	/**
	 * Constructor of ExternalTransferDelegate class. 
	 */ 

	public GetAccountDetailsAjaxDelegate(UserPrincipal objUserPrincipal) {
		this.objUserPrincipal = objUserPrincipal; 
	}

	/** 
	 * The Account balance(RTAB) is retrieved on selection of the Internal account.
	 * @param fromAccount
	 * @return
	 * @throws Exception
	 */
	public Object getAccountBalance(AjaxAttributes ajaxAccParams) throws Exception 
	{
		EBWLogger.trace(this, "Starting getAccountDetailsAjax_onAccSelection()"); 
		EBWLogger.trace(this, "Service name       : getSelectedAccountDetails"); 
		try 
		{
			boolean isExternalAccount=true;
			MMContext objMMContextData = objUserPrincipal.getContextData();
			ArrayList objMMContextAcc = objMMContextData.getAccounts();
			MSCommonUtils objCommonUtils = new MSCommonUtils();
			Object objOutput = null;
			String strStatement = "";
			boolean isAccntInContext  = false;
			String acntType = ajaxAccParams.getAccountType();
			String selectedRefId = ajaxAccParams.getAccountRefID();

			//Mapping the User and From account details...
			MSUser_DetailsTO objMSDetails = objCommonUtils.setMSUserDetailsTO(objMMContextData);
			FromMSAcc_DetailsTO objFromMSAcc_DetailsTO = new FromMSAcc_DetailsTO();

			//Reference account in context...
			for(int i=0;i<objMMContextData.getAccounts().size();i++){
				MMAccount objMMAccount = (MMAccount)objMMContextAcc.get(i);
				objMMAccount = getMSAccFormat(objMMAccount); //Formatting the FA,Office,Account Number ...
				if((objMMAccount.getKeyAccount()!=null && objMMAccount.getKeyAccount().equalsIgnoreCase(selectedRefId)))
				{
					objFromMSAcc_DetailsTO = objCommonUtils.setFromMSAccDetailsTO(objMMAccount);
					isExternalAccount=false;
					isAccntInContext = true;
					break;
				}
			}

			//Reference account not in context during edit ...
			if(!isAccntInContext) {
				if(acntType!=null && acntType.equalsIgnoreCase(TxnTypeCode.INTERNAL)){
					if(ajaxAccParams.getOffice_account_fa()!=null){
						objFromMSAcc_DetailsTO.setAccountNumber(ajaxAccParams.getOffice_account_fa().substring(3,9));
						objFromMSAcc_DetailsTO.setOfficeNumber(ajaxAccParams.getOffice_account_fa().substring(0,3));
						objFromMSAcc_DetailsTO.setFaNumber(ajaxAccParams.getOffice_account_fa().substring(9));
						isExternalAccount=false;
					}
				}
			}

			Object objTOParam[] = {objMSDetails,objFromMSAcc_DetailsTO};
			Object objParams[]={strStatement,objTOParam,new Boolean(false)};
			Class clsParamTypes[]={String.class,Object[].class,Boolean.class};

			//Service Call.....
			if(!isExternalAccount)
			{
				EBWAppContext appContext=new EBWAppContext();
				appContext.setUserPrincipal((UserPrincipal)SessionUtil.get("UserPrincipal"));

				//Service call...
				IEBWService objService = EBWServiceFactory.create("getSelectedAccountDetails",appContext);
				objOutput = objService.execute(clsParamTypes, objParams);
				if(objOutput!=null)
				{
					ArrayList onSelectAccBalance = (ArrayList)objOutput;
					ServiceContext contextData = (ServiceContext)onSelectAccBalance.get(0);
					if(contextData.getMaxSeverity()==MessageType.CRITICAL || contextData.getMaxSeverity()==MessageType.SEVERE){
						String errorMessage = MSCommonUtils.extractContextErrMessage(contextData);
						throw new Exception(errorMessage);
					}
				}
			}
			EBWLogger.trace(this, "Finished getAccountDetailsAjax_onAccSelection()"); 
			return objOutput;
		} 
		catch (Exception exception) {
			throw exception;
		}
	}

	/**
	 * Gets the filter account list mappings..
	 * @param transferType
	 * @return
	 * @throws Exception
	 */
	public ArrayList<Object> getFilteredAccList(String transferType) throws Exception 
	{
		EBWLogger.trace(this, "Starting getAccountDetailsAjax_INIT_iNIT()"); 
		EBWLogger.trace(this, "Service name       : getOnLoadAccDetailsAjax"); 
		try
		{
			Object objOutput = null;
			String userRole=null;
			ArrayList<String> getContextAccounts = new ArrayList<String>();
			ArrayList<Object> getFromAccounts = new ArrayList<Object>();
			ArrayList<Object> getToAccounts = new ArrayList<Object>();
			ArrayList<Object> getExternalAccounts = new ArrayList<Object> ();
			ArrayList<Object> getThirdPartyExtAccounts = new ArrayList<Object> ();
			ArrayList<Object> getLoanAccounts = new ArrayList<Object> ();
			ArrayList<Object> getCompleteAccountDetails = new ArrayList<Object>();
			String strStatement = "";
			MMContext objMMContextData = objUserPrincipal.getContextData();
			ArrayList objMMContextAcc = objMMContextData.getAccounts();
			boolean isFA= objMMContextData.isFA();
			if(isFA)
				userRole="FA";
			else
				userRole="Client";

			//Service Mappings 
			ArrayList<Object> onLoadAccDetailsTO= new ArrayList<Object>();
			int j=0;

			//MS User details mappings...
			MSCommonUtils objCommonUtils = new MSCommonUtils();
			MSUser_DetailsTO objMSDetails = objCommonUtils.setMSUserDetailsTO(objMMContextData);
			ServiceContext serviceContext = new ServiceContext();

			// Business Rule Input Control Mappings ...
			BUS_RULE_INP_CRTL objBUS_RULE_INP_CRTL = new BUS_RULE_INP_CRTL();
			objBUS_RULE_INP_CRTL.setUSER_ID(objMMContextData.getLoginId());
			objBUS_RULE_INP_CRTL.setGROUP(InitiatorRoleDesc.Client); //Needs to be changed according to the Context XML.
			objBUS_RULE_INP_CRTL.setROLE(userRole);
			objBUS_RULE_INP_CRTL.setAPPL_ID(WSDefaultInputsMap.getBusRuleCntrlAppID(new Boolean(false),serviceContext));
			objBUS_RULE_INP_CRTL.setSERVER_IP(WSDefaultInputsMap.getBusRuleCntrlServerID(new Boolean(false),serviceContext));

			// Business Rule Input Transaction Mappings ...
			BUS_RUL_TXN_INP objBUS_RUL_TXN_INP = new BUS_RUL_TXN_INP();
			if(transferType!=null && transferType.equalsIgnoreCase(TxnTypeCode.INTERNAL)){
				objBUS_RUL_TXN_INP.setTYPE(Bus_Rule_Input_Desc.TYPE_INT);
				objBUS_RUL_TXN_INP.setPAGE_TYPE(Bus_Rule_Input_Desc.CS_INT);
			}
			else if(transferType!=null && transferType.equalsIgnoreCase(TxnTypeCode.PORTFOLIO_LOAN)){
				objBUS_RUL_TXN_INP.setTYPE(Bus_Rule_Input_Desc.TYPE_PORTFOLIO_LOAN);
				objBUS_RUL_TXN_INP.setPAGE_TYPE(Bus_Rule_Input_Desc.CS_INT);
			}
			else {
				objBUS_RUL_TXN_INP.setTYPE(Bus_Rule_Input_Desc.TYPE_ACH);
				objBUS_RUL_TXN_INP.setPAGE_TYPE(Bus_Rule_Input_Desc.CS_ACH);
			}
			objBUS_RUL_TXN_INP.setACTION(Bus_Rule_Input_Desc.Create); 
			objBUS_RUL_TXN_INP.setRULE_TYPE(Bus_Rule_Input_Desc.RULE_TYPE_BLCK_DISP);  

			onLoadAccDetailsTO.add(objBUS_RULE_INP_CRTL);
			onLoadAccDetailsTO.add(objBUS_RUL_TXN_INP);

			for(int i =0; i<objMMContextData.getAccounts().size();i++)
			{
				MMAccount objMMAccount = (MMAccount)objMMContextAcc.get(i);
				objMMAccount = getMSAccFormat(objMMAccount); //Formatting the FA,Office,Account Number ...

				//Business Rule Input Mappings (Merlin Input)...
				MS_ACCOUNT_OUT_DTL objMS_ACCOUNT_OUT_DTL = new MS_ACCOUNT_OUT_DTL();
				objMS_ACCOUNT_OUT_DTL.setOFFICE(objMMAccount.getOfficeNumber());
				objMS_ACCOUNT_OUT_DTL.setACCOUNT_NO(objMMAccount.getAccountNumber());
				objMS_ACCOUNT_OUT_DTL.setFA(objMMAccount.getFaNumber());
				objMS_ACCOUNT_OUT_DTL.setDIVPAY(objMMAccount.getDivPay());
				objMS_ACCOUNT_OUT_DTL.setSTATUS(objMMAccount.getStatus());
				objMS_ACCOUNT_OUT_DTL.setACNT_CLS(objMMAccount.getAccountClass());
				objMS_ACCOUNT_OUT_DTL.setCHOICE_FUND_CODE(objMMAccount.getChoiceFundCode());
				objMS_ACCOUNT_OUT_DTL.setIRACODE(objMMAccount.getIraCode());
				objMS_ACCOUNT_OUT_DTL.setCLNT_CAT(objMMAccount.getClientCategory());
				objMS_ACCOUNT_OUT_DTL.setNVS_SUB_PROD(objMMAccount.getNovusSubProduct());
				objMS_ACCOUNT_OUT_DTL.setTRADCNTRL(objMMAccount.getTradeControl());
				objMS_ACCOUNT_OUT_DTL.setACC_CATEGORY(objMMAccount.getAccountCategory());
				objMS_ACCOUNT_OUT_DTL.setCOLLATERAL_ACC_IND(objMMAccount.getCollateralAcctInd());

				onLoadAccDetailsTO.add(objMS_ACCOUNT_OUT_DTL);
			}

			Object[] objTOParam = {objMSDetails,onLoadAccDetailsTO};

			Object objParams[]={strStatement,objTOParam,new Boolean(false),transferType};
			Class clsParamTypes[]={String.class,Object[].class,Boolean.class,String.class};

			//Service Call.....
			EBWAppContext appContext=new EBWAppContext();
			appContext.setUserPrincipal((UserPrincipal)SessionUtil.get("UserPrincipal"));

			IEBWService objService = EBWServiceFactory.create("getOnLoadAccDetailsAjax",appContext);
			objOutput = objService.execute(clsParamTypes, objParams);

			if(objOutput!=null)
			{
				ArrayList<Object> onLoadAccOut = (ArrayList<Object>)objOutput;
				ServiceContext contextData = (ServiceContext)onLoadAccOut.get(0);
				if(contextData.getMaxSeverity()==MessageType.CRITICAL){
					String errorMessage = MSCommonUtils.extractContextErrMessage(contextData);
					throw new Exception(errorMessage);
				}
				else
				{
					HashMap txnOutDetails = (HashMap)onLoadAccOut.get(1);
					BusinessRulesService objBusinessRulesService = new BusinessRulesService();
					ArrayList blockedAccList = new ArrayList();
					if(txnOutDetails.containsKey("BlockedDispOutputDetails")){
						objBusinessRulesService = (BusinessRulesService)txnOutDetails.get("BlockedDispOutputDetails");
						blockedAccList=objBusinessRulesService.getBlockedAccountDetails();
					}

					//Loans Account response..
					PortfolioLoansService objPortfolioLoansService = new PortfolioLoansService();
					ArrayList loansAcnts = new ArrayList();
					if(txnOutDetails.containsKey("PLACreditLoans")){
						objPortfolioLoansService = (PortfolioLoansService)txnOutDetails.get("PLACreditLoans");
						loansAcnts = objPortfolioLoansService.getLoanAcntsList();
					}

					//Same Name external accounts..
					Object externalAccounts = null;
					if(txnOutDetails.containsKey("ExternalAccountsList")){
						externalAccounts = txnOutDetails.get("ExternalAccountsList");
					}

					//Non-Same Name external accounts..
					Object thirdPartyExtAccounts = null;
					if(txnOutDetails.containsKey("ThirdPartyExtAccountsList")){
						thirdPartyExtAccounts = txnOutDetails.get("ThirdPartyExtAccountsList");
					}

					//Putting all the context accounts in the ArrayList ....
					for(int a = 0; a < objMMContextData.getAccounts().size(); a++){
						MMAccount objMMAccount = (MMAccount)objMMContextAcc.get(a);
						objMMAccount = getMSAccFormat(objMMAccount); //Formatting the FA,Office,Account Number ...
						getContextAccounts.add(objMMAccount.getAccountNumber());
						getFromAccounts.add(objMMAccount.getAccountNumber());
						getToAccounts.add(objMMAccount.getAccountNumber());
					}

					//Putting all the PLA Loan Accounts in the ArrayList ....
					for(int a=0; a < loansAcnts.size(); a++){
						PortfolioLoanAccount loanAcnt = (PortfolioLoanAccount)loansAcnts.get(a);
						getLoanAccounts.add(loanAcnt.getLoanAccount());
					}

					//Populating the From and To accounts respectively with valid account number...
					for(j=0;j<blockedAccList.size();j++)
					{
						ArrayList blockedAccVal= (ArrayList)blockedAccList.get(j);
						for(int k=0;k<getContextAccounts.size();k++)
						{
							if(getContextAccounts.contains(blockedAccVal.get(0)) && (blockedAccVal.get(1).equals("1"))){
								getFromAccounts.remove(blockedAccVal.get(0));
								break;
							}
							if(getContextAccounts.contains(blockedAccVal.get(0)) && (blockedAccVal.get(1).equals("2"))){
								getToAccounts.remove(blockedAccVal.get(0));
								break;
							}
							if(getContextAccounts.contains(blockedAccVal.get(0)) && (blockedAccVal.get(1).equals("3"))){
								getFromAccounts.remove(blockedAccVal.get(0));
								getToAccounts.remove(blockedAccVal.get(0));
								break;
							}
						}
					}

					//Creating From and To account arrayList , where each account contains , its key account number and nickname. 
					ArrayList<Object> getFrmAccsKeyName = new ArrayList<Object>();
					ArrayList<Object> getToAccsKeyName = new ArrayList<Object>();
					for(int a=0;a<objMMContextData.getAccounts().size();a++)
					{
						MMAccount objMMAccount = (MMAccount)objMMContextAcc.get(a);
						objMMAccount = getMSAccFormat(objMMAccount); //Formatting the FA,Office,Account Number ...
						ArrayList frmAccDispVal = new ArrayList();
						ArrayList toAccDispVal = new ArrayList();

						if(getFromAccounts.contains(objMMAccount.getAccountNumber())){
							frmAccDispVal.add(objMMAccount.getKeyAccount());
							if(objMMAccount.getNickName()!=null && !objMMAccount.getNickName().trim().equalsIgnoreCase("")){
								frmAccDispVal.add(objMMAccount.getNickName());
							}
							else {
								frmAccDispVal.add(objMMAccount.getFriendlyName());
							}
							getFrmAccsKeyName.add(frmAccDispVal);
						}
						if(getToAccounts.contains(objMMAccount.getAccountNumber())){
							toAccDispVal.add(objMMAccount.getKeyAccount());
							if(objMMAccount.getNickName()!=null && !objMMAccount.getNickName().trim().equalsIgnoreCase("")){
								toAccDispVal.add(objMMAccount.getNickName());
							}
							else {
								toAccDispVal.add(objMMAccount.getFriendlyName());
							}
							getToAccsKeyName.add(toAccDispVal);
						}
					}

					//From account data formatting..
					JSONArray jsonFromAccArray = formatComboData(getFrmAccsKeyName,MSSystemDefaults.DEFAULT_ACC_TEXT);   

					//To account data formatting..
					JSONArray jsonToAccArray = formatComboData(getToAccsKeyName,MSSystemDefaults.DEFAULT_ACC_TEXT); 

					//Adding the Filtered External Accounts (Display, Value ) inside a collection ... 
					JSONArray jsonExternalAccArray = new JSONArray();
					ArrayList<Object>  externalAccountValues = new ArrayList<Object> ();
					if(externalAccounts!=null)
					{
						ArrayList<Object>  extAccDetails = (ArrayList<Object>)externalAccounts;
						for( int i=1;i<extAccDetails.size();i++)
						{
							externalAccountValues=(ArrayList)extAccDetails.get(i);
							ArrayList<Object> getExtAccounts = new ArrayList<Object> ();
							String payeeNickName=(String)externalAccountValues.get(0);
							String paypayeeId=(String)externalAccountValues.get(1);
							getExtAccounts.add(paypayeeId);//External Account Number 
							getExtAccounts.add(payeeNickName);//External Nick Name 
							getExternalAccounts.add(getExtAccounts);
						}

						//External account data formatting..
						jsonExternalAccArray = formatComboData(getExternalAccounts,MSSystemDefaults.DEFAULT_ACC_TEXT); 
					}

					//Adding the Filtered Third Party External Accounts (Display, Value) inside a collection ... 
					JSONArray jsonThirdPartyExtAccArray = new JSONArray();
					ArrayList<Object>  thirdPartyExtAccountValues = new ArrayList<Object> ();
					if(thirdPartyExtAccounts!=null)
					{
						ArrayList<Object>  thirdPartyExtAccDetails = (ArrayList<Object>)thirdPartyExtAccounts;
						if(thirdPartyExtAccDetails.size()>1) //Checking if any third party accounts are present...
						{
							for( int i=1; i<thirdPartyExtAccDetails.size(); i++){
								thirdPartyExtAccountValues=(ArrayList)thirdPartyExtAccDetails.get(i);
								ArrayList<Object> getExtAccounts = new ArrayList<Object> ();
								String payeeNickName=(String)thirdPartyExtAccountValues.get(0);
								String paypayeeId=(String)thirdPartyExtAccountValues.get(1);
								getExtAccounts.add(paypayeeId);//Third Party External Account Reference Number 
								getExtAccounts.add(payeeNickName);//Third Party External Nick Name 
								getThirdPartyExtAccounts.add(getExtAccounts);
							}

							//Third Party External account data formatting..
							jsonThirdPartyExtAccArray = formatComboData(getThirdPartyExtAccounts,MSSystemDefaults.DEFAULT_ACC_TEXT); 
						}
					}

					//Adding the Filtered Portfolio Accounts (Display, Value) inside a collection ... 
					ArrayList<Object> loanAccountValues = new ArrayList<Object> ();
					if(!getLoanAccounts.isEmpty())
					{
						for(int a=0; a < objPortfolioLoansService.getLoanAcntsList().size(); a++)
						{
							PortfolioLoanAccount loanAcnt = (PortfolioLoanAccount)loansAcnts.get(a);
							loanAcnt = getLoanAccFormat(loanAcnt); //Formatting the Loan Account Number
							ArrayList loanAccDispVal = new ArrayList();
							if(getLoanAccounts.contains(loanAcnt.getLoanAccount())) {
								String accountDisplay = (formatLoanAccDisplay(loanAcnt));
								String accountValue = MSSystemDefaults.LOAN_ACC_IND_REGX+loanAcnt.getLoanAccount();
								loanAccDispVal.add(accountValue); //Account Form value..
								loanAccDispVal.add(accountDisplay); //Account Display name...
								loanAccountValues.add(loanAccDispVal);
							}
						}
					}

					//From account data formatting..
					JSONArray jsonLoanAccArray = formatComboData(loanAccountValues,MSSystemDefaults.DEFAULT_ACC_TEXT);   

					//Putting all the Accounts of the type JSON Array inside an ArrayList ...
					getCompleteAccountDetails.add(jsonFromAccArray);
					getCompleteAccountDetails.add(jsonToAccArray);
					getCompleteAccountDetails.add(jsonExternalAccArray);
					getCompleteAccountDetails.add(jsonThirdPartyExtAccArray);
					getCompleteAccountDetails.add(jsonLoanAccArray);
					getCompleteAccountDetails.add(objPortfolioLoansService);
				}
			}

			EBWLogger.trace(this, "Finished getAccountDetailsAjax_INIT_iNIT()"); 
			return getCompleteAccountDetails;
		} 
		catch (Exception exception) {
			throw exception;
		}
	}

	/**
	 * To get Only the external accounts on each external transfers screen load . 
	 * @throws Exception 
	 */
	public ArrayList getExtAccountsList() throws Exception
	{
		EBWLogger.trace(this, "Starting getExtAccountsList()"); 
		EBWLogger.trace(this, "Service name       : getExternalAccounts"); 
		ArrayList<JSONArray> getAllExternalAccounts = new ArrayList<JSONArray>();
		try 
		{
			Object objOutput = null;
			String strStatement = "";
			ArrayList<Object>  getExternalAccounts = new ArrayList<Object> ();
			ArrayList<Object>  getThirdPartyExtAccounts = new ArrayList<Object> ();
			MMContext objMMContextData = objUserPrincipal.getContextData();

			//MS User details mappings...
			MSCommonUtils objCommonUtils = new MSCommonUtils();
			MSUser_DetailsTO objMSUserDetails = objCommonUtils.setMSUserDetailsTO(objMMContextData);

			Object[] objTOParam = {objMSUserDetails};
			Object objParams[]={strStatement,objTOParam,new Boolean(false)};
			Class clsParamTypes[]={String.class,Object[].class,Boolean.class};

			//Application context...
			EBWAppContext appContext=new EBWAppContext();
			appContext.setUserPrincipal((UserPrincipal)SessionUtil.get("UserPrincipal"));

			//Service call..
			IEBWService objService = EBWServiceFactory.create("getAllExternalAccounts",appContext);
			objOutput = objService.execute(clsParamTypes, objParams);
			if(objOutput!=null)
			{
				ArrayList<Object> onLoadAccOut = (ArrayList<Object>)objOutput;
				ServiceContext contextData = (ServiceContext)onLoadAccOut.get(0);
				if(contextData.getMaxSeverity()==MessageType.CRITICAL){
					String errorMessage = MSCommonUtils.extractContextErrMessage(contextData);
					throw new Exception(errorMessage);
				}
				else
				{
					HashMap txnOutDetails = (HashMap)onLoadAccOut.get(1);

					//Same Name external accounts ...
					Object externalAccounts = null;
					if(txnOutDetails.containsKey("ExternalAccountsList")){
						externalAccounts = txnOutDetails.get("ExternalAccountsList");
					}

					//Non-Same Name external accounts..
					Object thirdPartyExtAccounts = null;
					if(txnOutDetails.containsKey("ThirdPartyExtAccountsList")){
						thirdPartyExtAccounts = txnOutDetails.get("ThirdPartyExtAccountsList");
					}

					//Adding the Filtered External Accounts (Display, Value ) inside a collection ... 
					JSONArray jsonExternalAccArray = new JSONArray();
					ArrayList<Object>  externalAccountValues = new ArrayList<Object> ();
					if(externalAccounts!=null)
					{
						ArrayList<Object>  extAccDetails = (ArrayList<Object>)externalAccounts;
						for( int i=1;i<extAccDetails.size();i++)
						{
							externalAccountValues=(ArrayList)extAccDetails.get(i);
							ArrayList<Object> getExtAccounts = new ArrayList<Object> ();
							String payeeNickName=(String)externalAccountValues.get(0);
							String paypayeeId=(String)externalAccountValues.get(1);
							getExtAccounts.add(paypayeeId);//External Account Ref Number 
							getExtAccounts.add(payeeNickName);//External Nick Name 
							getExternalAccounts.add(getExtAccounts);
						}

						//Combo data formatting..
						jsonExternalAccArray = formatComboData(getExternalAccounts,MSSystemDefaults.DEFAULT_ACC_TEXT); 
					}

					//Adding the Filtered Third Party External Accounts (Display, Value) inside a collection ... 
					JSONArray jsonThirdPartyExtAccArray = new JSONArray();
					ArrayList<Object>  thirdPartyExtAccountValues = new ArrayList<Object> ();
					if(thirdPartyExtAccounts!=null)
					{
						ArrayList<Object>  thirdPartyExtAccDetails = (ArrayList<Object>)thirdPartyExtAccounts;
						if(thirdPartyExtAccDetails.size()>1) //Checking if any third party accounts are present...
						{
							for( int i=1; i<thirdPartyExtAccDetails.size(); i++){
								thirdPartyExtAccountValues=(ArrayList)thirdPartyExtAccDetails.get(i);
								ArrayList<Object> getExtAccounts = new ArrayList<Object> ();
								String payeeNickName=(String)thirdPartyExtAccountValues.get(0);
								String paypayeeId=(String)thirdPartyExtAccountValues.get(1);
								getExtAccounts.add(paypayeeId);//Third Party External Account Reference Number 
								getExtAccounts.add(payeeNickName);//Third Party External Nick Name 
								getThirdPartyExtAccounts.add(getExtAccounts);
							}

							//Third Party External account combo data formatting..
							jsonThirdPartyExtAccArray = formatComboData(getThirdPartyExtAccounts,MSSystemDefaults.DEFAULT_ACC_TEXT); 
						}
					}

					getAllExternalAccounts.add(jsonExternalAccArray);
					getAllExternalAccounts.add(jsonThirdPartyExtAccArray);
				}
			}
			else {
				throw new Exception();
			}
			EBWLogger.trace(this, "Finished getExtAccountsList()"); 
			return getAllExternalAccounts;
		}
		catch (Exception exception) {
			throw exception;
		}
	}

	/**
	 * Gets the plating details for the Portfolio Loan accounts....
	 * @param transferType
	 * @return
	 * @throws Exception
	 */
	public Object getAccPlatingAttributes(AjaxAttributes ajaxAccParams) throws Exception 
	{
		EBWLogger.trace(this, "Starting getAccPlatingAttributes()");  
		Object objOutput = null;
		try 
		{
			boolean isLoanAccount = false;
			String acntType = ajaxAccParams.getAccountType();
			String selectedRefId = ajaxAccParams.getAccountRefID();

			//Check for the reference MSSB account not in context during INIT/EDIT ...
			if((selectedRefId!=null && selectedRefId.startsWith(MSSystemDefaults.LOAN_ACC_IND_REGX))
					|| (acntType!=null && acntType.equalsIgnoreCase(TxnTypeCode.PORTFOLIO_LOAN))){
				isLoanAccount = true;
			}

			if(isLoanAccount) {
				objOutput = getLoanAccPlatingAttributes(ajaxAccParams);
			}
		} 
		catch (Exception exception) {
			throw exception;
		}
		return objOutput;
	}

	/**
	 * Gets the plating details for the Portfolio Loan Account....
	 * @param transferType
	 * @return
	 * @throws Exception
	 */
	public Object getLoanAccPlatingAttributes(AjaxAttributes ajaxAccParams) throws Exception 
	{
		EBWLogger.trace(this, "Starting getLoanAccPlatingAttributes()"); 
		EBWLogger.trace(this, "Service name       : getLoanAccPlatingDetails"); 
		Object objOutput = null;
		try 
		{
			String loanAcnt = ajaxAccParams.getAccountRefID();
			if(loanAcnt!=null && loanAcnt.startsWith("PLA~")){
				loanAcnt = loanAcnt.split("PLA~")[1];
			}

			//Input mappings for the external account details....
			PaymentDetailsTO objPaymentDetails = new PaymentDetailsTO();
			objPaymentDetails.setLoanAcntNo(loanAcnt);
			objPaymentDetails.setTransfer_Type(TxnTypeCode.PORTFOLIO_LOAN);

			//Input mappings for the Portfolio Loan account details....
			PortfolioLoanAccount loanAcntDetails = (PortfolioLoanAccount)ajaxAccParams.getAcntDetails();

			//Service params and object mappings..
			String strStatement = "";

			Object[] objTOParam = {loanAcntDetails,objPaymentDetails};
			Object objParams[]={strStatement,objTOParam,new Boolean(false)};
			Class clsParamTypes[]={String.class,Object[].class,Boolean.class};

			//Service call...
			IEBWService objService = EBWServiceFactory.create("getLoanAccPlatingDetails");
			objOutput = objService.execute(clsParamTypes, objParams);
			if(objOutput!=null)
			{
				ArrayList onSelectAccPlatingDtls = (ArrayList)objOutput;
				ServiceContext contextData = (ServiceContext)onSelectAccPlatingDtls.get(0);
				if(contextData.getMaxSeverity()==MessageType.CRITICAL || contextData.getMaxSeverity()==MessageType.SEVERE){
					String errorMessage = MSCommonUtils.extractContextErrMessage(contextData);
					throw new Exception(errorMessage);
				}
			}
		} 
		catch (Exception exception) {
			throw exception;
		}
		return objOutput;
	}

	/**
	 * JSON Array Object Formatted for combobox data options...
	 * @param comboData
	 * @return
	 * @throws Exception 
	 */
	public JSONArray formatComboData(ArrayList comboData,String defaultTxt) throws Exception
	{
		EBWLogger.trace(this, "Starting formatComboData() for formatting the combodata to arrayList.."); 
		JSONArray jsonFormattedObj = new JSONArray();
		try 
		{
			//Initialization..
			LabelValueBean objLblValBean = new LabelValueBean();
			Collection<LabelValueBean> accountCollection = null;

			//Data formatting...
			accountCollection = MSCommonUtils.getComboboxValueDisp(comboData,defaultTxt);
			ArrayList accountsObj = new ArrayList();
			Vector<LabelValueBean> accountsOptions = (Vector<LabelValueBean>)accountCollection;
			for (int i=0, q=accountsOptions.size();i < q; i++) {
				objLblValBean = accountsOptions.get(i);
				if (objLblValBean != null) {
					//Creating a Hash Map for each accounts ( Value and Display)
					HashMap<String, String> accountsMap = new HashMap<String, String>();
					accountsMap.put("displayVal",objLblValBean.getLabel());
					accountsMap.put("accountVal",objLblValBean.getValue());
					accountsObj.add(accountsMap);// Putting the HashMap in the Object Array ...
				}
			}

			//Adding the Hash Map for the from accounts in the JSON ArrayObject.
			jsonFormattedObj = JSONArray.fromObject(accountsObj);
		} 
		catch (Exception exception) {
			throw exception;
		}   
		return jsonFormattedObj;
	}

	/** Formats the account number before storing in the DB in 3-6-3 format
	 *  In case the FA Number , Account Number , Office Number are not in the correct format ...  
	 * 
	 */
	public MMAccount getMSAccFormat(MMAccount objMMAccount){

		//Formatting Office number ...
		String officeAcc = objMMAccount.getOfficeNumber(); 
		do{
			if(officeAcc.length()>=3){
				break;
			}
			else {
				officeAcc="0"+officeAcc;
			}
		}while(officeAcc.length()!=3);
		objMMAccount.setOfficeNumber(officeAcc);

		//Formatting Account number ...
		String accNumber = objMMAccount.getAccountNumber(); 
		do{
			if(accNumber.length()>=6){
				break;
			}
			else {
				accNumber="0"+accNumber;
			}
		}while(accNumber.length()!=6);
		objMMAccount.setAccountNumber(accNumber);

		//Formatting FA number...
		String faNumber = objMMAccount.getFaNumber();  
		do{
			if(faNumber.length()>=3){
				break;
			}
			else {
				faNumber="0"+faNumber;
			}
		}while(faNumber.length()!=3);
		objMMAccount.setFaNumber(faNumber);

		return objMMAccount;
	}

	/**
	 * Formats the loan Account number before storing in the DB in 3-7 format
	 * @param objMMAccount
	 * @return
	 */
	public PortfolioLoanAccount getLoanAccFormat(PortfolioLoanAccount objLoanAcnt) {
		//Formatting Loan number ...
		String loanAcnt = objLoanAcnt.getLoanAccount(); 
		String bankNum = loanAcnt.substring(0,3);
		String loanNum = loanAcnt.substring(3);
		objLoanAcnt.setLoanAccount(bankNum+loanNum);
		return objLoanAcnt;
	}

	/**
	 * Formats the account number display in the drop down boxes.
	 * @param objMMAccount
	 * @return
	 */
	public static String formatLoanAccDisplay(PortfolioLoanAccount objLoanAcnt)
	{
		String accountDisplay = null;
		String loanAcnt = objLoanAcnt.getLoanAccount(); 

		String bankNum = loanAcnt.substring(0,3);
		String loanNum = loanAcnt.substring(3);
		if(loanNum.length()>3){
			int dispStartIndex = loanNum.length()-3;
			loanNum = loanNum.substring(dispStartIndex);
		}

		if(objLoanAcnt!=null){
			accountDisplay = MSSystemDefaults.LOAN_ACC_PREFIX_TEXT+" "+bankNum+" XXXX"+loanNum;
		}
		return accountDisplay;
	}
}
