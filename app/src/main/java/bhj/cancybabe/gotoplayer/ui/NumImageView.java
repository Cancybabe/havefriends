package bhj.cancybabe.gotoplayer.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by Cancybabe on 2017/1/5.
 */
public class NumImageView extends ImageView {
    private int messgeNum;//消息个数
    private float radius;//圆圈的半径
    private float textsize;//圆圈内数字的半径大小
    private int paddingRight;//圆圈的右边距
    private int paddingTop;//圆圈的上边距


    public NumImageView(Context context) {
        super(context);
    }

    public NumImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public NumImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }



    //设置显示的数量
    public void setNum(int messgeNum) {
        this.messgeNum = messgeNum;
        //重新绘制画布
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
            radius = getWidth()/6;
            textsize = messgeNum > 10 ? radius + 5:radius;
            //初始化上下边距
            paddingRight = getPaddingRight();
            paddingTop = getPaddingTop();
            //初始化画笔
            Paint paint = new Paint();
            //设置抗锯齿
            paint.setAntiAlias(true);
            //设置颜色为红色
            paint.setColor(0xFFFF567F);
            //设置样式
            paint.setStyle(Paint.Style.FILL);
            //画圆
            canvas.drawCircle(getWidth()-radius-getPaddingRight()/2,radius+getPaddingTop()/2,radius,paint);

            paint.setColor(Color.WHITE);

            paint.setTextSize(textsize);

            //画数字
            canvas.drawText("" + (messgeNum < 99 ? messgeNum : 99),
                    messgeNum < 10 ? getWidth() - radius - textsize / 4 - paddingRight/2
                            : getWidth() - radius - textsize / 2 - paddingRight/2,
                    radius + textsize / 3 + paddingTop/2, paint);


    }
}
