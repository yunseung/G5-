package com.ktrental.common;

import android.app.Activity;
import android.app.Application;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.crashlytics.android.Crashlytics;
import com.ktrental.calendar.CalendarController;
import com.ktrental.calendar.DayInfoModel;
import com.ktrental.cm.connect.ConnectController;
import com.ktrental.cm.connect.ConnectorUtil;
import com.ktrental.cm.db.DbAsyncTask;
import com.ktrental.cm.db.DbAsyncTask.DbAsyncResLintener;
import com.ktrental.model.DbQueryModel;
import com.ktrental.cm.db.SqlLiteAdapter;
import com.ktrental.model.LoginModel;
import com.ktrental.model.RepairDayInfoModel;
import com.ktrental.model.RepairPlanModel;
import com.ktrental.util.CommonUtil;
import com.ktrental.util.LogUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Observer;

import io.fabric.sdk.android.Fabric;

public class KtRentalApplication extends Application implements
        DbAsyncResLintener {
    // 2017.08.01. hjt. mLoginModel 에 대해 static 으로 변경
    private static LoginModel mLoginModel;

    private static KtRentalApplication mApplication;

    private String phoneNumber;

    private static RepairPlanObserver mRepairPlanObserver;

    private static String[] mEncondingFieldArray = {DEFINE.DRV_TEL,
            DEFINE.DRV_MOB, DEFINE.MOB_NUMBER2, DEFINE.USERID};

    private String[] maintenace_plan_colums = {DEFINE.CCMSTS, DEFINE.GSTRS,
            DEFINE.CEMER, DEFINE.GUBUN, DEFINE.DELAY};

    private ArrayList<RepairPlanModel> mRepairPlanModelArray = new ArrayList<RepairPlanModel>();
    private int mComplateVal, mComplateVal2, mComplateVal3 = 0;

    private int mPlanVal, mPlanVal2, mPlanVal3 = 0;

    private int mEmergencyVal, mEmergencyVal2, mEmergencyVal3 = 0;

    private int mNotImplementedVal = 0;
    private int mNotLongImplementedVal = 0;

    private ArrayList<RepairDayInfoModel> mDayList;

    private static final String DB_PATH = "DATABASE";
    private static final String DOWNLOAD_PATH = "DOWNLOAD";
    private String mDbPath;

    private OnCalendarComplate mOnCalendarComplate;

    public interface OnCalendarComplate {
        void onCalendarComplate();
    }

    public ArrayList<RepairDayInfoModel> getDayList() {
        return mDayList;
    }

    public void setDayList(ArrayList<RepairDayInfoModel> _DayList) {
        this.mDayList = _DayList;
    }

    private static HashMap<String, Boolean> mEmergencyMap = new HashMap<String, Boolean>();

    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();
        Fabric.with(this, new Crashlytics());

        initDb();
        ConnectorUtil.init();

        mRepairPlanObserver = new RepairPlanObserver();

        mApplication = this;
        phoneNumber = CommonUtil.getPhoneNumber(this);

        mDayList = new ArrayList<RepairDayInfoModel>();


    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public static KtRentalApplication getInstance() {
        return mApplication;
    }

    public static LoginModel getLoginModel() {
        return KtRentalApplication.getInstance().mLoginModel;
    }

    public static void setSetLoginModel(LoginModel loginModel) {
        LogUtil.d("hjt", "setSetLoginModel = " + loginModel);
        KtRentalApplication.getInstance().mLoginModel = loginModel;
    }

    public static void addRepairPlanObserver(Observer observer) {
        mRepairPlanObserver.addObserver(observer);
    }

    public static void changeRepair() {
        // KtRentalApplication.getInstance().queryMaintenacePlan();
        mRepairPlanObserver.testA();
    }

    public static boolean isEncoding(String str) {
        boolean isEncoding = false;

        for (int i = 0; i < mEncondingFieldArray.length; i++) {
            if (mEncondingFieldArray[i].equals(str)) {
                isEncoding = true;
                break;
            }
        }

        return isEncoding;
    }

    public void queryMaintenacePlan(DbAsyncResLintener listener) {

        String[] _whereArgs = {};
        String[] _whereCause = {};

        String[] colums = maintenace_plan_colums;

        DbQueryModel dbQueryModel = new DbQueryModel(
                ConnectController.REPAIR_TABLE_NAME, _whereCause, _whereArgs,
                colums);

        dbQueryModel.setOrderBy("GSTRS asc");

        DbAsyncTask dbAsyncTask = new DbAsyncTask("queryMaintenacePlan", this,
                listener, dbQueryModel);

        dbAsyncTask.execute(DbAsyncTask.DB_SELECT);

    }

    public void queryMaintenacePlan() {

        mDayList.clear();
//		Log.d("HONG", "queryMaintenacePlan " + mDayList.size());
        CalendarController mCalendarManager = new CalendarController(
                CalendarController.TYPE_SHOW_OTHERMONTH);
        for (DayInfoModel dayInfoModel : mCalendarManager.getDayInfoArrayList()) {
            mDayList.add(new RepairDayInfoModel(dayInfoModel));
        }

        // showProgress();

        String[] _whereArgs = {};
        String[] _whereCause = {};

        String[] colums = maintenace_plan_colums;

        DbQueryModel dbQueryModel = new DbQueryModel(
                ConnectController.REPAIR_TABLE_NAME, _whereCause, _whereArgs,
                colums);

        dbQueryModel.setOrderBy("GSTRS asc");

        DbAsyncTask dbAsyncTask = new DbAsyncTask("queryMaintenacePlan", this,
                this, dbQueryModel);

        dbAsyncTask.execute(DbAsyncTask.DB_SELECT);

    }

    final String E0004 = "E0004";
    final String E0005 = "E0005";

    @Override
    public void onCompleteDB(String funName, int type, Cursor cursor,
                             String tableName) {
        // TODO Auto-generated method stub

        if (cursor == null)
            return;

        try {
            cursor.moveToFirst();
            mRepairPlanModelArray.clear();
            mComplateVal = 0;
            mComplateVal2 = 0;
            mComplateVal3 = 0;
            mPlanVal = 0;
            mPlanVal2 = 0;
            mPlanVal3 = 0;
            mEmergencyVal = 0;
            mEmergencyVal2 = 0;
            mEmergencyVal3 = 0;
            mNotImplementedVal = 0;

            RepairPlanModel repairPlanModel = new RepairPlanModel();

            int backWorkDay = -1;
            int backIndex = 0;

            while (!cursor.isAfterLast()) {

                String work = cursor.getString(0);

                int workDay = Integer.valueOf(cursor.getString(1));
                String cemer = cursor.getString(2);
                String gubun = cursor.getString(3);
                String delay = cursor.getString(4);

                if (Integer.parseInt(delay) > 60) {
                    mNotLongImplementedVal++;
                }

                if (E0005.equals(work)) {
                    mNotImplementedVal++;
                }

                if (E0004.equals(work)) {
                    if (gubun.trim().isEmpty()) {
                        mComplateVal++;
                    } else if (gubun.trim().equals("A")) {
                        mComplateVal2++;
                    } else if (gubun.trim().equals("O")) {
                        mComplateVal3++;
                    }

                    if (cemer != null) {
                        if (!cemer.trim().isEmpty()) {
                            if (gubun.trim().isEmpty()) {
                                mEmergencyVal++;
                            } else if (gubun.trim().equals("A")) {
                                mEmergencyVal2++;
                            } else if (gubun.trim().equals("O")) {
                                mEmergencyVal3++;
                            }
                        }
                    }
                }

                if (backWorkDay < workDay) {
                    repairPlanModel = new RepairPlanModel();
                    boolean planFlag = true;
                    if (cemer != null)
                        if (cemer.equals(" "))
                            planFlag = false;

                    repairPlanModel.addWork(work, planFlag, gubun);
                    mRepairPlanModelArray.add(repairPlanModel);

                    for (int i = backIndex; i < mDayList.size(); i++) {
                        DayInfoModel model = mDayList.get(i).getDayInfoModel();
                        if (model != null) {

                            int key = Integer.valueOf(model.getCurrentDay());

                            if (key == workDay) {
                                mDayList.get(i).setRepairPlanModel(repairPlanModel);
                                backIndex = i;
                                break;
                            }
                        }

                    }

                } else {
                    repairPlanModel = mRepairPlanModelArray
                            .get(mRepairPlanModelArray.size() - 1);
                    boolean planFlag = true;
                    if (cemer != null)
                        if (cemer.equals(" "))
                            planFlag = false;
                    repairPlanModel.addWork(work, planFlag, gubun);
                    mRepairPlanModelArray.set(mRepairPlanModelArray.size() - 1,
                            repairPlanModel);

                    //
                }

                backWorkDay = workDay;

                // mComplateVal = mComplateVal + repairPlanModel.getComplate();

                if (cemer != null) {
                    if (cemer.equals(" ")) {
                        if (gubun.trim().isEmpty()) {
                            mPlanVal++;
                        } else if (gubun.trim().equals("A")) {
                            mPlanVal2++;
                        } else if (gubun.trim().equals("O")) {
                            mPlanVal3++;
                        }
                    } else if (!cemer.trim().isEmpty()) {
                        if (gubun.trim().isEmpty()) {
                            mEmergencyVal++;
                        } else if (gubun.trim().equals("A")) {
                            mEmergencyVal2++;
                        } else if (gubun.trim().equals("O")) {
                            mEmergencyVal3++;
                        }
                    }


                }

            cursor.moveToNext();

        }

        // mPlanVal = cursor.getCount();
        cursor.close();
        changeRepair();
    } catch(
    Exception e)

    {
        e.printStackTrace();
        cursor.close();
    }


    // setPlanTitle();
    //
    // maintenance_Date_Adapter.setDateList(mDayList);
    //
    // hideProgress();

}

    public int getComplateVal() {
        return mComplateVal;
    }

    public int getComplateVal2() {
        return mComplateVal2;
    }

    public int getComplateVal3() {
        return mComplateVal3;
    }

    public void setComplateVal(int ComplateVal) {
        this.mComplateVal = ComplateVal;
    }

    public int getPlanVal() {
        return mPlanVal;
    }

    public int getPlanVal2() {
        return mPlanVal2;
    }

    public int getPlanVal3() {
        return mPlanVal3;
    }

    public int getEmergencyVal() {
        return mEmergencyVal;
    }

    public int getEmergencyVal2() {
        return mEmergencyVal2;
    }

    public int getEmergencyVal3() {
        return mEmergencyVal3;
    }

    public int getNotImplementedVal() {
        return mNotImplementedVal;
    }

    public int getNotLongImplementedVal() {
        return mNotLongImplementedVal;
    }


    public void setPlanVal(int PlanVal) {
        this.mPlanVal = PlanVal;
    }

    public int loadSequence() {
        SharedPreferences sp = getSharedPreferences("KT_RENTAL",
                Activity.MODE_PRIVATE);
        int s = sp.getInt("Sequence", 0) + 1;

        SharedPreferences.Editor editor = sp.edit();
        editor.putInt("Sequence", s);
        editor.commit();

        return s;
    }

    public static void setEmergency(String carnum, boolean check) {
        if (mEmergencyMap.containsKey(carnum)) {
            mEmergencyMap.remove(carnum);

        }
        mEmergencyMap.put(carnum, check);
    }

    public static boolean getEmergency(String carnum) {

        boolean check = false;

        if (mEmergencyMap.containsKey(carnum)) {
            check = mEmergencyMap.get(carnum);
        }

        return check;
    }

    public static Handler timeout = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            ProgressDialog pd = (ProgressDialog) msg.obj;
            if (pd != null || pd.isShowing())
                pd.dismiss();
        }
    };

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

}
