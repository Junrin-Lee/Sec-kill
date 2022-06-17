package cn.pointer.lrl.generator.mapper;

import cn.pointer.lrl.generator.pojo.SeckillGoods;
import cn.pointer.lrl.vo.SeckillGoodsVo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class SeckillGoodsMapperTest {

    @Autowired
    SeckillGoodsMapper seckillGoodsMapper;

    @Test
    void findSeckillGoodsVo() {
        List<SeckillGoodsVo> seckillGoodsVo = seckillGoodsMapper.findSeckillGoodsVo();
        System.out.println(seckillGoodsVo.toString());
        SeckillGoodsVo seckillGoodsVo1 = new SeckillGoodsVo();
        System.out.println(seckillGoodsVo1);
    }
}