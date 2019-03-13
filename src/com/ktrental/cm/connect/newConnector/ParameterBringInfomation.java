/**
 * <pre>
 * Parameter에 들어있는 정보를 가져오는 클래스
 * </pre>
 * 
 * com.ktrental.connect ParameterBringInfomation.java
 * 
 * @version : v1.0.0
 * @since : 2013.2013. 10. 4.
 * @author : Calix
 */
package com.ktrental.cm.connect.newConnector;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Locale;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

import phos.android.client.Column;
import phos.android.client.ParameterList;
import phos.android.client.PhosCrypto;
import phos.android.client.Row;
import phos.android.client.Structure;
import phos.android.client.Table;

import com.ktrental.cm.util.PrintLog;

public class ParameterBringInfomation
{
    /* ******************** 전역 변수 셋팅 시작 ********************************* */
    /** Log Tag 정보 */
    private String                          TAG                       = "ParameterBringInfomation";

    /** ParameterBringInfomation Singleton 변수 */
    private static ParameterBringInfomation _parameterBringInfomation = null;
    
    /** 암호화 생성 날짜 */
    public SimpleDateFormat                 sdf;
    /** secretKey 생성에 필요한 키 */
    public String                           key                       = "_KTRENTAL!";
    /** secretKey 생성 */
    public String                           secretKey;
    /** 암호화 정보 생성 */
    public SecretKeySpec                    skeySpec;
    /** 암호화 클래스 */
    public Cipher                           cipher;

    /* ******************** 전역 변수 셋팅 끝 ********************************* */

    /* ******************** 인터페이스 셋팅 시작 ********************************* */

    /* ******************** 인터페이스 셋팅 끝 ********************************* */

    /* ******************** 메소드 셋팅 시작 ********************************* */

    /**
     * <pre>
     * Singleton으로만 생성할 수 있게 생성자의 속성을 private로 선언
     * </pre>
     * 
     * @since 2013. 10. 5.
     * @version v1.0.0
     * @author Calix
     * 
     * <pre></pre>
     */
    private ParameterBringInfomation()
    {
        try
        {
            sdf = new SimpleDateFormat("yyyyMM", Locale.KOREA);
            secretKey = (new StringBuilder(String.valueOf(sdf.format(new Date())))).append(key).toString();
            skeySpec = new SecretKeySpec(secretKey.getBytes(), "AES");

            cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");
            cipher.init(2, skeySpec);
        }
        catch (NoSuchAlgorithmException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        catch (NoSuchPaddingException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        catch (InvalidKeyException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * <pre>
     * Singleton 생성자
     * </pre>
     * 
     * @since 2013. 10. 4.
     * @version v1.0.0
     * @author Calix
     * @return <pre>
     *  ParameterBringInfomation 생성된 클래스를 넘겨준다.
     * </pre>
     */
    public static ParameterBringInfomation getInstance()
    {
        if (_parameterBringInfomation == null)
        {
            synchronized (ParameterBringInfomation.class)
            {
                if (_parameterBringInfomation == null)
                {
                    _parameterBringInfomation = new ParameterBringInfomation();
                }
            }
        }

        return _parameterBringInfomation;
    }

    /**
     * <pre>
     * ParameterList에서 해당 Name에 해당하는 Parameter를 반환한다.
     * </pre>
     * 
     * @since 2013. 10. 4.
     * @version v1.0.0
     * @author Calix
     * @param _paramList : Parameter 정보를 담고있는 ParameterList
     * @return <pre>
     *  Parameter 정보를 String 형태로 반환한다.
     * </pre>
     */
    public String getParameter(String _strParamKey, ParameterList _paramList)
    {
        String strValue = "";

        if (_strParamKey.equals("") || _strParamKey.length() <= 0)
        {
            PrintLog.Print(TAG, "strKey 없음.");
            return "";
        }

        if (_paramList == null)
        {
            PrintLog.Print(TAG, "_paramList 없음.");
            return "";
        }

        strValue = PhosCrypto.decrypt(_paramList.getValue(_strParamKey));

        PrintLog.Print(TAG, "PARAMETER === " + _strParamKey + "::" + strValue);

        return strValue;
    }

    /**
     * <pre>
     * ParameterList에 담겨있는 Parameter 전부를 반환한다.
     * </pre>
     * 
     * @since 2013. 10. 4.
     * @version v1.0.0
     * @author Calix
     * @param _paramList : Parameter 정보를 담고있는 ParameterList
     * @return <pre>
     *  Parameter 정보를 Hashtable에 담아서 반환한다.
     * </pre>
     */
    public Hashtable<String, String> getParameterMap(ParameterList _paramList)
    {
        if (_paramList == null)
        {
            PrintLog.Print(TAG, "_paramList 없음.");
            return new Hashtable<String, String>();
        }

        Hashtable<String, String> mapTemp = new Hashtable<String, String>();
        ArrayList<Column> arrTemp = _paramList.getScalar();
        int nColumnCount = arrTemp.size();
        for (int i = 0; i < nColumnCount; i++)
        {
            Column column = arrTemp.get(i);
            mapTemp.put(column.getName(), PhosCrypto.decrypt(column.getValue()));
        }

        return mapTemp;
    }

    /**
     * <pre>
     * ParameterList에서 Name에 해당하는 Structure를 반환한다.
     * </pre>
     * 
     * @since 2013. 10. 4.
     * @version v1.0.0
     * @author Calix
     * @param strStructName
     * @param _paramList : Structure 정보를 담고있는 ParameterList
     * @return <pre>
     *  Structure 정보를 Hashtable에 담아서 반환한다.
     * </pre>
     */
    public HashMap<String, String> getStructure(String strStructName, ParameterList _paramList)
    {
    	HashMap<String, String> resMap = new HashMap<String, String>();

        if (strStructName.equals("") || strStructName.length() <= 0)
        {
            PrintLog.Print(TAG, "strName 없음.");
            return resMap;
        }

        if (_paramList == null)
        {
            PrintLog.Print(TAG, "response 없음.");
            return resMap;
        }

        Structure struct = _paramList.getStructure(strStructName);

        if (struct == null || struct.getAllBytes() < 0)
        {
            PrintLog.Print(TAG, "Structure 없음.");
            return resMap;
        }

        ArrayList<Column> aColumn = struct.getScalarData();

        int nCnt = aColumn.size();
        PrintLog.Print(TAG, "SSSStruct Start == " + strStructName + " ==============================");
        for (int i = 0; i < nCnt; i++)
        {
            Column col = aColumn.get(i);

            // String strKey = col.getName();
            // StringBuffer strValue = new StringBuffer(PhosCrypto.decrypt(col.getValue()));

            // PrintLog.Print(TAG, strKey + "::" + strValue);

            resMap.put(col.getName(), PhosCrypto.decrypt(col.getValue()));

        }
        PrintLog.Print(TAG, "SSSStruct End == " + strStructName + " ==============================");
        return resMap;
    }

    /**
     * <pre>
     * ParameterList에서 Name에 해당하는 Table을 반환한다.
     * </pre>
     * 
     * @since 2013. 10. 4.
     * @version v1.0.0
     * @author Calix
     * @param strStructName
     * @param _paramList : Table 정보를 담고있는 ParameterList
     * @return <pre>
     *  Table 정보를 ArrayList<Hashtable<String, String>>에 담아서 반환한다.
     * </pre>
     */
    public ArrayList<HashMap<String, String>> getTable(String strTableName, ParameterList _paramList)
    {
        long startTime = System.currentTimeMillis();

        System.out.println("Table Get Time Start = " + startTime);

        if (strTableName.equals("") || strTableName.length() <= 0)
        {
            PrintLog.Print(TAG, "strName 없음.");
            return new ArrayList<HashMap<String, String>>();
        }

        if (_paramList == null)
        {
            PrintLog.Print(TAG, "response 없음.");
            return new ArrayList<HashMap<String, String>>();
        }

        Table table = _paramList.getTable(strTableName);

        if (table == null || table.getNumRow() <= 0)
        {
            PrintLog.Print(TAG, "Table 없음.");
            return new ArrayList<HashMap<String, String>>();
        }

        int nCnt = table.getNumRow();

        ArrayList<HashMap<String, String>> resArray = new ArrayList<HashMap<String, String>>(nCnt);// new ArrayList<Hashtable<String, String>>();

        PrintLog.Print(TAG, "TTTTable Start == " + strTableName + " ==============================");
        for (int i = 0; i < nCnt; i++)
        {
            Row row = table.getRow(i);

            ArrayList<Column> aColumn = row.getColumns();

            int nColumnCnt = aColumn.size();

            HashMap<String, String> mapColumn = new HashMap<String, String>(nColumnCnt);

            // PrintLog.Print(TAG, "TTTTable Row == " + i + " ==============================");
            for (int j = 0; j < nColumnCnt; j++)
            {
                // StringBuffer sbKey = new StringBuffer(aColumn.get(j).getName());
                // StringBuffer sbValue = new StringBuffer(PhosCrypto.decrypt(aColumn.get(j).getValue()));

                // PrintLog.Print(TAG, sbKey.toString() + " ::  " + sbValue.toString());
                mapColumn.put(aColumn.get(j).getName(), decrypt(aColumn.get(j).getValue()));
            }

            resArray.add(mapColumn);
        }
        PrintLog.Print(TAG, "TTTTable End == " + strTableName + " ==============================");

        System.out.println("Table Get Time End = " + (System.currentTimeMillis() - startTime) + " Table Row = " + resArray.size());
        return resArray;
    }

    /* ******************** 메소드 셋팅 끝 ********************************* */

    public String decrypt(String src)
    {
        String decrypted = "";
        try
        {
            decrypted = new String(cipher.doFinal(hexStringToByteArray(src)));
        }
        catch (Exception e)
        {
            // decrypted = "[ERROR] INVALID KEY.";
            decrypted = src;
        }
        return decrypted;
    }

//    private byte[] hexToByteArray(String hex)
//    {
//        if (hex == null || hex.length() == 0)
//            return null;
//        byte ba[] = new byte[hex.length() / 2];
//        for (int i = 0; i < ba.length; i++)
//            ba[i] = (byte) Integer.parseInt(hex.substring(2 * i, 2 * i + 2), 16);
//
//        return ba;
//    }

    private byte[] hexStringToByteArray(String s)
    {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2)
        {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4) + Character.digit(s.charAt(i + 1), 16));
        }
        return data;
    }

    /* ******************** Listener 셋팅 시작 ********************************* */

    /* ******************** Listener 셋팅 끝 ********************************* */
}
