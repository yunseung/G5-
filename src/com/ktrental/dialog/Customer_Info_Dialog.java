package com.ktrental.dialog;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.Editable;
import android.text.Layout;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import com.ktrental.R;
import com.ktrental.R.integer;
import com.ktrental.popup.Popup_Window_Text_Balloon;
import com.ktrental.util.kog;

public class Customer_Info_Dialog extends DialogC implements
		View.OnClickListener {

	private Window w;
	private WindowManager.LayoutParams lp;
	private Context context;
	private Button close;
	private RadioGroup rg;
	private RadioButton rb[] = new RadioButton[3];
	private LinearLayout tab[] = new LinearLayout[3];
	private HashMap<String, String> O_STRUCT1;
	private HashMap<String, String> O_STRUCT2;
	private HashMap<String, String> O_STRUCT3;
	private HashMap<String, String> O_STRUCT4;
	private HashMap<String, String> O_STRUCT5;
	private String CUSNAM;
	private String MODE;
	private Button bt_done;

	// 공통
	private TextView tv_top1, tv_top2, vipTextView;
	// 기본정보
	private TextView tv_basic_line1_id1, tv_basic_line1_id2;
	private TextView tv_basic_line2_id1, tv_basic_line2_id2;
	private TextView tv_basic_line3_id1, tv_basic_line3_id2;
	private TextView tv_driver_address;
	// private TextView tv_basic_line4_id1;
	// 보험정보
	private TextView tv_insurance_line1_id1, tv_insurance_line1_id2;
	private TextView tv_insurance_line2_id1, tv_insurance_line2_id2;
	private TextView tv_insurance_line3_id1, tv_insurance_line3_id2;
	private EditText tv_insurance_line4_id1;
	// 계약정보
	private TextView tv_contract_line1_id1, tv_contract_line1_id2;
	private TextView tv_contract_line2_id1, tv_contract_line2_id2;
	private TextView tv_contract_line3_id1, tv_contract_line3_id2;
	private TextView tv_contract_line4_id1;
	private TextView tv_contract_line5_id1, tv_contract_line5_id2,
			tv_contract_line5_id3;
	private TextView tv_contract_line6_id1, tv_contract_line6_id2;
	private TextView tv_contract_line7_id1;
	// 정비옵션정보
	private TextView tv_maintenance_line1_id1, tv_maintenance_line1_id2;
	private TextView tv_maintenance_line2_id1, tv_maintenance_line2_id2;
	private TextView tv_maintenance_line3_id1, tv_maintenance_line3_id2;
	private TextView tv_maintenance_line4_id1, tv_maintenance_line4_id2;
	private TextView tv_maintenance_line5_id1, tv_maintenance_line5_id2,
			tv_maintenance_line5_id3;
	private TextView tv_maintenance_line6_id1;
	private TextView tv_maintenance_line7_id1;
	private TextView tv_maintenance_line8_id1;

	public Customer_Info_Dialog(Context context,
			HashMap<String, String> o_struct1,
			HashMap<String, String> o_struct2,
			HashMap<String, String> o_struct3,
			HashMap<String, String> o_struct4,
			HashMap<String, String> o_struct5, String cusnam, String mode) {
		super(context);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.customer_info_dialog);
		// 2013.12.03 ypkim
		
		if (o_struct1 != null)
			this.O_STRUCT1 = o_struct1;
		
		if (o_struct2 != null)
			this.O_STRUCT2 = o_struct2;

		if (o_struct3 != null)
			this.O_STRUCT3 = o_struct3;

		if (o_struct4 != null)
			this.O_STRUCT4 = o_struct4;

		if (o_struct5 != null)
			this.O_STRUCT5 = o_struct5;
		this.CUSNAM = cusnam;
		this.MODE = mode;

		
//		Set <String> set;
//		Iterator <String> it;
//		String key;
//		
//		
//		set  = o_struct1.keySet();
//		it = set.iterator();
//		
//		while(it.hasNext()) 
//		{
//			key = it.next();
//			kog.e("Jonathan", "1어디보자~! key ===  " + key + "    value  === " + o_struct1.get(key));
//		}
//		
//		set  = o_struct2.keySet();
//		it = set.iterator();
//		
//		while(it.hasNext()) 
//		{
//			key = it.next();
//			kog.e("Jonathan", "2어디보자~! key ===  " + key + "    value  === " + o_struct2.get(key));
//		}
//		
//		set  = o_struct3.keySet();
//		it = set.iterator();
//		
//		while(it.hasNext()) 
//		{
//			key = it.next();
//			kog.e("Jonathan", "3어디보자~! key ===  " + key + "    value  === " + o_struct3.get(key));
//		}
//		
//		
//		set  = o_struct4.keySet();
//		it = set.iterator();
//		
//		while(it.hasNext()) 
//		{
//			key = it.next();
//			kog.e("Jonathan", "4어디보자~! key ===  " + key + "    value  === " + o_struct4.get(key));
//		}
//		
//		
//		set  = o_struct5.keySet();
//		it = set.iterator();
//		
//		while(it.hasNext()) 
//		{
//			key = it.next();
//			kog.e("Jonathan", "5어디보자~! key ===  " + key + "    value  === " + o_struct5.get(key));
//		}
		
		
		w = getWindow();
		w.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN
				| WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
		w.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
		lp = (WindowManager.LayoutParams) w.getAttributes();
		lp.width = LayoutParams.MATCH_PARENT;
		lp.height = LayoutParams.MATCH_PARENT;
		w.setAttributes(lp);
		this.context = context;

		initView();

		setButton();
		rb[0].setChecked(true);

		setField();
		
		
		tv_insurance_line4_id1.setOnTouchListener(new OnTouchListener() {
            public boolean onTouch(View view, MotionEvent event) {
                // TODO Auto-generated method stub
                if (view.getId() == R.id.customer_info_insurance_line4_id1) {
                    view.getParent().requestDisallowInterceptTouchEvent(true);
                    switch (event.getAction()&MotionEvent.ACTION_MASK){
                    case MotionEvent.ACTION_UP:
                        view.getParent().requestDisallowInterceptTouchEvent(false);
                        break;
                    }
                }
                return false;
            }
    });
		
	}

	private void initView() {
		close = (Button) findViewById(R.id.customer_info_dialog_close_id);
		close.setOnClickListener(this);
		bt_done = (Button) findViewById(R.id.customer_info_done_id);
		bt_done.setOnClickListener(this);
		rg = (RadioGroup) findViewById(R.id.customer_info_tab_rg_id);
		rb[0] = (RadioButton) findViewById(R.id.customer_info_tab1_rb_id);
		rb[1] = (RadioButton) findViewById(R.id.customer_info_tab2_rb_id);
		rb[2] = (RadioButton) findViewById(R.id.customer_info_tab3_rb_id);
		tab[0] = (LinearLayout) findViewById(R.id.customer_info_tab1_layout_id);
		tab[1] = (LinearLayout) findViewById(R.id.customer_info_tab2_layout_id);
		tab[2] = (LinearLayout) findViewById(R.id.customer_info_tab3_layout_id);

		// 공통
		tv_top1 = (TextView) findViewById(R.id.customer_info_carnum_id);
		tv_top2 = (TextView) findViewById(R.id.customer_info_name_id);
		vipTextView = (TextView) findViewById(R.id.vipTextView);
		// 기본정보
		tv_basic_line1_id1 = (TextView) findViewById(R.id.customer_info_basic_line1_id1);
		tv_basic_line1_id2 = (TextView) findViewById(R.id.customer_info_basic_line1_id2);
		tv_basic_line2_id1 = (TextView) findViewById(R.id.customer_info_basic_line2_id1);
		tv_basic_line2_id2 = (TextView) findViewById(R.id.customer_info_basic_line2_id2);
		tv_basic_line3_id1 = (TextView) findViewById(R.id.customer_info_basic_line3_id1);
		tv_basic_line3_id2 = (TextView) findViewById(R.id.customer_info_basic_line3_id2);
		tv_driver_address = (TextView) findViewById(R.id.tv_driver_address);
		tv_driver_address.setOnClickListener(this);
		// tv_basic_line4_id1 =
		// (TextView)findViewById(R.id.customer_info_basic_line4_id1);
		// 보험정보
		tv_insurance_line1_id1 = (TextView) findViewById(R.id.customer_info_insurance_line1_id1);
		tv_insurance_line1_id2 = (TextView) findViewById(R.id.customer_info_insurance_line1_id2);
		tv_insurance_line2_id1 = (TextView) findViewById(R.id.customer_info_insurance_line2_id1);
		tv_insurance_line2_id2 = (TextView) findViewById(R.id.customer_info_insurance_line2_id2);
		tv_insurance_line3_id1 = (TextView) findViewById(R.id.customer_info_insurance_line3_id1);
		tv_insurance_line3_id2 = (TextView) findViewById(R.id.customer_info_insurance_line3_id2);
		tv_insurance_line4_id1 = (EditText) findViewById(R.id.customer_info_insurance_line4_id1);
		// 계약정보
		tv_contract_line1_id1 = (TextView) findViewById(R.id.customer_info_contract_line1_id1);
		tv_contract_line1_id2 = (TextView) findViewById(R.id.customer_info_contract_line1_id2);
		tv_contract_line2_id1 = (TextView) findViewById(R.id.customer_info_contract_line2_id1);
		tv_contract_line2_id2 = (TextView) findViewById(R.id.customer_info_contract_line2_id2);
		tv_contract_line3_id1 = (TextView) findViewById(R.id.customer_info_contract_line3_id1);
		tv_contract_line3_id2 = (TextView) findViewById(R.id.customer_info_contract_line3_id2);
		tv_contract_line4_id1 = (TextView) findViewById(R.id.customer_info_contract_line4_id1);
		tv_contract_line5_id1 = (TextView) findViewById(R.id.customer_info_contract_line5_id1);
		tv_contract_line5_id2 = (TextView) findViewById(R.id.customer_info_contract_line5_id2);
		tv_contract_line5_id3 = (TextView) findViewById(R.id.customer_info_contract_line5_id3);
		tv_contract_line6_id1 = (TextView) findViewById(R.id.customer_info_contract_line6_id1);
		tv_contract_line6_id2 = (TextView) findViewById(R.id.customer_info_contract_line6_id2);
		tv_contract_line7_id1 = (TextView) findViewById(R.id.customer_info_contract_line7_id1);
		// 정비옵션정보
		tv_maintenance_line1_id1 = (TextView) findViewById(R.id.customer_info_maintenance_line1_id1);
		tv_maintenance_line1_id2 = (TextView) findViewById(R.id.customer_info_maintenance_line1_id2);
		tv_maintenance_line2_id1 = (TextView) findViewById(R.id.customer_info_maintenance_line2_id1);
		tv_maintenance_line2_id2 = (TextView) findViewById(R.id.customer_info_maintenance_line2_id2);
		tv_maintenance_line3_id1 = (TextView) findViewById(R.id.customer_info_maintenance_line3_id1);
		tv_maintenance_line3_id2 = (TextView) findViewById(R.id.customer_info_maintenance_line3_id2);
		tv_maintenance_line4_id1 = (TextView) findViewById(R.id.customer_info_maintenance_line4_id1);
		tv_maintenance_line4_id2 = (TextView) findViewById(R.id.customer_info_maintenance_line4_id2);
		tv_maintenance_line5_id1 = (TextView) findViewById(R.id.customer_info_maintenance_line5_id1);
		tv_maintenance_line5_id2 = (TextView) findViewById(R.id.customer_info_maintenance_line5_id2);
		tv_maintenance_line5_id3 = (TextView) findViewById(R.id.customer_info_maintenance_line5_id3);
		tv_maintenance_line6_id1 = (TextView) findViewById(R.id.customer_info_maintenance_line6_id1);
		tv_maintenance_line7_id1 = (TextView) findViewById(R.id.customer_info_maintenance_line7_id1);
		tv_maintenance_line8_id1 = (TextView) findViewById(R.id.customer_info_maintenance_line8_id1);
		
		
		
	}

	private void setField() {
		// 공통
		if (O_STRUCT2 != null) {
			tv_top1.setText(O_STRUCT2.get("INVNR"));
			tv_top2.setText(CUSNAM);
			// 기본정보
			tv_basic_line1_id1.setText(O_STRUCT2.get("MAKTX"));
			tv_basic_line1_id2.setText(""); // 없애기
			
			if(O_STRUCT2.get("VIPGBN").equals("Y"))
			{
				vipTextView.setText("VIP");
			}
			else if(O_STRUCT2.get("VIPGBN").equals("V"))
			{
				vipTextView.setText("롯데그룹VIP");
			}
			else
			{
				vipTextView.setText("");
			}
			
			
			// 14.11.03 Jonathan 사고일때 운전자명, 휴대폰 번호 안나온다고 해서 O_STRUCT1, MODE param으로 받아와서 처리함.
			if (MODE.equals("A")) {  //사고
				kog.e("Jonathan", "사고임.....");
				tv_basic_line2_id1.setText(O_STRUCT1.get("DRVNAM"));		
				tv_basic_line3_id1.setText(O_STRUCT1.get("DRVHP"));   
			}
			else	// 일반 긴급 등등
			{
				tv_basic_line2_id1.setText(O_STRUCT2.get("DLSM1"));		
				tv_basic_line3_id1.setText(O_STRUCT2.get("DLST1"));    
			}
			tv_basic_line2_id2.setText(O_STRUCT2.get("VIPGBN"));
			
			tv_basic_line3_id2.setText(O_STRUCT2.get("TELF1"));
			// myung 20131224 ADD 기본정보에 운전자 주소 추가.
			String strZipcodeFirst = "";
			String strZipcodeLast = "";
			if(O_STRUCT2.get("PSTLZ2")!=null && O_STRUCT2.get("PSTLZ2").length()>=6){
				strZipcodeFirst = O_STRUCT2.get("PSTLZ2").substring(0, 3);
				strZipcodeLast = O_STRUCT2.get("PSTLZ2").substring(3, 6);
			}
			if(O_STRUCT2.get("ORT02").equals(" ") || O_STRUCT2.get("ORT02")==null){
				tv_driver_address.setText("");
			}else{
				tv_driver_address.setText("[" + strZipcodeFirst + "-"
						+ strZipcodeLast + "] " + O_STRUCT2.get("ORT02") + " "
						+ O_STRUCT2.get("STRAS2"));
				
				isEnableTextBalloon(tv_driver_address);
			}
			
		}
		// tv_basic_line4_id1.setText("주소"); //없애기
		// 보험정보
		if (O_STRUCT3 != null) {
			tv_insurance_line1_id1.setText(O_STRUCT3.get("NAME1"));
			tv_insurance_line1_id2.setText(O_STRUCT3.get("SCADYNNM"));
			tv_insurance_line2_id1
					.setText(getDateFormat(O_STRUCT3.get("INSAD")));
			tv_insurance_line2_id2
					.setText(getDateFormat(O_STRUCT3.get("INSED")));
			tv_insurance_line3_id1.setText(O_STRUCT3.get("OCFCDNM"));
			tv_insurance_line3_id2.setText(O_STRUCT3.get("DRVACDNM"));
			tv_insurance_line4_id1.setText(O_STRUCT3.get("SUINSCNM"));
		}
		// 계약정보
		if (O_STRUCT4 != null) {
			tv_contract_line1_id1.setText(O_STRUCT4.get("VBELN")); // 14.11.03 계약번호 
			tv_contract_line1_id2.setText("");
			tv_contract_line2_id1.setText(O_STRUCT4.get("CNGBN"));
			tv_contract_line2_id2.setText(O_STRUCT4.get("SALEDM"));
			tv_contract_line3_id1.setText(O_STRUCT4.get("ONBRNM"));
			tv_contract_line3_id2.setText(O_STRUCT4.get("CNBRNM"));
			tv_contract_line4_id1.setText(O_STRUCT4.get("CIRDAM"));
			tv_contract_line5_id1
					.setText(getDateFormat(O_STRUCT4.get("WTSDT")));
			tv_contract_line5_id2
					.setText(getDateFormat(O_STRUCT4.get("WTFDT")));
//			tv_contract_line5_id3.setText(O_STRUCT4.get("WTMON"));
			
			// 14.11..04 Jonathan 숫자 앞 000 지우기
			int int_WTMON = 0;
			String string_WTMON = null;
			
			int_WTMON = Integer.valueOf(O_STRUCT4.get("WTMON"));
			string_WTMON = String.valueOf(int_WTMON);
			tv_contract_line5_id3.setText(string_WTMON);
		
			
			tv_contract_line6_id1
					.setText(getDateFormat(O_STRUCT4.get("INBDT")));
			tv_contract_line6_id2.setText(O_STRUCT4.get("LDWAMT") + " 원");
			
			
			String string_LDWAMT = null;
			string_LDWAMT = O_STRUCT4.get("LDWAMT").trim().replace(" ", "");
			
			// 14.11..04 Jonathan 금액 표시 , 넣기
			if(!"".equals(string_LDWAMT) || !" ".equals(string_LDWAMT))
			{
				tv_contract_line6_id2.setText(makeStringComma(string_LDWAMT) +  " 원");
			}

			tv_contract_line7_id1.setText(O_STRUCT4.get("CRSPEC"));
		}
		// 정비옵션정보
		if (O_STRUCT5 != null) {
//			tv_maintenance_line1_id1.setText(O_STRUCT5.get("MATMA"));	// 정비상품
			tv_maintenance_line1_id1.setText(O_STRUCT5.get("SANPM"));	// 정비상품  		// Jonathan 14.11.07 O_STRUCT5-SANPM 으로 바뀜.
			tv_maintenance_line1_id2.setText(O_STRUCT5.get("CYCMNT"));
			tv_maintenance_line2_id1.setText(O_STRUCT5.get("TIRGBN")); 
			// myung 20131202 UPDATE 대차제공서비스 표기 방법 변경 (O_STRUCT3-DCHAGBN /
			// O_STRUCT3-DCHADY 로 표기)
//			tv_maintenance_line2_id2.setText(O_STRUCT5.get("DCHADY")); //대차제공서비스
			tv_maintenance_line2_id2.setText(O_STRUCT5.get("DCHAGBN")); //대차제공서비스	// Jonathan 14.11.07 O_STRUCT5-DCHAGBN 으로 바뀜.
//			tv_maintenance_line3_id1.setText(O_STRUCT5.get("GENGBN"));	
			//일반정비 Y -> 가입   N -> 미가입			
			if("Y".equals(O_STRUCT5.get("GENGBN")))
			{
				tv_maintenance_line3_id1.setText("가입");
			}
			else
			{
				tv_maintenance_line3_id1.setText("미가입");
			}
			
			tv_maintenance_line3_id2.setText(O_STRUCT5.get("INPDT"));
//			tv_maintenance_line4_id1.setText(O_STRUCT5.get("TIRFLAT")); 
			//타이어 펑크  Y -> 가입   N -> 미가입
			if("Y".equals(O_STRUCT5.get("TIRFLAT")))
			{
				tv_maintenance_line4_id1.setText("가입");
			}
			else
			{
				tv_maintenance_line4_id1.setText("미가입");
			}
			
			tv_maintenance_line4_id2.setText(O_STRUCT5.get("GCHLQTY"));
//			tv_maintenance_line5_id1.setText(O_STRUCT5.get("SNWTIR"));	
			// 스노우 타이어 Y -> 가입   N -> 미가입
			if("N".equals(O_STRUCT5.get("SNWTIR")))
			{
				tv_maintenance_line5_id1.setText("미가입");
				tv_maintenance_line5_id2.setText("EA");
			}
			else
			{
				tv_maintenance_line5_id1.setText("가입");
				tv_maintenance_line5_id2.setText(O_STRUCT5.get("SNWTIR") + "EA");
			}
			tv_maintenance_line5_id3.setText(O_STRUCT5.get("CHAIN")); 
			tv_maintenance_line6_id1.setText(O_STRUCT5.get("SPIDER"));
			tv_maintenance_line7_id1.setText(O_STRUCT5.get("PRIOPT"));
			tv_maintenance_line8_id1.setText(O_STRUCT2.get("SERGE"));
		}
	}
	
	
	public String makeStringComma(String str)
	{
		kog.e("Jonatha", "str.length() == " + str.length());
		if(str.length() == 0)
			return "0";
		long value = Long.parseLong(str);
		
		DecimalFormat format = new DecimalFormat("###,###");
		return format.format(value);
			
	}

	//myung 20131220 ADD TextView에서 1라인을 넘을 경우 … 풍선 추가하여 클릭시 주소 확인
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
	
	//myung 20131220 ADD TextView에서 1라인을 넘을 경우 … 풍선 추가하여 클릭시 주소 확인
	private void showTextBalloon(TextView v){
		Layout layout = v.getLayout();
		if(layout != null){
			if (layout.getEllipsisCount(layout.getLineCount()-1) > 0) {
				Popup_Window_Text_Balloon pwtb = new Popup_Window_Text_Balloon(context);
				pwtb.show(v, v.getText().toString());
			}
		}
	}
	
	private void setButton() {
		rg.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				switch (checkedId) {
				case R.id.customer_info_tab1_rb_id:
					showTab(0);
					break;
				case R.id.customer_info_tab2_rb_id:
					showTab(1);
					break;
				case R.id.customer_info_tab3_rb_id:
					showTab(2);
					break;
				}
			}
		});
	}

	private void showTab(int num) {
		for (int i = 0; i < tab.length; i++) {
			rb[i].setTextColor(Color.parseColor("#cccccc"));
			tab[i].setVisibility(View.GONE);
		}
		rb[num].setTextColor(Color.parseColor("#ffa02f"));
		tab[num].setVisibility(View.VISIBLE);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.customer_info_done_id:
			dismiss();
			break;
		case R.id.customer_info_dialog_close_id: // 닫기
			dismiss();
			break;
		case R.id.tv_driver_address:
			showTextBalloon(tv_driver_address);
		}
	}
}
