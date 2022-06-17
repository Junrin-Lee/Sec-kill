package cn.lrl.generator.controller;

import cn.lrl.generator.service.IUserService;
import cn.lrl.vo.LoginVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
public class IndexController {

    @Autowired
    private IUserService userService;

    /**
     * 首页跳转
     *
     * @param vo 用户登录信息
     * @return ModelAndView
     */
    @GetMapping("/")
    public ModelAndView index(@RequestAttribute(name = "login_vo") LoginVo vo) {
        // 页面转发
        ModelAndView mv = new ModelAndView("index");

        // 设置用户名字
        String username = userService.getUserName(Long.parseLong(vo.getMobile()));
        if (username == null || StringUtils.isEmpty(username)) {
            mv.addObject("username", vo.getMobile());
        } else {
            mv.addObject("username", username);
        }
        return mv;
    }
}
