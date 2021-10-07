package tw.tcfarmgo.tcnrcloud110a01;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.method.ScrollingMovementMethod;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
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

public class Q0501c001 extends AppCompatActivity implements View.OnClickListener {

   // private Intent intent = new Intent();
    private EditText e101,e102,e103,e104,e111;
    private Button b101,b102,b103,b104,b105,b106,b107,b108,b109;


    private Q0501DBhlper dbHelper;
    private static final String DB_File = "FarmTravel.db";
    private static final int DBversion = 1;
    private int tcount;

    // ------------------
    public static String myselection = "";
    public static String myorder = "id ASC"; // 排序欄位
    public static String myargs[] = new String[]{};

    private ArrayList<String> recSet;
    private int index = 0;
    private String msg;
    private ListView lv001;
    private TextView tsub,t112,t114;
    private RelativeLayout rl01;
    private LinearLayout ll32;
    private String e_id,e_name,e_tel,e_text1,e_text2;
    private int old_index;
    private String sqlctl;
    private int rowsAffected;
    private int servermsgcolor;
    private String ser_msg;
    private String keep01,keep02;
    private Intent intent02=new Intent();
    private Intent intent03=new Intent();
    private Spinner s001;
    private Boolean account_state;
    private GoogleSignInAccount account;
    private String g_Email;
    private String e_email;
    String TAG="chris=>";
    private TextView t111;
    private Boolean flag_per_call;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        enableStrictMode(this);//use 使用暫存堆疊必須加此條件
        super.onCreate(savedInstanceState);
        setContentView(R.layout.q0501_c001);
        setupViewComponent();
    }


    private void setupViewComponent() {

        //----繼承主page標題---
        Intent intent=getIntent();
        setTitle(intent.getStringExtra("class_title"));

        //繼承上一頁名稱,電話
        keep01 = intent.getStringExtra("c001_farmname");
        keep02 = intent.getStringExtra("c001_tel");
        //flag_per_call= intent.getStringExtra("c001_permission");

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
        t114=(TextView)findViewById(R.id.q0501_t114); //server message, if connect well...
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

        // 動態調整高度 抓取使用裝置自身的尺寸
        DisplayMetrics displayMetrics = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int buttonwidth = displayMetrics.widthPixels /4;

        b101.getLayoutParams().width=buttonwidth;
        b102.getLayoutParams().width=buttonwidth;

        //----------
        e103.setMovementMethod(ScrollingMovementMethod.getInstance());//設定說明欄可以捲動
        e103.scrollTo(0,0);//出現的textview，回說明欄頂端(左上角)
        e104.setMovementMethod(ScrollingMovementMethod.getInstance());//設定說明欄可以捲動
        e104.scrollTo(0,0);//出現的textview，回說明欄頂端(左上角)

        u_layout_def();//
        initDB();
        showRec(index);

        //spinner和clicklistener--twice
//        s001 = (Spinner)findViewById(R.id.q0501_spinner);
//        s001.setOnItemSelectedListener(mSpnNameOnItemSelLis);
//        s001.setVisibility(View.INVISIBLE);//一定要放在這裡才不會當掉

        e111.setTextColor(ContextCompat.getColor(this, R.color.Red));
        e111.setBackgroundColor(ContextCompat.getColor(this, R.color.yellow_softness));

        //--繼承上一頁名稱,電話，放入對應位置; text1, text2 clean;
        //intent 會跟spinner mysql導入發生衝突, 無法intent
        //新安裝時，這裡會有用；第二次新增，就要靠adapter

        e101.setText(keep01);
        e102.setText(keep02);
        e103.setText("");
        e104.setText("");

        rl01.setVisibility(View.VISIBLE);
        ll32.setVisibility(View.INVISIBLE);
    }

    private void initDB() {
        if (dbHelper == null) {
            dbHelper = new Q0501DBhlper(this, DB_File, null, DBversion);//可自動生成
        }
        dbmysql();
        recSet = dbHelper.getRecSet_Q0501();
    }

    private void u_layout_def() {
        b101.setVisibility(View.VISIBLE);
        b102.setVisibility(View.VISIBLE);
        b103.setVisibility(View.INVISIBLE);
        b108.setVisibility(View.INVISIBLE);
        b109.setVisibility(View.INVISIBLE);
        b104.setVisibility(View.INVISIBLE);
        b105.setVisibility(View.INVISIBLE);
        b106.setVisibility(View.INVISIBLE);
        b107.setVisibility(View.INVISIBLE);
        t111.setVisibility(View.INVISIBLE);
        e111.setVisibility(View.INVISIBLE);
        e111.setEnabled(false);
        e101.setEnabled(false);
        e102.setEnabled(false);
    }

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
            case R.id.q0501_b101://新增
                if(account_state){
                    e_name = e101.getText().toString().trim();//名稱
                    e_tel = e102.getText().toString().trim();//電話
                    e_text1 = e103.getText().toString().trim();//備註1
                    e_text2 = e104.getText().toString().trim();//備註2
                    e_email=g_Email;
                    Log.d(TAG, "Q0501c001_b101//"+g_Email+"//");
                    if (e_name.equals("") || e_tel.equals("") || e_text1.equals("") || e_text2.equals("")) {
                        Toast.makeText(Q0501c001.this, "資料空白無法新增 !", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    //直接增加到MySQL
                    mysql_insert();
                    dbmysql();
                    //--------
                    msg = null;
                    // -------------------------
                    ContentValues newRow = new ContentValues();
                    newRow.put("name", e_name);
                    newRow.put("tel", e_tel);
                    newRow.put("text1", e_text1);
                    newRow.put("text2", e_text2);
                    //------------------------------
                    long rowID = dbHelper.insertRec_m_Q0501(newRow);
                    if (rowID != -1) {
                        e111.setHint("請繼續輸入");
                        e101.setText("");
                        e102.setText("");
                        e103.setText("");
                        e104.setText("");
                        dbmysql();
                        msg = "新增記錄  成功 ! \n" + "目前資料表共有 " + dbHelper.RecCount_Q0501() + " 筆記錄 !";
//                    ctlLast();  //成功跳到最後一筆
                    } else {
                        msg = "新增記錄  失敗 !";
                    }
                    Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
                    t112.setText("共計:" + Integer.toString(dbHelper.RecCount_Q0501()) + "筆");
                    u_insert();
                }else{
                    Toast.makeText(getApplicationContext(),getString(R.string.q0501_hint03),Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.q0501_b102://取消
                e103.setText("");
                e104.setText("");
                this.finish();
                break;

        }

    }

    private void u_insert() {
        b101.setVisibility(View.VISIBLE);
        b102.setVisibility(View.VISIBLE);
        b103.setVisibility(View.INVISIBLE);
        b104.setVisibility(View.INVISIBLE);
        b105.setVisibility(View.INVISIBLE);
        b106.setVisibility(View.INVISIBLE);
        b107.setVisibility(View.INVISIBLE);
        b108.setVisibility(View.INVISIBLE);
        b109.setVisibility(View.INVISIBLE);

        e111.setEnabled(false);
    }

    private void showRec(int index) {
        msg = "";
        if (recSet.size() != 0) {
            msg = getString(R.string.q0501_t112) + recSet.size() + getString(R.string.q0501_hint02);

            String[] fld = recSet.get(index).split("#");
            e111.setTextColor(ContextCompat.getColor(this, R.color.Red));
            e111.setBackgroundColor(ContextCompat.getColor(this, R.color.Yellow));
            e111.setText(fld[0]);//ID
            e101.setText(fld[1]);//名稱
            e102.setText(fld[2]);//電話
            e103.setText(fld[3]);//備註1
            e104.setText(fld[4]);//備註2

      //      s001.setSelection(index, true); //spinner 小窗跳到第幾筆

        } else {
            String stHead = "顯示資料：0 筆";
            e111.setText("");//ID
            e101.setText("");//名稱
            e102.setText("");//電話
            e103.setText("");//備註1
            e104.setText("");//備註2

        }

        t112.setText(msg);
    }

    private void dbmysql() {
        sqlctl = "SELECT * FROM farm02 WHERE email='"+ g_Email +"' ORDER BY id ASC";
        ArrayList<String> nameValuePairs = new ArrayList<>();
        nameValuePairs.add(sqlctl);
        try {
            String result = Q0501DBConnector.executeQuery_Q0501(nameValuePairs);
//----------------
            chk_httpstate();  //檢查 連結狀態
//-------------------------------------
            JSONArray jsonArray = new JSONArray(result);
            // -------------------------------------------------------
            if (jsonArray.length() > 0) { // MySQL 連結成功有資料
                rowsAffected = dbHelper.clearRec_Q0501();                 // 匯入前,刪除所有SQLite資料
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
                    // newRow.put("name", jsonData.getString("name").toString());
                    // newRow.put("grp", jsonData.getString("grp").toString());
                    // newRow.put("address", jsonData.getString("address")
                    // -------------------加入SQLite---------------------------------------
                    long rowID = dbHelper.insertRec_m_Q0501(newRow);
                    //Toast.makeText(getApplicationContext(), "共匯入 " + Integer.toString(jsonArray.length()) + " 筆資料", Toast.LENGTH_SHORT).show();
                }
               // Toast.makeText(getApplicationContext(), "共匯入 " + Integer.toString(jsonArray.length()) + " 筆資料", Toast.LENGTH_SHORT).show();
                // ---------------------------
            } else {
                Toast.makeText(getApplicationContext(), "主機資料庫無資料", Toast.LENGTH_LONG).show();
            }
            recSet = dbHelper.getRecSet_Q0501();  //重新載入SQLite
            // --------------------------------------------------------
        } catch (Exception e) {
            Log.d(TAG, e.toString());
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

    private void mysql_insert() {

        ArrayList<String> nameValuePairs = new ArrayList<>();
        nameValuePairs.add(e_name);
        nameValuePairs.add(e_tel);
        nameValuePairs.add(e_text1);
        nameValuePairs.add(e_text2);
        nameValuePairs.add(e_email);
        try {
            Thread.sleep(500); //  延遲Thread 睡眠0.5秒
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
//-----------------------------------------------
        String result = Q0501DBConnector.executeInsert_Q0501(nameValuePairs);  //真正執行新增
//-----------------------------------------------
    }

    //------生命週期-------
    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
//        if (dbHelper == null)
//            dbHelper = new Q0501DBhlper(this, DB_File, null, DBversion);
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
            Log.d(TAG, "Q0501c001start"+g_Email+"//"+g_GivenName);
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
                    intent02.putExtra("class_title",getString(R.string.q0501_t901));
                    intent02.setClass(Q0501c001.this, Q0501c002.class);
                    intent02.putExtra("c002_permission",flag_per_call);
                    startActivity(intent02);
                    this.finish();

                break;

            case R.id.q0501_msub_u001://更新刪除
                intent03.putExtra("class_title",getString(R.string.q0501_t903));
                intent03.putExtra("c003_permission",flag_per_call);
                intent03.setClass(Q0501c001.this, Q0501c003.class);
                startActivity(intent03);
                this.finish();
                break;


        }
        return super.onOptionsItemSelected(item);
    }
}