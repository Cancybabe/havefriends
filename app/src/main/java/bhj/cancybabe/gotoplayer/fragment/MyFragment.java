package bhj.cancybabe.gotoplayer.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.io.File;
import java.util.ArrayList;

import bhj.cancybabe.gotoplayer.R;
import bhj.cancybabe.gotoplayer.adpter.MyFraGridViewAdapter;
import bhj.cancybabe.gotoplayer.application.MyApplication;
import bhj.cancybabe.gotoplayer.base.BaseFragment;
import bhj.cancybabe.gotoplayer.bean.UserInfo;
import bhj.cancybabe.gotoplayer.ui.MyImageView;
import bhj.cancybabe.gotoplayer.utils.FindUserInfoUtils;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;
import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by Cancybabe on 2016/11/9.
 */
public class MyFragment extends BaseFragment {
    private View view;
    private GridView gridView;
    private ArrayList<String> datas;
    private MyImageView mTvHead;
    private File saveUserInfoFile = new File("sdcard/userhead.jpg");
    private static final int REQUESD_CODE_SAVEHEAD = 1;
    private UserInfo userInfo;
    private ImageLoader mImageLoader;
    private SweetAlertDialog sweetAlertDialog;
    private TextView tv_myNickName;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_my, null);
        initView();
//        initListener();
        mTvHead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeHeadPic();
            }
        });
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


    }

    /**
     * 注册监听事件
     */
//    private void initListener() {
//        mTvHead.setOnClickListener(this);
//    }

    private void initView() {
        mTvHead = (MyImageView) view.findViewById(R.id.fra_my_head);
        tv_myNickName = (TextView) view.findViewById(R.id.fra_my_tv_mynickname);
        if(MyApplication.userActInfo != null) {
            getHeadPicFromBmob(MyApplication.userInfo.getObjectId());
        }
        datas = new ArrayList<>();
        datas.add("优惠");
        datas.add("收藏");
        datas.add("发起");
        datas.add("订单");
        datas.add("我的");
        datas.add("设置");
        gridView = (GridView) view.findViewById(R.id.fra_my_gridview);
        gridView.setAdapter(new MyFraGridViewAdapter(act, datas));
    }


    /**
     * 从服务器拿用户头像
     *
     * @param objectId 该用户表里的用户id
     */
    private void getHeadPicFromBmob(String objectId) {
        //用户数据初始化
        FindUserInfoUtils.findUserInfo(objectId, new FindUserInfoUtils.getUserInfoListener() {
            @Override
            public void getUserInfo(UserInfo userInfo,BmobException e) {
                if (e == null){
                    Log.i("myTag", userInfo.getNickName());
                    tv_myNickName.setText(userInfo.getNickName());
                    BmobFile userhead = userInfo.getUserHead();
                    String url = userhead.getUrl();
                    Log.i("myTag", url);
                    imageLoaderSaveAndDiaplay(url,mTvHead);
                }
            }
        });
    }


    /**
     * 用imageloader缓存图片并展示到控件上
     * @param url  网络图片地址
     * @param mTvHead  要展示到哪个控件
     */
    public void imageLoaderSaveAndDiaplay(String url, final ImageView mTvHead) {
       /* ImageSize mImageSize = new ImageSize(100, 100);
        ImageLoader.getInstance().loadImage(url, mImageSize,new SimpleImageLoadingListener(){
            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                super.onLoadingComplete(imageUri, view, loadedImage);
               // Log.i("myTag","onLoadingComplete"+imageUri);
                mTvHead.setImageBitmap(loadedImage);
            }
        });*/

        ImageLoader.getInstance().displayImage(url, mTvHead, MyApplication.getSimpleOptions(), new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String s, View view) {

            }

            @Override
            public void onLoadingFailed(String s, View view, FailReason failReason) {

            }

            @Override
            public void onLoadingComplete(String s, View view, Bitmap bitmap) {
                mTvHead.setImageBitmap(bitmap);
            }

            @Override
            public void onLoadingCancelled(String s, View view) {

            }
        });

    }

    /**
     * 单击事件
     *
     * @param view
     */
//    @Override
//    public void onClick(View view) {
//        switch (view.getId()) {
//            case R.id.fra_my_head:
//                //更换头像
//                changeHeadPic();
//                break;
//        }
//    }


    /**
     * 跳转相册更换头像
     */
    private void changeHeadPic() {
        toast("changeHead");
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        //裁剪
        intent.putExtra("crop", "true");
        //裁剪比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        //裁剪像素点
        intent.putExtra("outputX", 300);
        intent.putExtra("outputY", 300);
        //存放位置
        intent.putExtra("output", Uri.fromFile(saveUserInfoFile));
        startActivityForResult(intent, REQUESD_CODE_SAVEHEAD);
    }


    /**
     * 跳转相册之后返回图片
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    public void onActivityResult(@Nullable int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == REQUESD_CODE_SAVEHEAD) {
            Bitmap bitmap = BitmapFactory.decodeFile(saveUserInfoFile.getAbsolutePath());
            //上传图片至服务器
            upLoadHeadImage(bitmap);
        }
    }


    /**
     * 上传用户头像图片
     *
     * @param bitmap 图片
     */
    private void upLoadHeadImage(Bitmap bitmap) {
        // 上传头像
        final BmobFile bmobFile = new BmobFile(saveUserInfoFile);
        bmobFile.uploadblock(new UploadFileListener() {

            @Override
            public void done(BmobException e) {
                if(e==null){
                    //bmobFile.getFileUrl()--返回的上传文件的完整地址
                    toast("上传文件成功:" + bmobFile.getFileUrl());
                    updateBmobUserHeadPic(saveUserInfoFile, bmobFile.getFileUrl());

                }else{
                    toast("上传文件失败：" + e.getMessage());
                }
            }

            @Override
            public void onProgress(Integer value) {
                // 返回的上传进度（百分比）
            }
        });
    }


    /**
     * 更新用户表中图片信息
     *
     * @param saveUserInfoFile 用户新选的头像图片
     */
    private void updateBmobUserHeadPic(final File saveUserInfoFile,String url) {
        UserInfo newUser = new UserInfo();

        final BmobFile file=new BmobFile(saveUserInfoFile.getAbsolutePath(),null,url);
        newUser.setUserHead(file);


        BmobUser bmobUser = BmobUser.getCurrentUser(UserInfo.class);
        newUser.update(bmobUser.getObjectId(),new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if(e==null){
                    toast("更新用户信息成功");
                    setNewUIHeadPic(saveUserInfoFile.getAbsolutePath());
                }else{
                    toast("更新用户信息失败:" + e.getMessage());
                }
            }
        });
    }


    /**
     * 设置本地UI头像
     *
     * @param filePath 裁剪好的图片保存的地址
     */

    private void setNewUIHeadPic(String filePath) {
        Bitmap userHead = BitmapFactory.decodeFile(filePath);
        mTvHead.setImageBitmap(userHead);
    }

}
