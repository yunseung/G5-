package com.ktrental.dialog;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;

import com.ktrental.R;
import com.ktrental.adapter.Account_List_Dialog_Adapter;
import com.ktrental.cm.connect.ConnectController2;
import com.ktrental.cm.connect.Connector.ConnectInterface;
import com.ktrental.model.CorCardAccountModel;
import com.ktrental.model.TableModel;
import com.ktrental.popup.EventPopupC;
import com.ktrental.util.kog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Handler;

public class Account_List_Dialog extends DialogC implements ConnectInterface,
		View.OnClickListener {
	private Window w;
	private WindowManager.LayoutParams lp;
	private Context context;
	private ProgressDialog pd;

	private Button bt_close;
	// private EditText et_input;
	private ConnectController2 cc;
	private ArrayList<CorCardAccountModel> o_struct1;

	public ArrayList<HashMap<String, String>> array_hash;
	private ListView lv_list;
	private Account_List_Dialog_Adapter tlda;
	public int SELECTED = -1;

	private Handler mHandler;

	public Account_List_Dialog(Context context) {
		super(context);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.account_list_dialog);
		w = getWindow();
		w.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN
				| WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
		w.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
		lp = (WindowManager.LayoutParams) w.getAttributes();
		lp.width = LayoutParams.MATCH_PARENT;
		lp.height = LayoutParams.MATCH_PARENT;
		w.setAttributes(lp);
		this.context = context;
		cc = new ConnectController2(context, this);

		bt_close = (Button) findViewById(R.id.account_list_close);
		bt_close.setOnClickListener(this);

		lv_list = (ListView) findViewById(R.id.account_list_list);

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}

	public Account_List_Dialog(Context context, ArrayList<CorCardAccountModel> o_struct1) {
		super(context);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.account_list_dialog);
		this.o_struct1 = o_struct1;
		w = getWindow();
		w.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN
				| WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
		w.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
		lp = (WindowManager.LayoutParams) w.getAttributes();
		lp.width = LayoutParams.MATCH_PARENT;
		lp.height = LayoutParams.MATCH_PARENT;
		w.setAttributes(lp);
		this.context = context;
		cc = new ConnectController2(context, this);

		bt_close = (Button) findViewById(R.id.account_list_close);
		bt_close.setOnClickListener(this);

		lv_list = (ListView) findViewById(R.id.account_list_list);
        setList(o_struct1);

	}

	@Override
	public void connectResponse(String FuntionName, String resultText,
			String MTYPE, int resulCode, TableModel tableModel) {
//		Log.i("#", "####" + FuntionName + "/" + resultText + "/" + MTYPE + "/"
//				+ resulCode);
		hideProgress();
		kog.e("KDH", "Tire_List_Dialog FuntionName = "+FuntionName);
		
		if (MTYPE == null || !MTYPE.equals("S")) {
			
			cc.duplicateLogin(mContext);
			
			EventPopupC epc = new EventPopupC(context);
			epc.show(resultText);
			return;
		}
	}

	@Override
	public void reDownloadDB(String newVersion) {

	}

	private void setList(final ArrayList<CorCardAccountModel> _list) {
		tlda = new Account_List_Dialog_Adapter(context,
				R.layout.account_list_dialog_row, _list);
		lv_list.setAdapter(tlda);
		lv_list.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				tlda.setCheckPosition(position);
                mOnClickRowListener.onClickRowListener(position);
                dismiss();
			}
		});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.account_list_close: // 닫기
			dismiss();
			break;
		}
	}

	private Account_List_Dialog.OnClickRowListener mOnClickRowListener;

	public void setOnClickRowListener(Account_List_Dialog.OnClickRowListener clickRowListener) {
		mOnClickRowListener = clickRowListener;
	}

	public interface OnClickRowListener {
		void onClickRowListener(int position);
	}
}
