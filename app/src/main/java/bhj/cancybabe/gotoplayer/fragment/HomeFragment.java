package bhj.cancybabe.gotoplayer.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import bhj.cancybabe.gotoplayer.R;
import bhj.cancybabe.gotoplayer.adpter.HomeFraListViewADapter;
import bhj.cancybabe.gotoplayer.application.MyApplication;
import bhj.cancybabe.gotoplayer.base.BaseFragment;
import bhj.cancybabe.gotoplayer.base.BaseInterface;
import bhj.cancybabe.gotoplayer.bean.UserActivtiesInfo;
import bhj.cancybabe.gotoplayer.bean.UserInfo;
import bhj.cancybabe.gotoplayer.ui.MyImageView;
import bhj.cancybabe.gotoplayer.ui.YIListView;
import bhj.cancybabe.gotoplayer.utils.FindActionInfoUtils;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by Cancybabe on 2016/11/9.
 */
public class HomeFragment extends BaseFragment implements BaseInterface,View.OnClickListener{
    private EditText mEtSearch;
    private ImageView mIvSearch;
    private YIListView mListView;
    private TextView mTvActionUserName,mTVActionTimer,mTvActionMoney,mTvActionDistance,mTvActionDetails;
    private MyImageView mIvHead;
    private ImageView mIvLike;
    private ArrayList<UserActivtiesInfo> datas;
    View view;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home, null);
        initView();
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();
        initOperations();
    }

    /**
     * 初始化数据
     */
    private void initData() {
        datas = new ArrayList<>();
        datas.add(new UserActivtiesInfo());
        datas.add(new UserActivtiesInfo());
        datas.add(new UserActivtiesInfo());
        datas.add(new UserActivtiesInfo());
        datas.add(new UserActivtiesInfo());
    }

    private void initOperations() {
        mIvSearch.setOnClickListener(this);
        ArrayList<UserActivtiesInfo> actInfos = (ArrayList<UserActivtiesInfo>) MyApplication.userActInfo.get("AllActInfos");
       // mListView.setPullLoadEnable(true);
        final HomeFraListViewADapter adapter = new HomeFraListViewADapter(act, actInfos);
        mListView.setAdapter(adapter);

        //listview子项的点击监听
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                ArrayList<UserActivtiesInfo>  activtiesInfos = (ArrayList<UserActivtiesInfo>) MyApplication.userActInfo.get("AllActInfos");

            }
        });

        //listview的下拉刷新和上拉加载
        mListView.setOnReflushOperationsListener(new YIListView.ReflushOperationsListener() {
            @Override
            public void onReflushOperations() {
                toast("刷新中.....");
                FindActionInfoUtils.findAllUserInfo(1, null, 0, 0, new FindActionInfoUtils.findAllActionInfoListener() {
                    @Override
                    public void getActionInfo(List<UserActivtiesInfo> activtiesInfos, BmobException ex) {
                        MyApplication.userActInfo.put("AllActInfos",activtiesInfos);
                        adapter.updateData((ArrayList<UserActivtiesInfo>) activtiesInfos);
                        mListView.completeReflush();
                    }
                });
            }

            @Override
            public void onLodingMore() {

                toast("加载更多中...");
                FindActionInfoUtils.findAllUserInfo(2, null, 3, 3, new FindActionInfoUtils.findAllActionInfoListener() {
                    @Override
                    public void getActionInfo(List<UserActivtiesInfo> activtiesInfos, BmobException ex) {
                        MyApplication.userActInfo.put("AllActInfos",activtiesInfos);
                        adapter.updateData((ArrayList<UserActivtiesInfo>) activtiesInfos);
                        mListView.completeReflush();
                        mListView.setSelection(0);
                    }
                });

            }
        });

    }

    /**
     * 从服务器获取用户的昵称
     */
    private void  getPublishUserName(String userID) {
        BmobQuery<UserInfo> query = new BmobQuery<UserInfo>();
        query.addWhereEqualTo("username", userID);
        query.findObjects(new FindListener<UserInfo>() {
            @Override
            public void done(List<UserInfo> object, BmobException e) {
                if(e==null){
//                    toast("查询用户成功:"+object.size());
                    UserInfo  userinfo = object.get(0);
                    String nickName = userinfo.getNickName();

                }else{
//                    toast("更新用户信息失败:" + e.getMessage());

                }
            }
        });
    }



    @Override
    public void initView() {
        mIvSearch = (ImageView) view.findViewById(R.id.fra_iv_search);
        mEtSearch = (EditText) view.findViewById(R.id.fra_ed_search);
        mListView = (YIListView) view.findViewById(R.id.fra_home_listview);
        mTvActionUserName = (TextView) view.findViewById(R.id.fra_home_lv_tv_name);
        mTVActionTimer = (TextView) view.findViewById(R.id.fra_home_lv_tv_time);
        mTvActionMoney = (TextView) view.findViewById(R.id.fra_home_lv_tv_money);
        mTvActionDistance = (TextView) view.findViewById(R.id.fra_home_lv_tv_distance);
        mTvActionDetails = (TextView) view.findViewById(R.id.fra_home_lv_tv_actionDetails);
        mIvHead = (MyImageView) view.findViewById(R.id.fra_home_iv_head);
        mIvLike = (ImageView) view.findViewById(R.id.fra_home_lv_iv_like);
    }


    @Override
    public void initListener() {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fra_iv_search:
                mIvSearch.setVisibility(View.INVISIBLE);
                mEtSearch.setVisibility(View.VISIBLE);
        }
    }

}
