package com.ktrental.dialog;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.hardware.Camera;
import android.hardware.Camera.AutoFocusCallback;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.ShutterCallback;
import android.hardware.Camera.Size;
import android.os.Environment;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.ktrental.R;
import com.ktrental.adapter.Tire_Picture_Dialog_Adapter;
import com.ktrental.adapter.Tire_Picture_Dialog_Adapter_PM033;
import com.ktrental.adapter.Tire_Picture_Dialog_Adapter_PM056;
import com.ktrental.beans.PM033;
import com.ktrental.beans.PM056;
import com.ktrental.beans.PM081;
import com.ktrental.common.DEFINE;
import com.ktrental.fragment.TireFragment;
import com.ktrental.popup.EventPopupC;
import com.ktrental.product.Menu2_1_Activity;
import com.ktrental.util.LogUtil;
import com.ktrental.util.kog;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Tire_Picture_Dialog extends Dialog implements
		View.OnClickListener, SurfaceHolder.Callback {
	private Window w;
	private WindowManager.LayoutParams lp;
	private Context context;
	private Button close;
	public ProgressDialog pd;
	private ListView lv_list;
	private Tire_Picture_Dialog_Adapter tpda;
	private Tire_Picture_Dialog_Adapter_PM033 tpda_pm033;
	private Tire_Picture_Dialog_Adapter_PM056 tpda_pm056;
	public static ArrayList<PM081> pm081;
	public static ArrayList<PM033> pm033;
	public static ArrayList<PM056> pm056;

	private SurfaceView surfaceView;
	private SurfaceHolder surfaceHolder;
	private Camera camera;
	private Parameters params;
	private Button bt_shot;
	private Button bt_delete;
	private Button bt_take;

	private ImageView iv_picture;
	private int MODE;
	
	public String PM_What;

	private CheckBox cb_flash;


	private LinearLayout ll_group;
	private boolean safeToTakePicture = false;

	private TireFragment mTireFragment = null;

	//myung 20131202 UPDATE PM081동기화
	public Tire_Picture_Dialog(Context context, int mode, ArrayList<PM081> insPM081) {
//	public Tire_Picture_Dialog(Context context, int mode1) {
		super(context);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		// myung 20131202 ADD 시스템 메뉴바 제거
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		setContentView(R.layout.tire_picture_dialog);
		w = getWindow();
		
		
		PM_What = "081";

		w.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
		lp = (WindowManager.LayoutParams) w.getAttributes();
		lp.width = LayoutParams.MATCH_PARENT;
		lp.height = LayoutParams.MATCH_PARENT;
		w.setAttributes(lp);
		this.context = context;
		pd = new ProgressDialog(context);
		pd.setCancelable(false);
		pd.setMessage("검색중입니다.");

		MODE = mode;

		ll_group = (LinearLayout)findViewById(R.id.ll_group);
		close = (Button) findViewById(R.id.tire_picture_close_id);
		close.setOnClickListener(this);

		lv_list = (ListView) findViewById(R.id.tire_picture_list_id);
		
		//myung 20131212 UPDATE 추가 클릭하여 차량사진촬영 화면 팝업 시 삭제한 타이어도 체크되어 있는 문제
		pm081 = initPM081();
		for (int i = 0; i < insPM081.size(); i++) {
			for(int j=0; j < pm081.size(); j++){
				if(insPM081.get(i).ZCODEVT.equals(pm081.get(j).ZCODEVT)){
					pm081.set(j, insPM081.get(i));
				}
			}
		}
		
		Collections.sort(pm081, new Compare1());
		
//		for(int i = 101 ; i < 105 ; i++)
//		{
//			PM081 tmp = new PM081();
//			tmp.ZCODEV = String.valueOf(i);
//			tmp.ZCODEVT = "기타"+(i-100);
//			tmp.CHECKED = false;
//			tmp.PATH = "";
//			tmp.ONESIDE_WEAR = false;
//			pm081.add(tmp);
//		}
//		
		int[] sort = 
			{10, 20, 
			 100, 101, 102, 103, 104, //기타 
			 190, 
			 30, 110, 120,
		     40, 130, 140,
			 50, 200, 210,
			 60, 220, 230,
			 70, 150, 160,
			 80, 170, 180,
			 90};
		
		ArrayList<PM081> pm081_sort = new ArrayList<PM081>();
		for(int i = 0 ; i < sort.length ; i++)
		{
			for(int j = 0 ; j < pm081.size() ; j++)
			{
				if(sort[i] == Integer.valueOf(pm081.get(j).ZCODEV))
				{
					pm081_sort.add(pm081.get(j));
					
				}
			}
		}
		pm081 = pm081_sort;
		
		
		
		for (int i = 0; i < pm081.size(); i++) 
		{
			kog.e("KDH", "pm081.get(i).ZCODEV = "+pm081.get(i).ZCODEV);
			kog.e("KDH", "pm081.get(i).ZCODEVT = "+pm081.get(i).ZCODEVT);
		}
		
		tpda = new Tire_Picture_Dialog_Adapter(context,
				R.layout.tire_picture_dialog_row, pm081, this, mode);
		lv_list.setAdapter(tpda);
		
		lv_list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int pos,
					long arg3) {
				PM081 _data = (PM081) arg0.getTag();
				kog.e("KDH", "pos  = "+pos + "     = "+tpda.curPos);
				tpda.curPos = pos;
				tpda.notifyDataSetChanged();
			}
		});

		surfaceView = (SurfaceView) findViewById(R.id.tire_picture_surface_id);
		surfaceView.setZOrderOnTop(true);
		surfaceView.setOnClickListener(this);
		surfaceHolder = surfaceView.getHolder();
		surfaceHolder.addCallback(this);

		bt_shot = (Button) findViewById(R.id.tire_picture_shot_id);
		bt_shot.setOnClickListener(this);

		bt_take = (Button) findViewById(R.id.tire_picture_take_id);
		bt_take.setOnClickListener(this);
		bt_take.setVisibility(View.GONE);

		bt_delete = (Button) findViewById(R.id.tire_picture_delete_id);
		bt_delete.setOnClickListener(this);

		iv_picture = (ImageView) findViewById(R.id.tire_picture_pic_id);
		iv_picture.bringToFront();

		cb_flash = (CheckBox) findViewById(R.id.tire_picture_flash_id);

		final Context fcontext = this.context;

		cb_flash.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				// myung 20131127 ADD 플래쉬 예외처리
//				Log.e("onCheckedChanged", "onCheckedChanged");

				if(fcontext.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH))
				{
					if (camera == null && cb_flash.isChecked()) {
						cb_flash.setChecked(false);
						return;
					}
					Parameters params = camera.getParameters();

					if (arg1) {
						params.setFlashMode(Parameters.FLASH_MODE_TORCH);
					} else {
						params.setFlashMode(Parameters.FLASH_MODE_OFF);
					}
					camera.setParameters(params);

				}
			}
		});

	}

	public Tire_Picture_Dialog(Context context, int mode, ArrayList<PM081> insPM081, TireFragment fragment) {
//	public Tire_Picture_Dialog(Context context, int mode1) {
		super(context);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		// myung 20131202 ADD 시스템 메뉴바 제거
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		setContentView(R.layout.tire_picture_dialog);
		w = getWindow();


		PM_What = "081";

		w.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
		lp = (WindowManager.LayoutParams) w.getAttributes();
		lp.width = LayoutParams.MATCH_PARENT;
		lp.height = LayoutParams.MATCH_PARENT;
		w.setAttributes(lp);
		this.context = context;
		pd = new ProgressDialog(context);
		pd.setCancelable(false);
		pd.setMessage("검색중입니다.");

		MODE = mode;

		ll_group = (LinearLayout)findViewById(R.id.ll_group);
		close = (Button) findViewById(R.id.tire_picture_close_id);
		close.setOnClickListener(this);

		lv_list = (ListView) findViewById(R.id.tire_picture_list_id);

		mTireFragment = fragment;

		//myung 20131212 UPDATE 추가 클릭하여 차량사진촬영 화면 팝업 시 삭제한 타이어도 체크되어 있는 문제
		pm081 = initPM081();
		for (int i = 0; i < insPM081.size(); i++) {
			for(int j=0; j < pm081.size(); j++){
				if(insPM081.get(i).ZCODEVT.equals(pm081.get(j).ZCODEVT)){
					pm081.set(j, insPM081.get(i));
				}
			}
		}

		Collections.sort(pm081, new Compare1());

//		for(int i = 101 ; i < 105 ; i++)
//		{
//			PM081 tmp = new PM081();
//			tmp.ZCODEV = String.valueOf(i);
//			tmp.ZCODEVT = "기타"+(i-100);
//			tmp.CHECKED = false;
//			tmp.PATH = "";
//			tmp.ONESIDE_WEAR = false;
//			pm081.add(tmp);
//		}
//
		int[] sort =
				{10, 20,
						100, 101, 102, 103, 104, //기타
						190,
						30, 110, 120,
						40, 130, 140,
						50, 200, 210,
						60, 220, 230,
						70, 150, 160,
						80, 170, 180,
						90};

		ArrayList<PM081> pm081_sort = new ArrayList<PM081>();
		for(int i = 0 ; i < sort.length ; i++)
		{
			for(int j = 0 ; j < pm081.size() ; j++)
			{
				if(sort[i] == Integer.valueOf(pm081.get(j).ZCODEV))
				{
					pm081_sort.add(pm081.get(j));

				}
			}
		}
		pm081 = pm081_sort;



		for (int i = 0; i < pm081.size(); i++)
		{
			kog.e("KDH", "pm081.get(i).ZCODEV = "+pm081.get(i).ZCODEV);
			kog.e("KDH", "pm081.get(i).ZCODEVT = "+pm081.get(i).ZCODEVT);
		}

		tpda = new Tire_Picture_Dialog_Adapter(context,
				R.layout.tire_picture_dialog_row, pm081, this, mode);
		lv_list.setAdapter(tpda);

		lv_list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int pos,
									long arg3) {
				PM081 _data = (PM081) arg0.getTag();
				kog.e("KDH", "pos  = "+pos + "     = "+tpda.curPos);
				tpda.curPos = pos;
				tpda.notifyDataSetChanged();
			}
		});

		surfaceView = (SurfaceView) findViewById(R.id.tire_picture_surface_id);
		surfaceView.setZOrderOnTop(true);
		surfaceView.setOnClickListener(this);
		surfaceHolder = surfaceView.getHolder();
		surfaceHolder.addCallback(this);

		bt_shot = (Button) findViewById(R.id.tire_picture_shot_id);
		bt_shot.setOnClickListener(this);

		bt_take = (Button) findViewById(R.id.tire_picture_take_id);
		bt_take.setOnClickListener(this);
		bt_take.setVisibility(View.GONE);

		bt_delete = (Button) findViewById(R.id.tire_picture_delete_id);
		bt_delete.setOnClickListener(this);

		iv_picture = (ImageView) findViewById(R.id.tire_picture_pic_id);
		iv_picture.bringToFront();

		cb_flash = (CheckBox) findViewById(R.id.tire_picture_flash_id);

		final Context fcontext = this.context;

		cb_flash.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				// myung 20131127 ADD 플래쉬 예외처리
//				Log.e("onCheckedChanged", "onCheckedChanged");

				if(fcontext.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH))
				{
					if (camera == null && cb_flash.isChecked()) {
						cb_flash.setChecked(false);
						return;
					}
					Parameters params = camera.getParameters();

					if (arg1) {
						params.setFlashMode(Parameters.FLASH_MODE_TORCH);
					} else {
						params.setFlashMode(Parameters.FLASH_MODE_OFF);
					}
					camera.setParameters(params);

				}
			}
		});
	}
	
	//Jonathan 150407
	public Tire_Picture_Dialog(Context context, int mode, ArrayList<PM033> insPM033, String a) {
//		public Tire_Picture_Dialog(Context context, int mode1) {
			super(context);
			requestWindowFeature(Window.FEATURE_NO_TITLE);
			// myung 20131202 ADD 시스템 메뉴바 제거
			getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
					WindowManager.LayoutParams.FLAG_FULLSCREEN);

			setContentView(R.layout.tire_picture_dialog);
			w = getWindow();
			
			kog.e("Jonathan", "033 들어옴");
			PM_What = "033";

			w.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
			lp = (WindowManager.LayoutParams) w.getAttributes();
			lp.width = LayoutParams.MATCH_PARENT;
			lp.height = LayoutParams.MATCH_PARENT;
			w.setAttributes(lp);
			this.context = context;
			pd = new ProgressDialog(context);
			pd.setCancelable(false);
			pd.setMessage("검색중입니다.");

			MODE = mode;

			close = (Button) findViewById(R.id.tire_picture_close_id);
			close.setOnClickListener(this);

			lv_list = (ListView) findViewById(R.id.tire_picture_list_id);
			
			//myung 20131212 UPDATE 추가 클릭하여 차량사진촬영 화면 팝업 시 삭제한 타이어도 체크되어 있는 문제
			pm033 = initPM033();
			for (int i = 0; i < insPM033.size(); i++) {
				for(int j=0; j < pm033.size(); j++){
					if(insPM033.get(i).ZCODEVT.equals(pm033.get(j).ZCODEVT)){
						pm033.set(j, insPM033.get(i));
					}
				}
			}
			
			Collections.sort(pm033, new Compare2());
			
			for (int i = 0; i < pm033.size(); i++) 
			{
				kog.e("KDH", "pm033.get(i).ZCODEV = "+pm033.get(i).ZCODEV);
				kog.e("KDH", "pm033.get(i).ZCODEVT = "+pm033.get(i).ZCODEVT);
			}
			
			tpda_pm033 = new Tire_Picture_Dialog_Adapter_PM033(context,
					R.layout.tire_picture_dialog_row, pm033, this, mode);
			lv_list.setAdapter(tpda_pm033);
			
			lv_list.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1, int pos,
						long arg3) {
					PM033 _data = (PM033) arg0.getTag();
					kog.e("KDH", "pos  = "+pos + "     = "+tpda_pm033.curPos);
					tpda_pm033.curPos = pos;
					tpda_pm033.notifyDataSetChanged();
				}
			});

			surfaceView = (SurfaceView) findViewById(R.id.tire_picture_surface_id);
			surfaceView.setZOrderOnTop(true);
			surfaceView.setOnClickListener(this);
			surfaceHolder = surfaceView.getHolder();
			surfaceHolder.addCallback(this);

			bt_shot = (Button) findViewById(R.id.tire_picture_shot_id);
			bt_shot.setOnClickListener(this);

			bt_take = (Button) findViewById(R.id.tire_picture_take_id);
			bt_take.setOnClickListener(this);

			bt_delete = (Button) findViewById(R.id.tire_picture_delete_id);
			bt_delete.setOnClickListener(this);

			iv_picture = (ImageView) findViewById(R.id.tire_picture_pic_id);
			iv_picture.bringToFront();

			cb_flash = (CheckBox) findViewById(R.id.tire_picture_flash_id);

			
			final Context fcontext = this.context;
			
			cb_flash.setOnCheckedChangeListener(new OnCheckedChangeListener() {
				@Override
				public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
					// myung 20131127 ADD 플래쉬 예외처리
//					Log.e("onCheckedChanged", "onCheckedChanged");
					
					if(fcontext.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH))
					{
						if (camera == null && cb_flash.isChecked()) {
							cb_flash.setChecked(false);
							return;
						}
						Parameters params = camera.getParameters();
						if (arg1) {
							params.setFlashMode(Parameters.FLASH_MODE_TORCH);
						} else {
							params.setFlashMode(Parameters.FLASH_MODE_OFF);
						}
						camera.setParameters(params);
					}
				}
			});
			if(camera != null) {
				Parameters params = camera.getParameters();
				if (params != null) {
					params.setFlashMode(Parameters.FLASH_MODE_TORCH);
					camera.setParameters(params);
				}
			}
		}
	
	
	public Tire_Picture_Dialog(Context context, int mode, ArrayList<PM056> insPM056, String a, String b) {
//		public Tire_Picture_Dialog(Context context, int mode1) {
			super(context);
			requestWindowFeature(Window.FEATURE_NO_TITLE);
			// myung 20131202 ADD 시스템 메뉴바 제거
			getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
					WindowManager.LayoutParams.FLAG_FULLSCREEN);

			setContentView(R.layout.tire_picture_dialog);
			w = getWindow();
			
			kog.e("Jonathan", "056 들어옴");
			PM_What = "056";

			w.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
			lp = (WindowManager.LayoutParams) w.getAttributes();
			lp.width = LayoutParams.MATCH_PARENT;
			lp.height = LayoutParams.MATCH_PARENT;
			w.setAttributes(lp);
			this.context = context;
			pd = new ProgressDialog(context);
			pd.setCancelable(false);
			pd.setMessage("검색중입니다.");

			MODE = mode;

			close = (Button) findViewById(R.id.tire_picture_close_id);
			close.setOnClickListener(this);

			lv_list = (ListView) findViewById(R.id.tire_picture_list_id);
			
			//myung 20131212 UPDATE 추가 클릭하여 차량사진촬영 화면 팝업 시 삭제한 타이어도 체크되어 있는 문제
			pm056 = initPM056();
			for (int i = 0; i < insPM056.size(); i++) {
				for(int j=0; j < pm056.size(); j++){
					if(insPM056.get(i).ZCODEVT.equals(pm056.get(j).ZCODEVT)){
						pm056.set(j, insPM056.get(i));
					}
				}
			}
			
			Collections.sort(pm056, new Compare3());
			
			for (int i = 0; i < pm056.size(); i++) 
			{
				kog.e("KDH", "pm056.get(i).ZCODEV = "+pm056.get(i).ZCODEV);
				kog.e("KDH", "pm056.get(i).ZCODEVT = "+pm056.get(i).ZCODEVT);
			}
			
			tpda_pm056 = new Tire_Picture_Dialog_Adapter_PM056(context,
					R.layout.tire_picture_dialog_row, pm056, this, mode);
			lv_list.setAdapter(tpda_pm056);
			
			lv_list.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1, int pos,
						long arg3) {
					PM056 _data = (PM056) arg0.getTag();
					kog.e("KDH", "pos  = "+pos + "     = "+tpda_pm056.curPos);
					tpda_pm056.curPos = pos;
					tpda_pm056.notifyDataSetChanged();
				}
			});

			surfaceView = (SurfaceView) findViewById(R.id.tire_picture_surface_id);
			surfaceView.setZOrderOnTop(true);
			surfaceView.setOnClickListener(this);
			surfaceHolder = surfaceView.getHolder();
			surfaceHolder.addCallback(this);

			bt_shot = (Button) findViewById(R.id.tire_picture_shot_id);
			bt_shot.setOnClickListener(this);

			bt_take = (Button) findViewById(R.id.tire_picture_take_id);
			bt_take.setOnClickListener(this);

			bt_delete = (Button) findViewById(R.id.tire_picture_delete_id);
			bt_delete.setOnClickListener(this);

			iv_picture = (ImageView) findViewById(R.id.tire_picture_pic_id);
			iv_picture.bringToFront();

			cb_flash = (CheckBox) findViewById(R.id.tire_picture_flash_id);

			final Context fcontext = this.context;
			
			cb_flash.setOnCheckedChangeListener(new OnCheckedChangeListener() {
				@Override
				public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
					// myung 20131127 ADD 플래쉬 예외처리
//					Log.e("onCheckedChanged", "onCheckedChanged");
					if(fcontext.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH))
					{
						if (camera == null && cb_flash.isChecked()) {
							cb_flash.setChecked(false);
							return;
						}
						Parameters params = camera.getParameters();
						if (arg1) {
							params.setFlashMode(Parameters.FLASH_MODE_TORCH);
						} else {
							params.setFlashMode(Parameters.FLASH_MODE_OFF);
						}
						camera.setParameters(params);
						
					}
				}
			});
			if(camera != null) {
				Parameters params = camera.getParameters();
				if (params != null) {
					params.setFlashMode(Parameters.FLASH_MODE_TORCH);
					camera.setParameters(params);
				}
			}

		}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		//myung 20131202 UPDATE PM081동기화
//		reWriteData();
	}
	
	// myung 20131120 DELETE 촬영 후 서피스뷰는 destroy됨으로 params는 null.
	// @Override
	// public void dismiss() {
	// Parameters params = camera.getParameters();
	// // if(params.getFlashMode().equals(Parameters.FLASH_MODE_TORCH))
	// params.setFlashMode(Parameters.FLASH_MODE_OFF);
	// super.dismiss();
	// }




	public void showShot(int mode, int position) {
		switch (mode) {
		case 0:
			bt_shot.setText("촬영");
			iv_picture.setVisibility(View.GONE);
			surfaceView.setVisibility(View.VISIBLE);
			if(camera != null) {
				kog.e("Jonathan", "Jonathan camera3");


				new Thread() {
					public void run() {
						try {
							sleep(500);

							LogUtil.d("hjt", "camera showShot!!");

							camera.setPreviewDisplay(surfaceHolder);
							camera.startPreview();
							safeToTakePicture = true;
							bt_shot.setVisibility(View.VISIBLE);

						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}.start();


				kog.e("Jonathan", "Jonathan camera4");


			}
			
			break;
		case 1:
			BitmapFactory.Options options = new BitmapFactory.Options ();
			options.inSampleSize = 2;
			options.inJustDecodeBounds = false;

			if("081".equals(PM_What))
			{
				bt_shot.setText("재촬영");
				// 이미지보기 테스트
				Bitmap bitmap = BitmapFactory.decodeFile(pm081.get(position).PATH, options);
				surfaceView.setVisibility(View.GONE);
				iv_picture.setVisibility(View.VISIBLE);
				iv_picture.setImageBitmap(bitmap);
				iv_picture.invalidate();
			}
			else if("033".equals(PM_What))
			{
				bt_shot.setText("재촬영");
				// 이미지보기 테스트
				Bitmap bitmap = BitmapFactory.decodeFile(pm033.get(position).PATH, options);
				surfaceView.setVisibility(View.GONE);
				iv_picture.setVisibility(View.VISIBLE);
				iv_picture.setImageBitmap(bitmap);
				iv_picture.invalidate();
			}
			else if("056".equals(PM_What))
			{
				bt_shot.setText("재촬영");
				// 이미지보기 테스트
				Bitmap bitmap = BitmapFactory.decodeFile(pm056.get(position).PATH, options);
				surfaceView.setVisibility(View.GONE);
				iv_picture.setVisibility(View.VISIBLE);
				iv_picture.setImageBitmap(bitmap);
				iv_picture.invalidate();
			}
			else
			{
				bt_shot.setText("재촬영");
				// 이미지보기 테스트
				Bitmap bitmap = BitmapFactory.decodeFile(pm081.get(position).PATH, options);
				surfaceView.setVisibility(View.GONE);
				iv_picture.setVisibility(View.VISIBLE);
				iv_picture.setImageBitmap(bitmap);
				iv_picture.invalidate();
			}
			
//			bt_shot.setText("재촬영");
//			// 이미지보기 테스트
//			Bitmap bitmap = BitmapFactory.decodeFile(pm081.get(position).PATH);
//			iv_picture.setVisibility(View.VISIBLE);
//			iv_picture.setImageBitmap(bitmap);
//			surfaceView.setVisibility(View.GONE);
			break;
		}
	}

	@Override
	public void onClick(View v) {
		int checked_num;
		switch (v.getId()) {
		case R.id.tire_picture_close_id:
			dismiss();
			break;
		case R.id.tire_picture_shot_id:
		case R.id.tire_picture_take_id:
			if("081".equals(PM_What))
			{
//				checked_num = tpda.getCheckPosition();
				checked_num = tpda.getSelectPosition();
				if (checked_num == -1)
					return;
				if (!pm081.get(checked_num).CHECKED) {
					// myung 20131202 ADD 체크 안하고 포커스만 있는 상태에서 촬영 버튼 클릭 시 메시지 출력.
					EventPopupC epc = new EventPopupC(context);
					epc.show("촬영대상을 체크해주세요.");
					return;
				}

				if (bt_shot.getText().toString().equals("재촬영")) 
				{
					String path = pm081.get(checked_num).PATH;
					deletePic(path);
					showShot(0, checked_num);
					pm081.get(checked_num).PATH = "";
					tpda.notifyDataSetChanged();
					return;
				}
			}
			else if("033".equals(PM_What))
			{
//				checked_num = tpda_pm033.getCheckPosition();
				checked_num = tpda_pm033.getSelectPosition();
				if (checked_num == -1)
					return;
				if (!pm033.get(checked_num).CHECKED) {
					// myung 20131202 ADD 체크 안하고 포커스만 있는 상태에서 촬영 버튼 클릭 시 메시지 출력.
					EventPopupC epc = new EventPopupC(context);
					epc.show("촬영대상을 체크해주세요.");
					return;
				}

				if (bt_shot.getText().toString().equals("재촬영")) 
				{
					String path = pm033.get(checked_num).PATH;
					deletePic(path);
					showShot(0, checked_num);
					pm033.get(checked_num).PATH = "";
					tpda_pm033.notifyDataSetChanged();
					return;
				}
			}
			else if("056".equals(PM_What))
			{
//				checked_num = tpda_pm056.getCheckPosition();
				checked_num = tpda_pm056.getSelectPosition();
				if (checked_num == -1)
					return;
				if (!pm056.get(checked_num).CHECKED) {
					// myung 20131202 ADD 체크 안하고 포커스만 있는 상태에서 촬영 버튼 클릭 시 메시지 출력.
					EventPopupC epc = new EventPopupC(context);
					epc.show("촬영대상을 체크해주세요.");
					return;
				}

				if (bt_shot.getText().toString().equals("재촬영")) 
				{
					String path = pm056.get(checked_num).PATH;
					deletePic(path);
					showShot(0, checked_num);
					pm056.get(checked_num).PATH = "";
					tpda_pm056.notifyDataSetChanged();
					return;
				}
			}
			else
			{
//				checked_num = tpda.getCheckPosition();
				checked_num = tpda.getSelectPosition();
				if (checked_num == -1)
					return;
				if (!pm081.get(checked_num).CHECKED) {
					// myung 20131202 ADD 체크 안하고 포커스만 있는 상태에서 촬영 버튼 클릭 시 메시지 출력.
					EventPopupC epc = new EventPopupC(context);
					epc.show("촬영대상을 체크해주세요.");
					return;
				}

				if (bt_shot.getText().toString().equals("재촬영")) 
				{
					String path = pm081.get(checked_num).PATH;
					deletePic(path);
					showShot(0, checked_num);
					pm081.get(checked_num).PATH = "";
					tpda.notifyDataSetChanged();
					return;
				}
			}
			
			
			
//			checked_num = tpda.getCheckPosition();
//			if (checked_num == -1)
//				return;
//			if (!pm081.get(checked_num).CHECKED) {
//				// myung 20131202 ADD 체크 안하고 포커스만 있는 상태에서 촬영 버튼 클릭 시 메시지 출력.
//				EventPopupC epc = new EventPopupC(context);
//				epc.show("촬영대상을 체크해주세요.");
//				return;
//			}
//
//			if (bt_shot.getText().toString().equals("재촬영")) 
//			{
//				String path = pm081.get(checked_num).PATH;
//				deletePic(path);
//				showShot(0, checked_num);
//				pm081.get(checked_num).PATH = "";
//				tpda.notifyDataSetChanged();
//				return;
//			}
			if(v.getId() == R.id.tire_picture_take_id) {
				surfaceView.destroyDrawingCache();
				surfaceView.setVisibility(View.GONE);
				try {
					if(mTireFragment != null) {
						mTireFragment.startActivityGallery();
					}
					else {
						try {
							Intent intent = new Intent();
							intent.setType("image/*");
							intent.setAction(Intent.ACTION_GET_CONTENT);
							if(context instanceof Menu2_1_Activity){
								((Menu2_1_Activity) context).startActivityForResult(intent, 2);
							}
						} catch (Exception e){
							e.printStackTrace();
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				break;
			}
			kog.e("Jonathan", "Jonathan camera5");

			if (safeToTakePicture && myAutoFocusCallback != null)
			{
				camera.autoFocus(new AutoFocusCallback() {
					public void onAutoFocus(boolean success, Camera camera) {
						if (success) {
							kog.e("Jonathan", "Jonathan camera6");
							kog.e("Jonathan", "Jonathan camera :: " + myShutterCallback + "  " + myPictureCallback_RAW + "   " + myPictureCallback_JPG);
							camera.takePicture(myShutterCallback, myPictureCallback_RAW, myPictureCallback_JPG);
							safeToTakePicture = false;
							kog.e("Jonathan", "Jonathan camera7");
							bt_shot.setVisibility(View.INVISIBLE);
						}
					}
				});


			}



			break;

		case R.id.tire_picture_surface_id:


//			if(myAutoFocusCallback != null )
//			{
//				camera.autoFocus(myAutoFocusCallback);
//			}


			break;


		case R.id.tire_picture_delete_id:
			
			if("081".equals(PM_What))
			{
//				checked_num = tpda.getCheckPosition();
				checked_num = tpda.getSelectPosition();
				if (checked_num == -1)
					return;
				if (!pm081.get(checked_num).CHECKED)
					return;
				if (pm081.get(checked_num).PATH.equals(""))
					return;

				String path = pm081.get(checked_num).PATH;
				deletePic(path);
				showShot(0, checked_num);
				pm081.get(checked_num).PATH = "";
				tpda.notifyDataSetChanged();
			}
			else if("033".equals(PM_What))
			{
//				checked_num = tpda_pm033.getCheckPosition();
				checked_num = tpda_pm033.getSelectPosition();
				if (checked_num == -1)
					return;
				if (!pm033.get(checked_num).CHECKED)
					return;
				if (pm033.get(checked_num).PATH.equals(""))
					return;

				String path = pm033.get(checked_num).PATH;
				deletePic(path);
				showShot(0, checked_num);
				pm033.get(checked_num).PATH = "";
				tpda_pm033.notifyDataSetChanged();
			}
			else if("056".equals(PM_What))
			{
//				checked_num = tpda_pm056.getCheckPosition();
				checked_num = tpda_pm056.getSelectPosition();
				if (checked_num == -1)
					return;
				if (!pm056.get(checked_num).CHECKED)
					return;
				if (pm056.get(checked_num).PATH.equals(""))
					return;

				String path = pm056.get(checked_num).PATH;
				deletePic(path);
				showShot(0, checked_num);
				pm056.get(checked_num).PATH = "";
				tpda_pm056.notifyDataSetChanged();
			}
			else
			{
//				checked_num = tpda.getCheckPosition();
				checked_num = tpda.getSelectPosition();
				if (checked_num == -1)
					return;
				if (!pm081.get(checked_num).CHECKED)
					return;
				if (pm081.get(checked_num).PATH.equals(""))
					return;

				String path = pm081.get(checked_num).PATH;
				deletePic(path);
				showShot(0, checked_num);
				pm081.get(checked_num).PATH = "";
				tpda.notifyDataSetChanged();
			}
			
//			checked_num = tpda.getCheckPosition();
//			if (checked_num == -1)
//				return;
//			if (!pm081.get(checked_num).CHECKED)
//				return;
//			if (pm081.get(checked_num).PATH.equals(""))
//				return;
//
//			String path = pm081.get(checked_num).PATH;
//			deletePic(path);
//			showShot(0, checked_num);
//			pm081.get(checked_num).PATH = "";
//			tpda.notifyDataSetChanged();
			break;
		}
	}

	public void deletePic(String path) {
		File file = new File(path);
		if (file.exists()) {
			file.delete();
			Toast.makeText(context, "이미지가 삭제되었습니다.", 0).show();
		}

	}

	AutoFocusCallback myAutoFocusCallback = new AutoFocusCallback() {
		@Override
		public void onAutoFocus(boolean arg0, Camera arg1) {
		}
	};
	ShutterCallback myShutterCallback = new ShutterCallback() {
		@Override
		public void onShutter() {
		}
	};
	PictureCallback myPictureCallback_RAW = new PictureCallback() {
		@Override
		public void onPictureTaken(byte[] arg0, Camera arg1) {
		}
	};
	PictureCallback myPictureCallback_JPG = new PictureCallback() {
		@Override
		public void onPictureTaken(byte[] arg0, Camera arg1) {
			Bitmap bm = BitmapFactory.decodeByteArray(arg0, 0, arg0.length);
//			Log.i("#", "### 사진크기" + bm.getWidth() + "/" + bm.getHeight());
			bm = Bitmap.createScaledBitmap(bm, 585, 481, true);

			// 매트릭스 좌우반전
			// Matrix matrix = new Matrix();
			// matrix.setScale(-1, 1);
			// bm = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(),
			// matrix, false);

			String path = Environment.getExternalStorageDirectory()
					+ File.separator + DEFINE.APP_NAME + File.separator;
			String name = +System.currentTimeMillis() + ".jpg";
			Log.i("hjt", "### 사진 위치" + path + name);
//			Log.i("#", "### 사진파일명" + name);
			File file = new File(path);
			if (!file.exists())
				file.mkdirs();

			file = new File(path + name);

			FileOutputStream fos = null;
			try {

				bt_shot.setVisibility(View.VISIBLE);
				
				if("081".equals(PM_What))
				{
					fos = new FileOutputStream(file);
					bm.compress(CompressFormat.JPEG, 100, fos);
					PM081 pm = pm081.get(tpda.getCheckPosition());
					pm.PATH = path + name;
					tpda.notifyDataSetChanged();
					showShot(1, tpda.getCheckPosition());
				}
				else if("033".equals(PM_What))
				{
					fos = new FileOutputStream(file);
					bm.compress(CompressFormat.JPEG, 100, fos);
					PM033 pm = pm033.get(tpda_pm033.getCheckPosition());
					pm.PATH = path + name;
					tpda_pm033.notifyDataSetChanged();
					showShot(1, tpda_pm033.getCheckPosition());
				}
				else if("056".equals(PM_What))
				{
					fos = new FileOutputStream(file);
					bm.compress(CompressFormat.JPEG, 100, fos);
					PM056 pm = pm056.get(tpda_pm056.getCheckPosition());
					pm.PATH = path + name;
					tpda_pm056.notifyDataSetChanged();
					showShot(1, tpda_pm056.getCheckPosition());
				}
				else
				{
					fos = new FileOutputStream(file);
					bm.compress(CompressFormat.JPEG, 100, fos);
					PM081 pm = pm081.get(tpda.getCheckPosition());
					pm.PATH = path + name;
					tpda.notifyDataSetChanged();
					showShot(1, tpda.getCheckPosition());
				}
				
//				fos = new FileOutputStream(file);
//				bm.compress(CompressFormat.JPEG, 100, fos);
//				PM081 pm = pm081.get(tpda.getCheckPosition());
//				pm.PATH = path + name;
//				tpda.notifyDataSetChanged();
//				showShot(1, tpda.getCheckPosition());
				
				
			} catch (FileNotFoundException e) {
				e.printStackTrace();
				EventPopupC epc = new EventPopupC(context);
				epc.show("다시 시도해 주십시오");
				return;
			} finally {
				try {
					fos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			bm = null;
		}
	};

	private ArrayList<PM081> initPM081() {
		String TABLE_NAME = "O_ITAB1";
		ArrayList<PM081> pm081_arr = new ArrayList<PM081>();
		pm081_arr.clear();
		String path = context.getExternalCacheDir() + "/DATABASE/"
				+ DEFINE.SQLLITE_DB_NAME;
		SQLiteDatabase sqlite = SQLiteDatabase.openDatabase(path, null,
				SQLiteDatabase.OPEN_READONLY);
		Cursor cursor = sqlite.rawQuery("select ZCODEV, ZCODEVT from "
				+ TABLE_NAME + " where ZCODEH = 'PM081'", null);
//		Cursor cursor = sqlite.rawQuery("select ZCODEV, ZCODEVT from "
//				+ TABLE_NAME + " where ZCODEH = 'PM033'", null);
		PM081 pm;
		while (cursor.moveToNext()) {
			int calnum1 = cursor.getColumnIndex("ZCODEV");
			int calnum2 = cursor.getColumnIndex("ZCODEVT");
			String zcodev = cursor.getString(calnum1);
			String zcodevt = cursor.getString(calnum2);

			pm = new PM081();
			pm.ZCODEV = zcodev;
			pm.ZCODEVT = zcodevt.replace("사이즈", "").replace("(주행거리)", "");
			pm.CHECKED = false;
			pm.ONESIDE_WEAR = false;
			pm.PATH = "";
			pm081_arr.add(pm);
		}
		cursor.close();
//		sqlite.close();
		
		
		
		for(int i = 101 ; i < 105 ; i++)
		{
			PM081 tmp = new PM081();
			tmp.ZCODEV = String.valueOf(i);
			tmp.ZCODEVT = "기타"+(i-100);
			tmp.CHECKED = false;
			tmp.PATH = "";
			tmp.ONESIDE_WEAR = false;
			pm081_arr.add(tmp);
		}
		
		
		return pm081_arr;
	}
	
	
	
	
	private ArrayList<PM033> initPM033() {
		String TABLE_NAME = "O_ITAB1";
		ArrayList<PM033> pm033_arr = new ArrayList<PM033>();
		pm033_arr.clear();
		String path = context.getExternalCacheDir() + "/DATABASE/"
				+ DEFINE.SQLLITE_DB_NAME;
		SQLiteDatabase sqlite = SQLiteDatabase.openDatabase(path, null,
				SQLiteDatabase.OPEN_READONLY);
		Cursor cursor = sqlite.rawQuery("select ZCODEV, ZCODEVT from "
				+ TABLE_NAME + " where ZCODEH = 'PM033'", null);
//		Cursor cursor = sqlite.rawQuery("select ZCODEV, ZCODEVT from "
//				+ TABLE_NAME + " where ZCODEH = 'PM033'", null);
		kog.e("Jonathan", " pm033 path :: " + path );
		
		PM033 pm;
		while (cursor.moveToNext()) {
			int calnum1 = cursor.getColumnIndex("ZCODEV");
			int calnum2 = cursor.getColumnIndex("ZCODEVT");
			String zcodev = cursor.getString(calnum1);
			String zcodevt = cursor.getString(calnum2);

			pm = new PM033();
			pm.ZCODEV = zcodev;
			pm.ZCODEVT = zcodevt.replace("사이즈", "").replace("(주행거리)", "");
			pm.CHECKED = false;
			pm.ONESIDE_WEAR = false;
			pm.PATH = "";
			pm033_arr.add(pm);
		}
		cursor.close();
//		sqlite.close();
		return pm033_arr;
	}
	
	
	private ArrayList<PM056> initPM056() {
		String TABLE_NAME = "O_ITAB1";
		ArrayList<PM056> pm056_arr = new ArrayList<PM056>();
		pm056_arr.clear();
		String path = context.getExternalCacheDir() + "/DATABASE/"
				+ DEFINE.SQLLITE_DB_NAME;
		SQLiteDatabase sqlite = SQLiteDatabase.openDatabase(path, null,
				SQLiteDatabase.OPEN_READONLY);
		Cursor cursor = sqlite.rawQuery("select ZCODEV, ZCODEVT from "
				+ TABLE_NAME + " where ZCODEH = 'PM056'", null);
//		Cursor cursor = sqlite.rawQuery("select ZCODEV, ZCODEVT from "
//				+ TABLE_NAME + " where ZCODEH = 'PM033'", null);
		
		kog.e("Jonathan", " pm056 path :: " + path );
		
		PM056 pm;
		while (cursor.moveToNext()) {
			int calnum1 = cursor.getColumnIndex("ZCODEV");
			int calnum2 = cursor.getColumnIndex("ZCODEVT");
			String zcodev = cursor.getString(calnum1);
			String zcodevt = cursor.getString(calnum2);

			pm = new PM056();
			pm.ZCODEV = zcodev;
			pm.ZCODEVT = zcodevt.replace("사이즈", "").replace("(주행거리)", "");
			pm.CHECKED = false;
			pm.ONESIDE_WEAR = false;
			pm.PATH = "";
			pm056_arr.add(pm);
		}
		cursor.close();
//		sqlite.close();
		return pm056_arr;
	}

	//myung 20131202 UPDATE PM081동기화
//	private void reWriteData(){
////		pm081.clear();
////		pm081 = initPM081();
//		for (int i = 0; i < pm081.size(); i++) {
//			pm081.get(i).CHECKED 		= false;
//			pm081.get(i).ONESIDE_WEAR 	= false;
//			pm081.get(i).PATH 			= "";
//			for (int j = 0; j < insPM081.size(); j++) {
//				if (pm081.get(i).ZCODEV.equals(insPM081.get(j).ZCODEV)) {
//					pm081.get(i).CHECKED 		= insPM081.get(j).CHECKED;
//					pm081.get(i).ONESIDE_WEAR 	= insPM081.get(j).ONESIDE_WEAR;
//					pm081.get(i).PATH 			= insPM081.get(j).PATH;
//				}
//			}
//		}
//		
//		tpda.notifyDataSetChanged();
//	}
	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		if (camera != null) {
			try {

				LogUtil.d("hjt", "camera showShot!!");
				camera.setPreviewDisplay(surfaceHolder);
				camera.startPreview();
				cb_flash.setChecked(true);
//				Parameters params = camera.getParameters();
//				params.setFlashMode(Parameters.FLASH_MODE_TORCH);
//				camera.setParameters(params);
				safeToTakePicture = true;
				bt_shot.setVisibility(View.VISIBLE);
				kog.e("Jonathan", "Jonathan camera2");


			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		camera = Camera.open(0);
		if (cb_flash != null){
			cb_flash.setChecked(true);
		}
		if(camera != null) {
			Parameters params = camera.getParameters();
			if (params != null) {
				params.setFlashMode(Parameters.FLASH_MODE_TORCH);
				camera.setParameters(params);
			}
		}
	}




	public void showPreViews() {
		params = camera.getParameters();
		List<Size> ListPreviewSize;
		ListPreviewSize = params.getSupportedPreviewSizes();
		int previewW = params.getPreviewSize().width;
		int previewH = params.getPreviewSize().height;

		if (ListPreviewSize == null) {
			return;
		} else {
			for (int i = 0; i < ListPreviewSize.size(); i++) {
				Size size = ListPreviewSize.get(i);
//				Log.i("해상도", "ListPreviewSize(" + i + "):" + size.width + ","
//						+ size.height);

				if (previewW < size.width) {
					previewW = size.width;
					previewH = size.height;
				}
			}
		}
		previewW = 400;
		previewH = 300;
		RelativeLayout.LayoutParams layout = new RelativeLayout.LayoutParams(
				previewW, previewH);
		surfaceView.setLayoutParams(layout);
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
//		Log.i("surfaceDestroyed", "surfaceDestroyed");
		// myung 20131127 ADD 예외처리
		cb_flash.setChecked(false);
		if (camera != null) {
			camera.stopPreview();
			camera.setPreviewCallback(null);
			camera.release();
			camera = null;
		}

	}
	
	class Compare1 implements Comparator<PM081>
	{
		@Override
		public int compare(PM081 arg0, PM081 arg1)
		{	
			String s1 = (String) arg0.ZCODEV;
			String s2 = (String) arg1.ZCODEV;
			
			return  Integer.valueOf(s1) < Integer.valueOf(s2) ? -1 :1;
//			return  s1.compareTo(s2);
		}
	}
	
	
	class Compare1_1 implements Comparator<PM081>
	{
		@Override
		public int compare(PM081 arg0, PM081 arg1)
		{	
			
			String s1 = (String) arg0.ZCODEVT;
			String s2 = (String) arg1.ZCODEVT;
			
			return  s1.compareTo(s2);
//			return  s1.compareTo(s2);
		}
	}
	
	
	//Jonathan 150407
	class Compare2 implements Comparator<PM033>
	{
		@Override
		public int compare(PM033 arg0, PM033 arg1)
		{	
			String s1 = (String) arg0.ZCODEV;
			String s2 = (String) arg1.ZCODEV;
			
			return  Integer.valueOf(s1) < Integer.valueOf(s2) ? -1 :1;
//			return  s1.compareTo(s2);
		}
	}
	
	
	class Compare3 implements Comparator<PM056>
	{
		@Override
		public int compare(PM056 arg0, PM056 arg1)
		{	
			String s1 = (String) arg0.ZCODEV;
			String s2 = (String) arg1.ZCODEV;
			
			return  Integer.valueOf(s1) < Integer.valueOf(s2) ? -1 :1;
//			return  s1.compareTo(s2);
		}
	}
	public void sendImg(Bitmap bmp, Intent data){
		Bitmap bm = null;
		bm = Bitmap.createScaledBitmap(bmp, 585, 481, true);
		safeToTakePicture = false;
		// 매트릭스 좌우반전
		// Matrix matrix = new Matrix();
		// matrix.setScale(-1, 1);
		// bm = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(),
		// matrix, false);

		String path = Environment.getExternalStorageDirectory().getAbsolutePath()
				+ File.separator + DEFINE.APP_NAME + File.separator;
		String name = +System.currentTimeMillis() + ".jpg";
		Log.i("hjt", "### 사진 위치" + path + name);

		String realPath;
		// SDK < API11
//		if (Build.VERSION.SDK_INT < 11)
//			realPath = CommonUtil.getRealPathFromURI_BelowAPI11(context, data.getData());
//
//			// SDK >= 11 && SDK < 19
//		else if (Build.VERSION.SDK_INT < 19)
//			realPath = CommonUtil.getRealPathFromURI_API11to18(context, data.getData());
//
//			// SDK > 19 (Android 4.4)
//		else
//			realPath = CommonUtil.getRealPathFromURI_API19(context, data.getData());

//			Log.i("#", "### 사진파일명" + name);
		File file = new File(path);
		if (!file.exists())
			file.mkdirs();
//
		file = new File(path + name);

		FileOutputStream fos = null;
		try {

			bt_shot.setVisibility(View.VISIBLE);
			if("081".equals(PM_What))
			{
				fos = new FileOutputStream(file);
				bm.compress(CompressFormat.JPEG, 100, fos);
				PM081 pm = pm081.get(tpda.getCheckPosition());
				pm.PATH = path + name;
				tpda.notifyDataSetChanged();
				showShot(1, tpda.getCheckPosition());
			}
			else if("033".equals(PM_What))
			{
				fos = new FileOutputStream(file);
				bm.compress(CompressFormat.JPEG, 100, fos);
				PM033 pm = pm033.get(tpda_pm033.getCheckPosition());
				pm.PATH = path + name;
				tpda_pm033.notifyDataSetChanged();
				showShot(1, tpda_pm033.getCheckPosition());
				if(tpda_pm033 != null){
					tpda_pm033.notifyDataSetChanged();
				}
			}
			else if("056".equals(PM_What))
			{
				fos = new FileOutputStream(file);
				bm.compress(CompressFormat.JPEG, 100, fos);
				PM056 pm = pm056.get(tpda_pm056.getCheckPosition());
				pm.PATH = path + name;
				showShot(1, tpda_pm056.getCheckPosition());
				if(tpda_pm056 != null){
					tpda_pm056.notifyDataSetChanged();
				}
			}
			else
			{
				showShot(1, tpda_pm056.getCheckPosition());
				fos = new FileOutputStream(file);
				bm.compress(CompressFormat.JPEG, 100, fos);
				PM081 pm = pm081.get(tpda.getCheckPosition());
				pm.PATH = path + name;
				showShot(1, tpda.getCheckPosition());
				if(tpda_pm056 != null){
					tpda_pm056.notifyDataSetChanged();
				}
				if(tpda != null) {
					tpda.notifyDataSetChanged();
				}
			}
			if(tpda != null) {
				tpda.notifyDataSetChanged();
			}

//				fos = new FileOutputStream(file);
//				bm.compress(CompressFormat.JPEG, 100, fos);
//				PM081 pm = pm081.get(tpda.getCheckPosition());
//				pm.PATH = path + name;
//				tpda.notifyDataSetChanged();
//				showShot(1, tpda.getCheckPosition());


		} catch (FileNotFoundException e) {
			e.printStackTrace();
			EventPopupC epc = new EventPopupC(context);
			epc.show("다시 시도해 주십시오");
			return;
		} finally {
			try {
				fos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		bm = null;
	}
	@Override
	public void dismiss() {
		super.dismiss();
	}
}
