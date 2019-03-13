package com.ktrental.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ktrental.R;
import com.ktrental.beans.PARTS_SEARCH;


public class Inventory_Search_Left_Adapter extends ArrayAdapter<PARTS_SEARCH> {
    
    private Context context;
    public static ArrayList<PARTS_SEARCH> items;
    private int layout_row;
    public static ArrayList<Integer> checked_items;

    public Inventory_Search_Left_Adapter(Context context, int textViewResourceId, ArrayList<PARTS_SEARCH> items) 
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
        
        final PARTS_SEARCH item = items.get(position);
        ImageView iv = (ImageView)v.findViewById(R.id.inventory_search_row_id1);

//        iv.setImageResource(checked_items.contains(position)?R.drawable.check_on:R.drawable.check_off);

        TextView tv1 = (TextView)v.findViewById(R.id.inventory_search_row_id2);
        TextView tv2 = (TextView)v.findViewById(R.id.inventory_search_row_id3);
        TextView tv3 = (TextView)v.findViewById(R.id.inventory_search_row_id4);
//        TextView tv4 = (TextView)v.findViewById(R.id.inventory_search_row_id5);
//        TextView tv5 = (TextView)v.findViewById(R.id.inventory_search_row_id6);
//        TextView tv6 = (TextView)v.findViewById(R.id.inventory_search_row_id7);

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
        
        tv1.setText(item.LABST);//재고
        tv2.setText(item.REQUEST);//신청
//        tv3.setText(item.MAKTX);//자재명
        
        tv3.setText(item.MAKTX);//자재명

        if(item.LABST.equals("0"))
            {
            int color = Color.parseColor("#fd2727");
            tv1.setTextColor(color);
            tv2.setTextColor(color);
            tv3.setTextColor(color);
//            tv4.setTextColor(color);
//            tv5.setTextColor(color);
//            tv6.setTextColor(color);
            }
        else{
            int color = Color.parseColor("#333333");
            tv1.setTextColor(color);
            tv2.setTextColor(color);
            tv3.setTextColor(color);
//            tv4.setTextColor(color);
//            tv5.setTextColor(color);
//            tv6.setTextColor(color);
            }
        
        return v;
        }
}
