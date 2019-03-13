package com.ktrental.adapter;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.ktrental.R;
import com.ktrental.dialog.History_Dialog;
import com.ktrental.util.SharedPreferencesUtil;
import com.ktrental.util.kog;

public class History_Dialog_Left_Adapter extends BaseCommonAdapter<HashMap<String, String>> {
	
    private int layout;
	private int checkPosition = 0;
	private ArrayList<HashMap<String, String>> list;
	private String PERMGP = "";
//	private Textview bt_history_left;
	
	ListView mListview;
	
	onDoneClick mListener;
	
	public interface onDoneClick{
	    void onClick(View v, int position);
	}
	
	
	public History_Dialog_Left_Adapter(Context context, int layout, ArrayList<HashMap<String, String>> list, onDoneClick listener)
	    {
		super(context);
		mContext = context;
		this.layout = layout;
		mItemArray = list;
		this.list = list;
		mListener = listener;
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
    	
    	 LinearLayout ll = (LinearLayout)v.findViewById(R.id.history_back_id);

    	 TextView row1 = (TextView) v.findViewById(R.id.history_row_id1);
    	 TextView row2 = (TextView) v.findViewById(R.id.history_row_id2);
    	 TextView bt_history_left = (TextView)v.findViewById(R.id.bt_history_left);
    	 
    	 
    	 final int fPosition = position;
    	 
    	 bt_history_left.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				kog.e("Jonathan", "눌러지니??? " + fPosition);
//				History_Dialog.showRightLayout(fPosition);
				mListener.onClick(v,fPosition);
			}
		});
    	 
    	 

    	 row1.setText(getDateFormat(item.get("MTFDT").toString()));
    	 row2.setText(item.get("MNTYPNM")); //정비유형변환
    	 
    	 
    	 SharedPreferencesUtil sharedPreferencesUtil = SharedPreferencesUtil.getInstance();
    	 PERMGP = sharedPreferencesUtil.getLoginType();
    	 
    	 if("P1".equals(PERMGP))
    	 {
    		 bt_history_left.setVisibility(View.GONE);
    	 }
    	 else
    	 {
    		 if("사고".equals(item.get("MNTYPNM")) || "일반".equals(item.get("MNTYPNM"))  || "긴급".equals(item.get("MNTYPNM")) || "타이어".equals(item.get("MNTYPNM"))) 
        	 {
        		 bt_history_left.setVisibility(View.VISIBLE);
        	 }
        	 else
        	 {
        		 bt_history_left.setVisibility(View.GONE);
        	 } 
    	 }
    	 
    	 
    	 
    	 
    	 if (checkPosition == position) { ll.setBackgroundResource(R.drawable.left_list_customer_s); }
    	 else                           { ll.setBackgroundResource(R.drawable.left_list_customer_n); }

    	 return v;
    	 }
	 
    public String getDateFormat(String date)
        {
        StringBuffer sb = new StringBuffer(date);
        if(date.length()==8)
            {
            sb.insert(4, ".");
            int last = sb.length()-2;
            sb.insert(last, ".");
            }
        return sb.toString();
        }
    
    
    private OnClickListener mOnTitleClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
        	
        }
    };
    

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
