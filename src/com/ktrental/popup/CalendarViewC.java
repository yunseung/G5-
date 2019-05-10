package com.ktrental.popup;

import java.util.ArrayList;
import java.util.Calendar;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.ktrental.R;
import com.ktrental.common.DEFINE;
import com.ktrental.fragment.InventoryControlFragment;


public class CalendarViewC extends LinearLayout
{

    private Context context;
    ArrayList<TextView> list;
    TextView            today;
    int                 firstDay;
    int                 totDays;
    int                 iYear;
    int                 iMonth;
    Button bt_prev;
    Button bt_next;
    String current_today;
    
    public CalendarViewC(Context context, AttributeSet attrs)
        {
        super(context, attrs);
        this.context = context;

        LayoutInflater inflate=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflate.inflate(R.layout.calendar_layout,null);

//        Log.i("####", "####디스플레이1 : "+DEFINE.DISPLAY);
        Calendar calendar = Calendar.getInstance();
        iYear = calendar.get(Calendar.YEAR);
        iMonth = calendar.get(Calendar.MONTH);
        today = (TextView)v.findViewById(R.id.today);
        today.setTextColor(Color.parseColor("#555555"));
        list = new ArrayList<TextView>();
        TableLayout table = (TableLayout)v.findViewById(R.id.table);
        for (int i = 0; i < 6; i++)
            {
            TableRow tr = new TableRow(context);
            for (int j = 0; j < 7; j++)
                {
                TextView tv = new TextView(context);
                TableRow.LayoutParams lp = new TableRow.LayoutParams(30, 54);
                tv.setLayoutParams(lp);
                tv.setTextSize(22);
                tv.setTypeface(null, Typeface.BOLD);
                if (j == 0) tv.setTextColor(Color.parseColor("#fd2727"));
                else if (j == 6) 
                    {
                    tv.setTextColor(Color.parseColor("#1d84a5"));
                    }
                else tv.setTextColor(Color.parseColor("#333333"));

                tv.setGravity(Gravity.CENTER);
                tr.addView(tv);
                list.add(tv);
                }
            table.addView(tr);
            }
        table.setStretchAllColumns(true);
        table = (TableLayout)v.findViewById(R.id.week);
        table.setStretchAllColumns(true);
        setCalendar(iYear, iMonth);
        bt_prev = (Button)v.findViewById(R.id.pre);
        bt_next = (Button)v.findViewById(R.id.next);
        setButton();
        
        current_today = ""+iYear+iMonth+calendar.get(Calendar.DAY_OF_MONTH);

        setDayColor(current_today);
        addView(v);
        }
    
    public void setDayColor(String day)
        {
        for(int i=0;i<list.size();i++)
            {
            String all_day = ""+iYear+iMonth+list.get(i).getText().toString();
            list.get(i).setBackgroundColor(Color.TRANSPARENT);
            if(all_day.equals(day))
                {
                list.get(i).setBackgroundResource(R.drawable.cal_today);
                }
            }
        }
    
    public void setDayColor(String current, String day)
        {
        setDayColor(current);
        
        for(int i=0;i<list.size();i++)
            {
            String all_day = list.get(i).getText().toString();
//            list.get(i).setBackgroundColor(Color.TRANSPARENT);
            if(all_day.equals(day))
                {
                list.get(i).setBackgroundResource(R.drawable.cal_s);
                }
            }
        }
    
    public void clearDayColor()
        {
        for(int i=0;i<list.size();i++)
            {
            list.get(i).setBackgroundColor(Color.TRANSPARENT);
            }
        }
    
    private void setButton()
        {
        bt_prev.setOnClickListener(new OnClickListener()
            {
            public void onClick(View v)
                {
                iMonth--;
                if (iMonth <= 0)
                    {
                    iYear--;
                    iMonth = 11;
                    }
                setCalendar(iYear, iMonth);
                setDayColor(current_today+"");
                }
            });
        bt_next.setOnClickListener(new OnClickListener()
            {
            public void onClick(View v)
                {
                iMonth++;
                if (iMonth >= 12)
                    {
                    iMonth = 0;
                    iYear++;
                    }
                setCalendar(iYear, iMonth);
                setDayColor(current_today+"");
                }
            });
        }
    
    Calendar calendar;
    private void setCalendar(int year, int month)
        {
        calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        
        String month_02 = String.format("%02d", (month + 1));
        
        today.setText(year + "." + month_02);
        int whatDay = calendar.get(Calendar.DAY_OF_WEEK);

        int DAY = 1;

        for (int i = 0; i < list.size(); i++)
            {
            TextView tv = list.get(i);
            tv.setText("");
            }
        for (int i = whatDay - 1; i < calendar.getActualMaximum(Calendar.DAY_OF_MONTH) + whatDay - 1; i++)
            {
            TextView tv = list.get(i);
            tv.setText(DAY++ + "");
            }
        setClick();
        clearDayColor();
        }
    
    private void setClick()
        {
        for (int i = 0; i < list.size(); i++)
            {
            final TextView tv = list.get(i);
            final String day = tv.getText().toString();
            if(day==null||day.equals("")) continue;
            tv.setOnClickListener(new OnClickListener()
                {
                @Override
                public void onClick(View v)
                    {
                    int year = calendar.get(Calendar.YEAR);
                    int month = calendar.get(Calendar.MONTH)+1;
                    String date = year+String.format("%02d", month)+day;
                    int date_int = Integer.parseInt(date);
                    
//                    Log.i("#","####날짜비교2/ "+MODE+"/"+DATE+"/"+date_int);
                    if(MODE==Integer.MAX_VALUE)
                        {
                        Toast.makeText(context, "날짜 "+year+"/"+month+"/"+day, 0).show();
                        }
                    else{
                        switch(MODE)
                            {
                            case 0:
                                if(DATE<date_int)
                                    {
                                    EventPopupC epc = new EventPopupC(context);
                                    epc.show("신청일자의 시작일이 종료일보다 큽니다.");
                                    return;
                                    }
                                break;
                            case 1:
                                if(DATE>date_int)
                                    {
                                    EventPopupC epc = new EventPopupC(context);
                                    epc.show("신청일자의 시작일이 종료일보다 큽니다.");
                                    return;
                                    }
                                break;
                            }
                        }
                    
                    setDayColor(current_today+"", day);
                    String month_str = String.format("%02d", month);
                    int int_day = Integer.parseInt(day);
                    String day_str = String.format("%02d", int_day);
                    
                    button.setText(year+"."+month_str+"."+day_str);
                    calendar.set(Calendar.DAY_OF_MONTH, int_day);
                    button.setTag(calendar.getTimeInMillis());
                    }
                });
            }
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
        }
    
    InventoryControlFragment icf;
    public void setFregment(InventoryControlFragment icf)
        {
        this.icf = icf;
        }

    Button button;
    public void setButton(View anchor)
        {
        button = (Button)anchor;
        }

}
