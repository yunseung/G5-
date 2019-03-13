package com.ktrental.fragment;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.ktrental.R;
import com.ktrental.adapter.MaintenaceParts_Order_Adapter;
import com.ktrental.beans.PM023;
import com.ktrental.cm.connect.ConnectController;
import com.ktrental.cm.connect.Connector;
import com.ktrental.cm.connect.Connector.ConnectInterface;
import com.ktrental.cm.db.DbAsyncTask;
import com.ktrental.cm.db.DbAsyncTask.DbAsyncResLintener;
import com.ktrental.common.DEFINE;
import com.ktrental.common.KtRentalApplication;
import com.ktrental.model.DbQueryModel;
import com.ktrental.model.LoginModel;
import com.ktrental.model.TableModel;
import com.ktrental.popup.DatepickPopup2;
import com.ktrental.popup.EventPopupC;
import com.ktrental.popup.InventoryPopup;
import com.ktrental.popup.InventoryPopup.OnDismissListener;
import com.ktrental.popup.MaintenancePartsReturnPopup;
import com.ktrental.popup.Popup_Window_M028;
import com.ktrental.popup.Popup_Window_PM023;
import com.ktrental.popup.Popup_Window_PM073;
import com.ktrental.popup.Popup_Window_Return;
import com.ktrental.popup.QuantityPopup;
import com.ktrental.popup.QuickAction;
import com.ktrental.util.OnChangeFragmentListener;
import com.ktrental.util.kog;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

public class MaintenancePartsFragment extends BaseFragment implements
		OnClickListener, ConnectInterface {

	private Connector connector;
	private Context context;
	// private ProgressDialog pd;
	private String TABLE_NAME = "O_ITAB1";
	private Button bt_date1;
	private Button bt_date2;
	private Button bt_group;
	private Button bt_search;
	private Button bt_done;
	private DatepickPopup2 idatepickpopup;
	// private PM023_Popup ipm023popup;
	private Popup_Window_PM023 pwPM023;
	private Popup_Window_M028 pwM028;
	private Popup_Window_Return pwRETURN;
	private Popup_Window_PM073 pwPM073;
	private String M028_TAG;
	private String RETURN_TAG;
	private String PM073_TAG;
	private ListView lv_order;
	private InventoryPopup mPopupWindow;
	public static String RELEASED = "40";
	public static String STOCKED = "31";
	private MaintenancePartsReturnPopup mprpopup;
	private ImageView iv_nodata;
	private int CHOICED = -1;

	private Button bt_progress;

	public MaintenancePartsFragment(){
	}

	public MaintenancePartsFragment(String className,
			OnChangeFragmentListener changeFragmentListener) {
		super(className, changeFragmentListener);
	}

	@Override
	public void onAttach(Activity activity) {
		context = activity;
		connector = new Connector(this, context);
		// pd = new ProgressDialog(context);
		// pd.setCancelable(false);
		M028_TAG = " ";
		RETURN_TAG = "3";
		PM073_TAG = "31";
		mPopupWindow = new InventoryPopup(context, QuantityPopup.HORIZONTAL,
				R.layout.inventory_popup, QuantityPopup.TYPE_PHONE_NUMBER);
		mPopupWindow.setOnDismissListener(new OnDismissListener() {
			@Override
			public void onDismiss(String result, int position) {
				if (!mPopupWindow.isDONE()) {
					// 되돌리기.
					EventPopupC epc = new EventPopupC(context);
					epc.show("취소되었습니다.");
					ZMO_1030_RD04 rld = zmo_1030_rd04_arr.get(CHOICED);
					rld.INC_QTY = "0";
					rld.UNINC_QTY = "0";
					rld.INC_DATE = " ";
					if (MaintenaceParts_Order_Adapter.checked_items
							.contains(CHOICED)) {
						MaintenaceParts_Order_Adapter.checked_items
								.remove(MaintenaceParts_Order_Adapter.checked_items
										.indexOf(CHOICED));
					}
					mpoa.notifyDataSetChanged();
				}
			}
		});
		// ipm023popup = new PM023_Popup(context, QuickAction.VERTICAL,
		// initPM023());
		pwPM023 = new Popup_Window_PM023(context);
		pwM028 = new Popup_Window_M028(context);
		pwRETURN = new Popup_Window_Return(context);
		pwPM073 = new Popup_Window_PM073(context);
		// idatepickpopup = new DatepickPopup2(context);
		mprpopup = new MaintenancePartsReturnPopup(context,
				QuickAction.VERTICAL, R.layout.maintenance_parts_return_popup);
		mprpopup.getmWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE
						| WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
		super.onAttach(activity);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.maintenance_parts_layout, null);
		bt_date1 = (Button) v
				.findViewById(R.id.inventory_maintenance_datepick1_id);
		bt_date2 = (Button) v
				.findViewById(R.id.inventory_maintenance_datepick2_id);
		bt_group = (Button) v
				.findViewById(R.id.inventory_maintenance_group_popup_id);
		bt_progress = (Button) v
				.findViewById(R.id.inventory_maintenance_progress_popup_id);
		bt_search = (Button) v
				.findViewById(R.id.inventory_maintenance_parts_getlist_id);
		bt_done = (Button) v.findViewById(R.id.inventory_maintenance_done_id);
		bt_date1.setOnClickListener(this);
		bt_date2.setOnClickListener(this);
		bt_group.setOnClickListener(this);
		bt_progress.setOnClickListener(this);
		bt_search.setOnClickListener(this);
		bt_done.setOnClickListener(this);
		setDateButton();
		setM028Click();
		setPM073Click();
		// setRETURNClick();
		lv_order = (ListView) v
				.findViewById(R.id.inventory_maintenance_parts_list_id);
		iv_nodata = (ImageView) v.findViewById(R.id.list_nodata_id);
		mRootView = v;
		return v;
	}

	@Override
	public void onHiddenChanged(boolean hidden) {
		super.onHiddenChanged(hidden);
		if (hidden) {
			return;
		}

		lv_order.setAdapter(null);
	}

	private void setM028Click() {
		ViewGroup vg = pwM028.getViewGroup();
		LinearLayout back = (LinearLayout) vg.getChildAt(0);
		for (int i = 0; i < back.getChildCount(); i++) {
			LinearLayout row_back = (LinearLayout) back.getChildAt(i);
			final Button bt = (Button) row_back.getChildAt(0);
			bt.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					bt_group.setText(bt.getText().toString());
					M028_TAG = bt.getTag().toString();
					pwM028.dismiss();
				}
			});
		}
	}

	private void setPM073Click() {
		ViewGroup vg = pwPM073.getViewGroup();
		LinearLayout back = (LinearLayout) vg.getChildAt(0);
		for (int i = 0; i < back.getChildCount(); i++) {
			LinearLayout row_back = (LinearLayout) back.getChildAt(i);
			final Button bt = (Button) row_back.getChildAt(0);
			bt.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// Toast.makeText(context, bt.getTag().toString() + "테그입니다",
					// 0) .show();
					bt_progress.setText(bt.getText().toString());
					PM073_TAG = bt.getTag().toString();
					pwPM073.dismiss();
				}
			});
		}
	}

	private void setDateButton() {
		Calendar cal = Calendar.getInstance();
		int year = cal.get(Calendar.YEAR);
		int month = cal.get(Calendar.MONTH) + 1;
		int day = cal.get(Calendar.DAY_OF_MONTH);
		String month_str = String.format("%02d", month);
		String day_str = String.format("%02d", day);
		// DATE2 = year + month_str + day_str;
		bt_date2.setText(year + "." + month_str + "." + day_str);// 오늘날짜
		cal.set(Calendar.DAY_OF_MONTH, 1);
		day = cal.get(Calendar.DAY_OF_MONTH);
		String day_str_1 = String.format("%02d", day);
		// DATE1 = year + month_str + day_str_1;
		bt_date1.setText(year + "." + month_str + "." + day_str_1);// 이달의첫날
	}

	ArrayList<PM023> pm023_arr;

	private ArrayList<PM023> initPM023() {
		pm023_arr = new ArrayList<PM023>();
		PM023 pm = new PM023();
		pm.ZCODEV = " ";
		pm.ZCODEVT = "전체";
		pm023_arr.add(pm);
		String path = context.getExternalCacheDir() + "/DATABASE/"
				+ DEFINE.SQLLITE_DB_NAME;
		SQLiteDatabase sqlite = SQLiteDatabase.openDatabase(path, null,
				SQLiteDatabase.OPEN_READONLY);
		Cursor cursor = sqlite.rawQuery("select ZCODEV, ZCODEVT from "
				+ TABLE_NAME + " where ZCODEH = 'PM023'", null);
		while (cursor.moveToNext()) {
			int calnum1 = cursor.getColumnIndex("ZCODEV");
			int calnum2 = cursor.getColumnIndex("ZCODEVT");
			String zcodev = cursor.getString(calnum1);
			String zcodevt = cursor.getString(calnum2);
			pm = new PM023();
			pm.ZCODEV = zcodev;
			pm.ZCODEVT = zcodevt;
			pm023_arr.add(pm);
		}
		cursor.close();
		// 2017.07.05. hjt. db close 는 app 종료시 알아서 된다
		// 이것때문에 throwIfClosedLocked Exception 발생한다
//		sqlite.close();
		// 출력테스트
		for (int i = 0; i < pm023_arr.size(); i++) {
			PM023 key = pm023_arr.get(i);
		}
		return pm023_arr;
	}

	ArrayList<ZMO_1030_RD04> zmo_1030_rd04_arr;
	MaintenaceParts_Order_Adapter mpoa;

	// private final String MAINTENANCE_PARTS_STOCKED_DONE = "ZMO_1030_WR04";
	// private final String MAINTENANCE_PARTS_STOCKED_DONE_TABLE_NAME =
	// "maintenance_parts_stocked_done_table";
	// private final String MAINTENANCE_PARTS_STOCKED = "ZMO_1030_RD04";
	// private final String MAINTENANCE_PARTS_STOCKED_TABLE_NAME =
	// "maintenance_parts_stocked_table";
	@Override
	public void connectResponse(String FuntionName, String resultText,
			String MTYPE, int resulCode, TableModel tableModel) {
		hideProgress();
//		Log.i("", "####connectResponse : " + FuntionName + "/" + resultText
//				+ "/" + resulCode + "/" + MTYPE);
		if (MTYPE == null || !MTYPE.equals("S")) {
			EventPopupC epc = new EventPopupC(context);
			epc.show(resultText);
			return;
		}

		if (FuntionName.equals(MAINTENANCE_PARTS_STOCKED)) {
			zmo_1030_rd04_arr = new ArrayList<ZMO_1030_RD04>();
			ArrayList<HashMap<String, String>> temp_arr = tableModel
					.getTableArray();
			if (temp_arr.size() > 0) {
				iv_nodata.setVisibility(View.GONE);
			} else {
				iv_nodata.setVisibility(View.VISIBLE);
			}
			for (int i = 0; i < temp_arr.size(); i++) {
				ZMO_1030_RD04 ps = new ZMO_1030_RD04();
				HashMap hm = temp_arr.get(i);
				Object data = hm.get("ZSTATUS");
				String encrypted = data != null ? data.toString() : " ";
				ps.ZSTATUS = encrypted;
				data = hm.get("ZSTATUST");
				encrypted = data != null ? data.toString() : " ";
				ps.ZSTATUST = encrypted;
				data = hm.get("EBELN");
				encrypted = data != null ? data.toString() : " ";
				ps.EBELN = encrypted;
				data = hm.get("EBELP");
				encrypted = data != null ? data.toString() : " ";
				ps.EBELP = encrypted;
				data = hm.get("MATKL");
				encrypted = data != null ? data.toString() : " ";
				ps.MATKL = encrypted;
				data = hm.get("WGBEZ");
				encrypted = data != null ? data.toString() : " ";
				ps.WGBEZ = encrypted;
				data = hm.get("MATNR");
				encrypted = data != null ? data.toString() : " ";
				ps.MATNR = encrypted;
				data = hm.get("MAKTX");
				encrypted = data != null ? data.toString() : " ";
				ps.MAKTX = encrypted;
				data = hm.get("MEINS");
				encrypted = data != null ? data.toString() : " ";
				ps.MEINS = encrypted;
				data = hm.get("EXP_DATE");
				encrypted = data != null ? data.toString() : " ";
				ps.EXP_DATE = encrypted;
				data = hm.get("EXP_QTY");
				encrypted = data != null ? data.toString() : " ";
				ps.EXP_QTY = encrypted;

				if (ps.ZSTATUS.equals("31")) // 출고완료
				{
					Calendar cal = Calendar.getInstance();
					int year = cal.get(Calendar.YEAR);
					int month = cal.get(Calendar.MONTH) + 1;
					int day = cal.get(Calendar.DAY_OF_MONTH);
					ps.INC_DATE = "" + year + month + day;
					ps.INC_QTY = encrypted;
				} else {
					data = hm.get("INC_DATE");
					encrypted = data != null ? data.toString() : " ";
					ps.INC_DATE = encrypted;
					data = hm.get("INC_QTY");
					encrypted = data != null ? data.toString() : " ";
					ps.INC_QTY = encrypted;
				}

				data = hm.get("UNINC_QTY");
				encrypted = data != null ? data.toString() : " ";
				ps.UNINC_QTY = encrypted;

				data = hm.get("RETEXT");
				encrypted = data != null ? data.toString() : " ";
				ps.RETEXT = encrypted;
				data = hm.get("MENGE");
				encrypted = data != null ? data.toString() : " ";
				ps.MENGE = encrypted;
				data = hm.get("WERKS");
				encrypted = data != null ? data.toString() : " ";
				ps.WERKS = encrypted;
				data = hm.get("GRP_CD");
				encrypted = data != null ? data.toString() : " ";
				ps.GRP_CD = encrypted;
				zmo_1030_rd04_arr.add(ps);
			}
			mpoa = new MaintenaceParts_Order_Adapter(context,
					R.layout.maintenanceparts_order_row, zmo_1030_rd04_arr);
			lv_order.setAdapter(mpoa);
			mpoa.notifyDataSetChanged();

			lv_order.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View v,
						int position, long arg3) {

					if (zmo_1030_rd04_arr.get(position).ZSTATUS
							.equals(RELEASED)) {
						EventPopupC epc = new EventPopupC(context);
						epc.show("입고완료 품목입니다.");
						return;
					}

					if (zmo_1030_rd04_arr.get(position).ZSTATUS.equals(STOCKED)) // 출고완료
					{
						CHOICED = position;
						final int final_position = position;
						// 체크안되있을경우
						if (!MaintenaceParts_Order_Adapter.checked_items
								.contains(position)) {
							final TextView tv_stock = (TextView) v
									.findViewById(R.id.inventory_maintenanceparts_order_row_id9);
							final TextView tv_return = (TextView) v
									.findViewById(R.id.inventory_maintenanceparts_order_row_id11);

							// myung 21031126 ADD 체크해제된 항목 다시 체크 가능토록 변경. 다시
							// 체크시에는 입고수량은 출고 수량과 동일하게 셋팅 함.
							MaintenaceParts_Order_Adapter.checked_items
									.add(position);
							zmo_1030_rd04_arr.get(position).INC_QTY = zmo_1030_rd04_arr
									.get(position).EXP_QTY;
							// myung 20131121 UPDATE 로직버그수정
							if (tv_stock.getText().equals("0"))
								zmo_1030_rd04_arr.get(position).INC_DATE = " ";
							else {
								Calendar cal = Calendar.getInstance();
								int year = cal.get(Calendar.YEAR);
								int month = cal.get(Calendar.MONTH) + 1;
								int day = cal.get(Calendar.DAY_OF_MONTH);
								zmo_1030_rd04_arr.get(position).INC_DATE = ""
										+ year + month + day;
							}

							tv_stock.setOnClickListener(new OnClickListener() {

								@Override
								public void onClick(View v) {
									showPopupNumber(final_position, tv_stock,
											tv_return);
								}
							});
							if(tv_stock.isClickable())
								tv_stock.requestFocus();
						}
						// 체크되있을경우
						else {
							zmo_1030_rd04_arr.get(position).INC_DATE = " ";
							zmo_1030_rd04_arr.get(position).INC_QTY = "0";
							ZMO_1030_RD04 rd04 = zmo_1030_rd04_arr
									.get(final_position);
							rd04.UNINC_QTY = "0";
							rd04.RETEXT = "";
							MaintenaceParts_Order_Adapter.checked_items
									.remove(MaintenaceParts_Order_Adapter.checked_items
											.indexOf(position));
							final TextView tv_stock = (TextView) v
									.findViewById(R.id.inventory_maintenanceparts_order_row_id9);
							final TextView tv_return = (TextView) v
									.findViewById(R.id.inventory_maintenanceparts_order_row_id11);
							tv_stock.setOnClickListener(new OnClickListener() {

								@Override
								public void onClick(View v) {
									showPopupNumber(final_position, tv_stock,
											tv_return);
								}
							});
//							tv_stock.setFocusable(true);
						}

						mpoa.notifyDataSetChanged();
					}
				}
			});
			//myung 20131202 ADD 조회 후 입고일자 당일로 기본 셋팅.
			setDateButton();

			
		}// MAINTENANCE_PARTS_STOCKED
		else if (FuntionName.equals(MAINTENANCE_PARTS_STOCKED_DONE)) {
			// refresh
			// getPartsSearch();
			initQueryItem();
		}
	}

	private void setRETURNClick(final int position, final TextView tv_return) {
		ViewGroup vg = pwRETURN.getViewGroup();
		LinearLayout back = (LinearLayout) vg.getChildAt(0);
		for (int i = 0; i < back.getChildCount(); i++) {
			LinearLayout row_back = (LinearLayout) back.getChildAt(i);
			final Button bt = (Button) row_back.getChildAt(0);
			bt.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					if (!bt.getTag().toString().equals("3")) {
						zmo_1030_rd04_arr.get(position).RETEXT = bt.getText()
								.toString();
						// EventPopupC epc = new EventPopupC(context);
						// epc.show(zmo_1030_rd04_arr.get(position).RETEXT +
						// " 입니다");
						RETURN_TAG = zmo_1030_rd04_arr.get(position).RETEXT;
						mpoa.notifyDataSetChanged();
					} else {
						// 반송사유창을 연다
						showReturnPopup(position, tv_return);
					}
					pwRETURN.dismiss();
				}
			});
		}
		pwRETURN.show(tv_return);
	}

	// EditText et_return;
	protected void showReturnPopup(final int position, final TextView tv_return) {
		final ViewGroup vg = mprpopup.getViewGroup();
		mprpopup.setOnDismissListener();
		et_return = (EditText) vg
				.findViewById(R.id.maintenance_return_edittext_id);
		Button done = (Button) vg
				.findViewById(R.id.maintenance_return_edittext_done_id);
		done.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Object data = et_return.getText();
				zmo_1030_rd04_arr.get(position).RETEXT = data != null ? data
						.toString() : " ";
				mpoa.notifyDataSetChanged();
				mprpopup.dismiss();
				keyboard_hide.sendEmptyMessageDelayed(0, 100);
			}
		});
		mprpopup.show(tv_return);
		keyboard_show.sendEmptyMessageDelayed(0, 100);
	}

	// 키보드내리기
	EditText et_return;
	Handler keyboard_hide = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			InputMethodManager imm = (InputMethodManager) context
					.getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(et_return.getWindowToken(), 0);
		}
	};
	Handler keyboard_show = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			InputMethodManager imm = (InputMethodManager) context
					.getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.showSoftInput(et_return, 0);
		}
	};

	private void showPopupNumber(final int position, TextView tv_request,
			final TextView tv_return) {
		ViewGroup vg = mPopupWindow.getViewGroup();
		final TextView input = (TextView) vg
				.findViewById(R.id.inventory_bt_input);
		final ZMO_1030_RD04 rld = zmo_1030_rd04_arr.get(position);
		Button done = (Button) vg.findViewById(R.id.inventory_bt_done);
		done.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Object data = input.getText();
				String num = data != null && !data.toString().equals("") ? data
						.toString() : "0";
				int int_num = Integer.parseInt(num);

				int int_source_num = Integer.parseInt(rld.EXP_QTY);
				if (int_num <= 0) {
					// 20131121 DELETE 요청에 의해 팝업 삭제
					// EventPopupC epc = new EventPopupC(context);
					// epc.show("입력수량이 없습니다.");
					rld.INC_QTY = "0";
					rld.UNINC_QTY = "0";
					rld.INC_DATE = " ";
					if (MaintenaceParts_Order_Adapter.checked_items
							.contains(position)) {
						MaintenaceParts_Order_Adapter.checked_items
								.remove(MaintenaceParts_Order_Adapter.checked_items
										.indexOf(position));
					}
				} else if (int_source_num < int_num) {
					EventPopupC epc = new EventPopupC(context);
					epc.show("반송수량이 출고수량보다 많습니다.");
				} else {
					int uninc_qty = int_source_num - int_num;
					rld.INC_QTY = num + "";
					rld.UNINC_QTY = uninc_qty + "";

					// myung 20131121 ADD 버그수정
					Calendar cal = Calendar.getInstance();
					int year = cal.get(Calendar.YEAR);
					int month = cal.get(Calendar.MONTH) + 1;
					int day = cal.get(Calendar.DAY_OF_MONTH);
					// ps.INC_DATE = ""+year+month+day;
					rld.INC_DATE = "" + year + month + day;

					if (!MaintenaceParts_Order_Adapter.checked_items
							.contains(position)) {
						MaintenaceParts_Order_Adapter.checked_items
								.add(position);
					}
					int exp_qty = Integer.parseInt(rld.EXP_QTY);
					int inc_qty = Integer.parseInt(rld.INC_QTY);
					if (exp_qty != inc_qty)
						setRETURNClick(position, tv_return);
				}
				mpoa.notifyDataSetChanged();
				mPopupWindow.setInput("CLEAR", true);
				mPopupWindow.setDONE(true);
				mPopupWindow.dismiss();
			}
		});
		mPopupWindow.show(tv_request, 0, rld.EXP_QTY);
	}

	public class ZMO_1030_RD04 {
		public String ZSTATUS; // 진행상태코드
		public String ZSTATUST; // 진행상태명
		public String EBELN; // 발주번호
		public String EBELP; // 발주 Item 번호
		public String MATKL; // 자재그룹코드
		public String WGBEZ; // 자재그룹명
		public String MATNR; // 부품코드
		public String MAKTX; // 부품명
		public String MEINS; // 단위
		public String EXP_DATE; // 출고일자
		public String EXP_QTY; // 출고수량
		public String INC_DATE; // 입고일자
		public String UNINC_QTY; // 반송수량
		public String INC_QTY; // 입고수량
		public String RETEXT; // 반송사유
		public String MENGE; // 구매오더수량
		public String WERKS; // 플랜트
		public String GRP_CD;
	}

	@Override
	public void reDownloadDB(String newVersion) {
	}

	private final String MAINTENANCE_PARTS_STOCKED = "ZMO_1030_RD04";
	private final String MAINTENANCE_PARTS_STOCKED_TABLE_NAME = "maintenance_parts_stocked_table";

	private void getPartsSearch() {
		showProgress("부품 발주내역을 조회 중입니다.");
		HashMap<String, String> map = getCommonConnectData();
		connector.setStructure("IS_LOGIN", map);
		connector.setParameter("I_PERNR", getLoginID());
		String date1 = bt_date1.getText().toString().replace(".", "");
		String date2 = bt_date2.getText().toString().replace(".", "");
		connector.setParameter("I_EXP_DATE_FR", date1);
		connector.setParameter("I_EXP_DATE_TO", date2);
		connector.setParameter("I_MATKL", M028_TAG);
		connector.setParameter("I_INVNR", getInvnr());
		connector.setParameter("I_ZSTATUS", PM073_TAG); // 파라미터추가 2013.09.24

//		Log.i("####", "####PM073_TAG" + PM073_TAG);
		if (PM073_TAG.equals("40")) {
			bt_done.setEnabled(false);
		} else {
			bt_done.setEnabled(true);
		}

		try {
			connector.executeRFCAsyncTask(MAINTENANCE_PARTS_STOCKED,
					MAINTENANCE_PARTS_STOCKED_TABLE_NAME);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onClick(View arg0) {
		String data;
		String date;
		switch (arg0.getId()) {
		case R.id.inventory_maintenance_datepick1_id:
			data = bt_date2.getText().toString();
			date = data.replace(".", "");
			idatepickpopup = new DatepickPopup2(context, date, 0);
			idatepickpopup.show(bt_date1);
			break;
		case R.id.inventory_maintenance_datepick2_id:
			data = bt_date1.getText().toString();
			date = data.replace(".", "");
			idatepickpopup = new DatepickPopup2(context, date, 1);
			idatepickpopup.show(bt_date2);
			break;
		case R.id.inventory_maintenance_group_popup_id:
			pwM028.show(bt_group);
			break;
		case R.id.inventory_maintenance_progress_popup_id:
			pwPM073.show(bt_progress);
			break;
		case R.id.inventory_maintenance_parts_getlist_id:
			zmo_1030_rd04_arr = new ArrayList<ZMO_1030_RD04>();
			mpoa = new MaintenaceParts_Order_Adapter(context,
					R.layout.maintenanceparts_order_row, zmo_1030_rd04_arr);
			lv_order.setAdapter(mpoa);
			iv_nodata.setVisibility(View.VISIBLE);
			getPartsSearch();

			break;
		case R.id.inventory_maintenance_done_id:
			if (MaintenaceParts_Order_Adapter.checked_items == null
					|| MaintenaceParts_Order_Adapter.checked_items.size() <= 0)
				return;
			goStock();
			break;
		}
	}

	private final String MAINTENANCE_PARTS_STOCKED_DONE = "ZMO_1030_WR04";
	private final String MAINTENANCE_PARTS_STOCKED_DONE_TABLE_NAME = "maintenance_parts_stocked_done_table";

	private void goStock() {
		showProgress("입고 완료 처리 중입니다.");
		HashMap<String, String> map = getCommonConnectData();
		connector.setStructure("IS_LOGIN", map);
		connector.setParameter("I_PERNR", getLoginID());
		connector.setTable("I_ITAB1", getTable());
		try {
			connector.executeRFCAsyncTask(MAINTENANCE_PARTS_STOCKED_DONE,
					MAINTENANCE_PARTS_STOCKED_DONE_TABLE_NAME);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// schneider
	private ArrayList<HashMap<String, String>> getTable() {
		ArrayList<HashMap<String, String>> i_itab1 = new ArrayList<HashMap<String, String>>();
		for (int i = 0; i < MaintenaceParts_Order_Adapter.checked_items.size(); i++) {
			int checked_num = MaintenaceParts_Order_Adapter.checked_items
					.get(i);
			ZMO_1030_RD04 rd04 = zmo_1030_rd04_arr.get(checked_num);
			HashMap<String, String> hm = new HashMap<String, String>();
			hm.put("EBELN", rd04.EBELN); // 구매요청번호(공백확인)
			hm.put("EBELP", rd04.EBELP); // 구매 요청 품목 번호(공백확인)
			hm.put("WERKS", rd04.WERKS); // 자재 번호(확인)
			hm.put("MATNR", rd04.MATNR); // 단위(확인)
			hm.put("MEINS", rd04.MEINS); // 신청수량(확인)
			hm.put("EXP_QTY", rd04.EXP_QTY); // 출고수량
			hm.put("INC_QTY", rd04.INC_QTY); // 입고수량
			hm.put("UNINC_QTY", rd04.UNINC_QTY); // 반송수량
			hm.put("RETEXT", rd04.RETEXT); // 반송사유		//Jonathan 14.06.19  TDLINE를 RETEXT 로 바꿈.
			i_itab1.add(hm);
			
			
			Set<String> set  = hm.keySet();
			Iterator<String> it = set.iterator();
			String key;
			
			while(it.hasNext()) 
			{
				key = it.next();
				kog.e("Jonathan", "반송사유 알고 싶어... ::  " + key + "    value  === " + hm.get(key));
			}
		}
		
		
		
		
		return i_itab1;
	}

	private HashMap<String, String> getCommonConnectData() {
		HashMap<String, String> reCommonMap = null;
		LoginModel model = KtRentalApplication.getLoginModel();
		if (model.getModelMap() != null)
			reCommonMap = model.getModelMap();
		return reCommonMap;
	}

	private String getInvnr() {
		return KtRentalApplication.getLoginModel().getInvnr();
	}

	private String getLoginID() {
		return KtRentalApplication.getLoginModel().getLogid();
	}

	private String getWerks() {
		return KtRentalApplication.getLoginModel().getWerks();
	}

	private String getLgort() {
		return KtRentalApplication.getLoginModel().getLgort();
	}

	private void initQueryItem() {
		showProgress();
		String[] _whereArgs = null;
		String[] _whereCause = null;
		String[] colums = null;
		DbQueryModel dbQueryModel = new DbQueryModel(DEFINE.STOCK_TABLE_NAME,
				_whereCause, _whereArgs, colums);
		dbQueryModel.setOrderBy(DEFINE.MATKL + " asc");
		DbAsyncTask dbAsyncTask = new DbAsyncTask("initQueryItem", mContext,
				new DbAsyncResLintener() {

					@Override
					public void onCompleteDB(String funName, int type,
							Cursor cursor, String tableName) {

						// TODO Auto-generated method stub
						hideProgress();
						if (cursor != null) {
							cursor.moveToFirst();
							ArrayList<HashMap<String, String>> updateArr = new ArrayList<HashMap<String, String>>();
							ArrayList<HashMap<String, String>> i_itab1 = new ArrayList<HashMap<String, String>>();
							for (int i = 0; i < MaintenaceParts_Order_Adapter.checked_items
									.size(); i++) {
								int checked_num = MaintenaceParts_Order_Adapter.checked_items
										.get(i);
								ZMO_1030_RD04 rd04 = zmo_1030_rd04_arr
										.get(checked_num);
								HashMap<String, String> hm = new HashMap<String, String>();
								hm.put("MATKL", rd04.MATKL);
								hm.put("GRP_CD", rd04.GRP_CD);
								hm.put("MAKTX", rd04.MAKTX);
								hm.put("MEINS", rd04.MEINS);
								hm.put("MATNR", rd04.MATNR);
								hm.put("INC_QTY", rd04.INC_QTY);
								hm.put("LTEXT", " ");
								hm.put("BUKRS", rd04.WERKS);
								i_itab1.add(hm);
							}
							while (!cursor.isAfterLast()) {
								HashMap<String, String> map = setStockDB(
										cursor, i_itab1);
								if (map != null)
									updateArr.add(map);
								// if (i_itab1.size() > 0)
								cursor.moveToNext();
								// else{
								// break;
								// }
							}
							cursor.close();
							for (HashMap<String, String> hashMap : i_itab1) {
								String incqty = hashMap.get("INC_QTY");
								hashMap.put("LABST", "" + incqty);
								hashMap.remove("INC_QTY");
								updateArr.add(hashMap);
							}
							updateStockDB(updateArr);
						}
					}
				}, dbQueryModel);
		dbAsyncTask.execute(DbAsyncTask.DB_SELECT);

		// myung 20131126 출고 완료 부품 입고 완료 후 출고대상 조회가 아닌 입고완료로 상태바꿔서 조회하도록 수정
		mpoa.notifyDataSetChanged();

	}

	private HashMap<String, String> setStockDB(Cursor cursor,
			ArrayList<HashMap<String, String>> i_itab1) {
		HashMap<String, String> map = null;
		String matkl = cursor.getString(cursor.getColumnIndex("MATKL"));
		String grpcd = cursor.getString(cursor.getColumnIndex("GRP_CD"));
		String maktx = cursor.getString(cursor.getColumnIndex("MAKTX"));
		String meins = cursor.getString(cursor.getColumnIndex("MEINS"));
		String matnr = cursor.getString(cursor.getColumnIndex("MATNR"));
		String labst = cursor.getString(cursor.getColumnIndex("LABST"));
		String ltext = cursor.getString(cursor.getColumnIndex("LTEXT"));
		String burks = cursor.getString(cursor.getColumnIndex("BUKRS"));
		for (HashMap<String, String> hashMap : i_itab1) {
			map = hashMap;
			String resultMatnr = hashMap.get("MATNR");
			String resultMatkl = hashMap.get("MATKL");
			String incqty = hashMap.get("INC_QTY");
			if (matnr.equals(resultMatnr)) {// && matkl.equals(resultMatkl)) {
				int iLabst = Integer.parseInt(labst);
				int iIncqty = Integer.parseInt(incqty);
				iLabst = iLabst + iIncqty;
				map.put("LABST", "" + iLabst);
				map.remove("INC_QTY");
				i_itab1.remove(hashMap);
				break;
			}
			// else {
			// HashMap<String, String> map = new HashMap<String, String>();
			// map.put("MATKL", matkl);
			// map.put("GRP_CD", grpcd);
			// map.put("MAKTX", maktx);
			// map.put("MEINS", meins);
			// map.put("MATNR", rd04.MATNR);
			// map.put("LABST", rd04.INC_QTY);
			// map.put("INC_QTY", rd04.INC_QTY);
			// map.put("LTEXT", " ");
			// map.put("BURKS", rd04.WERKS);
			// // i_itab1.add(object)
			// }
		}
		return map;
	}

	private void updateStockDB(ArrayList<HashMap<String, String>> i_itab1) {
		showProgress();
		HashMap<String, ArrayList<HashMap<String, String>>> tableArrayMap = new HashMap<String, ArrayList<HashMap<String, String>>>();
		HashMap<String, HashMap<String, String>> resStructMap = new HashMap<String, HashMap<String, String>>();
		String[] reTableNames = new String[1];
		reTableNames[0] = "I_ITAB1";
		tableArrayMap.put(reTableNames[0], i_itab1);
		String[] tableNames = new String[1];
		TableModel tableModel = new TableModel(tableNames, tableArrayMap,
				resStructMap, null, "");
		HashMap<String, String> loginTableNameMap = new HashMap<String, String>();
		loginTableNameMap.put("I_ITAB1", DEFINE.STOCK_TABLE_NAME);
		DbAsyncTask asyncTask = new DbAsyncTask(
				ConnectController.REPAIR_FUNTION_NAME, DEFINE.STOCK_TABLE_NAME,
				mContext, new DbAsyncResLintener() {

					@Override
					public void onCompleteDB(String funName, int type,
							Cursor cursor, String tableName) {
						hideProgress();
						// refresh
						// 20131126 ADD 출고 완료 부품 입고 완료 후 출고대상 조회가 아닌 입고완료로 상태바꿔서
						// 조회하도록 수정
						bt_progress.setText("입고완료");
						PM073_TAG = "40";
						getPartsSearch();
					}
				}, // DbAsyncResListener
				tableModel, loginTableNameMap);
		asyncTask.execute(DbAsyncTask.DB_ARRAY_INSERT);
	}
}
