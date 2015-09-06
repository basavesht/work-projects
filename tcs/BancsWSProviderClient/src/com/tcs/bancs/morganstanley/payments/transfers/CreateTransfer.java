
package com.tcs.bancs.morganstanley.payments.transfers;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for createTransfer complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="createTransfer">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{http://tcs.com/bancs/morganstanley/payments/transfers/createTransfer/request}CreateTransferRequestParam" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "createTransfer", propOrder = {
    "createTransferRequestParam"
})
public class CreateTransfer {

    @XmlElement(name = "CreateTransferRequestParam", namespace = "http://tcs.com/bancs/morganstanley/payments/transfers/createTransfer/request")
    protected TransferRequest createTransferRequestParam;

    /**
     * Gets the value of the createTransferRequestParam property.
     * 
     * @return
     *     possible object is
     *     {@link TransferRequest }
     *     
     */
    public TransferRequest getCreateTransferRequestParam() {
        return createTransferRequestParam;
    }

    /**
     * Sets the value of the createTransferRequestParam property.
     * 
     * @param value
     *     allowed object is
     *     {@link TransferRequest }
     *     
     */
    public void setCreateTransferRequestParam(TransferRequest value) {
        this.createTransferRequestParam = value;
    }

}
