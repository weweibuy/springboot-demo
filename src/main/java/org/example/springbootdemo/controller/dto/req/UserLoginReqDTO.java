package org.example.springbootdemo.controller.dto.req;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

/**
 * @date 2024/4/20
 **/
@Data
public class UserLoginReqDTO {

    @NotNull(message = "登录类型不能为空")
    @Min(value = 1, message = "登录类型错误")
    @Max(value = 2, message = "登录类型错误")
    private Integer loginType;

    @Valid
    @NotNull(message = "登录信息不能为空")
    private LoginInfo loginInfo;

    @NotBlank(message = "用户名不能为空", groups = UserNameGroup.class)
    private String username;

    @NotBlank(message = "密码不能为空", groups = UserNameGroup.class)
    private String password;

    @NotBlank(message = "手机号不能为空", groups = PhoneNoGroup.class)
    @Pattern(regexp = "^1\\d{10}$", message = "手机号格式错误")
    private String phoneNo;

    @NotBlank(message = "验证码不能为空", groups = PhoneNoGroup.class)
    @Length(min = 6, max = 6, message = "验证码不正确", groups = PhoneNoGroup.class)
    private String smsCode;

    @Data
    public static class LoginInfo {

        @NotBlank(message = "登录ip不能为空")
        private String ip;

        @NotBlank(message = "登录设备为空")
        private String device;

    }


    public interface UserNameGroup {
    }

    public interface PhoneNoGroup {
    }

}
