package com.tcs.bancs.objects.schema.request.payments.transfers;

public enum DrCrIndicator {
	DR ,
	CR ;

	public String value() {
		return name();
	}

	public static DrCrIndicator fromValue(String v) {
		return valueOf(v);
	}
}
