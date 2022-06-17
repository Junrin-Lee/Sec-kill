package cn.pointer.lrl.generator.pojo;

import com.baomidou.mybatisplus.annotation.*;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * <p>
 * 系统菜单表
 * </p>
 *
 * @author lrl
 * @since 2022-04-25
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("system_menu")
public class SystemMenu {

    /**
     * ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 父ID
     */
    private Long pid;

    /**
     * 名称
     */
    private String title;

    /**
     * 菜单图标
     */
    private String icon;

    /**
     * 链接
     */
    private String href;

    /**
     * 链接打开方式
     */
    private String target;

    /**
     * 菜单排序
     */
    private Integer sort;

    /**
     * 状态(1:禁用,0:启用)
     */
    @TableLogic
    private Boolean deleted;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    /**
     * 备注信息
     */
    private String remark;

    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    /**
     * 删除时间
     */
    private Date deleteAt;

    /**
     * 乐观锁
     */
    @Version
    private Integer version;


}