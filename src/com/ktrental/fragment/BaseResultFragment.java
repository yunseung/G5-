package com.ktrental.fragment;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;

import com.ktrental.cm.connect.ConnectController;
import com.ktrental.cm.connect.Connector.ConnectInterface;
import com.ktrental.cm.db.DbAsyncTask;
import com.ktrental.cm.db.DbAsyncTask.DbAsyncResLintener;
import com.ktrental.common.DEFINE;
import com.ktrental.dialog.Mistery_Shopping_Dialog;
import com.ktrental.model.BaseMaintenanceModel;
import com.ktrental.model.CarInfoModel;
import com.ktrental.model.TableModel;
import com.ktrental.util.CommonUtil;
import com.ktrental.util.OnChangeFragmentListener;
import com.ktrental.util.OnEventCancelListener;
import com.ktrental.util.OnEventOkListener;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * 결과등록화면이 필요한 화면들이 상속을 받는다. </br> </br>extends {@link BaseFragment} </br>
 * 
 * @author hongsungil
 */
public abstract class BaseResultFragment extends BaseFragment
{

    protected abstract void movePlan(ArrayList<BaseMaintenanceModel> models);

    protected ArrayList<BaseMaintenanceModel> mBaseMaintenanceModels = new ArrayList<BaseMaintenanceModel>();

    public BaseResultFragment()
    {
        super();
    }

    public BaseResultFragment(String className, OnChangeFragmentListener changeFragmentListener)
    {
        super(className, changeFragmentListener);
    }

    protected void showResultFragment(final BaseMaintenanceModel model, CarInfoModel carInfoModel)
    {
        ArrayList<BaseMaintenanceModel> baseMaintenanceModels = new ArrayList<BaseMaintenanceModel>();
        baseMaintenanceModels.add(model);

        setCurrentImageName(baseMaintenanceModels);
        changfragment(new MaintenanceResultFragment(MaintenanceResultFragment.class.getName(), mChangeFragmentListener, baseMaintenanceModels,
                carInfoModel));
    }

    protected void showResultFragment(final BaseMaintenanceModel model, boolean flag)
    {

        if (model.getProgress_status().equals("E0001"))
        { // 예약 대기일때
            // 예정일 변경 으로 이동..
            movePlanFragment(model);
        }
        else if (model.getProgress_status().equals("E0002"))
        { // 예약 일때
            if (!model.getDay().equals(CommonUtil.getCurrentDay()))
            {
                showEventPopup1("", "순회정비 예정일이 아닙니다.\n 해당 차량의 정비 예정일을 금일로 변경하시겠습니까?", new OnEventOkListener()
                {

                    @Override
                    public void onOk()
                    {
                        // TODO Auto-generated method stub
                        setChangeDate(model);
                    }
                }, new OnEventCancelListener()
                {

                    @Override
                    public void onCancel()
                    {
                        // TODO Auto-generated method stub
                        // 정비결과등록 화면 이동.
                        Mistery_Shopping_Dialog dlg = new Mistery_Shopping_Dialog(mContext, "10");
                        dlg.setOnDismissListener(new Dialog.OnDismissListener() {
                            @Override
                            public void onDismiss(DialogInterface dialog) {
                                moveResultFragment(model, true, false);
                            }
                        });
                        dlg.show();
//                        moveResultFragment(model, true, false);
                    }
                });
                return;
            }
            else
            {
                // TODO Auto-generated method stub
                // 정비결과등록 화면 이동.
                Mistery_Shopping_Dialog dlg = new Mistery_Shopping_Dialog(mContext, "10");
                dlg.setOnDismissListener(new Dialog.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        moveResultFragment(model, true, false);
                    }
                });
                dlg.show();
//                moveResultFragment(model, true, false);
            }

        }
        else if (model.getProgress_status().equals("E0003"))
        { // 이관요청일때
            // ??

            showEventPopup2(new OnEventOkListener()
            {

                @Override
                public void onOk()
                {
                    // TODO Auto-generated method stub
                }
            }, "이관요청 차량은 순회정비 대상이 아닙니다.");
        }
        else if (model.getProgress_status().equals("E0004"))
        { // 완료일때
            // 정비결과등록 화면 이동.
            Mistery_Shopping_Dialog dlg = new Mistery_Shopping_Dialog(mContext, "10");
            dlg.setOnDismissListener(new Dialog.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    moveResultFragment(model, false, false);
                }
            });
            dlg.show();
//            moveResultFragment(model, false, false); // 수정

        }
        else
        {

        }

    }

    protected void showResultFragment(final BaseMaintenanceModel model)
    {

        if(model != null
                && model.getProgress_status() != null) {
            if (model.getProgress_status().equals("E0001")) { // 예약 대기일때
                // 예정일 변경 으로 이동..
                movePlanFragment(model);
            } else if (model.getProgress_status().equals("E0002")) { // 예약 일때
                if (!model.getDay().equals(CommonUtil.getCurrentDay())) {
                    showEventPopup1("", "순회정비 예정일이 아닙니다.\n 해당 차량의 정비 예정일을 금일로 변경하시겠습니까?", new OnEventOkListener() {

                        @Override
                        public void onOk() {
                            // TODO Auto-generated method stub
                            setChangeDate(model);
                        }
                    }, new OnEventCancelListener() {

                        @Override
                        public void onCancel() {
                            // TODO Auto-generated method stub
                            // 정비결과등록 화면 이동.
                            Mistery_Shopping_Dialog dlg = new Mistery_Shopping_Dialog(mContext, "10");
                            dlg.setOnDismissListener(new Dialog.OnDismissListener() {
                                @Override
                                public void onDismiss(DialogInterface dialog) {
                                    moveResultFragment(model, true, false);
                                }
                            });
                            dlg.show();
//                            moveResultFragment(model, true, false);
                        }
                    });
                } else {
                    // TODO Auto-generated method stub
                    // 정비결과등록 화면 이동.
                    Mistery_Shopping_Dialog dlg = new Mistery_Shopping_Dialog(mContext, "10");
                    dlg.setOnDismissListener(new Dialog.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialog) {
                            moveResultFragment(model, true, true);
                        }
                    });
                    dlg.show();
                }

            } else if (model.getProgress_status().equals("E0003")) { // 이관요청일때
                // ??

                showEventPopup2(new OnEventOkListener() {

                    @Override
                    public void onOk() {
                        // TODO Auto-generated method stub
                    }
                }, "이관요청 차량은 순회정비 대상이 아닙니다.");
            } else if (model.getProgress_status().equals("E0004")) { // 완료일때
                // 정비결과등록 화면 이동.
                Mistery_Shopping_Dialog dlg = new Mistery_Shopping_Dialog(mContext, "10");
                dlg.setOnDismissListener(new Dialog.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        moveResultFragment(model, false, false);
                    }
                });
                dlg.show();
//                moveResultFragment(model, false, false); // 수정
            }
        }
        else
        {

        }

    }

    /**
     * 예정일 변경 화면으로 이동할지 선택한다.
     */
    private void movePlanFragment(final BaseMaintenanceModel model)
    {

        showEventPopup1(" ", "정비예정일이 등록되지 않았습니다. 정비예정일 등록화면으로 이동하겠습니까?", new OnEventOkListener()
        {

            @Override
            public void onOk()
            {
                // TODO Auto-generated method stub
                ArrayList<BaseMaintenanceModel> array = new ArrayList<BaseMaintenanceModel>();
                array.add(model);
                movePlan(array);
            }
        }, null);
    }

    /**
     * 결과등록화면으로 이동할지 선택
     * 
     * @param model
     */
    private void moveResultFragment(final BaseMaintenanceModel model, boolean completeFlag, boolean isOne)
    {
        final ArrayList<BaseMaintenanceModel> baseMaintenanceModels = getSeletedItemLikeInfo(model, isOne);

        if (completeFlag)
        {
            setChangeValue(baseMaintenanceModels, " ");
        }

        if (baseMaintenanceModels.size() > 1)
        { // 날짜,주소가 같은 model 1 개 이상 일때

            showEventPopup1(" ", "동일한 주소지에 정비 대상 차량이 있습니다. " + model.getCarNum() + " 차량과 같이 정비하시겠습니까?", new OnEventOkListener()
            {

                @Override
                public void onOk()
                {
                    // TODO Auto-generated method stub
                    setCurrentImageName(baseMaintenanceModels);
                    changfragment(new MaintenanceResultFragment(MaintenanceResultFragment.class.getName(), mChangeFragmentListener,
                            baseMaintenanceModels));
                }
            }, new OnEventCancelListener()
            {

                @Override
                public void onCancel()
                {
                    // TODO Auto-generated method stub
                    setCurrentImageName(baseMaintenanceModels);
                    baseMaintenanceModels.clear();
                    baseMaintenanceModels.add(model);

                    changfragment(new MaintenanceResultFragment(MaintenanceResultFragment.class.getName(), mChangeFragmentListener,
                            baseMaintenanceModels));
                }
            });

        }
        else
        { // 1개 일때
            setCurrentImageName(baseMaintenanceModels);
            changfragment(new MaintenanceResultFragment(MaintenanceResultFragment.class.getName(), mChangeFragmentListener, baseMaintenanceModels));

        }
    }

    private void setCurrentImageName(ArrayList<BaseMaintenanceModel> baseMaintenanceModels)
    {
        for (BaseMaintenanceModel baseMaintenanceModel : baseMaintenanceModels)
        {
            baseMaintenanceModel.setImageName(CommonUtil.getCurrentTime());
        }
    }

    private void setChangeValue(ArrayList<BaseMaintenanceModel> baseMaintenanceModels, String GUBUN)
    {
        for (BaseMaintenanceModel baseMaintenanceModel : baseMaintenanceModels)
        {
            baseMaintenanceModel.setGUBUN(GUBUN);
        }
    }

    private ArrayList<BaseMaintenanceModel> getSeletedItemLikeInfo(BaseMaintenanceModel model, boolean completeFlag)
    {

        String selAddress = model.getAddress();
        String selDay = model.getDay();

        ArrayList<BaseMaintenanceModel> baseMaintenanceModels = new ArrayList<BaseMaintenanceModel>();

        baseMaintenanceModels.add(model);
        if (!completeFlag)
            return baseMaintenanceModels;

        // String lastKufnr = model.getName();

        if (selDay != null && selAddress != null)
        {

            for (int i = 0; i < mBaseMaintenanceModels.size(); i++)
            {
                BaseMaintenanceModel baseMaintenanceModel = mBaseMaintenanceModels.get(i);
                String address = baseMaintenanceModel.getAddress();
                String day = baseMaintenanceModel.getDay();
                String progress = baseMaintenanceModel.getProgress_status();

                if (model != baseMaintenanceModel)
                {

                    if (address != null && day != null)
                    {
                        if (address.equals(selAddress) && selDay.equals(day) && progress.equals(model.getProgress_status()))
                        {
                            baseMaintenanceModels.add(baseMaintenanceModel);
                        }
                    }
                }
            }

        }

        return baseMaintenanceModels;
    }

    private void setChangeDate(final BaseMaintenanceModel model)
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
                // TODO Auto-generated method stub
                if (MTYPE.equals("S"))
                {
                    updateComplete(model);

                }
                else
                {
                    showEventPopup2(null, "" + resultText);
                }
            }
        }, mContext);
        connectController.setZMO_1050_WR04(CommonUtil.getCurrentDay(), getTable(model));
        showProgress("예정일을 변경 중 입니다.");
    }

    private ArrayList<HashMap<String, String>> getTable(BaseMaintenanceModel model)
    {
        ArrayList<HashMap<String, String>> i_itab1 = new ArrayList<HashMap<String, String>>();
        HashMap<String, String> hm = new HashMap<String, String>();
        hm.put("AUFNR", model.getAUFNR());
        i_itab1.add(hm);

        return i_itab1;
    }

    private void updateComplete(final BaseMaintenanceModel model)
    {
        ContentValues contentValues = new ContentValues();
        contentValues.put("CCMSTS", "E0002");
        contentValues.put("GSTRS", CommonUtil.getCurrentDay());
        contentValues.put("AUFNR", model.getAUFNR());
        String[] keys = new String[1];
        keys[0] = "AUFNR";
        DbAsyncTask dbAsyncTask = new DbAsyncTask("updateComplete", DEFINE.REPAIR_TABLE_NAME, mContext, new DbAsyncResLintener()
        {

            @Override
            public void onCompleteDB(String funName, int type, Cursor cursor, String tableName)
            {
                // TODO Auto-generated method stub
                ArrayList<BaseMaintenanceModel> baseMaintenanceModels = new ArrayList<BaseMaintenanceModel>();
                baseMaintenanceModels.add(model);
                setChangeValue(baseMaintenanceModels, " ");
                setCurrentImageName(baseMaintenanceModels);
                changfragment(new MaintenanceResultFragment(MaintenanceResultFragment.class.getName(), mChangeFragmentListener, baseMaintenanceModels));

                // 2014-01-19 KDH 여기서 SAP댕겨오고 업데이트된 해당월 작업건들이 업데이트가되면
                // 여기에서 데이터 뽑아서 가지고 가야 한다.업데이트치고난 후에 결과 완료된 것을 주면 끝.하지만 있겠지?
                // 2014-01-20 KDH 결론 최초 로그인 이후 한번이니.여기서 하지않고 홈에서 처리한다.

            }
        }, contentValues, keys);
        dbAsyncTask.execute(DbAsyncTask.DB_UPDATE);
    }
}
