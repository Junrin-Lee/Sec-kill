package cn.pointer.lrl.generator.controller;

import cn.pointer.lrl.annotation.AccessLimited;
import cn.pointer.lrl.generator.service.CommonService;
import cn.pointer.lrl.generator.service.ISystemMenuService;
import cn.pointer.lrl.utils.UploadUtil;
import cn.pointer.lrl.vo.LoginVo;
import cn.pointer.lrl.vo.RespMsgEnum;
import cn.pointer.lrl.vo.RespMsgVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;
import java.io.*;
import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("/common")
public class CommonController {

    @Autowired
    private ISystemMenuService systemMenuService;

    @Autowired
    CommonService commonService;

    /**
     * 生成每个用户这一次请求的唯一的抢购路径
     *
     * @param vo      用户信息
     * @param seckillGoodsId 需要抢购的秒杀商品id
     * @return 前端响应对象
     */
    @GetMapping("/path")
    @AccessLimited(second = 30, maxCount = 5)
    public RespMsgVo getPath(
            @RequestAttribute("login_vo") LoginVo vo,
            @NotNull Long seckillGoodsId) {

        // 创建随机路径
        String path = commonService.createPath(Long.parseLong(vo.getMobile()), seckillGoodsId);
        return RespMsgVo.success(path);
    }

    /**
     * 生成验证码
     *
     * @param request 请求
     * @param response 响应
     * @param timeStamp 时间戳
     */
    @GetMapping("/captcha")
    public void getCaptcha(HttpServletRequest request, HttpServletResponse response, String timeStamp) {
        commonService.createCaptcha(request, response, timeStamp);
    }

    /**
     * 首页菜单栏
     *
     * @return Map<String, Object>
     */
    @GetMapping("/menu")
    @Cacheable(cacheNames = "dataCache", key = "'menu'")
    public Map<String, Object> menu() {
        return systemMenuService.menu();
    }

    /**
     * 上传商品图片
     */
    @PostMapping("/upload")
    public RespMsgVo upload(MultipartFile file) {
        String image = commonService.uploadImg(file);
        return RespMsgVo.success(RespMsgEnum.UPLOAD_IMG_SUCCESS, image);
    }

}
