/*
 * Created on Jan 22, 2007
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.tcs.ebw.serverside.exception;

import java.util.Hashtable;

import com.tcs.ebw.common.util.PropertyFileReader;
import com.tcs.ebw.exception.EbwException;

/**
 * @author 193350
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class EBWLoginCtxCreationException extends EbwException {

	public EBWLoginCtxCreationException(Hashtable props){
		super("SYS0003",props);
	}
}
