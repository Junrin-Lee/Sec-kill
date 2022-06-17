package cn.lrl.generator.mapper;

import cn.lrl.generator.pojo.SeckillGoods;
import cn.lrl.vo.SeckillGoodsVo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author lrl
 * @since 2022-01-13
 */
@Repository
public interface SeckillGoodsMapper extends BaseMapper<SeckillGoods> {
    List<SeckillGoodsVo> findSeckillGoodsVo();

    SeckillGoodsVo findSeckillGoodsVoById(@Param("sgid") Long seckillGoodsId);

    int isSeckillGoodsCreate(@Param("uid") Long userId,@Param("sgid")Long seckillGoodsId);

    List<SeckillGoodsVo> getSeckillGoodsVoByMyself(@Param("uid") Long userId);

    List<SeckillGoodsVo> getSeckillGoodsVoByMyselfWithLike(@Param("uid") Long userId, @Param("sgid") Long seckillGoodsId, @Param("goodsName") String goodsName);

}
