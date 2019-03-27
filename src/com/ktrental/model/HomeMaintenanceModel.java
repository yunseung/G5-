package com.ktrental.model;

import android.util.Log;

import com.ktrental.util.LogUtil;

public class HomeMaintenanceModel extends BaseMaintenanceModel {

	private String status;

	private String NIELS_NM;
	private String LDATE;
	private String customerName;
	private String reoil = "";
	private String cermr = "";
	private String CCMRQ = "";
	
	public String getCermr() {
		return cermr;
	}

	public void setCermr(String cermr) {
		this.cermr = cermr;
	}

	public HomeMaintenanceModel(String _time, String customer_name, String driver_name, String _carNum,
			String _address, String _carname, String _status, String _tel,
			String _progress_status, String _day, String AUFNR, String EQUNR,
			String CTRTY, String postCode, String city, String street,
			String drv_mob, String NIELS_NM, String LDATE, String customerName,
			String _reoil,String _cermr, String _ccmrq, String green2, String txt30, String _mdlcd, String _vocNum, String _kunnr, String delay, String vbeln, String gubun) {
		super(customer_name, driver_name, _carNum, _address, _tel, _time, _carname,
				_progress_status, _day, AUFNR, EQUNR, CTRTY, postCode, city,
				street, drv_mob, green2, txt30, _mdlcd, _vocNum, _kunnr, delay, null, null, vbeln, gubun);
		
		status = _status;
		this.NIELS_NM = NIELS_NM;
		this.LDATE = LDATE;
		this.customerName = customerName;
		reoil = _reoil;
		cermr = _cermr;
		CCMRQ = _ccmrq;
		LogUtil.d("hjt", "delay = " + delay);
	}

	public String getStatus() {
		return status;
	}
	
	public String getCCMRQ() {
		return CCMRQ;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getNIELS_NM() {
		return NIELS_NM;
	}

	public void setNIELS_NM(String nIELS_NM) {
		NIELS_NM = nIELS_NM;
	}

	public String getLDATE() {
		return LDATE;
	}

	public void setLDATE(String lDATE) {
		LDATE = lDATE;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getReoil() {
		if (reoil.equals("X"))
			reoil = "엔진오일교체";
		else
			reoil = "";

		return reoil;
	}

	public void setReoil(String reoil) {
		this.reoil = reoil;
	}

}
