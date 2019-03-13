package com.ktrental.custom;

import java.util.ArrayList;
import java.util.Calendar;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;

import com.ktrental.R;
import com.ktrental.common.DEFINE;
import com.ktrental.dialog.Duedate_Dialog;

public class Cal_Custom extends LinearLayout {

	private Context context;
	private ArrayList<DayInfo> mDayList;
	public static int SUNDAY = 1;
	private Calendar hhmmdd;
	public String TODAY;
	public String TODAY2;
	public int SELECTED = -1;

	LayoutInflater inflater;

	public Cal_Custom(Context context, AttributeSet attrs) {
		super(context, attrs);
		setOrientation(LinearLayout.VERTICAL);
		setBackgroundResource(R.drawable.reservation_calendar_bg);
		setPadding(10, 10, 10, 10);
		this.context = context;

		inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		initView();
	}

	public void initView() {
		removeAllViews();

		hhmmdd = Calendar.getInstance();

		View title = (View) inflater.inflate(R.layout.title4_row, null);

		// LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(135,
		// 82);
		TextView tv = (TextView) title.findViewById(R.id.title1_id);
		
		// myung 20131119
//		if (DEFINE.DISPLAY.equals("2560")) {
			tv.setPadding(50, 38, 0, 0);
//		}
		// tv.setLayoutParams(lp);
		// tv.setPadding(23, 12, 0, 0);
		// tv.setTextSize(28);
		// tv.setTypeface(null, Typeface.BOLD);
		tv.setText(hhmmdd.get(Calendar.YEAR) + "."
				+ String.format("%02d", (hhmmdd.get(Calendar.MONTH) + 1)));
		tv.setTextColor(Color.parseColor("#555555"));
		addView(title);

		mDayList = new ArrayList<DayInfo>();

		TODAY = "" + hhmmdd.get(Calendar.YEAR)
				+ String.format("%02d", (hhmmdd.get(Calendar.MONTH) + 1))
				+ String.format("%02d", hhmmdd.get(Calendar.DAY_OF_MONTH));
		TODAY2 = "" + hhmmdd.get(Calendar.YEAR) + "."
				+ String.format("%02d", (hhmmdd.get(Calendar.MONTH) + 1)) + "."
				+ String.format("%02d", hhmmdd.get(Calendar.DAY_OF_MONTH));
		// Log.i("#","#### 오늘 "+TODAY);
		hhmmdd.set(Calendar.DAY_OF_MONTH, 1);
		getCalendar(hhmmdd);

		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		LinearLayout row = null;
		
		for (int i = 0; i < mDayList.size(); i++) {
			DayInfo dayinfo = mDayList.get(i);
			if (i == 0 || i % 7 == 0) {
				row = new LinearLayout(context);
				// myung 20131119 달력의 현재 날짜 표시 박스, 선택날짜 표시 박스 위치 수정
//				if (DEFINE.DISPLAY.equals("2560")) {
					// myung 20131121 ADD 달력의 현재 날짜 표시 박스, 선택날짜 표시 박스 위치 수정
					int tempY = 0;
					if(i / 7 == 0)
						tempY = 9;
					row.setPadding(18, tempY, 0, 0);
//				} else {
//					row.setPadding(4, 0, 0, 0);
//				}
			}
			View v = (View) inflater.inflate(R.layout.daydayday, null);
			String day = dayinfo.getYear() + dayinfo.getMonth()
					+ dayinfo.getDay();
			if (TODAY.equals(day)) {
				v.setBackgroundResource(R.drawable.cal_today);
			} else {
				v.setBackgroundColor(Color.TRANSPARENT);
			}
			if (SELECTED == i) {
				v.setBackgroundResource(R.drawable.cal_s);
			}

			TextView one_day = (TextView) v.findViewById(R.id.day_id);
			if (!dayinfo.isInMonth())
				one_day.setTextColor(Color.parseColor("#cccccc"));
			else {
				if (i == 0 || i % 7 == 0)
					one_day.setTextColor(Color.parseColor("#fd2727"));
				else if ((i + 1) % 7 == 0)
					one_day.setTextColor(Color.parseColor("#1d84a5"));
				else
					one_day.setTextColor(Color.parseColor("#333333"));
			}
			one_day.setText(dayinfo.getDay());
			if (dayinfo.isInMonth() && Duedate_Dialog.dd != null)
				Duedate_Dialog.dd.setDay(one_day, i, mDayList);

			row.addView(v);
			if (i % 7 == 0)
				addView(row);
		}
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
	}

	private void getCalendar(Calendar calendar) {
		int lastMonthStartDay;
		int dayOfMonth;
		int thisMonthLastDay;
		mDayList.clear();

		String last_year;
		String last_month;
		String this_year;
		String this_month;
		String next_year;
		String next_month;

		dayOfMonth = calendar.get(Calendar.DAY_OF_WEEK);
		thisMonthLastDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
		calendar.add(Calendar.MONTH, -1);
		last_year = "" + calendar.get(Calendar.YEAR);
		last_month = String.format("%02d", (calendar.get(Calendar.MONTH) + 1));

		lastMonthStartDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
		calendar.add(Calendar.MONTH, 1);
		this_year = "" + calendar.get(Calendar.YEAR);
		this_month = String.format("%02d", (calendar.get(Calendar.MONTH) + 1));

		calendar.add(Calendar.MONTH, 1);
		next_year = "" + calendar.get(Calendar.YEAR);
		next_month = String.format("%02d", (calendar.get(Calendar.MONTH) + 1));

		if (dayOfMonth == SUNDAY) {
			dayOfMonth += 7;
		}
		lastMonthStartDay -= (dayOfMonth - 1) - 1;

		DayInfo day;
		for (int i = 0; i < dayOfMonth - 1; i++) {
			int date = lastMonthStartDay + i;
			day = new DayInfo();
			day.setDay(Integer.toString(date));
			day.setInMonth(false);
			day.setYear(last_year);
			day.setMonth(last_month);
			mDayList.add(day);
		}

		for (int i = 1; i <= thisMonthLastDay; i++) {
			day = new DayInfo();
			day.setDay(Integer.toString(i));
			day.setInMonth(true);
			day.setYear(this_year);
			day.setMonth(this_month);
			mDayList.add(day);
		}
		for (int i = 1; i < 42 - (thisMonthLastDay + dayOfMonth - 1) + 1; i++) {
			day = new DayInfo();
			day.setDay(Integer.toString(i));
			day.setInMonth(false);
			day.setYear(next_year);
			day.setMonth(next_month);
			mDayList.add(day);
		}
	}
}
