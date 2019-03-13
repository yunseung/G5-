package com.ktrental.product;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.ktrental.R;
import com.ktrental.cm.connect.ConnectController;
import com.ktrental.cm.connect.Connector.ConnectInterface;
import com.ktrental.common.DEFINE;
import com.ktrental.dialog.Camera_Dialog;
import com.ktrental.fragment.VocInfoAdapter;
import com.ktrental.model.TableModel;
import com.ktrental.popup.DatepickPopup2;
import com.ktrental.popup.EventPopupC;
import com.ktrental.popup.Popup_Window_DelSearch;
import com.ktrental.popup.Popup_Window_Multy;
import com.ktrental.popup.Popup_Window_PM002_;
import com.ktrental.popup.Popup_Window_PM35_41_42;
import com.ktrental.util.kog;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

public class Menu2_Activity extends BaseActivity implements ConnectInterface, OnClickListener {

	private DatepickPopup2 dp2;

	private Popup_Window_PM002_ pwPM002;
	private Popup_Window_Multy pwMULTY;
	private Popup_Window_PM35_41_42 pwPM354142;

	private Popup_Window_DelSearch pwDelSearch;

	private Intent in;

	private Button bt_carnum;
	private Button bt_process;
	private Button bt_action;

	private Button bt_date1;
	private Button bt_date2;
	private Button bt_search;

	private Menu2_Adapter m3a;
	private VocInfoAdapter mVocAdapter;
	private ListView lv_list;
	private ListView lv_list_voc;

	private ImageView iv_nodata;

	private Button bt_done;
	// VOC 내역 버튼 추가
	// KangHyunJin 20151208
	private Button bt_voc_info;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.menu2_activity);
		in = getIntent();

		init(0);

		pwPM002 = new Popup_Window_PM002_(this);
		pwMULTY = new Popup_Window_Multy(this);
		pwPM354142 = new Popup_Window_PM35_41_42(this);

		pwDelSearch = new Popup_Window_DelSearch(context);

		initView();
		setDateButton();
		initLogin();

		// /////////////여기서부터///////////////
		// pp.hide();
		// myung 20131202 ADD 접수화면 이동 후 from ~ to 날짜 오늘로 셋팅하여 자동으로 조회해야 함.
		// if (in.getBooleanExtra("AUTO_SEARCH", false))
		// new Handler().postDelayed(mSearchDelayRunnable, 1000);
		// goSearch();
		if (in.getStringExtra("STATUS") != null) {
			Log.i("####", "####인텐트는" + in.getStringExtra("STATUS") + "/");
			Log.i("####", "####인텐트는" + in.getStringExtra("DATE1") + "/");
			Log.i("####", "####인텐트는" + in.getStringExtra("DATE2") + "/");
			String process = in.getStringExtra("STATUS");
			String date1 = in.getStringExtra("DATE1");
			String date2 = in.getStringExtra("DATE2");

			bt_date1.setText(getDateFormat(date1));

			// goSearch(process, date1, date2);
			// /////////////여기 까지 주석처리 되있었음. 중간에 뺌. Jonathan 14.10.23 당월현황을 보기
			// 위해서.///////////////
		}
	}

	// myung 20131213 ADD 내역 및 이동 액티비티 활성시에 자동 조회되도록 수정
	private Runnable mSearchDelayRunnable = new Runnable() {
		@Override
		public void run() {
			goSearch();
		}
	};

	private void goSearch(String process, String date1, String date2) {
		pp.show();
		cc.getZMO_1100_RD03(process, date1, date2);
	}

	private void initView() {
		bt_carnum = (Button) findViewById(R.id.menu3_top_bt1_id);
		bt_carnum.setOnClickListener(this);
		bt_process = (Button) findViewById(R.id.menu3_top_bt2_id);
		bt_process.setOnClickListener(this);
		bt_process.setText("전체");
		bt_process.setTag("A");
		bt_action = (Button) findViewById(R.id.menu3_top_bt3_id);
		bt_action.setOnClickListener(this);

		bt_action.setText("진행중");
		bt_action.setTag("N");

		bt_date1 = (Button) findViewById(R.id.menu3_top_bt_date1_id);
		bt_date1.setOnClickListener(this);
		bt_date2 = (Button) findViewById(R.id.menu3_top_bt_date2_id);
		bt_date2.setOnClickListener(this);
		bt_search = (Button) findViewById(R.id.menu3_top_bt_search_id);
		bt_search.setOnClickListener(this);
		lv_list = (ListView) findViewById(R.id.menu3_list_id);
		iv_nodata = (ImageView) findViewById(R.id.list_nodata_id);

		lv_list_voc = (ListView) findViewById(R.id.id_voc_listview);

		bt_done = (Button) findViewById(R.id.menu2_done_id);
		bt_done.setOnClickListener(this);

		bt_voc_info = (Button) findViewById(R.id.menu2_voc_info_id);
		bt_voc_info.setOnClickListener(this);
	}

	private void setDateButton() {
		Calendar cal = Calendar.getInstance();

		int year = cal.get(Calendar.YEAR);
		int month = cal.get(Calendar.MONTH) + 1;
		int day = cal.get(Calendar.DAY_OF_MONTH);

		String month_str = String.format("%02d", month);
		String day_str = String.format("%02d", day);

		bt_date2.setText(year + "." + month_str + "." + day_str);// 오늘날짜

		// myung 20131202 UPDATE 접수화면 이동 후 from ~ to 날짜 오늘로 셋팅하여 자동으로 조회해야 함.
		if (in.getBooleanExtra("AUTO_SEARCH", false)) {
			bt_date1.setText(year + "." + month_str + "." + day_str);
		} else {
			if (day < 7) {
				bt_date1.setText(year + "." + month_str + "." + "01");
			} // 이달의첫날

			else {
				int aday = day - 6;
				String aday_str = String.format("%02d", aday);
				bt_date1.setText(year + "." + month_str + "." + aday_str);
			}
		}

	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		pp.dismiss();
		super.onPause();
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		pp.dismiss();
		super.onBackPressed();
	}

	private String getDate() {
		Calendar cal = Calendar.getInstance();

		int year = cal.get(Calendar.YEAR);
		int month = cal.get(Calendar.MONTH) + 1;
		int day = cal.get(Calendar.DAY_OF_MONTH);

		String month_str = String.format("%02d", month);
		String day_str = String.format("%02d", day);

		return year + "." + month_str + "." + day_str;
	}

	ArrayList<HashMap<String, String>> O_ITAB1;
	ArrayList<HashMap<String, String>> O_ITAB1_VOC;

	@Override
	public void connectResponse(String FuntionName, String resultText, String MTYPE, int resulCode,
			TableModel tableModel) {
		// Log.i("#", "####" + FuntionName + "/" + resultText + "/" + MTYPE +
		// "/"
		// + resulCode);
		if (pp != null)
			pp.hide();
		
		
		
		
		if (MTYPE == null || !MTYPE.equals("S")) {
			
			cc.duplicateLogin(context);
			
			EventPopupC epc = new EventPopupC(this);
			epc.show(resultText);
			return;
		}
		// myung 20131213 ADD NoData임에도 MTYPE S로 들어오는 문제
		if (tableModel == null) {
			EventPopupC epc = new EventPopupC(this);
			epc.show("조건에 맞는 데이터가 없습니다.");
			return;
		}
		if (FuntionName.equals("ZMO_1100_RD03")) {
			
			cc.duplicateLogin(context);
			
			O_ITAB1 = tableModel.getTableArray();

			for (int i = 0; i < O_ITAB1.size(); i++) {
				Set<String> set = O_ITAB1.get(i).keySet();
				Iterator<String> it = set.iterator();
				String key;

				while (it.hasNext()) {
					key = it.next();
					kog.e("Jonathan", "메인 홈  key ===  " + key + "    value  === " + O_ITAB1.get(i).get(key));
				}
			}

			 kog.e("Jonathan", "ZMO_1100_RD03 setList2"); 
			setList();
			
		}
		if (FuntionName.equals("ZMO_1140_RD01")) {

			O_ITAB1_VOC = tableModel.getTableArray();
			// Log.i("####", "####ZMO_1100_RD03 : " + O_ITAB1.size());
			// setListVoc();
			
			cc.duplicateLogin(context);
			
		}

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		// myung 20131213 ADD 내역 리스트 상세조회 종료 후 리스트 재조회
		if (!pp.isShowing()) {
			pp.setMessage("조회 중 입니다.");
			pp.show();
		}
		goSearch();
		super.onResume();
	}

	private void setList() {
		if (O_ITAB1.size() > 0) {
			iv_nodata.setVisibility(View.GONE);
			lv_list.setVisibility(View.VISIBLE);
		} else {
			iv_nodata.setVisibility(View.VISIBLE);
			lv_list.setVisibility(View.GONE);
			return;
		}

		// myung 20131211 UPDATE 접수일자/시간 으로 소팅
		// ArrayList<HashMap<String, String>> tempO_ITAB1 = sortDate(O_ITAB1);
		Collections.sort(O_ITAB1, new Comparator<HashMap<String, String>>() {

			@Override
			public int compare(HashMap<String, String> one, HashMap<String, String> two) {
				// TODO Auto-generated method stub
				return (one.get("RECDT") + one.get("RECTM")).compareTo(two.get("RECDT") + two.get("RECTM"));
			}
		});

		m3a = new Menu2_Adapter(this, R.layout.menu3_row, O_ITAB1);
		lv_list.setAdapter(m3a);
		m3a.notifyDataSetChanged();
		lv_list.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
				m3a.setCheckPosition(position);
			}
		});
		
		
	}

	private void setListVoc() {

		mVocAdapter = new VocInfoAdapter(this, R.layout.voc_info_row, O_ITAB1_VOC);
		lv_list_voc.setAdapter(mVocAdapter);
		mVocAdapter.notifyDataSetChanged();
		lv_list_voc.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
				mVocAdapter.setCheckPosition(position);
			}
		});
	}

	// private ArrayList<HashMap<String, String>>
	// sortDate(ArrayList<HashMap<String, String>> O_ITAB1) {
	//
	// Collections.sort(O_ITAB1, new Comparator<HashMap<String, String>>() {
	//
	// @Override
	// public int compare(HashMap<String, String> one, HashMap<String, String>
	// two) {
	// // TODO Auto-generated method stub
	// return one.get("RECDT").compareTo(two.get("RECDT"));
	// }
	// });
	//
	// return O_ITAB1;
	//
	// }

	public String currentpoint(String result) {
		DecimalFormat df = new DecimalFormat("#,##0");
		DecimalFormatSymbols dfs = new DecimalFormatSymbols();
		dfs.setGroupingSeparator(',');
		df.setGroupingSize(3);
		df.setDecimalFormatSymbols(dfs);

		double inputNum = Double.parseDouble(result);
		result = df.format(inputNum).toString();
		return result;
	}

	@Override
	public void reDownloadDB(String newVersion) {
	}

	@Override
	public void onClick(View arg0) {
		String date;
		Object data;
		switch (arg0.getId()) {
		case R.id.menu3_top_bt_date1_id: // 기간1
			date = bt_date2.getText().toString();
			date = date.replace(".", "");
			dp2 = new DatepickPopup2(this, date, 0);
			dp2.show(bt_date1);
			break;
		case R.id.menu3_top_bt_date2_id: // 기간2
			date = bt_date1.getText().toString();
			date = date.replace(".", "");
			dp2 = new DatepickPopup2(this, date, 1);
			dp2.show(bt_date2);
			break;
		case R.id.menu3_top_bt_search_id: // 조회
			goSearch();
			break;

		case R.id.menu3_top_bt1_id: // 차량번호
			// myung 20131129 UPDATE 차량번호 터치 시 지우기, 조회하기 팝업 뛰우기
			data = bt_carnum.getText().toString();
			if (data != null && !data.toString().equals("")) {
				ViewGroup vg = pwDelSearch.getViewGroup();
				LinearLayout back = (LinearLayout) vg.getChildAt(0);
				final Button del = (Button) back.getChildAt(0);
				del.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						bt_carnum.setText("");
						bt_carnum.setTag("");
						pwDelSearch.dismiss();
					}
				});
				final Button sel = (Button) back.getChildAt(1);
				sel.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						new Camera_Dialog(context).show(bt_carnum);
						pwDelSearch.dismiss();
					}
				});
				pwDelSearch.show(bt_carnum);
			} else {
				new Camera_Dialog(context).show(bt_carnum);
			}

			break;
		case R.id.menu3_top_bt2_id: // 정비유형

			pwPM002.show("PM002", bt_process);
			break;
		case R.id.menu3_top_bt3_id: // 진행상태

			// 2013.11.18 YPKIM
			// 주석 제
			if (bt_process.getTag() == null) {
				EventPopupC epc = new EventPopupC(this);
				epc.show("정비유형을 선택해 주세요");
				return;
			}

			String tag = bt_process.getTag().toString();

			pwMULTY.show("M040", bt_action, true);
			/*
			 * if (tag.equals("10")) { pwMULTY.show("PM035", bt_action, true); }
			 * else if (tag.equals("20")) { pwMULTY.show("PM041", bt_action,
			 * true); } else if (tag.equals("30")) { // 긴급 pwMULTY.show("PM042",
			 * bt_action, true); } else if (tag.equals("A") || tag.equals("50"))
			 * { // 2013.11.18 // YPKIM TAG 50 // 추가.
			 * pwPM354142.show(bt_action); }
			 */

			// 2013.11.18 YPKIM
			// SD622 주석처리.
			// pwMULTY.show("SD622", bt_action, true);
			break;
		case R.id.menu2_done_id: {
			if (m3a == null)
				return;
			if (m3a.getCheckPosition() == -1) {
				EventPopupC epc = new EventPopupC(context);
				epc.show("차량을 선택해 주세요");
				return;
			}
			Intent in = new Intent(this, Menu2_1_Activity.class);
			in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
			HashMap<String, String> hm = O_ITAB1.get(m3a.getCheckPosition());
			String mode = hm.get("TXT").toString();
			if (mode.equals("사고")) {
				in.putExtra("MODE", "A");
			} else if (mode.equals("일반")) {
				in.putExtra("MODE", "B");
			} else if (mode.equals("긴급")) {
				in.putExtra("MODE", "C");
			} else if (mode.equals("타이어")) {
				in.putExtra("MODE", "E");
			} else {
				in.putExtra("MODE", "D");
				// myung
				// 사고/일반/긴급 이외의 대체예약번호 리턴처리
				return;
			}

			// myung 20131218 ADD 설비번호 없는 경우 메세지 후 리턴
			if (hm.get("EQUNR") == null || hm.get("EQUNR").equals(" ")) {
				EventPopupC epc = new EventPopupC(context);
				epc.show("설비번호가 없습니다.");
				return;
			}

			in.putExtra("CUSNAM", hm.get("CUSNAM"));
			in.putExtra("AUFNR", hm.get("AUFNR"));
			// myung 20131218 ADD
			in.putExtra("INVNR", hm.get("INVNR"));
			in.putExtra("EQUNR", hm.get("EQUNR"));
			in.putExtra("KUNNR", hm.get("KUNNR"));

			in.putExtra("is_CarManager", mPermgp); // Jonathan 14.10.23 정비접수
													// 상세내역 화면에서 메뉴의 카매니저 조회를
													// 누를때 안되는 것

			kog.e("Jonathan", "Jonathan Carmanger? :: " + mPermgp);
			kog.e("Jonathan", "Jonathan MODE :: " + mode);
			kog.e("Jonathan", "Jonathan CUSNAM :: " + hm.get("CUSNAM") + "  AUFNR :: " + hm.get("AUFNR") + "  INVNR :: "
					+ hm.get("INVNR") + "  EQUNR :: " + hm.get("EQUNR"));

			startActivity(in);
			overridePendingTransition(0, 0);
		}
			break;
		// VOC 내역 클릭 이벤트
		// KangHyunJin ADD (20151208)
		case R.id.menu2_voc_info_id: {
			if (O_ITAB1 == null || O_ITAB1.size() == 0 || m3a == null || m3a.getCheckPosition() == -1) {
				EventPopupC epc = new EventPopupC(context);
				epc.show("VOC 내역조회할 고객을 선택하세요.");
				return;
			}

			HashMap<String, String> hm = O_ITAB1.get(m3a.getCheckPosition());
			Intent intent = new Intent(this, VocInfoActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
			intent.putExtra(VocInfoActivity.VOC_KUNNR, hm.get("KUNNR"));
			startActivity(intent);

			// goSearchVoc();
		}
			break;
		}
	}

	public boolean isPackageInstalled(Context ctx, String pkgName) {
		try {
			ctx.getPackageManager().getPackageInfo(pkgName, PackageManager.GET_ACTIVITIES);
		} catch (NameNotFoundException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	private void goSearch() {
		// o_itab1 = new ArrayList<HashMap<String,String>>();
		// m2a = new Menu2_Adapter(this, R.layout.menu2_row, o_itab1);
		// lv_menu2.setAdapter(m2a);
		// iv_nodata.setVisibility(View.VISIBLE);

		init(0); // Jonathan 14.06.18
		Object object;
		object = bt_carnum.getText();
		String carnum = object == null || object.equals("") ? " " : object.toString();
		object = bt_process.getTag();
		String process = object == null || object.equals("전체") ? "A" : object.toString();
		object = bt_action.getTag();
		String action = object == null || object.equals("") ? " " : object.toString();
		//
		String date1 = bt_date1.getText().toString().replace(".", "");
		String date2 = bt_date2.getText().toString().replace(".", "");

		pp.show();
		// Log.i("####", "#### 오래걸리는 작업" + carnum + "/" + process + "/" + action
		// + "/" + date1 + "/" + date2 + "/");
		if(ConnectController.getInstance() == null){
			new ConnectController(this,this);
		}
		cc.getZMO_1100_RD03(carnum, process, action, date1, date2);
	}

	private void goSearchVoc() {
		// o_itab1 = new ArrayList<HashMap<String,String>>();
		// m2a = new Menu2_Adapter(this, R.layout.menu2_row, o_itab1);
		// lv_menu2.setAdapter(m2a);
		// iv_nodata.setVisibility(View.VISIBLE);

		init(0); // Jonathan 14.06.18
		Object object;
		object = bt_carnum.getText();
		String carnum = object == null || object.equals("") ? " " : object.toString();
		object = bt_process.getTag();
		String process = object == null || object.equals("전체") ? "A" : object.toString();
		object = bt_action.getTag();
		String action = object == null || object.equals("") ? " " : object.toString();
		//
		String date1 = bt_date1.getText().toString().replace(".", "");
		String date2 = bt_date2.getText().toString().replace(".", "");

		pp.show();
		// Log.i("####", "#### 오래걸리는 작업" + carnum + "/" + process + "/" + action
		// + "/" + date1 + "/" + date2 + "/");
		cc.getZMO_1100_RD03(carnum, process, action, date1, date2);
	}

	public HashMap<String, String> login_hm;

	private HashMap<String, String> initLogin() {
		login_hm = new HashMap<String, String>();
		String path = context.getExternalCacheDir() + "/DATABASE/" + DEFINE.SQLLITE_DB_NAME;
		try {
			SQLiteDatabase sqlite = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.OPEN_READONLY);
			Cursor cursor = sqlite.rawQuery("select * from " + "LOGIN_STRUCT_TABLE", null);
			while (cursor.moveToNext()) {
				for (int i = 0; i < cursor.getColumnCount(); i++) {
					login_hm.put(cursor.getColumnName(i), cursor.getString(i));
				}
			}
			cursor.close();
		} catch (Exception e){
			e.printStackTrace();
		}

		// 2017.07.05. hjt. db close 는 app 종료시 알아서 된다
		// 이것때문에 throwIfClosedLocked Exception 발생한다
//		sqlite.close();
		return login_hm;
	}

}
