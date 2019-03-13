package com.ktrental.model;

public class PerformanceModel {
	private String ZGUBUN = "";
	private String ZCODE = "";
	private String INGRP = "";
	private String INNAM = "";
	private String PROC_PCT = "";
	private String PLAN_TOT = "";
	private String PROC_CNT = "";
	private String UNTRET_TOT = "";
	private String BASYM = "";

	public PerformanceModel(String zGUBUN, String zCODE, String iNGRP,
			String iNNAM, String pROC_PCT, String pLAN_TOT, String pROC_CNT,
			String uNTRET_TOT, String bASYM) {
		super();
		ZGUBUN = zGUBUN;
		ZCODE = zCODE;
		INGRP = iNGRP;
		INNAM = iNNAM;
		PROC_PCT = pROC_PCT;
		PLAN_TOT = pLAN_TOT;
		PROC_CNT = pROC_CNT;
		UNTRET_TOT = uNTRET_TOT;
		BASYM = bASYM;
	}

	public String getZGUBUN() {
		return ZGUBUN;
	}

	public void setZGUBUN(String zGUBUN) {
		ZGUBUN = zGUBUN;
	}

	public String getZCODE() {
		return ZCODE;
	}

	public void setZCODE(String zCODE) {
		ZCODE = zCODE;
	}

	public String getINGRP() {
		return INGRP;
	}

	public void setINGRP(String iNGRP) {
		INGRP = iNGRP;
	}

	public String getINNAM() {
		return INNAM;
	}

	public void setINNAM(String iNNAM) {
		INNAM = iNNAM;
	}

	public String getPROC_PCT() {
		return PROC_PCT;
	}

	public void setPROC_PCT(String pROC_PCT) {
		PROC_PCT = pROC_PCT;
	}

	public String getPLAN_TOT() {
		return PLAN_TOT;
	}

	public void setPLAN_TOT(String pLAN_TOT) {
		PLAN_TOT = pLAN_TOT;
	}

	public String getPROC_CNT() {
		return PROC_CNT;
	}

	public void setPROC_CNT(String pROC_CNT) {
		PROC_CNT = pROC_CNT;
	}

	public String getUNTRET_TOT() {
		return UNTRET_TOT;
	}

	public void setUNTRET_TOT(String uNTRET_TOT) {
		UNTRET_TOT = uNTRET_TOT;
	}

	public String getBASYM() {
		return BASYM;
	}

	public void setBASYM(String bASYM) {
		BASYM = bASYM;
	}

}
