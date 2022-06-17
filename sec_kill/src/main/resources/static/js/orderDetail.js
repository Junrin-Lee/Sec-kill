$(function () {
    getDetails();
});

function render(detail) {
    var order = detail.order;
    var goods = detail.goodsVo;
    var status = order.status;
    var statusText = ""

    $('#goodsName').text(goods.goodsName);
    $("#goodsImg").attr("src", goods.goodsImg);
    $('#goodsPrice').text(goods.goodsPrice);
    $("#createTime").text(new Date(order.createTime).format("yyyy年MM月dd日 HH:mm:ss"));
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

function getDetails() {
    var orderId = hash_getQueryString("orderId");

    $.ajax({
        url: '/order/toDetail',
        type: 'GET',
        data: {
            orderId: orderId
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