package module.production.model;

import module.util.ComboBoxProperties;

public class GroupShift implements ComboBoxProperties{
	int id;
	String groupShfitCode;
	String lineCode;
	String shiftCode;
	String description;
	String employeeLeaderCode;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getGroupShfitCode() {
		return groupShfitCode;
	}
	public void setGroupShfitCode(String groupShfitCode) {
		this.groupShfitCode = groupShfitCode;
	}
	public String getLineCode() {
		return lineCode;
	}
	public void setLineCode(String lineCode) {
		this.lineCode = lineCode;
	}
	public String getShiftCode() {
		return shiftCode;
	}
	public void setShiftCode(String shiftCode) {
		this.shiftCode = shiftCode;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getEmployeeLeaderCode() {
		return employeeLeaderCode;
	}
	public void setEmployeeLeaderCode(String employeeLeaderCode) {
		this.employeeLeaderCode = employeeLeaderCode;
	}
	@Override
	public Object getField() {
		return description;
	}
	
}