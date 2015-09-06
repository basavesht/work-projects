/*
 * Copyright Tata Consultancy Services. All rights reserved.
 */
package com.tcs.ebw.serverside.jaas.principal;

import java.security.Principal;

/**
 * 
 * This Class provides getter-setter methods for various information that can be used for
 * evaluating the role of the user.
 *
 */
public class RolePrincipal implements Principal, java.io.Serializable{
	 	private String usrusername;
	 	private String usrgrpid;
	 	private String usruserid;
	 	private String usrchnl;
        
	 	private String usrapplsys;
	 	private String usrsigntype;
	 	private String usrbrcode;
	 	private String usrremarks;
	 	private String usremail;
	 	private String usrmobileno;
	 	private String usrpagerno;
	 	private String usrfaxno;
	 	private String createdby;
	 	private String createddate;
	 	private String modifiedby;
	 	private String modifieddate;
	 	private String deleteflag;
	 	private String versionnum;
	 	private String usrstatus;
	 	private String usrapprovedby;
	 	private String usrtimestamp;
	 	private String usrsecuritymedia;
	 	private String intstatus;
	 	private String usrissuerdn;
	 	private String usrsubjectdn;
	 	private String usrcheckerdn;
	 	private String usrmakerdn;
	 	private String usrdnstatus;
	 	private String firstauthorisedby;
	 	private String firstauthoriseddate;
	 	private String usrroleid;
	 	private String retlcustid;
	 	private String usertype;
	 	private String custid;
	 	private String usertitle;
	 	private String middlename;
	 	private String lastname;
	 	private String basebank;
	 	private String basebranch;
	 	private String roletype;
	 	private String validfrom;
	 	private String validto;
	 	private String dateofbirth;
	 	private String taxid;
	 	private String workphno;
	 	private String mobileno;
	 	private String homephno;
	 	private String faxno;
	 	private String email;
	 	private String resstreet;
	 	private String ressuburb;
	 	private String rsestate;
	 	private String respostcode;
	 	private String pastreet;
	 	private String pasuburb;
	 	private String pastate;
	 	private String papostcode;
	 	private String workphcode;
	 	private String homephcode;
	 	private String faxcode;
	 	private String authorisedby;
	 	private String authoriseddate;
	 	private String validatedby;
	 	private String validateddate;
	 	
	 	public String getUsrusername() {
            return usrusername;
        }
        public void setUsrusername(String usrusername) {
            this.usrusername = usrusername;
        }
	 	
        public String getAuthorisedby() {
            return authorisedby;
        }
        public void setAuthorisedby(String authorisedby) {
            this.authorisedby = authorisedby;
        }
        public String getAuthoriseddate() {
            return authoriseddate;
        }
        public void setAuthoriseddate(String authoriseddate) {
            this.authoriseddate = authoriseddate;
        }
        public String getBasebank() {
            return basebank;
        }
        public void setBasebank(String basebank) {
            this.basebank = basebank;
        }
        public String getBasebranch() {
            return basebranch;
        }
        public void setBasebranch(String basebranch) {
            this.basebranch = basebranch;
        }
        public String getCreatedby() {
            return createdby;
        }
        public void setCreatedby(String createdby) {
            this.createdby = createdby;
        }
        public String getCreateddate() {
            return createddate;
        }
        public void setCreateddate(String createddate) {
            this.createddate = createddate;
        }
        public String getCustid() {
            return custid;
        }
        public void setCustid(String custid) {
            this.custid = custid;
        }
        public String getDateofbirth() {
            return dateofbirth;
        }
        public void setDateofbirth(String dateofbirth) {
            this.dateofbirth = dateofbirth;
        }
        public String getDeleteflag() {
            return deleteflag;
        }
        public void setDeleteflag(String deleteflag) {
            this.deleteflag = deleteflag;
        }
        public String getEmail() {
            return email;
        }
        public void setEmail(String email) {
            this.email = email;
        }
        public String getFaxcode() {
            return faxcode;
        }
        public void setFaxcode(String faxcode) {
            this.faxcode = faxcode;
        }
        public String getFaxno() {
            return faxno;
        }
        public void setFaxno(String faxno) {
            this.faxno = faxno;
        }
        public String getFirstauthorisedby() {
            return firstauthorisedby;
        }
        public void setFirstauthorisedby(String firstauthorisedby) {
            this.firstauthorisedby = firstauthorisedby;
        }
        public String getFirstauthoriseddate() {
            return firstauthoriseddate;
        }
        public void setFirstauthoriseddate(String firstauthoriseddate) {
            this.firstauthoriseddate = firstauthoriseddate;
        }
        public String getHomephcode() {
            return homephcode;
        }
        public void setHomephcode(String homephcode) {
            this.homephcode = homephcode;
        }
        public String getHomephno() {
            return homephno;
        }
        public void setHomephno(String homephno) {
            this.homephno = homephno;
        }
        public String getIntstatus() {
            return intstatus;
        }
        public void setIntstatus(String intstatus) {
            this.intstatus = intstatus;
        }
        public String getLastname() {
            return lastname;
        }
        public void setLastname(String lastname) {
            this.lastname = lastname;
        }
        public String getMiddlename() {
            return middlename;
        }
        public void setMiddlename(String middlename) {
            this.middlename = middlename;
        }
        public String getMobileno() {
            return mobileno;
        }
        public void setMobileno(String mobileno) {
            this.mobileno = mobileno;
        }
        public String getModifiedby() {
            return modifiedby;
        }
        public void setModifiedby(String modifiedby) {
            this.modifiedby = modifiedby;
        }
        public String getModifieddate() {
            return modifieddate;
        }
        public void setModifieddate(String modifieddate) {
            this.modifieddate = modifieddate;
        }
        public String getPapostcode() {
            return papostcode;
        }
        public void setPapostcode(String papostcode) {
            this.papostcode = papostcode;
        }
        public String getPastate() {
            return pastate;
        }
        public void setPastate(String pastate) {
            this.pastate = pastate;
        }
        public String getPastreet() {
            return pastreet;
        }
        public void setPastreet(String pastreet) {
            this.pastreet = pastreet;
        }
        public String getPasuburb() {
            return pasuburb;
        }
        public void setPasuburb(String pasuburb) {
            this.pasuburb = pasuburb;
        }
        public String getRespostcode() {
            return respostcode;
        }
        public void setRespostcode(String respostcode) {
            this.respostcode = respostcode;
        }
        public String getResstreet() {
            return resstreet;
        }
        public void setResstreet(String resstreet) {
            this.resstreet = resstreet;
        }
        public String getRessuburb() {
            return ressuburb;
        }
        public void setRessuburb(String ressuburb) {
            this.ressuburb = ressuburb;
        }
        public String getRetlcustid() {
            return retlcustid;
        }
        public void setRetlcustid(String retlcustid) {
            this.retlcustid = retlcustid;
        }
        public String getRoletype() {
            return roletype;
        }
        public void setRoletype(String roletype) {
            this.roletype = roletype;
        }
        public String getRsestate() {
            return rsestate;
        }
        public void setRsestate(String rsestate) {
            this.rsestate = rsestate;
        }
        public String getTaxid() {
            return taxid;
        }
        public void setTaxid(String taxid) {
            this.taxid = taxid;
        }
        public String getUsertitle() {
            return usertitle;
        }
        public void setUsertitle(String usertitle) {
            this.usertitle = usertitle;
        }
        public String getUsertype() {
            return usertype;
        }
        public void setUsertype(String usertype) {
            this.usertype = usertype;
        }
        public String getUsrapplsys() {
            return usrapplsys;
        }
        public void setUsrapplsys(String usrapplsys) {
            this.usrapplsys = usrapplsys;
        }
        public String getUsrapprovedby() {
            return usrapprovedby;
        }
        public void setUsrapprovedby(String usrapprovedby) {
            this.usrapprovedby = usrapprovedby;
        }
        public String getUsrbrcode() {
            return usrbrcode;
        }
        public void setUsrbrcode(String usrbrcode) {
            this.usrbrcode = usrbrcode;
        }
        public String getUsrcheckerdn() {
            return usrcheckerdn;
        }
        public void setUsrcheckerdn(String usrcheckerdn) {
            this.usrcheckerdn = usrcheckerdn;
        }
        public String getUsrchnl() {
            return usrchnl;
        }
        public void setUsrchnl(String usrchnl) {
            this.usrchnl = usrchnl;
        }
        public String getUsrdnstatus() {
            return usrdnstatus;
        }
        public void setUsrdnstatus(String usrdnstatus) {
            this.usrdnstatus = usrdnstatus;
        }
        public String getUsremail() {
            return usremail;
        }
        public void setUsremail(String usremail) {
            this.usremail = usremail;
        }
        public String getUsrfaxno() {
            return usrfaxno;
        }
        public void setUsrfaxno(String usrfaxno) {
            this.usrfaxno = usrfaxno;
        }
        public String getUsrgrpid() {
            return usrgrpid;
        }
        public void setUsrgrpid(String usrgrpid) {
            this.usrgrpid = usrgrpid;
        }
        public String getUsrissuerdn() {
            return usrissuerdn;
        }
        public void setUsrissuerdn(String usrissuerdn) {
            this.usrissuerdn = usrissuerdn;
        }
        public String getUsrmakerdn() {
            return usrmakerdn;
        }
        public void setUsrmakerdn(String usrmakerdn) {
            this.usrmakerdn = usrmakerdn;
        }
        public String getUsrmobileno() {
            return usrmobileno;
        }
        public void setUsrmobileno(String usrmobileno) {
            this.usrmobileno = usrmobileno;
        }
        public String getUsrpagerno() {
            return usrpagerno;
        }
        public void setUsrpagerno(String usrpagerno) {
            this.usrpagerno = usrpagerno;
        }
        public String getUsrremarks() {
            return usrremarks;
        }
        public void setUsrremarks(String usrremarks) {
            this.usrremarks = usrremarks;
        }
        public String getUsrroleid() {
            return usrroleid;
        }
        public void setUsrroleid(String usrroleid) {
            this.usrroleid = usrroleid;
        }
        public String getUsrsecuritymedia() {
            return usrsecuritymedia;
        }
        public void setUsrsecuritymedia(String usrsecuritymedia) {
            this.usrsecuritymedia = usrsecuritymedia;
        }
        public String getUsrsigntype() {
            return usrsigntype;
        }
        public void setUsrsigntype(String usrsigntype) {
            this.usrsigntype = usrsigntype;
        }
        public String getUsrstatus() {
            return usrstatus;
        }
        public void setUsrstatus(String usrstatus) {
            this.usrstatus = usrstatus;
        }
        public String getUsrsubjectdn() {
            return usrsubjectdn;
        }
        public void setUsrsubjectdn(String usrsubjectdn) {
            this.usrsubjectdn = usrsubjectdn;
        }
        public String getUsrtimestamp() {
            return usrtimestamp;
        }
        public void setUsrtimestamp(String usrtimestamp) {
            this.usrtimestamp = usrtimestamp;
        }
        public String getUsruserid() {
            return usruserid;
        }
        public void setUsruserid(String usruserid) {
            this.usruserid = usruserid;
        }
        public String getValidatedby() {
            return validatedby;
        }
        public void setValidatedby(String validatedby) {
            this.validatedby = validatedby;
        }
        public String getValidateddate() {
            return validateddate;
        }
        public void setValidateddate(String validateddate) {
            this.validateddate = validateddate;
        }
        public String getValidfrom() {
            return validfrom;
        }
        public void setValidfrom(String validfrom) {
            this.validfrom = validfrom;
        }
        public String getValidto() {
            return validto;
        }
        public void setValidto(String validto) {
            this.validto = validto;
        }
        public String getVersionnum() {
            return versionnum;
        }
        public void setVersionnum(String versionnum) {
            this.versionnum = versionnum;
        }
        public String getWorkphcode() {
            return workphcode;
        }
        public void setWorkphcode(String workphcode) {
            this.workphcode = workphcode;
        }
        public String getWorkphno() {
            return workphno;
        }
        public void setWorkphno(String workphno) {
            this.workphno = workphno;
        }
	 	public RolePrincipal(String name) {
	 		if (name == null)
	 		    throw new NullPointerException("illegal null input");

	 		this.usrusername = name;
	 	}
	 	 
		/**
		 * @return Returns the name.
		 */
		public String getName() {
			return usrroleid;
		}
		/**
		 * @param name The name to set.
		 */
		public void setName(String name) {
			this.usrusername = usrroleid;
		}
		
}
