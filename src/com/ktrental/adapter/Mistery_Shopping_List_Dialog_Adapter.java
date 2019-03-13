package com.ktrental.adapter;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.text.Html;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.UnderlineSpan;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ktrental.R;
import com.ktrental.common.DEFINE;

import java.util.ArrayList;
import java.util.HashMap;

public class Mistery_Shopping_List_Dialog_Adapter extends ArrayAdapter<HashMap<String, String>> {

    private Context context;
    private int layout;
	private int checkPosition = -1;
	private ArrayList<HashMap<String, String>> list;
	private HashMap<String, String> pm168List;
	private HashMap<String, String> pm170List;

	public Mistery_Shopping_List_Dialog_Adapter(Context context, int layout, ArrayList<HashMap<String, String>> _list)
	    {
		super(context, layout, _list);
		this.context = context;
		this.layout = layout;
		this.list = _list;
		getPm168List();
		getPm170List();
	}

	private HashMap<String, String> getPm168List()
	{
		pm168List = new HashMap<>();
		String path = context.getExternalCacheDir() + "/DATABASE/" + DEFINE.SQLLITE_DB_NAME;
		SQLiteDatabase sqlite = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.OPEN_READONLY);
		Cursor cursor = sqlite.rawQuery("select ZCODEV, ZCODEVT from " + "O_ITAB1" + " where (ZCODEH = 'PM168')", null);
		//myung 20131202 ADD 내역 > 진행상태 선택 > 조회 > 진행상태의 전체가 사라짐.
		while (cursor.moveToNext())
		{
			int calnum1 = cursor.getColumnIndex("ZCODEV");
			int calnum2 = cursor.getColumnIndex("ZCODEVT");
			String zcodev = cursor.getString(calnum1);
			String zcodevt = cursor.getString(calnum2);

			boolean over = false;
			pm168List.put(zcodev, zcodevt);
		}
		cursor.close();
		return pm168List;
	}

	private HashMap<String, String> getPm170List()
	{
		pm170List = new HashMap<>();
		String path = context.getExternalCacheDir() + "/DATABASE/" + DEFINE.SQLLITE_DB_NAME;
		SQLiteDatabase sqlite = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.OPEN_READONLY);
		Cursor cursor = sqlite.rawQuery("select ZCODEV, ZCODEVT from " + "O_ITAB1" + " where (ZCODEH = 'PM170')", null);
		//myung 20131202 ADD 내역 > 진행상태 선택 > 조회 > 진행상태의 전체가 사라짐.
		while (cursor.moveToNext())
		{
			int calnum1 = cursor.getColumnIndex("ZCODEV");
			int calnum2 = cursor.getColumnIndex("ZCODEVT");
			String zcodev = cursor.getString(calnum1);
			String zcodevt = cursor.getString(calnum2);

			boolean over = false;
			pm170List.put(zcodev, zcodevt);
		}
		cursor.close();
		return pm170List;
	}

	 public View getView(int position, View convertView, ViewGroup parent)
	     {
    	 View v = convertView;
    	
    	 if (v == null)
    	     {
    	     LayoutInflater vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    	     v = vi.inflate(layout, null);
    	     }

    	 LinearLayout ll = (LinearLayout)v.findViewById(R.id.row_back_id);

    	 TextView row1 = (TextView) v.findViewById(R.id.row_id1);
		 TextView row2_1 = (TextView) v.findViewById(R.id.row_id2_1);

    	 if(list == null || list.size() == 0){
    	 	return null;
		 }
		 HashMap<String, String> hashMap = list.get(position);
		 if(hashMap == null || hashMap.size() == 0){
		 	return null;
		 }

		 String minor = hashMap.get("MINOR");
		 String minor_string = "약속변경";
		 if(pm170List != null){
			 minor_string = pm170List.get(minor);
		 }
		 row1.setText(minor_string);

		 String message = hashMap.get("MESSAGE");
		 String psize = hashMap.get("PSIZE");
		 String underline = hashMap.get("ULINE");

		 SpannableStringBuilder buffer = null;
		 boolean useBuffer = false;
		 if(message != null && message.contains("<font")){
		 	useBuffer = true;
		 	message = message.replaceAll("\r\n", "<br/>");
		 }

		 float fsize = 10f;
		 if(psize != null) {
			 fsize = Float.valueOf(psize);
			 fsize = 2*fsize;
		 }

		if(message != null && message.length() > 0) {
			if(useBuffer){
				row2_1.setText(Html.fromHtml(message));
			} else {
				row2_1.setText(message);
				row2_1.setTextSize(TypedValue.COMPLEX_UNIT_SP, fsize);
				row2_1.setTextColor(Color.parseColor(hashMap.get("COLOR")));

				if(underline != null && underline.equals("X")){
					SpannableString content = new SpannableString(message);
					content.setSpan(new UnderlineSpan(), 0, message.length(), 0);
					row2_1.setText(content);
				}
			}
			row2_1.setVisibility(View.VISIBLE);
		} else {
			row2_1.setVisibility(View.GONE);
		}

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
