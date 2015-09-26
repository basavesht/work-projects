/*
 * ExportForm
 * 
 * Version 1.1
 *
 * Date: 12/09/2005
 *  
 * Copyright (c) Tata Consutlancy Services, Inc. All Rights Reserved.
 * This software is the confidential and proprietary information of 
 * Tata Consultancy Services ("Confidential Information"). You shall not
 * disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with Tata Consultancy Services.
 */
package com.tcs.ebw.reports;

import java.util.LinkedHashMap;
import com.tcs.ebw.mvc.validator.EbwForm;

import com.tcs.ebw.taglib.DataInterface;
import java.util.Vector;

/**
 * ExportForm is a simple javabean used to popuplate  ExportForm data.
 * @author    tcs
 * @version   1.0
 */
public class ExportForm extends EbwForm {

   private String issueDate = "";
   private String description = "";
   private String ccyAmount="";
   private String ccyType = "";
   private String maxCreditAmount ="";
   private String expiryDate = "";
   private String at="";
   private String custRef="";
   private String status = "";
   private String ref = "";
   private String lic = "";
   private String transShip = "";
   private String partailShip = "";
   private String transfer = "";
   private String code = "";
   private String sight = "";
   private String condition = "";
   private String time = "";
   private String shipFrom ="";
   private String shipTo ="";
   private String incoTerm ="";
   private String location ="";
   private String shipmentPeriod ="";
   private String presentationPeriod ="";
   private String addlText ="";
   private String benBank ="";
   private String advBank ="";
   private String conBank ="";
   private String issueComm ="";
   private String issueCC ="";
   private String advBankFee ="";
   private String confirmFee="";
   private String negoFee ="";
   private String exportType = "";
   private LinkedHashMap groups = new LinkedHashMap();

  public String getissueDate() {
    return (this.issueDate);
  }
  public void setissueDate(String issueDate) {
    this.issueDate = issueDate;
  }

  public String getDescription() {
    return (this.description);
  }
  public void setDescription(String description) {
    this.description = description;
  }

  public String getCurrencyType(){
    return this.ccyType;
  }
  public void setCurrencyType(String ccyType){
  	this.ccyType = ccyType;
  }
  
 
   public String getCcyAmount() {
    return (this.ccyAmount);
  }
  public void setCcyAmount(String ccyAmount) {
    this.ccyAmount = ccyAmount;
  }

  public String getMaxCreditAmount() {
    return (this.maxCreditAmount);
  }
  public void setMaxCreditAmount(String maxCreditAmount) {
    this.maxCreditAmount = maxCreditAmount;
  }
 
 public String getExpiryDate() {
    return (this.expiryDate);
  }
  public void setExpiryDate(String expiryDate) {
    this.expiryDate = expiryDate;
  }
 
  public String getAt() {
    return (this.at);
  }
  
  public void setAt(String at) {
    this.at = at;
  }

  public String getCustRef() {
    return (this.custRef);
  }
  public void setCustomerRef(String custRef) {
    this.custRef = custRef;
  }

  public String getStatus() {
    return (this.status);
  }
  public void setStatus(String status) {
    this.status = status;
  }

  public String getRef() {
    return (this.ref);
  }
  public void setRef(String ref) {
    this.ref = ref;
  }

  public String getLic() {
    return (this.lic);
  }
  public void setLic(String lic) {
    this.lic = lic;
  }

  public String getTransShip() {
    return (this.transShip);
  }
  public void setTransship(String transship) {
    this.transShip = transship;
  }
  public String getPartailShip() {
    return (this.partailShip);
  }
  public void setPartailShip(String partailShip) {
    this.partailShip = partailShip;
  }
 
  public String getTransfer() {
    return (this.transfer);
  }
   public void setTransfer(String transfer) {
    this.transfer = transfer;
  }

  public String getcode() {
    return (this.code);
  }
   public void setCode(String code) {
    this.code = code;
  }

 public String getSight() {
    return (this.sight);
  }
   public void setSight(String sight) {
    this.sight = sight;
  }

 public String getCondition() {
    return (this.condition);
  }
   public void setCondition(String condition) {
    this.condition = condition;
  }

 public String getTime() {
    return (this.time);
  }
 public void setTime(String time) {
    this.time = time;
  }

public String getShipFrom() {
    return (this.shipFrom);
  }
 public void setShipFrom(String shipFrom) {
    this.shipFrom = shipFrom;
  }
public String getShipTo() {
    return (this.shipTo);
  }
 public void setShipTo(String shipTo) {
    this.shipTo = shipTo;
  }
 public void setIncoTerm(String incoTerm) {
    this.incoTerm = incoTerm;
  }
public String getIncoTerm() {
    return (this.incoTerm);
  }
 public void setLocation(String location) {
    this.location = location;
  }
public String getLocation() {
    return (this.location);
  }
 public void setShipmentPeriod(String shipmentPeriod) {
    this.shipmentPeriod = shipmentPeriod;
  }
public String getShipmentPeriod() {
    return (this.shipmentPeriod);
  }
 public void setPresentationPeriod(String presentationPeriod) {
    this.presentationPeriod =presentationPeriod ;
  }
public String getPresentationPeriod() {
    return (this.presentationPeriod);
  }
 public void setAddlText(String addlText) {
    this.addlText = addlText;
  }
public String getAddlText() {
    return (this.addlText);
  }
 public void setBenBank(String benBank) {
    this.benBank =benBank ;
  }
public String getBenBank() {
    return (this.benBank);
  }
 public void setAdvBank(String advBank) {
    this.advBank = advBank;
  }
public String getAdvBank() {
    return (this.advBank);
  }
 public void setConBank(String conBank) {
    this.conBank = conBank;
  }
public String getConBank() {
    return (this.conBank);
  }
 public void setIssueComm(String issueComm) {
    this.issueComm = issueComm;
  }
public String getIssueComm() {
    return (this.issueComm);
  }
 public void setIssueCC(String issueCC) {
    this.issueCC = issueCC ;
  }
public String getIssueCC() {
    return (this.issueCC);
  }
 public void setAdvBankFee(String advBankFee) {
    this.advBankFee = advBankFee;
  }
public String getAdvBankFee() {
    return (this.advBankFee);
  }
 public void setConfirmFee(String confirmFee) {
    this.confirmFee = confirmFee;
  }
public String getConfirmFee() {
    return (this.confirmFee);
  }
 public void setNegoFee(String negoFee) {
    this.negoFee = negoFee;
  }
 public String getNegoFee() {
    return (this.negoFee);
  }
 public void setExportType(String exportType){
	this.exportType = exportType;
}

public String getExportType(){
	return this.exportType;
}
 public DataInterface getTableDataCollection(){
 	TableModel tableModel = new TableModel();
 	return tableModel;
 }
 
 /**
  * This function is used to give the detailed Report information to pdf,excel,cvs etc
  * @return Vector output as vector
  */
   public Vector getReportInformation() {

		Vector reportInfo = new Vector();

		//Screen Name 
		Object[][] screenName = { { "Report", "ScreenName" } };
		reportInfo.addElement(screenName);
		screenName = null;

		// Report - Title Information
		Object[][] titleInfo = { { "informationOnTrading", "Title" } };
		reportInfo.addElement(titleInfo);
		titleInfo = null;
		//  BaseInformation - Group Fields
		Object[][] baseInfo = { { "baseInformation", "Group" },
				{ "issueDate", "textfield" }, { "description", "textfield" },
				{ "ccyType", "textfield" }, { "ccyAmount", "textfield" },
				{ "maxCreditAmount", "textfield" },
				{ "expiryDate", "textfield" }, { "customerRef", "textfield" },
				{ "tableData", "Table" } };
		reportInfo.addElement(baseInfo);
		baseInfo = null;

		// Reference - Group Fields 
		Object[][] refGroup = { { "reference", "Group" },
				{ "status", "textfield" }, { "ref", "textfield" },
				{ "lic", "textfield" } };
		reportInfo.addElement(refGroup);
		refGroup = null;

		//Tenor-Info Group Fields
		Object[][] tenorInfo = { { "tenorInfo", "Group" },
				{ "code", "textfield" }, { "sight", "textfield" },
				{ "condition", "textfield" }, { "time", "textfield" }

		};
		reportInfo.addElement(tenorInfo);
		tenorInfo = null;

		//Indicator - GroupFields
		Object[][] indicator = { { "indicator", "Group" },
				{ "transship", "textfield" }, { "partailShip", "textfield" },
				{ "transfer", "textfield" }, };
		reportInfo.addElement(indicator);
		indicator = null;

		//AddlInfo - Group Fields
		Object[][] addlInfo = { { "addlInfo", "Group" },
				{ "shipFrom", "textfield" }, { "shipTo", "textfield" },
				{ "incoTerm", "textfield" }, { "shipmentPeriod", "textfield" },
				{ "presentationPeriod", "textfield" }, { "tableData", "Table" } };
		reportInfo.add(addlInfo);
		addlInfo = null;

		//Parties - Group Fields
		Object[][] parties = { { "parties", "Group" },
				{ "benBank", "textfield" }, { "conBank", "textfield" },
				{ "advBank", "textfield" }, };
		reportInfo.add(parties);
		parties = null;

		//AddlText - Group Fields
		Object[][] addlText = { { "addlText", "Group" },
				{ "addlText", "textfield" }, };
		reportInfo.addElement(addlText);
		addlText = null;

		//Fees - Group Fields
		Object[][] feesGroup = { { "fees", "Group" },
				{ "issueComm", "textfield" }, { "issueCC", "textfield" },
				{ "advBankFee", "textfield" }, { "confirmFee", "textfield" },
				{ "negoFee", "textfield" }, };
		reportInfo.addElement(feesGroup);
		feesGroup = null;

		//Table Group
		Object[][] tableGroup = { { "tableGroup", "Group" },
				{ "tableData", "Table" } };
		reportInfo.addElement(tableGroup);
		tableGroup = null;

		return reportInfo;
	}  
}
