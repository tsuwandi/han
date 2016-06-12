package module.pembelian.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import module.pembelian.model.SupplierCP;

public class SuppCPDAO {
	private DataSource dataSource;

	private PreparedStatement getAllStatement;

	private String getAllQuery = "SELECT a.id, a.supp_code, a.supp_address_id, b.address, a.name, a.department, a.phone, a.email "
			+ "FROM supp_cp a INNER JOIN supp_address b ON a.supp_address_id = b.id WHERE 1 = 1 ";

	public SuppCPDAO(DataSource dataSource) throws SQLException {
		this.dataSource = dataSource;

	}

	public List<SupplierCP> getSuppCPBySupplier( String suppCode) throws SQLException {
		Connection con = null;
		ArrayList<SupplierCP> suppCps = new ArrayList<SupplierCP>();

		try {
			con = dataSource.getConnection();
			getAllStatement = con.prepareStatement(getAllQuery+" AND a.supp_code = ?");
			getAllStatement.setString(1, suppCode);

			ResultSet rs = getAllStatement.executeQuery();
			while (rs.next()) {
				SupplierCP suppCp = new SupplierCP();
				suppCp.setId(rs.getInt("id"));
				suppCp.setSuppCode(rs.getString("supp_code"));
				suppCp.setSuppAddressId(rs.getInt("supp_address_id"));
				suppCp.setSuppAddress(rs.getString("address"));
				suppCp.setName(rs.getString("name"));
				suppCp.setDepartment(rs.getString("department"));
				suppCp.setPhone(rs.getString("phone"));
				suppCp.setEmail(rs.getString("email"));
				suppCps.add(suppCp);
				
			}

		} catch (SQLException ex) {
			ex.printStackTrace();
			throw new SQLException(ex.getMessage());
		} finally {
			try {
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return suppCps;
	}
}
