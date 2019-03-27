package com.ktrental.dialog;

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
import android.widget.ListView;
import android.widget.TextView;

import com.ktrental.R;
import com.ktrental.cm.connect.ConnectController;
import com.ktrental.cm.connect.Connector;
import com.ktrental.model.TableModel;
import com.ktrental.popup.BaseTouchDialog;
import com.ktrental.popup.ProgressPopup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SimpleTextDialog extends BaseTouchDialog {

    private Context mContext;

    private View mRootView;
    private TextView mTvTitle, mTvContent;

    public SimpleTextDialog(Context context, String title, String content) {
        super(context);
        this.mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.iot_request_item_dialog);

        mRootView = findViewById(R.id.rl_root_view);

        mRootView.findViewById(R.id.iv_exit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        mRootView.findViewById(R.id.btn_confirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

    }

    @Override
    public void onStart() {
        // TODO Auto-generated method stub
        super.onStart();
        setCanceledOnTouchOutside(false);
        WindowManager.LayoutParams lp = getWindow().getAttributes();

        // WindowManager.LayoutParams lp =
        // getDialog().getWindow().getAttributes();
//        lp.dimAmount = 0.6f;
//        getWindow().setAttributes(lp);

        getWindow().setBackgroundDrawable(new ColorDrawable(0));
        // setCanceledOnTouchOutside(true);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);

    }

    @Override
    public void dismiss() {
        super.dismiss();

        // FragmentTransaction transaction = getFragmentManager()
        // .beginTransaction();
        // transaction.detach(this);
        // transaction.commit();
        // getChildFragmentManager().executePendingTransactions();
        // onDetach();
        // onDestroy();
    }

}
