package com.ktrental.popup;

import android.content.Context;
import android.database.Cursor;
import android.graphics.drawable.ColorDrawable;
import android.provider.MediaStore;
import android.telephony.SmsManager;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.ktrental.R;
import com.ktrental.cm.db.DbAsyncTask;
import com.ktrental.cm.db.DbAsyncTask.DbAsyncResLintener;
import com.ktrental.common.DEFINE;
import com.ktrental.model.BaseMaintenanceModel;
import com.ktrental.model.TableModel;
import com.ktrental.ui.PopupWindowTextView;
import com.ktrental.util.CommonUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;

public class InsureSMSPopup extends BaseTouchDialog implements View.OnClickListener, RadioGroup.OnCheckedChangeListener {

	private Context mContext;

	private HashMap<String, String> mO_struct1;
    private HashMap<String, String> mO_struct2;
    private HashMap<String, String> mO_struct3;
    private HashMap<String, String> mO_struct4;
    private HashMap<String, String> mO_struct5;
	private TextView mTvCareNum; //정비접수번호
	private TextView mTvCarNum; //차량번호
	private TextView mTvCarKind; //차종
	private TextView mTvAcciDate; //사고일시
	private TextView mTvAcciLocation; //대차요청장소
	private TextView mTvAcciContent; //사고내용
	private EditText mEtAcciInsureNum; //보험사접수번호
	private TextView mTvAcciRepairLocation; //수리처
	private TextView mTvCarManager; //카매니저
	private TextView mTvCarManagerPhone; //카매니저 연락처
	private TextView mTvSpName; //보험대차
	private TextView mTvSpPhone; //보험대차 연락처
	private TextView mTvCustomerName; //고객
	private TextView mTvCustomerPhone; //고객 연락처
	private TextView mTvEtc; //기타 요청사항

	private RadioButton mRadio1; // SP담당자, 보험대차
	private RadioButton mRadio2; // 카매니저, 수행자.
	private RadioGroup mRadioGroup; // 라디오 버튼 그룹.

	private FrameLayout mFlayout_carManager1;
	private FrameLayout mFlayout_carManager2;

	private FrameLayout mFlayout_sp1;
	private FrameLayout mFlayout_sp2;


    private String mCusNam;

	private View mRootView;

	private int[] mInventoryLocation = new int[2];

	private boolean firstFlag = true;

	private LinkedHashMap<String, String> mMessageTypeMap = new LinkedHashMap<String, String>();

	private String mSmsNumber = null; // 문자보낼때 수신번호

	public InsureSMSPopup(Context context, HashMap<String, String> o_struct1,
                          HashMap<String, String> o_struct2,
                          HashMap<String, String> o_struct3,
                          HashMap<String, String> o_struct4,
                          HashMap<String, String> o_struct5, String cusnam,
						  String spName,
						  String spPhoneNumber) {
		super(context);

		mContext = context;
        mO_struct1 = o_struct1;
        mO_struct2 = o_struct2;
        mO_struct3 = o_struct3;
        mO_struct4 = o_struct4;
        mO_struct5 = o_struct5;
        mCusNam = cusnam;

		getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.sms_popup_insure);

		mRootView = findViewById(R.id.ll_root);

		TextView tvTitle = (TextView) findViewById(R.id.tv_dialog_title);
		tvTitle.setText("보험대차SP SMS 발송");

		findViewById(R.id.iv_exit).setVisibility(View.INVISIBLE);

		mTvCareNum = (TextView) findViewById(R.id.tv_care_number);
		mTvCarNum = (TextView) findViewById(R.id.tv_carnum);
		mTvCarKind = (TextView) findViewById(R.id.tv_car_kind);
		mTvAcciDate = (TextView) findViewById(R.id.tv_accidate);
		mTvAcciLocation = (TextView) findViewById(R.id.tv_acci_location);
		mTvAcciContent = (TextView) findViewById(R.id.tv_acci_content);
		mEtAcciInsureNum = (EditText) findViewById(R.id.et_acci_insure_num);
		mTvAcciRepairLocation = (TextView) findViewById(R.id.tv_acci_repair_location);

		mFlayout_carManager1 = (FrameLayout)findViewById(R.id.flay_carmanager1);
		mFlayout_carManager2 = (FrameLayout)findViewById(R.id.flay_carmanager2);

		mTvCarManager = (TextView) findViewById(R.id.tv_car_manager);
		mTvCarManagerPhone = (TextView) findViewById(R.id.tv_car_manager_phone);

		mFlayout_sp1 = (FrameLayout)findViewById(R.id.flay_sp1);
		mFlayout_sp2 = (FrameLayout)findViewById(R.id.flay_sp2);

		mTvSpName = (TextView) findViewById(R.id.tv_sp_name);
		mTvSpPhone = (TextView) findViewById(R.id.tv_sp_phone);

		mTvCustomerName = (TextView) findViewById(R.id.tv_customer_name);
		mTvCustomerPhone = (TextView) findViewById(R.id.tv_customer_phone);
		mTvEtc = (TextView) findViewById(R.id.tv_etc);

		mRadio1 = (RadioButton)findViewById(R.id.called_row_rb1);
		mRadio2 = (RadioButton)findViewById(R.id.called_row_rb2);
		mRadioGroup = (RadioGroup)findViewById(R.id.called_row_id3);
		mRadioGroup.setOnCheckedChangeListener(this);

		mTvSpName.setVisibility(View.VISIBLE);
		mTvSpPhone.setVisibility(View.VISIBLE);
		mTvCarManager.setVisibility(View.GONE);
		mTvCarManagerPhone.setVisibility(View.GONE);

		mTvCareNum.setText(mO_struct1.get("AUFNR"));
		mTvCarNum.setText(mO_struct1.get("INVNR"));
		mTvCarKind.setText(mO_struct2.get("MAKTX"));
		mTvAcciDate.setText(mO_struct1.get("ACCDT"));
		mTvAcciLocation.setText(mO_struct1.get("FULL_ADDR2"));
		mTvAcciContent.setText(mO_struct1.get("ACCDES"));
//		mEtAcciInsureNum.setText(model.get("INVNR"));
		mTvAcciRepairLocation.setText(mO_struct2.get("INVNR"));
		mTvSpName.setText(mO_struct1.get("SPLIFNR"));
		mTvSpPhone.setText(mO_struct1.get("SPTEL"));
		mTvCarManager.setText(mO_struct1.get("DAM02N"));
		mTvCarManagerPhone.setText(mO_struct1.get("DAM02HP"));
		mTvCustomerName.setText(mO_struct1.get("DRVNAM"));
		mTvCustomerPhone.setText(mO_struct1.get("DRVHP"));
		mTvEtc.setText("");



//		mTvName.setText(model.);
//		mTvContact.setText(model.getTel());

		findViewById(R.id.btn_cancel).setOnClickListener(this);
		findViewById(R.id.btn_send).setOnClickListener(this);

		WindowManager.LayoutParams params = getWindow().getAttributes();
		params.width = WindowManager.LayoutParams.WRAP_CONTENT;
		getWindow().setAttributes(params);

		getWindow().setBackgroundDrawable(new ColorDrawable(0));
		setCanceledOnTouchOutside(true);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);

		WindowManager.LayoutParams lp = getWindow().getAttributes();

		// WindowManager.LayoutParams lp =
		// getDialog().getWindow().getAttributes();
		lp.dimAmount = 0.6f;
		getWindow().setAttributes(lp);

//		String date = CommonUtil.addDate(model.getDay());

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btn_cancel:
			dismiss();
			break;
		case R.id.btn_send:
			sendSMS();
			dismiss();
			break;
		default:
			break;
		}
	}

	private void sendSMS() {
		StringBuffer sb = new StringBuffer();

		String carenum = mContext.getString(R.string.insure_care_number);
		String carnum = mContext.getString(R.string.insure_carnum);
		String car_kind = mContext.getString(R.string.insure_car_kind);
		String accidate = mContext.getString(R.string.insure_accidate);
		String acci_location = mContext.getString(R.string.insure_acci_location);
		String acci_content = mContext.getString(R.string.insure_acci_content);
		String acci_insure_num = mContext.getString(R.string.insure_acci_insure_num);
		String acci_repair_location = mContext.getString(R.string.insure_acci_repair_location);
		String car_manager = mContext.getString(R.string.insure_car_manager);
		String car_manager_phone = mContext.getString(R.string.insure_car_manager_phone);
		String customer_name = mContext.getString(R.string.insure_customer_name);
		String customer_phone = mContext.getString(R.string.insure_customer_phone);
		String etc = mContext.getString(R.string.insure_etc);

		sb.append(carenum + " : " + mTvCareNum.getText().toString());
		sb.append(System.getProperty( "line.separator"));
		sb.append(carnum + " : " + mTvCarNum.getText().toString());
		sb.append(System.getProperty( "line.separator"));
		sb.append(car_kind + " : " + mTvCarKind.getText().toString());
		sb.append(System.getProperty( "line.separator"));
		sb.append(accidate + " : " + mTvAcciDate.getText().toString());
		sb.append(System.getProperty( "line.separator"));
		sb.append(acci_location + " : " + mTvAcciLocation.getText().toString());
		sb.append(System.getProperty( "line.separator"));
		sb.append(acci_content + " : " + mTvAcciContent.getText().toString());
		sb.append(System.getProperty( "line.separator"));
		sb.append(acci_insure_num + " : " + mEtAcciInsureNum.getText().toString());
		sb.append(System.getProperty( "line.separator"));
		sb.append(acci_repair_location + " : " + mTvAcciRepairLocation.getText().toString());
		sb.append(System.getProperty( "line.separator"));
		sb.append(car_manager + " : " + mTvCarManager.getText().toString());
		sb.append(System.getProperty( "line.separator"));
		sb.append(car_manager_phone + " : " + mTvCarManagerPhone.getText().toString());
		sb.append(System.getProperty( "line.separator"));
		sb.append(customer_name + " : " + mTvCustomerName.getText().toString());
		sb.append(System.getProperty( "line.separator"));
		sb.append(customer_phone + " : " + mTvCustomerPhone.getText().toString());
		sb.append(System.getProperty( "line.separator"));
		sb.append(etc + " : " + mTvEtc.getText().toString());

//		String phoneNumber = mTvContact.getText().toString();
//		String message = mEtMessage.getText().toString();
		SmsManager sms = SmsManager.getDefault();

		String message = String.valueOf(sb);
		if(mSmsNumber != null) {
			sms.sendTextMessage(mSmsNumber, null, message, null, null);
		}
		saveCallLog("SMS", mSmsNumber);
	}


	private String getSeletedItem(LinkedHashMap<String, String> linkedHashMap,
			int position, PopupWindowTextView mTvMessageType2) {

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
				if (mTvMessageType2 != null)
					mTvMessageType2.setText(strKey);
				break;
			}

			i++;
		}

		return value;
	}

	private void saveCallLog(String type, String tel) {
		ArrayList<HashMap<String, String>> arr = new ArrayList<HashMap<String, String>>();
		HashMap<String, String> map = new HashMap<String, String>();
//		map.put("INVNR", mModel.getCarNum());
//		map.put("DRIVN", mModel.getDRIVER_NAME());
//		map.put("DATE", CommonUtil.getCurrentDay());

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

	@Override
	public void dismiss() {
		InputMethodManager imm = (InputMethodManager) mContext
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(getWindow().getCurrentFocus()
				.getWindowToken(), 0);
		super.dismiss();
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		if(checkedId == R.id.called_row_rb1){
			mFlayout_carManager1.setVisibility(View.GONE);
			mFlayout_carManager2.setVisibility(View.GONE);
			mFlayout_sp1.setVisibility(View.VISIBLE);
			mFlayout_sp2.setVisibility(View.VISIBLE);
		} else {
			mFlayout_carManager1.setVisibility(View.VISIBLE);
			mFlayout_carManager2.setVisibility(View.VISIBLE);
			mFlayout_sp1.setVisibility(View.GONE);
			mFlayout_sp2.setVisibility(View.GONE);
		}
	}
}
