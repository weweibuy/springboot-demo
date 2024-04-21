package org.example.springbootdemo.controller.dto.req;

import lombok.Data;
import org.example.springbootdemo.controller.dto.resp.UserSignRespDTO;

/**
 * @date 2024/4/20
 **/
@Data
public class UserSignReqDTO {

    private String username;

    private String fullName;

    private String phoneNo;

    private String idNo;

    private String password;

    private String bankCardNo;


    public UserSignRespDTO toSignResp() {
        UserSignRespDTO resp = new UserSignRespDTO();
        resp.setUsername(username);
        resp.setFullName(fullName);
        resp.setPhoneNo(phoneNo);
        resp.setIdNo(idNo);
        resp.setPassword(password);
        resp.setBankCardNo(bankCardNo);
        return resp;
    }
}
