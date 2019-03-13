package com.ktrental.dialog;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.ktrental.R;
import com.ktrental.cm.connect.ConnectController2;
import com.ktrental.cm.connect.Connector.ConnectInterface;
import com.ktrental.common.KtRentalApplication;
import com.ktrental.model.LoginModel;
import com.ktrental.model.TableModel;
import com.ktrental.popup.EventPopupC;
import com.ktrental.util.CommonUtil;
import com.ktrental.util.kog;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class KDH_check_list extends DialogC implements ConnectInterface,OnClickListener {

	private Window w;
	private WindowManager.LayoutParams lp;
	private Context context;
	private ProgressDialog pd;

	// private EditText et_input;
	private ConnectController2 cc;

	public ArrayList<HashMap<String, String>> array_hash;

	int L_check_id[] = {
			R.id.L_check1,
			R.id.L_check2,
			R.id.L_check3,
			R.id.L_check4,
			R.id.L_check5,
	};

	int R_radio_group_id[] = {
			R.id.rg1,
			R.id.rg2,
			R.id.rg3,
			R.id.rg4,
			R.id.rg5,
	};

	int R_radio_id[] = {
			R.id.rd1,	
			R.id.rd2,	
			R.id.rd3,	
	};


	TextView textView1;

	int L_id[] = {
			R.id.L_1,	
			R.id.L_2,	
			R.id.L_3,	
			R.id.L_4,	
	};
	
	int selectPos[];
	LinearLayout L_A[];
	TextView mTextView[];
	
	String cur_day;
	String cur_time;
	ArrayList<HashMap<String, String>> O_ITAB1;
	RadioGroup R_group[];
	RadioButton R_button[][];
	EditText editText2;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}

	public KDH_check_list(Context _context, String I_AUFNR) {
		super(_context);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.kdh_check_list);
		w = getWindow();
		w.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE
				| WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
		w.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
		lp = (WindowManager.LayoutParams) w.getAttributes();
		lp.width = LayoutParams.MATCH_PARENT;
		lp.height = LayoutParams.MATCH_PARENT;
		w.setAttributes(lp);
		this.context = _context;
		cc = new ConnectController2(context, this);

		
		
		TextView tvTitle = (TextView)findViewById(R.id.tv_dialog_title);
		tvTitle.setText(context.getString(R.string.title_check_list));
		
		ImageView img_close = (ImageView) findViewById(R.id.iv_exit);
		img_close.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				dismiss();
			}
		});
		
		Button btn_cancel = (Button) findViewById(R.id.btn_cancel);
		btn_cancel.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				dismiss();
			}
		});
		
		textView1 = (TextView)findViewById(R.id.textView1);
		textView1.setText(I_AUFNR);
		
		L_A = new LinearLayout[L_id.length];
		mTextView = new TextView[L_id.length];
		
		for (int i = 0; i < L_A.length; i++) {
			L_A[i] = (LinearLayout)findViewById(L_id[i]);
			mTextView[i] = (TextView)L_A[i].findViewById(R.id.textView2);
			if(mTextView[i] != null) {
				mTextView[i].setText("");
			}
		}

		cc.getZMO_1120_RD01(I_AUFNR);
		
		String title[] = context.getResources().getStringArray(R.array.check_list_labels);
		String sub_title[] = context.getResources().getStringArray(R.array.check_list_labels2);
		String sub_title2[] = context.getResources().getStringArray(R.array.check_list_labels3);

		LinearLayout L_check[] = new LinearLayout[L_check_id.length];
		TextView text_title[] = new TextView[L_check_id.length];
		R_group = new RadioGroup[L_check_id.length];
		R_button = new RadioButton[L_check_id.length][R_radio_id.length];
		selectPos = new int[L_check_id.length];
		
		for (int i = 0; i < L_check_id.length; i++) 
		{
			L_check[i] = (LinearLayout)findViewById(L_check_id[i]);
			text_title[i]= (TextView)L_check[i].findViewById(R.id.textView1);
			if(text_title[i] != null) {
				text_title[i].setText(title[i]);
			}
			R_group[i] = (RadioGroup)L_check[i].findViewById(R_radio_group_id[i]);
			if(i==3)
			{
				for (int j = 0; j < sub_title2.length; j++) 
				{
					R_button[i][j] = (RadioButton)R_group[i].findViewById(R_radio_id[j]);
					if(R_button[i][j] != null) {
						R_button[i][j].setText(sub_title2[j]);
						if (j == 2) {
							R_button[i][j].setVisibility(View.INVISIBLE);
						}

						if (j == 0) {
							R_button[i][j].setChecked(true);
						}
					}
				}
			}
			else
			{
				for (int j = 0; j < R_radio_id.length; j++) 
				{
					R_button[i][j] = (RadioButton)R_group[i].findViewById(R_radio_id[j]);
					if(R_button[i][j] != null) {
						R_button[i][j].setText(sub_title[j]);
						if (j == 0) {
							R_button[i][j].setChecked(true);
						}
					}
				}
			}
		}


		//비고
		editText2 = (EditText)findViewById(R.id.editText2);

		Button btn_save = (Button)findViewById(R.id.btn_save);
		btn_save.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				
				String check_list_labels2[] = context.getResources().getStringArray(R.array.check_list_labels2);
				String check_list_labels3[] = context.getResources().getStringArray(R.array.check_list_labels3);
				String check_list_data[] = context.getResources().getStringArray(R.array.check_list_data);
				
				for (int i = 0; i < R_group.length; i++) 
				{
					RadioButton rb = (RadioButton)findViewById(R_group[i].getCheckedRadioButtonId());
					String str = rb.getText().toString();
					int index = R_group[i].indexOfChild(rb);
					kog.e("KDH", "tag = "+rb.getText().toString());
					for (int j = 0; j < check_list_labels2.length; j++) 
					{
						if(str.equals(check_list_labels2[j]))
						{
							selectPos[i] = j;
						}
					}
					kog.e("KDH", "selectPos = "+selectPos[i]);
				}
				
				ArrayList<HashMap<String, String>> IT_TAB = new ArrayList<HashMap<String, String>>();
				HashMap<String, String> _data = new HashMap<String, String>();
				LoginModel lm = KtRentalApplication.getLoginModel();
				
				for (int i = 0; i < selectPos.length; i++) {
					kog.e("KDH", "check_list_labels2 = "+check_list_labels2[selectPos[i]]);
				}
				
				_data.put("AUFNR", textView1.getText().toString());
				_data.put("PERNR", lm.getPernr());
				_data.put("INSP_DT", cur_day);
				_data.put("INSP_TM", cur_time);
				_data.put("REP_PART", check_list_data[selectPos[0]]);
				_data.put("IN_STAT", check_list_data[selectPos[1]]);
				_data.put("REP_DUR", check_list_data[selectPos[2]]);
				_data.put("KEEP_REP", check_list_labels3[selectPos[3]]);
				_data.put("EXP_REP", check_list_data[selectPos[4]]);
				_data.put("OTHERS", editText2.getText().toString());
				IT_TAB.add(_data);
				
				cc.getZMO_1120_WR03(IT_TAB);
				
			}
		});
	}

	@Override
	public void connectResponse(String FuntionName, String resultText,
			String MTYPE, int resulCode, TableModel tableModel) {
		//Log.i("#", "####" + FuntionName + "/" + resultText + "/" + MTYPE + "/"
		//		+ resulCode);
		hideProgress();
		kog.e("KDH", "KDH_check_list FuntionName = "+FuntionName);

		if (MTYPE == null || !MTYPE.equals("S")) {
			EventPopupC epc = new EventPopupC(context);
			epc.show(resultText);
			return;
		}
		
		if("ZMO_1120_RD01".equals(FuntionName))
		{
			O_ITAB1 = tableModel.getTableArray();
			HashMap<String, String> ES_CAR = tableModel.getStruct("ES_CAR");
			
			Calendar cal = Calendar.getInstance();
			int year = cal.get(Calendar.YEAR);
			int month = cal.get(Calendar.MONTH) + 1;
			int day = cal.get(Calendar.DAY_OF_MONTH);
			int mHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY); 
			int mMinute = Calendar.getInstance().get(Calendar.MINUTE);

			cur_day = year+CommonUtil.addZero(month)+CommonUtil.addZero(day);
			cur_time = CommonUtil.addZero(mHour)+CommonUtil.addZero(mMinute)+"00";
			
			if(O_ITAB1 != null)
			{
				kog.e("KDH", "ZMO_1120_RD01 O_ITAB1 = "+O_ITAB1.size());
				for (int i = 0; i < O_ITAB1.size(); i++) 
				{
					kog.e("KDH", "AUFNR = "+O_ITAB1.get(i).get("AUFNR"));
					kog.e("KDH", "PERNR = "+O_ITAB1.get(i).get("PERNR"));
					kog.e("KDH", "INSP_DT = "+O_ITAB1.get(i).get("INSP_DT"));
					kog.e("KDH", "INSP_TM = "+O_ITAB1.get(i).get("INSP_TM"));
					kog.e("KDH", "REP_PART = "+O_ITAB1.get(i).get("REP_PART"));
					kog.e("KDH", "IN_STAT = "+O_ITAB1.get(i).get("IN_STAT"));
					kog.e("KDH", "REP_DUR = "+O_ITAB1.get(i).get("REP_DUR"));
					kog.e("KDH", "KEEP_REP = "+O_ITAB1.get(i).get("KEEP_REP"));
					kog.e("KDH", "EXP_REP = "+O_ITAB1.get(i).get("EXP_REP"));
					kog.e("KDH", "OTHERS = "+O_ITAB1.get(i).get("OTHERS"));
					kog.e("KDH", "NAME1 = "+O_ITAB1.get(i).get("NAME1"));
					kog.e("KDH", "ENAME = "+O_ITAB1.get(i).get("ENAME"));
					mTextView[3].setText(O_ITAB1.get(i).get("INSP_DT")+" "+O_ITAB1.get(i).get("INSP_TM"));
							
					ArrayList<String> _check = new ArrayList<String>();
					_check.add(O_ITAB1.get(i).get("REP_PART"));
					_check.add(O_ITAB1.get(i).get("IN_STAT"));
					_check.add(O_ITAB1.get(i).get("REP_DUR"));
					_check.add(O_ITAB1.get(i).get("KEEP_REP"));
					_check.add(O_ITAB1.get(i).get("EXP_REP"));
//					String check_data[] = context.getResources().getStringArray(R.array.check_list_data);
					
//					String check_list_labels2[] = context.getResources().getStringArray(R.array.check_list_labels2);
					String check_list_labels3[] = context.getResources().getStringArray(R.array.check_list_labels3);
					String check_list_data[] = context.getResources().getStringArray(R.array.check_list_data);
					
					for (int j = 0; j < _check.size(); j++) 
					{
						for (int k = 0; k < check_list_data.length; k++) 
						{
							if(j==3)
							{
								if(_check.get(j).equals(check_list_labels3[1]))
								{
									R_button[j][1].setChecked(true);
								}
								else
								{
									R_button[j][0].setChecked(true);
								}
							}
							else
							{
								if(_check.get(j).equals(check_list_data[k]))
								{
									R_button[j][k].setChecked(true);
								}
							}
							
						}
					}
					mTextView[2].setText(O_ITAB1.get(i).get("NAME1"));
					editText2.setText(O_ITAB1.get(i).get("OTHERS"));
				}
			}
			else
			{
				mTextView[3].setText(cur_day+" "+cur_time);
				kog.e("KDH", "ZMO_1120_RD01 O_ITAB1 NULL ");
			}
			
			if(ES_CAR != null)
			{
				if(mTextView[0] != null && ES_CAR != null) {
					mTextView[0].setText(ES_CAR.get("INVNR"));
				}
				if(mTextView[1] != null && ES_CAR != null) {
					mTextView[1].setText(ES_CAR.get("MAKTX"));
				}
			}
			
			
		}
		else if("ZMO_1120_WR03".equals(FuntionName))
		{
			EventPopupC epc = new EventPopupC(context);
			epc.setOnDismissListener(new OnDismissListener() {
				
				@Override
				public void onDismiss(DialogInterface dialog) {
					// TODO Auto-generated method stub
					dismiss();
				}
			});
			epc.show(context.getString(R.string.reg_succ));
		}
		
	}
	
	@Override
	public void reDownloadDB(String newVersion) {
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		
	}
}
