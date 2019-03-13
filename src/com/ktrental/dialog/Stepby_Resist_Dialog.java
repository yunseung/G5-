package com.ktrental.dialog;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import com.ktrental.R;
import com.ktrental.adapter.StepBy_Dialog_Adapter;
import com.ktrental.cm.connect.ConnectController2;
import com.ktrental.cm.connect.Connector.ConnectInterface;
import com.ktrental.model.TableModel;
import com.ktrental.popup.EventPopupC;
import com.ktrental.popup.ProgressPopup;


public class Stepby_Resist_Dialog extends DialogC implements ConnectInterface, View.OnClickListener
{
    private Window w;
    private WindowManager.LayoutParams lp;
    private Context                            context;
    private Button                             close;
    private ConnectController2 cc;
    private ProgressPopup pp;
    private ImageView iv_nodata;
    private ListView                           lv_list;
    public StepBy_Dialog_Adapter sda;
    private ArrayList<HashMap<String, String>> o_itab2;
    private ArrayList<HashMap<String, String>> o_itab3;
    private String bukrs;
    private HashMap<String, String> selected_driver_map;
    private String VBELN_VL;
    private ArrayList<String> mStrArryDriverInfo;
    
    public boolean SEARCH = false;

    private EditText srd_name;
    private EditText srd_reason;
    private Button bt_arrive;
    private Button bt_restart;
    private Button srd_driver_name;
    private EditText srd_driver_phone;
    private Button bt_srd_save;

    public Stepby_Resist_Dialog(Context context)
        {
        super(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.stepby_resist_dialog);
        w = getWindow();
        w.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN|WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        w.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        lp = (WindowManager.LayoutParams)w.getAttributes();
        lp.width = LayoutParams.MATCH_PARENT;
        lp.height = LayoutParams.MATCH_PARENT;
        w.setAttributes(lp);
        this.context = context;
        pp = new ProgressPopup(context);
        cc = new ConnectController2(context, this);
        close = (Button) findViewById(R.id.stepby_close_id);
        close.setOnClickListener(this);
        iv_nodata = (ImageView)findViewById(R.id.list_nodata_id);
        lv_list = (ListView)findViewById(R.id.stepby_listview_id);
        
        srd_name = (EditText)findViewById(R.id.stepby_name_id);
        srd_reason = (EditText)findViewById(R.id.stepby_reason_id);
        bt_arrive = (Button)findViewById(R.id.stepby_arrive_id);
        bt_arrive.setOnClickListener(this);
        bt_restart = (Button)findViewById(R.id.stepby_restart_id);
        bt_restart.setOnClickListener(this);
        srd_driver_name = (Button)findViewById(R.id.stepby_driver_name_id);
        srd_driver_name.setOnClickListener(this);
        srd_driver_phone = (EditText)findViewById(R.id.stepby_driver_phone_id);
        bt_srd_save = (Button)findViewById(R.id.stepby_save_id);
        bt_srd_save.setOnClickListener(this);
        
        }
    //myung 20131118 UPDATE 운전자정보 출력
    public Stepby_Resist_Dialog(Context context, ArrayList<HashMap<String, String>> o_itab2, ArrayList<HashMap<String, String>> o_itab3, String bukrs
    		, ArrayList<String> strArryDriverInfo)
//    public Stepby_Resist_Dialog(Context context, ArrayList<HashMap<String, String>> o_itab2, ArrayList<HashMap<String, String>> o_itab3, String bukrs)
        {
        this(context);
        this.o_itab2 = o_itab2;
        this.o_itab3 = o_itab3;
        this.bukrs = bukrs;
        mStrArryDriverInfo = new ArrayList<String>();
        mStrArryDriverInfo = strArryDriverInfo;
        setList();
        }
    
    private void setList()
        {
    	
        //myung 20131118 INSERT 운전자명/연락처 삽입

        srd_driver_name.setText(mStrArryDriverInfo.get(0));
        srd_driver_phone.setText(mStrArryDriverInfo.get(1));
        
        srd_name.setText("");
        srd_reason.setText("");
        sda = new StepBy_Dialog_Adapter(context, R.layout.stepby_dialog_row, o_itab2);
        lv_list.setAdapter(sda);
        lv_list.setOnItemClickListener(new OnItemClickListener()
            {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3)
                {
                sda.setCheckedPosition(position);
                }
            });
        
        if (o_itab2.size() > 0) 
            {
            iv_nodata.setVisibility(View.GONE);
            }
        else{
            iv_nodata.setVisibility(View.VISIBLE);
            }
        }

    @Override
    public void connectResponse(String FuntionName, String resultText, String MTYPE, int resulCode, TableModel tableModel)
        {
//        Log.i("#", "####다이얼로그"+FuntionName+"/"+resultText+"/"+MTYPE+"/"+resulCode);
        if(pp != null)
            pp.hide();

        if(MTYPE==null||!MTYPE.equals("S"))
            {
        	cc.duplicateLogin(mContext);
        	
            EventPopupC epc = new EventPopupC(context);
            epc.show(resultText); return;
            } 
        if(FuntionName.equals("ZMO_3140_WR04"))
            {
            SEARCH = true;
            o_itab2 = tableModel.getTableArray();
            setList();
            cc.duplicateLogin(mContext);
            
            }
        }

    @Override
    public void reDownloadDB(String newVersion) {}

    @Override
    public void onClick(View v)
        {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd HH:mm");
        switch(v.getId())
            {
            case R.id.stepby_close_id:
                dismiss();
                break;
            case R.id.stepby_save_id:
                if(selected_driver_map==null)
                    {
                    EventPopupC epc = new EventPopupC(context);
                    epc.show("운저자를 선택해 주세요");
                    return;
                    }
            
                Object data = srd_name.getText();
                String ptran = data != null && !data.toString().equals("") ? data.toString() : "";
                data = srd_reason.getText();
                String ptran2 = data != null && !data.toString().equals("") ? data.toString() : "";
                data = bt_arrive.getText();
                String eeee = data != null && !data.toString().equals("") ? data.toString() : "";
                eeee = eeee.replace(".", "").replace(":", "");
                String edate = eeee.substring(0, 8);
                String etime = eeee.substring(9);
//                Log.i("####", "####" + edate + "/" + etime + "/");
                data = bt_restart.getText();
                String ssss = data != null && !data.toString().equals("") ? data.toString() : "";
                ssss = ssss.replace(".", "").replace(":", "");
                String sdate = ssss.substring(0, 8);
                String stime = ssss.substring(9);
//                Log.i("####", "####" + sdate + "/" + stime + "/");
//                data = srd_driver_name.getText();
//                String lifnr_cd = data != null && !data.toString().equals("") ? data.toString() : "";

                HashMap<String, String> i_struct1 = new HashMap<String, String>();
                i_struct1.put("VBELN_VL", VBELN_VL);
                i_struct1.put("PTRAN", ptran);
                i_struct1.put("PTRAN2", ptran2);
                i_struct1.put("EDATE", edate);
                i_struct1.put("ETIME", etime);
                i_struct1.put("SDATE", sdate);
                i_struct1.put("STIME", stime);
                i_struct1.put("LIFNR_CD", selected_driver_map.get("LIFNR_CD"));
                
                pp.setMessage("경유지 등록 중 입니다.");
                pp.show();
                if(bukrs.equals("3000"))
                    {
                    cc.setZMO_3140_WR04(i_struct1);
                    }
                else if(bukrs.equals("3100"))
                    {
                    cc.setZMO_3200_WR04(i_struct1);
                    }
                
                break;
            case R.id.stepby_driver_name_id:
                final Driver_Info_Dialog did = new Driver_Info_Dialog(context, o_itab3);
                Button bt_done = (Button)did.findViewById(R.id.driver_info_save_id);
                bt_done.setOnClickListener(new View.OnClickListener()
                    {
                    @Override
                    public void onClick(View v)
                        {
                        if(did.dida.getChoiced_num()==Integer.MAX_VALUE) 
                            {
                            EventPopupC epc = new EventPopupC(context);
                            epc.setTitle("운전자를 선택해 주세요.");
                            epc.show();
                            return;
                            }

                        selected_driver_map = o_itab3.get(did.dida.getChoiced_num());
                        srd_driver_name.setText(selected_driver_map.get("NAME1_CD"));
                        srd_driver_phone.setText(selected_driver_map.get("TELF13"));
                        did.dismiss();
                        }
                    });
                did.show();
                break;
                
            case R.id.stepby_arrive_id:
                String arrive = sdf.format(new Date());
                bt_arrive.setText(arrive);
            break;
            case R.id.stepby_restart_id:
                String restart = sdf.format(new Date());
                bt_restart.setText(restart);
            break;
            }
        }
    
    public void show(String str, boolean bool)
        {
        VBELN_VL = str;
        bt_srd_save.setEnabled(bool);
        show();
        }

}
