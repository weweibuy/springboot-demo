package org.example.springbootdemo.config.properties;

import lombok.Data;
import org.example.springbootdemo.utils.RSAUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Map;
import java.util.Optional;

/**
 * @date 2024/6/16
 **/
@Data
@ConfigurationProperties(prefix = "web.sign")
public class WebSignProperties implements InitializingBean {

    /**
     * 服务方的秘钥
     */
    private ServerKeyProperties serverKey;


    /**
     * 接入方的秘钥信息
     */
    private Map<String, ClientKeyProperties> clientKey;

    @Override
    public void afterPropertiesSet() throws Exception {
        serverKey.initKey();
        Optional.ofNullable(clientKey)
                .ifPresent(m -> m.values().forEach(ClientKeyProperties::initKey));

    }


    @Data
    public static class ServerKeyProperties {

        private String keyPath;

        private String keyPassword;

        private String cerPath;

        private PublicKey publicKey;

        private PrivateKey privateKey;

        public void initKey() {
            privateKey = RSAUtils.loadRSAPKCS12PrivateKey(keyPath, keyPassword);
            publicKey = RSAUtils.loadRSACertificate(cerPath);
        }

    }


    @Data
    public static class ClientKeyProperties {

        private String keyPath;

        private PublicKey publicKey;

        private PrivateKey privateKey;

        public void initKey() {
            KeyPair keyPair = RSAUtils.loadRSAPKCS1KeyPair(keyPath);
            privateKey = keyPair.getPrivate();
            publicKey = keyPair.getPublic();
        }

    }


}
