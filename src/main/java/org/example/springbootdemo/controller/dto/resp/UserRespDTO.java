package org.example.springbootdemo.controller.dto.resp;

import lombok.Data;
import org.example.springbootdemo.support.desensitization.annotation.MaskingData;

/**
 * @date 2024/4/20
 **/
@Data
public class UserRespDTO {

    private String username;

    @MaskingData(patten = "(?<=[\\u4e00-\\u9fa5]{1})(.)")
    private String fullName;

    @MaskingData(patten = "(?<=\\w{3})\\w(?=\\w{4})")
    private String phoneNo;

    @MaskingData(patten = "(?<=\\w{6})\\w(?=\\w{1})")
    private String idNo;

    @MaskingData(patten = ".+", replace = "******")
    private String password;

    @MaskingData(patten = "(?<=\\w{6})\\w(?=\\w{4})")
    private String bankCardNo;

}
