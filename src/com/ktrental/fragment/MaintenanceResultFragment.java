package com.ktrental.fragment;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import com.ktrental.R;
import com.ktrental.activity.Main_Activity;
import com.ktrental.adapter.CustomerCarAdapter;
import com.ktrental.cm.connect.ConnectController;
import com.ktrental.cm.connect.Connector.ConnectInterface;
import com.ktrental.cm.db.DbAsyncTask;
import com.ktrental.cm.db.DbAsyncTask.DbAsyncResLintener;
import com.ktrental.common.KtRentalApplication;
import com.ktrental.dialog.Duedate_Dialog;
import com.ktrental.dialog.History_Dialog;
import com.ktrental.fragment.CameraPopupFragment.OnNumberResult;
import com.ktrental.fragment.CheckingCompleteFragment.OnSendResult;
import com.ktrental.fragment.MaintenanceResultInfoFragment.OnAddressChange;
import com.ktrental.fragment.MaintenanceResultInfoFragment.OnEmergencyListener;
import com.ktrental.fragment.MaintenanceResultResistFragment.OnCheckingResult;
import com.ktrental.fragment.MaintenanceResultResistFragment.OnModify;
import com.ktrental.fragment.MaintenanceResultResistFragment.OnNumberResultCancel;
import com.ktrental.model.BaseMaintenanceModel;
import com.ktrental.model.CarInfoModel;
import com.ktrental.model.MaintenanceItemModel;
import com.ktrental.model.MaintenanceResultModel;
import com.ktrental.model.TableModel;
import com.ktrental.util.OnChangeFragmentListener;
import com.ktrental.util.OnEventCancelListener;
import com.ktrental.util.OnEventOkListener;
import com.ktrental.util.OnSelectedItem;
import com.ktrental.util.ResultController;
import com.ktrental.util.ResultController.OnResultCompleate;
import com.ktrental.util.kog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class MaintenanceResultFragment extends BaseFragment implements OnCheckedChangeListener, OnSelectedItem, OnNumberResult, OnCheckingResult,
        OnClickListener, OnAddressChange, OnSendResult, DbAsyncResLintener, OnModify, OnEmergencyListener, OnNumberResultCancel
{

    private CustomerCarAdapter                                             mCustomerCarAdapter;

    private ListView                                                       mLvCustomerCar;

    private ArrayList<BaseMaintenanceModel>                                mCarInfoModelArray  = new ArrayList<BaseMaintenanceModel>();

    private RadioGroup                                                     mRdTab;

    private MaintenanceResultInfoFragment                                  maintenanceResultInfoFragment;
    private MaintenanceResultResistFragment                                maintenanceResultResistFragment;
    private CheckingCompleteFragment                                       mCheckingComplateFragment;

    private LinearLayout                                                   mTabContent;

    private BaseFragment                                                   mCurrentBaseFragment;

    private TextView                                                       mTvTitle;

    private final static String                                            TITLE               = "순회정비결과등록";

    private RadioButton                                                    mRbResist;
    private RadioButton                                                    mRbInfo;
    private RadioButton                                                    mRbConfirm;

    private LinearLayout                                                   mLlResultTitle;

    private LinearLayout                                                   mLlEmergency;
    private CheckBox                                                       mChEmergency;
    private BaseMaintenanceModel                                           mCurrentModel;

    private Button                                                         mBtnTire;
    private Button                                                         mBtnDate;
    private Button                                                         mBtnTransfer;

    private LinearLayout                                                   mRbResistFake;
    private LinearLayout                                                   mRbConfirmFake;

    private int                                                            mInsertCount        = 0;

    public static HashMap<String, HashMap<String, MaintenanceResultModel>> mResultModelMap     = new HashMap<String, HashMap<String, MaintenanceResultModel>>();
    public static HashMap<String, HashMap<String, MaintenanceResultModel>> mTempResultModelMap = new HashMap<String, HashMap<String, MaintenanceResultModel>>();
    private HashMap<String, Integer>                                       mInsertCountMap     = new HashMap<String, Integer>();

    private int                                                            mType               = 0;

    private String                                                         mAddress            = "";

    private String                                                         mProgressStatus     = " ";

    private MaintenanceResultModel                                         mCurrentResultModel;

    private CarInfoModel                                                   mCarInfoModel;

    private boolean                                                        mEmergencyMode      = false;
    public static boolean                                                  mCallFlag           = false;

    // OnNumberResultCancel mOnNumberResultCancel;

    // public static int mPosition=0;

    public interface OnMaintenanceStart
    {
        void onMaintenanceStart();
    }

    public MaintenanceResultFragment()
    {
        // TODO Auto-generated constructor stub
    }

    public MaintenanceResultFragment(String className, OnChangeFragmentListener changeFragmentListener, ArrayList<BaseMaintenanceModel> arrayList)
    {
        super(className, changeFragmentListener);

        maintenanceResultInfoFragment = new MaintenanceResultInfoFragment(MaintenanceResultInfoFragment.class.getName(), null, this, this, this);
        maintenanceResultResistFragment = new MaintenanceResultResistFragment(MaintenanceResultResistFragment.class.getName(), null, this, this, this);

        mCheckingComplateFragment = new CheckingCompleteFragment(this, this);

        mCarInfoModelArray = arrayList;

    }

    public MaintenanceResultFragment(String className, OnChangeFragmentListener changeFragmentListener, ArrayList<BaseMaintenanceModel> arrayList,
            CarInfoModel carInfoModel)
    {
        super(className, changeFragmentListener);

        maintenanceResultInfoFragment = new MaintenanceResultInfoFragment(MaintenanceResultInfoFragment.class.getName(), null, this, this, this);
        maintenanceResultResistFragment = new MaintenanceResultResistFragment(MaintenanceResultResistFragment.class.getName(), null, this, this, this);
        mCheckingComplateFragment = new CheckingCompleteFragment(this, this);

        mCarInfoModelArray = arrayList;

        mCarInfoModel = carInfoModel;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        // TODO Auto-generated method stub
        // 2014-04-14 KDH 아놔-_-이렇게해야지 기본도없네
        mResultModelMap.clear();
        mTempResultModelMap.clear();
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {

        mRootView = inflater.inflate(R.layout.maintenance_result_layout, null);

        mTvTitle = (TextView) mRootView.findViewById(R.id.tv_common_title);

        mTvTitle.setText(TITLE);

        mLvCustomerCar = (ListView) mRootView.findViewById(R.id.lv_customer_car);
        mCustomerCarAdapter = new CustomerCarAdapter(mContext, mLvCustomerCar, this);
        mCustomerCarAdapter.setCustomerArray(mCarInfoModelArray);

        mCurrentModel = mCarInfoModelArray.get(0);
        // 초기화된 긴급정비 체크를 해주어야한다.
        KtRentalApplication.setEmergency(mCurrentModel.getCarNum(), false);

        mLvCustomerCar.setAdapter(mCustomerCarAdapter);
        mCustomerCarAdapter.setOnSeletedItem(this);

        mRdTab = (RadioGroup) mRootView.findViewById(R.id.rg_tab);
        mRdTab.setOnCheckedChangeListener(this);

        mTabContent = (LinearLayout) mRootView.findViewById(R.id.ll_tab);

        addFragment(mCheckingComplateFragment, R.id.ll_tab);
        addFragment(maintenanceResultResistFragment, R.id.ll_tab);
        addFragment(maintenanceResultInfoFragment, R.id.ll_tab);

        // maintenanceResultResistFragment.setOnNumberResultCancel(mOnNumberResultCancel);

        mRbResist = (RadioButton) mRootView.findViewById(R.id.rd_resist);
        mRbResist.setEnabled(true);

        mRbInfo = (RadioButton) mRootView.findViewById(R.id.rd_carinfo);

        mRbConfirm = (RadioButton) mRootView.findViewById(R.id.rd_confirm);

        mLlResultTitle = (LinearLayout) mRootView.findViewById(R.id.ll_result_title);

        mLlEmergency = (LinearLayout) mRootView.findViewById(R.id.ll_emergency);
        mLlEmergency.setOnClickListener(this);
        mChEmergency = (CheckBox) mRootView.findViewById(R.id.ch_emergency);

        mLlEmergency.setOnTouchListener(new OnTouchListener()
        {

            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                if (mEmergencyMode)
                    return true;

                return false;
            }
        });
        mChEmergency.setOnTouchListener(new OnTouchListener()
        {

            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                // TODO Auto-generated method stub
                if (mEmergencyMode)
                    return true;

                return false;
            }
        });
        mChEmergency.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                // TODO Auto-generated method stub
                if (isChecked)
                {
                    // 초기화된 긴급정비 체크를 해주어야한다.
                    KtRentalApplication.setEmergency(mCurrentModel.getCarNum(), true);
                    maintenanceResultInfoFragment.setGUBUN("E");
                    maintenanceResultResistFragment.setGUBUN("E");
                }
                else
                {
                    // 초기화된 긴급정비 체크를 해주어야한다.
                    KtRentalApplication.setEmergency(mCurrentModel.getCarNum(), false);
                    String gubun = " ";
                    if (mProgressStatus.equals("E0004"))
                    {
                        // myung 20131316 DELETE GUBUN 공백으로 보내야 함.
                        // gubun = "U";
                    }
                    maintenanceResultInfoFragment.setGUBUN(gubun);
                    maintenanceResultResistFragment.setGUBUN(gubun);
                }
            }
        });
        mRbResist.setEnabled(false);
        mRbConfirm.setEnabled(false);
        // 테스트
        // mRbResist.setEnabled(true);
        // mRbConfirm.setEnabled(true);

        if (mProgressStatus.equals("E0004"))
        {
            if (mCurrentResultModel == null)
            {
                mRbConfirm.setVisibility(View.INVISIBLE);
            }
            else
            {
                CarInfoModel carInfoModel = mCurrentResultModel.getmCarInfoModel();
                if (carInfoModel == null || mCurrentResultModel.getmCarInfoModel().getGUBUN().equals(""))
                {
                    mRbConfirm.setVisibility(View.INVISIBLE);
                }
            }
        }

        mRootView.findViewById(R.id.btn_tire_offer).setOnClickListener(this);
        mRootView.findViewById(R.id.btn_change_date).setOnClickListener(this);
        mRootView.findViewById(R.id.btn_transfer).setOnClickListener(this);

        mBtnTire = (Button) mRootView.findViewById(R.id.btn_tire_offer);
        mBtnTire.setOnClickListener(this);

        mBtnDate = (Button) mRootView.findViewById(R.id.btn_change_date);
        mBtnDate.setOnClickListener(this);

        mBtnTransfer = (Button) mRootView.findViewById(R.id.btn_transfer);
        mBtnTransfer.setOnClickListener(this);

        mLlEmergency.setVisibility(View.INVISIBLE);

        if (mCarInfoModelArray != null)
        {
            if (mCarInfoModelArray.size() > 0)
            {

                BaseMaintenanceModel model = mCarInfoModelArray.get(0);

                kog.e("Jonathan", "showCarinfo() model.... ::" + mCarInfoModelArray.get(0)); // Jonathan 14.06.19

                mProgressStatus = model.getProgress_status();

                if (mCarInfoModel == null)
                {
                    // 여기는 로컬에 데이터가 있는 경우 Jonathan 14.06.19
                    kog.e("Jonathan", "1model.getNAME1() :: " + model.getCUSTOMER_NAME() + "model.getname() :: " + model.getDRIVER_NAME()); // Jonathan
                                                                                                                                            // 14.06.18

                    maintenanceResultInfoFragment.showCarInfo(model.getCUSTOMER_NAME(), model.getCarNum(), model.getCarname(), mProgressStatus,
                            model.getImageName(), model.getAUFNR(), model.getGUBUN());

                }
                else
                {
                    // 여기가 서버에서 받는 곳 Jonathan 14.06.19
                    kog.e("Jonathan", "2model.getNAME1() :: " + model.getCUSTOMER_NAME() + "model.getname() :: " + model.getDRIVER_NAME()); // Jonathan
                                                                                                                                            // 14.06.18\

                    maintenanceResultInfoFragment.showCarInfo(model.getCUSTOMER_NAME(), model.getCarNum(), model.getCarname(), mProgressStatus,
                            model.getImageName(), model.getAUFNR(), model.getGUBUN(), mCarInfoModel);
                    mEmergencyMode = true;
                    mChEmergency.setChecked(true);
                }
            }
        }

        mRbResistFake = (LinearLayout) mRootView.findViewById(R.id.rd_resist_fake);
        mRbResistFake.setOnClickListener(this);
        mRbConfirmFake = (LinearLayout) mRootView.findViewById(R.id.rd_confirm_fake);
        mRbConfirmFake.setOnClickListener(this);

        return mRootView;
    }

    @Override
    public void onStart()
    {
        super.onStart();
        // Log.e("onStart", "mResultModelMap.clear()");
        // 2014-04-14 KDH 이게뭐지?왜 클리어하나?
        // if(mCallFlag){
        // mCallFlag = false;
        // }else{
        // mResultModelMap.clear();
        // mTempResultModelMap.clear();
        // }

        // mTotalConsumptionItemModels.clear();
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId)
    {
        // TODO Auto-generated method stub
        switch (checkedId)
        {
            case R.id.rd_carinfo:

                moveInfo();

                break;
            case R.id.rd_resist:
                moveResist();
                break;
            case R.id.rd_confirm:
                moveConfirm();
                break;
            default:
                break;
        }
    }

    private void moveInfo()
    {
        mRbResist.setEnabled(true);
        mRbResistFake.setVisibility(View.GONE);
        mRbConfirmFake.setVisibility(View.GONE);
        mLlResultTitle.setVisibility(View.VISIBLE);
        mTabContent.setVisibility(View.VISIBLE);
        mLlEmergency.setVisibility(View.INVISIBLE);
        changeFragment(maintenanceResultInfoFragment);
        mType = 0;
    }

    private void moveResist()
    {
        if (mProgressStatus.equals("E0002"))
        {
            mLlResultTitle.setVisibility(View.GONE);
        }
        else
        {
            if (mCurrentResultModel == null)
            {
                if (!mEmergencyMode)
                    mRbConfirm.setVisibility(View.INVISIBLE);
                mBtnTire.setVisibility(View.INVISIBLE);
                mBtnDate.setVisibility(View.INVISIBLE);
                mBtnTransfer.setVisibility(View.GONE);
                // mLlEmergency.setVisibility(View.INVISIBLE);

            }
            else
            {
                CarInfoModel carInfoModel = mCurrentResultModel.getmCarInfoModel();
                if (carInfoModel == null || carInfoModel.getGUBUN().equals(" "))
                {
                    mRbConfirm.setVisibility(View.INVISIBLE);
                    mBtnTire.setVisibility(View.INVISIBLE);
                    mBtnDate.setVisibility(View.INVISIBLE);
                    mBtnTransfer.setVisibility(View.GONE);
                    // mLlEmergency.setVisibility(View.INVISIBLE);
                }
                else
                {
                    mLlResultTitle.setVisibility(View.GONE);
                }
            }

        }
        mLlEmergency.setVisibility(View.VISIBLE);
        mTabContent.setVisibility(View.VISIBLE);
        mLlResultTitle.setVisibility(View.GONE);

        CarInfoModel carInfoModel = maintenanceResultInfoFragment.getCarInfoModel();
        if (carInfoModel != null)
        {
            HashMap<String, MaintenanceResultModel> hashMap = mResultModelMap.get(carInfoModel.getAddress());
            if (hashMap != null)
            {
                if (hashMap.containsKey(maintenanceResultInfoFragment.getCarInfoModel().getCarNum()))
                {
                    MaintenanceResultModel resultModel = hashMap.get(maintenanceResultInfoFragment.getCarInfoModel().getCarNum());
                    maintenanceResultResistFragment.setResultModel(resultModel);
                    maintenanceResultResistFragment.setTotalStockSelectArray(getTotalSelectedStock(hashMap));
                }
            }

            maintenanceResultResistFragment.setCarInfoModel(maintenanceResultInfoFragment.getCarInfoModel());
            changeFragment(maintenanceResultResistFragment);
            mType = 1;
        }
    }

    private void moveConfirm()
    {
        mLlResultTitle.setVisibility(View.GONE);
        mLlEmergency.setVisibility(View.INVISIBLE);

        changeFragment(mCheckingComplateFragment);
        mType = 2;
    }

    public boolean isResult()
    {

        boolean isResult = true;

        if (mType == 1)
        {

            HashMap<String, MaintenanceResultModel> hashMap = mResultModelMap.get(maintenanceResultInfoFragment.getCarInfoModel().getAddress());
            if (hashMap != null)
            {
                if (!hashMap.containsKey(maintenanceResultInfoFragment.getCarInfoModel().getCarNum()))
                {
                    isResult = false;
                }
            }
            else
            {
                isResult = false;
            }

        }

        return isResult;

    }

    // myung 20131224 ADD
    ArrayList<MaintenanceItemModel> mTotalConsumptionItemModels = new ArrayList<MaintenanceItemModel>();

    public void setToalConsumption()
    {

        Iterator<String> itMap = mResultModelMap.keySet().iterator();

        boolean sonjaiFlag = false;
        while (itMap.hasNext())
        {
            String strKey = itMap.next();

            HashMap<String, MaintenanceResultModel> tmpResultModel = mResultModelMap.get(strKey);

            Iterator<String> itMap1 = tmpResultModel.keySet().iterator();
            while (itMap1.hasNext())
            {
                String strKey1 = itMap1.next();
                for (int i = 0; i < tmpResultModel.size(); i++)
                {
                    sonjaiFlag = false;
                    for (int j = 0; j < mTotalConsumptionItemModels.size(); j++)
                    {
                        if (tmpResultModel.get(strKey1).getmLastItemModels().get(i).getMATNR().equals(mTotalConsumptionItemModels.get(j).getMATNR()))
                        {

                            mTotalConsumptionItemModels.get(j).setTotalConsumption(
                                    mTotalConsumptionItemModels.get(j).getTotalConsumption()
                                            + tmpResultModel.get(strKey1).getmLastItemModels().get(i).getConsumption());
                            sonjaiFlag = true;
                        }
                    }

                    if (!sonjaiFlag)
                    {
                        mTotalConsumptionItemModels.add(tmpResultModel.get(strKey1).getmLastItemModels().get(i));
                    }

                }

            }

        }

    }

    // myung 20131224 ADD 해당 아이템의 선택된 갯수를 구하는 함수
    public static int getToalConsumption(String strMATNR)
    {

        Iterator<String> itMap = mResultModelMap.keySet().iterator();
        int retTotalConsumption = 0;

        while (itMap.hasNext())
        {
            String strKey = itMap.next();
            HashMap<String, MaintenanceResultModel> tmpResultModel = mResultModelMap.get(strKey);

            Iterator<String> itMap1 = tmpResultModel.keySet().iterator();

            while (itMap1.hasNext())
            {
                String strKey1 = itMap1.next();
                for (int i = 0; i < tmpResultModel.get(strKey1).getmLastItemModels().size(); i++)
                {
                    if (tmpResultModel.get(strKey1).getmLastItemModels().get(i).getMATNR().equals(strMATNR))
                    {

                        retTotalConsumption += tmpResultModel.get(strKey1).getmLastItemModels().get(i).getConsumption();
                    }
                }

            }

        }
        return retTotalConsumption;

    }

    // myung 20131224 ADD
    public static int getToalConsumptionTemp(String strMATNR)
    {
        // Log.e("#", "");
        Iterator<String> itMap = mTempResultModelMap.keySet().iterator();
        int retTotalConsumption = 0;

        while (itMap.hasNext())
        {
            // Log.e("##", "");
            String strKey = itMap.next();
            HashMap<String, MaintenanceResultModel> tmpResultModel = mTempResultModelMap.get(strKey);
            Iterator<String> itMap1 = tmpResultModel.keySet().iterator();

            while (itMap1.hasNext())
            {
                // Log.e("###", "");
                String strKey1 = itMap1.next();
                for (int i = 0; i < tmpResultModel.get(strKey1).getmLastItemModels().size(); i++)
                {
                    // Log.e("####", "");
                    if (tmpResultModel.get(strKey1).getmLastItemModels().get(i).getMATNR().equals(strMATNR))
                    {
                        // Log.e("#####", "");
                        retTotalConsumption += tmpResultModel.get(strKey1).getmLastItemModels().get(i).getConsumption();
                    }
                }

            }

        }
        return retTotalConsumption;

    }

    public void userCheckResult(String carnum)
    {

        mRbInfo.setChecked(true);
        mRbResist.setChecked(false);
        mRbResistFake.setVisibility(View.GONE);
        mRbInfo.setEnabled(false);
        mRbResist.setEnabled(false);
        mRbConfirm.setChecked(false);
        mRbConfirm.setEnabled(false);
        mRbConfirmFake.setVisibility(View.GONE);

        maintenanceResultResistFragment.initText();
        maintenanceResultResistFragment.removeFirstView(carnum);
    }

    private void addFragment(BaseFragment fragment, int id)
    {

        FragmentTransaction ft = getChildFragmentManager().beginTransaction();
        ft.add(id, fragment, fragment.mClassName);

        if (mCurrentBaseFragment != null)
            ft.hide(mCurrentBaseFragment);

        ft.commit();

        mCurrentBaseFragment = fragment;
    }

    private void changeFragment(BaseFragment fragment)
    {
        FragmentTransaction ft = getChildFragmentManager().beginTransaction();
        ft.show(fragment);

        if (mCurrentBaseFragment != null)
            ft.hide(mCurrentBaseFragment);

        ft.commit();

        mCurrentBaseFragment = fragment;
    }

    // 대상차량선택x 콜백
    @Override
    public void OnSeletedItem(Object item)
    {

        setSelectedItem(item);

    }

    private void setSelectedItem(Object item)
    {
        // TODO Auto-generated method stub
        if (item instanceof BaseMaintenanceModel)
        {
            BaseMaintenanceModel model = (BaseMaintenanceModel) item;
            // 초기화된 긴급정비 체크를 해주어야한다.
            // KtRentalApplication.setEmergency(model.getCarNum(), false);
            mCurrentModel = model;

            kog.e("Jonathan", "3model.getNAME1() :: " + model.getCUSTOMER_NAME() + "model.getname() :: " + model.getDRIVER_NAME()); // Jonathan
                                                                                                                                    // 14.06.18

            maintenanceResultInfoFragment.showCarInfo(model.getCUSTOMER_NAME(), model.getCarNum(), model.getCarname(), model.getProgress_status(),
                    model.getImageName(), model.getGUBUN());

            String carnum = mCurrentModel.getCarNum();

            initEmergency(carnum, KtRentalApplication.getEmergency(carnum));

            HashMap<String, MaintenanceResultModel> hashMap = mResultModelMap.get(model.getAddress());
            if (hashMap != null)
            {
                if (hashMap.containsKey(model.getCarNum()))
                {

                    MaintenanceResultModel resultModel = hashMap.get(model.getCarNum());
                    mCheckingComplateFragment.setResultModel(resultModel);
                    maintenanceResultResistFragment.setResultModel(resultModel);
                    maintenanceResultResistFragment.setCarInfoModel(resultModel.getmCarInfoModel());

                }
                else
                {
                    maintenanceResultResistFragment.setTotalStockSelectArray(getTotalSelectedStock(hashMap));
                }

            }
        }
    }

    private ArrayList<MaintenanceItemModel> getTotalSelectedStock(HashMap<String, MaintenanceResultModel> hashMap)
    {
        ArrayList<MaintenanceItemModel> reArray = new ArrayList<MaintenanceItemModel>();

        Iterator<String> itMap = hashMap.keySet().iterator();
        while (itMap.hasNext())
        {
            String strKey = itMap.next();

            MaintenanceResultModel resultModel = hashMap.get(strKey);

            for (MaintenanceItemModel maintenanceItemModel : resultModel.getmLastItemModels())
            {
                int consumption = 0;
                for (MaintenanceItemModel itemModel : reArray)
                {
                    if (maintenanceItemModel.getName().equals(itemModel.getName()))
                    {

                        // consumption = itemModel.getConsumption();
                        consumption = maintenanceItemModel.getConsumption();
                        maintenanceItemModel.setConsumption(consumption);
                        // Log.d("HONG", "resultModel "
                        // + resultModel.getmCarInfoModel().getCarNum()
                        // + " " + itemModel.getName() + " " + consumption);
                        reArray.remove(itemModel);
                        break;
                    }
                }

                reArray.add(maintenanceItemModel.clone());

            }

            // reArray.addAll(resultModel.getmLastItemModels());
        }
        return reArray;
    }

    // 등록결과등록 콜백
    @Override
    public void onResult(boolean success, MaintenanceResultModel model)
    {

        // Log.e("onResult", "onResult");
        if (model != null)
        {
            mCurrentResultModel = model;
            maintenanceResultResistFragment.setResultModel(model);
        }
        mRbResist.setEnabled(success);

        mRbInfo.setChecked(false);
        mRbResist.setChecked(true);
        // mRbInfo.setEnabled(true);
        mRbResist.setEnabled(false);
        mRbResistFake.setVisibility(View.GONE);
        mRbConfirmFake.setVisibility(View.GONE);

        // mRbInfo.setEnabled(false);

        // if (success)
        // changeFragment(maintenanceResultResistFragment);
    }

    @Override
    public void onResultCancel(boolean success, MaintenanceResultModel model)
    {
        // TODO Auto-generated method stub
        // Log.e("onResultCancel", "onResultCancel");
        if (model != null)
        {
            mCurrentResultModel = model;
            maintenanceResultResistFragment.setResultModel(model);
        }
        mRbResist.setEnabled(success);

        mRbInfo.setChecked(false);
        mRbInfo.setEnabled(true);

        mRbResist.setChecked(true);
        mRbResist.setEnabled(true);

        mRbConfirm.setChecked(false);
        mRbConfirm.setEnabled(true);
        mRbResistFake.setVisibility(View.GONE);
        mRbConfirmFake.setVisibility(View.GONE);

        mProgressStatus = "E0002";
    }

    private void changeChecking(boolean show, MaintenanceResultModel model)
    {
        if (show)
        {
            // mLlResultTitle.setVisibility(View.GONE);

            // mTabContent.setVisibility(View.GONE);
            // mTabContentCheck.setVisibility(View.VISIBLE);

            maintenanceResultResistFragment.setCarInfoModel(maintenanceResultInfoFragment.getCarInfoModel());
            Log.e("setResultModel : 오더번호", "" + model.getmCarInfoModel().getAUFNR());
            mCheckingComplateFragment.setResultModel(model);

            mRbInfo.setChecked(false);
            // mRbResist.setChecked(false);
            mRbInfo.setEnabled(true);
            mRbResist.setEnabled(true);
            mRbResistFake.setVisibility(View.GONE);
            mRbConfirm.setChecked(true);
            mRbConfirm.setEnabled(true);
            mRbConfirmFake.setVisibility(View.GONE);

        }
        else
        {
            mRbInfo.setChecked(true);
            mRbResist.setChecked(false);
            mRbInfo.setEnabled(false);
            mRbResist.setEnabled(false);
            mRbResistFake.setVisibility(View.GONE);
            mRbConfirm.setChecked(false);
            mRbConfirm.setEnabled(false);
            mRbConfirmFake.setVisibility(View.GONE);

            // changeFragment(maintenanceResultInfoFragment);
        }
    }

    // 고객확인 완료 콜백
    @Override
    public void onCheckingResult(boolean show, MaintenanceResultModel model)
    {
        // Log.d("", "onCheckingResult");
        checkUser(model);
        // changeChecking(show, model);
    }

    @Override
    public void onClick(View v)
    {
        // TODO Auto-generated method stub
        switch (v.getId())
        {
            case R.id.ll_emergency: // 긴급정비

                clickEmergency();
                break;

            case R.id.btn_tire_offer: // 타이어신청
                if (isNetwork())
                    clickTireOffer();
                break;
            case R.id.btn_change_date: // 예정일변경
                if (mProgressStatus.equals("E0002"))
                    if (isNetwork())
                        clickChangeDate();
                break;
            case R.id.btn_transfer: // 이관이력조회
                if (isNetwork())
                    clickChangeTransfer();
                break;

            case R.id.rd_resist_fake:
                showEventPopup2(null, "정비시작버튼을 클릭하여 \n정비시작 후 확인 할 수 있습니다.");
                break;

            case R.id.rd_confirm_fake:
                showEventPopup2(null, "정비시작버튼을 클릭하여 \n정비시작 후 확인 할 수 있습니다.");
                break;
            // case R.id.btn_cancel:
            // clickCancel();
            // break;
            default:
                break;
        }
    }

    private void clickCancel()
    {
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

                hideProgress();
                if (MTYPE.equals("S"))
                {
                    showEventPopup2(null, "취소되었습니다.");
                }
                else
                {
                    showEventPopup2(null, "" + resultText);
                }

            }
        }, mContext);
        if (mCurrentModel != null)
        {
            showProgress("결과등록을 취소중입니다.");
            connectController.cancelResult(mCurrentModel.getAUFNR(), mCurrentResultModel.getmCarInfoModel().getCEMER(), mCurrentResultModel
                    .getmCarInfoModel().getTourCarNum());
        }
    }

    private void clickTireOffer()
    {

        TireFragment fragment = new TireFragment(TireFragment.class.getName(), mChangeFragmentListener, mCurrentModel.getCarNum(),
                mCurrentModel.getEQUNR());

        changfragment(fragment);
    }

    private void clickChangeDate()
    {
        ArrayList<BaseMaintenanceModel> arr = new ArrayList<BaseMaintenanceModel>();
        arr.add(mCurrentModel);
        if (arr.size() <= 0)
            return;
        Duedate_Dialog dd = new Duedate_Dialog(mContext, arr, null, null);
        dd.show();
    }

    private void clickChangeTransfer()
    {
        ArrayList<BaseMaintenanceModel> arr = new ArrayList<BaseMaintenanceModel>();
        arr.add(mCurrentModel);
        if (arr.size() <= 0)
            return;
        HashMap<String, String> o_struct1 = new HashMap<String, String>();
        o_struct1.put("INVNR", mCurrentModel.getCarNum());
        o_struct1.put("MAKTX", mCurrentModel.getCarname());
        o_struct1.put("CONTNM", mCurrentModel.getCUSTOMER_NAME());
        o_struct1.put("CUSJUSO", mCurrentModel.getAddress());
        o_struct1.put("DLST1", mCurrentModel.getTel());
        o_struct1.put("EQUNR", mCurrentModel.getEQUNR());

        History_Dialog hd = new History_Dialog(mContext, o_struct1, 2);
        hd.show();

    }

    private void clickEmergency()
    {
        if (mChEmergency.isChecked())
        {
            mChEmergency.setChecked(false);
        }
        else
        {
            mChEmergency.setChecked(true);
        }
    }

    // 소재지 변경 콜백
    @Override
    public void onAddresssChange(String carNum)
    {
        KtRentalApplication.changeRepair();
        // TODO Auto-generated method stub
        int size = mCustomerCarAdapter.getCount();
        if (size == 1)
        {
            Main_Activity main_Activity = (Main_Activity) getActivity();
            main_Activity.onBackPressed();
        }
        else
        {
            for (BaseMaintenanceModel model : mCarInfoModelArray)
            {
                if (model.getCarNum().equals(carNum))
                {
                    mCarInfoModelArray.remove(model);
                    break;
                }
            }

            mCustomerCarAdapter.setData(mCarInfoModelArray);
            mCustomerCarAdapter.notifyDataSetChanged();

            BaseMaintenanceModel model = mCarInfoModelArray.get(0);

            kog.e("Jonathan", "4model.getNAME1() :: " + model.getCUSTOMER_NAME() + "model.getname() :: " + model.getDRIVER_NAME()); // Jonathan
                                                                                                                                    // 14.06.18

            maintenanceResultInfoFragment.showCarInfo(model.getCUSTOMER_NAME(), model.getCarNum(), model.getCarname(), model.getProgress_status(),
                    model.getImageName(), model.getAUFNR(), model.getGUBUN());

        }
    }

    private void addResult(MaintenanceResultModel model, String kufnr)
    {
        if (mResultModelMap.containsKey(kufnr))
        {
            HashMap<String, MaintenanceResultModel> hashMap = mResultModelMap.get(kufnr);
            if (!hashMap.containsKey(model.getmCarInfoModel().getCarNum()))
            {
                hashMap.put(model.getmCarInfoModel().getCarNum(), model);
            }
            else
            {
                hashMap.remove(model.getmCarInfoModel().getCarNum());
                hashMap.put(model.getmCarInfoModel().getCarNum(), model);
            }
        }
        else
        {
            HashMap<String, MaintenanceResultModel> hashMap = new HashMap<String, MaintenanceResultModel>();
            hashMap.put(model.getmCarInfoModel().getCarNum(), model);
            mResultModelMap.put(kufnr, hashMap);
        }
    }

    private void moveResultIndex(boolean endFlag)
    {
        // myung 20131219 ADD 복수개 아이템 중에 최종 이외의 아이템에서 전송 시 이전화면으로 돌아가지 않는 문제
        if (endFlag)
        {
            Main_Activity activity = (Main_Activity) getActivity();
            if (activity != null)
                activity.onBackPressed();
        }

        int nextIndex = mCustomerCarAdapter.getmSelectedPosition() + 1 + 1;
        // mPosition = nextIndex;
        int max = mCarInfoModelArray.size();
        if (nextIndex > max)
        { // 마지막일때
            // myung 20131219 DEL 복수개 아이템 중에 최종 이외의 아이템에서 전송 시 이전화면으로 돌아가지 않는 문제
            // if (endFlag) {
            // Main_Activity activity = (Main_Activity) getActivity();
            // if (activity != null)
            // activity.onBackPressed();
            // }
        }
        else
        { // 마지막이 아닌경우
            mCustomerCarAdapter.setmSelectedPosition(nextIndex - 1);
        }
    }

    private void checkUser(final MaintenanceResultModel model)
    {

        addResult(model, model.getmCarInfoModel().getAddress());
        // int currentIndex = mCustomerCarAdapter.getmSelectedPosition();
        //
        // int currentMaxCount = mInsertCountMap.get(model.getmCarInfoModel()
        // .getName());
        int currentMaxCount = mCarInfoModelArray.size();

        HashMap<String, MaintenanceResultModel> hashMap = mResultModelMap.get(model.getmCarInfoModel().getAddress());

        int saveCount = hashMap.size();
        if (saveCount != currentMaxCount)
        {
            if (saveCount == 1)
            {

                showEventPopup1(" ", "점검 대상 중 동일한 고객 차량이 존재합니다. 모두 점검 후 고객 확인을 받으시겠습니까?", new OnEventOkListener()
                {

                    @Override
                    public void onOk()
                    {
                        // TODO Auto-generated method stub
                        moveResultIndex(false);

                        changeChecking(false, model);
                    }
                }, new OnEventCancelListener()
                {

                    @Override
                    public void onCancel()
                    {
                        // TODO Auto-generated method stub
                        changeChecking(true, model);
                    }
                });

            }
            else
            {
                moveResultIndex(false);

                changeChecking(false, model);
            }
        }
        else
        {
            changeChecking(true, model);
        }
    }

    public boolean isResult(BaseMaintenanceModel model)
    {
        boolean isResult = false;

        HashMap<String, MaintenanceResultModel> hashMap = mResultModelMap.get(model.getAddress());
        if (hashMap != null)
        {
            if (hashMap.containsKey(model.getCarNum()))
            {
                isResult = true;
            }
        }
        return isResult;
    }

    @Override
    public void onSendResult(String address)
    {
        // TODO Auto-generated method stub
        kog.e("KDH", "address = " + address);
        if (mResultModelMap.containsKey(address))
        {
            mAddress = address;
            showProgress("점검결과를 등록 중입니다.");

            HashMap<String, MaintenanceResultModel> hashMap = mResultModelMap.get(address);

            kog.e("Jonathan", "onSendResult hashmap : " + String.valueOf(hashMap.size()));

            if (hashMap != null)
            {
                Iterator<String> it = hashMap.keySet().iterator();

                while (it.hasNext())
                {
                    String strKey = "";

                    kog.e("KDH", "" + mResultModelMap.size());

                    strKey = it.next();
                    // 2014-01-16 KDH Db를 무조건 거치도록 한건가..데이터 조합이 환상적으로 타고내려가네-_-;;
                    kog.e("Jonathan", "checking Complete 2 while : " + strKey);
                    DbAsyncTask asyncTask = mCheckingComplateFragment.sendResult(hashMap.get(strKey), this);
                    asyncTask.execute(DbAsyncTask.DB_ARRAY_INSERT);
                    // 2014-01-16 KDH 결과등록 데이터 인서트 이후 다시뽑아서 보내버리나-ㅇ-?

                    hashMap.remove(strKey);
                    if (hashMap.size() < 1)
                        mResultModelMap.remove(address);
                    break;
                }
            }
        }
    }

    @Override
    public void onCompleteDB(String funName, int type, Cursor cursor, String tableName)
    {
        // TODO Auto-generated method stub
        if (funName.equals("saveResultDataBase"))
        {
            kog.e("Jonathan", "checking Complete 6 ");
            if (mResultModelMap.size() > 0)
            {
                if (mResultModelMap.containsKey(mAddress))
                {
                    HashMap<String, MaintenanceResultModel> hashMap = mResultModelMap.get(mAddress);
                    if (hashMap != null)
                    {
                        kog.e("Jonathan", "checking Complete 7 ");
                        onSendResult(mAddress);
                    }
                    else
                    {

                        kog.e("Jonathan", "checking Complete 8 ");
                        ResultController resultController = new ResultController(mContext, new OnResultCompleate()
                        {

                            @Override
                            public void onResultComplete(String message)
                            {
                                // TODO Auto-generated method stub
                                kog.e("Jonathan", "checking Complete 9 ");
                                KtRentalApplication.changeRepair();
                            }
                        });
                        kog.e("KDH", "결과등록하고 여기서 뒤지는 것 같다.");
                        // resultController.queryBaseGroup(true);
                        // 2014-01-21 KDH 원래는 위에것이 정상인데 내가 따로만듬-ㅇ-;
                        resultController.queryBaseGroupKDH(true);
                    }
                }
            }
            else
            {
                isNetwork();
                {
                    ResultController resultController = new ResultController(mContext, new OnResultCompleate()
                    {

                        @Override
                        public void onResultComplete(String message)
                        {
                            hideProgress();

                            showEventPopup2(new OnEventOkListener()
                            {

                                @Override
                                public void onOk()
                                {
                                    // TODO Auto-generated method stub

                                    KtRentalApplication.changeRepair();
                                    moveResultIndex(true);
                                }
                            }, "" + message);
                        }
                    });

                    // resultController.queryBaseGroup(true);
                    // 2014-01-21 KDH 원래는 위에것이 정상인데 내가 따로만듬-ㅇ-;
                    resultController.queryBaseGroupKDH(true);
                }
                kog.e("KDH", "설마 너가 미전송 데이터냐?");
            }
        }
    }

    private void initEmergency(String carnum, boolean check)
    {

        mChEmergency.setChecked(check);
        // 초기화된 긴급정비 체크를 해주어야한다.
        KtRentalApplication.setEmergency(mCurrentModel.getCarNum(), check);

    }

    // 결과 수정 이벤트
    @Override
    public void onModify(String gubun)
    {
        // TODO Auto-generated method stub
        mCurrentModel.setGUBUN(gubun);
        mRbConfirm.setVisibility(View.VISIBLE);
        mBtnTire.setVisibility(View.VISIBLE);
        mBtnDate.setVisibility(View.VISIBLE);
        mBtnTransfer.setVisibility(View.VISIBLE);
        mLlResultTitle.setVisibility(View.GONE);
        mRbConfirm.setEnabled(true);
        mRbConfirmFake.setVisibility(View.GONE);

        maintenanceResultInfoFragment.onModify();
        if (mResultModelMap.containsKey(mCurrentModel.getAddress()))
        {
            mResultModelMap.remove(mCurrentModel.getAddress());
        }
    }

    @Override
    public void onEmergencyListener(boolean emergencyFlag)
    {
        // TODO Auto-generated method stub
        mEmergencyMode = true;
        if (emergencyFlag)
        {
            mCurrentModel.setGUBUN("E");
            mRbConfirm.setVisibility(View.VISIBLE);
            mBtnTire.setVisibility(View.VISIBLE);
            mBtnDate.setVisibility(View.VISIBLE);
            mBtnTransfer.setVisibility(View.VISIBLE);
            mLlResultTitle.setVisibility(View.GONE);
            mChEmergency.setChecked(true);
            mRbConfirm.setEnabled(true);
            mRbConfirmFake.setVisibility(View.GONE);
        }
    }
}
