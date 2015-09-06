package com.tcs.ebw.serverside.services.channelPaymentServices;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;

import com.tcs.Payments.EAITO.MS_INTERFACE_TECH_FAILURE;
import com.tcs.Payments.EAITO.MS_PLA_OUT_DTL;
import com.tcs.Payments.ms360Utils.LoanAcntPlatingLabel;
import com.tcs.Payments.ms360Utils.MSCommonUtils;
import com.tcs.bancs.channels.ChannelsErrorCodes;
import com.tcs.bancs.channels.context.MessageType;
import com.tcs.bancs.channels.context.ServiceContext;
import com.tcs.bancs.ms360.integration.MMAccount;
import com.tcs.ebw.common.util.EBWLogger;
import com.tcs.ebw.payments.transferobject.PaymentDetailsTO;
import com.tcs.ebw.payments.transferobject.PortfolioLoanAccount;
import com.tcs.ebw.serverside.services.PaymentsUtility;
import com.tcs.mswitch.common.channel.SI_RETURN;

/**
 * @author : 224703
 * **************   Revision History ************************
 * Modified-By |  Modified-Date |  Project-Phase  | Changes
 *    224703       23-09-2011        P3-B            PLA  
 * **********************************************************
 */
public class PortfolioLoansService implements Serializable {

	private static final long serialVersionUID = -58699699716108533L;
	private ArrayList<PortfolioLoanAccount> loanAcntsList = new ArrayList<PortfolioLoanAccount>();

	public ArrayList<PortfolioLoanAccount> getLoanAcntsList() {
		return loanAcntsList;
	}
	public void setLoanAcntsList(ArrayList<PortfolioLoanAccount> loanAcntsList) {
		this.loanAcntsList = loanAcntsList;
	}

	/**
	 * Extraction code logic for the Portfolio Loan Service... 
	 * @param txnDetails
	 * @param plaAccountsSIOutput
	 * @param context
	 * @return
	 * @throws Exception 
	 */
	public static PortfolioLoansService getPLAAccountsDtls(HashMap txnDetails,Vector<Object> plaAccountsSIOutput, ServiceContext context) throws Exception 
	{
		EBWLogger.logDebug("PortfolioLoansService","Extracting from the PortfolioLoansService after the PLA Service Execution");
		PortfolioLoansService objPortfolioLoansService = new PortfolioLoansService();
		try 
		{
			//Interface Output attributes...
			SI_RETURN si_return = (SI_RETURN)plaAccountsSIOutput.get(0);
			ArrayList loanAcntdeatils = new ArrayList();

			//Output Extraction and storage...
			if(si_return.getReturnCode() == 0)
			{
				MS_PLA_OUT_DTL objMS_PLA_OUT_DTL = new MS_PLA_OUT_DTL();
				for(int k = 0; k < si_return.getOutputVector().size(); k++)
				{
					if(si_return.getOutputVector().get(k) instanceof MS_PLA_OUT_DTL )
					{	
						objMS_PLA_OUT_DTL = (MS_PLA_OUT_DTL)si_return.getOutputVector().get(k);
						if(objMS_PLA_OUT_DTL!=null)
						{
							String pla_loan_acnt_no = objMS_PLA_OUT_DTL.getPLA_NO();
							if(pla_loan_acnt_no != null)
							{	
								PortfolioLoanAccount portfolioLoanAccount= new PortfolioLoanAccount();
								ArrayList<MMAccount> collateralAcctInfo = new ArrayList<MMAccount>();

								//Setting loan account details..
								portfolioLoanAccount.setLoanAccount(objMS_PLA_OUT_DTL.getPLA_NO());	
								portfolioLoanAccount.setLoanAcntClientName(objMS_PLA_OUT_DTL.getACNT_CLIENT_NAME());
								portfolioLoanAccount.setLoanOutstandingBal(objMS_PLA_OUT_DTL.getOUTSTANDING_BAL());

								//Mapping the COLLATERAL ACCT INFO  if any in the response...
								MS_PLA_OUT_DTL.COLLATERAL_ACCT_INFO objCollateralInfo[] = objMS_PLA_OUT_DTL.getMS_COLLATERAL_ACCT_INFO();
								if(objCollateralInfo != null)
								{
									for (int j = 0; j < objCollateralInfo.length; j++)  {
										//Creating MMAccount Instance of collateral account..
										MMAccount objMMAccount = new MMAccount();
										objMMAccount.setAccountNumber(objCollateralInfo[j].getACCT());
										objMMAccount.setOfficeNumber(objCollateralInfo[j].getOFFICE());
										collateralAcctInfo.add(MSCommonUtils.getMSAccFormat(objMMAccount));
									}
									portfolioLoanAccount.setCollateralAcnt(collateralAcctInfo);
								}
								loanAcntdeatils.add(portfolioLoanAccount);

								//Validate the collateral accounts response.The list cannot be empty in any case for the PLA account number.
								if(collateralAcctInfo.isEmpty()){
									context.addMessage(MessageType.CRITICAL,ChannelsErrorCodes.PLA_COLLATERAL_ACNTS_EMPTY);
									return null;
								}
							}
						}
					}
				}

				//Setting the PortfolioLoansService object...
				objPortfolioLoansService.setLoanAcntsList(loanAcntdeatils);
			}
			else
			{
				EBWLogger.logDebug("PortfolioLoansService","The PLA Call was a failure..");
				for(int j=0;j<si_return.getErrorVector().size();j++){
					if(si_return.getErrorVector().get(j) instanceof MS_INTERFACE_TECH_FAILURE ){
						context.addMessage(MessageType.CRITICAL,ChannelsErrorCodes.INTERNAL_ERROR);
						break;
					}
				}
			}
			EBWLogger.logDebug("PortfolioLoansService","Returning from the PortfolioLoansService after the PLA Service Execution");
		} 
		catch (Exception exception) {
			throw exception;	
		}
		return objPortfolioLoansService;
	}

	/**
	 * Account Plating details for the Portfolio loan accounts..
	 * @param txnDetails
	 * @param context
	 * @return
	 * @throws Exception 
	 */
	public void getLoanAcntPlatingInfo(HashMap txnDetails,ServiceContext context) throws Exception 
	{
		EBWLogger.trace(this, "Starting getLoanAcntPlatingInfo method in PortfolioLoansService class");
		try
		{
			ArrayList acntPlatindInfo= new ArrayList();
			boolean isLoanAcntDetailsExists = false;

			//Loan Account Details ..
			PortfolioLoanAccount objLoanAcntDetails = new PortfolioLoanAccount();
			if(txnDetails.containsKey("LoanAccountDetails")){
				objLoanAcntDetails = (PortfolioLoanAccount)txnDetails.get("LoanAccountDetails");
			}

			//Extracting the attribute value for details... 
			if(objLoanAcntDetails!=null) {
				String loanAcnt = (String)objLoanAcntDetails.getLoanAccount();
				if(loanAcnt !=null && !loanAcnt.trim().equalsIgnoreCase("")){
					isLoanAcntDetailsExists = true;
				}
			}

			//In case loan account doesn't exist , invoke PLA details Web service ...
			if(!isLoanAcntDetailsExists) {
				EBWLogger.logDebug(this, "Invoke the loanAcnt Details webservice...");
				PortfolioLoansService objgetLoanAcntDetails = new PortfolioLoansService();
				objgetLoanAcntDetails.getLoanAcntDetails(txnDetails,context);
				EBWLogger.logDebug(this, "Finished invoking Loan Acnt Details...");
			}

			//Extracting the attribute value for attribute details...
			if(txnDetails.containsKey("LoanAccountDetails")){
				objLoanAcntDetails = (PortfolioLoanAccount)txnDetails.get("LoanAccountDetails");
			}
			String clientName = (String)objLoanAcntDetails.getLoanAcntClientName();
			String loanOutstandingBal = MSCommonUtils.formatTxnAmount(objLoanAcntDetails.getLoanOutstandingBal());

			// Setting the Account plating client details in the HashMap...
			HashMap acntOnwerMap = new HashMap();
			acntOnwerMap.put("AttributeName", LoanAcntPlatingLabel.CLIENT_NAME.trim());
			acntOnwerMap.put("AttributeValue", clientName);
			acntOnwerMap.put("AttributeSequence", "L1");
			acntPlatindInfo.add(acntOnwerMap);

			// Setting the Account plating Outstanding balance details in the HashMap...
			HashMap acntDetailsMap = new HashMap();
			acntDetailsMap.put("AttributeName", LoanAcntPlatingLabel.OUTSTANDING_BAL.trim());
			acntDetailsMap.put("AttributeValue", loanOutstandingBal);
			acntDetailsMap.put("AttributeSequence", "R1");
			acntPlatindInfo.add(acntDetailsMap);

			//Mapping details to the payment details transfer object..
			txnDetails.put("LoanAcntPlatingOutputDetails",acntPlatindInfo);
		}
		catch(Exception exception){
			throw exception;
		}
		finally{

		}
	}

	/**
	 * Validating Portfolio Loan Acnt Details..
	 * @param txnDetails
	 * @param context
	 * @return
	 * @throws Exception 
	 */
	public void getLoanAcntDetails(HashMap txnDetails,ServiceContext context) throws Exception 
	{
		EBWLogger.trace(this, "Starting getLoanAcntPlatingInfo method in PortfolioLoansService class");
		try
		{
			//Payment Details ...
			PaymentDetailsTO objPaymentDetails = new PaymentDetailsTO();
			if(txnDetails.containsKey("PaymentDetails")){
				objPaymentDetails = (PaymentDetailsTO)txnDetails.get("PaymentDetails");
			}

			//Invoke PLA Web service...
			EBWLogger.logDebug(this, "Getting the PLA Loan Account Details ...");
			PaymentsUtility.fetchPLACreditLoans(txnDetails,context);
			if (context.getMaxSeverity()== MessageType.CRITICAL || context.getMaxSeverity() == MessageType.SEVERE){
				return ;
			}

			//Loans Account response...
			PortfolioLoansService objPortfolioLoansService = new PortfolioLoansService();
			ArrayList loansAcnts = new ArrayList();
			if(txnDetails.containsKey("PLACreditLoans")){
				objPortfolioLoansService = (PortfolioLoansService)txnDetails.get("PLACreditLoans");
				loansAcnts = objPortfolioLoansService.getLoanAcntsList();
			}

			//Putting all the PLA Loan Accounts in the ArrayList ....
			for(int a=0; a < loansAcnts.size(); a++){
				PortfolioLoanAccount loanAcnt = (PortfolioLoanAccount)loansAcnts.get(a);
				if(loanAcnt!=null && loanAcnt.getLoanAccount().equalsIgnoreCase(objPaymentDetails.getLoanAcntNo())){
					txnDetails.put("LoanAccountDetails",loanAcnt);
				}
			}
		}
		catch(Exception exception){
			throw exception;
		}
		finally{

		}
	}
}
