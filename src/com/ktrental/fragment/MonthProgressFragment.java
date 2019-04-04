package com.ktrental.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.ktrental.R;
import com.ktrental.adapter.MonthProgressAdapter;
import com.ktrental.calendar.CalendarController;
import com.ktrental.calendar.DayInfoModel;
import com.ktrental.cm.connect.ConnectController;
import com.ktrental.cm.db.DbAsyncTask;
import com.ktrental.cm.db.DbAsyncTask.DbAsyncResLintener;
import com.ktrental.common.DEFINE;
import com.ktrental.common.KtRentalApplication;
import com.ktrental.dialog.Duedate_Dialog;
import com.ktrental.dialog.Unimplementation_Reason_Dialog;
import com.ktrental.fragment.CalendarFragment.OnCalendarListener;
import com.ktrental.fragment.TransferManageFragment.OnDismissDialogFragment;
import com.ktrental.model.BaseMaintenanceModel;
import com.ktrental.model.DbQueryModel;
import com.ktrental.model.MonthProgressModel;
import com.ktrental.model.RepairDayInfoModel;
import com.ktrental.model.RepairPlanModel;
import com.ktrental.popup.BaseTextPopup;
import com.ktrental.popup.BaseTextPopup.OnSelectedPopupItem;
import com.ktrental.popup.EventPopupC;
import com.ktrental.popup.IoTCancelPopup;
import com.ktrental.product.VocInfoActivity;
import com.ktrental.ui.Mam;
import com.ktrental.util.CommonUtil;
import com.ktrental.util.LogUtil;
import com.ktrental.util.OnChangeFragmentListener;
import com.ktrental.util.OnEventOkListener;
import com.ktrental.util.kog;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * 월간 진행상태 화면
 *
 * @author hongsungil
 * @since 2013.07.24
 */
@SuppressLint("ValidFragment")
public class MonthProgressFragment extends BaseRepairFragment implements OnItemClickListener, DbAsyncResLintener,
        OnClickListener, OnSelectedPopupItem, OnDismissDialogFragment, OnCalendarListener {

    private ListView mLvProgressStatus;
    private TextView mTvHeaderTitle;
    private CalendarFragment mCalendarFragment;
    private MonthProgressAdapter monthProgressAdapter;
    private EditText mEtSearch;

    // private ArrayList<MonthProgressModel> mProgressModels = new
    // ArrayList<MonthProgressModel>();

    private final static String PROGRESS_FILTER = "PROGRESS_FILTER";
    private final static String INFO_FILTER = "INFO_FILTER";
    private final static String INFO_FILTER2 = "INFO_FILTER2";

    private Button mAllButton;
    private Button mBtnInfo;

    private Button mBtnSearch;
    private TextView mTvPlan, mTvPlan2, mTvPlan3;
    private TextView mTvComplate, mTvComplate2, mTvComplate3;
    private TextView mTvNotImplemented, mTvNotLongImplemented;
    private Button mBtnDelText;

    private Button BtnDate; // 예정일변경 버튼
    private Button BtnTransfer; // 이관 관리 버튼
    private Button BtnNotImplemented; // 미실시사유등록 버튼
    private Button BtnMonthVocInfo; // VOC 내역 조회
    private Button BtnIotCancel; //IoT 정비 취소

//	private Mam mWorkProgress;

    private String[] month_colums = {DEFINE.GSUZS, DEFINE.INVNR, DEFINE.KUNNR_NM, DEFINE.DRIVN, DEFINE.MAKTX, //4
            DEFINE.POST_CODE, DEFINE.CITY, DEFINE.STREET, DEFINE.DRV_TEL, DEFINE.CCMSTS, DEFINE.GSTRS, DEFINE.AUFNR, //11
            DEFINE.EQUNR, DEFINE.CTRTY, DEFINE.DRV_MOB, DEFINE.CEMER, DEFINE.GUEEN2, DEFINE.TXT30, DEFINE.MDLCD, //18
            DEFINE.VOCNUM, DEFINE.KUNNR, DEFINE.DELAY, DEFINE.CYCMN_TX, DEFINE.APM, DEFINE.VBELN, DEFINE.GUBUN, DEFINE.REQNO, //26
            DEFINE.CCMRQ, DEFINE.ATVYN, DEFINE.PRERQ, DEFINE.MINVNR};

    private HashMap<String, DbAsyncTask> mAsyncMap = new HashMap<String, DbAsyncTask>();

    private String mCurrentDay = "";

    private LinkedHashMap<String, String> mProgressFilterMap = new LinkedHashMap<String, String>();

    private BaseTextPopup mProgressFilterPopup;

    private LinkedHashMap<String, String> mInfoFilterMap = new LinkedHashMap<String, String>();

    private BaseTextPopup mInfoFilterPopup;

    int getfiliter2 = 0;

    private CheckBox btn_filiter2;
    private BaseTextPopup mMaintenanceFilterPopup;
    private LinkedHashMap<String, String> mMaintenanceFilterMap = new LinkedHashMap<String, String>();
    private final static String MAINTENANCE_FILTER = "MaintenanceFilter";
    private LinkedHashMap<String, String> mInfoFilterMap2 = new LinkedHashMap<String, String>();

    private String[] FILTER_INFO_ARRAY = {"차량번호", "고객명"};
    private String[] FILTER_INFO_ARRAY2 = {"고객명", "주소"};

    private int mInfoFilterType = 0;
    private int mInfoFilterType2 = 0;

    private TransferManageFragment mTransferManageFragment;
    private ImageView mEmptyView;

    private ArrayList<RepairPlanModel> mRepairPlanModelArray = new ArrayList<RepairPlanModel>();
    private int mComplateVal = 0;

    private int mPlanVal = 0;

    private String AllBtnBuffer = "";

    private boolean AllButtonChangeFlag = false;
    private ArrayList<ArrayList<String>> tempBtnAll = new ArrayList<ArrayList<String>>();

    private ArrayList<RepairDayInfoModel> mDayList = new ArrayList<RepairDayInfoModel>();

    private boolean isShowVocView;

    private BarChart mBarChart = null;


    // public MonthProgressFragment(String className,
    // OnChangeFragmentListener changeFragmentListener) {
    // super(className, changeFragmentListener);
    // // TODO Auto-generated constructor stub
    // }
    public MonthProgressFragment() {
    }

    public MonthProgressFragment(String className, OnChangeFragmentListener changeFragmentListener,
                                 boolean bChangeFlag) {
        super(className, changeFragmentListener);
        // TODO Auto-generated constructor stub

        AllButtonChangeFlag = bChangeFlag;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        // Log.e("onCreate()", "onCreate()");
        // setRetainInstance(true);

        // mCalendarFragment = new CalendarFragment();
        // getFragmentManager()
        // .beginTransaction()
        // .add(R.id.ll_calendar, mCalendarFragment,
        // "MonthProgressFragment")
        // .commit();
        // initGetDBData();
        initQueryFilter();
        initInfoFilter();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.month_progress_layout, container, false);

        mRootView = root;

        initViewSetting(root);

        mTransferManageFragment = new TransferManageFragment(MonthProgressFragment.class.getName(), null, this,
                mContext);

        initMonthProgress();

        return root;
    }

    @Override
    public void onStart() {
        // TODO Auto-generated method stub
        super.onStart();
        // Log.e("onStart()", "onStart()");
        setMonthTitle();

        // myung 20131202 ADD 당월정비 처리/계획 클릭 시 월간 정비현황의 완료건만 보여져야 함.
        if (AllButtonChangeFlag) {
            // AllButtonChangeFlag = false;
            mAllButton.setText("완료");
            mAllButton.setTag("E0004");
            // myung 20131206 DELETE 홈->당월정비->처리/계획-> 완료만 리스트되지 않는 문제
            // clickSearch2();
        }

        if (isShowVocView)
            monthProgressAdapter.initSelectedMaintenanceArray();

        mCalendarFragment.setOnSelectedItem(this);
        mCalendarFragment.invisibleSyncButton();

        isShowVocView = false;
    }

    // myung 20131202 ADD 당월정비 처리/계획 클릭 시 월간 정비현황의 완료건만 보여져야 함.
    public void setAllButtonFlag(boolean bBtn) {
        AllButtonChangeFlag = bBtn;
    }

    private void initViewSetting(View root) {
        mBarChart = (BarChart) root.findViewById(R.id.chart);
        // mHeaderView = inflater.inflate(R.layout.month_progress_header, null);

        mLvProgressStatus = (ListView) root.findViewById(R.id.lv_progress_status);

        // mLvProgressStatus.addHeaderView(mHeaderView);
        mTvHeaderTitle = (TextView) root.findViewById(R.id.tv_month_title);

        monthProgressAdapter = new MonthProgressAdapter(mContext, this);

        monthProgressAdapter.setDataArr(mBaseMaintenanceModels);
        mLvProgressStatus.setAdapter(monthProgressAdapter);

        mLvProgressStatus.setOnItemClickListener(this);

        mCalendarFragment = new CalendarFragment(CalendarFragment.class.getName(), null, true);

        getChildFragmentManager().beginTransaction().add(R.id.ll_calendar, mCalendarFragment, "ss").commit();

        mCalendarFragment.setOnCalendarListener(this);

        mAllButton = (Button) root.findViewById(R.id.btn_all);
        mAllButton.setOnClickListener(this);
        mAllButton.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub
                // Log.e("onTextChanged", ""+s+"/"+start+"/"+count);
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // TODO Auto-generated method stub
                // Log.e("onTextChanged", ""+s+"/"+start+"/"+count+"/"+after);

            }

            @Override
            public void afterTextChanged(Editable s) {
                // Log.e("afterTextChanged", ""+mAllButton.getText()+"/"+s);
                // TODO Auto-generated method stub
                if (!AllBtnBuffer.equals(s)) {
                    clickSearch2();
                }
            }
        });

        btn_filiter2 = (CheckBox) root.findViewById(R.id.maintenance_filter2_id);
        getfiliter2 = 0;
        btn_filiter2.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                showFilterPopup(mMaintenanceFilterPopup, btn_filiter2);
            }
        });

        mTvComplate = (TextView) root.findViewById(R.id.tv_month_complate);
        mTvPlan = (TextView) root.findViewById(R.id.tv_month_plan);
        mTvComplate2 = (TextView) root.findViewById(R.id.tv_month_complate2);
        mTvPlan2 = (TextView) root.findViewById(R.id.tv_month_plan2);
        mTvComplate3 = (TextView) root.findViewById(R.id.tv_month_complate3);
        mTvPlan3 = (TextView) root.findViewById(R.id.tv_month_plan3);
        mTvNotImplemented = (TextView) root.findViewById(R.id.tv_notimplemeneted);
        mTvNotLongImplemented = (TextView) root.findViewById(R.id.tv_long_notimplemented);

        root.findViewById(R.id.btn_notimplemented).setOnClickListener(this);

//		mWorkProgress = (Mam) root.findViewById(R.id.graph_id);
        mBtnInfo = (Button) root.findViewById(R.id.btn_carnum);
        mBtnInfo.setOnClickListener(this);

        mBtnSearch = (Button) root.findViewById(R.id.btn_search);
        mBtnSearch.setOnClickListener(this);

        mBtnDelText = (Button) root.findViewById(R.id.btn_delete_text);
        mBtnDelText.setOnClickListener(this);

        BtnMonthVocInfo = (Button) root.findViewById(R.id.btn_month_voc_info);
        BtnMonthVocInfo.setOnClickListener(this);

        BtnIotCancel = (Button) root.findViewById(R.id.btn_cancel_iot);
        BtnIotCancel.setOnClickListener(this);

        mEtSearch = (EditText) root.findViewById(R.id.et_search);
        mEtSearch.setOnKeyListener(new OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_ENTER) {

                        // edit.setText("");
                        clickSearch();
                        return true;
                    }
                }
                return false;
            }
        });

        mEtSearch.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    InputMethodManager imm = (InputMethodManager) mContext
                            .getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(mEtSearch.getWindowToken(), 0);

                }
                // else{
                // if(mEtSearch.getText().toString().length()>0){
                // mBtnDelText.setVisibility(View.VISIBLE);
                // }
                // }
            }
        });

        mEtSearch.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
                if (s.toString().length() > 0) {
                    mBtnDelText.setVisibility(View.VISIBLE);
                }
            }
        });

        BtnTransfer = (Button) root.findViewById(R.id.btn_transfer); // 이관 관리 버튼
        BtnTransfer.setOnClickListener(this);

        BtnNotImplemented = (Button) root.findViewById(R.id.btn_notimplemented); // 미실시사유등록
        BtnNotImplemented.setOnClickListener(this);

        BtnDate = (Button) root.findViewById(R.id.btn_date); // 예정일변경 버튼
        BtnDate.setOnClickListener(this);
        mEmptyView = (ImageView) root.findViewById(R.id.iv_empty);

    }

    private void drawBarGraph(int va1, int val2, int val3) {
        List<BarEntry> entries1 = new ArrayList<>();
        entries1.add(new BarEntry(1, va1));
        List<BarEntry> entries2 = new ArrayList<>();
        entries2.add(new BarEntry(2, val2));
        List<BarEntry> entries3 = new ArrayList<>();
        entries3.add(new BarEntry(3, val3));

        List<IBarDataSet> bars = new ArrayList<>();

        BarDataSet barDataSet1 = new BarDataSet(entries1, "first");
        barDataSet1.setColor(0XFFF8D052);
        BarDataSet barDataSet2 = new BarDataSet(entries2, "second");
        barDataSet2.setColor(0XFF5BB6F1);
        BarDataSet barDataSet3 = new BarDataSet(entries3, "third");
        barDataSet3.setColor(0XFF44C0AB);

        bars.add(barDataSet1);
        bars.add(barDataSet2);
        bars.add(barDataSet3);

        BarData barData = new BarData(bars);
//		barData.setDrawValues(false);
        barData.setValueFormatter(new MyValueFormatter());

        mBarChart.setData(barData);


        mBarChart.getDescription().setEnabled(false);
        mBarChart.getLegend().setEnabled(false);
        mBarChart.setFitBars(false);
        mBarChart.animateXY(300, 3000);
        mBarChart.getXAxis().setDrawGridLines(false);
        mBarChart.getXAxis().setDrawAxisLine(false);
        mBarChart.getXAxis().setDrawLabels(false);
        mBarChart.getAxisRight().setDrawGridLines(false);
        mBarChart.getAxisLeft().setDrawGridLines(false);
        mBarChart.getAxisRight().setEnabled(false);
        mBarChart.getAxisLeft().setEnabled(false);
        mBarChart.setExtraOffsets(-10, -100, -100, -10);
        mBarChart.setScaleEnabled(false);
        mBarChart.setClickable(false);
        mBarChart.invalidate();
    }

    private void setMonthTitle() {
        if (mTvHeaderTitle != null && mCalendarFragment != null) {
            String month = mCalendarFragment.getCurrentMonthString();
            month = month + " 정비계획 리스트";

            mTvHeaderTitle.setText(getString(R.string.repair_plan));

        }
    }

    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {

        switch (arg0.getId()) {
            case R.id.lv_progress_status:
                monthProgressAdapter.checkItem(position);
                break;

            default:
                break;
        }

    }

    @Override
    public void onDestroyView() {
        monthProgressAdapter.releaseResouces();
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        // mCalendarFragment.onDestroy();
        super.onDestroy();

        mLvProgressStatus = null;
        mTvHeaderTitle = null;

        mCalendarFragment = null;

        monthProgressAdapter = null;

    }

    @Override
    public void onCompleteDB(final String funName, int type, final Cursor cursor, String tableName) {

        // Log.i("onCompleteDB", "btn_all");
        if ("btn_all".equals(funName)) {

            tempBtnAll.clear();
            mProgressFilterMap.put("전체", "전체");
            ArrayList<String> temp = new ArrayList<String>();
            temp.add("전체");
            temp.add("전체");
            tempBtnAll.add(temp);

            if (cursor == null)
                return;

            if (cursor.isClosed()) {
                LogUtil.d("hjt", "cursor.isClosed()");
                return;
            }
            try {
                cursor.moveToFirst();
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                while (!cursor.isAfterLast()) {
                    mProgressFilterMap.put(cursor.getString(0), cursor.getString(1));
                    ArrayList<String> temp1 = new ArrayList<String>();
                    temp1.add(cursor.getString(0));
                    temp1.add(cursor.getString(1));
                    tempBtnAll.add(temp1);
                    // Log.e("mProgressFilterMap",
                    // cursor.getString(0)+"/"+cursor.getString(1));
                    cursor.moveToNext();

                }
                mProgressFilterPopup = new BaseTextPopup(mContext, mProgressFilterMap, PROGRESS_FILTER);

                cursor.close();
                // myung 20131206 ADD 홈->당월정비->처리/계획-> 완료만 리스트되지 않는 문제
                if (AllButtonChangeFlag == true)
                    clickSearch2();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (mCurrentDay.equals(funName)) {

            boolean MaintenaceAsyncFlag = mAsyncMap.containsKey(funName);

            if (MaintenaceAsyncFlag) {

                if (cursor == null) {
                    hideProgress();
                    initMaintenanceEmpty(0);
                    return;
                }

                mBaseMaintenanceModels.clear();

                Thread thread = new Thread(new Runnable() {

                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        mBaseMaintenanceModels.clear();
                        try {
                            cursor.moveToFirst();

                            // while (!cursor.isAfterLast())
                            for (int cursorCount = 0; cursorCount < cursor.getCount(); cursorCount++) {
                                cursor.moveToPosition(cursorCount);

                                String time = cursor.getString(cursor.getColumnIndex(month_colums[0]));
                                String invnr = cursor.getString(cursor.getColumnIndex(month_colums[1]));
                                String name = cursor.getString(cursor.getColumnIndex(month_colums[2]));

                                if (name == null || name.equals(" ")) {
                                    name = cursor.getString(cursor.getColumnIndex(month_colums[3]));
                                }

                                String driver_name = cursor.getString(cursor // Jonathan
                                        // 14.06.19
                                        // 추가
                                        .getColumnIndex(month_colums[3]));

                                String carname = cursor.getString(cursor.getColumnIndex(month_colums[4]));

                                String postCode = cursor.getString(cursor.getColumnIndex(month_colums[5]));
                                String city = cursor.getString(cursor.getColumnIndex(month_colums[6]));
                                String street = cursor.getString(cursor.getColumnIndex(month_colums[7]));
                                String tel = cursor.getString(cursor.getColumnIndex(month_colums[8]));
                                String progress_status = cursor.getString(cursor.getColumnIndex(month_colums[9]));
                                String day = cursor.getString(cursor.getColumnIndex(month_colums[10]));
                                String aufnr = cursor.getString(cursor.getColumnIndex(month_colums[11]));

                                String equnr = cursor.getString(cursor.getColumnIndex(month_colums[12]));
                                String ctrty = cursor.getString(cursor.getColumnIndex(month_colums[13]));
                                String drv_mob = cursor.getString(cursor.getColumnIndex(month_colums[14]));
                                String cermr = cursor.getString(cursor.getColumnIndex(month_colums[15]));
                                String mdlcd = cursor.getString(cursor.getColumnIndex(month_colums[16]));

                                String vocNum = cursor.getString(cursor.getColumnIndex(month_colums[19]));

                                String kunnr = cursor.getString(cursor.getColumnIndex(month_colums[20]));

                                String delay = "";
                                try {
                                    delay = cursor.getString(cursor.getColumnIndex(month_colums[21]));
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                String CYCMN_TX = cursor.getString(cursor.getColumnIndex(month_colums[22]));

                                String apm = cursor.getString(cursor.getColumnIndex(month_colums[23]));
                                String vbeln = cursor.getString(cursor.getColumnIndex(month_colums[24]));
                                String gubun = cursor.getString(cursor.getColumnIndex(month_colums[25]));
                                String reqno = cursor.getString(cursor.getColumnIndex(month_colums[26]));
                                String ccmrq = cursor.getString(cursor.getColumnIndex(month_colums[27]));
                                String atvyn = cursor.getString(cursor.getColumnIndex(month_colums[28]));
                                String prerq = cursor.getString(cursor.getColumnIndex(month_colums[29]));
                                String minvnr = cursor.getString(cursor.getColumnIndex(month_colums[30]));

//							LogUtil.d("hjt", "hjt delay = " + delay);
//
//							kog.e("Jonathan", "Hello Jonathan MonthProgressFragment:: " + vocNum);

                                if (time != null) {
                                    if (time.length() > 4) {
                                        time = time.substring(0, 4);

                                        String hour = time.substring(0, 2);
                                        String sec = time.substring(2, 4);
                                        time = hour + ":" + sec;
                                    }
                                }
                                String gueen2 = cursor.getString(cursor.getColumnIndex(month_colums[16]));
                                String txt30 = cursor.getString(cursor.getColumnIndex(month_colums[17]));

                                // for(int i =0; i < cursor.getCount(); i++)
                                // {
                                // cursor.moveToPosition(i);
                                // for(int c = 0; c < cursor.getColumnCount(); c++)
                                // {
                                // System.out.println("Month Name = " +
                                // cursor.getColumnName(c) +
                                // cursor.getColumnIndex(cursor.getColumnName(c)));
                                // }
                                // }

                                postCode = decrypt(month_colums[4], postCode);
                                city = decrypt(month_colums[5], city);
                                street = decrypt(month_colums[6], street);
                                MonthProgressModel md = new MonthProgressModel(decrypt(month_colums[0], time),
                                        decrypt(month_colums[2], name), decrypt(month_colums[3], driver_name), // Jonathan
                                        // 14.06.19
                                        // 추가
                                        decrypt(month_colums[1], invnr), postCode + city + street,
                                        decrypt(month_colums[7], carname), decrypt(month_colums[8], tel),
                                        decrypt(month_colums[9], progress_status), decrypt(month_colums[10], day),
                                        decrypt(month_colums[11], aufnr), decrypt(month_colums[12], equnr),
                                        decrypt(month_colums[13], ctrty), postCode, city, street,
                                        decrypt(month_colums[14], drv_mob), decrypt(month_colums[15], cermr), gueen2, txt30,
                                        mdlcd, vocNum, kunnr, delay, CYCMN_TX, apm, vbeln, gubun, reqno, ccmrq, atvyn, prerq, minvnr);

                                // MaintenanceModel md = new MaintenanceModel(time,
                                // name, invnr,
                                // postCode + city + street, carname, status);
                                if (cermr.equals(" "))
                                    mBaseMaintenanceModels.add(md);
                                // cursor.moveToNext();

                                boolean Flag = mAsyncMap.containsKey(funName);

                                if (!Flag) {
                                    return;
                                }

                            }
                            cursor.close();

                            if (mLvProgressStatus != null) {
                                mLvProgressStatus.post(new Runnable() {

                                    @Override
                                    public void run() {
                                        // TODO Auto-generated method stub
                                        monthProgressAdapter.setDataArr(mBaseMaintenanceModels);
                                        hideProgress();
                                        initMaintenanceEmpty(monthProgressAdapter.getCount());
                                    }
                                });
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            cursor.close();
                        }

                    }
                });

                thread.start();

            }
            initScroll();
            hideProgress();
        } else if (funName.equals("queryMaintenacePlan")) {
            // TODO Auto-generated method stub

            // TODO Auto-generated method stub

            if (cursor == null)
                return;

            cursor.moveToFirst();
            mRepairPlanModelArray.clear();
            mComplateVal = 0;
            mPlanVal = 0;

            RepairPlanModel repairPlanModel = new RepairPlanModel();

            int backWorkDay = -1;
            int backIndex = 0;

            while (!cursor.isAfterLast()) {

                String work = cursor.getString(0);

                int workDay = Integer.valueOf(cursor.getString(1));
                String cemer = cursor.getString(2);
                String gubun = cursor.getString(cursor.getColumnIndex(month_colums[25]));
                if ("E0004".equals(work)) {
                    mComplateVal++;
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
                    repairPlanModel = mRepairPlanModelArray.get(mRepairPlanModelArray.size() - 1);
                    boolean planFlag = true;
                    if (cemer != null)
                        if (cemer.equals(" "))
                            planFlag = false;
                    repairPlanModel.addWork(work, planFlag, gubun);
                    mRepairPlanModelArray.set(mRepairPlanModelArray.size() - 1, repairPlanModel);

                    //
                }

                backWorkDay = workDay;

                // mComplateVal = mComplateVal + repairPlanModel.getComplate();

                if (cemer != null) {
                    if (cemer.equals(" "))
                        mPlanVal++;
                }
                cursor.moveToNext();

            }

            // mPlanVal = cursor.getCount();
            cursor.close();
            // changeRepair();

            // setPlanTitle();
            //
            // maintenance_Date_Adapter.setDateList(mDayList);
            //
            // hideProgress();

            // mDayList.clear();

            ArrayList<RepairDayInfoModel> repairDayInfoModels = mDayList;
            if (mCalendarFragment != null)
                mCalendarFragment.updateDayList(repairDayInfoModels);

            queryMaintenace(mCurrentDay);

        }

    }

    private void setStringArray(Cursor cursor, ArrayList<String> array) {
        if (cursor == null)
            return;

        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {
            array.add(cursor.getString(0));
            cursor.moveToNext();

        }
        cursor.close();
    }

    private void initGetDBData() {
        String[] _whereArgs = {String.valueOf(310), String.valueOf(320), String.valueOf(330), String.valueOf(340)};
        String[] _whereCause = {"ZCODEV", "ZCODEV", "ZCODEV", "ZCODEV"};
        String[] colums = {"ZCODEVT"};

        DbQueryModel dbQueryModel = new DbQueryModel(ConnectController.O_ITAB1_TABLE_NAME, _whereCause, _whereArgs,
                colums);

        DbAsyncTask dbAsyncTask = new DbAsyncTask("btn_all", mContext, this, dbQueryModel);
        dbAsyncTask.execute(DbAsyncTask.DB_SELECT);
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.btn_all:
                clickAll();
                break;
            case R.id.btn_carnum:
                clickCarnum();
                break;

            case R.id.btn_search:
                clickSearch();
                break;

            case R.id.btn_date: // 예정일등록
                if (isNetwork())
                    clickDate();
                break;
            case R.id.btn_transfer: // 이관등록
                if (isNetwork())
                    clickTransfer();
                break;
            case R.id.btn_notimplemented: // 미실시 사유등록
                if (isNetwork())
                    clickNotimplenented();

                break;
            case R.id.btn_delete_text:
                mEtSearch.setText("");
                mBtnDelText.setVisibility(View.INVISIBLE);
                clickSearch2();

                break;
            case R.id.btn_month_voc_info: {
                String KUNNR = getSelectKunnr();
                clickVocInfo(KUNNR);
            }
            break;

            case R.id.btn_cancel_iot:
                clickIotCancel();
                break;
            default:
                break;
        }
    }

    private boolean isE0001() {

        boolean isE0001 = false;

        ArrayList<BaseMaintenanceModel> selArrayList = monthProgressAdapter.getSelectedMaintenanceModels();
        for (BaseMaintenanceModel baseMaintenanceModel : selArrayList) {
            if (baseMaintenanceModel.getProgress_status().equals("E0001")) {
                isE0001 = true;
            }
        }

        return isE0001;
    }

    private void clickNotimplenented() {

        if (isE0001()) {
            showEventPopup2(null, "예약대기 상태일 경우 미실시 사유등록은 하실 수 없습니다.");
            return;
        }
        ArrayList<String> aufnr = getSelectedAUFNR();

        if (aufnr.size() <= 0) {
            showEventPopup2(null, "미실시 사유등록은 정비대상 차량을 선택 후 가능합니다. 정비대상 차량을 선택해 주세요.");
            return;
        }
        Unimplementation_Reason_Dialog urd = new Unimplementation_Reason_Dialog(mContext, aufnr);

        // 2014.01.1.29 ypkim
        // 미실시사유 등록 후 체크박스 해제 안되는 현상 수정.
        urd.setOnDismissListener(new OnDismissListener() {

            @Override
            public void onDismiss(DialogInterface dialog) {
                // TODO Auto-generated method stub
                monthProgressAdapter.initSelectedMaintenanceArray();
            }
        });

        urd.show();
    }

    private void clickDate() {
        ArrayList<BaseMaintenanceModel> arr = monthProgressAdapter.getSelectedMaintenanceModels();
        if (arr.size() <= 0) {

            showEventPopup2(null, "예정일 변경은 정비대상 차량을 선택후 가능합니다. 정비대상 차량을 선택해주세요.");
        } else {
            for (BaseMaintenanceModel baseMaintenanceModel : arr) {
                if (baseMaintenanceModel.getProgress_status().equals("E0004")
                        || baseMaintenanceModel.getProgress_status().equals("E0005")) {

                    showEventPopup2(null, "예정일을 변경 할 수 없는 차량이 선택되었습니다. 다시 확인하여 주십시요.");

                    // showEventPopup2(null,
                    // "완료된 정비차량이 선택 되었습니다. 정비 완료된 차량은 예정일을 변경 할 수 없습니다.");
                    return;
                }
            }
            movePlan(arr);
        }

    }

    private void clickTransfer() {

        String day = CommonUtil.getCurrentDay().substring(6);
        int today = Integer.parseInt(day);

        // myung 20131129 DELETE
        // if (today > 25) {
        // showEventPopup2(null, "26일 이후는 이관할 수 없습니다.");
        //
        // return;
        // }

        ArrayList<BaseMaintenanceModel> arr = monthProgressAdapter.getSelectedMaintenanceModels();

        // myung 20131227 ADD
        // int nCurrentDate = Integer.parseInt(arr.get(0).getDay());
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
        Calendar rightNow = Calendar.getInstance();
        int nCurrentDate = Integer.parseInt(formatter.format(rightNow.getTime()));

        ArrayList<String> arryOverGueen2 = new ArrayList<String>();

        for (int i = 0; i < arr.size(); i++) {
            int nGueen2 = Integer.parseInt(arr.get(i).getGUEEN2());
            if (nGueen2 < nCurrentDate) {
                arryOverGueen2.add(arr.get(i).getCarNum());
                // Log.e("nGueen2/nCurrentDate", "" + nGueen2 + "/" +
                // nCurrentDate);
            }

        }

        // myung 20131227 ADD 계획 > 계약 완료 건 표기 및 계약 완료건 이관 시 이관 안되도록 메시지 처리.
        if (arr.size() <= 0) {
            showEventPopup2(null, "이관등록은 이관대상 차량을 선택후 가능합니다. 이관대상 차량을 선택해주세요.");
        } else if (arryOverGueen2.size() > 0) {
            // Log.e("arryOverGueen2.size()", "" + arryOverGueen2.size());
            String temp = "";
            for (int i = 0; i < arryOverGueen2.size(); i++) {
                if (i > 0)
                    temp += "/ ";
                temp += arryOverGueen2.get(i) + " ";

            }

            showEventPopup2(new OnEventOkListener() {

                @Override
                public void onOk() {
                    // TODO Auto-generated method stub

                }
            }, temp + "차량은 \n계약종료되어 이관등록이 불가합니다.");
        } else {
            if (mTransferManageFragment == null)
                mTransferManageFragment = new TransferManageFragment(MonthProgressFragment.class.getName(), null, this,
                        mContext);

            mTransferManageFragment.setmMaintenanceModelArray(arr);
            //
            // dialogFragment.getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
            // myung 20131120 UPDATE 2560 대응
            // int tempX = 1060;
            // int tempY = 744;
            // if (DEFINE.DISPLAY.equals("2560"))
            // {
            // tempX *= 2;
            // tempY *= 2;
            // }

            // myung 20131230 ADD 체크 초기화
            mTransferManageFragment.setOnDismissDialogFragment(new OnDismissDialogFragment() {

                @Override
                public void onDismissDialogFragment() {
                    // TODO Auto-generated method stub
                    monthProgressAdapter.initSelectedMaintenanceArray();
                }
            });

            mTransferManageFragment.show(getChildFragmentManager(), null);// ,
            // tempX,
            // tempY);
        }
    }

    private void clickSearch() {

        InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(mEtSearch.getWindowToken(), 0);

        int standardLength = 0;

        String wanningText = "";
        String strInfoType = "";

        if (mInfoFilterType == 0) {
            standardLength = 4;
            strInfoType = "carnum";
            wanningText = "차량번호 검색은 4글자 이상입력을 해주셔야 합니다.";
        } else if (mInfoFilterType == 1) {
            standardLength = 2;
            strInfoType = "name";
            wanningText = "고객명 검색은 2글자 이상입력을 해주셔야 합니다.";
        }

        int textLength = mEtSearch.getText().toString().length();
        if (standardLength <= textLength) {

            String text = mEtSearch.getText().toString();
            String text1 = "";
            for (int i = 0; i < tempBtnAll.size(); i++) {
                if (mAllButton.getText().toString().equals(tempBtnAll.get(i).get(0))) {
                    text1 = tempBtnAll.get(i).get(1);
                }
            }
            monthProgressAdapter.setInfoText(text);
            monthProgressAdapter.setProgressType(text1);
            monthProgressAdapter.setInfoType(strInfoType);
            monthProgressAdapter.filtering();
            monthProgressAdapter.setMaintenanceType("");
            initMaintenanceEmpty(monthProgressAdapter.getCount());
        } else {

            if (wanningText.length() > 0)
                showEventPopup2(null, wanningText);
        }

    }

    private void clickSearch2() {
        String text = mEtSearch.getText().toString();
        // String text1 = mAllButton.getTag().toString();
        String text1 = "";
        for (int i = 0; i < tempBtnAll.size(); i++) {
            if (mAllButton.getText().toString().equals(tempBtnAll.get(i).get(0))) {
                text1 = tempBtnAll.get(i).get(1);
            }
        }
        monthProgressAdapter.setInfoText(text);
        monthProgressAdapter.setProgressType(text1);
        monthProgressAdapter.setInfoType("carnum");
        monthProgressAdapter.filtering();
        monthProgressAdapter.setMaintenanceType("");
        initMaintenanceEmpty(monthProgressAdapter.getCount());

    }

    private void clickVocInfo(String kunnr) {
        if (kunnr == null) {
            showEventPopup2(null, "VOC 내역조회는 고객 한명만 가능합니다.");
            return;
        }

        if (kunnr.equals("-1")) {
            showEventPopup2(null, "VOC 내역조회할 고객을 선택하세요.");
            return;
        }

        isShowVocView = true;

        Intent intent = new Intent(getActivity(), VocInfoActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.putExtra(VocInfoActivity.VOC_KUNNR, kunnr);
        startActivity(intent);
    }

    private void clickIotCancel() {
        ArrayList<BaseMaintenanceModel> arr = monthProgressAdapter.getSelectedMaintenanceModels();
        if (arr.size() <= 0) {
            showEventPopup2(null, "IoT 정비취소는 IoT 정비대상 차량을 선택후 가능합니다. IoT 정비대상 차량을 선택해주세요.");
            return;
        }

        if (arr.size() > 1) {
            showEventPopup2(null, "IoT 정비취소는 단건만 진행 가능합니다. 한 건만 선택해주세요.");
            return;
        }


        // iot 건 중에서도 상태에 따라 취소가 불가능한 건이 있지만 그 상황은 rfc 에서 return text 로 내려주기 때문에 여기서 막을 필요는 없어보임.
        if (arr.get(0).getATVYN().equals("A")) {
            IoTCancelPopup popup = new IoTCancelPopup(mContext, arr.get(0).getREQNO());
            popup.show();
        } else {
            Toast.makeText(mContext, "IoT 건을 선택해주세요.", Toast.LENGTH_SHORT).show();
        }

    }

    private void showFilterPopup(BaseTextPopup popup, View anchor) {

        popup.setOnSelectedItem(this);
        popup.show(anchor);

    }

    @Override
    public void onSelectedItem(int position, String popName) {

        LinkedHashMap<String, String> linkedHashMap = null;

        if (popName.equals(PROGRESS_FILTER)) {

            linkedHashMap = mProgressFilterMap;
            String item = getSeletedItem(linkedHashMap, position, mAllButton);
            monthProgressAdapter.setProgressType(item);

            LinkedHashMap<String, String> linkedHashMap2 = mInfoFilterMap2;
            String item2 = getSeletedItem(linkedHashMap2, getfiliter2, btn_filiter2);
            monthProgressAdapter.setMaintenanceType(item2);

            initMaintenanceEmpty(monthProgressAdapter.getCount());

        } else if (popName.equals(INFO_FILTER)) {
            if (mInfoFilterType != position) {
                mEtSearch.setText("");

                InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInputFromInputMethod(mEtSearch.getApplicationWindowToken(), InputMethodManager.SHOW_FORCED);
            }
            mInfoFilterType = position;
            linkedHashMap = mInfoFilterMap;
            String item = getSeletedItem(linkedHashMap, position, mBtnInfo);
            monthProgressAdapter.setInfoType(item);
            monthProgressAdapter.setMaintenanceType("");

            initMaintenanceEmpty(monthProgressAdapter.getCount());
        } else if (popName.equals(MAINTENANCE_FILTER)) {
            linkedHashMap = mInfoFilterMap2;
            getfiliter2 = position;
            String item = getSeletedItem(linkedHashMap, getfiliter2, btn_filiter2);
            monthProgressAdapter.setMaintenanceType(item);
        }
        initScroll();
    }

    private void clickAll() {
        AllBtnBuffer = mAllButton.getText().toString();
        showFilterPopup(mProgressFilterPopup, mAllButton);
    }

    private void clickCarnum() {
        kog.e("KDH", "clickCarnum");
        showFilterPopup(mInfoFilterPopup, mBtnInfo);
    }

    @Override
    protected void updateRepairPlan() {
        // KtRentalApplication.getInstance().queryMaintenacePlan(
        // new OnCalendarComplate() {
        //
        // @Override
        // public void onCalendarComplate() {
        // // TODO Auto-generated method stub
        // ArrayList<RepairDayInfoModel> repairDayInfoModels =
        // KtRentalApplication
        // .getInstance().getDayList();
        // if (mCalendarFragment != null)
        // mCalendarFragment
        // .updateDayList(repairDayInfoModels);
        //
        // queryMaintenace(mCurrentDay);
        // }
        // });
        mDayList.clear();
        // Log.d("HONG",
        // "queryMaintenacePlan DbAsyncResLintener " + mDayList.size());
        CalendarController mCalendarManager = new CalendarController(CalendarController.TYPE_SHOW_OTHERMONTH);
        for (DayInfoModel dayInfoModel : mCalendarManager.getDayInfoArrayList()) {
            mDayList.add(new RepairDayInfoModel(dayInfoModel));
        }

        KtRentalApplication.getInstance().queryMaintenacePlan(this);

    }

    // @Override
    // public void OnSeletedItem(DayInfoModel item) {
    // // TODO Auto-generated method stub
    // String currentDay = item.getCurrentDay();
    //
    // showProgress();
    //
    // if (mCurrentDay != null) {
    // DbAsyncTask dbAsyncTask = mAsyncMap.get(mCurrentDay);
    // if (dbAsyncTask != null) {
    // mAsyncMap.remove(mCurrentDay);
    // dbAsyncTask.cancel(true);
    // hideProgress();
    //
    // }
    // }
    // monthProgressAdapter.initSelectedMaintenanceArray();
    // queryMaintenace(currentDay);
    // }

    @Override
    protected void initSelectedMaintenanceArray(String currentDay) {
        // TODO Auto-generated method stub
        monthProgressAdapter.initSelectedMaintenanceArray();
    }

    @Override
    protected void queryMaintenace(String currentDay) {
        // TODO Auto-generated method stub

        mCurrentDay = currentDay;

        showProgress("순회 정비계획을 조회 중입니다.");

        String[] _whereCause = null;

        String[] _whereArgs = null;

        if (currentDay.length() > 0) {
            _whereCause = new String[1];
            _whereCause[0] = "GSTRS";
            _whereArgs = new String[1];
            _whereArgs[0] = currentDay;
        }

        String[] colums = month_colums;

        DbQueryModel dbQueryModel = new DbQueryModel(ConnectController.REPAIR_TABLE_NAME, _whereCause, _whereArgs,
                colums);

        dbQueryModel.setOrderBy("GSTRS asc");
        dbQueryModel.setOrderBy(
                "gubun DESC, GSTRS asc, GSUZS ASC, case CCMSTS   when 'E0001' then '1' when 'E0002' then '' when 'E0003' then '3' when 'E0004' then '4' else '9' end ");

        DbAsyncTask dbAsyncTask = new DbAsyncTask(currentDay, mContext, this, dbQueryModel);
        mAsyncMap.put(currentDay, dbAsyncTask);
        mCurrentDay = currentDay;

        dbAsyncTask.execute(DbAsyncTask.DB_SELECT);
    }

    private ArrayList<String> getSelectedAUFNR() {
        ArrayList<String> array = new ArrayList<String>();

        ArrayList<BaseMaintenanceModel> selArrayList = monthProgressAdapter.getSelectedMaintenanceModels();
        for (BaseMaintenanceModel baseMaintenanceModel : selArrayList) {
            array.add(baseMaintenanceModel.getAUFNR());
        }

        return array;
    }

    private void initQueryFilter() {

        String[] _whereArgs2 = {"PM015"};
        String[] _whereCause2 = {"ZCODEH"};

        String[] colums = {"ZCODEVT", "ZCODEV"};

        DbQueryModel dbQueryModel = new DbQueryModel(ConnectController.O_ITAB1_TABLE_NAME, _whereCause2, _whereArgs2,
                colums);

        DbAsyncTask dbAsyncTask = new DbAsyncTask("btn_all", mContext, this, dbQueryModel);
        dbAsyncTask.execute(DbAsyncTask.DB_SELECT);
    }

    private String getSeletedItem(LinkedHashMap<String, String> linkedHashMap, int position, Button btn) {

        String value = null;
        Iterator<String> it = linkedHashMap.keySet().iterator();

        int i = 0;

        while (it.hasNext()) {
            String strKey = "";
            String strValue = "";

            strKey = it.next();
            strValue = linkedHashMap.get(strKey);

            if (i == position) {
                value = strValue;
                if (btn != null)
                    btn.setText(strKey);
                break;
            }

            i++;
        }

        return value;
    }

    /**
     * 고객 정보를 반환
     *
     * @return
     */
    private String getSelectKunnr() {
        if (monthProgressAdapter.getSelectedMaintenanceModels().size() == 0) {
            return "-1";
        }

        if (!(monthProgressAdapter.getSelectedMaintenanceModels().size() == 1)) {
            for (BaseMaintenanceModel baseMaintenanceModel : monthProgressAdapter.getSelectedMaintenanceModels()) {
                System.out.println("KUNNR = " + baseMaintenanceModel.getKUNNR());
            }
            return null;
        }

        return monthProgressAdapter.getSelectedMaintenanceModels().get(0).getKUNNR();
    }

    private void initInfoFilter() {
        mInfoFilterMap.put(FILTER_INFO_ARRAY[0], "carnum");
        mInfoFilterMap.put(FILTER_INFO_ARRAY[1], "name");

        mInfoFilterPopup = new BaseTextPopup(mContext, mInfoFilterMap, INFO_FILTER);

        mInfoFilterMap2.put(FILTER_INFO_ARRAY2[0], "name");
        mInfoFilterMap2.put(FILTER_INFO_ARRAY2[1], "addr");

        mMaintenanceFilterPopup = new BaseTextPopup(mContext, mInfoFilterMap2, MAINTENANCE_FILTER);

    }

    @Override
    protected void movePlan(ArrayList<BaseMaintenanceModel> models) {
        boolean bool = true;
        if (models.size() > 1) {
            for (int i = 0; i < models.size(); i++) {
                if (!models.get(i).getATVYN().trim().isEmpty()) {
                    bool = false;
                    EventPopupC popupC = new EventPopupC(mContext);
                    popupC.show("iot 정비는 단건 예정일 변경만 가능합니다.");
                }
            }

            if (bool) {
                Duedate_Dialog dd = new Duedate_Dialog(mContext, models, models.get(0).getATVYN(), models.get(0).getREQNO());
                // myung 20131230 ADD 체크초기화
                dd.setOnDismissListener(new OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        monthProgressAdapter.initSelectedMaintenanceArray();
                    }
                });
                dd.show();
            }
        } else {
            // iot 이든 일반이든 무조건 1건이다.
            // iot 와 일반의 예정일 변경 RFC 가 상이하기 때문에 생성자 파라미터에 구분자를 추가함, // yunseung
            Duedate_Dialog dd = new Duedate_Dialog(mContext, models, models.get(0).getATVYN(), models.get(0).getREQNO());
            // myung 20131230 ADD 체크초기화
            dd.setOnDismissListener(new OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    monthProgressAdapter.initSelectedMaintenanceArray();
                }
            });
            dd.show();
        }
    }

    @Override
    public void onDismissDialogFragment() {
        // TODO Auto-generated method stub
        // TODO Auto-generated method stub
        monthProgressAdapter.initSelectedMaintenanceArray();
    }

    @Override
    public void onClickCalendarTitle() {
        // TODO Auto-generated method stub

        monthProgressAdapter.initSelectedMaintenanceArray();
        // monthProgressAdapter.initFiltering();
        monthProgressAdapter.setInfoType("");
        // monthProgressAdapter.filtering();
        mCalendarFragment.initSelectedPosition();
        monthProgressAdapter.setMaintenanceType("");
        mEtSearch.setText("");
        queryMaintenace("");
    }

    @Override
    protected void initScroll() {
        // TODO Auto-generated method stub
        // mLvProgressStatus.smoothScrollToPosition(0);
        if (mLvProgressStatus != null)
            mLvProgressStatus.setSelectionFromTop(0, 0);
    }

    private void initMaintenanceEmpty(int size) {
        if (size > 0) {
            mEmptyView.setVisibility(View.GONE);
            mLvProgressStatus.setVisibility(View.VISIBLE);
        } else {
            mEmptyView.setVisibility(View.VISIBLE);
            mLvProgressStatus.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClickSync() {
        // TODO Auto-generated method stub

    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        if (!hidden) {

            mCalendarFragment.initSelectedPosition();
            // initQueryFilter();
            initMonthProgress();
            // myung 20131230 월간리스트 체크 초기화
            monthProgressAdapter.initSelectedMaintenanceArray();
        }

        super.onHiddenChanged(hidden);
    }

    @Override
    public void onResume() {

        super.onResume();
    }

    private void initMonthProgress() {
        queryMaintenace("");

        setMonthTitle();

        mTvComplate.setText(getComplate());
        mTvComplate2.setText(getComplate2());
        mTvComplate3.setText(getComplate3());
        mTvPlan.setText(getPlan());
        mTvPlan2.setText(getPlan2());
        mTvPlan3.setText(getPlan3());
        mTvNotImplemented.setText(getNotImplemetned());
        mTvNotLongImplemented.setText(getNotLongImplemetned());

        int val1 = Integer.parseInt(getPlan()) == 0 ? 0 : (int)Math.round((Double.parseDouble(getComplate()) / Double.parseDouble(getPlan())) * 100);
        int val2 = Integer.parseInt(getPlan2()) == 0 ? 0 : (int)Math.round((Double.parseDouble(getComplate2()) / Double.parseDouble(getPlan2())) * 100);
        int val3 = Integer.parseInt(getPlan3()) == 0 ? 0 : (int)Math.round((Double.parseDouble(getComplate3()) / Double.parseDouble(getPlan3())) * 100);

        drawBarGraph(val1, val2, val3);

        int complate = Integer.valueOf(getComplate());
        int plan = Integer.valueOf(getPlan());

        try {
            int degree = 100 * complate / plan;

//			mWorkProgress.setDegree(degree);
        } catch (ArithmeticException e) {
            // TODO: handle exception
        }

    }


    private class MyValueFormatter implements IValueFormatter {

        private DecimalFormat mFormat;

        public MyValueFormatter() {
            mFormat = new DecimalFormat("###");
        }

        @Override
        public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
            // write your logic here
            return mFormat.format(value) + "%";
        }
    }
}
