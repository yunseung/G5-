package com.ktrental.dialog;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.ktrental.R;
import com.ktrental.popup.EventPopupC;

//import com.pace.license.view.CarNumberPlateSearchView;
//import com.pace.license.view.listener.CarNumberOnDataListener;
//import com.pace.license.view.listener.OnTouchViewListener;

public class Camera_Dialog extends DialogC implements OnClickListener {

	private Window w;
	private WindowManager.LayoutParams lp;
	private Context context;
	private Button close;
//	private CarNumberPlateSearchView carNumberPlateView;
	private int mCameraZoomInitValue;

	private EditText et_carnum;
//	private ImageView iv_captured;
	// private Button bt_done;
//	private Button bt_reshot;
//	private CheckBox cb_flash;
	private Button bt_done;

	private View ANCHOR;
	private final int BUTTON = 72352376;
	private final int TEXTVIEW = 87843763;
	private int MODE = BUTTON;

	public Camera_Dialog(Context context) {
		super(context);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.camera_dialog);
		this.context = context;
		// w = getWindow();
		// w.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

		// lp = (WindowManager.LayoutParams) w.getAttributes();
		// lp.width = width;
		// lp.height = height;
		// w.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
		// w.setAttributes(lp);

		close = (Button) findViewById(R.id.camera_dialog_close_id);
		close.setOnClickListener(this);

		et_carnum = (EditText) findViewById(R.id.camera_carnum_id);

//		iv_captured = (ImageView) findViewById(R.id.iv_capture);
//		iv_captured.setVisibility(View.INVISIBLE);
//
//		bt_reshot = (Button) findViewById(R.id.camera_reshot_id);
//		bt_reshot.setOnClickListener(this);
//
//		cb_flash = (CheckBox) findViewById(R.id.camera_flash_id);
//		cb_flash.setOnCheckedChangeListener(this);
		
//		cb_flash.setOnCheckedChangeListener(new OnCheckedChangeListener() {
//			
//			@Override
//			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//				
//				
//				carNumberPlateView.setCameraFlashMode(carNumberPlateView.getCameraFlashMode());
//			}
//		});

		bt_done = (Button) findViewById(R.id.camera_done_id);
		bt_done.setOnClickListener(this);

//		actInit();

		hidekeyboard.sendEmptyMessageDelayed(0, 200);
	}

	Handler hidekeyboard = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(et_carnum.getWindowToken(), 0);
		}
	};

/*	public void actInit() {
		RelativeLayout rl = (RelativeLayout) findViewById(R.id.camera_back_id);
		carNumberPlateView = new CarNumberPlateSearchView(context);
		carNumberPlateView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		rl.addView(carNumberPlateView);
		iv_captured.bringToFront();
		carNumberPlateView.setCarNumberDataListener(this);

		try {
			carNumberPlateView.setInit();
			carNumberPlateView.setSearchFlag(true);
			carNumberPlateView.setMultiTouchEventListener(carNumberPlateView, this);
			carNumberPlateView.setFocusView(R.drawable.photo_num_bg);
		} catch (RuntimeException e) {
			e.printStackTrace();
		}
	}*/

	@Override
	protected void onStart() {
		super.onStart();
		/*try {
			carNumberPlateView.onViewCameraStart();
		} catch (Exception e){
			e.printStackTrace();
			try {
				dismiss();
			} catch (Exception e2){
				e2.printStackTrace();
			}
		}*/
	}

	@Override
	public void onStop() {
		super.onStop();
		// Camera View Release
		/*if (carNumberPlateView != null) {
			carNumberPlateView.onViewCameraRelease();
			carNumberPlateView = null;
		}*/
	}

	/*public void onDataListener(Object arg0) {
		carNumberPlateView.setSearchFlag(false); // 이미지 인식 중지.

		HashMap<String, Object> data = (HashMap<String, Object>) arg0;

		String numberStr = data.get("NUMBER").toString();
		Bitmap bitmap = (Bitmap) data.get("BITMAP");
		iv_captured.setVisibility(View.VISIBLE);
		iv_captured.setImageBitmap(bitmap);
		et_carnum.setText(numberStr);
	}*/

//	public void setCameraZoomControl() {
//		carNumberPlateView.setZoom(mCameraZoomInitValue);
//	}

	/*@Override
	public void onMultiTouchGrow() {
		if (mCameraZoomInitValue < carNumberPlateView.getCameraMaxZoomValue())
			mCameraZoomInitValue++;
		setCameraZoomControl();
	}

	@Override
	public void onMultiTouchShrink() {
		if (mCameraZoomInitValue > 0)
			mCameraZoomInitValue--;
		setCameraZoomControl();
	}*/

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.camera_dialog_close_id:
			dismiss();
			break;
		/*case R.id.camera_reshot_id:
			et_carnum.setText("");
			carNumberPlateView.setSearchFlag(true);
			iv_captured.setVisibility(View.INVISIBLE);
			break;*/

		/*case R.id.camera_flash_id:
			
			boolean hasFlash = context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);
			if(hasFlash)
			{
				 boolean bool = carNumberPlateView.getCameraFlashMode();
				 if(bool)
				 {
				 carNumberPlateView.setCameraFlashMode(carNumberPlateView.getCameraFlashMode());
				 }
				 else{
				 carNumberPlateView.setCameraFlashMode(true);
				 }
//				carNumberPlateView.setCameraFlashMode(carNumberPlateView.getCameraFlashMode());
			}
			
			break;*/

		case R.id.camera_done_id:
			Object data = et_carnum.getText();
			String str = data == null || data.toString().equals("") ? "" : data.toString();
			if (str.equals("")) {
				EventPopupC epc = new EventPopupC(context);
				epc.show("차량번호를 입력해 주세요.");
			} else {
				switch (MODE) {
				case BUTTON:
					Button bt_anchor = (Button) ANCHOR;
					bt_anchor.setText(str);
					break;
				case TEXTVIEW:
					TextView tv_anchor = (TextView) ANCHOR;
					tv_anchor.setText(str);
					break;
				}
				dismiss();
			}
			break;
		}

	}

	public void show(View bt_carnum) {
		ANCHOR = bt_carnum;
		if (bt_carnum instanceof Button) {
			MODE = BUTTON;
		} else if (bt_carnum instanceof TextView) {
			MODE = TEXTVIEW;
		}

		show();
	}

	/*@Override
	public void onCheckedChanged(CompoundButton v, boolean arg1) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
			case R.id.camera_flash_id:
				
				boolean hasFlash = context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);
				if(hasFlash)
				{
					 boolean bool = carNumberPlateView.getCameraFlashMode();
					 if(bool)
					 {
					 carNumberPlateView.setCameraFlashMode(carNumberPlateView.getCameraFlashMode());
					 }
					 else{
					 carNumberPlateView.setCameraFlashMode(true);
					 }
//					carNumberPlateView.setCameraFlashMode(carNumberPlateView.getCameraFlashMode());
				}
				
				break;
		
		
		}
		
		
		
	}*/
}
