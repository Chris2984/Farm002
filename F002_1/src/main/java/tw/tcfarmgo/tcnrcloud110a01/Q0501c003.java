package tw.tcfarmgo.tcnrcloud110a01;

import static android.content.ContentValues.TAG;
import static android.content.DialogInterface.BUTTON_NEGATIVE;
import static android.content.DialogInterface.BUTTON_POSITIVE;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.method.ScrollingMovementMethod;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.Scope;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

public class Q0501c003 extends AppCompatActivity implements View.OnClickListener {

    private Intent intent = new Intent();
    private Intent intent03=new Intent();
    private Intent intent23=new Intent();

    private EditText e101,e102,e103,e104,e111;
    private Button b101,b102,b103,b104,b105,b106,b107,b108,b109;

    private Q0501DBhlper dbHelper;
    private static final String DB_File = "FarmTravel.db";
    private static final int DBversion = 1;

    // ------------------
    public static String myselection = "";
    public static String myorder = "id ASC"; // 排序欄位
    public static String myargs[] = new String[]{};

    private ArrayList<String> recSet;
    private int index=0;
    private String msg;
    private ListView lv001;
    private TextView tsub,t112,t114,t115;
    private RelativeLayout rl01;
    private LinearLayout ll32;
    private String e_id,e_name,e_tel,e_text1,e_text2;
    private int old_index;
    private String sqlctl;
    private int rowsAffected;
    private int servermsgcolor;
    private String ser_msg;
    private Spinner s003;
    private Intent intent032=new Intent();
    private String stHead;
    private Boolean account_state;
    private String g_Email;
    private String e_email;
    private int tcount;
    private TextView t111;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        enableStrictMode(this);//use 使用暫存堆疊必須加此條件
        super.onCreate(savedInstanceState);
        setContentView(R.layout.q0501_c001);
        //----繼承主page標題---
        Intent intent03=getIntent();
        setTitle(intent03.getStringExtra("class_title"));

        Intent intent023=getIntent();
        setTitle(intent023.getStringExtra("class_title"));

        Intent intent53=getIntent();
        setTitle(intent53.getStringExtra("class_title"));


        setupViewComponent();
    }


    private void setupViewComponent() {

        //--editview,按鍵對照
        e101=(EditText)findViewById(R.id.q0501_e101);//名稱
        e102=(EditText)findViewById(R.id.q0501_e102);//電話
        e103=(EditText)findViewById(R.id.q0501_e103);//備註1
        e104=(EditText)findViewById(R.id.q0501_e104);//備註2
        e111=(EditText)findViewById(R.id.q0501_e111);//ID
        t111=(TextView)findViewById(R.id.q0501_t111);
        lv001 = (ListView) findViewById(R.id.q0501_lv001);//in the page of searching, as listview
        tsub = (TextView) findViewById(R.id.q0501_c001_subtitle);//in the page of searching, as subtitle
        t112 = (TextView) findViewById(R.id.q0501_t112); //textView, "total"
        t114 = (TextView)findViewById(R.id.q0501_t114); //server message, if connect well...
        t115 = (TextView)findViewById(R.id.q0501_t115); //title,
        rl01 = (RelativeLayout) findViewById(R.id.q0501_rl01); //the page of tying wonderlist
        ll32 = (LinearLayout) findViewById(R.id.q0501_ll32);//the page of searching

        b101=(Button)findViewById(R.id.q0501_b101);//新增
        b102=(Button)findViewById(R.id.q0501_b102);//取消
        b103=(Button)findViewById(R.id.q0501_b103);//查詢
        b104=(Button)findViewById(R.id.q0501_b104);//首筆
        b105=(Button)findViewById(R.id.q0501_b105);//上一筆
        b106=(Button)findViewById(R.id.q0501_b106);//下一筆
        b107=(Button)findViewById(R.id.q0501_b107);//尾筆
        b108=(Button)findViewById(R.id.q0501_b108);//更新
        b109=(Button)findViewById(R.id.q0501_b109);//刪除

        b101.setOnClickListener(this);
        b102.setOnClickListener(this);
        b103.setOnClickListener(this);
        b104.setOnClickListener(this);
        b105.setOnClickListener(this);
        b106.setOnClickListener(this);
        b107.setOnClickListener(this);
        b108.setOnClickListener(this);
        b109.setOnClickListener(this);

        //spinner和clicklistener
        s003 = (Spinner)findViewById(R.id.q0501_spinner);
        s003.setOnItemSelectedListener(mSpnNameOnItemSelLis);
        s003.setVisibility(View.VISIBLE);//一定要放在這裡才不會當掉

        //----------
        e103.setMovementMethod(ScrollingMovementMethod.getInstance());//設定說明欄可以捲動
        e103.scrollTo(0,0);//出現的textview，回說明欄頂端(左上角)
        e104.setMovementMethod(ScrollingMovementMethod.getInstance());//設定說明欄可以捲動
        e104.scrollTo(0,0);//出現的textview，回說明欄頂端(左上角)

        // 動態調整高度 抓取使用裝置自身的尺寸
        DisplayMetrics displayMetrics = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int buttonwidth = displayMetrics.widthPixels /2;
        int buttonwidth2 = displayMetrics.widthPixels /4;
        int buttonwidth3 = displayMetrics.widthPixels /5;
        b108.getLayoutParams().width=buttonwidth2;
        b109.getLayoutParams().width=buttonwidth2;

        b104.getLayoutParams().width=buttonwidth3;
        b105.getLayoutParams().width=buttonwidth3;
        b106.getLayoutParams().width=buttonwidth3;
        b107.getLayoutParams().width=buttonwidth3;
        //--

        u_layout_def3();// the first page layout appearance
        initDB();
        showRec(index);

        //系統的ID欄位
        e111.setTextColor(ContextCompat.getColor(this, R.color.Red));
        e111.setBackgroundColor(ContextCompat.getColor(this, R.color.yellow_softness));

        //--text1, text2 clean
        e103.setText("");
        e104.setText("");

        //tvtitle. 初始顯示 (你在第幾/共計筆)
        t115.setTextColor(ContextCompat.getColor(this, R.color.Teal));
        stHead = "顯示資料：第" + (index + 1) + " / " + tcount + " 筆";
        t115.setText(stHead);

        u_setspinner();
        rl01.setVisibility(View.VISIBLE);
        ll32.setVisibility(View.INVISIBLE);

    }

    private void u_setspinner() {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item);
        for (int i = 0; i < recSet.size(); i++) {
            String[] fld = recSet.get(i).split("#");
            // adapter.add(fld[0] + " " + fld[1] + " " + fld[2]+ " " + fld[3]);
            adapter.add(fld[0] + " " + fld[1] + " " + fld[2] + " " + fld[3]+ " " + fld[4]);
        }

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        s003.setAdapter(adapter);
        s003.setOnItemSelectedListener(mSpnNameOnItemSelLis);
        System.out.println("usetspinerrrrrr");

    }

    private void initDB() {
        if (dbHelper == null) {
            dbHelper = new Q0501DBhlper(this, DB_File, null, DBversion);//可自動生成
        }
        //dbmysql();//如果新安裝時，這樣避免crash
        //e_email=g_Email;
        recSet = dbHelper.getRecSet_Q0501();
        Log.d(TAG, "Q0501c003_initDB()//"+e_email+"//");
        //recSet = dbHelper.getRecSet_Q0501c003();
    }

    private void u_layout_def3() {
        b101.setVisibility(View.INVISIBLE);//新增
        b102.setVisibility(View.INVISIBLE);//取消
        b103.setVisibility(View.INVISIBLE);
        b108.setVisibility(View.VISIBLE);//更新
        b109.setVisibility(View.VISIBLE);//刪除
        b104.setVisibility(View.VISIBLE);
        b105.setVisibility(View.VISIBLE);
        b106.setVisibility(View.VISIBLE);
        b107.setVisibility(View.VISIBLE);
        t112.setVisibility(View.VISIBLE);
        t111.setVisibility(View.VISIBLE);
//        s003.setVisibility(View.VISIBLE); //spinner--這裡會當掉
        t115.setVisibility(View.VISIBLE); //頭條

        e111.setEnabled(false); //ID欄位鎖住
        e101.setEnabled(false); //名字鎖住
        e102.setEnabled(false); //電話鎖住

    }

    private void showRec(int index) {
        msg = "";
        if (recSet.size() != 0) {
            String stHead = "顯示資料：第 " + (index + 1) + " 筆 / 共 " + recSet.size() + " 筆";
            msg = getString(R.string.q0501_t112) + recSet.size() + "筆";
            t115.setBackgroundColor(ContextCompat.getColor(this, R.color.yellow_softness));
            t115.setTextColor(ContextCompat.getColor(this, R.color.Teal));
            t115.setText(stHead);

            String[] fld = recSet.get(index).split("#");
            e111.setTextColor(ContextCompat.getColor(this, R.color.Red));
            e111.setBackgroundColor(ContextCompat.getColor(this, R.color.Yellow));
            e111.setText(fld[0]);//ID
            e101.setText(fld[1]);//名稱
            e102.setText(fld[2]);//電話
            e103.setText(fld[3]);//備註1
            e104.setText(fld[4]);//備註2
            s003.setSelection(index, true); //spinner 小窗跳到第幾筆
//            s003.setSelection(index, true); //spinner 小窗跳到第幾筆--放這裡當掉
//            e101.setText(keep01);  放這裡沒用
//            e102.setText(keep02);

        } else {
            String stHead = "顯示資料：0 筆";
            msg = getString(R.string.q0501_ncount) + "0筆";
            t115.setText(stHead);
            e111.setText("");//ID
            e101.setText("");//名稱
            e102.setText("");//電話
            e103.setText("");//備註1
            e104.setText("");//備註2

        }
        System.out.println("ShowIndexxxxxxxxxxxxxxxxxxxxxxxxxxx");
        t112.setText(msg);

    }


    private AdapterView.OnItemSelectedListener mSpnNameOnItemSelLis = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            int iSelect = s003.getSelectedItemPosition();//找到按哪個
            String[] fld = recSet.get(iSelect).split("#");
            String s = "資料:共" + recSet.size() + "筆," + "你按下第" + String.valueOf(iSelect + 1) + "項";//起始為0
            t115.setText(s);
//--------------12
            index = position;
            System.out.println("AdapterView.OnItemSelectedListener............");
            showRec(index);
            iSelect = index;
//-----------12


            if (b101.getVisibility() == View.VISIBLE) {
                e111.setHint("請繼續輸入");
                e101.setText("");
                e102.setText("");
                e103.setText("");
                e104.setText("");
            }

        }

        @Override
        public void onNothingSelected(AdapterView<?> arg0) {  //parent,
            // TODO Auto-generated method stub
//            e101.setText(keep01);
//            e102.setText(keep02); 放這裡沒用
            e101.setText("");
            e102.setText("");
            e103.setText("");
            e104.setText("");
        }
    };

    private void enableStrictMode(Context context) {

        //-------------抓取遠端資料庫設定執行續------------------------------
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().
                detectDiskReads().
                detectDiskWrites().
                detectNetwork().
                penaltyLog().
                build());
        StrictMode.setVmPolicy(
                new
                        StrictMode.
                                VmPolicy.
                                Builder().
                        detectLeakedSqlLiteObjects().
                        penaltyLog().
                        penaltyDeath().
                        build());


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.q0501_b104://首筆
                if(account_state) {
                    ctlFirst();
                }else {
                    Toast.makeText(getApplicationContext(),getString(R.string.q0501_hint03),Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.q0501_b105://上一筆
                if(account_state){
                    ctlPrev();
                }else {
                    Toast.makeText(getApplicationContext(),getString(R.string.q0501_hint03),Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.q0501_b106://下一筆
                if(account_state){
                    ctlNext();
                }else {
                    Toast.makeText(getApplicationContext(),getString(R.string.q0501_hint03),Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.q0501_b107://尾筆
                if(account_state){
                    ctlLast();
                }else {
                    Toast.makeText(getApplicationContext(),getString(R.string.q0501_hint03),Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.q0501_b108://更新
                if(account_state){
                    initDB();//避免跑出去又回來時crash
                    // 資料更新
                    e_id = e111.getText().toString().trim();//ID
                    e_name = e101.getText().toString().trim();//名稱
                    e_tel = e102.getText().toString().trim();//電話
                    e_text1 = e103.getText().toString().trim();//備註1
                    e_text2 = e104.getText().toString().trim();//備註2
                    e_email=g_Email;
                    Log.d(TAG, "Q0501c003_b108//"+e_email+"//");
                    // 資料更新
                    old_index=index;
//                  old_id =b_id; 舊的ID持續使用，後來才容易找
                    mysql_update(); // 執行MySQL更新
                    dbmysql();
                    //-------------------------------------
                    recSet = dbHelper.getRecSet_Q0501();
                    u_setspinner();
//------------------------
//                用 old_id 搜尋 index

//--------------------------------
                    index=old_index;   //這是陽春型
                    showRec(index);
                    msg = "第 " + (index + 1) + " 筆記錄  已修改 ! " ;
                    Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
                    ctlLast();
                }else {
                    Toast.makeText(getApplicationContext(),getString(R.string.q0501_hint03),Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.q0501_b109://刪除一筆
                if(account_state){
                    Q0501_MyAlertDialog aldDial_c003 = new Q0501_MyAlertDialog(Q0501c003.this);
                    aldDial_c003.getWindow().setBackgroundDrawableResource(R.color.test_background);
                    aldDial_c003.setTitle("刪除資料");
                    aldDial_c003.setMessage("資料刪除無法復原\n確定刪除這筆資料嗎?");
                    aldDial_c003.setCancelable(false);
                    aldDial_c003.setIcon(android.R.drawable.ic_delete);
                    aldDial_c003.setButton(BUTTON_POSITIVE, "確定刪除", aldBtListener);
                    aldDial_c003.setButton(BUTTON_NEGATIVE, "取消刪除", aldBtListener);
                    aldDial_c003.show();
                }else {
                    Toast.makeText(getApplicationContext(),getString(R.string.q0501_hint03),Toast.LENGTH_SHORT).show();
                }
                break;
        }

    }

    //---------刪除1筆的clicklistener
    private DialogInterface.OnClickListener aldBtListener = new DialogInterface.OnClickListener() {

        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which) {
                case BUTTON_POSITIVE:
                    initDB();//避免跑出去又回來時crash
                    old_index=index;
                    // ---------------------------
                    mysql_del();// 執行MySQL刪除
                    dbmysql();//下載買SQL
                    // ---------------------------
                    index=old_index;
                    u_setspinner();
                    if (index == dbHelper.RecCount_Q0501()) {
                        index--;
                        System.out.println("index---------------------");
                    }

//--                    if (index >= recSet.size()-1) {
//                        index=recSet.size()-1;
//                    }
                    showRec(index);
                    msg = "資料已刪除" ;
                    Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
                break;

                case BUTTON_NEGATIVE:
                    msg = "放棄刪除資料 !";
                    Toast.makeText(Q0501c003.this, msg, Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    private void mysql_del() {
        //---------
        String de_e111 = e111.getText().toString().trim();
        ArrayList<String> nameValuePairs = new ArrayList<>();
//        nameValuePairs.add(sqlctl);
        nameValuePairs.add(de_e111);
        try {
            Thread.sleep(100); //  延遲Thread 睡眠0.5秒
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
//-----------------------------------------------
        String result = Q0501DBConnector.executeDelet_Q0501(nameValuePairs);   //執行刪除
//-----------------------------------------------
        Log.d(TAG, "Delete result:" + result);
    }

    private void dbmysql() {
        sqlctl = "SELECT * FROM farm02 WHERE email='"+ g_Email +"' ORDER BY id ASC";
        ArrayList<String> nameValuePairs = new ArrayList<>();
        nameValuePairs.add(sqlctl);
        try {
            String result = Q0501DBConnector.executeQuery_Q0501(nameValuePairs);
            System.out.println("After delete?"+result);
            /**************************************************************************
             * SQL 結果有多筆資料時使用JSONArray
             * 只有一筆資料時直接建立JSONObject物件 JSONObject
             * jsonData = new JSONObject(result);
             **************************************************************************/
            //            String result = DBConnector.executeQuery("SELECT * FROM member");
            if (result.length() >8) {//php找不到資料會回傳"0 results",所以以長度8來判斷有沒有找到資料
                //這裡是"0 results">8，代表有資料
                //==========================================
                chk_httpstate();  //檢查 連結狀態
                //==========================================
                JSONArray jsonArray = new JSONArray(result);
                // -------------------------------------------------------
                if (jsonArray.length() > 0) { // MySQL 連結成功有資料
                    int rowsAffected = dbHelper.clearRec_Q0501();                 // 匯入前,刪除所有SQLite資料
                    // 處理JASON 傳回來的每筆資料
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonData = jsonArray.getJSONObject(i);
                        ContentValues newRow = new ContentValues();
                        // --(1) 自動取的欄位 --取出 jsonObject 每個欄位("key","value")-----------------------
                        Iterator itt = jsonData.keys();
                        while (itt.hasNext()) {
                            String key = itt.next().toString();
                            String value = jsonData.getString(key); // 取出欄位的值
                            if (value == null) {
                                continue;
                            } else if ("".equals(value.trim())) {
                                continue;
                            } else {
                                jsonData.put(key, value.trim());
                            }
                            // ------------------------------------------------------------------
                            newRow.put(key, value.toString()); // 動態找出有幾個欄位
                            // -------------------------------------------------------------------
                        }
                        // ---(2) 使用固定已知欄位---------------------------
                        // newRow.put("id", jsonData.getString("id").toString());
                        // newRow.put("name",
                        // jsonData.getString("name").toString());
                        // newRow.put("grp", jsonData.getString("grp").toString());
                        // newRow.put("address", jsonData.getString("address")
                        // -------------------加入SQLite---------------------------------------
                        long rowID = dbHelper.insertRec_m_Q0501(newRow);

                    }
                    Toast.makeText(getApplicationContext(), "共匯入 " + Integer.toString(jsonArray.length()) + " 筆資料", Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "匯入");
                    // ---------------------------
                } else {

                }
            }else{
                Toast.makeText(getApplicationContext(), "主機資料庫無資料", Toast.LENGTH_LONG).show();

            }
            recSet = dbHelper.getRecSet_Q0501();  //重新載入SQLite
            u_setspinner();

        } catch (Exception e) {
            //發現mysql中沒有資料，沒辦法轉成json
            int rowsafterdel = dbHelper.clearRec_Q0501();
            recSet = dbHelper.getRecSet_Q0501();  //重新載入SQLite
            Log.d(TAG, e.toString()+"CCCCCCCCCCCCCCC");
        }
    }

    private void chk_httpstate() {
//**************************************************
//*       檢查連線狀況
//**************************************************
        //存取類別成員 DBConnector01.httpstate 判定是否回應 200(連線要求成功)
        servermsgcolor = ContextCompat.getColor(this, R.color.Navy);

        if (Q0501DBConnector.httpstate == 200) {
            ser_msg = "伺服器匯入資料(code:" + Q0501DBConnector.httpstate + ") ";
            servermsgcolor = ContextCompat.getColor(this, R.color.Navy);
//                Toast.makeText(getBaseContext(), "由伺服器匯入資料 ",
//                        Toast.LENGTH_SHORT).show();
        } else {
            int checkcode = Q0501DBConnector.httpstate / 100;
            switch (checkcode) {
                case 1:
                    ser_msg = "資訊回應(code:" + Q0501DBConnector.httpstate + ") ";
                    servermsgcolor = ContextCompat.getColor(this, R.color.Red);
                    break;
                case 2:
                    ser_msg = "已經完成由伺服器會入資料(code:" + Q0501DBConnector.httpstate + ") ";
                    servermsgcolor = ContextCompat.getColor(this, R.color.Red);
                    break;
                case 3:
                    ser_msg = "伺服器重定向訊息，請稍後在試(code:" + Q0501DBConnector.httpstate + ") ";
                    servermsgcolor = ContextCompat.getColor(this, R.color.Red);
                    break;
                case 4:
                    ser_msg = "用戶端錯誤回應，請稍後在試(code:" + Q0501DBConnector.httpstate + ") ";
                    servermsgcolor = ContextCompat.getColor(this, R.color.Red);
                    break;
                case 5:
                    ser_msg = "伺服器error responses，請稍後在試(code:" + Q0501DBConnector.httpstate + ") ";
                    servermsgcolor = ContextCompat.getColor(this, R.color.Red);
                    break;
            }
//                Toast.makeText(getBaseContext(), msg, Toast.LENGTH_SHORT).show();
        }

        if (Q0501DBConnector.httpstate == 0) {
            ser_msg = "遠端資料庫異常(code:" + Q0501DBConnector.httpstate + ") ";
            servermsgcolor = ContextCompat.getColor(this, R.color.Red);
        }
        t114.setText(ser_msg);
        t114.setTextColor(servermsgcolor);
        //    servermsgcolor = ContextCompat.getColor(this, R.color.Red);
        //-------------------------------------------------------------------
    }

    private void mysql_update() {
        e_id = e111.getText().toString().trim();//ID
        e_name = e101.getText().toString().trim();//名稱
        e_tel = e102.getText().toString().trim();//電話
        e_text1 = e103.getText().toString().trim();//備註1
        e_text2 = e104.getText().toString().trim();//備註2

        ArrayList<String> nameValuePairs = new ArrayList<>();
        nameValuePairs.add(e_id);
        nameValuePairs.add(e_name);
        nameValuePairs.add(e_tel);
        nameValuePairs.add(e_text1);
        nameValuePairs.add(e_text2);

        try {
            Thread.sleep(100); //  延遲Thread 睡眠0.5秒
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
//-----------------------------------------------
        String result = Q0501DBConnector.executeUpdate_Q0501(nameValuePairs);
//-----------------------------------------------
    }
    //---------------
    private void ctlFirst() {
        //no.1筆
        index = 0;
        showRec(index);
    }

    private void ctlNext() {
        //下一筆
        index++;
        if (index >= recSet.size())
            index = 0;
        showRec(index);
    }

    private void ctlPrev() {
        //上一筆
        index--;
        if (index < 0)
            index = recSet.size() - 1;
        showRec(index);
    }

    private void ctlLast() {
        //最後一筆 origin recSet.size()-1
        index = recSet.size()-1 ;
        showRec(index);
    }
//---------------------------------


    //------生命週期-------
    @Override
    protected void onStop() {
        super.onStop();
        if (dbHelper != null) {
            dbHelper.close();
            dbHelper = null;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    @Override
    public void onBackPressed() {
       // super.onBackPressed();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //initDB();//避免跑出去又回來時crash

    }
    @Override
    protected void onStart() {
        super.onStart();
        // Check for existing Google Sign In account, if the user is already signed in
        // the GoogleSignInAccount will be non-null.
        //account = GoogleSignIn.getLastSignedInAccount(this);

        //account_state = account != null; //true false
        Q0501_CheckUserState cu= new Q0501_CheckUserState(this); //建構
        account_state = Q0501_CheckUserState.getAccount_state();
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        if (account != null && GoogleSignIn.hasPermissions(account, new Scope(Scopes.DRIVE_APPFOLDER))) {
            updateUI(account);
        } else {
            updateUI(null);
        }
    }

    //---------------------------------
    private void updateUI(GoogleSignInAccount account) {
        GoogleSignInAccount aa = account;
        int aaa=1;
        if (account != null) {
            g_Email=account.getEmail();  //信箱
            String g_GivenName=account.getGivenName(); //Firstname
            Log.d(TAG, "Q0501c003start"+g_Email+"//"+g_GivenName);
        }
    }
    //------------------------------------------Menu--------------------------------

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.q0501_menu_sub ,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.q0501_login_action_settings:
                this.finish();
                break;


            case R.id.q0501_msub_r001://查詢鈕  with 列表
                intent032.putExtra("class_title",getString(R.string.q0501_t901));
                intent032.setClass(Q0501c003.this, Q0501c002.class);
                startActivity(intent032);
                this.finish();

                break;

            case R.id.q0501_msub_u001://更新刪除
                Toast.makeText(getApplicationContext(), "就在這裡更新喔~", Toast.LENGTH_SHORT).show();
                break;


        }
        return super.onOptionsItemSelected(item);
    }
}