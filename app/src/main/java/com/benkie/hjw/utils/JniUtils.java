package com.benkie.hjw.utils;



import android.content.Context;

public class JniUtils {

	static {
		System.loadLibrary("fun_melibs");
	}
	public static native String zmTosn(String str,Context c);
	
//	public static String MD5str(String str)
//	{
////		System.out.println("y:"+str);
//		
//		// 使用MD5对待签名串求签
//        byte[] bytes = null;
//        try {
//            MessageDigest md5 = MessageDigest.getInstance("MD5");
//            bytes = md5.digest(str.getBytes("UTF-8"));
//        } catch (GeneralSecurityException ex) {
//            ex.printStackTrace();
//        } catch (UnsupportedEncodingException e) {
//			e.printStackTrace();
//		}
//        if(bytes == null)
//        	return null;
//        // 将MD5输出的二进制结果转换为小写的十六进制
//        StringBuilder sign = new StringBuilder();
//        for (int i = 0; i < bytes.length; i++) {
//            String hex = Integer.toHexString(bytes[i] & 0xFF);
//            if (hex.length() == 1) {
//                sign.append("0");
//            }
//            sign.append(hex);
//        }
//        return sign.toString();
//	}
}
