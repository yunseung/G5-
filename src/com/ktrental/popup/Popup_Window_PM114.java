package com.ktrental.popup;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.ktrental.R;
import com.ktrental.beans.PM114;
import com.ktrental.common.DEFINE;

import java.util.ArrayList;

public class Popup_Window_PM114 extends Popup_Window {

	private OnDismissListener mDismissListener;
	private Context context;

	public interface OnDismissListener {
		public abstract void onDismiss(String result, int position);
	}

	public Popup_Window_PM114(Context context, Button anchor) {
		super(context);
		this.context = context;

		initViewSettings(initPM114(), anchor);

	}

	String TABLE_NAME = "O_ITAB1";
	public ArrayList<PM114> PM114_arr;

	private ArrayList<PM114> initPM114() {
		PM114_arr = new ArrayList<PM114>();
		String path = context.getExternalCacheDir() + "/DATABASE/"
				+ DEFINE.SQLLITE_DB_NAME;
		SQLiteDatabase sqlite = SQLiteDatabase.openDatabase(path, null,
				SQLiteDatabase.OPEN_READONLY);
		Cursor cursor = sqlite.rawQuery("select ZCODEV, ZCODEVT from "
				+ TABLE_NAME + " where ZCODEH = 'PM114'", null);
		PM114 pm;
		while (cursor.moveToNext()) {
			int calnum1 = cursor.getColumnIndex("ZCODEV");
			int calnum2 = cursor.getColumnIndex("ZCODEVT");
			String zcodev = cursor.getString(calnum1);
			String zcodevt = cursor.getString(calnum2);

			pm = new PM114();
			pm.ZCODEV = zcodev;
			pm.ZCODEVT = zcodevt;
			PM114_arr.add(pm);
		}
		cursor.close();
//		sqlite.close();
		return PM114_arr;
	}

	public void setOnDismissListener(OnDismissListener listener) {
		setOnDismissListener(this);
		mDismissListener = listener;
	}

	public ArrayList<PM114> getPM114_Array(){
		return PM114_arr;
	}
	private void initViewSettings(ArrayList<PM114> list, final Button anchor) {
		LayoutInflater inflator = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		LinearLayout back = new LinearLayout(context);
		back.setOrientation(LinearLayout.VERTICAL);
		back.setBackgroundResource(R.drawable.popup_wbg);

		for (int i = 0; i < list.size(); i++) {
			View v = (View) inflator.inflate(R.layout.popup_row, null);
			final Button bt = (Button) v.findViewById(R.id.popup_row_text_id);
			bt.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					String tempAnchor = anchor.getText().toString();
					anchor.setText(bt.getText().toString());
					anchor.setTag(bt.getTag().toString());
					
//					if (!tempAnchor.equals(bt.getText().toString())) {
//						
//					}
					
					dismiss();
				}
			});

			bt.setText(list.get(i).ZCODEVT);
			bt.setTag(list.get(i).ZCODEV);
			back.addView(v);
		}

		mTrack.addView(back);
		setHeight(408);
	}

	public ViewGroup getViewGroup() {
		return mTrack;
	}

	public void show(View anchor) {
		super.show(anchor);
	}

	@Override
	public void onDismiss() {
		super.onDismiss();
	}

}
