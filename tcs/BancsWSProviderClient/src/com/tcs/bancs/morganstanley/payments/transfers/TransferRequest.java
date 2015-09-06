
package com.tcs.bancs.morganstanley.payments.transfers;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for transferRequest complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="transferRequest">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="paymentRequest" type="{http://tcs.com/bancs/morganstanley/payments/transfers}paymentRequest" minOccurs="0"/>
 *         &lt;element name="userDetails" type="{http://tcs.com/bancs/morganstanley/payments/transfers}userDetails" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "transferRequest", propOrder = {
    "paymentRequest",
    "userDetails"
})
public class TransferRequest {

    protected PaymentRequest paymentRequest;
    protected UserDetails userDetails;

    /**
     * Gets the value of the paymentRequest property.
     * 
     * @return
     *     possible object is
     *     {@link PaymentRequest }
     *     
     */
    public PaymentRequest getPaymentRequest() {
        return paymentRequest;
    }

    /**
     * Sets the value of the paymentRequest property.
     * 
     * @param value
     *     allowed object is
     *     {@link PaymentRequest }
     *     
     */
    public void setPaymentRequest(PaymentRequest value) {
        this.paymentRequest = value;
    }

    /**
     * Gets the value of the userDetails property.
     * 
     * @return
     *     possible object is
     *     {@link UserDetails }
     *     
     */
    public UserDetails getUserDetails() {
        return userDetails;
    }

    /**
     * Sets the value of the userDetails property.
     * 
     * @param value
     *     allowed object is
     *     {@link UserDetails }
     *     
     */
    public void setUserDetails(UserDetails value) {
        this.userDetails = value;
    }

}
