package bhj.cancybabe.gotoplayer.adpter;

import android.content.Context;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import bhj.cancybabe.gotoplayer.R;

/**
 * Created by Cancybabe on 2016/11/11.
 */
public class MyFraGridViewAdapter extends BaseAdapter {
    private int[] myFraChoicePics ={R.drawable.fra_my_youhui,R.drawable.fra_my_shoucang,
            R.drawable.fra_my_send,R.drawable.fra_my_order,
            R.drawable.fra_my_my,R.drawable.fra_my_shezhi};

    private ArrayList<String> datas;
    private  Context context;
    public MyFraGridViewAdapter(Context context, ArrayList<String> datas){
        this.datas = datas;
        this.context = context;
    }
    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public Object getItem(int i) {
        return datas.get(i);
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
            view = LayoutInflater.from(context).inflate(R.layout.fragemnt_my_gridview_item,null);
            viewHolder = new ViewHolder();
            viewHolder.imageView = (ImageView) view.findViewById(R.id.fra_my_gv_item_iv);
            viewHolder.textView = (TextView) view.findViewById(R.id.fra_my_gv_item_tv);
            view.setTag(viewHolder);
        }else {
            view = convertView;
            viewHolder = (ViewHolder)view.getTag();

        }
        viewHolder.imageView.setImageResource(myFraChoicePics[i]);
        viewHolder.textView.setText(datas.get(i));
        return view;
    }


    class ViewHolder{
        public TextView textView;
        public ImageView imageView;
    }
}
