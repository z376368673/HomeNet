package com.benkie.hjw.utils;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationClientOption.AMapLocationMode;
import com.amap.api.location.AMapLocationListener;

public class LocationUtil {
	private AMapLocationClient locationClient;
	private AMapLocationClientOption locationOption;
	private Context context;
	private Handler mHandler;
	public LocationUtil(Context context,Handler mHandler) {
		// TODO Auto-generated constructor stub
		this.context = context;
		this.mHandler = mHandler;
	}

	/**
	 * 初始化定位，设置基本定位信息
	 * 
	 */
	public void getLoationData(Handler mHandler) {
		locationClient = new AMapLocationClient(context);
		locationOption = new AMapLocationClientOption();
		
		// 设置定位模式为低功耗模式
		locationOption.setLocationMode(AMapLocationMode.Hight_Accuracy);
		// 设置为单次定位
		locationOption.setOnceLocation(false);
		// 设置是否需要显示地址信息
		locationOption.setNeedAddress(true);
		// 设置是否开启缓存
		locationOption.setLocationCacheEnable(true);
		// 设置是否等待设备wifi刷新，如果设置为true,会自动变为单次定位，持续定位时不要使用
		locationOption.setOnceLocationLatest(true);
		// 一个小时定位一次
		locationOption.setInterval(1000);
		// 设置定位参数
		locationClient.setLocationOption(locationOption);
		// 启动定位
		locationClient.startLocation();

		// 设置定位监听
		locationClient.setLocationListener(listener);
		
		mHandler.sendEmptyMessage(10111);
	}
	
	AMapLocationListener listener = new AMapLocationListener() {
		
		@Override
		public void onLocationChanged(AMapLocation loc) {
			// TODO Auto-generated method stub
			if (null != loc) {
				Message msg = mHandler.obtainMessage();
				msg.obj = loc;
				msg.what = 10111;
				//msg.what = Utils.MSG_LOCATION_FINISH;
				mHandler.sendMessage(msg);
			}
		}
	};

//使用方法 
	
//	LocationUtil locationUtil = new LocationUtil(getActivity(),
//			mHandler);
//	locationUtil.getLoationData(mHandler);
	
	
	
//	public Handler mHandler = new Handler() {
//		public void dispatchMessage(android.os.Message msg) {
//			switch (msg.what) {
//			// 定位完成
//			case 10111:
//				AMapLocation loc = (AMapLocation) msg.obj;
//				
//				break;
//			default:
//				break;
//			}
//		};
//	};
	
	/**
	 * 根据定位结果返回定位信息的字符串
	 * @param loc
	 * @return
	 */
	public synchronized static String getLocationStr(AMapLocation location){
		if(null == location){
			return null;
		}
		StringBuffer sb = new StringBuffer();
		//errCode等于0代表定位成功，其他的为定位失败，具体的可以参照官网定位错误码说明
		if(location.getErrorCode() == 0){
			sb.append("定位成功" + "\n");
			sb.append("定位类型: " + location.getLocationType() + "\n");
			sb.append("经    度    : " + location.getLongitude() + "\n");
			sb.append("纬    度    : " + location.getLatitude() + "\n");
			sb.append("精    度    : " + location.getAccuracy() + "米" + "\n");
			sb.append("提供者    : " + location.getProvider() + "\n");
			
			if (location.getProvider().equalsIgnoreCase(
					android.location.LocationManager.GPS_PROVIDER)) {
				// 以下信息只有提供者是GPS时才会有
				sb.append("速    度    : " + location.getSpeed() + "米/秒" + "\n");
				sb.append("角    度    : " + location.getBearing() + "\n");
				// 获取当前提供定位服务的卫星个数
				sb.append("星    数    : "
						+ location.getSatellites() + "\n");
			} else {
				// 提供者是GPS时是没有以下信息的
				sb.append("国    家    : " + location.getCountry() + "\n");
				sb.append("省            : " + location.getProvince() + "\n");
				sb.append("市            : " + location.getCity() + "\n");
				sb.append("城市编码 : " + location.getCityCode() + "\n");
				sb.append("区            : " + location.getDistrict() + "\n");
				sb.append("区域 码   : " + location.getAdCode() + "\n");
				sb.append("地    址    : " + location.getAddress() + "\n");
				sb.append("兴趣点    : " + location.getPoiName() + "\n");
			}
		} else {
			//定位失败
			sb.append("定位失败" + "\n");
			sb.append("错误码:" + location.getErrorCode() + "\n");
			sb.append("错误信息:" + location.getErrorInfo() + "\n");
			sb.append("错误描述:" + location.getLocationDetail() + "\n");
			System.out.println(location.getLocationDetail()+location.getErrorInfo()+"-----------------");
		}
		return sb.toString();
	}
}
