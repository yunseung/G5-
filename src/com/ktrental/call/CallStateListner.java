package com.ktrental.call;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.RemoteException;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.TextView;

import com.android.internal.telephony.ITelephony;
import com.ktrental.R;
import com.ktrental.cm.connect.ConnectController;
import com.ktrental.cm.connect.ConnectorUtil;
import com.ktrental.cm.db.DbAsyncTask;
import com.ktrental.cm.db.DbAsyncTask.DbAsyncResLintener;
import com.ktrental.common.DEFINE;
import com.ktrental.common.KtRentalApplication;
import com.ktrental.model.DbQueryModel;
import com.ktrental.util.CommonUtil;

import java.lang.reflect.Method;

/**
 * 순회정비대상 고객들에 통화가 오는 경우 호출되는 클래스이다.</br> WindowManager를 통해 최상위 팝업을 띄운다.</br>
 * 
 * @author hongsungil
 */

public class CallStateListner extends PhoneStateListener implements
		View.OnClickListener {

	public static final String TAG = "CallStateListner";

	private static int previousState = TelephonyManager.CALL_STATE_IDLE;

	private ITelephony telephonyService;

	private Context mContext;

	private WindowManager wm = null;
	private LayoutParams wmParams = null;

	private View mRootView = null;

	private Button mBtnOk;
	private Button mBtnNegative;
	private Button mBtnColse;

	private TextView mTvNum;
	private TextView mTvName;
	private TextView mTvCarName;
	private TextView mTvDay;

	public CallStateListner(Context context) {

		mContext = context;

	}

	public void onCallStateChanged(int state, String incomingNumber) {

//		Log.d(TAG, "onCallStateChanged " + state);

		switch (state) {

		case TelephonyManager.CALL_STATE_IDLE:

			// 대기상태

//			Log.d(TAG, "IDLE");
			if (mRootView != null) {
				try {
					wm.removeView(mRootView);
				} catch (Exception e){
					e.printStackTrace();
				}
			}

			break;

		case TelephonyManager.CALL_STATE_OFFHOOK:

			// 통화중인 상태 (통화버튼이 눌린 상태)

//			Log.d(TAG, "OFFHOOK");

			break;

		case TelephonyManager.CALL_STATE_RINGING:

			// 전화벨이 울리고 있는 상태

			if (previousState != TelephonyManager.CALL_STATE_RINGING) {

//				Log.d(TAG, "RINGING >> Incoming number : " + incomingNumber);

				// 전화번호를 체크 후 수신거부
				searchDb(incomingNumber);

				// previousState = TelephonyManager.CALL_STATE_RINGING;

			}

			break;

		}

	}

	private void searchDb(final String incomingNumber) {
		String[] _whereArgs = {};
		String[] _whereCause = {};

		String[] colums = { DEFINE.DRV_TEL, DEFINE.KUNNR_NM, DEFINE.MAKTX,
				DEFINE.GSTRS };

		DbQueryModel dbQueryModel = new DbQueryModel(
				ConnectController.REPAIR_TABLE_NAME, _whereCause, _whereArgs,
				colums);

		DbAsyncTask dbAsyncTask = new DbAsyncTask("", mContext,
				new DbAsyncResLintener() {

					@Override
					public void onCompleteDB(String funName, int type,
							Cursor cursor, String tableName) {
						if (cursor == null)
							return;

						if (cursor != null) {

							cursor.moveToFirst();

							while (!cursor.isAfterLast()) {
								String tel = cursor.getString(0);
								tel = decrypt(DEFINE.DRV_TEL, tel);
								if (incomingNumber.equals(tel)) {

									// endCall();
									TelephonyManager telephony = (TelephonyManager)

									mContext.getSystemService(Context.TELEPHONY_SERVICE);

									try {

										Class c = Class.forName(telephony
												.getClass().getName());

										Method m = c
												.getDeclaredMethod("getITelephony");

										m.setAccessible(true);

										telephonyService = (ITelephony) m
												.invoke(telephony);

										// telephonyService.silenceRinger();
										String name = cursor.getString(1);
										String carName = cursor.getString(2);
										String day = cursor.getString(3);
										createView(name, tel, carName, day);
										// telephonyService.endCall();

									} catch (Exception e) {

										e.printStackTrace();

									}
								}

								cursor.moveToNext();
							}
							cursor.close();

						}
					}
				}, dbQueryModel);

		dbAsyncTask.execute(DbAsyncTask.DB_SELECT);
	}

	private void endCall() {

		try {
			telephonyService.endCall();

		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		wm.removeView(mRootView);
		mRootView = null;

	}

	private void okCall() {

		wm.removeView(mRootView);
		mRootView = null;

		Intent new_intent = new Intent(Intent.ACTION_MEDIA_BUTTON);
		new_intent.putExtra(Intent.EXTRA_KEY_EVENT, new KeyEvent(
				KeyEvent.ACTION_UP, KeyEvent.KEYCODE_HEADSETHOOK));
		// sendOrderedBroadcast(new_intent, null);
		mContext.sendOrderedBroadcast(new_intent,
				"android.permission.CALL_PRIVILEGED");

	}

	private void createView(String name, String tel, String carName, String day) {
		wm = (WindowManager) mContext.getApplicationContext().getSystemService(
				Context.WINDOW_SERVICE);
		wmParams = new LayoutParams();

		LayoutInflater inflater = (LayoutInflater) mContext
				.getApplicationContext().getSystemService(
						Context.LAYOUT_INFLATER_SERVICE);
		mRootView = inflater.inflate(R.layout.call_receive_layout, null);
		mBtnOk = (Button) mRootView.findViewById(R.id.btn_OK);
		mBtnOk.setOnClickListener(this);

		mBtnNegative = (Button) mRootView.findViewById(R.id.btn_negative);
		mBtnNegative.setOnClickListener(this);

		mBtnColse = (Button) mRootView.findViewById(R.id.btn_close);
		mBtnColse.setOnClickListener(this);

		mTvNum = (TextView) mRootView.findViewById(R.id.tv_num);
		mTvName = (TextView) mRootView.findViewById(R.id.tv_name);
		mTvCarName = (TextView) mRootView.findViewById(R.id.tv_carname);
		mTvDay = (TextView) mRootView.findViewById(R.id.tv_day);
		mTvNum.setText(CommonUtil.setPhoneNumber(tel));
		mTvName.setText(name);
		mTvDay.setText(CommonUtil.setDotDate(day) + " 정비예정");
		mTvCarName.setText(carName);

		/**
		 * Window type: phone. These are non-application windows providing user
		 * interaction with the phone (in particular incoming calls). These
		 * windows are normally placed above all applications, but behind the
		 * status bar.
		 */
		wmParams.type = LayoutParams.TYPE_PHONE;
		wmParams.flags = LayoutParams.FLAG_NOT_TOUCH_MODAL
				| LayoutParams.FLAG_NOT_FOCUSABLE;
		wmParams.gravity = Gravity.CENTER;
		wmParams.x = 0;
		wmParams.y = 0;
		wmParams.width = 436;
		wmParams.height = 454;
		wm.addView(mRootView, wmParams);

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btn_OK:
			okCall();
			break;
		case R.id.btn_negative:
		case R.id.btn_close:
			endCall();
			break;

		default:
			break;
		}
	}

	protected String decrypt(String key, String value) {

		String reStr = value;

		if (KtRentalApplication.isEncoding(key)) {
			reStr = ConnectorUtil.decrypt(value);
		}

		return reStr;
	}
}
