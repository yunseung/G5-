package com.ktrental.product;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.ktrental.R;
import com.ktrental.activity.EmptyService;
import com.ktrental.adapter.HomeNoticeAdapter;
import com.ktrental.beans.PM114;
import com.ktrental.cm.connect.ConnectController;
import com.ktrental.cm.connect.Connector.ConnectInterface;
import com.ktrental.model.HomeNoticeModel;
import com.ktrental.model.TableModel;
import com.ktrental.popup.EventPopupC;
import com.ktrental.popup.EventPopupCC;
import com.ktrental.popup.Popup_Window_PM114;
import com.ktrental.util.SharedPreferencesUtil;
import com.ktrental.util.kog;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import static com.ktrental.common.DEFINE.ZMO_1100_RD02;
import static com.ktrental.common.DEFINE.ZMO_1100_RD03;

public class Menu1_Activity extends BaseActivity implements ConnectInterface,
		OnClickListener {

	private ArrayList<HashMap<String, String>> O_ITAB1;

	private TextView tv_l1b1, tv_l1b2, tv_l1b3, tv_l1b4;
	private TextView tv_l2b1, tv_l2b2, tv_l2b3, tv_l2b4;
	private TextView tv_l3b1, tv_l3b2, tv_l3b3, tv_l3b4;

	private ListView lv_list;
	private Menu1_Adapter m1a;
	private ImageView iv_nodata;

	private LinearLayout[] ll_block1 = new LinearLayout[4];
	private LinearLayout[] ll_block2 = new LinearLayout[4];

	// VOC 메뉴 추가 (menu1_voc_info)
	// KangHyunJin ADD(20151208)
	private Button bt_done, menu1_voc_info, bt_menu_pm114, btnReflesh;

	// private Popup_Window_PM114 pwPM11;
	private Popup_Window_PM114 pwPM11;
	private String mbeforeText;
	private String mPreferICURSTATUS = "대기";
	
	
	private Intent emptyService;
	
	private ListView                     mLvNotice;
	private HomeNoticeAdapter            mHomeNoticeAdapter;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.menu1_activity);
		

		
		
        
		EmptyService.getActivity(this);
		
		startEmptyService();

		// mPreferICURSTATUS = cc.loadPreferences(context, "I_CUR_STATUS");
		SharedPreferencesUtil shareU = SharedPreferencesUtil.getInstance();
		mPreferICURSTATUS = shareU.getString("I_CUR_STATUS", "대기");

		init(0);
		initView();
		
		
				

	}

	private void initView() {
		tv_l1b1 = (TextView) findViewById(R.id.menu1_line1_block1_id);
		tv_l1b2 = (TextView) findViewById(R.id.menu1_line1_block2_id);
		tv_l1b3 = (TextView) findViewById(R.id.menu1_line1_block3_id);
		tv_l1b4 = (TextView) findViewById(R.id.menu1_line1_block4_id);
		tv_l2b1 = (TextView) findViewById(R.id.menu1_line2_block1_id);
		tv_l2b2 = (TextView) findViewById(R.id.menu1_line2_block2_id);
		tv_l2b3 = (TextView) findViewById(R.id.menu1_line2_block3_id);
		tv_l2b4 = (TextView) findViewById(R.id.menu1_line2_block4_id);
		tv_l3b1 = (TextView) findViewById(R.id.menu1_line3_block1_id);
		tv_l3b2 = (TextView) findViewById(R.id.menu1_line3_block2_id);
		tv_l3b3 = (TextView) findViewById(R.id.menu1_line3_block3_id);
		tv_l3b4 = (TextView) findViewById(R.id.menu1_line3_block4_id);

		lv_list = (ListView) findViewById(R.id.menu1_list_id);
		iv_nodata = (ImageView) findViewById(R.id.list_nodata_id);
		
		
		mLvNotice = (ListView) findViewById(R.id.lv_notice);
		mHomeNoticeAdapter = new HomeNoticeAdapter(context);
		mLvNotice.setAdapter(mHomeNoticeAdapter);
        mLvNotice.setOnItemClickListener(new OnItemClickListener()
        {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3)
            {
                kog.e("Jonathan", "Jonathan" + position + "누름");
                Intent intent;
                intent = new Intent(context, Menu4_2_Notice_Activity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
        				| Intent.FLAG_ACTIVITY_SINGLE_TOP);
                intent.putExtra("is_CarManager", mPermgp);
        		startActivity(intent);
        		overridePendingTransition(0, 0);

            }
        });

		ll_block1[0] = (LinearLayout) findViewById(R.id.menu1_line1_block1_click_id);
		ll_block1[0].setOnClickListener(this);
		ll_block1[1] = (LinearLayout) findViewById(R.id.menu1_line1_block2_click_id);
		ll_block1[1].setOnClickListener(this);
		ll_block1[2] = (LinearLayout) findViewById(R.id.menu1_line1_block3_click_id);
		ll_block1[2].setOnClickListener(this);
		ll_block1[3] = (LinearLayout) findViewById(R.id.menu1_line1_block4_click_id);
		ll_block1[3].setOnClickListener(this);

		ll_block2[0] = (LinearLayout) findViewById(R.id.menu1_line2_block1_click_id);
		ll_block2[0].setOnClickListener(this);
		ll_block2[1] = (LinearLayout) findViewById(R.id.menu1_line2_block2_click_id);
		ll_block2[1].setOnClickListener(this);
		ll_block2[2] = (LinearLayout) findViewById(R.id.menu1_line2_block3_click_id);
		ll_block2[2].setOnClickListener(this);
		ll_block2[3] = (LinearLayout) findViewById(R.id.menu1_line2_block4_click_id);
		ll_block2[3].setOnClickListener(this);

		bt_done = (Button) findViewById(R.id.menu1_done_id);
		bt_done.setOnClickListener(this);
		menu1_voc_info = (Button) findViewById(R.id.menu1_voc_info_id);
		menu1_voc_info.setOnClickListener(this);
		btnReflesh = (Button) findViewById(R.id.btnReflesh);
		btnReflesh.setOnClickListener(this);
		bt_menu_pm114 = (Button) findViewById(R.id.menu_pm114);
		bt_menu_pm114.setOnClickListener(this);
		// Log.i("mPreferICURSTATUS", mPreferICURSTATUS);
		bt_menu_pm114.setText(mPreferICURSTATUS);
		bt_menu_pm114.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				if (!mbeforeText.equals(s.toString())) {
					onSelectPM114(null, s.toString());
				}
			}
		});

		String date1 = getDateFirst();
		String date2 = getDate();
		pp.show();
		Log.e("", date1 + "/" + date2);
		cc.getZMO_1100_RD02(date1, date2);
//		cc.updateSession(context, LoginActivity.session);
	}

	public void onSelectPM114(View v, final String status) {
		final EventPopupCC ep1 = new EventPopupCC(context, status
				+ " 로 상태를 변경하시겠습니까?");
		Button bt_yes = (Button) ep1.findViewById(R.id.btn_ok);
		bt_yes.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				ep1.dismiss();
				mbeforeText = status;
				ArrayList<PM114> tempPM114 = pwPM11.getPM114_Array();
				for (int i = 0; i < tempPM114.size(); i++) {
					if (tempPM114.get(i).ZCODEVT.equals(mbeforeText)) {
						cc.setZMO_1120_WR02(tempPM114.get(i).ZCODEV.toString());
						break;
					}
				}
			}
		});
		Button bt_no = (Button) ep1.findViewById(R.id.btn_cancel);
		bt_no.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				ep1.dismiss();
				bt_menu_pm114.setText(mbeforeText);
			}
		});
		ep1.show();
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

	private String getDateFirst() {
		Calendar calendar = Calendar.getInstance();
		int int_year = calendar.get(Calendar.YEAR);
		int int_month = calendar.get(Calendar.MONTH) + 1;
		int int_day = calendar.get(Calendar.DAY_OF_MONTH);
		String year = String.format("%04d", int_year);
		String month = String.format("%02d", int_month);
		String day = String.format("%02d", int_day);
		return year + month + "01";
	}

	@Override
	public void onMenu1(View v) {
	}

	@Override
	protected void onPause() {
		if(pp != null)
			pp.dismiss();
		super.onPause();
	}

	@Override
	public void connectResponse(String FuntionName, String resultText,
			String MTYPE, int resulCode, TableModel tableModel) {
		// Log.i("#", "####" + FuntionName + "/" + resultText + "/" + MTYPE +
		// "/"
		// + resulCode);

		
		
		
		if (FuntionName.equals("ZMO_1010_WR02")) {

			kog.e("Jonathan", "공지사항들어오니?	1");

			if (MTYPE == null || !MTYPE.equals("S")) {

				 final EventPopupC epc = new EventPopupC(context);
	             Button btn_confirm = (Button)epc.findViewById(R.id.btn_ok);
	             btn_confirm.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View arg0) {
							// TODO Auto-generated method stub
							epc.dismiss();
							finish();
							android.os.Process.killProcess(android.os.Process.myPid());
							System.exit(0);
							
							
						}
					});
	             epc.show(resultText);
			}
			else
			{
				kog.e("Jonathan", "공지사항들어오니?	2");
				String date1 = getDateFirst();
				String date2 = getDate();
				cc.getZMO_1100_RD02(date1, date2);
				
			}
			
		} 
		
		
		
		if (MTYPE == null || !MTYPE.equals("S")) {
			
			cc.duplicateLogin(context);
			
			EventPopupC epc = new EventPopupC(this);
			epc.show(resultText);
			return;
		}

		if (FuntionName.equals(ZMO_1100_RD02)) {

			kog.e("Jonathan", "공지사항들어오니?	3");

			O_ITAB1 = tableModel.getTableArray();

			if (O_ITAB1.size() <= 0) {
				setField1();
			} else {
				setField1(O_ITAB1.get(0));
			}

			if(pp != null)
				pp.show();
			// 2013.11.18 YPKIM
			// ZMO_1100_RD03 호출 시 차량번호, 정비유형, 정비진행상태 호출하도록 수정 함.
			// ZMO_1100_RD03 펑션 하나로 사용 함.
			// 2014-02-21 KDH 인자값 변경. I_SNDCUS
			// M040?<-컬럼명
			cc.getZMO_1100_RD03(" ", "A", "N", getDate(), getDate());
			// cc.getZMO_1100_RD03(carnum, type, status, date1, date2)
		} else if (FuntionName.equals(ZMO_1100_RD03)) {

			kog.e("Jonathan", "공지사항들어오니?	4");

			initNoticeData();
			
			O_ITAB1 = tableModel.getTableArray();
			if (O_ITAB1.size() > 0) {
				iv_nodata.setVisibility(View.GONE);
			} else {
				iv_nodata.setVisibility(View.VISIBLE);
				return;
			}

			kog.e("Jonathan", "공지사항들어오니?	5");

			setList();
			
			
		} else if (FuntionName.equals("ZMO_1120_WR02")) {
			// cc.savePreferences(context, "I_CUR_STATUS", mbeforeText);
			// Log.i("mbeforeText", mbeforeText);
			SharedPreferencesUtil shareU = SharedPreferencesUtil.getInstance();
			shareU.setString("I_CUR_STATUS", mbeforeText);
			
			cc.duplicateLogin(context);

		}
		else if (FuntionName.equals("ZMO_1060_RD06"))
        {
			kog.e("Jonathan", "공지사항들어오니?	6");

            if (tableModel == null)
                return;
            ArrayList<HashMap<String, String>> RD06Array = tableModel.getTableArray();
            ArrayList<HomeNoticeModel> arrayList = new ArrayList<HomeNoticeModel>();
            for (int i = 0; i < RD06Array.size(); i++)
            {
                // Log.i("", "####" + RD06Array.get(i).get("AN_SBM"));

                // NoticeModel model = new NoticeModel();
                HomeNoticeModel Hmodel = new HomeNoticeModel(RD06Array.get(i).get("AN_SBM"), RD06Array.get(i).get("RG_BDTM"));
                // model.setTitle(RD06Array.get(i).get("AN_SBM")); // 공지제목
                // model.setDay(RD06Array.get(i).get("RG_BDTM")); // 공지시작일
                // model.setDay(RD06Array.get(i).get("RG_BENO")); // 최초등록사원

                // mNoticeModels.add(model);
                arrayList.add(Hmodel);

            }

            mHomeNoticeAdapter.setData(arrayList);
            
            
//			cc.duplicateLogin(context);
			Intent intent;
			intent = new Intent(context, Menu4_2_Notice_Activity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
					| Intent.FLAG_ACTIVITY_SINGLE_TOP);
			intent.putExtra("is_CarManager", mPermgp);
			startActivity(intent);
			overridePendingTransition(0, 0);

        }

        if(pp != null && pp.isShowing()) {
			pp.hide();
		}
		
	}

	private void setList() {
		m1a = new Menu1_Adapter(this, R.layout.menu1_row, O_ITAB1);
		lv_list.setAdapter(m1a);
		lv_list.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				m1a.setCheckPosition(position);
			}
		});


		//170403 Jonathan 공지사항을 첫번째 페이지로..
		kog.e("Jonathan", "공지사항들어오니?	");

		Intent intent;
		intent = new Intent(context, Menu4_2_Notice_Activity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
				| Intent.FLAG_ACTIVITY_SINGLE_TOP);
		intent.putExtra("is_CarManager", mPermgp);
		startActivity(intent);
		overridePendingTransition(0, 0);

	}
	
	
	 private void initNoticeData()
	    {
	    	
	    	Calendar cal = Calendar.getInstance();
			int year = cal.get(Calendar.YEAR);
			int month = cal.get(Calendar.MONTH) + 1;
			int day = cal.get(Calendar.DAY_OF_MONTH);
			String month_str = String.format("%02d", month);
			String day_str = String.format("%02d", day);
			// DATE2 = year + month_str + day_str;
			String DATE2 = year + "." + month_str + "." + day_str;
			
//			  btn_notice_endt.setText(year + "." + month_str + "." + day_str);// 오늘날짜
			cal.set(Calendar.DAY_OF_MONTH, 1);
			day = cal.get(Calendar.DAY_OF_MONTH);
			String day_str_1 = String.format("%02d", day);
			// DATE1 = year + month_str + day_str_1;
//			btn_notice_bgdt.setText(year + "." + month_str + "." + day_str_1);// 이달의첫날
			String DATE1 = year + "." + month_str + "." + day_str_1;
	    	
	    	
	        ConnectController connectController = new ConnectController(this, context);
	        connectController.getZMO_1060_RD06("1", "10", "Y", DATE1, DATE2);
	    }
	    

	private void setField1(HashMap hm) {
		tv_l1b1.setText(hm.get("TODRCV").toString()); // 당일접수건
		tv_l1b2.setText(hm.get("TODUCL").toString()); // 미처리건
		tv_l1b3.setText(hm.get("TODCPL").toString()); // 당일완료
		tv_l1b4.setText(hm.get("TODCNL").toString()); // 당일 취소
		tv_l2b1.setText(hm.get("CUMRCV").toString()); // 누계 접수
		tv_l2b2.setText(hm.get("CUMUCL").toString()); // 누계 미처리
		tv_l2b3.setText(hm.get("CUMCPL").toString()); // 누계 완료
		tv_l2b4.setText(hm.get("CUMCNL").toString()); // 누계 취소
		tv_l3b1.setText(hm.get("CUMDCPL").toString()); // 대차완료누계
		tv_l3b2.setText(hm.get("CUMDAE").toString()); // 대차진행누계
		tv_l3b3.setText(hm.get("CUMHLD").toString()); // 대차대기누계
		tv_l3b4.setText(hm.get("CUMMLG").toString()); // 누계 운행내역

		for (int i = 0; i < O_ITAB1.size(); i++) {
			Set<String> set = O_ITAB1.get(i).keySet();
			Iterator<String> it = set.iterator();
			String key;

			while (it.hasNext()) {
				key = it.next();
				kog.e("Jonathan", "메인 홈  key ===  " + key + "    value  === "
						+ O_ITAB1.get(i).get(key));
			}
		}

	}

	private void setField1() {
		tv_l1b1.setText("0"); // 당일접수건
		tv_l1b2.setText("0"); // 미처리건
		tv_l1b3.setText("0"); // 당일완료
		tv_l1b4.setText("0"); // 당일 취소
		tv_l2b1.setText("0"); // 누계 접수
		tv_l2b2.setText("0"); // 누계 미처리
		tv_l2b3.setText("0"); // 누계 완료
		tv_l2b4.setText("0"); // 누계 취소
		tv_l3b1.setText("0"); // 대차완료누계
		tv_l3b2.setText("0"); // 대차진행누계
		tv_l3b3.setText("0"); // 대차대기누계
		tv_l3b4.setText("0"); // 누계 운행내역
	}

	@Override
	public void reDownloadDB(String newVersion) {
	}

	@Override
	public void onClick(View v) {
		Intent in;
		String date = getDate();
		switch (v.getId()) {
		case R.id.menu1_line1_block1_click_id:
			in = new Intent(this, Menu2_Activity.class);
			in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
					| Intent.FLAG_ACTIVITY_SINGLE_TOP);
			in.putExtra("STATUS", "A");
			in.putExtra("DATE1", date);
			in.putExtra("DATE2", date);
			// myung 20131202 ADD 접수화면 이동 후 from ~ to 날짜 오늘로 셋팅하여 자동으로 조회해야 함.
			in.putExtra("AUTO_SEARCH", true);
			startActivity(in);
			overridePendingTransition(0, 0);
			break;
		case R.id.menu1_line1_block2_click_id:
			in = new Intent(this, Menu2_Activity.class);
			in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
					| Intent.FLAG_ACTIVITY_SINGLE_TOP);
			in.putExtra("STATUS", "A");
			in.putExtra("DATE1", date);
			in.putExtra("DATE2", date);
			// myung 20131213 ADD
			in.putExtra("AUTO_SEARCH", true);
			startActivity(in);
			overridePendingTransition(0, 0);
			break;
		case R.id.menu1_line1_block3_click_id:
			in = new Intent(this, Menu2_Activity.class);
			in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
					| Intent.FLAG_ACTIVITY_SINGLE_TOP);
			in.putExtra("STATUS", "A");
			in.putExtra("DATE1", date);
			in.putExtra("DATE2", date);
			// myung 20131213 ADD
			in.putExtra("AUTO_SEARCH", true);
			startActivity(in);
			overridePendingTransition(0, 0);
			break;
		case R.id.menu1_line1_block4_click_id:
			kog.e("Jonathan", "당일현황 취소 누름.");
			in = new Intent(this, Menu2_Activity.class);
			in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
					| Intent.FLAG_ACTIVITY_SINGLE_TOP);
			in.putExtra("STATUS", "A");
			in.putExtra("DATE1", date);
			in.putExtra("DATE2", date);
			// myung 20131213 ADD
			in.putExtra("AUTO_SEARCH", true);
			startActivity(in);
			overridePendingTransition(0, 0);
			break;

		case R.id.menu1_line2_block1_click_id:
			kog.e("Jonathan", "당월현황 접수 누름.");
			in = new Intent(this, Menu2_Activity.class);
			in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
					| Intent.FLAG_ACTIVITY_SINGLE_TOP);
			in.putExtra("STATUS", "A");
			in.putExtra("DATE1", date.substring(0, 6) + "01");
			in.putExtra("DATE2", date);
			startActivity(in);
			overridePendingTransition(0, 0);
			break;
		case R.id.menu1_line2_block2_click_id:
			kog.e("Jonathan", "당월현황 미처리 누름.");
			in = new Intent(this, Menu2_Activity.class);
			in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
					| Intent.FLAG_ACTIVITY_SINGLE_TOP);
			in.putExtra("STATUS", "A");
			in.putExtra("DATE1", date.substring(0, 6) + "01");
			in.putExtra("DATE2", date);
			startActivity(in);
			overridePendingTransition(0, 0);
			break;
		case R.id.menu1_line2_block3_click_id:
			kog.e("Jonathan", "당월현황 완료 누름.");
			in = new Intent(this, Menu2_Activity.class);
			in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
					| Intent.FLAG_ACTIVITY_SINGLE_TOP);
			in.putExtra("STATUS", "A");
			in.putExtra("DATE1", date.substring(0, 6) + "01");
			in.putExtra("DATE2", date);
			startActivity(in);
			overridePendingTransition(0, 0);
			break;
		case R.id.menu1_line2_block4_click_id:
			in = new Intent(this, Menu2_Activity.class);
			in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
					| Intent.FLAG_ACTIVITY_SINGLE_TOP);
			in.putExtra("STATUS", "A");
			in.putExtra("DATE1", date.substring(0, 6) + "01");
			in.putExtra("DATE2", date);
			startActivity(in);
			overridePendingTransition(0, 0);
			break;
		case R.id.menu1_done_id: {
			if (m1a == null)
				return;
			if (m1a.getCheckPosition() == -1) {
				EventPopupC epc = new EventPopupC(context);
				epc.show("차량을 선택해 주세요");
				return;
			}
			in = new Intent(this, Menu2_1_Activity.class);
			in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
					| Intent.FLAG_ACTIVITY_SINGLE_TOP);

			HashMap<String, String> hm = O_ITAB1.get(m1a.getCheckPosition());
			String mode = hm.get("TXT").toString();
			if (mode.equals("사고")) {
				in.putExtra("MODE", "A");
			} else if (mode.equals("일반")) {
				in.putExtra("MODE", "B");
			} else if (mode.equals("긴급")) {
				in.putExtra("MODE", "C");
			} else if (mode.equals("타이어")) { // Jonathan 14.10.23 타이어 건이 안보인다고
												// 해서 넣음.
				in.putExtra("MODE", "E");
			} else {
				in.putExtra("MODE", "D");
				return;
			}
			in.putExtra("CUSNAM", hm.get("CUSNAM"));
			in.putExtra("AUFNR", hm.get("AUFNR"));

			in.putExtra("is_CarManager", mPermgp); // Jonathan 14.10.23 정비접수
													// 상세내역 화면에서 메뉴의 카매니저 조회를
													// 누를때 안되는 것

			startActivity(in);
			overridePendingTransition(0, 0);
		}
			break;
		// VOC 내역 클릭 이벤트
		// KangHyunJin ADD (20151208)
		case R.id.menu1_voc_info_id: {
			
			if(O_ITAB1 == null || O_ITAB1.size() == 0 || m1a == null || m1a.getCheckPosition() == -1) {
				EventPopupC epc = new EventPopupC(context);
				epc.show("당일접수현황 정보가 없거나 선택해주세요.");
				return;
			}
			
			HashMap<String, String> hm = O_ITAB1.get(m1a.getCheckPosition());
			Intent intent = new Intent(this, VocInfoActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
					| Intent.FLAG_ACTIVITY_SINGLE_TOP);
			intent.putExtra(VocInfoActivity.VOC_KUNNR, hm.get("KUNNR"));
			startActivity(intent);

		}
			break;
		case R.id.menu_pm114:
			mbeforeText = bt_menu_pm114.getText().toString();
			pwPM11 = new Popup_Window_PM114(this, bt_menu_pm114);
			pwPM11.show(bt_menu_pm114);

			break;

		// myung 20131202 ADD 홈 > 리플레쉬 기능 추가
		case R.id.btnReflesh:
			String date1 = getDateFirst();
			String date2 = getDate();
			pp.show();
			Log.e("", date1 + "/" + date2);
			cc.getZMO_1100_RD02(date1, date2);
		}

	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		pp.dismiss();
		stopEmptyService();
		super.onDestroy();
	}
	
	
	
	
	 public void startEmptyService() {
       emptyService = new Intent(this, EmptyService.class);
       startService(emptyService);
//	        PrintLog.print("startEmptyService", "startEmptyService");
   }

   public void stopEmptyService() {
   	stopService(emptyService);
//	        PrintLog.print("stopEmptyService", "stopEmptyService");
   }

}
