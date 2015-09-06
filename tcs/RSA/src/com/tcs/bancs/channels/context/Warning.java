package com.tcs.bancs.channels.context;

/**
 * @author : 224703
 * **************   Revision History ************************
 * Modified-By |  Modified-Date |  Project-Phase  | Changes
 * 
 * **********************************************************
 */
public class Warning extends Message {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8187409127076194872L;

	@Override
	public MessageType getType() {
		return MessageType.WARNING;
	}

}
