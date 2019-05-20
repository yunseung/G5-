package com.ktrental.calendar;

import java.util.ArrayList;

import android.content.Context;
import android.content.res.Resources;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ktrental.R;
import com.ktrental.adapter.BaseCommonAdapter;
import com.ktrental.common.DEFINE;
import com.ktrental.model.RepairDayInfoModel;
import com.ktrental.model.RepairPlanModel;
import com.ktrental.util.OnSelectedItem;
import com.ktrental.util.kog;

/**
 * BaseAdapter를 상속받아 구현한 CalendarAdapter
 * 
 * @author croute
 * @since 2011.03.08
 */
public class CalendarAdapter extends BaseCommonAdapter<RepairDayInfoModel> implements OnClickListener {

	private OnSelectedItem mOnSeletedItem;

	private int mSelectedPosition = -1;

	private DayInfoModel mTodayModel;
	private int mBackPosition = 0;

	private GridView gridview;

	private RepairPlanModel mTodayPlanModel;
	private boolean mSelectedFlag = true;

	public GridView getGridview() {
		return gridview;
	}

	public void setGridview(GridView gridview) {
		this.gridview = gridview;
	}

	/**
	 * Adpater 생성자
	 * 
	 * @param context
	 *            컨텍스트
	 * 
	 */
	public CalendarAdapter(Context context, boolean selectedFlag) {
		super(context);
		mSelectedFlag = selectedFlag;

	}

	private int getTodayPosition() {
		int rePos = 0;

		for (int i = 0; i < mItemArray.size(); i++) {

			RepairDayInfoModel repairDayInfoModel = mItemArray.get(i);

			DayInfoModel dayModel = repairDayInfoModel.getDayInfoModel();

			// 다음달이나 이전달에는 1일이 선택되게 한다.
			if (rePos == 0)
				if (dayModel.isInMonth())
					rePos = i;

			// 현재 달에 현재 날짜를 선택되게 한다.
			if (dayModel.isToDay()) {
				rePos = i;
				mTodayModel = dayModel;
				break;
			}
		}

		return rePos;
	}

	/**
	 * @param dayList
	 *            날짜정보들이 들어있는 리스트
	 */

	// private ArrayList<View> mRecycleViews = new ArrayList<View>();

	public void addAllDayInfoList(ArrayList<RepairDayInfoModel> dayList) {

		super.setData(dayList);

		// mSelectedPosition = getTodayPosition();
		mBackPosition = mSelectedPosition;
		notifyDataSetChanged();
	}

	public void setChangeDayInfoList(ArrayList<RepairDayInfoModel> dayList) {

		mItemArray.clear();

		super.setData(dayList);

		mSelectedPosition = -1;
		mBackPosition = mSelectedPosition;

		notifyDataSetChanged();
	}

	public class DayViewHolde {
		public LinearLayout llBackground;
		public TextView tvDay;
		public TextView tvHandle;
		public TextView tvPlan;

		int position = 0;

	}

	@Override
	protected void bindView(View rootView, Context context, int position) {

		RepairDayInfoModel model = mItemArray.get(position);

		DayInfoModel day = model.getDayInfoModel();
		day.setPosition(position);
		DayViewHolde dayViewHolder = (DayViewHolde) rootView.getTag();


		if (day != null) {
			dayViewHolder.tvDay.setText(String.valueOf(day.getDay()));
			int textColor = day.getDayColor(mContext);
			dayViewHolder.tvDay.setTextColor(textColor);
			// if (day.isToDay()) {
			// // Animation animation = AnimationUtils.loadAnimation(mContext,
			// // R.anim.twinkling_animation);
			// // dayViewHolder.tvDay.startAnimation(animation);
			// dayViewHolder.llBackground
			// .setBackgroundResource(R.drawable.cal_today);
			// } else {
			// dayViewHolder.tvDay.clearAnimation();
			// }

			dayViewHolder.position = position;

			if (mSelectedFlag) {
				if (mSelectedPosition == position)
					dayViewHolder.llBackground.setBackgroundResource(R.drawable.cal_s);
				else
					dayViewHolder.llBackground.setBackgroundDrawable(null);

			}
			RepairPlanModel planModel = model.getRepairPlanModel();

			if (day.isToDay()) {
				if (mSelectedPosition == position) {

					if (mSelectedFlag) {
						dayViewHolder.llBackground.setBackgroundResource(R.drawable.cal_todaysel);
					} else
						dayViewHolder.llBackground.setBackgroundResource(R.drawable.cal_today);
				} else {
					dayViewHolder.llBackground.setBackgroundResource(R.drawable.cal_today);
				}
				mTodayPlanModel = planModel;
			}

			if (planModel != null) {
				if (day.isInMonth()) {
					dayViewHolder.tvHandle.setText("" + planModel.getTotalComplateForDayInfo());
					dayViewHolder.tvPlan.setText("/" + planModel.getTotalPlanCountForDayInfo());
				} else {
					dayViewHolder.tvHandle.setText("");
					dayViewHolder.tvPlan.setText("");
				}
			} else {
				if (day.isInMonth()) {
					dayViewHolder.tvHandle.setText("" + 0);
					dayViewHolder.tvPlan.setText("/" + 0);
				} else {
					dayViewHolder.tvHandle.setText("");
					dayViewHolder.tvPlan.setText("");
				}
			}
		}
	}

	@Override
	protected View newView(Context context, int position, ViewGroup viewgroup) {

		View rootView = mInflater.inflate(R.layout.day, null);

		DayViewHolde dayViewHolder = new DayViewHolde();

		dayViewHolder.llBackground = (LinearLayout) rootView.findViewById(R.id.day_cell_ll_background);

		// int gridviewH = gridview.getHeight() / (calSize/7);
		// int
		// height=(int)mContext.getResources().getDimension(R.dimen.calendar_height);
		// int nWidth = 68;
		// int nHeight = 63;
		// if(DEFINE.DISPLAY.equals("2560")){
		// nWidth *= 2;
		// nHeight = nHeight * 2 + 9;
		// }
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams((int) Math.ceil(this.gridview.getWidth() / 7),
				this.gridview.getHeight() / 6);
		dayViewHolder.llBackground.setLayoutParams(params);

		dayViewHolder.tvDay = (TextView) rootView.findViewById(R.id.day_cell_tv_day);

		dayViewHolder.tvHandle = (TextView) rootView.findViewById(R.id.day_cell_handle);

		dayViewHolder.tvPlan = (TextView) rootView.findViewById(R.id.day_cell_tv_plan);

		dayViewHolder.llBackground.setOnClickListener(this);

		rootView.setTag(dayViewHolder);

		dayViewHolder.llBackground.setTag(dayViewHolder);

		return rootView;
	}

	private int getPixels(int dipValue) {
		Resources r = mContext.getResources();
		int px = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dipValue, r.getDisplayMetrics());
		return px;
	}

	@Override
	public void releaseResouces() {
		// TODO Auto-generated method stub
		mItemArray = null;
		super.releaseResouces();
	}

	public int getItemMonth(int position) {

		int reSelectedMonth = 0;
		RepairDayInfoModel repairDayInfoModel = mItemArray.get(position);
		DayInfoModel day = repairDayInfoModel.getDayInfoModel();

		reSelectedMonth = day.getInMonth();

		return reSelectedMonth;

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.day_cell_ll_background:
			clickRootView(v);
			break;

		default:
			break;
		}
	}

	private void clickRootView(View v) {

		DayViewHolde date_ViewHolder = (DayViewHolde) v.getTag();

		// KangHyunJin Add 20160309
		if (date_ViewHolder == null)
			return;

		mSelectedPosition = date_ViewHolder.position;

		if (mOnSeletedItem != null) {
			if (mSelectedFlag) {
				if (mBackPosition != mSelectedPosition) {

					selectedItem(date_ViewHolder);
				}
			} else {
				selectedItem(date_ViewHolder);
			}
		}

	}

	private void selectedItem(DayViewHolde date_ViewHolder) {
		RepairDayInfoModel repairDayInfoModel = (RepairDayInfoModel) getItem(date_ViewHolder.position);

		DayInfoModel model = repairDayInfoModel.getDayInfoModel();
		if (!model.isInMonth())
			return;
		mTodayModel = model;
		mOnSeletedItem.OnSeletedItem(model);
		mBackPosition = mSelectedPosition;
		notifyDataSetChanged();
	}

	public void setOnSelectedItem(OnSelectedItem aOnSelectedItem) {
		mOnSeletedItem = aOnSelectedItem;
	}

	public void initSelectedPosition() {
		mSelectedPosition = -1;
		mBackPosition = -1;
		notifyDataSetChanged();
	}

	public RepairPlanModel getTodayPlanModel() {
		return mTodayPlanModel;
	}

}