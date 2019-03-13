package com.ktrental.model;

public class NoticeModel {
	private String title;
	private String day;
	private String author;

	public NoticeModel() {
		super();
		this.title = "";
		this.day = "";
		this.author = "";
	}
	
	public NoticeModel(String title, String day, String author) {
		super();
		this.title = title;
		this.day = day;
		this.author = author;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDay() {
		return day;
	}

	public void setDay(String day) {
		this.day = day;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

}
