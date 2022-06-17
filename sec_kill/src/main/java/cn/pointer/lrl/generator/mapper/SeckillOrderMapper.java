package cn.pointer.lrl.generator.mapper;

import cn.pointer.lrl.generator.pojo.SeckillOrder;
import cn.pointer.lrl.po.SeckillOrderPO;
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
