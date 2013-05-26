package com.see.client.gui;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import javax.swing.table.DefaultTableModel;

import com.see.common.domain.OrderType;
import com.see.common.domain.Status;
import com.see.common.message.OrderRequest;
import com.see.common.message.TradeResponse;

public class TableRepresentation {
	public TableRepresentation(DefaultTableModel dataTable) {
		this.dataTable = dataTable;
	}

	private SimpleDateFormat dateFormater = new SimpleDateFormat("HH:mm:ss");
	private DefaultTableModel dataTable;

	public void drawTradeOrder(UUID orderID, OrderRequest order) {
		try {
			int index = getOrderIndex(orderID);
			int tradedShares = (int) dataTable.getValueAt(index, 4);
			float dealPrice = (float) dataTable.getValueAt(index, 5);
			dataTable.removeRow(index);
			Status status;
			if (tradedShares == order.getQuantity())
				status = Status.FULLY_FILLED;
			else
				status = Status.PARTIALLY_FILLED;
			drawRaw(orderID, order.getStockName(), status, order.getQuantity(),
					tradedShares, dealPrice, order.getType(), order.getPrice(),
					new Date());
		} catch (IllegalArgumentException e) {
			drawRaw(orderID, order.getStockName(), Status.SEND,
					order.getQuantity(), 0, (float) 0.0, order.getType(),
					order.getPrice(), new Date());
		}
	}

	public void drawResponse(TradeResponse response) {
		String symbol = "unknown";
		Status status = Status.DELAYED;
		int quantity = response.getQuantity();
		int tradedShares = response.getQuantity();
		float dealPrice = response.getPrice();
		OrderType tradeOperation = null;
		float price = response.getPrice();

		try {
			int index = getOrderIndex(response.getOrderID());
			symbol = (String) dataTable.getValueAt(index, 1);
			quantity = (int) dataTable.getValueAt(index, 3);
			String priceString = (String) dataTable.getValueAt(index, 7);
			tradeOperation = (OrderType) dataTable.getValueAt(index, 6);
			if (priceString.equals("MARKET"))
				price = (tradeOperation == OrderType.BUY) ? Float.POSITIVE_INFINITY
						: Float.NEGATIVE_INFINITY;
			else
				price = Float.parseFloat(priceString);
			tradedShares = (int) dataTable.getValueAt(index, 4);
			dealPrice = (float) dataTable.getValueAt(index, 5);
			if (dealPrice == Float.NaN)
				dealPrice = 0;
			dealPrice = (response.getPrice() * response.getQuantity() + dealPrice
					* tradedShares)
					/ (response.getQuantity() + tradedShares);
			tradedShares += response.getQuantity();
			if (tradedShares == quantity)
				status = Status.FULLY_FILLED;
			else
				status = Status.PARTIALLY_FILLED;
			dataTable.removeRow(index);
		} catch (IllegalArgumentException e) {
		}
		drawRaw(response.getOrderID(), symbol, status, quantity, tradedShares,
				dealPrice, tradeOperation, price, response.getDate());
	}

	public void drawCancel(UUID orderID) {
		try {
			int index = getOrderIndex(orderID);
			dataTable.setValueAt(Status.CANCELED, index, 2);
		} catch (IllegalArgumentException e) {
			return;
		}

	}

	private void drawRaw(UUID uuid, String stock, Status status, int quantity,
			int tradedShares, float price, OrderType type, float limit,
			Date date) {

		String limitString;
		if (limit == Float.POSITIVE_INFINITY
				|| limit == Float.NEGATIVE_INFINITY)
			limitString = "MARKET";
		else
			limitString = ((Float) limit).toString();
		String dateString = (date == null) ? "" : dateFormater.format(date);
		dataTable.addRow(new Object[] { uuid, stock, status, quantity,
				tradedShares, price, type, limitString, dateString });
	}

	private int getOrderIndex(UUID uuid) {
		for (int i = 0; i < dataTable.getRowCount(); i++) {
			if ((UUID) dataTable.getValueAt(i, 0) == uuid)
				return i;
		}
		throw new IllegalArgumentException();
	}

	public UUID getOrderID(int index) {
		return (UUID) dataTable.getValueAt(index, 0);
	}

	public String getStockName(int index) {
		return (String) dataTable.getValueAt(index, 1);
	}

	public String getStatus(int index) {
		return dataTable.getValueAt(index, 2).toString();
	}

	public void clearTable() {
		if (dataTable.getRowCount() > 0)
			for (int i = 0; i <= dataTable.getRowCount(); i++)
				dataTable.removeRow(0);
	}

}
