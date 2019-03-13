package com.ktrental.popup;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.ktrental.R;
import com.ktrental.util.OnEventCancelListener;
import com.ktrental.util.OnEventOkListener;

public class EventPopup1 extends Dialog implements View.OnClickListener {

	private String mBody = "";

	private OnEventOkListener mOnEventPopupListener;
	private OnEventCancelListener mOnEventCancelListener;

	private Button mBtnOk;
	private Button mBtnCancel;

	public EventPopup1(Context context, String body,
			OnEventOkListener onEventPopupListener) {
		super(context, R.style.Theme_Dialog);

		mBody = body;
		mOnEventPopupListener = onEventPopupListener;
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.event_popup_1);
		mBtnOk = (Button) findViewById(R.id.btn_ok);
		mBtnOk.setOnClickListener(this);
		mBtnCancel = (Button) findViewById(R.id.btn_cancel);
		mBtnCancel.setOnClickListener(this);

		TextView tvBody = (TextView) findViewById(R.id.tv_body);
		tvBody.setText(mBody);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btn_ok:
			clickOk();
			break;
		case R.id.btn_cancel:
			clickCancel();
			break;

		default:
			break;
		}
	}

	private void clickOk() {
		if (mOnEventPopupListener != null)
			mOnEventPopupListener.onOk();

		dismiss();
	}

	private void clickCancel() {
		if (mOnEventCancelListener != null)
			mOnEventCancelListener.onCancel();

		dismiss();
	}

	public void setOkButtonText(String text) {
		mBtnOk.setText(text);
	}

	public void setCancelButtonText(String text) {
		mBtnCancel.setText(text);
	}

	public void setOnCancelListener(OnEventCancelListener onEventCancelListener) {
		mOnEventCancelListener = onEventCancelListener;
	}
}
