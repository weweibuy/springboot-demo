package org.example.springbootdemo.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.RandomUtils;

/**
 * @date 2024/6/16
 **/
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class NumCodeGenerateUtils {


    private static final String SYMBOLS =
            "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

    /**
     * 生成随机数
     *
     * @return
     */
    public static String generateNonceStr(int length) {
        if (length < 1) {
            throw new IllegalArgumentException("length必须大于0");
        }
        char[] nonceChars = new char[length];
        for (int index = 0; index < nonceChars.length; ++index) {
            nonceChars[index] = SYMBOLS.charAt(RandomUtils.nextInt(0, SYMBOLS.length()));
        }
        return new String(nonceChars);
    }


}
