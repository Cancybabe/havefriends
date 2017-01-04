package bhj.cancybabe.gotoplayer.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import java.util.List;

import bhj.cancybabe.gotoplayer.R;
import bhj.cancybabe.gotoplayer.base.BaseActivity;
import bhj.cancybabe.gotoplayer.bean.UserActivtiesInfo;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by Cancybabe on 2017/1/3.
 */
public class HomeDetailsActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_homedetails);
        Intent intent = getIntent();
        UserActivtiesInfo userActivtiesInfo = (UserActivtiesInfo) intent.getParcelableExtra("itemdatas");
        String actionPlace = userActivtiesInfo.getActionPlace();  //活动地点
        String actionMoney = userActivtiesInfo.getActionRMB();   //活动金额
        String actionName =  userActivtiesInfo.getActionName(); //活动名
        String actionTime = userActivtiesInfo.getActionTime(); //活动时间
        int actionPraise = userActivtiesInfo.getPraiseUser().size();//点赞数
        String publishUserObjectId =  userActivtiesInfo.getActionUserId(); //活动发起人
        BmobQuery<BmobUser> query = new BmobQuery<BmobUser>();
        query.addWhereEqualTo("objectId", "publishUserObjectId");
        query.findObjects(new FindListener<BmobUser>() {
            @Override
            public void done(List<BmobUser> object, BmobException e) {
                if(e==null){
                    toast("查询用户成功:"+object.size());
                }else{
                    toast("更新用户信息失败:" + e.getMessage());
                }
            }
        });

    }

}
