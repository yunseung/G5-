package com.ktrental.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ktrental.R;
import com.ktrental.model.MaintenanceDetailModel;
import com.ktrental.util.CommonUtil;
/**
 * </br> 최종정비이력정보를 선택시 최종정비상세화면에  리스트에 보여주는 Adapter 이다. </br>
 * 
 * 
 * @author hongsungil
 */
public class MaintenanceDetailAdapter extends
		BaseCommonAdapter<MaintenanceDetailModel> {

	public MaintenanceDetailAdapter(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void bindView(View rootView, Context context, int position) {
		// TODO Auto-generated method stub
		MaintenanceDetailViewHolder holder = (MaintenanceDetailViewHolder) rootView
				.getTag();
		MaintenanceDetailModel detailModel = getItem(position);

		if (detailModel != null) {

			holder.tvCategory.setText(CommonUtil.setDotDate(detailModel
					.getCategory()));
			holder.tvDetail.setText(detailModel.getCategoryDetail());
			holder.tvNum.setText(detailModel.getNum());
			holder.tvAction.setText(detailModel.getAction());

		}
	}

	@Override
	protected View newView(Context context, int position, ViewGroup viewgroup) {
		View rootView = mInflater
				.inflate(R.layout.detail_row, viewgroup, false);

		MaintenanceDetailViewHolder holder = new MaintenanceDetailViewHolder();

		holder.tvCategory = (TextView) rootView.findViewById(R.id.tv_category);
		holder.tvDetail = (TextView) rootView.findViewById(R.id.tv_detail);
		holder.tvNum = (TextView) rootView.findViewById(R.id.tv_num);
		holder.tvAction = (TextView) rootView.findViewById(R.id.tv_action);

		rootView.setTag(holder);

		return rootView;
	}

	private class MaintenanceDetailViewHolder {
		TextView tvCategory;
		TextView tvDetail;
		TextView tvNum;
		TextView tvAction;
	}

}
