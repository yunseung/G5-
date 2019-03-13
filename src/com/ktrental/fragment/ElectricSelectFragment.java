package com.ktrental.fragment;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.ktrental.R;
import com.ktrental.adapter.ElectricAdapter;
import com.ktrental.cm.connect.ConnectController;
import com.ktrental.cm.db.DbAsyncTask;
import com.ktrental.cm.db.DbAsyncTask.DbAsyncResLintener;
import com.ktrental.model.DbQueryModel;
import com.ktrental.model.MaintenanceGroupModel;
import com.ktrental.model.MaintenanceItemModel;

import java.util.ArrayList;

public class ElectricSelectFragment extends BaseFragment implements
		OnClickListener, DbAsyncResLintener {

	private ArrayList<MaintenanceItemModel> mItemModels = new ArrayList<MaintenanceItemModel>();
	private ArrayList<MaintenanceGroupModel> mMaintenanceGroupModels = new ArrayList<MaintenanceGroupModel>();

	private ListView mLvElectric;
	private ElectricAdapter mElectricAdapter;

	public ElectricSelectFragment(){}

	public ElectricSelectFragment(ArrayList<MaintenanceItemModel> array) {
		mItemModels = array;
		queryGroup();
	}

	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		getDialog().setCanceledOnTouchOutside(false);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		mRootView = (ViewGroup) inflater.inflate(
				R.layout.electric_select_layout, null);

		mRootView.findViewById(R.id.iv_exit).setVisibility(View.GONE);
		TextView tvTitle = (TextView) mRootView
				.findViewById(R.id.tv_dialog_title);
		tvTitle.setText("전구류 정비항목 그룹 입력");

		mRootView.findViewById(R.id.btn_save).setOnClickListener(this);
		mLvElectric = (ListView) mRootView.findViewById(R.id.lv_electric);

		return mRootView;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btn_save:
			clickSave();
			break;

		default:
			break;
		}
	}

	private void clickSave() {
		for (MaintenanceItemModel itemModel : mItemModels) {
			if (itemModel.getGRP_CD().equals(" ")) {
				showEventPopup2(null, "전구류 장비항목 그룹을 필수로 입력해주세요.");
				return;
			}
		}
		dismiss();
	}

	private void queryGroup() {
		showProgress();
		String[] _whereArgs = { "PM023", "407", "408", "409", "410", "411" };
		String[] _whereCause = { "ZCODEH", "ZCODEV", "ZCODEV", "ZCODEV",
				"ZCODEV", "ZCODEV" };

		String[] colums = { "ZCODEVT", "ZCODEV" };

		DbQueryModel dbQueryModel = new DbQueryModel(
				ConnectController.O_ITAB1_TABLE_NAME, _whereCause, _whereArgs,
				colums);

		DbAsyncTask dbAsyncTask = new DbAsyncTask("queryGroup", mContext, this,
				dbQueryModel);
		dbAsyncTask.execute(DbAsyncTask.DB_SELECT);
	}

	@Override
	public void onCompleteDB(String funName, int type, Cursor cursor,
			String tableName) {
		// TODO Auto-generated method stub
		if (funName.equals("queryGroup")) {
			if (cursor == null)
				return;
			responseGroup(cursor);

		}
	}

	private void responseGroup(Cursor cursor) {
		cursor.moveToFirst();

		MaintenanceGroupModel maintenanceGroupModel = null;

		while (!cursor.isAfterLast()) {

			maintenanceGroupModel = new MaintenanceGroupModel(
					cursor.getString(0), cursor.getString(1));
			mMaintenanceGroupModels.add(maintenanceGroupModel);
			cursor.moveToNext();

		}
		cursor.close();

		mElectricAdapter = new ElectricAdapter(mContext,
				mMaintenanceGroupModels, mRootView);
		mElectricAdapter.setData(mItemModels);
		mLvElectric.setAdapter(mElectricAdapter);
	}

	@Override
	public void dismiss() {
		super.dismiss();

		FragmentTransaction transaction = getFragmentManager()
				.beginTransaction();
		transaction.detach(this);
		transaction.commit();
		getChildFragmentManager().executePendingTransactions();
		onDetach();
		onDestroy();
	}
}
