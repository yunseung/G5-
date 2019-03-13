package com.ktrental.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ktrental.R;
import com.ktrental.model.CorCardAccountModel;

import java.util.ArrayList;

public class Account_List_Dialog_Adapter extends ArrayAdapter<CorCardAccountModel> {

    private Context context;
    private int layout;
	private int checkPosition = -1;
	private ArrayList<CorCardAccountModel> list;

	public Account_List_Dialog_Adapter(Context context, int layout, ArrayList<CorCardAccountModel> _list)
	    {
		super(context, layout, _list);
		this.context = context;
		this.layout = layout;
		this.list = _list;
	    }

	 public View getView(int position, View convertView, ViewGroup parent)
	     {
    	 View v = convertView;
    	
    	 if (v == null)
    	     {
    	     LayoutInflater vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    	     v = vi.inflate(layout, null);
    	     }

			 CorCardAccountModel item = list.get(position);
    	
    	 LinearLayout ll = (LinearLayout)v.findViewById(R.id.row_back_id);

    	 TextView row1 = (TextView) v.findViewById(R.id.row_id1);
    	 TextView row2 = (TextView) v.findViewById(R.id.row_id2);

    	 row1.setText(item.getDOCTYPE());
    	 row2.setText(item.getDOCNAM());

    	 if (checkPosition == position) { ll.setBackgroundResource(R.drawable.table_list_s); }
    	 else                           { ll.setBackgroundResource(R.drawable.table_list_n); }

    	 return v;
    	 }

	public void setCheckPosition(int position) {
		checkPosition = position;
		notifyDataSetChanged();
	}

	public int getCheckPosition() {
		return checkPosition;
	}

}
