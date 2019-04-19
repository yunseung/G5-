package com.ktrental.fragment;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Bundle;
import android.telephony.PhoneNumberUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.ktrental.R;
import com.ktrental.adapter.CheckingAdapter;
import com.ktrental.adapter.MaintenancLastItemAdapter;
import com.ktrental.cm.connect.ConnectController;
import com.ktrental.cm.connect.Connector.ConnectInterface;
import com.ktrental.cm.db.DbAsyncTask;
import com.ktrental.cm.db.DbAsyncTask.DbAsyncResLintener;
import com.ktrental.cm.util.PrintLog;
import com.ktrental.common.AppSt;
import com.ktrental.common.DEFINE;
import com.ktrental.common.KtRentalApplication;
import com.ktrental.fragment.MaintenanceResultResistFragment.OnCheckingResult;
import com.ktrental.fragment.SignFragment.OnSignListener;
import com.ktrental.model.CarInfoModel;
import com.ktrental.model.CheckModel;
import com.ktrental.model.DbQueryModel;
import com.ktrental.model.EtcModel;
import com.ktrental.model.LoginModel;
import com.ktrental.model.MaintenanceGroupModel;
import com.ktrental.model.MaintenanceItemModel;
import com.ktrental.model.MaintenanceResultModel;
import com.ktrental.model.MaintenanceSendBaseModel;
import com.ktrental.model.MaintenanceSendModel;
import com.ktrental.model.MaintenanceSendSignModel;
import com.ktrental.model.MaintenanceSendStockModel;
import com.ktrental.model.O_ITAB1;
import com.ktrental.model.TableModel;
import com.ktrental.popup.InventoryPopup;
import com.ktrental.popup.InventoryPopup.OnDismissListener;
import com.ktrental.popup.QuickAction;
import com.ktrental.ui.PopupWindowTextView;
import com.ktrental.ui.PopupWindowTextView.OnLayoutListener;
import com.ktrental.util.CommonUtil;
import com.ktrental.util.OnEventOkListener;
import com.ktrental.util.kog;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;

public class CheckingCompleteFragment extends BaseFragment
        implements OnClickListener, DbAsyncResLintener, OnSignListener, ConnectInterface {

    private ListView mLvCheking;
    private CheckingAdapter mCheckingAdapter;
    private OnCheckingResult mOnCheckingResult;
    private Button mBtnComplate;

    private ImageView iv_confirm_check, iv_confirm_check2;

    //2017-11-30. hjt. 순회점검을 위한 레이아웃 추가
    private ImageView iv_tire_check;
    private EditText tire_size;
    private LinearLayout li_tire;

    private MaintenanceResultModel mResultModel;

    private ArrayList<MaintenanceItemModel> mLastItemModels = new ArrayList<MaintenanceItemModel>();

    private ArrayList<CheckModel> mCheckModels = new ArrayList<CheckModel>();

    private TextView mTvDate;
    private TextView mTvStartTime;
    private TextView mTvEndTime;
    private TextView mTvDistance;
    private EditText mEtName;
    // private FrameLayout mBtnComplateDummy2560;
    private PopupWindowTextView mTvContact;

    private final String EMPTY_TEXT = "-1";
    private String mSignImageName = EMPTY_TEXT;

    private ArrayList<MaintenanceGroupModel> mMaintenanceGroupModels = new ArrayList<MaintenanceGroupModel>();

    private ConnectController mConnectController;
    private ArrayList<MaintenanceSendSignModel> mMaintenanceSendSignModels = new ArrayList<MaintenanceSendSignModel>();

    private MaintenanceSendModel mSendModel;

    private OnSendResult mOnSendResult;

    private int[] mInventoryLocation = new int[2];

    private boolean firstFlag = true;

    private String mLastName = "";
    private String mLastContact = "";

    private boolean mIsInspect = false;
    // anchor.getLocationOnScreen(location);

    private final String InspectMatnrFront = "400893";
    private final String InspectMatnrRear = "400894";

    // 최종 점검 항목 리스트
    private ListView mLvLast;
    private TextView mTvLastTotalPrice;
    private int mTotalPrice = 0;
    private MaintenancLastItemAdapter mLastItemAdapter;

    private LinearLayout mLlSpecificationArea, mLlSpecificationArea2;

    public interface OnSendResult {
        void onSendResult(String name);
    }

    public void setResultModel(MaintenanceResultModel aResultModel) {
        this.mResultModel = aResultModel;
        String gubun = mResultModel.getmCarInfoModel().get_gubun();
        mLastItemModels = aResultModel.getmLastItemModels();

        if (gubun.trim().equals("A")) {
            mLastItemAdapter = new MaintenancLastItemAdapter(mContext, MaintenancLastItemAdapter.INPUT, gubun);
            mLastItemAdapter.setData(mLastItemModels);
            mLvLast.setAdapter(mLastItemAdapter);

            for (MaintenanceItemModel item : mLastItemModels) {
                mTotalPrice += (Integer.parseInt(item.getNETPR().replace(",", "")) * item.getConsumption());
            }
            mTvLastTotalPrice.setText(currencyFormat(mTotalPrice) + "원");

            mLlSpecificationArea.setVisibility(View.GONE);
            mLlSpecificationArea2.setVisibility(View.VISIBLE);
        } else {
            mCheckModels.clear();
            mMaintenanceGroupModels.clear();
            mCheckingAdapter.initArray();
            queryGroup();

        }
        initText(aResultModel.getmEtcModel(), aResultModel.getmCarInfoModel());

    }

    public CheckingCompleteFragment() {
    }

    public CheckingCompleteFragment(OnCheckingResult aOnCheckingResult, OnSendResult onSendResult) {
        mOnCheckingResult = aOnCheckingResult;
        mOnSendResult = onSendResult;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.checking_complate_layout, null);

        initSettinViews();

        mConnectController = new ConnectController(this, mContext);

        return mRootView;
    }

    private void initSettinViews() {
        mLvCheking = (ListView) mRootView.findViewById(R.id.lv_checking);
        mCheckingAdapter = new CheckingAdapter(mContext);
        mLvCheking.setAdapter(mCheckingAdapter);

        mBtnComplate = (Button) mRootView.findViewById(R.id.btn_complate);
        mBtnComplate.setOnClickListener(this);
        mTvDate = (TextView) mRootView.findViewById(R.id.tv_checking_day);
        mTvStartTime = (TextView) mRootView.findViewById(R.id.tv_checking_start_time);
        mTvEndTime = (TextView) mRootView.findViewById(R.id.tv_checking_end_time);
        mTvDistance = (TextView) mRootView.findViewById(R.id.tv_checking_distance);
        mEtName = (EditText) mRootView.findViewById(R.id.et_name);

        mTvContact = (PopupWindowTextView) mRootView.findViewById(R.id.tv_contact);
        mTvContact.setOnClickListener(this);
        mTvContact.setOnLayoutListener(new OnLayoutListener() {

            @Override
            public void onLayout() {
                if (firstFlag) {
                    mTvContact.getLocationOnScreen(mInventoryLocation);
                    firstFlag = false;
                }
            }
        });
        // mBtnComplateDummy2560 = (FrameLayout)
        // mRootView.findViewById(R.id.btn_complate_dummy_2560);

        iv_confirm_check = (ImageView) mRootView.findViewById(R.id.iv_confirm_check);
        iv_confirm_check2 = (ImageView) mRootView.findViewById(R.id.iv_confirm_check2);
        iv_confirm_check.setOnClickListener(this);
        iv_confirm_check2.setOnClickListener(this);

        iv_tire_check = (ImageView) mRootView.findViewById(R.id.iv_tire_check);
        iv_tire_check.setOnClickListener(this);

        li_tire = (LinearLayout) mRootView.findViewById(R.id.li_tire);
        tire_size = (EditText) mRootView.findViewById(R.id.tire_size);

        mRootView.findViewById(R.id.btn_sign).setOnClickListener(this);

        // myung 20131120 ADD
        // if (DEFINE.DISPLAY.equals("2560"))
        // {
        mRootView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, 1420));
        // }


        mLvLast = (ListView) mRootView.findViewById(R.id.lv_last_item);

        mTvLastTotalPrice = (TextView) mRootView.findViewById(R.id.tv_last_total_price);

        mLlSpecificationArea = (LinearLayout) mRootView.findViewById(R.id.ll_specification_area);
        mLlSpecificationArea2 = (LinearLayout) mRootView.findViewById(R.id.ll_specification_area2);

    }

    @Override
    public void onClick(final View v) {
        CommonUtil.hideKeyboad(mContext, mEtName);
        switch (v.getId()) {
            case R.id.btn_complate:
                kog.e("KDH", "btn_complate");
                clickComplate();
                break;

            case R.id.btn_sign:
                clickSign();
                break;
            case R.id.tv_contact:
                final InventoryPopup inventoryPopup = new InventoryPopup(mContext, QuickAction.HORIZONTAL,
                        R.layout.inventory_popup, InventoryPopup.TYPE_PHONE_NUMBER);
                inventoryPopup.show(v, mInventoryLocation);
                // mRootView.postDelayed(new Runnable() {
                //
                // @Override
                // public void run() {
                // // TODO Auto-generated method stub
                // inventoryPopup.show(v, mInventoryLocation);
                // }
                // }, 200);
                inventoryPopup.setOnDismissListener(new OnDismissListener() {

                    @Override
                    public void onDismiss(String result, int position) {
                        // TODO Auto-generated method stub
                        mLastContact = result;
                        mTvContact.setText(result);
                    }
                });

                break;


            case R.id.iv_confirm_check:

                if (iv_confirm_check.isSelected()) {
                    iv_confirm_check.setSelected(false);
                    iv_confirm_check.setImageDrawable(getResources().getDrawable(R.drawable.check_off));
                } else {
                    iv_confirm_check.setSelected(true);
                    iv_confirm_check.setImageDrawable(getResources().getDrawable(R.drawable.check_on));
                }
                break;

            case R.id.iv_confirm_check2:

                if (iv_confirm_check2.isSelected()) {
                    iv_confirm_check2.setSelected(false);
                    iv_confirm_check2.setImageDrawable(getResources().getDrawable(R.drawable.check_off));
                } else {
                    iv_confirm_check2.setSelected(true);
                    iv_confirm_check2.setImageDrawable(getResources().getDrawable(R.drawable.check_on));
                }
                break;

            case R.id.iv_tire_check:
                if (iv_tire_check.isSelected()) {
                    iv_tire_check.setSelected(false);
                    iv_tire_check.setImageDrawable(getResources().getDrawable(R.drawable.check_off));
                    tire_size.setEnabled(false);
                    tire_size.setSelected(false);
                } else {
                    iv_tire_check.setSelected(true);
                    iv_tire_check.setImageDrawable(getResources().getDrawable(R.drawable.check_on));
                    tire_size.setEnabled(true);
                }
            default:
                break;
        }

    }

    private void clickSign() {

        if (mResultModel != null) {
            kog.e("KDH", "mResultModel.getmCarInfoModel().getAUFNR() = " + mResultModel.getmCarInfoModel().getAUFNR());
            SignFragment signFragment = new SignFragment(mEtName.getText().toString(), mTvContact.getText().toString(),
                    this, mResultModel.getmCarInfoModel().getAUFNR(),
                    // mResultModel.getmCarInfoModel().getImageName(),
                    mResultModel.getmCarInfoModel().getCUSTOMER_NAME(), mResultModel.getmCarInfoModel().getCarNum());

            // myung 20131121 2560대응
            // int tempX = 1060;
            // int tempY = 614;
            // if (DEFINE.DISPLAY.equals("2560"))
            // {
            // tempX *= 2;
            // tempY *= 2;
            // }
            signFragment.show(getChildFragmentManager(), "SignFragment");// ,
            // tempX,
            // tempY);
        }
    }

    private void initCheckModels() {

        // for (int i = 0; i < mLastItemModels.size(); i++) {
        // CheckModel model = new CheckModel();
        //
        // MaintenanceItemModel left = mLastItemModels.get(i);
        // MaintenanceItemModel riModel = null;
        // int right = i + 1;
        // if (right < mLastItemModels.size())
        // riModel = mLastItemModels.get(right);
        //
        // model.setLeftModel(left);
        // model.setLeftModel(riModel);
        //
        // i++;
        // mCheckModels.add(model);
        // }
//		for(int k=0; k < mCheckModels.size(); k++){
//			CheckModel model = mCheckModels.get(k);
//			String matnr = model.getLeftModel().getMATNR();
//			if(matnr != null && matnr.equals(InspectMatnrFront)){
//
//			}
//		}

        for (int i = 0; i < mCheckModels.size(); i++) {
            CheckModel model = mCheckModels.get(i);

            MaintenanceItemModel left = model.getLeftModel();
            MaintenanceItemModel riModel = model.getRiItemModel();

            for (int j = 0; j < mLastItemModels.size(); j++) {
                MaintenanceItemModel itemModel = mLastItemModels.get(j);
                if (left.getGRP_CD().equals(itemModel.getGRP_CD())) {

                    int consumption = left.getConsumption() + itemModel.getConsumption();

                    left.setConsumption(consumption);
                }
                if (riModel != null) {

                    if (riModel.getGRP_CD().equals(itemModel.getGRP_CD())) {

                        int consumption = riModel.getConsumption() + itemModel.getConsumption();

                        riModel.setConsumption(consumption);
                    }
                }
                if (left.getMaintenanceGroupModel().getMATNR() != null && left.getMaintenanceGroupModel().getMATNR().equals(itemModel.getMATNR())) {
                    int consumption = left.getConsumption() + itemModel.getConsumption();
                    left.setConsumption(consumption);
                }
                if (riModel != null && riModel.getMaintenanceGroupModel().getMATNR() != null && riModel.getMaintenanceGroupModel().getMATNR().equals(itemModel.getMATNR())) {
                    int consumption = riModel.getConsumption() + itemModel.getConsumption();
                    riModel.setConsumption(consumption);
                }
            }
        }

        mCheckingAdapter.setData(mCheckModels);

        if (mIsInspect) {
            if (li_tire != null) {
                li_tire.setVisibility(View.VISIBLE);
            }
        } else {
            li_tire.setVisibility(View.GONE);
        }
        hideProgress();

    }

    private void initText(EtcModel etc, CarInfoModel carInfoModel) {

        String startTime = etc.getStartTime().substring(0, 4);
        String endTime = etc.getEndTime().substring(0, 4);

        mTvStartTime.setText(CommonUtil.setDotTime(startTime));
        mTvEndTime.setText(CommonUtil.setDotTime(endTime));
        mTvDistance.setText(CommonUtil.currentpoint(etc.getDistance()) + " km");

        mLastName = mEtName.getText().toString();
        kog.e("Jonathan", "고객 확인 고객명 :::" + carInfoModel.getCUSTOMER_NAME());
        kog.e("Jonathan", "고객 확인 운전자명:::" + carInfoModel.getDRIVER_NAME());

        if (mLastName.equals(""))
            mEtName.setText(carInfoModel.getCUSTOMER_NAME());
        else {
            mEtName.setText(mLastName);
        }

        // mEtName.setText(carInfoModel.getName());

        // myung 20131211 UPDATE 고객확인 고객전화번호 리스트 중 가장 마지막으로 세팅
        // String phoneNumber = PhoneNumberUtils.formatNumber(""
        // + carInfoModel.getTel());
        String phoneNumber = PhoneNumberUtils.formatNumber("" + carInfoModel.getDrv_mob());
        if (phoneNumber.equals(""))
            phoneNumber = PhoneNumberUtils.formatNumber("" + carInfoModel.getDrv_tel());

        if (mLastContact.equals(""))
            mTvContact.setText(phoneNumber);
        else {
            mTvContact.setText(mLastContact);
        }
        mTvDate.setText(CommonUtil.setDotDate(CommonUtil.getCurrentDay()));

    }

    private void queryGroup() {
        showProgress();
        String[] _whereArgs = {"PM023"};
        String[] _whereCause = {"ZCODEH"};

        String[] colums = {"ZCODEVT", "ZCODEV"};

        DbQueryModel dbQueryModel = new DbQueryModel(ConnectController.O_ITAB1_TABLE_NAME, _whereCause, _whereArgs,
                colums);

        DbAsyncTask dbAsyncTask = new DbAsyncTask("queryGroup", mContext, this, dbQueryModel);
        dbAsyncTask.execute(DbAsyncTask.DB_SELECT);
    }

    @Override
    public void onCompleteDB(String funName, int type, Cursor cursor, String tableName) {
        // TODO Auto-generated method stub
        if (funName.equals("queryGroup")) {
            if (cursor == null)
                return;
            responseGroup(cursor);

        } else if (funName.equals("updateComplete")) {
            updateStockComplete();

        } else if (funName.equals(DEFINE.REPAIR_RESULT_IMAGE_TABLE_NAME)) {

        } else if (funName.equals("updateStockComplete")) {
            KtRentalApplication.changeRepair();
            mOnCheckingResult.onCheckingResult(false, null);
        }
    }

    private void responseGroupForIoT() {

    }

    private void responseGroup(Cursor cursor) {
        cursor.moveToFirst();

        MaintenanceGroupModel maintenanceGroupModel = null;

        while (!cursor.isAfterLast()) {

            maintenanceGroupModel = new MaintenanceGroupModel(cursor.getString(0), cursor.getString(1));
            mMaintenanceGroupModels.add(maintenanceGroupModel);
            cursor.moveToNext();

        }
        cursor.close();

        String cycmn_tx = mResultModel.getmCarInfoModel().getTourMainenancePeriod();


        if (cycmn_tx != null && cycmn_tx.contains("점검")) {
            mIsInspect = true;
        }
        if (mIsInspect) {
            for (int i = 0; i < mMaintenanceGroupModels.size(); i++) {
                String name = mMaintenanceGroupModels.get(i).getName();
                if (name != null) {
                    if (name.equals("에어크리너") || name.equals("오일필터") || name.contains("배터리") || name.contains("(전/후)")) {
                        if (i < mMaintenanceGroupModels.size()) {
                            mMaintenanceGroupModels.remove(i);
                        }
                        if (name.contains("(전/후)")) {
//							for(int j=0; j<mResultModel.getmLastItemModels().size(); j++){
//								String matnr = mResultModel.getmLastItemModels().get(j).getMATNR();
//								if(matnr.equals("400894")){
                            MaintenanceGroupModel model = new MaintenanceGroupModel("라이닝 점검(후)", "inspect", InspectMatnrRear, 0);

                            mMaintenanceGroupModels.add(i, model);
//								} else if(matnr.equals("400893")){
                            model = new MaintenanceGroupModel("라이닝 점검(전)", "inspect", InspectMatnrFront, 0);
                            mMaintenanceGroupModels.add(i, model);
//								}
//							}
                        }
                    }

                }
            }
        }

        for (int i = 0; i < mMaintenanceGroupModels.size(); i++) {

            CheckModel model = new CheckModel();
            MaintenanceItemModel left = new MaintenanceItemModel(mMaintenanceGroupModels.get(i),
                    mMaintenanceGroupModels.get(i).getName_key());
            MaintenanceItemModel riModel = null;
            int right = i + 1;
            if (right < mMaintenanceGroupModels.size())
                riModel = new MaintenanceItemModel(mMaintenanceGroupModels.get(right),
                        mMaintenanceGroupModels.get(right).getName_key());

            model.setLeftModel(left);
            model.setRiItemModel(riModel);

            mCheckModels.add(model);
            i++;

        }
        initCheckModels();
    }

    private void clickComplate() {

//		kog.e("Jonathan", "주소가 도대체 뭐야 ? " + mResultModel.getmCarInfoModel().getAddress());
//		Log.e("mEtName", "" + mEtName.getText().toString().trim().length());

        if (mResultModel.getmCarInfoModel().get_gubun().trim().equals("A")) {
            if (!iv_confirm_check2.isSelected()) {
                showEventPopup2(null, "순회정비 유의 사항을 확인해 주세요. ");
                return;
            }
        } else {
            if (!iv_confirm_check.isSelected()) {
                showEventPopup2(null, "순회정비 유의 사항을 확인해 주세요.");
                return;
            }
        }
        if (iv_tire_check != null && iv_tire_check.isEnabled() == true && iv_tire_check.isSelected()) {
            if (tire_size != null && tire_size.getText() != null
                    && tire_size.getText().toString() != null && tire_size.getText().toString().equals("")) {
                showEventPopup2(null, "타이어 사이즈를 입력해 주세요. ");
                return;
            }
        }


        if (mMaintenanceSendSignModels.size() < 1) {
            showEventPopup2(new OnEventOkListener() {

                @Override
                public void onOk() {
                    // TODO Auto-generated method stub
                    clickSign();
                }
            }, "서명을 먼저 해주세요. ");
            return;
        }

        // myung 20131216 UPDATE 고객명이 있어도 고객명 입력팝업 창이 뜸.
        if (mEtName.getText().toString().trim().length() < 1) {
            showEventPopup2(null, "이름을 입력해 주세요. ");
            return;
        }

        if (mEtName.getText().toString().trim().length() < 2) {
            showEventPopup2(null, "이름을 2자 이상 입력해 주세요. ");
            return;
        }

        if (mTvContact.getText().toString().length() < 6) {
            showEventPopup2(new OnEventOkListener() {

                @Override
                public void onOk() {
                    // TODO Auto-generated method stub
                    final InventoryPopup inventoryPopup = new InventoryPopup(mContext, QuickAction.HORIZONTAL,
                            R.layout.inventory_popup, InventoryPopup.TYPE_PHONE_NUMBER);
                    mRootView.postDelayed(new Runnable() {

                        @Override
                        public void run() {
                            // TODO Auto-generated method stub
                            inventoryPopup.show(mTvContact);
                        }
                    }, 200);
                    inventoryPopup.setOnDismissListener(new OnDismissListener() {

                        @Override
                        public void onDismiss(String result, int position) {
                            // TODO Auto-generated method stub
                            mTvContact.setText(result);
                        }
                    });
                }
            }, "연락처를 입력해주세요. ");
            return;
        }

        // myung 20131226 ADD
        MaintenanceResultFragment.mTempResultModelMap.clear();
        MaintenanceResultFragment.mTempResultModelMap.putAll(MaintenanceResultFragment.mResultModelMap);

        // myung 20131231 ADD 점검결과 등록 BACK버튼 클릭시 확인팝업창 뛰우기
        DEFINE.RESIST_RESULT_FIRST_FLAG = true;

        // 2014-01-20 KDH 여기서 결과등록을 하는데..뭔가이상하네..-.;
        mOnSendResult.onSendResult(mResultModel.getmCarInfoModel().getAddress());
        kog.e("Jonathan", "checking Complete 1");

        // MaintenanceSendBaseModel sendBaseModel = getSendBaseModel();
        // ArrayList<MaintenanceSendStockModel> sendStockModels =
        // getSendStockModel();
        //
        // sendBaseModel.setSIGN_T(mMaintenanceSendSignModels.get(0).getSIGN_T());
        //
        // mSendModel = new MaintenanceSendModel(sendBaseModel, sendStockModels,
        // mMaintenanceSendSignModels);
        //
        // mConnectController.sendMaintenance(mSendModel);
        //
        // saveResultDataBase();

        // ContentValues contentValues = new ContentValues();
        // contentValues.put("CCMSTS", "E0004");
        // contentValues.put("GSTRS", mResultModel.getmCarInfoModel().getDay());
        // contentValues.put("AUFNR",
        // mResultModel.getmCarInfoModel().getAUFNR());
        //
        // String[] keys = new String[2];
        // keys[0] = "GSTRS";
        // keys[1] = "AUFNR";
        //
        // DbAsyncTask dbAsyncTask = new DbAsyncTask("SendResult",
        // DEFINE.REPAIR_TABLE_NAME, mContext, this, contentValues, keys);
        //
        // dbAsyncTask.execute(DbAsyncTask.DB_UPDATE);
    }

    private MaintenanceSendBaseModel getSendBaseModel() {

        CarInfoModel carInfoModel = mResultModel.getmCarInfoModel();
        EtcModel etcModel = mResultModel.getmEtcModel();
        String CEMER = " ";
        if (KtRentalApplication.getEmergency(carInfoModel.getCarNum()))
            CEMER = "X";

        String tiresize = "";

        if (tire_size != null && tire_size.getText() != null) {
            tiresize = tire_size.getText().toString();
        }
        String tirecheck = "";
        if (iv_tire_check.isSelected()) {
            tirecheck = "X";
        }

        MaintenanceSendBaseModel sendBaseModel = new MaintenanceSendBaseModel(carInfoModel.getAUFNR(),
                carInfoModel.getTourCarNum(), CEMER, carInfoModel.getCarNum(), etcModel.getDistance(),
                etcModel.getEmergency(), CommonUtil.getCurrentDay(), etcModel.getStartTime(),
                CommonUtil.getCurrentDay(), etcModel.getEndTime(), etcModel.getRemarks(), etcModel.getNextRequest(),
                etcModel.getContactRequest(), carInfoModel.getCUSTOMER_NAME(), tirecheck, tiresize);

        sendBaseModel.setGUBUN(carInfoModel.getGUBUN());
        sendBaseModel.setNAME1(mEtName.getText().toString());
        String phoneNumber = mTvContact.getText().toString();

        phoneNumber = PhoneNumberUtils.formatNumber("" + phoneNumber);
        sendBaseModel.setTELF1(phoneNumber);
        sendBaseModel.setSIGN_T(mSignImageName);
        return sendBaseModel;
    }

    private MaintenanceSendBaseModel getSendBaseModel(MaintenanceResultModel resultModel) {

        CarInfoModel carInfoModel = resultModel.getmCarInfoModel();
        EtcModel etcModel = resultModel.getmEtcModel();
        String CEMER = " ";
        if (KtRentalApplication.getEmergency(carInfoModel.getCarNum()))
            CEMER = "X";

        String tiresize = "";

        if (tire_size != null && tire_size.getText() != null) {
            tiresize = tire_size.getText().toString();
        }
        String tirecheck = "";
        if (iv_tire_check.isSelected()) {
            tirecheck = "X";
        }
        MaintenanceSendBaseModel sendBaseModel = new MaintenanceSendBaseModel(carInfoModel.getAUFNR(),
                carInfoModel.getTourCarNum(), CEMER, carInfoModel.getCarNum(), etcModel.getDistance(),
                etcModel.getEmergency(), CommonUtil.getCurrentDay(), etcModel.getStartTime(),
                CommonUtil.getCurrentDay(), etcModel.getEndTime(), etcModel.getRemarks(), etcModel.getNextRequest(),
                etcModel.getContactRequest(), carInfoModel.getCUSTOMER_NAME(), tirecheck, tiresize);

        sendBaseModel.setGUBUN(carInfoModel.getGUBUN());
        sendBaseModel.setNAME1(mEtName.getText().toString());
        String phoneNumber = mTvContact.getText().toString();

        phoneNumber = PhoneNumberUtils.formatNumber("" + phoneNumber);
        sendBaseModel.setTELF1(phoneNumber);
        sendBaseModel.setSIGN_T(mSignImageName);
        return sendBaseModel;
    }

    private ArrayList<MaintenanceSendStockModel> getSendStockModel() {

        CarInfoModel carInfoModel = mResultModel.getmCarInfoModel();
        ArrayList<MaintenanceItemModel> array = mResultModel.getmLastItemModels();

        ArrayList<MaintenanceSendStockModel> sendStockModels = new ArrayList<MaintenanceSendStockModel>();
        MaintenanceSendStockModel sendStockModel = null;
        LoginModel loginModel = KtRentalApplication.getLoginModel();

        for (MaintenanceItemModel maintenanceItemModel : array) {
            sendStockModel = new MaintenanceSendStockModel(carInfoModel.getAUFNR(), carInfoModel.getTourCarNum(),
                    maintenanceItemModel.getMATNR(), "" + maintenanceItemModel.getConsumption(),
                    maintenanceItemModel.getERFME(), loginModel.getLgort(), loginModel.getWerks(),
                    maintenanceItemModel.getMaintenanceGroupModel().getName_key(), carInfoModel.getCUSTOMER_NAME(),
                    carInfoModel.getCarNum(), maintenanceItemModel.getGRP_CD(), maintenanceItemModel.getNETPR());
            sendStockModels.add(sendStockModel);
        }

        return sendStockModels;
    }

    private ArrayList<MaintenanceSendStockModel> getSendStockModel(MaintenanceResultModel resultModel) {

        CarInfoModel carInfoModel = resultModel.getmCarInfoModel();
        ArrayList<MaintenanceItemModel> array = resultModel.getmLastItemModels();

        ArrayList<MaintenanceSendStockModel> sendStockModels = new ArrayList<MaintenanceSendStockModel>();
        MaintenanceSendStockModel sendStockModel = null;
        LoginModel loginModel = KtRentalApplication.getLoginModel();

        for (MaintenanceItemModel maintenanceItemModel : array) {
            Log.e("############",
                    "고객차량 번호 = " + carInfoModel.getCarNum() + ", GRP_CD = " + maintenanceItemModel.getGRP_CD());
            sendStockModel = new MaintenanceSendStockModel(carInfoModel.getAUFNR(), carInfoModel.getTourCarNum(),
                    maintenanceItemModel.getMATNR(), "" + maintenanceItemModel.getConsumption(),
                    maintenanceItemModel.getERFME(), loginModel.getLgort(), loginModel.getWerks(),
                    maintenanceItemModel.getMaintenanceGroupModel().getName_key(), carInfoModel.getCUSTOMER_NAME(),
                    carInfoModel.getCarNum(), maintenanceItemModel.getGRP_CD(), maintenanceItemModel.getNETPR());
            sendStockModels.add(sendStockModel);
        }

        return sendStockModels;
    }

    @Override
    public void onSignComplate(ArrayList<MaintenanceSendSignModel> sendSignModels, String name, String contact) {
        mEtName.setText(name);
        mTvContact.setText(contact);
        // TODO Auto-generated method stub
        mMaintenanceSendSignModels = sendSignModels;
    }

    @Override
    public void connectResponse(String FuntionName, String resultText, String MTYPE, int resulCode,
                                TableModel tableModel) {

//		PrintLog.Print("FuntionName", FuntionName);
//
//		kog.e("Jonathan", "FuntionName  ::: " + FuntionName);

        // 완료로 바꿔준다.
        updateComplete();

        if (MTYPE != null) {
            HashMap<String, String> o_itab1 = tableModel.getStruct("O_ITAB1");

            if (MTYPE.equals("S")) { // 결광등록 성공.
                // myung 20131209 ADD 오더번호 미전송에 의한 실패
                // 리턴처리
                for (int i = 0; i < o_itab1.size(); i++) {
                    PrintLog.Print("MTYPE", "" + o_itab1.get("MTYPE"));
                    if (o_itab1.get("MTYPE").toString().equals("E")) {
                        // myung 20131212 UPDATE 등록완료 후 이전화면으로 이동
                        // showEventPopup2(null,
                        // o_itab1.get("MESSAGE").toString());
                        showEventPopup2(new OnEventOkListener() {
                            @Override
                            public void onOk() {
                                // TODO Auto-generated method stub
                                // Main_Activity activity = (Main_Activity)
                                // getActivity();
                                // if (activity != null)
                                // activity.onBackPressed();
                            }
                        }, o_itab1.get("MESSAGE").toString());
                        return;
                    }
                }

                // 성공하였기때문에 임시저장해둔 결과등록을 지워준다.
                deleteResultDataBase(DEFINE.REPAIR_RESULT_BASE_TABLE_NAME);
                deleteResultDataBase(DEFINE.REPAIR_RESULT_STOCK_TABLE_NAME);
                deleteResultDataBase(DEFINE.REPAIR_RESULT_IMAGE_TABLE_NAME);
            } else { // 결과등록 실패
                // 2014-01-22 KDH 결과등록 실패시 여기로 떨궈지는데..이상하네?

            }
            AppSt.map_log(o_itab1);

        }

        // myung 20131212 UPDATE 등록완료 후 이전화면으로 이동
        // showEventPopup2(null, resultText);
        showEventPopup2(new OnEventOkListener() {
            @Override
            public void onOk() {
                // TODO Auto-generated method stub
                // Main_Activity activity = (Main_Activity) getActivity();
                // if (activity != null){
                // Log.e("activity.onBackPressed()",
                // "activity.onBackPressed()");
                // activity.onBackPressed();
                // }
            }
        }, resultText);

    }

    @Override
    public void reDownloadDB(String newVersion) {
        // TODO Auto-generated method stub
    }

    private void updateComplete() {
        ContentValues contentValues = new ContentValues();
        contentValues.put("CCMSTS", "E0004");
        contentValues.put("GSTRS", mResultModel.getmCarInfoModel().getDay());
        contentValues.put("AUFNR", mResultModel.getmCarInfoModel().getAUFNR());

        String[] keys = new String[2];
        keys[0] = "GSTRS";
        keys[1] = "AUFNR";

        DbAsyncTask dbAsyncTask = new DbAsyncTask("updateComplete", DEFINE.REPAIR_TABLE_NAME, mContext, this,
                contentValues, keys);

        dbAsyncTask.execute(DbAsyncTask.DB_UPDATE);
    }

    private void updateStockComplete() {

        for (MaintenanceItemModel model : mResultModel.getmLastItemModels()) {
            ContentValues contentValues = new ContentValues();
            contentValues.put("MATKL", model.getMaintenanceGroupModel().getName_key());
            contentValues.put("MATNR", model.getMATNR());

            int val = model.getStock() - model.getConsumption();

            contentValues.put("LABST", "" + val);

            String[] keys = new String[2];
            keys[0] = "MATKL";
            keys[1] = "MATNR";

            DbAsyncTask dbAsyncTask = new DbAsyncTask("updateStockComplete", DEFINE.STOCK_TABLE_NAME, mContext, this,
                    contentValues, keys);

            dbAsyncTask.execute(DbAsyncTask.DB_UPDATE);
        }

    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        // TODO Auto-generated method stub
        if (!hidden) {
            // Log.e("고객확인 오더번호", mResultModel.getmCarInfoModel().getAUFNR());
        }
        super.onHiddenChanged(hidden);
    }

    /**
     * 등록결과 성공했을때 DB에 저장된 데이터를 지워준다.
     */
    private void deleteResultDataBase(String TableName) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("AUFNR", mResultModel.getmCarInfoModel().getAUFNR());

        String[] keys = new String[1];
        keys[0] = "AUFNR";

        DbAsyncTask dbAsyncTask = new DbAsyncTask(TableName, TableName, mContext, this, contentValues, keys);

        dbAsyncTask.execute(DbAsyncTask.DB_DELETE);
    }

    private void saveResultDataBase() {
        // 결과등록 실패

        String[] reTableNames = new String[3];
        HashMap<String, ArrayList<HashMap<String, String>>> tableArrayMap = new HashMap<String, ArrayList<HashMap<String, String>>>();
        ArrayList<HashMap<String, String>> resTableArr = null;

        HashMap<String, HashMap<String, String>> resStructMap = new HashMap<String, HashMap<String, String>>();

        ArrayList<HashMap<String, String>> baseArr = new ArrayList<HashMap<String, String>>();
        baseArr.add(mSendModel.getMaintenanceSendBaseModel().getmHashMap());

        reTableNames[0] = "I_ITAB1";
        resTableArr = baseArr;
        tableArrayMap.put(reTableNames[0], resTableArr);
        reTableNames[1] = "I_ITAB2";
        tableArrayMap.put(reTableNames[1], mSendModel.getStockSendArray());
        reTableNames[2] = "I_ITAB3";

        for (HashMap<String, String> hashMap : mSendModel.getSignSendArray()) {
            hashMap.put("AUFNR", mResultModel.getmCarInfoModel().getAUFNR());
        }

        tableArrayMap.put(reTableNames[2], mSendModel.getSignSendArray());

        String[] tableNames = new String[3];

        TableModel tableModel = new TableModel(tableNames, tableArrayMap, resStructMap, null, "");

        HashMap<String, String> loginTableNameMap = new HashMap<String, String>();
        loginTableNameMap.put("I_ITAB1", DEFINE.REPAIR_RESULT_BASE_TABLE_NAME);
        loginTableNameMap.put("I_ITAB2", DEFINE.REPAIR_RESULT_STOCK_TABLE_NAME);

        tableModel.setTableName(DEFINE.REPAIR_RESULT_BASE_TABLE_NAME);

        loginTableNameMap.put("I_ITAB3", DEFINE.REPAIR_RESULT_IMAGE_TABLE_NAME);

        DbAsyncTask asyncTask = new DbAsyncTask(ConnectController.REPAIR_FUNTION_NAME, O_ITAB1.TABLENAME, mContext,
                this, // DbAsyncResListener
                tableModel, loginTableNameMap);

        asyncTask.execute(DbAsyncTask.DB_ARRAY_INSERT);

    }

    private DbAsyncTask saveResultDataBase(MaintenanceSendModel sendModel, DbAsyncResLintener dbAsyncResLintener) {
        // 결과등록 실패

        String[] reTableNames = new String[3];
        HashMap<String, ArrayList<HashMap<String, String>>> tableArrayMap = new HashMap<String, ArrayList<HashMap<String, String>>>();
        ArrayList<HashMap<String, String>> resTableArr = null;

        HashMap<String, HashMap<String, String>> resStructMap = new HashMap<String, HashMap<String, String>>();

        ArrayList<HashMap<String, String>> baseArr = new ArrayList<HashMap<String, String>>();
        baseArr.add(sendModel.getMaintenanceSendBaseModel().getmHashMap());

        reTableNames[0] = "I_ITAB1";
        resTableArr = baseArr;
        tableArrayMap.put(reTableNames[0], resTableArr);

        // myung 20131212 ADD 긴급정비 등록 시에 I_ITAB2의 AUFNR를 " " 로 세팅
        if (sendModel.getMaintenanceSendBaseModel().getGUBUN().equals("E")) {
            for (HashMap<String, String> hashMap : sendModel.getStockSendArray()) {
                // myung 20131223 UPDATE 긴급으로 정비등록시 부품이 서버로 전송되지 않는 문제
                hashMap.put("AUFNR", " ");
                kog.e("Jonathan", "checking Complete for 4");
            }
        }

        reTableNames[1] = "I_ITAB2";
        tableArrayMap.put(reTableNames[1], sendModel.getStockSendArray());
        reTableNames[2] = "I_ITAB3";

        // for (HashMap<String, String> hashMap : sendModel.getSignSendArray())
        // {
        // hashMap.put("AUFNR", sendModel.getMaintenanceSendBaseModel()
        // .getAUFNR());
        // }

        tableArrayMap.put(reTableNames[2], sendModel.getSignSendArray());

        String[] tableNames = new String[3];

        TableModel tableModel = new TableModel(tableNames, tableArrayMap, resStructMap, null, "");

        HashMap<String, String> loginTableNameMap = new HashMap<String, String>();
        loginTableNameMap.put("I_ITAB1", DEFINE.REPAIR_RESULT_BASE_TABLE_NAME);
        loginTableNameMap.put("I_ITAB2", DEFINE.REPAIR_RESULT_STOCK_TABLE_NAME);

        tableModel.setTableName(DEFINE.REPAIR_RESULT_BASE_TABLE_NAME);

        loginTableNameMap.put("I_ITAB3", DEFINE.REPAIR_RESULT_IMAGE_TABLE_NAME);

        DbAsyncTask asyncTask = new DbAsyncTask("saveResultDataBase", O_ITAB1.TABLENAME, mContext, dbAsyncResLintener, // DbAsyncResListener
                tableModel, loginTableNameMap);
        //
        // asyncTask.execute(DbAsyncTask.DB_ARRAY_INSERT);
        return asyncTask;
    }

    public DbAsyncTask sendResult(MaintenanceResultModel model, DbAsyncResLintener dbAsyncResLintener) {

        DbAsyncTask reAsyncTask;

        MaintenanceSendBaseModel sendBaseModel = getSendBaseModel(model);
        ArrayList<MaintenanceSendStockModel> sendStockModels = getSendStockModel(model);

        sendBaseModel.setSIGN_T(mMaintenanceSendSignModels.get(0).getSIGN_T());

        MaintenanceSendModel sendModel = new MaintenanceSendModel(sendBaseModel, sendStockModels,
                mMaintenanceSendSignModels);

        kog.e("Jonathan", "checking Complete 3");

        reAsyncTask = saveResultDataBase(sendModel, dbAsyncResLintener);

        return reAsyncTask;

    }

    private String currencyFormat(int inputMoney) {
        DecimalFormat decimalFormat = new DecimalFormat("#,##0");

        return decimalFormat.format(inputMoney);
    }

}
