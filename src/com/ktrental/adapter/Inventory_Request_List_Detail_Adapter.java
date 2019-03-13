package com.ktrental.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ktrental.R;
import com.ktrental.fragment.InventoryControlFragment;
import com.ktrental.fragment.InventoryControlFragment.REQUEST_LIST_DETAIL;


public class Inventory_Request_List_Detail_Adapter extends ArrayAdapter<REQUEST_LIST_DETAIL> {

	private Context context;
	private ArrayList<REQUEST_LIST_DETAIL> items;
	private int layout_row;
	public static ArrayList<Integer> checked_items;
	private InventoryControlFragment icf;
	public Inventory_Request_List_Detail_Adapter(Context context, int textViewResourceId, ArrayList<REQUEST_LIST_DETAIL> items, InventoryControlFragment icf) 
	{
		super(context, textViewResourceId, items);
		this.context = context;
		this.layout_row = textViewResourceId;
		this.items = items;
		this.icf = icf;
		checked_items = new ArrayList<Integer>();
	}

	@Override
	public View getView(int position, View v, ViewGroup parent) 
	{
		if(v == null)
		{
			LayoutInflater vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = vi.inflate(layout_row, null);
		}

		final int final_position = position;

		REQUEST_LIST_DETAIL item = items.get(position);

		ImageView iv = (ImageView)v.findViewById(R.id.inventory_request_list_detail_row_id1);

		TextView tv1 = (TextView)v.findViewById(R.id.inventory_request_list_detail_row_id2);
		TextView tv2 = (TextView)v.findViewById(R.id.inventory_request_list_detail_row_id3);
		TextView tv3 = (TextView)v.findViewById(R.id.inventory_request_list_detail_row_id4);
		TextView tv4 = (TextView)v.findViewById(R.id.inventory_request_list_detail_row_id5);
		TextView tv5 = (TextView)v.findViewById(R.id.inventory_request_list_detail_row_id6);
		ImageView del = (ImageView)v.findViewById(R.id.inventory_request_list_detail_row_id7);

		tv1.setText(item.MAKTX);//요청번호

		if(checked_items.contains(position))
		{
			iv.setImageResource(R.drawable.check_on);
			tv2.setBackgroundResource(R.drawable.input01);
			tv2.setPadding(12, 7, 12, 7);
		}
		else
		{
			iv.setImageResource(R.drawable.check_off);
			tv2.setBackgroundDrawable(null);
		}

		if(item.REQ_QTY_TEMP==null||!item.REQ_QTY_TEMP.equals(" "))
		{
			tv2.setText(item.REQ_QTY_TEMP);//신청수량
		}
		else
		{
			tv2.setText(item.REQ_QTY);//신청수량
		}
		tv3.setText(item.APV_QTY);//승인수량
		tv4.setText(item.ZSTATUST);//상태
		tv5.setText(item.CITY1+" "+item.STREET);//주소

		if(item.ZSTATUS.equals("10"))
		{
			del.setVisibility(View.VISIBLE);
		}
		else{
			del.setVisibility(View.GONE);
		}
		del.setOnClickListener(new OnClickListener() 
		{
			@Override
			public void onClick(View arg0)
			{
				//                Toast.makeText(context, items.get(final_position).MAKTX, 0).show();
				icf.goDetailDelete(final_position);
			}
		});

		return v;
	}
}
