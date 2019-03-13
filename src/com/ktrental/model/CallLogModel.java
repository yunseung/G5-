package com.ktrental.model;

public class CallLogModel {
	private String date;
	private String time;
	private String type;
	private String tel;

	public CallLogModel(String date, String time, String type, String tel) {
		super();
		this.date = date;
		this.time = time;
		this.type = type;
		this.tel = tel;
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

	public String getType() {
		return type;
	}

	public void setType(String _type) {
		this.type = _type;
	}

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

}
