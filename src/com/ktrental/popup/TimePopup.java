package com.ktrental.popup;

import java.util.ArrayList;

import kankan.wheel.widget.OnWheelChangedListener;
import kankan.wheel.widget.WheelView;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.PopupWindow.OnDismissListener;

import com.ktrental.R;
import com.ktrental.adapter.AddressWheelAdapter;

public class TimePopup extends QuickAction implements OnWheelChangedListener,
		View.OnClickListener, OnDismissListener {

	private Context mContext;
	private WheelView mWvHour;
	private WheelView mWvMin;

	private View mAnchorView;
	private Button mBtnSelected;

	private OnTimeListener mOnTimeListener;

	public interface OnTimeListener {
		void onTimeEnd(String time);
	}

	public TimePopup(Context context, OnTimeListener aOnTimeListener) {
		super(context);
		mContext = context;
		mOnTimeListener = aOnTimeListener;
		setOnDismissListener(this);
		initSettingView();
	}

	private void initSettingView() {

		// ViewGroup root = (ViewGroup) LayoutInflater.from(mContext).inflate(
		// layoutId, null, false);
		//
		// setContentView(root);
		addLayout(R.layout.time_select_layout);
		mWvHour = (WheelView) mTrack.findViewById(R.id.wv_hour);
		mWvHour.setCenterDrawable(mContext.getResources().getDrawable(
				R.drawable.popup_wheel_center));
		mWvHour.addChangingListener(this);

		mWvMin = (WheelView) mTrack.findViewById(R.id.wv_min);
		mWvMin.setCenterDrawable(mContext.getResources().getDrawable(
				R.drawable.popup_wheel_center));
		mWvMin.addChangingListener(this);

		mBtnSelected = (Button) mTrack.findViewById(R.id.btn_select);
		mBtnSelected.setOnClickListener(this);
		initWheel();
	}

	private void initWheel() {

		ArrayList<String> hourArray = new ArrayList<String>();
		for (int i = 1; i < 25; i++) {
			hourArray.add("" + i);
		}

		AddressWheelAdapter hourAdapter = new AddressWheelAdapter(mContext,
				hourArray);
		hourAdapter.setItemResource(R.layout.wheel_text_item);
		hourAdapter.setItemTextResource(R.id.text);
		mWvHour.setViewAdapter(hourAdapter);

		ArrayList<String> minArray = new ArrayList<String>();
		for (int i = 0; i < 51; i = i + 10) {
			int count = i;
			String min = "";
			if (count < 10) {
				min = "0";
			}
			minArray.add(min + i);
		}

		AddressWheelAdapter minAdapter = new AddressWheelAdapter(mContext,
				minArray);
		minAdapter.setItemResource(R.layout.wheel_text_item);
		minAdapter.setItemTextResource(R.id.text);
		mWvMin.setViewAdapter(minAdapter);
	}

	@Override
	public void onChanged(WheelView wheelView, int arg1, int arg2) {

		switch (wheelView.getId()) {
		case R.id.wv_hour:

			break;
		case R.id.wv_min:

			break;

		default:
			break;
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btn_select:

			int hour = mWvHour.getCurrentItem() + 1;
			int min = mWvMin.getCurrentItem();
			String hourStr = "";
			if (hour < 10)
				hourStr = hourStr + 0;

			min = min * 10;

			String minStr = "";
			if (min < 10)
				minStr = minStr + 0;

			String time = hourStr + "" + hour + ":" + minStr + "" + min;
			if (mOnTimeListener != null)
				mOnTimeListener.onTimeEnd(time);
			dismiss();
			break;

		default:
			break;
		}
	}

	@Override
	public void onDismiss() {

		super.onDismiss();
	}
}
