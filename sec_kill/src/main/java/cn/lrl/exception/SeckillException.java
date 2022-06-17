package cn.lrl.exception;

import cn.lrl.vo.RespMsgEnum;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@EqualsAndHashCode(callSuper = true)
@Getter
@Setter
@AllArgsConstructor
public class SeckillException extends RuntimeException {
    private RespMsgEnum respMsgEnum;
}
