package com.ktrental.adapter;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ktrental.R;

public class Driver_Info_Dialog_Adapter extends ArrayAdapter<HashMap<String, String>>
	{
	private Context context;
	private LayoutInflater Inflater;
	private ArrayList<HashMap<String, String>> list;
	private int layout;
	private int choiced_num = Integer.MAX_VALUE;


    public Driver_Info_Dialog_Adapter(Context context, int layout, ArrayList<HashMap<String, String>> list) 
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
        
        HashMap<String, String> item = list.get(position);
        LinearLayout ll = (LinearLayout)v.findViewById(R.id.row_back_id);
        
		TextView row1 = (TextView)v.findViewById(R.id.row_id1);
		TextView row2 = (TextView)v.findViewById(R.id.row_id2);
		
		row1.setText(item.get("NAME1_CD"));
		row2.setText(item.get("TELF13"));

		if(position==choiced_num)
		    {
		    ll.setBackgroundResource(R.drawable.table_list_s);
		    }
		else
		    {
		    ll.setBackgroundResource(R.drawable.table_list_n);
		    }

		return v;
		}
	
   
    
    public int getChoiced_num()
        {
        return choiced_num;
        }

    
    public void setChoiced_num(int choiced_num)
        {
        this.choiced_num = choiced_num;
        notifyDataSetChanged();
        }

	}
