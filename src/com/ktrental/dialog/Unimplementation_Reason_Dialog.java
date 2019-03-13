package com.ktrental.dialog;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.ktrental.R;
import com.ktrental.beans.MULTY;
import com.ktrental.cm.connect.ConnectController;
import com.ktrental.cm.connect.Connector.ConnectInterface;
import com.ktrental.cm.db.DbAsyncTask;
import com.ktrental.cm.db.DbAsyncTask.DbAsyncResLintener;
import com.ktrental.common.DEFINE;
import com.ktrental.common.KtRentalApplication;
import com.ktrental.model.TableModel;
import com.ktrental.popup.EventPopupC;
import com.ktrental.popup.M015_Popup;
import com.ktrental.popup.Popup_Window_Multy;
import com.ktrental.popup.QuickAction2;

import java.util.ArrayList;
import java.util.HashMap;

public class Unimplementation_Reason_Dialog extends DialogC implements
		android.widget.PopupWindow.OnDismissListener, DbAsyncResLintener,
		View.OnClickListener, ConnectInterface {

	private Context context;
	private Window w;
	private WindowManager.LayoutParams lp;
	private ProgressDialog pd;
	private ConnectController cc;
	private Button bt_close;
	private M015_Popup m015_popup;
	private Button bt_group;
	private Button bt_resist;
	private EditText et_detail;
	private ArrayList<String> aufnr_arr;

	private Button bt_group1;
	private Button bt_group2;

	private Popup_Window_Multy pwm;

	ArrayList<HashMap<String, String>> temp_itab1 = new ArrayList<HashMap<String, String>>();

	public Unimplementation_Reason_Dialog(Context context,
			ArrayList<String> aufnr) {
		super(context);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.unimplementation_reason_dialog);
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
		aufnr_arr = aufnr;

		initViews();
	}

	private void initViews() {
		bt_close = (Button) findViewById(R.id.unimplementation_reason_close_id);
		bt_close.setOnClickListener(this);
		pd = new ProgressDialog(context);
		pd.setCancelable(false);
		pd.setMessage("검색중입니다.");
		cc = new ConnectController(this, context);

		pwm = new Popup_Window_Multy(context);
		m015_popup = new M015_Popup(context, QuickAction2.VERTICAL, initM015());

		bt_group = (Button) findViewById(R.id.unimplementation_reason_group_id);
		bt_group.setOnClickListener(this);
		bt_group1 = (Button) findViewById(R.id.unimplementation_reason_group1_id);
		bt_group1.setOnClickListener(this);
		bt_group2 = (Button) findViewById(R.id.unimplementation_reason_group2_id);
		bt_group2.setOnClickListener(this);

		bt_resist = (Button) findViewById(R.id.unimplementation_reason_resist_id);
		bt_resist.setOnClickListener(this);

		et_detail = (EditText) findViewById(R.id.unimplementation_reason_content_id);

		setM015Click();
	}

	String M015_TAG = "";

	private void setM015Click() {
		ViewGroup vg = m015_popup.getViewGroup();
		LinearLayout back = (LinearLayout) vg.getChildAt(0);
		for (int i = 0; i < back.getChildCount(); i++) {
			LinearLayout row_back = (LinearLayout) back.getChildAt(i);
			final Button bt = (Button) row_back.getChildAt(0);
			bt.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					bt_group.setText(bt.getText().toString());
					M015_TAG = bt.getTag().toString();
					m015_popup.dismiss();
				}
			});
		}
	}

	private String TABLE_NAME = "O_ITAB1";
	private ArrayList<MULTY> m015_arr;

	private ArrayList<MULTY> initM015() {
		m015_arr = new ArrayList<MULTY>();
		MULTY m;
		String path = context.getExternalCacheDir() + "/DATABASE/"
				+ DEFINE.SQLLITE_DB_NAME;
		SQLiteDatabase sqlite = SQLiteDatabase.openDatabase(path, null,
				SQLiteDatabase.OPEN_READONLY);
		Cursor cursor = sqlite.rawQuery("select ZCODEV, ZCODEVT from "
				+ TABLE_NAME + " where ZCODEH = 'M015'", null);
		while (cursor.moveToNext()) {
			int calnum1 = cursor.getColumnIndex("ZCODEV");
			int calnum2 = cursor.getColumnIndex("ZCODEVT");
			String zcodev = cursor.getString(calnum1);
			String zcodevt = cursor.getString(calnum2);

			m = new MULTY();
			m.ZCODEV = zcodev;
			m.ZCODEVT = zcodevt;
			m015_arr.add(m);
		}
		cursor.close();
//		sqlite.close();

		// 출력테스트
		for (int i = 0; i < m015_arr.size(); i++) {
			MULTY key = m015_arr.get(i);
//			Log.i("", "####M015" + key.ZCODEVT);
		}

		return m015_arr;
	}

	@Override
	public void onDismiss() {
	}

	@Override
	public void onCompleteDB(String funName, int type, Cursor cursor,
			String tableName) {
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.unimplementation_reason_close_id:
			dismiss();
			break;

		case R.id.unimplementation_reason_group_id:
			m015_popup.show(bt_group);
			break;

		case R.id.unimplementation_reason_group1_id:
			pwm.show("PM111", bt_group1, true);
			bt_group2.setText("");
			bt_group2.setTag(null);
			break;

		case R.id.unimplementation_reason_group2_id:
			if (bt_group1.getTag() == null) {
				EventPopupC epc = new EventPopupC(context);
				epc.show("선택해주세요");
				return;
			} else {
				pwm.show(bt_group1.getTag().toString(), bt_group2, true);
			}
			break;

		//Jonathan 14.09.30 미실시 사유 등록 안됨.
		case R.id.unimplementation_reason_resist_id:
			if (bt_group2.getTag() == null) {
				EventPopupC epc = new EventPopupC(context);
				epc.show("미실시 사유를 선택해 주세요.");
				return;
			}
			showProgress("미실시 사유를 등록 중입니다.");
			Object data = et_detail.getText().toString();
			String detail = (data != null && !data.toString().equals("")) ? data
					.toString() : " ";
			cc.setZMO_1080_WR01(bt_group1.getTag().toString(), bt_group2.getTag().toString(), detail,
					getTable());
			break;
		}
	}

	private ArrayList<HashMap<String, String>> getTable() {
		ArrayList<HashMap<String, String>> i_itab1 = new ArrayList<HashMap<String, String>>();
		for (int i = 0; i < aufnr_arr.size(); i++) {
			HashMap<String, String> hm = new HashMap<String, String>();
			hm.put("AUFNR", aufnr_arr.get(i));// 고객차량 설비번호
			i_itab1.add(hm);
		}
		temp_itab1 = i_itab1;
		return i_itab1;
	}

	@Override
    public void connectResponse(String FuntionName, String resultText, String MTYPE, int resulCode, TableModel tableModel)
        {
//        Log.i("#", "####"+FuntionName+"/"+resultText+"/"+MTYPE+"/"+resulCode);
        hideProgress();

        if(MTYPE==null||!MTYPE.equals("S"))
            {
            EventPopupC epc = new EventPopupC(context);
            epc.show(resultText);
            ArrayList<HashMap<String, String>> o_itab1 = tableModel.getTableArray("O_ITAB1");
            HashMap<String, String> hm = o_itab1.get(0);
            String result = null;
            for (int i = 0; i < o_itab1.size(); i++)
                {
                result += hm.get("AUFNR")+"/"+hm.get("MESSAGE")+"\n";
                }
            return;
            } 
        
        if(FuntionName.equals("ZMO_1080_WR01"))
            {
            ArrayList<HashMap<String, String>> o_itab1 = tableModel.getTableArray("O_ITAB1");
//            Log.i("#","#### 결과물사이즈"+o_itab1.size());
            //myung 20131128 UPDATE 사유등록 DB갱신
            HashMap<String, String> hm;
            
            for(int i=0; i<temp_itab1.size(); i++){
            	hm = temp_itab1.get(i);
            	updateComplete(hm.get("AUFNR"));
            }
            
            dismiss();
            }
        }

	private void updateComplete(String strKey) {

		ContentValues contentValues = new ContentValues();
		contentValues.put("CCMSTS", "E0005");
		contentValues.put("AUFNR", strKey);

		String[] keys = new String[1];
		keys[0] = "AUFNR";

		DbAsyncTask dbAsyncTask = new DbAsyncTask("updateComplete",
				DEFINE.REPAIR_TABLE_NAME, mContext, new DbAsyncResLintener() {

					@Override
					public void onCompleteDB(String funName, int type,
							Cursor cursor, String tableName) {
						// TODO Auto-generated method stub
						hideProgress();
					}
				}, contentValues, keys);

		dbAsyncTask.execute(DbAsyncTask.DB_UPDATE);

		KtRentalApplication.changeRepair();

	}

	@Override
	public void reDownloadDB(String newVersion) {
		// TODO Auto-generated method stub

	}
	
	
	// myung 20140102 ADD 미실시 사유 입력 시 체크박스 선택/해제 정상적으로 안되는 오류 발생함.
	@Override
	public void dismiss() {
		// TODO Auto-generated method stub
		KtRentalApplication.changeRepair();
		KtRentalApplication.getInstance().queryMaintenacePlan();
		super.dismiss();
	}
}
