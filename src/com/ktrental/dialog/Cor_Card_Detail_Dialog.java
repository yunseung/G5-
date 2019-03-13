package com.ktrental.dialog;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.ktrental.R;
import com.ktrental.cm.connect.ConnectController;
import com.ktrental.cm.connect.Connector.ConnectInterface;
import com.ktrental.fragment.CorCardRegFragment;
import com.ktrental.model.CorCardAccountModel;
import com.ktrental.model.CorCardDetailSearchModel;
import com.ktrental.model.CorCardModel;
import com.ktrental.model.TableModel;
import com.ktrental.popup.EventPopupC;
import com.ktrental.util.kog;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

public class Cor_Card_Detail_Dialog extends DialogC
implements View.OnClickListener,ConnectInterface
{
    private Window w;
    private WindowManager.LayoutParams lp;
    private Context                            context;
    private CorCardModel model;

    private Button bt_cancel;
    private Button bt_clean;
    private Button bt_save;
    private Button bt_close;
    private Button bt_account_search;

    private TextView tv_account_type,tv_account_name;
//    private TextView tv_regnum, tv_surtax_type, tv_success_num;
//    private TextView tv_write_date, tv_use_date, tv_card_num, tv_operate_budget;
//    private TextView tv_use_price, tv_supply_price, tv_surtax, tv_available_budget, tv_wanna_date, tv_service_price, tv_resolution;
//    private TextView tv_deal_company, tv_deal_company_num, tv_deal_company_gubun;
    private TextView tv_real_use_dept1, tv_real_use_dept2, tv_real_user1, tv_real_user2,tv_project_order, tv_guide1, tv_guide2;

    private EditText tv_dept, tv_users, tv_purpose;

    private ArrayList<CorCardAccountModel> mAccountMap;
    private ArrayList<CorCardAccountModel> mAccountHeaderMap;

    private ConnectController connectController;
    private CorCardAccountModel mSelectedModel;
    private CorCardDetailSearchModel mDetailSearchModel;
    private Boolean mIsClose = false;
    private Boolean mIsSaving = false;

    Context mCtx;

    private final String BTN_OK_CLOSE = "90";

    public Cor_Card_Detail_Dialog(Context context, final CorCardModel model, Handler _mHandler)
    {
        super(context);
        mCtx = context;
        mHandler = _mHandler;
        connectController = new ConnectController(this, context);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.cor_card_detail_dialog);
        w = getWindow();
        w.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN|WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        w.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        lp = (WindowManager.LayoutParams)w.getAttributes();
        lp.width = LayoutParams.MATCH_PARENT;
        lp.height = LayoutParams.MATCH_PARENT;
        w.setAttributes(lp);
        this.context = context;
        this.model = model;

        bt_save = (Button)findViewById(R.id.btn_save);     bt_save.setOnClickListener(this);
        bt_cancel = (Button) findViewById(R.id.btn_cancel);  bt_cancel.setOnClickListener(this);
        bt_clean = (Button) findViewById(R.id.btn_clean);  bt_clean.setOnClickListener(this);
        bt_close = (Button) findViewById(R.id.btn_close); bt_close.setOnClickListener(this);
        bt_account_search = (Button)findViewById(R.id.btn_account_search); bt_account_search.setOnClickListener(this);

//        tv_regnum = (TextView)findViewById(R.id.tv_regnum);
        tv_account_type = (TextView)findViewById(R.id.tv_account_type);
        tv_account_name = (TextView)findViewById(R.id.tv_account_name);
//        tv_surtax_type = (TextView)findViewById(R.id.tv_surtax_type);
//        tv_success_num = (TextView)findViewById(R.id.tv_success_num);
//        tv_write_date = (TextView)findViewById(R.id.tv_write_date);
//        tv_use_date = (TextView)findViewById(R.id.tv_use_date);
//        tv_card_num = (TextView)findViewById(R.id.tv_card_num);
//        tv_operate_budget = (TextView)findViewById(R.id.tv_operate_budget);
//        tv_use_price = (TextView)findViewById(R.id.tv_use_price);
//
//        tv_supply_price = (TextView)findViewById(R.id.tv_supply_price);
//        tv_surtax = (TextView)findViewById(R.id.tv_surtax);
//        tv_available_budget = (TextView)findViewById(R.id.tv_available_budget);
//        tv_wanna_date = (TextView)findViewById(R.id.tv_wanna_date);
//        tv_service_price = (TextView)findViewById(R.id.tv_service_price);
//        tv_resolution = (TextView)findViewById(R.id.tv_resolution);
//        tv_deal_company = (TextView)findViewById(R.id.tv_deal_company);
//        tv_deal_company_num = (TextView)findViewById(R.id.tv_deal_company_num);
//        tv_deal_company_gubun = (TextView)findViewById(R.id.tv_deal_company_gubun);

        ///////////////////Jonathan/////////////////////////////
        tv_real_use_dept1 = (TextView)findViewById(R.id.tv_real_use_dept1);
        tv_real_use_dept2 = (TextView)findViewById(R.id.tv_real_use_dept2);
        tv_real_user1 = (TextView)findViewById(R.id.tv_real_user1);
        tv_real_user2 = (TextView)findViewById(R.id.tv_real_user2);
        tv_project_order = (TextView)findViewById(R.id.tv_project_order);
        tv_guide1= (TextView)findViewById(R.id.tv_guide1);
        tv_guide2= (TextView)findViewById(R.id.tv_guide2);

        tv_dept = (EditText)findViewById(R.id.tv_dept);
        tv_users = (EditText)findViewById(R.id.tv_users);
//        tv_place = (TextView)findViewById(R.id.tv_place);
        tv_purpose = (EditText)findViewById(R.id.tv_purpose);

        mAccountMap = new ArrayList<CorCardAccountModel>();
        callLockCheck();
        setItem();
    }

    private void setItem()
    {
//        tv_regnum.setText("");
//        tv_account_type.setText("");
//        tv_account_name.setText("");
//        tv_surtax_type.setText("");
//        tv_success_num.setText(model.getORGL_PERM_NO());
//        tv_write_date.setText(model.getORGL_PERM_DT());
//        tv_use_date.setText("");
//        tv_card_num.setText(model.getCARD_NO());
//        tv_operate_budget.setText("");
//        tv_use_price.setText(model.getBUY_AMT());
//        tv_supply_price.setText(model.getCHG_SALE_AMT());
//        tv_surtax.setText(model.getCHG_STAX());
//        tv_available_budget.setText("");
//        tv_wanna_date.setText(model.getAPP_SCD_DT());
//        tv_service_price.setText("");
//        tv_resolution.setText("");
//
//        tv_deal_company.setText(model.getTRE_FRAN());
//        tv_deal_company_num.setText(model.getFRAN_BRNO());
//        tv_deal_company_gubun.setText(model.getBT_NM());
        mDetailSearchModel = new CorCardDetailSearchModel(null);
        tv_real_use_dept1.setText(model.getVKGRP());
        tv_real_use_dept2.setText(model.getSKTEXT());
        String pernr = model.getPERNR();
        if(pernr != null) {
            pernr = pernr.replace("0", "");
        }
        tv_real_user1.setText(pernr);
        tv_real_user2.setText(model.getSENAME());
        tv_project_order.setText("");
        if("99".equals(model.getSTATUS())){
            tv_dept.setText("");
            tv_users.setText("");
            tv_purpose.setText("");
            bt_account_search.setEnabled(true);
//            callAccountList();
        } else if("01".equals(model.getSTATUS()) || "00".equals(model.getSTATUS()) || "05".equals(model.getSTATUS())) {
            tv_account_type.setText(model.getDOCTYPE());
            tv_account_name.setText(model.getDOCNAM());
            tv_dept.setText(model.getKTEXT());
            tv_purpose.setText(model.getSGTXT());

            bt_account_search.setEnabled(false);
            tv_dept.setEnabled(false);
            tv_users.setEnabled(false);
            tv_purpose.setEnabled(false);
            bt_save.setEnabled(false);
            bt_clean.setEnabled(false);
            if("05".equals(model.getSTATUS())){
                bt_cancel.setEnabled(false);
            }
        } else {
            bt_account_search.setEnabled(false);
            tv_purpose.setEnabled(false);
            bt_save.setEnabled(false);
            tv_users.setEnabled(false);
            tv_dept.setText("");
            tv_users.setText("");
            tv_purpose.setText(model.getSGTXT());
            tv_account_type.setText(model.getDOCTYPE());
            tv_account_name.setText(model.getDOCNAM());
            mReqeustHandler.sendEmptyMessageDelayed(1, 2000);
        }
        if(!"00".equals(model.getSTATUS())){
            bt_cancel.setEnabled(false);
        }
        mIsClose = false;
        mIsSaving = false;
    }

    private void callLockCheck(){
        connectController.getCorCardLockCheck(model.getELC_SEQ(), "R");
    }

    private void callAccountList(){
        String DOCNAM = "";
        String DOCTYPE = "";
        String I_GUBUN = "";
        if(model.getDOCNAM() != null){
            DOCNAM = model.getDOCNAM();
        }
        if(model.getDOCTYPE() != null){
            DOCTYPE = model.getDOCTYPE();
        }
        if(model.getGUBUN() != null){
            I_GUBUN = model.getGUBUN();
        }
        connectController.getCorCardTYPE(DOCNAM, DOCTYPE, I_GUBUN);
    }

    @Override
    public void onClick(View v)
        {
        switch(v.getId())
            {
                case R.id.btn_account_search: // 계정유형/명 호출
                    if(mAccountMap == null || mAccountMap.size() == 0) {
                        callAccountList();
                    }
                    else {
                        callAccountPopup();
                    }
                    break;
                case R.id.btn_close: //닫기
                    mIsClose = true;
                    connectController.getCorCardLockCheck(model.getELC_SEQ(), "E");
                    dismiss();
                    break;
                case R.id.btn_save:
                    // ZMO_1150_RD03 먼저 호출 (법인카드 Lock Check)
                    if(mSelectedModel == null || mSelectedModel.getDOCTYPE() == null){
                        final EventPopupC epc = new EventPopupC(mContext);
                        Button btn_confirm = (Button) epc.findViewById(R.id.btn_ok);
                        epc.show("계정유형/명 을 입력하여 주십시오.");
                        return;
                    }
                    mIsSaving = true;
                    connectController.getCorCardLockCheck(model.getELC_SEQ(), "R");
                    break;
                case R.id.btn_cancel:
                    if(model != null) {
                        connectController.getCorCardEnrollCancel(model.getWEBTYPE(), model.getWEBDOCNUM(), model.getDOCTYPE(), model.getBELNR(), model.getGJAHR(), model.getELC_SEQ(), model.getSTATUS(), "");
                    }
                    break;
            }
        }



	@Override
	public void connectResponse(String FuntionName, String resultText,
			String MTYPE, int resulCode, TableModel tableModel) {
		// TODO Auto-generated method stub
		hideProgress();

        if(FuntionName != null){
            // 1. 락 체크
            if(FuntionName.equals("ZMO_1150_RD03")){
                if("S".equals(MTYPE)){
                    // 2. 마감여부 호출
                    String dat = model.getORGL_PERM_DT();
                    if(dat != null){
                        dat = dat.replace("/", "");
                    }
//                    String orgeh = model.getORGEH();
//                    if(orgeh != null){
//                        orgeh = orgeh.replace("0", "");
//                    }
                    connectController.getCorCardCloseCheck(dat, model.getORGEH());
                } else {
                    showEventPopup2(null, "" + resultText);
                }
            }
            else if(FuntionName.equals("ZMO_1150_RD02")){
                if("S".equals(MTYPE) && mIsSaving){
//                    connectController.getCorCardDetailSearch(model.getWEBTYPE(), model.getWEBDOCNUM(), model.getDOCTYPE(), model.getBELNR(), model.getGJAHR()
//                    , model.getBUDAT(), model.getORGL_PERM_DT(), model.getSPERN(), model.getSORGEH(), model.getCARD_NO(), model.getORGL_PERM_NO());
//                    CorCardLockCheckModel lockCheckModel = new CorCardLockCheckModel(tableModel.get)
                    String DOCTYPE = "";
                    String BLART = "";
                    String HKONT = "";
                    String DEDUCT = "";
                    if(mSelectedModel != null){
                        DOCTYPE = mSelectedModel.getDOCTYPE();
                        BLART = mSelectedModel.getBLART();

                        if(mAccountMap != null){
                            for(int i=0; i<mAccountMap.size(); i++) {
                                CorCardAccountModel tmpmodel = mAccountMap.get(i);
                                if(tmpmodel != null && tmpmodel.getDOCTYPE() != null){
                                    String doctype = tmpmodel.getDOCTYPE();
                                    if(doctype != null){
                                        if(doctype.equals(DOCTYPE)){
                                            if(tmpmodel.getSHKZG() != null && tmpmodel.getSHKZG().equals("S")){
                                                HKONT = tmpmodel.getHKONT();
//                                                DEDUCT = tmpmodel.getDEDUCT();
                                                BLART = tmpmodel.getBLART();
                                                break;
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                    DEDUCT = mSelectedModel.getDEDUCT();
                    // 이미 등록된 건이라면 이미 전표처리 되었다고 얼럿 띄워 줘야 한다.
                    // 이 여부는 mDetailSearchModel.getmDetailSearchModel() 에 값이 있는지 여부로 보자.

                    boolean isError = false;
                    String errMessage = "";

                    String purpose = "";
                    if(tv_purpose != null){
                        if(tv_purpose.getText() != null){
                            purpose = tv_purpose.getText().toString();
                        }
                        if(purpose.equals("") || purpose.equals(" ")) {
                            isError = true;
                            errMessage = "사용목적을 입력해주세요.";
                        }
                    }
                    String users = "";
                    if(tv_users != null && tv_users.getText() != null){
                        users = tv_users.getText().toString();
                    }
                    String main_dept = "";
                    if(tv_dept != null && tv_dept.getText() != null){
                        main_dept = tv_dept.getText().toString();
                    }

                    if (DOCTYPE != null && DOCTYPE.equals("KD01")){
                        if(users != null){
                            if(users.equals("")) {
                                isError = true;
                                errMessage = "참가인원을 입력해주세요.";
                            } else if(purpose.equals("") || purpose.equals(" ")) {
                                isError = true;
                                errMessage = "사용목적을 입력해주세요.";
                            } else{
                                if (model.getBUY_AMT() != null && mSelectedModel.getWRBTR() != null) {
                                    try {
                                        String str_wrbtr = mSelectedModel.getWRBTR().substring(0, mSelectedModel.getWRBTR().indexOf("."));
                                        String str_buy_amt = model.getBUY_AMT().substring(0, model.getBUY_AMT().indexOf("."));
                                        int wrbtr = Integer.valueOf(str_wrbtr);
                                        int buy_amt = Integer.valueOf(str_buy_amt);
                                        int user_cnt = Integer.valueOf(users);
                                        int total = wrbtr * user_cnt;
                                        if (total < buy_amt) {
                                            isError = true;
                                            errMessage = "사용금액 대비 참가인원이 적습니다.";
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }

                                }
                            }
                        }
                    }

                    if(isError){
                        hideProgress();
                        final EventPopupC epc = new EventPopupC(mContext);
                        Button btn_confirm = (Button) epc.findViewById(R.id.btn_ok);
                        btn_confirm.setOnClickListener(new View.OnClickListener() {

                            @Override
                            public void onClick(View arg0) {
                                // TODO Auto-generated method stub
                                try {
                                    epc.dismiss();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                        epc.show(errMessage);
                        tv_users.requestFocus();
                        mIsSaving = false;
                        return;
                    }


                    connectController.getCorCardEnroll(model.getWEBTYPE(), model.getWEBDOCNUM(), DOCTYPE, "", model.getGJAHR(), BLART, model.getBUDAT(), model.getORGL_PERM_DT()
                            , model.getSPERN(), model.getORGEH(), model.getKTEXT(), "", "", HKONT, DEDUCT, "", model.getBUY_AMT(), model.getCHG_STAX()
                            , "KRW", model.getAPP_SCD_DT(), purpose, model.getSORGEH(), users, model.getTAXTN_TY(), model.getELC_SEQ(), main_dept, model.getCARD_NO()
                            , model.getORGL_PERM_NO(), model.getORGL_PERM_DT(), model.getUSETYPE(), model.getBUY_CANC(), model.getBUY_AMT(), model.getCHG_SALE_AMT(), mDetailSearchModel.getmDetailSearchModel());
                } else if(mIsSaving) {
                    showEventPopup2(null, "" + resultText);
                }
            }
            else if(FuntionName.equals("ZMO_1150_RD04")){
                if("S".equals(MTYPE)) {
                    if (tableModel == null) {
                        return;
                    }
                    ArrayList<HashMap<String, String>> tableArray1 = null;
                    ArrayList<HashMap<String, String>> tableArray2 = null;
                    try {
                        tableArray1 = tableModel.getTableArray("ET_ITEM");
                        tableArray2 = tableModel.getTableArray("ET_HEADER");
                    } catch (Exception e){
                        e.printStackTrace();
                    }

                    MapComparator comp = new MapComparator("DOCTYPE");

                    if (tableArray1 != null) {
                        Collections.sort(tableArray1, comp);
                    }

                    if (tableArray2 != null) {
                        Collections.sort(tableArray2, comp);
                    }

                    HashMap<String, String> table = null;

                    String LTEXT1;
                    String DOCNAM;
                    String USPLAC_CHK;
                    String TEXT20;
                    String USMEM_CHK;
                    String WITH_CHK;
                    String WRBTR;
                    String DOCTYPE;
                    String EXPENS_CHK;
                    String KAUFN_CHK;
                    String DONATN_CHK;
                    String BLART;
                    String DEDUCT;
                    String TEXT;
                    String USDEP_CHK;
                    String WEBTYPE;
                    String WEBTYPENM;
                    String BUKRS;
                    String MITKZ;
                    String TXT20;
                    String TXT50;
                    String HKONT;
                    String SHKZG;
                    String UMSKZ;
                    String SHBKZ;
                    String KTEXT;
                    String MWSKZ;

                    if (tableArray1 != null && tableArray1.size() > 0) {
                        for (int i = 0; i < tableArray1.size(); i++) {
                            table = tableArray1.get(i);
                            if (table != null && table.size() > 0) {
                                LTEXT1 = table.get("LTEXT1");
                                DOCNAM = table.get("DOCNAM");
                                USPLAC_CHK = table.get("USPLAC_CHK");
                                TEXT20 = table.get("TEXT20");
                                USMEM_CHK = table.get("USMEM_CHK");
                                WITH_CHK = table.get("WITH_CHK");
                                WRBTR = table.get("WRBTR");
                                DOCTYPE = table.get("DOCTYPE");
                                EXPENS_CHK = table.get("EXPENS_CHK");
                                KAUFN_CHK = table.get("KAUFN_CHK");
                                DONATN_CHK = table.get("DONATN_CHK");
                                BLART = table.get("BLART");
                                DEDUCT = table.get("DEDUCT");
                                TEXT = table.get("TEXT");
                                USDEP_CHK = table.get("USDEP_CHK");
                                WEBTYPE = table.get("WEBTYPE");
                                WEBTYPENM = table.get("WEBTYPENM");
                                BUKRS = table.get("BUKRS");
                                MITKZ = table.get("MITKZ");
                                TXT20 = table.get("TXT20");
                                TXT50 = table.get("TXT50");
                                HKONT = table.get("HKONT");
                                SHKZG = table.get("SHKZG");
                                UMSKZ = table.get("UMSKZ");
                                SHBKZ = table.get("SHBKZ");
                                KTEXT = table.get("KTEXT");
                                MWSKZ = table.get("MWSKZ");
                                CorCardAccountModel model = new CorCardAccountModel(LTEXT1, DOCNAM, USPLAC_CHK, TEXT20, USMEM_CHK, WITH_CHK, WRBTR, DOCTYPE, EXPENS_CHK, KAUFN_CHK
                                        , DONATN_CHK, BLART, DEDUCT, TEXT, USDEP_CHK, WEBTYPE, WEBTYPENM, BUKRS, MITKZ, TXT20, TXT50, HKONT, SHKZG, UMSKZ, SHBKZ, KTEXT, MWSKZ);
                                if (mAccountMap == null) {
                                    mAccountMap = new ArrayList<CorCardAccountModel>();
                                }
                                mAccountMap.add(model);
                            }
                        }
                    }
                    Log.d("TAG", "TAG");
                    Log.d("TAG", "TAG");
                    if (tableArray2 != null && tableArray2.size() > 0) {
                        for (int i = 0; i < tableArray2.size(); i++) {
                            table = tableArray2.get(i);
                            if (table != null && table.size() > 0) {
                                LTEXT1 = table.get("LTEXT1");
                                DOCNAM = table.get("DOCNAM");
                                USPLAC_CHK = table.get("USPLAC_CHK");
                                TEXT20 = table.get("TEXT20");
                                USMEM_CHK = table.get("USMEM_CHK");
                                WITH_CHK = table.get("WITH_CHK");
                                WRBTR = table.get("WRBTR");
                                DOCTYPE = table.get("DOCTYPE");
                                EXPENS_CHK = table.get("EXPENS_CHK");
                                KAUFN_CHK = table.get("KAUFN_CHK");
                                DONATN_CHK = table.get("DONATN_CHK");
                                BLART = table.get("BLART");
                                DEDUCT = table.get("DEDUCT");
                                TEXT = table.get("TEXT");
                                USDEP_CHK = table.get("USDEP_CHK");
                                WEBTYPE = table.get("WEBTYPE");
                                WEBTYPENM = table.get("WEBTYPENM");
                                BUKRS = table.get("BUKRS");
                                MITKZ = table.get("MITKZ");
                                TXT20 = table.get("TXT20");
                                TXT50 = table.get("TXT50");
                                HKONT = table.get("HKONT");
                                SHKZG = table.get("SHKZG");
                                UMSKZ = table.get("UMSKZ");
                                SHBKZ = table.get("SHBKZ");
                                KTEXT = table.get("KTEXT");
                                MWSKZ = table.get("MWSKZ");
                                CorCardAccountModel model = new CorCardAccountModel(LTEXT1, DOCNAM, USPLAC_CHK, TEXT20, USMEM_CHK, WITH_CHK, WRBTR, DOCTYPE, EXPENS_CHK, KAUFN_CHK
                                        , DONATN_CHK, BLART, DEDUCT, TEXT, USDEP_CHK, WEBTYPE, WEBTYPENM, BUKRS, MITKZ, TXT20, TXT50, HKONT, SHKZG, UMSKZ, SHBKZ, KTEXT, MWSKZ);
                                if (mAccountHeaderMap == null) {
                                    mAccountHeaderMap = new ArrayList<CorCardAccountModel>();
                                }
                                mAccountHeaderMap.add(model);
                            }
                        }
                    }
                    callAccountPopup();
                } else {
                    showEventPopup2(null, "" + resultText);
                }
//                CorCardAccountModel model = new CorCardAccountModel()
//                mAccountMap.put(tableModel.getTableArray())

            }
            else if(FuntionName.equals("ZMO_1150_RD05")){ // 상세조회. tableArray 가 두벌 온다
                if("S".equals(MTYPE)){
                    if(tableModel == null){
                        return;
                    }
                    ArrayList<HashMap<String, String>> tableArray = tableModel.getTableArray();
                    if(tableArray == null || tableArray.size() == 0){
                        return;
                    }

                    if(tableArray.size() > 0) {
                        mDetailSearchModel = new CorCardDetailSearchModel(tableArray);
                    }


                    String DOCTYPE = "";
                    String BLART = "";
                    String HKONT = "";
                    String DEDUCT = "";
                    if(mSelectedModel != null){
                        DOCTYPE = mSelectedModel.getDOCTYPE();
                        BLART = mSelectedModel.getBLART();
                        HKONT = mSelectedModel.getHKONT();
                        DEDUCT = mSelectedModel.getDEDUCT();
                    }
                } else {
                    showEventPopup2(null, "" + resultText);
                }
            } else if (FuntionName.equals("ZMO_1150_WR01")) {
                showEventPopup2(null, "" + resultText);
                if("S".equals(MTYPE)) {
                    mHandler.sendEmptyMessage(CorCardRegFragment.MESSAGE_REFRESH);
                    dismiss();
                }
            } else if (FuntionName.equals("ZMO_1150_WR02")) {
                showEventPopup2(null, "" + resultText);
                if("S".equals(MTYPE)) {
                    mHandler.sendEmptyMessage(CorCardRegFragment.MESSAGE_REFRESH);
                    dismiss();
                }
            }
        }

//		if("S".equals(MTYPE))
//		{
//
//			showEventPopup2(new OnEventOkListener() {
//
//				@Override
//				public void onOk() {
//					// TODO Auto-generated method stub
//					dismiss();
//					mHandler.sendEmptyMessage(0);
//				}
//			}, resultText);
//		}
//		else
//		{
//
//			connectController.duplicateLogin(mContext);
//
//			showEventPopup2(new OnEventOkListener() {
//
//				@Override
//				public void onOk() {
//					// TODO Auto-generated method stub
//					dismiss();
//				}
//			}, resultText);
//		}


	}

	private void callAccountPopup(){
        Account_List_Dialog account_list_dialog = new Account_List_Dialog(mContext, mAccountHeaderMap);
        account_list_dialog.setOnClickRowListener(new Account_List_Dialog.OnClickRowListener() {
            @Override
            public void onClickRowListener(int position) {
                mSelectedModel = mAccountHeaderMap.get(position);
                if(mSelectedModel != null){
                    tv_account_type.setText(mSelectedModel.getDOCTYPE());
                    tv_account_name.setText(mSelectedModel.getDOCNAM());
                    if(mSelectedModel.getDOCTYPE() != null && mSelectedModel.getDOCTYPE().equals("KD00")){
                        bt_save.setEnabled(false);
                        bt_cancel.setEnabled(false);
                    }
                    if(model.getREASON() != null && model.getREASON().equals("10")){
                        tv_guide1.setText("금액초과");
                        tv_guide2.setText("사용 내역이므로 사용목적에 상세한 내역(결재문서번호 등)을 입력해주세요. (금액 : 직책자 50만원, 직책자외 30만원)");
                    } else if(model.getREASON() != null && model.getREASON().equals("20")){
                        tv_guide1.setText("연차(휴가)");
                        tv_guide2.setText("사용 내역이므로 사용목적에 상세한 내역을 입력해주세요");
                    } else if(model.getREASON() != null && model.getREASON().equals("30")){
                        tv_guide1.setText("휴일");
                        tv_guide2.setText("사용 내역이므로 사용목적에 상세한 내역을 입력해주세요");
                    } else if(model.getREASON() != null && model.getREASON().equals("40")){
                        tv_guide1.setText("야간");
                        tv_guide2.setText("사용 내역이므로 사용목적에 상세한 내역을 입력해주세요. (시간 : 00-07시)");
                    } else {
                        tv_guide1.setText("");
                        tv_guide2.setText("");
                    }
                }
            }
        });
        account_list_dialog.show();
    }

    Handler mReqeustHandler = new Handler()
    {

        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            kog.e("KDH", "handleMessage");
            if(msg != null){
                if(msg.what == 1){
                    connectController.getCorCardDetailSearch(model.getWEBTYPE(), model.getWEBDOCNUM(), model.getDOCTYPE(), model.getBELNR(), model.getGJAHR(),
                            model.getBUDAT(), model.getORGL_PERM_DT(), model.getSPERN(), model.getSORGEH(), model.getCARD_NO(), model.getORGL_PERM_NO());
                }
            }
            super.handleMessage(msg);
        }

    };


    Handler mHandler = new Handler()
    {

        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            kog.e("KDH", "handleMessage");
            super.handleMessage(msg);
        }

    };

	@Override
	public void reDownloadDB(String newVersion) {
		// TODO Auto-generated method stub

	}

    @Override
    public void dismiss() {
        super.dismiss();
        mIsClose = true;
        mIsSaving = false;
    }

    @Override
    protected void onStop() {
        super.onStop();
        mIsClose = true;
        mIsSaving = false;
    }


    class MapComparator implements Comparator<HashMap<String, String>> {

        private final String key;

        public MapComparator(String key) {
            this.key = key;
        }

        @Override
        public int compare(HashMap<String, String> first, HashMap<String, String> second) {
            int result = first.get(key).compareTo(second.get(key));
            return result;
        }
    }

}
