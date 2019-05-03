package com.ktrental.fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
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
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

import com.ktrental.R;
import com.ktrental.adapter.CustomerSearchFragmentAdapter;
import com.ktrental.adapter.Customer_Search_Dialog_Adapter;
import com.ktrental.cm.connect.ConnectController;
import com.ktrental.cm.connect.Connector.ConnectInterface;
import com.ktrental.cm.db.DbAsyncTask;
import com.ktrental.cm.db.DbAsyncTask.DbAsyncResLintener;
import com.ktrental.common.DEFINE;
import com.ktrental.common.KtRentalApplication;
import com.ktrental.dialog.Address_Change_Dialog;
import com.ktrental.dialog.Camera_Dialog;
import com.ktrental.dialog.Customer_Search_Dialog;
import com.ktrental.dialog.Duedate_Dialog;
import com.ktrental.dialog.History_Dialog;
import com.ktrental.dialog.Related_Phone_Dialog;
import com.ktrental.dialog.Tire_List_Dialog;
import com.ktrental.fragment.TransferManageFragment.OnDismissDialogFragment;
import com.ktrental.model.BaseMaintenanceModel;
import com.ktrental.model.CarInfoModel;
import com.ktrental.model.DbQueryModel;
import com.ktrental.model.LoginModel;
import com.ktrental.model.MaintenanceModel;
import com.ktrental.model.O_ITAB1;
import com.ktrental.model.TableModel;
import com.ktrental.popup.CallSendPopup2;
import com.ktrental.popup.EventPopupC;
import com.ktrental.popup.SMSPopup2;
import com.ktrental.product.VocInfoActivity;
import com.ktrental.util.CommonUtil;
import com.ktrental.util.OnChangeFragmentListener;
import com.ktrental.util.SharedPreferencesUtil;
import com.ktrental.util.kog;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class CustomerSearchFragment extends BaseResultFragment implements
		OnClickListener, ConnectInterface, OnDismissDialogFragment {

	private LinearLayout customersearch_bottom_layout;
	LoginModel model = KtRentalApplication.getLoginModel();

	private Context context;
	private ConnectController cc;
	private ProgressDialog pd;
	private RadioGroup rg;
	private RadioButton rb[] = new RadioButton[3];
	private LinearLayout tab[];
	private TextView tv_carnum;
	private EditText et_name;
	private Button bt_search;
	private ArrayList<HashMap<String, String>> rd04_arr;
	private CustomerSearchFragmentAdapter csfa;
	private ListView lv_customer;
	private TextView tv_maktx;
	private TextView tv_oiltypnm;
	private TextView tv_dlsm1;
	private TextView tv_vipgbn;
	private TextView tv_dlst1;
	private TextView tv_tele1;
	private TextView tv_add1;
	private Customer_Search_Dialog csd;
	private TextView tv_vbeln;
	private TextView tv_owner;
	private TextView tv_cngbn;
	private TextView tv_saledm;
	private TextView tv_onbrnm;
	private TextView tv_cnbrnm;
	private TextView tv_cirdam;
	// private TextView tv_wtsdt;
	// private TextView tv_wtfdt;
	private TextView tv_wtmon;
	private TextView tv_wtsdt_wtfdt;
	private TextView tv_inbdt;
	private TextView tv_ldwamt;
	private TextView tv_crspec;
	private TextView tv_sanpm;
	private TextView tv_cycmnt;
	private TextView tv_tirgbn;
	private TextView tv_dchagbn;
	private TextView tv_gengbn;
	private TextView tv_inpdt;
	private TextView tv_tirflat;
	private TextView tv_gchlqty;
	private TextView tv_snwtir;
	private TextView tv_chain;
	private TextView tv_name1;
	private TextView tv_scadynnm;
	private TextView tv_insad;
	private TextView tv_insed;
	private TextView tv_ocfcdnm;
	private TextView tv_drvacdnm;
	private TextView tv_suinscnm;
	private Button bt_sms;
	private Button bt_call1;
	private Button bt_call2;
	private Button bt_edit;
	private Address_Change_Dialog acd;
	private HashMap<String, String> o_struct2;
	private HashMap<String, String> o_struct1;
	private HashMap<String, String> o_struct3;
	private HashMap<String, String> o_struct4;
	private Button bt_tire_change;
	private Button bt_tire_list;
	private Button bt_maintenance;
	private Button bt_history;
	private Button bt_contacts;
	private Button bt_transfermanage;
	private ImageView iv_nodata;
	// private MaintenanceModel md;
	private int mPosition;
	String P2 = "P2";
	
	String strVocNum;



	private TextView tv_mamager_name;
	private TextView tv_mamager_phonenum;
	private EditText customer_manager_send;
	private Button bt_customer_send;

	public CustomerSearchFragment(){}
	// private TransferManageFragment mTransferManageFragment;
	public CustomerSearchFragment(String className,
			OnChangeFragmentListener changeFragmentListener) {
		super(className, changeFragmentListener);
	}

	@Override
	public void onAttach(Activity activity) {
		context = activity;
		cc = new ConnectController(this, context);
		pd = new ProgressDialog(context);
		pd.setCancelable(false);
		super.onAttach(activity);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View v = inflater.inflate(R.layout.customersearchfragment, null);

		customersearch_bottom_layout = (LinearLayout) v
				.findViewById(R.id.customersearch_bottom_layout);
		rg = (RadioGroup) v.findViewById(R.id.customer_rg_id);
		rb[0] = (RadioButton) v.findViewById(R.id.customer_rb_id1);
		rb[1] = (RadioButton) v.findViewById(R.id.customer_rb_id2);
		rb[2] = (RadioButton) v.findViewById(R.id.customer_rb_id3);
		tab = new LinearLayout[rg.getChildCount()];
		tab[0] = (LinearLayout) v.findViewById(R.id.customer_basic_info);
		tab[1] = (LinearLayout) v.findViewById(R.id.customer_contract_info);
		tab[2] = (LinearLayout) v.findViewById(R.id.customer_option_info);
		tv_carnum = (TextView) v.findViewById(R.id.customer_carnum_id);

		et_name = (EditText) v.findViewById(R.id.customer_name_id);
		bt_search = (Button) v.findViewById(R.id.customer_search_id);
		bt_search.setOnClickListener(this);

		lv_customer = (ListView) v.findViewById(R.id.customer_list_id);
		tv_maktx = (TextView) v.findViewById(R.id.customer_maktx_id);
		tv_oiltypnm = (TextView) v.findViewById(R.id.customer_oiltypnm_id);
		tv_dlsm1 = (TextView) v.findViewById(R.id.customer_dlsm1_id);
		tv_vipgbn = (TextView) v.findViewById(R.id.customer_vipgbn_id);
		tv_dlst1 = (TextView) v.findViewById(R.id.customer_dlst1_id);
		tv_tele1 = (TextView) v.findViewById(R.id.customer_telf1_id);
		tv_add1 = (TextView) v.findViewById(R.id.customer_add1_id);
		tv_vbeln = (TextView) v.findViewById(R.id.customer_vbeln_id);
		tv_owner = (TextView) v.findViewById(R.id.customer_owner_id);
		tv_cngbn = (TextView) v.findViewById(R.id.customer_cngbn_id);
		tv_saledm = (TextView) v.findViewById(R.id.customer_saledm_id);
		tv_onbrnm = (TextView) v.findViewById(R.id.customer_onbrnm_id);
		tv_cnbrnm = (TextView) v.findViewById(R.id.customer_cnbrnm_id);
		tv_cirdam = (TextView) v.findViewById(R.id.customer_cirdam_id);
		// tv_wtsdt = (TextView)v.findViewById(R.id.customer_wtsdt_id);
		// tv_wtfdt = (TextView)v.findViewById(R.id.customer_wtfdt_id);
		tv_wtmon = (TextView) v.findViewById(R.id.customer_wtmon_id);
		tv_wtsdt_wtfdt = (TextView) v
				.findViewById(R.id.customer_wtsdt_wtfdt_id);
		tv_inbdt = (TextView) v.findViewById(R.id.customer_inbdt_id);
		tv_ldwamt = (TextView) v.findViewById(R.id.customer_ldwamt_id);
		tv_crspec = (TextView) v.findViewById(R.id.customer_crspec_id);
		tv_sanpm = (TextView) v.findViewById(R.id.customer_sanpm_id);
		tv_cycmnt = (TextView) v.findViewById(R.id.customer_cycmnt_id);
		tv_tirgbn = (TextView) v.findViewById(R.id.customer_tirgbn_id);
		tv_dchagbn = (TextView) v.findViewById(R.id.customer_dchagbn_id);
		tv_gengbn = (TextView) v.findViewById(R.id.customer_gengbn_id);
		tv_inpdt = (TextView) v.findViewById(R.id.customer_inpdt_id);
		tv_tirflat = (TextView) v.findViewById(R.id.customer_tirflat_id);
		tv_gchlqty = (TextView) v.findViewById(R.id.customer_gchlqty_id);
		tv_snwtir = (TextView) v.findViewById(R.id.customer_snwtir_id);
		tv_chain = (TextView) v.findViewById(R.id.customer_chain_id);
		tv_name1 = (TextView) v.findViewById(R.id.customer_name1_id);
		tv_scadynnm = (TextView) v.findViewById(R.id.customer_scadynnm_id);
		tv_insad = (TextView) v.findViewById(R.id.customer_insad_id);
		tv_insed = (TextView) v.findViewById(R.id.customer_insed_id);
		tv_ocfcdnm = (TextView) v.findViewById(R.id.customer_ocfcdnm_id);
		tv_drvacdnm = (TextView) v.findViewById(R.id.customer_drvacdnm_id);
		tv_suinscnm = (TextView) v.findViewById(R.id.customer_suinscnm_id);
		bt_sms = (Button) v.findViewById(R.id.customer_sms_id);
		bt_call1 = (Button) v.findViewById(R.id.customer_call_id1);
		bt_call2 = (Button) v.findViewById(R.id.customer_call_id2);
		bt_edit = (Button) v.findViewById(R.id.customer_edit_id);
		bt_sms.setOnClickListener(this);
		bt_call1.setOnClickListener(this);
		bt_call2.setOnClickListener(this);
		bt_edit.setOnClickListener(this);
		bt_tire_change = (Button) v.findViewById(R.id.customer_tire_change_id);
		bt_tire_change.setOnClickListener(this);
		bt_tire_list = (Button) v.findViewById(R.id.customer_tire_list_id);
		bt_tire_list.setOnClickListener(this);
		bt_maintenance = (Button) v.findViewById(R.id.customer_maintanance_id);
		bt_maintenance.setOnClickListener(this);
		bt_history = (Button) v.findViewById(R.id.customer_history_id);
		bt_history.setOnClickListener(this);
		bt_contacts = (Button) v.findViewById(R.id.customer_contacts_id);
		bt_contacts.setOnClickListener(this);
		bt_transfermanage = (Button) v
				.findViewById(R.id.customer_transfermanage_id);
		bt_transfermanage.setOnClickListener(this);
		iv_nodata = (ImageView) v.findViewById(R.id.list_nodata_id);


		tv_mamager_name = (TextView)v.findViewById(R.id.tv_mamager_name);
		tv_mamager_phonenum = (TextView)v.findViewById(R.id.tv_mamager_phonenum);
		customer_manager_send =(EditText)v.findViewById(R.id.customer_manager_send);
		bt_customer_send = (Button)v.findViewById(R.id.bt_customer_send);
		bt_customer_send.setOnClickListener(this);




		v.findViewById(R.id.btn_tire_voc_info_id).setOnClickListener(this);
		
		setTab();
		setTabVisible(0);
		setEditTextEnter();

		// kog.e("Jonathan", "검색할때 받아오는것 CarManger :: "+
		// getActivity().getIntent().getStringExtra("is_CarManager"));

		if (P2.equals(getActivity().getIntent().getStringExtra("is_CarManager"))) // 카매니저일
																					// Jonathan
																					// 14.06.20
																					// 경우.
		{
			kog.e("Jonathan", "카매니저 입니다.");
			// bt_sms.setVisibility(View.GONE);
			// bt_call1.setVisibility(View.GONE);
			// bt_call2.setVisibility(View.GONE);

			bt_maintenance.setVisibility(View.GONE);
			// bt_history
			// bt_tire_change
			// bt_tire_list
			// bt_contacts
			bt_transfermanage.setVisibility(View.GONE);

			// customersearch_bottom_layout.setVisibility(View.GONE);
		}

		return v;
	}

	private void setEditTextEnter() {
		tv_carnum.setOnKeyListener(new OnKeyListener() {

			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if (event.getAction() == KeyEvent.ACTION_DOWN
						&& keyCode == KeyEvent.KEYCODE_ENTER) {
					// Log.i("#", "###" + "et_carnum");
					clickSearch();
					return true;
				}
				return false;
			}
		});
		et_name.setOnKeyListener(new OnKeyListener() {

			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if (event.getAction() == KeyEvent.ACTION_DOWN
						&& keyCode == KeyEvent.KEYCODE_ENTER) {
					// Log.i("#", "###" + "et_name");
					clickSearch();
					return true;
				}
				return false;
			}
		});
		tv_carnum.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus)
					et_name.setText("");
			}
		});
		et_name.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus)
					tv_carnum.setText("");
			}
		});

		// myung 20131121 ADD 차량번호 입력 후 엔터 키 터치 시 줄바꿈이 아닌 조회를 실행하도록 수정 필요.
		tv_carnum.setOnEditorActionListener(new OnEditorActionListener() {

			@Override
			public boolean onEditorAction(TextView v, int actionId,
					KeyEvent event) {
				if (actionId == EditorInfo.IME_ACTION_SEARCH) {
					clickSearch();
					return true;
				} else {
					return true;
				}
			}
		});

		// myung 20131121 ADD 고객명 입력 후 엔터 키 터치 시 줄바꿈이 아닌 조회를 실행하도록 수정 필요.
		et_name.setOnEditorActionListener(new OnEditorActionListener() {

			@Override
			public boolean onEditorAction(TextView v, int actionId,
					KeyEvent event) {
				if (actionId == EditorInfo.IME_ACTION_SEARCH) {
					clickSearch();
					return true;
				} else {
					return true;
				}
			}
		});
	}

	private void setTab() {
		rg.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				switch (checkedId) {
				case R.id.customer_rb_id1:
					setTabVisible(0);
					break;
				case R.id.customer_rb_id2:
					// myung 20131202 UPDATE 계약정보 & 정비옵션정보 탭 클릭 시
					// "고객조회를 먼저 실행해 주세요." 메시지 출력 후 이동 안함.
					if (o_struct1 == null) {
						setTabVisible(0);
						EventPopupC epc = new EventPopupC(context);
						epc.show("고객조회를 먼저 실행해 주세요.");
						return;
					}
					setTabVisible(1);
					break;
				case R.id.customer_rb_id3:
					// myung 20131202 UPDATE 계약정보 & 정비옵션정보 탭 클릭 시
					// "고객조회를 먼저 실행해 주세요." 메시지 출력 후 이동 안함.
					if (o_struct1 == null) {
						setTabVisible(0);
						EventPopupC epc = new EventPopupC(context);
						epc.show("고객조회를 먼저 실행해 주세요.");
						return;
					}
					setTabVisible(2);
					break;
				}
			}
		});
	}

	private void setTabVisible(int num) {
		
		kog.e("Jonathan", "setTabVisible :: " + num);
		
		for (int i = 0; i < tab.length; i++) {
			tab[i].setVisibility(View.GONE);
		}
		tab[num].setVisibility(View.VISIBLE);
		rb[num].setChecked(true);
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
			
			Toast.makeText(context, resultText, Toast.LENGTH_SHORT).show();
			return;
		}
		if (FuntionName.equals("ZMO_1060_RD04")) {
			// pre.sendEmptyMessage(0);
			rd04_arr = tableModel.getTableArray();
			if (rd04_arr.size() > 0) {
				iv_nodata.setVisibility(View.GONE);
			} else {
				iv_nodata.setVisibility(View.VISIBLE);
			}
			for (int i = 0; i < rd04_arr.size(); i++) {
				// Log.i("", "####" + rd04_arr.get(i).get("NAME1"));
			}
			// Log.i("####", "####" + "setO_ITAB1() 실");
			setO_ITAB1();
			
		} else if (FuntionName.equals("ZMO_1060_RD03")) {
			// Log.i("####", "####" + "고객정보 조회 리스폰");
			setTabVisible(0);

			// Jonathan 14.09.16 미치겠다..ㅡㅡ;;

			customer_manager_send.setText("");
			
			o_struct1 = tableModel.getStruct("O_STRUCT1");
			o_struct2 = tableModel.getStruct("O_STRUCT2");
			o_struct3 = tableModel.getStruct("O_STRUCT3");
			o_struct4 = tableModel.getStruct("O_STRUCT4");
			strVocNum = tableModel.getResponse("E_VOCNUM");
			Set<String> set = o_struct1.keySet();
			Iterator<String> it = set.iterator();
			String key;

			while (it.hasNext()) {
				key = it.next();
				kog.e("Jonathan", "Hello Jonathan key ===  " + key
						+ "    value  === " + o_struct1.get(key));
			}

			set = o_struct2.keySet();
			it = set.iterator();

			while (it.hasNext()) {
				key = it.next();
				kog.e("Jonathan", "Hello Jonathan key ===  " + key
						+ "    value  === " + o_struct2.get(key));
			}

			set = o_struct3.keySet();
			it = set.iterator();

			while (it.hasNext()) {
				key = it.next();
				kog.e("Jonathan", "Hello Jonathan key ===  " + key
						+ "    value  === " + o_struct3.get(key));
			}

			set = o_struct4.keySet();
			it = set.iterator();

			while (it.hasNext()) {
				key = it.next();
				kog.e("Jonathan", "Hello Jonathan key ===  " + key
						+ "    value  === " + o_struct4.get(key));
			}

			if (!o_struct1.containsKey("MDLCD")) {
				// clickSearch();
				cc.getZMO_1060_RD03(rd04_arr.get(mPosition).get("INVNR"),
						rd04_arr.get(mPosition).get("EQUNR"));
			} else {
				tv_maktx.setText(o_struct1.get("MAKTX"));
				tv_oiltypnm.setText(o_struct1.get("OILTYPNM"));
				tv_dlsm1.setText(o_struct1.get("DLSM1"));
				tv_vipgbn.setText(o_struct1.get("VIPGBN"));
				String dlst1 = o_struct1.get("DLST1");
				tv_dlst1.setText(o_struct1.get("DLST1"));
				if (dlst1.startsWith("010") || dlst1.startsWith("011")
						|| dlst1.startsWith("016") || dlst1.startsWith("017")
						|| dlst1.startsWith("018") || dlst1.startsWith("019")) {
					bt_sms.setEnabled(true);
				} else {
					bt_sms.setEnabled(false);
				}
				tv_tele1.setText(o_struct1.get("TELF1"));
				// tv_zip.setText (o_struct1.get("CUSJUSO"));
				tv_add1.setText(o_struct1.get("CUSJUSO"));
				// tv_add2.setText (o_struct1.get("CUSJUSO"));
				tv_vbeln.setText(o_struct2.get("VBELN"));
				tv_owner.setText(o_struct2.get("OWNER"));
				tv_cngbn.setText(o_struct2.get("CNGBN"));
				tv_saledm.setText(o_struct2.get("SALEDM"));
				tv_onbrnm.setText(o_struct2.get("ONBRNM"));
				tv_cnbrnm.setText(o_struct2.get("CNBRNM"));
				tv_cirdam.setText(o_struct2.get("CIRDAM"));
				// Log.i("###", "####정비담당자명 wtsdt"+o_struct2.get("WTSDT"));
				// tv_wtsdt.setText (o_struct2.get("WTSDT"));
				// Log.i("###", "####정비담당자연락처 wtsdt"+o_struct2.get("WTFDT"));
				// tv_wtfdt.setText (o_struct2.get("WTFDT"));
				String str = o_struct2.get("WTMON").toString();
				// myung 20131202 UPDATE 계약정보 위탁총기간 00036 -> 36 개월로 변경. 우측정렬
				str = Integer.valueOf(str).toString();
				tv_wtmon.setText(str);
				str = o_struct2.get("WTSDT").toString();
				String str2 = o_struct2.get("WTFDT").toString();
				tv_wtsdt_wtfdt.setText(getDateFormat(str) + " ~ "
						+ getDateFormat(str2));
				str = o_struct2.get("INBDT").toString();
				tv_inbdt.setText(getDateFormat(str));

				// myung 20131202 UPDATE 계약정보 LDW 금액으로 변환 200,000 원 형식으로 표기.
				// 우측정렬
				str = o_struct2.get("LDWAMT");
				if (str.equals("") || str.equals(" ")) {
					tv_ldwamt.setText("");
				} else {
					DecimalFormat df = new DecimalFormat("#,##0");
					str = str.replace(" ", "");
					str = df.format(Integer.valueOf(str));
					tv_ldwamt.setText(str);
				}

				tv_crspec.setText(o_struct2.get("CRSPEC"));
				tv_sanpm.setText(o_struct3.get("SANPM"));
				tv_cycmnt.setText(o_struct3.get("CYCMNT"));
				tv_tirgbn.setText(o_struct3.get("TIRGBN"));
				tv_dchagbn.setText(o_struct3.get("DCHAGBN"));

				// myung 20131202 UPDATE 일반정비 값 변경(Y -> 가입, N-> 미가입)
				String tempYN = o_struct3.get("GENGBN"); // Jonathan 14.10.31
															// 정비옵션정보 Y 를 가입으로
															// 바꾸는것 찾음.
				if (tempYN.equals("Y"))
					tempYN = "가입";
				else if (tempYN.equals("N"))
					tempYN = "미가입";
				tv_gengbn.setText(tempYN);

				str = o_struct3.get("INPDT").toString();
				tv_inpdt.setText(getDateFormat(str));
				// myung 20131202 UPDATE 타이어펑크 값 변경(Y -> 가입, N-> 미가입)
				tempYN = o_struct3.get("TIRFLAT");
				if (tempYN.equals("Y"))
					tempYN = "가입";
				else if (tempYN.equals("N"))
					tempYN = "미가입";
				tv_tirflat.setText(tempYN);

				tv_gchlqty.setText(o_struct3.get("GCHLQTY"));

				// 최지연 차장 수정 요청 20151222 
				// 숫자값으로 내려온다고 함 모지 -_- 2년동안 몰른겨?
				// 0 : 미가입 1 이상 : 가입
				
				// myung 20131202 UPDATE 타이어펑크 값 변경(Y -> 가입, N-> 미가입)
				tempYN = o_struct3.get("SNWTIR");
				if (tempYN.equals("Y"))
					tempYN = "가입";
				else if (tempYN.equals("N"))
					tempYN = "미가입";

//				tempYN = o_struct3.get("SNWTIR");
//				if(tempYN.equals("0"))
//					tempYN = "미가입";
//				else
//					tempYN = "미가입";
				
				// 이건 모지 -_-.. 위에서 값 적용함... 제거(20151222) 
//				tv_tirflat.setText(tempYN);
//				((TextView)getView().findViewById(R.id.customer_snwtir_ea_id)).setText(o_struct3.get("SNWTIR") + " EA");
				tv_snwtir.setText(tempYN);
				tv_chain.setText(o_struct3.get("CHAIN"));
				tv_name1.setText(o_struct4.get("NAME1"));
				tv_scadynnm.setText(o_struct4.get("SCADYNNM"));

				String insad = o_struct4.get("INSAD");
				tv_insad.setText(getDateFormat(insad));
				String insed = o_struct4.get("INSED");
				tv_insed.setText(getDateFormat(insed));
				tv_ocfcdnm.setText(o_struct4.get("OCFCDNM"));
				tv_drvacdnm.setText(o_struct4.get("DRVACDNM"));
				tv_suinscnm.setText(o_struct4.get("SUINSCNM"));


				tv_mamager_name.setText(o_struct2.get("ENAME_2"));
				tv_mamager_phonenum.setText(o_struct2.get("CELLNO_2"));



				((TextView) getView().findViewById(R.id.tire_voc_info_id)).setText(strVocNum != null ? strVocNum : "");
				
				cc.duplicateLogin(mContext);
				
			}
			

			// myung 20131231 ADD 이관 받은 차량이 리스트에 표기되지 않음
		} else if (FuntionName.equals("ZMO_1040_WR02")) {
			EventPopupC epc = new EventPopupC(context);
			epc.show("업데이트 되었습니다.");
			
//			cc.duplicateLogin(mContext);
			
		}
		else if (FuntionName.equals("ZMO_1060_WR02")) {
			EventPopupC epc = new EventPopupC(context);
			epc.show("메시지를 보냈습니다.");

			cc.duplicateLogin(mContext);

		}
		// myung 20131231 UPDATE 이관 받은 차량이 리스트에 표기되지 않음
		else if (FuntionName.equals("ZMO_1040_WR01")) { // 2013.12.05 ypkim
														// EventPopupC epc = new
														// EventPopupC(context);
														// epc.show("이관받기가 완료되어 동기화를 진행 합니다.");
			if (isNetwork())
				repairPlanWork(); // ZMO_1020_RD02_2
			
			
		} else if (FuntionName.equals("ZMO_1020_RD02_2")) {
			hideProgress();
			// myung 20131231 ADD DB취득
			// KtRentalApplication.getInstance().queryMaintenacePlan();
			// KtRentalApplication.changeRepair();

			setTabVisible(0);

			ArrayList<HashMap<String, String>> arrayVocNum;
			arrayVocNum = tableModel.getTableArray();

			cc.getZMO_1060_RD03(rd04_arr.get(mPosition).get("INVNR"), rd04_arr
					.get(mPosition).get("EQUNR"));
		}
	}

	private void repairPlanWork() {
		showProgress("순회정비 일정을 동기화 중입니다.");

		SharedPreferencesUtil sharedPreferencesUtil = SharedPreferencesUtil
				.getInstance();

		String SyncStr = " ";

		// myung 20131231 DEL TEST
		// -----------------------------------------------------------------
		if (sharedPreferencesUtil.isSuccessSyncDB()) {
			// String id = SharedPreferencesUtil.getInstance().getId();
			// if (mFirstId.equals(id)) {
			// SyncStr = " ";
			// } else {
			// SyncStr = "A";
			// sharedPreferencesUtil.setSyncSuccess(false);
			// }
			SyncStr = " ";
			// loginSuccess(); // 순회정비계획 싱크가 느려 우선은 로그인성공으로 바로가게 한다.
			// return;
		} else {

			SyncStr = "A";
			sharedPreferencesUtil.setSyncSuccess(false);
		}

		LoginModel model = KtRentalApplication.getLoginModel();
		// runUiThread(UiRunnable.MODE_PROGRESS_SHOW, "순회정비 리스트를 확인 중 입니다.");
		ConnectController connectController = new ConnectController(this,
				mContext);
		// 테이블 데이타를 얻어온다.
		connectController.getRepairPlan(model.getPernr(), SyncStr, mContext);
		// -----------------------------------------------------------------

		// myung 20131231 ADD TEST
		// -----------------------------------------------------------------
		// final ConnectController connectController = new
		// ConnectController(this,
		// mContext);
		// dropRepairTables(new DbAsyncResLintener() {
		//
		// @Override
		// public void onCompleteDB(String funName, int type,
		// Cursor cursor, String tableName) {
		// // TODO Auto-generated method stub
		// String SyncStr = "A";
		// sharedPreferencesUtil.setSyncSuccess(false);
		// // runUiThread(UiRunnable.MODE_PROGRESS_SHOW,
		// // "순회정비 리스트를 확인 중 입니다.");
		// // Log.d("HONG", "model.getPernr() " + model.getPernr());
		// // 테이블 데이타를 얻어온다.
		// LoginModel model = KtRentalApplication.getLoginModel();
		// connectController.getRepairPlan(model.getPernr(), SyncStr,
		// mContext);
		// sharedPreferencesUtil.setLastLoginId(model.getPernr());
		// }
		// });
		// -----------------------------------------------------------------

	}

	// myung 201311231 ADD TEST
	private void dropRepairTables(DbAsyncResLintener lintener) {
		TableModel tableModel = new TableModel("");
		ArrayList<String> dropTables = new ArrayList<String>();
		// dropTables.add(DEFINE.O_ITAB1_TABLE_NAME);
		dropTables.add(DEFINE.ADDRESS_TABLE);
		dropTables.add(DEFINE.REPAIR_TABLE_NAME);
		dropTables.add(DEFINE.STOCK_TABLE_NAME);
		DbAsyncTask asyncTask = new DbAsyncTask(
				ConnectController.REPAIR_FUNTION_NAME, O_ITAB1.TABLENAME,
				mContext, lintener, // DbAsyncResListener
				tableModel, dropTables);
		asyncTask.execute(DbAsyncTask.DB_DROP_TABLES);
	}

	// myung 201311231 ADD TEST
	private void dropAllTables(DbAsyncResLintener lintener) {
		TableModel tableModel = new TableModel("");
		ArrayList<String> dropTables = new ArrayList<String>();
		// dropTables.add(DEFINE.O_ITAB1_TABLE_NAME);
		dropTables.add(DEFINE.LOGIN_TABLE_NAME);
		dropTables.add(DEFINE.ADDRESS_TABLE);
		dropTables.add(DEFINE.CAR_MASTER_TABLE_NAME);
		dropTables.add(DEFINE.REPAIR_TABLE_NAME);
		dropTables.add(DEFINE.STOCK_TABLE_NAME);
		DbAsyncTask asyncTask = new DbAsyncTask(
				ConnectController.REPAIR_FUNTION_NAME, O_ITAB1.TABLENAME,
				mContext, lintener, // DbAsyncResListener
				tableModel, dropTables);
		asyncTask.execute(DbAsyncTask.DB_DROP_TABLES);
	}

	public String getDateFormat(String date) {
		StringBuffer sb = new StringBuffer(date);
		if (date.length() == 8) {
			sb.insert(4, ".");
			int last = sb.length() - 2;
			sb.insert(last, ".");
		}
		return sb.toString();
	}

	private void setO_ITAB1() {
		csfa = new CustomerSearchFragmentAdapter(context,
				R.layout.customer_search_row, rd04_arr);
		lv_customer.setAdapter(csfa);
		lv_customer.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				showProgress("고객정보 조회 중입니다.");
				if (rd04_arr.size() <= 0) {
					hideProgress();
					return;
				}
				mPosition = position;
				csfa.setPosition(position);
				// .
				// 정보로드
				// cc.getZMO_1060_RD03(rd04_arr.get(position).get("INVNR"));
				cc.getZMO_1060_RD03(rd04_arr.get(position).get("INVNR"),
						rd04_arr.get(position).get("EQUNR"));
			}
		});
		// 정보로드
		showProgress("고객정보 조회 중입니다.");
		if (rd04_arr.size() <= 0) {
			hideProgress();
			return;
		}
		cc.getZMO_1060_RD03(rd04_arr.get(0).get("INVNR"),
				rd04_arr.get(0).get("EQUNR"));
		// Log.i("####", "####" + "고객정보 조회 보냄");
	}

	@Override
	public void reDownloadDB(String newVersion) {
	}

	@Override
	public void onClick(View arg0) {
		Object data;
		String str;
		Uri uri;
		Intent intent;
		// int position = csfa.getPosition();

		
		switch (arg0.getId()) {
		case R.id.customer_search_id:
			clickSearch();
			break;
		case R.id.customer_sms_id:
			// 어플설치여부 확인

			if (aviliableSMS(context)) {
				data = tv_dlst1.getText();
				String phone = data == null || data.toString().equals("")
						|| data.toString().equals(" ") ? "" : data.toString();
				// phone = "010-5155-9000"; //#### 테스트 지워야함
				// Log.i("####", "####"+phone);
				// myung 20131217 UPDATE SMS 발송 시 고객 전화번호가 없을 경우 메시지 출력 후 리턴 시킴.
				if (phone.equals("") || phone.equals(" ")) {
					EventPopupC epc = new EventPopupC(context);
					epc.show("연락처가 없습니다. \n고객 연락처를 확인하여 주십시요.");
				} else if (!phone.substring(0, 2).equals("01")) {
					EventPopupC epc = new EventPopupC(context);
					epc.show("SMS를 발신할 수 없는 전화번호 입니다.");
				} else {
					HashMap hm = rd04_arr.get(csfa.getPosition());
					SMSPopup2 sms = new SMSPopup2(context);
					sms.show(hm.get("INVNR").toString(), hm.get("NAME1")
							.toString(), phone);
				}
			} else {
				EventPopupC epc = new EventPopupC(context);
				epc.show("문자전송 어플이 설치되어있지 않습니다.");
			}

			break;
		case R.id.customer_call_id1:
			if (aviliableCALL(context)) {
				data = tv_dlst1.getText();
				String phone1 = data == null || data.toString().equals("")
						|| data.toString().equals(" ") ? "" : data.toString();
				data = tv_tele1.getText();
				String phone2 = data == null || data.toString().equals("")
						|| data.toString().equals(" ") ? "" : data.toString();
				// phone1 = "010-5155-9000"; //#### 테스트 지워야함
				// phone2 = "010-5454-3000"; //#### 테스트 지워야함

				if (phone1.equals("") && phone2.equals("")) {
					EventPopupC epc = new EventPopupC(context);
					// myung 20131217 UPDATE SMS 발송 시 고객 전화번호가 없을 경우 메시지 출력 후 리턴
					// 시킴.
					epc.show("연락처가 없습니다. \n고객 연락처를 확인하여 주십시요.");
				} else {
					HashMap hm = rd04_arr.get(csfa.getPosition());
					CallSendPopup2 sms = new CallSendPopup2(context);
					sms.show(phone1, phone2);

				}
			} else {
				EventPopupC epc = new EventPopupC(context);
				epc.show("전화어플이 설치되어있지 않습니다.");
			}
			// try {
			// data = tv_dlst1.getText();
			// str = data != null ? data.toString() : "";
			// uri = Uri.parse("tel:" + str);
			// intent = new Intent(Intent.ACTION_DIAL, uri);
			// startActivity(intent);
			// } catch (Exception e) {
			// EventPopupC epc = new EventPopupC(context);
			// epc.show("전화어플이 설치되어있지 않습니다.");
			// e.printStackTrace();
			// }
			break;
		case R.id.customer_call_id2:
			// 어플설치여부 확인
			try {
				data = tv_tele1.getText();
				str = data != null ? data.toString() : "";
				uri = Uri.parse("tel:" + str);
				intent = new Intent(Intent.ACTION_DIAL, uri);
				startActivity(intent);
			} catch (Exception e) {
				EventPopupC epc = new EventPopupC(context);
				epc.show("전화어플이 설치되어있지 않습니다.");
				e.printStackTrace();
			}
			break;
		case R.id.customer_edit_id:

			// o_struct1 여기에다가 Address_Change_Dialog로 데이터를 넘겨주는데
			// 필요한 컬럼값 즉 해쉬맵key를 동기화해서 넘겨줘야한다.
			// Address_Change_Dialog로에서는 o_struct1받으면 커서가 0인곳에서
			// 내가 제대로 mapping를 해야한다.
			data = tv_add1.getText();
			str = data != null ? data.toString() : "";
			if (o_struct1 == null) {
				EventPopupC epc = new EventPopupC(context);
				epc.show("조회후 변경가능합니다.");
				return;
			}
			acd = new Address_Change_Dialog(context, o_struct1);
			Button bt_save = (Button) acd
					.findViewById(R.id.address_change_save_id);
			bt_save.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// LoginModel lm = KtRentalApplication.getLoginModel();
					// LoginModel에서 INGRP 가져와야함 추가중.
					if (acd.getCity1() == null || (acd.getCity1() == "")) {
						EventPopupC epc = new EventPopupC(mContext);
						epc.show("주소를 선택해 주세요");
						return;
					}
					// myung 20131211 UPDATE 첫번째 param "120"을 로그인 시 받아온 INGRP정보로
					// 세팅
					// cc.setZMO_1040_WR02("120", getTable());
					cc.setZMO_1040_WR02(getTable());
					acd.dismiss();
				}
			});
			acd.show();
			break;
		case R.id.customer_tire_change_id: // 타이어교체
			// 프레그먼트 변경

			if (rd04_arr == null || rd04_arr.size() <= 0) {
				EventPopupC epc = new EventPopupC(context);
				epc.show("고객 조회를 먼저 실행해 주세요.");
				return;
			}

			// Jonathan 14.06.23 추가
			if (P2.equals(getActivity().getIntent().getStringExtra(
					"is_CarManager"))) {
				TireFragment mainFragment;
				// EditText mEditDummy;
				int position = csfa.getPosition();

				HashMap<String, String> hm = rd04_arr.get(position);
				String carnum = hm.get("INVNR");
				String equnr = hm.get("EQUNR");

				mainFragment = new TireFragment(TireFragment.class.getName(),
						null, carnum, equnr);

				getActivity().getSupportFragmentManager().beginTransaction()
						// .attach(mainFragment)
						.replace(R.id.layout_call_fragment, mainFragment)
						.commit();

				// layout_call_fragment 여기로 fragment 붙여야 함.

			} else {
				int position = csfa.getPosition();

				HashMap<String, String> hm = rd04_arr.get(position);
				String carnum = hm.get("INVNR");
				String equnr = hm.get("EQUNR");
				changfragment(new TireFragment(TireFragment.class.getName(),
						null, carnum, equnr));

			}

			// model.getModelMap().put("BUKRS", "3000");
			// Intent in = new Intent(this, Menu4_1_Activity.class);
			// in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
			// | Intent.FLAG_ACTIVITY_SINGLE_TOP);
			// startActivity(in);
			// overridePendingTransition(0, 0);
			// finish();

			kog.e("Jonathan", "Jonathan 이쪽 오나요?");
			break;

		case R.id.customer_tire_list_id:
			// myung 20131121 ADD 타이어 신청내역 버튼 클릭 시 "고객 조회를 먼저 실행해 주세요."
			// 메시지 출력 후 화면 이동 하면 안됨.
			if (o_struct1 == null || o_struct1.size() <= 0) {
				EventPopupC epc = new EventPopupC(context);
				epc.show("고객 조회를 먼저 실행해 주세요.");
				return;
			}

			// Set <String> set = o_struct1.keySet();
			// Iterator <String> it = set.iterator();
			// String key;
			//
			// while(it.hasNext())
			// {
			// key = it.next();
			// kog.e("Jonathan", "1어디보자~! key ===  " + key + "    value  === " +
			// o_struct1.get(key));
			// }
			//
			// set = o_struct2.keySet();
			// it = set.iterator();
			//
			// while(it.hasNext())
			// {
			// key = it.next();
			// kog.e("Jonathan", "2어디보자~! key ===  " + key + "    value  === " +
			// o_struct2.get(key));
			// }
			//
			// set = o_struct3.keySet();
			// it = set.iterator();
			//
			// while(it.hasNext())
			// {
			// key = it.next();
			// kog.e("Jonathan", "3어디보자~! key ===  " + key + "    value  === " +
			// o_struct3.get(key));
			// }

			Tire_List_Dialog tld = new Tire_List_Dialog(context, o_struct1);

			tld.show();
			break;
		case R.id.customer_maintanance_id: // 정비등록
			// Toast.makeText(context, "정비등록[n4h3d]", 0).show();
			if (rd04_arr == null) {
				EventPopupC epc = new EventPopupC(context);
				epc.show("고객 조회를 먼저 실행해 주세요.");
				return;
			}

			if (o_struct3.get("MATMA").equals("F") || o_struct3.get("MATMA").equals("G")) {
				EventPopupC epc = new EventPopupC(context);
				epc.show("IoT 건은 긴급정비를 실행할 수 없습니다.");
				return;
			}
			kog.e("Jonathan", "과연 고객명일까요?  " + rd04_arr.get(0).get("NAME1"));
			// myung 20131230 UPDATE
			// queryMaintenace(rd04_arr.get(0).get("NAME1"),
			// o_struct1.get("INVNR"));
			kog.e("Jonathan", "정비등록 누를때 MDLCD :: " + o_struct1.get("MDLCD"));
			queryMaintenace(o_struct1.get("DLSM1"), o_struct1.get("INVNR"));

			// Set <String> set = o_struct1.keySet();
			// Iterator <String> it = set.iterator();
			// String key;
			//
			// while(it.hasNext())
			// {
			// key = it.next();
			// kog.e("Jonathan", " 정비등록 눌렀을때  key ===" + key + "    value  === "
			// + o_struct1.get(key));
			// }
			//

			break;
		case R.id.customer_history_id: // 정비이력
			if (o_struct1 == null || o_struct1.size() <= 0) {
				EventPopupC epc = new EventPopupC(context);
				epc.show("고객 조회를 먼저 실행해 주세요.");
				return;
			}
			History_Dialog hd = new History_Dialog(context, o_struct1, 0);
			hd.show();
			break;
		case R.id.customer_contacts_id: // 관련연락처
			if (rd04_arr == null) {
				EventPopupC epc = new EventPopupC(context);
				epc.show("고객 조회를 먼저 실행해 주세요.");
				return;
			}
			Related_Phone_Dialog rpd = new Related_Phone_Dialog(context,
					o_struct1);
			rpd.show();
			break;

		case R.id.customer_carnum_id:
			final Camera_Dialog cd = new Camera_Dialog(context);
			Button cd_done = (Button) cd.findViewById(R.id.camera_done_id);
			final EditText et_carnum = (EditText) cd
					.findViewById(R.id.camera_carnum_id);
			cd_done.setOnClickListener(new OnClickListener() {
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
						cd.dismiss();
					}
				}
			});
			cd.show();

			break;

		case R.id.customer_transfermanage_id:
			if (rd04_arr == null) {
				EventPopupC epc = new EventPopupC(context);
				epc.show("고객 조회를 먼저 실행해 주세요.");
				return;
			}

			if (o_struct1.get("CURDAE") != null) {
				if (!o_struct1.get("CURDAE").equals("X")) {
					EventPopupC epc = new EventPopupC(context);
					epc.show("당월 순회 대상이 아닙니다.");
					return;
				}
			}

			clickTransfer();
			break;




			case R.id.bt_customer_send:

				sendSMS();


				break;




		case R.id.btn_tire_voc_info_id: {
			
			if(o_struct1 == null || o_struct1.size() == 0)
			{
				EventPopupC epc = new EventPopupC(context);
				epc.show("고객 정보가 없습니다.");
				return;
			}
			
			String Kunnr = o_struct1.get("KUNNR");
			Intent intent1 = new Intent(getActivity(), VocInfoActivity.class);
			intent1.addFlags(intent1.FLAG_ACTIVITY_CLEAR_TOP
					| intent1.FLAG_ACTIVITY_SINGLE_TOP);
			intent1.putExtra(VocInfoActivity.VOC_KUNNR, Kunnr);
			startActivity(intent1);
		}
			break;
		}
	}




	private void sendSMS() {
		String name = tv_mamager_name.getText().toString();
		String phoneNumber = tv_mamager_phonenum.getText().toString();
//		String name = "테스트";
//		String phoneNumber = "010-5459-6362";
		String VBELN = o_struct2.get("VBELN").toString();
		String EQUNR = o_struct1.get("EQUNR").toString();
		String INVNR = o_struct1.get("INVNR").toString();



		if("".equals(phoneNumber.replaceAll(" ", "")))
		{
			EventPopupC epc = new EventPopupC(context);
			epc.show("메시지를 보낼 담당자 번호가 없습니다.");

			cc.duplicateLogin(mContext);
		}
		else
		{
			String message = customer_manager_send.getText().toString();
			SmsManager sms = SmsManager.getDefault();
			sms.sendTextMessage(phoneNumber, null, message, null, null);

			kog.e("Joanthan", " name : " + name);
			kog.e("Joanthan", " phoneNumber " + phoneNumber);
			kog.e("Joanthan", " VBELN : " + VBELN);
			kog.e("Joanthan", " EQUNR : " + EQUNR);
			kog.e("Joanthan", " INVNR : " + INVNR);

			cc.getZMO_1060_WR02(name, phoneNumber.replaceAll("-",""), VBELN, EQUNR, INVNR);
		}

	}





	// myung 20131202 ADD 이관받기 버튼 추가.
	/**
	 * 이관등록 버튼 클릭
	 */
	private void clickTransfer() {

		// 2013.12.05 ypkim.
		String strBefINVNR = ""; // 이관전 순회차량
		String strBefINGRP = ""; // 이관전 순회차량 MOT
		String strAftINVNR = ""; // 이관후 순회차량 번호

		LoginModel lm = KtRentalApplication.getLoginModel();

		strBefINVNR = o_struct2.get("MINVNR");
		strBefINGRP = o_struct2.get("DEPTNM");

		// Log.i("======= getTransfer =======", "lm.getDeptnm = " +
		// lm.getDeptnm()
		// + ", strBefINGRP = " + strBefINGRP);

		if (!lm.getDeptnm().equals(strBefINGRP)) {
			EventPopupC epc = new EventPopupC(context);
			epc.show("동일 MOT내 이관만 가능합니다.\nMOT를 확인해 주세요.");
			return;
		}

		strAftINVNR = lm.getInvnr();

		if (strAftINVNR.equals(strBefINVNR)) {
			EventPopupC epc = new EventPopupC(context);
			epc.show("이관전 순회차량과 이관후 순회차량이 같습니다.");
			return;
		}

		ArrayList<HashMap<String, String>> array = new ArrayList<HashMap<String, String>>();

		for (int i = 0; i < 1; i++) {
			HashMap<String, String> map = new HashMap<String, String>();
			map.put("EQUNR", o_struct1.get("EQUNR")); // 고객차량 설비번호
			map.put("REQDT", CommonUtil.getCurrentDay()); // 이관 요청일
			map.put("PREINV", strBefINVNR); // 이관전 순회차량
			map.put("POSINV", strAftINVNR); // 이관후 순회차량
			map.put("GSTRS", CommonUtil.getCurrentDay()); // 순회정비일자

			// Log.i("======= getTransfer =======",
			// "EQUNR = " + o_struct1.get("EQUNR") + ", PREINV = "
			// + strBefINVNR + ", POSINV = " + strAftINVNR);

			array.add(map);
		}

		ConnectController connectController = new ConnectController(this,
				mContext);
		connectController.getTransfer(array);

	}

	private BaseMaintenanceModel setBaseMaintenanceModel() {
		String customer_name = rd04_arr.get(0).get("NAME1"); // Jonathan
																// 14.06.18 주석
																// 풀었음. 고객명.
		String driver_name = o_struct1.get("DLSM1"); // Jonathan 14.06.18
														// driver_name 으로 바꿈.
		// String _name = rd04_arr.get(0).get("NAME1"); // 고객명

		String _carNum = o_struct1.get("INVNR");
		String _address = o_struct1.get("CUSJUSO");
		String _tel = o_struct1.get("DLST1");
		String _time = "";
		String _carname = o_struct1.get("MAKTX");
		String _progress_status = "E0002";
		String _day = "" + CommonUtil.getCurrentDay();
		String AUFNR = "";
		String _EQUNR = o_struct1.get("EQUNR");
		String _CTRTY = "";
		String postCode = "";
		String city = "";
		String street = "";
		String _drv_mob = o_struct1.get("DLST1");
		String gueen2 = "";

		// ////////////////////
		String txt30 = "";
		// ////////////////////

		String MDLCD = o_struct1.get("MDLCD");

		String VOCNUM = o_struct1.get("VOCNUM");

		String KUNNR = o_struct1.get("KUNNR");

		// 2017-06-02. hjt. 지연일수 추가
		String DELAY = "0";
		try {
			DELAY = o_struct1.get("DELAY");
		} catch (Exception e){
			e.printStackTrace();
		}

		String apm = o_struct1.get("APM");

		String vbeln = o_struct1.get("VBELN");

		String gubun = o_struct1.get("GUBUN");

		String reqNo = o_struct1.get("REQNO");

		String atvyn = o_struct1.get("ATVYN");

		String prerq = o_struct1.get("PRERQ");

		String ccmrq = o_struct1.get("CCMRQ");

		String minvnr = o_struct1.get("MINVNR");

		kog.e("Jonathan", "Hello Jonathan2 :: " + VOCNUM);

		BaseMaintenanceModel model = new BaseMaintenanceModel(customer_name,
				driver_name, _carNum, _address, _tel, _time, _carname,
				_progress_status, _day, AUFNR, _EQUNR, _CTRTY, postCode, city,
				street, _drv_mob, gueen2, txt30, MDLCD, VOCNUM, KUNNR, DELAY, null, apm, vbeln, gubun, reqNo, atvyn, prerq, ccmrq, minvnr);

		return model;
	}

	public boolean aviliableSMS(Context context) {
		PackageManager pac = context.getPackageManager();
		Uri smsUri = Uri.parse("sms:");
		Intent smsIntent = new Intent(Intent.ACTION_SENDTO, smsUri);
		List list = pac.queryIntentActivities(smsIntent,
				PackageManager.COMPONENT_ENABLED_STATE_DEFAULT);
		ArrayList tempList = new ArrayList();
		int count = list.size();
		String packageName = "";
		for (int i = 0; i < count; i++) {
			ResolveInfo firstInfo = (ResolveInfo) list.get(i);
			packageName = firstInfo.activityInfo.applicationInfo.packageName;
			tempList.add(firstInfo.activityInfo);
			// Log.d("packageName", "packageName = " + packageName);
		}
		if (packageName == null || packageName.equals("")) {
			return false;
		} else {
			return true;
		}
	}

	public boolean aviliableCALL(Context context) {
		PackageManager pac = context.getPackageManager();
		Uri callUri = Uri.parse("tel:");
		Intent callIntent = new Intent(Intent.ACTION_CALL, callUri);
		List list = pac.queryIntentActivities(callIntent,
				PackageManager.COMPONENT_ENABLED_STATE_DEFAULT);
		ArrayList tempList = new ArrayList();
		int count = list.size();
		String packageName = "";
		for (int i = 0; i < count; i++) {
			ResolveInfo firstInfo = (ResolveInfo) list.get(i);
			packageName = firstInfo.activityInfo.applicationInfo.packageName;
			tempList.add(firstInfo.activityInfo);
			// Log.d("packageName", "packageName = " + packageName);
		}
		if (packageName == null || packageName.equals("")) {
			return false;
		} else {
			return true;
		}
	}

	private ArrayList<HashMap<String, String>> getTable() {
		LoginModel lm = KtRentalApplication.getLoginModel();
		ArrayList<HashMap<String, String>> i_itab1 = new ArrayList<HashMap<String, String>>();
		for (int i = 0; i < 1; i++) {
			HashMap<String, String> hm = new HashMap<String, String>();
			hm.put("EQUNR", o_struct1.get("EQUNR"));// 고객차량 설비번호
			hm.put("PREEQU", lm.getEqunr()); // 이전 순회차량 설비번호
			hm.put("POS_POST", acd.getPost()); // 변경후 주소(우편번호) 입력
			hm.put("POS_CITY1", acd.getCity1()); // 변경후 주소(시도명)
			hm.put("POS_STREET", acd.getStreet()); // 변경후 주소(상세주소)
			hm.put("POS_DRIVN", acd.getDrivn()); // 변경후 운전자 이름
			hm.put("POS_TEL_NO", acd.getTel_No()); // 변경후 운전자연락처
			hm.put("POSEQU", acd.getEqunr()); // 이후 순회차량 설비번호
			hm.put("POSINGRP", acd.getMot()); // 변경후 MOT
			hm.put("PRE_POST", acd.getPrePost());
			hm.put("PRE_CITY1", acd.getPreCity());
			hm.put("PRE_STREET", acd.getPreStreet());
			hm.put("PRE_DRIVN", acd.getPreDrivn());
			hm.put("PRE_TEL_NO", acd.getPreTelNo());
			i_itab1.add(hm);
		}
		return i_itab1;
	}

	private void clickSearch() {
		Object data;
		data = tv_carnum.getText();
		String carnum = data != null && !data.toString().equals("") ? data
				.toString() : "";
		data = et_name.getText();
		String name = data != null && !data.toString().equals("") ? data
				.toString() : "";

		// 20131121 UPDATE START Logic 및 Message 변경 "차량번호 또는 고객명을 입력해주세요."
		// if (carnum.length() >= 4) {
		// // 차량번호 4자리, 고객명 넣고 검색
		// showProgress("고객 조회 중입니다.");
		// cc.getZMO_1060_RD04(carnum, name);
		// } else if (name.length() >= 2) {
		// // 고객조회 창띄우기
		// csd = new Customer_Search_Dialog(context, name, this);
		// Button bt_done = (Button) csd
		// .findViewById(R.id.customer_search_save_id);
		// bt_done.setOnClickListener(new View.OnClickListener() {
		// @Override
		// public void onClick(View v) {
		// setCarNumSearch();
		// }
		// });
		// csd.show();
		// } else {
		// EventPopupC epc = new EventPopupC(context);
		// epc.show("차량번호는 4자리를 입력해야합니다.\n고객명은 2자이상 입력하세요.");
		// }

		if (carnum.length() == 0 && name.length() == 0) {
			EventPopupC epc = new EventPopupC(context);
			epc.show("차량번호 또는 고객명을 입력해 주세요.");
			return;
		}
		if (carnum.length() >= 4) {
			// 차량번호 4자리, 고객명 넣고 검색
			showProgress("고객 조회 중입니다.");
			cc.getZMO_1060_RD04(carnum, name);
		} else if (name.length() >= 2) {
			// 고객조회 창띄우기
			csd = new Customer_Search_Dialog(context, name, this);
			Button bt_done = (Button) csd
					.findViewById(R.id.customer_search_save_id);
			bt_done.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					setCarNumSearch();
				}
			});
			csd.show();
		} else if (carnum.length() < 4 && carnum.length() > 0) {
			EventPopupC epc = new EventPopupC(context);
			epc.show("차량번호는 4자리를 입력해야 합니다.");
		} else if (name.length() < 2 && name.length() > 0) {
			EventPopupC epc = new EventPopupC(context);
			epc.show("고객명는 2자 이상을 입력하세요.");
		}

		// 20131121 UPDATE END Logic 및 Message 변경 "차량번호 또는 고객명을 입력해주세요."
	}

	public void setCarNumSearch() {
		Customer_Search_Dialog_Adapter csda = csd.getCsda();
		if (csda == null)
			return;
		ArrayList<HashMap<String, String>> arr_hash = csd.getArray_hash();
		int position = csda.getCheckPosition();
		HashMap<String, String> map = arr_hash.get(position);
		// 차량번호조회
		String kunnr = map.get("KUNNR");
		showProgress("차량정보 조회 중입니다.");
		cc.getZMO_1060_RD04("", kunnr);
		csd.dismiss();
		// pre.sendEmptyMessage(0);
	}

	public void setCarNumSearch1() {
		ArrayList<HashMap<String, String>> arr_hash = csd.getArray_hash();
		int position = 0;
		HashMap<String, String> map = arr_hash.get(position);
		String kunnr = map.get("KUNNR");
		showProgress("차량번호 조회 중입니다.");
		cc.getZMO_1060_RD04("", kunnr);
		csd.dismiss();
		// pre.sendEmptyMessage(0);
	}

	// // 키보드내리기
	// Handler pre = new Handler() {
	//
	// @Override
	// public void handleMessage(Message msg) {
	// super.handleMessage(msg);
	// InputMethodManager imm = (InputMethodManager) context
	// .getSystemService(Context.INPUT_METHOD_SERVICE);
	// imm.hideSoftInputFromWindow(et_carnum.getWindowToken(), 0);
	// }
	// };

	@Override
	protected void movePlan(ArrayList<BaseMaintenanceModel> models) {
		// TODO Auto-generated method stub 안쓰이는 기능으로 추측
		Duedate_Dialog dd = new Duedate_Dialog(context, models, null, null);
		dd.show();
	}

	private void moveResult(Cursor cursor) {
		// Log.d("", "" + cursor.getCount());
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			String time = cursor.getString(cursor
					.getColumnIndex(maintenace_colums[0]));
			String invnr = cursor.getString(cursor
					.getColumnIndex(maintenace_colums[1]));
			String name = cursor.getString(cursor
					.getColumnIndex(maintenace_colums[2]));
			if (name == null || name.equals(" ")) {
				name = cursor.getString(cursor
						.getColumnIndex(maintenace_colums[3]));
			}

			String driver_name = cursor.getString(cursor
					.getColumnIndex(maintenace_colums[3]));

			String carname = cursor.getString(cursor
					.getColumnIndex(maintenace_colums[4]));
			String status = cursor.getString(cursor
					.getColumnIndex(maintenace_colums[5]));
			String postCode = cursor.getString(cursor
					.getColumnIndex(maintenace_colums[6]));
			String city = cursor.getString(cursor
					.getColumnIndex(maintenace_colums[7]));
			String street = cursor.getString(cursor
					.getColumnIndex(maintenace_colums[8]));
			String tel = cursor.getString(cursor
					.getColumnIndex(maintenace_colums[9]));
			String progress_status = cursor.getString(cursor
					.getColumnIndex(maintenace_colums[10]));
			String day = cursor.getString(cursor
					.getColumnIndex(maintenace_colums[11]));
			String aufnr = cursor.getString(cursor
					.getColumnIndex(maintenace_colums[12]));
			String equnr = cursor.getString(cursor
					.getColumnIndex(maintenace_colums[13]));
			String ctrty = cursor.getString(cursor
					.getColumnIndex(maintenace_colums[14]));
			String drv_mob = cursor.getString(cursor
					.getColumnIndex(maintenace_colums[15]));
			String cermr = cursor.getString(cursor
					.getColumnIndex(maintenace_colums[16]));

			String gueen2 = cursor.getString(cursor
					.getColumnIndex(maintenace_colums[17]));

			String txt30 = cursor.getString(cursor
					.getColumnIndex(maintenace_colums[18]));

			String MDLCD = cursor.getString(cursor
					.getColumnIndex(maintenace_colums[19]));

			String VOCNUM = cursor.getString(cursor
					.getColumnIndex(maintenace_colums[20]));

			String kunnr = cursor.getString(cursor
					.getColumnIndex(maintenace_colums[21]));

			String CYCMN_TX = cursor.getString(cursor.getColumnIndex(maintenace_colums[22]));

			String apm = cursor.getString(cursor.getColumnIndex(maintenace_colums[23]));
			String vbeln = cursor.getString(cursor.getColumnIndex(maintenace_colums[24]));
			String gubun = cursor.getString(cursor.getColumnIndex(maintenace_colums[25]));
			String reqNo = cursor.getString(cursor.getColumnIndex(maintenace_colums[26]));
			String atvyn = cursor.getString(cursor.getColumnIndex(maintenace_colums[27]));
			String prerq = cursor.getString(cursor.getColumnIndex(maintenace_colums[28]));
			String ccmrq = cursor.getString(cursor.getColumnIndex(maintenace_colums[29]));
			String minvnr = cursor.getString(cursor.getColumnIndex(maintenace_colums[30]));

			if (time != null) {
				if (time.length() > 4) {
					time = time.substring(0, 4);
					String hour = time.substring(0, 2);
					String sec = time.substring(2, 4);
					time = hour + ":" + sec;
				}
			}
			postCode = decrypt(maintenace_colums[4], postCode);
			city = decrypt(maintenace_colums[5], city);
			street = decrypt(maintenace_colums[6], street);
			MaintenanceModel md = new MaintenanceModel(
					decrypt(maintenace_colums[0], time),
					decrypt(maintenace_colums[2], name),
					decrypt(maintenace_colums[3], driver_name), // Jonathan
																// 14.06.19 추가
					decrypt(maintenace_colums[1], invnr), postCode + city
							+ street, decrypt(maintenace_colums[7], carname),
					decrypt(maintenace_colums[8], status), decrypt(
							maintenace_colums[9], tel), decrypt(
							maintenace_colums[10], progress_status), decrypt(
							maintenace_colums[11], day), decrypt(
							maintenace_colums[12], aufnr), decrypt(
							maintenace_colums[13], equnr), decrypt(
							maintenace_colums[14], ctrty), postCode, city,
					street, decrypt(maintenace_colums[15], drv_mob), decrypt(
							maintenace_colums[15], cermr), gueen2, txt30,
					MDLCD, VOCNUM, kunnr, maintenace_colums[22], CYCMN_TX, apm, vbeln,
					gubun, reqNo, atvyn, prerq, ccmrq, minvnr);

			System.out.println("요기는 모니 -__.... " + "CustomerSearch");

			// MaintenanceModel md = new MaintenanceModel(time,
			// name, invnr,
			// postCode + city + street, carname, status);
			showResultFragment(md);
			cursor.moveToNext();
			// boolean Flag = mAsyncMap.containsKey(funName);
			//
			// if (!Flag) {
			// return;
			// }
		}
		cursor.close();
	}

	private void setCarInfo() {
		String invnr = KtRentalApplication.getLoginModel().getInvnr();
		// myung 20131230 UPDATE
		// String customer_name = rd04_arr.get(0).get("NAME1"); // Jonathan
		// // 14.06.18 주석
		// // 풀었음. 고객명.
		String customer_name = rd04_arr.get(mPosition).get("NAME1"); // Jonathan
																		// 14.06.20
																		// 모든게
																		// "홍길동"이라고
																		// 되어있어서
																		// 된 줄
																		// 알았는데,
																		// 운용에서
																		// 오류
																		// 발견하고
																		// 바꿨음.
		String driver_name = o_struct1.get("DLSM1"); // Jonathan 14.06.18

		// driver_name 으로 바꿈.
		String _carNum = o_struct1.get("INVNR");
		String _address = o_struct1.get("CUSJUSO");
		String _tel = o_struct1.get("DLST1");
		String _time = "";
		String _carname = o_struct1.get("MAKTX");
		String _progress_status = "E0002";
		String _day = "" + CommonUtil.getCurrentDay();
		String AUFNR = "";
		String _EQUNR = o_struct1.get("EQUNR");
		String _CTRTY = "";
		String postCode = o_struct1.get("PSTLZ2"); // 우편번호 - Jonathan 수정
													// 14.06.03
		String city = o_struct1.get("ORT02"); // 주소 - Jonathan 수정 14.06.03
		String street = o_struct1.get("STRAS2"); // 상세주소 - Jonathan 수정 14.06.03
		String _drv_mob = o_struct1.get("DLST1");
		String _drv_tel = o_struct1.get("DLST1");
		String OWNER = o_struct2.get("OWNER");
		String _OILTYPNM = o_struct2.get("OILTYPNM");
		String tireFront = "";
		String tireRear = "";
		String customerName = "";
		String representativeName = "";// 담당자 연락처
		String lastMileage = o_struct1.get("INPML");
		String tourCarNum = "" + invnr;// 순회차량 번호
		String contractNum = o_struct1.get("VBELN");
		String contractCategory = o_struct2.get("CNGBN");
		String businessManager = o_struct2.get("SALEDM");
		String possetionBranch = o_struct2.get("ONBRNM");
		String contractBranch = o_struct2.get("CNBRNM");
		String tourRepresentative = o_struct2.get("CIRDAM");
		String tourContact = "";
		String trustTerm = o_struct2.get("WTSDT");
		String carResistDay = o_struct2.get("INBDT");
		String lDW = o_struct2.get("LDWAMT");
		String maintenanceProducts = o_struct3.get("SANPM");
		String tourMainenancePeriod = o_struct3.get("CYCMNT");
		String nomalTire = o_struct3.get("TIRGBN");
		String rentalService = o_struct3.get("DCHAGBN");
		String nomalMaintenance = o_struct3.get("GENGBN");
		String checkMaintenance = o_struct3.get("INPDT");
		String tireFunk = o_struct3.get("TIRFLAT");
		String emergencyResponseCount = o_struct3.get("GCHLQTY");
		String snowTire = o_struct3.get("SNWTIR");
		String CHNGBN = o_struct3.get("CHAIN");
		String mdlcd = o_struct1.get("MDLCD"); // Jonathan 14.09.11
		String _trustTerm2 = o_struct2.get("WTFDT");
		String oilType = o_struct1.get("OILTYP"); // 오일타입 f,g 등 Jonathan
													// 14.09.11
		String _CEMER = "";
		String GUBUN = "E";
		String GUEEN2 = "";

		// ///////////////////
		String TXT30 = "";
		// ////////////////////

		String VOCNUM = o_struct1.get("VOCNUM"); // Jonathan 14.09.11

		String kunnr = o_struct1.get("KUNNR");

		String DELAY = o_struct1.get("DELAY"); // hjt 지연일수 추가

		// yunseung ... 19년 IoT 추가건
		// 아래 주석된 필드들은 이번 IoT 건 추가 하면서 추가된 필드들인데... 차량 정보를 꾸릴 때 다른 곳에서는 아래 항목들이 필요하지만 여기서는 필요도 없고 가져올 수도 없다고 한다. (SAP 쪽 얘기)
		// 여기서도 정비등록 화면으로 진입할 수 있는데 그때 gubun 값이 필요하므로 gubun 값만 하드코딩으로 A (IoT) 가 아닌 다른 값을 넣어주면 된다.
		// 이 화면에서 정비등록 버튼을 눌렀을 때 정비시작을 하게되는데 IoT 건은 정비가 불가능 하다는 팝업을 띄우면 되는데
		// o_struct3 의 값 중 MATMA 값이 G 또는 F 이므로 두 가지 케이스에서 정비등록 버튼을 막으면 된다.
		// 결론 : 아래 필드들 이곳에서 안쓰임. 지우지 않고 주석처리해놓은 이유는 다른 화면들에서는 저 필드들이 다 들어가있는데 여기서는 carInfo 생성자를 만들 때 다 null 이면 다음 개발자가 헷갈릴까봐..

//		String apm = o_struct1.get("APM");
//		String vbeln = o_struct1.get("VBELN");
		String gubun = " ";
//		String reqNo = o_struct1.get("REQNO");
//		String atvyn = o_struct1.get("ATVYN");
//		String prerq = o_struct1.get("PRERQ");
//		String ccmrq = o_struct1.get("CCMRQ");
//		String minvnr = o_struct1.get("MINVNR");

		Set<String> set1 = o_struct1.keySet();
		Set<String> set2 = o_struct2.keySet();
		Set<String> set3 = o_struct3.keySet();
		Set<String> set4 = o_struct4.keySet();
		Iterator<String> it1 = set1.iterator();
		Iterator<String> it2 = set2.iterator();
		Iterator<String> it3 = set3.iterator();
		Iterator<String> it4 = set4.iterator();
		String key;

		while (it1.hasNext()) {
			key = it1.next();
			kog.e("Jonathan", "여기 중요~!! o_struct1 key ===" + key
					+ "    value  === " + o_struct1.get(key));
		}

		while (it2.hasNext()) {
			key = it2.next();
			kog.e("Jonathan", "여기 중요~!! o_struct2 key ===" + key
					+ "    value  === " + o_struct2.get(key));
		}

		while (it3.hasNext()) {
			key = it3.next();
			kog.e("Jonathan", "여기 중요~!! o_struct3 key ===" + key
					+ "    value  === " + o_struct3.get(key));
		}

		while (it4.hasNext()) {
			key = it4.next();
			kog.e("Jonathan", "여기 중요~!! o_struct4 key ===" + key
					+ "    value  === " + o_struct4.get(key));
		}
		/**
		 * 참고 참고
		 */
		BaseMaintenanceModel model = new BaseMaintenanceModel(customer_name,
				driver_name, _carNum, _address, _tel, _time, _carname,
				_progress_status, _day, AUFNR, _EQUNR, _CTRTY, postCode, city,
				street, _drv_mob, GUEEN2, TXT30, mdlcd, VOCNUM, kunnr, DELAY, null, null, null, gubun, null, null, null, null, null);

		CarInfoModel carInfoModel = new CarInfoModel(customer_name,
				driver_name, _carNum, _address, _tel, _time, _carname,
				_progress_status, _day, customerName, representativeName,
				tourCarNum, tireFront, tireRear, lastMileage, contractNum,
				contractCategory, businessManager, possetionBranch,
				contractBranch, tourRepresentative, tourContact, trustTerm,
				carResistDay, lDW, maintenanceProducts, tourMainenancePeriod,
				nomalTire, rentalService, nomalMaintenance, checkMaintenance,
				tireFunk, emergencyResponseCount, snowTire, mdlcd, oilType,
				AUFNR, _trustTerm2, _OILTYPNM, _EQUNR, "", _CEMER, CHNGBN,
				OWNER, postCode, city, street, _drv_mob, _drv_tel, GUEEN2,
				TXT30, VOCNUM, kunnr, DELAY, null, gubun, null, null, null, null, null);

		carInfoModel.setGUBUN(GUBUN);
		showResultFragment(model, carInfoModel);
	}

	private String[] maintenace_colums = { DEFINE.GSUZS, DEFINE.INVNR,
			DEFINE.KUNNR_NM, DEFINE.DRIVN, DEFINE.MAKTX, DEFINE.CCMRQ,
			DEFINE.POST_CODE, DEFINE.CITY, DEFINE.STREET, DEFINE.DRV_TEL,
			DEFINE.CCMSTS, DEFINE.GSTRS, DEFINE.AUFNR, DEFINE.EQUNR,
			DEFINE.CTRTY, DEFINE.DRV_MOB, DEFINE.CEMER, DEFINE.GUEEN2,
			DEFINE.TXT30, DEFINE.MDLCD, DEFINE.VOCNUM, DEFINE.KUNNR, DEFINE.DELAY,
			DEFINE.CYCMN_TX, DEFINE.APM, DEFINE.VBELN, DEFINE.GUBUN, DEFINE.REQNO,
			DEFINE.ATVYN, DEFINE.PRERQ, DEFINE.CCMRQ, DEFINE.MINVNR};

	private void queryMaintenace(String DriverName, String CarNum) {
		// CommonUtil.showCallStack();
		showProgress("순회 정비계획을 조회 중입니다.");
		String[] _whereArgs = { CarNum };
		String[] _whereCause = { DEFINE.INVNR };
		String[] colums = maintenace_colums;
		DbQueryModel dbQueryModel = new DbQueryModel(
				ConnectController.REPAIR_TABLE_NAME, _whereCause, _whereArgs,
				colums);
		dbQueryModel
				.setOrderBy("gubun DESC, GSUZS ASC, case CCMSTS   when 'E0001' then '1' when 'E0002' then '' when 'E0003' then '3' when 'E0004' then '4' else '9' end ");
		DbAsyncTask dbAsyncTask = new DbAsyncTask(DriverName, mContext,
				new DbAsyncResLintener() {

					@Override
					public void onCompleteDB(String funName, int type,
							Cursor cursor, String tableName) {
						hideProgress();
						if (cursor == null) {
							kog.e("Jonathan", "cursor. == null");
							return;
						}
						if (cursor.getCount() == 0) {
							kog.e("Jonathan", "cursor.getCount()");
							setCarInfo();
						} else {
							kog.e("Jonathan", "cursor.else");
							moveResult(cursor);
						}

					}
				}, dbQueryModel);
		dbAsyncTask.execute(DbAsyncTask.DB_SELECT);
	}

	@Override
	public void onDismissDialogFragment() {
		// TODO Auto-generated method stub

	}

}
