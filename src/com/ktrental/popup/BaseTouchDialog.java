package com.ktrental.popup;

import android.app.Dialog;
import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.ktrental.util.OnEventCancelListener;
import com.ktrental.util.OnEventOkListener;

public class BaseTouchDialog extends Dialog {

	protected Context mContext;

	public BaseTouchDialog(Context context) {
		super(context);
		mContext = context;
		// TODO Auto-generated constructor stub
	}

	public BaseTouchDialog(Context context, int theme) {
		super(context, theme);
		mContext = context;
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		View v = getCurrentFocus();
		boolean ret = super.dispatchTouchEvent(ev);

		if (v instanceof EditText) {
			View w = getCurrentFocus();
			int scrcoords[] = new int[2];
			w.getLocationOnScreen(scrcoords);
			float x = ev.getRawX() + w.getLeft() - scrcoords[0];
			float y = ev.getRawY() + w.getTop() - scrcoords[1];

			if (ev.getAction() == MotionEvent.ACTION_UP
					&& (x < w.getLeft() || x >= w.getRight() || y < w.getTop() || y > w
							.getBottom())) {

				InputMethodManager imm = (InputMethodManager) mContext
						.getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(getWindow().getCurrentFocus()
						.getWindowToken(), 0);
			}
		}
		return ret;
	}

	public void showEventPopup1(String title, String body, String okText,
			OnEventOkListener onEventPopupListener,
			OnEventCancelListener onEventCancelListener) {
		EventPopup1 popup = new EventPopup1(mContext, body,
				onEventPopupListener);
		popup.setOnCancelListener(onEventCancelListener);
		popup.show();
		popup.setOkButtonText(okText);
	}

	public void showEventPopup1(String title, String body,
			OnEventOkListener onEventPopupListener,
			OnEventCancelListener onEventCancelListener) {
		EventPopup1 popup = new EventPopup1(mContext, body,
				onEventPopupListener);
		popup.setOnCancelListener(onEventCancelListener);
		popup.show();
	}

	public void showEventPopup2(OnEventOkListener onEventPopupListener,
			String body) {
		EventPopup2 eventPopup = new EventPopup2(mContext, body,
				onEventPopupListener);

		eventPopup.show();

	}
}
