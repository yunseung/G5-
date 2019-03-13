package com.ktrental.dialog;

import android.app.ProgressDialog;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.ktrental.R;
import com.ktrental.adapter.PartsTransfer_Parts_Dialog_Adapter;
import com.ktrental.cm.connect.ConnectController;
import com.ktrental.cm.connect.Connector;
import com.ktrental.cm.connect.Connector.ConnectInterface;
import com.ktrental.common.DEFINE;
import com.ktrental.fragment.PartsTransferFragment;
import com.ktrental.model.TableModel;
import com.ktrental.popup.AreaSelectPopup;
import com.ktrental.popup.EventPopupC;
import com.ktrental.popup.PM023_Popup;
import com.ktrental.popup.QuickAction;

import java.util.ArrayList;
import java.util.HashMap;

public class PartsTransfer_Parts_Dialog extends DialogC implements ConnectInterface, View.OnClickListener
{
    private Window w;
    private WindowManager.LayoutParams lp;
    private Context                            context;
    private Button                             close;
    private EditText                           input;
    private Button                             search;


    private HashMap<String, ArrayList<String>> address_hash = new HashMap<String, ArrayList<String>>();
    private ArrayList<String>                  keys         = new ArrayList<String>();
    
    String                                     TABLE_NAME   = "O_ITAB1";
    String                                     ZCODE_KEY    = "CM010";
    String                                     ZCODE_VAL    = "M006";
    String                                     ZCODEH       = "ZCODEH";
    String                                     ZCODEV       = "ZCODEV";
    String                                     ZCODEVT      = "ZCODEVT";


    private AreaSelectPopup                    areaSelectPopup;
    private String                             mCityText    = "";
    private String                             mGuText      = "";
    private ConnectController                  connectController;
//    private PartsTransfer_Parts_Dialog_Adapter      asda;
    private ProgressDialog                     pd;
    private HashMap<String, String>            mSelectedMap = null;

    
    private String PM023_TAG;
    private PM023_Popup ipm023popup;
    private Button  bt_group;
    private Button bt_search;
    private EditText et_parts_name;
    private ListView listview;
    private PartsTransfer_Parts_Dialog_Adapter ppda;
    private Button bt_done;
    
    private ImageView iv_nodata;
    
    public PartsTransfer_Parts_Dialog(Context context)
        {
        super(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.partstransfer_parts_dialog);
        w = getWindow();
        w.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN|WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        w.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        lp = (WindowManager.LayoutParams)w.getAttributes();
        lp.width = LayoutParams.MATCH_PARENT;
        lp.height = LayoutParams.MATCH_PARENT;
        w.setAttributes(lp);

        this.context = context;
        pd = new ProgressDialog(context);
        pd.setMessage("검색중입니다.");
        
        PM023_TAG = " ";
        ipm023popup = new PM023_Popup(context, QuickAction.VERTICAL, PartsTransferFragment.pm023_arr);
        bt_group = (Button)findViewById(R.id.partstransfer_dialog_group_id);
        bt_group.setOnClickListener(this);
        connectController = new ConnectController(this, context);
        close = (Button) findViewById(R.id.partstransfer_parts_search_close_id);
        close.setOnClickListener(this);
//        input = (EditText) findViewById(R.id.address_search_jibun_edittext_id);
        bt_search = (Button) findViewById(R.id.partstransfer_parts_search_dialog_search_id);
        bt_search.setOnClickListener(this);
        et_parts_name = (EditText)findViewById(R.id.partstransfer_parts_search_jibun_edittext_id);
        listview = (ListView) findViewById(R.id.tire_provide_company_list_id);
        bt_done = (Button) findViewById(R.id.partstransfer_parts_search_save_id);
        bt_done.setOnClickListener(this);

        iv_nodata = (ImageView)findViewById(R.id.list_nodata_id);
        
        setPM023Click();
        }

    private void setButton()
        {
        close.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v)
                {
                dismiss();
                }
        });
        search.setOnClickListener(new View.OnClickListener()
            {
            public void onClick(View v)
                {
                if(areaSelectPopup.isShowing()) areaSelectPopup.dismiss();
                
                showProgress("검색중 입니다.");
                String dong = input.getText().toString();
                String cityText = mCityText;
                if (mCityText.equals("전체"))
                    {
                    cityText = "";
                    }
                connectController.getLotNumAddress(cityText, mGuText, dong);
                }
            });

        listview.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3)
                {
                mSelectedMap = array_hash.get(arg2);
//                asda.checkItem(arg2);
                }
        });
//        btnAreaSelect.setOnClickListener(new View.OnClickListener()
//            {
//            @Override
//            public void onClick(View arg0)
//                {
//                if(!areaSelectPopup.isShowing()) areaSelectPopup.showAsDropDown(btnAreaSelect);
//                }
//            });
        }
    ArrayList<HashMap<String, String>> array_hash;
    Connector                          connector;

    @Override
    public void connectResponse(String FuntionName, String resultText, String MTYPE, int resulCode, TableModel tableModel)
        {
        hideProgress();
        if(!MTYPE.equals("S"))
            {
        	
        	connectController.duplicateLogin(mContext);
        	
            EventPopupC epc = new EventPopupC(context);
            epc.show(resultText); return;
            } 
        
        if(FuntionName.equals("ZMO_1030_RD06"))
            {
//            Log.i("#", "####"+FuntionName+"/"+resultText+"/"+MTYPE+"/"+resulCode);
            array_hash = tableModel.getTableArray();
            if(array_hash.size()>0) { iv_nodata.setVisibility(View.GONE); }
            else                  { iv_nodata.setVisibility(View.VISIBLE); }
            if(array_hash.size()>0) pre.sendEmptyMessageDelayed(0, 100);
            ppda = new PartsTransfer_Parts_Dialog_Adapter(context, R.layout.partstransfer_parts_dialog_row, array_hash);
            listview.setAdapter(ppda);
            listview.setOnItemClickListener(new OnItemClickListener()
                {
                @Override
                public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3)
                    {
                    ppda.setCheckPosition(position);
                    }
                });
            
            connectController.duplicateLogin(mContext);
            
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
          imm.hideSoftInputFromWindow(et_parts_name.getWindowToken(), 0);
          }
      };

    @Override
    public void reDownloadDB(String newVersion)
        {
        // TODO Auto-generated method stub
        }

    private void initData()
        {
        String path = context.getExternalCacheDir() + "/DATABASE/" + DEFINE.SQLLITE_DB_NAME;
        SQLiteDatabase sqlite = null;
        Cursor cursor = null;
        sqlite = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.OPEN_READONLY);
        cursor = sqlite.rawQuery("select ZCODEVT from " + TABLE_NAME
            + " where ZCODEH = '" + ZCODE_KEY + "'"
            + " order by ZCODEV asc", null);
        while (cursor.moveToNext())
            {
            int calnum = cursor.getColumnIndex(ZCODEVT);
            String zcodevt = cursor.getString(calnum);
            keys.add(zcodevt);
            }
        cursor = sqlite.rawQuery("select ZCODEV,ZCODEVT from " + TABLE_NAME
            + " where ZCODEH = '" + ZCODE_VAL + "'", null);
        while (cursor.moveToNext())
            {
            String zcodev = cursor.getString(cursor.getColumnIndex(ZCODEV));
            String zcodevt = cursor.getString(cursor.getColumnIndex(ZCODEVT));
            ArrayList<String> temp;
            if (address_hash.containsKey(zcodev))
                {
                temp = address_hash.get(zcodev);
                }
            else
                {
                temp = new ArrayList<String>();
                }
            temp.add(zcodevt);
            address_hash.put(zcodev, temp);
            }
        cursor.close();
//        sqlite.close();
        }

    public HashMap<String, String> getSelectedAddress()
        {
        return mSelectedMap;
        }

    @Override
    public void onClick(View v)
        {
        switch(v.getId())
            {
            case R.id.partstransfer_dialog_group_id: //자재그룹
                ipm023popup.show(bt_group);
                break;
            case R.id.partstransfer_parts_search_close_id: //닫기
                dismiss();
                break;
            case R.id.partstransfer_parts_search_dialog_search_id: //조회
                showProgress("조회 중입니다.");
                Object data = et_parts_name.getText();
                String str = data!=null&&!data.equals("")?data.toString():" ";
                connectController.getZMO_1030_RD06(PM023_TAG, str, " ");
                break;
            }

        }

    private void setPM023Click()
        {
        ViewGroup vg = ipm023popup.getViewGroup();
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
                    bt_group.setText(bt.getText().toString());
                    PM023_TAG = bt.getTag().toString();
                    ipm023popup.dismiss();
                    }
                });
            }
        }
    
    public PartsTransfer_Parts_Dialog_Adapter getPpda()
        {
        return ppda;
        }

    
    public void setPpda(PartsTransfer_Parts_Dialog_Adapter ppda)
        {
        this.ppda = ppda;
        }
    
    
    public ArrayList<HashMap<String, String>> getArray_hash()
        {
        return array_hash;
        }

    
    public void setArray_hash(ArrayList<HashMap<String, String>> array_hash)
        {
        this.array_hash = array_hash;
        }
}
