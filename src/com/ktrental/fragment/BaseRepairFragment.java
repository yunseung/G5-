package com.ktrental.fragment;

import com.ktrental.adapter.BaseMaintenceAdapter.OnClickRootView;
import com.ktrental.calendar.DayInfoModel;
import com.ktrental.cm.db.DbAsyncTask;
import com.ktrental.common.KtRentalApplication;
import com.ktrental.common.RepairPlanObserver;
import com.ktrental.model.BaseMaintenanceModel;
import com.ktrental.util.OnChangeFragmentListener;
import com.ktrental.util.OnSelectedItem;

import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;

/**
 * 일정관련된 화면들이 상속받는다. </br>일일 단위 순회정비 대상차량을 관리하는 화면. </br> BaseResultFragment를
 * 상속받음.
 *
 * @author hongsungil
 */
public abstract class BaseRepairFragment extends BaseResultFragment implements
        Observer, OnSelectedItem, OnClickRootView {

    protected abstract void updateRepairPlan();

    protected abstract void initSelectedMaintenanceArray(String currentDay);

    protected abstract void queryMaintenace(String currentDay);

    protected abstract void initScroll();

    private boolean isRepairFlag = false;

    private HashMap<String, DbAsyncTask> mAsyncMap = new HashMap<String, DbAsyncTask>();

    private String mCurrentDay = "";

    public BaseRepairFragment() {
        super();
    }

    public BaseRepairFragment(String className,
                              OnChangeFragmentListener changeFragmentListener) {
        super(className, changeFragmentListener);
        KtRentalApplication.addRepairPlanObserver(this);
    }

    public void addRepairPlanObserver() {
        KtRentalApplication.addRepairPlanObserver(this);
    }

    @Override
    public void update(Observable observable, Object data) {
        // 순회 정비 계획 변경.
        if (observable instanceof RepairPlanObserver) {
            if (!this.isHidden()) {
                // 상속받은 클래스에 순회정비 계획이 바뀐것을 알려준다.
                updateRepairPlan();
                //2014-01-19 KDH 아놔여기서 다 업데이트떄리네-_-결론은..디비까지함댕겨와야하네..환장하긋네..한큐에좀하지..이걸다쓰겠다고
                //겁나쪼개놨어 ㅡ,.ㅡ;
            }

            isRepairFlag = true;
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        // TODO Auto-generated method stub
        super.onHiddenChanged(hidden);
        if (!hidden) {
            // if (isRepairFlag) {
            updateRepairPlan();
            isRepairFlag = false;
            // }
        }
    }

    protected String getPlan() {
        String rePlan = "";

        rePlan = String.valueOf(KtRentalApplication.getInstance().getPlanVal());

        return rePlan;
    }

    protected String getPlan2() {
        String rePlan = "";

        rePlan = String.valueOf(KtRentalApplication.getInstance().getPlanVal2());

        return rePlan;
    }

    protected String getPlan3() {
        String rePlan = "";

        rePlan = String.valueOf(KtRentalApplication.getInstance().getPlanVal3());

        return rePlan;
    }

    protected String getComplate() {
        String reComplate = "";

        reComplate = String.valueOf(KtRentalApplication.getInstance()
                .getComplateVal());

        return reComplate;
    }

    protected String getComplate2() {
        String reComplate = "";

        reComplate = String.valueOf(KtRentalApplication.getInstance()
                .getComplateVal2());

        return reComplate;
    }

    protected String getComplate3() {
        String reComplate = "";

        reComplate = String.valueOf(KtRentalApplication.getInstance()
                .getComplateVal3());

        return reComplate;
    }

    protected String getEmergency() {
        String reEmergency = "";

        reEmergency = String.valueOf(KtRentalApplication.getInstance()
                .getEmergencyVal());

        return reEmergency;
    }

    protected String getEmergency2() {
        String reEmergency2 = "";

        reEmergency2 = String.valueOf(KtRentalApplication.getInstance()
                .getEmergencyVal2());

        return reEmergency2;
    }

    protected String getEmergency3() {
        String reEmergency3 = "";

        reEmergency3 = String.valueOf(KtRentalApplication.getInstance()
                .getEmergencyVal3());

        return reEmergency3;
    }

    protected String getPossetion() {
        String rePossetion = "";

        rePossetion = String.valueOf(KtRentalApplication.getInstance().getPossetionVal());

        return rePossetion;
    }

    protected String getPossetion2() {
        String rePossetion = "";

        rePossetion = String.valueOf(KtRentalApplication.getInstance().getPossetionVal2());

        return rePossetion;
    }

    protected String getPossetion3() {
        String rePossetion = "";

        rePossetion = String.valueOf(KtRentalApplication.getInstance().getPossetionVal3());

        return rePossetion;
    }

    protected String getNotImplemetned() {
        String reNotImplemented = "";

        reNotImplemented = String.valueOf(KtRentalApplication.getInstance().getNotImplementedVal());

        return reNotImplemented;
    }

    protected String getNotLongImplemetned() {
        String reNotLongImplemented = "";

        reNotLongImplemented = String.valueOf(KtRentalApplication.getInstance().getNotLongImplementedVal());

        return reNotLongImplemented;
    }

    @Override
    public void OnSeletedItem(Object item) {
        // TODO Auto-generated method stub
        String currentDay = ((DayInfoModel) item).getCurrentDay();

        showProgress();

        // if (mCurrentDay != null) {
        // DbAsyncTask dbAsyncTask = mAsyncMap.get(mCurrentDay);
        // if (dbAsyncTask != null) {
        // mAsyncMap.remove(mCurrentDay);
        // dbAsyncTask.cancel(true);
        // hideProgress();
        // return;
        // }
        // }
        initSelectedMaintenanceArray(currentDay);
        queryMaintenace(currentDay);
        mCurrentDay = currentDay;

    }

    @Override
    public void onClickRoot(final BaseMaintenanceModel model) {

        showResultFragment(model);

    }

}
