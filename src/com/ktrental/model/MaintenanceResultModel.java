package com.ktrental.model;

import java.util.ArrayList;

public class MaintenanceResultModel {
	ArrayList<MaintenanceItemModel> mLastItemModels = new ArrayList<MaintenanceItemModel>();

	EtcModel mEtcModel;
	
	CarInfoModel mCarInfoModel;

	public MaintenanceResultModel(
			ArrayList<MaintenanceItemModel> mLastItemModels, EtcModel mEtcModel,CarInfoModel carInfoModel) {
		super();
		this.mLastItemModels = mLastItemModels;
		this.mEtcModel = mEtcModel;
		mCarInfoModel = carInfoModel;
	}

	public ArrayList<MaintenanceItemModel> getmLastItemModels() {
		return mLastItemModels;
	}

	public void setmLastItemModels(
			ArrayList<MaintenanceItemModel> mLastItemModels) {
		this.mLastItemModels = mLastItemModels;
	}

	public EtcModel getmEtcModel() {
		return mEtcModel;
	}

	public void setmEtcModel(EtcModel mEtcModel) {
		this.mEtcModel = mEtcModel;
	}

	public CarInfoModel getmCarInfoModel() {
		return mCarInfoModel;
	}

	public void setmCarInfoModel(CarInfoModel mCarInfoModel) {
		this.mCarInfoModel = mCarInfoModel;
	}
	
	
	
}
