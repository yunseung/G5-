package com.ktrental.fragment;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.ktrental.R;
import com.ktrental.adapter.MaintenancLastItemAdapter;
import com.ktrental.adapter.MaintenanceItemAdapter;
import com.ktrental.adapter.MaintenanceItemAdapter.OnConsumptionItem;
import com.ktrental.adapter.MaintenanceItemGroupAdapter;
import com.ktrental.cm.connect.ConnectController;
import com.ktrental.cm.connect.Connector;
import com.ktrental.cm.db.DbAsyncTask;
import com.ktrental.cm.db.DbAsyncTask.DbAsyncResLintener;
import com.ktrental.common.DEFINE;
import com.ktrental.model.CarInfoModel;
import com.ktrental.model.DbQueryModel;
import com.ktrental.model.MaintenanceGroupModel;
import com.ktrental.model.MaintenanceItemModel;
import com.ktrental.model.MaintenanceModel;
import com.ktrental.model.PartsMasterModel;
import com.ktrental.model.TableModel;
import com.ktrental.popup.ElectricSelectPopup;
import com.ktrental.popup.EventPopup2;
import com.ktrental.popup.InventoryPopup;
import com.ktrental.popup.QuickAction;
import com.ktrental.util.LogUtil;
import com.ktrental.util.OnEventOkListener;
import com.ktrental.util.OnSelectedItem;
import com.ktrental.util.kog;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

public class MaintentanceInputFragment extends BaseFragment
		implements OnClickListener, DbAsyncResLintener, OnSelectedItem, OnConsumptionItem, Connector.ConnectInterface {

	private final static String Tilte = "정비항목입력";

	/**
	 * 선택된 차량 보여줘야 되는 총 정보
	 */
	private CarInfoModel mCarInfoModel;

	public void setCarInfoModel(CarInfoModel aCarInfoModel) {
		this.mCarInfoModel = aCarInfoModel;
	}

	// 그룹 리스트
	private ListView mLvGroup;
	private MaintenanceItemGroupAdapter mGroupAdapter;
	private ArrayList<MaintenanceGroupModel> mGroupLArrayList = new ArrayList<MaintenanceGroupModel>();

	// 정비항목 리스트
	private ListView mLvItem;
	private MaintenanceItemAdapter mItemAdapter;
	private ArrayList<MaintenanceItemModel> mItemModels = new ArrayList<MaintenanceItemModel>();

	// 최종 점검 항목 리스트
	private ListView mLvLast;
	private MaintenancLastItemAdapter mLastItemAdapter;
	private ArrayList<MaintenanceItemModel> mLastItemModels = new ArrayList<MaintenanceItemModel>();

	private ArrayList<MaintenanceItemModel> mFirstItems = new ArrayList<MaintenanceItemModel>();

	private OnResultInut mOnResultInut;
	private ArrayList<PartsMasterModel> mPartsArray = new ArrayList<PartsMasterModel>();

	private HashMap<String, ArrayList<PartsMasterModel>> mPartsMap = new HashMap<String, ArrayList<PartsMasterModel>>();

	private LinkedHashMap<String, ArrayList<MaintenanceItemModel>> mGroupMap = new LinkedHashMap<String, ArrayList<MaintenanceItemModel>>();

	private Button mBtnSave;

	private static final String ELECTRIC_TEXT = "409";

	private MaintenanceGroupModel mCurrentGroupModel;

	private ArrayList<MaintenanceItemModel> mTotalLastItemModels = new ArrayList<MaintenanceItemModel>();

	private InventoryPopup inventoryPopup;

	private String mFirstMATKL = "";

	private ImageView mIvItemEmpty;
	private ImageView mIvLastEmpty;

	// 2017-11-29. hjt 순회점검을 위한 항목들
	private ArrayList<HashMap<String, String>> rd05_arr;
	// 순회점검인지 여부
	private boolean isInspect = false;

	private ConnectController cc;

	private TextView mTvTitlePrice1;
	private TextView mTvTitlePrice2;

	private LinearLayout mLlLastTotalArea;
	private TextView mTvLastTotalPrice;

	private int mLastTotalPrice = 0;

	public void setOnResultInput(OnResultInut aOnResultInut) {
		this.mOnResultInut = aOnResultInut;
	}

	@Override
	public void connectResponse(String FuntionName, String resultText, String MTYPE, int resulCode, TableModel tableModel) {
		if (MTYPE == null || !MTYPE.equals("S")) {
			cc.duplicateLogin(mContext);

			Toast.makeText(mContext, resultText, Toast.LENGTH_SHORT).show();
			return;
		}
		if (FuntionName.equals("ZMO_1020_RD05")) {
			rd05_arr = tableModel.getTableArray();
			queryGroup();
		} else if (FuntionName.equals("ZMO_1020_RD08")) {
			ArrayList<HashMap<String, String>> array_hash = tableModel.getTableArray("O_ITAB2");
			MaintenanceGroupModel maintenanceGroupModel = null;
			mGroupLArrayList.clear();

			String prev_item = "";
			for (int i = 0; i < array_hash.size(); i++) {
				String item = array_hash.get(i).get("MATKL");
				if(i==0) {
					maintenanceGroupModel = new MaintenanceGroupModel(array_hash.get(i).get("WGBEZ"), array_hash.get(i).get("MATKL"));
					mGroupLArrayList.add(maintenanceGroupModel);
				} else if(!prev_item.equals(item)) {
					maintenanceGroupModel = new MaintenanceGroupModel(array_hash.get(i).get("WGBEZ"), array_hash.get(i).get("MATKL"));
					mGroupLArrayList.add(maintenanceGroupModel);
				}
				prev_item = array_hash.get(i).get("MATKL");
			}
			mGroupAdapter.setData(mGroupLArrayList);

			if (array_hash.size() > 0)
				responsePartsTypeForIoT(array_hash);
			else
				setGroupList();
		}
	}

	@Override
	public void reDownloadDB(String newVersion) {

	}

	public interface OnResultInut {
		void onResultInput(ArrayList<MaintenanceItemModel> models, boolean bFlag);
	}

	public MaintentanceInputFragment() {

	}

	public ArrayList<MaintenanceItemModel> getmLastItemModels() {
		return mLastItemModels;
	}

	public void setTotalStockSelectArray(ArrayList<MaintenanceItemModel> array) {
		mTotalLastItemModels = array;
	}

	public void setmLastItemModels(ArrayList<MaintenanceItemModel> array) {
		if (array.size() > 0) {
			mLastItemModels = array;

			mFirstItems = new ArrayList<MaintenanceItemModel>();

			mLastTotalPrice = 0;

			ArrayList<MaintenanceItemModel> removeArr = new ArrayList<MaintenanceItemModel>();

			for (MaintenanceItemModel maintenanceItemModel : array) {

				MaintenanceItemModel firstItem = null;
				for (MaintenanceItemModel item : mFirstItems) {
					if (item.getName().equals(maintenanceItemModel.getName())) {
						firstItem = item;
					}
				}
				if (firstItem != null) {
					removeArr.add(firstItem);
					// mFirstItems.remove(firstItem);
				}

				mFirstItems.add(maintenanceItemModel.clone());

				if (!maintenanceItemModel.getNETPR().replace(",", "").trim().isEmpty()) {
					mLastTotalPrice += (Integer.parseInt(maintenanceItemModel.getNETPR().replace(",", "")) * maintenanceItemModel.getConsumption());
				}

			}
			for (MaintenanceItemModel maintenanceItemModel : removeArr) {
				mFirstItems.remove(maintenanceItemModel);
			}
		}
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {

		inventoryPopup = new InventoryPopup(mContext, QuickAction.HORIZONTAL, R.layout.inventory_popup,
				InventoryPopup.TYPE_NORMAL_NUMBER);

		return super.onCreateDialog(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mRootView = inflater.inflate(R.layout.input_layout, null);

		initSettingView();

		return mRootView;
	}

	private void initSettingView() {

		mTvTitlePrice1 = (TextView) mRootView.findViewById(R.id.tv_title_price1);
		mTvTitlePrice2 = (TextView) mRootView.findViewById(R.id.tv_title_price2);
		mLlLastTotalArea = (LinearLayout) mRootView.findViewById(R.id.ll_last_total_area);
		mTvLastTotalPrice = (TextView) mRootView.findViewById(R.id.tv_last_total_price);

		mTvLastTotalPrice.setText(currencyFormat(mLastTotalPrice) + "원");

		TextView title = (TextView) mRootView.findViewById(R.id.tv_dialog_title);
		title.setText(Tilte);

		ImageView exit = (ImageView) mRootView.findViewById(R.id.iv_exit);
		exit.setOnClickListener(this);

		// Button btnAdd = (Button) mRootView.findViewById(R.id.btn_item_add);
		// btnAdd.setOnClickListener(this);

		Button btnDelelte = (Button) mRootView.findViewById(R.id.btn_last_delete);
		btnDelelte.setOnClickListener(this);

		// Button btnEmpty = (Button) mRootView.findViewById(R.id.btn_empty);
		// btnEmpty.setOnClickListener(this);

		mLvGroup = (ListView) mRootView.findViewById(R.id.lv_group);

		mGroupAdapter = new MaintenanceItemGroupAdapter(mContext, mLvGroup);
		mGroupAdapter.setOnSeletedItem(this);
		mLvGroup.setAdapter(mGroupAdapter);

		// 2014-05-09 KDH 와 이건 진짜 핵노답 이다.
		mLvItem = (ListView) mRootView.findViewById(R.id.lv_maintenance_item);
		mItemAdapter = new MaintenanceItemAdapter(mContext, mRootView, mLvItem, inventoryPopup, mCarInfoModel.get_gubun(), this);
		mLvItem.setAdapter(mItemAdapter);

		mLvLast = (ListView) mRootView.findViewById(R.id.lv_last_item);
		mLastItemAdapter = new MaintenancLastItemAdapter(mContext, MaintenancLastItemAdapter.INPUT, mCarInfoModel.get_gubun());
		mLastItemAdapter.setData(mLastItemModels);
		mLvLast.setAdapter(mLastItemAdapter);

		mIvItemEmpty = (ImageView) mRootView.findViewById(R.id.iv_item_empty);
		mIvLastEmpty = (ImageView) mRootView.findViewById(R.id.iv_last_empty);

		cc = new ConnectController(this, mContext);


		//2017-11-28. hjt 순회점검일 경우엔 아이템이 다르다.
		if (mCarInfoModel != null) {
			String period = mCarInfoModel.getTourMainenancePeriod();
			if (period != null && period.contains("점검")) { // 6개월짜리 어쩌구.. 이건 냅둬??
				isInspect = true;
				mItemAdapter.setIsIspect(true);
				cc.getZMO_1020_RD05(mCarInfoModel.getCarNum());
			} else if (mCarInfoModel.get_gubun() != null) {
				if (mCarInfoModel.get_gubun().equals("A")) {
					cc.getZMO_1020_RD08(mCarInfoModel.getCarNum(), mCarInfoModel.getMINVNR());
				} else {
					isInspect = false;
					queryGroup(); // 보통의 경우 여기로 들어오는거같다.
				}
			}  else {
				isInspect = false;
				queryGroup(); // 보통의 경우 여기로 들어오는거같다.
			}
		} else {
			isInspect = false;
			queryGroup();
		}

		if (mCarInfoModel.get_gubun() != null) {
			if (mCarInfoModel.get_gubun().equals("A")) {
				mTvTitlePrice1.setVisibility(View.VISIBLE);
				mTvTitlePrice2.setVisibility(View.VISIBLE);
				mLlLastTotalArea.setVisibility(View.VISIBLE);
			} else {
				mTvTitlePrice1.setVisibility(View.GONE);
				mTvTitlePrice2.setVisibility(View.GONE);
				mLlLastTotalArea.setVisibility(View.GONE);
			}
		} else {
			mTvTitlePrice1.setVisibility(View.GONE);
			mTvTitlePrice2.setVisibility(View.GONE);
			mLlLastTotalArea.setVisibility(View.GONE);
		}

		mBtnSave = (Button) mRootView.findViewById(R.id.btn_save);
		mBtnSave.setOnClickListener(this);

	}

	private void initPartType(boolean allFlag) {
		kog.e("Jonathan", "관련품목 여기 들어와라~!!");

		kog.e("Jonathan", "mCarInfoModel.getMdlcd() :: " + mCarInfoModel.getMdlcd() + "  mCarInfoModel.getOilType() :: "
				+ mCarInfoModel.getOilType());

		String[] _whereArgs = {mCarInfoModel.getMdlcd(), mCarInfoModel.getOilType()};
		// String[] _whereArgs = { mCarInfoModel.getMdlcd(),
		// mCarInfoModel.getOilType(), mCarInfoModel.get()};
		String[] _whereCause = {DEFINE.MDLCD, DEFINE.FUELCD}; // 차종코드 / 유종코드
		// String[] _whereCause = { DEFINE.MDLCD, DEFINE.FUELCD, DEFINE.MTQTY };
		// //차종코드 / 유종코드 / 재고수량 //Jonathan 14.11.17 재고수량 추가

		// String[] colums = { DEFINE.MATNR, DEFINE.MATKL };
		String[] colums = {DEFINE.MATNR, DEFINE.MATKL, DEFINE.MTQTY, DEFINE.MINQTY, DEFINE.MAXQTY};

		if (allFlag) {
			_whereArgs = null;
			_whereCause = null;
		}

		kog.e("Jonathan", "222 _whereArgs :: " + _whereArgs[0].toString() + "   " + _whereArgs[1].toString());
		kog.e("Jonathan", "222 _whereCause :: " + _whereCause[0].toString() + "   " + _whereCause[1].toString());
		kog.e("Jonathan", "222 colums :: " + colums[0].toString() + "   " + colums[1].toString());

		DbQueryModel dbQueryModel = new DbQueryModel(DEFINE.PARTSMASTER_TABLE_NAME, _whereCause, _whereArgs, colums);

		kog.e("Jonathan", "222 dbQueryModel.gettablename :: " + dbQueryModel.getTableName());

		dbQueryModel.setOrderBy(DEFINE.MATKL + " asc");

		DbAsyncTask dbAsyncTask = new DbAsyncTask("initPartType", mContext, this, dbQueryModel);
		dbAsyncTask.execute(DbAsyncTask.DB_SELECT);
	}

	// myung 20131230 임시 정비항목 전부 노출
	private void initPartType2() {
		String[] _whereArgs = null;
		String[] _whereCause = null;

		String[] colums = {DEFINE.MATNR, DEFINE.MATKL};

		DbQueryModel dbQueryModel = new DbQueryModel(DEFINE.STOCK_TABLE_NAME, _whereCause, _whereArgs, colums);

		dbQueryModel.setOrderBy(DEFINE.MATKL + " asc");

		DbAsyncTask dbAsyncTask = new DbAsyncTask("initPartType2", mContext, this, dbQueryModel);
		dbAsyncTask.execute(DbAsyncTask.DB_SELECT);
	}

	private void queryItem(MaintenanceGroupModel groupModel) {

		// mItemModels.clear();

		// String[] _whereArgs = {};
		// String[] _whereCause = { DEFINE.MDLCD, DEFINE.FUELCD };
		//
		// String[] colums = { "ZCODEVT", "ZCODEV" };
		//
		// DbQueryModel dbQueryModel = new
		// DbQueryModel(DEFINE.PARTSMASTER_TABLE,
		// _whereCause, _whereArgs, colums);
		//
		// DbAsyncTask dbAsyncTask = new DbAsyncTask("queryGroup", mContext,
		// this,
		// dbQueryModel);
		// dbAsyncTask.execute(DbAsyncTask.DB_SELECT);

		// setDummy(groupModel);
		ArrayList<MaintenanceItemModel> arr = mGroupMap.get(groupModel.getName_key());

		// kog.e("KDH", "arrSize = "+arr.size());

		ArrayList<MaintenanceItemModel> backSelectedItem = new ArrayList<MaintenanceItemModel>();

		// for (MaintenanceItemModel maintenanceItemModel : arr) {
		// for (MaintenanceItemModel itemModel : mLastItemModels) {
		// if (itemModel.getName().equals(maintenanceItemModel.getName()))
		// {
		// if (itemModel
		// .getMaintenanceGroupModel()
		// .getName_key()
		// .equals(maintenanceItemModel
		// .getMaintenanceGroupModel().getName_key()))
		// {
		//
		// // int consumption = itemModel.getConsumption();
		// // maintenanceItemModel.setConsumption(consumption);
		//// if(itemModel.getMaintenanceGroupModel().get)
		// backSelectedItem.add(itemModel);
		//
		// // model.setCheck(true);
		// break;
		// }
		// }
		// }
		// }

		if (arr != null && arr.size() > 0) {
			for (MaintenanceItemModel maintenanceItemModel : arr) {
				for (MaintenanceItemModel itemModel : mLastItemModels) {
					MaintenanceGroupModel model = itemModel.getMaintenanceGroupModel();
					if(model == null){
						LogUtil.e("hjt", "model == null!!!");
						return;
					}
					try {
						if (itemModel.getName().equals(model.getName())) {
							if (model.getName_key()
									.equals(model.getName_key())) {

								// int consumption = itemModel.getConsumption();
								// maintenanceItemModel.setConsumption(consumption);
								// if(itemModel.getMaintenanceGroupModel().get)
								backSelectedItem.add(itemModel);

								// modsel.setCheck(true);
								LogUtil.e("hjt", "model != null!!!");
								break;
							}
						}
					} catch (Exception e){
						LogUtil.e("hjt", "model Exception!!!");
						e.printStackTrace();
					}

				}
			}

			mItemAdapter.setData(arr);
			mItemAdapter.setSelectedMaintenanceModels(backSelectedItem);
			initItemEmpty(arr);
		} else {
			initItemEmpty(arr);
		}
	}

	// private void setDummy(MaintenanceGroupModel groupModel) {
	//
	// Random random = new Random();
	//
	// for (int i = 0; i < random.nextInt(20); i++) {
	// MaintenanceItemModel model = new MaintenanceItemModel("AH 밧데리" + i,
	// random.nextInt(10), groupModel);
	// mItemModels.add(model);
	// }
	//
	// }

	private void queryGroup() {
		showProgress("정비항목을 조회 중입니다.");
		String[] _whereArgs = {"M028"};
		String[] _whereCause = {"ZCODEH"};

		String[] colums = {"ZCODEVT", "ZCODEV"};

		DbQueryModel dbQueryModel = new DbQueryModel(ConnectController.O_ITAB1_TABLE_NAME, _whereCause, _whereArgs,
				colums);

		DbAsyncTask dbAsyncTask = new DbAsyncTask("queryGroup", mContext, this, dbQueryModel);
		dbAsyncTask.execute(DbAsyncTask.DB_SELECT);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.iv_exit:
				mLastItemModels = mFirstItems;
				if (mOnResultInut != null)
					mOnResultInut.onResultInput(mLastItemModels, true);
				dismiss();
				break;

			// case R.id.btn_item_add:
			// clickAdd(); // 정비 항목 추가
			// break;

			case R.id.btn_last_delete:
				clickLastDelete();
				break;

			// case R.id.btn_empty:
			// clickEmpty();
			// break;

			case R.id.btn_save:
				clickSave();
				break;

			default:
				break;
		}

	}

	/**
	 * Engine oil set 가 모두 추가되어 있는지 확인하는 함수. (For IoT)
	 * @return true : engine oil set 모두 추가. false : engine oil set 완성되지 않음.
	 */
	private boolean checkEngineOilSet() {
		List<String> engineOilSet = new ArrayList<>();
		engineOilSet.add("401");
		engineOilSet.add("402");
		engineOilSet.add("403");
		engineOilSet.add("404");
		int isContainEngineOilSet = 0;
		if (mCarInfoModel.get_gubun().equals("A")) {
			for (MaintenanceItemModel model : mLastItemModels) {
				switch (model.getMaintenanceGroupModel().getName_key()) {
					case "401" :
						isContainEngineOilSet++;
						engineOilSet.remove("401");
						break;
					case "402" :
						isContainEngineOilSet++;
						engineOilSet.remove("402");
						break;
					case "403" :
						isContainEngineOilSet++;
						engineOilSet.remove("403");
						break;
					case "404" :
						isContainEngineOilSet++;
						engineOilSet.remove("404");
						break;
				}
			}
			if (isContainEngineOilSet < 3) {
				return false;
			} else {
				return true;
			}
		} else {
			return true;
		}
	}

	private void clickSave() {

		if (mLastItemModels.size() > 0) {

			if (!checkEngineOilSet()) {
				showEventPopup2(null, "엔진오일SET(가솔린/디젤오일, 에어크리너, 오일휠터)\n자제그룹은 필수항목입니다.");
				 return;
			}

			if (mOnResultInut != null) {
				mOnResultInut.onResultInput(mLastItemModels, true);
			}

			dismiss();
		} else {
			showEventPopup2(new OnEventOkListener() {

				@Override
				public void onOk() {

				}
			}, getString(R.string.maintenance_layout_msg));
		}

	}

	private void clickEmpty() {

		showEventPopup1("", "입력된 정비항목은 삭제됩니다.\n교환없음을 선택하시겠습니까?", new OnEventOkListener() {

			@Override
			public void onOk() {
				// TODO Auto-generated method stub
				if (mOnResultInut != null) {
					mLastItemModels.clear();
					mOnResultInut.onResultInput(null, false);
				}

				dismiss();
			}
		}, null);

	}

	private void clickLastDelete() {
		ArrayList<MaintenanceItemModel> models = mLastItemAdapter.getSelectedMaintenanceModels();

		ArrayList<MaintenanceItemModel> tempModels = new ArrayList<MaintenanceItemModel>();

		for (MaintenanceItemModel maintenanceItemModel : models) {
			MaintenanceItemModel model = maintenanceItemModel.clone();
			model.setCheck(false);
			mLastItemModels.remove(maintenanceItemModel);
			model.setConsumption(0);
			model.setSelectcConsumption(0);
			tempModels.add(model);
		}

		mItemAdapter.initSelectedMaintenanceItem(tempModels);
		mLastItemAdapter.initSelectedMaintenanceArray();
		mLastItemAdapter.setData(mLastItemModels);

		if (mCarInfoModel.get_gubun() != null) {
			if (mCarInfoModel.get_gubun().equals("A")) {
				mLastTotalPrice = 0;
				for (MaintenanceItemModel model : mLastItemModels) {
					mLastTotalPrice += (Integer.parseInt(model.getNETPR().replace(",", "")) * model.getConsumption());
				}

				mTvLastTotalPrice.setText(currencyFormat(mLastTotalPrice) + "원");
			}
		}
		initLastEmpty();
	}

	private String currencyFormat(int inputMoney) {
		DecimalFormat decimalFormat = new DecimalFormat("#,##0");

		return decimalFormat.format(inputMoney);
	}

	private void clickAdd() {

		ArrayList<MaintenanceItemModel> models = mItemAdapter.getSelectedMaintenanceModels();

		boolean checkLast = false;
		for (MaintenanceItemModel maintenanceItemModel : models) {
			for (int i = 0; i < mLastItemModels.size(); i++) {
				MaintenanceItemModel lastModel = mLastItemModels.get(i);
				if (lastModel.getName().equals(maintenanceItemModel.getName())) {
					if (lastModel.getMaintenanceGroupModel().getName_key()
							.equals(maintenanceItemModel.getMaintenanceGroupModel().getName_key())) {
						lastModel.setConsumption(lastModel.getConsumption() + maintenanceItemModel.getConsumption());
						lastModel.setSelectcConsumption(0);
						checkLast = true;
						lastModel.setCheck(false);
						break;
					}
				}
			}
			if (!checkLast) {
				//TODO 정비항목 체크해서 추가하면 하단 리스트에 이곳에서 add 된 후 이 메서드 아래로~~
				MaintenanceItemModel model = maintenanceItemModel.clone();
				model.setCheck(false);
				mLastItemModels.add(model);

			}
			checkLast = false;
		}

		if (mCurrentGroupModel != null) {
			if (mCurrentGroupModel.getName_key().equals(ELECTRIC_TEXT)) {

				ArrayList<MaintenanceItemModel> array = new ArrayList<MaintenanceItemModel>();

				for (MaintenanceItemModel maintenanceItemModel : mLastItemModels) {
					if (maintenanceItemModel.getMaintenanceGroupModel().getName_key().equals(ELECTRIC_TEXT))
						if (maintenanceItemModel.getGRP_CD().equals(" "))
							array.add(maintenanceItemModel);
				}
				if (array.size() > 0) {
					// ELECTRIC_TEXT 에 해당하는 GRP_CD 값을 팝업을 통해 돌려받아 넣어주어야 한다.
					// ElectricSelectFragment electricSelectFragment = new
					// ElectricSelectFragment(
					// array);
					// electricSelectFragment.show(getChildFragmentManager(),
					// ElectricSelectFragment.class.getName(), 860, 440);
					ElectricSelectPopup electricSelectPopup = new ElectricSelectPopup(array, mContext);
					electricSelectPopup.show();
					electricSelectPopup.setOnDismissListener(new OnDismissListener() {

						@Override
						public void onDismiss(DialogInterface dialog) {
							// TODO Auto-generated method stub
							// if (mOnResultInut != null)
							// mOnResultInut
							// .onResultInput(mLastItemModels);
							//
							// dismiss();
						}
					});
					// return;
				}
			}
		}

		mItemAdapter.initSelectedMaintenanceArray();
		mLastItemAdapter.setData(mLastItemModels);

		if (mCarInfoModel.get_gubun() != null) {
			if (mCarInfoModel.get_gubun().equals("A")) {
				mLastTotalPrice = 0;
				for (MaintenanceItemModel model : mLastItemModels) {
					mLastTotalPrice += (Integer.parseInt(model.getNETPR().replace(",", "")) * model.getConsumption());
				}
				mTvLastTotalPrice.setText(currencyFormat(mLastTotalPrice) + "원");
			}
		}



		mLastItemAdapter.notifyDataSetChanged();
	}

	@Override
	public void onCompleteDB(String funName, int type, Cursor cursor, String tableName) {

		kog.e("Jonathan", " funName :: " + funName + " type :: " + type + " cursor :: " + cursor.getCount()
				+ " tableName :: " + tableName);
		// Log.e("funName/tableName", funName+"/"+tableName);
		if (cursor != null && cursor.getCount() > 0) {
			if (funName.equals("queryGroup")) {
				responseGroup(cursor);
			} else if (funName.equals("initPartType")) {
				if (cursor.getCount() > 0)
					responsePartsType(cursor);
				else
					setGroupList();
				// else
				// initPartType(true);

			} else if (funName.equals("initQueryItem")) {
				kog.e("Jonathan", "여기로 들어와야함.");
				if (cursor.getCount() > 0) {
					kog.e("Jonathan", "cursor != null");
					responseItem(cursor);
				}

			} else if (funName.equals("initQueryItem2")) {
				if (cursor.getCount() > 0) {
					kog.e("Jonathan", "cursor != null");
					responseItem2(cursor);
				}
			}
			// myung 20131230 임시 정비항목 전부 노출 <삭제요망>
			// else if(funName.equals("initPartType2")){
			// kog.e("Jonathan", "관련품목 initPartType2");
			// if (cursor.getCount() > 0)
			// responsePartsType(cursor);
			// else
			// setGroupList();
			// }
		}
	}

	LinkedHashMap<String, ArrayList<MaintenanceItemModel>> tempGroupMap = new LinkedHashMap<String, ArrayList<MaintenanceItemModel>>();

	private void responseItem(Cursor cursor) {
		Thread thread = new Thread(new ItemSetRunnable(cursor));
		thread.start();
	}

	private void responseItem2(Cursor cursor) {
		Thread thread = new Thread(new ItemSetRunnable2(cursor));
		thread.start();
	}

	private void initApointPart() {

	}

	private void initItem() {
		final ArrayList<MaintenanceItemModel> temp = new ArrayList<MaintenanceItemModel>();
		final ArrayList<MaintenanceItemModel> backSelectedItem = new ArrayList<MaintenanceItemModel>();

		Iterator<String> it = mGroupMap.keySet().iterator();
		ArrayList<MaintenanceItemModel> arrayList = new ArrayList<MaintenanceItemModel>();
		// while (it.hasNext()) {
		// String strKey = "";
		// strKey = it.next();
		//
		// break;
		// }


		String matkl = mGroupLArrayList.get(0).getName_key();
		kog.e("KDH", "matkl = " + matkl);

		arrayList = mGroupMap.get(matkl);

		for (int i = 0; i < arrayList.size(); i++) {
			MaintenanceItemModel item = arrayList.get(i).clone();
			kog.e("Jonathan", "item getMTQTY = " + item.getMTQTY());
			temp.add(item);

			for (MaintenanceItemModel maintenanceItemModel : mLastItemModels) {
				if (maintenanceItemModel.getMATNR().equals(item.getMATNR())) {
					backSelectedItem.add(item);
					break;
				}
			}
		}

		mLvItem.post(new Runnable() {

			@Override
			public void run() {
				mItemAdapter.setData(temp);
				mItemAdapter.setSelectedMaintenanceModels(backSelectedItem);
				mItemAdapter.notifyDataSetChanged();
				initItemEmpty(mItemModels);
				initLastEmpty();
				// hideProgress();
			}
		});

	}

	// STOCK_TABLE ( MATKL, GRP_CD, MAKTX, LABST, MEINS MATNR, LTEXT, BUKRS )
	private class ItemSetRunnable implements Runnable {

		private Cursor mCursor;

		public ItemSetRunnable(Cursor cursor) {
			mCursor = cursor;
		}

		@Override
		public void run() {
			// TODO Auto-generated method stub
			String mMATNR = "";
			if (isInspect) {
				for(int i=0; i<rd05_arr.size(); i++) {
					int stock = 0;
					try {
						stock = Integer.parseInt(rd05_arr.get(i).get("MATKL"));
					} catch (Exception e) {
						e.printStackTrace();
					}

					// Jonathan 14.09.11 재고가 0일때에도 보여지게 함. >= 0 을 > 으로하면 0건 안나옴.
					if (stock >= 0) {
						String MATKL = rd05_arr.get(i).get("MATKL");

						if (mGroupMap.containsKey(MATKL)) {
							ArrayList<MaintenanceItemModel> arrayList = mGroupMap.get(MATKL);

							ArrayList<MaintenanceItemModel> tempList = tempGroupMap.get(MATKL);

							String MTQTY = "";
							String MINQTY = "";
							String MAXQTY = "";
							String NETPR = "";
							String ACTGRP = "";
							if (mPartsMap.containsKey(MATKL)) {
								ArrayList<PartsMasterModel> partsMasterModels = mPartsMap.get(MATKL);
								for (PartsMasterModel partsMasterModel : partsMasterModels) {
//								String mMATNR = mCursor.getString(5);

									for (int j = 0; j < rd05_arr.size(); j++) {
										if ("MATNR".equals(rd05_arr.get(j).get("MATNR"))) {
											mMATNR = rd05_arr.get(j).get("MATNR");
										}
									}

									if (partsMasterModel.getMATNR().equals(mMATNR)
											&& mCarInfoModel.getMdlcd().equals(partsMasterModel.getMDLCD())
											&& mCarInfoModel.getOilType().equals(partsMasterModel.getFUELCD()))

									{
										MTQTY = partsMasterModel.getMTQTY().trim();
										if (!partsMasterModel.getMINQTY().isEmpty()) {
											MINQTY = partsMasterModel.getMINQTY().trim();
										}

										if (!partsMasterModel.getMAXQTY().isEmpty()) {
											MAXQTY = partsMasterModel.getMAXQTY().trim();
										}

										if (!partsMasterModel.getNETPR().isEmpty()) {
											NETPR = partsMasterModel.getNETPR().trim();
										}

										if (!partsMasterModel.getACTGRP().isEmpty()) {
											ACTGRP = partsMasterModel.getACTGRP().trim();
										}
									}
								}
							}

							String labst = rd05_arr.get(i).get("LABST");
							labst = labst.trim();
							int labstInt = (int) Double.parseDouble(labst);

							MaintenanceItemModel model = new MaintenanceItemModel(
									rd05_arr.get(i).get("MAKTX"),
									labstInt,
									rd05_arr.get(i).get("MATNR"),
									rd05_arr.get(i).get("MEINS"), null,
									rd05_arr.get(i).get("GRP_CD"), MTQTY, MINQTY, MAXQTY, NETPR, ACTGRP);

							for (MaintenanceItemModel itemModel : mLastItemModels) {
								if (itemModel.getName().equals(model.getName())) {
									if (itemModel.getMaintenanceGroupModel().getName_key().equals(MATKL)) {

										int consumption = itemModel.getConsumption();
										// if (consumption > 0)
										// model.setSelectcConsumption(consumption);
										// model.setConsumption(consumption);
										model.setSelectcConsumption(consumption);
										// model.setConsumption(0);
										model.setCheck(true);
										break;
									}
								}
							}

							for (MaintenanceItemModel maintenanceItemModel : mTotalLastItemModels) {
								if (maintenanceItemModel.getName().equals(model.getName())) {
									if (maintenanceItemModel.getMaintenanceGroupModel().getName_key().equals(MATKL)) {

										int consumption = maintenanceItemModel.getConsumption();
										// if (consumption > 0)
										// model.setSelectcConsumption(consumption);
										model.setSelectcConsumption(consumption);
										// model.setConsumption(0);
										// model.setCheck(true);
										break;
									}
								}

							}

							arrayList.add(model);

							Set<String> set = mPartsMap.keySet();
							Iterator<String> it = set.iterator();
							String key;

							while (it.hasNext()) {
								key = it.next();
								for (int j = 0; j < mPartsMap.get(key).size(); j++) {
									kog.e("Jonathan","윤승아 444 mPartsMap 어디보자~! key ===  " + key + "    MTQTY  === "
													+ mPartsMap.get(key).get(j).getMTQTY() + "    MATNR  === "
													+ mPartsMap.get(key).get(j).getMATNR());
								}
							}

							if (mPartsMap.containsKey(MATKL)) {
								ArrayList<PartsMasterModel> partsMasterModels = mPartsMap.get(MATKL);

								for (PartsMasterModel partsMasterModel : partsMasterModels) {
									if (partsMasterModel.getMATNR().equals(model.getMATNR())) {
										tempList.add(model);

									}
								}
							}
						}
					}
				}

				Iterator<String> it = tempGroupMap.keySet().iterator();

				while (it.hasNext())

				{
					String strKey = "";

					strKey = it.next();

					ArrayList<MaintenanceItemModel> arrayList = tempGroupMap.get(strKey);
					if (arrayList.size() > 0) {
						mGroupMap.remove(strKey);
						mGroupMap.put(strKey, arrayList);
					}
				}

				for (MaintenanceGroupModel groupModel : mGroupLArrayList) {
					String matkl = groupModel.getName_key();
					if (mGroupMap.containsKey(matkl)) {
						ArrayList<MaintenanceItemModel> arrayList = mGroupMap.get(matkl);
						for (MaintenanceItemModel maintenanceItemModel : arrayList) {
							maintenanceItemModel.setMaintenanceGroupModel(groupModel);
						}
					}
				}
				tempGroupMap.clear();

				initItem();
				return;
			}
			kog.e("hjt", "not return");

			mCursor.moveToFirst();

			if(mCursor != null) {
				for (int i = 0; i < mCursor.getColumnCount(); i++) {
					kog.e("Jonathan", "mCursor.size() :: " + mCursor.getCount() + mCursor.getColumnName(i));
				}
			}

			// String firstMATKL = "";

			if(mCursor != null) {
				while (!mCursor.isAfterLast()) {
					int stock = 0;
					try {
						stock = Integer.parseInt(mCursor.getString(3));
					} catch (Exception e) {
						e.printStackTrace();
					}

					kog.e("KDH", "getString(3) = " + mCursor.getString(3));

					kog.e("Jonathan", "cursor 에 대해서  " + mCursor.getCount() + " 0 :: " + mCursor.getString(0) + " 1 :: "
							+ mCursor.getString(1) + " 2 :: " + mCursor.getString(2) + " 3 :: " + mCursor.getString(3)
							+ " 4 :: " + mCursor.getString(4) + " 5 :: " + mCursor.getString(5));

					kog.e("KDH", "CAR CD = " + mCarInfoModel.getMdlcd() + "   NAME = " + mCursor.getString(2) + "   수량 = "
							+ Integer.parseInt(mCursor.getString(3)));

					// Jonathan 14.09.11 재고가 0일때에도 보여지게 함. >= 0 을 > 으로하면 0건 안나옴.
					if (stock >= 0) {
						String MATKL = mCursor.getString(mCursor.getColumnIndex("MATKL"));

						if (mGroupMap.containsKey(MATKL)) {
							ArrayList<MaintenanceItemModel> arrayList = mGroupMap.get(MATKL);

							ArrayList<MaintenanceItemModel> tempList = tempGroupMap.get(MATKL);

							String MTQTY = "";
							String MINQTY = "";
							String MAXQTY = "";
							String NETPR = "";
							String ACTGRP = "";
							if (mPartsMap.containsKey(MATKL)) {
								ArrayList<PartsMasterModel> partsMasterModels = mPartsMap.get(MATKL);
								for (PartsMasterModel partsMasterModel : partsMasterModels) {
//								String mMATNR = mCursor.getString(5);

									for (int i = 0; i < mCursor.getColumnCount(); i++) {
										kog.e("Jonathan", "mCursor.size() :: " + mCursor.getCount() + "  " + mCursor.getColumnName(i) + "  " + i + "  " + mCursor.getString(i));
										if ("MATNR".equals(mCursor.getColumnName(i))) {
											mMATNR = mCursor.getString(i);
											kog.e("Jonathan", "mCursor 12:: " + mMATNR + mCursor.getString(i));
										}
									}

									if (partsMasterModel.getMATNR().equals(mMATNR)
											&& mCarInfoModel.getMdlcd().equals(partsMasterModel.getMDLCD())
											&& mCarInfoModel.getOilType().equals(partsMasterModel.getFUELCD()))

									{
										MTQTY = partsMasterModel.getMTQTY().trim();
										if (partsMasterModel.getMINQTY() != null) {
											MINQTY = partsMasterModel.getMINQTY().trim();
										}

										if (partsMasterModel.getMAXQTY() != null) {
											MAXQTY = partsMasterModel.getMAXQTY().trim();
										}

										if (partsMasterModel.getNETPR() != null) {
											NETPR = partsMasterModel.getNETPR().trim();
										}

										if (partsMasterModel.getACTGRP() != null) {
											ACTGRP = partsMasterModel.getACTGRP().trim();
										}

										kog.e("KDH", "AUAU MTQTY = " + MTQTY);
										kog.e("KDH", "AUAU MINQTY = " + MINQTY);
										kog.e("KDH", "AUAU MAXQTY = " + MAXQTY);
										kog.e("yunseung", "AUAU NETPR = " + NETPR);
										kog.e("yunseung", "AUAU ACTGRP = " + ACTGRP);
									}
								}
							}

							MaintenanceItemModel model = new MaintenanceItemModel(
									mCursor.getString(mCursor.getColumnIndex("MAKTX")),
									Integer.parseInt(mCursor.getString(mCursor.getColumnIndex("LABST"))),
									mCursor.getString(mCursor.getColumnIndex("MATNR")),
									mCursor.getString(mCursor.getColumnIndex("MEINS")), null,
									mCursor.getString(mCursor.getColumnIndex("GRP_CD")), MTQTY, MINQTY, MAXQTY, NETPR, ACTGRP);

							for (MaintenanceItemModel itemModel : mLastItemModels) {
								if (itemModel.getName().equals(model.getName())) {
									if (itemModel.getMaintenanceGroupModel().getName_key().equals(MATKL)) {

										int consumption = itemModel.getConsumption();
										// if (consumption > 0)
										// model.setSelectcConsumption(consumption);
										// model.setConsumption(consumption);
										model.setSelectcConsumption(consumption);
										// model.setConsumption(0);
										model.setCheck(true);
										break;
									}
								}
							}

							for (MaintenanceItemModel maintenanceItemModel : mTotalLastItemModels) {
								if (maintenanceItemModel.getName().equals(model.getName())) {
									if (maintenanceItemModel.getMaintenanceGroupModel().getName_key().equals(MATKL)) {

										int consumption = maintenanceItemModel.getConsumption();
										// if (consumption > 0)
										// model.setSelectcConsumption(consumption);
										model.setSelectcConsumption(consumption);
										// model.setConsumption(0);
										// model.setCheck(true);
										break;
									}
								}

							}

							arrayList.add(model);

							Set<String> set = mPartsMap.keySet();
							Iterator<String> it = set.iterator();
							String key;

							while (it.hasNext()) {
								key = it.next();
								for (int i = 0; i < mPartsMap.get(key).size(); i++) {
//									kog.e("Jonathan",
//											"mPartsMap 어디보자~! key ===  " + key + "    MTQTY  === "
//													+ mPartsMap.get(key).get(i).getMTQTY() + "    MATNR  === "
//													+ mPartsMap.get(key).get(i).getMATNR());
								}
							}

							if (mPartsMap.containsKey(MATKL)) {
								ArrayList<PartsMasterModel> partsMasterModels = mPartsMap.get(MATKL);

								for (PartsMasterModel partsMasterModel : partsMasterModels) {
									if (partsMasterModel.getMATNR().equals(model.getMATNR())) {
										tempList.add(model);
									}
								}
							}
						}
					}

					mCursor.moveToNext();

				}
				mCursor.close();
			}

			Iterator<String> it = tempGroupMap.keySet().iterator();

			while (it.hasNext()) {
				String strKey = "";

				strKey = it.next();

				ArrayList<MaintenanceItemModel> arrayList = tempGroupMap.get(strKey);
				if (arrayList.size() > 0) {
					mGroupMap.remove(strKey);
					//TODO 윤승 여기가 그룹맵에 하위 항목들 넣어주는 곳
					mGroupMap.put(strKey, arrayList);
				}
			}

			for (MaintenanceGroupModel groupModel : mGroupLArrayList) {
				String matkl = groupModel.getName_key();
				if (mGroupMap.containsKey(matkl)) {
					ArrayList<MaintenanceItemModel> arrayList = mGroupMap.get(matkl);
					for (MaintenanceItemModel maintenanceItemModel : arrayList) {
						maintenanceItemModel.setMaintenanceGroupModel(groupModel);
					}
				}
			}
			tempGroupMap.clear();
			initApointPart();
			initItem();
			// initQueryItem2();
		}
	}

	// PARTSMASTER_TABLE ( MATKL, GRP_CD, MAKTX, LABST, MEINS MATNR, LTEXT,
	// BUKRS )
	private class ItemSetRunnable2 implements Runnable {

		private Cursor mCursor;

		public ItemSetRunnable2(Cursor cursor) {
			mCursor = cursor;
		}

		@Override
		public void run() {
			// TODO Auto-generated method stub
			mCursor.moveToFirst();

			for (int i = 0; i < mCursor.getColumnCount(); i++) {
				kog.e("Jonathan", "mCursor.size() :: " + mCursor.getCount() + mCursor.getColumnName(i));
			}

			while (!mCursor.isAfterLast()) {
				String MATKL = mCursor.getString(mCursor.getColumnIndex("MATKL"));
				String GRP_CD = mCursor.getString(mCursor.getColumnIndex("GRP_CD"));
				String MDLCD = mCursor.getString(mCursor.getColumnIndex("MDLCD"));
				String FUELCD = mCursor.getString(mCursor.getColumnIndex("FUELCD"));
				String MTQTY = mCursor.getString(mCursor.getColumnIndex("MTQTY"));
				String MINQTY = mCursor.getString(mCursor.getColumnIndex("MINQTY"));
				String MAXQTY = mCursor.getString(mCursor.getColumnIndex("MAXQTY"));

				// kog.e("KDH", "MATKL = "+MATKL);
				// kog.e("KDH", "GRP_CD = "+GRP_CD);
				// kog.e("KDH", "MDLCD = "+MDLCD);
				// kog.e("KDH", "FUELCD = "+FUELCD);
				// kog.e("KDH", "MTQTY = "+MTQTY);

				for (MaintenanceItemModel itemModel : mLastItemModels) {
					if (GRP_CD.equals(itemModel.getGRP_CD()) && mCarInfoModel.getMDLCD().equals(MDLCD)) {
						itemModel.setMTQTY(MTQTY);
						itemModel.setMINQTY(MINQTY);
						itemModel.setMAXQTY(MAXQTY);
						kog.e("KDH", "IN MTQTY = " + MTQTY);
					}
				}

				if (mGroupMap.containsKey(MATKL)) {
					ArrayList<MaintenanceItemModel> arrayList = mGroupMap.get(MATKL);

					/*
					 * ArrayList<MaintenanceItemModel> tempList = tempGroupMap
					 * .get(MATKL);
					 *
					 * MaintenanceItemModel model = new MaintenanceItemModel(
					 * mCursor.getString(2), Integer.parseInt(mCursor
					 * .getString(3)), mCursor.getString(5),
					 * mCursor.getString(4), null, mCursor.getString(1));
					 *
					 *
					 * for (MaintenanceItemModel itemModel : mLastItemModels) {
					 * if (itemModel.getName().equals(model.getName())) { if
					 * (itemModel.getMaintenanceGroupModel().getName_key().
					 * equals(MATKL)) { int consumption =
					 * itemModel.getConsumption();
					 * model.setSelectcConsumption(consumption);
					 * model.setCheck(true); break; } } }
					 *
					 * for (MaintenanceItemModel maintenanceItemModel :
					 * mTotalLastItemModels) { if
					 * (maintenanceItemModel.getName().equals( model.getName()))
					 * { if (maintenanceItemModel.getMaintenanceGroupModel()
					 * .getName_key().equals(MATKL)) {
					 *
					 * int consumption = maintenanceItemModel .getConsumption();
					 * // if (consumption > 0) //
					 * model.setSelectcConsumption(consumption);
					 * model.setSelectcConsumption(consumption); //
					 * model.setConsumption(0); // model.setCheck(true); break;
					 * } } }
					 *
					 * arrayList.add(model);
					 *
					 * Set <String> set = mPartsMap.keySet(); Iterator <String>
					 * it = set.iterator(); String key;
					 *
					 * while(it.hasNext()) { key = it.next(); for(int i = 0 ; i
					 * < mPartsMap.get(key).size() ; i++ ) { kog.e("Jonathan",
					 * "mPartsMap 어디보자~! key ===  " + key + "    MTQTY  === " +
					 * mPartsMap.get(key).get(i).getMTQTY() + "    MATNR  === "
					 * + mPartsMap.get(key).get(i).getMATNR()); } }
					 *
					 * if (mPartsMap.containsKey(MATKL)) {
					 * ArrayList<PartsMasterModel> partsMasterModels = mPartsMap
					 * .get(MATKL);
					 *
					 * for (PartsMasterModel partsMasterModel :
					 * partsMasterModels) { if
					 * (partsMasterModel.getMATNR().equals( model.getMATNR())) {
					 * tempList.add(model); } } }
					 */
				}

				mCursor.moveToNext();
			}
			mCursor.close();

			Iterator<String> it = tempGroupMap.keySet().iterator();

			while (it.hasNext()) {
				String strKey = "";

				strKey = it.next();

				ArrayList<MaintenanceItemModel> arrayList = tempGroupMap.get(strKey);
				if (arrayList.size() > 0) {
					mGroupMap.remove(strKey);
					mGroupMap.put(strKey, arrayList);
				}
			}

			for (MaintenanceGroupModel groupModel : mGroupLArrayList) {
				String matkl = groupModel.getName_key();
				if (mGroupMap.containsKey(matkl)) {
					ArrayList<MaintenanceItemModel> arrayList = mGroupMap.get(matkl);
					for (MaintenanceItemModel maintenanceItemModel : arrayList) {
						maintenanceItemModel.setMaintenanceGroupModel(groupModel);
					}
				}
			}
			tempGroupMap.clear();
			initApointPart();
			initItem();
			// initQueryItem2();
		}
	}

	private void responseGroup(Cursor cursor) {
		cursor.moveToFirst();

		for (int i = 0; i < cursor.getColumnCount(); i++) {

			kog.e("Jonathan", "Group mCursor.size() :: " + cursor.getCount() + cursor.getColumnName(i));
		}

		MaintenanceGroupModel maintenanceGroupModel = null;

		mGroupLArrayList.clear();

		while (!cursor.isAfterLast()) {

			maintenanceGroupModel = new MaintenanceGroupModel(cursor.getString(0), cursor.getString(1));
			mGroupLArrayList.add(maintenanceGroupModel);
			cursor.moveToNext();

		}
		cursor.close();

		mGroupAdapter.setData(mGroupLArrayList);

		// Jonahtna 2014.08.26 다시 관련 품목만 보이게 한다.
		// myung 20131230 UPDATE 임시 정비항목 전부 노출을 위해 함수 대체
		initPartType(false);
		// initPartType2();

		hideProgress();

	}

	private void setGroupList() {
		if (mGroupLArrayList.size() > 0) {

			ArrayList<MaintenanceItemModel> tempArr = null;
			for (int i = 0; i < mGroupLArrayList.size(); i++) {
				tempArr = new ArrayList<MaintenanceItemModel>();
				mGroupMap.put(mGroupLArrayList.get(i).getName_key(), tempArr);
				ArrayList<MaintenanceItemModel> tempArr2 = new ArrayList<MaintenanceItemModel>();
				tempGroupMap.put(mGroupLArrayList.get(i).getName_key(), tempArr2);
				if (i == 0)
					mItemModels = tempArr;
			}
			initQueryItem();
			hideProgress();
		}
	}

	private void responsePartsType(Cursor cursor) {
		kog.e("Jonathan", "관련품목  responsePartsType");
		if (cursor == null)
			return;

		for (int i = 0; i < cursor.getColumnCount(); i++) {

			kog.e("Jonathan", "partstype mCursor.size() :: " + cursor.getCount() + cursor.getColumnName(i));
		}

		// myung 20131125 장비항목입력 리스트 생성 시에 같은 항목 증가하는 오류
		mPartsMap.clear();
		cursor.moveToFirst();

		while (!cursor.isAfterLast()) {
			String MATNR = cursor.getString(cursor.getColumnIndex("MATNR"));
			mMATNR.add(MATNR);
			String MATKL = cursor.getString(cursor.getColumnIndex("MATKL"));
			String MTQTY = cursor.getString(cursor.getColumnIndex("MTQTY"));
			String MINQTY = cursor.getString(cursor.getColumnIndex("MINQTY"));
			String MAXQTY = cursor.getString(cursor.getColumnIndex("MAXQTY"));

			kog.e("KDH", "KKK MATNR = " + MATNR + " || 부품명 = " + MATKL + " | 수량 = " + MTQTY + " || 최소수량 = " + MINQTY + " || 최대수량 = " + MAXQTY);

			// kog.e("KDH", "KKK GRP_CD = "+GRP_CD);

			PartsMasterModel model = new PartsMasterModel(MATNR, MATKL, MTQTY, mCarInfoModel.getOilType(),
					mCarInfoModel.getMdlcd(), MINQTY, MAXQTY, null, null);

			if (mPartsMap.containsKey(MATKL)) {
				ArrayList<PartsMasterModel> arrayList = mPartsMap.get(MATKL);
				arrayList.add(model);
			} else {
				ArrayList<PartsMasterModel> arrayList = new ArrayList<PartsMasterModel>();
				arrayList.add(model);
				mPartsMap.put(MATKL, arrayList);
			}
			cursor.moveToNext();
		}
		cursor.close();
		setGroupList();
		hideProgress();

	}

	private void responsePartsTypeForIoT(ArrayList<HashMap<String, String>> array_hash) {
		kog.e("Jonathan", "관련품목  responsePartsType");
		if (array_hash == null)
			return;


		// myung 20131125 장비항목입력 리스트 생성 시에 같은 항목 증가하는 오류
		mPartsMap.clear();

		for (int i = 0; i < array_hash.size(); i++) {
			String MATNR = array_hash.get(i).get("MATNR");
			mMATNR.add(MATNR);
			String MATKL = array_hash.get(i).get("MATKL");
			String MTQTY = array_hash.get(i).get("MTQTY");
			String NETPR = array_hash.get(i).get("NETPR");
			String ACTGRP = array_hash.get(i).get("ACTGRP");

			PartsMasterModel model = new PartsMasterModel(MATNR, MATKL, MTQTY, mCarInfoModel.getOilType(),
					mCarInfoModel.getMdlcd(), null, null, NETPR, ACTGRP);

			if (mPartsMap.containsKey(MATKL)) {
				ArrayList<PartsMasterModel> arrayList = mPartsMap.get(MATKL);
				arrayList.add(model);
			} else {
				ArrayList<PartsMasterModel> arrayList = new ArrayList<PartsMasterModel>();
				arrayList.add(model);
				mPartsMap.put(MATKL, arrayList);
			}
		}

		setGroupList();
		hideProgress();

	}

	ArrayList<String> mMATNR = new ArrayList<String>();

	private void initQueryItem() {

		// String[] _whereArgs = null;
		// String[] _whereCause = null;
		String where[] = new String[mMATNR.size()];
		String whereC[] = new String[mMATNR.size()];
		for (int i = 0; i < where.length; i++) {
			where[i] = mMATNR.get(i);
			whereC[i] = DEFINE.MATNR;
		}

		String[] _whereArgs = where;
		String[] _whereCause = whereC;

		String[] colums = null;

		DbQueryModel dbQueryModel = new DbQueryModel(DEFINE.STOCK_TABLE_NAME, _whereCause, _whereArgs, colums);

		dbQueryModel.setOrderBy(DEFINE.MATKL + " asc");

		DbAsyncTask dbAsyncTask = new DbAsyncTask("initQueryItem", mContext, this, dbQueryModel);
		dbAsyncTask.execute(DbAsyncTask.DB_SELECT);

	}

	private void initQueryItem2() {

		// String[] _whereArgs = null;
		// String[] _whereCause = null;

		String where[] = new String[mMATNR.size()];
		String whereC[] = new String[mMATNR.size()];
		for (int i = 0; i < where.length; i++) {
			where[i] = mMATNR.get(i);
			whereC[i] = DEFINE.MATNR;
		}

		String[] _whereArgs = where;
		String[] _whereCause = whereC;

		String[] colums = null;

		DbQueryModel dbQueryModel = new DbQueryModel(DEFINE.PARTSMASTER_TABLE_NAME, _whereCause, _whereArgs, colums);

		dbQueryModel.setOrderBy(DEFINE.MATKL + " asc");

		DbAsyncTask dbAsyncTask = new DbAsyncTask("initQueryItem2", mContext, this, dbQueryModel);
		dbAsyncTask.execute(DbAsyncTask.DB_SELECT);

	}

	@Override
	public void OnSeletedItem(Object item) {
		MaintenanceGroupModel groupModel = (MaintenanceGroupModel) item;
		mCurrentGroupModel = groupModel;
		initScrollPosition();
		queryItem(groupModel);

	}

	private void initScrollPosition() {

		mLvItem.smoothScrollToPosition(0);
		mItemAdapter.notifyDataSetChanged();

	}

	@Override
	public void dismiss() {
		// TODO Auto-generated method stub
		// mLastItemModels.clear();
		// FragmentTransaction transaction = getFragmentManager()
		// .beginTransaction();
		// transaction.detach(this);
		// transaction.commit();
		// getChildFragmentManager().executePendingTransactions();
		// onDetach();
		getDialog().dismiss();
		super.dismiss();
	}

	@Override
	public void onStart() {
		super.onStart();
		getDialog().setCanceledOnTouchOutside(false);
	}

	@Override
	public void onCancelConsumption(String matkl, String matnr) {
		// TODO Auto-generated method stub
		if(mLastItemModels != null) {
			for (MaintenanceItemModel model : mLastItemModels) {
				if(model != null && model.getMATNR() != null && mLastItemAdapter != null) {
					if (model.getMATNR().equals(matnr)) {
						mLastItemModels.remove(model);
						mLastItemAdapter.setData(mLastItemModels);
						mLastItemAdapter.notifyDataSetChanged();
						return;
					}
				}
			}
		}

	}

	@Override
	public void onAddConsumption(String matkl, String matnr, int inQty) {
		// TODO Auto-generated method stub
		//2014-12-04 KDH 여기에서처리해야하는데 힘드네.-.-
		ArrayList<MaintenanceItemModel> models = mItemAdapter
				.getSelectedMaintenanceModels();
		String light_bulb = mContext.getString(R.string.light_bulb);
		String washer_liquid = mContext.getString(R.string.washer_liquid);
		String brush = mContext.getString(R.string.brush);
		for (MaintenanceItemModel maintenanceItemModel : models)
		{
			kog.e("KDH", "maintenanceItemModel.getName() = "+maintenanceItemModel.getName());
			kog.e("Jonathan", "washer_liquid = "+washer_liquid);
			kog.e("Jonathan", "brush = " + brush);
			kog.e("KDH", "light_bulb = "+light_bulb);
			kog.e("KDH", "matkl = "+matkl);
			if((!maintenanceItemModel.getName().contains(light_bulb)) && (!maintenanceItemModel.getName().contains(brush)))
			{// && (!maintenanceItemModel.getName().contains(washer_liquid))
				kog.e("Jonathan", "여기 들어오면 안되는데?");
				String mtqty = maintenanceItemModel.getMTQTY().trim();
				if(mtqty == null || mtqty.equals("")){
					mtqty = "1";
				}
				int MTQTY = Integer.parseInt(mtqty);
				if(inQty > MTQTY)
				{
					if(!maintenanceItemModel.getName().contains("워셔액") && !maintenanceItemModel.getName().contains(washer_liquid)
							&& !maintenanceItemModel.getName().contains("가솔린") && !maintenanceItemModel.getName().contains("디젤"))
					{
						//2014-12-04 KDH 현재갯수보다불라불라
						EventPopup2 eventPopup = new EventPopup2(
								mContext, "" + "최대수량은 "+ MTQTY +" 입니다.", null);
						eventPopup.show();
						mItemAdapter.initSelectedMaintenanceArray();
						mLastItemAdapter.notifyDataSetChanged();
						return;
					}
				}
				kog.e("KDH", "MTQTY = "+MTQTY);
				kog.e("KDH", "inQty = "+inQty);
				for (int i = 0; i < mLastItemModels.size(); i++)
				{
					MaintenanceItemModel lastModel = mLastItemModels.get(i);
					kog.e("Jonathan", "Jonathan lastModel :: " + lastModel.getName() + " 갯수 :: " + lastModel.getConsumption());
					if(maintenanceItemModel.getName().contains(washer_liquid) || maintenanceItemModel.getName().contains("워셔액"))
					{
						if(lastModel.getName().contains(washer_liquid) || lastModel.getName().contains("워셔액"))
						{
							int washerNQty = lastModel.getConsumption();
							kog.e("Jonathan", "Jonathan washer :: " + washerNQty);
							if(washerNQty+inQty >2)
							{
								EventPopup2 eventPopup = new EventPopup2(mContext, "동일재고가있고 최대수량은 "+"2"+" 입니다.", null);
								eventPopup.show();
								mItemAdapter.initSelectedMaintenanceArray();
								mLastItemAdapter.notifyDataSetChanged();
								return;
							}
						}
					}
				}
				for (int i = 0; i < mLastItemModels.size(); i++)
				{
					MaintenanceItemModel lastModel = mLastItemModels.get(i);
					if(maintenanceItemModel.getName().contains(washer_liquid) || maintenanceItemModel.getName().contains("워셔액"))
					{
						//	for(int j  = 0 ; j > mLastItemModels.size() ; j++)
						//	{
						//	MaintenanceItemModel forWasher = mLastItemModels.get(j);
						//
						//	int washerNQty = forWasher.getConsumption();
						//	kog.e("Jonathan", "Jonathan washer :: " + washerNQty);
						//
						//	if(washerNQty >2)
						//	{
						//	EventPopup2 eventPopup = new EventPopup2(mContext, "동일재고가있고 최대수량은 "+"2"+" 입니다.", null);
						//	eventPopup.show();
						//	mItemAdapter.initSelectedMaintenanceArray();
						//	mLastItemAdapter.notifyDataSetChanged();
						//	return;
						//	}
						//
						//	}
					}
					else
					{
						if(lastModel.getName().equals(maintenanceItemModel.getName()))
						{
							kog.e("KDH", "lastModel.getName() = "+lastModel.getName());
							int nQty = lastModel.getConsumption();
							kog.e("KDH", "nQty  = "+nQty);
							if((nQty+inQty) > MTQTY)
							{
								EventPopup2 eventPopup = new EventPopup2(mContext, "동일재고가있고 최대수량은 "+MTQTY+" 입니다.", null);
								eventPopup.show();
								mItemAdapter.initSelectedMaintenanceArray();
								mLastItemAdapter.notifyDataSetChanged();
								return;
							}
						}
					}
				}
			}
			/*
for (int i = 0; i < mLastItemModels.size(); i++) 
{
MaintenanceItemModel lastModel = mLastItemModels.get(i);
if (lastModel.getName().equals(maintenanceItemModel.getName())) 
{
//2014-12-04 KDH 동일재고가있고, 최대수량과 현재수량이 오버되면 안된다고함.
int MTQTY = Integer.parseInt(maintenanceItemModel.getMTQTY().trim());
int nQty = lastModel.getConsumption();
if(nQty > MTQTY)
{
EventPopup2 eventPopup = new EventPopup2(
mContext, "" + "동일재고가있고, 최대수량과 현재수량이 오버되면 안된다."+MTQTY+" 입니다.", null);
eventPopup.show();
}
//	Toast.makeText(getActivity(), "동일재고있으니 다시해라", Toast.LENGTH_LONG).show();
return;
}
}
			 */
		}
		clickAdd();
		initLastEmpty();
	}


	@Override
	public void onEndNewView() {
		// TODO Auto-generated method stub
		hideProgress();
	}

	private void initItemEmpty(ArrayList<MaintenanceItemModel> arr) {
		if (arr.isEmpty()) {
			mIvItemEmpty.setVisibility(View.VISIBLE);
			mLvItem.setVisibility(View.GONE);
		} else {
			mIvItemEmpty.setVisibility(View.GONE);
			mLvItem.setVisibility(View.VISIBLE);
		}
	}

	private void initLastEmpty() {
		if (mLastItemModels.isEmpty()) {
			mIvLastEmpty.setVisibility(View.VISIBLE);
			mLvLast.setVisibility(View.GONE);
		} else {
			mIvLastEmpty.setVisibility(View.GONE);
			mLvLast.setVisibility(View.VISIBLE);
		}
	}
}
