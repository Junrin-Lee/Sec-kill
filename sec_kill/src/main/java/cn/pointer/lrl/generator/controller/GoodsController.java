package cn.pointer.lrl.generator.controller;

import cn.pointer.lrl.annotation.AccessLimited;
import cn.pointer.lrl.exception.GoodsException;
import cn.pointer.lrl.exception.UploadException;
import cn.pointer.lrl.generator.pojo.Goods;
import cn.pointer.lrl.generator.service.CommonService;
import cn.pointer.lrl.generator.service.IGoodsService;
import cn.pointer.lrl.generator.service.IUserService;
import cn.pointer.lrl.utils.RedisUtil;
import cn.pointer.lrl.vo.*;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/goods")
@Slf4j
public class GoodsController {

    @Autowired
    RedisUtil redisUtil;

    @Autowired
    IGoodsService goodsService;

    @Autowired
    CommonService commonService;

    @Autowired
    IUserService userService;

    @Autowired
    ThymeleafViewResolver thymeleafViewResolver;

    /**
     * 商品列表页面静态化
     *
     * @param request  请求
     * @param response 响应
     * @param model    数据回传
     * @return HTML文本
     */
    @Cacheable(cacheNames = "pageCache", key = "'goodsList'")
    @AccessLimited(second = 20, maxCount = 5)
    @GetMapping(value = "/toGoodsList", produces = "text/html;charset=utf-8")
    public String toList(HttpServletRequest request, HttpServletResponse response, Model model) {

        // 获取秒杀商品信息
        List<Goods> goodsList = goodsService.findGoodsList();
        model.addAttribute("goodsList", goodsList);

        // 解析为html文本
        WebContext webContext = new WebContext(
                request, response, request.getServletContext(), request.getLocale(), model.asMap());

        return thymeleafViewResolver.getTemplateEngine().process("goods", webContext);
    }

    /**
     * 查询自己的商品信息，同时能进行搜索查询
     *
     * @param vo   用户登录信息
     * @param good 商品搜索信息
     * @return RespMsgVo
     */
    @AccessLimited(second = 20, maxCount = 5)
    @GetMapping("/myGoods")
    public RespMsgVo myGoods(@RequestAttribute(name = "login_vo") LoginVo vo, Goods good) {

        // 获取商品信息
        List<Goods> goodsList = null;
        if (good.getId() == null && good.getGoodsName() == null) {
            // 没有搜索条件
            goodsList = goodsService.selectGoodsMyself(Long.parseLong(vo.getMobile()));
        } else {
            //有搜索条件
            goodsList = goodsService.selectGoodsMyself(Long.parseLong(vo.getMobile()), good);
        }

        return RespMsgVo.success(RespMsgEnum.DATA_SUCCESS, goodsList);
    }

    /**
     * 获取商品详细信息
     *
     * @param goodsId 商品id
     * @return RespMsgVo
     */
    @GetMapping(value = "/toDetail/{goodsId}")
    public RespMsgVo goodsDetail(@PathVariable("goodsId") Long goodsId) {
        GoodsVo goodVo = goodsService.findGoodById(goodsId);

        String producer = userService.getUserName(goodVo.getCreateUserId());
        GoodsDetailVo detailVo = new GoodsDetailVo(goodVo, producer);
        return RespMsgVo.success(detailVo);
    }

    /**
     * 添加商品
     *
     * @param vo   用户登录信息
     * @param json 前端数据Json
     * @return RespMsgVo
     */
    @Caching(evict = {
            @CacheEvict(cacheNames = "pageCache", key = "'goodsList'"),
            @CacheEvict(cacheNames = "dataCache", key = "#p0.mobile+'::myGoods'")
    })
    @PostMapping("/addGoods")
    public RespMsgVo addGoods(@RequestAttribute(name = "login_vo") LoginVo vo, @RequestBody String json) {
        // 解析Json
        Goods good = JSON.parseObject(json, Goods.class);
        // 图片数据库存储路径
        good.setGoodsImg("/img/" + good.getGoodsImg());
        // 商品的创建者Id
        good.setCreateUserId(Long.parseLong(vo.getMobile()));
        // 添加商品信息
        goodsService.addGoods(good);

        return RespMsgVo.success();
    }

    /**
     * 修改商品信息
     *
     * @param vo   用户登录信息
     * @param json 前端数据Json
     * @return RespMsgVo
     */
    @Caching(evict = {
            @CacheEvict(cacheNames = "pageCache", key = "'goodsList'"),
            @CacheEvict(cacheNames = "dataCache", key = "#p0.mobile+'::myGoods'")
    })
    @PutMapping("/modifiedGoods")
    public RespMsgVo modifiedGoods(@RequestAttribute(name = "login_vo") LoginVo vo, @RequestBody String json) {

        // 解析Json
        Goods good = JSON.parseObject(json, Goods.class);
        goodsService.modifiedGood(good);

        return RespMsgVo.success();
    }

    /**
     * 删除商品
     *
     * @param vo           用户信息
     * @param delGoodsJson 前端数据Json
     * @return RespMsgVo
     */
    @Caching(evict = {
            @CacheEvict(cacheNames = "pageCache", key = "'goodsList'"),
            @CacheEvict(cacheNames = "dataCache", key = "#p0.mobile+'::myGoods'")
    })
    @DeleteMapping("/delGoods")
    public RespMsgVo delGoods(@RequestAttribute(name = "login_vo") LoginVo vo, @RequestBody String delGoodsJson) {

        // 解析JSON，批量删除
        List<Goods> goods = JSON.parseArray(delGoodsJson, Goods.class);
        goodsService.delGoods(goods);

        return RespMsgVo.success();
    }

}
