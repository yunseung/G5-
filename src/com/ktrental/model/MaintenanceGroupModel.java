package com.ktrental.model;

public class MaintenanceGroupModel implements Cloneable{
	String name;
	String name_key;
	String MATNR;
	int consumption;

	/**
	 * 이 객체의 복제품을 리턴한다.
	 */
	public MaintenanceGroupModel clone() {
		// 내 객체 생성
		MaintenanceGroupModel objReturn;

		try {
			objReturn = (MaintenanceGroupModel) super.clone();
			return objReturn;
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
			return null;
		}

	}// end clone
	
	
	public MaintenanceGroupModel(String name, String name_key) {
		this.name = name;
		this.name_key = name_key;
	}

	public MaintenanceGroupModel(String name, String name_key, String matnr, int consumption) {
		this.name = name;
		this.name_key = name_key;
		this.MATNR = matnr;
		this.consumption = consumption;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMATNR() {
		return MATNR;
	}

	public void setMATNR(String MATNR) {
		this.MATNR = MATNR;
	}
	
	public String getName_key() {
		return name_key;
	}

	public void setName_key(String name_key) {
		this.name_key = name_key;
	}
	

}
