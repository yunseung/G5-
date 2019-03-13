package com.ktrental.model;

import java.util.HashMap;

public class LoginModel {

	public static final String SYSIP = "SYSIP";
	public static final String LOGID = "LOGID";
	public static final String NO_MASK = "NO_MASK";
	public static final String SCRNO = "SCRNO";
	public static final String DEPTCD = "DEPTCD";
	public static final String WERKS = "WERKS";
	public static final String BUKRS = "BUKRS";
	public static final String EQUNR = "EQUNR";
	public static final String SYS_CD = "SYS_CD";
	public static final String INVNR = "INVNR";
	public static final String DEPTNM = "DEPTNM";
	public static final String PERNR = "PERNR";
	public static final String LGORT = "LGORT";
	public static final String ENAME = "ENAME";
	public static final String SPRAS = "SPRAS";
	private String sysip;
	private String logid;
	private String no_mask;
	private String scrno;
	private String deptcd;
	private String werks;
	private String bukrs;
	private String equnr;
	private String sys_cd;
	private String invnr;
	private String deptnm;
	private String pernr;
	private String lgort;
	private String ename;
	private String spras;
	
    public String getSpras()
        {
        return spras;
        }

    
    public void setSpras(String spras)
        {
        this.spras = spras;
        }

    private static HashMap<String, String> mModelMap;

	public static final String VKORG = "VKORG";
	private String vkorg;
	public static final String LIFNR = "LIFNR";
	private String lifnr;

	public static final String LIFNR_CD = "LIFNR_CD";
	private String lifnr_cd;

	public String INGRP = "INGRP";
	public String FUELCD = "FUELCD";
	public String FUELNM = "FUELNM";

	public LoginModel(HashMap<String, String> aModelMap) {
		mModelMap = aModelMap;
		initMap();
	}

	public LoginModel(String _pernr, String _logid, String _ename,
			String _bukrs, String _scrno, String _sysip, String _sys_cd,
			String _no_mask, String _deptcd, String _werks, String _equnr,
			String _invnr, String _deptnm, String _lgort) {
		pernr = _pernr;
		logid = _logid;
		ename = _ename;
		bukrs = _bukrs;
		scrno = _scrno;
		sysip = _sysip;
		sys_cd = _sys_cd;
		no_mask = _no_mask;
		deptcd = _deptcd;
		werks = _werks;
		equnr = _equnr;
		invnr = _invnr;
		deptnm = _deptnm;
		lgort = _lgort;
		spras = "3";
	}

	private void initMap() {
		HashMap<String, String> tempMap = new HashMap<String, String>();
		if (mModelMap.containsKey(PERNR)) {
			pernr = mModelMap.get(PERNR);
			tempMap.put(PERNR, pernr);
		}
		if (mModelMap.containsKey(ENAME)) {
			ename = mModelMap.get(ENAME);
			tempMap.put(ENAME, ename);
		}
		if (mModelMap.containsKey(LOGID)) {
			logid = mModelMap.get(LOGID);
			tempMap.put(LOGID, logid);
		}
		if (mModelMap.containsKey(BUKRS)) {
			tempMap.put(BUKRS, mModelMap.get(BUKRS));
		}
		if (mModelMap.containsKey(SCRNO)) {
			tempMap.put(SCRNO, mModelMap.get(SCRNO));
		}
		if (mModelMap.containsKey(SYSIP)) {
			tempMap.put(SYSIP, mModelMap.get(SYSIP));
		}
		if (mModelMap.containsKey(SYS_CD)) {
			tempMap.put(SYS_CD, mModelMap.get(SYS_CD));
		}
		if (mModelMap.containsKey(NO_MASK)) {
			// tempMap.put(NO_MASK, mModelMap.get(NO_MASK));
			tempMap.put(NO_MASK, "X");
		}
		
		if (mModelMap.containsKey(SPRAS)) {
		spras = mModelMap.get(SPRAS);
		tempMap.put(SPRAS, "3");
		}


		if (mModelMap.containsKey(VKORG)) {
			vkorg = mModelMap.get(VKORG);
		}
		if (mModelMap.containsKey(DEPTCD)) {
			deptcd = mModelMap.get(DEPTCD);
		}
		if (mModelMap.containsKey(WERKS)) {
			werks = mModelMap.get(WERKS);
		}
		if (mModelMap.containsKey(EQUNR)) {
			equnr = mModelMap.get(EQUNR);
		}
		if (mModelMap.containsKey(INVNR)) {
			invnr = mModelMap.get(INVNR);
		}
		if (mModelMap.containsKey(DEPTNM)) {
			deptnm = mModelMap.get(DEPTNM);
		}
		if (mModelMap.containsKey(LGORT)) {
			lgort = mModelMap.get(LGORT);
		}

		if (mModelMap.containsKey(LIFNR)) {
			lifnr = mModelMap.get(LIFNR);
		}
		if (mModelMap.containsKey(LIFNR_CD)) {
			lifnr_cd = mModelMap.get(LIFNR_CD);
		}
		if (mModelMap.containsKey(INGRP)) {
			INGRP = mModelMap.get(INGRP);
		}
		if (mModelMap.containsKey(FUELCD)) {

			FUELCD = mModelMap.get(FUELCD);
		}
		if (mModelMap.containsKey(FUELNM)) {

			FUELNM = mModelMap.get(FUELNM);
		}

		mModelMap = tempMap;
	}

	public String getInvnr() {
		return invnr;
	}

	public void setInvnr(String invnr) {
		this.invnr = invnr;
	}

	public HashMap<String, String> getModelMap() {
		return mModelMap;
	}

	public String getPernr() {
		return pernr;
	}

	public void setPernr(String pernr) {
		this.pernr = pernr;
	}

	public String getLogid() {
		return logid;
	}

	public void setLogid(String logid) {
		this.logid = logid;
	}

	public String getEname() {
		return ename;
	}

	public void setEname(String ename) {
		this.ename = ename;
	}

	public String getBukrs() {
		return bukrs;
	}

	public void setBukrs(String bukrs) {
		this.bukrs = bukrs;
	}

	public String getScrno() {
		return scrno;
	}

	public void setScrno(String scrno) {
		this.scrno = scrno;
	}

	public String getSysip() {
		return sysip;
	}

	public void setSysip(String sysip) {
		this.sysip = sysip;
	}

	public String getSys_cd() {
		return sys_cd;
	}

	public void setSys_cd(String sys_cd) {
		this.sys_cd = sys_cd;
	}

	public String getNo_mask() {
		return no_mask;
	}

	public void setNo_mask(String no_mask) {
		this.no_mask = no_mask;
	}

	public String getDeptcd() {
		return deptcd;
	}

	public void setDeptcd(String deptcd) {
		this.deptcd = deptcd;
	}

	public String getWerks() {
		return werks;
	}

	public void setWerks(String werks) {
		this.werks = werks;
	}

	public String getEqunr() {
		return equnr;
	}

	public void setEqunr(String equnr) {
		this.equnr = equnr;
	}

	public String getDeptnm() {
		return deptnm;
	}

	public void setDeptnm(String deptnm) {
		this.deptnm = deptnm;
	}

	public String getLgort() {
		return lgort;
	}

	public void setLgort(String lgort) {
		this.lgort = lgort;
	}

	public HashMap<String, String> getmModelMap() {
		return mModelMap;
	}

	public void setmModelMap(HashMap<String, String> mModelMap) {
		this.mModelMap = mModelMap;
	}

	public String getVkorg() {
		return vkorg;
	}

	public void setVkorg(String vkorg) {
		this.vkorg = vkorg;
	}

	public String getLifnr() {
		return lifnr;
	}

	public void setLifnr(String lifnr) {
		this.lifnr = lifnr;
	}

	public String getLifnr_cd() {
		return lifnr_cd;
	}

	public void setLifnr_cd(String lifnr_cd) {
		this.lifnr_cd = lifnr_cd;
	}

	public String getINGRP() {
		return INGRP;
	}

	public void setINGRP(String iNGRP) {
		INGRP = iNGRP;
	}

	public String getFUELCD() {
		return FUELCD;
	}

	public void setFUELCD(String fUELCD) {
		FUELCD = fUELCD;
	}

	public String getFUELNM() {
		return FUELNM;
	}

	public void setFUELNM(String fUELNM) {
		FUELNM = fUELNM;
	}

}
