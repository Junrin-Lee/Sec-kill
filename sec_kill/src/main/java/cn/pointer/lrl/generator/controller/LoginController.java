package cn.pointer.lrl.generator.controller;

import cn.pointer.lrl.exception.CaptchaException;
import cn.pointer.lrl.exception.LoginException;
import cn.pointer.lrl.exception.RegisterException;
import cn.pointer.lrl.generator.service.CommonService;
import cn.pointer.lrl.generator.service.IOrderService;
import cn.pointer.lrl.generator.service.IUserService;
import cn.pointer.lrl.utils.CookieUtil;
import cn.pointer.lrl.utils.Md5Util;
import cn.pointer.lrl.utils.RedisUtil;
import cn.pointer.lrl.utils.UUIDUtil;
import cn.pointer.lrl.vo.LoginVo;
import cn.pointer.lrl.vo.RespMsgEnum;
import cn.pointer.lrl.vo.RespMsgVo;
import cn.pointer.lrl.vo.UserVo;
import com.alibaba.fastjson.JSON;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotEmpty;

@RestController
@RequestMapping("/login")
public class LoginController {

    @Autowired
    IUserService userService;

    @Autowired
    CommonService commonService;

    @Autowired
    RedisUtil redisUtil;

    /**
     * 登录
     *
     * @param from_vo        用户登录信息
     * @param captcha        验证码
     * @param loginCookies   用户登录cookies
     * @param captchaCookies 验证码时间戳(captchaId)
     * @return RespMsgVo
     */
    @PostMapping("/doLogin")
    public RespMsgVo toLogin(@Validated LoginVo from_vo, @NotEmpty String captcha,
                             HttpServletRequest request, HttpServletResponse response,
                             @CookieValue(name = "userLoginCookie", required = false) String loginCookies,
                             @CookieValue(name = "captchaId", required = false) String captchaCookies) {

        // 首先判断验证码有效性--前端
        if (StringUtils.isEmpty(captchaCookies)) {
            throw new CaptchaException(RespMsgEnum.CAPTCHA_EXPIRE);
        }

        // 其次判断验证码正确性
        boolean isAvailableCaptcha = commonService.checkCaptcha(captchaCookies, captcha);
        if (!isAvailableCaptcha) {
            throw new CaptchaException(RespMsgEnum.CAPTCHA_ERROR);
        }

        /* 用户是二次登陆 */
        // 客户端有ticket，服务端有对应的session
        if (!StringUtils.isEmpty(loginCookies) && redisUtil.get(loginCookies) != null) {
            return RespMsgVo.success();
        }

        /* 用户是第一次登录 */
        // 前端信息匹配数据库信息
        LoginVo to_vo = userService.login(from_vo);

        /* 设置最近登录时间 */
        userService.setLoginTime(to_vo.getMobile());

        // 设置客户端cookies
        String uuid = UUIDUtil.uuid();
        // MD5加密(用户登录信息+uuid)
        String ticket = Md5Util.md5(JSON.toJSONString(to_vo) + uuid);
        CookieUtil.setCookie(request, response, "userLoginCookie", ticket);

        // loginVo存入redis中
        redisUtil.set(ticket, to_vo, 6000);

        // 并发测试接口
//        return RespMsgVo.success(ticket);
        return RespMsgVo.success();
    }

    /**
     * 注册用户
     *
     * @param loginVo 前端用户信息
     * @return RespMsgVo
     */
    @PostMapping("/doRegister")
    public RespMsgVo register(@Validated LoginVo loginVo) {
        userService.register(loginVo);

        return RespMsgVo.success();
    }

    /**
     * 退出登录
     *
     * @param request      请求
     * @param response     响应
     * @param loginCookies 登录token
     * @return RespMsgVo
     */
    @PostMapping("/doLogout")
    public RespMsgVo logout(HttpServletRequest request, HttpServletResponse response,
                            @CookieValue(name = "userLoginCookie") String loginCookies) {
        try {
            // 清除session
            redisUtil.del(loginCookies);
            // 清除cookies
            CookieUtil.deleteCookie(request, response, "userLoginCookie");
        } catch (Exception e) {
            throw new LoginException(RespMsgEnum.LOGOUT_ERROR);
        }
        return RespMsgVo.success();
    }
}
