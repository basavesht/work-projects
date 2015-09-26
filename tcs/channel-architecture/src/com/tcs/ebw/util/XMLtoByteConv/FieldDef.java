package com.tcs.ebw.util.XMLtoByteConv;

/**
 * @author 231259
 * @year 2008
 */

public class FieldDef implements java.io.Serializable, java.lang.Cloneable {
	private String Name;
	private int Size;
	private String Value;
	static final String fieldCobolFormats[] = { "String", "Integer",
			"9(14)V9(3)S", "9(14)V999S" };
	static final int COBOL_FORMAT_STRING = 1;
	static final int COBOL_FORMAT_INTEGER = 2;
	static final int COBOL_FORMAT_9_14_D_3S = 3;
	static final int COBOL_FORMAT_9_14_D_999S = 4;
	private java.lang.String AsMessageId;
	private long Index;
	private java.lang.String PrefillChar;
	private int CobolFormat;
	private java.lang.String Id;
	private int Offset = 0;
	private int PostDec = 0;

	/**
	 * FieldDef constructor comment.
	 */
	public FieldDef() {
		super();
	}

	/**
	 * FieldDef constructor comment.
	 */
	public FieldDef(long IndexParam, String nameParam, String IdParam,
			int sizeParam, String PrefillCharParam, int CobolFormatIdParam,
			String AsMessageParam, String ValueParam, int OffsetParam) {
		setIndex(IndexParam);
		setName(nameParam);
		setId(IdParam);
		setSize(sizeParam);
		setPrefillChar(PrefillCharParam);
		setCobolFormat(CobolFormatIdParam);
		setAsMessageId(AsMessageParam);
		setOffset(OffsetParam);
		/* Check size of Value does not exceed Size specified */
		if (ValueParam.trim().length() <= sizeParam) {
			setValue(ValueParam);
		} else {
			/* raise an error here */
		}
	}

	/**
	 * Constructor added for taking care of the post decimal pt. precision
	 * 
	 * @param IndexParam
	 * @param nameParam
	 * @param IdParam
	 * @param sizeParam
	 * @param PrefillCharParam
	 * @param CobolFormatIdParam
	 * @param AsMessageParam
	 * @param ValueParam
	 * @param OffsetParam
	 */

	public FieldDef(long IndexParam, String nameParam, String IdParam,
			int sizeParam, String PrefillCharParam, int CobolFormatIdParam,
			String AsMessageParam, String ValueParam, int OffsetParam,
			int postDec) {
		setIndex(IndexParam);
		setName(nameParam);
		setId(IdParam);
		setSize(sizeParam);
		setPrefillChar(PrefillCharParam);
		setCobolFormat(CobolFormatIdParam);
		setAsMessageId(AsMessageParam);
		setOffset(OffsetParam);
		setPostDec(postDec);
		/* Check size of Value does not exceed Size specified */
		if (ValueParam.trim().length() <= sizeParam) {
			setValue(ValueParam);
		} else {
			/* raise an error here */
		}
	}

	/**
	 * FieldDef constructor comment.
	 */
	public FieldDef(String nameParam, int sizeParam, int CobolFormatParam,
			String ValueParam) {
		setName(nameParam);
		setSize(sizeParam);
		setCobolFormat(CobolFormatParam);
		/* Check size of Value does not exceed Size specified */
		if (ValueParam.trim().length() <= sizeParam) {
			setValue(ValueParam);
		} else {
			/* raise an error here */
		}
	}

	/**
	 * FieldDef constructor comment.
	 */
	public FieldDef(String nameParam, int sizeParam, int cobolFormatParam,
			String ValueParam, String AsMessageParam) {
		setName(nameParam);
		setSize(sizeParam);
		setCobolFormat(cobolFormatParam);
		setAsMessageId(AsMessageParam);
		/* Check size of Value does not exceed Size specified */
		if (ValueParam.trim().length() <= sizeParam) {
			setValue(ValueParam);
		} else {
			/* raise an error here */
		}
	}

	/**
	 * Insert the method's description here. Creation date: (12/07/2000
	 * 10:09:08)
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getAsMessageId() {
		return AsMessageId;
	}

	/**
	 * Insert the method's description here. Creation date: (18/07/2000
	 * 11:07:20)
	 * 
	 * @return long
	 */
	public int getCobolFormat() {
		return CobolFormat;
	}

	/**
	 * Insert the method's description here. Creation date: (18/07/2000
	 * 13:07:34)
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getId() {
		return Id;
	}

	/**
	 * Insert the method's description here. Creation date: (18/07/2000
	 * 11:04:00)
	 * 
	 * @return long
	 */
	public long getIndex() {
		return Index;
	}

	/**
	 * This method was created in VisualAge.
	 * 
	 * @return java.lang.String
	 */
	public String getName() {
		return Name;
	}

	/**
	 * Insert the method's description here. Creation date: (04/08/2000
	 * 12:16:52)
	 * 
	 * @return int
	 */
	public int getOffset() {
		return Offset;
	}

	/**
	 * Insert the method's description here. Creation date: (18/07/2000
	 * 11:06:23)
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getPrefillChar() {
		return PrefillChar;
	}

	/**
	 * This method was created in VisualAge.
	 * 
	 * @return int
	 */
	public int getSize() {
		return Size;
	}

	/**
	 * This method was created in VisualAge.
	 * 
	 * @return int
	 */
	public String getValue() {
		return Value;
	}

	/**
	 * Method to retrieve post decimal precision
	 * 
	 * @return
	 */
	public int getPostDec() {
		return PostDec;
	}

	/**
	 * Insert the method's description here. Creation date: (12/07/2000
	 * 10:09:08)
	 * 
	 * @param newAsMessageId
	 *            java.lang.String
	 */
	public void setAsMessageId(java.lang.String newAsMessageId) {
		if (newAsMessageId == null) {
			newAsMessageId = "";
		}
		AsMessageId = newAsMessageId;
	}

	/**
	 * Insert the method's description here. Creation date: (18/07/2000
	 * 11:07:20)
	 * 
	 * @param newCobolFormat
	 *            long
	 */
	public void setCobolFormat(int newCobolFormat) {
		String tmp = Integer.toString(newCobolFormat);
		if (tmp == null) {
			// if (newCobolFormat.equals(null)) {
			newCobolFormat = 1; // string
		}
		CobolFormat = newCobolFormat;
	}

	/**
	 * Insert the method's description here. Creation date: (18/07/2000
	 * 13:07:34)
	 * 
	 * @param newId
	 *            java.lang.String
	 */
	public void setId(java.lang.String newId) {
		if (newId == null) {
			newId = "";
		}
		Id = newId;
	}

	/**
	 * Insert the method's description here. Creation date: (18/07/2000
	 * 11:04:00)
	 * 
	 * @param newIndex
	 *            long
	 */
	public void setIndex(long newIndex) {
		Index = newIndex;
	}

	/**
	 * This method was created in VisualAge.
	 * 
	 * @param newValue
	 *            java.lang.String
	 */
	public void setName(String newValue) {
		if (newValue == null) {
			newValue = "";
		}
		this.Name = newValue;
	}

	/**
	 * Set post decimal precision
	 * 
	 * @param newOffset
	 */
	public void setPostDec(int newPostDec) {
		PostDec = newPostDec;
	}

	/**
	 * Insert the method's description here. Creation date: (04/08/2000
	 * 12:16:52)
	 * 
	 * @param newOffset
	 *            int
	 */
	public void setOffset(int newOffset) {
		Offset = newOffset;
	}

	/**
	 * Insert the method's description here. Creation date: (18/07/2000
	 * 11:06:23)
	 * 
	 * @param newPrefillChar
	 *            java.lang.String
	 */
	public void setPrefillChar(java.lang.String newPrefillChar) {
		if (newPrefillChar == null) {
			newPrefillChar = "_";
		}
		PrefillChar = newPrefillChar;
	}

	/**
	 * This method was created in VisualAge.
	 * 
	 * @param newValue
	 *            int
	 */
	public void setSize(int newValue) {
		this.Size = newValue;
	}

	/**
	 * This method was created in VisualAge.
	 * 
	 * @param newValue
	 *            java.lang.String
	 */
	public void setValue(String newValue) {
		if (newValue == null) {
			newValue = "";
		}
		if (newValue.trim().length() > this.Size) {
			newValue = newValue.substring(0, this.Size);
		}
		this.Value = newValue;
	}

	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("Index=");
		sb.append(getIndex());
		sb.append(",Name=");
		sb.append(getName());
		sb.append(",Size=");
		sb.append(getSize());
		sb.append(",Offset=");
		sb.append(getOffset());
		sb.append(",Value=");
		sb.append(getValue());
		sb.append(";");
		return sb.toString();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#clone()
	 */
	protected Object clone() throws CloneNotSupportedException {
		FieldDef fd = (FieldDef) super.clone();
		fd.setValue("");
		return fd;
	}
}
