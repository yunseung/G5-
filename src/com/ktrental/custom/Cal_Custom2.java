package com.ktrental.custom;

import java.util.ArrayList;
import java.util.Calendar;

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

import com.ktrental.R;
import com.ktrental.cm.util.PrintLog;
import com.ktrental.common.DEFINE;
import com.ktrental.popup.EventPopupC;

public class Cal_Custom2 extends LinearLayout
{

    private Context            context;
    private ArrayList<DayInfo> mDayList;
    public static int          SUNDAY        = 1;
    private Calendar           hhmmdd;
    public String              TODAY;
    public String              TODAY2;
    public int                 SELECTED      = -1;
    public int                 INIT_SELECTED = -1;
    public Button              anchor;
    LayoutInflater             inflater;

    public Cal_Custom2(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        setOrientation(LinearLayout.VERTICAL);
        setBackgroundResource(R.drawable.popup_calendar_bg);
        this.context = context;

        PrintLog.Print("Cal_Custom2", "Cal_Custom2");

        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        hhmmdd = Calendar.getInstance();
        TODAY = "" + hhmmdd.get(Calendar.YEAR) + String.format("%02d", (hhmmdd.get(Calendar.MONTH) + 1))
                + String.format("%02d", (hhmmdd.get(Calendar.DAY_OF_MONTH)));
        TODAY2 = "" + hhmmdd.get(Calendar.YEAR) + "." + String.format("%02d", (hhmmdd.get(Calendar.MONTH) + 1)) + "."
                + String.format("%02d", (hhmmdd.get(Calendar.DAY_OF_MONTH)));
        initView();
    }

    public void initView()
    {
        removeAllViews();
        LayoutParams rlp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        RelativeLayout rl = new RelativeLayout(context);
        rl.setLayoutParams(rlp);

        // Log.i("####", "####디스플레이3 : "+DEFINE.DISPLAY);

        View title = (View) inflater.inflate(R.layout.title_row, null);

        // LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(135,
        // 78);
        // TextView tv = new TextView(context);
        TextView tv = (TextView) title.findViewById(R.id.title1_id);

        // tv.setLayoutParams(lp);
        // tv.setPadding(23, 10, 0, 0);
        // tv.setTextSize(28);
        // tv.setTypeface(null, Typeface.BOLD);
        tv.setText(hhmmdd.get(Calendar.YEAR) + "." + String.format("%02d", (hhmmdd.get(Calendar.MONTH) + 1)));
        tv.setTextColor(Color.parseColor("#555555"));
        rl.addView(title);

        RelativeLayout.LayoutParams llp = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        if (DEFINE.DISPLAY.equals("2560"))
        {
            llp.leftMargin = 265 * 2;
            llp.topMargin = 10 * 2;
        }
        else
        {
            llp.leftMargin = 265;
            llp.topMargin = 10;
        }

        LinearLayout layout = new LinearLayout(context);
        layout.setLayoutParams(llp);
        layout.setOrientation(LinearLayout.HORIZONTAL);
        ImageView prev = new ImageView(context);
        prev.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                getCalender(hhmmdd, 0);
                SELECTED = -1;
                initView();
            }
        });
        prev.setBackgroundResource(R.drawable.cal_back_selector);
        ImageView next = new ImageView(context);
        next.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                getCalender(hhmmdd, 1);
                SELECTED = -1;
                initView();
            }
        });
        next.setBackgroundResource(R.drawable.cal_next_selector);
        layout.addView(prev);
        layout.addView(next);
        rl.addView(layout);

        addView(rl);

        mDayList = new ArrayList<DayInfo>();

        // Log.i("#","#### 오늘 "+TODAY);
        hhmmdd.set(Calendar.DAY_OF_MONTH, 1);
        getCalendar(hhmmdd);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        LinearLayout row = null;
        for (int i = 0; i < mDayList.size(); i++)
        {
            final DayInfo dayinfo = mDayList.get(i);
            if (i == 0 || i % 7 == 0)
            {
                row = new LinearLayout(context);
            }
            View v = (View) inflater.inflate(R.layout.daydayday, null);
            String day = dayinfo.getYear() + dayinfo.getMonth() + dayinfo.getDay();
            if (TODAY.equals(day))
            {
                v.setBackgroundResource(R.drawable.cal_today);
            }
            else
            {
                v.setBackgroundColor(Color.TRANSPARENT);
            }
            // 음 이상한데.. -__- 수정 수정
            // KangHyunJin 20151210
            // if (SELECTED == i)
            if (SELECTED == -1 && String.valueOf(INIT_SELECTED).equals(dayinfo.getDay()) && dayinfo.isInMonth())
            {
                PrintLog.Print("Cal_Custom2", SELECTED + "   " + i + "  dayinfo.getDay()  " + dayinfo.getDay());
                v.setBackgroundResource(R.drawable.cal_s);
            }
            // 실제 달력에서 선택된 부분을 선택한다
            else if(SELECTED == i)
            {
                v.setBackgroundResource(R.drawable.cal_s);
            }
            
            final TextView one_day = (TextView) v.findViewById(R.id.day_id);
            if (!dayinfo.isInMonth())
                one_day.setTextColor(Color.parseColor("#cccccc"));
            else
            {
                if (i == 0 || i % 7 == 0)
                    one_day.setTextColor(Color.parseColor("#fd2727"));
                else if ((i + 1) % 7 == 0)
                    one_day.setTextColor(Color.parseColor("#1d84a5"));
                else
                    one_day.setTextColor(Color.parseColor("#333333"));
            }
            one_day.setText(dayinfo.getDay());
            // if(dayinfo.isInMonth()) Duedate_Dialog.dd.setDay(one_day, i,
            // mDayList);

            final int final_i = i;
            v.setOnClickListener(new OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    if (!dayinfo.isInMonth())
                        return;
                    int year = hhmmdd.get(Calendar.YEAR);
                    int month = hhmmdd.get(Calendar.MONTH) + 1;
                    String day0 = dayinfo.getDay().length() <= 1 ? "0" + dayinfo.getDay() : dayinfo.getDay();
                    String date = year + String.format("%02d", month) + day0;
                    int date_int = Integer.parseInt(date);

                    // Log.i("#","####날짜비교2/ "+MODE+"/"+DATE+"/"+date_int);

                    switch (MODE)
                    {
                        case 0:
                            // 2014-02-28 달력이 귀찮어서..막어버림
                            /*
                             * if (DATE < date_int) { EventPopupC epc = new EventPopupC(context); epc.setTitle("시작일이 종료일보다 큽니다."); epc.show(); return;
                             * } // myung 20131122 ADD 조회기간은 한달 이내로 지정 Calendar cDATE = Calendar.getInstance(); Calendar cDateInt =
                             * Calendar.getInstance(); // Log.i("????", DATE/10000 + " " + (DATE%10000)/100 + // " " + (DATE%10000)%100);
                             * cDATE.set(DATE / 10000, (DATE % 10000) / 100, (DATE % 10000) % 100); cDateInt.set(date_int / 10000, (date_int % 10000)
                             * / 100, (date_int % 10000) % 100); cDATE.add(cDATE.MONTH, -1); cDATE.add(cDATE.DATE, 1); if (cDATE.after(cDateInt)) {
                             * EventPopupC epc = new EventPopupC(context); epc.setTitle("조회기간은 한달 이내로 설정해 주세요."); epc.show(); return; }
                             */
                            break;
                        case 1:
                            // myung 20131209 ADD 신청내역 신청일자의 기간은 1개월보다 크면 안됨
                            int tempToday = Integer.parseInt(TODAY);
                            // Log.e("TODAY", TODAY);
                            // Log.e("########", "Today:"+tempToday+"/"+"input day:"+date_int);
                            if (tempToday < date_int)
                            {
                                EventPopupC epc = new EventPopupC(context);
                                epc.setTitle("종료일자는 현재일자 이후로 입력할 수 없습니다.");
                                epc.show();
                                return;
                            }

                            if (DATE > date_int)
                            {
                                EventPopupC epc = new EventPopupC(context);
                                epc.setTitle("시작일이 종료일보다 큽니다.");
                                epc.show();
                                return;
                            }
                            break;
                        case 2:
                            if (DATE > date_int)
                            {
                                EventPopupC epc = new EventPopupC(context);
                                epc.setTitle("과거 일자는 선택 할 수 없습니다.\n출고 요청일을 확인해 주세요");
                                epc.show();
                                return;
                            }
                        case 3:
                            if (DATE > date_int)
                            {
                                EventPopupC epc = new EventPopupC(context);
                                epc.setTitle("시작일이 종료일보다 큽니다.");
                                epc.show();
                                return;
                            }

                            // myung 20131209 ADD 배반차 차량이동리스트의 종료일자는 MODE=3
                            Calendar cDATE1 = Calendar.getInstance();
                            Calendar cDateInt1 = Calendar.getInstance();
                            // Log.i("????", DATE/10000 + " " + (DATE%10000)/100 +
                            // " " + (DATE%10000)%100);
                            cDATE1.set(DATE / 10000, (DATE % 10000) / 100, (DATE % 10000) % 100);
                            cDateInt1.set(date_int / 10000, (date_int % 10000) / 100, (date_int % 10000) % 100);
                            cDATE1.add(cDATE1.MONTH, 1);
                            cDATE1.add(cDATE1.DATE, -1);

                            if (cDateInt1.after(cDATE1))
                            {
                                EventPopupC epc = new EventPopupC(context);
                                epc.setTitle("조회기간은 한달 이내로 설정해 주세요.");
                                epc.show();
                                return;
                            }
                            break;
                    }

                    DayInfo di = mDayList.get(final_i);
                    String day = di.getDay().length() <= 1 ? "0" + di.getDay() : di.getDay();
                    // Log.i("###",
                    // "####"+di.getYear()+"."+di.getMonth()+"."+day);
                    anchor.setText(di.getYear() + "." + di.getMonth() + "." + day);
                    SELECTED = final_i;
                    initView();
                }
            });

            row.addView(v);
            if (i % 7 == 0)
                addView(row);
        }
    }

    public Calendar getCalender(Calendar cal, int mode)
    {
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        // Log.i("####", "#### 년월1  " + year + "/" + month);
        switch (mode)
        {
            case 0:
                month--;
                if (month < 0)
                {
                    year--;
                    month = 11;
                }
                break;
            case 1:
                month++;
                if (month >= 12)
                {
                    year++;
                    month = 0;
                }
                break;
        }
        // Log.i("####", "#### 년월2  " + year + "/" + month);

        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month);
        cal.set(Calendar.DAY_OF_MONTH, 1);

        return cal;
    }

    public void setButton(View bt)
    {
        anchor = (Button) bt;
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);
    }

    private void getCalendar(Calendar calendar)
    {
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

        if (dayOfMonth == SUNDAY)
        {
            dayOfMonth += 7;
        }
        lastMonthStartDay -= (dayOfMonth - 1) - 1;

        DayInfo day;
        for (int i = 0; i < dayOfMonth - 1; i++)
        {
            int date = lastMonthStartDay + i;
            day = new DayInfo();
            day.setDay(Integer.toString(date));
            day.setInMonth(false);
            day.setYear(last_year);
            day.setMonth(last_month);
            mDayList.add(day);
        }

        for (int i = 1; i <= thisMonthLastDay; i++)
        {
            day = new DayInfo();
            day.setDay(Integer.toString(i));
            day.setInMonth(true);
            day.setYear(this_year);
            day.setMonth(this_month);
            mDayList.add(day);
        }
        for (int i = 1; i < 42 - (thisMonthLastDay + dayOfMonth - 1) + 1; i++)
        {
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

    public void setMode(int num)
    {
        MODE = num;
    }

    public void setDate(int num)
    {
        DATE = num;

        if (DATE != Integer.MAX_VALUE)
        {
            String initDate = String.valueOf(DATE);
            int initYear = Integer.parseInt(initDate.substring(0, 4));
            int initMonth = Integer.parseInt(initDate.substring(4, 6)) - 1;
            int initDay = Integer.parseInt(initDate.substring(6, 8));
            PrintLog.Print("Cal_Custom2", initYear + "  " + initMonth + "  " + initDay);
            INIT_SELECTED = initDay;
            hhmmdd.set(initYear, initMonth, initDay);

            initView();
        }
    }
}
