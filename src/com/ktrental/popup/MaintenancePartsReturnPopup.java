package com.ktrental.popup;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.ktrental.R;

/**
 * 수량입력 팝업. <br/>
 * QucikAction({@link QuickAction}) 이라는 팝업을 상속받아 구현.
 * 
 * 
 * 
 * @author Hong
 * @since 2013.07.12
 */
public class MaintenancePartsReturnPopup extends QuickAction implements android.widget.PopupWindow.OnDismissListener
{

    private Button mBtnInput;
    private OnDismissListener mDismissListener;
    private Context context;
    
    public interface OnDismissListener
        {
        public abstract void onDismiss(String result, int position);
        }

    public MaintenancePartsReturnPopup(Context context, int orientation, int layoutId)
        {
        super(context, orientation);
        this.context = context;
        initViewSettings(layoutId);
        }
    
    public void setOnDismissListener(OnDismissListener listener) 
        {
        setOnDismissListener(this);
        mDismissListener = listener;
        }

    EditText et;
    private void initViewSettings(int layoutId)
        {
        addLayout(layoutId);
        }
    

    
    public ViewGroup getViewGroup()
        {
        return mTrack;
        }

    @Override
    public void onDismiss()
        {
        EditText et = (EditText)mTrack.findViewById(R.id.maintenance_return_edittext_id);
        et.setText("");
        super.onDismiss();
        }

    public void show(View anchor)
        {
        super.show(anchor);
        
        et = (EditText)mTrack.findViewById(R.id.maintenance_return_edittext_id);
        pre.sendEmptyMessageDelayed(0, 100);
        }
    
    Handler pre = new Handler()
        {
        @Override
        public void handleMessage(Message msg)
            {
            super.handleMessage(msg);
            InputMethodManager imm = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(et, 0); //키보드올리기﻿
            }
        };
}
