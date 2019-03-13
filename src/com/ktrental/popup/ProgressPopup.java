package com.ktrental.popup;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.view.KeyEvent;
import android.view.WindowManager;
import android.widget.TextView;

import com.ktrental.R;

public class ProgressPopup extends BaseTouchDialog {

	private TextView mTvMassege;

	public ProgressPopup(Context context) {
		super(context);
		setContentView(R.layout.progress_popup);

		WindowManager.LayoutParams lp = this.getWindow().getAttributes();

		// WindowManager.LayoutParams lp =
		// this.getWindow().getAttributes();
		lp.dimAmount = 0.6f;

		this.getWindow().setBackgroundDrawable(new ColorDrawable(0));
		this.setCanceledOnTouchOutside(false);
		this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);

		lp.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;

		this.getWindow().setAttributes(lp);
		mTvMassege = (TextView) findViewById(R.id.tv_message);

		this.setOnKeyListener(new OnKeyListener() {

			@Override
			public boolean onKey(DialogInterface arg0, int keyCode,
					KeyEvent event) {
				// TODO Auto-generated method stub
				if (keyCode == KeyEvent.KEYCODE_BACK) {
					// finish();
					// dismiss();
					return false;
				}
				return true;
			}
		});

	}

	public void setMessage(String message) {
		if (mTvMassege != null) {
			mTvMassege.setText(message);
		}
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		// super.onBackPressed();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:

			break;

		default:
			break;
		}
		return super.onKeyDown(keyCode, event);
	}

}
