package com.ktrental.calendar;

import android.util.Log;

import java.util.ArrayList;
import java.util.Calendar;

public class CalendarController {

	/**
	 * 달력 컨트롤러
	 * 
	 * 
	 * @author Hong
	 * @since 2013.03.16
	 */

	private Calendar mThisMonthCalendar;

	private int mCurrentYear = 0;
	private int mCurrentMonth = 0;
	private int mToday = 0;

	public final static int TYPE_SHOW_DEFAULT = 0;
	public final static int TYPE_SHOW_OTHERMONTH = 1;

	private int mType = TYPE_SHOW_DEFAULT;

	public CalendarController(int type) {

		mThisMonthCalendar = Calendar.getInstance();
		mToday = mThisMonthCalendar.get(Calendar.DATE);

		mThisMonthCalendar.set(Calendar.DAY_OF_MONTH, 1);

		mCurrentYear = mThisMonthCalendar.get(Calendar.YEAR);
		mCurrentMonth = mThisMonthCalendar.get(Calendar.MONTH);

		mType = type;
	}

	/**
	 * 현재 선택된 달을 리턴해준다.
	 * 
	 * @return ArrayList<DayInfo> 현재 선택된 달에 보여줄 현재 달에 리스트를 리턴해준다.
	 */
	public ArrayList<DayInfoModel> getDayInfoArrayList() {

		ArrayList<DayInfoModel> reDayInfoList = new ArrayList<DayInfoModel>();

		int lastMonthStartDay;
		int dayOfMonth;
		int thisMonthLastDay;

		// 이번달 시작일의 요일을 구한다. 시작일이 일요일인 경우 인덱스를 1(일요일)에서 8(다음주 일요일)로 바꾼다.)
		dayOfMonth = mThisMonthCalendar.get(Calendar.DAY_OF_WEEK);
		thisMonthLastDay = mThisMonthCalendar.getActualMaximum(Calendar.DAY_OF_MONTH);

		mThisMonthCalendar.add(Calendar.MONTH, -1);

		// 지난달의 마지막 일자를 구한다.
		lastMonthStartDay = mThisMonthCalendar.getActualMaximum(Calendar.DAY_OF_MONTH);

		mThisMonthCalendar.add(Calendar.MONTH, 1);

		if (dayOfMonth == Calendar.SUNDAY) {
			dayOfMonth += 7;
		}

		lastMonthStartDay -= (dayOfMonth - 1) - 1;


		reDayInfoList = getShowCurrentCalendarList(dayOfMonth, lastMonthStartDay, thisMonthLastDay);


		return reDayInfoList;
	}

	/**
	 * 현재 선택되서 보여줄 달에 DayInfo 리스트를 리턴해준다.
	 * 
	 * @return ArrayList<DayInfo> 현재 선택되서 보여줄 달에 DayInfo 리스트
	 * 
	 * @param dayOfMonth 현재 날짜
	 * @param lastMonthStartDay 이전 달에 마지막 날짜
	 * @param thisMonthLastDay 현재 달에 마지막 날짜
	 */

	private ArrayList<DayInfoModel> getShowCurrentCalendarList(int dayOfMonth, int lastMonthStartDay, int thisMonthLastDay) {
		ArrayList<DayInfoModel> reDayInfoList = new ArrayList<DayInfoModel>();

		// // 일월화수목금토일을 표시하기 위해 리스트를 얻어온다.
		// reDayInfoList = repeatMonthDay(1, 8, 0, reDayInfoList, 0, true);

		// 이전달에 남은 날짜들을 리스트에 담아온다.
		Calendar thumb = mThisMonthCalendar;

		Calendar mThisMonthCalendar2 = Calendar.getInstance();
		reDayInfoList = repeatMonthDay(0, dayOfMonth - 1, lastMonthStartDay,
				reDayInfoList, DayInfoModel.PREV_MONTH, false);

		mThisMonthCalendar = thumb;

		// 현재달에 남은 날짜들을 리스트에 담아온다.
		reDayInfoList = repeatMonthDay(1, thisMonthLastDay + 1, 0,
				reDayInfoList, DayInfoModel.CURRENT_MONTH, false);

		if (mType == TYPE_SHOW_OTHERMONTH) {

			int nextMonthLoopSize = 42 - (thisMonthLastDay + dayOfMonth - 1) + 1;

			// 다음달에 남은 날짜들을 리스트에 담아온다.
			reDayInfoList = repeatMonthDay(1, nextMonthLoopSize, 0,
					reDayInfoList, DayInfoModel.NEXT_MONTH, false);

		}

		return reDayInfoList;

	}

	private ArrayList<DayInfoModel> repeatMonthDay(int increaseNum, int loopSize, int lastMonthStartDay, ArrayList<DayInfoModel> dayInfoList, int inMonth, boolean isHeaderText) {

		Calendar mThisMonthCalendar2 = Calendar.getInstance();
		DayInfoModel day = null;

		// 실제 현재 달과 같은 달인지 판별
		boolean isCurrentMonth = getCurrentMonth();

		ArrayList<DayInfoModel> currentArrayList = new ArrayList<DayInfoModel>();

		for (int i = increaseNum; i < loopSize; i++) {
			int date = lastMonthStartDay + i;
			day = new DayInfoModel();
			day.setDay(String.valueOf(date), isHeaderText);
			day.setInMonth(inMonth);
			day.setDayOfWeek(dayInfoList.size());
			if (isCurrentMonth) {
				day.setToDay(date, mThisMonthCalendar2.get(Calendar.DATE));
				day.setCurrentDay(String.valueOf(mThisMonthCalendar2.get(Calendar.YEAR))
						+ addZero(mThisMonthCalendar2.get(Calendar.MONTH) + 1) + addZero(date));
			} else {

				String year = null;
				String month = null;
				Calendar calendar = null;


				if (inMonth == DayInfoModel.NEXT_MONTH) {
					calendar = getMonth(mThisMonthCalendar2, +1);

				} else {
					calendar = getMonth(mThisMonthCalendar2, -1);

				}
				year = String.valueOf(calendar.get(Calendar.YEAR));
				month = addZero(calendar.get(Calendar.MONTH) + 1);

				day.setCurrentDay(year + month + String.valueOf(addZero(date)));
			}
			dayInfoList.add(day);
			if (mType == TYPE_SHOW_DEFAULT)
				currentArrayList.add(day);

		}

		if (mType == TYPE_SHOW_DEFAULT)
			dayInfoList = currentArrayList;

		return dayInfoList;
	}

	/**
	 * 현재 선택된 달이 실제 현재 달인지 구분한다.
	 * 
	 * @return boolean 실제 현재 달이면 true
	 */
	private boolean getCurrentMonth() {

		boolean reCurrentMonthFlag = false;

		int year = mThisMonthCalendar.get(Calendar.YEAR);
		int month = mThisMonthCalendar.get(Calendar.MONTH);

		if (year == mCurrentYear && month == mCurrentMonth)
			reCurrentMonthFlag = true;

		return reCurrentMonthFlag;

	}

	/**
	 * 현재 선택된 달에 타이틀을 보내준다.00
	 * 
	 * @return String 현재 선택된 달에 보여줄 현재 달에 타이틀을 리턴해준다.
	 */
	public String getCalendarTitle() {

		String reTitle = "";

		if (mThisMonthCalendar != null) {

			int month = (mThisMonthCalendar.get(Calendar.MONTH) + 1);

			String monthStr = String.valueOf(month);

			if (month < 10)
				monthStr = "0" + monthStr;

			reTitle = mThisMonthCalendar.get(Calendar.YEAR) + "." + monthStr;
		}

		return reTitle;

	}

	/**
	 * 현재 선택된 달에 타이틀을 보내준다.00
	 * 
	 * @return String 현재 선택된 달에 보여줄 현재 달에 타이틀을 리턴해준다.
	 */
	public String getCurrentCalendarTitle() {

		String reTitle = "";

		if (mThisMonthCalendar != null) {

			reTitle = getCalendarTitle();
			reTitle = reTitle + "." + mThisMonthCalendar.get(Calendar.DATE);

		}
		return reTitle;

	}

	public ArrayList<DayInfoModel> getPrevDayInfoList() {
		ArrayList<DayInfoModel> reDayInfoList = new ArrayList<DayInfoModel>();

		mThisMonthCalendar = getMonth(mThisMonthCalendar, -1);

		reDayInfoList = getDayInfoArrayList();

		return reDayInfoList;
	}

	public ArrayList<DayInfoModel> getNextDayInfoList() {
		ArrayList<DayInfoModel> reDayInfoList = new ArrayList<DayInfoModel>();

		mThisMonthCalendar = getMonth(mThisMonthCalendar, 1);

		reDayInfoList = getDayInfoArrayList();

		return reDayInfoList;
	}

	/**
	 * 지난달의 Calendar 객체를 반환합니다.
	 * 
	 * @param calendar
	 * @return LastMonthCalendar
	 */
	private Calendar getMonth(Calendar calendar, int addCount) {
		calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), 1);
		calendar.add(Calendar.MONTH, addCount);
		return calendar;
	}

	public String getCurrentDay() {
		String reTitle = "";

		if (mThisMonthCalendar != null) {

			int month = (mThisMonthCalendar.get(Calendar.MONTH) + 1);

			String monthStr = String.valueOf(month);

			monthStr = addZero(month);
			String today = addZero(mToday);

			reTitle = mThisMonthCalendar.get(Calendar.YEAR) + monthStr + today;
		}

		return reTitle;
	}

	private String addZero(int arg) {
		String reStr = String.valueOf(arg);

		if (arg < 10)
			reStr = "0" + reStr;

		return reStr;
	}

}