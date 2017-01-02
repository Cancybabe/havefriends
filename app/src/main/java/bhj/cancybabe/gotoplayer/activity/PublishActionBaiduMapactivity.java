package bhj.cancybabe.gotoplayer.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiCitySearchOption;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiIndoorResult;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;

import java.util.List;

import bhj.cancybabe.gotoplayer.R;
import bhj.cancybabe.gotoplayer.base.BaseActivity;
import bhj.cancybabe.gotoplayer.base.BaseInterface;

/**
 * Created by Cancybabe on 2016/11/14.
 */
public class PublishActionBaiduMapactivity extends BaseActivity implements BaseInterface{
    MapView mMapView = null;
    private EditText mEtMap = null;
    private ImageView mIvMap = null;
    private PoiSearch mPoiSearch;
    private BaiduMap mBaiduMap;
    private List<PoiInfo> allPoiInfo;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_publish_baidumap);
        initView();
        initListener();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        mMapView.onDestroy();
    }
    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
        mMapView.onResume();
    }
    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
        mMapView.onPause();
    }

    @Override
    public void initView() {
        mMapView = (MapView) findViewById(R.id.bmapView);
        mIvMap = (ImageView) findById(R.id.act_publicmap_iv_seach);
        mEtMap = (EditText) findById(R.id.act_publicmap_et_seach);
        mBaiduMap = mMapView.getMap();//获取百度地图的实例

        //第一步，创建POI检索实例
        mPoiSearch = PoiSearch.newInstance();//获得兴趣点搜索实例


        //第二步，创建POI检索监听者；
        OnGetPoiSearchResultListener poiListener = new OnGetPoiSearchResultListener() {
            @Override
            public void onGetPoiResult(final PoiResult result) {
                //第二步，在POI检索回调接口中添加自定义的PoiOverlay；
                if (result == null || result.error == SearchResult.ERRORNO.RESULT_NOT_FOUND) {
                    return;
                }
                if (result.error == SearchResult.ERRORNO.NO_ERROR) {
                   allPoiInfo = result.getAllPoi();
                    mBaiduMap.clear();

                    //创建PoiOverlay
                    final PoiOverlay overlay = new MyPoiOverlay(mBaiduMap);
                    //设置overlay可以处理标注点击事件

                    //PoiOverlay  extends  OverlayManager  implements  OnMarkerClickListener
                    mBaiduMap.setOnMarkerClickListener(overlay);
                    //设置PoiOverlay数据
                    overlay.setData(result);
                    //添加PoiOverlay到地图中
                    overlay.addToMap();
                    //缩放地图
                    overlay.zoomToSpan();
                }
            }

            @Override
            public void onGetPoiDetailResult(PoiDetailResult poiDetailResult) {

            }

            @Override
            public void onGetPoiIndoorResult(PoiIndoorResult poiIndoorResult) {

            }
        };

        //第三步，设置POI检索监听者
        mPoiSearch.setOnGetPoiSearchResultListener(poiListener);
    }

    /**
     * 地图的搜索按钮被单击
     * @param view
     */
    public void onSearchMapClick(View view){
        String searchContent = mEtMap.getText().toString().trim();
        toast(searchContent);

        //第四步，发起检索请求
        mPoiSearch.searchInCity((new PoiCitySearchOption())
                .city("北京")
                .keyword(searchContent)
                .pageNum(1));
    }




    //第一步，构造自定义 PoiOverlay 类；
    private class MyPoiOverlay extends PoiOverlay {
        public MyPoiOverlay(BaiduMap baiduMap) {
            super(baiduMap);
        }

        @Override
        public boolean onPoiClick(int index) {
            toast("opPoiClick");
            final PoiInfo poiInfo = allPoiInfo.get(index);

            View view  = getLayoutInflater().inflate(R.layout.act_pulishmap_poi_item,null);
            //创建InfoWindow , 传入 view， 地理坐标， y 轴偏移量
            InfoWindow mInfoWindow = new InfoWindow(view, poiInfo.location, -47);
            //显示InfoWindow
            TextView tvPoidetails = (TextView) view.findViewById(R.id.act_publishmap_poidetails);
            TextView tvPoiname = (TextView) view.findViewById(R.id.act_publishmap_poiname);
            Button btn_cancel = (Button) view.findViewById(R.id.act_publishmap_btn_cancel);
            Button btn_select = (Button) view.findViewById(R.id.act_publishmap_btn_select);
            final String placeName = poiInfo.name;
            String placeAddress = poiInfo.address;
            tvPoiname.setText(placeName);
            tvPoidetails.setText(placeAddress);
            mBaiduMap.showInfoWindow(mInfoWindow);

            btn_cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //第五步，释放POI检索实例
                   return;
                }
            });

            btn_select.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //第五步，释放POI检索实例

                    mPoiSearch.destroy();
                    changePublishActPlace(placeName,poiInfo);
                }
            });
            return true;
        }
    }

   public void changePublishActPlace(String  str,PoiInfo poiInfo){
       if (listener != null){
           listener.setPoiInfo(str,poiInfo);
           finish();
       }
   }


    public static onChosePoiInfoClickListener listener;

    public interface onChosePoiInfoClickListener{
        void setPoiInfo(String str,PoiInfo poiInfo);
    }

    public static void setonChosePoiInfoClick(onChosePoiInfoClickListener listener){
        PublishActionBaiduMapactivity.listener = listener;
    }

    @Override
    public void initListener() {

    }
}
