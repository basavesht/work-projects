/**
 * 
 */
package com.tcs.bancs.objects.schema.response.payments.transfers;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * @author 259245
 *
 */
public class ServiceContext implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3247623937706339647L;
	private ArrayList<Message> message = new ArrayList<Message>();

	/**
	 * @return the message
	 */
	public ArrayList<Message> getMessage() {
		return message;
	}
	/**
	 * @param message the message to set
	 */
	public void setMessage(ArrayList<Message> message) {
		this.message = message;
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

		retValue = "ServiceContext ( "
				+ super.toString() + TAB
				+ "message = " + this.message + TAB
				+ " )";

		return retValue;
	}

}
