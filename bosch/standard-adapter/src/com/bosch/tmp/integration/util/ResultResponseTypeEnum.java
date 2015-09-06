package com.bosch.tmp.integration.util;

/**
 * Enum declaration for all the possible result response type ...
 * @author BEA2KOR
 *
 */
public enum ResultResponseTypeEnum
{
	SUBJECTIVE("SUBJ"),
	OBJECTIVE("OBS"),
	NOTES("NTE"),
	SUBJECTIVE_NOTES("SUBJNTE"),
	OBJECTIVE_NOTES("OBSNTE");

	private String responseType;

	ResultResponseTypeEnum(final String responseType){
		this.responseType = responseType;
	}

	public String responseType(){
		return responseType;
	}

	public String toString(){
		return responseType;
	}

	public static ResultResponseTypeEnum findByResponseValue(String abbr){
	    for(ResultResponseTypeEnum v : values()){
	        if( v.responseType().equals(abbr)){
	            return v;
	        }
	    }
	    return null;
	}
}
