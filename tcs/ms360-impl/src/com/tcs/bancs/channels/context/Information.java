package com.tcs.bancs.channels.context;

/**
 * @author : 224703
 * **************   Revision History ************************
 * Modified-By |  Modified-Date |  Project-Phase  | Changes
 *
 * **********************************************************
 */
public class Information extends Message {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6626952875874935242L;

	@Override
	public MessageType getType() {
		return MessageType.INFORMATION;
	}

}
