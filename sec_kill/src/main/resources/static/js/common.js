//展示loading
function g_showLoading() {
    var idx = layer.msg('处理中...', {icon: 16, shade: [0.5, '#f5f5f5'], scrollbar: false, offset: '0px', time: 100000});
    return idx;
}

//salt
var g_passsword_salt = "1a2b3c4d"

function f_md5(inputPass) {
    var str = "" + g_passsword_salt.charAt(0) + g_passsword_salt.charAt(2) + inputPass + g_passsword_salt.charAt(5) + g_passsword_salt.charAt(4);
    return md5(str);
}

// 普通模式获取url参数
function g_getQueryString(name) {
    var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)");
    // alert(window.location.search)
    var r = window.location.search.substr(1).match(reg);
    if (r != null) return unescape(r[2]);
    return null;
}

// hash模式下获取url参数
function hash_getQueryString(variable) {
    const after = window.location.hash.split('?', 2)[1]
    if (after) {
        const reg = new RegExp('(^|&)' + variable + '=([^&]*)(&|$)')
        const r = after.match(reg)
        if (r != null) {
            return decodeURIComponent(r[2])
        } else {
            return false
        }
    }
}


//设定时间格式化函数，使用new Date().format("yyyy-MM-dd HH:mm:ss");
Date.prototype.format = function (format) {
    var args = {
        "M+": this.getMonth() + 1,
        "d+": this.getDate(),
        "H+": this.getHours(),
        "m+": this.getMinutes(),
        "s+": this.getSeconds(),
    };
    if (/(y+)/.test(format))
        format = format.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
    for (var i in args) {
        var n = args[i];
        if (new RegExp("(" + i + ")").test(format))
            format = format.replace(RegExp.$1, RegExp.$1.length == 1 ? n : ("00" + n).substr(("" + n).length));
    }
    return format;
}
