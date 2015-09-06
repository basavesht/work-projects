
package com.tcs.bancs.morganstanley.payments.transfers;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for transferResponse complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="transferResponse">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="paymentResponse" type="{http://tcs.com/bancs/morganstanley/payments/transfers}paymentResponse" minOccurs="0"/>
 *         &lt;element name="responseObject" type="{http://tcs.com/bancs/morganstanley/payments/transfers}responseObject" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "transferResponse", propOrder = {
    "paymentResponse",
    "responseObject"
})
public class TransferResponse {

    protected PaymentResponse paymentResponse;
    protected ResponseObject responseObject;

    /**
     * Gets the value of the paymentResponse property.
     * 
     * @return
     *     possible object is
     *     {@link PaymentResponse }
     *     
     */
    public PaymentResponse getPaymentResponse() {
        return paymentResponse;
    }

    /**
     * Sets the value of the paymentResponse property.
     * 
     * @param value
     *     allowed object is
     *     {@link PaymentResponse }
     *     
     */
    public void setPaymentResponse(PaymentResponse value) {
        this.paymentResponse = value;
    }

    /**
     * Gets the value of the responseObject property.
     * 
     * @return
     *     possible object is
     *     {@link ResponseObject }
     *     
     */
    public ResponseObject getResponseObject() {
        return responseObject;
    }

    /**
     * Sets the value of the responseObject property.
     * 
     * @param value
     *     allowed object is
     *     {@link ResponseObject }
     *     
     */
    public void setResponseObject(ResponseObject value) {
        this.responseObject = value;
    }

}
