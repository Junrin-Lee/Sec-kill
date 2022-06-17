package cn.lrl.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidateUtil {
    /**
     * 手机号验证
     *
     * @param str
     * @return 验证通过返回true
     */
    public static boolean isMobile(final String str) {
        Pattern p = null;
        Matcher m = null;
        boolean flag = false;

        if (str == null) {
            return false;
        }

        p = Pattern.compile("^[1][3,4,5,7,8][0-9]{9}$"); // 验证手机号
        m = p.matcher(str);
        flag = m.matches();
        return flag;
    }
}
