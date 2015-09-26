
package com.tcs.bancs.morganstanley.payments.transfers;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for selectAccountResponse complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="selectAccountResponse">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{http://tcs.com/bancs/morganstanley/payments/transfers/selectAccount/response}SelectAccountResponseParam" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "selectAccountResponse", propOrder = {
    "selectAccountResponseParam"
})
public class SelectAccountResponse {

    @XmlElement(name = "SelectAccountResponseParam", namespace = "http://tcs.com/bancs/morganstanley/payments/transfers/selectAccount/response")
    protected TransferResponse selectAccountResponseParam;

    /**
     * Gets the value of the selectAccountResponseParam property.
     * 
     * @return
     *     possible object is
     *     {@link TransferResponse }
     *     
     */
    public TransferResponse getSelectAccountResponseParam() {
        return selectAccountResponseParam;
    }

    /**
     * Sets the value of the selectAccountResponseParam property.
     * 
     * @param value
     *     allowed object is
     *     {@link TransferResponse }
     *     
     */
    public void setSelectAccountResponseParam(TransferResponse value) {
        this.selectAccountResponseParam = value;
    }

}
