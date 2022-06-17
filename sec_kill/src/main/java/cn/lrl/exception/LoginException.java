package cn.lrl.exception;

import cn.lrl.vo.RespMsgEnum;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Getter
@Setter
@AllArgsConstructor
public class LoginException extends RuntimeException {
    private RespMsgEnum respMsgEnum;
}
