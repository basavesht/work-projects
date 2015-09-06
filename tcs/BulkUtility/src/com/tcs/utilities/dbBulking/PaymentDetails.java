/**
 * 
 */
package com.tcs.utilities.dbBulking;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author 224703
 *
 */
public class PaymentDetails implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private HashMap ms_Acnt_1 = null;
	private HashMap ms_Acnt_2 = null;
	private ArrayList ach_Account = null;
	private String recurring = null;
	private Timestamp txn_date = null;
	private String txn_initiated_id = null;
	private String txn_type = null;
	private BigDecimal txn_amount = null;

	/**
	 * @return the ms_Acnt_1
	 */
	public HashMap getMs_Acnt_1() {
		return ms_Acnt_1;
	}
	/**
	 * @param ms_Acnt_1 the ms_Acnt_1 to set
	 */
	public void setMs_Acnt_1(HashMap ms_Acnt_1) {
		this.ms_Acnt_1 = ms_Acnt_1;
	}
	/**
	 * @return the ms_Acnt_2
	 */
	public HashMap getMs_Acnt_2() {
		return ms_Acnt_2;
	}
	/**
	 * @param ms_Acnt_2 the ms_Acnt_2 to set
	 */
	public void setMs_Acnt_2(HashMap ms_Acnt_2) {
		this.ms_Acnt_2 = ms_Acnt_2;
	}
	/**
	 * @return the ach_Account
	 */
	public ArrayList getAch_Account() {
		return ach_Account;
	}
	/**
	 * @param ach_Account the ach_Account to set
	 */
	public void setAch_Account(ArrayList ach_Account) {
		this.ach_Account = ach_Account;
	}
	/**
	 * @return the recurring
	 */
	public String getRecurring() {
		return recurring;
	}
	/**
	 * @param recurring the recurring to set
	 */
	public void setRecurring(String recurring) {
		this.recurring = recurring;
	}
	/**
	 * @return the txn_date
	 */
	public Timestamp getTxn_date() {
		return txn_date;
	}
	/**
	 * @param txn_date the txn_date to set
	 */
	public void setTxn_date(Timestamp txn_date) {
		this.txn_date = txn_date;
	}
	/**
	 * @return the txn_initiated_id
	 */
	public String getTxn_initiated_id() {
		return txn_initiated_id;
	}
	/**
	 * @param txn_initiated_id the txn_initiated_id to set
	 */
	public void setTxn_initiated_id(String txn_initiated_id) {
		this.txn_initiated_id = txn_initiated_id;
	}
	/**
	 * @return the txn_type
	 */
	public String getTxn_type() {
		return txn_type;
	}
	/**
	 * @param txn_type the txn_type to set
	 */
	public void setTxn_type(String txn_type) {
		this.txn_type = txn_type;
	}
	/**
	 * @return the txn_amount
	 */
	public BigDecimal getTxn_amount() {
		return txn_amount;
	}
	/**
	 * @param txn_amount the txn_amount to set
	 */
	public void setTxn_amount(BigDecimal txn_amount) {
		this.txn_amount = txn_amount;
	}
}
