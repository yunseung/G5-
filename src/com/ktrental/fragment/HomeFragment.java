package com.ktrental.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.ktrental.R;
import com.ktrental.adapter.HomeNoticeAdapter;
import com.ktrental.calendar.CalendarController;
import com.ktrental.calendar.DayInfoModel;
import com.ktrental.cm.connect.ConnectController;
import com.ktrental.cm.connect.Connector.ConnectInterface;
import com.ktrental.cm.db.DbAsyncTask;
import com.ktrental.cm.db.DbAsyncTask.DbAsyncResLintener;
import com.ktrental.common.DEFINE;
import com.ktrental.common.KtRentalApplication;
import com.ktrental.fragment.CalendarFragment.OnCalendarListener;
import com.ktrental.fragment.HomeCardFragment.OnCardClick;
import com.ktrental.model.BaseMaintenanceModel;
import com.ktrental.model.DbQueryModel;
import com.ktrental.model.HomeMaintenanceModel;
import com.ktrental.model.HomeNoticeModel;
import com.ktrental.model.LoginModel;
import com.ktrental.model.O_ITAB1;
import com.ktrental.model.RepairDayInfoModel;
import com.ktrental.model.RepairPlanModel;
import com.ktrental.model.TableModel;
import com.ktrental.popup.MovementDialog;
import com.ktrental.popup.MovementDialog.OnSelectedSave;
import com.ktrental.util.CommonUtil;
import com.ktrental.util.OnChangeFragmentListener;
import com.ktrental.util.ResultController;
import com.ktrental.util.ResultController.OnResultCompleate;
import com.ktrental.util.SharedPreferencesUtil;
import com.ktrental.util.kog;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

/**
 * 홈 <br/>
 * 실제 메인화면이다.
 * 
 * 
 * 
 * @author Hong
 * @since 2013.07.12
 */
public class HomeFragment extends BaseRepairFragment implements OnClickListener, OnCalendarListener, ConnectInterface, DbAsyncResLintener,
        OnCardClick
{

    private Context                      mContext;
    private CalendarFragment             mCalendarFragment;

    private HomeCardFragment             mHomeCardFragment;

    private TextView                     mTvTodayPlanLeft, mTvTodayPlanLeft2, mTvTodayPlanLeft3;
    private TextView                     mTvTodayPlanRight, mTvTodayPlanRight2, mTvTodayPlanRight3;
    private TextView                     mTvTodayOrder, mTvTodayOrder2, mTvTodayOrder3;
    private TextView                     mTvTodayOrderWait, mTvTodayOrderWait2, mTvTodayOrderWait3;
    private TextView                     mTvTodayPossetion, mTvTodayPossetion2, mTvTodayPossetion3;
    private TextView                     mTvTodayEmergency, mTvTodayEmergency2, mTvTodayEmergency3;
    private TextView                     mTvMontyPlanLeft, mTvMontyPlanLeft2, mTvMontyPlanLeft3;
    private TextView                     mTvMontyPlanRight, mTvMontyPlanRight2, mTvMontyPlanRight3;
    private TextView                     mTvDegree, mTvDegree2, mTvDegree3;
    private TextView                     mTvMonthlyEmergency, mTvMonthlyEmergency2, mTvMonthlyEmergency3;
    private Button                       mBtnSend;
    private LinearLayout                 mLlTodayPlan;
    private LinearLayout                 mLlTodayOrder;
    private LinearLayout                 mLlTodayOrderWait;
    private LinearLayout                 mLlTodayPossetion;
    private LinearLayout                 mLlMonthPlan;
    private LinearLayout                 mLlMonthPercent;

    private ImageView                    mIvEmpty;
    private TextView                     mTvComplateMessage;
    private FrameLayout                  mFlEmpty;
    
    ConnectController mCc;
    

    // private ArrayList<NoticeModel> mNoticeModels = new ArrayList<NoticeModel>();

    // 최종 정비이력 MATAPL / O_ITAB4 (한줄로 표현)
    // 미처리건 계획건 - (완료 + 사유등록건)
    // 사유등록건 미실시 사유 등록 건
    // 당월 미처리건
    private String[]                     maintenace_colums = {
            DEFINE.GSUZS, DEFINE.INVNR, DEFINE.KUNNR_NM, DEFINE.DRIVN, DEFINE.MAKTX, DEFINE.CCMRQ, DEFINE.POST_CODE, DEFINE.CITY, DEFINE.STREET,
            DEFINE.DRV_TEL, DEFINE.CCMSTS, DEFINE.GSTRS, DEFINE.AUFNR, DEFINE.EQUNR, DEFINE.CTRTY, DEFINE.DRV_MOB, DEFINE.NIELS_NM, DEFINE.LDATE,
            DEFINE.REPOIL, DEFINE.CEMER,  DEFINE.VOCNUM, DEFINE.KUNNR, DEFINE.DELAY, DEFINE.CYCMN_TX, DEFINE.VBELN, DEFINE.GUBUN, DEFINE.REQNO, DEFINE.ATVYN,
            DEFINE.PRERQ, DEFINE.MINVNR};

    private HashMap<String, DbAsyncTask> mAsyncMap         = new HashMap<String, DbAsyncTask>();

    private String                       mCurrentDay       = "";

    public static final String           KEY_PROCESS_START = "key_process_start";

    private ListView                     mLvNotice;
    private HomeNoticeAdapter            mHomeNoticeAdapter;

    // private View RootView;
    private OnChangeFragmentListener     mOnChangeFragmentListener;

    private int                          mComplate         = 0;

    private Button                       mBtnStart;
    private Button                       mBtnNoticeMore;
    private boolean                      mStartFlag        = true;

    SharedPreferencesUtil                shareU;

    public HomeFragment()
    {
        super();
    }

    @SuppressLint("ValidFragment")
    public HomeFragment(String className, OnChangeFragmentListener changeFragmentListener)
    {
        super(className, changeFragmentListener);
        // TODO Auto-generated constructor stub
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        // setRetainInstance(true);
        mCalendarFragment = new CalendarFragment(CalendarFragment.class.getName(), null, false);

        mHomeCardFragment = new HomeCardFragment(this);

        shareU = SharedPreferencesUtil.getInstance();
        mStartFlag = shareU.getBoolean("key_process_start", true);
        
        
        mCc = new ConnectController(this, mContext);
        
    }

    @Override
    public void onAttach(Activity activity)
    {

        mContext = activity;

        super.onAttach(activity);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {

        mRootView = inflater.inflate(R.layout.home_layout, null);
        getChildFragmentManager().beginTransaction().add(R.id.ll_home_calendar, mCalendarFragment, "ll_home_calendar").commit();

        getChildFragmentManager().beginTransaction().add(R.id.ll_home_card, mHomeCardFragment, "ll_home_card").commit();

        initViewSettings(mRootView);

        queryBaseGroup();

        return mRootView;
    }

    private void initViewSettings(View view)
    {

        mCalendarFragment.setOnCalendarListener(this);

        mTvTodayPlanLeft = (TextView) mRootView.findViewById(R.id.tv_today_plan_left);
        mTvTodayPlanLeft2 = (TextView) mRootView.findViewById(R.id.tv_today_plan_left2);
        mTvTodayPlanLeft3 = (TextView) mRootView.findViewById(R.id.tv_today_plan_left3);
        mTvTodayPlanRight = (TextView) mRootView.findViewById(R.id.tv_today_plan_right);
        mTvTodayPlanRight2 = (TextView) mRootView.findViewById(R.id.tv_today_plan_right2);
        mTvTodayPlanRight3 = (TextView) mRootView.findViewById(R.id.tv_today_plan_right3);
        mLlTodayPlan = (LinearLayout) mRootView.findViewById(R.id.ll_today_plan);
        mLlTodayPlan.setOnClickListener(this);
        mTvTodayOrder = (TextView) mRootView.findViewById(R.id.tv_today_order);
        mTvTodayOrder2 = (TextView) mRootView.findViewById(R.id.tv_today_order2);
        mTvTodayOrder3 = (TextView) mRootView.findViewById(R.id.tv_today_order3);
        mLlTodayOrder = (LinearLayout) mRootView.findViewById(R.id.ll_today_order);
        mLlTodayOrder.setOnClickListener(this);
        mTvTodayOrderWait = (TextView) mRootView.findViewById(R.id.tv_today_order_waitting);
        mTvTodayOrderWait2 = (TextView) mRootView.findViewById(R.id.tv_today_order_waitting2);
        mTvTodayOrderWait3 = (TextView) mRootView.findViewById(R.id.tv_today_order_waitting3);
        mLlTodayOrderWait = (LinearLayout) mRootView.findViewById(R.id.ll_today_order_waitting);
        mLlTodayOrderWait.setOnClickListener(this);
        mTvTodayPossetion = (TextView) mRootView.findViewById(R.id.tv_today_possetion);
        mTvTodayPossetion2 = (TextView) mRootView.findViewById(R.id.tv_today_possetion2);
        mTvTodayPossetion3 = (TextView) mRootView.findViewById(R.id.tv_today_possetion3);
        mLlTodayPossetion = (LinearLayout) mRootView.findViewById(R.id.ll_today_possetion);
        mLlTodayPossetion.setOnClickListener(this);
        mTvTodayEmergency = (TextView) mRootView.findViewById(R.id.tv_today_emergency);
        mTvTodayEmergency2 = (TextView) mRootView.findViewById(R.id.tv_today_emergency2);
        mTvTodayEmergency3 = (TextView) mRootView.findViewById(R.id.tv_today_emergency3);

        mTvMontyPlanLeft = (TextView) mRootView.findViewById(R.id.tv_month_plan_left);
        mTvMontyPlanLeft2 = (TextView) mRootView.findViewById(R.id.tv_month_plan_left2);
        mTvMontyPlanLeft3 = (TextView) mRootView.findViewById(R.id.tv_month_plan_left3);
        mTvMontyPlanRight = (TextView) mRootView.findViewById(R.id.tv_month_plan_right);
        mTvMontyPlanRight2 = (TextView) mRootView.findViewById(R.id.tv_month_plan_right2);
        mTvMontyPlanRight3 = (TextView) mRootView.findViewById(R.id.tv_month_plan_right3);
        mLlMonthPlan = (LinearLayout) mRootView.findViewById(R.id.ll_month_plan);
        mLlMonthPlan.setOnClickListener(this);
        mLlMonthPercent = (LinearLayout) mRootView.findViewById(R.id.ll_month_percent);
        mLlMonthPercent.setOnClickListener(this);
        mTvDegree = (TextView) mRootView.findViewById(R.id.tv_percent);
        mTvDegree2 = (TextView) mRootView.findViewById(R.id.tv_percent2);
        mTvDegree3 = (TextView) mRootView.findViewById(R.id.tv_percent3);
        mTvMonthlyEmergency = (TextView) mRootView.findViewById(R.id.tv_monthly_emergency);
        mTvMonthlyEmergency2 = (TextView) mRootView.findViewById(R.id.tv_monthly_emergency2);
        mTvMonthlyEmergency3 = (TextView) mRootView.findViewById(R.id.tv_monthly_emergency3);

//        mBtnSend = (Button) mRootView.findViewById(R.id.btn_send_result);
//        mBtnSend.setOnClickListener(this);
//        // Jonathan 15.02.11 미전송건 안한다고 해서 gone으로 해놓는다.
//        mBtnSend.setVisibility(View.GONE);
        mLvNotice = (ListView) mRootView.findViewById(R.id.lv_notice);
        mHomeNoticeAdapter = new HomeNoticeAdapter(mContext);
        mLvNotice.setAdapter(mHomeNoticeAdapter);
        mLvNotice.setOnItemClickListener(new OnItemClickListener()
        {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3)
            {
                kog.e("Jonathan", "Jonathan" + position + "누름");
                moveNotice();

            }
        });

        mIvEmpty = (ImageView) mRootView.findViewById(R.id.iv_empty);
        mTvComplateMessage = (TextView) mRootView.findViewById(R.id.tv_complate_message);
        mFlEmpty = (FrameLayout) mRootView.findViewById(R.id.fl_empty);

        mBtnNoticeMore = (Button) mRootView.findViewById(R.id.btn_notice_more);
        mBtnNoticeMore.setOnClickListener(this);

        mBtnStart = (Button) mRootView.findViewById(R.id.btn_start);
        mBtnStart.setOnClickListener(this);

        if (!mStartFlag)
        {
            mBtnStart.setBackgroundResource(R.drawable.main_btn_end_n);
        }
        else
        {
            mBtnStart.setBackgroundResource(R.drawable.main_btn_start_n);
        }
        // mBtnStart.setOnCheckedChangeListener(new OnCheckedChangeListener() {
        //
        // @Override
        // public void onCheckedChanged(CompoundButton buttonView,
        // boolean isChecked) {
        // // TODO Auto-generated method stub
        // if (isChecked)
        // clickMovementDialog("출발");
        // else
        // clickMovementDialog("도착");
        // }
        // });

        // initDummy();
        initNoticeData();

        ArrayList<RepairDayInfoModel> mDayList = new ArrayList<RepairDayInfoModel>();

        CalendarController mCalendarManager = new CalendarController(CalendarController.TYPE_SHOW_OTHERMONTH);

        ArrayList<RepairDayInfoModel> repairDayInfoModels = KtRentalApplication.getInstance().getDayList();

        kog.e("KDH", "repairDayInfoModels.size() = " + repairDayInfoModels.size());

        // mCalendarFragment = new CalendarFragment();
    }

    private void clickMovementDialog(final String step)
    {

        // shareU.setBoolean("key_process_start", mStartFlag);

        LoginModel model = KtRentalApplication.getLoginModel();
        MovementDialog mMovementDialog = new MovementDialog(mContext, model.getEqunr(), model.getPernr(), " ", model.getFUELNM(), model.getEname(),
                "1", step, model.getInvnr(), new OnSelectedSave()
                {

                    @Override
                    public void onSelectedSave(String step)
                    {
                        // TODO Auto-generated method stub

                        if (step.equals("출발"))
                        {
                            mStartFlag = false;
                            mBtnStart.setBackgroundResource(R.drawable.main_btn_end_n);
                        }
                        else
                        {
                            mStartFlag = true;
                            mBtnStart.setBackgroundResource(R.drawable.main_btn_start_n);
                        }
                    }
                });
        mMovementDialog.show();
    }

    // private void initDummy() {
    // ArrayList<HomeNoticeModel> arrayList = new ArrayList<HomeNoticeModel>();
    // HomeNoticeModel model = new HomeNoticeModel("2013년 긴급공지", "2013.05.01");
    // arrayList.add(model);
    // HomeNoticeModel model2 = new HomeNoticeModel("정비시주의사항 안내입니다.",
    // "2013.04.23");
    // arrayList.add(model2);
    // HomeNoticeModel model3 = new HomeNoticeModel("2013년 세번째줄 긴급공지",
    // "2013.04.14");
    // arrayList.add(model3);
    // HomeNoticeModel model4 = new HomeNoticeModel("2013년 네번째줄 긴급공지",
    // "2013.04.01");
    // arrayList.add(model4);
    // mHomeNoticeAdapter.setData(arrayList);
    //
    // }

    // myung 20131128 ADD 공지사항
    private void initNoticeData()
    {
    	
    	Calendar cal = Calendar.getInstance();
		int year = cal.get(Calendar.YEAR);
		int month = cal.get(Calendar.MONTH) + 1;
		int day = cal.get(Calendar.DAY_OF_MONTH);
		String month_str = String.format("%02d", month);
		String day_str = String.format("%02d", day);
		// DATE2 = year + month_str + day_str;
		String DATE2 = year + "." + month_str + "." + day_str;
		
//		  btn_notice_endt.setText(year + "." + month_str + "." + day_str);// 오늘날짜
		cal.set(Calendar.DAY_OF_MONTH, 1);
		day = cal.get(Calendar.DAY_OF_MONTH);
		String day_str_1 = String.format("%02d", day);
		// DATE1 = year + month_str + day_str_1;
//		btn_notice_bgdt.setText(year + "." + month_str + "." + day_str_1);// 이달의첫날
		String DATE1 = year + "." + month_str + "." + day_str_1;
    	
    	
        ConnectController connectController = new ConnectController(this, mContext);
        connectController.getZMO_1060_RD06("1", "10", "M", DATE1, DATE2);
    }
    
    
    
	

    @Override
    public void onClick(View view)
    {
        // TODO Auto-generated method stub
        switch (view.getId())
        {
            case R.id.btn_send_result:
                // clickSend();
                // 2014-03-17 KDH 예전에는 미전송건을 전송했는데 지금은 팝업으로 대체해서 거기서 보낸다.
                ResultSendFragment fragment = new ResultSendFragment();
                // myung 20131120 UPDATE 2560대응
                int tempX = 1060;
                int tempY = 614;
                if (DEFINE.DISPLAY.equals("2560"))
                {
                    tempX *= 2;
                    tempY *= 2;
                }
                fragment.show(getChildFragmentManager(), "", tempX, tempY);
                // mDrawerLayout.closeDrawer(mMenuView);
                break;
            case R.id.ll_today_plan:
                clickTodayPlan();
                break;
            case R.id.ll_today_order:
                clickOrder();
                break;
            case R.id.ll_today_order_waitting:
                clickOrderWait();
                break;
            case R.id.ll_today_possetion:
                clickPossetion();
                break;
            case R.id.ll_month_plan:
                clickMonthPlan();
                break;
            case R.id.ll_month_percent:
                clickMonthPercent();
                break;
            case R.id.btn_notice_more:
                clickNoticeMore();
                break;
            case R.id.btn_start:
                if (mStartFlag)
                    clickMovementDialog("출발");
                else
                    clickMovementDialog("도착");
                break;

            default:
                break;
        }
    }

    private void clickNoticeMore()
    {
        changfragment(new NoticeFragment(NoticeFragment.class.getName(), mOnChangeFragmentListener, true));
    }

    private void clickMonthPercent()
    {
        changfragment(new MaintenancePerpomanceFragment(MaintenancePerpomanceFragment.class.getName(), mOnChangeFragmentListener,
                CommonUtil.getCurrentMonth(), true));
    }

    private void clickMonthPlan()
    {

        changfragment(new MonthProgressFragment(MonthProgressFragment.class.getName(), mOnChangeFragmentListener, true));
    }

    private void clickTodayPlan()
    {
        moveMaintenance(mCurrentDay, "E0004", "완료");
    }

    private void clickOrder()
    {
        moveMaintenance(mCurrentDay, "E0002", "예약");
    }

    private void clickOrderWait()
    {
        moveMaintenance(mCurrentDay, "E0001", "예약대기");
    }

    private void clickPossetion()
    {
        moveMaintenance(mCurrentDay, "E0005", "미실시");
    }

    @Override
    protected void updateRepairPlan()
    {
        // TODO Auto-generated method stub
        // Toast.makeText(mContext,
        // "updateRepairPlan " + this.getClass().getName(),
        // Toast.LENGTH_SHORT).show();

        // 2014-01-19 KDH 여기서 업데이트 건이 있다면 SAP 호출이후 다시 여기로 떨어지므로 여기서 팝업을 띄우는것이아니다.
        // 최종 디비 인서트 이후 셀렉하는 곳을 찾어서 거기서 팝업띄워주면됨. 여기서 작업하면 팝업이 변경만큼떨어지므로 뭐같다 .//BaseRepairFragment

        // 2014-01-20 KDH 여기말곤없네-_-삽됐다.

        ArrayList<RepairDayInfoModel> repairDayInfoModels = KtRentalApplication.getInstance().getDayList();
        if (mCalendarFragment != null)
            mCalendarFragment.updateDayList(repairDayInfoModels);
        String complate = getComplate();
        String plan = getPlan();
        if (mTvMontyPlanLeft != null && mTvMontyPlanRight != null)
        {
            mTvMontyPlanLeft.setText(complate);
            mTvMontyPlanLeft2.setText(getComplate2());
            mTvMontyPlanLeft3.setText(getComplate3());
            mTvMontyPlanRight.setText(plan);
            mTvMontyPlanRight2.setText(getPlan2());
            mTvMontyPlanRight3.setText(getPlan3());
            mTvMonthlyEmergency.setText(getEmergency());
            mTvMonthlyEmergency2.setText(getEmergency2());
            mTvMonthlyEmergency3.setText(getEmergency3());

        }
        initDegree();

        for (RepairDayInfoModel repairDayInfoModel : repairDayInfoModels)
        {
            if (repairDayInfoModel.getDayInfoModel().isToDay())
            {
                RepairPlanModel planModel = repairDayInfoModel.getRepairPlanModel();
                if (planModel != null && mTvTodayPlanLeft != null && mTvTodayPlanRight != null)
                {

                    mComplate = planModel.getComplate();
                    mTvTodayPlanLeft.setText("" + mComplate);
                    mTvTodayPlanLeft2.setText("" + planModel.getComplate2());
                    mTvTodayPlanLeft3.setText("" + planModel.getComplate3());
                    mTvTodayPlanRight.setText("" + planModel.getPlan());
                    mTvTodayPlanRight2.setText("" + planModel.getPlan2());
                    mTvTodayPlanRight3.setText("" + planModel.getPlan3());
                    break;
                }
            }
        }

        initTodayText(repairDayInfoModels);

        queryMaintenace(CommonUtil.getCurrentDay());
    }

    private void initTodayText(ArrayList<RepairDayInfoModel> repairDayInfoModels)
    {

        int order = 0;
        int order2 = 0;
        int order3 = 0;
        int orderWait = 0;
        int orderWait2 = 0;
        int orderWait3 = 0;
        int possetion = 0;
        int possetion2 = 0;
        int possetion3 = 0;
        int emergency = 0;
        int emergency2 = 0;
        int emergency3 = 0;

        for (RepairDayInfoModel repairDayInfoModel : repairDayInfoModels)
        {
            if (repairDayInfoModel.getDayInfoModel().isToDay())
            {
                RepairPlanModel planModel = repairDayInfoModel.getRepairPlanModel();
                if (planModel != null)
                {
                    order = order + repairDayInfoModel.getRepairPlanModel().getSubscription();
                    order2 = order2 + repairDayInfoModel.getRepairPlanModel().getSubscription2();
                    order3 = order3 + repairDayInfoModel.getRepairPlanModel().getSubscription3();
                    orderWait = orderWait + repairDayInfoModel.getRepairPlanModel().getSubscriptionWaitting();
                    orderWait2 = orderWait2 + repairDayInfoModel.getRepairPlanModel().getSubscriptionWaitting2();
                    orderWait3 = orderWait3 + repairDayInfoModel.getRepairPlanModel().getSubscriptionWaitting3();
                    possetion = possetion + repairDayInfoModel.getRepairPlanModel().getPossetion();
                    possetion2 = possetion2 + repairDayInfoModel.getRepairPlanModel().getPossetion2();
                    possetion3 = possetion3 + repairDayInfoModel.getRepairPlanModel().getPossetion3();
                    emergency = emergency + repairDayInfoModel.getRepairPlanModel().getEmergency();
                    emergency2 = emergency2 + repairDayInfoModel.getRepairPlanModel().getEmergency2();
                    emergency3 = emergency3 + repairDayInfoModel.getRepairPlanModel().getEmergency3();
                    break;
                }
            }
        }

        mTvTodayOrder.setText("" + order);
        mTvTodayOrder2.setText("" + order2);
        mTvTodayOrder3.setText("" + order3);
        mTvTodayOrderWait.setText("" + orderWait);
        mTvTodayOrderWait2.setText("" + orderWait2);
        mTvTodayOrderWait3.setText("" + orderWait3);
        mTvTodayPossetion.setText("" + possetion);
        mTvTodayPossetion2.setText("" + possetion2);
        mTvTodayPossetion3.setText("" + possetion3);
        mTvTodayEmergency.setText("" + emergency);
        mTvTodayEmergency2.setText("" + emergency2);
        mTvTodayEmergency3.setText("" + emergency3);

        kog.e("KDH", "여기가 로그인 이후 화면이다. 결론은 이번달에 미처리 건에 대해서는 여기서 체크해야됨.");

    }

    @Override
    protected void initSelectedMaintenanceArray(String currentDay)
    {
        // TODO Auto-generated method stub

    }

    @Override
    protected void queryMaintenace(String currentDay)
    {
        // TODO Auto-generated method stub
        // CommonUtil.showCallStack();
        showProgress("순회 정비계획을 조회 중입니다.");

        String[] _whereArgs = {
                currentDay, "E0002" };
        String[] _whereCause = {
                "GSTRS", DEFINE.CCMSTS };

        String[] colums = maintenace_colums;

        DbQueryModel dbQueryModel = new DbQueryModel(ConnectController.REPAIR_TABLE_NAME, _whereCause, _whereArgs, colums);

        // dbQueryModel
        // .setOrderBy("  case CCMSTS   when 'E0001' then '1' when 'E0002' then '' when 'E0003' then '3' when 'E0004' then '4' else '9' end ");

        DbAsyncTask dbAsyncTask = new DbAsyncTask(currentDay, mContext, this, dbQueryModel);
        mAsyncMap.put(currentDay, dbAsyncTask);
        mCurrentDay = currentDay;

        dbAsyncTask.execute(DbAsyncTask.DB_SELECT);
    }

    @Override
    public void OnSeletedItem(Object item)
    {
        // TODO Auto-generated method stub
        DayInfoModel model = (DayInfoModel) item;
        moveMaintenance(model.getCurrentDay(), "전체", "전체");
    }

    @Override
    protected void movePlan(ArrayList<BaseMaintenanceModel> model)
    {
        // TODO Auto-generated method stub

    }

    @Override
    protected void initScroll()
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void onClickCalendarTitle()
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void onHiddenChanged(boolean hidden)
    {
        if (!hidden)
        {
            queryBaseGroup();
            KtRentalApplication.getInstance().queryMaintenacePlan();
            // StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
            // .detectDiskReads().detectDiskWrites().detectNetwork()
            // .penaltyLog().build());
        }
        super.onHiddenChanged(hidden);
    }

    @Override
    public void onClickSync()
    {
        // TODO Auto-generated method stub
        if (isNetwork())
            repairPlanWork();
    }

    private void repairPlanWork()
    {
        showProgress("순회정비 일정을 동기화 중입니다.");

        SharedPreferencesUtil sharedPreferencesUtil = SharedPreferencesUtil.getInstance();

        String SyncStr = " ";

        if (sharedPreferencesUtil.isSuccessSyncDB())
        {
            // String id = SharedPreferencesUtil.getInstance().getId();
            // if (mFirstId.equals(id)) {
            // SyncStr = " ";
            // } else {
            // SyncStr = "A";
            // sharedPreferencesUtil.setSyncSuccess(false);
            // }
            SyncStr = " ";
            // loginSuccess(); // 순회정비계획 싱크가 느려 우선은 로그인성공으로 바로가게 한다.
            // return;
        }
        else
        {

            SyncStr = "A";
            sharedPreferencesUtil.setSyncSuccess(false);
        }

        LoginModel model = KtRentalApplication.getLoginModel();
        // runUiThread(UiRunnable.MODE_PROGRESS_SHOW, "순회정비 리스트를 확인 중 입니다.");
        ConnectController connectController = new ConnectController(this, mContext);
        // 테이블 데이타를 얻어온다.
        connectController.getRepairPlan(model.getPernr(), SyncStr, mContext);
    }

    @Override
    public void connectResponse(String FuntionName, String resultText, String MTYPE, int resulCode, TableModel tableModel)
    {
        // TODO Auto-generated method stub
        if (FuntionName.equals(ConnectController.REPAIR_FUNTION_NAME))
        {

            kog.e("Jonathan", "설마 여기 ?? ");

            HashMap<String, String> loginTableNameMap = new HashMap<String, String>();
            loginTableNameMap.put("O_ITAB1", ConnectController.REPAIR_TABLE_NAME);
            loginTableNameMap.put("O_ITAB2", DEFINE.STOCK_TABLE_NAME);

            tableModel.setTableName(ConnectController.REPAIR_TABLE_NAME);

            loginTableNameMap.put("O_ITAB3", DEFINE.REPAIR_LAST_TABLE_NAME);
            loginTableNameMap.put("O_ITAB4", DEFINE.REPAIR_LAST_DETAIL_TABLE_NAME);

            DbAsyncTask asyncTask = new DbAsyncTask(ConnectController.REPAIR_FUNTION_NAME, O_ITAB1.TABLENAME, mContext, new DbAsyncResLintener()
            {

                @Override
                public void onCompleteDB(String funName, int type, Cursor cursor, String tableName)
                {
                    // TODO Auto-generated method stub
                    hideProgress();
                    // Log.e("????????", "tt");
                    // myung 20131211 ADD 일정동기화 시 금일정비 숫자 업데이트
                    queryBaseGroup();
                    KtRentalApplication.getInstance().queryMaintenacePlan();
                    
//                    mCc.duplicateLogin(mContext);
                }
            }, // DbAsyncResListener
                    tableModel, loginTableNameMap);
            asyncTask.execute(DbAsyncTask.DB_ARRAY_INSERT);
        }

        // myung 공지사항 목록
        else if (FuntionName.equals("ZMO_1060_RD06"))
        {
            if (tableModel == null)
                return;
            ArrayList<HashMap<String, String>> RD06Array = tableModel.getTableArray();
            ArrayList<HomeNoticeModel> arrayList = new ArrayList<HomeNoticeModel>();
            for (int i = 0; i < RD06Array.size(); i++)
            {
                // Log.i("", "####" + RD06Array.get(i).get("AN_SBM"));

                // NoticeModel model = new NoticeModel();
                HomeNoticeModel Hmodel = new HomeNoticeModel(RD06Array.get(i).get("AN_SBM"), RD06Array.get(i).get("RG_BDTM"));
                // model.setTitle(RD06Array.get(i).get("AN_SBM")); // 공지제목
                // model.setDay(RD06Array.get(i).get("RG_BDTM")); // 공지시작일
                // model.setDay(RD06Array.get(i).get("RG_BENO")); // 최초등록사원

                // mNoticeModels.add(model);
                arrayList.add(Hmodel);

            }

            mHomeNoticeAdapter.setData(arrayList);
            // myung 20131210 ADD 공지사항 조회 시 전체 리스트가 4건보다 클 경우만 더보기 버튼 활성화
            // Log.e("arrayList", ""+arrayList.size());
            if (arrayList.size() <= 4)
            {
                mBtnNoticeMore.setEnabled(false);
            }
            else
            {
                mBtnNoticeMore.setEnabled(true);
            }

            //170403 Jonathan 공지사항을 첫번째 페이지로..
            moveNotice();
        }
    }

    @Override
    public void reDownloadDB(String newVersion)
    {
        // TODO Auto-generated method stub

    }

    private void queryBaseGroup()
    {
        String[] _whereArgs = null;
        String[] _whereCause = null;

        String[] colums = null;

        DbQueryModel dbQueryModel = new DbQueryModel(DEFINE.REPAIR_RESULT_BASE_TABLE_NAME, _whereCause, _whereArgs, colums);
        kog.e("KDH", "HOMEFRAGMENT = queryBaseGroup()");

        DbAsyncTask dbAsyncTask = new DbAsyncTask("queryBaseGroup", mContext, new DbAsyncResLintener()
        {

            @Override
            public void onCompleteDB(String funName, int type, Cursor cursor, String tableName)
            {
                // TODO Auto-generated method stub
                if (funName.equals("queryBaseGroup"))
                {
                    int resultSendCount = 0;
                    if (cursor != null)
                    {
                        resultSendCount = cursor.getCount();
                        kog.e("KDH", "resultSendCount = " + resultSendCount);
                    }
                    if (mBtnSend != null)
                        mBtnSend.setText("미전송건 (" + resultSendCount + ")");

                }
            }
        }, dbQueryModel);
        dbAsyncTask.execute(DbAsyncTask.DB_SELECT);
    }

    private void clickSend()
    {
        showProgress("미전송 내역을 전송 중입니다");
        ResultController resultController = new ResultController(mContext, new OnResultCompleate()
        {

            @Override
            public void onResultComplete(String message)
            {
                // reDownloadDB("");
                // runUiThread(UiRunnable.MODE_PROGRESS_SHOW,
                // message);
                // Log.e("message", message);
                // myung 2013117 ADD 미전송 내역 갱신
                queryBaseGroup();
                hideProgress();
                if (!message.equals("0"))
                    KtRentalApplication.changeRepair();
            }
        });
        resultController.queryBaseGroup(false);
    }

    private void initDegree()
    {

        try
        {
            int complate = Integer.valueOf(getComplate());
            int complate2 = Integer.valueOf(getComplate2());
            int complate3 = Integer.valueOf(getComplate3());
            int plan = Integer.valueOf(getPlan());
            int plan2 = Integer.valueOf(getPlan2());
            int plan3 = Integer.valueOf(getPlan3());

            int degree = (int)Math.round(((double)complate / (double)plan) * 100);
            int degree2 = (int)Math.round(((double)complate2 / (double)plan2) * 100);
            int degree3 = (int)Math.round(((double)complate3 / (double)plan3) * 100);

            if (mTvDegree != null)
                mTvDegree.setText("" + degree + " %");
                mTvDegree2.setText("" + degree2 + " %");
                mTvDegree3.setText("" + degree3 + " %");
        }
        catch (ArithmeticException e)
        {
            // TODO: handle exception
        }

    }

    @Override
    public void onCompleteDB(String funName, int type, Cursor cursor, String tableName)
    {
        // TODO Auto-generated method stub
        try {
            boolean MaintenaceAsyncFlag = mAsyncMap.containsKey(funName);

            if (MaintenaceAsyncFlag)
            {

                parsingMaintenance(cursor);

            }
        } catch (Exception e){
            e.printStackTrace();
        }

    }

    private void parsingMaintenance(final Cursor asyncCursor)
    {

        mBaseMaintenanceModels.clear();

        if (asyncCursor == null)
        {
            hideProgress();
            // initMaintenanceEmpty(0);
            return;
        }

        // TODO Auto-generated method stub

        mBaseMaintenanceModels.clear();

        try {
            asyncCursor.moveToFirst();
            while (!asyncCursor.isAfterLast())
            {
                String time = asyncCursor.getString(asyncCursor.getColumnIndex(maintenace_colums[0]));
                String invnr = asyncCursor.getString(asyncCursor.getColumnIndex(maintenace_colums[1]));
                String name = asyncCursor.getString(asyncCursor.getColumnIndex(maintenace_colums[2]));

                // if (name == null || name.equals(" ")) {
                String customerName = asyncCursor.getString(asyncCursor.getColumnIndex(maintenace_colums[3]));
                // }

                String carname = asyncCursor.getString(asyncCursor.getColumnIndex(maintenace_colums[4]));
                String status = asyncCursor.getString(asyncCursor.getColumnIndex(maintenace_colums[5]));
                String postCode = asyncCursor.getString(asyncCursor.getColumnIndex(maintenace_colums[6]));
                String city = asyncCursor.getString(asyncCursor.getColumnIndex(maintenace_colums[7]));
                String street = asyncCursor.getString(asyncCursor.getColumnIndex(maintenace_colums[8]));
                String tel = asyncCursor.getString(asyncCursor.getColumnIndex(maintenace_colums[9]));
                String progress_status = asyncCursor.getString(asyncCursor.getColumnIndex(maintenace_colums[10]));
                String day = asyncCursor.getString(asyncCursor.getColumnIndex(maintenace_colums[11]));
                String aufnr = asyncCursor.getString(asyncCursor.getColumnIndex(maintenace_colums[12]));
                String equnr = asyncCursor.getString(asyncCursor.getColumnIndex(maintenace_colums[13]));
                String ctrty = asyncCursor.getString(asyncCursor.getColumnIndex(maintenace_colums[14]));
                String drv_mob = asyncCursor.getString(asyncCursor.getColumnIndex(maintenace_colums[15]));

                String vip = asyncCursor.getString(asyncCursor.getColumnIndex(maintenace_colums[16]));

                String ldate = asyncCursor.getString(asyncCursor.getColumnIndex(maintenace_colums[17]));
                String reOil = asyncCursor.getString(asyncCursor.getColumnIndex(maintenace_colums[18]));
                String cermr = asyncCursor.getString(asyncCursor.getColumnIndex(maintenace_colums[19]));
                String ccmrq = asyncCursor.getString(asyncCursor.getColumnIndex(maintenace_colums[5]));

                String vocNum = asyncCursor.getString(asyncCursor.getColumnIndex(maintenace_colums[20]));


                String kunnr = asyncCursor.getString(asyncCursor.getColumnIndex(maintenace_colums[21]));

                String vbeln = asyncCursor.getString(asyncCursor.getColumnIndex(maintenace_colums[24]));

                String gubun = asyncCursor.getString(asyncCursor.getColumnIndex(maintenace_colums[25]));

                String reqNo = asyncCursor.getString(asyncCursor.getColumnIndex(maintenace_colums[26]));

                String atvyn = asyncCursor.getString(asyncCursor.getColumnIndex(maintenace_colums[27]));

                String prerq = asyncCursor.getString(asyncCursor.getColumnIndex(maintenace_colums[28]));

                String minvnr = asyncCursor.getString(asyncCursor.getColumnIndex(maintenace_colums[29]));





                for (int i = 0; i < maintenace_colums.length; i++)
                {

                    kog.e("Jonathan", "maintenace_colums[" + i + "]" + asyncCursor.getString(asyncCursor.getColumnIndex(maintenace_colums[i])));

                }

                if (time != null)
                {
                    if (time.length() > 4)
                    {
                        time = time.substring(0, 4);

                        String hour = time.substring(0, 2);
                        String sec = time.substring(2, 4);
                        time = hour + ":" + sec;
                    }
                }

                postCode = decrypt(maintenace_colums[4], postCode);
                city = decrypt(maintenace_colums[5], city);
                street = decrypt(maintenace_colums[6], street);
                HomeMaintenanceModel md = new HomeMaintenanceModel(decrypt(maintenace_colums[0], time), decrypt(maintenace_colums[2], name),
                        decrypt(maintenace_colums[3], customerName), // Jonathan 14.06.19 추가
                        decrypt(maintenace_colums[1], invnr), postCode + city + street, decrypt(maintenace_colums[7], carname), decrypt(
                        maintenace_colums[8], status), decrypt(maintenace_colums[9], tel), decrypt(maintenace_colums[10], progress_status),
                        decrypt(maintenace_colums[11], day), decrypt(maintenace_colums[12], aufnr), decrypt(maintenace_colums[13], equnr), decrypt(
                        maintenace_colums[14], ctrty), postCode, city, street, decrypt(maintenace_colums[15], drv_mob), decrypt(
                        maintenace_colums[16], vip), decrypt(maintenace_colums[17], ldate), decrypt(maintenace_colums[3], customerName), decrypt(
                        maintenace_colums[18], reOil), decrypt(maintenace_colums[19], cermr), decrypt(maintenace_colums[5], ccmrq), "", "", "",
                        decrypt(maintenace_colums[20],vocNum), decrypt(maintenace_colums[21], kunnr), maintenace_colums[22], vbeln, gubun, reqNo, atvyn, prerq, minvnr);

                // MaintenanceModel md = new MaintenanceModel(time,
                // name, invnr,
                // postCode + city + street, carname, status);
                if (cermr.equals(" "))
                    mBaseMaintenanceModels.add(md);
                asyncCursor.moveToNext();

                // boolean Flag = mAsyncMap.containsKey(funName);
                //
                // if (!Flag) {
                // return;
                // }

            }
            asyncCursor.close();
        } catch (Exception e){
            e.printStackTrace();
        }



        mRootView.post(new Runnable()
        {

            @Override
            public void run()
            {
                // Toast.makeText(mContext, "setDataMaintenanceArr",
                // Toast.LENGTH_SHORT).show();
                // maintenanceAdapter.setDataArr(mBaseMaintenanceModels);
                mHomeCardFragment.setMaintenanceModels(mBaseMaintenanceModels);
                hideProgress();
                // maintenance_Date_Adapter.initScrollPosition(false);
                initScroll();
                initShadowView();
                if (mBaseMaintenanceModels.size() > 0)
                    mFlEmpty.setVisibility(View.GONE);
                else
                {
                    setEmpty();
                }
                // initMaintenanceEmpty(maintenanceAdapter.getCount());
            }
        });
        return;

    }

    @Override
    public void onCardClick(BaseMaintenanceModel model)
    {
        // TODO Auto-generated method stub
        showResultFragment(model, false);
    }

    @Override
    public void onStart()
    {
        // TODO Auto-generated method stub
        super.onStart();
        mCalendarFragment.setOnSelectedItem(this);

    }

    @Override
    public OnChangeFragmentListener getOnChangeFragmentListener()
    {
        return mOnChangeFragmentListener;
    }

    @Override
    public void setOnChangeFragmentListener(OnChangeFragmentListener mOnChangeFragmentListener)
    {
        this.mOnChangeFragmentListener = mOnChangeFragmentListener;
    }

    private void setEmpty()
    {
        boolean complateFlag = false;

        if (mComplate > 0)
            complateFlag = true;

        mFlEmpty.setVisibility(View.VISIBLE);
        if (complateFlag)
        {
            mTvComplateMessage.setText("금일순회정비가 완료 되었습니다.");
            mIvEmpty.setVisibility(View.VISIBLE);
            mIvEmpty.setBackgroundResource(R.drawable.main_completion);
        }
        else
        {
            mTvComplateMessage.setText("정비예약건이 없습니다.");
            mIvEmpty.setVisibility(View.VISIBLE);
            mIvEmpty.setBackgroundResource(R.drawable.main_nodata);
        }
    }

    private void initShadowView()
    {
        if (mRootView != null)
        {
            View leftShadow = mRootView.findViewById(R.id.v_left_shadow);
            View rightShadow = mRootView.findViewById(R.id.v_right_shadow);
            if (mBaseMaintenanceModels.size() < 2)
            {
                if (leftShadow != null)
                    leftShadow.setVisibility(View.GONE);
                if (rightShadow != null)
                    rightShadow.setVisibility(View.GONE);
            }
        }
    }
}
