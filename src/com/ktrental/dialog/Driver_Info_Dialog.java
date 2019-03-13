package com.ktrental.dialog;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;

import com.ktrental.R;
import com.ktrental.adapter.Driver_Info_Dialog_Adapter;
import com.ktrental.cm.connect.Connector.ConnectInterface;
import com.ktrental.model.TableModel;
import com.ktrental.popup.EventPopupC;

public class Driver_Info_Dialog extends DialogC implements ConnectInterface, View.OnClickListener
{
    private Window w;
    private WindowManager.LayoutParams lp;
    private Context                            context;
    private Button                             close;
    
    public Driver_Info_Dialog_Adapter dida;
    private ListView                           lv_list;
    private ArrayList<HashMap<String, String>> o_itab3;
    
    private ImageView iv_nodata;

    public Driver_Info_Dialog(Context context)
        {
        super(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.driver_info_dialog);
        w = getWindow();
        w.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN|WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        w.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        lp = (WindowManager.LayoutParams)w.getAttributes();
        lp.width = LayoutParams.MATCH_PARENT;
        lp.height = LayoutParams.MATCH_PARENT;
        w.setAttributes(lp);
        this.context = context;
        close = (Button) findViewById(R.id.driver_info_close_id);
        close.setOnClickListener(this);
        lv_list = (ListView)findViewById(R.id.driver_info_listview_id);
        
        iv_nodata = (ImageView)findViewById(R.id.list_nodata_id);
        }

    private void setList()
        {
        dida = new Driver_Info_Dialog_Adapter(context, R.layout.driver_info_dialog_row, o_itab3);
        lv_list.setAdapter(dida);
        lv_list.setOnItemClickListener(new OnItemClickListener()
            {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3)
                {
                dida.setChoiced_num(position);
                }
            });
        
        if (o_itab3.size() > 0) 
            {
            iv_nodata.setVisibility(View.GONE);
            }
        else{
            iv_nodata.setVisibility(View.VISIBLE);
            }
        }



    public Driver_Info_Dialog(Context context, ArrayList<HashMap<String, String>> o_itab3)
        {
        this(context);
        this.o_itab3 = o_itab3;
        
        setList();
        }

    @Override
    public void connectResponse(String FuntionName, String resultText, String MTYPE, int resulCode, TableModel tableModel)
        {
        hideProgress();
        if(MTYPE==null||!MTYPE.equals("S"))
            {
        	
            EventPopupC epc = new EventPopupC(context);
            epc.show(resultText); return;
            } 
        }

    @Override
    public void reDownloadDB(String newVersion) {}

    @Override
    public void onClick(View v)
        {
        switch(v.getId())
        {
        case R.id.driver_info_close_id:
            dismiss();
            break;
        }
        
        }

}
