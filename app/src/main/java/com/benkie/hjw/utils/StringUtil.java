package com.benkie.hjw.utils;


import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.CharacterStyle;
import android.text.style.ForegroundColorSpan;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

/**
 * 字符串工具类
 * 
 * @version 1.0
 * @date 2011-04-12
 */
public class StringUtil {

	public static final String NUM_XXX00 = "#,###.00";
	public static final String NUM_XXX0 = "#,###.0";
	public static final String NUM_XXX = "#,###";
	public static final String CHAR_ISO_8859_1 = "ISO-8859-1";

	public static final String CHAR_UTF_8 = "utf-8";

	public static void main(String[] args) {
		try {
			BigDecimal bigDecimal = new BigDecimal(1234.0);
			System.out.println(bigDecimal.longValue());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 字符串前面补位 如"3" 补成3位的字符位 "003"
	 * 
	 * @param input
	 *            要补位的字符串
	 * @param vchar
	 *            用来补位的字符
	 * @param length
	 *            要补到的长度
	 * @return 返回补位后的字符串
	 */
	public static String lpad(String input, String vchar, int length) {
		StringBuffer buffer = new StringBuffer(input);
		while (buffer.toString().length() < length) {
			buffer.insert(0, vchar);
		}
		return buffer.toString();
	}

	/**
	 * 字符串后面补位 如"3" 补成3位的字符位 "300"
	 * 
	 * @param input
	 *            要补位的字符串
	 * @param vchar
	 *            用来补位的字符
	 * @param length
	 *            要补到的长度
	 * @return 返回补位后的字符串
	 * @author 葛旭
	 */
	public static String rpad(String input, String vchar, int length) {
		StringBuffer buffer = new StringBuffer(input);
		while (buffer.toString().length() < length) {
			buffer.append(vchar);
		}
		return buffer.toString();
	}

	/**
	 * 字符串判空
	 * 
	 * @param str
	 * @return 空是true
	 */
	public static boolean isNull(Object str) {
		return str != null && !"".equals(str) && !"null".equals(str)
				&& !"".equals(str.toString().trim()) ? false : true;
	}

	public static boolean isNulls(Object... strs) {
		for (Object str : strs) {
			if (StringUtil.isNull(str)) {
				return true;
			}
		}
		return false;
	}

	public static String null2Zero(Object str) {
		return isNull(str) ? "0" : str.toString();
	}

	public static String null2Empty(Object str) {
		return isNull(str) ? "" : str.toString();
	}

	/**
	 * 编码
	 */
	public static String encode(String s, String char_set) {
		try {
			s = java.net.URLEncoder.encode(s, char_set);
		} catch (UnsupportedEncodingException e) {
		}
		return s;
	}

	public static String decode(String s) {
		try {
			s = java.net.URLDecoder.decode(s);
		} catch (Exception e) {
		}
		return s;
	}

	public static String decode(String s, String char_set) {
		try {
			s = java.net.URLDecoder.decode(s, char_set);
		} catch (UnsupportedEncodingException e) {
		}
		return s;
	}

	public static String convert(String str, String scharset, String tcharset) {
		if (isNull(str)) {
			return "";
		}
		try {
			return new String(str.trim().getBytes(scharset), tcharset);
		} catch (UnsupportedEncodingException e) {
		}
		return "";
	}

	public static String formatNum(String str, String pattren) {
		try {
			DecimalFormat df1 = new DecimalFormat(pattren);
			BigDecimal bd = new BigDecimal(str);
			str = df1.format(bd);
			if (str.startsWith(".")) {
				str = "0" + str;
			}
		} catch (RuntimeException e) {
		}
		return str;
	}

	public static String formatNum(String str) {
		try {
			DecimalFormat df1 = new DecimalFormat("#,###");
			BigDecimal bd = new BigDecimal(str);
			str = df1.format(bd);
			if (str.startsWith(".")) {
				str = "0" + str;
			}
		} catch (RuntimeException e) {
		}
		return str;
	}

	public static int Obj2Int(Object str, int _default) {
		if (!StringUtil.isNull(str)) {
			try {
				return new BigDecimal(str.toString()).intValue();
			} catch (Exception e) {
			}
		}
		return _default;
	}

	public static int Obj2Int(Object str) {
		return Obj2Int(str, 0);
	}

	public static long Obj2Long(Object str, long l) {
		if (!StringUtil.isNull(str)) {
			try {
				return new BigDecimal(str.toString()).longValue();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return l;
	}

	public static long Obj2Long(Object str) {
		return Obj2Long(str, 0);
	}

	public static float Obj2Float(Object str, float f) {
		if (!StringUtil.isNull(str)) {
			try {
				return new BigDecimal(str.toString()).floatValue();
			} catch (Exception e) {
			}
		}
		return f;
	}

	public static float Obj2Float(Object str) {
		return Obj2Float(str, 0);
	}

	/**
	 * 字符串转换成整数数组
	 * 
	 * @param string
	 *            待转换的字符串
	 * @return 整数数组
	 */
	public static Integer[] StringToIntegerArray(String string) {
		Integer[] status;
		String[] stringArr;
		string.toCharArray();
		stringArr = string.split(",");
		status = new Integer[stringArr.length];
		for (int i = 0; i < stringArr.length; i++) {
			status[i] = Integer.parseInt(stringArr[i]);
		}
		return status;
	}

	/**
	 * 判断字符串是否是邮箱
	 * 
	 * @return
	 */
	public static boolean isEmail(String str) {

		return str
				.matches("^\\s*\\w+(?:\\.{0,1}[\\w-]+)*@[a-zA-Z0-9]+(?:[-.][a-zA-Z0-9]+)*\\.[a-zA-Z]+\\s*$");

	}

	/**
	 * 根据日期格式把字符串转换成日期
	 * 
	 * @param dateStr
	 *            日期字符串
	 * @param dateFormat
	 *            日期格式：yyyy-MM-dd
	 * @return 日期
	 */
	public Date stringToDate(String dateStr, String dateFormat) {
		DateFormat format = new SimpleDateFormat(dateFormat);
		Date dt = null;
		try {
			dt = format.parse(dateStr);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return dt;
	}

	/**
	 * 随机生成字符串
	 * 
	 * @param pwd_len
	 * @return
	 */
	public static String genRandomNum(int pwd_len) {
		final int maxNum = 36;
		int i;
		int count = 0;
		char[] str = { 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k',
				'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w',
				'x', 'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' };
		StringBuffer pwd = new StringBuffer("");
		Random r = new Random();
		while (count < pwd_len) {
			i = Math.abs(r.nextInt(maxNum));
			if (i >= 0 && i < str.length) {
				pwd.append(str[i]);
				count++;
			}
		}
		return pwd.toString();
	}

	public static String reverse(Object object) {
		return object == null ? "" : object.toString();
	}
	
	
	  
	  
	  public final static String UTF_8 = "utf-8";

		/** 判断字符串是否有值，如果为null或者是空字符串或者只有空格或者为"null"字符串，则返回true，否则则返回false */
		public static boolean isStrEmpty(String value) {
			if (value != null && !"".equalsIgnoreCase(value.trim()) && !"null".equalsIgnoreCase(value.trim())) {
				return false;
			} else {
				return true;
			}
		}

		/** 判断多个字符串是否相等，如果其中有一个为空字符串或者null，则返回false，只有全相等才返回true */
		public static boolean isEquals(String... agrs) {
			String last = null;
			for (int i = 0; i < agrs.length; i++) {
				String str = agrs[i];
				if (isStrEmpty(str)) {
					return false;
				}
				if (last != null && !str.equalsIgnoreCase(last)) {
					return false;
				}
				last = str;
			}
			return true;
		}

		/**
		 * 返回一个高亮spannable
		 * @param content 文本内容
		 * @param color   高亮颜色
		 * @param start   起始位置
		 * @param end     结束位置
		 * @return 高亮spannable
		 */
		public static CharSequence getHighLightText(String content, int color, int start, int end) {
			if (TextUtils.isEmpty(content)) {
				return "";
			}
			start = start >= 0 ? start : 0;
			end = end <= content.length() ? end : content.length();
			SpannableString spannable = new SpannableString(content);
			CharacterStyle span = new ForegroundColorSpan(color);
			spannable.setSpan(span, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
			return spannable;
		}
}
