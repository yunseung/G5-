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

public class TroubleHistoryItemDialog extends BaseTouchDialog implements Connector.ConnectInterface {

    private Context context;
    private View mRootView;
    private ListView mListView;

    private ConnectController cc;

    private String mVbenl = null;

    protected ProgressPopup mProgressPopup;

    public TroubleHistoryItemDialog(Context context, String vbeln) {
        super(context);
        this.context = context;
        this.mVbenl = vbeln;
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

        showProgress("조회 중입니다.");
        cc.getZMO_1020_RD07("1");

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
        Log.e("yunseung", FuntionName);
        Log.e("yunseung", resultText);
        Log.e("yunseung", MTYPE);
        Log.e("yunseung", resulCode + "");
        Log.e("yunseung", FuntionName);
        ArrayList<HashMap<String, String>> array_hash = new ArrayList<>();
        array_hash = tableModel.getTableArray();

        mListView.setAdapter(new ListAdapter(array_hash));
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
        private List<HashMap<String, String>> mhashArray;
        private ViewHolder mViewHolder = null;

        public ListAdapter(List<HashMap<String, String>> list) {
            mhashArray = list;
        }

        @Override
        public int getCount() {
            return mhashArray.size();
        }

        @Override
        public Object getItem(int position) {
            return mhashArray.get(position);
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
                mViewHolder.tvCdnmKor = (TextView) convertView.findViewById(R.id.cdnm_kor);
                mViewHolder.tvIndex = (TextView) convertView.findViewById(R.id.index);
                mViewHolder.tvRegDate = (TextView) convertView.findViewById(R.id.reg_date);
                mViewHolder.tvErrCd = (TextView) convertView.findViewById(R.id.err_cd);
                mViewHolder.tvTotalDistance = (TextView) convertView.findViewById(R.id.tot_dri_dist);
                convertView.setTag(mViewHolder);
            } else {
                mViewHolder = (ViewHolder)convertView.getTag();
            }

            mViewHolder.tvIndex.setText(mhashArray.get(position).get("NO"));
            mViewHolder.tvRegDate.setText(mhashArray.get(position).get("STDDAT"));
            mViewHolder.tvTotalDistance.setText(mhashArray.get(position).get("TOTDRVDIST"));
            mViewHolder.tvErrCd.setText(mhashArray.get(position).get("ERRCD"));
            mViewHolder.tvCdnmKor.setText(mhashArray.get(position).get("CDNMKOR"));

            return convertView;
        }

        class ViewHolder {
            public TextView tvIndex = null; // 순번
            public TextView tvRegDate = null; // 등록일자
            public TextView tvTotalDistance = null; //운행거리
            public TextView tvErrCd = null; // 항목코드
            public TextView tvCdnmKor = null; // 세부설명
        }
    }
}
