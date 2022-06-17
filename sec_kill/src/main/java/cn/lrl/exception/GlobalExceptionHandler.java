package cn.lrl.exception;

import cn.lrl.vo.RespMsgVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(BindException.class)
    public RespMsgVo loginBindExceptionHandler(BindException e) {
        String msg = e.getBindingResult().getAllErrors().get(0).getDefaultMessage();
        log.warn(msg);
        return new RespMsgVo()
                .setCode(5000000)
                .setMessage(msg);
    }

    @ExceptionHandler(LoginException.class)
    public RespMsgVo loginException(LoginException e) {
        log.warn(e.getRespMsgEnum().getMessage());
        return RespMsgVo.error(e.getRespMsgEnum(), null);
    }

    @ExceptionHandler(CaptchaException.class)
    public RespMsgVo captchaException(CaptchaException e) {
        log.warn(e.getRespMsgEnum().getMessage());
        return RespMsgVo.error(e.getRespMsgEnum(), null);
    }

    @ExceptionHandler(AccessLimitedException.class)
    public RespMsgVo accessLimitedException(AccessLimitedException e) {
        log.warn(e.getRespMsgEnum().getMessage());
        return RespMsgVo.error(e.getRespMsgEnum(), null);
    }

    @ExceptionHandler(UserException.class)
    public RespMsgVo userException(UserException e) {
        log.warn(e.getRespMsgEnum().getMessage());
        return RespMsgVo.error(e.getRespMsgEnum(), null);
    }

    @ExceptionHandler(RegisterException.class)
    public RespMsgVo registerException(RegisterException e) {
        log.warn(e.getRespMsgEnum().getMessage());
        return RespMsgVo.error(e.getRespMsgEnum(), null);
    }

    @ExceptionHandler(OrderException.class)
    public RespMsgVo OrderException(OrderException e) {
        log.warn(e.getRespMsgEnum().getMessage());
        return RespMsgVo.error(e.getRespMsgEnum(), null);
    }

    @ExceptionHandler(SeckillException.class)
    public RespMsgVo SeckillException(SeckillException e) {
        log.warn(e.getRespMsgEnum().getMessage());
        return RespMsgVo.error(e.getRespMsgEnum(), null);
    }

    @ExceptionHandler(UploadException.class)
    public RespMsgVo UploadException(UploadException e) {
        log.warn(e.getRespMsgEnum().getMessage());
        return RespMsgVo.error(e.getRespMsgEnum(), null);
    }

    @ExceptionHandler(GoodsException.class)
    public RespMsgVo GoodsException(GoodsException e) {
        log.warn(e.getRespMsgEnum().getMessage());
        return RespMsgVo.error(e.getRespMsgEnum(), null);
    }

    @ExceptionHandler(SeckillGoodsException.class)
    public RespMsgVo SeckillGoodsException(SeckillGoodsException e) {
        log.warn(e.getRespMsgEnum().getMessage());
        return RespMsgVo.error(e.getRespMsgEnum(), null);
    }

    @ExceptionHandler(Exception.class)
    public RespMsgVo ExceptionHandler(Exception e) {
        log.warn(e.getMessage());
        return RespMsgVo.error();
    }
}
