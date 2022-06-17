package cn.lrl.generator.controller;

import cn.lrl.exception.UserException;
import cn.lrl.generator.service.IUserService;
import cn.lrl.utils.RedisUtil;
import cn.lrl.vo.LoginVo;
import cn.lrl.vo.RespMsgEnum;
import cn.lrl.vo.RespMsgVo;
import cn.lrl.vo.UserVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 用户信息管理
 * </p>
 *
 * @author lrl
 * @since 2022-01-05
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    IUserService userService;

    @Autowired
    RedisUtil redisUtil;

    /**
     * 获取账号
     *
     * @param vo 用户登录信息
     * @return RespMsgVo
     */
    @GetMapping("/getMobile")
    public RespMsgVo getLoginVoMsg(@RequestAttribute(name = "login_vo") LoginVo vo) {
        return RespMsgVo.success(vo.getMobile());
    }

    /**
     * 修改用户信息
     *
     * @param vo 前端用户信息
     * @return RespMsgVo
     */
    @PostMapping("/updateUserMsg")
    public RespMsgVo modifiedUserMsg(@RequestBody UserVo vo) {
        userService.updateUserName(vo);

        return RespMsgVo.success();
    }

    /**
     * 修改用户密码
     *
     * @param update_vo    用户修改信息
     * @param login_vo     用户登录信息
     * @param loginCookies 用户token
     * @return RespMsgVo
     */
    @PostMapping("/updateUserPwd")
    public RespMsgVo modifiedUserPwd(@RequestAttribute(name = "login_vo") LoginVo login_vo,
                                     @CookieValue(name = "userLoginCookie", required = true) String loginCookies,
                                     @RequestBody LoginVo update_vo) {

        // 匹配旧密码
        if (!update_vo.getOldPwd().equals(login_vo.getPassword())) {
            throw new UserException(RespMsgEnum.USER_PWD_CHECK_ERROR);
        }

        // 判断数据库修改情况
        userService.updateUserPwd(update_vo);

        // 删除用户登录cookies
        redisUtil.del(loginCookies);

        return RespMsgVo.success();
    }

}

