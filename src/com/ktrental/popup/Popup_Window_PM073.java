package com.ktrental.popup;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.ktrental.R;
import com.ktrental.beans.PM073;


public class Popup_Window_PM073 extends Popup_Window  {

	private OnDismissListener mDismissListener;
	private Context context;

	public interface OnDismissListener {
		public abstract void onDismiss(String result, int position);
	}

	public Popup_Window_PM073(Context context) {
		super(context);
		this.context = context;

		initViewSettings(initPM073());

	}
	
    String TABLE_NAME = "O_ITAB1";
    public ArrayList<PM073> PM073_arr;
    private ArrayList<PM073> initPM073()
        {
        PM073_arr = new ArrayList<PM073>();
//        String path = context.getExternalCacheDir() + "/DATABASE/" + DEFINE.SQLLITE_DB_NAME;
//        SQLiteDatabase sqlite = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.OPEN_READONLY);
//        Cursor cursor = sqlite.rawQuery("select ZCODEV, ZCODEVT from " + TABLE_NAME + " where ZCODEH = 'PM073'", null);
//        PM073 pm;
//        while (cursor.moveToNext())
//            {
//            int calnum1 = cursor.getColumnIndex("ZCODEV");
//            int calnum2 = cursor.getColumnIndex("ZCODEVT");
//            String zcodev = cursor.getString(calnum1);
//            String zcodevt = cursor.getString(calnum2);
//            
//            pm = new PM073();
//            pm.ZCODEV = zcodev;
//            pm.ZCODEVT = zcodevt;
//            PM073_arr.add(pm);
//            }
//        cursor.close();
//        sqlite.close();
        PM073 pm = new PM073();
        pm.ZCODEV = "A";
        pm.ZCODEVT = "전체";
        PM073_arr.add(pm);
        pm = new PM073();
        pm.ZCODEV = "31";
        pm.ZCODEVT = "출고완료";
        PM073_arr.add(pm);
        pm = new PM073();
        pm.ZCODEV = "40";
        pm.ZCODEVT = "입고완료";
        PM073_arr.add(pm);
        
        return PM073_arr;
        }

//    private ArrayList<PM073> initPM073()
//        {
//        PM073_arr = new ArrayList<PM073>();
//        String path = context.getExternalCacheDir() + "/DATABASE/" + DEFINE.SQLLITE_DB_NAME;
//        SQLiteDatabase sqlite = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.OPEN_READONLY);
//        Cursor cursor = sqlite.rawQuery("select ZCODEV, ZCODEVT from " + TABLE_NAME + " where ZCODEH = 'PM073'", null);
//        PM073 pm;
//        while (cursor.moveToNext())
//            {
//            int calnum1 = cursor.getColumnIndex("ZCODEV");
//            int calnum2 = cursor.getColumnIndex("ZCODEVT");
//            String zcodev = cursor.getString(calnum1);
//            String zcodevt = cursor.getString(calnum2);
//            
//            pm = new PM073();
//            pm.ZCODEV = zcodev;
//            pm.ZCODEVT = zcodevt;
//            PM073_arr.add(pm);
//            }
//        cursor.close();
//        sqlite.close();
//        return PM073_arr;
//        }

	public void setOnDismissListener(OnDismissListener listener) {
		setOnDismissListener(this);
		mDismissListener = listener;
	}

	private void initViewSettings(ArrayList<PM073> list)
	    {
	    LayoutInflater inflator = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        
        LinearLayout back = new LinearLayout(context);
        back.setOrientation(LinearLayout.VERTICAL);
        back.setBackgroundResource(R.drawable.popup_wbg);
	    
	    for(int i=0;i<list.size();i++)
	        {
	        View v = (View)inflator.inflate(R.layout.popup_row, null);
	        Button bt = (Button)v.findViewById(R.id.popup_row_text_id);
            bt.setText(list.get(i).ZCODEVT);
            bt.setTag(list.get(i).ZCODEV);
            back.addView(v);
	        }
	    
	    mTrack.addView(back);
	    setHeight(152);
	    }
	
   public ViewGroup getViewGroup()
        {
        return mTrack;
        }
   
   public void show(View anchor) 
       {
       super.show(anchor);
       }

	@Override
	public void onDismiss() 
	    {
		super.onDismiss();
	    }

}
