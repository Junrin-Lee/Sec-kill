package cn.lrl.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

/**
 * 前端状态码类
 */
@Getter
@ToString
@AllArgsConstructor
public enum RespMsgEnum {

    // 数据接口对接
    DATA_SUCCESS(0, "SUCCESS"),

    // 统一成功
    SUCCESS(200, "SUCCESS"),

    // 后端统一错误
    ERROR(500, "服务器异常！"),

    // 验证码
    CAPTCHA_ERROR(10001, "验证码输入错误！"),
    CAPTCHA_EXPIRE(10001, "验证码已过期，请点击验证码进行刷新！"),

    // 登录失败返回
    LOGIN_ERROR(50001, "登陆失败，账号或密码错误！"),
    MOBILE_ERROR(50002, "手机号格式错误！"),
    MOBILE_NOT_EXIST(50003, "用户名不存在！"),
    LOGOUT_ERROR(50004, "退出登录失败，请联系后台工作人员！"),

    // 注册
    REGISTER_ERROR(50501, "注册失败，请联系后台人员！"),

    // 秒杀
    DUPLICATE_SECKILL(80001, "秒杀商品不能重复抢购！"),
    BUYYOURSELF_ERROR(80002, "不能购买自己的商品！"),

    // 订单
    ORDER_NOT_EXIST(60001, "订单不存在！！！"),
    EMPTY_SECKILL_STOCK(60002, "库存不足，秒杀失败！"),
    EMPTY_GOODS_STOCK(60003, "库存不足，下单失败！"),


    // 服务器安全
    ACCESS_ERROR(70001, "访问过于频繁，请稍后再试！"),
    ILLEGAL_REQUEST(70002, "非法请求！！！"),

    // 用户
    USER_UPDATE_MSG_ERROR(80001, "修改用户名错误！"),
    USER_UPDATE_PWD_ERROR(80002, "修改用户密码错误！"),
    USER_PWD_CHECK_ERROR(80003, "密码错误，请正确输入旧密码！"),
    USER_UPDATE_ADDR_ERROR(80004, "修改收获地址失败，请联系后台工作人员！"),

    // 商品
    GOODS_ADD_ERROR(20001, "商品添加失败，请检查商品信息！"),
    GOODS_UPDATE_ERROR(20002, "商品信息修改失败，请检查商品信息！"),
    GOODS_DELETE_ERROR(20003, "商品信息删除失败，请检查商品信息！"),

    // 秒杀商品
    SECKILLGOODS_ADD_ERROR(30001, "秒杀商品添加失败，请检查商品信息！"),
    SECKILLGOODS_GOODEXIST_ERROR(30002, "秒杀商品添加失败，请不要重复添加秒杀商品！"),
    SECKILLGOODS_UPDATE_ERROR(30003, "秒杀商品信息修改失败，请检查商品信息！"),
    SECKILLGOODS_DELETE_ERROR(30004, "秒杀商品信息删除失败，请检查商品信息！"),

    // 文件上传
    UPLOAD_IMG_SUCCESS(6666, "图片上传成功！"),
    UPLOAD_IMG_DECODE_ERROR(6667, "图片上传编码路径有误！请联系后台工作人员！"),
    UPLOAD_IMG_ERROR(6668, "图片上传过程出现错误，请联系后台工作人员！");


    private final Integer code;
    private final String message;


    /**
     * 服务器加密盐值
     */
    public static final String SALT = "cn.lrl";


}
