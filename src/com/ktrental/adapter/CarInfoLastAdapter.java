package com.ktrental.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ktrental.R;
import com.ktrental.model.MaintenanceLastModel;
import com.ktrental.util.CommonUtil;

/**
 * 순회정비대상인 고객에 최종정비이력정보를 보여주는 Adapter 이다. </br>
 * 
 * 
 * @author hongsungil
 */
public class CarInfoLastAdapter extends BaseCommonAdapter<MaintenanceLastModel> {

	public CarInfoLastAdapter(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void bindView(View rootView, Context context, int position) {
		// TODO Auto-generated method stub
		CarInfoLastViewHolder holder = (CarInfoLastViewHolder) rootView
				.getTag();
		MaintenanceLastModel lastModel = getItem(position);

		if (lastModel != null) {

			holder.tvDay.setText(CommonUtil.setDotDate(lastModel.getDay()));
			holder.tvType.setText(lastModel.getType());
			holder.tvBranch.setText(lastModel.getBranch());
			holder.tvDistance.setText(lastModel.getDistance());

		}
	}

	@Override
	protected View newView(Context context, int position, ViewGroup viewgroup) {
		View rootView = mInflater.inflate(R.layout.carinfo_last_row, viewgroup,
				false);

		CarInfoLastViewHolder holder = new CarInfoLastViewHolder();

		holder.tvDay = (TextView) rootView.findViewById(R.id.tv_last_day);
		holder.tvType = (TextView) rootView.findViewById(R.id.tv_last_type);
		holder.tvBranch = (TextView) rootView.findViewById(R.id.tv_last_branch);
		holder.tvDistance = (TextView) rootView
				.findViewById(R.id.tv_last_distance);

		rootView.setTag(holder);

		return rootView;
	}

	private class CarInfoLastViewHolder {
		TextView tvDay;
		TextView tvType;
		TextView tvBranch;
		TextView tvDistance;
	}

}
