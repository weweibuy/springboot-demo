package org.example.springbootdemo.service;

import lombok.RequiredArgsConstructor;
import org.example.springbootdemo.mapper.UserMapper;
import org.example.springbootdemo.midmapper.VipUserMapper;
import org.example.springbootdemo.model.example.UserExample;
import org.example.springbootdemo.model.midpo.VipUser;
import org.example.springbootdemo.model.po.User;
import org.example.springbootdemo.support.multipleds.aspect.SpecDatasource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @date 2024/8/7
 **/
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserMapper userMapper;

    private final VipUserMapper vipUserMapper;

    public User queryUser(String username) {
        return userMapper.selectOneByExample(
                UserExample.newAndCreateCriteria()
                        .andDeletedEqualTo(false)
                        .andUserNameEqualTo(username)
                        .example());
    }

    @SpecDatasource("biz_backup")
    public User queryUserWithSpecDs(String username) {
        return userMapper.selectOneByExample(
                UserExample.newAndCreateCriteria()
                        .andDeletedEqualTo(false)
                        .andUserNameEqualTo(username)
                        .example());
    }

    @Transactional(value = "middleTxManager", rollbackFor = Exception.class)
    public void insertVipUser(VipUser vipUser, Boolean ex) {
        vipUserMapper.insertSelective(vipUser);
        if (Boolean.TRUE.equals(ex)) {
            throw new RuntimeException("xx");
        }
    }


}
