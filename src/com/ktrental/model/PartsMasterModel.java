package com.ktrental.model;

public class PartsMasterModel {
	String MATNR;
	String MATKL;

	//Jonathan 14.11.18 
	String MTQTY;
	String FUELCD;
	String MDLCD;
	String MINQTY;
	String MAXQTY;

//	String GRP_CD;

	public PartsMasterModel(String mATNR, String mATKL, String mTQTY, String mFUELCD
			,String mMDLCD, String mMINQTY, String mMAXQTY
	) {
		MATNR = mATNR;
		MATKL = mATKL;
		MTQTY = mTQTY;
		FUELCD = mFUELCD;
		MDLCD = mMDLCD;
		MINQTY = mMINQTY;
		MAXQTY = mMAXQTY;
	}


//	public PartsMasterModel(String mATNR, String mATKL) {
//		MATNR = mATNR;
//		MATKL = mATKL;
//	}

	public String getMATNR() {
		return MATNR;
	}

	public void setMATNR(String mATNR) {
		MATNR = mATNR;
	}

	public String getMATKL() {
		return MATKL;
	}

	public void setMATKL(String mATKL) {
		MATKL = mATKL;
	}


	public String getMTQTY() {
		return MTQTY;
	}

	public void setMTQTY(String mTQTY) {
		MTQTY = mTQTY;
	}

	public String getFUELCD() {
		return FUELCD;
	}

	public void setFUELCD(String mFUELCD) {
		FUELCD = mFUELCD;
	}

	public String getMDLCD() {
		return MDLCD;
	}

	public void setMDLCD(String mMDLCD) {
		MDLCD = mMDLCD;
	}

	public String getMINQTY() {
		return MINQTY;
	}

	public void setMINQTY(String minqty) {
		MINQTY = minqty;
	}

	public String getMAXQTY() {
		return MAXQTY;
	}

	public void setMAXQTY(String maxqty) {
		MAXQTY = maxqty;
	}
//	public String getGRP_CD() {
//		return GRP_CD;
//	}
//
//	public void setGRP_CD(String mGRP_CD) {
//		GRP_CD = mGRP_CD;
//	}


}
