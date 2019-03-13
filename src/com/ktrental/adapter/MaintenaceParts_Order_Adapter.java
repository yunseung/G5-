package com.ktrental.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ktrental.R;
import com.ktrental.fragment.MaintenancePartsFragment.ZMO_1030_RD04;
import com.ktrental.util.kog;

import java.util.ArrayList;


public class MaintenaceParts_Order_Adapter extends ArrayAdapter<ZMO_1030_RD04> {
    
    private Context context;
    private ArrayList<ZMO_1030_RD04> items;
    private int layout_row;
    public static ArrayList<Integer> checked_items;

    public MaintenaceParts_Order_Adapter(Context context, int textViewResourceId, ArrayList<ZMO_1030_RD04> items) 
        {
        super(context, textViewResourceId, items);
        this.context = context;
        this.layout_row = textViewResourceId;
        this.items = items;
        
        checked_items = new ArrayList<Integer>();
        for(int i=0;i<items.size();i++)
            {
            if(items.get(i).ZSTATUS.equals("31"))
                {
                checked_items.add(i);
                }
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
        
        ZMO_1030_RD04 item = items.get(position);
        ImageView iv = (ImageView)v.findViewById(R.id.inventory_maintenanceparts_order_row_id1);

        iv.setImageResource(checked_items.contains(position)?R.drawable.check_on:R.drawable.check_off);

        TextView tv1 = (TextView)v.findViewById(R.id.inventory_maintenanceparts_order_row_id2);
        TextView tv2 = (TextView)v.findViewById(R.id.inventory_maintenanceparts_order_row_id3);
        TextView tv3 = (TextView)v.findViewById(R.id.inventory_maintenanceparts_order_row_id4);
        TextView tv4 = (TextView)v.findViewById(R.id.inventory_maintenanceparts_order_row_id5);
        TextView tv5 = (TextView)v.findViewById(R.id.inventory_maintenanceparts_order_row_id6);
        TextView tv6 = (TextView)v.findViewById(R.id.inventory_maintenanceparts_order_row_id7);
        TextView tv7 = (TextView)v.findViewById(R.id.inventory_maintenanceparts_order_row_id8);
        TextView tv8 = (TextView)v.findViewById(R.id.inventory_maintenanceparts_order_row_id9);
        TextView tv9 = (TextView)v.findViewById(R.id.inventory_maintenanceparts_order_row_id10);
        TextView tv10 = (TextView)v.findViewById(R.id.inventory_maintenanceparts_order_row_id11);
        
        
        if(checked_items.contains(position))
            {
            iv.setImageResource(R.drawable.check_on);
            tv8.setBackgroundResource(R.drawable.input01);
            tv8.setPadding(12, 7, 12, 7);
            }
        else
            {
            iv.setImageResource(R.drawable.check_off);
            tv8.setBackgroundDrawable(null);
            }
        
        tv1.setText(item.ZSTATUST);//진행상태
        tv2.setText(item.WGBEZ);//자재그룹
        tv3.setText(item.MAKTX);//부품명
        tv4.setText(item.MEINS);//단위
        tv5.setText(getDateFormat(item.EXP_DATE));//출고일자
        tv6.setText(item.EXP_QTY);//출고수량
        String str = item.INC_DATE;
        tv7.setText(getDateFormat(item.INC_DATE)); //입고일자
        str = item.INC_QTY;
        tv8.setText(str.equals("00000000")?"0":str); //입고수량
        tv9.setText(item.UNINC_QTY);//반송수량
        if(item.UNINC_QTY.equals("0"))
            {
            tv10.setText("");
            }
        else{
            tv10.setText(item.RETEXT);//반송사유	Jonathan 14.06.19 반송사유가 나오지 않는다는 요청이 왔음. 펑션명. ZMO_1030_RD04
            }
        
        for(int i = 0 ; i < items.size() ; i++)
        {
//        	kog.e("Jonathan", "jonathan items.get(" + i + ")" + items.get(i).RETEXT);
            kog.e("Jonathan", " || MATNR === " + items.get(i).MATNR + " || 부품명 = " + items.get(i).MAKTX + " || 수량 = " + items.get(i).INC_QTY);
        }

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
}
