package com.ktrental.popup;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.ktrental.R;
import com.ktrental.cm.connect.ConnectController;
import com.ktrental.cm.connect.Connector;
import com.ktrental.dialog.IoTRequestItemDialog;
import com.ktrental.model.TableModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class IoTCancelPopup extends BaseTouchDialog implements Connector.ConnectInterface {

    private View mRootView;
    private Button mSpReason;
    private String mReqNo;
    private EditText mDetailMemo;

    private ConnectController mCc;
    protected ProgressPopup mProgressPopup;

    private Popup_Window_Multy pwm;

    public IoTCancelPopup(Context context, String reqNo) {
        super(context);

        mReqNo = reqNo;
    }

    @Override
    public void onStart() {
        // TODO Auto-generated method stub
        super.onStart();
        setCanceledOnTouchOutside(false);
        WindowManager.LayoutParams lp = getWindow().getAttributes();

        // WindowManager.LayoutParams lp =
        // getDialog().getWindow().getAttributes();
        lp.dimAmount = 0.6f;
        getWindow().setAttributes(lp);

        getWindow().setBackgroundDrawable(new ColorDrawable(0));
        // setCanceledOnTouchOutside(true);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);

        pwm = new Popup_Window_Multy(mContext);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.iot_cancel_layout);

        mRootView = findViewById(R.id.rl_root_view);

        mRootView.findViewById(R.id.iv_exit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        TextView tvTitle = (TextView) mRootView
                .findViewById(R.id.tv_dialog_title);
        tvTitle.setText("IoT정비취소등록");

        mRootView.findViewById(R.id.btn_registration).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mSpReason.getTag() == null) {
                    Toast.makeText(mContext, "사유를 선택해주세요.", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (mDetailMemo.getText().toString().isEmpty()) {
                    Toast.makeText(mContext, "싱세사유를 입력해주세요.", Toast.LENGTH_SHORT).show();
                    return;
                }
                mCc = new ConnectController(IoTCancelPopup.this, mContext);
                showProgress("조회 중입니다.");
                mCc.getZMO_1020_WR01(mReqNo, mSpReason.getTag().toString(), mDetailMemo.getText().toString());
            }
        });

        mDetailMemo = (EditText) mRootView.findViewById(R.id.detail_memo);

        mSpReason = (Button) mRootView.findViewById(R.id.sp_reason);
        mSpReason.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pwm.show("PM307", mSpReason);
            }
        });

    }

    @Override
    public void dismiss() {
        super.dismiss();

    }


    @Override
    public void connectResponse(String FuntionName, String resultText, String MTYPE, int resulCode, TableModel tableModel) {
        hideProgress();

        Toast.makeText(mContext, resultText, Toast.LENGTH_SHORT).show();
        dismiss();
    }

    @Override
    public void reDownloadDB(String newVersion) {

    }


    public void showProgress(String message) {
        if (mProgressPopup != null) {
            mProgressPopup.setMessage(message);
            if (mRootView != null) {
                // CommonUtil.showCallStack();
                mRootView.post(new Runnable() {

                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        try {
                            if (mProgressPopup != null) {
                                mProgressPopup.show();
                            }
                        } catch (WindowManager.BadTokenException e) {
                            // TODO: handle exception
                        } catch (IllegalStateException e) {
                            // TODO: handle exception
                        }

                    }
                });

            } else {
                mProgressPopup.show();
            }
        }
    }

    public void hideProgress() {
        if (mProgressPopup != null) {
            if (mRootView != null) {
                mRootView.post(new Runnable() {

                    @Override
                    public void run() {
                        if (mProgressPopup != null && mProgressPopup.isShowing()) {
                            mProgressPopup.dismiss();
                        }
                    }
                });

            } else {
                mProgressPopup.hide();
            }
        }
    }
}
