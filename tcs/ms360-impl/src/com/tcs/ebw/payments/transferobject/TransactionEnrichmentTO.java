/**
 * 
 */
package com.tcs.ebw.payments.transferobject;

import com.tcs.ebw.transferobject.EBWTransferObject;

/**
 * @author : 224703
 * **************   Revision History ************************
 * Modified-By |  Modified-Date |  Project-Phase  | Changes
 *
 * **********************************************************
 */
public class TransactionEnrichmentTO extends EBWTransferObject implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5327819338825033324L;
	private String keyAcct;
	private String txn_Pos_Num;
	private int acct_Type;
	private int acct_Tier;
	private Double acct_DrCrInd;
	private Double enrichment_Ind;
	private String office_id;
	private String fa_id;
	private String account_num;

	public String getKeyAcct() {
		return keyAcct;
	}
	public void setKeyAcct(String keyAcct) {
		this.keyAcct = keyAcct;
	}
	public int getAcct_Type() {
		return acct_Type;
	}
	public void setAcct_Type(int acct_Type) {
		this.acct_Type = acct_Type;
	}
	public int getAcct_Tier() {
		return acct_Tier;
	}
	public void setAcct_Tier(int acct_Tier) {
		this.acct_Tier = acct_Tier;
	}
	public String getTxn_Pos_Num() {
		return txn_Pos_Num;
	}
	public void setTxn_Pos_Num(String txn_Pos_Num) {
		this.txn_Pos_Num = txn_Pos_Num;
	}
	public Double getAcct_DrCrInd() {
		return acct_DrCrInd;
	}
	public void setAcct_DrCrInd(Double acct_DrCrInd) {
		this.acct_DrCrInd = acct_DrCrInd;
	}
	public Double getEnrichment_Ind() {
		return enrichment_Ind;
	}
	public void setEnrichment_Ind(Double enrichment_Ind) {
		this.enrichment_Ind = enrichment_Ind;
	}
	public String getAccount_num() {
		return account_num;
	}
	public void setAccount_num(String account_num) {
		this.account_num = account_num;
	}
	public String getOffice_id() {
		return office_id;
	}
	public void setOffice_id(String office_id) {
		this.office_id = office_id;
	}
	public String getFa_id() {
		return fa_id;
	}
	public void setFa_id(String fa_id) {
		this.fa_id = fa_id;
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

		retValue = "TransactionEnrichmentTO ( "
			+ super.toString() + TAB
			+ "keyAcct = " + this.keyAcct + TAB
			+ "txn_Pos_Num = " + this.txn_Pos_Num + TAB
			+ "acct_Type = " + this.acct_Type + TAB
			+ "acct_Tier = " + this.acct_Tier + TAB
			+ "acct_DrCrInd = " + this.acct_DrCrInd + TAB
			+ "enrichment_Ind = " + this.enrichment_Ind + TAB
			+ "office_id = " + this.office_id + TAB
			+ "fa_id = " + this.fa_id + TAB
			+ "account_num = " + this.account_num + TAB
			+ " )";

		return retValue;
	}

}
