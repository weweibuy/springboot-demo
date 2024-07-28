package org.example.springbootdemo.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


/**
 * BCrypt 加密工具
 *
 * @date 2024/7/27
 **/
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class BCryptUtils {

    private static final BCryptPasswordEncoder ENCODER_INSTANCE =
            new BCryptPasswordEncoder();

    /**
     * 加密
     *
     * @param content
     * @return
     */
    public static String encode(String content) {
        return ENCODER_INSTANCE.encode(content);
    }

    /**
     * 匹配
     *
     * @param ori    原文
     * @param encode 密文
     * @return
     */
    public static boolean match(CharSequence ori, String encode) {
        return ENCODER_INSTANCE.matches(ori, encode);
    }

}
