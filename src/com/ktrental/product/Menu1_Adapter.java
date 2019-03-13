package com.ktrental.product;

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

public class Menu1_Adapter extends ArrayAdapter<HashMap<String, String>>
{

    private Context                            context;
    private int                                layout;
    private int                                checkPosition = -1;
    private ArrayList<HashMap<String, String>> list;

    public Menu1_Adapter(Context context, int layout, ArrayList<HashMap<String, String>> list)
    {
        super(context, layout, list);
        this.context = context;
        this.layout = layout;
        this.list = list;
    }

    public View getView(int position, View convertView, ViewGroup parent)
    {
        View v = convertView;

        if (v == null)
        {
            LayoutInflater vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(layout, null);
        }

        HashMap<String, String> item = list.get(position);

        LinearLayout ll = (LinearLayout) v.findViewById(R.id.back_id);

        TextView row0 = (TextView) v.findViewById(R.id.row_id0);
        TextView row1 = (TextView) v.findViewById(R.id.row_id1);
        TextView row2 = (TextView) v.findViewById(R.id.row_id2);
        TextView row3 = (TextView) v.findViewById(R.id.row_id3);
        TextView row4 = (TextView) v.findViewById(R.id.row_id4);
        // VOC 내역 추가
        // KangHyunJin ADD(20151208)
        TextView row5 = (TextView) v.findViewById(R.id.row_id5);
        TextView row6 = (TextView) v.findViewById(R.id.row_id6);
        
        row0.setText("" + (position + 1));
        row1.setText(item.get("AUFNR"));
        row2.setText(item.get("TXT"));
        row3.setText(item.get("INVNR"));
        row4.setText(item.get("SNDCUS"));
        // VOC 내역 추가
        // KangHyunJin ADD(20151208)
        row5.setText(item.get("VOCNUM"));
        
        if(item.get("VIPMA").equals("V"))
        {
        	row6.setText("롯데그룹VIP");
        }
        else if(item.get("VIPMA").equals("Y"))
        {
        	row6.setText("VIP");
        }
        else
        {
        	row6.setText("");
        }
        
        System.out.println("Menu1_Activity : " + item);
        if (checkPosition == position)
        {
            ll.setBackgroundResource(R.drawable.table_list_s);
        }
        else
        {
            ll.setBackgroundResource(R.drawable.table_list_n);
        }

        return v;
    }

    public void setCheckPosition(int position)
    {
        checkPosition = position;
        notifyDataSetChanged();
    }

    public int getCheckPosition()
    {
        return checkPosition;
    }

}
