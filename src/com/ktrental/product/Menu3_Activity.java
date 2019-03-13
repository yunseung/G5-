package com.ktrental.product;

import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.ktrental.R;
import com.ktrental.beans.MULTY;
import com.ktrental.cm.connect.Connector.ConnectInterface;
import com.ktrental.common.DEFINE;
import com.ktrental.dialog.Camera_Dialog;
import com.ktrental.dialog.Cost_Resist_Dialog;
import com.ktrental.dialog.Driver_Info_Dialog;
import com.ktrental.dialog.Menu3_Resist_Dialog;
import com.ktrental.model.TableModel;
import com.ktrental.popup.CalendarPopup;
import com.ktrental.popup.DatepickPopup2;
import com.ktrental.popup.EventPopupC;
import com.ktrental.popup.InventoryPopup;
import com.ktrental.popup.InventoryPopup_Dot;
import com.ktrental.popup.Popup_Window_DelSearch;
import com.ktrental.popup.Popup_Window_Multy;
import com.ktrental.util.LogUtil;
import com.ktrental.util.SharedPreferencesUtil;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

public class Menu3_Activity extends BaseActivity implements ConnectInterface,
		OnClickListener {

	private Popup_Window_Multy pwm;
	private DatepickPopup2 dp2;

	private Button bt_top1;
	private Button bt_top2;
	private Button bt_top3;
	private Button bt_search;
	private Button bt_date;
	private Button bt_date1;
	private Button bt_date2;

	private ArrayList<HashMap<String, String>> o_itab1;
	private ArrayList<HashMap<String, String>> o_itab2;
	private ArrayList<HashMap<String, String>> o_itab3;
	private ArrayList<HashMap<String, String>> o_itab4;
	private Menu3_Adapter m2a;
	private ListView lv_menu2;
	private ImageView iv_nodata;
	private HashMap<String, String> o_struct1;

	private boolean SEARCH = false;

	private TextView tv_driver[] = new TextView[4];
	private TextView tv_start[] = new TextView[5];
	private TextView tv_end[] = new TextView[7];

	private Button bt_driver;

	private TextView tv_start_km;
	private Button bt_start_m026;
	private TextView tv_start_dfm;
	private Button bt_start_time;
	private Button bt_start_park;
	private Button bt_start_oil1;

	private TextView tv_end_km;
	private Button bt_end_m026;
	private TextView tv_end_dfm;
	private Button bt_end_time;
	private Button bt_end_cost;
	private Button bt_end_park;
	private Button bt_end_oil1;
	private Button bt_start_resist;
	private Button bt_start_paperess;
	private Button bt_end_resist;
	private Button bt_end_paperess;
	private TextView bt_start_bigo;
	private EditText bt_end_bigo;
	private Button bt_stepby;

	private InventoryPopup ip;
	private InventoryPopup_Dot ipd;

	private String MODIYN = "N";
	private String PAPLYN = "N";

	private int SEARCHED = -1;

	private boolean REFRESH = false;

	private Intent intent;

	private boolean RESUME = false;

	private CalendarPopup cp;

	private RadioGroup rg_menu3;
	private RadioButton rb_bt1, rb_bt2;

	private Popup_Window_DelSearch pwDelSearch;
	
	

	Menu3_Resist_Dialog m3rd;

	SharedPreferencesUtil shareU = SharedPreferencesUtil.getInstance();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.menu3_activity);

		
		
		init(0);

		pwm = new Popup_Window_Multy(this);
		pwDelSearch = new Popup_Window_DelSearch(context);
		// ip = new InventoryPopup(context, QuantityPopup.HORIZONTAL,
		// R.layout.inventory_popup, QuantityPopup.TYPE_NOMARL_NUMBER);
		// ipd = new InventoryPopup_Dot(context);

		initView();
		cost.put("GDOIL2", "0");
		cost.put("OILPRI", "0");
		cost.put("WASH", "0");
		cost.put("PARK", "0");
		cost.put("TOLL", "0");
		cost.put("OTRAMT", "0");
		cost.put("OPERA", "0");

		setDateButton();
		initLogin();
		// pp.hide();
		// myung 20131213 ADD 이동 액티비티 활성시에 자동 조회되도록 수정

		/**
		 * 20190118. hjt. 진입하자마자 전체조회 막아달라 요청 옴. (이기호책임님)
		 */
//		goSearch();

	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setIntent(intent);

	}

	@Override
	protected void onRestart() {
		super.onRestart();
//		String str = getIntent().getStringExtra("refresh");
		// Log.e("str", ""+str);
//		if (str == null || str.equals(""))
//			return;
//		callMenu3RegistDlg();
//		getIntent().putExtra("refresh", "");
		// if (str.equals("y")) {
		// onClearField();
		// getZMO_3140_RD01();
		// }

		// 리프레쉬 호출하는법
		// Intent i = new Intent("ktrental.launcher");
		// i.putExtra("refresh","n");
		// i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
		// i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
		// i.setComponent(new
		// ComponentName("com.ktrental","com.ktrental.product.Menu3_Activity"));
		// startActivity(i);
	}

	@Override
	protected void onResume() {
		super.onResume();
		// boolean bFlag = shareU.getBoolean("flag_dialog_dismiss", false);
		// if(bFlag){
		// shareU.setBoolean("flag_dialog_dismiss", false);
		// goSearch();
		// }

		// myung 20131213 ADD 페이버리스 처리 후 출발도착등록 재갱신 처리
		if (DEFINE.PAPERESS_STATUS_FLAG) {
			DEFINE.PAPERESS_STATUS_FLAG = false;
			callMenu3RegistDlg();
		}

	}

	private void initView() {
		bt_top1 = (Button) findViewById(R.id.menu2_top_bt1_id);
		bt_top1.setOnClickListener(this);
		bt_top2 = (Button) findViewById(R.id.menu2_top_bt2_id);
		bt_top2.setOnClickListener(this);
		bt_top3 = (Button) findViewById(R.id.menu2_top_bt3_id);
		bt_top3.setOnClickListener(this);
		bt_search = (Button) findViewById(R.id.menu2_top_bt_search_id);
		bt_search.setOnClickListener(this);
		bt_date = (Button) findViewById(R.id.menu2_top_bt_date_id);
		bt_date.setOnClickListener(this);
		bt_date1 = (Button) findViewById(R.id.menu2_top_bt_date1_id);
		bt_date1.setOnClickListener(this);
		bt_date2 = (Button) findViewById(R.id.menu2_top_bt_date2_id);
		bt_date2.setOnClickListener(this);

		lv_menu2 = (ListView) findViewById(R.id.menu2_list_id);
		iv_nodata = (ImageView) findViewById(R.id.list_nodata_id);

		// tv_driver[0] = (TextView)findViewById(R.id.menu2_driver1_1);
		// tv_driver[1] = (TextView)findViewById(R.id.menu2_driver1_2);
		// tv_driver[2] = (TextView)findViewById(R.id.menu2_driver2_1);
		// tv_driver[3] = (TextView)findViewById(R.id.menu2_driver2_2);
		//
		// tv_start[0] = (TextView)findViewById(R.id.menu2_start1_1);
		// tv_start[1] = (TextView)findViewById(R.id.menu2_start1_2);
		// tv_start[2] = (TextView)findViewById(R.id.menu2_start2_1);
		// tv_start[3] = (TextView)findViewById(R.id.menu2_start4_1);
		// tv_start[4] = (TextView)findViewById(R.id.menu2_start4_2);
		//
		// tv_end[0] = (TextView)findViewById(R.id.menu2_end1_1);
		// tv_end[1] = (TextView)findViewById(R.id.menu2_end1_2);
		// tv_end[2] = (TextView)findViewById(R.id.menu2_end2_1);
		// tv_end[3] = (TextView)findViewById(R.id.menu2_end4_1);
		// tv_end[4] = (TextView)findViewById(R.id.menu2_end4_2);
		// tv_end[5] = (TextView)findViewById(R.id.menu2_end4_3);
		// tv_end[6] = (TextView)findViewById(R.id.menu2_end5_1);
		//
		// bt_driver = (Button)findViewById(R.id.menu2_driver_input);
		// bt_driver.setOnClickListener(this);
		// tv_start_km = (TextView)findViewById(R.id.menu2_start_input1);
		// tv_start_km.setOnClickListener(this);
		// bt_start_m026 = (Button)findViewById(R.id.menu2_start_input2);
		// bt_start_m026.setOnClickListener(this);
		// tv_start_dfm = (TextView)findViewById(R.id.menu2_start_input3);
		// tv_start_dfm.setOnClickListener(this);
		// bt_start_time = (Button)findViewById(R.id.menu2_start_input4);
		// bt_start_time.setOnClickListener(this);
		// bt_start_park = (Button)findViewById(R.id.menu2_start2_2);
		// bt_start_park.setOnClickListener(this);
		// bt_start_oil1 = (Button)findViewById(R.id.menu2_start3_1);
		// bt_start_oil1.setOnClickListener(this);
		//
		// tv_end_km = (TextView)findViewById(R.id.menu2_end_input1);
		// tv_end_km.setOnClickListener(this);
		// bt_end_m026 = (Button)findViewById(R.id.menu2_end_input2);
		// bt_end_m026.setOnClickListener(this);
		// tv_end_dfm = (TextView)findViewById(R.id.menu2_end_input3);
		// tv_end_dfm.setOnClickListener(this);
		// bt_end_time = (Button)findViewById(R.id.menu2_end_input4);
		// bt_end_time.setOnClickListener(this);
		// bt_end_cost = (Button)findViewById(R.id.menu2_end_input5);
		// bt_end_cost.setOnClickListener(this);
		// bt_end_park = (Button)findViewById(R.id.menu2_end2_2);
		// bt_end_park.setOnClickListener(this);
		// bt_end_oil1 = (Button)findViewById(R.id.menu2_end3_1);
		// bt_end_oil1.setOnClickListener(this);
		//
		// bt_start_resist = (Button)findViewById(R.id.menu2_start_resist);
		// bt_start_paperess = (Button)findViewById(R.id.menu2_start_paperess);
		// bt_end_resist = (Button)findViewById(R.id.menu2_end_resist);
		// bt_end_paperess = (Button)findViewById(R.id.menu2_end_paperess);
		// bt_start_resist.setOnClickListener(this);
		// bt_start_paperess.setOnClickListener(this);
		// bt_end_resist.setOnClickListener(this);
		// bt_end_paperess.setOnClickListener(this);
		//
		// bt_stepby = (Button)findViewById(R.id.menu2_stepby_1);
		// bt_stepby.setOnClickListener(this);
		//
		// bt_start_bigo = (TextView)findViewById(R.id.menu2_start_bigo);
		// bt_end_bigo = (EditText)findViewById(R.id.menu2_end_bigo);
		rg_menu3 = (RadioGroup) findViewById(R.id.menu3_radiogroup_id);
		// rg_menu3.getCheckedRadioButtonId()
		// rb_bt1 = (RadioButton)findViewById(R.id.menu3_radiobutton1_id);
		// rb_bt2 = (RadioButton)findViewById(R.id.menu3_radiobutton2_id);
	}

	private void getZMO_3140_RD01() {
		pp.setMessage("상세 조회중 입니다.");
		pp.show();
		String top1 = bt_top1.getTag().toString();
		String top2 = bt_top2.getTag().toString();
		String top3 = bt_top3.getTag().toString();

		String period = bt_date.getTag().toString();
		String date1 = bt_date1.getText().toString().replace(".", "");
		String date2 = bt_date2.getText().toString().replace(".", "");

		// Log.i("####",
		// "#### 이동요청번호"+o_itab1.get(m2a.getCheckPosition()).get("VBELN_VL"));
		// Log.i("####",
		// "#### 상세조회" + top1 + "/" + top2 + "/" + top3 + "/" + period
		// + "/" + date1 + "/" + date2 + "/"
		// + o_itab1.get(m2a.getCheckPosition()).get("VBELN_VL"));
		cc.getZMO_3140_RD01(top1, top2, top3, period, date1, date2, o_itab1
				.get(m2a.getCheckPosition()).get("VBELN_VL"));
	}

	private void setEnable(boolean bool) {
		bt_top1.setEnabled(bool);
		bt_top2.setEnabled(bool);
		bt_top3.setEnabled(bool);
		bt_search.setEnabled(bool);
		bt_date.setEnabled(bool);
		bt_date1.setEnabled(bool);
		bt_date2.setEnabled(bool);
	}

	private void setDateButton() {

		// myung 20131129 UPDATE 이동 > 조회기간 변경 필요. 현재일 -7 일 ~ 현재일 + 7 일 로 변경.

		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, 7);

		int year = cal.get(Calendar.YEAR);
		int month = cal.get(Calendar.MONTH) + 1;
		int day = cal.get(Calendar.DAY_OF_MONTH);

		String month_str = String.format("%02d", month);
		String day_str = String.format("%02d", day);

		bt_date2.setText(year + "." + month_str + "." + day_str); // 현재일 + 7 일

		cal.add(Calendar.DATE, -14);

		year = cal.get(Calendar.YEAR);
		month = cal.get(Calendar.MONTH) + 1;
		day = cal.get(Calendar.DAY_OF_MONTH);

		month_str = String.format("%02d", month);
		day_str = String.format("%02d", day);

		bt_date1.setText(year + "." + month_str + "." + day_str); // 현재일 - 7 일

		// Calendar cal = Calendar.getInstance();
		//
		// int year = cal.get(Calendar.YEAR);
		// int month = cal.get(Calendar.MONTH) + 1;
		// int day = cal.get(Calendar.DAY_OF_MONTH);
		// String month_str = String.format("%02d", month);
		// String day_str = String.format("%02d", day);
		//
		// bt_date2.setText(year + "." + month_str + "." + day_str);// 오늘날짜
		// if (day < 7) {
		// bt_date1.setText(year + "." + month_str + "." + "01");
		// }// 이달의첫날
		//
		// else {
		// int aday = day - 6;
		// String aday_str = String.format("%02d", aday);
		// bt_date1.setText(year + "." + month_str + "." + aday_str);
		// }
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		pp.dismiss();
		super.onPause();
	}

	@Override
	public void connectResponse(String FuntionName, String resultText,
			String MTYPE, int resulCode, TableModel tableModel) {

		// Log.i("#", "####" + FuntionName + "/" + resultText + "/" + MTYPE +
		// "/"
		// + resulCode);
		//myung 20131217 ADD 내역, 이동 메뉴를 반복하여 이동하면 앱 종료 됨.(예외처리)
		if(pp!=null)
			pp.hide();
		if (MTYPE == null || !MTYPE.equals("S")) {
			
			cc.duplicateLogin(context);
			
			
			SEARCH = false; // 조회버튼 플래그 해제
			EventPopupC epc = new EventPopupC(this);
			epc.show(resultText);
			return;
		}
		if (FuntionName.equals("ZMO_3140_RD01")) {
			BUKRS = "3000";
			cc.duplicateLogin(context);
		}
		if (FuntionName.equals("ZMO_3200_RD01")) {
			BUKRS = "3100";
			cc.duplicateLogin(context);
		}
		if (FuntionName.equals("ZMO_3140_RD01")
				|| FuntionName.equals("ZMO_3140_WR01")
				|| FuntionName.equals("ZMO_3140_WR02")
				|| FuntionName.equals("ZMO_3200_RD01")) {
			if (SEARCH) // 조회눌림
			{
				o_itab1 = tableModel.getTableArray("O_ITAB1");

				if (o_itab1.size() > 0) {
					iv_nodata.setVisibility(View.GONE);
				} else {
					iv_nodata.setVisibility(View.VISIBLE);
					return;
				}

				m2a = new Menu3_Adapter(this, R.layout.menu2_row, o_itab1);
				lv_menu2.setAdapter(m2a);
				lv_menu2.setOnItemClickListener(new OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1,
							int position, long arg3) {
						m2a.setCheckPosition(position);
					}
				});
			} else if (FuntionName.equals("ZMO_3140_WR01")) {
				EventPopupC epc = new EventPopupC(context);
				epc.show("출발등록이 완료 되었습니다.");
				REFRESH = true;
				o_struct1 = tableModel.getStruct("O_STRUCT1");

				init("M026", o_struct1.get("GAGTY"));
				setContents();

				initPark();
			} else if (FuntionName.equals("ZMO_3140_WR02")) {
				EventPopupC epc = new EventPopupC(context);
				epc.show("도착등록이 완료 되었습니다.");
				REFRESH = true;
				o_struct1 = tableModel.getStruct("O_STRUCT1");

				init("M026", o_struct1.get("GAGTY"));
				setContents();

				initPark();
			} else {
				o_struct1 = tableModel.getStruct("O_STRUCT1");
				o_itab2 = tableModel.getTableArray("O_ITAB2");
				o_itab3 = tableModel.getTableArray("O_ITAB3");
				o_itab4 = tableModel.getTableArray("O_ITAB4");

				init("M026", o_struct1.get("GAGTY"));
				setContents();

				initPark();
			}
			
			cc.duplicateLogin(context);
			
		} else if (FuntionName.equals("ZMO_3140_WR03")) {
			getZMO_3140_RD01();
		} else if (FuntionName.equals("ZMO_3140_WR04")) {
		}

		SEARCH = false; // 조회버튼 플래그 해제
	}

	ArrayList<MULTY> start_park;
	ArrayList<MULTY> end_park;

	private void initPark() {
		// Log.i("#####", "####주차장 정의"+o_itab4.size());
		start_park = new ArrayList<MULTY>();
		end_park = new ArrayList<MULTY>();
		for (int i = 0; i < o_itab4.size(); i++) {
			HashMap hm = o_itab4.get(i);
			// Log.i("#####", "####주차장 " + hm.get("GUBUN") + "/" +
			// hm.get("TPLNR")
			// + "/" + hm.get("TPLNR_TX"));
			MULTY multy = new MULTY();
			multy.ZCODEV = hm.get("TPLNR").toString();
			multy.ZCODEVT = hm.get("TPLNR_TX").toString();
			if (hm.get("GUBUN").equals("D")) {
				start_park.add(multy);
			} else if (hm.get("GUBUN").equals("A")) {
				end_park.add(multy);
			}
		}
	}

	private String DATE;
	private String TIME;

	private void setContents() {
		tv_driver[0].setText(o_struct1.get("VSBED_TX")); // 운송방법
		tv_driver[1].setText(o_struct1.get("TELF13")); // 운전자전화번호
		tv_driver[2].setText(o_struct1.get("PAYFR_TX"));
		tv_driver[3].setText(o_struct1.get("MAKTX"));

		tv_start[0].setText(o_struct1.get("PLDPT_TX"));
		tv_start[1].setText(o_struct1.get("PLACE1"));
		tv_start[2].setText(o_struct1.get("ZONEA1_TX"));

		bt_start_park.setText(o_struct1.get("PLTXT_FR"));
		bt_start_park.setTag(o_struct1.get("TPLNR_FR"));

		// tv_start[3].setText(o_struct1.get("GAGTY_TX"));

		bt_start_oil1.setText(o_struct1.get("GAGTY_TX"));
		tv_start[3].setText(o_struct1.get("PERNR1"));
		tv_start[4].setText(o_struct1.get("TELF11"));

		tv_end[0].setText(o_struct1.get("PLARV_TX"));
		tv_end[1].setText(o_struct1.get("PLACE2"));
		tv_end[2].setText(o_struct1.get("ZONEA2_TX"));
		bt_end_park.setText(o_struct1.get("PLTXT_TO"));
		bt_end_park.setTag(o_struct1.get("TPLNR_TO"));
		// tv_end[3].setText(o_struct1.get("GAGTY_TX"));
		bt_end_oil1.setText(o_struct1.get("GAGTY_TX"));
		tv_end[3].setText(currentpoint(o_struct1.get("INKM")));
		tv_end[4].setText(o_struct1.get("PERNR2"));
		tv_end[5].setText(o_struct1.get("TELF12"));
		tv_end[6].setText(o_struct1.get("WADTM"));

		bt_driver.setText(o_struct1.get("NAME1_CD"));
		tv_start_km.setText(currentpoint(o_struct1.get("OUTKM")));

		String fuel = "";
		for (int i = 0; i < m026_arr.size(); i++) {
			if (m026_arr.get(i).ZCODEV.equals(o_struct1.get("OUTOIL"))) {
				fuel = m026_arr.get(i).ZCODEVT;
				break;
			}
		}

		bt_start_m026.setText(fuel);
		tv_start_dfm.setText(o_struct1.get("OUTGDOIL"));

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.ddHH:mm");
		String date = sdf.format(new Date());
		DATE = date.substring(0, 10);
		TIME = date.substring(10);

		bt_start_time.setText(DATE + " " + TIME);

		tv_end_km.setText(currentpoint(o_struct1.get("INKM")));
		for (int i = 0; i < m026_arr.size(); i++) {
			// Log.i("####",
			// "####유종"+m026_arr.get(i).ZCODEV+"/"+o_struct1.get("INOIL"));
			if (m026_arr.get(i).ZCODEV.equals(o_struct1.get("INOIL"))) {
				fuel = m026_arr.get(i).ZCODEVT;
				break;
			}
		}

		bt_end_m026.setText(fuel);
		tv_end_dfm.setText(o_struct1.get("INGDOIL"));
		bt_end_time.setText(o_struct1.get("ETTM"));
		bt_end_cost.setText(o_struct1.get("OILPRI"));

		if (o_struct1.get("MODIYN").equals("A")) {
			MODIYN = "A";
			setYN_A();
		} else if (o_struct1.get("MODIYN").equals("B")) {
			MODIYN = "B";
			setYN_B();
		} else if (o_struct1.get("MODIYN").equals("N")) {
			MODIYN = "N";
			setYN_N();
		} else {
			MODIYN = "N";
			setYN_N();
		}
		if (o_struct1.get("PAPLYN").equals("A")) {
			PAPLYN = "A";
			bt_start_paperess.setEnabled(true);
			bt_end_paperess.setEnabled(false);
		} else if (o_struct1.get("PAPLYN").equals("B")) {
			PAPLYN = "B";
			bt_start_paperess.setEnabled(false);
			bt_end_paperess.setEnabled(true);
		} else if (o_struct1.get("PAPLYN").equals("N")) {
			PAPLYN = "N";
			bt_start_paperess.setEnabled(false);
			bt_end_paperess.setEnabled(false);
		//myung 20131218 ADD PAPLYN값 Y 추가
		} else if (o_struct1.get("PAPLYN").equals("Y")) {
			PAPLYN = "Y";
			bt_start_paperess.setEnabled(true);
			bt_end_paperess.setEnabled(true);
		} else {
			PAPLYN = "N";
			bt_start_paperess.setEnabled(false);
			bt_end_paperess.setEnabled(false);
		}

		bt_start_bigo.setText(o_struct1.get("CUST_DESC1"));
		bt_end_bigo.setText(o_struct1.get("CUST_DESC1"));
	}

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

	private void setYN_A() {
		bt_driver.setEnabled(true);
		tv_start_km.setEnabled(true);
		String oil1 = o_struct1.get("GAGTY_TX");
		// Log.i("####", "####오일"+oil1+"/");
		if (oil1.equals(" ")) {
			bt_start_oil1.setEnabled(true);
		} else {
			bt_start_oil1.setEnabled(false);
		}
		bt_start_m026.setEnabled(true);
		tv_start_dfm.setEnabled(true);
		bt_start_time.setEnabled(true);
		bt_start_park.setEnabled(false);
		bt_start_resist.setEnabled(true);

		bt_stepby.setEnabled(false);

		tv_end_km.setEnabled(false);

		bt_end_oil1.setEnabled(false);
		bt_end_oil1.setEnabled(false);
		bt_end_m026.setEnabled(false);
		tv_end_dfm.setEnabled(false);
		bt_end_time.setEnabled(false);

		bt_end_park.setEnabled(false);

		bt_end_bigo.setEnabled(false);

		bt_end_resist.setEnabled(false);
	}

	private void setYN_B() {
		bt_driver.setEnabled(false);
		tv_start_km.setEnabled(false);
		String oil1 = o_struct1.get("GAGTY_TX");
		// Log.i("####", "####오일"+oil1+"/");

		bt_start_oil1.setEnabled(false);
		bt_start_m026.setEnabled(false);
		tv_start_dfm.setEnabled(false);
		bt_start_time.setEnabled(false);
		bt_start_resist.setEnabled(false);
		bt_start_park.setEnabled(true);
		bt_stepby.setEnabled(true);

		tv_end_km.setEnabled(true);
		if (oil1.equals(" ")) {
			bt_end_oil1.setEnabled(true);
		} else {
			bt_end_oil1.setEnabled(false);
		}
		bt_end_oil1.setEnabled(true);
		bt_end_m026.setEnabled(true);
		tv_end_dfm.setEnabled(true);
		bt_end_time.setEnabled(true);

		bt_end_park.setEnabled(true);

		bt_end_bigo.setEnabled(true);

		bt_end_resist.setEnabled(true);
	}

	private void setYN_N() {
		bt_driver.setEnabled(false);
		tv_start_km.setEnabled(false);
		String oil1 = o_struct1.get("GAGTY_TX");
		// Log.i("####", "####오일"+oil1+"/");

		bt_start_oil1.setEnabled(false);
		bt_start_m026.setEnabled(false);
		tv_start_dfm.setEnabled(false);
		bt_start_time.setEnabled(false);
		bt_start_resist.setEnabled(false);
		bt_start_park.setEnabled(true);
		bt_stepby.setEnabled(true);

		tv_end_km.setEnabled(false);
		bt_end_oil1.setEnabled(false);
		bt_end_m026.setEnabled(false);
		tv_end_dfm.setEnabled(false);
		bt_end_time.setEnabled(false);

		bt_end_park.setEnabled(true);

		bt_end_bigo.setEnabled(false);

		bt_end_resist.setEnabled(false);
	}

	@Override
	public void reDownloadDB(String newVersion) {
	}

	@Override
	public void onClick(View arg0) {
		ArrayList<MULTY> list = new ArrayList<MULTY>();
		MULTY multy;
		String date;
		EventPopupC epc;
		HashMap<String, String> i_struct1;
		ViewGroup vg;
		Button done;
		switch (arg0.getId()) {
		case R.id.menu2_top_bt1_id:
			pwm.show("M031", bt_top1);
			break;
		case R.id.menu2_top_bt2_id: // 진행상태
			// myung 20131118 UPDATE 상태 변경 시 오류 수정
			// multy = new MULTY(); multy.ZCODEVT = "전체"; multy.ZCODEV = " ";
			// list.add(multy);
			// multy = new MULTY(); multy.ZCODEVT = "출발대상"; multy.ZCODEV = "1";
			// list.add(multy);
			// multy = new MULTY(); multy.ZCODEVT = "도착대상"; multy.ZCODEV = "2";
			// list.add(multy);
			// pwm.show(list, bt_top2);
			pwm.show("SD622", bt_top2);
			break;
		case R.id.menu2_top_bt3_id:
			// myung 20131202 UPDATE 차량번호 버튼 클릭 시 지우기 | 조회하기 팝업 호출
			String data1 = bt_top3.getText().toString();
			if (data1 != null && !data1.toString().equals("")
					&& !data1.toString().equals("전체")) {
				ViewGroup vg1 = pwDelSearch.getViewGroup();
				LinearLayout back = (LinearLayout) vg1.getChildAt(0);
				final Button del = (Button) back.getChildAt(0);
				del.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						bt_top3.setText("");
						bt_top3.setTag("");
						pwDelSearch.dismiss();
					}
				});
				final Button sel = (Button) back.getChildAt(1);
				sel.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						new Camera_Dialog(context).show(bt_top3);
						pwDelSearch.dismiss();
					}
				});
				pwDelSearch.show(bt_top3);
			} else {
				final Camera_Dialog cd = new Camera_Dialog(this);
				Button bt_done = (Button) cd.findViewById(R.id.camera_done_id);
				final EditText et_carnum = (EditText) cd
						.findViewById(R.id.camera_carnum_id);
				bt_done.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						if (et_carnum.getText() == null) {
							EventPopupC epc = new EventPopupC(context);
							epc.show("차량번호를 입력해 주세요.");
							return;
						}
						bt_top3.setText(et_carnum.getText().toString());
						cd.dismiss();
					}
				});
				cd.show();
			}

			// final Camera_Dialog cd = new Camera_Dialog(this);
			// Button bt_done = (Button) cd.findViewById(R.id.camera_done_id);
			// final EditText et_carnum = (EditText) cd
			// .findViewById(R.id.camera_carnum_id);
			// bt_done.setOnClickListener(new OnClickListener() {
			//
			// @Override
			// public void onClick(View v) {
			// if (et_carnum.getText() == null) {
			// EventPopupC epc = new EventPopupC(context);
			// epc.show("차량번호를 입력해 주세요.");
			// return;
			// }
			// bt_top3.setText(et_carnum.getText().toString());
			// cd.dismiss();
			// }
			// });
			// cd.show();
			break;
		case R.id.menu2_top_bt_search_id: // 조회
			goSearch();

			break;
		case R.id.menu2_top_bt_date_id: // 기간
			multy = new MULTY();
			multy.ZCODEVT = "이동요청일";
			multy.ZCODEV = "1";
			list.add(multy);
			multy = new MULTY();
			multy.ZCODEVT = "도착요청일";
			multy.ZCODEV = "2";
			list.add(multy);
			pwm.show(list, bt_date);
			break;
		case R.id.menu2_top_bt_date1_id: // 기간1
			date = bt_date2.getText().toString();
			date = date.replace(".", "");
			dp2 = new DatepickPopup2(this, date, 0);
			dp2.show(bt_date1);
			break;
		case R.id.menu2_top_bt_date2_id: // 기간2
			date = bt_date1.getText().toString();
			date = date.replace(".", "");
			dp2 = new DatepickPopup2(this, date, 3);
			dp2.show(bt_date2);
			break;
		case R.id.menu2_start_resist:
			pp.setMessage("출발등록 중 입니다.");
			pp.show();

			i_struct1 = new HashMap<String, String>();
			i_struct1.put("VBELN_VL", o_struct1.get("VBELN_VL")); // 이동번호
			i_struct1.put("VBELN_VA", o_struct1.get("VBELN_VA")); // 판매문서번호(영업)
			i_struct1.put("VKGRP", o_struct1.get("VKGRP")); // 지점코드
			i_struct1.put("DAART", o_struct1.get("DAART")); // 이동유형
			i_struct1.put("LFART", o_struct1.get("LFART")); // 납품유형코드
			i_struct1.put("RSMOV", o_struct1.get("RSMOV")); // 이동사유
			i_struct1.put("EQUNR", o_struct1.get("EQUNR")); // 설비번호
			i_struct1.put("VSBED", o_struct1.get("VSBED")); // 운송방법코드
			i_struct1.put("PAYFR", o_struct1.get("PAYFR")); // 운임지불코드
			i_struct1.put("LIFNR_CD", o_struct1.get("LIFNR_CD")); // 운전자코드
			i_struct1.put("OSTEP", o_struct1.get("LIFNR_CD")); // 운행단계코드
			i_struct1.put("PLDPT", o_struct1.get("PLDPT")); // [출발]출발지구분코드
			i_struct1.put("PLACE1", o_struct1.get("PLACE1")); // [출발]출발지
			i_struct1.put("TPLNR_FR", bt_start_park.getTag().toString()); // [출발]주차장코드(기능위치)
			i_struct1.put("PSTLZ1", o_struct1.get("PSTLZ1")); // [출발]우편번호
			i_struct1.put("ORT011", o_struct1.get("ORT011")); // [출발]우편주소
			i_struct1.put("STRAS1", o_struct1.get("STRAS1")); // [출발]상세주소
			i_struct1.put("PERNR1", o_struct1.get("PERNR1")); // [출발]담당자
			i_struct1.put("TELF11", o_struct1.get("TELF11")); // [출발]담당자연락처

			i_struct1.put("OUTKM", tv_start_km.getText().toString()); // [출발]KM
			i_struct1.put("OUTOIL", bt_start_m026.getTag().toString()); // [출발]Oil
																		// 코드
			i_struct1.put("OUTGDOIL", tv_start_dfm.getText().toString()); // [출발]Oil
																			// 양(DFM)

			String time = bt_start_time.getText().toString();
			time = time.replace(".", "").replace(":", "");
			String start_date = time.substring(0, 8);
			String start_time = time.substring(9);

			i_struct1.put("GUEBG", start_date); // [출발]출발일자
			i_struct1.put("STIME", start_time); // [출발]출발시간

			cc.setZMO_3140_WR01(i_struct1);
			break;
		case R.id.menu2_start_paperess:
			epc = new EventPopupC(this);

			boolean isInstalled = isPackageInstalled(context,
					"com.ktkumhorental.pdfform");
			// Log.i("####", "#### 인스톨상태입니까?" + isInstalled);

			if (isInstalled) {
				Intent i = new Intent();
				String info = " :" + login_hm.get("LOGID") + ":"
						+ login_hm.get("PERNR") + ":"
						+ o_struct1.get("PSTYPE1") + ":"
						+ o_struct1.get("PSTYPE2") + ":"
						+ o_struct1.get("PSTYPE3") + ":"
						+ o_struct1.get("VBELN_PS") + ":"
						+ o_struct1.get("PSTYPE4") + ":"
						+ login_hm.get("BUKRS") + ":" + login_hm.get("DEPTNM")
						+ ":" + login_hm.get("ENAME");

				// Log.i("####", "####인포" + info);
				epc.show(info);

				i.putExtra("INFO", info);
				i.setAction(Intent.ACTION_MAIN);
				i.addCategory(Intent.CATEGORY_LAUNCHER);
				i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
						| Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
				i.setComponent(new ComponentName("com.ktkumhorental.pdfform",
						"com.ktkumhorental.pdfform.PdfControlWindow"));
				// i.setComponent(new
				// ComponentName("com.example.aaa_intent_app","com.example.aaa_intent_app.MainActivity"));
				startActivity(i);
			} else {
				Intent intent = new Intent(Intent.ACTION_VIEW,
						Uri.parse(DEFINE.PAPER_DOWN_URL));
				startActivity(intent);
			}
			break;
		case R.id.menu2_end_resist:
			pp.setMessage("도착등록 중 입니다.");
			pp.show();

			// ostruct4 vbeln 판매문
			
			i_struct1 = new HashMap<String, String>();
			i_struct1.put("VBELN_VL", o_struct1.get("VBELN_VL")); // 이동번호
			i_struct1.put("VBELN_VA", o_struct1.get("VBELN_VA")); // 판매문서번호(영업)
			i_struct1.put("VKGRP", o_struct1.get("VKGRP")); // 지점코드
			i_struct1.put("DAART", o_struct1.get("DAART")); // 이동유형
			i_struct1.put("LFART", o_struct1.get("LFART")); // 납품유형코드
			i_struct1.put("RSMOV", o_struct1.get("RSMOV")); // 이동사유
			i_struct1.put("EQUNR", o_struct1.get("EQUNR")); // 설비번호
			i_struct1.put("VSBED", o_struct1.get("VSBED")); // 운송방법코드
			i_struct1.put("PAYFR", o_struct1.get("PAYFR")); // 운임지불코드
			i_struct1.put("LIFNR_CD", o_struct1.get("LIFNR_CD")); // 운전자코드
			i_struct1.put("OSTEP", o_struct1.get("LIFNR_CD")); // 운행단계코드
			i_struct1.put("PLARV", o_struct1.get("PLDPT")); // [도착]도착지구분코드
			i_struct1.put("PLACE2", o_struct1.get("PLACE1")); // [도착]도착지
			i_struct1.put("TPLNR_TO", bt_end_park.getTag().toString()); // [도착]주차장코드(기능위치)
			i_struct1.put("PSTLZ2", o_struct1.get("PSTLZ1")); // [도착]우편번호
			i_struct1.put("ORT012", o_struct1.get("ORT011")); // [도착]우편주소
			i_struct1.put("STRAS2", o_struct1.get("STRAS1")); // [도착]상세주소
			i_struct1.put("PERNR2", o_struct1.get("PERNR1")); // [도착]담당자
			i_struct1.put("TELF12", o_struct1.get("TELF11")); // [도착]담당자연락처

			i_struct1.put("INKM", tv_end_km.getText().toString()); // [도착]KM
			i_struct1.put("INOIL", bt_end_m026.getTag().toString()); // [도착]Oil
																		// 코드
			i_struct1.put("INGDOIL", tv_end_dfm.getText().toString()); // [도착]Oil
																		// 양(DFM)

			String time1 = bt_end_time.getText().toString();
			time1 = time1.replace(".", "").replace(":", "");
			String end_date = time1.substring(0, 8);
			String end_time = time1.substring(9);
			// Log.i("####", "####"+end_date+"/"+end_time);

			i_struct1.put("GUEEN", end_date); // [도착]도착일자
			i_struct1.put("ETIME", end_time); // [도착]도착시간

			i_struct1.put("GDOIL2", cost.get("GDOIL2")); // [운행비용]주유량(L)
			i_struct1.put("OILPRI", cost.get("OILPRI")); // [운행비용]주유금액
			i_struct1.put("WASH", cost.get("WASH")); // [운행비용]세차비
			i_struct1.put("PARK", cost.get("PARK")); // [운행비용]주차비
			i_struct1.put("TOLL", cost.get("TOLL")); // [운행비용]통행료
			i_struct1.put("OTRAMT", cost.get("OTRAMT")); // [운행비용]기타비용
			i_struct1.put("TOT_AMT", cost.get("TOT_AMT")); // [운행비용]차량운행비용

			Object data = bt_end_bigo.getText();
			String bigo = data == null || !data.toString().equals("") ? data
					.toString() : " ";
			i_struct1.put("CUST_DESC2", bigo); // [운행비용]차량운행비용

			Iterator iterator = i_struct1.entrySet().iterator();
			while (iterator.hasNext()) {
				Entry entry = (Entry) iterator.next();
				// System.out.println("####"+entry.getKey() + " : " +
				// entry.getValue());
			}

			cc.setZMO_3140_WR02(i_struct1);
			break;
		case R.id.menu2_end_paperess:
			epc = new EventPopupC(this);

			isInstalled = isPackageInstalled(context,
					"com.ktkumhorental.pdfform");

			if (isInstalled) {
				Intent i = new Intent();
				String info = " :" + login_hm.get("LOGID") + ":"
						+ login_hm.get("PERNR") + ":"
						+ o_struct1.get("PETYPE1") + ":"
						+ o_struct1.get("PETYPE2") + ":"
						+ o_struct1.get("PETYPE3") + ":"
						+ o_struct1.get("VBELN_PE") + ":"
						+ o_struct1.get("PETYPE4") + ":"
						+ login_hm.get("BUKRS") + ":" + login_hm.get("DEPTNM")
						+ ":" + login_hm.get("ENAME");
				// Log.i("####", "####인포" + info);
				epc.show(info);
				i.putExtra("INFO", info);
				i.setAction(Intent.ACTION_MAIN);
				i.addCategory(Intent.CATEGORY_LAUNCHER);
				i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
						| Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
				i.setComponent(new ComponentName("com.ktkumhorental.pdfform",
						"com.ktkumhorental.pdfform.PdfControlWindow"));
				// i.setComponent(new
				// ComponentName("com.example.aaa_intent_app","com.example.aaa_intent_app.MainActivity"));

				startActivity(i);
			} else {
				Intent intent = new Intent(Intent.ACTION_VIEW,
						Uri.parse(DEFINE.PAPER_DOWN_URL));
				startActivity(intent);
			}

			break;
		case R.id.menu2_start_input2:

			// String oil1 = o_struct1.get("GAGTY_TX");
			// if(oil1.equals(" "))
			// {
			// pwm.show("M026", bt_start_oil1.getTag().toString(),
			// bt_start_m026);
			// }
			// else{
			// pwm.show("M026", o_struct1.get("GAGTY"), bt_start_m026);
			// }

			pwm.show("ZDOILTY", bt_start_m026, false);
			break;
		case R.id.menu2_end_input2:
			// oil1 = o_struct1.get("GAGTY_TX");
			// if(oil1.equals(" "))
			// {
			// pwm.show("M026", bt_end_oil1.getTag().toString(), bt_end_m026);
			// }
			// else{
			// pwm.show("M026", o_struct1.get("GAGTY"), bt_end_m026);
			// }
			pwm.show("ZDOILTY", bt_end_m026, false);
			break;
		case R.id.menu2_start_input1:
			vg = ip.getViewGroup();
			final TextView input_start = (TextView) vg
					.findViewById(R.id.inventory_bt_input);
			done = (Button) vg.findViewById(R.id.inventory_bt_done);
			done.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					String num = input_start.getText().toString();
					tv_start_km.setText(currentpoint(num));
					ip.setInput("CLEAR", true);
					ip.dismiss();
				}
			});
			ip.show(tv_start_km);
			break;
		case R.id.menu2_end_input1:
			vg = ip.getViewGroup();
			final TextView input_end = (TextView) vg
					.findViewById(R.id.inventory_bt_input);
			done = (Button) vg.findViewById(R.id.inventory_bt_done);
			done.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					String num = input_end.getText().toString();
					tv_end_km.setText(currentpoint(num));
					ip.setInput("CLEAR", true);
					ip.dismiss();
				}
			});
			ip.show(tv_end_km);
			break;
		case R.id.menu2_start_input3: // 출발 오일3
			if (bt_start_m026.getTag().equals("1")) {
				vg = ip.getViewGroup();
				final TextView dfm_start = (TextView) vg
						.findViewById(R.id.inventory_bt_input);
				done = (Button) vg.findViewById(R.id.inventory_bt_done);
				done.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						String num = dfm_start.getText().toString();
						tv_start_dfm.setText(num);
						ip.setInput("CLEAR", true);
						ip.dismiss();
					}
				});
				ip.show(tv_start_dfm);
			} else if (bt_start_m026.getTag().equals("2")) {
				vg = ipd.getViewGroup();
				final TextView dfm_start = (TextView) vg
						.findViewById(R.id.inventory_bt_input);
				done = (Button) vg.findViewById(R.id.inventory_bt_done);
				done.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						String num = dfm_start.getText().toString();
						tv_start_dfm.setText(num);
						ipd.setInput("CLEAR", true);
						ipd.dismiss();
					}
				});
				ipd.show(tv_start_dfm);
			}

			break;
		case R.id.menu2_end_input3:// 도착 오일3
			if (bt_end_m026.getTag().equals("1")) {
				vg = ip.getViewGroup();
				final TextView dfm_end = (TextView) vg
						.findViewById(R.id.inventory_bt_input);
				done = (Button) vg.findViewById(R.id.inventory_bt_done);
				done.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						String num = dfm_end.getText().toString();
						tv_end_dfm.setText(num);
						ip.setInput("CLEAR", true);
						ip.dismiss();
					}
				});
				ip.show(tv_end_dfm);
			} else if (bt_start_m026.getTag().equals("2")) {
				vg = ipd.getViewGroup();
				final TextView dfm_end = (TextView) vg
						.findViewById(R.id.inventory_bt_input);
				done = (Button) vg.findViewById(R.id.inventory_bt_done);
				done.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						String num = dfm_end.getText().toString();
						tv_end_dfm.setText(num);
						ipd.setInput("CLEAR", true);
						ipd.dismiss();
					}
				});
				ipd.show(tv_end_dfm);
			}

			break;
		case R.id.menu2_end_input5:
			final Cost_Resist_Dialog crd = new Cost_Resist_Dialog(this,
					o_struct1.get("INOIL_TX"));
			Button bt_save = (Button) crd.findViewById(R.id.cost_save_id);

			final TextView tv1_3 = (TextView) crd
					.findViewById(R.id.cost_input3_id);
			final TextView tv2_1 = (TextView) crd
					.findViewById(R.id.cost_input4_id);

			final TextView tv3_1 = (TextView) crd
					.findViewById(R.id.cost_cost1_id);
			final TextView tv3_2 = (TextView) crd
					.findViewById(R.id.cost_cost2_id);
			final TextView tv3_3 = (TextView) crd
					.findViewById(R.id.cost_cost3_id);

			final TextView tv_sum = (TextView) crd
					.findViewById(R.id.cost_cost4_id);
			bt_save.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Object data = tv1_3.getText();
					String gdoil2 = data != null && !data.toString().equals("") ? data
							.toString() : "0";
					gdoil2 = gdoil2.replace(",", "").replace("L", "");

					data = tv2_1.getText();
					String oilpri = data != null && !data.toString().equals("") ? data
							.toString() : "0";
					oilpri = oilpri.replace(",", "").replace("원", "");

					data = tv3_1.getText();
					String wash = data != null && !data.toString().equals("") ? data
							.toString() : "0";
					wash = wash.replace(",", "").replace("원", "");

					data = tv3_2.getText();
					String park = data != null && !data.toString().equals("") ? data
							.toString() : "0";
					park = park.replace(",", "").replace("원", "");

					data = tv3_3.getText();
					String toll = data != null && !data.toString().equals("") ? data
							.toString() : "0";
					toll = toll.replace(",", "").replace("원", "");

					data = tv_sum.getText();
					String opera = data != null && !data.toString().equals("") ? data
							.toString() : "0";
					opera = opera.replace(",", "").replace("원", "");

					data = tv_sum.getText().toString();
					String sum = data != null && !data.toString().equals("") ? data
							.toString() : "0";
					bt_end_cost.setText(sum);

					cost.put("GDOIL2", gdoil2);
					cost.put("OILPRI", oilpri);
					cost.put("WASH", wash);
					cost.put("PARK", park);
					cost.put("TOLL", toll);
					cost.put("OTRAMT", "0");
					cost.put("OPERA", opera);

					crd.dismiss();
				}
			});
			crd.show();
			break;
		case R.id.menu2_driver_input:
			final Driver_Info_Dialog did = new Driver_Info_Dialog(this, o_itab3);
			Button bt_donea = (Button) did
					.findViewById(R.id.driver_info_save_id);
			bt_donea.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					if (did.dida.getChoiced_num() == Integer.MAX_VALUE) {
						EventPopupC epc = new EventPopupC(context);
						epc.setTitle("운전자를 선택해 주세요.");
						epc.show();
						return;
					}

					pp.setMessage("운전자 변경 중입니다.");
					pp.show();
					HashMap<String, String> i_struct1 = new HashMap<String, String>();
					HashMap<String, String> map = o_itab3.get(did.dida
							.getChoiced_num());
					i_struct1.put("VBELN_VL", o_struct1.get("VBELN_VL"));
					i_struct1.put("LIFNR_CD", map.get("LIFNR_CD"));
					i_struct1.put("NAME1_CD", map.get("NAME1_CD"));
					i_struct1.put("TELF13", map.get("TELF13"));

					cc.setZMO_3140_WR03(i_struct1);
					did.dismiss();
				}
			});
			did.show();
			break;

		// myung 20131128 DELETE 사용되지 않는 로직
		// case R.id.menu2_stepby_1:
		// final Stepby_Resist_Dialog srd = new Stepby_Resist_Dialog(this,
		// o_itab2, o_itab3, BUKRS);
		// srd.setOnDismissListener(new OnDismissListener()
		// {
		// @Override
		// public void onDismiss(DialogInterface dialog)
		// {
		// if(srd.SEARCH)
		// {
		// getZMO_3140_RD01();
		// }
		// }
		// });
		// boolean bool = false;
		// if(MODIYN.equals("B"))
		// {
		// bool = true;
		// }
		// else
		// srd.show(o_struct1.get("VBELN_VL"), bool);
		// break;
		case R.id.menu2_start2_2:
			pwm.show(start_park, bt_start_park);
			break;
		case R.id.menu2_end2_2:
			pwm.show(end_park, bt_end_park);
			break;

		case R.id.menu2_start3_1:
			pwm.show("ZDGAUGE", bt_start_oil1, false);
			break;
		case R.id.menu2_end3_1:
			pwm.show("ZDGAUGE", bt_end_oil1, false);
			break;

		case R.id.menu2_start_input4:
			cp = new CalendarPopup(context);
			cp.show(bt_start_time);
			break;
		case R.id.menu2_end_input4:
			cp = new CalendarPopup(context);
			cp.show(bt_end_time);
			break;
		}
	}

	public boolean isPackageInstalled(Context ctx, String pkgName) {
		try {
			ctx.getPackageManager().getPackageInfo(pkgName,
					PackageManager.GET_ACTIVITIES);
		} catch (NameNotFoundException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		pp.dismiss();
		// myung 20131213 ADD 이동 리스트 상세조회 종료 후 리스트 재조회
		// if(m3rd!=null){
		// m3rd.dismiss();
		// goSearch();
		// return;
		// }
		super.onBackPressed();
	}

	private String BUKRS;

	public void goSearch() {
		SEARCH = true;
		o_itab1 = new ArrayList<HashMap<String, String>>();
		m2a = new Menu3_Adapter(this, R.layout.menu2_row, o_itab1);
		lv_menu2.setAdapter(m2a);
		iv_nodata.setVisibility(View.VISIBLE);

		pp.setMessage("조회 중 입니다.");
		pp.show();
		String top1 = bt_top1.getTag().toString();
		String top2 = bt_top2.getTag().toString();
		// 20131202 UPDATE 차량이동 리스트 조회 시 차량번호 파라미터 넘기지 않고 있음.
		// String top3 = bt_top3.getTag().toString();
		String top3 = bt_top3.getText().toString();
		if (top3.equals("전체"))
			top3 = "";
		String period = bt_date.getTag().toString();
		String date1 = bt_date1.getText().toString().replace(".", "");
		String date2 = bt_date2.getText().toString().replace(".", "");

		int rb_id = rg_menu3.getCheckedRadioButtonId();
		RadioButton rb = (RadioButton) findViewById(rb_id);
		String bukrs = rb.getTag().toString();
		if (bukrs.equals("3000")) {
			// Log.i("####", "####" + "3000");
			cc.getZMO_3140_RD01(top1, top2, top3, period, date1, date2);
		} else if (bukrs.equals("3100")) {
			// Log.i("####", "####" + "3100");
			cc.getZMO_3200_RD01(top1, top2, top3, period, date1, date2);
		}
	}

	public HashMap<String, String> cost = new HashMap<String, String>();
	public ArrayList<MULTY> m026_arr;

	private ArrayList<MULTY> init(String what, String what2) {
		m026_arr = new ArrayList<MULTY>();
		String path = context.getExternalCacheDir() + "/DATABASE/"
				+ DEFINE.SQLLITE_DB_NAME;
		SQLiteDatabase sqlite = SQLiteDatabase.openDatabase(path, null,
				SQLiteDatabase.OPEN_READONLY);
		Cursor cursor = sqlite.rawQuery("select ZCODEV, ZCODEVT from "
				+ "O_ITAB1" + " where ZCODEH = '" + what + "' and ZCODEH2 = '"
				+ what2 + "'", null);
		MULTY multy;
		while (cursor.moveToNext()) {
			int calnum1 = cursor.getColumnIndex("ZCODEV");
			int calnum2 = cursor.getColumnIndex("ZCODEVT");
			String zcodev = cursor.getString(calnum1);
			String zcodevt = cursor.getString(calnum2);

			multy = new MULTY();
			multy.ZCODEV = zcodev;
			multy.ZCODEVT = zcodevt;
			m026_arr.add(multy);
		}
		cursor.close();
		// 2017.07.05. hjt. db close 는 app 종료시 알아서 된다
		// 이것때문에 throwIfClosedLocked Exception 발생한다
//		sqlite.close();
		return m026_arr;
	}

	public void onHandle(View v) {
		callMenu3RegistDlg();

	}

	public void callMenu3RegistDlg() {
		if (m2a == null || m2a.getCheckPosition() == -1) {
			EventPopupC epc = new EventPopupC(context);
			epc.show("차량을 선택해 주세요.");
			return;
		}

		// 팝업띄우기
		String top1 = bt_top1.getTag().toString();
		String top2 = bt_top2.getTag().toString();
		String top3 = bt_top3.getTag().toString();

		String period = bt_date.getTag().toString();
		String date1 = bt_date1.getText().toString().replace(".", "");
		String date2 = bt_date2.getText().toString().replace(".", "");

		String vbeln_vl = o_itab1.get(m2a.getCheckPosition()).get("VBELN_VL");
		BUKRS = o_itab1.get(m2a.getCheckPosition()).get("BUKRS");

		// Log.i("####",
		// "#### 상세조회" + top1 + "/" + top2 + "/" + top3 + "/" + period
		// + "/" + date1 + "/" + date2 + "/"
		// + o_itab1.get(m2a.getCheckPosition()).get("VBELN_VL"));

		m3rd = new Menu3_Resist_Dialog(this);
		m3rd.setOnDismissListener(new OnDismissListener() {
			
			@Override
			public void onDismiss(DialogInterface dialog) {
				// TODO Auto-generated method stub
				goSearch();
			}
		});
		LogUtil.d("rental", "top1 = " + top1 + " | top2 = " + top2 + " | top3 = " + top3 + " | period = " + period + " | date1 = " + date1 + " | date2 = " + date2 + " vbeln_vl = " + vbeln_vl);
		m3rd.show(top1, top2, top3, period, date1, date2, vbeln_vl, BUKRS);
		Button bt_dismiss = (Button) m3rd
				.findViewById(R.id.menu3_resist_close_id);
		bt_dismiss.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
//				goSearch();
				m3rd.dismiss();
			}
		});

		// if(SLIDE_DOWN) //열기
		// {
		// ObjectAnimator mover = ObjectAnimator.ofFloat(ll_slide,
		// "translationY", 707, 0);
		// mover.start();
		//
		// if(SEARCHED!=m2a.getCheckPosition())
		// {
		// onClearField();
		// getZMO_3140_RD01();
		// }
		// }
		//
		// else{ //닫기
		// ObjectAnimator mover = ObjectAnimator.ofFloat(ll_slide,
		// "translationY", 0, 707);
		// mover.start();
		// if(REFRESH)
		// {
		// REFRESH = false;
		// goSearch();
		// }
		// }
		//
		// SLIDE_DOWN = !SLIDE_DOWN;
		//
		// SEARCHED = m2a.getCheckPosition();
	}

	public void onClearField() {
		for (int i = 0; i < tv_driver.length; i++) {
			tv_driver[i].setText("");
		}
		for (int i = 0; i < tv_start.length; i++) {
			tv_start[i].setText("");
		}
		for (int i = 0; i < tv_end.length; i++) {
			tv_end[i].setText("");
		}

		bt_driver.setText("");
		bt_driver.setTag("");

		tv_start_km.setText("");
		bt_start_m026.setText("");
		bt_start_m026.setTag("");
		tv_start_dfm.setText("");
		bt_start_time.setText("");
		bt_start_time.setTag("");
		bt_start_park.setText("");
		bt_start_park.setTag("");
		bt_start_oil1.setText("");
		bt_start_oil1.setTag("");

		tv_end_km.setText("");
		bt_end_m026.setText("");
		bt_end_m026.setTag("");
		tv_end_dfm.setText("");
		bt_end_time.setText("");
		bt_end_time.setTag("");
		bt_end_cost.setText("");
		bt_end_cost.setTag("");
		bt_end_park.setText("");
		bt_end_park.setTag("");
		bt_end_oil1.setText("");
		bt_end_oil1.setTag("");
	}

	public HashMap<String, String> login_hm;

	private HashMap<String, String> initLogin() {
		login_hm = new HashMap<String, String>();
		String path = context.getExternalCacheDir() + "/DATABASE/"
				+ DEFINE.SQLLITE_DB_NAME;
		SQLiteDatabase sqlite = SQLiteDatabase.openDatabase(path, null,
				SQLiteDatabase.OPEN_READONLY);
		Cursor cursor = sqlite.rawQuery(
				"select * from " + "LOGIN_STRUCT_TABLE", null);
		while (cursor.moveToNext()) {
			for (int i = 0; i < cursor.getColumnCount(); i++) {
				login_hm.put(cursor.getColumnName(i), cursor.getString(i));
			}
		}
		cursor.close();
//		sqlite.close();
		return login_hm;
	}

	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		super.onActivityResult(requestCode, resultCode, intent);
		switch (requestCode) {
		case 11:
			// Log.i("####", "#### 결과물 : ");
			break;
		}
	}

}
