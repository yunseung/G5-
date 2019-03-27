package com.ktrental.dialog;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
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

import java.util.ArrayList;
import java.util.List;

public class TroubleHistoryItemDialog extends BaseTouchDialog implements Connector.ConnectInterface {

    private Context context;
    private View mRootView;
    private ListView mListView;

    private ConnectController cc;
    private ProgressDialog pd;

    String mVbeml = null;

    public TroubleHistoryItemDialog(Context context, String vbeln) {
        super(context);
        this.context = context;
        this.mVbeml = vbeln;
        this.mVbeml = vbeln;
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


        cc = new ConnectController(this, context);
        pd = new ProgressDialog(context);
        pd.setCancelable(false);

//        cc.getZMO_1020_RD07();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.trouble_history_item_dialog);

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

        mListView = (ListView) mRootView.findViewById(R.id.iot_request_listview);

        List<String> reasonList = new ArrayList<>();
        reasonList.add("선택해주세요");
        reasonList.add("고객요청");
        reasonList.add("예약변경");
        reasonList.add("단순변심");
        reasonList.add("연락두절");
        reasonList.add("기타");

        mListView.setAdapter(new ReasonListAdapter(reasonList));

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


    @Override
    public void connectResponse(String FuntionName, String resultText, String MTYPE, int resulCode, TableModel tableModel) {

    }

    @Override
    public void reDownloadDB(String newVersion) {

    }


    public class ReasonListAdapter extends BaseAdapter {
        private List<String> mReasonList;
        private ViewHolder mViewHolder = null;

        public ReasonListAdapter(List<String> list) {
            mReasonList = list;
        }

        @Override
        public int getCount() {
            return mReasonList.size();
        }

        @Override
        public Object getItem(int position) {
            return mReasonList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(mContext).inflate(R.layout.trouble_history_item_dialog_row, null);
                mViewHolder = new ViewHolder();
                mViewHolder.tv = (TextView) convertView.findViewById(R.id.cdnm_kor);
                convertView.setTag(mViewHolder);
            } else {
                mViewHolder = (ViewHolder)convertView.getTag();
            }

            mViewHolder.tv.setText(mReasonList.get(position));
            return convertView;
        }

        class ViewHolder {
            public TextView tv = null;
        }
    }
}
