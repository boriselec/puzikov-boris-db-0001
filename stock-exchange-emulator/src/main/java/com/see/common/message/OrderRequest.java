package com.see.common.message;

import java.io.Serializable;

import com.see.common.domain.OrderType;

public class OrderRequest implements Serializable {
	private static final long serialVersionUID = -1095619134321659430L;

	public OrderRequest(String clientName, String stock, float price,
			int quantity, OrderType tradeOperation) {
		this.clientName = clientName;
		this.stock = stock;
		this.price = price;
		this.quantity = quantity;
		this.tradeOperation = tradeOperation;
	}

	private final String clientName;
	private final String stock;
	private final float price;
	private final int quantity;
	private final OrderType tradeOperation;
	private int localID;

	public String getStockName() {
		return this.stock;
	}

	public int getQuantity() {
		return this.quantity;
	}

	public float getPrice() {
		return this.price;
	}

	public void setLocalOrderID(int local) {
		this.localID = local;
	}

	public OrderType getType() {
		return this.tradeOperation;
	}

	public String getClientName() {
		return this.clientName;
	}

	public Integer getLocalOrderID() {
		return this.localID;
	}

}
