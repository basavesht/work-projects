package com.tcs.bancs.channels.context;

/**
 * @author : 224703
 * **************   Revision History ************************
 * Modified-By |  Modified-Date |  Project-Phase  | Changes
 * 
 * **********************************************************
 */
public class Critical extends Message {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7837228464059296910L;

	@Override
	public MessageType getType() {
		return MessageType.CRITICAL;
	}

}
