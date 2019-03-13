package com.ktrental.popup;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.ktrental.R;
import com.ktrental.beans.ZDOILTY;
import com.ktrental.common.DEFINE;

import java.util.ArrayList;


public class Popup_Window_ZDOILTY extends Popup_Window  {

	private OnDismissListener mDismissListener;
	private Context context;

	public interface OnDismissListener {
		public abstract void onDismiss(String result, int position);
	}

	public Popup_Window_ZDOILTY(Context context) {
		super(context);
		this.context = context;

		initViewSettings(initZDOILTY());

	}
	
    String TABLE_NAME = "O_ITAB1";
    public ArrayList<ZDOILTY> ZDOILTY_arr;
    private ArrayList<ZDOILTY> initZDOILTY()
        {
    	ZDOILTY_arr = new ArrayList<ZDOILTY>();
        String path = context.getExternalCacheDir() + "/DATABASE/" + DEFINE.SQLLITE_DB_NAME;
        SQLiteDatabase sqlite = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.OPEN_READONLY);
        Cursor cursor = sqlite.rawQuery("select ZCODEV, ZCODEVT from " + TABLE_NAME + " where ZCODEH = 'ZDOILTY'", null);
        ZDOILTY pm = new ZDOILTY();
        ZDOILTY_arr.add(pm);
        while (cursor.moveToNext())
            {
            int calnum1 = cursor.getColumnIndex("ZCODEV");
            int calnum2 = cursor.getColumnIndex("ZCODEVT");
            String zcodev = cursor.getString(calnum1);
            String zcodevt = cursor.getString(calnum2);
            
            pm = new ZDOILTY();
            pm.ZCODEV = zcodev;
            pm.ZCODEVT = zcodevt;
            ZDOILTY_arr.add(pm);
            }
        cursor.close();
//        sqlite.close();
        return ZDOILTY_arr;
        }

	public void setOnDismissListener(OnDismissListener listener) {
		setOnDismissListener(this);
		mDismissListener = listener;
	}

	private void initViewSettings(ArrayList<ZDOILTY> list)
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
//        mScroller.setPadding(14, 14, 14, 14);
	    setHeight(120);
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
