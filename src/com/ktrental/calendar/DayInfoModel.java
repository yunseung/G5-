package com.ktrental.calendar;

import java.util.Calendar;

import android.content.Context;

/**
 * 하루의 날짜정보를 저장하는 클래스
 * 
 * @author Hong
 * @since 2013.03.15
 */
public class DayInfoModel {

	public final static int PREV_MONTH = 0;
	public final static int CURRENT_MONTH = 1;
	public final static int NEXT_MONTH = 2;

	private String day; // int 로 변경.
	private int inMonth;
	private int dayOfWeek = -1;
	private ColorData mColorData;
	private boolean toDay = false;
	private int position = 0;

	private String currentDay = "";

	/**
	 * 날짜를 반환한다.
	 * 
	 * @return day 날짜
	 */
	public String getDay() {
		return day;
	}

	/**
	 * 날짜를 저장한다.
	 * 
	 * @param day
	 *            날짜
	 */
	public void setDay(String day) {
		this.day = day;
	}

	/**
	 * 날짜를 저장한다.
	 * 
	 * @param day
	 *            날짜
	 */
	public void setDay(String day, boolean isHeaderText) {

		if (isHeaderText)
			day = getHeaderText(Integer.parseInt(day));

		this.day = day;
	}

	public String getHeaderText(int day) {

		String reText = "";

		switch (day) {
		case Calendar.SUNDAY:
			reText = "일";
			break;
		case Calendar.MONDAY:
			reText = "월";
			break;
		case Calendar.TUESDAY:
			reText = "화";
			break;
		case Calendar.WEDNESDAY:
			reText = "수";
			break;
		case Calendar.THURSDAY:
			reText = "목";
			break;
		case Calendar.FRIDAY:
			reText = "금";
			break;
		case Calendar.SATURDAY:
			reText = "토";
			break;

		default:
			break;
		}

		return reText;

	}

	/**
	 * 현재 선택된 월를 반환한다.
	 * 
	 * @return int 현재 선택된 월를 반환한다.
	 */
	public int getInMonth() {
		return inMonth;
	}

	/**
	 * 이번달의 날짜인지 정보를 반환한다.
	 * 
	 * @return inMonth(true/false)
	 */
	public boolean isInMonth() {

		boolean isMonth = false;

		if (inMonth == CURRENT_MONTH)
			isMonth = true;

		return isMonth;
	}

	/**
	 * 이번달의 날짜인지 정보를 저장한다.
	 * 
	 * @param inMonth
	 *            (true/false)
	 */
	public void setInMonth(int inMonth) {
		this.inMonth = inMonth;
	}

	/**
	 * 현재 날짜에 요일 정보를 리턴한다.
	 * 
	 * @return int:dayOfWeek 현재 날짜에 요일 정보를 리턴
	 */
	public int getDayOfWeek() {
		return dayOfWeek;
	}

	/**
	 * 현재 날짜에 요일 정보를 저장한다. 저장된 날짜와 맞는 ColorDatar를 만든다.
	 * 
	 * @param position
	 *            현재 보여질 포지션
	 * 
	 */
	public void setDayOfWeek(int position) {

		if (mColorData == null)
			mColorData = new ColorData();

		if (isInMonth()) {
			if (position % 7 == 0) {
				dayOfWeek = Calendar.SUNDAY;
			} else if (position % 7 == 6) {
				dayOfWeek = Calendar.SATURDAY;
			} else if (position % 7 == 5) {
				dayOfWeek = Calendar.FRIDAY;
			} else if (position % 7 == 4) {
				dayOfWeek = Calendar.THURSDAY;
			} else if (position % 7 == 3) {
				dayOfWeek = Calendar.WEDNESDAY;
			} else if (position % 7 == 2) {
				dayOfWeek = Calendar.TUESDAY;
			} else if (position % 7 == 1) {
				dayOfWeek = Calendar.MONDAY;
			}

		} else {
			dayOfWeek = ColorData.OTHER_MONTH;
		}

	}

	/**
	 * 현재 날짜에 요일 색을 리턴한다
	 * 
	 * @return int 색상 정보이다.
	 * 
	 */
	public int getDayColor(Context context) {

		int reColor = 0;

		if (isInMonth()) {
			reColor = mColorData.getDayColor(dayOfWeek, context);
		} else {
			reColor = mColorData.getDayColor(-1, context);
		}

		return reColor;
	}

	public boolean isToDay() {
		return toDay;
	}

	public void setToDay(int date, int currentDate) {
		if (isInMonth())
			if (date == currentDate)
				toDay = true;

	}

	/**
	 * 현재 날짜에 포지션을 리턴한다.
	 * 
	 * @return int 현재 포지션
	 * 
	 */
	public int getPosition() {
		return position;
	}

	/**
	 * 현재 날짜에 포지션을 지정한다.
	 * 
	 * @param int 현재 포지션
	 * 
	 */
	public void setPosition(int position) {
		this.position = position;
	}

	public String getCurrentDay() {
		return currentDay;
	}

	public void setCurrentDay(String currentDay) {
		this.currentDay = currentDay;
	}

}