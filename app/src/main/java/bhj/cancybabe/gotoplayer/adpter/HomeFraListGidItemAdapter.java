package bhj.cancybabe.gotoplayer.adpter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import bhj.cancybabe.gotoplayer.R;
import bhj.cancybabe.gotoplayer.application.MyApplication;
import bhj.cancybabe.gotoplayer.bean.UserActivtiesInfo;

/**
 * Created by Cancybabe on 2017/1/3.
 */
public class HomeFraListGidItemAdapter extends BaseAdapter{
    private Context context;
    private UserActivtiesInfo currentActInfo;
    public HomeFraListGidItemAdapter(Context context, UserActivtiesInfo currentActInfo){
        this.context = context;
        this.currentActInfo = currentActInfo;

    }

    @Override
    public int getCount() {
        return currentActInfo.getActionPic().size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        View view;
        ImageView iv;
        if(convertView == null){
            view = LayoutInflater.from(context).inflate(R.layout.fra_home_list_gvitem,null);
            iv = (ImageView) view.findViewById(R.id.fra_home_list_grid_item);
            view.setTag(iv);
        }else {
            view = convertView;
            iv = (ImageView) view.getTag();
        }

        //获取当前项中的每张图片上的url
        String picUrl = currentActInfo.getActionPic().get(i).getUrl();
        imageLoaderSaveAndDiaplay(picUrl, iv);

        return view;
    }


    /**
     * 用imageloader缓存图片并展示到控件上
     * @param url  网络图片地址
     * @param iv  要展示到哪个控件
     */
    public void imageLoaderSaveAndDiaplay(String url, final ImageView iv) {
        ImageLoader.getInstance().displayImage(url, iv, MyApplication.getSimpleOptions(), new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String s, View view) {

            }

            @Override
            public void onLoadingFailed(String s, View view, FailReason failReason) {

            }

            @Override
            public void onLoadingComplete(String s, View view, Bitmap bitmap) {
                iv.setImageBitmap(bitmap);
            }

            @Override
            public void onLoadingCancelled(String s, View view) {

            }
        });

    }
}
