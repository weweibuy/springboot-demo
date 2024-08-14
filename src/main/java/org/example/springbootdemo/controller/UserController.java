package org.example.springbootdemo.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.springbootdemo.config.properties.WebJwtProperties;
import org.example.springbootdemo.config.properties.WebRSAProperties;
import org.example.springbootdemo.controller.dto.req.UserLoginReqDTO;
import org.example.springbootdemo.controller.dto.req.UserSignReqDTO;
import org.example.springbootdemo.controller.dto.resp.UserRespDTO;
import org.example.springbootdemo.controller.dto.resp.UserSignRespDTO;
import org.example.springbootdemo.mapper.UserMapper;
import org.example.springbootdemo.model.dto.resp.UserLoginRespDTO;
import org.example.springbootdemo.model.eum.ResponseCodeEum;
import org.example.springbootdemo.model.example.UserExample;
import org.example.springbootdemo.model.exception.BusinessException;
import org.example.springbootdemo.model.midpo.VipUser;
import org.example.springbootdemo.model.po.User;
import org.example.springbootdemo.service.UserService;
import org.example.springbootdemo.support.ValidateHelper;
import org.example.springbootdemo.utils.BCryptUtils;
import org.example.springbootdemo.utils.JwtUtils;
import org.example.springbootdemo.utils.RSAUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import java.security.InvalidKeyException;
import java.util.Objects;
import java.util.Optional;

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

    private final WebJwtProperties webJwtProperties;

    private final UserMapper userMapper;

    private final UserService userService;

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

        User user = signReq.toUser(idNo);
        userMapper.insertSelective(user);

        log.info("身份证号: {}", idNo);
        UserSignRespDTO signResp = signReq.toSignResp();
        signResp.setIdNo(idNo);
        signResp.setId(user.getId());
        return signResp;
    }

    @PostMapping("/login")
    public UserLoginRespDTO login(@Valid @RequestBody UserLoginReqDTO loginReq) {

        Class group = null;
        if (loginReq.getLoginType() == 1) {
            group = UserLoginReqDTO.UserNameGroup.class;
        } else if (loginReq.getLoginType() == 2) {
            group = UserLoginReqDTO.PhoneNoGroup.class;
        }

        validateHelper.validate(loginReq, group);

        User user = null;
        if (loginReq.getLoginType() == 1) {
            user = Optional.ofNullable(userMapper.selectOneByExample(
                            UserExample.newAndCreateCriteria()
                                    .andDeletedEqualTo(false)
                                    .andUserNameEqualTo(loginReq.getUsername())
                                    .example()))
                    .filter(u -> BCryptUtils.match(loginReq.getPassword(), u.getPassword()))
                    .orElse(null);
        } else if (loginReq.getLoginType() == 2) {
            user = userMapper.selectOneByExample(
                    UserExample.newAndCreateCriteria()
                            .andDeletedEqualTo(false)
                            .andPhoneNoEqualTo(loginReq.getPhoneNo())
                            .example());
        }
        if (user == null) {
            throw new BusinessException(ResponseCodeEum.LOGIN_IN_FAIL.getCode(),
                    "登录信息错误");
        }

        // 生成JWT
        String token = JwtUtils.generateToken(webJwtProperties, loginReq.getUsername());
        UserLoginRespDTO userLoginRespDTO = new UserLoginRespDTO();
        userLoginRespDTO.setToken(token);
        userLoginRespDTO.setFullName(user.getFullName());
        userLoginRespDTO.setUserName(user.getUserName());
        userLoginRespDTO.setIdNo(user.getIdNo());
        userLoginRespDTO.setPhoneNo(user.getPhoneNo());

        return userLoginRespDTO;
    }


    @GetMapping("/query")
    public UserRespDTO queryUser(@RequestParam("username") String username,
                                 @RequestParam(value = "ds", required = false) String ds) {
        User user = null;
        if (Objects.equals(ds, "backup")) {
            user = userService.queryUserWithSpecDs(username);
        } else {
            user = userService.queryUser(username);
        }

        UserRespDTO userRespDTO = new UserRespDTO();
        if (user != null) {
            BeanUtils.copyProperties(user, userRespDTO);
            userRespDTO.setUsername(user.getUserName());
        }
        return userRespDTO;
    }

    @GetMapping("/user2vip")
    public UserRespDTO userToVip(@RequestParam("username") String username,
                                 @RequestParam(value = "ex", required = false) Boolean ex) {

        User user = userService.queryUser(username);
        VipUser vipUser = new VipUser();
        UserRespDTO userRespDTO = new UserRespDTO();

        if (user != null) {
            BeanUtils.copyProperties(user, vipUser);

            userService.insertVipUser(vipUser, ex);

            BeanUtils.copyProperties(user, userRespDTO);
        }

        return userRespDTO;
    }


}
