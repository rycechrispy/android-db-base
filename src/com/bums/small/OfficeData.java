package com.bums.small;

public class OfficeData {
	private String officeType;
	private String isLeader;
	
	public OfficeData(String officeType, String isLeader) {
		this.officeType = officeType;
		this.isLeader = isLeader;
	}

	public String getOfficeType() {
		return officeType;
	}

	public void setOfficeType(String officeType) {
		this.officeType = officeType;
	}

	public String getIsLeader() {
		return isLeader;
	}

	public void setIsLeader(String isLeader) {
		this.isLeader = isLeader;
	}
}
