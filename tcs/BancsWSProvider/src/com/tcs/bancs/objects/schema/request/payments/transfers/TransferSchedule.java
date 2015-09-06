package com.tcs.bancs.objects.schema.request.payments.transfers;

public enum TransferSchedule {
	OneTime,
	Recurring ; 

	public String value() {
		return name();
	}

	public static TransferSchedule fromValue(String v) {
		return valueOf(v);
	}
}
