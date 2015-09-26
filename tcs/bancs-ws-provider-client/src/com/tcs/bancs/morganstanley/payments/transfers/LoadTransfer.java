
package com.tcs.bancs.morganstanley.payments.transfers;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for loadTransfer complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="loadTransfer">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{http://tcs.com/bancs/morganstanley/payments/transfers/loadTransfer/request}loadTransferRequestParam" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "loadTransfer", propOrder = {
    "loadTransferRequestParam"
})
public class LoadTransfer {

    @XmlElement(namespace = "http://tcs.com/bancs/morganstanley/payments/transfers/loadTransfer/request")
    protected TransferRequest loadTransferRequestParam;

    /**
     * Gets the value of the loadTransferRequestParam property.
     * 
     * @return
     *     possible object is
     *     {@link TransferRequest }
     *     
     */
    public TransferRequest getLoadTransferRequestParam() {
        return loadTransferRequestParam;
    }

    /**
     * Sets the value of the loadTransferRequestParam property.
     * 
     * @param value
     *     allowed object is
     *     {@link TransferRequest }
     *     
     */
    public void setLoadTransferRequestParam(TransferRequest value) {
        this.loadTransferRequestParam = value;
    }

}
