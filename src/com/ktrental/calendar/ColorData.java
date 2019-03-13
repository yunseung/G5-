package com.ktrental.calendar;

import java.util.Calendar;

import android.content.Context;
import android.content.res.Resources;

import com.ktrental.R;

public class ColorData {

	public final static int OTHER_MONTH = -1;

	public int getDayColor(int dayOfWeek, Context context) {

		
		
		int reDayColor = 0;

		Resources res = context.getResources();
		
		switch (dayOfWeek) {
		case Calendar.SATURDAY:
			reDayColor = res.getColor(R.color.cal_saturday);
			break;
		case Calendar.SUNDAY:
			reDayColor = res.getColor(R.color.cal_sunday);
			break;
		case OTHER_MONTH:
			reDayColor = res.getColor(R.color.cal_othermonth);
			break;

		default:
			reDayColor = res.getColor(R.color.cal_weekday);
			break;
		}

		return reDayColor;
	}

}
