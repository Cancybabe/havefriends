package bhj.cancybabe.gotoplayer.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import bhj.cancybabe.gotoplayer.R;
import bhj.cancybabe.gotoplayer.base.BaseActivity;
import bhj.cancybabe.gotoplayer.base.BaseInterface;
import bhj.cancybabe.gotoplayer.bean.UserActivtiesInfo;
import bhj.cancybabe.gotoplayer.ui.NumImageView;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by Cancybabe on 2017/1/3.
 */
public class HomeDetailsActivity extends BaseActivity implements BaseInterface {
    private TextView tv_actionPublisher;
    private TextView tv_actionContent;
    private TextView tv_actionMoney;
    private TextView tv_actionDistance;
    private TextView tv_actionTime;
    private NumImageView iv_actionPay;
    private ImageView iv_actionTime;
    private NumImageView iv_actionLike;
    private NumImageView iv_actionYouhui;
//    private String actionPlace;
//    private String actionMoney;
//    private String actionDesc;
//    private String actionTime;
//    private int actionPraise;
//    private String actionPublisher;
//    private String actionDistance;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_homedetails);

        initView();
        initData();
    }

    @Override
    public void initView() {
        tv_actionPublisher = (TextView) findViewById(R.id.act_homedetails_tv_publisher);
        tv_actionContent = (TextView) findViewById(R.id.act_homedetails_tv_content);
        tv_actionMoney = (TextView) findViewById(R.id.act_homedetails_tv_money);
        tv_actionDistance = (TextView) findViewById(R.id.act_homedetails_tv_diatance);
        tv_actionTime = (TextView) findViewById(R.id.act_homedetails_tv_time);

        iv_actionPay = (NumImageView) findViewById(R.id.act_homedetails_iv_pay);
        iv_actionTime = (ImageView) findViewById(R.id.act_homedetails_iv_time);
        iv_actionLike = (NumImageView) findViewById(R.id.act_homedetails_iv_like);
        iv_actionYouhui = (NumImageView) findViewById(R.id.act_homedetails_iv_discount);

    }

    @Override
    public void initListener() {

    }


    //初始化数据
    public void initData(){
        Intent intent = getIntent();
        UserActivtiesInfo userActivtiesInfo = intent.getParcelableExtra("itemdatas");
        String actionPlace = userActivtiesInfo.getActionPlace();  //活动地点
        String actionMoney = userActivtiesInfo.getActionRMB();   //活动金额
        String actionDesc =  userActivtiesInfo.getActionDesc(); //活动描述
        String actionTime = userActivtiesInfo.getActionTime(); //活动时间
        int actionPraise = 0;
        if(userActivtiesInfo.getPraiseUser() != null){
            actionPraise = userActivtiesInfo.getPraiseUser().size();//点赞数
        }
        String publishUserObjectId =  userActivtiesInfo.getActionUserId(); //活动发起人
        Log.i("myTag", "活动发起人"+publishUserObjectId);
        BmobQuery<BmobUser> query = new BmobQuery<BmobUser>();
        query.addWhereEqualTo("objectId", publishUserObjectId);
        query.findObjects(new FindListener<BmobUser>() {
            @Override
            public void done(List<BmobUser> object, BmobException e) {
                if(e==null){
                    String actionPublisher = (String) object.get(0).getObjectByKey("nickName");
                    tv_actionPublisher.setText(actionPublisher);
                }else{
                    toast("更新用户信息失败:" + e.getMessage());
                }
            }
        });



        tv_actionContent.setText(actionDesc);
        tv_actionMoney.setText(actionMoney);
        tv_actionDistance.setText(actionPlace);
        tv_actionTime.setText(actionTime);

        iv_actionLike.setNum(2);
    }
}
