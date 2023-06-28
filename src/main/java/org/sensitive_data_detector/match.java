package org.sensitive_data_detector;
import java.util.regex.*;

public class match {
    public static String moblie_phone_pattern = "1[356789]\\d{9}";//移动电话
    public static String phone_pattern = "0\\d{2,3}-[1-9]\\d{6,7}";//固话
    public static String id_pattern = "([1-9]\\d{5}(18|19|([23]\\d))\\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\\d{3}[0-9Xx])|([1-9]\\d{5}\\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\\d{2})";
//    public static String
    public static String email_pattern = "[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(?:\\.[a-zA-Z0-9_-]+)";
    public static String bank_card_pattern =" ([1-9]{1})(\\d{15}|\\d{18})";
    public static String digit_pattern = "0|1|2|3|4|5|6|7|8|9";
    public static String address = "(ns|nsf|nz|nt)";
    public static void main(String[] args) {
        String rex = "\\d*";
        String nb = "2858941676@qq.com";
        System.out.println(nb.matches(email_pattern));
    }

    public static String check_id(String value)
    {
        return null;
    }

    public static String check_phone(String value)
    {
        return null;
    }

    public static String check_mobile_phone(String value)
    {
        return null;
    }

    public static String check_email(String value)
    {
        return null;
    }

    public static String check_bank_card(String value)
    {
        return null;
    }

    public static String check_chinese_address_and_name(String value)
    {
        return null;
    }

    public static String auto_check_secret(String value)
    {
        if(value.length() <= 1)
            return "无风险";
        else if(check_id(value) != null && value.length() == 18 && value.length() == 15)
            return "身份证"+value;
        else if(check_bank_card(value) != null && value.length() == 16 && value.length() == 19)
            return "银行卡"+value;
        else if(check_phone(value) != null)
            return "固话"+value;
        else if(check_mobile_phone(value) != null)
            return "固话"+value;
        else if(check_email(value) != null)
            return "固话"+value;
        return value;
    }


}

