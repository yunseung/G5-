package com.ktrental.adapter;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ktrental.R;
import com.ktrental.dialog.Delivery_Point_Dialog;
import com.ktrental.util.kog;

public class Delivery_Point_Dialog_Adapter extends BaseCommonAdapter<HashMap<String, String>> {
	
    private int layout;
	private int checkPosition = -1;
	private ArrayList<HashMap<String, String>> list;
	CheckBox row1;
	
	
	public Delivery_Point_Dialog_Adapter(Context context, int layout, ArrayList<HashMap<String, String>> list)
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
    	 
    	 final int final_position = position;
	
    	 HashMap<String, String> item = list.get(position);
    	
    	 LinearLayout ll = (LinearLayout)v.findViewById(R.id.row_back_id);

    	 row1 = (CheckBox) v.findViewById(R.id.row_id1);
    	 row1.setVisibility(View.VISIBLE);
    	 TextView row2 = (TextView) v.findViewById(R.id.row_id2);
    	 row2.setVisibility(View.GONE);
    	 TextView row3 = (TextView) v.findViewById(R.id.row_id3);
    	 TextView row4 = (TextView) v.findViewById(R.id.row_id4);
    	 TextView row5 = (TextView) v.findViewById(R.id.row_id5);
    	 TextView row6 = (TextView) v.findViewById(R.id.row_id6);
    	 TextView row7 = (TextView) v.findViewById(R.id.row_id7);

    	 
    	 kog.e("Jonathan", "Jonathan000");
    	 
    	 kog.e("Jonathan", "Jonathan :: " + position);
    	 
    	 
    	 
    	row1.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				// TODO Auto-generated method stub
				if(arg0.isChecked())
				{
					if (!Delivery_Point_Dialog.check_list.contains(final_position))
	                {
	                	Delivery_Point_Dialog.check_list.add(final_position);
	                }
				}
				else
				{
					Delivery_Point_Dialog.check_list.remove(Delivery_Point_Dialog.check_list.indexOf(final_position));
				}
				
				
			}
		});
    	 
    	 
//    	 if(Delivery_Point_Dialog.check_list.contains(final_position))
//	     {
//    		 row1.setChecked(true);
//    		 kog.e("Jonathan", "Jonathan111");
//	     }
//    	 else
//    	 {
//    	     row1.setChecked(false);
//    	     kog.e("Jonathan", "Jonathan222");
//    	 }
//    	 
//    	 
//    	 row1.setOnCheckedChangeListener(new OnCheckedChangeListener()
//    	 {
//    	    @Override
//    	    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
//            {
//	            if(isChecked)
//	            {
//	            	kog.e("Jonathan", "Jonathan333 :: " + final_position);
//	            	//체크안되있을경우
//	                if (!Delivery_Point_Dialog.check_list.contains(final_position))
//	                {
//	                	kog.e("Jonathan", "Jonathan444 :: " + final_position);
//	                	Delivery_Point_Dialog.check_list.add(final_position);
//	                }
//	            }
//	            else
//	            {
//	            	for(int i = 0 ; i < Delivery_Point_Dialog.check_list.size() ; i++)
//	            	{
//	            		kog.e("Jonathan", "Jonathan77 :: " + Delivery_Point_Dialog.check_list.get(i).toString());
//	            	}
//	            	kog.e("Jonathan", "Jonathan555 :: " + final_position + " :: " + Delivery_Point_Dialog.check_list.indexOf(final_position));
//	            	
//	            	if(Delivery_Point_Dialog.check_list.indexOf(final_position) > 0)
//	            	{
//	            		Delivery_Point_Dialog.check_list.remove(Delivery_Point_Dialog.check_list.indexOf(final_position));
//	            	}
//	            }
//	            notifyDataSetChanged();
//            
//            }
//    	 });
    	 
    	 row2.setText(item.get("ZSEQ"));
    	 row3.setText(item.get("DELVR_ADDR"));
    	 row4.setText(item.get("DEFT_TYP"));
    	 row5.setText(item.get("ZIP_CODE"));
    	 row6.setText(item.get("CITY1"));
    	 row7.setText(item.get("STREET"));
    	 
    	 if (checkPosition == position) { ll.setBackgroundResource(R.drawable.table_list_s); }
    	 else                           { ll.setBackgroundResource(R.drawable.table_list_n); }

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
		kog.e("Jonathan", "setCheckPosition :: " + checkPosition);
		
//		 if(Delivery_Point_Dialog.check_list.contains(checkPosition))
//	     {
//    		 row1.setChecked(true);
//    		 kog.e("Jonathan", "Jonathan111");
//	     }
//    	 else
//    	 {
//    	     row1.setChecked(false);
//    	     kog.e("Jonathan", "Jonathan222");
//    	 }
//    	 
//    	 
//    	 row1.setOnCheckedChangeListener(new OnCheckedChangeListener()
//    	 {
//    	    @Override
//    	    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
//            {
//	            if(isChecked)
//	            {
//	            	kog.e("Jonathan", "Jonathan333 :: " + checkPosition);
//	            	//체크안되있을경우
//	                if (!Delivery_Point_Dialog.check_list.contains(checkPosition))
//	                {
//	                	kog.e("Jonathan", "Jonathan444 :: " + checkPosition);
//	                	Delivery_Point_Dialog.check_list.add(checkPosition);
//	                }
//	            }
//	            else
//	            {
//	            	for(int i = 0 ; i < Delivery_Point_Dialog.check_list.size() ; i++)
//	            	{
//	            		kog.e("Jonathan", "Jonathan77 :: " + Delivery_Point_Dialog.check_list.get(i).toString());
//	            	}
//	            	kog.e("Jonathan", "Jonathan555 :: " + checkPosition + " :: " + Delivery_Point_Dialog.check_list.indexOf(checkPosition));
//	            	
//            		Delivery_Point_Dialog.check_list.remove(Delivery_Point_Dialog.check_list.indexOf(checkPosition));
//	            }
//	            notifyDataSetChanged();
//            
//            }
//    	 });
    	 
    	 
		notifyDataSetChanged();
	}

	public int getCheckPosition() {
		return checkPosition;
	}

//	public void setCheckPosition(int checkPosition) {
//		this.checkPosition = checkPosition;
//	}

}
