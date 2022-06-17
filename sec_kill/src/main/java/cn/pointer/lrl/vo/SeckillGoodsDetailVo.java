package cn.pointer.lrl.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SeckillGoodsDetailVo {

    private SeckillGoodsVo seckillGoodsVo;

    private String producer;
    private int duringSeconds;
    private int seckillStatus;
    private int remainSeconds;

    public SeckillGoodsDetailVo(SeckillGoodsVo seckillGoodsVo, String producer) {
        this.seckillGoodsVo = seckillGoodsVo;
        this.producer = producer;
    }
}
