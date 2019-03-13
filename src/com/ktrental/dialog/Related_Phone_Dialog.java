package com.ktrental.dialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.ktrental.R;
import com.ktrental.cm.connect.ConnectController;
import com.ktrental.cm.connect.Connector.ConnectInterface;
import com.ktrental.cm.util.PrintLog;
import com.ktrental.model.TableModel;
import com.ktrental.popup.CallSendPopup2;
import com.ktrental.popup.EventPopupC;
import com.ktrental.popup.SMSPopup2;

public class Related_Phone_Dialog extends DialogC implements ConnectInterface,
		View.OnClickListener {
	private Window w;
	private WindowManager.LayoutParams lp;
	private Context context;
	private ProgressDialog pd;
	private ConnectController cc;

	private Button bt_done;
	private int PHONE_COUNT = 4;
//	private String mGUBUN = "";

	private HashMap<String, String> mO_struct1;
	private ArrayList<HashMap<String, String>> O_ITAB1;

	private TextView tv_name[] = new TextView[PHONE_COUNT];
	private TextView tv_phone[] = new TextView[PHONE_COUNT];
	private ImageView iv_call[] = new ImageView[PHONE_COUNT];
	private ImageView iv_sms[] = new ImageView[PHONE_COUNT];
	private int name_r[] = { R.id.related_name1_id, R.id.related_name2_id,
			R.id.related_name4_id, R.id.related_name5_id };
	private int phone_r[] = { R.id.related_phone1_id, R.id.related_phone2_id,
			R.id.related_phone4_id, R.id.related_phone5_id };
	private int call_r[] = { R.id.related_call1_id, R.id.related_call2_id,
			R.id.related_call4_id, R.id.related_call5_id };
	private int sms_r[] = { R.id.related_sms1_id, R.id.related_sms2_id,
			R.id.related_sms4_id, R.id.related_sms5_id };

	public Related_Phone_Dialog(Context context,
			HashMap<String, String> o_struct1) {
		super(context);
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.related_phone_dialog);

		mO_struct1 = o_struct1;
		w = getWindow();
		w.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN
				| WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
		w.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
		lp = (WindowManager.LayoutParams) w.getAttributes();
		lp.width = LayoutParams.MATCH_PARENT;
		lp.height = LayoutParams.MATCH_PARENT;
		w.setAttributes(lp);
		this.context = context;
		pd = new ProgressDialog(context);
		pd.setCancelable(false);
		pd.setMessage("검색중입니다.");
		cc = new ConnectController(this, context);

		bt_done = (Button) findViewById(R.id.related_done_id);
		bt_done.setOnClickListener(this);

		init();
	}

	private void init() {
		for (int i = 0; i < PHONE_COUNT; i++) {
			tv_name[i] = (TextView) findViewById(name_r[i]);
			tv_phone[i] = (TextView) findViewById(phone_r[i]);
			iv_call[i] = (ImageView) findViewById(call_r[i]);
			iv_call[i].setOnClickListener(this);
			iv_sms[i] = (ImageView) findViewById(sms_r[i]);
			iv_sms[i].setOnClickListener(this);
		}

		// pd.show();
		cc.getZMO_1060_RD08(mO_struct1.get("EQUNR"), mO_struct1.get("JDGBN"));
	}

	@Override
	public void connectResponse(String FuntionName, String resultText,
			String MTYPE, int resulCode, TableModel tableModel) {
		Log.i("#", "####" + FuntionName + "/" + resultText + "/" + MTYPE + "/" + resulCode);
		hideProgress();
		if (MTYPE == null || !MTYPE.equals("S")) {
			
			cc.duplicateLogin(mContext);
			
			EventPopupC epc = new EventPopupC(context);
			epc.show(resultText);
			dismiss();
			return;
		}
		
		// 2013.12.07	ypkim
		if (FuntionName.equals("ZMO_1060_RD08")) {
			// 순회기사 정보 
			if (tableModel.getResponse("O_ENAME_1") != null)
			{
				tv_name[0].setText(tableModel.getResponse("O_ENAME_1"));
			}
			
			if (tableModel.getResponse("O_CELLNO_1") != null)
			{
				tv_phone[0].setText(tableModel.getResponse("O_CELLNO_1"));
			}
			
			// 담당자 MOT 정보 
			if (tableModel.getResponse("O_DEPTNM_1") != null)
			{
				tv_name[1].setText(tableModel.getResponse("O_DEPTNM_1"));
			}
			
			if (tableModel.getResponse("O_TELNR_1") != null)
			{
				tv_phone[1].setText(tableModel.getResponse("O_TELNR_1"));
			}		
			
			// 영업 담당자 정보 
			if (tableModel.getResponse("O_ENAME_2") != null)
			{
				tv_name[2].setText(tableModel.getResponse("O_ENAME_2"));
			}
			
			if (tableModel.getResponse("O_CELLNO_2") != null)
			{
				tv_phone[2].setText(tableModel.getResponse("O_CELLNO_2"));
			}		
			
			// 보유지점정보
			if (tableModel.getResponse("O_DEPTNM_2") != null)
			{
				tv_name[3].setText(tableModel.getResponse("O_DEPTNM_2"));
			}
			
			if (tableModel.getResponse("O_TELNR_2") != null)
			{
				tv_phone[3].setText(tableModel.getResponse("O_TELNR_2"));
			}	
			
			cc.duplicateLogin(mContext);
			
		}
	}

	@Override
	public void reDownloadDB(String newVersion) {
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.related_done_id:
			dismiss();
			break;

		case R.id.related_call1_id:

			callPhone(0);
			break;

		case R.id.related_call2_id:
			callPhone(1);
			break;

		case R.id.related_call4_id:
			callPhone(2);
			break;

		case R.id.related_call5_id:
			callPhone(3);
			break;

		case R.id.related_sms1_id:

			break;

		case R.id.related_sms2_id:

			break;

		case R.id.related_sms4_id:

			break;

		case R.id.related_sms5_id:

			break;
		}

	}

	private void callPhone(int i) {
		if (aviliableCALL(context)) {
			Object data = tv_phone[i].getText();
			String phone1 = data == null || data.toString().equals("")
					|| data.toString().equals(" ") ? "" : data.toString();
			if (phone1.equals("")) {
				EventPopupC epc = new EventPopupC(context);
				//myung 20131217 UPDATE SMS 발송 시 고객 전화번호가 없을 경우 메시지 출력 후 리턴 시킴.
				epc.show("연락처가 없습니다. \n고객 연락처를 확인하여 주십시요.");
			} else {
				CallSendPopup2 sms = new CallSendPopup2(context);
				sms.show(phone1, "");
			}
		} else {
			EventPopupC epc = new EventPopupC(context);
			epc.show("전화어플이 설치되어있지 않습니다.");
		}
	}

	private void callSMS(int i) {
//		if (aviliableSMS(context)) {
//			Object data = tv_phone[i].getText();
//			data = tv_phone[i].getText();
//			String phone = data == null || data.toString().equals("")
//					|| data.toString().equals(" ") ? "" : data.toString();
//
//			if (phone.equals("")) {
//				EventPopupC epc = new EventPopupC(context);
//				epc.show("휴대폰번호가 없습니다.");
//			} else {
//				
//				SMSPopup2 sms = new SMSPopup2(context);
//				sms.show(mO_struct1.get("INVNR").toString(),
//						mO_struct1.get("NAME1").toString(), phone);
//			}
//		} else {
//			EventPopupC epc = new EventPopupC(context);
//			epc.show("문자전송 어플이 설치되어있지 않습니다.");
//		}
	}

	public boolean aviliableCALL(Context context) {
		PackageManager pac = context.getPackageManager();
		Uri callUri = Uri.parse("tel:");
		Intent callIntent = new Intent(Intent.ACTION_CALL, callUri);
		List list = pac.queryIntentActivities(callIntent,
				PackageManager.COMPONENT_ENABLED_STATE_DEFAULT);
		ArrayList tempList = new ArrayList();
		int count = list.size();
		String packageName = "";
		for (int i = 0; i < count; i++) {
			ResolveInfo firstInfo = (ResolveInfo) list.get(i);
			packageName = firstInfo.activityInfo.applicationInfo.packageName;
			tempList.add(firstInfo.activityInfo);
//			Log.d("packageName", "packageName = " + packageName);
		}
		if (packageName == null || packageName.equals("")) {
			return false;
		} else {
			return true;
		}
	}

	public boolean aviliableSMS(Context context) {
		PackageManager pac = context.getPackageManager();
		Uri smsUri = Uri.parse("sms:");
		Intent smsIntent = new Intent(Intent.ACTION_SENDTO, smsUri);
		List list = pac.queryIntentActivities(smsIntent,
				PackageManager.COMPONENT_ENABLED_STATE_DEFAULT);
		ArrayList tempList = new ArrayList();
		int count = list.size();
		String packageName = "";
		for (int i = 0; i < count; i++) {
			ResolveInfo firstInfo = (ResolveInfo) list.get(i);
			packageName = firstInfo.activityInfo.applicationInfo.packageName;
			tempList.add(firstInfo.activityInfo);
//			Log.d("packageName", "packageName = " + packageName);
		}
		if (packageName == null || packageName.equals("")) {
			return false;
		} else {
			return true;
		}
	}

}
