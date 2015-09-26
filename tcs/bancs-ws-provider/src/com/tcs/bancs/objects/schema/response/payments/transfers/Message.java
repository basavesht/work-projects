/**
 * 
 */
package com.tcs.bancs.objects.schema.response.payments.transfers;

import java.io.Serializable;

/**
 * @author 259245
 *
 */
public class Message implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2917856163477531038L;
	private MessageType messageType = null;
	private Integer messageCode = null;
	private String messageDescription = null;

	/**
	 * @return the messageType
	 */
	public MessageType getMessageType() {
		return messageType;
	}
	/**
	 * @param messageType the messageType to set
	 */
	public void setMessageType(MessageType messageType) {
		this.messageType = messageType;
	}
	/**
	 * @return the messageCode
	 */
	public Integer getMessageCode() {
		return messageCode;
	}
	/**
	 * @param messageCode the messageCode to set
	 */
	public void setMessageCode(Integer messageCode) {
		this.messageCode = messageCode;
	}
	/**
	 * @return the messageDescription
	 */
	public String getMessageDescription() {
		return messageDescription;
	}
	/**
	 * @param messageDescription the messageDescription to set
	 */
	public void setMessageDescription(String messageDescription) {
		this.messageDescription = messageDescription;
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

		retValue = "MessageTO ( "
				+ super.toString() + TAB
				+ "messageType = " + this.messageType + TAB
				+ "messageCode = " + this.messageCode + TAB
				+ "messageDescription = " + this.messageDescription + TAB
				+ " )";

		return retValue;
	}

}
