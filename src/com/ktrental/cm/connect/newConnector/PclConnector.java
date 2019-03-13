/**
 * <pre>
 * Middleware Wrapping Pcl 접속 클래스
 * </pre>
 * 
 * com.ktrental.connect PclConnector.java
 * 
 * @version : v1.0.0
 * @since : 2013.2013. 10. 4.
 * @author : Calix
 */
package com.ktrental.cm.connect.newConnector;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.ktrental.cm.util.PrintLog;
import com.ktrental.common.DEFINE;
import com.ktrental.common.KtRentalApplication;
import com.ktrental.model.LoginModel;
import com.ktrental.util.NetworkUtil;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;

import phos.android.client.ICode;
import phos.android.client.MiddlewareConnector;
import phos.android.client.ParameterList;
import phos.android.client.PhosCrypto;
import phos.android.client.Structure;
import phos.android.client.Table;

public class PclConnector
{
    /* ******************** 전역 변수 셋팅 시작 ********************************* */
    /** Log Tag명 */
    private String              TAG                   = "PclConnector";
    /** Pcl 결과값 FLAG(성공) */
    private final int           D_EXECUTE_SUCCESS     = 0;
    /** Pcl 결과값 FLAG(실패) */
    private final int           D_EXECUTE_ERROR       = -1;
    /** 공통코드 결과값 FLAG(성공) */
    private final int           D_COMMON_CODE_SUCCESS = 10;
    // /** 공통코드 결과값 FLAG(실패) */
    // private final int D_COMMON_CODE_ERROR = -10;

    private Context             mContext;
    private MiddlewareConnector m_sapConnector;
    private ParameterList       m_request;
    private ParameterList       m_response;

    Hashtable<String, String>   mTableConnectInfo;

    private String              NONE_SAP_LOG          = "ZMO_2010_RD01|";

    /* ******************** 전역 변수 셋팅 끝 ********************************* */

    /* ******************** 인터페이스 셋팅 시작 ********************************* */
    private PclResultListener   mPclResultListener    = null;

    public void setOnPclResultListenr(PclResultListener _pclResultListener)
    {
        mPclResultListener = _pclResultListener;
    }

    /* ******************** 인터페이스 셋팅 끝 ********************************* */
    /*
     * 생성자
     */
    public PclConnector(Context _context)
    {
        mContext = _context;
        initConector();
    }

    /**
     * sap connector 초기화
     */
    private void initConector()
    {
        m_sapConnector = null;
        m_sapConnector = new MiddlewareConnector();
        m_sapConnector.setMiddlewareInfo(DEFINE.MW_HOST_IP, DEFINE.MW_HOST_PORT);
        m_sapConnector.setSAPInfo(DEFINE.SAP_SERVER_IP, DEFINE.SAP_CLIENT_NO, DEFINE.SAP_SYSTEM_NO);
        m_sapConnector.setUserInfo("", "");
        m_sapConnector.setConnectTimeOut(DEFINE.MW_CONNECTION_TIMEOUT);
        m_sapConnector.setExecuteTimeOut(DEFINE.SAP_EXECUTE_TIMEOUT);

        PhosCrypto.makeSecretKey4KTRENTAL();

        clearPclInfo();

        PrintLog.Print(TAG, "INIT OK!!!!");
    }

    /**
     * <pre>
     * Pcl 정보를 초기화 시킨다.
     * </pre>
     * 
     * @since 2013. 10. 4.
     * @version v1.0.0
     * @author Calix
     * 
     * <pre></pre>
     */
    public void clearPclInfo()
    {
        mPclResultListener = null;
        m_request = null;
        m_response = null;
        // 생성
        m_request = new ParameterList();
        m_response = new ParameterList();
    }

    /**
     * <pre>
     * RFC 를 호출할 Input Parameter 를 셋팅 한다.
     * </pre>
     * @since      2014. 1. 28.
     * @version    v1.0.0
     * @author     Calix
     * @param _strKey 파라미터 key
     * @param _strValue 파라미터 value
     * <pre></pre>
     */
    public void setParameter(String _strKey, String _strValue)
    {
        if (_strKey.equals("") || _strKey.length() <= 0)
        {
            PrintLog.Print(TAG, "strKey 없음.");
            return;
        }

        if (_strValue.equals("") || _strValue.length() <= 0)
        {
            PrintLog.Print(TAG, "strValue  없음.");
            return;
        }

        // checkRequestParam();

        // PrintLog.Print(TAG, strKey + "::" + strValue);

        // 암호화
        _strValue = PhosCrypto.encrypt(_strValue);

        PrintLog.Print(TAG, _strKey + "::" + PhosCrypto.decrypt(_strValue));

        m_request.addInputParameter(_strKey, _strValue);
    }

    /**
     * Func Desc : RFC 를 호출할 Input Parameter 를 Hashtable으로 셋팅 한다. Input Param : mapParameter : 파라미터 Hashtable Return : SUCC : FAIL : History 최초 작성일 :
     * 2013.10.01 최초 작성자 : Calix 수정일 : 수정자 :
     */
    public void setParameterTable(Hashtable<String, String> _tableParameter)
    {
        if (_tableParameter == null || _tableParameter.size() == 0)
        {
            PrintLog.Print(TAG, "mapParameter 없음.");
            return;
        }

        Iterator<String> it = _tableParameter.keySet().iterator();

        while (it.hasNext())
        {
            String strKey = "";
            String strValue = "";

            strKey = it.next();
            strValue = _tableParameter.get(strKey);

            // PrintLog.Print(TAG, strKey + "::" + strValue);

            setParameter(strKey, strValue);

            // PrintLog.Print(TAG, strKey + "::" + strValue);
        }
    }

    /**
     * Func Desc : RFC 를 호출할 Input Parameter 를 셋팅 한다. Input Param : strKey : 파라미터 key strValue : 파라미터 value Return : SUCC : FAIL : History 최초 작성일 :
     * 2013.06.25 최초 작성자 : 김영필 수정일 : 수정자 :
     */
    public void setStructure(String _strStrcName, Hashtable<String, String> _tableStructure)
    {
        int nCnt = 0;
        nCnt = _tableStructure.size();

        if (nCnt <= 0)
        {
            PrintLog.Print(TAG, "aStructure 없음.");
            return;
        }

        if (_strStrcName.equals("") || _strStrcName.length() <= 0)
        {
            PrintLog.Print(TAG, "Structure  없음.");
            return;
        }

        Structure structure = new Structure(_strStrcName); // Structure 생성

        Iterator<String> it = _tableStructure.keySet().iterator();

        PrintLog.Print(TAG, "SSSStructure Start == " + _strStrcName + " ==============================");
        while (it.hasNext())
        {
            String strKey = "";
            String strValue = "";
            strKey = it.next();
            strValue = _tableStructure.get(strKey);
            PrintLog.Print(TAG, strKey + "::" + strValue);
            strValue = PhosCrypto.encrypt(strValue);
            structure.addParameter(strKey, strValue);
        }

        m_request.addStructure(structure);

        PrintLog.Print(TAG, "SSSStructure End == " + _strStrcName + " ==============================");
    }

    /**
     * Func Desc : RFC 를 호출할 Input Parameter 를 셋팅 한다. Input Param : strKey : 파라미터 key strValue : 파라미터 value Return : SUCC : FAIL : History 최초 작성일 :
     * 2013.06.25 최초 작성자 : 김영필 수정일 : 수정자 :
     */
    public void setTable(String _strTableName, ArrayList<Hashtable<String, String>> _Table)
    {
        int nCnt = 0;
        nCnt = _Table.size();

        if (nCnt <= 0)
        {
            PrintLog.Print("", "aTable 없음.");
            return;
        }

        if (_strTableName.equals("") || _strTableName.length() <= 0)
        {
            PrintLog.Print("", "strTableName  없음.");
            return;
        }

        Table table = new Table(_strTableName); // Table 생성
        PrintLog.Print(TAG, "TTTTable Start == " + _strTableName + " ==============================");
        for (int i = 0; i < nCnt; i++)
        {
            Hashtable<String, String> aMap = new Hashtable<String, String>();

            aMap = _Table.get(i);

            Iterator<String> itMap = aMap.keySet().iterator();
            table.appendRow(); // 열 추가

            while (itMap.hasNext())
            {
                String strKey = "";
                String strValue = "";

                strKey = itMap.next();
                strValue = aMap.get(strKey);
                PrintLog.Print("", strKey + "::" + strValue);
                strValue = PhosCrypto.encrypt(strValue);
                table.setColumn(strKey, strValue);
            }
        }

        m_request.addTable(table);

        PrintLog.Print(TAG, "TTTTable End == " + _strTableName + " ==============================");
    }

    /**
     * <pre>
     * SAP에 Log 남기는 Structure 설정
     * - PERNR      : 사용자 사번
     * - LOGID      : 웹 아이디
     * - ENAME      : 사용자 명
     * - BUKRS      : 회사코드
     * - SCRNO      : 
     * - SYSIP      : 서버아이피
     * - SYS_CD     : 시스템 코드
     * - NO_MASK    :
     * </pre>
     * 
     * @since 2013. 10. 4.
     * @version v1.0.0
     * @author Calix
     * 
     * <pre></pre>
     */
    private void addSapLogStructure()
    {
        Hashtable<String, String> mapTemp = new Hashtable<String, String>();
        LoginModel lm = KtRentalApplication.getLoginModel();

        mapTemp.put("PERNR", lm.getPernr());
        mapTemp.put("LOGID", lm.getLogid());
        mapTemp.put("ENAME", lm.getEname() == null ? "" : lm.getEname());
        mapTemp.put("BUKRS", "3000");//lm.getBukrs() == null ? "" : lm.getBukrs());
        mapTemp.put("SCRNO", lm.getScrno() == null ? "" : lm.getScrno());
        mapTemp.put("SYSIP", "");
        mapTemp.put("SYS_CD", lm.getSys_cd() == null ? "" : lm.getSys_cd());
        mapTemp.put("NO_MASK", "X");

        setStructure("IS_LOGIN", mapTemp);
    }

    private boolean networkConnectionCheck()
    {
        boolean isConnect = NetworkUtil.isNetwork(mContext);

        if (isConnect == false)
        {
            m_response = new ParameterList();
            Structure strcTemp = new Structure("ES_RETURN");
            strcTemp.addParameter("MTYPE", "E");
            strcTemp.addParameter("MESSAGE", PhosCrypto.encrypt("인터넷 연결상태를 확인해주세요."));
            m_response.addStructure(strcTemp);

            if (mPclResultListener != null)
                mPclResultListener.onPclResult(m_response);

            return isConnect;
        }

        return isConnect;
    }

    public void executeRFC(final String _strRFCName, PclResultListener _pclResultListener)
    {
        try
        {
            // 리스너를 추가한다.
            mPclResultListener = _pclResultListener;

            // 인터넷 연결상태를 체크
            if (networkConnectionCheck() == false)
                return;

            // Sap쪽에 Log 정보를 남긴다.
            PrintLog.Print(TAG, "" + NONE_SAP_LOG.indexOf(_strRFCName));
            if (NONE_SAP_LOG.indexOf(_strRFCName) == -1)
                addSapLogStructure();

            Thread executeThread = new Thread(new Runnable()
            {
                @Override
                public void run()
                {
                    try
                    {
                        m_response = m_sapConnector.execute(ICode.CREQ_ALL_UTF8, _strRFCName, m_request, m_sapConnector.getUser());

                        Message msg = new Message();
                        msg.what = D_EXECUTE_SUCCESS;
                        Bundle bundleTemp = new Bundle();
                        bundleTemp.putString("FUNCTION", _strRFCName);
                        msg.setData(bundleTemp);
                        handle.sendMessage(msg);
                    }
                    catch (Exception e)
                    {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                        Message msg = new Message();
                        msg.what = D_EXECUTE_ERROR;
                        msg.obj = e.getLocalizedMessage();
                        Bundle bundleTemp = new Bundle();
                        bundleTemp.putString("FUNCTION", _strRFCName);
                        msg.setData(bundleTemp);
                        handle.sendMessage(msg);
                    }
                }
            });
            executeThread.start();
        }
        catch (Exception e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    // @off
    Handler handle = new Handler()
    {
        public void handleMessage(Message msg)
        {
            // RFC 결과 실패일 경우
            if (m_response == null)
            {
                m_response = new ParameterList();
                Structure strcTemp = new Structure("ES_RETURN");
                strcTemp.addParameter("MTYPE", "E");
                strcTemp.addParameter("MESSAGE", PhosCrypto.encrypt("정보를 가져오지 못했습니다."));
                m_response.addStructure(strcTemp);
            }
            // 공통코드 결과 값
            else if (msg.what == D_COMMON_CODE_SUCCESS)
            {
                m_response = new ParameterList();
                Structure strcTemp = new Structure("ES_RETURN");
                strcTemp.addParameter("MTYPE", PhosCrypto.encrypt("S"));
                m_response.addStructure(strcTemp);
                Bundle bundleTemp = msg.getData();
                m_response.addInputParameter("FUNCTION", PhosCrypto.encrypt(bundleTemp.getString("FUNCTION")));
            }
            // RFC 결과 성공일 경우
            else if(msg.what == D_EXECUTE_ERROR)
            {
                m_response = null;
                m_response = new ParameterList();
                Structure strcTemp = new Structure("ES_RETURN");
                strcTemp.addParameter("MTYPE", PhosCrypto.encrypt("E"));
                strcTemp.addParameter("MESSAGE", PhosCrypto.encrypt(msg.obj.toString()));
                m_response.addStructure(strcTemp);
            }
            else
            {
                Bundle bundleTemp = msg.getData();
                m_response.addInputParameter("FUNCTION", PhosCrypto.encrypt(bundleTemp.getString("FUNCTION")));
            }

            if (mPclResultListener != null)
                mPclResultListener.onPclResult(m_response);

            super.handleMessage(msg);
        }
    };
    // @on
}
