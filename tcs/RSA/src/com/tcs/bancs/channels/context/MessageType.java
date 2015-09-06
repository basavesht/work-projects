package com.tcs.bancs.channels.context;

import java.io.Serializable;

/**
 * @author : 224703
 * **************   Revision History ************************
 * Modified-By |  Modified-Date |  Project-Phase  | Changes
 * 
 * **********************************************************
 */
public enum MessageType implements Serializable{
	SUCCESS, INFORMATION, WARNING ,RISK ,ERROR ,SEVERE ,CRITICAL
}
