package com.ktrental.dialog;

import android.app.ProgressDialog;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.SpannableStringBuilder;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.ktrental.R;
import com.ktrental.adapter.Mistery_Shopping_List_Dialog_Adapter;
import com.ktrental.cm.connect.ConnectController2;
import com.ktrental.cm.connect.Connector.ConnectInterface;
import com.ktrental.common.DEFINE;
import com.ktrental.model.CorCardAccountModel;
import com.ktrental.model.TableModel;
import com.ktrental.popup.EventPopupC;
import com.ktrental.util.kog;

import java.util.ArrayList;
import java.util.HashMap;

public class Mistery_Shopping_Dialog extends DialogC implements ConnectInterface,
		View.OnClickListener {
	private Window w;
	private WindowManager.LayoutParams lp;
	private Context context;
	private ProgressDialog pd;

	private TextView major;
	private Button bt_close;
	// private EditText et_input;
	private ConnectController2 cc;
	private ListView lv_list;
	private Mistery_Shopping_List_Dialog_Adapter tlda;
	public int SELECTED = -1;
	private Handler mHandler;
    private HashMap<String, String> pm168List;

	public Mistery_Shopping_Dialog(Context context, String i_major) {
		super(context);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.mistery_shopping_dialog);
		w = getWindow();
		w.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN
				| WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
		w.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
		lp = (WindowManager.LayoutParams) w.getAttributes();
		lp.width = LayoutParams.MATCH_PARENT;
		lp.height = LayoutParams.MATCH_PARENT;
		w.setAttributes(lp);
		this.context = context;
		cc = new ConnectController2(context, this);

		bt_close = (Button) findViewById(R.id.account_list_close);
		bt_close.setOnClickListener(this);

        major = (TextView) findViewById(R.id.textView1);


		lv_list = (ListView) findViewById(R.id.account_list_list);
		cc.getZMO_1160_RD01("J", i_major);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}

	public Mistery_Shopping_Dialog(Context context, ArrayList<CorCardAccountModel> o_struct1) {
		super(context);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.mistery_shopping_dialog);
		w = getWindow();
		w.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN
				| WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
		w.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
		lp = (WindowManager.LayoutParams) w.getAttributes();
		lp.width = LayoutParams.MATCH_PARENT;
		lp.height = LayoutParams.MATCH_PARENT;
		w.setAttributes(lp);
		this.context = context;
		cc = new ConnectController2(context, this);

		bt_close = (Button) findViewById(R.id.account_list_close);
		bt_close.setOnClickListener(this);

		lv_list = (ListView) findViewById(R.id.account_list_list);
//        setList(o_struct1);

	}

	@Override
	public void connectResponse(String FuntionName, String resultText,
			String MTYPE, int resulCode, TableModel tableModel) {
//		Log.i("#", "####" + FuntionName + "/" + resultText + "/" + MTYPE + "/"
//				+ resulCode);
		hideProgress();
		kog.e("KDH", "Tire_List_Dialog FuntionName = "+FuntionName);

		if (MTYPE == null || !MTYPE.equals("S")) {

			cc.duplicateLogin(mContext);

			EventPopupC epc = new EventPopupC(context);
			epc.show(resultText);
			return;
		} else {
			if(FuntionName.equals("ZMO_1160_RD01")){
				ArrayList<HashMap<String, String>> array = tableModel.getTableArray();
				if(array != null && array.size() > 0){
					setList(array);
				}
			}
		}
	}

	@Override
	public void reDownloadDB(String newVersion) {

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.account_list_close: // 닫기
			dismiss();
			break;
		}
	}

    private HashMap<String, String> getPm168List()
    {
        pm168List = new HashMap<>();
        String path = context.getExternalCacheDir() + "/DATABASE/" + DEFINE.SQLLITE_DB_NAME;
        SQLiteDatabase sqlite = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.OPEN_READONLY);
        Cursor cursor = sqlite.rawQuery("select ZCODEV, ZCODEVT from " + "O_ITAB1" + " where (ZCODEH = 'PM168')", null);
        //myung 20131202 ADD 내역 > 진행상태 선택 > 조회 > 진행상태의 전체가 사라짐.
        while (cursor.moveToNext())
        {
            int calnum1 = cursor.getColumnIndex("ZCODEV");
            int calnum2 = cursor.getColumnIndex("ZCODEVT");
            String zcodev = cursor.getString(calnum1);
            String zcodevt = cursor.getString(calnum2);

            boolean over = false;
            pm168List.put(zcodev, zcodevt);
        }
        cursor.close();
        return pm168List;
    }

	private void setList(final ArrayList<HashMap<String, String>> _list) {
		if(_list == null){
			return;
		}

        getPm168List();
		if(_list != null && _list.size() > 0) {
		    String major_txt = pm168List.get(_list.get(0).get("MAJOR"));
            major.setText("오토케어(순회정비) 미스터리쇼핑 - " + major_txt);
        }

		// 조합 작업을 해야한다.
		// 일단 같은 마이너꺼들을 붙여놓자
		ArrayList<HashMap<String, String>> newList = new ArrayList<>();
		StringBuffer buffer = new StringBuffer();
        HashMap<String, HashMap<String, SpannableStringBuilder>> bufferList = new HashMap<>();
        HashMap<String, SpannableStringBuilder> bufferHashMap = new HashMap<>();
		for(int i=0; i<_list.size(); i++){
			HashMap<String, String> hashMap = _list.get(i);
			String minor = hashMap.get("MINOR");
			if(newList != null && newList.size() > 0 && newList.get(newList.size() -1 ).get("MINOR").equals(minor)){
				String message = hashMap.get("MESSAGE");
				String fontSize = hashMap.get("PSIZE");
				int int_fontSize = 10;
				String underline = null;
				String nline = null;
				underline = hashMap.get("ULINE");
				nline = hashMap.get("NLINE");

				message = message.replaceAll(" ","&nbsp;");
				if(nline != null && nline.length() > 1){
					message = message + "\r\n";
				}

				if(underline != null && underline.equals("X")){
					message = "<u>" + message + "</u>";
				}

				if(fontSize != null){
					int_fontSize = Integer.parseInt(fontSize);
					if(int_fontSize > 10){
						fontSize = "<big>";
					} else if(int_fontSize < 10){
						fontSize = "<small>";
					} else if(int_fontSize == 10){
					    fontSize = "<normal>";
                    }
				}

                String color = hashMap.get("COLOR");
                message = "<font " +"color='" + color +"'>" + message + "</font>";
                if(fontSize != null && fontSize.equals("<big>")){
                	message = fontSize + message + "</big>";
				} else if(fontSize != null && fontSize.equals("<small>")){
                	message = "<small>" + message + "</small>";
				} else if(fontSize != null && fontSize.equals("<normal>")){
                    message = "<normal>" + message + "</normal>";
                }


				String newMessage = newList.get(newList.size()-1).get("MESSAGE");
                newMessage = newMessage + message;
				newList.get(newList.size() - 1).put("MESSAGE", newMessage);
			} else {
				String message = hashMap.get("MESSAGE");
				String fontSize = hashMap.get("PSIZE");
				String underline = null;
				String nline = null;
				underline = hashMap.get("ULINE");
				nline = hashMap.get("NLINE");

				message = message.replaceAll(" ","&nbsp;");

				if(nline != null && nline.length() > 1){
					message = message + "\r\n";
				}
				if(underline != null && underline.equals("X")){
					message = "<u>" + message + "</u>";
				}

				int int_fontSize = 10;
				if(fontSize != null){
					int_fontSize = Integer.parseInt(fontSize);
					if(int_fontSize > 10){
						fontSize = "<big>";
					} else if(int_fontSize < 10){
						fontSize = "<small>";
					} else if(int_fontSize == 10){
						fontSize = "<normal>";
					}
				}

				String color = hashMap.get("COLOR");
				message = "<font " +"color='" + color +"'>" + message + "</font>";
				if(fontSize != null && fontSize.equals("<big>")){
					message = fontSize + message + "</big>";
				} else if(fontSize != null && fontSize.equals("<small>")){
					message = "<small>" + message + "</small>";
				} else if(fontSize != null && fontSize.equals("<normal>")){
					message = "<normal>" + message + "</normal>";
				}

				hashMap.put("MESSAGE", message);
//				_list.set(i, hashMap);
				newList.add(hashMap);

			}
//			for(int j=0; j<newList.size(); j++){
//				HashMap<String, String> hashmap2 = newList.get(j);
//				String new_minor = hashmap2.get("MINOR");
//				if(new_minor.equals(min)){
//			}
		}

		tlda = new Mistery_Shopping_List_Dialog_Adapter(context,
				R.layout.mistery_shopping_dialog_row, newList);
		lv_list.setAdapter(tlda);


//        mHandler = new Handler() {
//            public void handleMessage(Message msg) {
//                int height = getListViewHeight(lv_list);
//                LayoutParams listParams = (LayoutParams) lv_list.getLayoutParams();
//                LayoutParams params = (LayoutParams) major.getLayoutParams();
//                params.height = height / 2;
//                major.setLayoutParams(params);
//            }
//        };
//
//        mHandler.sendEmptyMessageDelayed(0, 100);
//
	}

    private int getListViewHeight(ListView list) {
        ListAdapter adapter = list.getAdapter();

        int listviewHeight = 0;

        list.measure(View.MeasureSpec.makeMeasureSpec(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));

        listviewHeight = list.getMeasuredHeight() * adapter.getCount();

        return listviewHeight;
    }


	private Mistery_Shopping_Dialog.OnClickRowListener mOnClickRowListener;

	public void setOnClickRowListener(Mistery_Shopping_Dialog.OnClickRowListener clickRowListener) {
		mOnClickRowListener = clickRowListener;
	}

	public interface OnClickRowListener {
		void onClickRowListener(int position);
	}
}
