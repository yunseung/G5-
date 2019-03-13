package com.ktrental.dialog;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import com.ktrental.R;
import com.ktrental.activity.LoginActivity;
import com.ktrental.adapter.CallogAdapter;
import com.ktrental.adapter.History_Dialog_Detail_Adapter;
import com.ktrental.adapter.History_Dialog_Left_Adapter;
import com.ktrental.adapter.History_Dialog_Left_Adapter.onDoneClick;
import com.ktrental.adapter.History_Dialog_Tire_Adapter;
import com.ktrental.adapter.History_Dialog_Tire_Adapter.onDoneTireClick;
import com.ktrental.adapter.History_Dialog_Transfer_Adapter;
import com.ktrental.cm.connect.ConnectController;
import com.ktrental.cm.connect.Connector.ConnectInterface;
import com.ktrental.cm.db.DbAsyncTask;
import com.ktrental.cm.db.DbAsyncTask.DbAsyncResLintener;
import com.ktrental.common.DEFINE;
import com.ktrental.model.CallLogModel;
import com.ktrental.model.DbQueryModel;
import com.ktrental.model.TableModel;
import com.ktrental.popup.EventPopupC;
import com.ktrental.product.Menu2_1_Activity;
import com.ktrental.util.kog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

public class History_Dialog extends DialogC implements ConnectInterface,
		OnClickListener {
	
	
	private String I_CUSNAM;
	private String I_AUFNR;
	private String I_MODE;
	private String I_INVNR;
	private String I_EQUNR;
	private String I_KUNNR;

	
	
	private Window w;
	private WindowManager.LayoutParams lp;
	private Context context;
	private Button close;
	private ProgressDialog pd;
	private ConnectController cc;

	private RadioGroup rg;
	private RadioButton rb[] = new RadioButton[4];
	private LinearLayout tab[] = new LinearLayout[4];
	private HashMap<String, String> o_struct1;
	private String CARNUM="";
	private String NAME;
	private String MODEL;
	private TextView tv_carnum;
	private TextView tv_name;
	private TextView tv_model;
	private ArrayList<HashMap<String, String>> o_itab1_a;
	private ArrayList<HashMap<String, String>> o_itab2_a;
	private ArrayList<HashMap<String, String>> o_itab1_b;
	private ArrayList<HashMap<String, String>> o_itab1_c;
	private ListView lv_maintenance_left;
	private ListView lv_maintenance_detail;
	private ListView lv_call_log;
	private History_Dialog_Left_Adapter hdla;
	private History_Dialog_Detail_Adapter hdda;
	private History_Dialog_Tire_Adapter hdta;
	private History_Dialog_Transfer_Adapter hdtra;
	private ListView lv_transfer;

	private TextView tv_aufnr;
	private TextView tv_incml;
	private TextView tv_lifnrnm;
	private ListView lv_tire;

	private TextView tv_tiresize_front;
	// private TextView tv_tiresize_rear;
	private TextView tv_tire_receipt;
	private TextView tv_tire_mileage;
	private TextView tv_tire_change_num;
	private TextView tv_tire_front_left;
	private TextView tv_tire_front_right;
	private TextView tv_tire_rear1_left;
	private TextView tv_tire_rear1_right;
	private TextView tv_tire_rear2_left;
	private TextView tv_tire_rear2_right;
	private TextView tv_tire_spare;

	private Button bt_done;

	private int MODE = 0;

	boolean TAB1 = true;
	boolean TAB2 = true;
	boolean TAB3 = true;
	boolean TAB4 = true;

	private ImageView iv_nodata1;
	private ImageView iv_nodata2;
	private ImageView iv_nodata3;
	private ImageView iv_nodata4;

	private CallogAdapter mCallogAdapter;

	private Button bt_search;

	// private TextView tv_data;

	private TextView tv_accident;
	private TextView tv_normal;
	private TextView tv_around;
	private TextView tv_emergency;
	private TextView tv_tire;

	@Override
	protected void onStop() {
		super.onStop();
		try {
			dismiss();
		} catch (Exception e){
			e.printStackTrace();
		}
	}

	public History_Dialog(Context context, HashMap<String, String> o_struct1,
						  int mode) {
		super(context);
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.history_dialog);
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
		pd = new ProgressDialog(context);
		pd.setCancelable(false);
		pd.setMessage("검색중입니다.");
		cc = new ConnectController(this, context);
		close = (Button) findViewById(R.id.history_dialog_close_id);
		close.setOnClickListener(this);

		rg = (RadioGroup) findViewById(R.id.history_tab_rg_id);
		rb[0] = (RadioButton) findViewById(R.id.history_tab1_rb_id);
		rb[1] = (RadioButton) findViewById(R.id.history_tab2_rb_id);
		rb[2] = (RadioButton) findViewById(R.id.history_tab3_rb_id);
		rb[3] = (RadioButton) findViewById(R.id.history_tab4_rb_id);

		tab[0] = (LinearLayout) findViewById(R.id.history_tab1_layout_id);
		tab[1] = (LinearLayout) findViewById(R.id.history_tab2_layout_id);
		tab[2] = (LinearLayout) findViewById(R.id.history_tab3_layout_id);
		tab[3] = (LinearLayout) findViewById(R.id.history_tab4_layout_id);

		tv_carnum = (TextView) findViewById(R.id.history_carnum_id);
		tv_carnum.setOnClickListener(this);
		tv_name = (TextView) findViewById(R.id.history_name_id);
		tv_model = (TextView) findViewById(R.id.history_model_id);

		lv_maintenance_left = (ListView) findViewById(R.id.history_maintenance_left_list_id);
		lv_maintenance_detail = (ListView) findViewById(R.id.history_maintenance_detail_list_id);
		lv_call_log = (ListView) findViewById(R.id.history_dialog_calllog_list_id);
		tv_aufnr = (TextView) findViewById(R.id.history_aufnr_id);
		tv_incml = (TextView) findViewById(R.id.history_incml_id);
		tv_lifnrnm = (TextView) findViewById(R.id.history_lifnrnm_id);

		lv_tire = (ListView) findViewById(R.id.history_tire_list_id);
		lv_transfer = (ListView) findViewById(R.id.history_dialog_transfer_list_id);

		tv_tiresize_front = (TextView) findViewById(R.id.history_tiresize_front_id);
		// tv_tiresize_rear =
		// (TextView)findViewById(R.id.history_tiresize_rear_id);
		tv_tire_receipt = (TextView) findViewById(R.id.history_tire_receipt_id);
		tv_tire_mileage = (TextView) findViewById(R.id.history_tire_mileage_id);
		tv_tire_change_num = (TextView) findViewById(R.id.history_tire_change_num_id);
		tv_tire_front_left = (TextView) findViewById(R.id.history_front_left_id);
		tv_tire_front_right = (TextView) findViewById(R.id.history_front_right_id);
		tv_tire_rear1_left = (TextView) findViewById(R.id.history_rear1_left_id);
		tv_tire_rear1_right = (TextView) findViewById(R.id.history_rear1_right_id);
		tv_tire_rear2_left = (TextView) findViewById(R.id.history_rear2_left_id);
		tv_tire_rear2_right = (TextView) findViewById(R.id.history_rear2_right_id);
		tv_tire_spare = (TextView) findViewById(R.id.history_spare_id);

		bt_done = (Button) findViewById(R.id.history_done_id);
		bt_done.setOnClickListener(this);

		bt_search = (Button) findViewById(R.id.history_search_id);
		bt_search.setOnClickListener(this);

		iv_nodata1 = (ImageView) findViewById(R.id.list_nodata_id1);
		iv_nodata2 = (ImageView) findViewById(R.id.list_nodata_id2);
		iv_nodata3 = (ImageView) findViewById(R.id.list_nodata_id3);
		iv_nodata4 = (ImageView) findViewById(R.id.list_nodata_id4);

		MODE = mode;

		mCallogAdapter = new CallogAdapter(mContext);
		lv_call_log.setAdapter(mCallogAdapter);

		// tv_data = (TextView)findViewById(R.id.history_dialog_data_id);

		tv_accident = (TextView) findViewById(R.id.history_dialog_title1_id);
		tv_normal = (TextView) findViewById(R.id.history_dialog_title2_id);
		tv_around = (TextView) findViewById(R.id.history_dialog_title3_id);
		tv_emergency = (TextView) findViewById(R.id.history_dialog_title4_id);
		tv_tire = (TextView) findViewById(R.id.history_dialog_title5_id);
		
		
		
//		Set <String> set  = o_struct1.keySet();
//		Iterator <String> it = set.iterator();
//		String key;
//		
//		while(it.hasNext()) 
//		{
//			key = it.next();
//			kog.e("Jonathan", "모델명도 못받아. key 00000  " + key + "    value  === " + o_struct1.get(key));	//Jonathan 14.06.19 이름을 잘못 받아옴. 
//		}
		
		
		if(o_struct1 != null)
		{
			I_CUSNAM = o_struct1.get("CUSNAM");
			I_AUFNR = o_struct1.get("AUFNR");
			I_MODE = o_struct1.get("MODE");
			I_INVNR = o_struct1.get("INVNR");
			I_EQUNR = o_struct1.get("EQUNR");
			I_KUNNR= o_struct1.get("KUNNR");
		}
		
	}

	@Override
	public void onAttachedToWindow() {
		super.onAttachedToWindow();
		init(MODE);
	}

	private void init(int mode) {
		if (o_struct1 != null) {
			CARNUM = o_struct1.get("INVNR");
			NAME = o_struct1.get("CONTNM");
			MODEL = o_struct1.get("MAKTX");
			
			
			Set <String> set  = o_struct1.keySet();
			Iterator <String> it = set.iterator();
			String key;
			
			while(it.hasNext()) 
			{
				key = it.next();
				kog.e("Jonathan", "모델명도 못받아. key 00000  " + key + "    value  === " + o_struct1.get(key));	//Jonathan 14.06.19 이름을 잘못 받아옴. 
			}

			tv_carnum.setText(CARNUM);
			tv_name.setText(NAME);
			tv_model.setText(MODEL);
		}

		setButton();

		switch (mode) {
		case 0:
			rb[0].setChecked(true);
			break;
		case 1:
			rb[1].setChecked(true);
			break;
		case 2:
			rb[2].setChecked(true);
			break;
		}
	}

	private void showTab(int num) {
		for (int i = 0; i < tab.length; i++) {
			rb[i].setTextColor(Color.parseColor("#cccccc"));
			tab[i].setVisibility(View.GONE);
		}
		rb[num].setTextColor(Color.parseColor("#ffa02f"));
		tab[num].setVisibility(View.VISIBLE);

		switch (num) {
		case 0:
			if (TAB1) {
				if (o_struct1 == null)
					return;
					showProgress("조회중 입니다.");
				cc.getZMO_1020_RD03(o_struct1.get("EQUNR"), CARNUM);
			}
			break;
		case 1:
			if (TAB2) {
				if (o_struct1 == null)
					return;
				showProgress("조회중 입니다.");
				cc.getZMO_1070_RD01(CARNUM);
			}
			break;
		case 2:
			if (TAB3) {
				if (o_struct1 == null)
					return;
				showProgress("조회중 입니다.");
				cc.getZMO_1040_RD02(o_struct1.get("EQUNR"), CARNUM);
			}
			break;
		case 3:
			if (TAB4) {
				if (o_struct1 == null)
					return;
				// myung 20131202 DELETE 고객컨택이력 데이터가 있어도 데이터가 없습니다. 메시지 출력 됨.
				// showProgress("조회중 입니다.");
				// cc.getZMO_1040_RD02(o_struct1.get("EQUNR"), CARNUM);
				queryCallLog(CARNUM, NAME);
			}
			break;
		}
	}

	private void setButton() {
		close.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				try {
					dismiss();
				} catch (Exception e){
					e.printStackTrace();
				}
			}
		});

		rg.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				switch (checkedId) {
				case R.id.history_tab1_rb_id:
					showTab(0);
					break;
				case R.id.history_tab2_rb_id:
					showTab(1);
					break;
				case R.id.history_tab3_rb_id:
					showTab(2);
					break;
				case R.id.history_tab4_rb_id:
					showTab(3);
					break;
				}
			}
		});
	}

	@Override
	public void connectResponse(String FuntionName, String resultText,
			String MTYPE, int resulCode, TableModel tableModel) {
		// Log.i("#", "####" + FuntionName + "/" + resultText + "/" + MTYPE +
		// "/" + resulCode);
		hideProgress();
		if (MTYPE == null || !MTYPE.equals("S")) {
			
			cc.duplicateLogin(mContext);
			
			EventPopupC epc = new EventPopupC(context);
			try {
				epc.show(resultText);
			} catch (Exception e){
				e.printStackTrace();
			}
			if (FuntionName.equals("ZMO_1020_RD03")) {
				TAB1 = false;
			} else if (FuntionName.equals("ZMO_1070_RD01")) {
				TAB2 = false;
			}
			return;
		}

		if (FuntionName.equals("ZMO_1020_RD03")) {

			TAB1 = false;

			o_itab1_a = tableModel.getTableArray("O_ITAB1");
			o_itab2_a = tableModel.getTableArray("O_ITAB2");

			String brkcnt = tableModel.getResponse("O_BRKCNT");
			String gencnt = tableModel.getResponse("O_GENCNT");
			String circnt = tableModel.getResponse("O_CIRCNT");
			String emrcnt = tableModel.getResponse("O_EMRCNT");
			String chtire = tableModel.getResponse("O_CHTIRE");

			// Log.i("####","####brkcnt"+brkcnt+"/"+gencnt+"/"+circnt+"/"+emrcnt+"/"+chtire);

			brkcnt = brkcnt == null ? "00" : brkcnt;
			gencnt = gencnt == null ? "00" : gencnt;
			circnt = circnt == null ? "00" : circnt;
			emrcnt = emrcnt == null ? "00" : emrcnt;
			chtire = chtire == null ? "00" : chtire;

			tv_accident.setText(brkcnt);
			tv_normal.setText(gencnt);
			tv_around.setText(circnt);
			tv_emergency.setText(emrcnt);
			tv_tire.setText(chtire);

			// tv_emergency.setText(brkcnt);
			// tv_around.setText(brkcnt);

			if (o_itab1_a.size() > 0) {
				iv_nodata1.setVisibility(View.GONE);
			} else {
				iv_nodata1.setVisibility(View.VISIBLE);
			}
			if (o_itab2_a.size() > 0) {
				iv_nodata2.setVisibility(View.GONE);
			} else {
				iv_nodata2.setVisibility(View.VISIBLE);
			}

			if (o_itab1_a == null || o_itab1_a.size() <= 0)
				return;

			hdla = new History_Dialog_Left_Adapter(context,
					R.layout.history_dialog_row, o_itab1_a, new onDoneClick(){

						@Override
						public void onClick(View v, int position) {
							// TODO Auto-generated method stub
							
							kog.e("Jonathan", "정비이력 이동");
							
//							bt_done.performClick();
							
							
							Intent in = new Intent(context, Menu2_1_Activity.class);

							
							String mode = o_itab1_a.get(position).get("MNTYPNM");
							if (mode.equals("사고")) {
								in.putExtra("MODE", "A");
							} else if (mode.equals("일반") ) {
								in.putExtra("MODE", "B");
							} else if (mode.equals("긴급")) {
								in.putExtra("MODE", "C");
							} else if (mode.equals("타이어")) {
								in.putExtra("MODE", "E");
							} else {

								EventPopupC epc = new EventPopupC(context);
								try {
									epc.show("정비유형이 일반/사고/긴급/타이어 일경우만 가능합니다.");
								} catch (Exception e){
									e.printStackTrace();
								}
								return;
								
								
							}
							
							Set<String> set = o_itab2_a.get(position).keySet();
							Iterator<String> it = set.iterator();
							String key;

							while (it.hasNext()) {
								key = it.next();
								kog.e("Jonathan", "aaaaaaaa key ===  " + key	+ "    value  === " + o_itab2_a.get(position).get(key));
							}
							
//							Set<String> set1 = o_itab1_c.get(position).keySet();
//							Iterator<String> it1 = set1.iterator();
//							String key1;
//
//							while (it1.hasNext()) {
//								key1 = it1.next();
//								kog.e("Jonathan", "aaaaaaaa key ===  " + key1	+ "    value  === " + o_itab1_c.get(position).get(key1));
//							}
							
							Set<String> set2 = o_itab1_a.get(position).keySet();
							Iterator<String> it2 = set2.iterator();
							String key2;

							while (it2.hasNext()) {
								key2 = it2.next();
								kog.e("Jonathan", "aaaaaaaa key ===  " + key2	+ "    value  === " + o_itab1_a.get(position).get(key2));
							}
							
							
//							Set<String> set3 = o_itab1_b.get(position).keySet();
//							Iterator<String> it3 = set3.iterator();
//							String key3;
//
//							while (it3.hasNext()) {
//								key3 = it3.next();
//								kog.e("Jonathan", "aaaaaaaa key ===  " + key3	+ "    value  === " + o_itab1_b.get(position).get(key3));
//							}
							
							
							in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
//							in.putExtra("MODE", o_itab1_a.get(position).get("MNTYPNM"));
							in.putExtra("CUSNAM", I_CUSNAM);
							in.putExtra("AUFNR", o_itab1_a.get(position).get("AUFNR"));
//							o_itab1_a.get(num).get("AUFNR");
							in.putExtra("INVNR", I_INVNR);		// 차량번호
							in.putExtra("EQUNR", I_EQUNR);
							in.putExtra("KUNNR", I_KUNNR);
							in.putExtra("is_CarManager", LoginActivity.mPermgp); 
							
							
							
							kog.e("Jonathan", "I_MODE :: " + in.getStringExtra("MODE") + " I_CUSNAM :: " + I_CUSNAM + " AUFNR :: " + o_itab1_a.get(position).get("AUFNR") + " INVNR :: " + I_INVNR + " EQUNR :: " + I_EQUNR + " KUNNR :: " + I_KUNNR );
							
							

							((Activity)(context)).finish();
							context.startActivity(in);
							
							
							
							
//							hdla.setCheckPosition(position);
//							showRightLayout(position);
						}
			});
			lv_maintenance_left.setAdapter(hdla);
			
			lv_maintenance_left
					.setOnItemClickListener(new OnItemClickListener() {
						@Override
						public void onItemClick(AdapterView<?> arg0, View arg1,
								int position, long arg3) {
							hdla.setCheckPosition(position);
							showRightLayout(position);
						}
					});
			showRightLayout(hdla.getCheckPosition());
			
			
			cc.duplicateLogin(mContext);
			
		}

		else if (FuntionName.equals("ZMO_1070_RD01")) {
			TAB2 = false;
			o_itab1_b = tableModel.getTableArray();
			if (o_itab1_b.size() > 0) {
				iv_nodata1.setVisibility(View.GONE);
			} else {
				iv_nodata1.setVisibility(View.VISIBLE);
			}
			if (o_itab1_b == null || o_itab1_b.size() <= 0)
				return;
			
			hdta = new History_Dialog_Tire_Adapter(context,
					R.layout.history_dialog_row, o_itab1_b, new onDoneTireClick(){

						@Override
						public void onClick(View v, int position) {
							
							kog.e("Jonathan", "타이어 이동");
							
							bt_done.performClick();
							// TODO Auto-generated method stub
//							hdta.setCheckPosition(position);
//							showTireRightLayout(position);
						}
			});
			lv_tire.setAdapter(hdta);
			
//			hdta = new History_Dialog_Tire_Adapter(context,
//					R.layout.history_dialog_row, o_itab1_b);
			lv_tire.setAdapter(hdta);
			lv_tire.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int position, long arg3) {
					hdta.setCheckPosition(position);
					showTireRightLayout(position);
				}
			});
			showTireRightLayout(hdta.getCheckPosition());
			cc.duplicateLogin(mContext);
			
		}

		else if (FuntionName.equals("ZMO_1040_RD02")) {
			
			TAB3 = false;
			o_itab1_c = tableModel.getTableArray();
			if (o_itab1_c.size() > 0) {
				iv_nodata1.setVisibility(View.GONE);
			} else {
				iv_nodata1.setVisibility(View.VISIBLE);
			}
			if (o_itab1_c == null || o_itab1_c.size() <= 0)
				return;
			hdtra = new History_Dialog_Transfer_Adapter(context,
					R.layout.history_dialog_transfer_row, o_itab1_c);
			lv_transfer.setAdapter(hdtra);
			
			cc.duplicateLogin(mContext);
			
			// lv_transfer.setOnItemClickListener(new OnItemClickListener()
			// {
			// @Override
			// public void onItemClick(AdapterView<?> arg0, View arg1, int
			// position, long arg3)
			// {
			// hdta.setCheckPosition(position);
			// showTireRightLayout(position);
			// }
			// });
			// showTireRightLayout(hdta.getCheckPosition());
		}
	}

	private void showTireRightLayout(int num) {

		HashMap hm = o_itab1_b.get(num);
		tv_tiresize_front.setText(hm.get("MAKTX").toString());
		// tv_tiresize_rear .setText(hm.get("MATNR").toString());
		tv_tire_receipt.setText(hm.get("RECDT").toString());
		tv_tire_mileage.setText(hm.get("INCML").toString() + " Km");
		tv_tire_change_num.setText(hm.get("CHGQTY").toString());
		tv_tire_front_left.setText(hm.get("FROLFT").toString());
		tv_tire_front_right.setText(hm.get("FRORHT").toString());
		tv_tire_rear1_left.setText(hm.get("BAKLFT1").toString());
		tv_tire_rear1_right.setText(hm.get("BAKRHT1").toString());
		tv_tire_rear2_left.setText(hm.get("BAKLFT2").toString());
		tv_tire_rear2_right.setText(hm.get("BAKRHT2").toString());
		tv_tire_spare.setText(hm.get("SPARE").toString());
	}

	private void showRightLayout(int num) {
		String key = o_itab1_a.get(num).get("AUFNR");
		tv_aufnr.setText(key);
		tv_incml.setText(o_itab1_a.get(num).get("INCML"));
		tv_lifnrnm.setText(o_itab1_a.get(num).get("LIFNRNM"));

		ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
		for (int i = 0; i < o_itab2_a.size(); i++) {
			if (key.equals(o_itab2_a.get(i).get("AUFNR"))) {
				list.add(o_itab2_a.get(i));
			}
		}

		hdda = new History_Dialog_Detail_Adapter(context,
				R.layout.history_dialog_detail_row, list);
		lv_maintenance_detail.setAdapter(hdda);
	}

	@Override
	public void reDownloadDB(String newVersion) {
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.history_done_id:
			try {
				dismiss();
			} catch (Exception e){
				e.printStackTrace();
			}
			break;
		case R.id.partstransfer_parts_search_close_id: // 닫기
			try {
				dismiss();
			} catch (Exception e){
				e.printStackTrace();
			}
			break;
		case R.id.history_carnum_id: // 자동차번호판
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
						tv_carnum.setText(str);
						try {
							cd.dismiss();
						} catch (Exception e){
							e.printStackTrace();
						}
					}
				}
			});
			cd.show();
			break;
		case R.id.history_search_id:
			Object data = tv_carnum.getText();
			String str = data == null || data.toString().equals("") ? "" : data
					.toString();

			if (CARNUM.equals(str))
				return;
			CARNUM = str;

			showProgress("조회중 입니다.");
			if (rb[0].isChecked()) {
				// cc.getZMO_1020_RD03(o_struct1.get("EQUNR"), str);
				cc.getZMO_1020_RD03(" ", str);
			} else if (rb[1].isChecked()) {
				cc.getZMO_1070_RD01(str);
			} else if (rb[2].isChecked()) {
				// cc.getZMO_1040_RD02(o_struct1.get("EQUNR"), str);
				cc.getZMO_1040_RD02(" ", str);
			} else if (rb[3].isChecked()) {
				// cc.getZMO_1040_RD02(o_struct1.get("EQUNR"), str);
				cc.getZMO_1040_RD02(" ", str);
				queryCallLog(str, NAME);
			}

			break;
		}
	}

	private void queryCallLog(String carNum, String name) {
		TAB4 = false;
		String[] _whereArgs = { carNum, name };
		String[] _whereCause = { "INVNR", "DRIVN" };

		String[] colums = null;

		DbQueryModel dbQueryModel = new DbQueryModel(
				DEFINE.CALL_LOG_TABLE_NAME, _whereCause, _whereArgs, colums);

		DbAsyncTask dbAsyncTask = new DbAsyncTask("queryCallLog", mContext,
				new DbAsyncResLintener() {

					@Override
					public void onCompleteDB(String funName, int type,
							Cursor cursor, String tableName) {
						// TODO Auto-generated method stub
						if (cursor == null)
							return;
						cursor.moveToFirst();

						ArrayList<CallLogModel> array = new ArrayList<CallLogModel>();

						while (!cursor.isAfterLast()) {

							String date = cursor.getString(cursor
									.getColumnIndex("DATE"));
							String time = cursor.getString(cursor
									.getColumnIndex("TIME"));
							String _type = cursor.getString(cursor
									.getColumnIndex("TYPE"));
							String tel = cursor.getString(cursor
									.getColumnIndex("TEL"));

							CallLogModel callLogModel = new CallLogModel(date,
									time, _type, tel);

							array.add(callLogModel);

							cursor.moveToNext();
						}

						cursor.close();
						
						
//						if(kog.TEST)
//						{
//							String date = "2014/02/18"; 
//							String time = "10:00"; 
//							String __type = "나는 테스트임";
//							String tel = "010-2063-0705";
//							CallLogModel callLogModel2 = new CallLogModel(date, time, __type, tel);
//							
//							String _tel2 = "010-206-0705";
//							CallLogModel callLogModel3 = new CallLogModel(date, time, __type, _tel2);
//							array.add(callLogModel2);
//							array.add(callLogModel3);
//						}
						
						
						if (array.size() > 0)
							iv_nodata4.setVisibility(View.GONE);
						
						
						mCallogAdapter.setData(array);
						mCallogAdapter.notifyDataSetChanged();
					}
				}, dbQueryModel);

		dbAsyncTask.execute(DbAsyncTask.DB_SELECT);
	}
}
