package com.ktrental.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ktrental.R;
import com.ktrental.fragment.InventoryControlFragment;
import com.ktrental.fragment.InventoryControlFragment.REQUEST_LIST;


public class Inventory_Request_List_Adapter extends ArrayAdapter<REQUEST_LIST> {
    
    private Context context;
    private ArrayList<REQUEST_LIST> items;
    private int layout_row;
    public static ArrayList<Integer> checked_items;
    public static boolean EDITMODE = false;
    private int POSITION = 0;

    
    public int getPosition()
        {
        return POSITION;
        }

    public void setPosition(int position)
        {
        this.POSITION = position;
        notifyDataSetChanged();
        }

    public Inventory_Request_List_Adapter(Context context, int textViewResourceId, ArrayList<REQUEST_LIST> items) 
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
        
        REQUEST_LIST item = items.get(position);
        
        LinearLayout back = (LinearLayout)v.findViewById(R.id.inventory_request_list_back_id);

        if(POSITION==position)
            {
            back.setBackgroundResource(R.drawable.left_list_bg_s);
            }
        else{
            back.setBackgroundResource(R.drawable.left_list_bg_n);
            }
        
        ImageView iv = (ImageView)v.findViewById(R.id.inventory_request_list_row_id1);
//        iv.setChecked(checked_items.contains(position));
//        iv.setOnCheckedChangeListener(new OnCheckedChangeListener()
//            {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
//                {
//                if(isChecked)
//                    {
////                  삭제체크는 체크박스만 반응함
//                    String status = InventoryControlFragment.request_list_arr.get(final_position).ZSTATUS;
//                    if(status.equals("상태값을 넣어주세요")) { Toast.makeText(context, "체크할 수 없습니다." ,0).show(); return; }
//                    Inventory_Request_List_Adapter.checked_items.add(final_position);
//                    InventoryControlFragment.bt_request_delete.setEnabled(true);
//                    }
//                else
//                    {
////                체크박스 클릭위치로 옮겨주세요
//                    int location = Inventory_Request_List_Adapter.checked_items.indexOf(final_position);
//                    if(location!=-1)
//                        {
//                        Inventory_Request_List_Adapter.checked_items.remove(Inventory_Request_List_Adapter.checked_items.indexOf(final_position));
//                        }
//                    if(Inventory_Request_List_Adapter.checked_items.size()<=0) { InventoryControlFragment.bt_request_delete.setEnabled(false); };
//                    }
//                }
//            });
        
        if(checked_items.contains(position))
            {
            iv.setImageResource(R.drawable.check_on);
            InventoryControlFragment.bt_request_delete.setEnabled(true);
            }
        else
            {
            iv.setImageResource(R.drawable.check_off);
            if(Inventory_Request_List_Adapter.checked_items.size()<=0) { InventoryControlFragment.bt_request_delete.setEnabled(false); };
            }
        
        if(EDITMODE) iv.setVisibility(View.VISIBLE);
        else iv.setVisibility(View.INVISIBLE);

        TextView tv1 = (TextView)v.findViewById(R.id.inventory_request_list_row_id2);
        TextView tv2 = (TextView)v.findViewById(R.id.inventory_request_list_row_id3);

        tv1.setText(item.BANFN);//요청번호
        String date = item.BADAT;
        String year = date.substring(0, 4);
        String month = date.substring(4, 6);
        String day = date.substring(6);
        tv2.setText(year+"."+month+"."+day); //신청일자

        return v;
    	}
}
