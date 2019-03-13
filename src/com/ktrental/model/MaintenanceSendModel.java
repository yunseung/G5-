package com.ktrental.model;

import java.util.ArrayList;
import java.util.HashMap;

public class MaintenanceSendModel {
	private MaintenanceSendBaseModel maintenanceSendBaseModel;
	private ArrayList<MaintenanceSendStockModel> maintenanceSendStockModels;
	private ArrayList<MaintenanceSendSignModel> mMaintenanceSendSignModels = new ArrayList<MaintenanceSendSignModel>();

	public MaintenanceSendModel(
			MaintenanceSendBaseModel maintenanceSendBaseModel,
			ArrayList<MaintenanceSendStockModel> maintenanceSendStockModels,
			ArrayList<MaintenanceSendSignModel> signModels) {
		super();
		this.maintenanceSendBaseModel = maintenanceSendBaseModel;
		this.maintenanceSendStockModels = maintenanceSendStockModels;
		mMaintenanceSendSignModels = signModels;
	}

	public MaintenanceSendBaseModel getMaintenanceSendBaseModel() {
		return maintenanceSendBaseModel;
	}

	public void setMaintenanceSendBaseModel(
			MaintenanceSendBaseModel maintenanceSendBaseModel) {
		this.maintenanceSendBaseModel = maintenanceSendBaseModel;
	}

	public ArrayList<MaintenanceSendStockModel> getMaintenanceSendStockModel() {
		return maintenanceSendStockModels;
	}

	public void setMaintenanceSendStockModel(
			ArrayList<MaintenanceSendStockModel> maintenanceSendStockModels) {
		this.maintenanceSendStockModels = maintenanceSendStockModels;
	}

	public ArrayList<HashMap<String, String>> getStockSendArray() {
		ArrayList<HashMap<String, String>> array = new ArrayList<HashMap<String, String>>();
		//

		for (MaintenanceSendStockModel stockModel : maintenanceSendStockModels) {
			array.add(stockModel.getmHashMap());
		}

		return array;
	}

	public ArrayList<HashMap<String, String>> getSignSendArray() {
		ArrayList<HashMap<String, String>> array = new ArrayList<HashMap<String, String>>();
		//

		for (MaintenanceSendSignModel sendSignModel : mMaintenanceSendSignModels) {
			array.add(sendSignModel.getmHashMap());
		}

		return array;
	}
}
