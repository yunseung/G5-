package com.ktrental.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.ktrental.R;
import com.ktrental.model.BaseMaintenanceModel;
import com.ktrental.model.MaintenanceModel;
import com.ktrental.viewholder.MaintenanceViewHolder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * 선택 된 일정을 보여주는 Adapter 이다. </br> BaseMaintenceAdapter를 상속받음.</br>
 * 
 * </br>선택된 날짜에 일정을 보여주며 필터링을 제공.</br>
 * 
 * @author hongsungil
 */
public class MaintenanceAdapter extends BaseMaintenceAdapter
{

    private String mProgressType    = "전체";
    private String mMaintenanceType = "";
    private String mTermType        = "전체";

    public MaintenanceAdapter(Context context, OnClickRootView onClickRootView)
    {
        super(context, onClickRootView);
        // TODO Auto-generated constructor stub
    }

    @Override
    public BaseMaintenanceModel getItem(int position)
    {
        // TODO Auto-generated method stub
        return super.getItem(position);
    }

    @Override
    public int getCount()
    {
        // TODO Auto-generated method stub
        return super.getCount();
    }

    @Override
    protected void bindView(View rootView, Context context, int position)
    {

        super.bindView(rootView, context, position);

        MaintenanceViewHolder viewHolder = (MaintenanceViewHolder) rootView.getTag();

        MaintenanceModel model = (MaintenanceModel) getItem(position);
        if (model != null) {
            if (model.getAUFNR().trim().isEmpty() && model.getATVYN().equals("A")) {
                viewHolder.tvTime.setText(insertDot(model.getDay()) + " " + (model.getAPM().equals("AM") ? "오전" : "오후"));
            } else {
                viewHolder.tvTime.setText(insertDot(model.getDay()) + " " + model.getTime());
            }
            viewHolder.tvStatus.setText(model.getStatus());
            if("0".equals(model.getVocNum()))
            {
            	viewHolder.tvVocInfo.setVisibility(View.GONE);
            }
            else
            {
            	viewHolder.tvVocInfo.setVisibility(View.VISIBLE);
            	viewHolder.tvVocInfo.setText("VOC : " + model.getVocNum());
            }

        }
    }

    @Override
    protected View newView(Context context, int position, ViewGroup viewgroup)
    {

        MaintenanceViewHolder viewHolder = new MaintenanceViewHolder();

        View rootView = newMaintenanceView(context, position, viewgroup, viewHolder);

        viewHolder = (MaintenanceViewHolder) rootView.getTag();

        viewHolder.tvTime = (TextView) rootView.findViewById(R.id.tv_dayortime);
        viewHolder.tvStatus = (TextView) rootView.findViewById(R.id.tv_status);
        viewHolder.tvVocInfo = (TextView) rootView.findViewById(R.id.tv_voc_info);
        
        LayoutParams ll = (LayoutParams) viewHolder.tvCarNum.getLayoutParams();

        ll.setMargins(0, 0, 0, 0);
        viewHolder.tvCarNum.setLayoutParams(ll);

        // viewHolder.llMaintenace = (LinearLayout) rootView
        // .findViewById(R.id.ll_maintenace);

        // viewHolder.llMaintenace.setVisibility(View.VISIBLE);
        viewHolder.tvStatus.setVisibility(View.VISIBLE);
        
        viewHolder.tvVocInfo.setText("");
        
        // rootView.findViewById(R.id.tv_booking).setVisibility(View.GONE);

        rootView.setTag(viewHolder);

        return rootView;
    }

    @Override
    public void releaseResouces()
    {
        // TODO Auto-generated method stub
        mItemArray = null;
        recursiveRecycle(mRecycleViews);
    }

    public String getProgressType()
    {
        return mProgressType;
    }

    public void setProgressType(String aProgressType)
    {
        this.mProgressType = aProgressType;
        mFilteringFlag = true;
        filtering();
    }

    public String getMaintenanceType()
    {
        return mMaintenanceType;
    }

    public void setMaintenanceType(String aMaintenanceType)
    {
        this.mMaintenanceType = aMaintenanceType;
        mFilteringFlag = true;
        filtering();
    }

    public String getTermType()
    {
        return mTermType;
    }

    public void setTermType(String aTermType)
    {
        this.mTermType = aTermType;
        mFilteringFlag = true;
        filtering();
    }

    private String insertDot(String aDay) {
        String reStr = aDay;
        if (aDay != null || aDay.length() == 8) {
            String year = aDay.substring(0, 4) + ".";
            String month = aDay.substring(4, 6) + ".";
            String day = aDay.substring(6, 8);

            reStr = year + month + day;
        }
        return reStr;
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void filtering()
    {

        mFilterMaintenanceModelArray.clear();

        String backProgress = "E0002";

        ArrayList<MaintenanceModel> array = new ArrayList<MaintenanceModel>();

        if (!mItemArray.isEmpty())
        {
            backProgress = mItemArray.get(0).getProgress_status();
        }

        for (BaseMaintenanceModel model : mItemArray)
        {
            if (model instanceof MaintenanceModel)
            {
                MaintenanceModel maintenanceModel = (MaintenanceModel) model;
                // 진행 상태
                if (mProgressType.equals(maintenanceModel.getProgress_status()) || mProgressType.equals("전체"))
                {
                    if (mTermType.equals("전체") || mTermType.equals(" ") || mTermType.equals(model.getCTRTY()))
                    {

                        if (!backProgress.equals(maintenanceModel.getProgress_status()))
                        {

                            if (mMaintenanceType.equals("주소"))
                            {
                                Collections.sort(array, new NameAscCompare());
                            }
                            else if (mMaintenanceType.equals("고객명"))
                            {
                                Collections.sort(array, new NameAscCompare());
                            }

                            backProgress = maintenanceModel.getProgress_status();
                            mFilterMaintenanceModelArray.addAll(array);

                            array = new ArrayList<MaintenanceModel>();
                        }

                        array.add(maintenanceModel);

                    }

                }
            }
        }
        if (!array.isEmpty())
        {
            if (mMaintenanceType.equals("주소"))
            {
                Collections.sort(array, new NameAscCompare());
            }
            else if (mMaintenanceType.equals("고객명"))
            {
                Collections.sort(array, new NameAscCompare());
            }
            mFilterMaintenanceModelArray.addAll(array);

        }
        notifyDataSetChanged();

    }

    /**
     * 이름 오름차순
     * 
     * @author falbb
     * 
     */
    class NameAscCompare implements Comparator<BaseMaintenanceModel>
    {

        public NameAscCompare()
        {
            // TODO Auto-generated constructor stub
        }

        /**
         * 오름차순(ASC)
         */
        @Override
        public int compare(BaseMaintenanceModel arg0, BaseMaintenanceModel arg1)
        {
            // TODO Auto-generated method stub

            int compare = 0;

            if (mMaintenanceType.equals("고객명"))
            {
                compare = arg0.getCUSTOMER_NAME().compareTo(arg1.getCUSTOMER_NAME());
            }
            else if (mMaintenanceType.equals("주소"))
            {
                compare = arg0.getAddress().compareTo(arg1.getAddress());
            }

            return compare;
        }

    }
}
