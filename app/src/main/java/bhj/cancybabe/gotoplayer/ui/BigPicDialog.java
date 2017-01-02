package bhj.cancybabe.gotoplayer.ui;

import android.app.Dialog;
import android.content.Context;
import android.view.View;

import java.util.ArrayList;

import bhj.cancybabe.gotoplayer.R;

/**
 * Created by Cancybabe on 2017/1/3.
 */
public class BigPicDialog extends Dialog implements View.OnClickListener{
    private Context context;
    private ArrayList<String> picUrls;
    private int clickItem;

    /**
     *
     * @param context   上下文
     * @param clickItem 点击的项
     * @param picUrls 图片的Url
     */
    public BigPicDialog(Context context, int clickItem, ArrayList<String> picUrls) {
        super(context, R.style.CustomDialog_fill);
        this.context = context;
        this.clickItem = clickItem;
        this.picUrls = picUrls;
    }



    @Override
    public void onClick(View view) {

    }
}
