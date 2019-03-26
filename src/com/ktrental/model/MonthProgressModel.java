package com.ktrental.model;

public class MonthProgressModel extends BaseMaintenanceModel {
	private String cermr = "";

	public MonthProgressModel(String _time, String customer_name, String dirver_name, String _carNum,
			String _address, String _carname, String _tel,
			String _progress_status, String _day, String AUFNR, String EQUNR,
			String CTRTY, String postCode, String city, String street,
			String drv_mob, String _cermr, String green2, String txt30, String _mdlcd, String _vocNum, String _kunnr, String delay, String cycmn_tx, String apm) {
		super(customer_name, dirver_name, _carNum, _address, _tel, _time, _carname,
				_progress_status, _day, AUFNR, EQUNR, CTRTY, postCode, city,
				street, drv_mob, green2, txt30, _mdlcd, _vocNum, _kunnr, delay, cycmn_tx, apm);
		cermr = _cermr;
	}

	public String getCermr() {
		return cermr;
	}

	public void setCermr(String cermr) {
		this.cermr = cermr;
	}

}
