package com.ktrental.fragment;

import java.util.HashMap;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.ktrental.R;
import com.ktrental.model.MaintenanceResultModel;
import com.ktrental.util.CommonUtil;
import com.ktrental.util.OnChangeFragmentListener;
import com.ktrental.util.OnEventOkListener;
import com.ktrental.util.kog;
import com.pace.license.view.CarNumberPlateSearchView;
import com.pace.license.view.listener.CarNumberOnDataListener;
import com.pace.license.view.listener.OnTouchViewListener;

@SuppressLint("ValidFragment")
public class C_CameraPopupFragment extends BaseFragment implements
CarNumberOnDataListener, OnTouchViewListener, View.OnClickListener{

	// Camera PreView
	private CarNumberPlateSearchView carNumberPlateView;
	// Camera Zoom 초기값
	private int mCameraZoomInitValue;

	// Beeper 인식시 사운드
	// private Beeper mSound = null;

	private Context mContext;

	private ImageView mCaptureIv;
	private EditText mEtNumber;
	private ImageView mIvExit;
	private TextView title;

	private String mLastSearchText = "";

	private String TITLE = "";
	
	
	

	@SuppressLint("ValidFragment")
	public C_CameraPopupFragment(String className,
								 OnChangeFragmentListener changeFragmentListener)
	{
		super(className, changeFragmentListener);
	}
	

	public void setTitle(String TITLE){
		this.TITLE = TITLE;
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

		ViewGroup root = (ViewGroup) inflater.inflate(
				R.layout.camera_dialog_layout, null);

		ViewGroup carmeraLayout = (ViewGroup) root.findViewById(R.id.ll_camera);

		initCamera(carmeraLayout);

		initViewSettings(root);

//		int width = getResources().getDimensionPixelSize(
//				R.dimen.popup_camera_width);
//		int height = getResources().getDimensionPixelSize(
//				R.dimen.popup_camera_height);
//		getDialog().getWindow().setLayout(width, height);

		CheckBox cb_flash = (CheckBox) mRootView
				.findViewById(R.id.tire_picture_flash_id);
		cb_flash.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				if (arg1) {
					carNumberPlateView.setCameraFlashMode(false);
				} else {
					carNumberPlateView.setCameraFlashMode(true);
				}
			}
		});
		LinearLayout L_1 = (LinearLayout)mRootView.findViewById(R.id.L_1);
		L_1.setVisibility(View.GONE);

		return root;
	}

	private void initCamera(ViewGroup rootView) {
		// 사운드 설정
		// mSound = new Beeper(mContext, R.raw.good);

		// ==============================================================================================================================
		// -- 번호판 초기 설정 --

		// Camera PreView
		carNumberPlateView = new CarNumberPlateSearchView(mContext);
		carNumberPlateView.setLayoutParams(new LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		rootView.addView(carNumberPlateView);

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
			showEventPopup2(null, "" + e.getMessage());
			e.printStackTrace();
		}

	}

	private void initViewSettings(ViewGroup rootView) {
		mCaptureIv = (ImageView) rootView.findViewById(R.id.iv_capture);
		mEtNumber = (EditText) rootView.findViewById(R.id.et_car_num);
		// mEtNumber.setText(mCarNumberText);
		mIvExit = (ImageView) rootView.findViewById(R.id.iv_exit);
		mIvExit.setOnClickListener(this);
		rootView.findViewById(R.id.btn_re_shot).setOnClickListener(this);
		rootView.findViewById(R.id.btn_complate).setOnClickListener(this);

		title = (TextView) rootView.findViewById(R.id.tv_dialog_title);
		title.setText("도착등록");
		Log.i("TITLE", TITLE);
		//		if (TITLE != null)
		if (!TITLE.equals(""))
			title.setText(TITLE);
		rootView.findViewById(R.id.tv_dialog_sub_title).setVisibility(
				View.VISIBLE);

		mRootView = rootView;

		mCaptureIv.setVisibility(View.GONE);

		mEtNumber.setOnEditorActionListener(new OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView v, int actionId,
					KeyEvent event) {
				// TODO Auto-generated method stub
				if(actionId == EditorInfo.IME_ACTION_DONE){
					CommonUtil.hideKeyboad(mContext, mEtNumber);
					//					clickExit();
					new Handler().postDelayed(mclickExitRunnable, 500);
				}
				return false;
			}
		});

	}

	// myung 20131203 ADD 상태 변경 시 로딩바 추가 필요.
	private Runnable mclickExitRunnable = new Runnable() {
		@Override
		public void run() {
			clickExit();
		}
	};

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

	//	int mMatchingCarNum=0;
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
		showEventPopup2(new OnEventOkListener() {

			@Override
			public void onOk() {
				// TODO Auto-generated method stub
				dismiss();

			}
		}, numberStr);
		//		showEventPopup2(null, "여기서 리턴인데..으흠" + numberStr);

		mLastSearchText = numberStr;
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
	class Beeper {
		MediaPlayer player;

		Beeper(Context context, int id) {
			player = MediaPlayer.create(context, id);
		}

		void play() {
			player.seekTo(0);
			player.setVolume(1, 1);
			player.start();
		}
	}

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
		String text = mEtNumber.getText().toString();
		this.dismiss();
	}

	@Override
	public void dismiss() {
		// TODO Auto-generated method stub
		String text = mEtNumber.getText().toString();
		kog.e("KDH", "text = "+text);
		Message msg = new Message();
		msg.what = 0;
		msg.obj = (String)text;
		KDH_Donan.mHandler.sendMessage(msg);
		super.dismiss();
	}

	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		getDialog().setCanceledOnTouchOutside(false);
		// getDialog().getWindow().addFlags(
		// WindowManager.LayoutParams.FLAG_DIM_BEHIND);
		getDialog().getWindow().clearFlags(
				WindowManager.LayoutParams.FLAG_DIM_BEHIND);
	}

}
