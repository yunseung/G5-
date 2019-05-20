package com.ktrental.custom;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ktrental.R;
import com.ktrental.dialog.Duedate_Dialog;

import java.util.ArrayList;
import java.util.Calendar;

public class Cal_Custom extends LinearLayout {

	private Context context;
	private ArrayList<DayInfo> mDayList;
	public static int SUNDAY = 1;
	private Calendar hhmmdd;
	public String TODAY;
	public String TODAY2;
	public int SELECTED = -1;

	LayoutInflater inflater;

	private View title;
	private TextView tv;
	private LinearLayout row;
	private View v;

	public Cal_Custom(Context context, AttributeSet attrs) {
		super(context, attrs);
		setOrientation(LinearLayout.VERTICAL);
		setBackgroundResource(R.drawable.reservation_calendar_bg);
		setPadding(10, 10, 10, 10);
		this.context = context;

        hhmmdd = Calendar.getInstance();

		inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		initView();
	}

	public void initView() {
		removeAllViews();
        mDayList = new ArrayList<DayInfo>();



        TODAY = "" + hhmmdd.get(Calendar.YEAR)
                + String.format("%02d", (hhmmdd.get(Calendar.MONTH) + 1))
                + String.format("%02d", hhmmdd.get(Calendar.DAY_OF_MONTH));
        TODAY2 = "" + hhmmdd.get(Calendar.YEAR) + "."
                + String.format("%02d", (hhmmdd.get(Calendar.MONTH) + 1)) + "."
                + String.format("%02d", hhmmdd.get(Calendar.DAY_OF_MONTH));

        hhmmdd.set(Calendar.DAY_OF_MONTH, 1);

        title = (View) inflater.inflate(R.layout.title4_row, null);
        tv = (TextView) title.findViewById(R.id.title1_id);

        title.findViewById(R.id.pre).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                SELECTED = -1;
                hhmmdd.add(Calendar.MONTH, -1);
                hhmmdd.set(Calendar.DAY_OF_MONTH, 1);
                drawCalendar();
            }
        });

        title.findViewById(R.id.next).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                SELECTED = -1;
                hhmmdd.add(Calendar.MONTH, 1);
                hhmmdd.set(Calendar.DAY_OF_MONTH, 1);
                drawCalendar();
            }
        });

        drawCalendar();
	}

	private void drawCalendar() {
        removeAllViews();
        mDayList.clear();

        tv.setText(hhmmdd.get(Calendar.YEAR) + "." + String.format("%02d", (hhmmdd.get(Calendar.MONTH) + 1)));
        tv.setTextColor(Color.parseColor("#555555"));
        addView(title);

        Log.e("yunseung+++", " : " + hhmmdd.getTime());
        getCalendar(hhmmdd);

        for (int i = 0; i < mDayList.size(); i++) {
            DayInfo dayinfo = mDayList.get(i);
            if (i == 0 || i % 7 == 0) {
                row = new LinearLayout(context);
                int tempY = 0;
                if(i / 7 == 0)
                    tempY = 9;
                row.setPadding(18, tempY, 0, 0);
            }

            v = (View) inflater.inflate(R.layout.daydayday, null);
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

        calendar.add(Calendar.MONTH, -1);
	}
}
