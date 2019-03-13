package com.ktrental.dialog;

import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.text.Layout;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.ktrental.R;
import com.ktrental.beans.LANGUAGE;
import com.ktrental.beans.MULTY;
import com.ktrental.cm.connect.ConnectController2;
import com.ktrental.cm.connect.Connector.ConnectInterface;
import com.ktrental.common.DEFINE;
import com.ktrental.model.TableModel;
import com.ktrental.popup.CalendarPopup;
import com.ktrental.popup.CalendarPopup.YyyyMmDdHhMm;
import com.ktrental.popup.EventPopupC;
import com.ktrental.popup.InventoryPopup_Dot;
import com.ktrental.popup.NumberInputPopup;
import com.ktrental.popup.Popup_Window_Multy;
import com.ktrental.popup.Popup_Window_Text_Balloon;
import com.ktrental.util.SharedPreferencesUtil;
import com.ktrental.util.kog;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.concurrent.TimeUnit;

public class Menu3_Resist_Dialog extends DialogC implements ConnectInterface,
		View.OnClickListener {
	private Window w;
	private WindowManager.LayoutParams lp;
	private Context context; 
	private Button bt_close;
	private ConnectController2 cc;

	private ArrayList<HashMap<String, String>> o_itab1;
	private ArrayList<HashMap<String, String>> o_itab2;
	private ArrayList<HashMap<String, String>> o_itab3;
	private ArrayList<HashMap<String, String>> o_itab4;
	private HashMap<String, String> o_struct1;

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
	private TextView bt_end_bigo;
	private Button bt_stepby;
	
	//Jonathan 추가
	private Button bt_language_start;
	private Button bt_language_end;
	private Button menu2_stepby_2;

	private String MODIYN = "N";
	private String PAPLYN = "N";
	private String Type_204 = "204";
	private String Type_203 = "203";

	public HashMap<String, String> cost = new HashMap<String, String>();

	SharedPreferencesUtil shareU = SharedPreferencesUtil.getInstance();

	private Popup_Window_Multy pwm;
	private NumberInputPopup nip;
	private InventoryPopup_Dot ipd;
	private InventoryPopup_Dot ipd1;
	private CalendarPopup cp;
//	private Popup_Window_Text_Balloon pwtb;

	private String ATOB_DISTANCE;
	private String ATOB_TIME;
	private ProgressDialog pd;

	public Menu3_Resist_Dialog(Context context) {
		super(context);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.menu3_resist_dialog);
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
		pwm = new Popup_Window_Multy(context);
		nip = new NumberInputPopup(context, NumberInputPopup.TYPE_MONEY);
		ipd = new InventoryPopup_Dot(context);
		ipd1 = new InventoryPopup_Dot(context, InventoryPopup_Dot.TYPE_NORMAL_NUMBER_UNDER_100);
//		pwtb = new Popup_Window_Text_Balloon(context);
		
//		showProgress("조회 중 입니다.");
		pd = new ProgressDialog(context);
		pd.setMessage("조회 중 입니다.");
		pd.setCancelable(false);
		
		bt_close = (Button) findViewById(R.id.menu3_resist_close_id);
		bt_close.setOnClickListener(this);

		tv_driver[0] = (TextView) findViewById(R.id.menu2_driver1_1);
		tv_driver[1] = (TextView) findViewById(R.id.menu2_driver1_2);
		tv_driver[2] = (TextView) findViewById(R.id.menu2_driver2_1);
		tv_driver[3] = (TextView) findViewById(R.id.menu2_driver2_2);
		tv_start[0] = (TextView) findViewById(R.id.menu2_start1_1);
		tv_start[1] = (TextView) findViewById(R.id.menu2_start1_2);
		tv_start[2] = (TextView) findViewById(R.id.menu2_start2_1);
		tv_start[3] = (TextView) findViewById(R.id.menu2_start4_1);
		tv_start[4] = (TextView) findViewById(R.id.menu2_start4_2);
		tv_end[0] = (TextView) findViewById(R.id.menu2_end1_1);
		tv_end[1] = (TextView) findViewById(R.id.menu2_end1_2);
		tv_end[2] = (TextView) findViewById(R.id.menu2_end2_1);
		tv_end[3] = (TextView) findViewById(R.id.menu2_end4_1);
		tv_end[4] = (TextView) findViewById(R.id.menu2_end4_2);
		tv_end[5] = (TextView) findViewById(R.id.menu2_end4_3);
		tv_end[6] = (TextView) findViewById(R.id.menu2_end5_1);
		bt_driver = (Button) findViewById(R.id.menu2_driver_input);
		bt_driver.setOnClickListener(this);
		bt_driver.setEnabled(false);
		tv_start_km = (TextView) findViewById(R.id.menu2_start_input1);
		tv_start_km.setOnClickListener(this);
		bt_start_m026 = (Button) findViewById(R.id.menu2_start_input2);
		bt_start_m026.setOnClickListener(this);
		tv_start_dfm = (TextView) findViewById(R.id.menu2_start_input3);
		tv_start_dfm.setOnClickListener(this);
		bt_start_time = (Button) findViewById(R.id.menu2_start_input4);
		bt_start_time.setOnClickListener(this);
		bt_start_park = (Button) findViewById(R.id.menu2_start2_2);
		bt_start_park.setOnClickListener(this);
		bt_start_oil1 = (Button) findViewById(R.id.menu2_start3_1);
		bt_start_oil1.setOnClickListener(this);
		tv_end_km = (TextView) findViewById(R.id.menu2_end_input1);
		tv_end_km.setOnClickListener(this);
		bt_end_m026 = (Button) findViewById(R.id.menu2_end_input2);
		bt_end_m026.setOnClickListener(this);
		tv_end_dfm = (TextView) findViewById(R.id.menu2_end_input3);
		tv_end_dfm.setOnClickListener(this);
		bt_end_time = (Button) findViewById(R.id.menu2_end_input4);
		bt_end_time.setOnClickListener(this);
		bt_end_cost = (Button) findViewById(R.id.menu2_end_input5);
		bt_end_cost.setOnClickListener(this);
		bt_end_park = (Button) findViewById(R.id.menu2_end2_2);
		bt_end_park.setOnClickListener(this);
		bt_end_oil1 = (Button) findViewById(R.id.menu2_end3_1);
		bt_end_oil1.setOnClickListener(this);
		bt_start_resist = (Button) findViewById(R.id.menu2_start_resist);
		bt_start_paperess = (Button) findViewById(R.id.menu2_start_paperess);
		bt_end_resist = (Button) findViewById(R.id.menu2_end_resist);
		bt_end_paperess = (Button) findViewById(R.id.menu2_end_paperess);
		bt_start_resist.setOnClickListener(this);
		bt_start_paperess.setOnClickListener(this);
		bt_end_resist.setOnClickListener(this);
		bt_end_paperess.setOnClickListener(this);
		bt_stepby = (Button) findViewById(R.id.menu2_stepby_1);
		bt_stepby.setOnClickListener(this);
		
		
		
		bt_language_start = (Button)findViewById(R.id.bt_language_start);
		bt_language_start.setOnClickListener(this);
		
		bt_language_end = (Button)findViewById(R.id.bt_language_end);
		bt_language_end.setOnClickListener(this);
		
		bt_start_bigo = (TextView) findViewById(R.id.menu2_start_bigo);
		bt_end_bigo = (TextView) findViewById(R.id.menu2_end_bigo);

		
		menu2_stepby_2 = (Button)findViewById(R.id.menu2_stepby_2);
		menu2_stepby_2.setOnClickListener(this);
		
		
		initLogin();
		cost.put("GDOIL2", "0");
		cost.put("OILPRI", "0");
		cost.put("WASH", "0");
		cost.put("PARK", "0");
		cost.put("TOLL", "0");
		cost.put("OTRAMT", "0");
		cost.put("TOT_AMT", "0");

//		tv_start[2].setSelected(true);
//		tv_end[2].setSelected(true);
		//myung 20131211 ADD 주소창에서 1라인을 넘을 경우 … 풍선 추가하여 클릭시 주소 확인
		tv_start[2].setOnClickListener(this);
		tv_end[2].setOnClickListener(this);
		//myung 20131220 ADD 주소창에서 1라인을 넘을 경우 … 풍선 추가하여 클릭시 주소 확인
		bt_start_bigo.setOnClickListener(this);
		bt_end_bigo.setOnClickListener(this);
	}

    private Runnable mHideRunnable = new Runnable() {
        @Override
        public void run() {
        	if(pp!=null)
        	pp.hide();
        }
    };
    
	@Override
	public void onAttachedToWindow() {
		super.onAttachedToWindow();
//		pp.setMessage("상세 조회중 입니다.");
//		pp.show();

		// myung 2013/11/15
		if (bukrs == null) {
			bukrs = login_hm.get("BUKRS");
		}
		if (bukrs.equals("3000")) {
			cc.getZMO_3140_RD01(top1, top2, top3, period, date1, date2,
					vbeln_vl);
		} else if (bukrs.equals("3100")) {
			cc.getZMO_3200_RD01(top1, top2, top3, period, date1, date2,
					vbeln_vl);
		}

		init("ZDOILTY");
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

	@Override
	public void connectResponse(String FuntionName, String resultText,
			String MTYPE, int resulCode, TableModel tableModel) {
		// Log.i("#", "####" + FuntionName + "/" + resultText + "/" + MTYPE +
		// "/"
		// + resulCode);
		hideProgress();
		if (MTYPE == null || !MTYPE.equals("S")) {
			
			cc.duplicateLogin(mContext);
			
			final EventPopupC epc = new EventPopupC(context);
			epc.show(resultText);
			//myung 20131224 ADD 
			Button btn_done = (Button)epc.findViewById(R.id.btn_ok);
			btn_done.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					
					dismiss(); 
					epc.dismiss();
					
				}
			});
			return;
		}
		if (FuntionName.equals("ZMO_3140_RD01")
				|| FuntionName.equals("ZMO_3200_RD01")) {
			o_struct1 = tableModel.getStruct("O_STRUCT1");
			o_itab2 = tableModel.getTableArray("O_ITAB2");
			o_itab3 = tableModel.getTableArray("O_ITAB3");
			o_itab4 = tableModel.getTableArray("O_ITAB4");
			
			
			//Jonathan o_struct1에 뭐가 있는지 궁금해서 key value 있는거 확인 
			Set<String> set1 = o_struct1.keySet();
			Iterator<String> it1 = set1.iterator();
			String key;
			while (it1.hasNext()) {
				key = it1.next();
				kog.e("Jonathan", "ZMO_3140_RD01 key ===" + key + "    value  === "
						+ o_struct1.get(key));
			}

			init("M026", o_struct1.get("GAGTY"));
			setContents();

			initPark();
			
			cc.duplicateLogin(mContext);
			
		}

		if (FuntionName.equals("ZMO_3140_RD01")
				|| FuntionName.equals("ZMO_3140_WR01")
				|| FuntionName.equals("ZMO_3200_WR01")
				|| FuntionName.equals("ZMO_3140_WR02")
				|| FuntionName.equals("ZMO_3200_WR02")) {
			if (FuntionName.equals("ZMO_3140_WR01")
					|| FuntionName.equals("ZMO_3200_WR01")) {
				EventPopupC epc = new EventPopupC(context);
				epc.show("출발등록이 완료 되었습니다.");
				o_struct1 = tableModel.getStruct("O_STRUCT1");

				init("M026", o_struct1.get("GAGTY"));
				setContents();

				initPark();
			} else if (FuntionName.equals("ZMO_3140_WR02")
					|| FuntionName.equals("ZMO_3200_WR02")) {
				EventPopupC epc = new EventPopupC(context);
				epc.show("도착등록이 완료 되었습니다.");
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
			
			cc.duplicateLogin(mContext);
			
		} else if (FuntionName.equals("ZMO_3140_WR03")
				|| FuntionName.equals("ZMO_3140_WR03")) {
			pp.setMessage("상세 조회중 입니다.");
			pp.show();

			if (bukrs.equals("3000")) {
				cc.getZMO_3140_RD01(top1, top2, top3, period, date1, date2,
						vbeln_vl);
			} else if (bukrs.equals("3100")) {
				cc.getZMO_3200_RD01(top1, top2, top3, period, date1, date2,
						vbeln_vl);
			}
		} else if (FuntionName.equals("ZMO_3140_WR04")) {
			cc.duplicateLogin(mContext);
			
		}
	}

	ArrayList<MULTY> start_park;
	ArrayList<MULTY> end_park;

	private void initPark() {
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

	// private String DATE;
	// private String TIME;

	private void setContents() {
		tv_driver[0].setText(o_struct1.get("VSBED_TX")); // 운송방법
		tv_driver[1].setText(o_struct1.get("TELF13")); // 운전자전화번호
		tv_driver[2].setText(o_struct1.get("INVNR"));
		tv_driver[3].setText(o_struct1.get("MAKTX"));

		tv_start[0].setText(o_struct1.get("PLDPT_TX"));
		tv_start[1].setText(o_struct1.get("PLACE1"));
		// myung 20131118 ZONEA1_TX -> ADDR1 로 변경.
		// tv_start[2].setText(o_struct1.get("ZONEA1_TX"));
		tv_start[2].setText(o_struct1.get("ADDR1"));

		bt_start_park.setText(o_struct1.get("PLTXT_FR"));
		bt_start_park.setTag(o_struct1.get("TPLNR_FR"));

		// tv_start[3].setText(o_struct1.get("GAGTY_TX"));

		bt_start_oil1.setText(o_struct1.get("GAGTY_TX"));
		tv_start[3].setText(o_struct1.get("PERNR1"));
		tv_start[4].setText(o_struct1.get("TELF11"));

		tv_end[0].setText(o_struct1.get("PLARV_TX"));
		tv_end[1].setText(o_struct1.get("PLACE2"));
		// myung 20131118 ZONEA2_TX -> ADDR2 로 변경.
		// tv_end[2].setText(o_struct1.get("ZONEA2_TX"));
		tv_end[2].setText(o_struct1.get("ADDR2"));
		bt_end_park.setText(o_struct1.get("PLTXT_TO"));
		bt_end_park.setTag(o_struct1.get("TPLNR_TO"));
		// bt_end_park.setTag(o_struct1.get("ADDR2"));
		// tv_end[3].setText(o_struct1.get("GAGTY_TX"));
		bt_end_oil1.setText(o_struct1.get("GAGTY_TX"));

		String s_outkm = o_struct1.get("OUTKM").trim();
		String s_inkm = o_struct1.get("INKM").trim();
		int i_outkm = 0;
		int i_inkm = 0;
		try {
			i_outkm = Integer.parseInt(s_outkm);
			i_inkm = Integer.parseInt(s_inkm);
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
		int i_result = i_inkm - i_outkm;
		// Log.i("####", "####인아웃" + i_result + "/");

		ATOB_DISTANCE = i_result + " Km";
		ATOB_TIME = "0시간";

		tv_end[3].setText(ATOB_DISTANCE + "/" + ATOB_TIME);
		tv_end[4].setText(o_struct1.get("PERNR2"));
		tv_end[5].setText(o_struct1.get("TELF12"));
		tv_end[6].setText(o_struct1.get("WADTM"));

		bt_driver.setText(o_struct1.get("NAME1_CD"));
		tv_start_km.setText(currentpoint(o_struct1.get("OUTKM")) + " Km");

		String fuel = "";
		String fuel_code = "";
		for (int i = 0; i < ZDOILTY_arr.size(); i++) {
			if (ZDOILTY_arr.get(i).ZCODEV.equals(o_struct1.get("OUTOIL"))) {
				fuel_code = ZDOILTY_arr.get(i).ZCODEV;
				fuel = ZDOILTY_arr.get(i).ZCODEVT;
				break;
			}
		}

		// Log.i("####", "####휴엘 " + o_struct1.get("OUTOIL") + " /");

		bt_start_m026.setText(fuel);
		bt_start_m026.setTag(fuel_code);
		tv_start_dfm.setText(o_struct1.get("OUTGDOIL"));

		// myung 20131118
//		 DATE = date.substring(0, 10);
		// TIME = date.substring(10);
		
		
		String DATE = o_struct1.get("GUEBG");
		String TIME = o_struct1.get("STIME");
		
		// myung 20131223 UPDATE 출발일시가 없는 경우 현재 시간을 세팅.
		Date date;
		String strDate = "";
		if(DATE.equals(" ") || DATE.equals("")){
			date = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd[E] HH:mm");
			strDate = sdf.format(date).toString();
			bt_start_time.setText(strDate);
		}
		else{
			date = stringTODate(DATE, "yyyyMMdd");
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd[E]");
			strDate = sdf.format(date);
			bt_start_time.setText(strDate +" "+ TIME.substring(0, 2)+":"+ TIME.substring(2, 4));
		}
		
		//14.10.16 Jonathan 도착 km 안나오게 하기.
//		tv_end_km.setText(currentpoint(o_struct1.get("INKM")) + " Km");
		for (int i = 0; i < ZDOILTY_arr.size(); i++) {
			if (ZDOILTY_arr.get(i).ZCODEV.equals(o_struct1.get("INOIL"))) {
				fuel_code = ZDOILTY_arr.get(i).ZCODEV;
				fuel = ZDOILTY_arr.get(i).ZCODEVT;
				break;
			}
		}

		bt_end_m026.setText(fuel);
		bt_end_m026.setTag(fuel_code);
		tv_end_dfm.setText(o_struct1.get("INGDOIL"));
		bt_end_time.setText(o_struct1.get("ETTM"));
		bt_end_cost.setText(o_struct1.get("OILPRI"));

		 kog.e("Jonathan","MODIYN" +  o_struct1.get("MODIYN"));

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
		
		
		kog.e("Jonathan", "ostruct1.CTLAN :: " + o_struct1.get("CTLAN"));
//		o_struct1.put("CTLAN", "01");
		
		if(o_struct1.get("CTLAN") == null || "".equals(o_struct1.get("CTLAN")) || " ".equals(o_struct1.get("CTLAN")))
		{
			//국문
			if(bt_start_paperess.isEnabled())
			{
				bt_language_start.setEnabled(true);
			}
			else
			{
				bt_language_start.setEnabled(false);
			}
			
			
			if(bt_end_paperess.isEnabled())
			{
				bt_language_end.setEnabled(true);
			}
			else
			{
				bt_language_end.setEnabled(false);
			}
			
			
			
		}
		else
		{

			// bt_language_start를 막아놔야한다.
			bt_language_start.setEnabled(false);
			bt_language_end.setEnabled(false);

			if ("01".equals(o_struct1.get("CTLAN"))) 
			{
				//국문 
				bt_language_start.setText("국문");
				bt_language_end.setText("국문");
			}
			else if("13".equals(o_struct1.get("CTLAN")))
			{
				// 영문
				bt_language_start.setText("영문");
				bt_language_end.setText("영문");
			}
			else if("14".equals(o_struct1.get("CTLAN")))
			{
				// 중문
				bt_language_start.setText("중문");
				bt_language_end.setText("중문");
			}
//			else if("98".equals(o_struct1.get("CTLAN")))
//			{
//				// 위임장이 임시저장됨.
//				bt_language_start.setText("위임장");
//				bt_language_end.setText("위임장");
//			}
			else
			{
				//국문
				bt_language_start.setText("국문");
				bt_language_end.setText("국문");
				
			}

		}
		
		
		
		if(Type_204.equals(o_struct1.get("AUGRU")))
		{
			kog.e("Jonathan", "사고임.");
			menu2_stepby_2.setVisibility(View.VISIBLE);
			
			//Jonathan 150319
			kog.e("Jonathan", "ostruct1.DELEG :: " + o_struct1.get("DELEG"));
//			o_struct1.put("DELEG", "X");
			if("X".equals(o_struct1.get("DELEG")))
			{
				//위임장 있음 
				menu2_stepby_2.setEnabled(false);
			}
			else
			{
				//위임장 없음
				menu2_stepby_2.setEnabled(true);
			}
		}
		else
		{
			menu2_stepby_2.setEnabled(false);
		}
		
		

		bt_start_bigo.setText(o_struct1.get("CUST_DESC1"));
		bt_end_bigo.setText(o_struct1.get("CUST_DESC1"));
		
		//myung 20131211 ADD 주소창에서 1라인을 넘을 경우 … 풍선 추가하여 클릭시 주소 확인
		isEnableTextBalloon(tv_start[2]);
		isEnableTextBalloon(tv_end[2]);
		
		//myung 20131220 ADD 주소창에서 1라인을 넘을 경우 … 풍선 추가하여 클릭시 주소 확인
		isEnableTextBalloon(bt_start_bigo);
		isEnableTextBalloon(bt_end_bigo);
	}
	
	
	// myung 20131220 ADD 요일은 항상 금요일인 문제
	public Date stringTODate(String dateText, String pattern) {
		Date date = null;
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(pattern);
			date = sdf.parse(dateText);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return date;
	}

	// myung 20131220 ADD 요일은 항상 금요일인 문제
	public String getDayOfWeek(Date date, boolean korea) {
		String[][] week = { { "일", "Sun" }, { "월", "Mon" }, { "화", "Tue" },
				{ "수", "Wen" }, { "목", "Thu" }, { "금", "Fri" }, { "토", "Sat" } };
		Calendar cal1 = Calendar.getInstance();
		cal1.setTime(date);
		if (korea) {
			return week[cal1.get(Calendar.DAY_OF_WEEK) - 1][0];
		} else {
			return week[cal1.get(Calendar.DAY_OF_WEEK) - 1][1];
		}
	}

	private void setYN_A() {
		//myung 20131217 UPDATE 운전자 필드 Disable 처리 .
		bt_driver.setEnabled(false);
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
		bt_start_park.setEnabled(true);
		bt_start_resist.setEnabled(true);
		bt_stepby.setEnabled(false);

		tv_end_km.setEnabled(false);

		bt_end_oil1.setEnabled(false);
		bt_end_oil1.setEnabled(false);
		bt_end_m026.setEnabled(false);
		tv_end_dfm.setEnabled(false);
		bt_end_time.setEnabled(false);

		bt_end_park.setEnabled(false);

		bt_end_cost.setEnabled(false);
		bt_end_bigo.setEnabled(false);

		bt_end_resist.setEnabled(false);
	}

	private void setYN_B() {
		
		bt_driver.setEnabled(false);
		tv_start_km.setEnabled(false);
		String oil1 = o_struct1.get("GAGTY_TX");
		bt_start_oil1.setEnabled(false);
		bt_start_m026.setEnabled(false);
		tv_start_dfm.setEnabled(false);
		bt_start_time.setEnabled(false);
		bt_start_resist.setEnabled(false);
		// myung 20131128 변경 출발등록 후 MODIYN 값이 B 일 경우 주출발정보의 주차장은 비활성 처리
		bt_start_park.setEnabled(false);
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

		bt_end_cost.setEnabled(true);
		bt_end_bigo.setEnabled(true);
		
		bt_end_resist.setEnabled(true);
		
		//사고일때만.. x 값 확인...나머지는 x  값이 있던 없던 그냥 패스
//		if(Type_204.equals(o_struct1.get("AUGRU")))
//		{
//			if("X".equals(o_struct1.get("DELEG")))
//			{
//				//위임장 있음
//				bt_end_resist.setEnabled(true);
//			}
//			else
//			{
//				//위임장 없음,  도착등록 버튼 비활성
//				bt_end_resist.setEnabled(false);
//			}
//		}
//		else
//		{
//			//사고 아닐때 그냥 패스
//			bt_end_resist.setEnabled(true);
//			
//		}
		
		
//		bt_driver.setEnabled(false);
//		tv_start_km.setEnabled(false);
//		String oil1 = o_struct1.get("GAGTY_TX");
//		bt_start_oil1.setEnabled(false);
//		bt_start_m026.setEnabled(false);
//		tv_start_dfm.setEnabled(false);
//		bt_start_time.setEnabled(false);
//		bt_start_resist.setEnabled(false);
//		// myung 20131128 변경 출발등록 후 MODIYN 값이 B 일 경우 주출발정보의 주차장은 비활성 처리
//		bt_start_park.setEnabled(false);
//		bt_stepby.setEnabled(true);
//
//		tv_end_km.setEnabled(true);
//		if (oil1.equals(" ")) {
//			bt_end_oil1.setEnabled(true);
//		} else {
//			bt_end_oil1.setEnabled(false);
//		}
//		bt_end_oil1.setEnabled(true);
//		bt_end_m026.setEnabled(true);
//		tv_end_dfm.setEnabled(true);
//		bt_end_time.setEnabled(true);
//
//		bt_end_park.setEnabled(true);
//
//		bt_end_cost.setEnabled(true);
//		bt_end_bigo.setEnabled(true);
//
//		bt_end_resist.setEnabled(true);
//		
		
		
		
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
		bt_start_park.setEnabled(false);
		bt_stepby.setEnabled(true);

		tv_end_km.setEnabled(false);
		bt_end_oil1.setEnabled(false);
		bt_end_m026.setEnabled(false);
		tv_end_dfm.setEnabled(false);
		bt_end_time.setEnabled(false);

		bt_end_park.setEnabled(false);
		bt_end_cost.setEnabled(false);
		bt_end_bigo.setEnabled(false);

		bt_end_resist.setEnabled(false);
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
//		sqlite.close();
		return m026_arr;
	}

	public ArrayList<MULTY> ZDOILTY_arr;

	private ArrayList<MULTY> init(String what) {
		ZDOILTY_arr = new ArrayList<MULTY>();
		String path = context.getExternalCacheDir() + "/DATABASE/"
				+ DEFINE.SQLLITE_DB_NAME;
		SQLiteDatabase sqlite = SQLiteDatabase.openDatabase(path, null,
				SQLiteDatabase.OPEN_READONLY);
		Cursor cursor = sqlite.rawQuery("select ZCODEV, ZCODEVT from "
				+ "O_ITAB1" + " where ZCODEH = '" + what + "'", null);
		MULTY multy;
		while (cursor.moveToNext()) {
			int calnum1 = cursor.getColumnIndex("ZCODEV");
			int calnum2 = cursor.getColumnIndex("ZCODEVT");
			String zcodev = cursor.getString(calnum1);
			String zcodevt = cursor.getString(calnum2);

			multy = new MULTY();
			multy.ZCODEV = zcodev;
			multy.ZCODEVT = zcodevt;
			ZDOILTY_arr.add(multy);
		}
		cursor.close();
//		sqlite.close();
		return ZDOILTY_arr;
	}

	@Override
	public void reDownloadDB(String newVersion) {
	}

	
	ArrayList<LANGUAGE> language_list;
	
	@Override
	public void onClick(View v) {
		ArrayList<MULTY> list = new ArrayList<MULTY>();
		MULTY multy;
		String date;
		EventPopupC epc;
		HashMap<String, String> i_struct1;
		ViewGroup vg;
		Button done;

		Calendar cal = Calendar.getInstance();
		String year = String.format("%04d", cal.get(Calendar.YEAR));
		String month = String.format("%02d", cal.get(Calendar.MONTH) + 1);
		String day = String.format("%02d", cal.get(Calendar.DAY_OF_MONTH));
		String hour = String.format("%02d", cal.get(Calendar.HOUR_OF_DAY));
		String minute = String.format("%02d", cal.get(Calendar.MINUTE));

		switch (v.getId()) {
		// case R.id.menu3_resist_close_id: // 닫기
		//
		// dismiss();
		// break;

		case R.id.menu2_start_resist:

			// myung 20131128 출발/OIL 값이 없을 경우 메시지 출력 후 리턴
			String strOilValue = checkOILValue();
			if (Double.valueOf(strOilValue) == 0) {
				EventPopupC epc_start_oil = new EventPopupC(context);
				epc_start_oil.show("출발/OIL을 입력해 주세요.");
				return;
			}

			showProgress("출발등록 중 입니다.");
//			pp.setMessage("출발등록 중 입니다.");
//			pp.show();

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
			String outkm = tv_start_km.getText().toString();
			i_struct1.put("OUTKM", onlyNum(outkm)); // [출발]KM
			i_struct1.put("OUTOIL", bt_start_m026.getTag().toString()); // [출발]Oil
																		// 코드
			String dfm = tv_start_dfm.getText().toString();
			i_struct1.put("OUTGDOIL", onlyNum(dfm)); // [출발]Oil 양(DFM)

			String time = bt_start_time.getText().toString();
			time = time.replace(".", "").replace(":", "");
			String start_date = time.substring(0, 8);
			// myung 20131118
			String start_time = time.substring(9 + 3);

			// Log.e(" [출발]출발일자", start_date);
			// Log.e("[출발]출발시간", start_time);
			i_struct1.put("GUEBG", start_date); // [출발]출발일자
			i_struct1.put("STIME", start_time); // [출발]출발시간

			if (bukrs.equals("3000")) {
				cc.setZMO_3140_WR01(i_struct1);
			} else if (bukrs.equals("3100")) {
				cc.setZMO_3200_WR01(i_struct1);
			}

			break;
			
		case R.id.bt_language_start:
			pwm.show_language( bt_language_start);
			break;
			
			
		case R.id.bt_language_end:
			pwm.show_language( bt_language_end);
			break;
			
		case R.id.menu2_start_paperess:
			epc = new EventPopupC(context);

			String language = "01";
			kog.e("Jonathan", "페이퍼리스 누름");
			boolean isInstalled = isPackageInstalled(context,
					"com.ktkumhorental.pdfform");
			// Log.i("####", "#### 인스톨상태입니까?" + isInstalled);

			if (isInstalled) {
				Intent i = new Intent();
				// myung 20131205 ADD 전자계약 호출 인텐트플래그 추가
				i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK
						| Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
				
				if("국문".equals(bt_language_start.getText().toString()))
				{
					language = "01";
				}
				else if("영문".equals(bt_language_start.getText().toString()))
				{
					language = "13";
				}
				else
				{
					language = "01";
				}
				//150311
				// 출고할때만 
				// 공백이면 p-type 으로 처리 하고 공백이아니면 sd 204 값이 들어있으면, 비교해서 있어.... 
				// 방금준 코드값으로 대체하면 되고 없으면 p_type4 으로 던져주면된다. 
				
				
				
//				String info = " :" + login_hm.get("LOGID") + ":"
//						+ login_hm.get("PERNR") + ":"
//						+ o_struct1.get("PSTYPE1") + ":"
//						+ o_struct1.get("PSTYPE2") + ":"
//						+ o_struct1.get("PSTYPE3") + ":"
//						+ o_struct1.get("VBELN_PS") + ":"
//						+ o_struct1.get("PSTYPE4") + ":" + bukrs + ":"
//						+ login_hm.get("DEPTNM") + ":" + login_hm.get("ENAME");
				
				
				
				
//				String info = " :" + login_hm.get("LOGID") + ":"
//						+ login_hm.get("PERNR") + ":"
//						+ o_struct1.get("PSTYPE1") + ":"
//						+ o_struct1.get("PSTYPE2") + ":"
//						+ o_struct1.get("PSTYPE3") + ":"
//						+ o_struct1.get("VBELN_PS") + ":"
//						+ language + ":" + bukrs + ":"
//						+ login_hm.get("DEPTNM") + ":" + login_hm.get("ENAME");
				
				
				
				
				String info = null;
				
				if("01".equals(o_struct1.get("PSTYPE4")))
				{
					kog.e("Jonathan", " ### language 로 보내준다. ");
					info = " :" + login_hm.get("LOGID") + ":"
							+ login_hm.get("PERNR") + ":"
							+ o_struct1.get("PSTYPE1") + ":"
							+ o_struct1.get("PSTYPE2") + ":"
							+ o_struct1.get("PSTYPE3") + ":"
							+ o_struct1.get("VBELN_PS") + ":"
							+ language + ":" + bukrs + ":"
							+ login_hm.get("DEPTNM") + ":" + login_hm.get("ENAME");
				}
				else
				{
					kog.e("Jonathan", " ### PSTYPE4 로 보내준다. ");
					info = " :" + login_hm.get("LOGID") + ":"
							+ login_hm.get("PERNR") + ":"
							+ o_struct1.get("PSTYPE1") + ":"
							+ o_struct1.get("PSTYPE2") + ":"
							+ o_struct1.get("PSTYPE3") + ":"
							+ o_struct1.get("VBELN_PS") + ":"
							+ o_struct1.get("PSTYPE4") + ":" + bukrs + ":"
							+ login_hm.get("DEPTNM") + ":" + login_hm.get("ENAME");

				}
				
				
				
				
				
//				 short

//				PSTYPE4 == h값 
				//Jonathan 150315
//				PSTYPE 1 d  -> 계약서 구분값.
//				PSTYPE 2 e  -> 상품 종류 조회 구.
//				PSTYPE 3 f  -> 전자 계약 진행여부 .
//				PSTYPE 4 h  -> 전자서식구분 .
				
				kog.e("Jonathan", "###PSTYPE1 :: " + o_struct1.get("PSTYPE1"));
				kog.e("Jonathan", "###PSTYPE2 :: " + o_struct1.get("PSTYPE2"));
				kog.e("Jonathan", "###PSTYPE3 :: " + o_struct1.get("PSTYPE3"));
				kog.e("Jonathan", "###PSTYPE4 :: " + o_struct1.get("PSTYPE4"));
				kog.e("Jonathan", "###language :: " + language);
				kog.e("Jonathan", "###인포 :: " + info);
				
				// Log.i("####", "####인포" + info);
				// epc.show(info);

				i.putExtra("INFO", info);
				i.setAction(Intent.ACTION_MAIN);
				i.addCategory(Intent.CATEGORY_LAUNCHER);
				i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
						| Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
				i.setComponent(new ComponentName("com.ktkumhorental.pdfform",
						"com.ktkumhorental.pdfform.PdfControlWindow"));
				// i.setComponent(new
				// ComponentName("com.example.aaa_intent_app","com.example.aaa_intent_app.MainActivity"));
				DEFINE.PAPERESS_STATUS_FLAG = true;
				context.startActivity(i);

				dismiss();
			} else {
				Intent intent = new Intent(Intent.ACTION_VIEW,
						Uri.parse(DEFINE.PAPER_DOWN_URL));
				DEFINE.PAPERESS_STATUS_FLAG = true;
				context.startActivity(intent);
			}
			break;
		case R.id.menu2_end_resist:

			// myung ADD 도착등록버튼 애러처리
			String time1 = bt_end_time.getText().toString();
			if (time1 == null || time1.equals("") || time1.equals(" ")) {
				EventPopupC epc_start_oil = new EventPopupC(context);
				epc_start_oil.show("도착일시를 입력해 주세요.");
				return;
			}

//			pp.setMessage("도착등록 중 입니다.");
//			pp.show();
			showProgress("도착등록 중 입니다.");

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
			String inkm = tv_end_km.getText().toString();
			i_struct1.put("INKM", onlyNum(inkm)); // [도착]KM
			i_struct1.put("INOIL", bt_end_m026.getTag().toString()); // [도착]Oil
																		// 코드
			String end_dfm = tv_end_dfm.getText().toString();
			i_struct1.put("INGDOIL", onlyNum(end_dfm)); // [도착]Oil 양(DFM)

			time1 = time1.replace(".", "").replace(":", "");
			String end_date = time1.substring(0, 8);
			// myung UPDATE week 추가에 따른 수정
			String end_time = time1.substring(9 + 3);
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

			if (bukrs.equals("3000")) {
				cc.setZMO_3140_WR02(i_struct1);
			} else if (bukrs.equals("3100")) {
				cc.setZMO_3200_WR02(i_struct1);
			}
			 
			//Jonathan 150324
			if("X".equals(o_struct1.get("DELEG")))
			{
				//위임장 있음 
				menu2_stepby_2.setEnabled(false);
			}
			else
			{
				//위임장 없음
				menu2_stepby_2.setEnabled(true);
			}
			

			break;
		case R.id.menu2_end_paperess:
			
			

				epc = new EventPopupC(context);

				isInstalled = isPackageInstalled(context,
						"com.ktkumhorental.pdfform");
				
				//Jonathan
				if("국문".equals(bt_language_end.getText().toString()))
				{
					kog.e("Jonathan", "국문으로 들어옴.");
					language = "01";
				}
				else if("영문".equals(bt_language_end.getText().toString()))
				{
					language = "13";
				}
				else
				{
					kog.e("Jonathan", "국문으로 들어옴.");
					language = "01";
				}
				

				if (isInstalled) {
					Intent i = new Intent();
					// myung 20131205 ADD 전자계약 호출 인텐트플래그 추가
					i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK
							| Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
//					String info = " :" + login_hm.get("LOGID") + ":"
//							+ login_hm.get("PERNR") + ":"
//							+ o_struct1.get("PETYPE1") + ":"
//							+ o_struct1.get("PETYPE2") + ":"
//							+ o_struct1.get("PETYPE3") + ":" 
//							+ o_struct1.get("VBELN_PE") + ":"
//							+ language + ":" + bukrs + ":"
//							+ login_hm.get("DEPTNM") + ":" + login_hm.get("ENAME");
					
					
//					String info = " :" + login_hm.get("LOGID") + ":"
//							+ login_hm.get("PERNR") + ":"
//							+ o_struct1.get("PETYPE1") + ":"
//							+ o_struct1.get("PETYPE2") + ":"
//							+ o_struct1.get("PETYPE3") + ":"
//							+ o_struct1.get("VBELN_PE") + ":"
//							+ o_struct1.get("PETYPE4") + ":" + bukrs + ":"
//							+ login_hm.get("DEPTNM") + ":" + login_hm.get("ENAME");
					
//					kog.e("Jonathan", "###PETYPE4 :: " + o_struct1.get("PETYPE4"));
					
					
					String info = null;
					
					if("01".equals(o_struct1.get("PETYPE4")))
					{
						kog.e("Jonathan", " ### language 로 보내준다. ");
						info = " :" + login_hm.get("LOGID") + ":"
								+ login_hm.get("PERNR") + ":"
								+ o_struct1.get("PETYPE1") + ":"
								+ o_struct1.get("PETYPE2") + ":"
								+ o_struct1.get("PETYPE3") + ":"
								+ o_struct1.get("VBELN_PE") + ":"
								+ language + ":" + bukrs + ":"
								+ login_hm.get("DEPTNM") + ":" + login_hm.get("ENAME");
					}
					else
					{
						kog.e("Jonathan", " ### PETYPE4 로 보내준다. ");
						info = " :" + login_hm.get("LOGID") + ":"
								+ login_hm.get("PERNR") + ":"
								+ o_struct1.get("PETYPE1") + ":"
								+ o_struct1.get("PETYPE2") + ":"
								+ o_struct1.get("PETYPE3") + ":"
								+ o_struct1.get("VBELN_PE") + ":"
								+ o_struct1.get("PETYPE4") + ":" + bukrs + ":"
								+ login_hm.get("DEPTNM") + ":" + login_hm.get("ENAME");
					}
					
					
					
					
					
					kog.e("Jonathan", "###PETYPE1 :: " + o_struct1.get("PETYPE1"));
					kog.e("Jonathan", "###PETYPE2 :: " + o_struct1.get("PETYPE2"));
					kog.e("Jonathan", "###PETYPE3 :: " + o_struct1.get("PETYPE3"));
					kog.e("Jonathan", "###PETYPE4 :: " + o_struct1.get("PETYPE4"));
					kog.e("Jonathan", "###language :: " + language);
					kog.e("Jonathan", "###인포 :: " + info);
					
					
					
					kog.e("KDH", "info = "+info);
					
					
					
					// Log.i("####", "####인포" + info);
					// epc.show(info);
					i.putExtra("INFO", info);
					i.setAction(Intent.ACTION_MAIN);
					i.addCategory(Intent.CATEGORY_LAUNCHER);
					i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
							| Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
					i.setComponent(new ComponentName("com.ktkumhorental.pdfform",
							"com.ktkumhorental.pdfform.PdfControlWindow"));
					// i.setComponent(new
					// ComponentName("com.example.aaa_intent_app","com.example.aaa_intent_app.MainActivity"));
					DEFINE.PAPERESS_STATUS_FLAG = true;
					context.startActivity(i);

					dismiss();
				} else {
					Intent intent = new Intent(Intent.ACTION_VIEW,
							Uri.parse(DEFINE.PAPER_DOWN_URL));
					DEFINE.PAPERESS_STATUS_FLAG = true;
					context.startActivity(intent);
				}

			
			

			break;
		case R.id.menu2_start_input2:
			// if(bt_start_m026.getText().equals(""))
			// Log.i("VVVV", bt_start_m026.getText().toString());
			pwm.show("ZDOILTY", bt_start_m026, tv_start_dfm, false);
			break;
		case R.id.menu2_end_input2:
			// myung 20131126 UPDATE
			pwm.show("ZDOILTY", bt_end_m026, tv_end_dfm, false);
			break;
		case R.id.menu2_start_input1:
			vg = nip.getViewGroup();
			final TextView input_start = (TextView) vg
					.findViewById(R.id.inventory_bt_input);
			done = (Button) vg.findViewById(R.id.inventory_bt_done);
			done.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					String num = input_start.getText().toString();
					num = num.replaceAll(",", "");
					String s_inkm = o_struct1.get("INKM");
					if(num.equals("") || num.equals(" "))
						num="0";
					Double i_num = Double.valueOf(num);
					if(s_inkm.equals("") || s_inkm.equals(" "))
						s_inkm="0";
					Double i_inkm = Double.valueOf(s_inkm);

					// if(i_num>i_inkm)
					if (0 > i_num) {
						EventPopupC epc = new EventPopupC(context);
						epc.show("출발거리를 확인해 주세요.");
						input_start.setText("");
						return;
					}

					Double distance = i_inkm - i_num; 
					ATOB_DISTANCE = currentpoint(distance + "") + " Km";
					tv_start_km.setText(currentpoint(num) + " Km");

					// tv_end[3].setText(ATOB_DISTANCE +"/"+ATOB_TIME);
					nip.setInput("CLEAR", true);
					nip.dismiss();
				}
			});
			nip.show(tv_start_km); 
			break;
		case R.id.menu2_end_input1:
			
			//Jonathan 150409 풀어달라고 해서 품.
//			if(Type_204.equals(o_struct1.get("AUGRU")))
//			{
//				if("X".equals(o_struct1.get("DELEG")))
//				{
//					//위임장 있음
//					vg = nip.getViewGroup();
//					final TextView input_end = (TextView) vg
//							.findViewById(R.id.inventory_bt_input);
//					done = (Button) vg.findViewById(R.id.inventory_bt_done);
//					done.setOnClickListener(new View.OnClickListener() {
//						@Override
//						public void onClick(View v) {
//							String num = input_end.getText().toString();
//							num = num.replaceAll(",", "");
//							String s_outkm = o_struct1.get("OUTKM");
//							if(num.equals("") || num.equals(" "))
//								num="0";
//							Double i_num = Double.valueOf(num);
//							if(s_outkm.equals("") || s_outkm.equals(" "))
//								s_outkm="0";
//							Double i_outkm = Double.valueOf(s_outkm);
//
//							if (i_num < i_outkm) {
//								EventPopupC epc = new EventPopupC(context);
//								epc.show("출발거리가 도착거리보다 큽니다.\n다시 입력해 주십시오.");
//								input_end.setText("");
//								return;
//							}
//
//							Double distance = i_num - i_outkm;
//							ATOB_DISTANCE = currentpoint(distance + "") + " Km";
//							tv_end_km.setText(currentpoint(num) + " Km");
//							tv_end[3].setText(ATOB_DISTANCE + "/" + ATOB_TIME);
//
//							nip.setInput("CLEAR", true);
//							nip.dismiss();
//						}
//					});
//					nip.show(tv_end_km);
//				}
//				else
//				{
//					//위임장 없음,  도착등록 버튼 비활성
//					EventPopupC epc1 = new EventPopupC(context);
//					epc1.show("위임장 작성이 되지 않았습니다. 위임장을 입력하세요.");
//				}
//			}
//			else
//			{
				//사고 아닐때 그냥 패스
				vg = nip.getViewGroup();
				final TextView input_end = (TextView) vg
						.findViewById(R.id.inventory_bt_input);
				done = (Button) vg.findViewById(R.id.inventory_bt_done);
				done.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						String num = input_end.getText().toString();
						num = num.replaceAll(",", "");
						String s_outkm = o_struct1.get("OUTKM");
						if(num.equals("") || num.equals(" "))
							num="0";
						Double i_num = Double.valueOf(num);
						if(s_outkm.equals("") || s_outkm.equals(" "))
							s_outkm="0";
						Double i_outkm = Double.valueOf(s_outkm);

						if (i_num < i_outkm) {
							EventPopupC epc = new EventPopupC(context);
							epc.show("출발거리가 도착거리보다 큽니다.\n다시 입력해 주십시오.");
							input_end.setText("");
							return;
						}

						Double distance = i_num - i_outkm;
						ATOB_DISTANCE = currentpoint(distance + "") + " Km";
						tv_end_km.setText(currentpoint(num) + " Km");
						tv_end[3].setText(ATOB_DISTANCE + "/" + ATOB_TIME);

						nip.setInput("CLEAR", true);
						nip.dismiss();
					}
				});
				nip.show(tv_end_km);
				
//			}
			
			
			
			break;
		case R.id.menu2_start_input3: // 출발 오일3
			
			if (bt_start_m026.getTag().equals("2")) {
				vg = ipd.getViewGroup();
				final TextView dfm_start = (TextView) vg
						.findViewById(R.id.inventory_bt_input);
				done = (Button) vg.findViewById(R.id.inventory_bt_done);
				done.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						String num = dfm_start.getText().toString();
						tv_start_dfm.setText(num + "L");
						ipd.setInput("CLEAR", true);
						ipd.dismiss();
					}
				});
				ipd.show(tv_start_dfm);
			} else if (bt_start_m026.getTag().equals("1")) {
				vg = ipd1.getViewGroup();
				final TextView dfm_start = (TextView) vg
						.findViewById(R.id.inventory_bt_input);
				done = (Button) vg.findViewById(R.id.inventory_bt_done);
				done.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						String num = dfm_start.getText().toString();
						tv_start_dfm.setText(num + "칸");
						ipd1.setInput("CLEAR", true);
						ipd1.dismiss();
					}
				});
				ipd1.show(tv_start_dfm);
			}

			break;
		case R.id.menu2_end_input3:// 도착 오일3
			
			
			//Jonathan 150409 풀어달라고 해서 풀어줌.
			
//			if(Type_204.equals(o_struct1.get("AUGRU")))
//			{
//				if("X".equals(o_struct1.get("DELEG")))
//				{
//					//위임장 있음
//					if (bt_end_m026.getTag().equals("1")) {
//						vg = ipd1.getViewGroup();
//						final TextView dfm_end = (TextView) vg
//								.findViewById(R.id.inventory_bt_input);
//						done = (Button) vg.findViewById(R.id.inventory_bt_done);
//						done.setOnClickListener(new View.OnClickListener() {
//							@Override
//							public void onClick(View v) {
//								String num = dfm_end.getText().toString();
//								tv_end_dfm.setText(num + "칸");
//								ipd1.setInput("CLEAR", true);
//								ipd1.dismiss();
//							}
//						});
//						ipd1.show(tv_end_dfm);
//					} else if (bt_end_m026.getTag().equals("2")) {
//						vg = ipd.getViewGroup();
//						final TextView dfm_end = (TextView) vg
//								.findViewById(R.id.inventory_bt_input);
//						done = (Button) vg.findViewById(R.id.inventory_bt_done);
//						done.setOnClickListener(new View.OnClickListener() {
//							@Override
//							public void onClick(View v) {
//								String num = dfm_end.getText().toString();
//								tv_end_dfm.setText(num + "L");
//								ipd.setInput("CLEAR", true);
//								ipd.dismiss();
//							}
//						});
//						ipd.show(tv_end_dfm);
//					}
//				}
//				else
//				{
//					//위임장 없음,  도착등록 버튼 비활성
//					EventPopupC epc1 = new EventPopupC(context);
//					epc1.show("위임장 작성이 되지 않았습니다. 위임장을 입력하세요.");
//				}
//			}
//			else
//			{
				//사고 아닐때 그냥 패스
				if (bt_end_m026.getTag().equals("1")) {
					vg = ipd1.getViewGroup();
					final TextView dfm_end = (TextView) vg
							.findViewById(R.id.inventory_bt_input);
					done = (Button) vg.findViewById(R.id.inventory_bt_done);
					done.setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							String num = dfm_end.getText().toString();
							tv_end_dfm.setText(num + "칸");
							ipd1.setInput("CLEAR", true);
							ipd1.dismiss();
						}
					});
					ipd1.show(tv_end_dfm);
				} else if (bt_end_m026.getTag().equals("2")) {
					vg = ipd.getViewGroup();
					final TextView dfm_end = (TextView) vg
							.findViewById(R.id.inventory_bt_input);
					done = (Button) vg.findViewById(R.id.inventory_bt_done);
					done.setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							String num = dfm_end.getText().toString();
							tv_end_dfm.setText(num + "L");
							ipd.setInput("CLEAR", true);
							ipd.dismiss();
						}
					});
					ipd.show(tv_end_dfm);
				}
				
//			}
			

			break;
		case R.id.menu2_end_input5:
			final Cost_Resist_Dialog crd = new Cost_Resist_Dialog(context,
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
			bt_save.setOnClickListener(new View.OnClickListener() {
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
					cost.put("TOT_AMT", opera);

					crd.dismiss();
				}
			});
			crd.show();
			break;
		case R.id.menu2_driver_input:
			final Driver_Info_Dialog did = new Driver_Info_Dialog(context,
					o_itab3);
			Button bt_donea = (Button) did
					.findViewById(R.id.driver_info_save_id);
			bt_donea.setOnClickListener(new View.OnClickListener() {
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
					if (bukrs.equals("3000")) {
						cc.setZMO_3140_WR03(i_struct1);
					} else if (bukrs.equals("3100")) {
						cc.setZMO_3200_WR03(i_struct1);
					}

					did.dismiss();
				}
			});
			did.show();
			break;

		case R.id.menu2_stepby_1:
			// myung20131118 UPDATE 운전자정보 전달
			ArrayList<String> strArryDriverInfo = new ArrayList<String>();
			strArryDriverInfo.add(o_struct1.get("NAME1_CD"));
			strArryDriverInfo.add(o_struct1.get("TELF13"));
			final Stepby_Resist_Dialog srd = new Stepby_Resist_Dialog(context,
					o_itab2, o_itab3, bukrs, strArryDriverInfo);
			// final Stepby_Resist_Dialog srd = new
			// Stepby_Resist_Dialog(context, o_itab2, o_itab3, bukrs);
			srd.setOnDismissListener(new OnDismissListener() {
				@Override
				public void onDismiss(DialogInterface dialog) {
					if (srd.SEARCH) {
						pp.setMessage("상세 조회중 입니다.");
						pp.show();
						if (bukrs.equals("3000")) {
							cc.getZMO_3140_RD01(top1, top2, top3, period,
									date1, date2, vbeln_vl);
						} else if (bukrs.equals("3100")) {
							cc.getZMO_3200_RD01(top1, top2, top3, period,
									date1, date2, vbeln_vl);
						}
					}
				}
			});
			boolean bool = false;
			if (MODIYN.equals("B")) {
				bool = true;
			}
			// myung 20131118 수정 경유지 입력 화면 팝업
			// else
			srd.show(o_struct1.get("VBELN_VL"), bool);
			break;
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
			cp = new CalendarPopup(context, this);
			cp.show(bt_start_time);
			cp.setHour(hour);
			cp.setMinute(minute);
			break;
		case R.id.menu2_end_input4:
			cp = new CalendarPopup(context, this);
			cp.show(bt_end_time);
			cp.setStartTime(bt_start_time.getText().toString());
			cp.setHour(hour);
			cp.setMinute(minute);
			break;
			
		//myung 20131211 ADD 주소창에서 1라인을 넘을 경우 … 풍선 추가하여 클릭시 주소 확인
		case R.id.menu2_start2_1:
			showTextBalloon(tv_start[2]);
			break;
		case R.id.menu2_end2_1:
			showTextBalloon(tv_end[2]);
			break;
		//myung 20131220 ADD 주소창에서 1라인을 넘을 경우 … 풍선 추가하여 클릭시 주소 확인
		case R.id.menu2_start_bigo:
			showTextBalloon(bt_start_bigo);
			break;
		case R.id.menu2_end_bigo:
			showTextBalloon(bt_end_bigo);
			break;
			
			
		case R.id.menu2_stepby_2:
			
			epc = new EventPopupC(context);

			String attorney = "98";
			String short_go = "short";
			
			isInstalled = isPackageInstalled(context,
					"com.ktkumhorental.pdfform");

			if (isInstalled) {
				Intent i = new Intent();
				// myung 20131205 ADD 전자계약 호출 인텐트플래그 추가
				i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK
						| Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
				
				 
				//Jonathan 150316
//				String info = " :" + login_hm.get("LOGID") + ":"
//						+ login_hm.get("PERNR") + ":"
//						+ o_struct1.get("PSTYPE1") + ":"
//						+ o_struct1.get("PSTYPE2") + ":"
//						+ o_struct1.get("PSTYPE3") + ":"
//						+ o_struct1.get("VBELN_PS") + ":"
//						+ attorney + ":" + bukrs + ":"
//						+ login_hm.get("DEPTNM") + ":" + login_hm.get("ENAME");
				
				
				kog.e("Jonathan", "여기 들어오니?? 위임장 누름." );
				
				String info = " :" + login_hm.get("LOGID") + ":"
						+ login_hm.get("PERNR") + ":"
						+ short_go + ":"
						+ "out" + ":"
						+ o_struct1.get("PSTYPE3") + ":"
						+ o_struct1.get("VBELN_VA") + ":"
						+ attorney + ":" + bukrs + ":"
						+ login_hm.get("DEPTNM") + ":" + login_hm.get("ENAME");
				
				
				kog.e("Jonathan", "###PSTYPE1 :: " + o_struct1.get("PSTYPE1"));
				kog.e("Jonathan", "###PSTYPE2 :: " + o_struct1.get("PSTYPE2"));
				kog.e("Jonathan", "###PSTYPE3 :: " + o_struct1.get("PSTYPE3"));
				kog.e("Jonathan", "###attorney :: " + attorney);
				kog.e("Jonathan", "###인포 :: " + info);
//				 short

//				PSTYPE4 == h값 
				//Jonathan 150315
				// a 영업그룹
				// b id 
				// c 사원번호
//				PSTYPE 1 d  -> 계약서 구분값.
//				PSTYPE 2 e  -> 상품 종류 조회 구.
//				PSTYPE 3 f  -> 전자 계약 진행여부 .
				// g 계약번호
//				PSTYPE 4 h  -> 전자서식구분 .

				i.putExtra("INFO", info);
				i.setAction(Intent.ACTION_MAIN);
				i.addCategory(Intent.CATEGORY_LAUNCHER);
				i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
						| Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
				i.setComponent(new ComponentName("com.ktkumhorental.pdfform",
						"com.ktkumhorental.pdfform.PdfControlWindow"));
				DEFINE.PAPERESS_STATUS_FLAG = true;
				context.startActivity(i);

				dismiss();
			} else {
				Intent intent = new Intent(Intent.ACTION_VIEW,
						Uri.parse(DEFINE.PAPER_DOWN_URL));
				DEFINE.PAPERESS_STATUS_FLAG = true;
				context.startActivity(intent);
			}
			
			
			
		}
	}
	
	//myung 20131211 ADD 주소창에서 1라인을 넘을 경우 … 풍선 추가하여 클릭시 주소 확인
	private void showTextBalloon(TextView v){
		Layout layout = v.getLayout();
		if(layout != null){
			if (layout.getEllipsisCount(layout.getLineCount()-1) > 0) {
				Popup_Window_Text_Balloon pwtb = new Popup_Window_Text_Balloon(context);
				pwtb.show(v, v.getText().toString());
			}
		}
	}
	
	//myung 20131211 ADD 주소창에서 1라인을 넘을 경우 … 풍선 추가하여 클릭시 주소 확인
	private void isEnableTextBalloon(TextView v){
		Layout layout = v.getLayout();
		if(layout != null){
			if (layout.getEllipsisCount(layout.getLineCount()-1) > 0) {
				v.setEnabled(true);
			}else{
				v.setEnabled(false);
			}
				
		}
	}
	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		
		super.onBackPressed();
	}
	
	// myung 20131118 출발OIL 체크
	public String checkOILValue() {
		String strOilValue = tv_start_dfm.getText().toString();
		// int nPos=-1;
		// if(strOilValue.indexOf("칸")!=-1)
		// nPos=strOilValue.indexOf("칸");
		// else if(strOilValue.indexOf("L")!=-1)
		// nPos=strOilValue.indexOf("L");
		if (strOilValue.contains("칸") || strOilValue.contains("L")) {
			strOilValue = strOilValue.substring(0, strOilValue.length() - 1);
			return strOilValue;
		}

		return strOilValue;
	}

	public static String onlyNum(String str) {
		if (str == null)
			return "";
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < str.length(); i++) {
			if (Character.isDigit(str.charAt(i))) {
				sb.append(str.charAt(i));
			}
		}
		return sb.toString();
	}

	public void Temp(Button anchor, YyyyMmDdHhMm yyyymmddhhmm) {
		Object s_obj = bt_start_time.getText();
		Object e_obj = bt_end_time.getText();
		String s_date = s_obj == null || s_obj.toString().equals("") ? ""
				: s_obj.toString();
		String e_date = e_obj == null || e_obj.toString().equals("") ? ""
				: e_obj.toString();

		if (anchor == bt_start_time) {
			s_date = bt_start_time.getText().toString();
		} else if (anchor == bt_end_time) {
			e_date = bt_end_time.getText().toString();
		}

		if (s_date.equals("") || e_date.equals("") || s_date.equals(" ")
				|| e_date.equals(" ")) {
			ATOB_TIME = "0시간";
			// Log.i("####", "#### 0시간");
		} else {
			// Log.i("####", "#### 시작시간" + s_date);
			// Log.i("####", "#### 도착시간" + e_date);

			StringTokenizer st = new StringTokenizer(s_date, ".| |:");
			int year = Integer.parseInt(st.nextToken());
			int month = Integer.parseInt(st.nextToken()) - 1;
			int day = Integer.parseInt(st.nextToken());
			int hour = Integer.parseInt(st.nextToken());
			int minute = Integer.parseInt(st.nextToken());
			Calendar cal = new GregorianCalendar(year, month, day, hour, minute);
			long s_milliSecond = cal.getTimeInMillis();
			st = new StringTokenizer(e_date, ".| |:");
			year = Integer.parseInt(st.nextToken());
			month = Integer.parseInt(st.nextToken()) - 1;
			day = Integer.parseInt(st.nextToken());
			hour = Integer.parseInt(st.nextToken());
			minute = Integer.parseInt(st.nextToken());
			cal = new GregorianCalendar(year, month, day, hour, minute);
			long e_milliSecond = cal.getTimeInMillis();

			long duration = Math.abs(e_milliSecond - s_milliSecond);
			long hours = TimeUnit.MILLISECONDS.toHours(duration);
			duration -= TimeUnit.HOURS.toMillis(hours);
			long minutes = TimeUnit.MILLISECONDS.toMinutes(duration);
			// Log.i("####", "####time" + hours + "시간" + minutes + "분");
			ATOB_TIME = hours + "시간" + minutes + "분";
			tv_end[3].setText(ATOB_DISTANCE + "/" + ATOB_TIME);
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

	String top1;
	String top2;
	String top3;
	String period;
	String date1;
	String date2;
	String vbeln_vl;
	String bukrs;

	public void show(String top1, String top2, String top3, String period,
			String date1, String date2, String vbeln_vl) {
		this.top1 = top1;
		this.top2 = top2;
		this.top3 = top3;
		this.period = period;
		this.date1 = date1;
		this.date2 = date2;
		this.vbeln_vl = vbeln_vl;

		show();
	}

	public void show(String top1, String top2, String top3, String period,
			String date1, String date2, String vbeln_vl, String bukrs) {
		this.top1 = top1;
		this.top2 = top2;
		this.top3 = top3;
		this.period = period;
		this.date1 = date1;
		this.date2 = date2;
		this.vbeln_vl = vbeln_vl;
		this.bukrs = bukrs;
		show();
	}

}
