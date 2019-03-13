package com.ktrental.popup;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.ktrental.R;
import com.ktrental.fragment.InventoryControlFragment.ADDRESS;

public class InventoryAddressPopup extends QuickAction  {

	private OnDismissListener mDismissListener;
	private Context context;
	private ArrayList<ADDRESS> list;

	public interface OnDismissListener {
		public abstract void onDismiss(String result, int position);
	}

	public InventoryAddressPopup(Context context, int orientation, ArrayList<ADDRESS> _list) {
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
//	    for(int i=0;i<list.size();i++)
//	        {
//	        Button bt = new Button(context);
//	        bt.setText(list.get(i).TITLE);
//	        bt.setTag(list.get(i).ADDRESS);
//	        mTrack.addView(bt);
//	        }
	    
	    LayoutInflater inflator = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        
        LinearLayout back = new LinearLayout(context);
        back.setOrientation(LinearLayout.VERTICAL);
        back.setBackgroundResource(R.drawable.popup_wbg);
        for(int i=0;i<list.size();i++)
            {
            View v = (View)inflator.inflate(R.layout.popup_row, null);
            Button bt = (Button)v.findViewById(R.id.popup_row_text_id);
            bt.setText(list.get(i).TITLE);
            bt.setTag(list.get(i).ADDRESS);
            back.addView(v);
            }

        mTrack.addView(back);
        mScroller.setPadding(14, 14, 14, 14);
	    
	    setHeight(200);
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
