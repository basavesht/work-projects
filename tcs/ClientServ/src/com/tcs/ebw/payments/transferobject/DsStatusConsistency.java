/**
 * 
 */
package com.tcs.ebw.payments.transferobject;

import java.sql.Timestamp;

import com.tcs.ebw.transferobject.EBWTransferObject;

/**
 * @author : 224703
 * **************   Revision History ************************
 * Modified-By |  Modified-Date |  Project-Phase  | Changes
 *
 * **********************************************************
 */
public class DsStatusConsistency extends EBWTransferObject implements java.io.Serializable 
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 3378242229414905906L;
	private Double eventkey = null;
	private String application_id = null;
	private String event_name = null;
	private String input_status = null;
	private String paypayref = null;
	private String paybatchref = null;
	private String cpypayeeid = null;
	private String createdby = null;
	private Timestamp createddate = null;

	public Double getEventkey() {
		return eventkey;
	}
	public void setEventkey(Double eventkey) {
		this.eventkey = eventkey;
	}
	public String getApplication_id() {
		return application_id;
	}
	public void setApplication_id(String application_id) {
		this.application_id = application_id;
	}
	public String getEvent_name() {
		return event_name;
	}
	public void setEvent_name(String event_name) {
		this.event_name = event_name;
	}
	public String getCreatedby() {
		return createdby;
	}
	public void setCreatedby(String createdby) {
		this.createdby = createdby;
	}
	public Timestamp getCreateddate() {
		return createddate;
	}
	public void setCreateddate(Timestamp createddate) {
		this.createddate = createddate;
	}
	public String getInput_status() {
		return input_status;
	}
	public void setInput_status(String input_status) {
		this.input_status = input_status;
	}
	public String getPaypayref() {
		return paypayref;
	}
	public void setPaypayref(String paypayref) {
		this.paypayref = paypayref;
	}
	public String getPaybatchref() {
		return paybatchref;
	}
	public void setPaybatchref(String paybatchref) {
		this.paybatchref = paybatchref;
	}
	public String getCpypayeeid() {
		return cpypayeeid;
	}
	public void setCpypayeeid(String cpypayeeid) {
		this.cpypayeeid = cpypayeeid;
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

		retValue = "DsStatusConsistency ( "
			+ super.toString() + TAB
			+ "eventkey = " + this.eventkey + TAB
			+ "application_id = " + this.application_id + TAB
			+ "event_name = " + this.event_name + TAB
			+ "input_status = " + this.input_status + TAB
			+ "paypayref = " + this.paypayref + TAB
			+ "paybatchref = " + this.paybatchref + TAB
			+ "createdby = " + this.createdby + TAB
			+ "createddate = " + this.createddate + TAB
			+ " )";

		return retValue;
	}
}
