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
import android.widget.Button;
import android.widget.ListView;

import com.ktrental.R;
import com.ktrental.adapter.Mistery_Shopping_List_Login_Dialog_Adapter;
import com.ktrental.cm.connect.ConnectController2;
import com.ktrental.cm.connect.Connector.ConnectInterface;
import com.ktrental.model.CorCardAccountModel;
import com.ktrental.model.TableModel;
import com.ktrental.popup.EventPopupC;
import com.ktrental.util.kog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Handler;

public class Mistery_Shopping_Dialog_Login extends DialogC implements ConnectInterface,
		View.OnClickListener {
	private Window w;
	private WindowManager.LayoutParams lp;
	private Context context;
	private ProgressDialog pd;

	private Button bt_close;
	// private EditText et_input;
	private ConnectController2 cc;

	public ArrayList<HashMap<String, String>> array_hash;
	private ListView lv_list;
	private Mistery_Shopping_List_Login_Dialog_Adapter tlda;
	public int SELECTED = -1;

	private Handler mHandler;

	public Mistery_Shopping_Dialog_Login(Context context) {
		super(context);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.mistery_shopping_dialog_login);
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

		cc.getZMO_1160_RD01("L", "");
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}

	public Mistery_Shopping_Dialog_Login(Context context, ArrayList<CorCardAccountModel> o_struct1) {
		super(context);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.mistery_shopping_dialog_login);
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
//        setList(o_struct1);

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
		} else {
			if(FuntionName.equals("ZMO_1160_RD01")){
				ArrayList<HashMap<String, String>> array = tableModel.getTableArray();
				if(array != null && array.size() > 0){
					setList(array);
				}
			}
		}
	}

	@Override
	public void reDownloadDB(String newVersion) {

	}

	private void setList(final ArrayList<HashMap<String, String>> _list) {
	    if(_list == null){
	        return;
        }
		tlda = new Mistery_Shopping_List_Login_Dialog_Adapter(context,
				R.layout.mistery_shopping_dialog_login_row, _list);
		lv_list.setAdapter(tlda);
//		lv_list.setOnItemClickListener(new OnItemClickListener() {
//			@Override
//			public void onItemClick(AdapterView<?> arg0, View arg1,
//					int position, long arg3) {
//				tlda.setCheckPosition(position);
//                mOnClickRowListener.onClickRowListener(position);
//                dismiss();
//			}
//		});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.account_list_close: // 닫기
			dismiss();
			break;
		}
	}

	private Mistery_Shopping_Dialog_Login.OnClickRowListener mOnClickRowListener;

	public void setOnClickRowListener(Mistery_Shopping_Dialog_Login.OnClickRowListener clickRowListener) {
		mOnClickRowListener = clickRowListener;
	}

	public interface OnClickRowListener {
		void onClickRowListener(int position);
	}
}
