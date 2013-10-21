package com.bums.small;

import android.graphics.Bitmap;

public class EventData {
	private int image;
	private String imageLocation;
	private String title;
	private String location;
	private String date;
	private String time;
	private int organization;

	public static final int BINHI = 0;
	public static final int KADIWA = 1;
	public static final int BUKLOD = 2;
	public static final int CHOIR = 3;
	public static final int OVERSEER = 4;
	public static final int SCAN = 5;
	public static final int SECRETARY = 6;
	public static final int FINANCE = 7;
	public static final int LOS = 8;
	public static final int CFO = 9;
	public static final int WS = 10;

	public EventData(String title, String location, String date, String time) {
		this.title = title;
		this.location = location;
		this.date = date;
		this.time = time;
	}

	public EventData(String title, String location, String date, String time, int organization) {
		this.title = title;
		this.location = location;
		this.date = date;
		this.time = time;
		this.organization = organization;

		switch (organization) {
		case BINHI:
			setImage(R.drawable.binhi);
			break;
		case KADIWA:
			setImage(R.drawable.kadiwa);
			break;
		case BUKLOD:
			setImage(R.drawable.buklod);
			break;
		case CHOIR:
			setImage(R.drawable.choir);
			break;
		case OVERSEER:
			setImage(R.drawable.overseer);
			break;
		case SECRETARY:
			setImage(R.drawable.secretary);
			break;
		case FINANCE:
			setImage(R.drawable.finance);
			break;
		case LOS:
			setImage(R.drawable.los);
			break;
		case CFO:
			setImage(R.drawable.cfo);
			break;
		case WS:
			setImage(R.drawable.ws);
			break;
		}
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
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public int getOrganization() {
		return organization;
	}

	public void setOrganization(int organization) {
		this.organization = organization;
	}
}
