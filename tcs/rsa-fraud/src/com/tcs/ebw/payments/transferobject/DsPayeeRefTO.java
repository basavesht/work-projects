/*
 * Created on Thu Mar 26 21:57:30 IST 2009
 *
 * Copyright Tata Consultancy Services. All rights reserved.
 * 
 */

package com.tcs.ebw.payments.transferobject;

import java.util.Date;

import com.tcs.ebw.transferobject.EBWTransferObject;

/**
 * @author : 224703
 * **************   Revision History ************************
 * Modified-By |  Modified-Date |  Project-Phase  | Changes
 *
 * **********************************************************
 */
public class DsPayeeRefTO extends EBWTransferObject implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4065658543953959384L;
	private String cpygrpid = null;
	private String cpypayeeid = null;
	private String cpypayeename1 = null;
	private String cpyadd1 = null;
	private String cpyadd2 = null;
	private String cpyadd3 = null;
	private String cpycustaccnum = null;
	private String cpyaccnum = null;
	private String cpyacctype = null;
	private String cpyfaxnum = null;
	private String cpyctrycode = null;
	private String cpyintermediarybankcode = null;
	private String cpycitycode = null;
	private String cpypayeename2 = null;
	private String cpytaxid = null;
	private String cpyadd4 = null;
	private String cpyboname1 = null;
	private String cpybradd1 = null;
	private String cpyboaddr1 = null;
	private String cpybradd2 = null;
	private String cpyboaddr2 = null;
	private String cpybradd3 = null;
	private String createdby = null;
	private String cpyboaddr3 = null;
	private String cpybankcode = null;
	private Date createddate = null;
	private String modifiedby = null;
	private String cpybranchcode = null;
	private Date modifieddate = null;
	private String cpybranchscode = null;
	private String deleteflag = null;
	private String cpyclrcodett = null;
	private Double versionnum = null;
	private String cpycustodybank = null;
	private String cpybanklocalcode = null;
	private Double cpybenetype = null;
	private String cpystatus = null;
	private String cpypayeerefno = null;
	private Double cpythresholdamt = null;
	private Double cpypaymentscount = null;
	private String cpycurrency = null;
	private String cpybankswiftcode = null;
	private String cpyinterbankswiftcode = null;
	private String cpyinterbankaccnum = null;
	private String authorisedby = null;
	private Date authoriseddate = null;
	private String bfsfilereferenceno = null;
	private Double cpysortcode = null;
	private String life_user_id = null;
	private Double ver_trials = null;
	private String key_client = null;
	private String key_link = null;
	private String sign_up_mode = null;
	private String account_owner = null;
	private String account_form = null;
	private String voided_check = null;
	// added for Clubbing Key_Client & Key_Link vals
	private String[] keyClientId = null;

	/**
	 * @param cpygrpid the cpygrpid to set.
	 */
	public void setCpygrpid(String cpygrpid) {
		this.cpygrpid=cpygrpid;
	}
	/**
	 * @return Returns the cpygrpid.
	 */
	public String getCpygrpid() {
		return this.cpygrpid;
	}

	/**
	 * @param cpypayeeid the cpypayeeid to set.
	 */
	public void setCpypayeeid(String cpypayeeid) {
		this.cpypayeeid=cpypayeeid;
	}
	/**
	 * @return Returns the cpypayeeid.
	 */
	public String getCpypayeeid() {
		return this.cpypayeeid;
	}

	/**
	 * @param cpypayeename1 the cpypayeename1 to set.
	 */
	public void setCpypayeename1(String cpypayeename1) {
		this.cpypayeename1=cpypayeename1;
	}
	/**
	 * @return Returns the cpypayeename1.
	 */
	public String getCpypayeename1() {
		return this.cpypayeename1;
	}

	/**
	 * @param cpyadd1 the cpyadd1 to set.
	 */
	public void setCpyadd1(String cpyadd1) {
		this.cpyadd1=cpyadd1;
	}
	/**
	 * @return Returns the cpyadd1.
	 */
	public String getCpyadd1() {
		return this.cpyadd1;
	}

	/**
	 * @param cpyadd2 the cpyadd2 to set.
	 */
	public void setCpyadd2(String cpyadd2) {
		this.cpyadd2=cpyadd2;
	}
	/**
	 * @return Returns the cpyadd2.
	 */
	public String getCpyadd2() {
		return this.cpyadd2;
	}

	/**
	 * @param cpyadd3 the cpyadd3 to set.
	 */
	public void setCpyadd3(String cpyadd3) {
		this.cpyadd3=cpyadd3;
	}
	/**
	 * @return Returns the cpyadd3.
	 */
	public String getCpyadd3() {
		return this.cpyadd3;
	}

	/**
	 * @param cpycustaccnum the cpycustaccnum to set.
	 */
	public void setCpycustaccnum(String cpycustaccnum) {
		this.cpycustaccnum=cpycustaccnum;
	}
	/**
	 * @return Returns the cpycustaccnum.
	 */
	public String getCpycustaccnum() {
		return this.cpycustaccnum;
	}

	/**
	 * @param cpyaccnum the cpyaccnum to set.
	 */
	public void setCpyaccnum(String cpyaccnum) {
		this.cpyaccnum=cpyaccnum;
	}
	/**
	 * @return Returns the cpyaccnum.
	 */
	public String getCpyaccnum() {
		return this.cpyaccnum;
	}

	/**
	 * @param cpyacctype the cpyacctype to set.
	 */
	public void setCpyacctype(String cpyacctype) {
		this.cpyacctype=cpyacctype;
	}
	/**
	 * @return Returns the cpyacctype.
	 */
	public String getCpyacctype() {
		return this.cpyacctype;
	}

	/**
	 * @param cpyfaxnum the cpyfaxnum to set.
	 */
	public void setCpyfaxnum(String cpyfaxnum) {
		this.cpyfaxnum=cpyfaxnum;
	}
	/**
	 * @return Returns the cpyfaxnum.
	 */
	public String getCpyfaxnum() {
		return this.cpyfaxnum;
	}

	/**
	 * @param cpyctrycode the cpyctrycode to set.
	 */
	public void setCpyctrycode(String cpyctrycode) {
		this.cpyctrycode=cpyctrycode;
	}
	/**
	 * @return Returns the cpyctrycode.
	 */
	public String getCpyctrycode() {
		return this.cpyctrycode;
	}

	/**
	 * @param cpyintermediarybankcode the cpyintermediarybankcode to set.
	 */
	public void setCpyintermediarybankcode(String cpyintermediarybankcode) {
		this.cpyintermediarybankcode=cpyintermediarybankcode;
	}
	/**
	 * @return Returns the cpyintermediarybankcode.
	 */
	public String getCpyintermediarybankcode() {
		return this.cpyintermediarybankcode;
	}

	/**
	 * @param cpycitycode the cpycitycode to set.
	 */
	public void setCpycitycode(String cpycitycode) {
		this.cpycitycode=cpycitycode;
	}
	/**
	 * @return Returns the cpycitycode.
	 */
	public String getCpycitycode() {
		return this.cpycitycode;
	}

	/**
	 * @param cpypayeename2 the cpypayeename2 to set.
	 */
	public void setCpypayeename2(String cpypayeename2) {
		this.cpypayeename2=cpypayeename2;
	}
	/**
	 * @return Returns the cpypayeename2.
	 */
	public String getCpypayeename2() {
		return this.cpypayeename2;
	}

	/**
	 * @param cpytaxid the cpytaxid to set.
	 */
	public void setCpytaxid(String cpytaxid) {
		this.cpytaxid=cpytaxid;
	}
	/**
	 * @return Returns the cpytaxid.
	 */
	public String getCpytaxid() {
		return this.cpytaxid;
	}

	/**
	 * @param cpyadd4 the cpyadd4 to set.
	 */
	public void setCpyadd4(String cpyadd4) {
		this.cpyadd4=cpyadd4;
	}
	/**
	 * @return Returns the cpyadd4.
	 */
	public String getCpyadd4() {
		return this.cpyadd4;
	}

	/**
	 * @param cpyboname1 the cpyboname1 to set.
	 */
	public void setCpyboname1(String cpyboname1) {
		this.cpyboname1=cpyboname1;
	}
	/**
	 * @return Returns the cpyboname1.
	 */
	public String getCpyboname1() {
		return this.cpyboname1;
	}

	/**
	 * @param cpybradd1 the cpybradd1 to set.
	 */
	public void setCpybradd1(String cpybradd1) {
		this.cpybradd1=cpybradd1;
	}
	/**
	 * @return Returns the cpybradd1.
	 */
	public String getCpybradd1() {
		return this.cpybradd1;
	}

	/**
	 * @param cpyboaddr1 the cpyboaddr1 to set.
	 */
	public void setCpyboaddr1(String cpyboaddr1) {
		this.cpyboaddr1=cpyboaddr1;
	}
	/**
	 * @return Returns the cpyboaddr1.
	 */
	public String getCpyboaddr1() {
		return this.cpyboaddr1;
	}

	/**
	 * @param cpybradd2 the cpybradd2 to set.
	 */
	public void setCpybradd2(String cpybradd2) {
		this.cpybradd2=cpybradd2;
	}
	/**
	 * @return Returns the cpybradd2.
	 */
	public String getCpybradd2() {
		return this.cpybradd2;
	}

	/**
	 * @param cpyboaddr2 the cpyboaddr2 to set.
	 */
	public void setCpyboaddr2(String cpyboaddr2) {
		this.cpyboaddr2=cpyboaddr2;
	}
	/**
	 * @return Returns the cpyboaddr2.
	 */
	public String getCpyboaddr2() {
		return this.cpyboaddr2;
	}

	/**
	 * @param cpybradd3 the cpybradd3 to set.
	 */
	public void setCpybradd3(String cpybradd3) {
		this.cpybradd3=cpybradd3;
	}
	/**
	 * @return Returns the cpybradd3.
	 */
	public String getCpybradd3() {
		return this.cpybradd3;
	}

	/**
	 * @param createdby the createdby to set.
	 */
	public void setCreatedby(String createdby) {
		this.createdby=createdby;
	}
	/**
	 * @return Returns the createdby.
	 */
	public String getCreatedby() {
		return this.createdby;
	}

	/**
	 * @param cpyboaddr3 the cpyboaddr3 to set.
	 */
	public void setCpyboaddr3(String cpyboaddr3) {
		this.cpyboaddr3=cpyboaddr3;
	}
	/**
	 * @return Returns the cpyboaddr3.
	 */
	public String getCpyboaddr3() {
		return this.cpyboaddr3;
	}

	/**
	 * @param cpybankcode the cpybankcode to set.
	 */
	public void setCpybankcode(String cpybankcode) {
		this.cpybankcode=cpybankcode;
	}
	/**
	 * @return Returns the cpybankcode.
	 */
	public String getCpybankcode() {
		return this.cpybankcode;
	}

	/**
	 * @param createddate the createddate to set.
	 */
	public void setCreateddate(Date createddate) {
		this.createddate=createddate;
	}
	/**
	 * @return Returns the createddate.
	 */
	public Date getCreateddate() {
		return this.createddate;
	}

	/**
	 * @param modifiedby the modifiedby to set.
	 */
	public void setModifiedby(String modifiedby) {
		this.modifiedby=modifiedby;
	}
	/**
	 * @return Returns the modifiedby.
	 */
	public String getModifiedby() {
		return this.modifiedby;
	}

	/**
	 * @param cpybranchcode the cpybranchcode to set.
	 */
	public void setCpybranchcode(String cpybranchcode) {
		this.cpybranchcode=cpybranchcode;
	}
	/**
	 * @return Returns the cpybranchcode.
	 */
	public String getCpybranchcode() {
		return this.cpybranchcode;
	}

	/**
	 * @param modifieddate the modifieddate to set.
	 */
	public void setModifieddate(Date modifieddate) {
		this.modifieddate=modifieddate;
	}
	/**
	 * @return Returns the modifieddate.
	 */
	public Date getModifieddate() {
		return this.modifieddate;
	}

	/**
	 * @param cpybranchscode the cpybranchscode to set.
	 */
	public void setCpybranchscode(String cpybranchscode) {
		this.cpybranchscode=cpybranchscode;
	}
	/**
	 * @return Returns the cpybranchscode.
	 */
	public String getCpybranchscode() {
		return this.cpybranchscode;
	}

	/**
	 * @param deleteflag the deleteflag to set.
	 */
	public void setDeleteflag(String deleteflag) {
		this.deleteflag=deleteflag;
	}
	/**
	 * @return Returns the deleteflag.
	 */
	public String getDeleteflag() {
		return this.deleteflag;
	}

	/**
	 * @param cpyclrcodett the cpyclrcodett to set.
	 */
	public void setCpyclrcodett(String cpyclrcodett) {
		this.cpyclrcodett=cpyclrcodett;
	}
	/**
	 * @return Returns the cpyclrcodett.
	 */
	public String getCpyclrcodett() {
		return this.cpyclrcodett;
	}

	/**
	 * @param versionnum the versionnum to set.
	 */
	public void setVersionnum(Double versionnum) {
		this.versionnum=versionnum;
	}
	/**
	 * @return Returns the versionnum.
	 */
	public Double getVersionnum() {
		return this.versionnum;
	}

	/**
	 * @param cpycustodybank the cpycustodybank to set.
	 */
	public void setCpycustodybank(String cpycustodybank) {
		this.cpycustodybank=cpycustodybank;
	}
	/**
	 * @return Returns the cpycustodybank.
	 */
	public String getCpycustodybank() {
		return this.cpycustodybank;
	}

	/**
	 * @param cpybanklocalcode the cpybanklocalcode to set.
	 */
	public void setCpybanklocalcode(String cpybanklocalcode) {
		this.cpybanklocalcode=cpybanklocalcode;
	}
	/**
	 * @return Returns the cpybanklocalcode.
	 */
	public String getCpybanklocalcode() {
		return this.cpybanklocalcode;
	}

	/**
	 * @param cpybenetype the cpybenetype to set.
	 */
	public void setCpybenetype(Double cpybenetype) {
		this.cpybenetype=cpybenetype;
	}
	/**
	 * @return Returns the cpybenetype.
	 */
	public Double getCpybenetype() {
		return this.cpybenetype;
	}

	/**
	 * @param cpystatus the cpystatus to set.
	 */
	public void setCpystatus(String cpystatus) {
		this.cpystatus=cpystatus;
	}
	/**
	 * @return Returns the cpystatus.
	 */
	public String getCpystatus() {
		return this.cpystatus;
	}

	/**
	 * @param cpypayeerefno the cpypayeerefno to set.
	 */
	public void setCpypayeerefno(String cpypayeerefno) {
		this.cpypayeerefno=cpypayeerefno;
	}
	/**
	 * @return Returns the cpypayeerefno.
	 */
	public String getCpypayeerefno() {
		return this.cpypayeerefno;
	}

	/**
	 * @param cpythresholdamt the cpythresholdamt to set.
	 */
	public void setCpythresholdamt(Double cpythresholdamt) {
		this.cpythresholdamt=cpythresholdamt;
	}
	/**
	 * @return Returns the cpythresholdamt.
	 */
	public Double getCpythresholdamt() {
		return this.cpythresholdamt;
	}

	/**
	 * @param cpypaymentscount the cpypaymentscount to set.
	 */
	public void setCpypaymentscount(Double cpypaymentscount) {
		this.cpypaymentscount=cpypaymentscount;
	}
	/**
	 * @return Returns the cpypaymentscount.
	 */
	public Double getCpypaymentscount() {
		return this.cpypaymentscount;
	}

	/**
	 * @param cpycurrency the cpycurrency to set.
	 */
	public void setCpycurrency(String cpycurrency) {
		this.cpycurrency=cpycurrency;
	}
	/**
	 * @return Returns the cpycurrency.
	 */
	public String getCpycurrency() {
		return this.cpycurrency;
	}

	/**
	 * @param cpybankswiftcode the cpybankswiftcode to set.
	 */
	public void setCpybankswiftcode(String cpybankswiftcode) {
		this.cpybankswiftcode=cpybankswiftcode;
	}
	/**
	 * @return Returns the cpybankswiftcode.
	 */
	public String getCpybankswiftcode() {
		return this.cpybankswiftcode;
	}

	/**
	 * @param cpyinterbankswiftcode the cpyinterbankswiftcode to set.
	 */
	public void setCpyinterbankswiftcode(String cpyinterbankswiftcode) {
		this.cpyinterbankswiftcode=cpyinterbankswiftcode;
	}
	/**
	 * @return Returns the cpyinterbankswiftcode.
	 */
	public String getCpyinterbankswiftcode() {
		return this.cpyinterbankswiftcode;
	}

	/**
	 * @param cpyinterbankaccnum the cpyinterbankaccnum to set.
	 */
	public void setCpyinterbankaccnum(String cpyinterbankaccnum) {
		this.cpyinterbankaccnum=cpyinterbankaccnum;
	}
	/**
	 * @return Returns the cpyinterbankaccnum.
	 */
	public String getCpyinterbankaccnum() {
		return this.cpyinterbankaccnum;
	}

	/**
	 * @param authorisedby the authorisedby to set.
	 */
	public void setAuthorisedby(String authorisedby) {
		this.authorisedby=authorisedby;
	}
	/**
	 * @return Returns the authorisedby.
	 */
	public String getAuthorisedby() {
		return this.authorisedby;
	}

	/**
	 * @param authoriseddate the authoriseddate to set.
	 */
	public void setAuthoriseddate(Date authoriseddate) {
		this.authoriseddate=authoriseddate;
	}
	/**
	 * @return Returns the authoriseddate.
	 */
	public Date getAuthoriseddate() {
		return this.authoriseddate;
	}

	/**
	 * @param bfsfilereferenceno the bfsfilereferenceno to set.
	 */
	public void setBfsfilereferenceno(String bfsfilereferenceno) {
		this.bfsfilereferenceno=bfsfilereferenceno;
	}
	/**
	 * @return Returns the bfsfilereferenceno.
	 */
	public String getBfsfilereferenceno() {
		return this.bfsfilereferenceno;
	}

	/**
	 * @param cpysortcode the cpysortcode to set.
	 */
	public void setCpysortcode(Double cpysortcode) {
		this.cpysortcode=cpysortcode;
	}
	/**
	 * @return Returns the cpysortcode.
	 */
	public Double getCpysortcode() {
		return this.cpysortcode;
	}

	/**
	 * @param life_user_id the life_user_id to set.
	 */
	public void setLife_user_id(String life_user_id) {
		this.life_user_id=life_user_id;
	}
	/**
	 * @return Returns the life_user_id.
	 */
	public String getLife_user_id() {
		return this.life_user_id;
	}

	/**
	 * @param ver_trials the ver_trials to set.
	 */
	public void setVer_trials(Double ver_trials) {
		this.ver_trials=ver_trials;
	}
	/**
	 * @return Returns the ver_trials.
	 */
	public Double getVer_trials() {
		return this.ver_trials;
	}
	
	public String getKey_client() {
		return key_client;
	}
	public void setKey_client(String key_client) {
		this.key_client = key_client;
	}
	public String getKey_link() {
		return key_link;
	}
	public void setKey_link(String key_link) {
		this.key_link = key_link;
	}
	public String getSign_up_mode() {
		return sign_up_mode;
	}
	public void setSign_up_mode(String sign_up_mode) {
		this.sign_up_mode = sign_up_mode;
	}
	public String getAccount_owner() {
		return account_owner;
	}
	public void setAccount_owner(String account_owner) {
		this.account_owner = account_owner;
	}
	public String getAccount_form() {
		return account_form;
	}
	public void setAccount_form(String account_form) {
		this.account_form = account_form;
	}
	public String getVoided_check() {
		return voided_check;
	}
	public void setVoided_check(String voided_check) {
		this.voided_check = voided_check;
	}
	/**
	 * Returns TransferObject data as a String.
	 *
	 */
	public String toString() {
		StringBuffer strB = new StringBuffer();
		strB.append ("=====================================================\r\n");
		strB.append ("Data for DS_PAYEE_REF \r\n");
		strB.append ("cpygrpid = " + cpygrpid + "\r\n");
		strB.append ("cpypayeeid = " + cpypayeeid + "\r\n");
		strB.append ("cpypayeename1 = " + cpypayeename1 + "\r\n");
		strB.append ("cpyadd1 = " + cpyadd1 + "\r\n");
		strB.append ("cpyadd2 = " + cpyadd2 + "\r\n");
		strB.append ("cpyadd3 = " + cpyadd3 + "\r\n");
		strB.append ("cpycustaccnum = " + cpycustaccnum + "\r\n");
		strB.append ("cpyaccnum = " + cpyaccnum + "\r\n");
		strB.append ("cpyacctype = " + cpyacctype + "\r\n");
		strB.append ("cpyfaxnum = " + cpyfaxnum + "\r\n");
		strB.append ("cpyctrycode = " + cpyctrycode + "\r\n");
		strB.append ("cpyintermediarybankcode = " + cpyintermediarybankcode + "\r\n");
		strB.append ("cpycitycode = " + cpycitycode + "\r\n");
		strB.append ("cpypayeename2 = " + cpypayeename2 + "\r\n");
		strB.append ("cpytaxid = " + cpytaxid + "\r\n");
		strB.append ("cpyadd4 = " + cpyadd4 + "\r\n");
		strB.append ("cpyboname1 = " + cpyboname1 + "\r\n");
		strB.append ("cpybradd1 = " + cpybradd1 + "\r\n");
		strB.append ("cpyboaddr1 = " + cpyboaddr1 + "\r\n");
		strB.append ("cpybradd2 = " + cpybradd2 + "\r\n");
		strB.append ("cpyboaddr2 = " + cpyboaddr2 + "\r\n");
		strB.append ("cpybradd3 = " + cpybradd3 + "\r\n");
		strB.append ("createdby = " + createdby + "\r\n");
		strB.append ("cpyboaddr3 = " + cpyboaddr3 + "\r\n");
		strB.append ("cpybankcode = " + cpybankcode + "\r\n");
		strB.append ("createddate = " + createddate + "\r\n");
		strB.append ("modifiedby = " + modifiedby + "\r\n");
		strB.append ("cpybranchcode = " + cpybranchcode + "\r\n");
		strB.append ("modifieddate = " + modifieddate + "\r\n");
		strB.append ("cpybranchscode = " + cpybranchscode + "\r\n");
		strB.append ("deleteflag = " + deleteflag + "\r\n");
		strB.append ("cpyclrcodett = " + cpyclrcodett + "\r\n");
		strB.append ("versionnum = " + versionnum + "\r\n");
		strB.append ("cpycustodybank = " + cpycustodybank + "\r\n");
		strB.append ("cpybanklocalcode = " + cpybanklocalcode + "\r\n");
		strB.append ("cpybenetype = " + cpybenetype + "\r\n");
		strB.append ("cpystatus = " + cpystatus + "\r\n");
		strB.append ("cpypayeerefno = " + cpypayeerefno + "\r\n");
		strB.append ("cpythresholdamt = " + cpythresholdamt + "\r\n");
		strB.append ("cpypaymentscount = " + cpypaymentscount + "\r\n");
		strB.append ("cpycurrency = " + cpycurrency + "\r\n");
		strB.append ("cpybankswiftcode = " + cpybankswiftcode + "\r\n");
		strB.append ("cpyinterbankswiftcode = " + cpyinterbankswiftcode + "\r\n");
		strB.append ("cpyinterbankaccnum = " + cpyinterbankaccnum + "\r\n");
		strB.append ("authorisedby = " + authorisedby + "\r\n");
		strB.append ("authoriseddate = " + authoriseddate + "\r\n");
		strB.append ("bfsfilereferenceno = " + bfsfilereferenceno + "\r\n");
		strB.append ("cpysortcode = " + cpysortcode + "\r\n");
		strB.append ("life_user_id = " + life_user_id + "\r\n");
		strB.append ("ver_trials = " + ver_trials + "\r\n");
		strB.append ("key_client = " + key_client + "\r\n");
		strB.append ("key_link = " + key_link + "\r\n");
		strB.append ("sign_up_mode = " + sign_up_mode + "\r\n");
		strB.append ("account_owner = " + account_owner + "\r\n");
		strB.append ("account_form = " + account_form + "\r\n");
		strB.append ("voided_check = " + voided_check + "\r\n");
		strB.append ("keyClientId = " + keyClientId + "\r\n");
		
		return strB.toString();
	}
	public String[] getKeyClientId() {
		return keyClientId;
	}
	public void setKeyClientId(String[] keyClientId) {
		this.keyClientId = keyClientId;
	}
}