package com.ktrental.model;

public class ResultSendModel {
	/**
	 * 실행종료에 대한 확정일 (당일날짜)
	 */
	private String IEDD;
	/**
	 * '실행종료'에 대한 확정시간
	 */
	private String IEDZ;

	/**
	 * 고객차량번호
	 */
	private String INVNR;

	private String name;

	private String count;
	
	private String message; 
	

	public String getCount() {
		return count;
	}

	public void setCount(String count) {
		this.count = count;
	}

	private boolean checkFlag = false;

	public ResultSendModel(String iEDD, String iEDZ, String iNVNR, String name,
			String count, String _message) {
		super();
		IEDD = iEDD;
		IEDZ = iEDZ;
		INVNR = iNVNR;
		this.name = name;
		this.count = count;
		message = _message;
	}

	public String getIEDD() {
		return IEDD;
	}

	public void setIEDD(String iEDD) {
		IEDD = iEDD;
	}
	
	public String getmessage() {
		return message;
	}

	public void setmessage(String _message) {
		message = _message;
	}
	

	public String getIEDZ() {
		return IEDZ;
	}

	public void setIEDZ(String iEDZ) {
		IEDZ = iEDZ;
	}

	public String getINVNR() {
		return INVNR;
	}

	public void setINVNR(String iNVNR) {
		INVNR = iNVNR;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isCheckFlag() {
		return checkFlag;
	}

	public void setCheckFlag(boolean checkFlag) {
		this.checkFlag = checkFlag;
	}

}
