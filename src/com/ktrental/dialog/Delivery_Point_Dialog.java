package com.ktrental.dialog;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.ktrental.R;
import com.ktrental.adapter.Delivery_Point_Dialog_Adapter;
import com.ktrental.cm.connect.ConnectController;
import com.ktrental.cm.connect.Connector.ConnectInterface;
import com.ktrental.common.DEFINE;
import com.ktrental.model.TableModel;
import com.ktrental.popup.EventPopupC;
import com.ktrental.util.kog;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class Delivery_Point_Dialog extends DialogC implements ConnectInterface, View.OnClickListener, OnDismissListener
{
    private Window w;
    private WindowManager.LayoutParams lp;
    private Context                            context;
    private Button                             close;

    private ConnectController                  cc;
//    private ProgressDialog                     pd;
    
    private ImageView iv_nodata;
    public static Delivery_Point_Dialog_Adapter dpda;
    private ListView lv_list;
    public static ArrayList<Integer> check_list = new ArrayList<Integer>();
    public static ArrayList<HashMap<String, String>> o_itab1;
    private Button bt_resist;
    private Button bt_delete;
    
    public interface OnDismissListener {

    public abstract void onDismiss();
}
    
    private OnDismissListener mConfirmListener = null;
    public void setOnDismissListener(OnDismissListener onDismissListener)
        {
        mConfirmListener = onDismissListener;
        setOnDismissListener(this);
        }

    public Delivery_Point_Dialog(Context context)
        {
        super(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.delivery_point_dialog);
        
        w = getWindow();
        w.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN|WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        w.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        lp = (WindowManager.LayoutParams)w.getAttributes();
        lp.width = LayoutParams.MATCH_PARENT;
        lp.height = LayoutParams.MATCH_PARENT;
        w.setAttributes(lp);

        this.context = context;
        
        cc = new ConnectController(this, context);
        close = (Button) findViewById(R.id.delivery_point_close_id);
        close.setOnClickListener(this);
        
        iv_nodata = (ImageView)findViewById(R.id.list_nodata_id);
        lv_list = (ListView)findViewById(R.id.delivery_point_listview_id);
        
        bt_resist = (Button)findViewById(R.id.delivery_point_resist_id);
        bt_resist.setOnClickListener(this);
        
        bt_delete = (Button)findViewById(R.id.delivery_point_delete_id);
        bt_delete.setOnClickListener(this);
        }
    
    @Override
    public void onAttachedToWindow()
        {
        super.onAttachedToWindow();
        showProgress("조회 중 입니다."); //종료시 익셉션
        cc.getZMO_1090_RD01();
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
        
        if(FuntionName.equals("ZMO_1090_WR01"))
            {
            showProgress("조회 중 입니다.");
            cc.getZMO_1090_RD01();
            }
        
        if(FuntionName.equals("ZMO_1090_RD01"))
            {
            o_itab1 = tableModel.getTableArray();
            if(o_itab1==null||o_itab1.size()<=0) 
                {
                iv_nodata.setVisibility(View.VISIBLE);
                EventPopupC epc = new EventPopupC(context);
                epc.show("데이터가 없습니다.");
                dpda = new Delivery_Point_Dialog_Adapter(context, R.layout.delivery_point_dialog_row, new ArrayList<HashMap<String,String>>());
                lv_list.setAdapter(dpda);
                }
            else
                { 
                iv_nodata.setVisibility(View.GONE);
                int DEFAULT = 0;
                for(int i=0;i<o_itab1.size();i++)
                    {
                    if(o_itab1.get(i).get("DEFT_TYP").equals("X"))
                        {
                        DEFAULT = i;
                        }
                    }
                
                Collections.swap(o_itab1, DEFAULT, 0);
                dpda = new Delivery_Point_Dialog_Adapter(context, R.layout.delivery_point_dialog_row, o_itab1);
                
                lv_list.setOnItemClickListener(new OnItemClickListener()
                {
                @Override
                public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3)
                    {
                	kog.e("Jonathan", "Jonathan1 :: " + position);
                    dpda.setCheckPosition(position);
                    }
                });
                lv_list.setAdapter(dpda);
                setDBTable();            //디비테이블에 내용저장
                }
            
            cc.duplicateLogin(mContext);
            }
        }
    
    private void setDBTable()
        {
        //o_itab1 저장
        String path = context.getExternalCacheDir() + "/DATABASE/" + DEFINE.SQLLITE_DB_NAME;
        SQLiteDatabase sqlite = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.OPEN_READWRITE);

        try {
            sqlite.beginTransaction();
            sqlite.execSQL("DELETE FROM ADDRESS_TABLE");
            for (int i = 0; i < o_itab1.size(); i++)
                {
                HashMap<String, String> hm = o_itab1.get(i);
                ContentValues values = new ContentValues();  
                values.put("DEFT_TYP", hm.get("DEFT_TYP"));  
                values.put("ZIP_CODE", hm.get("ZIP_CODE"));
                
//                Log.i("####", "####세이브 디비테이블 시"+hm.get("CITY1"));
                values.put("CITY1", hm.get("CITY1"));
                values.put("ZSEQ", hm.get("ZSEQ"));
                values.put("STREET", hm.get("STREET"));
                values.put("DELFLG", " ");
                values.put("DELVR_ADDR", hm.get("DELVR_ADDR"));
                sqlite.insert("ADDRESS_TABLE", null, values);
                }
            sqlite.setTransactionSuccessful();
            }
        catch(SQLException e) {}
        finally
            {
            sqlite.endTransaction();
            }
//        sqlite.close();
        }

    @Override
    public void reDownloadDB(String newVersion) {}

    @Override
    public void onClick(View v)
        {
        switch(v.getId())
            {
            case R.id.delivery_point_close_id: //닫기
                dismiss();
                break;
            case R.id.delivery_point_resist_id:
                final Delivery_Resist_Dialog drd = new Delivery_Resist_Dialog(context);
                
                final EditText tv_2name = (EditText)drd.findViewById(R.id.delivery_resist_name_id);
                final CheckBox cb_check = (CheckBox)drd.findViewById(R.id.delivery_resist_check_id);
                final TextView tv_2addr1 = (TextView)drd.findViewById(R.id.delivery_resist_addr1_id);
                final TextView tv_2addr2 = (TextView)drd.findViewById(R.id.delivery_resist_addr2_id);
                final EditText tv_2addr3 = (EditText)drd.findViewById(R.id.delivery_resist_addr3_id);

                Button bt_done = (Button)drd.findViewById(R.id.delivery_resist_save_id);
                bt_done.setOnClickListener(new View.OnClickListener()
                    {
                    @Override
                    public void onClick(View v)
                        {
                        Object data = tv_2name.getText();
                        String DELVR_ADDR = data!=null&&!data.toString().equals("")?data.toString():"";
                        if(DELVR_ADDR.equals(""))
                            {
                            EventPopupC epc = new EventPopupC(context);
                            epc.show("배송지명을 입력해주세요");
                            return;
                            }
                        String DEFT_TYP = cb_check.isChecked()?"X":" ";
                        
                        data = tv_2addr1.getText();
                        String ZIP_CODE = data!=null&&!data.toString().equals("")?data.toString():"";
                        if(ZIP_CODE.equals(""))
                            {
                            EventPopupC epc = new EventPopupC(context);
                            epc.show("주소를 검색해 주세요");
                            return;
                            }
                        data = tv_2addr2.getText();
                        String CITY1 = data!=null&&!data.toString().equals("")?data.toString():"";
                        if(CITY1.equals(""))
                            {
                            EventPopupC epc = new EventPopupC(context);
                            epc.show("주소를 검색해 주세요");
                            return;
                            }
                        data = tv_2addr3.getText();
                        String STREET = data!=null&&!data.toString().equals("")?data.toString():"";
                        if(STREET.equals(""))
                            {
                            EventPopupC epc = new EventPopupC(context);
                            epc.show("상세주소를 입력해 주세요");
                            return ;
                            }

                        showProgress("등록 중 입니다.");
                        cc.getZMO_1090_WR01(getTable(DELVR_ADDR, DEFT_TYP, ZIP_CODE, CITY1, STREET), "I");
                        drd.dismiss();
                        }
                    });
                drd.show();
                break;
            case R.id.delivery_point_delete_id:
                if(check_list.size()<=0) 
                    {
                    EventPopupC epc = new EventPopupC(context);
                    epc.show("삭제할 배송지를 선택해 주세요.");
                    return;
                    }
                showProgress("삭제 중 입니다.");
                ArrayList<HashMap<String, String>> aa = getDeleteTable();
                cc.getZMO_1090_WR01(aa, "D");
                break;
            }
        }
    
    private ArrayList<HashMap<String,String>> getDeleteTable()
        {
        ArrayList<HashMap<String,String>> i_itab1 = new ArrayList<HashMap<String,String>>();
        HashMap<String, String> hm;

        for(int i=0;i<check_list.size();i++)
            {
//            Log.i("###", "####"+"배송처 삭제 체크넘"+check_list.get(i));
            hm = new HashMap<String, String>();
            int position = Integer.parseInt(check_list.get(i).toString());
            HashMap<String, String> temp = o_itab1.get(position);
            String addr = temp.get("DELVR_ADDR");
//            Log.i("###", "####"+"배송처 구분"+addr);
            hm.put("ZSEQ", temp.get("ZSEQ"));
            hm.put("DELVR_ADDR", addr);
            hm.put("DEFT_TYP", temp.get("DEFT_TYP"));
            hm.put("ZIP_CODE", temp.get("ZIP_CODE"));
            hm.put("CITY1", temp.get("CITY1"));
            hm.put("STREET", temp.get("STREET"));
            hm.put("DELFLG", "X");
            i_itab1.add(hm);
            }
        
        return i_itab1;
        }
    
    
    private ArrayList<HashMap<String,String>> getTable(String tv_2name, String cb_check, String tv_2addr1, String tv_2addr2, String tv_2addr3)
        {
        ArrayList<HashMap<String,String>> i_itab1 = new ArrayList<HashMap<String,String>>();
        HashMap<String, String> hm = new HashMap<String, String>();
        hm.put("DELVR_ADDR", tv_2name);
        hm.put("DEFT_TYP", cb_check);
        hm.put("ZIP_CODE", tv_2addr1);
        hm.put("CITY1", tv_2addr2);
        hm.put("STREET", tv_2addr3);
        i_itab1.add(hm);
        return i_itab1;
        }



    @Override
    public void onDismiss(DialogInterface dialog)
        {
        // TODO Auto-generated method stub
        
        }

    public void setOnDismissListener(com.ktrental.popup.InventoryPopup.OnDismissListener onDismissListener)
        {
        // TODO Auto-generated method stub
        
        }

    
}
