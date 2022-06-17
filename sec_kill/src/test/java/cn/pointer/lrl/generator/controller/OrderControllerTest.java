package cn.pointer.lrl.generator.controller;

import cn.pointer.lrl.vo.LoginVo;
import com.alibaba.fastjson.JSON;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class OrderControllerTest {
    @Autowired
    OrderController orderController;

    @Test
    void getOrderByOrderId() {
//        System.out.println(JSON.toJSONString(orderController.getOrderByOrderId(new LoginVo().setMobile("13751378427"), 55L)));
    }
}