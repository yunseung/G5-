package com.ktrental.popup;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ktrental.R;
import com.ktrental.common.DEFINE;
import com.ktrental.dialog.Menu3_Resist_Dialog;
import com.ktrental.popup.CalendarPopup.YyyyMmDdHhMm;

public class CalendarCustom extends LinearLayout implements
		View.OnClickListener {

	private Context context;
	private ArrayList<DayInfo> mDayList;
	public static int SUNDAY = 1;
	private Calendar hhmmdd;
	public String TODAY;
	// public String TODAY2;
	public int SELECTED = -1;
	public Button anchor;

	public static final int TYPE_LEFT = 0; // 시
	public static final int TYPE_RIGHT = 1; // 분
	private int mSelectedType = TYPE_LEFT;

	private String YYYY = null;
	private String MM = null;
	private String DD = null;
	private String hh = null;
	private String mm = null;
	private String EE = null;

	public String getYYYY() {
		return YYYY;
	}

	public String getMM() {
		return MM;
	}

	public String getDD() {
		return DD;
	}

	public String getHh() {
		return hh;
	}

	public String getMm() {
		return mm;
	}

	// myung 20131220 ADD
	public String getEE() {
		return EE;
	}

	LayoutInflater inflater;
	public boolean FIRST = true;

	public ImageView iv_done;

	String first_YYYY;
	String first_MM;
	String first_DD;
	String first_hh;
	String first_mm;
	String first_EE;

	private boolean NEWTYPE = true;

	String starttime;
	private boolean START = true;

	public CalendarCustom(Context context, AttributeSet attrs) {
		super(context, attrs);
		setOrientation(LinearLayout.HORIZONTAL);

		inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		this.context = context;

		hhmmdd = Calendar.getInstance();
		TODAY = "" + hhmmdd.get(Calendar.YEAR)
				+ String.format("%02d", (hhmmdd.get(Calendar.MONTH) + 1))
				+ hhmmdd.get(Calendar.DAY_OF_MONTH);

		YYYY = "" + hhmmdd.get(Calendar.YEAR);
		MM = String.format("%02d", (hhmmdd.get(Calendar.MONTH) + 1));
		DD = String.format("%02d", hhmmdd.get(Calendar.DAY_OF_MONTH));
		EE = String.format("%02d", hhmmdd.get(Calendar.DAY_OF_WEEK));
		EE = changeIntToWeek(Integer.valueOf(EE));

		first_YYYY = YYYY;
		first_MM = MM;
		first_DD = DD;
		// first_hh = hh;
		// first_mm = mm;
	}

	// myung 20131118 한글요일변경 함수
	public String changeIntToWeek(int week) {
		String strWeek = "";
		switch (week) {
		case Calendar.SUNDAY:
			strWeek = "일";
			break;

		case Calendar.MONDAY:
			strWeek = "월";
			break;

		case Calendar.TUESDAY:
			strWeek = "화";
			break;
		case Calendar.WEDNESDAY:
			strWeek = "수";
			break;

		case Calendar.THURSDAY:
			strWeek = "목";
			break;

		case Calendar.FRIDAY:
			strWeek = "금";
			break;

		case Calendar.SATURDAY:
			strWeek = "토";
			break;

		default:

			break;
		}

		return strWeek;
	}

	public void initView() {
		if (tv_hour != null) {
			hh = tv_hour.getText().toString();
			mm = tv_minute.getText().toString();
		}

		removeAllViews();
		LinearLayout ll = new LinearLayout(context);
		ll.setBackgroundResource(R.drawable.popup_mini_calendar_bg);
		ll.setOrientation(LinearLayout.VERTICAL);

		LayoutParams rlp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		
		RelativeLayout rl = new RelativeLayout(context);
		rl.setLayoutParams(rlp);

		// Log.i("####", "####디스플레이2 : "+DEFINE.DISPLAY);

		View title = (View) inflater.inflate(R.layout.title2_row, null);
		// LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(135,
		// 60);
		TextView tv = (TextView) title.findViewById(R.id.title1_id);
		// tv.setLayoutParams(lp);
		// tv.setPadding(12, 4, 0, 0);
		// tv.setTextSize(25);
		// tv.setTypeface(null, Typeface.BOLD);
		tv.setText(hhmmdd.get(Calendar.YEAR) + "." + String.format("%02d", (hhmmdd.get(Calendar.MONTH) + 1)));
		// tv.setTextColor(Color.BLACK);
		rl.addView(title);

		RelativeLayout.LayoutParams llp = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		llp.leftMargin = tv.getLayoutParams().width;
		llp.topMargin = 5 * 2;

		LinearLayout layout = new LinearLayout(context);
		layout.setLayoutParams(llp);
		layout.setOrientation(LinearLayout.HORIZONTAL);
		ImageView prev = new ImageView(context);
		prev.setBackgroundResource(R.drawable.cal_back_selector);
		ImageView next = new ImageView(context);
		next.setBackgroundResource(R.drawable.cal_next_selector);
		prev.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				getCalender(hhmmdd, 0);
				SELECTED = -1;
				initView();
			}
		});
		next.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				getCalender(hhmmdd, 1);
				SELECTED = -1;
				initView();
			}
		});

		layout.addView(prev);
		layout.addView(next);
		rl.addView(layout);

		ll.addView(rl);
		addView(ll);

		mDayList = new ArrayList<DayInfo>();

		hhmmdd.set(Calendar.DAY_OF_MONTH, 1);
		getCalendar(hhmmdd);

		LinearLayout row = null;
		for (int i = 0; i < mDayList.size(); i++) {
			final DayInfo dayinfo = mDayList.get(i);
			if (i == 0 || i % 7 == 0) {
				row = new LinearLayout(context);
			}
			View v = (View) inflater.inflate(R.layout.day_row, null);
			String day = dayinfo.getYear() + dayinfo.getMonth()
					+ dayinfo.getDay();

			if (TODAY.equals(day)) {
				// Log.i("####", "####TODAY"+TODAY);
				if (FIRST) {
					SELECTED = i;
					FIRST = false;
				}
				v.setBackgroundResource(R.drawable.cal_today);
			} else {
				v.setBackgroundColor(Color.TRANSPARENT);
			}

			if (SELECTED == i) {
				v.setBackgroundResource(R.drawable.cal_s);
			}

			final TextView one_day = (TextView) v.findViewById(R.id.day_id);
			if (!dayinfo.isInMonth())
				one_day.setTextColor(Color.GRAY);
			else {
				if (i == 0 || i % 7 == 0)
					one_day.setTextColor(Color.RED);
				else if ((i + 1) % 7 == 0)
					one_day.setTextColor(Color.BLUE);
			}
			one_day.setText(dayinfo.getDay());

			final int final_i = i;
			v.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					if (!dayinfo.isInMonth())
						return;
					int year = hhmmdd.get(Calendar.YEAR);
					int month = hhmmdd.get(Calendar.MONTH) + 1;
					String day0 = dayinfo.getDay().length() <= 1 ? "0"
							+ dayinfo.getDay() : dayinfo.getDay();
					String date = year + String.format("%02d", month) + day0;
					int date_int = Integer.parseInt(date);

					// Log.i("#","####날짜비교2/ "+MODE+"/"+DATE+"/"+date_int);

					switch (MODE) {
					case 0:
						if (DATE < date_int) {
							Toast.makeText(context, "시작일이 종료일보다 큽니다.", 0)
									.show();
							// EventPopupC epc = new EventPopupC(context);
							// epc.setTitle("시작일이 종료일보다 큽니다.");
							// epc.show();
							return;
						}
						break;
					case 1:
						if (DATE > date_int) {
							Toast.makeText(context, "시작일이 종료일보다 큽니다.", 0)
									.show();
							// EventPopupC epc = new EventPopupC(context);
							// epc.setTitle("시작일이 종료일보다 큽니다.");
							// epc.show();
							return;
						}
						break;
					}

					DayInfo di = mDayList.get(final_i);
					String day = di.getDay().length() <= 1 ? "0" + di.getDay()
							: di.getDay();
					// Log.i("###",
					// "####"+di.getYear()+"."+di.getMonth()+"."+day);

					YYYY = di.getYear();
					MM = di.getMonth();
					DD = day;

					SELECTED = final_i;
					initView();
				}
			});

			row.addView(v);
			if (i % 7 == 0) {
				ll.addView(row);
			}
		}

		initCalculator();
	}

	private void initCalculator() {
		RelativeLayout line = new RelativeLayout(context);
		line.setBackgroundResource(R.drawable.popup_mini_center_bg);
		addView(line);

		View v = inflater.inflate(R.layout.timepopup, null);
		setTimeView(v);
		addView(v);
	}

	private TextView tv_hour, tv_minute;

	private void setTimeView(View v) {
		tv_hour = (TextView) v.findViewById(R.id.tv_inventory_input_left);
		tv_hour.setOnClickListener(this);
		if (hh != null)
			tv_hour.setText(hh);
		tv_minute = (TextView) v.findViewById(R.id.tv_inventory_input_right);
		tv_minute.setOnClickListener(this);
		if (mm != null)
			tv_minute.setText(mm);

		if (mSelectedType == TYPE_RIGHT)
			setFocusInput(TYPE_RIGHT);

		v.findViewById(R.id.inventory_bt_num_0).setOnClickListener(this);
		v.findViewById(R.id.inventory_bt_num_1).setOnClickListener(this);
		v.findViewById(R.id.inventory_bt_num_2).setOnClickListener(this);
		v.findViewById(R.id.inventory_bt_num_3).setOnClickListener(this);
		v.findViewById(R.id.inventory_bt_num_4).setOnClickListener(this);
		v.findViewById(R.id.inventory_bt_num_5).setOnClickListener(this);
		v.findViewById(R.id.inventory_bt_num_6).setOnClickListener(this);
		v.findViewById(R.id.inventory_bt_num_7).setOnClickListener(this);
		v.findViewById(R.id.inventory_bt_num_8).setOnClickListener(this);
		v.findViewById(R.id.inventory_bt_num_9).setOnClickListener(this);

		v.findViewById(R.id.inventory_bt_delete).setOnClickListener(this);
		v.findViewById(R.id.inventory_bt_clear).setOnClickListener(this);

		iv_done = (ImageView) v.findViewById(R.id.inventory_bt_done);
		iv_done.setOnClickListener(this);
	}

	public ImageView getBt_done() {
		return iv_done;
	}

	public void setBt_done(ImageView iv_done) {
		this.iv_done = iv_done;
	}

	public Calendar getCalender(Calendar cal, int mode) {
		int year = cal.get(Calendar.YEAR);
		int month = cal.get(Calendar.MONTH);
		// Log.i("####", "#### 년월1  "+year+"/"+month);
		switch (mode) {
		case 0:
			month--;
			if (month < 0) {
				year--;
				month = 11;
			}
			break;
		case 1:
			month++;
			if (month >= 12) {
				year++;
				month = 0;
			}
			break;
		}
		// Log.i("####", "#### 년월2  "+year+"/"+month);

		cal.set(Calendar.YEAR, year);
		cal.set(Calendar.MONTH, month);
		cal.set(Calendar.DAY_OF_MONTH, 1);

		return cal;
	}
	
	
	public Calendar initCalender(Calendar cal) {
		int year = cal.get(Calendar.YEAR);
		int month = cal.get(Calendar.MONTH);
	
		cal.set(Calendar.YEAR, year);
		cal.set(Calendar.MONTH, month);
		cal.set(Calendar.DAY_OF_MONTH, 1);

		return cal;
	}

	CalendarPopup palendarpopup;

	public void setButton(View bt, CalendarPopup cp) {
		FIRST = true;
		// reset();
		initView();

		palendarpopup = cp;

		anchor = (Button) bt;

		Object data = anchor.getText();
		String str = data == null || data.toString().equals("")
				|| data.toString().equals(" ") ? "" : data.toString();
		// Log.i("####", "####"+str+"/");

		if (!str.equals("")) {
			String year = str.substring(0, 4);
			String month = str.substring(5, 7);
			String day = str.substring(8, 10);
			String hour = str.substring(14, 16);
			String minute = str.substring(17);

			// Log.i("####",
			// "####시간"+year+"/"+month+"/"+day+"/"+hour+"/"+minute);

			tv_hour.setText(hour);
			tv_minute.setText(minute);

			// SELECTED = 10; // 초기값 모양만 선택가능
			// YYYY = year;
			// MM = month;
			// DD = day;

			initView();
		}
	}

	Menu3_Resist_Dialog mrd;

	public void setButton(View bt, CalendarPopup cp, Menu3_Resist_Dialog mrd) {
		this.mrd = mrd;
		FIRST = true;
		// reset();
		initView();

		palendarpopup = cp;

		anchor = (Button) bt;

		Object data = anchor.getText();
		String str = data == null || data.toString().equals("")
				|| data.toString().equals(" ") ? "" : data.toString();
		// Log.i("####", "####"+str+"/");

		if (!str.equals("")) {
			String year = str.substring(0, 4);
			String month = str.substring(5, 7);
			String day = str.substring(8, 10);
			// myung 20131118 추가 & 변경
			// String hour = str.substring(11, 13);
			// String minute = str.substring(14, 16);
			String week = str.substring(10, 13);
			String hour = str.substring(14, 16);
			String minute = str.substring(17, 19);

			// Log.i("####",
			// "####시간"+year+"/"+month+"/"+day+"/"+week+"/"+hour+"/"+minute);

			tv_hour.setText(hour);
			tv_minute.setText(minute);

			// SELECTED = 10; // 초기값 모양만 선택가능
			// YYYY = year;
			// MM = month;
			// DD = day;

			initView();
		}
	}

	MovementDialog md;

	public void setButton(View bt, CalendarPopup cp, MovementDialog md) {
		this.md = md;
		FIRST = true;
		// reset();
		initView();

		palendarpopup = cp;

		anchor = (Button) bt;

		Object data = anchor.getText();
		String str = data == null || data.toString().equals("")
				|| data.toString().equals(" ") ? "" : data.toString();
		// Log.i("####", "####"+str+"/");

		if (!str.equals("")) {
			String year = str.substring(0, 4);
			String month = str.substring(5, 7);
			String day = str.substring(8, 10);
			String hour = str.substring(14, 16);
			String minute = str.substring(17);

			// Log.i("####",
			// "####시간"+year+"/"+month+"/"+day+"/"+hour+"/"+minute);

			tv_hour.setText(hour);
			tv_minute.setText(minute);

			// SELECTED = 10; // 초기값 모양만 선택가능
			// YYYY = year;
			// MM = month;
			// DD = day;

			initView();
		}
	}

	public void reset() {
		Calendar calendar = Calendar.getInstance();

		int int_year = calendar.get(Calendar.YEAR);
		int int_month = calendar.get(Calendar.MONTH) + 1;
		int int_day = calendar.get(Calendar.DAY_OF_MONTH);
		int int_hour = calendar.get(Calendar.HOUR_OF_DAY);
		int int_minute = calendar.get(Calendar.MINUTE);

		String year = String.format("%04d", int_year);
		String month = String.format("%02d", int_month);
		String day = String.format("%02d", int_day);
		String hour = String.format("%02d", int_hour);
		String minute = String.format("%02d", int_minute);

		// Log.i("####", "####"+year+"/"+month+"/"+day+"/"+hour+"/"+minute);

		YYYY = year;
		MM = month;
		DD = day;
		hh = hour;
		mm = minute;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
	}

	private void getCalendar(Calendar calendar) {
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH);

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

		calendar.set(Calendar.YEAR, year);
		calendar.set(Calendar.MONTH, month);
	}

	int DATE = Integer.MAX_VALUE;
	int MODE = Integer.MAX_VALUE;

	public void setMode(int num) {
		MODE = num;
	}

	public void setDate(int num) {
		DATE = num;
	}

	public class DayInfo {

		public String getYear() {
			return year;
		}

		public void setYear(String year) {
			this.year = year;
		}

		public String getMonth() {
			return month;
		}

		public void setMonth(String month) {
			this.month = month;
		}

		private String year;
		private String month;
		private String day;
		private boolean inMonth;

		public String getDay() {
			return day;
		}

		public void setDay(String day) {
			this.day = day;
		}

		public boolean isInMonth() {
			return inMonth;
		}

		public void setInMonth(boolean inMonth) {
			this.inMonth = inMonth;
		}

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.inventory_bt_num_0:
			setInput("0", false);
			break;
		case R.id.inventory_bt_num_1:
			setInput("1", false);
			break;
		case R.id.inventory_bt_num_2:
			setInput("2", false);
			break;
		case R.id.inventory_bt_num_3:
			setInput("3", false);
			break;
		case R.id.inventory_bt_num_4:
			setInput("4", false);
			break;
		case R.id.inventory_bt_num_5:
			setInput("5", false);
			break;
		case R.id.inventory_bt_num_6:
			setInput("6", false);
			break;
		case R.id.inventory_bt_num_7:
			setInput("7", false);
			break;
		case R.id.inventory_bt_num_8:
			setInput("8", false);
			break;
		case R.id.inventory_bt_num_9:
			setInput("9", false);
			break;
		case R.id.inventory_bt_delete:
			setInput("0", true);
			break;
		case R.id.inventory_bt_clear:
			setInput("CLEAR", true);
			break;
		case R.id.inventory_bt_done:
			if (NEWTYPE) {
				onDone();
			} else {
				if (START) {
					onDone2();
				} else {
					onDone3();
				}
			}

			break;
		case R.id.tv_inventory_input_left:
			setFocusInput(TYPE_LEFT);
			break;
		case R.id.tv_inventory_input_right:
			setFocusInput(TYPE_RIGHT);
			break;
		default:
			break;
		}
	}

	public void onDone() {
		if (YYYY == null) {
			Toast.makeText(context, "날짜를 선택해 주십시오", 0).show();
			return;
		}
		if (hh == null) {
			Toast.makeText(context, "시간을 입력해 주십시오", 0).show();
			return;
		}

		MM = MM.length() < 2 ? "0" + MM : MM;
		DD = DD.length() < 2 ? "0" + DD : DD;
		hh = hh.length() < 2 ? "0" + hh : hh;
		mm = mm.length() < 2 ? "0" + mm : mm;

		YyyyMmDdHhMm yjdhm = new YyyyMmDdHhMm();
		yjdhm.YYYY = YYYY;
		yjdhm.MM = MM;
		yjdhm.DD = DD;
		yjdhm.hh = hh;
		yjdhm.mm = mm;
		anchor.setTag(yjdhm);
		anchor.setText(YYYY + "." + MM + "." + DD + " " + hh + ":" + mm);
		reset();
		palendarpopup.dismiss();
		if (mrd != null)
			mrd.Temp(anchor, yjdhm);
	}

	public void onDone2() {
		// Log.i("####", "####"+"확인버튼이 눌렸습니다.");
		if (YYYY == null) {
			Toast.makeText(context, "날짜를 선택해 주십시오", 0).show();
			return;
		}
		if (hh == null) {
			Toast.makeText(context, "시간을 입력해 주십시오", 0).show();
			return;
		}

		// 미래일자 선택 안되게 막아주세요
		int i_first_YYYY = Integer.parseInt(first_YYYY);
		int i_first_MM = Integer.parseInt(first_MM);
		int i_first_DD = Integer.parseInt(first_DD);
		int i_first_hh = Integer.parseInt(first_hh);
		int i_first_mm = Integer.parseInt(first_mm);

		int i_YYYY = Integer.parseInt(YYYY);
		int i_MM = Integer.parseInt(MM);
		int i_DD = Integer.parseInt(DD);
		int i_hh = Integer.parseInt(hh);
		int i_mm = Integer.parseInt(mm);

		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, i_first_YYYY);
		cal.set(Calendar.MONTH, i_first_MM - 1);
		cal.set(Calendar.DAY_OF_MONTH, i_first_DD);
		cal.set(Calendar.HOUR_OF_DAY, i_first_hh);
		cal.set(Calendar.MINUTE, i_first_mm);

		long first_cal = cal.getTimeInMillis();

		cal.set(Calendar.MONTH, i_first_MM - 2);
		cal.set(Calendar.DAY_OF_MONTH, i_first_DD);

		long first_cal_before_1month = cal.getTimeInMillis();

		cal.set(Calendar.YEAR, i_YYYY);
		cal.set(Calendar.MONTH, i_MM - 1);
		cal.set(Calendar.DAY_OF_MONTH, i_DD);
		cal.set(Calendar.HOUR_OF_DAY, i_hh);
		cal.set(Calendar.MINUTE, i_mm);

		long now_cal = cal.getTimeInMillis();

		// Log.i("####", "####아아아아"+first_cal+"/"+now_cal);

		if (first_cal < now_cal) {
			final EventPopupC epc = new EventPopupC(context);
			// myung 20131220 ADD 출발시간 초기화
			final String temp_time = String.valueOf(i_first_hh);
			final String temp_minite = String.valueOf(i_first_mm);
			epc.show("출발/도착일시는 현재시간 이후로 등록할 수 없습니다.");
			epc.bt_done.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					setHour(temp_time);
					setMinute(temp_minite);
					setYearMonthDay(first_YYYY, first_MM, first_DD);
					FIRST = true;
					initView();
					epc.dismiss();
				}
			});
			return;
		}

		// myung 20131220 ADD 출발시간 세팅 변경
		if (first_cal_before_1month > now_cal) {
			EventPopupC epc = new EventPopupC(context);
			epc.show("조회기간은 한달 이내로 설정해 주세요.");
			return;
		}

		MM = MM.length() < 2 ? "0" + MM : MM;
		DD = DD.length() < 2 ? "0" + DD : DD;
		hh = hh.length() < 2 ? "0" + hh : hh;
		mm = mm.length() < 2 ? "0" + mm : mm;

		YyyyMmDdHhMm yjdhm = new YyyyMmDdHhMm();
		yjdhm.YYYY = YYYY;
		yjdhm.MM = MM;
		yjdhm.DD = DD;
		yjdhm.EE = EE;
		yjdhm.hh = hh.length() < 2 ? "0" + hh : hh;
		yjdhm.mm = mm.length() < 2 ? "0" + mm : mm;

		Date date = stringTODate(YYYY + MM + DD, "yyyyMMdd");
		EE = getDayOfWeek(date, true);

		anchor.setTag(yjdhm);
		// myung 20131118 요일 추가
		anchor.setText(YYYY + "." + MM + "." + DD + "[" + EE + "] " + hh + ":"
				+ mm);
		// Log.i("####", "####" + "음"+YYYY+MM+DD+"/"+hh+mm);
		if (md != null)
			md.setYYYYMMDDhhmm(YYYY + MM + DD, hh + mm, EE);
		palendarpopup.dismiss();
		reset();
	}

	// myung 20131220 ADD 요일은 항상 금요일인 문제
	public Date stringTODate(String dateText, String pattern) {
		Date date = null;
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(pattern);
			date = sdf.parse(dateText);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return date;
	}

	// myung 20131220 ADD 요일은 항상 금요일인 문제
	public String getDayOfWeek(Date date, boolean korea) {
		String[][] week = { { "일", "Sun" }, { "월", "Mon" }, { "화", "Tue" },
				{ "수", "Wen" }, { "목", "Thu" }, { "금", "Fri" }, { "토", "Sat" } };
		Calendar cal1 = Calendar.getInstance();
		cal1.setTime(date);
		if (korea) {
			return week[cal1.get(Calendar.DAY_OF_WEEK) - 1][0];
		} else {
			return week[cal1.get(Calendar.DAY_OF_WEEK) - 1][1];
		}
	}

	public void onDone3() {
		// Log.i("####", "####"+"확인버튼이 눌렸습니다.");
		if (YYYY == null) {
			Toast.makeText(context, "날짜를 선택해 주십시오", 0).show();
			return;
		}
		if (hh == null) {
			Toast.makeText(context, "시간을 입력해 주십시오", 0).show();
			return;
		}

		// 미래일자 선택 안되게 막아주세요
		int i_first_YYYY = Integer.parseInt(first_YYYY);
		int i_first_MM = Integer.parseInt(first_MM);
		int i_first_DD = Integer.parseInt(first_DD);
		int i_first_hh = Integer.parseInt(first_hh);
		int i_first_mm = Integer.parseInt(first_mm);

		int i_YYYY = Integer.parseInt(YYYY);
		int i_MM = Integer.parseInt(MM);
		int i_DD = Integer.parseInt(DD);
		int i_hh = Integer.parseInt(hh);
		int i_mm = Integer.parseInt(mm);

		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, i_first_YYYY);
		cal.set(Calendar.MONTH, i_first_MM - 1);
		cal.set(Calendar.DAY_OF_MONTH, i_first_DD);
		cal.set(Calendar.HOUR_OF_DAY, i_first_hh);
		cal.set(Calendar.MINUTE, i_first_mm);

		long first_cal = cal.getTimeInMillis();

		cal.set(Calendar.YEAR, i_YYYY);
		cal.set(Calendar.MONTH, i_MM - 1);
		cal.set(Calendar.DAY_OF_MONTH, i_DD);
		cal.set(Calendar.HOUR_OF_DAY, i_hh);
		cal.set(Calendar.MINUTE, i_mm);

		long now_cal = cal.getTimeInMillis();

		// Log.i("####", "####아아아아"+first_cal+"/"+now_cal);

		if (first_cal < now_cal) {
			EventPopupC epc = new EventPopupC(context);
			epc.show("출발/도착일시는 현재시간 이후로 등록할 수 없습니다.");
			return;
		}

		String start_YYYY = starttime.substring(0, 4);
		String start_MM = starttime.substring(5, 7);
		String start_DD = starttime.substring(8, 10);
		// myung 20131118 추가 & 변경
		// String start_hh = starttime.substring(11, 13);
		// String start_mm = starttime.substring(14);
		String start_hh = starttime.substring(14, 16);
		String start_mm = starttime.substring(17);

		// Log.i("####",
		// "####출발시간"+start_YYYY+"/"+start_MM+"/"+start_DD+"/"+start_hh+"/"+start_mm);

		int i_start_YYYY = Integer.parseInt(start_YYYY);
		int i_start_MM = Integer.parseInt(start_MM);
		int i_start_DD = Integer.parseInt(start_DD);
		int i_start_hh = Integer.parseInt(start_hh);
		int i_start_mm = Integer.parseInt(start_mm);

		cal.set(Calendar.YEAR, i_start_YYYY);
		cal.set(Calendar.MONTH, i_start_MM - 1);
		cal.set(Calendar.DAY_OF_MONTH, i_start_DD);
		cal.set(Calendar.HOUR_OF_DAY, i_start_hh);
		cal.set(Calendar.MINUTE, i_start_mm);

		long start_cal = cal.getTimeInMillis();

		if (start_cal > now_cal) {
			EventPopupC epc = new EventPopupC(context);
			epc.show("출발시간 이후의 시간을 입력해 주십시오");
			
			return;
		}

		MM = MM.length() < 2 ? "0" + MM : MM;
		DD = DD.length() < 2 ? "0" + DD : DD;
		hh = hh.length() < 2 ? "0" + hh : hh;
		mm = mm.length() < 2 ? "0" + mm : mm;

		YyyyMmDdHhMm yjdhm = new YyyyMmDdHhMm();
		yjdhm.YYYY = YYYY;
		yjdhm.MM = MM;
		yjdhm.DD = DD;
		yjdhm.EE = EE;
		yjdhm.hh = hh.length() < 2 ? "0" + hh : hh;
		yjdhm.mm = mm.length() < 2 ? "0" + mm : mm;
		anchor.setTag(yjdhm);
		// myung 20131118 요일 추가
		anchor.setText(YYYY + "." + MM + "." + DD + "[" + EE + "] " + hh + ":"
				+ mm);
		// anchor.setText(YYYY+"."+MM+"."+DD+" "+hh+":"+mm);
		// Log.i("####", "####" + "음"+YYYY+MM+DD+"/"+hh+mm);
		if (md != null)
			md.setYYYYMMDDhhmm(YYYY + MM + DD, hh + mm);
		reset();
		palendarpopup.dismiss();
	}

	private void setFocusInput(int type) {
		if (type == TYPE_LEFT) {
			tv_hour.setBackgroundResource(R.drawable.popup_calculator_focus);
			tv_minute.setBackgroundDrawable(null);
		} else {
			tv_minute.setBackgroundResource(R.drawable.popup_calculator_focus);
			tv_hour.setBackgroundDrawable(null);
		}
		mSelectedType = type;
	}

	private void setInput(String num, boolean delFlag) {
		TextView tvCurrent;
		if (num.equals("CLEAR")) {
			if (mSelectedType == TYPE_LEFT) {
				tv_hour.setText("");
			} else if (mSelectedType == TYPE_RIGHT) {
				tv_minute.setText("");
			}
		}
		if (mSelectedType == TYPE_LEFT) {
			tvCurrent = tv_hour;
		} else {
			tvCurrent = tv_minute;
		}
		if (!delFlag) {
			String text = tvCurrent.getText().toString();
			if (text.equals("00"))
				tvCurrent.setText("");
			if (tvCurrent.length() == 2) {
				tvCurrent.setText("");
			}
			num = tvCurrent.getText().toString() + num;
			int aaa = Integer.parseInt(num);

			if (mSelectedType == TYPE_LEFT) {
				if (aaa >= 24) {
					return;
				}
			} else {
				if (aaa >= 60) {
					return;
				}
			}

			tvCurrent.setText(num);

			if (tv_hour.getText() != null) {
				hh = tv_hour.getText().toString();
				if (hh.length() <= 1)
					hh = "0" + hh;
			}

			if (tv_minute.getText() != null) {
				mm = tv_minute.getText().toString();
				if (mm.length() <= 1)
					mm = "0" + mm;
			}

			if (mSelectedType == TYPE_LEFT) {
				if (tv_hour.length() == 2) {
					setFocusInput(TYPE_RIGHT);
					tvCurrent = tv_minute;
				}
			}
		} else {
			if (tvCurrent.length() == 0)
				return;
			String str = tvCurrent.getText().toString();
			StringBuilder sb = new StringBuilder(str);
			sb.deleteCharAt(sb.length() - 1);
			num = sb.toString();
			tvCurrent.setText(num);
		}
	}

	public String setDotTime(String time) {
		int size = time.length();
		StringBuffer sb = new StringBuffer(time);
		String dot = ":";
		if (size == 4) {
			sb.insert(2, dot);
		} else if (size == 8) {
			sb.insert(4, dot);
			sb.insert(7, dot);
		}
		return sb.toString();
	}

	public void setHour(String string) {
		tv_hour.setText(string);
		first_hh = hh = string;
		NEWTYPE = false;
	}

	public void setMinute(String string) {
		tv_minute.setText(string);
		first_mm = mm = string;
	}
	
	public void setYearMonthDay(String strYear, String strMonth, String strDay) {
		YYYY = strYear;
		MM = strMonth;
		DD = strDay;
	}

	public void setStartTime(String string) {
		starttime = string;
		START = false;
	}

}
