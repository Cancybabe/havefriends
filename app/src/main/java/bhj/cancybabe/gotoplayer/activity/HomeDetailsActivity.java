package bhj.cancybabe.gotoplayer.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.TextView;

import java.util.List;

import bhj.cancybabe.gotoplayer.R;
import bhj.cancybabe.gotoplayer.base.BaseActivity;
import bhj.cancybabe.gotoplayer.base.BaseInterface;
import bhj.cancybabe.gotoplayer.bean.UserActivtiesInfo;
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
    private String actionPlace;
    private String actionMoney;
    private String actionDesc;
    private String actionTime;
    private int actionPraise;
    private String actionPublisher;
    private String actionDistance;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_homedetails);
        initData();
        initView();
    }

    @Override
    public void initView() {
        tv_actionPublisher = (TextView) findViewById(R.id.act_homedetails_tv_publisher);
        tv_actionContent = (TextView) findViewById(R.id.act_homedetails_tv_content);
        tv_actionMoney = (TextView) findViewById(R.id.act_homedetails_tv_money);
        tv_actionDistance = (TextView) findViewById(R.id.act_homedetails_tv_diatance);
        tv_actionTime = (TextView) findViewById(R.id.act_homedetails_tv_time);

        tv_actionPublisher.setText(actionPublisher);
        tv_actionContent.setText(actionDesc);
        tv_actionMoney.setText(actionMoney);
        tv_actionDistance.setText(actionPlace);
        tv_actionTime.setText(actionTime);


    }

    @Override
    public void initListener() {

    }


    //初始化数据
    public void initData(){
        Intent intent = getIntent();
        UserActivtiesInfo userActivtiesInfo = (UserActivtiesInfo) intent.getParcelableExtra("itemdatas");
        actionPlace = userActivtiesInfo.getActionPlace();  //活动地点
        actionMoney = userActivtiesInfo.getActionRMB();   //活动金额
        actionDesc =  userActivtiesInfo.getActionDesc(); //活动描述
        actionTime = userActivtiesInfo.getActionTime(); //活动时间
        actionPraise = userActivtiesInfo.getPraiseUser().size();//点赞数
        String publishUserObjectId =  userActivtiesInfo.getActionUserId(); //活动发起人
        //actionDistance = userActivtiesInfo.get
        Log.i("myTag", "活动发起人"+publishUserObjectId);
        BmobQuery<BmobUser> query = new BmobQuery<BmobUser>();
        query.addWhereEqualTo("objectId", publishUserObjectId);
        query.findObjects(new FindListener<BmobUser>() {
            @Override
            public void done(List<BmobUser> object, BmobException e) {
                if(e==null){
                    actionPublisher = (String) object.get(0).getObjectByKey("nickName");
                   // toast("发起活动者为:"+ nickName);
                }else{
                    toast("更新用户信息失败:" + e.getMessage());
                }
            }
        });
    }
}
