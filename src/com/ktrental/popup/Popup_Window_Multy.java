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
import android.widget.TextView;

import com.ktrental.R;
import com.ktrental.beans.MULTY;
import com.ktrental.common.DEFINE;

import java.util.ArrayList;

public class Popup_Window_Multy extends Popup_Window {

	private OnDismissListener mDismissListener;
	private Context context;
	private String WHAT;
	// private TextView tv_start_dfm;
	// View mV;

	public interface OnDismissListener {
		public abstract void onDismiss(String result, int position);
	}

	public Popup_Window_Multy(Context context) {
		super(context);
		this.context = context;
	}

	public ArrayList<MULTY> arraylist;

	private ArrayList<MULTY> init(String what) {
		arraylist = new ArrayList<MULTY>();
		String path = context.getExternalCacheDir() + "/DATABASE/" + DEFINE.SQLLITE_DB_NAME;
		SQLiteDatabase sqlite = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.OPEN_READONLY);
		Cursor cursor = sqlite.rawQuery("select ZCODEV, ZCODEVT from " + "O_ITAB1" + " where ZCODEH = '" + what + "'",
				null);
		MULTY multy = new MULTY();
		multy.ZCODEV = " ";
		multy.ZCODEVT = "전체";
		arraylist.add(multy);
		while (cursor.moveToNext()) {
			int calnum1 = cursor.getColumnIndex("ZCODEV");
			int calnum2 = cursor.getColumnIndex("ZCODEVT");
			String zcodev = cursor.getString(calnum1);
			String zcodevt = cursor.getString(calnum2);

			multy = new MULTY();
			multy.ZCODEV = zcodev;
			multy.ZCODEVT = zcodevt;
			arraylist.add(multy);
		}
		cursor.close();
//		sqlite.close();
		return arraylist;
	}

	private ArrayList<MULTY> init(String what, boolean bool) {
		arraylist = new ArrayList<MULTY>();
		String path = context.getExternalCacheDir() + "/DATABASE/" + DEFINE.SQLLITE_DB_NAME;
		SQLiteDatabase sqlite = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.OPEN_READONLY);
		Cursor cursor = sqlite.rawQuery("select ZCODEV, ZCODEVT from " + "O_ITAB1" + " where ZCODEH = '" + what + "'",
				null);
		MULTY multy;
		while (cursor.moveToNext()) {
			int calnum1 = cursor.getColumnIndex("ZCODEV");
			int calnum2 = cursor.getColumnIndex("ZCODEVT");
			String zcodev = cursor.getString(calnum1);
			String zcodevt = cursor.getString(calnum2);

			multy = new MULTY();
			multy.ZCODEV = zcodev;
			multy.ZCODEVT = zcodevt;
			arraylist.add(multy);
		}
		cursor.close();
//		sqlite.close();
		return arraylist;
	}

	private ArrayList<MULTY> init(String what, String what2) {
		arraylist = new ArrayList<MULTY>();
		String path = context.getExternalCacheDir() + "/DATABASE/" + DEFINE.SQLLITE_DB_NAME;
		SQLiteDatabase sqlite = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.OPEN_READONLY);
		Cursor cursor = sqlite.rawQuery("select ZCODEV, ZCODEVT from " + "O_ITAB1" + " where ZCODEH = '" + what
				+ "' and ZCODEH2 = '" + what2 + "'", null);
		MULTY multy;
		while (cursor.moveToNext()) {
			int calnum1 = cursor.getColumnIndex("ZCODEV");
			int calnum2 = cursor.getColumnIndex("ZCODEVT");
			String zcodev = cursor.getString(calnum1);
			String zcodevt = cursor.getString(calnum2);

			multy = new MULTY();
			multy.ZCODEV = zcodev;
			multy.ZCODEVT = zcodevt;
			arraylist.add(multy);
		}
		cursor.close();
//		sqlite.close();
		return arraylist;
	}

	public void setOnDismissListener(OnDismissListener listener) {
		setOnDismissListener(this);
		mDismissListener = listener;
	}

	private void initViewSettings(ArrayList<MULTY> list, final Button anchor) {
		LayoutInflater inflator = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		LinearLayout back = new LinearLayout(context);
		back.setOrientation(LinearLayout.VERTICAL);
		back.setBackgroundResource(R.drawable.popup_wbg);

		// mV = (View)inflator.inflate(R.layout.menu3_resist_dialog, null);
		// tv_start_dfm = (TextView) mV.findViewById(R.id.menu2_start_input3);

		for (int i = 0; i < list.size(); i++) {
			View v = (View) inflator.inflate(R.layout.popup_row, null);
			final Button bt = (Button) v.findViewById(R.id.popup_row_text_id);
			bt.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {

					anchor.setText(bt.getText().toString());
					anchor.setTag(bt.getTag().toString());
					dismiss();
				}
			});
			bt.setText(list.get(i).ZCODEVT);
			bt.setTag(list.get(i).ZCODEV);
			back.addView(v);
		}
		mTrack.removeAllViews();
		mTrack.addView(back);
		
		int listHeight = 51;//context.getResources().getDimensionPixelSize(R.dimen.popup_list_height);
		int size = list.size() * listHeight;
		setHeight(size > (listHeight * 4) ? (listHeight * 4) : size);
	}

	// Jonaathan
	private void initViewSettings_Language(ArrayList<String> list, final Button anchor) {
		LayoutInflater inflator = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		LinearLayout back = new LinearLayout(context);
		back.setOrientation(LinearLayout.VERTICAL);
		back.setBackgroundResource(R.drawable.popup_wbg);

		// mV = (View)inflator.inflate(R.layout.menu3_resist_dialog, null);
		// tv_start_dfm = (TextView) mV.findViewById(R.id.menu2_start_input3);

		for (int i = 0; i < list.size(); i++) {
			View v = (View) inflator.inflate(R.layout.popup_row, null);
			final Button bt = (Button) v.findViewById(R.id.popup_row_text_id);
			bt.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {

					anchor.setText(bt.getText().toString());
					anchor.setTag(bt.getTag().toString());
					dismiss();
				}
			});
			bt.setText(list.get(i).toString());
			bt.setTag(list.get(i).toString());
			back.addView(v);
		}
		mTrack.removeAllViews();
		mTrack.addView(back);
		
		int listHeight = 51;//context.getResources().getDimensionPixelSize(R.dimen.popup_list_height);
		int size = list.size() * listHeight;
		setHeight(size > (listHeight * 4) ? (listHeight * 4) : size);
	}

	private void initViewSettings(ArrayList<MULTY> list, final TextView tv, final Button anchor) {
		LayoutInflater inflator = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		LinearLayout back = new LinearLayout(context);
		back.setOrientation(LinearLayout.VERTICAL);
		back.setBackgroundResource(R.drawable.popup_wbg);

		// mV = (View)inflator.inflate(R.layout.menu3_resist_dialog, null);
		// tv_start_dfm = (TextView) mV.findViewById(R.id.menu2_start_input3);

		for (int i = 0; i < list.size(); i++) {
			View v = (View) inflator.inflate(R.layout.popup_row, null);
			final Button bt = (Button) v.findViewById(R.id.popup_row_text_id);
			bt.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					if (!bt.getText().toString().equals(anchor.getText())) {
						TextView newText = tv;

						newText.setText("0");
					}
					anchor.setText(bt.getText().toString());
					anchor.setTag(bt.getTag().toString());
					dismiss();
				}
			});
			bt.setText(list.get(i).ZCODEVT);
			bt.setTag(list.get(i).ZCODEV);
			// Log.i("ZCODEVT", list.get(i).ZCODEVT);
			// Log.i("ZCODEV", list.get(i).ZCODEV);
			back.addView(v);
		}
		mTrack.removeAllViews();
		mTrack.addView(back);
		int listHeight = 51;//context.getResources().getDimensionPixelSize(R.dimen.popup_list_height);
		int size = list.size() * listHeight;
		setHeight(size > (listHeight * 4) ? (listHeight * 4) : size);
	}

	public ViewGroup getViewGroup() {
		return mTrack;
	}

	public void show(String what, String what2, Button anchor) {
		initViewSettings(init(what, what2), anchor);
		super.show(anchor);
	}

	public void show(String what, Button anchor) {
		initViewSettings(init(what), anchor);
		super.show(anchor);
	}

	public void show(String what, Button anchor, boolean bool) {
		initViewSettings(init(what, bool), anchor);
		super.show(anchor);
	}

	public void show(String what, Button anchor, TextView tv, boolean bool) {
		initViewSettings(init(what, bool), tv, anchor);
		super.show(anchor);
	}

	public void show(ArrayList<MULTY> list, Button anchor) {
		initViewSettings(list, anchor);
		super.show(anchor);
	}

	public void show_language(Button anchor) {

		String KOREAN = "국문"; // 01
		String ENGLISH = "영문"; // 13
		// String CHINESE= "중문"; //14
		// String ATTORNEY= "위임장"; //98

		// Jonathan 1500311 중문도 지워라.
		ArrayList<String> language = new ArrayList<String>();
		language.add(KOREAN);
		language.add(ENGLISH);
		// language.add(CHINESE);
		// language.add(ATTORNEY);

		initViewSettings_Language(language, anchor);
		super.show(anchor);
	}

	@Override
	public void onDismiss() {
		super.onDismiss();
	}

}
