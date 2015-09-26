package com.tcs.bancs.client;

import com.tcs.bancs.services.provider.payments.transfers.implementations.TransferRequest;
import com.tcs.bancs.services.provider.payments.transfers.implementations.TransferRequestException;
import com.tcs.bancs.services.provider.payments.transfers.implementations.TransferRequestService;

public class TransferRequestServiceClient {

	public static void main (String[] args) throws TransferRequestException{
		TransferRequestService client = new TransferRequestService();
		TransferRequest request = client.getTransferRequestPort();
		
		com.tcs.bancs.morganstanley.payments.transfers.TransferRequest re = new com.tcs.bancs.morganstanley.payments.transfers.TransferRequest();
		com.tcs.bancs.morganstanley.payments.transfers.TransferResponse rs = request.loadTransfer(re);
		
		System.out.println("completed"+rs);
	}
}
