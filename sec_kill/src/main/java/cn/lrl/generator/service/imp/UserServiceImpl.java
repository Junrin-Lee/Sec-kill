package cn.lrl.generator.service.imp;

import cn.lrl.exception.LoginException;
import cn.lrl.exception.RegisterException;
import cn.lrl.exception.UserException;
import cn.lrl.generator.mapper.UserMapper;
import cn.lrl.generator.pojo.User;
import cn.lrl.generator.service.IUserService;
import cn.lrl.utils.Md5Util;
import cn.lrl.utils.UUIDUtil;
import cn.lrl.vo.LoginVo;
import cn.lrl.vo.RespMsgEnum;
import cn.lrl.vo.UserVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

import static cn.lrl.vo.RespMsgEnum.SALT;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author lrl
 * @since 2022-01-05
 */
@Slf4j
@Service
@CacheConfig(cacheNames = "LoginVoCache")
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    @Autowired
    UserMapper userMapper;

    @Override
    @Cacheable(key = "#p0.mobile")
    public LoginVo login(LoginVo vo) {
        String mobileNum = vo.getMobile();
        String password = vo.getPassword();

        User user = userMapper.selectById(mobileNum);

        /* 用户名不存在 */
        if (user == null) {
            throw new LoginException(RespMsgEnum.MOBILE_NOT_EXIST);
        }

        /* 手机号或密码错误 */
        // 二次加密
        String pass = Md5Util.serverPass(password, user.getSalt() + SALT);
        // 二次加密后与数据库存入的数据进行匹对
        if (!pass.equals(user.getPassWord())) {
            throw new LoginException(RespMsgEnum.LOGIN_ERROR);
        }

        /* 登陆成功 */
        return vo;
    }

    @Override
    public void setLoginTime(String mobile) {
        userMapper.update(null, new UpdateWrapper<User>()
                .set("last_login_time", new Date())
                .eq("id", mobile)
        );
    }

    @Override
    public void register(LoginVo vo) {
        // 通过UUID生成盐值
        String salt = UUIDUtil.uuid();
        try {
            userMapper.insert(new User()
                    .setId(Long.valueOf(vo.getMobile()))
                    .setSalt(salt)
                    .setPassWord(Md5Util.serverPass(vo.getPassword(), salt + SALT)) //uuid盐+服务器盐，保证安全性
            );
        } catch (Exception e) {
            throw new RegisterException(RespMsgEnum.REGISTER_ERROR);
        }
    }

    @Override
    public String getUserName(Long mobile) {
        User user = userMapper.selectOne(new QueryWrapper<User>()
                .select("user_name")
                .eq("id", mobile));
        if (user != null) {
            return user.getUserName();
        } else {
            return null;
        }
    }

    @Override
    @Transactional
    public void updateUserName(UserVo vo) {
        try {
            userMapper.update(new User(), new UpdateWrapper<User>()
                    .set("user_name", vo.getUserName())
                    .eq("id", vo.getMobile())
            );

        } catch (Exception e) {
            throw new UserException(RespMsgEnum.USER_UPDATE_MSG_ERROR);
        }
    }

    @CacheEvict(key = "#p0.mobile")
    @Override
    @Transactional
    public void updateUserPwd(LoginVo vo) {
        try {
            User user = userMapper.selectOne(new QueryWrapper<User>().select("salt").eq("id", vo.getMobile()));
            userMapper.update(null, new UpdateWrapper<User>()
                    .set("pass_word", Md5Util.serverPass(vo.getPassword(), user.getSalt() + SALT))
                    .eq("id", vo.getMobile())
            );
        } catch (Exception e) {
            throw new UserException(RespMsgEnum.USER_UPDATE_PWD_ERROR);
        }
    }
}
