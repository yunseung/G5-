package com.ktrental.fragment;

import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.ktrental.R;
import com.ktrental.adapter.PerformanceAdapter;
import com.ktrental.cm.connect.ConnectController;
import com.ktrental.cm.connect.Connector.ConnectInterface;
import com.ktrental.model.BaseMaintenanceModel;
import com.ktrental.model.PerformanceModel;
import com.ktrental.model.TableModel;
import com.ktrental.util.CommonUtil;
import com.ktrental.util.OnChangeFragmentListener;

import java.util.ArrayList;
import java.util.HashMap;

public class MaintenancePerpomanceFragment extends BaseRepairFragment implements OnClickListener {

	private ArrayList<PerformanceModel> mPerformanceModels = new ArrayList<PerformanceModel>();
	private TextView mTvPrevLeftTag;
	private TextView mTvPrevCenterTag;
	private TextView mTvPrevRightTag;
	private ImageView mIvPrevLeft;
	private ImageView mIvPrevCenter;
	private ImageView mIvPrevRight;
	private TextView mTvCurrentLeftTag;
	private TextView mTvCurrentCenterTag;
	private TextView mTvCurrentRightTag;
	private ImageView mIvCurrentLeft;
	private ImageView mIvCurrentCenter;
	private ImageView mIvCurrentRight;
	private TextView mTvNextLeftTag;
	private TextView mTvNextCenterTag;
	private TextView mTvNextRightTag;
	private ImageView mIvNextLeft;
	private ImageView mIvNextCenter;
	private ImageView mIvNextRight;
	// private HashMap<GraphModel, ImageView> mGraphArrMap = new
	// HashMap<GraphModel, ImageView>();
	private ArrayList<TextView> mTextArrayList = new ArrayList<TextView>();
	private static String mCurrentYYMM;
	private TextView mTvTitle;
	private TextView mTvLeftMonth;
	private TextView mTvCenterMonth;
	private TextView mTvRightMonth;
	private TextView mTvGraphTitle;
	private TextView mTvListTitle;
	private String mPrevYYMM;
	private String mPrev2YYMM;
	private ListView mLvPerpomance;
	private PerformanceAdapter mPerformanceAdapter;
	private boolean mFirstFlag = true;
	private static boolean mCreateFlag = false;

	// public static MaintenancePerpomanceFragment getInstrance(String
	// className, String currentYYMM)
	// {
	// return MaintenancePerpomanceFragment(className, currentYYMM, "");
	// }
	//
	// public static MaintenancePerpomanceFragment getInstrance(String
	// className, String currentYYMM, String _aa)
	// {
	// return new MaintenancePerpomanceFragment();
	// }
	public MaintenancePerpomanceFragment(){}

	public MaintenancePerpomanceFragment(String className, OnChangeFragmentListener changeFragmentListener,
			String currentYYMM) {
		mCurrentYYMM = currentYYMM;
	}

	public MaintenancePerpomanceFragment(String className, OnChangeFragmentListener changeFragmentListener,
			String currentYYMM, boolean flag) {
		mCurrentYYMM = currentYYMM;
		mCreateFlag = flag;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mRootView = inflater.inflate(R.layout.perpomance_layout, null);
		mPerformanceAdapter = new PerformanceAdapter(mContext);
		initSettingView();
		if (mCreateFlag) {
			initPerformance();
			mCreateFlag = false;
		}
		return mRootView;
	}

	@Override
	public void onHiddenChanged(boolean hidden) {
		if (!hidden) {
			if (mFirstFlag) {
				initPerformance();
				mFirstFlag = false;
			} else {
				if (mPerformanceModels.isEmpty()) {
					initPerformance();
				}
			}
		}
		super.onHiddenChanged(hidden);
	}

	private void initSettingView() {
		int year = Integer.parseInt(mCurrentYYMM.substring(0, 4));
		int month = Integer.parseInt(mCurrentYYMM.substring(4, 6));
		mPrevYYMM = getMonth(year, month, -1);
		mPrev2YYMM = getMonth(year, month, -2);
		mTvPrevLeftTag = (TextView) mRootView.findViewById(R.id.tv_prev_month_left_tag);
		mTextArrayList.add(mTvPrevLeftTag);
		mTvPrevCenterTag = (TextView) mRootView.findViewById(R.id.tv_prev_month_center_tag);
		mTextArrayList.add(mTvPrevCenterTag);
		mTvPrevRightTag = (TextView) mRootView.findViewById(R.id.tv_prev_month_right_tag);
		mTextArrayList.add(mTvPrevRightTag);
		mIvPrevLeft = (ImageView) mRootView.findViewById(R.id.iv_prev_month_left);
		mIvPrevCenter = (ImageView) mRootView.findViewById(R.id.iv_prev_month_center);
		mIvPrevRight = (ImageView) mRootView.findViewById(R.id.iv_prev_month_right);
		mTvCurrentLeftTag = (TextView) mRootView.findViewById(R.id.tv_current_month_left_tag);
		mTextArrayList.add(mTvCurrentLeftTag);
		mTvCurrentCenterTag = (TextView) mRootView.findViewById(R.id.tv_current_month_center_tag);
		mTextArrayList.add(mTvCurrentLeftTag);
		mTvCurrentRightTag = (TextView) mRootView.findViewById(R.id.tv_current_month_right_tag);
		mIvCurrentLeft = (ImageView) mRootView.findViewById(R.id.iv_current_month_left);
		mIvCurrentCenter = (ImageView) mRootView.findViewById(R.id.iv_current_month_center);
		mIvCurrentRight = (ImageView) mRootView.findViewById(R.id.iv_current_month_right);
		mTvNextLeftTag = (TextView) mRootView.findViewById(R.id.tv_next_month_left_tag);
		mTvNextCenterTag = (TextView) mRootView.findViewById(R.id.tv_next_month_center_tag);
		mTvNextRightTag = (TextView) mRootView.findViewById(R.id.tv_next_month_right_tag);
		mIvNextLeft = (ImageView) mRootView.findViewById(R.id.iv_next_month_left);
		mIvNextCenter = (ImageView) mRootView.findViewById(R.id.iv_next_month_center);
		mIvNextRight = (ImageView) mRootView.findViewById(R.id.iv_next_month_right);
		mTvTitle = (TextView) mRootView.findViewById(R.id.tv_common_title);
		mTvTitle.setText("정비현황조회");
		mTvLeftMonth = (TextView) mRootView.findViewById(R.id.tv_left_month);
		mTvLeftMonth.setText(CommonUtil.setDotDate(mPrev2YYMM));
		mTvLeftMonth.setOnClickListener(this);
		mTvCenterMonth = (TextView) mRootView.findViewById(R.id.tv_center_month);
		mTvCenterMonth.setOnClickListener(this);
		mTvCenterMonth.setText(CommonUtil.setDotDate(mPrevYYMM));
		mTvRightMonth = (TextView) mRootView.findViewById(R.id.tv_right_month);
		mTvRightMonth.setText(CommonUtil.setDotDate(mCurrentYYMM));
		mTvRightMonth.setOnClickListener(this);
		mTvGraphTitle = (TextView) mRootView.findViewById(R.id.tv_graph_title);
		mTvGraphTitle.setText(CommonUtil.setDotDate(mCurrentYYMM) + " 정비현황");
		mTvListTitle = (TextView) mRootView.findViewById(R.id.tv_list_title);
		mTvListTitle.setText(CommonUtil.setDotDate(mCurrentYYMM) + " 정비현황상세");
		mLvPerpomance = (ListView) mRootView.findViewById(R.id.lv_perpomance);
		mLvPerpomance.setAdapter(mPerformanceAdapter);
	}

	@Override
	public void onStart() {
		super.onStart();
	}

	private void initPerformance() {
		showProgress("실적을 조회 중 입니다.");
		ConnectController connectController = new ConnectController(new ConnectInterface() {

			@Override
			public void reDownloadDB(String newVersion) {
				// TODO Auto-generated method stub
			}

			@Override
			public void connectResponse(String FuntionName, String resultText, String MTYPE, int resulCode,
					TableModel tableModel) {
				// TODO Auto-generated method stub
				hideProgress();
				if (MTYPE.equals("S")) {
					ArrayList<HashMap<String, String>> array = tableModel.getTableArray();
					for (HashMap<String, String> map : array) {
						PerformanceModel model = new PerformanceModel(map.get("ZGUBUN"), map.get("ZCODE"),
								map.get("INGRP"), map.get("INNAM"), map.get("PROC_PCT"), map.get("PLAN_TOT"),
								map.get("PROC_CNT"), map.get("UNTRET_TOT"), map.get("BASYM"));

						// Log.i("####",
						// "#### 실적" + map.get("ZGUBUN") + "/"
						// + map.get("ZCODE") + "/"
						// + map.get("INGRP") + "/"
						// + map.get("INNAM") + "/"
						// + map.get("PROC_PCT") + "/"
						// + map.get("PLAN_TOT") + "/"
						// + map.get("PROC_CNT") + "/"
						// + map.get("UNTRET_TOT") + "/"
						// + map.get("BASYM"));
						mPerformanceModels.add(model);
					}
					settingGraph();

					mTvListTitle.setText(CommonUtil.setDotDate(mCurrentYYMM) + " 정비현황상세");
					getPerpomanceModels(mCurrentYYMM);
				} else {
					showEventPopup2(null, "" + resultText);
				}
			}
		}, mContext);
		connectController.getPerformance(mCurrentYYMM);
	}

	private void settingGraph() {
		int year = Integer.parseInt(mCurrentYYMM.substring(0, 4));
		int month = Integer.parseInt(mCurrentYYMM.substring(4, 6));
		String prevYYMM = getMonth(year, month, -1);
		String prev2YYMM = getMonth(year, month, -2);
		ArrayList<PerformanceModel> array = new ArrayList<PerformanceModel>();
		try {
			for (PerformanceModel model : mPerformanceModels) {
				if (model.getBASYM().equals(prev2YYMM)) {
					if (model.getZGUBUN().equals("책임정비사")) {
						drawGraph(mIvPrevLeft, Integer.parseInt(model.getPROC_PCT()));
						mTvPrevLeftTag.setText(model.getPROC_PCT() + "%");
					} else if (model.getZGUBUN().equals("소속MOT")) {
						drawGraph(mIvPrevCenter, Integer.parseInt(model.getPROC_PCT()));
						mTvPrevCenterTag.setText(model.getPROC_PCT() + "%");
					} else {
						drawGraph(mIvPrevRight, Integer.parseInt(model.getPROC_PCT()));
						mTvPrevRightTag.setText(model.getPROC_PCT() + "%");
					}
				} else if (model.getBASYM().equals(prevYYMM)) {
					if (model.getZGUBUN().equals("책임정비사")) {
						drawGraph(mIvCurrentLeft, Integer.parseInt(model.getPROC_PCT()));
						mTvCurrentLeftTag.setText(model.getPROC_PCT() + "%");
					} else if (model.getZGUBUN().equals("소속MOT")) {
						drawGraph(mIvCurrentCenter, Integer.parseInt(model.getPROC_PCT()));
						mTvCurrentCenterTag.setText(model.getPROC_PCT() + "%");
					} else {
						drawGraph(mIvCurrentRight, Integer.parseInt(model.getPROC_PCT()));
						mTvCurrentRightTag.setText(model.getPROC_PCT() + "%");
					}
				} else {
					if (model.getZGUBUN().equals("책임정비사")) {
						drawGraph(mIvNextLeft, Integer.parseInt(model.getPROC_PCT()));
						mTvNextLeftTag.setText(model.getPROC_PCT() + "%");
					} else if (model.getZGUBUN().equals("소속MOT")) {
						drawGraph(mIvNextCenter, Integer.parseInt(model.getPROC_PCT()));
						mTvNextCenterTag.setText(model.getPROC_PCT() + "%");
					} else {
						drawGraph(mIvNextRight, Integer.parseInt(model.getPROC_PCT()));
						mTvNextRightTag.setText(model.getPROC_PCT() + "%");
					}
					array.add(model);
				}
			}
			mPerformanceAdapter.setData(array);
		} catch (NumberFormatException e) {
		}
	}

	private void drawGraph(ImageView iv, int count) {
		Display display = getActivity().getWindowManager().getDefaultDisplay();

		Point size2 = new Point();
		display.getSize(size2);
		int width = size2.x;
		int height = size2.y;

		int size = 250 * count / 100;
		LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) iv
				.getLayoutParams();
		lp.width = 45;
		lp.height = size;
//     myung 20131206 정비현황조회 그래프 사이즈 2560 대응
		if(width == 2048){
			lp.width *= 2;
			lp.height *= 2;
		}
		iv.setLayoutParams(lp);
		if(mContext != null) {
			Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.graph_up_animation);
			iv.startAnimation(animation);
		}
	}

	private String getMonth(int year, int month, int value) {
		String yymm = "";
		int sum = (month + value);
		if (sum > 0) {
			if (sum < 13)
				yymm = "" + year + "" + CommonUtil.addZero(month + value);
			else
				yymm = "" + (year + 1) + "" + 01;
		} else {
			yymm = "" + (year - 1) + "" + (12);
		}
		return yymm;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.tv_left_month:
			mTvListTitle.setText(CommonUtil.setDotDate(mPrev2YYMM) + " 정비현황상세");
			getPerpomanceModels(mPrev2YYMM);
			break;
		case R.id.tv_center_month:
			mTvListTitle.setText(CommonUtil.setDotDate(mPrevYYMM) + " 정비현황상세");
			getPerpomanceModels(mPrevYYMM);
			break;
		case R.id.tv_right_month:
			mTvListTitle.setText(CommonUtil.setDotDate(mCurrentYYMM) + " 정비현황상세");
			getPerpomanceModels(mCurrentYYMM);
			break;
		default:
			break;
		}
	}

	private ArrayList<PerformanceModel> getPerpomanceModels(String yymm) {
		ArrayList<PerformanceModel> arrayList = new ArrayList<PerformanceModel>();
		for (PerformanceModel perpomanceModel : mPerformanceModels) {
			if (yymm.equals(perpomanceModel.getBASYM())) {
				arrayList.add(perpomanceModel);
			}
		}
		mPerformanceAdapter.setData(arrayList);
		return arrayList;
	}

	@Override
	protected void updateRepairPlan() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void initSelectedMaintenanceArray(String currentDay) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void queryMaintenace(String currentDay) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void initScroll() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void movePlan(ArrayList<BaseMaintenanceModel> models) {
		// TODO Auto-generated method stub

	}
}
