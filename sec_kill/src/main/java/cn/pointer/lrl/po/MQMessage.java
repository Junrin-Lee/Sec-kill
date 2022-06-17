package cn.pointer.lrl.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class MQMessage {
    private Long userId;

    private Long goodsId;

    private Long seckillGoodsId;

    private Integer stockCount;

}
