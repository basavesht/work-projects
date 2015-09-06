/**
 * 
 */
package com.tcs.ebw.serverside.exception;

import com.tcs.ebw.exception.EbwException;

/**
 * @author 197172
 *
 */
public class PrepopulatorDataNotFoundException extends EbwException {

	/**
	 * @param errorCode
	 */
	public PrepopulatorDataNotFoundException(String errorCode) {
		super("SYS0015",errorCode);	
	}	

}
