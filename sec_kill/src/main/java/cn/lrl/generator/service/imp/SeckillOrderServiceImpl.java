package cn.lrl.generator.service.imp;

import cn.lrl.exception.OrderException;
import cn.lrl.exception.SeckillException;
import cn.lrl.generator.mapper.OrderMapper;
import cn.lrl.generator.mapper.SeckillOrderMapper;
import cn.lrl.generator.pojo.Order;
import cn.lrl.generator.pojo.SeckillGoods;
import cn.lrl.generator.pojo.SeckillOrder;
import cn.lrl.generator.service.ISeckillGoodsService;
import cn.lrl.generator.service.ISeckillOrderService;
import cn.lrl.po.SeckillOrderPO;
import cn.lrl.utils.RedisUtil;
import cn.lrl.vo.*;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author lrl
 * @since 2022-01-13
 */
@Service
public class SeckillOrderServiceImpl extends ServiceImpl<SeckillOrderMapper, SeckillOrder> implements ISeckillOrderService {

    @Autowired
    SeckillOrderMapper seckillOrderMapper;

    @Autowired
    ISeckillGoodsService seckillGoodsService;

    @Autowired
    OrderMapper orderMapper;

    @Autowired
    RedisUtil redisUtil;

    /**
     * 订单生成
     *
     * @param userId         用户id
     * @param seckillGoodsVo 秒杀商品
     */
    @Transactional
    @Caching(evict = {
            @CacheEvict(cacheNames = "dataCache", key = "#p0+'::orderMsg'", beforeInvocation = false),// 清空该用户的订单缓存
            @CacheEvict(cacheNames = "pageCache", key = "'seckillGoodsList'", beforeInvocation = false),// 清除秒杀商品页面缓存
    })
    public void doSeckillOrder(Long userId, SeckillGoodsVo seckillGoodsVo) {
        // 获取商品信息
//        SeckillGoods seckillGoods = seckillGoodsService.getOne(new QueryWrapper<SeckillGoods>()
//                .eq("goods_id", goodsVo.getId())
//        );

        // 修改商品库存
        boolean update = seckillGoodsService.update(new UpdateWrapper<SeckillGoods>()
                .setSql("stock_count=" + "stock_count-1")
                .eq("id", seckillGoodsVo.getId())
                .gt("stock_count", 0)
        );

        // 如果修改成功说明redis库存非空
        if (!update) {
            redisUtil.set("isStockEmpty:" + seckillGoodsVo.getId(), 0);
            throw new SeckillException(RespMsgEnum.EMPTY_SECKILL_STOCK);
        }

        // 生成订单
        Order order = new Order()
                .setUserId(userId)
                .setGoodsId(seckillGoodsVo.getId())
                .setGoodsName(seckillGoodsVo.getGoodsName())
                .setGoodsCount(1)
                .setGoodsPrice(seckillGoodsVo.getSeckillPrice())
                .setOrderChannel(1)
                .setStatus(0)
                .setType(1)
                .setCreateTime(new Date());
        orderMapper.insert(order);

        // 生成秒杀订单
        SeckillOrder seckillOrder = new SeckillOrder()
                .setOrderId(order.getId())
                .setUserId(userId)
                .setGoodsId(seckillGoodsVo.getId());

        // 数据库下单
        seckillOrderMapper.insert(seckillOrder);
        // 缓存设置订单信息
        redisUtil.set("order:" + userId + "-" + seckillGoodsVo.getId(), seckillOrder);
        // 删除该用户购买该商品的秒杀路径
        redisUtil.del("seckillPath:" + userId + "-" + seckillGoodsVo.getId());
    }

    /**
     * 异步响应订单信息
     *
     * @param userId         用户id
     * @param seckillGoodsId 秒杀商品id
     * @return Integer
     */
    @Override
    public Long getResult(Long userId, Long seckillGoodsId) {
        // 获取秒杀订单信息
        long goodsId = seckillGoodsService.getById(seckillGoodsId).getGoodsId();
        SeckillOrderPO seckillOrderPOMsg = seckillOrderMapper.getSeckillOrderPOMsg(userId, goodsId);

        if (seckillOrderPOMsg != null) {// 订单存在
            return seckillOrderPOMsg.getOrderId();
        } else if (redisUtil.hasKey("isStockEmpty:" + seckillGoodsId)) {// 库存为空
            return -1L;
        } else {// 这次请求没有捕获到订单信息
            return 0L;
        }
    }

    /**
     * 订单详情
     *
     * @param seckillOrderId 秒杀订单id
     * @return 订单前端模版返回
     */
    @Override
    public SeckillOrderDetailVo getDetail(Long seckillOrderId) {
        if (seckillOrderId == null) {
            throw new OrderException(RespMsgEnum.ORDER_NOT_EXIST);
        }

        SeckillOrder seckillOrder = seckillOrderMapper.selectById(seckillOrderId);
        Order order = orderMapper.selectById(seckillOrder.getOrderId());
        long seckillGoodsId = seckillGoodsService.getOne(new QueryWrapper<SeckillGoods>().eq("goods_id", seckillOrder.getGoodsId())).getId();

        SeckillGoodsVo seckillGoodsVo = seckillGoodsService.findSeckillGoodsVoById(seckillGoodsId);

        return new SeckillOrderDetailVo(order, seckillGoodsVo);
    }
}
