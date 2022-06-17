package cn.pointer.lrl.generator.service;

import cn.pointer.lrl.generator.pojo.SeckillGoods;
import cn.pointer.lrl.vo.GoodsVo;
import cn.pointer.lrl.vo.LoginVo;
import cn.pointer.lrl.vo.SeckillGoodsVo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author lrl
 * @since 2022-01-13
 */
public interface ISeckillGoodsService extends IService<SeckillGoods> {
    List<SeckillGoodsVo> findSeckillGoodsVo();

    SeckillGoodsVo findSeckillGoodsVoById(Long seckillGoodsId);

    List<SeckillGoods> findSeckillGoods();

    List<SeckillGoodsVo> selectSeckillGoodsMyself(Long mobile);

    List<SeckillGoodsVo> selectSeckillGoodsMyself(Long mobile, SeckillGoodsVo seckillGoodsVo);

    void modifiedSeckillGood(SeckillGoodsVo seckillGoodsVo);

    void addSeckillGoods(Long mobile,SeckillGoodsVo seckillGoodsVo);

    void delSeckillGoods(List<SeckillGoodsVo> seckillGoodsList);
}
