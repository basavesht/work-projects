package com.tcs.bancs.channels.context;

/**
 * @author : 224703
 * **************   Revision History ************************
 * Modified-By |  Modified-Date |  Project-Phase  | Changes
 *
 * **********************************************************
 */
public class Severe extends Message {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4524358547992864097L;

	@Override
	public MessageType getType() {
		return MessageType.SEVERE;
	}

}
