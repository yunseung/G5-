package com.ktrental.cm.connect;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.ktrental.R;
import com.ktrental.cm.util.PrintLog;
import com.ktrental.common.DEFINE;
import com.ktrental.model.TableModel;
import com.ktrental.popup.EventPopupC;
import com.ktrental.product.BaseActivity;
import com.ktrental.util.NetworkUtil;
import com.ktrental.util.SharedPreferencesUtil;
import com.ktrental.util.kog;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import phos.android.client.Column;
import phos.android.client.ICode;
import phos.android.client.MiddlewareConnector;
import phos.android.client.ParameterList;
import phos.android.client.PhosCrypto;
import phos.android.client.Row;
import phos.android.client.Structure;
import phos.android.client.Table;

public class Connector {

	private static final String TAG = "Connector";

	final int D_STRUCTURE_FLAG = 0;
	final int D_TABLE_FLAG = 1;
	public static final int D_EXECUTE_SUCCESS = 0;
	public static final int D_TABLE_EXECUTE_SUCCESS = 1;

	public static final int D_EXECUTE_ERROR = -1;
	public static final int D_TABLE_EXECUTE_ERROR = -2;

	public static final int D_EXECUTE_NETWORk_ERROR = -3;

	// private static final String FUNTION_NAME = "funtionname";
	// private static final String TABLE_NAME = "tablename";
	private static final String PROJECT_NAME = "androidktr";

	// private static final String DB_SERVER_IP = "10.220.64.137";
	// private static final String DB_SERVER_IP = "14.49.24.102";
	private static final String DB_SERVER_IP = DEFINE.MW_HOST_IP;
	private static final int DB_UPDATE_SERVER_PORT = 8222;

	private MiddlewareConnector m_sapConnector;
	
	private ParameterList m_request;
	private ParameterList m_response;

	private ConnectInterface mConnectInterface;

	private Context mContext;

	// private String m_strHostIp;
	// private int m_nHostPort;
	// private String m_strSapIp;
	// private String m_strClientNo;
	// private String m_strSystemNo;
	// private String m_strSapUser;
	// private String m_strSapPw;
	// private int m_nTimeOut;
	// private int m_nExecuteTimeOut;

	public interface ConnectInterface {
		public void connectResponse(String FuntionName, String resultText,
                                    String MTYPE, int resulCode, TableModel tableModel);

		public void reDownloadDB(String newVersion);

	}

	/*
	 * ����
	 */
	public Connector(ConnectInterface connectInterface, Context context) {
		mConnectInterface = connectInterface;
		mContext = context;
		initConector();
	}

	public Context getConText(){
		return mContext;
	}
	public void setContext(Context context){
		mContext = context;
	}

	/**
	 * sap connector �ʱ�ȭ
	 */
	private void initConector() {
		m_sapConnector = null;
		m_request = null;
		m_response = null;

		m_sapConnector = new MiddlewareConnector();
		m_sapConnector
				.setMiddlewareInfo(DEFINE.MW_HOST_IP, DEFINE.MW_HOST_PORT);
		m_sapConnector.setSAPInfo(DEFINE.SAP_SERVER_IP, DEFINE.SAP_CLIENT_NO,
				DEFINE.SAP_SYSTEM_NO);
		//2014-04-14 KDH 보안때문에 삭제.
//		m_sapConnector.setUserInfo(DEFINE.SAP_USER_ID, DEFINE.SAP_USER_PW);
		m_sapConnector.setConnectTimeOut(DEFINE.MW_CONNECTION_TIMEOUT);
		m_sapConnector.setExecuteTimeOut(DEFINE.SAP_EXECUTE_TIMEOUT);

		// 암호화모듈 초기화.
		PhosCrypto.makeSecretKey4KTRENTAL();

		m_request = new ParameterList();
		m_response = new ParameterList();

		PrintLog.Print("", "INIT OK!!!!");
	}

	public void checkRequestParam() {
		if (m_request != null) {
			m_request = new ParameterList();
		}
	}


	public void checkResponseParam() {
		if (m_response != null) {
			m_response = new ParameterList();
		}
	}
	
	
	
	
	public void compareStartEndTime() throws ParseException
	{
		SharedPreferencesUtil sharedPreferencesUtil = SharedPreferencesUtil.getInstance();
		Date date = new Date();
	    SimpleDateFormat format = new SimpleDateFormat("HHmmss", Locale.KOREA);
	    
	    Date startDate = format.parse(format.format(date));
        if(sharedPreferencesUtil.getEndTime() == null)
        {
        	kog.e("Jonathaan", "timeee :: is null");
        	sharedPreferencesUtil.setEndTime(format.format(date));
        }
        Date endDate = format.parse(sharedPreferencesUtil.getEndTime()); 
		long totalTime = endDate.getTime() - startDate.getTime();
		kog.e("Jonathan", "timeeee :: " + totalTime/1000/60 + " endDate : " + format.format(endDate) + " startDate : " + format.format(startDate));

		if(Math.abs(totalTime/1000/60) > 60)
		{
			kog.e("Jonathan", "timeeee :: 앱 종료 시켜야 함.");
			
			Toast.makeText(mContext, "업무진행 중 앱 미사용 1시간이 지나서 앱을 종료 합니다.", Toast.LENGTH_LONG).show();

			((BaseActivity)mContext).finish();
			Intent launchHome = new Intent(Intent.ACTION_MAIN);
			launchHome.addCategory(Intent.CATEGORY_DEFAULT);
			launchHome.addCategory(Intent.CATEGORY_HOME);
			mContext.startActivity(launchHome);
		}
		
		
	}
	

	public void executeRFCAsyncTask(final String strRFCName, String tableName,
			ArrayList<String> resArr) throws Exception {
		
		compareStartEndTime();
		
		SharedPreferencesUtil sharedPreferencesUtil = SharedPreferencesUtil.getInstance();
		Date date = new Date();
	    SimpleDateFormat format = new SimpleDateFormat("HHmmss", Locale.KOREA);
		sharedPreferencesUtil.setEndTime(format.format(date));
		
		MWAsyncTask asyncTask = new MWAsyncTask(strRFCName, tableName, resArr);
		asyncTask.execute("");
	}

	public void executeRFCAsyncTask(final String strRFCName, String tableName,
			String param) throws Exception {
		
		compareStartEndTime();
		
		SharedPreferencesUtil sharedPreferencesUtil = SharedPreferencesUtil.getInstance();
		Date date = new Date();
	    SimpleDateFormat format = new SimpleDateFormat("HHmmss", Locale.KOREA);
		sharedPreferencesUtil.setEndTime(format.format(date));
		
		MWAsyncTask asyncTask = new MWAsyncTask(strRFCName, tableName, param);
		asyncTask.execute("");
	}

	public void executeRFCAsyncTask(final String strRFCName, String tableName)
			throws Exception {
		
		compareStartEndTime();

		SharedPreferencesUtil sharedPreferencesUtil = SharedPreferencesUtil.getInstance();
		Date date = new Date();
	    SimpleDateFormat format = new SimpleDateFormat("HHmmss", Locale.KOREA);
		sharedPreferencesUtil.setEndTime(format.format(date));
		
		MWAsyncTask asyncTask = new MWAsyncTask(strRFCName, tableName);
		asyncTask.execute("");
	}

	public void executeRFCAsyncTask(final String strRFCName, String[] tableNames)
			throws Exception {

		compareStartEndTime();
		
		SharedPreferencesUtil sharedPreferencesUtil = SharedPreferencesUtil.getInstance();
		Date date = new Date();
	    SimpleDateFormat format = new SimpleDateFormat("HHmmss", Locale.KOREA);
		sharedPreferencesUtil.setEndTime(format.format(date));

		MWAsyncTask asyncTask = new MWAsyncTask(strRFCName, tableNames);
		asyncTask.execute("");
	}
	
	public void executeRFCAsyncTask(final String strRFCName, String[] tableNames, ArrayList<String> resArr)
			throws Exception {
		
		compareStartEndTime();
		
		SharedPreferencesUtil sharedPreferencesUtil = SharedPreferencesUtil.getInstance();
		Date date = new Date();
	    SimpleDateFormat format = new SimpleDateFormat("HHmmss", Locale.KOREA);
		sharedPreferencesUtil.setEndTime(format.format(date));
		
		MWAsyncTask asyncTask = new MWAsyncTask(strRFCName, tableNames, resArr);
		asyncTask.execute("");
	}

	private class MWAsyncTask extends AsyncTask<String, Integer, ResponseData> {
		//2014-01-14 KDH 삽됐다..어싱크를 이렇게 쓰라고 있는게 아닌데..지멋대로해버렸네..-.-;
		private String mStrRFCName;
		private String mTableName;

		private String[] mTableNameArr;
		private String value;

		private ArrayList<String> mResponseArray;

		public MWAsyncTask(String strRFCName, String tableName, ArrayList<String> arr) {
			// TODO Auto-generated constructor stub
			mStrRFCName = strRFCName;
			mTableName = tableName;
			mResponseArray = arr;
			kog.e("Jonathan", "여기는 (1)");
		}

		public MWAsyncTask(String strRFCName, String tableName, String param) {
			// TODO Auto-generated constructor stub
			mStrRFCName = strRFCName;
			mTableName = tableName;
			value = param;
			kog.e("Jonathan", "여기는 (2)");
		}

		public MWAsyncTask(String strRFCName, String tableName) {
			// TODO Auto-generated constructor stub
			mStrRFCName = strRFCName;
			mTableName = tableName;
			kog.e("Jonathan", "여기는 (3)");
		}

		public MWAsyncTask(String strRFCName, String[] tableNames) {
			// TODO Auto-generated constructor stub
			mStrRFCName = strRFCName;
			mTableNameArr = tableNames;
			kog.e("Jonathan", "여기는 (4)");
		}
		
		public MWAsyncTask(String strRFCName, String[] tableNames, ArrayList<String> arr) {
			// TODO Auto-generated constructor stub
			mStrRFCName = strRFCName;
			mTableNameArr = tableNames;
			mResponseArray = arr;
			kog.e("Jonathan", "여기는 (4)");
		}

		@Override
		protected ResponseData doInBackground(String... params) {

			checkResponseParam();

			ResponseData responseData = null;

			if (!NetworkUtil.isNetwork(mContext)) {

				responseData = responseError(mStrRFCName,
						D_EXECUTE_NETWORk_ERROR, "");
				checkRequestParam();

				return responseData;
			}

			responseData = excute();

			return responseData;
		}

		// AsyncTask 종료. UI 쓰레드 시작.
		@Override
		protected void onPostExecute(ResponseData result) {

			super.onPostExecute(result);
			//2014-01-16 KDH 환장하것네 여기서 다돌리면 뭐가들어갔는지 어케아는감-_-;;미춰불것네
			//데이터 못돌려서 인터페이스 겁나 돌렸네-_- 걍 스태틱 쓰면될꺼를..타고들어가는사람은 가다가 지쳐서 쓰러지것네
			if (mConnectInterface != null) 
			{
				if (result != null){
					
					kog.e("Jonathan", "Jonathan들어오는 값 funname :: " + result.funName + "  mtype :: " + result.mtype + " strMsg :: " + result.strMsg + " what :: " + result.what + " tableModel :: " + result.talbleModel);
					if("ZMO_1010_RD02".equals(result.funName))
					{
						String MTYPE = result.mtype;
						TableModel tableModel = result.talbleModel;
						final Activity activity = (Activity) mContext;
						
						if (MTYPE == null || !MTYPE.equals("S")) {

							 final EventPopupC epc = new EventPopupC(mContext);
				             Button btn_confirm = (Button)epc.findViewById(R.id.btn_ok);
				             btn_confirm.setOnClickListener(new OnClickListener() {

									@Override
									public void onClick(View arg0) {
										// TODO Auto-generated method stub
										try {
											epc.dismiss();
										} catch (Exception e){
											e.printStackTrace();
										}
										android.os.Process.killProcess(android.os.Process.myPid());
										System.exit(0);

									}
								});
							try {
								if(mContext != null && !((Activity) mContext).isFinishing()) {
									epc.show("세션정보 조회에 실패했습니다. 앱을 종료 합니다.");
								}
							} catch (Exception e){
								e.printStackTrace();
							}
						}
						
						HashMap<String, String> ES_LIST = new HashMap<String, String>();
						if(tableModel != null)
						{
							ES_LIST = tableModel.getStruct("ES_LIST");
							if(ES_LIST != null)
							{

								String SES_ID = ES_LIST.get("SES_ID");
								String LOG_CK = ES_LIST.get("LOG_CK");
								String SVR_IP = ES_LIST.get("SVR_IP");

								kog.e("Joanthan", "SES_ID :: " + SES_ID + "   DEFINE.session :: " + DEFINE.SESSION + " SVR_IP :: " + SVR_IP);

								if(!SVR_IP.contains("."))	// 모바일에서 접근할때만 이걸 탄다. 웹에서 접근시 중복로그인 체크 안함
								{
									if("X".equals(ES_LIST.get("LOG_CK")) || "LINK".equals(ES_LIST.get("SES_ID")))
									{
										kog.e("Joanthan", "LOG_CK :: " + ES_LIST.get("LOG_CK") + "   SES_ID :: " + ES_LIST.get("SES_ID"));
									}
									else
									{
										if(!activity.getLocalClassName().contains("LoginActivity"))
										{
											if(!SES_ID.equals(DEFINE.SESSION) || SES_ID == "" || SES_ID.isEmpty())
											{
												final EventPopupC epc = new EventPopupC(mContext);
												Button btn_confirm = (Button)epc.findViewById(R.id.btn_ok);
												btn_confirm.setOnClickListener(new OnClickListener() {

													@Override
													public void onClick(View arg0) {
														// TODO Auto-generated method stub
														try {
															epc.dismiss();
														} catch (Exception e){
															e.printStackTrace();
														}
//													finish();
														android.os.Process.killProcess(android.os.Process.myPid());
														System.exit(0);

													}
												});
												try {
													if(mContext != null && !((Activity) mContext).isFinishing()) {
														epc.show("중복로그인 되어 마지막 사용자가 로그인 세션을 강제 종료시켰습니다.");
													}
												} catch (Exception e){
													e.printStackTrace();
												}
											}


//									Set<String> set = ES_LIST.keySet();
//									Iterator<String> it = set.iterator();
//									String key;
//
//
//									kog.e("Jonathan", "context Name :: " + activity.getLocalClassName());
//
//									while (it.hasNext()) {
//										key = it.next();
//										kog.e("Jonathan", "OtherAc ES_LIST key ===  " + key	+ "    value  === " + ES_LIST.get(key));
//									}
										}
									}
								}

							}
						}

					}
					
					
					mConnectInterface.connectResponse(result.funName, result.strMsg, result.mtype, result.what, result.talbleModel);
					
					
				}

				kog.e("KDH", "result.funName  = "+result.funName);
				
			}
		}

		private ResponseData excute() {

			ResponseData responseData = null;

			checkResponseParam();

			//2014-01-16 KDH 환장하게 짜놨네 ㅡ,.ㅡ;; 어차피 전역으로 받을꺼면 뭣하러 이렇게 했는지원 아놔 ㅡ,.ㅡ;
			// 여기서 데이터 테스트 가능함. 
			try {
				
				PrintLog.Print(TAG, "executeRFCAsyncTask " + mStrRFCName);
				PrintLog.Print(TAG, "SAP_CLIENT_NO " + DEFINE.SAP_CLIENT_NO);
//				m_response = m_sapConnector.execute(ICode.CREQ_ALL_UTF8,
//						mStrRFCName, m_request, DEFINE.SAP_USER_ID);

				PrintLog.Print(TAG, "executeRFC End   :: " + mStrRFCName);
				
				
				
				m_response = m_sapConnector.execute(ICode.CREQ_ALL_UTF8, mStrRFCName, m_request, "");


				// 로그 추가
				//-------------------------
				if(DEFINE.DEBUG_MODE)
				{
					System.out.println("EXPORT----------------------------");
					ArrayList<Column> columns = m_response.getScalar();
					if(columns != null) {
						for (Column column : columns) {
							System.out.println(column.getName() + ":" + column.getValue());
							System.out.println(column.getName() + ":" + PhosCrypto.decrypt(column.getValue()));
						}
					}
					System.out.println("STRUCTURE----------------------------");
					ArrayList<Structure> structures = m_response.getAllStructure();
					if(structures != null) {
						for (Structure struct : structures) {

							System.out.println("STRUCTRUE " + struct.getName() + "......");
							ArrayList<Column> cols = struct.getScalarData();
							for (Column column : cols) {
								System.out.println(column.getName() + ":" + column.getValue());
								System.out.println(column.getName() + ":" + PhosCrypto.decrypt(column.getValue()));
							}
						}
					}

					System.out.println("TABLE----------------------------------");
					ArrayList<Table> tables = m_response.getAllTable();
					if(tables != null) {
						for (Table table : tables) {
							System.out.println("TABLE " + table.getName() + "......");
							ArrayList<Row> rows = table.getRows();
							for (Row row : rows) {
								System.out.println("------------------------------------");
								ArrayList<Column> cols = row.getColumns();
								for (Column column : cols) {
									System.out.println(column.getName() + ":" + column.getValue());
									System.out.println(column.getName() + ":" + PhosCrypto.decrypt(column.getValue()));
								}
							}
						}
					}
				}

				
				if (mTableName != null || mTableNameArr != null) {
					
					responseData = responseTable(mStrRFCName, mTableName,
							mTableNameArr, DEFINE.LOGIN_TABLE_NAME,
							D_TABLE_EXECUTE_SUCCESS);
					kog.e("Jonathan", "여기는 Connector.ResponseData excute() mTableName :: " + mTableName);
				} else {
					responseData = responseDefault(mStrRFCName,
							D_EXECUTE_SUCCESS, value);
				}
				
				kog.e("Jonathan", "funName = "+mStrRFCName+"   strMsg "+mTableName+ " strRes = "+mTableNameArr);
				

			} catch (Exception e) {
				e.printStackTrace();
				responseData = responseError(mStrRFCName, D_EXECUTE_ERROR,
						e.getMessage());
			}

			checkRequestParam();

			return responseData;
		}

		private ResponseData responseTable(String funName, String tableName,
				String[] tableNames, String structTableName, int what) {

			ResponseData responseData;

			String strRes = "";
			String strMsg = "";

			TableModel talbleModel = null;

			ArrayList<HashMap<String, String>> resTableArr = null;
			
			HashMap<String, HashMap<String, String>> resStructMap = new HashMap<String, HashMap<String, String>>();

			ArrayList<Structure> allStruct = m_response.getAllStructure();
			
			kog.e("Jonathan", "Jonathan1 :: " + allStruct.size() );
			
			String E = "";
			
			Structure structure;
			if (allStruct != null) {
				for (int i = 0; i < allStruct.size(); i++) {
					structure = allStruct.get(i);
					String structureName = structure.getName();
					kog.e("Jonthan", "Jonathan :: 12 : " + structureName);
					HashMap<String, String> struct = getStructure(structureName, true);
					kog.e("Jonathan", "여기는 Connector.responseTable");
					kog.e("Jonathan", "여기는 Connector.responseTable structureName :: " + structureName);
					
					
					resStructMap.put(structure.getName(), struct);
					
					
					
					
					if (structureName.equals("ES_RETURN")) {
						strRes = struct.get("MTYPE");
						strMsg = struct.get("MESSAGE");
					}
					
					

				}
			}

			HashMap<String, String> map = new HashMap<String, String>();

			if (mResponseArray != null) {
				for (String strKey : mResponseArray) {
					String val = getParameter(strKey);
					kog.e("KDH", "strKey = "+strKey+"    val = "+val);
					map.put(strKey, val);

				}
			}

			// HashMap<String, String> ES_RETURN = getStructure("ES_RETURN",
			// true);

			//2014-01-20 KDH ParameterList 리스트 구조를 통째로 추가.실제로 받은 데이터로 컬럼구성.
			//이거 찾다가 하루종일 걸리네 ㅡ.,ㅡ;유지보수 진행은 쉽지않네-ㅇ- 
			ArrayList<Table> allTable = m_response.getAllTable();
			if (allTable != null) {
				int allTableSize = allTable.size();

				String[] reTableNames = new String[allTableSize];

				if (allTableSize == 1) {
					tableName = allTable.get(0).getName();
					PrintLog.Print(TAG, tableName);
					resTableArr = getTable(tableName, false);

					PrintLog.Print("KDH", tableName);
					talbleModel = new TableModel(tableName, resTableArr, resStructMap, map, value);

				} else {

					HashMap<String, ArrayList<HashMap<String, String>>> tableArrayMap = new HashMap<String, ArrayList<HashMap<String, String>>>();
					for (int i = 0; i < allTableSize; i++) {
						reTableNames[i] = allTable.get(i).getName();

						resTableArr = getTable(reTableNames[i], false);
						PrintLog.Print(TAG, reTableNames[i] + "  "
								+ resTableArr.size());
						tableArrayMap.put(reTableNames[i], resTableArr);
					}
					talbleModel = new TableModel(tableNames, tableArrayMap,
							resStructMap, map, value);
				}
			} else {
				talbleModel = new TableModel(tableNames, null, resStructMap,
						map, value);
			}

			PrintLog.Print(TAG, "Response funName = " + funName);
			PrintLog.Print(TAG, "Response message = " + strMsg);
			
			responseData = new ResponseData(funName, strMsg, strRes, what,
					talbleModel);

			return responseData;
		}

		private ResponseData responseDefault(String funName, int what,
				String param) {

			ResponseData responseData;

			HashMap<String, String> resStruct = null;
			String strMsg = "";

			resStruct = getStructure("ES_RETURN", true);
			kog.e("Jonathan", "여기는 Connector.responseDefault");
			String strRes = resStruct.get("MTYPE");
			strMsg = resStruct.get("MESSAGE");

			HashMap<String, String> map = new HashMap<String, String>();

			if (mResponseArray != null) {
				for (String strKey : mResponseArray) {
					String val = getParameter(strKey);

					map.put(strKey, val);

				}
			}

			TableModel model = new TableModel(param, map);

			responseData = new ResponseData(funName, strMsg, strRes, what,
					model);

			return responseData;
		}

		private ResponseData responseError(String funName, int what,
				String strMsg) {

			ResponseData responseData;

			HashMap<String, String> resStruct = null;

			resStruct = getStructure("ES_RETURN", true);
			kog.e("Jonathan", "여기는 Connector.responseError");

			String strRes = null;

			if (resStruct != null) {

				strRes = resStruct.get("MTYPE");
				strMsg = resStruct.get("MESSAGE");
			}
			if (strMsg == null || strMsg.equals("")) {
				if (D_EXECUTE_NETWORk_ERROR == what) {
					strMsg = "네트워크에 연결되어 있지 않습니다.";
					strRes = "-1";
				}
			}

			responseData = new ResponseData(funName, strMsg, strRes, what, null);

			return responseData;
		}
	}

	public void setParameter(String strKey, String strValue) {
		ConnectorUtil.setParameter(strKey, strValue, m_request);
	}

    public void setParameter_nonEncrypt(String strKey, String strValue) {
        ConnectorUtil.setParameterNonEnCrypt(strKey, strValue, m_request);
    }

	public void setStructure(String strStructureName,
			HashMap<String, String> aStructure) {
		ConnectorUtil.setStructure(strStructureName, aStructure, m_request);
	}

	public void setTable(String strTableName,
			ArrayList<HashMap<String, String>> aTable) {
		ConnectorUtil.setTable(strTableName, aTable, m_request);
	}

	public String getParameter(String strKey) {
		String strValue = "";
		strValue = ConnectorUtil.getParameter(strKey, m_response);

		return strValue;
	}

	public HashMap<String, String> getStructure(String strName,
			boolean isDecrypt) {
		HashMap<String, String> resMap = new HashMap<String, String>();
		kog.e("Jonathan", "여기는 Connector.getStructure2");
		resMap = ConnectorUtil.getStructure(strName, m_response, isDecrypt);

		return resMap;
	}

	public ArrayList<HashMap<String, String>> getTable(String strName,
			boolean isDecrypt) {
		ArrayList<HashMap<String, String>> resArray = new ArrayList<HashMap<String, String>>();
		resArray = ConnectorUtil.getTable(strName, m_response, isDecrypt);

		return resArray;
	}

	public void downloadDB(final String version, final String directory) {
		Thread thread = new Thread(new Runnable() {

			@Override
			public void run() {

				try {
					// TODO Auto-generated method stub
					m_sapConnector.setVersionAndProjectName(version,
							PROJECT_NAME);
					m_sapConnector.setUpdateServerInfo(DB_SERVER_IP,
							DB_UPDATE_SERVER_PORT, directory);
					String newVerString = m_sapConnector.smartUpdate();

					if (!newVerString.equals("-1"))
						SharedPreferencesUtil.getInstance().setVersion(
								newVerString);

					if (mConnectInterface != null)
						mConnectInterface.reDownloadDB(newVerString);

				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});

		thread.start();

	}

	private class ResponseData {
		String strMsg = "";
		String mtype = "";

		TableModel talbleModel = null;

		String funName;
		int what;

		public ResponseData(String FuntionName, String resultText,
				String resultMTYPE, int resulCode, TableModel _tableModel) {
			funName = FuntionName;
			strMsg = resultText;
			what = resulCode;
			talbleModel = _tableModel;
			mtype = resultMTYPE;
			if (resultMTYPE == null) {
				mtype = "E";
			}
		}
	}

}