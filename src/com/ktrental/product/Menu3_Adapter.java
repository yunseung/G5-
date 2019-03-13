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

public class Menu3_Adapter extends ArrayAdapter<HashMap<String, String>> {
	
    private Context context;
    private int layout;
	private int checkPosition = -1;
	private ArrayList<HashMap<String, String>> list;
	
	public Menu3_Adapter(Context context, int layout, ArrayList<HashMap<String, String>> list)
	    {
		super(context, layout, list);
		this.context = context;
		this.layout = layout;
		this.list = list;
//		Log.i("####","####리스트 사이즈"+list.size());
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
    	
    	 LinearLayout ll = (LinearLayout)v.findViewById(R.id.back_id);

    	 TextView row0 = (TextView) v.findViewById(R.id.row_id0);
    	 TextView row1 = (TextView) v.findViewById(R.id.row_id1);
    	 TextView row2 = (TextView) v.findViewById(R.id.row_id2);
    	 TextView row3 = (TextView) v.findViewById(R.id.row_id3);
    	 TextView row4 = (TextView) v.findViewById(R.id.row_id4);
    	 TextView row5 = (TextView) v.findViewById(R.id.row_id5);
    	 TextView row6 = (TextView) v.findViewById(R.id.row_id6);
    	 TextView row7 = (TextView) v.findViewById(R.id.row_id7);
         TextView row8 = (TextView) v.findViewById(R.id.row_id8);
         TextView row9 = (TextView) v.findViewById(R.id.row_id9);
         TextView row10 = (TextView) v.findViewById(R.id.row_id10);
         TextView row11 = (TextView) v.findViewById(R.id.row_id11);
         TextView row12 = (TextView) v.findViewById(R.id.row_id12);
         
         row0.setText(""+(position+1));
    	 row1.setText(item.get("LFART_TX")); //이동유형
    	 row2.setText(getDateFormat(item.get("WADAT"))); //이동요청일
    	 row3.setText(item.get("VBELN_VL")); //이동번호
    	 row4.setText(item.get("INVNR")); //차량번호
    	 row5.setText(item.get("PLACE1")); //출발지
    	 row6.setText(item.get("PLACE2")); //도착지
    	 row7.setText(item.get("STATUSNM")); //진행상태
    	 row8.setText(item.get("SERGE")); //차대번호
         row9.setText(item.get("MAKTX")); //차량명
         row10.setText(item.get("NAME1")); //고객명
         row11.setText(item.get("VBELN_VA")); //계약(매각)번호
         row12.setText(item.get("CMCTRNO")); //CM계약번호
    	 
    	 if (checkPosition == position) { ll.setBackgroundResource(R.drawable.table_list_s); }
    	 else                           { ll.setBackgroundResource(R.drawable.table_list_n); }

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
