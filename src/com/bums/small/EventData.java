package com.bums.small;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.text.format.DateFormat;

public class EventData implements Serializable, Comparable<Object> {
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
		else if (organization.equals(SCAN))
			setImage(R.drawable.scan);
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

	public String getDateFrom() {
		return dateFrom;
	}

	public String getRegularDateFrom() {
		return sqlDateToRegularDate(dateFrom);
	}

	public void setDate(String date) {
		this.dateFrom = date;
	}

	public String getTimeFrom() {
		return timeFrom;
	}

	public String getRegularTimeFrom() {
		return sqlTimeToRegularTime(timeFrom);
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

	public String getRegularDateTo() {
		return sqlDateToRegularDate(dateTo);
	}

	public void setDateTo(String dateTo) {
		this.dateTo = dateTo;
	}

	public String getTimeTo() {
		return timeTo;
	}

	public String getRegularTimeTo() {
		return sqlTimeToRegularTime(timeTo);
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

	public String sqlDateToRegularDate(String strDate) {
		SimpleDateFormat inputFormatter = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat outputFormatter = new SimpleDateFormat("MM-dd-yyyy");
		Date da;
		String strDateTime = null;
		try {
			da = (Date)inputFormatter.parse(strDate);
			strDateTime = outputFormatter.format(da);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return strDateTime;
	}

	public String sqlTimeToRegularTime(String strTime) {
		int hour = 0;
		//int hour2 = 0;

		String minute = strTime.substring(3, 5);

		try {
			hour = Integer.valueOf(strTime.substring(0, 2));
			//hour2 = Integer.valueOf(strTime.substring(1, 2));
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}


		if (hour == 0) {
			strTime = 12 + ":" + minute + "am";
		} else if (hour == 12) {
			strTime = 12 + ":" + minute + "pm";
		} else if (hour > 12) {
			strTime = hour - 12 + ":" + minute + "pm";
		} else {
			strTime = hour + ":" + minute + "am";
		}
		return strTime;
	}

	public int compareTo(Object o) {
	    SimpleDateFormat formatter;
	    Date date1 = null;
	    Date date2 = null;  
	    EventData other = (EventData) o;

	    formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	    try {
	        date1 = (Date) formatter.parse(this.dateFrom + " " + this.timeFrom);
	        date2 = (Date) formatter.parse(other.dateFrom + " " + other.timeFrom);
	    } catch (ParseException e) {
	        e.printStackTrace();
	    }
	    catch(NullPointerException npe){
	        System.out.println("Exception thrown "+npe.getMessage()+" date1 is "+date1+" date2 is "+date2);
	    }

	    return date2.compareTo(date1);

	}
}
