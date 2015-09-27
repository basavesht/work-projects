package com.tcs.ebw.serverside.exception;

import java.util.HashMap;

import com.tcs.ebw.exception.EbwException;

public class SecurityManagerIllegalAccessException extends EbwException{

    /**
     * 
     */
    public SecurityManagerIllegalAccessException(HashMap configSystemInfo) {
    	super("SYS0019",configSystemInfo);
   	}

}
