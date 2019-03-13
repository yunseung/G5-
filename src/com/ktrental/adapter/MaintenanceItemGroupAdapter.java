package com.ktrental.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.TextView;

import com.ktrental.R;
import com.ktrental.common.DEFINE;
import com.ktrental.model.MaintenanceGroupModel;
import com.ktrental.viewholder.SelectedViewHolder;

/**
 * </br>
 * 정비항목입력에 보여질 그룹 리스트를 보여주는 Adapter 이다. </br>
 * 
 * @author hongsungil
 */
public class MaintenanceItemGroupAdapter extends BaseSelectAdapter<MaintenanceGroupModel> {

	public MaintenanceItemGroupAdapter(Context context, ListView listView) {
		super(context, listView);
		mSelectedPosition = 0;
		mBackPosition = mSelectedPosition;
	}

	@Override
	protected void bindView(View rootView, Context context, int position) {

		ItemGroupViewHolder groupViewHolder = (ItemGroupViewHolder) rootView.getTag();

		MaintenanceGroupModel model = (MaintenanceGroupModel) getItem(position);

		if (model != null) {
			String groupName = model.getName();

			if (groupName != null) {

				groupViewHolder.tvGroupName.setText(groupName);
			}
		}
		super.bindView(rootView, context, position);
	}

	@Override
	protected View newView(Context context, int position, ViewGroup viewgroup) {

		View rootView = mInflater.inflate(R.layout.maintenance_item_group_row, null);

		ItemGroupViewHolder itemGroupViewHolder = new ItemGroupViewHolder(position);

		itemGroupViewHolder.tvGroupName = (TextView) rootView.findViewById(R.id.tv_group_name);

		// myung 20131120 2560 대응
		// int tempX = 258;
		// int tempY = 46;
		// if(DEFINE.DISPLAY.equals("2560")){
		// tempX *= 2;
		// tempY *= 2;
		// }
		//
		// rootView.setLayoutParams(new AbsListView.LayoutParams(tempX, tempY));

		rootView.setTag(itemGroupViewHolder);

		mSeletedView = rootView;

		super.newView(context, position, (ViewGroup) rootView);

		return rootView;
	}

	@Override
	protected void setSelectedBackground(boolean isSelected, View contentView) {
		// TODO Auto-generated method stub
		if (isSelected) {
			contentView.setBackgroundResource(R.drawable.left_list_bg_s);
		} else {
			contentView.setBackgroundResource(R.drawable.left_list_bg_n);
		}
	}

	private class ItemGroupViewHolder extends SelectedViewHolder {
		TextView tvGroupName;

		public ItemGroupViewHolder(int _position) {
			super(_position);
			// TODO Auto-generated constructor stub
		}

	}

}
