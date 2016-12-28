package bhj.cancybabe.gotoplayer.application;

import android.app.Application;
import android.graphics.Bitmap;
import android.util.Log;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import bhj.cancybabe.gotoplayer.bean.UserActivtiesInfo;
import bhj.cancybabe.gotoplayer.bean.UserInfo;
import cn.bmob.sms.BmobSMS;
import cn.bmob.v3.Bmob;

/**
 * Created by Cancybabe on 2016/11/7.
 */
public class MyApplication extends Application {
    public static UserInfo userInfo;
    public static HashMap<String,Object> userActInfo = new HashMap<>();
    public static BDLocation currentUserLocation;
    public LocationClient mLocationClient = null;

    @Override
    public void onCreate() {
        super.onCreate();
        Bmob.initialize(this,"307ffa3b38b68a29946ec2c1de089cb1");
        BmobSMS.initialize(this,"307ffa3b38b68a29946ec2c1de089cb1");
        SDKInitializer.initialize(getApplicationContext());
        DisplayImageOptions options = new DisplayImageOptions.Builder().
        cacheInMemory(true)  //1.8.6包使用时候，括号里面传入参数true
        .cacheOnDisc(true)    //同上
        .build();

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this)
        .defaultDisplayImageOptions(options).build();
        ImageLoader.getInstance().init(config);

        mLocationClient = new LocationClient(this);     //声明LocationClient类
        mLocationClient.registerLocationListener(new BDLocationListener() {
            @Override
            public void onReceiveLocation(BDLocation bdLocation) {
                currentUserLocation = bdLocation;
//                String currentAddress = bdLocation.getAddress().address;
//                Log.i("myTag","获取到的定位结果" + currentAddress);
            }
        });
        initLocation();//注册监听函数
        mLocationClient.start();
    }

//    public BDLocationListener myListener = new BDLocationListener() {
//        @Override
//        public void onReceiveLocation(BDLocation bdLocation) {
//            String currentAddress = bdLocation.getAddress().address;
//            Log.i("myTag","获取到的定位结果" + currentAddress);
//        }
//    };


    private void initLocation(){
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy
        );//可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setCoorType("bd09ll");//可选，默认gcj02，设置返回的定位结果坐标系
        int span=1000;
        option.setScanSpan(span);//可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
        option.setIsNeedAddress(true);//可选，设置是否需要地址信息，默认不需要
        option.setOpenGps(true);//可选，默认false,设置是否使用gps
        option.setLocationNotify(true);//可选，默认false，设置是否当GPS有效时按照1S/1次频率输出GPS结果
        option.setIsNeedLocationDescribe(true);//可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
        option.setIsNeedLocationPoiList(true);//可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        option.setIgnoreKillProcess(false);//可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
        option.SetIgnoreCacheException(false);//可选，默认false，设置是否收集CRASH信息，默认收集
        option.setEnableSimulateGps(false);//可选，默认false，设置是否需要过滤GPS仿真结果，默认需要
        mLocationClient.setLocOption(option);
    }

}
