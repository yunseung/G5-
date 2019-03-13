package com.ktrental.fragment;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.ktrental.R;
import com.ktrental.adapter.ResultSendAdapter;
import com.ktrental.cm.db.DbAsyncTask;
import com.ktrental.cm.db.DbAsyncTask.DbAsyncResLintener;
import com.ktrental.common.DEFINE;
import com.ktrental.common.KtRentalApplication;
import com.ktrental.model.DbQueryModel;
import com.ktrental.model.ResultSendModel;
import com.ktrental.util.ResultController;
import com.ktrental.util.ResultController.OnResultCompleate;

import java.util.ArrayList;

public class ResultSendFragment extends BaseFragment implements OnClickListener {

	private ArrayList<ResultSendModel> mResultSendModels = new ArrayList<ResultSendModel>();

	private ListView mLvResultSend;

	private ResultSendAdapter mResultSendAdapter;

	public ResultSendFragment(){
		super();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mRootView = (View) inflater.inflate(R.layout.result_send_layout, null);

		mRootView.findViewById(R.id.btn_delete).setOnClickListener(this);
		mRootView.findViewById(R.id.btn_send).setOnClickListener(this);
		mRootView.findViewById(R.id.iv_exit).setOnClickListener(this);
		mLvResultSend = (ListView) mRootView.findViewById(R.id.lv_result_send);
		mResultSendAdapter = new ResultSendAdapter(mContext);
		mLvResultSend.setAdapter(mResultSendAdapter);

		TextView tvTitle = (TextView) mRootView
				.findViewById(R.id.tv_dialog_title);
		tvTitle.setText("미전송 내역 관리");

		queryBaseGroup();

		return mRootView;
	}

	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btn_delete:
			deleteBaseGroup();
			break;
		case R.id.btn_send:
			sendBaseGroup();
			break;
		case R.id.iv_exit:
			dismiss();
			break;

		default:
			break;
		}
	}

	private void sendBaseGroup() {
		showProgress("미전송 내역을 전송 중입니다");
		ResultController resultController = new ResultController(mContext,
				new OnResultCompleate() {

					@Override
					public void onResultComplete(String message) {
						// reDownloadDB("");
						// runUiThread(UiRunnable.MODE_PROGRESS_SHOW,
						// message);
						hideProgress();
						// if (!message.equals("0")) {
						// queryBaseGroup();
						// KtRentalApplication.changeRepair();
						// }
						queryBaseGroup();
						KtRentalApplication.changeRepair();
					}
				});
		resultController.queryBaseGroup(false);
//		resultController.queryBaseGroup(true);

	}

	private void deleteBaseGroup() {

		ArrayList<ResultSendModel> arrayList = new ArrayList<ResultSendModel>();
		boolean checkFlag = true;
		for (ResultSendModel model : mResultSendModels) {
			if (model.isCheckFlag()) {
				arrayList.add(model);
				checkFlag = false;
			}
		}
		if (checkFlag) {
			showEventPopup2(null, "삭제할 미전송 내역을 먼저 선택해 주세요.");
			return;
		}
		int size = mResultSendModels.size();

		for (ResultSendModel resultSendModel : arrayList) {
			deleteResultDataBase(DEFINE.REPAIR_RESULT_BASE_TABLE_NAME,
					resultSendModel.getINVNR());
			if (size == arrayList.size()) {
				deleteResultDataBase(DEFINE.REPAIR_RESULT_STOCK_TABLE_NAME,
						resultSendModel.getName());
				deleteResultDataBase(DEFINE.REPAIR_RESULT_IMAGE_TABLE_NAME,
						resultSendModel.getName());
			}
			mResultSendModels.remove(resultSendModel);
		}

		if (mResultSendModels.size() <= 0) {
			mRootView.findViewById(R.id.iv_empty).setVisibility(View.VISIBLE);
			mLvResultSend.setVisibility(View.GONE);
		}

		mResultSendAdapter.setData(mResultSendModels);
		KtRentalApplication.changeRepair();
	}

	/**
	 */
	private void deleteResultDataBase(String TableName, String INVNR) {
		ContentValues contentValues = new ContentValues();
		contentValues.put("INVNR", INVNR);

		String[] keys = new String[1];
		keys[0] = "INVNR";

		DbAsyncTask dbAsyncTask = new DbAsyncTask(TableName, TableName,
				mContext, new DbAsyncResLintener() {

					@Override
					public void onCompleteDB(String funName, int type,
							Cursor cursor, String tableName) {
						// TODO Auto-generated method stub

					}
				}, contentValues, keys);

		dbAsyncTask.execute(DbAsyncTask.DB_DELETE);
	}

	private void queryBaseGroup() {
		mResultSendModels.clear();
		showProgress();
		String[] _whereArgs = null;
		String[] _whereCause = null;

		String[] colums = null;

		DbQueryModel dbQueryModel = new DbQueryModel(
				DEFINE.REPAIR_RESULT_BASE_TABLE_NAME, _whereCause, _whereArgs,
				colums);

		DbAsyncTask dbAsyncTask = new DbAsyncTask("queryBaseGroup", mContext,
				new DbAsyncResLintener() {

					@Override
					public void onCompleteDB(String funName, int type,
							Cursor cursor, String tableName) {
						// TODO Auto-generated method stub
						hideProgress();
						if (funName.equals("queryBaseGroup")) {

							if (cursor == null || cursor.getCount() <= 0) {

								mRootView.findViewById(R.id.iv_empty)
										.setVisibility(View.VISIBLE);
								mLvResultSend.setVisibility(View.GONE);

								return;
							}

							if (cursor != null) {
								// if (mBtnSend != null)
								// mBtnSend.setText("미전송건 (" + resultSendCount
								// + ")");
								cursor.moveToFirst();
								while (!cursor.isAfterLast()) {

									String iEDD = cursor.getString(4);
									String name = cursor.getString(6);
									String iEDZ = cursor.getString(7);
									String iNVNR = cursor.getString(16);
									String message = cursor.getString(9);
									String count = cursor.getString(13);

									ResultSendModel model = new ResultSendModel(
											iEDD, iEDZ, iNVNR, name, count, message);
									mResultSendModels.add(model);

									cursor.moveToNext();
								}

								mResultSendAdapter.setData(mResultSendModels);
							}

						}
					}
				}, dbQueryModel);
		dbAsyncTask.execute(DbAsyncTask.DB_SELECT);
	}

}
