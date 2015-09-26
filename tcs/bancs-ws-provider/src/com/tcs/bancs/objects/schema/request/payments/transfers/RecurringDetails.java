/**
 * 
 */
package com.tcs.bancs.objects.schema.request.payments.transfers;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author 259245
 *
 */
public class RecurringDetails 
{
	private Integer frequency= null;
	private Integer repeat = null;
	private Date endDate = null;
	private BigDecimal untilAmount = null;
	private String noOfTransfers = null;

	/**
	 * @return the frequency
	 */
	public Integer getFrequency() {
		return frequency;
	}
	/**
	 * @param frequency the frequency to set
	 */
	public void setFrequency(Integer frequency) {
		this.frequency = frequency;
	}
	/**
	 * @return the repeat
	 */
	public Integer getRepeat() {
		return repeat;
	}
	/**
	 * @param repeat the repeat to set
	 */
	public void setRepeat(Integer repeat) {
		this.repeat = repeat;
	}
	/**
	 * @return the endDate
	 */
	public Date getEndDate() {
		return endDate;
	}
	/**
	 * @param endDate the endDate to set
	 */
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	/**
	 * @return the untilAmount
	 */
	public BigDecimal getUntilAmount() {
		return untilAmount;
	}
	/**
	 * @param untilAmount the untilAmount to set
	 */
	public void setUntilAmount(BigDecimal untilAmount) {
		this.untilAmount = untilAmount;
	}
	/**
	 * @return the noOfTransfers
	 */
	public String getNoOfTransfers() {
		return noOfTransfers;
	}
	/**
	 * @param noOfTransfers the noOfTransfers to set
	 */
	public void setNoOfTransfers(String noOfTransfers) {
		this.noOfTransfers = noOfTransfers;
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
		final String TAB = "    ";

		String retValue = "";

		retValue = "RecurringDetails ( "
				+ super.toString() + TAB
				+ "frequency = " + this.frequency + TAB
				+ "repeat = " + this.repeat + TAB
				+ "endDate = " + this.endDate + TAB
				+ "untilAmount = " + this.untilAmount + TAB
				+ "noOfTransfers = " + this.noOfTransfers + TAB
				+ " )";

		return retValue;
	}

}
