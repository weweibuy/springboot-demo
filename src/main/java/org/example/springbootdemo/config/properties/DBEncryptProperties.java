package org.example.springbootdemo.config.properties;

import lombok.Data;
import org.example.springbootdemo.utils.AESUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;

import javax.crypto.SecretKey;

/**
 * @date 2024/7/27
 **/
@Data
@ConfigurationProperties(prefix = "db.encrypt")
public class DBEncryptProperties implements InitializingBean {

    /**
     * AES 加密密码 长度32
     */
    private String aesKey;

    private SecretKey secretKey;


    @Override
    public void afterPropertiesSet() throws Exception {
        if (aesKey == null || aesKey.length() != 32) {
            throw new IllegalArgumentException("密钥长度错误");
        }
        secretKey = AESUtils.secretKey(aesKey);
    }

}
