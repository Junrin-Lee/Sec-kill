package cn.lrl.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GoodsDetailVo {
    private GoodsVo goodsVo;

    private String producer;
    private int duringSeconds;
    private int seckillStatus;
    private int remainSeconds;

    public GoodsDetailVo(GoodsVo goodsVo, String producer) {
        this.goodsVo = goodsVo;
        this.producer = producer;
    }
}
