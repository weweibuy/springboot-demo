package org.example.springbootdemo.controller;

import lombok.extern.slf4j.Slf4j;
import org.example.springbootdemo.controller.dto.req.UserSignReqDTO;
import org.example.springbootdemo.controller.dto.resp.UserSignRespDTO;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @date 2024/4/20
 **/
@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {


    @PostMapping("/sign-in")
    public UserSignRespDTO signIn(@RequestBody UserSignReqDTO signReq) {
        log.info("注册用户信息: {}", signReq);
        return signReq.toSignResp();
    }


}
