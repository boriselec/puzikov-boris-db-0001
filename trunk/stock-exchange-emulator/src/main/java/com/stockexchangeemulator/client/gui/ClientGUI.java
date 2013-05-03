package com.stockexchangeemulator.client.gui;

import java.awt.Color;
import java.awt.EventQueue;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

public class ClientGUI extends JFrame {

	private JPanel contentPane;
	private JTextField textField;
	private JTextField textField_1;
	private JTextField textField_2;
	private JTable table;

	private static final String[] COLUMN_NAMES = { "Symbol", "Status",
			"Quantity", "Traded", "Type", "Price", "Date" };
	private JTable table_1;

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
		panel.setBounds(10, 11, 178, 159);
		contentPane.add(panel);
		panel.setLayout(null);

		textField = new JTextField();
		textField.setBounds(54, 11, 114, 20);
		panel.add(textField);
		textField.setColumns(10);

		JLabel lblNewLabel = new JLabel("Symbol");
		lblNewLabel.setBounds(10, 14, 46, 14);
		panel.add(lblNewLabel);

		JRadioButton rdbtnNewRadioButton = new JRadioButton("Limit");
		rdbtnNewRadioButton.setBounds(54, 38, 47, 23);
		panel.add(rdbtnNewRadioButton);

		JRadioButton rdbtnNewRadioButton_1 = new JRadioButton("Market");
		rdbtnNewRadioButton_1.setBounds(108, 38, 60, 23);
		panel.add(rdbtnNewRadioButton_1);

		ButtonGroup orderTypeGroup = new ButtonGroup();
		orderTypeGroup.add(rdbtnNewRadioButton);
		orderTypeGroup.add(rdbtnNewRadioButton_1);

		JLabel lblType = new JLabel("Type");
		lblType.setBounds(10, 39, 46, 14);
		panel.add(lblType);

		JLabel lblPrice = new JLabel("Price");
		lblPrice.setBounds(10, 71, 46, 14);
		panel.add(lblPrice);

		textField_1 = new JTextField();
		textField_1.setBounds(54, 68, 114, 20);
		panel.add(textField_1);
		textField_1.setColumns(10);

		textField_2 = new JTextField();
		textField_2.setBounds(54, 99, 114, 20);
		panel.add(textField_2);
		textField_2.setColumns(10);

		JLabel lblQuantity = new JLabel("Quantity");
		lblQuantity.setBounds(10, 102, 46, 14);
		panel.add(lblQuantity);

		JButton btnNewButton = new JButton("Submit");
		btnNewButton.setBounds(10, 130, 158, 20);
		panel.add(btnNewButton);

		JPanel panel_1 = new JPanel();
		panel_1.setBorder(new LineBorder(new Color(0, 0, 0)));
		panel_1.setBounds(198, 11, 684, 451);
		contentPane.add(panel_1);
		panel_1.setLayout(null);

		JButton btnNewButton_1 = new JButton("Cancel");
		btnNewButton_1.setBounds(10, 417, 91, 23);
		panel_1.add(btnNewButton_1);

		Object[][] testDataObjects = { { "", "", "", "", "", "", "" },
				{ "", "", "", "", "", "", "" }, { "", "", "", "", "", "", "" },
				{ "", "", "", "", "", "", "" }, { "", "", "", "", "", "", "" },
				{ "", "", "", "", "", "", "" }, { "", "", "", "", "", "", "" },
				{ "", "", "", "", "", "", "" }, { "", "", "", "", "", "", "" },
				{ "", "", "", "", "", "", "" }, { "", "", "", "", "", "", "" },
				{ "", "", "", "", "", "", "" }, { "", "", "", "", "", "", "" },
				{ "", "", "", "", "", "", "" }, { "", "", "", "", "", "", "" },
				{ "", "", "", "", "", "", "" }, { "", "", "", "", "", "", "" },
				{ "", "", "", "", "", "", "" }, { "", "", "", "", "", "", "" },
				{ "", "", "", "", "", "", "" }, { "", "", "", "", "", "", "" },
				{ "", "", "", "", "", "", "" }, { "", "", "", "", "", "", "" },
				{ "", "", "", "", "", "", "" }, { "", "", "", "", "", "", "" },
				{ "", "", "", "", "", "", "" }, { "", "", "", "", "", "", "" },
				{ "", "", "", "", "", "", "" }, { "", "", "", "", "", "", "" },
				{ "", "", "", "", "", "", "" }, { "", "", "", "", "", "", "" } };
		table_1 = new JTable(testDataObjects, COLUMN_NAMES);
		JScrollPane jScrollPane = new JScrollPane(table_1);
		jScrollPane.setBounds(10, 11, 664, 395);
		panel_1.add(jScrollPane);

	}
}
