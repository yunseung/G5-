package com.ktrental.dialog;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.ktrental.R;
import com.ktrental.adapter.Duedate_Dialog_Left_Adapter;
import com.ktrental.adapter.Duedate_Dialog_Right_Adapter;
import com.ktrental.cm.connect.ConnectController;
import com.ktrental.cm.connect.Connector.ConnectInterface;
import com.ktrental.cm.db.DbAsyncTask;
import com.ktrental.cm.db.DbAsyncTask.DbAsyncResLintener;
import com.ktrental.common.DEFINE;
import com.ktrental.common.KtRentalApplication;
import com.ktrental.custom.Cal_Custom;
import com.ktrental.custom.DayInfo;
import com.ktrental.model.BaseMaintenanceModel;
import com.ktrental.model.TableModel;
import com.ktrental.popup.EventPopupC;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class Duedate_Dialog extends DialogC implements ConnectInterface,
		View.OnClickListener, DbAsyncResLintener {

	private Window w;
	private WindowManager.LayoutParams lp;
	private Context context;
	private Button close;
	private ProgressDialog pd;
	private ConnectController cc;
	private ListView lv_left;
	private Duedate_Dialog_Left_Adapter ddla;
	private ArrayList<BaseMaintenanceModel> bmm_arr;
	private Cal_Custom cal_custom;
	public static Duedate_Dialog dd;
	private ListView lv_right;
	private Duedate_Dialog_Right_Adapter ddra;
	private Button bt_save;
	private Context mContext;
	private TextView tv_today_title;
	private TextView tv_title1;
	private TextView tv_title2;
	private TextView tv_title3;

	private int TODAY;

	private String mTime, mMemo;

	private String mATVYN;
	private String mREQNO;

	public Duedate_Dialog(Context context, ArrayList<BaseMaintenanceModel> mBaseMaintenanceModels, String ATVYN, String REQNO) {
		super(context);
		mContext = context;
		this.bmm_arr = mBaseMaintenanceModels;
		mATVYN = ATVYN;
		mREQNO = REQNO;

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.dd = this;
		setContentView(R.layout.duedate_dialog);

		w = getWindow();
		w.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN
				| WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
		w.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
		lp = (WindowManager.LayoutParams) w.getAttributes();
		lp.width = LayoutParams.MATCH_PARENT;
		lp.height = LayoutParams.MATCH_PARENT;
		w.setAttributes(lp);
		this.context = context;
		pd = new ProgressDialog(context);
		pd.setCancelable(false);
		pd.setMessage("검색중입니다.");
		cc = new ConnectController(this, context);
		close = (Button) findViewById(R.id.dudate_dialog_close_id);
		close.setOnClickListener(this);
		lv_left = (ListView) findViewById(R.id.duedate_dialog_list_id);
		cal_custom = (Cal_Custom) findViewById(R.id.duedate_dialog_cal_id);
		lv_right = (ListView) findViewById(R.id.duedate_dialog_right_list_id);
		bt_save = (Button) findViewById(R.id.duedate_dialog_save_id);
		bt_save.setOnClickListener(this);
		tv_today_title = (TextView) findViewById(R.id.duedate_dialog_today_title_id);
		tv_title1 = (TextView) findViewById(R.id.duedate_dialog_title1_id);
		tv_title2 = (TextView) findViewById(R.id.duedate_dialog_title2_id);
		tv_title3 = (TextView) findViewById(R.id.duedate_dialog_title3_id);
		init();
	}

	private void init() {
		Calendar cal = Calendar.getInstance();
		String today = "" + cal.get(Calendar.YEAR)
				+ String.format("%02d", (cal.get(Calendar.MONTH) + 1))
				+ String.format("%02d", cal.get(Calendar.DAY_OF_MONTH));
		TODAY = Integer.parseInt(today);

		tv_today_title.setText(cal_custom.TODAY2);
		SELECTED_DAY = "-1";
		ddla = new Duedate_Dialog_Left_Adapter(context,
				R.layout.duedate_dialog_left_row, bmm_arr);
		lv_left.setAdapter(ddla);
		lv_left.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				if (!Duedate_Dialog_Left_Adapter.checked_items
						.contains(position)) {
					Duedate_Dialog_Left_Adapter.checked_items.add(position);
				} else {
					Duedate_Dialog_Left_Adapter.checked_items
							.remove(Duedate_Dialog_Left_Adapter.checked_items
									.indexOf(position));
				}
				ddla.notifyDataSetChanged();
			}
		});
		initREPAIR();
	}

	public String SELECTED_DAY;
	public int SELECTED_POSITION;
	public ArrayList<DayInfo> day_list;

	public void setDay(TextView tv, final int position,
			final ArrayList<DayInfo> mDayList) {
		tv.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				setCalCheck(position, mDayList);
				SELECTED_POSITION = position;
				day_list = mDayList;

				TimePickDialog dialog = new TimePickDialog(mContext);
				dialog.setOnTimePickListener(new TimePickDialog.OnTimePickListener() {
					@Override
					public void onTimePickResult(String time, String memo) {
						mTime = time;
						mMemo = memo;
					}
				});
				dialog.show();
			}
		});
	}

	public void setCalCheck(int position, final ArrayList<DayInfo> mDayList) {

		cal_custom.SELECTED = position;
		if (mDayList == null)
			return;
		DayInfo di = mDayList.get(position);
		// Log.i("####",
		// "##눌린날짜"+di.getYear()+"/"+di.getMonth()+"/"+di.getDay());
		String dayday;
		if (di.getDay().length() <= 1) {
			dayday = "0" + di.getDay();
		} else {
			dayday = di.getDay();
		}
		SELECTED_DAY = di.getYear() + di.getMonth() + dayday;
		tv_today_title.setText(di.getYear() + "." + di.getMonth() + "." + di.getDay());
		cal_custom.initView();
		ArrayList<HashMap<String, String>> day_arr = getDayList(SELECTED_DAY);
		for (int i = 0; i < day_arr.size(); i++) {
			String a = day_arr.get(i).get("GSTRS");
			// Log.i("#", "#### 눌린목록들" + a);
		}
		ArrayList<HashMap<String, String>> new_arr = new ArrayList<HashMap<String, String>>();
		int new_count = -1;
		if (bmm_arrs != null && SELECTED_DAY.equals(SENDED_DATE)) {
			new_count = bmm_arrs.size() - 1;
			new_arr.addAll(setRightNewList());
		}
		new_arr.addAll(day_arr);
		ddra = new Duedate_Dialog_Right_Adapter(context,
				R.layout.duedate_dialog_right_row, new_arr, new_count);
		lv_right.setAdapter(ddra);
		int a1 = 0;
		int a2 = 0;
		for (int i = 0; i < day_arr.size(); i++) {
			if (day_arr.get(i).get("CCMSTS").equals("E0001")) {
				a1++;
			} else if (day_arr.get(i).get("CCMSTS").equals("E0002")) {
				a2++;
			}
		}
		tv_title1.setText(a1 + "");
		tv_title2.setText(a2 + "");
		if (bmm_arrs != null && SELECTED_DAY.equals(SENDED_DATE)) {
			// 추가 예약 의 카운트 변경
			tv_title2.setText(a2 + bmm_arrs.size() + "");
			tv_title3.setText(bmm_arrs.size() + "");
		} else {
			tv_title3.setText("0");
		}
	}

	protected ArrayList<HashMap<String, String>> getDayList(String key) {
		ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
		for (int i = 0; i < repair_arr.size(); i++) {
			if (repair_arr.get(i).get("GSTRS").equals(key)) {
				list.add(repair_arr.get(i));
			}
		}
		return list;
	}

	String TABLE_NAME = "REPAIR_TABLE";
	ArrayList<HashMap<String, String>> repair_arr;

	private ArrayList<HashMap<String, String>> initREPAIR() {
		repair_arr = new ArrayList<HashMap<String, String>>();
		String path = context.getExternalCacheDir() + "/DATABASE/"
				+ DEFINE.SQLLITE_DB_NAME;
		SQLiteDatabase sqlite = SQLiteDatabase.openDatabase(path, null,
				SQLiteDatabase.OPEN_READONLY);
		Cursor cursor = sqlite.rawQuery("select * from " + TABLE_NAME
				+ " order by 'GSTRS'", null);
		ArrayList<String> colums = new ArrayList<String>();
		for (int i = 0; i < cursor.getColumnCount(); i++) {
			colums.add(cursor.getColumnName(i));
		}
		cursor.moveToFirst();
		while (cursor.moveToNext()) {
			HashMap<String, String> hm = new HashMap<String, String>();
			for (int i = 0; i < colums.size(); i++) {
				int calnum1 = cursor.getColumnIndex(colums.get(i));
				String field = cursor.getString(calnum1);
				hm.put(colums.get(i), field);
			}
			repair_arr.add(hm);
		}
		cursor.close();
		// 2017.07.05. hjt. db close 는 app 종료시 알아서 된다
		// 이것때문에 throwIfClosedLocked Exception 발생한다
//		sqlite.close();
		return repair_arr;
	}

	@Override
	public void connectResponse(String FuntionName, String resultText,
			String MTYPE, int resulCode, TableModel tableModel) {
		// Log.i("#", "####" + FuntionName + "/" + resultText + "/" + MTYPE +
		// "/"
		// + resulCode);
		hideProgress();
		if (MTYPE == null || !MTYPE.equals("S")) {
			cc.duplicateLogin(mContext);
			
			EventPopupC epc = new EventPopupC(context);
			epc.show(resultText);
			return;
		}
		if (FuntionName.equals("ZMO_1050_WR04")) {
			EventPopupC epc = new EventPopupC(context);
			epc.show("변경되었습니다.");
			bt_save.setText("닫기");
			bt_save.setBackgroundResource(R.drawable.btn05_selector);
			lv_left.setAdapter(null);
			// 우측리스트에 나열하기
			setRightNewList();
			setCalCheck(SELECTED_POSITION, day_list);
			// 디비에 쿼리해서 변경하기
			setDB();
			
			cc.duplicateLogin(mContext);
			
		}
	}

	private void setDB() {
		for (int i = 0; i < bmm_arr.size(); i++) {
			if (Duedate_Dialog_Left_Adapter.checked_items.contains(i)) {
				// Log.i("#", "####바꿀" + bmm_arr.get(i).getCarNum());
				updateComplete(bmm_arr.get(i).getAUFNR());
			}
		}
	}

	private void updateComplete(String aufnr) {
		ContentValues contentValues = new ContentValues();
		contentValues.put("CCMSTS", "E0002");
		contentValues.put("GSTRS", SELECTED_DAY);
		contentValues.put("AUFNR", aufnr);
		contentValues.put("PRERQ", mMemo);
		String[] keys = new String[1];
		keys[0] = "AUFNR";
		DbAsyncTask dbAsyncTask = new DbAsyncTask("updateComplete",
				DEFINE.REPAIR_TABLE_NAME, mContext, this, contentValues, keys);
		dbAsyncTask.execute(DbAsyncTask.DB_UPDATE);
	}

	ArrayList<HashMap<String, String>> bmm_arrs;

	private ArrayList<HashMap<String, String>> setRightNewList() {
		bmm_arrs = new ArrayList<HashMap<String, String>>();
		for (int i = 0; i < bmm_arr.size(); i++) {
			if (ddla.checked_items.contains(i)) {
				HashMap<String, String> hm = new HashMap<String, String>();
				hm.put("KUNNR_NM", bmm_arr.get(i).getCUSTOMER_NAME());
				hm.put("DRIVN", bmm_arr.get(i).getCarNum());
				hm.put("INVNR", bmm_arr.get(i).getCarNum());
				hm.put("ENAME", bmm_arr.get(i).getCUSTOMER_NAME());
				// 나눠서 데이터 받아야함
				hm.put("POST_CODE", bmm_arr.get(i).getPostCode());
				hm.put("CITY", bmm_arr.get(i).getCity());
				hm.put("STREET", bmm_arr.get(i).getStreet());
				// hm.put("POST_CODE", "우편번호");
				// hm.put("CITY", "시티");
				// hm.put("STREET", "스트리트");
				bmm_arrs.add(hm);
			}
		}
		return bmm_arrs;
	}

	public String SENDED_DATE;

	@Override
	public void reDownloadDB(String newVersion) {
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.dudate_dialog_close_id: // 닫기
			dismiss();
			break;
		case R.id.duedate_dialog_save_id:// 저장하기
			String str = bt_save.getText().toString();
			if (str.equals("닫기")) {
				dismiss();
			} else {
				if (SELECTED_DAY.equals("-1")) {
					final EventPopupC epc = new EventPopupC(context);
					TextView tv_title = (TextView) epc
							.findViewById(R.id.tv_body);
					tv_title.setText("예정일을 선택해 주세요");
					Button bt_done = (Button) epc.findViewById(R.id.btn_ok);
					bt_done.setOnClickListener(new View.OnClickListener() {

						@Override
						public void onClick(View v) {
							epc.dismiss();
						}
					});
					epc.show();
					return;
				}

				// myung 20131217 ADD 변경 대상 차량리스트에 체크가 모두 풀렸을 경우
				// "예정일 변경대상 차량이 없습니다." 메시지 출력 후 리턴.
				if(getTable().size()<=0){
					final EventPopupC epc = new EventPopupC(context);
					epc.show("예정일 변경대상 차량이 없습니다.");
					return;
				}
				// Log.i("####", "####투데이" + TODAY + "/" + SELECTED_DAY);
				int selected_day = Integer.parseInt(SELECTED_DAY);
				if (selected_day < TODAY) {
					EventPopupC epc = new EventPopupC(context);
					epc.show("과거일자를 선택할 수 없습니다.");
					return;
				}

				showProgress("저장중 입니다.");
				SENDED_DATE = SELECTED_DAY;


				cc.setZMO_1050_WR04(mREQNO, SENDED_DATE, mTime, mMemo, mATVYN, getTable());

			}
			break;
		}
	}

	ArrayList<HashMap<String, String>> i_itab1;

	private ArrayList<HashMap<String, String>> getTable() {
		i_itab1 = new ArrayList<HashMap<String, String>>();
		for (int i = 0; i < bmm_arr.size(); i++) {
			if (Duedate_Dialog_Left_Adapter.checked_items.contains(i)) {
				HashMap<String, String> hm = new HashMap<String, String>();
				hm.put("AUFNR", bmm_arr.get(i).getAUFNR());
				i_itab1.add(hm);
			}
		}
		return i_itab1;
	}

	@Override
	public void onCompleteDB(String funName, int type, Cursor cursor,
			String tableName) {
		// TODO Auto-generated method stub
	}

	@Override
	public void dismiss() {
		// TODO Auto-generated method stub
		KtRentalApplication.changeRepair();
		KtRentalApplication.getInstance().queryMaintenacePlan();
		super.dismiss();
	}
}
