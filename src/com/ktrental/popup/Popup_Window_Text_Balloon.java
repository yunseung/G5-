package com.ktrental.popup;

import java.util.ArrayList;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ktrental.R;
import com.ktrental.beans.PMO23;
import com.ktrental.common.DEFINE;


public class Popup_Window_Text_Balloon extends Popup_Window  {

	private OnDismissListener mDismissListener;
	private Context context;

	public interface OnDismissListener {
		public abstract void onDismiss(String result, int position);
	}

	public Popup_Window_Text_Balloon(Context context) {
		super(context);
		this.context = context;

//		initViewSettings(strText);

	}
	

	public void setOnDismissListener(OnDismissListener listener) {
		setOnDismissListener(this);
		mDismissListener = listener;
	}

	private void initViewSettings(String str)
	    {
	    LayoutInflater inflator = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        
        LinearLayout back = new LinearLayout(context);
        back.setOrientation(LinearLayout.VERTICAL);
        back.setBackgroundResource(R.drawable.popup_wbg);
	    
        View v = (View)inflator.inflate(R.layout.popup_balloon_row, null);
        TextView tv = (TextView)v.findViewById(R.id.popup_row_text_id);
//        Log.e("str", str);
        tv.setText(str);
        tv.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dismiss();
			}
		});
        back.addView(v);
	    mTrack.addView(back);
//	    setWidth(str.length() * 32);
	    }
	
   public ViewGroup getViewGroup()
        {
        return mTrack;
        }
   
   public void show(View anchor, String s) 
       {
	   	initViewSettings(s);
       super.show(anchor);
       }

	@Override
	public void onDismiss() 
	    {
		super.onDismiss();
	    }

}
