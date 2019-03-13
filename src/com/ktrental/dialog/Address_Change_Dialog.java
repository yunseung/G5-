package com.ktrental.dialog;

import android.app.ProgressDialog;
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
import android.widget.TextView;

import com.ktrental.R;
import com.ktrental.beans.PM013;
import com.ktrental.cm.connect.ConnectController;
import com.ktrental.cm.connect.Connector.ConnectInterface;
import com.ktrental.cm.connect.ConnectorUtil;
import com.ktrental.cm.db.DbAsyncTask;
import com.ktrental.cm.db.DbAsyncTask.DbAsyncResLintener;
import com.ktrental.common.DEFINE;
import com.ktrental.common.KtRentalApplication;
import com.ktrental.model.DbQueryModel;
import com.ktrental.model.TableModel;
import com.ktrental.popup.EventPopupC;
import com.ktrental.popup.InventoryPopup;
import com.ktrental.util.kog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

public class Address_Change_Dialog extends DialogC implements ConnectInterface,DbAsyncResLintener,
		View.OnClickListener {
	private Window w;
	private WindowManager.LayoutParams lp;
	private Context context;
	private ProgressDialog pd;
	private ConnectController cc;
	private Button bt_close;
	public ArrayList<HashMap<String, String>> array_hash;

	private TextView tv_carnum;
	private TextView text_customer;
	private TextView text_drvName;
	private TextView tv_name2;
	private TextView tv_addr1;

	private EditText edit_drvName;
	private TextView tv_2phone;
	private Button tv_2addr1;
	private TextView tv_2addr2;
	// private EditText tv_2addr3;
	private TextView tv_2mot1;
	private TextView tv_2mot2;
	// private EditText tv_2mot3;

	private HashMap<String, String> o_struct1;
	private HashMap<String, String> o_struct2;
	private HashMap<String, String> o_struct3;
	private HashMap<String, String> o_struct4;
	
	private InventoryPopup ip;
	
	private Address_Dialog ad;

	private String mot;

	private String equnr;
	
	/**
	 * 내가할려는건 고객조회에서 올경우와 아닌경우를 판단한다.
	 * 고객조회가아니라 로 왔는데 내꺼에 없으면 true
	 * ->false
	 * 근데 내꺼에 없어? 그러면 true하고 받어온데이터 고객조회화면에서 걔로 매핑을한다.
	 * 그외  왔는데 무조건 로컬이겠지? 왜냐면 로컬외에는 볼수가없으니깐?
	 * false
	 * 변수가다르다?
	 */
	
	public Address_Change_Dialog(Context context,
			HashMap<String, String> o_struct1) {
		super(context);
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.address_change_dialog);
		
		w = getWindow();
		w.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN
				| WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
		w.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
		lp = w.getAttributes();
		lp.width = LayoutParams.MATCH_PARENT;
		lp.height = LayoutParams.MATCH_PARENT;
		w.setAttributes(lp);
		this.context = context;

		pd = new ProgressDialog(context);
		pd.setCancelable(false);
		pd.setMessage("검색중입니다.");

		this.o_struct1 = o_struct1;
		this.o_struct2 = o_struct2;
		this.o_struct3 = o_struct3;
		this.o_struct4 = o_struct4;

		
		
		bt_close = (Button) findViewById(R.id.address_change_close_id);
		bt_close.setOnClickListener(this);

		cc = new ConnectController(this, context);
		
		ip = new InventoryPopup(context, InventoryPopup.HORIZONTAL, R.layout.inventory_popup, InventoryPopup.TYPE_PHONE_NUMBER);

		tv_carnum = (TextView) findViewById(R.id.address_change_carnum_id);
		text_customer = (TextView) findViewById(R.id.address_change_num_id);
		text_drvName = (TextView) findViewById(R.id.address_change_name1_id);
		tv_name2 = (TextView) findViewById(R.id.address_change_name2_id);
		tv_addr1 = (TextView) findViewById(R.id.address_change_addr1_id);

		edit_drvName = (EditText) findViewById(R.id.address_change2_name_id);
		tv_2phone = (TextView) findViewById(R.id.address_change2_phone_id);
		tv_2phone.setOnClickListener(this);
		tv_2addr1 = (Button) findViewById(R.id.address_change2_addr1_id);
		tv_2addr1.setOnClickListener(this);
		tv_2addr2 = (TextView) findViewById(R.id.address_change2_addr2_id);
		// tv_2addr3 = (EditText)findViewById(R.id.address_change2_addr3_id);
		tv_2mot1 = (TextView) findViewById(R.id.address_change2_mot1_id);
		tv_2mot2 = (TextView) findViewById(R.id.address_change2_mot2_id);
		// tv_2mot3 = (EditText)findViewById(R.id.address_change2_mot3_id);
		
			//나는 로컬입니다 하고온얘겠지?
			//여기서에서 차량번호 가지고 내 로컬 DB를 쿼리한다.
			//쿼리를 하고 난 후에 데이터를 매핑한다 지금까지있었던건 다 무시함.
			// o_struct1.get("INVNR")쿼리를 돌리는 키
		
		queryCarInfo(o_struct1.get("INVNR"), o_struct1.get("MAKTX"));
			
		
		/*
		tv_carnum.setText(o_struct1.get("INVNR")); // 고객차량번호
		text_customer.setText(o_struct1.get("KUNNR_NM")); // 계약자명
		text_drvName.setText(o_struct1.get("DLSM1")); // 운전자명
		kog.e("KDH", "DLST1 = "+o_struct1.get("DLST1"));
		tv_name2.setText(o_struct1.get("DLST1")); // 운전자연락처
		String tempAddress = o_struct1.get("CUSJUSO");
		
		Set <String> set  = o_struct1.keySet();
		Iterator <String> it = set.iterator();
		String key;
		
		while(it.hasNext())
		{
			key = it.next();
			kog.e("Jonathan", " 뿌려주는 값   key === " + key + "    value  === " + o_struct1.get(key));
		}
		
		
//		queryCarInfo(o_struct1.get("INVNR"), o_struct1.get("CARNAME"));
		//myung 20131129 ADD 예외처리
		if(tempAddress.equals("") || tempAddress.equals(" ") || tempAddress.length()<8){
			tv_addr1.setText(""); // 고객소재지
		}
		else
		{
//			queryMaintenace(o_struct1.get("DLSM1"),o_struct1.get("INVNR"));
			
			// myung 20131125 UPDATE 변경전 주소 표시 형태 변경. [우편번호] 주소…. 형태로 변경
			tempAddress = o_struct1.get("PostCode")
					+o_struct1.get("City")
					+o_struct1.get("Street");
//			tempAddress = "[" + tempAddress.substring(0, 3) + "-"
//					+ tempAddress.substring(4, 7) + "] " + tempAddress.substring(7);
//			tv_addr1.setText(tempAddress); // 고객소재지
		}

//		DLSM1
		String INVNR = o_struct1.get("INVNR");
		String MAKTX = o_struct1.get("MAKTX");
		
		queryCarInfo(INVNR, MAKTX);
		initPM013();
		*/
		
	}
	
	String currentQueryCarInfo = "carInfo";
	
//	private CarInfoModel mCarInfoModel;
	
	private void queryCarInfo(String carNum, String carName) {
//		showProgress("차량정보를 조회 중입니다.");

		String[] _whereArgs = { carNum, carName };
		String[] _whereCause = { DEFINE.INVNR, DEFINE.MAKTX };

		String[] colums = maintenace_colums;

		DbQueryModel dbQueryModel = new DbQueryModel(
				ConnectController.REPAIR_TABLE_NAME, _whereCause, _whereArgs,
				colums);

		DbAsyncTask dbAsyncTask = new DbAsyncTask(currentQueryCarInfo,
				mContext, this, dbQueryModel);

		dbAsyncTask.execute(DbAsyncTask.DB_SELECT);
	}
	
	String KUNNR_NM;
	
	private void queryResultCarInfo(Cursor cursor) {

		kog.e("KDH", "queryResultCarInfo");
		if (cursor == null)
			return;
		kog.e("KDH", "queryResultCarInfo11111");
		
		cursor.moveToFirst();

		ArrayList<String> array = new ArrayList<String>();
		kog.e("KDH", "queryResultCarInfo22222222");
		kog.e("Jonathan", "cursor.count()  == " + cursor.getCount()	);
		
		
		//Jonathan 추가 14.06.09
		if(cursor.getCount() == 0)
		{
			//실제로 로컬에서 0이 나오면 o_struct1 에서 다 처리를 해야한다.
			//이경우에는 서버에서 데이터를 받어온 경우에만 해당이된다.
			//설령 데이터를 서버에서 받아왔다고 하더라도 1이나오면 로컬에서 있으므로 그것을 사용하면 된다.
			
			Set <String> set  = o_struct1.keySet();
			Iterator <String> it = set.iterator();
			String key;
			
			while(it.hasNext()) 
			{
				key = it.next();
				kog.e("Jonathan", "여기 맞아? key ===1111  " + key + "    value  === " + o_struct1.get(key));
			}
			 
			String address;
			address = "[" + o_struct1.get("PSTLZ2") + "]" + o_struct1.get("ORT02") + o_struct1.get("STRAS2");
			
			mCity = o_struct1.get("ORT02");
			mStreet = o_struct1.get("STRAS2");
			mPostCode = o_struct1.get("PSTLZ2");
			
			
			
			tv_carnum.setText(o_struct1.get("INVNR")); // 고객차량번호
			text_drvName.setText(o_struct1.get("DLSM1")); // 운전자명
			tv_name2.setText(o_struct1.get("DLST1")); // 운전자연락처
			
			
			
			tv_addr1.setText(address);
			edit_drvName.setText(o_struct1.get("DLSM1")); // 운전자명
			tv_2phone.setText(o_struct1.get("DLST1"));
			text_customer.setText(o_struct1.get("NAME1")); // 고객명 -> 원래 계약자 명인데 대체.
			tv_2addr1.setText(o_struct1.get("PSTLZ2"));
			tv_2addr2.setText(o_struct1.get("ORT02")+o_struct1.get("STRAS2"));
		}
		else
		{
			while (!cursor.isAfterLast()) {

				for (int i = 0; i < cursor.getColumnCount(); i++) {
					kog.e("KDH", maintenace_colums[i]+ i +"  :   "+cursor.getString(i));
					String str = decrypt(maintenace_colums[i], cursor.getString(i));

					array.add(str);
					kog.e("KDH", "i = "+i+"   data = "+str);
				}
				kog.e("KDH", "queryResultCarInfo33333333333");
				String invnr = KtRentalApplication.getLoginModel().getInvnr();
				/*
				mCarInfoModel = new CarInfoModel(array.get(2), array.get(0),
						array.get(31) + array.get(32) + array.get(33),
						array.get(3), "시간", array.get(1), array.get(44),
						array.get(37), array.get(4), array.get(5), invnr,
						array.get(6), array.get(7), array.get(8), array.get(9),
						//myung 20131209 UPDATE 순회정비결과등록 > 차량기본정보 계약구분 값에 BSARK_TX 필드만 입력
//						array.get(11) + "/" + array.get(11), array.get(12),
						array.get(11), array.get(12),
						
						array.get(13), array.get(14), array.get(15) + " / "
								+ array.get(16), array.get(17), array.get(18),
						array.get(20), array.get(21), array.get(22), array.get(23),
						array.get(24), array.get(25), array.get(26), array.get(27),
						array.get(28), array.get(29), array.get(30), array.get(34),
						array.get(35), array.get(36), array.get(19), array.get(38),
						array.get(39), array.get(40), array.get(41), array.get(42),
						array.get(43), array.get(31), array.get(32), array.get(33),
						array.get(45),array.get(46),"", "");
						 */
//				mCarInfoModel.setGUBUN(mGubun);
//				mCarInfoModel.setImageName(mCarInfoModel.getAUFNR() + mImageName);
				cursor.moveToNext();
			}
			cursor.close();
			// queryLast("11허2224"); // 실제데이터가 적어 테스트용 하드코딩.
			kog.e("KDH", "queryResultCarInfo4444444");
			kog.e("KDH", "array.get(47) === " + array.get(47));
			KUNNR_NM  = array.get(47);
			
			tv_carnum.setText(o_struct1.get("INVNR")); // 고객차량번호
			
			//기본
			text_customer.setText(KUNNR_NM); // 고객명
//			tv_name2.setText(mCarInfoModel.getDrv_mob()); //운전자연락처
			//2014-05-21 KDH 내가 작업한건데 일단 다시 넘겨서 받어서 뿌리는걸로 변경
			tv_name2.setText(array.get(45)); //운전자연락처
			
			// Jonathan 2014.06.13 주석처리, 추가
			text_drvName.setText(array.get(2)); //운전자명			//14.06.15 여기는 CarInfo에서 받아온다.
//			text_drvName.setText(o_struct1.get("DLSM1"));
			
			//소재지변경
			// Jonathan 2014.06.13 주석처리, 추가
			edit_drvName.setText(array.get(2)); // 운전자명
//			edit_drvName.setText(o_struct1.get("DLSM1"));
			
			tv_2phone.setText(array.get(45)); // 운전자연락처
		
			mPostCode = array.get(31);
			mCity = array.get(32);
			mStreet = array.get(33);

			tv_addr1.setText("["+mPostCode+"] "+mCity+mStreet); // 고객소재지
			tv_2addr1.setText(mPostCode);
			tv_2addr2.setText(mCity+mStreet);
		}
		
// Jonathan 14.06.09 주석시작
//		while (!cursor.isAfterLast()) {
//
//			for (int i = 0; i < cursor.getColumnCount(); i++) {
//				kog.e("KDH", maintenace_colums[i]+ i +"  :   "+cursor.getString(i));
//				String str = decrypt(maintenace_colums[i], cursor.getString(i));
//
//				array.add(str);
//
//			}
//			kog.e("KDH", "queryResultCarInfo33333333333");
//			String invnr = KtRentalApplication.getLoginModel().getInvnr();
//
//			mCarInfoModel = new CarInfoModel(array.get(2), array.get(0),
//					array.get(31) + array.get(32) + array.get(33),
//					array.get(3), "시간", array.get(1), array.get(44),
//					array.get(37), array.get(4), array.get(5), invnr,
//					array.get(6), array.get(7), array.get(8), array.get(9),
//					//myung 20131209 UPDATE 순회정비결과등록 > 차량기본정보 계약구분 값에 BSARK_TX 필드만 입력
////					array.get(11) + "/" + array.get(11), array.get(12),
//					array.get(11), array.get(12),
//					
//					array.get(13), array.get(14), array.get(15) + " / "
//							+ array.get(16), array.get(17), array.get(18),
//					array.get(20), array.get(21), array.get(22), array.get(23),
//					array.get(24), array.get(25), array.get(26), array.get(27),
//					array.get(28), array.get(29), array.get(30), array.get(34),
//					array.get(35), array.get(36), array.get(19), array.get(38),
//					array.get(39), array.get(40), array.get(41), array.get(42),
//					array.get(43), array.get(31), array.get(32), array.get(33),
//					array.get(45),array.get(46),"", "");
////			mCarInfoModel.setGUBUN(mGubun);
////			mCarInfoModel.setImageName(mCarInfoModel.getAUFNR() + mImageName);
//			cursor.moveToNext();
//		}
//		cursor.close();
//		// queryLast("11허2224"); // 실제데이터가 적어 테스트용 하드코딩.
//		kog.e("KDH", "queryResultCarInfo4444444");
//		kog.e("KDH", "array.get(47) === " + array.get(47));
//		KUNNR_NM  = array.get(47);
//		
//		if(mCarInfoModel != null)
//		{
//			kog.e("KDH", "getCarname = "+mCarInfoModel.getCarname());
//			kog.e("KDH", "getName = "+mCarInfoModel.getName());
//			kog.e("KDH", "getCustomerName = "+mCarInfoModel.getCustomerName());
//			kog.e("KDH", "getPostCode = "+mCarInfoModel.getPostCode());
//			kog.e("KDH", "getCity = "+mCarInfoModel.getCity());
//			kog.e("KDH", "getStreet = "+mCarInfoModel.getStreet());
//			kog.e("KDH", "getDrv_tel = "+mCarInfoModel.getDrv_tel());
//
//			//기본
//			text_customer.setText(KUNNR_NM); // 고객명
////			tv_name2.setText(mCarInfoModel.getDrv_mob()); //운전자연락처
//			//2014-05-21 KDH 내가 작업한건데 일단 다시 넘겨서 받어서 뿌리는걸로 변경
//			tv_name2.setText(o_struct1.get("DLST1")); //운전자연락처
//			text_drvName.setText(mCarInfoModel.getName()); //운전자명
//			
//			//소재지변경
//			edit_drvName.setText(mCarInfoModel.getName()); // 운전자명
//			tv_2phone.setText(o_struct1.get("DLST1")); // 운전자연락처
//		
//			mPostCode = mCarInfoModel.getPostCode();
//			mCity = mCarInfoModel.getCity();
//			mStreet = mCarInfoModel.getStreet();
//
//			tv_addr1.setText("["+mPostCode+"] "+mCity+mStreet); // 고객소재지
//			tv_2addr1.setText(mPostCode);
//			tv_2addr2.setText(mCity+mStreet);
//		}
		
		//Jonathan 14.06.09 주석 끝
		
		
		
		// boolean Flag = mAsyncMap.containsKey(funName);
		//
		// if (!Flag) {
		// return;
		// }
	}

	@Override
	public void connectResponse(String FuntionName, String resultText,
			String MTYPE, int resulCode, TableModel tableModel) {
//		Log.i("#", "####" + FuntionName + "/" + resultText + "/" + MTYPE + "/"
//				+ resulCode);
		hideProgress();
		if (MTYPE != null && !MTYPE.equals("S")) {
			
			cc.duplicateLogin(mContext);
			
			EventPopupC epc = new EventPopupC(context);
			epc.setTitle(resultText);
			epc.show();
			return;
		}
		if (FuntionName.equals("ZMO_1040_RD01")) {
			array_hash = tableModel.getTableArray();
			if (array_hash == null || array_hash.size() <= 0)
				return;
			HashMap hm = array_hash.get(0);
			tv_2mot2.setText(hm.get("SNAME").toString());
//			Log.i("#", "####MOT번호" + hm.get("INGRP"));

			//myung 20131211 UPDATE MOT Connecting Value
			
			// Jonathan 추가
			if(hm.containsKey("CCMSTS"))
			{
				ccmsts = hm.get("CCMSTS").toString();
			}
			mot = hm.get("INGRP").toString();
			equnr = hm.get("EQUNR").toString();
			tv_2mot1.setText(getChangeZCODEVT_MOT(hm.get("INGRP").toString()));
			
			cc.duplicateLogin(mContext);
			
		}
	}

	private String getChangeZCODEVT_MOT(String _mot_num) {
		String mot = null;
		if (pm013_arr == null || pm013_arr.size() <= 0)
			return null;
		for (int i = 0; i < pm013_arr.size(); i++) {
			if (pm013_arr.get(i).ZCODEV.equals(_mot_num)) {
				mot = pm013_arr.get(i).ZCODEVT;
				break;
			}
		}
		return mot;
	}

	String TABLE_NAME = "O_ITAB1";
	public ArrayList<PM013> pm013_arr;

	private ArrayList<PM013> initPM013() {
		pm013_arr = new ArrayList<PM013>();
		String path = context.getExternalCacheDir() + "/DATABASE/"
				+ DEFINE.SQLLITE_DB_NAME;
		SQLiteDatabase sqlite = SQLiteDatabase.openDatabase(path, null,
				SQLiteDatabase.OPEN_READONLY);
		Cursor cursor = sqlite.rawQuery("select ZCODEV, ZCODEVT from "
				+ TABLE_NAME + " where ZCODEH = 'PM013'", null);
		if (cursor == null)
			return pm013_arr;
		PM013 pm;
		while (cursor.moveToNext()) {
			int calnum1 = cursor.getColumnIndex("ZCODEV");
			int calnum2 = cursor.getColumnIndex("ZCODEVT");
			String zcodev = cursor.getString(calnum1);
			String zcodevt = cursor.getString(calnum2);
			pm = new PM013();
			pm.ZCODEV = zcodev;
			pm.ZCODEVT = zcodevt;
			pm013_arr.add(pm);
		}
		cursor.close();
//		sqlite.close();

		return pm013_arr;
	}

	@Override
	public void reDownloadDB(String newVersion) {
	}

	@Override
	public void onClick(View v) {
		ViewGroup vg;
		switch (v.getId()) {
		case R.id.address_change_close_id: // 닫기
			dismiss();
			break;
		case R.id.address_change2_addr1_id: // 주소검색
			tv_2addr1.setText("");
			tv_2addr2.setText("");
			
			ad = new Address_Dialog(context);
			Button bt_save = (Button) ad.findViewById(R.id.address_save_id);
			bt_save.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					HashMap hm = ad.getTv_full_address();
					//myung 20131209 ADD 주소 미선택 시 에러 종료 처리
					if(hm==null){
						EventPopupC epc = new EventPopupC(context);
						epc.setTitle("주소를 선택해 주세요");
						epc.show();
						return;
					}
					if (hm.size() <= 0 ) {
						EventPopupC epc = new EventPopupC(context);
						epc.setTitle("주소를 정제해 주세요");
						epc.show();
						return;
					}
					String zip_code = hm.get("POST_CODE1").toString();
					tv_2addr2.setText(hm.get("CITY1").toString() + " "
							+ hm.get("STREET").toString());
					tv_2addr1.setText(zip_code);
					ad.dismiss();

					// Mot검색

					showProgress("검색중입니다.");
					cc.getZMO_1040_RD01(zip_code);
				}
			});
			ad.show();
			break;
		//myung 20131127 ADD 소재지 변경의 연락처 터치 시 전화번호 입력 키패드 팝업추가.
		case R.id.address_change2_phone_id: // 운전자 연락처
			vg = ip.getViewGroup();
			Button done;
			final TextView input4 = (TextView) vg.findViewById(R.id.inventory_bt_input);
            done = (Button) vg.findViewById(R.id.inventory_bt_done);
            done.setOnClickListener(new View.OnClickListener()
                {
                @Override
                public void onClick(View v) 
                    {
                    String phoneNum = input4.getText().toString();
                    kog.e("KDH", "phoneNum = "+phoneNum);
                    
                    tv_2phone.setText(phoneNum);
                    ip.setInput("CLEAR", true);
                    ip.dismiss();
                    }
                });
            ip.show(tv_2phone);
			
			break;
		}
	}
	
	private String[] maintenace_colums = { DEFINE.INVNR, DEFINE.MAKTX,
			DEFINE.DRIVN, DEFINE.DRV_MOB, DEFINE.PARNR2_TX, DEFINE.MOB_NUMBER2,
			DEFINE.TIRFR, DEFINE.TIRBK, DEFINE.INPML, DEFINE.VBELN,
			DEFINE.BSARK, DEFINE.BSARK_TX, DEFINE.PERNR_TX, DEFINE.VKGRP1_TX,
			DEFINE.VKGRP_TX, DEFINE.INNAM, DEFINE.ENAME, DEFINE.USRID,
			DEFINE.GUEBG2, DEFINE.GUEEN2, DEFINE.JOINDT, DEFINE.PMFRE_TX,
			DEFINE.MATMA_TX, DEFINE.CYCMN_TX, DEFINE.NOTIR_TX, DEFINE.LNTMA_TX,
			DEFINE.EXHIB_TX, DEFINE.GENMA_TX, DEFINE.BKTIR, DEFINE.EMCAL_TX,
			DEFINE.SNWTR, DEFINE.POST_CODE, DEFINE.CITY, DEFINE.STREET,
			DEFINE.MDLCD, DEFINE.OILTYP, DEFINE.AUFNR, DEFINE.GSTRS,
			DEFINE.OILTYPNM, DEFINE.EQUNR, DEFINE.CTRTY, DEFINE.CEMER,
			DEFINE.CHNGBN, DEFINE.OWNER, DEFINE.CCMSTS, DEFINE.DRV_TEL, 
			DEFINE.DRV_MOB, DEFINE.KUNNR_NM
			};

	///Jonathan  추가
	String ccmsts;
	
	String mPostCode;
	String mCity;
	String mStreet;
	
	
	protected String decrypt2(String value)
	{
		String reStr = null;
		reStr = ConnectorUtil.decrypt(value);
		return reStr;
	}
	
	
	protected String decrypt(String key, String value) {

		String reStr = value;

		if (KtRentalApplication.isEncoding(key)) {
			reStr = ConnectorUtil.decrypt(value);
		}

		return reStr;
	}
	
	

	public String getMot() {
		return mot;
	}

	public void setMot(String mot) {
		this.mot = mot;
	}

	public String getEqunr() {
		return equnr;
	}

	public void setEqunr(String equnr) {
		this.equnr = equnr;
	}
	
	
	///Jonathan  추가
	public String getCCMSTS() {
		
		return o_struct1.get("CCMSTS");
	}
	
	public void setCCMSTS(String ccmsts) {
		this.ccmsts = ccmsts;
	}

	
	
	
	public String getINVNR() {
		
		return o_struct1.get("INVNR");
	}

	public String getPrePost(){
		return mPostCode;
	}

	public String getPreCity(){
		return mCity;
//		return o_struct1.get("ORT02");
	}

	public String getPreStreet(){
		return mStreet;
//		return o_struct1.get("STRAS2");
	}
	public String getPreDrivn(){
		String ret = null;
		if(text_drvName != null && text_drvName.getText() != null){
			ret = text_drvName.getText().toString();
		} else {
			;
		}
		return ret;
//		return o_struct1.get("DLSM1");
	}
	public String getPreTelNo(){
		String ret = null;
		if(tv_name2 != null && tv_name2.getText() != null){
			ret = tv_name2.getText().toString();
		} else {
			;
		}
		return ret;
//		return o_struct1.get("DLST1");
	}

	public String getDrivn() {
		Object data = edit_drvName.getText();
		return data != null || !data.toString().equals("") ? data.toString()
				: "";
	}

	public String getTel_No() {
		Object data = tv_2phone.getText();
		return data != null || !data.toString().equals("") ? data.toString()
				: "";
	}

	public String getPost() {
		Object data = tv_2addr1.getText();
		return data != null || !data.toString().equals("") ? data.toString()
				: "";
	}

	public String getCity1() {
		if(ad == null || ad.getTv_full_address() == null)
		{
			return mCity;
		}
		else
		{
			HashMap<String, String> hm = ad.getTv_full_address();
			if(hm != null && hm.size() > 0)
			{
				return hm.get("CITY1").toString();
			}
			else{
				return "";
			}
		}
		/*
		HashMap<String, String> hm = null;
		try {
			hm = ad.getTv_full_address();
		} catch (Exception e) {
			e.printStackTrace();
			return mCity;
		}
		//myung 20131127 ADD 예외처리
		if(hm == null || hm.get("CITY1")==null)
			return mCity;
		return hm.get("CITY1").toString();
		*/
	}

	public String getStreet() {
		if(ad == null || ad.getTv_full_address() == null)
		{
			return mStreet;
		}
		else
		{
			HashMap<String, String> hm = ad.getTv_full_address();
			if(hm != null && hm.size() > 0)
			{
				return hm.get("STREET").toString();
			}
			else
			{
				return "";
			}
		}
	}

	@Override
	public void onCompleteDB(String funName, int type, Cursor cursor,
			String tableName) {
		// TODO Auto-generated method stub
		if (funName.equals(currentQueryCarInfo)) {
			kog.e("KDH", "cursor= "+cursor);
			queryResultCarInfo(cursor);
		}
		
		
	}

	// 키보드내리기
	// Handler pre = new Handler()
	// {
	// @Override
	// public void handleMessage(Message msg)
	// {
	// super.handleMessage(msg);
	// InputMethodManager imm = (InputMethodManager)
	// context.getSystemService(Context.INPUT_METHOD_SERVICE);
	// imm.hideSoftInputFromWindow(et_input.getWindowToken(), 0);
	// }
	// };

}
