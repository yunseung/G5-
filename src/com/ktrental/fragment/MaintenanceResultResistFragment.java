package com.ktrental.fragment;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.ktrental.R;
import com.ktrental.activity.Main_Activity;
import com.ktrental.adapter.MaintenancLastItemAdapter;
import com.ktrental.cm.connect.ConnectController;
import com.ktrental.cm.connect.Connector.ConnectInterface;
import com.ktrental.cm.db.DbAsyncTask;
import com.ktrental.cm.db.DbAsyncTask.DbAsyncResLintener;
import com.ktrental.common.DEFINE;
import com.ktrental.common.KtRentalApplication;
import com.ktrental.dialog.KDH_check_list;
import com.ktrental.dialog.Maintenance_Confirm_Dialog;
import com.ktrental.dialog.Mistery_Shopping_Dialog;
import com.ktrental.fragment.MaintentanceInputFragment.OnResultInut;
import com.ktrental.model.CarInfoModel;
import com.ktrental.model.DbQueryModel;
import com.ktrental.model.EtcModel;
import com.ktrental.model.MaintenanceItemModel;
import com.ktrental.model.MaintenanceResultModel;
import com.ktrental.model.TableModel;
import com.ktrental.popup.BaseTextPopup;
import com.ktrental.popup.BaseTextPopup.OnSelectedPopupItem;
import com.ktrental.popup.InventoryPopup;
import com.ktrental.popup.InventoryPopup.OnDismissListener;
import com.ktrental.popup.QuickAction;
import com.ktrental.popup.TimePickPopup;
import com.ktrental.ui.PopupWindowTextView;
import com.ktrental.ui.PopupWindowTextView.OnLayoutListener;
import com.ktrental.util.CommonUtil;
import com.ktrental.util.OnChangeFragmentListener;
import com.ktrental.util.OnEventCancelListener;
import com.ktrental.util.OnEventOkListener;
import com.ktrental.util.kog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;

public class MaintenanceResultResistFragment extends BaseFragment implements OnClickListener, OnResultInut,
		DbAsyncResLintener, OnSelectedPopupItem, OnKeyListener, ConnectInterface {

	private Button mBtnCareer;
	private FrameLayout mFlEmpty;

	/**
	 * 선택된 차량 보여줘야 되는 총 정보
	 */
	private CarInfoModel mCarInfoModel;

	private PopupWindowTextView mTvLastMileage;
	private Button mBtnEmergency;
	private Button mBtnMaintentnceStartTime;
	private Button mBtnMaintenanceEndTime;
	private EditText mEtRemarks;
	private EditText mEtNextRequest;
	private EditText mEtContract;
	private Button mBtnsave;
	private Button mBtnDelete;
	// private FrameLayout mMaintenanceSaveDummy2560;



	private ListView mLvItem;
	private MaintenancLastItemAdapter mLastItemAdapter;

	private OnCheckingResult mOnCheckingResult;
	private ArrayList<MaintenanceItemModel> mLastItemModels = new ArrayList<MaintenanceItemModel>();
	private ArrayList<MaintenanceItemModel> mTotalLastItemModels = new ArrayList<MaintenanceItemModel>();

	private LinkedHashMap<String, String> mEmergencyMap = new LinkedHashMap<String, String>();

	private HashMap<String, Boolean> mFirstMap = new HashMap<String, Boolean>();

	private boolean mFirstFlag = true;

	private MaintenanceResultModel mResultModel;

	private MaintentanceInputFragment mMaintentanceInputFragment;

	private String mStartDay = "";

	private String mStartTime = "";
	private String mEndTime = "";
	private String mMileage = "";
	private String mTempMileage = "";

	private String mProgressStatus = "";
	private String mGubun;

	private OnModify mOnModify;

	private int[] mInventoryLocation = new int[2];

	private boolean firstFlag = true;

	private OnNumberResultCancel mOnNumberResultCancel;

	public interface OnCheckingResult {
		void onCheckingResult(boolean show, MaintenanceResultModel model);
	}

	public interface OnNumberResultCancel {
		void onResultCancel(boolean success, MaintenanceResultModel model);
	}

	public interface OnModify {
		void onModify(String gubun);
	}

	public void setCarInfoModel(CarInfoModel aCarInfoModel) {
		this.mCarInfoModel = aCarInfoModel;
		if (mFirstMap.containsKey(aCarInfoModel.getCarNum())) {
			mFirstFlag = false;
			// myung 20131231 ADD 점검결과 등록 BACK버튼 클릭시 확인팝업창 뛰우기
			DEFINE.RESIST_RESULT_FIRST_FLAG = false;
		} else {
			mFirstMap.put(aCarInfoModel.getCarNum(), true);
			mProgressStatus = aCarInfoModel.getProgress_status();
			if (mProgressStatus.equals("E0002")) {

				String startDay = CommonUtil.setDotDate(CommonUtil.getCurrentDay());

				startDay = startDay + "(" + CommonUtil.getDayOfWeek() + ") ";

				mStartDay = startDay;
				mStartTime = CommonUtil.setDotTime(CommonUtil.getCurrentTimeHHMM());
				if (mBtnMaintentnceStartTime != null) {
					mBtnMaintentnceStartTime.setText(startDay + mStartTime);
				}

			} else {
				if (mProgressStatus.equals("E0004"))
					if (aCarInfoModel.getGUBUN().equals("E")) {
						String startDay = CommonUtil.setDotDate(CommonUtil.getCurrentDay());

						startDay = startDay + "(" + CommonUtil.getDayOfWeek() + ") ";

						mStartDay = startDay;
						mStartTime = CommonUtil.setDotTime(CommonUtil.getCurrentTimeHHMM());
						if (mBtnMaintentnceStartTime != null) {
							mBtnMaintentnceStartTime.setText(startDay + mStartTime);
						}
					}
			}
			mMileage = aCarInfoModel.getLastMileage();
			if (mMileage.equals("") || mMileage.equals(" "))
				mMileage = "0";
			// Log.e("$$$$$$", mMileage);
			if (mProgressStatus.equals("E0004"))
				;
			else
				mTvLastMileage.setText("" + CommonUtil.currentpoint(mMileage) + " km");

			mTempMileage = mMileage;
			initGubun();

			/**
			 * 2018.07.16 LDCC MobBusiness Dept. lsi
			 * 10000 km 이상이거나 최종 주행거리 이하로 입력하는경우 알림 팝업 1회 표시
			 */
			isShowingMileageCheckPopup = false;
		}
	}

	public void setGUBUN(String gubun) {
		if (mCarInfoModel != null)
			mCarInfoModel.setGUBUN(gubun);
		if (gubun.equals("E")) {
			if (mProgressStatus.equals("E0004")) {
				mBtnsave.setText("긴급정비 등록");

				mTvLastMileage.setEnabled(true);
				mBtnEmergency.setEnabled(true);
				mBtnMaintentnceStartTime.setEnabled(true);
				mBtnMaintenanceEndTime.setEnabled(false);
				mEtRemarks.setEnabled(true);
				mEtNextRequest.setEnabled(true);
				mEtContract.setEnabled(true);

				mBtnCareer.setEnabled(true);
				mBtnDelete.setEnabled(true);
				mOnModify.onModify("E");

				mResultModel = null;
				// mLastItemModels.clear();
				// mLastItemAdapter.notifyDataSetChanged();

				mEtRemarks.setText("");
				mEtContract.setText("");
				mEtNextRequest.setText("");
				mBtnMaintenanceEndTime.setText("정비결과 저장 시간으로 설정");
				mBtnEmergency.setText("");
				mLastItemModels.clear();
				mLastItemAdapter.setSetItemFlag(false);
				mLastItemAdapter.setData(new ArrayList<MaintenanceItemModel>());
				mLastItemAdapter.initSelectedMaintenanceArray();

				String startDay = CommonUtil.setDotDate(CommonUtil.getCurrentDay());

				startDay = startDay + "(" + CommonUtil.getDayOfWeek() + ") ";

				mStartDay = startDay;
				mStartTime = CommonUtil.setDotTime(CommonUtil.getCurrentTimeHHMM());
				if (mBtnMaintentnceStartTime != null) {
					mBtnMaintentnceStartTime.setText(startDay + mStartTime);
				}

				mMileage = mCarInfoModel.getLastMileage();
				if (mMileage.equals("") || mMileage.equals(" "))
					mMileage = "0";
				mTvLastMileage.setText("" + CommonUtil.currentpoint(mMileage) + " km");
			}
		} else {
			mBtnsave.setText("정비 결과 저장");

		}
	}

	public void setTotalStockSelectArray(ArrayList<MaintenanceItemModel> array) {
		mTotalLastItemModels = array;
	}

	public void setResultModel(MaintenanceResultModel aResultModel) {
		this.mResultModel = aResultModel;

		EtcModel etc = aResultModel.getmEtcModel();

		mTvLastMileage.setText(CommonUtil.currentpoint(etc.getDistance()) + " km");
		mEtRemarks.setText(etc.getRemarks());
		mEtNextRequest.setText(etc.getNextRequest());
		mEtContract.setText(etc.getContactRequest());

		String startDay = CommonUtil.setDotDate(CommonUtil.getCurrentDay());

		startDay = startDay + "(" + CommonUtil.getDayOfWeek() + ") ";

		mStartDay = startDay;

		mBtnMaintentnceStartTime.setText(startDay + CommonUtil.setDotTime(etc.getStartTime().substring(0, 4)));
		mBtnMaintenanceEndTime.setText(mStartDay + CommonUtil.setDotTime(etc.getEndTime().substring(0, 4)));
		mBtnMaintenanceEndTime.setEnabled(true);
		mBtnEmergency.setText(etc.getEmergency());
		mLastItemModels = mResultModel.getmLastItemModels();
		mProgressStatus = aResultModel.getmCarInfoModel().getProgress_status();

		if (mProgressStatus.equals("E0004")) {
			if (mCarInfoModel == null || mCarInfoModel.getGUBUN().equals(" ")) {
				// Log.e("", ""+etc.getISDD());
				if (etc.getISDD() == null)
					startDay = new java.text.SimpleDateFormat("yyyyMMdd").format(new java.util.Date());
				else
					startDay = etc.getISDD();

				startDay = CommonUtil.setDotDate(startDay) + "(" + CommonUtil.getDayOfWeek(startDay) + ") ";

				mBtnMaintentnceStartTime.setText(startDay + CommonUtil.setDotTime(etc.getStartTime().substring(0, 4)));

				if (etc.getIEDD() == null)
					startDay = new java.text.SimpleDateFormat("yyyyMMdd").format(new java.util.Date());
				else
					startDay = etc.getIEDD();
				// startDay = etc.getIEDD();

				startDay = CommonUtil.setDotDate(startDay) + "(" + CommonUtil.getDayOfWeek(startDay) + ") ";

				mBtnMaintenanceEndTime.setText(startDay + CommonUtil.setDotTime(etc.getEndTime().substring(0, 4)));
			}
		}
		mLastItemAdapter.setSetItemFlag(false);
		mLastItemAdapter.setData(mLastItemModels);

		// myung 20131230 ADD 고객 확인 시 입력한 정비항목이 고객 확인창에 표시도지 않는 현상
		if (mLastItemModels.isEmpty())
			mFlEmpty.setVisibility(View.VISIBLE);
		else
			mFlEmpty.setVisibility(View.GONE);
	}
	public MaintenanceResultResistFragment(){}

	public MaintenanceResultResistFragment(String className, OnChangeFragmentListener changeFragmentListener,
			OnCheckingResult onCheckingResult, OnModify onModify, OnNumberResultCancel onNumberResultCancel, String gubun) {
		super(className, changeFragmentListener);
		mOnCheckingResult = onCheckingResult;
		mOnModify = onModify;
		mOnNumberResultCancel = onNumberResultCancel;
		mGubun = gubun;
		// TODO Auto-generated constructor stub

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		mRootView = inflater.inflate(R.layout.maintenance_result_resist_layout, null);

		initSettingViews();

		mMaintentanceInputFragment = new MaintentanceInputFragment();
		mMaintentanceInputFragment.setmLastItemModels(mLastItemModels);

		return mRootView;
	}

	private void updateCompleteCCMSTS(String CCMSTS) {
		ContentValues contentValues = new ContentValues();
		// contentValues.put("CCMSTS", "E0004");
		contentValues.put("CCMSTS", CCMSTS);
		contentValues.put("GSTRS", mResultModel.getmCarInfoModel().getDay());
		contentValues.put("AUFNR", mResultModel.getmCarInfoModel().getAUFNR());

		String[] keys = new String[2];
		keys[0] = "GSTRS";
		keys[1] = "AUFNR";

		DbAsyncTask dbAsyncTask = new DbAsyncTask("updateCompleteCCMSTS", DEFINE.REPAIR_TABLE_NAME, mContext, this,
				contentValues, keys);

		dbAsyncTask.execute(DbAsyncTask.DB_UPDATE);

	}

	private void initSettingViews() {

		mBtnCareer = (Button) mRootView.findViewById(R.id.btn_item);
		mBtnCareer.setOnClickListener(this);

		mFlEmpty = (FrameLayout) mRootView.findViewById(R.id.fl_empty);

		mTvLastMileage = (PopupWindowTextView) mRootView.findViewById(R.id.tv_last_mileage);
		mTvLastMileage.setOnClickListener(this);
		mTvLastMileage.setTextColor(Color.RED);

		mBtnEmergency = (Button) mRootView.findViewById(R.id.btn_emergency);
		mBtnEmergency.setOnClickListener(this);

		mBtnMaintentnceStartTime = (Button) mRootView.findViewById(R.id.btn_maintentnce_start_time);
		mBtnMaintentnceStartTime.setOnClickListener(this);

		mBtnMaintenanceEndTime = (Button) mRootView.findViewById(R.id.btn_maintenance_end_time);
		mBtnMaintenanceEndTime.setOnClickListener(this);

		mEtRemarks = (EditText) mRootView.findViewById(R.id.et_remarks);
		mEtNextRequest = (EditText) mRootView.findViewById(R.id.et_next_request);

		mEtContract = (EditText) mRootView.findViewById(R.id.et_contract);

		mBtnsave = (Button) mRootView.findViewById(R.id.btn_maintenance_save);
		mBtnsave.setOnClickListener(this);

		mLastItemAdapter = new MaintenancLastItemAdapter(mContext, MaintenancLastItemAdapter.RESIST, mGubun);
		mLvItem = (ListView) mRootView.findViewById(R.id.lv_item);

		// mMaintenanceSaveDummy2560 =
		// (FrameLayout)mRootView.findViewById(R.id.maintenance_save_dummy_2560);

		mBtnDelete = (Button) mRootView.findViewById(R.id.btn_delete);
		mBtnDelete.setOnClickListener(this);

		// setDummy(null);
		// mLastItemAdapter.notifyDataSetChanged();
		mLastItemAdapter.setSetItemFlag(false);
		mLastItemAdapter.setData(mLastItemModels);
		mLvItem.setAdapter(mLastItemAdapter);

		mEtRemarks.setOnClickListener(this);
		// mEtRemarks.setOnKeyListener(this);
		// mEtNextRequest.setOnKeyListener(this);
		// mEtContract.setOnKeyListener(this);

		mEtRemarks.setOnEditorActionListener(new OnEditorActionListener() {

			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				// TODO Auto-generated method stub
				// Log.e("event", ""+event);
				// Log.e("actionId", ""+actionId);
				if (actionId == EditorInfo.IME_ACTION_NEXT) {
					mEtNextRequest.setNextFocusDownId(R.id.et_next_request);
				}
				return false;
			}
		});

		mEtNextRequest.setOnEditorActionListener(new OnEditorActionListener() {

			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				// TODO Auto-generated method stub
				if (actionId == EditorInfo.IME_ACTION_NEXT) {
					mEtContract.setNextFocusDownId(R.id.et_contract);
				}
				return false;
			}
		});

		mEtContract.setOnEditorActionListener(new OnEditorActionListener() {

			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				// TODO Auto-generated method stub
				if (actionId == EditorInfo.IME_ACTION_DONE) {
					pre.sendEmptyMessageDelayed(0, 100);
				}
				return false;
			}
		});

		mTvLastMileage.setOnLayoutListener(new OnLayoutListener() {

			@Override
			public void onLayout() {
				// TODO Auto-generated method stub
				if (firstFlag) {
					mTvLastMileage.getLocationOnScreen(mInventoryLocation);
					firstFlag = false;
				}
			}
		});

		// myung 20131120 ADD
		if (DEFINE.DISPLAY.equals("2560")) {
			mRootView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, 1420));
			// mMaintenanceSaveDummy2560.setPadding(0, 0, 0, 0);
		}

		initQuery();

		// 2014-03-25 KDH 추가됨.순회정비는 제외인데 일단 냅둬-_-;
		Button btn_check = (Button) mRootView.findViewById(R.id.btn_check);
		btn_check.setVisibility(View.GONE);
		btn_check.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (mCarInfoModel != null && mCarInfoModel.getAUFNR() != null) {
					String AUFNR = mCarInfoModel.getAUFNR();
					kog.e("KDH", "AUFNR  =  " + AUFNR);
					KDH_check_list mKdhCheck = new KDH_check_list(mContext, AUFNR);
					mKdhCheck.show();
				}
			}
		});
	}

	Handler pre = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(mEtContract.getWindowToken(), 0);
		}
	};

	private void initQuery() {

		showProgress();
		String[] _whereArgs = { "PM022" };
		String[] _whereCause = { "ZCODEH" };

		String[] colums = { "ZCODEVT", "ZCODEV" };

		DbQueryModel dbQueryModel = new DbQueryModel(ConnectController.O_ITAB1_TABLE_NAME, _whereCause, _whereArgs,
				colums);

		DbAsyncTask dbAsyncTask = new DbAsyncTask("initQuery", mContext, this, dbQueryModel);
		dbAsyncTask.execute(DbAsyncTask.DB_SELECT);
	}

	// myung 20131204 ADD 완료시 정비결과 수정 버튼 삭제 -> 정비 취소 버튼 추가.
	private void clickCancel() {

		// ZMO_1050_WR02 호출
		connectZMO_1050_WR02();

		// updateStockComplete4Cancel();

	}

	// myung 20131204 ADD 완료시 정비결과 수정 버튼 삭제 -> 정비 취소 버튼 추가.
	private void connectZMO_1050_WR02() {
		showProgress("정비 취소 중 입니다.");
		ConnectController connectController = new ConnectController(this, mContext);
		connectController.setZMO_1050_WR02(mResultModel.getmCarInfoModel().getAUFNR(),
				mResultModel.getmCarInfoModel().getTourCarNum(), mResultModel.getmCarInfoModel().getCEMER());
	}

	@Override
	public void connectResponse(String FuntionName, String resultText, String MTYPE, int resulCode,
			TableModel tableModel) {
		// TODO Auto-generated method stub
		hideProgress();
		if (MTYPE == null || !MTYPE.equals("S")) {
			showEventPopup2(null, "" + resultText);
			return;
		}

		if (FuntionName.equals("ZMO_1050_WR02")) {

			if (mBtnsave.getTag().toString().equals("0"))
				updateCompleteCCMSTS("E0002");
			else if (mBtnsave.getTag().toString().equals("1"))
				updateCompleteCCMSTS("E0004");

			onCancelShow();
			
			
		}
	}

	private void initData() {
		mTvLastMileage.setEnabled(true);
		mBtnEmergency.setEnabled(true);
		mBtnMaintentnceStartTime.setEnabled(true);
		mBtnMaintenanceEndTime.setEnabled(false);
		mEtRemarks.setEnabled(true);
		mEtNextRequest.setEnabled(true);
		mEtContract.setEnabled(true);

		mBtnCareer.setEnabled(true);
		mBtnDelete.setEnabled(true);
		mBtnsave.setText("정비 결과 저장");
		// myung 20131216 UPDATE "U"의미 없음 " "으로 변경
		// mCarInfoModel.setGUBUN("U");
		// mOnModify.onModify("U");
		mCarInfoModel.setGUBUN(" ");
		mOnModify.onModify(" ");

		mResultModel = null;
		mLastItemModels.clear();
		mLastItemAdapter.notifyDataSetChanged();

		mEtRemarks.setText("");
		mEtContract.setText("");
		mEtNextRequest.setText("");
		mBtnMaintenanceEndTime.setText("정비결과 저장 시간으로 설정");
		mBtnEmergency.setText("");
		mLastItemModels.clear();
		mLastItemAdapter.setData(new ArrayList<MaintenanceItemModel>());
		mLastItemAdapter.initSelectedMaintenanceArray();

		String startDay = CommonUtil.setDotDate(CommonUtil.getCurrentDay());

		startDay = startDay + "(" + CommonUtil.getDayOfWeek() + ") ";

		mStartDay = startDay;
		mStartTime = CommonUtil.setDotTime(CommonUtil.getCurrentTimeHHMM());
		if (mBtnMaintentnceStartTime != null) {
			mBtnMaintentnceStartTime.setText(startDay + mStartTime);
		}

		mMileage = mCarInfoModel.getLastMileage();
		mTvLastMileage.setText("" + CommonUtil.currentpoint(mMileage) + " km");
	}

	// myung 20131204 ADD 완료시 정비결과 수정 버튼 삭제 -> 정비 취소 버튼 추가.
	private void updateStockComplete4Cancel() {

		for (MaintenanceItemModel model : mResultModel.getmLastItemModels()) {
			ContentValues contentValues = new ContentValues();
			contentValues.put("MATKL", model.getMaintenanceGroupModel().getName_key());
			contentValues.put("MATNR", model.getMATNR());

			int val = model.getStock() + model.getConsumption();

			contentValues.put("LABST", "" + val);

			String[] keys = new String[2];
			keys[0] = "MATKL";
			keys[1] = "MATNR";

			DbAsyncTask dbAsyncTask = new DbAsyncTask("updateStockComplete4Cancel", DEFINE.STOCK_TABLE_NAME, mContext,
					this, contentValues, keys);

			dbAsyncTask.execute(DbAsyncTask.DB_UPDATE);
		}

		// myung 20131226 ADD
		for (int i = 0; mLastItemModels.size() > i; i++) {
			mLastItemModels.get(i).setConsumption(0);
			mResultModel.getmLastItemModels().get(i).setConsumption(0);
		}

	}

	@Override
	public void onClick(View v) {
		CommonUtil.hideKeyboad(mContext, mEtContract);
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btn_item:
			clickCareer(); // 정비항목입력
			break;
		case R.id.btn_maintenance_save:

			Mistery_Shopping_Dialog dlg = new Mistery_Shopping_Dialog(mContext, "30");
			dlg.setOnDismissListener(new Dialog.OnDismissListener() {
				@Override
				public void onDismiss(DialogInterface dialog) {
					// myung 20131204 ADD 완료시 정비결과 수정 버튼 삭제 -> 정비 취소 버튼 추가.
					if (mProgressStatus.equals("E0004")) {
						// myung 20131213 ADD 정비->일간정비현황->완료건 선택->긴급정비시작->완료시에 앱종료 현상
						if (mBtnsave.getTag() == null)
						{
							Maintenance_Confirm_Dialog rpd = new Maintenance_Confirm_Dialog(mContext, mLastItemModels);
							rpd.setOnDismissListener(new DialogInterface.OnDismissListener() {

								@Override
								public void onDismiss(DialogInterface dialog)
								{
									clickSave();
								}

							});
							rpd.show();

						}
						else if (mBtnsave.getTag().toString().equals("0"))
							clickCancel();
						else if (mBtnsave.getTag().toString().equals("1")) {
							if (mLastItemModels.size() == 0) {
								showEventPopup1("", "입력된 정비 항목이 없습니다.\n정말 저장하시겠니까?", new OnEventOkListener() {

									@Override
									public void onOk() {
										// TODO Auto-generated method stub
										Maintenance_Confirm_Dialog rpd = new Maintenance_Confirm_Dialog(mContext, mLastItemModels);
										rpd.setOnDismissListener(new DialogInterface.OnDismissListener() {

											@Override
											public void onDismiss(DialogInterface dialog)
											{
												clickSave();
											}

										});
										rpd.show();
									}
								}, new OnEventCancelListener() {

									@Override
									public void onCancel() {
										// TODO Auto-generated method stub
										return;
									}
								});
							} else {
								clickSave();
							}

						}

					} else if (mProgressStatus.equals("E0002")) {

						Maintenance_Confirm_Dialog rpd = new Maintenance_Confirm_Dialog(mContext, mLastItemModels);
						rpd.setOnDismissListener(new DialogInterface.OnDismissListener() {

							@Override
							public void onDismiss(DialogInterface dialog)
							{
								clickSave();
							}

						});
						rpd.show();

					}
					// myung 20131216 UPDATE "U"의미 없음 " "으로 변경
					// else if (mCarInfoModel.getGUBUN().equals("U")) {
					else if (mCarInfoModel.getGUBUN().equals(" ")) {

						Maintenance_Confirm_Dialog rpd = new Maintenance_Confirm_Dialog(mContext, mLastItemModels);
						rpd.setOnDismissListener(new DialogInterface.OnDismissListener() {

							@Override
							public void onDismiss(DialogInterface dialog)
							{
								clickSave();
							}

						});
						rpd.show();

					} else {
						if (mCarInfoModel.getGUBUN().equals("E")) {

							Maintenance_Confirm_Dialog rpd = new Maintenance_Confirm_Dialog(mContext, mLastItemModels);
							rpd.setOnDismissListener(new DialogInterface.OnDismissListener() {

								@Override
								public void onDismiss(DialogInterface dialog)
								{
									clickSave();
								}

							});
							rpd.show();

						} else {
							clickOnModifyResult();
						}
					}
				}
			});
			dlg.show();












			// myung 20131204 ADD 완료시 정비결과 수정 버튼 삭제 -> 정비 취소 버튼 추가.
//			if (mProgressStatus.equals("E0004")) {
//				// myung 20131213 ADD 정비->일간정비현황->완료건 선택->긴급정비시작->완료시에 앱종료 현상
//				if (mBtnsave.getTag() == null)
//					clickSave();
//				else if (mBtnsave.getTag().toString().equals("0"))
//					clickCancel();
//				else if (mBtnsave.getTag().toString().equals("1")) {
//					if (mLastItemModels.size() == 0) {
//						showEventPopup1("", "입력된 정비 항목이 없습니다.\n정말 저장하시겠니까?", new OnEventOkListener() {
//
//							@Override
//							public void onOk() {
//								// TODO Auto-generated method stub
//								clickSave();
//							}
//						}, new OnEventCancelListener() {
//
//							@Override
//							public void onCancel() {
//								// TODO Auto-generated method stub
//								return;
//							}
//						});
//					} else {
//						clickSave();
//					}
//
//				}
//
//			} else if (mProgressStatus.equals("E0002")) {
//
//				clickSave(); // 점검결과저장
//			}
//			// myung 20131216 UPDATE "U"의미 없음 " "으로 변경
//			// else if (mCarInfoModel.getGUBUN().equals("U")) {
//			else if (mCarInfoModel.getGUBUN().equals(" ")) {
//
//				clickSave(); // 점검결과저장
//			} else {
//				if (mCarInfoModel.getGUBUN().equals("E")) {
//
//					clickSave(); // 점검결과저장
//				} else {
//					clickOnModifyResult();
//				}
//			}



			break;
		case R.id.btn_maintentnce_start_time:
			clickStartTime();
			break;

		case R.id.btn_maintenance_end_time:
			clickEndTime();
			break;
		case R.id.btn_emergency:
			clickEmergency();
			break;

		case R.id.btn_delete:

			clickDelete();
			break;

		case R.id.tv_last_mileage:
			clickMileage();
			break;

		default:
			break;
		}
	}

	private void clickOnModifyResult() {

		showEventPopup1("", "이전 점검결과는 삭제 됩니다.\n 수정하시겠습니까?", new OnEventOkListener() {

			@Override
			public void onOk() {
				// TODO Auto-generated method stub
				mTvLastMileage.setEnabled(true);
				mBtnEmergency.setEnabled(true);
				mBtnMaintentnceStartTime.setEnabled(true);
				mBtnMaintenanceEndTime.setEnabled(false);
				mEtRemarks.setEnabled(true);
				mEtNextRequest.setEnabled(true);
				mEtContract.setEnabled(true);

				mBtnCareer.setEnabled(true);
				mBtnDelete.setEnabled(true);
				mBtnsave.setText("정비 결과 저장");
				// myung 20131216 UPDATE "U"의미 없음 " "으로 변경
				// mCarInfoModel.setGUBUN("U");
				// mOnModify.onModify("U");
				mCarInfoModel.setGUBUN(" ");
				mOnModify.onModify(" ");

				mResultModel = null;
				mLastItemModels.clear();
				mLastItemAdapter.notifyDataSetChanged();

				mEtRemarks.setText("");
				mEtContract.setText("");
				mEtNextRequest.setText("");
				mBtnMaintenanceEndTime.setText("정비결과 저장 시간으로 설정");
				mBtnEmergency.setText("");
				mLastItemModels.clear();
				mLastItemAdapter.setData(new ArrayList<MaintenanceItemModel>());
				mLastItemAdapter.initSelectedMaintenanceArray();

				String startDay = CommonUtil.setDotDate(CommonUtil.getCurrentDay());

				startDay = startDay + "(" + CommonUtil.getDayOfWeek() + ") ";

				mStartDay = startDay;
				mStartTime = CommonUtil.setDotTime(CommonUtil.getCurrentTimeHHMM());
				if (mBtnMaintentnceStartTime != null) {
					mBtnMaintentnceStartTime.setText(startDay + mStartTime);
				}

				mMileage = mCarInfoModel.getLastMileage();
				mTvLastMileage.setText("" + CommonUtil.currentpoint(mMileage) + " km");

			}
		}, null);

	}

	private void clickEmergency() {

		if (KtRentalApplication.getEmergency(mCarInfoModel.getCarNum())) {
			BaseTextPopup pop = new BaseTextPopup(mContext, mEmergencyMap, "Emergency");
			pop.setOnSelectedItem(this);
			pop.show(mBtnEmergency);
		} else {
			showEventPopup2(new OnEventOkListener() {

				@Override
				public void onOk() {
					// TODO Auto-generated method stub

				}
			}, "긴급정비를 체크해주세요.");
		}
	}

	private boolean isShowingMileageCheckPopup = false;
	private final int MAX_MILEAGE = 10000;

	private void clickMileage() {
		InventoryPopup inventoryPopup = new InventoryPopup(mContext, QuickAction.HORIZONTAL, R.layout.inventory_popup,
				InventoryPopup.TYPE_MONEY);

		inventoryPopup.show(mTvLastMileage, mInventoryLocation);
		inventoryPopup.setOnDismissListener(new OnDismissListener() {

			@Override
			public void onDismiss(String result, int position) {
				// TODO Auto-generated method stub
				if (result != null)
					if (result.length() > 0) {
						String selectMileage = result.replaceAll(",", "");
						mMileage = mMileage.replaceAll(" ", "");

						int iResult = Integer.parseInt(selectMileage);
						//int iMileage = Integer.parseInt(mMileage);
						// myung 20131230 UPDATE 주행거리가 이전보다 크거나 같으면 입력 가능.
						// if (iResult <= iMileage) {
						// myung 20131230 로직 임시 삭제
						// if (iResult < iMileage) {
						// showEventPopup2(null, "최종운행거리는 운행거리 보다 커야 합니다.");
						// return;
						// } else {
						/**
						 * 2018.07.16 LDCC MobBusiness Dept. lsi
						 * 최종주행거리 + 10000km 이상이거나 최종 주행거리 이하로 입력하는경우 알림 팝업 1회 표시
						 */
						int iMileage = 0;

						try {
							String lastMileage = mCarInfoModel.getLastMileage().replaceAll(" ", "");
							iMileage = Integer.parseInt(lastMileage);
						} catch (Exception e) {
							Log.e(MaintenanceResultResistFragment.class.getSimpleName(), e.getMessage());
						}

						if (!isShowingMileageCheckPopup) {
							if (iResult >= (iMileage + MAX_MILEAGE) || iResult <= iMileage) {
								isShowingMileageCheckPopup = true;
								showEventPopup2(null, "운행거리를 다시 한번 확인해 주세요.");
								return;
							}
						}
						mMileage = result.replaceAll(",", "");
						// Log.e("########", result);
						mTvLastMileage.setText(result + " km");
						mTvLastMileage.setTextColor(Color.BLACK);

						// }
					}
			}
		});
	}

	private void clickDelete() {
		ArrayList<MaintenanceItemModel> models = mLastItemAdapter.getSelectedMaintenanceModels();
		for (MaintenanceItemModel maintenanceItemModel : models) {
			mLastItemModels.remove(maintenanceItemModel);
		}
		// mLastItemModels.clear();
		mLastItemAdapter.initSelectedMaintenanceArray();
		mLastItemAdapter.setSetItemFlag(false);
		mLastItemAdapter.setData(mLastItemModels);
	}

	private void clickStartTime() {

		// TimePopup timePopup = new TimePopup(mContext, new OnTimeListener() {
		//
		// @Override
		// public void onTimeEnd(String time) {
		// // TODO Auto-generated method stub
		// mBtnMaintentnceStartTime.setText(time);
		// }
		// });
		// timePopup.show(mBtnMaintentnceStartTime);
		TimePickPopup inventoryPopup = new TimePickPopup(mContext, QuickAction.HORIZONTAL, R.layout.time_pick_popup);

		inventoryPopup.show(mBtnMaintentnceStartTime);
		inventoryPopup.setOnDismissListener(new TimePickPopup.OnDismissListener() {

			@Override
			public void onDismiss(String result, int position) {
				// TODO Auto-generated method stub

				if (result.length() > 4) {
					if (checkStartTime(result))
						if (checkTime(result, "시작")) {
							mBtnMaintentnceStartTime.setText(mStartDay + result);
							mStartTime = result;
						}

				} else {
					showEventPopup2(new OnEventOkListener() {

						@Override
						public void onOk() {
							// TODO Auto-generated method stub

						}
					}, "시작시간은 4자리로 입력해주세요.");
				}
			}
		});
	}

	private void clickEndTime() {

		// TimePopup timePopup = new TimePopup(mContext, new OnTimeListener() {
		//
		// @Override
		// public void onTimeEnd(String time) {
		// // TODO Auto-generated method stub
		// mBtnMaintenanceEndTime.setText(time);
		// }
		// });
		// timePopup.show(mBtnMaintenanceEndTime);

		TimePickPopup inventoryPopup = new TimePickPopup(mContext, QuickAction.HORIZONTAL, R.layout.time_pick_popup);

		inventoryPopup.show(mBtnMaintenanceEndTime);
		inventoryPopup.setOnDismissListener(new TimePickPopup.OnDismissListener() {

			@Override
			public void onDismiss(String result, int position) {
				// TODO Auto-generated method stub
				if (result.length() > 4) {
					if (checkEndTime(result))
						if (checkTime(result, "종료")) {
							mEndTime = result;
							mBtnMaintenanceEndTime.setText(mStartDay + result);
						}
				} else {
					showEventPopup2(new OnEventOkListener() {

						@Override
						public void onOk() {
							// TODO Auto-generated method stub

						}
					}, "종료시간은 4자리로 입력해주세요.");
				}
			}
		});
	}

	private boolean checkStartTime(String result) {
		boolean checkTime = true;
		String time = result.replaceAll(":", "");
		int hour = Integer.parseInt(time.substring(0, 2));
		int min = Integer.parseInt(time.substring(2, 4));

		String endText = mEndTime;// mBtnMaintenanceEndTime.getText().toString();
		if (endText.length() > 4) {
			String endTime = endText.replaceAll(":", "");
			int endHour = Integer.parseInt(endTime.substring(0, 2));
			int endMin = Integer.parseInt(endTime.substring(2, 4));

			if (hour > endHour) {
				showEventPopup2(new OnEventOkListener() {

					@Override
					public void onOk() {
						// TODO Auto-generated method stub

					}
				}, "시작시간은 종료시간 이전 부터 선택해주세요.");
				checkTime = false;
			}

			else if (hour == endHour) {
				if (min >= endMin) {
					showEventPopup2(new OnEventOkListener() {

						@Override
						public void onOk() {
							// TODO Auto-generated method stub

						}
					}, "시작시간은 종료 이전 부터 선택해주세요.");
					checkTime = false;
				}
			}

		}

		return checkTime;
	}

	private boolean checkEndTime(String result) {
		boolean checkTime = true;
		String time = result.replaceAll(":", "");
		int hour = Integer.parseInt(time.substring(0, 2));
		int min = Integer.parseInt(time.substring(2, 4));

		String startText = mStartTime;// mBtnMaintentnceStartTime.getText().toString();
		if (startText.length() > 4) {
			String startTime = startText.replaceAll(":", "");
			int startHour = Integer.parseInt(startTime.substring(0, 2));
			int startMin = Integer.parseInt(startTime.substring(2, 4));

			if (hour < startHour) {
				showEventPopup2(new OnEventOkListener() {

					@Override
					public void onOk() {
						// TODO Auto-generated method stub

					}
				}, "종료시간은 시작시간 이후 부터 선택해주세요.");
				checkTime = false;
			}

			else if (hour == startHour) {
				if (min <= startMin) {
					showEventPopup2(new OnEventOkListener() {

						@Override
						public void onOk() {
							// TODO Auto-generated method stub

						}
					}, "종료시간은 시작시간 이후 부터 선택해주세요.");
					checkTime = false;
				}
			}

		}

		return checkTime;
	}

	private boolean checkTime(String result, String startStr) {
		boolean checkTime = true;

		String time = result.replaceAll(":", "");
		int hour = Integer.parseInt(time.substring(0, 2));
		int min = Integer.parseInt(time.substring(2, 4));
		if (hour < 7) {
			showEventPopup2(new OnEventOkListener() {

				@Override
				public void onOk() {
					// TODO Auto-generated method stub

				}
			}, startStr + "시간은 07시 이후부터 선택해주세요.");
			checkTime = false;
		}

		if (hour > 23) {
			if (min < 50) {
				showEventPopup2(new OnEventOkListener() {

					@Override
					public void onOk() {
						// TODO Auto-generated method stub

					}
				}, startStr + "시간은 23시 50분 이전으로 선택해주세요.");
				checkTime = false;
			}
			if (hour > 24) {
				showEventPopup2(new OnEventOkListener() {

					@Override
					public void onOk() {
						// TODO Auto-generated method stub

					}
				}, startStr + "시간은 23시 50분 이전으로 선택해주세요.");
				checkTime = false;
			}
		}

		if (min > 59) {
			showEventPopup2(new OnEventOkListener() {

				@Override
				public void onOk() {
					// TODO Auto-generated method stub

				}
			}, startStr + "시간은 59분 이하로 선택해주세요.");
			checkTime = false;
		}

		return checkTime;
	}

	private void clickSave() {
		// CheckingComplateFragment

		if (mLastItemModels.size() > 0) {
			CommonUtil.hideKeyboad(mContext, mEtContract);

			if (mEndTime.length() < 4)
				mEndTime = CommonUtil.setDotTime(CommonUtil.getCurrentTimeHHMM());

			if (checkEmpty())
				return;

			// myung 20131209 ADD 운행거리 수정 안함 -> 정비결과저장 버튼 클릭 시 저장됨.
			int nTempMileage = Integer.valueOf(mTempMileage);
			int nMileage = Integer.valueOf(mMileage);
			// myung 20131230 UPDATE 주행거리가 이전보다 크거나 같으면 입력 가능
			// if(nTempMileage >= nMileage){
			// myung 20131230 로직 임시 삭제
			// if(nTempMileage > nMileage)
			// {
			// showEventPopup2(null, "최종운행거리는 운행거리 보다 커야 합니다.");
			// return;
			// }

			if (nMileage == 0) {
				showEventPopup2(null, "운행거리입력하세요.");
				return;
			}

			String mileage = mTvLastMileage.getText().toString();
			mileage = mileage.replace(" km", "");
			mileage = mileage.replaceAll(",", "");
			String remarks = mEtRemarks.getText().toString();
			String nextRequest = mEtNextRequest.getText().toString();
			String contactRequest = mEtContract.getText().toString();

			String startTime = mStartTime;// mBtnMaintentnceStartTime.getText().toString();

			String endTime = mEndTime;// mBtnMaintenanceEndTime.getText().toString();
			String emergency = mBtnEmergency.getText().toString();

			startTime = startTime.replace(":", "") + "00";
			endTime = endTime.replace(":", "") + "00";

			int startHour = Integer.parseInt(startTime.substring(0, 2));
			int endHour = Integer.parseInt(endTime.substring(0, 2));

			if (startHour >= endHour) {
				int startMin = Integer.parseInt(startTime.substring(2, 4));
				int endMin = Integer.parseInt(endTime.substring(2, 4));
				endMin++;
				if (startMin >= endMin) {
					showEventPopup2(null, "정비종료시간은 정비시작시간보다 커야 합니다. ");
					mEndTime = "";
					return;
				}
			}

			// myung 20131216 UPDATE 전송전에 정비항목 변경 시에 "E"로 전송 됨
			// Log.e("emergency.length()", ""+emergency.length());
			if (emergency.length() == 0) {
				emergency = "";
			} else {
				mCarInfoModel.setGUBUN("E");
			}

			EtcModel etcModel = new EtcModel(emergency, startTime, endTime, remarks, nextRequest, contactRequest,
					mileage);

			ArrayList<MaintenanceItemModel> models = new ArrayList<MaintenanceItemModel>();

			for (MaintenanceItemModel itemModel : mLastItemModels) {
				models.add(itemModel.clone());
			}

			MaintenanceResultModel model = new MaintenanceResultModel(models, etcModel, mCarInfoModel);
			initText();
			mOnCheckingResult.onCheckingResult(true, model);
		} else {
			showEventPopup2(new OnEventOkListener() {

				@Override
				public void onOk() {
					// TODO Auto-generated method stub

				}
			}, getString(R.string.maintenance_layout_msg));

		}

	}

	public void initText() {
		mTvLastMileage.setText("");
		mEtRemarks.setText("");
		mEtRemarks.setText("");
		mEtContract.setText("");

		mBtnMaintentnceStartTime.setText("");
		mBtnMaintenanceEndTime.setText("");
		mBtnEmergency.setText("");
		mLastItemModels.clear();
		mLastItemAdapter.setSetItemFlag(false);
		mLastItemAdapter.setData(new ArrayList<MaintenanceItemModel>());
		mLastItemAdapter.initSelectedMaintenanceArray();
	}

	private boolean checkEmpty() {
		boolean isEmpty = false;

		int length = mTvLastMileage.getText().toString().length();

		String mileageEmpty = "'최종운행거리' ";

		if (length <= 0)
			isEmpty = true;
		else
			mileageEmpty = "";

		length = mStartTime.length();// mBtnMaintentnceStartTime.getText().toString().length();

		String startTimeEmpty = "'정비시작시간' ";

		if (length <= 0)
			isEmpty = true;
		else
			startTimeEmpty = "";

		length = mEndTime.length();// mBtnMaintenanceEndTime.getText().toString().length();

		String endTimeEmpty = "'정비종료시간' ";

		if (length <= 0)
			isEmpty = true;
		else
			endTimeEmpty = "";

		String emergencyEmpty = "'긴급정비' ";

		if (KtRentalApplication.getEmergency(mCarInfoModel.getCarNum())) {

			length = mBtnEmergency.getText().toString().length();

			if (length <= 0)
				isEmpty = true;
			else
				emergencyEmpty = "";

		} else
			emergencyEmpty = "";

		if (isEmpty) {
			String toastText = mileageEmpty + startTimeEmpty + endTimeEmpty + emergencyEmpty + "는 입력은 필수 입니다.";

			showEventPopup2(new OnEventOkListener() {

				@Override
				public void onOk() {
					// TODO Auto-generated method stub

				}
			}, toastText);
		}

		return isEmpty;
	}

	private void clickCareer() {
		// Log.i("clickCareer()", "clickCareer()");
		// mFlEmpty.setVisibility(View.GONE);
		mLastItemAdapter.notifyDataSetChanged();

		if (mMaintentanceInputFragment == null)
			mMaintentanceInputFragment = new MaintentanceInputFragment();

		// getChildFragmentManager().executePendingTransactions();

		mMaintentanceInputFragment.setmLastItemModels(mLastItemModels);
		mMaintentanceInputFragment.setCarInfoModel(mCarInfoModel);
		mMaintentanceInputFragment.setOnResultInut(this);
		mMaintentanceInputFragment.setTotalStockSelectArray(mTotalLastItemModels);

		// myung 20131120 2560대응
		int tempX = 1060;
		int tempY = 744;
		if (DEFINE.DISPLAY.equals("2560")) {
			tempX *= 2;
			tempY *= 2;
		}
		mMaintentanceInputFragment.show(getChildFragmentManager(), null);// ,
																			// tempX,
																			// tempY);
	}

	@Override
	public void onResultInput(ArrayList<MaintenanceItemModel> aSelectedModels, boolean bflag) {
		if (aSelectedModels == null)
			mFlEmpty.setVisibility(View.VISIBLE);
		else {
			mFlEmpty.setVisibility(View.GONE);
			setSelectedItemList(aSelectedModels, bflag);
			// 화면에 보여준다. 리스트
		}

	}

	private void setSelectedItemList(ArrayList<MaintenanceItemModel> aSelectedModels, boolean bflag) {
		mLastItemModels = aSelectedModels;
		// boolean isSelected = false;

		for (MaintenanceItemModel maintenanceItemModel : aSelectedModels) {
			int totalConsumption = 0;
			for (MaintenanceItemModel totalModel : mTotalLastItemModels) {

				if (totalModel.getName().equals(maintenanceItemModel.getName())) {
					// myung 20131224 UPDATE
					int stock = maintenanceItemModel.getStock();
					// int stock = maintenanceItemModel.getStock() -
					// maintenanceItemModel.getConsumption();
					totalConsumption = totalModel.getConsumption();
					maintenanceItemModel.setStock(stock);
					maintenanceItemModel.setTotalConsumption(totalConsumption);
				}

				// isSelected = false;
			}

			// maintenanceItemModel.setSelectcConsumption(selectedConsumption);
			maintenanceItemModel.setCheck(false);
		}

		if (mLastItemModels.isEmpty())
			mFlEmpty.setVisibility(View.VISIBLE);
		else
			mFlEmpty.setVisibility(View.GONE);
		// myung 20131224 UPDATE
		// mLastItemAdapter.setData(mLastItemModels);
		if (mFirstFlag && bflag)
			mLastItemAdapter.setSetItemFlag(bflag);
		else
			mLastItemAdapter.setSetItemFlag(false);
		mLastItemAdapter.setData(mLastItemModels);
		mLastItemAdapter.notifyDataSetChanged();
	}

	@Override
	public void onHiddenChanged(boolean hidden) {
		if (!hidden) {
			// Log.e("점검결과등록 오더번호", mCarInfoModel.getAUFNR());
			// mLastItemModels.clear();
			if (mProgressStatus.equals("E0002")) {
				mRootView.postDelayed(new Runnable() {

					@Override
					public void run() {
						if (mFirstFlag)
							clickCareer();
					}
				}, 10);
			} else {
				if (mProgressStatus.equals("E0004")) {
					if (!mCarInfoModel.getGUBUN().equals(" ")) {
						mRootView.postDelayed(new Runnable() {

							@Override
							public void run() {
								if (mFirstFlag)
									clickCareer();
							}
						}, 10);
					}
				}
			}
			if (mLastItemAdapter != null) {
				if (mLastItemModels.isEmpty())
					mFlEmpty.setVisibility(View.VISIBLE);
				else
					mFlEmpty.setVisibility(View.GONE);
			}

		}
		super.onHiddenChanged(hidden);

	}

	@Override
	public void onCompleteDB(String funName, int type, Cursor cursor, String tableName) {
		hideProgress();
		if (funName.equals("initQuery")) {
			if (cursor == null)
				return;

			cursor.moveToFirst();

			while (!cursor.isAfterLast()) {
				mEmergencyMap.put(cursor.getString(0), cursor.getString(1));
				cursor.moveToNext();

			}
			cursor.close();

		} else if (funName.equals("updateStockComplete4Cancel")) {
			// onCancelShow();

		} else if (funName.equals("updateCompleteCCMSTS")) {
			if (mLastItemModels.size() == 0) {
				// showEventPopup2(null, "" + resultText);
				// onCancelShow();
				return;
			}

			// for(int i=0; mLastItemModels.size()>i; i++){
			// mLastItemModels.get(i).setConsumption(0);
			// mResultModel.getmLastItemModels().get(i).setConsumption(0);
			// }

			updateStockComplete4Cancel();

		}
	}

	// myung 20131204 ADD 완료시 정비결과 수정 버튼 삭제 -> 정비 취소 버튼 추가.
	public void onCancelShow() {
		// mDrawerFragment.onSlide();
		showEventPopup1("", "정비결과를 등록하시겠습니까?", new OnEventOkListener() {

			@Override
			public void onOk() {
				// TODO Auto-generated method stub
				// 정비등록화면 초기화
				kog.e("Jonathan", "정비등록 누름.");
				mBtnsave.setText("정비 결과 수정");
				mBtnsave.setTag("1");
				initData();
				ResultCancel();

			}
		}, new OnEventCancelListener() {

			@Override
			public void onCancel() {
				// TODO Auto-generated method stub
				// 이전화면으로 이동.
				Main_Activity activity = (Main_Activity) getActivity();
				if (activity != null)
					activity.onBackPressed();
			}
		});

	}

	// myung 20131217 ADD
	public void ResultCancel() {
		mOnNumberResultCancel.onResultCancel(true, null);
		mBtnMaintentnceStartTime.setEnabled(false);
		mBtnMaintenanceEndTime.setEnabled(false);
		// myung 20131217 ADD 등록취소 -> 등록하기겠습니까?->예-> 아이템 선택 창 뛰우기
		clickCareer();
	}

	@Override
	public void dismiss() {
		// TODO Auto-generated method stub
		KtRentalApplication.changeRepair();
		KtRentalApplication.getInstance().queryMaintenacePlan();
		super.dismiss();
	}

	@Override
	public void onSelectedItem(int position, String popName) {
		// TODO Auto-generated method stub
		if (popName.equals("Emergency")) {
			getSeletedItem(mEmergencyMap, position, mBtnEmergency);
		}
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

	@Override
	public void onResume() {

		super.onResume();
	}

	@Override
	public boolean onKey(View v, int keyCode, KeyEvent event) {
		if (event.getAction() == KeyEvent.ACTION_DOWN) {
			if (keyCode == KeyEvent.KEYCODE_ENTER) {

				// edit.setText("");
				clickSave();
				return true;
			}
		}
		return false;
	}

	public void removeFirstView(String carnum) {
		if (mFirstMap.containsKey(carnum))
			mFirstMap.remove(carnum);
	}

	private void initGubun() {
		if (mProgressStatus.equals("E0004")) {
			if (mCarInfoModel.getGUBUN().equals(" ")) {
				mTvLastMileage.setEnabled(false);
				mBtnEmergency.setEnabled(false);
				mBtnMaintentnceStartTime.setEnabled(false);
				mBtnMaintenanceEndTime.setEnabled(false);
				mEtRemarks.setEnabled(false);
				mEtNextRequest.setEnabled(false);
				mEtContract.setEnabled(false);

				mBtnCareer.setEnabled(false);
				mBtnDelete.setEnabled(false);
				// myung 20131204 DPDATE 정비결과 수정 버튼 삭제 -> 정비 취소 버튼 추가.
				if (mProgressStatus.equals("E0004")) {
					mBtnsave.setText("정비 취소");
					mBtnsave.setTag("0");
				} else {
					mBtnsave.setText("정비 결과 수정");
					mBtnsave.setTag("1");

				}
			}
		} else {

		}

		mLastItemAdapter.notifyDataSetChanged();
	}

	@Override
	public void reDownloadDB(String newVersion) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		// myung 20131231 ADD 점검결과 등록 BACK버튼 클릭시 확인팝업창 뛰우기
		DEFINE.RESIST_RESULT_FIRST_FLAG = true;
		super.onDestroy();
	}

}
