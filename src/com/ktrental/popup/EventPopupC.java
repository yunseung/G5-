package com.ktrental.popup;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.ktrental.R;

public class EventPopupC extends Dialog implements View.OnClickListener
{

    TextView tv_title;
    Button bt_done;

    public EventPopupC(Context context)
        {
        super(context, R.style.Theme_Dialog);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.event_popupc);
        
        tv_title = (TextView)findViewById(R.id.tv_body);
        bt_done = (Button)findViewById(R.id.btn_ok);
        bt_done.setOnClickListener(this);
        }

    @Override
    protected void onCreate(Bundle savedInstanceState)
        {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        }

    @Override
    public void onClick(View v)
        {
        switch(v.getId())
            {
            case R.id.btn_ok: dismiss(); break;
            }
        }
    
    public void setTitle(String str)
        {
        tv_title.setText(str);
        }
    
    public void show(String str)
        {
        tv_title.setText(str);
            try {
                super.show();
            } catch (Exception e){
                e.printStackTrace();
            }
        }

}
