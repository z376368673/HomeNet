package com.benkie.hjw.map;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationClientOption.AMapLocationMode;
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
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.benkie.hjw.R;
import com.benkie.hjw.utils.ToastUtil;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * AMapV1地图中简单介绍显示定位小蓝点
 */
public class LocationSourceActivity extends Activity implements LocationSource,
		AMap.OnMapClickListener,
		GeocodeSearch.OnGeocodeSearchListener,
		AMap.OnMapScreenShotListener ,
        AMapLocationListener ,PoiSearch.OnPoiSearchListener {
	private AMap aMap;
	private MapView mapView;
	private MarkerOptions markerOption;
	private Marker marker;
	private OnLocationChangedListener mListener;
	private AMapLocationClient mlocationClient;
	private AMapLocationClientOption mLocationOption;
	private double custLat; 	//纬度
	private double custLon; 	//	经度

	private PoiSearch.Query query;

	//你编码对象
	private GeocodeSearch geocoderSearch;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.locationsource_activity);
		mapView = (MapView) findViewById(R.id.map);
		geocoderSearch = new GeocodeSearch(this);
		geocoderSearch.setOnGeocodeSearchListener(this);
		mapView.onCreate(savedInstanceState);// 此方法必须重写
		init();
	}

	/**
	 * 初始化AMap对象
	 */
	private void init() {
		if (aMap == null) {
			aMap = mapView.getMap();
			aMap.setOnMapClickListener(this);
			aMap.moveCamera(CameraUpdateFactory.zoomTo(15)); //缩放比例
			aMap.setLocationSource(this);// 设置定位监听
			aMap.getUiSettings().setMyLocationButtonEnabled(true);// 设置默认定位按钮是否显示
			aMap.setMyLocationEnabled(true);
		}
	}

	/**
	 * 定位成功后回调函数
	 */
	@Override
	public void onLocationChanged(AMapLocation amapLocation) {
		if (mListener != null && amapLocation != null) {
			if (amapLocation != null
					&& amapLocation.getErrorCode() == 0) {
				LatLng la = new LatLng(amapLocation.getLatitude(), amapLocation.getLongitude());
				addMarkersToMap(la);// 往地图上添加marker
				mListener.onLocationChanged(amapLocation);// 显示系统小蓝点
				deactivate();
			} else {
				String errText = "定位失败," + amapLocation.getErrorCode()+ ": " + amapLocation.getErrorInfo();
				Log.e("AmapErr",errText);
				Toast.makeText(this,errText,Toast.LENGTH_SHORT).show();
			}
		}
	}

	/**
	 * 激活定位
	 */
	@Override
	public void activate(OnLocationChangedListener listener) {
		mListener = listener;
		if (mlocationClient == null) {
			mlocationClient = new AMapLocationClient(this);
			mLocationOption = new AMapLocationClientOption();
			//设置定位监听
			mlocationClient.setLocationListener(this);
			//设置为高精度定位模式
			mLocationOption.setLocationMode(AMapLocationMode.Hight_Accuracy);
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
	 * 在地图上添加marker
	 */
	private void addMarkersToMap(LatLng latlng) {
		aMap.clear();
		markerOption = new MarkerOptions().icon(BitmapDescriptorFactory
				.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
				.position(latlng)
				.title("标题")
				.snippet("详细信息")
				.draggable(true);
		markerOption.icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_dw));
		marker = aMap.addMarker(markerOption);
		marker.showInfoWindow();
		getAddress(latlng);
		aMap.moveCamera(CameraUpdateFactory.changeLatLng(latlng));
	}
	@Override
	public void onMapClick(LatLng latLng) {
		aMap.clear();
		addMarkersToMap(latLng);
		aMap.moveCamera(CameraUpdateFactory.changeLatLng(latLng));
	}

	/**
	 * 根据经纬度得到地址
	 */
	public void getAddress(final LatLng latLonPoint) {
		// 第一个参数表示一个Latlng，第二参数表示范围多少米，第三个参数表示是火系坐标系还是GPS原生坐标系
		RegeocodeQuery query = new RegeocodeQuery(convertToLatLonPoint(latLonPoint), 50, GeocodeSearch.AMAP);
		geocoderSearch.getFromLocationAsyn(query);// 设置同步逆地理编码请求
	}

	@Override
	public void onRegeocodeSearched(RegeocodeResult result, int rCode) {
		Log.e("map",rCode+"");
		if (rCode == 1000) {
			if (result != null && result.getRegeocodeAddress() != null&& result.getRegeocodeAddress().getFormatAddress() != null) {
				markerOption.title(result.getRegeocodeAddress().getCity());
				markerOption.snippet(result.getRegeocodeAddress().getFormatAddress()+"附近");

				marker = aMap.addMarker(markerOption);
				marker.showInfoWindow();


			}
		}
	}

	/**
	 * 周边搜索
	 * @param latLonPoint
	 */
	private void searchBound(LatLonPoint latLonPoint){
		if (query==null){
			query = new PoiSearch.Query("keyWord", "", "cityCode");
//				//keyWord表示搜索字符串，
//				//第二个参数表示POI搜索类型，二者选填其一，选用POI搜索类型时建议填写类型代码，码表可以参考下方（而非文字）
//				//cityCode表示POI搜索区域，可以是城市编码也可以是城市名称，也可以传空字符串，空字符串代表全国在全国范围内进行搜索
			query.setPageSize(30);// 设置每页最多返回多少条poiitem
			query.setPageNum(0);//设置查询页码
		}

		PoiSearch poiSearch = new PoiSearch(this, query);
		PoiSearch.SearchBound  searchBound = new PoiSearch.SearchBound(latLonPoint,3000);
		poiSearch.setBound(searchBound);
		poiSearch.setOnPoiSearchListener(this);
		poiSearch.searchPOIAsyn();
	}

	@Override
	public void onGeocodeSearched(GeocodeResult result, int rCode) {
		if (rCode==1000){
			Log.e("map",rCode+"");
		}
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


	/**
	 * 方法必须重写
	 */
	@Override
	protected void onResume() {
		super.onResume();
		mapView.onResume();
	}

	/**
	 * 方法必须重写
	 */
	@Override
	protected void onPause() {
		super.onPause();
		mapView.onPause();
		deactivate();
	}

	/**
	 * 方法必须重写
	 */
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		mapView.onSaveInstanceState(outState);
	}

	/**
	 * 方法必须重写
	 */
	@Override
	protected void onDestroy() {
		super.onDestroy();
		mapView.onDestroy();
	}


	@Override
	public void onMapScreenShot(Bitmap bitmap) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		if(null == bitmap){
			return;
		}
		try {
			FileOutputStream fos = new FileOutputStream(
					Environment.getExternalStorageDirectory() + "/test_"
							+ sdf.format(new Date()) + ".png");
			boolean b = bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
			try {
				fos.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				fos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			if (b)
				ToastUtil.showInfo(this, "截屏成功");
			else {
				ToastUtil.showInfo(this, "截屏失败");
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onPoiSearched(PoiResult poiResult, int i) {

	}

	@Override
	public void onPoiItemSearched(PoiItem poiItem, int i) {

	}
}
