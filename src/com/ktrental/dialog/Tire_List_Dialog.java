package com.ktrental.dialog;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.ktrental.R;
import com.ktrental.adapter.Tire_List_Dialog_Adapter;
import com.ktrental.cm.connect.ConnectController2;
import com.ktrental.cm.connect.Connector.ConnectInterface;
import com.ktrental.model.TableModel;
import com.ktrental.popup.DatepickPopup2;
import com.ktrental.popup.EventPopupC;
import com.ktrental.popup.Popup_Window_DelSearch;
import com.ktrental.popup.DatepickPopup2.OnDismissListener;
import com.ktrental.util.kog;

public class Tire_List_Dialog extends DialogC implements ConnectInterface, 
		View.OnClickListener {
	private Window w;
	private WindowManager.LayoutParams lp;
	private Context context;
	private ProgressDialog pd;

	private Button bt_close;
	private Button bt_search;
	private Button bt_done;
	// private EditText et_input;
	private ConnectController2 cc;
	private HashMap<String, String> o_struct1;

	public ArrayList<HashMap<String, String>> array_hash;
	private ListView lv_list;
	private Tire_List_Dialog_Adapter tlda;
	public int SELECTED = -1;

	private Button bt_date1, bt_date2;
	private DatepickPopup2 dp2;
	private Button bt_carnum;
	
	private Popup_Window_DelSearch pwDelSearch;
	
	public Tire_List_Dialog(Context context) {
		super(context);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.tire_list_dialog);
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

		bt_done = (Button) findViewById(R.id.tire_list_done_id);
		bt_done.setOnClickListener(this);
		bt_close = (Button) findViewById(R.id.tire_list_close_id);
		bt_close.setOnClickListener(this);
		bt_search = (Button) findViewById(R.id.tire_search_id);
		bt_search.setOnClickListener(this);

		bt_date1 = (Button) findViewById(R.id.tire_date1_id);
		bt_date1.setOnClickListener(this);
		bt_date2 = (Button) findViewById(R.id.tire_date2_id);
		bt_date2.setOnClickListener(this);

		bt_carnum = (Button) findViewById(R.id.tire_carnum_id);
		bt_carnum.setOnClickListener(this);

		lv_list = (ListView) findViewById(R.id.tire_list_list_id);

		setDateButton();
	}

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		pwDelSearch = new Popup_Window_DelSearch(context);
	}
	
	public Tire_List_Dialog(Context context, HashMap<String, String> o_struct1) {
		super(context);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.tire_list_dialog);
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

		bt_done = (Button) findViewById(R.id.tire_list_done_id);
		bt_done.setOnClickListener(this);
		bt_close = (Button) findViewById(R.id.tire_list_close_id);
		bt_close.setOnClickListener(this);
		bt_search = (Button) findViewById(R.id.tire_search_id);
		bt_search.setOnClickListener(this);

		bt_date1 = (Button) findViewById(R.id.tire_date1_id);
		bt_date1.setOnClickListener(this);
		bt_date2 = (Button) findViewById(R.id.tire_date2_id);
		bt_date2.setOnClickListener(this);

		bt_carnum = (Button) findViewById(R.id.tire_carnum_id);
		bt_carnum.setOnClickListener(this);

		lv_list = (ListView) findViewById(R.id.tire_list_list_id);

//		showProgress("조회중 입니다.");
		setDateButton();
//		clickSearch();
	}

	private void setDateButton() { 
		Calendar cal = Calendar.getInstance();
		int year = cal.get(Calendar.YEAR);
		int month = cal.get(Calendar.MONTH) + 1;
		int day = cal.get(Calendar.DAY_OF_MONTH);
		String month_str = String.format("%02d", month);
		String day_str = String.format("%02d", day);
		bt_date2.setText(year + "." + month_str + "." + day_str);// 오늘날짜
		if (day < 7) {
			bt_date1.setText(year + "." + month_str + "." + "01");// 이달의첫날
		} else {
			int aday = day - 6;
			String aday_str = String.format("%02d", aday);
			bt_date1.setText(year + "." + month_str + "." + aday_str);
		}
		
		//myung 20131121 ADD
		//2014-02-18 KDH 정책변경 삭제 
		if(o_struct1!=null)
			bt_carnum.setText(o_struct1.get("INVNR"));
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
		if (FuntionName.equals("ZMO_1030_RD07")) {
			array_hash = tableModel.getTableArray();
			setList(array_hash);
			cc.duplicateLogin(mContext);
			
		}
	}

	private void setList(final ArrayList<HashMap<String, String>> _list) {
		for (int i = 0; i < array_hash.size(); i++) {
			kog.e("KDH", "AUFNR = "+array_hash.get(i).get("AUFNR"));
			
		}
		tlda = new Tire_List_Dialog_Adapter(context,
				R.layout.tire_list_dialog_row, _list);
		lv_list.setAdapter(tlda);
		lv_list.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				tlda.setCheckPosition(position);
				Tire_List_Detail_Dialog tldd = new Tire_List_Detail_Dialog(
						context, _list.get(position), mHandler);
				tldd.show();
			}
		});
	}
	
	Handler mHandler = new Handler()
	{

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			kog.e("KDH", "handleMessage");
			clickSearch();
			super.handleMessage(msg);
		}
		
	};
	
	

	//myung 20131121 ADD 
	private void clickSearch(){
		showProgress("조회중 입니다.");
		kog.e("Jonathan", "프로그래스바 돌아가냐?");
		String data;
		data = bt_date1.getText().toString();
		String date1 = data.replace(".", "");
		data = bt_date2.getText().toString();
		String date2 = data.replace(".", "");
		Object obj = bt_carnum.getText();
		/*
		if (obj == null || obj.toString().equals("")) 
		{
			EventPopupC epc = new EventPopupC(context);
			epc.show("차량번호를 입력해 주세요");
			return;
		}
		*/
//		` wr01

		String carnum = obj.toString();
		
		cc.getZMO_1030_RD07(date1, date2, carnum);
	}
	@Override
	public void reDownloadDB(String newVersion) {
	}

	@Override
	public void onClick(View v) {
		String data;
		String date;
		switch (v.getId()) {
		case R.id.tire_list_close_id: // 닫기
			dismiss();
			break;
		case R.id.tire_list_done_id:
			dismiss();
			break;
		case R.id.tire_search_id: // 조회
			clickSearch();
			kog.e("Jonathan", "타이어신청 내역 조회 누름");
//			data = bt_date1.getText().toString();
//			String date1 = data.replace(".", "");
//			data = bt_date2.getText().toString();
//			String date2 = data.replace(".", "");
//			Object obj = bt_carnum.getText();
//			if (obj == null || obj.toString().equals("")) {
//				EventPopupC epc = new EventPopupC(context);
//				epc.show("차량번호를 입력해 주세요");
//				return;
//			}
//
//			String carnum = obj.toString();
//			showProgress("조회중 입니다.");
//			cc.getZMO_1030_RD07(date1, date2, carnum);

			break;
		case R.id.tire_date1_id:
			data = bt_date2.getText().toString();
			date = data.replace(".", "");
			dp2 = new DatepickPopup2(context, date, 0);
			dp2.show(bt_date1);
			break;
		case R.id.tire_date2_id:
			data = bt_date1.getText().toString();
			date = data.replace(".", "");
			dp2 = new DatepickPopup2(context, date, 1);
			dp2.show(bt_date2);
			break;

		case R.id.tire_carnum_id:
			
			//myung 20131202 UPDATE 차량번호 터치 시 지우기, 조회하기 팝업 뛰우기
			data = bt_carnum.getText().toString();
			if (data != null && !data.toString().equals("")) {
				ViewGroup vg = pwDelSearch.getViewGroup();
				LinearLayout back = (LinearLayout) vg.getChildAt(0);
				final Button del = (Button) back.getChildAt(0);
				
				del.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						bt_carnum.setText("");
						bt_carnum.setTag("");
						pwDelSearch.dismiss();
					}
				});
				final Button sel = (Button) back.getChildAt(1);
				sel.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						new Camera_Dialog(context).show(bt_carnum);
						pwDelSearch.dismiss();
					}
				});
				
				pwDelSearch.show(bt_carnum);
			} else {
				
//				new Camera_Dialog(context).show(bt_carnum);
				final Camera_Dialog cd = new Camera_Dialog(context);
				Button cd_done = (Button) cd.findViewById(R.id.camera_done_id);
				final EditText et_carnum = (EditText) cd
						.findViewById(R.id.camera_carnum_id);
				cd_done.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View arg0) {
						Object data = et_carnum.getText();
						String str = data == null || data.toString().equals("") ? ""
								: data.toString();
						if (str.equals("")) {
							EventPopupC epc = new EventPopupC(context);
							epc.show("차량번호를 입력해 주세요.");
						} else {
							bt_carnum.setText(str);
							cd.dismiss();
						}
					}
				});
				cd.show();
			}	
			
//			final Camera_Dialog cd = new Camera_Dialog(context);
//			Button cd_done = (Button) cd.findViewById(R.id.camera_done_id);
//			final EditText et_carnum = (EditText) cd
//					.findViewById(R.id.camera_carnum_id);
//			cd_done.setOnClickListener(new View.OnClickListener() {
//				@Override
//				public void onClick(View arg0) {
//					Object data = et_carnum.getText();
//					String str = data == null || data.toString().equals("") ? ""
//							: data.toString();
//					if (str.equals("")) {
//						EventPopupC epc = new EventPopupC(context);
//						epc.show("차량번호를 입력해 주세요.");
//					} else {
//						bt_carnum.setText(str);
//						cd.dismiss();
//					}
//				}
//			});
//			cd.show();
			break;
		}
	}
	
	

	// 키보드내리기
	// Handler pre = new Handler()
	// {
	// @Override
	// public void handleMessage(Message msg)
	// {
	// super.handleMessage(msg);
	// InputMethodManager imm = (InputMethodManager)
	// context.getSystemService(Context.INPUT_METHOD_SERVICE);
	// imm.hideSoftInputFromWindow(et_input.getWindowToken(), 0);
	// }
	// };
}
