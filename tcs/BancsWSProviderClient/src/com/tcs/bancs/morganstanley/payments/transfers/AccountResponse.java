
package com.tcs.bancs.morganstanley.payments.transfers;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for accountResponse complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="accountResponse">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="account" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="acntType" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="displayName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="externalAcntRefId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="fa" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="keyAccount" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="office" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "accountResponse", propOrder = {
    "account",
    "acntType",
    "displayName",
    "externalAcntRefId",
    "fa",
    "keyAccount",
    "office"
})
public class AccountResponse {

    protected Integer account;
    protected String acntType;
    protected String displayName;
    protected String externalAcntRefId;
    protected Integer fa;
    protected String keyAccount;
    protected Integer office;

    /**
     * Gets the value of the account property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getAccount() {
        return account;
    }

    /**
     * Sets the value of the account property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setAccount(Integer value) {
        this.account = value;
    }

    /**
     * Gets the value of the acntType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAcntType() {
        return acntType;
    }

    /**
     * Sets the value of the acntType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAcntType(String value) {
        this.acntType = value;
    }

    /**
     * Gets the value of the displayName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * Sets the value of the displayName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDisplayName(String value) {
        this.displayName = value;
    }

    /**
     * Gets the value of the externalAcntRefId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getExternalAcntRefId() {
        return externalAcntRefId;
    }

    /**
     * Sets the value of the externalAcntRefId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setExternalAcntRefId(String value) {
        this.externalAcntRefId = value;
    }

    /**
     * Gets the value of the fa property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getFa() {
        return fa;
    }

    /**
     * Sets the value of the fa property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setFa(Integer value) {
        this.fa = value;
    }

    /**
     * Gets the value of the keyAccount property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getKeyAccount() {
        return keyAccount;
    }

    /**
     * Sets the value of the keyAccount property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setKeyAccount(String value) {
        this.keyAccount = value;
    }

    /**
     * Gets the value of the office property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getOffice() {
        return office;
    }

    /**
     * Sets the value of the office property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setOffice(Integer value) {
        this.office = value;
    }

}
