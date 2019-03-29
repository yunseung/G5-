package com.ktrental.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ktrental.R;
import com.ktrental.common.DEFINE;
import com.ktrental.fragment.MaintenanceResultFragment;
import com.ktrental.model.MaintenanceItemModel;

/**
 * </br>
 * 정비항목입력에 보여질 선택 정비항목 리스트를 보여주는 Adapter 이다. </br>
 * 
 * @author hongsungil
 */
public class MaintenancLastItemAdapter extends BaseCommonAdapter<MaintenanceItemModel> implements View.OnClickListener {

	private ArrayList<MaintenanceItemModel> selectedMaintenanceModels = new ArrayList<MaintenanceItemModel>();

	private int mType;
	boolean bSetItem = false;
	public final static int INPUT = 0;
	public final static int RESIST = 1;
	private String mGubun = "";

	public MaintenancLastItemAdapter(Context context, int type, String gubun) {
		super(context);
		mType = type;
		mGubun = gubun;
	}

	@Override
	protected void bindView(View rootView, Context context, int position) {
		// TODO Auto-generated method stub
		LastItemViewHolder lastItemViewHolder = (LastItemViewHolder) rootView.getTag();
		MaintenanceItemModel model = getItem(position);

		if (model != null) {

			if (model.isCheck()) {
				rootView.setBackgroundResource(R.drawable.table_list_s);
				lastItemViewHolder.ivCheck.setImageResource(R.drawable.check_on);

			} else {
				rootView.setBackgroundResource(R.drawable.table_list_n);
				lastItemViewHolder.ivCheck.setImageResource(R.drawable.check_off);
			}

			lastItemViewHolder.tvConsumption.setText("" + model.getConsumption());
			if (model.getMaintenanceGroupModel() != null) {
				lastItemViewHolder.tvGroupName.setText("" + model.getMaintenanceGroupModel().getName());
			}

			lastItemViewHolder.tvLastPrice.setText("" + model.getNETPR());

			int stock = model.getStock();
			int consumption = model.getConsumption();

			// myung 20131226 UPDATE
			// stock = stock - consumption - model.getTotalConsumption();
			if (bSetItem)
				stock = stock - MaintenanceResultFragment.getToalConsumption(model.getMATNR()) - consumption;
			else
				stock = stock - MaintenanceResultFragment.getToalConsumption(model.getMATNR());

			lastItemViewHolder.tvStock.setText("" + stock);

			lastItemViewHolder.tvItemName.setText(model.getName());

			if (selectedMaintenanceModels.size() - 1 == position) {
				bSetItem = false;
			}
		}
		lastItemViewHolder.ivCheck.setTag(position);
		// lastItemViewHolder.rlRoot.setTag(position);
	}

	@Override
	protected View newView(Context context, int position, ViewGroup viewgroup) {
		View rootView = mInflater.inflate(R.layout.last_item_row, viewgroup, false);

		LastItemViewHolder lastItemViewHolder = new LastItemViewHolder();

		lastItemViewHolder.rlRoot = (LinearLayout) rootView.findViewById(R.id.rl_root);

		// myung 20131120 2560 대응
		// int tempY = 50;
		// if(DEFINE.DISPLAY.equals("2560")){
		// tempY *= 2;
		// }
		// rootView.setLayoutParams(new AbsListView.LayoutParams(
		// LayoutParams.MATCH_PARENT, tempY));
		lastItemViewHolder.ivCheck = (ImageView) rootView.findViewById(R.id.iv_last_check);

		lastItemViewHolder.tvGroupName = (TextView) rootView.findViewById(R.id.tv_last_group_name);

		lastItemViewHolder.tvItemName = (TextView) rootView.findViewById(R.id.tv_last_item_name);

		lastItemViewHolder.tvConsumption = (TextView) rootView.findViewById(R.id.tv_last_consumption);

		lastItemViewHolder.tvLastPrice = (TextView) rootView.findViewById(R.id.tv_last_price);

		lastItemViewHolder.tvStock = (TextView) rootView.findViewById(R.id.tv_last_stock);

		lastItemViewHolder.rlRoot.setOnClickListener(this);
		lastItemViewHolder.ivCheck.setOnClickListener(this);

		if (mType == INPUT) {
			LinearLayout.LayoutParams _params = (LinearLayout.LayoutParams) lastItemViewHolder.tvConsumption
					.getLayoutParams();
//			_params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
			lastItemViewHolder.tvStock.setVisibility(View.GONE);
			lastItemViewHolder.tvConsumption.setLayoutParams(_params);
			
			((LinearLayout.LayoutParams)lastItemViewHolder.tvItemName.getLayoutParams()).weight = 6;
			((LinearLayout.LayoutParams)lastItemViewHolder.tvStock.getLayoutParams()).weight = 0;
		} else {
			lastItemViewHolder.tvStock.setVisibility(View.VISIBLE);
			
			((LinearLayout.LayoutParams)lastItemViewHolder.tvItemName.getLayoutParams()).weight = 5;
			((LinearLayout.LayoutParams)lastItemViewHolder.tvStock.getLayoutParams()).weight = 1;
		}

		if (mGubun.equals("A")) {
			lastItemViewHolder.tvLastPrice.setVisibility(View.VISIBLE);
		} else {
			lastItemViewHolder.tvLastPrice.setVisibility(View.GONE);
		}

		rootView.setTag(lastItemViewHolder);

		// Log.d("",
		// "lastItemViewHolder.ivCheck = "
		// + lastItemViewHolder.ivCheck.getId());
		// Log.d("",
		// "lastItemViewHolder.rlRoot = "
		// + lastItemViewHolder.rlRoot.getId());

		return rootView;
	}

	private class LastItemViewHolder {
		public LinearLayout rlRoot;
		public ImageView ivCheck;
		public TextView tvConsumption;
		public TextView tvGroupName;
		public TextView tvItemName;
		public TextView tvStock;
		public TextView tvLastPrice;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.iv_last_check:
			clickCheck(v);
			break;
		case R.id.rl_root:
			clickRoot(v);
			break;

		default:
			break;
		}
	}

	private void clickCheck(View v) {
		int position = (Integer) v.getTag();
		checkItem(position);
	}

	private void clickRoot(View v) {
		View view = v.findViewById(R.id.iv_last_check);
		clickCheck(view);
	}

	private void checkItem(int position) {

		MaintenanceItemModel model = getItem(position);

		if (model != null) {
			if (model.isCheck()) {
				if (selectedMaintenanceModels.remove(model))
					model.setCheck(false);
			} else {
				selectedMaintenanceModels.add(model);
				model.setCheck(true);

			}

			notifyDataSetChanged();
		}
	}

	public ArrayList<MaintenanceItemModel> getSelectedMaintenanceModels() {
		return selectedMaintenanceModels;
	}

	public void setSelectedMaintenanceModels(ArrayList<MaintenanceItemModel> selectedMaintenanceModels) {
		this.selectedMaintenanceModels = selectedMaintenanceModels;
	}

	public void initSelectedMaintenanceArray() {
		// for (int i = 0; i < selectedMaintenanceModels.size(); i++) {
		// selectedMaintenanceModels.get(i).setCheck(false);
		// selectedMaintenanceModels.get(i).setSelectcConsumption(
		// selectedMaintenanceModels.get(i).getConsumption());
		// selectedMaintenanceModels.get(i).setConsumption(0);
		// }

		selectedMaintenanceModels = new ArrayList<MaintenanceItemModel>();
		notifyDataSetChanged();
	}

	public void setSetItemFlag(boolean setFlag) {
		bSetItem = setFlag;
	}

}
