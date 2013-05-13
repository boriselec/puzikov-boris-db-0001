package com.stockexchangeemulator.client.gui;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.table.DefaultTableModel;

import com.stockexchangeemulator.domain.Response;
import com.stockexchangeemulator.domain.Status;
import com.stockexchangeemulator.domain.TradeOperation;
import com.stockexchangeemulator.domain.TradeOrder;

public class TableRepresentation {
	public TableRepresentation(DefaultTableModel dataTable) {
		this.dataTable = dataTable;
	}

	private SimpleDateFormat dateFormater = new SimpleDateFormat("HH:mm:ss");
	private DefaultTableModel dataTable;

	public void drawTradeOrder(int orderID, TradeOrder order) {
		drawRaw(orderID, order.getStockName(), Status.SEND,
				order.getSharesCount(), 0, Float.NaN, order.getType(),
				order.getPrice(), new Date());
	}

	public void drawCancelOrder() {
	}

	public void drawResponse(Response response) {
		int index;
		try {
			index = getOrderIndex(response.getOrderID());
			dataTable.removeRow(index);
		} catch (IllegalArgumentException e) {
		}
		drawRaw(response.getOrderID(), response.getSymbol(),
				response.getStatus(), response.getRequestedShares(),
				response.getTradedShares(), response.getDealPrice(),
				response.getTradeOperation(), response.getPrice(),
				response.getDate());
	}

	private void drawRaw(int orderID, String stock, Status status,
			int quantity, int tradedShares, float price, TradeOperation type,
			float limit, Date date) {

		String limitString;
		if (limit == Float.POSITIVE_INFINITY
				|| limit == Float.NEGATIVE_INFINITY)
			limitString = "MARKET";
		else
			limitString = ((Float) limit).toString();
		String dateString = (date == null) ? "" : dateFormater.format(date);
		dataTable.addRow(new Object[] { orderID, stock, status, quantity,
				tradedShares, price, type, limitString, dateString });
	}

	private int getOrderIndex(int orderID) {
		for (int i = 0; i < dataTable.getRowCount(); i++) {
			if ((Integer) dataTable.getValueAt(i, 0) == orderID)
				return i;
		}
		throw new IllegalArgumentException();
	}

	public int getOrderID(int index) {
		return (int) dataTable.getValueAt(index, 0);
	}

	public String getStockName(int index) {
		return (String) dataTable.getValueAt(index, 1);
	}

	public String getStatus(int index) {
		return dataTable.getValueAt(index, 2).toString();
	}

}
