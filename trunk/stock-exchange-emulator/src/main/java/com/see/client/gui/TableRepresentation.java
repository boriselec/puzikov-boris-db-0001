package com.see.client.gui;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import javax.swing.table.DefaultTableModel;

import com.see.common.domain.ClientResponse;
import com.see.common.domain.Status;
import com.see.common.domain.TradeOperation;
import com.see.common.domain.TradeOrder;

public class TableRepresentation {
	public TableRepresentation(DefaultTableModel dataTable) {
		this.dataTable = dataTable;
	}

	private SimpleDateFormat dateFormater = new SimpleDateFormat("HH:mm:ss");
	private DefaultTableModel dataTable;

	public void drawTradeOrder(UUID orderID, TradeOrder order) {
		drawRaw(orderID, order.getStockName(), Status.SEND,
				order.getSharesCount(), 0, Float.NaN, order.getType(),
				order.getPrice(), order.getDate());
	}

	public void drawCancelOrder() {
	}

	public void drawResponse(ClientResponse response) {
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

	private void drawRaw(UUID uuid, String stock, Status status, int quantity,
			int tradedShares, float price, TradeOperation type, float limit,
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
