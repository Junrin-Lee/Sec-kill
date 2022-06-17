package cn.pointer.lrl.vo;

import cn.pointer.lrl.annotation.IsMobile;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotEmpty;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class UserVo {
    /**
     * 电话
     */
    @IsMobile
    @NotEmpty
    private String mobile;

    /**
     * 昵称
     */
    @NotEmpty
    private String userName;

    /**
     * 角色
     */
    private String role;

    /**
     * 权限
     */
    private String perms;
}
