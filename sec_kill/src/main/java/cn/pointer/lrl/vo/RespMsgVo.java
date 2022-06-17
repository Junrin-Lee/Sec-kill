package cn.pointer.lrl.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * 前端消息返回类
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Accessors(chain = true)
public class RespMsgVo {
    private long code;
    private String message;
    private Object object;
    private Object data;

    // 不带数据的成功传递
    public static RespMsgVo success() {
        return new RespMsgVo(RespMsgEnum.SUCCESS.getCode(), RespMsgEnum.SUCCESS.getMessage(), null, null);
    }

    // 带数据的成功传递
    public static RespMsgVo success(Object obj) {
        return new RespMsgVo(RespMsgEnum.SUCCESS.getCode(), RespMsgEnum.SUCCESS.getMessage(), obj, null);
    }

    //
    public static RespMsgVo success(RespMsgEnum respMsgEnum, Object data) {
        return new RespMsgVo(respMsgEnum.getCode(), respMsgEnum.getMessage(), null, data);
    }

    // 不带额外数据的错误传递
    public static RespMsgVo error() {
        return new RespMsgVo(RespMsgEnum.ERROR.getCode(), RespMsgEnum.ERROR.getMessage(), null, null);
    }

    // 带数据的错误传递
    public static RespMsgVo error(RespMsgEnum respMsgEnum, Object obj) {
        return new RespMsgVo(respMsgEnum.getCode(), respMsgEnum.getMessage(), obj, null);
    }
}
