package bhj.cancybabe.gotoplayer.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import java.util.HashMap;
import bhj.cancybabe.gotoplayer.application.MyApplication;
import bhj.cancybabe.gotoplayer.bean.UserInfo;

/**
 * Created by Cancybabe on 2016/11/12.
 */
public class SaveAndGetUserInfoUtils {
    /**
     * SharePreference 记住登录密码，将用户名和密码保存到本地
     *
     * @param phoneNumber
     * @param loginPassword
     */
    public static void saveUsernameAndPassword(Context context, String phoneNumber, String loginPassword) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("userLoginInfo", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("phonenumber", phoneNumber);
        editor.putString("password", loginPassword);
        editor.commit();
//        Log.i("MyTag", phoneNumber + "----" + passWord);
    }


    /**
     * 记住密码功能从本地拿到用户名和密码
     */
    public static String[] getUserInfo(Context context) {
        String[] userLoginInfo = new String[2];
        SharedPreferences sharedPreferences = context.getSharedPreferences("userLoginInfo", Context.MODE_PRIVATE);
        if (sharedPreferences != null) {
            String phoneNumber = sharedPreferences.getString("phonenumber", "");
            String passWord = sharedPreferences.getString("password", "");
            userLoginInfo[0] = phoneNumber;
            userLoginInfo[1] = passWord;
        } else {
            userLoginInfo[0] = "";
            userLoginInfo[1] = "";
        }
        return userLoginInfo;

    }
}
