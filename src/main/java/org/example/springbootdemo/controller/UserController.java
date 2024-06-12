package org.example.springbootdemo.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.springbootdemo.config.properties.WebRSAProperties;
import org.example.springbootdemo.controller.dto.req.UserLoginReqDTO;
import org.example.springbootdemo.controller.dto.req.UserSignReqDTO;
import org.example.springbootdemo.controller.dto.resp.UserRespDTO;
import org.example.springbootdemo.controller.dto.resp.UserSignRespDTO;
import org.example.springbootdemo.model.dto.resp.CommonCodeResponseDTO;
import org.example.springbootdemo.model.exception.BusinessException;
import org.example.springbootdemo.support.ValidateHelper;
import org.example.springbootdemo.utils.RSAUtils;
import org.springframework.web.bind.annotation.*;

import java.security.InvalidKeyException;

/**
 * @date 2024/4/20
 **/
@Slf4j
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final ValidateHelper validateHelper;

    private final WebRSAProperties webRSAProperties;


    @PostMapping("/sign-in")
    public UserSignRespDTO signIn(@RequestBody UserSignReqDTO signReq) {
        log.info("注册用户信息: {}", signReq);

        String idNo = null;
        try {
            idNo = RSAUtils.decryptHex(webRSAProperties.getPrivateKey(), signReq.getIdNo());
        } catch (InvalidKeyException e) {
            throw new RuntimeException("秘钥错误");
        } catch (Exception e) {
            throw BusinessException.badParam("加密数据错误", e);
        }
        log.info("身份证号: {}", idNo);
        UserSignRespDTO signResp = signReq.toSignResp();
        signResp.setIdNo(idNo);
        return signResp;
    }

    @PostMapping("/login")
    public CommonCodeResponseDTO login(@Valid @RequestBody UserLoginReqDTO loginReq) {

        Class group = null;
        if (loginReq.getLoginType() == 1) {
            group = UserLoginReqDTO.UserNameGroup.class;
        } else if (loginReq.getLoginType() == 2) {
            group = UserLoginReqDTO.PhoneNoGroup.class;
        }

        validateHelper.validate(loginReq, group);


        log.info("用户登录成功, 用户: {}", loginReq);
        return CommonCodeResponseDTO.success();
    }


    @GetMapping("/query")
    public UserRespDTO queryUser() {
        UserRespDTO userRespDTO = new UserRespDTO();
        userRespDTO.setUsername("my_username");
        userRespDTO.setFullName("张三丰");
        userRespDTO.setPhoneNo("13800000000");
        userRespDTO.setIdNo("110101199109146678");
        userRespDTO.setPassword("123QWErt:?!@#$");
        userRespDTO.setBankCardNo("6222352637685542580");
        return userRespDTO;
    }


}
