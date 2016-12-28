package bhj.cancybabe.gotoplayer.utils;

import android.util.Log;

import bhj.cancybabe.gotoplayer.bean.UserInfo;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;

/**
 * Created by Cancybabe on 2016/11/11.
 */
public class FindUserInfoUtils {


    /**
     *从服务器查询用户信息
     * @param  userObjectId 要查询的用户ID
     * @param listener 获取到的用户信息回调接口
     */
    public static void findUserInfo(String userObjectId, final getUserInfoListener listener){
        BmobQuery<UserInfo> query = new BmobQuery<UserInfo>();
        query.getObject(userObjectId, new QueryListener<UserInfo>() {

            @Override
            public void done(UserInfo userInfo, BmobException e) {
                if(e==null){
                    listener.getUserInfo(userInfo,e);

                }else{
                    Log.i("bmob","失败："+e.getMessage()+","+e.getErrorCode());
                }
            }

        });
    }

    public interface  getUserInfoListener{
        /**
         *
         * @param userInfo 查询到的用户信息
         */
        public    void  getUserInfo(UserInfo userInfo,BmobException e);
    }
}
