package cn.lrl.generator.pojo;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
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
@TableName("t_goods")
public class Goods {

    /**
     * 商品id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 商品名称
     */
    private String goodsName;

    /**
     * 商品价格
     */
    private BigDecimal goodsPrice;

    /**
     * 商品图片
     */
    private String goodsImg;

    /**
     * 商品图片
     */
    private String goodsPic;

    /**
     * 商品标题
     */
    private String goodsTitle;

    /**
     * 商品库存 -1标识没有限制
     */
    private Integer goodsStock;

    /**
     * 商品详情
     */
    private String goodsDetail;

    /**
     * 商品创建者Id
     */
    private Long createUserId;

    /**
     * 商品创建时间
     */
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private Date createTime;

    /**
     * 商品修改时间
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
