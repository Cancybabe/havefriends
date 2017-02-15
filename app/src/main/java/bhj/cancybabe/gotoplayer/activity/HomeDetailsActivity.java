package bhj.cancybabe.gotoplayer.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import bhj.cancybabe.gotoplayer.R;
import bhj.cancybabe.gotoplayer.application.MyApplication;
import bhj.cancybabe.gotoplayer.base.BaseActivity;
import bhj.cancybabe.gotoplayer.base.BaseInterface;
import bhj.cancybabe.gotoplayer.bean.UserActivtiesInfo;
import bhj.cancybabe.gotoplayer.ui.NumImageView;

/**
 * Created by Cancybabe on 2017/1/3.
 */
public class HomeDetailsActivity extends BaseActivity implements BaseInterface {
    //private TextView tv_actionPublisher;
    private TextView tv_actionContent;
    private TextView tv_actionMoney;
    private TextView tv_actionDistance;
    private TextView tv_actionTime;
    //private NumImageView iv_actionPay;
    //private ImageView iv_actionTime;
    private NumImageView iv_actionLike;//自定义ImageView,收藏
    //private NumImageView iv_actionYouhui;
   // private GridView gv_actionPics;
    //private ImageView iv_actionPic;
    private Toolbar toolbar;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private ImageView imageViewToolBar;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_homedetail);

        initView();
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }



        initData();
    }

    @Override
    public void initView() {
        //tv_actionPublisher = (TextView) findViewById(R.id.act_homedetails_tv_publisher);
        tv_actionContent = (TextView) findViewById(R.id.act_homedetails_tv_content);
        tv_actionMoney = (TextView) findViewById(R.id.act_homedetails_tv_money);
        tv_actionDistance = (TextView) findViewById(R.id.act_homedetails_tv_diatance);
        tv_actionTime = (TextView) findViewById(R.id.act_homedetails_tv_time);

        //iv_actionPay = (NumImageView) findViewById(R.id.act_homedetails_iv_pay);
        //iv_actionTime = (ImageView) findViewById(R.id.act_homedetails_iv_time);
        iv_actionLike = (NumImageView) findViewById(R.id.act_homedetails_iv_like);
        //iv_actionYouhui = (NumImageView) findViewById(R.id.act_homedetails_iv_discount);

       // gv_actionPics = (GridView) findViewById(R.id.fra_homedetails_gv);
        //iv_actionPic = (ImageView) findViewById(R.id.fra_homedetails_iv_one);
        toolbar = (Toolbar) findViewById(R.id.act_detail_toobar);
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.act_detail_collapseLayout);
        imageViewToolBar = (ImageView) findViewById(R.id.act_detail_bar_iv);
    }

    @Override
    public void initListener() {

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //初始化数据
    public void initData(){
        Intent intent = getIntent();
        //获取当前Item的所有数据
        UserActivtiesInfo userActivtiesInfo = intent.getParcelableExtra("itemdatas");
        String actionPlace = userActivtiesInfo.getActionPlace();  //活动地点
        String actionMoney = userActivtiesInfo.getActionRMB();   //活动金额
        String actionDesc =  userActivtiesInfo.getActionDesc(); //活动描述
        String actionTime = userActivtiesInfo.getActionTime(); //活动时间
        String actionName = userActivtiesInfo.getActionName();//活动名称
        int actionPraise = 0;
        if(userActivtiesInfo.getPraiseUser() != null){
            actionPraise = userActivtiesInfo.getPraiseUser().size();//点赞数
        }
//        String publishUserObjectId =  userActivtiesInfo.getActionUserId(); //活动发起人
//        Log.i("myTag", "活动发起人"+publishUserObjectId);
//        BmobQuery<BmobUser> query = new BmobQuery<BmobUser>();
//        query.addWhereEqualTo("objectId", publishUserObjectId);
//        query.findObjects(new FindListener<BmobUser>() {
//            @Override
//            public void done(List<BmobUser> object, BmobException e) {
//                if(e==null){
//                    String actionPublisher = (String) object.get(0).getObjectByKey("nickName");
//                    tv_actionPublisher.setText(actionPublisher);
//                }else{
//                    toast("更新用户信息失败:" + e.getMessage());
//                }
//            }
//        });



        tv_actionContent.setText(actionDesc);
        tv_actionMoney.setText(actionMoney);
        tv_actionDistance.setText(actionPlace);
        tv_actionTime.setText(actionTime);
        iv_actionLike.setNum(actionPraise);
        collapsingToolbarLayout.setTitle(actionName);


        //标题栏图片
        String tooBarPicUrl = userActivtiesInfo.getActionPic().get(0).getUrl();
        imageLoaderSaveAndDiaplay(tooBarPicUrl, imageViewToolBar);


//        //图片的三级缓存
//        //下载所有的图片,如果当前图片已经存在则直接展示，否则从网络下载之后保存到本地再展示
//        if(userActivtiesInfo.getActionPic().size() == 1){
//            gv_actionPics.setVisibility(View.INVISIBLE);
//            iv_actionPic.setVisibility(View.VISIBLE);
//            String picUrl = userActivtiesInfo.getActionPic().get(0).getUrl();
//            imageLoaderSaveAndDiaplay(picUrl, iv_actionPic);
//
//        }else{
//            gv_actionPics.setVisibility(View.VISIBLE);
//            iv_actionPic.setVisibility(View.INVISIBLE);
//            gv_actionPics.setAdapter(new HomeFraListGidItemAdapter(this,userActivtiesInfo));
//        }

    }


    /**
     * 用imageloader缓存图片并展示到控件上
     * @param url  网络图片地址
     * @param iv  要展示到哪个控件
     */
    public void imageLoaderSaveAndDiaplay(String url, final ImageView iv) {
        ImageLoader.getInstance().displayImage(url, iv, MyApplication.getSimpleOptions(), new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String s, View view) {

            }

            @Override
            public void onLoadingFailed(String s, View view, FailReason failReason) {

            }

            @Override
            public void onLoadingComplete(String s, View view, Bitmap bitmap) {
                iv.setImageBitmap(bitmap);
            }

            @Override
            public void onLoadingCancelled(String s, View view) {

            }
        });

    }
}
