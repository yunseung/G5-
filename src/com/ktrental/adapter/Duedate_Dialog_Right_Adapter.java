package com.ktrental.adapter;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.ktrental.R;

public class Duedate_Dialog_Right_Adapter extends
		ArrayAdapter<HashMap<String, String>> {

	private Context context;
	private ArrayList<HashMap<String, String>> items;
	private int layout_row;
	public static ArrayList<Integer> checked_items;
	public int new_count;

	public Duedate_Dialog_Right_Adapter(Context context,
			int textViewResourceId, ArrayList<HashMap<String, String>> items,
			int _new_count) {
		super(context, textViewResourceId, items);
		this.context = context;
		this.layout_row = textViewResourceId;
		this.items = items;
		this.new_count = _new_count;
	}

	@Override
	public View getView(int position, View v, ViewGroup parent) {
		if (v == null) {
			LayoutInflater vi = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = vi.inflate(layout_row, null);
		}

		final HashMap<String, String> item = items.get(position);

		TextView tv1 = (TextView) v
				.findViewById(R.id.duedate_dialog_right_row_id1);
		TextView tv2 = (TextView) v
				.findViewById(R.id.duedate_dialog_right_row_id2);
		TextView tv3 = (TextView) v
				.findViewById(R.id.duedate_dialog_right_row_id3);

		if (new_count < position) {
			tv1.setTextColor(Color.parseColor("#555555"));
			tv2.setTextColor(Color.parseColor("#555555"));
			tv3.setTextColor(Color.parseColor("#555555"));
		} else {
			tv1.setTextColor(Color.parseColor("#0079a0"));
			tv2.setTextColor(Color.parseColor("#0079a0"));
			tv3.setTextColor(Color.parseColor("#0079a0"));
		}

		tv1.setText(item.get("INVNR"));
		String kunnr_nm = item.get("KUNNR_NM");
		if (kunnr_nm == null)
			kunnr_nm = " ";
		kunnr_nm = kunnr_nm.equals(" ") ? "" : kunnr_nm.toString();
		String drivn = item.get("DRIVN");
		if (drivn == null)
			drivn = " ";

		drivn = drivn.equals(" ") ? "" : drivn.toString();
		String name = kunnr_nm.equals("") ? drivn : kunnr_nm;

//		Log.i("###", "###kunnr_nm" + kunnr_nm + "/");
//		Log.i("###", "###drivn" + drivn + "/");

		tv2.setText(name);

		String post_code = item.get("POST_CODE").equals(" ") ? "" : "["
				+ item.get("POST_CODE") + "] ";
		tv3.setText(post_code + item.get("CITY") + " " + item.get("STREET"));

		return v;
	}
}
