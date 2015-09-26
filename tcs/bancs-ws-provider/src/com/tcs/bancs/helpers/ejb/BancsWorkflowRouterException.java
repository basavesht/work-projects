package com.tcs.bancs.helpers.ejb;

public class BancsWorkflowRouterException extends Exception {

	private static final long serialVersionUID = -3383897706008005267L;

	public BancsWorkflowRouterException(String string) {
		super(string);
	}

	public BancsWorkflowRouterException(String message, Exception e) {
		super(message,e);
	}

	public BancsWorkflowRouterException(Exception e) {
		super(e);
	}
}
