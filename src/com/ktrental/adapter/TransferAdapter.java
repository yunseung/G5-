package com.ktrental.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ktrental.R;
import com.ktrental.model.BaseMaintenanceModel;

/**
 * 이관등록에 보여지는 왼쪽 리스트이다.</br> 이전화면에서 선택된 고객에 정보를 리스트에 보여주는 Adapter 이다. </br>
 * 
 * 
 * @author hongsungil
 */
public class TransferAdapter extends BaseCommonAdapter<BaseMaintenanceModel>
		implements OnClickListener {

	private ArrayList<BaseMaintenanceModel> selectedMaintenanceModels = new ArrayList<BaseMaintenanceModel>();

	public TransferAdapter(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		
	}

	@Override
	protected void bindView(View rootView, Context context, int position) {
		// TODO Auto-generated method stub
		TransferHolder holder = (TransferHolder) rootView.getTag();
		BaseMaintenanceModel model = getItem(position);

		if (model != null) {

			holder.tvName.setText(model.getCUSTOMER_NAME());
			holder.tvCarNum.setText(model.getCarNum());
//			Log.e("isCheck()",""+model.isCheck());
			if (model.isCheck()) {
				// holder.llRoot.setBackgroundResource(R.drawable.table_list_s);
				holder.ivCheck.setImageResource(R.drawable.check_on);

			} else {

				// holder.llRoot.setBackgroundResource(R.drawable.table_list_n);
				holder.ivCheck.setImageResource(R.drawable.check_off);

			}
		}

		holder.ivCheck.setTag(position);
		holder.llRoot.setTag(position);

	}

	@Override
	protected View newView(Context context, int position, ViewGroup viewgroup) {

		View rootView = mInflater.inflate(R.layout.transfer_row, viewgroup,
				false);

		TransferHolder holder = new TransferHolder();
		holder.llRoot = (LinearLayout) rootView.findViewById(R.id.ll_root);
		holder.ivCheck = (ImageView) rootView.findViewById(R.id.iv_check);
		holder.tvName = (TextView) rootView.findViewById(R.id.tv_name);
		holder.tvCarNum = (TextView) rootView.findViewById(R.id.tv_carnum);

		holder.llRoot.setOnClickListener(this);
		holder.ivCheck.setOnClickListener(this);

		rootView.setTag(holder);

		return rootView;
	}

	public class TransferHolder {
		public LinearLayout llRoot;
		public ImageView ivCheck;
		public TextView tvName;
		public TextView tvCarNum;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.iv_check:
			clickCheck(v);
			break;
		case R.id.ll_root:
			clickCheck(v);
			break;

		default:
			break;
		}
	}

	private void clickCheck(View v) {
		int position = (Integer) v.getTag();

		BaseMaintenanceModel model = getItem(position);

		if (model != null) {

			if (model.isCheck()) {

				model.setCheck(false);
				selectedMaintenanceModels.remove(model);

			} else {
				model.setCheck(true);
				if (selectedMaintenanceModels.contains(model)) {
					selectedMaintenanceModels.remove(model);
				}
				selectedMaintenanceModels.add(model);
			}
		}

		notifyDataSetChanged();
	}

	public ArrayList<BaseMaintenanceModel> getSelectedMaintenanceModels() {
		return selectedMaintenanceModels;
	}

	public void setSelectedMaintenanceModels(
			ArrayList<BaseMaintenanceModel> selectedMaintenanceModels) {
		this.selectedMaintenanceModels = selectedMaintenanceModels;
	}

	@Override
	public void setData(List<BaseMaintenanceModel> datas) {

		for (BaseMaintenanceModel model : datas) {
			if (selectedMaintenanceModels.contains(model)) {
				selectedMaintenanceModels.remove(model);
			}
			selectedMaintenanceModels.add(model);
		}

		super.setData(datas);
	}

	public void initSelectedArray() {
		selectedMaintenanceModels = new ArrayList<BaseMaintenanceModel>();
	}

}
