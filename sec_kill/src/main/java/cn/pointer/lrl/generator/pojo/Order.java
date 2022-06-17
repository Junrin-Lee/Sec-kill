package cn.pointer.lrl.generator.pojo;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

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
@TableName("t_order")
public class Order {

    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 商品id
     */
    private Long goodsId;

    /**
     * 收货地址
     */
    private String deliveryAddr;

    /**
     * 商品名称
     */
    private String goodsName;

    /**
     * 商品数量
     */
    private Integer goodsCount;

    /**
     * 商品价格
     */
    private BigDecimal goodsPrice;

    /**
     * 1:pc 2: android 3: ios
     */
    private Integer orderChannel;

    /**
     * 0:普通商品 1:秒杀商品
     */
    private Integer type;

    /**
     * 0:未支付 1：已支付 2：已发货 3：已收货 4：已退款
     */
    private Integer status;

    /**
     * 订单创建时间
     */
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private Date createTime;

    /**
     * 订单最近修改时间
     */
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    /**
     * 逻辑删除
     */
    @TableLogic
    private Integer deleted;

    /**
     * 乐观锁
     */
    @Version
    private Integer version;

    /**
     * 支付时间
     */
    private Date payTime;


}
