package cn.lrl.generator.service;

import cn.lrl.generator.pojo.SeckillGoods;
import cn.lrl.vo.SeckillGoodsVo;
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
