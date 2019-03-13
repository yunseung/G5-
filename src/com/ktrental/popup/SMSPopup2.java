package com.ktrental.popup;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;

import android.content.Context;
import android.database.Cursor;
import android.graphics.drawable.ColorDrawable;
import android.telephony.SmsManager;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.ktrental.R;
import com.ktrental.cm.db.DbAsyncTask;
import com.ktrental.cm.db.DbAsyncTask.DbAsyncResLintener;
import com.ktrental.common.DEFINE;
import com.ktrental.model.BaseMaintenanceModel;
import com.ktrental.model.TableModel;
import com.ktrental.popup.BaseTextPopup.OnSelectedPopupItem;
import com.ktrental.ui.PopupWindowTextView;
import com.ktrental.ui.PopupWindowTextView.OnLayoutListener;
import com.ktrental.util.CommonUtil;

public class SMSPopup2 extends BaseTouchDialog implements View.OnClickListener {

	private BaseMaintenanceModel mModel;
	private TextView mTvCarNum;
	private TextView mTvName;
	private TextView mTvContact;
	private EditText mEtMessage;
	private View mRootView;
	private PopupWindowTextView mTvMessageType;
	private String[] mArrMessage = { " 고객님의 차량 정기점검(순회)이 예약되었습니다.",

	" 예정된 고객님의 차량 정기점검(순회)이 연기되었습니다.  추후 다시 연락 드리겠습니다. ",

	"고객님의 차량 정기점검(순회)이 ",

	"고객님의 차량 정기점검(순회)을 위해 소재지에 도착 하였습니다. 연락 주시기 바랍니다.",

	"고객님의 차량 정기점검(순회)이 완료 되었습니다. 자세한 점검 사항은 MMS 확인 바랍니다.",

	"" };

	private int[] mInventoryLocation = new int[2];

	private boolean firstFlag = true;

	private LinkedHashMap<String, String> mMessageTypeMap = new LinkedHashMap<String, String>();

	public SMSPopup2(Context context) {
    super(context);

//    mModel = model;
    getWindow().requestFeature(Window.FEATURE_NO_TITLE);
    setContentView(R.layout.sms_popup);

    mRootView = findViewById(R.id.ll_root);

    TextView tvTitle = (TextView) findViewById(R.id.tv_dialog_title);
    tvTitle.setText("SMS 발송");

    findViewById(R.id.iv_exit).setVisibility(View.INVISIBLE);
    mTvCarNum = (TextView) findViewById(R.id.tv_carnum);
    mTvName = (TextView) findViewById(R.id.tv_name);
    mTvContact = (TextView) findViewById(R.id.tv_contact);
    mEtMessage = (EditText) findViewById(R.id.et_message);
    mTvMessageType = (PopupWindowTextView) findViewById(R.id.tv_message_type);
    mTvMessageType.setOnClickListener(this);
    mTvMessageType.setText("직접입력");

   
    findViewById(R.id.btn_cancel).setOnClickListener(this);
    findViewById(R.id.btn_send).setOnClickListener(this);

    mTvMessageType.setOnLayoutListener(new OnLayoutListener() {

        @Override
        public void onLayout() {
            // TODO Auto-generated method stub
            if (firstFlag) {
                mTvMessageType.getLocationOnScreen(mInventoryLocation);
                firstFlag = false;
            }
        }
    });

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
    
    Calendar cal = Calendar.getInstance();
    int year    = cal.get(Calendar.YEAR);
    int month   = cal.get(Calendar.MONTH)+1;
    int day     = cal.get(Calendar.DAY_OF_MONTH);
    
    String month_str    = String.format("%02d", month);
    String day_str      = String.format("%02d", day);

    String date = CommonUtil.addDate(year+month_str+day_str);

    mArrMessage[0] = date + mArrMessage[0];
    mArrMessage[1] = date + mArrMessage[1];
    mArrMessage[2] = date + "로 변경되었습니다.";

    mMessageTypeMap.put("고객 스케줄 예약시", "");
    mMessageTypeMap.put("고객 스케줄 취소시", "");
    mMessageTypeMap.put("고객 스케줄 변경시", "");
    mMessageTypeMap.put("고객 소재지 도착시(통화불가시)", "");
    mMessageTypeMap.put("차량 점검 완료 후(통화불가시)", "");
    mMessageTypeMap.put("직접 입력", "");

}


	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.tv_message_type:
			clickMessageType();
			break;
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
		String phoneNumber = mTvContact.getText().toString();
		String message = mEtMessage.getText().toString();
		SmsManager sms = SmsManager.getDefault();
		sms.sendTextMessage(phoneNumber, null, message, null, null);
		saveCallLog("SMS", phoneNumber);
	    }

	private void clickMessageType() {
		BaseTextPopup pop = new BaseTextPopup(mContext, mMessageTypeMap,
				"MessageType");
		pop.setOnSelectedItem(new OnSelectedPopupItem() {

			@Override
			public void onSelectedItem(int position, String popName) {
				// TODO Auto-generated method stub
				if (popName.equals("MessageType")) {
					getSeletedItem(mMessageTypeMap, position, mTvMessageType);
					mEtMessage.setText(mArrMessage[position]);
					int len = mEtMessage.length();
					len = mEtMessage.length();
					mEtMessage.setSelection(len);
					mEtMessage.requestFocus();
					InputMethodManager imm = (InputMethodManager) mContext
							.getSystemService(Context.INPUT_METHOD_SERVICE);
					imm.showSoftInputFromInputMethod(
							mEtMessage.getApplicationWindowToken(),
							InputMethodManager.SHOW_FORCED);
				}
			}
		});
		pop.show(mTvMessageType, mRootView.getMeasuredWidth(),
				mRootView.getMeasuredHeight(), mInventoryLocation);
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
		map.put("INVNR", mTvCarNum.getText().toString());
		map.put("DRIVN", mTvName.getText().toString());
		map.put("DATE", CommonUtil.getCurrentDay());

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

	public void show(String carnum, String name, String phone)
	    {
	    mTvCarNum.setText(carnum);
	    mTvName.setText(name);
	    mTvContact.setText(phone);

	    super.show();
	    }
}
