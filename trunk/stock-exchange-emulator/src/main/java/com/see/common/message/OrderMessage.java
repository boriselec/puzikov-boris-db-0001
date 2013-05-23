package com.see.common.message;

import java.io.Serializable;

import com.see.common.domain.OrderType;

public class OrderMessage implements Serializable {

	public OrderMessage(String clientName, String stock, float price,
			int quantity, OrderType tradeOperation) {
		this.clientName = clientName;
		this.stock = stock;
		this.price = price;
		this.quantity = quantity;
		this.tradeOperation = tradeOperation;
	}

	private static final long serialVersionUID = -1095619134321659430L;

	private String clientName;
	private String stock;
	private float price;
	private int quantity;
	private OrderType tradeOperation;
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
