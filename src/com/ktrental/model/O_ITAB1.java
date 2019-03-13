package com.ktrental.model;

import java.io.Serializable;

public class O_ITAB1 implements Serializable {
	/**
	 * 
	 */
	public final static String TABLENAME="O_ITAB1";
	
	private static final long serialVersionUID = 1L;
	private String ZCODEH;
	private String ZCODEH2;
	private String ZCODEV;
	private String ZCODEVT;
	private String SET1;

	public O_ITAB1(String zcodeh, String zcodeh2, String zcodev,
			String zcodevt, String set1) {

		ZCODEH = zcodeh;
		ZCODEH2 = zcodeh2;
		ZCODEV = zcodev;
		ZCODEVT = zcodevt;
		SET1 = set1;
	}


	
	public String getZCODEH() {
		return ZCODEH;
	}

	public void setZCODEH(String zCODEH) {
		ZCODEH = zCODEH;
	}

	public String getZCODEH2() {
		return ZCODEH2;
	}

	public void setZCODEH2(String zCODEH2) {
		ZCODEH2 = zCODEH2;
	}

	public String getZCODEV() {
		return ZCODEV;
	}

	public void setZCODEV(String zCODEV) {
		ZCODEV = zCODEV;
	}

	public String getZCODEVT() {
		return ZCODEVT;
	}

	public void setZCODEVT(String zCODEVT) {
		ZCODEVT = zCODEVT;
	}

	public String getSET1() {
		return SET1;
	}

	public void setSET1(String sET1) {
		SET1 = sET1;
	}
}
