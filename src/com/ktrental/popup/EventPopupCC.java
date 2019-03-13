package com.ktrental.popup;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.ktrental.R;

public class EventPopupCC extends Dialog {

	private String mBody = "";

	private Button mBtnOk;
	private Button mBtnCancel;

	public EventPopupCC(Context context, String body) {
		super(context, R.style.Theme_Dialog);
		

		mBody = body;
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.event_popup_1);
		mBtnOk = (Button) findViewById(R.id.btn_ok);
		mBtnCancel = (Button) findViewById(R.id.btn_cancel);

		TextView tvBody = (TextView) findViewById(R.id.tv_body);
		tvBody.setText(mBody);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

	}

	public void setOkButtonText(String text) {
		mBtnOk.setText(text);
	}

	public void setCancelButtonText(String text) {
		mBtnCancel.setText(text);
	}

}
