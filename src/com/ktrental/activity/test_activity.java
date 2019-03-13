package com.ktrental.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.ktrental.R;
import com.ktrental.util.kog;

public class test_activity extends Activity{


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
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.kdh_check_list);
		textView1 = (TextView)findViewById(R.id.textView1);

		LinearLayout L_A[] = new LinearLayout[L_id.length];

		for (int i = 0; i < L_A.length; i++) {
			L_A[i] = (LinearLayout)findViewById(L_id[i]);
			TextView text = (TextView)L_A[i].findViewById(R.id.textView2);
			text.setText("");
		}

		String title[] = getResources().getStringArray(R.array.check_list_labels);
		String sub_title[] = getResources().getStringArray(R.array.check_list_labels2);
		String sub_title2[] = getResources().getStringArray(R.array.check_list_labels3);

		LinearLayout L_check[] = new LinearLayout[L_check_id.length];
		TextView text_title[] = new TextView[L_check_id.length];
		final RadioGroup R_group[] = new RadioGroup[L_check_id.length];
		final RadioButton R_button[] = new RadioButton[R_radio_id.length];
		selectPos = new int[L_check_id.length];
		
		for (int i = 0; i < L_check_id.length; i++) 
		{
			L_check[i] = (LinearLayout)findViewById(L_check_id[i]);
			text_title[i]= (TextView)L_check[i].findViewById(R.id.textView1);
			text_title[i].setText(title[i]);
			R_group[i] = (RadioGroup)L_check[i].findViewById(R_radio_group_id[i]);
			if(i==3)
			{
				for (int j = 0; j < sub_title2.length; j++) 
				{
					R_button[j] = (RadioButton)R_group[i].findViewById(R_radio_id[j]);
					R_button[j].setText(sub_title2[j]);
					if(j==2)
					{
						R_button[j].setVisibility(View.INVISIBLE);
					}

					if(j==0)
					{
						R_button[j].setChecked(true);
					}
				}
			}
			else
			{
				for (int j = 0; j < R_radio_id.length; j++) 
				{
					R_button[j] = (RadioButton)R_group[i].findViewById(R_radio_id[j]);
					R_button[j].setText(sub_title[j]);
					if(j==0)
					{
						R_button[j].setChecked(true);
					}
				}
			}
		}


		final EditText ed = (EditText)findViewById(R.id.editText2);


		Button btn_save = (Button)findViewById(R.id.btn_save);
		btn_save.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				
				String sub_title[] = getResources().getStringArray(R.array.check_list_labels2);
				
				for (int i = 0; i < R_group.length; i++) 
				{
					RadioButton rb = (RadioButton)findViewById(R_group[i].getCheckedRadioButtonId());
					String str = rb.getText().toString();
					int index = R_group[i].indexOfChild(rb);
					kog.e("KDH", "tag = "+rb.getText().toString());
					for (int j = 0; j < sub_title.length; j++) 
					{
						if(str.equals(sub_title[j]))
						{
							selectPos[i] = j;
						}
					}
					kog.e("KDH", "selectPos = "+selectPos[i]);
				}
				
			}
			
		});
		Button btn_cancel = (Button)findViewById(R.id.btn_cancel);
		btn_cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

			}
		});
	}
}
