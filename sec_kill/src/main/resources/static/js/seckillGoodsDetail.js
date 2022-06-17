$(function () {
    getDetails();
});

// 获取商品详情
function getDetails() {
    var seckillGoodsId = hash_getQueryString("seckillGoodsId");
    $.ajax({
        url: '/seckillGoods/toDetail/' + seckillGoodsId,
        type: 'GET',
        success: function (data) {
            if (data.code == 200) {
                render(data.object)
            } else {
                layer.msg("客户端请求出错")
            }
        },
        error: function () {
            layer.msg("客户端请求出错")
        }
    })
}

// 页面解析
function render(detail) {
    var seckillGoods = detail.seckillGoodsVo;
    var remainSeconds = detail.remainSeconds;
    var duringSeconds = detail.duringSeconds;
    var producer = detail.producer;

    if (producer != null) {
        $("#goodsProducer").text(producer);
    } else {
        $("#goodsProducer").text(seckillGoods.createUserId);
    }
    $("#goodsName").text(seckillGoods.goodsName);
    $("#goodsImg").attr("src", seckillGoods.goodsImg);
    $("#startTime").text(new Date(seckillGoods.startTime).format("yyyy-MM-dd HH:mm:ss"));
    $("#remainSeconds").val(remainSeconds);
    $("#duringSeconds").val(duringSeconds);
    debugger
    $("#seckillGoodsId").val(seckillGoods.id);
    $("#goodsPrice").text(seckillGoods.goodsPrice);
    $("#seckillPrice").text(seckillGoods.seckillPrice);
    $("#stockCount").text(seckillGoods.stockCount);

    countDown();
}

// 秒杀开始倒计时
function countDown() {
    var remainSeconds = $("#remainSeconds").val();
    var timeout;
    var duringTime;
    if (remainSeconds > 0) {
        $("#buyButton").attr("disabled", true);
        timeout = setTimeout(function () {
            $("#countDown").text(remainSeconds - 1);
            $("#remainSeconds").val(remainSeconds - 1);
            countDown();
        }, 1000)
    } else if (remainSeconds == 0) {
        $("#buyButton").attr("disabled", false);
        clearTimeout(timeout);
        $("#seckillTip").html("秒杀进行中");
        timer($('#duringSeconds').val())
    } else {
        secKillOver()
    }
}

// 秒杀结束时页面处理
function secKillOver() {
    $("#seckillTip").html("秒杀已结束");
    $("#buyButton").attr("disabled", true);
}

// 秒杀进行中倒计时
function timer(diff) {
    window.setInterval(function () {
        var duringSeconds = $("#duringSeconds")

        if (duringSeconds.val() > 0) {
            var back_time = duringSeconds.val() - 1;
            $("#duringSeconds").val(back_time)
            $("#seckillTip").html("秒杀进行中<br>剩余时间：" + back_time + "秒");
        } else {
            if (duringSeconds.val() == 0) {
                secKillOver()
            }
        }
        diff--;
    }, 1000);
}

// 获取自定义的秒杀路径
function getSeckillPath() {
    var seckillGoodsId = $("#seckillGoodsId").val()
    g_showLoading();
    $.ajax({
        url: "/common/path",
        type: "GET",
        data: {
            seckillGoodsId: seckillGoodsId
        },
        success: function (data) {
            if (data.code == 200) {
                var path = data.object
                console.log(seckillGoodsId)
                doSeckill(path)
            } else {
                layer.msg(data.message)
            }
        },
        error: function () {
            layer.msg("客户端请求路径错误")
        }
    })
}

// 异步自旋等结果
function getResult(seckillGoodsId) {
    g_showLoading();
    $.ajax({
        url: "/seckillOrder/result",
        type: 'GET',
        data: {
            seckillGoodsId: seckillGoodsId
        },
        success: function (data) {
            var result = data.object;
            if (result < 0) {
                layer.msg("对不起，秒杀失败！")
            } else if (result == 0) {
                setTimeout(function () {
                    getResult(seckillGoodsId)
                }, 1000)
            } else {
                layer.confirm
                ("恭喜你，秒杀成功！！！\n是否查看订单", {btn: ["确定", "取消"]},
                    function (index) {
                        layui.use('miniPage', function () {
                            layer.close(index)
                            var miniPage = layui.miniPage;
                            miniPage.hashChange('seckillOrderDetail.htm?seckillOrderId=' + result);
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

// 发起异步秒杀请求
function doSeckill(path) {
    $.ajax({
        url: '/seckill/' + path + '/doSeckill',
        type: 'POST',
        data: {
            seckillGoodsId: $("#seckillGoodsId").val()
        },
        success: function (data) {
            if (data.code == 200) {
                getResult($('#seckillGoodsId').val())
            } else {
                layer.msg("错误信息：" + data.message)
            }
        },
        error: function (data) {
            layer.msg("错误信息：" + data.message)
        }
    })
}