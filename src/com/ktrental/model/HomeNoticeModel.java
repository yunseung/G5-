package com.ktrental.model;

public class HomeNoticeModel {
	private String title;
	private String date;

	public HomeNoticeModel(String title, String date) {
		super();
		this.title = title;
		this.date = date;
	}


	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

}
