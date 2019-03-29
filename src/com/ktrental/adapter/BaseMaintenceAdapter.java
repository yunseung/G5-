package com.ktrental.adapter;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ktrental.R;
import com.ktrental.activity.GPS;
import com.ktrental.cm.connect.ConnectController;
import com.ktrental.cm.connect.Connector.ConnectInterface;
import com.ktrental.cm.connect.ConnectorUtil;
import com.ktrental.cm.db.DbAsyncTask;
import com.ktrental.cm.db.DbAsyncTask.DbAsyncResLintener;
import com.ktrental.common.AppSt;
import com.ktrental.common.DEFINE;
import com.ktrental.common.KtRentalApplication;
import com.ktrental.dialog.Address_Change_Dialog;
import com.ktrental.dialog.History_Dialog;
import com.ktrental.dialog.IoTRequestItemDialog;
import com.ktrental.dialog.SimpleTextDialog;
import com.ktrental.dialog.TroubleHistoryItemDialog;
import com.ktrental.fragment.BaseRepairFragment;
import com.ktrental.model.BaseMaintenanceModel;
import com.ktrental.model.HomeMaintenanceModel;
import com.ktrental.model.LoginModel;
import com.ktrental.model.MaintenanceModel;
import com.ktrental.model.MonthProgressModel;
import com.ktrental.model.RepairPlanModel;
import com.ktrental.model.TableModel;
import com.ktrental.popup.CallSendPopup;
import com.ktrental.popup.EventPopupC;
import com.ktrental.popup.SMSPopup;
import com.ktrental.product.BaseActivity;
import com.ktrental.util.CommonUtil;
import com.ktrental.util.kog;
import com.ktrental.viewholder.BaseMaintenanceViewHolder;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * 선택 된 일정을 보여주는 Adapter 이다. </br> BaseCommonAdapter를 상속받음.</br> </br> MonthProgressAdapter 와 BaseCommonAdapter 가 상속받음. </br>선택된 날짜에 일정을 보여주며
 * 전화,문자,위치,편집등을 제공.</br>
 * 
 * 
 * 
 * @author hongsungil
 */
public abstract class BaseMaintenceAdapter extends BaseCommonAdapter<BaseMaintenanceModel> implements OnClickListener, ConnectInterface,
        DbAsyncResLintener
{

    protected ArrayList<View>                 mRecycleViews                = new ArrayList<View>();

    protected static final int                TYPE_MONTH                   = 0;
    protected static final int                TYPE_MAINTANCE               = 1;

    private ArrayList<BaseMaintenanceModel>   selectedMaintenanceModels    = new ArrayList<BaseMaintenanceModel>();

    protected ArrayList<BaseMaintenanceModel> mFilterMaintenanceModelArray = new ArrayList<BaseMaintenanceModel>();

    private OnClickRootView                   mOnClickRootView;

    protected abstract void filtering();

    protected boolean          mFilteringFlag = false;

    private ConnectController  mConnectController;

    private BaseRepairFragment mBaseRepairFragment;

    // myung 20131227 ADD
    SimpleDateFormat           formatter      = new SimpleDateFormat("yyyyMMdd");
    Calendar                   rightNow       = Calendar.getInstance();
    int                        nCurrentDate   = Integer.parseInt(formatter.format(rightNow.getTime()));

    String                     beforeAddr;

    public interface OnClickRootView
    {
        public void onClickRoot(BaseMaintenanceModel model);
    }

    public BaseMaintenceAdapter(Context context, OnClickRootView onClickRootView)
    {
        super(context);
        mBaseRepairFragment = (BaseRepairFragment) onClickRootView;
        mOnClickRootView = onClickRootView;
        mConnectController = new ConnectController(this, context);
    }

    @Override
    protected void bindView(View rootView, Context context, int position)
    {

        BaseMaintenanceViewHolder viewHolder = (BaseMaintenanceViewHolder) rootView.getTag();

        BaseMaintenanceModel model = (BaseMaintenanceModel) getItem(position);
        if (model != null)
        {

            viewHolder.tvCarNum.setText(model.getCarNum());
            viewHolder.tvName.setText(model.getCUSTOMER_NAME());
            // = model.getAddress();
            String postCode = "[" + model.getPostCode() + "]";
            String city = model.getCity();
            String street = model.getStreet();

            // if (postCode.length() > 5) {
            // postCode = "[" + p
            // StringBuffer sb = new StringBuffer(postCode);
            // sb.insert(postCode.length(), "]");
            // postCode = "[" + sb;
            // }
            String address = postCode + city + street;
            beforeAddr = address;

            viewHolder.tvAddress.setText(address);
            viewHolder.tvCarname.setText(model.getCarname());
            viewHolder.tvAgreement.setText(RepairPlanModel.getProgressStatus(model.getProgress_status()));


            // IOT ODM 고급형에 따른 UI 변경
            if (model.get_gubun().trim().isEmpty()) {
                viewHolder.tvGubun.setVisibility(View.INVISIBLE);
                viewHolder.btnReqIot.setVisibility(View.INVISIBLE);
                viewHolder.tvDelay.setVisibility(View.INVISIBLE);
                viewHolder.btnTroubleHistory.setVisibility(View.INVISIBLE);
            } else if (model.get_gubun().equals("A")) {
                viewHolder.tvGubun.setVisibility(View.VISIBLE);
                viewHolder.tvGubun.setText("IoT");
                viewHolder.tvDelay.setVisibility(View.INVISIBLE);
                viewHolder.btnReqIot.setVisibility(View.VISIBLE);
                viewHolder.btnTroubleHistory.setVisibility(View.VISIBLE);
            } else if (model.get_gubun().equals("O")) {
                viewHolder.tvGubun.setVisibility(View.VISIBLE);
                viewHolder.btnReqIot.setVisibility(View.INVISIBLE);
                viewHolder.tvDelay.setVisibility(View.VISIBLE);
                viewHolder.btnTroubleHistory.setVisibility(View.INVISIBLE);
                viewHolder.tvGubun.setText("ODM");

                // 2017-06-02. hjt 지연일수 추가
                // 2017-06-08. 지연일수 0이면 안보이도록
                if(model != null) {
                    if(model.getDelay() != null && !model.getDelay().equals("0")) {
                        viewHolder.tvDelay.setText("지연일수 : " + model.getDelay());
                        viewHolder.tvDelay.setVisibility(View.VISIBLE);
                    } else {
                        viewHolder.tvDelay.setVisibility(View.GONE);
                    }
                }
            }

            if("0".equals(model.getVocNum()))
            {
            	viewHolder.tvVocNum.setVisibility(View.GONE);
            }
            else
            {
            	viewHolder.tvVocNum.setVisibility(View.VISIBLE);
            	viewHolder.tvVocNum.setText("VOC : " + model.getVocNum());
            }
            // 2017-11-28. hjt ODC 추가
            if(model.getCYCMN_TX() != null){
                if(model.getCYCMN_TX().contains("점검")){
                    viewHolder.tvODC.setVisibility(View.VISIBLE);
                    viewHolder.item_back.setBackgroundColor(Color.parseColor("#A9E2F3"));
                }else {
                    viewHolder.tvODC.setVisibility(View.GONE);
                    viewHolder.item_back.setBackgroundColor(Color.TRANSPARENT);
                }
            }else {
                viewHolder.tvODC.setVisibility(View.GONE);
                viewHolder.item_back.setBackgroundColor(Color.TRANSPARENT);
            }

            viewHolder.tvPeriod.setText(RepairPlanModel.getPeriodStatus(model.getCTRTY()));
            if (model.isCheck())
            {
                rootView.setBackgroundResource(R.drawable.list01_bg_s);
                viewHolder.ivCheck.setImageResource(R.drawable.check_on);
            }
            else
            {
                rootView.setBackgroundResource(R.drawable.list01_bg_n);
                viewHolder.ivCheck.setImageResource(R.drawable.check_off);
            }

            if ("E0001".equals(model.getProgress_status()))
            {
                viewHolder.tvAgreement.setBackgroundResource(R.drawable.list_infobox02);
            }
            else if ("E0002".equals(model.getProgress_status()))
            {
                viewHolder.tvAgreement.setBackgroundResource(R.drawable.list_infobox03);
            }
            else if ("E0003".equals(model.getProgress_status()))
            {
                viewHolder.tvAgreement.setBackgroundResource(R.drawable.list_infobox04);
            }
            else if ("E0004".equals(model.getProgress_status()))
            {
                viewHolder.tvAgreement.setBackgroundResource(R.drawable.list_infobox05);
            }
            else if ("E0005".equals(model.getProgress_status()))
            {
                viewHolder.tvAgreement.setBackgroundResource(R.drawable.list_infobox06);
            }

            // myung 20131227 ADD 계약 완료 건 표기 및 계약 완료건 이관 시 이관 안되도록 메시지 처리.
            viewHolder.tvGueen2 = (TextView) rootView.findViewById(R.id.tv_gueen2);

            // if(model.getGUEEN2().equals("") || model.getGUEEN2().equals(" "));
            // else{
            // // int nCurrentDay = Integer.parseInt(model.getDay());
            // int nGueen2 = Integer.parseInt(model.getGUEEN2());
            // // Log.e("nGueen2/nCurrentDay", ""+nGueen2+"/"+nCurrentDay);
            //
            // if(nGueen2<nCurrentDate)
            // viewHolder.tvGueen2.setVisibility(View.VISIBLE);
            // else
            // viewHolder.tvGueen2.setVisibility(View.GONE);
            // }

            /*
             * txt30을 Y로 했을때 "계약 종료"라고 뜨게 했음. 다시 원복 시키라고 하여 주석 처리함. Jonathan 14.05.31 다시 요청이 와서 주석 푼다.
             */
            kog.e("KDH", "model.getTXT30() = " + model.getTXT30());
            kog.e("KDH", "model.voc() = " + model.getVocNum());
            
           	viewHolder.tvVocNum.setTag(position);
            
            if (AppSt.ETC_Y.equals(model.getTXT30()))
            {
                viewHolder.tvGueen2.setVisibility(View.VISIBLE);
            }
            else
            {
                viewHolder.tvGueen2.setVisibility(View.GONE);
            }

            viewHolder.ivCheck.setTag(position);
            viewHolder.llRoot.setTag(position);
            viewHolder.btnFind.setTag(position);
            viewHolder.btnCall.setTag(position);
            viewHolder.btnSms.setTag(position);
            viewHolder.btnEdit.setTag(position);
            viewHolder.btnMap.setTag(position);
            viewHolder.tvVocNum.setTag(position);
            viewHolder.tvODC.setTag(position);
            viewHolder.btnTroubleHistory.setTag(position);
            viewHolder.btnReqIot.setTag(position);
            viewHolder.btnReq1.setTag(position);
            viewHolder.btnReq2.setTag(position);
        }

    }

    protected View newMaintenanceView(Context context, int position, ViewGroup viewgroup, BaseMaintenanceViewHolder viewHolder)
    {
        View rootView = mInflater.inflate(R.layout.month_progress_list_item, null);

        viewHolder.item_back = (LinearLayout) rootView.findViewById(R.id.item_back);
        viewHolder.llRoot = (LinearLayout) rootView.findViewById(R.id.ll_root);
        viewHolder.tvCarNum = (TextView) rootView.findViewById(R.id.tv_carnum);
        viewHolder.tvName = (TextView) rootView.findViewById(R.id.tv_name);
        viewHolder.tvAddress = (TextView) rootView.findViewById(R.id.tv_address);

        viewHolder.ivCheck = (ImageView) rootView.findViewById(R.id.iv_check);
        viewHolder.tvCarname = (TextView) rootView.findViewById(R.id.tv_carname);

        // 2017-06-02. hjt 지연일수 추가
        viewHolder.tvDelay = (TextView) rootView.findViewById(R.id.tv_delay);

        viewHolder.tvPeriod = (TextView) rootView.findViewById(R.id.tv_period);
        // viewHolder.tvMaintenance = (TextView) rootView
        // .findViewById(R.id.tv_maintenance);
        viewHolder.tvAgreement = (TextView) rootView.findViewById(R.id.tv_agreement);

        viewHolder.btnFind = (Button) rootView.findViewById(R.id.btn_find);
        viewHolder.btnCall = (Button) rootView.findViewById(R.id.btn_call);
        viewHolder.btnSms = (Button) rootView.findViewById(R.id.btn_sms);
        viewHolder.btnEdit = (Button) rootView.findViewById(R.id.btn_edit);
        viewHolder.btnMap = (Button) rootView.findViewById(R.id.btn_map);
        viewHolder.btnReq1 = (Button) rootView.findViewById(R.id.btn_req1);
        viewHolder.btnReq2 = (Button) rootView.findViewById(R.id.btn_req2);

        viewHolder.tvVocNum = (TextView) rootView.findViewById(R.id.tv_voc_info);

        viewHolder.tvODC = (TextView) rootView.findViewById(R.id.tv_odc_info);

        viewHolder.tvGubun = (TextView) rootView.findViewById(R.id.gubun);
        viewHolder.btnTroubleHistory = (Button) rootView.findViewById(R.id.btn_trouble_history);
        viewHolder.btnReqIot = (Button) rootView.findViewById(R.id.btn_req_iot);

        rootView.findViewById(R.id.btn_find).setOnClickListener(this);
        rootView.findViewById(R.id.btn_call).setOnClickListener(this);
        rootView.findViewById(R.id.btn_sms).setOnClickListener(this);
        rootView.findViewById(R.id.btn_map).setOnClickListener(this);
        rootView.findViewById(R.id.btn_edit).setOnClickListener(this);
        rootView.findViewById(R.id.btn_req_iot).setOnClickListener(this);
        rootView.findViewById(R.id.btn_trouble_history).setOnClickListener(this);
        rootView.findViewById(R.id.btn_req1).setOnClickListener(this);
        rootView.findViewById(R.id.btn_req2).setOnClickListener(this);

        viewHolder.ivCheck.setOnClickListener(this);
        viewHolder.llRoot.setOnClickListener(this);

        rootView.setTag(viewHolder);

        mRecycleViews.add(rootView);

        return rootView;

    }

    @Override
    public void releaseResouces()
    {
        // TODO Auto-generated method stub

    }

    /*
     * public class BaseMaintenanceViewHolder { protected LinearLayout llRoot; protected TextView tvCarNum; protected TextView tvName; protected
     * TextView tvAddress; protected ImageView ivCheck; }
     */
    @Override
    public void onClick(View v)
    {
        // TODO Auto-generated method stub
        switch (v.getId())
        {
            case R.id.btn_find:
                clickFind(v);
                break;
            case R.id.btn_call:
                clickCall(v);
                break;
            case R.id.btn_sms:
                clickSms(v);
                break;
            case R.id.iv_check:
                clickCheck(v);
                break;
            case R.id.ll_root:
                clickRoot(v);
                break;
            case R.id.btn_edit:
                clickEdit(v);
                break;
            case R.id.btn_map:
                clickMap(v);
                break;
            case R.id.btn_trouble_history:
                clickTroubleHistory(v);
                break;
            case R.id.btn_req_iot:
                clickReqIot(v);
                break;
            case R.id.btn_req1:
                clickReq1(v);
                break;
            case R.id.btn_req2:
                clickReq2(v);
                break;
            default:
                break;
        }
    }

    private void clickFind(View v)
    {
        int position = (Integer) v.getTag();
        BaseMaintenanceModel model = getItem(position);

        HashMap<String, String> o_struct1 = new HashMap<String, String>();
        o_struct1.put("INVNR", model.getCarNum());
        o_struct1.put("MAKTX", model.getCarname());
        o_struct1.put("CONTNM", model.getCUSTOMER_NAME());
        o_struct1.put("CUSJUSO", model.getAddress());
        o_struct1.put("DLST1", model.getTel());
        o_struct1.put("EQUNR", model.getEQUNR());

        History_Dialog hd = new History_Dialog(mContext, o_struct1, 0);
        hd.show();

    }

    private void clickMap(View v)
    {

        GPS.startLocationReceiving();


        int position = (Integer) v.getTag();
        BaseMaintenanceModel model = getItem(position);

        String address = model.getAddress();
        // myung 20131202 ADD 올레네비 호출 시 우편번호는 전송하지 않아야 함.
        // 2017.07.06. 새주소는 우편번호가 5자리이다 "-"포함하면 6자리.
        // 일단 "-" 제거
        if(address != null){
            address = address.replaceFirst("-", "");
            while (true) {
                if(address.length() > 0){
                    char ch = address.charAt(0);
                    if (ch >= '0' && ch <= '9') {
                        address = address.replaceFirst("[0-9]", "");
                    } else {
                        break;
                    }
                }
            }
        }

//        if (address.length() > 7)
//            address = address.substring(7);
        // Log.i("address", address);

        if (address.equals("") || address.equals("   "))
        {
            showEventPopup2(null, "주소정보가 없어 올레네비를 실행할 수 없습니다.");
            return;
        }
        mBaseRepairFragment.showProgress("좌표를 얻어 오고 있습니다.");
        ConnectController connectController = new ConnectController(new ConnectInterface()
        {

            @Override
            public void reDownloadDB(String newVersion)
            {
                // TODO Auto-generated method stub

            }

            @Override
            public void connectResponse(String FuntionName, String resultText, String MTYPE, int resulCode, TableModel tableModel)
            {
                // TODO Auto-generated method stub
                mBaseRepairFragment.hideProgress();
                if (MTYPE.equals("S"))
                {
                    kog.e("Jonathan", "올레2 MTYPE == S 나옴.");
                    ArrayList<HashMap<String, String>> arrayList = tableModel.getTableArray();

                    if (arrayList.size() > 0)
                    {
                        HashMap<String, String> map = arrayList.get(0);
                        String tr_x = map.get("TR_X");
                        String tr_y = map.get("TR_Y");

                        kog.e("Jonathan", "올레2 tr_x ::	 " + tr_x + " 올레맵 tr_y :: " + tr_y);

                        /**
                         * 2017.07.06. 네비 맵 연동이 잘 되지 않아 gps 얻어오는 방식을 변경함
                         */
                        LocationManager locationManager;
                        Location location = null;
//                        locationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
//
//                        // Criteria 클래스로, 이용 가능한 provider 들 중 가장 적합한 것을
//                        // 고르도록 한다.
//                        Criteria criteria = new Criteria();
//                        criteria.setAccuracy(Criteria.ACCURACY_FINE);
//                        criteria.setAltitudeRequired(false);
//                        criteria.setBearingRequired(false);
//                        criteria.setCostAllowed(true);
//                        criteria.setPowerRequirement(Criteria.POWER_LOW);
//                        String provider = locationManager.getBestProvider(criteria, true);
//
//                        Location location = locationManager.getLastKnownLocation(provider);
//
//                        kog.e("Jonathan", "올레2 provider ::	 " + provider);
//                        kog.e("Jonathan", "올레2 Location ::	 " + String.valueOf(location));

                        locationManager = (LocationManager) mContext
                                .getSystemService(Context.LOCATION_SERVICE);

                        boolean isGPSEnabled = false;
                        boolean isNetworkEnabled = false;
                        // getting GPS status
                        isGPSEnabled = locationManager
                                .isProviderEnabled(LocationManager.GPS_PROVIDER);

                        // getting network status
                        isNetworkEnabled = locationManager
                                .isProviderEnabled(LocationManager.NETWORK_PROVIDER);

                        if (!isGPSEnabled && !isNetworkEnabled) {
                            // no network provider is enabled
                        } else {
                            if (isNetworkEnabled) {
                                if (locationManager != null) {
                                    location = locationManager
                                            .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                                }
                            }
                            // if GPS Enabled get lat/long using GPS Services
                            else if (isGPSEnabled) {
                                if (location == null) {
                                    if (locationManager != null) {
                                        location = locationManager
                                                .getLastKnownLocation(LocationManager.GPS_PROVIDER);
//													}
                                    }
                                }
                            }
                        }

                        try
                        {
                            double start_x = Double.parseDouble(tr_x);
                            double start_y = Double.parseDouble(tr_y);
                            Toast.makeText(mContext, "start_x = " + start_x + " start_y " + start_y, Toast.LENGTH_LONG).show();
                            // Log.d("", "location.getLongitude() "
                            // + location.getLongitude()
                            // + " location.getLatitude() "
                            // + location.getLatitude());
//                            CommonUtil.onOllehNavi(location.getLatitude(), location.getLongitude(), start_y, start_x, mContext.getPackageName(),
//                                    mContext);

                            CommonUtil.onOllehNavi(GPS.dbLat, GPS.dbLng, start_y, start_x, mContext.getPackageName(),
                                    mContext);

                            GPS.stopLocationReceiving();

                        }
                        catch (NumberFormatException e)
                        {
                            // TODO: handle exception
                        }

                    }
                }
                else
                {
                    mBaseRepairFragment.showEventPopup2(null, "실패 하였습니다.");
                }
            }
        }, mContext);

        connectController.addressToWGS(address);

    }

    private void clickTroubleHistory(View v) {
        BaseMaintenanceModel model = getItem((Integer)v.getTag());
        if (model.getVBELN().trim().isEmpty()) {
            EventPopupC popupC = new EventPopupC(mContext);
            popupC.show("고장코드이력이 없습니다.");
        } else {
            TroubleHistoryItemDialog dialog = new TroubleHistoryItemDialog(mContext, model.getVBELN());
            dialog.show();
        }
    }

    private void clickReqIot(View v) {
        BaseMaintenanceModel model = getItem((Integer)v.getTag());
        if (model.getREQNO().trim().isEmpty()) {
            EventPopupC popupC = new EventPopupC(mContext);
            popupC.show("IoT 요청항목이 없습니다.");
        } else {
            IoTRequestItemDialog dialog = new IoTRequestItemDialog(mContext, model.getREQNO());
            dialog.show();
        }
    }

    private void clickReq1(View v) {
        BaseMaintenanceModel model = getItem((Integer)v.getTag());
        if (model.getCCMRQ().trim().isEmpty()) {
            EventPopupC popupC = new EventPopupC(mContext);
            popupC.show("차기요청사항이 없습니다.");
        } else {
            SimpleTextDialog dialog = new SimpleTextDialog(mContext, "차기요청사항", model.getCCMRQ());
            dialog.show();
        }
    }

    private void clickReq2(View v) {
        BaseMaintenanceModel model = getItem((Integer)v.getTag());
        if (model.getPRERQ().trim().isEmpty()) {
            EventPopupC popupC = new EventPopupC(mContext);
            popupC.show("요청사항이 없습니다.");
        } else {
            SimpleTextDialog dialog = new SimpleTextDialog(mContext, "요청사항", model.getPRERQ());
            dialog.show();
        }

    }


    private void clickEdit(View v)
    {
        int position = (Integer) v.getTag();
        BaseMaintenanceModel model = getItem(position);

        kog.e("Jonathan", "getName() == " + model.getCUSTOMER_NAME() + "getNAME1() == " + model.getDRIVER_NAME());

        // myung 20131227 ADD

        int nCurrentDay = Integer.parseInt(model.getDay());
        int nGueen2 = Integer.parseInt(model.getGUEEN2());
        Log.e("nGueen2/nCurrentDay", "" + nGueen2 + "/" + nCurrentDay);

        if (nGueen2 < nCurrentDay)
        {
            EventPopupC epc = new EventPopupC(mContext);
            epc.show("계약종료되어 주소지변경이 불가합니다.");
            return;
        }

        final HashMap<String, String> o_struct1 = new HashMap<String, String>();
        o_struct1.put("INVNR", model.getCarNum());
        o_struct1.put("MAKTX", model.getCarname());
        o_struct1.put("AUFNR", model.getAUFNR());

        // o_struct1.put("CONTNM", model.getName());
        // KUNNR_NM
        o_struct1.put("NAME1", model.getCUSTOMER_NAME()); // 여기는 고객명 /// Jonathan 발견 getNAME1() 에는 아무것도 없다. 14.06.13
        o_struct1.put("KUNNR", model.getKUNNR()); //KUNNR
        
        
        o_struct1.put("DLSM1", model.getDRIVER_NAME()); // 여기는 운전자명//// getName->고객명 getNAME1->운전자명.
        o_struct1.put("DLST1", model.getTel());

        kog.e("KDH", "model.getTel() = " + model.getTel());
        kog.e("KDH", "model.getNAME1() = " + model.getDRIVER_NAME());

        o_struct1.put("CUSJUSO", model.getAddress());
        o_struct1.put("EQUNR", model.getEQUNR());

        o_struct1.put("PostCode", model.getPostCode());
        o_struct1.put("City", model.getCity());
        o_struct1.put("Street", model.getStreet());

        Set<String> set = o_struct1.keySet();
        Iterator<String> it = set.iterator();
        String key;

        while (it.hasNext())
        {
            key = it.next();
            kog.e("Jonathan", "  연필눌렀을때 들어오는   key ===" + key + "    value  === " + o_struct1.get(key));
        }

        kog.e("KDH", "getPostCode = " + model.getPostCode());
        kog.e("KDH", "getCity = " + model.getCity());
        kog.e("KDH", "getStreet = " + model.getStreet());

        final String equnr = model.getEQUNR();

        final Address_Change_Dialog acd = new Address_Change_Dialog(mContext, o_struct1);
        Button bt_save = (Button) acd.findViewById(R.id.address_change_save_id);
        bt_save.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View arg0)
            {
                // LoginModel lm = KtRentalApplication.getLoginModel();
                // LoginModel에서 INGRP 가져와야함 추가중.
                // showProgress();
                if (acd.getCity1() == "")
                {
                    EventPopupC epc = new EventPopupC(mContext);
                    epc.show("주소를 선택해 주세요");
                    return;
                }

                mBaseRepairFragment.showProgress();
                // myung 20131211 UPDATE 첫번째 param "120"을 로그인 시 받아온 INGRP정보로 세팅
                mConnectController.setZMO_1040_WR02(getTable(acd, equnr));

                CCMSTS = acd.getCCMSTS();

                acd.dismiss();
                notifyDataSetChanged();
            }
        });
        acd.show();
    }

    String CCMSTS = "";

    private void clickCall(View v)
    {
        int position = (Integer) v.getTag();
        BaseMaintenanceModel model = getItem(position);
        // myung 20131217 ADD SMS 발송 시 고객 전화번호가 없을 경우 메시지 출력 후 리턴 시킴.
        if ((model.getTel().equals("") || model.getTel().equals(" ")) && (model.getDrv_mob().equals("") || model.getDrv_mob().equals(" ")))
        {
            EventPopupC epc = new EventPopupC(mContext);
            epc.show("연락처가 없습니다. \n고객 연락처를 확인하여 주십시요.");
            return;
        }

        CallSendPopup callSendPopup = new CallSendPopup(mContext, model);
        callSendPopup.show();
    }

    private void clickSms(View v)
    {
        int position = (Integer) v.getTag();
        BaseMaintenanceModel model = getItem(position);
        // myung 20131217 ADD SMS 발송 시 고객 전화번호가 없을 경우 메시지 출력 후 리턴 시킴.
        if (model.getTel().equals("") || model.getTel().equals(" "))
        {
            EventPopupC epc = new EventPopupC(mContext);
            epc.show("연락처가 없습니다. \n고객 연락처를 확인하여 주십시요.");
            return;
        }

        if (model != null
                && model.getTel() != null
                && model.getTel().length() > 2
                && !model.getTel().substring(0, 2).equals("01"))
        {
            EventPopupC epc = new EventPopupC(mContext);
            epc.show("SMS를 발신할 수 없는 전화번호 입니다.");
            return;
        }
        SMSPopup popup = new SMSPopup(mContext, model);
        popup.show();
    }

    private void clickCheck(View v)
    {
        int position = (Integer) v.getTag();
        checkItem(position);

    }

    private void clickRoot(View v)
    {
        if (mOnClickRootView != null)
        {
            int position = (Integer) v.getTag();
            BaseMaintenanceModel model = getItem(position);
            mOnClickRootView.onClickRoot(model);

            initSelectedMaintenanceArray();
        }

    }

    public void checkItem(int position)
    {
        BaseMaintenanceModel model = null;

        model = getItem(position);

        if (model != null)
        {
            if (model.isCheck())
            {
                if (selectedMaintenanceModels.remove(model))
                    model.setCheck(false);
            }
            else
            {
                if (!checkAUFNR(model))
                    model.setCheck(false);
                // myung 20131230 ADD 순회정비 예정일 변경 -> 최대값 설정 30건
                else if (selectedMaintenanceModels.size() >= 30)
                {
                    EventPopupC epc = new EventPopupC(mContext);
                    epc.show("30건 이상 선택할 수 없습니다.");
                    model.setCheck(false);
                }
                else
                {
                    selectedMaintenanceModels.add(model);
                    model.setCheck(true);
                }

            }
            notifyDataSetChanged();
        }
    }

    private boolean checkAUFNR(BaseMaintenanceModel model)
    {
        String aufnr = model.getAUFNR();
        // Log.i("checkAUFNR", "aufnr : "+aufnr);

        if (aufnr == null || aufnr.equals("") || aufnr.equals(" "))
        {
//TODO
//            EventPopupC epc = new EventPopupC(mContext);
//            epc.show("정비번호가 없습니다. MOT 에 문의해 주세요.");

            // showEventPopup2(new OnEventOkListener() {
            //
            // @Override
            // public void onOk() {
            // // TODO Auto-generated method stub
            //
            // }
            // }, "정비번호가 없습니다. MOT 에 문의해 주세요.");
            return true;
        }
        // EventPopupC epc = new EventPopupC(mContext);
        // epc.show("정비번호가 없습니다. MOT 에 문의해 주세요.");
        return true;
    }

    public ArrayList<BaseMaintenanceModel> getSelectedMaintenanceModels()
    {
        return selectedMaintenanceModels;
    }

    public void initSelectedMaintenanceArray()
    {
        if(selectedMaintenanceModels != null) {
            for (int i = 0; i < selectedMaintenanceModels.size(); i++) {
                // myung 20131202 UPDATE 이관등록 화면팝업 시 이관대상고객 선택 리스트의 고객은 기본으로 체크되어
                // 있어야 함.
                selectedMaintenanceModels.get(i).setCheck(true);
            }
        }
        selectedMaintenanceModels = new ArrayList<BaseMaintenanceModel>();
        notifyDataSetChanged();
    }

    public void setDataArr(List<? extends BaseMaintenanceModel> arr)
    {
        if (mItemArray != null)
        {
            mItemArray.clear();
            mItemArray.addAll(arr);
            filtering();
        }
        notifyDataSetChanged();
    }

    @Override
    public int getCount()
    {

        int count = 0;

        if (mFilteringFlag)
        {
            count = mFilterMaintenanceModelArray.size();
        }
        else
        {
            count = super.getCount();
        }

        return count;
    }

    @Override
    public BaseMaintenanceModel getItem(int position)
    {
        BaseMaintenanceModel model = null;
        // 14.06.15 Jonathan 이쪽 받아오는 부분 들어가서 BaseCommonAdapter로 가게된다.

        if (!mFilterMaintenanceModelArray.isEmpty())
        {
            if (mFilterMaintenanceModelArray.size() > position)
                model = mFilterMaintenanceModelArray.get(position);
        }
        else
        {
            model = super.getItem(position);
        }
        return model;
    }

    @Override
    public void connectResponse(String FuntionName, String resultText, String MTYPE, int resulCode, TableModel tableModel)
    {
        // TODO Auto-generated method stub
        mBaseRepairFragment.hideProgress();

        if (FuntionName.equals("ZMO_1010_RD02")) {
			if (MTYPE == null || !MTYPE.equals("S")) {

				 final EventPopupC epc = new EventPopupC(mContext);
	             Button btn_confirm = (Button)epc.findViewById(R.id.btn_ok);
	             btn_confirm.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View arg0) {
							// TODO Auto-generated method stub
							epc.dismiss();
							((BaseActivity) mContext).finish();
							android.os.Process.killProcess(android.os.Process.myPid());
							System.exit(0);


						}
					});
	             epc.show(resultText);
			}
		}
        if (FuntionName.equals("ZMO_1040_WR02"))
        {

            if (MTYPE.equals("S"))
            {

                if (tableModel.getTableArray() != null)
                {
                    ArrayList<HashMap<String, String>> array = tableModel.getTableArray();
                    for (HashMap<String, String> hashMap : array)
                    {

                        String EQUNR = hashMap.get("EQUNR");

                        String postCode = hashMap.get("POS_POST");
                        String city = hashMap.get("POS_CITY1");
                        String street = hashMap.get("POS_STREET");
                        String equnr = hashMap.get("EQUNR");
                        String POS_TEL_NO = hashMap.get("POS_TEL_NO");
                        String POS_DRIVN = hashMap.get("POS_DRIVN"); // Jonathan 추가 14.06.13
                        // String CCMSTS = hashMap.get("CCMSTS"); // Jonathan 추가

                        // String POS_DRIVN = hashMap.get("POS_DRIVN");

                        kog.e("Jonathan", "BASEMAIN_ POS_TEL_NO = " + POS_TEL_NO);
                        kog.e("Jonathan", "BASEMAIN POS_DRIVN == " + POS_DRIVN);
                        // kog.e("KDH", "BASEMAIN_ CCMSTS = "+CCMSTS);
                        kog.e("Jonathan", "BASEMAIN_ EQUNR = " + EQUNR);

                        // mCarInfoModel.setAddress(postCode + city + street);
                        for (BaseMaintenanceModel model : mItemArray)
                        {
                            if (equnr.equals(model.getEQUNR()))
                            {
                                model.setAddress(postCode + city + street);
                                break;
                            }
                        }

                        // 이관요청으로 값을 변경. (DB에서 변경 E0003 으로)
                        // 주소 또한 변경.
                        kog.e("KDH", "beforeAddr = " + beforeAddr);
                        // String addr = "["+postCode+"]"+city+street;
                        String addr = "[" + postCode + "]" + city + street;
                        kog.e("KDH", "addr = " + addr);

                        for (int i = 0; i < mFilterMaintenanceModelArray.size(); i++)
                        {
                            BaseMaintenanceModel _data = mFilterMaintenanceModelArray.get(i);
                            if (_data.getEQUNR().equals(EQUNR))
                            {
                                // 2014-05-21 KDH 고객정보수정팝업에서 이걸로 가져오는지확인해야됨.
                                // _data.setDrv_mob(POS_TEL_NO);
                                _data.setTel(POS_TEL_NO);
                                _data.setDRIVER_NAME(POS_DRIVN); // Jonathan 추가 14.06.13
                                mFilterMaintenanceModelArray.set(i, _data);

                                break;
                            }
                        }

                        for (int i = 0; i < selectedMaintenanceModels.size(); i++)
                        {
                            BaseMaintenanceModel _data = selectedMaintenanceModels.get(i);
                            if (_data.getEQUNR().equals(EQUNR))
                            {
                                // 2014-05-21 KDH 고객정보수정팝업에서 이걸로 가져오는지확인해야됨.
                                // _data.setDrv_mob(POS_TEL_NO);
                                _data.setTel(POS_TEL_NO);
                                _data.setDRIVER_NAME(POS_DRIVN); // Jonathan 추가 14.06.13
                                selectedMaintenanceModels.set(i, _data);
                                break;
                            }
                        }

                        Log.e("Jonathan", "beforeAddr =====" + beforeAddr);
                        Log.e("Jonathan", "addresssss =====" + addr);

                        updateComplete(postCode, city, street, equnr, POS_DRIVN, POS_TEL_NO); // Jonathan 추가 14.06.17
                        // if(!beforeAddr.equals(addr))
                        // {

                        // }
                        // else {
                        // // 2014-05-21 KDH 여기다가 내가 할려다가말었네 개짜증나서=-_-=;
                        // // 여기서 리쿼리해야함..아놔
                        // kog.e("Jonathan", "여기 등어오냐? Jonathan.2");
                        // updateCompleteTelCh(CCMSTS, postCode, city, street,
                        // POS_TEL_NO, POS_DRIVN);
                        //
                        // }

                        // 리스트가 1개일경우 다시 순회정비 리스트로 이동.
                        // 2개 이상일경우 한개를 제거.

                    }

                }
                notifyDataSetChanged();
            }
        }
    }

    private ArrayList<HashMap<String, String>> getTable(Address_Change_Dialog acd, String equnr)
    {
        LoginModel lm = KtRentalApplication.getLoginModel();
        ArrayList<HashMap<String, String>> i_itab1 = new ArrayList<HashMap<String, String>>();
        for (int i = 0; i < 1; i++)
        {
            HashMap<String, String> hm = new HashMap<String, String>();
            // hm.put("INVNR", acd.getINVNR());// 고객차량 번호
            hm.put("EQUNR", equnr);// 고객차량 설비번호
            hm.put("PREEQU", lm.getEqunr()); // 이전 순회차량 설비번호
            hm.put("POS_POST", acd.getPost()); // 변경후 주소(우편번호) 입력
            hm.put("POS_CITY1", acd.getCity1()); // 변경후 주소(시도명)
            hm.put("POS_STREET", acd.getStreet()); // 변경후 주소(상세주소)
            hm.put("POS_DRIVN", acd.getDrivn()); // 변경후 운전자 이름
            hm.put("POS_TEL_NO", acd.getTel_No()); // 변경후 운전자연락처
            hm.put("POSEQU", acd.getEqunr()); // 이후 순회차량 설비번호
            hm.put("POSINGRP", acd.getMot()); // 변경후 MOT
            hm.put("PRE_POST", acd.getPrePost());
            hm.put("PRE_CITY1", acd.getPreCity());
            hm.put("PRE_STREET", acd.getPreStreet());
            hm.put("PRE_DRIVN", acd.getPreDrivn());
            hm.put("PRE_TEL_NO", acd.getPreTelNo());
            // hm.put("CCMSTS", acd.getCCMSTS()); // Jonathan 추가

            i_itab1.add(hm);
        }
        return i_itab1;
    }

    @Override
    public void reDownloadDB(String newVersion)
    {
        // TODO Auto-generated method stub

    }

    private void updateComplete(String postCode, String city, String street, String equnr, String pos_drivn, String drv_tel)
    {
        mBaseRepairFragment.showProgress();
        ContentValues contentValues = new ContentValues();
        contentValues.put("CCMSTS", "E0003");
        contentValues.put(DEFINE.POST_CODE, postCode);
        contentValues.put(DEFINE.CITY, city);
        contentValues.put(DEFINE.STREET, street);
        contentValues.put("EQUNR", equnr);
        contentValues.put(DEFINE.DRIVN, pos_drivn);// Jonathan 추가 14.06.13
        contentValues.put(DEFINE.DRV_TEL, decrypt(DEFINE.DRV_TEL, drv_tel));

        kog.e("Jonathan", "여기로 오는 구먼~!!!! jonathan.");

        String[] keys = new String[1];
        keys[0] = "EQUNR";

        DbAsyncTask dbAsyncTask = new DbAsyncTask("updateComplete", DEFINE.REPAIR_TABLE_NAME, mContext, this, contentValues, keys);

        dbAsyncTask.execute(DbAsyncTask.DB_UPDATE);

    }

    // private void updateCompleteTelCh(String CCMSTS, String postCode, String city, String street, String drv_tel) {
    //
    // ContentValues contentValues = new ContentValues();
    // contentValues.put("CCMSTS", CCMSTS);
    // contentValues.put(DEFINE.POST_CODE, postCode);
    // contentValues.put(DEFINE.CITY, city);
    // contentValues.put(DEFINE.STREET, street);
    // // contentValues.put(DEFINE.DRV_TEL, decrypt(DEFINE.DRV_TEL, drv_tel));
    // // contentValues.put("AUFNR", mCarInfoModel.getAUFNR());
    //
    // String[] keys = new String[1];
    // keys[0] = "AUFNR";
    //
    // DbAsyncTask dbAsyncTask = new DbAsyncTask("updateComplete",
    // DEFINE.REPAIR_TABLE_NAME, mContext, this, contentValues, keys);
    //
    // dbAsyncTask.execute(DbAsyncTask.DB_UPDATE);
    // }

    private void updateCompleteTelCh(String CCMSTS, String postCode, String city, String street, String drv_tel, String drv_name)
    {
        // showProgress();
        ContentValues contentValues = new ContentValues();
        contentValues.put("CCMSTS", CCMSTS);
        contentValues.put(DEFINE.POST_CODE, postCode);
        contentValues.put(DEFINE.CITY, city);
        contentValues.put(DEFINE.STREET, street);
        contentValues.put(DEFINE.DRV_TEL, decrypt(DEFINE.DRV_TEL, drv_tel));
        contentValues.put(DEFINE.DRIVN, drv_name); // Jonathan 추가 14.06.13
        // contentValues.put("AUFNR", mCarInfoModel.getAUFNR());

        // // 14.06.16 Jonathan 여기 업데이트 할때 여기로 안옴.... 다른 곳을 타는 것 같음.

        kog.e("Jonathan", "CCMSTS : " + CCMSTS + "POSTCODE : " + postCode + "CITY : " + city + "DRIVN : " + drv_name);
        // kog.e("KDH",
        // "updateCompleteTelCh getAUFNR = " + mCarInfoModel.getAUFNR());
        String[] keys = new String[1];
        keys[0] = "AUFNR";

        DbAsyncTask dbAsyncTask = new DbAsyncTask("updateComplete", DEFINE.REPAIR_TABLE_NAME, mContext, this, contentValues, keys);

        dbAsyncTask.execute(DbAsyncTask.DB_UPDATE);

    }

    private void updateComplete2()
    {
        mBaseRepairFragment.showProgress();
        ContentValues contentValues = new ContentValues();
        String[] keys = new String[1];
        keys[0] = "EQUNR";

        DbAsyncTask dbAsyncTask = new DbAsyncTask("updateComplete", DEFINE.REPAIR_TABLE_NAME, mContext, this, contentValues, keys);

        dbAsyncTask.execute(DbAsyncTask.DB_UPDATE);
    }

    protected String decrypt(String key, String value)
    {

        String reStr = value;

        if (KtRentalApplication.isEncoding(key))
        {
            reStr = ConnectorUtil.decrypt(value);
        }

        return reStr;
    }

    @Override
    public void onCompleteDB(String funName, int type, Cursor cursor, String tableName)
    {
        mBaseRepairFragment.hideProgress();
        if (funName.equals("updateComplete"))
        {

            kog.e("Jonathan", "업데이트 된거야?");
        }

    }

}
