package org.example.springbootdemo.controller.dto.resp;

import lombok.Data;

/**
 * @date 2024/4/20
 **/
@Data
public class UserSignRespDTO {

    private Long id;

    private String username;

    private String fullName;

    private String phoneNo;

    private String idNo;

    private String password;

    private String bankCardNo;

}
