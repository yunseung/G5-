package com.ktrental.adapter;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.ktrental.R;
import com.ktrental.model.MaintenanceActiclesModel;
import com.ktrental.util.CommonUtil;

/**
 * 이관등록에 보여지는 오른쪽 리스트이다.</br> 자신을 제외한 순회기사 정보를 리스트에 보여주는 Adapter 이다. </br>
 * 
 * 
 * @author hongsungil
 */
public class MaintenanceArticlesAdapter extends
		BaseCommonAdapter<MaintenanceActiclesModel> implements OnClickListener {

	private int mSelectedPosition = -1;

	public MaintenanceArticlesAdapter(Context context, ListView lv) {
		super(context);
		// lv.setOnItemClickListener(this);
		// TODO Auto-generated constructor stub
	}

	private class MaintenaceAticleViewHolder {
		TextView tvCarNumber;
		TextView tvName;
		TextView tvPhoneNumber;
		ImageView ivSelect;
		LinearLayout llRoot;
		Button btnCall;
	}

	@Override
	protected void bindView(View rootView, Context context, int position) {
		// TODO Auto-generated method stub
		MaintenaceAticleViewHolder holder = (MaintenaceAticleViewHolder) rootView
				.getTag();

		MaintenanceActiclesModel model = (MaintenanceActiclesModel) mItemArray
				.get(position);

		if (model != null) {
			holder.tvCarNumber.setText(model.getInvnr());
			holder.tvName.setText(model.getEname());
			holder.tvPhoneNumber.setText(model.getHpphon());
			if (mSelectedPosition == position) {
				holder.ivSelect.setBackgroundResource(R.drawable.radio_on);
				holder.llRoot.setBackgroundResource(R.drawable.table_list_s);
			} else {
				holder.ivSelect.setBackgroundResource(R.drawable.radio_off);
				holder.llRoot.setBackgroundResource(R.drawable.table_list_n);
			}
		}
		holder.ivSelect.setTag(position);
		holder.llRoot.setTag(position);
		holder.btnCall.setTag(position);
	}

	@Override
	protected View newView(Context context, int position, ViewGroup viewgroup) {
		ViewGroup root = (ViewGroup) mInflater.inflate(
				R.layout.maintenancearticles_row, viewgroup, false);

		MaintenaceAticleViewHolder holder = new MaintenaceAticleViewHolder();

		holder.tvCarNumber = (TextView) root.findViewById(R.id.tv_carnum);
		holder.tvName = (TextView) root.findViewById(R.id.tv_name);
		holder.tvPhoneNumber = (TextView) root.findViewById(R.id.tv_phonenum);
		holder.ivSelect = (ImageView) root.findViewById(R.id.iv_select);
		holder.llRoot = (LinearLayout) root.findViewById(R.id.ll_root);

		holder.btnCall = (Button) root.findViewById(R.id.btn_call);

		holder.ivSelect.setOnClickListener(this);
		holder.llRoot.setOnClickListener(this);
		holder.btnCall.setOnClickListener(this);

		root.setTag(holder);
		return root;
	}

	@Override
	public void setData(List<MaintenanceActiclesModel> datas) {
		super.setData(datas);
		notifyDataSetChanged();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.iv_select:
			selectedItem(v);
			break;
		case R.id.ll_root:
			selectedItem(v);
			break;
		case R.id.btn_call:
			clickCall(v);
			break;

		default:
			break;
		}
	}

	private void selectedItem(View v) {
		int position = (Integer) v.getTag();
		mSelectedPosition = position;
		notifyDataSetChanged();
	}

	private void clickCall(View v) {
		int position = (Integer) v.getTag();
		String phoneNum = getItem(position).getHpphon();
		CommonUtil.callAction(mContext, phoneNum);
	}

	public MaintenanceActiclesModel getSelectedMaster() {

		MaintenanceActiclesModel model = getItem(mSelectedPosition);

		return model;
	}
}
