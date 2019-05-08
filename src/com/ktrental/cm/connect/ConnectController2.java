package com.ktrental.cm.connect;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.provider.Settings.Secure;

import com.ktrental.cm.connect.Connector.ConnectInterface;
import com.ktrental.common.DEFINE;
import com.ktrental.common.KtRentalApplication;
import com.ktrental.model.LoginModel;
import com.ktrental.util.LogUtil;
import com.ktrental.util.SharedPreferencesUtil;
import com.ktrental.util.kog;

import java.util.ArrayList;
import java.util.HashMap;

import static com.ktrental.common.DEFINE.ZMO_1030_RD08;
import static com.ktrental.common.DEFINE.ZMO_1100_RD02;
import static com.ktrental.common.DEFINE.ZMO_1100_RD03;
import static com.ktrental.common.DEFINE.ZMO_1100_RD04;
import static com.ktrental.common.DEFINE.ZMO_1100_RD05;
import static com.ktrental.common.DEFINE.ZMO_1100_RD06;
import static com.ktrental.common.DEFINE.ZMO_1100_WR01;
import static com.ktrental.common.DEFINE.getDEBUG_MODE;

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
public class ConnectController2 {

	private Connector connector;
	private Context mContext;

	public ConnectController2(Context context) {
		initConnctAdapter((ConnectInterface) context, context);
	}

	public ConnectController2(Context context, ConnectInterface connectinterface) {
		initConnctAdapter(connectinterface, context);
	}

	private void initConnctAdapter(ConnectInterface connectInterface, Context context) {
		connector = new Connector(connectInterface, context);
	}

	String MY_PREFS = "I_CUR_STATUS";

	public void savePreferences(Context context, String key, String value) {
		SharedPreferences sp = context.getSharedPreferences(MY_PREFS, Activity.MODE_PRIVATE);
		SharedPreferences.Editor editor = sp.edit();
		editor.putString(key, value);
		editor.commit();
	}

	public String loadPreferences(Context context, String key) {
		SharedPreferences sp = context.getSharedPreferences(MY_PREFS, Activity.MODE_PRIVATE);
		return sp.getString(key, "대기");
	}

	// 차량이동리스트
	private final String ZMO_3140_RD01 = "ZMO_3140_RD01";
	
	
	//20161122 Jonathan 중복 체크 추가
	public final static String ZMO_1010_RD02 = "ZMO_1010_RD02";
	public final static String ZMO_1010_RD02_TABLE = "ZMO_1010_RD02_TABLE";
		
	
	public final static String ZMO_1010_WR02 = "ZMO_1010_WR02";
	public final static String ZMO_1010_WR02_TABLE = "ZMO_1010_WR02_TABLE";
	
	
	
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
	
	public void updateSession(Context context, String inout, String session)
	{
		String strFuncName = ZMO_1010_WR02;
		HashMap<String, String> map = getCommonConnectData();
		
		
		connector.setParameter("I_GUBUN", inout);
		connector.setParameter("I_SYS_CD", "IN3000");
		connector.setParameter("I_USR_ID", map.get("LOGID").toString());
		connector.setParameter("I_SES_ID", session);
		connector.setParameter("I_SYS_IP", getAndroidID(context));
		
		connector.setStructure("IS_LOGIN", map);
		

		try {
			connector.executeRFCAsyncTask(strFuncName, ZMO_1010_WR02_TABLE);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	public void duplicateLogin(Context context)
	{
		String strFuncName = ZMO_1010_RD02;
		HashMap<String, String> map = getCommonConnectData();
		
		kog.e("Joanthan", "duplicate connectcontroller2");
		
		connector.setParameter("I_USR_ID", map.get("LOGID").toString());
		connector.setParameter("I_SYS_CD", "IN3000");
		connector.setStructure("IS_LOGIN", map);



		try {
			connector.executeRFCAsyncTask(strFuncName, ZMO_1010_RD02_TABLE);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 차량이동 대상리스트 조회
	 * @param top1 이동유형
	 * @param top2 진행상태(1:출발대상,2:도착대상,'':전체)
	 * @param top3 차량번호
	 * @param period
	 * @param date1 이동요청일(FROM)
	 * @param date2 이동요청일(TO)
	 */
	public void getZMO_3140_RD01(String top1, String top2, String top3, String period, String date1, String date2) {
		LoginModel model = KtRentalApplication.getLoginModel();
		HashMap<String, String> map = getCommonConnectData();

		// map.put("PERNR", "70060");
		// map.put("LOGID", "R70060");
		map.put("BUKRS", "3000");

		connector.setStructure("IS_LOGIN", map);

		connector.setParameter("I_STATUS", top2); // 진행상태(1:출발대상,2:도착대상,'':전체)
		// connector.setParameter("I_VKGRP", " "); //지점
		connector.setParameter("I_LIFNR", model.getLifnr()); // 탁송사
		// connector.setParameter("I_LIFNR", "M902"); //탁송사
		connector.setParameter("I_LIFNR_CD", model.getLifnr_cd()); // 기사
		// connector.setParameter("I_LIFNR_CD", "P00070060"); //기사
		connector.setParameter("I_AUART", top1); // 이동유형
		connector.setParameter("I_INVNR", top3); // 차량번호
		connector.setParameter("I_FRDT", date1); // 이동요청일(FROM)
		connector.setParameter("I_TODT", date2); // 이동요청일(TO)
		connector.setParameter("I_VBELN", " "); // 이동요청번호

		// Log.i("##",
		// "###" + top2 + "/" + model.getLifnr() + "/"
		// + model.getLifnr_cd() + "/" + top1);
		// Log.i("##", "###" + top3 + "/" + date1 + "/" + date2);

		try {
			connector.executeRFCAsyncTask(ZMO_3140_RD01, ZMO_3140_RD01 + "_table");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private final String ZMO_3200_RD01 = "ZMO_3200_RD01";

	/**
	 * 차량이동 대상리스트 조회(오토리스)
	 * @param top1 이동유형
	 * @param top2 진행상태(1:출발대상,2:도착대상,'':전체)
	 * @param top3 차량번호
	 * @param period
	 * @param date1 이동요청일(FROM)
	 * @param date2 이동요청일(TO)
	 */
	public void getZMO_3200_RD01(String top1, String top2, String top3, String period, String date1, String date2) {
		LoginModel model = KtRentalApplication.getLoginModel();
		HashMap<String, String> map = getCommonConnectData();

		map.put("BUKRS", "3100"); //회사구분코드 오토리스(3100)로 셋팅.

		connector.setStructure("IS_LOGIN", map);
		connector.setParameter("I_MNTTYP", model.getEqunr()); // 종료일

		connector.setParameter("I_STATUS", top2); // 진행상태(1:출발대상,2:도착대상,'':전체)
		connector.setParameter("I_LIFNR", model.getLifnr()); // 탁송사
		connector.setParameter("I_LIFNR_CD", model.getLifnr_cd()); // 기사
		connector.setParameter("I_AUART", top1); // 이동유형
		connector.setParameter("I_INVNR", top3); // 차량번호
		connector.setParameter("I_FRDT", date1); // 이동요청일(FROM)
		connector.setParameter("I_TODT", date2); // 이동요청일(TO)
		connector.setParameter("I_VBELN", " "); // 이동요청번호

		// Log.i("##",
		// "###" + top2 + "/" + model.getLifnr() + "/"
		// + model.getLifnr_cd() + "/" + top1);
		// Log.i("##", "###" + top3 + "/" + date1 + "/" + date2);

		try {
			connector.executeRFCAsyncTask(ZMO_3200_RD01, ZMO_3200_RD01 + "_table");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 차량이동 대상 리스트 조회
	 * @param top1 이동유형
	 * @param top2 진행상태(1:출발대상,2:도착대상,'':전체)
	 * @param top3 차량번호
	 * @param period
	 * @param date1 이동요청일(FROM)
	 * @param date2 이동요청일(TO)
	 * @param vbeln_vl 이동요청번호
	 */
	public void getZMO_3140_RD01(String top1, String top2, String top3, String period, String date1, String date2,
			String vbeln_vl) {
		LoginModel model = KtRentalApplication.getLoginModel();
		HashMap<String, String> map = getCommonConnectData();

		map.put("BUKRS", "3000");

		connector.setStructure("IS_LOGIN", map);

		connector.setParameter("I_STSTUS", top2); // 진행상태(1:출발대상,2:도착대상,'':전체)
		connector.setParameter("I_LIFNR", model.getLifnr()); // 탁송사
		connector.setParameter("I_LIFNR_CD", model.getLifnr_cd()); // 기사
		connector.setParameter("I_AUART", top1); // 이동유형
		connector.setParameter("I_INVNR", top3); // 차량번호
		connector.setParameter("I_FRDT", date1); // 이동요청일(FROM)
		connector.setParameter("I_TODT", date2); // 이동요청일(TO)
		connector.setParameter("I_VBELN", vbeln_vl); // 이동요청번호

		// Log.i("####", "#### 상세" + top1 + "/" + top2 + "/" + top3 + "/" +
		// period
		// + "/" + date1 + "/" + date2 + "/" + vbeln_vl);

		try {
			connector.executeRFCAsyncTask(ZMO_3140_RD01, ZMO_3140_RD01 + "_table");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// myung 20131128 ADD [MO]수행자 업무 상태 저장
	private final String ZMO_1120_WR02 = "ZMO_1120_WR02";
	private final String ZMO_1120_WR02_TABLE = "zmo_1120_wr02_table";

	/**
	 * [MO] 수행자 업무 상태 저장
	 * @param i_cur_status 카매니저 상태
	 */
	public void setZMO_1120_WR02(String i_cur_status) {
		LoginModel model = KtRentalApplication.getLoginModel();
		HashMap<String, String> map = getCommonConnectData();
		connector.setStructure("IS_LOGIN", map);
		connector.setParameter("I_PERNR", model.getPernr()); // 사용자번호
		connector.setParameter("I_CUR_STATUS", i_cur_status); // 카매니저 상태
		try {
			connector.executeRFCAsyncTask(ZMO_1120_WR02, ZMO_1120_WR02_TABLE);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 차량이동 대상리스트 조회(오토리스)
	 * @param top1 이동유형
	 * @param top2 진행상태(1:출발대상,2:도착대상,'':전체)
	 * @param top3 차량번호
	 * @param period
	 * @param date1 이동요청일(FROM)
	 * @param date2 이동요청일(TO)
	 * @param vbeln_vl 이동요청번호
	 */
	public void getZMO_3200_RD01(String top1, String top2, String top3, String period, String date1, String date2,
			String vbeln_vl) {
		LoginModel model = KtRentalApplication.getLoginModel();
		HashMap<String, String> map = getCommonConnectData();

		map.put("BUKRS", "3100");

		connector.setStructure("IS_LOGIN", map);

		connector.setParameter("I_STSTUS", top2); // 진행상태(1:출발대상,2:도착대상,'':전체)
		connector.setParameter("I_LIFNR", model.getLifnr()); // 탁송사
		connector.setParameter("I_LIFNR_CD", model.getLifnr_cd()); // 기사
		connector.setParameter("I_AUART", top1); // 이동유형
		connector.setParameter("I_INVNR", top3); // 차량번호
		connector.setParameter("I_FRDT", date1); // 이동요청일(FROM)
		connector.setParameter("I_TODT", date2); // 이동요청일(TO)
		connector.setParameter("I_VBELN", vbeln_vl); // 이동요청번호

		// Log.i("####", "#### 상세" + top1 + "/" + top2 + "/" + top3 + "/" +
		// period
		// + "/" + date1 + "/" + date2 + "/" + vbeln_vl);

		try {
			connector.executeRFCAsyncTask(ZMO_3200_RD01, ZMO_3200_RD01 + "_table");
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
		connector.downloadDB(version, directory);
	}

	private final String ZMO_3140_WR01 = "ZMO_3140_WR01";

	/**
	 * 차량이동 출발 등록
	 * @param i_struct1
	 */
	public void setZMO_3140_WR01(HashMap<String, String> i_struct1) {
		LoginModel model = KtRentalApplication.getLoginModel();
		HashMap<String, String> map = getCommonConnectData();

		map.put("BUKRS", "3000");

		connector.setStructure("IS_LOGIN", map);
		connector.setStructure("I_STRUCT1", i_struct1);

		try {
			connector.executeRFCAsyncTask(ZMO_3140_WR01, ZMO_3140_WR01 + "_table");
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private final String ZMO_3200_WR01 = "ZMO_3200_WR01";

	public void setZMO_3200_WR01(HashMap<String, String> i_struct1) {
		LoginModel model = KtRentalApplication.getLoginModel();
		HashMap<String, String> map = getCommonConnectData();

		map.put("BUKRS", "3100");

		connector.setStructure("IS_LOGIN", map);
		connector.setStructure("I_STRUCT1", i_struct1);

		try {
			connector.executeRFCAsyncTask(ZMO_3200_WR01, ZMO_3200_WR01 + "_table");
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private final String ZMO_3140_WR02 = "ZMO_3140_WR02";

	public void setZMO_3140_WR02(HashMap<String, String> i_struct1) {
		LoginModel model = KtRentalApplication.getLoginModel();
		HashMap<String, String> map = getCommonConnectData();

		// map.put("PERNR", "70060");
		// map.put("LOGID", "R70060");
		map.put("BUKRS", "3000");

		connector.setStructure("IS_LOGIN", map);
		connector.setStructure("I_STRUCT1", i_struct1);

		try {
			connector.executeRFCAsyncTask(ZMO_3140_WR02, ZMO_3140_WR02 + "_table");
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private final String ZMO_3200_WR02 = "ZMO_3200_WR02";

	public void setZMO_3200_WR02(HashMap<String, String> i_struct1) {
		LoginModel model = KtRentalApplication.getLoginModel();
		HashMap<String, String> map = getCommonConnectData();

		// map.put("PERNR", "70060");
		// map.put("LOGID", "R70060");
		map.put("BUKRS", "3100");

		connector.setStructure("IS_LOGIN", map);
		connector.setStructure("I_STRUCT1", i_struct1);

		try {
			connector.executeRFCAsyncTask(ZMO_3200_WR02, ZMO_3200_WR02 + "_table");
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private final String ZMO_3140_WR03 = "ZMO_3140_WR03";

	public void setZMO_3140_WR03(HashMap<String, String> i_struct1) {
		LoginModel model = KtRentalApplication.getLoginModel();
		HashMap<String, String> map = getCommonConnectData();

		// map.put("PERNR", "70060");
		// map.put("LOGID", "R70060");
		map.put("BUKRS", "3000");

		connector.setStructure("IS_LOGIN", map);
		connector.setStructure("I_STRUCT1", i_struct1);

		try {
			connector.executeRFCAsyncTask(ZMO_3140_WR03, ZMO_3140_WR03 + "_table");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private final String ZMO_3200_WR03 = "ZMO_3200_WR03";

	public void setZMO_3200_WR03(HashMap<String, String> i_struct1) {
		LoginModel model = KtRentalApplication.getLoginModel();
		HashMap<String, String> map = getCommonConnectData();

		// map.put("PERNR", "70060");
		// map.put("LOGID", "R70060");
		map.put("BUKRS", "3100");

		connector.setStructure("IS_LOGIN", map);
		connector.setStructure("I_STRUCT1", i_struct1);

		try {
			connector.executeRFCAsyncTask(ZMO_3200_WR03, ZMO_3200_WR03 + "_table");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private final String ZMO_3140_WR04 = "ZMO_3140_WR04";

	public void setZMO_3140_WR04(HashMap<String, String> i_struct1) {
		HashMap<String, String> map = getCommonConnectData();

		// map.put("PERNR", "70060");
		// map.put("LOGID", "R70060");
		map.put("BUKRS", "3000");

		connector.setStructure("IS_LOGIN", map);
		connector.setStructure("I_STRUCT1", i_struct1);

		try {
			connector.executeRFCAsyncTask(ZMO_3140_WR04, ZMO_3140_WR04 + "_table");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private final String ZMO_3200_WR04 = "ZMO_3200_WR04";

	public void setZMO_3200_WR04(HashMap<String, String> i_struct1) {
		HashMap<String, String> map = getCommonConnectData();

		// map.put("PERNR", "70060");
		// map.put("LOGID", "R70060");
		map.put("BUKRS", "3100");

		connector.setStructure("IS_LOGIN", map);
		connector.setStructure("I_STRUCT1", i_struct1);

		try {
			connector.executeRFCAsyncTask(ZMO_3200_WR04, ZMO_3200_WR04 + "_table");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private final String ZMO_1030_RD07 = "ZMO_1030_RD07";

	public void getZMO_1030_RD07(String date1, String date2, String carnum) {
		HashMap<String, String> map = getCommonConnectData();
		connector.setStructure("IS_LOGIN", map);
		LoginModel lm = KtRentalApplication.getLoginModel();

		connector.setParameter("I_PERNR", lm.getPernr()); // 사용자번호
		// connector.setParameter("I_DATEGBN", lm.getPernr());
		// //"신청일자구분 (""접수일자"" 고정)"
		connector.setParameter("I_FRDATE", date1); // 시작일
		connector.setParameter("I_TODATE", date2); // 종료일
		connector.setParameter("I_INVNR", carnum); // 고객차량번호
		connector.setParameter("I_INGRP", "A"); // 소속 MOT
		connector.setParameter("I_RQPRN", lm.getPernr()); // 순회사원번호
		// connector.setParameter("I_FRDATE", date1); //"신청부서구분 (""신청부서"" 고정)"

		try {
			connector.executeRFCAsyncTask(ZMO_1030_RD07, ZMO_1030_RD07 + "_table");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 2013.11.18 YPKIM
	// 정비유형 추가.
	public void getZMO_1100_RD03(String carnum, String type, String I_SNDCUS, String date1, String date2, String gubun) {
		HashMap<String, String> map = getCommonConnectData();
		connector.setStructure("IS_LOGIN", map);
		LoginModel lm = KtRentalApplication.getLoginModel();


		for (int i=0; i<10; i++) {
			if (lm == null) {
				String id = SharedPreferencesUtil.getInstance().getLastLoginId();
				String pw = "1";
				ConnectController.getInstance().logIn(id, pw, DEFINE.SESSION, connector.getConText());
				lm = KtRentalApplication.getLoginModel();
				LogUtil.e("hjt", "lm == null!!! count == " + i);
			} else {
				LogUtil.e("hjt", "lm != null!!! count == " + i);
				break;
			}
		}

		connector.setParameter("I_PERNR", lm.getPernr()); // 사용자번호
		connector.setParameter("I_INVNR", carnum); // 고객차량번호
		connector.setParameter("I_MNTTYP", type); // 정비유형
		// connector.setParameter("I_STATUS", status); // 진행상태
		connector.setParameter("I_SNDCUS", I_SNDCUS); // 고객안내상태()
		// 2014-02-21 KDH 인자값 변경. I_SNDCUS
		connector.setParameter("I_RECDTFR", date1); // 시작일
		connector.setParameter("I_RECDTTO", date2); // 종료일
		connector.setParameter("I_ATVYN", gubun); // 대상구분

		try {
			connector.executeRFCAsyncTask(ZMO_1100_RD03, ZMO_1100_RD03 + "_table");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void getZMO_1100_RD02(String date1, String date2) {
		HashMap<String, String> map = getCommonConnectData();
		if(map == null || map.size() == 0){
			return;
		}
		try {
			connector.setStructure("IS_LOGIN", map);
			LoginModel lm = KtRentalApplication.getLoginModel();

			connector.setParameter("I_RECDTFR", date1);
			connector.setParameter("I_RECDTTO", date2);
			connector.setParameter("I_INGPR", lm.getINGRP());
			// connector.setParameter("I_INGPR", "110");
			connector.setParameter("I_DAM02", lm.getPernr());
		} catch (Exception e){
			e.printStackTrace();
		}

		// Log.i("####", "####I_RECDTFR " + date1);
		// Log.i("####", "####I_RECDTTO " + date2);
		// Log.i("####", "####I_INGPR" + lm.getINGRP());
		// Log.i("####", "####I_DAM02 " + lm.getPernr());

		try {
			connector.executeRFCAsyncTask(ZMO_1100_RD02, ZMO_1100_RD02 + "_table");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void getZMO_1100_RD03(String status, String date1, String date2) {
		HashMap<String, String> map = getCommonConnectData();
		connector.setStructure("IS_LOGIN", map);
		LoginModel lm = KtRentalApplication.getLoginModel();

		connector.setParameter("I_PERNR", lm.getPernr()); // 사용자번호
		connector.setParameter("I_INVNR", " "); // 고객차량번호
		connector.setParameter("I_STATUS", status);
		connector.setParameter("I_RECDTFR", date1); // 시작일
		connector.setParameter("I_RECDTTO", date2); // 종료일
		connector.setParameter("I_MNTTYP", "A");

		try {
			connector.executeRFCAsyncTask(ZMO_1100_RD03, ZMO_1100_RD03 + "_table");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void getZMO_1100_RD03(String date) {
		HashMap<String, String> map = getCommonConnectData();
		connector.setStructure("IS_LOGIN", map);
		LoginModel lm = KtRentalApplication.getLoginModel();

		connector.setParameter("I_PERNR", lm.getPernr()); // 사용자번호
		connector.setParameter("I_INVNR", " "); // 고객차량번호
		connector.setParameter("I_STATUS", "A");
		connector.setParameter("I_RECDTFR", date); // 시작일
		connector.setParameter("I_RECDTTO", date); // 종료일
		// 2013.11.18 ypkim
		connector.setParameter("I_MNTTYP", "A");

		try {
			connector.executeRFCAsyncTask(ZMO_1100_RD03, ZMO_1100_RD03 + "_table");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void getZMO_1100_RD05(String aUFNR) {
		HashMap<String, String> map = getCommonConnectData();
		connector.setStructure("IS_LOGIN", map);
		LoginModel lm = KtRentalApplication.getLoginModel();

		connector.setParameter("I_PERNR", lm.getPernr()); // 사용자번호
		connector.setParameter("I_AUFNR", aUFNR);

		ArrayList<String> arr = new ArrayList<String>();
		arr.add("O_REQDES");

		try {
			connector.executeRFCAsyncTask(ZMO_1100_RD05, ZMO_1100_RD05 + "_table", arr);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void getZMO_1100_RD06(String aUFNR) {
		HashMap<String, String> map = getCommonConnectData();
		connector.setStructure("IS_LOGIN", map);
		LoginModel lm = KtRentalApplication.getLoginModel();

		connector.setParameter("I_PERNR", lm.getPernr()); // 사용자번호
		connector.setParameter("I_AUFNR", aUFNR);

		ArrayList<String> arr = new ArrayList<String>();
		arr.add("O_ACCDES");
		arr.add("O_INTCONT");
		arr.add("O_CUSCONT");
		arr.add("O_VENCONT");

		try {
			connector.executeRFCAsyncTask(ZMO_1100_RD06, ZMO_1100_RD06 + "_table", arr);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void getZMO_1100_RD04(String aUFNR) {
		HashMap<String, String> map = getCommonConnectData();
		connector.setStructure("IS_LOGIN", map);
		LoginModel lm = KtRentalApplication.getLoginModel();

		connector.setParameter("I_PERNR", lm.getPernr()); // 사용자번호
		connector.setParameter("I_AUFNR", aUFNR);

		ArrayList<String> arr = new ArrayList<String>();
		arr.add("O_EMRCONT");

		try {
			connector.executeRFCAsyncTask(ZMO_1100_RD04, ZMO_1100_RD04 + "_table", arr);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 2013.12.03 ypkim
	public void getZMO_1030_RD08(String aUFNR) {
		HashMap<String, String> map = getCommonConnectData();
		connector.setStructure("IS_LOGIN", map);
		LoginModel lm = KtRentalApplication.getLoginModel();

		ArrayList<String> arr = new ArrayList<String>();
		arr.add("O_TRCUSR");
		arr.add("O_TRSPEC");

		connector.setParameter("I_PERNR", lm.getPernr()); // 사용자번호
		connector.setParameter("I_AUFNR", aUFNR);

		try {
			connector.executeRFCAsyncTask(ZMO_1030_RD08, ZMO_1030_RD08 + "_table", arr);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 2014-02-05 KDH 이거맞나-ㅇ-?
	public void getZMO_1100_WR01(ArrayList<HashMap<String, String>> IT_TAB) {
		HashMap<String, String> map = getCommonConnectData();
		connector.setStructure("IS_LOGIN", map);
		LoginModel lm = KtRentalApplication.getLoginModel();

		map.put("BUKRS", "3000");

		connector.setParameter("I_PERNR", lm.getPernr()); // 사용자번호
		connector.setTable("IT_TAB", IT_TAB);

		try {
			connector.executeRFCAsyncTask(ZMO_1100_WR01, ZMO_1100_WR01 + "_table");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// Jonathan 150406

	public void setZMO_1100_WR02(String MODE, ArrayList<HashMap<String, String>> tableZMO_1100_WR02) {

		final String ZMO_1100_WR02 = "ZMO_1100_WR02";
		final String ZMO_1100_WR02_TABLE = "zmo_1100_wr02_table";

		LoginModel model = KtRentalApplication.getLoginModel();
		HashMap<String, String> map = getCommonConnectData();
		connector.setStructure("IS_LOGIN", map);
		connector.setParameter("I_FILGBN", MODE);

		connector.setTable("I_ITAB1", tableZMO_1100_WR02);

		try {
			ArrayList<String> arr = new ArrayList<String>();
			arr.add("O_ATTATCHNO");
			connector.executeRFCAsyncTask(ZMO_1100_WR02, ZMO_1100_WR02_TABLE, arr);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	// Jonathan 150406

	// public void setZMO_1100_WR02(
	// String MODE,
	// ArrayList<HashMap<String, String>> tableZMO_1100_WR01_1
	// ) {
	// final String ZMO_1060_WR02 = "ZMO_1060_WR02";
	// final String ZMO_1060_WR02_TABLE = "zmo_1060_wr02_table";
	//
	// HashMap<String, String> map = getCommonConnectData();
	// connector.setStructure("IS_LOGIN", map);
	// connector.setParameter("I_FILGBN", MODE);
	// connector.setTable("IT_DLDOCS", tableZMO_1100_WR01_1);
	//
	// try {
	// ArrayList<String> arr = new ArrayList<String>();
	// arr.add("O_ATTATCHNO");
	// connector.executeRFCAsyncTask(ZMO_1060_WR02, ZMO_1060_WR02_TABLE);
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	//
	// }

	// 2014-02-11 ZMO_1120_WR01
	public void getZMO_1120_WR01(String I_AUFNR, String ZCODEV) {
		final String ZMO_1120_WR01 = "ZMO_1120_WR01";
		HashMap<String, String> map = getCommonConnectData();
		map.put("BUKRS", "3000");

		connector.setStructure("IS_LOGIN", map);
		LoginModel lm = KtRentalApplication.getLoginModel();

		connector.setParameter("I_PERNR", lm.getPernr()); // 사용자번호
		connector.setParameter("I_AUFNR", I_AUFNR); // 접수번호
		connector.setParameter("I_SNDCUS", ZCODEV); // 안내값
		// connector.setTable("IT_TAB", IT_TAB);

		try {
			connector.executeRFCAsyncTask(ZMO_1120_WR01, ZMO_1120_WR01 + "_table");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 2014-03-25 KDH 정비차량체크리스트 조회
	public void getZMO_1120_RD01(String I_AUFNR) {
		final String ZMO_1120_RD01 = "ZMO_1120_RD01";
		HashMap<String, String> map = getCommonConnectData();
		map.put("BUKRS", "3000");
		connector.setStructure("IS_LOGIN", map);
		LoginModel lm = KtRentalApplication.getLoginModel();
		connector.setParameter("I_PERNR", lm.getPernr()); // 사용자번호
		connector.setParameter("I_AUFNR", I_AUFNR); // 접수번호
		// connector.setTable("IT_TAB", IT_TAB);

		kog.e("KDH", "getPernr = " + lm.getPernr());
		kog.e("KDH", "I_AUFNR  = " + I_AUFNR);

		try {
			connector.executeRFCAsyncTask(ZMO_1120_RD01, ZMO_1120_RD01 + "_table");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 2014-03-26 KDH 추가
	public void getZMO_1120_WR03(ArrayList<HashMap<String, String>> O_ITAB1) {
		final String ZMO_1120_WR03 = "ZMO_1120_WR03";
		HashMap<String, String> map = getCommonConnectData();
		map.put("BUKRS", "3000");
		connector.setStructure("IS_LOGIN", map);

		LoginModel lm = KtRentalApplication.getLoginModel();
		connector.setParameter("I_PERNR", lm.getPernr()); // 사용자번호
		// connector.setTable("IT_TAB", IT_TAB);
		connector.setTable("O_ITAB1", O_ITAB1);

		try {
			connector.executeRFCAsyncTask(ZMO_1120_WR03, ZMO_1120_WR03 + "_table");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	
	// 20160831 Jonathan
	public void getZPM_0110_007(String aUFNR) {
		
		final String ZPM_0110_007 = "ZPM_0110_007";
		HashMap<String, String> map = getCommonConnectData();
		connector.setStructure("IS_LOGIN", map);
		LoginModel lm = KtRentalApplication.getLoginModel();

		connector.setParameter("I_CHECKYN", "Y"); // 사용자번호
		connector.setParameter("I_AUFNR", aUFNR);
		
		//I_AUFNR , I_CHECKYN, IS_LOGIN

		ArrayList<String> arr = new ArrayList<String>();
		arr.add("O_REQDES");
		
		

		try {
			connector.executeRFCAsyncTask(ZPM_0110_007, ZPM_0110_007 + "_table");
//			connector.executeRFCAsyncTask(ZMO_1100_RD05, ZMO_1100_RD05 + "_table", arr);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void getZMO_1160_RD01(String i_gubun, String i_major) {

		final String ZMO_1160_RD01 = "ZMO_1160_RD01";
		HashMap<String, String> map = getCommonConnectData();
//		connector.setStructure("IS_LOGIN", map);
		connector.setParameter("I_GUBUN", i_gubun);
		connector.setParameter("I_MAJOR", i_major);

		//I_AUFNR , I_CHECKYN, IS_LOGIN

//		ArrayList<String> arr = new ArrayList<String>();
//		arr.add("O_REQDES");

		try {
			connector.executeRFCAsyncTask(ZMO_1160_RD01, ZMO_1160_RD01 + "_table");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	// 20160831 Jonathan
	public void getZMO_0110_004(String aUFNR) {
		
		final String ZMO_0110_004 = "ZMO_0110_004";
		HashMap<String, String> map = getCommonConnectData();
		connector.setStructure("IS_LOGIN", map);
		connector.setParameter("I_AUFNR", aUFNR);
		
		//I_AUFNR , I_CHECKYN, IS_LOGIN

		ArrayList<String> arr = new ArrayList<String>();
		arr.add("O_REQDES");
		
		

		try {
			connector.executeRFCAsyncTask(ZMO_0110_004, ZMO_0110_004 + "_table");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	
	public void getZMO_1030_WR07(String aUFNR) {
		
		final String ZMO_1030_WR07 = "ZMO_1030_WR07";
		LoginModel model = KtRentalApplication.getLoginModel();
		HashMap<String, String> map = getCommonConnectData();
		connector.setStructure("IS_LOGIN", map);
		connector.setParameter("I_PERNR", model.getPernr());
		connector.setParameter("I_CHECKYN", "Y");
		connector.setParameter("I_AUFNR", aUFNR);
		
		
		//I_AUFNR , I_CHECKYN, IS_LOGIN
	
		ArrayList<String> arr = new ArrayList<String>();
		arr.add("O_REQDES");
		
		
	
		try {
			connector.executeRFCAsyncTask(ZMO_1030_WR07, ZMO_1030_WR07 + "_table");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	
	public void setZMO_1100_WR03(String aUFNR, String erdat, String erzet, String kunnr, String invnr) {
		LoginModel model = KtRentalApplication.getLoginModel();
		HashMap<String, String> map = getCommonConnectData();
		
//		I_AUFNR  : 접수번호
//		I_ERDAT  : 통화 일
//		I_ERZET  : 통화 시분초
//		I_KUNNR : 고객번호(코드)
//		I_INVNR : 차량번호
//		IS_LOGIN : 로그인 기본
		
		final String ZMO_1100_WR03 = "ZMO_1100_WR03";
		
		kog.e("Jonathan", "Jonathan setZMO_1100_WR03 1");
		
		connector.setParameter("I_AUFNR", aUFNR);
		connector.setParameter("I_ERDAT", erdat);
		connector.setParameter("I_ERZET", erzet);
		connector.setParameter("I_KUNNR", kunnr);
		connector.setParameter("I_INVNR", invnr);
		connector.setStructure("IS_LOGIN", map);
		
		kog.e("Jonathan", "Jonathan setZMO_1100_WR03 2");
		
		
		
		
		
		try {
			connector.executeRFCAsyncTask(ZMO_1100_WR03, ZMO_1100_WR03 + "_table");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 2017-05-24. hjt
	 * 단순 종료 기능 추가
	 * 카매니저.
	 * 정비접수내역 중 정비유형이 사고 인 것에 대해
	 * 현장에서 사고 종료처리를 할 수 있도록.
	 * @param aUFNR : 사고접수번호
	 * @param invnr : 차량번호
	 * @param prmsts : 정비 상태
	 *
	 */
	public void setZMO_1100_WR04(String aUFNR, String invnr, String prmsts) {
		LoginModel model = KtRentalApplication.getLoginModel();
		HashMap<String, String> map = getCommonConnectData();

//		I_AUFNR  : 접수번호
//		I_INVNR : 차량번호
//		IS_LOGIN : 로그인 기본

		kog.e("hjt", "hjt setZMO_1100_WR04 1");

		connector.setParameter("I_AUFNR", aUFNR);
		connector.setParameter("I_INVNR", invnr);
		connector.setParameter("I_PRMSTS", prmsts);
		connector.setStructure("IS_LOGIN", map);

		kog.e("hjt", "hjt setZMO_1100_WR04 setParameter I_AUFNR = " + aUFNR);
		kog.e("hjt", "hjt setZMO_1100_WR04 setParameter I_INVNR = " + invnr);
		kog.e("hjt", "hjt setZMO_1100_WR04 setParameter I_PRMSTS = " + prmsts);

		try {
			connector.executeRFCAsyncTask(DEFINE.ZMO_1100_WR04, DEFINE.ZMO_1100_WR04);
//			connector.executeRFCAsyncTask(DEFINE.ZMO_1100_WR04, DEFINE.ZMO_1100_WR04 + "_table");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	//2019-01-10 hjt
	public void getZPM_0087_001(String i_postcd, String i_aufnr, String i_impyn) {

		final String ZPM_0087_001 = "ZPM_0087_001";
		HashMap<String, String> map = getCommonConnectData();
		LoginModel model = KtRentalApplication.getLoginModel();
		connector.setStructure("IS_LOGIN", map);
		if(getDEBUG_MODE()){
			i_postcd = "01012";
//			i_aufnr = "4100411524";
		} else {
			;
		}
		connector.setParameter("I_POSTCD", i_postcd);
		connector.setParameter("I_AUFNR", i_aufnr);
		connector.setParameter("I_IMPYN", i_impyn);


		try {
			connector.executeRFCAsyncTask(ZPM_0087_001, ZPM_0087_001);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	//2019-01-10 hjt
	public void getZPM_0087_002(String lifnr) {

		final String ZPM_0087_002 = "ZPM_0087_002";
		HashMap<String, String> map = getCommonConnectData();
		connector.setStructure("IS_LOGIN", map);
		connector.setParameter("I_LIFNR", lifnr);


		try {
			connector.executeRFCAsyncTask(ZPM_0087_002, ZPM_0087_002);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/** 2019.01.31 hjt
	 * SMS 발송 실패일때 "X" 전송.
	 * 성공하면 " " 공백으로 전송.*/
	public void getZPM_0087_007(String AUFNR, String I_SPSMSFYN) {

		final String ZPM_0087_007 = "ZPM_0087_007";
		HashMap<String, String> map = getCommonConnectData();
		connector.setStructure("IS_LOGIN", map);
		connector.setParameter("I_AUFNR", AUFNR);
		connector.setParameter("I_SPSMSFYN", I_SPSMSFYN);


		try {
			connector.executeRFCAsyncTask(ZPM_0087_007, ZPM_0087_007);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 *
	 * @param BUKRS
	 * @param KUNNRNM
	 * @param USERID "PM019" 사용
	 * @param TEMPCD
	 * @param JSCD
	 * @param TRTEL 수신번호
	 * @param ANSTEL 회신번호
	 * @param SCHETP '5' : 알림톡, '0' : 즉시발송, '1' : 예약발송
	 * @param SUBJECT
	 * @param MSG
	 */
	public void ZSD_SEND_SMS(String BUKRS, String KUNNRNM, String USERID, String TEMPCD, String JSCD,
							 String TRTEL, String ANSTEL, String SCHETP, String SUBJECT, String MSG) {

		final String ZSD_SEND_SMS_STR = "ZSD_SEND_SMS_STR";
		LoginModel model = KtRentalApplication.getLoginModel();
		HashMap<String, String> map = getCommonConnectData();

		HashMap<String, String> RT5001 = new HashMap<>();
		ArrayList<HashMap<String, String>> ZSSDC5001_table = new ArrayList<HashMap<String, String>>();
		map.put("BUKRS", "3000");
		RT5001.put("BUKRS", "3000");
		RT5001.put("KUNNRNM", KUNNRNM);
		RT5001.put("USERID", USERID);
		RT5001.put("TEMPCD", TEMPCD);
		RT5001.put("TRTEL", TRTEL);
		RT5001.put("ANSTEL", ANSTEL);
		RT5001.put("SCHETP", SCHETP);
		RT5001.put("SUBJECT", SUBJECT);
		RT5001.put("MSG", MSG);
		ZSSDC5001_table.add(RT5001);
		connector.setStructure("IS_LOGIN", map);
		connector.setTable("RT5001", ZSSDC5001_table);
//		connector.setParameter("BUKRS", "3000");
//		connector.setParameter("KUNNRNM", KUNNRNM);
//		connector.setParameter("USERID", USERID);
//		connector.setParameter("TEMPCD", TEMPCD);
////		connector.setParameter("JSCD", JSCD);
//		connector.setParameter("TRTEL", TRTEL);
//		connector.setParameter("ANSTEL", TRTEL);
//		connector.setParameter("SCHETP", SCHETP);
//		connector.setParameter("SUBJECT", SUBJECT);
//		connector.setParameter("MSG", MSG);

		try {
			connector.executeRFCAsyncTask(ZSD_SEND_SMS_STR, ZSD_SEND_SMS_STR);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// private final String ZMO_1060_WR01 = "ZMO_1060_WR01";
	// private final String ZMO_1060_WR01_TABLE = "zmo_1060_wr01_table";
	//
	// public void setZMO_1060_WR01(
	// ArrayList<HashMap<String, String>> tableZMO_1060_WR01, String trcusr,
	// String trspec)
	// {
	// LoginModel model = KtRentalApplication.getLoginModel();
	// HashMap<String, String> map = getCommonConnectData();
	// mConnector.setStructure("IS_LOGIN", map);
	// mConnector.setParameter("I_PERNR", model.getPernr());
	// //
	// // mConnector.setParameter("I_TRCUSR", trcusr);
	// // mConnector.setParameter("I_TRSPEC", trspec);
	// mConnector.setTable("I_ITAB1", tableZMO_1060_WR01);
	// try
	// {
	// ArrayList<String> arr = new ArrayList<String>();
	// arr.add("O_ATTATCHNO");
	// mConnector.executeRFCAsyncTask(ZMO_1060_WR01, ZMO_1060_WR01_TABLE,
	// arr);
	// }
	// catch(Exception e)
	// {
	// e.printStackTrace();
	// }
	// }
	// private final String ZMO_1090_WR01 = "ZMO_1090_WR01";
	// private final String ZMO_1090_WR01_TABLE = "zmo_1090_wr01_table";
	//
	// public void getZMO_1090_WR01(ArrayList<HashMap<String, String>> table,
	// String tp)
	// {
	// LoginModel model = KtRentalApplication.getLoginModel();
	// HashMap<String, String> map = getCommonConnectData();
	// mConnector.setStructure("IS_LOGIN", map);
	// mConnector.setParameter("I_PERNR", model.getPernr());
	// mConnector.setParameter("I_MINVNR", model.getInvnr());
	// mConnector.setParameter("I_PROC_TP", tp);
	// if (table == null) return;
	// for (int i = 0; i < table.size(); i++)
	// {
	// Log.i("###", "#### 보내는 주소" + table.get(i).get("DELVR_ADDR"));
	// }
	// mConnector.setTable("I_ITAB1", table);
	// try
	// {
	// mConnector.executeRFCAsyncTask(ZMO_1090_WR01, ZMO_1090_WR01_TABLE);
	// }
	// catch(Exception e)
	// {
	// e.printStackTrace();
	// }
	// }
	//
	// private final String ZMO_1090_RD01 = "ZMO_1090_RD01";
	// private final String ZMO_1090_RD01_TABLE = "zmo_1090_rd01_table";
	// public void getZMO_1090_RD01()
	// {
	// LoginModel model = KtRentalApplication.getLoginModel();
	// HashMap<String, String> map = getCommonConnectData();
	// mConnector.setStructure("IS_LOGIN", map);
	// mConnector.setParameter("I_PERNR", model.getPernr());
	// mConnector.setParameter("I_MINVNR", model.getInvnr());
	// try
	// {
	// mConnector.executeRFCAsyncTask(ZMO_1090_RD01, ZMO_1090_RD01_TABLE);
	// }
	// catch(Exception e)
	// {
	// e.printStackTrace();
	// }
	// }

}
