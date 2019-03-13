package com.ktrental.adapter;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ktrental.R;

public class Customer_Search_Dialog_Adapter extends BaseCommonAdapter<HashMap<String, String>> {
	
    private int layout;
	private int checkPosition = -1;
	private ArrayList<HashMap<String, String>> list;
	
	public Customer_Search_Dialog_Adapter(Context context, int layout, ArrayList<HashMap<String, String>> list)
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

    	 TextView row1 = (TextView) v.findViewById(R.id.row_id1);
    	 TextView row2 = (TextView) v.findViewById(R.id.row_id2);
    	 TextView row3 = (TextView) v.findViewById(R.id.row_id3);
    	 TextView row4 = (TextView) v.findViewById(R.id.row_id4);
    	 TextView row5 = (TextView) v.findViewById(R.id.row_id5);
    	 TextView row6 = (TextView) v.findViewById(R.id.row_id6);

    	 row1.setText(item.get("KUNNR"));
    	 row2.setText(item.get("NAME1"));
    	 row3.setText(item.get("POST_CODE1"));
    	 row4.setText(item.get("POST_CODE1"));
    	 row5.setText(item.get("CITY1"));
    	 row6.setText(item.get("STREET"));
    	 
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


	public void setCheckPosition(int position) {
		checkPosition = position;
		notifyDataSetChanged();
	}

	public int getCheckPosition() {
		return checkPosition;
	}

}
