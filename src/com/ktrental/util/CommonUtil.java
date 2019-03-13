package com.ktrental.util;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v4.content.CursorLoader;
import android.telephony.PhoneNumberUtils;
import android.telephony.TelephonyManager;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.ktrental.cm.util.PrintLog;

import java.io.File;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.regex.Pattern;

/**
 * 공통적으로 static 기능 클래스. <br/>
 * 안드로이드 Intent 호출을 통해 제공되는 전화,sms 등에 Action 을 이용하여 사용됨.
 * 
 * 
 * 
 * @author Hong
 * @since 2013.07.12
 */
public class CommonUtil {

	public static final String OLLEH_NAVI_PACKAGE_NAME = "kt.navi.OLLEH_NAVIGATION";
	public static final String CALLER_PACKAGE_NAME = "CALLER_PACKAGE_NAME";
	public static final String COORDINATE_WGS84 = "COORDINATE_WGS84";
	public static final String ROUTE_WGS84_POINT = "ROUTE_WGS84_POINT_NAME";

	public static void callAction(Context context, String num) {
		try {
			// 전화 걸기
			Uri uri = Uri.parse("tel:" + num);
			Intent it = new Intent(Intent.ACTION_CALL, uri);
			context.startActivity(it);
		} catch (ActivityNotFoundException e) {
			Toast.makeText(context, "" + e.getMessage(), Toast.LENGTH_SHORT)
					.show();
		}
	}

	public static void smsAction(Context context, String num, String body) {
		try {
			// SMS 발송
			Uri uri = Uri.parse("smsto:" + num);
			Intent it = new Intent(Intent.ACTION_SENDTO, uri);
			it.putExtra("sms_body", body);
			context.startActivity(it);
		} catch (ActivityNotFoundException e) {
			Toast.makeText(context, "" + e.getMessage(), Toast.LENGTH_SHORT)
					.show();
		}
	}

	// 화폐단위 숫자 세자리마다 쉼표넣기
	public static String currentpoint(String result) {

		DecimalFormatSymbols dfs = new DecimalFormatSymbols();
		dfs.setGroupingSeparator(',');

		DecimalFormat df = new DecimalFormat("###,###,###,###");
		df.setDecimalFormatSymbols(dfs);

		try {
			double inputNum = Double.parseDouble(result);
			result = df.format(inputNum).toString();
		} catch (NumberFormatException e) {
			// TODO: handle exception
		}

		return result;
	}

	// 전화번 숫자 세자리마다 쉼표넣기
	public static String setPhoneNumber(String result) {

		boolean flag = false;

		if (result.startsWith("0"))
			flag = true;

		DecimalFormatSymbols dfs = new DecimalFormatSymbols();
		dfs.setGroupingSeparator('-');

		DecimalFormat df = new DecimalFormat("###,####,####");
		df.setDecimalFormatSymbols(dfs);

		try {
			double inputNum = Double.parseDouble(result);
			result = df.format(inputNum).toString();

			if (flag) {
				if (!result.equals("0"))
					result = "0" + result;
			}

		} catch (NumberFormatException e) {
			// TODO: handle exception
		}

		return result;
	}

	// 화폐단위 숫자 세자리마다 쉼표넣기

	public static String setTime(String result) {

		DecimalFormatSymbols dfs = new DecimalFormatSymbols();
		dfs.setGroupingSeparator(':');

		DecimalFormat df = new DecimalFormat("##,##");
		df.setDecimalFormatSymbols(dfs);

		try {
			double inputNum = Double.parseDouble(result);
			result = df.format(inputNum).toString();
		} catch (NumberFormatException e) {
			// TODO: handle exception
		}

		return result;
	}

	// 주민등록번호 - 넣기
	public static String setSocialNum(String text) {
		String result = text;

		if (text != null) {
			int length = text.length();
			if (length == 6) {
				result = text + "-";
			}
		}

		return result;

	}

	// 사업자번호 - 넣기
	public static String setBusinessNum(String text) {
		String result = text;

		if (text != null) {
			int length = text.length();
			if (length == 3 || length == 6) {
				result = text + "-";
			}
		}

		return result;

	}

	// 운전면허번호 - 넣기
	public static String setDrivingNum(String text) {
		String result = text;

		if (text != null) {
			int length = text.length();
			if (length == 2 || length == 9) {
				result = text + "-";
			}
		}

		return result;

	}

	public static String getPhoneNumber(Context context) {

		String phoneNumber = "";

		TelephonyManager tMgr = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		phoneNumber = tMgr.getLine1Number();

		if (phoneNumber != null) {
			phoneNumber = phoneNumber.replace("+82", "0");

			phoneNumber = PhoneNumberUtils.formatNumber(phoneNumber);
		}
		return phoneNumber;
	}

	/**
	 * Activity 혹은 fragment 등에 ui 종료시 bitmap 및 각각의 뷰에 저장되어 있는 background drawable이 누수가 발생한다. </br> 이를 통해 종료시에 메모리에 계속 쌓이는 현상을 없애준다.
	 * 
	 * @param root
	 */
	public static void unbindDrawables(View root) {

		if (root != null) {
			if (root.getBackground() != null) {

				root.getBackground().setCallback(null);
			}
			if (root instanceof ImageView) {

				ImageView iv = (ImageView) root;

				Drawable drawable = iv.getDrawable();

				if (drawable instanceof BitmapDrawable) {
					BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
					Bitmap bitmap = bitmapDrawable.getBitmap();
					if (bitmap != null) {

						// bitmap.recycle();
						bitmap = null;
					}
				}
				iv.setImageDrawable(null);

			}
		}
		if (root instanceof ViewGroup) {
			for (int i = 0; i < ((ViewGroup) root).getChildCount(); i++) {
				unbindDrawables(((ViewGroup) root).getChildAt(i));
			}
			if (!(root instanceof AdapterView))
				((ViewGroup) root).removeAllViews();
		}

		root = null;

	}

	public static void showCallStack() {
		PrintLog.Print("showCallStack", "stacktrace");
		StringBuffer stacktrace = new StringBuffer();
		StackTraceElement[] stackTrace = new Exception().getStackTrace();
		for (int x = 0; x < stackTrace.length; x++) {
			stacktrace.append(stackTrace[x].toString() + " ");
			PrintLog.Print("showCallStack", stackTrace[x].toString());
		}

	}

	public static boolean isFile(String FilePath) {
		boolean isFile = false;

		File file = new File(FilePath);
		isFile = file.exists(); // 원하는 경로에 폴더가 있는지 확인

		return isFile;
	}

	public static boolean isValidSDCard() {

		boolean reValid = false;

		reValid = Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED);

		return reValid;
	}

	public static void deleteFile(String FilePath) {
		if (isFile(FilePath)) {
			File file = new File(FilePath);

			file.delete();
		}
	}

	public static String setDotDate(String day) {
		int size = day.length();
		StringBuffer sb = new StringBuffer(day);

		String dot = ".";

		if (size == 4) {
			sb.insert(2, dot);
		} else if (size == 6) {
			sb.insert(4, dot);
		} else if (size == 8) {
			sb.insert(4, dot);
			sb.insert(7, dot);
		}
		return sb.toString();
	}

	public static String setDotTime(String time) {
		int size = time.length();
		StringBuffer sb = new StringBuffer(time);

		String dot = ":";

		if (size == 4) {
			sb.insert(2, dot);
		} else if (size == 8) {
			sb.insert(4, dot);
			sb.insert(7, dot);
		} else if (size == 6) {
			sb.insert(2, dot);
			sb.insert(5, dot);
		}
		return sb.toString();
	}

	public static String getCurrentDay() {
		String day = "";
		Calendar calendar = Calendar.getInstance();
		int today = calendar.get(Calendar.DATE);

		calendar.set(Calendar.DAY_OF_MONTH, 1);

		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH) + 1;

		day = addZero(year) + addZero(month) + addZero(today);

		return day;
	}

	public static String getCurrentMonth() {
		String day = "";
		Calendar calendar = Calendar.getInstance();
		int today = calendar.get(Calendar.DATE);

		calendar.set(Calendar.DAY_OF_MONTH, 1);

		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH) + 1;

		day = addZero(year) + addZero(month);

		return day;
	}

	public static String getTomorrow() {
		String day = "";
		Calendar cal = new GregorianCalendar();
		cal.add(Calendar.DATE, 1);

		int year = cal.get(Calendar.YEAR);
		int month = cal.get(Calendar.MONTH) + 1;
		int today = cal.get(Calendar.DAY_OF_MONTH);

		day = addZero(year) + addZero(month) + addZero(today);

		return day;
	}

	public static String addZero(int arg) {
		String val = String.valueOf(arg);

		if (arg < 10)
			val = "0" + val;

		return val;
	}

	public static String getCurrentTime() {
		String strNow = "";
		// 현재 시간을 msec으로 구한다.
		long now = System.currentTimeMillis();
		// 현재 시간을 저장 한다.
		Date date = new Date(now);
		// 시간 포맷으로 만든다.
		SimpleDateFormat sdfNow = new SimpleDateFormat("yyMMddHHmmss");
		strNow = sdfNow.format(date);

		return strNow;
	}

	public static void hideKeyboad(Context context, EditText editText) {
		InputMethodManager imm = (InputMethodManager) context
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
	}

	/**
	 * 일정한 문자열 끝에 문자를 삽입한다. (예) 이어진 영어단어에 라인피티를 추가 등
	 * 
	 * @@param String str 원본 문자열
	 * @@int len 자를 바이트 수
	 * @@param addStr 삽입하고자하는 문자열
	 */

	public static String addLineEndString(String str, int len, String addStr) {
		if (str == null)
			return "";

		char[] charArray = str.toCharArray();

		StringBuffer returnStr = new StringBuffer("");
		int byteSize = 0;

		for (int i = 0; i < str.length(); i++) {
			if (charArray[i] < 256) {
				byteSize += 1;
			} else {
				byteSize += 2;
			}

			if (byteSize >= len) {
				byteSize = 0;
				returnStr.append(charArray[i]).append(addStr);
				break;
			} else {
				returnStr.append(charArray[i]);
			}

		}

		return returnStr.toString();
	}

	public static String getDayOfWeek() {

		String dayOfWeek = "";

		Calendar cal = Calendar.getInstance();
		int day_of_week = cal.get(Calendar.DAY_OF_WEEK);

		switch (day_of_week) {
		case 1:
			dayOfWeek = "일";
			break;
		case 2:
			dayOfWeek = "월";
			break;
		case 3:
			dayOfWeek = "화";
			break;
		case 4:
			dayOfWeek = "수";
			break;
		case 5:
			dayOfWeek = "목";
			break;
		case 6:
			dayOfWeek = "금";
			break;
		case 7:
			dayOfWeek = "토";
			break;
		}

		return dayOfWeek;
	}

	public static String getCurrentTimeHHMM() {
		String strNow = "";
		// 현재 시간을 msec으로 구한다.
		long now = System.currentTimeMillis();
		// 현재 시간을 저장 한다.
		Date date = new Date(now);
		// 시간 포맷으로 만든다.
		SimpleDateFormat sdfNow = new SimpleDateFormat("HH:mm");
		strNow = sdfNow.format(date);

		return strNow;
	}

	public static String getDayOfWeek(String date) {

		int year = 0;
		int month = 0;
		int day = 0;

		if (date.length() > 7) {
			year = Integer.parseInt(date.substring(0, 4));
			month = Integer.parseInt(date.substring(4, 6));
			day = Integer.parseInt(date.substring(6, 8));
		}
		String dayOfWeek = "";

		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, year);
		cal.set(Calendar.MONTH, month - 1);
		cal.set(Calendar.DATE, day);

		int day_of_week = cal.get(Calendar.DAY_OF_WEEK);

		switch (day_of_week) {
		case 1:
			dayOfWeek = "일";
			break;
		case 2:
			dayOfWeek = "월";
			break;
		case 3:
			dayOfWeek = "화";
			break;
		case 4:
			dayOfWeek = "수";
			break;
		case 5:
			dayOfWeek = "목";
			break;
		case 6:
			dayOfWeek = "금";
			break;
		case 7:
			dayOfWeek = "토";
			break;
		}

		return dayOfWeek;
	}

	public static void onOllehNavi(double start_x, double start_y,
			double destination_x, double destination_y, String packageName,
			Context context) {

		Intent intent = new Intent(OLLEH_NAVI_PACKAGE_NAME);
		intent.putExtra(CALLER_PACKAGE_NAME, packageName + ".cm");// getPackageName());
		intent.putExtra(COORDINATE_WGS84, true);

		// 출발지 도착지 위도경도
		double[] pt = { start_x, start_y, destination_x, destination_y };
		// double[] pt = { 37.50445d, 127.0489861d, 37.5646515d, 126.9756165d };
		intent.putExtra(ROUTE_WGS84_POINT, pt);

		context.sendBroadcast(intent);
	}
	

	public static String addDate(String day) {
		int size = day.length();
		StringBuffer sb = new StringBuffer(day);

		if (size == 8) {
			sb.insert(4, "년 ");
			sb.insert(8, "월 ");
			sb.insert(12, "일 ");
		}
		return sb.toString();
	}

	public static String makePhoneNumber(String phoneNumber) {
		String regEx = "(\\d{3})(\\d{3,4})(\\d{4})";

		if (!Pattern.matches(regEx, phoneNumber))
			return null;

		return phoneNumber.replaceAll(regEx, "$1-$2-$3");

	}

	public static String getRealPathFromURI_API19(Context context, Uri uri){
		String filePath = "";
		String wholeID = DocumentsContract.getDocumentId(uri);

		// Split at colon, use second item in the array
		String id = wholeID.split(":")[1];

		String[] column = { MediaStore.Images.Media.DATA };

		// where id is equal to
		String sel = MediaStore.Images.Media._ID + "=?";

		Cursor cursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
				column, sel, new String[]{ id }, null);

		int columnIndex = cursor.getColumnIndex(column[0]);

		if (cursor.moveToFirst()) {
			filePath = cursor.getString(columnIndex);
		}
		cursor.close();
		return filePath;
	}


	public static String getRealPathFromURI_API11to18(Context context, Uri contentUri) {
		String[] proj = { MediaStore.Images.Media.DATA };
		String result = null;

		CursorLoader cursorLoader = new CursorLoader(
				context,
				contentUri, proj, null, null, null);
		Cursor cursor = cursorLoader.loadInBackground();

		if(cursor != null){
			int column_index =
					cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
			cursor.moveToFirst();
			result = cursor.getString(column_index);
		}
		return result;
	}

	public static String getRealPathFromURI_BelowAPI11(Context context, Uri contentUri){
		String[] proj = { MediaStore.Images.Media.DATA };
		Cursor cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
		int column_index
				= cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
		cursor.moveToFirst();
		return cursor.getString(column_index);
	}
}
