package com.see.common.domain;

import java.io.Serializable;
import java.util.UUID;

public class UUIDPair implements Serializable {

	private static final long serialVersionUID = 8289098375042855268L;

	public UUIDPair(UUID localUuid, UUID gloUuid) {
		super();
		this.localUuid = localUuid;
		this.globalUuid = gloUuid;
	}

	public UUID localUuid;
	public UUID globalUuid;

	public UUID getGlobalUuid() {
		return globalUuid;
	}

	public UUID getLocalUuid() {
		return localUuid;
	}

}
