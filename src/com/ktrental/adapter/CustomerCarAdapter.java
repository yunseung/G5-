package com.ktrental.adapter;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.ktrental.R;
import com.ktrental.fragment.MaintenanceResultFragment;
import com.ktrental.model.BaseMaintenanceModel;
import com.ktrental.popup.CallSendPopup;
import com.ktrental.popup.EventPopupC;
import com.ktrental.popup.SMSPopup;
import com.ktrental.util.kog;
import com.ktrental.viewholder.SelectedViewHolder;

/**
 * 순회정비 결과등록 시작시 왼쪽에 보이는 리스트에 보여주는 Adapter 이다. </br>
 * 
 * 
 * @author hongsungil
 */
public class CustomerCarAdapter extends BaseSelectAdapter<BaseMaintenanceModel>
		implements OnClickListener {

	private MaintenanceResultFragment mResultFragment;

	public CustomerCarAdapter(Context context, ListView listView,
			MaintenanceResultFragment fragment) {
		super(context, listView);
		mSelectedPosition = 0;
		mBackPosition = mSelectedPosition;
		mResultFragment = fragment;
	}

	@Override
	protected void bindView(View rootView, Context context, int position) {

		CarInfoViewHolder carInfoViewHolder = (CarInfoViewHolder) rootView
				.getTag();

		BaseMaintenanceModel model = getItem(position);

		if (model != null) {

			String carNum = model.getCarNum();
			String name = model.getCUSTOMER_NAME();
			

			if (carNum == null)
				carNum = "";
			if (name == null)
				name = "";

			carInfoViewHolder.TvCarnum.setText(carNum);

			carInfoViewHolder.TvName.setText(name);
		}

		carInfoViewHolder.btnCall.setTag(position);
		carInfoViewHolder.btnSms.setTag(position);
		super.bindView(rootView, context, position);
	}

	@Override
	protected View newView(Context context, int position, ViewGroup viewgroup) {

		View rootView = mInflater.inflate(R.layout.maintenance_carinfo_row,
				viewgroup, false);

		CarInfoViewHolder carInfoViewHolder = new CarInfoViewHolder(position);

		carInfoViewHolder.TvCarnum = (TextView) rootView
				.findViewById(R.id.tv_carnum);
		carInfoViewHolder.TvName = (TextView) rootView
				.findViewById(R.id.tv_name);

		carInfoViewHolder.btnCall = (Button) rootView
				.findViewById(R.id.btn_call);
		carInfoViewHolder.btnSms = (Button) rootView.findViewById(R.id.btn_sms);

		rootView.findViewById(R.id.btn_call).setOnClickListener(this);
		rootView.findViewById(R.id.btn_sms).setOnClickListener(this);

		rootView.setTag(carInfoViewHolder);

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

	public void setCustomerArray(ArrayList<BaseMaintenanceModel> arrayList) {
		setData(arrayList);
	}

	public class CarInfoViewHolder extends SelectedViewHolder {

		TextView TvCarnum;
		TextView TvName;
		public Button btnCall;
		public Button btnSms;

		public CarInfoViewHolder(int _position) {
			super(_position);
			// TODO Auto-generated constructor stub
		}

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_call:
			clickCall(v);
			break;
		case R.id.btn_sms:
			clickSms(v);
			break;
		case R.id.rl_root:
			if (mSeletedView != null) {

				int seletedId = mSeletedView.getId();

				if (seletedId == v.getId()) {

					if (v.getTag() != null) {
						if (v.getTag() instanceof SelectedViewHolder) {

							SelectedViewHolder selectedModel = (SelectedViewHolder) v
									.getTag();
							// if
							// (mResultFragment.isResult(getItem(selectedModel
							// .getPosition()))) {
							if (mResultFragment.isResult()) {
								selectedItem(selectedModel.getPosition());
							} else {
								userCheckMove(getItem(mBackPosition),
										selectedModel.getPosition());

							}

						}
					}
				}

			}
			break;
		}

		// super.onClick(v);
	}

	private void selectedItem(int position) {
		checkItem(position);
		if (mBackPosition != mSelectedPosition) {
			if (mOnSeletedItem != null) {

				mOnSeletedItem.OnSeletedItem(getItem(position));
			}
			mBackPosition = mSelectedPosition;
			// }
		}
	}

	private void clickCall(View v) {
		int position = (Integer) v.getTag();
		BaseMaintenanceModel model = getItem(position);
		// CommonUtil.callAction(mContext, model.getTel());
		//myung 20131217 ADD SMS 발송 시 고객 전화번호가 없을 경우 메시지 출력 후 리턴 시킴.
		if ((model.getTel().equals("") || model.getTel().equals(" "))
				&& (model.getDrv_mob().equals("") || model.getDrv_mob().equals(
						" "))) {
			EventPopupC epc = new EventPopupC(mContext);
			epc.show("연락처가 없습니다. \n고객 연락처를 확인하여 주십시요.");
			return;
		}
		CallSendPopup callSendPopup = new CallSendPopup(mContext, model);
		callSendPopup.show();
	}

	private void clickSms(View v) {
		int position = (Integer) v.getTag();
		BaseMaintenanceModel model = getItem(position);
		//myung 20131217 ADD SMS 발송 시 고객 전화번호가 없을 경우 메시지 출력 후 리턴 시킴.
		if (model.getTel().equals("") || model.getTel().equals(" ")) {
			EventPopupC epc = new EventPopupC(mContext);
			epc.show("연락처가 없습니다. \n고객 연락처를 확인하여 주십시요.");
			return;
		}
		
		if(!model.getTel().substring(0, 2).equals("01")){
			EventPopupC epc = new EventPopupC(mContext);
			epc.show("SMS를 발신할 수 없는 전화번호 입니다.");
			return;
		}
		SMSPopup popup = new SMSPopup(mContext, model);
		popup.show();
	}

	public void checkItem(int position) {
		mSelectedPosition = position;

		int last = mListView.getLastVisiblePosition();

		int first = mListView.getFirstVisiblePosition();

		if (position >= last || first <= position) {
			mListView.smoothScrollToPosition(position);
		}

		notifyDataSetChanged();
	}

	public void userCheckMove(final Object item, final int selectedPosition) {
		final String carnum = ((BaseMaintenanceModel) item).getCarNum();
		Builder builder = new Builder(mContext);
		builder.setTitle(carnum + " 점검대상 점검결과가 저장되지 않았습니다. 이동하시겠습니까?");
		builder.setPositiveButton("예", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				mResultFragment.userCheckResult(carnum);
				selectedItem(selectedPosition);
			}
		});
		builder.setNegativeButton("아니요", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
			}
		});

		builder.show();
	}
}
