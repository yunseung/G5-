package com.ktrental.activity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.PowerManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.ktrental.R;
import com.ktrental.cm.db.SqlLiteAdapter;
import com.ktrental.common.DEFINE;
import com.ktrental.common.KtRentalApplication;
import com.ktrental.dialog.Rounds_Dialog;
import com.ktrental.fragment.DrawerFragment;
import com.ktrental.popup.EventPopup1;
import com.ktrental.popup.EventPopupC;
import com.ktrental.popup.EventPopupCC;
import com.ktrental.popup.ProgressPopup;
import com.ktrental.product.Menu1_Activity;
import com.ktrental.util.CommonUtil;
import com.ktrental.util.FragmentController;
import com.ktrental.util.LogUtil;
import com.ktrental.util.OnEventCancelListener;
import com.ktrental.util.OnEventOkListener;
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

//import com.ktrental.fragment.SlidingFragment;

public class Main_Activity extends FragmentActivity {
 
	// private SlidingFragment mSlidingFragment;

	private DrawerFragment mDrawerFragment;

	private LinearLayout mRootView;

	private static final String DB_PATH = "DATABASE";
	private static final String DOWNLOAD_PATH = "DOWNLOAD";
	private String mDbPath;

//	private Handler mHandler;

	private Runnable mRunnable;

	private long RESTART_TIME = 1000 * 60 * 60 * 24;
	
	
	private Intent emptyService;

	private PowerManager powerManager;
	private PowerManager.WakeLock wakeLock;

//	private String mCreatDate = "";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_activity);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

//		powerManager = (PowerManager) getSystemService(POWER_SERVICE);
//		wakeLock = powerManager.newWakeLock(powerManager.FULL_WAKE_LOCK, getLocalClassName());
//
//		wakeLock.acquire();


		// mSlidingFragment = new
		// SlidingFragment(SlidingFragment.class.getName(),
		// null);

		// FragmentTransaction ft =
		// getSupportFragmentManager().beginTransaction();
		// ft.replace(R.id.drawer_layout, mSlidingFragment);
		//
		// ft.commit();
		EmptyService.getActivity(this);
		

		mRootView = (LinearLayout) findViewById(R.id.ll_root);

		mDrawerFragment = (DrawerFragment) getSupportFragmentManager().findFragmentById(R.id.fm_drawer);

		String strCreatDate = CommonUtil.getCurrentDay();
//		mCreatDate = CommonUtil.getCurrentDay();
		
		SharedPreferencesUtil shareU = SharedPreferencesUtil.getInstance();
		shareU.setString("Restart_time_prefer", strCreatDate);
		
		startEmptyService(); 
		
		

	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		// mCreatDate = "20131027";
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		KtRentalApplication.getInstance().queryMaintenacePlan();

		//myung 201129 DELETE
//		runRestartChecking();

		// initDb();
		String currentDay = CommonUtil.getCurrentDay();
		int year = Integer.parseInt(currentDay.substring(0, 4));
		int month = Integer.parseInt(currentDay.substring(4, 6));
		int day = Integer.parseInt(currentDay.substring(6, 8));

		//myung 20131129 UPDATE 날짜변경되면 자동종료되는 부분  날짜저장 : 전역변수->Sharedpreference로 변경
		SharedPreferencesUtil shareU = SharedPreferencesUtil.getInstance();
		String strCreatDate = shareU.getString("Restart_time_prefer", currentDay);
		
		int backYear = Integer.parseInt(strCreatDate.substring(0, 4));
		int backMonth = Integer.parseInt(strCreatDate.substring(4, 6));
		int backDay = Integer.parseInt(strCreatDate.substring(6, 8));

		if(!kog.TEST)
		{
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
		//버전체크
		//Jonathan 14.12.16 다시 활성화 함.
//		startVerCheck();
	}
	
    private void startVerCheck()
        {
    	kog.e("Jonathan", "버전체크함~~~!!1");
        VersionAsync va = new VersionAsync(this)
            {
            @Override
            protected void onPostExecute(String result) {
				super.onPostExecute(result);
				compareVer(result.trim(), getAppVer());
			}
			};
        va.execute();
        }
    
    public void compareVer(String new_ver, String now_ver) {
    if (new_ver.equals(now_ver)) {
    	kog.e("Jonathan", "버전체크함~~~!! 버전 같음.");
        start();
    } else {
    	kog.e("Jonathan", "버전체크함~~~!! 버전 다름");
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

private void start() {
    // Intent in = new Intent(this, LoginActivity.class);
    // startActivity(in);
    // finish();
}

private class VersionAsync extends AsyncTask<String, String, String> {
    Context context;
//    ProgressPopup pp;

    public VersionAsync(Context context) {
        this.context = context;

//        pp = new ProgressPopup(context);
    }

    @Override
    protected void onPreExecute() {
//        pp.setMessage("버전을 조회 중입니다.");
//        pp.show();
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(String... args) {
//        pp.dismiss();
        return loadApkVersionData();
    }

    @Override
    protected void onCancelled() {
//        pp.dismiss();
        super.onCancelled();
    }

    private String loadApkVersionData() {
        StringBuilder sb = new StringBuilder();
        try {
            String strLoadPage = "";
            strLoadPage = DEFINE.DOWNLOAD_URL + "version.txt";
//            Log.i("####", "####버전정보 : " + strLoadPage);
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

//	private void runRestartChecking() {
//		Calendar calendar = new GregorianCalendar();
//		calendar.add(Calendar.DATE, 1);	//다음날
//
//		// int year = calendar.get(Calendar.YEAR);
//		// int month = calendar.get(Calendar.MONTH) + 1;
//		// int today = calendar.get(Calendar.DAY_OF_MONTH);
//		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
//		java.util.Date date = calendar.getTime();
//		String Today = new SimpleDateFormat("yyyyMMdd").format(date);
//		try {
//			try {
//				calendar.setTime(formatter.parse(Today));
//			} catch (java.text.ParseException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		} catch (ParseException e1) {
//			e1.printStackTrace();
//		}
//		Calendar cal = Calendar.getInstance();
//
//		RESTART_TIME = calendar.getTimeInMillis() - cal.getTimeInMillis();
//
//		mRunnable = new Runnable() {
//			@Override
//			public void run() {
//				reStartLogin();
//			}
//		};
//
//		mHandler = new Handler();
//		mHandler.postDelayed(mRunnable, RESTART_TIME);
//
//	}

	private void reStartLogin() {
		Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
		startActivity(intent);
		finish();
	}

	private void initDb() {
		if (CommonUtil.isValidSDCard()) { // sd card 사용가능 여

			File root = this.getExternalCacheDir();

			String dirPath = root.getPath() + "/";

			File file = new File(dirPath + DOWNLOAD_PATH);
			if (!file.exists()) // 원하는 경로에 폴더가 있는지 확인
				file.mkdirs();
			mDbPath = dirPath + DB_PATH;
			new SqlLiteAdapter(this, mDbPath); // db 생성.( 싱글톤)
		}
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
	}

	public void onSlide(View v) {
		mDrawerFragment.onSlide();
	}

	public void onMaintence(View v) {
		mDrawerFragment.onMaintence(FragmentController.TYPE_MAINTENANCE);
	}

	public void onFinish(View v) {
		// mDrawerFragment.onSlide();
		showEventPopup1("", "차량관리앱을 종료하시겠습니까?", new OnEventOkListener() {

			@Override
			public void onOk() {
				// TODO Auto-generated method stub
				Main_Activity.super.onBackPressed();
			}
		}, new OnEventCancelListener() {

			@Override
			public void onCancel() {
				// TODO Auto-generated method stub

			}
		});

	}
	
	
	public void onAutoCare(View v) {
		// mDrawerFragment.onSlide()
		
		   try {
		          // G5앱을 호출
		          Intent intent = this.getPackageManager().getLaunchIntentForPackage("com.lotte.autocare");
		          intent.setAction(Intent.ACTION_MAIN);
		          startActivity(intent);
		      }catch (NullPointerException e){
		    	  
		    	  
		      };

	}
	

	public void onStock(View v) {
		mDrawerFragment.onStock(FragmentController.TYPE_STOCK);
	}

	public void onSearch(View v) {
		mDrawerFragment.onSearch(FragmentController.TYPE_SEARCH);
	}

	public void onEtc(View v) {
		mDrawerFragment.onEtc(FragmentController.TYPE_ETC);
	}

	public void onTest(View v) {
		Intent in = new Intent(this, Menu1_Activity.class);
		in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
				| Intent.FLAG_ACTIVITY_SINGLE_TOP);
		startActivity(in);
	}

	public void onSetting(View v) {
		// // mSlidingFragment.onSlide();
		// final Rounds_Dialog dialog = new Rounds_Dialog(this);
		// Button dialogButton = (Button)
		// dialog.findViewById(R.id.rounds_save_id);
		// dialogButton.setOnClickListener(new View.OnClickListener() {
		// @Override
		// public void onClick(View v) {
		// Toast.makeText(Main_Activity.this,
		// dialog.getChoicedNum() + "선택된번호", 0).show();
		// dialog.dismiss();
		// }
		// });
		// dialog.show();
		mDrawerFragment.onSetting(FragmentController.TYPE_SETTING);
	}

	public void onHome(View v) {
		mDrawerFragment.onHome(FragmentController.TYPE_HOME);

	}

	

	public void onRounds(View v) {
		final Rounds_Dialog dialog = new Rounds_Dialog(this);
		Button dialogButton = (Button) dialog.findViewById(R.id.rounds_save_id);
		dialogButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Toast.makeText(Main_Activity.this,
						dialog.getChoicedNum() + "선택된번호", Toast.LENGTH_SHORT).show();
				dialog.dismiss();
			}
		});
		dialog.show();
	}

	@Override
	public void onAttachedToWindow() {
		// TODO Auto-generated method stub
		super.onAttachedToWindow();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

	}

	@Override
	protected void onDestroy() {
		stopEmptyService();
		SqlLiteAdapter.getInstance().closeDB();
		CommonUtil.unbindDrawables(mRootView);
		System.gc();
		//20131129 DELETE 미사용에따라 삭제
//		mHandler.removeCallbacks(mRunnable);
		super.onDestroy();
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		// super.onBackPressed();
		// myung 20131231 ADD 점검결과 등록 back버튼 클릭시 확인팝업창 뛰우기
		if(!DEFINE.RESIST_RESULT_FIRST_FLAG){
			final EventPopupCC ep1 = new EventPopupCC(this, "정비결과등록을 취소하시겠습니까?");
			Button bt_yes = (Button) ep1.findViewById(R.id.btn_ok);
			bt_yes.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					ep1.dismiss();
					mDrawerFragment.onBackPressed();
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
			return;
		}
		
		mDrawerFragment.onBackPressed();
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent event) {

		View v = getCurrentFocus();
		boolean ret = super.dispatchTouchEvent(event);

		if (v instanceof EditText) {
			View w = getCurrentFocus();
			int scrcoords[] = new int[2];
			w.getLocationOnScreen(scrcoords);
			float x = event.getRawX() + w.getLeft() - scrcoords[0];
			float y = event.getRawY() + w.getTop() - scrcoords[1];

			if (event.getAction() == MotionEvent.ACTION_UP
					&& (x < w.getLeft() || x >= w.getRight() || y < w.getTop() || y > w
							.getBottom())) {

				InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(getWindow().getCurrentFocus()
						.getWindowToken(), 0);
			}
		}
		return ret;
	}

	public void onMoveMaintenace(String moveDay, String progressType,
			String showText) {
		mDrawerFragment.onMoveMaintenave(moveDay, progressType, showText);
	}

	
	public void onMoveNotice() {
		mDrawerFragment.onMoveNotice();
	}

	
	
	public void showEventPopup1(String title, String body,
			OnEventOkListener onEventPopupListener,
			OnEventCancelListener onEventCancelListener) {
		EventPopup1 popup = new EventPopup1(this, body, onEventPopupListener);
		popup.setOnCancelListener(onEventCancelListener);
		popup.show();
	}
	
	
	
	public void startEmptyService() {
        emptyService = new Intent(this, EmptyService.class);
        startService(emptyService);
//	        PrintLog.print("startEmptyService", "startEmptyService");
    }

    public void stopEmptyService() {
    	stopService(emptyService);
//	        PrintLog.print("stopEmptyService", "stopEmptyService");
    }

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(childFragment != null){
			try {
				childFragment.onActivityResult(requestCode, resultCode, data);
			} catch (Exception e){
				e.printStackTrace();
			}
		}
//		mDrawerFragment.onActivityResult(requestCode, requestCode, data);
		LogUtil.d("TAG", "requestCode = " + requestCode);
		LogUtil.d("TAG", "resultCode = " + resultCode);
	}

	private Fragment childFragment = null;
	public void setFragment(Fragment fragment){
		childFragment = fragment;
	}

}
