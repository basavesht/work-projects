package com.tcs.bancs.objects.schema.request.payments.transfers;

public enum AcntType {
	Internal,
	External ;

	public String value() {
		return name();
	}

	public static AcntType fromValue(String v) {
		return valueOf(v);
	}
}
