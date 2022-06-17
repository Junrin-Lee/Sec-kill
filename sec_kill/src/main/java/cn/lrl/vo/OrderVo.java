package cn.lrl.vo;

import cn.lrl.generator.pojo.Order;

public class OrderVo extends Order {

    /**
     * 商品图片
     */
    private String goodsImg;

    public String getGoodsImg() {
        return goodsImg;
    }

    public void setGoodsImg(String goodsImg) {
        this.goodsImg = goodsImg;
    }
}
