package com.ktrental.product;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ktrental.R;
import com.ktrental.activity.LoginActivity;
import com.ktrental.cm.connect.ConnectController2;
import com.ktrental.common.DEFINE;
import com.ktrental.common.KtRentalApplication;
import com.ktrental.dialog.History_Dialog;
import com.ktrental.dialog.Menu7_2_RepairSearch_Dialog;
import com.ktrental.dialog.PartsTransfer_Cars_Dialog;
import com.ktrental.dialog.Tire_List_Dialog;
import com.ktrental.model.LoginModel;
import com.ktrental.popup.EventPopupC;
import com.ktrental.popup.EventPopupCC;
import com.ktrental.popup.ProgressPopup;
import com.ktrental.util.CommonUtil;
import com.ktrental.util.SharedPreferencesUtil;
import com.ktrental.util.kog;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public abstract class BaseActivity extends FragmentActivity {
	protected Context context;
	protected ConnectController2 cc;
	
	protected ProgressPopup pp;
	
	protected RelativeLayout MENU_LAYOUT;
	protected Menu_Layout ML;
	protected Menu7_2_RepairSearch_Dialog m7_2Dialog;
	protected History_Dialog hd;
	protected PartsTransfer_Cars_Dialog PTCDF;
	

	public static String mPermgp = LoginActivity.mPermgp;

	Intent min;

	protected void init() {
		context = this;
		cc = new ConnectController2(this);
		pp = new ProgressPopup(this);
		pp.setMessage("조회 중 입니다.");
		// myung 20131203 ADD 상태 변경 시 로딩바 추가 필요.
		pp.show();
		new Handler().postDelayed(mHideRunnable, 300);
		
		
	}

	protected void init(int i) {
		context = this;
		if (cc == null)
			cc = new ConnectController2(this);
		if (pp == null)
			pp = new ProgressPopup(this);
		pp.setMessage("조회 중 입니다.");
		

		// myung 20131203 ADD 상태 변경 시 로딩바 추가 필요.
		// pp.show();
		// new Handler().postDelayed(mHideRunnable, 300);
		if (i > 0) {
			pp.show();
			new Handler().postDelayed(mHideRunnable, i);
		}

	}

	// myung 20131203 ADD 상태 변경 시 로딩바 추가 필요.
	private Runnable mHideRunnable = new Runnable() {
		@Override
		public void run() {
			if (pp != null)
				pp.hide();
		}
	};

	LoginModel model = KtRentalApplication.getLoginModel();

	public void onMenu1(View v) {
		// myung 20131213 ADD 이동->회사구분 오토리스 선택시에 다른 메뉴에서 모두 적용되어 버리는 현상
		model.getModelMap().put("BUKRS", "3000");
		Intent in = new Intent(this, Menu1_Activity.class);
		in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
				| Intent.FLAG_ACTIVITY_SINGLE_TOP);
		// Jonathan 14.08.05 추가
		in.putExtra("is_CarManager", mPermgp);
		startActivity(in);
		overridePendingTransition(0, 0);
		finish();
	}

	public void onMenu2(View v) {
		if (checkTopActivity("com.ktrental.product.Menu2_Activity"))
			return;
		// myung 20131213 ADD 이동->회사구분 오토리스 선택시에 다른 메뉴에서 모두 적용되어 버리는 현상
		model.getModelMap().put("BUKRS", "3000");
		Intent in = new Intent(this, Menu2_Activity.class);
		in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
				| Intent.FLAG_ACTIVITY_SINGLE_TOP);
		// Jonathan 14.08.05 추가
		in.putExtra("is_CarManager", mPermgp);
		startActivity(in);
		overridePendingTransition(0, 0);
		finish();
	}

	public void onMenu3(View v) {
		if (checkTopActivity("com.ktrental.product.Menu3_Activity"))
			return;
		// myung 20131213 ADD 이동->회사구분 오토리스 선택시에 다른 메뉴에서 모두 적용되어 버리는 현상
		model.getModelMap().put("BUKRS", "3000");
		Intent in = new Intent(this, Menu3_Activity.class);
		in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
				| Intent.FLAG_ACTIVITY_SINGLE_TOP);
		// Jonathan 14.08.05 추가
		in.putExtra("is_CarManager", mPermgp);
		startActivity(in);
		overridePendingTransition(0, 0);
		finish();
	}

	public void onMenu4(View v) {
		if (checkTopActivity("com.ktrental.product.Menu4_1_Activity"))
			return;
		// myung 20131213 ADD 이동->회사구분 오토리스 선택시에 다른 메뉴에서 모두 적용되어 버리는 현상
		model.getModelMap().put("BUKRS", "3000");
		Intent in = new Intent(this, Menu4_1_Activity.class);
		in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
				| Intent.FLAG_ACTIVITY_SINGLE_TOP);
		// Jonathan 14.08.05 추가
		in.putExtra("is_CarManager", mPermgp);
		startActivity(in);
		overridePendingTransition(0, 0);
		finish();
	}
	
	//Jonathan 14.06.17 추가 고객조회 버튼 추가 요청
	public void onMenu8(View v) {
		model.getModelMap().put("BUKRS", "3000");
		
		kog.e("Jonathan", "받아온 mPermgp  :: " + mPermgp);
		
		if (MENU_LAYOUT == null) {
			MENU_LAYOUT = (RelativeLayout) findViewById(R.id.menu_layout_id);
			ML = new Menu_Layout(this);
			RelativeLayout back = (RelativeLayout) ML.findViewById(R.id.back);


			TextView menu3 = (TextView) ML.findViewById(R.id.menu3);
			menu3.setText("고객조회");
			menu3.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
//					customMenu7_2_StartDialog();
					customMenu7_1_StartActivity();
				}
			});
			
			//Jonathan 14.07.30
			TextView menu4 = (TextView) ML.findViewById(R.id.menu4);
			menu4.setVisibility(View.VISIBLE);
			menu4.setText("차량운행일지");
			menu4.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
//					customMenu7_2_StartDialog();
					customMenu7_2_MovementActivity();
				}
			});
			
			
			//Jonathan 14.07.30
			TextView menu5 = (TextView) ML.findViewById(R.id.menu5);
			menu5.setVisibility(View.VISIBLE);
			menu5.setText("타이어신청 내역");
			menu5.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
////					customMenu7_2_StartDialog();
//					customMenu7_2_MovementActivity();
					Tire_List_Dialog tld = new Tire_List_Dialog(context, null);
					tld.show();
					
				}
			});
			
			
			
			TextView menu2 = (TextView) ML.findViewById(R.id.menu2);
			menu2.setVisibility(View.VISIBLE);
			menu2.setText("카매니저 조회");
			menu2.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
//					customMenu7_2_StartDialog();
					customMenu7_3_PartsTransferCarsDialog();
				}
			});
			

			setMenuBack(back);
			MENU_LAYOUT.addView(ML);
		} else {
			onBackPressed();
		}
		
		
	}

	public void onMenu5(View v) {
		if (checkTopActivity("com.ktrental.product.Menu5_Activity"))
			return;
		// myung 20131213 ADD 이동->회사구분 오토리스 선택시에 다른 메뉴에서 모두 적용되어 버리는 현상
		model.getModelMap().put("BUKRS", "3000");
		Intent in = new Intent(this, Menu5_Activity.class);
		in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
				| Intent.FLAG_ACTIVITY_SINGLE_TOP);
		// Jonathan 14.08.05 추가
		in.putExtra("is_CarManager", mPermgp);
		startActivity(in);
		overridePendingTransition(0, 0);
		finish();
	}

	// myung 20131223 ADD 메인메뉴-> 기타 항목 추가. 기타->1)고객조회 2)정비접수상세내역조회 추가
	public void onMenu7(View v) {
		model.getModelMap().put("BUKRS", "3000");
		if (MENU_LAYOUT == null) {
			MENU_LAYOUT = (RelativeLayout) findViewById(R.id.menu_layout_id);
			ML = new Menu_Layout(this);
			RelativeLayout back = (RelativeLayout) ML.findViewById(R.id.back);

//			TextView menu1 = (TextView) ML.findViewById(R.id.menu1);
//			menu1.setOnClickListener(new OnClickListener() {
//				@Override
//				public void onClick(View arg0) {
//					if (!checkTopActivity("com.ktrental.product.Menu7_1_CustomerSearch_Activity"))
//						customMenu7_1_StartActivity();
//					else
//						onGone();
//				}
//			});

			TextView menu2 = (TextView) ML.findViewById(R.id.menu2);
			menu2.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					customMenu7_2_StartDialog();
				}
			});
			
			TextView menu3 = (TextView) ML.findViewById(R.id.menu3);
			menu3.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					customMenu7_3_HistoryDialog();
				}
			});
			
			 
			//Joanthan 170213 공지사항 추가
			TextView menu4 = (TextView) ML.findViewById(R.id.menu4);
			menu4.setVisibility(View.VISIBLE);
			menu4.setText("공지사항");
			menu4.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					menu4_2_NoticeActivity();
				}
			});

			TextView menu6 = (TextView) ML.findViewById(R.id.menu6);
			menu6.setVisibility(View.VISIBLE);
			menu6.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					menu4_4_CorCard_Activity();
				}
			});
			
			// myung 20140106 ADD 차량운행일지 조회 추가
//			TextView menu4 = (TextView) ML.findViewById(R.id.menu4);
//			menu4.setOnClickListener(new OnClickListener() {
//				@Override
//				public void onClick(View arg0) {
//					customMenu7_2_MovementActivity();
//				}
//			});

			setMenuBack(back);
			MENU_LAYOUT.addView(ML);
		} else {
			onBackPressed();
		}
	}
	
	
	private void customMenu7_3_PartsTransferCarsDialog(){
		PTCDF = new PartsTransfer_Cars_Dialog(context);
		Button bt_dismiss = (Button) PTCDF
				.findViewById(R.id.partstransfer_parts_search_close_id);
		PTCDF.setTitle("카매니저 조회");
		TextView Driver_name = (TextView) PTCDF.findViewById(R.id.maintenance_date2_id1);
		Driver_name.setText("기사명");
		bt_dismiss.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				PTCDF.dismiss();
				onGone();
			}
		});
		Button bt_done = (Button) PTCDF.findViewById(R.id.partstransfer_car_search_done_id);
		bt_done.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				PTCDF.dismiss(); 
				onGone();
			}
		});
		PTCDF.show();
	}
	
	
	// myung 20140106 ADD 차량운행일지 조회 추가
	private void customMenu7_2_MovementActivity() {
//		btn_movement_name
		
		min = new Intent(this, Menu7_2_Movement_Activity.class);
		min.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
				| Intent.FLAG_ACTIVITY_SINGLE_TOP);
		//Jonathan 14.07.30
		min.putExtra("is_CarManager", mPermgp);
		
	
		
		startActivity(min);
		overridePendingTransition(0, 0);
	}

	//myung 20131224 ADD 메인메뉴-> 기타 항목 추가. 기타-> 정비이력조회 추가
	private void customMenu7_3_HistoryDialog(){
		hd = new History_Dialog(context, null, 0);
		Button bt_dismiss = (Button) hd
				.findViewById(R.id.history_dialog_close_id);
		bt_dismiss.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				hd.dismiss();
				onGone();
			}
		});
		Button bt_done = (Button) hd.findViewById(R.id.history_done_id);
		bt_done.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				try {
					hd.dismiss();
					onGone();
				} catch (Exception e){
					e.printStackTrace();
				}

			}
		});
		hd.show();
	}
	
	
	// Jonathan 170213 공지사항 화면추가
	private void menu4_2_NoticeActivity() {
//			min.putExtra("is_CarManager", mPermgp);
		min = new Intent(this, Menu4_2_Notice_Activity.class);
		min.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
				| Intent.FLAG_ACTIVITY_SINGLE_TOP);
		min.putExtra("is_CarManager", mPermgp);
		startActivity(min);
		overridePendingTransition(0, 0);
	}

	private void menu4_4_CorCard_Activity() {
//			min.putExtra("is_CarManager", mPermgp);
		min = new Intent(this, Menu4_4_CorCard_Activity.class);
		min.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
				| Intent.FLAG_ACTIVITY_SINGLE_TOP);
		min.putExtra("is_CarManager", mPermgp);
		startActivity(min);
		overridePendingTransition(0, 0);
	}
	
	
	
	// myung 20131223 ADD 메인메뉴-> 기타 항목 추가. 기타-> 1)고객조회
	private void customMenu7_1_StartActivity() {
//		min.putExtra("is_CarManager", mPermgp);
		min = new Intent(this, Menu7_1_CustomerSearch_Activity.class);
		min.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
				| Intent.FLAG_ACTIVITY_SINGLE_TOP);
		min.putExtra("is_CarManager", mPermgp);
		startActivity(min);
		overridePendingTransition(0, 0);
	}

	// myung 20131223 ADD 메인메뉴-> 기타 항목 추가. 기타-> 2)정비접수상세내역조회 추가
	private void customMenu7_2_StartDialog() {
		m7_2Dialog = new Menu7_2_RepairSearch_Dialog(context);
		Button bt_dismiss = (Button) m7_2Dialog
				.findViewById(R.id.menu7_repair_search_close_id);
		bt_dismiss.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				m7_2Dialog.dismiss();
				onGone();
			}
		});
		Button bt_done = (Button) m7_2Dialog.findViewById(R.id.btn_done);
		bt_done.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				m7_2Dialog.dismiss();
				onGone();
			}
		});
		
		m7_2Dialog.show();
	}

	private void customMenu4_1StartActivity() {
		min = new Intent(this, Menu4_1_Activity.class);
		min.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
				| Intent.FLAG_ACTIVITY_SINGLE_TOP);
		startActivity(min);
		overridePendingTransition(0, 0);
	}

	private void customMenu1_StartActivity() {
		min = new Intent(this, Menu1_Activity.class);
		min.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
				| Intent.FLAG_ACTIVITY_SINGLE_TOP);
		startActivity(min);
		overridePendingTransition(0, 0);
	}

	private boolean checkTopActivity(String strClassName) {

		ActivityManager am = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningTaskInfo> Info = am.getRunningTasks(1);
		ComponentName topActivity = Info.get(0).topActivity;
		String topactivityname = topActivity.getClassName();

		if (strClassName.equals(topactivityname))
			return true;
		else
			return false;

	}

	public void onFinish(View v) {
		final EventPopupCC ep1 = new EventPopupCC(context, "차량관리앱을 종료하시겠습니까?");
		Button bt_yes = (Button) ep1.findViewById(R.id.btn_ok);
		bt_yes.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				
//				cc.updateSession(context, "OUT", DEFINE.SESSION);
				
				ep1.dismiss();
				finish();
				Intent launchHome = new Intent(Intent.ACTION_MAIN);
				launchHome.addCategory(Intent.CATEGORY_DEFAULT);
				launchHome.addCategory(Intent.CATEGORY_HOME);
				startActivity(launchHome);
			}
		});
		Button bt_no = (Button) ep1.findViewById(R.id.btn_cancel);
		bt_no.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				ep1.dismiss();
			}
		});
		ep1.show();
	}
	
	public void onAutoCare(View v) {
		
		

	      try {
	          // G5앱을 호출
	          Intent intent = this.getPackageManager().getLaunchIntentForPackage("com.lotte.autocare");
	          intent.setAction(Intent.ACTION_MAIN);
	          startActivity(intent);
	      }catch (NullPointerException e){
	    	  
	    	  
	      }

		
		
		
	}
	
	

	public void setMenuBack(RelativeLayout back) {
		back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				onBackPressed();
			}
		});
	}

	@Override
	protected void onStop() {
		if (MENU_LAYOUT != null) {
			MENU_LAYOUT.removeView(ML);
			MENU_LAYOUT = null;
		}
		super.onStop();
	}

	@Override
	public void onBackPressed() {
		// myung 20131213 ADD 이동->회사구분 오토리스 선택시에 다른 메뉴에서 모두 적용되어 버리는 현상
		LoginModel model = KtRentalApplication.getLoginModel();
		model.getModelMap().put("BUKRS", "3000");

		if (MENU_LAYOUT != null) {
			onGone();
		}
		// myung
		// 홈 이외 페이지에서 홈으로 이동
		else if (!checkTopActivity("com.ktrental.product.Menu1_Activity")) {
			// myung 20131127 ADD
			finish();
			customMenu1_StartActivity();
		} else {
			// super.onBackPressed();
			onFinish(null);
		}
	}

	private void onGone() {
		LinearLayout menu_back = (LinearLayout) ML.findViewById(R.id.menu_back);
		menu_back.clearAnimation();
		Animation slide_out = AnimationUtils.loadAnimation(this,
				R.anim.slidemenu_out_left);
		slide_out.setAnimationListener(new AnimationListener() {
			public void onAnimationStart(Animation arg0) {
			}

			public void onAnimationRepeat(Animation arg0) {
			}

			public void onAnimationEnd(Animation arg0) {
				MENU_LAYOUT.removeView(ML);
				MENU_LAYOUT = null;
			}
		});
		menu_back.setAnimation(slide_out);
		menu_back.startLayoutAnimation();

		RelativeLayout back = (RelativeLayout) ML.findViewById(R.id.back);
		Animation fade_out = AnimationUtils.loadAnimation(this,
				R.anim.slidemenu_fadeout);
		back.setAnimation(fade_out);
		back.startLayoutAnimation();
	}

	public String getDateFormat(String date) {
		StringBuffer sb = new StringBuffer(date);
		if (date.length() == 8) {
			sb.insert(4, ".");
			int last = sb.length() - 2;
			sb.insert(last, ".");
		}
		return sb.toString();
	}

	public String getTimeFormat(String date) {
		StringBuffer sb = new StringBuffer(date);
		if (date.length() == 4) {
			sb.insert(2, ":");
		}
		return sb.toString();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		// myung 20131129 ADD 날짜변경되면 자동종료되는 부분 날짜저장 : 전역변수->Sharedpreference로
		// 변경
		
		 
		 
		String strCreatDate = CommonUtil.getCurrentDay();
		SharedPreferencesUtil shareU = SharedPreferencesUtil.getInstance();
		if(shareU == null){
			shareU = new SharedPreferencesUtil(this);
		}
		if(shareU != null) {
			shareU.setString("Restart_time_prefer", strCreatDate);
		}

		

	}

	@Override
	protected void onStart() {
		// startVerCheck();
		super.onStart();

		// myung 20131129 ADD 날짜변경되면 자동종료되는 부분 날짜저장 : 전역변수->Sharedpreference로
		// 변경
		String currentDay = CommonUtil.getCurrentDay();
		int year = Integer.parseInt(currentDay.substring(0, 4));
		int month = Integer.parseInt(currentDay.substring(4, 6));
		int day = Integer.parseInt(currentDay.substring(6, 8));

		SharedPreferencesUtil shareU = SharedPreferencesUtil.getInstance();
		String strCreatDate = shareU.getString("Restart_time_prefer",
				currentDay);

		int backYear = Integer.parseInt(strCreatDate.substring(0, 4));
		int backMonth = Integer.parseInt(strCreatDate.substring(4, 6));
		int backDay = Integer.parseInt(strCreatDate.substring(6, 8));

		if (day > backDay) { // 생성되었던 날짜가 재실행된 날짜보다 작으면 앱을 다시 실행한다.
			reStartLogin();
		} else {
			if (day < backDay) {
				if (month > backMonth || backYear < year) {
					reStartLogin();
				}
			}
		}
	}

	private void reStartLogin() {
		Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
		startActivity(intent);
		finish();
	}

	private void startVerCheck() {
		VersionAsync va = new VersionAsync(this) {
			@Override
			protected void onPostExecute(String result) {
				super.onPostExecute(result);
				// Log.i("####", "####버전정보" + result.trim() + "/" +
				// getAppVer());
				compareVer(result.trim(), getAppVer());
			}
		};
		va.execute();
	}

	public void compareVer(String new_ver, String now_ver) {
		if (new_ver.equals(now_ver)) {
		} else {
			showUpdate();
		}
	}

	public void showUpdate() {
		final EventPopupC epc = new EventPopupC(this);
		epc.setCancelable(false);
		Button bt_done = (Button) epc.findViewById(R.id.btn_ok);
		bt_done.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				download();
				epc.dismiss();
			}
		});
		epc.show("버전이 업데이트 되었습니다.\n다운로드 합니다.");
	}

	private void download() {
		DownloadTask dt = new DownloadTask(this);
		dt.execute();
	}

	private class DownloadTask extends AsyncTask<String, String, Boolean> {

		ProgressPopup pp;

		public DownloadTask(Context context) {
			pp = new ProgressPopup(context);
		}

		@Override
		protected void onPreExecute() {
			pp.setMessage("업데이트 버전을 다운로드 중입니다.");
			pp.show();
			super.onPreExecute();
		}

		@Override
		protected Boolean doInBackground(String... args) {
			String strApkName = DEFINE.DOWNLOAD_APK;
			String strLocalPath = Environment.getExternalStorageDirectory()
					.getAbsolutePath() + "/";
			String strServerApkPath = DEFINE.DOWNLOAD_URL;
			File file = new File(strLocalPath + strApkName);
			try {
				if (file.exists())
					file.delete();
				InputStream inputStream = new URL(strServerApkPath + strApkName)
						.openStream();
				OutputStream out = new FileOutputStream(file);
				writeFile(inputStream, out);
				out.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			Intent i = new Intent(Intent.ACTION_VIEW);
			i.setDataAndType(Uri.fromFile(file),
					"application/vnd.android.package-archive");
			startActivity(i);
			pp.dismiss();
			finish();
			return null;
		}

		public void writeFile(InputStream is, OutputStream os)
				throws IOException {
			byte[] buffer = new byte[1024];
			int length;
			while ((length = is.read(buffer)) >= 0) {
				os.write(buffer, 0, length);
			}
			is.close();
			os.flush();
			os.close();
		}

		@Override
		protected void onCancelled() {
			pp.dismiss();
			super.onCancelled();
		}
	}

	private String getAppVer() {
		String version = "0";
		try {
			PackageInfo i = getPackageManager().getPackageInfo(
					getPackageName(), 0);
			version = i.versionName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return version;
	}

	private class VersionAsync extends AsyncTask<String, String, String> {

		Context context;

		// ProgressPopup pp;
		public VersionAsync(Context context) {
			this.context = context;
			// pp = new ProgressPopup(context);
		}

		@Override
		protected void onPreExecute() {
			// pp.setMessage("버전을 조회 중입니다.");
			// pp.show();
			super.onPreExecute();
		}

		@Override
		protected String doInBackground(String... args) {
			// pp.dismiss();
			return loadApkVersionData();
		}

		@Override
		protected void onCancelled() {
			// pp.dismiss();
			super.onCancelled();
		}

		private String loadApkVersionData() {
			StringBuilder sb = new StringBuilder();
			try {
				String strLoadPage = "";
				strLoadPage = DEFINE.DOWNLOAD_URL + "version.txt";
				// Log.i("####", "####버전정보 : " + strLoadPage);
				URL url = new URL(strLoadPage);
				HttpURLConnection urlConnection = (HttpURLConnection) url
						.openConnection();
				if (urlConnection == null)
					return null;
				urlConnection.setConnectTimeout(10000);
				urlConnection.setUseCaches(false);
				if (urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
					InputStream inputStream = urlConnection.getInputStream();
					InputStreamReader isr = new InputStreamReader(inputStream,
							"utf-8");
					BufferedReader br = new BufferedReader(isr);
					while (true) {
						String line = br.readLine();
						if (line == null)
							break;
						sb.append(line + "\n");
					}
					br.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return sb.toString();
		}
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		if(pp != null){
			pp = null;
		}
		super.onDestroy();
	}
	

}
