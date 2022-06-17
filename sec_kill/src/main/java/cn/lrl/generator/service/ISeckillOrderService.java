package cn.lrl.generator.service;

import cn.lrl.generator.pojo.SeckillOrder;
import cn.lrl.vo.SeckillGoodsVo;
import cn.lrl.vo.SeckillOrderDetailVo;
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
