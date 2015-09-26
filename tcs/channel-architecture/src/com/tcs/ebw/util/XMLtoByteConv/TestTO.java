package com.tcs.ebw.util.XMLtoByteConv;

import com.tcs.ebw.transferobject.EBWTransferObject;

/**
 * @author 231259
 * @year 2008
 */
public class TestTO extends EBWTransferObject {
	
	public String scrCustOrAcctNum;
	public String scrIdNum;
	public String scrIdType;
	public String scrSystemIdCode;
	public String scrCustOrAcctOpt;
	public String screenId;
	
	public TestTO() {
		this.scrCustOrAcctNum = "8122089";
		this.scrIdNum = "";
		this.scrIdType = "";
		this.scrSystemIdCode = "";
		this.scrCustOrAcctOpt = "";
		this.screenId = "062000";
	}

	public String getScrCustOrAcctNum() {
		return scrCustOrAcctNum;
	}

	public void setScrCustOrAcctNum(String scrCustOrAcctNum) {
		this.scrCustOrAcctNum = scrCustOrAcctNum;
	}

	public String getScrIdNum() {
		return scrIdNum;
	}

	public void setScrIdNum(String scrIdNum) {
		this.scrIdNum = scrIdNum;
	}

	public String getScrIdType() {
		return scrIdType;
	}

	public void setScrIdType(String scrIdType) {
		this.scrIdType = scrIdType;
	}

	public String getScrSystemIdCode() {
		return scrSystemIdCode;
	}

	public void setScrSystemIdCode(String scrSystemIdCode) {
		this.scrSystemIdCode = scrSystemIdCode;
	}

	public String getScrCustOrAcctOpt() {
		return scrCustOrAcctOpt;
	}

	public void setScrCustOrAcctOpt(String scrCustOrAcctOpt) {
		this.scrCustOrAcctOpt = scrCustOrAcctOpt;
	}

	public String getScreenId() {
		return screenId;
	}

	public void setScreenId(String screenId) {
		this.screenId = screenId;
	}
}
