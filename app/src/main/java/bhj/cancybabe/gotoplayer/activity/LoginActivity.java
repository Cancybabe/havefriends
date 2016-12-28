package bhj.cancybabe.gotoplayer.activity;


import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import bhj.cancybabe.gotoplayer.R;
import bhj.cancybabe.gotoplayer.application.MyApplication;
import bhj.cancybabe.gotoplayer.base.BaseActivity;
import bhj.cancybabe.gotoplayer.base.BaseInterface;
import bhj.cancybabe.gotoplayer.bean.UserActivtiesInfo;
import bhj.cancybabe.gotoplayer.bean.UserInfo;
import bhj.cancybabe.gotoplayer.utils.FindActionInfoUtils;
import bhj.cancybabe.gotoplayer.utils.FindUserInfoUtils;
import bhj.cancybabe.gotoplayer.utils.SaveAndGetUserInfoUtils;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.DownloadFileListener;
import cn.bmob.v3.listener.SaveListener;
import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by Cancybabe on 2016/11/7.
 * 登录界面
 */
public class LoginActivity extends BaseActivity implements BaseInterface {
    private EditText mEt_UserName;
    private EditText mEt_PassWord;
    private Button mBtn_Login;
    private SweetAlertDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
        String[] userLoginInfo = SaveAndGetUserInfoUtils.getUserInfo(getApplicationContext());
        mEt_UserName.setText(userLoginInfo[0]);
        mEt_PassWord.setText(userLoginInfo[1]);


    }

    @Override
    public void initView() {
        mEt_UserName = (EditText) findById(R.id.act_login_et_username);
        mEt_PassWord = (EditText) findById(R.id.act_login_et_password);
        mBtn_Login = (Button) findById(R.id.act_login_btnlogin);
    }

    @Override
    public void initListener() {

    }

    /**
     * 从注册页面返回主界面时展示注册的用户名和密码
     */
    @Override
    protected void onRestart() {
        super.onRestart();
        String[] userLoginInfo = SaveAndGetUserInfoUtils.getUserInfo(getApplicationContext());
        mEt_UserName.setText(userLoginInfo[0]);
        mEt_PassWord.setText(userLoginInfo[1]);
    }

    /**
     * 注册按钮被点击
     *
     * @param view
     */
    public void onRegisterClick(View view) {
        //跳转至注册页面
        Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
        startActivity(intent);
    }


    /**
     * 登录按钮被点击
     *
     * @param view
     */

    public void onLoginBtnClick(View view) {
        //获取文本框的用户名和密码
        String actLoginPhoneNumber = mEt_UserName.getText().toString().trim();
        String actloginPassword = mEt_PassWord.getText().toString().trim();
        //判断文本框的用户名或密码不为空
        if (actLoginPhoneNumber.equals("") || actLoginPhoneNumber == null || actloginPassword.equals("") || actloginPassword == null) {

            toast("用户名或密码不能为空");
        } else {
            //发送给服务器判断用户名和密码是否正确
            sendLoginMessageToserver(actLoginPhoneNumber, actloginPassword);
        }
    }

    /**
     * 弹出对话框
     */
    public void showDilaog() {
        pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#FF567F"));
        pDialog.setTitleText("Loading");
        pDialog.setCancelable(false);
        pDialog.show();
    }

    /**
     * 登录的用户名和密码发给服务器判断
     *
     * @param actLoginUsername
     * @param actloginPassword
     */
    private void sendLoginMessageToserver(final String actLoginUsername, final String actloginPassword) {
        showDilaog();
        BmobUser loginBm = new BmobUser();
        loginBm.setUsername(actLoginUsername);
        loginBm.setPassword(actloginPassword);
        loginBm.login(new SaveListener<BmobUser>() {
            @Override
            public void done(BmobUser bmobUser, BmobException e) {
                if (e == null) {
                    toast("登录成功");
                    pDialog.dismiss();
//                    通过BmobUser user = BmobUser.getCurrentUser()获取登录成功后的本地用户信息
                    //如果是自定义用户对象MyUser，可通过MyUser user = BmobUser.getCurrentUser(MyUser.class)获取自定义用户信息
                    //保存用户名和密码至本地
                    SaveAndGetUserInfoUtils.saveUsernameAndPassword(getApplicationContext(), actLoginUsername, actloginPassword);

                    MyApplication.userInfo = BmobUser.getCurrentUser(UserInfo.class);
                    //跳转至主界面



                    jumpToHomeAct();
                    return;

                } else {
                    pDialog.dismiss();
                    toast("用户名或密码错误........");
                }
            }
        });
    }

    /**
     * 跳转至主界面
     */
    public void jumpToHomeAct() {
        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
        startActivity(intent);
        finish();
    }
}
