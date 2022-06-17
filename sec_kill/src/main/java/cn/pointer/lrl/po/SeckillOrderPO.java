package cn.pointer.lrl.po;

import cn.pointer.lrl.generator.pojo.SeckillOrder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class SeckillOrderPO extends SeckillOrder {
    Integer type;
}
