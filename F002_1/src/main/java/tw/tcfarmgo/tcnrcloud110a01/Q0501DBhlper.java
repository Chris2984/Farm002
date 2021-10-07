package tw.tcfarmgo.tcnrcloud110a01;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

public class Q0501DBhlper extends SQLiteOpenHelper {

    private static final String DB_File = "FarmTravel.db";
    private static final String DB_TABLE_Q0501 = "farm001";
    public static final int VERSION = 1;
    private static SQLiteDatabase database;
    public String sCreateTableCommand; //資料庫名稱
    String TAG="chris=>";
    private String Creat_Table_Q0501 = "CREATE TABLE " + DB_TABLE_Q0501 + " ( "
            + "id INTEGER PRIMARY KEY," + "name TEXT NOT NULL," + "tel TEXT,"
            + "text1 TEXT,"
            + "text2 TEXT,"
            + "email TEXT) ;" ;
    private String g_Email;


    //----------------------------------------------
    // 需要資料庫的元件呼叫這個方法，這個方法在一般的應用都不需要修改
    public static SQLiteDatabase getDatabase(Context context) {
        if (database == null || !database.isOpen()) {
            database = new Q0501DBhlper(context, DB_File, null, VERSION).getWritableDatabase();
        }
        return database;
    }

    public Q0501DBhlper(Context context,
                        String name,
                        SQLiteDatabase.CursorFactory factory,
                        int version) {
        //本來是super(context, name, factory, version);
        super(context, DB_File, null, 1);
        Log.d(TAG, "Q0501DBhlper()");
        sCreateTableCommand="";
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
//        if (sCreateTableCommand.isEmpty()){
//            return;
//        }

        db.execSQL(Creat_Table_Q0501);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d(TAG, "onUpgrade()");
        db.execSQL("DROP     TABLE     IF    EXISTS    " + DB_TABLE_Q0501);
        onCreate(db);

    }

    public int clearRec_Q0501() {
        SQLiteDatabase db = getWritableDatabase();
        String sql = "SELECT * FROM " + DB_TABLE_Q0501;
        Cursor recSet = db.rawQuery(sql, null);
        if (recSet.getCount() != 0) {
//			String whereClause = "id < 0";
            int rowsAffected = db.delete(DB_TABLE_Q0501, "1", null);
            // From the documentation of SQLiteDatabase delete method:
            // To remove all rows and get a count pass "1" as the whereClause.
            Log.d(TAG, "clearRec_Q0501()影響數字"+rowsAffected);
            recSet.close();
            db.close();
            return rowsAffected;

        } else {
            recSet.close();
            db.close();
            return -1;
        }

    }

    public long insertRec_m_Q0501(ContentValues rec) {
        SQLiteDatabase db = getWritableDatabase();
        long rowID = db.insert(DB_TABLE_Q0501, null, rec);
        db.close();
        return rowID;
    }

    public int RecCount_Q0501() {
        SQLiteDatabase db=getWritableDatabase();
        String sql="SELECT * FROM "+DB_TABLE_Q0501;
        Cursor recSet=db.rawQuery(sql,null);
        int count = recSet.getCount();
        recSet.close();
        return count;
    }

    public ArrayList<String> getRecSet_query_Q0501(String t_name, String t_tel, String t_text1, String t_text2) {
        SQLiteDatabase db = getReadableDatabase();

        String sql = "SELECT * FROM " + DB_TABLE_Q0501 +
                " WHERE name LIKE ? AND tel LIKE ? AND text1 LIKE ? AND text2 LIKE ? ORDER BY id ASC";
        String[] args = new String[]{"%" + t_name.toString() + "%",
                "%" + t_tel.toString() + "%",
                "%" + t_text1.toString() + "%",
                "%" + t_text2.toString() + "%"};
        Log.d(TAG, "getRecSet_query_Q0501-查詢//"+"//");
        Cursor recSet = db.rawQuery(sql, args);
        ArrayList<String> recAry = new ArrayList<String>();
        //----------------------------
        int columnCount = recSet.getColumnCount();
        while (recSet.moveToNext()) {
            String fldSet = "";
            for (int i = 1; i < columnCount; i=i+6)
                fldSet += recSet.getString(i) ;
            recAry.add(fldSet);
        }
        //------------------------
        recSet.close();
        db.close();
        return recAry;
    }

    public ArrayList<String> getRecSet_Q0501() {
        SQLiteDatabase db = getReadableDatabase();
        String sql = "SELECT * FROM " + DB_TABLE_Q0501;
        Cursor recSet = db.rawQuery(sql, null);
        ArrayList<String> recAry = new ArrayList<String>();
        //----------------------------
        int columnCount = recSet.getColumnCount();
        //recSet.moveToFirst();

        while (recSet.moveToNext()) {
            String fldSet = "";
            for (int i = 0; i < columnCount; i++)
                fldSet += recSet.getString(i) + "#";
            recAry.add(fldSet);
        }
        //------------------------
        recSet.close();
        db.close();
        Log.d(TAG, "getRecSet_Q0501(),影響數字"+columnCount);
        return recAry;
    }

    public ArrayList<String> getRecSet_query_Q0501c002n(String t_name) {
        SQLiteDatabase db = getReadableDatabase();

        String sql = "SELECT * FROM " + DB_TABLE_Q0501 +
                " WHERE name LIKE ?  ORDER BY id ASC";
        String[] args = new String[]{"%" + t_name.toString() + "%"};
        Log.d(TAG, "查詢清單的二次()"+t_name.toString());
        Cursor recSet = db.rawQuery(sql, args);
        ArrayList<String> recAry = new ArrayList<String>();
        //----------------------------
        int columnCount = recSet.getColumnCount();
        while (recSet.moveToNext()) {
            String fldSet = "";
            for (int i = 1; i < columnCount; i=i+5)
                fldSet += recSet.getString(i);
            recAry.add(fldSet);
        }
        //------------------------
        recSet.close();
        db.close();
        return recAry;

    }

    public ArrayList<String> getRecSet_query_Q0501c002t(String t_tel) {
        SQLiteDatabase db = getReadableDatabase();

        String sql = "SELECT * FROM " + DB_TABLE_Q0501 +
                " WHERE tel LIKE ? ORDER BY id ASC";
        String[] args = new String[]{"%" + t_tel.toString() + "%"};

        Cursor recSet = db.rawQuery(sql, args);
        ArrayList<String> recAry = new ArrayList<String>();
        //----------------------------
        int columnCount = recSet.getColumnCount();
        while (recSet.moveToNext()) {
            String fldSet = "";
            for (int i = 2; i < columnCount; i=i+4)
                fldSet += recSet.getString(i);
            recAry.add(fldSet);
        }
        //------------------------
        recSet.close();
        db.close();
        return recAry;
    }

    public ArrayList<String> getRecSet_query_Q0501c002q(String t_tname, String t_ttel, String t_ttext1, String t_ttext2, String e_temail) {
        SQLiteDatabase db = getReadableDatabase();

        String sql = "SELECT * FROM " + DB_TABLE_Q0501 +
                " WHERE name LIKE ? AND tel LIKE ? AND text1 LIKE ? AND text2 LIKE ? AND email='"+e_temail+"' ORDER BY id ASC";
        String[] args = new String[]{"%" + t_tname.toString() + "%",
                "%" + t_ttel.toString() + "%",
                "%" + t_ttext1.toString() + "%",
                "%" + t_ttext2.toString() + "%"};
        Cursor recSet = db.rawQuery(sql, args);
        ArrayList<String> recAry = new ArrayList<String>();
        //----------------------------
        int columnCount = recSet.getColumnCount();
        while (recSet.moveToNext()) {
            String fldSet = "";
            for (int i = 0; i < columnCount; i++)
                fldSet += recSet.getString(i) + "#";
            recAry.add(fldSet);
        }
        //------------------------
        recSet.close();
        db.close();
        return recAry;
    }

}
