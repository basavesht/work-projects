package com.tcs.Payments.EAITO;

/**
 * @author : 224703
 * **************   Revision History ************************
 * Modified-By |  Modified-Date |  Project-Phase  | Changes
 *
 * **********************************************************
 */
public class MS_ACNT_PLATING_OUT implements java.io.Serializable 
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 2811019755808048567L;

	private String ACCOUNT_NUMBER = null;
	private String FA = null;
	private String OFFICE = null;
	private ACCOUNT_PLATING_INFO[] ACCOUNT_PLATING_INFO = new ACCOUNT_PLATING_INFO[20];
	private AUTHORISED_ENTITIES[] AUTHORISED_ENTITIES = new AUTHORISED_ENTITIES[20];
	private PAYEE_INFO[] PAYEE_INFO = new PAYEE_INFO[20];

	/**
	 * @return the aCCOUNT_NUMBER
	 */
	public String getACCOUNT_NUMBER() {
		return ACCOUNT_NUMBER;
	}
	/**
	 * @param account_number the aCCOUNT_NUMBER to set
	 */
	public void setACCOUNT_NUMBER(String account_number) {
		ACCOUNT_NUMBER = account_number;
	}
	/**
	 * @return the fA
	 */
	public String getFA() {
		return FA;
	}
	/**
	 * @param fa the fA to set
	 */
	public void setFA(String fa) {
		FA = fa;
	}
	/**
	 * @return the oFFICE
	 */
	public String getOFFICE() {
		return OFFICE;
	}
	/**
	 * @param office the oFFICE to set
	 */
	public void setOFFICE(String office) {
		OFFICE = office;
	}


	/**
	 * Payee name mapping class..
	 * @author 224703
	 *
	 */
	public class ACCOUNT_PLATING_INFO implements java.io.Serializable 
	{
		/**
		 * 
		 */
		private static final long serialVersionUID = -8177925818383853902L;

		private String LABEL = " ";
		private String NAME = null;
		private String VALUE = " ";
		private String SEQUENCE = null;

		/**
		 * @return the lABEL
		 */
		public String getLABEL() {
			return LABEL;
		}
		/**
		 * @param label the lABEL to set
		 */
		public void setLABEL(String label) {
			LABEL = label;
		}
		/**
		 * @return the nAME
		 */
		public String getNAME() {
			return NAME;
		}
		/**
		 * @param name the nAME to set
		 */
		public void setNAME(String name) {
			NAME = name;
		}
		/**
		 * @return the vALUE
		 */
		public String getVALUE() {
			return VALUE;
		}
		/**
		 * @param value the vALUE to set
		 */
		public void setVALUE(String value) {
			VALUE = value;
		}
		/**
		 * @return the sEQUENCE
		 */
		public String getSEQUENCE() {
			return SEQUENCE;
		}
		/**
		 * @param sequence the sEQUENCE to set
		 */
		public void setSEQUENCE(String sequence) {
			SEQUENCE = sequence;
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

			retValue = "ACCOUNT_PLATING_INFO ( "
				+ super.toString() + TAB
				+ "LABEL = " + this.LABEL + TAB
				+ "NAME = " + this.NAME + TAB
				+ "VALUE = " + this.VALUE + TAB
				+ "SEQUENCE = " + this.SEQUENCE + TAB
				+ " )";

			return retValue;
		}
	}

	/**
	 * Authorised entities object mapping class..
	 * @author 224703
	 *
	 */
	public class AUTHORISED_ENTITIES implements java.io.Serializable 
	{
		/**
		 * 
		 */
		private static final long serialVersionUID = -991574422560231296L;

		private String HASHED_UNIQUE_ID =null;
		private String MASKED_UNIQUE_ID =null;
		private String AUTH_ENTITY_NAME =null;

		/**
		 * @return the hASHED_UNIQUE_ID
		 */
		public String getHASHED_UNIQUE_ID() {
			return HASHED_UNIQUE_ID;
		}
		/**
		 * @param hashed_unique_id the hASHED_UNIQUE_ID to set
		 */
		public void setHASHED_UNIQUE_ID(String hashed_unique_id) {
			HASHED_UNIQUE_ID = hashed_unique_id;
		}
		/**
		 * @return the mASKED_UNIQUE_ID
		 */
		public String getMASKED_UNIQUE_ID() {
			return MASKED_UNIQUE_ID;
		}
		/**
		 * @param masked_unique_id the mASKED_UNIQUE_ID to set
		 */
		public void setMASKED_UNIQUE_ID(String masked_unique_id) {
			MASKED_UNIQUE_ID = masked_unique_id;
		}
		/**
		 * @return the aUTH_ENTITY_NAME
		 */
		public String getAUTH_ENTITY_NAME() {
			return AUTH_ENTITY_NAME;
		}
		/**
		 * @param auth_entity_name the aUTH_ENTITY_NAME to set
		 */
		public void setAUTH_ENTITY_NAME(String auth_entity_name) {
			AUTH_ENTITY_NAME = auth_entity_name;
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

			retValue = "AUTHORISED_ENTITIES ( "
				+ super.toString() + TAB
				+ "HASHED_UNIQUE_ID = " + this.HASHED_UNIQUE_ID + TAB
				+ "MASKED_UNIQUE_ID = " + this.MASKED_UNIQUE_ID + TAB
				+ "AUTH_ENTITY_NAME = " + this.AUTH_ENTITY_NAME + TAB
				+ " )";

			return retValue;
		}
	}

	/**
	 * Payee name mapping class..
	 * @author 224703
	 *
	 */
	public class PAYEE_INFO implements java.io.Serializable 
	{
		/**
		 * 
		 */
		private static final long serialVersionUID = 7147164358381394451L;

		private String PAYEE = null;
		/**
		 * @return the pAYEE
		 */
		public String getPAYEE() {
			return PAYEE;
		}

		/**
		 * @param payee the pAYEE to set
		 */
		public void setPAYEE(String payee) {
			PAYEE = payee;
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

			retValue = "PAYEE_INFO ( "
				+ super.toString() + TAB
				+ "PAYEE = " + this.PAYEE + TAB
				+ " )";

			return retValue;
		}
	}

	/**
	 * @return the aCCOUNT_PLATING_INFO
	 */
	public ACCOUNT_PLATING_INFO[] getACCOUNT_PLATING_INFO() {
		return ACCOUNT_PLATING_INFO;
	}
	/**
	 * @param account_plating_info the aCCOUNT_PLATING_INFO to set
	 */
	public void setACCOUNT_PLATING_INFO(ACCOUNT_PLATING_INFO[] account_plating_info) {
		ACCOUNT_PLATING_INFO = account_plating_info;
	}
	/**
	 * @return the aUTHORISED_ENTITIES
	 */
	public AUTHORISED_ENTITIES[] getAUTHORISED_ENTITIES() {
		return AUTHORISED_ENTITIES;
	}
	/**
	 * @param authorised_entities the aUTHORISED_ENTITIES to set
	 */
	public void setAUTHORISED_ENTITIES(AUTHORISED_ENTITIES[] authorised_entities) {
		AUTHORISED_ENTITIES = authorised_entities;
	}
	/**
	 * @return the pAYEE_INFO
	 */
	public PAYEE_INFO[] getPAYEE_INFO() {
		return PAYEE_INFO;
	}
	/**
	 * @param payee_info the pAYEE_INFO to set
	 */
	public void setPAYEE_INFO(PAYEE_INFO[] payee_info) {
		PAYEE_INFO = payee_info;
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

		retValue = "MS_ACNT_PLATING_OUT ( "
			+ super.toString() + TAB
			+ "ACCOUNT_NUMBER = " + this.ACCOUNT_NUMBER + TAB
			+ "FA = " + this.FA + TAB
			+ "OFFICE = " + this.OFFICE + TAB
			+ "ACCOUNT_PLATING_INFO = " + this.ACCOUNT_PLATING_INFO + TAB
			+ "AUTHORISED_ENTITIES = " + this.AUTHORISED_ENTITIES + TAB
			+ "PAYEE_INFO = " + this.PAYEE_INFO + TAB
			+ " )";

		return retValue;
	}


}
