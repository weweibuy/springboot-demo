package org.example.springbootdemo.support;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.example.springbootdemo.config.properties.DBEncryptProperties;
import org.example.springbootdemo.utils.AESUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;

/**
 * AES 加密
 *
 * @date @date 2024/7/27
 **/
@Slf4j
@Component
public class AESDbEncryptHelper {

    @Autowired
    private DBEncryptProperties dbEncryptProperties;

    private static DBEncryptProperties encryptProperties;

    @PostConstruct
    public void init() {
        encryptProperties = dbEncryptProperties;
    }


    static String encrypt(String parameter) {
        SecretKey secretKey = encryptProperties.getSecretKey();
        return AESUtils.encryptToBase64(secretKey, parameter);
    }


    static String decrypt(String r) {
        SecretKey secretKey = encryptProperties.getSecretKey();

        try {
            return AESUtils.decryptBase64(secretKey, r);
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

}
