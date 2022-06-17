package cn.lrl.generator.mapper;

import cn.lrl.generator.pojo.Goods;
import cn.lrl.vo.GoodsVo;
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
public interface GoodsMapper extends BaseMapper<Goods> {
    GoodsVo findGoodsVOById(@Param("goodsId") Long goodsId);

    List<Goods> getGoodsByMyself(@Param("uid") Long uid, @Param("gid") Long gid, @Param("goodsName") String goodsName);

}
