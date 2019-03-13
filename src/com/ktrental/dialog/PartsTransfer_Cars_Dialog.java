package com.ktrental.dialog;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

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
import com.ktrental.util.kog;

import java.util.ArrayList;
import java.util.HashMap;

public class PartsTransfer_Cars_Dialog extends DialogC implements ConnectInterface, View.OnClickListener {
	private Window w;
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

	private TextView tv_title;
	private Button bt_done;

	private TextView tv_Car_Number, textView3, row_top_DriverName;

	private String P2 = "P2";
	private String is_CarManager;

	boolean isCheck;

	public PartsTransfer_Cars_Dialog(Context context) {
		super(context);
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.partstransfer_cars_dialog);
		w = getWindow();
		w.setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN | WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
		w.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
		lp = (WindowManager.LayoutParams) w.getAttributes();
		lp.width = LayoutParams.MATCH_PARENT;
		lp.height = LayoutParams.MATCH_PARENT;
		w.setAttributes(lp);
		this.context = context;
		pd = new ProgressDialog(context);
		pd.setCancelable(false);
		pd.setMessage("검색중입니다.");

		initPM013();
		// Jonathan 14.07.31 주석처리 함. 책임정비기사 조회에 워셔액.. 뭐 이런거 필요 없을거 같아서...
		// ipm023popup = new PM023_Popup(context, QuickAction.VERTICAL,
		// PartsTransferFragment.pm023_arr);
		ppm013popup = new Popup_Window_PM013(context);

		PM013_TAG = " ";

		bt_group = (Button) findViewById(R.id.partstransfer_dialog_mot_id);
		bt_group.setOnClickListener(this);
		connectController = new ConnectController(this, context);
		close = (Button) findViewById(R.id.partstransfer_parts_search_close_id);
		close.setOnClickListener(this);
		et_carnum = (EditText) findViewById(R.id.partstransfer_car_search_carnum_et_id);
		et_drivername = (EditText) findViewById(R.id.partstransfer_car_search_drivername_et_id);
		bt_search = (Button) findViewById(R.id.partstransfer_car_search_dialog_search_id);
		bt_search.setOnClickListener(this);
		listview = (ListView) findViewById(R.id.partstransfer_car_search_listview_id);

		iv_nodata = (ImageView) findViewById(R.id.list_nodata_id);

		tv_title = (TextView) findViewById(R.id.title_id);
		bt_done = (Button) findViewById(R.id.partstransfer_car_search_done_id);

		tv_Car_Number = (TextView) findViewById(R.id.tv_Car_Number);
		textView3 = (TextView) findViewById(R.id.textView3);
		row_top_DriverName = (TextView) findViewById(R.id.row_top_DriverName);

		// Jonathan 14.08.04 추가.
		if (context instanceof Activity) {
			setOwnerActivity((Activity) context);
		}

		kog.e("Jonathan", "getOwnerActivity() :: " + getOwnerActivity().toString());
		kog.e("Jonathan",
				"getOwner is_CarManager :: " + getOwnerActivity().getIntent().getStringExtra("is_CarManager"));

		// Jonathan 14.08.04 추가.
		if (P2.equals(getOwnerActivity().getIntent().getStringExtra("is_CarManager"))) {
			is_CarManager = getOwnerActivity().getIntent().getStringExtra("is_CarManager");

			tv_Car_Number.setVisibility(View.GONE);
			et_carnum.setVisibility(View.GONE);
			textView3.setVisibility(View.GONE);
			row_top_DriverName.setText("기사명");

//			LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(150, 36);
//			llp.setMargins(50, 0, 0, 0); // llp.setMargins(left, top, right,
//											// bottom);
//			bt_search.setLayoutParams(llp);
		}

		setPM013Click();

	}

	public void setTitle(String str) {
		tv_title.setText(str);
	}

	public void setDone(String str) {
		bt_done.setText(str);
	}

	@Override
	public void connectResponse(String FuntionName, String resultText, String MTYPE, int resulCode,
			TableModel tableModel) {
		hideProgress();

		isCheck = false;

		if (!MTYPE.equals("S")) {
			kog.e("Jonathan", "카매니저 필드값 오류.");
			EventPopupC epc = new EventPopupC(context);

			showProgress("조회중 입니다.");
			// EventPopupC epc = new EventPopupC(context);
			// epc.show("조회중 입니다.");

			Object ob_carnum = et_carnum.getText();
			String str_carnum = ob_carnum != null && !ob_carnum.equals("") ? ob_carnum.toString() : " ";

			Object ob_name = et_drivername.getText();
			String str_name = ob_name != null && !ob_name.equals("") ? ob_name.toString() : " ";

			kog.e("Jonathan", "카매니저 조회 버튼 누르면 들어와야함...");

			// Jonathan 14.08.04 추가.
			// kog.e("Jonathan", "this.getOwnerActivity().getIntent()." +
			// getOwnerActivity().getIntent().getStringExtra("is_CarManager"));

			if (P2.equals(getOwnerActivity().getIntent().getStringExtra("is_CarManager"))) {
				kog.e("Jonathan", "카매니저 조회 여기 ??");
				kog.e("Jonathan",
						"str_carnum :: " + str_carnum + "  PM013_TAG :: " + PM013_TAG + "  str_name :: " + str_name);
				connectController.getZMO_1060_RD01_CarManager(str_carnum, PM013_TAG, str_name, "");
				isCheck = true;
			} else

			{
				kog.e("Jonathan", "카매니저 아닌 순회정비.");
				connectController.getZMO_1060_RD01(str_carnum, PM013_TAG, str_name, " ");
				isCheck = true;
			}

			// epc.show("조회 버튼을 다시 눌러주세요.");
			// epc.show(resultText);
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

			pcda = new PartsTransfer_Cars_Dialog_Adapter(context, R.layout.partstransfer_cars_dialog_row, array_hash,
					is_CarManager);
			listview.setAdapter(pcda);
			listview.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
					pcda.setCheckPosition(position);
				}
			});
			
			connectController.duplicateLogin(mContext);
			
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
			if (isCheck) {
				return;
			}
			showProgress("조회중 입니다.");
			// EventPopupC epc = new EventPopupC(context);
			// epc.show("조회중 입니다.");

			Object ob_carnum = et_carnum.getText();
			String str_carnum = ob_carnum != null && !ob_carnum.equals("") ? ob_carnum.toString() : " ";

			Object ob_name = et_drivername.getText();
			String str_name = ob_name != null && !ob_name.equals("") ? ob_name.toString() : " ";

			kog.e("Jonathan", "카매니저 조회 버튼 누르면 들어와야함...");

			// Jonathan 14.08.04 추가.
			// kog.e("Jonathan", "this.getOwnerActivity().getIntent()." +
			// getOwnerActivity().getIntent().getStringExtra("is_CarManager"));

			if (P2.equals(getOwnerActivity().getIntent().getStringExtra("is_CarManager"))) {
				kog.e("Jonathan", "카매니저 조회 여기 ??");
				kog.e("Jonathan",
						"str_carnum :: " + str_carnum + "  PM013_TAG :: " + PM013_TAG + "  str_name :: " + str_name);
				connectController.getZMO_1060_RD01_CarManager(str_carnum, PM013_TAG, str_name, "");
				isCheck = true;
			} else

			{
				kog.e("Jonathan", "카매니저 아닌 순회정비.");
				connectController.getZMO_1060_RD01(str_carnum, PM013_TAG, str_name, " ");
				isCheck = true;
			}

			break;
		}
	}

	private void setPM013Click() {
		ViewGroup vg = ppm013popup.getViewGroup();
		LinearLayout back = (LinearLayout) vg.getChildAt(0);
		for (int i = 0; i < back.getChildCount(); i++) {
			LinearLayout row_back = (LinearLayout) back.getChildAt(i);
			final Button bt = (Button) row_back.getChildAt(0);
			bt.setOnClickListener(new View.OnClickListener() {
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
