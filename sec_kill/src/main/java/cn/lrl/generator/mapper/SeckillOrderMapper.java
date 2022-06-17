package cn.lrl.generator.mapper;

import cn.lrl.generator.pojo.SeckillOrder;
import cn.lrl.po.SeckillOrderPO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author lrl
 * @since 2022-01-13
 */
@Repository
public interface SeckillOrderMapper extends BaseMapper<SeckillOrder> {

    SeckillOrderPO getSeckillOrderPOMsg(@Param("uid") Long userId, @Param("gid") Long goodsId);
}
