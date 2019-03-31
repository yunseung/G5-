package com.ktrental.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ktrental.R;
import com.ktrental.calendar.DayInfoModel;
import com.ktrental.common.DEFINE;
import com.ktrental.model.RepairDayInfoModel;
import com.ktrental.model.RepairPlanModel;
import com.ktrental.util.OnSelectedItem;

public class Maintenance_Date_Adapter extends
		BaseCommonAdapter<RepairDayInfoModel> implements View.OnClickListener {

	private int mSelectedPosition = 0;
	private int mBackPosition = 0;

	private ListView mListView;

	private OnSelectedItem mOnSeletedItem;

	private RepairDayInfoModel mTodayModel;

	public Maintenance_Date_Adapter(Context context,
			ArrayList<RepairDayInfoModel> list, ListView listView,
			OnSelectedItem aOnSelectedItem) {
		super(context);
		setData(list);
		mSelectedPosition = getTodayPosition();
		mBackPosition = mSelectedPosition;
		mListView = listView;
		mOnSeletedItem = aOnSelectedItem;
	}

	private int getTodayPosition() {
		int rePos = 0;
		for (int i = 0; i < mItemArray.size(); i++) {
			RepairDayInfoModel repairDayInfoModel = mItemArray.get(i);
			if (repairDayInfoModel.getDayInfoModel().isToDay()) {
				rePos = i;
				mTodayModel = repairDayInfoModel;
				break;
			}
		}

		return rePos;
	}

	@Override
	protected void bindView(View rootView, Context context, int position) {

		Maintenance_Date_ViewHolder date_ViewHolder = (Maintenance_Date_ViewHolder) rootView
				.getTag();

		RepairDayInfoModel repairDayInfoModel = mItemArray.get(position);

		if (repairDayInfoModel != null) {

			repairDayInfoModel.getDayInfoModel().setPosition(position);
			String dayStr = repairDayInfoModel.getDayInfoModel().getDay();

			date_ViewHolder.tvDate.setText(dayStr);
			int textColor = repairDayInfoModel.getDayInfoModel().getDayColor(
					mContext);
			date_ViewHolder.tvDow.setTextColor(textColor);
			date_ViewHolder.tvDow
					.setText(repairDayInfoModel.getDayInfoModel()
							.getHeaderText(
									repairDayInfoModel.getDayInfoModel()
											.getDayOfWeek()));

			date_ViewHolder.position = position;

			if (mSelectedPosition == position)
				date_ViewHolder.rlRoot
						.setBackgroundResource(R.drawable.left_list_bg_s);
			else
				date_ViewHolder.rlRoot
						.setBackgroundResource(R.drawable.left_list_bg_n);

			RepairPlanModel repairPlanModel = repairDayInfoModel
					.getRepairPlanModel();

			if (repairPlanModel != null ) {
				date_ViewHolder.tvComplate.setText("" + repairPlanModel.getTotalComplateForDayInfo());
				date_ViewHolder.tvPlan.setText(" / " + repairPlanModel.getTotalPlanCountForDayInfo());
			} else {
				date_ViewHolder.tvComplate.setText("" + 0);
				date_ViewHolder.tvPlan.setText(" / " + 0);
			}

			// if (day.isToDay()) {
			// // Animation animation = AnimationUtils.loadAnimation(mContext,
			// // R.anim.twinkling_animation);
			// // dayViewHolder.tvDay.startAnimation(animation);
			// dayViewHolder.llBackground
			// .setBackgroundResource(R.drawable.cal_today);
			// } else {
			// dayViewHolder.tvDay.clearAnimation();
			// }
		}

	}

	@Override
	protected View newView(Context context, int position, ViewGroup viewgroup) {

		Maintenance_Date_ViewHolder date_ViewHolder = new Maintenance_Date_ViewHolder();
		View rootView = mInflater.inflate(R.layout.maintenance_date_row, null);

		date_ViewHolder.rlRoot = (RelativeLayout) rootView
				.findViewById(R.id.rl_root);

		//myung 2013 2560 리사이징
//		int tempX = 258;
//		int tempY = 72;
//		if(DEFINE.DISPLAY.equals("2560")){
//			tempX *= 2;
//			tempY *= 2;
//		}
//			
//		date_ViewHolder.rlRoot.setLayoutParams(new AbsListView.LayoutParams(
//				tempX, tempY));

		date_ViewHolder.tvDate = (TextView) rootView.findViewById(R.id.tv_day);
		date_ViewHolder.tvDow = (TextView) rootView
				.findViewById(R.id.tv_dayofweek);

		date_ViewHolder.tvComplate = (TextView) rootView
				.findViewById(R.id.tv_complate);

		date_ViewHolder.tvPlan = (TextView) rootView.findViewById(R.id.tv_plan);

		date_ViewHolder.rlRoot.setOnClickListener(this);

		rootView.setTag(date_ViewHolder);

		return rootView;
	}

	@Override
	public void releaseResouces() {
		// TODO Auto-generated method stub
		super.releaseResouces();
	}

	private class Maintenance_Date_ViewHolder {
		RelativeLayout rlRoot;
		TextView tvDate;
		TextView tvDow;
		TextView tvComplate;
		TextView tvPlan;

		int position = 0;
	}

	public void checkItem(int position) {
		mSelectedPosition = position;

		int last = mListView.getLastVisiblePosition();

		int first = mListView.getFirstVisiblePosition();

		if (position >= last || first <= position) {
			mListView.smoothScrollToPosition(position);
		}

		notifyDataSetChanged();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.rl_root:
			clickRootView(v);
			break;

		default:
			break;
		}
	}

	private void clickRootView(View v) {

		Maintenance_Date_ViewHolder date_ViewHolder = (Maintenance_Date_ViewHolder) v
				.getTag();
		checkItem(date_ViewHolder.position);

		if (mOnSeletedItem != null) {
			if (mBackPosition != mSelectedPosition) {
				RepairDayInfoModel model = (RepairDayInfoModel) getItem(date_ViewHolder.position);
				mTodayModel = model;
				mOnSeletedItem.OnSeletedItem(model.getDayInfoModel());
			}
		}

		mBackPosition = mSelectedPosition;
	}

	public void initScrollPosition(final boolean initFlag) {

		int last = mListView.getLastVisiblePosition();

		int first = mListView.getFirstVisiblePosition();
		if (mSelectedPosition >= last || first >= mSelectedPosition) {
			mListView.postDelayed(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					if (initFlag)
						mListView.smoothScrollToPosition(mSelectedPosition);
					else
						mListView.setSelection(mSelectedPosition);
				}
			}, 20);

			notifyDataSetChanged();
		}

	}

	public DayInfoModel getTodayModel() {
		return mTodayModel.getDayInfoModel();
	}

	public void setDateList(ArrayList<RepairDayInfoModel> arr) {
		mItemArray.clear();
		setData(arr);
		notifyDataSetChanged();

	}

	public void setSelectedDay(String selectedDay) {
		int pos = 0;
		for (RepairDayInfoModel dayModel : mItemArray) {

			String day = dayModel.getDayInfoModel().getCurrentDay();

			if (day.equals(selectedDay)) {
				mSelectedPosition = pos;
				mTodayModel = dayModel;
				mBackPosition = mSelectedPosition;
				notifyDataSetChanged();
				return;
			}
			pos++;
		}
	}
}
