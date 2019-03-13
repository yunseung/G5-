package com.ktrental.popup;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.ktrental.R;
import com.ktrental.util.OnEventOkListener;

public class EventPopup2 extends Dialog implements
		View.OnClickListener {

	private String mBody = "";

	private OnEventOkListener mOnEventPopupListener;

	public EventPopup2(Context context, String body,
			OnEventOkListener onEventPopupListener) {
		super(context, R.style.Theme_Dialog);

		mBody = body;
		mOnEventPopupListener = onEventPopupListener;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.event_popup2);
		findViewById(R.id.btn_ok).setOnClickListener(this);

		TextView tvBody = (TextView) findViewById(R.id.tv_body);
		tvBody.setText(mBody);

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btn_ok:
			clickOk();
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

}
