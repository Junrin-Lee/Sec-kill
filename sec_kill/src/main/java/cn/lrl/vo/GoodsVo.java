package cn.lrl.vo;

import cn.lrl.generator.pojo.Goods;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 商品前端响应模型
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class GoodsVo extends Goods {
    private BigDecimal secKillPrice;
    private Integer stockCount;
    private Date startTime;
    private Date endTime;
}
