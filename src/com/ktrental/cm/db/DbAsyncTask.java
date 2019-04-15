package com.ktrental.cm.db;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;

import com.ktrental.cm.connect.ConnectController;
import com.ktrental.cm.db.SqlLiteAdapter.DbProgressInterface;
import com.ktrental.cm.util.PrintLog;
import com.ktrental.common.DEFINE;
import com.ktrental.model.DbQueryModel;
import com.ktrental.model.TableModel;
import com.ktrental.util.CommonUtil;
import com.ktrental.util.SharedPreferencesUtil;
import com.ktrental.util.kog;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * 렌탈에서 사용되는 AsyncTask. 9가지 타입을 가지고 RFC 모듈에서 가져오는 DB 파일 로드 , 검색, 입력을 할 수 있다.
 * 
 * 
 * @author Hong
 * @since 2013.07.12
 */
public class DbAsyncTask extends AsyncTask<Integer// excute()실행시 넘겨줄 데이터타입
        , String// 진행정보 데이터 타입 publishProgress(), onProgressUpdate()의 인수
        , Integer// doInBackground() 종료시 리턴될 데이터 타입 onPostExecute()의 인수
        > implements DbProgressInterface
{

    public final static int         DB_ROAD         = 0;
    public final static int         DB_TABLE_CREATE = 1;
    public final static int         DB_SELECT       = 2;
    public final static int         DB_INSERT       = 3;
    public final static int         DB_DELETE       = 4;
    public final static int         DB_ARRAY_INSERT = 5;
    public final static int         DB_UPDATE       = 6;
    public final static int         DB_TABLE_CHECK  = 7;
    public final static int         DB_DROP_TABLES  = 8;
    public final static int         DB_DELETE_ALL_ROW = 9;
    private String                  mTableName      = null;
    private ProgressDialog          mDlg;
    private Context                 mContext;
    private DbAsyncResLintener      mAsyncResLintener;
    private TableModel              mTableModel;
    private DbQueryModel            mDbQueryModel;
    private Cursor                  mCursor;
    private String                  mFunName        = "";
    private boolean                 calcenFlag      = false;
    private HashMap<String, String> mTableNameMap;
    private String                  mDownLoadDbPath;
    private ContentValues           mContentValues;
    private String[]                mWhereKey;
    private ArrayList<String>       mDropTables     = new ArrayList<String>();

    // public DbAsyncTask(int type) {
    // mType = type;
    // }
    public interface DbAsyncResLintener
    {

        public void onCompleteDB(String funName, int type, Cursor cursor, String tableName);
    }

    // public DbAsyncTask(Context context, DbAsyncResLintener lintener) {
    // mContext = context;
    // mAsyncResLintener = lintener;
    // }

    public DbAsyncTask(String funName, String tableName, Context context, DbAsyncResLintener lintener, TableModel tableModel)
    {
        kog.e("Jonathan", "관련품목 펑션 1");
        mTableName = tableName;
        mContext = context;
        mAsyncResLintener = lintener;
        mTableModel = tableModel;
        mFunName = funName;
    }

    public DbAsyncTask(String funName, String tableName, Context context, DbAsyncResLintener lintener, TableModel tableModel,
            ArrayList<String> tableNames)
    {
        kog.e("Jonathan", "관련품목 펑션 2");
        mTableName = tableName;
        mContext = context;
        mAsyncResLintener = lintener;
        mTableModel = tableModel;
        mFunName = funName;
        mDropTables = tableNames;
    }

    public DbAsyncTask(String funName, String tableName, Context context, DbAsyncResLintener lintener, ContentValues contentValues, String[] whereKey)
    {
        kog.e("Jonathan", "관련품목 펑션 3");
        mTableName = tableName;
        mContext = context;
        mFunName = funName;
        mAsyncResLintener = lintener;
        mContentValues = contentValues;
        mWhereKey = whereKey;
    }

    public DbAsyncTask(String funName, String tableName, Context context, DbAsyncResLintener lintener, TableModel tableModel, String downloadPath)
    {
        kog.e("Jonathan", "관련품목 펑션 4");
        mTableName = tableName;
        mContext = context;
        mAsyncResLintener = lintener;
        mTableModel = tableModel;
        mFunName = funName;
        mDownLoadDbPath = downloadPath;
    }

    public DbAsyncTask(String funName, Context context, DbAsyncResLintener lintener, DbQueryModel dbQueryModel)
    {
        kog.e("Jonathan", "관련품목 펑션 5");
        mTableName = dbQueryModel.getTableName();
        mContext = context;
        mAsyncResLintener = lintener;
        mDbQueryModel = dbQueryModel;
        mFunName = funName;
        kog.e("Jonathan", "123 :: 1" + mTableName + " 2" + mContext + " 3" + mAsyncResLintener + " 4" + mDbQueryModel + " 5" + mFunName.toString());
    }

    public DbAsyncTask(String funName, String tableName, Context context, DbAsyncResLintener lintener, TableModel tableModel,
            HashMap<String, String> aTableNameMap)
    {
        kog.e("Jonathan", "관련품목 펑션 6");
        mTableName = tableName;
        mContext = context;
        mAsyncResLintener = lintener;
        mTableModel = tableModel;
        mFunName = funName;
        mTableNameMap = aTableNameMap;
    }

    public void printMap(Map mp)
    {
        Iterator it = mp.entrySet().iterator();
        while (it.hasNext())
        {
            Map.Entry pairs = (Map.Entry) it.next();
            kog.e("KDH", pairs.getKey() + " = " + pairs.getValue());
            it.remove(); // avoids a ConcurrentModificationException
        }
    }

    public DbAsyncTask(String funName, Context context, DbAsyncResLintener lintener, DbQueryModel dbQueryModel, HashMap<String, String> aTableNameMap)
    {
        kog.e("Jonathan", "관련품목 펑션 7");
        mTableName = dbQueryModel.getTableName();
        mContext = context;
        mAsyncResLintener = lintener;
        mDbQueryModel = dbQueryModel;
        mFunName = funName;
        mTableNameMap = aTableNameMap;
    }

    @Override
    protected void onPreExecute()
    {
        // mDlg = new ProgressDialog(mContext);
        // mDlg.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        // mDlg.setMessage("DB 작업 시작");
        // mDlg.show();

        super.onPreExecute();
    }

    @Override
    protected Integer doInBackground(Integer... code)
    {
        if (isCancelled())
            onPostExecute(null);
        switch (code[0])
        {
            case DB_ROAD:
                roadDB();
                break;
            case DB_TABLE_CREATE:
                createTable();
                break;
            case DB_SELECT:
                selectDB();
                break;
            case DB_INSERT:
                selectAllTable();
                break;
            case DB_DELETE:
                deleteRow();
                break;
            case DB_ARRAY_INSERT:
                createArrayTable();
                break;
            case DB_UPDATE:
                updateTable();
                break;
            case DB_TABLE_CHECK:
                kog.e("Jonathan", "123 Table_Check 하나?");
                checkNeedTable();
                break;
            case DB_DROP_TABLES:
                dropTables();
                break;
            case DB_DELETE_ALL_ROW:
                deleteAllRow();
                break;
            default:
                break;
        }
        if (isCancelled())
            onPostExecute(null);
        // adapter[0].executeQuery(strQuery)
        return code[0];
    }

    // onProgressUpdate() 함수는 publishProgress() 함수로 넘겨준 데이터들을 받아옴
    @Override
    protected void onProgressUpdate(String... progress)
    {
        // if (progress[0].equals("progress")) {
        // mDlg.setProgress(Integer.parseInt(progress[1]));
        // mDlg.setMessage(progress[2]);
        // } else if (progress[0].equals("max")) {
        // mDlg.setMax(Integer.parseInt(progress[1]));
        // }
    }

    @Override
    protected void onPostExecute(Integer result)
    {
        // if(mDlg.isShowing())
        // mDlg.dismiss();

        kog.e("KDH", "-여기서 이어플의 모든일이 일어난다-");
        kog.e("KDH", "mFunName = " + mFunName);
        kog.e("KDH", "result= " + result);

        if (mTableNameMap != null && mTableNameMap.size() > 0)
        {
            kog.e("Jonathan", "checking Complete mTableNameMap :: " + mTableNameMap.toString());
            printMap(mTableNameMap);
        }

        if (isCancelled())
        {
            kog.e("Jonathan", "checking Complete isCancled()");
            super.onPostExecute(null);
            return;
        }
        if (result == DB_ROAD)
        {
            kog.e("Jonathan", "checking Complete result ==  DB_ROAD");
            CommonUtil.deleteFile(mDownLoadDbPath);
        }
        else if (result == DB_ARRAY_INSERT)
        {
            kog.e("Jonathan", "checking Complete result ==  DB_ARRAY_INSERT");
            if (mFunName != null)
            {
                if (mFunName.equals(ConnectController.REPAIR_FUNTION_NAME))
                {
                    SharedPreferencesUtil sharedPreferencesUtil = SharedPreferencesUtil.getInstance();
                    sharedPreferencesUtil.setSyncSuccess(true);
                }
            }
        }
        if (mAsyncResLintener != null)
        {

                String tableName = null;
                if (mTableModel != null)
                {
                    tableName = mTableModel.getTableName();
                }
            try {
                // 2014-01-16 만든 데이터 여기서 뽑아서 배출.
                kog.e("Jonathan", "checking Complete tableName == " + tableName);
                kog.e("Jonathan", "checking Complete mFunName == " + mFunName);
                kog.e("Jonathan", "checking Complete result == " + result);
                kog.e("Jonathan", "checking Complete mCursor == " + mCursor);
                // 이게 무슨 소스냐 도대체.. 모든 케이스에 같은 함수 호출이라니.. 어쩌라는거임
                if(mCursor != null && !mCursor.isClosed() && mCursor.getCount() > 0){
                    try {
                        if(mCursor.moveToFirst()){
                            mAsyncResLintener.onCompleteDB(mFunName, result, mCursor, tableName);
                        }
                    } catch (Exception e){
                        e.printStackTrace();
                        mAsyncResLintener.onCompleteDB(mFunName, result, mCursor, tableName);
                    }
                } else {
                    mAsyncResLintener.onCompleteDB(mFunName, result, mCursor, tableName);
                }
            } catch (Exception e){
                e.printStackTrace();
                mAsyncResLintener.onCompleteDB(mFunName, result, mCursor, tableName);
            }


        }
        super.onPostExecute(result);
    }

    @Override
    protected void onCancelled()
    {
        // TODO Auto-generated method stub
        super.onCancelled();
    }

    @Override
    public void onPublishUdapte(String type, int progressCount)
    {
        // TODO Auto-generated method stub
        publishProgress(type, Integer.toString(progressCount), "작업 번호 " + String.valueOf(progressCount + 1) + "번 수행중");
    }

    private void roadDB()
    {
        selectAllTable();
    }

    private void insertTable(String tableName, ArrayList<HashMap<String, String>> aTableMap)
    {
        SqlLiteAdapter sqlLiteAdapter = SqlLiteAdapter.getInstance();
        // sqlLiteAdapter.insertTableContentValues(mTableModel.getTableName(),
        // mTableModel.getTableArray());
        sqlLiteAdapter.insertTableContentValues(tableName, aTableMap);
    }

    private void createTable()
    {
        kog.e("Jonathan", "insertTable :: ");
        SqlLiteAdapter sqlLiteAdapter = SqlLiteAdapter.getInstance();
        if (mTableModel.getTableArray() != null && mTableModel.getTableArray().size() > 0)
        {
            ArrayList<String> reColumNameArr = convertColumNameArray(mTableModel.getTableArray().get(0));
            sqlLiteAdapter.createTable(mTableModel.getTableName(), reColumNameArr);
            insertTable(mTableModel.getTableName(), mTableModel.getTableArray());
        }
        for (int i = 0; i < DEFINE.SAVE_DATABASE_STRUCT.length; i++)
        {
            HashMap<String, String> struct = mTableModel.getStruct(DEFINE.SAVE_DATABASE_STRUCT[i]);
            // 저장할 stuct가 있을시에 저장한다.
            // 저장할 stuct 테이블 이름과 stuct 배열이 있어야 한다.
            if (struct != null)
            {
                ArrayList<String> reColumNameArr = convertColumNameArray(struct);
                sqlLiteAdapter.createTable(DEFINE.SAVE_DATABASE_STRUCT[i], reColumNameArr);
                ArrayList<HashMap<String, String>> tableMap = new ArrayList<HashMap<String, String>>();
                tableMap.add(struct);
                insertTable(DEFINE.SAVE_DATABASE_STRUCT[i], tableMap);
            }
        }
    }

    private void createArrayTable()
    {
        kog.e("Jonathan", "여기들어오니? 이재욱 createArrayTable :: ");
        SqlLiteAdapter sqlLiteAdapter = SqlLiteAdapter.getInstance();
        Iterator<String> itMap = mTableModel.mTableArrayMap.keySet().iterator();
        ArrayList<HashMap<String, String>> tableArray;
        while (itMap.hasNext())
        {
            String strKey = itMap.next();
            kog.e("Jonathan", "이재욱 strkey :: " + strKey);
            tableArray = mTableModel.getTableArray(strKey);
            String createTableName = mTableNameMap.get(strKey);
            if (tableArray.size() > 0 && createTableName != null)
            {
                kog.e("Jonathan", "이재욱 들어와?? ");
                ArrayList<String> reColumNameArr = convertColumNameArray(tableArray.get(0));
                sqlLiteAdapter.createTable(createTableName, reColumNameArr);
                insertTable(createTableName, tableArray);
            }
        }
        kog.e("Jonathan", "이재욱 :: " + DEFINE.SAVE_DATABASE_STRUCT.length);
        for (int i = 0; i < DEFINE.SAVE_DATABASE_STRUCT.length; i++)
        {
            HashMap<String, String> struct = mTableModel.getStruct(DEFINE.SAVE_DATABASE_STRUCT[i]);

            // Set <String> set = struct.keySet();
            // Iterator <String> it = set.iterator();
            // String key;
            //
            // while(it.hasNext())
            // {
            // key = it.next();
            // kog.e("Jonathan", "이재욱  key ===  " + key + "    value  === " + struct.get(key));
            // }

            // 저장할 stuct가 있을시에 저장한다.
            // 저장할 stuct 테이블 이름과 stuct 배열이 있어야 한다.
            if (struct != null)
            {
                ArrayList<String> reColumNameArr = convertColumNameArray(struct);
                kog.e("Joanthan", "이재욱 reColumNameArr :: " + reColumNameArr.get(i).toString());
                sqlLiteAdapter.createTable(DEFINE.SAVE_DATABASE_STRUCT[i], reColumNameArr);
                ArrayList<HashMap<String, String>> tableMap = new ArrayList<HashMap<String, String>>();
                tableMap.add(struct);
                insertTable(DEFINE.SAVE_DATABASE_STRUCT[i], tableMap);
            }
        }
    }

    private HashMap<Integer, Serializable> selectAllTable()
    {
        HashMap<Integer, Serializable> reMap = null;
        SqlLiteAdapter sqlLiteAdapter = SqlLiteAdapter.getInstance();
        ArrayList<String> tableNames = sqlLiteAdapter.getTableNames(mDownLoadDbPath);
        for (int i = 0; i < tableNames.size(); i++)
        {
            reMap = sqlLiteAdapter.selectAllTableQuery(tableNames.get(i), this, mDownLoadDbPath);
        }
        return reMap;
    }

    private ArrayList<String> convertColumNameArray(HashMap<String, String> aTableMap)
    {
        ArrayList<String> reColumNameArr = new ArrayList<String>();
        // HashMap<String, String> map = aTableArray.get(0);
        Iterator<String> iterator = aTableMap.keySet().iterator();
        while (iterator.hasNext())
        {
            String key = (String) iterator.next();
            reColumNameArr.add(key);
        }
        return reColumNameArr;
    }

    private void selectDB()
    {
        if (mDbQueryModel != null)
        {
            SqlLiteAdapter sqlLiteAdapter = SqlLiteAdapter.getInstance();
            mCursor = sqlLiteAdapter.selectDB(mDbQueryModel);
        }
    }

    // 14.06.10 Jonathan 찾음.
    private void updateTable()
    {
        SqlLiteAdapter sqlLiteAdapter = SqlLiteAdapter.getInstance();
        boolean flag = sqlLiteAdapter.updateRow(mTableName, mWhereKey, mContentValues);
        PrintLog.Print("", "updateTable" + flag);
    }

    private void deleteRow()
    {
        SqlLiteAdapter sqlLiteAdapter = SqlLiteAdapter.getInstance();
        boolean flag = sqlLiteAdapter.deleteRow(mTableName, mWhereKey, mContentValues);
        PrintLog.Print("", "deleteRow" + flag);
    }

    private void deleteAllRow(){
        SqlLiteAdapter sqlLiteAdapter = SqlLiteAdapter.getInstance();
        boolean flag = sqlLiteAdapter.deleteAllRow(mTableName);
        PrintLog.Print("", "deleteRow" + flag);
    }

    private void checkNeedTable()
    {
        SqlLiteAdapter sqlLiteAdapter = SqlLiteAdapter.getInstance();
        String message = sqlLiteAdapter.checkNeedTable();
        if (mTableModel != null)
        {
            mTableModel.setTableName(message);
        }
    }

    private void dropTables()
    {
        SqlLiteAdapter sqlLiteAdapter = SqlLiteAdapter.getInstance();
        for (String tableName : mDropTables)
        {
            kog.e("KDH", "tableName = " + tableName);
            sqlLiteAdapter.dropTable(tableName);
        }
    }
}
