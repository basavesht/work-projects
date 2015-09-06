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
import com.tcs.Payments.ms360Utils.ChkReqConstants;
import com.tcs.Payments.ms360Utils.IRA_Input_Desc;
import com.tcs.Payments.ms360Utils.MSCommonUtils;
import com.tcs.Payments.ms360Utils.MSSystemDefaults;
import com.tcs.Payments.ms360Utils.TxnTypeCode;
import com.tcs.bancs.channels.context.MessageType;
import com.tcs.bancs.channels.context.ServiceContext;
import com.tcs.bancs.ms360.integration.AccountOwner;
import com.tcs.bancs.ms360.integration.Address;
import com.tcs.bancs.ms360.integration.MMAccount;
import com.tcs.bancs.ms360.integration.MMContext;
import com.tcs.bancs.ms360.integration.PlatingAddress;
import com.tcs.bancs.ms360.integration.UserInfo;
import com.tcs.ebw.businessdelegate.EbwBusinessDelegate;
import com.tcs.ebw.common.util.EBWLogger;
import com.tcs.ebw.payments.transferobject.AjaxAttributes;
import com.tcs.ebw.payments.transferobject.DsOnloadAccDetailsTO;
import com.tcs.ebw.payments.transferobject.FromMSAcc_DetailsTO;
import com.tcs.ebw.payments.transferobject.IRATransferTypeTO;
import com.tcs.ebw.payments.transferobject.MSAcntPlatingDetails;
import com.tcs.ebw.payments.transferobject.MSUser_DetailsTO;
import com.tcs.ebw.payments.transferobject.MerlinOutResponse;
import com.tcs.ebw.payments.transferobject.PaymentDetailsTO;
import com.tcs.ebw.payments.transferobject.PortfolioLoanAccount;
import com.tcs.ebw.payments.transferobject.ToMSAcc_DetailsTO;
import com.tcs.ebw.payments.transferobject.MerlinOutResponse.AddressDtls;
import com.tcs.ebw.payments.transferobject.MerlinOutResponse.ClientDtls;
import com.tcs.ebw.payments.transferobject.MerlinOutResponse.PlatingAddressDtls;
import com.tcs.ebw.serverside.factory.EBWServiceFactory;
import com.tcs.ebw.serverside.factory.IEBWService;
import com.tcs.ebw.serverside.jaas.principal.UserPrincipal;
import com.tcs.ebw.serverside.services.channelPaymentServices.AccountDetails;
import com.tcs.ebw.serverside.services.channelPaymentServices.BusinessRulesService;
import com.tcs.ebw.serverside.services.channelPaymentServices.PortfolioLoansService;
import com.tcs.ebw.serverside.services.channelPaymentServices.WSDefaultInputsMap;

/**
 * @author : 224703
 * **************   Revision History ************************
 * Modified-By |  Modified-Date |  Project-Phase  | Changes
 *  224703          23-09-2011      P3-B             PLA  
 * **********************************************************
 */
public class GetAccountDetailsAjaxDelegate extends EbwBusinessDelegate{

	/**
	 * Constructor of GetAccountDetailsAjaxDelegate class. 
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
		EBWLogger.trace(this, "Starting getAccountBalance()"); 
		EBWLogger.trace(this, "Service name       : getSelectedAccountDetails"); 
		try 
		{
			//Initialization..
			boolean isExternalAccount=true;
			MMContext objMMContextData = objUserPrincipal.getContextData();
			ArrayList objMMContextAcc = objMMContextData.getAccounts();
			MSCommonUtils objCommonUtils = new MSCommonUtils();
			Object objOutput = null;
			String strStatement = "";
			boolean isAccntInContext = false;
			String acntType = ajaxAccParams.getAccountType();
			String selectedRefId = ajaxAccParams.getAccountRefID();

			//Mapping the User and From account details...
			MSUser_DetailsTO objMSDetails = objCommonUtils.setMSUserDetailsTO(objUserPrincipal);
			FromMSAcc_DetailsTO objFromMSAcc_DetailsTO = new FromMSAcc_DetailsTO();

			//Reference account in context...
			for(int i=0;i<objMMContextData.getAccounts().size();i++) {
				MMAccount objMMAccount = (MMAccount)objMMContextAcc.get(i);
				objMMAccount = getMSAccFormat(objMMAccount); //Formatting the FA,Office,Account Number ...
				if((objMMAccount.getKeyAccount()!=null && objMMAccount.getKeyAccount().equalsIgnoreCase(selectedRefId)))
				{
					objFromMSAcc_DetailsTO = objCommonUtils.setFromMSAccDetailsTO(objMMAccount);
					isExternalAccount = false;
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
				IEBWService objService = EBWServiceFactory.create("getSelectedAccountDetails");
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
			EBWLogger.trace(this, "Finished getAccountBalance()"); 
			return objOutput;
		} 
		catch (Exception exception) {
			throw exception;
		}
	}

	/**
	 * Gets the filtered list accounts using the block display call..
	 * @param transferType
	 * @return
	 * @throws Exception
	 */
	public ArrayList<Object> getFilteredAccList(String transferType) throws Exception 
	{
		EBWLogger.trace(this, "Starting getFilteredAccList()"); 
		EBWLogger.trace(this, "Service name       : getOnLoadAccDetailsAjax"); 
		try 
		{	
			int j=0;
			String strStatement = "";
			Object objOutput = null;
			ArrayList<String> getContextAccounts = new ArrayList<String>();
			ArrayList<Object> getFromAccounts = new ArrayList<Object>();
			ArrayList<Object> getToAccounts = new ArrayList<Object>();
			ArrayList<Object> getExternalAccounts = new ArrayList<Object> ();
			ArrayList<Object> getThirdPartyExtAccounts = new ArrayList<Object> ();
			ArrayList<Object> getLoanAccounts = new ArrayList<Object> ();
			ArrayList<Object> getCompleteAccountDetails = new ArrayList<Object>();
			MMContext objMMContextData = objUserPrincipal.getContextData();
			ArrayList objMMContextAcc = objMMContextData.getAccounts();
			ArrayList<Object> onLoadAccDetailsTO= new ArrayList<Object>();
			MSCommonUtils objCommonUtils = new MSCommonUtils();
			ServiceContext serviceContext = new ServiceContext();

			//MS User details mappings...
			MSUser_DetailsTO objMSUserDetails = objCommonUtils.setMSUserDetailsTO(objUserPrincipal);

			// Business Rule Input Control Mappings ...
			BUS_RULE_INP_CRTL objBUS_RULE_INP_CRTL = new BUS_RULE_INP_CRTL();
			objBUS_RULE_INP_CRTL.setUSER_ID(objMSUserDetails.getRcafId());
			objBUS_RULE_INP_CRTL.setGROUP(MSCommonUtils.getInitiatorRoleDesc(objMSUserDetails.getInitiatorRole())); 
			objBUS_RULE_INP_CRTL.setROLE(MSCommonUtils.getInitiatorRoleDesc(objMSUserDetails.getInitiatorRole()));
			objBUS_RULE_INP_CRTL.setAPPL_ID(WSDefaultInputsMap.getBusRuleCntrlAppID(serviceContext));
			objBUS_RULE_INP_CRTL.setSERVER_IP(WSDefaultInputsMap.getBusRuleCntrlServerID(serviceContext));

			// Business Rule Input Transaction Mappings ...
			BUS_RUL_TXN_INP objBUS_RUL_TXN_INP = new BUS_RUL_TXN_INP();
			if(transferType!=null && transferType.equalsIgnoreCase(TxnTypeCode.INTERNAL)){
				objBUS_RUL_TXN_INP.setTYPE(Bus_Rule_Input_Desc.TYPE_INT);
				objBUS_RUL_TXN_INP.setPAGE_TYPE(Bus_Rule_Input_Desc.MS360_INT);
			}
			else if(transferType!=null && transferType.equalsIgnoreCase(TxnTypeCode.PORTFOLIO_LOAN)){
				objBUS_RUL_TXN_INP.setTYPE(Bus_Rule_Input_Desc.TYPE_PORTFOLIO_LOAN);
				objBUS_RUL_TXN_INP.setPAGE_TYPE(Bus_Rule_Input_Desc.MS360_INT);
			}
			else if(transferType!=null && transferType.startsWith(ChkReqConstants.CHK)){
				objBUS_RUL_TXN_INP.setTYPE(Bus_Rule_Input_Desc.TYPE_CHK);
				objBUS_RUL_TXN_INP.setPAGE_TYPE(Bus_Rule_Input_Desc.MS360_CHECK);
			}
			else {
				objBUS_RUL_TXN_INP.setTYPE(Bus_Rule_Input_Desc.TYPE_ACH);
				objBUS_RUL_TXN_INP.setPAGE_TYPE(Bus_Rule_Input_Desc.MS360_ACH);
			}
			objBUS_RUL_TXN_INP.setACTION(Bus_Rule_Input_Desc.Create); 
			objBUS_RUL_TXN_INP.setRULE_TYPE(Bus_Rule_Input_Desc.RULE_TYPE_BLCK_DISP);  


			//Mapping the business rules objects...
			onLoadAccDetailsTO.add(objBUS_RULE_INP_CRTL);
			onLoadAccDetailsTO.add(objBUS_RUL_TXN_INP);

			for(int i =0; i<objMMContextData.getAccounts().size();i++)
			{
				MMAccount objMMAccount = (MMAccount)objMMContextAcc.get(i);
				objMMAccount = getMSAccFormat(objMMAccount); //Formatting the FA,Office,Account Number ...

				//Business Rule Input Mappings (AccountView Input) for Internal Accounts...
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
				objMS_ACCOUNT_OUT_DTL.setSHELL_ACC_IND(objMMAccount.getShell_acc_ind());

				onLoadAccDetailsTO.add(objMS_ACCOUNT_OUT_DTL);
			}

			//Contains the mapping for getting the external accounts ....
			DsOnloadAccDetailsTO dsOnloadAccDetailsTO = new DsOnloadAccDetailsTO();
			dsOnloadAccDetailsTO.setKey_client_id(MSCommonUtils.extractKeyClientId(objMMContextData));

			Object[] objTOParam = {dsOnloadAccDetailsTO,onLoadAccDetailsTO};
			Object objParams[]={strStatement,objTOParam,new Boolean(false),transferType};
			Class clsParamTypes[]={String.class,Object[].class,Boolean.class,String.class};

			//Service Call.....
			IEBWService objService = EBWServiceFactory.create("getOnLoadAccDetailsAjax");
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

					//Blocked display response for the MSSB account..
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

					//Putting all the context accounts in the ArrayList ....
					for(int a=0;a<objMMContextData.getAccounts().size();a++){
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
					for(int a=0; a < objMMContextData.getAccounts().size(); a++)
					{
						MMAccount objMMAccount = (MMAccount)objMMContextAcc.get(a);
						objMMAccount = getMSAccFormat(objMMAccount); //Formatting the FA,Office,Account Number ...
						ArrayList frmAccDispVal = new ArrayList();
						ArrayList toAccDispVal = new ArrayList();
						if(getFromAccounts.contains(objMMAccount.getAccountNumber())){
							String accountDisplay = (formatMSAccDisplay(objMMAccount));
							String accountValue =objMMAccount.getKeyAccount();
							frmAccDispVal.add(accountValue); //Account Form value..
							frmAccDispVal.add(accountDisplay); //Account Display name...
							getFrmAccsKeyName.add(frmAccDispVal);
						}
						if(getToAccounts.contains(objMMAccount.getAccountNumber())){
							String accountDisplay = (formatMSAccDisplay(objMMAccount));
							String accountValue =objMMAccount.getKeyAccount();
							toAccDispVal.add(accountValue); //Account Form value..
							toAccDispVal.add(accountDisplay); //Account Display name...
							getToAccsKeyName.add(toAccDispVal);
						}
					}

					//From account data formatting..
					JSONArray jsonFromAccArray = formatComboData(getFrmAccsKeyName,MSSystemDefaults.DEFAULT_ACC_TEXT);   

					//To account data formatting..
					JSONArray jsonToAccArray = formatComboData(getToAccsKeyName,MSSystemDefaults.DEFAULT_ACC_TEXT); 

					//Adding the Filtered External Accounts (Display, Value) inside a collection ... 
					JSONArray jsonExternalAccArray = new JSONArray();
					ArrayList<Object>  externalAccountValues = new ArrayList<Object> ();
					if(externalAccounts!=null)
					{
						ArrayList<Object>  extAccDetails = (ArrayList<Object>)externalAccounts;
						for( int i=1; i<extAccDetails.size(); i++)
						{
							externalAccountValues=(ArrayList)extAccDetails.get(i);
							ArrayList<Object> getExtAccounts = new ArrayList<Object> ();
							String payeeNickName=(String)externalAccountValues.get(0);
							String paypayeeId=(String)externalAccountValues.get(1);
							getExtAccounts.add(paypayeeId);//External Account Reference Number 
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

			EBWLogger.trace(this, "Finished getFilteredAccList()"); 
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

			//Contains the mapping for getting the external accounts ....
			DsOnloadAccDetailsTO dsOnloadAccDetailsTO = new DsOnloadAccDetailsTO();
			dsOnloadAccDetailsTO.setKey_client_id(MSCommonUtils.extractKeyClientId(objMMContextData));

			Object[] objTOParam = {dsOnloadAccDetailsTO};
			Object objParams[]={strStatement,objTOParam,new Boolean(false)};
			Class clsParamTypes[]={String.class,Object[].class,Boolean.class};

			//Service call..
			IEBWService objService = EBWServiceFactory.create("getAllExternalAccounts");
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

					//Adding the Filtered External Accounts (Display, Value) inside a collection ... 
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
							getExtAccounts.add(paypayeeId);//External Account Reference Number 
							getExtAccounts.add(payeeNickName);//External Nick Name 
							getExternalAccounts.add(getExtAccounts);
						}

						//External account Combo data formatting..
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
	 * Gets the plating details for the Internal/External MS accounts....
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
			boolean isExternalAccount = false;
			boolean isMSSBAccount = false;
			boolean isLoanAccount = false;
			MMContext objMMContextData = objUserPrincipal.getContextData();
			ArrayList objMMContextAcc = objMMContextData.getAccounts();
			boolean isAccntInContext = false;
			String acntType = ajaxAccParams.getAccountType();
			String selectedRefId = ajaxAccParams.getAccountRefID();

			//Checking for the reference account in the context...
			for(int i=0;i<objMMContextData.getAccounts().size();i++) {
				MMAccount objMMAccount = (MMAccount)objMMContextAcc.get(i);
				objMMAccount = getMSAccFormat(objMMAccount); //Formatting the FA,Office,Account Number ...
				if((objMMAccount.getKeyAccount()!=null && objMMAccount.getKeyAccount().equalsIgnoreCase(selectedRefId))){
					isMSSBAccount = true;
					isAccntInContext = true;
					break;
				}
			}

			//Check for the reference MSSB account not in context during INIT/EDIT ...
			if(!isAccntInContext && acntType!=null && acntType.equalsIgnoreCase(TxnTypeCode.INTERNAL)) {
				isMSSBAccount = true;
			}
			else if((selectedRefId!=null && selectedRefId.startsWith(MSSystemDefaults.LOAN_ACC_IND_REGX))
					|| (acntType!=null && acntType.equalsIgnoreCase(TxnTypeCode.PORTFOLIO_LOAN))){
				isLoanAccount = true;
			}
			else if(!isMSSBAccount && !isLoanAccount) {
				isExternalAccount = true;
			}

			//Condition to propagate the call to the corresponding event.....
			if(isMSSBAccount){
				objOutput = getIntAccPlatingAttributes(ajaxAccParams);
			}
			else if(isExternalAccount) {
				objOutput = getExtAccPlatingAttributes(ajaxAccParams);
			}
			else if(isLoanAccount) {
				objOutput = getLoanAccPlatingAttributes(ajaxAccParams);
			}
		} 
		catch (Exception exception) {
			throw exception;
		}
		return objOutput;
	}

	/**
	 * Gets the plating details for the internal MS accounts....
	 * @param transferType
	 * @return
	 * @throws Exception
	 */
	public Object getIntAccPlatingAttributes(AjaxAttributes ajaxAccParams) throws Exception 
	{
		EBWLogger.trace(this, "Starting getIntAccPlatingAttributes()"); 
		EBWLogger.trace(this, "Service name       : getIntAccPlatingDetails"); 
		Object objOutput = null;
		try 
		{
			String strStatement = "";

			int count=0;
			MSAcntPlatingDetails objMSAccPlatingDetails = new MSAcntPlatingDetails();

			//MMContext enriched extraction process..
			MMContext objMMContextData = objUserPrincipal.getContextData();
			UserInfo objUserInfo = objMMContextData.getUserInfo();
			ArrayList objMMAccounts = objMMContextData.getAccounts();
			boolean isAccntInContext = false;
			String acntType = ajaxAccParams.getAccountType();
			String selectedRefId = ajaxAccParams.getAccountRefID();

			//Reference account in context...
			for(int i =0; i<objMMAccounts.size();i++) {
				MMAccount objMMAccount = (MMAccount)objMMAccounts.get(i);
				objMMAccount = getMSAccFormat(objMMAccount); //Formatting the FA,Office,Account Number ...
				if(objMMAccount.getKeyAccount()!=null && objMMAccount.getKeyAccount().equalsIgnoreCase(selectedRefId))
				{
					objMSAccPlatingDetails.setOffice(objMMAccount.getOfficeNumber());
					objMSAccPlatingDetails.setAccount_number(objMMAccount.getAccountNumber());
					objMSAccPlatingDetails.setFa(objMMAccount.getFaNumber());
					objMSAccPlatingDetails.setUser_id(objUserInfo.getRACFId());
					count++;
					isAccntInContext = true;
					break;
				}
			}

			//Reference account not in context during edit ...
			if(!isAccntInContext) {
				if (acntType!=null && acntType.equalsIgnoreCase(TxnTypeCode.INTERNAL)){
					objMSAccPlatingDetails.setAccount_number(ajaxAccParams.getOffice_account_fa().substring(3,9));
					objMSAccPlatingDetails.setOffice(ajaxAccParams.getOffice_account_fa().substring(0,3));
					objMSAccPlatingDetails.setFa(ajaxAccParams.getOffice_account_fa().substring(9));
					objMSAccPlatingDetails.setUser_id(objUserInfo.getRACFId());
					count++;
				}
			}

			//In case no account matches...
			if(count==0){
				return null;
			}

			//Service objects and params mappings...
			Object[] objTOParam = {objMSAccPlatingDetails};
			Object objParams[]={strStatement,objTOParam,new Boolean(false)};
			Class clsParamTypes[]={String.class,Object[].class,Boolean.class};

			//Service Call.....
			IEBWService objService = EBWServiceFactory.create("getIntAccPlatingDetails");
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
	 * Gets the plating details for the external accounts....
	 * @param transferType
	 * @return
	 * @throws Exception
	 */
	public Object getExtAccPlatingAttributes(AjaxAttributes ajaxAccParams) throws Exception 
	{
		EBWLogger.trace(this, "Starting getExtAccPlatingAttributes()"); 
		EBWLogger.trace(this, "Service name       : getExtAccPlatingDetails"); 
		Object objOutput = null;
		try 
		{
			//Input mappings for the external account details....
			PaymentDetailsTO objPaymentDetails = new PaymentDetailsTO();
			objPaymentDetails.setExtAccount_RefId(ajaxAccParams.getAccountRefID());

			//Service params and object mappings..
			String strStatement = "";

			Object[] objTOParam = {objPaymentDetails};
			Object objParams[]={strStatement,objTOParam,new Boolean(false)};
			Class clsParamTypes[]={String.class,Object[].class,Boolean.class};

			//Service call...
			IEBWService objService = EBWServiceFactory.create("getExtAccPlatingDetails");
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
	 * Gets the Account Info for the selected accounts from the context..
	 * @param ajaxAccParams
	 * @return
	 * @throws Exception
	 */
	public ArrayList populateAccountInfo(AjaxAttributes ajaxAccParams) throws Exception 
	{
		EBWLogger.trace(this, "Starting GetAccDefaultAddress");  
		ArrayList acntInfo = new ArrayList();

		//Default Client Address ....
		HashMap defaultAccAdd = new HashMap();
		Address defaultAddress = new Address();

		//Account Plating Address ....
		ArrayList<PlatingAddress> acntPlatingAddressList = new ArrayList<PlatingAddress>();
		ArrayList acntPlatingAdd = new ArrayList();

		//Client Details ....
		ArrayList<AccountOwner> clientDtlsList = new ArrayList<AccountOwner>();
		HashMap clientDtls = new HashMap();

		//Account Category .....
		String accountCategory = null;

		Object objOutput = null;
		boolean isAcntInfoInContext = false;
		try 
		{
			MMContext objMMContextData = objUserPrincipal.getContextData();
			ArrayList objMMContextAcc = objMMContextData.getAccounts();
			String selectedRefId = ajaxAccParams.getAccountRefID();
			String acntType = ajaxAccParams.getAccountType();

			//Reference account in context...
			for(int i=0;i<objMMContextData.getAccounts().size();i++){
				MMAccount objMMAccount = (MMAccount)objMMContextAcc.get(i);
				objMMAccount = getMSAccFormat(objMMAccount); //Formatting the FA,Office,Account Number ...
				if((objMMAccount.getKeyAccount()!=null && objMMAccount.getKeyAccount().equalsIgnoreCase(selectedRefId)))
				{
					//Default client address.... (May be Domestic or Foreign)
					defaultAddress = (Address)objMMAccount.getAddress();
					if(defaultAddress!=null) {
						defaultAccAdd = MSCommonUtils.setClientDefaultAddress(defaultAddress);
					}
					//Account Plating address ... (May be Domestic or Foreign)
					acntPlatingAddressList = (ArrayList)objMMAccount.getPlatingAddress();
					if(acntPlatingAddressList!=null) {
						acntPlatingAdd = MSCommonUtils.setAcntPlatingAddress(acntPlatingAddressList);
					}
					//Client Details ... 
					clientDtlsList = (ArrayList)objMMAccount.getOwners();
					if(clientDtlsList!=null) {
						clientDtls = MSCommonUtils.setClientDetails(clientDtlsList);
					}
					//Additional Account related details (if any)..
					accountCategory = objMMAccount.getAccountCategory();

					//Setting flag in order to not make a call to Account's view ...
					isAcntInfoInContext = true;
					break;
				}
			}

			//Reference account not in context during edit ...
			if(!isAcntInfoInContext){
				if(acntType!=null && acntType.equalsIgnoreCase(TxnTypeCode.INTERNAL)){
					objOutput = getAccountViewDetails(ajaxAccParams);
					if(objOutput!=null) 
					{
						ArrayList getAccountViewDetails = (ArrayList)objOutput;
						if(getAccountViewDetails.get(1)!=null)
						{
							HashMap txnOutDetails = (HashMap)getAccountViewDetails.get(1);
							MMAccount objMMAccount = new MMAccount();

							//Extract from Merlin response and set in MMAccount...
							extractAcntViewResponse(txnOutDetails,objMMAccount);

							//Default client address.... (May be Domestic or Foreign)
							defaultAddress = (Address)objMMAccount.getAddress();
							if(defaultAddress!=null) {
								defaultAccAdd = MSCommonUtils.setClientDefaultAddress(defaultAddress);
							}
							//Account Plating address ... (May be Domestic or Foreign)
							acntPlatingAddressList = (ArrayList)objMMAccount.getPlatingAddress();
							if(acntPlatingAddressList!=null) {
								acntPlatingAdd = MSCommonUtils.setAcntPlatingAddress(acntPlatingAddressList);
							}
							//Client Details ... 
							clientDtlsList = (ArrayList)objMMAccount.getOwners();
							if(clientDtlsList!=null) {
								clientDtls = MSCommonUtils.setClientDetails(clientDtlsList);
							}
							//Account related details (if any)..
							accountCategory = objMMAccount.getAccountCategory();
						}
					}
				}
			}

			//Output Mappings..
			acntInfo.add(defaultAccAdd);
			acntInfo.add(acntPlatingAdd);
			acntInfo.add(clientDtls);
			acntInfo.add(accountCategory);
		} 
		catch (Exception exception) {
			throw exception;
		}
		return acntInfo;
	}

	/**
	 * To get Only the external accounts on each external transfers screen load . 
	 * @throws Exception 
	 */
	public JSONArray getIRATransferType(AjaxAttributes ajaxAccParams) throws Exception
	{
		EBWLogger.trace(this, "Starting getIRATransferType()"); 
		EBWLogger.trace(this, "Service name       : getIRATransferType"); 
		JSONArray jsonIRATransferTypes = null;
		try 
		{
			Object objOutput = null;
			boolean isIRAAccount = false;
			ArrayList getIRATransferTypes = new ArrayList();
			ServiceContext serviceContext = new ServiceContext();

			//Selected account object mappings..
			MMAccount selectedAccDetls = null;
			String selectedRefId = ajaxAccParams.getAccountRefID();

			//MMContext extraction from UserPrincipal...
			MMContext objMMContextData = objUserPrincipal.getContextData();
			ArrayList objMMContextAcc = objMMContextData.getAccounts();
			for(int i=0;i<objMMContextData.getAccounts().size();i++)
			{
				MMAccount objMMAccount = (MMAccount)objMMContextAcc.get(i);
				objMMAccount = getMSAccFormat(objMMAccount); //Formatting the FA,Office,Account Number ...
				if((objMMAccount.getKeyAccount()!=null && objMMAccount.getKeyAccount().equalsIgnoreCase(selectedRefId))){
					selectedAccDetls = objMMAccount ;
					break;
				}
			}

			//Checking if the selected account is an IRA account..
			isIRAAccount = getIRAAccountFlag(selectedAccDetls);
			if(isIRAAccount) 
			{
				if(selectedAccDetls!=null && selectedAccDetls.getIraCode()!=null && !selectedAccDetls.getIraCode().trim().equalsIgnoreCase(""))
				{
					//Data mapping for getting the IRA Transfer types...
					IRATransferTypeTO objIRATransferTypeTO = new IRATransferTypeTO();
					if(ajaxAccParams.getDebitCreditInd()!=null && ajaxAccParams.getDebitCreditInd().equalsIgnoreCase("DR")){
						objIRATransferTypeTO.setFrom_plan_code(selectedAccDetls.getIraCode());
						objIRATransferTypeTO.setTo_plan_code(MSCommonUtils.getDefaultIRAPlanCode(ajaxAccParams.getTransferType(),serviceContext));
					}
					else if(ajaxAccParams.getDebitCreditInd()!=null && ajaxAccParams.getDebitCreditInd().equalsIgnoreCase("CR")){
						objIRATransferTypeTO.setFrom_plan_code(MSCommonUtils.getDefaultIRAPlanCode(ajaxAccParams.getTransferType(),serviceContext));
						objIRATransferTypeTO.setTo_plan_code(selectedAccDetls.getIraCode());
					}
					objIRATransferTypeTO.setTxn_type(MSCommonUtils.getIRATxnType(ajaxAccParams.getTransferType()));
					objIRATransferTypeTO.setActive_flag(IRA_Input_Desc.ACTIVE);

					//Statement Id and Transfer objects...
					String strStatement = "getIRATransferType";
					Object objTOParam = objIRATransferTypeTO;

					Object objParams[]={strStatement,objTOParam,new Boolean(false)};
					Class clsParamTypes[]={String.class,Object.class,Boolean.class};

					//Service call..
					IEBWService objService = EBWServiceFactory.create("getIRATransferType");
					objOutput = objService.execute(clsParamTypes, objParams);

					//Adding the IRA Transfer types(Disp,Value) inside a collection ... 
					ArrayList<Object>  transferTypesValues = new ArrayList<Object> ();
					if(objOutput!=null)
					{
						ArrayList<Object>  iraTxnTypeDetails = (ArrayList<Object>)objOutput;
						if(iraTxnTypeDetails!=null && !iraTxnTypeDetails.isEmpty())
						{
							for( int i=1;i<iraTxnTypeDetails.size();i++)
							{
								transferTypesValues=(ArrayList)iraTxnTypeDetails.get(i);
								ArrayList<Object> getRetireMnemonic = new ArrayList<Object> ();
								String retire_txn_Desc =(String)transferTypesValues.get(0);
								String retirement_mnemonics=(String)transferTypesValues.get(1);

								getRetireMnemonic.add(retirement_mnemonics); //Retirement Mnemonics..
								getRetireMnemonic.add(retire_txn_Desc); //Retirement Transfer Types Description..
								getIRATransferTypes.add(getRetireMnemonic);
							}
							//Combo data formatting..
							jsonIRATransferTypes = formatComboData(getIRATransferTypes,MSSystemDefaults.DEFAULT_TXN_TYPE); 
						}
					}
				}
			}
		}
		catch (Exception exception) {
			throw exception;
		}
		return jsonIRATransferTypes;
	}

	/**
	 * Calling the Accounts View to get the required attributes...
	 * @param ajaxAccParams
	 * @throws Exception 
	 */
	public Object getAccountViewDetails(AjaxAttributes ajaxAccParams) throws Exception
	{
		EBWLogger.trace(this, "Starting getAccountDetails"); 
		EBWLogger.trace(this, "Service name       : getMerlinAccDetails"); 
		Object objOutput = null;
		try 
		{
			String txnType = ajaxAccParams.getTransferType();
			String strStatement = "";

			//Payment Details...
			PaymentDetailsTO objPaymentDetails = new PaymentDetailsTO();
			objPaymentDetails.setTransfer_Type(txnType);

			//From Account Details...
			FromMSAcc_DetailsTO objFromMSAcc_Details = new FromMSAcc_DetailsTO();
			objFromMSAcc_Details.setOfficeNumber(ajaxAccParams.getOffice_account_fa().substring(0,3));
			objFromMSAcc_Details.setAccountNumber(ajaxAccParams.getOffice_account_fa().substring(3,9));
			objFromMSAcc_Details.setFaNumber(ajaxAccParams.getOffice_account_fa().substring(9));

			//To Account Details... (Acnt view details not required for To internal account during account selection..)
			ToMSAcc_DetailsTO objToMSAcc_Details = new ToMSAcc_DetailsTO();
			objToMSAcc_Details.setAcntViewCallReq(false);

			//Service objects and params mappings...
			Object[] objTOParam = {objPaymentDetails,objFromMSAcc_Details,objToMSAcc_Details};
			Object objParams[]={strStatement,objTOParam,new Boolean(false)};
			Class clsParamTypes[]={String.class,Object[].class,Boolean.class};

			//Service Call.....
			IEBWService objService = EBWServiceFactory.create("getMerlinAccDetails");
			objOutput = objService.execute(clsParamTypes, objParams);

			if(objOutput!=null)
			{
				ArrayList getAccountViewDetails = (ArrayList)objOutput;
				ServiceContext contextData = (ServiceContext)getAccountViewDetails.get(0);
				if(contextData.getMaxSeverity()==MessageType.CRITICAL || contextData.getMaxSeverity()==MessageType.SEVERE){
					String errorMessage = MSCommonUtils.extractContextErrMessage(contextData);
					throw new Exception(errorMessage);
				}
			}
		}
		catch (Exception exception){
			throw exception;
		}
		return objOutput;
	}

	/**
	 * Gets the IRA flag for the selected Internal MS accounts...
	 * @param ajaxAccParams
	 * @return
	 * @throws Exception
	 */
	public static boolean getIRAAccountFlag(MMAccount selectedAccDetls) throws Exception
	{
		boolean iraAccFlag = false;
		try 
		{
			iraAccFlag = MSCommonUtils.getIRATxnFlag(selectedAccDetls);
		} 
		catch (Exception exception) {
			throw exception;
		}
		return iraAccFlag;
	}

	/**
	 * JSON Array Object Formatted for Combobox data options...
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
			for (int i=0, q = accountsOptions.size();i < q; i++) {
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
	public MMAccount getMSAccFormat(MMAccount objMMAccount)
	{
		//Formatting Office number ...
		String officeAcc = objMMAccount.getOfficeNumber(); 
		do{
			if(officeAcc.length()>=3){
				break;
			}
			else {
				officeAcc="0"+officeAcc;
			}
		} while(officeAcc.length()!=3);
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
		} while(faNumber.length()!=3);
		objMMAccount.setFaNumber(faNumber);

		return objMMAccount;
	}

	/**
	 * Formats the account number display in the drop down boxes.
	 * @param objMMAccount
	 * @return
	 */
	public static String formatMSAccDisplay(MMAccount objMMAccount)
	{
		String accountDisplay = null;
		if(objMMAccount!=null){
			accountDisplay = objMMAccount.getOfficeNumber()+"-"+objMMAccount.getAccountNumber()+"-"+objMMAccount.getFaNumber()+" "+MSCommonUtils.getAccCorporateName(objMMAccount);
		}
		return accountDisplay;
	}

	/**
	 * Formats the loan Account number before storing in the DB in 3-7 format
	 * @param objMMAccount
	 * @return
	 */
	public PortfolioLoanAccount getLoanAccFormat(PortfolioLoanAccount objLoanAcnt)
	{
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

		if(objLoanAcnt!=null){
			accountDisplay = MSSystemDefaults.LOAN_ACC_PREFIX_TEXT+" "+bankNum+"-"+loanNum;
		}
		return accountDisplay;
	}

	/** Following function is used to set the external services response details in the BR Service
	 * 
	 * @param toObjects
	 * @param externalSrvResponse
	 * @throws Exception 
	 */
	public static void extractAcntViewResponse(HashMap txnDetails,MMAccount objMMAccount) throws Exception
	{
		Address defaultAddress = new Address();
		ArrayList<PlatingAddress> acntPlatingAddressList = new ArrayList<PlatingAddress>();
		ArrayList<AccountOwner> clientDtlsList = new ArrayList<AccountOwner>();
		try 
		{
			FromMSAcc_DetailsTO objFromMSAcc_Details = new FromMSAcc_DetailsTO();
			if(txnDetails.containsKey("MSFromAccDetails")){
				objFromMSAcc_Details = (FromMSAcc_DetailsTO)txnDetails.get("MSFromAccDetails");
			}

			//Account View Response Mappings ..
			if(txnDetails.containsKey("MerlinOutputDetails"))
			{
				AccountDetails objaccDetails= (AccountDetails)txnDetails.get("MerlinOutputDetails");
				ArrayList merlinAccDetails = objaccDetails.getMerlinOutResponse(); 
				for(int j=0;j<merlinAccDetails.size();j++)
				{
					if(merlinAccDetails.get(j) instanceof MerlinOutResponse)
					{
						MerlinOutResponse accMerlinResponse =(MerlinOutResponse)merlinAccDetails.get(j);

						//Account Default Address List....
						ArrayList<AddressDtls> addressDtlsList = accMerlinResponse.getAddressDtls();
						if(addressDtlsList!=null && !addressDtlsList.isEmpty())
						{
							for(int i = 0;i<addressDtlsList.size();i++) 
							{
								MerlinOutResponse.AddressDtls address = addressDtlsList.get(i) ;
								if(accMerlinResponse.getAccountNumber().equalsIgnoreCase(objFromMSAcc_Details.getAccountNumber()))
								{
									String category = address.getCategory();
									if(category!=null && category.equalsIgnoreCase(MSSystemDefaults.ACNT_VIEW_MAIL_ADRSS_CATEGORY)){
										defaultAddress.setLine1(address.getLine1());
										defaultAddress.setLine2(address.getLine2());
										defaultAddress.setLine3(address.getLine3());
										defaultAddress.setCity(address.getCity());
										defaultAddress.setState(address.getState());
										defaultAddress.setZip5(address.getZip());
										defaultAddress.setProvince(address.getPostal());
										defaultAddress.setForeignStatus(address.getForiegnStatus());
										defaultAddress.setPostal(address.getPostal());
										break;
									}
								}
							}
						}

						//Account Plating Address List...
						ArrayList<PlatingAddressDtls> platingAddDtlsList = accMerlinResponse.getPlatingAddressDtls();
						if(platingAddDtlsList!=null && !platingAddDtlsList.isEmpty())
						{
							for(int i = 0;i<platingAddDtlsList.size();i++) 
							{
								MerlinOutResponse.PlatingAddressDtls platingAddress = platingAddDtlsList.get(i) ;
								if(accMerlinResponse.getAccountNumber().equalsIgnoreCase(objFromMSAcc_Details.getAccountNumber()))
								{
									String category = platingAddress.getAddress_category();
									if(category!=null && category.equalsIgnoreCase(MSSystemDefaults.ACNT_VIEW_PLATING_ADRSS_CATEGORY)){
										PlatingAddress objPlateAdd = new PlatingAddress();
										objPlateAdd.setAddressCategory(platingAddress.getAddress_category());
										objPlateAdd.setAddressline(platingAddress.getAddress_line());
										objPlateAdd.setAddresslineindex(platingAddress.getAddress_line_index());
										acntPlatingAddressList.add(objPlateAdd);
									}
								}
							}
						}

						//Client Information....
						ArrayList<ClientDtls> clientDtls = accMerlinResponse.getClientDtls();
						if(clientDtls!=null && !clientDtls.isEmpty())
						{
							for(int i = 0;i<clientDtls.size();i++) 
							{
								MerlinOutResponse.ClientDtls clientInfo = clientDtls.get(i) ;
								if(accMerlinResponse.getAccountNumber().equalsIgnoreCase(objFromMSAcc_Details.getAccountNumber()))
								{
									String relationship = clientInfo.getRelationship();
									if(relationship!=null && relationship.equalsIgnoreCase(MSSystemDefaults.ACNT_VIEW_CLIENT_RELATIONSHIP)){
										AccountOwner accountOwner = new AccountOwner();
										accountOwner.setRelationship(clientInfo.getRelationship());
										accountOwner.setResidentCountry(clientInfo.getResidentCountry());
										clientDtlsList.add(accountOwner);
										break;
									}
								}
							}
						}

						//Output mappings...
						objMMAccount.setAddress(defaultAddress);
						objMMAccount.setPlatingAddress(acntPlatingAddressList);
						objMMAccount.setOwners(clientDtlsList);
						objMMAccount.setAccountCategory(accMerlinResponse.getAcntCategory());
					}
				}
			}
		} 
		catch (Exception exception) {
			throw exception;
		}
	}
}
