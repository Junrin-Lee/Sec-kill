package cn.lrl.po;

import lombok.Data;

import java.io.Serializable;

@Data
public class MenuKey implements Serializable {
    private Long id;

    /**
     * 名称
     */
    private String title;

    /**
     * 链接
     */
    private String href;
}