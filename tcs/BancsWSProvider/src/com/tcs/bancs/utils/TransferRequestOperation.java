package com.tcs.bancs.utils;

public enum TransferRequestOperation
{
	LOAD_TRANSFER("loadTransfer"),
	SELECT_ACCOUNT("selectAccount"),
	SUBMIT_TRANSFER("submitTransfer"),
	CREATE_TRANSFER("createTransfer");

	String name = null;
	TransferRequestOperation(String name) {
		this.name = name;
	}

	public String getOperation(){
		return name;
	}
}
