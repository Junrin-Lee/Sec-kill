$(function () {
    getDetails();
});

function getDetails() {
    var seckillOrderId = hash_getQueryString("seckillOrderId");

    $.ajax({
        url: '/seckillOrder/toDetail',
        type: 'GET',
        data: {
            seckillOrderId: seckillOrderId
        },
        success: function (data) {
            if (data.code == 200) {
                render(data.object)
            } else {
                layer.msg(data.message)
            }
        },
        error: function () {
            layer.msg(data.message)
        }
    })
}

function render(detail) {
    var order = detail.order;
    var seckillGoods = detail.seckillGoodsVo;
    var status = order.status;
    var statusText = ""

    $('#goodsName').text(seckillGoods.goodsName);
    $("#goodsImg").attr("src", seckillGoods.goodsImg);
    $('#goodsPrice').text(seckillGoods.goodsPrice);
    $("#createTime").text(new Date(order.createTime).format("yyyy-MM-dd HH:mm:ss"));
    switch (status) {
        case 0:
            statusText = "未支付"
            break;
        case 1:
            statusText = "已支付"
            break;
        case 2:
            statusText = "已发货"
            break;
        case 3:
            statusText = "已收货"
            break;
        case 4:
            statusText = "已退款"
            break;
    }
    $('#status').text(statusText);
}