package com.benkie.hjw.map;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.LocationSource;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.model.MarkerOptions;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.core.SuggestionCity;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.benkie.hjw.R;
import com.benkie.hjw.ui.BaseActivity;
import com.benkie.hjw.utils.ToastUtil;

import java.util.List;

/**
 * Created by 37636 on 2018/1/22.
 */

public class MyMapActivity extends BaseActivity implements View.OnClickListener, LocationSource,
        AMap.OnMapClickListener,
        GeocodeSearch.OnGeocodeSearchListener,
        AMapLocationListener, PoiSearch.OnPoiSearchListener {
    private PoiSearch.Query query;//周边搜索
    private AMap aMap;
    private MapView mapView;
    private MarkerOptions markerOption;
    private Marker marker;
    private OnLocationChangedListener mListener;
    private AMapLocationClient mlocationClient;
    private AMapLocationClientOption mLocationOption;
    private double custLat;    //纬度
    private double custLon;    //	经度
    private String address;
    private String title;

    //你编码对象
    private GeocodeSearch geocoderSearch;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        mapView = (MapView) findViewById(R.id.map);
        geocoderSearch = new GeocodeSearch(mActivity);
        geocoderSearch.setOnGeocodeSearchListener(this);
        mapView.onCreate(savedInstanceState);// 此方法必须重写
        initView();
        initMap();
    }

    ListView listView;
    MapAdapter mapAdapter;
    boolean isMap = true;
    LatLonPoint latLonPoint;
    private static MapBean mapBean;

    private void initView() {
        TextView tv_title = (TextView) findViewById(R.id.tv_title);
        String name = getIntent().getStringExtra("Name");
        if (name != null) {
            tv_title.setText(name);
        }
        mapBean = null;
        listView = (ListView) findViewById(R.id.listView);
        findViewById(R.id.iv_back).setOnClickListener(this);
        findViewById(R.id.tv_save).setOnClickListener(this);
        findViewById(R.id.iv_search).setOnClickListener(this);
        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        mapAdapter = new MapAdapter(this);
        mapAdapter.setBox(true);
        listView.setAdapter(mapAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                mapBean = (MapBean) adapterView.getAdapter().getItem(i);
                isMap = false;
                latLonPoint = mapBean.getPoiItem().getLatLonPoint();
                addMarkersToMap(convertToLatLng(latLonPoint));
                address = mapBean.getPoiItem().getCityName() + mapBean.getPoiItem().getAdName() + mapBean.getPoiItem().getTitle() + mapBean.getPoiItem().getSnippet();
                title = mapBean.getPoiItem().getTitle();
            }
        });
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.iv_search:
                intent = new Intent(this, SearchLocationActivity.class);
                startActivityForResult(intent, 1000);
                break;
            case R.id.tv_save:
                if (TextUtils.isEmpty(address)) {
                    ToastUtil.showInfo(this, "您还没选择地址呢！");
                    return;
                }
                intent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putParcelable("LatLonPoint", latLonPoint);
                bundle.putString("Address", address);
                bundle.putString("Title", title);
                intent.putExtras(bundle);
                setResult(RESULT_OK, intent);
                finish();
                break;
        }
    }

    private void initMap() {
        if (aMap == null) {
            aMap = mapView.getMap();
            aMap.setOnMapClickListener(this);
            aMap.moveCamera(CameraUpdateFactory.zoomTo(18)); //缩放比例
            aMap.setLocationSource(this);// 设置定位监听
            //aMap.getUiSettings().setMyLocationButtonEnabled(true);// 设置默认定位按钮是否显示
            aMap.setMyLocationEnabled(true);
        }
    }

    /**
     * 定位成功后回调函数
     */
    @Override
    public void onLocationChanged(AMapLocation amapLocation) {
        Log.e("AMapLocation", amapLocation.getAddress());
        if (mListener != null && amapLocation != null) {
            if (amapLocation != null
                    && amapLocation.getErrorCode() == 0) {
                LatLng la = new LatLng(amapLocation.getLatitude(), amapLocation.getLongitude());
                addMarkersToMap(la);// 往地图上添加marker
                isMap = true;
                mListener.onLocationChanged(amapLocation);// 显示系统小蓝点
                deactivate();
            } else {
                String errText = "定位失败," + amapLocation.getErrorCode() + ": " + amapLocation.getErrorInfo();
                Log.e("AmapErr", errText);
                Toast.makeText(mActivity, errText, Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * 在地图上添加marker
     */
    private void addMarkersToMap(LatLng latlng) {
        aMap.clear();
        markerOption = new MarkerOptions().icon(BitmapDescriptorFactory
                .defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                .position(latlng)
//                .title("标题")
//                .snippet("详细信息")
                .draggable(true);
        markerOption.icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_map_dw));
        marker = aMap.addMarker(markerOption);
        //marker.showInfoWindow();
        getAddress(latlng);
        aMap.moveCamera(CameraUpdateFactory.changeLatLng(latlng));
        custLat = latlng.latitude;    //纬度
        custLon = latlng.longitude;    //	经度
    }

    /**
     * 根据经纬度得到地址
     */
    public void getAddress(final LatLng latLonPoint) {
        LatLonPoint latlng = convertToLatLonPoint(latLonPoint);
        // 第一个参数表示一个Latlng，第二参数表示范围多少米，第三个参数表示是火系坐标系还是GPS原生坐标系
        RegeocodeQuery query = new RegeocodeQuery(latlng, 50, GeocodeSearch.AMAP);
        geocoderSearch.getFromLocationAsyn(query);// 设置同步逆地理编码请求

        //查询周围得POI
        if (isMap)
            searchBound(latLonPoint);
    }

    /**
     * 把LatLng对象转化为LatLonPoint对象
     */
    public static LatLonPoint convertToLatLonPoint(LatLng latlon) {
        return new LatLonPoint(latlon.latitude, latlon.longitude);
    }

    /**
     * 把LatLonPoint对象转化为LatLon对象
     */
    public static LatLng convertToLatLng(LatLonPoint latLonPoint) {
        return new LatLng(latLonPoint.getLatitude(), latLonPoint.getLongitude());
    }

    @Override
    public void onMapClick(LatLng latLng) {
        isMap = true;
        aMap.clear();
        addMarkersToMap(latLng);
        aMap.moveCamera(CameraUpdateFactory.changeLatLng(latLng));
    }


    /**
     * 激活定位
     *
     * @param onLocationChangedListener
     */
    @Override
    public void activate(OnLocationChangedListener onLocationChangedListener) {
        mListener = onLocationChangedListener;
        if (mlocationClient == null) {
            mlocationClient = new AMapLocationClient(mActivity);
            mLocationOption = new AMapLocationClientOption();
            //设置定位监听
            mlocationClient.setLocationListener(this);
            //设置为高精度定位模式
            mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
            //设置定位参数
            mlocationClient.setLocationOption(mLocationOption);
            // 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
            // 注意设置合适的定位时间的间隔（最小间隔支持为2000ms），并且在合适时间调用stopLocation()方法来取消定位请求
            // 在定位结束后，在合适的生命周期调用onDestroy()方法
            // 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
            mlocationClient.startLocation();
        }
    }

    /**
     * 停止定位
     */
    @Override
    public void deactivate() {
        mListener = null;
        if (mlocationClient != null) {
            mlocationClient.stopLocation();
            mlocationClient.onDestroy();
        }
        mlocationClient = null;
    }

    /**
     * 周边搜索
     *
     * @param latlon 位置
     */
    private void searchBound(LatLng latlon) {
        if (query == null) {
            query = new PoiSearch.Query("", "", "");
//				//keyWord表示搜索字符串，
//				//第二个参数表示POI搜索类型，二者选填其一，选用POI搜索类型时建议填写类型代码，码表可以参考下方（而非文字）
//				//cityCode表示POI搜索区域，可以是城市编码也可以是城市名称，也可以传空字符串，空字符串代表全国在全国范围内进行搜索
            query.setPageSize(30);// 设置每页最多返回多少条poiitem
            query.setPageNum(0);//设置查询页码
        }

        PoiSearch poiSearch = new PoiSearch(this, query);
        LatLonPoint latLonPoint = convertToLatLonPoint(latlon);
        PoiSearch.SearchBound searchBound = new PoiSearch.SearchBound(latLonPoint, 2000);
        poiSearch.setBound(searchBound);
        poiSearch.setOnPoiSearchListener(this);
        poiSearch.searchPOIAsyn();
    }

    @Override
    public void onRegeocodeSearched(RegeocodeResult result, int rCode) {
        Log.e("map", rCode + "");
        if (rCode == 1000) {
            if (result != null && result.getRegeocodeAddress() != null && result.getRegeocodeAddress().getFormatAddress() != null) {
                markerOption.snippet(result.getRegeocodeAddress().getFormatAddress());
                marker = aMap.addMarker(markerOption);
                //marker.showInfoWindow();
//                address = result.getRegeocodeAddress().getFormatAddress() ;
//                title = result.getRegeocodeAddress().getPois().get(0).getTitle();
            }
        }
    }

    @Override
    public void onGeocodeSearched(GeocodeResult result, int rCode) {
        Log.e("map", rCode + "");
    }

    @Override
    public void onPoiSearched(PoiResult poiResult, int i) {
        List<PoiItem> poiItems = poiResult.getPois();// 取得第一页的poiitem数据，页数从数字0开始
        if (mapAdapter != null) {
            mapAdapter.clear();
            if (mapBean != null) {
                mapBean.setChecked(true);
                mapAdapter.add(mapBean);
            }
            mapAdapter.addAll(MapBean.addAll(poiItems));
            listView.setAdapter(mapAdapter);
            mapAdapter.notifyDataSetChanged();
            address = "";
            title = "";
        }
    }

    @Override
    public void onPoiItemSearched(PoiItem poiItem, int i) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
        deactivate();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
        aMap = null;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1000 & resultCode == RESULT_OK) {
            LatLonPoint latLonPoint = data.getExtras().getParcelable("LatLonPoint");
            addMarkersToMap(convertToLatLng(latLonPoint));
            mapBean = SearchLocationActivity.getMapBean();
        }
    }
}
