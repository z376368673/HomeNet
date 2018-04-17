package com.benkie.hjw.utils;


import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeUtil {
	public static String getYMDTimes(String cc_time) {
		String re_StrTime = null;
		if ( null == cc_time )
		{
		    return null;
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		// 例如：cc_time=1291778220
		long lcc_time = Long.valueOf(cc_time);
		re_StrTime = sdf.format(new Date(lcc_time * 1000L));

		return re_StrTime;
	}
	/**
	 * 时间转化格式
	 */
	public static String getYMDTimeYR(String cc_time) {
		String re_StrTime = null;
		if ( null == cc_time )
		{
		    return null;
		}
		SimpleDateFormat sdf = new SimpleDateFormat("MM月dd日");
		// 例如：cc_time=1291778220
		long lcc_time = Long.valueOf(cc_time);
		re_StrTime = sdf.format(new Date(lcc_time * 1000L));

		return re_StrTime;
	}
	/**
	 * 时间转化格式
	 */
	public static String getYMDTime(String cc_time) {
		String re_StrTime = null;
		if ( null == cc_time )
		{
		    return null;
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		// 例如：cc_time=1291778220
		long lcc_time = Long.valueOf(cc_time);
		re_StrTime = sdf.format(new Date(lcc_time * 1000L));

		return re_StrTime;
	}
	/**
	 * 时间转化格式
	 */
	public static String getYMDTimeymd(String cc_time) {
		String re_StrTime = null;
		if ( null == cc_time )
		{
		    return null;
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		// 例如：cc_time=1291778220
		long lcc_time = Long.valueOf(cc_time);
		re_StrTime = sdf.format(new Date(lcc_time * 1000L));

		return re_StrTime;
	}
	
}
