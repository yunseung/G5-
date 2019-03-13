package com.ktrental.adapter;

import java.util.StringTokenizer;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ktrental.R;
import com.ktrental.model.CallLogModel;
import com.ktrental.util.CommonUtil;

/**
 * 순회정비대상인 고객과에 통화정보를 보여주는 Adapter 이다. </br>
 * 
 * 
 * 
 * 
 * @author hongsungil
 */
public class CallogAdapter extends BaseCommonAdapter<CallLogModel> {

	public CallogAdapter(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void bindView(View rootView, Context context, int position) {
		// TODO Auto-generated method stub
		CallLogViewHolder holder = (CallLogViewHolder) rootView.getTag();
		CallLogModel callLogModel = getItem(position);

		if (callLogModel != null) {

			holder.tvDate
					.setText(CommonUtil.setDotDate(callLogModel.getDate()));
			holder.tvTime
					.setText(CommonUtil.setDotTime(callLogModel.getTime()));
			holder.tvType.setText(callLogModel.getType());
			String tel = callLogModel.getTel();
			 if(tel != null && tel.length() > 4)
	    	 {
				 holder.tvTel.setText(tel.subSequence(0, tel.length()-3)+context.getString(R.string.callog_star));
	    	 }
		}
	}

	@Override
	protected View newView(Context context, int position, ViewGroup viewgroup) {
		View rootView = mInflater.inflate(R.layout.call_log_row, viewgroup,
				false);

		CallLogViewHolder holder = new CallLogViewHolder();

		holder.tvDate = (TextView) rootView.findViewById(R.id.tv_day);
		holder.tvType = (TextView) rootView.findViewById(R.id.tv_type);
		holder.tvTime = (TextView) rootView.findViewById(R.id.tv_time);
		holder.tvTel = (TextView) rootView.findViewById(R.id.tv_tel);

		rootView.setTag(holder);

		return rootView;
	}

	private class CallLogViewHolder {
		TextView tvDate;
		TextView tvType;
		TextView tvTime;
		TextView tvTel;
	}

}
