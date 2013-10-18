package com.bums.small;

public class DepartmentData {
	private String organization;
	private String department;
	
	public DepartmentData(String organization, String department) {
		this.organization = organization;
		this.department = department;
	}

	public String getOrganization() {
		return organization;
	}

	public void setOrganization(String organization) {
		this.organization = organization;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}
}
