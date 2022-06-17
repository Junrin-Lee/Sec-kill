package cn.pointer.lrl.generator.service;

import cn.pointer.lrl.generator.pojo.Goods;
import cn.pointer.lrl.vo.GoodsVo;
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
public interface IGoodsService extends IService<Goods> {
    List<Goods> findGoodsList();

    GoodsVo findGoodById(Long goodsId);

    void addGoods(List<Goods> goodsList);

    void addGoods(Goods good);

    void modifiedGood(Goods good);

    List<Goods> selectGoodsMyself(Long userId);

    List<Goods> selectGoodsMyself(Long userId, Goods good);

    void delGoods(List<Goods> goods);
}
