package com.ktrental.dialog;

import android.app.ProgressDialog;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.Editable;
import android.text.TextWatcher;
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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ktrental.R;
import com.ktrental.adapter.Address_Dialog_Adapter;
import com.ktrental.beans.CM011;
import com.ktrental.cm.connect.ConnectController;
import com.ktrental.cm.connect.Connector.ConnectInterface;
import com.ktrental.cm.db.DbAsyncTask.DbAsyncResLintener;
import com.ktrental.common.DEFINE;
import com.ktrental.model.TableModel;
import com.ktrental.popup.AddressStructurePopup;
import com.ktrental.popup.EventPopupC;

import java.util.ArrayList;
import java.util.HashMap;

public class Address_Dialog extends DialogC implements
		android.widget.PopupWindow.OnDismissListener, DbAsyncResLintener,
		View.OnClickListener, ConnectInterface {

	private Context context;
	private Window w;
	private WindowManager.LayoutParams lp;
	private RadioGroup tab;
	private RadioButton[] rb_tab = new RadioButton[2];
	private LinearLayout[] tab_ll = new LinearLayout[2];
	private Button bt_jibun_search;
	private Button bt_doro_search;
	private TextView tv_jibun_address;
	private TextView tv_doro_address;
	private ImageView bt_close;
	private AddressStructurePopup structure_popup;
	private Button bt_jibun_structure;
	private Button bt_doro_structure;
	private Button bt_address_gen;
	private ProgressDialog pd;
	private ConnectController connectController;
	private ListView lv_gen_address;

	private int[] jibun_r = { R.id.address_layout_a, R.id.address_layout_a_1,
			R.id.address_layout_b, R.id.address_layout_b_1,
			R.id.address_layout_c, R.id.address_layout_c_1,
			R.id.address_layout_d, R.id.address_layout_d_1,
			R.id.address_layout_e };

	private int[] doro_r = { R.id.address_layout_j, R.id.address_layout_j_1,
			R.id.address_layout_k, R.id.address_layout_k_1,
			R.id.address_layout_l, R.id.address_layout_l_1,
			R.id.address_layout_m, R.id.address_layout_m_1,
			R.id.address_layout_n };

	private RelativeLayout[] jibun_layout = new RelativeLayout[jibun_r.length];
	private RelativeLayout[] doro_layout = new RelativeLayout[doro_r.length];

	private int[] a_r = { R.id.address_layout_a_et_apt_id,
			R.id.address_layout_a_et_dong_id, R.id.address_layout_a_et_ho_id };
	private int[] a1_r = { R.id.address_layout_a_1_et_bunji_id,
			R.id.address_layout_a_1_et_apt_id,
			R.id.address_layout_a_1_et_dong_id,
			R.id.address_layout_a_1_et_ho_id };
	private int[] b_r = { R.id.address_layout_b_et_office_id,
			R.id.address_layout_b_et_dong_id, R.id.address_layout_b_et_ho_id };
	private int[] b1_r = { R.id.address_layout_b_1_et_bunji_id,
			R.id.address_layout_b_1_et_office_id,
			R.id.address_layout_b_1_et_dong_id,
			R.id.address_layout_b_1_et_ho_id };
	private int[] c_r = { R.id.address_layout_c_et_villa_id,
			R.id.address_layout_c_et_dong_id, R.id.address_layout_c_et_ho_id };
	private int[] c1_r = { R.id.address_layout_c_1_et_bunji_id,
			R.id.address_layout_c_1_et_villa_id,
			R.id.address_layout_c_1_et_dong_id,
			R.id.address_layout_c_1_et_ho_id };
	private int[] d_r = { R.id.address_layout_d_et_building_id,
			R.id.address_layout_d_et_dong_id,
			R.id.address_layout_d_et_story_id, R.id.address_layout_d_et_ho_id,
			R.id.address_layout_d_et_name_id };
	private int[] d1_r = { R.id.address_layout_d_1_et_building_id,
			R.id.address_layout_d_1_et_dong_id,
			R.id.address_layout_d_1_et_story_id,
			R.id.address_layout_d_1_et_ho_id,
			R.id.address_layout_d_1_et_name_id };
	private int[] e_r = { R.id.address_layout_e_et_bunji_id,
			R.id.address_layout_e_et_name_id,
			R.id.address_layout_e_et_detail_id };
	private int[] j_r = { R.id.address_layout_j_et_dong_id,
			R.id.address_layout_j_et_ho_id, R.id.address_layout_j_et_detail_id };
	private int[] j1_r = { R.id.address_layout_j_1_et_apt_id,
			R.id.address_layout_j_1_et_dong_id,
			R.id.address_layout_j_1_et_ho_id,
			R.id.address_layout_j_1_et_detail_id };
	private int[] k_r = { R.id.address_layout_k_et_dong_id,
			R.id.address_layout_k_et_ho_id, R.id.address_layout_k_et_detail_id };
	private int[] k1_r = { R.id.address_layout_k_1_et_office_id,
			R.id.address_layout_k_1_et_dong_id,
			R.id.address_layout_k_1_et_ho_id,
			R.id.address_layout_k_1_et_detail_id };
	private int[] l_r = { R.id.address_layout_l_et_dong_id,
			R.id.address_layout_l_et_ho_id, R.id.address_layout_l_et_detail_id };
	private int[] l1_r = { R.id.address_layout_l_1_et_villa_id,
			R.id.address_layout_l_1_et_dong_id,
			R.id.address_layout_l_1_et_ho_id,
			R.id.address_layout_l_1_et_detail_id };
	private int[] m_r = { R.id.address_layout_m_et_dong_id,
			R.id.address_layout_m_et_story_id, R.id.address_layout_m_et_ho_id,
			R.id.address_layout_m_et_name_id,
			R.id.address_layout_m_et_detail_id };
	private int[] m1_r = { R.id.address_layout_m_1_et_building_id,
			R.id.address_layout_m_1_et_dong_id,
			R.id.address_layout_m_1_et_story_id,
			R.id.address_layout_m_1_et_ho_id,
			R.id.address_layout_m_1_et_name_id,
			R.id.address_layout_m_1_et_detail_id };
	private int[] n_r = { R.id.address_layout_n_et_name_id,
			R.id.address_layout_n_et_detail_id };

	private EditText[] et_a = new EditText[a_r.length];
	private EditText[] et_a1 = new EditText[a1_r.length];
	private EditText[] et_b = new EditText[b_r.length];
	private EditText[] et_b1 = new EditText[b1_r.length];
	private EditText[] et_c = new EditText[c_r.length];
	private EditText[] et_c1 = new EditText[c1_r.length];
	private EditText[] et_d = new EditText[d_r.length];
	private EditText[] et_d1 = new EditText[d1_r.length];
	private EditText[] et_e = new EditText[e_r.length];
	private EditText[] et_j = new EditText[j_r.length];
	private EditText[] et_j1 = new EditText[j1_r.length];
	private EditText[] et_k = new EditText[k_r.length];
	private EditText[] et_k1 = new EditText[k1_r.length];
	private EditText[] et_l = new EditText[l_r.length];
	private EditText[] et_l1 = new EditText[l1_r.length];
	private EditText[] et_m = new EditText[m_r.length];
	private EditText[] et_m1 = new EditText[m1_r.length];
	private EditText[] et_n = new EditText[n_r.length];

	private TextView tv_full_address;

	private String JIBUN_CITY = "";
	private String JIBUN_STREET = "";

	private String DORO_CITY = "";
	private String DORO_STREET = "";

	private final int JIBUN = 98675423;
	private final int DORO = 98352983;
	private int JIBUN_DORO_MODE = 0;

	private int JIBUN_MODE = 0;
	private int DORO_MODE = 0;

	private ImageView iv_nodata;

	public Address_Dialog(Context context) {
		super(context);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.address_dialog);
		w = getWindow();

		lp = (WindowManager.LayoutParams) w.getAttributes();
		lp.width = LayoutParams.MATCH_PARENT;
		lp.height = LayoutParams.MATCH_PARENT;
		w.setAttributes(lp);
		// w.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
		w.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
		this.context = context;

		initViews();
		initButtons();
		initStructure();

		JIBUN_DORO_MODE = JIBUN;
	}

	private void initViews() {
		tab = (RadioGroup) findViewById(R.id.address_tab_id);
		rb_tab[0] = (RadioButton) findViewById(R.id.address_jibun_tab);
		rb_tab[1] = (RadioButton) findViewById(R.id.address_doro_tab);
		tab_ll[0] = (LinearLayout) findViewById(R.id.address_jibun_tab_tab);
		tab_ll[1] = (LinearLayout) findViewById(R.id.address_doro_tab_tab);
		bt_jibun_search = (Button) findViewById(R.id.address_jibun_search_id);
		bt_jibun_search.setOnClickListener(this);
		bt_doro_search = (Button) findViewById(R.id.address_doro_search_id);
		bt_doro_search.setOnClickListener(this);
		tv_jibun_address = (TextView) findViewById(R.id.address_jibun_address_id);
		tv_doro_address = (TextView) findViewById(R.id.address_doro_address_id);
		bt_close = (ImageView) findViewById(R.id.address_close_bt_id);
		bt_close.setOnClickListener(this);
		bt_jibun_structure = (Button) findViewById(R.id.address_jibun_structure_id);
		bt_jibun_structure.setOnClickListener(this);
		bt_doro_structure = (Button) findViewById(R.id.address_doro_structure_id);
		bt_doro_structure.setOnClickListener(this);
		tv_full_address = (TextView) findViewById(R.id.address_popup_full_address_id);
		bt_address_gen = (Button) findViewById(R.id.address_popup_address_gen_id);
		bt_address_gen.setOnClickListener(this);
		pd = new ProgressDialog(context);
		pd.setMessage("검색중입니다.");
		connectController = new ConnectController(this, context);
		lv_gen_address = (ListView) findViewById(R.id.address_gen_lv_id);

		for (int i = 0; i < jibun_layout.length; i++) {
			jibun_layout[i] = (RelativeLayout) findViewById(jibun_r[i]);
		}
		for (int i = 0; i < doro_layout.length; i++) {
			doro_layout[i] = (RelativeLayout) findViewById(doro_r[i]);
		}
		for (int i = 0; i < et_a.length; i++) {
			et_a[i] = (EditText) findViewById(a_r[i]);
			et_a[i].addTextChangedListener(watcher);
		}
		for (int i = 0; i < et_a1.length; i++) {
			et_a1[i] = (EditText) findViewById(a1_r[i]);
			et_a1[i].addTextChangedListener(watcher);
		}
		for (int i = 0; i < et_b.length; i++) {
			et_b[i] = (EditText) findViewById(b_r[i]);
			et_b[i].addTextChangedListener(watcher);
		}
		for (int i = 0; i < et_b1.length; i++) {
			et_b1[i] = (EditText) findViewById(b1_r[i]);
			et_b1[i].addTextChangedListener(watcher);
		}
		for (int i = 0; i < et_c.length; i++) {
			et_c[i] = (EditText) findViewById(c_r[i]);
			et_c[i].addTextChangedListener(watcher);
		}
		for (int i = 0; i < et_c1.length; i++) {
			et_c1[i] = (EditText) findViewById(c1_r[i]);
			et_c1[i].addTextChangedListener(watcher);
		}
		for (int i = 0; i < et_d.length; i++) {
			et_d[i] = (EditText) findViewById(d_r[i]);
			et_d[i].addTextChangedListener(watcher);
		}
		for (int i = 0; i < et_d1.length; i++) {
			et_d1[i] = (EditText) findViewById(d1_r[i]);
			et_d1[i].addTextChangedListener(watcher);
		}
		for (int i = 0; i < et_e.length; i++) {
			et_e[i] = (EditText) findViewById(e_r[i]);
			et_e[i].addTextChangedListener(watcher);
		}
		for (int i = 0; i < et_j.length; i++) {
			et_j[i] = (EditText) findViewById(j_r[i]);
			et_j[i].addTextChangedListener(watcher);
		}
		for (int i = 0; i < et_j1.length; i++) {
			et_j1[i] = (EditText) findViewById(j1_r[i]);
			et_j1[i].addTextChangedListener(watcher);
		}
		for (int i = 0; i < et_k.length; i++) {
			et_k[i] = (EditText) findViewById(k_r[i]);
			et_k[i].addTextChangedListener(watcher);
		}
		for (int i = 0; i < et_k1.length; i++) {
			et_k1[i] = (EditText) findViewById(k1_r[i]);
			et_k1[i].addTextChangedListener(watcher);
		}
		for (int i = 0; i < et_l.length; i++) {
			et_l[i] = (EditText) findViewById(l_r[i]);
			et_l[i].addTextChangedListener(watcher);
		}
		for (int i = 0; i < et_l1.length; i++) {
			et_l1[i] = (EditText) findViewById(l1_r[i]);
			et_l1[i].addTextChangedListener(watcher);
		}
		for (int i = 0; i < et_m.length; i++) {
			et_m[i] = (EditText) findViewById(m_r[i]);
			et_m[i].addTextChangedListener(watcher);
		}
		for (int i = 0; i < et_m1.length; i++) {
			et_m1[i] = (EditText) findViewById(m1_r[i]);
			et_m1[i].addTextChangedListener(watcher);
		}
		for (int i = 0; i < et_n.length; i++) {
			et_n[i] = (EditText) findViewById(n_r[i]);
			et_n[i].addTextChangedListener(watcher);
		}

		iv_nodata = (ImageView) findViewById(R.id.list_nodata_id);

		setJibunReset();
		setDoroReset();
		setJibunVisible(8);
		setDoroVisible(8);
		showTab(0);
	}

	private void setJibunReset() {
		for (int i = 0; i < et_a.length; i++) {
			et_a[i].setText("");
		}
		for (int i = 0; i < et_a1.length; i++) {
			et_a1[i].setText("");
		}
		for (int i = 0; i < et_b.length; i++) {
			et_b[i].setText("");
		}
		for (int i = 0; i < et_b1.length; i++) {
			et_b1[i].setText("");
		}
		for (int i = 0; i < et_c.length; i++) {
			et_c[i].setText("");
		}
		for (int i = 0; i < et_c1.length; i++) {
			et_c1[i].setText("");
		}
		for (int i = 0; i < et_d.length; i++) {
			et_d[i].setText("");
		}
		for (int i = 0; i < et_d1.length; i++) {
			et_d1[i].setText("");
		}
		for (int i = 0; i < et_e.length; i++) {
			et_e[i].setText("");
		}
	}

	private void setDoroReset() {
		for (int i = 0; i < et_j.length; i++) {
			et_j[i].setText("");
		}
		for (int i = 0; i < et_j1.length; i++) {
			et_j1[i].setText("");
		}
		for (int i = 0; i < et_k.length; i++) {
			et_k[i].setText("");
		}
		for (int i = 0; i < et_k1.length; i++) {
			et_k1[i].setText("");
		}
		for (int i = 0; i < et_l.length; i++) {
			et_l[i].setText("");
		}
		for (int i = 0; i < et_l1.length; i++) {
			et_l1[i].setText("");
		}
		for (int i = 0; i < et_m.length; i++) {
			et_m[i].setText("");
		}
		for (int i = 0; i < et_m1.length; i++) {
			et_m1[i].setText("");
		}
		for (int i = 0; i < et_n.length; i++) {
			et_n[i].setText("");
		}
	}

	private void setJibunVisible(int num) {
		for (int i = 0; i < jibun_layout.length; i++) {
			jibun_layout[i].setVisibility(View.GONE);
		}
		jibun_layout[num].setVisibility(View.VISIBLE);

		switch (num) {
		case 0:
		case 1:
			bt_jibun_structure.setText("아파트");
			break;
		case 2:
		case 3:
			bt_jibun_structure.setText("오피스텔");
			break;
		case 4:
		case 5:
			bt_jibun_structure.setText("빌라/연립");
			break;
		case 6:
		case 7:
			bt_jibun_structure.setText("빌딩/상가");
			break;
		case 8:
			bt_jibun_structure.setText("기타");
			break;
		default:
			bt_jibun_structure.setText("기타");
		}

		JIBUN_MODE = num;
	}

	private void setDoroVisible(int num) {
		for (int i = 0; i < doro_layout.length; i++) {
			doro_layout[i].setVisibility(View.GONE);
		}
		doro_layout[num].setVisibility(View.VISIBLE);

		switch (num) {
		case 0:
		case 1:
			bt_doro_structure.setText("아파트");
			break;
		case 2:
		case 3:
			bt_doro_structure.setText("오피스텔");
			break;
		case 4:
		case 5:
			bt_doro_structure.setText("빌라/연립");
			break;
		case 6:
		case 7:
			bt_doro_structure.setText("빌딩/상가");
			break;
		case 8:
			bt_doro_structure.setText("기타");
			break;
		default:
			bt_doro_structure.setText("기타");
		}

		DORO_MODE = num;
	}

	private void showTab(int num) {
		tab_ll[0].setVisibility(View.GONE);
		tab_ll[1].setVisibility(View.GONE);
		rb_tab[0].setTextColor(Color.parseColor("#cccccc"));
		rb_tab[1].setTextColor(Color.parseColor("#cccccc"));

		tab_ll[num].setVisibility(View.VISIBLE);
		rb_tab[num].setTextColor(Color.parseColor("#ffa02f"));

		switch (num) {
		case 0:
			JIBUN_DORO_MODE = JIBUN;
			break;
		case 1:
			JIBUN_DORO_MODE = DORO;
			break;
		}
	}

	private void initButtons() {
		tab.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				switch (checkedId) {
				case R.id.address_jibun_tab:

					showTab(0);
					break;
				case R.id.address_doro_tab:
					showTab(1);
					break;
				}
			}
		});
	}

	@Override
	public void onDismiss() {
	}

	@Override
	public void onCompleteDB(String funName, int type, Cursor cursor,
			String tableName) {
	}

	@Override
	public void onClick(View v) {
		final Address_Jibun_Search_Dialog asd = new Address_Jibun_Search_Dialog(
				context);
		Button bt_save = (Button) asd.findViewById(R.id.address_search_save_id);

		final Address_Doro_Search_Dialog adsd = new Address_Doro_Search_Dialog(
				context);
		Button bt_doro_save = (Button) adsd
				.findViewById(R.id.address_search_save_id);

		switch (v.getId()) {
		case R.id.address_jibun_search_id:
			bt_save.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View arg0) {
					setJibunReset();
					HashMap<String, String> selected = asd.getSelectedAddress();
					if (selected == null) {
						EventPopupC epc = new EventPopupC(context);
						epc.setTitle("주소를 선택해 주세요");
						epc.show();
						return;
					}
					String city = selected.get("CITY1");
					String street = selected.get("STREET");

					JIBUN_CITY = city;
					JIBUN_STREET = street;

					String post_code = selected.get("POST_CODE1");
					tv_jibun_address.setText("[" + post_code + "] "
							+ JIBUN_CITY);
					String offi_flag = selected.get("OFFI_FLAG");
					int num = Integer.parseInt(offi_flag);
					tv_full_address.setText(JIBUN_CITY + " " + JIBUN_STREET);
					setStructure(true, num, selected);
					asd.dismiss();
				}
			});
			asd.show();
			break;

		case R.id.address_doro_search_id:
			bt_doro_save.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View arg0) {
					setDoroReset();
					HashMap<String, String> selected = adsd
							.getSelectedAddress();
					if (selected == null) {
						EventPopupC epc = new EventPopupC(context);
						epc.setTitle("주소를 선택해주세요");
						epc.show();
						return;
					}
					String city = selected.get("CITY1");
					String street = selected.get("STREET");

					DORO_CITY = city;
					DORO_STREET = street;

					String post_code = selected.get("POST_CODE1");
					tv_doro_address.setText("[" + post_code + "] " + DORO_CITY);
					String offi_flag = selected.get("OFFI_FLAG");
					int num = Integer.parseInt(offi_flag);
					tv_full_address.setText(DORO_CITY + " " + DORO_STREET);
					setStructure(false, num, selected);
					adsd.dismiss();
				}
			});
			adsd.show();
			break;

		case R.id.address_close_bt_id:
			dismiss();
			break;

		case R.id.address_jibun_structure_id:
			if (structure_popup == null) {
				EventPopupC epc = new EventPopupC(context);
				epc.setTitle("검색후 선택하여 주십시오.");
				epc.show();
				break;
			}
			structure_popup.who = true;
			if (structure_popup != null)
				structure_popup.show(bt_jibun_structure);
			break;

		case R.id.address_doro_structure_id:
			if (structure_popup == null) {
				EventPopupC epc = new EventPopupC(context);
				epc.setTitle("검색후 선택하여 주십시오.");
				epc.show();
				break;
			}
			structure_popup.who = false;
			if (structure_popup != null)
				structure_popup.show(bt_doro_structure);
			break;

		case R.id.address_popup_address_gen_id:
			//myung 20131210 ADD 주소정제 버튼 클릭 시 RFC 호출되지 않도록 수정.

			if ((JIBUN_DORO_MODE == JIBUN && tv_jibun_address.getText().equals(""))
			    ||(JIBUN_DORO_MODE == DORO && tv_doro_address.getText().equals(""))) {
				EventPopupC epc = new EventPopupC(context);
				epc.setTitle("주소 검색 후 주소정제를 실행해 주세요.");
				epc.show();
				break;
			}
			// 필수값체크
			if (!checkRed())
				break;

			showProgress("주소정제 중 입니다.");
			connectController.getAllAddress(tv_full_address.getText()
					.toString());
			break;
		}

	}

	private boolean checkRed() {
		Object data;
		boolean check = true;
		// Log.i("#","####모드"+JIBUN_DORO_MODE+"/"+JIBUN_MODE+"/"+DORO_MODE);
		switch (JIBUN_DORO_MODE) {
		
		case JIBUN:
			switch (JIBUN_MODE) {
			case 0:
				data = et_a[2].getText().toString();
				if (data.equals("")) {
					EventPopupC epc = new EventPopupC(context);
					epc.setTitle("아파트 호를 입력해 주세요.");
					epc.show();
					check = false;
				}
				break;
			case 1:
				data = et_a1[1].getText().toString();
				if (data.equals("")) {
					EventPopupC epc = new EventPopupC(context);
					epc.setTitle("아파트 명을 입력해 주세요.");
					epc.show();
					check = false;
					break;
				}
				data = et_a1[3].getText().toString();
				if (data.equals("")) {
					EventPopupC epc = new EventPopupC(context);
					epc.setTitle("아파트 호를 입력해 주세요.");
					epc.show();
					check = false;
					break;
				}
				break;
			case 2:
				data = et_b[2].getText().toString();
				if (data.equals("")) {
					EventPopupC epc = new EventPopupC(context);
					epc.setTitle("오피스텔 호수를 입력해 주세요.");
					epc.show();
					check = false;
				}
				break;
			case 3:
				data = et_b1[1].getText().toString();
				if (data.equals("")) {
					EventPopupC epc = new EventPopupC(context);
					epc.setTitle("오피스텔 명을 입력해 주세요.");
					epc.show();
					check = false;
					break;
				}
				data = et_b1[3].getText().toString();
				if (data.equals("")) {
					EventPopupC epc = new EventPopupC(context);
					epc.setTitle("오피스텔 호를 입력해 주세요.");
					epc.show();
					check = false;
					break;
				}
				break;
			case 4:
				data = et_c[2].getText().toString();
				if (data.equals("")) {
					EventPopupC epc = new EventPopupC(context);
					epc.setTitle("빌라/연립 호수를 입력해 주세요.");
					epc.show();
					check = false;
				}
				break;
			case 5:
				data = et_c1[0].getText().toString();
				if (data.equals("")) {
					EventPopupC epc = new EventPopupC(context);
					epc.setTitle("빌라/연립 번지를 입력해 주세요.");
					epc.show();
					check = false;
					break;
				}
				data = et_c1[1].getText().toString();
				if (data.equals("")) {
					EventPopupC epc = new EventPopupC(context);
					epc.setTitle("빌라/연립 명을 입력해 주세요.");
					epc.show();
					check = false;
					break;
				}
				data = et_c1[3].getText().toString();
				if (data.equals("")) {
					EventPopupC epc = new EventPopupC(context);
					epc.setTitle("빌라/연립 호를 입력해 주세요.");
					epc.show();
					check = false;
					break;
				}
				break;
			case 6:
				data = et_d[0].getText().toString();
				if (data.equals("")) {
					EventPopupC epc = new EventPopupC(context);
					epc.setTitle("빌딩/상가 명을 입력해 주세요.");
					epc.show();
					check = false;
				}
				break;
			case 7:
				data = et_d1[0].getText().toString();
				if (data.equals("")) {
					EventPopupC epc = new EventPopupC(context);
					epc.setTitle("빌딩/상가 명을 입력해 주세요.");
					epc.show();
					check = false;
				}
				break;
			case 8:
				data = et_e[0].getText().toString();
				if (data.equals("")) {
					EventPopupC epc = new EventPopupC(context);
					epc.setTitle("번지를 입력해 주세요.");
					epc.show();
					check = false;
				}
				break;
			}
			break;
		case DORO:
			switch (DORO_MODE) {
			// case 0: Toast.makeText(context, "아파트에 아파트", 0).show(); break;
			// case 1: Toast.makeText(context, "기타에 아파트", 0).show(); break;
			// case 2: Toast.makeText(context, "오피스텔에 오피스텔", 0).show(); break;
			// case 3: Toast.makeText(context, "기타에 오피스텔", 0).show(); break;
			// case 4: Toast.makeText(context, "빌라/연립에 빌라/연립", 0).show(); break;
			// case 5: Toast.makeText(context, "기타에 빌라/연립", 0).show(); break;
			// case 6: Toast.makeText(context, "빌딩/상가에 빌딩/상가", 0).show(); break;
			// case 7: Toast.makeText(context, "기타에 빌딩/상가", 0).show(); break;
			// case 8: Toast.makeText(context, "모두의 기타", 0).show(); break;
			case 0:
				data = et_j[1].getText().toString();
				if (data.equals("")) {
					EventPopupC epc = new EventPopupC(context);
					epc.setTitle("아파트 호를 입력해 주세요.");
					epc.show();
					check = false;
				}
				break;
			case 1:
				data = et_j1[0].getText().toString();
				if (data.equals("")) {
					EventPopupC epc = new EventPopupC(context);
					epc.setTitle("아파트 명을 입력해 주세요.");
					epc.show();
					check = false;
					break;
				}
				data = et_j1[2].getText().toString();
				if (data.equals("")) {
					EventPopupC epc = new EventPopupC(context);
					epc.setTitle("아파트 호를 입력해 주세요.");
					epc.show();
					check = false;
					break;
				}
				break;
			case 2:
				data = et_k[1].getText().toString();
				if (data.equals("")) {
					EventPopupC epc = new EventPopupC(context);
					epc.setTitle("오피스텔 호수를 입력해 주세요.");
					epc.show();
					check = false;
				}
				break;
			case 3:
				data = et_k1[0].getText().toString();
				if (data.equals("")) {
					EventPopupC epc = new EventPopupC(context);
					epc.setTitle("오피스텔 명을 입력해 주세요.");
					epc.show();
					check = false;
					break;
				}
				data = et_k1[2].getText().toString();
				if (data.equals("")) {
					EventPopupC epc = new EventPopupC(context);
					epc.setTitle("오피스텔 호를 입력해 주세요.");
					epc.show();
					check = false;
					break;
				}
				break;
			case 4:
				data = et_l[1].getText().toString();
				if (data.equals("")) {
					EventPopupC epc = new EventPopupC(context);
					epc.setTitle("빌라/연립 호수를 입력해 주세요.");
					epc.show();
					check = false;
				}
				break;
			case 5:
				data = et_l1[0].getText().toString();
				if (data.equals("")) {
					EventPopupC epc = new EventPopupC(context);
					epc.setTitle("빌라/연립 명을 입력해 주세요.");
					epc.show();
					check = false;
					break;
				}
				data = et_l1[2].getText().toString();
				if (data.equals("")) {
					EventPopupC epc = new EventPopupC(context);
					epc.setTitle("빌라/연립 호 입력해 주세요.");
					epc.show();
					check = false;
					break;
				}
				break;
			case 6:

				break;
			case 7:
				data = et_m[0].getText().toString();
				if (data.equals("")) {
					EventPopupC epc = new EventPopupC(context);
					epc.setTitle("빌딩/상가 명을 입력해 주세요.");
					epc.show();
					check = false;
				}
				break;
			case 8:

				break;
			}
			break;
		}

		return check;
	}

	// public void genAddress(String id, String cityDo, String gu, String dong)
	// {
	// String strFuncName = ADDRESS_LOTNUMBER_FUNTION_NAME;
	//
	// LoginModel model = KtRentalApplication.getLoginModel();
	//
	// mConnector.setParameter("I_PERNR", model.getPernr());
	// mConnector.setParameter("I_DO_NM", cityDo);
	// mConnector.setParameter("I_CT_NM", gu);
	// mConnector.setParameter("I_DNG_NM", dong);
	//
	// HashMap<String, String> map = getCommonConnectData();
	// mConnector.setStructure("IS_LOGIN", map);
	//
	// try {
	// mConnector.executeRFCAsyncTask(strFuncName,
	// ADDRESS_LOTNUMBER_TABLE_NAME);
	// }
	// catch (Exception e) { e.printStackTrace(); }
	//
	// }

	private void setStructure(boolean yn, int num, HashMap<String, String> map) {
		// Log.i("####", "####주소구조"+yn+"/"+num);
		ArrayList<CM011> temp = null;
		String decrypted = map.get("STREET");
		if (yn) {
			switch (num) {
			case 0:
				temp = cm0110;
				setJibunVisible(8);
				et_e[1].setText(decrypted);
				break; // 기타
			case 1:
				temp = cm0111;
				setJibunVisible(0);
				et_a[0].setText(decrypted);
				et_e[1].setText(decrypted);
				break; // 아파트
			case 2:
				temp = cm0112;
				setJibunVisible(2);
				et_b[0].setText(decrypted);
				et_e[1].setText(decrypted);
				break; // 오피스텔
			case 3:
				temp = cm0113;
				setJibunVisible(4);
				et_c[0].setText(decrypted);
				et_e[1].setText(decrypted);
				break; // 빌라/연립
			case 4:
				temp = cm0114;
				setJibunVisible(6);
				et_d[0].setText(decrypted);
				et_e[1].setText(decrypted);
				break; // 빌딩/상가
			default:
				temp = cm0110;
				setJibunVisible(8);
				et_e[1].setText(decrypted);
				break;
			}
		} else {
			switch (num) {
			case 0:
				temp = cm0110;
				setDoroVisible(8);
				et_n[1].setText(decrypted);
				break; // 기타
			case 1:
				temp = cm0111;
				setDoroVisible(0);
				et_j[2].setText(decrypted);
				et_n[1].setText(decrypted);
				break; // 아파트
			case 2:
				temp = cm0112;
				setDoroVisible(2);
				et_k[2].setText(decrypted);
				et_n[1].setText(decrypted);
				break; // 오피스텔
			case 3:
				temp = cm0113;
				setDoroVisible(4);
				et_l[2].setText(decrypted);
				et_n[1].setText(decrypted);
				break; // 빌라/연립
			case 4:
				temp = cm0114;
				setDoroVisible(6);
				et_m[4].setText(decrypted);
				et_n[1].setText(decrypted);
				break; // 빌딩/상가
			default:
				temp = cm0110;
				setDoroVisible(8);
				et_n[1].setText(decrypted);
				break;
			}
		}

		structure_popup = new AddressStructurePopup(context, temp);
		ViewGroup vg = structure_popup.getViewGroup();
		LinearLayout back = (LinearLayout) vg.getChildAt(0);
		for (int i = 0; i < back.getChildCount(); i++) {
			LinearLayout row_back = (LinearLayout) back.getChildAt(i);
			final Button bt = (Button) row_back.getChildAt(0);
			bt.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					// setJibunReset();
					// setDoroReset();
					// Toast.makeText(context, bt.getText().toString() + " 입니다",
					// 0).show();
					CM011 cmo11 = (CM011) bt.getTag();
					String zcodeh2 = cmo11.ZCODEH2;
					String zcodev = cmo11.ZCODEV;

					if (zcodeh2.equals("0"))// 기타 5가지
					{
						if (zcodev.equals("0")) {
							if (structure_popup.who)
								setJibunVisible(8);
							else
								setDoroVisible(8);
						}// 기타=기타
						else if (zcodev.equals("1")) {
							if (structure_popup.who)
								setJibunVisible(1);
							else
								setDoroVisible(1);
						}// 기타=아파트
						else if (zcodev.equals("2")) {
							if (structure_popup.who)
								setJibunVisible(3);
							else
								setDoroVisible(3);
						}// 기타=오피스텔
						else if (zcodev.equals("3")) {
							if (structure_popup.who)
								setJibunVisible(5);
							else
								setDoroVisible(5);
						}// 기타=빌라/연립
						else if (zcodev.equals("4")) {
							if (structure_popup.who)
								setJibunVisible(7);
							else
								setDoroVisible(7);
						}// 기타=빌딩/상가
					} else if (zcodeh2.equals("1"))// 아파트
					{
						if (zcodev.equals("0")) {
							if (structure_popup.who)
								setJibunVisible(8);
							else
								setDoroVisible(8);
						}// 아파트=기타
						else if (zcodev.equals("1")) {
							if (structure_popup.who)
								setJibunVisible(0);
							else
								setDoroVisible(0);
						}// 아파트=아파트
					} else if (zcodeh2.equals("2"))// 오피스텔
					{
						if (zcodev.equals("0")) {
							if (structure_popup.who)
								setJibunVisible(8);
							else
								setDoroVisible(8);
						}// 오피스텔=기타
						else if (zcodev.equals("2")) {
							if (structure_popup.who)
								setJibunVisible(2);
							else
								setDoroVisible(2);
						}// 오피스텔=오피스텔
					} else if (zcodeh2.equals("3"))// 빌라/연립
					{
						if (zcodev.equals("0")) {
							if (structure_popup.who)
								setJibunVisible(8);
							else
								setDoroVisible(8);
						}// 빌라/연립=기타
						else if (zcodev.equals("3")) {
							if (structure_popup.who)
								setJibunVisible(4);
							else
								setDoroVisible(4);
						}// 빌라/연립=빌라/연립
					} else if (zcodeh2.equals("4"))// 빌딩/상가
					{
						if (zcodev.equals("0")) {
							if (structure_popup.who)
								setJibunVisible(8);
							else
								setDoroVisible(8);
						}// 빌딩/상가=기타
						else if (zcodev.equals("4")) {
							if (structure_popup.who)
								setJibunVisible(6);
							else
								setDoroVisible(6);
						}// 빌딩/상가=빌라/연립
					}
					structure_popup.dismiss();
				}
			});
		}

	}

	private String TABLE_NAME = "O_ITAB1";
	private ArrayList<CM011> cm0110 = new ArrayList<CM011>();
	private ArrayList<CM011> cm0111 = new ArrayList<CM011>();
	private ArrayList<CM011> cm0112 = new ArrayList<CM011>();
	private ArrayList<CM011> cm0113 = new ArrayList<CM011>();
	private ArrayList<CM011> cm0114 = new ArrayList<CM011>();

	private void initStructure() {
		String path = context.getExternalCacheDir() + "/DATABASE/"
				+ DEFINE.SQLLITE_DB_NAME;
		SQLiteDatabase sqlite = SQLiteDatabase.openDatabase(path, null,
				SQLiteDatabase.OPEN_READONLY);
		Cursor cursor = sqlite.rawQuery("select ZCODEH2, ZCODEV, ZCODEVT from "
				+ TABLE_NAME + " where ZCODEH = 'CM011'", null);
		while (cursor.moveToNext()) {
			int calnum1 = cursor.getColumnIndex("ZCODEH2");
			int calnum2 = cursor.getColumnIndex("ZCODEV");
			int calnum3 = cursor.getColumnIndex("ZCODEVT");

			String zcodeh2 = cursor.getString(calnum1);
			String zcodev = cursor.getString(calnum2);
			String zcodevt = cursor.getString(calnum3);

			CM011 cm011 = new CM011();
			cm011.ZCODEH2 = zcodeh2;
			cm011.ZCODEV = zcodev;
			cm011.ZCODEVT = zcodevt;

			if (zcodeh2.equals("0")) {
				cm0110.add(cm011);
			} else if (zcodeh2.equals("1")) {
				cm0111.add(cm011);
			} else if (zcodeh2.equals("2")) {
				cm0112.add(cm011);
			} else if (zcodeh2.equals("3")) {
				cm0113.add(cm011);
			} else if (zcodeh2.equals("4")) {
				cm0114.add(cm011);
			}
		}
		cursor.close();
//		sqlite.close();
	}

	TextWatcher watcher = new TextWatcher() {
		public void afterTextChanged(Editable s) {
		}

		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
		}

		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			switch (JIBUN_DORO_MODE) {
			case JIBUN:
				switch (JIBUN_MODE) {
				case 0:
					tv_full_address.setText(getFullAddress(JIBUN, et_a));
					break;
				case 1:
					tv_full_address.setText(getFullAddress(JIBUN, et_a1));
					break;
				case 2:
					tv_full_address.setText(getFullAddress(JIBUN, et_b));
					break;
				case 3:
					tv_full_address.setText(getFullAddress(JIBUN, et_b1));
					break;
				case 4:
					tv_full_address.setText(getFullAddress(JIBUN, et_c));
					break;
				case 5:
					tv_full_address.setText(getFullAddress(JIBUN, et_c1));
					break;
				case 6:
					tv_full_address.setText(getFullAddress(JIBUN, et_d));
					break;
				case 7:
					tv_full_address.setText(getFullAddress(JIBUN, et_d1));
					break;
				case 8:
					tv_full_address.setText(getFullAddress(JIBUN, et_e));
					break;
				}
				break;
			case DORO:
				switch (JIBUN_MODE) {
				case 0:
					tv_full_address.setText(getFullAddress(DORO, et_j));
					break;
				case 1:
					tv_full_address.setText(getFullAddress(DORO, et_j1));
					break;
				case 2:
					tv_full_address.setText(getFullAddress(DORO, et_k));
					break;
				case 3:
					tv_full_address.setText(getFullAddress(DORO, et_k1));
					break;
				case 4:
					tv_full_address.setText(getFullAddress(DORO, et_l));
					break;
				case 5:
					tv_full_address.setText(getFullAddress(DORO, et_l1));
					break;
				case 6:
					tv_full_address.setText(getFullAddress(DORO, et_m));
					break;
				case 7:
					tv_full_address.setText(getFullAddress(DORO, et_m1));
					break;
				case 8:
					tv_full_address.setText(getFullAddress(DORO, et_n));
					break;
				}
				break;
			}
		}
	};

	private String getFullAddress(int mode, EditText[] _et) {
		String add1 = "";
		String add2 = "";
		String result = "";
		switch (mode) {
		case JIBUN:
			add1 = JIBUN_CITY;
			break;
		case DORO:
			add1 = DORO_CITY;
			break;
		}

		for (int i = 0; i < _et.length; i++) {
			Object data = _et[i].getText();
			if (data == null || data.toString().equals(""))
				continue;
			String str = _et[i].getTag() != null ? _et[i].getTag().toString()
					: ""; // 동.호
			add2 += data != null ? data.toString() + str : "";
			add2 += " ";
		}

		if (add2.length() <= 0 || add2.equals("  ")) {
			result = add1;
		} else {
			result = add1 + " " + add2;
		}

		return result;
	}

	ArrayList<HashMap<String, String>> array_hash = new ArrayList<HashMap<String,String>>();
	Address_Dialog_Adapter ada;

	@Override
	public void connectResponse(String FuntionName, String resultText,
			String MTYPE, int resulCode, TableModel tableModel) {
		hideProgress();



		if (MTYPE == null || !MTYPE.equals("S")) {
			
			connectController.duplicateLogin(mContext);
			
			EventPopupC epc = new EventPopupC(context);
			epc.show(resultText);
			Toast.makeText(context, resultText, 0).show();
			return;
		}
		hideKeyboard();

        if(!"ZMO_1010_RD02".equals(FuntionName)) {

            array_hash = tableModel.getTableArray();

            if (array_hash.size() > 0) {
                iv_nodata.setVisibility(View.GONE);
            } else {
                iv_nodata.setVisibility(View.VISIBLE);
            }

            ada = new Address_Dialog_Adapter(context, R.layout.address_row,
                    array_hash);
            lv_gen_address.setAdapter(ada);
            lv_gen_address.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> arg0, View arg1,
                                        int position, long arg3) {
                    ada.setChoiced_num(position);
                    // Toast.makeText(context,
                    // "주소 : "+array_hash.get(position).get("FULL_ADDR"), 0).show();
                }
            });

            connectController.duplicateLogin(mContext);

        }


		
	}

	private void hideKeyboard() {
		InputMethodManager mInputMethodManager = (InputMethodManager) context
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		mInputMethodManager
				.hideSoftInputFromWindow(et_a[0].getWindowToken(), 0);
	}

	@Override
	public void reDownloadDB(String newVersion) {
	}

	
	public void setTv_full_address(ArrayList<HashMap<String, String>> hash) 
	{
		array_hash.addAll(hash);
	}
	
	
	public HashMap<String, String> getTv_full_address() {
		if (array_hash == null || array_hash.size() <= 0) {
			return new HashMap();
		}
		int position = ada.getChoiced_num();
		if (position == Integer.MAX_VALUE) {
			return null;
		}
		HashMap hm = array_hash.get(ada.getChoiced_num());
		return hm;
	}

	public void setTv_full_address(TextView tv_full_address) {
		this.tv_full_address = tv_full_address;
	}

}
