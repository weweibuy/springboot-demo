package org.example.springbootdemo.model.dto.resp;

import lombok.Data;

/**
 * @date 2024/7/6
 **/
@Data
public class UserLoginRespDTO {

    private String token;

    /**
     * 姓名
     */
    private String fullName;

    /**
     * 用户名
     */
    private String userName;

    /**
     * 手机号
     */
    private String phoneNo;

    /**
     * 身份证号
     */
    private String idNo;

}
