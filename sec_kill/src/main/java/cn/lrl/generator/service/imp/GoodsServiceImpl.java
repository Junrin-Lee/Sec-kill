package cn.lrl.generator.service.imp;

import cn.lrl.exception.GoodsException;
import cn.lrl.generator.mapper.GoodsMapper;
import cn.lrl.generator.pojo.Goods;
import cn.lrl.generator.service.IGoodsService;
import cn.lrl.vo.GoodsVo;
import cn.lrl.vo.RespMsgEnum;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author lrl
 * @since 2022-01-13
 */
@Service
@Slf4j
public class GoodsServiceImpl extends ServiceImpl<GoodsMapper, Goods> implements IGoodsService {

    @Autowired
    GoodsMapper goodsMapper;

    @Override
    public List<Goods> findGoodsList() {
        return goodsMapper.selectList(new QueryWrapper<>());
    }

    @Override
    public GoodsVo findGoodById(Long goodsId) {
        return goodsMapper.findGoodsVOById(goodsId);
    }

    @Override
    public void addGoods(List<Goods> goodsList) {
        for (Goods good : goodsList) {
            try {
                goodsMapper.insert(good);
            } catch (Exception e) {
                throw new GoodsException(RespMsgEnum.GOODS_ADD_ERROR);
            }
        }
    }

    @Override
    @Transactional
    public void addGoods(Goods good) {
        try {
            goodsMapper.insert(good);
        } catch (Exception e) {
            throw new GoodsException(RespMsgEnum.GOODS_ADD_ERROR);
        }
    }

    @Override
    @Transactional
    public void modifiedGood(Goods good) {
        try {
            goodsMapper.updateById(good);
        } catch (Exception e) {
            throw new GoodsException(RespMsgEnum.GOODS_UPDATE_ERROR);
        }
    }

    @Override
    @Cacheable(cacheNames = "dataCache", key = "#p0+'::myGoods'")
    public List<Goods> selectGoodsMyself(Long userId) {
        return goodsMapper.selectList(new QueryWrapper<Goods>().eq("create_user_id", userId));
    }

    @Override
    @CachePut(cacheNames = "dataCache", key = "#p0+'::myGoods'")
    public List<Goods> selectGoodsMyself(Long userId, Goods good) {
        return goodsMapper.getGoodsByMyself(userId, good.getId(), good.getGoodsName());
    }

    @Override
    @Transactional
    public void delGoods(List<Goods> goods) {
        for (Goods good : goods) {
            int count = goodsMapper.deleteById(good.getId());
            if (count > 0) {
                continue;
            } else {
                throw new GoodsException(RespMsgEnum.GOODS_DELETE_ERROR);
            }
        }
    }
}
