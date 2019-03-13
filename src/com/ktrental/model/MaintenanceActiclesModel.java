package com.ktrental.model;

public class MaintenanceActiclesModel {

	private String invnr = "";
	private String ename = "";
	private String hpphon = "";

	public MaintenanceActiclesModel(String _invnr, String _ename, String _hpphon) {
		invnr = _invnr;
		ename = _ename;
		hpphon = _hpphon;
	}

	public String getInvnr() {
		return invnr;
	}

	public void setInvnr(String invnr) {
		this.invnr = invnr;
	}

	public String getEname() {
		return ename;
	}

	public void setEname(String ename) {
		this.ename = ename;
	}

	public String getHpphon() {
		return hpphon;
	}

	public void setHpphon(String hpphon) {
		this.hpphon = hpphon;
	}

}
