
package com.tcs.bancs.morganstanley.payments.transfers;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Java class for paymentRequest complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="paymentRequest">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="account" type="{http://tcs.com/bancs/morganstanley/payments/transfers}account" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="amount" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/>
 *         &lt;element name="comments" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="recurringDetails" type="{http://tcs.com/bancs/morganstanley/payments/transfers}recurringDetails" minOccurs="0"/>
 *         &lt;element name="transferAction" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="transferDate" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *         &lt;element name="transferSchedule" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="transferType" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="txnConfirmationNo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "paymentRequest", propOrder = {
    "account",
    "amount",
    "comments",
    "recurringDetails",
    "transferAction",
    "transferDate",
    "transferSchedule",
    "transferType",
    "txnConfirmationNo"
})
public class PaymentRequest {

    @XmlElement(nillable = true)
    protected List<Account> account;
    protected BigDecimal amount;
    protected String comments;
    protected RecurringDetails recurringDetails;
    protected String transferAction;
    protected XMLGregorianCalendar transferDate;
    protected Integer transferSchedule;
    protected String transferType;
    protected String txnConfirmationNo;

    /**
     * Gets the value of the account property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the account property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getAccount().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Account }
     * 
     * 
     */
    public List<Account> getAccount() {
        if (account == null) {
            account = new ArrayList<Account>();
        }
        return this.account;
    }

    /**
     * Gets the value of the amount property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getAmount() {
        return amount;
    }

    /**
     * Sets the value of the amount property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setAmount(BigDecimal value) {
        this.amount = value;
    }

    /**
     * Gets the value of the comments property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getComments() {
        return comments;
    }

    /**
     * Sets the value of the comments property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setComments(String value) {
        this.comments = value;
    }

    /**
     * Gets the value of the recurringDetails property.
     * 
     * @return
     *     possible object is
     *     {@link RecurringDetails }
     *     
     */
    public RecurringDetails getRecurringDetails() {
        return recurringDetails;
    }

    /**
     * Sets the value of the recurringDetails property.
     * 
     * @param value
     *     allowed object is
     *     {@link RecurringDetails }
     *     
     */
    public void setRecurringDetails(RecurringDetails value) {
        this.recurringDetails = value;
    }

    /**
     * Gets the value of the transferAction property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTransferAction() {
        return transferAction;
    }

    /**
     * Sets the value of the transferAction property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTransferAction(String value) {
        this.transferAction = value;
    }

    /**
     * Gets the value of the transferDate property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getTransferDate() {
        return transferDate;
    }

    /**
     * Sets the value of the transferDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setTransferDate(XMLGregorianCalendar value) {
        this.transferDate = value;
    }

    /**
     * Gets the value of the transferSchedule property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getTransferSchedule() {
        return transferSchedule;
    }

    /**
     * Sets the value of the transferSchedule property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setTransferSchedule(Integer value) {
        this.transferSchedule = value;
    }

    /**
     * Gets the value of the transferType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTransferType() {
        return transferType;
    }

    /**
     * Sets the value of the transferType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTransferType(String value) {
        this.transferType = value;
    }

    /**
     * Gets the value of the txnConfirmationNo property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTxnConfirmationNo() {
        return txnConfirmationNo;
    }

    /**
     * Sets the value of the txnConfirmationNo property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTxnConfirmationNo(String value) {
        this.txnConfirmationNo = value;
    }

}
