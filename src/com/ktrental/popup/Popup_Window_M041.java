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
import com.ktrental.beans.ZDOILTY;
import com.ktrental.common.DEFINE;

import java.util.ArrayList;

public abstract class Popup_Window_M041 extends Popup_Window
{

    private OnDismissListener mDismissListener;
    private Context           context;

    public interface OnDismissListener
    {
        public abstract void onDismiss(String result, int position);
    }
    
    // 값을 셋팅할때 초기값을 반환한다.
    public abstract void initM049Value(String code, String value);
    public abstract void selectM049Value(String code, String value);
    
    public Popup_Window_M041(Context context)
    {
        super(context);
        this.context = context;

        initViewSettings(initZDOILTY());
    }

    String                    TABLE_NAME = "O_ITAB1";
    public ArrayList<ZDOILTY> ZDOILTY_arr;

    private ArrayList<ZDOILTY> initZDOILTY()
    {
        ZDOILTY_arr = new ArrayList<ZDOILTY>();
        String path = context.getExternalCacheDir() + "/DATABASE/" + DEFINE.SQLLITE_DB_NAME;
        SQLiteDatabase sqlite = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.OPEN_READONLY);
        Cursor cursor = sqlite.rawQuery("select ZCODEV, ZCODEVT from " + TABLE_NAME + " where ZCODEH = 'M041' order by ZCODEV DESC" , null);
        
        while (cursor.moveToNext())
        {
            int calnum1 = cursor.getColumnIndex("ZCODEV");
            int calnum2 = cursor.getColumnIndex("ZCODEVT");
            String zcodev = cursor.getString(calnum1);
            String zcodevt = cursor.getString(calnum2);
            
            ZDOILTY pm = new ZDOILTY();
            pm.ZCODEV = zcodev;
            pm.ZCODEVT = zcodevt;
            ZDOILTY_arr.add(pm);
        }
        
        cursor.close();
//        sqlite.close();
        return ZDOILTY_arr;
    }

    public void setOnDismissListener(OnDismissListener listener)
    {
        super.setOnDismissListener(this);
        mDismissListener = listener;
    }

    private void initViewSettings(ArrayList<ZDOILTY> list)
    {
        LayoutInflater inflator = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        LinearLayout back = new LinearLayout(context);
        back.setOrientation(LinearLayout.VERTICAL);
        back.setBackgroundResource(R.drawable.popup_wbg);
        
        String code = "";
        String value = "";
        
        for (int i = 0; i < list.size(); i++)
        {
            View v = (View) inflator.inflate(R.layout.popup_row, null);
            Button bt = (Button) v.findViewById(R.id.popup_row_text_id);
            bt.setText(list.get(i).ZCODEVT);
            bt.setTag(list.get(i).ZCODEV);
            bt.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) 
				{
					selectM049Value(v.getTag().toString(), ((Button)v).getText().toString());
					dismiss();
				}
			});
            back.addView(v);
            
            if(i == 0)
            {
                code = list.get(i).ZCODEV;
                value = list.get(i).ZCODEVT;
            }
        }

        mTrack.addView(back);
        // mScroller.setPadding(14, 14, 14, 14);
        setHeight(155);
        
        initM049Value(code, value);
    }
    
    /**
     * 
     * <pre>
     * 선택 된 데이타 반환
     * </pre>
     * @since      2015. 12. 10.
     * @version    v1.0.0
     * @author     JuHH
     * @param position
     * @return
     * <pre></pre>
     */
    public ZDOILTY getSelectValue(int position) {
        
        if(ZDOILTY_arr == null || ZDOILTY_arr.size() == 0 || (ZDOILTY_arr.size() - 1) < position)
            return null;
        
        return ZDOILTY_arr.get(position);
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
