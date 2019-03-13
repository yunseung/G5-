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

public class Address_Dialog_Adapter extends ArrayAdapter<HashMap<String, String>>
	{
	private Context context;
	private LayoutInflater Inflater;
	private ArrayList<HashMap<String, String>> list;
	private int layout;
	private int choiced_num = Integer.MAX_VALUE;


    public Address_Dialog_Adapter(Context context, int layout, ArrayList<HashMap<String, String>> list) 
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

        LinearLayout ll = (LinearLayout)v.findViewById(R.id.ar_back_id);
		TextView num = (TextView)v.findViewById(R.id.ar_num_id);
		TextView jibun_doro = (TextView)v.findViewById(R.id.ar_jibun_doro_id);
		TextView gen_address = (TextView)v.findViewById(R.id.ar_gen_address_id);
   
		num.setText((position+1)+"");
		jibun_doro.setText(item.get("ADDRTP_TX"));
		gen_address.setText(item.get("FULL_ADDR"));
		
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
