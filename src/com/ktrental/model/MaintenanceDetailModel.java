package com.ktrental.model;

public class MaintenanceDetailModel {
	/**
	 * 정비항목
	 */
	private String category;
	/**
	 * 정비세부항목
	 */
	private String categoryDetail;

	/**
	 * 수량
	 */
	private String num;
	/**
	 * 행위
	 */
	private String action;

	public MaintenanceDetailModel(String category, String categoryDetail,
			String num, String action) {
		super();
		this.category = category;
		this.categoryDetail = categoryDetail;
		this.num = num;
		this.action = action;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getCategoryDetail() {
		return categoryDetail;
	}

	public void setCategoryDetail(String categoryDetail) {
		this.categoryDetail = categoryDetail;
	}

	public String getNum() {
		return num;
	}

	public void setNum(String num) {
		this.num = num;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

}
