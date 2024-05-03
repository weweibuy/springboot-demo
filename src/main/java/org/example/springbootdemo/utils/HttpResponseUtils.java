package org.example.springbootdemo.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.example.springbootdemo.mvc.ResponseCodeAndMsg;

/**
 * @date 2024/5/2
 **/
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class HttpResponseUtils {

    public static ResponseCodeAndMsg newResponseCodeAndMsg(String code, String msg) {
        return new ResponseCodeAndMsg() {
            @Override
            public String getCode() {
                return code;
            }

            @Override
            public String getMsg() {
                return msg;
            }
        };
    }


}
