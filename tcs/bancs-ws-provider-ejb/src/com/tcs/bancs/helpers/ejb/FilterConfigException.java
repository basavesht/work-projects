package com.tcs.bancs.helpers.ejb;

public class FilterConfigException extends Exception 
{
	private static final long serialVersionUID = -8173860425262404989L;

	public FilterConfigException(String msg, Throwable e) {
		super(msg,e);
	}
	public FilterConfigException(String msg) {
		super(msg);
	}

}
