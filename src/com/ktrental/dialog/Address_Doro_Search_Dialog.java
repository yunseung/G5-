package com.ktrental.dialog;

import android.app.ProgressDialog;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.ktrental.R;
import com.ktrental.adapter.Address_Search_Dialog_Adapter;
import com.ktrental.cm.connect.ConnectController;
import com.ktrental.cm.connect.Connector;
import com.ktrental.cm.connect.Connector.ConnectInterface;
import com.ktrental.common.DEFINE;
import com.ktrental.model.TableModel;
import com.ktrental.popup.AreaSelectPopup;
import com.ktrental.popup.QuantityPopup;

import java.util.ArrayList;
import java.util.HashMap;

import kankan.wheel.widget.OnWheelChangedListener;
import kankan.wheel.widget.WheelView;

public class Address_Doro_Search_Dialog extends DialogC implements ConnectInterface
{
    private Window w;
    private WindowManager.LayoutParams lp;
    private Context                            context;
    private Button                             close;
    private EditText                           input;
    private Button                             search;
    private ListView                           listview;
    private EditText                           num1;
    private EditText                           num2;


    private HashMap<String, ArrayList<String>> address_hash = new HashMap<String, ArrayList<String>>();
    private ArrayList<String>                  keys         = new ArrayList<String>();
    
    String                                     TABLE_NAME   = "O_ITAB1";
    String                                     ZCODE_KEY    = "CM010";
    String                                     ZCODE_VAL    = "M006";
    String                                     ZCODEH       = "ZCODEH";
    String                                     ZCODEV       = "ZCODEV";
    String                                     ZCODEVT      = "ZCODEVT";

    private Button                             btnAreaSelect;
    private AreaSelectPopup                    areaSelectPopup;
    private String                             mCityText    = "";
    private String                             mGuText      = "";
    private ConnectController                  connectController;
    private Address_Search_Dialog_Adapter      asda;
    private ProgressDialog                     pd;
    private HashMap<String, String>            mSelectedMap = null;

    
    private ImageView iv_nodata;

    public Address_Doro_Search_Dialog(Context context)
        {
        super(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.address_doro_search_dialog);
        w = getWindow();
        w.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN|WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        w.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        lp = (WindowManager.LayoutParams)w.getAttributes();
        lp.width = LayoutParams.MATCH_PARENT;
        lp.height = LayoutParams.MATCH_PARENT;
        w.setAttributes(lp);

        this.context = context;
        connectController = new ConnectController(this, context);
        close = (Button) findViewById(R.id.address_search_close_id);
        input = (EditText) findViewById(R.id.address_search_doro_edittext_id);
        search = (Button) findViewById(R.id.address_search_dialog_search_id);

        listview = (ListView) findViewById(R.id.address_search_listview_id);
        btnAreaSelect = (Button) findViewById(R.id.btn_selectArea);
        num1 = (EditText)findViewById(R.id.address_search_doro_num1_id);
        num2 = (EditText)findViewById(R.id.address_search_doro_num2_id);
        
        iv_nodata = (ImageView)findViewById(R.id.list_nodata_id);

        initData();
        setButton();
        pd = new ProgressDialog(context);
        pd.setMessage("검색중입니다.");
        
        setLocationPopup();
        }
    
    private void setLocationPopup()
        {
        View root = LayoutInflater.from(context).inflate(R.layout.area_select_layout, null, false);
        mCityText = keys.get(0);
        areaSelectPopup = new AreaSelectPopup(root, context, QuantityPopup.HORIZONTAL, R.layout.area_select_layout,
            address_hash, keys, new OnWheelChangedListener()
                {
                @Override
                public void onChanged(WheelView arg0, int arg1, int arg2)
                    {
                    switch (arg0.getId())
                        {
                        case R.id.city:
                            areaSelectPopup.setGuArray(arg2);
                            mCityText = keys.get(arg2);
                            int cityIndex = keys.indexOf(mCityText);
                            ArrayList<String> guArr = address_hash.get(keys.get(cityIndex));
                            if (guArr != null) mGuText = guArr.get(0);
                            else               mGuText = "";
                            break;
                        case R.id.gu:
                            cityIndex = keys.indexOf(mCityText);
                            guArr = address_hash.get(keys.get(cityIndex));
                            if (guArr != null) mGuText = guArr.get(arg2);
                            else               mGuText = "";
                            break;
                        default:
                            break;
                        }
                    btnAreaSelect.setText(mCityText+" "+mGuText);
                    }
            });
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
                
                String dong = "";
                String num1_str = "";
                String num2_str = "";
                Object data = input.getText();
                if(data==null) return;
                dong = data.toString();
                data = num1.getText();
                num1_str = data!=null?data.toString():" ";
                data = num2.getText();    
                num2_str = data!=null?data.toString():" ";

                String cityText = mCityText;
                if (mCityText.equals("전체"))
                    {
                    cityText = "";
                    }

                connectController.getStreetAddress(cityText, mGuText, dong, num1_str, num2_str); 
                }
            });

        listview.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3)
                {
                    if(array_hash != null && array_hash.size() > arg2){
                        mSelectedMap = array_hash.get(arg2);
                        asda.checkItem(arg2);
                    }
                }
        });
        btnAreaSelect.setOnClickListener(new View.OnClickListener()
            {
            @Override
            public void onClick(View arg0)
                {
                if(!areaSelectPopup.isShowing()) areaSelectPopup.showAsDropDown(btnAreaSelect);
                }
            });
//        btnSave.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View arg0)
//                {
//                // TODO Auto-generated method stub
//                dismiss();
//                }
//        });
        }
    ArrayList<HashMap<String, String>> array_hash;
    Connector                          connector;

    @Override
    public void connectResponse(String FuntionName, String resultText, String MTYPE, int resulCode, TableModel tableModel)
    {
        hideProgress();
        if(!"ZMO_1010_RD02".equals(FuntionName)) {
            if (MTYPE.equals("E")) {
                    connectController.duplicateLogin(context);
                    Toast.makeText(context, resultText, 0).show();
                    return;
            }


            array_hash = tableModel.getTableArray();

            if (array_hash != null) {
                if (array_hash.size() > 0) {
                    iv_nodata.setVisibility(View.GONE);
                } else {
                    iv_nodata.setVisibility(View.VISIBLE);
                }

                if (array_hash.size() > 0) pre.sendEmptyMessageDelayed(0, 100);
            }
            asda = new Address_Search_Dialog_Adapter(context, R.layout.address_search_row, array_hash);
            listview.setAdapter(asda);
            connectController.duplicateLogin(context);
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
          imm.hideSoftInputFromWindow(input.getWindowToken(), 0);
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
}
