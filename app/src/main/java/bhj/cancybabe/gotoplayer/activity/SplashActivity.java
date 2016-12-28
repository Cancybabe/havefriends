package bhj.cancybabe.gotoplayer.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.util.HashMap;
import java.util.List;

import bhj.cancybabe.gotoplayer.R;
import bhj.cancybabe.gotoplayer.application.MyApplication;
import bhj.cancybabe.gotoplayer.base.BaseActivity;
import bhj.cancybabe.gotoplayer.bean.UserActivtiesInfo;
import bhj.cancybabe.gotoplayer.bean.UserInfo;
import bhj.cancybabe.gotoplayer.utils.FindActionInfoUtils;
import bhj.cancybabe.gotoplayer.utils.FindUserInfoUtils;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.DownloadFileListener;

/**
 * 闪屏界面
 */
public class SplashActivity extends BaseActivity {
    private ImageView imageView;
    private static final int CODE_ENTER_LOGINACT = 1;
    private AlertDialog.Builder builder;
    private  Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case CODE_ENTER_LOGINACT:
                    enterLoginAct();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        initView();
        startAnimation();
    }

    /**
     * 去系统设置WIFI之后再回到之前的界面，再次判断是否打开网络
     */
    @Override
    protected void onRestart() {
        super.onRestart();
        if (isNetConnected()) {

            waitToEnterLoginAct();
        } else {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            showSettingNetDialog();
        }
    }

    /**
     * 初始化控件
     */
    private void initView() {
        imageView = (ImageView) findViewById(R.id.spl_iv_anim);
    }

    /**
     * 开启开启应用动画，并设置动画监听
     */
    public void startAnimation() {
        AlphaAnimation alphaAnimation = (AlphaAnimation) AnimationUtils.loadAnimation(this, R.anim.anim_alpha);
        imageView.startAnimation(alphaAnimation);
        alphaAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                //去判断手机当前是否有网络
                boolean isConn = isNetConnected();
                if (isConn) {
                    //从Bmob获取数据
                    FindActionInfoUtils.findAllUserInfo(1, null, 0, 0, new FindActionInfoUtils.findAllActionInfoListener() {
                        @Override
                        public void getActionInfo(List<UserActivtiesInfo> activtiesInfos, BmobException ex) {
                            if (ex == null){
                                MyApplication.userActInfo.put("AllActInfos",activtiesInfos);


                                //用户昵称集合

                                final HashMap<String,String> userNickName = new HashMap<String, String>();



                                for (int i = 0; i < activtiesInfos.size();i++) {
                                    UserActivtiesInfo actInfo = activtiesInfos.get(i);

                                    String picUrl = actInfo.getActionPic().get(0).getUrl();
                                    Log.i("myTag", picUrl);

                                    String fileUrl = picUrl.substring(picUrl.lastIndexOf("/") + 1, picUrl.length() - 4);
                                    Log.i("myTag", fileUrl);

                                    File actionFile = new File("sdcard/gotoplayActionPic" + fileUrl + ".png");

                                    if (!actionFile.exists()){
                                    //下载活动图片
                                    actInfo.getActionPic().get(0).download(actionFile, new DownloadFileListener() {
                                        @Override
                                        public void done(String path, BmobException e) {
                                            if (e == null) {
                                                toast("活动图片下载成功");
                                            }
                                        }

                                        @Override
                                        public void onProgress(Integer integer, long l) {

                                        }
                                    });

                                    }
                                    //获取头像和昵称
                                    FindUserInfoUtils.findUserInfo(actInfo.getActionUserId(), new FindUserInfoUtils.getUserInfoListener() {

                                        @Override
                                        public void getUserInfo(UserInfo userInfo, BmobException e) {
                                            //获取发布者的昵称
                                            userNickName.put(userInfo.getObjectId(),userInfo.getNickName());
                                            MyApplication.userActInfo.put("UserNickName",userNickName);

                                            //获取发布者的头像
                                            String picUrl = userInfo.getUserHead().getUrl();
                                            String fileUrl = picUrl.substring(picUrl.lastIndexOf("/"),picUrl.length() - 3);
                                            File actionFile = new File("sdcard/gotoplayUserHeadPic"+fileUrl+".png");
                                            if (!actionFile.exists()){
                                                userInfo.getUserHead().download(actionFile,new DownloadFileListener() {
                                                    @Override
                                                    public void done(String path, BmobException e) {
                                                        if (e == null){
                                                            toast("活动用户头像下载成功");
                                                        }
                                                    }
                                                    @Override
                                                    public void onProgress(Integer integer, long l) {

                                                    }
                                                });
                                            }

                                        }
                                    });

                                }

                            }else {

                                toast("服务器获取数据失败");
                            }

                        }
                    });



                    waitToEnterLoginAct();
                } else {
                    //提示框提示打开网络
                    showSettingNetDialog();
                }




            }

            @Override
            public void onAnimationEnd(Animation animation) {
//                enterHome();

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    /**
     * 提示框提示设置网络状态
     */
    private void showSettingNetDialog() {

        builder = new AlertDialog.Builder(this);
        builder.setTitle("当前无网络");
        builder.setCancelable(false);
        builder.setMessage("去设置网络");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //跳转至系统设置网络的界面
                goToSetInternet();
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(SplashActivity.this, "当前无网络", Toast.LENGTH_SHORT).show();
                waitToEnterLoginAct();
            }
        });
        builder.show();

    }

    /**
     * 跳转至系统设置网络的界面
     */
    private void goToSetInternet() {
        Intent netSettingsIntent = new Intent("android.settings.WIFI_SETTINGS");
        startActivity(netSettingsIntent);
    }

    /**
     * 判断当前手机是否有网络
     */
    private boolean isNetConnected() {
        ConnectivityManager connManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connManager.getActiveNetworkInfo();
        if (networkInfo != null) {
            if (networkInfo.isConnected())
                return true;
        }
        return false;
    }

    public void enterLoginAct(){
        Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }


    /**
     * 睡眠两秒进入主界面
     */
    private void waitToEnterLoginAct() {
        new Thread() {
            @Override
            public void run() {
               Message message =  handler.obtainMessage();
                try {
                    Thread.sleep(2000);
                    message.what = CODE_ENTER_LOGINACT;
                    handler.sendMessage(message);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }.start();



    }


}
