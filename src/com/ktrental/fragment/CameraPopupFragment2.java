package com.ktrental.fragment;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.ktrental.R;
import com.ktrental.model.MaintenanceResultModel;
import com.ktrental.util.CommonUtil;
import com.ktrental.util.OnChangeFragmentListener;
import com.pace.license.view.CarNumberPlateSearchView;
import com.pace.license.view.listener.CarNumberOnDataListener;
import com.pace.license.view.listener.OnTouchViewListener;

import java.util.HashMap;

public class CameraPopupFragment2 extends BaseFragment implements
		CarNumberOnDataListener, OnTouchViewListener, View.OnClickListener {

	// Camera PreView
	private CarNumberPlateSearchView carNumberPlateView;
	// Camera Zoom 초기값
	private int mCameraZoomInitValue;

	private Context mContext;
	private ImageView mCaptureIv;
	private EditText mEtNumber;
	private ImageView mIvExit;
	private String mCarNumberText = "";
	private OnNumberResult mOnNumberResult;
	private String mLastSearchText = "";
	private String TITLE;

	private ViewGroup rootView;

	public void setOnNumberResult(OnNumberResult onNumberResult) {
		mOnNumberResult = onNumberResult;
	}

	public interface OnNumberResult {

		void onResult(boolean success, MaintenanceResultModel model);
	}
	public CameraPopupFragment2(){}

	public CameraPopupFragment2(String className,
			OnChangeFragmentListener changeFragmentListener, String number) {
		super(className, changeFragmentListener);
		mCarNumberText = number;
	}

	public CameraPopupFragment2(String className,
			OnChangeFragmentListener changeFragmentListener, String number,
			String title) {
		super(className, changeFragmentListener);
		mCarNumberText = number;
		TITLE = title;
	}

	public void setCarNumber(String carNumberText) {
		mCarNumberText = carNumberText;
	}

	public String getCarNumber() {
		return mCarNumberText;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		int style = DialogFragment.STYLE_NO_TITLE;
		int theme = android.R.style.Theme_Light;
		//
		setStyle(style, theme);
		super.onCreate(savedInstanceState);
	}

	@Override
	public void onAttach(Activity activity) {
		mContext = activity;
		super.onAttach(activity);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
//		Log.i("###", "####onCreateView");
		rootView = (ViewGroup) inflater.inflate(R.layout.camera_dialog_layout,
				null);
		ViewGroup carmeraLayout = (ViewGroup) rootView
				.findViewById(R.id.ll_camera);

		initCamera(carmeraLayout);
		initViewSettings(rootView);
//		int width = getResources().getDimensionPixelSize(
//				R.dimen.popup_camera_width);
//		int height = getResources().getDimensionPixelSize(
//				R.dimen.popup_camera_height);
//		getDialog().getWindow().setLayout(width, height);
		return rootView;
	}

	private void initCamera(ViewGroup rootView) {
		// -- 번호판 초기 설정 --
		// Camera PreView
		carNumberPlateView = new CarNumberPlateSearchView(mContext);
		carNumberPlateView.setLayoutParams(new LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
//		rootView.addView(carNumberPlateView);
		// // 인식된 데이터를 받아오는 Listener 설정
		carNumberPlateView.setCarNumberDataListener(this);
		try {
			// Camera View 초기화
			carNumberPlateView.setInit();
			// 번호판 검색을 시작할지 여부 true - 인식 시작, false - 인식 중지
			carNumberPlateView.setSearchFlag(true);
			// 핀치 줌 인/아웃 Listener 설정
			carNumberPlateView.setMultiTouchEventListener(carNumberPlateView,
					this);
			// 아래의 해당 이미지를 보이지 않게 하려면 주석으로 막거나 설정하지 않으면 보이지 않음
			// 화면에 중앙인식 영역을 표시하는 이미지
			carNumberPlateView.setFocusView(R.drawable.photo_num_bg);
			// 번호판을 인식했을때 화면에 표시하는 이미지
			// carNumberPlateView.setResCheckView(R.drawable.checked2);
			// ------------------------------
			// TODO - 인식 이미지 저장 디렉토리 설정
			// Image Save directory set
			// Default directory : /mnt/sdcard/carLp/ (설정울 하지 않으면 Default위치에
			// 저장됩니다)
			// ------------------------------
			carNumberPlateView.setSaveImageDirectory(Environment
					.getExternalStorageDirectory().getPath() + "/carLp2/");
		} catch (RuntimeException e) {
			// showEventPopup2(null, "" + e.getMessage());
			e.printStackTrace();
		}
	}

	TextView title;

	private void initViewSettings(ViewGroup rootView) {
		mCaptureIv = (ImageView) rootView.findViewById(R.id.iv_capture);
		mEtNumber = (EditText) rootView.findViewById(R.id.et_car_num);
		// mEtNumber.setText(mCarNumberText);
		mIvExit = (ImageView) rootView.findViewById(R.id.iv_exit);
		mIvExit.setOnClickListener(this);
		rootView.findViewById(R.id.btn_re_shot).setOnClickListener(this);
		rootView.findViewById(R.id.btn_complate).setOnClickListener(this);
		title = (TextView) rootView.findViewById(R.id.tv_dialog_title);
		if (TITLE != null)
			title.setText(TITLE);
		rootView.findViewById(R.id.tv_dialog_sub_title).setVisibility(
				View.VISIBLE);
		mRootView = rootView;
		mEtNumber.setText(mCarNumberText);
	}

	@Override
	public void onResume() {
		super.onResume();
		// Camera View Start
		if (carNumberPlateView != null) {
			try {
				carNumberPlateView.onViewCameraStart();
			} catch (RuntimeException e) {
				showEventPopup2(null, "" + e);
			} catch (ExceptionInInitializerError e) {
				showEventPopup2(null, "" + e.getMessage());
			}
		}
	}

	@Override
	public void onStop() {
		super.onStop();
		// Camera View Release
		if (carNumberPlateView != null)
			carNumberPlateView.onViewCameraRelease();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		carNumberPlateView = null;
	}

	@Override
	public void onDestroyView() {
		// TODO Auto-generated method stub
		super.onDestroyView();
	}

	/**
	 * 
	 * 인식된 번호판 결과 값이 들어 오는 부분
	 * 
	 * HashMap<String, Object> 의 형태로 key "BITMAP" - 인식한 사진 Bitmap 이미지 key
	 * "NUMBER" - 인식한 번호판 String Key "TXT" - 인식한 번호판과 인식한 시간값 String
	 * 
	 */
	@SuppressWarnings("unchecked")
	public void onDataListener(Object arg0) {
		// setListAdapter((HashMap<String, Object>) arg0);
		carNumberPlateView.setSearchFlag(false); // 이미지 인식 중지.
		HashMap<String, Object> data = (HashMap<String, Object>) arg0;
		String numberStr = data.get("NUMBER").toString();
		Bitmap bitmap = (Bitmap) data.get("BITMAP");
		carNumberPlateView.SaveBitmapToFile(bitmap, numberStr); // 이미지 저장
		mCaptureIv.setVisibility(View.VISIBLE);
		mCaptureIv.setImageBitmap(bitmap);
		mEtNumber.setText(numberStr);
		// mCarNumberText = mEtNumber.getText().toString();
		// if (mCarNumberText.equals(numberStr))
		// {
		// // mIvExit.setText("완료");
		// showEventPopup2(null, "일치하였습니다. " + numberStr);
		// }
		// else
		// {
		// if (carNumberPlateView != null)
		// {
		// showEventPopup2(null, "불일치하였습니다. " + numberStr);
		// }
		// }
		mLastSearchText = numberStr;
		// if (mOnNumberResult != null)
		// mOnNumberResult.onResult(true);
		// mSound.play();
	}

	/**
	 * 카메라 줌 설정
	 */
	public void setCameraZoomControl() {
		carNumberPlateView.setZoom(mCameraZoomInitValue);
	}

	/**
	 * 카메라 줌 인
	 */
	@Override
	public void onMultiTouchGrow() {
		if (mCameraZoomInitValue < carNumberPlateView.getCameraMaxZoomValue())
			mCameraZoomInitValue++;
		setCameraZoomControl();
	}

	/**
	 * 카메라 줌 아웃
	 */
	@Override
	public void onMultiTouchShrink() {
		if (mCameraZoomInitValue > 0)
			mCameraZoomInitValue--;
		setCameraZoomControl();
	}

	// 인식시 사운드 설정

	@Override
	public void onClick(View v) {
		CommonUtil.hideKeyboad(mContext, mEtNumber);
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.iv_exit:
			// clickExit();
			this.dismiss();
			break;
		case R.id.btn_re_shot:
			clickReShot();
			break;
		case R.id.btn_complate:
			clickExit();
			break;
		default:
			break;
		}
	}

	private void clickReShot() {
		carNumberPlateView.setSearchFlag(true);
		mCaptureIv.setVisibility(View.GONE);
	}

	private void clickExit() {
		if (mOnNumberResult != null) {
//			Log.d("", "clickExit");
			String text = mEtNumber.getText().toString();
			if (text.equals(mCarNumberText)) {
				mOnNumberResult.onResult(true, null);
				this.dismiss();
			} else {
				carNumberPlateView.setSearchFlag(true);
				mCaptureIv.setVisibility(View.GONE);
				showEventPopup2(null,
						"입력하신 차량번호와 점검대상 차량번호가 다릅니다. 다시확인하여 주십시요. ");
			}
		}
	}

	@Override
	public void dismiss() {
		// TODO Auto-generated method stub
		super.dismiss();
	}

	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		getDialog().setCanceledOnTouchOutside(false);
	}

	public void show(FragmentManager manager, String tag, int width,
			int height, String a) {
//		Log.i("####", "####show");
		show(manager, tag, width, height);
	}

	public ViewGroup getRootView() {
		return rootView;
	}

	public void setRootView(ViewGroup rootView) {
		this.rootView = rootView;
	}
}
