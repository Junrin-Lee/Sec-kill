package cn.pointer.lrl.generator.service.imp;

import cn.pointer.lrl.exception.SeckillException;
import cn.pointer.lrl.exception.SeckillGoodsException;
import cn.pointer.lrl.generator.mapper.SeckillGoodsMapper;
import cn.pointer.lrl.generator.pojo.SeckillGoods;
import cn.pointer.lrl.generator.service.ISeckillGoodsService;
import cn.pointer.lrl.utils.RedisUtil;
import cn.pointer.lrl.vo.RespMsgEnum;
import cn.pointer.lrl.vo.SeckillGoodsVo;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

import static cn.pointer.lrl.generator.controller.SecKillController.EmptyStockMap;

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
public class SeckillGoodsServiceImpl extends ServiceImpl<SeckillGoodsMapper, SeckillGoods> implements ISeckillGoodsService {

    @Autowired
    SeckillGoodsMapper seckillGoodsMapper;

    @Autowired
    RedisUtil redisUtil;

    @Override
    public List<SeckillGoodsVo> findSeckillGoodsVo() {
        return seckillGoodsMapper.findSeckillGoodsVo();
    }

    @Override
    public SeckillGoodsVo findSeckillGoodsVoById(Long seckillGoodsId) {
        return seckillGoodsMapper.findSeckillGoodsVoById(seckillGoodsId);
    }

    @Override
    public List<SeckillGoods> findSeckillGoods() {
        return seckillGoodsMapper.selectList(new QueryWrapper<SeckillGoods>());
    }

    @Override
    @Cacheable(cacheNames = "dataCache", key = "#p0+'::mySeckillGoods'")
    public List<SeckillGoodsVo> selectSeckillGoodsMyself(Long mobile) {
        return seckillGoodsMapper.getSeckillGoodsVoByMyself(mobile);
    }

    @Override
    @CachePut(cacheNames = "dataCache", key = "#p0+'::mySeckillGoods'")
    public List<SeckillGoodsVo> selectSeckillGoodsMyself(Long mobile, SeckillGoodsVo seckillGoodsVo) {
        return seckillGoodsMapper.getSeckillGoodsVoByMyselfWithLike(mobile, seckillGoodsVo.getId(), seckillGoodsVo.getGoodsName());
    }

    @Override
    @Transactional
    public void modifiedSeckillGood(SeckillGoodsVo seckillGoodsVo) {
        try {
            seckillGoodsMapper.updateById(seckillGoodsVo);
        } catch (Exception e) {
            throw new SeckillGoodsException(RespMsgEnum.SECKILLGOODS_UPDATE_ERROR);
        }
    }

    @Override
    @Transactional
    public void addSeckillGoods(Long mobile, SeckillGoodsVo seckillGoodsVo) {

        // 一个商品对应一个秒杀商品
        List<SeckillGoodsVo> seckillGoodsVoList = JSON.parseArray(JSON.toJSONString(redisUtil.get("dataCache::" + mobile + "::mySeckillGoods")), SeckillGoodsVo.class);
        if (seckillGoodsVoList != null) {
            seckillGoodsVoList.forEach(sgVo -> {
                if (sgVo.getGoodsId().equals(seckillGoodsVo.getGoodsId())) {
                    throw new SeckillGoodsException(RespMsgEnum.SECKILLGOODS_GOODEXIST_ERROR);
                }
            });
        }

        try {
            // 秒杀商品添加
            seckillGoodsMapper.insert(new SeckillGoods()
                    .setGoodsId(seckillGoodsVo.getGoodsId())
                    .setSeckillPrice(seckillGoodsVo.getSeckillPrice())
                    .setStockCount(seckillGoodsVo.getStockCount())
                    .setStartTime(seckillGoodsVo.getStartTime())
                    .setEndTime(seckillGoodsVo.getEndTime())
            );

            // 查询插入结果
            SeckillGoods one = seckillGoodsMapper.selectOne(new QueryWrapper<SeckillGoods>()
                    .eq("goods_id", seckillGoodsVo.getGoodsId())
            );

            //内存标记
            EmptyStockMap.put(one.getId(), false);

            // redis库存
            redisUtil.set("seckillGoods:" + one.getId(), seckillGoodsVo.getStockCount(), (seckillGoodsVo.getEndTime().getTime() - new Date().getTime()) / 1000);
        } catch (Exception e) {
            throw new SeckillGoodsException(RespMsgEnum.SECKILLGOODS_ADD_ERROR);
        }
    }

    @Override
    @Transactional
    public void delSeckillGoods(List<SeckillGoodsVo> seckillGoodsList) {
        for (SeckillGoodsVo seckillGoods : seckillGoodsList) {
            int count = seckillGoodsMapper.deleteById(seckillGoods.getId());
            if (count > 0) {
                EmptyStockMap.put(seckillGoods.getId(), false);
                redisUtil.del("seckillGoods:" + seckillGoods.getId());
            } else {
                throw new SeckillException(RespMsgEnum.SECKILLGOODS_DELETE_ERROR);
            }
        }
    }
}
