package tw.tcfarmgo.tcnrcloud110a01;

import static android.content.ContentValues.TAG;

import android.Manifest;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
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
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.Scope;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class Q0501c002 extends AppCompatActivity implements View.OnClickListener {

    private Intent intent = new Intent();
    private Intent intent02=new Intent();

    private EditText e101,e102,e103,e104,e111;
    private Button b101,b102,b103,b104,b105,b106,b107,b108,b109,b110;


    private Q0501DBhlper dbHelper;
    private static final String DB_File = "FarmTravel.db";
    private static final int DBversion = 1;
    private int tcount;

    // ------------------
    public static String myselection = "";
    public static String myorder = "id ASC"; // 排序欄位
    public static String myargs[] = new String[]{};
    private static final int REQUEST_CODE_ASK_PERMISSIONS = 1;
    private ArrayList<String> recSet;
    private int index=0;
    private String msg;
    private ListView lv001;
    private TextView tsub,t112,t114,t115;
    private RelativeLayout rl01;
    private LinearLayout ll32,ll34;
    private String e_id,e_name,e_tel,e_text1,e_text2;
    private int old_index;
    private String sqlctl;
    private int rowsAffected;
    private int servermsgcolor;
    private String ser_msg;
    private Spinner s002;
    private Intent intent023=new Intent();
    private String t_name,t_tel,t_text1,t_text2;
    private Button b111,b112;
    private int nowposition;
    private Uri uri;

    private Geocoder geocoder;
    private double latitude,longitude;
    private Uri utel;
    private Intent Utel;
    private Boolean account_state;
    private String g_Email;
    private String e_email;
    private Boolean flag_per_call;
    private TextView t111;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        enableStrictMode(this);//use 使用暫存堆疊必須加此條件
        super.onCreate(savedInstanceState);
        setContentView(R.layout.q0501_c001);
        //----繼承主page標題---
        Intent intent02=getIntent();
        setTitle(intent02.getStringExtra("class_title"));

        Intent intent032=getIntent();
        setTitle(intent032.getStringExtra("class_title"));

        Intent intent52=getIntent();
        setTitle(intent52.getStringExtra("class_title"));
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
        ll34 = (LinearLayout) findViewById(R.id.q0501_ll34);//the page of after searching

        b101=(Button)findViewById(R.id.q0501_b101);//新增
        b102=(Button)findViewById(R.id.q0501_b102);//取消
        b103=(Button)findViewById(R.id.q0501_b103);//查詢
        b104=(Button)findViewById(R.id.q0501_b104);//首筆
        b105=(Button)findViewById(R.id.q0501_b105);//上一筆
        b106=(Button)findViewById(R.id.q0501_b106);//下一筆
        b107=(Button)findViewById(R.id.q0501_b107);//尾筆
        b108=(Button)findViewById(R.id.q0501_b108);//更新
        b109=(Button)findViewById(R.id.q0501_b109);//刪除
        b110=(Button)findViewById(R.id.q0501_b110);//回到列表
        b111=(Button)findViewById(R.id.q0501_b111);//查詢店家位置
        b112=(Button)findViewById(R.id.q0501_b112);//打電話

        b101.setOnClickListener(this);
        b102.setOnClickListener(this);
        b103.setOnClickListener(this);
        b104.setOnClickListener(this);
        b105.setOnClickListener(this);
        b106.setOnClickListener(this);
        b107.setOnClickListener(this);
        b108.setOnClickListener(this);
        b109.setOnClickListener(this);
        b110.setOnClickListener(this);
        b111.setOnClickListener(this);
        b112.setOnClickListener(this);

        //建立Geocorder物件,用哪一區的地圖做解碼(most important)
        geocoder=new Geocoder(this, Locale.TAIWAN);

        //spinner和clicklistener
        s002 = (Spinner)findViewById(R.id.q0501_spinner);
        s002.setOnItemSelectedListener(mSpnNameOnItemSelLis);
        s002.setVisibility(View.INVISIBLE);//一定要放在這裡才不會當掉
//        s002.setSelection(index, true);//..........................................

        // 動態調整高度 抓取使用裝置自身的尺寸
        DisplayMetrics displayMetrics = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int buttonwidth = displayMetrics.widthPixels /2;

        b111.getLayoutParams().width=buttonwidth;
        b112.getLayoutParams().width=buttonwidth;

        int buttonwidth2 = displayMetrics.widthPixels /4;

        b103.getLayoutParams().width=buttonwidth2;
        b102.getLayoutParams().width=buttonwidth2;
        //--

        u_layout_def2();// the first page layout appearance
        initDB();
        showRec(index);

        //系統的ID欄位
        e111.setTextColor(ContextCompat.getColor(this, R.color.Red));
        e111.setBackgroundColor(ContextCompat.getColor(this, R.color.yellow_softness));

        //--text1, text2 clean, 沒效,被spinner衝掉了
        e103.setText("");
        e104.setText("");
        e101.setText("");
        e102.setText("");
        //tvtitle. 初始顯示 (你在第幾/共計筆)
        t115.setTextColor(ContextCompat.getColor(this, R.color.Teal));
        t115.setText("顯示資料： 共" + tcount + " 筆");
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
        s002.setAdapter(adapter);
        s002.setOnItemSelectedListener(mSpnNameOnItemSelLis);
    }

    private void initDB() {
        if (dbHelper == null) {
            dbHelper = new Q0501DBhlper(this, DB_File, null, DBversion);//可自動生成
        }
        dbmysql();
        recSet = dbHelper.getRecSet_Q0501();

    }

    private void u_layout_def2() {
        b101.setVisibility(View.INVISIBLE);//新增
        b102.setVisibility(View.VISIBLE);//取消
        b103.setVisibility(View.VISIBLE);//查詢
        b108.setVisibility(View.INVISIBLE);//更新
        b109.setVisibility(View.INVISIBLE);//刪除
        b104.setVisibility(View.INVISIBLE);
        b105.setVisibility(View.INVISIBLE);
        b106.setVisibility(View.INVISIBLE);
        b107.setVisibility(View.INVISIBLE);
        //t112.setVisibility(View.INVISIBLE);
        t115.setVisibility(View.INVISIBLE); //頭條
        e111.setVisibility(View.INVISIBLE); //ID不顯示
        t111.setVisibility(View.INVISIBLE);


        e111.setEnabled(false); //ID欄位鎖住
        s002.setEnabled(false); //spinner鎖住


    }




    private AdapterView.OnItemSelectedListener mSpnNameOnItemSelLis = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            int iSelect = s002.getSelectedItemPosition();//找到按哪個
            String[] fld = recSet.get(iSelect).split("#");
            String s = "資料:共" + recSet.size() + "筆," + "你按下第" + String.valueOf(iSelect + 1) + "項";//起始為0
            t115.setText(s);
            //           b_id.setTextColor((ContextCompat.getColor(parent.getContext(), R.color.Red)));
//--------------12
            index = position;
            showRec(index);
            iSelect = index;
//-----------12
//            e101.setText(keep01);沒用
//            e102.setText(keep02);

            if (b101.getVisibility() == View.VISIBLE) { //b101是新增
                e111.setHint("請繼續輸入");
                e101.setText("");
                e102.setText("");
                e103.setText("");
                e104.setText("");
            }

            if (b103.getVisibility() == View.VISIBLE) { //b103是查詢

                //e111.setHint("請繼續輸入");
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

            if(index == -1){
                Toast.makeText(getApplicationContext(), "int index is null", Toast.LENGTH_SHORT).show();
            }else {
                s002.setSelection(index, true);
            }
           // s002.setSelection(index, true); //spinner 小窗跳到第幾筆  放這裡當掉
//            e101.setText(keep01);  放這裡沒用
//            e102.setText(keep02);

        } else {
            String stHead = "顯示資料：0 筆";
            t115.setText(stHead);
            e111.setText("");//ID
            e101.setText("");//名稱
            e102.setText("");//電話
            e103.setText("");//備註1
            e104.setText("");//備註2

        }

        t112.setText(msg);
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

            case R.id.q0501_b102://取消
                //Toast.makeText(getApplicationContext(), "*施工中*", Toast.LENGTH_SHORT).show();
                //--暫定不清空名稱和電話
                e101.setText("");
                e102.setText("");
                e103.setText("");
                e104.setText("");
                //this.finish();
                break;

            case R.id.q0501_b103://查詢
                if(account_state) {
                    //account_state=true
                    initDB();//避免跑出去又回來時crash
                    t_name = e101.getText().toString().trim();
                    t_tel = e102.getText().toString().trim();
                    t_text1 = e103.getText().toString().trim();
                    t_text2 = e104.getText().toString().trim();
                    e_email=g_Email;
                    msg = null;
                    recSet = dbHelper.getRecSet_query_Q0501c002q(t_name,t_tel,t_text1,t_text2,e_email);
                    Log.d(TAG, "Q0501c003_b103()//"+recSet+"//");
                    //Toast.makeText(getApplicationContext(), "顯示資料： 共 " + recSet.size() + " 筆", Toast.LENGTH_SHORT).show();
                    //--------取SQLite 資料---------------
                    List<Map<String, Object>> mList;
                    mList = new ArrayList<Map<String, Object>>();
                    for (int i = 0; i < recSet.size(); i++) {
                        Map<String, Object> item = new HashMap<String, Object>();
                        String[] fld = recSet.get(i).split("#");
                        item.put("q0501_img020", R.drawable.t007);
                        item.put("txtView", "id:" + fld[0] + "\n名稱:" + fld[1] + "\n電話:" + fld[2] + "\n備註1:" + fld[3] + "\n備註2:" + fld[4]);

                        mList.add(item);
                    }
                    //System.out: CCCCCCCCCC[{txtView=id:1
                    //    名稱:台中火車站
                    //    電話:0422228888
                    //    備註1:開放時間: 除國定假日皆開放
                    //    備註2:門票:半價, q0501_img020=2131165331}]
                    //=========設定listview========
                    rl01.setVisibility(View.INVISIBLE);
                    ll32.setVisibility(View.VISIBLE);
                    //Toast.makeText(getApplicationContext(), "*施工中*", Toast.LENGTH_SHORT).show();
                    //-----------------------------
                    SimpleAdapter adapter = new SimpleAdapter(
                            this,
                            mList,
                            R.layout.q0501_c002_list,
                            new String[]{"q0501_img020", "txtView"},
                            new int[]{R.id.q0501_img020, R.id.txtView}
                    );
                    lv001.setAdapter(adapter);
                    lv001.setTextFilterEnabled(true);
                    lv001.setOnItemClickListener(listviewOnItemClkLis);

                    //------------------------------------
                }else {
                    Toast.makeText(getApplicationContext(),getString(R.string.q0501_hint03),Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.q0501_b110://取消列表
                initDB(); //避免跑出去又回來時crash
                tsub.setText("");
                old_index=s002.getSelectedItemPosition();
                //dbmysql(); 他已經包含在initDB();
                recSet=dbHelper.getRecSet_Q0501();
                rl01.setVisibility(View.VISIBLE);
                ll32.setVisibility(View.INVISIBLE);
                ll34.setVisibility(View.GONE);
                index=old_index;
                showRec(index);//重設spinner小窗顯示和內容
                ctlLast();
                break;

            case R.id.q0501_b111: //搜尋店家位置
                msg = null;
                recSet = dbHelper.getRecSet_query_Q0501(t_name,t_tel,t_text1,t_text2);
                System.out.println("DDDDDDDDDDDDDDDD"+recSet);
               // Log.d(TAG, t_name.toString());
                String addr=recSet.get(nowposition);
                String addressName = addr.trim();

                System.out.println("WWWWWWWWWWWWWWWWWWWW"+addressName);
               // Log.d(TAG, addressName.toString());
                try{
                    //將地名轉換成GPS座標
                    //取得經緯度座標清單的list物件
                    List<Address> listGPSAddress=geocoder.getFromLocationName(addressName,1);
                    // 有找到經緯度座標
                    if (listGPSAddress != null) {
                        latitude = listGPSAddress.get(0).getLatitude();
                        longitude = listGPSAddress.get(0).getLongitude();

                    }
                }catch (Exception ex) {
                    //Toast.makeText(getApplicationContext(),"錯誤:"+ex.toString(),Toast.LENGTH_SHORT).show();
                    Toast.makeText(getApplicationContext(),"用這個名稱登記的地址不存在!",Toast.LENGTH_SHORT).show();
                }

                //啟動google地圖，已取得經緯度座標
                // 建立URI字串
                String uri = String.format("geo:%f,%f?z=17", latitude, longitude);
                // 建立Intent物件
                Intent geoMap = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                startActivity(geoMap);  // 啟動活動

                break;

            case R.id.q0501_b112: //許願清單致電店家
                if(flag_per_call == true){
                    recSet = dbHelper.getRecSet_query_Q0501c002t(t_tel);
                    String call=recSet.get(nowposition);
                    System.out.println("PPPPPPPPPPPPPP"+call);
                    utel = Uri.parse("tel:"+call);
                    Utel = new Intent(Intent.ACTION_DIAL, utel);
                    startActivity(Utel);
                    }else{
                        //Toast.makeText(getApplicationContext(),getString(R.string.q0501_hint04),Toast.LENGTH_LONG).show();
                    makeACall();
                    }
                break;
        }

    }

    private void makeACall() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE)
                != PackageManager.PERMISSION_GRANTED) {

            // 這邊是照官網說法，在確認沒有權限的時候，確認是否需要說明原因
            // 需要的話就先顯示原因，在使用者看過原因後，再request權限
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CALL_PHONE)) {
                Q0501_Util.showDialog(this, R.string.dialog_msg_call, android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        requestNeededPermission();
                    }
                });
            } else {
                // 否則就直接request
                requestNeededPermission();
            }
        } else {
            recSet = dbHelper.getRecSet_query_Q0501c002t(t_tel);
            String call=recSet.get(nowposition);
            System.out.println("4044444444444"+call);
            utel = Uri.parse("tel:"+call);
            Utel = new Intent(Intent.ACTION_DIAL, utel);
            startActivity(Utel);
        }

    }

    /*** request需要的權限*/
    private void requestNeededPermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CODE_ASK_PERMISSIONS);
        System.out.println("requestNeededPermission()");
    }

    private ListView.OnItemClickListener listviewOnItemClkLis = new ListView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            String s = "你按下第 " + Integer.toString(position + 1) + "筆"
                    + ((TextView) view.findViewById(R.id.txtView))
                    .getText()
                    .toString();

            ll34.setVisibility(View.VISIBLE);
            tsub.setText(s);
            System.out.println("PPPPPPPPXXXXXXXXXXXXXXXXXXPPPPPP"+s);
            //取得listview的選擇
            nowposition=position;
        }

    };
    private void dbmysql() {
         //這裡會影響spinner中的data呈現，進行影響後面查詢
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
                    // newRow.put("name",
                    // jsonData.getString("name").toString());
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
            u_setspinner();
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
        initDB(); //跑出去再回來，避免crash(for 查詢位址和電話聯絡)
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

        //------檢查權限
        flag_per_call= Q0501.getCall_state();
        System.out.println("c0002onstart-------"+flag_per_call);
    }

    //---------------------------------
    private void updateUI(GoogleSignInAccount account) {
        GoogleSignInAccount aa = account;
        int aaa=1;
        if (account != null) {
            g_Email=account.getEmail();  //信箱
            Log.d(TAG, "Q0501c002_onStart()//"+g_Email+"//");
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
                Toast.makeText(getApplicationContext(), "這是查詢頁喔~", Toast.LENGTH_SHORT).show();
                break;

            case R.id.q0501_msub_u001://更新刪除
                intent023.putExtra("class_title",getString(R.string.q0501_t903));
                intent023.setClass(Q0501c002.this, Q0501c003.class);
                startActivity(intent023);
                this.finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}