package com.see.common.message;

import java.io.Serializable;
import java.util.UUID;

public class IDPair implements Serializable {

	private static final long serialVersionUID = 8289098375042855268L;

	public IDPair(Integer localUuid, UUID gloUuid, boolean isPlaced) {
		this.localId = localUuid;
		this.globalUuid = gloUuid;
		this.isPlaced = isPlaced;
	}

	public IDPair(int local, UUID orderID) {
		this.localId = local;
		this.globalUuid = orderID;
		this.isPlaced = false;
	}

	private Integer localId;
	private UUID globalUuid;
	private boolean isPlaced;

	public boolean isPlaced() {
		return isPlaced;
	}

	public UUID getGlobalUuid() {
		return globalUuid;
	}

	public Integer getLocalID() {
		return localId;
	}

}
