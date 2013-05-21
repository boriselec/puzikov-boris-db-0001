package com.see.common.exception;

@SuppressWarnings("serial")
public class CancelOrderException extends Exception {

	public CancelOrderException() {
		super();
	}

	public CancelOrderException(String arg0, Throwable arg1, boolean arg2,
			boolean arg3) {
		super(arg0, arg1, arg2, arg3);
	}

	public CancelOrderException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	public CancelOrderException(String arg0) {
		super(arg0);
	}

	public CancelOrderException(Throwable arg0) {
		super(arg0);
	}

}
