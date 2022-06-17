package cn.lrl.generator.pojo;

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
@TableName("t_seckill_goods")
public class SeckillGoods {

    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 商品id
     */
    private Long goodsId;

    /**
     * 秒杀价格
     */
    private BigDecimal seckillPrice;

    /**
     * 秒杀库存
     */
    private Integer stockCount;

    /**
     * 秒杀开始时间
     */
    private Date startTime;

    /**
     * 秒杀结束时间
     */
    private Date endTime;

    /**
     * 秒杀商品创建时间
     */
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private Date createTime;

    /**
     * 秒杀商品修改时间
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


}
