package com.tcs.bancs.objects.schema.response.payments.transfers;

public enum MessageType {
	SUCCESS,
	INFORMATION,
	WARNING,
	RISK,
	ERROR,
	SEVERE,
	CRITICAL ;

	public String value() {
		return name();
	}

	public static MessageType fromValue(String v) {
		return valueOf(v);
	}
}
