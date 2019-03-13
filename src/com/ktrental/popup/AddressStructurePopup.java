package com.ktrental.popup;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.ktrental.R;
import com.ktrental.beans.CM011;


public class AddressStructurePopup extends Popup_Window  {

	private OnDismissListener mDismissListener;
	private Context context;
	private ArrayList<CM011> list;
	
	private Button anchor;
	
	public boolean who;

	public interface OnDismissListener {
		public abstract void onDismiss(String result, int position);
	}

	public AddressStructurePopup(Context context, ArrayList<CM011> _list) {
		super(context);
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
	    LayoutInflater inflator = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        
        LinearLayout back = new LinearLayout(context);
        back.setOrientation(LinearLayout.VERTICAL);
        back.setBackgroundResource(R.drawable.popup_wbg);
        
        int one = 51;
        int height = 0;
        for(int i=0;i<list.size();i++)
            {
            View v = (View)inflator.inflate(R.layout.popup_row, null);
            Button bt = (Button)v.findViewById(R.id.popup_row_text_id);
            bt.setText(list.get(i).ZCODEVT);
            bt.setTag(list.get(i));
            back.addView(v);
            height += one;
            }

        mTrack.addView(back);
        setHeight(height);
	    }
	
   public ViewGroup getViewGroup()
        {
        return mTrack;
        }
   public Button getAnchor()
       {
       return anchor;
       }
   
   public void show(Button anchor) 
       {
       super.show(anchor);
       this.anchor = anchor;
       }

	@Override
	public void onDismiss() 
	    {
		super.onDismiss();
	    }

}
