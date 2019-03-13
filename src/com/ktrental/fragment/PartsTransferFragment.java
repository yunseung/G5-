package com.ktrental.fragment;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.ktrental.R;
import com.ktrental.adapter.PartsTransfer_Cars_Dialog_Adapter;
import com.ktrental.adapter.PartsTransfer_Parts_Dialog_Adapter;
import com.ktrental.adapter.PartsTransfer_Search_Adapter;
import com.ktrental.adapter.PartsTransfer_Search_Right_Adapter;
import com.ktrental.beans.PARTS_SEARCH;
import com.ktrental.beans.PM023;
import com.ktrental.cm.connect.ConnectController;
import com.ktrental.cm.connect.Connector.ConnectInterface;
import com.ktrental.cm.db.DbAsyncTask;
import com.ktrental.cm.db.DbAsyncTask.DbAsyncResLintener;
import com.ktrental.common.DEFINE;
import com.ktrental.common.KtRentalApplication;
import com.ktrental.dialog.PartsTransfer_Cars_Dialog;
import com.ktrental.dialog.PartsTransfer_Parts_Dialog;
import com.ktrental.model.DbQueryModel;
import com.ktrental.model.LoginModel;
import com.ktrental.model.TableModel;
import com.ktrental.popup.EventPopupC;
import com.ktrental.popup.InventoryPopup;
import com.ktrental.popup.InventoryPopup.OnDismissListener;
import com.ktrental.popup.PM023_Popup;
import com.ktrental.popup.Popup_Window_DelSearch;
import com.ktrental.popup.Popup_Window_M028;
import com.ktrental.popup.Popup_Window_PM023;
import com.ktrental.popup.QuantityPopup;
import com.ktrental.popup.QuickAction;
import com.ktrental.util.OnChangeFragmentListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

public class PartsTransferFragment extends BaseFragment implements
    OnClickListener, ConnectInterface
{

    private Context                context;
//    private ProgressDialog         pd;
    private String                 TABLE_NAME = "O_ITAB1";
    private PM023_Popup            ipm023popup;
    private Popup_Window_PM023     pwPM023;
    private Popup_Window_M028      pwM028;
    private String                 P028_TAG;
    private Button                 bt_search;
    private Button                 bt_group;
    private Button                 bt_parts_code_search;
    private ConnectController      connectController;
    private ListView               lv_partslist;
    private InventoryPopup         mPopupWindow;
    private Button                 bt_done;
    // private TextView tv_car_num;
    private EditText               et_reason;
    private Button                 bt_car_search;
    // private TextView tv_parts_code;
    private TextView               tv_parts_name;
    private ImageView              iv_nodata;
    private Popup_Window_DelSearch pwDelSearch;
    
    private ArrayList<PARTS_SEARCH> parts_search_arr;
    private PartsTransfer_Search_Adapter    ptsa;

    private ArrayList<PARTS_SEARCH> BASE_PARTS_ITEMS;
    private ArrayList<PARTS_SEARCH> NOW_LIST_ARR;
    private int SORT_MODE = 0;
    private int LEFT_SORT_MODE = 0;
    private ListView lv_right;
    private PartsTransfer_Search_Right_Adapter ptsra;
    private ImageView iv_nodata_right;
    
    private TextView tv_sort1, tv_sort2;
    private TextView tv_left_sort;

    public PartsTransferFragment(){}

    public PartsTransferFragment(String className,
        OnChangeFragmentListener changeFragmentListener)
    {
    super(className, changeFragmentListener);
    }

    @Override
    public void onAttach(Activity activity)
        {
        context = activity;
//        pd = new ProgressDialog(context);
//        pd.setCancelable(false);
//        pd.setMessage("검색중입니다.");
        P028_TAG = " ";
        connectController = new ConnectController(this, context);
        mPopupWindow = new InventoryPopup(context, QuantityPopup.HORIZONTAL,
            R.layout.inventory_popup, QuantityPopup.TYPE_PHONE_NUMBER);
        ipm023popup = new PM023_Popup(context, QuickAction.VERTICAL,
            initPM023());
        pwPM023 = new Popup_Window_PM023(context);
        pwM028 = new Popup_Window_M028(context);
        pwDelSearch = new Popup_Window_DelSearch(context);
        super.onAttach(activity);
        }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
        Bundle savedInstanceState)
        {
        View v = inflater.inflate(R.layout.partstransferfragment, null);
        bt_search = (Button) v
            .findViewById(R.id.parts_transfer_parts_getlist_id);
        bt_search.setOnClickListener(this);
        bt_group = (Button) v.findViewById(R.id.parts_transfer_group_popup_id);
        bt_group.setOnClickListener(this);
        bt_parts_code_search = (Button) v
            .findViewById(R.id.parts_transfer_partscode_search_id);
        bt_parts_code_search.setOnClickListener(this);
        // tv_parts_code =
        // (TextView)v.findViewById(R.id.parts_transfer_partscode_id);
        lv_partslist = (ListView) v
            .findViewById(R.id.parts_transfer_parts_list_id);
        bt_done = (Button) v.findViewById(R.id.parts_transfer_done_id);
        bt_done.setOnClickListener(this);
        // tv_car_num =
        // (TextView)v.findViewById(R.id.parts_transfer_stock_car_id);
        et_reason = (EditText) v
            .findViewById(R.id.parts_transfer_stock_reason_id);
        bt_car_search = (Button) v
            .findViewById(R.id.parts_transfer_stock_car_search_id);
        bt_car_search.setOnClickListener(this);
        tv_parts_name = (TextView) v
            .findViewById(R.id.parts_transfer_parts_name_id);
        iv_nodata = (ImageView) v.findViewById(R.id.list_nodata_id);
        mRootView = v;
        
        lv_right = (ListView)v.findViewById(R.id.partstransfer_listview_right_id);
        iv_nodata_right = (ImageView)v.findViewById(R.id.partstransfer_nodata_right_id);
        
        tv_sort1 = (TextView)v.findViewById(R.id.partstransfer_right_sort1_id); tv_sort1.setOnClickListener(this);
        tv_sort2 = (TextView)v.findViewById(R.id.partstransfer_right_sort2_id); tv_sort2.setOnClickListener(this);
        
        tv_left_sort = (TextView)v.findViewById(R.id.partstransfer_left_sort2_id); tv_left_sort.setOnClickListener(this);
        
        // setClick();
        setM028Click();
        return v;
        }
    
    @Override
    public void onHiddenChanged(boolean hidden)
        {
        super.onHiddenChanged(hidden);
        if (hidden) { return; }

        lv_partslist.setAdapter(null);
        BASE_PARTS_ITEMS = new ArrayList<PARTS_SEARCH>();
        }
    // private void setClick()
    // {
    // tv_parts_code.setOnClickListener(new OnClickListener()
    // {
    // @Override
    // public void onClick(View arg0)
    // {
    // tv_parts_code.setText("");
    // tv_parts_name.setText("");
    // }
    // });
    // }

    @Override
    public void connectResponse(String FuntionName, String resultText,
        String MTYPE, int resulCode, TableModel tableModel)
        {
//        Log.i("#", "####" + FuntionName + "/" + resultText + "/" + MTYPE + "/" + resulCode);
        hideProgress();
        if (MTYPE == null||!MTYPE.equals("S"))
            {
        	connectController.duplicateLogin(mContext);
        	
            EventPopupC epc = new EventPopupC(context);
            epc.show(resultText);
            return;
            }
        if (FuntionName.equals("ZMO_1030_RD01"))
            {
            parts_search_arr = new ArrayList<PARTS_SEARCH>();
            ArrayList<HashMap<String, String>> temp_arr = tableModel
                .getTableArray();
            if (temp_arr.size() > 0)
                {
                iv_nodata.setVisibility(View.GONE);
                }
            else
                {
                iv_nodata.setVisibility(View.VISIBLE);
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
                data = hm.get("MATKL");
                encrypted = data != null ? data.toString() : " ";
                ps.MATKL = encrypted;
                data = hm.get("GRP_CD");
                encrypted = data != null ? data.toString() : " ";
                ps.GRP_CD = encrypted;
                ps.REQUEST = "0";
                ps.COMPATIBILITY = "0";
                parts_search_arr.add(ps);
                }
            ptsa = new PartsTransfer_Search_Adapter(context, R.layout.partstransfer_row, parts_search_arr);
            lv_partslist.setAdapter(ptsa);
            lv_partslist.setOnItemClickListener(new OnItemClickListener()
                {
                @Override
                public void onItemClick(AdapterView<?> av, View v, int position, long arg3)
                    {
                    final int final_position = position;
                    if (!PartsTransfer_Search_Adapter.checked_items.contains(position))
                        {
                        final TextView tv_request = (TextView)v.findViewById(R.id.inventory_search_row_id4);
                        setInputButton(tv_request, position);
                        tv_request.setOnClickListener(new OnClickListener()
                            {
                            @Override
                            public void onClick(View v)
                                {
                                setInputButton(tv_request, final_position);
                                }
                            });
                        }
                    else
                        {
                        PARTS_SEARCH ps = parts_search_arr.get(final_position);
                        ps.REQUEST = "0";
                        PartsTransfer_Search_Adapter.checked_items.remove(PartsTransfer_Search_Adapter.checked_items.indexOf(position));
                        }
                    ptsa.notifyDataSetChanged();
                    }
                });
            
            connectController.duplicateLogin(mContext);
            
            }
        else if (FuntionName.equals("ZMO_1030_WR05"))
            {
            initQueryItem();
            EventPopupC epc = new EventPopupC(context);
            epc.show("이관이 완료되었습니다.");

            showProgress("정비부품 내역을 조회 중 입니다.");
            Object data;
            data = tv_parts_name.getText();
            String str = data != null && !data.equals("") ? data.toString() : " ";
            connectController.getPartsSearch(P028_TAG, str);
            
            NOW_LIST_ARR = new ArrayList<PARTS_SEARCH>();
            BASE_PARTS_ITEMS = new ArrayList<PARTS_SEARCH>();
            ptsra = new PartsTransfer_Search_Right_Adapter(context, R.layout.partstransfer_search_right_row, NOW_LIST_ARR);
            lv_right.setAdapter(ptsra);
            if(NOW_LIST_ARR.size()>0)
                {
                iv_nodata_right.setVisibility(View.GONE);
                }
            else{
                iv_nodata_right.setVisibility(View.VISIBLE);
                }
            
            
            }
        }

    private String NUM = "";
    private int POSITION = 0;
    private void setInputButton(TextView tv, final int position)
        {
        PARTS_SEARCH ps = parts_search_arr.get(position);
        if(ps.LABST.equals("0")) return;
        mPopupWindow = new InventoryPopup(context, QuantityPopup.HORIZONTAL, R.layout.inventory_popup, QuantityPopup.TYPE_NOMARL_NUMBER);
        ViewGroup vg = mPopupWindow.getViewGroup();
        final TextView input = (TextView) vg.findViewById(R.id.inventory_bt_input);
        Button done = (Button) vg.findViewById(R.id.inventory_bt_done);
        done.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v)
                {
                Object data = input.getText();
                String num = data != null && !data.toString().equals("") ? data
                    .toString() : "0";
                int int_num = Integer.parseInt(num);
                PARTS_SEARCH ps = parts_search_arr.get(position);
                int int_source_num = Integer.parseInt(ps.LABST);
                if (int_num <= 0)
                    {
                    EventPopupC epc = new EventPopupC(context);
                    epc.show("입력수량이 없습니다.");
                    ps.REQUEST = "0";
                    if (PartsTransfer_Search_Adapter.checked_items
                        .contains(position))
                        {
                        PartsTransfer_Search_Adapter.checked_items
                            .remove(PartsTransfer_Search_Adapter.checked_items
                                .indexOf(position));
                        }
                    }
                else if (int_source_num < int_num)
                    {
                    EventPopupC epc = new EventPopupC(context);
                    epc.show("재고수량보다 많이 이관할 수 없습니다.");
                    }
                else
                    {
                    NUM = num;
                    POSITION = position;
                    ps.REQUEST = num;
                    if (!PartsTransfer_Search_Adapter.checked_items
                        .contains(position))
                        {
                        PartsTransfer_Search_Adapter.checked_items
                            .add(position);
                        }
                    }
                ptsa.notifyDataSetChanged();
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
                for(int i=0;i<BASE_PARTS_ITEMS.size();i++)
                    {
                    PARTS_SEARCH bbb = BASE_PARTS_ITEMS.get(i);
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
        ptsra = new PartsTransfer_Search_Right_Adapter(context, R.layout.partstransfer_search_right_row, NOW_LIST_ARR);
        lv_right.setAdapter(ptsra);
        lv_right.setOnItemClickListener(new OnItemClickListener()
            {
            @Override
            public void onItemClick(AdapterView<?> arg0, View v, int position, long arg3)
                {
//                Log.i("####","#### 리스트 클릭");
                PARTS_SEARCH ps = NOW_LIST_ARR.get(position);
                ps.CHECKED = !ps.CHECKED;
                ptsra.notifyDataSetChanged(); 
                }
            });
        
        if(NOW_LIST_ARR.size()>0)
            {
            iv_nodata_right.setVisibility(View.GONE);
            }
        else{
            iv_nodata_right.setVisibility(View.VISIBLE);
            }
        }

    @Override
    public void reDownloadDB(String newVersion)
        {}

    @Override
    public void onClick(View arg0)
        {
        Object data;
        switch (arg0.getId())
            {
            case R.id.parts_transfer_group_popup_id: // 자재그룹
                pwM028.show(bt_group);
                break;
            case R.id.parts_transfer_partscode_search_id: // 부품코드조회
                data = bt_parts_code_search.getText();
                if (data != null && !data.toString().equals(""))
                    {
                    ViewGroup vg = pwDelSearch.getViewGroup();
                    LinearLayout back = (LinearLayout) vg.getChildAt(0);
                    final Button del = (Button) back.getChildAt(0);
                    del.setOnClickListener(new OnClickListener() {

                        @Override
                        public void onClick(View v)
                            {
                            tv_parts_name.setText("");
                            bt_parts_code_search.setText("");
                            pwDelSearch.dismiss();
                            }
                    });
                    final Button sel = (Button) back.getChildAt(1);
                    sel.setOnClickListener(new OnClickListener() {

                        @Override
                        public void onClick(View v)
                            {
                            showPartSearchDialog();
                            pwDelSearch.dismiss();
                            }
                    });
                    pwDelSearch.show(bt_parts_code_search);
                    }
                else
                    {
                    showPartSearchDialog();
                    }
                break;
            case R.id.parts_transfer_parts_getlist_id: // 부품리스트조회
            
                parts_search_arr = new ArrayList<PARTS_SEARCH>();
                ptsa = new PartsTransfer_Search_Adapter(context, R.layout.partstransfer_row, parts_search_arr);
                lv_partslist.setAdapter(ptsa);
                iv_nodata.setVisibility(View.VISIBLE);
            
                showProgress("부품리스트를 조회 중입니다.");
                data = tv_parts_name.getText();
                String str = data != null && !data.equals("") ? data.toString()
                    : " ";
                connectController.getPartsSearch(P028_TAG, str);
                break;
            case R.id.parts_transfer_done_id: // 이관완료
                data = bt_car_search.getText();
                str = data != null && !data.equals("") ? data.toString() : "";
                if (str.equals(""))
                    {
                    EventPopupC epc = new EventPopupC(context);
                    epc.show("입고지를 입력해 주십시오");
                    return;
                    }
                data = et_reason.getText();
                str = data != null && !data.equals("") ? data.toString() : "";
                if (str.equals(""))
                    {
                    EventPopupC epc = new EventPopupC(context);
                    epc.show("이관사유를 입력해 주십시오");
                    return;
                    }
                if (PartsTransfer_Search_Adapter.checked_items == null
                    || PartsTransfer_Search_Adapter.checked_items.size() <= 0)
                    return;
                showProgress("이관완료 신청 중입니다.");
                connectController.writeZMO_1030_WR05(makeZMO_1030_WR05());
                break;
            case R.id.parts_transfer_stock_car_search_id: // 순회차량조회
                data = bt_car_search.getText();
                if (data != null && !data.toString().equals(""))
                    {
                    ViewGroup vg = pwDelSearch.getViewGroup();
                    LinearLayout back = (LinearLayout) vg.getChildAt(0);
                    final Button del = (Button) back.getChildAt(0);
                    del.setOnClickListener(new OnClickListener() {

                        @Override
                        public void onClick(View v)
                            {
                            bt_car_search.setText("");
                            pwDelSearch.dismiss();
                            }
                    });
                    final Button sel = (Button) back.getChildAt(1);
                    sel.setOnClickListener(new OnClickListener() {

                        @Override
                        public void onClick(View v)
                            {
                            showCarSearchDialog();
                            pwDelSearch.dismiss();
                            }
                    });
                    pwDelSearch.show(bt_car_search);
                    }
                else
                    {
                    showCarSearchDialog();
                    }
                break;
                
            case R.id.partstransfer_right_sort1_id:
                break;
            case R.id.partstransfer_right_sort2_id:
                SORT_MODE++;
                SORT_MODE = SORT_MODE>=2?0:SORT_MODE;
                sortMode(SORT_MODE);
                ptsra.notifyDataSetChanged();
                break;
                
            case R.id.partstransfer_left_sort2_id:
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
                ptsa.notifyDataSetChanged();
                break;
            }
        }
    
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

    private void showCarSearchDialog()
        {
        final PartsTransfer_Cars_Dialog ptcd = new PartsTransfer_Cars_Dialog(
            context);
        Button bt_done_car = (Button) ptcd
            .findViewById(R.id.partstransfer_car_search_done_id);
        bt_done_car.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v)
                {
                PartsTransfer_Cars_Dialog_Adapter ptcda = ptcd.getPcda();
                if (ptcda == null)
                    return;
                ArrayList<HashMap<String, String>> arr_hash = ptcd
                    .getArray_hash();
                int position = ptcda.getCheckPosition();
                if (position == Integer.MIN_VALUE)
                    return;
                HashMap<String, String> map = arr_hash.get(position);
                bt_car_search.setText(map.get("INVNR"));
                ptcd.dismiss();
                }
        });
        ptcd.show();
        }

    private void showPartSearchDialog()
        {
        final PartsTransfer_Parts_Dialog ptpd = new PartsTransfer_Parts_Dialog(
            context);
        Button bt_done = (Button) ptpd
            .findViewById(R.id.partstransfer_parts_search_save_id);
        bt_done.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v)
                {
                PartsTransfer_Parts_Dialog_Adapter ptpda = ptpd.getPpda();
                if (ptpda == null)
                    return;
                ArrayList<HashMap<String, String>> arr_hash = ptpd
                    .getArray_hash();
                int position = ptpd.getPpda().getCheckPosition();
                HashMap<String, String> map = arr_hash.get(position);
                bt_parts_code_search.setText(map.get("MATNR"));
                tv_parts_name.setText(map.get("MAKTX"));
                P028_TAG = map.get("MATKL");
                String group_name = getPM023name(P028_TAG);
                bt_group.setText(group_name);
                ptpd.dismiss();
                }
        });
        ptpd.show();
        }

    private ArrayList<HashMap<String, String>> makeZMO_1030_WR05()
        {
        LoginModel model = KtRentalApplication.getLoginModel();
        ArrayList<HashMap<String, String>> i_itab1 = new ArrayList<HashMap<String, String>>();
        for (int i = 0; i < NOW_LIST_ARR.size(); i++)
            {
//            int checked_num = PartsTransfer_Search_Adapter.checked_items.get(i);
            PARTS_SEARCH ps = NOW_LIST_ARR.get(i);
            HashMap<String, String> hm = new HashMap<String, String>();
            hm.put("MATNR", ps.MATNR); // 자재번호
            hm.put("WERKS", model.getWerks()); // 플랜트(파라미터 다름 확인됨)
            hm.put("INVNR", model.getInvnr()); // 재고번호(순회차량번호)
            hm.put("MOVE_TYPE", "311"); // 이동 유형(재고 관리)
            hm.put("ENTRY_QNT", ps.REQUEST); // 입력 단위 수량
            hm.put("ENTRY_UOM", ps.MEINS); // 입력단위
            Object data = bt_car_search.getText();
            String str = data != null && !data.equals("") ? data.toString() : " ";
            hm.put("INVNR_O", str); // 재고번호(차량번호)
            data = et_reason.getText();
            str = data != null && !data.equals("") ? data.toString() : " ";
            hm.put("BIGO", str); // 비고
            i_itab1.add(hm);
            }
        return i_itab1;
        }

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
                    bt_group.setText(bt.getText().toString());
                    P028_TAG = bt.getTag().toString();
                    pwM028.dismiss();
                    }
            });
            }
        }

    private String getPM023name(String _num)
        {
        String name = " ";
        for (int i = 0; i < pm023_arr.size(); i++)
            {
            if (pm023_arr.get(i).ZCODEV.equals(_num))
                {
                name = pm023_arr.get(i).ZCODEVT;
                }
            }
        return name;
        }
    public static ArrayList<PM023> pm023_arr;

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
        return pm023_arr;
        }

    private void initQueryItem()
        {
        showProgress();
        String[] _whereArgs = null;
        String[] _whereCause = null;
        String[] colums = null;
        DbQueryModel dbQueryModel = new DbQueryModel(DEFINE.STOCK_TABLE_NAME,
            _whereCause, _whereArgs, colums);
        dbQueryModel.setOrderBy(DEFINE.MATKL + " asc");
        DbAsyncTask dbAsyncTask = new DbAsyncTask("initQueryItem", mContext,
            new DbAsyncResLintener() {

                @Override
                public void onCompleteDB(String funName, int type,
                    Cursor cursor, String tableName)
                    {
                    // TODO Auto-generated method stub
                    hideProgress();
                    if (cursor != null)
                        {
                        cursor.moveToFirst();
                        ArrayList<HashMap<String, String>> updateArr = new ArrayList<HashMap<String, String>>();
                        ArrayList<HashMap<String, String>> i_itab1 = new ArrayList<HashMap<String, String>>();
                        for (int i = 0; i < PartsTransfer_Search_Adapter.checked_items
                            .size(); i++)
                            {
                            int checked_num = PartsTransfer_Search_Adapter.checked_items
                                .get(i);
                            PARTS_SEARCH rd04 = parts_search_arr
                                .get(checked_num);
                            HashMap<String, String> hm = new HashMap<String, String>();
                            hm.put("MATKL", rd04.MATKL);
                            hm.put("GRP_CD", rd04.GRP_CD);
                            hm.put("MAKTX", rd04.MAKTX);
                            hm.put("MEINS", rd04.MEINS);
                            hm.put("MATNR", rd04.MATNR);
                            hm.put("REQUEST", rd04.REQUEST);
                            hm.put("LTEXT", " ");
                            hm.put("BUKRS", rd04.BUKRS);
                            i_itab1.add(hm);
                            }
                        while (!cursor.isAfterLast())
                            {
                            HashMap<String, String> map = setStockDB(
                                                                     cursor, i_itab1);
                            if (map != null)
                                updateArr.add(map);
                            // if (i_itab1.size() > 0)
                            cursor.moveToNext();
                            // else{
                            // break;
                            // }
                            }
                        cursor.close();
                        // for (HashMap<String, String> hashMap : i_itab1) {
                        // String REQUEST = hashMap.get("REQUEST");
                        // hashMap.put("LABST", "" + REQUEST);
                        // hashMap.remove("REQUEST");
                        //
                        // updateArr.add(hashMap);
                        // }
                        if (updateArr.size() > 0)
                            updateStockDB(updateArr);
                        }
                    }
            }, dbQueryModel);
        dbAsyncTask.execute(DbAsyncTask.DB_SELECT);
        }

    private HashMap<String, String> setStockDB(Cursor cursor,
        ArrayList<HashMap<String, String>> i_itab1)
        {
        HashMap<String, String> map = null;
        String matkl = cursor.getString(cursor.getColumnIndex("MATKL"));
        String grpcd = cursor.getString(cursor.getColumnIndex("GRP_CD"));
        String maktx = cursor.getString(cursor.getColumnIndex("MAKTX"));
        String meins = cursor.getString(cursor.getColumnIndex("MEINS"));
        String matnr = cursor.getString(cursor.getColumnIndex("MATNR"));
        String labst = cursor.getString(cursor.getColumnIndex("LABST"));
        String ltext = cursor.getString(cursor.getColumnIndex("LTEXT"));
        String burks = cursor.getString(cursor.getColumnIndex("BUKRS"));
        for (HashMap<String, String> hashMap : i_itab1)
            {
            map = hashMap;
            String resultMatnr = hashMap.get("MATNR");
            String resultMatkl = hashMap.get("MATKL");
            String REQUEST = hashMap.get("REQUEST");
            if (matnr.equals(resultMatnr))
                {// && matkl.equals(resultMatkl)) {
//                Log.d("HONG", "resultMatnr " + resultMatnr);
                int iLabst = Integer.parseInt(labst);
                int iREQUEST = Integer.parseInt(REQUEST);
                iLabst = iLabst - iREQUEST;
                map.put("LABST", "" + iLabst);
                map.remove("REQUEST");
                i_itab1.remove(hashMap);
                break;
                }
            // else {
            // HashMap<String, String> map = new HashMap<String, String>();
            // map.put("MATKL", matkl);
            // map.put("GRP_CD", grpcd);
            // map.put("MAKTX", maktx);
            // map.put("MEINS", meins);
            // map.put("MATNR", rd04.MATNR);
            // map.put("LABST", rd04.INC_QTY);
            // map.put("INC_QTY", rd04.INC_QTY);
            // map.put("LTEXT", " ");
            // map.put("BURKS", rd04.WERKS);
            // // i_itab1.add(object)
            // }
            }
        return map;
        }

    private void updateStockDB(ArrayList<HashMap<String, String>> i_itab1)
        {
        showProgress();
        HashMap<String, ArrayList<HashMap<String, String>>> tableArrayMap = new HashMap<String, ArrayList<HashMap<String, String>>>();
        HashMap<String, HashMap<String, String>> resStructMap = new HashMap<String, HashMap<String, String>>();
        String[] reTableNames = new String[1];
        reTableNames[0] = "I_ITAB1";
        tableArrayMap.put(reTableNames[0], i_itab1);
        String[] tableNames = new String[1];
        TableModel tableModel = new TableModel(tableNames, tableArrayMap,
            resStructMap, null, "");
        HashMap<String, String> loginTableNameMap = new HashMap<String, String>();
        loginTableNameMap.put("I_ITAB1", DEFINE.STOCK_TABLE_NAME);
        DbAsyncTask asyncTask = new DbAsyncTask(
            ConnectController.REPAIR_FUNTION_NAME, DEFINE.STOCK_TABLE_NAME,
            mContext, new DbAsyncResLintener() {

                @Override
                public void onCompleteDB(String funName, int type,
                    Cursor cursor, String tableName)
                    {
                    hideProgress();
                    // refresh
                    }
            }, // DbAsyncResListener
            tableModel, loginTableNameMap);
        asyncTask.execute(DbAsyncTask.DB_ARRAY_INSERT);
        }
}
