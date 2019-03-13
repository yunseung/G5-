package com.ktrental.dialog;

import java.util.ArrayList;
import java.util.List;

import com.ktrental.R;
import com.ktrental.popup.CallSendPopup2;
import com.ktrental.popup.EventPopupC;
import com.ktrental.util.CommonUtil;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.TextView;

public class Menu7_2_RepairSearch_Dialog extends DialogC implements
		View.OnClickListener {

	private Window w;
	private WindowManager.LayoutParams lp;
	private Context context;
	private TextView tv_insurance_call_num[] = new TextView[15];
	private Button bt_insurance_call[] = new Button[15];
	private int bt_insurance_ids[] = { R.id.bt_insurance_call_01,
			R.id.bt_insurance_call_02, R.id.bt_insurance_call_03,
			R.id.bt_insurance_call_04, R.id.bt_insurance_call_05,
			R.id.bt_insurance_call_06, R.id.bt_insurance_call_07,
			R.id.bt_insurance_call_08, R.id.bt_insurance_call_09,
			R.id.bt_insurance_call_10, R.id.bt_insurance_call_11,
			R.id.bt_insurance_call_12, R.id.bt_insurance_call_13,
			R.id.bt_insurance_call_14, R.id.bt_insurance_call_15};
	private int tv_insurance_ids[] = { R.id.tv_insurance_01,
			R.id.tv_insurance_02, R.id.tv_insurance_03, R.id.tv_insurance_04,
			R.id.tv_insurance_05, R.id.tv_insurance_06, R.id.tv_insurance_07,
			R.id.tv_insurance_08, R.id.tv_insurance_09, R.id.tv_insurance_10,
			R.id.tv_insurance_11, R.id.tv_insurance_12, R.id.tv_insurance_13,
			R.id.tv_insurance_14, R.id.tv_insurance_15 };

	public Menu7_2_RepairSearch_Dialog(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.menu7_2_repairsearch_dialog);
		w = getWindow();
		w.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN
				| WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
		w.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
		lp = (WindowManager.LayoutParams) w.getAttributes();
		lp.width = LayoutParams.MATCH_PARENT;
		lp.height = LayoutParams.MATCH_PARENT;
		w.setAttributes(lp);
		this.context = context;

		for (int i = 0; i < tv_insurance_call_num.length; i++)
			tv_insurance_call_num[i] = (TextView) findViewById(tv_insurance_ids[i]);

		for (int i = 0; i < bt_insurance_call.length; i++)
			bt_insurance_call[i] = (Button) findViewById(bt_insurance_ids[i]);

		for (int i = 0; i < bt_insurance_call.length; i++) {
			bt_insurance_call[i].setOnClickListener(this);
		}

	}

	private void callInsuranceContact(TextView tv) {
		// if (aviliableCALL(context)) {
		Object data = tv.getText().toString();
		String phone = data == null || data.toString().equals("")
				|| data.toString().equals(" ") ? "" : data.toString();

		if (phone.equals("")) {
			EventPopupC epc = new EventPopupC(context);
			epc.show("연락처가 없습니다. \n고객 연락처를 확인하여 주십시요.");
		} else {
			// CallSendPopup2 csp = new CallSendPopup2(context);
			// if (phone.substring(0, 2).equals("01"))
			// csp.show(phone, "");
			// else
			// csp.show("", phone);
			
			CommonUtil.callAction(mContext, data.toString());

		}
		// } else {
		// EventPopupC epc = new EventPopupC(context);
		// epc.show("전화어플이 설치되어있지 않습니다.");
		// }
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
		}
		if (packageName == null || packageName.equals("")) {
			return false;
		} else {
			return true;
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		for (int i = 0; i < bt_insurance_call.length; i++) {
			if (v.getId() == bt_insurance_ids[i]) {
				callInsuranceContact(tv_insurance_call_num[i]);
				break;
			}
		}
	}

}
