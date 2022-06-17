$(function () {
    getDetails();
});

// 获取商品详情
function getDetails() {
    var goodsId = hash_getQueryString("goodsId");
    $.ajax({
        url: '/goods/toDetail/' + goodsId,
        type: 'GET',
        success: function (data) {
            if (data.code == 200) {
                render(data.object)
            } else {
                layer.msg(data.message)
            }
        },
        error: function () {
            layer.msg("客户端请求出错")
        }
    })
}

// 页面解析
function render(goodsDetail) {
    var goods=goodsDetail.goodsVo
    $("#goodsName").text(goods.goodsName);
    $("#goodsImg").attr("src", goods.goodsImg);
    $("#goodsId").val(goods.id);
    if (goodsDetail.producer!=null){
        $("#goodsProducer").text(goodsDetail.producer);
    }else{
        $("#goodsProducer").text(goods.createUserId);
    }
    $("#goodsTitle").val(goods.goodsTitle);
    $("#goodsPrice").text(goods.goodsPrice);
    $("#goodsStock").text(goods.goodsStock);
    $("#goodsDetail").text(goods.goodsDetail);
}

// 异步自旋等结果
function getResult(goodsId) {
    g_showLoading();
    $.ajax({
        url: "/order/result",
        type: 'GET',
        data: {
            goodsId: goodsId
        },
        success: function (data) {
            var result = data.object;
            if (result < 0) {
                layer.msg("对不起，下单失败！")
            } else if (result == 0) {
                setTimeout(function () {
                    getResult(goodsId)
                }, 1000)
                layer.close()
            } else {
                layer.confirm("恭喜你，下单成功！！！\n是否查看订单", {btn: ["确定", "取消"]},
                    function (index) {
                        layui.use('miniPage', function () {
                            layer.close(index)
                            var miniPage = layui.miniPage;
                            miniPage.hashChange('orderDetail.htm?orderId=' + result);
                        })
                    },
                    function () {
                        layer.close()
                    }
                )
            }
        },
        error: function () {

        }
    })
}

// 发起异步下单请求
function doOrder() {
    $.ajax({
        url: '/order/doOrder',
        type: 'POST',
        data: {
            goodsId: $("#goodsId").val(),
            stockCount: 1
        },
        success: function (data) {
            if (data.code == 200) {
                getResult($('#goodsId').val())
            } else {
                layer.msg("错误信息：" + data.message)
            }
        },
        error: function (data) {
            layer.msg("错误信息：" + data.message)
        }
    })
}