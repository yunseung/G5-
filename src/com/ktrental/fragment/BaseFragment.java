package com.ktrental.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.WindowManager.BadTokenException;

import com.ktrental.R;
import com.ktrental.activity.Main_Activity;
import com.ktrental.cm.connect.ConnectorUtil;
import com.ktrental.common.KtRentalApplication;
import com.ktrental.popup.BaseTouchDialog;
import com.ktrental.popup.EventPopup1;
import com.ktrental.popup.EventPopup2;
import com.ktrental.popup.ProgressPopup;
import com.ktrental.util.CommonUtil;
import com.ktrental.util.LogUtil;
import com.ktrental.util.NetworkUtil;
import com.ktrental.util.OnChangeFragmentListener;
import com.ktrental.util.OnEventCancelListener;
import com.ktrental.util.OnEventOkListener;
import com.ktrental.util.OnStartFragmentListner;

/**
 * 순회정비기사 관련 화면들이 상속받는 Fragment 이다. </br> </br>extends {@link DialogFragment} 일반 Fragment와 다른점은 다이얼로그형태로 보여주기위해 상속. </br> </br> progress , 이벤트팝업등을 호출할
 * 수 있다.</br>
 *
 * @author hongsungil
 */
public class BaseFragment extends DialogFragment
{

    private OnStartFragmentListner     mOnAttachedFragment;

    protected String                   mClassName;

    protected Context                  mContext;

    protected OnChangeFragmentListener mChangeFragmentListener;

    protected Activity                 mActivity;

    private int                        mDialogWidth  = -1;
    private int                        mDialogHeight = -1;

    private ProgressDialog             mProgressDialog;

    protected View                     mRootView;

    protected FragmentManager          mFragmentManager;

    protected ProgressPopup            mProgressPopup;
    
    
    public BaseFragment()
    {
        // TODO Auto-generated constructor stub
    }

    public BaseFragment(String className, OnChangeFragmentListener changeFragmentListener)
    {
        mClassName = className;
        mChangeFragmentListener = changeFragmentListener;

    }

    public void setClassName(String className)
    {
        mClassName = className;
    }

    public String getClassName()
    {
        return mClassName;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        // setRetainInstance(false); // 재활용
        mFragmentManager = getFragmentManager();
        setStyle(DialogFragment.STYLE_NO_FRAME, 0);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        mContext = getActivity();
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onAttach(Activity activity)
    {
        mContext = activity;
        mActivity = activity;

        mProgressDialog = new ProgressDialog(mContext, R.style.Theme_Dialog);
        mProgressDialog.setCanceledOnTouchOutside(false);

        mProgressPopup = new ProgressPopup(mContext);

        super.onAttach(activity);
    }

    public void setOnAttachedFragment(OnStartFragmentListner aOnAttachedFragment)
    {
        mOnAttachedFragment = aOnAttachedFragment;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        // TODO Auto-generated method stub
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public void onStart()
    {
        // TODO Auto-generated method stub
        super.onStart();

        if (getDialog() == null)
            return;

        
        
        WindowManager.LayoutParams lp = getDialog().getWindow().getAttributes();

        // WindowManager.LayoutParams lp =
        // getDialog().getWindow().getAttributes();
        lp.dimAmount = 0.6f;
        getDialog().getWindow().setAttributes(lp);

        getDialog().getWindow().setLayout(mDialogWidth, mDialogHeight);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(0));
        getDialog().setCanceledOnTouchOutside(true);

        getDialog().getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        // Dialog dlg = getDialog();

        mRootView = getView();
    }

    // @Override
    // public Dialog onCreateDialog(Bundle savedInstanceState) {
    //
    // Dialog dialog = new BaseTouchDialog(mContext);
    // dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
    // WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
    // params.width = WindowManager.LayoutParams.WRAP_CONTENT;
    // dialog.getWindow().setAttributes(params);
    // return dialog;
    // }
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        // TODO Auto-generated method stub
        Dialog dialog = new BaseTouchDialog(mContext, getTheme());
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
        params.width = WindowManager.LayoutParams.WRAP_CONTENT;
        dialog.getWindow().setAttributes(params);
        return dialog;
    }

    @Override
    public void onViewStateRestored(Bundle savedInstanceState)
    {

        super.onViewStateRestored(savedInstanceState);
    }

    @Override
    public void onResume()
    {
        // TODO Auto-generated method stub
        super.onResume();
        if (mOnAttachedFragment != null)
            mOnAttachedFragment.onAttachedFragment(mClassName);

    }

    public void setOnChangeFragmentListener(OnChangeFragmentListener aChangeFragmentListener)
    {
        mChangeFragmentListener = aChangeFragmentListener;
    }

    public OnChangeFragmentListener getOnChangeFragmentListener()
    {
        return mChangeFragmentListener;
    }

    protected void changfragment(Fragment fragment)
    {
        if (mChangeFragmentListener != null)
        {
            mChangeFragmentListener.onChageFragment(mClassName, fragment);
        }
    }

    @Override
    public void onDestroyView()
    {
        if (getView() != null)
            CommonUtil.unbindDrawables(getView());
        mProgressDialog = null;
        super.onDestroyView();
    }

    @Override
    public void onDestroy()
    {

        super.onDestroy();
        System.gc();
        mContext = null;
        mChangeFragmentListener = null;
        mOnAttachedFragment = null;
    }

    public void show(FragmentManager manager, String tag, int width, int height)
    {

        if (!isVisible())
        {
            mDialogWidth = width;
            mDialogHeight = height;

            super.show(manager, tag);
        }
    }

    public void showProgress()
    {
        if (mProgressPopup != null)
        {
            if (mRootView != null)
            {
                // CommonUtil.showCallStack();
                mRootView.post(new Runnable()
                {

                    @Override
                    public void run()
                    {
                        // TODO Auto-generated method stub
                        if (mProgressPopup != null)
                            mProgressPopup.show();
                    }
                });

            }
            else
            {
                mProgressPopup.show();
            }
        }
    }

    public void showProgress(String message)
    {
        if (mProgressPopup != null)
        {
            mProgressPopup.setMessage(message);
            if (mRootView != null)
            {
                // CommonUtil.showCallStack();
                mRootView.post(new Runnable()
                {

                    @Override
                    public void run()
                    {
                        // TODO Auto-generated method stub
                        try
                        {
                            if (mProgressPopup != null)
                            {
                                mProgressPopup.show();
                            }
                        }
                        catch (BadTokenException e)
                        {
                            // TODO: handle exception
                        }
                        catch (IllegalStateException e)
                        {
                            // TODO: handle exception
                        }

                    }
                });

            }
            else
            {
                mProgressPopup.show();
            }
        }
    }

    public void hideProgress()
    {
        if (mProgressPopup != null)
        {
            if (mRootView != null)
            {
                mRootView.post(new Runnable()
                {

                    @Override
                    public void run()
                    {
                        if (mProgressPopup != null && mProgressPopup.isShowing()) {
                            mProgressPopup.dismiss();
                        }
                    }
                });

            }
            else
            {
                mProgressPopup.hide();
            }
        }
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
    public void onDismiss(DialogInterface dialog)
    {
        // TODO Auto-generated method stub
        super.onDismiss(dialog);
        onDestroyView();
    }

    protected boolean isNetwork()
    {

        boolean reNetwork = false;

        if (!NetworkUtil.isNetwork(mContext))
        {
            showEventPopup2(null, "인터넷과 연결되지 않아 해당기능을 사용할 수 없습니다. 인터넷 연결 후 다시 시도해주세요.");
        }
        else
            reNetwork = true;

        return reNetwork;
    }

    public void showEventPopup1(String title, String body, String okText, OnEventOkListener onEventPopupListener,
            OnEventCancelListener onEventCancelListener)
    {
        EventPopup1 popup = new EventPopup1(mContext, body, onEventPopupListener);
        popup.setOnCancelListener(onEventCancelListener);
        popup.show();
        popup.setOkButtonText(okText);
    }

    public void showEventPopup1(String title, String body, OnEventOkListener onEventPopupListener, OnEventCancelListener onEventCancelListener)
    {
        EventPopup1 popup = new EventPopup1(mContext, body, onEventPopupListener);
        popup.setOnCancelListener(onEventCancelListener);
        popup.show();
    }

    public void showEventPopup2(OnEventOkListener onEventPopupListener, String body)
    {
        EventPopup2 eventPopup = new EventPopup2(mContext, body, onEventPopupListener);

        eventPopup.show();

    }

    public void moveMaintenance(String moveDay, String progressType, String showText)
    {
        if (mContext instanceof Main_Activity)
        {
            Main_Activity mainActivity = (Main_Activity) mContext;
            mainActivity.onMoveMaintenace(moveDay, progressType, showText);

        }
    }

    // Jonathan 14.11.07
    public void moveNotice()
    {
        if (mContext instanceof Main_Activity)
        {
            Main_Activity mainActivity = (Main_Activity) mContext;
            mainActivity.onMoveNotice();

        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        LogUtil.d("HJT", "test requestCode = " + requestCode);
        LogUtil.d("HJT", "test requestCode = " + resultCode);
    }
    

}
