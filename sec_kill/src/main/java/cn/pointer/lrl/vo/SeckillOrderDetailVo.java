package cn.pointer.lrl.vo;

import cn.pointer.lrl.generator.pojo.Order;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class SeckillOrderDetailVo {
    private Order order;
    private SeckillGoodsVo seckillGoodsVo;
}
