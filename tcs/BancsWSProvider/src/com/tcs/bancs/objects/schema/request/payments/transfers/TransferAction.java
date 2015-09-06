package com.tcs.bancs.objects.schema.request.payments.transfers;

public enum TransferAction {
	GetBalance,
	CreatePageLoad,
	CreatePreConfirm,
	CreateConfirm ;
	
	public String value() {
		return name();
	}

	public static TransferAction fromValue(String v) {
		return valueOf(v);
	}
}
