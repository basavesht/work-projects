package com.bosch.tmp.integration.util;

/**
 * Enumeration for HL7 Message Acknowledgement Type Code.
 * 
 * @author gtk1pal
 */
public enum AckResponseCodeEnum {
	CA("CA"),
	CE("CE"),
	CR("CR"),
    AA("AA"),
    AR("AR"),
    AE("AE");

	private String id;

	AckResponseCodeEnum(final String id)
	{
		this.id = id;
	}

	public String toString()
	{
		return id;
	}
}
