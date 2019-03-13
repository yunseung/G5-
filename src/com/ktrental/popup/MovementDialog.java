package com.ktrental.popup;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.WindowManager.BadTokenException;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ktrental.R;
import com.ktrental.cm.connect.ConnectController;
import com.ktrental.cm.connect.Connector.ConnectInterface;
import com.ktrental.cm.db.DbAsyncTask;
import com.ktrental.cm.db.DbAsyncTask.DbAsyncResLintener;
import com.ktrental.dialog.Camera_Dialog;
import com.ktrental.model.DbQueryModel;
import com.ktrental.model.MovementSaveModel;
import com.ktrental.model.TableModel;
import com.ktrental.popup.BaseTextPopup.OnSelectedPopupItem;
import com.ktrental.ui.PopupWindowButton;
import com.ktrental.util.OnEventOkListener;
import com.ktrental.util.SharedPreferencesUtil;
import com.ktrental.util.kog;

import java.util.Calendar;
import java.util.Iterator;
import java.util.LinkedHashMap;

public class MovementDialog extends BaseTouchDialog implements
		OnSelectedPopupItem, OnClickListener {
	
	
	private String mEQUNR;
	private String mDRIVER;
	private String mAUFNR;
	private String mOilType;
	private String mDriverName;
	private BaseTextPopup mMovePopup;
	private BaseTextPopup mStepPopup;
	private BaseTextPopup mZDOILTYPopup;
	private LinkedHashMap<String, String> mMoveMap = new LinkedHashMap<String, String>();
	private final static String MOVE_DEF = "MOVE_DEF";
	private LinkedHashMap<String, String> mStepMap = new LinkedHashMap<String, String>();
	private final static String STEP_DEF = "STEP_DEF";
	private LinkedHashMap<String, String> mZDOILTYMap = new LinkedHashMap<String, String>();
	private final static String ZDOILTY_DEF = "ZDOILTY_DEF";
	private Button mBtnOTYPE;
	private Button mBtnSTEP;
	private Button mBtnZDOILTY;
	private View mRootView;
	private String mMoveType = "";
	private String mStepType = "";
	private String mZDOILTYOilType = "";
	protected ProgressPopup mProgressPopup;
	private PopupWindowButton mBtnMileage;
	private int[] mInventoryLocation = new int[2];
	private boolean firstFlag = true;
	private String mMileage = "0";
	private String mOilCount = "0";
	private String mOilInput = "0";
	private String mOilMoney = "0";
	private String mWashCar = "0";
	private String mParking = "0";
	private String mToll = "0";
	private String mEtc = "0";
	private String mINVNR = "";
	private String mINVNR_FIRST = "";
	private int mTotal = 0;
	private PopupWindowButton mBtnOilCount;
	private PopupWindowButton mBtnOilMoney;
	private PopupWindowButton mBtnOilInput;
	private EditText mEtOiltype;
	private EditText mEtOilLiter;
	private PopupWindowButton mBtnWashCar;
	private PopupWindowButton mBtnParking;
	private PopupWindowButton mBtnToll;
	private PopupWindowButton mBtnEtc;
	private TextView mTvTotal;
	private Button mBtnName;
	private TextView mBtnCarNum;
	private PopupWindowButton mBtnDATUM;
	private EditText mEtVKBUR;
	private EditText mEtReason;
	private EditText mEtMoneyReason;
	private String mYYMMDD = "";
	private String mHHMM = "";
	private String mSeq = "";
	private String mEE = "";
	private Button mBtnAufnr;
	private String mStep = "";
	private OnSelectedSave mOnSelectedSave;
	private Popup_Window_DelSearch pwDelSearch;
	private boolean CarNumEnableFlag = false;
	
	private String P2 = "P2";

	public interface OnSelectedSave {

		void onSelectedSave(String step);
	}

	// 순회정비 업무
	public MovementDialog(Context context, String mEQUNR, String mDRIVER,
			String mAUFNR, String oilType, String driverName, String seq,
			String step, String carNum, OnSelectedSave aOnSelectedSave) {
		super(context);
		this.mEQUNR = mEQUNR;
		this.mDRIVER = mDRIVER;
		this.mAUFNR = mAUFNR;
		mOilType = oilType;
		mDriverName = driverName;
		mSeq = seq;
		mStep = step;
		mOnSelectedSave = aOnSelectedSave;
		mINVNR_FIRST = carNum;
		mINVNR = mINVNR_FIRST;

	}

	// 배반차 상세내역
	public MovementDialog(Context context, String mEQUNR, String mDRIVER,
			String mAUFNR, String oilType, String driverName, String seq,
			String carNum) {
		super(context);
		this.mEQUNR = mEQUNR;
		this.mDRIVER = mDRIVER;
		this.mAUFNR = mAUFNR;
		mOilType = oilType;
		mDriverName = driverName;
		mSeq = seq;
		mINVNR_FIRST = carNum;
		CarNumEnableFlag = true;
		mINVNR = mINVNR_FIRST;
		
		
		//Jonathan 14.08.04 추가
		if(context instanceof Activity)
		{
			setOwnerActivity((Activity) context);
		}
		
		
		kog.e("Jonathan",  "context.toString() ::  " + context.toString());
		

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		
		getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.movement_dialog_layout);
		mRootView = findViewById(R.id.ll_root);
		TextView tvTitle = (TextView) findViewById(R.id.tv_dialog_title);
		tvTitle.setText("차량운행일지등록");
		mBtnOTYPE = (Button) findViewById(R.id.btn_OTYPE);
		mBtnOTYPE.setOnClickListener(this);
		mBtnSTEP = (Button) findViewById(R.id.btn_OSTEP);
		mBtnSTEP.setOnClickListener(this);
		findViewById(R.id.iv_exit).setOnClickListener(this);
		mBtnZDOILTY = (Button) findViewById(R.id.btn_ZDOILTY);
		mBtnZDOILTY.setOnClickListener(this);
		findViewById(R.id.btn_save).setOnClickListener(this);
		mProgressPopup = new ProgressPopup(mContext);
		mBtnMileage = (PopupWindowButton) findViewById(R.id.btn_mileage);
		mBtnMileage.setOnClickListener(this);
		mBtnMileage
				.setOnLayoutListener(new PopupWindowButton.OnLayoutListener() {

					@Override
					public void onLayout() {
						// TODO Auto-generated method stub
						if (firstFlag) {
							mBtnMileage.getLocationOnScreen(mInventoryLocation);
							firstFlag = false;
						}
					}
				});
		mEtOiltype = (EditText) findViewById(R.id.et_oil_type);
		mEtOilLiter = (EditText) findViewById(R.id.et_liter);
		mBtnOilCount = (PopupWindowButton) findViewById(R.id.btn_oil_count);
		mBtnOilCount.setOnClickListener(this);
		mBtnOilInput = (PopupWindowButton) findViewById(R.id.btn_oil_input);
		mBtnOilInput.setOnClickListener(this);
		mBtnOilMoney = (PopupWindowButton) findViewById(R.id.btn_oil_money);
		mBtnOilMoney.setOnClickListener(this);
		mEtOiltype.setText(mOilType);
		mBtnWashCar = (PopupWindowButton) findViewById(R.id.btn_wash_car);
		mBtnWashCar.setOnClickListener(this);
		mBtnParking = (PopupWindowButton) findViewById(R.id.btn_parking);
		mBtnParking.setOnClickListener(this);
		mBtnToll = (PopupWindowButton) findViewById(R.id.btn_toll);
		mBtnToll.setOnClickListener(this);
		mBtnEtc = (PopupWindowButton) findViewById(R.id.btn_etc);
		mBtnEtc.setOnClickListener(this);
		mTvTotal = (TextView) findViewById(R.id.tv_total_money);
		mBtnName = (Button) findViewById(R.id.btn_DRVNAM);
		mBtnCarNum = (TextView) findViewById(R.id.btn_CarNum);
		mBtnCarNum.setEnabled(CarNumEnableFlag);
		mBtnCarNum.setOnClickListener(this);
		mBtnCarNum.setText(mINVNR);
		mBtnName.setText(mDriverName);
		mBtnOilCount.setText("칸");
		if (mAUFNR.equals(" ")) {
//			mBtnOTYPE.setText("순회차량 이동");
//			mBtnOTYPE.setEnabled(false);
//			mMoveType = "PZ04";
			
			
			//Jonathan 14.08.04 추가. PZ05
//			kog.e("Jonathan", "this.getOwnerActivity().getIntent()." + getOwnerActivity().getIntent().getStringExtra("is_CarManager"));
//			
//			if(P2.equals(getOwnerActivity().getIntent().getStringExtra("is_CarManager")))
//			{
//				mBtnOTYPE.setText("업무용차량 이동");
//				mBtnOTYPE.setEnabled(false);
//				mMoveType = "PZ05";
//			}
//			else
//			{
				mBtnOTYPE.setText("순회차량 이동");
				mBtnOTYPE.setEnabled(false);
				mMoveType = "PZ04";
//			}
			
			
		}
		mBtnDATUM = (PopupWindowButton) findViewById(R.id.btn_DATUM);
		mBtnDATUM.setOnClickListener(this);
		mEtVKBUR = (EditText) findViewById(R.id.et_VKBUR);
		mEtReason = (EditText) findViewById(R.id.et_reason);
		mEtMoneyReason = (EditText) findViewById(R.id.et_money_reason);
		mBtnAufnr = (Button) findViewById(R.id.btn_AUFNR);
		mBtnAufnr.setText(mAUFNR);

		pwDelSearch = new Popup_Window_DelSearch(mContext);
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		setCanceledOnTouchOutside(false);
		WindowManager.LayoutParams lp = getWindow().getAttributes();
		// WindowManager.LayoutParams lp =
		// getDialog().getWindow().getAttributes();
		lp.dimAmount = 0.6f;
		getWindow().setAttributes(lp);
		getWindow().setBackgroundDrawable(new ColorDrawable(0));
		// setCanceledOnTouchOutside(true);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
		initQuery();
	}

	private void initQuery() {
		String[] colums = { "ZCODEVT", "ZCODEV" };
		String[] _whereArgs2 = { "PM110" };
		String[] _whereCause2 = { "ZCODEH" };
		DbQueryModel dbQueryModel = new DbQueryModel(
				ConnectController.O_ITAB1_TABLE_NAME, _whereCause2,
				_whereArgs2, colums);
		DbAsyncTask dbAsyncTask = new DbAsyncTask("PM110", mContext,
				new DbAsyncResLintener() {

					@Override
					public void onCompleteDB(String funName, int type,
							Cursor cursor, String tableName) {
						// TODO Auto-generated method stub
						if (funName.equals("PM110")) {
							// mMoveMap.put("전체", "전체");
							if (cursor == null)
								return;
							cursor.moveToFirst();
							while (!cursor.isAfterLast()) {
								String code = cursor.getString(1);
								if (!mAUFNR.equals(" ") && !code.equals("PZ04")) {
									mMoveMap.put(cursor.getString(0), code);
								}
								cursor.moveToNext();
							}
							mMovePopup = new BaseTextPopup(mContext, mMoveMap,
									MOVE_DEF,
									R.layout.text_array_popup_shot_layout);
							cursor.close();
						}
					}
				}, dbQueryModel);
		dbAsyncTask.execute(DbAsyncTask.DB_SELECT);
		String[] _whereArgs = { "ZDOILTY" };
		String[] _whereCause = { "ZCODEH" };
		dbQueryModel = new DbQueryModel(ConnectController.O_ITAB1_TABLE_NAME,
				_whereCause, _whereArgs, colums);
		dbAsyncTask = new DbAsyncTask("ZDOILTY", mContext,
				new DbAsyncResLintener() {

					@Override
					public void onCompleteDB(String funName, int type,
							Cursor cursor, String tableName) {
						// TODO Auto-generated method stub
						if (funName.equals("ZDOILTY")) {
							// mMoveMap.put("전체", "전체");
							if (cursor == null)
								return;
							cursor.moveToFirst();
							while (!cursor.isAfterLast()) {
								mZDOILTYMap.put(cursor.getString(0),
										cursor.getString(1));
								cursor.moveToNext();
							}
							if (mZDOILTYMap.containsKey("게이지")) {
								mZDOILTYOilType = mZDOILTYMap.get("게이지");
								mBtnZDOILTY.setText("게이지");
							}
							mZDOILTYPopup = new BaseTextPopup(mContext,
									mZDOILTYMap, ZDOILTY_DEF,
									R.layout.text_array_popup_shot_layout);
							cursor.close();
						}
					}
				}, dbQueryModel);
		dbAsyncTask.execute(DbAsyncTask.DB_SELECT);
		String[] _whereArgs3 = { "M035" };
		String[] _whereCause3 = { "ZCODEH" };
		dbQueryModel = new DbQueryModel(ConnectController.O_ITAB1_TABLE_NAME, _whereCause3, _whereArgs3, colums);
		dbAsyncTask = new DbAsyncTask("M035", mContext,
				new DbAsyncResLintener() {

					@Override
					public void onCompleteDB(String funName, int type,
							Cursor cursor, String tableName) {
						// TODO Auto-generated method stub
						if (funName.equals("M035")) {
							// mMoveMap.put("전체", "전체");
							if (cursor == null)
								return;
							cursor.moveToFirst();
							while (!cursor.isAfterLast()) {
								if (mStep.equals("")) {
									mStepMap.put(cursor.getString(0),
											cursor.getString(1));
								} else {
									String step = cursor.getString(0);
									if (mStep.equals("출발")) {
										if (step.equals("출발")) {
											mStepMap.put(cursor.getString(0),
													cursor.getString(1));
										}
									} else {
										if (step.equals("도착")) {
											mStepMap.put(cursor.getString(0),
													cursor.getString(1));
										}
									}
								}
								cursor.moveToNext();
							}
							mStepPopup = new BaseTextPopup(mContext, mStepMap,
									STEP_DEF,
									R.layout.text_array_popup_shot_layout);
							if (!mStep.equals("")) {
								if (mStepMap.containsKey(mStep)) {
									mBtnSTEP.setText(mStep);
									mBtnSTEP.setEnabled(false);
									mStepType = mStepMap.get(mStep);
								}
							}
							cursor.close();
						}
					}
				}, dbQueryModel);
		dbAsyncTask.execute(DbAsyncTask.DB_SELECT);
		// mStepMap.put("출발", "출발");
		// mStepMap.put("도착", "도착");
	}

	private void showFilterPopup(BaseTextPopup popup, View anchor) {

		popup.setOnSelectedItem(this);
		popup.show(anchor, mRootView.getMeasuredWidth(),
				mRootView.getMeasuredHeight());
	}

	@Override
	public void onSelectedItem(int position, String popName) {
		LinkedHashMap<String, String> linkedHashMap = null;
		if (popName.equals(MOVE_DEF)) {
			linkedHashMap = mMoveMap;
			mMoveType = getSeletedItem(linkedHashMap, position, mBtnOTYPE);
		} else if (popName.equals(STEP_DEF)) {
			linkedHashMap = mStepMap;
			mStepType = getSeletedItem(linkedHashMap, position, mBtnSTEP);

			// 2017-08-31. 출발일땐 주유금액 세차비 등 입력 못하도록 수정
			initMiddle_bottom(mStepType);
		} else if (popName.equals(ZDOILTY_DEF)) {
			linkedHashMap = mZDOILTYMap;
			mZDOILTYOilType = getSeletedItem(linkedHashMap, position,
					mBtnZDOILTY);
			String oiltype = mBtnZDOILTY.getText().toString();

			String tmp = mBtnOilCount.getText().toString();
			if (tmp.contains("L") && !oiltype.equals("DFM"))
				mOilCount = "0";
			else if (tmp.contains("칸") && oiltype.equals("DFM"))
				mOilCount = "0";

			if (oiltype.equals("DFM")) {
				mBtnOilCount.setText(mOilCount + " L");
			} else {
				mBtnOilCount.setText(mOilCount + " 칸");
			}
		}
	}

	private void initMiddle_bottom(String mStepType){
		if(mBtnOilInput != null){
			mBtnOilInput.setText("L");
			mBtnOilInput.setEnabled(true);
		}
		if(mBtnOilMoney != null){
			mBtnOilMoney.setText("0 원");
			mBtnOilMoney.setEnabled(true);
		}
		if(mBtnWashCar != null){
			mBtnWashCar.setText("0 원");
			mBtnWashCar.setEnabled(true);
		}
		if(mBtnParking != null){
			mBtnParking.setText("0 원");
			mBtnParking.setEnabled(true);
		}
		if(mBtnToll != null){
			mBtnToll.setText("0 원");
			mBtnToll.setEnabled(true);
		}
		if(mBtnEtc != null){
			mBtnEtc.setText("0 원");
			mBtnEtc.setEnabled(true);
		}
		if(mEtMoneyReason != null){
			mEtMoneyReason.setText("");
			mEtMoneyReason.setEnabled(true);
		}
		if(mTvTotal != null){
			mTvTotal.setText("");
			mTotal = 0;
		}
		if(mStepType != null && mStepType.equals("11")){
			if(mBtnOilInput != null){
				mBtnOilInput.setEnabled(false);
			}
			if(mBtnOilMoney != null){
				mBtnOilMoney.setEnabled(false);
			}
			if(mBtnWashCar != null){
				mBtnWashCar.setEnabled(false);
			}
			if(mBtnParking != null){
				mBtnParking.setEnabled(false);
			}
			if(mBtnToll != null){
				mBtnToll.setEnabled(false);
			}
			if(mBtnEtc != null){
				mBtnEtc.setEnabled(false);
			}
			if(mEtMoneyReason != null){
				mEtMoneyReason.setText("");
				mEtMoneyReason.setEnabled(false);
			}

		}
	}
	private String getSeletedItem(LinkedHashMap<String, String> linkedHashMap,
			int position, Button btn) {
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
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btn_OTYPE:
			clickOTYE();
			break;
		case R.id.btn_OSTEP:
			clickSTEP();
			break;
		case R.id.btn_ZDOILTY:
			clickZDOILTY();
			break;
		case R.id.iv_exit:
			dismiss();
			break;
		case R.id.btn_save:
			clickSave();
			break;
		case R.id.btn_mileage:
			clickMileage();
			break;
		case R.id.btn_oil_count:
			clickOilCount();
			break;
		case R.id.btn_oil_input:
			clickOilInput();
			break;
		case R.id.btn_oil_money:
			clickOilMoney();
			break;
		case R.id.btn_wash_car:
			clickWashCar();
			break;
		case R.id.btn_parking:
			clickParking();
			break;
		case R.id.btn_toll:
			clickToll();
			break;
		case R.id.btn_etc:
			clickEtc();
			break;
		case R.id.btn_DATUM:
			clickDAUTM();
			break;
		case R.id.btn_CarNum:

			String data = mBtnCarNum.getText().toString();
			if (data != null && !data.equals("")) {
				ViewGroup vg1 = pwDelSearch.getViewGroup();
				LinearLayout back = (LinearLayout) vg1.getChildAt(0);
				final Button del = (Button) back.getChildAt(0);
				del.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						mBtnCarNum.setText("");
						mINVNR = "";
						// Log.e("mINVNR", mINVNR);
						pwDelSearch.dismiss();
					}

				});
				final Button sel = (Button) back.getChildAt(1);
				sel.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						// new Camera_Dialog(mContext).show(mBtnCarNum);
						final Camera_Dialog cd = new Camera_Dialog(mContext);
						Button cd_done = (Button) cd
								.findViewById(R.id.camera_done_id);
						final EditText et_carnum = (EditText) cd
								.findViewById(R.id.camera_carnum_id);
						cd_done.setOnClickListener(new View.OnClickListener() {
							@Override
							public void onClick(View arg0) {
								Object data = et_carnum.getText();
								String str = data == null
										|| data.toString().equals("") ? ""
										: data.toString();
								if (str.equals("")) {
									EventPopupC epc = new EventPopupC(mContext);
									epc.show("차량번호를 입력해 주세요.");
								} else {
									mBtnCarNum.setText(str);
									mINVNR = str;
									Log.e("mINVNR", mINVNR);
									cd.dismiss();
								}
							}
						});
						cd.show();

						pwDelSearch.dismiss();
					}
				});
				pwDelSearch.show(mBtnCarNum);
			} else {
				final Camera_Dialog cd = new Camera_Dialog(mContext);
				Button cd_done = (Button) cd.findViewById(R.id.camera_done_id);
				final EditText et_carnum = (EditText) cd
						.findViewById(R.id.camera_carnum_id);
				cd_done.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View arg0) {
						Object data = et_carnum.getText();
						String str = data == null || data.toString().equals("") ? ""
								: data.toString();
						if (str.equals("")) {
							EventPopupC epc = new EventPopupC(mContext);
							epc.show("차량번호를 입력해 주세요.");
						} else {
							mBtnCarNum.setText(str);
							mINVNR = str;
							// Log.e("mINVNR", mINVNR);
							cd.dismiss();
						}
					}
				});
				cd.show();

			}
			break;
		default:
			break;
		}
	}

	private void clickDAUTM() {
		final CalendarPopup cp = new CalendarPopup(mContext, MovementDialog.this);
		cp.show(mBtnDATUM, mRootView.getMeasuredWidth(), mRootView.getMeasuredHeight(), mBtnDATUM.getInventoryLocation());
		Calendar cal = Calendar.getInstance();
		String year = String.format("%04d", cal.get(Calendar.YEAR));
		String month = String.format("%02d", cal.get(Calendar.MONTH) + 1);
		String day = String.format("%02d", cal.get(Calendar.DAY_OF_MONTH));
		String hour = String.format("%02d", cal.get(Calendar.HOUR_OF_DAY));
		String minute = String.format("%02d", cal.get(Calendar.MINUTE));
		cp.setHour(hour);
		cp.setMinute(minute);
//		cp.setOnDismissListener(new CalendarPopup.OnDismissListener() {
//
//			@Override
//			public void onDismiss(String YYMMDD, String hhmm) {
//				Log.i("####", "####" + "디스미스");
//				mYYMMDD = YYMMDD;
//				mHHMM = hhmm + "00";
//			}
//		});
//		
//		ImageView done = cp.getBt_Done();
//		done.setOnClickListener(new View.OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				Log.i("####", "####" + "확인버튼이 눌렸습니다.");
//				cp.getCalv().onDone2();
//				
//			}
//		});
	}

	public void setYYYYMMDDhhmm(String YYMMDD, String hhmm) {
		// Log.i("####", "####" + "저장" + YYMMDD + "/" + hhmm);
		mYYMMDD = YYMMDD;
		mHHMM = hhmm + "00";
	}

	public void setYYYYMMDDhhmm(String YYMMDD, String hhmm, String EE) {
		// Log.i("####", "####" + "저장" + YYMMDD + "/" + hhmm);
		mYYMMDD = YYMMDD;
		mHHMM = hhmm + "00";
		mEE=EE;
	}
	
	private void clickWashCar() {
		InventoryPopup inventoryPopup = new InventoryPopup(mContext,
				QuickAction.HORIZONTAL, R.layout.inventory_popup,
				InventoryPopup.TYPE_MONEY);
		inventoryPopup.show(mBtnWashCar, mRootView.getMeasuredWidth(),
				mRootView.getMeasuredHeight(),
				mBtnWashCar.getInventoryLocation());
		inventoryPopup
				.setOnDismissListener(new InventoryPopup.OnDismissListener() {

					@Override
					public void onDismiss(String result, int position) {
						// TODO Auto-generated method stub
						if (result != null)
							if (result.length() > 0) {
								String selectMileage = result.replaceAll(",",
										"");
								int iResult = Integer.parseInt(selectMileage);
								int iBackScore = Integer.parseInt(mWashCar);
								mWashCar = result.replaceAll(",", "");
								mBtnWashCar.setText(result + " 원");
								mTotal = mTotal + iResult - iBackScore;
								mTvTotal.setText("" + mTotal + " 원");
							}
					}
				});
	}

	private void clickParking() {
		InventoryPopup inventoryPopup = new InventoryPopup(mContext,
				QuickAction.HORIZONTAL, R.layout.inventory_popup,
				InventoryPopup.TYPE_MONEY);
		inventoryPopup.show(mBtnParking, mRootView.getMeasuredWidth(),
				mRootView.getMeasuredHeight(),
				mBtnParking.getInventoryLocation());
		inventoryPopup
				.setOnDismissListener(new InventoryPopup.OnDismissListener() {

					@Override
					public void onDismiss(String result, int position) {
						// TODO Auto-generated method stub
						if (result != null)
							if (result.length() > 0) {
								String selectMileage = result.replaceAll(",",
										"");
								int iResult = Integer.parseInt(selectMileage);
								int iBackScore = Integer.parseInt(mParking);
								mParking = result.replaceAll(",", "");
								mBtnParking.setText(result + " 원");
								mTotal = mTotal + iResult - iBackScore;
								mTvTotal.setText("" + mTotal + " 원");
							}
					}
				});
	}

	private void clickToll() {
		InventoryPopup inventoryPopup = new InventoryPopup(mContext,
				QuickAction.HORIZONTAL, R.layout.inventory_popup,
				InventoryPopup.TYPE_MONEY);
		inventoryPopup.show(mBtnToll, mRootView.getMeasuredWidth(),
				mRootView.getMeasuredHeight(), mBtnToll.getInventoryLocation());
		inventoryPopup
				.setOnDismissListener(new InventoryPopup.OnDismissListener() {

					@Override
					public void onDismiss(String result, int position) {
						// TODO Auto-generated method stub
						if (result != null)
							if (result.length() > 0) {
								String selectMileage = result.replaceAll(",",
										"");
								int iResult = Integer.parseInt(selectMileage);
								int iBackScore = Integer.parseInt(mToll);
								mToll = result.replaceAll(",", "");
								mBtnToll.setText(result + " 원");
								mTotal = mTotal + iResult - iBackScore;
								mTvTotal.setText("" + mTotal + " 원");
							}
					}
				});
	}

	private void clickEtc() {
		InventoryPopup inventoryPopup = new InventoryPopup(mContext,
				QuickAction.HORIZONTAL, R.layout.inventory_popup,
				InventoryPopup.TYPE_MONEY);
		inventoryPopup.show(mBtnEtc, mRootView.getMeasuredWidth(),
				mRootView.getMeasuredHeight(), mBtnEtc.getInventoryLocation());
		inventoryPopup
				.setOnDismissListener(new InventoryPopup.OnDismissListener() {

					@Override
					public void onDismiss(String result, int position) {
						// TODO Auto-generated method stub
						if (result != null)
							if (result.length() > 0) {
								String selectMileage = result.replaceAll(",",
										"");
								int iResult = Integer.parseInt(selectMileage);
								int iBackScore = Integer.parseInt(mEtc);
								mEtc = result.replaceAll(",", "");
								mBtnEtc.setText(result + " 원");
								mTotal = mTotal + iResult - iBackScore;
								mTvTotal.setText("" + mTotal + " 원");
							}
					}
				});
	}

	private void clickOilMoney() {
		InventoryPopup inventoryPopup = new InventoryPopup(mContext,
				QuickAction.HORIZONTAL, R.layout.inventory_popup,
				InventoryPopup.TYPE_MONEY);
		inventoryPopup.show(mBtnOilMoney, mRootView.getMeasuredWidth(),
				mRootView.getMeasuredHeight(),
				mBtnOilMoney.getInventoryLocation());
		inventoryPopup
				.setOnDismissListener(new InventoryPopup.OnDismissListener() {

					@Override
					public void onDismiss(String result, int position) {
						// TODO Auto-generated method stub
						if (result != null)
							if (result.length() > 0) {
								String selectMileage = result.replaceAll(",",
										"");
								double iResult = Double.parseDouble(selectMileage);
								mOilMoney = result.replaceAll(",", "");
								mBtnOilMoney.setText(result + " 원");
								double iOilInput = Double.parseDouble(mOilInput);
								if (iOilInput > 0) {
//									long literMoney = iResult / iOilInput;
//									double literMoney = Double.parseDouble(String.format("%d", iResult / iOilInput));
									long literMoney = Math.round(iResult / iOilInput);
									mEtOilLiter.setText("" + literMoney + " 원");
								}
							}
					}
				});
	}

	private void clickOilInput() {
		// myung 20131126 UPDATE 유류량 입력 > 게이지 선택 시 키패드는 소수점 입력 가능해야 함.
		InventoryPopup_Dot inventoryPopup_dot = new InventoryPopup_Dot(mContext);
		inventoryPopup_dot.show(mBtnOilInput, mRootView.getMeasuredWidth(),
				mRootView.getMeasuredHeight(),
				mBtnOilInput.getInventoryLocation());
		inventoryPopup_dot
				.setOnDismissListener(new InventoryPopup_Dot.OnDismissListener() {

					@Override
					public void onDismiss(String result, int position) {
						// TODO Auto-generated method stub
						if (result != null)
							if (result.length() > 0) {
								String selectMileage = result.replaceAll(",",
										"");
								// myung 20131126 UPDATE 유류량 입력 > 게이지 선택 시 키패드는
								// 소수점 입력 가능해야 함.
								double dResult = Double
										.parseDouble(selectMileage);
								mOilInput = result.replaceAll(",", "");
								// 20171127. hjt. L + Kwh 단위 추가
								mBtnOilInput.setText(result + " L/Kwh");
								double iOilMoney = Double
										.parseDouble(mOilMoney);
								if (iOilMoney > 0) {
//									double literMoney = iOilMoney / dResult;
									long literMoney = Math.round(iOilMoney / dResult);
									mEtOilLiter.setText("" + literMoney + " 원");
								}
							}
					}
				});
	}

	private void clickOilCount() {
		InventoryPopup inventoryPopup = new InventoryPopup(mContext,
				QuickAction.HORIZONTAL, R.layout.inventory_popup,
				InventoryPopup.TYPE_OIL);
		inventoryPopup.show(mBtnOilCount, mRootView.getMeasuredWidth(),
				mRootView.getMeasuredHeight(),
				mBtnOilCount.getInventoryLocation());
		inventoryPopup
				.setOnDismissListener(new InventoryPopup.OnDismissListener() {

					@Override
					public void onDismiss(String result, int position) {
						// TODO Auto-generated method stub
						if (result != null)
							if (result.length() > 0) {
								// String selectMileage = result.replaceAll(",",
								// "");
								// Double iResult =
								// Double.valueOf(selectMileage);
								// Double iMileage = Double.valueOf(mOilCount);
								mOilCount = result.replaceAll(",", "");
								String oiltype = mBtnZDOILTY.getText()
										.toString();

								if (oiltype.equals("DFM")) {
									mBtnOilCount.setText(result + " L");
								} else {
									mBtnOilCount.setText(result + " 칸");
								}
							}
					}
				});
	}

	private void clickMileage() {
		InventoryPopup inventoryPopup = new InventoryPopup(mContext,
				QuickAction.HORIZONTAL, R.layout.inventory_popup,
				InventoryPopup.TYPE_MILEAGE);
		inventoryPopup.show(mBtnMileage, mRootView.getMeasuredWidth(),
				mRootView.getMeasuredHeight(), mInventoryLocation);
		inventoryPopup
				.setOnDismissListener(new InventoryPopup.OnDismissListener() {

					@Override
					public void onDismiss(String result, int position) {
						// TODO Auto-generated method stub
						if (result != null)
							if (result.length() > 0) {
								// String selectMileage = result.replaceAll(",",
								// "");
								// Double iResult =
								// Double.valueOf(selectMileage);
								// Double iMileage = Double.valueOf(mMileage);
								mMileage = result.replaceAll(",", "");
								mBtnMileage.setText(result + " km");
							}
					}
				});
	}

	private void clickSave() {
		if (checkSaveText()) {
			showProgress("차량운행일지를 등록중 입니다.");
			ConnectController connectController = new ConnectController(
					new ConnectInterface() {

						@Override
						public void reDownloadDB(String newVersion) {
							// TODO Auto-generated method stub
						}

						@Override
						public void connectResponse(String FuntionName,
								String resultText, String MTYPE, int resulCode,
								TableModel tableModel) {
							hideProgress();
							if (MTYPE.equals("S")) {
								// Log.i("####", "####" + "여기1");
								showEventPopup2(new OnEventOkListener() {

									@Override
									public void onOk() {
										// TODO Auto-generated method stub
										if (mOnSelectedSave != null) {
											mOnSelectedSave.onSelectedSave(mStep);
											SharedPreferencesUtil shareU = SharedPreferencesUtil
													.getInstance();
											shareU.getBoolean(
													"key_process_start", true);
											if (shareU.getBoolean(
													"key_process_start", true))
												shareU.setBoolean(
														"key_process_start",
														false);
											else
												shareU.setBoolean(
														"key_process_start",
														true);
										}
										dismiss();
									}
								}, resultText);
							} else {
								// Log.i("####", "####" + "여기2");
								showEventPopup2(null, resultText);
							}
						}
					}, mContext);
			String dATUM = mYYMMDD;
			// myun 20131209 UPDATE 날짜오류 수정
			String uZEIT = mHHMM;
			// int time = Integer.parseInt(mHHMM) - 100;
			// String uZEIT = "" + time;
			String vKBUR = mEtVKBUR.getText().toString();
			String mILAG = mMileage;
			String dIVSN = mZDOILTYOilType;
			String gDOIL = mOilCount;
			String pTRAN2 = mEtReason.getText().toString();
			String gDOIL2 = mOilInput;
			String oILPRI = mOilMoney;
			String wASH = mWashCar;
			String pARK = mParking;
			String tOLL = mToll;
			String oTRAMT = mEtc;
			
			
			
			
			String rEASON_TX = mEtMoneyReason.getText().toString();
			// myung 20131218 ADD
			// Log.e("mINVNR", mINVNR);
			// Log.e("mINVNR_FIRST", mINVNR_FIRST);
			if (!mINVNR.equals(mINVNR_FIRST)) {
				mEQUNR = "";
			}
			MovementSaveModel model = new MovementSaveModel(mEQUNR, mSeq,
					mINVNR, mMoveType, mAUFNR, mDRIVER, mDriverName, mStepType,
					dATUM, uZEIT, vKBUR, mILAG, dIVSN, gDOIL, pTRAN2, gDOIL2,
					oILPRI, wASH, pARK, tOLL, oTRAMT, rEASON_TX);
			
			

			// Log.i("####", "####보내는시간" + dATUM + "/" + uZEIT);
			connectController.saveMovement(model);
		}
	}

	private boolean checkSaveText() {
		boolean reCheck = true;
		String otype = mBtnOTYPE.getText().toString();
		// String otypeTag = mBtnOTYPE.getTag().toString();
		String ostep = mBtnSTEP.getText().toString();
		String dATUM = mYYMMDD;
		String uZEIT = mHHMM;
		String vKBUR = mEtVKBUR.getText().toString();
		String mILAG = mMileage;
		String dIVSN = mZDOILTYOilType;
		String gDOIL = mOilCount;
		String pTRAN2 = mEtReason.getText().toString();
		String gDOIL2 = mOilInput;
		String oILPRI = mOilMoney;
		String wASH = mWashCar;
		String pARK = mParking;
		String tOLL = mToll;
		String oTRAMT = mEtc;
		String rEASON_TX = mEtMoneyReason.getText().toString();
		String message = "";
		if (otype.equals("")) {
			message = message + "이동유형을 입력해주세요.";
			reCheck = false;
			// myung 20131218 ADD

		} else if (CarNumEnableFlag && mINVNR.equals("")) {
			message = message + "차량번호를 입력해 주세요.";
			reCheck = false;
		} else if (otype.equals("")) {
			message = message + "운행단계를 입력해주세요.";
			reCheck = false;
		} else if (dATUM.equals("")) {
			message = message + "출발/도착일시를 입력해주세요.";
			reCheck = false;
		} else if (vKBUR.equals("")) {
			message = message + "출발/도착위치를 입력해주세요.";
			reCheck = false;
		} else if (mILAG.equals("0")) {
			message = message + "출발/도착KM를 입력해주세요.";
			reCheck = false;
		} else if (gDOIL.equals("0")) {
			message = message + "유류량을 입력해주세요.";
			reCheck = false;
			// 경유지 이동만 경유 사유필수
		} else if (otype.equals("경유지 이동") && pTRAN2.equals("")) {
			message = message + "경유사유를 입력해주세요.";
			reCheck = false;
		}

		if (!reCheck) {
			showEventPopup2(null, message);
		}

		return reCheck;
	}

	private void clickZDOILTY() {
		// showFilterPopup(mZDOILTYPopup, mBtnZDOILTY, mBtnOilCount);
		showFilterPopup(mZDOILTYPopup, mBtnZDOILTY);
	}

	private void clickOTYE() {
		showFilterPopup(mMovePopup, mBtnOTYPE);
	}

	private void clickSTEP() {
		showFilterPopup(mStepPopup, mBtnSTEP);
	}

	public void showProgress(String message) {
		if (mProgressPopup != null) {
			mProgressPopup.setMessage(message);
			if (mRootView != null) {
				// CommonUtil.showCallStack();
				mRootView.post(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						try {
							if (mProgressPopup != null)
								mProgressPopup.show();
						} catch (BadTokenException e) {
							// TODO: handle exception
						} catch (IllegalStateException e) {
							// TODO: handle exception
						}
					}
				});
			} else {
				mProgressPopup.show();
			}
		}
	}

	public void hideProgress() {
		if (mProgressPopup != null) {
			if (mRootView != null) {
				mRootView.post(new Runnable() {

					@Override
					public void run() {
						if (mProgressPopup != null)
							mProgressPopup.dismiss();
					}
				});
			} else {
				mProgressPopup.hide();
			}
		}
	}
}
