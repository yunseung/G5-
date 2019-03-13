package com.ktrental.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.ktrental.model.LoginModel;

/**
 * sharedPreferences 컨트롤러이다. 렌탈에서 사용되는 file I.O 컨틀롤러이다.
 * 
 * 
 * @author Hong
 * @since 2013.07.12
 */
public class SharedPreferencesUtil {

	private Context mContext;
	private SharedPreferences mSharedPreferences;

	private static final String SHAREDPREFERENCES_NAME = "SharedPreferencesName";

	public static final String KEY_VERSION = "version";
	public static final String KEY_SAVE_ID_FLAG = "save_id_flag";
	public static final String KEY_SAVE_ID = "save_id";
	public static final String KEY_DB_SUCCESS = "db_success";
	public static final String KEY_SYNC_SUCCESS = "sync_success";
	public static final String KEY_FIRST_MONTH_DAY = "KEY_FIRST_MONTH_DAY";
	public static final String KEY_FIRST_DAY = "KEY_FIRST_DAY";
	public static final String KEY_LAST_LOGIN_ID = "KEY_LAST_LOGIN_ID";
	
	public static final String FIRST_START = "FIRST_START";
	

	private static SharedPreferencesUtil mSharedPreferencesUtil;

	public SharedPreferencesUtil(Context context) {
		mContext = context;
		mSharedPreferences = mContext.getSharedPreferences(SHAREDPREFERENCES_NAME, Context.MODE_PRIVATE);
		mSharedPreferencesUtil = this;
	}

	public static SharedPreferencesUtil getInstance() {
		return mSharedPreferencesUtil;
	}

	public String getString(String key, String defaultVal) {
		String reString = null;

		reString = mSharedPreferences.getString(key, defaultVal); // 키값, 디폴트값

		return reString;
	}

	public void setString(String key, String value) {
		SharedPreferences.Editor editor = mSharedPreferences.edit();
		editor.putString(key, value); // 키값, 저장값
		editor.apply();

	}

	public boolean getBoolean(String key, boolean defaultVal) {
		boolean reFlag = false;

		reFlag = mSharedPreferences.getBoolean(key, defaultVal); // 키값, 디폴트값

		return reFlag;
	}

	public void setBoolean(String key, boolean value) {
		SharedPreferences.Editor editor = mSharedPreferences.edit();
		editor.putBoolean(key, value); // 키값, 저장값
		editor.apply();

	}
	
	public int getInt(String key, int defaultVal) {
		int reFlag = 0;

		reFlag = mSharedPreferences.getInt(key, defaultVal); // 키값, 디폴트값

		return reFlag;
	}

	public void setInt(String key, int value) {
		SharedPreferences.Editor editor = mSharedPreferences.edit();
		editor.putInt(key, value); // 키값, 저장값
		editor.apply();

	}

	private int getInteger(String key, int defaultVal) {
		int reString = 0;

		reString = mSharedPreferences.getInt(key, defaultVal);

		return reString;
	}

	private void setInteger(String key, int value) {
		SharedPreferences.Editor editor = mSharedPreferences.edit();
		editor.putInt(key, value); // 키값, 저장값
		editor.apply();

	}

	public String getVersion() {

		String reVersion = null;

		reVersion = getString(KEY_VERSION, "0");

		return reVersion;

	}

	public void setVersion(String version) {

		setString(KEY_VERSION, version);

	}

	public boolean isIdSave() {

		boolean reValid = false;

		reValid = getBoolean(KEY_SAVE_ID_FLAG, false);

		return reValid;

	}

	public void setSaveIdFlag(boolean isSave) {

		setBoolean(KEY_SAVE_ID_FLAG, isSave);

	}

	public String getId() {

		String reVersion = null;

		reVersion = getString(KEY_SAVE_ID, "-1");

		return reVersion;

	}

	public void setId(String id) {

		setString(KEY_SAVE_ID, id);

	}

	public void setLoginModel(LoginModel model){
		
	}

	public boolean isValedDB() {

		boolean reValedDB = false;

		reValedDB = getBoolean(KEY_DB_SUCCESS, reValedDB);

		return reValedDB;

	}

	public void setDownLoadDataBassSuccess(boolean successed) {

		setBoolean(KEY_DB_SUCCESS, successed);

	}

	public boolean isSuccessSyncDB() {

		boolean reValedDB = false;

		reValedDB = getBoolean(KEY_SYNC_SUCCESS, reValedDB);

		return reValedDB;

	}

	public void setSyncSuccess(boolean successed) {

		setBoolean(KEY_SYNC_SUCCESS, successed);

	}

	public String getFirstMonthDay() {

		String day = "0";

		day = getString(KEY_FIRST_MONTH_DAY, "0");

		return day;

	}

	public void setFirstMonthDay(String day) {

		setString(KEY_FIRST_MONTH_DAY, day);

	}
	
	public int getFirstDay() {
		int day = 0;
		day = getInt(KEY_FIRST_DAY, 0);
		return day;
	}

	public void setFirstDay(int day) {

		setInt(KEY_FIRST_DAY, day);

	}
	
	public boolean getFIRST_START() {
		return getBoolean(FIRST_START, true);
	}

	public void setFIRST_START(boolean first) {

		setBoolean(FIRST_START, first);
	}
	
	

	public String getLastLoginId() {

		String id = "0";

		id = getString(KEY_LAST_LOGIN_ID, "0");

		return id;

	}

	public void setLastLoginId(String id) {

		setString(KEY_LAST_LOGIN_ID, id);

	}
	
	
	
	
	public String getEndTime() {

		String id = "0";

		id = getString("get_end_time", "0");

		return id;

	}

	public void setEndTime(String value) {

		setString("get_end_time", value);

	}
	
	
	
	
	
	public String getLoginType() {

		String id = "0";

		id = getString("login_type", "0");

		return id;

	}

	public void setLoginType(String value) {

		setString("login_type", value);

	}
	
	
	
	public String getSession() {

		String session = "";

		session = getString("SESSION", "");

		return session;

	}

	public void setSession(String value) {

		setString("SESSION", value);

	}
	
	
	
	

}
