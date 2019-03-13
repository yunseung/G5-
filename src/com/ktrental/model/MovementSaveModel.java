package com.ktrental.model;

import java.util.HashMap;

public class MovementSaveModel {
	private String EQUNR;
	private String SEQ;
	private String INVNR;
	private String OTYPE;
	private String AUFNR;
	private String DRIVER;
	private String DRVNAM;
	private String OSTEP;
	private String DATUM;
	private String UZEIT;
	private String VKBUR;
	private String MILAG;
	private String DIVSN;
	private String GDOIL;
	private String PTRAN2;
	private String GDOIL2;
	private String OILPRI;
	private String WASH;
	private String PARK;
	private String TOLL;
	private String OTRAMT;
	private String REASON_TX;

	private HashMap<String, String> mMap = new HashMap<String, String>();

	public MovementSaveModel(String eQUNR, String sEQ, String iNVNR,
			String oTYPE, String aUFNR, String dRIVER, String dRVNAM,
			String oSTEP, String dATUM, String uZEIT, String vKBUR,
			String mILAG, String dIVSN, String gDOIL, String pTRAN2,
			String gDOIL2, String oILPRI, String wASH, String pARK,
			String tOLL, String oTRAMT, String rEASON_TX) {
		super();
		EQUNR = eQUNR;
		mMap.put("EQUNR", EQUNR);
		SEQ = sEQ;
		mMap.put("SEQ", SEQ);
		INVNR = iNVNR;
		mMap.put("INVNR", INVNR);
		OTYPE = oTYPE;
		mMap.put("OTYPE", OTYPE);
		AUFNR = aUFNR;
		mMap.put("AUFNR", AUFNR);
		DRIVER = dRIVER;
		mMap.put("DRIVER", DRIVER);
		DRVNAM = dRVNAM;
		mMap.put("DRVNAM", DRVNAM);
		OSTEP = oSTEP;
		mMap.put("OSTEP", OSTEP);
		DATUM = dATUM;
		mMap.put("DATUM", DATUM);
		UZEIT = uZEIT;
		mMap.put("UZEIT", UZEIT);
		VKBUR = vKBUR;
		mMap.put("VKBUR", VKBUR);
		MILAG = mILAG;
		mMap.put("MILAG", MILAG);
		DIVSN = dIVSN;
		mMap.put("DIVSN", DIVSN);
		GDOIL = gDOIL;
		mMap.put("GDOIL", GDOIL);
		PTRAN2 = pTRAN2;
		mMap.put("PTRAN2", PTRAN2);
		GDOIL2 = gDOIL2;
		mMap.put("GDOIL2", GDOIL2);
		OILPRI = oILPRI;
		mMap.put("OILPRI", OILPRI);
		WASH = wASH;
		mMap.put("WASH", WASH);
		PARK = pARK;
		mMap.put("PARK", PARK);
		TOLL = tOLL;
		mMap.put("TOLL", TOLL);
		OTRAMT = oTRAMT;
		mMap.put("OTRAMT", OTRAMT);
		REASON_TX = rEASON_TX;
		mMap.put("REASON_TX", REASON_TX);
	}

	public String getEQUNR() {
		return EQUNR;
	}

	public void setEQUNR(String eQUNR) {
		EQUNR = eQUNR;
	}

	public String getINVNR() {
		return INVNR;
	}

	public void setINVNR(String iNVNR) {
		INVNR = iNVNR;
	}
	
	public String getSEQ() {
		return SEQ;
	}

	public void setSEQ(String sEQ) {
		SEQ = sEQ;
	}

	public String getOTYPE() {
		return OTYPE;
	}

	public void setOTYPE(String oTYPE) {
		OTYPE = oTYPE;
	}

	public String getAUFNR() {
		return AUFNR;
	}

	public void setAUFNR(String aUFNR) {
		AUFNR = aUFNR;
	}

	public String getDRIVER() {
		return DRIVER;
	}

	public void setDRIVER(String dRIVER) {
		DRIVER = dRIVER;
	}

	public String getDRVNAM() {
		return DRVNAM;
	}

	public void setDRVNAM(String dRVNAM) {
		DRVNAM = dRVNAM;
	}

	public String getOSTEP() {
		return OSTEP;
	}

	public void setOSTEP(String oSTEP) {
		OSTEP = oSTEP;
	}

	public String getDATUM() {
		return DATUM;
	}

	public void setDATUM(String dATUM) {
		DATUM = dATUM;
	}

	public String getUZEIT() {
		return UZEIT;
	}

	public void setUZEIT(String uZEIT) {
		UZEIT = uZEIT;
	}

	public String getVKBUR() {
		return VKBUR;
	}

	public void setVKBUR(String vKBUR) {
		VKBUR = vKBUR;
	}

	public String getMILAG() {
		return MILAG;
	}

	public void setMILAG(String mILAG) {
		MILAG = mILAG;
	}

	public String getDIVSN() {
		return DIVSN;
	}

	public void setDIVSN(String dIVSN) {
		DIVSN = dIVSN;
	}

	public String getGDOIL() {
		return GDOIL;
	}

	public void setGDOIL(String gDOIL) {
		GDOIL = gDOIL;
	}

	public String getPTRAN2() {
		return PTRAN2;
	}

	public void setPTRAN2(String pTRAN2) {
		PTRAN2 = pTRAN2;
	}

	public String getGDOIL2() {
		return GDOIL2;
	}

	public void setGDOIL2(String gDOIL2) {
		GDOIL2 = gDOIL2;
	}

	public String getOILPRI() {
		return OILPRI;
	}

	public void setOILPRI(String oILPRI) {
		OILPRI = oILPRI;
	}

	public String getWASH() {
		return WASH;
	}

	public void setWASH(String wASH) {
		WASH = wASH;
	}

	public String getPARK() {
		return PARK;
	}

	public void setPARK(String pARK) {
		PARK = pARK;
	}

	public String getTOLL() {
		return TOLL;
	}

	public void setTOLL(String tOLL) {
		TOLL = tOLL;
	}

	public String getOTRAMT() {
		return OTRAMT;
	}

	public void setOTRAMT(String oTRAMT) {
		OTRAMT = oTRAMT;
	}

	public String getREASON_TX() {
		return REASON_TX;
	}

	public void setREASON_TX(String rEASON_TX) {
		REASON_TX = rEASON_TX;
	}

	public HashMap<String, String> getMap() {
		return mMap;
	}

}
