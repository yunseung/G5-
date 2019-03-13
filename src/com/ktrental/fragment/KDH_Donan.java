package com.ktrental.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ktrental.R;
import com.ktrental.cm.connect.ConnectController;
import com.ktrental.cm.connect.Connector.ConnectInterface;
import com.ktrental.common.DEFINE;
import com.ktrental.model.TableModel;
import com.ktrental.popup.EventPopupC;
import com.ktrental.util.kog;

import java.util.HashMap;

public class KDH_Donan extends BaseFragment implements ConnectInterface {

	// 05호4660

	int L_id[] = { R.id.L_1, R.id.L_2, R.id.L_3, R.id.L_4, R.id.L_5, R.id.L_6, R.id.L_7, R.id.L_8, };
	int L_tv1[] = { R.id.tv1_1, R.id.tv1_2, R.id.tv1_3, R.id.tv1_4, R.id.tv1_5, R.id.tv1_6, R.id.tv1_7, R.id.tv1_8};
	int L_tv2[] = { R.id.tv2_1, R.id.tv2_2, R.id.tv2_3, R.id.tv2_4, R.id.tv2_5, R.id.tv2_6, R.id.tv2_7, R.id.tv2_8};



	LinearLayout L_layout[];
	TextView text_data[];
	static EditText editText;
	static Button btn_search;

	private ConnectController connectController;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mRootView = (View) inflater.inflate(R.layout.kdh_donan, null);

		TextView tvTitle = (TextView) mRootView.findViewById(R.id.tv_dialog_title);
		tvTitle.setText(getString(R.string.title_donan));

		L_layout = new LinearLayout[L_id.length];
		text_data = new TextView[L_id.length];
		TextView text_label[] = new TextView[L_id.length];
		String s_label[] = getResources().getStringArray(R.array.donan_labels);
		for (int i = 0; i < L_id.length; i++) {
			L_layout[i] = (LinearLayout) mRootView.findViewById(L_id[i]);
			text_label[i] = (TextView) L_layout[i].findViewById(L_tv1[i]);
			text_data[i] = (TextView) L_layout[i].findViewById(L_tv2[i]);
			text_label[i].setText(s_label[i]);
			text_data[i].setText("");
		}

		mRootView.findViewById(R.id.iv_exit).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dismiss();
			}
		});

		editText = (EditText) mRootView.findViewById(R.id.editText1);
		// 차량번호인식
		Button button1 = (Button) mRootView.findViewById(R.id.button1);
		button1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				showCameraPopup();
			}
		});

		// 조회
		btn_search = (Button) mRootView.findViewById(R.id.button2);
		btn_search.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String str = editText.getText().toString();
				if (str.length() > 0) {
					// 데이터 가지고 쏜다.
					showProgress(getString(R.string.searching));
					connectController.getZMO_3220_RD01(str);

				} else {
					// 검색어 입력하라는 팝업
					showEventPopup2(null, getString(R.string.search_word_empty));
				}
			}
		});

		connectController = new ConnectController(this, getActivity());
		return mRootView;
	}

	private void showCameraPopup() {

		C_CameraPopupFragment mCameraPopupFragment = new C_CameraPopupFragment(CameraPopupFragment.class.getName(),
				null);

		if (!mCameraPopupFragment.isAdded()) {
			if (!mCameraPopupFragment.isHidden()) {
				if (!mCameraPopupFragment.isRemoving()) {

					try {
						// mCameraPopupFragment.setCarNumber("");
						mCameraPopupFragment.setTitle("차량확인");

						// myung 20131120 ADD 차량인식 창 팝업 2560 대응
						int tempX = 1280;
						int tempY = 752;

						if (DEFINE.DISPLAY.equals("2560")) {
							tempX *= 2;
							tempY *= 2;
						}
						mCameraPopupFragment.show(getChildFragmentManager(), "");// ,
																					// tempX,
																					// tempY);

						getFragmentManager().executePendingTransactions();
					} catch (IllegalStateException exception) {
					}
				}
			}
		}
	}

	/**
	 * 2014-01-24 KDH 완전 야메이긴한데 데이터 땡겨 올수있는 방법을 아직 이소스에선 못찾었다. 그러므로 야메로 진행하고 추후
	 * 개선사항을 찾어보자-_-;;
	 */
	public static Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			kog.e("KDH", "msg.what = " + msg.what);
			kog.e("KDH", "msg.obj = " + msg.obj);

			String str = (String) msg.obj;
			if (str.length() > 0) {
				editText.setText(str);
				btn_search.performClick();
			}
			super.handleMessage(msg);
		}
	};

	@Override
	public void connectResponse(String FuntionName, String resultText, String MTYPE, int resulCode,
			TableModel tableModel) {
		// TODO Auto-generated method stub
		hideProgress();
		// 66허7869
		if ("S".equals(MTYPE)) {
			// 성공
			HashMap<String, String> o_struct1 = tableModel.getStruct("O_STRUCT1");
			text_data[0].setText(o_struct1.get("INVNR"));
			text_data[1].setText(o_struct1.get("SERGE"));
			text_data[2].setText(o_struct1.get("LOSTDT"));
			text_data[3].setText(o_struct1.get("LOSTEDNM"));
			text_data[4].setText(o_struct1.get("LOSTED2NM"));
			text_data[5].setText(o_struct1.get("CTRDEPTNM"));
			text_data[6].setText(o_struct1.get("CTRNO"));
			text_data[7].setText(o_struct1.get("KUNNRNM"));
		} else {
			EventPopupC epc = new EventPopupC(getActivity());
			epc.show(resultText);
			for (int i = 0; i < text_data.length; i++) {
				text_data[i].setText("");
			}
		}
	}

	@Override
	public void reDownloadDB(String newVersion) {
		// TODO Auto-generated method stub

	}

	/*
	 * @Override public void onClick(View v) { // TODO Auto-generated method
	 * stub switch (v.getId()) { case R.id.btn_delete: deleteBaseGroup(); break;
	 * case R.id.btn_send: sendBaseGroup(); break; case R.id.iv_exit: dismiss();
	 * break;
	 * 
	 * default: break; } }
	 */

}
