package cn.lrl.generator.mapper;

import cn.lrl.generator.pojo.Order;
import cn.lrl.vo.OrderVo;
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
public interface OrderMapper extends BaseMapper<Order> {

    long SelectsgidbyOrderId();

    List<OrderVo> selectOrderVoByUid(@Param("uid") Long uid);

    List<OrderVo> selectOrderVoLikeOrderId(@Param("uid") Long uid, @Param("oid") String oid);

    List<OrderVo> selectConsumerOrderVoByUid(@Param("uid") Long uid);

    List<OrderVo> selectConsumerOrderVoLikeOrderId(@Param("uid") Long uid, @Param("oid") String oid);
}
