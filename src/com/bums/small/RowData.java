package com.bums.small;

import android.graphics.Bitmap;

public class RowData {
	
	private Bitmap image;
	private String username;
	private String caption;
	private String link;

	public RowData(Bitmap image, String username, String caption, String link) {
		this.image = image;
		this.username = username;
		this.caption = caption;
		this.link = link;
	}
	
	public RowData() {

	}
	
	public String getLink() {
		return link;
	}
	
	public void setLink(String link) {
		this.link = link;
	}

	public Bitmap getImage() {
		return image;
	}

	public void setImage(Bitmap image) {
		this.image = image;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getCaption() {
		return caption;
	}

	public void setCaption(String caption) {
		this.caption = caption;
	}
}
