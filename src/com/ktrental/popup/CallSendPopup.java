package com.ktrental.popup;

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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class CallSendPopup extends BaseTouchDialog implements
		View.OnClickListener {

	private BaseMaintenanceModel mModel;
	private TextView mTvCustomCall;
	private View mRootView;

	public CallSendPopup(Context context, BaseMaintenanceModel model) {
		super(context);

		mModel = model;
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

		TextView tvHp = (TextView) findViewById(R.id.tv_hp);
		TextView tvHome = (TextView) findViewById(R.id.tv_home);
		tvHp.setText(model.getDrv_mob());
		tvHome.setText(model.getTel());

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
		switch (v.getId()) {
		case R.id.tv_custom:
			clickCustom();
			break;
		case R.id.btn_call_hp:
			clickHp();
			break;
		case R.id.btn_call_home:
			clickHome();
			break;
		case R.id.btn_call_custom:
			clickCallCustom();
			break;
		case R.id.btn_cancel:
			dismiss();
			break;
		default:
			break;
		}
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
		String callNum = mModel.getDrv_mob();
		CommonUtil.callAction(mContext, callNum);
		saveCallLog("핸드폰", callNum);
//		MaintenanceResultFragment.mCallFlag = true;
		dismiss();
	}

	private void clickHome() {
		String callNum = mModel.getTel();
		CommonUtil.callAction(mContext, callNum);
		saveCallLog("집", callNum);
//		MaintenanceResultFragment.mCallFlag = true;
		dismiss();
	}

	private void clickCallCustom() {
		String callNum = mTvCustomCall.getText().toString();
		CommonUtil.callAction(mContext, callNum);
		saveCallLog("직접입력", callNum);
//		MaintenanceResultFragment.mCallFlag = true;
		dismiss();
	}

	private void saveCallLog(String type, String tel) {
		ArrayList<HashMap<String, String>> arr = new ArrayList<HashMap<String, String>>();
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("INVNR", mModel.getCarNum());
		map.put("DRIVN", mModel.getDRIVER_NAME());
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
}
