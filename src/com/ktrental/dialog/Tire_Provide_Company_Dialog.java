package com.ktrental.dialog;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.ktrental.R;
import com.ktrental.adapter.Tire_Provide_Company_Dialog_Adapter;
import com.ktrental.cm.connect.ConnectController;
import com.ktrental.cm.connect.Connector.ConnectInterface;
import com.ktrental.model.TableModel;
import com.ktrental.popup.EventPopupC;
import com.ktrental.util.kog;

import java.util.ArrayList;
import java.util.HashMap;

public class Tire_Provide_Company_Dialog extends DialogC implements ConnectInterface, View.OnClickListener
{
    private Window w;
    private WindowManager.LayoutParams lp;
    private Context                            context;
    private ProgressDialog                     pd;
    


//    private Button                             search;
//    private HashMap<String, ArrayList<String>> address_hash = new HashMap<String, ArrayList<String>>();
//    private ArrayList<String>                  keys         = new ArrayList<String>();
//    
//    String                                     TABLE_NAME   = "O_ITAB1";
//    String                                     ZCODE_KEY    = "CM010";
//    String                                     ZCODE_VAL    = "M006";
//    String                                     ZCODEH       = "ZCODEH";
//    String                                     ZCODEV       = "ZCODEV";
//    String                                     ZCODEVT      = "ZCODEVT";
//
//
//    private AreaSelectPopup                    areaSelectPopup;
//    private String                             mCityText    = "";
//    private String                             mGuText      = "";

////    private PartsTransfer_Parts_Dialog_Adapter      asda;

//    private HashMap<String, String>            mSelectedMap = null;
//
//    
//    private String PM023_TAG;
//    private InventoryPM023Popup ipm023popup;
//    private Button  bt_group;
//    private Button bt_search;
//    private EditText et_parts_name;

//    private PartsTransfer_Parts_Dialog_Adapter ppda;
//    private Button bt_done;
    
    private Button bt_close;
    private Button bt_search;
    private EditText et_input;
    private ConnectController connectController;


    public ArrayList<HashMap<String, String>> array_hash;
    private ListView lv_list;
    private Tire_Provide_Company_Dialog_Adapter tpcda;
    public int SELECTED = -1;
    
    String type; 
    public Tire_Provide_Company_Dialog(Context context, String _type)
        {
        super(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.tire_provide_company_dialog);
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
        
//        PM023_TAG = " ";
//        ipm023popup = new InventoryPM023Popup(context, QuickAction.VERTICAL, PartsTransferFragment.pm023_arr);
//        bt_group = (Button)findViewById(R.id.partstransfer_dialog_group_id);
//        bt_group.setOnClickListener(this);

        bt_close = (Button) findViewById(R.id.tire_provide_company_close_id);
        bt_close.setOnClickListener(this);
        et_input = (EditText) findViewById(R.id.tire_provider_company_input_id);
        bt_search = (Button) findViewById(R.id.tire_provide_company_search_id);
        bt_search.setOnClickListener(this);
        connectController = new ConnectController(this, context);
//        et_parts_name = (EditText)findViewById(R.id.partstransfer_parts_search_jibun_edittext_id);
        lv_list = (ListView) findViewById(R.id.tire_provide_company_list_id);
//        bt_done = (Button) findViewById(R.id.partstransfer_parts_search_save_id);
//        bt_done.setOnClickListener(this);
//        btnSave = (Button) findViewById(R.id.address_search_save_id);
//        initData();
//        setButton();
//        setLocationPopup();
//        setPM023Click();
        type = _type;
        		
        }


    
    String LIFNR = "" ;
    public Tire_Provide_Company_Dialog(Context context, String _type, String lifnr)
    {
    	super(context);
    	requestWindowFeature(Window.FEATURE_NO_TITLE);

    	setContentView(R.layout.tire_provide_company_dialog);
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

    	//    PM023_TAG = " ";
    	//    ipm023popup = new InventoryPM023Popup(context, QuickAction.VERTICAL, PartsTransferFragment.pm023_arr);
    	//    bt_group = (Button)findViewById(R.id.partstransfer_dialog_group_id);
    	//    bt_group.setOnClickListener(this);

    	bt_close = (Button) findViewById(R.id.tire_provide_company_close_id);
    	bt_close.setOnClickListener(this);
    	et_input = (EditText) findViewById(R.id.tire_provider_company_input_id);
    	bt_search = (Button) findViewById(R.id.tire_provide_company_search_id);
    	bt_search.setOnClickListener(this);
    	connectController = new ConnectController(this, context);
    	//    et_parts_name = (EditText)findViewById(R.id.partstransfer_parts_search_jibun_edittext_id);
    	lv_list = (ListView) findViewById(R.id.tire_provide_company_list_id);
    	//    bt_done = (Button) findViewById(R.id.partstransfer_parts_search_save_id);
    	//    bt_done.setOnClickListener(this);
    	//    btnSave = (Button) findViewById(R.id.address_search_save_id);
    	//    initData();
    	//    setButton();
    	//    setLocationPopup();
    	//    setPM023Click();
    	type = _type;
    	if(lifnr != null)
    	{
    		LIFNR = lifnr;
    	}
    	
    }




//    private void setButton()
//        {
//        close.setOnClickListener(new View.OnClickListener() {
//
//            public void onClick(View v)
//                {
//                dismiss();
//                }
//        });
//        search.setOnClickListener(new View.OnClickListener()
//            {
//            public void onClick(View v)
//                {
//                if(areaSelectPopup.isShowing()) areaSelectPopup.dismiss();
//                
//                pd.show();
//                String dong = input.getText().toString();
//                String cityText = mCityText;
//                if (mCityText.equals("전체"))
//                    {
//                    cityText = "";
//                    }
//                connectController.getLotNumAddress(cityText, mGuText, dong);
//                }
//            });
//
//        listview.setOnItemClickListener(new OnItemClickListener() {
//
//            @Override
//            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3)
//                {
//                mSelectedMap = array_hash.get(arg2);
////                asda.checkItem(arg2);
//                }
//        });
////        btnAreaSelect.setOnClickListener(new View.OnClickListener()
////            {
////            @Override
////            public void onClick(View arg0)
////                {
////                if(!areaSelectPopup.isShowing()) areaSelectPopup.showAsDropDown(btnAreaSelect);
////                }
////            });
//        }

//    Connector                          connector;

    @Override
    public void connectResponse(String FuntionName, String resultText, String MTYPE, int resulCode, TableModel tableModel)
        {
//        Log.i("#", "####"+FuntionName+"/"+resultText+"/"+MTYPE+"/"+resulCode);
        hideProgress();
        if(!MTYPE.equals("S"))
            {
        	
        	connectController.duplicateLogin(context);
        	
            EventPopupC epc = new EventPopupC(context);
            epc.show(resultText);
            return;
            } 
        if(FuntionName.equals("ZMO_1060_RD02"))
            {
            pre.sendEmptyMessage(0);
            array_hash = tableModel.getTableArray();
            setList(array_hash);
            
            
            for (int i = 0; i < array_hash.size(); i++)
                {
                array_hash.get(i).get("NAME");
                }
            
            connectController.duplicateLogin(context);
            
            }
        
        }
    
    private void setList(ArrayList<HashMap<String, String>> _list)
        {
        tpcda = new Tire_Provide_Company_Dialog_Adapter(context, R.layout.tire_provide_company_dialog_row, _list);
        lv_list.setAdapter(tpcda);
        lv_list.setOnItemClickListener(new OnItemClickListener()
            {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3)
                {
                SELECTED = position;
                tpcda.setCheckPosition(position);
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
//            case R.id.partstransfer_dialog_group_id: //자재그룹
//                ipm023popup.show(bt_group);
//                break;
            case R.id.tire_provide_company_close_id: //닫기
                dismiss();
                break;
            case R.id.tire_provide_company_search_id: //조회
                
            	
            	
                Object data = et_input.getText();
                if(data==null||data.toString().equals("")||data.toString().equals(" "))
                    {
                    EventPopupC epc = new EventPopupC(context);
                    epc.show("공급업체명을 입력해 주세요");
                    return;
                    }
                showProgress("조회중 입니다.");
                kog.e("Jonathan", "공급업체 type :: " + type + " LIFNR :: " +LIFNR);
                connectController.getZMO_1060_RD02(data.toString(), type, LIFNR);
                break;
            }
        }
    
    //키보드내리기
  Handler pre = new Handler()
      {
      @Override
      public void handleMessage(Message msg)
          {
          super.handleMessage(msg);
          InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
          imm.hideSoftInputFromWindow(et_input.getWindowToken(), 0);
          }
      };
}
