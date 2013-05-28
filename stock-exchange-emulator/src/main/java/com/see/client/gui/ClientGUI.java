package com.see.client.gui;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.UUID;
import java.util.logging.Logger;

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

import com.see.client.network.DefaultClient;
import com.see.client.network.TradeListener;
import com.see.common.domain.OrderType;
import com.see.common.exception.BadOrderException;
import com.see.common.exception.CancelOrderException;
import com.see.common.exception.LoginException;
import com.see.common.message.DisconnectRequest;
import com.see.common.message.OrderRequest;
import com.see.common.message.TradeResponse;

@SuppressWarnings("serial")
public class ClientGUI extends JFrame {
	private static Logger log = Logger.getLogger(ClientGUI.class.getName());
	private String login;
	boolean isConnected = false;

	private TradeListener responseObserver = new TradeListener() {
		@Override
		public void onTrade(TradeResponse response) {
			table.drawResponse(response);
			log.info("new response received");
		}

	};

	private DefaultClient client;

	private ActionListener loginListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			loginClick();
		}
	};
	private ActionListener disconnectListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			if (checkConnection() == false)
				return;
			disconnectClick();
		}
	};
	private ActionListener cancelListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent arg0) {
			if (checkConnection() == false)
				return;
			cancelOrderClick();
		}
	};
	private ActionListener sendListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent arg0) {
			if (checkConnection() == false)
				return;
			sendOrderClick();
		}
	};

	private final JPanel contentPane;
	private final JTextField symbolTextField;
	private final JTextField priceTextField;
	private final JTextField quantityTextField;

	private static final String[] COLUMN_NAMES = { "Order ID", "Symbol",
			"Status", "Quantity", "Traded", "Deal price", "Type", "Limit",
			"Date" };
	private final JRadioButton limitRadioButton;
	private final JRadioButton marketRadioButton;
	private final JRadioButton buyRadioButton;
	private final JRadioButton sellRadioButton;
	private final JLabel loginStatusLabel;
	private final JTextField loginTextField;
	private final TableRepresentation table;
	private final JTable jtable;

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

		client = new DefaultClient();
		client.addObserver(responseObserver);

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1000, 500);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JPanel panel = new JPanel();
		panel.setToolTipText("");
		panel.setBorder(new LineBorder(new Color(0, 0, 0)));
		panel.setBounds(10, 11, 203, 191);
		contentPane.add(panel);
		panel.setLayout(null);

		symbolTextField = new JTextField();
		symbolTextField.setBounds(66, 11, 127, 20);
		panel.add(symbolTextField);
		symbolTextField.setColumns(10);

		JLabel lblNewLabel = new JLabel("Symbol");
		lblNewLabel.setBounds(10, 14, 46, 14);
		panel.add(lblNewLabel);

		limitRadioButton = new JRadioButton("Limit");
		limitRadioButton.setSelected(true);
		limitRadioButton.setBounds(79, 67, 47, 23);
		panel.add(limitRadioButton);

		marketRadioButton = new JRadioButton("Market");
		marketRadioButton.setBounds(133, 67, 60, 23);
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
		priceTextField.setBounds(66, 97, 127, 20);
		panel.add(priceTextField);
		priceTextField.setColumns(10);

		quantityTextField = new JTextField();
		quantityTextField.setBounds(66, 128, 127, 20);
		panel.add(quantityTextField);
		quantityTextField.setColumns(10);

		JLabel lblQuantity = new JLabel("Quantity");
		lblQuantity.setBounds(10, 131, 46, 14);
		panel.add(lblQuantity);

		JButton submitButton = new JButton("Submit");

		submitButton.setBounds(10, 159, 183, 20);
		panel.add(submitButton);

		JLabel lblOperation = new JLabel("Operation");
		lblOperation.setBounds(10, 42, 63, 14);
		panel.add(lblOperation);

		buyRadioButton = new JRadioButton("Buy");
		buyRadioButton.setSelected(true);
		buyRadioButton.setBounds(79, 38, 47, 23);
		panel.add(buyRadioButton);

		sellRadioButton = new JRadioButton("Sell");
		sellRadioButton.setBounds(133, 38, 60, 23);
		panel.add(sellRadioButton);

		ButtonGroup buyOrSellTypeGroup = new ButtonGroup();
		buyOrSellTypeGroup.add(buyRadioButton);
		buyOrSellTypeGroup.add(sellRadioButton);

		submitButton.addActionListener(sendListener);

		JPanel panel_1 = new JPanel();
		panel_1.setBorder(new LineBorder(new Color(0, 0, 0)));
		panel_1.setBounds(223, 11, 759, 451);
		contentPane.add(panel_1);
		panel_1.setLayout(null);

		JButton cancelButton = new JButton("Cancel");
		cancelButton.addActionListener(cancelListener);
		cancelButton.setBounds(10, 417, 91, 23);
		panel_1.add(cancelButton);

		DefaultTableModel dataTable = new DefaultTableModel(null, COLUMN_NAMES);
		jtable = new JTable();
		table = new TableRepresentation(dataTable);
		jtable.setModel(dataTable);
		JScrollPane jScrollPane = new JScrollPane(jtable);
		jScrollPane.setBounds(10, 11, 739, 395);
		panel_1.add(jScrollPane);

		JPanel panel_2 = new JPanel();
		panel_2.setBorder(new LineBorder(new Color(0, 0, 0)));
		panel_2.setBounds(10, 213, 203, 134);
		contentPane.add(panel_2);
		panel_2.setLayout(null);

		loginStatusLabel = new JLabel("Disconnected");
		loginStatusLabel.setBounds(64, 11, 139, 14);
		panel_2.add(loginStatusLabel);

		JButton loginButton = new JButton("Connect");
		loginButton.addActionListener(loginListener);
		loginButton.setBounds(10, 67, 183, 23);
		panel_2.add(loginButton);

		loginTextField = new JTextField();
		loginTextField.setBounds(64, 36, 129, 20);
		panel_2.add(loginTextField);
		loginTextField.setColumns(10);

		JLabel lblStatus = new JLabel("Status:");
		lblStatus.setBounds(10, 11, 46, 14);
		panel_2.add(lblStatus);

		JLabel lblLogin = new JLabel("Login");
		lblLogin.setBounds(10, 42, 46, 14);
		panel_2.add(lblLogin);

		JButton btnNewButton = new JButton("Disconnect");
		btnNewButton.addActionListener(disconnectListener);
		btnNewButton.setBounds(10, 101, 183, 23);
		panel_2.add(btnNewButton);
	}

	private boolean checkConnection() {
		if (isConnected == false) {
			JOptionPane.showMessageDialog(contentPane, "No connection");
			return false;
		} else
			return true;
	}

	private void sendOrderClick() {
		String stockName = symbolTextField.getText();
		OrderType type = (buyRadioButton.getSelectedObjects() != null) ? OrderType.BUY
				: OrderType.SELL;
		boolean isLimit = (limitRadioButton.getSelectedObjects() != null);
		String priceString = priceTextField.getText();
		String quantityString = quantityTextField.getText();
		try {
			float price;
			if (isLimit)
				price = Float.parseFloat(priceString);
			else
				price = (type == OrderType.BUY) ? Float.POSITIVE_INFINITY
						: Float.NEGATIVE_INFINITY;
			int quantity = Integer.parseInt(quantityString);
			OrderRequest order = new OrderRequest(login, stockName, price,
					quantity, type);
			clearTextFields();
			UUID orderId = client.sendOrder(order);
			log.info(String.format("Sended new order: orderID=%s",
					orderId.toString()));
			table.drawTradeOrder(orderId, order);
		} catch (NumberFormatException | BadOrderException e) {
			JOptionPane.showMessageDialog(contentPane,
					"Bad arguments: " + e.getMessage());
		} catch (LoginException e) {
			JOptionPane.showMessageDialog(contentPane,
					"Not connected: " + e.getMessage());
		}

	}

	private void clearTextFields() {
		symbolTextField.setText("");
		priceTextField.setText("");
		quantityTextField.setText("");
	}

	private void cancelOrderClick() {
		int index = jtable.getSelectedRow();
		if (index == -1) {
			JOptionPane
					.showMessageDialog(contentPane, "Select order to cancel");
			return;
		}
		String statusString = table.getStatus(index);
		UUID orderID = table.getOrderID(index);
		if (statusString == "CANCELED" || statusString == "SEND CANCEL") {
			JOptionPane.showMessageDialog(contentPane, "Already canceled");
			return;
		}
		if (statusString == "FULLY_FILLED") {
			JOptionPane.showMessageDialog(contentPane, "Already executed");
			return;
		}

		log.info(String.format("Send cancel for order: orderID=%s",
				orderID.toString()));
		try {
			client.cancelOrder(orderID);
			log.info("Send to cancel order");
			table.drawCancel(orderID);
		} catch (CancelOrderException e) {
			JOptionPane.showMessageDialog(contentPane, "Can't cancel order. "
					+ e.getMessage());
			log.info("Failed to cancel order");
		}

	}

	private void loginClick() {
		if (isConnected == true) {
			JOptionPane.showMessageDialog(contentPane, "Already connected");
			return;
		}
		try {
			login = loginTextField.getText();
			client.login(login);
			isConnected = true;
			log.info(String.format("Connected to stock exchange. ClientID=%s",
					login));
			loginStatusLabel.setText("Connected");
		} catch (LoginException ex) {
			loginStatusLabel.setText("Disconnected");
			JOptionPane.showMessageDialog(contentPane,
					"Login error: " + ex.getMessage());
		}
	}

	private void disconnectClick() {
		table.clearTable();
		DisconnectRequest request = new DisconnectRequest(false);
		client.sendDisconnect(request);
		isConnected = false;
		loginStatusLabel.setText("Disconnected");
	}
}
