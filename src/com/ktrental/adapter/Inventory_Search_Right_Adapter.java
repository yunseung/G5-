package com.ktrental.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ktrental.R;
import com.ktrental.beans.PARTS_SEARCH;
import com.ktrental.fragment.InventoryControlFragment;
import com.ktrental.popup.EventPopupC;
import com.ktrental.popup.InventoryPopup;
import com.ktrental.popup.QuantityPopup;
import com.ktrental.popup.InventoryPopup.OnDismissListener;


public class Inventory_Search_Right_Adapter extends ArrayAdapter<PARTS_SEARCH> {
    
    private Context context;
    private ArrayList<PARTS_SEARCH> items;
    private int layout_row;
    public static ArrayList<Integer> checked_items;
    private InventoryControlFragment icf;

    public Inventory_Search_Right_Adapter(Context context, int textViewResourceId, ArrayList<PARTS_SEARCH> items) 
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
        
        final PARTS_SEARCH item = items.get(position);
        ImageView iv = (ImageView)v.findViewById(R.id.inventory_search_row_id1);

//        Log.i("####", "##### 트루펄스"+item.CHECKED);
        iv.setImageResource(item.CHECKED?R.drawable.check_on:R.drawable.check_off);

        TextView tv1 = (TextView)v.findViewById(R.id.inventory_search_row_id2);
        TextView tv2 = (TextView)v.findViewById(R.id.inventory_search_row_id3);
        TextView tv3 = (TextView)v.findViewById(R.id.inventory_search_row_id4);
        TextView tv4 = (TextView)v.findViewById(R.id.inventory_search_row_id5);
        
        tv1.setText("자재그룹");//재고
//        tv2.setText(item.MAKTX);//자재명
        
        tv2.setText(item.MAKTX);//자재명
        tv3.setText(item.REQUEST);//신청
        tv4.setText(item.MEINS);//단위

        if(item.LABST.equals("0"))
            {
            int color = Color.parseColor("#fd2727");
            tv1.setTextColor(color);
            tv2.setTextColor(color);
            tv3.setTextColor(color);
            tv4.setTextColor(color);
            }
        else{
            int color = Color.parseColor("#333333");
            tv1.setTextColor(color);
            tv2.setTextColor(color);
            tv3.setTextColor(color);
            tv4.setTextColor(color);
            }
        
//        tv1.setOnClickListener(new OnClickListener()
//            {
//            @Override
//            public void onClick(View arg0)
//                {
//                onList(final_position);
//                }
//            });
//        tv2.setOnClickListener(new OnClickListener()
//            {
//            
//            @Override
//            public void onClick(View arg0)
//                {
//                onList(final_position);
//                }
//            });
        
        tv3.setOnClickListener(new OnClickListener()
            {
            @Override
            public void onClick(View v)
                {
                final TextView tv_request = (TextView) v.findViewById(R.id.inventory_search_row_id4);
//                Log.i("####","#### 아이템클릭");
                setInputButton(tv_request, final_position, item);
                }
            });
        
        return v;
    	}
    
    private void onList(int position)
        {
        PARTS_SEARCH ps = items.get(position);
        ps.CHECKED = !ps.CHECKED;
        notifyDataSetChanged();
        }
    
    InventoryPopup mPopupWindow;
    public void setInputButton(final TextView tv, final int position, final PARTS_SEARCH item)
        {
        mPopupWindow = new InventoryPopup(context, QuantityPopup.HORIZONTAL, R.layout.inventory_popup, QuantityPopup.TYPE_NOMARL_NUMBER);
        ViewGroup vg = mPopupWindow.getViewGroup();
        final TextView input = (TextView) vg .findViewById(R.id.inventory_bt_input);
        Button done = (Button) vg.findViewById(R.id.inventory_bt_done);
        done.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v)
                {

                Object data = input.getText();
                String num = data != null && !data.toString().equals("") ? data .toString() : "0";

                int int_num = Integer.parseInt(num);
                if (int_num <= 0)
                    {
                    EventPopupC epc = new EventPopupC(context);
                    epc.show("입력수량이 없습니다.");
                    return;
                    }
                else
                    {
                    item.REQUEST = num;
                    }
                mPopupWindow.setInput("CLEAR", true);
                mPopupWindow.dismiss();
                notifyDataSetChanged();
                }
            });
        mPopupWindow.show(tv, 0);
        }
}
