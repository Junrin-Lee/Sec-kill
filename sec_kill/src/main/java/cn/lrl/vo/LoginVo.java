package cn.lrl.vo;

import cn.lrl.annotation.IsMobile;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotEmpty;

/**
 * 前端用户登陆交互类
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class LoginVo {
    @IsMobile
    @NotEmpty
    private String mobile;

    @NotEmpty
    private String password;

    private String oldPwd;
}
