package com.ktrental.model;

import android.util.Log;

public class MonthProgressModel extends BaseMaintenanceModel {
	private String cermr = "";
	private String ccmrq = "";
	private String prerq = "";

	public MonthProgressModel(String _time, String customer_name, String dirver_name, String _carNum,
			String _address, String _carname, String _tel,
			String _progress_status, String _day, String AUFNR, String EQUNR,
			String CTRTY, String postCode, String city, String street,
			String drv_mob, String _cermr, String green2, String txt30, String _mdlcd, String _vocNum, String _kunnr, String delay, String cycmn_tx, String apm, String vbeln, String gubun, String reqNo, String ccmrq, String atvyn, String prerq) {
		super(customer_name, dirver_name, _carNum, _address, _tel, _time, _carname,
				_progress_status, _day, AUFNR, EQUNR, CTRTY, postCode, city,
				street, drv_mob, green2, txt30, _mdlcd, _vocNum, _kunnr, delay, cycmn_tx, apm, vbeln, gubun, reqNo, atvyn, prerq, ccmrq);
		cermr = _cermr;
		this.ccmrq = ccmrq;
		this.prerq = prerq;
	}

	public String getCermr() {
		return cermr;
	}

	public void setCermr(String cermr) {
		this.cermr = cermr;
	}

	public String getCcmrq() {
		return ccmrq;
	}

	public String getPrerq() {
		return prerq;
	}
}
