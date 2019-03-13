package com.ktrental.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.telephony.PhoneNumberUtils;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ktrental.R;
import com.ktrental.adapter.Inventory_Request_List_Adapter;
import com.ktrental.adapter.Inventory_Request_List_Detail_Adapter;
import com.ktrental.adapter.Inventory_Search_Left_Adapter;
import com.ktrental.adapter.Inventory_Search_Right_Adapter;
import com.ktrental.beans.PARTS_SEARCH;
import com.ktrental.beans.PM023;
import com.ktrental.beans.PM073;
import com.ktrental.cm.connect.ConnectController;
import com.ktrental.cm.connect.Connector;
import com.ktrental.cm.connect.Connector.ConnectInterface;
import com.ktrental.common.DEFINE;
import com.ktrental.common.KtRentalApplication;
import com.ktrental.dialog.Delivery_Point_Dialog;
import com.ktrental.model.LoginModel;
import com.ktrental.model.TableModel;
import com.ktrental.popup.DatepickPopup2;
import com.ktrental.popup.EventPopupC;
import com.ktrental.popup.InventoryPopup;
import com.ktrental.popup.InventoryPopup.OnDismissListener;
import com.ktrental.popup.PM023_Popup;
import com.ktrental.popup.Popup_Window_M028;
import com.ktrental.popup.Popup_Window_PM023;
import com.ktrental.popup.QuantityPopup;
import com.ktrental.popup.QuickAction;
import com.ktrental.util.OnChangeFragmentListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;

@SuppressLint("ValidFragment")
public class InventoryControlFragment extends BaseFragment implements OnClickListener, ConnectInterface
{

    private Activity                              activity;
    private Context                               context;
    private RadioGroup                            tabgroup;
    private RadioButton[]                         tabrb            = new RadioButton[2];
    private LinearLayout[]                        tab              = new LinearLayout[2];
    private final int                             PARTS_REQUEST    = 2336267;
    private final int                             REQUEST_LIST     = 9876234;
    private Button                                bt_search;
    private ArrayList<PARTS_SEARCH>               parts_search_arr;
    private Inventory_Search_Left_Adapter         isa;
    private ListView                              lv_parts_search;
    private InventoryPopup                        mPopupWindow;
    private PM023_Popup                           ipm023popup;
    private Popup_Window_M028                     pwM028;
    private Button                                bt_m028;
    private String                                M028_TAG;
    private EditText                              et_pm023;
    private TextView                              tv_phone;
    private String                                PHONE_NUM;
    private RadioGroup                            rg_delivery;
    private RadioButton[]                         rb_in_out        = new RadioButton[2];
    private String                                DELIVERY         = "in";
    private String                                DELIVERY_DETAIL  = "in";
    private EditText                              et_etc;
    private Button                                bt_address;
    private EditText                              et_address;
    private String                                ADDRESS_STR;                              ;
//    private Button                                bt_reset;
    private Button                                bt_parts_request;
    private Button                                bt_datepick1;
    private Button                                bt_datepick2;
    private DatepickPopup2                        idatepickpopup;
    public static Button                          bt_request_delete;
    private Button                                bt_request_getlist;
    private ListView                              lv_request_list;
    private ListView                              lv_request_list_detail;
    private Inventory_Request_List_Detail_Adapter irlda;
    private Button                                bt_detail_delete;
    private int                                   DETAIL_SELECTED;
    private Button                                bt_detail_modity;
    private EditText                              et_detail_etc;
    private RadioGroup                            rg_detail_delivery;
    private RadioButton[]                         rb_detail_in_out = new RadioButton[2];
    private TextView                              tv_detail_phone;
    private FrameLayout                           fl_editmode_layout;
    private Button                                bt_editmode;
    private RelativeLayout[]                      editmode         = new RelativeLayout[2];
    private Button                                bt_edit_cancel;
    private ImageView                             iv_nodata1;
    private ImageView                             iv_nodata2;
    private ImageView                             iv_nodata3;
    private Popup_Window_PM023                    pwPM023;
    private Button                                bt_address_detail;
    private TextView                              tv_address_detail;
    private String                                ZIP_CODE;
    private String                                CITY1;
    private String                                STREET;
    private String                                ZIP_CODE_D;
    private String                                CITY1_D;
    private String                                STREET_D;
    private Delivery_Point_Dialog                 dpd;
    private ListView                              lv_right;
    
    
    private ConnectController cc;

    private ArrayList<PARTS_SEARCH> BASE_PARTS_ITEMS;
    private Inventory_Search_Right_Adapter isra;
    private ImageView iv_right_nodata;
    
    private TextView tv_sort1, tv_sort2;
    
    private TextView tv_left_sort;

    public InventoryControlFragment(){
    }

    public InventoryControlFragment(String className, OnChangeFragmentListener changeFragmentListener)
    {
    super(className, changeFragmentListener);
    }

    @Override
    public void onAttach(Activity activity)
        {
        context = activity;
        super.onAttach(activity);
        }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
        Bundle savedInstanceState)
        {
        connector = new Connector(this, context);
        mPopupWindow = new InventoryPopup(context, QuantityPopup.HORIZONTAL, R.layout.inventory_popup, QuantityPopup.TYPE_NOMARL_NUMBER);
//        mPopupWindow.setOnDismissListener();
        ipm023popup = new PM023_Popup(context, QuickAction.HORIZONTAL, initPM023());
        
        pwM028 = new Popup_Window_M028(context);
        dpd = new Delivery_Point_Dialog(context);
        
        cc = new ConnectController(this, context);

        dpd.setOnDismissListener(new DialogInterface.OnDismissListener() {
        
        @Override
        public void onDismiss(DialogInterface dialog)
            {
//            Log.i("####", "#### 디스미스");
            getAddressTable();
            }
        });

        pwPM023 = new Popup_Window_PM023(context);
        
        setM028Click();
        View v = inflater.inflate(R.layout.inventory_control_layout, null);
        tabgroup = (RadioGroup) v.findViewById(R.id.inventory_tab_group_id);
        tabrb[0] = (RadioButton) v.findViewById(R.id.inventory_tab1_id);
        tabrb[1] = (RadioButton) v.findViewById(R.id.inventory_tab2_id);
        tab[0] = (LinearLayout) v.findViewById(R.id.inventory_partsrequest_layout_id);
        tab[1] = (LinearLayout) v.findViewById(R.id.inventory_requestlist_layout_id);
        bt_search = (Button) v.findViewById(R.id.inventory_parts_search_id);
        bt_search.setOnClickListener(this);
        lv_parts_search = (ListView) v.findViewById(R.id.inventory_search_list_id);
        bt_m028 = (Button) v.findViewById(R.id.inventory_pm023_id);
        bt_m028.setText("전체");
        M028_TAG = " ";
        bt_m028.setOnClickListener(this);
        et_pm023 = (EditText) v.findViewById(R.id.inventory_pm023_partsname_id);
        tv_phone = (TextView) v.findViewById(R.id.inventory_phone_id);
        tv_phone.setOnClickListener(this);
        rg_delivery = (RadioGroup) v
            .findViewById(R.id.inventory_delivery_rg_id);
        rb_in_out[0] = (RadioButton) v
            .findViewById(R.id.inventory_delivery_in_id);
        rb_in_out[1] = (RadioButton) v
            .findViewById(R.id.inventory_delivery_out_id);
        et_etc = (EditText) v.findViewById(R.id.inventory_etc_text_id);
        bt_address = (Button) v.findViewById(R.id.inventory_address_popup_id);
        bt_address.setOnClickListener(this);
        et_address = (EditText) v.findViewById(R.id.inventory_address_id);
        // bt_address.setText(addr_arr.get(0).TITLE);
        // et_address.setText(addr_arr.get(0).ADDRESS);
//        bt_reset = (Button) v.findViewById(R.id.inventory_reset_id);
//        bt_reset.setOnClickListener(this);
        bt_parts_request = (Button) v
            .findViewById(R.id.inventory_parts_request_id);
        bt_parts_request.setOnClickListener(this);
        bt_datepick1 = (Button) v
            .findViewById(R.id.inventory_maintenance_datepick1_id);
        bt_datepick2 = (Button) v
            .findViewById(R.id.inventory_maintenance_datepick2_id);
        bt_datepick1.setOnClickListener(this);
        bt_datepick2.setOnClickListener(this);
        setDateButton();
        bt_request_delete = (Button) v.findViewById(R.id.inventory_request_delete_id);
        bt_request_getlist = (Button) v.findViewById(R.id.inventory_request_getlist_id);
        bt_request_delete.setOnClickListener(this);
        bt_request_getlist.setOnClickListener(this);
        bt_request_delete.setEnabled(false);
        lv_request_list = (ListView) v
            .findViewById(R.id.inventory_request_list_id);
        lv_request_list_detail = (ListView) v
            .findViewById(R.id.inventory_request_list_detail_lv_id);
        bt_detail_delete = (Button) v
            .findViewById(R.id.inventory_detail_delete_id);
        bt_detail_delete.setOnClickListener(this);
        bt_detail_modity = (Button) v
            .findViewById(R.id.inventory_detail_modify_id);
        bt_detail_modity.setOnClickListener(this);
        et_detail_etc = (EditText) v.findViewById(R.id.inventory_detail_etc_id);
        rg_detail_delivery = (RadioGroup) v
            .findViewById(R.id.inventory_detail_radio_id);
        rb_detail_in_out[0] = (RadioButton) v.findViewById(R.id.detail_in_id);
        rb_detail_in_out[1] = (RadioButton) v.findViewById(R.id.detail_out_id);
        tv_detail_phone = (TextView) v
            .findViewById(R.id.inventory_detail_phone_id);
        tv_detail_phone.setOnClickListener(this);
        bt_editmode = (Button) v
            .findViewById(R.id.inventory_detail_editmode_id);
        bt_editmode.setOnClickListener(this);
        bt_edit_cancel = (Button) v
            .findViewById(R.id.inventory_request_delete_cancel_id);
        bt_edit_cancel.setOnClickListener(this);
        fl_editmode_layout = (FrameLayout) v
            .findViewById(R.id.inventory_request_list_delete_layout_id);
        editmode[0] = (RelativeLayout) v
            .findViewById(R.id.inventory_request_list_delete_show_id);
        editmode[1] = (RelativeLayout) v
            .findViewById(R.id.inventory_request_list_delete_hide_id);
        setEditModeGone();
        iv_nodata1 = (ImageView) v.findViewById(R.id.list_nodata_id1);
        iv_nodata2 = (ImageView) v.findViewById(R.id.list_nodata_id2);
        iv_nodata3 = (ImageView) v.findViewById(R.id.list_nodata_id3);
        bt_address_detail = (Button) v
            .findViewById(R.id.inventory_detail_address_popup_id);
        bt_address_detail.setOnClickListener(this);
        tv_address_detail = (TextView) v
            .findViewById(R.id.inventory_detail_address_id);
        lv_right = (ListView)v.findViewById(R.id.inventory_listview_right_id);
        iv_right_nodata = (ImageView)v.findViewById(R.id.inventory_nodata_right_id);
        
        tv_sort1 = (TextView)v.findViewById(R.id.inventory_right_sort1_id); tv_sort1.setOnClickListener(this);
        tv_sort2 = (TextView)v.findViewById(R.id.inventory_right_sort2_id); tv_sort2.setOnClickListener(this);
        
        tv_left_sort = (TextView)v.findViewById(R.id.inventory_left_sort2_id); tv_left_sort.setOnClickListener(this);
        
            init();
        setTab();
        initPM073();
        return v;
        }

    private void init()
        {
        String phoneNumber = "";
        try
            {
            phoneNumber = KtRentalApplication.getInstance().getPhoneNumber();
            }
        catch(Exception e)
            {
            e.printStackTrace();
            }
        tv_phone.setText(phoneNumber);
        tv_detail_phone.setText(phoneNumber);
        }

    @Override
    public void onHiddenChanged(boolean hidden)
        {
        super.onHiddenChanged(hidden);
        if (hidden) { return; }
        getAddressTable();
        setDelivery();
        // super.onHiddenChanged(hidden);
        lv_parts_search.setAdapter(null);
        lv_request_list.setAdapter(null);
        lv_request_list_detail.setAdapter(null);
        tabrb[0].setChecked(true);
        lv_right.setAdapter(null);
        BASE_PARTS_ITEMS = new ArrayList<PARTS_SEARCH>();
        
        NOW_LIST_ARR = new ArrayList<PARTS_SEARCH>();
        isra = new Inventory_Search_Right_Adapter(context, R.layout.inventory_search_right_row, NOW_LIST_ARR);
        lv_right.setAdapter(isra);
        
        bt_m028.setText("전체");
        M028_TAG = " ";
        }

    @Override
    public void onResume()
        {
        super.onResume();
        // Log.i("####", "####onResume");
        }
    private HashMap<String, String> ADDRESS_TABLE_MAP;
    private HashMap<String, String> ADDRESS_TABLE_MAP_OUT;

    private void getAddressTable()
        {
        String path = context.getExternalCacheDir() + "/DATABASE/" + DEFINE.SQLLITE_DB_NAME;
        SQLiteDatabase sqlite = SQLiteDatabase.openDatabase(path, null,
                                                            SQLiteDatabase.OPEN_READWRITE);
        sqlite.execSQL("CREATE TABLE IF NOT EXISTS ADDRESS_TABLE (DEFT_TYP text, ZIP_CODE text, CITY1 text, ZSEQ text, STREET text, DELFLG text, DELVR_ADDR text)");
        Cursor cursor = sqlite.rawQuery("SELECT * FROM " + "ADDRESS_TABLE",
                                        null);
        ADDRESS_TABLE_MAP = new HashMap<String, String>();
        ArrayList<HashMap<String, String>> arr = new ArrayList<HashMap<String, String>>();
        while (cursor.moveToNext())
            {
            HashMap<String, String> hm = new HashMap<String, String>();
            for (int i = 0; i < cursor.getColumnCount(); i++)
                {
                hm.put(cursor.getColumnName(i), cursor.getString(i));
                }
            arr.add(hm);
            }
        for (int i = 0; i < arr.size(); i++)
            {
            HashMap<String, String> hm = arr.get(i);
            if (hm.get("DEFT_TYP").equals("X"))
                {
                ADDRESS_TABLE_MAP = hm;
                }
            }
        
        
        
        cursor.close();
//        sqlite.close();
        }

    public String getPM073String(String code)
        {
        String str = "준비";
        for (int i = 0; i < pm073_arr.size(); i++)
            {
            if (pm073_arr.get(i).ZCODEV.equals(code))
                {
                str = pm073_arr.get(i).ZCODEVT;
                break;
                }
            }
        return str;
        }
    public ArrayList<PM073> pm073_arr;

    private ArrayList<PM073> initPM073()
        {
        pm073_arr = new ArrayList<PM073>();
        String path = context.getExternalCacheDir() + "/DATABASE/"
            + DEFINE.SQLLITE_DB_NAME;
        SQLiteDatabase sqlite = SQLiteDatabase.openDatabase(path, null,
                                                            SQLiteDatabase.OPEN_READONLY);
        Cursor cursor = sqlite.rawQuery("select ZCODEV, ZCODEVT from "
            + TABLE_NAME + " where ZCODEH = 'PM073'", null);
        PM073 pm;
        while (cursor.moveToNext())
            {
            int calnum1 = cursor.getColumnIndex("ZCODEV");
            int calnum2 = cursor.getColumnIndex("ZCODEVT");
            String zcodev = cursor.getString(calnum1);
            String zcodevt = cursor.getString(calnum2);
            pm = new PM073();
            pm.ZCODEV = zcodev;
            pm.ZCODEVT = zcodevt;
            pm073_arr.add(pm);
            }
        cursor.close();
//        sqlite.close();
        return pm073_arr;
        }

    private void setEditVisible(int num)
        {
        fl_editmode_layout.setVisibility(View.VISIBLE);
        editmode[0].setVisibility(View.GONE);
        editmode[1].setVisibility(View.GONE);
        editmode[num].setVisibility(View.VISIBLE);
        switch (num)
            {
            case 0:
                lv_request_list.setOnItemClickListener(new OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> av, View v,
                        int position, long arg3)
                        {
                        getRequestListDetail(request_list_arr.get(position).BANFN);
                        DETAIL_SELECTED = position;
                        irla.setPosition(position);
                        }
                });
                break;
            case 1:
                lv_request_list.setOnItemClickListener(new OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> av, View v,
                        int position, long arg3)
                        {
                        final int final_position = position;
                        // 체크안되있을경우
                        if (!Inventory_Request_List_Adapter.checked_items
                            .contains(position))
                            {
                            Inventory_Request_List_Adapter.checked_items
                                .add(position);
                            }
                        // 체크되있을경우
                        else
                            {
                            Inventory_Request_List_Adapter.checked_items
                                .remove(Inventory_Request_List_Adapter.checked_items
                                    .indexOf(position));
                            }
                        irla.notifyDataSetChanged();
                        }
                });
                break;
            }
        if (num == 0) Inventory_Request_List_Adapter.EDITMODE = false;
        else Inventory_Request_List_Adapter.EDITMODE = true;
        if (irla != null)
            irla.notifyDataSetChanged();
        }

    private void setEditModeGone()
        {
        setEditVisible(0);
        fl_editmode_layout.setVisibility(View.GONE);
        }

    private void setDelivery()
        {
        DELIVERY = "I";
        rg_delivery.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId)
                {
                switch (checkedId)
                    {
                    case R.id.inventory_delivery_in_id:
                        DELIVERY = "I";
                        bt_address.setEnabled(false);
                        bt_address.setTextColor(Color.GRAY);
                        setAddressTable(0);
                        break;
                    case R.id.inventory_delivery_out_id:
                        DELIVERY = "T";
                        bt_address.setEnabled(true);
                        bt_address.setTextColor(Color.BLACK);
                        if (ADDRESS_TABLE_MAP_OUT == null)
                            {
                            bt_address.setText("주소선택");
                            et_address.setText("");
                            }
                        else
                            {
                            bt_address.setText(ADDRESS_TABLE_MAP_OUT
                                .get("DELVR_ADDR"));
                            String add = "["
                                + ADDRESS_TABLE_MAP_OUT.get("ZIP_CODE")
                                + "] " + ADDRESS_TABLE_MAP_OUT.get("CITY1")
                                + " " + ADDRESS_TABLE_MAP_OUT.get("STREET");
                            et_address.setText(add);
                            }
                        break;
                    }
                }
        });
        rb_in_out[0].setChecked(true);
        DELIVERY_DETAIL = "I";
        rg_detail_delivery
            .setOnCheckedChangeListener(new OnCheckedChangeListener() {

                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId)
                    {
                    switch (checkedId)
                        {
                        case R.id.detail_in_id:
                            DELIVERY_DETAIL = "I";
                            bt_address_detail.setTextColor(Color.GRAY);
                            bt_address_detail.setEnabled(false);
                            setAddressTable(1);
                            break;
                        case R.id.detail_out_id:
                            DELIVERY_DETAIL = "T";
                            bt_address_detail.setTextColor(Color.BLACK);
                            bt_address_detail.setEnabled(true);
                            if (ADDRESS_TABLE_MAP_OUT == null)
                                {
                                bt_address.setText("주소선택");
                                et_address.setText("");
                                }
                            else
                                {
                                bt_address.setText(ADDRESS_TABLE_MAP_OUT
                                    .get("DELVR_ADDR"));
                                String add = "["
                                    + ADDRESS_TABLE_MAP_OUT
                                        .get("ZIP_CODE") + "] "
                                    + ADDRESS_TABLE_MAP_OUT.get("CITY1")
                                    + " "
                                    + ADDRESS_TABLE_MAP_OUT.get("STREET");
                                et_address.setText(add);
                                }
                            break;
                        }
                    }
            });
        rb_detail_in_out[0].setChecked(true);
        }

    private void setAddressTable(int mode)
        {
//        Log.i("####", "####setAddressTable"+ADDRESS_TABLE_MAP.size()+"사이즈");
        if (ADDRESS_TABLE_MAP.size() <= 0)
            {
            resistDelivery();
            }
        
        
        else if (mode == 0)
            {
            String address = "[" + ADDRESS_TABLE_MAP.get("ZIP_CODE") + "] "
                + ADDRESS_TABLE_MAP.get("CITY1") + " "
                + ADDRESS_TABLE_MAP.get("STREET");
            String phone = KtRentalApplication.getInstance().getPhoneNumber();
            tv_detail_phone.setText(phone == null ? "" : phone);
            et_address.setText(address);
            bt_address.setText(ADDRESS_TABLE_MAP.get("DELVR_ADDR").toString());
            }
        else if (mode == 1)
            {
            String address = "[" + ADDRESS_TABLE_MAP.get("ZIP_CODE") + "] "
                + ADDRESS_TABLE_MAP.get("CITY1") + " "
                + ADDRESS_TABLE_MAP.get("STREET");
            String phone = KtRentalApplication.getInstance().getPhoneNumber();
            tv_detail_phone.setText(phone == null ? "" : phone);
            tv_address_detail.setText(address);
            bt_address_detail.setText(ADDRESS_TABLE_MAP.get("DELVR_ADDR")
                .toString());
            }
        }

    private void resistDelivery()
        {
        if (dpd == null || dpd.isShowing())
            return;
        Button bt_done = (Button) dpd.findViewById(R.id.delivery_point_save_id);
        bt_done.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v)
                {
                if (dpd.dpda.getCheckPosition() == -1)
                    {
                    EventPopupC epc = new EventPopupC(context);
                    epc.show("배송지를 선택해 주십시오");
                    return;
                    }
                boolean point_exist = false;
                for (int i = 0; i < Delivery_Point_Dialog.o_itab1.size(); i++)
                    {
                    if (Delivery_Point_Dialog.o_itab1.get(i).get("DEFT_TYP")
                        .equals("X"))
                        {
                        point_exist = true;
                        }
                    }
                if (!point_exist)
                    {
                    EventPopupC epc = new EventPopupC(context);
                    TextView tv_title = (TextView) epc
                        .findViewById(R.id.tv_body);
                    tv_title.setText("기본배송처를 등록해 주십시오");
                    epc.show();
                    return;
                    }
                HashMap hm = Delivery_Point_Dialog.o_itab1
                    .get(Delivery_Point_Dialog.dpda.getCheckPosition());
                ADDRESS_TABLE_MAP_OUT = new HashMap<String, String>();
                ADDRESS_TABLE_MAP_OUT.put("DELVR_ADDR", hm.get("DELVR_ADDR")
                    .toString());
                ADDRESS_TABLE_MAP_OUT.put("ZIP_CODE", hm.get("ZIP_CODE")
                    .toString());
                ADDRESS_TABLE_MAP_OUT.put("CITY1", hm.get("CITY1").toString());
                ADDRESS_TABLE_MAP_OUT
                    .put("STREET", hm.get("STREET").toString());
                ADDRESS_TABLE_MAP_OUT.put("HOUSE_NUM1", hm.get("STREET")
                    .toString());
                bt_address.setText(hm.get("DELVR_ADDR").toString());
                et_address.setText("[" + hm.get("ZIP_CODE") + "] "
                    + hm.get("CITY1") + " " + hm.get("STREET"));
                ZIP_CODE = hm.get("ZIP_CODE").toString();
                CITY1 = hm.get("CITY1").toString();
                STREET = hm.get("STREET").toString();
                bt_address_detail.setText(hm.get("DELVR_ADDR").toString());
                tv_address_detail.setText("[" + hm.get("ZIP_CODE") + "] "
                    + hm.get("CITY1") + " " + hm.get("STREET"));
                ZIP_CODE_D = hm.get("ZIP_CODE").toString();
                CITY1_D = hm.get("CITY1").toString();
                STREET_D = hm.get("STREET").toString();
                
                getAddressTable();
                dpd.dismiss();
                }
        });
        if (!dpd.isShowing())
            dpd.show();
        }

    private String getPhone()
        {
        TelephonyManager telManager = (TelephonyManager) context
            .getSystemService(context.TELEPHONY_SERVICE);
        String phoneNum = telManager.getLine1Number();
        return PhoneNumberUtils.formatNumber(phoneNum);
        }

    private void setTab()
        {
        tabgroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId)
                {
                switch (checkedId)
                    {
                    case R.id.inventory_tab1_id:
                        setTab1();
                        break;
                    case R.id.inventory_tab2_id:
                        setTab2();
                        break;
                    }
                }
        });
        setTab1();
        }

    private void setTab1()
        {
        setTabVisible(0);
//        NOW_LIST_ARR = new ArrayList<PARTS_SEARCH>();
//        isra = new Inventory_Search_Right_Adapter(context, R.layout.inventory_search_right_row, NOW_LIST_ARR);
//        lv_right.setAdapter(isra);
        }

    private void setTab2()
        {
        setTabVisible(1);
        // 신청내역 초기화
        }

    private void setTabVisible(int num)
        {
        tab[0].setVisibility(View.GONE);
        tab[1].setVisibility(View.GONE);
        tab[num].setVisibility(View.VISIBLE);
        }

    // private String DATE1;
    // private String DATE2;
    private void setDateButton()
        {
        Calendar cal = Calendar.getInstance();
        // cal.set(Calendar.DAY_OF_MONTH, 8);
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH) + 1;
        int day = cal.get(Calendar.DAY_OF_MONTH);
        String month_str = String.format("%02d", month);
        String day_str = String.format("%02d", day);
        bt_datepick2.setText(year + "." + month_str + "." + day_str);// 오늘날짜
        // cal.set(Calendar.DAY_OF_MONTH, 1);
        // day = cal.get(Calendar.DAY_OF_MONTH);
        // String day_str_1 = String.format("%02d", day);
        if (day < 7)
            {
            bt_datepick1.setText(year + "." + month_str + "." + "01");// 이달의첫날
            }
        else
            {
            int aday = day - 6;
            String aday_str = String.format("%02d", aday);
            bt_datepick1.setText(year + "." + month_str + "." + aday_str);
            }
        }
    // ArrayList<ADDRESS> addr_arr;
    public class ADDRESS
    {

        public String TITLE;
        public String ADDRESS;
    }

    // private ArrayList<ADDRESS> initAddress()
    // {
    // addr_arr = new ArrayList<ADDRESS>();
    // for(int i=0;i<5;i++)
    // {
    // ADDRESS addr = new ADDRESS();
    // addr.TITLE = "기본주소"+i;
    // addr.ADDRESS = "주소내용"+i;
    // addr_arr.add(addr);
    // }
    // return addr_arr;
    // }
    private void setM028Click()
        {
        ViewGroup vg = pwM028.getViewGroup();
        LinearLayout back = (LinearLayout) vg.getChildAt(0);
        for (int i = 0; i < back.getChildCount(); i++)
            {
            LinearLayout row_back = (LinearLayout) back.getChildAt(i);
            final Button bt = (Button) row_back.getChildAt(0);
            bt.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v)
                    {
                    bt_m028.setText(bt.getText().toString());
                    M028_TAG = bt.getTag().toString();
                    pwM028.dismiss();
                    }
            });
            }
        }

    // private void setAddressClick()
    // {
    // ViewGroup vg = iaddresspopup.getViewGroup();
    // LinearLayout back = (LinearLayout)vg.getChildAt(0);
    // for(int i=0;i<back.getChildCount();i++)
    // {
    // LinearLayout row_back = (LinearLayout)back.getChildAt(i);
    // final Button bt = (Button)row_back.getChildAt(0);
    // bt.setOnClickListener(new OnClickListener()
    // {
    // @Override
    // public void onClick(View v)
    // {
    // bt_address.setText(bt.getText().toString());
    // ADDRESS_STR = bt.getTag().toString();
    // et_address.setText(ADDRESS_STR);
    // iaddresspopup.dismiss();
    // }
    // });
    // }
    // }
    @Override
    public void onStart()
        {
        // 여기서 프레그먼트 작업
        super.onStart();
        }

    private void setChoicedReset()
        {
        if (Inventory_Search_Left_Adapter.checked_items == null)
            return;
        Inventory_Search_Left_Adapter.checked_items.clear();
        for (int i = 0; i < parts_search_arr.size(); i++)
            {
            PARTS_SEARCH ps = parts_search_arr.get(i);
            ps.REQUEST = "0";
            }
        isa.notifyDataSetChanged();
        }
    private final String INVENTORY_PARTS_REQUEST = "ZMO_1030_WR01";

    private void sendPartsRequest()
        {
        HashMap<String, String> map = getCommonConnectData();
        connector.setParameter("I_PERNR", getLoginID());
        connector.setParameter("I_WERKS", getWerks());
        connector.setParameter("I_LGORT", getLgort()); // 저장위치
        SimpleDateFormat sd = new SimpleDateFormat("yyyyMMdd");
        String date = sd.format(new Date());
        connector.setParameter("I_LFDAT", date);
        Object data = et_etc.getText();
        String str = data != null && !data.toString().equals("") ? data
            .toString() : "";
        connector.setParameter("I_MTEXT", str);
        connector.setStructure("IS_LOGIN", map);
        if (setConnectorParam(date) == null) return;
        connector.setTable("I_ITAB1", i_itab1);
        showProgress("부품을 신청합니다.");
        try
            {
            connector.executeRFCAsyncTask(INVENTORY_PARTS_REQUEST,
                                          INVENTORY_PARTS_REQUEST + "_TABLE", "request");
            }
        catch(Exception e)
            {
            e.printStackTrace();
            }
        }
    ArrayList<HashMap<String, String>> i_itab1;

    private ArrayList<HashMap<String, String>> setConnectorParam(String _date)
        {

        Object data;
        String str;
        i_itab1 = new ArrayList<HashMap<String, String>>();

        for (int i = 0; i < NOW_LIST_ARR.size(); i++)
            {
            PARTS_SEARCH ps = NOW_LIST_ARR.get(i);
            if(!ps.CHECKED) continue;
            HashMap<String, String> hm = new HashMap<String, String>();
            hm.put("BANFN", " "); // 구매요청번호(공백확인)
            hm.put("BNFPO", " "); // 구매 요청 품목 번호(공백확인)
            hm.put("MATNR", ps.MATNR); // 자재 번호(확인)
            hm.put("MEINS", ps.MEINS); // 단위(확인)
            hm.put("REQ_QTY", ps.REQUEST); // 신청수량(확인)
            HashMap<String, String> address;
            if (rb_in_out[0].isChecked())
                {
                address = ADDRESS_TABLE_MAP;
                }
            else
                {
                address = ADDRESS_TABLE_MAP_OUT;
                }
            Log.i("####", "##주소" + address.get("ZIP_CODE") + "/" + address.get("CITY1") + "/" + address.get("STREET"));
            data = address.get("ZIP_CODE");
            str = data != null && !data.toString().equals("") ? data.toString() : " ";
            hm.put("POST_CODE1", str); // 우편번호(확인)
            data = address.get("CITY1");
            str = data != null && !data.toString().equals("") ? data.toString()
                : " ";
            hm.put("CITY1", str); // 도시(확인)
            data = address.get("STREET");
            str = data != null && !data.toString().equals("") ? data.toString()
                : " ";
            hm.put("STREET", str); // 상세 주소(확인)
            data = address.get("STREET");
            str = data != null && !data.toString().equals("") ? data.toString()
                : " ";
            hm.put("HOUSE_NUM1", str); // NUM1 번지(확인)
            hm.put("ZSTATUS", " "); // 진행상태코드(공백확인)
            hm.put("INVNR", getInvnr()); // 순회차량번호(확인)
            hm.put("BADAT", _date); // 요청일(확인)
            hm.put("DELI_DIV", DELIVERY); // 배송구분
            data = tv_phone.getText();
            str = data != null && !data.toString().equals("") ? data.toString()
                : "";
            hm.put("TEL_NUMBER", str); // 대표 전화번호: 국번+번호
            i_itab1.add(hm);
            }
        return i_itab1;
        // 전송성공시 호출해야함
        // setChoicedReset();
        }
    private final String INVENTORY_PARTS_SEARCH            = "ZMO_1030_RD01";
    private final String INVENTORY_PARTS_SEARCH_TABLE_NAME = "inventory_parts_search_table";
    private Connector    connector;

    private void getPartsSearch()
        {
        showProgress("부품을 조회중 입니다.");
        HashMap<String, String> map = getCommonConnectData();
        connector.setParameter("I_PERNR", getLoginID());
        connector.setParameter("I_INVNR", getInvnr());
        connector.setParameter("I_MATKL", M028_TAG);
        String parts = et_pm023.getText().toString();
        connector.setParameter("I_MAKTX",
                               parts == null | parts.equals("") ? " " : parts);
        connector.setStructure("IS_LOGIN", map);
        try
            {
            connector.executeRFCAsyncTask(INVENTORY_PARTS_SEARCH,
                                          INVENTORY_PARTS_SEARCH_TABLE_NAME);
            }
        catch(Exception e)
            {
            e.printStackTrace();
            }
        }

    private HashMap<String, String> getCommonConnectData()
        {
        HashMap<String, String> reCommonMap = null;
        LoginModel model = KtRentalApplication.getLoginModel();
        if (model.getModelMap() != null)
            reCommonMap = model.getModelMap();
        return reCommonMap;
        }

    private String getInvnr()
        {
        return KtRentalApplication.getLoginModel().getInvnr();
        }

    private String getLoginID()
        {
        return KtRentalApplication.getLoginModel().getLogid();
        }

    private String getWerks()
        {
        return KtRentalApplication.getLoginModel().getWerks();
        }

    private String getLgort()
        {
        return KtRentalApplication.getLoginModel().getLgort();
        }

    @Override
    public void reDownloadDB(String newVersion)
        {}
    String           TABLE_NAME = "O_ITAB1";
    ArrayList<PM023> pm023_arr;

    private ArrayList<PM023> initPM023()
        {
        pm023_arr = new ArrayList<PM023>();
        PM023 pm = new PM023();
        pm.ZCODEV = " ";
        pm.ZCODEVT = "전체";
        pm023_arr.add(pm);
        String path = context.getExternalCacheDir() + "/DATABASE/"
            + DEFINE.SQLLITE_DB_NAME;
        SQLiteDatabase sqlite = SQLiteDatabase.openDatabase(path, null,
                                                            SQLiteDatabase.OPEN_READONLY);
        Cursor cursor = sqlite.rawQuery("select ZCODEV, ZCODEVT from "
            + TABLE_NAME + " where ZCODEH = 'PM023'", null);
        while (cursor.moveToNext())
            {
            int calnum1 = cursor.getColumnIndex("ZCODEV");
            int calnum2 = cursor.getColumnIndex("ZCODEVT");
            String zcodev = cursor.getString(calnum1);
            String zcodevt = cursor.getString(calnum2);
            pm = new PM023();
            pm.ZCODEV = zcodev;
            pm.ZCODEVT = zcodevt;
            pm023_arr.add(pm);
            }
        cursor.close();
//        sqlite.close();
        // 출력테스트
        for (int i = 0; i < pm023_arr.size(); i++)
            {
            PM023 key = pm023_arr.get(i);
            }
        return pm023_arr;
        }

    private ArrayList<HashMap<String, String>> makeRequestListDelete()
        {
        ArrayList<HashMap<String, String>> i_itab1 = new ArrayList<HashMap<String, String>>();
        for (int i = 0; i < Inventory_Request_List_Adapter.checked_items.size(); i++)
            {
            int checked_num = Inventory_Request_List_Adapter.checked_items
                .get(i);
            REQUEST_LIST ps = request_list_arr.get(checked_num);
            HashMap<String, String> hm = new HashMap<String, String>();
            hm.put("BANFN", ps.BANFN); // 구매요청번호(공백확인)
            i_itab1.add(hm);
            }
        return i_itab1;
        }
    // 신청내역삭제
    final String INVENTORY_REQUEST_LIST_DELETE = "ZMO_1030_WR02";

    private void goRequestDelete()
        {
        showProgress("신청내역을 삭제중 입니다.");
        HashMap<String, String> map = getCommonConnectData();
        connector.setStructure("IS_LOGIN", map);
        connector.setParameter("I_PERNR", getLoginID());
        ArrayList<HashMap<String, String>> i_itab1 = makeRequestListDelete();
        connector.setTable("I_ITAB1", i_itab1);
        try
            {
            connector.executeRFCAsyncTask(INVENTORY_REQUEST_LIST_DELETE,
                                          TABLE_NAME);
            }
        catch(Exception e)
            {
            e.printStackTrace();
            }
        }
    final String INVENTORY_REQUEST_LIST = "ZMO_1030_RD02";

    private void goRequestGetList()
        {
        showProgress("신청내역을 조회중 입니다.");
        HashMap<String, String> map = getCommonConnectData();
        connector.setParameter("I_PERNR", getLoginID());
        connector.setParameter("I_INVNR", getInvnr());
        connector.setParameter("I_BADAT_FR", bt_datepick1.getText().toString()
            .replace(".", ""));
        connector.setParameter("I_BADAT_TO", bt_datepick2.getText().toString()
            .replace(".", ""));
        connector.setStructure("IS_LOGIN", map);
        try
            {
            connector.executeRFCAsyncTask(INVENTORY_REQUEST_LIST, TABLE_NAME);
            }
        catch(Exception e)
            {
            e.printStackTrace();
            }
        }

    @Override
    public void onClick(View arg0)
        {
        String data;
        String date;
        ViewGroup vg;
        final TextView input;
        Button done;
        final Delivery_Point_Dialog dpd;
        Button bt_done;
        switch (arg0.getId())
            {
            case R.id.inventory_parts_search_id:
                parts_search_arr = new ArrayList<PARTS_SEARCH>();
                isa = new Inventory_Search_Left_Adapter(context, R.layout.inventory_search_left_row, parts_search_arr);
                lv_parts_search.setAdapter(isa);
                iv_nodata1.setVisibility(View.VISIBLE);
                getPartsSearch();
                break;
            case R.id.inventory_pm023_id: // 자재그룹 드롭다운
                pwM028.show(bt_m028);
                break;
            case R.id.inventory_address_popup_id: // 기본주소 드롭다운
                resistDelivery();
                break;
//            case R.id.inventory_reset_id: // 리셋
//                setChoicedReset();
//                break;
            case R.id.inventory_parts_request_id: // 부품신청
//            	2014-04-14 KDH 여기가 문제네
            	
//                if (Inventory_Search_Left_Adapter.checked_items == null
//                    || Inventory_Search_Left_Adapter.checked_items.size() <= 0)
//                    return;

            	if(NOW_LIST_ARR.size() == 0)
            	{
            		return;
            	}
                
                if (rb_in_out[1].isChecked() && ADDRESS_TABLE_MAP_OUT == null)
                    {
                    EventPopupC epc = new EventPopupC(context);
                    epc.show("배송지를 선택해 주세요");
                    return;
                    }
                if(rb_in_out[0].isChecked() && ADDRESS_TABLE_MAP.get("ZIP_CODE")==null)
                    {
                    resistDelivery();
                    EventPopupC epc = new EventPopupC(context);
                    epc.show("배송지를 입력해 주세요");
                    return;
                    }
                
                sendPartsRequest();
                break;
            case R.id.inventory_maintenance_datepick1_id: // 날짜선택
                data = bt_datepick2.getText().toString();
                date = data.replace(".", "");
                idatepickpopup = new DatepickPopup2(context, date, 0);
                idatepickpopup.show(bt_datepick1);
                break;
            case R.id.inventory_maintenance_datepick2_id: // 날짜선택
                data = bt_datepick1.getText().toString();
                date = data.replace(".", "");
                idatepickpopup = new DatepickPopup2(context, date, 1);
                idatepickpopup.show(bt_datepick2);
                break;
            case R.id.inventory_request_delete_id: // 신청내역삭제
                goRequestDelete();
                break;
            case R.id.inventory_request_getlist_id: // 신청내역조회
                goRequestGetList();
                setEditVisible(0);
                break;
            case R.id.inventory_detail_delete_id: // 상세 삭제
                if (Inventory_Request_List_Detail_Adapter.checked_items == null
                    || Inventory_Request_List_Detail_Adapter.checked_items
                        .size() <= 0)
                    return;
                goDetailDelete();
                break;
            case R.id.inventory_detail_modify_id: // 수정내용등록
                if (request_list_detail_arr == null
                    || request_list_detail_arr.size() <= 0)
                    return;
                Object obj = tv_address_detail.getText();
                if (obj == null || obj.toString().equals(""))
                    return;
                goDetailModify();
                break;
            case R.id.inventory_detail_editmode_id:// 에디트모드
                setEditVisible(1);
                break;
            case R.id.inventory_request_delete_cancel_id:// 에디트모드 취소
                Inventory_Request_List_Adapter.checked_items.clear();
                InventoryControlFragment.bt_request_delete.setEnabled(false);
                irla.notifyDataSetChanged();
                setEditVisible(0);
                break;
            case R.id.inventory_detail_address_popup_id: // 상세주소버튼
                resistDelivery();
                break;
            case R.id.inventory_phone_id:
                mPopupWindow = new InventoryPopup(context,
                    QuantityPopup.HORIZONTAL, R.layout.inventory_popup,
                    InventoryPopup.TYPE_PHONE_NUMBER);
                vg = mPopupWindow.getViewGroup();
                input = (TextView) vg.findViewById(R.id.inventory_bt_input);
                done = (Button) vg.findViewById(R.id.inventory_bt_done);
                done.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v)
                        {
                        String num = input.getText().toString();
                        tv_phone.setText(num);
                        mPopupWindow.setInput("CLEAR", true);
                        mPopupWindow.dismiss();
                        }
                });
                mPopupWindow.show(tv_phone);
                break;
            case R.id.inventory_detail_phone_id:
                mPopupWindow = new InventoryPopup(context,
                    QuantityPopup.HORIZONTAL, R.layout.inventory_popup,
                    InventoryPopup.TYPE_PHONE_NUMBER);
                vg = mPopupWindow.getViewGroup();
                input = (TextView) vg.findViewById(R.id.inventory_bt_input);
                done = (Button) vg.findViewById(R.id.inventory_bt_done);
                done.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v)
                        {
                        String num = input.getText().toString();
                        tv_detail_phone.setText(num);
                        mPopupWindow.setInput("CLEAR", true);
                        mPopupWindow.dismiss();
                        }
                });
                mPopupWindow.show(tv_detail_phone);
                break;
            case R.id.inventory_right_sort1_id:
                break;
            case R.id.inventory_right_sort2_id:
                SORT_MODE++;
                SORT_MODE = SORT_MODE>=2?0:SORT_MODE;
                sortMode(SORT_MODE);
                isra.notifyDataSetChanged();
                break;
                
            case R.id.inventory_left_sort2_id:
                LEFT_SORT_MODE++;
                LEFT_SORT_MODE = LEFT_SORT_MODE>=2?0:LEFT_SORT_MODE;

                switch(LEFT_SORT_MODE)
                    {
                    case 0:
                        Collections.sort(parts_search_arr, new Compare1());
                        break;
                    case 1:
                        Collections.sort(parts_search_arr, new Compare1());
                        Collections.reverse(parts_search_arr);
                        break;
                    }
                isa.notifyDataSetChanged();
                break;
            }
        }
    
    int SORT_MODE = 0;
    int LEFT_SORT_MODE = 0;

    private ArrayList<PARTS_SEARCH> NOW_LIST_ARR;
    
    private void sortMode(int mode)
        {
        NOW_LIST_ARR = BASE_PARTS_ITEMS;
        switch(mode)
            {
            case 0:
                Collections.sort(NOW_LIST_ARR, new Compare1());
                break;
            case 1:
                Collections.sort(NOW_LIST_ARR, new Compare1());
                Collections.reverse(NOW_LIST_ARR);
                break;
            }
        
//        Log.i("####", "####BASE_PARTS_ITEMS0순위"+BASE_PARTS_ITEMS.get(0).MAKTX);
//        Log.i("####", "####NOW_LIST_ARR0순위"+NOW_LIST_ARR.get(0).MAKTX);
        }
    
    class Compare1 implements Comparator<PARTS_SEARCH>
        {
        @Override
        public int compare(PARTS_SEARCH arg0, PARTS_SEARCH arg1)
            {
            String s1 = (String) arg0.MAKTX;
            String s2 = (String) arg1.MAKTX;
            return s1.compareTo(s2);
            }
        }

    private void goDetailModify()
        {
        showProgress("수정중 입니다.");
        HashMap<String, String> map = getCommonConnectData();
        connector.setParameter("I_PERNR", getLoginID());
        connector.setParameter("I_WERKS", getWerks());// 디비에서 가져와야함
        connector.setParameter("I_LGORT", getLgort()); // 디비에서 가져와야함
        connector.setParameter("I_LFDAT", request_list_detail_arr.get(0).BADAT); // 부품요청날짜중
                                                                                 // 첫번째것(08.05수정);
        connector.setParameter("I_MTEXT", et_detail_etc.getText().toString()); // 비고내용
                                                                               // 쓰기
        connector.setStructure("IS_LOGIN", map);
        ArrayList<HashMap<String, String>> temp = setDetailModify(); // I_ITAB1
                                                                     // 입력하기
        if (temp.size() <= 0) {
            hideProgress();
            return;
        }
        connector.setTable("I_ITAB1", temp);
        try
            {
            connector.executeRFCAsyncTask(INVENTORY_PARTS_REQUEST,
                                          INVENTORY_PARTS_REQUEST + "_TABLE", "modify");
            }
        catch(Exception e)
            {
            e.printStackTrace();
            }
        }

    private ArrayList<HashMap<String, String>> setDetailModify()
        {
        String phone = tv_detail_phone.getText().toString();
        ArrayList<HashMap<String, String>> i_itab1 = new ArrayList<HashMap<String, String>>();
        if (Inventory_Request_List_Detail_Adapter.checked_items == null)
            return i_itab1;
        for (int i = 0; i < Inventory_Request_List_Detail_Adapter.checked_items
            .size(); i++)
            {
            int checked_num = Inventory_Request_List_Detail_Adapter.checked_items
                .get(i);
            REQUEST_LIST_DETAIL rld = request_list_detail_arr.get(checked_num);
            HashMap<String, String> hm = new HashMap<String, String>();
            hm.put("BANFN", rld.BANFN); // 구매요청번호(공백확인)
            hm.put("BNFPO", rld.BNFPO); // 구매 요청 품목 번호(공백확인)
            hm.put("MATNR", rld.MATNR); // 자재 번호(확인)
            hm.put("MEINS", rld.MEINS); // 단위(확인)
            hm.put("REQ_QTY", rld.REQ_QTY_TEMP); // 신청수량(확인)
            hm.put("POST_CODE1", ZIP_CODE_D); // 우편번호(확인)
            hm.put("CITY1", CITY1_D); // 도시(확인)
            hm.put("STREET", STREET_D); // 상세 주소(확인)
            hm.put("HOUSE_NUM1", STREET_D); // NUM1 번지(확인)
            hm.put("ZSTATUS", rld.ZSTATUS); // 진행상태코드(공백확인)
            hm.put("INVNR", getInvnr()); // 순회차량번호(확인)
            hm.put("BADAT", rld.BADAT); // 요청일(확인)
            hm.put("DELI_DIV", DELIVERY_DETAIL); // 배송구분
            hm.put("TEL_NUMBER", phone); // 대표 전화번호: 국번+번호
            i_itab1.add(hm);
            }
        // Inventory_Request_List_Detail_Adapter.checked_items.clear();
        return i_itab1;
        }
    final String INVENTORY_DETAIL_DELETE = "ZMO_1030_WR03";

    private void goDetailDelete()
        {
        showProgress("내역을 삭제중 입니다.");
        HashMap<String, String> map = getCommonConnectData();
        connector.setStructure("IS_LOGIN", map);
        connector.setParameter("I_PERNR", getLoginID());
        ArrayList<HashMap<String, String>> i_itab1 = makeDetailListDelete();
        connector.setTable("I_ITAB1", i_itab1);
        try
            {
            connector.executeRFCAsyncTask(INVENTORY_DETAIL_DELETE, "");
            }
        catch(Exception e)
            {
            e.printStackTrace();
            }
        }

    public void goDetailDelete(int num)
        {
        showProgress("내역을 삭제중 입니다.");
        HashMap<String, String> map = getCommonConnectData();
        connector.setStructure("IS_LOGIN", map);
        connector.setParameter("I_PERNR", getLoginID());
        ArrayList<HashMap<String, String>> i_itab1 = new ArrayList<HashMap<String, String>>();
        REQUEST_LIST_DETAIL pld = request_list_detail_arr.get(num);
        HashMap<String, String> hm = new HashMap<String, String>();
        hm.put("BANFN", pld.BANFN); // 구매요청번호
        hm.put("BNFPO", pld.BNFPO); // 구매 요청 품목 번호
        i_itab1.add(hm);
        connector.setTable("I_ITAB1", i_itab1);
        try
            {
            connector.executeRFCAsyncTask(INVENTORY_DETAIL_DELETE, "");
            }
        catch(Exception e)
            {
            e.printStackTrace();
            }
        }

    public ArrayList<HashMap<String, String>> makeDetailListDelete()
        {
        ArrayList<HashMap<String, String>> i_itab1 = new ArrayList<HashMap<String, String>>();
        for (int i = 0; i < Inventory_Request_List_Detail_Adapter.checked_items
            .size(); i++)
            {
            int checked_num = Inventory_Request_List_Detail_Adapter.checked_items
                .get(i);
            REQUEST_LIST_DETAIL pld = request_list_detail_arr.get(checked_num);
            HashMap<String, String> hm = new HashMap<String, String>();
            hm.put("BANFN", pld.BANFN); // 구매요청번호
            hm.put("BNFPO", pld.BNFPO); // 구매 요청 품목 번호
            i_itab1.add(hm);
            }
        return i_itab1;
        }
    
    

    @Override
    public void connectResponse(String FuntionName, String resultText, String MTYPE, int resulCode, TableModel tableModel)
        {
//        Log.i("#", "####" + FuntionName + "/" + resultText + "/" + MTYPE + "/" + resulCode);
        hideProgress();
        if (MTYPE == null || !MTYPE.equals("S"))
            {
        	
        	cc.duplicateLogin(context);
        	
            EventPopupC epc = new EventPopupC(context);
            epc.show(resultText);
            return;
            }
        
        if (FuntionName.equals("ZMO_1030_RD01"))
            {
            parts_search_arr = new ArrayList<PARTS_SEARCH>();
            ArrayList<HashMap<String, String>> temp_arr = tableModel.getTableArray();
            if (temp_arr.size() > 0)
                {
                iv_nodata1.setVisibility(View.GONE);
                }
            else
                {
                iv_nodata1.setVisibility(View.VISIBLE);
                }
            for (int i = 0; i < temp_arr.size(); i++)
                {
                PARTS_SEARCH ps = new PARTS_SEARCH();
                HashMap hm = temp_arr.get(i);
                Object data = hm.get("BUKRS");
                String encrypted = data != null ? data.toString() : " ";
                ps.BUKRS = encrypted;
                data = hm.get("LGORT");
                encrypted = data != null ? data.toString() : " ";
                ps.LGORT = encrypted;
                data = hm.get("WERGW");
                encrypted = data != null ? data.toString() : " ";
                ps.WERGW = encrypted;
                data = hm.get("MATNR");
                encrypted = data != null ? data.toString() : " ";
                ps.MATNR = encrypted;
                data = hm.get("MAKTX");
                encrypted = data != null ? data.toString() : " ";
                ps.MAKTX = encrypted;
                data = hm.get("MEINS");
                encrypted = data != null ? data.toString() : " ";
                ps.MEINS = encrypted;
                data = hm.get("LABST");
                encrypted = data != null ? data.toString() : " ";
                ps.LABST = encrypted;
                data = hm.get("LTEXT");
                encrypted = data != null ? data.toString() : " ";
                ps.LTEXT = encrypted;
                ps.REQUEST = "0";
                ps.COMPATIBILITY = "0";
                parts_search_arr.add(ps);
                }
            isa = new Inventory_Search_Left_Adapter(context, R.layout.inventory_search_left_row, parts_search_arr);
            lv_parts_search.setAdapter(isa);
            lv_parts_search.setOnItemClickListener(new OnItemClickListener()
                {
                @Override
                public void onItemClick(AdapterView<?> av, View v, int position, long arg3)
                    {
//                    Log.i("####", "####눌린리스트 포지션"+position);
                    final int final_position = position;
                    final TextView tv_request = (TextView) v.findViewById(R.id.inventory_search_row_id4);
                    setInputButton(tv_request, final_position);
//                    tv_request.setOnClickListener(new OnClickListener() 
//                        {
//                        @Override
//                        public void onClick(View v)
//                            {
//                            setInputButton(tv_request, final_position);
//                            }
//                        });
                    }
                });
            
            cc.duplicateLogin(context);
            
            }
        else if (FuntionName.equals("ZMO_1030_WR01"))
            {
            // Log.i("#", "####aaa");
            // 부품신청
            if (tableModel.getValue().equals("request"))
                {
                // Log.i("#", "####bbb");
                // for(int
                // i=0;i<Inventory_Search_Adapter.checked_items.size();i++)
                // {
                // parts_search_arr.get(Inventory_Search_Adapter.checked_items.get(i)).REQUEST
                // = "0";
                // }
                // Inventory_Search_Adapter.checked_items.clear();
                // isa.notifyDataSetChanged();
                parts_search_arr = new ArrayList<PARTS_SEARCH>();
                isa = new Inventory_Search_Left_Adapter(context, R.layout.inventory_search_left_row, parts_search_arr);
                lv_parts_search.setAdapter(isa);
                iv_nodata1.setVisibility(View.VISIBLE);
                getPartsSearch();
                EventPopupC epc = new EventPopupC(context);
                TextView tv_title = (TextView) epc .findViewById(R.id.tv_body);
                tv_title.setText("부품신청이 완료되었습니다.");
                epc.show();
                
                NOW_LIST_ARR = new ArrayList<PARTS_SEARCH>();
                BASE_PARTS_ITEMS = new ArrayList<PARTS_SEARCH>();
                isra = new Inventory_Search_Right_Adapter(context, R.layout.inventory_search_right_row, NOW_LIST_ARR);
                lv_right.setAdapter(isra);
                if(NOW_LIST_ARR.size()>0)
                    {
                    iv_right_nodata.setVisibility(View.GONE);
                    }
                else{
                    iv_right_nodata.setVisibility(View.VISIBLE);
                    }
                }
            
            // 부품신청수정
            else if (tableModel.getValue().equals("modify"))
                {
                Inventory_Request_List_Detail_Adapter.checked_items.clear();
                irlda.notifyDataSetChanged();
                }
            
            
            
            }
        else if (FuntionName.equals("ZMO_1030_RD02"))
            {
            resultZMO_1030_RD02(FuntionName, resultText, MTYPE, resulCode,
                                tableModel);
            irla = new Inventory_Request_List_Adapter(context,
                R.layout.inventory_request_list_row, request_list_arr);
            lv_request_list.setAdapter(irla);
            lv_request_list.setOnItemClickListener(new OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> av, View v,
                    int position, long arg3)
                    {
                    getRequestListDetail(request_list_arr.get(position).BANFN);
                    DETAIL_SELECTED = position;
                    irla.setPosition(position);
                    }
            });
            if (request_list_arr == null || request_list_arr.size() <= 0)
                return;
            getRequestListDetail(request_list_arr.get(0).BANFN);
            DETAIL_SELECTED = 0;
            
            }
        // 삭제
        else if (FuntionName.equals("ZMO_1030_WR02"))
            {
            goRequestGetList();
            }
        // 상세조회
        else if (FuntionName.equals("ZMO_1030_RD03"))
            {
            request_list_detail_arr = new ArrayList<REQUEST_LIST_DETAIL>();
            // String o_cnt = tableModel.getResponse("O_CNT");
            String o_mtext = tableModel.getResponse("O_MTEXT");
//            Log.i("####", "####" + "비고" + "/" + o_mtext + "/");
            ArrayList<HashMap<String, String>> detail_arr = tableModel.getTableArray();
            if (detail_arr.size() > 0)
                {
                iv_nodata3.setVisibility(View.GONE);
                }
            else
                {
                iv_nodata3.setVisibility(View.VISIBLE);
                }
            for (int i = 0; i < detail_arr.size(); i++)
                {
                REQUEST_LIST_DETAIL rld = new REQUEST_LIST_DETAIL();
                HashMap hm = detail_arr.get(i);
                Object data = hm.get("BANFN");
                String encrypted = data != null ? data.toString() : " ";
                rld.BANFN = encrypted;
                data = hm.get("BNFPO");
                encrypted = data != null ? data.toString() : " ";
                rld.BNFPO = encrypted;
                data = hm.get("MATNR");
                encrypted = data != null ? data.toString() : " ";
                rld.MATNR = encrypted;
                data = hm.get("MAKTX");
                encrypted = data != null ? data.toString() : " ";
                rld.MAKTX = encrypted;
                data = hm.get("MEINS");
                encrypted = data != null ? data.toString() : " ";
                rld.MEINS = encrypted;
                data = hm.get("REQ_QTY");
                encrypted = data != null ? data.toString() : " ";
                rld.REQ_QTY = encrypted;
                data = hm.get("APV_QTY");
                encrypted = data != null ? data.toString() : " ";
                rld.APV_QTY = encrypted;
                data = hm.get("POST_CODE1");
                encrypted = data != null ? data.toString() : " ";
                rld.POST_CODE1 = encrypted;
                data = hm.get("CITY1");
                encrypted = data != null ? data.toString() : " ";
                rld.CITY1 = encrypted;
                data = hm.get("STREET");
                encrypted = data != null ? data.toString() : " ";
                rld.STREET = encrypted;
                data = hm.get("HOUSE_NUM1");
                encrypted = data != null ? data.toString() : " ";
                rld.HOUSE_NUM1 = encrypted;
                data = hm.get("ZSTATUS");
                encrypted = data != null ? data.toString() : " ";
                rld.ZSTATUS = encrypted;
                data = hm.get("ZSTATUST");
                encrypted = data != null ? data.toString() : " ";
                rld.ZSTATUST = encrypted;
                data = hm.get("INVNR");
                encrypted = data != null ? data.toString() : " ";
                rld.INVNR = encrypted;
                data = hm.get("BADAT");
                encrypted = data != null ? data.toString() : " ";
                rld.BADAT = encrypted;
                data = hm.get("DELI_DIV");
                encrypted = data != null ? data.toString() : " ";
                rld.DELI_DIV = encrypted;
                data = hm.get("TEL_NUMBER");
                encrypted = data != null ? data.toString() : " ";
                rld.TEL_NUMBER = encrypted;
                data = hm.get("REQ_QTY_TEMP");
                encrypted = data != null ? data.toString() : " ";
                rld.REQ_QTY_TEMP = encrypted;
                request_list_detail_arr.add(rld);
                }
            irlda = new Inventory_Request_List_Detail_Adapter(context,
                R.layout.inventory_request_list_detail_row,
                request_list_detail_arr, this);
            lv_request_list_detail.setAdapter(irlda);
            lv_request_list_detail
                .setOnItemClickListener(new OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> av, View v,
                        int position, long arg3)
                        {
                        // ////////////////////
                        if (!request_list_detail_arr.get(position).ZSTATUS
                            .equals("10"))
                            return; // 부품신청상태가 아니면 리턴
                        final int final_position = position;
                        if (!Inventory_Request_List_Detail_Adapter.checked_items
                            .contains(position))
                            {
                            final TextView tv_request = (TextView) v
                                .findViewById(R.id.inventory_request_list_detail_row_id3);
                            setInputDetailButton(tv_request, final_position);
                            tv_request
                                .setOnClickListener(new OnClickListener() {

                                    @Override
                                    public void onClick(View v)
                                        {
                                        setInputDetailButton(
                                                             tv_request,
                                                             final_position);
                                        }
                                });
                            }
                        else
                            {
                            Inventory_Request_List_Detail_Adapter.checked_items
                                .remove(Inventory_Request_List_Detail_Adapter.checked_items
                                    .indexOf(position));
                            REQUEST_LIST_DETAIL rld = request_list_detail_arr
                                .get(final_position);
                            rld.REQ_QTY_TEMP = " ";
                            }
                        irlda.notifyDataSetChanged();
                        // //////////////////////
                        // if(!request_list_detail_arr.get(position).ZSTATUS.equals("10"))
                        // return; //부품신청상태가 아니면 리턴
                        // final int final_position = position;
                        // //체크안되있을경우
                        // if
                        // (!Inventory_Request_List_Detail_Adapter.checked_items.contains(position))
                        // {
                        // Inventory_Request_List_Detail_Adapter.checked_items.add(position);
                        // final TextView tv_request =
                        // (TextView)v.findViewById(R.id.inventory_request_list_detail_row_id3);
                        // ViewGroup vg = mPopupWindow.getViewGroup();
                        // final TextView input =
                        // (TextView)vg.findViewById(R.id.inventory_bt_input);
                        // Button done =
                        // (Button)vg.findViewById(R.id.inventory_bt_done);
                        // done.setOnClickListener(new OnClickListener()
                        // {
                        // @Override
                        // public void onClick(View v)
                        // {
                        // String num = input.getText().toString();
                        // mPopupWindow.dismiss();
                        //
                        // REQUEST_LIST_DETAIL rld =
                        // request_list_detail_arr.get(final_position);
                        // rld.REQ_QTY_TEMP = num;
                        // mPopupWindow.setInput("CLEAR", true);
                        // irlda.notifyDataSetChanged();
                        // }
                        // });
                        // mPopupWindow.show(tv_request, 0);
                        //
                        // tv_request.setOnClickListener(new
                        // OnClickListener()
                        // {
                        // @Override
                        // public void onClick(View v)
                        // {
                        // mPopupWindow = new InventoryPopup(context,
                        // QuantityPopup.HORIZONTAL,
                        // R.layout.inventory_popup,QuantityPopup.TYPE_NOMARL_NUMBER);
                        // ViewGroup vg = mPopupWindow.getViewGroup();
                        // final TextView input =
                        // (TextView)vg.findViewById(R.id.inventory_bt_input);
                        // Button done =
                        // (Button)vg.findViewById(R.id.inventory_bt_done);
                        // done.setOnClickListener(new OnClickListener()
                        // {
                        // @Override
                        // public void onClick(View v)
                        // {
                        // String num = input.getText().toString();
                        //
                        // REQUEST_LIST_DETAIL rld =
                        // request_list_detail_arr.get(final_position);
                        // rld.REQ_QTY_TEMP = num;
                        // mPopupWindow.setInput("CLEAR", true);
                        // irlda.notifyDataSetChanged();
                        // mPopupWindow.dismiss();
                        // }
                        // });
                        //
                        // mPopupWindow.show(tv_request, 0);
                        // }
                        // });
                        // }
                        // //체크되있을경우
                        // else
                        // {
                        // final TextView tv_request =
                        // (TextView)v.findViewById(R.id.inventory_request_list_detail_row_id3);
                        // tv_request.setOnClickListener(null);
                        // REQUEST_LIST_DETAIL rld =
                        // request_list_detail_arr.get(final_position);
                        // rld.REQ_QTY_TEMP = " ";
                        // Inventory_Request_List_Detail_Adapter.checked_items.remove(Inventory_Request_List_Detail_Adapter.checked_items.indexOf(position));
                        // }
                        // irlda.notifyDataSetChanged();
                        }
                });
            if (request_list_detail_arr.size() <= 0)
                return;
            REQUEST_LIST_DETAIL rld = request_list_detail_arr.get(0);
            String phone = rld.TEL_NUMBER;
            String delivery = rld.DELI_DIV;
            String bigo = "비고";
            String address = "[" + rld.POST_CODE1 + "] " + rld.CITY1 + " "
                + rld.STREET + " " + rld.HOUSE_NUM1;
            // Toast.makeText(context, phone+"/"+delivery+"/"+bigo+"/"+address,
            // 0).show();
            tv_detail_phone.setText(phone);
            et_detail_etc.setText(o_mtext);
            tv_address_detail.setText(address);
            if (delivery.equals("I"))
                {
                rb_detail_in_out[0].setChecked(true);
                }
            else
                {
                rb_detail_in_out[1].setChecked(true);
                }
            }
        else if (FuntionName.equals("ZMO_1030_WR03"))
            {
            getRequestListDetail(request_list_arr.get(DETAIL_SELECTED).BANFN);
            irlda.notifyDataSetChanged();
            }
        
        
        }

    
    private String NUM = "";
    private int POSITION = 0;
    public void setInputButton(TextView tv, final int position)
        {
        mPopupWindow = new InventoryPopup(context, QuantityPopup.HORIZONTAL,
            R.layout.inventory_popup, QuantityPopup.TYPE_NOMARL_NUMBER);
        ViewGroup vg = mPopupWindow.getViewGroup();
        final TextView input = (TextView) vg
            .findViewById(R.id.inventory_bt_input);
        Button done = (Button) vg.findViewById(R.id.inventory_bt_done);
        done.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v)
                {
                Object data = input.getText();
                String num = data != null && !data.toString().equals("") ? data
                    .toString() : "0";

                int int_num = Integer.parseInt(num);
                if (int_num <= 0)
                    {
                    EventPopupC epc = new EventPopupC(context);
                    epc.show("입력수량이 없습니다.");
                    PARTS_SEARCH ps = parts_search_arr.get(position);
                    ps.REQUEST = "0";
                    if (Inventory_Search_Left_Adapter.checked_items
                        .contains(position))
                        {
                        Inventory_Search_Left_Adapter.checked_items
                            .remove(Inventory_Search_Left_Adapter.checked_items
                                .indexOf(position));
                        }
                    }
                else
                    {
                    NUM = num;
                    POSITION = position;
                    PARTS_SEARCH ps = parts_search_arr.get(position);
                    ps.REQUEST = num;
                    if (!Inventory_Search_Left_Adapter.checked_items.contains(position))
                        {
                        Inventory_Search_Left_Adapter.checked_items.add(position);
                        }
                    }
                isa.notifyDataSetChanged();
                mPopupWindow.setInput("CLEAR", true);
                mPopupWindow.dismiss();
                }
        });
        
        mPopupWindow.setOnDismissListener(new OnDismissListener()
            {
            @Override
            public void onDismiss(String result, int position)
                {
                PARTS_SEARCH ps = parts_search_arr.get(POSITION);
//                PARTS_SEARCH ps = parts_search_arr.get(position);
//                Log.i("####","####원본MATNR"+ps.MATNR);
                for(int i=0;i<BASE_PARTS_ITEMS.size();i++)
                    {
                    PARTS_SEARCH bbb = BASE_PARTS_ITEMS.get(i);
//                    Log.i("####","####비교MATNR"+bbb.MATNR);
                    if(bbb.MATNR.equals(ps.MATNR))
                        {
                        EventPopupC epc = new EventPopupC(context);
                        epc.show("이미 추가되었습니다.\n수정해 주세요.");
                        return;
                        }
                    }
                
                if(!NUM.equals(""))
                    {
                    ps.REQUEST = NUM;
                    ps.CHECKED = true;
                    BASE_PARTS_ITEMS.add(ps);
                    NOW_LIST_ARR = BASE_PARTS_ITEMS;
                    setRightListView();
                    }
                NUM = "";
                POSITION = 0;
                }
            });

        mPopupWindow.show(tv, 0);
        }
    
    private void setRightListView()
        {
        isra = new Inventory_Search_Right_Adapter(context, R.layout.inventory_search_right_row, NOW_LIST_ARR);
        lv_right.setAdapter(isra);
        lv_right.setOnItemClickListener(new OnItemClickListener()
            {
            @Override
            public void onItemClick(AdapterView<?> arg0, View v, int position, long arg3)
                {
//                Log.i("####","#### 리스트 클릭");
                PARTS_SEARCH ps = NOW_LIST_ARR.get(position);
                ps.CHECKED = !ps.CHECKED;
                isra.notifyDataSetChanged(); 
                }
            });
        lv_right.setSelection(NOW_LIST_ARR.size() - 1);

        if(NOW_LIST_ARR.size()>0)
            {
            iv_right_nodata.setVisibility(View.GONE);
            }
        else{
            iv_right_nodata.setVisibility(View.VISIBLE);
            }
        }

    private void setInputDetailButton(TextView tv, final int position)
        {
        mPopupWindow = new InventoryPopup(context, QuantityPopup.HORIZONTAL,
            R.layout.inventory_popup, QuantityPopup.TYPE_NOMARL_NUMBER);
        ViewGroup vg = mPopupWindow.getViewGroup();
        final TextView input = (TextView) vg
            .findViewById(R.id.inventory_bt_input);
        Button done = (Button) vg.findViewById(R.id.inventory_bt_done);
        done.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v)
                {
                Object data = input.getText();
                String num = data != null && !data.toString().equals("") ? data
                    .toString() : "0";
                int int_num = Integer.parseInt(num);
                if (int_num <= 0)
                    {
                    EventPopupC epc = new EventPopupC(context);
                    epc.show("입력수량이 없습니다.");
                    REQUEST_LIST_DETAIL ps = request_list_detail_arr
                        .get(position);
                    ps.REQ_QTY_TEMP = " ";
                    if (Inventory_Request_List_Detail_Adapter.checked_items
                        .contains(position))
                        {
                        Inventory_Request_List_Detail_Adapter.checked_items
                            .remove(Inventory_Request_List_Detail_Adapter.checked_items
                                .indexOf(position));
                        }
                    }
                else
                    {
                    REQUEST_LIST_DETAIL ps = request_list_detail_arr
                        .get(position);
                    ps.REQ_QTY_TEMP = num;
                    if (!Inventory_Request_List_Detail_Adapter.checked_items
                        .contains(position))
                        {
                        Inventory_Request_List_Detail_Adapter.checked_items
                            .add(position);
                        }
                    }
                irlda.notifyDataSetChanged();
                mPopupWindow.setInput("CLEAR", true);
                mPopupWindow.dismiss();
                }
        });
        mPopupWindow.show(tv, 0);
        }
    ArrayList<REQUEST_LIST_DETAIL> request_list_detail_arr;
    public class REQUEST_LIST_DETAIL
    {

        public String BANFN;       // 구매 요청 번호
        public String BNFPO;       // 구매 요청 품목 번호
        public String MATNR;       // 자재 번호
        public String MAKTX;       // 자재내역
        public String MEINS;       // 기본 단위
        public String REQ_QTY;     // 신청수량
        public String APV_QTY;     // 승인수량
        public String POST_CODE1;  // 도시우편번호
        public String CITY1;       // 도시
        public String STREET;      // 상세 주소
        public String HOUSE_NUM1;  // 번지
        public String ZSTATUS;     // STATUS
        public String ZSTATUST;    // 설명 내역
        public String INVNR;       // 재고번호
        public String BADAT;       // 요청일
        public String DELI_DIV;    // 배송구분
        public String TEL_NUMBER;  // 대표 전화번호: 국번+번호
        public String REQ_QTY_TEMP; //
    }
    // 신청내역상세조회
    final String INVENTORY_REQUEST_LIST_DETAIL = "ZMO_1030_RD03";

    private void getRequestListDetail(String _i_banfn)
        {
        showProgress("조회중 입니다.");
        HashMap<String, String> map = getCommonConnectData();
        connector.setParameter("I_PERNR", getLoginID());
        connector.setStructure("IS_LOGIN", map);
        connector.setParameter("I_BANFN", _i_banfn);
        try
            {
            // connector.executeRFCAsyncTask(strRFCName, tableName, resArr)
            ArrayList<String> arr = new ArrayList<String>();
            arr.add("O_CNT");
            arr.add("O_MTEXT");
            connector.executeRFCAsyncTask(INVENTORY_REQUEST_LIST_DETAIL,
                                          TABLE_NAME, arr);
            }
        catch(Exception e)
            {
            e.printStackTrace();
            }
        }
    Inventory_Request_List_Adapter        irla;
    public static ArrayList<REQUEST_LIST> request_list_arr;

    private void resultZMO_1030_RD02(String FuntionName, String resultText,
        String MTYPE, int resulCode, TableModel tableModel)
        {
        request_list_arr = new ArrayList<REQUEST_LIST>();
        if (tableModel == null)
            {
            setEditModeGone();
            return; // 리스트없으면리턴
            }
        ArrayList<HashMap<String, String>> list = tableModel.getTableArray();
        if (list.size() > 0)
            {
            iv_nodata2.setVisibility(View.GONE);
            }
        else
            {
            iv_nodata2.setVisibility(View.VISIBLE);
            }
        for (int i = 0; i < list.size(); i++)
            {
            HashMap hm = list.get(i);
            REQUEST_LIST rl = new REQUEST_LIST();
            Object obj = hm.get("BANFN");
            String encrypted = (obj != null) ? obj.toString() : " ";
            rl.BANFN = encrypted;
            obj = hm.get("BADAT");
            encrypted = (obj != null) ? obj.toString() : " ";
            rl.BADAT = encrypted;
            obj = hm.get("AFNAM");
            encrypted = (obj != null) ? obj.toString() : " ";
            rl.AFNAM = encrypted;
            obj = hm.get("USR_NM");
            encrypted = (obj != null) ? obj.toString() : " ";
            rl.USR_NM = encrypted;
            obj = hm.get("TEL_NUMBER");
            encrypted = (obj != null) ? obj.toString() : " ";
            rl.TEL_NUMBER = encrypted;
            obj = hm.get("ZSTATUS");
            encrypted = (obj != null) ? obj.toString() : " ";
            rl.ZSTATUS = encrypted;
            obj = hm.get("MTEXT");
            encrypted = (obj != null) ? obj.toString() : " ";
            rl.MTEXT = encrypted;
            request_list_arr.add(rl);
            }
        Collections.sort(request_list_arr, new Comp());
        Collections.reverse(request_list_arr);
        }
    class Comp implements Comparator<REQUEST_LIST>
    {

        @Override
        public int compare(REQUEST_LIST arg0, REQUEST_LIST arg1)
            {
            String s1 = (String) arg0.BADAT;
            String s2 = (String) arg1.BADAT;
            return s1.compareTo(s2);
            }
    }
    public class REQUEST_LIST
    {

        public String BANFN;
        public String BADAT;
        public String AFNAM;
        public String USR_NM;
        public String TEL_NUMBER;
        public String ZSTATUS;
        public String MTEXT;
    }
}
