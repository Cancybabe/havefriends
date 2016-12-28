package bhj.cancybabe.gotoplayer.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import bhj.cancybabe.gotoplayer.R;
import bhj.cancybabe.gotoplayer.activity.MoreClassActivity;
import bhj.cancybabe.gotoplayer.adpter.MoreFragmentGridViewAdapter;
import bhj.cancybabe.gotoplayer.adpter.MoreFragmentViewPagerAdapter;
import bhj.cancybabe.gotoplayer.application.MyApplication;
import bhj.cancybabe.gotoplayer.base.BaseFragment;
import bhj.cancybabe.gotoplayer.base.BaseInterface;
import bhj.cancybabe.gotoplayer.bean.UserActivtiesInfo;
import bhj.cancybabe.gotoplayer.utils.FindActionInfoUtils;
import cn.bmob.v3.exception.BmobException;

/**
 * Created by Cancybabe on 2016/11/9.
 */
public class MoreFragment extends BaseFragment implements BaseInterface,View.OnClickListener{
    private ViewPager viewPager;
    private TextView tv_intro;
    private LinearLayout dot_layout;
    private GridView mGridView;
    private View view;
    private Button mBtn_free,mBtn_hot,mBtn_near;
    private String[] datas = {"周边","少儿","DIY","健身","赶集","演出",
            "展览","沙龙","品茶","聚会"};

    private int[] imagesId = {R.drawable.a,R.drawable.b,R.drawable.c,R.drawable.d,R.drawable.e};
    private String[] ads = {"巩俐不低俗，我就不能低俗","朴树又回来了，再唱经典老歌引百万人同唱啊","揭秘北京电影如何升级",
            "乐视网TV版大放送","热血屌丝的反杀"};

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
           viewPager.setCurrentItem(viewPager.getCurrentItem()+1);
            handler.sendEmptyMessageDelayed(0,2000);
        }
    };
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_more,null);
        initView();
        initData();
        initListener();
        return view;
    }

    @Override
    public void initView() {
        mGridView = (GridView) view.findViewById(R.id.fra_more_gd);
        viewPager = (ViewPager) view.findViewById(R.id.viewPager);
        tv_intro = (TextView) view.findViewById(R.id.tv_intro);
        dot_layout = (LinearLayout) view.findViewById(R.id.dot_layout);
        mBtn_free = (Button) view.findViewById(R.id.act_moreclass_btn_free);
        mBtn_hot = (Button) view.findViewById(R.id.act_moreclass_btn_hot);
        mBtn_near = (Button) view.findViewById(R.id.act_moreclass_btn_near);
        initDots();
        viewPager.setCurrentItem(Integer.MAX_VALUE/2-Integer.MAX_VALUE%imagesId.length);
    }


    private void initData() {
        mGridView.setAdapter(new MoreFragmentGridViewAdapter(act,datas));
        viewPager.setAdapter(new MoreFragmentViewPagerAdapter(act,imagesId,ads));
        tv_intro.setText(ads[0]);
        handler.sendEmptyMessageDelayed(0,2000);
    }

    /**
     * 初始化ViewPager点
     */
    private void initDots(){
        for (int i = 0; i < imagesId.length; i++) {
            View view = new View(act);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(8, 8);
            if(i!=0){
                params.leftMargin = 5;
            }
            view.setLayoutParams(params);
            view.setBackgroundResource(R.drawable.selector_dot);
            dot_layout.addView(view);
        }
        dot_layout.getChildAt(0).setEnabled(true);

    }

    @Override
    public void initListener() {
        mBtn_free.setOnClickListener(this);
        mBtn_hot.setOnClickListener(this);
        mBtn_near.setOnClickListener(this);

        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

               final String actionClass =  datas[i] ;
                FindActionInfoUtils.findAllUserInfo(4, actionClass, 0, 0, new FindActionInfoUtils.findAllActionInfoListener() {
                    @Override
                    public void getActionInfo(List<UserActivtiesInfo> activtiesInfos, BmobException ex) {

                        if (ex == null){
                            MyApplication.userActInfo.put("actionClass",activtiesInfos);
                            Intent intent = new Intent(act, MoreClassActivity.class);
                            intent.putExtra("actionClass",actionClass);
                            startActivity(intent);
                        }

                    }
                });
            }
        });




        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                updateIntroAndDot(position%5);
            }
            @Override
            public void onPageScrolled(int position, float positionOffset,
                                       int positionOffsetPixels) {
            }
            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }


    /**
     * 更新文本
     */
    private void updateIntroAndDot(int position){
        tv_intro.setText(ads[position]);
        for (int i = 0; i < dot_layout.getChildCount(); i++) {
            dot_layout.getChildAt(i).setEnabled(i==position);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.act_moreclass_btn_free:
                findActionByClass(5,"0");
                break;
            case R.id.act_moreclass_btn_hot:
                break;
            case R.id.act_moreclass_btn_near:
                break;
        }
    }

    /**
     * 免费、热门、附近
     */
    public void findActionByClass(int type,String keyword){
           FindActionInfoUtils.findAllUserInfo(type, keyword, 0, 0, new FindActionInfoUtils.findAllActionInfoListener() {
               @Override
               public void getActionInfo(List<UserActivtiesInfo> activtiesInfos, BmobException ex) {
                   if (ex == null){
                       MyApplication.userActInfo.put("actionClass",activtiesInfos);
                       Intent intent = new Intent(act, MoreClassActivity.class);
                       intent.putExtra("actionClass","免费");
                       startActivity(intent);
                   }
               }
           });

    }
}





