package tw.tcfarmgo.tcnrcloud110a01;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Q0501DBConnector {
    public static int httpstate;
    //--------------------------------------------------------
    private static String postUrl;
    private static String myResponse;
    static String result = null;
    private static OkHttpClient client = new OkHttpClient();

    //---------------------------------------------------------
    static String connect_ip = "https://tcnrcloud110a.com/11/Freefarm/android_connect_db_farm02.php";


    public static String executeQuery_Q0501(ArrayList<String> query_string) { //search mysql's wordpress
//        OkHttpClient client = new OkHttpClient();
        postUrl=connect_ip ;

        String query_0 = query_string.get(0);
//        String query_0 = "SELECT * FROM XXXX where name = 'text' ";

        FormBody body = new FormBody.Builder()
                .add("selefunc_string","query")
                .add("query_string", query_0)
                .build(); //build a "Q", fong bou
//--------------
        Request request = new Request.Builder()
                .url(postUrl)
                .post(body)
                .build(); //throw out a fong bou
//---====================================================
        // ===========================================
        // 使用httpResponse的方法取得http 狀態碼設定給httpstate變數
        httpstate = 0;   //設 httpcode初始值
        // ===========================================
        try (Response response = client.newCall(request).execute()) {
            httpstate = response.code();
            // ===========================================
            return response.body().string();//傳回去
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static String executeInsert_Q0501(ArrayList<String> query_string) {
        OkHttpClient client = new OkHttpClient();
        postUrl=connect_ip ;
        //--------------
        String query_0 = query_string.get(0);
        String query_1 = query_string.get(1);
        String query_2 = query_string.get(2);
        String query_3 = query_string.get(3);
        String query_4 = query_string.get(4);

        FormBody body = new FormBody.Builder()
                .add("selefunc_string","insert")
                .add("name", query_0)
                .add("tel", query_1)
                .add("text1", query_2)
                .add("text2", query_3)
                .add("email", query_4)
                .build();
//--------------
        Request request = new Request.Builder()
                .url(postUrl)
                .post(body)
                .build();
        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static String executeDelet_Q0501(ArrayList<String> query_string) {
        postUrl=connect_ip ;
        //--------------
        String query_0 = query_string.get(0);

        FormBody body = new FormBody.Builder()
                .add("selefunc_string","delete")
                .add("id", query_0)
                .build();
//--------------
        Request request = new Request.Builder()
                .url(postUrl)
                .post(body)
                .build();
        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static String executeUpdate_Q0501(ArrayList<String> query_string) {
//        OkHttpClient client = new OkHttpClient();
        postUrl=connect_ip ;
        //--------------
        String query_0 = query_string.get(0);
        String query_1 = query_string.get(1);
        String query_2 = query_string.get(2);
        String query_3 = query_string.get(3);
        String query_4 = query_string.get(4);

        FormBody body = new FormBody.Builder()
                .add("selefunc_string","update")
                .add("id", query_0)
                .add("name", query_1)
                .add("tel", query_2)
                .add("text1", query_3)
                .add("text2", query_4)
                .build();
//--------------
        Request request = new Request.Builder()
                .url(postUrl)
                .post(body)
                .build();
        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }


//==========================
}
