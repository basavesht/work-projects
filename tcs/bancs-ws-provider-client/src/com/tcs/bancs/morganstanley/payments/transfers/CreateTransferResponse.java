
package com.tcs.bancs.morganstanley.payments.transfers;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for createTransferResponse complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="createTransferResponse">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{http://tcs.com/bancs/morganstanley/payments/transfers/createTransfer/response}CreateTransferResponseParam" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "createTransferResponse", propOrder = {
    "createTransferResponseParam"
})
public class CreateTransferResponse {

    @XmlElement(name = "CreateTransferResponseParam", namespace = "http://tcs.com/bancs/morganstanley/payments/transfers/createTransfer/response")
    protected TransferResponse createTransferResponseParam;

    /**
     * Gets the value of the createTransferResponseParam property.
     * 
     * @return
     *     possible object is
     *     {@link TransferResponse }
     *     
     */
    public TransferResponse getCreateTransferResponseParam() {
        return createTransferResponseParam;
    }

    /**
     * Sets the value of the createTransferResponseParam property.
     * 
     * @param value
     *     allowed object is
     *     {@link TransferResponse }
     *     
     */
    public void setCreateTransferResponseParam(TransferResponse value) {
        this.createTransferResponseParam = value;
    }

}
