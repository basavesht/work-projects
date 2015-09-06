
package com.tcs.bancs.morganstanley.payments.transfers.selectaccount.request;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;
import com.tcs.bancs.morganstanley.payments.transfers.TransferRequest;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the com.tcs.bancs.morganstanley.payments.transfers.selectaccount.request package. 
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

    private final static QName _SelectAccountRequestParam_QNAME = new QName("http://tcs.com/bancs/morganstanley/payments/transfers/selectAccount/request", "SelectAccountRequestParam");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.tcs.bancs.morganstanley.payments.transfers.selectaccount.request
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link TransferRequest }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tcs.com/bancs/morganstanley/payments/transfers/selectAccount/request", name = "SelectAccountRequestParam")
    public JAXBElement<TransferRequest> createSelectAccountRequestParam(TransferRequest value) {
        return new JAXBElement<TransferRequest>(_SelectAccountRequestParam_QNAME, TransferRequest.class, null, value);
    }

}
