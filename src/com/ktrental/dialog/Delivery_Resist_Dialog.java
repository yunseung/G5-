package com.ktrental.dialog;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.ktrental.R;
import com.ktrental.cm.connect.ConnectController;
import com.ktrental.cm.connect.Connector.ConnectInterface;
import com.ktrental.model.TableModel;
import com.ktrental.popup.EventPopupC;

public class Delivery_Resist_Dialog extends DialogC implements ConnectInterface, View.OnClickListener
{
    private Window w;
    private WindowManager.LayoutParams lp;
    private Context                            context;
    private ProgressDialog                     pd;
    private ConnectController cc;
    private Button bt_close;
    public ArrayList<HashMap<String, String>> array_hash;
    
//    private TextView tv_carnum;
//    private TextView tv_num;
//    private TextView tv_name1;
//    private TextView tv_name2;
//    private TextView tv_addr1;
//    private TextView tv_addr2;
//    private TextView tv_addr3;

    private EditText tv_2name;
//    private EditText tv_2phone;
    private Button tv_2addr1;
    private TextView tv_2addr2;
    private TextView tv_2addr3;
//    private EditText tv_2mot1;
//    private TextView tv_2mot2;
//    private EditText tv_2mot3;
    
    private HashMap<String, String> o_struct1;
    private HashMap<String, String> o_struct2;
    private HashMap<String, String> o_struct3;
    private HashMap<String, String> o_struct4;
    
    private Address_Dialog ad; 
    
    private String mot;
    
    private String equnr;
    
    private Button bt_resist;
    private CheckBox cb_check;

    public Delivery_Resist_Dialog(Context context)
        {
        super(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.delivery_resist_dialog);
        w = getWindow();
        w.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN|WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        w.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        lp = (WindowManager.LayoutParams)w.getAttributes();
        lp.width = LayoutParams.MATCH_PARENT;
        lp.height = LayoutParams.MATCH_PARENT;
        w.setAttributes(lp);
        this.context = context;
        
        pd = new ProgressDialog(context); pd.setCancelable(false);
        pd.setMessage("검색중입니다.");
        
        this.o_struct1 = o_struct1;
        this.o_struct2 = o_struct2;
        this.o_struct3 = o_struct3;
        this.o_struct4 = o_struct4;
        
        bt_close = (Button) findViewById(R.id.address_change_close_id);
        bt_close.setOnClickListener(this);

        cc = new ConnectController(this, context);

        tv_2name = (EditText)findViewById(R.id.delivery_resist_name_id);
        tv_2addr1 = (Button)findViewById(R.id.delivery_resist_addr1_id);
        tv_2addr1.setOnClickListener(this);
        tv_2addr2 = (TextView)findViewById(R.id.delivery_resist_addr2_id);
        tv_2addr3 = (TextView)findViewById(R.id.delivery_resist_addr3_id);
        
        
        bt_resist = (Button)findViewById(R.id.delivery_resist_save_id);
        bt_resist.setOnClickListener(this);
        
        cb_check = (CheckBox)findViewById(R.id.delivery_resist_check_id);
        
        
        
//        initPM013();
        }


    @Override
    public void connectResponse(String FuntionName, String resultText, String MTYPE, int resulCode, TableModel tableModel)
        {
//        Log.i("#", "####"+FuntionName+"/"+resultText+"/"+MTYPE+"/"+resulCode);
        hideProgress();
        if(MTYPE!=null&&!MTYPE.equals("S"))
            {
        	
        	cc.duplicateLogin(mContext);
        	
            EventPopupC epc = new EventPopupC(context);
            epc.show(resultText);
            return;
            } 
        
        
        cc.duplicateLogin(mContext);
        

        }
    
//    private String getMOT(String _mot_num)
//        {
//        String mot = null;
//        for(int i=0;i<pm013_arr.size();i++)
//            {
//            if(pm013_arr.get(i).ZCODEV.equals(_mot_num))
//                {
//                mot = pm013_arr.get(i).ZCODEVT;
//                break;
//                }
//            }
//        return mot;
//        }
    
//    String TABLE_NAME   = "O_ITAB1";
//    public ArrayList<PM013> pm013_arr;
//    private ArrayList<PM013> initPM013()
//        {
//        pm013_arr = new ArrayList<PM013>();
//        String path = context.getExternalCacheDir() + "/DATABASE/" + DEFINE.SQLLITE_DB_NAME;
//        SQLiteDatabase sqlite = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.OPEN_READONLY);
//        Cursor cursor = sqlite.rawQuery("select ZCODEV, ZCODEVT from " + TABLE_NAME + " where ZCODEH = 'PM013'", null);
//        PM013 pm;
//        while (cursor.moveToNext())
//            {
//            int calnum1 = cursor.getColumnIndex("ZCODEV");
//            int calnum2 = cursor.getColumnIndex("ZCODEVT");
//            String zcodev = cursor.getString(calnum1);
//            String zcodevt = cursor.getString(calnum2);
//            pm = new PM013();
//            pm.ZCODEV = zcodev;
//            pm.ZCODEVT = zcodevt;
//            pm013_arr.add(pm);
//            }
//        cursor.close();
//        sqlite.close();
//
//        return pm013_arr;
//        }

    @Override
    public void reDownloadDB(String newVersion) {}

    @Override
    public void onClick(View v)
        {
        switch(v.getId())
            {
            case R.id.address_change_close_id: //닫기
                dismiss();
                break;
            case R.id.delivery_resist_addr1_id: //주소검색
                ad = new Address_Dialog(context);
                Button bt_save = (Button)ad.findViewById(R.id.address_save_id);
                bt_save.setOnClickListener(new View.OnClickListener()
                    {
                    @Override
                    public void onClick(View v)
                        {
                        HashMap hm = ad.getTv_full_address();
                        if(hm==null)
                            {
                            EventPopupC epc = new EventPopupC(context);
                            epc.show("주소를 선택해주세요.");
                            return;
                            }
                        if(hm.size()<=0) 
                            {
                            EventPopupC epc = new EventPopupC(context);
                            epc.show("주소를 정제해 주세요");
                            return;
                            }
                        String zip_code = hm.get("POST_CODE1").toString();
                        
                        tv_2addr1.setText(zip_code);
                        tv_2addr2.setText(hm.get("CITY1").toString());
                        tv_2addr3.setText(hm.get("STREET").toString());
                        ad.dismiss();

                        }
                    });
                ad.show();
                break;
                
//            case R.id.delivery_resist_save_id:
//                pd.show();
//                Message msg = KtRentalApplication.timeout.obtainMessage(0, 0, 0, pd);
//                KtRentalApplication.timeout.sendMessageDelayed(msg, 30000);
//                cc.getZMO_1090_WR01(getTable());
//                break;
            }
        }

    
//    private ArrayList<HashMap<String,String>> getTable()
//        {
//        ArrayList<HashMap<String,String>> i_itab1 = new ArrayList<HashMap<String,String>>();
//        HashMap<String, String> hm = new HashMap<String, String>();
//        Object data;
//        String str;
//        
//        data = tv_2name.getText();
//        str = data!=null&&!data.toString().equals("")?data.toString():"";
//        if(str.equals(""))
//            {
//            Toast.makeText(context, "배송지명을 입력해주세요", 0).show(); return null;
//            }
//        hm.put("DELVR_ADDR", str);
//        hm.put("DEFT_TYP", cb_check.isChecked()?"X":" ");
//        
//        data = tv_2addr1.getText();
//        str = data!=null&&!data.toString().equals("")?data.toString():"";
//        if(str.equals(""))
//            {
//            Toast.makeText(context, "주소를 검색해 주세요", 0).show(); return null;
//            }
//        hm.put("ZIP_CODE", str);
//        data = tv_2addr2.getText();
//        str = data!=null&&!data.toString().equals("")?data.toString():"";
//        if(str.equals(""))
//            {
//            Toast.makeText(context, "주소를 검색해 주세요", 0).show(); return null;
//            }
//        hm.put("CITY1", str);
//        data = tv_2addr3.getText();
//        str = data!=null&&!data.toString().equals("")?data.toString():"";
//        if(str.equals(""))
//            {
//            Toast.makeText(context, "상세주소를 입력해 주세", 0).show(); return null;
//            }
//        hm.put("STREET", str);
//        i_itab1.add(hm);
//        return i_itab1;
//        }
    
    public String getEqunr()
        {
        return equnr;
        }

    
    public void setEqunr(String equnr)
        {
        this.equnr = equnr;
        }
    
    public String getDrivn()
        {
        Object data = tv_2name.getText();
        return data!=null||!data.toString().equals("")?data.toString():"";
        }
    
//    public String getTel_No()
//        {
//        Object data = tv_2phone.getText();
//        return data!=null||!data.toString().equals("")?data.toString():"";
//        }
    
    public String getPost()
        {
        Object data = tv_2addr1.getText();
        return data!=null||!data.toString().equals("")?data.toString():"";
        }
    
    public String getCity1()
        {
        HashMap hm = ad.getTv_full_address();
        return hm.get("CITY1").toString();
        }
    
    public String getStreet()
        {
        HashMap hm = ad.getTv_full_address();
        return hm.get("STREET").toString();
        }
    
    

    //키보드내리기
//  Handler pre = new Handler()
//      {
//      @Override
//      public void handleMessage(Message msg)
//          {
//          super.handleMessage(msg);
//          InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
//          imm.hideSoftInputFromWindow(et_input.getWindowToken(), 0);
//          }
//      };
    
}
