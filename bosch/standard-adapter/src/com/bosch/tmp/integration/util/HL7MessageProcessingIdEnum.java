package com.bosch.tmp.integration.util;

/**
 * Enumeration for HL7 Message Processing ID.
 *
 * @author gtk1pal
 */
public enum HL7MessageProcessingIdEnum {
	D("Debugging"),
	P("Production"),
	T("Training");

	private String id;

	HL7MessageProcessingIdEnum(final String id)
	{
		this.id = id;
	}

	public String toString()
	{
		return id;
	}
}
