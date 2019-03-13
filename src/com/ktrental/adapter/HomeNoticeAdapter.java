package com.ktrental.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ktrental.R;
import com.ktrental.model.HomeNoticeModel;
/**
 * 홈화면에 공지사항들을 리스트에 보여주는 Adapter 이다. </br>
 * 
 * 
 * @author hongsungil
 */
public class HomeNoticeAdapter extends BaseCommonAdapter<HomeNoticeModel> {

	public HomeNoticeAdapter(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void bindView(View rootView, Context context, int position) {
		// TODO Auto-generated method stub
		HomeNoticeModel model = getItem(position);
		HomeNoticeViewHolder viewHolder = (HomeNoticeViewHolder) rootView
				.getTag();

		if (model != null) {
			viewHolder.tvTitle.setText(model.getTitle());
			viewHolder.tvDate.setText(model.getDate());
		}
	}

	@Override
	protected View newView(Context context, int position, ViewGroup viewgroup) {
		View rootView = mInflater.inflate(R.layout.home_notice_item, viewgroup,
				false);

		HomeNoticeViewHolder viewHolder = new HomeNoticeViewHolder();
		viewHolder.tvTitle = (TextView) rootView.findViewById(R.id.tv_title);
		viewHolder.tvDate = (TextView) rootView.findViewById(R.id.tv_date);

		rootView.setTag(viewHolder);

		return rootView;
	}

	class HomeNoticeViewHolder {
		TextView tvTitle;
		TextView tvDate;
	}

}
