package com.tcs.ebw.serverside.services;

import com.tcs.ebw.exception.EbwException;

public interface EbwBussinessErrorInterface {
	public Object addErrorDescString(Object retVal)throws EbwException;
	public Object addErrorDescVector(Object retVal)throws EbwException;
	public Object addErrorDescArraylist(Object retVal)throws EbwException;
	public Object addErrorDescHashMap(Object retVal)throws EbwException;
}
