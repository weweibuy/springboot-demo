package org.example.springbootdemo.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.example.springbootdemo.config.properties.WebJwtProperties;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;

/**
 * jwt 工具
 *
 * @date 2024/7/6
 **/
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class JwtUtils {

    /**
     * 时区
     */
    private static final ZoneOffset ZONE_OFFSET = ZoneId.systemDefault().getRules().getOffset(Instant.now());

    /**
     * 生成JWT
     *
     * @param webJwtProperties
     * @param username
     * @return
     */
    public static String generateToken(WebJwtProperties webJwtProperties, String username) {
        Algorithm algorithm = Algorithm.RSA256(null, webJwtProperties.getPrivateKey());
        return JWT.create()
                // 主体
                .withSubject(username)
                // 签发者
                .withIssuer(webJwtProperties.getIssuer())
                // 签发时间
                .withIssuedAt(LocalDateTime.now().toInstant(ZONE_OFFSET))
                // 过期时间
                .withExpiresAt(LocalDateTime.now()
                        .plusSeconds(webJwtProperties.getEffectiveTimeSeconds())
                        .toInstant(ZONE_OFFSET))
                // 自定义的claim, nonce为随机数, 让每次生成的JWT都不一样
                .withClaim("nonce", NumCodeGenerateUtils.generateNonceStr(32))
                // 秘钥id, 用于有多个签名秘钥或者有秘钥跟换的场景.
//                .withKeyId("my_sign_key_id")
                .sign(algorithm);
    }


    /**
     * 验证jwt
     *
     * @param webJwtProperties
     * @param token
     * @return
     */
    public static DecodedJWT verifyToken(WebJwtProperties webJwtProperties, String token) {
        Algorithm algorithm = Algorithm.RSA256(webJwtProperties.getPublicKey(), null);
        return JWT.require(algorithm)
                .withIssuer(webJwtProperties.getIssuer())
                .build()
                .verify(token);
    }


}
