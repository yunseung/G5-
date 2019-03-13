package com.ktrental.model;

import com.ktrental.calendar.DayInfoModel;

public class RepairDayInfoModel {
	private RepairPlanModel mRepairPlanModel;
	private DayInfoModel mDayInfoModel;

	public RepairDayInfoModel(DayInfoModel dayInfoModel) {
		mDayInfoModel = dayInfoModel;
	}
	
	public DayInfoModel getDayInfoModel() {
		return mDayInfoModel;
	}

	public void setDayInfoModel(DayInfoModel dayInfoModel) {
		mDayInfoModel = dayInfoModel;
	}

	public RepairPlanModel getRepairPlanModel() {
		return mRepairPlanModel;
	}

	public void setRepairPlanModel(RepairPlanModel _RepairPlanModel) {
		this.mRepairPlanModel = _RepairPlanModel;
	}

}
