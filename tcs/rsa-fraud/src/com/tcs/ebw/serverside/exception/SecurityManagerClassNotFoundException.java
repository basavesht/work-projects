package com.tcs.ebw.serverside.exception;

import java.util.HashMap;

import com.tcs.ebw.exception.EbwException;

public class SecurityManagerClassNotFoundException extends EbwException{

	/**
     * 
     */
    public SecurityManagerClassNotFoundException(HashMap configSystemInfo) {
    	super("SYS0018",configSystemInfo);
   	}
    
}
