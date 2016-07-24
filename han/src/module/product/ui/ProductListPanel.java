package module.product.ui;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;

import controller.DaoFactory;
import controller.ServiceFactory;
import main.component.DialogBox;
import main.panel.MainPanel;
import module.product.model.Product;
import module.supplier.model.Supplier;

public class ProductListPanel extends JPanel {

	/**
	 * Create the panel.
	 */
	private JLabel titleLbl;
	private JButton createNewBtn;
	private JButton exportBtn;
	private JButton advanceSearchBtn;
	private JButton searchBtn;

	private JTextField searchField;
	private JTable productTable;
	
	private ProductTableModel productTableModel;
	private JScrollPane scrollPane;
	public List<Product> products = null;
	
	public ProductListPanel() {
		setLayout(null);
		
		if(products!=null){
			refreshTable();
		}
		
//		titleLbl = new JLabel("PRODUK");
//		titleLbl.setFont(new Font("", Font.BOLD, 24));
//		titleLbl.setBounds(20, 20, 200, 50);
		
		JLabel lblBreadcrumb = new JLabel("ERP > Produk");
		lblBreadcrumb.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblBreadcrumb.setBounds(50, 10, 320, 30);
		add(lblBreadcrumb);

		JLabel lblHeader = new JLabel("PRODUK");
		lblHeader.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblHeader.setBounds(50, 45, 320, 30);
		add(lblHeader);

		createNewBtn = new JButton("Buat Baru");
		createNewBtn.setBounds(700, 80, 100, 30);
		createNewBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				MainPanel.changePanel("module.product.ui.CreateProductPanel");
			}
		});

		exportBtn = new JButton("Export");
		exportBtn.setBounds(800, 80, 100, 30);

		advanceSearchBtn = new JButton("Pencarian Lanjut");
		advanceSearchBtn.setBounds(900, 80, 150, 30);
		advanceSearchBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				//MainPanel.changePanel("module.product.ui.EmployeeSearchPanel");

			}
		});

		searchBtn = new JButton("Cari");
		searchBtn.setBounds(950, 130, 100, 30);
		searchBtn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				doSearch(searchField.getText());
			}
		});

		searchField = new JTextField();
		searchField.setBounds(800, 131, 150, 28);

		try{
			products = ServiceFactory.getProductBL().getAll();
		}catch(SQLException e1){
			e1.printStackTrace();
		}
		
		//System.out.println(products.size());
		productTableModel = new ProductTableModel(products);
		productTable = new JTable(productTableModel);
//		productTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
//		productTable.getTableHeader().setReorderingAllowed(false);
//		productTable.getTableHeader().setResizingAllowed(false);
//		productTable.getColumnModel().getColumn(0).setPreferredWidth(30);
//		productTable.getColumnModel().getColumn(1).setPreferredWidth(120);
//		productTable.getColumnModel().getColumn(2).setPreferredWidth(170);
//		productTable.getColumnModel().getColumn(3).setPreferredWidth(170);
//		productTable.getColumnModel().getColumn(4).setPreferredWidth(130);
//		productTable.getColumnModel().getColumn(5).setPreferredWidth(140);

		DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
		centerRenderer.setHorizontalAlignment( JLabel.CENTER );
		productTable.getColumnModel().getColumn(4).setCellRenderer(centerRenderer);

		productTable.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2) {
					JTable target = (JTable)e.getSource();
					int row = target.getSelectedRow();
					int column = target.getSelectedColumn();
					Product product;
					// do some action if appropriate column
					if(column == 4){
						product = new Product();
						
						product.setProductCode(productTable.getValueAt(row, 1).toString());

						MainPanel.changePanel("module.product.ui.ProductViewPanel", product);
					}
				}
			}
		});

		scrollPane = new JScrollPane(productTable);
		//scrollPane.setBounds(20, 200, 1130, 265);
		scrollPane.setBounds(50, 200, 1000, 300);
		//scrollPane.setBorder(new EmptyBorder(0, 0, 0, 0));

		//add(titleLbl);
		add(createNewBtn);
		add(exportBtn);
		add(advanceSearchBtn);
		add(searchField);
		add(searchBtn);
		add(scrollPane);
	}
	
	public void refreshTable() {
		try {
			productTable.setModel(new ProductTableModel(products));
		} catch (Exception e1) {
			e1.printStackTrace();
			DialogBox.showErrorException();
		}
	}
	
	public void doSearch(String value) {
		try {
			products = new ArrayList<Product>();
			products = ServiceFactory.getProductBL().getSearchProduct(value);
			refreshTable();
//			productTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
//			productTable.getTableHeader().setReorderingAllowed(false);
//			productTable.getTableHeader().setResizingAllowed(false);
//			productTable.getColumnModel().getColumn(0).setPreferredWidth(30);
//			productTable.getColumnModel().getColumn(1).setPreferredWidth(120);
//			productTable.getColumnModel().getColumn(2).setPreferredWidth(170);
//			productTable.getColumnModel().getColumn(3).setPreferredWidth(170);
//			productTable.getColumnModel().getColumn(4).setPreferredWidth(130);
//			productTable.getColumnModel().getColumn(5).setPreferredWidth(140);
		} catch (SQLException e1) {
			e1.printStackTrace();
			DialogBox.showErrorException();
		}
	}

	class ProductTableModel extends AbstractTableModel {
		private static final long serialVersionUID = 1L;

		private List<Product> products;
		int seq=0;

		public ProductTableModel(List<Product> products) {
			this.products = products;
		}

		/**
		 * Method to get row count
		 * 
		 * @return int
		 */
		public int getRowCount() {
			return products.size();
		}

		/**
		 * Method to get Column Count
		 */
		public int getColumnCount() {
			return 5;
		}

		/**
		 * Method to get selected value
		 * 
		 * @param rowIndex
		 *            rowIndex of selected table
		 * @param columnIndex
		 *            columnIndex of selected table
		 * @return ({@link Supplier}) Object
		 */
		public Object getValueAt(int rowIndex, int columnIndex) {
			Product p = products.get(rowIndex);
			switch (columnIndex) {
			case 0:
				return p.getProductCode();
			case 1:
				return p.getProductName();
			case 2:
				return p.getProductCat();
			case 3: 
				return p.getProductStat();
			case 4:
				return "<html><u>View</u></html>";
			default:
				return "";
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
				return "Kategori Produk";
			case 3:
				return "Status Produk";
			case 4:
				return "Action";
			default:
				return "";
			}
		}
		@Override
		public boolean isCellEditable(int row, int column) {
			//all cells false
			return false;
		}

	}

}
