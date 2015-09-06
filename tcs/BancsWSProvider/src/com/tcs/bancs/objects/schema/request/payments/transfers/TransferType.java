package com.tcs.bancs.objects.schema.request.payments.transfers;

public enum TransferType {
	Internal,
	ACH ;
	
    public String value() {
        return name();
    }

    public static TransferType fromValue(String v) {
        return valueOf(v);
    }
}
