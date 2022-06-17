package cn.pointer.lrl.exception;

import cn.pointer.lrl.vo.RespMsgEnum;
import cn.pointer.lrl.vo.RespMsgVo;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Getter
@Setter
@AllArgsConstructor
public class LoginException extends RuntimeException {
    private RespMsgEnum respMsgEnum;
}
