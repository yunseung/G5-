package com.ktrental.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;

import com.ktrental.R;


public class Dialog_Custom extends Dialog
{

    private Window w;
    WindowManager.LayoutParams lp;
    private LinearLayout ll;
    private int height = 250;
    public Dialog_Custom(Context context)
        {
        super(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_custom);
        w = getWindow();
        w.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        w.setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        w.setGravity(Gravity.TOP|Gravity.LEFT);
        lp = (WindowManager.LayoutParams)w.getAttributes();
        ll = (LinearLayout)findViewById(R.id.dialog_ll_id);
        }
    
    public void setButton(Button _bt)
        {
        ll.addView(_bt);
        }

    public void showLocation(int _x, int _y)
        {
        lp.x = _x;
        lp.y = _y-height;
        w.setAttributes(lp);
        show();
        }
}
