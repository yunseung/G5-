package com.ktrental.popup;

import android.content.Context;
import android.telephony.PhoneNumberUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.PopupWindow.OnDismissListener;

import com.ktrental.R;
import com.ktrental.util.CommonUtil;

/**
 * 수량입력 팝업. <br/>
 * QucikAction({@link QuickAction}) 이라는 팝업을 상속받아 구현.
 * 
 * 
 * 
 * @author Hong
 * @since 2013.07.12
 */
public class QuantityPopup extends QuickAction implements OnClickListener,
		OnDismissListener {

	private Button mBtnInput;
	private OnDismissListener mDismissListener;

	private int mPosition = -1; // 현재 리스트 포지션

	/**
	 * Listener for window dismiss
	 * 
	 */
	public static final int TYPE_NOMARL_NUMBER = 0; // 일반 번호
	public static final int TYPE_PHONE_NUMBER = 5; // 전화번호
	public static final int TYPE_MONEY = 1; // 돈
	public static final int TYPE_SOCIAL_SECURITY_NUMBER = 2; // 주민등록번호
	public static final int TYPE_BUSINESS_NUMBER = 3; // 사업자등록번호
	public static final int TYPE_DRIVING_NUMBER = 4; // 운전면허번호

	private int mType = 0;
	private String mRealText = "";

	public interface OnDismissListener {
		public abstract void onDismiss(String result, int position);
	}

	public QuantityPopup(Context context, int orientation, int layoutId,
			int type) {
		super(context, orientation);
		initViewSettings(layoutId);
		mType = type;

	}

	public void setOnDismissListener(OnDismissListener listener) {
		setOnDismissListener(this);
		mDismissListener = listener;
	}

	private void initViewSettings(int layoutId) {
		addLayout(layoutId);
		mBtnInput = (Button) mTrack.findViewById(R.id.btn_input);
		mBtnInput.setOnClickListener(this);
		mTrack.findViewById(R.id.btn_input).setOnClickListener(this);
		mTrack.findViewById(R.id.btn_num_0).setOnClickListener(this);
		mTrack.findViewById(R.id.btn_num_1).setOnClickListener(this);
		mTrack.findViewById(R.id.btn_num_2).setOnClickListener(this);
		mTrack.findViewById(R.id.btn_num_3).setOnClickListener(this);
		mTrack.findViewById(R.id.btn_num_4).setOnClickListener(this);
		mTrack.findViewById(R.id.btn_num_5).setOnClickListener(this);
		mTrack.findViewById(R.id.btn_num_6).setOnClickListener(this);
		mTrack.findViewById(R.id.btn_num_7).setOnClickListener(this);
		mTrack.findViewById(R.id.btn_num_8).setOnClickListener(this);
		mTrack.findViewById(R.id.btn_num_9).setOnClickListener(this);
		mTrack.findViewById(R.id.btn_exit).setOnClickListener(this);
		mTrack.findViewById(R.id.btn_delete).setOnClickListener(this);
		mTrack.findViewById(R.id.btn_clear).setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btn_num_0:
			setInput("0", false);
			break;
		case R.id.btn_num_1:
			setInput("1", false);
			break;
		case R.id.btn_num_2:
			setInput("2", false);
			break;
		case R.id.btn_num_3:
			setInput("3", false);
			break;
		case R.id.btn_num_4:
			setInput("4", false);
			break;
		case R.id.btn_num_5:
			setInput("5", false);
			break;
		case R.id.btn_num_6:
			setInput("6", false);
			break;
		case R.id.btn_num_7:
			setInput("7", false);
			break;
		case R.id.btn_num_8:
			setInput("8", false);
			break;
		case R.id.btn_num_9:
			setInput("9", false);
			break;
		case R.id.btn_exit:
			this.dismiss();
			break;
		case R.id.btn_delete:
			setInput("0", true);
			break;
		case R.id.btn_clear:
			setInput("CLEAR", true);
			break;

		default:
			break;
		}
	}

	public void setInput(String num, boolean delFlag) {

		if (num.equals("CLEAR")) {
			mBtnInput.setText("");
			mRealText = "";
			return;
		}

		mRealText = mRealText + num;

		if (!delFlag) {
			num = mBtnInput.getText().toString() + num;

			num = getTypeInputData(num);

			mBtnInput.setText(num);
		} else {
			String text = mBtnInput.getText().toString();
			if (text.length() > 0) {
				StringBuilder sb = new StringBuilder(text);
				sb.deleteCharAt(sb.length() - 1);

				text = sb.toString();
				text = getMinusString(text);
				text = getTypeInputData(text);
				mBtnInput.setText(text);
			}
		}
	}

	@Override
	public void onDismiss() {
		if (mDismissListener != null) {
			String result = null;

			if (mBtnInput != null) {
				result = mBtnInput.getText().toString();
				mBtnInput.setText("");
			}
			mDismissListener.onDismiss(result, mPosition);
		}

		super.onDismiss();
	}

	public void show(View anchor, int position) {
		mPosition = position;
		super.show(anchor);
	}

	private String getTypeInputData(String aText) {
		String resultText = aText;

		switch (mType) {
		case TYPE_PHONE_NUMBER:
			//myung 20131127 UPDATE 전화번호 포멧 수정
//			resultText = PhoneNumberUtils.formatNumber(aText);
			aText = aText.replaceAll("-", "");
			resultText = PhoneNumberUtils.formatNumber(aText);
			if (!resultText.matches("-")) {
				//myung 20131127 UPDATE 전화번호 포멧 변경
//				resultText = CommonUtil.setPhoneNumber(aText);
				resultText.replace("/(^02.{0}|^01.{1}|[0-9]{3})([0-9]+)([0-9]{4})/", "$1-$2-$3");
			}
			break;
		case TYPE_MONEY:
			resultText = CommonUtil.currentpoint(mRealText);
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
}
