package com.tcs.ebw.serverside.services.channelPaymentServices;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;

import com.tcs.Payments.EAITO.BLKD_ACNT_OUT;
import com.tcs.Payments.EAITO.BR_VALIDATION_LOG;
import com.tcs.Payments.EAITO.MS_INTERFACE_TECH_FAILURE;
import com.tcs.Payments.ms360Utils.Auth_Mode;
import com.tcs.bancs.channels.ChannelsErrorCodes;
import com.tcs.bancs.channels.context.MessageType;
import com.tcs.bancs.channels.context.ServiceContext;
import com.tcs.ebw.common.util.EBWLogger;
import com.tcs.ebw.payments.transferobject.PaymentDetailsTO;
import com.tcs.mswitch.common.channel.SI_RETURN;

/**
 * @author : 224703
 * **************   Revision History ************************
 * Modified-By |  Modified-Date |  Project-Phase  | Changes
 *   224703         12-05-2011        3              3rd Party ACH
 *   224703         01-07-2011        3              CR 262
 * **********************************************************
 */
public class BusinessRulesService implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 8722966081852985680L;
	private ArrayList blockedAccountDetails;
	private ArrayList brErrorMsgDesc;
	private ArrayList brWarningMsgDesc;
	private ArrayList brInfoMsgDesc;
	private Vector brValidationOut;
	private String rsa_Review_Flag = "N";
	private String next_approver_role;
	private String auth_indicator;
	private String auth_mode;
	private String dont_spawn_flag = "N";
	private String ofac_case_id;
	private ArrayList signed_documents_req = new ArrayList();
	private boolean DR_ACC_IN_ELIGIBLE = false;
	private boolean CR_ACC_IN_ELIGIBLE = false;
	private boolean NEXT_APPROVAL_REQ = false;

	public ArrayList getBlockedAccountDetails() {
		return blockedAccountDetails;
	}
	public void setBlockedAccountDetails(ArrayList blockedAccountDetails) {
		this.blockedAccountDetails = blockedAccountDetails;
	}
	public Vector getBrValidationOut() {
		return brValidationOut;
	}
	public void setBrValidationOut(Vector brValidationOut) {
		this.brValidationOut = brValidationOut;
	}
	public String getRsa_Review_Flag() {
		return rsa_Review_Flag;
	}
	public void setRsa_Review_Flag(String rsa_Review_Flag) {
		this.rsa_Review_Flag = rsa_Review_Flag;
	}
	public String getNext_approver_role() {
		return next_approver_role;
	}
	public void setNext_approver_role(String next_approver_role) {
		this.next_approver_role = next_approver_role;
	}
	public String getAuth_indicator() {
		return auth_indicator;
	}
	public void setAuth_indicator(String auth_indicator) {
		this.auth_indicator = auth_indicator;
	}
	public String getAuth_mode() {
		return auth_mode;
	}
	public void setAuth_mode(String auth_mode) {
		this.auth_mode = auth_mode;
	}
	public String getDont_spawn_flag() {
		return dont_spawn_flag;
	}
	public void setDont_spawn_flag(String dont_spawn_flag) {
		this.dont_spawn_flag = dont_spawn_flag;
	}
	public ArrayList getSigned_documents_req() {
		return signed_documents_req;
	}
	public void setSigned_documents_req(ArrayList signed_documents_req) {
		this.signed_documents_req = signed_documents_req;
	}
	public boolean isDR_ACC_IN_ELIGIBLE() {
		return DR_ACC_IN_ELIGIBLE;
	}
	public void setDR_ACC_IN_ELIGIBLE(boolean dr_acc_in_eligible) {
		DR_ACC_IN_ELIGIBLE = dr_acc_in_eligible;
	}
	public boolean isCR_ACC_IN_ELIGIBLE() {
		return CR_ACC_IN_ELIGIBLE;
	}
	public void setCR_ACC_IN_ELIGIBLE(boolean cr_acc_in_eligible) {
		CR_ACC_IN_ELIGIBLE = cr_acc_in_eligible;
	}
	public boolean isNEXT_APPROVAL_REQ() {
		return NEXT_APPROVAL_REQ;
	}
	public void setNEXT_APPROVAL_REQ(boolean next_approval_req) {
		NEXT_APPROVAL_REQ = next_approval_req;
	}
	public ArrayList getBrErrorMsgDesc() {
		return brErrorMsgDesc;
	}
	public void setBrErrorMsgDesc(ArrayList brErrorMsgDesc) {
		this.brErrorMsgDesc = brErrorMsgDesc;
	}
	public ArrayList getBrWarningMsgDesc() {
		return brWarningMsgDesc;
	}
	public void setBrWarningMsgDesc(ArrayList brWarningMsgDesc) {
		this.brWarningMsgDesc = brWarningMsgDesc;
	}
	public ArrayList getBrInfoMsgDesc() {
		return brInfoMsgDesc;
	}
	public void setBrInfoMsgDesc(ArrayList brInfoMsgDesc) {
		this.brInfoMsgDesc = brInfoMsgDesc;
	}
	public String getOfac_case_id() {
		return ofac_case_id;
	}
	public void setOfac_case_id(String ofac_case_id) {
		this.ofac_case_id = ofac_case_id;
	}

	/**
	 * Business Rule response extraction code...
	 * @param txnDetails
	 * @param brOutDetails
	 * @param context
	 * @return
	 * @throws Exception
	 */
	public static BusinessRulesService getBusinessRuleOutput(HashMap txnDetails,Vector brOutDetails,ServiceContext context) throws Exception
	{
		EBWLogger.logDebug("PaymentOutDetails","SI Returned Vector for Business Rule Input....");
		BusinessRulesService objBusinessRuleOutDetails = new BusinessRulesService();
		try 
		{
			//Payment Output Details .....
			PaymentDetailsTO objPaymentDetails = new PaymentDetailsTO();
			if(txnDetails.containsKey("PaymentDetails")){
				objPaymentDetails = (PaymentDetailsTO)txnDetails.get("PaymentDetails");
			}

			//Interface Output attributes...
			SI_RETURN si_return = (SI_RETURN)brOutDetails.get(0);
			String blockedAcc = null;
			String dr_cr_indicator=null;
			ArrayList auth_mode_list = new ArrayList();
			ArrayList auth_doc_list = new ArrayList();
			ArrayList blockedAccountOut = new ArrayList();
			ArrayList brErrorMsgDesc = new ArrayList();
			ArrayList brWarningMsgDesc = new ArrayList();
			ArrayList brInfoMsgDesc = new ArrayList();

			String transferType = objPaymentDetails.getTransfer_Type();

			//Output Extraction and storage...
			if(si_return.getReturnCode()==0)
			{
				for(int i=0;i<si_return.getOutputVector().size();i++)
				{
					if(si_return.getOutputVector().get(i) instanceof BLKD_ACNT_OUT)
					{
						//Block Display output..
						BLKD_ACNT_OUT objBLKD_ACNT_OUT=(BLKD_ACNT_OUT)si_return.getOutputVector().get(i);
						ArrayList<String> blockedAccountDetails = new ArrayList<String>();
						blockedAcc = objBLKD_ACNT_OUT.getACNTNO();
						dr_cr_indicator = objBLKD_ACNT_OUT.getDR_CR();
						blockedAccountDetails.add(blockedAcc);
						blockedAccountDetails.add(dr_cr_indicator);
						blockedAccountOut.add(blockedAccountDetails);
					}
					else if(si_return.getOutputVector().get(i) instanceof BR_VALIDATION_LOG)
					{
						BR_VALIDATION_LOG objBr_validation_log = (BR_VALIDATION_LOG)si_return.getOutputVector().get(i);
						if(objBr_validation_log.getERR_LVL().equalsIgnoreCase("Error"))
						{
							//DR & CR indicator flag..
							if(objBr_validation_log.getCANCEL_TXN_FLG()==1){
								if(objBr_validation_log.getACC_IN_ELIGIBLE().equalsIgnoreCase("DR")){
									objBusinessRuleOutDetails.setDR_ACC_IN_ELIGIBLE(true);
								}
								else if(objBr_validation_log.getACC_IN_ELIGIBLE().equalsIgnoreCase("CR")){
									objBusinessRuleOutDetails.setCR_ACC_IN_ELIGIBLE(true);
								}
							}

							//Adding message description in the context and business rule out object...
							brErrorMsgDesc.add(objBr_validation_log.getERRMESG());
							context.addMessage(MessageType.ERROR,ChannelsErrorCodes.BUSSINESS_ERROR,objBr_validation_log.getERRMESG());
						}
						else if(objBr_validation_log.getERR_LVL().equalsIgnoreCase("Warning"))
						{
							//DR & CR indicator flag..
							if(objBr_validation_log.getCANCEL_TXN_FLG()==1){
								if(objBr_validation_log.getACC_IN_ELIGIBLE().equalsIgnoreCase("DR")){
									objBusinessRuleOutDetails.setDR_ACC_IN_ELIGIBLE(true);
								}
								else if(objBr_validation_log.getACC_IN_ELIGIBLE().equalsIgnoreCase("CR")){
									objBusinessRuleOutDetails.setCR_ACC_IN_ELIGIBLE(true);
								}
							}
							//RSA Review flag...
							if(objBr_validation_log.getRSA_REVIEW_FLAG()!=null && objBr_validation_log.getRSA_REVIEW_FLAG().trim().equalsIgnoreCase("Y")){
								objBusinessRuleOutDetails.setRsa_Review_Flag(objBr_validation_log.getRSA_REVIEW_FLAG());
							}
							//Approver Role...
							if(objBr_validation_log.getAPPROVER_ROLE()!=null && !objBr_validation_log.getAPPROVER_ROLE().trim().equalsIgnoreCase("")){
								objBusinessRuleOutDetails.setNEXT_APPROVAL_REQ(true);
								objBusinessRuleOutDetails.setNext_approver_role(objBr_validation_log.getAPPROVER_ROLE());
							}
							//Don't spawn flag..
							if(objBr_validation_log.getDONT_SPAWN_FLAG()!=null && objBr_validation_log.getDONT_SPAWN_FLAG().trim().equalsIgnoreCase("Y")){
								objBusinessRuleOutDetails.setDont_spawn_flag(objBr_validation_log.getDONT_SPAWN_FLAG());
							}
							//Authorization Required 
							if(objBr_validation_log.getAUTH_REQ_FLAG()!=null && objBr_validation_log.getAUTH_REQ_FLAG().equalsIgnoreCase("Y")){
								objBusinessRuleOutDetails.setAuth_indicator(objBr_validation_log.getAUTH_REQ_FLAG());
								auth_mode_list.add(objBr_validation_log.getAUTH_MODE());
								if(objBr_validation_log.getAUTH_MODE()!=null && (objBr_validation_log.getAUTH_MODE().equalsIgnoreCase(Auth_Mode.SIGNED) || objBr_validation_log.getAUTH_MODE().equalsIgnoreCase(Auth_Mode.EITHER))){
									if(!auth_doc_list.contains(objBr_validation_log.getDOCUMENTS_REQ())){
										auth_doc_list.add(objBr_validation_log.getDOCUMENTS_REQ());
									}
								}
							}
							//Check for the documents required flag..
							if(objBr_validation_log.getDOCUMENTS_REQ()!=null && !objBr_validation_log.getDOCUMENTS_REQ().trim().equalsIgnoreCase("")) {
								if(!auth_doc_list.contains(objBr_validation_log.getDOCUMENTS_REQ())){
									auth_doc_list.add(objBr_validation_log.getDOCUMENTS_REQ());
								}
							}
							//OFAC Case Id..
							if(objBr_validation_log.getOFAC_CASE_ID()!=null && !objBr_validation_log.getOFAC_CASE_ID().trim().equalsIgnoreCase("")){
								objBusinessRuleOutDetails.setOfac_case_id(objBr_validation_log.getOFAC_CASE_ID());
							}

							//Adding message description in the context and business rule out object...
							brWarningMsgDesc.add(objBr_validation_log.getERRMESG());
							context.addMessage(MessageType.WARNING,ChannelsErrorCodes.BUSSINESS_WARNING,objBr_validation_log.getERRMESG());
						}
						else
						{
							//DR & CR indicator flag..
							if(objBr_validation_log.getCANCEL_TXN_FLG()==1){
								if(objBr_validation_log.getACC_IN_ELIGIBLE().equalsIgnoreCase("DR")){
									objBusinessRuleOutDetails.setDR_ACC_IN_ELIGIBLE(true);
								}
								else if(objBr_validation_log.getACC_IN_ELIGIBLE().equalsIgnoreCase("CR")){
									objBusinessRuleOutDetails.setCR_ACC_IN_ELIGIBLE(true);
								}
							}
							//RSA Review flag...
							if(objBr_validation_log.getRSA_REVIEW_FLAG()!=null && objBr_validation_log.getRSA_REVIEW_FLAG().trim().equalsIgnoreCase("Y")){
								objBusinessRuleOutDetails.setRsa_Review_Flag(objBr_validation_log.getRSA_REVIEW_FLAG());
							}
							//Authorization Required 
							if(objBr_validation_log.getAUTH_REQ_FLAG()!=null && objBr_validation_log.getAUTH_REQ_FLAG().equalsIgnoreCase("Y")){
								objBusinessRuleOutDetails.setAuth_indicator(objBr_validation_log.getAUTH_REQ_FLAG());
								auth_mode_list.add(objBr_validation_log.getAUTH_MODE());
								if(objBr_validation_log.getAUTH_MODE()!=null && (objBr_validation_log.getAUTH_MODE().equalsIgnoreCase(Auth_Mode.SIGNED) || objBr_validation_log.getAUTH_MODE().equalsIgnoreCase(Auth_Mode.EITHER))){
									if(!auth_doc_list.contains(objBr_validation_log.getDOCUMENTS_REQ())){
										auth_doc_list.add(objBr_validation_log.getDOCUMENTS_REQ());
									}
								}
							}
							//Check for the documents required flag..
							if(objBr_validation_log.getDOCUMENTS_REQ()!=null && !objBr_validation_log.getDOCUMENTS_REQ().trim().equalsIgnoreCase("")) {
								if(!auth_doc_list.contains(objBr_validation_log.getDOCUMENTS_REQ())){
									auth_doc_list.add(objBr_validation_log.getDOCUMENTS_REQ());
								}
							}
							//OFAC Case Id..
							if(objBr_validation_log.getOFAC_CASE_ID()!=null && !objBr_validation_log.getOFAC_CASE_ID().trim().equalsIgnoreCase("")){
								objBusinessRuleOutDetails.setOfac_case_id(objBr_validation_log.getOFAC_CASE_ID());
							}

							//Adding message description in the context and business rule out object...
							brInfoMsgDesc.add(objBr_validation_log.getERRMESG());
							context.addMessage(MessageType.INFORMATION,ChannelsErrorCodes.BUSSINESS_INFORMATION,objBr_validation_log.getERRMESG());
						}
					}
				}

				//Setting the info..
				objBusinessRuleOutDetails.setBlockedAccountDetails(blockedAccountOut);
				objBusinessRuleOutDetails.setBrValidationOut(brOutDetails);
				objBusinessRuleOutDetails.setBrErrorMsgDesc(brErrorMsgDesc);
				objBusinessRuleOutDetails.setBrWarningMsgDesc(brWarningMsgDesc);
				objBusinessRuleOutDetails.setBrInfoMsgDesc(brInfoMsgDesc);

				//Setting the mode list and documents list in case of authorization indicator on...
				if(objBusinessRuleOutDetails.getAuth_indicator()!=null && objBusinessRuleOutDetails.getAuth_indicator().equalsIgnoreCase("Y")){
					objBusinessRuleOutDetails.setAuth_mode(getAuthMode(auth_mode_list,transferType));
					objBusinessRuleOutDetails.setSigned_documents_req(auth_doc_list);
				}
			}
			else
			{
				for(int k=0;k<si_return.getErrorVector().size();k++){
					if(si_return.getErrorVector().get(k) instanceof MS_INTERFACE_TECH_FAILURE ){
						context.addMessage(MessageType.CRITICAL,ChannelsErrorCodes.INTERNAL_ERROR);
						break;
					}
				}
			}
		}
		catch (Exception exception) {
			throw exception;
		}
		return objBusinessRuleOutDetails;
	}

	/**
	 * Gets the authorization mode list if required...
	 * CR 196 - Precedence of the authorization mode will be in the order of Signed , Either , Verbal...
	 * @param auth_mode_list
	 * @return
	 */
	private static String getAuthMode(ArrayList auth_mode_list,String transferType)
	{
		String auth_mode = "";
		if(!auth_mode_list.isEmpty())
		{
			if(auth_mode_list.contains(Auth_Mode.SIGNED)){
				auth_mode = Auth_Mode.SIGNED;
			}
			else if(auth_mode_list.contains(Auth_Mode.EITHER)){
				auth_mode = Auth_Mode.EITHER;
			}
			else if(auth_mode_list.contains(Auth_Mode.VERBAL)){
				auth_mode = Auth_Mode.VERBAL;
			}
		}
		return auth_mode;
	}

	/**
	 * Constructs a <code>String</code> with all attributes
	 * in name = value format.
	 *
	 * @return a <code>String</code> representation 
	 * of this object.
	 */
	public String toString()
	{
		final String TAB = "\r\n";

		String retValue = "";

		retValue = "BusinessRulesService ( "
			+ super.toString() + TAB
			+ "blockedAccountDetails = " + this.blockedAccountDetails + TAB
			+ "brErrorMsgDesc = " + this.brErrorMsgDesc + TAB
			+ "brWarningMsgDesc = " + this.brWarningMsgDesc + TAB
			+ "brInfoMsgDesc = " + this.brInfoMsgDesc + TAB
			+ "brValidationOut = " + this.brValidationOut + TAB
			+ "rsa_Review_Flag = " + this.rsa_Review_Flag + TAB
			+ "next_approver_role = " + this.next_approver_role + TAB
			+ "auth_indicator = " + this.auth_indicator + TAB
			+ "auth_mode = " + this.auth_mode + TAB
			+ "dont_spawn_flag = " + this.dont_spawn_flag + TAB
			+ "ofac_case_id = " + this.ofac_case_id + TAB
			+ "signed_documents_req = " + this.signed_documents_req + TAB
			+ "DR_ACC_IN_ELIGIBLE = " + this.DR_ACC_IN_ELIGIBLE + TAB
			+ "CR_ACC_IN_ELIGIBLE = " + this.CR_ACC_IN_ELIGIBLE + TAB
			+ "NEXT_APPROVAL_REQ = " + this.NEXT_APPROVAL_REQ + TAB
			+ " )";

		return retValue;
	}

}
