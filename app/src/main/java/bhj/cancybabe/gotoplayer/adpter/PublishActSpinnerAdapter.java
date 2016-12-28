package bhj.cancybabe.gotoplayer.adpter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import bhj.cancybabe.gotoplayer.R;

/**
 * Created by Cancybabe on 2016/11/13.
 */
public class PublishActSpinnerAdapter extends BaseAdapter {
    private Context context;
    private String[] datas;
    private int[] images;

    public  PublishActSpinnerAdapter(Context context, String[] datas,int[] images){
        this.context = context;
        this.datas = datas;
        this.images = images;

    }
    @Override
    public int getCount() {
        return datas.length;
    }

    @Override
    public Object getItem(int i) {
        return datas[i];
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertview, ViewGroup viewGroup) {
       LayoutInflater layoutInflater =  LayoutInflater.from(context);
        View view = null;
        ViewHolder viewHolder;
        if (convertview == null){
            viewHolder = new ViewHolder();
            view  = layoutInflater.inflate(R.layout.fragemnt_my_spinner_item,null);
            viewHolder.iv = (ImageView) view.findViewById(R.id.act_publish_sp_item_iv);
            viewHolder.tv = (TextView) view.findViewById(R.id.act_publish_sp_item_tv);
            view.setTag(viewHolder);
        }else {
            view = convertview;
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.iv.setImageResource(images[i]);
        viewHolder.tv.setText(datas[i]);
        return view;
    }

    class ViewHolder{
        ImageView iv;
        TextView tv;
    }
}
