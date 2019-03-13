package com.ktrental.popup;

import java.util.ArrayList;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.ktrental.beans.PM013;


public class PM013_Popup extends QuickAction  {

	private OnDismissListener mDismissListener;
	private Context context;
	private ArrayList<PM013> list;

	public interface OnDismissListener {
		public abstract void onDismiss(String result, int position);
	}

	public PM013_Popup(Context context, int orientation, ArrayList<PM013> _list) {
		super(context, orientation);
		this.context = context;
		this.list = _list;
		initViewSettings();
	}

	public void setOnDismissListener(OnDismissListener listener) {
		setOnDismissListener(this);
		mDismissListener = listener;
	}

	private void initViewSettings()
	    {
	    for(int i=0;i<list.size();i++)
	        {
	        Button bt = new Button(context);
	        bt.setText(list.get(i).ZCODEVT);
	        bt.setTag(list.get(i).ZCODEV);
	        mTrack.addView(bt);
	        }
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
