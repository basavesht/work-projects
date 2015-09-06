/**
 * 
 */
package com.tcs.ebw.serverside.services.channelPaymentServices;

import java.sql.SQLException;

import com.tcs.Payments.ms360Utils.ChkReqConstants;
import com.tcs.Payments.ms360Utils.TxnTypeCode;
import com.tcs.bancs.channels.context.ServiceContext;
import com.tcs.ebw.common.util.EBWLogger;
import com.tcs.ebw.payments.transferobject.DsPayTxnsTO;
import com.tcs.ebw.payments.transferobject.TransactionEnrichmentTO;
import com.tcs.ebw.serverside.services.DatabaseService;

/**
 * @author : 224703
 * **************   Revision History ************************
 * Modified-By |  Modified-Date |  Project-Phase  | Changes
 *
 * **********************************************************
 */
public class TransactionEnrichment extends DatabaseService{

	public TransactionEnrichment(){

	}

	/** Method is used for making an entry into the TRANSACTION_ENRICHMENT table for each transaction in
	 * turn for each MS account selected . In case of INT Transfers 2 entries will be made for each transaction
	 * and in case of the ACH Transfers only one entry will be there for the corresponding MS account always 
	 * irrespective of the transaction status.
	 * @param toObjects
	 */

	public void setTransactionEnrichment(Object payment_DtlObj,ServiceContext serviceContext) throws Exception,SQLException 
	{
		EBWLogger.logDebug(this, "Starting setTransactionEnrichment method in TransactionEnrichment class");
		String txnEnrichmentStmntId = "setTranasactionEnrichment";
		boolean boolean1 = Boolean.TRUE;
		try 
		{
			if(payment_DtlObj!=null && payment_DtlObj instanceof DsPayTxnsTO){
				TransactionEnrichmentTO frm_txnEnrichmentTO = new TransactionEnrichmentTO();
				TransactionEnrichmentTO to_txnEnrichmentTO = new TransactionEnrichmentTO();

				DsPayTxnsTO objDsPayTxnsTO = (DsPayTxnsTO)payment_DtlObj;
				String txnType = objDsPayTxnsTO.getTxn_type();
				String toOfficeId = "";
				String toAccountNum = "";
				String toFa_Id = "";
				String frmOfficeId = "";
				String frmAccountNum = "";
				String frmFa_Id = "";

				//From account details (FA_ID, ACCNUM , OFFICE_ID)--- 3-6-3 Format
				if(objDsPayTxnsTO.getFrombr_acct_no_fa()!=null && !objDsPayTxnsTO.getFrombr_acct_no_fa().trim().equalsIgnoreCase("")){
					frmOfficeId = objDsPayTxnsTO.getFrombr_acct_no_fa().substring(0,3);
					frmAccountNum = objDsPayTxnsTO.getFrombr_acct_no_fa().substring(3,9);
					frmFa_Id = objDsPayTxnsTO.getFrombr_acct_no_fa().substring(9);
				}

				//To account details (FA_ID, ACCNUM , OFFICE_ID)--- 3-6-3 Format
				if(objDsPayTxnsTO.getTobr_acct_no_fa()!=null && !objDsPayTxnsTO.getTobr_acct_no_fa().trim().equalsIgnoreCase("")){
					toOfficeId = objDsPayTxnsTO.getTobr_acct_no_fa().substring(0,3);
					toAccountNum = objDsPayTxnsTO.getTobr_acct_no_fa().substring(3,9);
					toFa_Id = objDsPayTxnsTO.getTobr_acct_no_fa().substring(9);
				}

				if(txnType!=null && txnType.equalsIgnoreCase(TxnTypeCode.INTERNAL))
				{
					//TransactionEnrichment population for the FROM_ACCOUNT.
					frm_txnEnrichmentTO.setTxn_Pos_Num(objDsPayTxnsTO.getPaypayref());
					frm_txnEnrichmentTO.setKeyAcct(objDsPayTxnsTO.getKeyaccountnumber_from());
					frm_txnEnrichmentTO.setAcct_DrCrInd(new Double(1));
					frm_txnEnrichmentTO.setEnrichment_Ind(new Double(2));
					frm_txnEnrichmentTO.setFa_id(frmFa_Id);
					frm_txnEnrichmentTO.setOffice_id(frmOfficeId);
					frm_txnEnrichmentTO.setAccount_num(frmAccountNum); //In case of Internal Transfers FROM account will be stored in paydebitaccnum column.
					EBWLogger.logDebug(this,"Executing for Debit MS Account.... "+txnEnrichmentStmntId);
					execute(txnEnrichmentStmntId,frm_txnEnrichmentTO,boolean1);
					EBWLogger.logDebug(this,"Execution Completed.... "+txnEnrichmentStmntId);

					//TransactionEnrichment population for the TO_ACCOUNT.
					to_txnEnrichmentTO.setTxn_Pos_Num(objDsPayTxnsTO.getPaypayref());
					to_txnEnrichmentTO.setKeyAcct(objDsPayTxnsTO.getKeyaccountnumber_to());
					to_txnEnrichmentTO.setAcct_DrCrInd(new Double(2));
					to_txnEnrichmentTO.setEnrichment_Ind(new Double(2));
					to_txnEnrichmentTO.setFa_id(toFa_Id);
					to_txnEnrichmentTO.setOffice_id(toOfficeId);
					to_txnEnrichmentTO.setAccount_num(toAccountNum); //In case of  Internal Transfers TO account number will be stored in paypayeeaccnum column. 
					EBWLogger.logDebug(this,"Executing for Credit MS Account.... "+txnEnrichmentStmntId);
					execute(txnEnrichmentStmntId,to_txnEnrichmentTO,boolean1);
					EBWLogger.logDebug(this,"Execution Completed.... "+txnEnrichmentStmntId);
				}
				else if(txnType!=null && (txnType.equalsIgnoreCase(TxnTypeCode.ACH_WITHDRAWAL)))
				{
					//TransactionEnrichment population for the FROM_ACCOUNT.
					frm_txnEnrichmentTO.setTxn_Pos_Num(objDsPayTxnsTO.getPaypayref());
					frm_txnEnrichmentTO.setKeyAcct(objDsPayTxnsTO.getKeyaccountnumber_from());
					frm_txnEnrichmentTO.setAcct_DrCrInd(new Double(1));
					frm_txnEnrichmentTO.setEnrichment_Ind(new Double(2));
					frm_txnEnrichmentTO.setFa_id(frmFa_Id);
					frm_txnEnrichmentTO.setOffice_id(frmOfficeId);
					frm_txnEnrichmentTO.setAccount_num(frmAccountNum); // In case of ACH transfers , all the internal MS account will be stored in the paydebitaccnum column only..
					EBWLogger.logDebug(this,"Executing for Debit MS Account.... "+txnEnrichmentStmntId);
					execute(txnEnrichmentStmntId,frm_txnEnrichmentTO,boolean1);
					EBWLogger.logDebug(this,"Execution Completed.... "+txnEnrichmentStmntId);
				}
				else if(txnType!=null && txnType.equalsIgnoreCase(TxnTypeCode.ACH_DEPOSIT))
				{
					//TransactionEnrichment population for the TO_ACCOUNT.
					to_txnEnrichmentTO.setTxn_Pos_Num(objDsPayTxnsTO.getPaypayref());
					to_txnEnrichmentTO.setKeyAcct(objDsPayTxnsTO.getKeyaccountnumber_to());
					to_txnEnrichmentTO.setAcct_DrCrInd(new Double(2));
					to_txnEnrichmentTO.setEnrichment_Ind(new Double(2));
					to_txnEnrichmentTO.setFa_id(toFa_Id);
					to_txnEnrichmentTO.setOffice_id(toOfficeId);
					to_txnEnrichmentTO.setAccount_num(toAccountNum); // In case of ACH transfers , all the internal MS account will be stored in the paydebitaccnum column only..
					EBWLogger.logDebug(this,"Executing for Credit MS Account.... "+txnEnrichmentStmntId);
					execute(txnEnrichmentStmntId,to_txnEnrichmentTO,boolean1);
					EBWLogger.logDebug(this,"Execution Completed.... "+txnEnrichmentStmntId);
				}
				else if(txnType!=null && (txnType.startsWith(ChkReqConstants.CHK)))
				{
					//TransactionEnrichment population for the FROM_ACCOUNT.
					frm_txnEnrichmentTO.setTxn_Pos_Num(objDsPayTxnsTO.getPaypayref());
					frm_txnEnrichmentTO.setKeyAcct(objDsPayTxnsTO.getKeyaccountnumber_from());
					frm_txnEnrichmentTO.setAcct_DrCrInd(new Double(1));
					frm_txnEnrichmentTO.setEnrichment_Ind(new Double(2));
					frm_txnEnrichmentTO.setFa_id(frmFa_Id);
					frm_txnEnrichmentTO.setOffice_id(frmOfficeId);
					frm_txnEnrichmentTO.setAccount_num(frmAccountNum); // In case of ACH transfers , all the internal MS account will be stored in the paydebitaccnum column only..
					EBWLogger.logDebug(this,"Executing for Debit MS Account.... "+txnEnrichmentStmntId);
					execute(txnEnrichmentStmntId,frm_txnEnrichmentTO,boolean1);
					EBWLogger.logDebug(this,"Execution Completed.... "+txnEnrichmentStmntId);
				}
				else if(txnType!=null && (txnType.equalsIgnoreCase(TxnTypeCode.PORTFOLIO_LOAN)))
				{
					//TransactionEnrichment population for the FROM_ACCOUNT.
					frm_txnEnrichmentTO.setTxn_Pos_Num(objDsPayTxnsTO.getPaypayref());
					frm_txnEnrichmentTO.setKeyAcct(objDsPayTxnsTO.getKeyaccountnumber_from());
					frm_txnEnrichmentTO.setAcct_DrCrInd(new Double(1));
					frm_txnEnrichmentTO.setEnrichment_Ind(new Double(2));
					frm_txnEnrichmentTO.setFa_id(frmFa_Id);
					frm_txnEnrichmentTO.setOffice_id(frmOfficeId);
					frm_txnEnrichmentTO.setAccount_num(frmAccountNum); // In case of ACH transfers , all the internal MS account will be stored in the paydebitaccnum column only..
					EBWLogger.logDebug(this,"Executing for Debit MS Account.... "+txnEnrichmentStmntId);
					execute(txnEnrichmentStmntId,frm_txnEnrichmentTO,boolean1);
					EBWLogger.logDebug(this,"Execution Completed.... "+txnEnrichmentStmntId);
				}
			}
			EBWLogger.trace(this, "Finished with setTransactionEnrichment method of TransactionEnrichment class");
		} 		
		catch(SQLException sqlexception){
			sqlexception.printStackTrace();
			throw sqlexception;
		}
		catch(Exception exception){
			throw exception;
		}
		finally{

		}
	}
}
