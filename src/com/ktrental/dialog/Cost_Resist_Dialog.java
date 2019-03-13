package com.ktrental.dialog;

import java.text.NumberFormat;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.ktrental.R;
import com.ktrental.popup.InventoryPopup;
import com.ktrental.popup.QuantityPopup;

public class Cost_Resist_Dialog extends DialogC implements View.OnClickListener
{
    private Window w;
    private WindowManager.LayoutParams lp;
    private Context                            context;
    private Button bt_close;
    
    private InventoryPopup ip; 
    
    private TextView tv1_1;
    private TextView tv1_2;
    private TextView tv1_3;
    private TextView tv2_1;
    
    private TextView tv3_1;
    private TextView tv3_2;
    private TextView tv3_3;
    private TextView tv_sum;

    public Cost_Resist_Dialog(Context context)
        {
        super(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.cost_resist_dialog);
        w = getWindow();
        w.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN|WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        w.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        lp = (WindowManager.LayoutParams)w.getAttributes();
        lp.width = LayoutParams.MATCH_PARENT;
        lp.height = LayoutParams.MATCH_PARENT;
        w.setAttributes(lp);
        this.context = context;
        bt_close = (Button) findViewById(R.id.cost_close_id);
        bt_close.setOnClickListener(this);
        
        ip = new InventoryPopup(context, QuantityPopup.HORIZONTAL, R.layout.inventory_popup, QuantityPopup.TYPE_MONEY);
        
        tv1_1 = (TextView)findViewById(R.id.cost_input1_id);
        tv1_2 = (TextView)findViewById(R.id.cost_input2_id);    
        tv1_3 = (TextView)findViewById(R.id.cost_input3_id);    tv1_3.setOnClickListener(this);
        tv2_1 = (TextView)findViewById(R.id.cost_input4_id);    tv2_1.setOnClickListener(this);
                                                                      
        tv3_1 = (TextView)findViewById(R.id.cost_cost1_id);     tv3_1.setOnClickListener(this);
        tv3_2 = (TextView)findViewById(R.id.cost_cost2_id);     tv3_2.setOnClickListener(this);
        tv3_3 = (TextView)findViewById(R.id.cost_cost3_id);     tv3_3.setOnClickListener(this);
        tv_sum = (TextView)findViewById(R.id.cost_cost4_id);    
        }

    public Cost_Resist_Dialog(Context context, String str)
        {
        this(context);
        tv1_1.setText(str); 
        }

    @Override
    public void onClick(View v)
        {
        ViewGroup vg;
        Button done;
        switch(v.getId())
            {
            case R.id.cost_close_id: //닫기
                dismiss();
                break;
            case R.id.cost_input3_id: //주유량
                vg = ip.getViewGroup();
                final TextView input3 = (TextView) vg.findViewById(R.id.inventory_bt_input);
                done = (Button) vg.findViewById(R.id.inventory_bt_done);
                done.setOnClickListener(new View.OnClickListener()
                    {
                    @Override
                    public void onClick(View v) 
                        {
                        String num = input3.getText().toString();
                        tv1_3.setText(num+"L");
                        ip.setInput("CLEAR", true);
                        sum();
                        ip.dismiss();
                        }
                    });
                ip.show(tv1_3);
                break;
            case R.id.cost_input4_id: //주유금액
                vg = ip.getViewGroup();
                final TextView input4 = (TextView) vg.findViewById(R.id.inventory_bt_input);
                done = (Button) vg.findViewById(R.id.inventory_bt_done);
                done.setOnClickListener(new View.OnClickListener()
                    {
                    @Override
                    public void onClick(View v) 
                        {
                        String num = input4.getText().toString();
                        tv2_1.setText(num+"원");
                        ip.setInput("CLEAR", true);
                        sum();
                        ip.dismiss();
                        }
                    });
                ip.show(tv2_1);
                break;
            case R.id.cost_cost1_id: //세차비
                vg = ip.getViewGroup();
                final TextView cost1 = (TextView) vg.findViewById(R.id.inventory_bt_input);
                done = (Button) vg.findViewById(R.id.inventory_bt_done);
                done.setOnClickListener(new View.OnClickListener()
                    {
                    @Override
                    public void onClick(View v) 
                        {
                        String num = cost1.getText().toString();
                        tv3_1.setText(num+"원");
                        ip.setInput("CLEAR", true);
                        sum();
                        ip.dismiss();
                        }
                    });
                ip.show(tv3_1);
                break;
            case R.id.cost_cost2_id: //주차비
                vg = ip.getViewGroup();
                final TextView cost2 = (TextView) vg.findViewById(R.id.inventory_bt_input);
                done = (Button) vg.findViewById(R.id.inventory_bt_done);
                done.setOnClickListener(new View.OnClickListener()
                    {
                    @Override
                    public void onClick(View v) 
                        {
                        String num = cost2.getText().toString();
                        tv3_2.setText(num+"원");
                        ip.setInput("CLEAR", true);
                        sum();
                        ip.dismiss();
                        }
                    });
                ip.show(tv3_2);
                break;
            case R.id.cost_cost3_id: //통행요금
                vg = ip.getViewGroup();
                final TextView cost3 = (TextView) vg.findViewById(R.id.inventory_bt_input);
                done = (Button) vg.findViewById(R.id.inventory_bt_done);
                done.setOnClickListener(new View.OnClickListener()
                    {
                    @Override
                    public void onClick(View v) 
                        {
                        String num = cost3.getText().toString();
                        tv3_3.setText(num+"원");
                        ip.setInput("CLEAR", true);
                        sum();
                        ip.dismiss();
                        }
                    });
                ip.show(tv3_3);
                break;
            }
        }
    
    public void sum()
        {
        String quantity_str = tv1_3.getText().toString().replace(",", "").replace("L", "");
        String cost_str = tv2_1.getText().toString().replace(",", "").replace("원", "");
        
        if(quantity_str.equals("")) quantity_str = "0";
        if(cost_str.equals("")) cost_str = "0";
        
        int quantity = Integer.parseInt(quantity_str);
        int cost = Integer.parseInt(cost_str);
        int each = 0;
        
        if(quantity!=0&&cost!=0)
            {
            each = cost/quantity;
            }
        
        NumberFormat nf = NumberFormat.getInstance();
        String mile = nf.format(each);
        tv1_2.setText(mile+"원");
        
        String wash_str = tv3_1.getText().toString().replace(",", "").replace("원", "");
        String park_str = tv3_2.getText().toString().replace(",", "").replace("원", "");
        String pass_str = tv3_3.getText().toString().replace(",", "").replace("원", "");
        
        if(wash_str.equals("")) wash_str = "0";
        if(park_str.equals("")) park_str = "0";
        if(pass_str.equals("")) pass_str = "0";
        
        int wash = Integer.parseInt(wash_str);
        int park = Integer.parseInt(park_str);
        int pass = Integer.parseInt(pass_str);
        int sum = cost+wash+park+pass;
        tv_sum.setText(nf.format(sum)+"원");
        }
}
