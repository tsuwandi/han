package module.purchaseprodresult.ui;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.text.DocumentFilter;

import org.apache.log4j.Logger;

import controller.ServiceFactory;
import main.component.AppConstants;
import main.component.ComboBox;
import main.component.DialogBox;
import main.component.NumberField;
import main.component.UppercaseDocumentFilter;
import module.product.model.Product;
import module.purchaseprodresult.model.PPRProduct;

public class BigPPRProductDialog extends JDialog {

	private static final long serialVersionUID = 1L;

	private static final Logger LOGGER = Logger.getLogger(BigPPRProductDialog.class);

	JPanel panel;

	JLabel lblProduct;
	JLabel lblQty;

	ComboBox<Product> cbProduct;
	JTextField txtQty;

	JLabel lblErrorProduct;
	JLabel lblErrorQty;

	JButton btnInsert;

	private boolean isEdit;
	private boolean isView;
	private PPRProduct pprProduct;
	private BigPurchaseProdResultCreatePanel pprCreatePanel;
	private BigPurchaseProdResultEditPanel pprEditPanel;
	private BigPurchaseProdResultViewPanel pprViewPanel;
	List<Product> listOfProduct = null;

	private Integer index;
	

	public BigPPRProductDialog(boolean edit, PPRProduct pprProduct, BigPurchaseProdResultCreatePanel pprCreatePanel,
			Integer index) {
		this.isEdit = edit;
		this.isView = false;
		this.pprProduct = pprProduct;
		this.pprCreatePanel = pprCreatePanel;
		this.index = index;
		init();
	}

	public BigPPRProductDialog(boolean edit, PPRProduct pprProduct, BigPurchaseProdResultEditPanel pprEditPanel,
			Integer index) {
		this.isEdit = edit;
		this.isView = false;
		this.pprProduct = pprProduct;
		this.pprEditPanel = pprEditPanel;
		this.index = index;
		init();
	}
	
	public BigPPRProductDialog(boolean view, PPRProduct pprProduct, BigPurchaseProdResultViewPanel pprViewPanel,
			Integer index) {
		this.isEdit = true;
		this.isView = view;
		this.pprProduct = pprProduct;
		this.pprViewPanel = pprViewPanel;
		this.index = index;
		init();
	}

	public void init() {

		setModal(true);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 600, 165);
		getContentPane().setLayout(null);

		DocumentFilter filter = new UppercaseDocumentFilter();

		lblProduct = new JLabel("<html>Produk <font color=\"red\">*</font></html>");
		lblProduct.setBounds(25, 15, 150, 25);
		getContentPane().add(lblProduct);

		listOfProduct = new ArrayList<Product>();
		try {
			listOfProduct = ServiceFactory.getPurchaseProductResultBL().getAllByProductCode(AppConstants.PRODUCT_CODE_NORMAL_A_TYPE_12, AppConstants.PRODUCT_CODE_NORMAL_B_TYPE_12);
			listOfProduct.add(0, new Product("-- Pilih Produk --"));
		} catch (SQLException e1) {
			LOGGER.error(e1.getMessage());
			DialogBox.showErrorException();
		}

		cbProduct = new ComboBox<Product>();
		cbProduct.setList(listOfProduct);
		cbProduct.setBounds(150, 15, 150, 25);
		getContentPane().add(cbProduct);

		lblErrorProduct = new JLabel();
		lblErrorProduct.setForeground(Color.RED);
		lblErrorProduct.setBounds(335, 15, 225, 25);
		getContentPane().add(lblErrorProduct);

		lblQty = new JLabel("<html>Qty <font color=\"red\">*</font></html>");
		lblQty.setBounds(25, 45, 150, 25);
		getContentPane().add(lblQty);

		txtQty = new NumberField(5);
		txtQty.setBounds(150, 45, 150, 25);
		getContentPane().add(txtQty);

		lblErrorQty = new JLabel();
		lblErrorQty.setForeground(Color.RED);
		lblErrorQty.setBounds(335, 45, 225, 25);
		getContentPane().add(lblErrorQty);

		
		btnInsert = new JButton("Insert");
		btnInsert.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (doValidate() == false) {
					return;
				}

				doInsert();
			}
		});
		btnInsert.setBounds(460, 75, 100, 25);
		getContentPane().add(btnInsert);

		if (isEdit == true) {
			txtQty.setText(String.valueOf(pprProduct.getQty()));
			cbProduct.setSelectedItem(pprProduct.getProduct().getProductName());
		}
		
		if(isView == true) {
			txtQty.setEnabled(false);
			cbProduct.setEnabled(false);
			btnInsert.setEnabled(false);
		}
	}

	protected boolean doValidate() {
		boolean isValid = true;

		lblErrorProduct.setText("");
		lblErrorQty.setText("");
		
		if (cbProduct.getSelectedItem() == null || cbProduct.getSelectedIndex() == 0) {
			lblErrorProduct.setText("Combobox Product harus dipilih.");
			isValid = false;
		}

		if (txtQty.getText() == null || txtQty.getText().length() == 0) {
			lblErrorQty.setText("Textbox Qty harus diisi.");
			isValid = false;
		}

		return isValid;
	}

	protected void doInsert() {
		pprProduct.setProductCode(cbProduct.getDataIndex().getProductCode());
		pprProduct.setQty(new BigDecimal(txtQty.getText()));
		
		Product product = new Product();
		product.setProductCode(cbProduct.getDataIndex().getProductCode());
		product.setProductName(cbProduct.getDataIndex().getProductName());
		pprProduct.setProduct(product);
		try {
			if (isEdit == false) {
				if (pprCreatePanel != null) {
					boolean isExists = false;
					
					for(PPRProduct p : pprCreatePanel.listOfPPRProduct) {
						if(pprProduct.getProductCode().equals(p.getProductCode())) {
							BigDecimal qty = new BigDecimal(txtQty.getText());
							p.setQty(p.getQty().add(qty));
							isExists = true;
							break;
						}
					}
					if(isExists == false) {
						pprCreatePanel.listOfPPRProduct.add(pprProduct);
					}
					
				} else if (pprEditPanel != null) {
					boolean isExists = false;
					
					for(PPRProduct p : pprEditPanel.listOfPPRProduct) {
						if(pprProduct.getProductCode().equals(p.getProductCode())) {
							BigDecimal qty = new BigDecimal(txtQty.getText());
							p.setQty(p.getQty().add(qty));
							isExists = true;
							break;
						}
					}
					if(isExists == false) {
						pprEditPanel.listOfPPRProduct.add(pprProduct);
					}
				}

				DialogBox.showInsert();
			} else {
				if (pprCreatePanel != null) {
					pprCreatePanel.listOfPPRProduct.set(index, pprProduct);
				} else if (pprEditPanel != null) {
					pprEditPanel.listOfPPRProduct.set(index, pprProduct);
				}

				DialogBox.showInsert();
			}

			closeDialog();
		} catch (Exception e1) {
			LOGGER.error(e1.getMessage());
			DialogBox.showErrorException();
		}
	}

	protected void closeDialog() {
		if (pprCreatePanel != null)
			pprCreatePanel.refreshTablePPRProduct();
		 else if (pprEditPanel != null)
			 pprEditPanel.refreshTablePPRProduct();

		dispose();
	}
}