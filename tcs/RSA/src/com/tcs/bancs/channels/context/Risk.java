package com.tcs.bancs.channels.context;

/**
 * @author : 224703
 * **************   Revision History ************************
 * Modified-By |  Modified-Date |  Project-Phase  | Changes
 * 
 * **********************************************************
 */
public class Risk extends Message {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8300759199038447387L;

	@Override
	public MessageType getType() {
		return MessageType.RISK;
	}
}
