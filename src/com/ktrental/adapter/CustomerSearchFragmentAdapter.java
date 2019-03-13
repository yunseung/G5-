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


public class CustomerSearchFragmentAdapter extends ArrayAdapter<HashMap<String, String>> {
    
    private Context context;
    private ArrayList<HashMap<String, String>> items;
    private int layout_row;
    public static ArrayList<Integer> checked_items;
    public static boolean EDITMODE = false;
    private int POSITION = 0;


    public CustomerSearchFragmentAdapter(Context context, int textViewResourceId, ArrayList<HashMap<String, String>> items) 
        {
        super(context, textViewResourceId, items);
        this.context = context;
        this.layout_row = textViewResourceId;
        this.items = items;
        
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
        HashMap<String, String> item = items.get(position);
        LinearLayout back = (LinearLayout)v.findViewById(R.id.row_back_id);

        if(POSITION==position)  { back.setBackgroundResource(R.drawable.left_list_customer_s); }
        else                    { back.setBackgroundResource(R.drawable.left_list_customer_n); }

        TextView tv1 = (TextView)v.findViewById(R.id.row_id1);
        TextView tv2 = (TextView)v.findViewById(R.id.row_id2);

        tv1.setText(item.get("INVNR"));//차량번호
        tv2.setText(item.get("NAME1"));//고객명

        return v;
    	}

    public int getPosition()
        {
        return POSITION;
        }

    public void setPosition(int position)
        {
        this.POSITION = position;
        notifyDataSetChanged();
        }
}
