package com.ktrental.popup;

import android.content.Context;
import android.telephony.PhoneNumberUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.TextView;

import com.ktrental.R;
import com.ktrental.util.CommonUtil;

/**
 * 수량입력 팝업. <br/>
 * QucikAction({@link QuickAction}) 이라는 팝업을 상속받아 구현.
 * 
 * @author Hong
 * @since 2013.07.12
 */
public class NumberInputPopup extends QuickAction implements OnClickListener,
		OnDismissListener {

	private TextView mBtnInput;
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
	public static final int TYPE_NORMAL_NUMBER = 5; // 일반번호
	public static final int TYPE_TIME = 6; // 시간
	public static final int TYPE_NORMAL_NUMBER_UNDER_100 = 7; // 100보다 작은 자연수
	private int mType = 0;
	private String mRealText = "";
	private boolean DONE = false;

	public interface OnDismissListener {

		public abstract void onDismiss(String result, int position);
	}

	public NumberInputPopup(Context context) {
		super(context, QuantityPopup.HORIZONTAL);
		initViewSettings(R.layout.number_input_popup);
		mType = 5;
	}

	public NumberInputPopup(Context context, int nType) {
		super(context, QuantityPopup.HORIZONTAL);
		initViewSettings(R.layout.number_input_popup);
		mType = nType;
	}

	public void setOnDismissListener(OnDismissListener listener) {
		setOnDismissListener(this);
		mDismissListener = listener;
	}

	private void initViewSettings(int layoutId) {
		addLayout(layoutId);
		mBtnInput = (TextView) mTrack.findViewById(R.id.inventory_bt_input);
		mBtnInput.setOnClickListener(this);
		mTrack.findViewById(R.id.inventory_bt_input).setOnClickListener(this);
		mTrack.findViewById(R.id.inventory_bt_num_0).setOnClickListener(this);
		mTrack.findViewById(R.id.inventory_bt_num_1).setOnClickListener(this);
		mTrack.findViewById(R.id.inventory_bt_num_2).setOnClickListener(this);
		mTrack.findViewById(R.id.inventory_bt_num_3).setOnClickListener(this);
		mTrack.findViewById(R.id.inventory_bt_num_4).setOnClickListener(this);
		mTrack.findViewById(R.id.inventory_bt_num_5).setOnClickListener(this);
		mTrack.findViewById(R.id.inventory_bt_num_6).setOnClickListener(this); 
		mTrack.findViewById(R.id.inventory_bt_num_7).setOnClickListener(this);
		mTrack.findViewById(R.id.inventory_bt_num_8).setOnClickListener(this);
		mTrack.findViewById(R.id.inventory_bt_num_9).setOnClickListener(this);
		// mTrack.findViewById(R.id.inventory_bt_num_dot).setOnClickListener(this);
		// mTrack.findViewById(R.id.inventory_bt_exit).setOnClickListener(this);
		mTrack.findViewById(R.id.inventory_bt_delete).setOnClickListener(this);
		mTrack.findViewById(R.id.inventory_bt_clear).setOnClickListener(this);
		mTrack.findViewById(R.id.inventory_bt_done).setOnClickListener(this);
	}

	public ViewGroup getViewGroup() {
		return mTrack;
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
		case R.id.inventory_bt_num_dot:
			setInput(".", false);
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
			if (mDismissListener != null) {
				String result = "";
				if (mBtnInput != null) {
					result = mBtnInput.getText().toString();
					mBtnInput.setText("");
				}
				// checkTime();
				mDismissListener.onDismiss(result, mPosition);
				mDismissListener = null;
			}
			// setInput("CLEAR", true);
			this.dismiss();
			break;
		default:
			break;
		}
	}

	private void checkTime() {
		boolean checkTime = false;
		if (mType == TYPE_TIME) {
			if (mBtnInput.getText().length() > 4) {
				String time = mBtnInput.getText().toString()
						.replaceAll(":", "");
				int hour = Integer.parseInt(time.substring(0, 2));
				int min = Integer.parseInt(time.substring(2, 4));
				if (hour > 24) {
				}
				if (min > 50) {
				} else {
					checkTime = true;
				}
			}
		}
	}

	public void setInput(String num, boolean delFlag) {
		if (mBtnInput.getText() != null && mType == TYPE_NORMAL_NUMBER) {
			if (mBtnInput.getText().toString().length() >= 3 && !delFlag)
				return;
		}
		if (num.equals("CLEAR")) {
			mBtnInput.setText("");
			mRealText = "";
			return;
		}
		if (!delFlag) {
			if (mBtnInput.getText().length() < 13) {
				if (mType == TYPE_TIME) {
					if (mBtnInput.getText().length() > 4) {
						return;
					}
				}
				// else
				// {
				// if (mType != TYPE_PHONE_NUMBER)
				// {
				// if (mRealText.equals("0"))
				// {
				// mRealText = "";
				// mBtnInput.setText("");
				// }
				// }
				// }
				mRealText = mRealText + num;
				num = mBtnInput.getText().toString() + num;
				num = getTypeInputData(num);
				mBtnInput.setText(num);
			}
		} else {
			String text = mBtnInput.getText().toString();
			if (text.length() > 0) {
				if (mType == TYPE_PHONE_NUMBER) {
					StringBuilder sb = new StringBuilder(mRealText);
					sb.deleteCharAt(sb.length() - 1);
					mRealText = sb.toString();
					text = getTypeInputData(mRealText);
					mBtnInput.setText(text);
				} else {
					StringBuilder sb = new StringBuilder(text);
					sb.deleteCharAt(sb.length() - 1);
					text = sb.toString();
					text = getMinusString(text);
					text = getTypeInputData(text);
					mBtnInput.setText(text);
				}
			}
		}
	}

	@Override
	public void onDismiss() {
		setInput("CLEAR", true);
		if (mDismissListener != null) {
			String result = "";
			mDismissListener.onDismiss(result, mPosition);
		}
		super.onDismiss();
	}

	public void show(View anchor, int position) {
		DONE = false;
		mPosition = position;
		super.show(anchor);
	}

	public void show(View anchor, int position, String num) {
		mBtnInput.setText(num);
		show(anchor, position);
	}

	public void show(View anchor, int position, int popWidth, int popHeight) {
		mPosition = position;
		super.show(anchor, popWidth, popHeight);
	}

	public void show(View anchor, int position, int popWidth, int popHeight,
			int[] location) {
		mPosition = position;
		super.show(anchor, popWidth, popHeight, location);
	}

	private String getTypeInputData(String aText) {
		String resultText = aText;
		switch (mType) {
		case TYPE_PHONE_NUMBER:
			aText = aText.replaceAll("-", "");
			resultText = PhoneNumberUtils.formatNumber(aText);
			if (!resultText.matches("-")) {
				resultText = CommonUtil.setPhoneNumber(aText);
			}
			break;
		case TYPE_MONEY:
			aText = aText.replaceAll(",", "");
			resultText = CommonUtil.currentpoint(aText);
			break;
		case TYPE_SOCIAL_SECURITY_NUMBER:
			if (aText.length() < 15)
				resultText = CommonUtil.setSocialNum(aText);
			else {
				resultText = resultText.substring(0, 14);
			}
			break;
		case TYPE_BUSINESS_NUMBER:
			if (aText.length() < 13)
				resultText = CommonUtil.setBusinessNum(aText);
			else {
				resultText = resultText.substring(0, 12);
			}
			break;
		case TYPE_DRIVING_NUMBER:
			if (aText.length() < 13)
				resultText = CommonUtil.setDrivingNum(aText);
			else {
				resultText = resultText.substring(0, 12);
			}
			break;
		case TYPE_TIME:
			aText = aText.replaceAll(":", "");
			resultText = CommonUtil.setTime(aText);
			break;
		default:
			break;
		}
		return resultText;
	}

	private String getMinusString(String result) {
		if (result != null) {
			switch (mType) {
			case TYPE_SOCIAL_SECURITY_NUMBER:
				result = deleteMinus(result, 6);
				break;
			case TYPE_BUSINESS_NUMBER:
				result = deleteMinus(result, 3);
				result = deleteMinus(result, 6);
				break;
			case TYPE_DRIVING_NUMBER:
				result = deleteMinus(result, 2);
				result = deleteMinus(result, 9);
				break;
			case TYPE_MONEY:
				result = result.replaceAll(",", "");
				break;
			case TYPE_PHONE_NUMBER:
				result = result.replaceAll("-", "");
				break;
			case TYPE_TIME:
				result = result.replaceAll(":", "");
				break;
			default:
				break;
			}
		}
		return result;
	}

	private String deleteMinus(String result, int standardLength) {
		int length = result.length();
		if (length == standardLength) {
			result = result.substring(0, standardLength - 1);
		}
		return result;
	}

	public boolean isDONE() {
		return DONE;
	}

	public void setDONE(boolean dONE) {
		DONE = dONE;
	}

	public void setOnDismissListener(
			InventoryPopup.OnDismissListener onDismissListener) {
		// TODO Auto-generated method stub

	}
}
