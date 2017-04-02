package module.invoiceprodresult.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.table.AbstractTableModel;
import javax.swing.text.AbstractDocument;
import javax.swing.text.DocumentFilter;

import main.component.AppConstants;
import main.component.DialogBox;
import main.component.UppercaseDocumentFilter;
import main.panel.MainPanel;
import module.invoiceprodresult.model.InvPrProduct;
import module.invoiceprodresult.model.InvoiceProdResult;
import module.receiveprodresult.model.RPRProduct;
import module.util.Bridging;
import module.util.JTextFieldLimit;

import org.apache.log4j.Logger;

import module.purchaseprodresult.model.PPRProduct;

import com.toedter.calendar.JDateChooser;

import controller.ServiceFactory;

public class InvoiceProdResultViewPanel extends JPanel implements Bridging {

	private static final long serialVersionUID = 1L;

	private static final Logger LOGGER = Logger
			.getLogger(InvoiceProdResultViewPanel.class);

	JLabel lblPurchaseProductResultCode;
	JLabel lblPurchaseDate;
	JLabel lblReceiveProductResultCode;
	JLabel lblReceiveDate;
	JLabel lblDueDate;
	JLabel lblSupplier;
	JLabel lblCurrency;
	JLabel lblCurrencyRate;
	JLabel lblPaymentStatus;
	JLabel lblSubtotal;
	JLabel lblDiscount;
	JLabel lblTax;
	JLabel lblOtherFee;
	JLabel lblTotal;
	JLabel lblPaymentDate;
	
	JTextField txtSubtotal;
	JTextField txtDiscount;
	JTextField txtTax;
	JTextField txtOtherFee;
	JTextField txtTotal;
	
	JButton btnInsertInvPrProduct;
	JButton btnDeleteInvPrProduct;

	JButton btnCancel;
	JButton btnSave;
	JButton btnDelete;
	JButton btnPrint;

	JPanel panel;
	JScrollPane scrollPane;

	JTextField txtPurchaseProductResultCode;
	JTextField txtReceiveProductResultCode;
	JDateChooser dcPurchaseDate;
	JDateChooser dcReceiveDate;
	JDateChooser dcDueDate;
	JTextField txtSupplier;
	JTextField txtCurrency;
	JTextField txtCurrencyRate;
	JComboBox<String> cbPaymentStatus;
	JDateChooser dcPaymentDate;

	JLabel lblErrorPurchaseProductResultCode;
	JLabel lblErrorPurchaseDate;
	JLabel lblErrorReceiveProductResultCode;
	JLabel lblErrorReceiveDate;
	JLabel lblErrorDueDate;
	JLabel lblErrorSupplier;
	JLabel lblErrorCurrency;
	JLabel lblErrorCurrencyRate;
	JLabel lblErrorPaymentStatus;
	JLabel lblErrorPaymentDate;
	
	InvoiceProdResult invoiceProductResult;
	DocumentFilter filter = new UppercaseDocumentFilter();

	List<InvPrProduct> listOfInvPrProduct = null;

	JLabel lblBreadcrumb;
	JLabel lblHeader;

	JScrollPane scrollPaneInvPrProduct;
	JTable tblInvPrProduct;

	InvPrProductTableModel payPrProductTableModel = null;

	private InvoiceProdResultViewPanel invProdResultViewPanel;

	JLabel lblInvPrProduct;

	public InvoiceProdResultViewPanel() {
		invProdResultViewPanel = this;
		invoiceProductResult = new InvoiceProdResult();

		setLayout(null);
		panel = new JPanel();
		panel.setPreferredSize(new Dimension(800, 750));
		panel.setLayout(null);

		lblBreadcrumb = new JLabel("ERP > Pembelian > Invoice Hasil Produksi");
		lblBreadcrumb.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblBreadcrumb.setBounds(50, 10, 500, 25);
		panel.add(lblBreadcrumb);

		lblHeader = new JLabel("View Invoice Hasil Produksi");
		lblHeader.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblHeader.setBounds(50, 45, 320, 25);
		panel.add(lblHeader);

		lblPurchaseProductResultCode = new JLabel(
				"Kode Pembelian");
		lblPurchaseProductResultCode.setBounds(50, 80, 150, 25);
		panel.add(lblPurchaseProductResultCode);

		txtPurchaseProductResultCode = new JTextField();
		txtPurchaseProductResultCode.setBounds(220, 80, 150, 25);
		txtPurchaseProductResultCode.setDocument(new JTextFieldLimit(15));
		((AbstractDocument) txtPurchaseProductResultCode.getDocument())
				.setDocumentFilter(filter);
		txtPurchaseProductResultCode.setEnabled(false);
		panel.add(txtPurchaseProductResultCode);

		lblErrorPurchaseProductResultCode = new JLabel();
		lblErrorPurchaseProductResultCode.setForeground(Color.RED);
		lblErrorPurchaseProductResultCode.setBounds(425, 80, 225, 25);
		panel.add(lblErrorPurchaseProductResultCode);

		lblPurchaseDate = new JLabel(
				"Tanggal Pembelian");
		lblPurchaseDate.setBounds(50, 110, 150, 25);
		panel.add(lblPurchaseDate);

		dcPurchaseDate = new JDateChooser();
		dcPurchaseDate.setBounds(220, 110, 150, 25);
		dcPurchaseDate.setEnabled(false);
		dcPurchaseDate.setDateFormatString("dd-MM-yyyy");
		panel.add(dcPurchaseDate);

		lblErrorPurchaseDate = new JLabel();
		lblErrorPurchaseDate.setForeground(Color.RED);
		lblErrorPurchaseDate.setBounds(425, 110, 225, 25);
		panel.add(lblErrorPurchaseDate);

		lblReceiveProductResultCode = new JLabel(
				"Kode Penerimaan");
		lblReceiveProductResultCode.setBounds(50, 140, 150, 25);
		panel.add(lblReceiveProductResultCode);

		txtReceiveProductResultCode = new JTextField();
		txtReceiveProductResultCode.setBounds(220, 140, 150, 25);
		txtReceiveProductResultCode.setDocument(new JTextFieldLimit(15));
		((AbstractDocument) txtReceiveProductResultCode.getDocument())
				.setDocumentFilter(filter);
		txtReceiveProductResultCode.setEnabled(false);
		panel.add(txtReceiveProductResultCode);

		lblErrorReceiveProductResultCode = new JLabel();
		lblErrorReceiveProductResultCode.setForeground(Color.RED);
		lblErrorReceiveProductResultCode.setBounds(425, 140, 225, 25);
		panel.add(lblErrorReceiveProductResultCode);

		lblReceiveDate = new JLabel(
				"Tanggal Penerimaan");
		lblReceiveDate.setBounds(50, 170, 150, 25);
		panel.add(lblReceiveDate);

		dcReceiveDate = new JDateChooser();
		dcReceiveDate.setBounds(220, 170, 150, 25);
		dcReceiveDate.setEnabled(false);
		dcReceiveDate.setDateFormatString("dd-MM-yyyy");
		panel.add(dcReceiveDate);

		lblErrorReceiveDate = new JLabel();
		lblErrorReceiveDate.setForeground(Color.RED);
		lblErrorReceiveDate.setBounds(425, 170, 225, 25);
		panel.add(lblErrorReceiveDate);
		
		lblDueDate = new JLabel(
				"<html>Tanggal Jatuh Tempo <font color=\"red\">*</font></html>");
		lblDueDate.setBounds(50, 200, 150, 25);
		panel.add(lblDueDate);

		dcDueDate = new JDateChooser();
		dcDueDate.setBounds(220, 200, 150, 25);
		dcDueDate.setEnabled(false);
		dcDueDate.setDateFormatString("dd-MM-yyyy");
		panel.add(dcDueDate);

		lblErrorDueDate = new JLabel();
		lblErrorDueDate.setForeground(Color.RED);
		lblErrorDueDate.setBounds(425, 200, 225, 25);
		panel.add(lblErrorDueDate);
		
		lblSupplier = new JLabel("Supplier");
		lblSupplier.setBounds(50, 230, 150, 25);
		panel.add(lblSupplier);

		txtSupplier = new JTextField();
		txtSupplier.setBounds(220, 230, 150, 25);
		txtSupplier.setEnabled(false);
		panel.add(txtSupplier);

		lblErrorSupplier = new JLabel();
		lblErrorSupplier.setForeground(Color.RED);
		lblErrorSupplier.setBounds(425, 230, 225, 25);
		panel.add(lblErrorSupplier);
		
		lblCurrency = new JLabel("Kurs");
		lblCurrency.setBounds(50, 260, 150, 25);
		panel.add(lblCurrency);

		txtCurrency = new JTextField();
		txtCurrency.setBounds(220, 260, 150, 25);
		txtCurrency.setEnabled(false);
		panel.add(txtCurrency);

		lblErrorCurrency = new JLabel();
		lblErrorCurrency.setForeground(Color.RED);
		lblErrorCurrency.setBounds(425, 260, 225, 25);
		panel.add(lblErrorCurrency);
		
		lblCurrencyRate = new JLabel("Kurs Rate");
		lblCurrencyRate.setBounds(50, 290, 150, 25);
		panel.add(lblCurrencyRate);

		txtCurrencyRate = new JTextField();
		txtCurrencyRate.setBounds(220, 290, 150, 25);
		txtCurrencyRate.setEnabled(false);
		panel.add(txtCurrencyRate);

		lblErrorCurrencyRate = new JLabel();
		lblErrorCurrencyRate.setForeground(Color.RED);
		lblErrorCurrencyRate.setBounds(425, 290, 225, 25);
		panel.add(lblErrorCurrencyRate);
		
		lblPaymentStatus = new JLabel("Status Pembayaran");
		lblPaymentStatus.setBounds(50, 320, 150, 25);
		panel.add(lblPaymentStatus);
		
		String paymentStatus[]={"Ya", "Belum"}; 
		cbPaymentStatus = new JComboBox<String>(paymentStatus);
		cbPaymentStatus.setBounds(220, 320, 150, 25);
		cbPaymentStatus.setEnabled(false);
		panel.add(cbPaymentStatus);
		
		lblErrorPaymentStatus = new JLabel();
		lblErrorPaymentStatus.setForeground(Color.RED);
		lblErrorPaymentStatus.setBounds(425, 320, 225, 25);
		panel.add(lblErrorPaymentStatus);
		
		lblPaymentDate = new JLabel(
				"Tanggal Pembayaran");
		lblPaymentDate.setBounds(50, 350, 150, 25);
		panel.add(lblPaymentDate);

		dcPaymentDate = new JDateChooser();
		dcPaymentDate.setBounds(220, 350, 150, 25);
		dcPaymentDate.setEnabled(false);
		dcPaymentDate.setDateFormatString("dd-MM-yyyy");
		panel.add(dcPaymentDate);

		lblErrorPaymentDate = new JLabel();
		lblErrorPaymentDate.setForeground(Color.RED);
		lblErrorPaymentDate.setBounds(425, 350, 225, 25);
		panel.add(lblErrorPaymentDate);


		scrollPaneInvPrProduct = new JScrollPane();
		scrollPaneInvPrProduct.setBounds(50, 390, 975, 150);
		panel.add(scrollPaneInvPrProduct);

		listOfInvPrProduct = new ArrayList<InvPrProduct>();
		payPrProductTableModel = new InvPrProductTableModel(listOfInvPrProduct);
		tblInvPrProduct = new JTable(payPrProductTableModel);
		tblInvPrProduct.setBorder(new EmptyBorder(5, 5, 5, 5));
		tblInvPrProduct.setFocusable(false);
		tblInvPrProduct.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2) {
					JTable target = (JTable) e.getSource();
					int row = target.getSelectedRow();
					int column = target.getSelectedColumn();

					if (column == 7) {
						// showViewPPRProductDialog(listOfRPRProduct.get(row),
						// pprViewPanel, row);
					}
				}
			}
		});
		scrollPaneInvPrProduct.setViewportView(tblInvPrProduct);
		
		lblSubtotal = new JLabel("Subtotal");
		lblSubtotal.setBounds(800, 550, 150, 25);
		panel.add(lblSubtotal);
		
		txtSubtotal = new JTextField();
		txtSubtotal.setBounds(875, 550, 150, 25);
		txtSubtotal.setText("0.00");
		txtSubtotal.setEnabled(false);
		panel.add(txtSubtotal);
		
		lblDiscount = new JLabel("Diskon");
		lblDiscount.setBounds(800, 580, 150, 25);
		panel.add(lblDiscount);
		
		txtDiscount = new JTextField();
		txtDiscount.setBounds(875, 580, 150, 25);
		txtDiscount.setText("0.00");
		txtDiscount.setEnabled(false);
		panel.add(txtDiscount);
		
		lblTax = new JLabel("Pajak");
		lblTax.setBounds(800, 610, 150, 25);
		panel.add(lblTax);
		
		txtTax = new JTextField();
		txtTax.setBounds(875, 610, 150, 25);
		txtTax.setText("0.00");
		txtTax.setEnabled(false);
		panel.add(txtTax);
		
		lblOtherFee = new JLabel("Biaya Lain-lain");
		lblOtherFee.setBounds(800, 640, 150, 25);
		panel.add(lblOtherFee);
		
		txtOtherFee = new JTextField();
		txtOtherFee.setBounds(875, 640, 150, 25);
		txtOtherFee.setText("0.00");
		txtOtherFee.setEnabled(false);
		panel.add(txtOtherFee);
		
		lblTotal = new JLabel("Total");
		lblTotal.setBounds(800, 670, 150, 25);
		panel.add(lblTotal);
		
		txtTotal = new JTextField();
		txtTotal.setBounds(875, 670, 150, 25);
		txtTotal.setText("0.00");
		txtTotal.setEnabled(false);
		panel.add(txtTotal);

		btnSave = new JButton("Input");
		btnSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				MainPanel
						.changePanel(
								"module.invoiceprodresult.ui.InvoiceProdResultCreatePanel",
								invoiceProductResult);
			}
		});
		btnSave.setBounds(925, 700, 100, 25);
		btnSave.setFocusable(false);
		panel.add(btnSave);

		btnPrint = new JButton("Cetak");
		btnPrint.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				// doPrint();
			}
		});
		btnPrint.setBounds(820, 700, 100, 25);
		btnPrint.setFocusable(false);
		panel.add(btnPrint);

		// btnDelete = new JButton("Hapus");
		// btnDelete.addActionListener(new ActionListener() {
		// public void actionPerformed(ActionEvent arg0) {
		// int response = DialogBox.showDeleteChoice();
		// if (response == JOptionPane.YES_OPTION) {
		// doDelete();
		// }
		// }
		// });
		// btnDelete.setBounds(820, 560, 100, 25);
		// panel.add(btnDelete);

		btnCancel = new JButton("Kembali");
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				int response = DialogBox.showCloseChoice();
				if (response == JOptionPane.YES_OPTION) {
					MainPanel
							.changePanel("module.paymentprodresult.ui.InvoiceProdResultListPanel");
				}
			}
		});
		btnCancel.setBounds(50, 700, 100, 25);
		btnCancel.setFocusable(false);
		panel.add(btnCancel);

		scrollPane = new JScrollPane(panel,
				JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setSize(MainPanel.bodyPanel.getSize());
		scrollPane
				.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollPane
				.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		scrollPane.getVerticalScrollBar().setUnitIncrement(32);
		scrollPane.getHorizontalScrollBar().setUnitIncrement(32);
		scrollPane.setBorder(new EmptyBorder(0, 0, 0, 0));

		add(scrollPane);

	}

	protected String doCalculateSubTotal() {
		BigDecimal subtotal = new BigDecimal("0.00");
		for (InvPrProduct pprProduct : listOfInvPrProduct) {
			subtotal = subtotal.add(pprProduct.getSubtotal());
		}

		return subtotal.toString();
	}

	protected void showViewPPRProductDialog(RPRProduct pprProduct,
			InvoiceProdResultViewPanel pprViewPanel, Integer index) {
		// PPRProductDialog pprProductDialog = new PPRProductDialog(true,
		// pprProduct, pprViewPanel, index);
		// pprProductDialog.setTitle("Barang");
		// pprProductDialog.setLocationRelativeTo(null);
		// pprProductDialog.setVisible(true);
	}

	protected void doDelete() {
//		try {
//			ServiceFactory.getReceiveProductResultBL().deleteAll(
//					invoiceProductResult);
//			DialogBox.showDelete();
//			MainPanel
//					.changePanel("module.purchaseprodresult.ui.ReceiveProdResultListPanel");
//		} catch (SQLException e1) {
//			LOGGER.error(e1.getMessage());
//			DialogBox.showErrorException();
//		}
	}

	/**
	 * Class as TableModel for PPR Product table
	 * 
	 * @author TSI
	 *
	 */
	class InvPrProductTableModel extends AbstractTableModel {

		private static final long serialVersionUID = 1L;

		private List<InvPrProduct> listOfInvPrProduct;

		public InvPrProductTableModel(List<InvPrProduct> listOfInvPrProduct) {
			this.listOfInvPrProduct = listOfInvPrProduct;
		}

		/**
		 * Method to get row count
		 * 
		 * @return int
		 */
		public int getRowCount() {
			return listOfInvPrProduct.size();
		}

		/**
		 * Method to get Column Count
		 */
		public int getColumnCount() {
			return 7;
		}

		/**
		 * Method to get selected value
		 * 
		 * @param rowIndex
		 *            rowIndex of selected table
		 * @param columnIndex
		 *            columnIndex of selected table
		 * @return ({@link PPRProduct}) Object
		 */
		public Object getValueAt(int rowIndex, int columnIndex) {
			InvPrProduct p = listOfInvPrProduct.get(rowIndex);
			switch (columnIndex) {
			case 0:
				return p.getProductCode();
			case 1:
				return p.getProduct().getProductName();
			case 2:
				return p.getQtyPurchase();
			case 3:
				return p.getQtyReceive();
			case 4:
				return p.getPrice();
			case 5:
				return p.getIdrPrice();
			case 6:
				return p.getSubtotal();
//			case 7:
//				return "<html><u>Edit</u></html>";
			default:
				return "";
			}
		}

		public boolean isCellEditable(int row, int column) {
			return false;
		}

		@SuppressWarnings({ "unchecked", "rawtypes" })
		public Class getColumnClass(int column) {
			switch (column) {
			case 0:
				return String.class;
			case 1:
				return String.class;
			case 2:
				return BigDecimal.class;
			case 3:
				return BigDecimal.class;
			case 4:
				return BigDecimal.class;
			case 5:
				return BigDecimal.class;
			case 6:
				return BigDecimal.class;
			default:
				return String.class;
			}
		}

		/**
		 * Method to getColumnName
		 * 
		 * @param column
		 *            columnIndex
		 * @return String column name
		 */
		public String getColumnName(int column) {
			switch (column) {
			case 0:
				return "Kode Produk";
			case 1:
				return "Nama Produk";
			case 2:
				return "Qty Pembelian (m3)";
			case 3:
				return "Qty Penerimaan (m3)";
			case 4:
				return "Harga Satuan";
			case 5:
				return "Harga Satuan (IDR)";
			case 6:
				return "Subtotal";
//			case 7:
//				return "Tindakan";
			default:
				return "";
			}
		}

	}

	public void refreshTableRPRProduct() {
		try {
			tblInvPrProduct.setModel(new InvPrProductTableModel(listOfInvPrProduct));
		} catch (Exception e1) {
			LOGGER.error(e1.getMessage());
			DialogBox.showErrorException();
		}
	}

	@Override
	public void invokeObjects(Object... objects) {
		this.invoiceProductResult = (InvoiceProdResult) objects[0];

		loadData(invoiceProductResult.getId(), invoiceProductResult.getSource());
	}

	protected void loadData(Integer id, String source) {
		try {
			if(AppConstants.SOURCE_INVOICE.equals(source)) {
				invoiceProductResult = ServiceFactory.getInvoiceProductResultBL().getPayPrById(id);
				if (invoiceProductResult != null) {
					listOfInvPrProduct = ServiceFactory.getInvoiceProductResultBL().getInvPrProductByPPRCode(invoiceProductResult.getId());
				}
			} 
			else if(AppConstants.SOURCE_RECEIVE.equals(source)) {
				invoiceProductResult = ServiceFactory.getInvoiceProductResultBL().getRPRById(id);
				
				if (invoiceProductResult != null) {
					listOfInvPrProduct = ServiceFactory.getInvoiceProductResultBL().getInvPrProductByRPRCode(invoiceProductResult.getRprCode());
				}
			}
		
			if (invoiceProductResult != null) {
				txtPurchaseProductResultCode.setText(invoiceProductResult.getPprCode());
				dcPurchaseDate.setDate(invoiceProductResult.getPurchaseDate());
				txtReceiveProductResultCode.setText(invoiceProductResult.getRprCode());
				dcReceiveDate.setDate(invoiceProductResult.getReceiveDate());
				dcDueDate.setDate(invoiceProductResult.getDueDate());
				txtSupplier.setText(invoiceProductResult.getSuppName());
				txtCurrency.setText(invoiceProductResult.getCurrency());
				dcPaymentDate.setDate(invoiceProductResult.getPaymentDate());
				
				if(invoiceProductResult.getRate() == null) {
					txtCurrencyRate.setText(new BigDecimal("1").toString());
				} else {
					txtCurrencyRate.setText(invoiceProductResult.getRate().setScale(2, BigDecimal.ROUND_DOWN).toString());
				}
				
				if(invoiceProductResult.getSubtotal() == null) {
					String subtotal = doCalculateSubTotal();
					txtSubtotal.setText(subtotal);
				} else {
					txtSubtotal.setText(invoiceProductResult.getSubtotal().setScale(2, BigDecimal.ROUND_DOWN).toString());
				}
				
				if(invoiceProductResult.getDisc() == null) {
					txtDiscount.setText(new BigDecimal("0.00").toString());
				} else {
					txtDiscount.setText(invoiceProductResult.getDisc().setScale(2, BigDecimal.ROUND_DOWN).toString());
				}
				
				if(invoiceProductResult.getTax() == null) {
					txtTax.setText(new BigDecimal("0.00").toString());
				} else {
					txtTax.setText(invoiceProductResult.getTax().setScale(2, BigDecimal.ROUND_DOWN).toString());
				}
				
				if(invoiceProductResult.getOtherFee() == null) {
					txtOtherFee.setText(new BigDecimal("0.00").toString());
				} else {
					txtOtherFee.setText(invoiceProductResult.getOtherFee().setScale(2, BigDecimal.ROUND_DOWN).toString());
				}
				
				if(invoiceProductResult.getTotal() == null) {
					String total = calculateTotal(txtSubtotal.getText(), txtTax.getText()
							, txtDiscount.getText(), txtOtherFee.getText()).toString();
					txtTotal.setText(total);
				} else {
					txtTotal.setText(invoiceProductResult.getTotal().setScale(2, BigDecimal.ROUND_DOWN).toString());
				}
				
				cbPaymentStatus.setSelectedItem(invoiceProductResult.getPaymentStatus());
				
				refreshTableRPRProduct();
				
				if(AppConstants.FINAL.equals(invoiceProductResult.getStatus()) && AppConstants.SOURCE_INVOICE.equals(source))
				{
					btnSave.setEnabled(false);
				}
			}
		} catch (SQLException e1) {
			e1.printStackTrace();
			LOGGER.error(e1.getMessage());
			DialogBox.showErrorException();
		}
	}
	
	private BigDecimal calculateTotal(String subTotal, String tax, String discount, String otherFee) {
		if("".equals(subTotal) || "0.00".equals(subTotal))
			subTotal = "0";
		if("".equals(tax)  || "0.00".equals(tax))
			tax = "0";
		if("".equals(discount)  || "0.00".equals(discount))
			discount = "0";
		if("".equals(otherFee)  || "0.00".equals(otherFee))
			otherFee = "0";
		
		BigDecimal bSubTotal = new BigDecimal(subTotal);
		BigDecimal bTax = new BigDecimal(tax);
		BigDecimal bDiscount = new BigDecimal(discount);
		BigDecimal bOtherFee = new BigDecimal(otherFee);
		
		return bSubTotal.subtract(bDiscount).add(bTax).add(bOtherFee);
		
	}
}
