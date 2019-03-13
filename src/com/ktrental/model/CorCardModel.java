package com.ktrental.model;

public class CorCardModel {

	private boolean bCheck;
	private String UTYPE;
	private String GRAM_SEQ;  // 00000073
	private String DEAL_CDCO; // LT
	private String REASON10;
	private String REASON20;
	private String BT_NM; // 특급호텔
	private String ORGL_PERM_DT; // 사용일자
	private String REASON30; //
	private String REASON;
	private String DEAL_DIV; // T
	private String FR_USE_YN; // A
	private String FRAN_NO; // 9870113735
	private String BUY_SAM; // 11000.00 사용금액
	private String DOCSTATUS; // 02
	private String TAXTN_TY_INQ_DT; // 2017/03/01 ?
	private String FRAN_DTS_ADDR; //1번지 (주)호텔롯데 경리부수입관리과
	private String TRS_DT; // 2017/03/04
	private String ELC_WK_DT; // 2017/03/04
	private String APP_SCD_DT; // 2017/04/23
	private String SLIP_SUBM_YN;
	private String CHG_STAX; // 1000.00
	private String BUY_PUTP; // 0.00
	private String ELC_CUR_CD; // KRW
	private String VKGRP; // Y03
	private String BELNR; // 35000009242
	private String REASON40; //
	private String SENAME; // 공원배
	private String OWRSKTEXT; //
	private String ELC_SEQ; // 0000000000000001752459
	private String FRAN_PSNO; // 100070
	private String ORGEH; // 00002595
	private String BUY_CANC; //
	private String RFN_OJ_YN; // Y
	private String NTH_PERM_BUY_YN; // A
	private String CARDKIND; // S01
	private String BUY_APY_XRT; // 0.00000
	private String NOM_CNC_DIV; // A
	private String CARD_NO; // 1111111111
	private String SGTXT; // 33소2048 레이 공원배 순회차량 주차비
	private String STATNM; // 승인완료
	private String STATUS; // 05
	private String USETYPENM; // 회사사용
	private String EAI_DEAL_STAT; //
	private String WEBDOCNUM; // AA0B115D-4644-46A7-8030-B728E63D57D7
	private String WEBTYPE; // A1
	private String CHG_SALE_AMT; // 10000.00
	private String DOCNAM; // 차량주차통행료(순회차량)
	private String TAXTN_TY; // 일반
	private String BUY_DOL_CA; // 0.00
	private String ELC_SLIP_NO; //
	private String ERROR_YN; // 2
	private String BUY_SAM2; // 11000.00
	private String FRAN_BRNO; // 1048125980
	private String BUDAT; // 2017/03/02
	private String PERM_SAM; // 11000.00
	private String UTYPENM; //
	private String TRE_FRAN; // 호텔롯데 본점
	private String FRAN_REPTR; // 송용덕
	private String SKTEXT; // 서울북부MOT
	private String BT_CD; // 1101
	private String ATTATCHNO;
	private String CORP_ID; // 1178183462
	private String ZCLOSE; // X
	private String DATA_DC; // D
	private String ORGL_PERM_NO; // 51959188
	private String GJAHR; // 2017
	private String GUBUN;
	private String KTEXT;
	private String IFYN;
	private String FRAN_PNO; // 0000000007SCA
	private String BUY_AMT; // 11000.00
	private String ORGL_BUY_COLL_NO;
	private String ELC_USE_DIV;
	private String ENAME;
	private String DSC_SRVC_DIV;
	private String CARDNM;
	private String FRAN_ADDR; // 주소 / 서울시 중구 소공동
	private String PD_DIV; // A
	private String SORGEH; // 00002595
	private String USETYPE; // 02
	private String PERNR; // 00070092
	private String DOCTYPE; // KD49
	private String BUY_CLCTN_NO; // 2017030314238001     00100010126   //이렇게 전달옴
	private String PT_BUY_CNC_YN; // A
	private String CARDOWRTXT;
	private String CARDKINDNM; // 개인형 법인
	private String ECFEE_AMT; // 0.00
	private String BUY_PTM; // 13:02:33
	private String DOCSTATNM; // 실 전표
	private String ELC_BUY_DT; // 2017/03/03
	private String SLIP_DEAL_DC;
	private String SPERN; // 00070092
	private String BUKRS; // 3200

	public CorCardModel(String USETYPENM, String STATNM, String ORGL_PERM_DT,
                        String BUY_PTM, String PERM_SAM, String DOCNAM, String SGTXT
			, String BT_NM, String TRE_FRAN, String ORGL_PERM_NO, String TAXTN_TY, String FRAN_ADDR
			, String SKTEXT, String SENAME, String CHG_SALE_AMT, String CHG_STAX, String ELC_BUY_DT
			, String BELNR, String CARDKINDNM, String DOCSTATNM, String FRAN_BRNO, String ELC_SEQ, String ORGEH, String WEBTYPE, String WEBDOCNUM
			, String DOCTYPE, String GJAHR, String BUDAT, String SPERN, String SORGEH, String CARD_NO, String VKGRP, String KTEXT, String STATUS, String BUY_CANC
			, String USETYPE, String APP_SCD_DT, String BUY_AMT, String BUY_SAM, String REASON, boolean isCheck) {
		super();
		setUSETYPENM(USETYPENM); setSTATNM(STATNM);	setBUY_PTM(BUY_PTM); setPERM_SAM(PERM_SAM);
		setDOCNAM(DOCNAM); 	setSGTXT(SGTXT);setBT_NM(BT_NM); setTRE_FRAN(TRE_FRAN);

		String orgl_perm_dt = "";
		if(ORGL_PERM_DT != null){
			orgl_perm_dt = ORGL_PERM_DT.replace("/", "");
		}
		setORGL_PERM_DT(orgl_perm_dt);

		String orgl_perm_no = "";
		if(ORGL_PERM_NO != null){
			orgl_perm_no = ORGL_PERM_NO.replace("/", "");
		}

		setORGL_PERM_NO(orgl_perm_no);
		setTAXTN_TY(TAXTN_TY);
		setFRAN_ADDR(FRAN_ADDR);
		setSKTEXT(SKTEXT);
		setSENAME(SENAME);
		setCHG_SALE_AMT(CHG_SALE_AMT);
		setCHG_STAX(CHG_STAX);
		setELC_BUY_DT(ELC_BUY_DT);
		setBELNR(BELNR);
		setCARDKINDNM(CARDKINDNM);
		setDOCSTATNM(DOCSTATNM);
		setFRAN_BRNO(FRAN_BRNO);
		setCheckFlag(isCheck);
		setELC_SEQ(ELC_SEQ);
		setORGEH(ORGEH);
		setWEBTYPE(WEBTYPE);
		setWEBDOCNUM(WEBDOCNUM);
		setDOCTYPE(DOCTYPE);
		setGJAHR(GJAHR);
		String budat = "";
		if(BUDAT != null){
			budat = BUDAT.replace("/", "");
		}
		setBUDAT(budat);
		setSPERN(SPERN);
		setSORGEH(SORGEH);
		setCARD_NO(CARD_NO);
        setVKGRP(VKGRP);
        setKTEXT(KTEXT);
		setSTATUS(STATUS);
		setBUY_CANC(BUY_CANC);
		setUSETYPE(USETYPE);
		setAPP_SCD_DT(APP_SCD_DT);
		setBUY_AMT(BUY_AMT);
		setBUY_SAM(BUY_SAM);
		setREASON(REASON);
	}

	public String getUTYPE() {
		return UTYPE;
	}

	public void setUTYPE(String UTYPE) {
		if(UTYPE == null){
			UTYPE = "";
		}
		this.UTYPE = UTYPE;
	}

	public String getGRAM_SEQ() {
		return GRAM_SEQ;
	}

	public void setGRAM_SEQ(String GRAM_SEQ) {
		if(UTYPE == null){
			UTYPE = "";
		}this.GRAM_SEQ = GRAM_SEQ;
	}

	public String getDEAL_CDCO() {
		return DEAL_CDCO;
	}

	public void setDEAL_CDCO(String DEAL_CDCO) {
		if(UTYPE == null){
			UTYPE = "";
		}this.DEAL_CDCO = DEAL_CDCO;
	}

	public String getREASON10() {
		return REASON10;
	}

	public void setREASON10(String REASON10) {
		if(UTYPE == null){
			UTYPE = "";
		}
		this.REASON10 = REASON10;
	}

	public String getREASON20() {
		return REASON20;
	}

	public void setREASON20(String REASON20) {
		if(UTYPE == null){
			UTYPE = "";
		}
		this.REASON20 = REASON20;
	}

	public String getBT_NM() {
		return BT_NM;
	}

	public void setBT_NM(String BT_NM) {
		if(UTYPE == null){
			UTYPE = "";
		}
		this.BT_NM = BT_NM;
	}

	public String getORGL_PERM_DT() {
		return ORGL_PERM_DT;
	}

	public void setORGL_PERM_DT(String ORGL_PERM_DT) {

		if(UTYPE == null){
			UTYPE = "";
		}
		this.ORGL_PERM_DT = ORGL_PERM_DT;
	}

	public String getREASON30() {
		return REASON30;
	}

	public void setREASON30(String REASON30) {

		if(UTYPE == null){
			UTYPE = "";
		}
		this.REASON30 = REASON30;
	}

	public String getREASON() {
		return REASON;
	}

	public void setREASON(String REASON) {

		if(UTYPE == null){
			UTYPE = "";
		}
		this.REASON = REASON;
	}

	public String getDEAL_DIV() {
		return DEAL_DIV;
	}

	public void setDEAL_DIV(String DEAL_DIV) {
		if(UTYPE == null){
			UTYPE = "";
		}
		this.DEAL_DIV = DEAL_DIV;
	}

	public String getFR_USE_YN() {
		return FR_USE_YN;
	}

	public void setFR_USE_YN(String FR_USE_YN) {
		this.FR_USE_YN = FR_USE_YN;
	}

	public String getFRAN_NO() {
		return FRAN_NO;
	}

	public void setFRAN_NO(String FRAN_NO) {
		this.FRAN_NO = FRAN_NO;
	}

	public String getBUY_SAM() {
		return BUY_SAM;
	}

	public void setBUY_SAM(String BUY_SAM) {
		this.BUY_SAM = BUY_SAM;
	}

	public String getDOCSTATUS() {
		return DOCSTATUS;
	}

	public void setDOCSTATUS(String DOCSTATUS) {
		this.DOCSTATUS = DOCSTATUS;
	}

	public String getTAXTN_TY_INQ_DT() {
		return TAXTN_TY_INQ_DT;
	}

	public void setTAXTN_TY_INQ_DT(String TAXTN_TY_INQ_DT) {
		this.TAXTN_TY_INQ_DT = TAXTN_TY_INQ_DT;
	}

	public String getFRAN_DTS_ADDR() {
		return FRAN_DTS_ADDR;
	}

	public void setFRAN_DTS_ADDR(String FRAN_DTS_ADDR) {
		this.FRAN_DTS_ADDR = FRAN_DTS_ADDR;
	}

	public String getTRS_DT() {
		return TRS_DT;
	}

	public void setTRS_DT(String TRS_DT) {
		this.TRS_DT = TRS_DT;
	}

	public String getELC_WK_DT() {
		return ELC_WK_DT;
	}

	public void setELC_WK_DT(String ELC_WK_DT) {
		this.ELC_WK_DT = ELC_WK_DT;
	}

	public String getAPP_SCD_DT() {
		return APP_SCD_DT;
	}

	public void setAPP_SCD_DT(String APP_SCD_DT) {
		this.APP_SCD_DT = APP_SCD_DT;
	}

	public String getSLIP_SUBM_YN() {
		return SLIP_SUBM_YN;
	}

	public void setSLIP_SUBM_YN(String SLIP_SUBM_YN) {
		this.SLIP_SUBM_YN = SLIP_SUBM_YN;
	}

	public void setCHG_STAX(String CHG_STAX) {
		this.CHG_STAX = CHG_STAX;
	}

	public String getCHG_STAX() {
		return CHG_STAX;
	}

	public String getBUY_PUTP() {
		return BUY_PUTP;
	}

	public void setBUY_PUTP(String BUY_PUTP) {
		this.BUY_PUTP = BUY_PUTP;
	}

	public String getELC_CUR_CD() {
		return ELC_CUR_CD;
	}

	public void setELC_CUR_CD(String ELC_CUR_CD) {
		this.ELC_CUR_CD = ELC_CUR_CD;
	}

	public String getVKGRP() {
		return VKGRP;
	}

	public void setVKGRP(String VKGRP) {
		this.VKGRP = VKGRP;
	}

	public String getBELNR() {
		return BELNR;
	}

	public void setBELNR(String BELNR) {
		this.BELNR = BELNR;
	}

	public String getREASON40() {
		return REASON40;
	}

	public void setREASON40(String REASON40) {
		this.REASON40 = REASON40;
	}

	public String getSENAME() {
		return SENAME;
	}

	public void setSENAME(String SENAME) {
		this.SENAME = SENAME;
	}

	public String getOWRSKTEXT() {
		return OWRSKTEXT;
	}

	public void setOWRSKTEXT(String OWRSKTEXT) {
		this.OWRSKTEXT = OWRSKTEXT;
	}

	public String getELC_SEQ() {
		return ELC_SEQ;
	}

	public void setELC_SEQ(String ELC_SEQ) {
		this.ELC_SEQ = ELC_SEQ;
	}

	public String getFRAN_PSNO() {
		return FRAN_PSNO;
	}

	public void setFRAN_PSNO(String FRAN_PSNO) {
		this.FRAN_PSNO = FRAN_PSNO;
	}

	public String getORGEH() {
		return ORGEH;
	}

	public void setORGEH(String ORGEH) {
		this.ORGEH = ORGEH;
	}

	public String getBUY_CANC() {
		return BUY_CANC;
	}

	public void setBUY_CANC(String BUY_CANC) {
		this.BUY_CANC = BUY_CANC;
	}

	public String getRFN_OJ_YN() {
		return RFN_OJ_YN;
	}

	public void setRFN_OJ_YN(String RFN_OJ_YN) {
		this.RFN_OJ_YN = RFN_OJ_YN;
	}

	public String getNTH_PERM_BUY_YN() {
		return NTH_PERM_BUY_YN;
	}

	public void setNTH_PERM_BUY_YN(String NTH_PERM_BUY_YN) {
		this.NTH_PERM_BUY_YN = NTH_PERM_BUY_YN;
	}

	public String getCARDKIND() {
		return CARDKIND;
	}

	public void setCARDKIND(String CARDKIND) {
		this.CARDKIND = CARDKIND;
	}

	public String getBUY_APY_XRT() {
		return BUY_APY_XRT;
	}

	public void setBUY_APY_XRT(String BUY_APY_XRT) {
		this.BUY_APY_XRT = BUY_APY_XRT;
	}

	public String getNOM_CNC_DIV() {
		return NOM_CNC_DIV;
	}

	public void setNOM_CNC_DIV(String NOM_CNC_DIV) {
		this.NOM_CNC_DIV = NOM_CNC_DIV;
	}

	public String getCARD_NO() {
		return CARD_NO;
	}

	public void setCARD_NO(String CARD_NO) {
		this.CARD_NO = CARD_NO;
	}

	public String getSGTXT() {
		return SGTXT;
	}

	public void setSGTXT(String SGTXT) {
		this.SGTXT = SGTXT;
	}

	public String getSTATNM() {
		return STATNM;
	}

	public void setSTATNM(String STATNM) {
		this.STATNM = STATNM;
	}

	public String getSTATUS() {
		return STATUS;
	}

	public void setSTATUS(String STATUS) {
		this.STATUS = STATUS;
	}

	public String getUSETYPENM() {
		return USETYPENM;
	}

	public void setUSETYPENM(String USETYPENM) {
		this.USETYPENM = USETYPENM;
	}

	public String getEAI_DEAL_STAT() {
		return EAI_DEAL_STAT;
	}

	public void setEAI_DEAL_STAT(String EAI_DEAL_STAT) {
		this.EAI_DEAL_STAT = EAI_DEAL_STAT;
	}

	public String getWEBDOCNUM() {
		return WEBDOCNUM;
	}

	public void setWEBDOCNUM(String WEBDOCNUM) {
		this.WEBDOCNUM = WEBDOCNUM;
	}

	public String getWEBTYPE() {
		return WEBTYPE;
	}

	public void setWEBTYPE(String WEBTYPE) {
		this.WEBTYPE = WEBTYPE;
	}

	public String getCHG_SALE_AMT() {
		return CHG_SALE_AMT;
	}

	public void setCHG_SALE_AMT(String CHG_SALE_AMT) {
		this.CHG_SALE_AMT = CHG_SALE_AMT;
	}

	public String getDOCNAM() {
		return DOCNAM;
	}

	public void setDOCNAM(String DOCNAM) {
		this.DOCNAM = DOCNAM;
	}

	public String getTAXTN_TY() {
		return TAXTN_TY;
	}

	public void setTAXTN_TY(String TAXTN_TY) {
		this.TAXTN_TY = TAXTN_TY;
	}

	public String getBUY_DOL_CA() {
		return BUY_DOL_CA;
	}

	public void setBUY_DOL_CA(String BUY_DOL_CA) {
		this.BUY_DOL_CA = BUY_DOL_CA;
	}

	public String getELC_SLIP_NO() {
		return ELC_SLIP_NO;
	}

	public void setELC_SLIP_NO(String ELC_SLIP_NO) {
		this.ELC_SLIP_NO = ELC_SLIP_NO;
	}

	public String getERROR_YN() {
		return ERROR_YN;
	}

	public void setERROR_YN(String ERROR_YN) {
		this.ERROR_YN = ERROR_YN;
	}

	public String getBUY_SAM2() {
		return BUY_SAM2;
	}

	public void setBUY_SAM2(String BUY_SAM2) {
		this.BUY_SAM2 = BUY_SAM2;
	}

	public String getFRAN_BRNO() {
		return FRAN_BRNO;
	}

	public void setFRAN_BRNO(String FRAN_BRNO) {
		this.FRAN_BRNO = FRAN_BRNO;
	}

	public String getBUDAT() {
		return BUDAT;
	}

	public void setBUDAT(String BUDAT) {
		this.BUDAT = BUDAT;
	}

	public String getPERM_SAM() {
		return PERM_SAM;
	}

	public void setPERM_SAM(String PERM_SAM) {
		this.PERM_SAM = PERM_SAM;
	}

	public String getUTYPENM() {
		return UTYPENM;
	}

	public void setUTYPENM(String UTYPENM) {
		this.UTYPENM = UTYPENM;
	}

	public String getTRE_FRAN() {
		return TRE_FRAN;
	}

	public void setTRE_FRAN(String TRE_FRAN) {
		this.TRE_FRAN = TRE_FRAN;
	}

	public String getFRAN_REPTR() {
		return FRAN_REPTR;
	}

	public void setFRAN_REPTR(String FRAN_REPTR) {
		this.FRAN_REPTR = FRAN_REPTR;
	}

	public String getSKTEXT() {
		return SKTEXT;
	}

	public void setSKTEXT(String SKTEXT) {
		this.SKTEXT = SKTEXT;
	}

	public String getBT_CD() {
		return BT_CD;
	}

	public void setBT_CD(String BT_CD) {
		this.BT_CD = BT_CD;
	}

	public String getATTATCHNO() {
		return ATTATCHNO;
	}

	public void setATTATCHNO(String ATTATCHNO) {
		this.ATTATCHNO = ATTATCHNO;
	}

	public String getCORP_ID() {
		return CORP_ID;
	}

	public void setCORP_ID(String CORP_ID) {
		this.CORP_ID = CORP_ID;
	}

	public String getZCLOSE() {
		return ZCLOSE;
	}

	public void setZCLOSE(String ZCLOSE) {
		this.ZCLOSE = ZCLOSE;
	}

	public String getDATA_DC() {
		return DATA_DC;
	}

	public void setDATA_DC(String DATA_DC) {
		this.DATA_DC = DATA_DC;
	}

	public String getORGL_PERM_NO() {
		return ORGL_PERM_NO;
	}

	public void setORGL_PERM_NO(String ORGL_PERM_NO) {
		this.ORGL_PERM_NO = ORGL_PERM_NO;
	}

	public String getGJAHR() {
		return GJAHR;
	}

	public void setGJAHR(String GJAHR) {
		this.GJAHR = GJAHR;
	}

	public String getGUBUN() {
		return GUBUN;
	}

	public void setGUBUN(String GUBUN) {
		this.GUBUN = GUBUN;
	}

	public String getKTEXT() {
		return KTEXT;
	}

	public void setKTEXT(String KTEXT) {
		this.KTEXT = KTEXT;
	}

	public String getIFYN() {
		return IFYN;
	}

	public void setIFYN(String IFYN) {
		this.IFYN = IFYN;
	}

	public String getFRAN_PNO() {
		return FRAN_PNO;
	}

	public void setFRAN_PNO(String FRAN_PNO) {
		this.FRAN_PNO = FRAN_PNO;
	}

	public String getBUY_AMT() {
		return BUY_AMT;
	}

	public void setBUY_AMT(String BUY_AMT) {
		this.BUY_AMT = BUY_AMT;
	}

	public String getORGL_BUY_COLL_NO() {
		return ORGL_BUY_COLL_NO;
	}

	public void setORGL_BUY_COLL_NO(String ORGL_BUY_COLL_NO) {
		this.ORGL_BUY_COLL_NO = ORGL_BUY_COLL_NO;
	}

	public String getELC_USE_DIV() {
		return ELC_USE_DIV;
	}

	public void setELC_USE_DIV(String ELC_USE_DIV) {
		this.ELC_USE_DIV = ELC_USE_DIV;
	}

	public String getENAME() {
		return ENAME;
	}

	public void setENAME(String ENAME) {
		this.ENAME = ENAME;
	}

	public String getDSC_SRVC_DIV() {
		return DSC_SRVC_DIV;
	}

	public void setDSC_SRVC_DIV(String DSC_SRVC_DIV) {
		this.DSC_SRVC_DIV = DSC_SRVC_DIV;
	}

	public String getCARDNM() {
		return CARDNM;
	}

	public void setCARDNM(String CARDNM) {
		this.CARDNM = CARDNM;
	}

	public String getFRAN_ADDR() {
		return FRAN_ADDR;
	}

	public void setFRAN_ADDR(String FRAN_ADDR) {
		this.FRAN_ADDR = FRAN_ADDR;
	}

	public String getPD_DIV() {
		return PD_DIV;
	}

	public void setPD_DIV(String PD_DIV) {
		this.PD_DIV = PD_DIV;
	}

	public String getSORGEH() {
		return SORGEH;
	}

	public void setSORGEH(String SORGEH) {
		this.SORGEH = SORGEH;
	}

	public String getUSETYPE() {
		return USETYPE;
	}

	public void setUSETYPE(String USETYPE) {
		this.USETYPE = USETYPE;
	}

	public String getPERNR() {
		return PERNR;
	}

	public void setPERNR(String PERNR) {
		this.PERNR = PERNR;
	}

	public String getDOCTYPE() {
		return DOCTYPE;
	}

	public void setDOCTYPE(String DOCTYPE) {
		this.DOCTYPE = DOCTYPE;
	}

	public String getBUY_CLCTN_NO() {
		return BUY_CLCTN_NO;
	}

	public void setBUY_CLCTN_NO(String BUY_CLCTN_NO) {
		this.BUY_CLCTN_NO = BUY_CLCTN_NO;
	}

	public String getPT_BUY_CNC_YN() {
		return PT_BUY_CNC_YN;
	}

	public void setPT_BUY_CNC_YN(String PT_BUY_CNC_YN) {
		this.PT_BUY_CNC_YN = PT_BUY_CNC_YN;
	}

	public String getCARDOWRTXT() {
		return CARDOWRTXT;
	}

	public void setCARDOWRTXT(String CARDOWRTXT) {
		this.CARDOWRTXT = CARDOWRTXT;
	}

	public String getCARDKINDNM() {
		return CARDKINDNM;
	}

	public void setCARDKINDNM(String CARDKINDNM) {
		this.CARDKINDNM = CARDKINDNM;
	}

	public String getECFEE_AMT() {
		return ECFEE_AMT;
	}

	public void setECFEE_AMT(String ECFEE_AMT) {
		this.ECFEE_AMT = ECFEE_AMT;
	}

	public String getBUY_PTM() {
		return BUY_PTM;
	}

	public void setBUY_PTM(String BUY_PTM) {
		this.BUY_PTM = BUY_PTM;
	}

	public String getDOCSTATNM() {
		return DOCSTATNM;
	}

	public void setDOCSTATNM(String DOCSTATNM) {
		this.DOCSTATNM = DOCSTATNM;
	}

	public String getELC_BUY_DT() {
		return ELC_BUY_DT;
	}

	public void setELC_BUY_DT(String ELC_BUY_DT) {
		this.ELC_BUY_DT = ELC_BUY_DT;
	}

	public String getSLIP_DEAL_DC() {
		return SLIP_DEAL_DC;
	}

	public void setSLIP_DEAL_DC(String SLIP_DEAL_DC) {
		this.SLIP_DEAL_DC = SLIP_DEAL_DC;
	}

	public String getSPERN() {
		return SPERN;
	}

	public void setSPERN(String SPERN) {
		this.SPERN = SPERN;
	}

	public String getBUKRS() {
		return BUKRS;
	}

	public void setBUKRS(String BUKRS) {
		this.BUKRS = BUKRS;
	}

	public boolean isCheckFlag() {
		return bCheck;
	}

	public void setCheckFlag(boolean bCheck) {
		this.bCheck = bCheck;
	}

}
