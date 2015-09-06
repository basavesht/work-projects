/**
 * 
 */
package com.tcs.bancs.services.provider.payments.transfers.interfaces;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.ParameterStyle;

import com.tcs.bancs.helpers.ws.TransferRequestException;
import com.tcs.bancs.objects.schema.request.payments.transfers.TransferRequest;
import com.tcs.bancs.objects.schema.response.payments.transfers.TransferResponse;

@WebService(
		name = "TransferRequest", 
		targetNamespace = "http://tcs.com/bancs/morganstanley/payments/transfers")
@SOAPBinding(
		style = SOAPBinding.Style.DOCUMENT, 
		use = SOAPBinding.Use.LITERAL,
		parameterStyle = ParameterStyle.WRAPPED)
public interface ITransferRequest  
{
	@WebMethod (
			operationName = "loadTransfer",
			action = "http://tcs.com/bancs/morganstanley/payments/transfers/loadTransfer",
			exclude = false)
	@WebResult(
			name = "LoadTransferResponseParam",
			targetNamespace = "http://tcs.com/bancs/morganstanley/payments/transfers/loadTransfer/response")
	public TransferResponse loadTransfer(@WebParam(
			name = "loadTransferRequestParam", 
			partName = "loadTransferRequestParam",
			targetNamespace = "http://tcs.com/bancs/morganstanley/payments/transfers/loadTransfer/request") TransferRequest request) throws TransferRequestException ;

	@WebMethod (
			operationName = "selectAccount",
			action = "http://tcs.com/bancs/morganstanley/payments/transfers/selectAccount",
			exclude = false)
	@WebResult(
			name = "SelectAccountResponseParam",
			targetNamespace = "http://tcs.com/bancs/morganstanley/payments/transfers/selectAccount/response")
	public TransferResponse selectAccount(@WebParam(
			name = "SelectAccountRequestParam", 
			partName = "SelectAccountRequestParam",
			targetNamespace = "http://tcs.com/bancs/morganstanley/payments/transfers/selectAccount/request") TransferRequest request) throws TransferRequestException ;

	@WebMethod (
			operationName = "createTransfer",
			action = "http://tcs.com/bancs/morganstanley/payments/transfers/createTransfer",
			exclude = false)
	@WebResult(
			name = "CreateTransferResponseParam",
			targetNamespace = "http://tcs.com/bancs/morganstanley/payments/transfers/createTransfer/response")
	public TransferResponse createTransfer(@WebParam(
			name = "CreateTransferRequestParam", 
			partName = "CreateTransferRequestParam",
			targetNamespace = "http://tcs.com/bancs/morganstanley/payments/transfers/createTransfer/request") TransferRequest request) throws TransferRequestException ;
}
