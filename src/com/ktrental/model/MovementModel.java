package com.ktrental.model;

public class MovementModel {
	String OTYPE_TX;
	String AUFNR;
	String DRVNAM;
	String OSTEP_TX;
	String DATUM;
	String UZEIT;
	String VKBUR;
	String MILAG;
	String DIVSN_TX;
	String GDOIL;
	String GDOIL2;
	// 2014.01.09	ypkim add
	String EQUNR;
	String SEQ;
	boolean bCheck;


	String OILPRI;
	String WASH;
	String PARK;
	String TOLL;
	String OTRAMT;
	String REASON_TX;
	String PTRAN2;


	public MovementModel(String oTYPE_TX, String aUFNR, String dRVNAM,
			String oSTEP_TX, String dATUM, String uZEIT, String vKBUR,
			String mILAG, String dIVSN_TX, String gDOIL, String gDOIL2,
			String mEQUNR, String mSEQ, boolean mCheck,
			String mOILPRI, String mWASH, String mPARK, String mTOLL,
			String mOTRAMT, String mREASON_TX, String mPTRAN2 ) {
		super();
		OTYPE_TX = oTYPE_TX;
		AUFNR = aUFNR;
		DRVNAM = dRVNAM;
		OSTEP_TX = oSTEP_TX;
		DATUM = dATUM;
		UZEIT = uZEIT;
		VKBUR = vKBUR;
		MILAG = mILAG;
		DIVSN_TX = dIVSN_TX;
		GDOIL = gDOIL;
		GDOIL2 = gDOIL2;
		EQUNR = mEQUNR;
		SEQ = mSEQ;		
		bCheck = mCheck;

		OILPRI = mOILPRI;
		WASH = mWASH;
		PARK = mPARK;
		TOLL = mTOLL;
		OTRAMT = mOTRAMT;
		REASON_TX = mREASON_TX;
		PTRAN2 = mPTRAN2;



	}

	public String getOTYPE_TX() {
		return OTYPE_TX;
	}

	public void setOTYPE_TX(String oTYPE_TX) {
		OTYPE_TX = oTYPE_TX;
	}

	public String getAUFNR() {
		return AUFNR;
	}

	public void setAUFNR(String aUFNR) {
		AUFNR = aUFNR;
	}

	public String getDRVNAM() {
		return DRVNAM;
	}

	public void setDRVNAM(String dRVNAM) {
		DRVNAM = dRVNAM;
	}

	public String getOSTEP_TX() {
		return OSTEP_TX;
	}

	public void setOSTEP_TX(String oSTEP_TX) {
		OSTEP_TX = oSTEP_TX;
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

	public String getDIVSN_TX() {
		return DIVSN_TX;
	}

	public void setDIVSN_TX(String dIVSN_TX) {
		DIVSN_TX = dIVSN_TX;
	}

	public String getGDOIL() {
		return GDOIL;
	}

	public void setGDOIL(String gDOIL) {
		GDOIL = gDOIL;
	}

	public String getGDOIL2() {
		return GDOIL2;
	}

	public void setGDOIL2(String gDOIL2) {
		GDOIL2 = gDOIL2;
	}
	
	// 2014.01.09	ypkim add
	public String getEQUNR() {
		return EQUNR;
	}

	public void setEQUNR(String mEQUNR) {
		EQUNR = mEQUNR;
	}
	
	public String getSEQ() {
		return SEQ;
	}

	public void setSEQ(String mSEQ) {
		SEQ = mSEQ;
	}
	
	public boolean isCheckFlag() {
		return bCheck;
	}

	public void setCheckFlag(boolean check) {
		bCheck = check;
	}




	public String getOILPRI() {
		return OILPRI;
	}

	public void setOILPRI(String gOILPRI) {
		OILPRI = gOILPRI;
	}

	public String getWASH() {
		return WASH;
	}

	public void setWASH(String gWASH) {
		WASH = gWASH;
	}


	public String getPARK() {
		return PARK;
	}

	public void setPARK(String gPARK) {	PARK = gPARK; }

	public String getTOLL() {
		return TOLL;
	}

	public void setTOLL(String gTOLL) {
		TOLL = gTOLL;
	}

	public String getOTRAMT() {
		return OTRAMT;
	}

	public void setOTRAMT(String gOTRAMT) {
		OTRAMT = gOTRAMT;
	}

	public String getREASON_TX() {
		return REASON_TX;
	}

	public void setREASON_TX(String gREASON_TX) {
		REASON_TX = gREASON_TX;
	}

	public String getPTRAN2() {
		return PTRAN2;
	}

	public void setPTRAN2(String gPTRAN2) {
		PTRAN2 = gPTRAN2;
	}


}
