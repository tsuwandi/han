package module.purchaseprodresult.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import module.purchaseprodresult.model.PurchaseProdResult;
import module.sn.currency.model.Currency;
import module.supplier.model.Supplier;
import module.util.DateUtil;

public class PurchaseProdResultDAO {
	private Connection connection;
	
	private PreparedStatement getAllStatement;
	private PreparedStatement getOrdinalOfCodeNumberStatement;
	private PreparedStatement isPPRCodeExistsStatement;
	private PreparedStatement insertStatement;
	private PreparedStatement updateStatement;
	private PreparedStatement deleteStatement;
	
	private String getAllQuery = new StringBuilder().append("select p.id, p.ppr_code, p.supp_code, ")
			.append("p.purchase_date, p.due_date, p.status, ")
			.append("s.id as supp_id, s.supp_name from purchase_prod_result p ")
			.append("inner join supplier s on p.supp_code = s.supp_code ")
			.append("where s.deleted_date is null and p.deleted_date is null ").toString();
	
	private String isPPRCodeExistsQuery = "select count(*) as is_exists from purchase_prod_result where ppr_code = ? and deleted_date is null ";

	private String insertQuery = new StringBuilder().append("insert into purchase_prod_result (ppr_code, supp_code, purchase_date, due_date, ")
			.append(" status, input_date, input_by) ")
			.append(" values (?,?,?,?,?,?,?)").toString();

	private String updateQuery = new StringBuilder().append("update purchase_prod_result set supp_code=?, purchase_date=?, ")
			.append("due_date=?, status=?, ")
			.append("edit_date=?, edited_by=? where ppr_code=?").toString();

	private String deleteQuery = "update purchase_prod_result set deleted_date=?, deleted_by=? where id=?";
	
	private String getOrdinalOfCodeNumberQuery = "SELECT SUBSTRING_INDEX(ppr_code, '/', 1) AS ordinal FROM purchase_prod_result "
			+ "WHERE SUBSTRING_INDEX(ppr_code, '/', -1) = ? "
			+ "ORDER BY ordinal DESC LIMIT 1 ";
	
	public PurchaseProdResultDAO(Connection connection) throws SQLException {
		this.connection = connection;
	}

	public List<PurchaseProdResult> getAll() throws SQLException {
		List<PurchaseProdResult> pprs = new ArrayList<PurchaseProdResult>();

		try {
			getAllStatement = connection.prepareStatement(getAllQuery);

			ResultSet rs = getAllStatement.executeQuery();
			while (rs.next()) {
				PurchaseProdResult ppr = new PurchaseProdResult();
				ppr.setId(rs.getInt("id"));
				ppr.setPprCode(rs.getString("ppr_code"));
				ppr.setSuppCode(rs.getString("supp_code"));
				ppr.setPurchaseDate(rs.getDate("purchase_date"));
				ppr.setDueDate(rs.getDate("due_date"));
				ppr.setStatus(rs.getString("status"));
				
				Supplier supplier = new Supplier();
				supplier.setId(rs.getInt("supp_id"));
				supplier.setSuppCode(rs.getString("supp_code"));
				supplier.setSuppName(rs.getString("supp_name"));
				
				ppr.setSupplier(supplier);

				pprs.add(ppr);
			}

		} catch (SQLException ex) {
			ex.printStackTrace();
			throw new SQLException(ex.getMessage());
		}

		return pprs;
	}
	
	public List<PurchaseProdResult> getAllBySimpleSearch(String value) throws SQLException {
		List<PurchaseProdResult> pprs = new ArrayList<PurchaseProdResult>();
		try {
			if (null != value && !"".equals(value)) {
				String keyword = new StringBuilder().append("%").append(value).append("%").toString();
				String query = new StringBuilder().append(getAllQuery).append(" and")
						.append(" (lower(p.ppr_code) like lower('%s')")
						.append(" or lower(p.supp_code) like lower('%s')")
						.append(" or lower(s.supp_name) like lower('%s')")
						.append(" or lower(p.status) like lower('%s'))").toString();
				getAllStatement = connection.prepareStatement(String.format(query, keyword, keyword, keyword, keyword));
			} else {
				getAllStatement = connection.prepareStatement(getAllQuery);
			}

			ResultSet rs = getAllStatement.executeQuery();
			while (rs.next()) {
				PurchaseProdResult ppr = new PurchaseProdResult();
				ppr.setId(rs.getInt("id"));
				ppr.setPprCode(rs.getString("ppr_code"));
				ppr.setSuppCode(rs.getString("supp_code"));
				ppr.setPurchaseDate(rs.getDate("purchase_date"));
				ppr.setDueDate(rs.getDate("due_date"));
				ppr.setStatus(rs.getString("status"));
				
				Supplier supplier = new Supplier();
				supplier.setId(rs.getInt("supp_id"));
				supplier.setSuppCode(rs.getString("supp_code"));
				supplier.setSuppName(rs.getString("supp_name"));
				
				Currency currency = new Currency();
				currency.setId(rs.getInt("currency_id"));
				currency.setCurrency(rs.getString("currency"));

				ppr.setSupplier(supplier);

				pprs.add(ppr);
			}
		} catch (SQLException ex) {
			throw new SQLException(ex.getMessage());
		}

		return pprs;
	}
	
	public int isPPRCodeExists(String pprCode) throws SQLException {
		int count = 0;
		try {
			isPPRCodeExistsStatement = connection.prepareStatement(isPPRCodeExistsQuery);
			isPPRCodeExistsStatement.setString(1, pprCode);

			ResultSet rs = isPPRCodeExistsStatement.executeQuery();

			while (rs.next()) {
				count = rs.getInt("is_exists");
			}

		} catch (SQLException ex) {
			throw new SQLException(ex.getMessage());
		}

		return count;
	}
	
	public void save(PurchaseProdResult ppr) throws SQLException {
		try {
			insertStatement = connection.prepareStatement(insertQuery);
			insertStatement.setString(1, ppr.getPprCode());
			insertStatement.setString(2, ppr.getSuppCode());
			insertStatement.setDate(3, DateUtil.toDate(ppr.getPurchaseDate()));
			insertStatement.setDate(4, DateUtil.toDate(ppr.getDueDate()));
			insertStatement.setString(5, ppr.getStatus());
			insertStatement.setDate(6, DateUtil.getCurrentDate());
			insertStatement.setString(7, "timotius");
			insertStatement.executeUpdate();
			
		} catch (SQLException ex) {
			ex.getCause();
			throw new SQLException(ex.getMessage());
		}
	}
	
	public void update(PurchaseProdResult ppr) throws SQLException {
		try {
			updateStatement = connection.prepareStatement(updateQuery);
			updateStatement.setString(1, ppr.getSuppCode());
			updateStatement.setDate(2, DateUtil.toDate(ppr.getPurchaseDate()));
			updateStatement.setDate(3, DateUtil.toDate(ppr.getDueDate()));
			updateStatement.setString(4, ppr.getStatus());
			updateStatement.setDate(5, DateUtil.getCurrentDate());
			updateStatement.setString(6, "timotius");
			updateStatement.setString(7, ppr.getPprCode());
			updateStatement.executeUpdate();

		} catch (SQLException ex) {
			throw new SQLException(ex.getMessage());
		}
	}
	
	public void delete(int id) throws SQLException {
		try {
			deleteStatement = connection.prepareStatement(deleteQuery);
			deleteStatement.setDate(1, DateUtil.getCurrentDate());
			deleteStatement.setString(2, "timotius");
			deleteStatement.setInt(3, id);
			deleteStatement.executeUpdate();
		} catch (SQLException ex) {
			throw new SQLException(ex.getMessage());
		}
	}
	
	public PurchaseProdResult getById(int id) throws SQLException {
		PurchaseProdResult ppr = null;
		String query = new StringBuilder().append(getAllQuery).append(" and p.id=?").toString();
		try {
			getAllStatement = connection.prepareStatement(query);
			getAllStatement.setInt(1, id);
			ResultSet rs = getAllStatement.executeQuery();

			while (rs.next()) {
				ppr = new PurchaseProdResult();
				ppr.setId(rs.getInt("id"));
				ppr.setPprCode(rs.getString("ppr_code"));
				ppr.setSuppCode(rs.getString("supp_code"));
				ppr.setPurchaseDate(rs.getDate("purchase_date"));
				ppr.setDueDate(rs.getDate("due_date"));
				ppr.setStatus(rs.getString("status"));
				
				Supplier supplier = new Supplier();
				supplier.setId(rs.getInt("supp_id"));
				supplier.setSuppCode(rs.getString("supp_code"));
				supplier.setSuppName(rs.getString("supp_name"));

				ppr.setSupplier(supplier);
			}

		} catch (SQLException ex) {
			throw new SQLException(ex.getMessage());
		}

		return ppr;
	}
	
	public int getOrdinalOfCodeNumberByYear(int year) throws SQLException {
		int ordinal = 0;
		try {
			getOrdinalOfCodeNumberStatement = connection.prepareStatement(getOrdinalOfCodeNumberQuery);
			getOrdinalOfCodeNumberStatement.setInt(1, year);

			ResultSet rs = getOrdinalOfCodeNumberStatement.executeQuery();
			while (rs.next()) {
				ordinal = rs.getInt("ordinal");
			}

		} catch (SQLException ex) {
			throw new SQLException(ex.getMessage());
		}

		return ordinal;
	}
}
