package org.example.springbootdemo.config.properties;

import lombok.Data;
import org.example.springbootdemo.utils.RSAUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.security.PrivateKey;
import java.security.PublicKey;

/**
 * @date 2024/6/1
 **/
@Data
@ConfigurationProperties(prefix = "web.rsa")
public class WebRSAProperties implements InitializingBean {

    private String publicKeyPath;

    private String privateKeyPath;

    private PublicKey publicKey;

    private PrivateKey privateKey;

    @Override
    public void afterPropertiesSet() throws Exception {
        publicKey = RSAUtils.loadRSAX509PublicKey(publicKeyPath);
        privateKey = RSAUtils.loadRSAPKCS8PrivateKey(privateKeyPath);
    }
}
