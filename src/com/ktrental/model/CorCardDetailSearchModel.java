package com.ktrental.model;

import java.util.ArrayList;
import java.util.HashMap;

public class CorCardDetailSearchModel {

	/** 상세조회 할 시 같은 HashMap 2벌이 온다 */
	private HashMap<String, String> hashMap1;
	private HashMap<String, String> hashMap2;

	private ArrayList<HashMap<String, String>> mDetailSearchModel;

	public CorCardDetailSearchModel(ArrayList<HashMap<String, String>> model){
		super();
		setmDetailSearchModel(model);

	}

	public ArrayList<HashMap<String, String>> getmDetailSearchModel() {
		return mDetailSearchModel;
	}

	public void setmDetailSearchModel(ArrayList<HashMap<String, String>> mDetailSearchModel) {
		this.mDetailSearchModel = mDetailSearchModel;
	}

}
