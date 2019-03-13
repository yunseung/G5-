package com.ktrental.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.ktrental.R;
import com.ktrental.model.MaintenanceGroupModel;
import com.ktrental.model.MaintenanceItemModel;
import com.ktrental.popup.BaseTextPopup;
import com.ktrental.popup.BaseTextPopup.OnSelectedPopupItem;

/**
 * 정비항목화면에서 전구항목을 입력시 인렵된 리스트에 보여주는 Adapter 이다. </br>
 * 
 * 
 * @author hongsungil
 */
public class ElectricAdapter extends BaseCommonAdapter<MaintenanceItemModel>
		implements OnClickListener {

	private ArrayList<MaintenanceGroupModel> mMaintenanceGroupModels = new ArrayList<MaintenanceGroupModel>();
	private ArrayList<String> mGroupModels = new ArrayList<String>();

	private View mPopView;

	public ElectricAdapter(Context context,
			ArrayList<MaintenanceGroupModel> mMaintenanceGroupModels,
			View popView) {
		super(context);
		mPopView = popView;
		this.mMaintenanceGroupModels = mMaintenanceGroupModels;
		for (MaintenanceGroupModel maintenanceGroupModel : mMaintenanceGroupModels) {
			mGroupModels.add(maintenanceGroupModel.getName());
		}
	}

	@Override
	protected void bindView(View rootView, Context context, int position) {
		// TODO Auto-generated method stub
		ElectricViewHolder holder = (ElectricViewHolder) rootView.getTag();
		MaintenanceItemModel model = getItem(position);

		if (model != null) {

			holder.tvItemName.setText(model.getName());
			holder.tvItemNum.setText("" + model.getConsumption());

		}
		holder.btnGroupName.setTag(position);
	}

	@Override
	protected View newView(Context context, int position, ViewGroup viewgroup) {
		View rootView = mInflater.inflate(R.layout.electric_row, viewgroup,
				false);

		ElectricViewHolder holder = new ElectricViewHolder();

		holder.tvItemName = (TextView) rootView.findViewById(R.id.tv_item_name);
		holder.tvItemNum = (TextView) rootView.findViewById(R.id.tv_item_num);
		holder.btnGroupName = (Button) rootView
				.findViewById(R.id.btn_group_name);

		holder.btnGroupName.setOnClickListener(this);

		rootView.setTag(holder);

		return rootView;
	}

	private class ElectricViewHolder {
		TextView tvItemName;
		TextView tvItemNum;
		Button btnGroupName;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btn_group_name:
			clickGroupName(v);
			break;

		default:
			break;
		}
	}

	private void clickGroupName(final View v) {
		final int position = (Integer) v.getTag();
		// checkItem(position);

		MaintenanceItemModel model = null;

		model = getItem(position);

		if (model != null) {
			BaseTextPopup popup = new BaseTextPopup(mContext, mGroupModels, "",
					R.layout.text_array_popup_shot_layout);
			popup.setOnSelectedItem(new OnSelectedPopupItem() {

				@Override
				public void onSelectedItem(int pos, String popName) {

					MaintenanceGroupModel groupModel = mMaintenanceGroupModels
							.get(pos);

					String groupName = groupModel.getName();

					MaintenanceItemModel itemModel = null;

					itemModel = getItem(position);
					itemModel.setGRP_CD(groupModel.getName_key());
					Button btn = (Button) v;
					btn.setText(groupName);
				}
			});

			popup.show(v, mPopView.getMeasuredWidth(),
					mPopView.getMeasuredHeight());
		}
	}
}
