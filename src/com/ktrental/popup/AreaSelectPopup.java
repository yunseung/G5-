package com.ktrental.popup;

import java.util.ArrayList;
import java.util.HashMap;

import kankan.wheel.widget.OnWheelChangedListener;
import kankan.wheel.widget.WheelView;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout.LayoutParams;
import android.widget.PopupWindow;

import com.ktrental.R;
import com.ktrental.adapter.AddressWheelAdapter;

public class AreaSelectPopup extends PopupWindow {

	private Context mContext;
	private View mAnchorView;
	private ArrayList<String> mBuildingStructArr = new ArrayList<String>();
	private AddressWheelAdapter mAddressWheelAdapter;
	private WheelView mCityView;
	private WheelView mGuView;

	private String currentText = "";
	private OnWheelChangedListener mOnWheelChangedListener;

	private HashMap<String, ArrayList<String>> address_hash = new HashMap<String, ArrayList<String>>();
	private ArrayList<String> keys = new ArrayList<String>();

	public AreaSelectPopup(View root, Context context, int orientation,
			int layoutId, HashMap<String, ArrayList<String>> _address_hash,
			ArrayList<String> _keys, OnWheelChangedListener listener) {
		super(root, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		mOnWheelChangedListener = listener;
		mContext = context;
		address_hash = _address_hash;
		keys = _keys;

		initSettingView(root);

	}

	private void initSettingView(View root) {
	    
		mCityView = (WheelView) root.findViewById(R.id.city);
		mCityView.setCenterDrawable(mContext.getResources().getDrawable(
				R.drawable.wheel_val));
		mCityView.addChangingListener(mOnWheelChangedListener);

		mGuView = (WheelView) root.findViewById(R.id.gu);
		mGuView.setCenterDrawable(mContext.getResources().getDrawable(
				R.drawable.wheel_val));
		mGuView.addChangingListener(mOnWheelChangedListener);

		AddressWheelAdapter cityAdapter = new AddressWheelAdapter(mContext,
				keys);
		cityAdapter.setItemResource(R.layout.wheel_text_item);
		cityAdapter.setItemTextResource(R.id.text);
		mCityView.setViewAdapter(cityAdapter);

		ArrayList<String> array = (ArrayList<String>) address_hash.get(keys
				.get(0));

		if (array != null) {

		} else {
			array = new ArrayList<String>();
		}
		if (array.size() < 1)
			array.add("   ");

		AddressWheelAdapter guAdapter = new AddressWheelAdapter(mContext, array);
		guAdapter.setItemResource(R.layout.wheel_text_item);
		guAdapter.setItemTextResource(R.id.text);
		mGuView.setViewAdapter(guAdapter);

		root.findViewById(R.id.btn_exit).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						dismiss();
					}
				});

	}

	public void setGuArray(int cityIndex) {
		ArrayList<String> array = (ArrayList<String>) address_hash.get(keys
				.get(cityIndex));

		if (array != null) {

		} else {
			array = new ArrayList<String>();
		}
		if (array.size() < 1)
			array.add("   ");

		AddressWheelAdapter guAdapter = new AddressWheelAdapter(mContext, array);
		guAdapter.setItemResource(R.layout.wheel_text_item);
		guAdapter.setItemTextResource(R.id.text);
		mGuView.setViewAdapter(guAdapter);
		mGuView.setCurrentItem(0);
	}
}
