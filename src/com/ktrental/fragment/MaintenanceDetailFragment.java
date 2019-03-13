package com.ktrental.fragment;

import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.ktrental.R;
import com.ktrental.adapter.MaintenanceDetailAdapter;
import com.ktrental.cm.db.DbAsyncTask;
import com.ktrental.cm.db.DbAsyncTask.DbAsyncResLintener;
import com.ktrental.common.DEFINE;
import com.ktrental.model.CarInfoModel;
import com.ktrental.model.DbQueryModel;
import com.ktrental.model.MaintenanceDetailModel;
import com.ktrental.model.MaintenanceLastModel;
import com.ktrental.util.CommonUtil;

import java.util.ArrayList;

public class MaintenanceDetailFragment extends BaseFragment implements
		OnClickListener, DbAsyncResLintener {

	private MaintenanceLastModel mLastModel;

	private ArrayList<MaintenanceDetailModel> mDetailModels = new ArrayList<MaintenanceDetailModel>();

	private ListView mLvDetail;
	private MaintenanceDetailAdapter mMaintenanceDetailAdapter;

	private TextView mTvCarNum;
	private TextView mTvCarName;
	private TextView mTvName;
	private TextView mTvDay;
	private TextView mTvType;
	private TextView mTvDistance;

	/**
	 * 선택된 차량 보여줘야 되는 총 정보
	 */
	private CarInfoModel mCarInfoModel;

	public MaintenanceDetailFragment(){}

	public MaintenanceDetailFragment(MaintenanceLastModel model,
			CarInfoModel aCarInfoModel) {
		mLastModel = model;
		mCarInfoModel = aCarInfoModel;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub

		mRootView = inflater.inflate(R.layout.maintenace_detail_layout, null);

		mLvDetail = (ListView) mRootView.findViewById(R.id.lv_detail);

		initTextView();

		mMaintenanceDetailAdapter = new MaintenanceDetailAdapter(mContext);

		mLvDetail.setAdapter(mMaintenanceDetailAdapter);

		mRootView.findViewById(R.id.iv_exit).setOnClickListener(this);
		
		
		queryLastDetail(mLastModel.getKey());

		return mRootView;
	}

	private void initTextView() {
		mTvCarNum = (TextView) mRootView.findViewById(R.id.tv_carnum);
		mTvCarName = (TextView) mRootView.findViewById(R.id.tv_carname);
		mTvName = (TextView) mRootView.findViewById(R.id.tv_name);
		mTvDay = (TextView) mRootView.findViewById(R.id.tv_day);
		mTvType = (TextView) mRootView.findViewById(R.id.tv_type);
		mTvDistance = (TextView) mRootView.findViewById(R.id.tv_distance);

		mTvCarNum.setText(mLastModel.getCarNum());
		mTvCarName.setText(mCarInfoModel.getCarname());
		mTvName.setText(mCarInfoModel.getCUSTOMER_NAME());
		mTvDay.setText(CommonUtil.setDotDate(mLastModel.getDay()));
		mTvType.setText(mLastModel.getType());
		mTvDistance.setText(mCarInfoModel.getLastMileage());
	}

	private void queryLastDetail(String key) {

		// if (!this.isHidden()) {

		showProgress();

		String[] _whereArgs = { key };
		String[] _whereCause = { DEFINE.AUFNR };

		String[] colums = null;

		DbQueryModel dbQueryModel = new DbQueryModel(
				DEFINE.REPAIR_LAST_DETAIL_TABLE_NAME, _whereCause, _whereArgs,
				colums);

		DbAsyncTask dbAsyncTask = new DbAsyncTask("queryLastDetail", mContext,
				this, dbQueryModel);

		dbAsyncTask.execute(DbAsyncTask.DB_SELECT);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.iv_exit:
			dismiss();
			break;

		default:
			break;
		}
	}

	@Override
	public void onCompleteDB(String funName, int type, final Cursor cursor,
			String tableName) {
		hideProgress();

		if (funName.equals("queryLastDetail")) {
			Thread thread = new Thread(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					queryLastDetailResult(cursor);

					mRootView.post(new Runnable() {

						@Override
						public void run() {
							// setData();
							// mCarInfoLastAdapter.setData(mLastModels);
							mMaintenanceDetailAdapter.setData(mDetailModels);
						}
					});
				}

			});

			thread.start();
		}
	}

	private void queryLastDetailResult(Cursor cursor) {

		if (cursor == null)
			return;

		cursor.moveToFirst();

		MaintenanceDetailModel model = null;

		while (!cursor.isAfterLast()) {

			model = new MaintenanceDetailModel(cursor.getString(3),
					cursor.getString(0), cursor.getString(1),
					cursor.getString(2));

			mDetailModels.add(model);

			cursor.moveToNext();

		}
		cursor.close();

	}
}
