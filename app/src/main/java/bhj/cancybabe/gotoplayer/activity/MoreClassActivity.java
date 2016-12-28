package bhj.cancybabe.gotoplayer.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import bhj.cancybabe.gotoplayer.R;
import bhj.cancybabe.gotoplayer.adpter.HomeFraListViewADapter;
import bhj.cancybabe.gotoplayer.application.MyApplication;
import bhj.cancybabe.gotoplayer.base.BaseActivity;
import bhj.cancybabe.gotoplayer.base.BaseInterface;
import bhj.cancybabe.gotoplayer.bean.UserActivtiesInfo;

/**
 * Created by Cancybabe on 2016/11/19.
 */
public class MoreClassActivity extends BaseActivity implements BaseInterface{
    private ListView listView;
    private TextView tvTitle;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.act_moreclass);
        super.onCreate(savedInstanceState);
        initView();
        initData();
        initListener();
    }


    @Override
    public void initView() {
        String actionClass = getIntent().getStringExtra("actionClass");
        listView = (ListView) findViewById(R.id.act_moreclass_listview);
        tvTitle = (TextView) findViewById(R.id.act_moreclass_tv_title);
        tvTitle.setText("搜索"+actionClass+"的结果");
    }

    private void initData() {
        ArrayList<UserActivtiesInfo> actionClass = (ArrayList<UserActivtiesInfo>) MyApplication.userActInfo.get("actionClass");

        if (actionClass == null){
            actionClass = new ArrayList<>();
        }
        listView.setAdapter(new HomeFraListViewADapter(this,actionClass));
    }


    @Override
    public void initListener() {

    }
}
