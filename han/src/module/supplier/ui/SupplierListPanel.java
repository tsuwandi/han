package module.supplier.ui;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.table.AbstractTableModel;

import controller.DataSourceFactory;
import controller.ServiceFactory;
import main.component.DialogBox;
import main.panel.MainPanel;
import module.supplier.model.Supplier;
import module.util.JTextFieldLimit;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JRDesignQuery;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;

public class SupplierListPanel extends JPanel {

	JButton btnCreateNew;
	JButton btnExport;
	JButton btnAdvancedSearch;
	JButton btnSearch;

	JTextField txtSearch;

	JLabel lblBreadcrumb;
	JLabel lblHeader;

	JScrollPane scrollPaneSupplier;

	private SupplierTableModel supplierTableModel;
	public List<Supplier> listOfSupplier = new ArrayList<Supplier>();

	JTable tblSupplier;

	private SupplierListPanel supplierListPanel;

	private static final long serialVersionUID = 1L;

	public SupplierListPanel() {
		supplierListPanel = this;
		setLayout(null);

		setPreferredSize(new Dimension(1024, 768));

		lblBreadcrumb = new JLabel("ERP > Pembelian");
		lblBreadcrumb.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblBreadcrumb.setBounds(50, 10, 320, 30);
		add(lblBreadcrumb);

		lblHeader = new JLabel("SUPPLIER");
		lblHeader.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblHeader.setBounds(50, 45, 320, 30);
		add(lblHeader);

		btnCreateNew = new JButton("Buat Baru");
		btnCreateNew.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				MainPanel.changePanel("module.supplier.ui.SupplierCreatePanel");
			}
		});
		btnCreateNew.setBounds(700, 80, 100, 30);
		add(btnCreateNew);

		btnExport = new JButton("Export");
		btnExport.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

			}
		});
		btnExport.setBounds(800, 80, 100, 30);
		add(btnExport);

		btnAdvancedSearch = new JButton("Pencarian Lanjut");
		btnAdvancedSearch.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				showAdvancedSearchDialog(supplierListPanel);
			}
		});
		btnAdvancedSearch.setBounds(900, 80, 150, 30);
		add(btnAdvancedSearch);
		
//		JButton btnPrint = new JButton("Print");
//		btnPrint.addActionListener(new ActionListener() {
//			public void actionPerformed(ActionEvent arg0) {
//				try {
//					java.sql.Connection conn = DataSourceFactory.getDataSource().getConnection();
//					JasperDesign jDesign = JRXmlLoader.load("src/module/pembelian/report/PurchaseReport.jrxml");
//					String sql = "SELECT received_code, received_date, rit_no, license_plate, driver, delivery_note FROM received order by id desc";
//					JRDesignQuery jDesignQuery = new JRDesignQuery();		
//					jDesignQuery.setText(sql);
//					jDesign.setQuery(jDesignQuery);
//					JasperReport jreprt = JasperCompileManager.compileReport(jDesign);
//					JasperPrint jprintt = JasperFillManager.fillReport(jreprt, null, conn);
//					JasperViewer.viewReport(jprintt, false);
//
////					 String namafile= "src/module/pembelian/report/PurchaseReport.jasper"; 
////					    File report = new File(namafile);
////					    JasperReport jreprt = (JasperReport)JRLoader.loadObject(report.getPath());
////					    JasperPrint jprintt = JasperFillManager.fillReport(jreprt,null,conn);
////					    JasperViewer.viewReport(jprintt,false);
//				} catch (Exception e) {
//					e.printStackTrace();
//					JOptionPane.showMessageDialog(null, "Gagal Membuka Laporan", "Cetak Laporan",
//							JOptionPane.ERROR_MESSAGE);
//				}
//
//			}
//		});
//		btnPrint.setBounds(50, 104, 89, 23);
//		add(btnPrint);

		txtSearch = new JTextField();
		txtSearch.setBounds(800, 131, 150, 28);
		add(txtSearch);

		btnSearch = new JButton("Cari");
		btnSearch.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				doSearch(txtSearch.getText());
			}
		});
		btnSearch.setBounds(950, 130, 100, 30);
		add(btnSearch);

		scrollPaneSupplier = new JScrollPane();
		scrollPaneSupplier.setBounds(50, 200, 1000, 300);
		add(scrollPaneSupplier);

		supplierTableModel = new SupplierTableModel(new ArrayList<Supplier>());
		tblSupplier = new JTable(supplierTableModel);
		tblSupplier.setFocusable(false);
		scrollPaneSupplier.setViewportView(tblSupplier);

		tblSupplier.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2) {
					JTable target = (JTable) e.getSource();
					int row = target.getSelectedRow();
					int column = target.getSelectedColumn();

					if (column == 3)
						MainPanel.changePanel("module.supplier.ui.SupplierViewPanel", listOfSupplier.get(row));
				}
			}
		});

		try {
			listOfSupplier = new ArrayList<Supplier>();
			listOfSupplier = ServiceFactory.getSupplierBL().getAllSupplier();
			refreshTableSupplier();
		} catch (SQLException e1) {
			e1.printStackTrace();
			DialogBox.showErrorException();
		}
		
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				btnCreateNew.requestFocusInWindow();
			}
		});

	}

	public void refreshTableSupplier() {
		try {
			tblSupplier.setModel(new SupplierTableModel(listOfSupplier));
		} catch (Exception e1) {
			e1.printStackTrace();
			DialogBox.showErrorException();
		}
	}

	public void doSearch(String value) {
		try {
			listOfSupplier = new ArrayList<Supplier>();
			listOfSupplier = ServiceFactory.getSupplierBL().getAllSupplierBySimpleSearch(value);
			refreshTableSupplier();
		} catch (SQLException e1) {
			e1.printStackTrace();
			DialogBox.showErrorException();
		}
	}

	/**
	 * Method to display add supp cp dialog
	 */
	protected void showAdvancedSearchDialog(SupplierListPanel supplierListPanel) {
		SupplierAdvSearchDialog suppAdvSearchDialog = new SupplierAdvSearchDialog(supplierListPanel);
		suppAdvSearchDialog.setTitle("Advanced Search");
		suppAdvSearchDialog.setLocationRelativeTo(null);
		suppAdvSearchDialog.setVisible(true);
	}

	/**
	 * Class as TableModel for Supplier table
	 * 
	 * @author TSI
	 *
	 */
	class SupplierTableModel extends AbstractTableModel {

		private static final long serialVersionUID = 1L;

		private List<Supplier> listOfSupplier;

		public SupplierTableModel(List<Supplier> listOfSupplier) {
			this.listOfSupplier = listOfSupplier;
		}

		/**
		 * Method to get row count
		 * 
		 * @return int
		 */
		public int getRowCount() {
			return listOfSupplier.size();
		}

		/**
		 * Method to get Column Count
		 */
		public int getColumnCount() {
			return 4;
		}

		public boolean isCellEditable(int row, int column) {
			return false;
		}

		public Class getColumnClass(int column) {
			switch (column) {
			case 0:
				return String.class;
			case 1:
				return String.class;
			case 2:
				return String.class;
			default:
				return String.class;
			}
		}

		/**
		 * Method to get selected value
		 * 
		 * @param rowIndex
		 *            rowIndex of selected table
		 * @param columnIndex
		 *            columnIndex of selected table
		 * @return ({@link SupplierAddress}) Object
		 */
		public Object getValueAt(int rowIndex, int columnIndex) {
			Supplier p = listOfSupplier.get(rowIndex);
			switch (columnIndex) {
			case 0:
				return p.getSuppCode();
			case 1:
				return p.getSuppName();
			case 2:
				return p.getSuppType().getSuppType();
			case 3:
				return "<html><a><u>View</u></a></html>";
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
				return "Kode Supplier";
			case 1:
				return "Nama Supplier";
			case 2:
				return "Tipe Supplier";
			case 3:
				return "Tindakan";
			default:
				return "";
			}
		}
	}
}
