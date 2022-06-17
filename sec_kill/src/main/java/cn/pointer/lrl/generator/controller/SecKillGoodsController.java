package cn.pointer.lrl.generator.controller;


import cn.pointer.lrl.annotation.AccessLimited;
import cn.pointer.lrl.generator.pojo.Goods;
import cn.pointer.lrl.generator.service.IGoodsService;
import cn.pointer.lrl.generator.service.ISeckillGoodsService;
import cn.pointer.lrl.generator.service.IUserService;
import cn.pointer.lrl.vo.*;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 商品秒杀
 * </p>
 *
 * @author lrl
 * @since 2022-01-13
 */
@RestController
@RequestMapping("/seckillGoods")
@Slf4j
public class SecKillGoodsController {
    @Autowired
    ISeckillGoodsService seckillGoodsService;

    @Autowired
    IGoodsService goodsService;

    @Autowired
    IUserService userService;

    @Autowired
    ThymeleafViewResolver thymeleafViewResolver;

    /**
     * 获取秒杀商品列表
     *
     * @param request  请求
     * @param response 响应
     * @param model    数据
     * @return HTML文本
     */
    @GetMapping(value = "/toSeckillGoodsList", produces = "text/html;charset=utf-8")
    @Cacheable(cacheNames = "pageCache", key = "'seckillGoodsList'")
    @AccessLimited(second = 20, maxCount = 5)
    public String toSeckillGoodsList(HttpServletRequest request, HttpServletResponse response, Model model) {

        // 获取秒杀商品信息
        List<SeckillGoodsVo> seckillGoodsVoList = seckillGoodsService.findSeckillGoodsVo();
        model.addAttribute("seckillGoodsList", seckillGoodsVoList);

        // 解析为html文本
        WebContext webContext = new WebContext(
                request, response, request.getServletContext(), request.getLocale(), model.asMap());

        return thymeleafViewResolver.getTemplateEngine().process("seckillGoods", webContext);
//        return "goods";
    }

    /**
     * 获取秒杀商品详情信息
     *
     * @param vo             用户登录信息
     * @param seckillGoodsId 秒杀商品id
     * @return RespMsgVo
     */
    @GetMapping(value = "/toDetail/{seckillGoodsId}")
    public RespMsgVo seckillGoodsDetail(@RequestAttribute(name = "login_vo") LoginVo vo,
                                        @PathVariable("seckillGoodsId") Long seckillGoodsId) {

        SeckillGoodsVo seckillGoodsVo = seckillGoodsService.findSeckillGoodsVoById(seckillGoodsId);
        Date startTime = seckillGoodsVo.getStartTime();
        Date endTime = seckillGoodsVo.getEndTime();
        Date nowDate = new Date();

        log.debug("秒杀开始时间：" + startTime);
        log.debug("秒杀结束时间：" + endTime);
        log.debug("现在：" + nowDate);

        // 秒杀状态
        int seckillStatus = 0;

        // 秒杀倒计时
        int remainSeconds = 0;

        // 秒杀持续时间
        int duringSeconds = (int) ((endTime.getTime() - startTime.getTime()) / 1000);
        log.debug("秒杀持续时间：" + duringSeconds);

        // 秒杀还未开始
        if (nowDate.before(startTime)) {
            log.debug("秒杀未开始");
            remainSeconds = (int) ((startTime.getTime() - nowDate.getTime()) / 1000);
        } else if (nowDate.after(endTime)) {
            // 秒杀已经结束
            log.debug("秒杀已结束");
            seckillStatus = 2;
            remainSeconds = -1;
        } else {
            // 秒杀进行中
            log.debug("秒杀进行中");
            duringSeconds = (int) ((endTime.getTime() - nowDate.getTime()) / 1000);
            seckillStatus = 1;
            remainSeconds = 0;
        }

        String producer = userService.getUserName(seckillGoodsVo.getCreateUserId());
        SeckillGoodsDetailVo detailVo = new SeckillGoodsDetailVo(seckillGoodsVo, producer, duringSeconds, seckillStatus, remainSeconds);

        return RespMsgVo.success(detailVo);
    }

    /**
     * 查询自己的秒杀商品信息，同时能进行搜索查询
     *
     * @param vo             用户登录信息
     * @param seckillGoodsVo 商品搜索信息
     * @return RespMsgVo
     */
    @AccessLimited(second = 20, maxCount = 5)
    @GetMapping("/mySeckillGoods")
    public RespMsgVo mySeckillGoods(@RequestAttribute(name = "login_vo") LoginVo vo, SeckillGoodsVo seckillGoodsVo) {

        // 获取商品信息
        List<SeckillGoodsVo> seckillGoodsVoList = null;
        if (seckillGoodsVo.getId() == null && seckillGoodsVo.getGoodsName() == null) {
            // 没有搜索条件
            seckillGoodsVoList = seckillGoodsService.selectSeckillGoodsMyself(Long.parseLong(vo.getMobile()));
        } else {
            //有搜索条件
            seckillGoodsVoList = seckillGoodsService.selectSeckillGoodsMyself(Long.parseLong(vo.getMobile()), seckillGoodsVo);
        }

        return RespMsgVo.success(RespMsgEnum.DATA_SUCCESS, seckillGoodsVoList);
    }

    /**
     * 获取商家的商品信息
     *
     * @param vo 用户登录信息
     * @return RespMsgVo
     */
    @GetMapping("/seckillGoodsSelectData")
    public RespMsgVo seckillGoodsSelectData(@RequestAttribute(name = "login_vo") LoginVo vo) {
        List<Goods> goodsList = goodsService.selectGoodsMyself(Long.parseLong(vo.getMobile()));

        return RespMsgVo.success(RespMsgEnum.DATA_SUCCESS, goodsList);
    }

    /**
     * 添加秒杀商品
     *
     * @param vo   用户登录信息
     * @param json 前端数据json
     * @return RespMsgVo
     */
    @Caching(evict = {
            @CacheEvict(cacheNames = "pageCache", key = "'seckillGoodsList'"),
            @CacheEvict(cacheNames = "dataCache", key = "#p0.mobile+'::mySeckillGoods'")
    })
    @PostMapping("addSeckillGoods")
    public RespMsgVo addSeckillGoods(@RequestAttribute(name = "login_vo") LoginVo vo, @RequestBody String json) {
        // 解析Json
        SeckillGoodsVo seckillGoodsVo = JSON.parseObject(json, SeckillGoodsVo.class);
        //添加秒杀商品
        seckillGoodsVo.setCreateUserId(Long.parseLong(vo.getMobile()));
        seckillGoodsService.addSeckillGoods(Long.parseLong(vo.getMobile()),seckillGoodsVo);

        return RespMsgVo.success();
    }

    /**
     * 修改秒杀商品
     *
     * @param vo   用户登录信息
     * @param json 前端数据json
     * @return RespMsgVo
     */
    @Caching(evict = {
            @CacheEvict(cacheNames = "pageCache", key = "'seckillGoodsList'"),
            @CacheEvict(cacheNames = "dataCache", key = "#p0.mobile+'::mySeckillGoods'")
    })
    @PutMapping("/modifiedSeckillGoods")
    public RespMsgVo modifiedSeckillGoods(@RequestAttribute(name = "login_vo") LoginVo vo, @RequestBody String json) {
        // 解析Json
        SeckillGoodsVo seckillGoodsVo = JSON.parseObject(json, SeckillGoodsVo.class);
        // 修改秒杀商品信息
        seckillGoodsService.modifiedSeckillGood(seckillGoodsVo);

        return RespMsgVo.success();
    }

    /**
     * 删除秒杀商品
     *
     * @param vo   用户登录信息
     * @param json 前端数据json
     * @return RespMsgVo
     */
    @Caching(evict = {
            @CacheEvict(cacheNames = "pageCache", key = "'seckillGoodsList'"),
            @CacheEvict(cacheNames = "dataCache", key = "#p0.mobile+'::mySeckillGoods'")
    })
    @DeleteMapping("/delGoods")
    public RespMsgVo delGoods(@RequestAttribute(name = "login_vo") LoginVo vo, @RequestBody String json) {
        // 解析Json，实现批量删除
        List<SeckillGoodsVo> seckillGoodsVo = JSON.parseArray(json, SeckillGoodsVo.class);
        // 移除redis库存
        seckillGoodsService.delSeckillGoods(seckillGoodsVo);

        return RespMsgVo.success();
    }
}

