package module.personalia.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import module.personalia.model.Department;
import module.personalia.model.Division;
import module.personalia.model.MSPosition;
import module.util.DateUtil;

public class MSPositionDAO {

	private Connection connection;

	private PreparedStatement getAllStatement;
	private PreparedStatement insertStatement;
	private PreparedStatement updateStatement;
	private PreparedStatement deleteStatement;

	private String getAllQuery = "select * from ms_position where delete_date is null and delete_by is null";
	private String insertQuery = "insert into ms_position (id, name, departement_id, division_id, input_date, input_by, edit_date, edit_by) values (?, ?, ?, ?, ?, ?, ?, ?)";
	private String updateQuery = "update ms_position set name = ?, departement_id = ?, division_id = ?, edit_date = ?, edit_by = ? where id = ?";
	private String deleteQuery = "update ms_position set delete_date = ?, delete_by = ? where id = ?";
	
	public Division getDivision(String id) {
		String query = "select * from division where id = ";
		Division division = null;
		try {
			PreparedStatement selectDivision = connection.prepareStatement(query+id);
			
			ResultSet resultSet = selectDivision.executeQuery();
			
			while (resultSet.next()) {
				division = new Division();
				division.setId(resultSet.getString("id"));
				division.setName(resultSet.getString("name"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return division;
	}

	public Department getDepartement(String id) {
		String query = "select * from departement where id = ";
		Department departement = null;
		try {
			PreparedStatement selectDepartement = connection.prepareStatement(query+id);
			
			ResultSet resultSet = selectDepartement.executeQuery();
			
			while (resultSet.next()) {
				departement = new Department();
				departement.setId(resultSet.getString("id"));
				departement.setName(resultSet.getString("name"));
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return departement;
	}

	public MSPositionDAO(Connection connection) {
		this.connection = connection;
	}
	
	public List<MSPosition> getAllData(String query) {
		List<MSPosition> msPositions = new ArrayList<>();
		try {
			getAllStatement = connection.prepareStatement(getAllQuery+query);

			ResultSet resultSet = getAllStatement.executeQuery();

			while (resultSet.next()) {
				MSPosition msPosition = new MSPosition();
				msPosition.setId(resultSet.getString("id"));
				msPosition.setName(resultSet.getString("name"));
				msPosition.setDepartementId(resultSet.getString("departemen_id"));
				msPosition.setDepartementName(getDepartement(msPosition.getDepartementId()).getName());
				msPosition.setDivisionId(resultSet.getString("division_id"));
				msPosition.setDivisionName(getDivision(msPosition.getDivisionId()).getName());

				msPositions.add(msPosition);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return msPositions;
	}
	
	public void insert(MSPosition msPosition) {
		try {
			insertStatement = connection.prepareStatement(insertQuery);

			insertStatement.setString(1, msPosition.getId());
			insertStatement.setString(2, msPosition.getName());
			insertStatement.setString(3, msPosition.getDepartementId());
			insertStatement.setString(4, msPosition.getDivisionId());
			insertStatement.setDate(5, DateUtil.toDate(msPosition.getInputDate()));
			insertStatement.setString(6, msPosition.getInputBy());
			insertStatement.setDate(7, DateUtil.toDate(msPosition.getEditDate()));
			insertStatement.setString(8, msPosition.getEditBy());

			insertStatement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void update(MSPosition msPosition) {
		try {
			updateStatement = connection.prepareStatement(updateQuery);

			updateStatement.setString(1, msPosition.getName());
			updateStatement.setString(2, msPosition.getDepartementId());
			updateStatement.setString(3, msPosition.getDivisionId());
			updateStatement.setDate(4, DateUtil.toDate(msPosition.getEditDate()));
			updateStatement.setString(5, msPosition.getEditBy());
			updateStatement.setString(6, msPosition.getId());

			updateStatement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void delete(MSPosition msPosition) {
		try {
			deleteStatement = connection.prepareStatement(deleteQuery);

			deleteStatement.setDate(1, DateUtil.toDate(msPosition.getDeleteDate()));
			deleteStatement.setString(2, msPosition.getDeleteBy());
			deleteStatement.setString(3, msPosition.getId());

			deleteStatement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public Integer getLastId() {
		return getAllData("").size()+1;
	}
}