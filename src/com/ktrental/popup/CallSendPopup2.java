package com.ktrental.popup;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.ktrental.R;
import com.ktrental.cm.db.DbAsyncTask;
import com.ktrental.cm.db.DbAsyncTask.DbAsyncResLintener;
import com.ktrental.common.DEFINE;
import com.ktrental.model.BaseMaintenanceModel;
import com.ktrental.model.TableModel;
import com.ktrental.util.CommonUtil;
import com.ktrental.util.OnEventOkCallListener;
import com.ktrental.util.kog;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class CallSendPopup2 extends BaseTouchDialog implements
		View.OnClickListener {

	private BaseMaintenanceModel mModel;
	private TextView mTvCustomCall;
	private View mRootView;
	
    TextView tvHp;
    TextView tvHome;
    
    String m_strName;
    String m_strCarNum;
    
    
    String AUFNR;
    Context mcontext;
    Activity AC;
    
    HashMap<String, String> hashMap = new HashMap<String, String>();
    
    private OnEventOkCallListener mOnEventPopupListener;
    

    public CallSendPopup2(Context context) {
		super(context);

		
		
//		mModel = model;
		getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.call_popup);

		mRootView = findViewById(R.id.ll_root);

		TextView tvTitle = (TextView) findViewById(R.id.tv_dialog_title);
		tvTitle.setText("전화 걸기");

		findViewById(R.id.iv_exit).setVisibility(View.INVISIBLE);
		mTvCustomCall = (TextView) findViewById(R.id.tv_custom);
		mTvCustomCall.setOnClickListener(this);
		findViewById(R.id.btn_call_hp).setOnClickListener(this);
		findViewById(R.id.btn_call_home).setOnClickListener(this);
		findViewById(R.id.btn_call_custom).setOnClickListener(this);
		findViewById(R.id.btn_cancel).setOnClickListener(this);

		WindowManager.LayoutParams params = getWindow().getAttributes();
		params.width = WindowManager.LayoutParams.WRAP_CONTENT;
		params.y = -40;
		getWindow().setAttributes(params);

		tvHp = (TextView) findViewById(R.id.tv_hp);
		tvHome = (TextView) findViewById(R.id.tv_home);
		
//		tvHp.setText(model.getDrv_mob());
//		tvHome.setText(model.getTel());

		getWindow().setBackgroundDrawable(new ColorDrawable(0));
		setCanceledOnTouchOutside(true);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);

		WindowManager.LayoutParams lp = getWindow().getAttributes();

		// WindowManager.LayoutParams lp =
		// getDialog().getWindow().getAttributes();
		lp.dimAmount = 0.6f;
		getWindow().setAttributes(lp);
	}
	
    
    
    
	public CallSendPopup2(Context context, String aufnr, HashMap<String, String> setData, Activity ac, OnEventOkCallListener onEventPopupListener) {
		super(context);

		
		mOnEventPopupListener = onEventPopupListener;
		kog.e("Jonathan", "CallSendPopup2 2");
		
		getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.call_popup);

		mRootView = findViewById(R.id.ll_root);

		hashMap = setData;
		AUFNR = aufnr;
		mcontext = context;
		AC = ac;
		
		kog.e("Jonathan", "1call_time : AC :: " + String.valueOf(this));
		
		TextView tvTitle = (TextView) findViewById(R.id.tv_dialog_title);
		tvTitle.setText("전화 걸기");

		findViewById(R.id.iv_exit).setVisibility(View.INVISIBLE);
		mTvCustomCall = (TextView) findViewById(R.id.tv_custom);
		mTvCustomCall.setOnClickListener(this);
		findViewById(R.id.btn_call_hp).setOnClickListener(this);
		findViewById(R.id.btn_call_home).setOnClickListener(this);
		findViewById(R.id.btn_call_custom).setOnClickListener(this);
		findViewById(R.id.btn_cancel).setOnClickListener(this);

		WindowManager.LayoutParams params = getWindow().getAttributes();
		params.width = WindowManager.LayoutParams.WRAP_CONTENT;
		params.y = -40;
		getWindow().setAttributes(params);

		tvHp = (TextView) findViewById(R.id.tv_hp);
		tvHome = (TextView) findViewById(R.id.tv_home);
		
//		tvHp.setText(model.getDrv_mob());
//		tvHome.setText(model.getTel());

		getWindow().setBackgroundDrawable(new ColorDrawable(0));
		setCanceledOnTouchOutside(true);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);

		WindowManager.LayoutParams lp = getWindow().getAttributes();

		// WindowManager.LayoutParams lp =
		// getDialog().getWindow().getAttributes();
		lp.dimAmount = 0.6f;
		getWindow().setAttributes(lp);
	}
	
	
	
	public CallSendPopup2(Context context, String aufnr, HashMap<String, String> setData, Activity ac) {
		super(context);

//		mModel = model;
		getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.call_popup);

		mRootView = findViewById(R.id.ll_root);

		hashMap = setData;
		AUFNR = aufnr;
		mcontext = context;
		AC = ac;
		
		kog.e("Jonathan", "1call_time : AC :: " + String.valueOf(this));
		
		TextView tvTitle = (TextView) findViewById(R.id.tv_dialog_title);
		tvTitle.setText("전화 걸기");

		findViewById(R.id.iv_exit).setVisibility(View.INVISIBLE);
		mTvCustomCall = (TextView) findViewById(R.id.tv_custom);
		mTvCustomCall.setOnClickListener(this);
		findViewById(R.id.btn_call_hp).setOnClickListener(this);
		findViewById(R.id.btn_call_home).setOnClickListener(this);
		findViewById(R.id.btn_call_custom).setOnClickListener(this);
		findViewById(R.id.btn_cancel).setOnClickListener(this);

		WindowManager.LayoutParams params = getWindow().getAttributes();
		params.width = WindowManager.LayoutParams.WRAP_CONTENT;
		params.y = -40;
		getWindow().setAttributes(params);

		tvHp = (TextView) findViewById(R.id.tv_hp);
		tvHome = (TextView) findViewById(R.id.tv_home);
		
//		tvHp.setText(model.getDrv_mob());
//		tvHome.setText(model.getTel());

		getWindow().setBackgroundDrawable(new ColorDrawable(0));
		setCanceledOnTouchOutside(true);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);

		WindowManager.LayoutParams lp = getWindow().getAttributes();

		// WindowManager.LayoutParams lp =
		// getDialog().getWindow().getAttributes();
		lp.dimAmount = 0.6f;
		getWindow().setAttributes(lp);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
		long now = System.currentTimeMillis();
		Date date = new Date(now);
		SimpleDateFormat CurDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String strCurDate = CurDateFormat.format(date);
		String Call_Time_Exist;
		
		String AUFNR =  "";
		String KUNNR =  "";
		String INVNR =  "";
		String ERDAT = "";
		String ERZET = "";
		
		if(!hashMap.isEmpty())
		{
			AUFNR =  hashMap.get("AUFNR").toString();
			KUNNR =  hashMap.get("KUNNR").toString();
			INVNR =  hashMap.get("INVNR").toString();
			ERDAT = strCurDate.substring(0, 10);
			ERZET = strCurDate.substring(11, strCurDate.length()).replaceAll(":", "");
			
		}
		
		
		
		
		switch (v.getId()) {
		
		
		case R.id.tv_custom:
			clickCustom();
			break;
		case R.id.btn_call_hp:
			
			
			now = System.currentTimeMillis();
			date = new Date(now);
			CurDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			strCurDate = CurDateFormat.format(date);
			
//			kog.e("Jonathan", "2call_time : AC :: " + String.valueOf(AC));
//			kog.e("Jonathan", "2call_time AUFNR :: " + AUFNR);
//			
//			if(AUFNR != null)
//			{
//				
//				Call_Time_Exist = Common.FileRead(AUFNR, AC);
//				
//				
//				if((Call_Time_Exist == null) || "".equals(Call_Time_Exist) || " ".equals(Call_Time_Exist))
//				{
//					// 전화한 시간이 없으면 아무것도 안하고 
//					Common.FileWrite(AUFNR, strCurDate, AC);
//				}
//				else
//				{
//					// 전화한 시간이 있으면 아무것도 안함
//					
//				}
//			}
			
			
			clickOk(R.id.btn_call_hp, AUFNR, ERDAT, ERZET, KUNNR, INVNR);
			
//			clickHp();
			
			
			
			
			break;
		case R.id.btn_call_home:
			
			
			
			now = System.currentTimeMillis();
			date = new Date(now);
			CurDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			strCurDate = CurDateFormat.format(date);
			
//			kog.e("Jonathan", "3call_time : AC :: " + String.valueOf(AC));
//			
//			
//			if(AUFNR != null)
//			{
//				Call_Time_Exist = Common.FileRead(AUFNR, AC);
//				
//				
//				if((Call_Time_Exist == null) || "".equals(Call_Time_Exist) || " ".equals(Call_Time_Exist))
//				{
//					// 전화한 시간이 없으면 아무것도 안하고 
//					Common.FileWrite(AUFNR, strCurDate, AC);
//				}
//				else
//				{
//					// 전화한 시간이 있으면 아무것도 안함
//					
//				}
//				
//			}
			
			
			
			kog.e("Jonathan", "AUFNR : " + AUFNR + " KUNNR : " + KUNNR + " INVNR : " + INVNR + " ERDAT : " + ERDAT + " ERZET : " + ERZET);
			kog.e("Jonathan", "CallSendPopup2 3");
			
			
			clickOk(R.id.btn_call_home, AUFNR, ERDAT, ERZET, KUNNR, INVNR);
			
//			clickHome();
			
			
			
			
			break;
		case R.id.btn_call_custom:
			
			
			
			now = System.currentTimeMillis();
			date = new Date(now);
			CurDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			strCurDate = CurDateFormat.format(date);
			
			kog.e("Jonathan", "4call_time : AC :: " + strCurDate);
			
			
//			if(AUFNR != null)
//			{
//				Call_Time_Exist = Common.FileRead(AUFNR, AC);
//				
//				if((Call_Time_Exist == null) || "".equals(Call_Time_Exist) || " ".equals(Call_Time_Exist))
//				{
//					// 전화한 
//					Common.FileWrite(AUFNR, strCurDate, AC);
//				}
//				else
//				{
//					// 전화한 시간이 있으면 아무것도 안함
//					
//				}
//				
//			}
			
			
			
			kog.e("Jonathan", "AUFNR : " + AUFNR + " KUNNR : " + KUNNR + " INVNR : " + INVNR + " ERDAT : " + ERDAT + " ERZET : " + ERZET);
			kog.e("Jonathan", "CallSendPopup2 3");
			
			
			clickOk(R.id.btn_call_custom, AUFNR, ERDAT, ERZET, KUNNR, INVNR);
			
//			clickCallCustom();
			
			
			
			
			
			
			break;
		case R.id.btn_cancel:
			dismiss();
			break;
		default:
			break;
		}
	}
	
	
	private void clickOk(int bt_id, String AUFNR, String ERDAT, String ERZET, String KUNNR, String INVNR) {
//		kog.e("Jonathan", "CallSendPopup2 3 " + AUFNR + ERDAT + ERZET + KUNNR + INVNR + "  :::  " + mOnEventPopupListener.toString());
		
		if (mOnEventPopupListener != null)
		{
			mOnEventPopupListener.onOk(AUFNR, ERDAT, ERZET, KUNNR, INVNR);
		}

		kog.e("Jonathan", "CallSendPopup2 4");
		

		if(bt_id == R.id.btn_call_hp)
		{
			clickHp();
		}
		else if (bt_id == R.id.btn_call_home)
		{
			clickHome();
		}
		else if (bt_id == R.id.btn_call_custom)
		{
			clickCallCustom();
		}
			
		
//		dismiss();
	}
	
	

	private void clickCustom() {
		InventoryPopup inventoryPopup = new InventoryPopup(mContext,
				QuickAction.HORIZONTAL, R.layout.inventory_popup,
				InventoryPopup.TYPE_PHONE_NUMBER);
		inventoryPopup.show(mTvCustomCall, mRootView.getMeasuredWidth(),
				mRootView.getMeasuredHeight() + 80);
		inventoryPopup
				.setOnDismissListener(new InventoryPopup.OnDismissListener() {

					@Override
					public void onDismiss(String result, int position) {
						// TODO Auto-generated method stub
						mTvCustomCall.setText(result);
					}
				});
	}

	private void clickHp() {
		//myung 20131218 UPDATE 고객조회->고객전화연결-> 앱종료
		String callNum = tvHp.getText().toString();
//		String callNum = mModel.getDrv_mob();
		CommonUtil.callAction(mContext, callNum);
		saveCallLog("핸드폰", callNum);
//		dismiss();
	}

	private void clickHome() {
		//myung 20131218 UPDATE 고객조회->고객전화연결-> 앱종료
		String callNum = tvHome.getText().toString();
//		String callNum = mModel.getTel();
		CommonUtil.callAction(mContext, callNum);
		saveCallLog("집", callNum);
//		dismiss();
	}

	private void clickCallCustom() {
		String callNum = mTvCustomCall.getText().toString();
		CommonUtil.callAction(mContext, callNum);
		saveCallLog("직접입력", callNum);
//		dismiss();
	}

	private void saveCallLog(String type, String tel) {
		ArrayList<HashMap<String, String>> arr = new ArrayList<HashMap<String, String>>();
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("INVNR", m_strCarNum);
		map.put("DRIVN", m_strName);
		map.put("DATE", CommonUtil.getCurrentDay());
		
		//Log.i("========== SAVE CALLLOG ============", "Name = " + m_strName + " CARNUM = " + m_strCarNum);

		String strNow = "";
		// 현재 시간을 msec으로 구한다.
		long now = System.currentTimeMillis();
		// 현재 시간을 저장 한다.
		Date date = new Date(now);
		// 시간 포맷으로 만든다.
		SimpleDateFormat sdfNow = new SimpleDateFormat("HHmm");
		strNow = sdfNow.format(date);

		map.put("TIME", strNow);
		map.put("TYPE", type);
		map.put("TEL", tel);
		arr.add(map);

		TableModel tableModel = new TableModel(DEFINE.CALL_LOG_TABLE_NAME, arr,
				"");

		DbAsyncTask task = new DbAsyncTask("saveCallLog",
				DEFINE.CALL_LOG_TABLE_NAME, mContext, new DbAsyncResLintener() {

					@Override
					public void onCompleteDB(String funName, int type,
							Cursor cursor, String tableName) {
						// TODO Auto-generated method stub

					}
				}, tableModel);
		task.execute(DbAsyncTask.DB_TABLE_CREATE);
	}
	
	public void show(String phone1, String phone2)
	    {
	    tvHp.setText(phone1);
	    tvHome.setText(phone2);
	    super.show();
	    }
	
	public void show(String phone1, String phone2, String strCarNum, String strName)
	{
		m_strName 	= strName;
		m_strCarNum = strCarNum;
	    tvHp.setText(phone1);
	    tvHome.setText(phone2);
	    super.show();
	}



}
