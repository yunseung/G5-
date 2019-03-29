package com.ktrental.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ktrental.R;
import com.ktrental.model.BaseMaintenanceModel;
import com.ktrental.model.MonthProgressModel;
import com.ktrental.util.kog;
import com.ktrental.viewholder.MonthProgressViewHolder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class MonthProgressAdapter extends BaseMaintenceAdapter {
	private String mProgressType = "전체";
	private String mInfoType = "carnum";
	private String mInfoText = "";

	public MonthProgressAdapter(Context context, OnClickRootView onClickRootView) {
		super(context, onClickRootView);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void bindView(View rootView, Context context, int position) {

		super.bindView(rootView, context, position);

		MonthProgressViewHolder viewHolder = (MonthProgressViewHolder) rootView
				.getTag();

		MonthProgressModel model = (MonthProgressModel) getItem(position);
		if (model != null) {
			if (model.getAUFNR().trim().isEmpty() && model.getATVYN().equals("A")) {
				viewHolder.tvDay.setText(insertDot(model.getDay()) + " " + (model.getAPM().equals("AM") ? "오전" : "오후"));
			} else {
				viewHolder.tvDay.setText(insertDot(model.getDay()) + " " + model.getTime());
			}

			// viewHolder.tvBooking.setText(RepairPlanModel
			// .getProgressStatus(model.getProgress_status()));
		}
	}

	@Override
	protected View newView(Context context, int position, ViewGroup viewgroup) {

		MonthProgressViewHolder viewHolder = new MonthProgressViewHolder();

		View rootView = newMaintenanceView(context, position, viewgroup,
				viewHolder);

		viewHolder.tvDay = (TextView) rootView.findViewById(R.id.tv_dayortime);
		// viewHolder.tvBooking = (TextView) rootView
		// .findViewById(R.id.tv_booking);

		rootView.setTag(viewHolder);

		return rootView;
	}

	@Override
	public void releaseResouces() {
		// TODO Auto-generated method stub
		mItemArray = null;
		recursiveRecycle(mRecycleViews);
	}

	@SuppressWarnings("null")
	private String insertDot(String aDay) {
		String reStr = aDay;
		if (aDay != null || aDay.length() == 8) {
			String year = aDay.substring(0, 4) + ".";
			String month = aDay.substring(4, 6) + ".";
			String day = aDay.substring(6, 8);

			reStr = year + month + day;
		}
		return reStr;
	}

	public void setProgressType(String aProgressType) {
		kog.e("KDH", "aProgressType = "+aProgressType);
		this.mProgressType = aProgressType;
		mFilteringFlag = true;
		// filtering();
	}

	public void setInfoType(String aProgressType) {
		this.mInfoType = aProgressType;
		mFilteringFlag = true;
		// filtering();
	}

	public void setInfoText(String aInfoText) {
		kog.e("KDH", "aInfoText = "+aInfoText);
		this.mInfoText = aInfoText;
		mFilteringFlag = true;
		// filtering();
	}

	public void setFilteringFlag(boolean bFlag) {
		mFilteringFlag = bFlag;
	}
	
	public void setMaintenanceType(String aMaintenanceType) {
		
		this.mMaintenanceType = aMaintenanceType;
		mFilteringFlag = true;
		filtering();
	}
	

	@Override
	public void filtering() {
		// TODO Auto-generated method stub
//		Log.i("mInfoType", mInfoType);
//		Log.i("mInfoText", mInfoText);
		
		//2014-02-24 뭐같지만 여기서 할수있는게없다.결국엔 내가 변경.
		kog.e("KDH", "mMaintenanceType = "+mMaintenanceType);
		kog.e("KDH", "mProgressType = "+mProgressType);
		
		
		if(!TextUtils.isEmpty(mMaintenanceType))
		{
			ArrayList<BaseMaintenanceModel> array = new ArrayList<BaseMaintenanceModel>();
			array.addAll(mFilterMaintenanceModelArray);
			mFilterMaintenanceModelArray.clear();
			Collections.sort(array, new NameAscCompare());
			mFilterMaintenanceModelArray.addAll(array);
		}
		else
		{
			int temp = 0;
			mFilterMaintenanceModelArray.clear();

			for (BaseMaintenanceModel model : mItemArray) {
				if (model instanceof BaseMaintenanceModel) {
//					Log.e("ProgressType", mProgressType+"/"+model.getProgress_status());
					// 진행 상태
					if (mProgressType.equals(model.getProgress_status())
							|| mProgressType.equals("전체")){
							 

						String infoText = "";
						if (mInfoType.equals("carnum"))
							infoText = model.getCarNum();
						else if (mInfoType.equals("name"))
							infoText = model.getCUSTOMER_NAME();
						else if(mInfoType.equals("addr"))
							infoText = model.getAddress();

						// myung 20131202 ADD 검색어 입력란에 데이터가 있을 시 검색어 옆에 X 버튼 추가 필요.
						// X 버튼 클릭 시 검색란 초기화 -> 전체 데이터 출력 필요.
						if (model.getCarNum().equals("")) {
							mFilterMaintenanceModelArray.add(model);
							continue;
						}
						// 진행 상태

						if (infoText.contains(mInfoText) || mInfoType.equals("")) {
							temp++;
							mFilterMaintenanceModelArray.add(model);
						}
					}
				}
			}

//			Log.i("filtering()", "리스트수 : " + String.valueOf(temp));

			// for (BaseMaintenanceModel model : mItemArray) {
			// if (model instanceof BaseMaintenanceModel) {
			// String infoText = "";
			// if (mInfoType.equals("carnum"))
			// infoText = model.getCarNum();
			// else if (mInfoType.equals("name"))
			// infoText = model.getName();
			//
			// // 진행 상태
			// if (mInfoText.equals(infoText) || mInfoText.equals("")) {
			//
			// mFilterMaintenanceModelArray.add(model);
			// }
			// }
			// }
		}
		

		notifyDataSetChanged();
	}

	// public void initFiltering() {
	// // mProgressType = "전체";
	// mInfoType = "";
	// filtering();
	// }

	private String mMaintenanceType = "";
	class NameAscCompare implements Comparator<BaseMaintenanceModel> {

		public NameAscCompare() {
			// TODO Auto-generated constructor stub
		}

		/**
		 * 오름차순(ASC)
		 */
		@Override
		public int compare(BaseMaintenanceModel arg0, BaseMaintenanceModel arg1) {
			// TODO Auto-generated method stub

			int compare = 0;

			if (mMaintenanceType.equals("name")) {
				compare = arg0.getCUSTOMER_NAME().compareTo(arg1.getCUSTOMER_NAME());
			} else if (mMaintenanceType.equals("addr")) {
				compare = arg0.getAddress().compareTo(arg1.getAddress());
			}

			return compare;
		}

	}
	
}
