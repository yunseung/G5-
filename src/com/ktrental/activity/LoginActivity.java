package com.ktrental.activity;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

import com.ktrental.R;
import com.ktrental.cm.connect.ConnectController;
import com.ktrental.cm.connect.Connector;
import com.ktrental.cm.connect.Connector.ConnectInterface;
import com.ktrental.cm.db.DbAsyncTask;
import com.ktrental.cm.db.DbAsyncTask.DbAsyncResLintener;
import com.ktrental.cm.db.SqlLiteAdapter;
import com.ktrental.common.DEFINE;
import com.ktrental.common.KtRentalApplication;
import com.ktrental.dialog.IoTRequestItemDialog;
import com.ktrental.dialog.TimePickDialog;
import com.ktrental.dialog.TroubleHistoryItemDialog;
import com.ktrental.model.DbQueryModel;
import com.ktrental.model.LoginModel;
import com.ktrental.model.O_ITAB1;
import com.ktrental.model.ResultSendModel;
import com.ktrental.model.TableModel;
import com.ktrental.popup.EventPopup2;
import com.ktrental.popup.EventPopupC;
import com.ktrental.popup.EventPopupCC;
import com.ktrental.popup.ProgressPopup;
import com.ktrental.popup.TimePopup;
import com.ktrental.product.Menu1_Activity;
import com.ktrental.util.CommonUtil;
import com.ktrental.util.LogUtil;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Set;

/**
 * 로그인 Activity <br/>
 * 로그인화면이다. 현재 RFCController ({@link ConnectController})을 이용하여 로그인을 수행한다.
 *
 * @author Hong
 * @since 2013.07.12
 */
public class LoginActivity extends Activity implements OnClickListener, OnCheckedChangeListener, ConnectInterface, DbAsyncResLintener
{

    private ConnectController     mConnectController;
    private SharedPreferencesUtil mPreferencesUtil;
    private static final String   DB_PATH       = "DATABASE";
    private static final String   DOWNLOAD_PATH = "DOWNLOAD";
    private Button                mLoginButton;
    private EditText              mIdEditText;
    private EditText              mPwEditText;
    private CheckBox              mSaveCheckBox;
    private String                mDbPath;
    private boolean               mCheckFlag    = false;
    private ProgressPopup         mProgressDialog;
    private TableModel            mCarMasterModel;
    private LinearLayout          mRootLayout;
    public static String          mPermgp       = "";
    private TextView              mTvAppVersion;

    private boolean               rooting;

    Context       mContext;
    private String OpenActivity = "";


    public static String session = "";

    // 2014-01-19 KDH 오늘날짜인 경우에는 작업화면에서 이값을 TRUE로넘겨주고 오늘일짜 팝업띄워준다.
    private boolean               isToday;
    private ProgressPopup         pp;
    {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);

        mContext = this;
//        DEFINE.SESSION = fnRandom();

        initConnect();

        // 2014-01-23 KDH 환장하것네 최초 실행시 무조건 DB 떨군다..프로바이더 쓰면 될꺼를 개고생해서 만들었네 -_- 덕분에 나만 개고생-ㅇ-
        final SharedPreferencesUtil sharedPreferencesUtil = SharedPreferencesUtil.getInstance();
        DEFINE.SESSION = sharedPreferencesUtil.getSession();
        if("".equals(DEFINE.SESSION))
        {
            sharedPreferencesUtil.setSession(fnRandom());
        }


        final GPS gps = new GPS(mContext);
        gps.startLocationReceiving();


        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("HHmmss", Locale.KOREA);
        sharedPreferencesUtil.setEndTime(format.format(date));



        mRootLayout = (LinearLayout) findViewById(R.id.ll_root);
        pp = new ProgressPopup(this);
        // StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
        // .detectDiskReads().detectDiskWrites().detectNetwork()
        // .penaltyLog().build());

        initViewSettings();




        Bundle b = getIntent().getExtras();
        if(b!=null){
            String id = b.getString("LOTTE_AUTOCARE_ID");
            // and any other data that the other app sent
            kog.e("Jonathan", "LOTTE_AUTOCARE_ID :: " + id);
            mIdEditText.setText(id);
        }
//        else {
//            mIdEditText.setFocusable(true);
//        }
//        mIdEditText.setText("R70040");
//        mIdEditText.setText("R70049");
//        mIdEditText.setText("R70063");
//        mIdEditText.setText("R70036");
//        mIdEditText.setText("R70276");
//        mIdEditText.setText("R71397");
//        mIdEditText.setText("R70131");



        //
        // // runUiThread(UiRunnable.MODE_PROGRESS_SHOW, "");
        // Thread thread = new Thread(new Runnable() {
        //
        // @Override
        // public void run() {
        // // TODO Auto-generated method stub
        //
        // // runUiThread(UiRunnable.MODE_PROGRESS_HIDE, "");
        // // loginSuccess();
        //
        // // Log.i("####", "####버전체크 온크리에잇");
        //
        // }
        // });

        // 루팅체크
        rooting = isRooted();
        kog.e("Jonathan", "rooting :: " + rooting);

        if (rooting)
        {// 루팅되어 있음

            final EventPopupC epc = new EventPopupC(this);
            Button bt_done = (Button) epc.findViewById(R.id.btn_ok);
            bt_done.setOnClickListener(new OnClickListener()
            {

                @Override
                public void onClick(View arg0)
                {
                    epc.dismiss();
                    finish();
                    android.os.Process.killProcess(android.os.Process.myPid());
                    System.exit(0);
                }
            });
            epc.show("디바이스가 루팅되어 있습니다.");

        }

        // 무결성

        // PackageManager pm = this.getPackageManager();
        // PackageInfo info = null;
        // try {
        // info = pm.getPackageInfo( this.getPackageName(), PackageManager.GET_SIGNATURES );
        //
        // kog.e("Jonathan", "packageinfo :: " + info.signatures[ 0 ].toCharsString());
        //
        // } catch (NameNotFoundException e) {
        // // TODO Auto-generated catch block
        // e.printStackTrace();
        // }
        //
        // if ( info.signatures[ 0 ].toCharsString().equals( "" ) )
        // {
        // //signature is OK
        // }

        initDb();
        dropTables();
        if (sharedPreferencesUtil.getFIRST_START())
        {
            mPreferencesUtil.setSyncSuccess(false);
            dropTablesWithRepairResult(new DbAsyncResLintener()
            {

                @Override
                public void onCompleteDB(String funName, int type, Cursor cursor, String tableName)
                {
                    // TODO Auto-generated method stub
                    sharedPreferencesUtil.setFIRST_START(false);
                    mProgressDialog.hide();
                }
            });
        }

        // / 체크
        // integrityVerCheck();

        checkFirstMonth();

        // 2017-11-16. 개발버전에선 아이디 입력 가능하도록 변경하자
        if(DEFINE.getDEBUG_MODE() == true && mIdEditText != null && mIdEditText.getText().toString().equals("")){
                mIdEditText.setEnabled(true);
                mLoginButton.setVisibility(View.VISIBLE);
        } else {
            startVerCheck();
        }


//        compareVer("1","1");



        // myung 20140102 ADD 로그인 시 미전송 DB 의 데이터 중 달이 다른 데이터 삭제 필요.
        // 2014-01-16 KDH 1. 매일 최초 로그인 성공 후 미전송 내역 전송 -> 전송 결과 성공/실패에 관계없이 미전송 DB 삭제 처리
        deleteRowResultDataAnotherMonth();

        Display dis = getWindowManager().getDefaultDisplay();
        DEFINE.DISPLAY = "" + dis.getWidth();

        // 2014-01-16 KDH 미전송 데이터 테스트용.전송실패를 강제로 해줘야되네-_-;
        // 2014-01-20 KDH 정책변경으로 인하여 주석.
        // queryBaseGroup();

        // 2014-01-23 KDH 처음시작했냐-ㅇ- 그럼 떨궈라./
    }


    private void initDb()
    {
        SqlLiteAdapter.getInstance().closeDB();
        if (CommonUtil.isValidSDCard())
        { // sd card 사용가능 여
            File root = this.getExternalCacheDir();
            String dirPath = root.getPath() + "/";
            File file = new File(dirPath + DOWNLOAD_PATH);
            if (!file.exists()) // 원하는 경로에 폴더가 있는지 확인
                file.mkdirs();
            mDbPath = dirPath + DB_PATH;
            new SqlLiteAdapter(this, mDbPath); // db 생성.( 싱글톤)
        }
    }

    private static boolean isRooted()
    {
        return findBinary("su");
    }

    public static boolean findBinary(String binaryName)
    {
        boolean found = false;
        if (!found)
        {
            String[] places = {
                    "/sbin/", "/system/bin/", "/system/xbin/", "/data/local/xbin/", "/data/local/bin/", "/system/sd/xbin/", "/system/bin/failsafe/",
                    "/data/local/" };

            for (String where : places)
            {
                if (new File(where + binaryName).exists())
                {
                    found = true;
                    break;
                }
            }
        }

        kog.e("Jonathan", "rooting1 :: " + String.valueOf(found));
        return found;
    }

    private void dropTables()
    {
        TableModel tableModel = new TableModel("");
        ArrayList<String> dropTables = new ArrayList<String>();
        // dropTables.add(DEFINE.O_ITAB1_TABLE_NAME);

        // dropTables.add(DEFINE.REPAIR_TABLE_NAME);

        dropTables.add(DEFINE.LOGIN_TABLE_NAME);
        dropTables.add(DEFINE.ADDRESS_TABLE);
        dropTables.add(DEFINE.CAR_MASTER_TABLE_NAME);

        DbAsyncTask asyncTask = new DbAsyncTask(ConnectController.REPAIR_FUNTION_NAME, O_ITAB1.TABLENAME, LoginActivity.this, LoginActivity.this, // DbAsyncResListener
                tableModel, dropTables);
        asyncTask.execute(DbAsyncTask.DB_DROP_TABLES);
    }

    private void initViewSettings()
    {
        pp.show();

        mLoginButton = (Button) findViewById(R.id.bt_login);
        mIdEditText = (EditText) findViewById(R.id.et_id);
        mPwEditText = (EditText) findViewById(R.id.et_pw);
        mSaveCheckBox = (CheckBox) findViewById(R.id.cb_save);
        mSaveCheckBox.setOnCheckedChangeListener(this);
        // mPwEditText.setText("123");
        mLoginButton.setOnClickListener(this);
        mProgressDialog = new ProgressPopup(this);
        mProgressDialog.setMessage("연결중입니다.");
        WindowManager.LayoutParams lp = mProgressDialog.getWindow().getAttributes();
        // WindowManager.LayoutParams lp =
        // getDialog().getWindow().getAttributes();
        lp.dimAmount = 0.6f;
        mProgressDialog.getWindow().setAttributes(lp);
        mProgressDialog.setCanceledOnTouchOutside(false);
        String id = SharedPreferencesUtil.getInstance().getId();

        mIdEditText.setEnabled(false);
        mLoginButton.setVisibility(View.GONE);
        mSaveCheckBox.setVisibility(View.GONE);
        mPwEditText.setVisibility(View.GONE);

//        if (!id.equals("-1"))
//        {
//            mIdEditText.setText(id);
//            mSaveCheckBox.setChecked(true);
//            // clickLogin();
//        }

        // mIdEditText.setText("R70178");
        // mIdEditText.setText("R81145");
        // mIdEditText.setText("R70126");

        mPwEditText.setOnEditorActionListener(new OnEditorActionListener()
        {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event)
            {
                if (actionId == EditorInfo.IME_ACTION_DONE)
                {
                    clickLogin();
                    return false;
                }
                else
                {
                    return false;
                }
            }
        });

        // myung 20131121 ADD 포커스 상실 시에 ID 입력 시 알파벳은 대문자로 변경 필요.
        mIdEditText.setOnFocusChangeListener(new OnFocusChangeListener()
        {

            @Override
            public void onFocusChange(View v, boolean hasFocus)
            {
                // TODO Auto-generated method stub
                if (hasFocus == false)
                {
                    String text = mIdEditText.getText().toString();
                    text = text.toUpperCase();// 모두 대문자로
                    mIdEditText.setText(text);
                }
            }
        });

//        if(DEFINE.SAP_SERVER_IP.equals("10.220.64.134")) // 개발
//        {
//            mTvAppVersion = (TextView) findViewById(R.id.tv_app_version);
//            mTvAppVersion.setText("Copyright ⓒ 2013 LOTTE RENTAL Co.," + "Ltd. All rights reserved. (개발 App Ver: " + getAppVer() + ")");
//        }
//        else	//운영
//        {
//            mTvAppVersion = (TextView) findViewById(R.id.tv_app_version);
//            mTvAppVersion.setText("Copyright ⓒ 2013 LOTTE RENTAL Co.," + "Ltd. All rights reserved. (App Ver: " + getAppVer() + ")");
//        }
        /**
         * 2017.05.23. L Cloud 개발기
         */
        if(DEFINE.SAP_SERVER_IP.equals("10.106.7.13")) // 개발
        {
            mTvAppVersion = (TextView) findViewById(R.id.tv_app_version);
            mTvAppVersion.setText("Copyright ⓒ 2013 LOTTE RENTAL Co.," + "Ltd. All rights reserved. (개발 App Ver: " + getAppVer() + ")");
        }
        else	//운영
        {
            mTvAppVersion = (TextView) findViewById(R.id.tv_app_version);
            mTvAppVersion.setText("Copyright ⓒ 2013 LOTTE RENTAL Co.," + "Ltd. All rights reserved. (App Ver: " + getAppVer() + ")");
        }

        if (kog.SHOW_LOGS)
        {
            // mPwEditText.setText("qwer1234!");
            // mPwEditText.setText("rb26dett!!");
            // mPwEditText.setText("123");
            // mPwEditText.setText("1234qwer!");

        }


        if(pp != null)
            pp.hide();

    }

    private void initConnect()
    {
        kog.e("Jonathan", "initConnect");
        mConnectController = new ConnectController(this, this);
        mPreferencesUtil = new SharedPreferencesUtil(this);
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            // 로그인 클릭 이벤
            case R.id.bt_login:
                clickLogin();
//                IoTCancelPopup popup = new IoTCancelPopup(LoginActivity.this);
//                popup.show();

//                TimePickDialog popup = new TimePickDialog(LoginActivity.this);
//                popup.show();

//                IoTRequestItemDialog popup = new IoTRequestItemDialog(LoginActivity.this);
//                popup.show();

//                TroubleHistoryItemDialog popup = new TroubleHistoryItemDialog(LoginActivity.this);
//                popup.show();

                break;
            default:
                break;
        }
    }

    private void clickLogin()
    {
        // myung 20131121 DELETE
        // String text = mIdEditText.getText().toString();
        // text = text.toUpperCase();// 모두 대문자로
        // mIdEditText.setText(text);
        if (isValidText(mIdEditText))
        { // id 미입력 확인

            //160808 롯데오토케어에서 로그인을 바로 할 수 있게. 아이디만 체크한다. 비번은 체크 안한다.
            String id = mIdEditText.getText().toString().toUpperCase();
            String pw = "1";

            final SharedPreferencesUtil sharedPreferencesUtil = SharedPreferencesUtil.getInstance();
            DEFINE.SESSION = sharedPreferencesUtil.getSession();
            if (mCheckFlag)
                SharedPreferencesUtil.getInstance().setId(id);
            else
                SharedPreferencesUtil.getInstance().setId("-1");

            runUiThread(UiRunnable.MODE_PROGRESS_SHOW, "로그인 정보를 확인 중입니다.");
            // Log.d("HONG", "id " + id);
            // 로그인 시도.
            mConnectController.logIn(id, pw, DEFINE.SESSION, this);





//            if (isValidText(mPwEditText))
//            { // pw 미입력 확인
//              // 둘다 입력된 값이 있음.
//                String id = mIdEditText.getText().toString();
//                String pw = mPwEditText.getText().toString();
//                if (mCheckFlag)
//                    SharedPreferencesUtil.getInstance().setId(id);
//                else
//                    SharedPreferencesUtil.getInstance().setId("-1");
//
//                runUiThread(UiRunnable.MODE_PROGRESS_SHOW, "로그인 정보를 확인 중입니다.");
//                // Log.d("HONG", "id " + id);
//                // 로그인 시도.
//                mConnectController.logIn(id, pw, this);
//            }
//            else
//            {
//                // Toast.makeText(this, "비밀번호를 입력해주세요.", Toast.LENGTH_SHORT)
//                // .show();
//                EventPopup2 eventPopup = new EventPopup2(LoginActivity.this, "" + "비밀번호를 입력해 주세요.", null);
//                eventPopup.show();
//            }


        }
        else
        {
            //160808 롯데오토케어에서만 로그인 할 수 있다. 아이디 안보내부면 앱 죽게 해야 함.
//            EventPopup2 eventPopup = new EventPopup2(LoginActivity.this, "" + "id를 입력해 주세요.", null);
//            eventPopup.show();

            if(DEFINE.getDEBUG_MODE()){
                //nothing
            } else {
                finish();
                android.os.Process.killProcess(android.os.Process.myPid());
                System.exit(0);
            }

        }
    }

    public String fnRandom()
    {
        String nansu = "";
        for(int i = 0 ; i < 21 ; i++)
        {
            if(Math.floor(Math.random() * 100) + 10 > 50 )
            {
                nansu += Character.toString((char) ((Math.random() * 26) + 65));
            }
            else
            {
                nansu += Character.toString((char) ((Math.random() * 26) + 97));
            }
        }


        return nansu;
    }



    private boolean isValidText(EditText et)
    {
        boolean reValid = false;
        String id = et.getText().toString();
        // 2014-01-14 et null체크는 위에서부터 해야하지않나..-.-환장하긋네
        if (id != null)
        {
            if (id.length() > 0)
            {
                reValid = true;
            }
        }
        return reValid;
    }

    // 서버 연동 결과 이벤트
    @Override
    public void connectResponse(String FunName, final String resultText, String MTYPE, int resultCode, TableModel tableModel)
    {

        kog.e("Jonathan", "Login result :: " + resultText + "  " + resultCode);

        runUiThread(UiRunnable.MODE_PROGRESS_HIDE, "");

        if (FunName.equals("ZMO_1010_RD01"))
        {
            if (MTYPE == null || !MTYPE.equals("S"))
            {
                final EventPopupC epc = new EventPopupC(this);
                Button btn_confirm = (Button)epc.findViewById(R.id.btn_ok);
                btn_confirm.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View arg0) {
                        // TODO Auto-generated method stub
                        epc.dismiss();
                        finish();
                        android.os.Process.killProcess(android.os.Process.myPid());
                        System.exit(0);


                    }
                });
                epc.show(resultText);

                return;
            }

        }


        if("ZMO_1010_RD02".equals(FunName))
        {
            if (MTYPE == null || !MTYPE.equals("S")) {

                final EventPopupC epc = new EventPopupC(this);
                Button btn_confirm = (Button)epc.findViewById(R.id.btn_ok);
                btn_confirm.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View arg0) {
                        // TODO Auto-generated method stub
                        epc.dismiss();
                        finish();
                        android.os.Process.killProcess(android.os.Process.myPid());
                        System.exit(0);

                    }
                });
                epc.show("세션정보 조회에 실패했습니다. 앱을 종료 합니다.");
            }

            HashMap<String, String> ES_LIST = new HashMap<String, String>();
            if(tableModel != null) {
                ES_LIST = tableModel.getStruct("ES_LIST");
            }

            if(ES_LIST != null)
            {

                if("X".equals(ES_LIST.get("LOG_CK")) || "LINK".equals(ES_LIST.get("SES_ID")))
                {
                    // 중복허용인 사람

                    if("X".equals(ES_LIST.get("LOG_CK")))
                    {
                        mConnectController.updateSession(this, "IN", DEFINE.SESSION);
                    }
                    else if("LINK".equals(ES_LIST.get("SES_ID")))
                    {
                        mConnectController.updateSession(this, "IN", "LINK");
                    }


                }
                else
                {
                    // 일반적인 사람
                    String FSP_DAT = ES_LIST.get("FSP_DAT");
                    String FSP_TIM = ES_LIST.get("FSP_TIM");
                    String FNL_DAT = ES_LIST.get("FNL_DAT");
                    String FNL_TIM = ES_LIST.get("FNL_TIM");
                    String FSP = null;
                    String FNL = null;
                    if(FSP_DAT != null && FSP_TIM != null){
                        FSP = ES_LIST.get("FSP_DAT").replaceAll("-", "") + " " +  ES_LIST.get("FSP_TIM").replaceAll(":", "");
                    }
                    if(FNL_DAT != null && FNL_TIM != null){
                        FNL = ES_LIST.get("FNL_DAT").replaceAll("-", "") + " " +  ES_LIST.get("FNL_TIM").replaceAll(":", "");
                    }
                    String SES_ID = ES_LIST.get("SES_ID");
                    Calendar cal = Calendar.getInstance();

                    try
                    {
                        SimpleDateFormat reFormat = new SimpleDateFormat("yyyyMMdd hhmmss");
                        Date d = cal.getTime();
                        Date dtStart = d;
                        try {
                            if(FNL != null) {
                                dtStart = reFormat.parse(FNL);
                            }
                        } catch (Exception e){
                            e.printStackTrace();
                        }
                        Date dtEnd = d;
                        try {
                            if(FSP != null){
                                dtEnd = reFormat.parse(FSP);
                            }
                        } catch (Exception e){
                            e.printStackTrace();
                        }


                        if (dtStart.before(dtEnd))
                        {
                            // 정상 로그아웃 되있을떄 세션정보 업데이트 치고 진행
                            mConnectController.updateSession(this, "IN", DEFINE.SESSION);
//                            if("M".equals(OpenActivity))
//                            {
//                                openMainActivity();
//                            }
//                            else if("M1".equals(OpenActivity))
//                            {
//                                openMenu1_Activity();
//                            }

                        }
                        else
                        {
                            // 정상적인 로그아웃 안함. 그리고 누군가 쓰고있는거임.
                            //지금 로그인 할때 만들었던 세션과 비교 -> 같으면 나인거니까 진행 -> 같지 않으면 이미 접속한 사람이 있습니다. 강제종료하고 다시 하십시요.
                            if(DEFINE.SESSION.equals(SES_ID))
                            {
                                //로그인 할때 만들었던 세션과 비교 -> 같은경우 -> 업데이트하고 그냥 진행.
                                kog.e("Joanthan", "11 DEFINE.SESSION :: " + DEFINE.SESSION + "  sesstion :: " + SES_ID);
                                mConnectController.updateSession(this, "IN", DEFINE.SESSION);
//                                if("M".equals(OpenActivity))
//                                {
//                                    openMainActivity();
//                                }
//                                else if("M1".equals(OpenActivity))
//                                {
//                                    openMenu1_Activity();
//                                }


                            }
                            else
                            {
                                kog.e("Joanthan", "22 DEFINE.SESSION :: " + DEFINE.SESSION + "  sesstion :: " + SES_ID);

                                final EventPopupCC ep1 = new EventPopupCC(this, "이미 접속한 사용자가 있습니다. 강제종료하고 다시 로그인 하시겠습니까?");
                                Button bt_yes = (Button) ep1.findViewById(R.id.btn_ok);
                                bt_yes.setOnClickListener(new OnClickListener() {
                                    @Override
                                    public void onClick(View arg0) {

                                        mConnectController.updateSession(mContext, "IN", DEFINE.SESSION);
//                                        if("M".equals(OpenActivity))
//                                        {
//                                            openMainActivity();
//                                        }
//                                        else if("M1".equals(OpenActivity))
//                                        {
//                                            openMenu1_Activity();
//                                        }


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

                        }


                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                }

            }
        }



        if(FunName.equals("ZMO_1010_WR02"))
        {
            kog.e("Jonathan","1111");
            if (MTYPE == null || !MTYPE.equals("S")) {

                final EventPopupC epc = new EventPopupC(this);
                Button btn_confirm = (Button)epc.findViewById(R.id.btn_ok);
                btn_confirm.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View arg0) {
                        // TODO Auto-generated method stub
                        epc.dismiss();
                        android.os.Process.killProcess(android.os.Process.myPid());
                        System.exit(0);

                    }
                });
                epc.show(resultText);
            }


            if("M".equals(OpenActivity))
            {
                openMainActivity();
            }
            else if("M1".equals(OpenActivity))
            {
                openMenu1_Activity();
            }


        }



        switch (resultCode)
        {
            // 성공
            case Connector.D_EXECUTE_SUCCESS:
                successedExecute(FunName, tableModel);

                break;
            // 실패
            case Connector.D_EXECUTE_ERROR:
                failedExecute(FunName, resultText);
                break;
            // 테이블 성공
            case Connector.D_TABLE_EXECUTE_SUCCESS:
                successedExecuteTable(FunName, tableModel);

                break;

            case Connector.D_EXECUTE_NETWORk_ERROR:
                runUiThread(UiRunnable.MODE_PROGRESS_HIDE, "");
                Toast.makeText(this, resultText, Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }

        if (MTYPE.equals("E"))
        {
            runUiThread(UiRunnable.MODE_PROGRESS_HIDE, "");
            runOnUiThread(new Runnable()
            {

                @Override
                public void run()
                {


                    final EventPopupC epc = new EventPopupC(LoginActivity.this);
                    Button btn_confirm = (Button)epc.findViewById(R.id.btn_ok);
                    btn_confirm.setOnClickListener(new OnClickListener() {

                        @Override
                        public void onClick(View arg0) {
                            // TODO Auto-generated method stub
                            epc.dismiss();
                            finish();
                            android.os.Process.killProcess(android.os.Process.myPid());
                            System.exit(0);


                        }
                    });
                    epc.show(resultText);


//                    // TODO Auto-generated method stub
//                    final EventPopup2 eventPopup = new EventPopup2(LoginActivity.this, "" + resultText, null);
//                    Button bt_done = (Button)eventPopup.findViewById(R.id.btn_ok);
//                    bt_done.setOnClickListener(new OnClickListener()
//                    {
//
//                        @Override
//                        public void onClick(View arg0)
//                        {
//                        	eventPopup.dismiss();
//                            finish();
//                        }
//                    });
//                    eventPopup.show();

                }
            });
        }
    }

    private void successedExecute(String FunName, TableModel tableModel)
    {
        if (FunName.equals(ConnectController.COMMONCHECK_FUNTION_NAME))
        {
            // loginSuccess();
        }
    }

    private void successedExecuteTable(String FunName, final TableModel tableModel)
    {

        kog.e("KDH", "successedExecuteTable = " + FunName);

        if (FunName.equals(ConnectController.REPAIR_FUNTION_NAME))
        {
            // myung 20131119 DELETE
            // pp.dismiss();

            kog.e("Jonathan", "repair도 들어와??");
            runOnUiThread(new Runnable()
            {

                @Override
                public void run()
                {
                    HashMap<String, String> loginTableNameMap = new HashMap<String, String>();
                    loginTableNameMap.put("O_ITAB1", ConnectController.REPAIR_TABLE_NAME);
                    loginTableNameMap.put("O_ITAB2", DEFINE.STOCK_TABLE_NAME);
                    tableModel.setTableName(ConnectController.REPAIR_TABLE_NAME);
                    loginTableNameMap.put("O_ITAB3", DEFINE.REPAIR_LAST_TABLE_NAME);
                    loginTableNameMap.put("O_ITAB4", DEFINE.REPAIR_LAST_DETAIL_TABLE_NAME);
                    DbAsyncTask asyncTask = new DbAsyncTask(ConnectController.REPAIR_FUNTION_NAME, O_ITAB1.TABLENAME, LoginActivity.this,
                            LoginActivity.this, // DbAsyncResListener
                            tableModel, loginTableNameMap);
                    asyncTask.execute(DbAsyncTask.DB_ARRAY_INSERT);

                }
            });
            // schneider 2013.11.11
            SharedPreferencesUtil sharedPreferencesUtil = SharedPreferencesUtil.getInstance();
            sharedPreferencesUtil.setSyncSuccess(true);
            // schneider 2013.11.11
        }
        else if (FunName.equals(ConnectController.LOGIN_FUNTION_NAME))
        {

            kog.e("KDH", "LOGIN_FUNTION_NAME ");

            HashMap<String, String> structTableNames = new HashMap<String, String>();
            mCarMasterModel = tableModel;
            HashMap<String, String> struct = tableModel.getStruct("O_STRUCT1");
            if (struct != null)
            {
                struct.put("SCRNO", " ");
                struct.put("NO_MASK", " ");
                struct.put("SYSIP", DEFINE.MW_HOST_IP);
                mPermgp = struct.get("PERMGP");
                String INVNR = struct.get("INVNR");
                LoginModel lm = new LoginModel(struct);
                lm.setInvnr(INVNR);
                KtRentalApplication.setSetLoginModel(lm);
                kog.e("KDH", "ConnectController.LOGIN_FUNTION_NAME");
            }

            // kog.e("Jonatha","tableModel.getTableName() :: " + tableModel.getStruct("O_STRUCT2"));
            Set<String> set = tableModel.getStruct("O_STRUCT1").keySet();
            Iterator<String> it = set.iterator();
            String key;

            while (it.hasNext())
            {
                key = it.next();
                kog.e("Jonathan", "login_function_1 key ===  " + key + "    value  === " + tableModel.getStruct("O_STRUCT1").get(key));
            }

            structTableNames.put(DEFINE.LOGIN_TABLE_NAME, "O_STRUCT1");
            tableModel.setStructTableName(structTableNames);

            kog.e("KDH", "미전송 발송");

            // Jonathan 14.09.03 로그인시 미전송건 전송하지 않기.
            downloadDB(); // db 파일 변경을 위해 나중에 풀어주어야한다.

            // if(kog.TEST)
            // {
            // downloadDB(); // db 파일 변경을 위해 나중에 풀어주어야한다.
            // }
            // else
            // {
            // ResultController resultController = new ResultController(this,
            // new OnResultCompleate() {
            //
            //
            // @Override
            // public void onResultComplete(String message) {
            // downloadDB(); // db 파일 변경을 위해 나중에 풀어주어야한다.
            // // reDownloadDB("");
            // tableModel.setTableName(DEFINE.STOCK_TABLE_NAME);
            // // runUiThread(UiRunnable.MODE_PROGRESS_SHOW,
            // // message);
            // }
            // });
            // resultController.queryBaseGroup(false);
            // }

            // downloadDB(); // db 파일 변경을 위해 나중에 풀어주어야한다.
            // // reDownloadDB("");
            // tableModel.setTableName(DEFINE.STOCK_TABLE_NAME);
            // runUiThread(UiRunnable.MODE_PROGRESS_SHOW,
            // message);
        }

    }

    private void failedExecute(String FunName, String strText)
    {
        if (FunName.equals(ConnectController.LOGIN_FUNTION_NAME))
        {
            kog.T(getApplicationContext(), "failedExecute FAIL");
        }
        else if (FunName.equals(ConnectController.COMMONCHECK_FUNTION_NAME))
        {

        }
        else if (ConnectController.REPAIR_FUNTION_NAME.equals(FunName))
        {
            // 2014-01-16 KDH 미전송 실패 할 경우 인데 오늘 최초 로그인 할 경우에만 해줘야하남?
            // 미전송 데이터의 날짜와 오늘 날짜를 비교가 된다면, 오늘날짜와 미전송 데이터 날짜만 비교해서 다르면, 삭제해주고 팝업을 띄워준다.
            // 테이블 조회 후 몇건의 데이터가 있는지 확인해보자.
            kog.e("KDH", "REPAIR_FUNTION_NAME FAIL");

        }
        runUiThread(UiRunnable.MODE_PROGRESS_HIDE, "");

    }


    private void openMainActivity()
    {
        Intent intent = new Intent(this, Main_Activity.class);
        intent.putExtra("today", isToday);
        startActivity(intent);

        if (pp != null)
        {
            if(pp.isShowing()) {
                pp.dismiss();
            }
        }
        finish();
    }


    private void openMenu1_Activity()
    {
        Intent in = new Intent(this, Menu1_Activity.class);
        in.putExtra("is_CarManager", mPermgp);
        in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(in);
        finish();
    }


    private void loginSuccess()
    {
        kog.e("Jonathan", "로그인 success()");

        runUiThread(UiRunnable.MODE_PROGRESS_HIDE, "");


        //177777
        OpenActivity = "M";
        mConnectController.duplicateLogin(this);

//        Intent intent = new Intent(this, Main_Activity.class);
//        intent.putExtra("today", isToday);
//        startActivity(intent);
//
//        if (pp != null)
//        {
//            pp.dismiss();
//        }
//        finish();


    }

    private void downloadDB()
    {
        if (CommonUtil.isValidSDCard())
        { // sd card 사용가능 여
            File root = this.getExternalCacheDir();
            String dirPath = root.getPath() + "/";
            File file = new File(dirPath + DOWNLOAD_PATH);
            if (!file.exists()) // 원하는 경로에 폴더가 있는지 확인
                file.mkdirs();
            mDbPath = dirPath + DB_PATH;
            kog.e("Jonathan", "downloadDB::" + mDbPath);
            runUiThread(UiRunnable.MODE_PROGRESS_SHOW, "버전 정보를 확인 중 입니다.");
            mConnectController.downloadDB(mPreferencesUtil.getVersion(), file.getPath());
//            reDownloadDB("1.1.5.1");
            // new SqlLiteAdapter(this, mDbPath); // db 생성.( 싱글톤)
        }
    }

    @Override
    public void reDownloadDB(String newVersion)
    {
        File root = this.getExternalCacheDir();
        String dirPath = root.getPath() + "/" + DOWNLOAD_PATH + "/" + DEFINE.SQLLITE_DB_NAME;
        if (CommonUtil.isFile(dirPath))
            dbRoad(dirPath);
        else
            addLoginDataToDb();
    }

    private void dbRoad(final String downloadPath)
    {
        runOnUiThread(new Runnable()
        {

            @Override
            public void run()
            {
                // TODO Auto-generated method stub
                DbAsyncTask asyncTask = new DbAsyncTask("DB_ROAD", O_ITAB1.TABLENAME, LoginActivity.this, LoginActivity.this, // DbAsyncResListener
                        null, downloadPath);
                asyncTask.execute(DbAsyncTask.DB_ROAD);
            }
        });
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
    {
        switch (buttonView.getId())
        {
            case R.id.cb_save:
                checkChanged(isChecked);
                break;
            default:
                break;
        }
    }

    private void checkChanged(boolean isChecked)
    {
        if (isChecked)
        {
            // SharedPreferencesUtil.getInstance().setSaveIdFlag(true);
            mCheckFlag = true;
        }
        else
        {
            SharedPreferencesUtil sharedPreferencesUtil = SharedPreferencesUtil.getInstance();
            // SharedPreferencesUtil.getInstance().setSaveIdFlag(false);
            sharedPreferencesUtil.setSyncSuccess(false);
            mCheckFlag = false;
            dropAllTables(new DbAsyncResLintener()
            {

                @Override
                public void onCompleteDB(String funName, int type, Cursor cursor, String tableName)
                {
                    // TODO Auto-generated method stub
                    mProgressDialog.hide();
                }
            });
        }
    }

    private void dropAllTables(DbAsyncResLintener lintener)
    {
        mProgressDialog.show();
        TableModel tableModel = new TableModel("");
        ArrayList<String> dropTables = new ArrayList<String>();
        // dropTables.add(DEFINE.O_ITAB1_TABLE_NAME);
        dropTables.add(DEFINE.LOGIN_TABLE_NAME);
        dropTables.add(DEFINE.ADDRESS_TABLE);
        dropTables.add(DEFINE.CAR_MASTER_TABLE_NAME);
        dropTables.add(DEFINE.REPAIR_TABLE_NAME);
        dropTables.add(DEFINE.STOCK_TABLE_NAME);
        DbAsyncTask asyncTask = new DbAsyncTask(ConnectController.REPAIR_FUNTION_NAME, O_ITAB1.TABLENAME, LoginActivity.this, lintener, // DbAsyncResListener
                tableModel, dropTables);
        asyncTask.execute(DbAsyncTask.DB_DROP_TABLES);
    }

    // myung 20140102 ADD 로그인시 달이 바뀌는 경우, 미전송내역들은 DB에서 삭제한다.
    private void dropTablesWithRepairResult(DbAsyncResLintener lintener)
    {
        TableModel tableModel = new TableModel("");
        ArrayList<String> dropTables = new ArrayList<String>();
        dropTables.add(DEFINE.REPAIR_RESULT_BASE_TABLE_NAME);
        dropTables.add(DEFINE.REPAIR_RESULT_STOCK_TABLE_NAME);
        dropTables.add(DEFINE.REPAIR_RESULT_IMAGE_TABLE_NAME);
        DbAsyncTask asyncTask = new DbAsyncTask(ConnectController.REPAIR_FUNTION_NAME, O_ITAB1.TABLENAME, LoginActivity.this, lintener, // DbAsyncResListener
                tableModel, dropTables);
        asyncTask.execute(DbAsyncTask.DB_DROP_TABLES);
    }

    // myung 20140102 ADD 로그인시 달이 바뀌는 경우, 미전송내역들은 DB에서 삭제한다.
    private void dropTablesAnotherMonth(DbAsyncResLintener lintener)
    {
        TableModel tableModel = new TableModel("");
        ArrayList<String> dropTables = new ArrayList<String>();
        dropTables.add(DEFINE.REPAIR_RESULT_BASE_TABLE_NAME);
        dropTables.add(DEFINE.REPAIR_RESULT_STOCK_TABLE_NAME);
        dropTables.add(DEFINE.REPAIR_RESULT_IMAGE_TABLE_NAME);
        DbAsyncTask asyncTask = new DbAsyncTask(ConnectController.REPAIR_FUNTION_NAME, O_ITAB1.TABLENAME, LoginActivity.this, lintener, // DbAsyncResListener
                tableModel, dropTables);
        asyncTask.execute(DbAsyncTask.DB_DROP_TABLES);
    }

    private void dropRepairTables(DbAsyncResLintener lintener)
    {
        TableModel tableModel = new TableModel("");
        ArrayList<String> dropTables = new ArrayList<String>();
        // dropTables.add(DEFINE.O_ITAB1_TABLE_NAME);
        dropTables.add(DEFINE.ADDRESS_TABLE);
        dropTables.add(DEFINE.REPAIR_TABLE_NAME);
        dropTables.add(DEFINE.STOCK_TABLE_NAME);
        DbAsyncTask asyncTask = new DbAsyncTask(ConnectController.REPAIR_FUNTION_NAME, O_ITAB1.TABLENAME, LoginActivity.this, lintener, // DbAsyncResListener
                tableModel, dropTables);
        asyncTask.execute(DbAsyncTask.DB_DROP_TABLES);
    }

    @Override
    public void onCompleteDB(String funName, int type, Cursor cursor, String tableName)
    {
        if (type == DbAsyncTask.DB_ROAD)
        {
            addLoginDataToDb();
        }
        else if (type == DbAsyncTask.DB_ARRAY_INSERT)
        {
            if (funName.equals("LOGIN_END"))
            {
                SharedPreferencesUtil sharedPreferencesUtil = SharedPreferencesUtil.getInstance();
                sharedPreferencesUtil.setLoginType(mPermgp);

                runUiThread(UiRunnable.MODE_PROGRESS_HIDE, "");
                if (mPermgp.equals("P1")) // 순회기사
                    // 순회 정비 계획을 받아온다.
                    repairPlanWork();
                else
                { // 카매니저

                    //177777
                    OpenActivity = "M1";
                    mConnectController.duplicateLogin(this);

//                    Intent in = new Intent(this, Menu1_Activity.class);
//                    in.putExtra("is_CarManager", mPermgp);
//                    in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
//                    startActivity(in);
//                    finish();
                }
                // loginSuccess();
            }
            else if (tableName.equals(ConnectController.REPAIR_TABLE_NAME))
            {
                runUiThread(UiRunnable.MODE_PROGRESS_HIDE, "");
                checkNeedTables();
            }
            //
        }
        else if (funName.equals("checkNeedTables"))
        {
            if (tableName.equals(""))
            { // 성공
                loginSuccess();
            }
            else
            { // 실패
                SharedPreferencesUtil sharedPreferencesUtil = new SharedPreferencesUtil(this);
                sharedPreferencesUtil.setSyncSuccess(false);
                EventPopup2 eventPopup = new EventPopup2(this, tableName + " 정보가 없어 일부 기능만 사용 가능합니다.", new OnEventOkListener()
                {

                    @Override
                    public void onOk()
                    {
                        // TODO Auto-generated method stub
                        // finish();
                        loginSuccess();
                    }
                });
                eventPopup.show();
            }
        }
        if (cursor != null)
            cursor.close();
    }

    private void addLoginDataToDb()
    {
        if (mCarMasterModel != null)
        {
            HashMap<String, String> loginTableNameMap = new HashMap<String, String>();
            loginTableNameMap.put("O_ITAB1", DEFINE.CAR_MASTER_TABLE_NAME);
            loginTableNameMap.put("O_ITAB2", DEFINE.ADDRESS_TABLE);
            TableModel tableModel = mCarMasterModel;

            kog.e("Jonathan", "O_ITAB1 123 :: " + tableModel.getValue());
            kog.e("Jonathan", "O_ITAB1 123 :: " + tableModel.getTableName());
            kog.e("Jonathan", "O_ITAB1 123 :: " + tableModel.toString());

            Set<String> set = loginTableNameMap.keySet();
            Iterator<String> it = set.iterator();
            String key;

            while (it.hasNext())
            {
                key = it.next();
                kog.e("Jonathan", "login_function_1 key ===  " + key + "    value  === " + loginTableNameMap.get(key));
            }

            DbAsyncTask asyncTask = new DbAsyncTask("LOGIN_END", O_ITAB1.TABLENAME, LoginActivity.this, LoginActivity.this, // DbAsyncResListener
                    tableModel, loginTableNameMap);
            asyncTask.execute(DbAsyncTask.DB_ARRAY_INSERT);
        }
    }

    private class UiRunnable implements Runnable
    {

        private final static int MODE_PROGRESS_SHOW = 0;
        private final static int MODE_PROGRESS_HIDE = 1;
        private int              mMode              = -1;
        private String           mMessage           = "";

        public UiRunnable(int mode, String message)
        {
            mMode = mode;
            mMessage = message;
        }

        @Override
        public void run()
        {
            // TODO Auto-generated method stub
            switch (mMode)
            {
                case MODE_PROGRESS_SHOW:
                    showProgress(true);
                    break;
                case MODE_PROGRESS_HIDE:
                    showProgress(false);
                    break;
                default:
                    break;
            }
        }

        private void showProgress(boolean isShow)
        {
            if (mProgressDialog != null)
            {
                mProgressDialog.setMessage(mMessage);
                if (isShow)
                    mProgressDialog.show();
                else
                    mProgressDialog.dismiss();
            }
        }
    }

    private void runUiThread(int UiRunnableMode, String message)
    {
        // runOnUiThread(new UiRunnable(UiRunnableMode));
        mRootLayout.post(new UiRunnable(UiRunnableMode, message));
    }

    private void repairPlanWork()
    {
        final SharedPreferencesUtil sharedPreferencesUtil = SharedPreferencesUtil.getInstance();
        // String SyncStr = " ";
        final LoginModel model = KtRentalApplication.getLoginModel();
        if (sharedPreferencesUtil.getLastLoginId().equals(model.getPernr()))
        {

            // myung 20131204 ADD 매월 첫쨋날 첫번째 로그인 시 I_SYNC 값을 "A" 로 보내도록 수정 필요.
            SharedPreferencesUtil shareU = SharedPreferencesUtil.getInstance();
            int nMonth = shareU.getInt("login_first_check_month", 0);

            GregorianCalendar today = new GregorianCalendar();
            int month = today.get(Calendar.MONTH) + 1;
            // Log.i("nMonth/todayMonth", nMonth+"/"+month);

            if (nMonth == 12 && month == 1)
                nMonth = 0;
                if (month > nMonth)
                {
                    shareU.setInt("login_first_check_month", month);

                    kog.e("Jonathan", "로그인1");
                    runUiThread(UiRunnable.MODE_PROGRESS_SHOW, "데이터 초기화");
                    mPreferencesUtil.setSyncSuccess(false);
                    dropRepairTables(new DbAsyncResLintener()
                    {

                        @Override
                        public void onCompleteDB(String funName, int type, Cursor cursor, String tableName)
                        {
                            // TODO Auto-generated method stub
                            runUiThread(UiRunnable.MODE_PROGRESS_HIDE, "데이터 초기화");
                            String SyncStr = "A";
                            sharedPreferencesUtil.setSyncSuccess(false);
                            pp.setMessage("순회정비 리스트를 확인 중 입니다.");
                            pp.show();
                            // runUiThread(UiRunnable.MODE_PROGRESS_SHOW,
                            // "순회정비 리스트를 확인 중 입니다.");
                            Log.d("HONG", "model.getPernr() " + model.getPernr());
                            // 테이블 데이타를 얻어온다.
                            mConnectController.getRepairPlan(model.getPernr(), SyncStr, LoginActivity.this);
                            sharedPreferencesUtil.setLastLoginId(model.getPernr());
                        }
                    });

                }
                else if (sharedPreferencesUtil.isSuccessSyncDB())
                {
                    kog.e("Jonathan", "로그인2");
                    String SyncStr = " ";
                    pp.setMessage("순회정비 리스트를 확인 중 입니다.");
                    pp.show();
                    // runUiThread(UiRunnable.MODE_PROGRESS_SHOW,
                    // "순회정비 리스트를 확인 중 입니다.");
                    // Log.d("HONG", "model.getPernr() " + model.getPernr());
                    // 테이블 데이타를 얻어온다.
                    mConnectController.getRepairPlan(model.getPernr(), SyncStr, this);
                    sharedPreferencesUtil.setLastLoginId(model.getPernr());

                }
                else
                {
                    kog.e("Jonathan", "로그인3");
                    String SyncStr = "A";
                    sharedPreferencesUtil.setSyncSuccess(false);
                    pp.setMessage("순회정비 리스트를 확인 중 입니다.");
                    pp.show();
                    // runUiThread(UiRunnable.MODE_PROGRESS_SHOW,
                    // "순회정비 리스트를 확인 중 입니다.");
                    // Log.d("HONG", "model.getPernr() " + model.getPernr());
                    // 테이블 데이타를 얻어온다.
                    mConnectController.getRepairPlan(model.getPernr(), SyncStr, this);
                    sharedPreferencesUtil.setLastLoginId(model.getPernr());
            }
        }
        else
        {
            kog.e("Jonathan", "로그인4");
            runUiThread(UiRunnable.MODE_PROGRESS_SHOW, "데이터 초기화");
            mPreferencesUtil.setSyncSuccess(false);
            dropRepairTables(new DbAsyncResLintener()
            {

                @Override
                public void onCompleteDB(String funName, int type, Cursor cursor, String tableName)
                {
                    // TODO Auto-generated method stub
                    runUiThread(UiRunnable.MODE_PROGRESS_HIDE, "데이터 초기화");
                    String SyncStr = "A";
                    sharedPreferencesUtil.setSyncSuccess(false);
                    pp.setMessage("순회정비 리스트를 확인 중 입니다.");
                    pp.show();
                    // runUiThread(UiRunnable.MODE_PROGRESS_SHOW,
                    // "순회정비 리스트를 확인 중 입니다.");
                    // Log.d("HONG", "model.getPernr() " + model.getPernr());
                    // 테이블 데이타를 얻어온다.
                    mConnectController.getRepairPlan(model.getPernr(), SyncStr, LoginActivity.this);
                    sharedPreferencesUtil.setLastLoginId(model.getPernr());
                }
            });
        }
    }

    @Override
    protected void onDestroy()
    {
        // SqlLiteAdapter.getInstance().closeDB();
        mProgressDialog = null;
        super.onDestroy();
    }

    // 필수 테이블들을 확인한다.
    private void checkNeedTables()
    {
        TableModel tableModel = new TableModel("");
        DbAsyncTask asyncTask = new DbAsyncTask("checkNeedTables", O_ITAB1.TABLENAME, LoginActivity.this, LoginActivity.this, // DbAsyncResListener
                tableModel, "");
        asyncTask.execute(DbAsyncTask.DB_TABLE_CHECK);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event)
    {
        View v = getCurrentFocus();
        boolean ret = super.dispatchTouchEvent(event);
        if (v instanceof EditText)
        {
            View w = getCurrentFocus();
            int scrcoords[] = new int[2];
            w.getLocationOnScreen(scrcoords);
            float x = event.getRawX() + w.getLeft() - scrcoords[0];
            float y = event.getRawY() + w.getTop() - scrcoords[1];
            if (event.getAction() == MotionEvent.ACTION_UP && (x < w.getLeft() || x >= w.getRight() || y < w.getTop() || y > w.getBottom()))
            {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getWindow().getCurrentFocus().getWindowToken(), 0);
            }
        }
        return ret;
    }

    private void startVerCheck()
    {
        pp.setMessage("버전체크중");
        pp.show();

        VersionAsync va = new VersionAsync(this)
        {

            @Override
            protected void onPostExecute(String result)
            {
                super.onPostExecute(result);
                Log.i("####", "####버전정보" + result.trim() + "/" + getAppVer());
                compareVer(result.trim(), getAppVer());
//                LogUtil.d("hjt", "new_ver == now_ver");
//                if(pp != null)
//                    pp.hide();
//                start();
//                clickLogin();
            }
        };
        va.execute();


    }

    // 무결성 점검.
    private void integrityVerCheck()
    {
        IntegrityAsync ia = new IntegrityAsync(this)
        {

            @Override
            protected void onPostExecute(String result)
            {
                super.onPostExecute(result);
                // Log.i("####", "####버전정보" + result.trim() + "/" + getAppVer());

                compareIntegrity(result.trim(), getIntegrity());
            }
        };
        ia.execute();
    }

    // 무결성
    public void compareIntegrity(String server_ver, String local_ver)
    {

        Log.e("Jonathan", "integrity server_ver :: " + server_ver);
        Log.e("Jonathan", "integrity local_ver :: " + local_ver);

        if (server_ver == null || server_ver.equals(""))
        {
            final EventPopupC epc = new EventPopupC(this);
            Button bt_done = (Button) epc.findViewById(R.id.btn_ok);
            bt_done.setOnClickListener(new OnClickListener()
            {

                @Override
                public void onClick(View arg0)
                {
                    epc.dismiss();
                    finish();
                }
            });
            epc.show("무결성 검증에 실패하였습니다.\n다시 실행하여 주십시오.");
            return;
        }
        if (server_ver.equals(local_ver))
        {
            start();
        }
        else
        {
            showIntegrity();
        }
    }

    public void compareVer(String new_ver, String now_ver)
    {
        LogUtil.e("Jonathan", "version server    :: " + new_ver);
        LogUtil.e("Jonathan", "version local_ver :: " + now_ver);
        LogUtil.d("hjt", "new_ver, now_ver  " + "new_ver = " + new_ver + " | now_ver = " + now_ver);
        int app_ver_float = -1;
        int server_ver_float = -1;
        if(new_ver != null && now_ver != null && new_ver != "" && now_ver != ""){
            app_ver_float = Integer.parseInt(now_ver.replace(".", ""));
            server_ver_float = Integer.parseInt(new_ver.replace(".", ""));
        }
        LogUtil.d("hjt", " versionCheck  " + "app_ver_float = " + app_ver_float + " | server_ver_float = " + server_ver_float);

        if (new_ver == null || new_ver.equals(""))
        {
            final EventPopupC epc = new EventPopupC(this);
            Button bt_done = (Button) epc.findViewById(R.id.btn_ok);
            bt_done.setOnClickListener(new OnClickListener()
            {

                @Override
                public void onClick(View arg0)
                {
                    epc.dismiss();
                    finish();
                    android.os.Process.killProcess(android.os.Process.myPid());
                    System.exit(0);
//                    ExitActivity.exitApplication(LoginActivity.this);

                }
            });
            if(pp != null)
                pp.hide();
            if(epc != null)
                epc.show("버전정보확인에 실패하였습니다.\n다시 실행하여 주십시오.");
            return;
        }
        else if((app_ver_float != -1 && server_ver_float != -1 ) && (app_ver_float < server_ver_float))
        {
            LogUtil.d("hjt", "new_ver != now_ver  " + "new_ver = " + new_ver + " | now_ver = " + now_ver);
            if (pp != null)
                pp.hide();
            showUpdate();
        }
        else
        {
            LogUtil.d("hjt", "new_ver == now_ver");
            if(pp != null)
                pp.hide();
            start();
            clickLogin();
        }
    }

    public void showUpdate()
    {
        final EventPopupC epc = new EventPopupC(this);
        epc.setCancelable(false);
        Button bt_done = (Button) epc.findViewById(R.id.btn_ok);
        bt_done.setOnClickListener(new OnClickListener()
        {

            @Override
            public void onClick(View arg0)
            {
                download();
                if(epc != null)
                    epc.dismiss();
            }
        });
        epc.show("버전이 업데이트 되었습니다.\n다운로드 합니다.");
    }

    // 무결성
    public void showIntegrity()
    {
        final EventPopupC epc = new EventPopupC(this);
        epc.setCancelable(false);
        Button bt_done = (Button) epc.findViewById(R.id.btn_ok);
        bt_done.setOnClickListener(new OnClickListener()
        {

            @Override
            public void onClick(View arg0)
            {
                // download();
                epc.dismiss();
                finish();
                android.os.Process.killProcess(android.os.Process.myPid());
                System.exit(0);
            }
        });
        epc.show("무결성 검증에 실패하였습니다.\n 관리자에게 문의하세요.");
    }

    private void download()
    {
        DownloadTask dt = new DownloadTask(this);
        dt.execute();
    }

    private class DownloadTask extends AsyncTask<String, String, Boolean>
    {

        ProgressPopup pp;

        public DownloadTask(Context context)
        {
            pp = new ProgressPopup(context);
        }

        @Override
        protected void onPreExecute()
        {
            if(pp != null){
                pp.setMessage("업데이트 버전을 다운로드 중입니다.");
                pp.show();
            }
            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(String... args)
        {
            String strApkName = DEFINE.DOWNLOAD_APK;
            String strLocalPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/";
            String strServerApkPath = DEFINE.DOWNLOAD_URL;
            File file = new File(strLocalPath + strApkName);
            try
            {
                if (file.exists())
                    file.delete();
                InputStream inputStream = new URL(strServerApkPath + strApkName).openStream();
                OutputStream out = new FileOutputStream(file);
                writeFile(inputStream, out);
                out.close();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
            startActivity(i);
            if(pp != null)
                pp.dismiss();
            finish();
            return null;
        }

        public void writeFile(InputStream is, OutputStream os) throws IOException
        {
            byte[] buffer = new byte[1024];
            int length;
            while ((length = is.read(buffer)) >= 0)
            {
                os.write(buffer, 0, length);
            }
            is.close();
            os.flush();
            os.close();
        }

        @Override
        protected void onCancelled()
        {
            pp.dismiss();
            super.onCancelled();
        }
    }

    private String getAppVer()
    {
        String version = "0";
        try
        {
            PackageInfo i = getPackageManager().getPackageInfo(getPackageName(), 0);
            version = i.versionName;
        }
        catch (NameNotFoundException e)
        {
            e.printStackTrace();
        }
        return version;
    }

    // 무결성 가져오기
    private String getIntegrity()
    {
        PackageManager pm = this.getPackageManager();
        PackageInfo info = null;
        String Integrity = "";
        try
        {
            info = pm.getPackageInfo(this.getPackageName(), PackageManager.GET_SIGNATURES);
            Integrity = info.signatures[0].toCharsString();
            kog.e("Jonathan", "packageinfo :: " + info.signatures[0].toCharsString());

        }
        catch (NameNotFoundException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        if (info.signatures[0].toCharsString().equals(""))
        {
            // signature is OK
        }
        return Integrity;
    }

    private void start()
    {
        // Intent in = new Intent(this, LoginActivity.class);
        // startActivity(in);
        // finish();
    }

    private class VersionAsync extends AsyncTask<String, String, String>
    {

        Context       context;
        ProgressPopup pp;

        public VersionAsync(Context context)
        {
            this.context = context;
            pp = new ProgressPopup(context);
        }

        @Override
        protected void onPreExecute()
        {
            pp.setMessage("버전을 조회 중입니다.");
            pp.show();
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... args)
        {
            if(pp != null)
                pp.dismiss();
            return loadApkVersionData();
        }

        @Override
        protected void onCancelled()
        {
            pp.dismiss();
            super.onCancelled();
        }

        private String loadApkVersionData()
        {
            StringBuilder sb = new StringBuilder();
            try
            {
                String strLoadPage = "";

                strLoadPage = DEFINE.DOWNLOAD_URL + "version.txt";

                Log.i("####", "####versiontxt : " + strLoadPage);
                URL url = new URL(strLoadPage);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                if (urlConnection == null)
                    return null;
                urlConnection.setConnectTimeout(10000);
                urlConnection.setUseCaches(false);
                if (urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK)
                {
                    InputStream inputStream = urlConnection.getInputStream();
                    InputStreamReader isr = new InputStreamReader(inputStream, "utf-8");
                    BufferedReader br = new BufferedReader(isr);
                    while (true)
                    {
                        String line = br.readLine();
                        if (line == null)
                            break;
                        sb.append(line + "\n");
                    }
                    br.close();
                }

            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
            return sb.toString();
        }
    }

    // 무결성 검증
    private class IntegrityAsync extends AsyncTask<String, String, String>
    {

        Context       context;
        ProgressPopup pp;

        public IntegrityAsync(Context context)
        {
            this.context = context;
            pp = new ProgressPopup(context);
        }

        @Override
        protected void onPreExecute()
        {
            pp.setMessage("무결성 검증을 하고 있습니다.");
            pp.show();
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... args)
        {
            pp.dismiss();
            return IntegrityData();
        }

        @Override
        protected void onCancelled()
        {
            pp.dismiss();
            super.onCancelled();
        }

        private String IntegrityData()
        {
            StringBuilder sb = new StringBuilder();
            try
            {

                String integrity_num = "";

                integrity_num = DEFINE.DOWNLOAD_URL + "integrity.txt";
                Log.e("Jonathan", integrity_num);

                URL url = new URL(integrity_num);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                if (urlConnection == null)
                    return null;
                urlConnection.setConnectTimeout(10000);
                urlConnection.setUseCaches(false);
                if (urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK)
                {
                    InputStream inputStream = urlConnection.getInputStream();
                    InputStreamReader isr = new InputStreamReader(inputStream, "utf-8");
                    BufferedReader br = new BufferedReader(isr);
                    while (true)
                    {
                        String line = br.readLine();
                        if (line == null)
                            break;
                        sb.append(line + "\n");
                    }
                    br.close();
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
            return sb.toString();
        }
    }

    private void checkFirstMonth()
    {
        String currentDay = CommonUtil.getCurrentDay();
        int year = Integer.parseInt(currentDay.substring(0, 4));
        int month = Integer.parseInt(currentDay.substring(4, 6));
        int day = Integer.parseInt(currentDay.substring(6, 8));
        if (day == 1)
        {
            String backDate = mPreferencesUtil.getFirstMonthDay();
            if (backDate.equals("0"))
            {
                mPreferencesUtil.setFirstMonthDay(currentDay);
            }
            else
            {
                int backYear = Integer.parseInt(backDate.substring(0, 4));
                int backMonth = Integer.parseInt(backDate.substring(4, 6));
                // int backDay = Integer.parseInt(backDate.substring(6, 8));
                if (year > backYear)
                {
                    runUiThread(UiRunnable.MODE_PROGRESS_SHOW, "데이터 초기화");
                    mPreferencesUtil.setSyncSuccess(false);
                    mPreferencesUtil.setFirstMonthDay(currentDay);
                    dropAllTables(new DbAsyncResLintener()
                    {

                        @Override
                        public void onCompleteDB(String funName, int type, Cursor cursor, String tableName)
                        {
                            // TODO Auto-generated method stub
                            mProgressDialog.hide();
                        }
                    });
                    runUiThread(UiRunnable.MODE_PROGRESS_HIDE, "데이터 초기화");
                }
                else
                {
                    if (month > backMonth)
                    {
                        runUiThread(UiRunnable.MODE_PROGRESS_SHOW, "데이터 초기화");
                        mPreferencesUtil.setSyncSuccess(false);
                        mPreferencesUtil.setFirstMonthDay(currentDay);
                        dropAllTables(new DbAsyncResLintener()
                        {

                            @Override
                            public void onCompleteDB(String funName, int type, Cursor cursor, String tableName)
                            {
                                // TODO Auto-generated method stub
                                mProgressDialog.hide();
                            }
                        });
                        runUiThread(UiRunnable.MODE_PROGRESS_HIDE, "데이터 초기화");
                    }
                }
            }
        }
    }

    private void checkFirstMonth2()
    {

        final SharedPreferencesUtil sharedPreferencesUtil = SharedPreferencesUtil.getInstance();
        SharedPreferencesUtil shareU = SharedPreferencesUtil.getInstance();
        int nMonth = shareU.getInt("login_first_check_month", 0);

        GregorianCalendar today = new GregorianCalendar();
        int month = today.get(Calendar.MONTH) + 1;
        Log.i("nMonth/todayMonth", nMonth + "/" + month);

        if (nMonth == 12 && month == 1)
            nMonth = 0;

        if (month > nMonth)
        {
            Log.e("First login the month", "First login the month");
            runUiThread(UiRunnable.MODE_PROGRESS_SHOW, "데이터 초기화");
            mPreferencesUtil.setSyncSuccess(false);
            dropTablesWithRepairResult(new DbAsyncResLintener()
            {

                @Override
                public void onCompleteDB(String funName, int type, Cursor cursor, String tableName)
                {
                    // TODO Auto-generated method stub
                    runUiThread(UiRunnable.MODE_PROGRESS_HIDE, "데이터 초기화");
                    sharedPreferencesUtil.setSyncSuccess(false);
                }
            });
        }
    }

    // private void deleteRowResultDataAnotherMonth() {
    //
    // Log.e("deleteRowResultDataAnotherMonth", "deleteRowResultDataAnotherMonth");
    //
    // deleteResultDataBase(new DbAsyncResLintener() {
    //
    // @Override
    // public void onCompleteDB(String funName, int type, Cursor cursor,
    // String tableName) {
    // // TODO Auto-generated method stub
    //
    // Log.e("Cursor Size", ""+cursor.getColumnCount());
    // while (!cursor.isAfterLast()) {
    // int idex = cursor.getColumnIndex("INVNR");
    //
    // deleteResultDataBaseAther(DEFINE.REPAIR_RESULT_BASE_TABLE_NAME, cursor.getString(idex));
    // deleteResultDataBaseAther(DEFINE.REPAIR_RESULT_STOCK_TABLE_NAME, cursor.getString(idex));
    // deleteResultDataBaseAther(DEFINE.REPAIR_RESULT_IMAGE_TABLE_NAME, cursor.getString(idex));
    //
    // cursor.moveToNext();
    // }
    // cursor.close();
    // }
    // });
    // }

    private ArrayList<ResultSendModel> mResultSendModels = new ArrayList<ResultSendModel>();

    private void queryBaseGroup()
    {
        SharedPreferencesUtil sharedPreferencesUtil = SharedPreferencesUtil.getInstance();
        final Calendar c = Calendar.getInstance();
        int mDay = c.get(Calendar.DAY_OF_MONTH);
        // 2014-01-19 KDH 오늘 날짜인 경우라면 할필요가없다.
        if (sharedPreferencesUtil.getFirstDay() != mDay)
        {
            sharedPreferencesUtil.setFirstDay(mDay);
            isToday = true;
        }
        else
        {
            // 2014-01-16 KDH 집에간다.
            return;
        }

        mResultSendModels.clear();
        // showProgress();
        String[] _whereArgs = null;
        String[] _whereCause = null;

        String[] colums = null;

        DbQueryModel dbQueryModel = new DbQueryModel(DEFINE.REPAIR_RESULT_BASE_TABLE_NAME, _whereCause, _whereArgs, colums);

        DbAsyncTask dbAsyncTask = new DbAsyncTask("queryBaseGroup", this, new DbAsyncResLintener()
        {

            @SuppressWarnings("null")
            @Override
            public void onCompleteDB(String funName, int type, Cursor cursor, String tableName)
            {
                // TODO Auto-generated method stub
                // hideProgress();
                if (funName.equals("queryBaseGroup"))
                {

                    if (cursor != null || cursor.getCount() > 0)
                    {
                        cursor.moveToFirst();
                        // 여기에서 바로 삭제한다.
                        while (!cursor.isAfterLast())
                        {

                            String iEDD = cursor.getString(4);
                            String name = cursor.getString(6);
                            String iEDZ = cursor.getString(7);
                            String iNVNR = cursor.getString(16);
                            String message = cursor.getString(9);
                            String count = cursor.getString(13);

                            kog.e("KDH", "iEDD = " + iEDD);
                            kog.e("KDH", "iEDZ = " + iEDZ);
                            kog.e("KDH", "iNVNR = " + iNVNR);
                            kog.e("KDH", "count = " + count);

                            deleteResultDataBaseAther(DEFINE.REPAIR_RESULT_BASE_TABLE_NAME, iNVNR);
                            deleteResultDataBaseAther(DEFINE.REPAIR_RESULT_STOCK_TABLE_NAME, iNVNR);
                            deleteResultDataBaseAther(DEFINE.REPAIR_RESULT_IMAGE_TABLE_NAME, iNVNR);

                            ResultSendModel model = new ResultSendModel(iEDD, iEDZ, iNVNR, name, count, message);
                            mResultSendModels.add(model);
                            cursor.moveToNext();
                        }
                        kog.e("KDH", "====================" + mResultSendModels.size());

                        if (mResultSendModels.size() > 0)
                        {
                            String msg = mResultSendModels.size() + getString(R.string.login_save_data);
                            EventPopup2 eventPopup = new EventPopup2(LoginActivity.this, msg, null);
                            eventPopup.show();
                        }
                    }
                }
            }
        }, dbQueryModel);
        dbAsyncTask.execute(DbAsyncTask.DB_SELECT);
    }

    // private void deleteResultDataBase(){
    private void deleteRowResultDataAnotherMonth()
    {

        // Log.e("deleteRowResultDataAnotherMonth", "deleteRowResultDataAnotherMonth");

        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMM");
        Calendar rightNow = Calendar.getInstance();
        final String strCurrentDate = formatter.format(rightNow.getTime());

        // ContentValues contentValues = new ContentValues();
        // contentValues.put("IEDD", "NOT LIKE"+strCurrentDate+"%");
        //
        // String[] keys = new String[1];
        // keys[0] = "IEDD";

        String[] _whereArgs = null;
        String[] _whereCause = null;
        String[] colums = null;

        DbQueryModel dbQueryModel = new DbQueryModel(DEFINE.REPAIR_RESULT_BASE_TABLE_NAME, _whereCause, _whereArgs, colums);

        DbAsyncTask dbAsyncTask = new DbAsyncTask("deleteResultDataBase", LoginActivity.this, new DbAsyncResLintener()
        {

            @Override
            public void onCompleteDB(String funName, int type, Cursor cursor, String tableName)
            {
                // TODO Auto-generated method stub

                if (cursor == null)
                    return;

                kog.e("Cursor Size", "" + cursor.getCount());

                cursor.moveToFirst();
                while (!cursor.isAfterLast())
                {

                    String strIEDD = cursor.getString(4);
                    String strINVNR = cursor.getString(15);
                    if (strIEDD.length() >= 6)
                    {
                        strIEDD = strIEDD.substring(0, 6);
                    }
                    Log.e("INVNR", "" + strINVNR);
                    if (!strCurrentDate.equals(strIEDD))
                    {
                        deleteResultDataBaseAther(DEFINE.REPAIR_RESULT_BASE_TABLE_NAME, strINVNR);
                        deleteResultDataBaseAther(DEFINE.REPAIR_RESULT_STOCK_TABLE_NAME, strINVNR);
                        deleteResultDataBaseAther(DEFINE.REPAIR_RESULT_IMAGE_TABLE_NAME, strINVNR);
                    }
                    cursor.moveToNext();
                }
                cursor.close();
            }
        }, dbQueryModel);

        dbAsyncTask.execute(DbAsyncTask.DB_SELECT);
    }

    private void deleteResultDataBaseAther(String TableName, String INVNR)
    {
        ContentValues contentValues = new ContentValues();
        contentValues.put("INVNR", INVNR);

        String[] keys = new String[1];
        keys[0] = "INVNR";

        DbAsyncTask dbAsyncTask = new DbAsyncTask(TableName, TableName, this, this, contentValues, keys);

        dbAsyncTask.execute(DbAsyncTask.DB_DELETE);
    }






}
