package com.ktrental.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.ktrental.R;
import com.ktrental.adapter.CorCardAdapter;
import com.ktrental.cm.connect.ConnectController;
import com.ktrental.cm.connect.Connector.ConnectInterface;
import com.ktrental.dialog.Cor_Card_Detail_Dialog;
import com.ktrental.model.CorCardECount;
import com.ktrental.model.CorCardModel;
import com.ktrental.model.TableModel;
import com.ktrental.popup.BaseTextPopup;
import com.ktrental.popup.DatepickPopup2;
import com.ktrental.popup.EventPopupC;
import com.ktrental.popup.MovementDialog;
import com.ktrental.popup.Popup_Window_DelSearch;
import com.ktrental.util.kog;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public class CorCardRegFragment extends BaseFragment implements OnClickListener, OnItemClickListener, BaseTextPopup.OnSelectedPopupItem {

	private Button mBtnSearch, mBtnSend, mBtnSendCancel;
	private Button btn_move_first_page, btn_move_backward, btn_move_forward, btn_move_last_page;
	private CorCardAdapter mCorCardAdapter;
	private ListView mLvMovement;
	private CorCardECount eCount;
	//	private ArrayList<MovementModel> mMovementModels = new ArrayList<MovementModel>();
	private ArrayList<CorCardModel> mCorCardModels = new ArrayList<CorCardModel>();

	private HashMap<Integer, CorCardModel> mCheckedModels = new HashMap<>();

	private String mStatus = "";
	private String mUseType = "";
	private String mPage = "1";
	private String mAUFNR;
	private String mDRIVER;
	private String mEQUNR;
	private String mCarNum;
	//Jonathan 14.07.31 추가
	private String mINVNR;

	private String mDATUMF;
	private String mDATUMT;


	private Button mBtnCarNum;
	private Button mBtnAufnr;
	private Button mBtnName;
	private String mOilType;
	private String mDriverName;
	private boolean mInitFlag = false;

	private MovementDialog mMovementDialog;

	private Button mBtnStart;

	private ImageView mIvEmpty;

	private Popup_Window_DelSearch pwDelSearch;


	private Button datepick1_bt, datepick2_bt;
	private DatepickPopup2 idatepickpopup;


	// 승인 박스
	private BaseTextPopup mStatusFilterPopup;
	// tkdyd
	private BaseTextPopup mUsetypeFilterPopup;

	private LinkedHashMap<String, String> mStatusFilterMap = new LinkedHashMap<String, String>();
	private LinkedHashMap<String, String> mUsetypeFilterMap = new LinkedHashMap<String, String>();
	private LinkedHashMap<String, String> mPageFilterMap = new LinkedHashMap<String, String>();

	private final static String STATUS_FILTER = "StatusFilter";
	private final static String USETYPE_FILTER = "UseTypeFilter";
	private final static String PAGE_FILTER = "PageFilter";

	private CheckBox btn_status;
	private CheckBox btn_usetype;

	private TextView tv_item_count, tv_page_count, tv_won_count;
	private CheckBox btn_move_page;

	private BaseTextPopup mPageFilterPopup;

	public static int MESSAGE_REFRESH = 0x000303;


	ConnectController mCc;


	String P2 = "P2";

	public CorCardRegFragment(){
	}

	public CorCardRegFragment(String mEQUNR, String mDRIVER, String mAUFNR,
                              String carNum, String oilType, String driverName, boolean initFlag) {
		super();
		this.mEQUNR = mEQUNR;
		this.mDRIVER = mDRIVER;
		this.mAUFNR = mAUFNR;
		this.mCarNum = carNum;
		mOilType = oilType;
		mDriverName = driverName;
		mInitFlag = initFlag;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		mContext = getActivity();
		mRootView = (View) inflater.inflate(R.layout.corcard_reg_fragment_layout,
				null);

		mBtnSearch = (Button) mRootView.findViewById(R.id.btn_search);
		mBtnSearch.setOnClickListener(this);

		TextView tvTitle = (TextView) mRootView
				.findViewById(R.id.tv_common_title);
		tvTitle.setText("[7101]법인카드-개인처리");

		mCorCardAdapter = new CorCardAdapter(mContext);
		mCorCardAdapter.setOnClickRowListener(new CorCardAdapter.OnClickRowListener() {
			@Override
			public void onClickRowListener(int pos) {
//				mCorCardModels.get(pos).setCheckFlag(!mCorCardModels.get(pos).isCheckFlag());
//				mCorCardAdapter.notifyDataSetChanged();

				Cor_Card_Detail_Dialog tldd = new Cor_Card_Detail_Dialog(mContext, mCorCardModels.get(pos), mHandler);
				tldd.show();
			}

			@Override
			public void onCheckRowListener(int position, boolean check) {
				if(mCheckedModels == null){
					mCheckedModels = new HashMap<Integer, CorCardModel>();
				}
				if(check){
					mCheckedModels = null;
					mCheckedModels = new HashMap<Integer, CorCardModel>();
					mCheckedModels.put(1, mCorCardModels.get(position));
				} else {
					if(mCheckedModels != null && mCheckedModels.size() > 0){
						mCheckedModels.remove(position);
					}
				}

			}
		});
		mLvMovement = (ListView) mRootView.findViewById(R.id.lv_movement);
		mLvMovement.setAdapter(mCorCardAdapter);
//		mLvMovement.setOnItemClickListener(this);
		mBtnCarNum = (Button) mRootView.findViewById(R.id.btn_movement_carnum);
		mBtnCarNum.setText(mCarNum);
		mBtnCarNum.setOnClickListener(this);
		mBtnCarNum.setEnabled(mInitFlag);

		mBtnSend = (Button) mRootView.findViewById(R.id.btn_send);
		mBtnSend.setOnClickListener(this);
		mBtnSendCancel = (Button) mRootView.findViewById(R.id.btn_send_cancel);
		mBtnSendCancel.setOnClickListener(this);

		mBtnAufnr = (Button) mRootView.findViewById(R.id.btn_movement_number);
		mBtnAufnr.setText(mAUFNR);
		mBtnName = (Button) mRootView.findViewById(R.id.btn_movement_name);
		mBtnName.setText(mDriverName);
		mBtnName.setEnabled(mInitFlag);

		datepick1_bt = (Button)mRootView.findViewById(R.id.datepick1_bt);
		datepick1_bt.setOnClickListener(this);

		datepick2_bt = (Button)mRootView.findViewById(R.id.datepick2_bt);
		datepick2_bt.setOnClickListener(this);

		btn_status = (CheckBox) mRootView.findViewById(R.id.card_confirm);
		btn_status.setOnClickListener(this);
		btn_usetype = (CheckBox) mRootView.findViewById(R.id.card_use);
		btn_usetype.setOnClickListener(this);

		tv_won_count = (TextView)mRootView.findViewById(R.id.tv_won_count);
		tv_item_count = (TextView)mRootView.findViewById(R.id.tv_item_count);
		tv_page_count = (TextView)mRootView.findViewById(R.id.tv_page_count);

		btn_move_page = (CheckBox)mRootView.findViewById(R.id.btn_move_page);
		btn_move_page.setOnClickListener(this);

		btn_move_first_page = (Button)mRootView.findViewById(R.id.btn_move_first_page);
		btn_move_backward = (Button)mRootView.findViewById(R.id.btn_move_backward);
		btn_move_forward = (Button)mRootView.findViewById(R.id.btn_move_forward);
		btn_move_last_page = (Button)mRootView.findViewById(R.id.btn_move_last_page);

		btn_move_first_page.setOnClickListener(this);
		btn_move_backward.setOnClickListener(this);
		btn_move_forward.setOnClickListener(this);
		btn_move_last_page.setOnClickListener(this);

//		mCc = new ConnectController(this, mContext);

		setDateButton();

		mIvEmpty = (ImageView) mRootView.findViewById(R.id.iv_empty);

		if (mCorCardModels.size() <= 0) {
			mIvEmpty.setVisibility(View.VISIBLE);
			mLvMovement.setVisibility(View.GONE);
		}

		if (P2.equals(getActivity().getIntent().getStringExtra("is_CarManager")))
		{

			mBtnName.setEnabled(false);

		}

		pwDelSearch = new Popup_Window_DelSearch(mContext);

		initFilter();

		return mRootView;
	}


	private void initFilter() {

		// myung 20131210 DELETE 고객명 기본으로 선택. 전체는 삭제
		// mMaintenanceFilterMap.put("전체", "전체");
		mStatusFilterMap.put("전체", "");
		mStatusFilterMap.put("미등록", "99");
		mStatusFilterMap.put("개인등록", "00");
		mStatusFilterMap.put("개인전송", "01");

		mStatusFilterPopup = new BaseTextPopup(mContext, mStatusFilterMap, STATUS_FILTER);

		mUsetypeFilterMap.put("전체", "");
		mUsetypeFilterMap.put("개인사용", "01");
		mUsetypeFilterMap.put("회사사용", "02");
		mUsetypeFilterPopup = new BaseTextPopup(mContext, mUsetypeFilterMap, USETYPE_FILTER);
	}

	private void initFilterPage(String page){
		if(page == null){
			return;
		}
		int int_page = Integer.valueOf(page);
		for(int i=1; i<=int_page; i++){
			String str_value = String.valueOf(i);
			mPageFilterMap.put(str_value + " page", str_value);
		}
		mPageFilterPopup = new BaseTextPopup(mContext, mPageFilterMap, PAGE_FILTER);
	}

	private void setDateButton() {
		Calendar cal = Calendar.getInstance();
		int year = cal.get(Calendar.YEAR);
		int month = cal.get(Calendar.MONTH) + 1;
		int day = cal.get(Calendar.DAY_OF_MONTH);
		String month_str = String.format("%02d", month);
		String day_str = String.format("%02d", day);
		// DATE2 = year + month_str + day_str;
		datepick2_bt.setText(year + "." + month_str + "." + day_str);// 오늘날짜
		cal.set(Calendar.DAY_OF_MONTH, 1);
		day = cal.get(Calendar.DAY_OF_MONTH);
		String day_str_1 = String.format("%02d", day);
		// DATE1 = year + month_str + day_str_1;
		datepick1_bt.setText(year + "." + month_str + "." + day_str_1);// 이달의첫날
	}

	@Override
	public void onClick(View v) {
		String data_get;
		String date;

		switch (v.getId()) {

			case R.id.card_confirm:
				showFilterPopup(mStatusFilterPopup, btn_status, btn_status.isChecked());
				break;
			case R.id.card_use:
				showFilterPopup(mUsetypeFilterPopup, btn_usetype, btn_usetype.isChecked());
				break;
			case R.id.btn_move_page:
				if(mPageFilterPopup != null) {
					showFilterPopup(mPageFilterPopup, btn_move_page, btn_move_page.isChecked());
				}
				break;
			case R.id.btn_move_first_page:
				clickSearch(R.id.btn_move_first_page);
				break;
			case R.id.btn_move_backward:
				clickSearch(R.id.btn_move_backward);
				break;
			case R.id.btn_move_forward:
				clickSearch(R.id.btn_move_forward);
				break;
			case R.id.btn_move_last_page:
				clickSearch(R.id.btn_move_last_page);
				break;

			case R.id.datepick1_bt:
				data_get = datepick1_bt.getText().toString();
				date = data_get.replace(".", "");
				idatepickpopup = new DatepickPopup2(mContext, date, 0);
				idatepickpopup.show(datepick1_bt);
				break;

			case R.id.datepick2_bt:
				data_get = datepick1_bt.getText().toString();
				date = data_get.replace(".", "");
				idatepickpopup = new DatepickPopup2(mContext, date, 0);
				idatepickpopup.show(datepick2_bt);
				break;

			case R.id.btn_search:
				clickSearch(0);
				break;
			case R.id.btn_send:
				corCardSend();
				break;
			case R.id.btn_send_cancel:
				corCardSendCancel();
				break;
			default:
				break;
		}

	}

	private void corCardSendCancel(){
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

						if (MTYPE.equals("S")) {
							showEventPopup2(null, "" + resultText);
							// myung 20131231 리스트 중복 조회 수정
							clickSearch(0);
						} else {
							showEventPopup2(null, "" + resultText);
						}
					}
				}, mContext);

		CorCardModel model = null;
		if(mCheckedModels != null && mCheckedModels.size() > 0) {
//			Set set = mCheckedModels.keySet();
//			Iterator<String> it = set.iterator();
//			String key;
//
			Set entrySet = mCheckedModels.entrySet();
			if(entrySet != null) {
				Iterator it = entrySet.iterator();

				while (it.hasNext()) {
					Map.Entry me = (Map.Entry) it.next();
					model = (CorCardModel) me.getValue();
					HashMap<String, String> map2 = new HashMap<>();
//
//					kog.e("Jonathan", "login_function_1 key ===  value  === ");
//					key = it.next();
//					kog.e("Jonathan", "login_function_1 key ===  " + key + "    value  === " + mCheckedModels.get(key));
//					model = mCheckedModels.get(key);
					break;
				}
			}
		}
		if(model == null){
			return;
		}
		if(mCheckedModels == null || mCheckedModels.size() == 0 || mCheckedModels.get(1) == null
				|| mCheckedModels.get(1).getSTATUS() == null || !mCheckedModels.get(1).getSTATUS().equals("01")){
			final EventPopupC epc = new EventPopupC(mContext);
			Button btn_confirm = (Button) epc.findViewById(R.id.btn_ok);
			btn_confirm.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					try {
						epc.dismiss();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
			epc.show("처리할 데이터가 없습니다.");
			return;
		}
		connectController.getCorCardSendCancel(model.getWEBTYPE(), model.getWEBDOCNUM(), model.getDOCTYPE(), model.getBELNR(), model.getGJAHR(), model.getELC_SEQ(), model.getSTATUS(), "");
	}

	private void clickSearch(int id) {

		int page = 0;
		if(mPage != null){
			page = Integer.valueOf(mPage);
		}

		if(id != 0){
			if(id == R.id.btn_move_first_page){
				if(mPage.equals("1")){
					return;
				} else {
					mPage = "1";
				}
			} else if(id == R.id.btn_move_backward){
				if(!mPage.equals("1")){
					page--;
					mPage = String.valueOf(page);
				} else {
					return;
				}
			} else if(id == R.id.btn_move_forward){
				if(eCount != null && eCount.getE_PAGE() != null){
					int totalPage = Integer.valueOf(eCount.getE_PAGE());
					if(page < totalPage){
						page++;
					} else {
						return;
					}
					mPage = String.valueOf(page);
				}
			} else if(id == R.id.btn_move_last_page){
				if(eCount != null && eCount.getE_PAGE() != null){
					int totalPage = Integer.valueOf(eCount.getE_PAGE());
					if(page < totalPage){
						page = totalPage;
					} else {
						return;
					}
					mPage = String.valueOf(page);
				}
			}
		}

		showProgress("법인카드 사용내역 조회중 입니다.");

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
							// myung 20131231 리스트 중복 조회 수정
							mCorCardModels.clear();
							ArrayList<HashMap<String, String>> array = tableModel.getTableArray();

							for (HashMap<String, String> map : array) {
								CorCardModel model = new CorCardModel(map.get("USETYPENM"), map.get("STATNM"),
										map.get("ORGL_PERM_DT"), map.get("BUY_PTM"),
										map.get("PERM_SAM"), map.get("DOCNAM"), map.get("SGTXT"),
										map.get("BT_NM"), map.get("TRE_FRAN"),
										map.get("ORGL_PERM_NO"), map.get("TAXTN_TY"),
										map.get("FRAN_ADDR"), map.get("SKTEXT"),
										map.get("SENAME"), map.get("CHG_SALE_AMT"),
										map.get("CHG_STAX"), map.get("ELC_BUY_DT"),
										map.get("BELNR"), map.get("CARDKINDNM"),
										map.get("DOCSTATNM"), map.get("FRAN_BRNO"), map.get("ELC_SEQ"), map.get("ORGEH"), map.get("WEBTYPE"),
										map.get("WEBDOCNUM"), map.get("DOCTYPE"), map.get("GJAHR"), map.get("BUDAT"), map.get("SPERN"),
										map.get("SORGEH"), map.get("CARD_NO"), map.get("VKGRP"), map.get("KTEXT"), map.get("STATUS"), map.get("BUY_CANC"),
										map.get("USETYPE"), map.get("APP_SCD_DT"), map.get("BUY_AMT"), map.get("BUY_SAM"), map.get("REASON"), false
								);
								mCorCardModels.add(model);

								Set <String> set  = map.keySet();
								Iterator <String> it = set.iterator();
								String key;

								while(it.hasNext())
								{
									key = it.next();
									kog.e("Jonathan", "moveF key ===  " + key + "    value  === " + map.get(key));
								}


								kog.e("Jonathan", "================================================================================");
								kog.e("Jonathan", "================================================================================");
								kog.e("Jonathan", "================================================================================");

							}
							if (mCorCardModels.size() <= 0) {
								mIvEmpty.setVisibility(View.VISIBLE);
								mLvMovement.setVisibility(View.GONE);
							} else {
								mLvMovement.setVisibility(View.VISIBLE);
								mIvEmpty.setVisibility(View.GONE);
							}

							mCorCardAdapter.setData(mCorCardModels);

							String e_sum = tableModel.getResponse("E_SUM");
							String e_sum2 = tableModel.getResponse("E_SUM2");
							String e_count = tableModel.getResponse("E_COUNT");
							String e_totalcount = tableModel.getResponse("E_TOTALCNT");
							String e_page = tableModel.getResponse("E_PAGE");

							tv_won_count.setText(e_sum);
							tv_page_count.setText(e_page);
							tv_item_count.setText(e_count);

							if(eCount == null){
								eCount = new CorCardECount(e_sum, e_count, e_page);
							}
							btn_move_page.setText(mPage + " page");

							initFilterPage(e_page);

						} else {
							showEventPopup2(null, "" + resultText);
							mIvEmpty.setVisibility(View.VISIBLE);
							mLvMovement.setVisibility(View.GONE);
						}
					}
				}, mContext);
		mDATUMF = datepick1_bt.getText().toString().replace(".", "");
		mDATUMT = datepick2_bt.getText().toString().replace(".", "");

		Calendar cal2 = Calendar.getInstance();
		Calendar cal1 = Calendar.getInstance();

		int year2 = Integer.valueOf(mDATUMT.substring(0,4));
		int month2 = Integer.valueOf(mDATUMT.substring(4,6));
		int day2 = Integer.valueOf(mDATUMT.substring(6, 8));

		int year1 = Integer.valueOf(mDATUMF.substring(0,4));
		int month1 = Integer.valueOf(mDATUMF.substring(4,6));
		int day1 = Integer.valueOf(mDATUMF.substring(6, 8));

		cal2.set(Calendar.YEAR, year2);
		cal2.set(Calendar.MONTH, month2);
		cal2.set(Calendar.DAY_OF_MONTH, day2);
		cal2.set(Calendar.HOUR, 0);
		cal2.set(Calendar.MINUTE, 0);

		cal1.set(Calendar.YEAR, year1);
		cal1.set(Calendar.MONTH, month1);
		cal1.set(Calendar.DAY_OF_MONTH, day1);
		cal1.set(Calendar.HOUR, 0);
		cal1.set(Calendar.MINUTE, 0);

		long d1 = cal1.getTime().getTime();
		long d2 = cal2.getTime().getTime();

		// 계산
		int days =(int)((d2-d1)/(1000*60*60*24));

		if(days >= 31){
			hideProgress();
			final EventPopupC epc = new EventPopupC(mContext);
			Button btn_confirm = (Button)epc.findViewById(R.id.btn_ok);
			btn_confirm.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {

					try {
						epc.dismiss();
					} catch (Exception e){
						e.printStackTrace();
					}
				}
			});
			epc.show("31일 이상은 조회가 불가능 합니다");
			return;
		}
		// 임시로 강제로 넣자
//		mDATUMF = "20170601";
//		mDATUMT = "20170630";
		kog.e("Jonathan", "CorCard_ mStatus ::" + mStatus + "  mUseType :: " + mUseType + " mPage :: " + mPage + " mDATUMF :: " + mDATUMF + " mDATUMT :: " + mDATUMT);
		connectController.getCorCard(mStatus, mUseType, mPage, mDATUMF, mDATUMT);
	}

	private void corCardSend(){
		if(mCheckedModels == null || mCheckedModels.size() == 0 || mCheckedModels.get(1) == null
				|| mCheckedModels.get(1).getSTATUS() == null || !mCheckedModels.get(1).getSTATUS().equals("00")){
			final EventPopupC epc = new EventPopupC(mContext);
			Button btn_confirm = (Button) epc.findViewById(R.id.btn_ok);
			btn_confirm.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					try {
						epc.dismiss();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
			epc.show("전송할 항목이 없습니다");
			return;
		}

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

						if (MTYPE.equals("S")) {
							// myung 20131231 리스트 중복 조회 수정
							clickSearch(0);
							showEventPopup2(null, "" + resultText);
						} else {
							if (MTYPE.equals("E"))
							{
								showEventPopup2(null, "" + resultText);
							}
						}
					}
				}, mContext);

		connectController.getCorCardSend(mCheckedModels);
	}
	@Override
	public void onItemClick(AdapterView<?> arg0, View v, int pos, long arg3) {
		// TODO Auto-generated method stub
		kog.e("KDH", "pos = "+pos);
//		mCorCardModels.get(pos).setCheckFlag(!mCorCardModels.get(pos).isCheckFlag());
//		mCorCardAdapter.notifyDataSetChanged();

		Cor_Card_Detail_Dialog tldd = new Cor_Card_Detail_Dialog(mContext, mCorCardModels.get(pos), mHandler);
		tldd.show();
	}

	Handler mHandler = new Handler()
	{

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			kog.e("KDH", "handleMessage");
			if(msg != null){
				// 갱신 필요시
				if(msg.what == MESSAGE_REFRESH){
					clickSearch(0);
				}
			}
			super.handleMessage(msg);
		}

	};

	private void showFilterPopup(BaseTextPopup popup, View anchor, boolean flag) {

		popup.show(anchor);
		popup.setOnSelectedItem(this);
	}

	@Override
	public void onSelectedItem(int position, String popName) {

		LinkedHashMap<String, String> linkedHashMap = null;

		if (popName.equals(STATUS_FILTER)) {
			linkedHashMap = mStatusFilterMap;
			mStatus = getSeletedItem(linkedHashMap, position, btn_status);
		} else if (popName.equals(USETYPE_FILTER)) {
			linkedHashMap = mUsetypeFilterMap;
			mUseType = getSeletedItem(linkedHashMap, position, btn_usetype);
		} else if (popName.equals(PAGE_FILTER)) {
			linkedHashMap = mPageFilterMap;
			int page = Integer.valueOf(mPage);
			page--;
			if(page != position) {
				mPage = getSeletedItem(linkedHashMap, position, btn_move_page);
				clickSearch(0);
			}
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
}
