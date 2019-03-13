package com.ktrental.product;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Locale;

import phos.android.client.ParameterList;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;

import com.ktrental.R;
import com.ktrental.beans.ZDOILTY;
import com.ktrental.cm.connect.newConnector.ParameterBringInfomation;
import com.ktrental.cm.connect.newConnector.PclConnector;
import com.ktrental.cm.connect.newConnector.PclResultListener;
import com.ktrental.cm.util.PrintLog;
import com.ktrental.dialog.VOC_Dialog;
import com.ktrental.fragment.VocInfoAdapter;
import com.ktrental.popup.DatepickPopup2;
import com.ktrental.popup.EventPopup1;
import com.ktrental.popup.EventPopup2;
import com.ktrental.popup.EventPopupC;
import com.ktrental.popup.EventPopupCC;
import com.ktrental.popup.Popup_Window_M041;
import com.ktrental.popup.Popup_Window_M041.OnDismissListener;
import com.ktrental.popup.ProgressPopup;
import com.ktrental.util.OnEventOkListener;

/**
 * <p class="note">
 * <strong>Class 설명:</strong>
 * </p>
 * <p class="note">
 * <strong>Note:</strong>
 * </p>
 */
public class VocInfoActivity extends BaseActivity implements OnClickListener, PclResultListener, OnItemClickListener
{
    /* ******************** Variable Set ********************************* */
    
    public static String TAG = "VocInfoFragment";
    public static String VOC_KUNNR = "KUNNR";
    public static String VOC_VCDIV = "VCDIV";
    public static String FUNC_VOC_LIST = "ZMO_1140_RD01";
    public static String FUNC_VOC_DETAIL = "ZMO_1140_RD02";
    
    // Voc 데이타 없음 
    ImageView imgVocNoData;
    // Voc Info 리스트 
    ListView lvVocInfo;
    // 테이블 구성 어답터
    VocInfoAdapter adapterVocInfo;
    // VOC 구분 팝업 뷰
    Popup_Window_M041 popupM049;
    /* ******************** Variable Set End ********************************* */

    /* ******************** Interface Set ********************************* */

    /* ******************** Interface Set End ********************************* */

    /* ******************** Abstract Set ********************************* */

    /* ******************** Abstract Set End ********************************* */

    /* ******************** Override Set ********************************* */

    public VocInfoActivity()
    {

    }

    @Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
//		super.onBackPressed();
    	finish();
	}



	@Override 
    protected void onCreate(Bundle savedInstanceState)
    {
       super.onCreate(savedInstanceState);
       setContentView(R.layout.voc_info_fragment);
       
       initLayout();
       initData();
       sendDataProcessing();
    }

    /* ******************** Override Set End ********************************* */

    /* ******************** Method Set ********************************* */

    // 레이아웃 초기화
    private void initLayout()
    {
        // 버튼 이벤트 생성
        // 닫기
        findViewById(R.id.id_btn_voc_close).setOnClickListener(this);
        // 시작 일자
        findViewById(R.id.id_btn_voc_date_start).setOnClickListener(this);
        // 종료 일자
        findViewById(R.id.id_btn_voc_date_end).setOnClickListener(this);
        // VOC 구분
        findViewById(R.id.id_btn_voc_gubun).setOnClickListener(this);
        // 조회
        findViewById(R.id.id_btn_voc_search).setOnClickListener(this);
        // 상세조회
        findViewById(R.id.id_voc_detail_search).setOnClickListener(this);
        
        // 전역 변수 레이아웃 생성
        // 데이타 없음 이미지
        imgVocNoData = (ImageView) findViewById(R.id.id_voc_info_nodata_img);
        // 리스트 뷰
        lvVocInfo = (ListView) findViewById(R.id.id_voc_listview);
        lvVocInfo.setOnItemClickListener(this);
    }

    // 데이타 초기화
    private void initData()
    {
        adapterVocInfo = new VocInfoAdapter(this, R.layout.voc_info_row, new ArrayList<HashMap<String,String>>());
        // 어답터 추가
        lvVocInfo.setAdapter(adapterVocInfo);
        
        // 날짜 정보 추가
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH) + 1;
        int day = cal.get(Calendar.DAY_OF_MONTH);
        
        String previousYearDate = String.format(Locale.getDefault(), "%04d.%02d.%02d", (year - 1), month, day);
        String nowDate = String.format(Locale.getDefault(), "%04d.%02d.%02d", year, month, day);
        
        ((Button) findViewById(R.id.id_btn_voc_date_start)).setText(previousYearDate);
        ((Button) findViewById(R.id.id_btn_voc_date_end)).setText(nowDate);
        
        // VOC 구분 정보 추가
        popupM049 = new Popup_Window_M041(this){
            // 값을 셋팅하면 초기값을 반환
            @Override
            public void initM049Value(String code, String value)
            {
                findViewById(R.id.id_btn_voc_gubun).setTag(code);
                ((Button) findViewById(R.id.id_btn_voc_gubun)).setText(value);
            }
            // 선택 된 데이타 처리
			@Override
			public void selectM049Value(String code, String value) {
				findViewById(R.id.id_btn_voc_gubun).setTag(code);
                ((Button) findViewById(R.id.id_btn_voc_gubun)).setText(value);
			}
        };
    }

    // 전달 받은 데이타가 있는경우 처리한다
    private void sendDataProcessing() 
    {
        if(getIntent() == null || getIntent().hasExtra(VOC_KUNNR) == false) {
            // 메시지 팝업
            EventPopup2 ep1 = new EventPopup2(this, "고객정보가 없습니다.", new OnEventOkListener()
            {
                
                @Override
                public void onOk()
                {
                    finish();
                }
            });
            ep1.show();
            
            return;
        }
        
        findViewById(R.id.id_btn_voc_gubun).setTag("01");
        ((Button) findViewById(R.id.id_btn_voc_gubun)).setText("고객불만");
        
        excuteVOCInfoSearch();
    }
    
    private boolean checkLoginInfo() 
    {
        String dateStart = ((Button) findViewById(R.id.id_btn_voc_date_start)).getText().toString();
        dateStart = dateStart.replace(".", "");
        // 종료 날짜
        String dateEnd= ((Button) findViewById(R.id.id_btn_voc_date_end)).getText().toString();
        dateEnd= dateEnd.replace(".", "");
        
        int start = Integer.parseInt(dateStart) - 10000;
        int end = Integer.parseInt(dateEnd);
        
        if(getIntent() != null)
        {
            String kunnr = getIntent().hasExtra(VOC_KUNNR) ? getIntent().getStringExtra(VOC_KUNNR) : "";
            
            if(kunnr == null || kunnr.trim().length() == 0) {
                
                // 고객번호 없음 에러
            	EventPopup2 popupCC = new EventPopup2(this, "고객번호가 없습니다.", null);
                popupCC.show();
                
                return false;
            }
        }
        else if(end - 10000 <= start)
        {
        	EventPopup2 popupCC = new EventPopup2(this, "조회 기간은 1년입니다.", null);
            popupCC.show();
//            EventPopupCC popupCC = new EventPopupCC(this, "조회 기간은 1년입니다.");
//            popupCC.show();
            
            return false;
        }
        
        return true;
    }
    
    /*
     * VOC 팝업을 닫는다.
     */
    private void closeVocInfo()
    {
        finish();
        //getFragmentManager().popBackStack(TAG, FragmentManager.POP_BACK_STACK_INCLUSIVE);
    }

    /*
     * 날짜 팝업을 보여준다.
     */
    private void showDatePopup(Button btnDate)
    {
        String date = btnDate.getText().toString();
        date = date.replace(".", "");
        DatepickPopup2 idatepickpopup = new DatepickPopup2(this, date, 0);
        idatepickpopup.show(btnDate);
    }

    /*
     * VOC 구분 DropBox를 보여준다.
     */
    private void showGubunDropBox(Button btnGubun)
    {
        popupM049.show(btnGubun);
    }

    /* ******************** Method Set End ********************************* */

    /* ******************** Server Method Set ********************************* */

    /*
     * VOC 검색을 한다.
     */
    private void excuteVOCInfoSearch()
    {
        if(checkLoginInfo() == false)
            return;
        
        adapterVocInfo.clear();
        imgVocNoData.setVisibility(View.VISIBLE);
        
        pp = new ProgressPopup(this);
        pp.setMessage("조회 중 입니다.");
        pp.show();
        
        // 시작 날짜
        String dateStart = ((Button) findViewById(R.id.id_btn_voc_date_start)).getText().toString();
        dateStart = dateStart.replace(".", "");
        // 종료 날짜
        String dateEnd= ((Button) findViewById(R.id.id_btn_voc_date_end)).getText().toString();
        dateEnd= dateEnd.replace(".", "");
        
        String vocCode = findViewById(R.id.id_btn_voc_gubun).getTag().toString();
        
        PclConnector pclConnector = new PclConnector(this);
        Hashtable<String, String> mapTemp = new Hashtable<String, String>();
        // 고객번호
        mapTemp.put("KUNNR", getIntent().getStringExtra(VOC_KUNNR));
        // 조회 시작일
        mapTemp.put("DT_FROM", dateStart);
        // 조회 종료일
        mapTemp.put("DT_TO", dateEnd);
        // VOC 구분 코드
        mapTemp.put("VCDIV_V", vocCode);
        pclConnector.setStructure("IS_1035", mapTemp);
        pclConnector.executeRFC(FUNC_VOC_LIST , this);
    }

    /*
     * VOC 상세 검색을 한다.
     */
    private void excuteVOCInfoDetailSearch()
    {
    	HashMap<String, String> mapTemp = adapterVocInfo.getItem(adapterVocInfo.getCheckPosition());
    	
    	if(mapTemp == null || mapTemp.size() == 0)
    	{
    		EventPopup2 eventPopup = new EventPopup2(this, "VOC 내역조회할 고객을 선택하세요.", null);
    	    eventPopup.show();
    	    return;
    	}
    	
    	pp = new ProgressPopup(this);
        pp.setMessage("조회 중 입니다.");
        pp.show();
    	
    	String I_VBELN = mapTemp.get("VBELN");
    	
    	PclConnector pclConnector = new PclConnector(this);
    	
        // 고객번호
        pclConnector.setParameter("I_VBELN", I_VBELN);
        pclConnector.executeRFC(FUNC_VOC_DETAIL , this);
    }

    /* ******************** Server Method Set End ********************************* */

    /* ******************** Listener Set ********************************* */

    // 버튼 선택 이벤트
    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
        // 닫기
            case R.id.id_btn_voc_close:
            {

                closeVocInfo();
                break;
            }
            // 날짜 시작, 끝
            case R.id.id_btn_voc_date_start:
            case R.id.id_btn_voc_date_end:
            {

                showDatePopup((Button) v);
                break;
            }
            // VOC 구분
            case R.id.id_btn_voc_gubun:
            {
                showGubunDropBox((Button) v);
                break;
            }
            // 조회
            case R.id.id_btn_voc_search:
            {
                excuteVOCInfoSearch();
                break;
            }
            // 상세 조회
            case R.id.id_voc_detail_search:
            {

                excuteVOCInfoDetailSearch();
                break;
            }
            default:
                break;
        }
    }
    
    /**
     * 
     * <pre>
     * RFC 통신 결과 값
     * </pre>
     * @see PclResultListener#onPclResult(ParameterList)
     * @since      2015. 12. 10.
     * @version    v1.0.0
     * @author     JuHH
     * @param _paramList
     * <pre></pre>
     */
    @Override
    public void onPclResult(ParameterList _paramList)
    {
        pp.dismiss();
        
        HashMap<String, String> mapReturn = ParameterBringInfomation.getInstance().getStructure("ES_RETURN", _paramList);

        String strReturnType = mapReturn.get("MTYPE");
        if (strReturnType == null || strReturnType.equals("S") == false)
        {
            String strMessae = mapReturn.get("MESSAGE");
            
            PrintLog.Print("onPclResult", strMessae);
            // 에러 메시지 표시
//            EventPopupCC popupCC = new EventPopupCC(this, strMessae);
//            popupCC.show();
            EventPopup2 eventPopup = new EventPopup2(this, strMessae, null);
    	    eventPopup.show();
            return;
        }
        
        String strFunc = ParameterBringInfomation.getInstance().getParameter("FUNCTION", _paramList);
        // VOC 리스트 조회
        if(strFunc.equals(FUNC_VOC_LIST))
        {
            ArrayList<HashMap<String, String>> datas = ParameterBringInfomation.getInstance().getTable("ET_1036", _paramList);
            
            adapterVocInfo.clear();
            adapterVocInfo.addAll(datas);
            
            if(datas.size() > 0) 
            {
            	imgVocNoData.setVisibility(View.GONE);
            }
        }
        // VOC 상세 조회
        else if(strFunc.equals(FUNC_VOC_DETAIL))
        {
        	HashMap<String, String> mapTemp = ParameterBringInfomation.getInstance().getStructure("ES_1015", _paramList);
        	ArrayList<HashMap<String, String>> datas = ParameterBringInfomation.getInstance().getTable("ET_1020", _paramList);
        	
        	VOC_Dialog vocDialog = new VOC_Dialog(this, mapTemp, datas, 0);
        	vocDialog.show();
        	
        }
    }

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		
		PrintLog.Print("VOC LIST", "position = " + position);
		adapterVocInfo.setCheckPosition(position);
	}

    /* ******************** Listener Set End ********************************* */
}
