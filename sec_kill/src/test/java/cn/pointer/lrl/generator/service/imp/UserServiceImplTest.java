package cn.pointer.lrl.generator.service.imp;

import cn.pointer.lrl.generator.service.IUserService;
import cn.pointer.lrl.vo.LoginVo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserServiceImplTest {
    @Autowired
    IUserService userService;

    @Test
    void getUserName() {
//        System.out.println(userService.getUserName(new LoginVo().setMobile("13751378427")));
    }
}