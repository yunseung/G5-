package com.ktrental.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ktrental.R;
import com.ktrental.fragment.CheckingCompleteFragment;
import com.ktrental.fragment.MaintenanceResultFragment;
import com.ktrental.model.MaintenanceItemModel;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

/**
 * </br>
 * 정비항목입력에 보여질 선택 정비항목 리스트를 보여주는 Adapter 이다. </br>
 * 
 * @author hongsungil
 */
public class MaintenancCheckingCompleteItemAdapter extends BaseAdapter {

	private List<CheckingCompleteFragment.MaintenanceCheckingCompleteModel> mItemList;
	private Context mContext = null;
	private LastItemViewHolder mViewHolder = null;

	public MaintenancCheckingCompleteItemAdapter(Context context, List<CheckingCompleteFragment.MaintenanceCheckingCompleteModel> list) {
		super();
		mItemList = list;
		mContext = context;
	}

	@Override
	public int getCount() {
		return mItemList.size();
	}

	@Override
	public Object getItem(int position) {
		return mItemList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			mViewHolder = new LastItemViewHolder();
			convertView = LayoutInflater.from(mContext).inflate(R.layout.checking_complete_item_row, null);
			mViewHolder.tvGroupName = (TextView) convertView.findViewById(R.id.tv_group_name);

			mViewHolder.tvPrice = (TextView) convertView.findViewById(R.id.tv_price);

			mViewHolder.tvVat = (TextView) convertView.findViewById(R.id.tv_vat);

			mViewHolder.tvTotalPrice = (TextView) convertView.findViewById(R.id.tv_total_price);

			mViewHolder.tvConsumption = (TextView) convertView.findViewById(R.id.tv_consumption);
			convertView.setTag(mViewHolder);
		} else {
			mViewHolder = (LastItemViewHolder)convertView.getTag();
		}

		if (mItemList.get(position).getGroupName().equals("엔진오일SET")) {
			mViewHolder.tvConsumption.setText("1");
		} else {
			mViewHolder.tvConsumption.setText(mItemList.get(position).getConsumption());
		}
		mViewHolder.tvGroupName.setText(mItemList.get(position).getGroupName());


		String price = mItemList.get(position).getPrice();
		int vat = (int)Math.round((Double.parseDouble(price)) * 0.1);
		int totalPrice = Integer.parseInt(price) + vat;


		mViewHolder.tvPrice.setText(price);
		mViewHolder.tvVat.setText(Integer.toString(vat));
		mViewHolder.tvTotalPrice.setText(Integer.toString(totalPrice));
		return convertView;
	}

	private class LastItemViewHolder {
		public TextView tvPrice;
		public TextView tvGroupName;
		public TextView tvVat;
		public TextView tvTotalPrice;
		public TextView tvConsumption;
	}

}
