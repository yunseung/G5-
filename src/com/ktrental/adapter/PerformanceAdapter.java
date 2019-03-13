package com.ktrental.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ktrental.R;
import com.ktrental.model.PerformanceModel;
/**
 * </br> 실적화면에 상세 정보 리스트를 보여주는 Adapter 이다. </br>
 * 
 * @author hongsungil
 */
public class PerformanceAdapter extends BaseCommonAdapter<PerformanceModel> {

	public PerformanceAdapter(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void bindView(View rootView, Context context, int position) {
		// TODO Auto-generated method stub
		PerpomanceHolder holder = (PerpomanceHolder) rootView.getTag();

		PerformanceModel model = getItem(position);

		if (holder != null) {
			holder.TvZGUBUN.setText(model.getZGUBUN());
			holder.TvPROC_PCT.setText(model.getPROC_PCT());
			holder.TvPROC_CNT.setText(model.getPROC_CNT());
			holder.TvPLAN_TOT.setText(model.getPLAN_TOT());
			holder.TvUNTRET_TOT.setText(model.getUNTRET_TOT());
		}
	}

	@Override
	protected View newView(Context context, int position, ViewGroup viewgroup) {
		View rootView = mInflater.inflate(R.layout.perpomance_row, viewgroup,
				false);

		PerpomanceHolder holder = new PerpomanceHolder();
		holder.TvZGUBUN = (TextView) rootView.findViewById(R.id.tv_zgubun);
		holder.TvPROC_PCT = (TextView) rootView.findViewById(R.id.tv_PROC_PCT);
		holder.TvPROC_CNT = (TextView) rootView.findViewById(R.id.tv_PROC_CNT);
		holder.TvPLAN_TOT = (TextView) rootView.findViewById(R.id.tv_PLAN_TOT);
		holder.TvUNTRET_TOT = (TextView) rootView
				.findViewById(R.id.tv_UNTRET_TOT);

		rootView.setTag(holder);

		return rootView;
	}

	public class PerpomanceHolder {

		TextView TvZGUBUN;
		TextView TvPROC_PCT;
		TextView TvPLAN_TOT;
		TextView TvPROC_CNT;
		TextView TvUNTRET_TOT;

	}
}
