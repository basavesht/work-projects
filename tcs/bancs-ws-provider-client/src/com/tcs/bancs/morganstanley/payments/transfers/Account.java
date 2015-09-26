
package com.tcs.bancs.morganstanley.payments.transfers;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for account complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="account">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="account" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="accountCategory" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="accountClass" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="accountStatus" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="acntType" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="choiceFundCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="clientCategory" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="collateralAcctInd" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="divPay" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="drCrIndicator" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="externalAcntRefId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="fa" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="iraCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="keyAccount" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="novusSubProduct" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="office" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="tradeControl" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "account", propOrder = {
    "account",
    "accountCategory",
    "accountClass",
    "accountStatus",
    "acntType",
    "choiceFundCode",
    "clientCategory",
    "collateralAcctInd",
    "divPay",
    "drCrIndicator",
    "externalAcntRefId",
    "fa",
    "iraCode",
    "keyAccount",
    "novusSubProduct",
    "office",
    "tradeControl"
})
public class Account {

    protected Integer account;
    protected String accountCategory;
    protected String accountClass;
    protected String accountStatus;
    protected Integer acntType;
    protected String choiceFundCode;
    protected String clientCategory;
    protected String collateralAcctInd;
    protected String divPay;
    protected String drCrIndicator;
    protected String externalAcntRefId;
    protected Integer fa;
    protected String iraCode;
    protected String keyAccount;
    protected String novusSubProduct;
    protected Integer office;
    protected String tradeControl;

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
     * Gets the value of the accountCategory property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAccountCategory() {
        return accountCategory;
    }

    /**
     * Sets the value of the accountCategory property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAccountCategory(String value) {
        this.accountCategory = value;
    }

    /**
     * Gets the value of the accountClass property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAccountClass() {
        return accountClass;
    }

    /**
     * Sets the value of the accountClass property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAccountClass(String value) {
        this.accountClass = value;
    }

    /**
     * Gets the value of the accountStatus property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAccountStatus() {
        return accountStatus;
    }

    /**
     * Sets the value of the accountStatus property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAccountStatus(String value) {
        this.accountStatus = value;
    }

    /**
     * Gets the value of the acntType property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getAcntType() {
        return acntType;
    }

    /**
     * Sets the value of the acntType property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setAcntType(Integer value) {
        this.acntType = value;
    }

    /**
     * Gets the value of the choiceFundCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getChoiceFundCode() {
        return choiceFundCode;
    }

    /**
     * Sets the value of the choiceFundCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setChoiceFundCode(String value) {
        this.choiceFundCode = value;
    }

    /**
     * Gets the value of the clientCategory property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getClientCategory() {
        return clientCategory;
    }

    /**
     * Sets the value of the clientCategory property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setClientCategory(String value) {
        this.clientCategory = value;
    }

    /**
     * Gets the value of the collateralAcctInd property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCollateralAcctInd() {
        return collateralAcctInd;
    }

    /**
     * Sets the value of the collateralAcctInd property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCollateralAcctInd(String value) {
        this.collateralAcctInd = value;
    }

    /**
     * Gets the value of the divPay property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDivPay() {
        return divPay;
    }

    /**
     * Sets the value of the divPay property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDivPay(String value) {
        this.divPay = value;
    }

    /**
     * Gets the value of the drCrIndicator property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDrCrIndicator() {
        return drCrIndicator;
    }

    /**
     * Sets the value of the drCrIndicator property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDrCrIndicator(String value) {
        this.drCrIndicator = value;
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
     * Gets the value of the iraCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIraCode() {
        return iraCode;
    }

    /**
     * Sets the value of the iraCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIraCode(String value) {
        this.iraCode = value;
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
     * Gets the value of the novusSubProduct property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNovusSubProduct() {
        return novusSubProduct;
    }

    /**
     * Sets the value of the novusSubProduct property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNovusSubProduct(String value) {
        this.novusSubProduct = value;
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

    /**
     * Gets the value of the tradeControl property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTradeControl() {
        return tradeControl;
    }

    /**
     * Sets the value of the tradeControl property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTradeControl(String value) {
        this.tradeControl = value;
    }

}
