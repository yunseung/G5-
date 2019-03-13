package com.ktrental.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ktrental.R;
import com.ktrental.fragment.MaintenanceResultResistFragment;
import com.ktrental.model.CheckModel;
import com.ktrental.model.MaintenanceItemModel;

/**
 * 순회정비 결과등록 고객확인 화면에 이전 {@link MaintenanceResultResistFragment} 화면에서 선택된 정비항목을
 * 보여주는 Adapter 이다. </br>
 * 
 * 
 * @author hongsungil
 */
public class CheckingAdapter extends BaseCommonAdapter<CheckModel> {

	public CheckingAdapter(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void bindView(View rootView, Context context, int position) {
		// TODO Auto-generated method stub

		CheckViewHolder viewHolder = (CheckViewHolder) rootView.getTag();

		CheckModel checkModel = getItem(position);

		MaintenanceItemModel leftModel = checkModel.getLeftModel();
		MaintenanceItemModel riItemModel = checkModel.getRiItemModel();

		if (leftModel != null) {
			String leftName = leftModel.getMaintenanceGroupModel().getName();
//			if(leftModel.getMaintenanceGroupModel().getName_key() != null && (leftModel.getMaintenanceGroupModel().getName_key().equals("400893") || leftModel.getMaintenanceGroupModel().getName_key().equals("400894"))){
//				leftName = leftModel.getName();
//			}
			viewHolder.tvLeftGroupName.setText(leftName);
			viewHolder.tvLeftNum.setText("" + leftModel.getConsumption() + " EA");

			if (leftModel.getConsumption() > 0) {
				// viewHolder.rbLeftChange.setEnabled(false);
				// viewHolder.rbLeftGood.setEnabled(true);

				viewHolder.rbLeftChange.setChecked(true);
				viewHolder.rbLeftGood.setChecked(false);
				viewHolder.tvLeftChange.setEnabled(true);
				viewHolder.tvLeftGood.setEnabled(false);

			} else {
				// viewHolder.rbLeftChange.setEnabled(true);
				// viewHolder.rbLeftGood.setEnabled(false);

				viewHolder.rbLeftChange.setChecked(false);
				viewHolder.rbLeftGood.setChecked(true);
				viewHolder.tvLeftChange.setEnabled(false);
				viewHolder.tvLeftGood.setEnabled(true);
			}

		} else {

		}
		if (riItemModel == null) {
			viewHolder.llRight.setVisibility(View.INVISIBLE);
		} else {
			viewHolder.llRight.setVisibility(View.VISIBLE);
			String rightName = riItemModel.getMaintenanceGroupModel().getName();
			viewHolder.tvRightGroupName.setText(rightName);
			viewHolder.tvRightNum.setText("" + riItemModel.getConsumption() + " EA");
			if (riItemModel.getConsumption() > 0) {
				// viewHolder.rbRightChange.setEnabled(false);
				// viewHolder.rbRightGood.setEnabled(true);

				viewHolder.rbRightChange.setChecked(true);
				viewHolder.rbRightGood.setChecked(false);
				viewHolder.tvRightChange.setEnabled(true);
				viewHolder.tvRightGood.setEnabled(false);
			} else {
				// viewHolder.rbRightChange.setEnabled(true);
				// viewHolder.rbRightGood.setEnabled(false);

				viewHolder.rbRightChange.setChecked(false);
				viewHolder.rbRightGood.setChecked(true);
				// viewHolder.rbRightChange.setChecked(true);
				// viewHolder.rbRightGood.setChecked(false);
				viewHolder.tvRightChange.setEnabled(false);
				viewHolder.tvRightGood.setEnabled(true);
			}

		}

	}

	@Override
	protected View newView(Context context, int position, ViewGroup viewgroup) {

		View rootView = mInflater.inflate(R.layout.checking_row, viewgroup, false);

		CheckViewHolder viewHolder = new CheckViewHolder();

		viewHolder.tvLeftGroupName = (TextView) rootView.findViewById(R.id.tv_oil_left);

		viewHolder.rbLeftGood = (RadioButton) rootView.findViewById(R.id.rb_left_good);

		viewHolder.rbLeftChange = (RadioButton) rootView.findViewById(R.id.rb_left_change);

		viewHolder.tvLeftNum = (TextView) rootView.findViewById(R.id.tv_left_num);
		viewHolder.tvLeftGood = (TextView) rootView.findViewById(R.id.tv_left_good);
		viewHolder.tvLeftChange = (TextView) rootView.findViewById(R.id.tv_left_change);

		viewHolder.tvRightGroupName = (TextView) rootView.findViewById(R.id.tv_oil_right);

		viewHolder.rbRightGood = (RadioButton) rootView.findViewById(R.id.rb_right_good);

		viewHolder.rbRightChange = (RadioButton) rootView.findViewById(R.id.rb_right_change);

		viewHolder.tvRightNum = (TextView) rootView.findViewById(R.id.tv_right_num);

		viewHolder.tvRightGood = (TextView) rootView.findViewById(R.id.tv_right_good);
		viewHolder.tvRightChange = (TextView) rootView.findViewById(R.id.tv_right_change);

		viewHolder.llRight = (RelativeLayout) rootView.findViewById(R.id.ll_right);

		rootView.setTag(viewHolder);

		return rootView;
	}

	private class CheckViewHolder {
		TextView tvLeftGroupName;
		RadioButton rbLeftGood;
		RadioButton rbLeftChange;
		TextView tvLeftNum;
		TextView tvLeftGood;
		TextView tvLeftChange;

		RelativeLayout llRight;
		TextView tvRightGroupName;
		RadioButton rbRightGood;
		RadioButton rbRightChange;
		TextView tvRightNum;
		TextView tvRightGood;
		TextView tvRightChange;
	}

	public void initArray() {
		mItemArray.clear();
	}
}
