package bhj.cancybabe.gotoplayer.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import bhj.cancybabe.gotoplayer.R;
import bhj.cancybabe.gotoplayer.base.BaseActivity;
import bhj.cancybabe.gotoplayer.bean.UserInfo;
import bhj.cancybabe.gotoplayer.broadcast.SmsContentReceiver;
import bhj.cancybabe.gotoplayer.utils.SaveAndGetUserInfoUtils;
import cn.bmob.sms.BmobSMS;
import cn.bmob.sms.exception.BmobException;
import cn.bmob.sms.listener.RequestSMSCodeListener;
import cn.bmob.sms.listener.VerifySMSCodeListener;
import cn.bmob.v3.listener.SaveListener;

/**
 * Created by Cancybabe on 2016/11/8.
 */
public class RegisterActivity extends BaseActivity {
    private EditText mEt_nickName;
    private EditText mEt_phoneNumber;
    private EditText mEt_passWord;
    private CheckBox mCb_man;
    private CheckBox mCb_woman;
    private EditText mEt_code;
    private Button mBtn_getCode;
    private Button mBtn_register;
    private String mPhoneNumber;//获取到的电话号码
    private String mNickName;//获取到的昵称
    private String mPassWord;//获取到的密码
    private Boolean mCbMan_Flag;//CheckBox是否被选中的标记
    private Boolean mCbWoman_Flag;//CheckBox是否被选中的标记
    private int mCbSexFlag;//CheckBox是否被选中的String标记
    private String mCode;//获取到的验证码
    private  Boolean flag =false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initViews();
        //短信广播的监听
        smsListenerAndSetCodeText();

    }


    /**
     * 收到短信广播，设置验证码输入框
     */
    public void smsListenerAndSetCodeText() {
        SmsContentReceiver.setOnGetSmsNotifySetSmsListener(new SmsContentReceiver.OnGetSmsNotifySetSmsListener() {
            @Override
            public void onSetText(String code) {
                mEt_code.setText(code);
            }
        });
    }


    /**
     * 初始化控件
     */
    private void initViews() {
        mEt_nickName = (EditText) findById(R.id.act_register_et_nickname);
        mEt_phoneNumber = (EditText) findById(R.id.act_register_et_phonenumber);
        mEt_passWord = (EditText) findById(R.id.act_register_et_password);
        mEt_code = (EditText) findById(R.id.act_register_et_code);
        mCb_man = (CheckBox) findById(R.id.act_register_cb_man);
        mCb_woman = (CheckBox) findById(R.id.act_register_cb_woman);
        mBtn_getCode = (Button) findById(R.id.act_register_btn_getcode);
        mBtn_register = (Button) findById(R.id.act_register_btn_sumbitregister);
    }


    /**
     * 监听获取验证码按钮
     * 获取验证码按钮被点击
     */
    public void onGetCodeOnClick(View view) {

        //获取电话号码
        mPhoneNumber = mEt_phoneNumber.getText().toString().trim();
        if (mPhoneNumber == null || mPhoneNumber.equals("")) {
            Log.i("bmob", "号码为不能为空");//用于查询本次短信发送详情
            toast("号码为不能为空");
        } else if (mPhoneNumber.length() != 11) {
            toast("号码有误，请重新输入");
        } else {
            //改变获取验证码按钮文字
            setCodeBtnChange();
            //向服务器请求数据
            requestCode(mPhoneNumber);
        }

    }

    /**
     * 改变获取验证码按钮文字
     */
    public void setCodeBtnChange() {
        mBtn_getCode.setClickable(false);

        CountDownTimer countDownTimer = new CountDownTimer(60000, 1000) {
            @Override
            public void onTick(long l) {
                int time = (int) (l / 1000);
                //设置按钮文字变化
                mBtn_getCode.setText(time + "s");
                mBtn_getCode.setTextColor(getResources().getColor(R.color.act_titlebar));
                mBtn_getCode.setBackground(getResources().getDrawable(R.drawable.act_register_btn_codeafter));

            }

            @Override
            public void onFinish() {
                mBtn_getCode.setText("获取验证码");
                mBtn_getCode.setClickable(true);
                mBtn_getCode.setTextColor(getResources().getColor(R.color.act_text));
                mBtn_getCode.setBackground(getResources().getDrawable(R.drawable.act_register_btn_selector));
            }
        };
        countDownTimer.start();
    }


    /**
     * 向服务器请求验证码
     *
     * @param mPhoneNumber 电话号码
     */

    public void requestCode(String mPhoneNumber) {
        //向服务器请求验证码
        BmobSMS.requestSMSCode(this, mPhoneNumber, "GoToPlay", new RequestSMSCodeListener() {
            @Override
            public void done(Integer smsId, BmobException ex) {
                if (ex == null) {//验证码发送成功
                    Log.i("bmob", "短信id:" + smsId);//用于查询本次短信发送详情
                    //监听广播接受者
                } else {
                    Log.i("bmob", "短信id:" + "不成功");//用于查询本次短信发送详情
                }
            }
        });
    }


    /**
     * 监听注册按钮
     * 注册按钮被单击
     *
     * @param view
     */
    public void onRegisterUserClick(View view) {
        mNickName = mEt_nickName.getText().toString().trim();
        mPassWord = mEt_passWord.getText().toString().trim();
        mCbMan_Flag = mCb_man.isChecked();
        mCbWoman_Flag = mCb_woman.isChecked();
        if (mCbMan_Flag) {//如果选中男生，标记为0
            mCbSexFlag = 0;//男
        } else {
            mCbSexFlag = 1;//女
        }
        mCode = mEt_code.getText().toString().trim();

        if (mNickName == null || mNickName.equals("")) {
            toast("昵称不能为空");
        } else if (mPassWord == null || mPassWord.equals("")) {
            toast("密码不能为空");
        } else if (mPhoneNumber == null || mPhoneNumber.equals("")) {
            toast("号码不能为空");
        } else if (mCbMan_Flag == false && mCbWoman_Flag == false) {
            toast("性别不能为空");
        } else if (mCode == null || mCode.equals("")) {
            toast("请获取验证码");
        } else if (!isCodeCorrect(this,mPhoneNumber,mCode)) {//判断验证码是否正确
            toast("验证码有误");
        } else {
            //将用户信息发送数据至服务器
            sendRegisterMessageToServer(mNickName, mPhoneNumber, mPassWord, mCbSexFlag);
        }

    }


    /**
     * 访问服务器，返回验证码是否正确
     */

    public Boolean isCodeCorrect(Context context ,String mPhoneNumber,String code) {

        BmobSMS.verifySmsCode(context,mPhoneNumber,code , new VerifySMSCodeListener() {
            @Override
            public void done(BmobException ex) {
                // TODO Auto-generated method stub
                if(ex==null){//短信验证码已验证成功
                    Log.i("bmob", "验证通过");
                    flag = true;
                }else{
                    Log.i("bmob", "验证失败：code ="+ex.getErrorCode()+",msg = "+ex.getLocalizedMessage());
                    flag = false;

                }
            }
        });
        return flag;

    }


    /**
     * 将用户注册信息发送至服务器
     */

    public void sendRegisterMessageToServer(final String mNickName, final String mPhoneNumber, final String mPassWord, final int mCbSexFlag) {
        UserInfo userInfoBu = new UserInfo();
        //bmob提供的方法
        userInfoBu.setUsername(mPhoneNumber);
        userInfoBu.setPassword(mPassWord);
        //本地设置的其他属性的方法
        userInfoBu.setNickName(mNickName);
        userInfoBu.setSex(mCbSexFlag);
        //注意：不能用save方法进行注册
        userInfoBu.signUp(new SaveListener<UserInfo>() {
            @Override
            public void done(UserInfo userInfo, cn.bmob.v3.exception.BmobException e) {
                if (e == null) {
                    toast("注册成功");
                    //将密码和用户名保存到本地
                    SaveAndGetUserInfoUtils.saveUsernameAndPassword(getApplicationContext(), mPhoneNumber, mPassWord);
                    //跳转至登录页面
                    jumpToLoginActivity();
                } else {
                    toast("注册失败");
                }
            }

        });
    }


    /**
     * 跳转至登录页面
     */

    public void jumpToLoginActivity() {
        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
        startActivity(intent);
        //广播接受者的注册监听者置为空
        SmsContentReceiver.setOnGetSmsNotifySetSmsListener(null);
        finish();
    }


    /**
     * 监听checkbox
     * 男女性别只可以选择一个
     */
    public void onChoseOneSexClick(View view) {
        if (view.getId() == R.id.act_register_cb_man) {
            mCb_man.setChecked(true);
            mCb_woman.setChecked(false);
        } else if (view.getId() == R.id.act_register_cb_woman) {
            mCb_man.setChecked(false);
            mCb_woman.setChecked(true);
        }
    }

}
