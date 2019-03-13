package com.ktrental.fragment;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.LayoutParams;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ktrental.R;
import com.ktrental.cm.connect.ConnectController;
import com.ktrental.cm.connect.Connector.ConnectInterface;
import com.ktrental.cm.db.DbAsyncTask;
import com.ktrental.cm.db.DbAsyncTask.DbAsyncResLintener;
import com.ktrental.common.DEFINE;
import com.ktrental.common.KtRentalApplication;
import com.ktrental.dialog.Address_Change_Dialog;
import com.ktrental.model.BaseMaintenanceModel;
import com.ktrental.model.HomeMaintenanceModel;
import com.ktrental.model.LoginModel;
import com.ktrental.model.RepairPlanModel;
import com.ktrental.model.TableModel;
import com.ktrental.popup.CallSendPopup;
import com.ktrental.popup.EventPopupC;
import com.ktrental.popup.SMSPopup;
import com.ktrental.ui.IconPageIndicator;
import com.ktrental.ui.IconPagerAdapter;
import com.ktrental.util.CommonUtil;
import com.ktrental.util.kog;

import java.util.ArrayList;
import java.util.HashMap;

public class HomeCardFragment extends BaseFragment {

	private ViewPager mPager;
	private CardPagerAdapter mAdapter;
	private IconPageIndicator mIndicator;

	private ArrayList<BaseMaintenanceModel> mMaintenanceModels = new ArrayList<BaseMaintenanceModel>();

	private boolean firstFlag = false;

	private OnCardClick mOnCardClick;

	private final static int CARD_MAX = 10;
	String beforeAddr;
	
	interface OnCardClick {
		void onCardClick(BaseMaintenanceModel model);
	}

	public ArrayList<BaseMaintenanceModel> getMaintenanceModels() {
		return mMaintenanceModels;
	}

	public void setMaintenanceModels(
			ArrayList<BaseMaintenanceModel> aMaintenanceModels) {
		mMaintenanceModels.clear();
		if (aMaintenanceModels != null) {

			int size = aMaintenanceModels.size();
			if (aMaintenanceModels.size() > CARD_MAX)
				size = CARD_MAX;

			for (int i = 0; i < size; i++) {
				BaseMaintenanceModel baseMaintenanceModel = aMaintenanceModels
						.get(i);
				mMaintenanceModels.add(baseMaintenanceModel);
			}

		}

		// this.mMaintenanceModels = aMaintenanceModels;
		try {
//			if (firstFlag) {
//				if (mAdapter.getCount() > 0) {
					mAdapter.notifyDataSetChanged();
					mIndicator.notifyDataSetChanged();
					// mPager.requestLayout();
//				}
//			}
		} catch (IllegalStateException e) {
			// TODO: handle exception
		}

	}

	public HomeCardFragment() {
		// TODO Auto-generated constructor stub
	}

	public HomeCardFragment(OnCardClick aOnCardClick) {
		// TODO Auto-generated constructor stub
		mOnCardClick = aOnCardClick;
	}

	public HomeCardFragment(ArrayList<BaseMaintenanceModel> container,
			OnCardClick aOnCardClick) {
		mMaintenanceModels = container;
		mOnCardClick = aOnCardClick;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mRootView = inflater.inflate(R.layout.card_layout, container, false);

		mPager = (ViewPager) mRootView.findViewById(R.id.pager);

		mAdapter = new CardPagerAdapter(mContext);
		mPager.setAdapter(mAdapter);
		// 2017.08.02. hjt. test. mAdapter.getCount 하기 전 notify를 해야 하지 않을까함.
		mAdapter.notifyDataSetChanged();

		mPager.setOffscreenPageLimit(mAdapter.getCount());
		// A little space between pages

		// If hardware acceleration is enabled, you should also remove
		// clipping on the pager for its children.
		mPager.setClipChildren(false);
		mIndicator = (IconPageIndicator) mRootView.findViewById(R.id.indicator);
		mIndicator.setViewPager(mPager);

		firstFlag = true;

		return mRootView;
	}

	@Override
	public void onStart() {
		super.onStart();

	}

	private class CardPagerAdapter extends PagerAdapter implements
	IconPagerAdapter, OnClickListener {

		private Context mAdaperContext;
		private LayoutInflater mInflater;

		public CardPagerAdapter(Context c) {
			super();
			mInflater = LayoutInflater.from(c);

		}

		@Override
		public int getCount() {
			return mMaintenanceModels.size();
		}

		public int getItemPosition(Object object) {
			return POSITION_NONE;
		}

		@Override
		public Object instantiateItem(View pager, int position) {
			View v = null;

			HomeMaintenanceModel model = (HomeMaintenanceModel) mMaintenanceModels
					.get(position);

			v = mInflater.inflate(R.layout.card_item, null);
			Button btnCall = (Button) v.findViewById(R.id.btn_call);
			btnCall.setTag(position);
			btnCall.setOnClickListener(this);

			Button btnSms = (Button) v.findViewById(R.id.btn_sms);
			btnSms.setTag(position);
			btnSms.setOnClickListener(this);

			Button btnMap = (Button) v.findViewById(R.id.btn_map);
			btnMap.setTag(position);
			btnMap.setOnClickListener(this);

			TextView tvCarNum = (TextView) v.findViewById(R.id.tv_carnum);
			tvCarNum.setText(model.getCarNum());
			tvCarNum.setOnClickListener(this);

			TextView tvPeriod = (TextView) v.findViewById(R.id.tv_period);

			//myung 20131121 UPDATE "중고차 장기" 사이즈 변경
			if(model.getCTRTY().equals("2")){			
				// TextView 크기 조절
				LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) tvPeriod.getLayoutParams();
				lp.width *= 2;
				lp.height = LayoutParams.WRAP_CONTENT;
				tvPeriod.setLayoutParams(lp);
			}

			tvPeriod.setText(RepairPlanModel.getPeriodStatus(model.getCTRTY()));

			TextView tvCarName = (TextView) v.findViewById(R.id.tv_car_name);
			tvCarName.setText(model.getCarname());

			TextView tvName = (TextView) v.findViewById(R.id.tv_name);
			tvName.setText(model.getCUSTOMER_NAME());
			tvName.setOnClickListener(this);

			TextView tvCustomerName = (TextView) v
					.findViewById(R.id.tv_customer_name);

			tvCustomerName.setText(model.getCustomerName());
			tvCustomerName.setOnClickListener(this);

			TextView tvAddress = (TextView) v.findViewById(R.id.tv_address);

			String postCode = model.getPostCode();
			String city = model.getCity();
			String street = model.getStreet();

			if (postCode.length() > 5) {
				StringBuffer sb = new StringBuffer(postCode);
				sb.insert(postCode.length(), "]");
				postCode = "[" + sb;
			}
			String address = postCode + city + street;
			beforeAddr = address;
			tvAddress.setText(address);
			tvAddress.setOnClickListener(this);

			TextView tvLastMaintenance = (TextView) v
					.findViewById(R.id.tv_last_maintenance);
			//myung 20131127 UPDATE 최정정비 -> 요청사항 변경
			tvLastMaintenance.setText("요청사항 "
					+ CommonUtil.setDotDate(model.getCCMRQ()));

			TextView tvReoil = (TextView) v.findViewById(R.id.tv_reoil);
			tvReoil.setText(model.getReoil());

			if (model.getNIELS_NM().equals(" "))
				v.findViewById(R.id.iv_vip).setVisibility(View.GONE);

			FrameLayout rootView = (FrameLayout) v;
			tvCarNum.setTag(position);
			tvAddress.setTag(position);
			tvCustomerName.setTag(position);
			tvName.setTag(position);
			// rootView.setTag(position);
			// rootView.setOnClickListener(this);

			((ViewPager) pager).addView(v, 0);
			// views.put(position, v);

			return v;
		}

		@Override
		public void destroyItem(View pager, int position, Object view) {
			((ViewPager) pager).removeView((View) view);
			// views.remove(position);
			// view = null;
		}

		// public int getItemPosition(Object object) {
		// return POSITION_NONE;
		// }

		@Override
		public boolean isViewFromObject(View pager, Object obj) {
			return pager == obj;
		}

		@Override
		public void restoreState(Parcelable arg0, ClassLoader arg1) {
		}

		@Override
		public Parcelable saveState() {
			return null;
		}

		@Override
		public void startUpdate(View arg0) {
		}

		@Override
		public void finishUpdate(View arg0) {
		}

		@Override
		public int getIconResId(int index) {
			// TODO Auto-generated method stub
			return R.drawable.main_navi_selector;
		}

		// @Override
		// public void notifyDataSetChanged() {
		// int key = 0;
		// for(int i = 0; i < views.size(); i++) {
		// key = views.keyAt(i);
		// View view = views.get(key);
		// //refresh할 작업들
		// }
		// super.notifyDataSetChanged();
		// }

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
			case R.id.btn_call:
				clickCall(v);
				break;
			case R.id.btn_sms:
				clickSms(v);
				break;

			case R.id.btn_map:
				clickMap(v);
				break;
			case R.id.fl_root:
				clickCard(v);
				break;
			case R.id.tv_carnum:
				clickCarnum(v);
				break;
			case R.id.tv_name:
				clickCard(v);
				break;
			case R.id.tv_customer_name:
				clickCard(v);
				break;
			case R.id.tv_address:
				clickAddress(v);
				break;

			default:
				break;
			}
		}

		private void clickAddress(View v) {
			int position = (Integer) v.getTag();
			final BaseMaintenanceModel model = mMaintenanceModels.get(position);

			final HashMap<String, String> o_struct1 = new HashMap<String, String>();
			o_struct1.put("INVNR", model.getCarNum());
			o_struct1.put("MAKTX", model.getCarname());
			o_struct1.put("DLSM1", model.getDRIVER_NAME());
			o_struct1.put("DLST1", model.getTel());
			o_struct1.put("NAME1", model.getCUSTOMER_NAME());
			//			o_struct1.put("DLST1", model.getTel());
			o_struct1.put("CUSJUSO", model.getAddress());
			o_struct1.put("EQUNR", model.getEQUNR());

			if (isNetwork()) {
				final ConnectController connectController = new ConnectController(
						new ConnectInterface() {

							@Override
							public void reDownloadDB(String newVersion) {
								// TODO Auto-generated method stub

							}

							@Override
							public void connectResponse(String FuntionName,
									String resultText, String MTYPE,
									int resulCode, TableModel tableModel) {
								// TODO Auto-generated method stub
								if (FuntionName.equals("ZMO_1040_WR02")) {

									if (MTYPE.equals("S")) {

										if (tableModel.getTableArray() != null) {
											ArrayList<HashMap<String, String>> array = tableModel
													.getTableArray();
											for (HashMap<String, String> hashMap : array) {
												String postCode = hashMap
														.get("POS_POST");
												String city = hashMap
														.get("POS_CITY1");
												String street = hashMap
														.get("POS_STREET");

												model.setAddress(postCode
														+ city + street);


												// StringBuffer sb = new
												// StringBuffer(
												// mCarInfoModel.getAddress());
												// sb.insert(7, "]");
												//
												// mTvAddress.setText("[" + sb);

												// 이관요청으로 값을 변경. (DB에서 변경 E0003
												// 으로)
												// 주소 또한 변경.
												//2014-04-29 KDH 주소변경아놔
												kog.e("KDH", "beforeAddr = "+beforeAddr);
												String addr = "["+postCode+"]"+city+street;
												kog.e("KDH", "addr = "+addr);
												if(!beforeAddr.equals(addr))
												{
													updateComplete(postCode, city,
															street,
															model.getAUFNR());

												}
												else
												{
													//리쿼리
												}
											
												// 리스트가 1개일경우 다시 순회정비 리스트로 이동.
												// 2개 이상일경우 한개를 제거.
											}
											
											

										}
									} else {
										hideProgress();
									}
								}
							}
						}, mContext);

				final Address_Change_Dialog acd = new Address_Change_Dialog(
						mContext, o_struct1);
				Button bt_save = (Button) acd
						.findViewById(R.id.address_change_save_id);
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
						//myung 20131211 UPDATE 첫번째 param "120"을 로그인 시 받아온 INGRP정보로 세팅
						//						connectController.setZMO_1040_WR02("120",getTable(acd, o_struct1));
						connectController.setZMO_1040_WR02(getTable(acd, o_struct1));
						acd.dismiss();
					}
				});
				acd.show();
			}
		}

		private void clickCarnum(View v) {
			int position = (Integer) v.getTag();
			BaseMaintenanceModel model = mMaintenanceModels.get(position);

			if (mOnCardClick != null)
				mOnCardClick.onCardClick(model);
		}

		private void MaintenanceResultFragmentclickCard(View v) {
			int position = (Integer) v.getTag();
			BaseMaintenanceModel model = mMaintenanceModels.get(position);

			if (mOnCardClick != null)
				mOnCardClick.onCardClick(model);
		}

		private void clickCall(View v) {
			int position = (Integer) v.getTag();
			BaseMaintenanceModel model = mMaintenanceModels.get(position);
			//myung 20131217 ADD SMS 발송 시 고객 전화번호가 없을 경우 메시지 출력 후 리턴 시킴.
			if (model.getTel().equals("") && model.getDrv_mob().equals("")) {
				EventPopupC epc = new EventPopupC(mContext);
				epc.show("연락처가 없습니다. \n고객 연락처를 확인하여 주십시요.");
				return;
			}
			CallSendPopup callSendPopup = new CallSendPopup(mContext, model);
			callSendPopup.show();
		}

		private void clickCard(View v) {
			int position = (Integer) v.getTag();
			BaseMaintenanceModel model = mMaintenanceModels.get(position);

			if (mOnCardClick != null)
				mOnCardClick.onCardClick(model);
		}

		private void clickSms(View v) {
			int position = (Integer) v.getTag();
			BaseMaintenanceModel model = mMaintenanceModels.get(position);
			//myung 20131217 ADD SMS 발송 시 고객 전화번호가 없을 경우 메시지 출력 후 리턴 시킴.
			if (model.getTel().equals("") || model.getTel().equals(" ")) {
				EventPopupC epc = new EventPopupC(mContext);
				epc.show("연락처가 없습니다. \n고객 연락처를 확인하여 주십시요.");
				return;
			}

			if(!model.getTel().substring(0, 2).equals("01")){
				EventPopupC epc = new EventPopupC(mContext);
				epc.show("SMS를 발신할 수 없는 전화번호 입니다.");
				return;
			}
			SMSPopup popup = new SMSPopup(mContext, model);
			popup.show();
		}

		private void clickMap(View v) {

			int position = (Integer) v.getTag();
			BaseMaintenanceModel model = mMaintenanceModels.get(position);

			String address = model.getAddress();
			//myung 20131202 ADD 올레네비 호출 시 우편번호는 전송하지 않아야 함.
			if(address.length()>7)
				address = address.substring(7);
			//			Log.i("address", address);

			if (address.equals("") || address.equals("   ")) {
				showEventPopup2(null, "주소정보가 없어 올레네비를 실행할 수 없습니다.");
				return;
			}
			showProgress("좌표를 얻어 오고 있습니다.");
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
							// TODO Auto-generated method stub
							hideProgress();
							if (MTYPE.equals("S")) {
								ArrayList<HashMap<String, String>> arrayList = tableModel.getTableArray();

								kog.e("Jonathan", "올레 MTYPE == S 나옴.");
								if (arrayList.size() > 0) {
									HashMap<String, String> map = arrayList
											.get(0);
									String tr_x = map.get("TR_X"); 
									String tr_y = map.get("TR_Y");
									
									kog.e("Jonathan", "올레 tr_x ::	 " + tr_x + " 올레맵 tr_y :: " + tr_y );
									
									LocationManager locationManager;
									Location location = null;
//									locationManager = (LocationManager) mContext
//											.getSystemService(Context.LOCATION_SERVICE);
//
//									// Criteria 클래스로, 이용 가능한 provider 들 중 가장 적합한
//									// 것을
//									// 고르도록 한다.
//									Criteria criteria = new Criteria();
//									criteria.setAccuracy(Criteria.ACCURACY_FINE);
//									criteria.setAltitudeRequired(false);
//									criteria.setBearingRequired(false);
//									criteria.setCostAllowed(true);
//									criteria.setPowerRequirement(Criteria.POWER_LOW);
//									String provider = locationManager
//											.getBestProvider(criteria, true);
//
//									Location location = locationManager
//											.getLastKnownLocation(provider);
//									kog.e("Jonathan", "올레 provider ::	 " + provider);
//									kog.e("Jonathan", "올레 Location ::	 " + String.valueOf(location));

									locationManager = (LocationManager) mContext
											.getSystemService(Context.LOCATION_SERVICE);

									boolean isGPSEnabled = false;
									boolean isNetworkEnabled = false;
									// getting GPS status
									isGPSEnabled = locationManager
											.isProviderEnabled(LocationManager.GPS_PROVIDER);

									// getting network status
									isNetworkEnabled = locationManager
											.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

									if (!isGPSEnabled && !isNetworkEnabled) {
										// no network provider is enabled
									} else {
										if (isNetworkEnabled) {
											if (locationManager != null) {
												location = locationManager
														.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
											}
										}
										// if GPS Enabled get lat/long using GPS Services
										else if (isGPSEnabled) {
											if (location == null) {
												if (locationManager != null) {
													location = locationManager
															.getLastKnownLocation(LocationManager.GPS_PROVIDER);
//													}
												}
											}
										}
									}


									try {
										if (location == null) {
											showEventPopup2(null, "실패 하였습니다.");
											return;
										}
										double start_x = Double
												.parseDouble(tr_x);
										double start_y = Double
												.parseDouble(tr_y);
										//										Log.d("", "location.getLongitude() "
										//												+ location.getLongitude()
										//												+ " location.getLatitude() "
										//												+ location.getLatitude());
										CommonUtil.onOllehNavi(
												location.getLatitude(),
												location.getLongitude(),
												start_y, start_x,
												mContext.getPackageName(),
												mContext);
									} catch (NumberFormatException e) {
										// TODO: handle exception
										//										Log.e("", "");
									}

								}
							} else {
								//								showEventPopup2(null, "실패 하였습니다.");
								showEventPopup2(null, "고객 주소를 확인해 주세요.");
							}
						}
					}, mContext);

			connectController.addressToWGS(address);

		}
	}

	private ArrayList<HashMap<String, String>> getTable(
			Address_Change_Dialog acd, HashMap<String, String> o_struct1) {
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
			i_itab1.add(hm);
		}
		return i_itab1;
	}

	private void updateComplete(String postCode, String city, String street,
			String aufnr) {

		ContentValues contentValues = new ContentValues();
		contentValues.put("CCMSTS", "E0003");
		contentValues.put(DEFINE.POST_CODE, postCode);
		contentValues.put(DEFINE.CITY, city);
		contentValues.put(DEFINE.STREET, street);
		contentValues.put("AUFNR", aufnr);

		String[] keys = new String[1];
		keys[0] = "AUFNR";

		DbAsyncTask dbAsyncTask = new DbAsyncTask("updateComplete",
				DEFINE.REPAIR_TABLE_NAME, mContext, new DbAsyncResLintener() {

			@Override
			public void onCompleteDB(String funName, int type,
					Cursor cursor, String tableName) {
				// TODO Auto-generated method stub
				synchronized (notifyDataSetChangedRunnable){
					mActivity.runOnUiThread(notifyDataSetChangedRunnable);
					try {
						notifyDataSetChangedRunnable.wait();
					} catch (Exception e){
						e.printStackTrace();
					}
				}
//				hideProgress();
//				mAdapter.notifyDataSetChanged();
//				mIndicator.notifyDataSetChanged();
			}
		}, contentValues, keys);

		dbAsyncTask.execute(DbAsyncTask.DB_UPDATE);
	}
	private final Runnable notifyDataSetChangedRunnable = new Runnable() {
		@Override
		public void run() {
			hideProgress();
			mAdapter.notifyDataSetChanged();
			mIndicator.notifyDataSetChanged();
			synchronized (this){
				this.notify();
			}
		}
	};
}
