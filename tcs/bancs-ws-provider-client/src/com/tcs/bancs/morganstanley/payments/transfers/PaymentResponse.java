
package com.tcs.bancs.morganstanley.payments.transfers;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for paymentResponse complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="paymentResponse">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="accountResponse" type="{http://tcs.com/bancs/morganstanley/payments/transfers}accountResponse" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="confirmationNo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="paymentStatus" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="serviceContext" type="{http://tcs.com/bancs/morganstanley/payments/transfers}serviceContext" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "paymentResponse", propOrder = {
    "accountResponse",
    "confirmationNo",
    "paymentStatus",
    "serviceContext"
})
public class PaymentResponse {

    @XmlElement(nillable = true)
    protected List<AccountResponse> accountResponse;
    protected String confirmationNo;
    protected Integer paymentStatus;
    protected ServiceContext serviceContext;

    /**
     * Gets the value of the accountResponse property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the accountResponse property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getAccountResponse().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link AccountResponse }
     * 
     * 
     */
    public List<AccountResponse> getAccountResponse() {
        if (accountResponse == null) {
            accountResponse = new ArrayList<AccountResponse>();
        }
        return this.accountResponse;
    }

    /**
     * Gets the value of the confirmationNo property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getConfirmationNo() {
        return confirmationNo;
    }

    /**
     * Sets the value of the confirmationNo property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setConfirmationNo(String value) {
        this.confirmationNo = value;
    }

    /**
     * Gets the value of the paymentStatus property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getPaymentStatus() {
        return paymentStatus;
    }

    /**
     * Sets the value of the paymentStatus property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setPaymentStatus(Integer value) {
        this.paymentStatus = value;
    }

    /**
     * Gets the value of the serviceContext property.
     * 
     * @return
     *     possible object is
     *     {@link ServiceContext }
     *     
     */
    public ServiceContext getServiceContext() {
        return serviceContext;
    }

    /**
     * Sets the value of the serviceContext property.
     * 
     * @param value
     *     allowed object is
     *     {@link ServiceContext }
     *     
     */
    public void setServiceContext(ServiceContext value) {
        this.serviceContext = value;
    }

}
