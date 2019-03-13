package com.ktrental.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ktrental.R;
import com.ktrental.beans.PM033;
import com.ktrental.product.Menu2_1_Activity;

public class Menu4_Tire_Adapter_PM033 extends BaseCommonAdapter<PM033> {
	
    private int layout;
	private int checkPosition = -1;
	private ArrayList<PM033> list;
	private Context context;

	public Menu4_Tire_Adapter_PM033(Context context, int layout, ArrayList<PM033> list)
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
    	 LayoutInflater vi = null;
    	 if (v == null)
    	     {
    	     vi = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    	     v = vi.inflate(layout, null);
    	     }
	
    	 final PM033 item = list.get(position);
    	 
    	 
    	 LinearLayout ll = (LinearLayout)v.findViewById(R.id.row_back_id);
    	 ImageView row1 = (ImageView) v.findViewById(R.id.tire_row_id1);
    	 TextView row2 = (TextView) v.findViewById(R.id.tire_row_id2);
    	 TextView row3 = (TextView) v.findViewById(R.id.tire_row_id3);
    	 ImageView row4 = (ImageView) v.findViewById(R.id.tire_row_id4);

    	 if(Menu2_1_Activity.pm033_del_arr.contains(item.ZCODEV)) row1.setImageResource(R.drawable.check_on);
    	 else                                                 row1.setImageResource(R.drawable.check_off);
    	 
    	 row2.setText(item.ZCODEVT);
    	 
    	 if(item.ONESIDE_WEAR) { row3.setText("O"); }
    	 else                  { row3.setText("X"); }
    	 
    	 if(!item.PATH.equals("")) { row4.setVisibility(View.VISIBLE); }
    	 else                      { row4.setVisibility(View.INVISIBLE); }
    	 
//    	 if (checkPosition == position) { ll.setBackgroundResource(R.drawable.table_list_s); }
//         else                           { ll.setBackgroundResource(R.drawable.table_list_n); }

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
		notifyDataSetChanged();
	}

	public int getCheckPosition() {
		return checkPosition;
	}

//	public void setCheckPosition(int checkPosition) {
//		this.checkPosition = checkPosition;
//	}

}
