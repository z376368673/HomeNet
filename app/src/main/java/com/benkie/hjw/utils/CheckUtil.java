package com.benkie.hjw.utils;

import android.text.TextUtils;

import java.util.regex.Pattern;

/**
 * Created by Administrator on 2017/4/5.
 */

public class CheckUtil {

    public static int checkAll(boolean[] judges) {
        int len = judges.length;
        for (int i = 0; i < len; i++) {
            if (!judges[i]) {
                return i;
            }
        }
        return len;
    }

    /**
     * 是否手机号
     * @param mobile
     * @return
     */
    public static boolean isMobile(String mobile) {
        return Pattern.compile("^0?(13[0-9]|15[012356789]|17[013678]|18[0-9]|14[57])[0-9]{8}$").matcher(mobile).matches();
    }

    /**
     * 是否4-6位验证码
     * @param code
     * @return
     */
    public static boolean isValifyCode(String code) {
        return Pattern.compile("^\\d{4,6}$").matcher(code).matches();
    }



    /**
     * 验证密码 6-18位
     * @param pass
     * @return
     */
    public static boolean isPass(String pass) {
        return Pattern.compile("^\\w{6,18}$").matcher(pass).matches();
    }

    /**
     * 是否相等
     * @param content1
     * @param content2
     * @return
     */
    public static boolean isSame(String content1, String content2) {
        return content1.equals(content2);
    }

    public static boolean isName(String realName) {
        return Pattern.compile("^[a-zA-Z·]{2,}|[\\u4E00-\\u9FA5]{2,}$").matcher(realName).matches();
    }

    /**
     * 长度大于length的字符串
     * @param text
     * @param length
     * @return
     */
    public static boolean isText(String text,int length) {
      return   text!=null&&!TextUtils.isEmpty(text)&&text.length()>=length;
        //return Pattern.compile("^[a-zA-Z·]{2,}|[\\u4E00-\\u9FA5]{2,}$").matcher(realName).matches();
    }
    /**
     * 是否银行卡号
     * @param number
     * @return
     */
    public static boolean isBankCardNo(String number) {
        return Pattern.compile("^\\d{16}|\\d{19}$").matcher(number).matches();
    }
    /**
     * 是不是身份证号
     * @param pass
     * @return
     */
    public static boolean identityCard(String pass) {
        return Pattern.compile("^(^\\d{18}$)|(^\\d{17}(\\d|X|x)$)").matcher(pass).matches();
    }
    public static boolean isIDCard15(String idCardNo) {
        return Pattern.compile("^[1-9]\\d{5}\\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\\d{2}$").matcher(idCardNo).matches();
    }

    public static boolean isIDCard18(String idCardNo) {
        return Pattern.compile("^[1-9]\\d{5}(18|19|([23]\\d))\\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\\d{3}[0-9Xx]$").matcher(idCardNo).matches();
    }

    /**
     * 是否邮箱
     * @param email
     * @return
     */
    public static boolean isEmail(String email) {
        return Pattern.compile("^([a-zA-Z0-9]+[_|\\-|\\.]?)*[a-zA-Z0-9]+@([a-zA-Z0-9]+[_|\\-|\\.]?)*[a-zA-Z0-9]+(\\.[a-zA-Z]{2,3})+$").matcher(email).matches();
    }

    /**
     * 时间格式
     * @param dateTime
     * @return
     */
    public static boolean isDateTime(String dateTime) {
        return Pattern.compile("^(201[7-9])-((0[13578]|1[02])-(0[1-9]|[12][0-9]|3[01])|(0[469]|11)-(0[1-9]|[12][0-9]|3[0])|02-(0[1-9]|[12][0-9])) ([0-1][0-9]|2[0-3]):([0-5][0-9])$").matcher(dateTime).matches();
    }

    /**
     * 是否包含表情
     * @param source
     * @return
     */
    public static boolean containsEmoji(String source) {
        int len = source.length();
        boolean isEmoji = false;
        for (int i = 0; i < len; i++) {
            char hs = source.charAt(i);
            if (0xd800 <= hs && hs <= 0xdbff) {
                if (source.length() > 1) {
                    char ls = source.charAt(i+1);
                    int uc = ((hs - 0xd800) * 0x400) + (ls - 0xdc00) + 0x10000;
                    if (0x1d000 <= uc && uc <= 0x1f77f) {
                        return true;
                    }
                }
            } else {
                // non surrogate
                if (0x2100 <= hs && hs <= 0x27ff && hs != 0x263b) {
                    return true;
                } else if (0x2B05 <= hs && hs <= 0x2b07) {
                    return true;
                } else if (0x2934 <= hs && hs <= 0x2935) {
                    return true;
                } else if (0x3297 <= hs && hs <= 0x3299) {
                    return true;
                } else if (hs == 0xa9 || hs == 0xae || hs == 0x303d || hs == 0x3030 || hs == 0x2b55 || hs == 0x2b1c || hs == 0x2b1b || hs == 0x2b50|| hs == 0x231a ) {
                    return true;
                }
                if (!isEmoji && source.length() > 1 && i < source.length() -1) {
                    char ls = source.charAt(i+1);
                    if (ls == 0x20e3) {
                        return true;
                    }
                }
            }
        }
        return  isEmoji;
    }
}
