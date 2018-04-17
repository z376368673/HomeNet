package com.benkie.hjw.utils;


import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.StrikethroughSpan;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TextTools {
	/**
	 * java手机号码的验证
	 * @param mobiles
	 * @return
	 */
	public static boolean isMobileNO(String mobiles){     
//        Pattern p = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");     
//        Matcher m = p.matcher(mobiles);     
//        return m.matches();   

//		2G号段：134、135、136、137、138、139、150、151、152、158、159；
//		3G号段：157、182、187、188、147（上网本）
//		中国联通：
//		2G号段：130、131、132、155、156；
//		3G号段：185、186   中国电信：2G号段：133、153；3G号段：180、189

		 String check = "^(13[0,1,2,3,4,5,6,7,8,9]|15[0,8,9,1,7,2,6,3]|188|187|186|182|189|181)\\d{8}$"; 
		 Pattern regex = Pattern.compile(check); 
		 Matcher matcher = regex.matcher(mobiles); 
		 boolean isMatched = matcher.matches(); 
		 return isMatched;
    } 
   /**
    * java的邮箱的验证
    * @param email
    * @return
    */
    public static boolean isEmail(String email){     
//     String str="^([a-zA-Z0-9]*[-_]?[a-zA-Z0-9]+)*@([a-zA-Z0-9]*[-_]?[a-zA-Z0-9]+)+[\\.][A-Za-z]{2,3}([\\.][A-Za-z]{2})?$";
//        Pattern p = Pattern.compile(str);     
//        Matcher m = p.matcher(email);     
//        return m.matches();  
    	//电子邮件  
    	 String check = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";  
    	 Pattern regex = Pattern.compile(check);  
    	 Matcher matcher = regex.matcher(email);  
    	 boolean isMatched = matcher.matches(); 
    	 return isMatched;
    } 
    
    /**
     * 删除线
     * @param str
     * @return
     */
    public  static SpannableString setDeleteLine(String str) 
	{
		// TODO Auto-generated method stub
		SpannableString spanText = new SpannableString(str);
		StrikethroughSpan style=new StrikethroughSpan();
		spanText.setSpan(style, 0, spanText.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
		return spanText;
		
	}
    
    /**
     * 删除最后一个“,"截取串子
     */
   public static String getStringText(String s)
   {
	   String str=s.substring(0, s.length()-1);
	   
	return str;
	   
   }

}
