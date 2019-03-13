package com.ktrental.dialog;

import java.util.HashMap;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Environment;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.ktrental.R;
import com.ktrental.cm.connect.Connector.ConnectInterface;
import com.ktrental.model.TableModel;
import com.ktrental.popup.EventPopup1;
import com.ktrental.popup.EventPopup2;
import com.ktrental.popup.EventPopupC;
import com.ktrental.util.OnEventCancelListener;
import com.ktrental.util.OnEventOkListener;
import com.pace.license.view.CarNumberPlateSearchView;
import com.pace.license.view.listener.CarNumberOnDataListener;
import com.pace.license.view.listener.OnTouchViewListener;

public class Product_Camera_Dialog extends DialogC implements ConnectInterface,
		View.OnClickListener, CarNumberOnDataListener, OnTouchViewListener {

	private Window w;
	private WindowManager.LayoutParams lp;
	private Context context;
	private Activity activity;
	private Button close;
	private RelativeLayout preview_layout;
	// Camera PreView
	private CarNumberPlateSearchView carNumberPlateView;
	// Camera Zoom 초기값
	private int mCameraZoomInitValue;

	// Beeper 인식시 사운드
	// private Beeper mSound = null;
	public Product_Camera_Dialog(Context context) {
		super(context);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.product_camera_dialog);
		w = getWindow();
		w.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN
				| WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
		w.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
		lp = (WindowManager.LayoutParams) w.getAttributes();
		lp.width = LayoutParams.MATCH_PARENT;
		lp.height = LayoutParams.MATCH_PARENT;
		w.setAttributes(lp);
		this.context = context;
		this.activity = (Activity) context;
		close = (Button) findViewById(R.id.product_camera_dialog_close_id);
		close.setOnClickListener(this);
		// preview_layout =
		// (RelativeLayout)findViewById(R.id.product_dialog_preview_id);
		carNumberPlateView = (CarNumberPlateSearchView) findViewById(R.id.preview_id);
		initCamera();
	}

	@Override
	protected void onStart() {
		super.onStart();
		if (carNumberPlateView != null)
			carNumberPlateView.onViewCameraStart();
	}

	@Override
	protected void onStop() {
		super.onStop();
		// Camera View Release
		if (carNumberPlateView != null)
			carNumberPlateView.onViewCameraRelease();
	}

	public void initCamera() {
		// 사운드 설정
		// mSound = new Beeper(this, R.raw.good);
		// ==============================================================================================================================
		// -- 번호판 초기 설정 --
		// Camera PreView
		carNumberPlateView = (CarNumberPlateSearchView) findViewById(R.id.preview_id);
		// 인식된 데이터를 받아오는 Listener 설정
		carNumberPlateView.setCarNumberDataListener(this);
		// Camera View 초기화
		carNumberPlateView.setInit();
		// 번호판 검색을 시작할지 여부 true - 인식 시작, false - 인식 중지
		carNumberPlateView.setSearchFlag(true);
		// 핀치 줌 인/아웃 Listener 설정
		carNumberPlateView.setMultiTouchEventListener(carNumberPlateView, this);
		// 아래의 해당 이미지를 보이지 않게 하려면 주석으로 막거나 설정하지 않으면 보이지 않음
		// 화면에 중앙인식 영역을 표시하는 이미지
		carNumberPlateView.setFocusView(R.drawable.border);
		// 번호판을 인식했을때 화면에 표시하는 이미지
		carNumberPlateView.setResCheckView(R.drawable.checked2);
		// ------------------------------
		// TODO - 인식 이미지 저장 디렉토리 설정
		// Image Save directory set
		// Default directory : /mnt/sdcard/carLp/ (설정울 하지 않으면 Default위치에 저장됩니다)
		// ------------------------------
		carNumberPlateView.setSaveImageDirectory(Environment
				.getExternalStorageDirectory().getPath() + "/carLp2/");
		// ==============================================================================================================================
		// ==============================================================================================================================
		// -- 인식된 번호판에 대한 정보를 출력한 list Data --
		// ==============================================================================================================================
	}

	@Override
	public void connectResponse(String FuntionName, String resultText,
			String MTYPE, int resulCode, TableModel tableModel) {
		hideProgress();
		if (MTYPE == null || !MTYPE.equals("S")) {
			EventPopupC epc = new EventPopupC(context);
			epc.show(resultText);
			return;
		}
		if (FuntionName.equals("ZMO_1030_RD06")) {
		}
	}

	@Override
	public void reDownloadDB(String newVersion) {
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.product_camera_dialog_close_id: // 닫기
			dismiss();
			break;
		}
	}

	@SuppressWarnings("unchecked")
	public void onDataListener(Object arg0) {
		HashMap<String, Object> hm = (HashMap<String, Object>) arg0;
		Toast.makeText(context, hm.get("TXT").toString(), 0).show();
	}

	// class Beeper {}
	private void setListAdapter(HashMap<String, Object> map) {
	}

	@Override
	public void onMultiTouchGrow() {
		// TODO Auto-generated method stub
	}

	@Override
	public void onMultiTouchShrink() {
		// TODO Auto-generated method stub
	}

}
