package com.ktrental.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.ktrental.model.BaseMaintenanceModel;

import java.util.ArrayList;

public class SchelduleFragment extends BaseFragment implements OnClickListener {

	private ArrayList<BaseMaintenanceModel> mBaseMaintenanceModels = new ArrayList<BaseMaintenanceModel>();

	public SchelduleFragment(
			ArrayList<BaseMaintenanceModel> mBaseMaintenanceModels) {
		super();
		this.mBaseMaintenanceModels = mBaseMaintenanceModels;
	}

	public SchelduleFragment(){}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		return super.onCreateView(inflater, container, savedInstanceState);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

	}

}
