package com.ktrental.dialog;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ktrental.R;
import com.ktrental.popup.BaseTouchDialog;

public class TimePickDialog extends BaseTouchDialog implements View.OnClickListener  {

	private TextView mLeftInput, mRightInput;
	private EditText mEtDetail;
	private Context mContext;

	public static final int TYPE_LEFT = 0; // 시
	public static final int TYPE_RIGHT = 1; // 분
	private int mSelectedType = TYPE_LEFT;

	public TimePickDialog(Context context) {
		super(context);
		mContext = context;
		requestWindowFeature(Window.FEATURE_NO_TITLE);
	}

	private OnTimePickListener mListener;

	public interface OnTimePickListener {
		void onTimePickResult(String time,String memo);
	}

	public void setOnTimePickListener(OnTimePickListener listener) {
		this.mListener = listener;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setContentView(R.layout.time_pick_dialog);

		setCanceledOnTouchOutside(false);

		mLeftInput = (TextView)findViewById(R.id.tv_inventory_input_left);
		mLeftInput.setOnClickListener(this);
		mRightInput = (TextView)findViewById(R.id.tv_inventory_input_right);
		mRightInput.setOnClickListener(this);

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
		findViewById(R.id.btn_cancel).setOnClickListener(this);
		findViewById(R.id.btn_confirm).setOnClickListener(this);
	}

	@Override
	protected void onStart() {
		super.onStart();

		InputMethodManager im = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
		im.hideSoftInputFromWindow(mEtDetail.getWindowToken(), 0);

		mEtDetail.setFocusable(true);
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
			case R.id.btn_confirm:
				if (timeValidation(mLeftInput.getText().toString().trim(), mRightInput.getText().toString().trim())) {
					mListener.onTimePickResult(mLeftInput.getText().toString().trim()+mRightInput.getText().toString().trim(), mEtDetail.getText().toString());
					this.dismiss();
				} else {
					Toast.makeText(mContext, "시간 형식이 잘못되었습니다.", Toast.LENGTH_SHORT).show();
				}

//				if (mDismissListener != null) {
//					String result = "";
//					if (mLeftInput != null) {
//						result = mLeftInput.getText().toString();
//						result = result + mRightInput.getText().toString();
//						mLeftInput.setText("");
//						mRightInput.setText("");
//					}
//					result = CommonUtil.setDotTime(result);
//					// checkTime();
//					mDismissListener.onDismiss(result, mPosition);
//					mDismissListener = null;
//				}
				// setInput("CLEAR", true);
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

			case R.id.btn_cancel:
				dismiss();
				break;
			default:
				break;
		}
	}

	private void setInput(String num, boolean delFlag) {

		TextView tvCurrent;

		if (num.equals("CLEAR")) {
			setFocusInput(TYPE_LEFT);
			mLeftInput.setText("");
			mRightInput.setText("");
		}

		if (mSelectedType == TYPE_LEFT) {
			// if (mLeftInput.length() == 2) {
			// setFocusInput(TYPE_RIGHT);
			// return;
			// }

			tvCurrent = mLeftInput;

		} else {
			tvCurrent = mRightInput;
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
				if (mLeftInput.length() == 2) {
					setFocusInput(TYPE_RIGHT);
					tvCurrent = mRightInput;
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
			mLeftInput
					.setBackgroundResource(R.drawable.popup_calculator_focus);
			mRightInput.setBackgroundDrawable(null);
		} else {
			mRightInput
					.setBackgroundResource(R.drawable.popup_calculator_focus);
			mLeftInput.setBackgroundDrawable(null);
		}
		mSelectedType = type;
	}

	private boolean timeValidation(String hour, String minute) {
		int intHour = Integer.parseInt(hour);
		int intMinute = Integer.parseInt(minute);

		if (intHour > 23 | intMinute > 60) {
			return false;
		} else {
			return true;
		}
	}
}
