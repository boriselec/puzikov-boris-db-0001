package com.see.common.message;

import java.io.Serializable;

public class DisconnectRequest implements Serializable {

	private static final long serialVersionUID = 7809867503024988773L;

	public DisconnectRequest(boolean isClear) {
		this.isClear = isClear;
	}

	private final boolean isClear;

	public boolean isClear() {
		return isClear;
	}
}
