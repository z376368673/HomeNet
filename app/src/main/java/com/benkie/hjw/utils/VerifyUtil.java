package com.benkie.hjw.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2017/2/22.
 * 验证工具类
 */

public class VerifyUtil {
    public static int checkAll(boolean[] judges) {
        int len = judges.length;
        for (int i = 0; i < len; i++) {
            if (!judges[i]) {
                return i;
            }
        }
        return len;
    }
    public static boolean isNumeric(String str){
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(str);
        if( !isNum.matches() ){
            return false;
        }
        return true;
    }
    public static boolean isValifyCode(String code) {
        return Pattern.compile("^\\d{4,6}$").matcher(code).matches();
    }

    public static boolean isPass(String pass) {
        return Pattern.compile("^\\w{6,18}$").matcher(pass).matches();
    }

    public static boolean isSame(String content1, String content2) {
        return content1.equals(content2);
    }

    public static boolean isName(String realName) {
        return Pattern.compile("^[a-zA-Z·]{2,}|[\\u4E00-\\u9FA5]{2,}$").matcher(realName).matches();
    }

    public static boolean isBankCardNo(String number) {
        return Pattern.compile("^\\d{16}|\\d{19}$").matcher(number).matches();
    }
    /**
     * 验证中国大陆手机号码
     *
     * @param mobile
     * @return
     */
    public static boolean isMobile(String mobile) {
        return Pattern.compile("^1[34578]\\d{9}$").matcher(mobile).matches();
    }

    /**
     * 验证固定电话
     *
     * @param phone
     * @return
     */
    public static boolean isPhone(String phone) {
        return Pattern.compile("^(\\(\\d{3,4}\\)|\\d{3,4}-|\\s)?\\d{7,14}$").matcher(phone).matches();
    }

    public static boolean isIDCard15(String idCardNo) {
        return Pattern.compile("^[1-9]\\d{5}\\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\\d{2}$").matcher(idCardNo).matches();
    }

    public static boolean isIDCard18(String idCardNo) {
        return Pattern.compile("^[1-9]\\d{5}(18|19|([23]\\d))\\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\\d{3}[0-9Xx]$").matcher(idCardNo).matches();
    }

    public static boolean isEmail(String email) {
        return Pattern.compile("^([a-zA-Z0-9]+[_|\\-|\\.]?)*[a-zA-Z0-9]+@([a-zA-Z0-9]+[_|\\-|\\.]?)*[a-zA-Z0-9]+(\\.[a-zA-Z]{2,3})+$").matcher(email).matches();
    }

    public static boolean isDateTime(String dateTime) {
        return Pattern.compile("^(201[7-9])-((0[13578]|1[02])-(0[1-9]|[12][0-9]|3[01])|(0[469]|11)-(0[1-9]|[12][0-9]|3[0])|02-(0[1-9]|[12][0-9])) ([0-1][0-9]|2[0-3]):([0-5][0-9])$").matcher(dateTime).matches();
    }

}
