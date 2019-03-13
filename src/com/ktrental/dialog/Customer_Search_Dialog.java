package com.ktrental.dialog;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.ktrental.R;
import com.ktrental.adapter.Customer_Search_Dialog_Adapter;
import com.ktrental.cm.connect.ConnectController;
import com.ktrental.cm.connect.Connector;
import com.ktrental.cm.connect.Connector.ConnectInterface;
import com.ktrental.fragment.CustomerSearchFragment;
import com.ktrental.model.TableModel;
import com.ktrental.popup.AreaSelectPopup;
import com.ktrental.popup.EventPopupC;

public class Customer_Search_Dialog extends DialogC implements
		ConnectInterface, View.OnClickListener {
	private Window w;
	private WindowManager.LayoutParams lp;
	private Context context;
	private Button close;
	private EditText input;
	private Button search;

	private HashMap<String, ArrayList<String>> address_hash = new HashMap<String, ArrayList<String>>();
	private ArrayList<String> keys = new ArrayList<String>();

	String TABLE_NAME = "O_ITAB1";
	String ZCODE_KEY = "CM010";
	String ZCODE_VAL = "M006";
	String ZCODEH = "ZCODEH";
	String ZCODEV = "ZCODEV";
	String ZCODEVT = "ZCODEVT";

	private AreaSelectPopup areaSelectPopup;
	private String mCityText = "";
	private String mGuText = "";
	private ConnectController cc;

	private ProgressDialog pd;
	private HashMap<String, String> mSelectedMap = null;

	// private String PM023_TAG;
	// private PM023_Popup ipm023popup;
	// private Button bt_group;
	private Button bt_search;
	private EditText et_customer_name;
	private ListView listview;
	// private PartsTransfer_Parts_Dialog_Adapter ppda;

	private Customer_Search_Dialog_Adapter csda;
	private CustomerSearchFragment csf;
	
	
	
	
	public Customer_Search_Dialog(Context context) {
		super(context);
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.customer_search_dialog);
		w = getWindow();
		w.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN
				| WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
		w.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
		lp = (WindowManager.LayoutParams) w.getAttributes();
		lp.width = LayoutParams.MATCH_PARENT;
		lp.height = LayoutParams.MATCH_PARENT;
		w.setAttributes(lp);

		this.context = context;
//		this.csf = customerSearchFragment;
		
		pd = new ProgressDialog(context);
		pd.setCancelable(false);
		pd.setMessage("검색중입니다.");
//		hideProgress();
		// PM023_TAG = " ";
		// ipm023popup = new PM023_Popup(context, QuickAction.VERTICAL,
		// PartsTransferFragment.pm023_arr);
		// bt_group = (Button)findViewById(R.id.partstransfer_dialog_group_id);
		// bt_group.setOnClickListener(this);
		cc = new ConnectController(this, context);
		close = (Button) findViewById(R.id.partstransfer_parts_search_close_id);
		close.setOnClickListener(this);
		// input = (EditText)
		// findViewById(R.id.address_search_jibun_edittext_id);
		bt_search = (Button) findViewById(R.id.customer_search_dialog_search_id);
		bt_search.setOnClickListener(this);
		et_customer_name = (EditText) findViewById(R.id.customer_search_name_id);
		et_customer_name.setText("");
		listview = (ListView) findViewById(R.id.customer_search_list_id);
		// bt_done = (Button)
		// findViewById(R.id.partstransfer_parts_search_save_id);
		// bt_done.setOnClickListener(this);
		// btnSave = (Button) findViewById(R.id.address_search_save_id);
		// initData();
		// setButton();
		// setLocationPopup();

//		setEditTextEnter();
//		goSearch();
	}
	
	
	

	public Customer_Search_Dialog(Context context, String name,
			CustomerSearchFragment customerSearchFragment) {
		super(context);
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.customer_search_dialog);
		w = getWindow();
		w.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN
				| WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
		w.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
		lp = (WindowManager.LayoutParams) w.getAttributes();
		lp.width = LayoutParams.MATCH_PARENT;
		lp.height = LayoutParams.MATCH_PARENT;
		w.setAttributes(lp);

		this.context = context;
		this.csf = customerSearchFragment;
		
		pd = new ProgressDialog(context);
		pd.setCancelable(false);
		pd.setMessage("검색중입니다.");
//		hideProgress();
		// PM023_TAG = " ";
		// ipm023popup = new PM023_Popup(context, QuickAction.VERTICAL,
		// PartsTransferFragment.pm023_arr);
		// bt_group = (Button)findViewById(R.id.partstransfer_dialog_group_id);
		// bt_group.setOnClickListener(this);
		cc = new ConnectController(this, context);
		close = (Button) findViewById(R.id.partstransfer_parts_search_close_id);
		close.setOnClickListener(this);
		// input = (EditText)
		// findViewById(R.id.address_search_jibun_edittext_id);
		bt_search = (Button) findViewById(R.id.customer_search_dialog_search_id);
		bt_search.setOnClickListener(this);
		et_customer_name = (EditText) findViewById(R.id.customer_search_name_id);
		et_customer_name.setText(name);
		listview = (ListView) findViewById(R.id.customer_search_list_id);
		// bt_done = (Button)
		// findViewById(R.id.partstransfer_parts_search_save_id);
		// bt_done.setOnClickListener(this);
		// btnSave = (Button) findViewById(R.id.address_search_save_id);
		// initData();
		// setButton();
		// setLocationPopup();

		setEditTextEnter();
		goSearch();
	}

	private void setEditTextEnter() {
		et_customer_name.setOnKeyListener(new View.OnKeyListener() {
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if (event.getAction() == KeyEvent.ACTION_DOWN
						&& keyCode == KeyEvent.KEYCODE_ENTER) {
//					Log.i("#", "###" + "et_customer_name");
					goSearch();
					return true;
				}
				return false;
			}
		});
	}

	private void setButton() {
	}

	ArrayList<HashMap<String, String>> array_hash;
	Connector connector;

	@Override
	public void connectResponse(String FuntionName, String resultText,
			String MTYPE, int resulCode, TableModel tableModel) {
//		Log.i("#", "####" + FuntionName + "/" + resultText + "/" + MTYPE + "/"
//				+ resulCode);
		hideProgress();
		if (MTYPE == null || !MTYPE.equals("S")) {
			
			cc.duplicateLogin(mContext);
			
			EventPopupC epc = new EventPopupC(context);
			epc.show(resultText);
			return;
		}

		else if (FuntionName.equals("ZMO_3020_RD02")) {
			array_hash = tableModel.getTableArray();
			if (array_hash.size() > 0)
				pre.sendEmptyMessageDelayed(0, 100);

			if (array_hash.size() > 1) {
				csda = new Customer_Search_Dialog_Adapter(context,
						R.layout.customer_search_dialog_row, array_hash);
				listview.setAdapter(csda);
				listview.setOnItemClickListener(new OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1,
							int position, long arg3) {
						csda.setCheckPosition(position);
					}
				});
			} else if (array_hash.size() == 1) {
//				pre.sendEmptyMessage(0);
//				csf.setCarNumSearch1();
			}
			
			
			cc.duplicateLogin(mContext);
		}

	}

	// 키보드내리기
	Handler pre = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			InputMethodManager imm = (InputMethodManager) context
					.getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(et_customer_name.getWindowToken(), 0);
		}
	};
	
	//myung 20131202 ADD 고객조회 화면 팝업과 동시에 조회 시작 됨. 조회 로딩바 없음. 
    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
        	showProgress("검색중 입니다.");
        }
    };


	@Override
	public void reDownloadDB(String newVersion) {
		// TODO Auto-generated method stub
	}

	public HashMap<String, String> getSelectedAddress() {
		return mSelectedMap;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.partstransfer_dialog_group_id: // 자재그룹
			// ipm023popup.show(bt_group);
			break;
		case R.id.partstransfer_parts_search_close_id: // 닫기
			dismiss();
			break;
		case R.id.customer_search_dialog_search_id: // 조회
			goSearch();
			break;
		}
	}

	private void goSearch() {

		//myung 20131202 UPDATE 고객조회 화면 팝업과 동시에 조회 시작 됨. 조회 로딩바 없음. 
//		showProgress("검색중 입니다.");
		new Handler().postDelayed(mRunnable, 300);
		
		Object data = et_customer_name.getText();
		String str = data != null && !data.equals("") ? data.toString() : " ";
		cc.getZMO_3020_RD02(str);
	}

	public Customer_Search_Dialog_Adapter getCsda() {
		return csda;
	}

	public void setCsda(Customer_Search_Dialog_Adapter csda) {
		this.csda = csda;
	}

	public ArrayList<HashMap<String, String>> getArray_hash() {
		return array_hash;
	}

	public void setArray_hash(ArrayList<HashMap<String, String>> array_hash) {
		this.array_hash = array_hash;
	}
}
