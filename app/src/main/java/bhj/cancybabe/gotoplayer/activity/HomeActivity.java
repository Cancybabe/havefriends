package bhj.cancybabe.gotoplayer.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import bhj.cancybabe.gotoplayer.R;
import bhj.cancybabe.gotoplayer.adpter.MyViewpagerAdapter;
import bhj.cancybabe.gotoplayer.base.BaseActivity;
import bhj.cancybabe.gotoplayer.base.BaseInterface;
import bhj.cancybabe.gotoplayer.fragment.HomeFragment;
import bhj.cancybabe.gotoplayer.fragment.MessageFragment;
import bhj.cancybabe.gotoplayer.fragment.MoreFragment;
import bhj.cancybabe.gotoplayer.fragment.MyFragment;

/**
 * Created by Cancybabe on 2016/11/9.
 */
public class HomeActivity extends BaseActivity implements BaseInterface{
    private LinearLayout[] linearLayouts = new LinearLayout[4];
    private TextView[] textViews = new TextView[4];
    private ImageView[] imageViews = new ImageView[4];
    //private LinearLayout linearLayoutAdd;
    private int[] linearLayoutsId = {R.id.act_home_lin_home, R.id.act_home_lin_message, R.id.act_home_lin_my, R.id.act_home_lin_more};
    private int[] imageViewdId = {R.id.act_home_iv_home, R.id.act_home_iv_message, R.id.act_home_iv_my, R.id.act_home_iv_more};
    private int[] textViewsId = {R.id.act_home_tv_home, R.id.act_home_tv_message, R.id.act_home_tv_my, R.id.act_home_tv_more};
    private int[] imagesPressed = {R.drawable.act_home_home_after, R.drawable.act_home_message_after, R.drawable.act_home_my_after, R.drawable.act_home_more_after};
    private int[] imagesUnPressed = {R.drawable.act_home_home_before, R.drawable.act_home_message_before, R.drawable.act_home_my_before, R.drawable.act_home_more_before};
    private Fragment[] fragments = {new HomeFragment(), new MessageFragment(), new MyFragment(), new MoreFragment()};
    private ViewPager mViewPager;
    ArrayList<Fragment> arraylistFragments = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initView();
        initData();
        initListener();
        mViewPager.setAdapter(new MyViewpagerAdapter(arraylistFragments, getSupportFragmentManager()));
        mViewPager.setOnPageChangeListener(onPageChangeListener);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.toolbar_search:
                Toast.makeText(this,"search",Toast.LENGTH_SHORT).show();
                break;
            case R.id.toolbar_sanner:
                Toast.makeText(this,"scanner",Toast.LENGTH_SHORT).show();
                break;
            case R.id.toolbar_menu:
                Toast.makeText(this,"menu",Toast.LENGTH_SHORT).show();
                break;
        }
        return true;
    }

    /**
     * 底部LinearLayout的监听
     */


    public void onLinearLayoutClick(View view) {
        switch (view.getId()) {
            case R.id.act_home_lin_home:
                changePicAndTextColor(0);
                mViewPager.setCurrentItem(0);
                break;
            case R.id.act_home_lin_message:
                changePicAndTextColor(1);
                mViewPager.setCurrentItem(1);
                break;
            case R.id.act_home_lin_my:
                changePicAndTextColor(2);
                mViewPager.setCurrentItem(2);
                break;
            case R.id.act_home_lin_more:
                changePicAndTextColor(3);
                mViewPager.setCurrentItem(3);
                break;

//            case R.id.act_home_lin_add:
//                toast("act_home_iv_add");
//                Intent intent = new Intent(HomeActivity.this,PublishActionsActivity.class);
//                startActivity(intent);
//                break;
        }
    }

    /**
     * 改变图片和字体颜色
     * @param index
     */
    private void changePicAndTextColor(int index) {

        for (int i = 0;i < 4; i++){
            if (i == index){
                imageViews[index].setImageResource(imagesPressed[i]);
                textViews[index].setTextColor(Color.parseColor("#FF567F"));

            }else {
                imageViews[i].setImageResource(imagesUnPressed[i]);
                textViews[i].setTextColor(Color.parseColor("#a4a4a4"));
            }
        }
    }

    /**
     * viewpager监听
     *
     * @param mViewPager
     *
     *
     */
    ViewPager.OnPageChangeListener onPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            changePicAndTextColor(position);
        }

        @Override
        public void onPageSelected(int position) {

        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };


    /**
     *
     */
    private void initData() {
        for (int i = 0; i < 4; i++) {
            arraylistFragments.add(fragments[i]);
        }
    }

    @Override
    public void initView() {
        for (int i = 0; i < 4; i++) {
            linearLayouts[i] = (LinearLayout) findById(linearLayoutsId[i]);
            textViews[i] = (TextView) findById(textViewsId[i]);
            imageViews[i] = (ImageView) findById(imageViewdId[i]);
            mViewPager = (ViewPager) findViewById(R.id.act_home_viewpager);
            textViews[i].setTextColor(Color.parseColor("#a4a4a4"));
            imageViews[i].setImageResource(imagesUnPressed[i]);
            //默认展示第一个
        }
        //linearLayoutAdd = (LinearLayout) findById(R.id.act_home_lin_add);
        imageViews[0].setImageResource(R.drawable.act_home_home_after);
        textViews[0].setTextColor(Color.parseColor("#FF567F"));
    }

    @Override
    public void initListener() {

    }
}
