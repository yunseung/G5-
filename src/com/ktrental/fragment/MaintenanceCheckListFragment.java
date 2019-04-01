package com.ktrental.fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.ktrental.R;
import com.ktrental.adapter.Customer_Search_Dialog_Adapter;
import com.ktrental.adapter.MaintenanceCheckListAdapter;
import com.ktrental.cm.connect.ConnectController;
import com.ktrental.cm.connect.Connector.ConnectInterface;
import com.ktrental.common.KtRentalApplication;
import com.ktrental.dialog.Customer_Search_Dialog;
import com.ktrental.dialog.MaintenanceCheckListDialog;
import com.ktrental.dialog.MaintenanceCheckListDialog2;
import com.ktrental.model.BaseMaintenanceModel;
import com.ktrental.model.LoginModel;
import com.ktrental.model.TableModel;
import com.ktrental.popup.DatepickPopup2;
import com.ktrental.util.OnChangeFragmentListener;
import com.ktrental.util.kog;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

public class MaintenanceCheckListFragment extends BaseRepairFragment implements
		ConnectInterface, OnClickListener {

	private ConnectController mCC;
	private Context mContext;
	private ProgressDialog mPDialog;

	private ArrayList<HashMap<String, String>> m1070RD03Array;
	private ArrayList<HashMap<String, String>> O_ITAB1;
	// private HashMap<String, String> O_ITAB2;
//	private NoticeAdapter mNoticeAdapter;
	
//	ListAdapter mNoticeAdapter;
	
	
	private TextView tv_mclf_list_1, tv_mclf_list_2,tv_mclf_list_3, tv_mclf_list_4, 
					tv_mclf_list_5, tv_mclf_list_6, tv_mclf_list_7, tv_mclf_list_8, 
					tv_mclf_list_9,	tv_mclf_list_10, tv_mclf_list_11;
	
	
	private EditText et_search_mtinvnr_mclf;	//순회차량ㅋㅋㅋㅋ
	private EditText et_search_kunnr_mclf;		//고객번호
	private TextView tv_search_kunnr_mclf;		//고객번호
	private EditText et_search_invnr_mclf;		//고객차량
	
	private Button btn_search_frdate_mclf;		//출고일자시작
	private Button btn_search_todate_mclf;		//출고일작끝
	
	private Button bt_search_mclf;		//조회
	private Button bt_search_tmp;		
	
	
	private Customer_Search_Dialog csd;
	
	private DatepickPopup2 idatepickpopup;
	
	private ListView lv_mclf;
	private MaintenanceCheckListAdapter mMaintenanceCheckListAdapter;


	

	private final static String SEARCH_COUNT = "10";
	private int mLastIndex = 1;
	private boolean mHidenFlag = true;
	private boolean mCreateFlag = false;
	private ImageView iv_nodata;

	View footer;

	public MaintenanceCheckListFragment(){
	}

	public MaintenanceCheckListFragment(String name,
			OnChangeFragmentListener mOnChangeFragmentListener) {
		// TODO Auto-generated constructor stub
	}

	public MaintenanceCheckListFragment(String name,
			OnChangeFragmentListener mOnChangeFragmentListener,
			boolean createFlag) {
		// TODO Auto-generated constructor stub
		mCreateFlag = createFlag;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		// Log.i("onCreateView", "onCreateView");
		mRootView = (View) inflater.inflate(R.layout.maintenance_checklist_layout, null);
		
		
		
		lv_mclf = (ListView)mRootView.findViewById(R.id.lv_mclf);
		
		TextView tvTitle = (TextView) mRootView
				.findViewById(R.id.tv_common_title);
		tvTitle.setText("순회정비차량점검표");
		
		et_search_mtinvnr_mclf = (EditText)mRootView.findViewById(R.id.et_search_mtinvnr_mclf);
		et_search_kunnr_mclf = (EditText)mRootView.findViewById(R.id.et_search_kunnr_mclf);
		et_search_invnr_mclf = (EditText)mRootView.findViewById(R.id.et_search_invnr_mclf);
		et_search_kunnr_mclf.setOnClickListener(this);
		
		tv_search_kunnr_mclf = (TextView)mRootView.findViewById(R.id.tv_search_kunnr_mclf);
		
		btn_search_frdate_mclf = (Button)mRootView.findViewById(R.id.btn_search_frdate_mclf);
		btn_search_todate_mclf = (Button)mRootView.findViewById(R.id.btn_search_todate_mclf);
		
		bt_search_mclf = (Button)mRootView.findViewById(R.id.bt_search_mclf);
		bt_search_tmp = (Button)mRootView.findViewById(R.id.bt_search_tmp);


		
//		et_search_mtinvnr_mclf.setText("88머8155");
//		et_search_kunnr_mclf.setText("10027645");
//		et_search_invnr_mclf.setText("30호4796"); 
		
		
		LoginModel lm = KtRentalApplication.getLoginModel();
        String car = lm.getInvnr();
		Log.e("Jonathan", "carNum getBukrs:: " + lm.getBukrs());
		Log.e("Jonathan", "carNum getDeptcd:: " + lm.getDeptcd());
		Log.e("Jonathan", "carNum getDeptnm:: " + lm.getDeptnm());
		Log.e("Jonathan", "carNum getEname:: " + lm.getEname());
		Log.e("Jonathan", "carNum getEqunr:: " + lm.getEqunr());
		Log.e("Jonathan", "carNum getFUELCD:: " + lm.getFUELCD());
		Log.e("Jonathan", "carNum getFUELNM:: " + lm.getFUELNM());
		Log.e("Jonathan", "carNum getINGRP:: " + lm.getINGRP());
		Log.e("Jonathan", "carNum getInvnr:: " + lm.getInvnr());
		Log.e("Jonathan", "carNum getLgort:: " + lm.getLgort());
		Log.e("Jonathan", "carNum getLifnr:: " + lm.getLifnr());
		Log.e("Jonathan", "carNum getPernr:: " + lm.getPernr());
		Log.e("Jonathan", "carNum getScrno:: " + lm.getScrno());


        et_search_mtinvnr_mclf.setText(car);
		
		btn_search_frdate_mclf.setOnClickListener(this);
		btn_search_todate_mclf.setOnClickListener(this);
		bt_search_mclf.setOnClickListener(this);
		bt_search_tmp.setOnClickListener(this);
		
		m1070RD03Array = new ArrayList<HashMap<String,String>>();
		
		setDateButton() ;
		
		
//		mMaintenanceCheckListAdapter = new MaintenanceCheckListAdapter(mContext, R.layout.partstransfer_cars_dialog_row, array_hash);
//		lv_mclf.setAdapter(mMaintenanceCheckListAdapter);
//		lv_mclf.setOnItemClickListener(new OnItemClickListener() {
//			@Override
//			public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
//				mMaintenanceCheckListAdapter.setCheckPosition(position);
//			}
//		});
		
		

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
		btn_search_todate_mclf.setText(year + "." + month_str + "." + day_str);// 오늘날짜
		cal.set(Calendar.DAY_OF_MONTH, 1);
		day = cal.get(Calendar.DAY_OF_MONTH);
		String day_str_1 = String.format("%02d", day);
		// DATE1 = year + month_str + day_str_1;
		btn_search_frdate_mclf.setText(year + "." + month_str + "." + day_str_1);// 이달의첫날
	}
	
	
//	private OnItemClickListener mItemClickListener = new OnItemClickListener() {
//		@Override
//		public void onItemClick(AdapterView<?> arg0, View arg1, int position,
//				long arg3) {
//			kog.e("Jonathan", position + "번째 눌렀음.");
//			showProgress("상세조회 중입니다.");
//			mCC.getZMO_1060_RD07(mRD06Array.get(position).get("AN_NO"));
//
//		}};
		
//	private OnItemClickListener mItemClickListener = new
//			OnItemClickListener() {
//
//		@Override
//		public void onItemClick(AdapterView<?> arg0, View arg1, int position,
//				long arg3) {
//			// TODO Auto-generated method stub
//			kog.e("Jonathan", position + "번째 눌렀음. onItemClick");
//			kog.e("item", String.valueOf(position));
//			// TODO Auto-generated method stub
//			showProgress("상세조회 중입니다.");
//			mCC.getZMO_1060_RD07(mRD06Array.get(position).get("AN_NO"));
//
//		}
//	};

	// private void setDummy() {
	// for (int i = 0; i < 10; i++) {
	// NoticeModel model = new NoticeModel("공지사항 " + i, "20131024", "홍길동 "
	// + i);
	// mNoticeModels.add(model);
	// }
	// mNoticeAdapter.setData(mNoticeModels);
	// }

	
	
	@Override
	public void OnSeletedItem(Object item) {
		// TODO Auto-generated method stub
		kog.e("KDH", "OnSeletedItem");
		super.OnSeletedItem(item);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		// initItem();
	}

	@Override
	public void onClick(View v) {
		
		String data;
		String date;

		switch (v.getId()) {
		
		
		case R.id.bt_search_mclf:
			
			kog.e("Joanthan", "click bt_search_mclf");
			
			clickSearch();
			
			break;

		case R.id.bt_search_tmp:
			// yunseung 여기 호출 안됨.. view 의 상태가 gone 이고,... 풀어주는 곳도 없음.
			showCarSearchDialog("", 1) ;
			
			break;
			
			
		case R.id.et_search_kunnr_mclf:
			
			showCustomersearchDialog();
			
			break;
		
			

			
		case R.id.btn_search_frdate_mclf:
			data = btn_search_todate_mclf.getText().toString();
			date = data.replace(".", "");
			idatepickpopup = new DatepickPopup2(mContext, date, 0);
			idatepickpopup.show(btn_search_frdate_mclf);
			break;
		case R.id.btn_search_todate_mclf:
			data = btn_search_frdate_mclf.getText().toString();
			date = data.replace(".", "");
			idatepickpopup = new DatepickPopup2(mContext, date, 1);
			idatepickpopup.show(btn_search_todate_mclf);
			break;
		
		}
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		// initItem();
		// Log.i("onResume", "onResume");

		super.onResume();
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	@Override
	public void onHiddenChanged(boolean hidden) {
		// TODO Auto-generated method stub
		// Log.i("onHiddenChanged", "onHiddenChanged");
		super.onHiddenChanged(hidden);
//		mHidenFlag = hidden;
//		if (!hidden) {
//			initItem();
//			mEditSearch.setText("");
//		}
	}

	@Override
	public void onAttach(Activity activity) {
		mContext = activity;
		mCC = new ConnectController(this, mContext);
		mPDialog = new ProgressDialog(mContext);
		mPDialog.setCancelable(false);
		super.onAttach(activity);
	}
	
	
	 
//	private void showCarSearchDialog() {
//		final MaintenanceCheckListDialog ptcd = new MaintenanceCheckListDialog(mContext);
//		Button bt_done_car = (Button) ptcd.findViewById(R.id.partstransfer_car_search_done_id);
//		ptcd.setTitle("순회정비차량 점검표");
//		ptcd.setDone("확인");
//
//		bt_done_car.setOnClickListener(new View.OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				ptcd.dismiss();
//			}
//		});
//
//		ptcd.show();
//	}
	

	public void setCarNumSearch() {
		Customer_Search_Dialog_Adapter csda = csd.getCsda();
		if (csda == null)
			return;
		ArrayList<HashMap<String, String>> arr_hash = csd.getArray_hash();
		int position = csda.getCheckPosition();
		HashMap<String, String> map = arr_hash.get(position);
		
		String getKUNNR = map.get("KUNNR");
		String getNAME1 = map.get("NAME1");
		
		
		et_search_kunnr_mclf.setText(getNAME1);
		tv_search_kunnr_mclf.setText(getKUNNR);
		
//		KUNNR
//		NAME1
		csd.dismiss();
		
		
		
	}
	
	
	private void showCustomersearchDialog()
	{
		et_search_kunnr_mclf.setText("");
		tv_search_kunnr_mclf.setText("");
		
		
		csd = new Customer_Search_Dialog(mContext);
		Button bt_done = (Button) csd
				.findViewById(R.id.customer_search_save_id);
		bt_done.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				
				kog.e("Joanthan", "customer search dialog 입력 완료");
				setCarNumSearch();
				
			}
		});
		csd.show();
		
		
	}
	
	
	private void showCarSearchDialog(String aufnr, int type) {
		if(type == 1) {
			final MaintenanceCheckListDialog ptcd = new MaintenanceCheckListDialog(mContext, aufnr);
			Button bt_done_car = (Button) ptcd.findViewById(R.id.partstransfer_car_search_done_id);
			ptcd.setTitle("순회정비차량 점검표");
			ptcd.setDone("확인");

			bt_done_car.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					ptcd.dismiss();
				}
			});

			ptcd.show();
		} else if ( type == 2){
			final MaintenanceCheckListDialog2 ptcd = new MaintenanceCheckListDialog2(mContext, aufnr);
			Button bt_done_car = (Button) ptcd.findViewById(R.id.partstransfer_car_search_done_id);
			ptcd.setTitle("순회정비차량 점검표");
			ptcd.setDone("확인");

			bt_done_car.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					ptcd.dismiss();
				}
			});

			ptcd.show();
		} else if (type == 3) {
//			final MaintenanceCheckListDialog3 ptcd = new MaintenanceCheckListDialog3(mContext, aufnr);
//			Button bt_done_car = (Button) ptcd.findViewById(R.id.partstransfer_car_search_done_id);
//			ptcd.setTitle("순회정비차량 점검표");
//			ptcd.setDone("확인");
//
//			bt_done_car.setOnClickListener(new OnClickListener() {
//				@Override
//				public void onClick(View v) {
//					ptcd.dismiss();
//				}
//			});
//
//			ptcd.show();
		}

	}
	

	private void clickSearch() {
		m1070RD03Array.clear();
		showProgress("조회 중입니다.");
		
//		getZMO_1070_RD03(String _mtinvnr, String _kunnr, String _frdate, String _todate, String _invnr)
		
		
		String mtinvnr = et_search_mtinvnr_mclf.getText().toString();
		String kunnr = tv_search_kunnr_mclf.getText().toString();
		String invnr = et_search_invnr_mclf.getText().toString();
		String frdate = btn_search_frdate_mclf.getText().toString();
		String todate = btn_search_todate_mclf.getText().toString();
		
		
		mCC.getZMO_1070_RD03(mtinvnr, kunnr,frdate, todate, invnr);
		

	}
	
	
	
	
	
	
	
	

	private void initItem() {

		m1070RD03Array.clear();
		mMaintenanceCheckListAdapter.setData(m1070RD03Array);
		showProgress("조회 중입니다.");
//		mCC.getZMO_1060_RD06(Integer.toString(mLastIndex), SEARCH_COUNT);
////		mNoticeAdapter.notifyDataSetChanged();

	}

	private void moreView() {

//		showProgress("조회 중입니다.");
//		mCC.getZMO_1060_RD06(Integer.toString(mLastIndex), SEARCH_COUNT);

	}

	@Override
	public void connectResponse(String FuntionName, String resultText,
			String MTYPE, int resulCode, TableModel tableModel) {
		// TODO Auto-generated method stub
		// Log.i("#", "####" + FuntionName + "/" + resultText + "/" + MTYPE +
		// "/"
		// + resulCode);
		hideProgress();

		if (MTYPE == null || !MTYPE.equals("S")) {
//			mCC.duplicateLogin(mContext);
			
			Toast.makeText(mContext, resultText, 0).show();
			return;
		}
		else if("E".equals(MTYPE))
		{
//			mCC.duplicateLogin(mContext);
			Toast.makeText(mContext, resultText, 0).show();
		}
		
		
		
		if(FuntionName.equals("ZMO_1070_RD03"))
		{
			
			kog.e("Jonathan", "getTableName :: " + tableModel.getTableName());
			lv_mclf.setAdapter(null);
			

			ArrayList<HashMap<String, String>> ET_SLCURPRG = new ArrayList<HashMap<String,String>>(); 
			ET_SLCURPRG = tableModel.getTableArray();
					

			for (int i = 0 ; i < ET_SLCURPRG.size() ; i++) {
				
				Set <String> set  = ET_SLCURPRG.get(i).keySet();
				Iterator <String> it = set.iterator();
				String key;
				
				while(it.hasNext()) 
				{
					key = it.next();
					kog.e("Jonathan", "moveF key ===  " + key + "    value  === " + ET_SLCURPRG.get(i).get(key));
				}
				
			}
			
			if(ET_SLCURPRG.size() < 1)
			{
				
				Toast.makeText(mContext, resultText, Toast.LENGTH_SHORT).show();
				return;
				
			}
			
			
			mMaintenanceCheckListAdapter = new MaintenanceCheckListAdapter(mContext, R.layout.maintenance_checklist_row, ET_SLCURPRG);
			lv_mclf.setAdapter(mMaintenanceCheckListAdapter);
			mMaintenanceCheckListAdapter.notifyDataSetChanged();
			
			final ArrayList<HashMap<String, String>> tmpSlcurprg = ET_SLCURPRG;
			
			lv_mclf.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
					mMaintenanceCheckListAdapter.setCheckPosition(position);
					String cycmnt = tmpSlcurprg.get(position).get("CYCMNT");
					// 2017-11-28. hjt. 순회정비가 아닌 순회점검여부
					boolean check2 = false;
					int type = 1;
					if(cycmnt != null){
						if(cycmnt.contains("점검")){
							type = 2;
						} else if (cycmnt.toLowerCase().trim().contains("iot")) {
							type = 3;
						}
					}
					showCarSearchDialog(tmpSlcurprg.get(position).get("AUFNR"), type) ;
					
					
				}
			});
			
			
//			mCC.duplicateLogin(mContext);
			
			
		}
		
		
		

		if (FuntionName.equals("ZMO_1060_RD06")) {
			mLastIndex += tableModel.getTableArray().size();
			if (tableModel.getTableArray().size() > 0)
				iv_nodata.setVisibility(View.GONE);
//			else
//				iv_nodata.setVisibility(View.VISIBLE);

			if (tableModel.getTableArray().size() > 0)
				setO_ITAB1(tableModel);

		} else if (FuntionName.equals("ZMO_1060_RD07")) {

			O_ITAB1 = tableModel.getTableArray("O_ITAB1");

			for (int i = 0; i < O_ITAB1.size(); i++) 
			{
				HashMap<String, String> hm = O_ITAB1.get(i);

				
				
			}
//			mNoticeAdapter.notifyDataSetChanged();
		}
	}

	private void setO_ITAB1(TableModel tableModel) {

//		mListNotice.setAdapter(mNoticeAdapter);
//		mListNotice.setOnItemClickListener(new OnItemClickListener() {
//
//			@Override
//			public void onItemClick(AdapterView<?> arg0, View arg1,
//					int position, long arg3) {
//				// Log.i("item", String.valueOf(position));
//				// TODO Auto-generated method stub
//				POS = position;
//				kog.e("Jonathan", position + "번째 눌렀음.");
//				showProgress("상세조회 중입니다.");
////				mNoticeAdapter.setPosition(position);
//				mCC.getZMO_1060_RD07(mRD06Array.get(position).get("AN_NO"));
//
//			}
//		});
//
//		showProgress("조회 중입니다.");
//		mRD06Array = tableModel.getTableArray();
//		
//
//		for (int i = 0; i < mRD06Array.size(); i++) {
//			// Log.i("", "####" + mRD06Array.get(i).get("AN_SBM"));
//
//			Set <String> set;
//			Iterator <String> it;
//			String key;
//			
//			
//			set  = mRD06Array.get(i).keySet();
//			it = set.iterator();
//			
//			while(it.hasNext()) 
//			{
//				key = it.next();
//				kog.e("Jonathan", "mRD06Array " + i + " key ===  " + key + "    value  === " + mRD06Array.get(i).get(key));
//			}
//			
//			
//			
//			
//			NoticeModel model = new NoticeModel();
//			model.setTitle(mRD06Array.get(i).get("AN_SBM")); // 공지제목
//			model.setDay(mRD06Array.get(i).get("RG_BDTM")); // 공지시작일
//			model.setDay(mRD06Array.get(i).get("RG_BENO")); // 최초등록사원
//			mNoticeModels.add(model);
//		}
//
////		mNoticeAdapter.setData(mNoticeModels);
//
//		if (!mHidenFlag)
//			if (mRD06Array.size() > 0)
//				mCC.getZMO_1060_RD07(mRD06Array.get(0).get("AN_NO"));
//
//		mNoticeAdapter.notifyDataSetChanged();

	}

	@Override
	public void reDownloadDB(String newVersion) {
		// TODO Auto-generated method stub

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
	
	private int POS = 0;
	 
//	class ListAdapter extends BaseAdapter
//	{
//		LayoutInflater inflater;
//		public ListAdapter(Context ctx)
//		{
//			inflater = (LayoutInflater)ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//		}
//		public int getCount() {
//			// TODO Auto-generated method stub
//			return mNoticeModels.size();
//		}
//
//		public Object getItem(int position) {
//			// TODO Auto-generated method stub
//			return mNoticeModels.get(position);
//		}
//
//		public long getItemId(int position) {
//			// TODO Auto-generated method stub
//			return position;
//		}
//
//		public View getView(int position, View view, ViewGroup parent) {
//			// TODO Auto-generated method stub
//			if(view == null)
//			{
//				view = inflater.inflate(R.layout.notice_row, parent, false);
//			}
//
//			NoticeModel _data = mNoticeModels.get(position);
//			
//			LinearLayout back = (LinearLayout)view.findViewById(R.id.row_back_id);
//			TextView tvNoticeTitle = (TextView) view.findViewById(R.id.tv_list_title);
//			TextView tvDay = (TextView) view.findViewById(R.id.tv_list_day);
//			TextView tvAuthor = (TextView) view.findViewById(R.id.tv_list_author);
//			
//			tvNoticeTitle.setText(_data.getTitle());
//			tvDay.setText(_data.getDay());
//			tvAuthor.setText(_data.getAuthor());
//			
//			if(POS==position)  { back.setBackgroundResource(R.drawable.left_list_customer_s); }
//			else                    { back.setBackgroundResource(R.drawable.left_list_customer_n); }
//			
//			
//			return view;
//		}
//
//	}
	

}
