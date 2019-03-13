package com.ktrental.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.ktrental.R;
import com.ktrental.adapter.MovementAdapter;
import com.ktrental.cm.connect.ConnectController;
import com.ktrental.cm.connect.Connector.ConnectInterface;
import com.ktrental.dialog.Camera_Dialog;
import com.ktrental.model.MovementModel;
import com.ktrental.model.TableModel;
import com.ktrental.popup.DatepickPopup2;
import com.ktrental.popup.MovementDialog;
import com.ktrental.popup.Popup_Window_DelSearch;
import com.ktrental.util.CommonUtil;
import com.ktrental.util.kog;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

public class MovementFragment extends BaseFragment implements OnClickListener, OnItemClickListener {

	private Button mBtnSearch;
	private MovementAdapter mMovementAdapter;
	private ListView mLvMovement;
	private ArrayList<MovementModel> mMovementModels = new ArrayList<MovementModel>();

	private String mEQUNR;
	private String mDRIVER;
	private String mAUFNR;
	private String mCarNum;
	//Jonathan 14.07.31 추가
	private String mINVNR;

	private String mDATUMF;
	private String mDATUMT;


	private Button mBtnCarNum;
	private Button mBtnAufnr;
	private EditText mBtnName;
	private String mOilType;
	private String mDriverName;
	private boolean mInitFlag = false;

	private MovementDialog mMovementDialog;

	private Button mBtnStart;

	private ImageView mIvEmpty;

	private Popup_Window_DelSearch pwDelSearch;


	private Button datepick1_bt, datepick2_bt;
	private DatepickPopup2 idatepickpopup;


	ConnectController mCc;


	private TextView tv_sum_OILPRI;
	private TextView tv_sum_WASH;
	private TextView tv_sum_PARK;
	private TextView tv_sum_TOLL;
	private TextView tv_sum_OTRAMT;



	String P2 = "P2";

	public MovementFragment(){
	}

	public MovementFragment(String mEQUNR, String mDRIVER, String mAUFNR,
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
		mRootView = (View) inflater.inflate(R.layout.movement_fragment_layout,
				null);

		mBtnSearch = (Button) mRootView.findViewById(R.id.btn_search);
		mBtnSearch.setOnClickListener(this);

		TextView tvTitle = (TextView) mRootView
				.findViewById(R.id.tv_common_title);
		tvTitle.setText("차량운행일지 조회");

		mMovementAdapter = new MovementAdapter(mContext);
		mLvMovement = (ListView) mRootView.findViewById(R.id.lv_movement);
		mLvMovement.setAdapter(mMovementAdapter);
		mLvMovement.setOnItemClickListener(this);
		mBtnCarNum = (Button) mRootView.findViewById(R.id.btn_movement_carnum);
		mBtnCarNum.setText(mCarNum);
		mBtnCarNum.setOnClickListener(this);
		mBtnCarNum.setEnabled(mInitFlag);

		mBtnAufnr = (Button) mRootView.findViewById(R.id.btn_movement_number);
		mBtnAufnr.setText(mAUFNR);
		mBtnName = (EditText) mRootView.findViewById(R.id.btn_movement_name);
		mBtnName.setText(mDriverName);
		mBtnName.setEnabled(mInitFlag);

		mBtnStart = (Button) mRootView.findViewById(R.id.btn_movement_start);
		mBtnStart.setOnClickListener(this);


		datepick1_bt = (Button)mRootView.findViewById(R.id.datepick1_bt);
		datepick1_bt.setOnClickListener(this);

		datepick2_bt = (Button)mRootView.findViewById(R.id.datepick2_bt);
		datepick2_bt.setOnClickListener(this);


		tv_sum_OILPRI = (TextView)mRootView.findViewById(R.id.tv_sum_OILPRI);
		tv_sum_WASH = (TextView)mRootView.findViewById(R.id.tv_sum_WASH);
		tv_sum_PARK = (TextView)mRootView.findViewById(R.id.tv_sum_PARK);
		tv_sum_TOLL = (TextView)mRootView.findViewById(R.id.tv_sum_TOLL);
		tv_sum_OTRAMT = (TextView)mRootView.findViewById(R.id.tv_sum_OTRAMT);

//		mCc = new ConnectController(this, mContext);

		setDateButton();

		mIvEmpty = (ImageView) mRootView.findViewById(R.id.iv_empty);

		if (mMovementModels.size() <= 0) {
			mIvEmpty.setVisibility(View.VISIBLE);
			mLvMovement.setVisibility(View.GONE);
		}

		if (P2.equals(getActivity().getIntent().getStringExtra("is_CarManager")))
		{

			mBtnName.setEnabled(false);

		}

		pwDelSearch = new Popup_Window_DelSearch(mContext);
		Button btn_del = (Button)mRootView.findViewById(R.id.btn_del);
		btn_del.setOnClickListener(this);
		return mRootView;
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

			case R.id.datepick1_bt:
				data_get = datepick1_bt.getText().toString();
				date = data_get.replace(".", "");
				idatepickpopup = new DatepickPopup2(mContext, date, 0);
				idatepickpopup.show(datepick1_bt);

				break;



			case R.id.datepick2_bt:
				data_get = datepick2_bt.getText().toString();
				date = data_get.replace(".", "");
				idatepickpopup = new DatepickPopup2(mContext, date, 0);
				idatepickpopup.show(datepick2_bt);

				break;




			case R.id.btn_search:
				clickSearch();
				break;
			case R.id.btn_movement_start:
				clickMovementDialog();
				break;

			// myung 20150106 ADD
			case R.id.btn_movement_carnum:
				// myung 20131129 UPDATE 차량번호 터치 시 지우기, 조회하기 팝업 뛰우기
				Object data = mBtnCarNum.getText().toString();
				if (data != null && !data.toString().equals("")) {
					ViewGroup vg = pwDelSearch.getViewGroup();
					LinearLayout back = (LinearLayout) vg.getChildAt(0);
					final Button del = (Button) back.getChildAt(0);
					del.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							mBtnCarNum.setText("");
							mBtnCarNum.setTag("");
							pwDelSearch.dismiss();
						}
					});
					final Button sel = (Button) back.getChildAt(1);
					sel.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							new Camera_Dialog(mContext).show(mBtnCarNum);
							pwDelSearch.dismiss();
						}
					});
					pwDelSearch.show(mBtnCarNum);
				} else {
					new Camera_Dialog(mContext).show(mBtnCarNum);
				}
				break;
			case R.id.btn_del:
			{
				clickDelete();
			}
			break;
			default:
				break;
		}

	}

	private void clickDelete() {

		if (mMovementModels.size() == 0)
		{
			showEventPopup2(null, "운행내역 조회를 실행 후 삭제해 주세요.");
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
						hideProgress();
						if (MTYPE.equals("S")) {
							// Log.i("####", "####" + "여기1");
							clickSearch();
//							showEventPopup2(new OnEventOkListener() {
//
//								@Override
//								public void onOk() {
//									// TODO Auto-generated method stub
//									dismiss();
//									
//									clickSearch();
//								}
//							}, resultText);
						} else {
							// Log.i("####", "####" + "여기2");
							showEventPopup2(null, resultText);
						}
					}
				}, mContext);
		// Log.i("####", "####보내는시간" + dATUM + "/" + uZEIT);

		ArrayList<HashMap<String, String>> array = new ArrayList<HashMap<String, String>>();

		for (int i=0; i< mMovementModels.size(); i++ )
		{
			MovementModel tmpModel = mMovementModels.get(i);

			if (tmpModel.isCheckFlag())
			{
				String strEquni = tmpModel.getEQUNR();
				String strSeq	= tmpModel.getSEQ();

				HashMap<String, String> map = new HashMap<String, String>();
				map.put("EQUNR", strEquni);
				map.put("SEQ", strSeq);

				array.add(map);
			}
		}

		if (array.size() <= 0)
		{
			showEventPopup2(null, "삭제할 운행내역을 선택해 주세요.");
			return;
		}

		showProgress("차량운행일지를 삭제중 입니다.");

		connectController.deleteMovement(array);
	}

	private void clickMovementDialog() {

		mMovementDialog = new MovementDialog(mContext, mEQUNR, mDRIVER, mAUFNR,
				mOilType, mDriverName, "1", mCarNum);


		mMovementDialog.show();
	}

	private void clickSearch() {

		showProgress("차량운행일지를 조회중 입니다.");




		//Jonathan 14.07.30 
		kog.e("Jonathan", "Move mPermgp ::  " + getActivity().getIntent().getStringExtra("is_CarManager"));
		if (P2.equals(getActivity().getIntent().getStringExtra("is_CarManager")))
		{

			mINVNR = mBtnCarNum.getText().toString();

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
						// TODO Auto-generated method stub
						hideProgress();

						long OILPRI = 0;
						long WASH = 0;
						long PARK = 0;
						long TOLL = 0;
						long OTRAMT = 0;

						tv_sum_OILPRI.setText("0 원");
						tv_sum_WASH.setText("0 원");
						tv_sum_PARK.setText("0 원");
						tv_sum_TOLL.setText("0 원");
						tv_sum_OTRAMT.setText("0 원");

						if (MTYPE.equals("S")) {
							// myung 20131231 리스트 중복 조회 수정
							mMovementModels.clear();
							ArrayList<HashMap<String, String>> array = tableModel.getTableArray();

							for (HashMap<String, String> map : array) {
								MovementModel model = new MovementModel(map
										.get("OTYPE_TX"), map.get("AUFNR"), map
										.get("DRVNAM"), map.get("OSTEP_TX"),
										map.get("DATUM"), map.get("UZEIT"), map
										.get("VKBUR"),
										map.get("MILAG"), map.get("DIVSN_TX"),
										map.get("GDOIL"), map.get("GDOIL2"),
										map.get("EQUNR"), map.get("SEQ"), false,
										map.get("OILPRI"), map.get("WASH"),
										map.get("PARK"), map.get("TOLL"),
										map.get("OTRAMT"), map.get("REASON_TX"),
										map.get("PTRAN2")
								);
								mMovementModels.add(model);

								Set <String> set  = map.keySet();
								Iterator <String> it = set.iterator();
								String key;

								while(it.hasNext())
								{
									key = it.next();
									kog.e("Jonathan", "moveF key ===  " + key + "    value  === " + map.get(key));
									if("OILPRI".equals(key))
									{
										OILPRI = OILPRI + Long.valueOf(map.get(key));
									}

									if("WASH".equals(key))
									{
										WASH = WASH + Long.valueOf(map.get(key));
									}

									if("PARK".equals(key))
									{
										PARK = PARK + Long.valueOf(map.get(key));
									}

									if("TOLL".equals(key))
									{
										TOLL = TOLL + Long.valueOf(map.get(key));
									}

									if("OTRAMT".equals(key))
									{
										OTRAMT = OTRAMT + Long.valueOf(map.get(key));
									}



								}


								kog.e("Jonathan", "합계  OILPRI: " + OILPRI);
								kog.e("Jonathan", "합계  WASH: " + WASH);
								kog.e("Jonathan", "합계  PARK: " + PARK);
								kog.e("Jonathan", "합계  TOLL: " + TOLL);
								kog.e("Jonathan", "합계  OTRAMT: " + OTRAMT);



								tv_sum_OILPRI.setText(CommonUtil.currentpoint(String.valueOf(OILPRI)) + " 원");
								tv_sum_WASH.setText(CommonUtil.currentpoint(String.valueOf(WASH)) + " 원");
								tv_sum_PARK.setText(CommonUtil.currentpoint(String.valueOf(PARK)) + " 원");
								tv_sum_TOLL.setText(CommonUtil.currentpoint(String.valueOf(TOLL)) + " 원");
								tv_sum_OTRAMT.setText(CommonUtil.currentpoint(String.valueOf(OTRAMT)) + " 원");


							}
							if (mMovementModels.size() <= 0) {
								mIvEmpty.setVisibility(View.VISIBLE);
								mLvMovement.setVisibility(View.GONE);
							} else {
								mLvMovement.setVisibility(View.VISIBLE);
								mIvEmpty.setVisibility(View.GONE);
							}



							Collections.sort(mMovementModels, new Comparator<MovementModel>() {

								@Override
								public int compare(MovementModel first, MovementModel second) {
									// TODO Auto-generated method stub
									return second.getUZEIT().compareTo(first.getUZEIT());
								}
							});


							Collections.sort(mMovementModels, new Comparator<MovementModel>() {

								@Override
								public int compare(MovementModel first, MovementModel second) {
									// TODO Auto-generated method stub
									return second.getDATUM().compareTo(first.getDATUM());
								}
							});


							mMovementAdapter.setData(mMovementModels);

						} else {
							showEventPopup2(null, "" + resultText);
						}
					}
				}, mContext);




		if (P2.equals(getActivity().getIntent().getStringExtra("is_CarManager")))
		{
			//Jonathan 14.07.31 카메니저일 경우 차량번호(INVNR)을 던져야 하므로 다른 함수 사용한다.


			mINVNR = mBtnCarNum.getText().toString();
			mDATUMF = datepick1_bt.getText().toString().replace(".", "");
			mDATUMT = datepick2_bt.getText().toString().replace(".", "");
			kog.e("Jonathan", "Movement_ INVNR ::" + mINVNR + "  mAUFNR :: " + mAUFNR + " mDRIVER :: " + mDRIVER );

			connectController.getMovement_is_CarManger(mINVNR, mAUFNR, mDRIVER, mDATUMF, mDATUMT);



		}
		else
		{
			//Jonathan 14.07.31 순회정비일 경우 받아온 것 그대로 던져준다.

			mDATUMF = datepick1_bt.getText().toString().replace(".", "");
			mDATUMT = datepick2_bt.getText().toString().replace(".", "");
			kog.e("Jonathan", "Movement_ mEQUNR ::" + mEQUNR + "  mAUFNR :: " + mAUFNR + " mDRIVER :: " + mDRIVER + " mDATUMF :: " + mDATUMF + " mDATUMT :: " + mDATUMT);

			connectController.getMovement(mEQUNR, mAUFNR, mDRIVER, mDATUMF, mDATUMT);



		}

	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View v, int pos, long arg3) {
		// TODO Auto-generated method stub
		kog.e("KDH", "pos = "+pos);
		mMovementModels.get(pos).setCheckFlag(!mMovementModels.get(pos).isCheckFlag());
		mMovementAdapter.notifyDataSetChanged();
	}

}
