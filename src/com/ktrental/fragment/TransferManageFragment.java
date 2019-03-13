package com.ktrental.fragment;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.ktrental.R;
import com.ktrental.adapter.MaintenanceArticlesAdapter;
import com.ktrental.adapter.TransferAdapter;
import com.ktrental.cm.connect.ConnectController;
import com.ktrental.cm.connect.Connector.ConnectInterface;
import com.ktrental.cm.db.DbAsyncTask;
import com.ktrental.cm.db.DbAsyncTask.DbAsyncResLintener;
import com.ktrental.common.DEFINE;
import com.ktrental.common.KtRentalApplication;
import com.ktrental.model.BaseMaintenanceModel;
import com.ktrental.model.DbQueryModel;
import com.ktrental.model.LoginModel;
import com.ktrental.model.MaintenanceActiclesModel;
import com.ktrental.model.TableModel;
import com.ktrental.util.CommonUtil;
import com.ktrental.util.OnChangeFragmentListener;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * 이관관리
 */
public class TransferManageFragment extends BaseFragment implements
		DbAsyncResLintener, OnDismissListener, View.OnClickListener,
		ConnectInterface {

	private static final String INVNR = "INVNR"; // 차량번호
	private static final String ENAME = "ENAME"; // 차량 담당자 명
	private static final String HPPPHON = "HPPPHON"; // 담당자 전화 번호

	private ListView mLvCustomer;
	private ListView mLvMaintenanceArticles;

	private MaintenanceArticlesAdapter maintenanceArticlesAdapter;

	private ArrayList<BaseMaintenanceModel> mMaintenanceModelArray = new ArrayList<BaseMaintenanceModel>();

	private OnDismissDialogFragment mOnDismissDialogFragment;

	private Button mBtnTransfer;

	private TransferAdapter transferAdapter;

	public interface OnDismissDialogFragment {
		void onDismissDialogFragment();
	}

	public TransferManageFragment(){}

	@SuppressWarnings("unchecked")
	public <T> TransferManageFragment(String className,
			OnChangeFragmentListener changeFragmentListener,
			OnDismissDialogFragment aOnDismissDialogFragment, Context context) {
		super(className, changeFragmentListener);
		mContext = context;
		transferAdapter = new TransferAdapter(mContext);
		transferAdapter.setData(mMaintenanceModelArray);
		// mMaintenanceModelArray = (ArrayList<BaseMaintenanceModel>) array;
		mOnDismissDialogFragment = aOnDismissDialogFragment;
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		initGetDBData();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View root = inflater.inflate(R.layout.transfer_layout, null);

		mRootView = root;

		mLvCustomer = (ListView) root.findViewById(R.id.lv_customer);
		mLvMaintenanceArticles = (ListView) root.findViewById(R.id.lv_maintenancearticles);

		maintenanceArticlesAdapter = new MaintenanceArticlesAdapter(mContext,
				mLvMaintenanceArticles);

		mLvCustomer.setAdapter(transferAdapter);

		mLvMaintenanceArticles.setAdapter(maintenanceArticlesAdapter);

		maintenanceArticlesAdapter.notifyDataSetChanged();

		mBtnTransfer = (Button) root.findViewById(R.id.btn_transfer);
		mBtnTransfer.setOnClickListener(this);

		TextView tvTitle = (TextView) mRootView
				.findViewById(R.id.tv_dialog_title);
		tvTitle.setText("이관등록");

		mRootView.findViewById(R.id.iv_exit).setOnClickListener(this);

		return root;
	}

	private void initGetDBData() {

		DbQueryModel dbQueryModel = new DbQueryModel(
				DEFINE.CAR_MASTER_TABLE_NAME, null, null, null);

		DbAsyncTask dbAsyncTask = new DbAsyncTask("MaintenanceArticles",
				mContext, this, dbQueryModel);
		dbAsyncTask.execute(DbAsyncTask.DB_SELECT);
	}

	@Override
	public void onCancel(DialogInterface dialog) {
		if (mOnDismissDialogFragment != null)
			mOnDismissDialogFragment.onDismissDialogFragment();

		super.onCancel(dialog);
	}

	@Override
	public void onCompleteDB(String funName, int type, Cursor cursor,
			String tableName) {
		// TODO Auto-generated method stub
		if ("MaintenanceArticles".equals(funName)) {

			if (cursor == null)
				return;

			LoginModel lm = KtRentalApplication.getLoginModel();
			String myEname = lm.getEname();
			String myInvnr = lm.getInvnr();

			cursor.moveToFirst();

			ArrayList<MaintenanceActiclesModel> array = new ArrayList<MaintenanceActiclesModel>();

			while (!cursor.isAfterLast()) {
				String invnr = cursor.getString(cursor.getColumnIndex(INVNR));
				String ename = cursor.getString(cursor.getColumnIndex(ENAME));
				String hppphon = cursor.getString(cursor
						.getColumnIndex(HPPPHON));

				invnr = decrypt(INVNR, invnr);
				ename = decrypt(ENAME, ename);
				hppphon = decrypt(HPPPHON, hppphon);

				if (myEname.equals(ename) && myInvnr.equals(invnr))
					;
				else
					array.add(new MaintenanceActiclesModel(invnr, ename,
							hppphon));
				
				cursor.moveToNext();

			}
			cursor.close();

			maintenanceArticlesAdapter.setData(array);

		} else if (DEFINE.REPAIR_TABLE_NAME.equals(funName)) {
			hideProgress();
			KtRentalApplication.changeRepair();
			dismiss();
		}
	}

	@Override
	public void onDismiss(DialogInterface dialog) {
		if (mOnDismissDialogFragment != null)
			mOnDismissDialogFragment.onDismissDialogFragment();
		super.onDismiss(dialog);
		// mMaintenanceModelArray.clear();
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();

	}

	public void setOnDismissDialogFragment(
			OnDismissDialogFragment aOnDismissDialogFragment) {
		mOnDismissDialogFragment = aOnDismissDialogFragment;
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.btn_transfer:
			clickTransfer();
			break;
		case R.id.iv_exit:
			//myung 20131230 ADD 체크 초기화
			KtRentalApplication.changeRepair();
			KtRentalApplication.getInstance().queryMaintenacePlan();
			dismiss();
			break;

		default:
			break;
		}
	}

	/**
	 * 이관등록 버튼 클릭
	 */
	private void clickTransfer() {

		MaintenanceActiclesModel model = maintenanceArticlesAdapter
				.getSelectedMaster();

		ArrayList<BaseMaintenanceModel> selectedModels = transferAdapter
				.getSelectedMaintenanceModels();

		if (model == null) {
			showEventPopup2(null, "순회정비기사님을 선택해주세요.");
			return;
		}
		if (selectedModels.size() <= 0) {
			showEventPopup2(null, "이관하실 고객님을 선택해주세요.");
			return;
		}

		showProgress("이관 실행 중입니다.");

		ArrayList<HashMap<String, String>> array = new ArrayList<HashMap<String, String>>();

		LoginModel lm = KtRentalApplication.getLoginModel();
		String myInvnr = lm.getInvnr();

		for (BaseMaintenanceModel baseMaintenanceModel : selectedModels) {
			HashMap<String, String> map = new HashMap<String, String>();
			map.put("EQUNR", baseMaintenanceModel.getEQUNR());
			map.put("REQDT", CommonUtil.getCurrentDay());
			map.put("PREINV", myInvnr);
			map.put("POSINV", model.getInvnr());
			map.put("GSTRS", CommonUtil.getTomorrow());

			array.add(map);
		}

		ConnectController connectController = new ConnectController(this,
				mContext);
		connectController.sendTransfer(array);

	}

	@Override
	public void connectResponse(String FuntionName, String resultText,
			String MTYPE, int resulCode, TableModel tableModel) {

		hideProgress();
		if (MTYPE.equals("S")) {
			ArrayList<BaseMaintenanceModel> selectedModels = transferAdapter
					.getSelectedMaintenanceModels();
			for (BaseMaintenanceModel baseMaintenanceModel : selectedModels) {
				deleteResultDataBase(DEFINE.REPAIR_TABLE_NAME,
						baseMaintenanceModel.getEQUNR());
			}

			KtRentalApplication.getInstance().queryMaintenacePlan();
		}
	}

	@Override
	public void reDownloadDB(String newVersion) {

	}

	/**
	 * 등록결과 성공했을때 DB에 저장된 데이터를 지워준다.
	 */
	private void deleteResultDataBase(String TableName, String equnr) {
		ContentValues contentValues = new ContentValues();
		contentValues.put("EQUNR", equnr);

		String[] keys = new String[1];
		keys[0] = "EQUNR";

		DbAsyncTask dbAsyncTask = new DbAsyncTask(TableName, TableName,
				mContext, this, contentValues, keys);

		dbAsyncTask.execute(DbAsyncTask.DB_DELETE);
	}

	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		getDialog().setCanceledOnTouchOutside(false);
	}

	public ArrayList<BaseMaintenanceModel> getmMaintenanceModelArray() {
		return mMaintenanceModelArray;
	}

	public void setmMaintenanceModelArray(
			ArrayList<BaseMaintenanceModel> mMaintenanceModelArray) {
		this.mMaintenanceModelArray = mMaintenanceModelArray;
		transferAdapter.initSelectedArray();
		transferAdapter.setData(mMaintenanceModelArray);
		transferAdapter.notifyDataSetChanged();
	}

}
