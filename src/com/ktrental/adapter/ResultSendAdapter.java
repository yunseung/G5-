package com.ktrental.adapter;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ktrental.R;
import com.ktrental.model.ResultSendModel;
import com.ktrental.util.CommonUtil;
/**
 * </br> 결과등록이 실패한 리스트를 보여주는 Adapter 이다. </br>
 * 
 * @author hongsungil
 */
public class ResultSendAdapter extends BaseCommonAdapter<ResultSendModel>
		implements OnClickListener {

	public ResultSendAdapter(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void bindView(View rootView, Context context, int position) {
		// TODO Auto-generated method stub
		ResultSendHolder holder = (ResultSendHolder) rootView.getTag();

		ResultSendModel model = getItem(position);

		if (holder != null) {
			// holder.tvDay.setText(model.get());
			holder.tvCarnum.setText(model.getINVNR());
			holder.tvName.setText(model.getName());
//			holder.tvCount.setText(model.getCount() + "회");
			holder.tvCount.setText(model.getmessage());

			String day = CommonUtil.setDotDate(model.getIEDD()) + "("
					+ CommonUtil.getDayOfWeek(model.getIEDD()) + ") "
					+ CommonUtil.setDotTime(model.getIEDZ().substring(0, 4));
			holder.tvDay.setText(day);
			// holder.ivCheck.setText(model.getUNTRET_TOT());
			if (model.isCheckFlag()) {
				holder.ivCheck.setImageResource(R.drawable.check_on);
			} else {
				holder.ivCheck.setImageResource(R.drawable.check_off);
			}
		}

		holder.ivCheck.setTag(position);
		holder.llRoot.setTag(position);
	}

	@Override
	protected View newView(Context context, int position, ViewGroup viewgroup) {
		View rootView = mInflater.inflate(R.layout.result_send_row, viewgroup,
				false);

		ResultSendHolder holder = new ResultSendHolder();
		holder.tvDay = (TextView) rootView.findViewById(R.id.tv_day);
		holder.tvCarnum = (TextView) rootView.findViewById(R.id.tv_carnum);
		holder.tvName = (TextView) rootView.findViewById(R.id.tv_name);
		holder.tvCount = (TextView) rootView.findViewById(R.id.tv_count);
		holder.ivCheck = (ImageView) rootView.findViewById(R.id.iv_check);
		holder.llRoot = (LinearLayout) rootView.findViewById(R.id.ll_root);
		holder.ivCheck.setOnClickListener(this);
		holder.llRoot.setOnClickListener(this);

		rootView.setTag(holder);

		return rootView;
	}

	public class ResultSendHolder {

		TextView tvDay;
		TextView tvCarnum;
		TextView tvName;
		TextView tvCount;
		ImageView ivCheck;
		LinearLayout llRoot;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.iv_check:
			clickCheck2(v);
			break;
		case R.id.ll_root:
			clickCheck2(v);
			break;

		default:
			break;
		}
	}

	private void clickCheck2(View v) {
		final int position = (Integer) v.getTag();
		// checkItem(position);

		ResultSendModel model = null;

		model = getItem(position);

		if (model != null) {
			if (model.isCheckFlag()) {
				model.setCheckFlag(false);
			} else {
				model.setCheckFlag(true);
			}
			notifyDataSetChanged();
		}
	}
}
