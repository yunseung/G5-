package com.ktrental.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

import com.ktrental.R;
import com.ktrental.calendar.CalendarAdapter;
import com.ktrental.calendar.CalendarController;
import com.ktrental.calendar.DayInfoModel;
import com.ktrental.common.DEFINE;
import com.ktrental.common.KtRentalApplication;
import com.ktrental.model.RepairDayInfoModel;
import com.ktrental.model.RepairPlanModel;
import com.ktrental.util.CommonUtil;
import com.ktrental.util.OnChangeFragmentListener;
import com.ktrental.util.OnSelectedItem;

import java.util.ArrayList;

/**
 * 렌탈에서 사용되는 달력 Fragment. <br/>
 * DayInfoModel이라는 데이터 클래스를 가지고 달력정보를 보여주고 해당 날짜와 클릭 이벤트등을 통해 수정이 가능하다.
 *
 * @author Hong
 * @since 2013.07.12
 */

public class CalendarFragment extends BaseFragment implements OnItemClickListener, View.OnClickListener {

    private GridView mGvCalendar;
    private TextView mTvCalendarTitle;

    private ArrayList<RepairDayInfoModel> mDayList;
    private ArrayList<RepairPlanModel> mRepairPlanModelArray = new ArrayList<RepairPlanModel>();
    private CalendarAdapter mCalendarAdapter;

    private CalendarController mCalendarManager;

    private Context mContext;

    private OnCalendarListener mOnCalendarListener;
    private boolean mSelectedFlag = true;

    // yunseung 2019.05.09 익월 데이터 보는거 추가 요청
    private Button mBtnNext, mBtnPrev;

    public void setOnCalendarListener(OnCalendarListener aOnCalendarListener) {
        mOnCalendarListener = aOnCalendarListener;
    }

    public interface OnCalendarListener {
        void onClickCalendarTitle();

        void onClickSync();
    }

    public CalendarFragment() {
    }

    public CalendarFragment(String className, OnChangeFragmentListener changeFragmentListener, boolean selectedFlag) {
        super(className, changeFragmentListener);
        // TODO Auto-generated constructor stub
        mSelectedFlag = selectedFlag;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onActivityCreated(savedInstanceState);

        handler.sendEmptyMessageDelayed(0, 1000);
    }

    Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);

            if (getView() == null)
                return;

            if (getView().getHeight() == 0) {

                handler.sendEmptyMessageDelayed(0, 1000);
            } else {
                initCalendarAdapter();
            }
        }
    };

    @Override
    public void onAttach(Activity activity) {
        // TODO Auto-generated method stub
        super.onAttach(activity);
        mContext = activity;
        mCalendarAdapter = new CalendarAdapter(mContext, mSelectedFlag);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO Auto-generated method stub

        View root = (View) inflater.inflate(R.layout.calendar_fragment_layout, container, false);
        mRootView = root;
        mGvCalendar = (GridView) root.findViewById(R.id.gv_calendar_activity_gv_calendar);

        mGvCalendar.setOverScrollMode(View.OVER_SCROLL_NEVER);

        mTvCalendarTitle = (TextView) root.findViewById(R.id.gv_calendar_activity_tv_title);
        mTvCalendarTitle.setOnClickListener(this);

        // Button bLastMonth = (Button) root
        // .findViewById(R.id.gv_calendar_activity_b_last);
        // Button bNextMonth = (Button) root
        // .findViewById(R.id.gv_calendar_activity_b_next);
        //
        // bLastMonth.setOnClickListener(this);
        // bNextMonth.setOnClickListener(this);

        mGvCalendar.setOnItemClickListener(this);
        mRootView.findViewById(R.id.btn_sync).setOnClickListener(this);

        mBtnNext = (Button) mRootView.findViewById(R.id.next);
        mBtnPrev = (Button) mRootView.findViewById(R.id.pre);
        setMoveButton();

        initCalendarDataSetting();
        // initCalendarAdapter();

        ArrayList<RepairDayInfoModel> repairDayInfoModels = KtRentalApplication.getInstance().getDayList();

        return root;
    }

    @Override
    public void onStart() {
        // TODO Auto-generated method stub
        super.onStart();
        initCalendarDataSetting();
        initCalendarAdapter();
        setCalendarTitle();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onDestroyView() {
        CommonUtil.unbindDrawables(getView());

        mCalendarAdapter.releaseResouces();
        System.gc();
        super.onDestroy();
    }

    ;

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();

        mGvCalendar = null;
        mTvCalendarTitle = null;

        mDayList = null;
        mCalendarAdapter = null;

        mCalendarManager = null;

        mContext = null;

    }

    /**
     * 달력 정 초기화 및 세팅.
     */
    private void initCalendarDataSetting() {

        mDayList = new ArrayList<RepairDayInfoModel>();

        mCalendarManager = new CalendarController(CalendarController.TYPE_SHOW_OTHERMONTH);
        // mDayList = mCalendarManager.getDayInfoArrayList();

    }

    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
    }

    private void initCalendarAdapter() {

        // mCalendarAdapter.addAllDayInfoList(mDayList);
        // 2014-01-20 KDH 이게뭐지?서로가 서로를 set해줘?
        mGvCalendar.setAdapter(mCalendarAdapter);
        mCalendarAdapter.setGridview(mGvCalendar);

        ArrayList<RepairDayInfoModel> repairDayInfoModels = KtRentalApplication.getInstance().getDayList();
        if (repairDayInfoModels != null) {
            updateDayList(repairDayInfoModels);
        }

    }

    private void setCalendarTitle() {
        String title = mCalendarManager.getCalendarTitle();
        mTvCalendarTitle.setText(title);
        if (DEFINE.DISPLAY.equals("2560")) {
            mTvCalendarTitle.setTextSize(28);
        }
    }

    private void setMoveButton() {
        mBtnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//				mDayList.clear();
//
//                for (DayInfoModel dayInfoModel : mCalendarManager.getNextDayInfoList()) {
//                    mDayList.add(new RepairDayInfoModel(dayInfoModel));
//                }
//
//                updateDayList(mDayList);
                changeNextMonth();
            }
        });

        mBtnPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                mDayList.clear();
//
//                for (DayInfoModel dayInfoModel : mCalendarManager.getPrevDayInfoList()) {
//                    mDayList.add(new RepairDayInfoModel(dayInfoModel));
//                }
//
//                updateDayList(mDayList);
                changePrevMonth();
            }
        });
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            // case R.id.gv_calendar_activity_b_last:
            // changePrevMonth();
            // break;
            //
            // case R.id.gv_calendar_activity_b_next:
            // changeNextMonth();
            // break;
            case R.id.gv_calendar_activity_tv_title:
                if (mOnCalendarListener != null)
                    mOnCalendarListener.onClickCalendarTitle();
                break;
            case R.id.btn_sync:
                if (mOnCalendarListener != null)
                    mOnCalendarListener.onClickSync();
                break;

        }
    }

    /**
     * 달력을 이전달로 변경해준다.
     */
    private void changePrevMonth() {
        mDayList.clear();
        for (DayInfoModel model : mCalendarManager.getPrevDayInfoList()) {
            mDayList.add(new RepairDayInfoModel(model));
        }

        for (int i = 0; i < mDayList.size(); i++) {
            for (int j = 0; j < mRepairPlanModelArray.size(); j++) {
                if (mDayList.get(i).getDayInfoModel().getCurrentDay().equals(Integer.toString(mRepairPlanModelArray.get(j).getWorkDay())) &&
                        mDayList.get(i).getDayInfoModel().isInMonth()) {
                    mDayList.get(i).setRepairPlanModel(mRepairPlanModelArray.get(j));
                }
            }
        }
        changeDayInfo();
    }

    /**
     * 달력을 다을달로 변경해준다.
     */
    private void changeNextMonth() {
        mDayList.clear();
        for (DayInfoModel model : mCalendarManager.getNextDayInfoList()) {
            mDayList.add(new RepairDayInfoModel(model));
        }

        for (int i = 0; i < mDayList.size(); i++) {
            for (int j = 0; j < mRepairPlanModelArray.size(); j++) {
                if (mDayList.get(i).getDayInfoModel().getCurrentDay().equals(Integer.toString(mRepairPlanModelArray.get(j).getWorkDay())) &&
                        mDayList.get(i).getDayInfoModel().isInMonth()) {
                    mDayList.get(i).setRepairPlanModel(mRepairPlanModelArray.get(j));
                }
            }
        }
        changeDayInfo();
    }

    /**
     * 달력을 선택된 달로 변경해준다.
     */
    private void changeDayInfo() {
        mCalendarAdapter.setChangeDayInfoList(mDayList);
        mCalendarAdapter.notifyDataSetChanged();
        setCalendarTitle();
    }

    /**
     * 현재 보여지는 달에 text를 보내준
     */
    public String getCurrentMonthString() {
        String title = null;
        if (mCalendarManager != null)
            title = mCalendarManager.getCalendarTitle();

        return title;
    }

    public void setRepairPlanModelArray(ArrayList<RepairPlanModel> array) {
        mRepairPlanModelArray = array;
    }

    public void updateDayList(ArrayList<RepairDayInfoModel> array) {
        if (mCalendarAdapter != null) {
            mCalendarAdapter.setChangeDayInfoList(array);
            mCalendarAdapter.notifyDataSetChanged();
        }
    }

    public void setOnSelectedItem(OnSelectedItem aOnSelectedItem) {
        if (mCalendarAdapter != null)
            mCalendarAdapter.setOnSelectedItem(aOnSelectedItem);
    }

    public void initSelectedPosition() {
        mCalendarAdapter.initSelectedPosition();
        initCalendarDataSetting();
        initCalendarAdapter();
        setCalendarTitle();
    }

    public RepairPlanModel getTodayPlanModel() {
        if (mCalendarAdapter == null)
            return null;
        return mCalendarAdapter.getTodayPlanModel();
    }

    public void invisibleSyncButton() {
        if (mRootView != null)
            mRootView.findViewById(R.id.btn_sync).setVisibility(View.GONE);
    }
}
