package com.ktrental.model;

public class CorCardLockCheckModel {
	private String LOG_CK = "";



	private String SES_ID = "";
	private String SVR_IP = "";
	private String FNL_IP = "";
	private String FNL_DAT = "";
	private String FNL_TIM = "";
	private String FSP_DAT = "";
	private String FSP_TIM = "";

	public CorCardLockCheckModel(String log_ck, String ses_id, String svr_ip,
                                 String fnl_ip, String fnl_dat, String fnl_tim, String fsp_dat,
                                 String fsp_tim) {
		super();
		LOG_CK = log_ck;
		SES_ID = ses_id;
		SVR_IP = svr_ip;
		FNL_IP = fnl_ip;
		FNL_DAT = fnl_dat;
		FNL_TIM = fnl_tim;
		FSP_DAT = fsp_dat;
		FSP_TIM = fsp_tim;
	}

	public String getLOG_CK() {
		return LOG_CK;
	}

	public void setLOG_CK(String LOG_CK) {
		this.LOG_CK = LOG_CK;
	}

	public String getSES_ID() {
		return SES_ID;
	}

	public void setSES_ID(String SES_ID) {
		this.SES_ID = SES_ID;
	}

	public String getSVR_IP() {
		return SVR_IP;
	}

	public void setSVR_IP(String SVR_IP) {
		this.SVR_IP = SVR_IP;
	}

	public String getFNL_IP() {
		return FNL_IP;
	}

	public void setFNL_IP(String FNL_IP) {
		this.FNL_IP = FNL_IP;
	}

	public String getFNL_DAT() {
		return FNL_DAT;
	}

	public void setFNL_DAT(String FNL_DAT) {
		this.FNL_DAT = FNL_DAT;
	}

	public String getFNL_TIM() {
		return FNL_TIM;
	}

	public void setFNL_TIM(String FNL_TIM) {
		this.FNL_TIM = FNL_TIM;
	}

	public String getFSP_DAT() {
		return FSP_DAT;
	}

	public void setFSP_DAT(String FSP_DAT) {
		this.FSP_DAT = FSP_DAT;
	}

	public String getFSP_TIM() {
		return FSP_TIM;
	}

	public void setFSP_TIM(String FSP_TIM) {
		this.FSP_TIM = FSP_TIM;
	}

}
