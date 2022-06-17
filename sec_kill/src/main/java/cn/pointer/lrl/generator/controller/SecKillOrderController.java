package cn.pointer.lrl.generator.controller;


import cn.pointer.lrl.generator.service.ISeckillOrderService;
import cn.pointer.lrl.generator.service.imp.SeckillOrderServiceImpl;
import cn.pointer.lrl.vo.LoginVo;
import cn.pointer.lrl.vo.OrderDetailVo;
import cn.pointer.lrl.vo.RespMsgVo;
import cn.pointer.lrl.vo.SeckillOrderDetailVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;

/**
 * <p>
 * 秒杀订单
 * </p>
 *
 * @author lrl
 * @since 2022-01-13
 */
@RestController
@RequestMapping("/seckillOrder")
public class SecKillOrderController {

    @Autowired
    ISeckillOrderService seckillOrderService;

    /**
     * 查询订单完成信息
     *
     * @param vo      用户登录信息
     * @param seckillGoodsId 秒杀商品id
     * @return RespMsgVo
     */
    @GetMapping("/result")
    public RespMsgVo getResult(@RequestAttribute("login_vo") LoginVo vo, @NotNull Long seckillGoodsId) {
        long orderId = seckillOrderService.getResult(Long.valueOf(vo.getMobile()), seckillGoodsId);
        return RespMsgVo.success(orderId);
    }

    /**
     * 秒杀商品订单详情
     *
     * @param seckillOrderId 秒杀订单号
     * @return RespMsgVo
     */
    @GetMapping("/toDetail")
    public RespMsgVo getOrderHtml(@NotNull Long seckillOrderId) {
        SeckillOrderDetailVo seckillOrderDetailVo = seckillOrderService.getDetail(seckillOrderId);
        return RespMsgVo.success(seckillOrderDetailVo);
    }
}

