package cn.pointer.lrl.interceptor;

import cn.pointer.lrl.annotation.AccessLimited;
import cn.pointer.lrl.exception.AccessLimitedException;
import cn.pointer.lrl.utils.IPUtils;
import cn.pointer.lrl.utils.RedisUtil;
import cn.pointer.lrl.utils.SpringUtil;
import cn.pointer.lrl.vo.RespMsgEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 接口限流，防盗刷拦截器
 */
@Slf4j
public class AccessLimitedInterceptor implements HandlerInterceptor {

    RedisUtil redisUtil = SpringUtil.getBean(RedisUtil.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 判断是不是需要被拦截的方法
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }

        // 捕获注解
        AccessLimited accessLimit = ((HandlerMethod) handler).getMethod().getAnnotation(AccessLimited.class);
        if (accessLimit == null) {
            return true;
        }

        // 接口的访问最大时间和次数
        int second = accessLimit.second();
        int maxCount = accessLimit.maxCount();

        // 路径和ip
        String uri = request.getRequestURI();
        String ip = IPUtils.getIpAddr(request);

        // redis键值对
        String key = "accessLimited::" + ip + "-->" + uri;

        Integer count = null;
        boolean flag = redisUtil.hasKey(key);
        if (flag) {
            count = Integer.valueOf(String.valueOf(redisUtil.get(key)));
        }

        //第一次访问记录到redis，第二次则累加1，否则超出访问次数
        if (null == count) {
            redisUtil.set(key, 1, second);
        } else if (count < maxCount) {
            redisUtil.incr(key, 1);
        } else {
            //超出访问次数
            throw new AccessLimitedException(RespMsgEnum.ACCESS_ERROR);
        }
        return true;
    }
}