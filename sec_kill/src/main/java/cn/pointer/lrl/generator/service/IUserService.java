package cn.pointer.lrl.generator.service;

import cn.pointer.lrl.generator.pojo.User;
import cn.pointer.lrl.vo.LoginVo;
import cn.pointer.lrl.vo.UserVo;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author lrl
 * @since 2022-01-05
 */
public interface IUserService extends IService<User> {

    LoginVo login(LoginVo vo);

    void register(LoginVo vo);

    String getUserName(Long mobile);

    void updateUserName(UserVo vo);

    void updateUserPwd(LoginVo vo);

    void setLoginTime(String mobile);

}
