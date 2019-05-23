package com.ktrental.common;

/**
 * ���ϸ� : DEFINES.java �ۼ��� : ypkim �ۼ��� : 2013. 06. 25. ���ϼ��� : ����,
 * �����, ���α׷� ��� ���� ����
 */
public class DEFINE {

	final public static String APP_NAME = "KT_RENTAL";
	// SAP Connector Info Star!

	// public static String SAP_CLIENT_NO = "630";

	final public static String SAP_SYSTEM_NO = "00";
	final public static int SAP_EXECUTE_TIMEOUT = 3000;
	// SAP Connector Info End!

	// MiddleWare Connector Info Start!
	// final public static String MW_HOST_IP = "14.49.24.102";
	
	// 순회정비 : R70131, R70180, R70074, R70062
	// 배반차   : R70276 R70267

	//E0004 , E0005 CTMSTS가 일때  
	//완료 된 정비차량이 선택되었습니다. 

//	 테스트 서버
//	public static String SAP_CLIENT_NO = "620";
//	public static String SAP_CLIENT_NO = "100";
	
	 public static String SAP_CLIENT_NO = "100";

//	 public final static String SAP_SERVER_IP = "10.220.64.134";
//	 public final static String MW_HOST_IP = "10.220.64.137";
//	 public final static String PAPER_DOWN_URL = "https://edi.ktrental.com/smarteInterface_erp/android/KTRPWdev.apk";
//	 public final static String DOWNLOAD_URL = "http://10.220.64.137:8090/app/Android/cm/";
//	 public final static String PIC_UPLOAD_URL = "http://10.220.64.136:8004";
//	 public final static String DOWNLOAD_APK = "KTRental_Test.apk";

	// 운영서버
//	final public static String MW_HOST_IP = "14.49.24.102"; // 사용안함

//	final public static String SAP_SERVER_IP = "rerpci";
//	final public static String MW_HOST_IP = "merp.ktrental.com";
//	final public static String PAPER_DOWN_URL = "http://app.ktrental.com";
//	public final static String DOWNLOAD_URL = "http://app.ktrental.com/Android/cm/";
//	public final static String PIC_UPLOAD_URL = "http://ext.lotterental.net:8001";	//"http://14.49.24.100:8001";
//	public final static String DOWNLOAD_APK = "KTRental_Real.apk";


	/**
	 * 2017.05.19. hjt
	 * L.Cloud 이전 관련 서버 주소 변경 건.
	 */
	/**
	 * 신규 테스트 서버
	 */

//	public final static String SAP_SERVER_IP = "10.106.7.13";
//	public final static String MW_HOST_IP = "124.243.70.42";
//	public final static String PAPER_DOWN_URL = "https://edi.ktrental.com/smarteInterface_erp/android/KTRPWdev.apk";
//	public final static String DOWNLOAD_URL = "http://124.243.70.42:8090/app/Android/cm/";
//	public final static String PIC_UPLOAD_URL = "http://10.106.7.11:8004";
//	public final static String DOWNLOAD_APK = "KTRental_Test.apk";

	/**
	 * 신규 운영 서버.
	 * 2017.05.29.
	 * 운영서버로 완전 변경 전에 테스트 서버로 활용한다
	 */
	final public static String SAP_SERVER_IP = "rerpci"; //"10.106.2.21"
	final public static String MW_HOST_IP = "merp.lotterental.net"; //"124.243.70.84,85"
	final public static String PAPER_DOWN_URL = "http://app.lotterental.net";
	public final static String DOWNLOAD_URL = "http://app.lotterental.net/Android/cm/";
	public final static String PIC_UPLOAD_URL = "http://ext.lotterental.net:8001";	//"http://14.49.24.100:8001";
	public final static String DOWNLOAD_APK = "KTRental_Real.apk";

	final public static int MW_HOST_PORT = 9192;
	final public static int MW_CONNECTION_TIMEOUT = 10;
	// MiddleWare Connector Info End! 

	// SQLlite Info Start!!
	final public static String SQLLITE_DB_NAME = "O_ITAB1.db";

	// 주소검색
	final public static String ADDRESS_TABLE_NAME = "address_table_name";

	public final static String O_ITAB1_TABLE_NAME = "O_ITAB1";

	public final static String CAR_MASTER_TABLE_NAME = "CAR_MASTER_TABLE"; //
	public final static String LOGIN_TABLE_NAME = "LOGIN_STRUCT_TABLE"; // 로그인
																		// 정보
	public final static String ADDRESS_TABLE = "ADDRESS_TABLE";

	public final static String STOCK_TABLE_NAME = "STOCK_TABLE"; // 재고 테이블
	public final static String REPAIR_TABLE_NAME = "REPAIR_TABLE"; // 순회정비현황
	public final static String PARTSMASTER_TABLE_NAME = "PARTSMASTER";
	public final static String REPAIR_LAST_TABLE_NAME = "REPAIR_LAST_TABLE";
	public final static String REPAIR_LAST_DETAIL_TABLE_NAME = "REPAIR_LAST_DETAIL_TABLE";

	public final static String CALL_LOG_TABLE_NAME = "CALL_LOG_TABLE";

	// 결과등록 실패
	public final static String REPAIR_RESULT_BASE_TABLE_NAME = "REPAIR_RESULT_BASE_TABLE_NAME";
	public final static String REPAIR_RESULT_STOCK_TABLE_NAME = "REPAIR_RESULT_STOCK_TABLE_NAME";
	public final static String REPAIR_RESULT_IMAGE_TABLE_NAME = "REPAIR_RESULT_IMAGE_TABLE_NAME";

	/**
	 * 정비결과 전송테이블
	 */
	public final static String MAINTENANCE_SAND_BASE_TABLE_NAME = "MAINTENANCE_SAND_BASE_TABLE";
	public final static String MAINTENANCE_SAND_STOCK_TABLE_NAME = "MAINTENANCE_SAND_STOCK_TABLE_NAME";

	// 순회정비계획 리스트
	final public static String GSUZS = "GSUZS";
	final public static String INVNR = "INVNR";
	final public static String KUNNR_NM = "KUNNR_NM";
	final public static String DRIVN = "DRIVN";
	final public static String MAKTX = "MAKTX";
	final public static String CCMRQ = "CCMRQ";
	final public static String POST_CODE = "POST_CODE";
	final public static String CITY = "CITY";
	final public static String STREET = "STREET";
	final public static String ABC = "ABC";
	

	// 인코딩되어야 되는 값들.
	public static final String DRV_TEL = "DRV_TEL";
	public static final String DRV_MOB = "DRV_MOB";
	public static final String MOB_NUMBER2 = "MOB_NUMBER2";
	public static final String USERID = "USERID";

	// 계획 처리 값
	final public static String CCMSTS = "CCMSTS";
	final public static String GSTRS = "GSTRS";

	public static String[] SAVE_DATABASE_STRUCT = { LOGIN_TABLE_NAME };

	// 차량 정보
	final public static String PARNR2_TX = "PARNR2_TX";
	// 로그인한 사용자의 차량번호
	final public static String TIRFR = "TIRFR";
	final public static String TIRBK = "TIRBK";
	final public static String INPML = "INPML";
	final public static String VBELN = "VBELN";
	final public static String BSARK = "BSARK";
	final public static String BSARK_TX = "BSARK_TX";
	final public static String PERNR_TX = "PERNR_TX";
	final public static String VKGRP1_TX = "VKGRP1_TX";
	final public static String VKGRP_TX = "VKGRP_TX";
	final public static String INNAM = "INNAM";
	final public static String ENAME = "ENAME";
	final public static String USRID = "USRID";
	final public static String GUEBG2 = "GUEBG2";
	final public static String GUEEN2 = "GUEEN2";
	final public static String TXT30 = "TXT30";
	final public static String JOINDT = "JOINDT";
	final public static String PMFRE_TX = "PMFRE_TX";
	final public static String MATMA_TX = "MATMA_TX";
	final public static String CYCMN_TX = "CYCMN_TX";
	final public static String NOTIR_TX = "NOTIR_TX";
	final public static String LNTMA_TX = "LNTMA_TX";
	final public static String EXHIB_TX = "EXHIB_TX";
	final public static String GENMA_TX = "GENMA_TX";
	final public static String BKTIR = "BKTIR";
	final public static String EMCAL_TX = "EMCAL_TX";
	final public static String SNWTR = "SNWTR";
	final public static String OILTYP = "OILTYP";
	final public static String MDLCD = "MDLCD";
	final public static String FUELCD = "FUELCD";
	final public static String MTQTY = "MTQTY";
	final public static String MINQTY = "MINQTY";
	final public static String MAXQTY = "MAXQTY";
	
	public final static String OILTYPNM = "OILTYPNM";
	public final static String MATNR = "MATNR";
	public final static String MATKL = "MATKL";

	public final static String AUFNR = "AUFNR";
	public final static String EQUNR = "EQUNR";
	public final static String CTRTY = "CTRTY";
	public final static String CEMER = "CEMER";
	public final static String GUBUN = "GUBUN";
	public final static String CHNGBN = "CHNGBN";
	public final static String OWNER = "OWNER";
	public final static String NIELS_NM = "NIELS_NM";
	public final static String LDATE = "LDATE";
	public final static String REPOIL = "REPOIL";

	public final static String VOCNUM = "VOCNUM";
	
	public final static String KUNNR = "KUNNR";


	public final static String DELAY = "DELAY";

	public final static String APM = "APM";
	public final static String REQNO = "REQNO";
	public static final String ATVYN = "ATVYN";
	public static final String PRERQ = "PRERQ";
	public static final String MINVNR = "MINVNR";
	public static final String REQDT = "REQDT";

	public static String SESSION = "";
	
	
	public static String DISPLAY;
	public static int MATCHING_CAR_NUMBER = 0;
	public static boolean PAPERESS_STATUS_FLAG = false;
	public static boolean RESIST_RESULT_FIRST_FLAG = true;

	/**
	 * RFC FUNCTION
	 */
	public static final String ZMO_0110_004  = "ZMO_0110_004";
	public static final String ZMO_1010_RD02 = "ZMO_1010_RD02"; // 중복 로그인 체크
	public static final String ZMO_1020_RD03 = "ZMO_1020_RD03";
	public static final String ZMO_1030_RD04 = "ZMO_1030_RD04";
	public static final String ZMO_1030_RD05 = "ZMO_1030_RD05";
	public static final String ZMO_1030_RD06 = "ZMO_1030_RD06";
	public static final String ZMO_1030_RD07 = "ZMO_1030_RD07";
	public static final String ZMO_1030_RD08 = "ZMO_1030_RD08";
	public static final String ZMO_1040_RD01 = "ZMO_1040_RD01";
	public static final String ZMO_1040_RD02 = "ZMO_1040_RD02";
	public static final String ZMO_1060_RD01 = "ZMO_1060_RD01";
	public static final String ZMO_1060_RD02 = "ZMO_1060_RD02";
	public static final String ZMO_1060_RD03 = "ZMO_1060_RD03";
	public static final String ZMO_1060_RD04 = "ZMO_1060_RD04";
	public static final String ZMO_1060_RD05 = "ZMO_1060_RD05";
	public static final String ZMO_1060_RD06 = "ZMO_1060_RD06";
	public static final String ZMO_1060_RD07 = "ZMO_1060_RD07";
	public static final String ZMO_1060_RD08 = "ZMO_1060_RD08";
	public static final String ZMO_1070_RD01 = "ZMO_1070_RD01";
	public static final String ZMO_1070_RD03 = "ZMO_1070_RD03";
	public static final String ZMO_1070_RD04 = "ZMO_1070_RD04";
	public static final String ZMO_1090_RD01 = "ZMO_1090_RD01";
	public static final String ZMO_1100_RD02 = "ZMO_1100_RD02";
	public static final String ZMO_1100_RD03 = "ZMO_1100_RD03";
	public static final String ZMO_1100_RD04 = "ZMO_1100_RD04";
	public static final String ZMO_1100_RD05 = "ZMO_1100_RD05";
	public static final String ZMO_1100_RD06 = "ZMO_1100_RD06";
	public static final String ZMO_1120_RD01 = "ZMO_1120_RD01";
	public static final String ZMO_1140_RD02 = "ZMO_1140_RD02";
	public static final String ZMO_3020_RD02 = "ZMO_3020_RD02";
	public static final String ZMO_3140_RD01 = "ZMO_3140_RD01";
	public static final String ZMO_3200_RD01 = "ZMO_3200_RD01";
	public static final String ZMO_1010_WR02 = "ZMO_1010_WR02";


	public static final String ZMO_3220_RD01 = "ZMO_3220_RD01";
	public static final String ZMO_1030_WR05 = "ZMO_1030_WR05";
	public static final String ZMO_1030_WR06 = "ZMO_1030_WR06";
	public static final String ZMO_1030_WR07 = "ZMO_1030_WR07";
	public static final String ZMO_1040_WR02 = "ZMO_1040_WR02";
	public static final String ZMO_1050_WR02 = "ZMO_1050_WR02";
	public static final String ZMO_1050_WR04 = "ZMO_1050_WR04";
	public static final String ZMO_1060_WR01 = "ZMO_1060_WR01";
	public static final String ZMO_1060_WR02 = "ZMO_1060_WR02";
	public static final String ZMO_1080_WR01 = "ZMO_1080_WR01";
	public static final String ZMO_1090_WR01 = "ZMO_1090_WR01";
	public static final String ZMO_1100_WR01 = "ZMO_1100_WR01";
	public static final String ZMO_1100_WR02 = "ZMO_1100_WR02";
	public static final String ZMO_1100_WR03 = "ZMO_1100_WR03";
	public static final String ZMO_1100_WR04 = "ZMO_1100_WR04"; // 단순 종료시 콜 function
	public static final String ZMO_1120_WR01 = "ZMO_1120_WR01";
	public static final String ZMO_1120_WR03 = "ZMO_1120_WR03";
	public static final String ZMO_1130_WR01 = "ZMO_1130_WR01";
	public static final String ZMO_3140_WR01 = "ZMO_3140_WR01";
	public static final String ZMO_3140_WR02 = "ZMO_3140_WR02";
	public static final String ZMO_3140_WR03 = "ZMO_3140_WR03";
	public static final String ZMO_3140_WR04 = "ZMO_3140_WR04";
	public static final String ZMO_3200_WR01 = "ZMO_3200_WR01";
	public static final String ZMO_3200_WR02 = "ZMO_3200_WR02";
	public static final String ZMO_3200_WR03 = "ZMO_3200_WR03";
	public static final String ZMO_3200_WR04 = "ZMO_3200_WR04";

	public static final String ZMO_1020_RD05 = "ZMO_1020_RD05"; // 2017-11-28. 순회점검일때 정비항목

	public static boolean DEBUG_MODE = false;

	public static boolean getDEBUG_MODE(){
		boolean rVal = false;

		if(SAP_SERVER_IP != null && SAP_SERVER_IP.equals("rerpci")){
			rVal = false;
		} else {
			rVal = true;
		}
		return rVal;
	};
}

