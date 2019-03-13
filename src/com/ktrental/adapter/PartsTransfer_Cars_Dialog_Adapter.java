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

public class PartsTransfer_Cars_Dialog_Adapter extends BaseCommonAdapter<HashMap<String, String>> {
	
    private int layout;
	private int checkPosition = Integer.MIN_VALUE;
	private ArrayList<HashMap<String, String>> list;
	private String is_CarManager;
	private String P2 = "P2";
	
	public PartsTransfer_Cars_Dialog_Adapter(Context context, int layout, ArrayList<HashMap<String, String>> list, String _is_CarManager)
	    {
			super(context);
			mContext = context;
			this.layout = layout;
			mItemArray = list;
			this.list = list;
			is_CarManager = _is_CarManager;
	    }
	
	public PartsTransfer_Cars_Dialog_Adapter(Context context, int layout, ArrayList<HashMap<String, String>> list)
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
    	 ImageView row1 = (ImageView) v.findViewById(R.id.row_id1);
    	 row1.setVisibility(View.INVISIBLE);
    	 TextView row2 = (TextView) v.findViewById(R.id.row_id2);
    	 TextView row3 = (TextView) v.findViewById(R.id.row_id3);
    	 TextView row4 = (TextView) v.findViewById(R.id.row_id4);
    	 TextView row5 = (TextView) v.findViewById(R.id.row_id5);
    	 ImageView call = (ImageView) v.findViewById(R.id.row_call_id);

//    	 row1.setText(position + "");
    	 row2.setText(item.get("INVNR"));
    	 row3.setText(item.get("INNAM"));
    	 row4.setText(item.get("ENAME"));
    	 row5.setText(item.get("HPPPHON"));
    	 
    	if(P2.equals(is_CarManager))
    	{
    		row1.setVisibility(View.GONE);
    		row2.setVisibility(View.GONE);
    	}
    	     	 
    	 
    	 
    	 final String hp = item.get("HPPPHON");
    	 
    	 if (checkPosition == position) { ll.setBackgroundResource(R.drawable.table_list_s); }
    	 else                           { ll.setBackgroundResource(R.drawable.table_list_n); }
    	 
    	 
    	 call.setOnClickListener(new View.OnClickListener()
    	     {
             @Override
             public void onClick(View arg0)
                 {
                 try {
                     Uri uri = Uri.parse("tel:"+hp);
                     Intent in = new Intent(Intent.ACTION_CALL, uri);  
                     mContext.startActivity(in);
                     }
                 catch(Exception e)
                     {
                     EventPopupC epc = new EventPopupC(mContext);
                     epc.show("전화어플이 설치되어있지 않습니다.");
                     e.printStackTrace();
                     }  
                 }
    	     });

    	 return v;
    	 }
	 
	    public boolean aviliableCALL(Context context)
	        {
	        PackageManager pac = context.getPackageManager();
	        Uri callUri = Uri.parse("tel.");
	        Intent callIntent = new Intent(Intent.ACTION_CALL, callUri);
	        List list = pac.queryIntentActivities
	            (callIntent, PackageManager.COMPONENT_ENABLED_STATE_DEFAULT);
	        ArrayList tempList = new ArrayList();
	        int count = list.size();
	        String packageName = "";
	        for (int i = 0; i < count; i++)
	            {
	            ResolveInfo firstInfo = (ResolveInfo) list.get(i);
	            packageName = firstInfo.activityInfo.applicationInfo.packageName;
	            tempList.add(firstInfo.activityInfo);
//	            Log.d("packageName", "packageName = " + packageName);
	            }
	        if (packageName == null || packageName.equals(""))
	            {
	            return false;
	            }
	        else
	            {
	            return true;
	            }
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
