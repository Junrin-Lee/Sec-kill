package cn.pointer.lrl;

import cn.pointer.lrl.utils.Md5Util;
import org.apache.commons.codec.digest.DigestUtils;

import static cn.pointer.lrl.vo.RespMsgEnum.SALT;

public class testMD5 {
    // md5加密
    public static String md5(String str) {
        return DigestUtils.md5Hex(str);
    }

    public static void main(String[] args) {
        System.out.println("123123");
        System.out.println(Md5Util.F_Pass("123123", "1a2b3c4d"));
        System.out.println(Md5Util.serverPass(Md5Util.F_Pass("123123", "1a2b3c4d"), "fc7ee28e27fb4941a0a420ff59a97ba2" + SALT));
    }
}
