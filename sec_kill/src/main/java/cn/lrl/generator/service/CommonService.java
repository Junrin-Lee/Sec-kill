package cn.lrl.generator.service;


import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface CommonService {

    String createPath(long userId, Long seckillGoodsId);

    boolean checkPath(long userId, Long seckillGoodsId, String path);

    void createCaptcha(HttpServletRequest request, HttpServletResponse response, String timeStamp);

    boolean checkCaptcha(String captchaId, String captcha);

    String uploadImg(MultipartFile file);

    boolean isGoodCreate(Long userId, Long goodsId);

    boolean isSeckillGoodCreate(Long userId, Long goodsId);

}
