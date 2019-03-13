package com.ktrental.adapter;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ktrental.R;

/**
 * </br> 메인 메뉴 리스트를 보여주는 Adapter 이다. </br>
 * 
 * @author hongsungil
 */
public class MenuAdapter extends BaseCommonAdapter<String> {

	public MenuAdapter(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void bindView(View rootView, Context context, int position) {
		// TODO Auto-generated method stub
		MenuHolder menuHolder = (MenuHolder) rootView.getTag();
		menuHolder.title.setText(mItemArray.get(position));
	}

	@Override
	protected View newView(Context context, int position, ViewGroup viewgroup) {

		View rootView = mInflater.inflate(R.layout.menu_item, null);

		MenuHolder menuHolder = new MenuHolder();
		menuHolder.title = (TextView) rootView.findViewById(R.id.tv_menu);

		rootView.setTag(menuHolder);

		return rootView;
	}

	@Override
	public void releaseResouces() {
		// TODO Auto-generated method stub
		super.releaseResouces();
	}

	@Override
	public void setData(List<String> datas) {
		super.setData(datas);
		notifyDataSetChanged();
	}

	private class MenuHolder {
		public TextView title;
	}

	public void chageAllData(List<String> datas) {
		mItemArray = datas;

		notifyDataSetChanged();
	}
}
