
package com.tcs.bancs.morganstanley.payments.transfers;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for loadTransferResponse complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="loadTransferResponse">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{http://tcs.com/bancs/morganstanley/payments/transfers/loadTransfer/response}LoadTransferResponseParam" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "loadTransferResponse", propOrder = {
    "loadTransferResponseParam"
})
public class LoadTransferResponse {

    @XmlElement(name = "LoadTransferResponseParam", namespace = "http://tcs.com/bancs/morganstanley/payments/transfers/loadTransfer/response")
    protected TransferResponse loadTransferResponseParam;

    /**
     * Gets the value of the loadTransferResponseParam property.
     * 
     * @return
     *     possible object is
     *     {@link TransferResponse }
     *     
     */
    public TransferResponse getLoadTransferResponseParam() {
        return loadTransferResponseParam;
    }

    /**
     * Sets the value of the loadTransferResponseParam property.
     * 
     * @param value
     *     allowed object is
     *     {@link TransferResponse }
     *     
     */
    public void setLoadTransferResponseParam(TransferResponse value) {
        this.loadTransferResponseParam = value;
    }

}
