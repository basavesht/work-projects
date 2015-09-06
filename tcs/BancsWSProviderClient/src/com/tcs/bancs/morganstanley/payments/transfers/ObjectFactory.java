
package com.tcs.bancs.morganstanley.payments.transfers;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the com.tcs.bancs.morganstanley.payments.transfers package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _LoadTransfer_QNAME = new QName("http://tcs.com/bancs/morganstanley/payments/transfers", "loadTransfer");
    private final static QName _TransferRequestException_QNAME = new QName("http://tcs.com/bancs/morganstanley/payments/transfers", "TransferRequestException");
    private final static QName _CreateTransferResponse_QNAME = new QName("http://tcs.com/bancs/morganstanley/payments/transfers", "createTransferResponse");
    private final static QName _SelectAccountResponse_QNAME = new QName("http://tcs.com/bancs/morganstanley/payments/transfers", "selectAccountResponse");
    private final static QName _SelectAccount_QNAME = new QName("http://tcs.com/bancs/morganstanley/payments/transfers", "selectAccount");
    private final static QName _CreateTransfer_QNAME = new QName("http://tcs.com/bancs/morganstanley/payments/transfers", "createTransfer");
    private final static QName _LoadTransferResponse_QNAME = new QName("http://tcs.com/bancs/morganstanley/payments/transfers", "loadTransferResponse");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.tcs.bancs.morganstanley.payments.transfers
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link LoadTransferResponse }
     * 
     */
    public LoadTransferResponse createLoadTransferResponse() {
        return new LoadTransferResponse();
    }

    /**
     * Create an instance of {@link LoadTransfer }
     * 
     */
    public LoadTransfer createLoadTransfer() {
        return new LoadTransfer();
    }

    /**
     * Create an instance of {@link TransferResponse }
     * 
     */
    public TransferResponse createTransferResponse() {
        return new TransferResponse();
    }

    /**
     * Create an instance of {@link SelectAccountResponse }
     * 
     */
    public SelectAccountResponse createSelectAccountResponse() {
        return new SelectAccountResponse();
    }

    /**
     * Create an instance of {@link TransferRequestException }
     * 
     */
    public TransferRequestException createTransferRequestException() {
        return new TransferRequestException();
    }

    /**
     * Create an instance of {@link CreateTransferResponse }
     * 
     */
    public CreateTransferResponse createCreateTransferResponse() {
        return new CreateTransferResponse();
    }

    /**
     * Create an instance of {@link AccountResponse }
     * 
     */
    public AccountResponse createAccountResponse() {
        return new AccountResponse();
    }

    /**
     * Create an instance of {@link ResponseObject }
     * 
     */
    public ResponseObject createResponseObject() {
        return new ResponseObject();
    }

    /**
     * Create an instance of {@link Account }
     * 
     */
    public Account createAccount() {
        return new Account();
    }

    /**
     * Create an instance of {@link CreateTransfer }
     * 
     */
    public CreateTransfer createCreateTransfer() {
        return new CreateTransfer();
    }

    /**
     * Create an instance of {@link TransferRequest }
     * 
     */
    public TransferRequest createTransferRequest() {
        return new TransferRequest();
    }

    /**
     * Create an instance of {@link UserDetails }
     * 
     */
    public UserDetails createUserDetails() {
        return new UserDetails();
    }

    /**
     * Create an instance of {@link PaymentRequest }
     * 
     */
    public PaymentRequest createPaymentRequest() {
        return new PaymentRequest();
    }

    /**
     * Create an instance of {@link Message }
     * 
     */
    public Message createMessage() {
        return new Message();
    }

    /**
     * Create an instance of {@link PaymentResponse }
     * 
     */
    public PaymentResponse createPaymentResponse() {
        return new PaymentResponse();
    }

    /**
     * Create an instance of {@link ServiceContext }
     * 
     */
    public ServiceContext createServiceContext() {
        return new ServiceContext();
    }

    /**
     * Create an instance of {@link SelectAccount }
     * 
     */
    public SelectAccount createSelectAccount() {
        return new SelectAccount();
    }

    /**
     * Create an instance of {@link RecurringDetails }
     * 
     */
    public RecurringDetails createRecurringDetails() {
        return new RecurringDetails();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link LoadTransfer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tcs.com/bancs/morganstanley/payments/transfers", name = "loadTransfer")
    public JAXBElement<LoadTransfer> createLoadTransfer(LoadTransfer value) {
        return new JAXBElement<LoadTransfer>(_LoadTransfer_QNAME, LoadTransfer.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link TransferRequestException }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tcs.com/bancs/morganstanley/payments/transfers", name = "TransferRequestException")
    public JAXBElement<TransferRequestException> createTransferRequestException(TransferRequestException value) {
        return new JAXBElement<TransferRequestException>(_TransferRequestException_QNAME, TransferRequestException.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CreateTransferResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tcs.com/bancs/morganstanley/payments/transfers", name = "createTransferResponse")
    public JAXBElement<CreateTransferResponse> createCreateTransferResponse(CreateTransferResponse value) {
        return new JAXBElement<CreateTransferResponse>(_CreateTransferResponse_QNAME, CreateTransferResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SelectAccountResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tcs.com/bancs/morganstanley/payments/transfers", name = "selectAccountResponse")
    public JAXBElement<SelectAccountResponse> createSelectAccountResponse(SelectAccountResponse value) {
        return new JAXBElement<SelectAccountResponse>(_SelectAccountResponse_QNAME, SelectAccountResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SelectAccount }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tcs.com/bancs/morganstanley/payments/transfers", name = "selectAccount")
    public JAXBElement<SelectAccount> createSelectAccount(SelectAccount value) {
        return new JAXBElement<SelectAccount>(_SelectAccount_QNAME, SelectAccount.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CreateTransfer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tcs.com/bancs/morganstanley/payments/transfers", name = "createTransfer")
    public JAXBElement<CreateTransfer> createCreateTransfer(CreateTransfer value) {
        return new JAXBElement<CreateTransfer>(_CreateTransfer_QNAME, CreateTransfer.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link LoadTransferResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tcs.com/bancs/morganstanley/payments/transfers", name = "loadTransferResponse")
    public JAXBElement<LoadTransferResponse> createLoadTransferResponse(LoadTransferResponse value) {
        return new JAXBElement<LoadTransferResponse>(_LoadTransferResponse_QNAME, LoadTransferResponse.class, null, value);
    }

}
