package com.ktrental.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.ktrental.R;
import com.ktrental.beans.ROUND;

public class Rounds_Dialog_Adapter extends ArrayAdapter<ROUND>
	{
	private Context context;
	private LayoutInflater Inflater;
	private ArrayList<ROUND> list;
	private int layout;
	
	public Rounds_Dialog_Adapter(Context context, int layout, ArrayList<ROUND> list) 
		{
		super(context, layout, list);
		this.context = context;
		this.layout = layout;
		this.list = list;
		}

	public View getView(int position, View convertView, ViewGroup parent) 
		{
        View v = convertView;
        
        if(v == null)
            {
            LayoutInflater vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(layout, null);
            }
        
        ROUND item = list.get(position);
        
//        if(Rounds_Dialog.CHOICED_NUM==position) 
//            {
//            //배경색 바꾸기
//            LinearLayout back = (LinearLayout)v.findViewById(R.id.rounds_row_back_id);
//            back.setBackgroundColor(Color.YELLOW);
//            notifyDataSetChanged();
//            }

		TextView invnr = (TextView)v.findViewById(R.id.rounds_invnr_id);
		TextView ingrp = (TextView)v.findViewById(R.id.rounds_ingrp_id);
		TextView ename = (TextView)v.findViewById(R.id.rounds_ename_id);
		TextView hppphon = (TextView)v.findViewById(R.id.rounds_hppphon_id);
   
		invnr.setText(item.INVNR);
		ingrp.setText(item.INGRP);
		ename.setText(item.ENAME);
		hppphon.setText(item.HPPPHON);

		return v;
		}
	}
