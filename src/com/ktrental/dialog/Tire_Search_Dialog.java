package com.ktrental.dialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.ktrental.R;
import com.ktrental.adapter.Tire_Search_Dialog_Adapter;
import com.ktrental.beans.M029;
import com.ktrental.cm.connect.ConnectController;
import com.ktrental.cm.connect.Connector.ConnectInterface;
import com.ktrental.model.TableModel;
import com.ktrental.popup.EventPopupC;
import com.ktrental.popup.M029_Popup;
import com.ktrental.popup.QuickAction;
import com.ktrental.util.kog;

public class Tire_Search_Dialog extends DialogC implements ConnectInterface, View.OnClickListener
{
    private Window w;
    private WindowManager.LayoutParams lp;
    private Context                            context;
    private ProgressDialog                     pd;
    
    private Button bt_close;
    private Button bt_search;
//    private EditText et_input;
    private ConnectController connectController;


    public ArrayList<HashMap<String, String>> array_hash;
    private ListView lv_list;
    private Tire_Search_Dialog_Adapter tsda;
    public int SELECTED = -1;
    
    private Button bt_group;
    private EditText et_size;
    
    private M029_Popup m029_popup;
//    private PM023_Popup ipm023popup;
    private String INFNR;
    
    private String TIRE_CODE;
    boolean isSnow;
    String acceptor_lifnr;
    String TIRE_TYPE;
    
    
    public Tire_Search_Dialog(Context context, String infnr, String tire_code, boolean _isSnow, String _acceptor_lifnr, String tire_type)
        {
        super(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        this.INFNR = infnr;
        this.TIRE_CODE = tire_code;
        
        setContentView(R.layout.tire_search_dialog);
        w = getWindow();
        w.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN|WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        w.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        lp = (WindowManager.LayoutParams)w.getAttributes();
        lp.width = LayoutParams.MATCH_PARENT;
        lp.height = LayoutParams.MATCH_PARENT;
        w.setAttributes(lp);
        this.context = context;
        
        if("1".equals(tire_type))
        {//직접
        	TIRE_TYPE = "D";
        }
        else if("2".equals(tire_type))
        {//배송 
        	TIRE_TYPE = "L";
        }
        	
        
        
        
        pd = new ProgressDialog(context); pd.setCancelable(false);
        pd.setMessage("검색중입니다.");
        
        bt_close = (Button) findViewById(R.id.tire_search_close_id);
        bt_close.setOnClickListener(this);
        bt_search = (Button) findViewById(R.id.tire_search_id);
        bt_search.setOnClickListener(this);
        connectController = new ConnectController(this, context);
        lv_list = (ListView) findViewById(R.id.tire_search_list_id);
        
        bt_group = (Button)findViewById(R.id.tire_group_id);
        bt_group.setOnClickListener(this);
        bt_group.setTag("A");
        M029_TAG = "A";
        et_size = (EditText)findViewById(R.id.tire_size_id);
        et_size.setText(tire_code);
        et_size.setTag(" ");
        
        m029_popup = new M029_Popup(context, QuickAction.VERTICAL);
        isSnow = _isSnow;
        acceptor_lifnr = _acceptor_lifnr;
        setM029Click();
        }
    
    @Override
    public void onAttachedToWindow()
        {
        Object data = et_size.getText();
        String size = data!=null&&!data.toString().equals("")?data.toString():""; 
        
        // Jonathan 14.10.14 
        if(!size.equals(""))
            {
            showProgress("조회중 입니다.");
            kog.e("Jonathan", "M029 :: " + M029_TAG + " size :: " + size + " INFNR :: " + INFNR + " isSnow : " + isSnow + " acceptor_lifnr :: " + acceptor_lifnr + " 배송구분 :: " + TIRE_TYPE	);
            connectController.getZMO_1030_RD05(M029_TAG, size, INFNR, isSnow, acceptor_lifnr, TIRE_TYPE);
            }

        super.onAttachedToWindow();
        }
    
    
    public String M029_TAG;
    private void setM029Click()
        {
        ArrayList<M029> m029_arr = m029_popup.m029_arr;
        bt_group.setText(m029_arr.get(0).ZCODEVT);
        bt_group.setTag(m029_arr.get(0).ZCODEV);
        M029_TAG = m029_arr.get(0).ZCODEV;
        
        ViewGroup vg = m029_popup.getViewGroup();
        LinearLayout back = (LinearLayout)vg.getChildAt(0);
        for(int i=0;i<back.getChildCount();i++)
            {
            LinearLayout row_back = (LinearLayout)back.getChildAt(i);
            final Button bt = (Button)row_back.getChildAt(0);
            bt.setOnClickListener(new View.OnClickListener() 
                {
                @Override
                public void onClick(View v)
                    {
//                    Toast.makeText(context, bt.getTag().toString()+"테그입니다", 0).show();
                    bt_group.setText(bt.getText().toString());
                    M029_TAG = bt.getTag().toString();
                    m029_popup.dismiss();
                    }
                });
            }
        }
    
    
    //Jonathan 14.10.14
    @Override
    public void connectResponse(String FuntionName, String resultText, String MTYPE, int resulCode, TableModel tableModel)
        {
//        Log.i("#", "####"+FuntionName+"/"+resultText+"/"+MTYPE+"/"+resulCode);
        hideProgress();
        if(MTYPE==null||!MTYPE.equals("S"))
            {
        	connectController.duplicateLogin(context);
        	
        	kog.e("Jonathan", " resultText :: " + resultText);
            EventPopupC epc = new EventPopupC(context);
            epc.show(resultText);
            return;
            } 
        if(FuntionName.equals("ZMO_1030_RD05"))
            {
            array_hash = tableModel.getTableArray();
            for(int i = 0; i < array_hash.size() ; i++)
            {
            	kog.e("Jonathan", "array_hash :: " + array_hash.get(i));
            }
          
            setList(array_hash);
            connectController.duplicateLogin(context);
            
            }
        }
    
    private void setList(ArrayList<HashMap<String, String>> _list)
        {
        tsda = new Tire_Search_Dialog_Adapter(context, R.layout.tire_search_dialog_row, _list);
        lv_list.setAdapter(tsda);
        lv_list.setOnItemClickListener(new OnItemClickListener()
            {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3)
                {
                SELECTED = position;
                tsda.setCheckPosition(position);
                }
            });
        }

    @Override
    public void reDownloadDB(String newVersion) {}

    @Override
    public void onClick(View v)
        {
        switch(v.getId())
            {
            case R.id.tire_search_close_id: //닫기
                dismiss();
                break;
            case R.id.tire_search_id: //조회
                goSearch();
                break;
            case R.id.tire_group_id:
                m029_popup.show(bt_group);
                break;
            }
        }
    
    private void goSearch()
        {
    	
        Object data = et_size.getText();
        String size = data!=null&&!data.toString().equals("")?data.toString():"";
        
        showProgress("조회중 입니다.");
        connectController.getZMO_1030_RD05(M029_TAG, size, INFNR, isSnow, acceptor_lifnr, TIRE_TYPE);
        }

}
