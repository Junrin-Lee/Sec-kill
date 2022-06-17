package cn.pointer.lrl.generator.service.imp;

import cn.pointer.lrl.generator.mapper.SystemMenuMapper;
import cn.pointer.lrl.generator.pojo.SystemMenu;
import cn.pointer.lrl.generator.service.ISystemMenuService;
import cn.pointer.lrl.utils.TreeUtil;
import cn.pointer.lrl.vo.MenuVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 系统菜单表 服务实现类
 * </p>
 *
 * @author lrl
 * @since 2022-04-25
 */
@Service
public class SystemMenuServiceImpl extends ServiceImpl<SystemMenuMapper, SystemMenu> implements ISystemMenuService {

    @Autowired
    private SystemMenuMapper systemMenuMapper;

    @Override
    public Map<String, Object> menu() {
        Map<String, Object> map = new HashMap<>(16);
        Map<String, Object> home = new HashMap<>(16);
        Map<String, Object> logo = new HashMap<>(16);
        List<SystemMenu> menuList = systemMenuMapper.selectList(new QueryWrapper<SystemMenu>().orderByDesc("sort"));
        List<MenuVo> menuInfo = new ArrayList<>();
        for (SystemMenu menu : menuList) {
            MenuVo menuVO = new MenuVo();
            menuVO.setId(menu.getId());
            menuVO.setPid(menu.getPid());
            menuVO.setHref(menu.getHref());
            menuVO.setTitle(menu.getTitle());
            menuVO.setIcon(menu.getIcon());
            menuVO.setTarget(menu.getTarget());
            menuInfo.add(menuVO);
        }
        home.put("title", "首页");
        home.put("href", "/page/welcome");//控制器路由,自行定义
        logo.put("title", "商城平台");
        logo.put("image", "img/logo.png");//静态资源文件路径,可使用默认的logo.png
        map.put("homeInfo", home);
        map.put("logoInfo", logo);
        map.put("menuInfo", TreeUtil.toTree(menuInfo, 0L));
        return map;
    }
}
