package com.tcs.ebw.serverside.services.channelPaymentServices;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Vector;
import com.tcs.Payments.EAITO.BLKD_ACNT_OUT;
import com.tcs.Payments.EAITO.BR_VALIDATION_LOG;
import com.tcs.Payments.EAITO.MS_INTERFACE_TECH_FAILURE;
import com.tcs.bancs.channels.ChannelsErrorCodes;
import com.tcs.bancs.channels.context.MessageType;
import com.tcs.bancs.channels.context.ServiceContext;
import com.tcs.ebw.common.util.EBWLogger;
import com.tcs.mswitch.common.channel.SI_RETURN;

/**
 * @author : 224703
 * **************   Revision History ************************
 * Modified-By |  Modified-Date |  Project-Phase  | Changes
 *   224703         12-05-2011        3              3rd Party ACH
 *   224703         01-07-2011        3               CR 262
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
	private ArrayList brRiskMsgDesc;
	private ArrayList brWarningMsgDesc;
	private ArrayList brInfoMsgDesc;
	private Vector brValidationOut;
	private String rsa_Review_Flag = "N";
	private String next_approver_role;
	private String dont_spawn_flag = "N";
	private String ofac_case_id;

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
	public String getDont_spawn_flag() {
		return dont_spawn_flag;
	}
	public void setDont_spawn_flag(String dont_spawn_flag) {
		this.dont_spawn_flag = dont_spawn_flag;
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
	public ArrayList getBrRiskMsgDesc() {
		return brRiskMsgDesc;
	}
	public void setBrRiskMsgDesc(ArrayList brRiskMsgDesc) {
		this.brRiskMsgDesc = brRiskMsgDesc;
	}
	public String getOfac_case_id() {
		return ofac_case_id;
	}
	public void setOfac_case_id(String ofac_case_id) {
		this.ofac_case_id = ofac_case_id;
	}

	/**
	 * Business Rule response extraction code...
	 * @param brOutDetails
	 * @param context
	 * @return
	 * @throws Exception
	 */
	public static BusinessRulesService getBusinessRuleOutput(Vector<Object> brOutDetails,ServiceContext context) throws Exception
	{
		EBWLogger.logDebug("PaymentOutDetails","SI Returned Vector for Business Rule Input....");
		BusinessRulesService objBusinessRuleOutDetails = new BusinessRulesService();
		try 
		{
			//Interface Output attributes...
			SI_RETURN si_return = (SI_RETURN)brOutDetails.get(0);
			String blockedAcc = null;
			String dr_cr_indicator=null;
			ArrayList blockedAccountOut = new ArrayList();
			ArrayList brErrorMsgDesc = new ArrayList();
			ArrayList brRiskMsgDesc = new ArrayList();
			ArrayList brWarningMsgDesc = new ArrayList();
			ArrayList brInfoMsgDesc = new ArrayList();

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
						dr_cr_indicator =objBLKD_ACNT_OUT.getDR_CR();
						blockedAccountDetails.add(blockedAcc);
						blockedAccountDetails.add(dr_cr_indicator);
						blockedAccountOut.add(blockedAccountDetails);
					}
					else if(si_return.getOutputVector().get(i) instanceof BR_VALIDATION_LOG)
					{
						BR_VALIDATION_LOG objBr_validation_log = (BR_VALIDATION_LOG)si_return.getOutputVector().get(i);
						if(objBr_validation_log.getERR_LVL().equalsIgnoreCase("Error"))
						{
							//Adding message description in the context and business rule out object...
							brErrorMsgDesc.add(objBr_validation_log.getRULE_NAME());
							context.addMessage(MessageType.ERROR,ChannelsErrorCodes.BUSSINESS_ERROR,objBr_validation_log.getRULE_NAME());
						}
						else if(objBr_validation_log.getERR_LVL().equalsIgnoreCase("Risk"))
						{
							//RSA Review flag...
							if(objBr_validation_log.getRSA_REVIEW_FLAG()!=null && objBr_validation_log.getRSA_REVIEW_FLAG().trim().equalsIgnoreCase("Y")){
								objBusinessRuleOutDetails.setRsa_Review_Flag(objBr_validation_log.getRSA_REVIEW_FLAG());
							}
							//Approver Role...
							if(objBr_validation_log.getAPPROVER_ROLE()!=null && !objBr_validation_log.getAPPROVER_ROLE().trim().equalsIgnoreCase("")){
								objBusinessRuleOutDetails.setNext_approver_role(objBr_validation_log.getAPPROVER_ROLE());
							}
							//Don't spawn flag..
							if(objBr_validation_log.getDONT_SPAWN_FLAG()!=null && objBr_validation_log.getDONT_SPAWN_FLAG().trim().equalsIgnoreCase("Y")){
								objBusinessRuleOutDetails.setDont_spawn_flag(objBr_validation_log.getDONT_SPAWN_FLAG());
							}
							//OFAC Case Id..
							if(objBr_validation_log.getOFAC_CASE_ID()!=null && !objBr_validation_log.getOFAC_CASE_ID().trim().equalsIgnoreCase("")){
								objBusinessRuleOutDetails.setOfac_case_id(objBr_validation_log.getOFAC_CASE_ID());
							}

							//Adding message description in the context and business rule out object...
							brRiskMsgDesc.add(objBr_validation_log.getRULE_NAME());
							context.addMessage(MessageType.RISK,ChannelsErrorCodes.BUSSINESS_RISK,objBr_validation_log.getRULE_NAME());
						}
						else if(objBr_validation_log.getERR_LVL().equalsIgnoreCase("Warning"))
						{
							//RSA Review flag...
							if(objBr_validation_log.getRSA_REVIEW_FLAG()!=null && objBr_validation_log.getRSA_REVIEW_FLAG().trim().equalsIgnoreCase("Y")){
								objBusinessRuleOutDetails.setRsa_Review_Flag(objBr_validation_log.getRSA_REVIEW_FLAG());
							}
							//Approver Role...
							if(objBr_validation_log.getAPPROVER_ROLE()!=null && !objBr_validation_log.getAPPROVER_ROLE().trim().equalsIgnoreCase("")){
								objBusinessRuleOutDetails.setNext_approver_role(objBr_validation_log.getAPPROVER_ROLE());
							}
							//Don't spawn flag..
							if(objBr_validation_log.getDONT_SPAWN_FLAG()!=null && objBr_validation_log.getDONT_SPAWN_FLAG().trim().equalsIgnoreCase("Y")){
								objBusinessRuleOutDetails.setDont_spawn_flag(objBr_validation_log.getDONT_SPAWN_FLAG());
							}
							//OFAC Case Id..
							if(objBr_validation_log.getOFAC_CASE_ID()!=null && !objBr_validation_log.getOFAC_CASE_ID().trim().equalsIgnoreCase("")){
								objBusinessRuleOutDetails.setOfac_case_id(objBr_validation_log.getOFAC_CASE_ID());
							}

							//Adding message description in the context and business rule out object...
							brWarningMsgDesc.add(objBr_validation_log.getRULE_NAME());
							context.addMessage(MessageType.WARNING,ChannelsErrorCodes.BUSSINESS_WARNING,objBr_validation_log.getRULE_NAME());
						}
						else 
						{
							//RSA Review flag...
							if(objBr_validation_log.getRSA_REVIEW_FLAG()!=null && objBr_validation_log.getRSA_REVIEW_FLAG().trim().equalsIgnoreCase("Y")){
								objBusinessRuleOutDetails.setRsa_Review_Flag(objBr_validation_log.getRSA_REVIEW_FLAG());
							}

							//OFAC Case Id..
							if(objBr_validation_log.getOFAC_CASE_ID()!=null && !objBr_validation_log.getOFAC_CASE_ID().trim().equalsIgnoreCase("")){
								objBusinessRuleOutDetails.setOfac_case_id(objBr_validation_log.getOFAC_CASE_ID());
							}

							//Adding message description in the context and business rule out object...
							brInfoMsgDesc.add(objBr_validation_log.getRULE_NAME());
							context.addMessage(MessageType.INFORMATION,ChannelsErrorCodes.BUSSINESS_INFORMATION,objBr_validation_log.getRULE_NAME());
						}
					}
				}

				//Setting the info..
				objBusinessRuleOutDetails.setBlockedAccountDetails(blockedAccountOut);
				objBusinessRuleOutDetails.setBrValidationOut(brOutDetails);
				objBusinessRuleOutDetails.setBrErrorMsgDesc(brErrorMsgDesc);
				objBusinessRuleOutDetails.setBrRiskMsgDesc(brRiskMsgDesc);
				objBusinessRuleOutDetails.setBrWarningMsgDesc(brWarningMsgDesc);
				objBusinessRuleOutDetails.setBrInfoMsgDesc(brInfoMsgDesc);
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
			+ "brRiskMsgDesc = " + this.brRiskMsgDesc + TAB
			+ "brWarningMsgDesc = " + this.brWarningMsgDesc + TAB
			+ "brInfoMsgDesc = " + this.brInfoMsgDesc + TAB
			+ "brValidationOut = " + this.brValidationOut + TAB
			+ "rsa_Review_Flag = " + this.rsa_Review_Flag + TAB
			+ "next_approver_role = " + this.next_approver_role + TAB
			+ "dont_spawn_flag = " + this.dont_spawn_flag + TAB
			+ "ofac_case_id = " + this.ofac_case_id + TAB
			+ " )";

		return retValue;
	}

}
