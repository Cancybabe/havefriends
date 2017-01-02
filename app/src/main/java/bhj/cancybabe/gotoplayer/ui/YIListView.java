package bhj.cancybabe.gotoplayer.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

import bhj.cancybabe.gotoplayer.R;


/**
 * Created by Cancybabe on 2016/11/21.
 */
public class YIListView extends ListView implements AbsListView.OnScrollListener{
    private View headView;
    private View footerView;
    private int headHeight;
    private int footerHeight;
    private int startY = 0;
    private ImageView ivArrow;
    private TextView lvState;
    private TextView lvTime;
    private final  int PULLTOREFRESH = 1;
    private final int RELESETOREFLASH = 2;
    private final int REFLASHING = 3;
    private int state;
    private ProgressBar pb;

    //代码中使用时用的构造
    public YIListView(Context context) {
        super(context);
        initView();
    }

    //布局中使用的构造
    public YIListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public void initView(){
        headView =  LayoutInflater.from(getContext()).inflate(R.layout.headerview,null);
        footerView = LayoutInflater.from(getContext()).inflate(R.layout.footerview,null);
        ivArrow = (ImageView) headView.findViewById(R.id.iv_arrow);
        lvState = (TextView) headView.findViewById(R.id.tv_state);
        lvTime = (TextView) headView.findViewById(R.id.tv_time);
        pb = (ProgressBar) headView.findViewById(R.id.pb_rotate);
        addHeaderView(headView);
        addFooterView(footerView);
        headView.measure(0,0);//通知系统去测量
        headHeight =  headView.getMeasuredHeight();
        headView.setPadding(0,-headHeight,0,0);
        footerView.measure(0,0);
        footerHeight = footerView.getMeasuredHeight();
        footerView.setPadding(0,-footerHeight,0,0);
        setOnScrollListener(this);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()){
            case  MotionEvent.ACTION_DOWN:
                state = PULLTOREFRESH;
                startY =   (int) ev.getY();
                break;
            case  MotionEvent.ACTION_MOVE:
                int moveY = (int) (ev.getY() - startY);
                int paddingTop = -headHeight + moveY;
                if (paddingTop > -headHeight && getFirstVisiblePosition()==0){
                    if(paddingTop > 0 && state != RELESETOREFLASH){//如果下拉的距离超过自身
                        state = RELESETOREFLASH;
                        refreshHeadView(state);
                    }else if(paddingTop <= 0 && state != PULLTOREFRESH){//下拉的距离没有超过自身，还是下拉状态
                        state = PULLTOREFRESH;
                        refreshHeadView(state);
                    }
                    headView.setPadding(0,paddingTop,0,0);
                    return true;//阻止listview的滑动事件
                }
                break;
            case  MotionEvent.ACTION_UP:
                if (state == PULLTOREFRESH){
                    headView.setPadding(0,-headHeight,0,0);
                }else if(state == RELESETOREFLASH){
                    headView.setPadding(0,0,0,0);
                    state = REFLASHING;
                    refreshHeadView(state);
                }
                break;
        }
        return super.onTouchEvent(ev);
    }

    public void refreshHeadView(int state){
        switch(state){
            case PULLTOREFRESH:
                //下拉刷新
                lvState.setText("下拉刷新");
                RotateAnimation rotateAnima = new RotateAnimation(-180,-360, Animation.RELATIVE_TO_SELF,0.5f,
                        Animation.RELATIVE_TO_SELF,0.5f);
                rotateAnima.setFillAfter(true);
                rotateAnima.setDuration(300);
                ivArrow.startAnimation(rotateAnima);
                break;
            case RELESETOREFLASH:
                //释放刷新
                lvState.setText("松开刷新");
                RotateAnimation rotateAnima2 = new RotateAnimation(0,-180, Animation.RELATIVE_TO_SELF,0.5f,
                        Animation.RELATIVE_TO_SELF,0.5f);
                rotateAnima2.setFillAfter(true);
                rotateAnima2.setDuration(300);
                ivArrow.startAnimation(rotateAnima2);
                break;
            case REFLASHING:
                //正在刷新
                ivArrow.clearAnimation();
                lvState.setText("正在刷新");
                ivArrow.setVisibility(View.INVISIBLE);
                pb.setVisibility(View.VISIBLE);
                //暴露接口给外部做刷新操作
                listener.onReflushOperations();
                break;
        }

    }


    public void completeReflush(){
        if (isLoadingMore == false){
            state = PULLTOREFRESH;
            headView.setPadding(0,-headHeight,0,0);
            lvState.setText("下拉刷新");
            ivArrow.setVisibility(View.VISIBLE);
            pb.setVisibility(View.INVISIBLE);
            lvTime.setText("最后刷新："+getCurrentTime());
        }else{
            footerView.setPadding(0,-footerHeight,0,0);
            isLoadingMore = false;//加载完的时候改变标记
        }
    }


    public String getCurrentTime(){
        SimpleDateFormat simpleDateformat = new SimpleDateFormat("yy-MM-dd HH:mm:ss");
        String time= simpleDateformat.format(new Date());
        return time;
    }

    ReflushOperationsListener listener;
    private boolean isLoadingMore = false;
    @Override
    public void onScrollStateChanged(AbsListView absListView, int i) {
        if (i == OnScrollListener.SCROLL_STATE_IDLE && getLastVisiblePosition() == (getCount()-1)){
            if (isLoadingMore == false) {
                isLoadingMore = true;
                footerView.setPadding(0, 0, 0, 0);
                setSelection(getCount());
                listener.onLodingMore();
            }
        }
    }

    @Override
    public void onScroll(AbsListView absListView, int i, int i1, int i2) {

    }

    public interface  ReflushOperationsListener{
        void onReflushOperations();
        void onLodingMore();
    }

    public void setOnReflushOperationsListener(ReflushOperationsListener listener){
        this.listener = listener;
    }

}
