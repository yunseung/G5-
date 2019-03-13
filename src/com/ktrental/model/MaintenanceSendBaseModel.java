package com.ktrental.model;

import java.util.HashMap;

public class MaintenanceSendBaseModel {

	private HashMap<String, String> mHashMap = new HashMap<String, String>();

	/**
	 * 오더 번호
	 * 
	 */
	private String AUFNR;
	/**
	 * 순회차량번호
	 */
	private String MTINVNR;
	/**
	 * 긴급정비 (긴급정비일때 X, 아닐때 공백)
	 */
	private String CEMER;
	/**
	 * 고객차량번호
	 */
	private String INVNR;
	/**
	 * 카운터 값 (주행거리)
	 */
	private String INPML;
	/**
	 * 긴급정비사유
	 */
	private String CEMRS;
	/**
	 * 실행시작의 확정일 (당일날짜)
	 */
	private String ISDD;
	/**
	 * '실행시작' 확정시간
	 */
	private String ISDZ;
	/**
	 * 실행종료에 대한 확정일 (당일날짜)
	 */
	private String IEDD;
	/**
	 * '실행종료'에 대한 확정시간
	 */
	private String IEDZ;
	/**
	 * 비고
	 */
	private String CCMBI;
	/**
	 * 차기요청사항
	 */
	private String CCMRQ;
	/**
	 * 영업담당자요구사항
	 */
	private String CCMSL;

	/**
	 * "정비등록 유형 ( "" "", 긴급정비""E"", 정비수정 ""U"")"
	 */
	private String GUBUN;
	/**
	 * 고객명
	 */
	private String NAME1;
	/**
	 * 고객연락처
	 */
	private String TELF1;
	/**
	 * 사인이미지 명
	 */
	private String SIGN_T;

	/**
	 * "정비번호 상태(""I"", ""D"", ""U"")"
	 */

	/**
	 * MESSAGE
	 */
	private String MESSAGE="";
	
	private String name;

	// 2017-12-27. 순회점검 타이어 교체 체크 여부
	private String TIRE = "";
	// 2017-12-27. 순회점검 타이어 사이즈
	private String TIRE_SIZE = "";

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}


	public MaintenanceSendBaseModel(String aUFNR, String mTINVNR, String cEMER,
			String iNVNR, String iNPML, String cEMRS, String iSDD, String iSDZ,
			String iEDD, String iEDZ, String cCMBI, String cCMRQ, String cCMSL,
			String _name, String tire, String tire_size) {
		super();
		AUFNR = aUFNR;
		mHashMap.put("AUFNR", AUFNR);

		MTINVNR = mTINVNR;
		mHashMap.put("MTINVNR", MTINVNR);

		CEMER = cEMER;
		mHashMap.put("CEMER", CEMER);

		INVNR = iNVNR;
		mHashMap.put("INVNR", INVNR);

		INPML = iNPML;
		mHashMap.put("INPML", INPML);

		CEMRS = cEMRS;
		mHashMap.put("CEMRS", CEMRS);

		ISDD = iSDD;
		mHashMap.put("ISDD", ISDD);

		ISDZ = iSDZ;
		mHashMap.put("ISDZ", ISDZ);

		IEDD = iEDD;
		mHashMap.put("IEDD", IEDD);

		IEDZ = iEDZ;
		mHashMap.put("IEDZ", IEDZ);

		CCMBI = cCMBI;
		mHashMap.put("CCMBI", CCMBI);

		CCMRQ = cCMRQ;
		mHashMap.put("CCMRQ", CCMRQ);

		CCMSL = cCMSL;
		mHashMap.put("CCMSL", CCMSL);
		name = _name;
		mHashMap.put("DRIVN", name);

		mHashMap.put("COUNT", "0");
		
		mHashMap.put("MESSAGE", MESSAGE);

		TIRE = tire;
		mHashMap.put("TIRE", TIRE);

		TIRE_SIZE = tire_size;
		mHashMap.put("TIRESIZE", TIRE_SIZE);
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

	public String getCEMER() {
		return CEMER;
	}

	public void setCEMER(String cEMER) {
		CEMER = cEMER;
	}

	public String getINVNR() {
		return INVNR;
	}

	public void setINVNR(String iNVNR) {
		INVNR = iNVNR;
	}

	public String getINPML() {
		return INPML;
	}

	public void setINPML(String iNPML) {
		INPML = iNPML;
	}

	public String getCEMRS() {
		return CEMRS;
	}

	public void setCEMRS(String cEMRS) {
		CEMRS = cEMRS;
	}

	public String getISDD() {
		return ISDD;
	}

	public void setISDD(String iSDD) {
		ISDD = iSDD;
	}

	public String getISDZ() {
		return ISDZ;
	}

	public void setISDZ(String iSDZ) {
		ISDZ = iSDZ;
	}

	public String getIEDD() {
		return IEDD;
	}

	public void setIEDD(String iEDD) {
		IEDD = iEDD;
	}

	public String getIEDZ() {
		return IEDZ;
	}

	public void setIEDZ(String iEDZ) {
		IEDZ = iEDZ;
	}

	public String getCCMBI() {
		return CCMBI;
	}

	public void setCCMBI(String cCMBI) {
		CCMBI = cCMBI;
	}

	public String getCCMRQ() {
		return CCMRQ;
	}

	public void setCCMRQ(String cCMRQ) {
		CCMRQ = cCMRQ;
	}

	public String getCCMSL() {
		return CCMSL;
	}

	public void setCCMSL(String cCMSL) {
		CCMSL = cCMSL;
	}

	public String getGUBUN() {
		return GUBUN;
	}

	public void setGUBUN(String gUBUN) {

		GUBUN = gUBUN;
		mHashMap.put("GUBUN", GUBUN);
	}
	
	public String getNAME1() {
		return NAME1;
	}

	public void setNAME1(String nAME1) {

		NAME1 = nAME1;
		mHashMap.put("NAME1", NAME1);
	}

	public String getTELF1() {
		return TELF1;
	}

	public void setTELF1(String tELF1) {
		TELF1 = tELF1;
		mHashMap.put("TELF1", TELF1);
	}

	public String getSIGN_T() {
		return SIGN_T;
	}

	public void setSIGN_T(String sIGN_T) {

		SIGN_T = sIGN_T;
		if (mHashMap.containsKey("SIGN_T"))
			mHashMap.remove("SIGN_T");
		mHashMap.put("SIGN_T", SIGN_T);
	}
	
	
	public String getMESSAGE() {
		return MESSAGE;
	}
	
	public void setMESSAGE(String gMESSAGE) {
		
		MESSAGE = gMESSAGE;
		mHashMap.put("MESSAGE", MESSAGE);
	}

	public void setTIRE_SIZE(String tire_size){
		TIRE_SIZE = tire_size;
		mHashMap.put("TIRESIZE", tire_size);
	}

	public String getTIRE_SIZE() {return TIRE_SIZE;}

	public void setTire(String tire){
		TIRE = tire;
		mHashMap.put("TIRE", tire);
	}

	public String getTire(){
		return TIRE;
	}
	public HashMap<String, String> getmHashMap() {
		return mHashMap;
	}

	public void setmHashMap(HashMap<String, String> mHashMap) {
		this.mHashMap = mHashMap;
	}

}
