package cn.pointer.lrl.generator.mapper;

import cn.pointer.lrl.generator.pojo.SystemMenu;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * 系统菜单表 Mapper 接口
 * </p>
 *
 * @author lrl
 * @since 2022-04-25
 */
@Repository
public interface SystemMenuMapper extends BaseMapper<SystemMenu> {

}
