package org.example.springbootdemo.model.po;

import java.time.LocalDateTime;
import lombok.Data;

@Data
public class User {
    /**
     * id
     */
    private Long id;

    /**
     * 姓名
     */
    private String fullName;

    /**
     * 用户名
     */
    private String userName;

    /**
     * 密码
     */
    private String password;

    /**
     * 手机号
     */
    private String phoneNo;

    /**
     * 身份证号
     */
    private String idNo;

    /**
     * 是否删除
     */
    private Boolean deleted;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}