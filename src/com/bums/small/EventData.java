package com.bums.small;

import java.io.Serializable;

public class EventData implements Serializable {
	private static final long serialVersionUID = -9175628155796554906L;
	private int image;
	private String imageLocation;
	private String title;
	private String location;
	private String description;
	private String dateFrom;
	private String dateTo;
	private String timeFrom;
	private String timeTo;
	private String organization;
	private String department;

	public static final String BINHI = "Binhi";
	public static final String KADIWA = "Kadiwa";
	public static final String BUKLOD = "Buklod";
	public static final String CHOIR = "Choir";
	public static final String OVERSEER = "Overseer";
	public static final String SCAN = "SCAN-I";
	public static final String SECRETARY = "Secretary";
	public static final String FINANCE = "Finance";
	public static final String LOS = "Light of Salvation";
	public static final String CFO = "Christian Family Organization";
	public static final String WS = "Worship Service";
	
	public EventData(String title, String location, String description,
			String dateFrom, String dateTo, String timeFrom, String timeTo, String organization) {
		this.title = title;
		this.location = location;
		this.description = description;
		this.dateFrom = dateFrom;
		this.dateTo = dateTo;
		this.timeFrom = timeFrom;
		this.timeTo = timeTo;
		this.organization = organization;
		
		if (organization.equals(BINHI))
			setImage(R.drawable.binhi);
		else if (organization.equals(KADIWA))
			setImage(R.drawable.kadiwa);
		else if (organization.equals(BUKLOD))
			setImage(R.drawable.buklod);
		else if (organization.equals(CHOIR))
			setImage(R.drawable.choir);
		else if (organization.equals(OVERSEER))
			setImage(R.drawable.overseer);
		else if (organization.equals(SECRETARY))
			setImage(R.drawable.secretary);
		else if (organization.equals(FINANCE))
			setImage(R.drawable.finance);
		else if (organization.equals(LOS))
			setImage(R.drawable.los);
		else if (organization.equals(CFO))
			setImage(R.drawable.cfo);
		else if (organization.equals(WS))
			setImage(R.drawable.ws);
		
		setDepartment(getDepartment(organization));
	}
	
	private String getDepartment(String group) {
		String theGroup = "";
		if (group.equals("Buklod") || group.equals("Kadiwa") || group.equals("Binhi")) 
			theGroup = "Christian Family Organization"; 
		else if (group.equals("Overseer") || group.equals("Choir") || group.equals("Finance") 
				|| group.equals("SCAN-I") || group.equals("Secretary") || group.equals("Leadership") || group.equals(WS)) 
			theGroup = "Worship Service";
		else if (group.equals("Missionizer") || group.equals(LOS))
			theGroup = "Light of Salvation";
		
		return theGroup;
	}

	public int getImage() {
		return image;
	}

	public void setImage(int image) {
		this.image = image;
	}

	public String getImageLocation() {
		return imageLocation;
	}

	public void setImageLocation(String imageLocation) {
		this.imageLocation = imageLocation;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getDate() {
		return dateFrom;
	}

	public void setDate(String date) {
		this.dateFrom = date;
	}

	public String getTime() {
		return timeFrom;
	}

	public void setTime(String time) {
		this.timeFrom = time;
	}

	public String getOrganization() {
		return organization;
	}

	public void setOrganization(String organization) {
		this.organization = organization;
	}
	
	public String getDateTo() {
		return dateTo;
	}

	public void setDateTo(String dateTo) {
		this.dateTo = dateTo;
	}

	public String getTimeTo() {
		return timeTo;
	}

	public void setTimeTo(String timeTo) {
		this.timeTo = timeTo;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}
}
