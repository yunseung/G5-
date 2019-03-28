package com.ktrental.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.ktrental.R;
import com.ktrental.adapter.MaintenanceAdapter;
import com.ktrental.adapter.Maintenance_Date_Adapter;
import com.ktrental.calendar.CalendarController;
import com.ktrental.calendar.DayInfoModel;
import com.ktrental.cm.connect.ConnectController;
import com.ktrental.cm.db.DbAsyncTask;
import com.ktrental.cm.db.DbAsyncTask.DbAsyncResLintener;
import com.ktrental.common.DEFINE;
import com.ktrental.dialog.Duedate_Dialog;
import com.ktrental.dialog.Unimplementation_Reason_Dialog;
import com.ktrental.fragment.TransferManageFragment.OnDismissDialogFragment;
import com.ktrental.model.BaseMaintenanceModel;
import com.ktrental.model.DbQueryModel;
import com.ktrental.model.MaintenanceModel;
import com.ktrental.model.RepairDayInfoModel;
import com.ktrental.model.RepairPlanModel;
import com.ktrental.popup.BaseTextPopup;
import com.ktrental.popup.BaseTextPopup.OnSelectedPopupItem;
import com.ktrental.popup.Maintenance1_Popup;
import com.ktrental.product.VocInfoActivity;
import com.ktrental.util.CommonUtil;
import com.ktrental.util.LogUtil;
import com.ktrental.util.OnChangeFragmentListener;
import com.ktrental.util.OnEventOkListener;
import com.ktrental.util.kog;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;

/**
 * 일간정비현황 화면이다. <br>
 * 일일 단위 순회정비 대상차량을 관리하는 화면. <br>
 * <br>
 * BaseRepairFragment를 상속받음.<br>
 * <br>
 * 선택된 일정에 예정일변경,이관등록,미실시사유등록 기능등을 할 수 있다.<br>
 * 
 * <br>
 * MaintenanceAdapter에 선택된 날짜에 일정을 보여주며 필터링기능 및 전화,문자,위치,편집등을 제공.<br>
 * 
 * <br>
 * Maintenance_Date_Adapter 현재 달에 해당되는 날짜별로 처리와 계획을 보여주고 선택이 가능하다.<br>
 * 
 * <br>
 * REPAIR_TABLE 을 통해 일정을 받아 처리.<br>
 * 
 * @author hongsungil
 */
public class MaintenanceStatusFragment extends BaseRepairFragment
		implements OnClickListener, DbAsyncResLintener, OnDismissDialogFragment, OnSelectedPopupItem {

	private Context context;
	private CheckBox btn_filiter1;
	private CheckBox btn_filiter2;
	private CheckBox btn_filiter3;
	private ListView listview_date;

	// private MonthProgressFragment mMonthProgressFragment;
	private MaintenanceAdapter maintenanceAdapter;

	// private ArrayList<MaintenanceModel> maintenanceModels = new
	// ArrayList<MaintenanceModel>();
	private ListView mLvMaintenace;

	// private Button BtnResidence; // 소재지 변경 버튼
	private Button BtnDate; // 예정일변경 버튼
	private Button BtnTransfer; // 이관 관리 버튼
	private Button BtnNotImplemented; // 미실시사유등록 버튼
	private Button BtnVocInfo; // VOC 내역 조회
	// private Button BtnMaintenanceHistory;
	// private Button BtnMaintenanceResult;

	private TextView mTvHeaderTitle;
	private CalendarController mCalendarManager;
	private TextView mTvRightTitle;
	private ArrayList<RepairDayInfoModel> mDayList;

	private String[] maintenace_colums = { DEFINE.GSUZS, DEFINE.INVNR, DEFINE.KUNNR_NM, DEFINE.DRIVN, DEFINE.MAKTX,
			DEFINE.CCMRQ, DEFINE.POST_CODE, DEFINE.CITY, DEFINE.STREET, DEFINE.DRV_TEL, DEFINE.CCMSTS, DEFINE.GSTRS,
			DEFINE.AUFNR, DEFINE.EQUNR, DEFINE.CTRTY, DEFINE.DRV_MOB, DEFINE.CEMER, DEFINE.GUEEN2, DEFINE.TXT30,
			DEFINE.MDLCD, DEFINE.VOCNUM, DEFINE.KUNNR, DEFINE.DELAY, DEFINE.CYCMN_TX, DEFINE.APM, DEFINE.VBELN, DEFINE.GUBUN, DEFINE.REQNO,
			DEFINE.ATVYN};

	private String[] maintenace_plan_colums = { DEFINE.CCMSTS, DEFINE.GSTRS, DEFINE.CEMER, DEFINE.GUBUN };

	private HashMap<String, DbAsyncTask> mAsyncMap = new HashMap<String, DbAsyncTask>();

	private String mCurrentDay = "";

	private TextView mTvComplate, mTvComplate2, mTvComplate3;
	private TextView mTvPlan, mTvPlan2, mTvPlan3;

	private ArrayList<RepairPlanModel> mRepairPlanModelArray = new ArrayList<RepairPlanModel>();
	private int mComplateVal, mComplateVal2, mComplateVal3 = 0;
	private int mPlanVal, mPlanVal2, mPlanVal3 = 0;

	private Maintenance_Date_Adapter maintenance_Date_Adapter;

	private Cursor asyncCursor;

	private boolean firstFlag = true;
	private boolean updateFirstFlag = false;

	private LinkedHashMap<String, String> mProgressFilterMap = new LinkedHashMap<String, String>();
	private LinkedHashMap<String, String> mMaintenanceFilterMap = new LinkedHashMap<String, String>();
	private LinkedHashMap<String, String> mTermFilterMap = new LinkedHashMap<String, String>();

	private final static String PROGRESS_FILTER = "ProgressFilter";
	private final static String MAINTENANCE_FILTER = "MaintenanceFilter";
	private final static String TERM_FILTER = "TermFilter";

	// private View RootView;
	private OnChangeFragmentListener mOnChangeFragmentListener;

	private BaseTextPopup mProgressFilterPopup;
	private BaseTextPopup mMaintenanceFilterPopup;
	private BaseTextPopup mTermFilterPopup;

	private boolean mProgressPopFlag = false;
	private boolean mMaintenancePopFlag = false;
	private boolean mTermPopFlag = false;

	private TransferManageFragment mTransferManageFragment;

	private ImageView mEmptyView;
	private String mProgressType = "";

	public MaintenanceStatusFragment(){
	}

	public MaintenanceStatusFragment(String className, OnChangeFragmentListener changeFragmentListener) {
		super(className, changeFragmentListener);
		mOnChangeFragmentListener = changeFragmentListener;

	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		Log.e("MaintenanceStatus", "onCreate()++");
		initCalendar();

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		View root = inflater.inflate(R.layout.maintenance_layout, null);

		mRootView = root;

		btn_filiter1 = (CheckBox) root.findViewById(R.id.maintenance_filter1_id);
		btn_filiter1.setOnClickListener(this);

		btn_filiter1.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				// Log.d("", "onCheckedChanged = " + isChecked);
			}
		});
		btn_filiter1.setChecked(false);
		btn_filiter2 = (CheckBox) root.findViewById(R.id.maintenance_filter2_id);
		btn_filiter2.setOnClickListener(this);
		btn_filiter3 = (CheckBox) root.findViewById(R.id.maintenance_filter3_id);
		btn_filiter3.setOnClickListener(this);

		// mMonthProgressFragment = new MonthProgressFragment(
		// MonthProgressFragment.class.getName(), null);

		root.findViewById(R.id.inventory_detail_modify_id).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// BaseFragment 함수.
				changfragment(new MonthProgressFragment(MonthProgressFragment.class.getName(),
						mOnChangeFragmentListener, false));
			}
		});

		initFilter();

		listview_date = (ListView) root.findViewById(R.id.maintenance_date_list_id);
		listview_date.setDividerHeight(0);
		maintenance_Date_Adapter = new Maintenance_Date_Adapter(context, mDayList, listview_date, this);
		listview_date.setAdapter(maintenance_Date_Adapter);

		mLvMaintenace = (ListView) root.findViewById(R.id.lv_maintenance);
		// setDummyData();
		maintenanceAdapter = new MaintenanceAdapter(mContext, this);
		maintenanceAdapter.setDataArr(mBaseMaintenanceModels);

		mLvMaintenace.setAdapter(maintenanceAdapter);

		// BtnResidence = (Button) root.findViewById(R.id.btn_residence); // 소재지
		// 변경
		// BtnResidence.setOnClickListener(this); // 버튼

		BtnDate = (Button) root.findViewById(R.id.btn_date); // 예정일변경 버튼
		BtnDate.setOnClickListener(this);

		BtnTransfer = (Button) root.findViewById(R.id.btn_transfer); // 이관 관리 버튼
		BtnTransfer.setOnClickListener(this);

		BtnNotImplemented = (Button) root.findViewById(R.id.btn_notimplemented); // 미실시사유등록
		BtnNotImplemented.setOnClickListener(this);

		BtnVocInfo = (Button) root.findViewById(R.id.btn_daily_voc_info); // VOC
																			// 내역
																			// 조회
		BtnVocInfo.setOnClickListener(this);

		// BtnMaintenanceHistory = (Button) root
		// .findViewById(R.id.btn_maintenance_history);
		// BtnNotImplemented.setOnClickListener(this);

		// BtnMaintenanceResult = (Button) root
		// .findViewById(R.id.btn_maintenance_result);
		// BtnMaintenanceResult.setOnClickListener(this);

		mTvHeaderTitle = (TextView) root.findViewById(R.id.maintenance_filter1_1_id);

		mTvRightTitle = (TextView) root.findViewById(R.id.tv_righttitle);

		mTvComplate = (TextView) root.findViewById(R.id.tv_complate);
		mTvComplate2 = (TextView) root.findViewById(R.id.tv_complate2);
		mTvComplate3 = (TextView) root.findViewById(R.id.tv_complate3);
		mTvPlan = (TextView) root.findViewById(R.id.tv_plan);
		mTvPlan2 = (TextView) root.findViewById(R.id.tv_plan2);
		mTvPlan3 = (TextView) root.findViewById(R.id.tv_plan3);

		// queryMaintenacePlan();

		setMonthTitle();
		setDateTitle();

		mTransferManageFragment = new TransferManageFragment(MonthProgressFragment.class.getName(), null, this,
				mContext);

		mEmptyView = (ImageView) root.findViewById(R.id.iv_empty);

		return root;
	}

	private void initCalendar() {
		mDayList = new ArrayList<RepairDayInfoModel>();

		mCalendarManager = new CalendarController(CalendarController.TYPE_SHOW_DEFAULT);

		for (DayInfoModel dayInfoModel : mCalendarManager.getDayInfoArrayList()) {
			mDayList.add(new RepairDayInfoModel(dayInfoModel));
		}
	}

	@Override
	public void onAttach(Activity activity) {
		context = activity;
		super.onAttach(activity);
	}

	@Override
	public void onStart() {
		// 여기서 프레그먼트 작
		super.onStart();

	}

	@Override
	public void onResume() {
		super.onResume();
		maintenanceAdapter.initSelectedMaintenanceArray();
	};

	public void onHiddenChanged(boolean hidden) {
		if (!hidden) {

			if (firstFlag) {
				// initMaintenace();
				firstFlag = false;
				maintenance_Date_Adapter.initScrollPosition(false);
				// myung 20131210 ADD 정렬조건 -> 고객명 기본으로 선택
				onSelectedItem(0, MAINTENANCE_FILTER);
			} else {
				maintenance_Date_Adapter.initScrollPosition(true);
			}
			initScroll();

			maintenanceAdapter.initSelectedMaintenanceArray();

		} else {
			mProgressType = "전체";
			if (btn_filiter1 != null) {
				btn_filiter1.setText("전체");
			}
			mPlanVal = 0;
			mPlanVal2 = 0;
			mPlanVal3 = 0;

		}
		super.onHiddenChanged(hidden);
	};

	private void initFilter() {

		// String[] _whereArgs = { "PM002", String.valueOf(21),
		// String.valueOf(22) };
		// String[] _whereCause = { "ZCODEH", "ZCODEV", "ZCODEV" };
		//
		// String[] colums = { "ZCODEVT", "ZCODEV" };
		//
		// DbQueryModel dbQueryModel = new DbQueryModel(
		// ConnectController.O_ITAB1_TABLE_NAME, _whereCause, _whereArgs,
		// colums);
		//
		// dbQueryModel.setNotFlag(true);
		//
		// DbAsyncTask dbAsyncTask = new DbAsyncTask("maintenance_filter2_id",
		// mContext, this, dbQueryModel);
		// dbAsyncTask.execute(DbAsyncTask.DB_SELECT);

		String[] colums = { "ZCODEVT", "ZCODEV" };
		String[] _whereArgs2 = { "PM015" };
		String[] _whereCause2 = { "ZCODEH" };

		DbQueryModel dbQueryModel = new DbQueryModel(ConnectController.O_ITAB1_TABLE_NAME, _whereCause2, _whereArgs2,
				colums);

		DbAsyncTask dbAsyncTask = new DbAsyncTask("maintenance_filter1_id", mContext, this, dbQueryModel);
		dbAsyncTask.execute(DbAsyncTask.DB_SELECT);

		String[] _whereArgs3 = { "PM104" };
		String[] _whereCause3 = { "ZCODEH" };

		String[] colums3 = { "ZCODEVT", "ZCODEV" };

		dbQueryModel = new DbQueryModel(ConnectController.O_ITAB1_TABLE_NAME, _whereCause3, _whereArgs3, colums3);

		dbAsyncTask = new DbAsyncTask("maintenance_filter3_id", mContext, this, dbQueryModel);
		dbAsyncTask.execute(DbAsyncTask.DB_SELECT);

		// myung 20131210 DELETE 고객명 기본으로 선택. 전체는 삭제
		// mMaintenanceFilterMap.put("전체", "전체");
		mMaintenanceFilterMap.put("고객명", "고객명");
		mMaintenanceFilterMap.put("주소", "주소");

		mMaintenanceFilterPopup = new BaseTextPopup(mContext, mMaintenanceFilterMap, MAINTENANCE_FILTER);
	}

	public void onFilter2(View v) {

	}

	public void onFilter3(View v) {

	}

	Maintenance1_Popup filter1_pop;
	TextView filter1_tv[];
	int[] filter1_tv_id = { R.id.maintenance_filter1_1_id, R.id.maintenance_filter1_2_id, R.id.maintenance_filter1_3_id,
			R.id.maintenance_filter1_4_id, R.id.maintenance_filter1_5_id };

	@Override
	public void onClick(View arg0) {

		switch (arg0.getId()) {
		case R.id.maintenance_filter1_id:

			showFilterPopup(mProgressFilterPopup, btn_filiter1, mProgressPopFlag);
			break;
		case R.id.maintenance_filter2_id:
			showFilterPopup(mMaintenanceFilterPopup, btn_filiter2, btn_filiter2.isChecked());
			break;
		case R.id.maintenance_filter3_id:
			showFilterPopup(mTermFilterPopup, btn_filiter3, btn_filiter3.isChecked());
			break;

		case R.id.btn_date: // 예정일등록
			if (isNetwork())
				clickDate();
			break;
		case R.id.btn_transfer: // 이관등록
			if (isNetwork())
				clickTransfer();
			break;
		case R.id.btn_notimplemented: // 미실시 사유등록
			if (isNetwork())
				clickNotimplenented();

			break;
		// VOC 내역 조회
		// KangHyunJin 20151208
		case R.id.btn_daily_voc_info: {
			String KUNNR = getSelectKunnr();
			clickVocInfo(KUNNR);
		}
			break;

		}

	}

	private void clickNotimplenented() {

		if (isE0001()) {
			showEventPopup2(new OnEventOkListener() {

				@Override
				public void onOk() {
					// TODO Auto-generated method stub

				}
			}, "예약대기 상태일 경우 미실시 사유등록은 하실 수 없습니다.");
			return;
		}
		ArrayList<String> aufnr = getSelectedAUFNR();

		if (aufnr.size() <= 0) {
			showEventPopup2(new OnEventOkListener() {

				@Override
				public void onOk() {
					// TODO Auto-generated method stub

				}
			}, "미실시 사유등록은 정비대상 차량을 선택 후 가능합니다. 정비대상 차량을 선택해 주세요.");
			return;
		}
		Unimplementation_Reason_Dialog urd = new Unimplementation_Reason_Dialog(context, aufnr);
		urd.show();
		maintenanceAdapter.initSelectedMaintenanceArray();
	}

	private void clickDate() {
		ArrayList<BaseMaintenanceModel> arr = maintenanceAdapter.getSelectedMaintenanceModels();
		if (arr.size() <= 0) {

			showEventPopup2(null, "예정일 변경은 정비대상 차량을 선택후 가능합니다. 정비대상 차량을 선택해주세요.");
		} else {
			for (BaseMaintenanceModel baseMaintenanceModel : arr) {
				if (baseMaintenanceModel.getProgress_status().equals("E0004")
						|| baseMaintenanceModel.getProgress_status().equals("E0005")) {
					// showEventPopup2(null,
					// "완료된 정비차량이 선택 되었습니다. 정비 완료된 차량은 예정일을 변경 할 수 없습니다.");
					showEventPopup2(null, "예정일을 변경 할 수 없는 차량이 선택되었습니다. 다시 확인하여 주십시요.");
					return;
				}
			}
			movePlan(arr);
		}
	}

	private void clickVocInfo(String kunnr) {
		if (kunnr == null) {
			showEventPopup2(null, "VOC 내역조회는 고객 한명만 가능합니다.");
			return;
		}

		if (kunnr.equals("-1")) {
			showEventPopup2(null, "VOC 내역조회할 고객을 선택하세요.");
			return;
		}

		Intent intent = new Intent(getActivity(), VocInfoActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
		intent.putExtra(VocInfoActivity.VOC_KUNNR, kunnr);
		startActivity(intent);
	}

	private boolean isE0001() {

		boolean isE0001 = false;

		ArrayList<BaseMaintenanceModel> selArrayList = maintenanceAdapter.getSelectedMaintenanceModels();
		for (BaseMaintenanceModel baseMaintenanceModel : selArrayList) {
			if (baseMaintenanceModel.getProgress_status().equals("E0001")) {
				isE0001 = true;
			}
		}

		return isE0001;
	}

	private void showFilterPopup(BaseTextPopup popup, View anchor, boolean flag) {

		popup.setOnSelectedItem(this);
		popup.show(anchor);

	}

	private void clickTransfer() {
		String day = CommonUtil.getCurrentDay().substring(6);
		int today = Integer.parseInt(day);

		// myung 20131129 DELETE
		// if (today > 25) {
		// showEventPopup2(null, "26일 이후는 이관할 수 없습니다.");
		//
		// return;
		// }

		ArrayList<BaseMaintenanceModel> arr = maintenanceAdapter.getSelectedMaintenanceModels();

		// myung 20131227 ADD
		// int nCurrentDate = Integer.parseInt(arr.get(0).getDay());
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
		Calendar rightNow = Calendar.getInstance();
		int nCurrentDate = Integer.parseInt(formatter.format(rightNow.getTime()));

		ArrayList<String> arryOverGueen2 = new ArrayList<String>();

		for (int i = 0; i < arr.size(); i++) {
			int nGueen2 = Integer.parseInt(arr.get(i).getGUEEN2());
			if (nGueen2 < nCurrentDate) {
				arryOverGueen2.add(arr.get(i).getCarNum());
				// Log.e("nGueen2/nCurrentDate", ""+nGueen2+"/"+nCurrentDate);
			}

		}

		if (arr.size() <= 0) {
			showEventPopup2(new OnEventOkListener() {

				@Override
				public void onOk() {
					// TODO Auto-generated method stub

				}
			}, "이관등록은 이관대상 차량을 선택후 가능합니다. 이관대상 차량을 선택해주세요.");
		}
		// myung 20131227 ADD 계획 > 계약 완료 건 표기 및 계약 완료건 이관 시 이관 안되도록 메시지 처리.
		else if (arryOverGueen2.size() > 0) {
			// Log.e("arryOverGueen2.size()", ""+arryOverGueen2.size());
			String temp = "";
			for (int i = 0; i < arryOverGueen2.size(); i++) {
				if (i > 0)
					temp += "/ ";
				temp += arryOverGueen2.get(i) + " ";

			}

			showEventPopup2(new OnEventOkListener() {

				@Override
				public void onOk() {
					// TODO Auto-generated method stub

				}
			}, temp + "차량은 \n계약종료되어 이관등록이 불가합니다.");
		} else {
			if (mTransferManageFragment == null)
				mTransferManageFragment = new TransferManageFragment(MonthProgressFragment.class.getName(), null, this,
						mContext);

			mTransferManageFragment.setmMaintenanceModelArray(arr);
			//
			// dialogFragment.getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
			// myung 20131120 UPDATE 2560 대응
			// int tempX = 1060;
			// int tempY = 744;
			// if (DEFINE.DISPLAY.equals("2560"))
			// {
			// tempX *= 2;
			// tempY *= 2;
			// }
			mTransferManageFragment.show(getChildFragmentManager(), null);// ,
																			// tempX,
																			// tempY);
			maintenanceAdapter.initSelectedMaintenanceArray();
		}
	}

	@Override
	public void onDestroyView() {
		maintenance_Date_Adapter.releaseResouces();
		super.onDestroyView();
	}

	@Override
	public void onCompleteDB(String funName, int type, Cursor cursor, String tableName) {

		// Log.e("funName", funName);
		boolean MaintenaceAsyncFlag = mAsyncMap.containsKey(funName);

		if (MaintenaceAsyncFlag) {
			kog.e("Jonathan", "mainenanceStatusFragment :: funName" + funName);
			parsingMaintenance(cursor);

		} else {

			if (funName.equals("maintenance_filter1_id")) {
				mProgressFilterMap.put("전체", "전체");

				if (cursor == null)
					return;

				try {
					cursor.moveToFirst();

					while (!cursor.isAfterLast()) {
						mProgressFilterMap.put(cursor.getString(0), cursor.getString(1));
						cursor.moveToNext();

					}
					mProgressFilterPopup = new BaseTextPopup(mContext, mProgressFilterMap, PROGRESS_FILTER,
							R.layout.text_array_popup_shot_layout);

					cursor.close();
				} catch (Exception e){
					e.printStackTrace();
					cursor.close();
				}

			} else if (funName.equals("maintenance_filter2_id")) {
				// myung 20131210 DELETE 고객명 기본으로 선택. 전체는 삭제
				// mMaintenanceFilterMap.put("전체", "전체");

				if (cursor == null)
					return;

				cursor.moveToFirst();

				while (!cursor.isAfterLast()) {
					mMaintenanceFilterMap.put(cursor.getString(0), cursor.getString(1));
					cursor.moveToNext();

				}
				cursor.close();
				mMaintenanceFilterPopup = new BaseTextPopup(mContext, mMaintenanceFilterMap, MAINTENANCE_FILTER);

			} else if (funName.equals("maintenance_filter3_id")) {
				mTermFilterMap.put("전체", "전체");

				if (cursor == null)
					return;

				try {
					cursor.moveToFirst();

					while (!cursor.isAfterLast()) {
						mTermFilterMap.put(cursor.getString(0), cursor.getString(1));
						cursor.moveToNext();

					}
					cursor.close();
					mTermFilterPopup = new BaseTextPopup(mContext, mTermFilterMap, TERM_FILTER,
							R.layout.text_array_popup_shot_layout);
				} catch (Exception e){
					e.printStackTrace();
					cursor.close();
				}


			} else if (funName.equals("queryMaintenacePlan")) {
				if (cursor == null)
					return;

				cursor.moveToFirst();
				mRepairPlanModelArray.clear();
				mPlanVal = 0;

				RepairPlanModel repairPlanModel = new RepairPlanModel();

				int backWorkDay = -1;
				int backIndex = 0;
				mComplateVal = 0;
				mComplateVal2 = 0;
				mComplateVal3 = 0;

				// myung 20131206 ADD 전체 계획을 다른 날로 이관 시 계획이 0으로 안바뀜
				for (int i = 0; i < mDayList.size(); i++) {
					mDayList.get(i).setRepairPlanModel(new RepairPlanModel());
				}

				while (!cursor.isAfterLast()) {

					String work = cursor.getString(0);

					int workDay = Integer.valueOf(cursor.getString(1));

					String cemer = cursor.getString(2);

					String gubun = cursor.getString(3);

					if ("E0004".equals(work)) {
						if (gubun.trim().isEmpty()) {
							mComplateVal++;
						} else if (gubun.trim().equals("A")) {
							mComplateVal2++;
						} else if (gubun.trim().equals("O")) {
							mComplateVal3++;
						}

					}

					if (backWorkDay < workDay) {
						repairPlanModel = new RepairPlanModel();
						boolean planFlag = true;
						if (cemer != null)
							if (cemer.equals(" "))
								planFlag = false;
						repairPlanModel.addWork(work, planFlag, gubun);
						mRepairPlanModelArray.add(repairPlanModel);

						for (int i = backIndex; i < mDayList.size(); i++) {
							DayInfoModel model = mDayList.get(i).getDayInfoModel();
							if (model != null) {

								int key = Integer.valueOf(model.getCurrentDay());

								if (key == workDay) {

									mDayList.get(i).setRepairPlanModel(repairPlanModel);
									backIndex = i;
									break;
								}
							}

						}

					} else {
						// Log.e("들어올리 없다", "들어올리 없다");
						repairPlanModel = mRepairPlanModelArray.get(mRepairPlanModelArray.size() - 1);
						boolean planFlag = true;
						if (cemer != null)
							if (cemer.equals(" "))
								planFlag = false;
						repairPlanModel.addWork(work, planFlag, gubun);
						mRepairPlanModelArray.set(mRepairPlanModelArray.size() - 1, repairPlanModel);

						//
					}

					backWorkDay = workDay;

					if (cemer != null) {
						if (cemer.equals(" "))
							if (gubun.trim().isEmpty()) {
								mPlanVal++;
							} else if (gubun.trim().equals("A")) {
								mPlanVal2++;
							} else if (gubun.trim().equals("O")) {
								mPlanVal3++;
							}

					}

					// for(int i=0; i< mDayList.size(); i++){
					// if(mDayList.get(i).getDayInfoModel().getDay().toString().equals("29")){
					// Log.e("mPlanVal", ""+mPlanVal);
					// break;
					// }
					// }

					// mComplateVal = mComplateVal +
					// repairPlanModel.getComplate();

					cursor.moveToNext();

				}
				// mComplateVal = complete;
				// mPlanVal = cursor.getCount();
				cursor.close();
				setPlanTitle();
				ArrayList<RepairDayInfoModel> repairDayInfoModels = mDayList;
				maintenance_Date_Adapter.setDateList(repairDayInfoModels);
				initScroll();
				hideProgress();
			}

		}

	}

	@Override
	public void onDismissDialogFragment() {
		// TODO Auto-generated method stub
		maintenanceAdapter.initSelectedMaintenanceArray();
	}

	private void setMonthTitle() {
		if (mTvHeaderTitle != null && mCalendarManager != null) {
			String month = mCalendarManager.getCalendarTitle();
			month = month + " 정비현황";

			mTvHeaderTitle.setText(month);

		}
	}

	private void setDateTitle() {
		if (mTvRightTitle != null && mCalendarManager != null) {
			String month = mCalendarManager.getCalendarTitle();
			String todayStr = maintenance_Date_Adapter.getTodayModel().getDay();
			String week = maintenance_Date_Adapter.getTodayModel()
					.getHeaderText(maintenance_Date_Adapter.getTodayModel().getDayOfWeek());
			int today = Integer.parseInt(todayStr);

			if (today < 10)
				todayStr = "0" + todayStr;

			month = month + "." + todayStr + " [" + week + "]";

			mTvRightTitle.setText(month);

		}
	}

	@Override
	protected void updateRepairPlan() {
		// TODO Auto-generated method stub
		queryMaintenacePlan();
		// if (updateFirstFlag) {
		if (!mCurrentDay.equals(""))
			queryMaintenace(mCurrentDay);
		else {
			if (mCalendarManager != null) {
				String currentDay = // "20130803";
						mCalendarManager.getCurrentDay();

				mCurrentDay = currentDay;
				queryMaintenace(currentDay);
			}
		}
		// } else {
		// updateFirstFlag = true;
		// }
	}

	// 순회정비 계획을 가져온다.
	private void initMaintenace() {

		String currentDay = // "20130803";
				mCalendarManager.getCurrentDay();

		mCurrentDay = currentDay;

		queryMaintenace(currentDay);
		// hideProgress();

	}

	private void queryMaintenacePlan() {

		// showProgress();

		String[] _whereArgs = {};
		String[] _whereCause = {};

		String[] colums = maintenace_plan_colums;

		DbQueryModel dbQueryModel = new DbQueryModel(ConnectController.REPAIR_TABLE_NAME, _whereCause, _whereArgs,
				colums);

		dbQueryModel.setOrderBy("GSTRS asc");

		DbAsyncTask dbAsyncTask = new DbAsyncTask("queryMaintenacePlan", mContext, this, dbQueryModel);

		dbAsyncTask.execute(DbAsyncTask.DB_SELECT);

	}

	private void setPlanTitle() {

		mTvPlan.setText("" + mPlanVal);
		mTvPlan2.setText("" + mPlanVal2);
		mTvPlan3.setText("" + mPlanVal3);
		mTvComplate.setText("" + mComplateVal);
		mTvComplate2.setText("" + mComplateVal2);
		mTvComplate3.setText("" + mComplateVal3);
	}

	@Override
	protected void initSelectedMaintenanceArray(String currentDay) {
		maintenanceAdapter.initSelectedMaintenanceArray();
		setDateTitle();
	}

	@Override
	protected void queryMaintenace(String currentDay) {
		// CommonUtil.showCallStack();
		showProgress("순회 정비계획을 조회 중입니다.");

		Log.e("HomeFragment", "순회 윤승3");

		String[] _whereArgs = { currentDay };
		String[] _whereCause = { "GSTRS" };

		String[] colums = maintenace_colums;

		DbQueryModel dbQueryModel = new DbQueryModel(ConnectController.REPAIR_TABLE_NAME, _whereCause, _whereArgs,
				colums);

		dbQueryModel.setOrderBy(
				"  case CCMSTS   when 'E0001' then '1' when 'E0002' then '' when 'E0003' then '3' when 'E0004' then '4' else '9' end ");

		DbAsyncTask dbAsyncTask = new DbAsyncTask(currentDay, mContext, this, dbQueryModel);
		mAsyncMap.put(currentDay, dbAsyncTask);
		mCurrentDay = currentDay;

		dbAsyncTask.execute(DbAsyncTask.DB_SELECT);
	}

	private void parsingMaintenance(final Cursor asyncCursor) {

		mBaseMaintenanceModels.clear();

		if (asyncCursor == null) {
			hideProgress();
			initMaintenanceEmpty(0);
			return;
		}

		Thread thread = new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub

				mBaseMaintenanceModels.clear();

				asyncCursor.moveToFirst();
				kog.e("Jonathan", " asyncCursor.length :: " + asyncCursor.getCount());
				while (!asyncCursor.isAfterLast()) {
					String time = asyncCursor.getString(asyncCursor.getColumnIndex(maintenace_colums[0]));
					String invnr = asyncCursor.getString(asyncCursor.getColumnIndex(maintenace_colums[1]));
					String name = asyncCursor.getString(asyncCursor.getColumnIndex(maintenace_colums[2]));

					if (name == null || name.equals(" ")) {
						name = asyncCursor.getString(asyncCursor.getColumnIndex(maintenace_colums[3]));
					}

					String driver_name = asyncCursor.getString(asyncCursor.getColumnIndex(maintenace_colums[3]));

					String carname = asyncCursor.getString(asyncCursor.getColumnIndex(maintenace_colums[4]));
					String status = asyncCursor.getString(asyncCursor.getColumnIndex(maintenace_colums[5]));
					String postCode = asyncCursor.getString(asyncCursor.getColumnIndex(maintenace_colums[6]));
					String city = asyncCursor.getString(asyncCursor.getColumnIndex(maintenace_colums[7]));
					String street = asyncCursor.getString(asyncCursor.getColumnIndex(maintenace_colums[8]));
					String tel = asyncCursor.getString(asyncCursor.getColumnIndex(maintenace_colums[9]));
					String progress_status = asyncCursor.getString(asyncCursor.getColumnIndex(maintenace_colums[10]));
					String day = asyncCursor.getString(asyncCursor.getColumnIndex(maintenace_colums[11]));
					String aufnr = asyncCursor.getString(asyncCursor.getColumnIndex(maintenace_colums[12]));
					String equnr = asyncCursor.getString(asyncCursor.getColumnIndex(maintenace_colums[13]));
					String ctrty = asyncCursor.getString(asyncCursor.getColumnIndex(maintenace_colums[14]));
					String drv_mob = asyncCursor.getString(asyncCursor.getColumnIndex(maintenace_colums[15]));
					String cermr = asyncCursor.getString(asyncCursor.getColumnIndex(maintenace_colums[16]));

					// myung 20131227 ADD
					String gueen2 = asyncCursor.getString(asyncCursor.getColumnIndex(maintenace_colums[17]));
					String txt30 = asyncCursor.getString(asyncCursor.getColumnIndex(maintenace_colums[18]));
					String MDLCD = asyncCursor.getString(asyncCursor.getColumnIndex(maintenace_colums[19]));

					String VOCNUM = asyncCursor.getString(asyncCursor.getColumnIndex(maintenace_colums[20]));

					String KUNNR = asyncCursor.getString(asyncCursor.getColumnIndex(maintenace_colums[21]));

					String DELAY = null;
					try {
						DELAY = asyncCursor.getString(asyncCursor.getColumnIndex(maintenace_colums[22]));
					}catch (Exception e){
						e.printStackTrace();
					}
					LogUtil.d("hjt", "hjt delay == " + DELAY);

					String CYCMN_TX = asyncCursor.getString(asyncCursor.getColumnIndex(maintenace_colums[23]));
					for (int i = 0; i < maintenace_colums.length; i++) {
						kog.e("Jonathan",
								"Hello Jonathan MaintenanceStatusFragment colums:: " + i + "  " + maintenace_colums[i]
										+ "  "
										+ asyncCursor.getString(asyncCursor.getColumnIndex(maintenace_colums[i])));
					}

					String apm = asyncCursor.getString(asyncCursor.getColumnIndex(maintenace_colums[24]));
					String vbeln = asyncCursor.getString(asyncCursor.getColumnIndex(maintenace_colums[25]));
					String gubun = asyncCursor.getString(asyncCursor.getColumnIndex(maintenace_colums[26]));
					String reqNo = asyncCursor.getString(asyncCursor.getColumnIndex(maintenace_colums[27]));
					String atvyn = asyncCursor.getString(asyncCursor.getColumnIndex(maintenace_colums[28]));

					// kog.e("Jonathan", "Hello Jonathan
					// MaintenanceStatusFragment:: " + VOCNUM);

					// Log.e("GUEEN2", gueen2);

					if (time != null) {
						if (time.length() > 4) {
							time = time.substring(0, 4);

							String hour = time.substring(0, 2);
							String sec = time.substring(2, 4);
							time = hour + ":" + sec;
						}
					}

					postCode = decrypt(maintenace_colums[4], postCode);
					city = decrypt(maintenace_colums[5], city);
					street = decrypt(maintenace_colums[6], street);
					MaintenanceModel md = new MaintenanceModel(decrypt(maintenace_colums[0], time),
							decrypt(maintenace_colums[2], name), decrypt(maintenace_colums[3], driver_name),
							decrypt(maintenace_colums[1], invnr), postCode + city + street,
							decrypt(maintenace_colums[7], carname), decrypt(maintenace_colums[8], status),
							decrypt(maintenace_colums[9], tel), decrypt(maintenace_colums[10], progress_status),
							decrypt(maintenace_colums[11], day), decrypt(maintenace_colums[12], aufnr),
							decrypt(maintenace_colums[13], equnr), decrypt(maintenace_colums[14], ctrty), postCode,
							city, street, decrypt(maintenace_colums[15], drv_mob),
							decrypt(maintenace_colums[16], cermr), gueen2, txt30, MDLCD, VOCNUM, KUNNR, DELAY, CYCMN_TX, apm, vbeln, gubun, reqNo, atvyn);

					System.out.println("요기는 모니 -__.... " + "MainenanceStateMnetn   ");// +
																						// VOCINFO);

					// MaintenanceModel md = new MaintenanceModel(time,
					// name, invnr,
					// postCode + city + street, carname, status);
					if (cermr.equals(" "))
						mBaseMaintenanceModels.add(md);
					asyncCursor.moveToNext();

					// boolean Flag = mAsyncMap.containsKey(funName);
					//
					// if (!Flag) {
					// return;
					// }

				}
				asyncCursor.close();

				mRootView.post(new Runnable() {

					@Override
					public void run() {
						// Toast.makeText(mContext, "setDataMaintenanceArr",
						// Toast.LENGTH_SHORT).show();
						maintenanceAdapter.setDataArr(mBaseMaintenanceModels);
						hideProgress();
						maintenanceAdapter.setProgressType(mProgressType);
						maintenance_Date_Adapter.initScrollPosition(false);
						initScroll();
						initMaintenanceEmpty(maintenanceAdapter.getCount());
					}
				});
			}
		});

		thread.start();

		return;

	}

	private void initMaintenanceEmpty(int size) {
		if (size > 0) {
			mEmptyView.setVisibility(View.GONE);
			mLvMaintenace.setVisibility(View.VISIBLE);
		} else {
			mEmptyView.setVisibility(View.VISIBLE);
			mLvMaintenace.setVisibility(View.GONE);
		}
	}

	@Override
	public void onSelectedItem(int position, String popName) {

		LinkedHashMap<String, String> linkedHashMap = null;

		if (popName.equals(PROGRESS_FILTER)) {
			linkedHashMap = mProgressFilterMap;
			String item = getSeletedItem(linkedHashMap, position, btn_filiter1);
			mProgressType = item;
			maintenanceAdapter.setProgressType(item);
		} else if (popName.equals(MAINTENANCE_FILTER)) {
			linkedHashMap = mMaintenanceFilterMap;
			String item = getSeletedItem(linkedHashMap, position, btn_filiter2);
			maintenanceAdapter.setMaintenanceType(item);
		} else if (popName.equals(TERM_FILTER)) {
			linkedHashMap = mTermFilterMap;
			String item = getSeletedItem(linkedHashMap, position, btn_filiter3);
			maintenanceAdapter.setTermType(item);
		}
		initScroll();
		initMaintenanceEmpty(maintenanceAdapter.getCount());
	}

	private String getSeletedItem(LinkedHashMap<String, String> linkedHashMap, int position, Button btn) {

		String value = null;
		Iterator<String> it = linkedHashMap.keySet().iterator();

		int i = 0;

		while (it.hasNext()) {
			String strKey = "";
			String strValue = "";

			strKey = it.next();
			strValue = linkedHashMap.get(strKey);

			if (i == position) {
				value = strValue;
				if (btn != null)
					btn.setText(strKey);
				break;
			}

			i++;
		}

		return value;
	}

	private ArrayList<String> getSelectedAUFNR() {
		ArrayList<String> array = new ArrayList<String>();

		ArrayList<BaseMaintenanceModel> selArrayList = maintenanceAdapter.getSelectedMaintenanceModels();
		for (BaseMaintenanceModel baseMaintenanceModel : selArrayList) {
			array.add(baseMaintenanceModel.getAUFNR());
		}

		return array;
	}

	/**
	 * 고객 정보를 반환
	 * 
	 * @return
	 */
	private String getSelectKunnr() {
		if (maintenanceAdapter.getSelectedMaintenanceModels().size() == 0) {
			return "-1";
		}

		if (!(maintenanceAdapter.getSelectedMaintenanceModels().size() == 1)) {
			for (BaseMaintenanceModel baseMaintenanceModel : maintenanceAdapter.getSelectedMaintenanceModels()) {
				System.out.println("KUNNR = " + baseMaintenanceModel.getKUNNR());
			}
			return null;
		}

		return maintenanceAdapter.getSelectedMaintenanceModels().get(0).getKUNNR();
	}

	@Override
	protected void movePlan(ArrayList<BaseMaintenanceModel> models) {

		Duedate_Dialog dd = new Duedate_Dialog(context, models);
		dd.show();
		maintenanceAdapter.initSelectedMaintenanceArray();
	}

	@Override
	protected void initScroll() {
		// TODO Auto-generated method stub
		// mLvMaintenace.smoothScrollToPosition(0);
		mLvMaintenace.setSelectionFromTop(0, 0);
	}

	public void setCurrentDay(String moveDay, String progressType, String showText) {
		initScroll();
		mCurrentDay = moveDay;
		mProgressType = progressType;
		maintenance_Date_Adapter.setSelectedDay(moveDay);

		btn_filiter1.setText(showText);
		setDateTitle();
	}

}
