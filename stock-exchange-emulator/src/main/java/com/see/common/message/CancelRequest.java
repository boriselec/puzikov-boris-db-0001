package com.see.common.message;

import java.io.Serializable;
import java.util.UUID;

public class CancelRequest implements Serializable {

	private static final long serialVersionUID = -5333905396967051612L;

	public CancelRequest(Integer localId, UUID globalUuid) {
		this.localId = localId;
		this.globalUuid = globalUuid;
	}

	private final Integer localId;
	private final UUID globalUuid;

	public UUID getGlobalUuid() {
		return globalUuid;
	}

	public Integer getLocalID() {
		return localId;
	}
}
