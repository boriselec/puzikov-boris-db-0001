package com.see.common.message;

import java.io.Serializable;
import java.util.UUID;

public class PlasedResponse implements Serializable {

	private static final long serialVersionUID = -2375170614457314907L;

	public PlasedResponse(Integer localId, UUID globalUuid, boolean isPlaced) {
		super();
		this.localId = localId;
		this.globalUuid = globalUuid;
		this.isPlaced = isPlaced;
	}

	private final Integer localId;
	private final UUID globalUuid;
	private final boolean isPlaced;

	public UUID getGlobalUuid() {
		return globalUuid;
	}

	public Integer getLocalID() {
		return localId;
	}

	public boolean isPlaced() {
		return isPlaced;
	}
}
