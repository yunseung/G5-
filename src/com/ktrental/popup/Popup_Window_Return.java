package com.ktrental.popup;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.ktrental.R;
import com.ktrental.beans.RETURN;


public class Popup_Window_Return extends Popup_Window  {

	private OnDismissListener mDismissListener;
	private Context context;

	public interface OnDismissListener {
		public abstract void onDismiss(String result, int position);
	}

	public Popup_Window_Return(Context context) {
		super(context);
		this.context = context;

		initViewSettings(initRETURN());

	}
	
    String TABLE_NAME = "O_ITAB1";
    public ArrayList<RETURN> return_arr;
    private ArrayList<RETURN> initRETURN()
        {
        return_arr = new ArrayList<RETURN>();
        RETURN pm = new RETURN();
        pm.ZCODEV = "1";
        pm.ZCODEVT = "파손";
        return_arr.add(pm);
        pm = new RETURN();
        pm.ZCODEV = "2";
        pm.ZCODEVT = "부품품목 오류배송";
        return_arr.add(pm);
        pm = new RETURN();
        pm.ZCODEV = "3";
        pm.ZCODEVT = "직접입력";
        return_arr.add(pm);
       
        return return_arr;
        }

	public void setOnDismissListener(OnDismissListener listener) {
		setOnDismissListener(this);
		mDismissListener = listener;
	}

	private void initViewSettings(ArrayList<RETURN> list)
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
	    setHeight(153);
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
