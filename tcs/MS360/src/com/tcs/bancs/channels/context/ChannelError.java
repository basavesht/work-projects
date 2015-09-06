package com.tcs.bancs.channels.context;

/**
 * @author : 224703
 * **************   Revision History ************************
 * Modified-By |  Modified-Date |  Project-Phase  | Changes
 *
 * **********************************************************
 */
public class ChannelError extends Message {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6397730247869508989L;

	@Override
	public MessageType getType() {
		return MessageType.ERROR;
	}

}
