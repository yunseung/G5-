package com.ktrental.dialog;

import android.app.Dialog;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;

import com.ktrental.R;
import com.ktrental.adapter.Rounds_Dialog_Adapter;
import com.ktrental.beans.ROUND;
import com.ktrental.common.DEFINE;

import java.util.ArrayList;

public class Rounds_Dialog extends Dialog
{

    private Context         context;
    private Button           close;
//    private Button           saved;
    private ListView         listview;
    private ArrayList<ROUND> round_list;
    private String           TABLE_NAME  = "O_ITAB1";
    private String           INVNR       = "INVNR";           // 차량번호
    private String           INGRP       = "INGRP";           // 소속 MOT 코드
    private String           INNAM       = "INNAM";           // 소속 MOT 명
    private String           ENAME       = "ENAME";           // 차량 담당자 명
    private String           HPPPHON     = "HPPPHON";        // 담당자 전화 번호
    public static int        CHOICED_NUM = Integer.MIN_VALUE;

    public Rounds_Dialog(Context context)
        {
        super(context);
        this.context = context;
        setContentView(R.layout.rounds_dialog);
        close = (Button) findViewById(R.id.rounds_close_id);
        listview = (ListView) findViewById(R.id.rounds_listview_id);

        initData();
        setButton();
        Rounds_Dialog_Adapter asda = new Rounds_Dialog_Adapter(
        context, R.layout.rounds_row, round_list);
        listview.setAdapter(asda);
        }

    private void setButton()
        {
        close.setOnClickListener(new View.OnClickListener() 
            {
            public void onClick(View v)
                {
                dismiss();
                }
            });
        listview.setOnItemClickListener(new OnItemClickListener()
            {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3)
                {
                CHOICED_NUM = arg2;
                }
            });

        }

    private void initData()
        {
        String path = context.getExternalCacheDir() + "/DATABASE/" + DEFINE.SQLLITE_DB_NAME;
        SQLiteDatabase sqlite = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.OPEN_READONLY);
        Cursor cursor = sqlite.rawQuery("select INVNR, INGRP, INNAM, ENAME, HPPPHON from " + DEFINE.CAR_MASTER_TABLE_NAME, null);
        round_list = new ArrayList<ROUND>();
        while (cursor.moveToNext())
            {
            String invnr = cursor.getString(cursor.getColumnIndex(INVNR));
            String ingrp = cursor.getString(cursor.getColumnIndex(INGRP));
            String innam = cursor.getString(cursor.getColumnIndex(INNAM));
            String ename = cursor.getString(cursor.getColumnIndex(ENAME));
            String hppphon = cursor.getString(cursor.getColumnIndex(HPPPHON));
            ROUND round = new ROUND();
            round.INVNR = invnr;
            round.INGRP = ingrp;
            round.INNAM = innam;
            round.ENAME = ename;
            round.HPPPHON = hppphon;
            round_list.add(round);
            }
        cursor.close();
//        sqlite.close();
         //출력테스
         for(int i=0;i<round_list.size();i++)
         {
         ROUND round = round_list.get(i);
//         Log.i("####", "#### 카마스"+round.INVNR);
        
         }
        }
    
    public int getChoicedNum()
        {
        return CHOICED_NUM;
        }

}
