package com.ktrental.model;

import com.ktrental.util.kog;

public class MaintenanceItemModel implements Cloneable {
	private String name;
	private boolean check = false;
	private int consumption = 0;
	private int stock = 0;

	private int totalConsumption = 0;

	public int getTotalConsumption() {
		return totalConsumption;
	}

	public void setTotalConsumption(int totalConsumption) {
		this.totalConsumption = totalConsumption;
	}

	private MaintenanceGroupModel maintenanceGroupModel;
	private int selectConsumption = 0;
	private String MATNR;
	private String ERFME;
	private String GRP_CD = "";

	public String MDLCD = "";
	public String FUELCD = "";
	public String MAKTX = "";

	// Jonathan 14.11.19 추가
	private String MTQTY;

	// hjt 18.06.07 추가
	private String MINQTY = "";
	private String MAXQTY = "";


	public MaintenanceItemModel(MaintenanceGroupModel group, String _GRP_CD) {
		// TODO Auto-generated constructor stub
		maintenanceGroupModel = group;
		GRP_CD = _GRP_CD;
	}

	public int getSelectcConsumption() {
		return selectConsumption;
	}

	public void setSelectcConsumption(int selectcConsumption) {
		this.selectConsumption = selectcConsumption;
	}

	/**
	 * 이 객체의 복제품을 리턴한다.
	 */
	public MaintenanceItemModel clone() {
		// 내 객체 생성
		MaintenanceItemModel objReturn;

		try {
			objReturn = (MaintenanceItemModel) super.clone();
			return objReturn;
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
			return null;
		}

	}// end clone

	public MaintenanceGroupModel getMaintenanceGroupModel() {
		return maintenanceGroupModel;
	}

	public void setMaintenanceGroupModel(MaintenanceGroupModel maintenanceGroupModel) {
		this.maintenanceGroupModel = maintenanceGroupModel;
	}

	public MaintenanceItemModel(MaintenanceGroupModel group, String matnr, int consumption) {
		// TODO Auto-generated constructor stub
		maintenanceGroupModel = group;
		this.MATNR = matnr;
		this.consumption = consumption;
	}

	public MaintenanceItemModel(String name, int stock, String MATNR, String ERFME, MaintenanceGroupModel groupModel,
								String _GRP_CD, String _MTQTY, String _MINQTY, String _MAXQTY
								// ,String FUELCD
								// ,String MAKTX
	) {

		this.name = name;
		this.stock = stock;
		this.MATNR = MATNR;
		this.ERFME = ERFME;
		MTQTY = _MTQTY;
		MINQTY = _MINQTY;
		MAXQTY = _MAXQTY;

		// this.MDLCD = MDLCD;
		// this.FUELCD = FUELCD;
		// this.MAKTX = MAKTX;

		kog.e("KDH", "MDLCD = " + MDLCD);
		kog.e("KDH", "FUELCD = " + FUELCD);
		kog.e("KDH", "MAKTX = " + MAKTX);
		maintenanceGroupModel = groupModel;
		GRP_CD = _GRP_CD;
	}

	// public MaintenanceItemModel(String name, int stock, String MATNR,
	// String ERFME, MaintenanceGroupModel groupModel, String _GRP_CD, String
	// MTQTY) {
	// this.name = name;
	// this.stock = stock;
	// this.MATNR = MATNR;
	// this.ERFME = ERFME;
	// maintenanceGroupModel = groupModel;
	// GRP_CD = _GRP_CD;
	// this.MTQTY = MTQTY;
	// }

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isCheck() {
		return check;
	}

	public void setCheck(boolean check) {
		this.check = check;
	}

	public int getConsumption() {
		return consumption;
	}

	public void setConsumption(int _consumption) {
		this.consumption = _consumption;
	}

	public int getStock() {
		return stock;
	}

	public void setStock(int stock) {
		this.stock = stock;
	}

	public String getMATNR() {
		return MATNR;
	}

	public void setMATNR(String mATNR) {
		MATNR = mATNR;
	}

	public String getERFME() {
		return ERFME;
	}

	public void setERFME(String eRFME) {
		ERFME = eRFME;
	}

	public int getSelectConsumption() {
		return selectConsumption;
	}

	public void setSelectConsumption(int selectConsumption) {
		this.selectConsumption = selectConsumption;
	}

	public String getGRP_CD() {
		return GRP_CD;
	}

	public void setGRP_CD(String gRP_CD) {
		GRP_CD = gRP_CD;
	}

	public String getMTQTY() {
		return MTQTY;
	}

	public void setMTQTY(String mTQTY) {
		MTQTY = mTQTY;
	}

	public String getMINQTY() {
		return MINQTY;
	}

	public void setMINQTY(String minqty) {
		MINQTY = minqty;
	}

	public String getMAXQTY() {
		return MAXQTY;
	}

	public void setMAXQTY(String maxqty) {
		MAXQTY = maxqty;
	}

}
