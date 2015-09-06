package com.tcs.ebw.payments.transferobject;

import java.util.ArrayList;

/**
 * @author : 224703
 * **************   Revision History ************************
 * Modified-By |  Modified-Date |  Project-Phase  | Changes
 *
 * **********************************************************
 */
public class MerlinOutResponse implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 412142082314294393L;
	private String accountNumber = null;
	private String keyAccNumber = null;
	private String catTier = null;
	private String acntCategory = null;
	private ArrayList<AddressDtls> addressDtls = null;
	private ArrayList<ClientDtls> clientDtls = null;
	private ArrayList<PlatingAddressDtls> platingAddressDtls = null;

	public class AddressDtls implements java.io.Serializable 
	{
		/**
		 * 
		 */
		private static final long serialVersionUID = 6442845350535882525L;
		private String line1 = null;
		private String line2 = null;
		private String line3 = null;
		private String city  = null;
		private String state = null;
		private String zip   = null;
		private String foriegnStatus   = null;
		private String province   = null;
		private String postal   = null;
		private String category   = null;

		public String getCategory() {
			return category;
		}
		public void setCategory(String category) {
			this.category = category;
		}
		public String getLine1() {
			return line1;
		}
		public void setLine1(String line1) {
			this.line1 = line1;
		}
		public String getLine2() {
			return line2;
		}
		public void setLine2(String line2) {
			this.line2 = line2;
		}
		public String getLine3() {
			return line3;
		}
		public void setLine3(String line3) {
			this.line3 = line3;
		}
		public String getCity() {
			return city;
		}
		public void setCity(String city) {
			this.city = city;
		}
		public String getState() {
			return state;
		}
		public void setState(String state) {
			this.state = state;
		}
		public String getZip() {
			return zip;
		}
		public void setZip(String zip) {
			this.zip = zip;
		}
		public String getForiegnStatus() {
			return foriegnStatus;
		}
		public void setForiegnStatus(String foriegnStatus) {
			this.foriegnStatus = foriegnStatus;
		}
		public String getProvince() {
			return province;
		}
		public void setProvince(String province) {
			this.province = province;
		}
		public String getPostal() {
			return postal;
		}
		public void setPostal(String postal) {
			this.postal = postal;
		}
		/**
		 * Constructs a <code>String</code> with all attributes
		 * in name = value format.
		 *
		 * @return a <code>String</code> representation 
		 * of this object.
		 */
		public String toString()
		{
			final String TAB = "\r\n";

			String retValue = "";

			retValue = "AddressDtls ( "
				+ super.toString() + TAB
				+ "line1 = " + this.line1 + TAB
				+ "line2 = " + this.line2 + TAB
				+ "line3 = " + this.line3 + TAB
				+ "city = " + this.city + TAB
				+ "state = " + this.state + TAB
				+ "zip = " + this.zip + TAB
				+ "foriegnStatus = " + this.foriegnStatus + TAB
				+ "province = " + this.province + TAB
				+ "postal = " + this.postal + TAB
				+ "category = " + this.category + TAB
				+ " )";

			return retValue;
		}
	}

	public class ClientDtls implements java.io.Serializable
	{
		/**
		 * 
		 */
		private static final long serialVersionUID = 7285644027484921060L;
		private String relationship   = null;
		private String residentCountry   = null;

		public String getRelationship() {
			return relationship;
		}
		public void setRelationship(String relationship) {
			this.relationship = relationship;
		}
		public String getResidentCountry() {
			return residentCountry;
		}
		public void setResidentCountry(String residentCountry) {
			this.residentCountry = residentCountry;
		}
		/**
		 * Constructs a <code>String</code> with all attributes
		 * in name = value format.
		 *
		 * @return a <code>String</code> representation 
		 * of this object.
		 */
		public String toString()
		{
			final String TAB = "\r\n";

			String retValue = "";

			retValue = "ClientDtls ( "
				+ super.toString() + TAB
				+ "relationship = " + this.relationship + TAB
				+ "residentCountry = " + this.residentCountry + TAB
				+ " )";

			return retValue;
		}

	}

	public class PlatingAddressDtls implements java.io.Serializable
	{
		/**
		 * 
		 */
		private static final long serialVersionUID = 955150452157028149L;
		private String address_line_index   = null;
		private String address_line	   = null;
		private String address_category	   = null;

		public String getAddress_line_index() {
			return address_line_index;
		}
		public void setAddress_line_index(String address_line_index) {
			this.address_line_index = address_line_index;
		}
		public String getAddress_line() {
			return address_line;
		}
		public void setAddress_line(String address_line) {
			this.address_line = address_line;
		}
		public String getAddress_category() {
			return address_category;
		}
		public void setAddress_category(String address_category) {
			this.address_category = address_category;
		}
		/**
		 * Constructs a <code>String</code> with all attributes
		 * in name = value format.
		 *
		 * @return a <code>String</code> representation 
		 * of this object.
		 */
		public String toString()
		{
			final String TAB = "\r\n";

			String retValue = "";

			retValue = "PlatingAddressDtls ( "
				+ super.toString() + TAB
				+ "address_line_index = " + this.address_line_index + TAB
				+ "address_line = " + this.address_line + TAB
				+ "address_category = " + this.address_category + TAB
				+ " )";

			return retValue;
		}

	}

	public String getAccountNumber() {
		return accountNumber;
	}
	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}
	public String getKeyAccNumber() {
		return keyAccNumber;
	}
	public void setKeyAccNumber(String keyAccNumber) {
		this.keyAccNumber = keyAccNumber;
	}
	public String getCatTier() {
		return catTier;
	}
	public void setCatTier(String catTier) {
		this.catTier = catTier;
	}
	public ArrayList<AddressDtls> getAddressDtls() {
		return addressDtls;
	}
	public void setAddressDtls(ArrayList<AddressDtls> addressDtls) {
		this.addressDtls = addressDtls;
	}
	public ArrayList<ClientDtls> getClientDtls() {
		return clientDtls;
	}
	public void setClientDtls(ArrayList<ClientDtls> clientDtls) {
		this.clientDtls = clientDtls;
	}
	public ArrayList<PlatingAddressDtls> getPlatingAddressDtls() {
		return platingAddressDtls;
	}
	public void setPlatingAddressDtls(
			ArrayList<PlatingAddressDtls> platingAddressDtls) {
		this.platingAddressDtls = platingAddressDtls;
	}
	public String getAcntCategory() {
		return acntCategory;
	}
	public void setAcntCategory(String acntCategory) {
		this.acntCategory = acntCategory;
	}

	/**
	 * Constructs a <code>String</code> with all attributes
	 * in name = value format.
	 *
	 * @return a <code>String</code> representation 
	 * of this object.
	 */
	public String toString()
	{
		final String TAB = "\r\n";

		String retValue = "";

		retValue = "MerlinOutResponse ( "
			+ super.toString() + TAB
			+ "accountNumber = " + this.accountNumber + TAB
			+ "keyAccNumber = " + this.keyAccNumber + TAB
			+ "catTier = " + this.catTier + TAB
			+ "acntCategory = " + this.acntCategory + TAB
			+ "addressDtls = " + this.addressDtls + TAB
			+ "clientDtls = " + this.clientDtls + TAB
			+ "platingAddressDtls = " + this.platingAddressDtls + TAB
			+ " )";

		return retValue;
	}
}