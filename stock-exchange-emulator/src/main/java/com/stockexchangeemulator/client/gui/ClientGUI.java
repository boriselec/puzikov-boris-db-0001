package com.stockexchangeemulator.client.gui;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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

import com.stockexchangeemulator.client.network.OrderingService;
import com.stockexchangeemulator.client.service.api.ResponseObserver;
import com.stockexchangeemulator.client.service.exception.BadOrderException;
import com.stockexchangeemulator.client.service.exception.NoLoginException;
import com.stockexchangeemulator.domain.Order;
import com.stockexchangeemulator.domain.OrderVerifier;
import com.stockexchangeemulator.domain.Response;
import com.stockexchangeemulator.domain.Status;
import com.stockexchangeemulator.domain.TradeOperation;
import com.stockexchangeemulator.domain.TradeOrder;

@SuppressWarnings("serial")
public class ClientGUI extends JFrame {
	private static Logger log = Logger.getLogger(ClientGUI.class.getName());
	private String login;
	boolean isConnected = false;

	private ResponseObserver responseObserver = new ResponseObserver() {
		@Override
		public void onResponse(Response response) {
			if (response.getStatus() == Status.ERROR)
				JOptionPane.showMessageDialog(contentPane,
						response.getMessage());
			table.drawResponse(response);
			log.info("new response received");
		}
	};

	private OrderVerifier orderVerifier = new OrderVerifier();
	private OrderingService orderingService = new OrderingService(
			responseObserver);

	private ActionListener loginListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			loginClick();
		}
	};
	private ActionListener disconnectListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
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

	private JPanel contentPane;
	private JTextField symbolTextField;
	private JTextField priceTextField;
	private JTextField quantityTextField;

	private static final String[] COLUMN_NAMES = { "Order ID", "Symbol",
			"Status", "Quantity", "Traded", "Deal price", "Type", "Limit",
			"Date" };
	private final JRadioButton limitRadioButton;
	private final JRadioButton marketRadioButton;
	private final JRadioButton buyRadioButton;
	private final JRadioButton sellRadioButton;
	private final JLabel loginStatusLabel;
	private JTextField loginTextField;
	private TableRepresentation table;
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

	public boolean checkConnection() {
		if (isConnected == false) {
			JOptionPane.showMessageDialog(contentPane, "No connection");
			return false;
		} else
			return true;
	}

	private void sendOrderClick() {
		String stockName = symbolTextField.getText();
		TradeOperation type = (buyRadioButton.getSelectedObjects() != null) ? TradeOperation.BID
				: TradeOperation.OFFER;
		String limitOrMarket = (limitRadioButton.getSelectedObjects() != null) ? "limit"
				: "market";
		String price = priceTextField.getText();
		String sharesCount = quantityTextField.getText();
		try {
			TradeOrder order = orderVerifier.getTradeOrder(login, stockName,
					type, limitOrMarket, price, sharesCount);
			clearTextFields();
			int orderId = 0;
			orderId = orderingService.sendOrder(order);
			log.info(String.format("Sended new order: orderID=%d", orderId));
			table.drawTradeOrder(orderId, order);
		} catch (BadOrderException e) {
			JOptionPane.showMessageDialog(contentPane,
					"Bad arguments: " + e.getMessage());
			log.info("Failed to send order");
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
		int orderID = table.getOrderID(index);
		String stockNameString = table.getStockName(index);
		if (statusString == "CANCELED" || statusString == "SEND CANCEL") {
			JOptionPane.showMessageDialog(contentPane, "Already canceled");
			return;
		}
		if (statusString == "FULLY_FILLED") {
			JOptionPane.showMessageDialog(contentPane, "Already executed");
			return;
		}

		Order cancelOrder = orderVerifier.getCancelOrder(login,
				stockNameString, orderID);
		log.info(String.format("Send cancel for order: orderID=%d", orderID));
		try {
			orderingService.sendOrder(cancelOrder);
			log.info("Send to cancel order");
		} catch (BadOrderException e) {
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
			orderingService.login(login);
			isConnected = true;
			log.info(String.format("Connected to stock exchange. ClientID=%s",
					login));
			loginStatusLabel.setText("Connected");
		} catch (NoLoginException ex) {
			loginStatusLabel.setText("Disconnected");
			JOptionPane.showMessageDialog(contentPane,
					"Login error: " + ex.getMessage());
		}
	}

	private void disconnectClick() {
		if (checkConnection()) {
			orderingService.disconnect();
			isConnected = false;
			loginStatusLabel.setText("Disconnected");
		}
	}
}
