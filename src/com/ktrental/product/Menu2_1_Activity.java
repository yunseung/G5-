package com.ktrental.product;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.telephony.SmsManager;
import android.text.Layout;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.ktrental.R;
import com.ktrental.adapter.Menu4_Tire_Adapter_PM033;
import com.ktrental.adapter.Menu4_Tire_Adapter_PM056;
import com.ktrental.beans.PM033;
import com.ktrental.beans.PM056;
import com.ktrental.call.Common;
import com.ktrental.cm.connect.ConnectController;
import com.ktrental.cm.connect.ConnectController2;
import com.ktrental.cm.connect.Connector.ConnectInterface;
import com.ktrental.cm.db.DbAsyncTask;
import com.ktrental.cm.db.DbAsyncTask.DbAsyncResLintener;
import com.ktrental.common.AppSt;
import com.ktrental.common.DEFINE;
import com.ktrental.common.KtRentalApplication;
import com.ktrental.dialog.Customer_Info_Dialog;
import com.ktrental.dialog.History_Dialog;
import com.ktrental.dialog.KDH_check_list;
import com.ktrental.dialog.Menu3_Resist_Dialog;
import com.ktrental.dialog.Tire_List_Dialog;
import com.ktrental.dialog.Tire_Picture_Dialog;
import com.ktrental.fragment.TireUpload_Async;
import com.ktrental.model.DbQueryModel;
import com.ktrental.model.KDH_COMMON;
import com.ktrental.model.LoginModel;
import com.ktrental.model.TableModel;
import com.ktrental.popup.CallSendPopup2;
import com.ktrental.popup.EventPopup1;
import com.ktrental.popup.EventPopupC;
import com.ktrental.popup.EventPopupCC;
import com.ktrental.popup.InsureSMSPopup;
import com.ktrental.popup.MovementDialog;
import com.ktrental.popup.Popup_Window_Text_Balloon;
import com.ktrental.popup.ProgressPopup;
import com.ktrental.util.LogUtil;
import com.ktrental.util.OnEventCancelListener;
import com.ktrental.util.OnEventOkCallListener;
import com.ktrental.util.OnEventOkListener;
import com.ktrental.util.kog;

import java.io.File;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class Menu2_1_Activity extends BaseActivity implements ConnectInterface, OnClickListener {

	private final String SNDCUS_X = "X";
	private final String INFO_SUCC = "4";
	private final String INFO_FAIL = "5";
	private final String INFO_FAIL_CON = "6";

	private Intent in;
	private String CUSNAM;
	private String AUFNR;
	private String MODE;
	private String INVNR;
	private String EQUNR;
	private String KUNNR;

	private String mCusReq;
	protected EventPopupC epc;

	// 공통
	private TextView tv_top1_id1, tv_top1_id3, tv_top1_id4;
	private TextView tv_top2_id1, tv_top2_id2, tv_top2_id3, tv_top2_id4, tv_top2_id5, tv_top2_id6, tv_top2_id7;
	private LinearLayout Layout_bejung_suheng;

	private TextView menu4_top2_id6_1, menu4_top2_id7_1;

	// 사고
	private TextView tv_accident_line1_id1, tv_accident_line1_id2, tv_accident_line1_id3;
	private TextView tv_accident_line2_id1, tv_accident_line2_id2, tv_accident_line2_id3;
	private TextView tv_accident_line3_id1, tv_accident_line3_id2, tv_accident_line3_id3;
	private TextView tv_accident_line4_id1;
	private TextView tv_accident_line5_id1, tv_accident_line5_id2;
	private TextView tv_accident_line6_id1;
	private TextView tv_accident_line7_id1;
	private TextView tv_accident_line8_id1;
	private TextView tv_accident_line8_id2;
	private TextView tv_accident_line9_id1, tv_accident_line9_id2, tv_accident_line9_id3;
	private TextView tv_accident_line9_id4;
	private Button bt_accident_done1, bt_accident_done2, bt_accident_done3, bt_simple_end;
	private Button bt_accident_insure_call;
	private TextView tv_accident_insure_sp_name, tv_accident_insure_sp_number;


	// 일반
	private TextView tv_normal_line1_id1, tv_normal_line1_id2;
	private TextView tv_normal_line2_id1;
	private TextView tv_normal_line3_id1;
	private TextView tv_normal_line4_id1;
	private EditText tv_normal_line5_id1;
	private TextView tv_normal_line6_id1;
	private TextView tv_normal_line7_id1, tv_normal_line7_id2, tv_normal_line7_id3;
	private TextView tv_normal_line7_id4;
	private Button bt_normal_done1, bt_normal_done2, bt_normal_done3;
	private ImageView img_recall_target, img_recall_done;
	private boolean isCheck_recall_target, isCheck_recall_done;

	// 긴급
	private TextView tv_emergency_line1_id1, tv_emergency_line1_id2;
	private TextView tv_emergency_line2_id1, tv_emergency_line2_id2;
	private TextView tv_emergency_line3_id1, tv_emergency_line3_id2;
	private ImageView iv_emergency_line4_id1, iv_emergency_line4_id2, iv_emergency_line4_id3, iv_emergency_line4_id4;
	private ImageView iv_emergency_line5_id1, iv_emergency_line5_id2, iv_emergency_line5_id3, iv_emergency_line5_id4;
	private TextView tv_emergency_line6_id1;
	private TextView tv_emergency_line7_id1;
	private TextView tv_emergency_line8_id1, tv_emergency_line8_id2, tv_emergency_line8_id3;
	private Button bt_emergency_done1, bt_emergency_done2;

	// 2013.12.03 ypkim 타이어
	private TextView tv_tire_line1_id1, tv_tire_line1_id2, tv_tire_line1_id3;
	private TextView tv_tire_line2_id1, tv_tire_line2_id2;
	private TextView tv_tire_line3_id1;
	private TextView tv_tire_line4_id1, tv_tire_line4_id2;
	private TextView tv_tire_line5_id1;
	private TextView tv_tire_line6_id1;
	private EditText tv_tire_line7_id1;
	private EditText tv_tire_line8_id1;
	private TextView tv_tire_line9_id1, tv_tire_line9_id2, tv_tire_line9_id3, tv_tire_line9_id4;
	private Button bt_tire_done1, bt_tire_done2, bt_tire_done3;

	private LinearLayout back[] = new LinearLayout[4];

	private LinearLayout ll_driver;

	private Button bt_customer, bt_tire, bt_history, bt_call, bt_custom_call;

	// Jonathan 150309
	private Button menu4_tire_add_id, menu4_tire_delete_id;
	private TextView menu4_tire_search_bon_id;

	private Button menu4_tire_add_id_01, menu4_tire_delete_id_01;
	private TextView menu4_tire_search_bon_id_01;

	private Tire_Picture_Dialog tpd;
	// public static ArrayList<PM081> pm081_arr_menu4 = new ArrayList<PM081>();
	// public static ArrayList<String> pm081_del_arr_menu4 = new
	// ArrayList<String>();

	public static ArrayList<PM033> pm033_arr = new ArrayList<PM033>();
	public static ArrayList<String> pm033_del_arr = new ArrayList<String>();

	public static ArrayList<PM056> pm056_arr = new ArrayList<PM056>();
	public static ArrayList<String> pm056_del_arr = new ArrayList<String>();

	public static ArrayList<PM033> pm033_arr_prev = new ArrayList<PM033>(); // 2019.01.31. 이전 업로드한 사진
	public static ArrayList<PM056> pm056_arr_prev = new ArrayList<PM056>(); // 2019.01.31. 이전 업로드한 사진

	private ListView menu4_tire_list_id;
	private ListView menu4_tire_list_id_01;

	private Menu4_Tire_Adapter_PM033 ta_033;
	private Menu4_Tire_Adapter_PM056 ta_056;
	private ScrollView menu4_sv;
	// Jonathan 150327
	ArrayList<String> LISTa;

	// Jonathan 150414
	private TextView menu4_call_time1, menu4_call_time2, menu4_call_time3;
	private String Call_Time;

	// private Button bt_accident_done, bt_accident_close;
	// private Button bt_normal_done, bt_normal_close;
	// private Button bt_emergency_close;

	HashMap<String, String> O_STRUDCT1;
	HashMap<String, String> O_STRUDCT2;
	HashMap<String, String> O_STRUDCT3;
	HashMap<String, String> O_STRUDCT4;
	HashMap<String, String> O_STRUDCT5;
	ArrayList<HashMap<String, String>> O_ITAB1;

	String dam01nm = null; // 담당자 이름. 지금은 쓰는 곳 없다.
	String telf1 = null; // 담당자 연락처. "보험대차 요청" 버튼 옆에 연락처를 이걸로 보여주면 된다.
	String lifnrNm = null; // 공급처 이름. "보험대차 요청" 버튼 옆에 이걸로 보여주면 된다.
	String lifnr = null; // 공급처 코드. ZPM_0087_002 호출 시 이거 넣어 보낸다.

	String attatNo;
	
	public static ConnectController2 cc2;
	

	
	// 2014-02-04 KDH 귀찮어서 배열
	LinearLayout L_send[];
	int L_send_id[] = { R.id.L_send1, R.id.L_send2, R.id.L_send3, R.id.L_send4, };

	EditText L_edit[];
	int L_edit_id[] = { R.id.editText1, R.id.editText2, R.id.editText3, R.id.editText4, };

	Button L_button[];
	int L_button_id[] = { R.id.btn_send1, R.id.btn_send2, R.id.btn_send3, R.id.btn_send4, };

	EditText mEditText;


	//2017.07.04. hjt. 출고안내
	int L_checkbox_id[] = {R.id.checkBox1, R.id.checkBox2, R.id.checkBox3, R.id.checkBox4, R.id.checkBox5, R.id.checkBox6, R.id.checkBox7, };
//	int L_checkbox_id[] = {R.id.checkBox1, R.id.checkBox2, R.id.checkBox3, R.id.checkBox4, R.id.checkBox5, R.id.checkBox6};

	// 2014-02-11 KDH 화면추가
	int LC_id[] = { R.id.L_C1, R.id.L_C2, R.id.L_C3, R.id.L_C4, R.id.L_C5, R.id.L_C6, R.id.L_C7, };
//	int LC_id[] = { R.id.L_C1, R.id.L_C2, R.id.L_C3, R.id.L_C4, R.id.L_C5, R.id.L_C6, };

	LinearLayout L_C[];
	ImageView C_box[];
	boolean isCBox[];
	// 2014-02-13 답없어서-_-전역으로뻄 아놔.
	int getSendPos = AppSt.EMPTY;
	
	
	
	// 20160831 Jonathan 
	LinearLayout llCheckReceive;
	ImageView ivCheckReceive;
	TextView tvCheckReceive, tvCheckReceiveTime;

	/**
	 * 2014-02-13 KDH 일단 4개의 정비과정에서 안내상태 값때문에 전역으로뺌!
	 */
	ArrayList<HashMap<String, String>> TOTAL_O_ITAB1;

	ArrayList<HashMap<String, String>> O_FTAB1;
	/**
	 * 2019-01-30. SMS 발송 여부 확인을 위해 추가.
	 */
	BroadcastReceiver sentReceiver = null;
	int msgParts = 0;
	private final String SENT_ACTION = "SMS_SENT_ACTION";

	private int mSendSmsCount = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.menu4_activity);

		O_STRUDCT1 = new HashMap<String, String>();
		O_STRUDCT2 = new HashMap<String, String>();
		O_STRUDCT3 = new HashMap<String, String>();
		O_STRUDCT4 = new HashMap<String, String>();
		O_STRUDCT5 = new HashMap<String, String>();

		// myung 20131212 UPDATE 항목 세팅전에 조회중 프로그래시브창이 사라지는 문제
		// init();
		
		cc2 = cc;

		init(0);
		initView();
		in = getIntent();
		// Jonathan 150624 사진찍은거 겹쳐서 나오는것 수정
		initData();

		// 2014-01-23 KDH MODE별로 정하면된다. =ㅇ='
		MODE = in.getStringExtra("MODE");

		AUFNR = in.getStringExtra("AUFNR");
		CUSNAM = in.getStringExtra("CUSNAM");
		INVNR = in.getStringExtra("INVNR");
		EQUNR = in.getStringExtra("EQUNR");
		KUNNR = in.getStringExtra("KUNNR");
		// Log.i("####", "#### 인텐트" + MODE + "/" + AUFNR);

		if (MODE.equals("A")) { // 사고
			setShowTab(0);
			// ll_driver.setVisibility(View.GONE); // Jonathan 14.06.17 주석 처리 함.
			// 사고, 일반 일때도 운전자 명 필요하다고 요청 옴.
		} else if (MODE.equals("B")) { // 일반
			setShowTab(1);
			// ll_driver.setVisibility(View.GONE); // Jonathan 14.06.17 주석 처리 함.
		} else if (MODE.equals("C")) {
			setShowTab(2);
			ll_driver.setVisibility(View.GONE);
		} else if (MODE.equals("D")) {
			setShowTab(0);
		} else if (MODE.equals("E")) { // 2013.12.03 YPKIM //타이어
			setShowTab(3);
		}

		kog.e("Jonathan", " AUFNR :: Menu2_1 :: " + AUFNR);

		if (MODE.equals("A")) {
			pp.show();
			cc.getZMO_1100_RD05(AUFNR);
		} else if (MODE.equals("B")) {
			pp.show();
			cc.getZMO_1100_RD06(AUFNR);
		} else if (MODE.equals("C")) {
			pp.show();
			cc.getZMO_1100_RD04(AUFNR);
		} else if (MODE.equals("E")) { // 2013.12.03 YPKIM // 타이어
			pp.show();
			cc.getZMO_1030_RD08(AUFNR);
		}
	}

	@Override
	protected void onPause() {
		pp.dismiss();
		super.onPause();
	}

	// 2014-02-05 KDH 귀찮어서 여기서 생성해버렸더니 받을때가 문제라서 전역으로뺌.
	int getPos;
	String MODE_NUM;

	private void setShowTab(int num) {
		back[0].setVisibility(View.GONE);
		back[1].setVisibility(View.GONE);
		back[2].setVisibility(View.GONE);
		// 2013.12.03 ypkim
		back[3].setVisibility(View.GONE);

		getPos = num;

		back[num].setVisibility(View.VISIBLE);
		if(MODE != null && MODE.equals("A") && bt_simple_end != null){
			bt_simple_end.setVisibility(View.VISIBLE);
		} else {
			bt_simple_end.setVisibility(View.INVISIBLE);
		}
		L_send = new LinearLayout[L_send_id.length];
		L_edit = new EditText[L_edit_id.length];
		L_button = new Button[L_button_id.length];

		for (int i = 0; i < L_send_id.length; i++) {
			L_send[i] = (LinearLayout) findViewById(L_send_id[i]);
		}


		for (int i = 0; i < L_edit_id.length; i++) {
			L_edit[i] = (EditText) findViewById(L_edit_id[i]);
		}


		for (int i = 0; i < L_button_id.length; i++) {
			L_button[i] = (Button) findViewById(L_button_id[i]);
		}



//		if(num == 3)
//		{
//			llCheckReceive.setVisibility(View.VISIBLE);
//		}
//		else
//		{
//			llCheckReceive.setVisibility(View.GONE);
//		}
		

//		mEditText = (EditText) L_send[num].findViewById(R.id.editText1);
		mEditText = (EditText) L_edit[num];
//		Button btn_send = (Button) L_send[num].findViewById(R.id.btn_send);
		Button btn_send = (Button) L_button[num];
		btn_send.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				// Jonathan 150309 주석 풀고 else에 밑에거 넣기

				// if (menu4_tire_list_id == null ||
				// menu4_tire_list_id.getChildCount() <= 0) {
				// EventPopupC epc = new EventPopupC(context);
				// epc.show("타이어항목은 필수 입력사항입니다.\n타이어항목을 선택해 주세요.");
				// }
				// else
				// {
				//
				// }

				// TODO Auto-generated method stub
				String str = mEditText.getText().toString();
				if (str.length() > 0) {
					ArrayList<HashMap<String, String>> IT_TAB = new ArrayList<HashMap<String, String>>();
					HashMap<String, String> _data = new HashMap<String, String>();

					_data.put("AUFNR", AUFNR);
					_data.put("PROC_TXT", str);

					if(MODE.equals("B")) {
						String recallst = "";
						if(isCheck_recall_done){
							recallst = "X";
						}
						_data.put("RECALLST", recallst);
					}
					IT_TAB.add(_data);
					cc.getZMO_1100_WR01(IT_TAB);
					pp.show();

					// if (MODE.equals("A")) // 사고
					// {
					// MODE_NUM = "1";
					// }
					// else if (MODE.equals("B")) // 일반
					// {
					// MODE_NUM = "2";
					// }
					//
					//
					// //사진전송
					//
					// kog.e("Jonathan", "사진전송 시작");
					//
					// Object data;
					// int check_count = 0;
					// //final String[] path = null ;
					// //final String[] name = null;
					//
					// if("1".equals(MODE_NUM)) //사고
					// {
					// for (int i = 0; i < pm033_arr.size(); i++) {
					//
					// kog.e("Jonathan", "사진전송 pm033_arr_menu4 (" + i + ")");
					//
					// if (!pm033_arr.get(i).PATH.equals(""))
					// check_count++;
					// }
					// //final String[] path = new String[check_count];
					// //final String[] name = new String[check_count];
					// if(check_count != 0)
					// {
					// final String[] path_1 = new String[check_count];
					// final String[] name_1 = new String[check_count];
					//
					// int count = 0;
					// for (int i = 0; i < pm033_arr.size(); i++) {
					// if (!pm033_arr.get(i).PATH.equals("")) {
					// int num = pm033_arr.get(0).PATH.lastIndexOf("/") + 1;
					// String aa = pm033_arr.get(0).PATH.substring(num);
					//
					// path_1[count] = pm033_arr.get(i).PATH;
					// name_1[count] = aa;
					//
					// kog.e("Jonathan", "사진전송 Path :: " + pm033_arr.get(i).PATH
					// );
					//
					// count++;
					// }
					// }
					//
					//
					//
					// kog.e("Jonathan", "사진전송 TireUpload_Async 시작 ");
					// TireUpload_Async asynctask = new
					// TireUpload_Async(context) {
					// @Override
					// protected void onProgressUpdate(ArrayList<String>...
					// values) {
					// ArrayList<String> list = values[0];
					// for (int i = 0; i < list.size(); i++) {
					// kog.e("Jonathan", "사진전송 사진코드넘버들" + list.get(i));
					// }
					//
					// LISTa = list;
					// kog.e("Jonathan","타이어 신청 중 입니다.");
					// // cc.setZMO_1060_WR01(getTableZMO_1060_WR01(path, list),
					// "", "");
					// cc.setZMO_1100_WR02(MODE_NUM,
					// getTableZMO_1100_WR02(path_1, list));
					// }
					// };
					// asynctask.execute(path_1);
					// }
					//
					//
					// }
					// else if("2".equals(MODE_NUM)) // 일반
					// {
					// for (int i = 0; i < pm056_arr.size(); i++) {
					//
					// kog.e("Jonathan", "사진전송 pm056_arr_menu4 (" + i + ")");
					//
					// if (!pm056_arr.get(i).PATH.equals(""))
					// check_count++;
					// }
					// //final String[] path = new String[check_count];
					// //final String[] name = new String[check_count];
					//
					// if(check_count != 0)
					// {
					// final String[] path_2 = new String[check_count];
					// final String[] name_2 = new String[check_count];
					//
					// int count = 0;
					// for (int i = 0; i < pm056_arr.size(); i++) {
					// if (!pm056_arr.get(i).PATH.equals("")) {
					// int num = pm056_arr.get(0).PATH.lastIndexOf("/") + 1;
					// String aa = pm056_arr.get(0).PATH.substring(num);
					//
					// path_2[count] = pm056_arr.get(i).PATH;
					// name_2[count] = aa;
					//
					// kog.e("Jonathan", "사진전송 Path :: " + pm056_arr.get(i).PATH
					// );
					//
					// count++;
					// }
					// }
					//
					//
					//
					// kog.e("Jonathan", "사진전송 TireUpload_Async 시작 ");
					// TireUpload_Async asynctask = new
					// TireUpload_Async(context) {
					// @Override
					// protected void onProgressUpdate(ArrayList<String>...
					// values) {
					// ArrayList<String> list = values[0];
					// for (int i = 0; i < list.size(); i++) {
					// kog.e("Jonathan", "사진전송 사진코드넘버들" + list.get(i));
					// }
					//
					// LISTa = list;
					// kog.e("Jonathan","타이어 신청 중 입니다.");
					// // cc.setZMO_1060_WR01(getTableZMO_1060_WR01(path, list),
					// "", "");
					// cc.setZMO_1100_WR02(MODE_NUM,
					// getTableZMO_1100_WR02(path_2, list));
					// }
					// };
					// asynctask.execute(path_2);
					// }
					// }

				} else {
					EventPopupC epc = new EventPopupC(context);
					epc.show(getString(R.string.measure_msg));
				}

			}
		});
	}

	// private ArrayList<HashMap<String, String>> getTableZMO_1030_WR06_1(
	// ArrayList<PM081> pm081_arr,
	// ArrayList<String> pic_code, String attaNo) {
	// ArrayList<HashMap<String, String>> i_itab1 = new
	// ArrayList<HashMap<String, String>>();
	// try {
	// int count = 0;
	// for (int i = 0; i < pm081_arr.size(); i++) // 리스트 갯수만큼
	// {
	// HashMap<String, String> hm = new HashMap<String, String>();
	// if (pm081_arr.get(i).PATH.equals(""))
	// continue;
	// hm.put("AUFNR", AUFNR); // 정비접수번호 조회시 나오는번호 동일
	// hm.put("SEQNO", (i + 1) + ""); // 일련번호 시퀀스 생성해서 증가
	// hm.put("GEDOCN", pm081_arr.get(i).ZCODEV); // 일반 첨부문서 문서유형 pm081
	// // 넘버
	// hm.put("ATTATCHNO", attaNo); // 첨부번호
	// hm.put("FILENO", pic_code.get(count++)); // 파일번호
	// hm.put("FILESEQ", "1"); // 파일일련번호
	// i_itab1.add(hm);
	// }
	// } catch (Exception e) {
	//
	// e.printStackTrace();
	// }
	// return i_itab1;
	// }

	private ArrayList<HashMap<String, String>> getTableZMO_1100_WR02(String[] path, ArrayList<String> pic_num) {
		ArrayList<HashMap<String, String>> i_itab1 = new ArrayList<HashMap<String, String>>();
		try {
			for (int i = 0; i < pic_num.size(); i++) // 리스트 갯수만큼
			{
				HashMap<String, String> hm = new HashMap<String, String>();
				String MODE_NUM = null;

				if (MODE.equals("A")) { // 사고'
					MODE_NUM = "1";
				} else if (MODE.equals("B")) { // 일반
					MODE_NUM = "2";
				} else if (MODE.equals("E")) { // 타이어
					MODE_NUM = "3";
				} else {
					MODE_NUM = "2";
				}

				hm.put("AUFNR", AUFNR);
				// hm.put("ACDOCN", pm081_arr_menu4.get(i).ZCODEV);

				if (MODE.equals("A")) // 사고
				{
					hm.put("ACDOCN", pm033_arr.get(i).ZCODEV);
				} else if (MODE.equals("B")) // 일반
				{
					hm.put("ACDOCN", pm056_arr.get(i).ZCODEV);
				}

				hm.put("ATTATCHNO", " "); // 첨부번호j(공백)
				hm.put("FILENO", pic_num.get(i)); // 파일번호(서버에서 나온번호)
				hm.put("FILESEQ", "1"); // 파일일련번호(파일순서)

				int num = path[i].lastIndexOf("/") + 1;
				String name = path[i].substring(num);

				hm.put("FILEPH", "/" + pic_num.get(i).substring(0, 8) + "/" + pic_num.get(i).substring(8));
				hm.put("FILEPNM", name);
				hm.put("FILELNM", name);
				File file = new File(path[i]);
				hm.put("FILESZ", "" + file.length());
				hm.put("IDFLAG", "I");

				Set<String> set = hm.keySet();
				Iterator<String> it = set.iterator();
				String key;
				while (it.hasNext()) {
					key = it.next();
					kog.e("Jonathan", "사진전송 key ===  " + key + "    value  === " + hm.get(key));
				}

				i_itab1.add(hm);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return i_itab1;
	}

	ArrayList<KDH_COMMON> mData = new ArrayList<KDH_COMMON>();;

	private void queryGroup() {
		// showProgress();
		String[] _whereArgs = { "PM055" };
		String[] _whereCause = { "ZCODEH" };

		String[] colums = { "ZCODEV", "ZCODEVT" };

		DbQueryModel dbQueryModel = new DbQueryModel(ConnectController.O_ITAB1_TABLE_NAME, _whereCause, _whereArgs,
				colums);

		DbAsyncTask dbAsyncTask = new DbAsyncTask("queryGroup", this, new DbAsyncResLintener() {

			@Override
			public void onCompleteDB(String funName, int type, Cursor cursor, String tableName) {
				// TODO Auto-generated method stub
				cursor.moveToFirst();
				if(mData == null){
					mData = new ArrayList<KDH_COMMON>();
				}
				mData.clear();
				if (cursor != null && cursor.getCount() > 0) {
					while (!cursor.isAfterLast()) {
						kog.e("KDH", "ZCODEV = " + cursor.getString(0));
						kog.e("KDH", "ZCODEVT = " + cursor.getString(1));
						KDH_COMMON _data = new KDH_COMMON();
						_data.ZCODEV = cursor.getString(0);
						_data.ZCODEVT = cursor.getString(1);
						mData.add(_data);
						cursor.moveToNext();
					}
					cursor.close();
				}

				L_C = new LinearLayout[LC_id.length];
				C_box = new ImageView[LC_id.length];
				isCBox = new boolean[LC_id.length];

//				final TextView text[] = new TextView[LC_id.length];

				final TextView text[];
				//2017.07.04. hjt 출고안내 추가
				int L_text_id[] = { R.id.textView1, R.id.textView2, R.id.textView3, R.id.textView4, R.id.textView5, R.id.textView6, R.id.textView7 };
//				int L_text_id[] = { R.id.textView1, R.id.textView2, R.id.textView3, R.id.textView4, R.id.textView5, R.id.textView6 };

				text = new TextView[L_text_id.length];

				for (int i = 0; i < L_text_id.length; i++) {
					text[i] = (TextView) findViewById(L_text_id[i]);
				}



				for (int i = 0; i < LC_id.length; i++) {
					final int getPos = i;

					L_C[i] = (LinearLayout) findViewById(LC_id[i]);
//					C_box[i] = (ImageView) L_C[i].findViewById(R.id.checkBox1);
					C_box[i] = (ImageView) L_C[i].findViewById(L_checkbox_id[i]);
					C_box[i].setBackgroundResource(R.drawable.check_off);

//					text[i] = (TextView) L_C[i].findViewById(R.id.textView1);

					if (mData != null && mData.size() > i && !TextUtils.isEmpty(mData.get(i).ZCODEVT)) {
						text[i].setText(mData.get(i).ZCODEVT);
					}

					L_C[i].setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							kog.e("KDH", "getPos = " + getPos);
							if (INFO_FAIL.equals(mData.get(getPos).ZCODEV)
									|| INFO_FAIL_CON.equals(mData.get(getPos).ZCODEV)) {
								for (int j = 0; j < mData.size(); j++) {
									if (INFO_SUCC.equals(mData.get(j).ZCODEV)) {
										if (isCBox[j]) {
											EventPopupC epc = new EventPopupC(context);
											if (INFO_FAIL.equals(mData.get(getPos).ZCODEV)) {
												epc.show(getString(R.string.info_fail));
											} else {
												epc.show(getString(R.string.info_fail_con));
											}
											return;
										}
									}
								}
							}

							if (isCBox[getPos]) {
								isCBox[getPos] = false;
								C_box[getPos].setBackgroundResource(R.drawable.check_off);
							} else {
								for (int j = 0; j < LC_id.length; j++) {
									if (L_C[j].isEnabled()) {
										isCBox[j] = false;
										C_box[j].setBackgroundResource(R.drawable.check_off);
									}
								}
								isCBox[getPos] = true;
								C_box[getPos].setBackgroundResource(R.drawable.check_on);
							}

						}
					});

					// C_box[i].setOnCheckedChangeListener(new
					// OnCheckedChangeListener() {
					//
					// @Override
					// public void onCheckedChanged(CompoundButton buttonView,
					// boolean isChecked) {
					// // TODO Auto-generated method stub
					// kog.e("KDH", isChecked + " = "+getPos);
					// for (int j = 0; j < LC_id.length; j++) {
					// C_box[j].setChecked(false);
					// }
					//
					// C_box[getPos].setChecked(!C_box[getPos].isChecked());
					//
					// }
					// });
				}
				

				Button btn_info_save = (Button) findViewById(R.id.btn_info_save);
				btn_info_save.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// ZMO_1120_WR01
						// 체크한 값을 보내준다.
						
						
//						boolean isCheck = false;
//						getSendPos = AppSt.EMPTY;
						
						ArrayList<String> arryString = new ArrayList<String>();
						
						for (int i = 0; i < LC_id.length; i++) {
							
							TextView tv = (TextView)L_C[i].getChildAt(1);
							if(L_C[i].isEnabled() && isCBox[i])
							{
								arryString.add(tv.getText().toString());
							}
							
							
						}
						
						
						
						if(arryString.contains("조치불가"))
						{
							final EventPopupCC ep1 = new EventPopupCC(context, "조치불가를 저장하시겠습니까?");
							Button bt_yes = (Button) ep1.findViewById(R.id.btn_ok);
							bt_yes.setOnClickListener(new OnClickListener() {
								@Override
								public void onClick(View arg0) {
									ep1.dismiss();
									
									
									boolean isCheck = false;
									getSendPos = AppSt.EMPTY;
									for (int i = 0; i < LC_id.length; i++) {

										if (L_C[i].isEnabled() && isCBox[i]) {
											getSendPos = i;
											isCheck = true;
											kog.e("KDH", "btn_info_save = " + i);
											cc.getZMO_1120_WR01(AUFNR, mData.get(i).ZCODEV);
											pp.show();
											break;
										}
									}
									if (!isCheck) {
										EventPopupC epc = new EventPopupC(context);
										epc.show(getString(R.string.info_msg));
									}
									
									
								}
							});
							Button bt_no = (Button) ep1.findViewById(R.id.btn_cancel);
							bt_no.setOnClickListener(new OnClickListener() {
								@Override
								public void onClick(View arg0) {
									ep1.dismiss();
								}
							});
							ep1.show();
						}
						else
						{
							
							boolean isCheck = false;
							getSendPos = AppSt.EMPTY;
							for (int i = 0; i < LC_id.length; i++) {

								if (L_C[i].isEnabled() && isCBox[i]) {
									getSendPos = i;
									isCheck = true;
									kog.e("KDH", "btn_info_save = " + i);
									cc.getZMO_1120_WR01(AUFNR, mData.get(i).ZCODEV);
									pp.show();
									break;
								}
							}
							if (!isCheck) {
								EventPopupC epc = new EventPopupC(context);
								epc.show(getString(R.string.info_msg));
							}
							
						}
						
						
						
						
						
						
//						for (int i = 0; i < LC_id.length; i++) {
//
//							if (L_C[i].isEnabled() && isCBox[i]) {
//								getSendPos = i;
//								isCheck = true;
//								kog.e("KDH", "btn_info_save = " + i);
//								cc.getZMO_1120_WR01(AUFNR, mData.get(i).ZCODEV);
//								pp.show();
//								break;
//							}
//						}
//						if (!isCheck) {
//							EventPopupC epc = new EventPopupC(context);
//							epc.show(getString(R.string.info_msg));
//						}

						

					}
				});
				// 2014-02-13 KDH 미조치는 기본이다.
				L_C[0].performClick();
				L_C[0].setEnabled(false);
				C_box[0].setBackgroundResource(R.drawable.check_dis);

				if (TOTAL_O_ITAB1 != null && TOTAL_O_ITAB1.size() > 0) {
					for (int i = 0; i < TOTAL_O_ITAB1.size(); i++) {
						String AUFNR = TOTAL_O_ITAB1.get(i).get("AUFNR");
						String SEQNO = TOTAL_O_ITAB1.get(i).get("SEQNO");
						String SNDCUS = TOTAL_O_ITAB1.get(i).get("SNDCUS");
						String SNDCUSNM = TOTAL_O_ITAB1.get(i).get("SNDCUSNM");

						kog.e("KDH", "AUFNR = " + AUFNR + "\n  SEQNO = " + SEQNO + "\n  SNDCUS = " + SNDCUS
								+ " \n SNDCUSNM = " + SNDCUSNM);
						if (SNDCUS_X.equalsIgnoreCase(SNDCUS)) {
							int num = AppSt.EMPTY;
							try {
								num = Integer.parseInt(SEQNO);
							} catch (Exception e) {
							}

							if (num > AppSt.EMPTY) {
								for (int j = 0; j < mData.size(); j++) {
									int get = AppSt.EMPTY;
									try {
										get = Integer.parseInt(mData.get(j).ZCODEV);
									} catch (Exception e) {
									}

									if (get > AppSt.EMPTY) {
										if (num == get) {
											// 2014-02-13 KDH 데이터떨궈지면 이미 설정된
											// 값이므로 무조건 선택안되야
											L_C[j].setEnabled(false);

											isCBox[j] = true;
											C_box[j].setBackgroundResource(R.drawable.check_dis);
										}
									}
								}
							}
						}
					}

				}
			}
		}, dbQueryModel);
		dbAsyncTask.execute(DbAsyncTask.DB_SELECT);
	}

	private void initView() {
		mSendSmsCount = 0;
		bt_customer = (Button) findViewById(R.id.menu4_top_bt_customer_id);
		bt_customer.setOnClickListener(this);
		bt_tire = (Button) findViewById(R.id.menu4_top_bt_tire_id);
		bt_tire.setOnClickListener(this);
		bt_history = (Button) findViewById(R.id.menu4_top_bt_history_id);
		bt_history.setOnClickListener(this);

		back[0] = (LinearLayout) findViewById(R.id.menu4_tab1_layout);
		back[1] = (LinearLayout) findViewById(R.id.menu4_tab2_layout);
		back[2] = (LinearLayout) findViewById(R.id.menu4_tab3_layout);
		// 2013.12.03 ypkim
		back[3] = (LinearLayout) findViewById(R.id.menu4_tab4_layout);

		//////////////////////////////////////////////
		ll_driver = (LinearLayout) findViewById(R.id.ll_driver_set);
		//////////////////////////////////////////////

		menu4_top2_id6_1 = (TextView) findViewById(R.id.menu4_top2_id6_1);
		menu4_top2_id7_1 = (TextView) findViewById(R.id.menu4_top2_id7_1);

		// 공통
		tv_top1_id1 = (TextView) findViewById(R.id.menu4_top1_id1);
		// tv_top1_id2 = (TextView) findViewById(R.id.menu4_top1_id2);
		tv_top1_id3 = (TextView) findViewById(R.id.menu4_top1_id3);
		tv_top1_id4 = (TextView) findViewById(R.id.menu4_top1_id4); // 14.11.10
																	// Jonathan
																	// 정비접수번호
																	// 추가.
		tv_top2_id1 = (TextView) findViewById(R.id.menu4_top2_id1);
		tv_top2_id2 = (TextView) findViewById(R.id.menu4_top2_id2);
		tv_top2_id3 = (TextView) findViewById(R.id.menu4_top2_id3);
		tv_top2_id4 = (TextView) findViewById(R.id.menu4_top2_id4);
		tv_top2_id5 = (TextView) findViewById(R.id.menu4_top2_id5);
		tv_top2_id6 = (TextView) findViewById(R.id.menu4_top2_id6);
		tv_top2_id7 = (TextView) findViewById(R.id.menu4_top2_id7);

		Layout_bejung_suheng = (LinearLayout) findViewById(R.id.bejon_suheng);

		// 사고
		tv_accident_line1_id1 = (TextView) findViewById(R.id.menu4_accident_line1_id1);
		tv_accident_line1_id2 = (TextView) findViewById(R.id.menu4_accident_line1_id2);
		tv_accident_line1_id3 = (TextView) findViewById(R.id.menu4_accident_line1_id3);
		tv_accident_line2_id1 = (TextView) findViewById(R.id.menu4_accident_line2_id1);
		tv_accident_line2_id2 = (TextView) findViewById(R.id.menu4_accident_line2_id2);
		tv_accident_line2_id2.setOnClickListener(this);
		tv_accident_line2_id3 = (TextView) findViewById(R.id.menu4_accident_line2_id3);
		tv_accident_line2_id3.setOnClickListener(this);
		tv_accident_line3_id1 = (TextView) findViewById(R.id.menu4_accident_line3_id1);
		tv_accident_line3_id2 = (TextView) findViewById(R.id.menu4_accident_line3_id2);
		tv_accident_line3_id3 = (TextView) findViewById(R.id.menu4_accident_line3_id3);
		tv_accident_line4_id1 = (TextView) findViewById(R.id.menu4_accident_line4_id1);
		tv_accident_line4_id1.setOnClickListener(this);
		tv_accident_line5_id1 = (TextView) findViewById(R.id.menu4_accident_line5_id1);
		tv_accident_line5_id2 = (TextView) findViewById(R.id.menu4_accident_line5_id2);
		tv_accident_line5_id2.setOnClickListener(this);
		tv_accident_line6_id1 = (TextView) findViewById(R.id.menu4_accident_line6_id1);
		tv_accident_line6_id1.setOnClickListener(this);
		tv_accident_line7_id1 = (TextView) findViewById(R.id.menu4_accident_line7_id1);
		tv_accident_line7_id1.setOnClickListener(this);
		tv_accident_line8_id1 = (TextView) findViewById(R.id.menu4_accident_line8_id1);
		tv_accident_line8_id1.setOnClickListener(this);
		tv_accident_line8_id2 = (TextView) findViewById(R.id.menu4_accident_line8_id2);
		tv_accident_line9_id1 = (TextView) findViewById(R.id.menu4_accident_line9_id1);
		tv_accident_line9_id2 = (TextView) findViewById(R.id.menu4_accident_line9_id2);
		tv_accident_line9_id3 = (TextView) findViewById(R.id.menu4_accident_line9_id3);
		tv_accident_line9_id4 = (TextView) findViewById(R.id.menu4_accident_line9_id4);
		bt_accident_done1 = (Button) findViewById(R.id.menu4_accident_done_id1);
		bt_accident_done1.setOnClickListener(this);
		bt_accident_done2 = (Button) findViewById(R.id.menu4_accident_done_id2);
		bt_accident_done2.setOnClickListener(this);
		// myung 20131220 ADD 차량운행내역 버튼 상입
		bt_accident_done3 = (Button) findViewById(R.id.menu4_accident_done_id3);
		bt_accident_done3.setOnClickListener(this);

		bt_simple_end = (Button) findViewById(R.id.simple_end);
		bt_simple_end.setOnClickListener(this);

		bt_accident_insure_call = (Button)findViewById(R.id.call_insure);
		tv_accident_insure_sp_name = (TextView)findViewById(R.id.sp_name);
		tv_accident_insure_sp_number = (TextView)findViewById(R.id.sp_number);

		bt_accident_insure_call.setVisibility(View.INVISIBLE);
		tv_accident_insure_sp_name.setVisibility(View.INVISIBLE);
		tv_accident_insure_sp_number.setVisibility(View.INVISIBLE);

		bt_accident_insure_call.setOnClickListener(this);

		// 일반
		tv_normal_line1_id1 = (TextView) findViewById(R.id.menu4_normal_line1_id1);
		tv_normal_line1_id2 = (TextView) findViewById(R.id.menu4_normal_line1_id2);
		tv_normal_line1_id2.setOnClickListener(this);
		tv_normal_line2_id1 = (TextView) findViewById(R.id.menu4_normal_line2_id1);
		tv_normal_line2_id1.setOnClickListener(this);
		tv_normal_line3_id1 = (TextView) findViewById(R.id.menu4_normal_line3_id1);
		tv_normal_line3_id1.setOnClickListener(this);
		tv_normal_line4_id1 = (TextView) findViewById(R.id.menu4_normal_line4_id1);
		tv_normal_line4_id1.setOnClickListener(this);
		tv_normal_line5_id1 = (EditText) findViewById(R.id.menu4_normal_line5_id1);
		tv_normal_line5_id1.setOnClickListener(this);
		tv_normal_line6_id1 = (TextView) findViewById(R.id.menu4_normal_line6_id1);
		tv_normal_line6_id1.setOnClickListener(this);
		tv_normal_line7_id1 = (TextView) findViewById(R.id.menu4_normal_line7_id1);
		tv_normal_line7_id2 = (TextView) findViewById(R.id.menu4_normal_line7_id2);
		tv_normal_line7_id3 = (TextView) findViewById(R.id.menu4_normal_line7_id3);
		tv_normal_line7_id4 = (TextView) findViewById(R.id.menu4_normal_line7_id4);
		bt_normal_done1 = (Button) findViewById(R.id.menu4_normal_done_id1);
		bt_normal_done1.setOnClickListener(this);
		bt_normal_done2 = (Button) findViewById(R.id.menu4_normal_done_id2);
		bt_normal_done2.setOnClickListener(this);
		// myung 20131220 ADD 차량운행내역 버튼 상입
		bt_normal_done3 = (Button) findViewById(R.id.menu4_normal_done_id3);
		bt_normal_done3.setOnClickListener(this);

		// Jonathan 150309
		menu4_tire_add_id = (Button) findViewById(R.id.menu4_tire_add_id);
		menu4_tire_delete_id = (Button) findViewById(R.id.menu4_tire_delete_id);
		menu4_tire_search_bon_id = (TextView) findViewById(R.id.menu4_tire_search_bon_id);

		menu4_tire_add_id_01 = (Button) findViewById(R.id.menu4_tire_add_id_01);
		menu4_tire_delete_id_01 = (Button) findViewById(R.id.menu4_tire_delete_id_01);
		menu4_tire_search_bon_id_01 = (TextView) findViewById(R.id.menu4_tire_search_bon_id_01);

		// Jonathan 150414
		menu4_call_time1 = (TextView) findViewById(R.id.menu4_call_time1);
		menu4_call_time2 = (TextView) findViewById(R.id.menu4_call_time2);
		menu4_call_time3 = (TextView) findViewById(R.id.menu4_call_time3);

		menu4_tire_add_id.setOnClickListener(this);
		menu4_tire_delete_id.setOnClickListener(this);
		menu4_tire_list_id = (ListView) findViewById(R.id.menu4_tire_list_id);
		menu4_sv = (ScrollView) findViewById(R.id.menu4_sv);
		menu4_tire_list_id.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				menu4_sv.requestDisallowInterceptTouchEvent(true);
				return false;
			}
		});

		menu4_tire_add_id_01.setOnClickListener(this);
		menu4_tire_delete_id_01.setOnClickListener(this);
		menu4_tire_list_id_01 = (ListView) findViewById(R.id.menu4_tire_list_id_01);
		menu4_sv = (ScrollView) findViewById(R.id.menu4_sv);
		menu4_tire_list_id_01.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				menu4_sv.requestDisallowInterceptTouchEvent(true);
				return false;
			}
		});

		// pm081_arr_menu4 = new ArrayList<PM081>();
		// ta = new Menu4_Tire_Adapter(context, R.layout.tire_row,
		// pm081_arr_menu4);
		// menu4_tire_list_id.setAdapter(ta);
		// ta.notifyDataSetChanged();

		// 긴급
		tv_emergency_line1_id1 = (TextView) findViewById(R.id.menu4_emergency_line1_id1);
		tv_emergency_line1_id2 = (TextView) findViewById(R.id.menu4_emergency_line1_id2);
		tv_emergency_line2_id1 = (TextView) findViewById(R.id.menu4_emergency_line2_id1);
		tv_emergency_line2_id2 = (TextView) findViewById(R.id.menu4_emergency_line2_id2);
		tv_emergency_line3_id1 = (TextView) findViewById(R.id.menu4_emergency_line3_id1);
		tv_emergency_line3_id2 = (TextView) findViewById(R.id.menu4_emergency_line3_id2);
		iv_emergency_line4_id1 = (ImageView) findViewById(R.id.menu4_emergency_line4_id1);
		iv_emergency_line4_id2 = (ImageView) findViewById(R.id.menu4_emergency_line4_id2);
		iv_emergency_line4_id3 = (ImageView) findViewById(R.id.menu4_emergency_line4_id3);
		iv_emergency_line4_id4 = (ImageView) findViewById(R.id.menu4_emergency_line4_id4);
		iv_emergency_line5_id1 = (ImageView) findViewById(R.id.menu4_emergency_line5_id1);
		iv_emergency_line5_id2 = (ImageView) findViewById(R.id.menu4_emergency_line5_id2);
		iv_emergency_line5_id3 = (ImageView) findViewById(R.id.menu4_emergency_line5_id3);
		iv_emergency_line5_id4 = (ImageView) findViewById(R.id.menu4_emergency_line5_id4);
		tv_emergency_line6_id1 = (TextView) findViewById(R.id.menu4_emergency_line6_id1);
		tv_emergency_line7_id1 = (TextView) findViewById(R.id.menu4_emergency_line7_id1);
		tv_emergency_line8_id1 = (TextView) findViewById(R.id.menu4_emergency_line8_id1);
		tv_emergency_line8_id2 = (TextView) findViewById(R.id.menu4_emergency_line8_id2);
		tv_emergency_line8_id3 = (TextView) findViewById(R.id.menu4_emergency_line8_id3);
		bt_emergency_done1 = (Button) findViewById(R.id.menu4_emergency_done_id1);
		bt_emergency_done1.setOnClickListener(this);
		bt_emergency_done2 = (Button) findViewById(R.id.menu4_emergency_done_id2);
		bt_emergency_done2.setOnClickListener(this);

		// 2013.12.03 ypkim add 타이어
		tv_tire_line1_id1 = (TextView) findViewById(R.id.menu4_tire_line1_id1);
		tv_tire_line1_id2 = (TextView) findViewById(R.id.menu4_tire_line1_id2);
		tv_tire_line1_id3 = (TextView) findViewById(R.id.menu4_tire_line1_id3);

		tv_tire_line2_id1 = (TextView) findViewById(R.id.menu4_tire_line2_id1);
		tv_tire_line2_id2 = (TextView) findViewById(R.id.menu4_tire_line2_id2);

		tv_tire_line3_id1 = (TextView) findViewById(R.id.menu4_tire_line3_id1);

		tv_tire_line4_id1 = (TextView) findViewById(R.id.menu4_tire_line4_id1);
		tv_tire_line4_id2 = (TextView) findViewById(R.id.menu4_tire_line4_id2);

		tv_tire_line5_id1 = (TextView) findViewById(R.id.menu4_tire_line5_id1);

		tv_tire_line6_id1 = (TextView) findViewById(R.id.menu4_tire_line6_id1);
		tv_tire_line7_id1 = (EditText) findViewById(R.id.menu4_tire_line7_id1);
		tv_tire_line8_id1 = (EditText) findViewById(R.id.menu4_tire_line8_id1);

		tv_tire_line9_id1 = (TextView) findViewById(R.id.menu4_tire_line9_id1);
		tv_tire_line9_id2 = (TextView) findViewById(R.id.menu4_tire_line9_id2);
		tv_tire_line9_id3 = (TextView) findViewById(R.id.menu4_tire_line9_id3);
		tv_tire_line9_id4 = (TextView) findViewById(R.id.menu4_tire_line9_id4);

		bt_tire_done1 = (Button) findViewById(R.id.menu4_tire_done_id1);
		bt_tire_done1.setOnClickListener(this);
		bt_tire_done2 = (Button) findViewById(R.id.menu4_tire_done_id2);
		bt_tire_done2.setOnClickListener(this);

		bt_tire_done3 = (Button) findViewById(R.id.menu4_tire_done_id3);
		bt_tire_done3.setOnClickListener(this);

		bt_call = (Button) findViewById(R.id.customer_call_id);
		bt_call.setOnClickListener(this);

		bt_custom_call = (Button) findViewById(R.id.driver_call_id);
		bt_custom_call.setOnClickListener(this);

		// 2014-03-25 KDH 추가됨.
		Button btn_check = (Button) findViewById(R.id.menu4_check_list);
		/*
		 * btn_check.setVisibility(View.GONE); if("B".equals(MODE)) {
		 * btn_check.setVisibility(View.VISIBLE); }
		 */

		btn_check.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				KDH_check_list mKdhCheck = new KDH_check_list(Menu2_1_Activity.this, AUFNR);
				mKdhCheck.show();
			}
		});
		
		

		///20160831 Jonathan
		llCheckReceive = (LinearLayout)findViewById(R.id.llCheckReceive);
		ivCheckReceive = (ImageView)findViewById(R.id.ivCheckReceive);
		ivCheckReceive.setOnClickListener(this);
		tvCheckReceive = (TextView)findViewById(R.id.tvCheckReceive);
		tvCheckReceiveTime = (TextView)findViewById(R.id.tvCheckReceiveTime);
		


	}

	// HashMap<String, String> O_STRUDCT1;
	// HashMap<String, String> O_STRUDCT2;
	// HashMap<String, String> O_STRUDCT3;
	// HashMap<String, String> O_STRUDCT4;
	// HashMap<String, String> O_STRUDCT5;
	// ArrayList<HashMap<String, String>> O_ITAB1;

	@Override
	public void connectResponse(String FuntionName, String resultText, String MTYPE, int resulCode,
			TableModel tableModel) {
		 Log.i("#", "####" + FuntionName + "/" + resultText + "/" + MTYPE +
		 "/"
		 + resulCode);
		// pp.hide();
		// pp.setMessage("조회 중 입니다.");

		// myung 20131118 ADD 긴급에서는 배정자/수행자 미출력

		// kog.e("Jonathan", " FuntionName :: Menu2_1 :: " + FuntionName + "
		// tableModel :: " + tableModel.getTableName() + " Value :: " +
		// tableModel.getValue() );
		// kog.e("Jonathan", " Menu2_1 :: FuntionName :: " + FuntionName + "
		// reultText :: " + resultText + " MTYPE :: " + MTYPE + " resulCode :: "
		// + resulCode);

		if (MODE.equals("C"))
			Layout_bejung_suheng.setVisibility(View.GONE);
		else
			Layout_bejung_suheng.setVisibility(View.VISIBLE);

		if (MTYPE == null || !MTYPE.equals("S")) {
			
			cc.duplicateLogin(context);

			if(pp != null)
				pp.hide();

			EventPopupC epc = new EventPopupC(this);
			epc.show(resultText);

			if(FuntionName.equals("ZSD_SEND_SMS_STR")){
				cc.getZPM_0087_007(O_STRUDCT1.get("AUFNR"), "X");
				bt_accident_insure_call.setClickable(true);
				bt_accident_insure_call.setAlpha(1.0f);
			}
			return;
		}

		if (FuntionName.equals("ZMO_1100_RD05")) {
			O_STRUDCT1 = tableModel.getStruct("O_STRUCT1");
			O_STRUDCT2 = tableModel.getStruct("O_STRUCT2");
			O_STRUDCT3 = tableModel.getStruct("O_STRUCT3");
			O_STRUDCT4 = tableModel.getStruct("O_STRUCT4");
			O_STRUDCT5 = tableModel.getStruct("O_STRUCT5");
			// Jonathan 14.06.23 사고 , 일반, 타이어 로 구분 해야 한다.

			Set<String> set;
			Iterator<String> it;
			String key;

			set = O_STRUDCT1.keySet();
			it = set.iterator();

			while (it.hasNext()) {
				key = it.next();
				kog.e("Jonathan", "Menu2_1 o_struct1 key ===  " + key + "    value  === " + O_STRUDCT1.get(key));
			}

			tv_top2_id6.setText(O_STRUDCT1.get("DRVNAM")); // 운전자명
			tv_top2_id7.setText(O_STRUDCT1.get("DRVHP")); // 운전자연락처

			String O_REQDES = tableModel.getResponse("O_REQDES");
			setCommon();
			setAccident(O_REQDES);
			// myung 20131220 UPDATE 차량운행내역 버튼 상입
			// buttonChange();
			// setbuttonEnable(bt_accident_done1);
			TOTAL_O_ITAB1 = tableModel.getTableArray("O_ITAB");
			O_FTAB1 = tableModel.getTableArray("O_FTAB");

			if(TOTAL_O_ITAB1 == null){
				TOTAL_O_ITAB1 = tableModel.getTableArray();
			}
			if(O_FTAB1 != null && O_FTAB1.size() > 0){
				for(int i=0; i<O_FTAB1.size(); i++){
					PM033 pm033 = new PM033();
					pm033.ZCODEV = O_FTAB1.get(i).get("GEDOCN");
					pm033.ZCODEVT = O_FTAB1.get(i).get("GEDOCNM");
					pm033.PATH = O_FTAB1.get(i).get("FILEPH");
					pm033_arr_prev.add(pm033);
				}
			}


			queryGroup();

			if(pp != null)
				pp.hide();
			if(pm033_arr_prev != null && pm033_arr_prev.size() > 0) {
				ta_033 = new Menu4_Tire_Adapter_PM033(context, R.layout.tire_row, pm033_arr_prev);
				menu4_tire_list_id.setAdapter(ta_033);
			}
			if(O_STRUDCT1.get("SPLIFNRNM") != null && O_STRUDCT1.get("SPLIFNRNM").length() > 2){
				if(O_STRUDCT1.get("SPSMSFYN") != null && O_STRUDCT1.get("SPSMSFYN").equals("X")){
					bt_accident_insure_call.setClickable(true);
				} else {
					bt_accident_insure_call.setAlpha(0.5f);
					bt_accident_insure_call.setClickable(false);
				}
				tv_accident_insure_sp_name.setText(O_STRUDCT1.get("SPLIFNRNM"));
				tv_accident_insure_sp_number.setText(O_STRUDCT1.get("SPTEL"));
			}
			cc.duplicateLogin(context);
		}else if (FuntionName.equals("ZPM_0087_001")) {
//            cc.duplicateLogin(context);
			HashMap<String, String> ES_DAM01 = new HashMap<>();
			HashMap<String, String> ES_RESULT = new HashMap<>();
			if(tableModel == null){
				return;
			}

			ES_DAM01 = tableModel.getStruct("ES_DAM01");
			ES_RESULT = tableModel.getStruct("ES_RESULT");
			if(ES_DAM01 != null){
				dam01nm = ES_DAM01.get("DAM01NM");
				telf1 = ES_DAM01.get("TELF1");
				lifnr = ES_RESULT.get("LIFNR");
				lifnrNm = ES_DAM01.get("LIFNRNM");
			}
			if(lifnrNm != null){
				if(tv_accident_insure_sp_name != null){
					tv_accident_insure_sp_name.setText(lifnrNm);
				}
			}
			if(telf1 != null){
				if(tv_accident_insure_sp_number != null){
					tv_accident_insure_sp_number.setText(telf1);
				}
			}
			cc.getZPM_0087_002(lifnr);
//			showSMSPopup();
//            sendSMS();
            if(tv_accident_insure_sp_name != null){
                tv_accident_insure_sp_name.setText(dam01nm);
            }
            if(tv_accident_insure_sp_number != null){
                tv_accident_insure_sp_number.setText(telf1);
                bt_accident_insure_call.setClickable(false);
            }
		} else if (FuntionName.equals("ZPM_0087_002")) {
//			showSMSPopup();
			mSendSmsCount++;
            sendSMS();
		} else if (FuntionName.equals("ZSD_SEND_SMS_STR")) {
//			showSMSPopup();

			if(mSendSmsCount > 0){
				mSendSmsCount--;
				sendSMS();
			} else {
				EventPopupC epc = new EventPopupC(context);
				epc.show(resultText);
				cc.getZPM_0087_007(O_STRUDCT1.get("AUFNR"), " ");
				bt_accident_insure_call.setAlpha(0.5f);
				bt_accident_insure_call.setClickable(false);
				if(pp != null){
					pp.hide();
				}
//				cc.duplicateLogin(context);
			}
		} else if (FuntionName.equals("ZMO_1100_RD06")) {
			O_STRUDCT1 = tableModel.getStruct("O_STRUCT1");
			O_STRUDCT2 = tableModel.getStruct("O_STRUCT2");
			O_STRUDCT3 = tableModel.getStruct("O_STRUCT3");
			O_STRUDCT4 = tableModel.getStruct("O_STRUCT4");
			O_STRUDCT5 = tableModel.getStruct("O_STRUCT5");
			String O_ACCDES = tableModel.getResponse("O_ACCDES");
			String O_INTCONT = tableModel.getResponse("O_INTCONT");
			String O_CUSCONT = tableModel.getResponse("O_CUSCONT");
			String O_VENCONT = tableModel.getResponse("O_VENCONT");
			// Jonathan 14.06.23 사고 , 일반, 타이어 로 구분 해야 한다.
			menu4_top2_id6_1.setText("요청자명");
			menu4_top2_id7_1.setText("요청자연락처");
			tv_top2_id6.setText(O_STRUDCT1.get("REQNAM")); // 일반 요청자명
			tv_top2_id7.setText(O_STRUDCT1.get("REQHP")); // 일반 요청자 연락처

			setCommon();
			setNormal(O_ACCDES, O_INTCONT, O_CUSCONT, O_VENCONT);
			// myung 20131220 UPDATE 차량운행내역 버튼 상입
			// buttonChange();
			// setbuttonEnable(bt_normal_done1);
			TOTAL_O_ITAB1 = tableModel.getTableArray("O_ITAB");
			O_FTAB1 = tableModel.getTableArray("O_FTAB");
			if(TOTAL_O_ITAB1 == null){
				TOTAL_O_ITAB1 = tableModel.getTableArray();
			}
			if(O_FTAB1 != null && O_FTAB1.size() > 0){
				for(int i=0; i<O_FTAB1.size(); i++){
					PM056 pm056 = new PM056();
					pm056.ZCODEV = O_FTAB1.get(i).get("GEDOCN");
					pm056.ZCODEVT = O_FTAB1.get(i).get("GEDOCNM");
					pm056.PATH = O_FTAB1.get(i).get("FILEPH");
					pm056_arr_prev.add(pm056);
				}
			}
//			kog.e("KDH", "TOTAL_O_ITAB1 = " + TOTAL_O_ITAB1.size());
			queryGroup();

			recall_check(O_STRUDCT1);

			if (pp != null)
				pp.hide();
			if(pm056_arr_prev != null && pm056_arr_prev.size() > 0) {
				ta_056 = new Menu4_Tire_Adapter_PM056(context, R.layout.tire_row, pm056_arr_prev);
				menu4_tire_list_id_01.setAdapter(ta_056);
			}
			cc.duplicateLogin(context);
		} else if (FuntionName.equals("ZMO_1100_RD04")) {
			O_STRUDCT1 = tableModel.getStruct("O_STRUCT1");
			O_STRUDCT2 = tableModel.getStruct("O_STRUCT2");
			O_STRUDCT3 = tableModel.getStruct("O_STRUCT3");
			O_STRUDCT4 = tableModel.getStruct("O_STRUCT4");
			O_STRUDCT5 = tableModel.getStruct("O_STRUCT5");

			String O_EMRCONT = tableModel.getResponse("O_EMRCONT");
			setCommon();
			setEmergency(O_EMRCONT);

			buttonChange();
			TOTAL_O_ITAB1 = tableModel.getTableArray("O_ITAB");
			if(TOTAL_O_ITAB1 == null || TOTAL_O_ITAB1.size() == 0){
				TOTAL_O_ITAB1 = tableModel.getTableArray();
			}
			queryGroup();

			if (pp != null)
				pp.hide();

			cc.duplicateLogin(context);

		} else if (FuntionName.equals("ZMO_1030_RD08")) { // 2013.12.03 ypkim
			// structure 순서 중요
			// 함.

			O_STRUDCT1 = tableModel.getStruct("O_STRUCT1");
			O_STRUDCT2 = tableModel.getStruct("O_STRUCT2");
			O_STRUDCT4 = tableModel.getStruct("O_STRUCT3");
			O_STRUDCT5 = tableModel.getStruct("O_STRUCT4");


			if ("배송".equals(O_STRUDCT1.get("SHIPGBNM"))) {
				if ("Y".equals(tableModel.getStruct("OS_TIREREG").get("O_CHECKYN"))) {
					llCheckReceive.setVisibility(View.VISIBLE);
					ivCheckReceive.setBackgroundResource(R.drawable.check_dis);
					ivCheckReceive.setClickable(false);
					tvCheckReceiveTime.setText(tableModel.getStruct("OS_TIREREG").get("O_CHECKFORM"));

				} else {
					llCheckReceive.setVisibility(View.VISIBLE);
					ivCheckReceive.setBackgroundResource(R.drawable.check_off);
				}
			} else {
				llCheckReceive.setVisibility(View.GONE);
			}


			String O_TRCUSR = tableModel.getResponse("O_TRCUSR"); // 고객요청사항(TRCUSR)
			String O_TRSPEC = tableModel.getResponse("O_TRSPEC"); // 특이사항(TRSPEC)
			// Jonathan 14.06.23 사고 , 일반, 타이어 로 구분 해야 한다.
			tv_top2_id6.setText(O_STRUDCT1.get("DRVNAM")); // 운전자명
			tv_top2_id7.setText(O_STRUDCT1.get("CUSTEL")); // 운전자연락처

			TOTAL_O_ITAB1 = tableModel.getTableArray("O_ITAB");
			if(TOTAL_O_ITAB1 == null || TOTAL_O_ITAB1.size() == 0){
				TOTAL_O_ITAB1 = tableModel.getTableArray();
			}
			mCusReq = O_TRCUSR;
			setCommon();
			setTire(O_TRCUSR, O_TRSPEC);

			buttonChange();
			queryGroup();

			if (pp != null)
				pp.hide();

			cc.duplicateLogin(context);
		} else if (FuntionName.equals("ZMO_3140_RD01")) // 상세조회
		{
			ArrayList<HashMap<String, String>> o_itab1 = tableModel.getTableArray("O_ITAB1");
			ArrayList<HashMap<String, String>> o_itab2 = tableModel.getTableArray("O_ITAB2");
			ArrayList<HashMap<String, String>> o_itab3 = tableModel.getTableArray("O_ITAB3");
			ArrayList<HashMap<String, String>> o_itab4 = tableModel.getTableArray("O_ITAB4");
			// Log.i("####", "####" + "상세조회" + o_itab1.size());
			// Log.i("####", "####" + "상세조회" + o_itab2.size());
			// Log.i("####", "####" + "상세조회" + o_itab3.size());
			// Log.i("####", "####" + "상세조회" + o_itab4.size());
			HashMap<String, String> o_struct1 = tableModel.getStruct("O_STRUCT1");

			if (o_struct1.get("MODIYN").equals("A")) {
				// Log.i("####", "####" + "출발등록");
				setStart(o_struct1);
			} else {
				// Log.i("####", "####" + "도착등록");
				setEnd(o_struct1);
			}
			if (pp != null)
				pp.hide();

			cc.duplicateLogin(context);
		} else if (FuntionName.equals("ZMO_3140_WR01") || FuntionName.equals("ZMO_3140_WR02")) // 출발도착등록
		{
			// Log.i("####", "####" + "출발 도착등록 완료");
			if (pp != null)
				pp.hide();

			cc.duplicateLogin(context);

		} else if ("ZMO_1100_WR01".equals(FuntionName) || "ZMO_1120_WR01".equals(FuntionName)) {

			// Jonathan 150406
			// attatNo = tableModel.getResponse("O_ATTATCHNO");
			// kog.e("Jonathan", "사진전송 attatNo :; " + attatNo);

			if (getSendPos > AppSt.EMPTY) {
				L_C[getSendPos].setEnabled(false);
				isCBox[getSendPos] = true;
				C_box[getSendPos].setBackgroundResource(R.drawable.check_dis);
			}

			EventPopupC epc = new EventPopupC(context);
			epc.show(resultText);
			// SAP 에서 주는 결과로 보여주기!
//			epc.show(getString(R.string.reg_succ));

			if (pp != null)
				pp.hide();

			cc.duplicateLogin(context);


		} else if (FuntionName.equals("ZMO_1060_WR01")) {
			// Toast.makeText(context, tableModel.getResponse("O_ATTATCHNO")+"",
			// 0).show();
			String attatNo = tableModel.getResponse("O_ATTATCHNO");

			kog.e("Jonathan", "다시 돌아온 ZMO_1060_WR01");

			// 타이어신청
			// ArrayList<HashMap<String, String>> temp_hm2 =
			// getTableZMO_1030_WR06_1(
			// pm081_arr_menu4, LISTa, attatNo);
			// if (temp_hm2.size() <= 0) {
			// EventPopupC epc = new EventPopupC(context);
			// epc.show("사진이 전송되지 않았습니다. \n네트워크 상태를 확인하세요.");
			// return;
			// }

			cc.duplicateLogin(context);

		} else if (FuntionName.equals("ZMO_1030_WR07")) {
			// structure 순서 중요 함.
			kog.e("Jonathan", "Jonathan OS_TIREREG.O_CHECKYN::  " + tableModel.getStruct("OS_TIREREG").get("O_CHECKYN"));

			if ("Y".equals(tableModel.getStruct("OS_TIREREG").get("O_CHECKYN"))) {
				llCheckReceive.setVisibility(View.VISIBLE);
				ivCheckReceive.setBackgroundResource(R.drawable.check_dis);
				ivCheckReceive.setClickable(false);
				tvCheckReceiveTime.setText(tableModel.getStruct("OS_TIREREG").get("O_CHECKFORM"));

			} else {
				llCheckReceive.setVisibility(View.VISIBLE);
				ivCheckReceive.setBackgroundResource(R.drawable.check_off);
			}

			if (pp != null)
				pp.hide();

			cc.duplicateLogin(context);
		} else if (FuntionName.equals("ZMO_1100_WR03")) {
			// structure 순서 중요 함.
			kog.e("Jonathan", "Jonathan ZMO_1100_WR03 11");

			if (pp != null)
				pp.hide();

			cc.duplicateLogin(context);

		} else if (FuntionName.equals("ZMO_1100_WR04")) {
			// structure 순서 중요 함.
			kog.e("Jonathan", "Jonathan ZMO_1100_WR04 11");

			if (pp != null)
				pp.hide();

			if (cc != null) {
				cc.getZMO_1100_RD05(AUFNR);
				if (menu4_sv != null) {
					menu4_sv.fullScroll(ScrollView.FOCUS_UP);
				}
			} else {
				Log.d("hjt", "cc == null");
			}
			Log.d("hjt", "hjt resultText = " + resultText);
			EventPopupC epc = new EventPopupC(this);
			epc.show(resultText);

		} else {
			if (pp != null)
				pp.hide();

		}
	}

	private void buttonChange() {
		Object data = O_STRUDCT1.get("RESVNO");
		if (data == null || data.toString().equals("") || data.toString().equals(" ")) {
			// Log.i("####", "####" + "차량운행내역");
			bt_accident_done1.setText("차량운행내역");
			bt_normal_done1.setText("차량운행내역");
		} else {
			// Log.i("####", "####" + "출발/도착등록");
			bt_accident_done1.setText("출발/도착등록");
			bt_normal_done1.setText("출발/도착등록");
		}

	}

	// myung 20131220 ADD 차량운행내역 버튼 상입
	private void setbuttonEnable(Button v) {
		Object data = O_STRUDCT1.get("RESVNO");
		if (data == null || data.toString().equals("") || data.toString().equals(" ")) {
			v.setEnabled(false);
		} else {
			v.setEnabled(true);
		}

	}

	private void recall_check(HashMap<String, String> O_STRUCT1){
		img_recall_target = (ImageView) findViewById(R.id.img_recall_target);
		img_recall_done = (ImageView) findViewById(R.id.img_recall_done);
		img_recall_target.setBackgroundResource(R.drawable.check_off);
//		img_recall_target.setOnClickListener(this);
		isCheck_recall_target = false;
		img_recall_done.setBackgroundResource(R.drawable.check_off);
		img_recall_done.setOnClickListener(this);
		isCheck_recall_done = false;

		String recall_target = O_STRUCT1.get("RECALL");
		String recall_done = O_STRUCT1.get("RECALLST");

		if(recall_target != null && recall_target.equalsIgnoreCase("X")){
			isCheck_recall_target = true;
			img_recall_target.setBackgroundResource(R.drawable.check_on);
		} else {
			isCheck_recall_target = false;
			img_recall_target.setBackgroundResource(R.drawable.check_off);
		}

		if(recall_done != null && recall_done.equalsIgnoreCase("X")){
			isCheck_recall_done = true;
			img_recall_done.setBackgroundResource(R.drawable.check_on);
		} else {
			isCheck_recall_done = false;
			img_recall_done.setBackgroundResource(R.drawable.check_off);
		}

	}


	private void setStart(HashMap<String, String> o_struct1) {
		pp.setMessage("출발등록 중 입니다.");
		pp.show();

		HashMap<String, String> i_struct1 = new HashMap<String, String>();
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
		i_struct1.put("TPLNR_FR", o_struct1.get("TPLNR_FR")); // [출발]주차장코드(기능위치)
		i_struct1.put("PSTLZ1", o_struct1.get("PSTLZ1")); // [출발]우편번호
		i_struct1.put("ORT011", o_struct1.get("ORT011")); // [출발]우편주소
		i_struct1.put("STRAS1", o_struct1.get("STRAS1")); // [출발]상세주소
		i_struct1.put("PERNR1", o_struct1.get("PERNR1")); // [출발]담당자
		i_struct1.put("TELF11", o_struct1.get("TELF11")); // [출발]담당자연락처
		i_struct1.put("OUTKM", o_struct1.get("OUTKM")); // [출발]KM
		i_struct1.put("OUTOIL", o_struct1.get("OUTOIL")); // [출발]Oil 코드
		i_struct1.put("OUTGDOIL", o_struct1.get("OUTGDOIL")); // [출발]Oil 양(DFM)
		i_struct1.put("GUEBG", o_struct1.get("GUEBG")); // [출발]출발일자
		i_struct1.put("STIME", o_struct1.get("STIME")); // [출발]출발시간

		cc.setZMO_3140_WR01(i_struct1);
	}

	private void setEnd(HashMap<String, String> o_struct1) {
		pp.setMessage("도착등록 중 입니다.");
		pp.show();

		HashMap<String, String> i_struct1 = new HashMap<String, String>();
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
		i_struct1.put("TPLNR_TO", o_struct1.get("TPLNR_TO")); // [도착]주차장코드(기능위치)
		i_struct1.put("PSTLZ2", o_struct1.get("PSTLZ1")); // [도착]우편번호
		i_struct1.put("ORT012", o_struct1.get("ORT011")); // [도착]우편주소
		i_struct1.put("STRAS2", o_struct1.get("STRAS1")); // [도착]상세주소
		i_struct1.put("PERNR2", o_struct1.get("PERNR1")); // [도착]담당자
		i_struct1.put("TELF12", o_struct1.get("TELF11")); // [도착]담당자연락처

		i_struct1.put("INKM", o_struct1.get("INKM")); // [도착]KM
		i_struct1.put("INOIL", o_struct1.get("INOIL")); // [도착]Oil 코드
		i_struct1.put("INGDOIL", o_struct1.get("INGDOIL")); // [도착]Oil 양(DFM)

		i_struct1.put("GUEEN", o_struct1.get("GUEEN")); // [도착]도착일자
		i_struct1.put("ETIME", o_struct1.get("ETIME")); // [도착]도착시간

		i_struct1.put("GDOIL2", o_struct1.get("GDOIL2")); // [운행비용]주유량(L)
		i_struct1.put("OILPRI", o_struct1.get("OILPRI")); // [운행비용]주유금액
		i_struct1.put("WASH", o_struct1.get("WASH")); // [운행비용]세차비
		i_struct1.put("PARK", o_struct1.get("PARK")); // [운행비용]주차비
		i_struct1.put("TOLL", o_struct1.get("TOLL")); // [운행비용]통행료
		i_struct1.put("OTRAMT", o_struct1.get("OTRAMT")); // [운행비용]기타비용
		i_struct1.put("TOT_AMT", o_struct1.get("TOT_AMT")); // [운행비용]차량운행비용
		i_struct1.put("CUST_DESC2", o_struct1.get("CUST_DESC2")); // [운행비용]차량운행비용

		cc.setZMO_3140_WR02(i_struct1);
	}

	@Override
	public void reDownloadDB(String newVersion) {
		// TODO Auto-generated method stub

	}

	// myung 20131220 ADD TextView에서 1라인을 넘을 경우 … 풍선 추가하여 클릭시 주소 확인
	private void isEnableTextBalloon(TextView v) {
		Layout layout = v.getLayout();
		if (layout != null) {
			if (layout.getEllipsisCount(layout.getLineCount() - 1) > 0) {
				v.setEnabled(true);
			} else {
				v.setEnabled(false);
			}

		}
	}

	// myung 20131220 ADD TextView에서 1라인을 넘을 경우 … 풍선 추가하여 클릭시 주소 확인
	private void showTextBalloon(TextView v) {
		Layout layout = v.getLayout();
		if (layout != null) {
			if (layout.getEllipsisCount(layout.getLineCount() - 1) > 0) {
				Popup_Window_Text_Balloon pwtb = new Popup_Window_Text_Balloon(context);
				pwtb.show(v, v.getText().toString());
			}
		}
	}

	private void setCommon() {
		kog.e("hjt", "hjt 진행 상태 = " + O_STRUDCT1.get("PRMSTSNM") + " Code = " + O_STRUDCT1.get("PRMSTS"));
		String prmsts = O_STRUDCT1.get("PRMSTS");
		tv_top1_id1.setText(O_STRUDCT1.get("INVNR")); // 차량번호
		// tv_top1_id2.setText(""); // 고객안내
		tv_top1_id3.setText(O_STRUDCT1.get("PRMSTSNM"));// 정비진행상태
		tv_top1_id4.setText(O_STRUDCT1.get("AUFNR")); // 14.11.10 Jonathan
														// 정비접수번호 추가
		tv_top2_id1.setText(O_STRUDCT1.get("RECNAM")); // 접수자명
		tv_top2_id2.setText(O_STRUDCT1.get("RECHP")); // 접수자연락처
		tv_top2_id3.setText(O_STRUDCT1.get("RECRELNM"));// 접수자관계명
		tv_top2_id4.setText(O_STRUDCT1.get("DAM01N")); // 배정자
		tv_top2_id5.setText(O_STRUDCT1.get("DAM02N")); // 수행자
		// Jonathan 14.06.23 사고 , 일반, 타이어 로 구분 해야 한다.

		if (O_STRUDCT1.get("PROC_TXT").equals("") || O_STRUDCT1.get("PROC_TXT").equals(null)
				|| O_STRUDCT1.get("PROC_TXT").equals(" ")) {
			kog.e("Jonathan", "PROC_TXT is null");
			String setTextProc = "";
//			setTextProc = "1. 통화일시 : \n" + "2. 정비(사고)부 : \n" + "3. 입고지 : \n" + "4. 진행사항 : \n" + "5. 완료일정 : \n";
			setTextProc = "1. \n" + "2. \n" + "3. \n" + "4. \n" + "5. \n";

			mEditText.setText(setTextProc); // 업무조치사항 내용

		} else {
			kog.e("Jonathan", "PROC_TXT is not null");
			mEditText.setText(O_STRUDCT1.get("PROC_TXT")); // 업무조치사항 내용
		}

		// Jonathan 150414
		Call_Time = Common.FileRead(O_STRUDCT1.get("AUFNR"), Menu2_1_Activity.this);
		kog.e("Jonathan", "call_time :: " + Call_Time);
		menu4_call_time1.setText(Call_Time);
		menu4_call_time2.setText(Call_Time);
		menu4_call_time3.setText(Call_Time);

		// 2017.05.30. hjt
		// 진행상태에 따른 단순종료 버튼 활성화 토글
		if(prmsts != null && bt_simple_end != null && bt_simple_end.getVisibility() == View.VISIBLE){
			if(prmsts.equals("10") || prmsts.equals("30") || prmsts.equals("60")){
				bt_simple_end.setClickable(true);
				bt_simple_end.setBackgroundResource(R.drawable.search_btn_selector);
				bt_accident_insure_call.setClickable(true);
			} else {
				bt_simple_end.setClickable(false);
				bt_simple_end.setBackgroundResource(R.drawable.btn02_d);
				bt_accident_insure_call.setClickable(false);
			}
		}
	}

	private void setAccident(String o_reqdes) {
		String date = O_STRUDCT1.get("ACCDT");
		tv_accident_line1_id1.setText(getDateFormat(date)); // 사고일시
		tv_accident_line1_id2.setText(O_STRUDCT1.get("ACCTYPNM")); // 사고유형
		tv_accident_line1_id3.setText(O_STRUDCT1.get("ACCKNDNM")); // 사고종류
		tv_accident_line2_id1.setText(O_STRUDCT1.get("ACCPCD")); // 사고장소1
		tv_accident_line2_id2.setText(O_STRUDCT1.get("FULL_ADDR1")); // 사고장소2
		tv_accident_line2_id3.setText(O_STRUDCT1.get("ACCRNM")); // 사고장소3
		tv_accident_line3_id1.setText(O_STRUDCT1.get("ACCCAUNM")); // 사고원인1
		tv_accident_line3_id2.setText(O_STRUDCT1.get("ACCCAU2N")); // 사고원인2
		tv_accident_line3_id3.setText(O_STRUDCT1.get("RESCAUNM")); // 귀책사유
		tv_accident_line4_id1.setText(O_STRUDCT1.get("ACCDES")); // 사고내용
		tv_accident_line5_id1.setText(O_STRUDCT1.get("REQPCD")); // 정비요청장소1
		// myung 20131119 정비요청장소=> FULL_ADDR2 + REQRNM
		tv_accident_line5_id2.setText(O_STRUDCT1.get("FULL_ADDR2") + " " + O_STRUDCT1.get("REQRNM")); // 정비요청장소2
		tv_accident_line6_id1.setText(o_reqdes); // 요청사항
		// myung 20131119 특이사항/초치사항 변경 추가
		tv_accident_line7_id1.setText(O_STRUDCT1.get("MCRSPE")); // 특이사항

		bt_accident_insure_call.setVisibility(View.VISIBLE);
		tv_accident_insure_sp_name.setVisibility(View.VISIBLE);
		tv_accident_insure_sp_number.setVisibility(View.VISIBLE);

		// myung 20131218 ADD 조치사항, 특이사항 전송 필드 추가
		if (O_STRUDCT1.get("MCRSPE").equals(" ")) {
			// tv_accident_line7_id1.setEnabled(true);
		}

		tv_accident_line8_id1.setText(O_STRUDCT1.get("MCRACT")); // 조치사항
		// myung 20131218 ADD 조치사항, 특이사항 전송 필드 추가
		if (O_STRUDCT1.get("MCRACT").equals(" ")) {
			// tv_accident_line8_id1.setEnabled(true);
		}

		tv_accident_line8_id2.setText(O_STRUDCT1.get("LIFNRNM"));


		String time = O_STRUDCT1.get("RECTM");
		tv_accident_line9_id1
				.setText(getDateFormat(O_STRUDCT1.get("RECDT")) + " " + getTimeFormat(time.substring(0, 4))); // 접수일시
		tv_accident_line9_id2.setText(O_STRUDCT1.get("REGPRNNM")); // 등록자
		tv_accident_line9_id3.setText("  " + O_STRUDCT1.get("RESVNO")); // 대차예약번호
		tv_accident_line9_id4.setText(O_STRUDCT1.get("RVKGRPNM")); // 대차지점

		// myung 20131220 ADD Textview에서 1라인을 넘을 경우 … 풍선 추가하여 클릭시 주소 확인
		isEnableTextBalloon(tv_accident_line2_id2);
		isEnableTextBalloon(tv_accident_line2_id3);
		isEnableTextBalloon(tv_accident_line4_id1);
		isEnableTextBalloon(tv_accident_line5_id2);
		isEnableTextBalloon(tv_accident_line6_id1);
		isEnableTextBalloon(tv_accident_line7_id1);
		isEnableTextBalloon(tv_accident_line8_id1);

	}

	private void setNormal(String o_ACCDES, String o_INTCONT, String o_CUSCONT, String o_VENCONT) {
		tv_normal_line1_id1.setText(O_STRUDCT1.get("REQPCD")); // 정비요청장소1
		// myung 20131129 UPDATE 정비요청장소=> FULL_ADDR2 + REQRNM
		tv_normal_line1_id2.setText(O_STRUDCT1.get("FULL_ADDR2") + " " + O_STRUDCT1.get("REQRNM")); // 정비요청장소2
		tv_normal_line2_id1.setText(o_ACCDES); // 요청사항
		tv_normal_line3_id1.setText(o_INTCONT); // 내부전달사항
		tv_normal_line4_id1.setText(o_CUSCONT); // 협력전달사항
		tv_normal_line5_id1.setText(O_STRUDCT1.get("MCRSPE")); // 특이사항
		// myung 20131218 ADD 조치사항, 특이사항 전송 필드 추가
		if (O_STRUDCT1.get("MCRSPE").equals(" ")) {
			// tv_normal_line5_id1.setEnabled(true);
		}
		tv_normal_line6_id1.setText(O_STRUDCT1.get("MCRACT")); // 조치내용
		// myung 20131218 ADD 조치사항, 특이사항 전송 필드 추가
		if (O_STRUDCT1.get("MCRACT").equals(" ")) {
			// tv_normal_line6_id1.setEnabled(true);
		}
		String time = O_STRUDCT1.get("RECTM");
		tv_normal_line7_id1.setText(getDateFormat(O_STRUDCT1.get("RECDT")) + " " + getTimeFormat(time.substring(0, 4))); // 접수일
		tv_normal_line7_id2.setText(O_STRUDCT1.get("REGPRNNM")); // 등록자
		tv_normal_line7_id3.setText("  " + O_STRUDCT1.get("RESVNO")); // 대차예약번호
		tv_normal_line7_id4.setText(O_STRUDCT1.get("RVKGRPNM")); // 대차지점

		// myung 20131220 ADD Textview에서 1라인을 넘을 경우 … 풍선 추가하여 클릭시 주소 확인
		isEnableTextBalloon(tv_normal_line1_id2);
		isEnableTextBalloon(tv_normal_line2_id1);
		isEnableTextBalloon(tv_normal_line3_id1);
		isEnableTextBalloon(tv_normal_line4_id1);
		isEnableTextBalloon(tv_normal_line5_id1);
		isEnableTextBalloon(tv_normal_line6_id1);

	}

	private void setEmergency(String o_EMRCONT) {
		// Log.i("출동위치1", O_STRUDCT1.get("CLDPCD"));
		// Log.i("출동위치2", O_STRUDCT1.get("CLDRNM"));
		tv_emergency_line1_id1.setText(O_STRUDCT1.get("NAME1")); // 보험사
		tv_emergency_line1_id2.setText(O_STRUDCT1.get("LIFHP")); // 연락처
		tv_emergency_line2_id1.setText(O_STRUDCT1.get("CLDPCD")); // 출동위치1
		// myung 20131119 출동위치 수정 필요. CLDPCD + FULL_ADDR2 + CLDRNM 로 출력 필요.
		tv_emergency_line2_id2.setText(O_STRUDCT1.get("FULL_ADDR2") + " " + O_STRUDCT1.get("CLDRNM")); // 출동위치2
		tv_emergency_line3_id1.setText(O_STRUDCT1.get("DRVNAM")); // 운전자명
		tv_emergency_line3_id2.setText(O_STRUDCT1.get("DRVHP")); // 연락처

		if (O_STRUDCT1.get("BATTERY").equals("X")) {
			iv_emergency_line4_id1.setImageResource(R.drawable.check_on); // 배터리
		} else {
			iv_emergency_line4_id1.setImageResource(R.drawable.check_off); // 배터리
		}
		if (O_STRUDCT1.get("PUNCT").equals("X")) {
			iv_emergency_line4_id2.setImageResource(R.drawable.check_on); // 펑크
		} else {
			iv_emergency_line4_id2.setImageResource(R.drawable.check_off); // 펑크
		}
		if (O_STRUDCT1.get("TOWCAR").equals("X")) {
			iv_emergency_line4_id3.setImageResource(R.drawable.check_on); // 견인
		} else {
			iv_emergency_line4_id3.setImageResource(R.drawable.check_off); // 견인
		}

		if (O_STRUDCT1.get("RESCUE").equals("X")) {
			iv_emergency_line4_id4.setImageResource(R.drawable.check_on); // 구난
		} else {
			iv_emergency_line4_id4.setImageResource(R.drawable.check_off); // 구난
		}

		if (O_STRUDCT1.get("RELSLK").equals("X")) {
			iv_emergency_line5_id1.setImageResource(R.drawable.check_on); // 잠금장치해제
		} else {
			iv_emergency_line5_id1.setImageResource(R.drawable.check_off); // 잠금장치해제
		}

		if (O_STRUDCT1.get("EMFUEL").equals("X")) {
			iv_emergency_line5_id2.setImageResource(R.drawable.check_on); // 비상급유
		} else {
			iv_emergency_line5_id2.setImageResource(R.drawable.check_off); // 비상급유
		}

		if (O_STRUDCT1.get("FAILST").equals("X")) {
			iv_emergency_line5_id3.setImageResource(R.drawable.check_on); // 시동불능
		} else {
			iv_emergency_line5_id3.setImageResource(R.drawable.check_off); // 시동불능
		}
		if (O_STRUDCT1.get("CHGTIR").equals("X")) {
			iv_emergency_line5_id4.setImageResource(R.drawable.check_on); // 타이어교체
		} else {
			iv_emergency_line5_id4.setImageResource(R.drawable.check_off); // 타이어교체
		}

		tv_emergency_line6_id1.setText(O_STRUDCT1.get("OTHERS")); // 기타사항

		tv_emergency_line7_id1.setText(o_EMRCONT); // 내용

		// myung 20131118 UPDATE 긴급출동상태, 완료일시 처리
		// String hikaku_hensu = O_STRUDCT1.get("EMRSTS");
		// if (hikaku_hensu.equals(" "))
		// tv_emergency_line8_id1.setText(""); // 긴급출동상태
		// else if (Integer.valueOf(hikaku_hensu) != null
		// && Integer.valueOf(hikaku_hensu) == 0)
		// tv_emergency_line8_id1.setText(""); // 긴급출동상태
		// else
		// tv_emergency_line8_id1.setText(hikaku_hensu); // 긴급출동상태

		// myung 20131119 ADD 공통코드의 PM076 의 코드값과 EMRSTS 를 비교하여 같은 값을 코드명을 표기함.
		if (O_STRUDCT1.get("EMRSTS").equals(" "))
			tv_emergency_line8_id1.setText(" ");
		else
			tv_emergency_line8_id1.setText(initPM076(O_STRUDCT1.get("EMRSTS")));

		// Log.i("####", "긴근출동상태" + O_STRUDCT1.get("EMRSTS"));
		String time = O_STRUDCT1.get("COMPTM"); // 완료시간
		String hikaku_hensu = O_STRUDCT1.get("COMPDT"); // 완료날짜
		// Log.i("####", "####타임" + time);

		try {
			if (hikaku_hensu.equals(" "))
				tv_emergency_line8_id2.setText(" "); // 완료일시
			if (time != null && !time.equals("") && !time.equals(" ") && Integer.valueOf(time) != null && Integer.valueOf(time) == 0)
				tv_emergency_line8_id2.setText(" "); // 완료일시
			else if (Integer.valueOf(hikaku_hensu) != null && Integer.valueOf(hikaku_hensu) == 0)
				tv_emergency_line8_id2.setText(" "); // 완료일시
			else
				tv_emergency_line8_id2
						.setText(getDateFormat(O_STRUDCT1.get("COMPDT")) + " " + getTimeFormat(time.substring(0, 4))); // 완료일시
		} catch (Exception e){
			e.printStackTrace();
		}

		tv_emergency_line8_id3.setText(O_STRUDCT1.get("ZNAME1")); // 출동기사

	}

	// 2013.12.03 ypkim
	private void setTire(String strTrcUser, String strTrcSpec) {
		String strDate = "";
		String strAddr = "";

		tv_tire_line1_id1.setText(O_STRUDCT1.get("SHIPGBNM")); // 배송구분

		strDate = O_STRUDCT1.get("REQUDT");
		tv_tire_line1_id2.setText(getDateFormat(strDate)); // 출고요청일
		tv_tire_line1_id3.setText(O_STRUDCT1.get("QTYSUM")); // 출고수량

		tv_tire_line2_id1.setText(O_STRUDCT1.get("LIFNRNM")); // 공급업체
		tv_tire_line2_id2.setText(O_STRUDCT1.get("MOB_NUMBER")); // 공급업체 연락처

		tv_tire_line3_id1.setText(O_STRUDCT1.get("STRAS")); // 공급업체 주소

		tv_tire_line4_id1.setText(O_STRUDCT1.get("LIFNR2NM")); // 인수자
		tv_tire_line4_id2.setText(O_STRUDCT1.get("LIF2TL1")); // 인수자 연락처1

		strAddr = "[" + O_STRUDCT1.get("LIF2ZIP") + "] " + O_STRUDCT1.get("LIF2DST");
		tv_tire_line5_id1.setText(strAddr); // 인수자 주소

		tv_tire_line6_id1.setText(strTrcUser); // 고객요청사항
		tv_tire_line7_id1.setText(strTrcSpec); // 특이사항

	}

	private void showSMSPopup(){
		InsureSMSPopup insureSMSPopup = new InsureSMSPopup(this, O_STRUDCT1, O_STRUDCT2, O_STRUDCT3, O_STRUDCT4, O_STRUDCT5, CUSNAM, dam01nm, telf1);
		insureSMSPopup.show();
	}

	/**
	 * 수입은 "X", 국산은 ""
	 * @param i_impyn
	 */

	private void callZPM_0087_001(String i_impyn){
		cc.getZPM_0087_001(O_STRUDCT1.get("REQPCD"), O_STRUDCT1.get("AUFNR"), i_impyn);
	}

	private void showSelectInOutPopup(){
        final EventPopupCC ep1 = new EventPopupCC(this, "수입 / 국산 중 선택해주세요");
        Button bt_yes = (Button) ep1.findViewById(R.id.btn_ok);
        bt_yes.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                ep1.dismiss();
                callZPM_0087_001("X");
            }
        });
        Button bt_no = (Button) ep1.findViewById(R.id.btn_cancel);
        bt_no.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                ep1.dismiss();
                callZPM_0087_001(" ");
            }
        });
		ep1.setOkButtonText("수입");
		ep1.setCancelButtonText("국산");
        ep1.show();
    }
	@Override
	public void onClick(View v) {
		Object data = null;
		switch (v.getId()) {
			case R.id.call_insure:
				showSelectInOutPopup();
				break;

		case R.id.menu4_top_bt_customer_id:
			Customer_Info_Dialog cid = new Customer_Info_Dialog(context, O_STRUDCT1, O_STRUDCT2, O_STRUDCT3, O_STRUDCT4,
					O_STRUDCT5, CUSNAM, MODE);
			cid.show();
			break;
		case R.id.menu4_top_bt_tire_id:
			kog.e("Jonathan", "타이어 신청내역 누름");
			Tire_List_Dialog tld = new Tire_List_Dialog(context, O_STRUDCT1);
			tld.show();
			break;
		case R.id.menu4_top_bt_history_id:
			HashMap<String, String> o_struct1 = new HashMap<String, String>();
			String strEqunr = O_STRUDCT1.get("EQUNR");

			// 2013.12.03 ypkim
			if (strEqunr == null || strEqunr.equals(" ")) {
				// 아래 message 처리 필요.
				// 차량의 설비번호가 없어 조회 할 수 없습니다.
				// MOT에 문의해 주세요.

				return;
			}

			
			
			o_struct1.put("CUSNAM", CUSNAM); 
			o_struct1.put("AUFNR", AUFNR); 
			o_struct1.put("MODE", MODE); 
			o_struct1.put("INVNR", INVNR);
			o_struct1.put("EQUNR", EQUNR); 
			o_struct1.put("KUNNR", KUNNR); 
			
			kog.e("Jonathan", "aaaaaaaaaaaaaaa :: " + MODE);
			
			
			o_struct1.put("EQUNR", O_STRUDCT1.get("EQUNR")); // 설비번호
			o_struct1.put("INVNR", O_STRUDCT1.get("INVNR")); // 차번호
			o_struct1.put("CONTNM", O_STRUDCT1.get("RECNAM")); // 이름(접수자)
			History_Dialog hd = new History_Dialog(this, o_struct1, 0);
			hd.show();
			break;

		case R.id.menu4_accident_done_id1:
			// myung 20131220 UPDATE 차량운행내역 버튼 상입
			// getDetail();
			data = O_STRUDCT1.get("RESVNO");
			if (data == null || data.toString().equals("") || data.toString().equals(" ")) {
				EventPopupC epc = new EventPopupC(context);
				epc.show("대차예약정보가 없습니다");
				return;
			}
			getDetailMenu3Regist();
			break;
		// myung 20131220 ADD 차량운행내역 버튼 상입
		case R.id.menu4_accident_done_id3:
			getDetailMovementDialog();
			break;
		case R.id.menu4_accident_done_id2:
			finish();
			break;
		case R.id.menu4_normal_done_id1:
			// myung 20131220 UPDATE 차량운행내역 버튼 상입
			// getDetail();
			data = O_STRUDCT1.get("RESVNO");
			if (data == null || data.toString().equals("") || data.toString().equals(" ")) {
				EventPopupC epc = new EventPopupC(context);
				epc.show("대차예약정보가 없습니다");
				return;
			}
			getDetailMenu3Regist();
			break;
		// myung 20131220 ADD 차량운행내역 버튼 상입
		case R.id.menu4_normal_done_id3:
			getDetailMovementDialog();
			break;
		case R.id.menu4_normal_done_id2:
			finish();
			break;
		// myung 20131118 UPDATE 긴급선택 -> 상세내역 조회 -> 확인 버튼 옆에 차량운행 등록 버튼 추가
		case R.id.menu4_emergency_done_id1:
			getDetail();
			break;
		case R.id.menu4_emergency_done_id2:
			finish();
			break;
		// 2013.12.03 ypkim
		case R.id.menu4_tire_done_id1: {
			// getDetail();
			// 2014-01-23 KDH 일단 팝업으로 대체를 했지만 추가 될 사항임.
			EventPopupC epc = new EventPopupC(context);
			epc.show("대차예약정보가 없습니다");
		}
			break;
		case R.id.menu4_tire_done_id2:
			finish();
			break;
		// 2014-01-23 KDH 으흠 차량운행내역 추가.
		case R.id.menu4_tire_done_id3:
			getDetailMovementDialog();
			break;

		// myung 20131217 ADD 사고정비접수 / 일반정비접수 상세내역의 연락처 필드에 전화걸기 버튼 추가. 버튼 클릭 시
		// 전화연결.
		case R.id.customer_call_id:
			if (aviliableCALL(context)) {
				data = tv_top2_id2.getText();
				String phone = data == null || data.toString().equals("") || data.toString().equals(" ") ? ""
						: data.toString();

				if (phone.equals("")) {
					EventPopupC epc = new EventPopupC(context);
					epc.show("연락처가 없습니다. \n고객 연락처를 확인하여 주십시요.");
				} else {
//					CallSendPopup2 csp = new CallSendPopup2(context);
//					if (phone.substring(0, 2).equals("01"))
//						csp.show(phone, "");
//					else
//						csp.show("", phone);
					
					
HashMap<String, String> setData = new HashMap<String, String>();
					
					setData.put("AUFNR", AUFNR);
					setData.put("KUNNR", KUNNR);
					setData.put("INVNR", INVNR);
					
					showEventPopup1(context, O_STRUDCT1.get("AUFNR"), setData, this, phone, new OnEventOkCallListener() {


						@Override
						public void onOk(String AUFNR, String ERDAT, String ERZET, String KUNNR, String INVNR) {
							// TODO Auto-generated method stub
							
						}
	                });
					
					
					
				}
			} else {
				EventPopupC epc = new EventPopupC(context);
				epc.show("전화어플이 설치되어있지 않습니다.");
			}
			break;

		case R.id.driver_call_id:
			if (aviliableCALL(context)) {
				data = tv_top2_id7.getText();
				String phone = data == null || data.toString().equals("") || data.toString().equals(" ") ? ""
						: data.toString();

				if (phone.equals("")) {
					EventPopupC epc = new EventPopupC(context);
					epc.show("연락처가 없습니다. \n운전자 연락처를 확인하여 주십시요.");
				} else {

					// Jonathan 150414
					// long now = System.currentTimeMillis();
					// Date date = new Date(now);
					// SimpleDateFormat CurDateFormat = new
					// SimpleDateFormat("yyyy-MM-dd HH:mm");
					// String strCurDate = CurDateFormat.format(date);
					//
					//
					// String Call_Time_Exist;
					// Call_Time_Exist =
					// Common.FileRead(O_STRUDCT1.get("AUFNR"),
					// Menu2_1_Activity.this);
					//
					//
					// if((Call_Time_Exist == null) ||
					// "".equals(Call_Time_Exist) || "
					// ".equals(Call_Time_Exist))
					// {
					// // 전화한 시간이 있으면 아무것도 안하고
					// }
					// else
					// {
					// // 전화한 시간이 존재하지 않으면 시간 저장
					// Common.FileWrite(O_STRUDCT1.get("AUFNR"), strCurDate,
					// Menu2_1_Activity.this);
					// }

					HashMap<String, String> setData = new HashMap<String, String>();
					
					setData.put("AUFNR", AUFNR);
					setData.put("KUNNR", KUNNR);
					setData.put("INVNR", INVNR);
					
					kog.e("Jonathan", "callSendPopup2 :: AUFNR : " + AUFNR + " : " + INVNR + " : " + KUNNR);
					
					
//					CallSendPopup2 csp = new CallSendPopup2(context, O_STRUDCT1.get("AUFNR"), setData, this);
//					if (phone.substring(0, 2).equals("01"))
//						csp.show(phone, "");
//					else
//						csp.show("", phone);
					
					showEventPopup1(context, O_STRUDCT1.get("AUFNR"), setData, this, phone, new OnEventOkCallListener() {


						@Override
						public void onOk(String AUFNR, String ERDAT, String ERZET, String KUNNR, String INVNR) {
							// TODO Auto-generated method stub
							
							kog.e("Jonathan", "11111 AUFNR : " + AUFNR + " KUNNR : " + KUNNR + " INVNR : " + INVNR + " ERDAT : " + ERDAT + " ERZET : " + ERZET);
							cc.setZMO_1100_WR03(AUFNR, ERDAT, ERZET, KUNNR, INVNR);
							
							
							
						}
	                });

					
					
				}
			} else {
				EventPopupC epc = new EventPopupC(context);
				epc.show("전화어플이 설치되어있지 않습니다.");
			}
			break;

		// myung 20131220 ADD Textview에서 1라인을 넘을 경우 … 풍선 추가하여 클릭시 주소 확인
		// 사고
		case R.id.menu4_accident_line2_id2:
			showTextBalloon(tv_accident_line2_id2);
			break;
		case R.id.menu4_accident_line2_id3:
			showTextBalloon(tv_accident_line2_id3);
			break;
		case R.id.menu4_accident_line4_id1:
			showTextBalloon(tv_accident_line4_id1);
			break;
		case R.id.menu4_accident_line5_id2:
			showTextBalloon(tv_accident_line5_id2);
			break;
		case R.id.menu4_accident_line6_id1:
			showTextBalloon(tv_accident_line6_id1);
			break;
		case R.id.menu4_accident_line7_id1:
			showTextBalloon(tv_accident_line7_id1);
			break;
		case R.id.menu4_accident_line8_id1:
			showTextBalloon(tv_accident_line8_id1);
			break;
		// 일반
		case R.id.menu4_normal_line1_id2:
			showTextBalloon(tv_normal_line1_id2);
			break;
		case R.id.menu4_normal_line2_id1:
			showTextBalloon(tv_normal_line2_id1);
			break;
		case R.id.menu4_normal_line3_id1:
			showTextBalloon(tv_normal_line3_id1);
			break;
		case R.id.menu4_normal_line4_id1:
			showTextBalloon(tv_normal_line4_id1);
			break;
		case R.id.menu4_normal_line5_id1:
			showTextBalloon(tv_normal_line5_id1);
			break;
		case R.id.menu4_normal_line6_id1:
			showTextBalloon(tv_normal_line6_id1);
			break;

		// Jonathan 150309
		// case R.id.menu4_tire_add_id:
		// int mode = 0;
		// tpd = new Tire_Picture_Dialog(context, mode, pm081_arr_menu4);
		// Button tpd_done = (Button)
		// tpd.findViewById(R.id.tire_picture_save_id);
		// tpd_done.setOnClickListener(new View.OnClickListener() {
		// @Override
		// public void onClick(View v) {
		//
		// pm081_arr_menu4.clear();
		// for (int i = 0; i < Tire_Picture_Dialog.pm081.size(); i++) {
		// if (Tire_Picture_Dialog.pm081.get(i).CHECKED) {
		// pm081_arr_menu4.add(Tire_Picture_Dialog.pm081.get(i));
		// }
		// }
		// String CHECK_BON[] = {
		// "30",
		// "40",
		// "50",
		// "60",
		// "70",
		// "80",
		// "90",
		// };
		// int bon = 0;
		// for (int i = 0; i < pm081_arr_menu4.size(); i++)
		// {
		// for (int j = 0; j < CHECK_BON.length; j++)
		// {
		// if(CHECK_BON[j].equals(pm081_arr_menu4.get(i).ZCODEV))
		// {
		// bon++;
		// }
		// }
		// }
		// menu4_tire_search_bon_id.setText(bon + "본");
		//
		//
		//
		// ta = new Menu4_Tire_Adapter(context, R.layout.tire_row,
		// pm081_arr_menu4);
		// menu4_tire_list_id.setAdapter(ta);
		// menu4_tire_list_id.setOnItemClickListener(new OnItemClickListener() {
		// @Override
		// public void onItemClick(AdapterView<?> arg0, View arg1,
		// int position, long arg3) {
		// if
		// (!pm081_del_arr_menu4.contains(pm081_arr_menu4.get(position).ZCODEV))
		// {
		// pm081_del_arr_menu4.add(pm081_arr_menu4.get(position).ZCODEV);
		// } else {
		// pm081_del_arr_menu4.remove(pm081_arr_menu4.get(position).ZCODEV);
		// }
		// ta.notifyDataSetChanged();
		// }
		// });
		// tpd.dismiss();
		//
		//
		// }
		// });
		//
		// tpd.show();
		//
		// break;

		case R.id.menu4_tire_add_id:
			int mode = 0;
			tpd = new Tire_Picture_Dialog(context, mode, pm033_arr, "");
			Button tpd_done = (Button) tpd.findViewById(R.id.tire_picture_save_id);
			tpd_done.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {

					pm033_arr.clear();
					for (int i = 0; i < Tire_Picture_Dialog.pm033.size(); i++) {
						if (Tire_Picture_Dialog.pm033.get(i).CHECKED) {
							pm033_arr.add(Tire_Picture_Dialog.pm033.get(i));
						}
					}
					String CHECK_BON[] = { "30", "40", "50", "60", "70", "80", "90", };
					int bon = 0;
					for (int i = 0; i < pm033_arr.size(); i++) {
						for (int j = 0; j < CHECK_BON.length; j++) {
							if (CHECK_BON[j].equals(pm033_arr.get(i).ZCODEV)) {
								bon++;
							}
						}
					}
					menu4_tire_search_bon_id.setText(bon + "장");

					if(pm033_arr_prev != null && pm033_arr_prev.size() > 0){
						pm033_arr_prev.addAll(pm033_arr);
						ta_033 = new Menu4_Tire_Adapter_PM033(context, R.layout.tire_row, pm033_arr_prev);
					} else {
						ta_033 = new Menu4_Tire_Adapter_PM033(context, R.layout.tire_row, pm033_arr);
					}

					menu4_tire_list_id.setAdapter(ta_033);
					menu4_tire_list_id.setOnItemClickListener(new OnItemClickListener() {
						@Override
						public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
							if(pm033_arr_prev != null && pm033_arr_prev.size() > 0) {
								if (!pm033_del_arr.contains(pm033_arr_prev.get(position).ZCODEV)) {
									pm033_del_arr.add(pm033_arr_prev.get(position).ZCODEV);
								} else {
									pm033_del_arr.remove(pm033_arr_prev.get(position).ZCODEV);
								}
							} else {
								if (!pm033_del_arr.contains(pm033_arr.get(position).ZCODEV)) {
									pm033_del_arr.add(pm033_arr.get(position).ZCODEV);
								} else {
									pm033_del_arr.remove(pm033_arr.get(position).ZCODEV);
								}
							}
							ta_033.notifyDataSetChanged();
						}
					});
					// tpd.dismiss();

					// 사진전송 시작
					if (MODE.equals("A")) // 사고
					{
						MODE_NUM = "1";
					} else if (MODE.equals("B")) // 일반
					{
						MODE_NUM = "2";
					}
					kog.e("Jonathan", "사진전송 시작");
					Object data;
					int check_count = 0;
					// final String[] path = null ;
					// final String[] name = null;

					if ("1".equals(MODE_NUM)) // 사고
					{
						for (int i = 0; i < pm033_arr.size(); i++) {

							kog.e("Jonathan", "사진전송 pm033_arr_menu4 (" + i + ")");

							if (!pm033_arr.get(i).PATH.equals(""))
								check_count++;
						}
						// final String[] path = new String[check_count];
						// final String[] name = new String[check_count];
						if (check_count != 0) {
							final String[] path_1 = new String[check_count];
							final String[] name_1 = new String[check_count];

							int count = 0;
							for (int i = 0; i < pm033_arr.size(); i++) {
								if (!pm033_arr.get(i).PATH.equals("")) {
									int num = pm033_arr.get(0).PATH.lastIndexOf("/") + 1;
									String aa = pm033_arr.get(0).PATH.substring(num);

									path_1[count] = pm033_arr.get(i).PATH;
									name_1[count] = aa;

									kog.e("Jonathan", "사진전송 Path :: " + pm033_arr.get(i).PATH);

									count++;
								}
							}
							kog.e("Jonathan", "사진전송 TireUpload_Async 시작 ");
							TireUpload_Async asynctask = new TireUpload_Async(context) {
								@Override
								protected void onProgressUpdate(ArrayList<String>... values) {
									ArrayList<String> list = values[0];
									for (int i = 0; i < list.size(); i++) {
										kog.e("Jonathan", "사진전송 사진코드넘버들" + list.get(i));
									}

									LISTa = list;
									kog.e("Jonathan", "타이어 신청 중 입니다.");
									// cc.setZMO_1060_WR01(getTableZMO_1060_WR01(path,
									// list), "", "");
									cc.setZMO_1100_WR02(MODE_NUM, getTableZMO_1100_WR02(path_1, list));
								}
							};
							asynctask.execute(path_1);
						}
					} else if ("2".equals(MODE_NUM)) // 일반
					{
						for (int i = 0; i < pm056_arr.size(); i++) {

							kog.e("Jonathan", "사진전송 pm056_arr_menu4 (" + i + ")");

							if (!pm056_arr.get(i).PATH.equals(""))
								check_count++;
						}
						// final String[] path = new String[check_count];
						// final String[] name = new String[check_count];

						if (check_count != 0) {
							final String[] path_2 = new String[check_count];
							final String[] name_2 = new String[check_count];

							int count = 0;
							for (int i = 0; i < pm056_arr.size(); i++) {
								if (!pm056_arr.get(i).PATH.equals("")) {
									int num = pm056_arr.get(0).PATH.lastIndexOf("/") + 1;
									String aa = pm056_arr.get(0).PATH.substring(num);

									path_2[count] = pm056_arr.get(i).PATH;
									name_2[count] = aa;

									kog.e("Jonathan", "사진전송 Path :: " + pm056_arr.get(i).PATH);

									count++;
								}
							}

							kog.e("Jonathan", "사진전송 TireUpload_Async 시작 ");
							TireUpload_Async asynctask = new TireUpload_Async(context) {
								@Override
								protected void onProgressUpdate(ArrayList<String>... values) {
									ArrayList<String> list = values[0];
									for (int i = 0; i < list.size(); i++) {
										kog.e("Jonathan", "사진전송 사진코드넘버들" + list.get(i));
									}

									LISTa = list;
									kog.e("Jonathan", "타이어 신청 중 입니다.");
									// cc.setZMO_1060_WR01(getTableZMO_1060_WR01(path,
									// list), "", "");
									cc.setZMO_1100_WR02(MODE_NUM, getTableZMO_1100_WR02(path_2, list));
								}
							};
							asynctask.execute(path_2);
						}
					}
					// 사진전송 끝

					tpd.dismiss();

				}
			});

			tpd.show();

			break;

		case R.id.menu4_tire_add_id_01:
			int mode_01 = 0;
			tpd = new Tire_Picture_Dialog(context, mode_01, pm056_arr, "", "");
			Button tpd_done_01 = (Button) tpd.findViewById(R.id.tire_picture_save_id);
			tpd_done_01.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {

					pm056_arr.clear();
					for (int i = 0; i < Tire_Picture_Dialog.pm056.size(); i++) {
						if (Tire_Picture_Dialog.pm056.get(i).CHECKED) {
							pm056_arr.add(Tire_Picture_Dialog.pm056.get(i));
						}
					}
					String CHECK_BON[] = { "30", "40", "50", "60", "70", "80", "90", };
					int bon = 0;
					for (int i = 0; i < pm056_arr.size(); i++) {
						for (int j = 0; j < CHECK_BON.length; j++) {
							if (CHECK_BON[j].equals(pm056_arr.get(i).ZCODEV)) {
								bon++;
							}
						}
					}
					menu4_tire_search_bon_id_01.setText(bon + "장");
					if(pm056_arr_prev != null && pm056_arr_prev.size() > 0){
						pm056_arr_prev.addAll(pm056_arr);
						ta_056 = new Menu4_Tire_Adapter_PM056(context, R.layout.tire_row, pm056_arr_prev);
					} else {
						ta_056 = new Menu4_Tire_Adapter_PM056(context, R.layout.tire_row, pm056_arr);
					}
//					ta_056 = new Menu4_Tire_Adapter_PM056(context, R.layout.tire_row, pm056_arr);
					menu4_tire_list_id_01.setAdapter(ta_056);
					menu4_tire_list_id_01.setOnItemClickListener(new OnItemClickListener() {
						@Override
						public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
							if(pm056_arr_prev != null && pm056_arr_prev.size() > 0) {
								if (!pm056_del_arr.contains(pm056_arr_prev.get(position).ZCODEV)) {
									pm056_del_arr.add(pm056_arr_prev.get(position).ZCODEV);
								} else {
									pm056_del_arr.remove(pm056_arr_prev.get(position).ZCODEV);
								}
							} else {
								if (!pm056_del_arr.contains(pm056_arr.get(position).ZCODEV)) {
									pm056_del_arr.add(pm056_arr.get(position).ZCODEV);
								} else {
									pm056_del_arr.remove(pm056_arr.get(position).ZCODEV);
								}
							}
							ta_056.notifyDataSetChanged();
						}
					});
					// tpd.dismiss();

					// 사진전송 시작
					if (MODE.equals("A")) // 사고
					{
						MODE_NUM = "1";
					} else if (MODE.equals("B")) // 일반
					{
						MODE_NUM = "2";
					}
					kog.e("Jonathan", "사진전송 시작");
					Object data;
					int check_count = 0;
					// final String[] path = null ;
					// final String[] name = null;

					if ("1".equals(MODE_NUM)) // 사고
					{
						for (int i = 0; i < pm033_arr.size(); i++) {

							kog.e("Jonathan", "사진전송 pm033_arr_menu4 (" + i + ")");

							if (!pm033_arr.get(i).PATH.equals(""))
								check_count++;
						}
						// final String[] path = new String[check_count];
						// final String[] name = new String[check_count];
						if (check_count != 0) {
							final String[] path_1 = new String[check_count];
							final String[] name_1 = new String[check_count];

							int count = 0;
							for (int i = 0; i < pm033_arr.size(); i++) {
								if (!pm033_arr.get(i).PATH.equals("")) {
									int num = pm033_arr.get(0).PATH.lastIndexOf("/") + 1;
									String aa = pm033_arr.get(0).PATH.substring(num);

									path_1[count] = pm033_arr.get(i).PATH;
									name_1[count] = aa;

									kog.e("Jonathan", "사진전송 Path :: " + pm033_arr.get(i).PATH);

									count++;
								}
							}
							kog.e("Jonathan", "사진전송 TireUpload_Async 시작 ");
							TireUpload_Async asynctask = new TireUpload_Async(context) {
								@Override
								protected void onProgressUpdate(ArrayList<String>... values) {
									ArrayList<String> list = values[0];
									for (int i = 0; i < list.size(); i++) {
										kog.e("Jonathan", "사진전송 사진코드넘버들" + list.get(i));
									}

									LISTa = list;
									kog.e("Jonathan", "타이어 신청 중 입니다.");
									// cc.setZMO_1060_WR01(getTableZMO_1060_WR01(path,
									// list), "", "");
									cc.setZMO_1100_WR02(MODE_NUM, getTableZMO_1100_WR02(path_1, list));
								}
							};
							asynctask.execute(path_1);
						}
					} else if ("2".equals(MODE_NUM)) // 일반
					{
						for (int i = 0; i < pm056_arr.size(); i++) {

							kog.e("Jonathan", "사진전송 pm056_arr_menu4 (" + i + ")");

							if (!pm056_arr.get(i).PATH.equals(""))
								check_count++;
						}
						// final String[] path = new String[check_count];
						// final String[] name = new String[check_count];

						if (check_count != 0) {
							final String[] path_2 = new String[check_count];
							final String[] name_2 = new String[check_count];

							int count = 0;
							for (int i = 0; i < pm056_arr.size(); i++) {
								if (!pm056_arr.get(i).PATH.equals("")) {
									int num = pm056_arr.get(0).PATH.lastIndexOf("/") + 1;
									String aa = pm056_arr.get(0).PATH.substring(num);

									path_2[count] = pm056_arr.get(i).PATH;
									name_2[count] = aa;

									kog.e("Jonathan", "사진전송 Path :: " + pm056_arr.get(i).PATH);

									count++;
								}
							}

							kog.e("Jonathan", "사진전송 TireUpload_Async 시작 ");
							TireUpload_Async asynctask = new TireUpload_Async(context) {
								@Override
								protected void onProgressUpdate(ArrayList<String>... values) {
									ArrayList<String> list = values[0];
									for (int i = 0; i < list.size(); i++) {
										kog.e("Jonathan", "사진전송 사진코드넘버들" + list.get(i));
									}

									LISTa = list;
									kog.e("Jonathan", "타이어 신청 중 입니다.");
									// cc.setZMO_1060_WR01(getTableZMO_1060_WR01(path,
									// list), "", "");
									cc.setZMO_1100_WR02(MODE_NUM, getTableZMO_1100_WR02(path_2, list));
								}
							};
							asynctask.execute(path_2);
						}
					}
					// 사진전송 끝

					tpd.dismiss();

				}
			});

			tpd.show();

			break;

		case R.id.menu4_tire_delete_id:

			if (pm033_del_arr.size() <= 0)
				return;

			if(pm033_arr_prev != null && pm033_arr_prev.size() > 0) {
				for (int i = 0; i < pm033_arr_prev.size(); i++) {
					for (int j = 0; j < pm033_del_arr.size(); j++) {
						if (pm033_arr_prev.get(i).ZCODEV.equals(pm033_del_arr.get(j))) {
							pm033_arr_prev.remove(i);
						}
					}
				}
			} else {
				for (int i = 0; i < pm033_arr.size(); i++) {
					for (int j = 0; j < pm033_del_arr.size(); j++) {
						if (pm033_arr.get(i).ZCODEV.equals(pm033_del_arr.get(j))) {
							pm033_arr.remove(i);
						}
					}
				}
			}
			ta_033.notifyDataSetChanged();

			break;

		case R.id.menu4_tire_delete_id_01:

			if (pm056_del_arr.size() <= 0)
				return;

			if(pm056_arr_prev != null && pm056_arr_prev.size() > 0) {
				for (int i = 0; i < pm056_arr_prev.size(); i++) {
					for (int j = 0; j < pm056_del_arr.size(); j++) {
						if (pm056_arr_prev.get(i).ZCODEV.equals(pm056_del_arr.get(j))) {
							pm056_arr_prev.remove(i);
						}
					}
				}
			} else {
				for (int i = 0; i < pm056_arr.size(); i++) {
					for (int j = 0; j < pm056_del_arr.size(); j++) {
						if (pm056_arr.get(i).ZCODEV.equals(pm056_del_arr.get(j))) {
							pm056_arr.remove(i);
						}
					}
				}
			}
			ta_056.notifyDataSetChanged();

			break;
			
			
			
			
		case R.id.ivCheckReceive:
			
			kog.e("Jonathan", "Jonathan clickclick");

			cc.getZMO_1030_WR07(AUFNR);
//			cc.getZMO_1030_RD08(AUFNR);
//			cc.getZMO_0110_004(AUFNR);
//			cc.getZPM_0110_007(AUFNR);


			break;

		/**
		 * 2017.05.23. 단순 종료 버튼 추가
		 * 사고 출동시 현장에서 완료될 수 있을 때 사용
		 */
		case R.id.simple_end:
			// 펑션 콜.
			// 화면 refresh 함
			showEventPopup1("", "단순 종료하시겠습니까?", new OnEventOkListener() {

				@Override
				public void onOk() {
					if(pp != null) {
						pp.show();
					}
					if(cc != null && O_STRUDCT1 != null){
						cc.setZMO_1100_WR04(AUFNR, INVNR, O_STRUDCT1.get("PRMSTS"));
					}
				}
			}, new OnEventCancelListener() {

				@Override
				public void onCancel() {
					// Nothing !
				}
			});
			break;

//		case R.id.img_recall_target:
//			isCheck_recall_target = !isCheck_recall_target;
//			if(isCheck_recall_target){
//				img_recall_target.setBackgroundResource(R.drawable.check_on);
//			} else {
//				img_recall_target.setBackgroundResource(R.drawable.check_off);
//			}
//			break;
		case R.id.img_recall_done:
			isCheck_recall_done = !isCheck_recall_done;
			if(isCheck_recall_done){
				img_recall_done.setBackgroundResource(R.drawable.check_on);
			} else {
				img_recall_done.setBackgroundResource(R.drawable.check_off);
			}
			break;
		}
	}

	public void showEventPopup1(String title, String body,
								OnEventOkListener onEventPopupListener,
								OnEventCancelListener onEventCancelListener) {
		EventPopup1 popup = new EventPopup1(this, body, onEventPopupListener);
		popup.setOnCancelListener(onEventCancelListener);
		popup.show();
	}

	// Jonathan 이전 사진 겹치는것 초기화
	public void initData() {

		pm056_arr = new ArrayList<PM056>();
		pm056_arr_prev = new ArrayList<PM056>();
		pm056_del_arr = new ArrayList<String>();
		ta_056 = new Menu4_Tire_Adapter_PM056(context, R.layout.tire_row, pm056_arr);

		menu4_tire_list_id_01.setAdapter(ta_056);
		ta_056.notifyDataSetChanged();

		pm033_arr = new ArrayList<PM033>();
		pm033_arr_prev = new ArrayList<PM033>();
		pm033_del_arr = new ArrayList<String>();
		ta_033 = new Menu4_Tire_Adapter_PM033(context, R.layout.tire_row, pm033_arr);
		menu4_tire_list_id.setAdapter(ta_033);
		ta_033.notifyDataSetChanged();

	}

	public boolean aviliableCALL(Context context) {
		PackageManager pac = context.getPackageManager();
		Uri callUri = Uri.parse("tel:");
		Intent callIntent = new Intent(Intent.ACTION_CALL, callUri);
		List list = pac.queryIntentActivities(callIntent, PackageManager.COMPONENT_ENABLED_STATE_DEFAULT);
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
	
	
	
	public void showEventPopup1(Context context, String AUFNR, HashMap<String, String> setData, Activity ac, String phone, OnEventOkCallListener onEventPopupListener)
    {
		kog.e("Jonathan", "CallSendPopup2 1");
		CallSendPopup2 csp = new CallSendPopup2(context, AUFNR, setData, this, onEventPopupListener);
		
//        PayConfirmPopup popup = new PayConfirmPopup(this, title, body, onEventPopupListener);
        csp.setCanceledOnTouchOutside(false);
        
        if (phone.substring(0, 2).equals("01"))
			csp.show(phone, "");
		else
			csp.show("", phone);
        	
//        csp.show();
    }


	private void getDetailMenu3Regist() { // 출발/도착등록
		// 대차예약번호가 있음 출발도착등록 창띄우기
		Object data = O_STRUDCT1.get("VBELN");
		// Log.i("####", "####VBELN / " + data);
		// myung 20131126 ADD 출발도착등록 버튼 클릭 시 VBELN 값이 없을 경우, 메시지 출력 후
		// 리턴시킴.
		if (data == null || data.toString().equals("") || data.toString().equals(" ")) {
			epc = new EventPopupC(context);
			epc.show("대차예약이 정상적으로 이루어 지지 않았습니다.\nMOT 에 문의하여 주십시요.");
			return;
		}
		// String vbeln = data == null || data.toString().equals(" ") ?
		// ""
		// : data.toString();
		// Log.i("####", "####VBELN / " + vbeln);

		final Menu3_Resist_Dialog m3rd = new Menu3_Resist_Dialog(this);
		m3rd.show(" ", " ", " ", " ", " ", " ", data.toString());
		// myung 20131213 ADD 정비접수상세내역->출발/도착등록-> 취소버튼 처리되지 않는 문제
		Button bt_dismiss = (Button) m3rd.findViewById(R.id.menu3_resist_close_id);
		bt_dismiss.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				m3rd.dismiss();
			}
		});
	}

	private void getDetailMovementDialog() { // 차량운행일지등록
		// 차량이동등록 팝업
		kog.e("KDH", "menu4_tire_done_id3");
		showMovementDialog();
	}

	private void getDetail() {
		kog.e("KDH", "menu4_tire_done_id1");
		if (MODE.equals("C")) {
			// Log.i("####", "####" + "대차예약번호가 없음");
			// 차량이동등록 팝업
			showMovementDialog();
		} else {
			if (O_STRUDCT1 == null) {
				return;// 타이어일경우 리턴
			}

			Object data = O_STRUDCT1.get("RESVNO");
			if (data == null || data.toString().equals("") || data.toString().equals(" ")) {
				// Log.i("####", "####" + "대차예약번호가 없음");
				// 차량이동등록 팝업
				showMovementDialog();
			} else {
				// 대차예약번호가 있음 출발도착등록 창띄우기
				data = O_STRUDCT1.get("VBELN");
				// Log.i("####", "####VBELN / " + data);
				// myung 20131126 ADD 출발도착등록 버튼 클릭 시 VBELN 값이 없을 경우, 메시지 출력 후
				// 리턴시킴.
				if (data == null || data.toString().equals("") || data.toString().equals(" ")) {
					epc = new EventPopupC(context);
					epc.show("대차예약이 정상적으로 이루어 지지 않았습니다.\nMOT 에 문의하여 주십시요.");
					return;
				}
				// String vbeln = data == null || data.toString().equals(" ") ?
				// ""
				// : data.toString();
				// Log.i("####", "####VBELN / " + vbeln);

				final Menu3_Resist_Dialog m3rd = new Menu3_Resist_Dialog(this);
				m3rd.show(" ", " ", " ", " ", " ", " ", data.toString());
				// myung 20131213 ADD 정비접수상세내역->출발/도착등록-> 취소버튼 처리되지 않는 문제
				Button bt_dismiss = (Button) m3rd.findViewById(R.id.menu3_resist_close_id);
				bt_dismiss.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						m3rd.dismiss();
					}
				});
			}
		}
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		// myung 20131213 ADD 페이버리스 처리 후 출발도착등록 재갱신 처리
		if (DEFINE.PAPERESS_STATUS_FLAG) {
			DEFINE.PAPERESS_STATUS_FLAG = false;
			getDetail();
		}

		super.onResume();
	}

	// Jonathan 140804 파라미터 "NULL" 추가.
	private void showMovementDialog() {
		// Log.i("####", "####aufnr" + AUFNR);
		LoginModel model = KtRentalApplication.getLoginModel();
		MovementDialog mMovementDialog = new MovementDialog(this, EQUNR, model.getPernr(), AUFNR, model.getFUELNM(),
				model.getEname(), "1", INVNR);
		mMovementDialog.show();
	}

	private String getDate() {
		Calendar calendar = Calendar.getInstance();
		int int_year = calendar.get(Calendar.YEAR);
		int int_month = calendar.get(Calendar.MONTH) + 1;
		int int_day = calendar.get(Calendar.DAY_OF_MONTH);
		String year = String.format("%04d", int_year);
		String month = String.format("%02d", int_month);
		String day = String.format("%02d", int_day);
		return year + month + day;
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		pp.dismiss();
		finish();
		// super.onBackPressed();
	}

	// myung 20131119 ADD 함수추가 공통코드의 PM076 의 코드값과 EMRSTS 를 비교하여 같은 값을 코드명을 표기함.
	String TABLE_NAME = "O_ITAB1";

	private String initPM076(String strEMRSTS) {
		String retZCODEVT = " ";
		String path = context.getExternalCacheDir() + "/DATABASE/" + DEFINE.SQLLITE_DB_NAME;
		SQLiteDatabase sqlite = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.OPEN_READONLY);
		Cursor cursor = sqlite.rawQuery(
				"select ZCODEV, ZCODEVT from " + TABLE_NAME + " where ZCODEH = 'PM076' AND ZCODEV = " + strEMRSTS,
				null);

		cursor.moveToFirst();
		int calnum = cursor.getColumnIndex("ZCODEVT");
		retZCODEVT = cursor.getString(calnum);

		cursor.close();
		// 2017.07.05. hjt. db close 는 app 종료시 알아서 된다
		// 이것때문에 throwIfClosedLocked Exception 발생한다
//		sqlite.close();

		return retZCODEVT;
	}

	private void sendSMS() {
		kog.e("Jonathan", " sendSMS");
		StringBuffer sb = new StringBuffer();

		ArrayList<String> sp_message_parts = null;
		ArrayList<String> cus_message_parts = null;

		String carenum = context.getString(R.string.insure_care_number);
		String carnum = context.getString(R.string.insure_carnum);
		String car_kind = context.getString(R.string.insure_car_kind);
		String accidate = context.getString(R.string.insure_accidate);
		String acci_location = context.getString(R.string.insure_acci_location);
		String acci_content = context.getString(R.string.insure_acci_content);
		String acci_insure_num = context.getString(R.string.insure_acci_insure_num);
		String acci_repair_location = context.getString(R.string.insure_acci_repair_location);
		String spName = context.getString(R.string.insure_sp_name);
		String spPhone = context.getString(R.string.insure_sp_phone);
		String car_manager = context.getString(R.string.insure_car_manager);
		String car_manager_phone = context.getString(R.string.insure_car_manager_phone);
		String customer_name = context.getString(R.string.insure_customer_name);
		String customer_phone = context.getString(R.string.insure_customer_phone);
		String etc = context.getString(R.string.insure_etc);

		String content_carenum = O_STRUDCT1.get("AUFNR");
		String content_carnum  = O_STRUDCT1.get("INVNR");
		String content_car_kind = O_STRUDCT2.get("MAKTX");
		String content_accidate = O_STRUDCT1.get("ACCDT");
		String content_acci_location = O_STRUDCT1.get("FULL_ADDR2") + " " + O_STRUDCT1.get("REQRNM");
		String content_acci_content = O_STRUDCT1.get("ACCDES");
//		mEtAcciInsureNum.setText(model.get("INVNR"));
		String content_acci_repair_location = O_STRUDCT1.get("REQPCD");
		String content_spName = O_STRUDCT1.get("SPLIFNR");
		String content_spPhone = O_STRUDCT1.get("SPTEL");
		String content_car_manager = O_STRUDCT1.get("DAM02N");
		String content_car_manager_phone = O_STRUDCT1.get("DAM02HP");
		String content_customer_name = O_STRUDCT1.get("DRVNAM");
		String content_customer_phone = O_STRUDCT1.get("DRVHP");

		sb.append(carenum + " : " + content_carenum);
		sb.append("\r\n");
//        sb.append(System.getProperty( "line.separator"));
		sb.append(carnum + " : " + content_carnum);
		sb.append("\r\n");
//        sb.append(System.getProperty( "line.separator"));
		sb.append(car_kind + " : " + content_car_kind);
		sb.append("\r\n");
//        sb.append(System.getProperty( "line.separator"));
		sb.append(accidate + " : " + content_accidate);
		sb.append("\r\n");
//        sb.append(System.getProperty( "line.separator"));
		sb.append(acci_location + " : " + content_acci_location);
		sb.append("\r\n");
//        sb.append(System.getProperty( "line.separator"));
//        sb.append(acci_repair_location + " : " + content_acci_repair_location);
//        sb.append(System.getProperty( "line.separator"));
		sb.append(acci_content + " : " + content_acci_content);
		sb.append("\r\n");
//        sb.append(System.getProperty( "line.separator"));
//        sb.append(acci_insure_num + " : " + "");
//        sb.append(System.getProperty( "line.separator"));
		sb.append(acci_repair_location + " : " + content_acci_repair_location);
//        sb.append(System.getProperty( "line.separator"));
		sb.append("\r\n");
		sb.append(customer_name + " : " + content_customer_name);
		sb.append("\r\n");
//        sb.append(System.getProperty( "line.separator"));
		sb.append(customer_phone + " : " + content_customer_phone);
		sb.append("\r\n");
//        sb.append(System.getProperty( "line.separator"));
		sb.append(car_manager + " : " + content_car_manager);
		sb.append("\r\n");
//        sb.append(System.getProperty( "line.separator"));
		sb.append(car_manager_phone + " : " + content_car_manager_phone);
		sb.append("\r\n");
//        sb.append(System.getProperty( "line.separator"));
//        sb.append(etc + " : " + mTvEtc.getText().toString());

//		String phoneNumber = mTvContact.getText().toString();
//		String message = mEtMessage.getText().toString();
		SmsManager sms = SmsManager.getDefault();
		String sp_message = String.valueOf(sb);

		/**
		 * 여기는 고객에게 전달할 내용
		 */

		StringBuffer cus_sb = new StringBuffer();
		cus_sb.append(carnum + " : " + content_carnum);
		cus_sb.append("\r\n");
//        cus_sb.append(System.getProperty( "line.separator"));
		cus_sb.append(car_kind + " : " + content_car_kind);
//        cus_sb.append(System.getProperty( "line.separator"));
		cus_sb.append("\r\n");
		cus_sb.append(accidate + " : " + content_accidate);
		cus_sb.append("\r\n");
//        cus_sb.append(System.getProperty( "line.separator"));
		cus_sb.append(acci_location + " : " + content_acci_location);
		cus_sb.append("\r\n");
//        cus_sb.append(System.getProperty( "line.separator"));
		cus_sb.append(acci_repair_location + " : " + content_acci_repair_location);
		cus_sb.append("\r\n");
//        cus_sb.append(System.getProperty( "line.separator"));
		cus_sb.append(spName + " : " + dam01nm);
		cus_sb.append("\r\n");
//        cus_sb.append(System.getProperty( "line.separator"));
		cus_sb.append(spPhone + " : " + telf1);
		cus_sb.append("\r\n");
//        cus_sb.append(System.getProperty( "line.separator"));
		cus_sb.append("* 대차 담당자가 순차적으로 빠른 시간 내에 연락 드릴 예정입니다.");

		String cus_message = String.valueOf(cus_sb);
		kog.e("Jonathan", "SMS cus_message = " + cus_message);
		LogUtil.e("SMS", "SMS cus_message = " + cus_message);

		if(sp_message != null) {
			sp_message_parts = sms.divideMessage(sp_message);
			LogUtil.e("SMS", "SMS SENT 1");
		}

		if(cus_message != null) {
			cus_message_parts = sms.divideMessage(cus_message);
			LogUtil.e("SMS", "SMS SENT 2");
		}

		ArrayList<PendingIntent> sentIntents = new ArrayList<PendingIntent>();
		sentReceiver = new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent) {

				Log.d("SMS", "SMS onReceive intent received.");
				boolean anyError = false;
				switch (getResultCode()) {
					case Activity.RESULT_OK:
						break;
					case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
					case SmsManager.RESULT_ERROR_NO_SERVICE:
					case SmsManager.RESULT_ERROR_NULL_PDU:
					case SmsManager.RESULT_ERROR_RADIO_OFF:
						anyError = true;
						break;
				}
				msgParts--;
				if (msgParts == 0) {
					if(pp != null){
						if(pp.isShowing()){
							pp.hide();
						}
					}
					if (anyError) {
						EventPopupC epc = new EventPopupC(context);
						epc.show("문자 발송 실패하였습니다.\n 보험대차 요청을 다시 하시기 바랍니다.");
//						Toast.makeText(context,"발송 실패!!!",
//								Toast.LENGTH_LONG).show();
						cc.getZPM_0087_007(O_STRUDCT1.get("AUFNR"), "X");
						bt_accident_insure_call.setClickable(true);
						bt_accident_insure_call.setAlpha(1.0f);
					} else {
//						Toast.makeText(context,"발송 성공!!!",
//								Toast.LENGTH_LONG).show();
						EventPopupC epc = new EventPopupC(context);
						epc.show("문자 발송이 완료되었습니다.");
						cc.getZPM_0087_007(O_STRUDCT1.get("AUFNR"), " ");
						bt_accident_insure_call.setAlpha(0.5f);
						bt_accident_insure_call.setClickable(false);
					}

					unregisterReceiver(sentReceiver);
				}

			}
		};
		registerReceiver(sentReceiver, new IntentFilter(SENT_ACTION));

		// SMS delivered receiver
//		 registerReceiver(new BroadcastReceiver() {
//		 @Override
//		 public void onReceive(Context context, Intent intent) {
////		 Log.d(TAG, "SMS delivered intent received.");
//		 }
//		 }, new IntentFilter(DELIVERED_ACTION));

		final int numParts = sp_message_parts.size() + cus_message_parts.size();

		for (int i = 0; i < numParts; i++) {
			sentIntents.add(PendingIntent.getBroadcast(this, 0, new Intent(
					SENT_ACTION), 0));
		}


		msgParts = numParts;
		if(pp == null) {
			pp = new ProgressPopup(this);
		}
		pp.setMessage("SMS 전송중입니다.");
		pp.show();

		if(DEFINE.getDEBUG_MODE()){
			content_customer_phone = "01099458330";
			content_spPhone = "01099458330";
		}

		LogUtil.e("SMS", "SMS content_spPhone = " + content_spPhone);
		LogUtil.e("SMS", "SMS content_customer_phone = " + content_customer_phone);
		/** SMS 발송 시작 **/
		sms.sendMultipartTextMessage(content_spPhone, null, sp_message_parts, sentIntents, null);
		saveCallLog("SMS", content_spPhone);

		final SmsManager sms2 = sms;
		final String content_customer_phone2 = content_customer_phone;
		final ArrayList<String> cus_message_parts2 = cus_message_parts;
		final ArrayList<PendingIntent> sentIntents2 = sentIntents;

		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				sms2.sendMultipartTextMessage(content_customer_phone2, null, cus_message_parts2, sentIntents2, null);
				saveCallLog("SMS", content_customer_phone2);
			}
		}, 1000);


//		if(mSendSmsCount > 0) {
//			send_SMS_RFC(content_spPhone, "1588-1230", "0", "보험대차SMS", sp_message, "PM019", content_car_manager);
//		} else {
//			send_SMS_RFC(content_customer_phone, "1588-1230", "0", "보험대차SMS", cus_message, "PM019", dam01nm);
//		}
	}

	private void send_SMS_RFC(String TRTEL, String ANSTEL, String SCHETP, String SUBJECT, String MSG, String USERID, String KUNNRNM){
		cc.ZSD_SEND_SMS("", KUNNRNM, USERID, "", "", TRTEL, ANSTEL, SCHETP, SUBJECT, MSG);
	}

    private void saveCallLog(String type, String tel) {
        ArrayList<HashMap<String, String>> arr = new ArrayList<HashMap<String, String>>();
        HashMap<String, String> map = new HashMap<String, String>();
//		map.put("INVNR", mModel.getCarNum());
//		map.put("DRIVN", mModel.getDRIVER_NAME());
//		map.put("DATE", CommonUtil.getCurrentDay());

        String strNow = "";
        // 현재 시간을 msec으로 구한다.
        long now = System.currentTimeMillis();
        // 현재 시간을 저장 한다.
        Date date = new Date(now);
        // 시간 포맷으로 만든다.
        SimpleDateFormat sdfNow = new SimpleDateFormat("HHmm");
        strNow = sdfNow.format(date);

        map.put("TIME", strNow);
        map.put("TYPE", type);
        map.put("TEL", tel);
        arr.add(map);

        TableModel tableModel = new TableModel(DEFINE.CALL_LOG_TABLE_NAME, arr,
                "");

        DbAsyncTask task = new DbAsyncTask("saveCallLog",
                DEFINE.CALL_LOG_TABLE_NAME, context, new DbAsyncResLintener() {

            @Override
            public void onCompleteDB(String funName, int type,
                                     Cursor cursor, String tableName) {
                // TODO Auto-generated method stub

            }
        }, tableModel);
        task.execute(DbAsyncTask.DB_TABLE_CREATE);
    }

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(childFragment != null){
			try {
				childFragment.onActivityResult(requestCode, resultCode, data);
				return;
			} catch (Exception e){
				e.printStackTrace();
			}
		}
		if(requestCode == 2){
			if(resultCode == RESULT_OK){
				try {
					if(tpd != null){
						try {
							Uri selectedImage = data.getData();
							InputStream in = context.getContentResolver().openInputStream(selectedImage);
							Bitmap img = BitmapFactory.decodeStream(in);
							tpd.sendImg(img, data);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
//		mDrawerFragment.onActivityResult(requestCode, requestCode, data);
		LogUtil.d("TAG", "requestCode = " + requestCode);
		LogUtil.d("TAG", "resultCode = " + resultCode);
	}

	private Fragment childFragment = null;
	public void setFragment(Fragment fragment){
		childFragment = fragment;
	}
}
