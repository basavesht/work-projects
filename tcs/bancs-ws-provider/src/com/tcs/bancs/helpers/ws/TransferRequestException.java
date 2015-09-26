package com.tcs.bancs.helpers.ws;

import javax.xml.ws.WebFault;

@WebFault(name="TransferRequestFault",
		  targetNamespace="http://tcs.com/bancs/morganstanley/payments/transfers/fault")
public class TransferRequestException extends Exception 
{
	private static final long serialVersionUID = -6719343235218802062L;
	private TransferRequestFaultBean faultInfo;

	public TransferRequestException(String message, TransferRequestFaultBean faultInfo) {
		super(message);
		this.faultInfo = faultInfo;
	}

	public TransferRequestException(String message,TransferRequestFaultBean faultInfo,Throwable cause) {
		super(message,cause);
		this.faultInfo = faultInfo;
	}

	public TransferRequestException(TransferRequestFaultBean faultInfo,Throwable cause) {
		super(cause);
		this.faultInfo = faultInfo;
	}

	public TransferRequestFaultBean getFaultInfo() {
		return faultInfo;
	}
}
