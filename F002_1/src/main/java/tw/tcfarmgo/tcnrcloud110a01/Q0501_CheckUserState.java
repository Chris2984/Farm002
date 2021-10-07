package tw.tcfarmgo.tcnrcloud110a01;

import android.content.Context;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

public class Q0501_CheckUserState {
    private static Boolean account_state=false;

    public Q0501_CheckUserState(Context context){
        //建構時會確認登入，不呼叫建構的話，每次開啟程式會預設 account_state=false ，進行登入動作才會 true

        GoogleSignInAccount signInAccount = GoogleSignIn.getLastSignedInAccount(context);
        if(signInAccount!=null){
            account_state=true;
        }else{
            account_state=false;
        }
    }
    public  void setAccount_state(Boolean state){
        account_state =state;
    }
    public static Boolean getAccount_state() {
        //使用 static Boolean getAccount_state()  靜態類別 可不建構即可呼叫(預設false)
        return account_state;
    }
}
