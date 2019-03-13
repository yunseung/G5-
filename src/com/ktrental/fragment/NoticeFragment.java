package com.ktrental.fragment;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.ktrental.R;
import com.ktrental.cm.connect.ConnectController;
import com.ktrental.cm.connect.Connector.ConnectInterface;
import com.ktrental.dialog.Mistery_Shopping_Dialog_Login;
import com.ktrental.model.BaseMaintenanceModel;
import com.ktrental.model.NoticeModel;
import com.ktrental.model.TableModel;
import com.ktrental.popup.DatepickPopup2;
import com.ktrental.util.OnChangeFragmentListener;
import com.ktrental.util.kog;

public class NoticeFragment extends BaseRepairFragment implements
		ConnectInterface, OnClickListener {

	private ConnectController mCC;
	private Context mContext;
	private ProgressDialog mPDialog;

	private ArrayList<HashMap<String, String>> mRD06Array;
	private ArrayList<HashMap<String, String>> O_ITAB1;
	// private HashMap<String, String> O_ITAB2;
//	private NoticeAdapter mNoticeAdapter;
	
	ListAdapter mNoticeAdapter;

	// private ListView mLvNotice;
	private Button mBtnSearch;
	private EditText mEditSearch;
	private ListView mListNotice;
//	private Button mBtnMore;

	private TextView mTextNoticeTitle;
	private TextView mTextNoticeBody;
	private TextView mTextDay;
	private TextView mTextAuthor;

	private final static String SEARCH_COUNT = "10";
	private int mLastIndex = 1;
	private boolean mHidenFlag = true;
	private boolean mCreateFlag = false;
	private ImageView iv_nodata;
	
	
	private String I_AN_TY = "";
	private Button btn_notice_bgdt;
	private Button btn_notice_endt;
	private DatepickPopup2 idatepickpopup;
	

//	View footer;

	private ArrayList<NoticeModel> mNoticeModels = new ArrayList<NoticeModel>();

	public NoticeFragment(){

	}

	public NoticeFragment(String name,
			OnChangeFragmentListener mOnChangeFragmentListener) {
		// TODO Auto-generated constructor stub
	}

	public NoticeFragment(String name,
			OnChangeFragmentListener mOnChangeFragmentListener,
			boolean createFlag) {
		// TODO Auto-generated constructor stub
		mCreateFlag = createFlag;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		// Log.i("onCreateView", "onCreateView");
		mRootView = (View) inflater.inflate(R.layout.notice_layout, null);

//		footer = inflater.inflate(R.layout.list_button_footer, null, false);

		TextView tvTitle = (TextView) mRootView
				.findViewById(R.id.tv_common_title);
		tvTitle.setText("공지사항");
		
		
		if ("P2".equals(getActivity().getIntent().getStringExtra("is_CarManager")))
		{//카매니저 공지사항
			I_AN_TY = "Y";
		}
		else
		{//순회정비 공지사항
			I_AN_TY = "M";
		}
			
		
		

		mListNotice = (ListView) mRootView.findViewById(R.id.lv_notice);
		mBtnSearch = (Button) mRootView.findViewById(R.id.btn_search);
		mBtnSearch.setOnClickListener(this);
		mEditSearch = (EditText) mRootView.findViewById(R.id.et_search);
		// mEditSearch.setOnClickListener(this);
		mTextNoticeTitle = (TextView) mRootView
				.findViewById(R.id.tv_notice_title);
		mTextNoticeBody = (TextView) mRootView
				.findViewById(R.id.tv_notice_body);
		mTextDay = (TextView) mRootView.findViewById(R.id.tv_day);
		mTextAuthor = (TextView) mRootView.findViewById(R.id.tv_author);
		iv_nodata = (ImageView) mRootView.findViewById(R.id.list_nodata_id);

//		mNoticeAdapter = new NoticeAdapter(mContext, mListNotice);
		mNoticeAdapter = new ListAdapter(mContext);

//		mBtnMore = (Button) footer.findViewById(R.id.btn_buttonFooter);
//		mBtnMore.setOnClickListener(this);
//		mListNotice.addFooterView(footer);
		mListNotice.setAdapter(mNoticeAdapter);
		
		
		btn_notice_bgdt = (Button)mRootView.findViewById(R.id.btn_notice_bgdt);
		btn_notice_endt = (Button)mRootView.findViewById(R.id.btn_notice_endt);
		
		btn_notice_bgdt.setOnClickListener(this);
		btn_notice_endt.setOnClickListener(this);
		
		setDateButton();
		
		
		
//		mNoticeAdapter.setOnSeletedItem(this);
		
//		 mListNotice.setOnItemClickListener(mItemClickListener);

//		 setEditTextKeyListener();
		 initItem();

//		if (mCreateFlag) {
//			initItem();
//			mCreateFlag = false;
//		}

		if(I_AN_TY != null && I_AN_TY.equals("M")) {
			callMisteryShopping();
		}

		return mRootView;
	}

	private void callMisteryShopping(){
		Mistery_Shopping_Dialog_Login dlg = new Mistery_Shopping_Dialog_Login(mContext);
		dlg.show();
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
//		mBtnSearch.performClick();
	}
	
	
	
	private void setDateButton() {
		Calendar cal = Calendar.getInstance();
		int year = cal.get(Calendar.YEAR);
		int month = cal.get(Calendar.MONTH) + 1;
		int day = cal.get(Calendar.DAY_OF_MONTH);
		String month_str = String.format("%02d", month);
		String day_str = String.format("%02d", day);
		// DATE2 = year + month_str + day_str;
		btn_notice_endt.setText(year + "." + month_str + "." + day_str);// 오늘날짜
		cal.set(Calendar.DAY_OF_MONTH, 1);
		day = cal.get(Calendar.DAY_OF_MONTH);
		String day_str_1 = String.format("%02d", day);
		// DATE1 = year + month_str + day_str_1;
		btn_notice_bgdt.setText(year + "." + month_str + "." + day_str_1);// 이달의첫날
	}
	

	@Override
	public void onClick(View v) {
		
		String data;
		String date;
		

		switch (v.getId()) {
		case R.id.btn_search:
//			if (!mHidenFlag)
				clickSearch();
			break;

		case R.id.btn_buttonFooter:
//			if (!mHidenFlag)
			
			kog.e("Jonathan", "111 btn_buttonFooter");
				moreView();
			break;
			
			
			
		case R.id.btn_notice_bgdt:
			
			data = btn_notice_endt.getText().toString();
			date = data.replace(".", "");
			idatepickpopup = new DatepickPopup2(mContext, date, 0);
			idatepickpopup.show(btn_notice_bgdt);
			
			
			break;
			
			

		case R.id.btn_notice_endt:
			
			data = btn_notice_bgdt.getText().toString();
			date = data.replace(".", "");
			idatepickpopup = new DatepickPopup2(mContext, date, 1);
			idatepickpopup.show(btn_notice_endt);
			
			break;
			
			
			

		// case R.id.et_search:
		//
		// break;
		//
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

//	@Override
//	public void onHiddenChanged(boolean hidden) {
//		// TODO Auto-generated method stub
//		// Log.i("onHiddenChanged", "onHiddenChanged");
//		super.onHiddenChanged(hidden);
//		mHidenFlag = hidden;
//		if (!hidden) {
//			initItem();
//			mEditSearch.setText("");
//		}
//	}

	@Override
	public void onAttach(Activity activity) {
		mContext = activity;
		mCC = new ConnectController(this, mContext);
		mPDialog = new ProgressDialog(mContext);
		mPDialog.setCancelable(false);
		super.onAttach(activity);
	}

	private void clickSearch() {
		mNoticeModels.clear();
//		mNoticeAdapter.setData(mNoticeModels);
		mTextNoticeTitle.setText("");
		mTextNoticeBody.setText("");
		mLastIndex = 1;
		String Search = mEditSearch.getText().toString();
		
		String _from = btn_notice_bgdt.getText().toString();
		String _to = btn_notice_endt.getText().toString();
				
		

		showProgress("조회 중입니다.");
		mCC.getZMO_1060_RD06(Search, I_AN_TY, _from, _to);
		// mListNotice.removeFooterView(footer);
//		footer.setVisibility(View.GONE);
		mNoticeAdapter.notifyDataSetChanged();
		

	}

	private void initItem() {
		
		String _from = btn_notice_bgdt.getText().toString();
		String _to = btn_notice_endt.getText().toString();
		

		mNoticeModels.clear();
//		mNoticeAdapter.setData(mNoticeModels);
		mLastIndex = 1;
		showProgress("조회 중입니다.");
		mCC.getZMO_1060_RD06(Integer.toString(mLastIndex), SEARCH_COUNT, I_AN_TY, _from, _to);
//		footer.setVisibility(View.VISIBLE);
		mNoticeAdapter.notifyDataSetChanged();

	}

	private void moreView() {
		
		String _from = btn_notice_bgdt.getText().toString();
		String _to = btn_notice_endt.getText().toString();

		showProgress("조회 중입니다.");
		mCC.getZMO_1060_RD06(Integer.toString(mLastIndex), SEARCH_COUNT, I_AN_TY, _from, _to);

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
		

		if (FuntionName.equals("ZMO_1060_RD06")) {
			mLastIndex += tableModel.getTableArray().size();
			if (tableModel.getTableArray().size() > 0)
				iv_nodata.setVisibility(View.GONE);
//			else
//				iv_nodata.setVisibility(View.VISIBLE);

			if (tableModel.getTableArray().size() > 0)
				setO_ITAB1(tableModel);
			
//			mCC.duplicateLogin(mContext);

		} else if (FuntionName.equals("ZMO_1060_RD07")) {

			O_ITAB1 = tableModel.getTableArray("O_ITAB1");

			for (int i = 0; i < O_ITAB1.size(); i++) 
			{
				HashMap<String, String> hm = O_ITAB1.get(i);

				
				
				mTextNoticeTitle.setText(hm.get("AN_SBM").toString());
				mTextNoticeBody.setText(hm.get("AN_DES").toString());
				mTextDay.setText(hm.get("AN_BGDT").toString());
				mTextAuthor.setText(hm.get("RG_BENO").toString());
			}
			mNoticeAdapter.notifyDataSetChanged();
			
//			mCC.duplicateLogin(mContext);
			
		}
	}

	private void setO_ITAB1(TableModel tableModel) {

		mListNotice.setAdapter(mNoticeAdapter);
		mListNotice.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				// Log.i("item", String.valueOf(position));
				// TODO Auto-generated method stub
				POS = position;
				kog.e("Jonathan", position + "번째 눌렀음.");
				showProgress("상세조회 중입니다.");
//				mNoticeAdapter.setPosition(position);
				mCC.getZMO_1060_RD07(mRD06Array.get(position).get("AN_NO"));

			}
		});

		showProgress("조회 중입니다.");
		mRD06Array = tableModel.getTableArray();
		

		for (int i = 0; i < mRD06Array.size(); i++) {
			// Log.i("", "####" + mRD06Array.get(i).get("AN_SBM"));

			Set <String> set;
			Iterator <String> it;
			String key;
			
			
			set  = mRD06Array.get(i).keySet();
			it = set.iterator();
			
			while(it.hasNext()) 
			{
				key = it.next();
				kog.e("Jonathan", "mRD06Array " + i + " key ===  " + key + "    value  === " + mRD06Array.get(i).get(key));
			}
			
			
			
			
			NoticeModel model = new NoticeModel();
			model.setTitle(mRD06Array.get(i).get("AN_SBM")); // 공지제목
			model.setDay(mRD06Array.get(i).get("RG_BDTM")); // 공지시작일
			model.setDay(mRD06Array.get(i).get("RG_BENO")); // 최초등록사원
			mNoticeModels.add(model);
		}

//		mNoticeAdapter.setData(mNoticeModels);

//		if (!mHidenFlag)
			if (mRD06Array.size() > 0)
				mCC.getZMO_1060_RD07(mRD06Array.get(0).get("AN_NO"));

		mNoticeAdapter.notifyDataSetChanged();

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
	 
	class ListAdapter extends BaseAdapter
	{
		LayoutInflater inflater;
		public ListAdapter(Context ctx)
		{
			inflater = (LayoutInflater)ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}
		public int getCount() {
			// TODO Auto-generated method stub
			return mNoticeModels.size();
		}

		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return mNoticeModels.get(position);
		}

		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		public View getView(int position, View view, ViewGroup parent) {
			// TODO Auto-generated method stub
			if(view == null)
			{
				view = inflater.inflate(R.layout.notice_row, parent, false);
			}

			NoticeModel _data = mNoticeModels.get(position);
			
			LinearLayout back = (LinearLayout)view.findViewById(R.id.row_back_id);
			TextView tvNoticeTitle = (TextView) view.findViewById(R.id.tv_list_title);
			TextView tvDay = (TextView) view.findViewById(R.id.tv_list_day);
			TextView tvAuthor = (TextView) view.findViewById(R.id.tv_list_author);
			
			tvNoticeTitle.setText(_data.getTitle());
			tvDay.setText(_data.getDay());
			tvAuthor.setText(_data.getAuthor());
			
			if(POS==position)  { back.setBackgroundResource(R.drawable.left_list_customer_s); }
			else                    { back.setBackgroundResource(R.drawable.left_list_customer_n); }
			
			
			return view;
		}

	}
	

}
