package com.ktrental.fragment;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.telephony.PhoneNumberUtils;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.ktrental.R;
import com.ktrental.cm.connect.ConnectController;
import com.ktrental.cm.connect.Connector.ConnectInterface;
import com.ktrental.cm.db.DbAsyncTask;
import com.ktrental.cm.db.DbAsyncTask.DbAsyncResLintener;
import com.ktrental.cm.util.PrintLog;
import com.ktrental.common.DEFINE;
import com.ktrental.common.KtRentalApplication;
import com.ktrental.dialog.Address_Change_Dialog;
import com.ktrental.dialog.History_Dialog;
import com.ktrental.dialog.Mistery_Shopping_Dialog;
import com.ktrental.fragment.CameraPopupFragment.OnNumberResult;
import com.ktrental.model.CarInfoModel;
import com.ktrental.model.DbQueryModel;
import com.ktrental.model.EtcModel;
import com.ktrental.model.LoginModel;
import com.ktrental.model.MaintenanceGroupModel;
import com.ktrental.model.MaintenanceItemModel;
import com.ktrental.model.MaintenanceResultModel;
import com.ktrental.model.TableModel;
import com.ktrental.popup.EventPopupC;
import com.ktrental.util.CommonUtil;
import com.ktrental.util.LogUtil;
import com.ktrental.util.OnChangeFragmentListener;
import com.ktrental.util.kog;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

public class MaintenanceResultInfoFragment extends BaseFragment
		implements DbAsyncResLintener, OnClickListener, OnItemClickListener, ConnectInterface {

	/**
	 * 선택된 차량 보여줘야 되는 총 정보
	 */
	private CarInfoModel mCarInfoModel;

	private String currentQueryCarInfo = "";

	private String[] maintenace_colums = { DEFINE.INVNR, // 0
			DEFINE.MAKTX, DEFINE.DRIVN, DEFINE.DRV_MOB, DEFINE.PARNR2_TX, DEFINE.MOB_NUMBER2, DEFINE.TIRFR,
			DEFINE.TIRBK, DEFINE.INPML, DEFINE.VBELN, // 9
			DEFINE.BSARK, // 10
			DEFINE.BSARK_TX, DEFINE.PERNR_TX, DEFINE.VKGRP1_TX, DEFINE.VKGRP_TX, DEFINE.INNAM, DEFINE.ENAME,
			DEFINE.USRID, DEFINE.GUEBG2, DEFINE.GUEEN2, // 19
			DEFINE.JOINDT, // 20
			DEFINE.PMFRE_TX, DEFINE.MATMA_TX, DEFINE.CYCMN_TX, DEFINE.NOTIR_TX, DEFINE.LNTMA_TX, DEFINE.EXHIB_TX,
			DEFINE.GENMA_TX, DEFINE.BKTIR, DEFINE.EMCAL_TX, // 29
			DEFINE.SNWTR, // 30
			DEFINE.POST_CODE, DEFINE.CITY, DEFINE.STREET, DEFINE.MDLCD, // 34
			DEFINE.OILTYP, DEFINE.AUFNR, DEFINE.GSTRS, DEFINE.OILTYPNM, DEFINE.EQUNR, DEFINE.CTRTY, // 40
			DEFINE.CEMER, DEFINE.CHNGBN, DEFINE.OWNER, DEFINE.CCMSTS, DEFINE.DRV_TEL, DEFINE.DRV_MOB, // 46
			DEFINE.VOCNUM, DEFINE.KUNNR, DEFINE.DELAY, DEFINE.GUBUN, // 50
			DEFINE.REQNO, DEFINE.ATVYN};

	private TextView mTvCarName, mTvDriverName, mTvAddress, mTvDriverContact, mTvCustomerName, mTvRepresentativeName,
			mTvTourCarnum, mTvTireFront, mTvTireRear, mTvLastMileage, mTvContractNum, mTvContractCategory,
			mTvBusinessManager, mTvPossetionBranch, mTvContractBranch, mTvTrustTerm, mTvTrustTerm2, mTvTrustTerm3,
			mTvCarResistDay, mTvLWD, mTvMaintenanceProducts, mTvTourMainenancePeriod, mTvNomalTire, mTvRentalService,
			mTvNomalMaintenance, mTvCheckMaintenance, mTvTireFunk, mTvEmergencyResponseCount, mTvSnowTire, mTvOilType,
			mTvChain, mTvOwner, mTvLDWSecond, mTvDelay;

	private Button mBtnChangeAddress;
	private Button mBtnCompareCarNumber;

	private OnNumberResult mOnNumberResult;

	// private ListView mLvLast;
	// private ArrayList<MaintenanceLastModel> mLastModels = new
	// ArrayList<MaintenanceLastModel>();
	// private CarInfoLastAdapter mCarInfoLastAdapter;

	private String mProgressStatus;
	private String mGubun;
	private String mImageName = "";
	private String mCustomerName;
	private HashMap<String, String> o_struct1;
	// private HashMap<String, String> o_1060_rd03;

	private ConnectController mConnectController;
	private OnAddressChange mOnAddressChange;
	private ImageView mEmptyView;
	private CameraPopupFragment mCameraPopupFragment;

	private C_CameraPopupFragment mC_CameraPopupFragment;

	private Button mBtnEmergency;
	private OnEmergencyListener mOnEmergencyListener;
	private boolean mEmergencyFlag = false;
	String beforeAddr;

	public interface OnEmergencyListener {
		void onEmergencyListener(boolean emergencyFlag);
	}

	public interface OnAddressChange {
		void onAddresssChange(String carNum);
	}

	public MaintenanceResultInfoFragment() {
		// TODO Auto-generated constructor stub
	}

	@SuppressLint("ValidFragment")
	public MaintenanceResultInfoFragment(String className, OnChangeFragmentListener changeFragmentListener,
										 OnNumberResult onNumberResult, OnAddressChange onAddressChange, OnEmergencyListener onEmergencyListener) {
		super(className, changeFragmentListener);
		mOnNumberResult = onNumberResult;
		mOnAddressChange = onAddressChange;
		mOnEmergencyListener = onEmergencyListener;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		mConnectController = new ConnectController(this, mContext);

		mRootView = inflater.inflate(R.layout.maintenance_result_info_layout, null);

		initTextView();

		mBtnChangeAddress = (Button) mRootView.findViewById(R.id.btn_change_address);
		mBtnChangeAddress.setOnClickListener(this);

		mBtnCompareCarNumber = (Button) mRootView.findViewById(R.id.btn_maintenance_start);
		mBtnEmergency = (Button) mRootView.findViewById(R.id.btn_emergency_start);
		mBtnEmergency.setOnClickListener(this);
		mBtnCompareCarNumber.setOnClickListener(this);

		if (mEmergencyFlag)
			if (mBtnCompareCarNumber != null) {
				mBtnCompareCarNumber.setText("정비 결과등록 내역조회");
				mBtnCompareCarNumber.setVisibility(View.GONE);
				mBtnEmergency.setVisibility(View.VISIBLE);

			}

		initGubun();

		if (mCarInfoModel != null) {
			setData();
		}

		return mRootView;
	}

	private void initTextView() {
		mTvCarName = (TextView) mRootView.findViewById(R.id.tv_car_name);
		mTvDriverName = (TextView) mRootView.findViewById(R.id.tv_driver_name);
		mTvAddress = (TextView) mRootView.findViewById(R.id.tv_address);
		mTvDriverContact = (TextView) mRootView.findViewById(R.id.tv_driver_contact);
		mTvCustomerName = (TextView) mRootView.findViewById(R.id.tv_customer_name);
		mTvRepresentativeName = (TextView) mRootView.findViewById(R.id.tv_representative_name);
		mTvTourCarnum = (TextView) mRootView.findViewById(R.id.tv_tour_carnum);
		mTvTireFront = (TextView) mRootView.findViewById(R.id.tv_tire_front);
		mTvTireRear = (TextView) mRootView.findViewById(R.id.tv_tire_rear);
		mTvLastMileage = (TextView) mRootView.findViewById(R.id.tv_last_mileage);
		mTvContractNum = (TextView) mRootView.findViewById(R.id.tv_contract_num);
		mTvContractCategory = (TextView) mRootView.findViewById(R.id.tv_contract_category);
		mTvBusinessManager = (TextView) mRootView.findViewById(R.id.tv_business_manager);
		mTvPossetionBranch = (TextView) mRootView.findViewById(R.id.tv_possetion_branch);
		mTvContractBranch = (TextView) mRootView.findViewById(R.id.tv_contract_branch);

		mTvTrustTerm = (TextView) mRootView.findViewById(R.id.tv_trust_term);
		mTvTrustTerm2 = (TextView) mRootView.findViewById(R.id.tv_trust_term_2);
		mTvTrustTerm3 = (TextView) mRootView.findViewById(R.id.tv_trust_term_3);
		mTvCarResistDay = (TextView) mRootView.findViewById(R.id.tv_car_resist_day);
		mTvLWD = (TextView) mRootView.findViewById(R.id.tv_ldw);
		mTvMaintenanceProducts = (TextView) mRootView.findViewById(R.id.tv_maintenance_products);
		mTvTourMainenancePeriod = (TextView) mRootView.findViewById(R.id.tv_tour_mainenance_period);

		mTvNomalTire = (TextView) mRootView.findViewById(R.id.tv_nomal_tire);
		mTvRentalService = (TextView) mRootView.findViewById(R.id.tv_rental_service);
		mTvNomalMaintenance = (TextView) mRootView.findViewById(R.id.tv_nomal_maintenance);
		mTvCheckMaintenance = (TextView) mRootView.findViewById(R.id.tv_check_maintenance);
		mTvTireFunk = (TextView) mRootView.findViewById(R.id.tv_tire_funk);
		mTvEmergencyResponseCount = (TextView) mRootView.findViewById(R.id.tv_emergency_response_count);
		mTvSnowTire = (TextView) mRootView.findViewById(R.id.tv_snow_tire);

		mTvOilType = (TextView) mRootView.findViewById(R.id.tv_oil_type);

		mTvChain = (TextView) mRootView.findViewById(R.id.tv_chain);

		mTvOwner = (TextView) mRootView.findViewById(R.id.tv_owner);

		// mLvLast = (ListView) mRootView.findViewById(R.id.lv_last_item);
		// mCarInfoLastAdapter = new CarInfoLastAdapter(mContext);
		// mLvLast.setAdapter(mCarInfoLastAdapter);
		// mLvLast.setOnItemClickListener(this);

		// mRootView.findViewById(R.id.btn_career).setOnClickListener(this);
		mEmptyView = (ImageView) mRootView.findViewById(R.id.iv_empty);
		mTvLDWSecond = (TextView) mRootView.findViewById(R.id.tv_ldw_second);

		//2017-06-02. hjt. 지연일수 추가
		mTvDelay = (TextView) mRootView.findViewById(R.id.tv_delay);
	}

	/**
	 * 보여주어야 하는 차량에 총 정보를 DB 에서 얻어온다. (Async)
	 *
	 * @param carNum
	 * @param carName
	 */
	public void showCarInfo(String _name, String carNum, String carName, String progressStatus, String imageName,
							String gubun) {

		kog.e("Jonathan", "1showCarInfo :: " + _name); // Jonathan 14.06.18

		mGubun = gubun;
		mCustomerName = _name;

		// if (!this.isHidden()) {
		mProgressStatus = progressStatus;
		if (progressStatus.equals("U")) {

		}
		mImageName = imageName;
		currentQueryCarInfo = carNum + carName;

		queryCarInfo(carNum, carName);
	}

	/**
	 * 보여주어야 하는 차량에 총 정보를 DB 에서 얻어온다. (Async)
	 *
	 * @param carNum
	 * @param carName
	 */
	public void showCarInfo(String _name, String carNum, String carName, String progressStatus, String imageName,
							final String aufnr, String gubun) {

		// 로컬에서 데이터 받으면 이리로 온다. Jonathan 14.06.19

		kog.e("Jonathan", "2showCarInfo :: " + _name); // Jonathan 14.06.18

		mGubun = gubun;
		// if (!this.isHidden()) {
		mProgressStatus = progressStatus;
		mImageName = imageName;
		currentQueryCarInfo = carNum + carName;
		mCustomerName = _name;
		if (progressStatus.equals("E0004")) {

			// ResultController resultController = new
			// ResultController(mContext,
			// new OnResultCompleate() {
			//
			// @Override
			// public void onResultComplete() {
			// // TODO Auto-generated method stub
			// queryCarInfo(aufnr);
			// }
			// });
			//
			// resultController.recoveryResult(aufnr);

			queryCarInfo(aufnr);

		} else {

			queryCarInfo(aufnr);
		}
	}

	/**
	 * 보여주어야 하는 차량에 총 정보를 DB 에서 얻어온다. (Async)
	 *
	 */
	public void showCarInfo(String _name, String carNum, String carName, String progressStatus, String imageName,
							final String aufnr, String gubun, CarInfoModel aCarInfoModel) {

		// 서버에서 직접 받는 곳 Jonathan 14.06.19

		kog.e("Jonathan", "3showCarInfo :: " + _name); // Jonathan 14.06.18

		mGubun = gubun;
		// if (!this.isHidden()) {
		mProgressStatus = progressStatus;
		mImageName = imageName;
		currentQueryCarInfo = carNum + carName;
		mCarInfoModel = aCarInfoModel;
		mEmergencyFlag = true;
		mCustomerName = _name;
		// setData();

		// queryLast("11허2224"); // 실제데이터가 적어 테스트용 하드코딩.
		kog.e("Jonathan", "차량정보 조회 받아오는 데이터2");
		queryLast(mCarInfoModel.getCarNum()); // 실제 데이터 쿼리

		mCameraPopupFragment = new CameraPopupFragment(CameraPopupFragment.class.getName(), null,
				mCarInfoModel.getCarNum());
	}

	private void queryCarInfo(String aufnr) {
		showProgress("차량정보를 조회 중입니다.");
		kog.e("Jonathan", "차량정보 조회1");

		String[] _whereArgs = { aufnr };
		String[] _whereCause = { DEFINE.AUFNR };

		String[] colums = maintenace_colums;

		DbQueryModel dbQueryModel = new DbQueryModel(ConnectController.REPAIR_TABLE_NAME, _whereCause, _whereArgs,
				colums);

		DbAsyncTask dbAsyncTask = new DbAsyncTask(currentQueryCarInfo, mContext, this, dbQueryModel);

		dbAsyncTask.execute(DbAsyncTask.DB_SELECT);
	}

	private void queryCarInfo(String carNum, String carName) {
		showProgress("차량정보를 조회 중입니다.");
		kog.e("Jonathan", "차량정보 조회2");

		String[] _whereArgs = { carNum, carName };
		String[] _whereCause = { DEFINE.INVNR, DEFINE.MAKTX };

		String[] colums = maintenace_colums;

		DbQueryModel dbQueryModel = new DbQueryModel(ConnectController.REPAIR_TABLE_NAME, _whereCause, _whereArgs,
				colums);

		DbAsyncTask dbAsyncTask = new DbAsyncTask(currentQueryCarInfo, mContext, this, dbQueryModel);

		dbAsyncTask.execute(DbAsyncTask.DB_SELECT);
	}

	/**
	 * 보여주어야 하는 차량에 총 정보를 DB 에서 얻어온다. (Async)
	 *
	 * @param carNum
	 */
	public void queryLast(String carNum) {

		// if (!this.isHidden()) {

		// showProgress("차량정보를 가져오고 있습니다.");
		kog.e("Jonathan", "차량정보 조회3");

		String[] _whereArgs = { carNum };
		String[] _whereCause = { DEFINE.INVNR };

		String[] colums = null;

		DbQueryModel dbQueryModel = new DbQueryModel(DEFINE.REPAIR_LAST_TABLE_NAME, _whereCause, _whereArgs, colums);

		DbAsyncTask dbAsyncTask = new DbAsyncTask("queryLast", mContext, this, dbQueryModel);

		dbAsyncTask.execute(DbAsyncTask.DB_SELECT);
	}

	@Override
	public void onCompleteDB(String funName, int type, final Cursor cursor, String tableName) {

		hideProgress();

		if (funName.equals(currentQueryCarInfo)) {

			Thread thread = new Thread(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					queryResultCarInfo(cursor);

					mRootView.post(new Runnable() {

						@Override
						public void run() {
							setData();
						}
					});
				}

			});

			thread.start();
		} else if (funName.equals("queryLast")) {
			// Thread thread = new Thread(new Runnable() {
			//
			// @Override
			// public void run() {
			// // TODO Auto-generated method stub
			// queryLastResult(cursor);
			//
			// mRootView.post(new Runnable() {
			//
			// @Override
			// public void run() {
			// // setData();
			// mCarInfoLastAdapter.setData(mLastModels);
			// initMaintenanceEmpty(mCarInfoLastAdapter.getCount());
			// setData();
			// }
			// });
			// }
			//
			// });
			//
			// thread.start();
			//
			// //
			// mConnectController.getZMO_1060_RD03(mCarInfoModel.getCarNum());

			o_struct1 = new HashMap<String, String>();
			o_struct1.put("INVNR", mCarInfoModel.getCarNum());
			o_struct1.put("MAKTX", mCarInfoModel.getCarname());
			o_struct1.put("OILTYPNM", mCarInfoModel.getOILTYPNM());
			o_struct1.put("CUSJUSO", mCarInfoModel.getAddress());

			// 고객명
			o_struct1.put("NAME1", mCarInfoModel.getCUSTOMER_NAME()); // 운전자 명으로
			// 받아옴.
			// NAME1
			// = 운전자
			// 명.
			// 운전자명
			o_struct1.put("DLSM1", mCarInfoModel.getDRIVER_NAME()); // Jonathan
			// 수정
			// 14.06.13

			o_struct1.put("DLST1", mTvDriverContact.getText().toString());
			// o_struct1.put("CONTNM", mCarInfoModel.getName());

			// o_struct1.put("DLST1", mCarInfoModel.getTel());
			o_struct1.put("EQUNR", mCarInfoModel.getEQUNR());
			o_struct1.put("DRV_MOB", mCarInfoModel.getDrv_mob());
			o_struct1.put("DRV_TEL", mCarInfoModel.getDrv_tel());
			// o_struct1.put("DLSM1", mCarInfoModel.getNAME1());

			// /Jonathan 추가 14.06.09
			o_struct1.put("ORT02", mCarInfoModel.getCity()); // 주소
			o_struct1.put("STRAS2", mCarInfoModel.getStreet());// 상세 주소
			o_struct1.put("PSTLZ2", mCarInfoModel.getPostCode()); // 우편번호

			// Jonathan 14.09.11
			o_struct1.put("MDLCD", mCarInfoModel.getMdlcd()); // 긴급정비시 들어가야할 차종
			o_struct1.put("FUELCD", mCarInfoModel.getOilType()); // 긴급정비시 들어가야할
			// 차 유류종류

			o_struct1.put("CCMSTS", mProgressStatus);
			kog.e("KDH", "mProgressStatus = " + mProgressStatus);

			kog.e("Jonathan", "1234 :: 1" + mCarInfoModel.getAddress());
			kog.e("Jonathan", "1234 :: 1" + mCarInfoModel.getPostCode());
			kog.e("Jonathan", "1234 :: 1" + mCarInfoModel.getCity());
			kog.e("Jonathan", "1234 :: 1" + mCarInfoModel.getStreet());
			kog.e("Jonathan", "1234 :: 2" + mCarInfoModel.getAUFNR());
			kog.e("Jonathan", "1234 :: 3" + mCarInfoModel.getBusinessManager());
			kog.e("Jonathan", "1234 :: 4" + mCarInfoModel.getDriverName());
			kog.e("Jonathan", "1234 :: 5" + mCarInfoModel.getImageName());
			kog.e("Jonathan", "1234 :: 6" + mCarInfoModel.getCUSTOMER_NAME());
			kog.e("Jonathan", "1234 :: 7" + mCarInfoModel.getDRIVER_NAME());
			kog.e("Jonathan", "1234 :: 8" + mCarInfoModel.getOWNER());
			kog.e("Jonathan", "1234 :: 9" + mCarInfoModel.getRepresentativeName());
			kog.e("Jonathan", "1234 :: 0" + mCarInfoModel.getTXT30());
			kog.e("Jonathan", "1234 :: 00000 " + mCarInfoModel.getVocNum());

			kog.e("Jonathan", "긴급정비시작 MDLCD ::" + mCarInfoModel.getMdlcd());
			kog.e("Jonathan", "긴급정비시작 FUELCD ::" + mCarInfoModel.getOilType());

			Set<String> set = o_struct1.keySet();
			Iterator<String> it = set.iterator();
			String key;

			while (it.hasNext()) {
				key = it.next();
				kog.e("Jonathan", " o_struct1    key ===" + key + "    value  === " + o_struct1.get(key));
			}

			// kog.e("Jonathan", " 1 : " + mCarInfoModel.getName() + " 2 : " +
			// m+ " 3 : " + mCarInfoModel.getCustomerName() );

			// myung 20131125 ADD

			// Jonathan 추가 14.06.10 (필요 없을것 같아서 다시 주석처리)
			// o_1060_rd03 = new HashMap<String, String>();
			// o_1060_rd03.put("INVNR", mCarInfoModel.getCarNum());
			// o_1060_rd03.put("MAKTX", mCarInfoModel.getCarname());
			// o_1060_rd03.put("OILTYPNM", mCarInfoModel.getOILTYPNM());
			// o_1060_rd03.put("CUSJUSO", mCarInfoModel.getAddress());
			// //고객명
			// o_1060_rd03.put("NAME1", mCarInfoModel.getName());
			// //운전자명
			// o_1060_rd03.put("DLSM1", mCarInfoModel.getName());
			// o_1060_rd03.put("DLST1", mTvDriverContact.getText().toString());
			// o_1060_rd03.put("EQUNR", mCarInfoModel.getEQUNR());
			// o_1060_rd03.put("DRV_MOB", mCarInfoModel.getDrv_mob());
			// o_1060_rd03.put("DRV_TEL", mCarInfoModel.getDrv_tel());
			// o_1060_rd03.put("DLSM1", mCarInfoModel.getName());
			// o_1060_rd03.put("ORT02", mCarInfoModel.getCity()); // 주소
			// o_1060_rd03.put("STRAS2", mCarInfoModel.getStreet());// 상세 주소
			// o_1060_rd03.put("PSTLZ2", mCarInfoModel.getPostCode()); // 우편번호
			// o_1060_rd03.put("CCMSTS", mProgressStatus);

		} else if (funName.equals("updateComplete")) {
			hideProgress();
			mOnAddressChange.onAddresssChange(mCarInfoModel.getCarNum());
		}
	}

	// private void queryLastResult(Cursor cursor) {
	//
	// if (cursor == null)
	// return;
	//
	// cursor.moveToFirst();
	//
	// MaintenanceLastModel model = null;
	//
	// while (!cursor.isAfterLast()) {
	//
	// model = new MaintenanceLastModel(cursor.getString(0),
	// cursor.getString(1), cursor.getString(2),
	// cursor.getString(3), cursor.getString(4),
	// cursor.getString(5));
	//
	// mLastModels.add(model);
	//
	// cursor.moveToNext();
	//
	// }
	// cursor.close();
	//
	// }

	private void queryResultCarInfo(Cursor cursor) {

		if (cursor == null)
			return;

		cursor.moveToFirst();

		ArrayList<String> array = new ArrayList<String>();

		while (!cursor.isAfterLast()) {

			for (int i = 0; i < cursor.getColumnCount(); i++) {

				String str = decrypt(maintenace_colums[i], cursor.getString(i));

				array.add(str);

			}

			for (int j = 0; j < array.size(); j++) // Jonathan 14.06.19
			{
				kog.e("Jonathan", "array[" + j + "]" + array.get(j));
			}

			String invnr = KtRentalApplication.getLoginModel().getInvnr();

			String delay = null;
			LogUtil.d("hjt", "hjt delay size = " + array.size());
			if(array.size() > 49){
				delay = array.get(49);
				LogUtil.d("hjt", "hjt delay = " + delay);
			}

			mCarInfoModel = new CarInfoModel(
					// Jonathan 14.06.19
					mCustomerName, // 이부분 추가함. 고객명이 들어가야 함.
					array.get(2), array.get(0), array.get(31) + array.get(32) + array.get(33), array.get(3), "시간",
					array.get(1), array.get(44), array.get(37), array.get(4), array.get(5), invnr, array.get(6),
					array.get(7), array.get(8), array.get(9),
					// myung 20131209 UPDATE 순회정비결과등록 > 차량기본정보 계약구분 값에 BSARK_TX
					// 필드만 입력
					// array.get(11) + "/" + array.get(11), array.get(12),
					array.get(11), array.get(12),

					array.get(13), array.get(14), array.get(15) + " / " + array.get(16), array.get(17), array.get(18),
					array.get(20), array.get(21), array.get(22), array.get(23), array.get(24), array.get(25),
					array.get(26), array.get(27), array.get(28), array.get(29), array.get(30), array.get(34),
					array.get(35), array.get(36), array.get(19), array.get(38), array.get(39), array.get(40),
					array.get(41), array.get(42), array.get(43), array.get(31), array.get(32), array.get(33),
					array.get(45), array.get(46), "", "", array.get(47), array.get(48), delay, array.get(9), array.get(50), array.get(51), array.get(52));
			mCarInfoModel.setGUBUN(mGubun);
			mCarInfoModel.setImageName(mCarInfoModel.getAUFNR() + mImageName);
			cursor.moveToNext();

		}
		cursor.close();

		// queryLast("11허2224"); // 실제데이터가 적어 테스트용 하드코딩.
		kog.e("Jonathan", "차량정보 조회 받아오는 데이터1");
		queryLast(mCarInfoModel.getCarNum()); // 실제 데이터 쿼리

		mCameraPopupFragment = new CameraPopupFragment(CameraPopupFragment.class.getName(), null,
				mCarInfoModel.getCarNum());
		//
	}

	private void setData() {
		if (mCarInfoModel != null) {

			kog.e("Jonathan", "setdata() :: 1" + mCarInfoModel.getAddress());
			kog.e("Jonathan", "setdata() :: 1" + mCarInfoModel.getPostCode());
			kog.e("Jonathan", "setdata() :: 1" + mCarInfoModel.getCity());
			kog.e("Jonathan", "setdata() :: 1" + mCarInfoModel.getStreet());
			kog.e("Jonathan", "setdata() :: 2" + mCarInfoModel.getAUFNR());
			kog.e("Jonathan", "setdata() :: 3" + mCarInfoModel.getBusinessManager());
			kog.e("Jonathan", "setdata() :: 4" + mCarInfoModel.getDriverName());
			kog.e("Jonathan", "setdata() :: 5" + mCarInfoModel.getImageName());
			kog.e("Jonathan", "setdata() :: 6" + mCarInfoModel.getCUSTOMER_NAME());
			kog.e("Jonathan", "setdata() :: 7" + mCarInfoModel.getDRIVER_NAME());
			kog.e("Jonathan", "setdata() :: 8" + mCarInfoModel.getOWNER());
			kog.e("Jonathan", "setdata() :: 9" + mCarInfoModel.getRepresentativeName());
			kog.e("Jonathan", "setdata() :: 0" + mCarInfoModel.getTXT30());
			kog.e("Jonathan", "setdata() :: getcustomername" + mCarInfoModel.getCustomerName());

			// mTvCarNum.setText(mTvCarNum.getText() + " "
			// + mCarInfoModel.getCarNum());
			mTvCarName.setText(mCarInfoModel.getCarname());
			mTvDriverName.setText(mCarInfoModel.getDRIVER_NAME());

			// 2017-06-08. 지연일수 0이면 안보이도록
			if(mCarInfoModel != null) {
				if(mCarInfoModel.getDelay() != null && !mCarInfoModel.getDelay().equals("0")) {
					mTvDelay.setText("지연일수 : " + mCarInfoModel.getDelay());
					mTvDelay.setVisibility(View.VISIBLE);
				} else {
					mTvDelay.setVisibility(View.GONE);
				}
				LogUtil.e("hjt", "hjt dealy 지연일수 = " + mCarInfoModel.getDelay());
			}
//			mTvDelay.setText("지연일수 : " + mCarInfoModel.getDelay());

			String address = mCarInfoModel.getAddress();

			String postCode = mCarInfoModel.getPostCode();
			String city = mCarInfoModel.getCity();
			String street = mCarInfoModel.getStreet();

			// Jonathan 괄호 고침.(postCode 때문)
			mTvAddress.setText("[" + postCode + "]" + city + street);
			beforeAddr = mTvAddress.getText().toString();

			// myung 20131125 UPDATE (DRV_MOB 필드가 없을 경우 DRV_TEL 확인하여 값이 있으면
			// DRV_TEL 입력)
			String phoneNumber = PhoneNumberUtils.formatNumber("" + mCarInfoModel.getDrv_mob());
			// Log.i("phoneNumber", phoneNumber);
			if (phoneNumber.equals(""))
				phoneNumber = PhoneNumberUtils.formatNumber("" + mCarInfoModel.getDrv_tel());
			mTvDriverContact.setText(phoneNumber);
			mTvCustomerName.setText(mCarInfoModel.getCustomerName());
			mTvRepresentativeName.setText(mCarInfoModel.getRepresentativeName());
			mTvTourCarnum.setText(mCarInfoModel.getTourCarNum());
			mTvTireFront.setText(mCarInfoModel.getTireFront());
			mTvTireRear.setText(mCarInfoModel.getTireRear());
			mTvLastMileage.setText(CommonUtil.currentpoint(mCarInfoModel.getLastMileage()) + " km");
			mTvContractNum.setText(mCarInfoModel.getContractNum());
			mTvContractCategory.setText(mCarInfoModel.getContractCategory());
			mTvBusinessManager.setText(mCarInfoModel.getBusinessManager());
			mTvPossetionBranch.setText(mCarInfoModel.getPossetionBranch());
			mTvContractBranch.setText(mCarInfoModel.getContractBranch());
			mTvTrustTerm.setText(CommonUtil.setDotDate(mCarInfoModel.getTrustTerm()));
			mTvCarResistDay.setText(CommonUtil.setDotDate(mCarInfoModel.getCarResistDay()));
			mTvLWD.setText(mCarInfoModel.getLDW());
			if (mCarInfoModel.getLDW().equals(" ")) {
				mTvLDWSecond.setText("미가입");
			} else {
				mTvLDWSecond.setText("가입");
			}
			mTvMaintenanceProducts.setText(mCarInfoModel.getMaintenanceProducts());
			mTvTourMainenancePeriod.setText(mCarInfoModel.getTourMainenancePeriod());
			mTvNomalTire.setText(mCarInfoModel.getNomalTire());
			mTvRentalService.setText(mCarInfoModel.getRentalService());
			mTvNomalMaintenance.setText(mCarInfoModel.getNomalMaintenance());
			mTvCheckMaintenance.setText(mCarInfoModel.getCheckMaintenance());

			// myung UPDATE 타이어 펑크, 스노우 타이어, 체인 값 변경 필요. N -> 미가입, Y-> 가입으로 변경
			// 필요.
			String tempYN = "";
			if (mCarInfoModel.getTireFunk().equals("Y"))
				tempYN = "가입";
			else if (mCarInfoModel.getTireFunk().equals("N"))
				tempYN = "미가입";
			mTvTireFunk.setText(tempYN);
			mTvEmergencyResponseCount.setText(mCarInfoModel.getEmergencyResponseCount());

			tempYN = "";
			kog.e("Jonathan", "snwtir :: " + mCarInfoModel.getSnowTire());
			if (mCarInfoModel.getSnowTire().equals("N"))
				tempYN = "미가입";
			else
			{
				tempYN = "가입 (" + mCarInfoModel.getSnowTire().toString() + ")";
			}
			mTvSnowTire.setText(tempYN);


			mTvOilType.setText(mCarInfoModel.getOILTYPNM());

			mTvTrustTerm2.setText(CommonUtil.setDotDate(mCarInfoModel.getTrustTerm2()));

			tempYN = "";
			if (mCarInfoModel.getChain().equals("Y"))
				tempYN = "가입";
			else if (mCarInfoModel.getChain().equals("N"))
				tempYN = "미가입";
			mTvChain.setText(tempYN);

			mTvOwner.setText(mCarInfoModel.getOWNER());

			try {

				int difference = 0;

				// int year1 = date.getYear();
				int year = Integer.parseInt(mCarInfoModel.getTrustTerm().substring(0, 4));
				int year2 = Integer.parseInt(mCarInfoModel.getTrustTerm2().substring(0, 4));

				int month = Integer.parseInt(mCarInfoModel.getTrustTerm().substring(4, 5));
				int month2 = Integer.parseInt(mCarInfoModel.getTrustTerm2().substring(4, 5));

				int yearDifference = year2 - year;

				int monthDifference = month2 - month;

				difference = (yearDifference * 12) + monthDifference;

				mTvTrustTerm3.setText("" + difference + " 개월");

			} catch (NumberFormatException exception) {

			}
			if (mProgressStatus.equals("E0004")) {
				// myung 20131216 UPDATE "U"의미 없음 " "으로 변경
				// if (mCarInfoModel.getGUBUN().equals("U")) {
				if (mCarInfoModel.getGUBUN().equals(" ")) {
					if (!mCarInfoModel.getDay().equals(CommonUtil.getCurrentDay())) {
						// myung 20131226 DELETE 과거일짜에서도 정비 결과등록 내욕조회가 가능하도록...
						// mBtnCompareCarNumber.setEnabled(false);
					}
				}
			} else {
				if (!mCarInfoModel.getDay().equals(CommonUtil.getCurrentDay())) {
					mBtnCompareCarNumber.setEnabled(false);
				}
			}
		}
	}

	// Jonathan 추가 14.06.10 (서버에서 받아오는 것인가 , 로컬데이터를 받아오는 것인가를 판별하려고 썼음.)
	// boolean IS_SERARCH = true;

	private void goTestAddressDialog() {
		// if(o_struct1.get("").equals("")|| o_struct1.get("").equals(" ")){
		// EventPopupC epc_address = new EventPopupC(mContext);
		// epc_address.show("주소가 존재하지 않습니다.");
		// return;
		// }

		Set<String> set = o_struct1.keySet();
		Iterator<String> it = set.iterator();
		String key;

		while (it.hasNext()) {
			key = it.next();
			kog.e("Jonathan", "key 222222 " + key + "    value  === " + o_struct1.get(key));
		}

		final Address_Change_Dialog acd = new Address_Change_Dialog(mContext, o_struct1);

		kog.e("Jonathan", "주소 +++++=====" + o_struct1.get("ORT02"));
		Button bt_save = (Button) acd.findViewById(R.id.address_change_save_id);
		bt_save.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// LoginModel lm = KtRentalApplication.getLoginModel();
				// LoginModel에서 INGRP 가져와야함 추가중.
				if (acd.getCity1() == null || (acd.getCity1() == "")) {
					EventPopupC epc = new EventPopupC(mContext);
					epc.show("주소를 선택해 주세요");
					return;
				}
				showProgress("");
				// myung 20131211 UPDATE 첫번째 param "120"을 로그인 시 받아온 INGRP정보로 세팅
				// mConnectController.setZMO_1040_WR02("120", getTable(acd));
				mConnectController.setZMO_1040_WR02(getTable(acd));

				CCMSTS = acd.getCCMSTS();

				acd.dismiss();
			}
		});
		acd.show();

	}

	String CCMSTS = "";

	private ArrayList<HashMap<String, String>> getTable(Address_Change_Dialog acd) {
		LoginModel lm = KtRentalApplication.getLoginModel();
		ArrayList<HashMap<String, String>> i_itab1 = new ArrayList<HashMap<String, String>>();
		for (int i = 0; i < 1; i++) {

			HashMap<String, String> hm = new HashMap<String, String>();
			hm.put("EQUNR", o_struct1.get("EQUNR"));// 고객차량 설비번호
			hm.put("PREEQU", lm.getEqunr()); // 이전 순회차량 설비번호
			hm.put("POS_POST", acd.getPost()); // 변경후 주소(우편번호) 입력
			hm.put("POS_CITY1", acd.getCity1()); // 변경후 주소(시도명)
			hm.put("POS_STREET", acd.getStreet()); // 변경후 주소(상세주소)
			hm.put("POS_DRIVN", acd.getDrivn()); // 변경후 운전자 이름
			hm.put("POS_TEL_NO", acd.getTel_No()); // 변경후 운전자연락처
			hm.put("POSEQU", acd.getEqunr()); // 이후 순회차량 설비번호
			hm.put("POSINGRP", acd.getMot()); // 변경후 MOT
			hm.put("PRE_POST", acd.getPrePost());
			hm.put("PRE_CITY1", acd.getPreCity());
			hm.put("PRE_STREET", acd.getPreStreet());
			hm.put("PRE_DRIVN", acd.getPreDrivn());
			hm.put("PRE_TEL_NO", acd.getPreTelNo());
			// hm.put("CCMSTS", acd.getCCMSTS()); // Jonathan 추가

			kog.e("Jonathan", "변경 주소 : " + acd.getCity1());

			i_itab1.add(hm);
		}

		return i_itab1;
	}

	private ArrayList<HashMap<String, String>> getTable(Address_Change_Dialog acd, String equnr)
	{
		LoginModel lm = KtRentalApplication.getLoginModel();
		ArrayList<HashMap<String, String>> i_itab1 = new ArrayList<HashMap<String, String>>();
		for (int i = 0; i < 1; i++)
		{
			HashMap<String, String> hm = new HashMap<String, String>();
			// hm.put("INVNR", acd.getINVNR());// 고객차량 번호
			hm.put("EQUNR", equnr);// 고객차량 설비번호
			hm.put("PREEQU", lm.getEqunr()); // 이전 순회차량 설비번호
			hm.put("POS_POST", acd.getPost()); // 변경후 주소(우편번호) 입력
			hm.put("POS_CITY1", acd.getCity1()); // 변경후 주소(시도명)
			hm.put("POS_STREET", acd.getStreet()); // 변경후 주소(상세주소)
			hm.put("POS_DRIVN", acd.getDrivn()); // 변경후 운전자 이름
			hm.put("POS_TEL_NO", acd.getTel_No()); // 변경후 운전자연락처
			hm.put("POSEQU", acd.getEqunr()); // 이후 순회차량 설비번호
			hm.put("POSINGRP", acd.getMot()); // 변경후 MOT
			hm.put("PRE_POST", acd.getPrePost());
			hm.put("PRE_CITY1", acd.getPreCity());
			hm.put("PRE_STREET", acd.getPreStreet());
			hm.put("PRE_DRIVN", acd.getPreDrivn());
			hm.put("PRE_TEL_NO", acd.getPreTelNo());
			// hm.put("CCMSTS", acd.getCCMSTS()); // Jonathan 추가

			i_itab1.add(hm);
		}
		return i_itab1;
	}

	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		switch (view.getId()) {
			case R.id.btn_change_address: // 소재지 변경
				if (isNetwork())
					goTestAddressDialog();
				break;

			case R.id.btn_maintenance_start: //
				Mistery_Shopping_Dialog dlg = new Mistery_Shopping_Dialog(mContext, "20");
				dlg.setOnDismissListener(new Dialog.OnDismissListener() {
					@Override
					public void onDismiss(DialogInterface dialog) {
						mOnEmergencyListener.onEmergencyListener(false);
						clickCarCompare();
					}
				});
				dlg.show();
//				mOnEmergencyListener.onEmergencyListener(false);
//				clickCarCompare();
				break;
			// case R.id.btn_career:
			// if (isNetwork())
			// clickCareer();
			// break;
			case R.id.btn_emergency_start:
				dlg = new Mistery_Shopping_Dialog(mContext, "20");
				dlg.setOnDismissListener(new Dialog.OnDismissListener() {
					@Override
					public void onDismiss(DialogInterface dialog) {
						mCarInfoModel.setGUBUN("E");
						mOnEmergencyListener.onEmergencyListener(true);
						clickCarCompare();
					}
				});
				dlg.show();
//				mCarInfoModel.setGUBUN("E");
//				mOnEmergencyListener.onEmergencyListener(true);
//				clickCarCompare();
				break;

			default:
				break;
		}
	}

	private void clickCareer() {
		History_Dialog hd = new History_Dialog(mContext, o_struct1, 0);
		hd.show();

	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		getFragmentManager().executePendingTransactions();
	}

	@Override
	public void onHiddenChanged(boolean flag) {
		// TODO Auto-generated method stub
		// getFragmentManager().executePendingTransactions();
		if (!flag)
			Log.e("차량기본정보 오더번호", mCarInfoModel.getAUFNR());

		super.onHiddenChanged(flag);
	}

	private void clickCarCompare() {
		if (mCarInfoModel != null) {

			if (mProgressStatus.equals("E0004")) {
				if (mCarInfoModel.getGUBUN().equals(" ")) {
					getResult();
					kog.e("Jonathan", "Jonathan  getresult");

				} else {
					kog.e("Jonathan", "Jonathan not ");
					showCameraPopup();
				}

			} else {
				kog.e("Jonathan", "Jonathan not E004");
				showCameraPopup();

				Set<String> set = o_struct1.keySet();
				Iterator<String> it = set.iterator();
				String key;

				while (it.hasNext()) {
					key = it.next();
					kog.e("Jonathan", "MATNR  위한 곳 key :: " + key + "    value  === " + o_struct1.get(key));
				}

			}
		}
	}

	// myung 20140103 ADD TEST 사인이미지 확인

	private void SaveBitmapToFileCache(Bitmap bitmap, String strFileName) {

		File file = new File("mnt/sdcard/" + strFileName);
		OutputStream outputStream = null;
		try {
			file.createNewFile();
			outputStream = new FileOutputStream(file);
			bitmap.compress(CompressFormat.JPEG, 100, outputStream);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				outputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	private void getResult() {

		ConnectController connectController = new ConnectController(new ConnectInterface() {

			@Override
			public void reDownloadDB(String newVersion) {
			}

			@Override
			public void connectResponse(String FuntionName, String resultText, String MTYPE, int resulCode,
										TableModel tableModel) {
				// TODO Auto-generated method stub
				hideProgress();
				if (MTYPE.equals("S")) {
					ArrayList<HashMap<String, String>> array = tableModel.getTableArray("O_ITAB1");

					// myung 20140103 ADD TEST 사인이미지 확인

					ArrayList<HashMap<String, String>> arraySign = tableModel.getTableArray("O_ITAB2");
					Log.e("O_ITAB2 Size", "" + tableModel.getTableArray("O_ITAB2").size());

					for (int i = 0; i < tableModel.getTableArray("O_ITAB2").size(); i++) {
						HashMap<String, String> arraySign1 = tableModel.getTableArray("O_ITAB2").get(i);
						String tempSignT = "O_ITAB2" + "_" + i + "_" + arraySign1.get("SIGN_T");
						String tempSign = arraySign1.get("SIGN");
						Log.e("RECEIVE SIGN SIZE", "" + tempSign.length());
						byte[] signBytes = Base64.decode(tempSign, Base64.NO_WRAP);

						Bitmap SignBMP = BitmapFactory.decodeByteArray(signBytes, 0, signBytes.length);

						if (SignBMP == null) {
							Log.e("SignBMP", "SignBMP is NULL");
							break;
						}
						Log.e("SignBMP ByteCount", "" + SignBMP.getByteCount());
						SaveBitmapToFileCache(SignBMP, tempSignT);
					}

					ArrayList<MaintenanceItemModel> mLastItemModels = new ArrayList<MaintenanceItemModel>();
					for (HashMap<String, String> hashMap : array) {

						String stockStr = hashMap.get("LABST");
						stockStr = stockStr.replaceAll(" ", "");

						// 2013.12.08 ypkim
						// exception handling for stockStr variable's
						// length is zero
						int stock = 0;

						if (!stockStr.equals("")) {
							stock = Integer.parseInt(stockStr);
						}

						String MAKTX = hashMap.get("MAKTX");
						String MATNR = hashMap.get("MATNR");
						String ERFME = hashMap.get("ERFME");
						String MATKL = hashMap.get("MATKL");
						String GRP_CD = hashMap.get("GRP_CD");

						// String MDLCD = hashMap.get("MDLCD");
						// String FUELCD = hashMap.get("FUELCD");

						String selectConsumptionStr = hashMap.get("ERFMG");
						selectConsumptionStr = selectConsumptionStr.replaceAll(" ", "");

						int selectConsumption = 0;

						// 2013.12.08 ypkim
						// exception handling for stockStr variable's
						// length is zero
						if (!selectConsumptionStr.equals("")) {
							selectConsumption = Integer.parseInt(selectConsumptionStr);
						}

						// 2013.12.08 ypkim
						// 재고 수량 보정.
						// MaintenancLastItemAdapter 에서 stock -
						// selectConsumption 를 실행 하므로 재고가
						// selectConsumption 만큼 모자람.
						// stock 에 selectConsumption 를 더해서 보정 처리 함.
						// myung20131226 DELETE
						// stock += selectConsumption;

						MaintenanceGroupModel groupModel = new MaintenanceGroupModel("", MATKL);
						MaintenanceItemModel model = new MaintenanceItemModel(MAKTX, stock, MATNR, ERFME, groupModel,
								GRP_CD, null, null, null
								// ,FUELCD
								// ,MAKTX
						);

						PrintLog.Print("============= connectResponse ============", "FuntionName = " + FuntionName
								+ ", STOCK = " + stock + ", Consumption = " + selectConsumption);

						kog.e("Jonathan", "Jonathan MATNR :: " + MATNR);

						// model.MDLCD = MDLCD;
						// model.FUELCD = FUELCD;

						model.MAKTX = MAKTX;

						model.setConsumption(selectConsumption);
						mLastItemModels.add(model);
					}

					HashMap<String, String> map = tableModel.getStruct("O_STRUCT1");

					CarInfoModel carInfoModel = mCarInfoModel;

					String iSDZ = map.get("ISDZ");
					String iEDZ = map.get("IEDZ");
					String CCMBI = map.get("CCMBI");
					String CCMRQ = map.get("CCMRQ");
					String CCMSL = map.get("CCMSL");
					String INPML = map.get("INPML");
					String ISDD = map.get("ISDD");
					String IEDD = map.get("IEDD");

					EtcModel etcModel = new EtcModel(map.get("CEMRS"), iSDZ, iEDZ, CCMBI, CCMRQ, CCMSL, INPML, ISDD,
							IEDD);

					MaintenanceResultModel model = new MaintenanceResultModel(mLastItemModels, etcModel, carInfoModel);

					queryGroup(model);


					mConnectController.duplicateLogin(mContext);

				}
			}
		}, mContext);
		showProgress("정비 결과등록 내역을 조회중입니다.");
		connectController.getResult(mCarInfoModel.getAUFNR(), mCarInfoModel.getCEMER());

	}

	private int count = 0;

	@SuppressLint("NewApi")
	private void showCameraPopup() {
		DEFINE.MATCHING_CAR_NUMBER = 0;
		if (mCameraPopupFragment == null)
			// myung 20131209 UPDATE 차량번호 인식 팝업의 타이틀 변경 (도착등록 -> 차량확인 으로 변경)
			mCameraPopupFragment = new CameraPopupFragment(CameraPopupFragment.class.getName(), null,
					mCarInfoModel.getCarNum());

		if (!mCameraPopupFragment.isAdded()) {
			if (!mCameraPopupFragment.isHidden()) {
				if (!mCameraPopupFragment.isRemoving()) {

					try {
						mOnNumberResult.onResult(true, null);
//
//						mCameraPopupFragment.setCarNumber(mCarInfoModel.getCarNum());
//						mCameraPopupFragment.setOnNumberResult(mOnNumberResult);
//
//						mCameraPopupFragment.setTitle("차량확인");
//
//						// 현재 디스플레이 크기로 변경
//						Display display = getActivity().getWindowManager().getDefaultDisplay();
//						Point size = new Point();
//						display.getSize(size);
//						int width = size.x;
//						int height = size.y;
//
//						// myung 20131120 ADD 차량인식 창 팝업 2560 대응
//						// int tempX = getView().getWidth();//1280;
//						// int tempY = getView().getHeight();//752;
//
//						// if (DEFINE.DISPLAY.equals("2560"))
//						// {
//						// tempX *= 2;
//						// tempY *= 2;
//						// }
//
//						// if(tempX < tempY) {
//						// int temp = tempX;
//						// tempX = tempY;
//						// tempY = temp;
//						// }
//
//						mCameraPopupFragment.show(getChildFragmentManager(), "" + count, width, height);
//						// mCameraPopupFragment.show(getChildFragmentManager(),
//						// "" + count);
//						count++;
//						getFragmentManager().executePendingTransactions();
					} catch (IllegalStateException exception) {

					}

				}
			}
		}
	}

	public CarInfoModel getCarInfoModel() {
		return mCarInfoModel;
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		// switch (arg0.getId()) {
		// case R.id.lv_last_item:
		// MaintenanceDetailFragment detailFragment = new
		// MaintenanceDetailFragment(
		// mLastModels.get(arg2), mCarInfoModel);
		// detailFragment.show(getChildFragmentManager(), null, 1060, 744);
		// break;
		//
		// default:
		// break;
		// }
	}

	@Override
	public void connectResponse(String FuntionName, String resultText, String MTYPE, int resulCode,
								TableModel tableModel) {
		hideProgress();
		if (FuntionName.equals("ZMO_1060_RD03")) {
			o_struct1 = tableModel.getStruct("O_STRUCT1");

			// o_1060_rd03 = tableModel.getStruct("O_STRUCT1");
			//
			// Set <String> set = o_1060_rd03.keySet();
			// Iterator <String> it = set.iterator();
			// String key;
			//
			// while(it.hasNext())
			// {
			// key = it.next();
			// kog.e("Jonathan", " o_1060_rd03 key ===" + key +
			// " value === " + o_1060_rd03.get(key));
			// }
			//
			mConnectController.duplicateLogin(mContext);


		} else if (FuntionName.equals("ZMO_1040_WR02")) {

			kog.e("KDH", "ZMO_1040_WR02 MTYPE = " + MTYPE);

			if (MTYPE.equals("S")) {
				if (tableModel.getTableArray() != null) {
					ArrayList<HashMap<String, String>> array = tableModel.getTableArray();
					for (HashMap<String, String> hashMap : array) {
						String postCode = hashMap.get("POS_POST");
						String city = hashMap.get("POS_CITY1");
						String street = hashMap.get("POS_STREET");
						String POS_TEL_NO = hashMap.get("POS_TEL_NO");
						String POS_DRIVN = hashMap.get("POS_DRIVN");// Jonathan
						// 추가
						// 14.06.13
						// String CCMSTS = hashMap.get("CCMSTS"); // Jonathan 추가

						// 05-29 17:29:47.755: E/KDH(18121): CCMSTS : E0002
						// 05-29 17:29:49.820: E/KDH(18121): CCMSTS = null

						kog.e("KDH", "POS_TEL_NO = " + POS_TEL_NO);
						kog.e("Jonathan", "POS_DRIVN =+=+= " + POS_DRIVN);
						kog.e("Jonathan", "postcode =+=+= " + POS_DRIVN);
						kog.e("Jonathan", "city =+=+= " + POS_DRIVN);
						kog.e("Jonathan", "street =+=+= " + POS_DRIVN);

						mCarInfoModel.setAddress(postCode + city + street);

						// Jonathan 괄호 고침.
						// if (postCode.length() > 5) {
						// StringBuffer sb = new StringBuffer(postCode);
						// sb.insert(postCode.length(), "]");
						// postCode = "[" + sb;
						// }

						//
						// StringBuffer sb = new StringBuffer(
						// mCarInfoModel.getAddress());
						// sb.insert(7, "]");

						// Jonathan 괄호 고침.(postCode 때문)
						mTvAddress.setText("[" + postCode + "]" + city + street);

						// 2014-04-29 KDH현재주소가 변경된경우에만 이관으로한다.
						// 이관요청으로 값을 변경. (DB에서 변경 E0003 으로)
						// 주소 또한 변경.

						Log.e("Jonathan", "beforeAddr =====" + beforeAddr);
						Log.e("Jonathan", "mTvAddress =====" + mTvAddress.getText().toString());

						// Jonathan 추가 14.06.10
						// if(IS_SERARCH) //받아오는 데이터를 바꾼거라면
						// {
						// //로컬에도 있는 차량인가? 로컬도 업데이트 해줘야지~
						// if(o_1060_rd03.get("EQUNR").equals(o_struct1.get("EQUNR")))
						// //여기 불안함.
						// {
						//
						// kog.e("Jonathan", "받아온거 바꾸고 로컬도 바꾸려고...");
						//
						// if(!beforeAddr.equals(mTvAddress.getText().toString()))
						// {
						// kog.e("Jonathan", "주소가 바뀌었어...");
						// updateComplete( postCode, city, street, POS_TEL_NO);
						// }
						// else
						// {
						// //2014-05-21 KDH 여기다가 내가 할려다가말었네 개짜증나서=-_-=;
						// //여기서 리쿼리해야함..아놔
						// kog.e("Jonathan", "주소가 안바뀌었어...");
						// updateCompleteTelCh(CCMSTS, postCode, city, street,
						// POS_TEL_NO );
						// }
						// }
						// else// 로컬엔 없는 차량인가? 뭐 안해도 됨
						// {
						//
						// }
						//
						// }
						// else// 로컬 데이터를 바꾼거라면
						// {
						// 업데이트 해줘.
						// }

						if (!beforeAddr.equals(mTvAddress.getText().toString())) {
							kog.e("Jonathan", "여기 등어오냐? Jonathan.1");
							updateComplete(postCode, city, street, POS_TEL_NO, POS_DRIVN);

						} else {
							// 2014-05-21 KDH 여기다가 내가 할려다가말었네 개짜증나서=-_-=;
							// 여기서 리쿼리해야함..아놔
							kog.e("Jonathan", "여기 등어오냐? Jonathan.2");
							updateCompleteTelCh(CCMSTS, postCode, city, street, POS_TEL_NO, POS_DRIVN);

						}

						// if(!beforeAddr.equals(mTvAddress.getText().toString()))
						// {
						// updateComplete( postCode, city, street, POS_TEL_NO);
						// }
						// else
						// {
						// //2014-05-21 KDH 여기다가 내가 할려다가말었네 개짜증나서=-_-=;
						// //여기서 리쿼리해야함..아놔
						// updateCompleteTelCh(CCMSTS, postCode, city, street,
						// POS_TEL_NO );
						// }

						// 리스트가 1개일경우 다시 순회정비 리스트로 이동.
						// 2개 이상일경우 한개를 제거.
					}

//					mConnectController.duplicateLogin(mContext);

				}
			}
		}
	}

	private void updateComplete(String postCode, String city, String street, String drv_tel, String drv_name) {
		showProgress();
		ContentValues contentValues = new ContentValues();
		contentValues.put("CCMSTS", "E0003");
		contentValues.put(DEFINE.POST_CODE, postCode);
		contentValues.put(DEFINE.CITY, city);
		contentValues.put(DEFINE.STREET, street);
		contentValues.put(DEFINE.DRIVN, drv_name); // Jonathan 추가 14.06.13
		contentValues.put("AUFNR", mCarInfoModel.getAUFNR());

		String[] keys = new String[1];
		keys[0] = "AUFNR";
		// // 14.06.16 Jonathan 여기 업데이트 할때 여기로 안옴.... 다른 곳을 타는 것 같음. 여기도 안탐.
		kog.e("Jonathan", "바뀐주소 ::: " + city);

		DbAsyncTask dbAsyncTask = new DbAsyncTask("updateComplete", DEFINE.REPAIR_TABLE_NAME, mContext, this,
				contentValues, keys);

		dbAsyncTask.execute(DbAsyncTask.DB_UPDATE);

	}

	private void updateCompleteTelCh(String CCMSTS, String postCode, String city, String street, String drv_tel,
									 String drv_name) {
		showProgress();
		ContentValues contentValues = new ContentValues();
		contentValues.put("CCMSTS", CCMSTS);
		contentValues.put(DEFINE.POST_CODE, postCode);
		contentValues.put(DEFINE.CITY, city);
		contentValues.put(DEFINE.STREET, street);
		contentValues.put(DEFINE.DRV_TEL, decrypt(DEFINE.DRV_TEL, drv_tel));
		contentValues.put(DEFINE.DRIVN, drv_name); // Jonathan 추가 14.06.13
		contentValues.put("AUFNR", mCarInfoModel.getAUFNR());

		// // 14.06.16 Jonathan 여기 업데이트 할때 여기로 안옴.... 다른 곳을 타는 것 같음.

		kog.e("Jonathan", "CCMSTS : " + CCMSTS + "POSTCODE : " + postCode + "CITY : " + city + "DRIVN : " + drv_name);
		kog.e("KDH", "updateCompleteTelCh getAUFNR = " + mCarInfoModel.getAUFNR());
		String[] keys = new String[1];
		keys[0] = "AUFNR";

		DbAsyncTask dbAsyncTask = new DbAsyncTask("updateComplete", DEFINE.REPAIR_TABLE_NAME, mContext, this,
				contentValues, keys);

		dbAsyncTask.execute(DbAsyncTask.DB_UPDATE);

	}

	private void updateComplete2() {
		showProgress();
		ContentValues contentValues = new ContentValues();
		String[] keys = new String[1];
		keys[0] = "AUFNR";

		DbAsyncTask dbAsyncTask = new DbAsyncTask("SELECT", DEFINE.REPAIR_TABLE_NAME, mContext, this, contentValues,
				keys);

		dbAsyncTask.execute(DbAsyncTask.DB_SELECT);

	}

	@Override
	public void reDownloadDB(String newVersion) {
		// TODO Auto-generated method stub

	}

	// private void initMaintenanceEmpty(int size) {
	// if (size > 0) {
	// mEmptyView.setVisibility(View.GONE);
	// mLvLast.setVisibility(View.VISIBLE);
	// } else {
	// mEmptyView.setVisibility(View.VISIBLE);
	// mLvLast.setVisibility(View.GONE);
	// }
	// }

	private void initGubun() {
		if (mProgressStatus.equals("E0004")) {
			if (mCarInfoModel == null || mCarInfoModel.getGUBUN().equals(" ")) {
				if (mBtnCompareCarNumber != null) {
					mBtnCompareCarNumber.setText("정비 결과등록 내역조회");
					mBtnEmergency.setVisibility(View.VISIBLE);

				}
			} else {
				mBtnCompareCarNumber.setText("정비 시작");
			}
		}

	}

	private void queryGroup(final MaintenanceResultModel model) {
		showProgress("정비항목을 조회 중입니다.");
		String[] _whereArgs = { "M028" };
		String[] _whereCause = { "ZCODEH" };

		String[] colums = { "ZCODEVT", "ZCODEV" };

		DbQueryModel dbQueryModel = new DbQueryModel(ConnectController.O_ITAB1_TABLE_NAME, _whereCause, _whereArgs,
				colums);

		DbAsyncTask dbAsyncTask = new DbAsyncTask("queryGroup", mContext, new DbAsyncResLintener() {

			@Override
			public void onCompleteDB(String funName, int type, Cursor cursor, String tableName) {

				hideProgress();

				if (cursor == null)
					return;

				cursor.moveToFirst();
				while (!cursor.isAfterLast()) {

					for (MaintenanceItemModel itemModel : model.getmLastItemModels()) {
						MaintenanceGroupModel groupModel = itemModel.getMaintenanceGroupModel();
						if (groupModel.getName_key().equals(cursor.getString(1))) {
							groupModel.setName(cursor.getString(0));
							// model.getmLastItemModels()
							// .remove(itemModel);
							// break;
						}
					}
					cursor.moveToNext();

				}
				cursor.close();

				mOnNumberResult.onResult(true, model);
			}
		}, dbQueryModel);
		dbAsyncTask.execute(DbAsyncTask.DB_SELECT);
	}

	public void onModify() {
		mBtnCompareCarNumber.setText("정비 시작");
		if (mProgressStatus.equals("E0004")) {
			// myung 20131216 UPDATE "U"의미 없음 " "으로 변경
			// if (mCarInfoModel.getGUBUN().equals("U")) {
			if (mCarInfoModel.getGUBUN().equals(" ")) {
				if (!mCarInfoModel.getDay().equals(CommonUtil.getCurrentDay())) {
					mBtnCompareCarNumber.setEnabled(false);
				}
			}
		}
	}

	public void setGUBUN(String gubun) {

		mGubun = gubun;
		mCarInfoModel.setGUBUN(gubun);
	}
}
