package com.ktrental.model;

public class MaintenanceLastModel {

	/**
	 * 
	 */
	private String key;

	/**
	 * 정비일자
	 */
	private String day;
	/**
	 * 정비유형
	 */
	private String type;

	/**
	 * 주행거리
	 */
	private String distance;

	/**
	 * 정비업체
	 */
	private String branch;

	/**
	 * 차량번호
	 */
	private String carNum;

	public MaintenanceLastModel(String key, String distance, String day,
			String type, String branch, String carNum) {
		super();
		this.key = key;
		this.day = day;
		this.type = type;
		this.distance = distance;
		this.branch = branch;
		this.carNum = carNum;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getDay() {
		return day;
	}

	public void setDay(String day) {
		this.day = day;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getDistance() {
		return distance;
	}

	public void setDistance(String distance) {
		this.distance = distance;
	}

	public String getBranch() {
		return branch;
	}

	public void setBranch(String branch) {
		this.branch = branch;
	}

	public String getCarNum() {
		return carNum;
	}

	public void setCarNum(String carNum) {
		this.carNum = carNum;
	}

}
