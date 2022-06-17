package cn.lrl.generator.service;

import cn.lrl.generator.pojo.SystemMenu;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * <p>
 * 系统菜单表 服务类
 * </p>
 *
 * @author lrl
 * @since 2022-04-25
 */
public interface ISystemMenuService extends IService<SystemMenu> {

    Map<String, Object> menu();
}
