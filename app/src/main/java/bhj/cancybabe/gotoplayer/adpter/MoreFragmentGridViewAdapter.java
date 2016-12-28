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
 * Created by Cancybabe on 2016/11/18.
 */
public class MoreFragmentGridViewAdapter extends BaseAdapter{

    private int[] myFraChoicePics ={R.drawable.more_zhoubian,R.drawable.more_shaoer,R.drawable.more_diy,
            R.drawable.more_jianshen,R.drawable.more_jishi,R.drawable.more_yanchu,
            R.drawable.more_zhanlan,R.drawable.more_shalong,R.drawable.more_pincha,R.drawable.more_juhui};

    private String[] datas;
    private Context context;
    public MoreFragmentGridViewAdapter(Context context,String[] datas){
        this.datas = datas;
        this.context = context;
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
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        View view = null;
        ViewHolder viewHolder;

        if (convertView == null){
            view = LayoutInflater.from(context).inflate(R.layout.fragemnt_more_gridview_item,null);
            viewHolder = new ViewHolder();
            viewHolder.imageView = (ImageView) view.findViewById(R.id.fra_more_gv_item_iv);
            viewHolder.textView = (TextView) view.findViewById(R.id.fra_more_gv_item_tv);
            view.setTag(viewHolder);
        }else {
            view = convertView;
            viewHolder = (ViewHolder)view.getTag();

        }
        viewHolder.imageView.setImageResource(myFraChoicePics[i]);
        viewHolder.textView.setText(datas[i]);
        return view;
    }


    class ViewHolder{
        public TextView textView;
        public ImageView imageView;
    }
}
