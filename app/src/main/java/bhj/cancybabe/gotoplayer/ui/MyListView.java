package bhj.cancybabe.gotoplayer.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import bhj.cancybabe.gotoplayer.R;

/**
 * Created by Cancybabe on 2016/11/16.
 */
public class MyListView extends ListView implements AbsListView.OnScrollListener{
    View headerView;
    private int headerHeight;

    private int firstVisibleItem;//当前第一个可见的item位置
    int scrollState;

    public MyListView(Context context) {
        super(context);
        initView(context);
    }

    public MyListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public MyListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    public void initView(Context context){
        headerView = LayoutInflater.from(context).inflate(R.layout.mylistview_header,null);
        measureView(headerView);
        headerHeight = headerView.getMeasuredHeight();
        topPadding(-headerHeight);
        this.addHeaderView(headerView);//将头部布局添加到listview中
        this.setOnScrollListener(this);
    }


    public void topPadding(int toppadding){
        headerView.setPadding(headerView.getPaddingLeft(),toppadding,headerView.getRight(),headerView.getBottom());
        headerView.invalidate();
    }

    /**
     * 通知父布局占多大的地方
     */
    public void measureView(View view){
        ViewGroup.LayoutParams params = view.getLayoutParams();

        if (params == null){
            params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }


        int width = ViewGroup.getChildMeasureSpec(0,0,params.width);

        int height;
        int tempHeight = params.height;
        if (tempHeight > 0){//listview高度大于0

            //MeasureSpec封装了父布局对子布局的要求
            height = MeasureSpec.makeMeasureSpec(tempHeight,MeasureSpec.EXACTLY);

        }else {
            height = MeasureSpec.makeMeasureSpec(0,MeasureSpec.UNSPECIFIED);
        }

        headerView.measure(width,height);
    }

    @Override
    public void onScrollStateChanged(AbsListView absListView, int scrollState) {
        this.scrollState = scrollState;
    }

    @Override
    public void onScroll(AbsListView absListView, int firstVisibleItem, int i1, int i2) {
            this.firstVisibleItem = firstVisibleItem;
    }

    Boolean isLvTopRemark;//在listview最顶端
    int startY = 0;


    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()){
            case MotionEvent.ACTION_DOWN:
                Log.i("myTag","ACTION_DOWN");
                Log.i("myTag","ACTION_DOWN"+firstVisibleItem);
                if (firstVisibleItem == 0){
                    Log.i("myTag","ACTION_DOWN"+firstVisibleItem);
                    isLvTopRemark = true;
                    startY = (int) ev.getY();
                }
                break;
            case MotionEvent.ACTION_MOVE:
                Log.i("myTag","ACTION_MOVE");
                onMove(ev);
                break;
            case MotionEvent.ACTION_UP:
                Log.i("myTag","ACTION_UP");
                //用户松开，提示正在刷新
                if (state == RELESE){
                    state = RELESEING;
                    reflashViewByState();
                    //加载最新数据
                }else if (state == PULL){
                    state = NONE;
                    isLvTopRemark = false;
                    reflashViewByState();
                }
                break;

        }
        return super.onTouchEvent(ev);
    }

    /**
     * 判断移动过程的操作
     * @param me
     */
    int state = 0;//当前状态
    final int NONE = 0;//正常状态
    final int PULL = 1;//提示下拉可刷新状态
    final int RELESE = 2;//提示松开可刷新状态
    final int RELESEING = 3;//正在刷新状态
    public void onMove(MotionEvent ev){
        if (!isLvTopRemark){
            return;
        }
        int tempY = (int) ev.getY();
        Log.i("myTag","tempY"+tempY);
        int space = tempY - startY;
        int topPading = space - headerHeight; //下拉的过程中不断设置toppading
        switch (state){
            case NONE:
                //拉的距离大于0时，改为拉的状态
                if (space > 0){
                    Log.i("myTag","onMove+NONE:space > 0");
                    state = PULL;
                    reflashViewByState();
                }
                break;
            case PULL:
                //当用户拉到一定的高度，并且还在继续拉时，提示释放
                Log.i("myTag","onMove+PULL");
                topPadding(topPading);
                if (space > headerHeight + 30  && scrollState == SCROLL_STATE_TOUCH_SCROLL){
                    state = RELESE;
                    reflashViewByState();
                }
                break;
            case RELESE:
                topPadding(topPading);
                if (space < headerHeight + 30 ){
                    state = PULL;
                    reflashViewByState();
                }else if (space <= 0){
                    state = NONE;
                    isLvTopRemark = false;
                    reflashViewByState();
                }
                break;
        }
    }


    /**
     * 根据当前状态，改变界面显示
     */
    private void reflashViewByState(){


        TextView tip = (TextView) headerView.findViewById(R.id.mylistview_tv_hint);
        ImageView iv = (ImageView) headerView.findViewById(R.id.mylistview_iv_header);
        ProgressBar progress = (ProgressBar) headerView.findViewById(R.id.mylistview_headerprogress);

        RotateAnimation animation1 = new RotateAnimation(0,180,RotateAnimation.RELATIVE_TO_SELF,0.5f, Animation.RELATIVE_TO_SELF,0.5f);
        animation1.setDuration(500);
        animation1.setFillAfter(true);
        RotateAnimation animation2 = new RotateAnimation(180,0,RotateAnimation.RELATIVE_TO_SELF,0.5f, Animation.RELATIVE_TO_SELF,0.5f);
        animation2.setDuration(500);
        animation2.setFillAfter(true);

        switch (state){
            case NONE:
                Log.i("myTag","reflashViewByState+NONE"+state);
                iv.clearAnimation();
                topPadding(-headerHeight);
                break;
            case PULL:
                Log.i("myTag","reflashViewByState+PULL"+state);
                iv.setVisibility(VISIBLE);
                progress.setVisibility(INVISIBLE);
                tip.setText("下拉可以刷新");
                iv.clearAnimation();
                iv.setAnimation(animation2);
                break;
            case RELESE:
                iv.setVisibility(VISIBLE);
                progress.setVisibility(INVISIBLE);
                tip.setText("松开可以刷新");
                iv.clearAnimation();
                iv.setAnimation(animation1);
                break;
            case RELESEING:
                topPadding(50);
                iv.clearAnimation();
                iv.setVisibility(INVISIBLE);
                progress.setVisibility(VISIBLE);
                tip.setText("正在刷新");
                break;
        }
    }
}
