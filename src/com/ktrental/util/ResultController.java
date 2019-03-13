package com.ktrental.util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.media.MediaPlayer.OnCompletionListener;
import android.text.TextUtils;
import android.util.Log;

import com.ktrental.activity.LoginActivity;
import com.ktrental.cm.connect.ConnectController;
import com.ktrental.cm.connect.Connector.ConnectInterface;
import com.ktrental.cm.db.DbAsyncTask;
import com.ktrental.cm.db.DbAsyncTask.DbAsyncResLintener;
import com.ktrental.common.AppSt;
import com.ktrental.common.DEFINE;
import com.ktrental.common.KtRentalApplication;
import com.ktrental.common.Result_Send;
import com.ktrental.fragment.MaintenanceResultFragment;
import com.ktrental.model.DbQueryModel;
import com.ktrental.model.LoginModel;
import com.ktrental.model.O_ITAB1;
import com.ktrental.model.TableModel;

//2014-01-16 KDH 결과등록 -_-/ 이건뭐..끝판왕이네..
public class ResultController implements DbAsyncResLintener, ConnectInterface {

	private HashMap<String, ArrayList<HashMap<String, String>>> mBaseMap = new HashMap<String, ArrayList<HashMap<String, String>>>();
	private HashMap<String, ArrayList<HashMap<String, String>>> mStockMap = new HashMap<String, ArrayList<HashMap<String, String>>>();
	private HashMap<String, ArrayList<HashMap<String, String>>> mImageMap = new HashMap<String, ArrayList<HashMap<String, String>>>();
	
//	private ArrayList<HashMap<String, String>> mImageMap = new ArrayList<HashMap<String, String>>();
	

//	private HashMap<String, Integer> mERFMGMap = new HashMap<String, Integer>();
	private HashMap<String, String> mCERMRMap = new HashMap<String, String>();
	private ArrayList<String> mMATNR_MAP = new ArrayList<String>();
	private ArrayList<Integer> mERFMG_MAP = new ArrayList<Integer>();

	private Context mContext;

	private String mLastDRIVN = "";
	private OnResultCompleate mResultCompleate;

	private ConnectController mConnectController;

	private ArrayList<String> aufnrList = new ArrayList<String>();
	private ArrayList<String> invnrList = new ArrayList<String>();
	private String mResultText = "";

	private boolean mUpdateStockFlag = false;

	public interface OnResultCompleate {
		void onResultComplete(String message);
	}

	public ResultController(Context context, OnResultCompleate onResultCompleate) {
		mContext = context;
		mResultCompleate = onResultCompleate;
		mConnectController = new ConnectController(this, context);
	}

	public void queryBaseGroup(boolean updateStockFlag) {
		kog.e("KDH", updateStockFlag+" = 결과등록하고 여기서 뒤지는 것 같다.2");
		
		mUpdateStockFlag = updateStockFlag;
		String[] _whereArgs = null;
		String[] _whereCause = null;

		String[] colums = null;

		DbQueryModel dbQueryModel = new DbQueryModel(
				DEFINE.REPAIR_RESULT_BASE_TABLE_NAME, _whereCause, _whereArgs,
				colums);

		DbAsyncTask dbAsyncTask = new DbAsyncTask("queryBaseGroup", mContext,
				this, dbQueryModel);
		dbAsyncTask.execute(DbAsyncTask.DB_SELECT);
	}
	//2014-01-22 KDH 결과등록 현재 진행 건만 전송함. 나머지는 로그인 할때 미전송 처리
	public void queryBaseGroupKDH(boolean updateStockFlag) {
		
		kog.e("Jonathan", "checking Complete queryBaseGroupKDH ");
		
		mUpdateStockFlag = updateStockFlag;
		
		String[] _whereArgs = null;
		String[] _whereCause = null;

		String[] colums = null;

		DbQueryModel dbQueryModel = new DbQueryModel(
				DEFINE.REPAIR_RESULT_BASE_TABLE_NAME, _whereCause, _whereArgs,
				colums);

		DbAsyncTask dbAsyncTask = new DbAsyncTask(AppSt.KDH_SEND_RESULT, mContext,
				this, dbQueryModel);
		dbAsyncTask.execute(DbAsyncTask.DB_SELECT);
	}
	

	private void queryStock(String funName) {
		String[] _whereArgs = null;
		String[] _whereCause = null;

		String[] colums = null;

		DbQueryModel dbQueryModel = new DbQueryModel(
				DEFINE.REPAIR_RESULT_STOCK_TABLE_NAME, _whereCause, _whereArgs,
				colums);

		DbAsyncTask dbAsyncTask = new DbAsyncTask(funName, mContext, this,
				dbQueryModel);
		dbAsyncTask.execute(DbAsyncTask.DB_SELECT);
	}

	private void queryStock(String aufnr, String funName) {
		String[] _whereArgs = { aufnr };
		String[] _whereCause = { "AUFNR" };

		String[] colums = null;

		DbQueryModel dbQueryModel = new DbQueryModel(
				DEFINE.REPAIR_RESULT_STOCK_TABLE_NAME, _whereCause, _whereArgs,
				colums);

		DbAsyncTask dbAsyncTask = new DbAsyncTask(funName, mContext, this,
				dbQueryModel);
		dbAsyncTask.execute(DbAsyncTask.DB_SELECT);
	}

	private void queryImage() {

		String[] _whereArgs = null;
		String[] _whereCause = null;

		String[] colums = null;

		DbQueryModel dbQueryModel = new DbQueryModel(
				DEFINE.REPAIR_RESULT_IMAGE_TABLE_NAME, _whereCause, _whereArgs,
				colums);

		DbAsyncTask dbAsyncTask = new DbAsyncTask("queryImage", mContext, this,
				dbQueryModel);
		dbAsyncTask.execute(DbAsyncTask.DB_SELECT);
	}

	@Override
	public void onCompleteDB(String funName, int type, Cursor cursor,
			String tableName) {

		kog.e("KDH", "funName = "+funName +"  type = "+type);
		
		if (funName.equals("queryBaseGroup") || AppSt.KDH_SEND_RESULT.equals(funName)) {

			if (cursor == null || cursor.getCount() < 1) {
				if (mContext != null)
					// Toast.makeText(mContext, "onResultComplete",
					// Toast.LENGTH_SHORT).show();
					mResultCompleate.onResultComplete("0");
				return;
			}

			// Log.d("", "");

			if(AppSt.KDH_SEND_RESULT.equals(funName))
			{
				kog.e("Jonathan", "onCompleteDB if ");
				responseGroupCount(cursor);
			}
			else
			{
				kog.e("Jonathan", "onCompleteDB else ");
				responseGroup(cursor);
			}
			
			// Iterator<String> itMap = mBaseMap.keySet().iterator();
			// while (itMap.hasNext()) {
			// String strKey = itMap.next();
			// queryStock("send");
			// }

			queryStock("send");

		} else if (funName.equals("send")) {
			if (cursor == null) {
				// if (mContext != null)
				// // Toast.makeText(mContext, "onResultComplete",
				// // Toast.LENGTH_SHORT).show();
				// mResultCompleate.onResultComplete("");
				// return;

			} else {
				responseStock(cursor);
			}
			queryImage();
			// Iterator<String> itMap = mStockMap.keySet().iterator();
			//
			// String backKey = "";
			//
			// while (itMap.hasNext()) {
			// String strKey = itMap.next();
			// if (!strKey.equals(backKey))
			// queryImage();
			// backKey = strKey;
			// }

		} else if (funName.equals("queryImage")) {

			if (cursor == null || cursor.getCount() < 1) {
				if (mContext != null)
					// Toast.makeText(mContext, "onResultComplete",
					// Toast.LENGTH_SHORT).show();
					mResultCompleate.onResultComplete("");
				return;
			}
			responseImage(cursor);
		} else if (funName.equals("recovery")) {
			if (cursor == null || cursor.getCount() < 1) {
				// if (mContext != null)
				// Toast.makeText(mContext, "onResultComplete",
				// Toast.LENGTH_SHORT).show();
				mResultCompleate.onResultComplete("");
				return;

			}
			responseRecovery(cursor);

		} else if (funName.equals("queryRealStock")) {
			if (cursor == null || cursor.getCount() < 1) {
				// if (mContext != null)
				// Toast.makeText(mContext, "onResultComplete",
				// Toast.LENGTH_SHORT).show();
				mResultCompleate.onResultComplete("");
				return;

			}

			responseRealStock(cursor);
		} else if (funName.equals("updateStockComplete")) {
			if (mStockMap.isEmpty()) {
				if (mContext != null)
					// Toast.makeText(mContext, "onResultComplete",
					// Toast.LENGTH_SHORT).show();
					mResultCompleate.onResultComplete("");
				return;
			}
		}
	}
	 
	private void responseGroup(Cursor cursor) {
		cursor.moveToFirst();

		ArrayList<HashMap<String, String>> array = null;

		String lastDRIVN = "";

		array = new ArrayList<HashMap<String, String>>();
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) 
		{
			HashMap<String, String> map = new HashMap<String, String>();
			for (int i = 0; i < cursor.getColumnCount(); i++) {
				//2014-01-21 KDH 아놔-_-답안나오네..-.-;
				kog.e("KDH", "cursor.getColumnName(i) = "+cursor.getColumnName(i)
						+"  cursor.getString(i) = "+cursor.getString(i));
//				if(!"MESSAGE".equals(cursor.getColumnName(i)))
				{
					map.put(cursor.getColumnName(i), cursor.getString(i));
				}
			}
			
			map.remove("DRIVN");
			String invnr = cursor.getString(cursor.getColumnIndex("INVNR"));
			String aufnr = cursor.getString(cursor.getColumnIndex("AUFNR"));
			
			
			aufnrList.add(aufnr);
			array.add(map);
			invnrList.add(invnr);

			lastDRIVN = invnr;
			cursor.moveToNext();
		}
		kog.e("Jonathan", "responseGroup");
		mBaseMap.put(lastDRIVN, array);
		cursor.close();
	}
	
	private void responseGroupCount(Cursor cursor) {
		

		ArrayList<HashMap<String, String>> array = null;

		String lastDRIVN = "";

		array = new ArrayList<HashMap<String, String>>();
		
		//2014-01-21 KDH 환장하것네 컬럼을..이렇게쓰나?
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) 
		{
			String count = cursor.getString(cursor.getColumnIndex("COUNT"));
			kog.e("KDH", "count = "+count);
			if(AppSt.KDH_SEND_COUNT_ZERO.equals(count))
			{
				HashMap<String, String> map = new HashMap<String, String>();
				for (int i = 0; i < cursor.getColumnCount(); i++) {
					
					kog.e("KDH", "responseGroupCount column = "+cursor.getColumnName(i) +"  responseGroupCount getstring = "+cursor.getString(i));
					map.put(cursor.getColumnName(i), cursor.getString(i));
				}
				
				map.remove("DRIVN");
				String aufnr = cursor.getString(cursor.getColumnIndex("AUFNR"));
				String invnr = cursor.getString(cursor.getColumnIndex("INVNR"));
				
				//Jonathan 150529 if 문 지워주면 원복가능 지금까지 안된 것들 빼고 마지막 것만 보낸다. 최신것.
				if(cursor.isLast())
				{
					kog.e("Jonathan", "responseGroupCount last aufnr :: " +aufnr + " " + invnr); 
					aufnrList.add(aufnr);
					array.add(map);
					invnrList.add(invnr);
					
				}

				lastDRIVN = invnr;
			}
			cursor.moveToNext();
		}
		
		kog.e("Jonathan", "responseGroupCount");
		mBaseMap.put(lastDRIVN, array);
		cursor.close();
	}
	

	private void responseStock(Cursor cursor) {
		cursor.moveToFirst();

		ArrayList<HashMap<String, String>> array = null;
		array = new ArrayList<HashMap<String, String>>();
		String lastDRIVN = "";

		while (!cursor.isAfterLast()) {

			HashMap<String, String> map = new HashMap<String, String>();
			for (int i = 0; i < cursor.getColumnCount(); i++) {
				map.put(cursor.getColumnName(i), cursor.getString(i));
			}

			map.remove("DRIVN");
			// myung 20131216 DELETE I_TAB2 의 INVNR(고객차량 번호) 필드 추가 필요.
			// map.remove("INVNR");

			String INVNR = cursor.getString(cursor.getColumnIndex("INVNR"));
			array.add(map);

			lastDRIVN = INVNR;

			cursor.moveToNext();
		}

		mStockMap.put(lastDRIVN, array);

		cursor.close();
	}

	private void responseImage(Cursor cursor) {

		cursor.moveToFirst();

		ArrayList<HashMap<String, String>> array = null;
		array = new ArrayList<HashMap<String, String>>();
		String aufnr = "";
		String lastDRIVN = "";
		while (!cursor.isAfterLast()) 
		{
			
			HashMap<String, String> map = new HashMap<String, String>();
			for (int i = 0; i < cursor.getColumnCount(); i++) {
				map.put(cursor.getColumnName(i), cursor.getString(i));
			}
			map.remove("DRIVN");
			map.remove("INVNR");
			// array.add(map);

			String INVNR = cursor.getString(cursor.getColumnIndex("INVNR"));
			// if (!lastDRIVN.equals(INVNR)) {
			// array = new ArrayList<HashMap<String, String>>();
			// array.add(map);
			// mImageMap.put(INVNR, array);
			// }
			
//			mImageMap.put(INVNR, array);
			
			kog.e("KDH", "MAP IMG INVNR = "+INVNR);
			array.add(map);
			lastDRIVN = INVNR;
			cursor.moveToNext();
		}
		
		mImageMap.put(lastDRIVN, array);
		kog.e("KDH" ,"mImageMap = "+mImageMap.size());
		cursor.close();

		sendResult();

	}

	private String sendResult() {
		Log.e("sendResult", "sendResult");
		String reAUFNR = "";

		if (mConnectController != null) {

			Iterator<String> itMap = mBaseMap.keySet().iterator();
			
			kog.e("Jonathan", "sendResult :: mBaseMap" + String.valueOf(mBaseMap.size()));
					

			while (itMap.hasNext()) {
				String strKey = itMap.next();
				ArrayList<HashMap<String, String>> baseArr = mBaseMap
						.get(strKey);
				
				
				kog.e("Jonathan", "sendResult :: while" + String.valueOf(baseArr.size()));
				
				String GUBUN = "";

				// myung 20131216 ADD I_TAB1 의 정비오더&사인이미지명과 I_TAB2의
				// 정비오더&사인이미지명을 비교하여 다른게 있으면 다른 정비오더 데이터 전송하지 말것.
				ArrayList<HashMap<String, String>> tempStockArr = new ArrayList<HashMap<String, String>>();
				ArrayList<HashMap<String, String>> tempImageArr = new ArrayList<HashMap<String, String>>();

				for (HashMap<String, String> map : baseArr) {
					
					kog.e("Jonathan", "sendResult :: baseArr" + String.valueOf(baseArr.size()));
					
					GUBUN = map.get("GUBUN");
					String aufnr = map.get("AUFNR");
					// map.remove("AUFNR");
					if ("E".equals(GUBUN)) {
						map.remove("AUFNR");
						map.put("AUFNR", " ");
						map.remove("CEMER");
						map.put("CEMER", "X");

					}
					
					map.remove(AppSt.KDH_SEND_MSG);
					map.remove("COUNT");

					mCERMRMap.put(aufnr, map.get("CEMER"));

					// myung 20131216 ADD I_TAB1 의 정비오더&사인이미지명과 I_TAB2의
					// 정비오더&사인이미지명을 비교하여 다른게 있으면 다른 정비오더 데이터 전송하지 말것.
					Iterator<String> itMap1 = mStockMap.keySet().iterator();
					while (itMap1.hasNext()) {
						String strKey1 = itMap1.next();
						ArrayList<HashMap<String, String>> stockArr = mStockMap
								.get(strKey1);

						for (HashMap<String, String> map1 : stockArr) {
							// Log.e("map1 AUFNR size",
							// ""+map1.get("AUFNR").length());
							// Log.e("map AUFNR size",
							// ""+map.get("AUFNR").length());
							// 2014.101.08	ypkim
							if (map1.get("AUFNR").equals(map.get("AUFNR")) &&
									map1.get("INVNR").equals(map.get("INVNR"))) {
								tempStockArr.add(map1);
							}
						}
					}

					
					Iterator<String> itMap2 = mImageMap.keySet().iterator();
					while (itMap2.hasNext()) 
					{
						String strKey2 = itMap2.next();
						kog.e("KDH", "tempImageArr strKey2 = "+strKey2);
						ArrayList<HashMap<String, String>> imageArr = mImageMap
								.get(strKey2);

						for (HashMap<String, String> map2 : imageArr) 
						{
							if (map2.get("SIGN_T").equals(map.get("SIGN_T"))) 
							{
								//2014-01-23 KDH 테러당했다.
//								if (tempImageArr.size() == 0)
								{
									Log.e("SEND SIGN SIZE", ""+map2.get("SIGN").length());
									if(!tempImageArr.contains(map2)){
										tempImageArr.add(map2);
									}
								}
							}
						}
					}
					
				}

				kog.e("KDH", ""+tempImageArr.size());
				// myung 20131216 UPDATE I_TAB1 의 정비오더&사인이미지명과 I_TAB2의
				// 정비오더&사인이미지명을 비교하여 다른게 있으면 다른 정비오더 데이터 전송하지 말것.
				
				mConnectController.sendMaintenance(mBaseMap.get(strKey),
						tempStockArr, tempImageArr, GUBUN, strKey);
				
				// mConnectController.sendMaintenance(mBaseMap.get(strKey),
				// mStockMap.get(strKey), mImageMap.get(strKey), GUBUN,
				// strKey);

				mBaseMap.remove(strKey);
				// mStockMap.remove(strKey);
				mImageMap.remove(strKey);
				reAUFNR = strKey;
				break;
			}

		}

		return reAUFNR;
	}

	@Override
	public void connectResponse(String FuntionName, final String resultText,
			String MTYPE, int resulCode, TableModel tableModel) {
		// TODO Auto-generated method stub
		kog.e("KDH", "connectResponse = "+FuntionName);
		kog.e("Jonathan", "connectResponse MTYPE :: " + MTYPE);
		if (MTYPE != null) 
		{
			if (MTYPE.equals("S") || MTYPE.equals(" ")) { // 결광등록 성공.

				if (tableModel != null) {
					// String aufnr = tableModel.getValue();
					
					for (int i = 0; i < aufnrList.size(); i++) {
						String aufnr = aufnrList.get(i);
						kog.e("Jonathan", "updateComplete ?? " + aufnr );
						updateComplete(aufnr);
					}
					for (int i = 0; i < invnrList.size(); i++) {

						String INVNR = invnrList.get(i);

						deleteResultDataBase(
								DEFINE.REPAIR_RESULT_BASE_TABLE_NAME, INVNR);
						deleteResultDataBase(
								DEFINE.REPAIR_RESULT_STOCK_TABLE_NAME, INVNR);
						deleteResultDataBase(
								DEFINE.REPAIR_RESULT_IMAGE_TABLE_NAME, INVNR);

					}
					updateStock();

					// sendResult();
					boolean flag = true;
					for (int i = 0; i < aufnrList.size(); i++) {
						String aufnr = aufnrList.get(i);
						if (mCERMRMap.containsKey(aufnr)) {
							String cemer = mCERMRMap.get(aufnr);
							if (cemer.equals("X")) {
								flag = false;

								break;
							}
						}
					}
					if (flag) {
						if (mBaseMap.size() == 0)
							mResultCompleate.onResultComplete("" + resultText);
					} else {
						mResultText = resultText;
						repairPlanWork();
					}
				}
			}
			else 
			{
				//Jonathan 15.05.28
				deleteRowResultDataAnotherMonth();
				
				ArrayList<String> removeArray = new ArrayList<String>();
				ArrayList<KDH_MSG> _data = new ArrayList<KDH_MSG>();
				
				if (tableModel != null) 
				{
					kog.e("Jonathan", "updateComplete 1" );
					ArrayList<HashMap<String, String>> tableArray = tableModel
							.getTableArray("O_ITAB1");
					
					for (HashMap<String, String> hashMap : tableArray) 
					{
						map_log(hashMap);
						
						String mtype = hashMap.get("MTYPE");
						String aufnr = hashMap.get("AUFNR");
						String INVNR = hashMap.get("INVNR");
						
						// Log.d("HONG", "mtype = " + mtype);
						if (mtype.equals("S")) 
						{
							kog.e("Jonathan", "updateComplete 11 " + INVNR );
							removeArray.add(INVNR);

							for (String _aufnr : aufnrList) {
								kog.e("Jonathan", "updateComplete 12 aufnr :: " + aufnr );
								if (aufnr.equals(_aufnr)) {
//									 aufnrList.remove(_aufnr);
									mStockMap.remove(aufnr);
									break;
								}
							}
						}
						else
						{
							kog.e("Jonathan", "updateComplete 13" );
							KDH_MSG data = new KDH_MSG();
							data.INVNR = hashMap.get("INVNR");
							data.AUFNR = hashMap.get("AUFNR");
							data.msg = hashMap.get("MESSAGE");
							_data.add(data);
						}
					}
					//Jonathan 150528 주석처리함
//					for (int i = 0; i < aufnrList.size(); i++) {
//						String aufnr = aufnrList.get(i);
//						kog.e("Jonathan", "updateComplete 14" );
//						updateComplete(aufnr);
//					}

					//Jonathan 150528 주석처리함
					// sendResult();
					boolean flag = true;
					for (int i = 0; i < aufnrList.size(); i++) {
						String aufnr = aufnrList.get(i);
						if (mCERMRMap.containsKey(aufnr)) {
							String cemer = mCERMRMap.get(aufnr);
							if (cemer.equals("X")) {
								flag = false;

								break;
							}
						}
					}
					for (int i = 0; i < removeArray.size(); i++) {
						
						kog.e("Jonathan", "미전송 내역 에러남 :: ");

						String INVNR = removeArray.get(i);

						deleteResultDataBase(
								DEFINE.REPAIR_RESULT_BASE_TABLE_NAME, INVNR);
						deleteResultDataBase(
								DEFINE.REPAIR_RESULT_STOCK_TABLE_NAME, INVNR);
						deleteResultDataBase(
								DEFINE.REPAIR_RESULT_IMAGE_TABLE_NAME, INVNR);

					}

//					updateCount(_data, resultText);
					
					kog.e("Jonathan", "resultText 에러남 :: " + resultText);
					mResultCompleate.onResultComplete("에러발생\n관리자에게 문의하세요.");
					
					
				} 
				else 
				{
					kog.e("Jonathan", "updateComplete 2" );
					for (int i = 0; i < aufnrList.size(); i++) {
						String aufnr = aufnrList.get(i);
						kog.e("Jonathan", "updateComplete ?? 1" );
						updateComplete(aufnr);
					}

					// sendResult();
					boolean flag = true;
					for (int i = 0; i < aufnrList.size(); i++) {
						String aufnr = aufnrList.get(i);
						if (mCERMRMap.containsKey(aufnr)) {
							String cemer = mCERMRMap.get(aufnr);
							if (cemer.equals("X")) {
								flag = false;

								break;
							}
						}
					}
					for (int i = 0; i < removeArray.size(); i++) {

						String INVNR = removeArray.get(i);

						deleteResultDataBase(
								DEFINE.REPAIR_RESULT_BASE_TABLE_NAME, INVNR);
						deleteResultDataBase(
								DEFINE.REPAIR_RESULT_STOCK_TABLE_NAME, INVNR);
						deleteResultDataBase(
								DEFINE.REPAIR_RESULT_IMAGE_TABLE_NAME, INVNR);

					}
					String text = resultText;

					if (resultText == null || resultText.equals(""))
						text = "미전송 되었습니다.";

					updateCount(_data, text);

				}
			}
		}
		else 
		{
			mResultCompleate.onResultComplete("미전송되었습니다.");
		}
	}
	
	
	//Jonathan 15.05.28
private void deleteRowResultDataAnotherMonth(){
		
//		Log.e("deleteRowResultDataAnotherMonth", "deleteRowResultDataAnotherMonth");
		
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMM");
		Calendar rightNow = Calendar.getInstance();
		final String strCurrentDate = formatter.format(rightNow.getTime());
		
//		ContentValues contentValues = new ContentValues();
//		contentValues.put("IEDD", "NOT LIKE"+strCurrentDate+"%");
//
//		String[] keys = new String[1];
//		keys[0] = "IEDD";
		
		String[] _whereArgs = null;
		String[] _whereCause = null;
		String[] colums = null;
		
		DbQueryModel dbQueryModel = new DbQueryModel(
				DEFINE.REPAIR_RESULT_BASE_TABLE_NAME, _whereCause, _whereArgs,
				colums);
	
		
		DbAsyncTask dbAsyncTask = new DbAsyncTask("deleteResultDataBase",
				mContext, new DbAsyncResLintener() {
					
					@Override
					public void onCompleteDB(String funName, int type, Cursor cursor,
							String tableName) {
						// TODO Auto-generated method stub
						
						if(cursor==null)
							return;
						
						kog.e("Cursor Size", ""+cursor.getCount());
						
						cursor.moveToFirst();
						while (!cursor.isAfterLast()) {
							
							String strIEDD = cursor.getString(4);
							String strINVNR = cursor.getString(15);
							if(strIEDD.length()>=6){
								strIEDD = strIEDD.substring(0, 6);
							}
							Log.e("INVNR", ""+strINVNR);
							if(!strCurrentDate.equals(strIEDD)){
								deleteResultDataBaseAther(DEFINE.REPAIR_RESULT_BASE_TABLE_NAME, strINVNR);
								deleteResultDataBaseAther(DEFINE.REPAIR_RESULT_STOCK_TABLE_NAME, strINVNR);
								deleteResultDataBaseAther(DEFINE.REPAIR_RESULT_IMAGE_TABLE_NAME, strINVNR);
							}
							cursor.moveToNext();
						}
						cursor.close();
					}
				},dbQueryModel);

		dbAsyncTask.execute(DbAsyncTask.DB_SELECT);
	}
	
	public void map_log(HashMap<String, String> map)
	{
		Iterator<String> iterator = map.keySet().iterator();
		while (iterator.hasNext()) {
			String key = (String) iterator.next();
			kog.e("KDH", "RESULT_ map_log key = "+key);
			kog.e("KDH", "RESULT_ map_log value = "+map.get(key));
			
		}	
	}

	@Override
	public void reDownloadDB(String newVersion) {
		// TODO Auto-generated method stub

	}

	
	//Jonathan 15.05.28
	private void deleteResultDataBaseAther(String TableName, String INVNR) {
		ContentValues contentValues = new ContentValues();
		contentValues.put("INVNR", INVNR);

		String[] keys = new String[1];
		keys[0] = "INVNR";

		DbAsyncTask dbAsyncTask = new DbAsyncTask(TableName, TableName,
				mContext, this, contentValues, keys);

		dbAsyncTask.execute(DbAsyncTask.DB_DELETE);
	}
	
	/**
	 * 등록결과 성공했을때 DB에 저장된 데이터를 지워준다.
	 */
	private void deleteResultDataBase(String TableName, String INVNR) {
		ContentValues contentValues = new ContentValues();
		contentValues.put("INVNR", INVNR);

		String[] keys = new String[1];
		keys[0] = "INVNR";

		DbAsyncTask dbAsyncTask = new DbAsyncTask(TableName, TableName,
				mContext, this, contentValues, keys);

		dbAsyncTask.execute(DbAsyncTask.DB_DELETE);
	}

	public void recoveryResult(String aufnr) {
		queryStock(aufnr, "recovery");
	}

	private void responseRecovery(Cursor cursor) {
		cursor.moveToFirst();

		ArrayList<HashMap<String, String>> array = null;

		while (!cursor.isAfterLast()) {

			String aufnr = cursor.getString(cursor.getColumnIndex("AUFNR"));

			if (mStockMap.containsKey(aufnr)) {
				array = mStockMap.get(aufnr);
			} else {
				array = new ArrayList<HashMap<String, String>>();
			}
			HashMap<String, String> map = new HashMap<String, String>();
			for (int i = 0; i < cursor.getColumnCount(); i++) {
				map.put(cursor.getColumnName(i), cursor.getString(i));
			}

			array.add(map);

			if (!mStockMap.containsKey(aufnr))
				mStockMap.put(aufnr, array);

			queryRealStock(cursor.getString(cursor.getColumnIndex("MATKL")),
					cursor.getString(cursor.getColumnIndex("MATNR")));

			cursor.moveToNext();
		}

		cursor.close();
	}

	private void responseRealStock(Cursor cursor) {
		cursor.moveToFirst();

		while (!cursor.isAfterLast()) {

			Iterator<String> itMap = mStockMap.keySet().iterator();

			// String aufnr = cursor.getString(cursor.getColumnIndex("AUFNR"));

			while (itMap.hasNext()) {
				String aufnr = itMap.next();

				ArrayList<HashMap<String, String>> array = mStockMap.get(aufnr);
				for (int i = 0; i < array.size(); i++) {
					HashMap<String, String> map = array.get(i);

					String matkl = cursor.getString(cursor
							.getColumnIndex("MATKL"));
					String matnr = cursor.getString(cursor
							.getColumnIndex("MATNR"));
					int stock = Integer.parseInt(cursor.getString(cursor
							.getColumnIndex("LABST")));
					int consumption = Integer.parseInt(map.get("ERFMG"));
					updateStockComplete(matkl, matnr, stock, consumption);

					deleteResultDataBase(DEFINE.REPAIR_RESULT_BASE_TABLE_NAME,
							aufnr);
					deleteResultDataBase(DEFINE.REPAIR_RESULT_STOCK_TABLE_NAME,
							aufnr);
					deleteResultDataBase(DEFINE.REPAIR_RESULT_IMAGE_TABLE_NAME,
							aufnr);

					mStockMap.remove(aufnr);

				}

			}

			cursor.moveToNext();
		}

		cursor.close();
	}

	private void queryRealStock(String matkl, String matnr) {
		String[] _whereArgs = { matkl, matnr };
		String[] _whereCause = { "MATKL", "MATNR" };

		String[] colums = null;

		DbQueryModel dbQueryModel = new DbQueryModel(DEFINE.STOCK_TABLE_NAME,
				_whereCause, _whereArgs, colums);

		dbQueryModel.setOrderBy(DEFINE.MATKL + " asc");

		DbAsyncTask dbAsyncTask = new DbAsyncTask("queryRealStock", mContext,
				this, dbQueryModel);
		dbAsyncTask.execute(DbAsyncTask.DB_SELECT);
	}

	private void updateStockComplete(String matkl, String matnr, int stock,
			int consumption) {
		ContentValues contentValues = new ContentValues();
		// contentValues.put("MATKL", matkl);
		contentValues.put("MATNR", matnr);

		// myung 20131226 UPDATE
		 int val = stock - consumption;
		 contentValues.put("LABST", "" + val);
//		int val = stock
//				- MaintenanceResultFragment.getToalConsumptionTemp(matnr);
		// int totalConsuption = 0;
		// for(int i=0;
		// i<MaintenanceResultFragment.mTotalConsumptionItemModels.size(); i++){
		// if(MaintenanceResultFragment.mTotalConsumptionItemModels.get(i).getMATNR().equals(matnr)){
		// totalConsuption
		// =MaintenanceResultFragment.mTotalConsumptionItemModels.get(i).getTotalConsumption();
		// break;
		// }
		// }

		Log.e("val/stock/consumption", "" +val +"/"+ stock + "/" + consumption);
		contentValues.put("LABST", "" + val);

		String[] keys = new String[1];
		// keys[0] = "MATKL";
		keys[0] = "MATNR";

		DbAsyncTask dbAsyncTask = new DbAsyncTask("updateStockComplete",
				DEFINE.STOCK_TABLE_NAME, mContext, this, contentValues, keys);

		dbAsyncTask.execute(DbAsyncTask.DB_UPDATE);

	}

	private void updateComplete(String aufnr) {
		if (mCERMRMap.containsKey(aufnr)) {
			String cermr = mCERMRMap.get(aufnr);
			ContentValues contentValues = new ContentValues();
			contentValues.put("CCMSTS", "E0004");
			// contentValues.put("CEMER", cermr);
			contentValues.put("AUFNR", aufnr);

			String[] keys = new String[1];
			keys[0] = "AUFNR";

			DbAsyncTask dbAsyncTask = new DbAsyncTask("updateComplete",
					DEFINE.REPAIR_TABLE_NAME, mContext, this, contentValues,
					keys);

			dbAsyncTask.execute(DbAsyncTask.DB_UPDATE);
		}
	}

	private void repairPlanWork() {
		// showProgress("순회정비 일정을 동기화 중입니다.");

		SharedPreferencesUtil sharedPreferencesUtil = SharedPreferencesUtil
				.getInstance();

		String SyncStr = " ";

		if (sharedPreferencesUtil.isSuccessSyncDB()) {
			// String id = SharedPreferencesUtil.getInstance().getId();
			// if (mFirstId.equals(id)) {
			// SyncStr = " ";
			// } else {
			// SyncStr = "A";
			// sharedPreferencesUtil.setSyncSuccess(false);
			// }
			SyncStr = " ";
			// loginSuccess(); // 순회정비계획 싱크가 느려 우선은 로그인성공으로 바로가게 한다.
			// return;
		} else {

			SyncStr = "A";
			sharedPreferencesUtil.setSyncSuccess(false);
		}

		LoginModel model = KtRentalApplication.getLoginModel();
		// runUiThread(UiRunnable.MODE_PROGRESS_SHOW, "순회정비 리스트를 확인 중 입니다.");
		ConnectController connectController = new ConnectController(
				new ConnectInterface() {

					@Override
					public void reDownloadDB(String newVersion) {
						// TODO Auto-generated method stub

					}

					@Override
					public void connectResponse(String FuntionName,
							String resultText, String MTYPE, int resulCode,
							TableModel tableModel) {
						// TODO Auto-generated method stub
						if (FuntionName.equals(ConnectController.REPAIR_FUNTION_NAME)) {
							HashMap<String, String> loginTableNameMap = new HashMap<String, String>();
							loginTableNameMap.put("O_ITAB1",
									ConnectController.REPAIR_TABLE_NAME);
							loginTableNameMap.put("O_ITAB2",
									DEFINE.STOCK_TABLE_NAME);

							tableModel
									.setTableName(ConnectController.REPAIR_TABLE_NAME);

							loginTableNameMap.put("O_ITAB3",
									DEFINE.REPAIR_LAST_TABLE_NAME);
							loginTableNameMap.put("O_ITAB4",
									DEFINE.REPAIR_LAST_DETAIL_TABLE_NAME);

							DbAsyncTask asyncTask = new DbAsyncTask(
									ConnectController.REPAIR_FUNTION_NAME,
									O_ITAB1.TABLENAME, mContext,
									new DbAsyncResLintener() {

										@Override
										public void onCompleteDB(
												String funName, int type,
												Cursor cursor, String tableName) {
											// TODO Auto-generated method stub
											// hideProgress();
											if (mBaseMap.size() == 0)
												mResultCompleate
														.onResultComplete(""
																+ mResultText);
										}
									}, // DbAsyncResListener
									tableModel, loginTableNameMap);
							asyncTask.execute(DbAsyncTask.DB_ARRAY_INSERT);
						}
					}
				}, mContext);
		// 테이블 데이타를 얻어온다.
		connectController.getRepairPlan(model.getPernr(), SyncStr, mContext);
	}

	private void updateCount(final ArrayList<KDH_MSG> msg, final String message) {

		String[] _whereArgs = null;
		String[] _whereCause = null;

		String[] colums = null;

		DbQueryModel dbQueryModel = new DbQueryModel(
				DEFINE.REPAIR_RESULT_BASE_TABLE_NAME, _whereCause, _whereArgs,
				colums);

		DbAsyncTask dbAsyncTask = new DbAsyncTask("updateCount", mContext,
				new DbAsyncResLintener() {

					@Override
					public void onCompleteDB(String funName, int type,
							Cursor cursor, String tableName) {
						// TODO Auto-generated method stub
						if (funName.equals("updateCount")) {
							if (cursor != null) {
								cursor.moveToFirst();
								while (!cursor.isAfterLast()) {

									String invnr = cursor.getString(cursor
											.getColumnIndex("INVNR"));

									String count = cursor.getString(cursor
											.getColumnIndex("COUNT"));
									
									String getMsg = cursor.getString(cursor
											.getColumnIndex("MESSAGE"));
									

									ContentValues contentValues = new ContentValues();
									// contentValues.put("MATKL", matkl);
									contentValues.put("INVNR", invnr);

									int val = Integer.parseInt(count);
									if (val == 0) {
										updateStock();
									}
									val++;

									contentValues.put("COUNT", "" + val);
									
									if(msg.size() > 0)
									{
										for (int i = 0; i < msg.size(); i++) 
										{
											if(invnr.equals(msg.get(i).INVNR))
											{
												if(getMsg == null || TextUtils.isEmpty(getMsg))
												{
													contentValues.put("MESSAGE", "" + msg.get(i).msg);
												}
											}
										}
									}
									else
									{
										if(getMsg == null || TextUtils.isEmpty(getMsg))
										{
											contentValues.put("MESSAGE", "" + message);
										}
									}

									String[] keys = new String[1];
									// keys[0] = "MATKL";
									keys[0] = "INVNR";

									DbAsyncTask dbAsyncTask = new DbAsyncTask(
											"updateStockComplete",
											DEFINE.REPAIR_RESULT_BASE_TABLE_NAME,
											mContext, this, contentValues, keys);

									dbAsyncTask.execute(DbAsyncTask.DB_UPDATE);
									cursor.moveToNext();
								}
								cursor.close();
								mResultCompleate.onResultComplete("" + message);
							}
						}
					}
				}, dbQueryModel);
		dbAsyncTask.execute(DbAsyncTask.DB_SELECT);

	}

	private void updateStock() {
		if (mUpdateStockFlag) {
			Iterator<String> itMap = mStockMap.keySet().iterator();

			// String aufnr =
			// cursor.getString(cursor.getColumnIndex("AUFNR"));
			
			final ArrayList<String> tempNameMATNR = new ArrayList<String>();

			while (itMap.hasNext()) {
				String aufnr = itMap.next();

				ArrayList<HashMap<String, String>> array = mStockMap.get(aufnr);
				for (int i = 0; i < array.size(); i++) {
					HashMap<String, String> map = array.get(i);

					// String matkl = cursor.getString(cursor
					// .getColumnIndex("MATKL"));
					// String matnr = cursor.getString(cursor
					// .getColumnIndex("MATNR"));
					// int stock =
					// Integer.parseInt(cursor.getString(cursor
					// .getColumnIndex("LABST")));
					// int consumption =
					// Integer.parseInt(map.get("ERFMG"));
					// updateStockComplete(matkl, matnr, stock,
					// consumption);
					// queryRealStock(map.get("MATKL"),
					// map.get("MATNR"));
					String[] _whereArgs = { map.get("MATKL"), map.get("MATNR") };
					String[] _whereCause = { "MATKL", "MATNR" };

//					mERFMGMap.put(map.get("MATNR"),
//							Integer.parseInt(map.get("ERFMG")));
					boolean tempFlag = false;
					for(int j=0; j<mMATNR_MAP.size(); j++){
						if(mMATNR_MAP.get(j).equals(map.get("MATNR"))){
							mERFMG_MAP.set(j, mERFMG_MAP.get(j)+Integer.parseInt(map.get("ERFMG")));
							tempFlag = true;
							break;
						}
					}
					if(!tempFlag){
						mMATNR_MAP.add(map.get("MATNR"));
						mERFMG_MAP.add(Integer.parseInt(map.get("ERFMG")));
					}
					
					
					String[] colums = null;

					DbQueryModel dbQueryModel = new DbQueryModel(
							DEFINE.STOCK_TABLE_NAME, _whereCause, _whereArgs,
							colums);

					dbQueryModel.setOrderBy(DEFINE.MATKL + " asc");

					DbAsyncTask dbAsyncTask = new DbAsyncTask("queryRealStock",
							mContext, new DbAsyncResLintener() {

								@Override
								public void onCompleteDB(String funName,
										int type, Cursor cursor,
										String tableName) {
									// TODO Auto-generated method stub
									if (funName.equals("queryRealStock")) {
										if (cursor == null)
											return;

//										ArrayList<String> tempNameMATNR = new ArrayList<String>();
//										boolean bSonjai = false;
										cursor.moveToFirst();
										while (!cursor.isAfterLast()) {

											String matkl = cursor.getString(cursor
													.getColumnIndex("MATKL"));
											String matnr = cursor.getString(cursor
													.getColumnIndex("MATNR"));
											int stock = Integer.parseInt(cursor.getString(cursor
													.getColumnIndex("LABST")));
											int consumption = 0;
//											int consumption = mERFMGMap
//													.get(matnr);
											for(int j=0; j<mMATNR_MAP.size(); j++){
												if(mMATNR_MAP.get(j).equals(matnr)){
													consumption = mERFMG_MAP.get(j);
												}
											}
//											String strConsumption = cursor.getString(cursor
//													.getColumnIndex("ERFMG"));
//											int consumption = 0;
//											if(strConsumption==null || strConsumption.equals(" ") || strConsumption.equals(""))
//												;
//											else
//												consumption = Integer.parseInt(strConsumption);
											// myung 20131226 ADD 
											
											boolean bSonjai = false; 
											for (int i = 0; i < tempNameMATNR
													.size(); i++) {
												if (tempNameMATNR.get(i)
														.equals(matnr)) {
													bSonjai = true;
													break;
												}
											}
											
											// myung 20131226 UPDATE 
//											updateStockComplete(matkl,
//											matnr, stock,
//											consumption);
											if (!bSonjai){
												bSonjai = false;
												updateStockComplete(matkl,
														matnr, stock,
														consumption);
												tempNameMATNR.add(matnr);
											}
											// int val = stock - consumption;
											cursor.moveToNext();
										}

									}
								}
							}, dbQueryModel);
					dbAsyncTask.execute(DbAsyncTask.DB_SELECT);
				}
			}
		}
	}
	
	class KDH_MSG
	{
		public String INVNR;
		public String AUFNR; 
		public String msg;
		public String SING_T;
		public String SIGN;
		
		
	}
}
