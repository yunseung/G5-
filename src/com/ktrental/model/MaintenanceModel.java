package com.ktrental.model;

public class MaintenanceModel extends BaseMaintenanceModel {

	private String status;
	private String cermr = "";
	private String drv_mob = "";

	public MaintenanceModel(String _time, String customer_name, String driver_name, String _carNum,
			String _address, String _carname, String _status, String _tel,
			String _progress_status, String _day, String AUFNR, String EQUNR,
			String CTRTY, String postCode, String city, String street,
			String drv_mob, String _cermr, String _gueen2, String _txt30, String _mdlcd, String _vocID, String _kunnr, String delay, String CYCMN_TX) {
		super(customer_name, driver_name, _carNum, _address, _tel, _time, _carname,
				_progress_status, _day, AUFNR, EQUNR, CTRTY, postCode, city,
				street, drv_mob, _gueen2, _txt30, _mdlcd , _vocID, _kunnr, delay, CYCMN_TX);

		status = _status;
		cermr = _cermr;
		this.drv_mob = drv_mob;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	public String getCermr() {
		return cermr;
	}

	public void setCermr(String cermr) {
		this.cermr = cermr;
	}
	
	public String getdrv_mob() {
		return drv_mob;
	}

	public void setdrv_mob(String drv_mob) {
		this.drv_mob = drv_mob;
	}
	


}
