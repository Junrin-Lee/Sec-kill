package cn.pointer.lrl.generator.service.imp;

import cn.pointer.lrl.exception.CaptchaException;
import cn.pointer.lrl.exception.UploadException;
import cn.pointer.lrl.generator.mapper.GoodsMapper;
import cn.pointer.lrl.generator.mapper.OrderMapper;
import cn.pointer.lrl.generator.mapper.SeckillGoodsMapper;
import cn.pointer.lrl.generator.pojo.Goods;
import cn.pointer.lrl.generator.pojo.SeckillGoods;
import cn.pointer.lrl.generator.service.CommonService;
import cn.pointer.lrl.utils.CookieUtil;
import cn.pointer.lrl.utils.Md5Util;
import cn.pointer.lrl.utils.RedisUtil;
import cn.pointer.lrl.utils.UploadUtil;
import cn.pointer.lrl.vo.RespMsgEnum;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wf.captcha.ArithmeticCaptcha;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Objects;
import java.util.UUID;

@Service
@Slf4j
public class CommonServiceImpl implements CommonService {

    @Autowired
    RedisUtil redisUtil;

    @Autowired
    GoodsMapper goodsMapper;

    @Autowired
    SeckillGoodsMapper seckillGoodsMapper;

    /**
     * 创建随机路径
     *
     * @param userId         用户id
     * @param seckillGoodsId 秒杀商品id
     * @return path
     */
    @Override
    public String createPath(long userId, Long seckillGoodsId) {
        // 路径加密
        String path = Md5Util.md5(UUID.randomUUID().toString() + userId);

        // 存入redis
        redisUtil.set("seckillPath:" + userId + "-" + seckillGoodsId, path, 600);
        return path;
    }

    /**
     * 校验随机路径
     *
     * @param userId         用户id
     * @param seckillGoodsId 秒杀商品id
     * @param path           传入路径
     * @return boolean
     */
    @Override
    public boolean checkPath(long userId, Long seckillGoodsId, String path) {
        // 如果是不存在的商品id或是path为空值
        if (seckillGoodsId < 0 || StringUtils.isEmpty(path)) {
            return false;
        }

        // 去redis匹配path
        String orderPathFromRedis = (String) redisUtil.get("seckillPath:" + userId + "-" + seckillGoodsId);
        return path.equals(orderPathFromRedis);
    }

    /**
     * 生成验证码
     *
     * @param request   请求
     * @param response  响应
     * @param timeStamp 时间戳
     */
    @Override
    public void createCaptcha(HttpServletRequest request, HttpServletResponse response, String timeStamp) {
        // 设置请求头为输出图片类型
        response.setContentType("image/gif");
        response.setHeader("Pragma", "No-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);

        // 算术类型验证码
        ArithmeticCaptcha captcha = new ArithmeticCaptcha(130, 30, 3);

        // redis存入验证码的结果<captchaId,captcha>
        redisUtil.set("captcha:" + timeStamp, captcha.text(), 60);

        //将captcha时间戳存入前端cookies
        CookieUtil.setCookie(request, response, "captchaId", timeStamp, 60);
        try {
            captcha.out(response.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 校验验证码
     *
     * @param captchaId 验证码id
     * @param captcha   验证码
     * @return boolean
     */
    public boolean checkCaptcha(String captchaId, String captcha) {
        // 匹配存入redis的验证码密码
        String captchaFromRedis = (String) redisUtil.get("captcha:" + captchaId);

        // 验证码过期--后端
        if (captchaFromRedis == null) {
            throw new CaptchaException(RespMsgEnum.CAPTCHA_EXPIRE);
        }

        return captcha.equals(captchaFromRedis);
    }

    /**
     * 文件上传
     *
     * @param file 文件
     * @return 文件名
     */
    @Override
    public String uploadImg(MultipartFile file) {
        String path = null;// 文件上传路径
        String image = null;// 文件名
        try {
            // 捕获项目发布路径
            path = java.net.URLDecoder.decode(Objects.requireNonNull(this.getClass().getResource("/")).getPath().substring(1), "utf-8");
            InputStream is = file.getInputStream();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];

            // NIO写入流
            int length;
            while ((length = is.read(buffer)) > -1) {
                baos.write(buffer, 0, length);
            }
            baos.flush();

            // 文件上传
            image = UploadUtil.uploadImage(file, path);
        } catch (UnsupportedEncodingException e) {
            throw new UploadException(RespMsgEnum.UPLOAD_IMG_DECODE_ERROR);
        } catch (IOException e) {
            log.warn(e.getMessage());
            throw new UploadException(RespMsgEnum.UPLOAD_IMG_ERROR);
        }
        return image;
    }

    /**
     * 普通商品检测刷单
     *
     * @param userId  用户id
     * @param goodsId 商品id
     * @return boolean
     */
    @Override
    public boolean isGoodCreate(Long userId, Long goodsId) {
        Long count = goodsMapper.selectCount(new QueryWrapper<Goods>()
                .eq("id", goodsId)
                .eq("create_user_id", userId)
        );
        return count > 0;
    }

    /**
     * 秒杀商品检测刷单
     *
     * @param userId         用户id
     * @param seckillGoodsId 秒杀商品id
     * @return boolean
     */
    @Override
    public boolean isSeckillGoodCreate(Long userId, Long seckillGoodsId) {
        int count = seckillGoodsMapper.isSeckillGoodsCreate(userId, seckillGoodsId);
        return count == 1;
    }
}
