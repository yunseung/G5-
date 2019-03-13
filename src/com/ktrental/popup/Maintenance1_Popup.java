package com.ktrental.popup;

import java.util.ArrayList;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ktrental.R;

/**
 * 수량입력 팝업. <br/>
 * QucikAction({@link QuickAction}) 이라는 팝업을 상속받아 구현.
 * 
 * 
 * 
 * @author Hong
 * @since 2013.07.12
 */
public class Maintenance1_Popup extends QuickAction {

	// private Button mBtnInput;
	private OnDismissListener mDismissListener;

	private int mPosition = -1; // 현재 리스트 포지션

	/**
	 * Listener for window dismiss
	 * 
	 */

	public static final int TYPE_PHONE_NUMBER = 0; // 전화번호
	public static final int TYPE_MONEY = 1; // 돈
	public static final int TYPE_SOCIAL_SECURITY_NUMBER = 2; // 주민등록번호
	public static final int TYPE_BUSINESS_NUMBER = 3; // 사업자등록번호
	public static final int TYPE_DRIVING_NUMBER = 4; // 운전면허번호

	private int mType = 0;
	private String mRealText = "";

	private ArrayList<String> list;

	public interface OnDismissListener {
		public abstract void onDismiss(String result, int position);
	}

	public Maintenance1_Popup(Context context, int orientation, int layoutId,
			int type, ArrayList<String> list) {
		super(context, orientation);
		mType = type;
		this.list = list;
		initViewSettings(layoutId);
	}

	public void setOnDismissListener(OnDismissListener listener) {
		setOnDismissListener(this);
		mDismissListener = listener;
	}

	TextView tv[];
	int[] tv_id = { R.id.maintenance_filter1_1_id,
			R.id.maintenance_filter1_2_id, R.id.maintenance_filter1_3_id,
			R.id.maintenance_filter1_4_id, R.id.maintenance_filter1_5_id };

	private void initViewSettings(int layoutId) {
		addLayout(layoutId);

		tv = new TextView[list.size()];
		for (int i = 0; i < tv.length; i++) {
			tv[i] = (TextView) mTrack.findViewById(tv_id[i]);
			tv[i].setText(list.get(i));
		}

	}

	public ViewGroup getViewGroup() {
		return mTrack;
	}

	public void show(View anchor, int position) {
		mPosition = position;
		super.show(anchor);
	}
}
