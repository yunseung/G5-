package com.ktrental.fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.ktrental.R;
import com.ktrental.adapter.PartsTransfer_Cars_Dialog_Adapter;
import com.ktrental.beans.PM013;
import com.ktrental.cm.connect.ConnectController;
import com.ktrental.cm.connect.Connector;
import com.ktrental.cm.connect.Connector.ConnectInterface;
import com.ktrental.common.DEFINE;
import com.ktrental.model.TableModel;
import com.ktrental.popup.AreaSelectPopup;
import com.ktrental.popup.EventPopupC;
import com.ktrental.popup.PM023_Popup;
import com.ktrental.popup.Popup_Window_PM013;
import com.ktrental.popup.QuickAction;

import java.util.ArrayList;
import java.util.HashMap;

public class PartsTransferCarsDialogFragment extends BaseFragment implements ConnectInterface, OnClickListener {

	private WindowManager.LayoutParams lp;
	private Context context;
	private Button close;
	private EditText input;
	private Button search;
	private ListView listview;

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

	private PartsTransfer_Cars_Dialog_Adapter pcda;
	private ProgressDialog pd;
	private HashMap<String, String> mSelectedMap = null;

	Connector connector;

	private String PM023_TAG;
	private String PM013_TAG;
	private PM023_Popup ipm023popup;
	private Button bt_group;

	private ConnectController connectController;
	private ArrayList<HashMap<String, String>> array_hash;
	private Button bt_search;
	private EditText et_carnum;
	private EditText et_drivername;

	private Popup_Window_PM013 ppm013popup;

	private ImageView iv_nodata;

	// public PartsTransferCarsDialogFragment(String className,
	// OnChangeFragmentListener changeFragmentListener){
	// super(className, changeFragmentListener);
	// }

	public PartsTransferCarsDialogFragment(){}

	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		// View v = inflater.inflate(R.layout.partstransfer_cars_dialog, null);
		mRootView = inflater.inflate(R.layout.partstransfer_cars_dialog, null);

		bt_group = (Button) mRootView.findViewById(R.id.partstransfer_dialog_mot_id);
		bt_group.setOnClickListener(this);
		// connectController = new ConnectController(this, context);
		close = (Button) mRootView.findViewById(R.id.partstransfer_parts_search_close_id);
		close.setOnClickListener(this);
		et_carnum = (EditText) mRootView.findViewById(R.id.partstransfer_car_search_carnum_et_id);
		et_drivername = (EditText) mRootView.findViewById(R.id.partstransfer_car_search_drivername_et_id);
		bt_search = (Button) mRootView.findViewById(R.id.partstransfer_car_search_dialog_search_id);
		bt_search.setOnClickListener(this);
		listview = (ListView) mRootView.findViewById(R.id.partstransfer_car_search_listview_id);

		iv_nodata = (ImageView) mRootView.findViewById(R.id.list_nodata_id);

		return mRootView;

	}

	@Override
	public void onAttach(Activity activity) {
		context = activity;

		connectController = new ConnectController(this, context);

		pd = new ProgressDialog(context);
		pd.setCancelable(false);
		pd.setMessage("검색중입니다.");

		initPM013();
		ipm023popup = new PM023_Popup(context, QuickAction.VERTICAL, PartsTransferFragment.pm023_arr);
		ppm013popup = new Popup_Window_PM013(context);

		PM013_TAG = " ";

		setPM013Click();

		super.onAttach(activity);
	}

	// @Override
	// public void onStop() {
	// // TODO Auto-generated method stub
	//
	// super.onStop();
	// }

	// @Override
	// public void onStart() {
	// // TODO Auto-generated method stub
	//
	// array_hash.clear();
	// pcda.notifyDataSetChanged();
	// super.onStart();
	// }

	@Override
	public void connectResponse(String FuntionName, String resultText, String MTYPE, int resulCode,
			TableModel tableModel) {
		hideProgress();
		if (!MTYPE.equals("S")) {
			connectController.duplicateLogin(context);
			
			EventPopupC epc = new EventPopupC(context);
			epc.show(resultText);
			return;
		}

		if (FuntionName.equals("ZMO_1060_RD01")) {
			// Log.i("#",
			// "####"+FuntionName+"/"+resultText+"/"+MTYPE+"/"+resulCode);
			array_hash = tableModel.getTableArray();

			if (array_hash.size() > 0) {
				iv_nodata.setVisibility(View.GONE);
			} else {
				iv_nodata.setVisibility(View.VISIBLE);
			}

			if (array_hash.size() > 0)
				pre.sendEmptyMessageDelayed(0, 100);

			pcda = new PartsTransfer_Cars_Dialog_Adapter(context, R.layout.partstransfer_cars_dialog_row, array_hash);
			listview.setAdapter(pcda);
			listview.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
					pcda.setCheckPosition(position);
				}
			});
			
			
			connectController.duplicateLogin(context);
			
		}
	}

	// 키보드내리기
	Handler pre = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(et_carnum.getWindowToken(), 0);
		}
	};

	@Override
	public void reDownloadDB(String newVersion) {
	}

	public HashMap<String, String> getSelectedAddress() {
		return mSelectedMap;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.partstransfer_dialog_mot_id: // 자재그룹
			ppm013popup.show(bt_group);
			break;
		case R.id.partstransfer_parts_search_close_id:
			dismiss();
			break;
		case R.id.partstransfer_car_search_dialog_search_id: // 조회버튼
			showProgress("조회중 입니다.");

			Object ob_carnum = et_carnum.getText();
			String str_carnum = ob_carnum != null && !ob_carnum.equals("") ? ob_carnum.toString() : " ";

			Object ob_name = et_drivername.getText();
			String str_name = ob_name != null && !ob_name.equals("") ? ob_name.toString() : " ";

			connectController.getZMO_1060_RD01(str_carnum, PM013_TAG, str_name, " ");
			break;
		}
	}

	private void setPM013Click() {
		ViewGroup vg = ppm013popup.getViewGroup();
		LinearLayout back = (LinearLayout) vg.getChildAt(0);
		for (int i = 0; i < back.getChildCount(); i++) {
			LinearLayout row_back = (LinearLayout) back.getChildAt(i);
			final Button bt = (Button) row_back.getChildAt(0);
			bt.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					bt_group.setText(bt.getText().toString());
					PM013_TAG = bt.getTag().toString();
					ppm013popup.dismiss();
				}
			});
		}
	}

	public ArrayList<PM013> pm013_arr;

	private ArrayList<PM013> initPM013() {
		pm013_arr = new ArrayList<PM013>();
		PM013 pm = new PM013();
		pm.ZCODEV = " ";
		pm.ZCODEVT = "전체";
		pm013_arr.add(pm);
		String path = context.getExternalCacheDir() + "/DATABASE/" + DEFINE.SQLLITE_DB_NAME;
		SQLiteDatabase sqlite = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.OPEN_READONLY);
		Cursor cursor = sqlite.rawQuery("select ZCODEV, ZCODEVT from " + TABLE_NAME + " where ZCODEH = 'PM013'", null);
		while (cursor.moveToNext()) {
			int calnum1 = cursor.getColumnIndex("ZCODEV");
			int calnum2 = cursor.getColumnIndex("ZCODEVT");
			String zcodev = cursor.getString(calnum1);
			String zcodevt = cursor.getString(calnum2);
			pm = new PM013();
			pm.ZCODEV = zcodev;
			pm.ZCODEVT = zcodevt;
			pm013_arr.add(pm);
		}
		cursor.close();
//		sqlite.close();

		return pm013_arr;
	}

	public PartsTransfer_Cars_Dialog_Adapter getPcda() {
		return pcda;
	}

	public ArrayList<HashMap<String, String>> getArray_hash() {
		return array_hash;
	}
}
