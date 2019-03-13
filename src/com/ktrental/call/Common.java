package com.ktrental.call;

import com.ktrental.util.kog;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class Common {

	
	private static Common instance;
	
	public static Common getInstance() {
		if (instance == null)
			instance = new Common();
		return instance;
	}	
	
	
	
	
	
//	public static boolean isTicket = false;
	
	
	
	
	
	
	
	
	public static String FileRead(String aufnr , Activity _activity) {
		/** READ **/
		SharedPreferences PREFREAD = _activity.getSharedPreferences("CALL_TIME", Context.MODE_PRIVATE);
		String call_time = PREFREAD.getString(aufnr, "");
		kog.e("Jonathan", "call_time :: " + call_time);
		return call_time ; 
	}

	public static void FileWrite(String aufnr, String call_time ,Activity _activity) {
		/** WRITE **/
		SharedPreferences PREF = _activity.getSharedPreferences("CALL_TIME", Context.MODE_PRIVATE);
		Editor ed = PREF.edit();
		
		ed.putString(aufnr, call_time);
		
		ed.commit();
	}
	
	public static String FileRead_Call(String aufnr, String key , Activity _activity) {
		/** READ **/
		SharedPreferences PREFREAD = _activity.getSharedPreferences("CALL_TIME_" + aufnr, Context.MODE_PRIVATE);
		String value = PREFREAD.getString(key, "");
		kog.e("Jonathan", "FileRead " + "CALL_TIME_"+aufnr + " 여기에 key == " + key + " value == " + value);
		return value ; 
	}

	public static void FileWrite_Call(String aufnr, String key, String call_time ,Activity _activity) {
		/** WRITE **/
		SharedPreferences PREF = _activity.getSharedPreferences("CALL_TIME_" + aufnr, Context.MODE_PRIVATE);
		Editor ed = PREF.edit();
		
		ed.putString(key, call_time);
		kog.e("Jonathan", "FileWrite " + "WLSAP_SAVE_DATA_"+aufnr + " 여기에 key == " + key + " value == " + call_time);
		
		ed.commit();
	}
	
	
	
	public static String FileRead_History(String key , Activity _activity) {
		/** READ **/
		SharedPreferences PREFREAD = _activity.getSharedPreferences("WLSAP_SAVE_HISTORY", Context.MODE_PRIVATE);
		String C_DATE = PREFREAD.getString(key, "");
		kog.e("Jonathan", "C_DATE :: " + C_DATE);
		return C_DATE ; 
	}

	public static void FileWrite_History(String key, String WtriteStar ,Activity _activity) {
		/** WRITE **/
		SharedPreferences PREF = _activity.getSharedPreferences("WLSAP_SAVE_HISTORY", Context.MODE_PRIVATE);
		Editor ed = PREF.edit();
		
		ed.putString(key, WtriteStar);
		
		ed.commit();
	}
	
	
	
	
	
	public static void FileRemove(Activity _activity) {
		/** WRITE **/
		SharedPreferences PREF = _activity.getSharedPreferences("WLSAP_SAVE_DATA", Context.MODE_PRIVATE);
		Editor editor = PREF.edit();
        editor.clear();
        editor.commit();
		
	}
	
	public static void FileRemove_ATT(String att_id, Activity _activity) {
		/** WRITE **/
		SharedPreferences PREF = _activity.getSharedPreferences("WLSAP_SAVE_DATA_" + att_id, Context.MODE_PRIVATE);
		Editor editor = PREF.edit();
		
		kog.e("Jonathan", "FileRemove " + "WLSAP_SAVE_DATA_"+att_id + " 여기에 모든 키를 지움.");
		
        editor.clear();
        editor.commit();
		
	}
	
	
	
	
	
}
