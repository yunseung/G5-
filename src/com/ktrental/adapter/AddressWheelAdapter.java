package com.ktrental.adapter;

import java.util.ArrayList;

import android.content.Context;

public class AddressWheelAdapter extends AbstractWheelTextAdapter {

	private ArrayList<String> mTextArray;

	public AddressWheelAdapter(Context context, ArrayList<String> aTextArray) {
		super(context);

		mTextArray = aTextArray;
	}

	@Override
	public int getItemsCount() {
		// TODO Auto-generated method stub
		return mTextArray.size();
	}

	@Override
	protected CharSequence getItemText(int index) {

		String text = mTextArray.get(index);

		return text;
	}

}
