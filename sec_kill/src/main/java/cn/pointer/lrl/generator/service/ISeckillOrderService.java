package cn.pointer.lrl.generator.service;

import cn.pointer.lrl.generator.pojo.SeckillOrder;
import cn.pointer.lrl.vo.GoodsVo;
import cn.pointer.lrl.vo.OrderDetailVo;
import cn.pointer.lrl.vo.SeckillGoodsVo;
import cn.pointer.lrl.vo.SeckillOrderDetailVo;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author lrl
 * @since 2022-01-13
 */
public interface ISeckillOrderService extends IService<SeckillOrder> {

    Long getResult(Long userId, Long seckillGoodsId);

    SeckillOrderDetailVo getDetail(Long seckillOrderId);

    void doSeckillOrder(Long userId, SeckillGoodsVo seckillGoodsVo);
}
