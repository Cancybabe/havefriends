package bhj.cancybabe.gotoplayer.adpter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.mapapi.search.core.PoiInfo;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import bhj.cancybabe.gotoplayer.R;
import bhj.cancybabe.gotoplayer.activity.HomeDetailsActivity;
import bhj.cancybabe.gotoplayer.application.MyApplication;
import bhj.cancybabe.gotoplayer.bean.UserActivtiesInfo;
import bhj.cancybabe.gotoplayer.bean.UserInfo;
import bhj.cancybabe.gotoplayer.utils.FindUserInfoUtils;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by Cancybabe on 2017/2/8.
 */
public class HomeFraRecycleViewADapter extends RecyclerView.Adapter<HomeFraRecycleViewADapter.ViewHolder> {
    private Context context;
    private ArrayList<UserActivtiesInfo> datas;
    String nickName;//用户名
    public HomeFraRecycleViewADapter(Context context, ArrayList<UserActivtiesInfo> datas) {
        this.context = context;
        this.datas = datas;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragemnt_home_listview_item,parent,false);
        HomeFraRecycleViewADapter.ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {
        final UserActivtiesInfo currentActInfo = datas.get(position);
        getPublishUserName(currentActInfo.getObjectId());


        viewHolder.tvActionPubUserName.setText(currentActInfo.getActionpublishUser());
        viewHolder.tvActionTime.setText(currentActInfo.getActionTime());
        viewHolder.tvActionMoney.setText(currentActInfo.getActionRMB()+"元");
        viewHolder.tvActionContent.setText(currentActInfo.getActionName());

        //图片的三级缓存
        //下载所有的图片,如果当前图片已经存在则直接展示，否则从网络下载之后保存到本地再展示
        if(currentActInfo.getActionPic().size() == 1){
            viewHolder.gvPics.setVisibility(View.INVISIBLE);
            viewHolder.ivPicOne.setVisibility(View.VISIBLE);
            String picUrl = currentActInfo.getActionPic().get(0).getUrl();
            imageLoaderSaveAndDiaplay(picUrl, viewHolder.ivPicOne);

        }else{
            viewHolder.gvPics.setVisibility(View.VISIBLE);
            viewHolder.ivPicOne.setVisibility(View.INVISIBLE);
            viewHolder.gvPics.setAdapter(new HomeFraListGidItemAdapter(context,currentActInfo));
        }


        //获取发布者的昵称
        HashMap<String,String> userNickName = (HashMap<String, String>) MyApplication.userActInfo.get("UserNickName");
        if (nickName == null){
            FindUserInfoUtils.findUserInfo(currentActInfo.getActionUserId(), new FindUserInfoUtils.getUserInfoListener() {

                @Override
                public void getUserInfo(UserInfo userInfo,BmobException e) {
                    //获取发布者的昵称
                    viewHolder.tvActionPubUserName.setText(userInfo.getNickName());
                }
            });

        }else {
            //本地有发布者昵称数据
            viewHolder.tvActionPubUserName.setText(userNickName.get(currentActInfo.getActionUserId()));
        }

        FindUserInfoUtils.findUserInfo(currentActInfo.getActionUserId(), new FindUserInfoUtils.getUserInfoListener() {
            @Override
            public void getUserInfo(UserInfo userInfo,BmobException e) {
                //获取发布者的头像
                String picUrl = userInfo.getUserHead().getUrl();

                imageLoaderSaveAndDiaplay(picUrl, viewHolder.ivActionUserHead);
            }
        });



        boolean isPraiseFlag = false;//默认未收藏
        //当前用户收藏了哪些活动
        List<String> praiseActions = MyApplication.userInfo.getPraiseActions();

        if (praiseActions == null){
            praiseActions = new ArrayList<>();
        }
        Log.i("myTag", "当前用户已点赞的活动"+praiseActions.size());

        //如果用户收藏的活动的活动ID中包含当前的item的活动ID
        if(praiseActions.contains(currentActInfo.getObjectId())) {
            //如果收藏的集合包含当前集合
            viewHolder.ivUserPraise.setImageResource(R.drawable.fra_home_lv_item_heartafter);
            isPraiseFlag = true;
        }else {
            viewHolder.ivUserPraise.setImageResource(R.drawable.fra_home_lv_item_heartbefore);
            isPraiseFlag = false;
        }

        //点赞图片的监听
        final ActionPraiseHolder praiseHolder = new ActionPraiseHolder();
        praiseHolder.userActivtiesInfo = currentActInfo;// 活动信息
        praiseHolder.isPraFlag = isPraiseFlag;
        viewHolder.ivUserPraise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (praiseHolder.isPraFlag){//当前是已经点赞状态
                    List<String> userActivtiesInfos = MyApplication.userInfo.getPraiseActions();
                    Log.i("myTag","用户表里已经点赞的个数"+userActivtiesInfos.size());
                    userActivtiesInfos.remove(currentActInfo.getObjectId());
                    MyApplication.userInfo.setPraiseActions(userActivtiesInfos);//当前用户已经收藏的活动
                    MyApplication.userInfo.update(MyApplication.userInfo.getObjectId(), new UpdateListener() {
                        @Override
                        public void done(BmobException e) {//更新后台用户表
                            if (e == null) {
                                Log.i("myTag","用户表取赞活动");
                                praiseHolder.isPraFlag = false;
                                viewHolder.ivUserPraise.setImageResource(R.drawable.fra_home_lv_item_heartbefore);
                            }
                        }
                    });

                    // final UserActivtiesInfo currentActInfo = (UserActivtiesInfo) ((ArrayList) MyApplication.userActInfo.get("AllActInfos")).get(i);
                    List<String> praiseUserIdList = currentActInfo.getPraiseUser();  //活动表中，收藏该活动的用户
                    praiseUserIdList.remove(MyApplication.userInfo.getObjectId());
                    currentActInfo.setPraiseUser(praiseUserIdList);
                    ((ArrayList) MyApplication.userActInfo.get("AllActInfos")).set(position,currentActInfo);//把本地活动信息的数据集改了

                    currentActInfo.update(currentActInfo.getObjectId(), new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            Log.i("myTag","活动表删除点赞用户");
                            if (e == null){
                                currentActInfo.increment("priseCount",-1);
                            }
                        }
                    });


                }else {//当前是没有点赞状态

                    //当前用户收藏了哪些活动
                    List<String> praiseActions = MyApplication.userInfo.getPraiseActions();
                    List<String> userActivtiesInfos;
                    if(praiseActions == null){
                        userActivtiesInfos = new ArrayList<String>();
                    }else{
                        userActivtiesInfos = praiseActions;
                    }

                    userActivtiesInfos.add(currentActInfo.getObjectId());
                    //修改本地当前用户的点赞活动
                    MyApplication.userInfo.setPraiseActions(userActivtiesInfos);
                    MyApplication.userInfo.update(MyApplication.userInfo.getObjectId(), new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if (e == null){
                                Log.i("myTag","用户表点赞活动");
                                praiseHolder.isPraFlag = true;
                                viewHolder.ivUserPraise.setImageResource(R.drawable.fra_home_lv_item_heartafter);
                            }

                        }
                    });

                    //final UserActivtiesInfo currentActInfo = new UserActivtiesInfo();
                    List<String> praiseUserIdList;
                    if(currentActInfo.getPraiseUser() == null){
                        praiseUserIdList = new ArrayList<String>();
                    }else{
                        praiseUserIdList = currentActInfo.getPraiseUser();
                    }

                    praiseUserIdList.add(MyApplication.userInfo.getObjectId());
                    currentActInfo.setPraiseUser(praiseUserIdList);
                    ((ArrayList) MyApplication.userActInfo.get("AllActInfos")).set(position,currentActInfo);//把本地活动信息的数据集改了
                    currentActInfo.update(currentActInfo.getObjectId(), new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if (e == null) {
                                // currentActInfo.increment("priseCount", 1);
                                currentActInfo.increment("priseCount"); // 分数递增1
                                currentActInfo.update(new UpdateListener() {
                                    @Override
                                    public void done(BmobException e) {
                                        Log.i("myTag","点赞加1");
                                    }
                                });
                                Log.i("myTag","活动表添加点赞用户");
                            }else{
                                Log.i("bmob","更新失败："+e.getMessage()+","+e.getErrorCode());
                            }


                        }
                    });

                }

            }
        });


        //用户举例活动的距离
        BDLocation userLocation = MyApplication.currentUserLocation;
        if (userLocation != null){
            PoiInfo actionPoiInfo = currentActInfo.getActionPoiInfo();

            //通过经纬度计算距离
            double distance =  LantitudeLongitudeDist(actionPoiInfo.location.longitude,actionPoiInfo.location.latitude,
                    userLocation.getLongitude(),userLocation.getLatitude());

            if(distance < 1){
                int distanceMetres = (int)distance*1000;
                viewHolder.tvActionDistance.setText(distanceMetres+"m");
            }else if(distance <=1000){
                int distanceKiloMetres = (int) (distance*10/10);
                viewHolder.tvActionDistance.setText(distanceKiloMetres+"km");
            }else {
                viewHolder.tvActionDistance.setText(1000+"+ "+"km");
            }

        }

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Toast.makeText(context,"hhe",Toast.LENGTH_SHORT).show();
                ArrayList<UserActivtiesInfo>  activtiesInfos = (ArrayList<UserActivtiesInfo>) MyApplication.userActInfo.get("AllActInfos");
                Log.i("myTag","点击了子项"+(position-1)+"activtiesInfos的长度"+activtiesInfos.size());
                Intent intent = new Intent(context, HomeDetailsActivity.class);
                Bundle bundle = new Bundle();
                bundle.putParcelable("itemdatas",activtiesInfos.get(position));
                intent.putExtras(bundle);
                //intent.putExtra("itemdatas",activtiesInfos);
                context.startActivity(intent);
            }

        });



    }



    @Override
    public int getItemCount() {
        return datas.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        TextView tvActionPubUserName;
        TextView tvActionTime;
        TextView tvActionDistance;
        TextView tvActionMoney;
        TextView tvActionContent;
        ImageView ivActionUserHead;
        ImageView ivUserPraise;
        GridView gvPics;
        ImageView ivPicOne;
        public ViewHolder(View view) {
            super(view);
            tvActionPubUserName = (TextView) view.findViewById(R.id.fra_home_lv_tv_name);
            tvActionTime = (TextView) view.findViewById(R.id.fra_home_lv_tv_time);
            tvActionDistance = (TextView) view.findViewById(R.id.fra_home_lv_tv_distance);
            tvActionContent = (TextView) view.findViewById(R.id.fra_home_lv_tv_actionDetails);
            tvActionMoney = (TextView) view.findViewById(R.id.fra_home_lv_tv_money);
            ivActionUserHead = (ImageView) view.findViewById(R.id.fra_home_iv_head);
            ivUserPraise = (ImageView) view.findViewById(R.id.fra_home_lv_iv_like);
            gvPics = (GridView) view.findViewById(R.id.fra_home_gv);
            ivPicOne = (ImageView) view.findViewById(R.id.fra_home_iv_one);
        }
    }


    class  ActionPraiseHolder{
        UserActivtiesInfo userActivtiesInfo;
        Boolean isPraFlag;
    }
    /**
     * 从服务器获取用户的昵称
     */
    private void  getPublishUserName(String userID) {
        BmobQuery<UserInfo> query = new BmobQuery<UserInfo>();
        query.addWhereEqualTo("username", userID);
        query.findObjects(new FindListener<UserInfo>() {
            @Override
            public void done(List<UserInfo> object, BmobException e) {
                if(e==null){
//                    toast("查询用户成功:"+object.size());
                    UserInfo  userinfo = object.get(0);
                    nickName = userinfo.getNickName();
                }else{
//                    toast("更新用户信息失败:" + e.getMessage());

                }
            }
        });
    }

    private static final  double EARTH_RADIUS = 6378137;//赤道半径(单位m)

    /**
     * 转化为弧度(rad)
     * */
    private static double rad(double d)
    {
        return d * Math.PI / 180.0;
    }

    /**
     * 基于余弦定理求两经纬度距离
     * @param lon1 第一点的精度
     * @param lat1 第一点的纬度
     * @param lon2 第二点的精度
     * @param lat2 第二点的纬度
     * @return 返回的距离，单位km
     * */
    public static double LantitudeLongitudeDist(double lon1, double lat1,double lon2, double lat2) {
        double radLat1 = rad(lat1);
        double radLat2 = rad(lat2);

        double radLon1 = rad(lon1);
        double radLon2 = rad(lon2);

        if (radLat1 < 0)
            radLat1 = Math.PI / 2 + Math.abs(radLat1);// south
        if (radLat1 > 0)
            radLat1 = Math.PI / 2 - Math.abs(radLat1);// north
        if (radLon1 < 0)
            radLon1 = Math.PI * 2 - Math.abs(radLon1);// west
        if (radLat2 < 0)
            radLat2 = Math.PI / 2 + Math.abs(radLat2);// south
        if (radLat2 > 0)
            radLat2 = Math.PI / 2 - Math.abs(radLat2);// north
        if (radLon2 < 0)
            radLon2 = Math.PI * 2 - Math.abs(radLon2);// west
        double x1 = EARTH_RADIUS * Math.cos(radLon1) * Math.sin(radLat1);
        double y1 = EARTH_RADIUS * Math.sin(radLon1) * Math.sin(radLat1);
        double z1 = EARTH_RADIUS * Math.cos(radLat1);

        double x2 = EARTH_RADIUS * Math.cos(radLon2) * Math.sin(radLat2);
        double y2 = EARTH_RADIUS * Math.sin(radLon2) * Math.sin(radLat2);
        double z2 = EARTH_RADIUS * Math.cos(radLat2);

        double d = Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2) + (z1 - z2) * (z1 - z2));
        //余弦定理求夹角
        double theta = Math.acos((EARTH_RADIUS * EARTH_RADIUS + EARTH_RADIUS * EARTH_RADIUS - d * d) / (2 * EARTH_RADIUS * EARTH_RADIUS));
        double dist = theta * EARTH_RADIUS;
        return dist;
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


    public void updateData(ArrayList<UserActivtiesInfo> datas){
        // this.datas.clear();
        this.datas = datas;
        this.notifyDataSetChanged();
    }

}
