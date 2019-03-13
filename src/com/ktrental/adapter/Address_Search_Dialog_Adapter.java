package com.ktrental.adapter;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ktrental.R;

public class Address_Search_Dialog_Adapter extends
		BaseCommonAdapter<HashMap<String, String>> {
	private int layout;

	private int checkPosition = -1;


	public Address_Search_Dialog_Adapter(Context context, int layout,
			ArrayList<HashMap<String, String>> list) {
		super(context);
		mContext = context;
		this.layout = layout;
		mItemArray = list;

	}


	@Override
	protected void bindView(View rootView, Context context, int position)
	    {
		AddressSearchViewHolder addressSearchViewHolder = (AddressSearchViewHolder) rootView.getTag();

		HashMap<String, String> item = mItemArray.get(position);

		addressSearchViewHolder.tvNum.setText((position+1) + "");
		String encrypted = item.get("POST_CODE1");
		addressSearchViewHolder.tvZipcode.setText(encrypted);
		encrypted = item.get("FULL_ADDR");
		addressSearchViewHolder.tvAddress.setText(encrypted);

		if (checkPosition == position)
		    {
			addressSearchViewHolder.llBack.setBackgroundResource(R.drawable.table_list_s);
		    }
		else{
			addressSearchViewHolder.llBack.setBackgroundResource(R.drawable.table_list_n);
		    }
	    }

	@Override
	protected View newView(Context context, int position, ViewGroup viewgroup) {
		View rootView = mInflater.inflate(layout, null);

		AddressSearchViewHolder addressSearchViewHolder = new AddressSearchViewHolder();

		addressSearchViewHolder.rootView = rootView;

		addressSearchViewHolder.llBack = (LinearLayout)rootView.findViewById(R.id.asr_back_id);
		
		addressSearchViewHolder.tvNum = (TextView) rootView
				.findViewById(R.id.asr_num_id);
		addressSearchViewHolder.tvZipcode = (TextView) rootView
				.findViewById(R.id.asr_zipcode_id);
		addressSearchViewHolder.tvAddress = (TextView) rootView
				.findViewById(R.id.asr_address_id);

		rootView.setTag(addressSearchViewHolder);

		return rootView;
	}

	@Override
	public void releaseResouces() {
		// TODO Auto-generated method stub
		super.releaseResouces();
	}

	private class AddressSearchViewHolder {
		TextView tvNum;
		TextView tvZipcode;
		TextView tvAddress;
		LinearLayout llBack;
		View rootView;
	}

	public void checkItem(int position) {
		checkPosition = position;
		notifyDataSetChanged();
	}

	public int getCheckPosition() {
		return checkPosition;
	}

	public void setCheckPosition(int checkPosition) {
		this.checkPosition = checkPosition;
	}

}
