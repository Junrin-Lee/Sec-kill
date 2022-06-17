package cn.lrl.generator.service;

import cn.lrl.generator.pojo.Order;
import cn.lrl.vo.GoodsVo;
import cn.lrl.vo.OrderDetailVo;
import cn.lrl.vo.OrderVo;
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
public interface IOrderService extends IService<Order> {

    void doGoodsOrder(Long userId, GoodsVo goodsVo);

    OrderDetailVo getDetail(Long orderId);

    List<OrderVo> getOrderMsgByUid(Long userId);

    List<OrderVo> getConsumerOrderMsgByUid(Long userId);

    List<OrderVo> getConsumerOrderMsgByUid(Long userId, String orderId);

    void updateUserAddr(Long orderId, String addr);

    List<OrderVo> getOrderMsgByUid(Long userId, String orderId);

    long getResult(Long userId, Long goodsId);

    void delOrders(List<Order> orders);
}
