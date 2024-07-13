package org.example.springbootdemo.config.properties;

import lombok.Data;
import org.example.springbootdemo.utils.RSAUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

/**
 * jwt 配置
 *
 * @date 2024/6/1
 **/
@Data
@ConfigurationProperties(prefix = "web.jwt")
public class WebJwtProperties implements InitializingBean {

    /**
     * 公钥路径
     */
    private String publicKeyPath;

    /**
     * 私钥路径
     */
    private String privateKeyPath;

    /**
     * 有效时间
     */
    private Long effectiveTimeSeconds = 3600L;

    /**
     * JWT签发者
     */
    private String issuer = "https://www.my-website.com";

    private RSAPublicKey publicKey;

    private RSAPrivateKey privateKey;

    @Override
    public void afterPropertiesSet() throws Exception {
        publicKey = (RSAPublicKey) RSAUtils.loadRSAX509PublicKey(publicKeyPath);
        privateKey = (RSAPrivateKey) RSAUtils.loadRSAPKCS8PrivateKey(privateKeyPath);
    }
}
