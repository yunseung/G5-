package com.ktrental.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ktrental.R;
import com.ktrental.model.BaseMaintenanceModel;


public class Duedate_Dialog_Left_Adapter extends ArrayAdapter<BaseMaintenanceModel> {
    
    private Context context;
    private ArrayList<BaseMaintenanceModel> items;
    private int layout_row;
    public static ArrayList<Integer> checked_items;

    public Duedate_Dialog_Left_Adapter(Context context, int textViewResourceId, ArrayList<BaseMaintenanceModel> items) 
        {
        super(context, textViewResourceId, items);
        this.context = context;
        this.layout_row = textViewResourceId;
        this.items = items;
        
        checked_items = new ArrayList<Integer>();
        for(int i=0;i<items.size();i++)
            {
            checked_items.add(i);
            }
        }
    
    @Override
    public View getView(int position, View v, ViewGroup parent) 
        {
        if(v == null)
            {
            LayoutInflater vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(layout_row, null);
        	    }
        
        final BaseMaintenanceModel item = items.get(position);
        ImageView iv = (ImageView)v.findViewById(R.id.duedate_dialog_left_row_id1);
        iv.setImageResource(checked_items.contains(position)?R.drawable.check_on:R.drawable.check_off);
        
        TextView tv1 = (TextView)v.findViewById(R.id.duedate_dialog_left_row_id2);
        TextView tv2 = (TextView)v.findViewById(R.id.duedate_dialog_left_row_id3);
        
        if(checked_items.contains(position))
            {
            iv.setImageResource(R.drawable.check_on);
            }
        else
            {
            iv.setImageResource(R.drawable.check_off);
            }
        
        tv1.setText(item.getCarNum());
        tv2.setText(item.getCUSTOMER_NAME());

        return v;
    	}
}
