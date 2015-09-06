package com.bosch.tmp.integration.util;

/**
 * Enumeration for HL7 Message Acknowledgement Type Code.
 * 
 * @author gtk1pal
 */
public enum AckRequestCodeEnum {
	AL("AL"),
	NE("NE"),
	SU("SU"),
    ER("ER");

	private String id;

	AckRequestCodeEnum(final String id)
	{
		this.id = id;
	}

	public String toString()
	{
		return id;
	}
}
