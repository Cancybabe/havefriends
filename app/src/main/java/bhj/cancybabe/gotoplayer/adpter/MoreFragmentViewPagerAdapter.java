package bhj.cancybabe.gotoplayer.adpter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import bhj.cancybabe.gotoplayer.R;

/**
 * Created by Cancybabe on 2016/11/18.
 */
public class MoreFragmentViewPagerAdapter extends PagerAdapter {
    private Context context;
    private int[] imageIds;
    private String[] ADs;
    public MoreFragmentViewPagerAdapter(Context context,int[] imageIds,String[] ADs) {
        this.context = context;
        this.imageIds = imageIds;
        this.ADs = ADs;
    }

    @Override
    public int getCount() {
        return Integer.MAX_VALUE;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
       View view =  View.inflate(context, R.layout.fragement_more_viewpager_item,null);
        ImageView imageView = (ImageView) view.findViewById(R.id.image);
        imageView.setImageResource(imageIds[position%5]);
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }
}
