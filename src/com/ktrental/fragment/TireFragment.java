package com.ktrental.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.PhoneNumberUtils;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.ktrental.R;
import com.ktrental.activity.Main_Activity;
import com.ktrental.adapter.Tire_Adapter;
import com.ktrental.beans.PM081;
import com.ktrental.beans.PMO23;
import com.ktrental.cm.connect.ConnectController;
import com.ktrental.cm.connect.Connector.ConnectInterface;
import com.ktrental.common.KtRentalApplication;
import com.ktrental.dialog.Address_Dialog;
import com.ktrental.dialog.Camera_Dialog;
import com.ktrental.dialog.History_Dialog;
import com.ktrental.dialog.Tire_Picture_Dialog;
import com.ktrental.dialog.Tire_Provide_Company_Dialog;
import com.ktrental.dialog.Tire_Search_Dialog;
import com.ktrental.model.LoginModel;
import com.ktrental.model.TableModel;
import com.ktrental.popup.DatepickPopup2;
import com.ktrental.popup.EventPopup2;
import com.ktrental.popup.EventPopupC;
import com.ktrental.popup.InventoryPopup;
import com.ktrental.popup.PM082_Popup;
import com.ktrental.popup.Popup_Window_DelSearch;
import com.ktrental.popup.QuickAction;
import com.ktrental.product.Menu2_1_Activity;
import com.ktrental.product.Menu4_1_Activity;
import com.ktrental.util.OnChangeFragmentListener;
import com.ktrental.util.kog;

import java.io.File;
import java.io.InputStream;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import static android.app.Activity.RESULT_OK;

public class TireFragment extends BaseFragment implements OnClickListener, ConnectInterface
{

    private Context                     context;
    // private ProgressDialog pd;
    private ConnectController           cc;

    private String                      CARNUM        = "";
    private String                      EQUNR         = "";

    private TextView                    tv_carnum;
    private TextView                    tv_contractor1;
    private TextView                    tv_contractor2;
    private TextView                    tv_snowtire;
    private TextView                    tv_normaltire;
    private TextView                    tv_tirecode1;
    private TextView                    tv_tiremade1;
    private TextView                    tv_tirecode2;
    private TextView                    tv_tiremade2;
    private TextView                    tv_sparetire;
    private TextView                    tv_place;
    private EditText                    et_receiptor;
    private TextView                    tv_receipter_phone;
    
    
    private TextView					tire_similar_id;

    private DatepickPopup2              dp_date;
    private Button                      bt_releasedate;

    
    private Button                      bt_provide_company;
    private Tire_Provide_Company_Dialog tpcd;
    // private TextView tv_providor_name;
    // private TextView tv_providor_phone;
    private TextView                    tv_providor_address;

    private Button                      bt_acceptor;
    // private TextView tv_acceptor_name;
    // private TextView tv_acceptor_zip;
    private TextView                    tv_acceptor_address;

    private Button                      bt_pm082;
    private PM082_Popup                 pm082;
    private String                      PM082_TAG;

    private Button                      bt_tire_front;
    private Button                      bt_tire_rear;
    private Tire_Search_Dialog          tsd;
    // private TextView tv_front;
    // private TextView tv_rear;

    private Button                      bt_add;
    private Button                      bt_delete;
    private Tire_Picture_Dialog         tpd;

    public static ArrayList<PM081>      pm081_arr     = new ArrayList<PM081>();
    public static ArrayList<String>     pm081_del_arr = new ArrayList<String>();
    private ScrollView                  sv;
    private ListView                    lv_list;
    private Tire_Adapter                ta;

    private Button                      bt_request;
    private RadioGroup                  rg_tire_kind;
    private RadioButton                 rb_normal;
    private RadioButton                 rb_snow;
    private CheckBox                    cb_brandnew;
    // 2014-02-18 KDH 정책변경 삭제.
    // private CheckBox cb_emergency;
    private TextView                    tv_customer_num;
    private TextView                    tv_last_milease;
    private TextView                    tv_mileage;
    private TextView                    tv_address;

    private TextView                    tv_cumlqty_cumtqty;
    private TextView                    tv_tchgdt_chgrun;

    private CheckBox                    cb_same;

    private Button                      bt_change_history;

    private InventoryPopup              inventoryPopup;

    // 필수항목
    private EditText                    et_customer_request;
    private EditText                    et_acceptor_issue;

    private Button                      bt_acceptor_address;

    private Popup_Window_DelSearch      pwDelSearch;

    private TextView                    tv_bon;

    private Button                      bt_search;

    private Button                      bt_tire_voc_info;

    String                              TIRE_TYPE     = "1";

    public TireFragment(){}

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1){
            if(resultCode == RESULT_OK){
                try {
                    if(tpd != null){
                        try {
                            Uri selectedImage = data.getData();
                            InputStream in = context.getContentResolver().openInputStream(selectedImage);
                            Bitmap img = BitmapFactory.decodeStream(in);
                            tpd.sendImg(img, data);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void startActivityGallery(){
        try {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            if(context instanceof  Main_Activity) {
                ((Main_Activity) context).setFragment(this);
                ((Main_Activity) context).startActivityForResult(intent, 1);
            } else if(context instanceof Menu4_1_Activity){
                ((Menu4_1_Activity) context).setFragment(this);
                ((Menu4_1_Activity) context).startActivityForResult(intent, 1);
            } else if(context instanceof Menu2_1_Activity){
                ((Menu2_1_Activity) context).setFragment(this);
                ((Menu2_1_Activity) context).startActivityForResult(intent, 1);
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }


    @SuppressLint("ValidFragment")
    public TireFragment(String className, OnChangeFragmentListener changeFragmentListener, String carnum, String equnr)
    {
        super(className, changeFragmentListener);

        this.CARNUM = carnum;
        this.EQUNR = equnr;

        kog.e("Jonathan", "타이어교체 => 누름. 받아와~!!");
        kog.e("Jonathan", "타이어교체CARNUM  :: " + carnum);
        kog.e("Jonathan", "타이어교체EQUNR  ::  " + equnr);
        kog.e("Jonathan", "타이어교체className ::  " + className);
        kog.e("Jonathan", "타이어교체ChangeFragmentListener :: " + changeFragmentListener);

    }

    @Override
    public void onAttach(Activity activity)
    {

        kog.e("Jonathan", "onAttach 부분  여기서 activity 작동?");

        context = activity;
        cc = new ConnectController(this, context);

        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH) + 1;
        int day = cal.get(Calendar.DAY_OF_MONTH);
        String month_str = String.format("%02d", month);
        String day_str = String.format("%02d", day);

        dp_date = new DatepickPopup2(context, year + month_str + day_str, 2);

        pm082 = new PM082_Popup(context);
        inventoryPopup = new InventoryPopup(context, QuickAction.HORIZONTAL, R.layout.inventory_popup, InventoryPopup.TYPE_PHONE_NUMBER);
        inventoryPopup.setOnDismissListener();

        pwDelSearch = new Popup_Window_DelSearch(context);

        super.onAttach(activity);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {

        kog.e("Jonathan", "화면 그리는 곳 처음.");

        View v = inflater.inflate(R.layout.tirefragment, null);
        tv_carnum = (TextView) v.findViewById(R.id.tire_carnum_id);
        tv_carnum.setOnClickListener(this);
        if (kog.TEST_ADMIN)
        {
            tv_carnum.setText("82라7149");
        }
        tv_contractor1 = (TextView) v.findViewById(R.id.tire_contractor_name1_id);
        tv_contractor2 = (TextView) v.findViewById(R.id.tire_contractor_name2_id);
        tv_snowtire = (TextView) v.findViewById(R.id.tire_snowtire_id);
        tv_normaltire = (TextView) v.findViewById(R.id.tire_normaltire_id);
        tv_tirecode1 = (TextView) v.findViewById(R.id.tire_tirecode1_id);
        tv_tiremade1 = (TextView) v.findViewById(R.id.tire_tiremade1_id);
        tv_tirecode2 = (TextView) v.findViewById(R.id.tire_tirecode2_id);
        tv_tiremade2 = (TextView) v.findViewById(R.id.tire_tiremade2_id);
        tv_sparetire = (TextView) v.findViewById(R.id.tire_sparetire_id);
        tv_place = (TextView) v.findViewById(R.id.tire_place_id);
        et_receiptor = (EditText) v.findViewById(R.id.tire_receiptor_id);
        tv_receipter_phone = (TextView) v.findViewById(R.id.tire_receiptor_phone_id); // 인수자 연락처
        tv_receipter_phone.setOnClickListener(this);
        
        tire_similar_id = (TextView)v.findViewById(R.id.tire_similar_id);

        bt_releasedate = (Button) v.findViewById(R.id.tire_releasedate_id);
        bt_releasedate.setOnClickListener(this);

        bt_provide_company = (Button) v.findViewById(R.id.tire_provide_company_id);
        bt_provide_company.setOnClickListener(this);

        // tv_providor_name =
        // (TextView)v.findViewById(R.id.tire_provide_company_name_id);
        // tv_providor_phone =
        // (TextView)v.findViewById(R.id.tire_provide_company_phone_id);
        // tv_providor_phone.setOnClickListener(this);
        tv_providor_address = (TextView) v.findViewById(R.id.tire_provide_company_address_id);

        bt_acceptor = (Button) v.findViewById(R.id.tire_acceptor_id);
        bt_acceptor.setOnClickListener(this);
        // tv_acceptor_name =
        // (TextView)v.findViewById(R.id.tire_acceptor_name_id);
        // tv_acceptor_zip =
        // (TextView)v.findViewById(R.id.tire_acceptor_zip_id);
        tv_acceptor_address = (TextView) v.findViewById(R.id.tire_acceptor_address_id);

        bt_pm082 = (Button) v.findViewById(R.id.tire_pm082_id);
        bt_pm082.setOnClickListener(this);

        bt_tire_front = (Button) v.findViewById(R.id.tire_search_front_id);
        bt_tire_front.setOnClickListener(this);
        bt_tire_rear = (Button) v.findViewById(R.id.tire_search_rear_id);
        bt_tire_rear.setOnClickListener(this);

        // tv_front = (TextView)v.findViewById(R.id.tire_front_id);
        // tv_rear = (TextView)v.findViewById(R.id.tire_rear_id);

        bt_add = (Button) v.findViewById(R.id.tire_add_id);
        bt_delete = (Button) v.findViewById(R.id.tire_delete_id);
        bt_add.setOnClickListener(this);
        bt_delete.setOnClickListener(this);

        sv = (ScrollView) v.findViewById(R.id.scrollView1);
        lv_list = (ListView) v.findViewById(R.id.tire_list_id);
        lv_list.setOnTouchListener(new OnTouchListener()
        {
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                sv.requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });

        bt_request = (Button) v.findViewById(R.id.tire_request_id);
        bt_request.setOnClickListener(this);

        rg_tire_kind = (RadioGroup) v.findViewById(R.id.tire_rg_id);
        rb_normal = (RadioButton) v.findViewById(R.id.tire_rb_normal_id);
        rb_snow = (RadioButton) v.findViewById(R.id.tire_rb_snow_id);
        cb_brandnew = (CheckBox) v.findViewById(R.id.tire_brandnew_id);
        // cb_emergency = (CheckBox) v.findViewById(R.id.tire_emergency_id);

        tv_customer_num = (TextView) v.findViewById(R.id.tire_customer_num_id);
        tv_customer_num.setOnClickListener(this);
        tv_last_milease = (TextView) v.findViewById(R.id.last_milease_id);
        tv_mileage = (TextView) v.findViewById(R.id.tire_mileage_id);
        tv_mileage.setOnClickListener(this);
        tv_address = (TextView) v.findViewById(R.id.tire_address_id);

        tv_cumlqty_cumtqty = (TextView) v.findViewById(R.id.tire_cumlqty_cumtqty_id);
        tv_tchgdt_chgrun = (TextView) v.findViewById(R.id.tire_tchgdt_chgrun_id);

        cb_same = (CheckBox) v.findViewById(R.id.tire_same_check_id);
        cb_same.setOnCheckedChangeListener(new OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton arg0, boolean arg1)
            {
                // myung 20131216 ADD 타이어 검색 후 앞뒤 동일 체크 시에도 동일하게 적용
                if (arg1)
                {
                    String strFront = bt_tire_front.getText().toString();
                    String strRear = bt_tire_rear.getText().toString();
                    if (!strFront.equals("") && !strRear.equals(""))
                    {
                        cb_same.setChecked(false);
                        return;
                    }
                    if (!strFront.equals(""))
                    {
                        bt_tire_rear.setText(strFront);
                        bt_tire_rear.setTag(bt_tire_front.getTag().toString());
                        bt_tire_rear.setTextColor(Color.RED);
                        return;
                    }
                    if (!strRear.equals(""))
                    {
                        bt_tire_front.setText(strRear);
                        bt_tire_front.setTag(bt_tire_rear.getTag().toString());
                        bt_tire_front.setTextColor(Color.RED);
                        return;
                    }

                }
            }
        });

        bt_change_history = (Button) v.findViewById(R.id.tire_change_history_id);
        bt_change_history.setOnClickListener(this);

        et_customer_request = (EditText) v.findViewById(R.id.tire_customer_request_id);
        et_acceptor_issue = (EditText) v.findViewById(R.id.tire_acceptor_issue_id);

        bt_acceptor_address = (Button) v.findViewById(R.id.tire_acceptor_zip_search_id);
        bt_acceptor_address.setOnClickListener(this);

        tv_bon = (TextView) v.findViewById(R.id.tire_search_bon_id);

        bt_search = (Button) v.findViewById(R.id.tire_search_id);
        bt_search.setOnClickListener(this);
        
//        bt_tire_voc_info = (Button) v.findViewById(R.id.btn_tire_voc_info_id);
//        bt_tire_voc_info.setOnClickListener(this);
        
        // init();
        setPM082Click();
        if (!CARNUM.equals(""))
        {
            showProgress("상세조회 중 입니다.");
            cc.getZMO_1060_RD03(CARNUM, EQUNR);
        }

        kog.e("Jonathan", "화면 그리는 곳 끝.");
        return v;
    }

    // private void init() {
    // // dp_date.setOnDismissListener(new OnDismissListener()
    // // {
    // // @Override
    // // public void onDismiss()
    // // {
    // // if(bt_releasedate.getTag()==null) return;
    // // long time = ((Long)bt_releasedate.getTag())+((1000*60*60*24)-1);
    // // long now = System.currentTimeMillis();
    // // if(now>=time)
    // // {
    // // EventPopupC epc = new EventPopupC(context);
    // // epc.show("과거 일자는 선택 할 수 없습니다.\n출고 요청일을 확인해 주세요");
    // // bt_releasedate.setText("");
    // // bt_releasedate.setTag("");
    // // }
    // // }
    // // });
    // }

    private void setPM082Click()
    {
        ArrayList<PMO23> pm082_arr = pm082.pm082_arr;
        bt_pm082.setText(pm082_arr.get(0).ZCODEVT);
        bt_pm082.setTag(pm082_arr.get(0).ZCODEV);
        PM082_TAG = pm082_arr.get(0).ZCODEV;

        ViewGroup vg = pm082.getViewGroup();
        LinearLayout back = (LinearLayout) vg.getChildAt(0);
        for (int i = 0; i < back.getChildCount(); i++)
        {
            LinearLayout row_back = (LinearLayout) back.getChildAt(i);
            final Button bt = (Button) row_back.getChildAt(0);
            bt.setOnClickListener(new OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    // Toast.makeText(context, bt.getTag().toString()+"테그입니다",
                    // 0).show();
                    bt_pm082.setText(bt.getText().toString());
                    PM082_TAG = bt.getTag().toString();
                    kog.e("KDH", "bt_pm082 = " + bt_pm082);
                    kog.e("KDH", "PM082_TAG = " + PM082_TAG);
                    TIRE_TYPE = PM082_TAG;
                    pm082.dismiss();
                }
            });
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onClick(View arg0)
    {
        Object data;
        String str;
        switch (arg0.getId())
        {
            case R.id.tire_releasedate_id:
                sv.scrollTo(0, 500);
                dp_date.show(bt_releasedate);
                break;

            case R.id.tire_provide_company_id:
                data = bt_provide_company.getText();
                if (data != null && !data.toString().equals(""))
                {
                    ViewGroup vg = pwDelSearch.getViewGroup();
                    LinearLayout back = (LinearLayout) vg.getChildAt(0);
                    final Button del = (Button) back.getChildAt(0);
                    del.setOnClickListener(new OnClickListener()
                    {
                        @Override
                        public void onClick(View v)
                        {
                            // tv_providor_phone.setText("");
                            tv_providor_address.setText("");
                            bt_provide_company.setText("");
                            pwDelSearch.dismiss();
                        }
                    });
                    final Button sel = (Button) back.getChildAt(1);
                    sel.setOnClickListener(new OnClickListener()
                    {
                        @Override
                        public void onClick(View v)
                        {
                            showProvideCompanyDialog();
                            pwDelSearch.dismiss();
                        }
                    });
                    pwDelSearch.show(bt_provide_company);
                }
                else
                {
                    showProvideCompanyDialog();
                }
                break;
            case R.id.tire_acceptor_id:

                data = bt_acceptor.getText();
                if (data != null && !data.toString().equals(""))
                {
                    ViewGroup vg = pwDelSearch.getViewGroup();
                    LinearLayout back = (LinearLayout) vg.getChildAt(0);
                    final Button del = (Button) back.getChildAt(0);
                    del.setOnClickListener(new OnClickListener()
                    {
                        @Override
                        public void onClick(View v)
                        {
                            bt_acceptor.setText("");
                            tv_receipter_phone.setText("");
                            bt_acceptor_address.setText("");
                            tv_acceptor_address.setText("");
                            pwDelSearch.dismiss();
                        }
                    });
                    final Button sel = (Button) back.getChildAt(1);
                    sel.setOnClickListener(new OnClickListener()
                    {
                        @Override
                        public void onClick(View v)
                        {
                            showProvideCompanyDialog2();
                            pwDelSearch.dismiss();
                        }
                    });
                    pwDelSearch.show(bt_acceptor);
                }
                else
                {
                    showProvideCompanyDialog2();
                }
                break;

            case R.id.tire_pm082_id:
                sv.scrollTo(0, 500);
                pm082.show(bt_pm082);
                break;

            case R.id.tire_search_front_id:
                data = bt_tire_front.getText();
                if (data != null && !data.toString().equals(""))
                {
                    ViewGroup vg = pwDelSearch.getViewGroup();
                    LinearLayout back = (LinearLayout) vg.getChildAt(0);
                    final Button del = (Button) back.getChildAt(0);
                    del.setOnClickListener(new OnClickListener()
                    {
                        @Override
                        public void onClick(View v)
                        {
                            bt_tire_front.setText("");
                            bt_tire_front.setTag("");
                            pwDelSearch.dismiss();
                        }
                    });
                    final Button sel = (Button) back.getChildAt(1);
                    sel.setOnClickListener(new OnClickListener()
                    {
                        @Override
                        public void onClick(View v)
                        {
                            showTireFront();
                            pwDelSearch.dismiss();
                        }
                    });
                    pwDelSearch.show(bt_tire_front);
                }
                else
                {
                    showTireFront();
                }
                break;
            case R.id.tire_search_rear_id:

                data = bt_tire_rear.getText();
                if (data != null && !data.toString().equals(""))
                {
                    ViewGroup vg = pwDelSearch.getViewGroup();
                    LinearLayout back = (LinearLayout) vg.getChildAt(0);
                    final Button del = (Button) back.getChildAt(0);
                    del.setOnClickListener(new OnClickListener()
                    {
                        @Override
                        public void onClick(View v)
                        {
                            bt_tire_rear.setText("");
                            bt_tire_rear.setTag("");
                            pwDelSearch.dismiss();
                        }
                    });
                    final Button sel = (Button) back.getChildAt(1);
                    sel.setOnClickListener(new OnClickListener()
                    {
                        @Override
                        public void onClick(View v)
                        {
                            showTireRear();
                            pwDelSearch.dismiss();
                        }
                    });
                    pwDelSearch.show(bt_tire_rear);
                }
                else
                {
                    showTireRear();
                }
                break;

            case R.id.tire_add_id:
                if (bt_tire_front.getText().equals("") && bt_tire_rear.getText().equals(""))
                {
                    EventPopupC epc = new EventPopupC(context);
                    epc.show("타이어를 앞/뒤를 선택해 주십시오");
                    return;
                }

                int mode = 0;
                if (bt_tire_front.getText().length() > 0 && bt_tire_rear.getText().length() > 0)
                {
                    mode = 0;
                }
                else if (bt_tire_front.getText().length() > 0 && bt_tire_rear.getText().length() <= 0)
                {
                    mode = 1;
                }
                else if (bt_tire_front.getText().length() <= 0 && bt_tire_rear.getText().length() > 0)
                {
                    mode = 2;
                }

                // myung 20131202 UPDATE PM081동기화
//                tpd = new Tire_Picture_Dialog(context, mode, pm081_arr);
                tpd = new Tire_Picture_Dialog(context, mode, pm081_arr, this);
                
                // tpd = new Tire_Picture_Dialog(context, mode);
                Button tpd_done = (Button) tpd.findViewById(R.id.tire_picture_save_id);
                tpd_done.setOnClickListener(new OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        // 전체항목
                        // myung 20121212 UPDATE 추가 클릭하여 차량사진촬영 화면 팝업 시 삭제한 타이어도 체크되어 있는 문제
                        // pm081_arr = new ArrayList<PM081>();
                        pm081_arr.clear();
                        
                        
                        for (int i = 0; i < Tire_Picture_Dialog.pm081.size(); i++)
                        {
                            if (Tire_Picture_Dialog.pm081.get(i).CHECKED)
                            {
                                pm081_arr.add(Tire_Picture_Dialog.pm081.get(i));
                            }
                        }
                        String CHECK_BON[] = {
                                "30", "40", "50", "60", "70", "80", "90", };
                        int bon = 0;
                        for (int i = 0; i < pm081_arr.size(); i++)
                        {
                            for (int j = 0; j < CHECK_BON.length; j++)
                            {
                                if (CHECK_BON[j].equals(pm081_arr.get(i).ZCODEV))
                                {
                                    bon++;
                                }
                            }
                        }
                        // for (int i = 3; i < Tire_Picture_Dialog.pm081.size(); i++) {
                        // if (Tire_Picture_Dialog.pm081.get(i).CHECKED)
                        // bon++;
                        // }
                        tv_bon.setText(bon + "본");

                        // schneider
                        ta = new Tire_Adapter(context, R.layout.tire_row, pm081_arr);
                        lv_list.setAdapter(ta);

                        // 리스트 클릭
                        lv_list.setOnItemClickListener(new OnItemClickListener()
                        {
                            @Override
                            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3)
                            {
                                if (!pm081_del_arr.contains(pm081_arr.get(position).ZCODEV))
                                {
                                    pm081_del_arr.add(pm081_arr.get(position).ZCODEV);
                                }
                                else
                                {
                                    pm081_del_arr.remove(pm081_arr.get(position).ZCODEV);
                                }
                                ta.notifyDataSetChanged();
                            }
                        });
                        tpd.dismiss();
                        
                    }
                });
                tpd.show();
                break;
            case R.id.tire_delete_id:
                if (pm081_del_arr.size() <= 0)
                    return;

                for (int i = 0; i < pm081_arr.size(); i++)
                {
                    for (int j = 0; j < pm081_del_arr.size(); j++)
                    {
                        if (pm081_arr != null && pm081_arr.size() > i && pm081_arr.get(i).ZCODEV.equals(pm081_del_arr.get(j)))
                        {
                            pm081_arr.remove(i);
                        }
                    }
                }
                ta.notifyDataSetChanged();
                break;

            case R.id.tire_request_id:
                if (!checkField())
                    return;

                // Jonathan 14.10.23 미가입 시 타이어 신청 안되게 막는다.
                if ("미가입".equals(notir_tx))
                {
                    EventPopup2 eventPopup = new EventPopup2(context, "일반타이어 미가입 차량입니다.\n 타이어 신청이 불가합니다.", null);
                    eventPopup.show();
                    return;
                }

                if (pm081_arr == null || pm081_arr.size() <= 0)
                    return;
                LoginModel model = KtRentalApplication.getLoginModel();
                final HashMap hm = new HashMap();
                // String aufnr = this.aufnr;
                // hm.put("AUFNR", aufnr); //정비접수번호
                data = tv_carnum.getText();
                str = data != null && !data.toString().equals("") ? data.toString() : " ";
                String invnr = str;
                hm.put("INVNR", invnr); // 고객차량번호
                String stirgb = rb_normal.isChecked() ? "1" : "2";
                hm.put("STIRGB", stirgb); // (일반: 1 , 스노우: 2)

                // String dcemg = cb_emergency.isChecked() ? "X" : " ";
                // hm.put("DCEMG", dcemg); // 긴급요청여부

                String prmsts = "30";
                hm.put("PRMSTS", prmsts); // 진행상태
                String newtir = cb_brandnew.isChecked() ? "X" : " ";
                hm.put("NEWTIR", newtir);// 신규타이어 여부
                String dam01 = model.getPernr();
                hm.put("DAM01", dam01); // 접수자사번
                String recnam = model.getEname();
                hm.put("RECNAM", recnam); // 접수자명

                // 2014.01.10 ypkim
                String rechp = "";
                data = tv_customer_num.getText().toString(); // "010-0123-4567";
                rechp = data != null && !data.toString().equals("") ? data.toString() : " ";
                hm.put("RECHP", rechp); // 접수자연락처

                String drvnam = "";
                data = et_receiptor.getText().toString(); // "운전자";
                drvnam = data != null && !data.toString().equals("") ? data.toString() : " ";
                hm.put("DRVNAM", drvnam); // 운전자명 입력화면 생성해야함
                data = tv_customer_num.getText();
                str = data != null && !data.toString().equals("") ? data.toString() : " ";
                String custel = str;
                hm.put("CUSTEL", custel); // 운전자연락처
                data = tv_address.getText();
                str = data != null && !data.toString().equals("") ? data.toString() : " ";
                String cusdst = str;
                hm.put("CUSDST", cusdst); // 소재지주소
                data = tv_mileage.getText();
                str = data != null && !data.toString().equals("") ? data.toString() : " ";
                String incml = str.replace(" Km", "");
                hm.put("INCML", incml); // 주행거리
                // String qtysum =
                // pm081_arr==null||pm081_arr.size()<=0?"0":pm081_arr.size()+"";
                // hm.put("QTYSUM", qtysum); //타이어수량합계
                String shipgb = bt_pm082.getTag().toString();
                kog.e("KDH", "shipgb = " + shipgb);

                hm.put("SHIPGB", TIRE_TYPE); // 배송구분

                data = bt_releasedate.getText();
                str = data != null && !data.toString().equals("") ? data.toString().replace(".", "") : " ";
                String requdt = str;
                hm.put("REQUDT", requdt); // 출고요청일
                String lifnr = provider_lifnr;
                hm.put("LIFNR", lifnr); // 공급업체번호
                String lifnr2 = acceptor_lifnr;
                hm.put("LIFNR2", lifnr2); // 인수업체번호
                data = tv_receipter_phone.getText();
                str = (data != null && !data.toString().equals("")) ? data.toString() : " ";
                hm.put("LIF2TL1", str); // 인수자연락처1 접수일시를(바꿔야함)
                String lif2tl2 = " ";
                // hm.put("LIF2TL2", lif2tl2); //인수자연락처2
                String lif2zip = acceptor_pstlz;
                hm.put("LIF2ZIP", lif2zip); // 인수자우편번호
                String lif2dst = acceptor_stras;
                hm.put("LIF2DST", lif2dst); // 인수자주소
                String rqprn = model.getPernr();
                hm.put("RQPRN", rqprn); // 신청자(접수자사번과 같음)
                String deptcd = model.getDeptcd();
                hm.put("DEPTCD", model.getDeptcd()); // 부서(HR) mot코드

                data = et_customer_request.getText();
                final String i_trcusr = data != null && !data.toString().equals("") ? data.toString() : " ";
                data = et_acceptor_issue.getText();
                final String i_trspec = data != null && !data.toString().equals("") ? data.toString() : " ";

                // Log.i("###", "####AUFNR  " + aufnr);
                // Log.i("###", "####INVNR  " + invnr);
                // Log.i("###", "####STIRGB " + stirgb);
                // Log.i("###", "####DCEMG  " + dcemg);
                // Log.i("###", "####PRMSTS " + prmsts);
                // Log.i("###", "####NEWTIR " + newtir);
                // Log.i("###", "####DAM01  " + dam01);
                // Log.i("###", "####RECNAM " + recnam);
                // Log.i("###", "####RECHP  " + rechp);
                // Log.i("###", "####DRVNAM " + drvnam);
                // Log.i("###", "####CUSTEL " + custel);
                // Log.i("###", "####CUSDST " + cusdst);
                // Log.i("###", "####INCML  " + incml);
                // // Log.i("###","####QTYSUM "+qtysum );
                // Log.i("###", "####SHIPGB " + shipgb);
                // Log.i("###", "####REQUDT " + requdt);
                // Log.i("###", "####LIFNR  " + lifnr);
                // Log.i("###", "####LIFNR2 " + lifnr2);
                // // Log.i("###","####LIF2TL1"+lif2tl1);
                // Log.i("###", "####LIF2TL2" + lif2tl2);
                // Log.i("###", "####LIF2ZIP" + lif2zip);
                // Log.i("###", "####LIF2DST" + lif2dst);
                // Log.i("###", "####RQPRN  " + rqprn);
                // Log.i("###", "####DEPTCD " + deptcd);

                // 사진전송
                int check_count = 0;
                for (int i = 0; i < pm081_arr.size(); i++)
                {
                    if (!pm081_arr.get(i).PATH.equals(""))
                        check_count++;
                }
                final String[] path = new String[check_count];
                final String[] name = new String[check_count];
                int count = 0;
                for (int i = 0; i < pm081_arr.size(); i++)
                {
                    if (!pm081_arr.get(i).PATH.equals(""))
                    {
                        int num = pm081_arr.get(0).PATH.lastIndexOf("/") + 1;
                        String aa = pm081_arr.get(0).PATH.substring(num);
                        kog.e("Jonathan", "aa :: " + aa);

                        path[count] = pm081_arr.get(i).PATH;
                        name[count] = aa;
                        count++;
                    }
                }
                HM = hm;

                TireUpload_Async asynctask = new TireUpload_Async(context)
                {
                    @Override
                    protected void onProgressUpdate(ArrayList<String>... values)
                    {
                        ArrayList<String> list = values[0];
                        for (int i = 0; i < list.size(); i++)
                        {
                            kog.e("KDH", "#### 사진코드넘버들" + list.get(i));
                        }

                        LISTa = list;
                        showProgress("타이어 신청 중 입니다.");
                        cc.setZMO_1060_WR01(getTableZMO_1060_WR01(path, list), i_trcusr, i_trspec);
                    }
                };
                asynctask.execute(path);
                break;

            case R.id.tire_change_history_id:

                if (o_struct1 == null || o_struct1.size() <= 0)
                {
                    EventPopupC epc = new EventPopupC(context);
                    epc.show("번호를 조회하세요");
                    return;
                }

                History_Dialog hd = new History_Dialog(context, o_struct1, 1);
                hd.show();

                // Test_Dialog td = new Test_Dialog(context);
                // td.show();

                break;
            // case R.id.tire_provide_company_phone_id: //공급업체 연락처
            // inventoryPopup.setOnDismissListener(new
            // InventoryPopup.OnDismissListener()
            // {
            // @Override
            // public void onDismiss(String result, int position)
            // {
            // tv_providor_phone.setText(result);
            // }
            // });
            // inventoryPopup.show(tv_providor_phone);
            // break;
            case R.id.tire_receiptor_phone_id: // 인수자 연락처
                Toast.makeText(context, "인수자 연락처", Toast.LENGTH_SHORT).show();
                inventoryPopup = new InventoryPopup(context, QuickAction.HORIZONTAL, R.layout.inventory_popup, InventoryPopup.TYPE_PHONE_NUMBER);
                inventoryPopup.setOnDismissListener(new InventoryPopup.OnDismissListener()
                {
                    @Override
                    public void onDismiss(String result, int position)
                    {
                        String num = PhoneNumberUtils.formatNumber(result);
                        tv_receipter_phone.setText(num);
                    }
                });
                inventoryPopup.show(tv_receipter_phone);
                break;

            case R.id.tire_acceptor_zip_search_id:

                data = bt_acceptor_address.getText();
                if (data != null && !data.toString().equals(""))
                {
                    ViewGroup vg = pwDelSearch.getViewGroup();
                    LinearLayout back = (LinearLayout) vg.getChildAt(0);
                    final Button del = (Button) back.getChildAt(0);
                    del.setOnClickListener(new OnClickListener()
                    {
                        @Override
                        public void onClick(View v)
                        {
                            bt_acceptor_address.setText("");
                            tv_acceptor_address.setText("");
                            pwDelSearch.dismiss();
                        }
                    });
                    final Button sel = (Button) back.getChildAt(1);
                    sel.setOnClickListener(new OnClickListener()
                    {
                        @Override
                        public void onClick(View v)
                        {
                            showAddressDialog();
                            pwDelSearch.dismiss();
                        }
                    });
                    pwDelSearch.show(bt_acceptor_address);
                }
                else
                {
                    showAddressDialog();
                }
                break;
            case R.id.tire_customer_num_id:
                inventoryPopup = new InventoryPopup(context, QuickAction.HORIZONTAL, R.layout.inventory_popup, InventoryPopup.TYPE_PHONE_NUMBER);
                inventoryPopup.setOnDismissListener(new InventoryPopup.OnDismissListener()
                {
                    @Override
                    public void onDismiss(String result, int position)
                    {
                        if (result.equals(""))
                            return;
                        // int int_mile = Integer.parseInt(result);
                        // NumberFormat nf = NumberFormat.getInstance();
                        // String mile = nf.format(int_mile);
                        tv_customer_num.setText(result);
                    }
                });
                inventoryPopup.show(tv_customer_num);
                break;
            case R.id.tire_mileage_id:
                inventoryPopup = new InventoryPopup(context, QuickAction.HORIZONTAL, R.layout.inventory_popup, InventoryPopup.TYPE_MONEY);
                inventoryPopup.setOnDismissListener(new InventoryPopup.OnDismissListener()
                {
                    @Override
                    public void onDismiss(String result, int position)
                    {
                        if (result.equals(""))
                            return;
                        // int int_mile = Integer.parseInt(result);
                        // NumberFormat nf = NumberFormat.getInstance();
                        // String mile = nf.format(int_mile);
                        tv_mileage.setText(result + " Km");
                    }
                });
                inventoryPopup.show(tv_mileage);
                break;
            case R.id.tire_search_id:
                // Toast.makeText(context, "조회버튼", 0).show();
                data = tv_carnum.getText();
                str = data != null && !data.toString().equals("") ? data.toString() : "";
                if (!str.equals(""))
                {
                    CARNUM = str;
                    showProgress("상세조회 중 입니다.");
                    cc.getZMO_1060_RD03(CARNUM, EQUNR);
                }
                else
                {
                    EventPopupC epc = new EventPopupC(context);
                    epc.show("차량번호를 입력해 주세요");
                }

                break;
            case R.id.tire_carnum_id:
                // Toast.makeText(context, "차량번호검색 버튼", 0).show();
                // myung 20131202 UPDATE 차량번호 입력 필드 터치 시 지우기 | 조회하기 창 팝업
                String data1 = tv_carnum.getText().toString();
                if (data1 != null && !data1.toString().equals(""))
                {
                    ViewGroup vg1 = pwDelSearch.getViewGroup();
                    LinearLayout back = (LinearLayout) vg1.getChildAt(0);
                    final Button del = (Button) back.getChildAt(0);
                    del.setOnClickListener(new OnClickListener()
                    {
                        @Override
                        public void onClick(View v)
                        {
                            tv_carnum.setText("");
                            tv_carnum.setTag("");
                            // myung 20131223 ADD 설비번호 EQUNR 삭제
                            EQUNR = "";
                            pwDelSearch.dismiss();
                        }
                    });
                    final Button sel = (Button) back.getChildAt(1);
                    sel.setOnClickListener(new OnClickListener()
                    {
                        @Override
                        public void onClick(View v)
                        {
                            new Camera_Dialog(context).show(tv_carnum);
                            pwDelSearch.dismiss();
                        }
                    });
                    pwDelSearch.show(tv_carnum);
                }
                else
                {
                    final Camera_Dialog cd = new Camera_Dialog(context);
                    Button cd_done = (Button) cd.findViewById(R.id.camera_done_id);
                    final EditText et_carnum = (EditText) cd.findViewById(R.id.camera_carnum_id);
                    cd_done.setOnClickListener(new OnClickListener()
                    {
                        @Override
                        public void onClick(View arg0)
                        {
                            Object data = et_carnum.getText();
                            String str = data == null || data.toString().equals("") ? "" : data.toString();
                            if (str.equals(""))
                            {
                                EventPopupC epc = new EventPopupC(context);
                                epc.show("차량번호를 입력해 주세요.");
                            }
                            else
                            {
                                tv_carnum.setText(str);
                                // myung 20131223 ADD 설비번호 EQUNR 삭제
                                EQUNR = "";
                                cd.dismiss();
                            }
                        }
                    });
                    cd.show();

                }

                // final Camera_Dialog cd = new Camera_Dialog(context);
                // Button cd_done = (Button) cd.findViewById(R.id.camera_done_id);
                // final EditText et_carnum = (EditText) cd
                // .findViewById(R.id.camera_carnum_id);
                // cd_done.setOnClickListener(new OnClickListener() {
                // @Override
                // public void onClick(View arg0) {
                // Object data = et_carnum.getText();
                // String str = data == null || data.toString().equals("") ? ""
                // : data.toString();
                // if (str.equals("")) {
                // EventPopupC epc = new EventPopupC(context);
                // epc.show("차량번호를 입력해 주세요.");
                // } else {
                // tv_carnum.setText(str);
                // cd.dismiss();
                // }
                // }
                // });
                // cd.show();

                // Product_Camera_Dialog pcd = new Product_Camera_Dialog(context);
                // pcd.show();
                // final CameraPopupFragment2 cpf2 = new CameraPopupFragment2
                // (CameraPopupFragment2.class.getName(), null, "", "차량번호 지정");
                // cpf2.setOnAttachedFragment(new OnStartFragmentListner()
                // {
                // @Override
                // public void onAttachedFragment(String flagmentName)
                // {
                // ViewGroup vg = cpf2.getRootView();
                // Button cpf2_done = (Button)vg.findViewById(R.id.btn_complate);
                // final EditText cpf2_num =
                // (EditText)vg.findViewById(R.id.et_car_num);
                // cpf2_done.setOnClickListener(new OnClickListener()
                // {
                // @Override
                // public void onClick(View v)
                // {
                // Object data = cpf2_num.getText();
                // String num =
                // data!=null&&!data.toString().equals("")?data.toString():"";
                // Toast.makeText(context, num, 0).show();
                // cpf2.dismiss();
                // }
                // });
                // }
                // });
                // cpf2.show(getChildFragmentManager(), "카운트" , 1044, 544, "a");
                // Intent in = new Intent(context,
                // CarNumberPlateSearchPhoneActivity.class);
                // startActivity(in);

                break;
            
        }
    }

    private boolean checkField()
    {
        Object data;
        Object data2;
        
        data = et_receiptor.getText();
        if (data == null || data.toString().equals(""))
        {
            EventPopupC epc = new EventPopupC(context);
            epc.show("운전자명은 필수 입력사항입니다.\n운전자명을 입력해 주세요.");
            return false;
        }
        
        data = tv_customer_num.getText();
        if (data == null || data.toString().equals(""))
        {
            EventPopupC epc = new EventPopupC(context);
            epc.show("운전자연락처는 필수 입력사항입니다.\n운전자연락처를 입력해 주세요.");
            return false;
        }
        
        data = tv_mileage.getText();
        if (data == null || data.toString().equals(""))
        {
            EventPopupC epc = new EventPopupC(context);
            epc.show("현재주행거리는 필수 입력사항입니다.\n현재주행거리를 입력해 주세요.");
            return false;
        }

        data = et_customer_request.getText();
        if (data == null || data.toString().equals(""))
        {
            EventPopupC epc = new EventPopupC(context);
            epc.show("고객요청사항은 필수 입력사항입니다.\n고객요청사항을 입력해 주세요.");
            return false;
        }
        data = bt_releasedate.getText();
        if (data == null || data.toString().equals(""))
        {
            EventPopupC epc = new EventPopupC(context);
            epc.show("출고요청일은 필수 입력사항입니다.\n출고요청일을 입력해 주세요.");
            return false;
        }
        data = bt_provide_company.getText();
        if (data == null || data.toString().equals(""))
        {
            EventPopupC epc = new EventPopupC(context);
            epc.show("공급자업체는 필수 입력사항입니다.\n공급자업체를 입력해 주세요.");
            return false;
        }
        // data = tv_providor_phone.getText();
        // if(data==null||data.toString().equals(""))
        // {
        // Toast.makeText(context, "공급자연락처는 필수 입력사항입니다.\n공급자연락처를 입력해 주세요.",
        // 0).show();
        // return false;
        // }
        data = bt_acceptor.getText();
        if (data == null || data.toString().equals(""))
        {
            EventPopupC epc = new EventPopupC(context);
            epc.show("인수자는 필수 입력사항입니다.\n인수자를 입력해 주세요.");
            return false;
        }
        data = tv_receipter_phone.getText();
        if (data == null || data.toString().equals(""))
        {
            EventPopupC epc = new EventPopupC(context);
            epc.show("인수자연락처는 필수 입력사항입니다.\n인수자연락처를 입력해 주세요.");
            return false;
        }
        data = tv_acceptor_address.getText();
        if (data == null || data.toString().equals(""))
        {
            EventPopupC epc = new EventPopupC(context);
            epc.show("인수자주소는 필수 입력사항입니다.\n인수자주소를 입력해 주세요.");
            return false;
        }
        data = bt_tire_front.getText();
        data2 = bt_tire_rear.getText();
        if ((data == null || data.toString().equals("")) && (data2 == null || data2.toString().equals("")))
        {
            EventPopupC epc = new EventPopupC(context);
            epc.show("타이어는 필수 입력사항입니다.\n타이어 앞 또는 뒤를 입력해 주세요.");
            return false;
        }

        if (lv_list == null || lv_list.getChildCount() <= 0)
        {
            EventPopupC epc = new EventPopupC(context);
            epc.show("타이어항목은 필수 입력사항입니다.\n타이어항목을 선택해 주세요.");
            return false;
        }

        return true;
    }

    HashMap<String, String> HM;
    ArrayList<String>       LISTa;

    private ArrayList<HashMap<String, String>> getTableZMO_1060_WR01(String[] path, ArrayList<String> pic_num)
    {
        ArrayList<HashMap<String, String>> i_itab1 = new ArrayList<HashMap<String, String>>();
        try
        {
            for (int i = 0; i < pic_num.size(); i++) // 리스트 갯수만큼
            {
                HashMap<String, String> hm = new HashMap<String, String>();
                hm.put("ATTATCHNO", " "); // 첨부번호j(공백)
                hm.put("FILENO", pic_num.get(i)); // 파일번호(서버에서 나온번호)
                hm.put("FILESEQ", "1"); // 파일일련번호(파일순서)
                // 2013.12.02 add
                hm.put("FILEPH", "/" + pic_num.get(i).substring(0, 8) + "/" + pic_num.get(i).substring(8)); // 파일번호(서버에서 나온번호)

                // Log.i("#", "###패스" + path[i]);
                int num = path[i].lastIndexOf("/") + 1;
                String name = path[i].substring(num);	
                hm.put("FILEPNM", name); // 파일명(사진파일명.jpg)
                hm.put("FILELNM", name); // 파일명(사진파일명.jpg)
                File file = new File(path[i]);
                hm.put("FILESZ", "" + file.length()); // 파일사이즈(byte)
                hm.put("IDFLAG", "I"); // 저장삭제여부(Insert : I, Update : U, Delete
                // : D)
                i_itab1.add(hm);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return i_itab1;
    }

    private ArrayList<HashMap<String, String>> getTableZMO_1030_WR06_1(HashMap<String, String> _hm, ArrayList<PM081> pm081_arr,
            ArrayList<String> pic_code, String attaNo)
    {
        ArrayList<HashMap<String, String>> i_itab1 = new ArrayList<HashMap<String, String>>();
        try
        {
            int count = 0;
            for (int i = 0; i < pm081_arr.size(); i++) // 리스트 갯수만큼
            {
                HashMap<String, String> hm = new HashMap<String, String>();
                if (pm081_arr.get(i).PATH.equals(""))
                    continue;
                hm.put("AUFNR", _hm.get("AUFNR")); // 정비접수번호 조회시 나오는번호 동일
                hm.put("SEQNO", (i + 1) + ""); // 일련번호 시퀀스 생성해서 증가
                if(Integer.valueOf(pm081_arr.get(i).ZCODEV) > 100 && Integer.valueOf(pm081_arr.get(i).ZCODEV) < 105)
                {
                	hm.put("GEDOCN", "100"); // 일반 첨부문서 문서유형 pm081
                }
                else
                {
                	hm.put("GEDOCN", pm081_arr.get(i).ZCODEV); // 일반 첨부문서 문서유형 pm081
                }
                // 넘버
                hm.put("ATTATCHNO", attaNo); // 첨부번호
                hm.put("FILENO", pic_code.get(count++)); // 파일번호
                hm.put("FILESEQ", "1"); // 파일일련번호
                i_itab1.add(hm);
            }
        }
        catch (Exception e)
        {

            e.printStackTrace();
        }
        return i_itab1;
    }

    // 30, 타이어 앞좌 FROLFT
    // 40, 타이어 앞우 FRORHT
    // 50, 뒤1좌 BAKLFT1
    // 60, 뒤1우 BAKRHT1
    // 70, 뒤2좌 BAKLFT2
    // 80, 뒤2우 BAKRHT2
    // 90, 스페어 SPARE

    String CheckZCODEV[] = {
            "30", "40", "50", "60", "70", "80", "90", };
    String CheckCOL[]    = {
            "FROLFT", "FRORHT", "BAKLFT1", "BAKRHT1", "BAKLFT2", "BAKRHT2", "SPARE", };

    private ArrayList<HashMap<String, String>> getTableZMO_1030_WR06_2(HashMap<String, String> _hm, ArrayList<PM081> pm081_arr, String front,
            String rear)
    {
        ArrayList<HashMap<String, String>> i_itab2 = new ArrayList<HashMap<String, String>>();
        for (int i = 0; i < 1; i++)
        {
            HashMap<String, String> hm = new HashMap<String, String>();
            hm.put("AUFNR", _hm.get("AUFNR")); // 정비접수번호 조회시 나오는번호 동일
            hm.put("MATNR", front); // 타이어코드 (앞) 체크표시 X 체크안된것 " "공백
            hm.put("MATNR2", rear); // 타이어코드(뒤)체크표시 X 체크안된것 " "공백

            for (int j = 0; j < Tire_Picture_Dialog.pm081.size(); j++)
            {
                for (int k = 0; k < CheckZCODEV.length; k++)
                {
                    if (CheckZCODEV[k].equals(Tire_Picture_Dialog.pm081.get(j).ZCODEV) && !TextUtils.isEmpty(Tire_Picture_Dialog.pm081.get(j).PATH))
                    {
                        if (Tire_Picture_Dialog.pm081.get(j).CHECKED)
                        {
                            hm.put(CheckCOL[k], "X");
                        }
                        else
                        {
                            hm.put(CheckCOL[k], " ");
                        }
                    }
                }
            }

            /*
             * //2014-04-22 KDH 불안하다..나도 하드코딩으로함 if (!front.equals(" ")) { hm.put("FROLFT", Tire_Picture_Dialog.pm081.get(3).CHECKED ? "X" : " "); //
             * 앞(좌)체크표시 X 체크안된것 " "공백 hm.put("FRORHT", Tire_Picture_Dialog.pm081.get(4).CHECKED ? "X" : " "); // 앞(우)체크표시 X 체크안된것 " "공백
             * hm.put("FROSKL", Tire_Picture_Dialog.pm081.get(3).ONESIDE_WEAR ? "X" : " "); // 앞(좌) 편마모 X 체크안된것 " "공백 hm.put("FROSKR",
             * Tire_Picture_Dialog.pm081.get(4).ONESIDE_WEAR ? "X" : " "); // 앞(우) 편마모 X 체크안된것 " "공백 } if (!rear.equals(" ")) { hm.put("BAKLFT1",
             * Tire_Picture_Dialog.pm081.get(5).CHECKED ? "X" : " "); // 뒤1(좌) // X // 체크안된것 // " "공백 hm.put("BAKRHT1",
             * Tire_Picture_Dialog.pm081.get(6).CHECKED ? "X" : " "); // 뒤1(우) // X // 체크안된것 // " "공백 hm.put("BAKLFT2",
             * Tire_Picture_Dialog.pm081.get(7).CHECKED ? "X" : " "); // 뒤2(좌) // X // 체크안된것 // " "공백 hm.put("BAKRHT2",
             * Tire_Picture_Dialog.pm081.get(8).CHECKED ? "X" : " "); // 뒤2(우) // X // 체크안된것 // " "공백 hm.put("BAKSKL",
             * Tire_Picture_Dialog.pm081.get(5).ONESIDE_WEAR ? "X" : " "); // 뒤(좌) 편마모 X 체크안된것 " "공백 hm.put("BAKSKR",
             * Tire_Picture_Dialog.pm081.get(6).ONESIDE_WEAR ? "X" : " "); // 뒤(우) 편마모 X 체크안된것 " "공백 } hm.put("SPARE",
             * Tire_Picture_Dialog.pm081.get(9).CHECKED ? "X" : " "); // 스페어 X 체크안된것 " "공백
             */

            // hm.put("CHGQTY", pm081_arr.size()+""); //교체수량 리스트 카운트전체()
            i_itab2.add(hm);
        }
        return i_itab2;
    }

    private HashMap<String, String> getStructureZMO_1030_WR06(HashMap<String, String> hm)
    {
        HashMap<String, String> stuct_map = new HashMap<String, String>();
        stuct_map.put("AUFNR", hm.get("AUFNR")); // 정비접수번호 조회시
        stuct_map.put("INVNR", hm.get("INVNR")); // 고객차량번호 조회시 (체크)
        // 2014.01.10 ypkim add
        stuct_map.put("EQUNR", this.EQUNR); // 고객차량번호 조회시 (체크)

        stuct_map.put("STIRGB", hm.get("STIRGB")); // 타이어 구분(일반: 1 , 스노우: 2)
        // 화면입력값
        stuct_map.put("DCEMG", hm.get("DCEMG")); // 긴급요청 여부(긴급 : X) 체크표시확인
        stuct_map.put("PRMSTS", hm.get("PRMSTS")); // 진행상태 조회시나옴
        stuct_map.put("NEWTIR", hm.get("NEWTIR")); // 신규타이어여부 체크표시확인
        stuct_map.put("DAM01", hm.get("DAM01")); // 접수자 사번 로그인사번70064
        stuct_map.put("RECNAM", hm.get("RECNAM")); // 접수자명 로그인사용자 이름
        stuct_map.put("RECHP", hm.get("RECHP")); // 접수자연락처 로그인 사용자 번호
        stuct_map.put("DRVNAM", hm.get("DRVNAM")); // 운전자명 조회시나옴(체크)
        stuct_map.put("CUSTEL", hm.get("CUSTEL")); // 운전자 연락처 고객연락처 입력
        stuct_map.put("CUSDST", hm.get("CUSDST")); // 소재지 주소 화면입력값(조회시 나옴)
        stuct_map.put("INCML", hm.get("INCML")); // 주행거리 화면입력값(현재주행거리)
        stuct_map.put("QTYSUM", hm.get("QTYSUM")); // 타이어 수량 합계 타이어신청내역
        // 리스트(전체카운트 갯수)
        stuct_map.put("SHIPGB", hm.get("SHIPGB")); // 배송구분 화면입력값
        stuct_map.put("REQUDT", hm.get("REQUDT")); // 출고요청일 화면입력값
        stuct_map.put("LIFNR", hm.get("LIFNR")); // 공급업체 번호 입력값
        stuct_map.put("LIFNR2", hm.get("LIFNR2")); // 인수업체 번호 입력값
        stuct_map.put("LIF2TL1", hm.get("LIF2TL1")); // 인수자 연락처1 입력값(접수일시)변
        stuct_map.put("LIF2TL2", hm.get("LIF2TL2")); // 인수자 연락처2 공백" "
        stuct_map.put("LIF2ZIP", hm.get("LIF2ZIP")); // 인수자 우편번호 화면입력값
        stuct_map.put("LIF2DST", hm.get("LIF2DST")); // 인수자 주소 화면입력값
        stuct_map.put("RQPRN", hm.get("RQPRN")); // 신청자 로그인사용자 번호70064
        stuct_map.put("DEPTCD", hm.get("DEPTCD")); // 부서(HR) MOT 코드

        return stuct_map;
    }

    private void setTireFront()
    {
        HashMap<String, String> map = tsd.array_hash.get(tsd.SELECTED);
        bt_tire_front.setText(map.get("MAKTX"));
        bt_tire_front.setTag(map.get("MATNR"));
        if (tifszcd == null)
            return;
        if (!tifszcd.equals(map.get("MATNR")))
        {
            bt_tire_front.setTextColor(Color.RED);
        }
        else
        {
            bt_tire_front.setTextColor(Color.BLACK);
        }

        if (cb_same.isChecked())
        {
            bt_tire_rear.setText(map.get("MAKTX"));
            bt_tire_rear.setTag(map.get("MATNR"));

            if (!tibszcd.equals(map.get("MATNR")))
            {
                bt_tire_rear.setTextColor(Color.RED);
            }
            else
            {
                bt_tire_rear.setTextColor(Color.BLACK);
            }
        }
        // myung 20131216 ADD 타이어 검색 후 앞뒤 동일 체크 시에도 동일하게 적용
        else
        {
            if (tibszcd.equals(map.get("MATNR")))
                cb_same.setChecked(true);
        }
    }

    private void setTireRear()
    {
        HashMap<String, String> map = tsd.array_hash.get(tsd.SELECTED);
        bt_tire_rear.setText(map.get("MAKTX"));
        bt_tire_rear.setTag(map.get("MATNR"));
        if (tibszcd == null)
            return;
        if (!tibszcd.equals(map.get("MATNR")))
        {
            bt_tire_rear.setTextColor(Color.RED);
        }
        else
        {
            bt_tire_rear.setTextColor(Color.BLACK);
        }

        if (cb_same.isChecked())
        {
            bt_tire_front.setText(map.get("MAKTX"));
            bt_tire_front.setTag(map.get("MATNR"));
            if (!tifszcd.equals(map.get("MATNR")))
            {
                bt_tire_front.setTextColor(Color.RED);
            }
            else
            {
                bt_tire_front.setTextColor(Color.BLACK);
            }
        }
        // myung 20131216 ADD 타이어 검색 후 앞뒤 동일 체크 시에도 동일하게 적용
        else
        {
            if (tifszcd.equals(map.get("MATNR")))
                cb_same.setChecked(true);
        }
    }

    String provider_lifnr;
    String acceptor_lifnr;
    String acceptor_tele1;
    String acceptor_pstlz;
    String acceptor_stras;

    private void setProvideCompany()
    {
        HashMap<String, String> map = tpcd.array_hash.get(tpcd.SELECTED);

        // bt_provide_company.setText(map.get("NAME1"));
        // tv_receipter_phone.setText(map.get("TELE1"));
        // tv_acceptor_address.setText(map.get("PSTLZ")+" "+map.get("STRAS"));
        // provider_lifnr = map.get("LIFNR");

        // 150715
        bt_provide_company.setText(map.get("NAME1"));
        tv_providor_address.setText(map.get("PSTLZ") + " " + map.get("STRAS"));
        provider_lifnr = map.get("LIFNR");

        Set<String> set = map.keySet();
        Iterator<String> it = set.iterator();
        String key;

        while (it.hasNext())
        {
            key = it.next();
            kog.e("Jonathan", "공급업체 key ===  " + key + "    value  === " + map.get(key));
        }

        kog.e("KDH", "tpcd.SELECTED = " + tpcd.SELECTED);
        kog.e("KDH", "provider_lifnr = " + provider_lifnr);
    }

    private void setAcceptor()
    {
        HashMap<String, String> map = tpcd.array_hash.get(tpcd.SELECTED);
        bt_acceptor.setText(map.get("NAME1"));
        bt_acceptor_address.setText(map.get("PSTLZ"));
        tv_acceptor_address.setText(map.get("STRAS"));
        tv_receipter_phone.setText(map.get("TELF1"));

        acceptor_lifnr = map.get("LIFNR");
        acceptor_tele1 = map.get("TELE1");
        acceptor_pstlz = map.get("PSTLZ");
        acceptor_stras = map.get("STRAS");

    }

    private void setAcceptor_from_provider()
    {
        HashMap<String, String> map = tpcd.array_hash.get(tpcd.SELECTED);
        // bt_acceptor.setText(map.get("NAME1"));
        // bt_acceptor_address.setText(map.get("PSTLZ"));
        // tv_acceptor_address.setText(map.get("STRAS"));
        // tv_receipter_phone.setText(map.get("TELF1"));

        acceptor_lifnr = map.get("LIFNR");
        acceptor_tele1 = map.get("TELE1");
        acceptor_pstlz = map.get("PSTLZ");
        acceptor_stras = map.get("STRAS");

    }

    HashMap<String, String> o_struct1;
    HashMap<String, String> is_return;

    @Override
    public void connectResponse(String FuntionName, String resultText, String MTYPE, int resulCode, TableModel tableModel)
    {
         kog.e("#", "####" + FuntionName + "/" + resultText + "/" + MTYPE + "/");
        // + resulCode);
        hideProgress();
        
        
        if (MTYPE == null || !MTYPE.equals("S"))
        {
        	
        	cc.duplicateLogin(mContext);
        	
            EventPopupC epc = new EventPopupC(context);
            epc.show(resultText);
            return;
        }

        if (FuntionName.equals("ZMO_1060_RD03"))
        {
            ArrayList<HashMap<String, String>> et_tiresin_arr = tableModel.getTableArray("O_ITAB1");
            ArrayList<HashMap<String, String>> et_tiretab_arr = tableModel.getTableArray("O_ITAB2");
            o_struct1 = tableModel.getStruct("O_STRUCT1");
            is_return = tableModel.getStruct("IS_RETURN");
            
            kog.e("Joanthan", "Joanthan is_return :: " + is_return.get("MTYPE"));
            

            setO_ITAB1(et_tiresin_arr);
            setO_ITAB2(et_tiretab_arr);
            setO_STRUCT1(o_struct1);
            // myung 20131223 ADD SANPM/CHGTYN/CHGJYN 조건에 따라 타이어신청 버튼 비활성
            setTireRequestButton(et_tiretab_arr.get(0), tableModel.getStruct("O_STRUCT3"));
            
            
            if("I".equals(is_return.get("MTYPE")))
            {
                EventPopupC epc = new EventPopupC(context);
                epc.show(is_return.get("MESSAGE"));
                return;
            }
            
            
            cc.duplicateLogin(mContext);
            

        }

        else if (FuntionName.equals("ZMO_1030_WR06"))
        {
            Toast.makeText(context, resultText, Toast.LENGTH_SHORT).show();
            // myung 20131126 ADD 성공일 경우 화면 초기화
            // TireFragment fragment = new TireFragment(TireFragment.class.getName(),
            // mChangeFragmentListener, CARNUM, EQUNR);
            //
            // changfragment(fragment);

            // myung 20131128 ADD 전송 후 초기화
            initData();
            setPM082Click();
            setO_STRUCT1(o_struct1);
            
            cc.duplicateLogin(mContext);
            
            // showProgress("상세조회 중 입니다.");
            // cc.getZMO_1060_RD03(CARNUM, EQUNR);
        }

        else if (FuntionName.equals("ZMO_1060_WR01"))
        {
            // Toast.makeText(context, tableModel.getResponse("O_ATTATCHNO")+"",
            // 0).show();
            String attatNo = tableModel.getResponse("O_ATTATCHNO");
            kog.e("Jonathan", "attatNo tire :: " + attatNo);

            // 타이어신청
            Object data = bt_tire_front.getTag();
            String front = data != null && !data.toString().equals("") ? data.toString() : " ";

            data = bt_tire_rear.getTag();
            String rear = data != null && !data.toString().equals("") ? data.toString() : " ";
            HashMap<String, String> temp_hm = getStructureZMO_1030_WR06(HM);
            ArrayList<HashMap<String, String>> temp_hm2 = getTableZMO_1030_WR06_1(HM, pm081_arr, LISTa, attatNo);
            if (temp_hm2.size() <= 0)
            {
                EventPopupC epc = new EventPopupC(context);
                epc.show("사진이 전송되지 않았습니다. \n네트워크 상태를 확인하세요.");
                return;
            }
            ArrayList<HashMap<String, String>> temp_hm3 = getTableZMO_1030_WR06_2(HM, pm081_arr, front, rear);

            data = et_customer_request.getText();
            final String i_trcusr = data != null && !data.toString().equals("") ? data.toString() : " ";
            data = et_acceptor_issue.getText();
            final String i_trspec = data != null && !data.toString().equals("") ? data.toString() : " ";

            showProgress();
            cc.setZMO_1030_WR06(temp_hm, temp_hm2, temp_hm3, i_trcusr, i_trspec);
        }

    }

    // myung 20131223 ADD SANPM/CHGTYN/CHGJYN 조건에 따라 타이어신청 버튼 비활성
    private void setTireRequestButton(HashMap<String, String> itab2_map, HashMap<String, String> struct3_map)
    {
    	
    	
    	String CPMAT_TX = struct3_map.get("CPMAT_TX"); //유의업종
    	tire_similar_id.setText(CPMAT_TX);

    	

        String strTemp = struct3_map.get("SANPM");
        if (strTemp.equals("정비불포화"))
        {
            // myung 20140102 DELETE 타이어신청 비활성화 해제
            // bt_request.setEnabled(false);
            EventPopupC epc = new EventPopupC(context);
            epc.show("정비불포함 차량입니다.\n타이어교체접수 불가합니다. \nMOT에 확인하시기 바랍니다.");
            return;
        }

        strTemp = itab2_map.get("CHGTYN");
        if (strTemp.equals("N"))
        {
            // myung 20140102 DELETE 타이어신청 비활성화 해제
            // bt_request.setEnabled(false);
            EventPopupC epc = new EventPopupC(context);
            epc.show("교체불가능한 차량번호입니다.\nMOT에 확인하시기 바랍니다.");
            return;
        }

//        strTemp = itab2_map.get("CHGJYN");
//        if (strTemp.equals("Y"))
//        {
//            // myung 20140102 DELETE 타이어신청 비활성화 해제
//            // bt_request.setEnabled(false);
//            EventPopupC epc = new EventPopupC(context);
//            epc.show("교체진행중인 건입니다.\nMOT에 확인하시기 바랍니다.");
//            return;
//        }

    }

    // myung 20131128 ADD 전송 후 초기화
    private void initData()
    {
        tv_mileage.setText("");
        et_customer_request.setText("");
        bt_releasedate.setText("");
        tv_providor_address.setText("");
        bt_provide_company.setText("");
        bt_acceptor.setText("");
        tv_receipter_phone.setText("");
        bt_acceptor_address.setText("");
        tv_acceptor_address.setText("");
        et_acceptor_issue.setText("");
        cb_same.setChecked(false);
        bt_tire_front.setText("");
        bt_tire_front.setTag("");
        bt_tire_rear.setText("");
        bt_tire_rear.setTag("");
        tv_bon.setText("본");
        tv_cumlqty_cumtqty.setText("");
        tv_tchgdt_chgrun.setText("");

        pm081_arr = new ArrayList<PM081>();
        ta = new Tire_Adapter(context, R.layout.tire_row, pm081_arr);
        lv_list.setAdapter(ta);
        ta.notifyDataSetChanged();
        
        for (int i = 0; i < pm081_arr.size(); i++) 
		{
			kog.e("KDH", "pm081_arr ZCODEV = "+pm081_arr.get(i).ZCODEV);
			kog.e("KDH", "pm081_arr ZCODEVT = "+pm081_arr.get(i).ZCODEVT);
		}

    }

    String snwtrq;  // 스노우타이어
    String notir_tx; // 내역
    String tifszcd; // 타이어(앞)사이즈코드
    String tifnat;  // 타이어사이즈(앞) 제조구분
    String tibszcd; // 타이어(뒤)사이즈코드
    String tibnat;  // 타이어사이즈(뒤) 제조구분
    String sparet;  // 스페어타이어
    String storag;  // 보관장소
    String storagnm; // 보관장소명
    String stirgb;  // 타이어구분
    String conqty;  // 타이어계약본수
    String cumtqty; // 누적본수
    String cumlqty; // 최종본수
    String tchgdt;  // 교체일자
    String chgrun;  // 교체주행거리
    String chgtyn;  // 교체가능여부
    String chgjyn;  // 교체진행여부
    String aufnr;   // 정비접수번호
    
    

    private void setO_ITAB1(ArrayList<HashMap<String, String>> _list)
    {
        HashMap<String, String> map = _list.get(0);
        
//        Set<String> set = map.keySet();
//        Iterator<String> it = set.iterator();
//        String key;
//        while (it.hasNext())
//        {
//            key = it.next();
//            kog.e("Jonathan", "login_function_1 key ===  " + key + "    value  === " +map.get(key));
//        }
        
        
        snwtrq = map.get("SNWTRQ"); // 스노우타이어
        notir_tx = map.get("NOTIR_TX"); // 내역
        tifszcd = map.get("TIFSZCD"); // 타이어(앞)사이즈코드
        tifnat = map.get("TIFNAT"); // 타이어사이즈(앞) 제조구분
        tibszcd = map.get("TIBSZCD"); // 타이어(뒤)사이즈코드
        tibnat = map.get("TIBNAT"); // 타이어사이즈(뒤) 제조구분
        sparet = map.get("SPARET"); // 스페어타이어
        storag = map.get("STORAG"); // 보관장소
        storagnm = map.get("STORAGNM"); // 보관장소명
        
        tv_carnum.setText(CARNUM);// 내가보낸 차번호
        tv_contractor1.setText("계약자명1");
        tv_contractor2.setText("계약자명2");
        tv_snowtire.setText(snwtrq);
        tv_normaltire.setText(notir_tx);
        

        tv_tirecode1.setText(tifszcd);
        tv_tiremade1.setText(tifnat);
        tv_tirecode2.setText(tibszcd);
        tv_tiremade2.setText(tibnat);
        tv_sparetire.setText(sparet);
        tv_place.setText(storagnm);

        Set<String> set = map.keySet();
        Iterator<String> it = set.iterator();
        String key;

        while (it.hasNext())
        {
            key = it.next();
            kog.e("Jonathan", "일반타이어 key ===  " + key + "    value  === " + map.get(key));
        }

        LoginModel model = KtRentalApplication.getLoginModel();
        // et_receiptor.setText(model.getPernr());

    }

    private void setO_ITAB2(ArrayList<HashMap<String, String>> _list)
    {
        HashMap<String, String> map = _list.get(0);
        stirgb = map.get("STIRGB"); // 타이어구분
        conqty = map.get("CONQTY"); // 타이어계약본수
        cumtqty = map.get("CUMTQTY"); // 누적본수
        cumlqty = map.get("CUMLQTY"); // 최종본수
        tchgdt = map.get("TCHGDT"); // 교체일자
        chgrun = map.get("CHGRUN"); // 교체주행거리
        chgtyn = map.get("CHGTYN"); // 교체가능여부
        chgjyn = map.get("CHGJYN"); // 교체진행여부
        aufnr = map.get("AUFNR"); // 정비접수번호

        tv_cumlqty_cumtqty.setText(cumlqty + "  /  " + cumtqty);

        // Log.i("####", "####" + tchgdt + ":");
        tv_tchgdt_chgrun.setText(getDateFormat(tchgdt) + "  /  " + chgrun + " KM");
    }
    
    
    
    public String getDateFormat(String date)
    {
        StringBuffer sb = new StringBuffer(date);
        if (date.length() == 8)
        {
            sb.insert(4, ".");
            int last = sb.length() - 2;
            sb.insert(last, ".");
        }
        return sb.toString();
    }

    private void setO_STRUCT1(HashMap hm)
    {
        et_receiptor.setText(hm.get("DLSM1").toString());
        tv_customer_num.setText(hm.get("DLST1").toString());
        // et_receipter_phone.setText(hm.get("LIF2TL1").toString());
        // et_receipter_phone.setText("");
        tv_address.setText(hm.get("CUSJUSO").toString());

        tv_contractor1.setText(hm.get("CONTNM").toString());
        tv_contractor2.setText(hm.get("VIPGBN").toString());

        Object data = hm.get("INLML");
        String str = data == null || data.toString().equals("") || data.toString().equals(" ") ? "0" : data.toString();

        int int_mile = Integer.parseInt(str);
        NumberFormat nf = NumberFormat.getInstance();
        String mile = nf.format(int_mile);
        String date = getDateFormat(hm.get("IDATE").toString());

        tv_last_milease.setText(mile + "KM / " + date);

        // 2014.01.10 ypkim
        this.EQUNR = hm.get("EQUNR").toString();
        // Log.i("####", "##########EQUNR############" + this.EQUNR + ":");
    }

    @Override
    public void reDownloadDB(String newVersion)
    {
    }

    private void showProvideCompanyDialog()
    {
        kog.e("Jonathan", "123 provider_lifnr :: " + provider_lifnr + " type :: " + TIRE_TYPE);
        // tpcd = new Tire_Provide_Company_Dialog(context, TIRE_TYPE);
        tpcd = new Tire_Provide_Company_Dialog(context, TIRE_TYPE, provider_lifnr);
        Button tpcd_bt_done = (Button) tpcd.findViewById(R.id.tire_provide_company_done_id);
        tpcd_bt_done.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (tpcd.SELECTED == -1)
                    return;

                final EventPopupC epc = new EventPopupC(context);
                Button bt_done = (Button) epc.findViewById(R.id.btn_ok);
                bt_done.setOnClickListener(new OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        epc.dismiss();
                        tpcd.dismiss();
                        setProvideCompany();
                        // setAcceptor();
                        setAcceptor_from_provider();
                    }
                });
                if (PM082_TAG.equals("1"))
                {
                    // epc.show("배송구분이 직접이면 인수자는 공급자로 세팅됩니다.");

                    // 150715
                    epc.dismiss();
                    tpcd.dismiss();
                    setProvideCompany();
                    setAcceptor_from_provider();

                }
                else
                {
                    tpcd.dismiss();
                    setProvideCompany();
                    // setAcceptor_from_provider();
                }
            }
        });
        tpcd.show();
    }

    // 150714 인수자를 오토케어로 했을때 부서정보 넣기 위해 처리해줌 I_LIFNR, I_SHIPGB
    private void showProvideCompanyDialog2()
    {
        // tpcd = new Tire_Provide_Company_Dialog(context, TIRE_TYPE);
        kog.e("Jonathan", "provider_lifnr :: " + provider_lifnr + " type :: " + TIRE_TYPE);
        tpcd = new Tire_Provide_Company_Dialog(context, TIRE_TYPE, provider_lifnr);
        Button bt_done = (Button) tpcd.findViewById(R.id.tire_provide_company_done_id);
        bt_done.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                tpcd.dismiss();
                if (tpcd.SELECTED == -1)
                    return;
                setAcceptor();
            }
        });
        tpcd.show();
    }

    private void showAddressDialog()
    {
        final Address_Dialog ad = new Address_Dialog(context);
        Button bt_save = (Button) ad.findViewById(R.id.address_save_id);
        bt_save.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                HashMap hm = ad.getTv_full_address();
                if (hm == null)
                {
                    EventPopupC epc = new EventPopupC(context);
                    epc.show("주소를 정제해 주세요");
                    return;
                }

                if (hm.size() <= 0)
                {
                    EventPopupC epc = new EventPopupC(context);
                    epc.show("주소를 정제해 주세요");
                    return;
                }
                String zip_code = hm.get("POST_CODE1").toString();
                tv_acceptor_address.setText(hm.get("CITY1").toString() + " " + hm.get("STREET").toString());
                bt_acceptor_address.setText(zip_code);
                ad.dismiss();
            }
        });
        ad.show();
    }

    // Jonathan 14.10.14 타이어(앞)(뒤) 누를때.
    // 1 : 직접, 2 : 배송
    private void showTireFront()
    {
        if (provider_lifnr == null)
        {
            EventPopupC epc = new EventPopupC(context);
            epc.show("공급업체를 먼저 조회해 주세요.");
            return;
        }
        boolean isSnow = false;
        if (!"0".equals(snwtrq))
        {
            isSnow = true;
        }

        // 배송구분 추가 했음. TIRE_TYPE
        TIRE_TYPE = PM082_TAG;
        kog.e("Jonathan", "배송 구분 / PM082_TAG :: " + TIRE_TYPE);
        tsd = new Tire_Search_Dialog(context, provider_lifnr, tifszcd, isSnow, acceptor_lifnr, TIRE_TYPE);
        Button front_done = (Button) tsd.findViewById(R.id.tire_search_done_id);
        front_done.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                tsd.dismiss();
                if (tsd.SELECTED == -1)
                    return;
                setTireFront();
            }
        });
        tsd.show();
    }

    private void showTireRear()
    {
        if (provider_lifnr == null)
        {
            EventPopupC epc = new EventPopupC(context);
            epc.show("공급업체를 먼저 조회해 주세요.");
            return;
        }
        boolean isSnow = false;
        if (!"0".equals(snwtrq))
        {
            isSnow = true;
        }

        // Jonathan 14.10.14 배송구분 추가 했음. TIRE_TYPE
        TIRE_TYPE = PM082_TAG;
        kog.e("Jonathan", "배송 구분 / PM082_TAG :: " + TIRE_TYPE);

        tsd = new Tire_Search_Dialog(context, provider_lifnr, tibszcd, isSnow, acceptor_lifnr, TIRE_TYPE);
        Button rear_done = (Button) tsd.findViewById(R.id.tire_search_done_id);
        rear_done.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                tsd.dismiss();
                if (tsd.SELECTED == -1)
                    return;
                setTireRear();
            }
        });
        tsd.show();
    }

}
