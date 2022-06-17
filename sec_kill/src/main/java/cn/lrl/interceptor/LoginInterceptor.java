package cn.lrl.interceptor;

import cn.lrl.utils.CookieUtil;
import cn.lrl.utils.RedisUtil;
import cn.lrl.utils.SpringUtil;
import cn.lrl.vo.LoginVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
public class LoginInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
                             Object handler) throws Exception {

        // 获取客户端cookies
        String loginCookie = CookieUtil.getCookieValue(request, "userLoginCookie");

        // 判断登录cookies的存在性
        if (StringUtils.isEmpty(loginCookie)) {
            log.debug("cookies信息过期！");
            request.getRequestDispatcher("/toLogin").forward(request, response);
            return false;
        }

        // 判断
        RedisUtil redisUtil = SpringUtil.getBean(RedisUtil.class);
        LoginVo loginVo = (LoginVo) redisUtil.get(loginCookie);

        // 判断登录的session存在性
        if (loginVo == null) {
            log.debug("session信息过期！");
            request.getRequestDispatcher("/toLogin").forward(request, response);
            return false;
        }

        request.setAttribute("login_vo", loginVo);
        return true;
    }
}
