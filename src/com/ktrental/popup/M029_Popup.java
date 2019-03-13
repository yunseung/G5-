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
import com.ktrental.beans.M029;
import com.ktrental.common.DEFINE;

import java.util.ArrayList;


public class M029_Popup extends QuickAction  {

	private OnDismissListener mDismissListener;
	private Context context;

	public interface OnDismissListener {
		public abstract void onDismiss(String result, int position);
	}

	public M029_Popup(Context context, int orientation) {
		super(context, orientation);
		this.context = context;
		initViewSettings(initM029());
	}
	
    String TABLE_NAME = "O_ITAB1";
    public ArrayList<M029> m029_arr;
    private ArrayList<M029> initM029()
        {
        m029_arr = new ArrayList<M029>();
        String path = context.getExternalCacheDir() + "/DATABASE/" + DEFINE.SQLLITE_DB_NAME;
        SQLiteDatabase sqlite = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.OPEN_READONLY);
        Cursor cursor = sqlite.rawQuery("select ZCODEV, ZCODEVT from " + TABLE_NAME + " where ZCODEH = 'M029'", null);
        M029 m029 = new M029();
        m029.ZCODEV = "A";
        m029.ZCODEVT = "전체";
        m029_arr.add(m029);
        while (cursor.moveToNext())
            {
            int calnum1 = cursor.getColumnIndex("ZCODEV");
            int calnum2 = cursor.getColumnIndex("ZCODEVT");
            String zcodev = cursor.getString(calnum1);
            String zcodevt = cursor.getString(calnum2);
            
            m029 = new M029();
            m029.ZCODEV = zcodev;
            m029.ZCODEVT = zcodevt;
            m029_arr.add(m029);
            }
        cursor.close();
//        sqlite.close();
        return m029_arr;
        }

	public void setOnDismissListener(OnDismissListener listener) {
		setOnDismissListener(this);
		mDismissListener = listener;
	}

	private void initViewSettings(ArrayList<M029> list)
	    {
	    LayoutInflater inflator = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        
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
        mScroller.setPadding(14, 14, 14, 14);
	    setHeight(300);
	    }
	
   public ViewGroup getViewGroup()
        {
        return mTrack;
        }
   
   public void show(View anchor) 
       {
       super.show(anchor);
       setArrowGone();
       }

	@Override
	public void onDismiss() 
	    {
		super.onDismiss();
	    }

}
