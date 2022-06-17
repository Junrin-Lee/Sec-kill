package cn.lrl.generator.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author lrl
 * @since 2022-01-13
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("t_seckill_order")
public class SeckillOrder {

    /**
     * 秒杀订单id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 用户iD
     */
    private Long userId;

    /**
     * 订单id
     */
    private Long orderId;

    /**
     * 商品id
     */
    private Long goodsId;


}
