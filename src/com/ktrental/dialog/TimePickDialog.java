package com.ktrental.dialog;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.ktrental.R;
import com.ktrental.adapter.Account_List_Dialog_Adapter;
import com.ktrental.cm.connect.ConnectController2;
import com.ktrental.cm.connect.Connector.ConnectInterface;
import com.ktrental.model.CorCardAccountModel;
import com.ktrental.model.TableModel;
import com.ktrental.popup.EventPopupC;
import com.ktrental.util.CommonUtil;
import com.ktrental.util.kog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Handler;

public class TimePickDialog extends DialogC implements View.OnClickListener  {

	private TextView mBtnLeftInput, mBtnRightInput;
	private EditText mEtDetail;

	public static final int TYPE_LEFT = 0; // 시
	public static final int TYPE_RIGHT = 1; // 분
	private int mSelectedType = TYPE_LEFT;

	public TimePickDialog(Context context) {
		super(context);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setContentView(R.layout.time_pick_dialog);

		mBtnLeftInput = (TextView)findViewById(R.id.tv_inventory_input_left);
		mBtnLeftInput.setOnClickListener(this);
		mBtnRightInput = (TextView)findViewById(R.id.tv_inventory_input_right);
		mBtnRightInput.setOnClickListener(this);

		mEtDetail = (EditText)findViewById(R.id.detail_text);

		findViewById(R.id.fl_inventory_input_left).setOnClickListener(this);
		findViewById(R.id.fl_inventory_input_right).setOnClickListener(this);
		findViewById(R.id.inventory_bt_num_0).setOnClickListener(this);
		findViewById(R.id.inventory_bt_num_1).setOnClickListener(this);
		findViewById(R.id.inventory_bt_num_2).setOnClickListener(this);
		findViewById(R.id.inventory_bt_num_3).setOnClickListener(this);
		findViewById(R.id.inventory_bt_num_4).setOnClickListener(this);
		findViewById(R.id.inventory_bt_num_5).setOnClickListener(this);
		findViewById(R.id.inventory_bt_num_6).setOnClickListener(this);
		findViewById(R.id.inventory_bt_num_7).setOnClickListener(this);
		findViewById(R.id.inventory_bt_num_8).setOnClickListener(this);
		findViewById(R.id.inventory_bt_num_9).setOnClickListener(this);
		// findViewById(R.id.inventory_bt_exit).setOnClickListener(this);
		findViewById(R.id.inventory_bt_delete).setOnClickListener(this);
		findViewById(R.id.inventory_bt_clear).setOnClickListener(this);
		findViewById(R.id.inventory_bt_done).setOnClickListener(this);
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.inventory_bt_num_0:
				setInput("0", false);
				break;
			case R.id.inventory_bt_num_1:
				setInput("1", false);
				break;
			case R.id.inventory_bt_num_2:
				setInput("2", false);
				break;
			case R.id.inventory_bt_num_3:
				setInput("3", false);
				break;
			case R.id.inventory_bt_num_4:
				setInput("4", false);
				break;
			case R.id.inventory_bt_num_5:
				setInput("5", false);
				break;
			case R.id.inventory_bt_num_6:
				setInput("6", false);
				break;
			case R.id.inventory_bt_num_7:
				setInput("7", false);
				break;
			case R.id.inventory_bt_num_8:
				setInput("8", false);
				break;
			case R.id.inventory_bt_num_9:
				setInput("9", false);
				break;
			// case R.id.inventory_bt_exit:
			// //
			// this.dismiss();
			// setInput("CLEAR", true);
			// break;
			case R.id.inventory_bt_delete:
				setInput("0", true);
				break;
			case R.id.inventory_bt_clear:
				setInput("CLEAR", true);
				break;
			case R.id.inventory_bt_done:
//				if (mDismissListener != null) {
//					String result = "";
//					if (mBtnLeftInput != null) {
//						result = mBtnLeftInput.getText().toString();
//						result = result + mBtnRightInput.getText().toString();
//						mBtnLeftInput.setText("");
//						mBtnRightInput.setText("");
//					}
//					result = CommonUtil.setDotTime(result);
//					// checkTime();
//					mDismissListener.onDismiss(result, mPosition);
//					mDismissListener = null;
//				}
				// setInput("CLEAR", true);
				this.dismiss();
				break;
			case R.id.tv_inventory_input_left:
				setFocusInput(TYPE_LEFT);
				break;
			case R.id.tv_inventory_input_right:
				setFocusInput(TYPE_RIGHT);
				break;
			case R.id.fl_inventory_input_left:
				setFocusInput(TYPE_LEFT);
				break;
			case R.id.fl_inventory_input_right:
				setFocusInput(TYPE_RIGHT);
				break;
			default:
				break;
		}
	}

	private void setInput(String num, boolean delFlag) {

		TextView tvCurrent;

		if (num.equals("CLEAR")) {
			setFocusInput(TYPE_LEFT);
			mBtnLeftInput.setText("");
			mBtnRightInput.setText("");
		}

		if (mSelectedType == TYPE_LEFT) {
			// if (mBtnLeftInput.length() == 2) {
			// setFocusInput(TYPE_RIGHT);
			// return;
			// }

			tvCurrent = mBtnLeftInput;

		} else {
			tvCurrent = mBtnRightInput;
		}

		if (!delFlag) {
			String text = tvCurrent.getText().toString();
			if (text.equals("00"))
				tvCurrent.setText("");

			if (tvCurrent.length() == 2)
				return;

			num = tvCurrent.getText().toString() + num;
			tvCurrent.setText(num);
			if (mSelectedType == TYPE_LEFT) {
				if (mBtnLeftInput.length() == 2) {
					setFocusInput(TYPE_RIGHT);
					tvCurrent = mBtnRightInput;
				}
			}
		} else {
			if (tvCurrent.length() == 0)
				return;
			String str = tvCurrent.getText().toString();
			StringBuilder sb = new StringBuilder(str);
			sb.deleteCharAt(sb.length() - 1);
			num = sb.toString();
			tvCurrent.setText(num);
		}

	}

	private void setFocusInput(int type) {
		if (type == TYPE_LEFT) {
			mBtnLeftInput
					.setBackgroundResource(R.drawable.popup_calculator_focus);
			mBtnRightInput.setBackgroundDrawable(null);
		} else {
			mBtnRightInput
					.setBackgroundResource(R.drawable.popup_calculator_focus);
			mBtnLeftInput.setBackgroundDrawable(null);
		}
		mSelectedType = type;
	}
}
