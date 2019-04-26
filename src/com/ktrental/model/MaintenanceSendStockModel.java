package com.ktrental.model;

import java.util.HashMap;

public class MaintenanceSendStockModel {

	private HashMap<String, String> mHashMap = new HashMap<String, String>();

	/**
	 * 오더 번호
	 */
	private String AUFNR;
	/**
	 * 순회차량
	 */
	private String MTINVNR;
	/**
	 * 자재 번호
	 */
	private String MATNR;
	/**
	 * 입력 단위 수량
	 */
	private String ERFMG;
	/**
	 * 입력단위
	 */
	private String ERFME;
	/**
	 * 저장 위치
	 */
	private String LGORT;
	/**
	 * 플랜트
	 */
	private String WERKS;
	/**
	 * 자재 그룹
	 */
	private String MATKL;

	private String name;

	private String INVNR;
	
	private String GRP_CD;

	private String NETPR;

	private String ACTGRP;

	public MaintenanceSendStockModel(String aUFNR, String mTINVNR,
									 String mATNR, String eRFMG, String eRFME, String lGORT,
									 String wERKS, String mATKL, String _name, String invnr,
									 String gRPCD, String nETPR, String aCTGRP) {
		super();
		AUFNR = aUFNR;
		mHashMap.put("AUFNR", AUFNR);

		MTINVNR = mTINVNR;
		mHashMap.put("MTINVNR", MTINVNR);

		MATNR = mATNR;
		mHashMap.put("MATNR", MATNR);

		ERFMG = eRFMG;
		mHashMap.put("ERFMG", ERFMG);

		ERFME = eRFME;
		mHashMap.put("ERFME", ERFME);

		LGORT = lGORT;
		mHashMap.put("LGORT", LGORT);

		WERKS = wERKS;
		mHashMap.put("WERKS", WERKS);

		MATKL = mATKL;
		mHashMap.put("MATKL", MATKL);
		name = _name;
		mHashMap.put("DRIVN", name);
		INVNR = invnr;
		mHashMap.put("INVNR", INVNR);
		//2014.01.11	ypkim
		GRP_CD = gRPCD;
		mHashMap.put("GRP_CD", GRP_CD);

		NETPR = nETPR;
		mHashMap.put("NETPR", NETPR);

		ACTGRP = aCTGRP;
		mHashMap.put("ACTGRP", ACTGRP);
	}
	
	public String getINVNR() {
		return INVNR;
	}

	public void setINVNR(String iNVNR) {
		INVNR = iNVNR;
	}
	

	public String getAUFNR() {
		return AUFNR;
	}

	public void setAUFNR(String aUFNR) {
		AUFNR = aUFNR;
	}

	public String getMTINVNR() {
		return MTINVNR;
	}

	public void setMTINVNR(String mTINVNR) {
		MTINVNR = mTINVNR;
	}

	public String getMATNR() {
		return MATNR;
	}

	public void setMATNR(String mATNR) {
		MATNR = mATNR;
	}

	public String getERFMG() {
		return ERFMG;
	}

	public void setERFMG(String eRFMG) {
		ERFMG = eRFMG;
	}

	public String getERFME() {
		return ERFME;
	}

	public void setERFME(String eRFME) {
		ERFME = eRFME;
	}

	public String getLGORT() {
		return LGORT;
	}

	public void setLGORT(String lGORT) {
		LGORT = lGORT;
	}

	public String getWERKS() {
		return WERKS;
	}

	public void setWERKS(String wERKS) {
		WERKS = wERKS;
	}

	public String getMATKL() {
		return MATKL;
	}

	public void setMATKL(String mATKL) {
		MATKL = mATKL;
	}

	public HashMap<String, String> getmHashMap() {
		return mHashMap;
	}

	public void setmHashMap(HashMap<String, String> mHashMap) {
		this.mHashMap = mHashMap;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	//2014.01.11	ypkim
	public String getGRPCD() {
		return GRP_CD;
	}

	public void setGRPCD(String grpcd) {
		this.GRP_CD = grpcd;
	}
	
}
