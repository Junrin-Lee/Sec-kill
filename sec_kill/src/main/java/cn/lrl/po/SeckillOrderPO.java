package cn.lrl.po;

import cn.lrl.generator.pojo.SeckillOrder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class SeckillOrderPO extends SeckillOrder {
    Integer type;
}
