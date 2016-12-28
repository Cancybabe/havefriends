package bhj.cancybabe.gotoplayer.utils;

import android.util.Log;

import java.util.List;

import bhj.cancybabe.gotoplayer.bean.UserActivtiesInfo;
import bhj.cancybabe.gotoplayer.bean.UserInfo;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListener;

/**
 * Created by Cancybabe on 2016/11/11.
 */
public class FindActionInfoUtils {

    /**
     *
     * @param type 查询类型 1.默认查询服务器中表的十条数据，刷新操作时，忽略参数3，不跳过，查询count条数据
     *                      2.使用参数3，查询count条数据，跳过skip条数据，加载更多时使用
     *                      3.使用参数2，查询一个活动
     *                      4.使用参数2，按活动的类别来查询
     *                      5.使用参数2，所有免费活动
     *                      6.使用参数2，按照热度来查询
     *                      7.使用参数2，按照距离排序来查询
     *
     * @param data
     * @param skip  跳过数据，下拉刷新的时候，跳过以及加载的数据
     * @param count 查询多少条，默认十条
     * @param listener
     */
    public static void findAllUserInfo(int type, String data, int skip, int count, final findAllActionInfoListener listener){
                BmobQuery<UserActivtiesInfo> query = new BmobQuery<>();

        switch (type){
            case 1:
                query.setLimit(10);

                break;
            case 2:
                query.setLimit(count);
                query.setSkip(skip);

                break;

            case 3:
                //查询一条数据
                query.addWhereEqualTo("objectId",data);

                break;
            case 4:
                query.addWhereEqualTo("actionClass",data);

                break;
            case 5:
                query.addWhereEqualTo("actionRMB",data);
                break;

//            case 6:
//                query.addWhereEqualTo("praiseCount",data);
//                break;
        }

        query.findObjects(new FindListener<UserActivtiesInfo>() {
            @Override
            public void done(List<UserActivtiesInfo> list, BmobException e) {
                listener.getActionInfo(list,e);
            }
        });
    }

    public  interface findAllActionInfoListener{
        void getActionInfo(List<UserActivtiesInfo> activtiesInfos,BmobException ex);
    }

}
