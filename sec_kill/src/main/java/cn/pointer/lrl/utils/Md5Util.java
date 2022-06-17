package cn.pointer.lrl.utils;

import org.apache.commons.codec.digest.DigestUtils;

public class Md5Util {
    // md5加密
    public static String md5(String str) {
        return DigestUtils.md5Hex(str);
    }

    // 前端md5加密
    public static String F_Pass(String str, String salt) {
        String pass = "" + salt.charAt(0) + salt.charAt(2) + str + salt.charAt(5) + salt.charAt(4);
        return md5(pass);
    }

    //
    public static String serverPass(String str, String salt) {
        String pass = str + salt;
        return md5(pass);
    }
}
