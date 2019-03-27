package com.ktrental.cm.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabaseLockedException;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import com.ktrental.cm.connect.ConnectController;
import com.ktrental.cm.util.PrintLog;
import com.ktrental.common.DEFINE;
import com.ktrental.model.DbQueryModel;
import com.ktrental.model.O_ITAB1;
import com.ktrental.util.kog;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;

public class SqlLiteAdapter {

    String TAG = "SqlLiteAdapter";

    SQLiteDatabase m_db = null;

    String m_strColumnType = "text";

    private String mDBPath;

    private static SqlLiteAdapter mSqlLiteAdapter;

    // private String m_strTableName = "";

    private DbProgressInterface mDbProgressInterface;

    private final static String CUD_KEY = "CUD";
    private final static String CUD_CREATE = "C";
    private final static String CUD_UPDATE = "U";
    private final static String CUD_DELETE = "D";

    private final static String REFAIR_ZSEQ = "ZSEQ";
    private final static String REFAIR_PERNR = "MPERNR";
    private final static String REFAIR_EQUNR = "EQUNR";
    private final static String REFAIR_CEMER = "CEMER";
    private final static String REFAIR_BSYM = "BSYM";

    private final static String CAMASTER_INVNR = "INVNR";

    private final static String LOGIN_BUKRS = "BUKRS";

    private final static String[] REFAIR_KEY_COLUMNAMES = {REFAIR_ZSEQ,
            REFAIR_PERNR, REFAIR_EQUNR, REFAIR_CEMER, REFAIR_BSYM};

    private final static String[] CAR_MASTER_KEY_COLUMNAMES = {CAMASTER_INVNR};

    private final static String[] LOGIN_KEY_COLUMNAMES = {"LOGID"};

    private final static String[] STOCK_KEY_COLUMNAMES = {"MATNR"};
    private final static String[] SEND_BASE_KEY_COLUMNAMES = {DEFINE.DRIVN};
    private final static String[] SEND_STOCK_KEY_COLUMNAMES = {DEFINE.DRIVN};
    private final static String[] SEND_IMAGE_KEY_COLUMNAMES = {DEFINE.DRIVN};
    private final static String[] ADDRESS_KEY_COLUMNAMES = {"ZSEQ"};
    private final static String[] CHECK_TABLES = {DEFINE.O_ITAB1_TABLE_NAME,
            DEFINE.REPAIR_TABLE_NAME, DEFINE.CAR_MASTER_TABLE_NAME,
            DEFINE.LOGIN_TABLE_NAME, DEFINE.STOCK_TABLE_NAME,
            DEFINE.PARTSMASTER_TABLE_NAME};

    private Context mContext;

    public void setDbProgressInterface(DbProgressInterface dbProgressInterface) {
        mDbProgressInterface = dbProgressInterface;
    }

    public interface DbProgressInterface {
        public void onPublishUdapte(String type, int progressCount);

    }

    public SqlLiteAdapter(Context _context, String path) {
        mDBPath = path;
        mSqlLiteAdapter = this;
        mContext = _context;

        initData(_context);
    }

    public static SqlLiteAdapter getInstance() {

        // if(mSqlLiteAdapter==null)
        // mSqlLiteAdapter = new SqlLiteAdapter(_context, path)

        return mSqlLiteAdapter;
    }

    private void initData(Context context) {
        // m_db = SQLiteDatabase.openDatabase(mDBPath + "/"
        // + DEFINE.SQLLITE_DB_NAME, null,
        // SQLiteDatabase.CREATE_IF_NECESSARY);

        try {
            m_db = context.openOrCreateDatabase(mDBPath + "/"
                            + DEFINE.SQLLITE_DB_NAME,
                    SQLiteDatabase.CREATE_IF_NECESSARY, null);

        } catch (SQLiteDatabaseLockedException e) {
            // TODO: handle exception
//			PrintLog.Print("HONG", " initData Exception " + e.getMessage());
        }

    }

    public void closeDB() {
        if (m_db != null) {
            m_db.close();
            m_db = null;
        }
    }

    public boolean createTable(String strTable, ArrayList<String> aColumn) {
        String strQuery = "";
        int nCnt = 0;
        boolean bRes = false;

        nCnt = aColumn.size();

        if (nCnt <= 0) {
            System.out.println("DB = " + aColumn.get(nCnt));
            return bRes;
        }

        strQuery = makeCreateTableQuery(strTable, aColumn);

        System.out.println("Query = " + strQuery);

        bRes = executeQuery(strQuery);

        return bRes;
    }

    public boolean dropTable(String strTable) {
        String strQuery = "";
        boolean bRes = false;

        strQuery = makeDropQuery(strTable);

        bRes = executeQuery(strQuery);

        return bRes;
    }

    public boolean insertTable(String strTable,
                               ArrayList<HashMap<String, String>> aInsertData) {
        String strQuery = "";
        boolean bRes = false;

        ArrayList<String> strQueryArr = makeInsertQuery(strTable, aInsertData);

        bRes = executeQuery(strQueryArr);

        return bRes;
    }

    public boolean insertTableContentValues(String strTable,
                                            ArrayList<HashMap<String, String>> aInsertData) {
        boolean bRes = false;

        ArrayList<ContentValues> strQueryArr = makeQuery(strTable, aInsertData);

        bRes = doTransaction(strQueryArr, strTable);

        return bRes;
    }

    public boolean selectData() {
        String strQuery = "";
        boolean bRes = false;

        return bRes;
    }

    public boolean updateData() {
        String strQuery = "";
        boolean bRes = false;

        return bRes;
    }

    public boolean deleteData() {
        String strQuery = "";
        boolean bRes = false;

        return bRes;
    }

    public boolean executeQuery(String strQuery) {
        boolean bRes = false;

        System.out.println("excuteQuery = " + strQuery);

        if (strQuery.length() < 0 || strQuery.equals("")) {

            return bRes;
        }

        ArrayList<String> aValue = new ArrayList<String>();


        aValue.add(strQuery);

        bRes = doTransaction(aValue);

        return bRes;
    }

    public boolean executeQuery(ArrayList<String> aValue) {
        boolean bRes = false;

        System.out.println("excuteQuery2 = " + aValue);

        int nCnt = aValue.size();

        if (nCnt <= 0) {
            PrintLog.Print("", "Query Data ����.");
            return bRes;
        }

        bRes = doTransaction(aValue);

        return bRes;
    }

    @SuppressWarnings("finally")
    private boolean doTransaction(ArrayList<String> aValue) {
        boolean bRes = false;

        if (m_db == null) {
            PrintLog.Print("", "DB OPEN ����.");
            return bRes;
        }

        m_db.beginTransaction();

        try {
            for (int i = 0; i < aValue.size(); i++) {
                String strQuery = aValue.get(i);
                try {
                    m_db.execSQL(strQuery);
                } catch (SQLiteException e) {
                    // TODO: handle exception
                }

            }

            m_db.setTransactionSuccessful();
            bRes = true;
        } catch (Exception e) {
            e.printStackTrace();

            PrintLog.Print("", e.getMessage());

            bRes = false;
        } finally {
            m_db.endTransaction();
            return bRes;
        }
    }

    private Cursor doRowQuery(String strQuery) {
        Cursor cur = null;

        cur = m_db.rawQuery(strQuery, null);

        return cur;
    }

    private String makeCreateTableQuery(String strTable,
                                        ArrayList<String> aColumn) {
        String strQuery = "";
        String strColumn = "";

        int nCnt = aColumn.size();

        if (nCnt <= 0) {
            PrintLog.Print("", "Query Data ����.");
            return strQuery;
        }

        strQuery = "Create table IF NOT EXISTS " + strTable;
        strQuery += "(";

        for (int i = 0; i < aColumn.size(); i++) {
            strColumn += aColumn.get(i) + " " + m_strColumnType;

            if (i < nCnt - 1) {
                strColumn += ",";
            }
        }

        strQuery += strColumn + ")";

        PrintLog.Print("", strQuery);

        return strQuery;
    }

    private String makeDropQuery(String strTable) {
        String strQuery = "";

        strQuery = "drop table " + strTable;

        return strQuery;
    }

    //
    // private String getCUD(HashMap<String, String> aData, String tableName) {
    // String strQuery = "";
    //
    // String cud = aData.get(CUD_KEY);
    //
    // if (cud.equals(CUD_CREATE)) {
    // strQuery = "Insert Into " + tableName + "(";
    //
    // } else if (cud.equals(CUD_UPDATE)) {
    // strQuery = "Insert Into " + tableName + "(";
    // } else if (cud.equals(CUD_DELETE)) {
    //
    // }
    //
    // return strQuery;
    // }

    private ArrayList<String> makeInsertQuery(String strTable,
                                              ArrayList<HashMap<String, String>> aData) {
        String strQuery = "";

        ArrayList<String> aRes = new ArrayList<String>();

        int nCnt = aData.size();

        if (nCnt <= 0) {
            PrintLog.Print("", "Query Data ����.");
            return aRes;
        }

        for (int i = 0; i < nCnt; i++) {
            int nMapSize = 0;
            int nMapCount = 0;

            HashMap<String, String> aMap = new HashMap<String, String>();

            aMap = aData.get(i);
            nMapSize = aMap.size();

            strQuery = "Insert Into " + strTable + "(";
            // strQuery= makeQuery(aMap,strTable);

            String strKeys = "";
            String strValues = "";

            Iterator<String> itMap = aMap.keySet().iterator();

            while (itMap.hasNext()) {
                String strTmpKey = "";
                String strTmpValues = "";

                strTmpKey = itMap.next();
                strTmpValues = "'" + aMap.get(strTmpKey) + "'";

                PrintLog.Print("", strTmpKey + "::" + strTmpValues);

                strKeys += strTmpKey;
                strValues += strTmpValues;

                nMapCount++;

                if (nMapCount < nMapSize) {
                    strKeys += ",";
                    strValues += ",";
                }
            }

            strQuery += strKeys + ") values(" + strValues + ")";

            PrintLog.Print("", strQuery);

            aRes.add(strQuery);
        }

        return aRes;
    }

    private ArrayList<ContentValues> makeQuery(String strTable,
                                               ArrayList<HashMap<String, String>> aData) {
        String strQuery = "";

        ArrayList<ContentValues> aRes = new ArrayList<ContentValues>();

        int nCnt = aData.size();

        if (nCnt <= 0) {
            PrintLog.Print("", "Query Data ����.");
            return aRes;
        }

        // 윤승이 로그
        for (HashMap<String, String> aa : aData) {
            Log.e("yunseung11", "valuese : " + aa.values());
            Log.e("yunseung11", "reqNo : " + aa.get("REQNO"));
            Log.e("yunseung11", "OILTYP : " + aa.get("OILTYP"));
            Log.e("yunseung11", "VKGRP_TX : " + aa.get("VKGRP_TX"));
        }

        for (int i = 0; i < nCnt; i++) {

            HashMap<String, String> aMap = new HashMap<String, String>();

            aMap = aData.get(i);

            Iterator<String> itMap = aMap.keySet().iterator();

            ContentValues contentValues = new ContentValues();

            while (itMap.hasNext()) {
                String strTmpKey = "";
                String strTmpValues = "";

                strTmpKey = itMap.next();
                strTmpValues = aMap.get(strTmpKey);

                contentValues.put(strTmpKey, strTmpValues);

            }

            aRes.add(contentValues);
        }

        return aRes;
    }

    private String makeSelectQuery(String strTable,
                                   ArrayList<HashMap<String, String>> aData) {
        String strQuery = "";

        ArrayList<String> aRes = new ArrayList<String>();

        int nCnt = aData.size();

        if (nCnt <= 0) {
            PrintLog.Print("", "Query Data ����.");
            return strQuery;
        }

        for (int i = 0; i < nCnt; i++) {
            int nMapSize = 0;
            int nMapCount = 0;

            HashMap<String, String> aMap = new HashMap<String, String>();

            aMap = aData.get(i);
            nMapSize = aMap.size();

            strQuery = "Insert Into " + strTable + "(";

            String strKeys = "";
            String strValues = "";

            Iterator<String> itMap = aMap.keySet().iterator();

            while (itMap.hasNext()) {
                String strTmpKey = "";
                String strTmpValues = "";

                strTmpKey = itMap.next();
                strTmpValues = aMap.get(strTmpKey);

                PrintLog.Print("", strTmpKey + "::" + strTmpValues);

                strKeys += strTmpKey;
                strValues += strTmpValues;

                nMapCount++;

                if (nMapCount < nMapSize) {
                    strKeys += ",";
                    strValues += ",";
                }
            }

            strQuery += strKeys + ") VALUES(" + strValues + ")";

            PrintLog.Print("", strQuery);

            aRes.add(strQuery);
        }

        return strQuery;
    }

    private String makeUpdateQuery(String strTable,
                                   ArrayList<HashMap<String, String>> aData) {
        String strQuery = "";

        return strQuery;
    }

    private String makeDeleteQuery() {
        String strQuery = "";

        return strQuery;
    }

    public HashMap<Integer, Serializable> selectAllTableQuery(String tableName,
                                                              DbProgressInterface dbProgressInterface, String downloadPath) {
        mDbProgressInterface = dbProgressInterface;

        HashMap<Integer, Serializable> map = null;

        SQLiteDatabase db = SQLiteDatabase.openDatabase(downloadPath, null,
                SQLiteDatabase.CONFLICT_NONE);

        Cursor cursor = db.query(tableName, null, null, null, null, null, null);

        kog.e("Jonathan", "sqlite ???");

        if (tableName.equals("COMMON"))
            tableName = O_ITAB1.TABLENAME;

        if (isTable(tableName))
            m_db.delete(tableName, null, null);
        else
            createTable(tableName, cursor);

        map = getCursorData(cursor, tableName);

//		db.close(); //2018-11-02 db close 는 하지 않는다. 워낙 많은 곳에서 사용하는 중에 닫아버려 문제됨.

        return map;
    }

    public ArrayList<String> getTableNames(String downloadPath) {
        ArrayList<String> reTables = new ArrayList<String>();

        SQLiteDatabase db = SQLiteDatabase.openDatabase(downloadPath, null,
                SQLiteDatabase.CONFLICT_NONE);

        Cursor c = db.rawQuery("SELECT name FROM sqlite_master", null);

        if (c.moveToFirst()) {
            for (; ; ) {
                reTables.add(c.getString(0));
                if (!c.moveToNext())
                    break;
            }
        }
        c.close();
//		db.close(); //2018-11-02 db close 는 하지 않는다. 워낙 많은 곳에서 사용하는 중에 닫아버려 문제됨.
        return reTables;
    }

    private void createTable(String tableName, Cursor cursor) {

        int columnCnt = cursor.getColumnCount();

        ArrayList<String> arr = new ArrayList<String>();
        for (int i = 0; i < columnCnt; i++) {
            arr.add(cursor.getColumnName(i));
        }

        String query = makeCreateTableQuery(tableName, arr);
        executeQuery(query);
    }

    private boolean isTable(String tableName) {

        boolean isTableFlag = false;

        Cursor c = m_db.rawQuery("SELECT name FROM sqlite_master", null);

        // if (c != null) {
        // if (c.getCount() > 0)
        // isTableFlag = true;
        // }

        if (c.moveToFirst()) {
            for (; ; ) {
                // Log.e(TAG, "table name : " + c.getString(0));
                if (tableName.equals(c.getString(0))) {
                    isTableFlag = true;
                    break;
                }
                if (!c.moveToNext())
                    break;
            }
        }

        return isTableFlag;
    }

    private HashMap<Integer, Serializable> getCursorData(Cursor cursor,
                                                         String tableName) {

        HashMap<Integer, Serializable> map = new HashMap<Integer, Serializable>();

        cursor.moveToFirst();

        int columnCnt = cursor.getColumnCount();
        int count = 0;

        mDbProgressInterface.onPublishUdapte("max", cursor.getCount());
        m_db.beginTransaction();

        while (!cursor.isAfterLast()) {

            ArrayList<String> arr = new ArrayList<String>();
            ContentValues contentValues = new ContentValues();
            for (int i = 0; i < columnCnt; i++) {
                contentValues.put(cursor.getColumnName(i), cursor.getString(i));
                arr.add(cursor.getString(i));
            }

            m_db.insert(tableName, null, contentValues);
            if (mDbProgressInterface != null)
                mDbProgressInterface.onPublishUdapte("progress", count);

            count++;
            cursor.moveToNext();

        }

        m_db.setTransactionSuccessful();

        m_db.endTransaction();
        cursor.close();

        return map;
    }

    private O_ITAB1 getO_ITAB1(ArrayList<String> arr) {

        O_ITAB1 o_ITAB1 = null;

        String zcodeh = arr.get(0);
        String zcodeh2 = arr.get(1);
        String zcodev = arr.get(2);
        String zcodevt = arr.get(3);

        String set1 = arr.get(4);

        o_ITAB1 = new O_ITAB1(zcodeh, zcodeh2, zcodev, zcodevt, set1);

        return o_ITAB1;
    }

    public boolean deleteRow(String tableName, String[] keys,
                             ContentValues values) {

        int re = -1;

        String whereCause = "";
        String[] whereArgs = new String[keys.length];

        for (int i = 0; i < keys.length; i++) {
            String key = keys[i] + " = ?";
            whereCause = whereCause + key;

            whereArgs[i] = (String) values.get(keys[i]);

            if (keys.length == i + 1)
                break;
            whereCause = whereCause + " AND ";

        }
        try {
            re = m_db.delete(tableName, whereCause, whereArgs);
        } catch (SQLiteException exception) {

        }

        return re > 0;

    }

    public boolean updateRow(String tableName, String[] keys,
                             ContentValues values) {

        String whereCause = "";
        int count = 0;
        String[] whereArgs = null;
        if (keys != null) {
            count = keys.length;
            whereArgs = new String[count];
        }
        for (int i = 0; i < count; i++) {
            String key = keys[i] + " = ?";
            whereCause = whereCause + key;

            whereArgs[i] = (String) values.get(keys[i]);

            if (keys.length == i + 1)
                break;
            whereCause = whereCause + " AND ";

        }

        int re = m_db.update(tableName, values, whereCause, whereArgs);

        return re > 0;

    }

    private boolean insertRow(String tableName, ContentValues values) {
        long re = m_db.insert(tableName, null, values);

        return re > 0;
    }

    private void queryCUD(String tableName, ContentValues values) {

        String cud = values.getAsString(CUD_KEY);

        String[] columes = getColumes(tableName, values);//
        // getColumes(values);

        PrintLog.Print("", "tableName = " + tableName + "  cud = " + cud + "  " + values);

        if (cud == null) {
            if (tableName.equals(DEFINE.CALL_LOG_TABLE_NAME)) {
                insertRow(tableName, values);
                return;
            }
            notCUD(tableName, values, columes);
            return;
        }

        // DB에 인코딩으로 저장으로 바뀌어 디코딩 후 값을 확인 해야한다.
        // cud = ConnectorUtil.decrypt(cud);

        if (cud.equals(CUD_CREATE)) {
            insertRow(tableName, values);
        } else if (cud.equals(CUD_UPDATE)) {
            // String primary = (String)
            // values.getAsString(CUD_PRIMARY_COLUMNAME);

//			PrintLog.Print(TAG,
//					tableName + "  " + cud + " " + values.get("AUFNR") + " "
//							+ values.get("CCMSTS") + " " + values.get("GSTRS"));


            updateRow(tableName, columes, values);
        } else if (cud.equals(CUD_DELETE)) {
            // String primary = (String)
            // values.getAsString(CUD_PRIMARY_COLUMNAME);
            deleteRow(tableName, columes, values);
        }

    }

    private void notCUD(String tableName, ContentValues values, String[] columes) {
        boolean result = false;

        try {
            result = updateRow(tableName, columes, values);

            if (!result)
                insertRow(tableName, values);
        } catch (Exception e) {
            if (!result)
                insertRow(tableName, values);
        }
    }

    @SuppressWarnings("finally")
    private boolean doTransaction(ArrayList<ContentValues> aValue,
                                  String tableName) {
        boolean bRes = false;

        if (m_db == null) {
            PrintLog.Print("", "DB OPEN ����.");
            return bRes;
        }

        m_db.beginTransaction();

        if (tableName.equals(DEFINE.ADDRESS_TABLE)) {
            m_db.delete(DEFINE.ADDRESS_TABLE, null, null);
        }

        if (mDbProgressInterface != null)
            mDbProgressInterface.onPublishUdapte("max", aValue.size());
        try {
            for (int i = 0; i < aValue.size(); i++) {
                ContentValues contentValues = aValue.get(i);
                queryCUD(tableName, contentValues);
                if (mDbProgressInterface != null)
                    mDbProgressInterface.onPublishUdapte("progress", i);

            }

            m_db.setTransactionSuccessful();
            bRes = true;
        } catch (Exception e) {
            e.printStackTrace();

            PrintLog.Print("", e.getMessage());

            bRes = false;
        } finally {
            m_db.endTransaction();
            return bRes;
        }
    }

    public Cursor selectDB(DbQueryModel dbQueryModel) {

        if (m_db == null)
            initData(mContext);

        Cursor reCursor = null;

        String whereCause = "";
        String[] whereCauseArray = dbQueryModel.getWhereCause();

        String backWhere = "";

        int andCount = 0;

        if (whereCauseArray != null) {

            for (int i = 0; i < whereCauseArray.length; i++) {
                whereCause = whereCause + whereCauseArray[i] + " = ?";

                if (whereCauseArray.length == i + 1) {
                    for (int j = 0; j < andCount; j++) {
                        whereCause = whereCause + " )";
                    }
                    break;
                }

                if (whereCauseArray[i].equals(whereCauseArray[i + 1])) {

                    whereCause = whereCause + " OR ";
                } else {

                    andCount++;
                    if (dbQueryModel.isNotFlag()) {
                        whereCause = whereCause + "AND NOT (";
                    } else {
                        whereCause = whereCause + " AND (";
                    }
                }

            }
        } else {
            whereCause = null;
        }

        kog.e("Jonathan", " 쿼리문 :: " + whereCause);

        String orderBy = dbQueryModel.getOrderBy();

        //2014-01-16 KDH 데이터 겁나게 못돌리네 결국엔 여기서 쓸꺼면 스태틱으로 뺴면 더편하지 에휴-_-;;어디서 나오는 패턴 줏어다가 그대로 만들었구만.
        try {

//			kog.e("Jonathan", "쿼리문 ?? :: " + dbQueryModel.getTableName() + " " + dbQueryModel.getColums().toString()+ " " + whereCause+ " " + dbQueryModel.getWhereArgs()+ " " + orderBy);

            reCursor = m_db.query(dbQueryModel.getTableName(),
                    dbQueryModel.getColums(), whereCause,
                    dbQueryModel.getWhereArgs(), null, null, orderBy);

        } catch (SQLiteException e) {
            // TODO: handle exception
            PrintLog.Print("SQLiteException", e.getMessage());
        }

        if (dbQueryModel.getTableName().equals(ConnectController.REPAIR_TABLE_NAME)) {
            Log.e("yunseung", "쿼리문 ?? :: " + dbQueryModel.getTableName() + " " + dbQueryModel.getColums().toString() + " " + whereCause + " " + dbQueryModel.getWhereArgs() + " " + orderBy);
        }
        return reCursor;

    }

    private String[] getColumes(String tableName, ContentValues values) {

        String[] resultColoumes = null;

        if (tableName.equals(DEFINE.CAR_MASTER_TABLE_NAME)) {
            kog.e("Jonathan", "CAR_MASTER_TABLE_NAME :: ");
            resultColoumes = CAR_MASTER_KEY_COLUMNAMES;
        } else if (tableName.equals(ConnectController.REPAIR_TABLE_NAME)) {
            resultColoumes = REFAIR_KEY_COLUMNAMES;
        } else if (tableName.equals(DEFINE.LOGIN_TABLE_NAME)) {
            resultColoumes = LOGIN_KEY_COLUMNAMES;
        } else if (tableName.equals(DEFINE.STOCK_TABLE_NAME)) {
            resultColoumes = STOCK_KEY_COLUMNAMES;
        } else if (tableName.equals(DEFINE.REPAIR_RESULT_BASE_TABLE_NAME)) {
            resultColoumes = getColumes(values);
        } else if (tableName.equals(DEFINE.REPAIR_RESULT_STOCK_TABLE_NAME)) {
            resultColoumes = getColumes(values);
        } else if (tableName.equals(DEFINE.REPAIR_RESULT_IMAGE_TABLE_NAME)) {
            resultColoumes = getColumes(values);
        } else if (tableName.equals(DEFINE.ADDRESS_TABLE)) {
            resultColoumes = ADDRESS_KEY_COLUMNAMES;
        }

        return resultColoumes;
    }

    private String[] getColumes(ContentValues values) {

        String[] resultColoumes = new String[values.size()];

        Set<Entry<String, Object>> s = values.valueSet();
        Iterator itr = s.iterator();

        int i = 0;

        while (itr.hasNext()) {
            Entry me = (Entry) itr.next();
            String key = me.getKey().toString();

            resultColoumes[i] = key;
            i++;
        }

        return resultColoumes;
    }

    public String checkNeedTable() {

        String reMessage = "";

        Cursor c = m_db.rawQuery("SELECT name FROM sqlite_master", null);

        // if (c != null) {
        // if (c.getCount() > 0)
        // isTableFlag = true;
        // }

        ArrayList<String> array = new ArrayList<String>();

        if (c.moveToFirst()) {
            for (; ; ) {

                array.add(c.getString(0));

                if (!c.moveToNext())
                    break;
            }
        }
        reMessage = checkTable(array);
        return reMessage;

    }

    private String checkTable(ArrayList<String> array) {
        String message = "";

        for (int i = 0; i < CHECK_TABLES.length; i++) {
            String checkTableName = CHECK_TABLES[i];
            boolean checkTable = false;

            for (String string : array) {
                if (checkTableName.equals(string)) {
                    checkTable = true;
                    break;
                }
            }
            if (!checkTable) {
                message = getCheckTableMessage(checkTableName);
                return message;
            }
        }

        return message;
    }

    private String getCheckTableMessage(String tableName) {
        String message = "";
        if (DEFINE.O_ITAB1_TABLE_NAME.equals(tableName)) {
            message = "공통코드";
        } else if (DEFINE.REPAIR_TABLE_NAME.equals(tableName)) {
            message = "순회정비 계획";
        } else if (DEFINE.CAR_MASTER_TABLE_NAME.equals(tableName)) {
            message = "소속 MOT 순회기사 리스트";
        } else if (DEFINE.LOGIN_TABLE_NAME.equals(tableName)) {
            message = "로그인 사용자 정보";
        } else if (DEFINE.STOCK_TABLE_NAME.equals(tableName)) {
            message = "보유재고 테이블";
        } else if (DEFINE.PARTSMASTER_TABLE_NAME.equals(tableName)) {
            message = "순회정비 재고 마스터";
        }

        return message;
    }
}
