package com.ktrental.popup;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.ktrental.R;
import com.ktrental.beans.PM023;
import com.ktrental.util.kog;


public class PM023_Popup extends QuickAction2  {

	private OnDismissListener mDismissListener;
	private Context context;
	private ArrayList<PM023> list;

	public interface OnDismissListener {
		public abstract void onDismiss(String result, int position);
	}

	public PM023_Popup(Context context, int orientation, ArrayList<PM023> _list) {
		super(context, orientation);
		this.context = context;
		this.list = _list;

		kog.e("Jonathan", "PM023 LIST :: " + _list);
		
		initViewSettings();
	}

	public void setOnDismissListener(OnDismissListener listener) {
		setOnDismissListener(this);
		mDismissListener = listener;
	}

	private void initViewSettings()
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
//       setArrowGone();
       }

	@Override
	public void onDismiss() 
	    {
		super.onDismiss();
	    }

}
