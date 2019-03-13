package com.ktrental.model;

import java.util.HashMap;

public class MaintenanceSendSignModel {

	private HashMap<String, String> mHashMap = new HashMap<String, String>();

	/**
	 * 사인인덱스
	 */
	// private String SEQ;
	/**
	 * 사인이미지명
	 */
	private String SIGN_T;
	/**
	 * 사인이미지(BASE64 Incoding)
	 */
	private String SIGN;

	private String name;

	private String INVNR;

	public String getINVNR() {
		return INVNR;
	}

	public void setINVNR(String invnr) {
		INVNR = invnr;
	}

	public MaintenanceSendSignModel(String sIGN_T, String sIGN, String _name,
			String invnr) {
		super();
		SIGN_T = sIGN_T;
		SIGN = sIGN;
		mHashMap.put("SIGN_T", SIGN_T);
		mHashMap.put("SIGN", SIGN);
		name = _name;
		mHashMap.put("DRIVN", name);
		INVNR = invnr;
		mHashMap.put("INVNR", INVNR);

	}

	public String getSIGN_T() {
		return SIGN_T;
	}

	public void setSIGN_T(String sIGN_T) {
		SIGN_T = sIGN_T;
	}

	public String getSIGN() {
		return SIGN;
	}

	public void setSIGN(String sIGN) {
		SIGN = sIGN;
	}

	public HashMap<String, String> getmHashMap() {
		return mHashMap;
	}

	public void setmHashMap(HashMap<String, String> mHashMap) {
		this.mHashMap = mHashMap;
	}
}
