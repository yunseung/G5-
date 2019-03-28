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

public class IoTRequestItemDialog extends BaseTouchDialog implements Connector.ConnectInterface {

    private Context mContext;

    private View mRootView;
    private TextView mTvTotalPrice, mTvCarKind, mTvMemo;
    private ListView mListView;

    private ConnectController mCc;
    protected ProgressPopup mProgressPopup;

    private String mReqNo = null;

    public IoTRequestItemDialog(Context context, String reqNo) {
        super(context);
        this.mContext = context;
        this.mReqNo = reqNo;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.iot_request_item_dialog);

        mRootView = findViewById(R.id.rl_root_view);
        mTvTotalPrice = (TextView)findViewById(R.id.total_price);
        mTvCarKind = (TextView)findViewById(R.id.tv_car_kind);
        mTvMemo = (TextView)findViewById(R.id.tv_memo);

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

        mCc = new ConnectController(this, mContext);

        showProgress("조회 중입니다.");
        mCc.getZMO_1020_RD06(mReqNo);
//        mCc.getZMO_1020_RD06("19032700006");

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
        hideProgress();

        if (MTYPE.trim().equals("S")) {
            Log.e("yunseung", FuntionName);
            Log.e("yunseung", resultText);
            Log.e("yunseung", MTYPE);
            Log.e("yunseung", resulCode + "");
            Log.e("yunseung", FuntionName);

            ArrayList<HashMap<String, String>> ET_ITAB = tableModel.getTableArray("ET_ITAB");
            ArrayList<HashMap<String, String>> ET_ITAB2 = tableModel.getTableArray("ET_ITAB2");


            // 마지막 row 의 DMBTR 이 총 가격
            mTvTotalPrice.setText(ET_ITAB.get(ET_ITAB.size() - 1).get("DMBTR"));
            mTvCarKind.setText(ET_ITAB2.get(0).get("MAKTX"));
            mTvMemo.setText(ET_ITAB2.get(0).get("MEMO"));

            mListView.setAdapter(new ListAdapter(ET_ITAB));
        } else {
            // ?????????????????????????????????????????? 어쩔까
        }
    }

    @Override
    public void reDownloadDB(String newVersion) {

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
                        catch (WindowManager.BadTokenException e)
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


    public class ListAdapter extends BaseAdapter {
        private List<HashMap<String, String>> mHashArray;
        private ViewHolder mViewHolder = null;

        public ListAdapter(List<HashMap<String, String>> list) {
            mHashArray = list;
        }

        @Override
        public int getCount() {
            return mHashArray.size();
        }

        @Override
        public Object getItem(int position) {
            return mHashArray.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(mContext).inflate(R.layout.iot_request_item_row, null);
                mViewHolder = new ViewHolder();
                mViewHolder.tvNo = (TextView) convertView.findViewById(R.id.tv_no);
                mViewHolder.tvItem = (TextView) convertView.findViewById(R.id.tv_item);
                mViewHolder.tvPartName = (TextView) convertView.findViewById(R.id.tv_part_name);
                mViewHolder.tvQuantity = (TextView) convertView.findViewById(R.id.tv_quantity);
                mViewHolder.tvPrice = (TextView) convertView.findViewById(R.id.tv_price);
                convertView.setTag(mViewHolder);
            } else {
                mViewHolder = (ViewHolder)convertView.getTag();
            }

            mViewHolder.tvNo.setText(mHashArray.get(position).get("NO"));
            mViewHolder.tvItem.setText(mHashArray.get(position).get("WGBEZ"));
            mViewHolder.tvPartName.setText(mHashArray.get(position).get("MAKTX"));
            mViewHolder.tvQuantity.setText(mHashArray.get(position).get("QTY"));
            mViewHolder.tvPrice.setText(mHashArray.get(position).get("DMBTR"));
            return convertView;
        }

        class ViewHolder {
            public TextView tvNo = null;
            public TextView tvItem = null;
            public TextView tvPartName = null;
            public TextView tvQuantity = null;
            public TextView tvPrice = null;
        }
    }
}
