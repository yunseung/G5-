package com.ktrental.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ktrental.R;
import com.ktrental.popup.CallSendPopup2;
import com.ktrental.popup.EventPopupC;
import com.ktrental.util.kog;

public class MaintenanceCheckListAdapter extends BaseCommonAdapter<HashMap<String, String>> {
	
    private int layout;
	private int checkPosition = 0;
	private ArrayList<HashMap<String, String>> list;
	
	public MaintenanceCheckListAdapter(Context context, int layout, ArrayList<HashMap<String, String>> list, String _is_CarManager)
	    {
			super(context);
			mContext = context;
			this.layout = layout;
			mItemArray = list;
			this.list = list;
	    }
	
	public MaintenanceCheckListAdapter(Context context, int layout, ArrayList<HashMap<String, String>> list)
    {
		super(context);
		mContext = context;
		this.layout = layout;
		mItemArray = list;
		this.list = list;
    }

	 public View getView(int position, View convertView, ViewGroup parent)
	     {
    	 View v = convertView;
    	
    	 if (v == null)
    	     {
    	     LayoutInflater vi = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    	     v = vi.inflate(layout, null);
    	     }
	
    	 HashMap<String, String> item = list.get(position);
    	
    	 LinearLayout ll = (LinearLayout)v.findViewById(R.id.row_back_id);
    	 TextView row_checklist_00 = (TextView) v.findViewById(R.id.row_checklist_00);
    	 TextView row_checklist_01 = (TextView) v.findViewById(R.id.row_checklist_01);
    	 TextView row_checklist_02 = (TextView) v.findViewById(R.id.row_checklist_02);
    	 TextView row_checklist_03 = (TextView) v.findViewById(R.id.row_checklist_03);
    	 TextView row_checklist_04 = (TextView) v.findViewById(R.id.row_checklist_04);
    	 TextView row_checklist_05 = (TextView) v.findViewById(R.id.row_checklist_05);
    	 TextView row_checklist_06 = (TextView) v.findViewById(R.id.row_checklist_06);
    	 TextView row_checklist_07 = (TextView) v.findViewById(R.id.row_checklist_07);
    	 TextView row_checklist_08 = (TextView) v.findViewById(R.id.row_checklist_08);
    	 TextView row_checklist_09 = (TextView) v.findViewById(R.id.row_checklist_09);
    	 TextView row_checklist_10 = (TextView) v.findViewById(R.id.row_checklist_10);
    	 TextView row_checklist_11 = (TextView) v.findViewById(R.id.row_checklist_11);
    	 

//    	 row1.setText(position + "");
    	 row_checklist_00.setText("" + (position + 1));
    	 row_checklist_01.setText(item.get("AUFNR"));
    	 row_checklist_02.setText(item.get("CCINVNR"));
    	 row_checklist_03.setText(item.get("CEMER"));
    	 row_checklist_04.setText(item.get("DLSM1"));
    	 row_checklist_05.setText(item.get("GLTRI"));
    	 row_checklist_06.setText(item.get("CCMINVNR"));
    	 row_checklist_07.setText(item.get("NAME1"));
    	 row_checklist_08.setText(item.get("CYCMNT"));
    	 row_checklist_09.setText(item.get("ENAME"));
    	 row_checklist_10.setText(item.get("CTRNM"));
    	 row_checklist_11.setText(item.get("STSNM"));
    	 
    	 
    	 
    	     	 
    	 
    	 return v;
    	 }
	 

    @Override
    protected void bindView(View rootView, Context context, int position)
        {
        // TODO Auto-generated method stub
        
        }

    @Override
    protected View newView(Context context, int position, ViewGroup viewgroup)
        {
        // TODO Auto-generated method stub
        return null;
        }

//	@Override
//	protected void bindView(View rootView, Context context, int position)
//	    {
//		AddressSearchViewHolder addressSearchViewHolder = (AddressSearchViewHolder) rootView.getTag();
//
//		HashMap<String, String> item = mItemArray.get(position);
//
//		addressSearchViewHolder.tvNum.setText(position + "");
//		String encrypted = item.get("POST_CODE1");
//		addressSearchViewHolder.tvZipcode.setText(encrypted);
//		encrypted = item.get("FULL_ADDR");
//		addressSearchViewHolder.tvAddress.setText(encrypted);
//
//		if (checkPosition == position)
//		    {
//			addressSearchViewHolder.llBack.setBackgroundResource(R.drawable.table_list_s);
//		    }
//		else{
//			addressSearchViewHolder.llBack.setBackgroundResource(R.drawable.table_list_n);
//		    }
//	    }

//	@Override
//	protected View newView(Context context, int position, ViewGroup viewgroup) {
//		View rootView = mInflater.inflate(layout, null);
//
//		AddressSearchViewHolder addressSearchViewHolder = new AddressSearchViewHolder();
//
//		addressSearchViewHolder.rootView = rootView;
//
//		addressSearchViewHolder.llBack = (LinearLayout)rootView.findViewById(R.id.asr_back_id);
//		
//		addressSearchViewHolder.tvNum = (TextView) rootView
//				.findViewById(R.id.asr_num_id);
//		addressSearchViewHolder.tvZipcode = (TextView) rootView
//				.findViewById(R.id.asr_zipcode_id);
//		addressSearchViewHolder.tvAddress = (TextView) rootView
//				.findViewById(R.id.asr_address_id);
//
//		rootView.setTag(addressSearchViewHolder);
//
//		return rootView;
//	}

//	@Override
//	public void releaseResouces() {
//		// TODO Auto-generated method stub
//		super.releaseResouces();
//	}

//	private class AddressSearchViewHolder {
//		TextView tvNum;
//		TextView tvZipcode;
//		TextView tvAddress;
//		LinearLayout llBack;
//		View rootView;
//	}

	public void setCheckPosition(int position) {
		checkPosition = position;
		kog.e("Jonathan", position + " 눌리니?? ㅋ");
		notifyDataSetChanged();
	}

	public int getCheckPosition() {
		return checkPosition;
	}

//	public void setCheckPosition(int checkPosition) {
//		this.checkPosition = checkPosition;
//	}

}
