package com.bosch.tmp.integration.util;

public enum ORUBatchResponseMessageType 
{
	ACK("ACK"),
	ORU_R30("ORU_R30"),
	ORU_R01("ORU_R01");

	private String batchMessageType;

	ORUBatchResponseMessageType(final String batchMessageType){
		this.batchMessageType = batchMessageType;
	}

	public String getBatchMessageType(){
		return batchMessageType;
	}

	public String toString(){
		return batchMessageType.toString();
	}
}
