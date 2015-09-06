package com.tcs.utilities.dbBulking;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author 224703
 *
 */
public class TxnStatusDistributionHandler extends DBBulking
{
	//Past/Present Dated transactions (in %)	

	//Internal Transfers...
	//PastDated txns...
	private final static Double INT_TXN_STATUS_4_PAST = 94D ;
	private final static Double INT_TXN_STATUS_20 = 2.5D ;
	private final static Double INT_TXN_STATUS_44 = 1D ;
	private final static Double INT_TXN_STATUS_52 = 2D ;
	private final static Double INT_TXN_STATUS_50 = 0.5D ;

	//Present/Future txns...
	private final static Double INT_TXN_STATUS_2 = 4D ;
	private final static Double INT_TXN_STATUS_4 = 10D ;
	private final static Double INT_TXN_STATUS_6 = 86D ;


	//External Transfers (ACH-OUT) ...
	//PastDated txns..
	private final static Double ACH_OUT_TXN_STATUS_4_PAST = 92.5D ;
	private final static Double ACH_OUT_TXN_STATUS_20 = 4D ;
	private final static Double ACH_OUT_TXN_STATUS_44 = 1D ;
	private final static Double ACH_OUT_TXN_STATUS_46 = 0.5D ;
	private final static Double ACH_OUT_TXN_STATUS_52 = 1.5D ;
	private final static Double ACH_OUT_TXN_STATUS_50 = 0.5D ;

	//Present/Future txns...
	private final static Double ACH_OUT_TXN_STATUS_2 = 4D ;
	private final static Double ACH_OUT_TXN_STATUS_4 = 10D ;
	private final static Double ACH_OUT_TXN_STATUS_6 = 86D ;

	//External Transfers (ACH-IN) ...
	//PastDated txns...
	private final static Double ACH_IN_TXN_STATUS_4_PAST = 92.5D ;
	private final static Double ACH_IN_TXN_STATUS_20 = 4D ;
	private final static Double ACH_IN_TXN_STATUS_44 = 1D ;
	private final static Double ACH_IN_TXN_STATUS_46 = 0.5D ;
	private final static Double ACH_IN_TXN_STATUS_52 = 1.5D ;
	private final static Double ACH_IN_TXN_STATUS_50 = 0.5D ;

	//Present/Future txns...
	private final static Double ACH_IN_TXN_STATUS_2 = 4D ;
	private final static Double ACH_IN_TXN_STATUS_4 = 10D ;
	private final static Double ACH_IN_TXN_STATUS_6 = 86D ;

	//Check Transfers (CHK-REG) ...
	//PastDated txns...
	private final static Double CHK_REG_TXN_STATUS_4_PAST = 92.5D ;
	private final static Double CHK_REG_TXN_STATUS_20 = 4D ;
	private final static Double CHK_REG_TXN_STATUS_44 = 1D ;
	private final static Double CHK_REG_TXN_STATUS_52 = 2D ;
	private final static Double CHK_REG_TXN_STATUS_50 = 0.5D ;

	//Present/Future txns...
	private final static Double CHK_REG_TXN_STATUS_2 = 4D ;
	private final static Double CHK_REG_TXN_STATUS_4 = 10D ;
	private final static Double CHK_REG_TXN_STATUS_6 = 86D ;

	//Check Transfers (CHK-LOC) ...
	//PastDated txns...
	private final static Double CHK_LOC_TXN_STATUS_101 = 92.5D ;
	private final static Double CHK_LOC_TXN_STATUS_20 = 4D ;
	private final static Double CHK_LOC_TXN_STATUS_44 = 1D ;
	private final static Double CHK_LOC_TXN_STATUS_52 = 2D ;
	private final static Double CHK_LOC_TXN_STATUS_50 = 0.5D ;

	//Present/Future txns...
	private final static Double CHK_LOC_TXN_STATUS_2 = 4D ;
	private final static Double CHK_LOC_TXN_STATUS_103 = 10D ;
	private final static Double CHK_LOC_TXN_STATUS_6 = 86D ;

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws Exception  
	{
		try 
		{
			TxnStatusDistributionHandler txnDistribute = new TxnStatusDistributionHandler();
			txnDistribute.processPastDatedTxnStatus();
			txnDistribute.processPresentFutureDatedTxnStatus();
		} 
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Process past dated transactions...
	 * @param txn_type
	 * @throws Exception
	 */
	public void processPastDatedTxnStatus() throws Exception 
	{
		try 
		{
			//Get the connection...
			DBUtils dbutils = new DBUtils();
			conn = dbutils.getConnection();

			List<String> txn_types = Arrays.asList("INT","ACH-IN","ACH-OUT","CHK-REG","CHK-LOC");
			for(String txn_type : txn_types)
			{
				List txnCountList = getPastTxnDistributionCount(txn_type);
				if(!txnCountList.isEmpty()) 
				{
					System.out.println("Processing Past dated transaction status for type .... :"+txn_type);
					System.out.println("Transaction Distribution for the type : "+txn_type+ " is " +txnCountList );
					updatePastDatedTxns(txnCountList,txn_type);
					System.out.println("Processed  Past dated transaction status for type :"+txn_type);
				}
				else {
					System.out.println("No transaction found for the type :"+txn_type);
				}
				conn.commit();
			}
		} 
		catch (Exception e) {
			e.printStackTrace();
			throw e ;
		}
		finally {
			try {
				if(conn != null) 
					conn.close();
			} catch (SQLException sqle) {
				sqle.printStackTrace();
			}
		}
	}

	/**
	 * Process Future dated transactions...
	 * @param txn_type
	 * @throws Exception
	 */
	public void processPresentFutureDatedTxnStatus() throws Exception 
	{
		try 
		{
			//Get the connection...
			DBUtils dbutils = new DBUtils();
			conn = dbutils.getConnection();

			List<String> txn_types = Arrays.asList("INT","ACH-IN","ACH-OUT","CHK-REG","CHK-LOC");
			for(String txn_type : txn_types) 
			{
				List txnCountList = getPresentFutureTxnDistributionCount(txn_type);
				if(!txnCountList.isEmpty()) 
				{
					System.out.println("Processing Present/Future dated transaction status for type .... :"+txn_type);
					System.out.println("Transaction Distribution for the type : "+txn_type+ " is " +txnCountList );
					updatePresentFutureDatedTxns(txnCountList,txn_type);
					System.out.println("Processed  Present/Future dated transaction status for type :"+txn_type);
				}
				else {
					System.out.println("No transaction found for the type :"+txn_type);
				}
				conn.commit();
			}
		} 
		catch (Exception e) {
			e.printStackTrace();
			throw e ;
		}
		finally {
			try {
				if(conn != null) 
					conn.close();
			} catch (SQLException sqle) {
				sqle.printStackTrace();
			}
		}
	}

	/**
	 * Get process past transaction distribution count...
	 * @param txn_type
	 * @return
	 * @throws Exception
	 */
	public ArrayList getPastTxnDistributionCount(String txn_type) throws Exception 
	{
		ArrayList txn_distribution = new ArrayList();

		//Total Transactions....
		Double total_txns_count = getTotal_PastTxn_Count(txn_type);

		if(total_txns_count > 0) 
		{
			Integer approx_executed_txns_count = 0 ;
			if(txn_type!=null && !txn_type.equalsIgnoreCase("CHK-LOC"))
			{
				//Executed Txns...
				ArrayList executed_txns = new ArrayList();
				approx_executed_txns_count = calculateTxnUpdateCount(txn_type,4,total_txns_count,0).intValue();
				executed_txns.add("4");
				executed_txns.add(approx_executed_txns_count);
				if(approx_executed_txns_count > 0 ) {
					txn_distribution.add(executed_txns);
				}
			}

			//Cancelled Txns...
			ArrayList cancelled_txns = new ArrayList();
			Integer approx_cancelled_txns_count = calculateTxnUpdateCount(txn_type,20,total_txns_count,0).intValue();
			cancelled_txns.add("20");
			cancelled_txns.add(approx_cancelled_txns_count);
			if(approx_cancelled_txns_count > 0 ) {
				txn_distribution.add(cancelled_txns);
			}

			//Not Approved Txns...
			ArrayList not_approved_txns = new ArrayList();
			Integer approx_not_approved_txns_count = calculateTxnUpdateCount(txn_type,44,total_txns_count,0).intValue();
			not_approved_txns.add("44");
			not_approved_txns.add(approx_not_approved_txns_count);
			if(approx_not_approved_txns_count > 0 ) {
				txn_distribution.add(not_approved_txns);
			}

			Integer approx_suspended_txns_count = 0 ;
			if(txn_type!=null && txn_type.startsWith("ACH"))
			{
				//Suspended Txns...
				ArrayList suspended_txns = new ArrayList();
				approx_suspended_txns_count = calculateTxnUpdateCount(txn_type,46,total_txns_count,0).intValue();
				suspended_txns.add("46");
				suspended_txns.add(approx_suspended_txns_count);
				if(approx_suspended_txns_count > 0 ) {
					txn_distribution.add(suspended_txns);
				}
			}

			//System Rejected Txns...
			ArrayList system_rejected_txns = new ArrayList();
			Integer approx_system_rejected_txns_count = calculateTxnUpdateCount(txn_type,52,total_txns_count,0).intValue();
			system_rejected_txns.add("52");
			system_rejected_txns.add(approx_system_rejected_txns_count);
			if(approx_system_rejected_txns_count > 0 ) {
				txn_distribution.add(system_rejected_txns);
			}

			//Expired Txns...
			ArrayList expired_txns = new ArrayList();
			Integer approx_expired_txns_count = calculateTxnUpdateCount(txn_type,50,total_txns_count,0).intValue();
			expired_txns.add("50");
			expired_txns.add(approx_expired_txns_count);
			if(approx_expired_txns_count > 0 ) {
				txn_distribution.add(expired_txns);
			}

			Integer approx_printed_count = 0 ;
			if(txn_type!=null && txn_type.equalsIgnoreCase("CHK-LOC"))
			{
				//Printed Txns...
				ArrayList printed_txns = new ArrayList();
				approx_printed_count = calculateTxnUpdateCount(txn_type,101,total_txns_count,0).intValue();
				printed_txns.add("101");
				printed_txns.add(approx_printed_count);
				if(approx_printed_count > 0 ) {
					txn_distribution.add(printed_txns);
				}
			}

			Integer total_txn_processed_count =  approx_executed_txns_count
			+ approx_cancelled_txns_count + approx_not_approved_txns_count +  approx_suspended_txns_count + 
			approx_system_rejected_txns_count + approx_expired_txns_count  + approx_printed_count ;

			if(total_txn_processed_count < total_txns_count.intValue())
			{
				Integer remaining_txn_count = total_txns_count.intValue() - total_txn_processed_count;
				if(txn_type!=null && !txn_type.equalsIgnoreCase("CHK-LOC"))
				{
					//Executed Txns...
					ArrayList remaningTxns = new ArrayList();
					remaningTxns.add("4");
					remaningTxns.add(remaining_txn_count);
					if(remaining_txn_count > 0 ) {
						txn_distribution.add(remaningTxns);
					}
				}
				else
				{
					//Printed Txns...
					ArrayList remaningTxns = new ArrayList();
					remaningTxns.add("101");
					remaningTxns.add(remaining_txn_count);
					if(remaining_txn_count > 0 ) {
						txn_distribution.add(remaningTxns);
					}
				}

			}
		}
		return txn_distribution ;
	}

	/**
	 * Get process Present/Future transaction distribution count...
	 * @param txn_type
	 * @return
	 * @throws Exception
	 */
	public ArrayList getPresentFutureTxnDistributionCount(String txn_type) throws Exception 
	{
		ArrayList txn_distribution = new ArrayList();

		//Total Transactions....
		Double total_txns_count = getTotal_PresentFutureTxn_Count(txn_type);

		if(total_txns_count > 0) 
		{
			//Awaiting-Approval Txns...
			ArrayList awaiting_approval_txns = new ArrayList();
			Integer approx_awaiting_approval_txns_count = calculateTxnUpdateCount(txn_type,2,total_txns_count,1).intValue();
			awaiting_approval_txns.add("2");
			awaiting_approval_txns.add(approx_awaiting_approval_txns_count);
			if(approx_awaiting_approval_txns_count > 0 ) {
				txn_distribution.add(awaiting_approval_txns);
			}

			Integer approx_executed_txns_count = 0 ;
			if(txn_type!=null && !txn_type.equalsIgnoreCase("CHK-LOC"))
			{
				//Executed Txns...
				ArrayList executed_txns = new ArrayList();
				approx_executed_txns_count = calculateTxnUpdateCount(txn_type,4,total_txns_count,1).intValue();
				executed_txns.add("4");
				executed_txns.add(approx_executed_txns_count);
				if(approx_executed_txns_count > 0 ) {
					txn_distribution.add(executed_txns);
				}
			}

			Integer approx_awaiting_print_count = 0;
			Integer approx_printed_count = 0 ;
			if(txn_type!=null && txn_type.equalsIgnoreCase("CHK-LOC"))
			{
				//Awaiting Print Txns...
				ArrayList awaiting_print_txns = new ArrayList();
				approx_awaiting_print_count = calculateTxnUpdateCount(txn_type,103,total_txns_count,1).intValue();
				awaiting_print_txns.add("103");
				awaiting_print_txns.add(approx_awaiting_print_count);
				if(approx_awaiting_print_count > 0 ) {
					txn_distribution.add(awaiting_print_txns);
				}

			}

			//Scheduled Txns...
			ArrayList scheduled_txns = new ArrayList();
			Integer approx_scheduled_txns_count = calculateTxnUpdateCount(txn_type,6,total_txns_count,1).intValue();
			scheduled_txns.add("6");
			scheduled_txns.add(approx_scheduled_txns_count);
			if(approx_scheduled_txns_count > 0 ) {
				txn_distribution.add(scheduled_txns);
			}

			//Remaining txns..
			Integer total_txn_processed_count = approx_awaiting_approval_txns_count + approx_executed_txns_count
			+ approx_awaiting_print_count + approx_printed_count ;
			if(total_txn_processed_count < total_txns_count.intValue())
			{
				Integer remaining_txn_count = total_txns_count.intValue() - total_txn_processed_count;
				ArrayList remaningTxns = new ArrayList();
				remaningTxns.add("6");
				remaningTxns.add(remaining_txn_count);
				if(remaining_txn_count > 0 ) {
					txn_distribution.add(remaningTxns);
				}
			}
		}
		return txn_distribution ;
	}


	/**
	 * Calculate transaction update count...
	 * @param txn_type
	 * @param txn_status
	 * @param total_txn_count
	 * @return
	 */
	public Double calculateTxnUpdateCount(String txn_type,int txn_status,Double total_txn_count,int dateInd)
	{
		Double txn_Update_count = new Double(0D);
		if(txn_type!=null && txn_type.equalsIgnoreCase("INT")){
			if(txn_status==2){
				txn_Update_count = (total_txn_count*(INT_TXN_STATUS_2/100));
			}
			else if(txn_status==4) {
				if(dateInd == 0) {
					txn_Update_count = (total_txn_count*(INT_TXN_STATUS_4_PAST/100));
				}
				else {
					txn_Update_count = (total_txn_count*(INT_TXN_STATUS_4/100));
				}
			}
			else if(txn_status==20){
				txn_Update_count = (total_txn_count*(INT_TXN_STATUS_20/100));
			}
			else if(txn_status==44){
				txn_Update_count = (total_txn_count*(INT_TXN_STATUS_44/100));
			}
			else if(txn_status==52){
				txn_Update_count = (total_txn_count*(INT_TXN_STATUS_52/100));
			}
			else if(txn_status==50){
				txn_Update_count = (total_txn_count*(INT_TXN_STATUS_50/100));
			}
			else if(txn_status==6){
				txn_Update_count = (total_txn_count*(INT_TXN_STATUS_6/100));
			}
		}
		else if(txn_type!=null && txn_type.equalsIgnoreCase("ACH-OUT"))
		{
			if(txn_status==2){
				txn_Update_count = (total_txn_count*(ACH_OUT_TXN_STATUS_2/100));
			}
			else if(txn_status==4){
				if(dateInd == 0) {
					txn_Update_count = (total_txn_count*(ACH_OUT_TXN_STATUS_4_PAST/100));
				}
				else {
					txn_Update_count = (total_txn_count*(ACH_OUT_TXN_STATUS_4/100));
				}
			}
			else if(txn_status==20){
				txn_Update_count = (total_txn_count*(ACH_OUT_TXN_STATUS_20/100));
			}
			else if(txn_status==44){
				txn_Update_count = (total_txn_count*(ACH_OUT_TXN_STATUS_44/100));
			}
			else if(txn_status==52){
				txn_Update_count = (total_txn_count*(ACH_OUT_TXN_STATUS_52/100));
			}
			else if(txn_status==50){
				txn_Update_count = (total_txn_count*(ACH_OUT_TXN_STATUS_50/100));
			}
			else if(txn_status==46){
				txn_Update_count = (total_txn_count*(ACH_OUT_TXN_STATUS_46/100));
			}
			else if(txn_status==6){
				txn_Update_count = (total_txn_count*(ACH_OUT_TXN_STATUS_6/100));
			}
		}
		else if(txn_type!=null && txn_type.equalsIgnoreCase("ACH-IN"))
		{
			if(txn_status==2){
				txn_Update_count = (total_txn_count*(ACH_IN_TXN_STATUS_2/100));
			}
			else if(txn_status==4){
				if(dateInd == 0) {
					txn_Update_count = (total_txn_count*(ACH_IN_TXN_STATUS_4_PAST/100));
				}
				else {
					txn_Update_count = (total_txn_count*(ACH_IN_TXN_STATUS_4/100));
				}
			}
			else if(txn_status==20){
				txn_Update_count = (total_txn_count*(ACH_IN_TXN_STATUS_20/100));
			}
			else if(txn_status==44){
				txn_Update_count = (total_txn_count*(ACH_IN_TXN_STATUS_44/100));
			}
			else if(txn_status==52){
				txn_Update_count = (total_txn_count*(ACH_IN_TXN_STATUS_52/100));
			}
			else if(txn_status==50){
				txn_Update_count = (total_txn_count*(ACH_IN_TXN_STATUS_50/100));
			}
			else if(txn_status==46){
				txn_Update_count = (total_txn_count*(ACH_IN_TXN_STATUS_46/100));
			}
			else if(txn_status==6){
				txn_Update_count = (total_txn_count*(ACH_IN_TXN_STATUS_6/100));
			}
		}
		else if(txn_type!=null && txn_type.equalsIgnoreCase("CHK-REG"))
		{
			if(txn_status==2){
				txn_Update_count = (total_txn_count*(CHK_REG_TXN_STATUS_2/100));
			}
			else if(txn_status==4){
				if(dateInd == 0) {
					txn_Update_count = (total_txn_count*(CHK_REG_TXN_STATUS_4_PAST/100));
				}
				else {
					txn_Update_count = (total_txn_count*(CHK_REG_TXN_STATUS_4/100));
				}
			}
			else if(txn_status==20){
				txn_Update_count = (total_txn_count*(CHK_REG_TXN_STATUS_20/100));
			}
			else if(txn_status==44){
				txn_Update_count = (total_txn_count*(CHK_REG_TXN_STATUS_44/100));
			}
			else if(txn_status==52){
				txn_Update_count = (total_txn_count*(CHK_REG_TXN_STATUS_52/100));
			}
			else if(txn_status==50){
				txn_Update_count = (total_txn_count*(CHK_REG_TXN_STATUS_50/100));
			}
			else if(txn_status==6){
				txn_Update_count = (total_txn_count*(CHK_REG_TXN_STATUS_6/100));
			}
		}
		else if(txn_type!=null && txn_type.equalsIgnoreCase("CHK-LOC"))
		{
			if(txn_status==2){
				txn_Update_count = (total_txn_count*(CHK_LOC_TXN_STATUS_2/100));
			}
			else if(txn_status==20){
				txn_Update_count = (total_txn_count*(CHK_LOC_TXN_STATUS_20/100));
			}
			else if(txn_status==44){
				txn_Update_count = (total_txn_count*(CHK_LOC_TXN_STATUS_44/100));
			}
			else if(txn_status==52){
				txn_Update_count = (total_txn_count*(CHK_LOC_TXN_STATUS_52/100));
			}
			else if(txn_status==50){
				txn_Update_count = (total_txn_count*(CHK_LOC_TXN_STATUS_50/100));
			}
			else if(txn_status==101){
				txn_Update_count = (total_txn_count*(CHK_LOC_TXN_STATUS_101/100));
			}
			else if(txn_status==103){
				txn_Update_count = (total_txn_count*(CHK_LOC_TXN_STATUS_103/100));
			}
			else if(txn_status==6){
				txn_Update_count = (total_txn_count*(CHK_LOC_TXN_STATUS_6/100));
			}
		}
		return txn_Update_count;
	}
}
