package com.ktrental.dialog;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ktrental.R;
import com.ktrental.cm.connect.ConnectController;
import com.ktrental.cm.connect.Connector.ConnectInterface;
import com.ktrental.model.MaintenanceItemModel;
import com.ktrental.model.TableModel;
import com.ktrental.popup.CallSendPopup2;
import com.ktrental.popup.EventPopupC;
import com.ktrental.util.kog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Maintenance_Confirm_Dialog extends DialogC implements View.OnClickListener, DialogInterface.OnDismissListener {
	private Window w;
	private WindowManager.LayoutParams lp;
	private Context context;
	private ProgressDialog pd;

	private Button bt_done;
	private int PHONE_COUNT = 4;


	private LinearLayout ll_engine_oil;
	private LinearLayout ll_oil_filter;
	private LinearLayout ll_air_cleaner;
	private LinearLayout ll_lining_pad;
	private LinearLayout ll_battery;


	private ArrayList<MaintenanceItemModel> mLastItemModels = new ArrayList<MaintenanceItemModel>();




	@Override
	public void onDismiss(DialogInterface dialogInterface) {

	}
//	private String mGUBUN = "";

	public interface OnDismissListener {

		public abstract void onDismiss();
	}

	private Delivery_Point_Dialog.OnDismissListener mConfirmListener = null;
	public void setOnDismissListener(Delivery_Point_Dialog.OnDismissListener onDismissListener)
	{
		mConfirmListener = onDismissListener;
		setOnDismissListener(this);
	}




	public Maintenance_Confirm_Dialog(Context context, ArrayList<MaintenanceItemModel> list) {
		super(context);
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.maintenance_confirm_dialog);

		mLastItemModels = list;


		w = getWindow();
		w.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN
				| WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
		w.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
		lp = (WindowManager.LayoutParams) w.getAttributes();
		lp.width = LayoutParams.MATCH_PARENT;
		lp.height = LayoutParams.MATCH_PARENT;
		w.setAttributes(lp);
		this.context = context;

		ll_engine_oil = (LinearLayout)findViewById(R.id.ll_engine_oil);
		ll_oil_filter = (LinearLayout)findViewById(R.id.ll_oil_filter);
		ll_air_cleaner = (LinearLayout)findViewById(R.id.ll_air_cleaner);
		ll_lining_pad = (LinearLayout)findViewById(R.id.ll_lining_pad);
		ll_battery = (LinearLayout)findViewById(R.id.ll_battery);


		ll_engine_oil.setVisibility(View.GONE);
		ll_oil_filter.setVisibility(View.GONE);
		ll_air_cleaner.setVisibility(View.GONE);
		ll_lining_pad.setVisibility(View.GONE);
		ll_battery.setVisibility(View.GONE);



		for(int i = 0 ; i < mLastItemModels.size() ; i++)
		{
			kog.e("Jonathan", " mLastItemModels :: " + mLastItemModels.get(i).getName() + "   " + mLastItemModels.get(i).getMaintenanceGroupModel().getName() + "  " + mLastItemModels.get(i).getGRP_CD());
			if("401".equals(mLastItemModels.get(i).getGRP_CD()))
			{
				ll_engine_oil.setVisibility(View.VISIBLE);
			}
			else if("403".equals(mLastItemModels.get(i).getGRP_CD()))
			{
				ll_oil_filter.setVisibility(View.VISIBLE);
			}
			else if("402".equals(mLastItemModels.get(i).getGRP_CD()))
			{
				ll_air_cleaner.setVisibility(View.VISIBLE);
			}
			else if("412".equals(mLastItemModels.get(i).getGRP_CD()))
			{
				ll_lining_pad.setVisibility(View.VISIBLE);
			}
			else if("413".equals(mLastItemModels.get(i).getGRP_CD()))
			{
				ll_battery.setVisibility(View.VISIBLE);
			}

		}


		bt_done = (Button) findViewById(R.id.bt_confirm_maintenance);
		bt_done.setOnClickListener(this);
	}



	@Override
	public void onClick(View v) {

		switch (v.getId()) {
			case R.id.bt_confirm_maintenance:

				kog.e("Jonathan", "정비 결과 저장 클릭 되니??");

				dismiss();
				break;


		}


	}


}
