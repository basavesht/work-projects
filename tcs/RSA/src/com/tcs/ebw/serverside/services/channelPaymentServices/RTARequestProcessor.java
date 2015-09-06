/**
 * 
 */
package com.tcs.ebw.serverside.services.channelPaymentServices;

import java.util.Date;
import java.util.HashMap;

import com.tcs.Payments.ms360Utils.ChkReqConstants;
import com.tcs.Payments.ms360Utils.RTAActionKeyDesc;
import com.tcs.Payments.ms360Utils.TxnTypeCode;
import com.tcs.ebw.common.util.ConvertionUtil;
import com.tcs.ebw.payments.transferobject.PaymentDetailsTO;

/**
 * @author : 224703
 * **************   Revision History ************************
 * Modified-By |  Modified-Date |  Project-Phase  | Changes
 *    224703       01-05-2011        2               CR 215
 *    224703       01-05-2011        3               Internal 24x7
 *    224703       01-05-2011        3               3rd Party ACH
 * **********************************************************
 */
public class RTARequestProcessor {

	//RTA Booked In Flag..
	public enum BookedInFlag {RTA_PENDING,RTA_INTRADAY,RTA_CLEAR};

	//Transfer types..
	public enum TransferType {INTERNAL,ACH_WITHDRAWAL,ACH_DEPOSIT,CHK_LOCAL,CHK_REGIONAL};

	//Transaction Statuses..
	public static final int AWAITING_APPROVAL = 2;
	public static final int EXECUTED  = 4;
	public static final int SCHEDULED  = 6;
	public static final int CANCELLED = 20;
	public static final int NOT_APPROVED  = 44;
	public static final int SUSPENDED = 46;
	public static final int RETURNED = 48;
	public static final int EXPIRED = 50;
	public static final int SYSTEM_REJECTED  = 52;
	public static final int PENDING_RISK_REVIEW = 80;
	public static final int PRINTED  = 101;
	public static final int SENT_TO_PRINTER = 102;
	public static final int AWAITING_PRINT  = 103;
	public static final int PRINT_FAILED = 110; 

	//RTA Action Key..
	public static final String NO_ACTION  = "0";
	public static final String CRT_PEND   = "1";
	public static final String MOD_PEND   = "2";
	public static final String DEL_PEND   = "3";
	public static final String APR_TRAN   = "4";
	public static final String CRT_APRV   = "5"; //Action Key is same for CRT_APRV and REV_RTA..
	public static final String REV_RTA    = "5"; //Action Key is same for CRT_APRV and REV_RTA..

	/**
	 * Determines the action as per the specifications document. 
	 * @param txnDetails
	 * @throws Exception 
	 */
	public static boolean getRTAAction(HashMap txnDetails) throws Exception
	{ 
		boolean isRTAActionRequired = false;
		boolean isReverseRTARequired = false;
		boolean isCancelRTARequired = false;
		String action_key = NO_ACTION;
		BookedInFlag  booked_in_flag = BookedInFlag.RTA_CLEAR;
		try 
		{
			//Transaction details..
			PaymentDetailsTO objPaymentDetails = new PaymentDetailsTO();
			if(txnDetails.containsKey("PaymentDetails")){
				objPaymentDetails = (PaymentDetailsTO)txnDetails.get("PaymentDetails");
			}

			//RTA Input details extraction...
			TransferType txn_type = setTransferType(objPaymentDetails.getTransfer_Type());

			//Transaction State determination...
			Date transferDate = ConvertionUtil.convertToDate(objPaymentDetails.getRequestedDate());
			Date businessDate = ConvertionUtil.convertToDate(objPaymentDetails.getBusiness_Date());
			boolean isCurrentDated = false;
			boolean isFutureDated = false;
			if(transferDate.after(businessDate)){
				isFutureDated = true;
			}
			else {
				isCurrentDated = true;
			}

			//Previous and current status before and after the action...
			int previousTxnStatus = 0;
			if(objPaymentDetails.getTxnPrevStatusCode()!=null){
				previousTxnStatus = new Integer(objPaymentDetails.getTxnPrevStatusCode()).intValue();
			}
			int currentTxnStatus = 0;
			if(objPaymentDetails.getTxnCurrentStatusCode()!=null){
				currentTxnStatus = new Integer(objPaymentDetails.getTxnCurrentStatusCode()).intValue();
			}
			BookedInFlag prev_booked_in_flag = setBookedInFlag(objPaymentDetails.getRta_booked_flag());


			//Create Transaction Case...................
			if(objPaymentDetails.isTxnInitiated())
			{
				if(isCurrentDated)
				{
					if(currentTxnStatus == AWAITING_APPROVAL){
						action_key = CRT_PEND;
						booked_in_flag = BookedInFlag.RTA_PENDING;
					}
					else if(currentTxnStatus == EXECUTED){
						action_key = CRT_APRV;
						booked_in_flag = BookedInFlag.RTA_INTRADAY;
					}
					else if(currentTxnStatus == AWAITING_PRINT){
						if(txn_type == TransferType.CHK_LOCAL){
							action_key = CRT_PEND;
							booked_in_flag = BookedInFlag.RTA_PENDING;
						}
					}
					else if(currentTxnStatus == PENDING_RISK_REVIEW){
						if(txn_type == TransferType.ACH_WITHDRAWAL){
							action_key = CRT_PEND;
							booked_in_flag = BookedInFlag.RTA_PENDING;
						}
					}
				}
			}


			//Modify Transaction case................
			if(objPaymentDetails.isTxnModified())
			{
				if(isCurrentDated)
				{
					if(previousTxnStatus == SCHEDULED && currentTxnStatus == AWAITING_APPROVAL){
						action_key = CRT_PEND;
						booked_in_flag = BookedInFlag.RTA_PENDING;
					}
					else if(previousTxnStatus == SCHEDULED && currentTxnStatus == EXECUTED){
						action_key = CRT_APRV;
						booked_in_flag = BookedInFlag.RTA_INTRADAY;
					}
					else if(previousTxnStatus == SCHEDULED && currentTxnStatus == AWAITING_PRINT){
						action_key = CRT_PEND;
						booked_in_flag = BookedInFlag.RTA_PENDING;
					}
					else if(previousTxnStatus == SCHEDULED  && currentTxnStatus == PENDING_RISK_REVIEW){
						action_key = CRT_PEND;
						booked_in_flag = BookedInFlag.RTA_PENDING;
					}
					else if(previousTxnStatus == AWAITING_APPROVAL && currentTxnStatus == EXECUTED){
						if(prev_booked_in_flag == null || prev_booked_in_flag == BookedInFlag.RTA_CLEAR){
							action_key = CRT_APRV;
							booked_in_flag = BookedInFlag.RTA_INTRADAY;
						}
						else if(prev_booked_in_flag == BookedInFlag.RTA_PENDING){
							action_key = APR_TRAN;
							booked_in_flag = BookedInFlag.RTA_INTRADAY;
						}
					}
					else if(previousTxnStatus == AWAITING_APPROVAL && currentTxnStatus == AWAITING_APPROVAL){
						if(prev_booked_in_flag == null  || prev_booked_in_flag == BookedInFlag.RTA_CLEAR){
							action_key = CRT_PEND;
							booked_in_flag = BookedInFlag.RTA_PENDING;
						}
						else if(prev_booked_in_flag == BookedInFlag.RTA_PENDING){
							action_key = MOD_PEND;
							booked_in_flag = BookedInFlag.RTA_PENDING;
						}
					}
					else if(previousTxnStatus == AWAITING_APPROVAL && currentTxnStatus == AWAITING_PRINT){
						if(prev_booked_in_flag == null  || prev_booked_in_flag == BookedInFlag.RTA_CLEAR){
							action_key = CRT_PEND;
							booked_in_flag = BookedInFlag.RTA_PENDING;
						}
						else if(prev_booked_in_flag == BookedInFlag.RTA_PENDING){
							action_key = MOD_PEND;
							booked_in_flag = BookedInFlag.RTA_PENDING;
						}
					}
					else if(previousTxnStatus == AWAITING_APPROVAL  && currentTxnStatus == PENDING_RISK_REVIEW){
						if(prev_booked_in_flag == null  || prev_booked_in_flag == BookedInFlag.RTA_CLEAR){
							action_key = CRT_PEND;
							booked_in_flag = BookedInFlag.RTA_PENDING;
						}
						else if(prev_booked_in_flag == BookedInFlag.RTA_PENDING){
							action_key = MOD_PEND;
							booked_in_flag = BookedInFlag.RTA_PENDING;
						}
					}
					else if(previousTxnStatus == PENDING_RISK_REVIEW  && currentTxnStatus == PENDING_RISK_REVIEW){
						if(prev_booked_in_flag == null  || prev_booked_in_flag == BookedInFlag.RTA_CLEAR){
							action_key = CRT_PEND;
							booked_in_flag = BookedInFlag.RTA_PENDING;
						}
						else if(prev_booked_in_flag == BookedInFlag.RTA_PENDING){
							action_key = MOD_PEND;
							booked_in_flag = BookedInFlag.RTA_PENDING;
						}
					}
				}
				else if(isFutureDated)
				{
					if(previousTxnStatus == AWAITING_APPROVAL && currentTxnStatus == SCHEDULED){
						if(prev_booked_in_flag == BookedInFlag.RTA_PENDING){
							action_key = DEL_PEND;
							booked_in_flag = BookedInFlag.RTA_CLEAR;
						}
					}
					else if(previousTxnStatus == AWAITING_APPROVAL && currentTxnStatus == AWAITING_APPROVAL){
						if(prev_booked_in_flag == BookedInFlag.RTA_PENDING){
							action_key = DEL_PEND;
							booked_in_flag = BookedInFlag.RTA_CLEAR;
						}
					}
					else if(previousTxnStatus == AWAITING_APPROVAL  && currentTxnStatus == PENDING_RISK_REVIEW){
						if(prev_booked_in_flag == BookedInFlag.RTA_PENDING){
							if(txn_type == TransferType.ACH_WITHDRAWAL){
								action_key = DEL_PEND;
								booked_in_flag = BookedInFlag.RTA_CLEAR;
							}
						}
					}
					else if(previousTxnStatus == PENDING_RISK_REVIEW  && currentTxnStatus == PENDING_RISK_REVIEW){
						if(prev_booked_in_flag == BookedInFlag.RTA_PENDING){
							if(txn_type == TransferType.ACH_WITHDRAWAL){
								action_key = DEL_PEND;
								booked_in_flag = BookedInFlag.RTA_CLEAR;
							}
						}
					}
				}
			}


			//Cancel , Reject ,Void and Cancel Transaction case.....
			if(objPaymentDetails.isTxnCancelled())
			{
				if(previousTxnStatus == AWAITING_APPROVAL){
					if(prev_booked_in_flag == BookedInFlag.RTA_PENDING){
						action_key = DEL_PEND;
						booked_in_flag = BookedInFlag.RTA_CLEAR;
					}
				}
				else if(previousTxnStatus == PENDING_RISK_REVIEW){
					if(prev_booked_in_flag == BookedInFlag.RTA_PENDING){
						action_key = DEL_PEND;
						booked_in_flag = BookedInFlag.RTA_CLEAR;
					}
				}
				else if(previousTxnStatus == AWAITING_PRINT){
					if(prev_booked_in_flag == BookedInFlag.RTA_PENDING){
						action_key = DEL_PEND;
						booked_in_flag = BookedInFlag.RTA_CLEAR;
					}
				}
				else if(previousTxnStatus == SENT_TO_PRINTER){
					if(prev_booked_in_flag == BookedInFlag.RTA_PENDING){
						action_key = DEL_PEND;
						booked_in_flag = BookedInFlag.RTA_CLEAR;
					}
					else if(prev_booked_in_flag == BookedInFlag.RTA_INTRADAY){
						action_key = REV_RTA;
						booked_in_flag = BookedInFlag.RTA_CLEAR;
						isReverseRTARequired = true;
					}
				}
				else if(previousTxnStatus == PRINT_FAILED){
					if(prev_booked_in_flag == BookedInFlag.RTA_PENDING){
						action_key = DEL_PEND;
						booked_in_flag = BookedInFlag.RTA_CLEAR;
					}
					else if(prev_booked_in_flag == BookedInFlag.RTA_INTRADAY){
						action_key = REV_RTA;
						booked_in_flag = BookedInFlag.RTA_CLEAR;
						isReverseRTARequired = true;
					}
				}
				else if(previousTxnStatus == PRINTED){
					if(prev_booked_in_flag == BookedInFlag.RTA_INTRADAY){
						action_key = REV_RTA;
						booked_in_flag = BookedInFlag.RTA_CLEAR;
						isReverseRTARequired = true;
					}
				}
				else if(previousTxnStatus == EXECUTED){
					if(prev_booked_in_flag == BookedInFlag.RTA_INTRADAY){
						action_key = REV_RTA;
						booked_in_flag = BookedInFlag.RTA_CLEAR;
						isCancelRTARequired = true;
					}
				}
			}


			//Suspend Transaction case.....
			if(objPaymentDetails.isParentTxnSuspended())
			{
				if(previousTxnStatus == AWAITING_APPROVAL){
					if(prev_booked_in_flag == BookedInFlag.RTA_PENDING){
						action_key = DEL_PEND;
						booked_in_flag = BookedInFlag.RTA_CLEAR;
					}
				}
				if(previousTxnStatus == PENDING_RISK_REVIEW){
					if(prev_booked_in_flag == BookedInFlag.RTA_PENDING){
						if(txn_type == TransferType.ACH_WITHDRAWAL){
							action_key = DEL_PEND;
							booked_in_flag = BookedInFlag.RTA_CLEAR;
						}
					}
				}
			}

			//Approve Transaction case.....
			if(objPaymentDetails.isTxnApproved())
			{
				if(previousTxnStatus == AWAITING_APPROVAL && currentTxnStatus == SYSTEM_REJECTED){
					if(prev_booked_in_flag == BookedInFlag.RTA_PENDING){
						action_key = DEL_PEND;
						booked_in_flag = BookedInFlag.RTA_CLEAR;
					}
				}
				else if(previousTxnStatus == PENDING_RISK_REVIEW && currentTxnStatus == SYSTEM_REJECTED){
					if(prev_booked_in_flag == BookedInFlag.RTA_PENDING){
						action_key = DEL_PEND;
						booked_in_flag = BookedInFlag.RTA_CLEAR;
					}
				}
				else if(isCurrentDated)
				{
					if(previousTxnStatus == AWAITING_APPROVAL && currentTxnStatus == AWAITING_APPROVAL){
						if(prev_booked_in_flag == null  || prev_booked_in_flag == BookedInFlag.RTA_CLEAR){
							action_key = CRT_PEND;
							booked_in_flag = BookedInFlag.RTA_PENDING;
						}
					}
					else if(previousTxnStatus == AWAITING_APPROVAL && currentTxnStatus == EXECUTED){
						if(prev_booked_in_flag == null  || prev_booked_in_flag == BookedInFlag.RTA_CLEAR){
							action_key = CRT_APRV;
							booked_in_flag = BookedInFlag.RTA_INTRADAY;
						}
						else if(prev_booked_in_flag == BookedInFlag.RTA_PENDING){
							action_key = APR_TRAN;
							booked_in_flag = BookedInFlag.RTA_INTRADAY;
						}
					}
					else if(previousTxnStatus == AWAITING_APPROVAL && currentTxnStatus == AWAITING_PRINT){
						if(prev_booked_in_flag == null  || prev_booked_in_flag == BookedInFlag.RTA_CLEAR){
							action_key = CRT_PEND;
							booked_in_flag = BookedInFlag.RTA_PENDING;
						}
					}
					else if(previousTxnStatus == PENDING_RISK_REVIEW && currentTxnStatus == AWAITING_APPROVAL){
						if(prev_booked_in_flag == null  || prev_booked_in_flag == BookedInFlag.RTA_CLEAR){
							action_key = CRT_PEND;
							booked_in_flag = BookedInFlag.RTA_PENDING;
						}
					}
					else if(previousTxnStatus == PENDING_RISK_REVIEW && currentTxnStatus == EXECUTED){
						if(prev_booked_in_flag == null  || prev_booked_in_flag == BookedInFlag.RTA_CLEAR){
							action_key = CRT_APRV;
							booked_in_flag = BookedInFlag.RTA_INTRADAY;
						}
						else if(prev_booked_in_flag == BookedInFlag.RTA_PENDING){
							action_key = APR_TRAN;
							booked_in_flag = BookedInFlag.RTA_INTRADAY;
						}
					}
				}
			}

			//Print Check case...
			if(objPaymentDetails.isCheckPrinted())
			{
				if(previousTxnStatus == SENT_TO_PRINTER && currentTxnStatus == PRINTED){
					if(prev_booked_in_flag == BookedInFlag.RTA_PENDING){
						action_key = APR_TRAN;
						booked_in_flag = BookedInFlag.RTA_INTRADAY;
					}
				}
			}

			//Output mappings to the PaymentDetails TO...
			objPaymentDetails.setRta_Action_key(ConvertionUtil.convertToDouble(action_key));
			objPaymentDetails.setCurrent_rta_booked_flag(getBookedInFlagValue(booked_in_flag));
			objPaymentDetails.setReverseRTAFlag(isReverseRTARequired);
			objPaymentDetails.setCancelRTAFlag(isCancelRTARequired);

			//Returned action key and booked in flag is .....
			System.out.println("RTA Action Key : " + action_key);
			System.out.println("RTA Booked In Flag : " + booked_in_flag);
		} 
		catch (Exception exception) {
			throw exception;
		}
		finally 
		{
			if(action_key != null && action_key != NO_ACTION){
				isRTAActionRequired = true;
			}
		}
		return isRTAActionRequired;
	}

	/**
	 * Getting the RTA action key for the fee amount based on the RTA action key for transaction amount...
	 * @param txnDetails
	 * @return
	 * @throws Exception
	 */
	public static boolean getRTAAction_Fee(HashMap txnDetails) throws Exception 
	{
		boolean isRTAAction_FeeRequired = false;
		String feeAmount_ActionKey = NO_ACTION;
		try 
		{
			//Transaction details..
			PaymentDetailsTO objPaymentDetails = new PaymentDetailsTO();
			if(txnDetails.containsKey("PaymentDetails")){
				objPaymentDetails = (PaymentDetailsTO)txnDetails.get("PaymentDetails");
			}

			String txnAmount_ActionKey = ConvertionUtil.convertToString(objPaymentDetails.getRta_Action_key());

			//Finding the action key for the fee amount based on the transaction amount rta action key....
			if (txnAmount_ActionKey.equals(CRT_PEND)){
				feeAmount_ActionKey = CRT_PEND; 
			}
			else if (txnAmount_ActionKey.equals(MOD_PEND)){
				feeAmount_ActionKey = CRT_PEND;
			}
			else if (txnAmount_ActionKey.equals(DEL_PEND)){
				feeAmount_ActionKey = NO_ACTION;
			}
			else if (txnAmount_ActionKey.equals(APR_TRAN)){
				feeAmount_ActionKey = CRT_APRV;
			}
			else if (txnAmount_ActionKey.equals(CRT_APRV)){
				feeAmount_ActionKey = CRT_APRV;
			}
			else if (txnAmount_ActionKey.equals(REV_RTA)){
				feeAmount_ActionKey = REV_RTA;
			}

			//Setting the action key for the fee amount...
			objPaymentDetails.setRta_Action_Key_Fee(ConvertionUtil.convertToDouble(feeAmount_ActionKey));
		}
		catch (Exception exception){
			throw exception;
		}
		finally 
		{
			if(feeAmount_ActionKey != null && feeAmount_ActionKey != NO_ACTION){
				isRTAAction_FeeRequired = true;
			}
		}
		return isRTAAction_FeeRequired;
	}

	/**
	 * Sets the enum BookedInFlag type for the booked in flag...
	 * @param bookedInFlag
	 * @return
	 */
	public static BookedInFlag setBookedInFlag(String bookedInFlag)
	{
		BookedInFlag bookingFlag = null;
		if(bookedInFlag!=null && bookedInFlag.equalsIgnoreCase(RTAActionKeyDesc.RTA_INTRADAY)){
			bookingFlag = BookedInFlag.RTA_INTRADAY;
		}
		else if(bookedInFlag!=null && bookedInFlag.equalsIgnoreCase(RTAActionKeyDesc.RTA_PENDING)){
			bookingFlag = BookedInFlag.RTA_PENDING;
		}
		else {
			bookingFlag = BookedInFlag.RTA_CLEAR;
		}
		return bookingFlag;
	}

	/**
	 * Gets the String value for the enum bookedInFlag type..
	 * @param bookedInFlag
	 * @return
	 */
	public static String getBookedInFlagValue(BookedInFlag bookedInFlag)
	{
		String rta_booked_in_flag = null;
		if(bookedInFlag == BookedInFlag.RTA_INTRADAY){
			rta_booked_in_flag = RTAActionKeyDesc.RTA_INTRADAY;
		}
		else if(bookedInFlag == BookedInFlag.RTA_PENDING){
			rta_booked_in_flag = RTAActionKeyDesc.RTA_PENDING;
		}
		else {
			rta_booked_in_flag = RTAActionKeyDesc.RTA_CLEAR;
		}
		return rta_booked_in_flag;
	}

	/**
	 * Set the enum TransferType type for the transfer type..
	 * @param transferType
	 * @return
	 */
	public static TransferType setTransferType(String transferType)
	{
		TransferType txnType = null;
		if(transferType!=null && transferType.equalsIgnoreCase(TxnTypeCode.INTERNAL)){
			txnType = TransferType.INTERNAL;
		}
		else if(transferType!=null && transferType.equalsIgnoreCase(TxnTypeCode.ACH_DEPOSIT)){
			txnType = TransferType.ACH_DEPOSIT;
		}
		else if(transferType!=null && transferType.equalsIgnoreCase(TxnTypeCode.ACH_WITHDRAWAL)){
			txnType = TransferType.ACH_WITHDRAWAL;
		}
		else if(transferType!=null && transferType.equalsIgnoreCase(ChkReqConstants.CHK_REG)){
			txnType = TransferType.CHK_REGIONAL;
		}
		else if(transferType!=null && transferType.equalsIgnoreCase(ChkReqConstants.CHK_LOC)){
			txnType = TransferType.CHK_LOCAL;
		}
		return txnType;
	}
}
