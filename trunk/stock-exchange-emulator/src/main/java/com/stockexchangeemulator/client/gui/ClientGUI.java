package com.stockexchangeemulator.client.gui;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;

import com.stockexchangeemulator.client.network.OrderingService;
import com.stockexchangeemulator.client.service.exception.BadOrderException;
import com.stockexchangeemulator.domain.Operation;
import com.stockexchangeemulator.domain.Order;
import com.stockexchangeemulator.domain.Response;
import com.stockexchangeemulator.domain.Status;
import com.stockexchangeemulator.domain.WrappedOrder;

public class ClientGUI extends JFrame {

	private OrderingService orderingService = new OrderingService(null);
	private JPanel contentPane;
	private JTextField symbolTextField;
	private JTextField priceTextField;
	private JTextField quantityTextField;

	private static final String[] COLUMN_NAMES = { "Order ID", "Symbol",
			"Status", "Quantity", "Traded", "Type", "Price", "Date" };
	private JTable table_1;
	private DefaultTableModel model;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					UIManager.setLookAndFeel(UIManager
							.getSystemLookAndFeelClassName());
					ClientGUI frame = new ClientGUI();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public ClientGUI() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 900, 500);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JPanel panel = new JPanel();
		panel.setToolTipText("");
		panel.setBorder(new LineBorder(new Color(0, 0, 0)));
		panel.setBounds(10, 11, 178, 191);
		contentPane.add(panel);
		panel.setLayout(null);

		symbolTextField = new JTextField();
		symbolTextField.setBounds(54, 11, 114, 20);
		panel.add(symbolTextField);
		symbolTextField.setColumns(10);

		JLabel lblNewLabel = new JLabel("Symbol");
		lblNewLabel.setBounds(10, 14, 46, 14);
		panel.add(lblNewLabel);

		final JRadioButton limitRadioButton = new JRadioButton("Limit");
		limitRadioButton.setSelected(true);
		limitRadioButton.setBounds(54, 67, 47, 23);
		panel.add(limitRadioButton);

		JRadioButton marketRadioButton = new JRadioButton("Market");
		marketRadioButton.setBounds(108, 67, 60, 23);
		panel.add(marketRadioButton);

		ButtonGroup limitOrMarketTypeGroup = new ButtonGroup();
		limitOrMarketTypeGroup.add(limitRadioButton);
		limitOrMarketTypeGroup.add(marketRadioButton);

		JLabel lblType = new JLabel("Type");
		lblType.setBounds(10, 71, 46, 14);
		panel.add(lblType);

		JLabel lblPrice = new JLabel("Price");
		lblPrice.setBounds(10, 100, 46, 14);
		panel.add(lblPrice);

		priceTextField = new JTextField();
		priceTextField.setBounds(54, 97, 114, 20);
		panel.add(priceTextField);
		priceTextField.setColumns(10);

		quantityTextField = new JTextField();
		quantityTextField.setBounds(54, 128, 114, 20);
		panel.add(quantityTextField);
		quantityTextField.setColumns(10);

		JLabel lblQuantity = new JLabel("Quantity");
		lblQuantity.setBounds(10, 131, 46, 14);
		panel.add(lblQuantity);

		JButton submitButton = new JButton("Submit");

		submitButton.setBounds(10, 159, 158, 20);
		panel.add(submitButton);

		JLabel lblOperation = new JLabel("Operation");
		lblOperation.setBounds(10, 42, 46, 14);
		panel.add(lblOperation);

		final JRadioButton buyRadioButton = new JRadioButton("Buy");
		buyRadioButton.setSelected(true);
		buyRadioButton.setBounds(54, 38, 47, 23);
		panel.add(buyRadioButton);

		JRadioButton sellRadioButton = new JRadioButton("Sell");
		sellRadioButton.setBounds(108, 38, 60, 23);
		panel.add(sellRadioButton);

		ButtonGroup buyOrSellTypeGroup = new ButtonGroup();
		buyOrSellTypeGroup.add(buyRadioButton);
		buyOrSellTypeGroup.add(sellRadioButton);

		submitButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String stockName = symbolTextField.getText();
				Operation operation = (buyRadioButton.getSelectedObjects() != null) ? Operation.BID
						: Operation.OFFER;
				String type = (limitRadioButton.getSelectedObjects() != null) ? "limit"
						: "market";
				String price = priceTextField.getText();
				String sharesCount = quantityTextField.getText();
				try {
					Order order = orderingService.getOrder(stockName,
							operation, type, price, sharesCount);
					int orderId = orderingService.sendOrder(order);
					drawOrder(orderId, order);
				} catch (BadOrderException e) {
					JOptionPane.showMessageDialog(contentPane,
							"Bad order arguments: " + e.getMessage());
				}
			}
		});

		JPanel panel_1 = new JPanel();
		panel_1.setBorder(new LineBorder(new Color(0, 0, 0)));
		panel_1.setBounds(198, 11, 684, 451);
		contentPane.add(panel_1);
		panel_1.setLayout(null);

		JButton btnNewButton_1 = new JButton("Cancel");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				int index = table_1.getSelectedRow();
				if (index == -1)
					JOptionPane.showMessageDialog(contentPane,
							"Select order to cancel");
				else if (model.getValueAt(index, 2) == "CANCELED"
						|| model.getValueAt(index, 2) == "SEND CANCEL") {
					JOptionPane.showMessageDialog(contentPane,
							"Already canceled");
				} else {

					int orderID = Integer.parseInt((String) model.getValueAt(
							index, 0));
					model.setValueAt("SEND CANCEL", index, 2);
					orderingService.sendCancelOrder(orderID);
				}

			}
		});
		btnNewButton_1.setBounds(10, 417, 91, 23);
		panel_1.add(btnNewButton_1);

		model = new DefaultTableModel(null, COLUMN_NAMES);
		table_1 = new JTable();
		table_1.setModel(model);
		JScrollPane jScrollPane = new JScrollPane(table_1);
		jScrollPane.setBounds(10, 11, 664, 395);
		panel_1.add(jScrollPane);

		drawOrder(1, new Order("TEST", Operation.OFFER, 1, (float) 1.0));
		drawOrder(23, new Order("TEST", Operation.OFFER, 1, (float) 1.0));
		drawResponse(new Response(new WrappedOrder(0, 23, new Order("TEST",
				Operation.BID, 1, (float) 1.0), new Date()),
				Status.FULLY_FILLED, "ok", (float) 1.0, 1, new Date()));
	}

	public void drawOrder(int orderID, Order order) {
		String orderIDString = ((Integer) orderID).toString();
		String symbolsString = order.getStockName();
		String statusString = "SEND";
		String quantityString = ((Integer) order.getSharesCount()).toString();
		String tradedString = "0";
		String typeString = order.getType().toString();
		String priceString = ((Float) order.getPrice()).toString();
		String dateString = new Date().toString();
		model.addRow(new Object[] { orderIDString, symbolsString, statusString,
				quantityString, tradedString, typeString, priceString,
				dateString });

	}

	public void drawResponse(Response response) {
		int index;
		try {
			index = getOrderIndex(response.getOrderID());
		} catch (IllegalArgumentException e) {
			JOptionPane.showMessageDialog(contentPane, "Bad responce received");
			return;
		}
		String orderIDString = ((Integer) response.getOrderID()).toString();
		String symbolsString = (String) model.getValueAt(index, 1);
		String statusString = response.getStatus().toString();
		String quantityString = (String) model.getValueAt(index, 3);
		String tradedString = ((Integer) response.getTradedShares()).toString();
		String typeString = (String) model.getValueAt(index, 5);
		String priceString = ((Float) response.getPrice()).toString();
		String dateString = response.getDate().toString();

		model.removeRow(index);
		model.addRow(new Object[] { orderIDString, symbolsString, statusString,
				quantityString, tradedString, typeString, priceString,
				dateString });
	}

	private int getOrderIndex(int orderID) {
		for (int i = 0; i < model.getRowCount(); i++) {
			if (Integer.parseInt((String) model.getValueAt(i, 0)) == orderID)
				return i;
		}
		throw new IllegalArgumentException();
	}
}