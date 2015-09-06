
package com.bosch.tmp.integration.util;

/**
 * Enumeration for the type of results stored in the DB.
 * SESSION type is used for subjective results, SESSION_VITAL_SIGN type is used for vitals within a session and
 * VITAL_SIGN type is used for any other adhoc vitals.
 * 
 * @author gtk1pal
 */
public enum ResultTypeEnum {
    SESSION("SESSION"),
    SESSION_VITAL_SIGN("SESSION_VITAL_SIGN"),
    VITAL_SIGN("VITAL_SIGN");

    private String id;

	ResultTypeEnum(final String id)
	{
		this.id = id;
	}

	public String toString()
	{
		return id;
	}
};
