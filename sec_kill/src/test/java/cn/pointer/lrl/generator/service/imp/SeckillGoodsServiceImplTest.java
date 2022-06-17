package cn.pointer.lrl.generator.service.imp;

import cn.pointer.lrl.vo.SeckillGoodsVo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class SeckillGoodsServiceImplTest {

    @Autowired
    SeckillGoodsServiceImpl seckillGoodsService;

    @Test
    void selectSeckillGoodsMyself() {
        System.out.println(seckillGoodsService.selectSeckillGoodsMyself(13751378427L));
    }

    @Test
    void addSeckillGoods() {
    }
}