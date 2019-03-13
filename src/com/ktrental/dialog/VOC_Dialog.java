package com.ktrental.dialog;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.ScrollView;
import android.widget.TextView;

import com.ktrental.R;
import com.ktrental.cm.util.PrintLog;
import com.ktrental.model.TableModel;

public class VOC_Dialog extends DialogC implements View.OnClickListener {
	private Window w;
	private WindowManager.LayoutParams lp;
	private Context context;
	private Button close;
	private ProgressDialog pd;

	private RadioGroup rg;
	private RadioButton rb[] = new RadioButton[4];
	private LinearLayout tab[] = new LinearLayout[4];
	private ScrollView scv[] = new ScrollView[4];
	
	HashMap<String, String> es1015;
	ArrayList<HashMap<String, String>> et1020;

	private TableModel mVocModel;

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

	public VOC_Dialog(Context context, HashMap<String, String> mapES1015, ArrayList<HashMap<String,String>> arrET1020, int mode) {
		super(context);
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.voc_dialog);
		
		es1015 = new HashMap<String, String>(mapES1015);
		et1020 = new ArrayList<HashMap<String,String>>(arrET1020);
		
		// this.o_struct1 = o_struct1;
		w = getWindow();
		w.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN
				| WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
		w.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
		lp = (WindowManager.LayoutParams) w.getAttributes();
		lp.width = LayoutParams.MATCH_PARENT;
		lp.height = LayoutParams.MATCH_PARENT;
		w.setAttributes(lp);
		this.context = context;
		
		// 제목
		((TextView)findViewById(R.id.id_voc_detail_title1)).setText(es1015.get("VCTITL"));
		((TextView)findViewById(R.id.id_voc_detail_title2)).setText(es1015.get("VCTITL"));
		((TextView)findViewById(R.id.id_voc_detail_title3)).setText(es1015.get("VCTITL"));
		// 접수 내용
		((TextView)findViewById(R.id.id_voc_detail_content1)).setText(es1015.get("VCDES"));
		((TextView)findViewById(R.id.id_voc_detail_content2)).setText(es1015.get("VCDES"));
		((TextView)findViewById(R.id.id_voc_detail_content3)).setText(es1015.get("VCDES"));
		// 접수자 의견
		((TextView)findViewById(R.id.id_voc_detail_opinion1)).setText(es1015.get("RCPDES"));
		((TextView)findViewById(R.id.id_voc_detail_opinion2)).setText(es1015.get("RCPDES"));
		((TextView)findViewById(R.id.id_voc_detail_opinion3)).setText(es1015.get("RCPDES"));
		
		// 1차
		if(arrET1020.size() > 0) 
		{
			((TextView)findViewById(R.id.id_voc_detail_cause1)).setText(checkNullValue(arrET1020.get(0), "OCDES"));
			((TextView)findViewById(R.id.id_voc_detail_measure1)).setText(checkNullValue(arrET1020.get(0), "FRS_T"));
			((TextView)findViewById(R.id.id_voc_detail_result1)).setText(checkNullValue(arrET1020.get(0), "BOFF_T"));
			((TextView)findViewById(R.id.id_voc_detail_why1)).setText(checkNullValue(arrET1020.get(0), "DOVRT"));
		}
		
		// 2차
		if(arrET1020.size() > 1)
		{
			((TextView)findViewById(R.id.id_voc_detail_cause2)).setText(checkNullValue(arrET1020.get(1), "OCDES"));
			((TextView)findViewById(R.id.id_voc_detail_measure2)).setText(checkNullValue(arrET1020.get(1), "FRS_T"));
			((TextView)findViewById(R.id.id_voc_detail_result2)).setText(checkNullValue(arrET1020.get(1), "BOFF_T"));
			((TextView)findViewById(R.id.id_voc_detail_why2)).setText(checkNullValue(arrET1020.get(1), "DOVRT"));
		}
		
		// 2차
		if(arrET1020.size() > 2)
		{
			((TextView)findViewById(R.id.id_voc_detail_cause3)).setText(checkNullValue(arrET1020.get(2), "OCDES"));
			((TextView)findViewById(R.id.id_voc_detail_measure3)).setText(checkNullValue(arrET1020.get(2), "FRS_T"));
			((TextView)findViewById(R.id.id_voc_detail_result3)).setText(checkNullValue(arrET1020.get(2), "BOFF_T"));
			((TextView)findViewById(R.id.id_voc_detail_why3)).setText(checkNullValue(arrET1020.get(2), "DOVRT"));
		}
		
		close = (Button) findViewById(R.id.history_dialog_close_id);
		close.setOnClickListener(this);

		rg = (RadioGroup) findViewById(R.id.history_tab_rg_id);
		rb[0] = (RadioButton) findViewById(R.id.history_tab1_rb_id);
		rb[1] = (RadioButton) findViewById(R.id.history_tab2_rb_id);
		rb[2] = (RadioButton) findViewById(R.id.history_tab3_rb_id);

		tab[0] = (LinearLayout) findViewById(R.id.history_tab1_layout_id);
		tab[1] = (LinearLayout) findViewById(R.id.history_tab2_layout_id);
		tab[2] = (LinearLayout) findViewById(R.id.history_tab3_layout_id);

		scv[0] = (ScrollView) findViewById(R.id.scrollView1);
		scv[1] = (ScrollView) findViewById(R.id.scrollView2);
		scv[2] = (ScrollView) findViewById(R.id.scrollView3);

		bt_done = (Button) findViewById(R.id.history_done_id);
		bt_done.setOnClickListener(this);

		// bt_search = (Button) findViewById(R.id.history_search_id);
		// bt_search.setOnClickListener(this);

		iv_nodata1 = (ImageView) findViewById(R.id.list_nodata_id1);
		iv_nodata2 = (ImageView) findViewById(R.id.list_nodata_id2);
		iv_nodata3 = (ImageView) findViewById(R.id.list_nodata_id3);
		iv_nodata4 = (ImageView) findViewById(R.id.list_nodata_id4);
	}
	
	/**
     * Null 값인 지 체크해서 공백 또는 값을 반환 한다.
     * @param temp
     * @param key
     * @return
     */
    private String checkNullValue(HashMap<String, String> temp, String key) 
    {
    	if(temp.containsKey(key))
    	{
    		String value = temp.get(key);
    		if(value == null || value.trim().length() == 0)
    		{
    			PrintLog.Print("VOC INFO", key + " is not value");
    			return "";
    		}
    		
    		return value;
    	}
    	
    	return "";
    }

	@Override
	public void onAttachedToWindow() {
		super.onAttachedToWindow();
		init(MODE);
	}

	private void init(int mode) {
		setButton();

		switch (mode) {
		case 0:
			rb[0].setChecked(true);

			break;
		case 1:
			if(et1020.size() <= 1)
				return;
			rb[1].setChecked(true);

			break;
		case 2:
			if(et1020.size() <= 2)
				return;
			
			rb[2].setChecked(true);
			break;
		}
	}

	private void showTab(int num) {
		for (int i = 0; i < tab.length - 1; i++) {
			rb[i].setTextColor(Color.parseColor("#cccccc"));
			scv[i].setVisibility(View.GONE);
			tab[i].setVisibility(View.GONE);

		}
		rb[num].setTextColor(Color.parseColor("#ffa02f"));
		scv[num].setVisibility(View.VISIBLE);
		tab[num].setVisibility(View.VISIBLE);

	}

	private void setButton() {
		close.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				dismiss();
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
				}
			}
		});
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
	}

//	@Override
//	public void connectResponse(String FuntionName, String resultText,
//			String MTYPE, int resulCode, TableModel tableModel) {
//		// Log.i("#", "####" + FuntionName + "/" + resultText + "/" + MTYPE +
//		// "/" + resulCode);
//		hideProgress();
//		if (MTYPE == null || !MTYPE.equals("S")) {
//			EventPopupC epc = new EventPopupC(context);
//			epc.show(resultText);
//			return;
//		}
//
//		if (FuntionName.equals("ZMO_1140_RD02")) {
//
//			HashMap<String, String> structTableNames = new HashMap<String, String>();
//			mVocModel = tableModel;
//			HashMap<String, String> struct = tableModel.getStruct("ET_1020");
//			HashMap<String, String> struct1 = tableModel.getStruct("ES_1015");
//
//			if (struct1 != null) {
//				String VCTITL = struct1.get("VCTITL"); // 제목
//				String VCDES = struct1.get("VCDES"); // 접수내용
//				String RCPDES = struct1.get("RCPDES"); // 접수자의견
//
//			}
//
//			if (struct != null) {
//				String OCDES = struct.get("OCDES"); // 발생원인
//				String FRS_T = struct.get("FRS_T"); // 조치내용
//				String DOVRT = struct.get("DOVRT"); // 24시간 경과사유
//				String BOFF_T = struct.get("BOFF_T"); // 조치결과및 지점의견
//
//			}
//
//			// kog.e("Jonatha","tableModel.getTableName() :: " +
//			// tableModel.getStruct("O_STRUCT2"));
//			Set<String> set = tableModel.getStruct("ET_1020").keySet();
//			Iterator<String> it = set.iterator();
//			String key;
//
//			while (it.hasNext()) {
//				key = it.next();
//				kog.e("Jonathan", "voc_dialog key ===  " + key
//						+ "    value  === "
//						+ tableModel.getStruct("ET_1020").get(key));
//			}
//
//			Set<String> set1 = tableModel.getStruct("ES_1015").keySet();
//			Iterator<String> it1 = set1.iterator();
//			String key1;
//
//			while (it1.hasNext()) {
//				key1 = it1.next();
//				kog.e("Jonathan", "voc_dialog key ===  " + key1
//						+ "    value  === "
//						+ tableModel.getStruct("ES_1015").get(key1));
//			}
//
//		}
//	}
}
