package com.ktrental.popup;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ktrental.R;


public class Popup_Window_DelSearch extends Popup_Window  {

	private OnDismissListener mDismissListener;
	private Context context;

	public interface OnDismissListener
	{
		public abstract void onDismiss(String result, int position);
	}

	public Popup_Window_DelSearch(Context context)
	{
		super(context);
		this.context = context;

		initViewSettings();

	}

	public void setOnDismissListener(OnDismissListener listener)
	{
		setOnDismissListener(this);
		mDismissListener = listener;
	}

	private void initViewSettings()
	{
		LayoutInflater inflator = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View v = (View)inflator.inflate(R.layout.del_select, null);

		mTrack.addView(v);
		//setHeight(41);//context.getResources().getDimensionPixelSize(R.dimen.delete_search_height));
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
