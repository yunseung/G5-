package com.ktrental.cm.connect;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;
import android.provider.Settings.Secure;
import android.util.Log;

import com.ktrental.activity.RootingCheck;
import com.ktrental.cm.connect.Connector.ConnectInterface;
import com.ktrental.common.DEFINE;
import com.ktrental.common.KtRentalApplication;
import com.ktrental.model.CorCardModel;
import com.ktrental.model.LoginModel;
import com.ktrental.model.MovementSaveModel;
import com.ktrental.util.kog;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

//import android.app.Activity;

/**
 * RFC 모듈 연동 컨트롤러 <br/>
 * 현재 Connector ({@link Connector})을 이용하여 함수 코드명을 이용하여 기능 수행.
 * 
 * 
 * 
 * @author Hong
 * @since 2013.07.12
 */
public class ConnectController {

	public final static String LOGIN_FUNTION_NAME = "ZMO_1010_RD01";
	public final static String COMMONCHECK_FUNTION_NAME = "ZMO_9980_RD04";
	public final static String REPAIR_FUNTION_NAME = "ZMO_1020_RD02_2";

	public final static String O_ITAB1_TABLE_NAME = "O_ITAB1";

	public final static String REPAIR_TABLE_NAME = "REPAIR_TABLE";

	// 주소검색
	public final static String  ADDRESS_ALL_FUNTION_NAME = "ZMO_9010_RD03";
	public final static String ADDRESS_STREET_FUNTION_NAME = "ZMO_9010_RD02";
	public final static String ADDRESS_LOTNUMBER_FUNTION_NAME = "ZMO_9010_RD01";

	public final static String ADDRESS_ALL_TABLE_NAME = "address_all_table";
	public final static String ADDRESS_STREET_TABLE_NAME = "address_street_table";
	public final static String ADDRESS_LOTNUMBER_TABLE_NAME = "address_lotnumber_table";

	// 이관등록
	public final static String TRANSFER_FUNTION_NAME = "ZMO_1040_WR01";
	public final static String TRANSFER_FUNTION_TABLE = "PARTS_TRANSFER_FUNTION_TABLE";

	// 결과등록
	public final static String SEND_FUNTION_NAME = "ZMO_1050_WR01";
	public final static String SEND_FUNTION_NAME_TABLE = "SEND_FUNTION_NAME_TABLE";

	// 결과조회
	public final static String GET_FUNTION_NAME = "ZMO_1050_RD01";
	public final static String GET_FUNTION_NAME_TABLE = "GET_FUNTION_NAME_TABLE";
	// 결과취소
	public final static String CANCEL_FUNTION_NAME = "ZMO_1050_WR02";
	public final static String CANCEL_FUNTION_NAME_TABLE = "CANCEL_FUNTION_NAME_TABLE";

	public final static String ADDRESS_TO_WGS = "ZMO_9010_RD03";
	public final static String ADDRESS_TO_WGS_TABLE = "ADDRESS_TO_WGS_TABLE";

	public final static String PERFORMANCE_FUNTION_NAME = "ZMO_1100_RD01";
	public final static String PERFORMANCE_FUNTION_TABLE = "PERFORMANCE_FUNTION_TABLE";

	public final static String MOVEMENT_FUNTION_NAME = "ZMO_1110_RD01";
	public final static String MOVEMENT_FUNTION_TABLE = "MOVEMENT_FUNTION_TABLE";

	public final static String MOVEMENT_SAVE_FUNTION_NAME = "ZMO_1110_WR01";
	public final static String MOVEMENT_SAVE_FUNTION_TABLE = "MOVEMENT_SAVE_FUNTION_TABLE";
	
	// 2014.01.09	ypkim
	public final static String MOVEMENT_DELETE_FUNTION_NAME = "ZMO_1110_WR02";
	public final static String MOVEMENT_DELETE_FUNTION_TABLE = "MOVEMENT_DELETE_FUNTION_TABLE";

	
	//2014-01-27 KDH 이게뭘까-ㅇ-?
	public final static String ZMO_3220_RD01 = "ZMO_3220_RD01";
	public final static String ZMO_3220_RD01_TABLE = "ZMO_3220_RD01_TABLE";
	
	//2014-02-28 KDH 타이어신청내역 추가
	public final static String ZMO_1130_WR01 = "ZMO_1130_WR01";
	public final static String ZMO_1130_WR01_TABLE = "ZMO_1130_WR01_TABLE";
	
	
	
	//20161122 Jonathan 중복 체크 추가
	public final static String ZMO_1010_RD02 = "ZMO_1010_RD02";
	public final static String ZMO_1010_RD02_TABLE = "ZMO_1010_RD02_TABLE";
	
	
	//20161122 Jonathan 중복 체크 추가
	public final static String ZMO_1010_WR02 = "ZMO_1010_WR02";
	public final static String ZMO_1010_WR02_TABLE = "ZMO_1010_WR02_TABLE";
		
	private Connector mConnector;

	private static ConnectController mConnectController;

	private static String[] login_table_names = { "O_ITAB1", "O_ITAB2" };

	public static ConnectController getInstance() {
		return mConnectController;
	}

	public ConnectController(ConnectInterface connectInterface, Context context) {
		mConnectController = this;
		initConnctAdapter(connectInterface, context);
	}

	private void initConnctAdapter(ConnectInterface connectInterface,
			Context context) {
		mConnector = new Connector(connectInterface, context);
	}
	
	
	private String getNowTime() {

        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("HHmm", Locale.KOREA);
        return format.format(date);
    }
	
	
	
	
	public void duplicateLogin(Context context)
	{
		HashMap<String, String> map = getCommonConnectData();
		
		kog.e("Joanthan", "duplicate connectcontroller1");

		mConnector.setParameter("I_USR_ID", map.get("LOGID").toString());
		mConnector.setParameter("I_SYS_CD", "IN3000");
		mConnector.setStructure("IS_LOGIN", map);


		Log.e("Joanthan", "ZMO_1010_RD02 1::  " + map.get("LOGID").toString());
		Log.e("Joanthan", "ZMO_1010_RD02 2::  " + map.get("LOGID").toString());
		Log.e("Joanthan", "ZMO_1010_RD02 3::  " + map);



		try {
			mConnector.executeRFCAsyncTask(ZMO_1010_RD02, ZMO_1010_RD02_TABLE);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	
	public void updateSession(Context context, String inout, String session)
	{
		String strFuncName = ZMO_1010_WR02;
		HashMap<String, String> map = getCommonConnectData();
		
		mConnector.setStructure("IS_LOGIN", map);

		
		mConnector.setParameter("I_GUBUN", inout);
		mConnector.setParameter("I_SYS_CD", "IN3000");
		mConnector.setParameter("I_USR_ID", map.get("LOGID").toString());
		mConnector.setParameter("I_SES_ID", session);
		mConnector.setParameter("I_SYS_IP", getAndroidID(context));
		
		
		


		try {
			mConnector.executeRFCAsyncTask(strFuncName, ZMO_1010_WR02_TABLE);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	

	// 로그인
	public void logIn(String id, String passWord, String session, Context context) {
		String strFuncName = LOGIN_FUNTION_NAME;

		// HashMap<String, String> map = getCommonConnectData();
		// mConnector.setStructure("IS_LOGIN", map);

		
		
		
		mConnector.setParameter("I_USR_ID", id);
		mConnector.setParameter("I_PWD", passWord);
		mConnector.setParameter("I_SYS_CD", "IN3000");
//		mConnector.setParameter("I_SYSIP", DEFINE.MW_HOST_IP);
		mConnector.setParameter("I_SYSIP", getAndroidID(context));
		mConnector.setParameter("I_DTOKEN", " ");
		mConnector.setParameter("I_SERIAL", getAndroidID(context));
		mConnector.setParameter("I_OS_VER", Build.VERSION.RELEASE + "/"
				+ getAppVer(context));
		mConnector.setParameter("I_HACK_YN", isPhoneRootingCheck());
		mConnector.setParameter("I_SES_ID", session);

		// Log.i("####",
		// "####"+getAndroidID(context)+"/"+Build.VERSION.RELEASE+"/"+getAppVer(context)+"/"+isPhoneRootingCheck());

		String phoneNumber = KtRentalApplication.getInstance().getPhoneNumber();
		// mConnector.setParameter("I_MPHN_NO", "010-0123-4567");

		try {
			mConnector.executeRFCAsyncTask(strFuncName, login_table_names);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private String getAppVer(Context context) {
		String version = "0";
		try {
			PackageInfo i = context.getPackageManager().getPackageInfo(
					context.getPackageName(), 0);
			version = i.versionName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return version;
	}

	public String getAndroidID(Context _context) {
		try {
			String android_id = Secure.getString(_context.getContentResolver(),
					Secure.ANDROID_ID);
			return Build.SERIAL;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public String isPhoneRootingCheck() {
		RootingCheck rootingCheck = new RootingCheck();
		boolean isRooting = rootingCheck.isRootingCheck();

		return isRooting == false ? "N" : "Y";
	}

	// 공통 조회
	public void commonCheck() {

		String strFuncName = COMMONCHECK_FUNTION_NAME;

		LoginModel model = KtRentalApplication.getLoginModel();

		mConnector.setParameter("I_PERNR", model.getPernr());

		try {
			mConnector.executeRFCAsyncTask(strFuncName, "");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * 
	 * @param id
	 * @param syncData
	 */
	// public void getRepairPlan(String id, String syncData) {
	// String strFuncName = REPAIR_FUNTION_NAME;
	//
	// mConnector.setParameter("I_PERNR", id);
	// mConnector.setParameter("I_SYNC", syncData);
	// // mConnector.setParameter("I_SYNC", "A");
	//
	// HashMap<String, String> map = getCommonConnectData();
	// mConnector.setStructure("IS_LOGIN", map);
	//
	// try {
	// mConnector.executeRFCAsyncTask(strFuncName, login_table_names);
	// } catch (Exception e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	// }

	// 전체 받아오는 곳??
	public void getRepairPlan(String id, String syncData, Context context) {
		String strFuncName = REPAIR_FUNTION_NAME;
		//zmo_1020_RD02_2
		mConnector.setParameter("I_PERNR", id);

		// String i_sync = " ";
		// String dirPath = context.getExternalCacheDir().getPath() + "/" +
		// "DOWNLOAD" + "/" + DEFINE.SQLLITE_DB_NAME;
		// File file = new File(dirPath);
		// if(!file.exists())
		// {
		// i_sync = "A";
		// }
		// else{
		// SQLiteDatabase sqlite = SQLiteDatabase.openDatabase(dirPath, null,
		// SQLiteDatabase.OPEN_READWRITE);
		// Cursor cursor = sqlite.rawQuery("SELECT * FROM " + "REPAIR_TABLE",
		// null);
		// if(cursor.getCount()<=0)
		// {
		// i_sync = "A";
		// }
		// }
		//
		// Calendar cal = Calendar.getInstance();
		// int year = cal.get(Calendar.YEAR);
		// int month = cal.get(Calendar.MONTH)+1;
		// int day = cal.get(Calendar.DAY_OF_MONTH);
		// String key = ""+year+month+day;
		//
		// if(!loadPreferences(context, key))
		// {
		// i_sync = "A";
		// savePreferences(context, key);
		// }
		//
		// Log.i("####", "####모드를 변경"+i_sync);
		// mConnector.setParameter("I_SYNC", i_sync);
		kog.e("Jonathan", "syncData :: " + syncData);

		mConnector.setParameter("I_SYNC", syncData);
		HashMap<String, String> map = getCommonConnectData();
		mConnector.setStructure("IS_LOGIN", map);

		try {
			mConnector.executeRFCAsyncTask(strFuncName, login_table_names);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	String MY_PREFS = "KTREN.xml";

	protected void savePreferences(Context context, String key) {
		SharedPreferences sp = context.getSharedPreferences(MY_PREFS,
				Activity.MODE_PRIVATE);
		SharedPreferences.Editor editor = sp.edit();
		editor.putBoolean(key, true);
		editor.commit();
	}

	public boolean loadPreferences(Context context, String key) {
		SharedPreferences sp = context.getSharedPreferences(MY_PREFS,
				Activity.MODE_PRIVATE);
		return sp.getBoolean(key, false);
	}

	/**
	 * 지번 검색
	 * 
	 * @param cityDo
	 *            시도
	 * @param gu
	 *            시구군
	 * @param dong
	 *            읍면동명(건물명)
	 */
	public void getLotNumAddress(String cityDo, String gu, String dong) {
		String strFuncName = ADDRESS_LOTNUMBER_FUNTION_NAME;

		LoginModel model = KtRentalApplication.getLoginModel();
		mConnector.setParameter("I_PERNR", model.getPernr());
		mConnector.setParameter("I_DO_NM", cityDo);
		mConnector.setParameter("I_CT_NM", gu);
		mConnector.setParameter("I_DNG_NM", dong);

		HashMap<String, String> map = getCommonConnectData();
		mConnector.setStructure("IS_LOGIN", map);

		try {
			mConnector.executeRFCAsyncTask(strFuncName,
					ADDRESS_LOTNUMBER_TABLE_NAME);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 도로명 주소 검색
	 * 
	 * @param cityDo
	 *            시도명
	 * @param gu
	 *            시구군명
	 * 
	 * @param streetName
	 *            도로명/건물명
	 * @param buildMainNum
	 *            건물주번호
	 * @param buildSubNum
	 *            건물번호
	 */
	public void getStreetAddress(String cityDo, String gu, String streetName,
			String buildMainNum, String buildSubNum) {
		String strFuncName = ADDRESS_STREET_FUNTION_NAME;

		LoginModel model = KtRentalApplication.getLoginModel();

		mConnector.setParameter("I_PERNR", model.getPernr());
		mConnector.setParameter("I_DO_NM", cityDo);
		mConnector.setParameter("I_CT_NM", gu);
		mConnector.setParameter("I_RD_NM", streetName);
		mConnector.setParameter("I_BLD_MB", buildMainNum);
		mConnector.setParameter("I_BLD_SB", buildSubNum);

		HashMap<String, String> map = getCommonConnectData();
		mConnector.setStructure("IS_LOGIN", map);

		try {
			mConnector.executeRFCAsyncTask(strFuncName,
					ADDRESS_STREET_TABLE_NAME);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 주소 정제
	 * 
	 * @param allAddress
	 */
	public void getAllAddress(String allAddress) {
		String strFuncName = ADDRESS_ALL_FUNTION_NAME;

		LoginModel model = KtRentalApplication.getLoginModel();

		mConnector.setParameter("I_PERNR", model.getPernr());
		mConnector.setParameter("I_ADDRESS", allAddress);

		HashMap<String, String> map = getCommonConnectData();
		mConnector.setStructure("IS_LOGIN", map);

		try {
			mConnector.executeRFCAsyncTask(strFuncName, ADDRESS_ALL_TABLE_NAME);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 이관등록 부품목록
	private final String PARTS_TRANSFER_PARTS_SEARCH = "ZMO_1030_RD01";
	private final String PARTS_TRANSFER_SEARCH_TABLE_NAME = "parts_transfer_search_table";

	public void getPartsSearch(String pm023, String parts) {
		LoginModel model = KtRentalApplication.getLoginModel();
		HashMap<String, String> map = getCommonConnectData();

		mConnector.setParameter("I_PERNR", model.getPernr());
		mConnector.setParameter("I_INVNR", model.getInvnr());
		mConnector.setParameter("I_MATKL", pm023);
		mConnector.setParameter("I_MAKTX", parts);
		mConnector.setStructure("IS_LOGIN", map);

		try {
			mConnector.executeRFCAsyncTask(PARTS_TRANSFER_PARTS_SEARCH,
					PARTS_TRANSFER_SEARCH_TABLE_NAME);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 이관등록 이관완료
	private final String ZMO_1030_WR05 = "ZMO_1030_WR05";
	private final String ZMO_1030_WR05_TABLE = "zmo_1030_wr05_table";

	public void writeZMO_1030_WR05(ArrayList<HashMap<String, String>> _i_itab1) {
		LoginModel model = KtRentalApplication.getLoginModel();

		mConnector.setParameter("I_PERNR", model.getPernr());
		SimpleDateFormat sd = new SimpleDateFormat("yyyyMMdd");
		String date = sd.format(new Date());
		mConnector.setParameter("I_PSTNG_DATE", date);
		mConnector.setParameter("I_DOC_DATE", date);

		mConnector.setTable("I_ITAB1", _i_itab1);

		HashMap<String, String> map = getCommonConnectData();
		mConnector.setStructure("IS_LOGIN", map);
		try {
			mConnector.executeRFCAsyncTask(ZMO_1030_WR05, ZMO_1030_WR05_TABLE);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private final String ZMO_1030_RD06 = "ZMO_1030_RD06";
	private final String ZMO_1030_RD06_TABLE = "zmo_1030_rd06_table";

	public void getZMO_1030_RD06(String pm023, String parts_group,
			String parts_num)// 부품코드명 검색
	{
		LoginModel model = KtRentalApplication.getLoginModel();
		HashMap<String, String> map = getCommonConnectData();

		mConnector.setParameter("I_PERNR", model.getPernr());
		mConnector.setParameter("I_MATKL", pm023);
		mConnector.setParameter("I_MAKTX", parts_group);
		mConnector.setParameter("I_MATNR", parts_num);
		mConnector.setStructure("IS_LOGIN", map);

		try {
			mConnector.executeRFCAsyncTask(ZMO_1030_RD06, ZMO_1030_RD06_TABLE);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private final String ZMO_1060_RD01 = "ZMO_1060_RD01";
	private final String ZMO_1060_RD01_TABLE = "zmo_1060_rd01_table";

	public void getZMO_1060_RD01(String _carnum, String _mot,
			String _drivername, String _drivernum)// 부품코드명 검색
	{
		LoginModel model = KtRentalApplication.getLoginModel();
		HashMap<String, String> map = getCommonConnectData();
		mConnector.setStructure("IS_LOGIN", map);

		mConnector.setParameter("I_PERNR", model.getPernr());
		mConnector.setParameter("I_INVNR", _carnum);
		mConnector.setParameter("I_INGRP", _mot); // MOT
		mConnector.setParameter("I_SAWNM", _drivername);
		mConnector.setParameter("I_HPPHON", _drivernum);

		
		
		try {
			mConnector.executeRFCAsyncTask(ZMO_1060_RD01, ZMO_1060_RD01_TABLE);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public void getZMO_1060_RD01_CarManager(String _carnum, String _mot,
			String _drivername, String _drivernum)// 부품코드명 검색
	{
		LoginModel model = KtRentalApplication.getLoginModel();
		HashMap<String, String> map = getCommonConnectData();
		mConnector.setStructure("IS_LOGIN", map);

		mConnector.setParameter("I_PERNR", model.getPernr());
		mConnector.setParameter("I_INVNR", _carnum);
		mConnector.setParameter("I_INGRP", _mot); // MOT
		mConnector.setParameter("I_SAWNM", _drivername);
		mConnector.setParameter("I_HPPHON", _drivernum);
		mConnector.setParameter("I_CAR", "X");

		kog.e("Jonathan", "카매니저 :: "+ mConnector);
		kog.e("Jonathan", "Connector mot :: " + _mot);
		
		try {
			mConnector.executeRFCAsyncTask(ZMO_1060_RD01, ZMO_1060_RD01_TABLE);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private final String ZMO_1020_RD07 = "ZMO_1020_RD07";
	private final String ZMO_1020_RD07_TABLE = "zmo_1020_rd07_table";
	public void getZMO_1020_RD07(String i_vbeln)// 고장코드이력 검색
	{
		HashMap<String, String> map = getCommonConnectData();
		mConnector.setStructure("IS_LOGIN", map);

		mConnector.setParameter("I_VBELN", i_vbeln);

		try {
			mConnector.executeRFCAsyncTask(ZMO_1020_RD07, ZMO_1020_RD07_TABLE);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private final String ZMO_1020_RD06 = "ZMO_1020_RD06";
	private final String ZMO_1020_RD06_TABLE = "zmo_1020_rd06_table";
	public void getZMO_1020_RD06(String reqNo)// IoT 요청사항 검색
	{
		HashMap<String, String> map = getCommonConnectData();
		mConnector.setStructure("IS_LOGIN", map);

		mConnector.setParameter("I_REQNO", reqNo);

		try {
			mConnector.executeRFCAsyncTask(ZMO_1020_RD06, ZMO_1020_RD06_TABLE);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private final String ZMO_1020_WR01 = "ZMO_1020_WR01";
	private final String ZMO_1020_WR01_TABLE = "zmo_1020_wr01_table";
	public void getZMO_1020_WR01(String reqNo, String cnslcd, String cnslmemo)// IoT 요청사항 검색
	{
		HashMap<String, String> map = getCommonConnectData();
		mConnector.setStructure("IS_LOGIN", map);

		mConnector.setParameter("I_REQNO", reqNo);
		mConnector.setParameter("I_CNSLCD", cnslcd);
		mConnector.setParameter("I_CNSLMEMO", cnslmemo);

		try {
			mConnector.executeRFCAsyncTask(ZMO_1020_WR01, ZMO_1020_WR01_TABLE);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	

	private final String ZMO_1060_RD03 = "ZMO_1060_RD03";
	private final String[] ZMO_1060_RD03_TABLE = { "zmo_1060_rd03_table", "" };

	public void getZMO_1060_RD03(String _invnr, String _equnr) {
		LoginModel model = KtRentalApplication.getLoginModel();
		HashMap<String, String> map = getCommonConnectData();
		mConnector.setStructure("IS_LOGIN", map);

		mConnector.setParameter("I_PERNR", model.getPernr());
		mConnector.setParameter("I_INVNR", _invnr);
		// myung 20131202 ADD 차량정보 조회 펑션(ZMO_1060_RD03) 호출 시 I_EQUNR 값 추가
		mConnector.setParameter("I_EQUNR", _equnr);
		mConnector.setParameter("I_SAGO", "T");

		ArrayList<String> arr = new ArrayList<String>();
		arr.add("E_VOCNUM");
		
		try {
			mConnector.executeRFCAsyncTask(ZMO_1060_RD03, ZMO_1060_RD03_TABLE, arr);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private final String ZMO_1060_RD04 = "ZMO_1060_RD04";
	private final String ZMO_1060_RD04_TABLE = "zmo_1060_rd04_table";

	public void getZMO_1060_RD04(String _carnum, String _num) {
		LoginModel model = KtRentalApplication.getLoginModel();
		HashMap<String, String> map = getCommonConnectData();
		mConnector.setStructure("IS_LOGIN", map);

		mConnector.setParameter("I_PERNR", model.getPernr());
		mConnector.setParameter("I_INVNR", _carnum);
		mConnector.setParameter("I_KUNNR", _num);

		try {
			mConnector.executeRFCAsyncTask(ZMO_1060_RD04, ZMO_1060_RD04_TABLE);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// myung 20131122 ADD 공지사항 리스트 조회 (기준 인덱스, 요청 카운트)
	private final String ZMO_1060_RD06 = "ZMO_1060_RD06";
	private final String ZMO_1060_RD06_TABLE = "zmo_1060_rd06_table";

	public void getZMO_1060_RD06(String i_first, String i_count, String i_an_ty, String _from, String _to) {
		LoginModel model = KtRentalApplication.getLoginModel();
		HashMap<String, String> map = getCommonConnectData();
		mConnector.setStructure("IS_LOGIN", map);

		mConnector.setParameter("I_PERNR", model.getPernr()); // 사용자번호
		mConnector.setParameter("I_SYS_CD", "IN3000"); // I_SYS_CD
		mConnector.setParameter("I_FIRST", i_first); // 기준 인덱스
		mConnector.setParameter("I_COUNT", i_count); // 요청 카운트
		

		String date1 = _from.replace(".", "");
		String date2 = _to.replace(".", "");
		mConnector.setParameter("I_RG_BGDT", date1); // From
		mConnector.setParameter("I_RG_ENDT", date2); // To
		mConnector.setParameter("I_AN_TY", i_an_ty); // 공지사항 구분 (카매니저 순회정비)
		

		try {
			mConnector.executeRFCAsyncTask(ZMO_1060_RD06, ZMO_1060_RD06_TABLE);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// myung 20131122 ADD 공지사항 리스트 조회 (공지주제)
	public void getZMO_1060_RD06(String NoticeTitle, String i_an_ty, String _from, String _to) {
		LoginModel model = KtRentalApplication.getLoginModel();
		HashMap<String, String> map = getCommonConnectData();
		mConnector.setStructure("IS_LOGIN", map);

		mConnector.setParameter("I_PERNR", model.getPernr()); // 사용자번호
		mConnector.setParameter("I_SYS_CD", "IN3000"); // I_SYS_CD
		mConnector.setParameter("I_AN_SBM", NoticeTitle); // 공지주제
		
		String date1 = _from.replace(".", "");
		String date2 = _to.replace(".", "");
		mConnector.setParameter("I_RG_BGDT", date1); // From
		mConnector.setParameter("I_RG_ENDT", date2); // To
		mConnector.setParameter("I_AN_TY", i_an_ty); // 공지사항 구분 (카매니저 순회정비)
		

		try {
			mConnector.executeRFCAsyncTask(ZMO_1060_RD06, ZMO_1060_RD06_TABLE);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// myung 20131122 ADD 공지사항 리스트 상세조회 (공지번호)
	private final String ZMO_1060_RD07 = "ZMO_1060_RD07";
	private final String ZMO_1060_RD07_TABLE = "zmo_1060_rd07_table";

	public void getZMO_1060_RD07(String i_an_no) {
		LoginModel model = KtRentalApplication.getLoginModel();
		HashMap<String, String> map = getCommonConnectData();
		mConnector.setStructure("IS_LOGIN", map);

		mConnector.setParameter("I_PERNR", model.getPernr()); // 사용자번호
		mConnector.setParameter("I_SYS_CD", "IN3000"); // I_SYS_CD
		mConnector.setParameter("I_AN_NO", i_an_no); // 공지번호

		try {
			mConnector.executeRFCAsyncTask(ZMO_1060_RD07, ZMO_1060_RD07_TABLE);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// myung 20131122 ADD 관련자 연락처
	private final String ZMO_1060_RD08 = "ZMO_1060_RD08";
	private final String ZMO_1060_RD08_TABLE = "zmo_1060_rd08_table";

	public void getZMO_1060_RD08(String EQUNR, String JDGBN) {
		LoginModel model = KtRentalApplication.getLoginModel();
		HashMap<String, String> map = getCommonConnectData();
		mConnector.setStructure("IS_LOGIN", map);

		mConnector.setParameter("I_PERNR", model.getPernr()); // 사용자번호
		mConnector.setParameter("I_EQUNR", EQUNR); // 설비번호
		mConnector.setParameter("I_GUBUN", JDGBN); // 장단기구분

		// 2013.12.07 ypkim
		ArrayList<String> arr = new ArrayList<String>();
		arr.add("O_ENAME_1");
		arr.add("O_CELLNO_1");
		arr.add("O_DEPTNM_1");
		arr.add("O_TELNR_1");
		arr.add("O_ENAME_2");
		arr.add("O_CELLNO_2");
		arr.add("O_DEPTNM_2");
		arr.add("O_TELNR_2");

		try {
			mConnector.executeRFCAsyncTask(ZMO_1060_RD08, ZMO_1060_RD08_TABLE,
					arr);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}




	private final String ZMO_1060_WR02 = "ZMO_1060_WR02";
	private final String ZMO_1060_WR02_TABLE = "ZMO_1060_WR02_TABLE";

	public void getZMO_1060_WR02(String ENAME2, String CELLNO2, String VBELN, String EQUNR, String INVNR) {
		LoginModel model = KtRentalApplication.getLoginModel();
		HashMap<String, String> map = getCommonConnectData();
		mConnector.setStructure("IS_LOGIN", map);

		mConnector.setParameter("I_ENAME_2", ENAME2); // 영업담당자명
		mConnector.setParameter("I_CELLNO_2", CELLNO2); // 영업담당자번호
		mConnector.setParameter("I_VBELN", VBELN); // 계약번호
		mConnector.setParameter("I_EQUNR", EQUNR); // 설비번호
		mConnector.setParameter("I_INVNR", INVNR); // 재고번호

		try {
			mConnector.executeRFCAsyncTask(ZMO_1060_WR02, ZMO_1060_WR02_TABLE);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}




	// myung 20131204 ADD 완료시 정비결과 수정 버튼 삭제 -> 정비 취소 버튼 추가.
	private final String ZMO_1050_WR02 = "ZMO_1050_WR02";
	private final String ZMO_1050_WR02_TABLE = "zmo_1050_wr02_table";

	public void setZMO_1050_WR02(String aufnr, String MTINVNR, String cemer) {
		LoginModel model = KtRentalApplication.getLoginModel();
		HashMap<String, String> map = getCommonConnectData();
		mConnector.setStructure("IS_LOGIN", map);

		mConnector.setParameter("I_PERNR", model.getPernr()); // 사용자번호
		mConnector.setParameter("I_AUFNR", aufnr); // 오더 번호
		mConnector.setParameter("I_MTINVNR", MTINVNR); // 순회차량
		mConnector.setParameter("I_CEMER", cemer); // 긴급정비(긴급정비 : X , 순회정비 " ")

		try {
			mConnector.executeRFCAsyncTask(ZMO_1050_WR02, ZMO_1050_WR02_TABLE);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private final String ZMO_3020_RD02 = "ZMO_3020_RD02";
	private final String ZMO_3020_RD02_TABLE = "zmo_3020_rd02_table";

	public void getZMO_3020_RD02(String str) {
		LoginModel model = KtRentalApplication.getLoginModel();
		HashMap<String, String> map = getCommonConnectData();
		mConnector.setStructure("IS_LOGIN", map);

		mConnector.setParameter("I_PERNR", model.getPernr());
		mConnector.setParameter("I_VKORG", model.getVkorg()); // 영업조직
		mConnector.setParameter("I_NAME1", str); // 고객명
		try {
			mConnector.executeRFCAsyncTask(ZMO_3020_RD02, ZMO_3020_RD02_TABLE);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private final String ZMO_1060_RD02 = "ZMO_1060_RD02";
	private final String ZMO_1060_RD02_TABLE = "zmo_1060_rd02_table";

	//150714 공급업체 인수자
	public void getZMO_1060_RD02(String _str, String type, String lifnr) {
		LoginModel model = KtRentalApplication.getLoginModel();
		HashMap<String, String> map = getCommonConnectData();
		mConnector.setStructure("IS_LOGIN", map);
		mConnector.setParameter("I_PERNR", model.getPernr());
		mConnector.setParameter("I_NAME1", _str);
		//2014-04-22 KDH  G5에 타이어업체만 검색되도록 변경함 나는 하드코딩함.
		//2014-05-09 KDH 직접인 경우 업체검색을 추가해준다. TAG로구분한다 1 : 직접, 2 : 배송 
		kog.e("KDH", "type = "+type);
//		if("1".equals(type))
//		{
			mConnector.setParameter("I_ANRED", "타이어");
//		}
		
		
		mConnector.setParameter("I_LIFNR", lifnr);
		mConnector.setParameter("I_SHIPGB", type);
		

		try {
			mConnector.executeRFCAsyncTask(ZMO_1060_RD02, ZMO_1060_RD02_TABLE);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private final String ZMO_1030_RD05 = "ZMO_1030_RD05";
	private final String ZMO_1030_RD05_TABLE = "zmo_1030_rd05_table";

	public void getZMO_1030_RD05(String group, String size, String infnr, boolean isSnow, String I_LIFNR, String tire_code) {
		LoginModel model = KtRentalApplication.getLoginModel();
		HashMap<String, String> map = getCommonConnectData();
		mConnector.setStructure("IS_LOGIN", map);

		mConnector.setParameter("I_PERNR", model.getPernr());

		mConnector.setParameter("I_WERKS", model.getWerks());
		mConnector.setParameter("I_MATKL", group);
		
		if(isSnow)
		{
			mConnector.setParameter("I_TIRGCD", "A");// 타이어구분코드
		}
		else
		{
			mConnector.setParameter("I_TIRGCD", "1");// 타이어구분코드
		}
		mConnector.setParameter("I_TIRMCD", "A");// 타이어제조사코드
		
		
		mConnector.setParameter("I_MATNR", " ");// 자재 번호
		mConnector.setParameter("I_MAKTX", size);// 자재내역
		mConnector.setParameter("I_USEYN", "Y");// 사용여부
		mConnector.setParameter("I_LIFNR", infnr);
		
		//Jonathan 14.10.14 I_GUBUN 추가. 
		kog.e("Jonathan", "Connector  tire_code :: " + tire_code );
		mConnector.setParameter("I_GUBUN", tire_code); //배송구분
		

		// mConnector.setParameter("I_INFNR", infnr);// INFNR 파라미터 오류 13/09/13
		// 11:25

		try {
			mConnector.executeRFCAsyncTask(ZMO_1030_RD05, ZMO_1030_RD05_TABLE);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private final String ZMO_1030_WR06 = "ZMO_1030_WR06";
	private final String ZMO_1030_WR06_TABLE = "zmo_1030_wr06_table";
	
//사진 보내기.
	public void setZMO_1030_WR06(HashMap<String, String> hm,
			ArrayList<HashMap<String, String>> hm2_arr,
			ArrayList<HashMap<String, String>> hm3_arr, String i_trcusr,
			String i_trspec) {
		LoginModel model = KtRentalApplication.getLoginModel();
		HashMap<String, String> map = getCommonConnectData();
		mConnector.setStructure("IS_LOGIN", map);

		mConnector.setParameter("I_PERNR", model.getPernr());
		mConnector.setStructure("I_STRUCT1", hm);

		mConnector.setTable("I_ITAB1", hm2_arr);// 사진보내기
		mConnector.setTable("I_ITAB2", hm3_arr);

		mConnector.setParameter("I_TRCUSR", i_trcusr);
		mConnector.setParameter("I_TRSPEC", i_trspec);

		try {
			mConnector.executeRFCAsyncTask(ZMO_1030_WR06, ZMO_1030_WR06_TABLE);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private final String ZMO_1040_WR02 = "ZMO_1040_WR02";
	private final String ZMO_1040_WR02_TABLE = "zmo_1040_wr02_table";

	//myung 20131211 UPDATE 첫번째 param "120"을 로그인 시 받아온 INGRP정보로 세팅
//	public void setZMO_1040_WR02(String _mot, ArrayList<HashMap<String, String>> table) {
	public void setZMO_1040_WR02(ArrayList<HashMap<String, String>> table) {
		LoginModel model = KtRentalApplication.getLoginModel();
		HashMap<String, String> map = getCommonConnectData();
		mConnector.setStructure("IS_LOGIN", map);

		mConnector.setParameter("I_PERNR", model.getPernr());
		//myung 20131211 UPDATE 첫번째 param "120"을 로그인 시 받아온 INGRP정보로 세팅
//		mConnector.setParameter("I_MOT", _mot);
		mConnector.setParameter("I_MOT", model.getINGRP());
		if(table != null && table.size() > 0) {
			String POSINGRP = table.get(0).get("POSINGRP");
			if(POSINGRP == null || POSINGRP.equals("")){
				table.get(0).put("POSINGRP", model.getINGRP());
			}
		}

		mConnector.setTable("I_ITAB1", table);
		 
		
		kog.e("Jonathan", "model.getINGRP() ::: " + model.getINGRP());
		kog.e("Jonathan", "model.getPernr() ::: " + model.getPernr());
		
		
		for(int i = 0 ; i < table.size() ; i++)
		{
			Set <String> set  = table.get(i).keySet();
			Iterator <String> it = set.iterator();
			String key;
			
			while(it.hasNext())
			{
				key = it.next();
				kog.e("Jonathan", "table.get()  key :::" + key + "    value  ::: " + table.get(i).get(key));
			}
		}
		
		
		try {
 			mConnector.executeRFCAsyncTask(ZMO_1040_WR02, ZMO_1040_WR02_TABLE);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private final String ZMO_1040_RD01 = "ZMO_1040_RD01";
	private final String ZMO_1040_RD01_TABLE = "zmo_1040_rd01_table";

	public void getZMO_1040_RD01(String _zip_code) {
		LoginModel model = KtRentalApplication.getLoginModel();
		HashMap<String, String> map = getCommonConnectData();
		mConnector.setStructure("IS_LOGIN", map);
		mConnector.setParameter("I_PERNR", model.getPernr());
		mConnector.setParameter("I_ZIP_CODE", _zip_code);

		try {
			mConnector.executeRFCAsyncTask(ZMO_1040_RD01, ZMO_1040_RD01_TABLE);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// public void sendMaintenance(MaintenanceSendModel sendModel) {
	// LoginModel model = KtRentalApplication.getLoginModel();
	// HashMap<String, String> map = getCommonConnectData();
	// mConnector.setStructure("IS_LOGIN", map);
	// mConnector.setParameter("I_PERNR", model.getPernr());
	// mConnector.setParameter("I_GUBUN", sendModel
	// .getMaintenanceSendBaseModel().getGUBUN());
	// ArrayList<HashMap<String, String>> baseArr = new
	// ArrayList<HashMap<String, String>>();
	// baseArr.add(sendModel.getMaintenanceSendBaseModel().getmHashMap());
	// mConnector.setTable("I_ITAB1", baseArr);
	//
	// mConnector.setTable("I_ITAB2", sendModel.getStockSendArray());
	// mConnector.setTable("I_ITAB3", sendModel.getSignSendArray());
	//
	// try {
	// mConnector.executeRFCAsyncTask(SEND_FUNTION_NAME,
	// SEND_FUNTION_NAME_TABLE);
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// }

	public void sendMaintenance(ArrayList<HashMap<String, String>> baseArr,
			ArrayList<HashMap<String, String>> stockArr,
			ArrayList<HashMap<String, String>> signArr, String GUBUN,
			String aufnr) {
		LoginModel model = KtRentalApplication.getLoginModel();
		HashMap<String, String> map = getCommonConnectData();
		mConnector.setStructure("IS_LOGIN", map);
		mConnector.setParameter("I_PERNR", model.getPernr());
		//myung 20131211 DELETE 요청에 의해 
//		mConnector.setParameter("I_GUBUN", GUBUN);
		mConnector.setParameter("I_CHECK", "X");
		mConnector.setTable("I_ITAB1", baseArr);

		mConnector.setTable("I_ITAB2", stockArr);
		
//		mConnector.setTable("I_ITAB3", signArr);

		try {
			mConnector.executeRFCAsyncTask(SEND_FUNTION_NAME,
					SEND_FUNTION_NAME_TABLE, aufnr);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private HashMap<String, String> getCommonConnectData() {
		HashMap<String, String> reCommonMap = null;
		LoginModel model = KtRentalApplication.getLoginModel();
		if (model != null && model.getModelMap() != null) {
			reCommonMap = model.getModelMap();
		}
		return reCommonMap;
	}

	// 데이터베이스 다운로드.
	public void downloadDB(String version, String directory) {
		mConnector.downloadDB(version, directory);
	}

	private final String ZMO_1020_RD03 = "ZMO_1020_RD03";

	public void getZMO_1020_RD03(String _equnr, String _carnum) {
		LoginModel model = KtRentalApplication.getLoginModel();
		HashMap<String, String> map = getCommonConnectData();
		mConnector.setStructure("IS_LOGIN", map);
		mConnector.setParameter("I_PERNR", model.getPernr());
		mConnector.setParameter("I_EQUNR", _equnr);
		mConnector.setParameter("I_INVNR", _carnum);
		mConnector.setParameter("I_MNTTYP", "A");
		mConnector.setParameter("I_DAY", "");

		ArrayList<String> arr = new ArrayList<String>();
		arr.add("O_BRKCNT");
		arr.add("O_GENCNT");
		arr.add("O_CIRCNT");
		arr.add("O_EMRCNT");
		arr.add("O_CHTIRE");

		try {
			mConnector.executeRFCAsyncTask(ZMO_1020_RD03, ZMO_1020_RD03
					+ "_TABLE", arr);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private final String ZMO_1070_RD01 = "ZMO_1070_RD01";
	private final String ZMO_1070_RD01_TABLE = "zmo_1070_rd01_table";

	public void getZMO_1070_RD01(String _invnr) {
		LoginModel model = KtRentalApplication.getLoginModel();
		HashMap<String, String> map = getCommonConnectData();
		mConnector.setStructure("IS_LOGIN", map);
		mConnector.setParameter("I_PERNR", model.getPernr());
		mConnector.setParameter("I_INVNR", _invnr);

		try {
			mConnector.executeRFCAsyncTask(ZMO_1070_RD01, ZMO_1070_RD01_TABLE);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	//20170131 Jonathan 순회정비차량점검표 리스트 
	public final static String ZMO_1070_RD03 = "ZMO_1070_RD03";
	public final static String ZMO_1070_RD03_TABLE = "ZMO_1070_RD03_TABLE";
	
	public void getZMO_1070_RD03(String _mtinvnr, String _kunnr, String _frdate, String _todate, String _invnr) {
		LoginModel model = KtRentalApplication.getLoginModel();
		HashMap<String, String> map = getCommonConnectData();
		mConnector.setStructure("IS_LOGIN", map);
		mConnector.setParameter("I_MTINVNR", _mtinvnr);
		mConnector.setParameter("I_KUNNR", _kunnr);
		
		String date1 = _frdate.replace(".", "");
		String date2 = _todate.replace(".", "");
		mConnector.setParameter("I_FRDATE", date1);
		mConnector.setParameter("I_TODATE", date2);
		mConnector.setParameter("I_INVNR", _invnr);

		try {
			mConnector.executeRFCAsyncTask(ZMO_1070_RD03, ZMO_1070_RD03_TABLE);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	
	//20170131 Jonathan 순회정비차량점검표 리스트 
	public final static String ZMO_1070_RD04 = "ZMO_1070_RD04";
	public final static String ZMO_1070_RD04_TABLE = "ZMO_1070_RD04_TABLE";
	
	public void getZMO_1070_RD04(String _aufnr) {
		LoginModel model = KtRentalApplication.getLoginModel();
		HashMap<String, String> map = getCommonConnectData();
		mConnector.setStructure("IS_LOGIN", map);
		mConnector.setParameter("I_AUFNR", _aufnr);
		mConnector.setParameter("I_URL", "");
		

		try {
			mConnector.executeRFCAsyncTask(ZMO_1070_RD04, ZMO_1070_RD04_TABLE);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	//20190402 yunseung 순회정비 명세표?? For IoT
	public final static String ZMO_1070_RD11 = "ZMO_1070_RD11";
	public final static String ZMO_1070_RD11_TABLE = "ZMO_1070_RD11_TABLE";

	public void getZMO_1070_RD11(String reqNo) {
		LoginModel model = KtRentalApplication.getLoginModel();
		HashMap<String, String> map = getCommonConnectData();
		mConnector.setStructure("IS_LOGIN", map);
		mConnector.setParameter("I_REQNO", reqNo);
		mConnector.setParameter("I_BUKRS", "3000");


		try {
			mConnector.executeRFCAsyncTask(ZMO_1070_RD04, ZMO_1070_RD04_TABLE);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	
	

	private final String ZMO_1040_RD02 = "ZMO_1040_RD02";
	private final String ZMO_1040_RD02_TABLE = "zmo_1020_rd03_table";

	public void getZMO_1040_RD02(String _equnr, String _carnum) {
		LoginModel model = KtRentalApplication.getLoginModel();
		HashMap<String, String> map = getCommonConnectData();
		mConnector.setStructure("IS_LOGIN", map);
		mConnector.setParameter("I_PERNR", model.getPernr());
		mConnector.setParameter("I_EQUNR", _equnr);
		mConnector.setParameter("I_INVNR", _carnum);

		try {
			mConnector.executeRFCAsyncTask(ZMO_1040_RD02, ZMO_1040_RD02_TABLE);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private final String ZMO_1080_WR01 = "ZMO_1080_WR01";
	private final String ZMO_1080_WR01_TABLE = "zmo_1080_wr01_table";

	public void setZMO_1080_WR01(String code1, String code2, String detail,
			ArrayList<HashMap<String, String>> table) {
		LoginModel model = KtRentalApplication.getLoginModel();
		HashMap<String, String> map = getCommonConnectData();
		mConnector.setStructure("IS_LOGIN", map);
		mConnector.setParameter("I_PERNR", model.getPernr());
		mConnector.setParameter("I_DETCD", code1);
		mConnector.setParameter("I_GRUND", code2);
		mConnector.setParameter("I_LTXA1", detail);

		mConnector.setTable("I_ITAB1", table);

		try {
			mConnector.executeRFCAsyncTask(ZMO_1080_WR01, ZMO_1080_WR01_TABLE);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private final String ZMO_1050_WR04 = "ZMO_1050_WR04";
	private final String ZMO_1050_WR04_TABLE = "zmo_1050_wr04_table";

	public void setZMO_1050_WR04(String reqNo, String sELECTED_DAY, String chtime, String chprerq, String ATVYN, ArrayList<HashMap<String, String>> table) {
		LoginModel model = KtRentalApplication.getLoginModel();
		HashMap<String, String> map = getCommonConnectData();
		mConnector.setStructure("IS_LOGIN", map);
		mConnector.setParameter("I_REQNO", reqNo);
		mConnector.setParameter("I_PERNR", model.getPernr());
		mConnector.setParameter("I_CHDATE", sELECTED_DAY);
		mConnector.setParameter("I_CHTIME", chtime + "00");
		mConnector.setParameter("I_CHPRERQ", chprerq);
		mConnector.setParameter("I_ATVYN", ATVYN);

		mConnector.setTable("I_ITAB1", table);

		try {
			mConnector.executeRFCAsyncTask(ZMO_1050_WR04, ZMO_1050_WR04_TABLE);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

//	private final String ZMP_0024_005 = "ZMP_0024_005";
//	private final String ZMP_0024_005_TABLE = "zmp_0024_005_table";
//
//	public void setZMP_0024_005(String sELECTED_DAY, String chprerq, String chtime, String reqNo, String atvyn) {
//		LoginModel model = KtRentalApplication.getLoginModel();
//		HashMap<String, String> map = getCommonConnectData();
//		mConnector.setStructure("IS_LOGIN", map);
//		mConnector.setParameter("I_REQNO", reqNo);
//		mConnector.setParameter("I_PERNR", model.getPernr());
//		mConnector.setParameter("I_CHDATE", sELECTED_DAY);
//		mConnector.setParameter("I_CHTIME", chtime);
//		mConnector.setParameter("I_CHPRERQ", chprerq);
//		mConnector.setParameter("I_ATVYN", atvyn);
//
//		try {
//			mConnector.executeRFCAsyncTask(ZMP_0024_005, ZMP_0024_005_TABLE);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//
//	}

	private final String ZMO_1060_WR01 = "ZMO_1060_WR01";
	private final String ZMO_1060_WR01_TABLE = "zmo_1060_wr01_table";

	public void setZMO_1060_WR01(
			ArrayList<HashMap<String, String>> tableZMO_1060_WR01,
			String trcusr, String trspec) {
		LoginModel model = KtRentalApplication.getLoginModel();
		HashMap<String, String> map = getCommonConnectData();
		mConnector.setStructure("IS_LOGIN", map);
		mConnector.setParameter("I_PERNR", model.getPernr());

		// mConnector.setParameter("I_TRCUSR", trcusr);
		// mConnector.setParameter("I_TRSPEC", trspec);

		mConnector.setTable("I_ITAB1", tableZMO_1060_WR01);
		
		

		try {
			ArrayList<String> arr = new ArrayList<String>();
			arr.add("O_ATTATCHNO");
			mConnector.executeRFCAsyncTask(ZMO_1060_WR01, ZMO_1060_WR01_TABLE,
					arr);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}


	public void getZMO_1020_RD05(String invnr) {
		HashMap<String, String> map = getCommonConnectData();
		LoginModel model = KtRentalApplication.getLoginModel();
		mConnector.setStructure("IS_LOGIN", map);
//		mConnector.setParameter("I_PERNR", model.getPernr());
		mConnector.setParameter("I_INVNR", invnr);

		try {
			mConnector.executeRFCAsyncTask(DEFINE.ZMO_1020_RD05, DEFINE.ZMO_1020_RD05);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private final String ZMO_1020_RD08 = "ZMO_1020_RD08";
	private final String ZMO_1020_RD08_TABLE = "zmo_1020_rd08_table";
	public void getZMO_1020_RD08(String invnr, String minvnr) {
		HashMap<String, String> map = getCommonConnectData();
		LoginModel model = KtRentalApplication.getLoginModel();
		mConnector.setStructure("IS_LOGIN", map);
		mConnector.setParameter("I_INVNR", invnr);
		mConnector.setParameter("I_MINVNR", minvnr);

		try {
			mConnector.executeRFCAsyncTask(ZMO_1020_RD08, ZMO_1020_RD08_TABLE);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	

	private final String ZMO_1090_WR01 = "ZMO_1090_WR01";
	private final String ZMO_1090_WR01_TABLE = "zmo_1090_wr01_table";

	public void getZMO_1090_WR01(ArrayList<HashMap<String, String>> table,
			String tp) {
		LoginModel model = KtRentalApplication.getLoginModel();
		HashMap<String, String> map = getCommonConnectData();
		mConnector.setStructure("IS_LOGIN", map);
		mConnector.setParameter("I_PERNR", model.getPernr());
		mConnector.setParameter("I_MINVNR", model.getInvnr());
		mConnector.setParameter("I_PROC_TP", tp);

		if (table == null)
			return;

		// for (int i = 0; i < table.size(); i++) {
		// Log.i("###", "#### 보내는 주소" + table.get(i).get("DELVR_ADDR"));
		// }

		mConnector.setTable("I_ITAB1", table);

		try {
			mConnector.executeRFCAsyncTask(ZMO_1090_WR01, ZMO_1090_WR01_TABLE);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private final String ZMO_1090_RD01 = "ZMO_1090_RD01";
	private final String ZMO_1090_RD01_TABLE = "zmo_1090_rd01_table";

	public void getZMO_1090_RD01() {
		LoginModel model = KtRentalApplication.getLoginModel();
		HashMap<String, String> map = getCommonConnectData();
		mConnector.setStructure("IS_LOGIN", map);

		mConnector.setParameter("I_PERNR", model.getPernr());
		mConnector.setParameter("I_MINVNR", model.getInvnr());

		try {
			mConnector.executeRFCAsyncTask(ZMO_1090_RD01, ZMO_1090_RD01_TABLE);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void sendTransfer(ArrayList<HashMap<String, String>> array) {
		LoginModel model = KtRentalApplication.getLoginModel();
		HashMap<String, String> map = getCommonConnectData();
		mConnector.setStructure("IS_LOGIN", map);
		mConnector.setParameter("I_PERNR", model.getPernr());

		mConnector.setTable("I_ITAB1", array);

		try {
			mConnector.executeRFCAsyncTask(TRANSFER_FUNTION_NAME,
					TRANSFER_FUNTION_TABLE);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void getTransfer(ArrayList<HashMap<String, String>> array) {
		LoginModel model = KtRentalApplication.getLoginModel();
		HashMap<String, String> map = getCommonConnectData();
		mConnector.setStructure("IS_LOGIN", map);
		mConnector.setParameter("I_PERNR", model.getPernr());

		mConnector.setTable("I_ITAB1", array);

		try {
			mConnector.executeRFCAsyncTask(TRANSFER_FUNTION_NAME,
					TRANSFER_FUNTION_TABLE);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void getResult(String aufnr, String cemer) {
		LoginModel model = KtRentalApplication.getLoginModel();
		HashMap<String, String> map = getCommonConnectData();
		mConnector.setStructure("IS_LOGIN", map);
		mConnector.setParameter("I_PERNR", model.getPernr());
		mConnector.setParameter("I_AUFNR", aufnr);
		mConnector.setParameter("I_CEMER", cemer);

		try {
			mConnector.executeRFCAsyncTask(GET_FUNTION_NAME,
					GET_FUNTION_NAME_TABLE);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void cancelResult(String aufnr, String cemer, String MTINVNR) {
		LoginModel model = KtRentalApplication.getLoginModel();
		HashMap<String, String> map = getCommonConnectData();
		mConnector.setStructure("IS_LOGIN", map);
		mConnector.setParameter("I_PERNR", model.getPernr());
		mConnector.setParameter("I_AUFNR", aufnr);
		mConnector.setParameter("I_CEMER", cemer);
		mConnector.setParameter("I_MTINVNR", MTINVNR);

		try {
			mConnector.executeRFCAsyncTask(CANCEL_FUNTION_NAME,
					CANCEL_FUNTION_NAME_TABLE);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void addressToWGS(String address) {
		LoginModel model = KtRentalApplication.getLoginModel();
		HashMap<String, String> map = getCommonConnectData();
		mConnector.setStructure("IS_LOGIN", map);
		mConnector.setParameter("I_PERNR", model.getPernr());
		mConnector.setParameter("I_ADDRESS", address);

		try {
			mConnector
					.executeRFCAsyncTask(ADDRESS_TO_WGS, ADDRESS_TO_WGS_TABLE);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void getPerformance(String yymm) {
		LoginModel model = KtRentalApplication.getLoginModel();
		HashMap<String, String> map = getCommonConnectData();
		mConnector.setStructure("IS_LOGIN", map);
		mConnector.setParameter("I_PERNR", model.getPernr());
		mConnector.setParameter("I_INGRP", model.getINGRP());
		mConnector.setParameter("I_MINVNR", model.getInvnr());
		mConnector.setParameter("I_JDANGBN", "A");
		mConnector.setParameter("I_BASYM", yymm);
		mConnector.setParameter("I_MNTTYP", "40");

		try {
			mConnector.executeRFCAsyncTask(PERFORMANCE_FUNTION_NAME,
					PERFORMANCE_FUNTION_TABLE);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	//Jonathan 14.07.31 카매니저일경우 차량운행일지 조회.
	public void getMovement_is_CarManger(String INVNR, String AUFNR, String DRIVER, String DATUMF, String DATUMT) {
		LoginModel model = KtRentalApplication.getLoginModel();
		HashMap<String, String> map = getCommonConnectData();
		mConnector.setStructure("IS_LOGIN", map);
		mConnector.setParameter("I_PERNR", model.getPernr());
		mConnector.setParameter("I_INVNR", INVNR);
		mConnector.setParameter("I_AUFNR", AUFNR);
		mConnector.setParameter("I_DRIVER", DRIVER);
		mConnector.setParameter("I_DATUMF", DATUMF);
		mConnector.setParameter("I_DATUMT", DATUMT);
		//Jonathan 14.08.04 추가
//		mConnector.setParameter("I_STRUCT1-OTYPE", "PZ05");
		
		kog.e("Jonathan", "ConnectController movement INVNR :: " + INVNR);

		try {
			mConnector.executeRFCAsyncTask(MOVEMENT_FUNTION_NAME,
					MOVEMENT_FUNTION_TABLE);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/** 법인카드 조회 */
	public void getCorCard(String status, String usetype, String page, String DATUMF, String DATUMT) {
		LoginModel model = KtRentalApplication.getLoginModel();
		HashMap<String, String> map = getCommonConnectData();
		mConnector.setStructure("IS_LOGIN", map);
		mConnector.setParameter("I_BUKRS", "3200");
		mConnector.setParameter("I_TRS_DT_FR", DATUMF);
		mConnector.setParameter("I_TRS_DT_TO", DATUMT);
		mConnector.setParameter("I_CARD_NO", "");
		mConnector.setParameter("I_STATUS", status);
		mConnector.setParameter("I_PERNR", model.getPernr());
		mConnector.setParameter("I_ORGEH", "");
		mConnector.setParameter("I_USETYPE", usetype);
		mConnector.setParameter("I_DOCTYPE", "");
		mConnector.setParameter("I_PAGE", page);
		mConnector.setParameter("I_ALL", "");
		mConnector.setParameter("I_FLAG", "");
		mConnector.setParameter("I_SPERN", "");

		kog.e("Jonathan", "datumf :: " + DATUMF + " datumt :: " + DATUMT);

		ArrayList<String> arr = new ArrayList<>();
		arr.add("E_PAGE");
		arr.add("E_COUNT");
		arr.add("E_TOTALCNT");
		arr.add("E_SUM");
		arr.add("E_SUM2");

		try {
			mConnector.executeRFCAsyncTask("ZMO_1150_RD01",
					"ZMO_1150_RD01", arr);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/** 법인카드 잠금 체크 */
	public void getCorCardLockCheck(String ELC_SEQ, String GUBUN) {
		LoginModel model = KtRentalApplication.getLoginModel();
		HashMap<String, String> map = getCommonConnectData();
		mConnector.setStructure("IS_LOGIN", map);
		mConnector.setParameter("I_BUKRS", "3200");
		mConnector.setParameter("I_ELC_SEQ", ELC_SEQ);
		mConnector.setParameter("I_GUBUN", GUBUN);

		kog.e("Jonathan", "I_ELC_SEQ :: " + ELC_SEQ + " GUBUN :: " + GUBUN);

		try {
			mConnector.executeRFCAsyncTask("ZMO_1150_RD03",
					"ZMO_1150_RD03_TABLE");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/** 마감체크 */
	public void getCorCardCloseCheck(String useDate, String ORGEH) {
		LoginModel model = KtRentalApplication.getLoginModel();
		HashMap<String, String> map = getCommonConnectData();
		mConnector.setStructure("IS_LOGIN", map);
		mConnector.setParameter("I_BUKRS", "3200");
		mConnector.setParameter("I_CLDAT", useDate);


		ORGEH = "2595";

		mConnector.setParameter("I_ORGEH", ORGEH);

		kog.e("Jonathan", "CLDAT :: " + useDate + " ORGEH :: " + ORGEH);

		try {
			mConnector.executeRFCAsyncTask("ZMO_1150_RD02",
					"ZMO_1150_RD02_TABLE");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/** 계정유형 */
	public void getCorCardTYPE(String DOCNAM, String DOCTYPE, String I_GUBUN) {
		LoginModel model = KtRentalApplication.getLoginModel();
		HashMap<String, String> map = getCommonConnectData();
		mConnector.setStructure("IS_LOGIN", map);
		HashMap<String, String> hashmap = new HashMap<>();
		hashmap.put("WEBTYPE", "A1");
		ArrayList<HashMap<String,String>> arr = new ArrayList<>();
		arr.add(hashmap);
//		mConnector.setTable("IT_WEBTYPE", arr);
//		mConnector.setStructure("IT_WEBTYPE", hashmap);
		mConnector.setParameter("I_BUKRS", "3200");
		mConnector.setParameter("I_DOCNAM", "");
		mConnector.setParameter("I_DOCTYPE", "");
		mConnector.setParameter("I_GUBUN", "");

		try {
			mConnector.executeRFCAsyncTask("ZMO_1150_RD04",
					"ZMO_1150_RD04_TABLE");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/** 상세조회 */
	public void getCorCardDetailSearch(String WEBTYPE, String WEBDOCNUM, String DOCTYPE, String BELNR,
									   String GJAHR, String BUDAT, String BLDAT, String SPERN,
									   String SORGEH, String CARD_NO, String ORGL_PERM_NO) {
		LoginModel model = KtRentalApplication.getLoginModel();
		HashMap<String, String> map = getCommonConnectData();

		HashMap<String, String> map2 = new HashMap<String, String>();
		map2.put("BUKRS", "3200");
		map2.put("WEBTYPE", WEBTYPE);
		map2.put("WEBDOCNUM", WEBDOCNUM);
		map2.put("DOCTYPE", DOCTYPE);
		map2.put("BELNR", BELNR);
		map2.put("GJAHR", GJAHR);
		map2.put("BUDAT", BUDAT);
		map2.put("BLDAT", BLDAT);
		map2.put("SPERN", SPERN);
		map2.put("SORGEH", SORGEH);
		map2.put("CARD_NO", CARD_NO);
		map2.put("ORGL_PERM_NO", ORGL_PERM_NO);

		mConnector.setStructure("IS_LOGIN", map);
		mConnector.setStructure("IS_ZSFIC0264", map2);

//		mConnector.setParameter("BUKRS", "3200");
//		mConnector.setParameter("WEBTYPE", WEBTYPE);
//		mConnector.setParameter("WEBDOCNUM", WEBDOCNUM);
//		mConnector.setParameter("DOCTYPE", DOCTYPE);
//		mConnector.setParameter("BELNR", BELNR);
//		mConnector.setParameter("GJAHR", GJAHR);
//		mConnector.setParameter("BUDAT", BUDAT);
//		mConnector.setParameter("BLDAT", BLDAT);
//		mConnector.setParameter("SPERN", SPERN);
//		mConnector.setParameter("SORGEH", SORGEH);
//		mConnector.setParameter("CARD_NO", CARD_NO);
//		mConnector.setParameter("ORGL_PERM_NO", ORGL_PERM_NO);

		kog.e("Jonathan", "WEBTYPE :: " + WEBTYPE + " WEBDOCNUM :: " + WEBDOCNUM + "DOCTYPE :: " + DOCTYPE
				+ " BELNR :: " + BELNR + "GJAHR :: " + GJAHR + " BUDAT :: " + BUDAT
				+ "BLDAT :: " + BLDAT + " SPERN :: " + SPERN + " SORGEH :: " + SORGEH + " CARD_NO :: " + CARD_NO + " ORGL_PERM_NO :: " + ORGL_PERM_NO);

		try {
			mConnector.executeRFCAsyncTask("ZMO_1150_RD05",
					"");
//			mConnector.executeRFCAsyncTask("ZMO_1150_RD05",
//					"ZMO_1150_RD05_TABLE");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/** 등록 */
	public void getCorCardEnroll(String WEBTYPE, String WEBDOCNUM, String DOCTYPE, String LINEITEMNUM,
								 String GJAHR, String BLART, String BUDAT, String BLDAT, String SPERN,
								 String ORGEH, String KTEXT, String VKGRP,
								 String KOART, String NEWKO, String MWSKZ, String SHKZG, String WRBTR, String WMWST,
								 String WAERS, String ZFBDT, String SGTXT, String SORGEH, String USMEM, String TAXTN_TY,
								 String ELC_SEQ, String USDEP, String CARD_NO, String ORGL_PERM_NO, String ORGL_PERM_DT,
								 String USETYPE, String BUY_CANC, String BUY_AMT, String CHG_SALE_AMT, ArrayList<HashMap<String, String>> tableHash) {

		LoginModel model = KtRentalApplication.getLoginModel();
		HashMap<String, String> map = getCommonConnectData();

		mConnector.setStructure("IS_LOGIN", map);

		ArrayList<HashMap<String, String>> it_item_table = new ArrayList<>();
		if(tableHash != null && tableHash.size() > 1) {
			// 이미 전표처리 되었다고 얼럿 띄워줘야 한다
//			mConnector.setTable("IT_ITEM", tableHash);
//			mConnector.setStructure("IT_ITEM", tableHash.get(0));
//			mConnector.setStructure("IT_ITEM", tableHash.get(1));
		} else {

			// TEST!!!
//			SORGEH = "2595";
			WEBTYPE = "A1";
			WEBDOCNUM = UUID.randomUUID().toString();

			// 차변 대변을 던져야 한다
			HashMap<String, String> itemMap1 = new HashMap<>();
			HashMap<String, String> itemMap2 = new HashMap<>();
			itemMap1.put("BUKRS", "3200");
			itemMap1.put("WEBTYPE", WEBTYPE);
			itemMap1.put("WEBDOCNUM", WEBDOCNUM); // 웹전
			itemMap1.put("LINEITEMNUM", "01"); //차변은 01, 대변은 02 로 고정
			if(ORGL_PERM_DT != null && ORGL_PERM_DT.length() >= 4){
				BUDAT = ORGL_PERM_DT.substring(0,4);
			}
			itemMap1.put("GJAHR", BUDAT); // 경비작성일자 연도만
			itemMap1.put("BLART", BLART); // 전표유형
			itemMap1.put("BLDAT", ORGL_PERM_DT); // 사용일자
			itemMap1.put("BUDAT", ORGL_PERM_DT); // 경비작성일자
			itemMap1.put("PERNR", SPERN); //로그인 사번
//			itemMap1.put("ORGEH", ORGEH); //로그인부서
			itemMap1.put("KOART", "K"); // 계정유형
			itemMap1.put("SHKZG", "H"); // 차변/대변 지시자
			itemMap1.put("NEWKO", "");  // 경비계정
			itemMap1.put("WRBTR", BUY_AMT); //공급가
			itemMap1.put("WMWST", WMWST); // 부가세.
			itemMap1.put("WAERS", "KRW"); //전표통화금액
			itemMap1.put("SGTXT", SGTXT); // 사용목적
			itemMap1.put("TAXTN_TY", TAXTN_TY); // 과세유형
			itemMap1.put("ELC_SEQ", ELC_SEQ); // 전자일련
			itemMap1.put("CARD_NO", CARD_NO); //카드번호
			itemMap1.put("ORGL_PERM_NO", ORGL_PERM_NO); //승인번호
			itemMap1.put("ORGL_PERM_DT", ORGL_PERM_DT); //사용일자
			itemMap1.put("DOCTYPE", DOCTYPE); // 계정유형
			itemMap1.put("SPERN", SPERN); // 실 사용자
//			itemMap1.put("SORGEH", SORGEH); // 실 사용부점
			if(USETYPE == null || USETYPE.equals(" ") || USETYPE.equals("")){
				USETYPE = "02";
			}
			itemMap1.put("USETYPE", USETYPE); // 사용구분, 회사사용
			itemMap1.put("USDEP", USDEP); // 주관부서
			itemMap1.put("USMEM", USMEM); // 참가인원
			itemMap1.put("MWSKZ", MWSKZ); // 부가세유형 DEDUCT
			if(ZFBDT != null){
				ZFBDT = ZFBDT.replace("/", "");
			}

			itemMap1.put("ZFBDT", ZFBDT); // APP_SCD_DT . 지급 희망일자
			itemMap1.put("BUY_CANC", BUY_CANC); // 매입취소.


			// 두번째 맵.
			itemMap2.put("BUKRS", "3200");
			itemMap2.put("WEBTYPE", WEBTYPE);
			itemMap2.put("WEBDOCNUM", WEBDOCNUM); // 웹전
			itemMap2.put("LINEITEMNUM", "02"); //차변은 01, 대변은 02 로 고정
			itemMap2.put("GJAHR", BUDAT); // 경비작성일자 연도만
			itemMap2.put("BLART", BLART); // 전표유형
			itemMap2.put("BLDAT", ORGL_PERM_DT); // 사용일자
			itemMap2.put("BUDAT", ORGL_PERM_DT); // 경비작성일자
			itemMap2.put("PERNR", SPERN); //로그인 사번
//			itemMap2.put("ORGEH", ORGEH); //로그인부서
			itemMap2.put("KOART", "S"); // 계정유형
			itemMap2.put("SHKZG", "S"); // 차변/대변 지시자
			itemMap2.put("NEWKO", NEWKO);  // 경비계정 . HKONT
			itemMap2.put("WRBTR", CHG_SALE_AMT); //공급가
//			itemMap2.put("WMWST", WMWST); // 부가세.
			itemMap2.put("WAERS", "KRW"); //전표통화금액
			itemMap2.put("SGTXT", SGTXT); // 사용목적
			itemMap2.put("TAXTN_TY", TAXTN_TY); // 과세유형
			itemMap2.put("ELC_SEQ", ELC_SEQ); // 전자일련
			itemMap2.put("CARD_NO", CARD_NO); //카드번호
			itemMap2.put("ORGL_PERM_NO", ORGL_PERM_NO); //승인번호
			itemMap2.put("ORGL_PERM_DT", ORGL_PERM_DT); //사용일자
			itemMap2.put("DOCTYPE", DOCTYPE); // 계정유형
			itemMap2.put("SPERN", SPERN); // 실 사용자
//			itemMap2.put("SORGEH", SORGEH); // 실 사용부점
			if(USETYPE == null || USETYPE.equals(" ") || USETYPE.equals("")){
				USETYPE = "02";
			}
			itemMap2.put("USETYPE", USETYPE); // 사용구분, 회사사용
			itemMap2.put("USDEP", USDEP); // 주관부서
			itemMap2.put("USMEM", USMEM); // 참가인원
			itemMap2.put("MWSKZ", MWSKZ); // 부가세유형 DEDUCT
//			itemMap2.put("ZFBDT", ZFBDT); // APP_SCD_DT . 지급 희망일자
			itemMap2.put("BUY_CANC", BUY_CANC); // 매입취소.

			it_item_table.add(itemMap1);
			it_item_table.add(itemMap2);
		}

		mConnector.setTable("IT_ITEM", it_item_table);

//		mConnector.setParameter("I_BUKRS", "3200");
//		mConnector.setParameter("WEBTYPE", WEBTYPE); mConnector.setParameter("WEBDOCNUM", WEBDOCNUM);
//		mConnector.setParameter("DOCTYPE", DOCTYPE); mConnector.setParameter("LINEITEMNUM", LINEITEMNUM); mConnector.setParameter("GJAHR", GJAHR);
//		mConnector.setParameter("BLART", BLART); mConnector.setParameter("BUDAT", BUDAT); mConnector.setParameter("BLDAT", BLDAT);
//		mConnector.setParameter("SPERN", SPERN); mConnector.setParameter("ORGEH", ORGEH); mConnector.setParameter("KTEXT", KTEXT);
//		mConnector.setParameter("VKGRP", VKGRP); mConnector.setParameter("GJAHR", GJAHR);
//		mConnector.setParameter("BUDAT", BUDAT); mConnector.setParameter("KOART", KOART);
//		mConnector.setParameter("BLDAT", BLDAT); mConnector.setParameter("NEWKO", NEWKO);
//		mConnector.setParameter("SPERN", SPERN); mConnector.setParameter("SORGEH", SORGEH);
//		mConnector.setParameter("CARD_NO", CARD_NO); mConnector.setParameter("SHKZG", SHKZG); mConnector.setParameter("MWSKZ", MWSKZ);
//		mConnector.setParameter("ORGL_PERM_NO", ORGL_PERM_NO);
//
//		mConnector.setParameter("WRBTR", WRBTR); mConnector.setParameter("WMWST", WMWST); mConnector.setParameter("WAERS", WAERS);
//		mConnector.setParameter("ZFBDT", ZFBDT); mConnector.setParameter("SGTXT", SGTXT); mConnector.setParameter("USMEM", USMEM);
//
//		mConnector.setParameter("TAXTN_TY", TAXTN_TY); mConnector.setParameter("ELC_SEQ", ELC_SEQ);
//		mConnector.setParameter("BUY_CANC", BUY_CANC);
//		mConnector.setParameter("USDEP", USDEP); mConnector.setParameter("ORGL_PERM_DT", ORGL_PERM_DT);
//		mConnector.setParameter("USETYPE", USETYPE);

		try {
			mConnector.executeRFCAsyncTask("ZMO_1150_WR01",
					"ZMO_1150_WR01_TABLE");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/** 등록
	public void getCorCardEnroll(String WEBTYPE, String WEBDOCNUM, String WEBTYPENM, String DOCTYPE, String LINEITEMNUM, String BELNR,
								 String GJAHR, String BLART, String BUDAT, String BLDAT, String BKTXT, String SPERN, String ENAME,
								 String ORGEH, String KTEXT, String VKGRP, String STATUS, String STATNM, String DOCSTATUS,
								 String LOG, String BSCHL, String KOART, String NEWKO, String NAME1, String UMSKZ, String HKONT, String TXT20,
								 String TXT50, String HKONT2, String HKONTTXT, String MWSKZ, String MWSKZNM, String BUPLA, String BUPLANM,
								 String SECCO, String SECCONM, String KOSTL, String SHKZG, String SHKZGNM, String WRBTR, String WMWST,
								 String WAERS, String EBELN, String EBELP, String BVTYP, String ZTERM, String TEXT1, String ZFBDT, String ZLSCH,
								 String ZLSCHTXT, String SGTXT, String PAYSTAT, String PAYSTATNM, String AUGDT, String AUGBL, String SENAME,
								 String SORGEH, String SKTEXT, String VKGRP_S, String DOCNAM, String ZUONR, String RKE_BUKRS, String RKE_KAUFN,
								 String KDPOS, String ARKTX, String DONATN_TYP, String DONATN_NAM, String FWBAS, String INCOMTAX,
								 String LCINCOMTAX, String USMEM, String CCNUM, String TAXTYPE, String AUFNR, String REFUND_CHK, String HDTXT,
								 String LIFNR, String NAME_H, String STCD2, String J_1KFREPRE, String WRBTR_H, String WMWST_H,
								 String MWSKZ_H, String STCD1, String BANKA, String BANKN, String TR_DATE, String SEQ, String ORG_BANK,
								 String BANKNM1, String ACCT_NO, String TBCODE, String BANKNM2, String TBACCNO, String TNAME,
								 String TTIME, String CTR_NO, String CTR_NO_SB, String THG_USWY_CD, String USWT_CD_TXT, String KAUFN_CHK, String BUY_PUTPYN,
								 String TAXTN_TYP, String ELC_SEQ, String USDEP, String CARD_NO, String ORGL_PERM_NO, String ORGL_PERM_DT,
								 String USETYPE, String USPLAC, String ATTATCHNO, String BUY_CANC, String MANDT) {
		LoginModel model = KtRentalApplication.getLoginModel();
		HashMap<String, String> map = getCommonConnectData();

		mConnector.setStructure("IS_LOGIN", map);
		mConnector.setParameter("I_BUKRS", "3200");
		mConnector.setParameter("WEBTYPE", WEBTYPE); mConnector.setParameter("WEBDOCNUM", WEBDOCNUM); mConnector.setParameter("WEBTYPENM", WEBTYPENM); mConnector.setParameter("DOCTYPE", DOCTYPE);
		mConnector.setParameter("LINEITEMNUM", LINEITEMNUM); mConnector.setParameter("BELNR", BELNR); mConnector.setParameter("GJAHR", GJAHR); mConnector.setParameter("BLART", BLART);
		mConnector.setParameter("BUDAT", BUDAT); mConnector.setParameter("BLDAT", BLDAT); mConnector.setParameter("BKTXT", BKTXT); mConnector.setParameter("SPERN", SPERN);
		mConnector.setParameter("ENAME", ENAME); mConnector.setParameter("ORGEH", ORGEH); mConnector.setParameter("KTEXT", KTEXT); mConnector.setParameter("VKGRP", VKGRP);
		mConnector.setParameter("GJAHR", GJAHR); mConnector.setParameter("STATUS", STATUS); mConnector.setParameter("STATNM", STATNM); mConnector.setParameter("DOCSTATUS", DOCSTATUS);
		mConnector.setParameter("BUDAT", BUDAT); mConnector.setParameter("LOG", LOG); mConnector.setParameter("BSCHL", BSCHL); mConnector.setParameter("KOART", KOART);
		mConnector.setParameter("BLDAT", BLDAT); mConnector.setParameter("NEWKO", NEWKO); mConnector.setParameter("NAME1", NAME1); mConnector.setParameter("BELNR", BELNR);
		mConnector.setParameter("SPERN", SPERN); mConnector.setParameter("HKONT", HKONT); mConnector.setParameter("TXT20", TXT20); mConnector.setParameter("TXT50", TXT50);
		mConnector.setParameter("SORGEH", SORGEH); mConnector.setParameter("HKONT2", HKONT2); mConnector.setParameter("HKONTTXT", HKONTTXT); mConnector.setParameter("BUPLA", BUPLA);
		mConnector.setParameter("CARD_NO", CARD_NO); mConnector.setParameter("SHKZG", SHKZG); mConnector.setParameter("MWSKZ", MWSKZ); mConnector.setParameter("MWSKZNM", MWSKZNM);
		mConnector.setParameter("ORGL_PERM_NO", ORGL_PERM_NO); mConnector.setParameter("BUPLANM", BUPLANM); mConnector.setParameter("SECCO", SECCO); mConnector.setParameter("SECCONM", SECCONM);

		mConnector.setParameter("KOSTL", KOSTL); mConnector.setParameter("SHKZGNM", SHKZGNM); mConnector.setParameter("WRBTR", WRBTR); mConnector.setParameter("WMWST", WMWST);
		mConnector.setParameter("WAERS", WAERS); mConnector.setParameter("EBELN", EBELN); mConnector.setParameter("EBELP", EBELP); mConnector.setParameter("BVTYP", BVTYP);
		mConnector.setParameter("ZTERM", ZTERM); mConnector.setParameter("TEXT1", TEXT1); mConnector.setParameter("ZFBDT", ZFBDT); mConnector.setParameter("ZLSCH", ZLSCH);
		mConnector.setParameter("ZLSCHTXT", ZLSCHTXT); mConnector.setParameter("SGTXT", SGTXT); mConnector.setParameter("PAYSTAT", PAYSTAT); mConnector.setParameter("PAYSTATNM", PAYSTATNM);
		mConnector.setParameter("AUGDT", AUGDT); mConnector.setParameter("AUGBL", AUGBL); mConnector.setParameter("SENAME", SENAME); mConnector.setParameter("SKTEXT", SKTEXT);
		mConnector.setParameter("VKGRP_S", VKGRP_S); mConnector.setParameter("DOCNAM", DOCNAM); mConnector.setParameter("ZUONR", ZUONR); mConnector.setParameter("RKE_BUKRS", RKE_BUKRS);
		mConnector.setParameter("RKE_KAUFN", RKE_KAUFN); mConnector.setParameter("LCINCOMTAX", LCINCOMTAX); mConnector.setParameter("USMEM", USMEM); mConnector.setParameter("CCNUM", CCNUM);
		mConnector.setParameter("TAXTYPE", TAXTYPE); mConnector.setParameter("AUFNR", AUFNR); mConnector.setParameter("REFUND_CHK", REFUND_CHK); mConnector.setParameter("HDTXT", HDTXT);
		mConnector.setParameter("LIFNR", LIFNR); mConnector.setParameter("NAME_H", NAME_H); mConnector.setParameter("STCD2", STCD2); mConnector.setParameter("J_1KFREPRE", J_1KFREPRE);
		mConnector.setParameter("STCD1", STCD1); mConnector.setParameter("MWSKZ_H", MWSKZ_H); mConnector.setParameter("WMWST_H", WMWST_H); mConnector.setParameter("WRBTR_H", WRBTR_H);
		mConnector.setParameter("BANKA", BANKA); mConnector.setParameter("BANKN", BANKN); mConnector.setParameter("TR_DATE", TR_DATE); mConnector.setParameter("SEQ", SEQ);

		mConnector.setParameter("UMSKZ", UMSKZ); mConnector.setParameter("KDPOS", KDPOS); mConnector.setParameter("ARKTX", ARKTX); mConnector.setParameter("DONATN_TYP", DONATN_TYP);
		mConnector.setParameter("DONATN_NAM", DONATN_NAM); mConnector.setParameter("FWBAS", FWBAS); mConnector.setParameter("INCOMTAX", INCOMTAX); mConnector.setParameter("SKTEXT", SKTEXT);
		mConnector.setParameter("VKGRP_S", VKGRP_S); mConnector.setParameter("DOCNAM", DOCNAM); mConnector.setParameter("ZUONR", ZUONR); mConnector.setParameter("RKE_BUKRS", RKE_BUKRS);
		mConnector.setParameter("BANKNM2", BANKNM2); mConnector.setParameter("ORG_BANK", ORG_BANK); mConnector.setParameter("TBACCNO", TBACCNO); mConnector.setParameter("TNAME", TNAME);
		mConnector.setParameter("TBCODE", TBCODE); mConnector.setParameter("AUFNR", AUFNR); mConnector.setParameter("REFUND_CHK", REFUND_CHK); mConnector.setParameter("HDTXT", HDTXT);
		mConnector.setParameter("USWT_CD_TXT", USWT_CD_TXT); mConnector.setParameter("KAUFN_CHK", KAUFN_CHK); mConnector.setParameter("CTR_NO_SB", CTR_NO_SB); mConnector.setParameter("THG_USWY_CD", THG_USWY_CD);
		mConnector.setParameter("ATTATCHNO", ATTATCHNO); mConnector.setParameter("BUY_CANC", BUY_CANC); mConnector.setParameter("MANDT", MANDT); mConnector.setParameter("BUY_PUTPYN", BUY_PUTPYN);
		mConnector.setParameter("TAXTN_TYP", TAXTN_TYP); mConnector.setParameter("ELC_SEQ", ELC_SEQ); mConnector.setParameter("USDEP", USDEP); mConnector.setParameter("ORGL_PERM_DT", ORGL_PERM_DT);
		mConnector.setParameter("BANKNM1", BANKNM1); mConnector.setParameter("ACCT_NO", ACCT_NO); mConnector.setParameter("TTIME", TTIME); mConnector.setParameter("CTR_NO", CTR_NO);
		mConnector.setParameter("USETYPE", USETYPE); mConnector.setParameter("USPLAC", USPLAC);

		try {
			mConnector.executeRFCAsyncTask("ZMO_1150_WR01",
					"ZMO_1150_WR01_TABLE");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}*/
	/** 등록취소 */
	public void getCorCardEnrollCancel(String WEBTYPE, String WEBDOCNUM, String DOCTYPE, String BELNR,
									   String GJAHR, String ELC_SEQ, String STATUS, String BDCMODE) {
		LoginModel model = KtRentalApplication.getLoginModel();
		HashMap<String, String> map = getCommonConnectData();
		mConnector.setStructure("IS_LOGIN", map);
		HashMap<String, String> map2 = new HashMap<>();
		map2.put("BUKRS", "3200");
		map2.put("WEBTYPE", WEBTYPE);
		map2.put("WEBDOCNUM", WEBDOCNUM);
//		map2.put("DOCTYPE", DOCTYPE);
		map2.put("BELNR", BELNR);
		map2.put("GJAHR", GJAHR);
		map2.put("ELC_SEQ", ELC_SEQ);
		map2.put("STATUS", STATUS);
//		map2.put("I_BDCMODE", "N");
		if(WEBTYPE == null || WEBDOCNUM == null || WEBTYPE.equals(" ") || WEBTYPE.equals("") || WEBDOCNUM.equals("") || WEBDOCNUM.equals(" ")){
			return;
		}
		ArrayList<HashMap<String, String>> table = new ArrayList<>();
		table.add(map2);
		mConnector.setTable("IT_ITEM", table);
//		mConnector.setStructure("IT_ITEM", map2);
//		mConnector.setParameter("I_BUKRS", "3200");
//		mConnector.setParameter("WEBTYPE", WEBTYPE);
//		mConnector.setParameter("WEBDOCNUM", WEBDOCNUM);
//		mConnector.setParameter("DOCTYPE", DOCTYPE);
//		mConnector.setParameter("BELNR", BELNR);
//		mConnector.setParameter("GJAHR", GJAHR);
//		mConnector.setParameter("ELC_SEQ", ELC_SEQ);
//		mConnector.setParameter("STATUS", STATUS);
		mConnector.setParameter("I_BDCMODE", "N");

		try {
			mConnector.executeRFCAsyncTask("ZMO_1150_WR02",
					"ZMO_1150_WR02_TABLE");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/** 전송 */
	public void getCorCardSend(HashMap<Integer, CorCardModel> checkedModels) {
		if(checkedModels == null || checkedModels.size() == 0){
			return;
		}
		LoginModel model = KtRentalApplication.getLoginModel();
		HashMap<String, String> map = getCommonConnectData();
		mConnector.setStructure("IS_LOGIN", map);

		String i_pernr = null;
		ArrayList<HashMap<String, String>> table = new ArrayList<>();

		HashMap<String, String> mapmap = new HashMap<>();
		Set entrySet = checkedModels.entrySet();
		if(entrySet != null){
			Iterator it = entrySet.iterator();
			if(it != null){
				while(it.hasNext()){
					Map.Entry me = (Map.Entry)it.next();
					CorCardModel tmpModel = (CorCardModel)me.getValue();
					HashMap<String, String> map2 = new HashMap<>();
					map2.put("WEBTYPE", tmpModel.getWEBTYPE());
					map2.put("WEBDOCNUM", tmpModel.getWEBDOCNUM());
//					map2.put("DOCTYPE", tmpModel.getDOCTYPE());
					map2.put("BELNR", tmpModel.getBELNR());
					map2.put("GJAHR", tmpModel.getGJAHR());
					map2.put("DEPLV", tmpModel.getORGEH());
					map2.put("ACCTYPE", tmpModel.getDOCTYPE());
					map2.put("SPERN", tmpModel.getSPERN());
					map2.put("SORGEH", tmpModel.getSORGEH());
					map2.put("CARDNO", tmpModel.getCARD_NO());
					map2.put("ORGL_PERM_DT", tmpModel.getORGL_PERM_DT());
					String elc_seq = tmpModel.getELC_SEQ();
					if(elc_seq != null){
						elc_seq = elc_seq.replace("0", "");
					}
					map2.put("ELC_SEQ", elc_seq);
					map2.put("PPERN", tmpModel.getSPERN());
					i_pernr = tmpModel.getSPERN();
					map2.put("PORGEH", tmpModel.getORGEH());
					map2.put("BUY_SAM", tmpModel.getBUY_SAM());
					table.add(map2);
				}
			}
		}
		mConnector.setTable("ET_ZSFIC0330", table);
		mConnector.setParameter("I_BUKRS", "3200");
		mConnector.setParameter("I_PERNR", i_pernr);
//		mConnector.setParameter("WEBTYPE", WEBTYPE);
//		mConnector.setParameter("WEBDOCNUM", WEBDOCNUM);
//		mConnector.setParameter("DOCTYPE", DOCTYPE);
//		mConnector.setParameter("BELNR", BELNR);
//		mConnector.setParameter("GJAHR", GJAHR);
//		mConnector.setParameter("DEPLV", DEPLV);
//		mConnector.setParameter("ACCTYPE", ACCTYPE);
//		mConnector.setParameter("SPERN", SPERN);
//		mConnector.setParameter("SORGEH", SORGEH);
//		mConnector.setParameter("CARD_NO", CARD_NO);
//		mConnector.setParameter("ORGL_PERM_DT", ORGL_PERM_DT);
//		mConnector.setParameter("ELC_SEQ", ELC_SEQ);
//		mConnector.setParameter("PPERN", PPERN);
//		mConnector.setParameter("PORGEH", PORGEH);
//		mConnector.setParameter("SORGEH", SORGEH);
//		mConnector.setParameter("BUY_SAM", BUY_SAM);

		try {
			mConnector.executeRFCAsyncTask("ZMO_1150_WR03",
					"ZMO_1150_WR03_TABLE");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/** 전송취소 */
	public void getCorCardSendCancel(String WEBTYPE, String WEBDOCNUM, String DOCTYPE, String BELNR,
									   String GJAHR, String ELC_SEQ, String STATUS, String BDCMODE) {
		LoginModel model = KtRentalApplication.getLoginModel();
		HashMap<String, String> map = getCommonConnectData();
		mConnector.setStructure("IS_LOGIN", map);
		HashMap<String, String> map2 = new HashMap<>();
		map2.put("BUKRS", "3200");
		map2.put("WEBTYPE", WEBTYPE);
		map2.put("WEBDOCNUM", WEBDOCNUM);
//		map2.put("DOCTYPE", DOCTYPE);
		map2.put("BELNR", BELNR);
		map2.put("GJAHR", GJAHR);
		map2.put("ELC_SEQ", ELC_SEQ);
		map2.put("STATUS", STATUS);
//		map2.put("I_BDCMODE", "N");
		ArrayList<HashMap<String, String>> table = new ArrayList<>();
		table.add(map2);
		mConnector.setTable("IT_ITEM", table);
//		mConnector.setStructure("IT_ITEM", map2);
//		mConnector.setParameter("I_BUKRS", "3200");
//		mConnector.setParameter("WEBTYPE", WEBTYPE);
//		mConnector.setParameter("WEBDOCNUM", WEBDOCNUM);
//		mConnector.setParameter("DOCTYPE", DOCTYPE);
//		mConnector.setParameter("BELNR", BELNR);
//		mConnector.setParameter("GJAHR", GJAHR);
//		mConnector.setParameter("ELC_SEQ", ELC_SEQ);
//		mConnector.setParameter("STATUS", STATUS);
		mConnector.setParameter("I_BDCMODE", "N");

		try {
			mConnector.executeRFCAsyncTask("ZMO_1150_WR04",
					"ZMO_1150_WR04_TABLE");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/** 법인카드 저장 */
	public void getCorCardSave(String CARD_NO, String ORGL_PERM_NO, String ORGL_PERM_DT,
							   String USETYPE, String ORGEH) {
		LoginModel model = KtRentalApplication.getLoginModel();
		HashMap<String, String> map = getCommonConnectData();
		mConnector.setStructure("IS_LOGIN", map);
		mConnector.setParameter("I_BUKRS", "3200");
		mConnector.setParameter("CARD_NO", CARD_NO);
		mConnector.setParameter("ORGL_PERM_NO", ORGL_PERM_NO);
		mConnector.setParameter("ORGL_PERM_DT", ORGL_PERM_DT);
		mConnector.setParameter("USETYPE", USETYPE);
		mConnector.setParameter("ORGEH", ORGEH);

		try {
			mConnector.executeRFCAsyncTask("저장",
					"ZMO_1150_RD04_TABLE");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	public void getMovement(String EQUNR, String AUFNR, String DRIVER, String DATUMF, String DATUMT) {
		LoginModel model = KtRentalApplication.getLoginModel();
		HashMap<String, String> map = getCommonConnectData();
		mConnector.setStructure("IS_LOGIN", map);
		mConnector.setParameter("I_PERNR", model.getPernr());
		mConnector.setParameter("I_EQUNR", EQUNR);
		mConnector.setParameter("I_AUFNR", AUFNR);
		mConnector.setParameter("I_DRIVER", DRIVER);
		mConnector.setParameter("I_DATUMF", DATUMF);
		mConnector.setParameter("I_DATUMT", DATUMT);
		//Jonathan 14.08.04 추가
//		mConnector.setParameter("I_STRUCT1-OTYPE", "PZ04");
		
		kog.e("Jonathan", "datumf :: " + DATUMF + " datumt :: " + DATUMT);

		try {
			mConnector.executeRFCAsyncTask(MOVEMENT_FUNTION_NAME,
					MOVEMENT_FUNTION_TABLE);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void saveMovement(MovementSaveModel saveModel) {
		LoginModel model = KtRentalApplication.getLoginModel();
		HashMap<String, String> map = getCommonConnectData();
		mConnector.setStructure("IS_LOGIN", map);
		mConnector.setParameter("I_PERNR", model.getPernr());
		mConnector.setStructure("I_STRUCT1", saveModel.getMap());

		try {
			mConnector.executeRFCAsyncTask(MOVEMENT_SAVE_FUNTION_NAME,
					MOVEMENT_SAVE_FUNTION_TABLE);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void deleteMovement(ArrayList<HashMap<String, String>> array) {
		LoginModel model = KtRentalApplication.getLoginModel();
		HashMap<String, String> map = getCommonConnectData();
		mConnector.setStructure("IS_LOGIN", map);
		mConnector.setParameter("I_PERNR", model.getPernr());

		mConnector.setTable("I_ITAB1", array);

		try {
			mConnector.executeRFCAsyncTask(MOVEMENT_DELETE_FUNTION_NAME,
					MOVEMENT_DELETE_FUNTION_TABLE);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void getZMO_3220_RD01(String I_INVNR) {
		LoginModel model = KtRentalApplication.getLoginModel();
		HashMap<String, String> map = getCommonConnectData();
		mConnector.setStructure("IS_LOGIN", map);
		mConnector.setParameter("I_PERNR", model.getPernr());
		
		mConnector.setParameter("I_INVNR", I_INVNR);

		try {
			mConnector.executeRFCAsyncTask(ZMO_3220_RD01, ZMO_3220_RD01_TABLE);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void getZMO_1130_WR01(String I_AUFNR) {
		LoginModel model = KtRentalApplication.getLoginModel();
		HashMap<String, String> map = getCommonConnectData();
		mConnector.setStructure("IS_LOGIN", map);
		mConnector.setParameter("I_PERNR", model.getPernr());
		mConnector.setParameter("I_AUFNR", I_AUFNR);

		try {
			mConnector.executeRFCAsyncTask(ZMO_1130_WR01, ZMO_1130_WR01_TABLE);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	

	public void getZMO_1140_RD02(String vbeln_vl) {
		final String ZMO_1140_RD02 = "ZMO_1140_RD02";
		HashMap<String, String> map = getCommonConnectData();
		mConnector.setStructure("IS_LOGIN", map);

		mConnector.setParameter("I_VBELN", vbeln_vl); // 접수번호

		try {
			mConnector.executeRFCAsyncTask(ZMO_1140_RD02, ZMO_1140_RD02
					+ "_table");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	
	
	
	
	
}
