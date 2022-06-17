package cn.lrl.vo;

import cn.lrl.generator.pojo.SeckillGoods;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

/**
 * 秒杀商品前端响应模型
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class SeckillGoodsVo extends SeckillGoods {
    private String goodsName;
    private BigDecimal goodsPrice;
    private String goodsImg;
    private String goodsPic;
    private String goodsTitle;
    private String goodsDetail;
    private Long createUserId;
}
