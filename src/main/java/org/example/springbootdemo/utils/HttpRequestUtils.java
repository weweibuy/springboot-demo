package org.example.springbootdemo.utils;

import io.micrometer.common.util.StringUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * @date 2024/3/30
 **/
@NoArgsConstructor(access = AccessLevel.PACKAGE)
public final class HttpRequestUtils {

    /**
     * 获取请求ip
     *
     * @param request
     * @return
     */
    public static String getRequestIp(HttpServletRequest request) {
        String ip = "";
        if (StringUtils.isNotBlank(ip = request.getHeader("X-Forwarded-For"))) {
            return ip.split(",")[0];
        }

        if (StringUtils.isNotBlank(ip = request.getHeader("Proxy-Client-IP"))) {
            return ip;
        }

        if (StringUtils.isNotBlank(ip = request.getHeader("WL-Proxy-Client-IP"))) {
            return ip;
        }

        if (StringUtils.isNotBlank(ip = request.getHeader("HTTP_CLIENT_IP"))) {
            return ip;
        }

        if (StringUtils.isNotBlank(ip = request.getHeader("HTTP_X_FORWARDED_FOR"))) {
            return ip;
        }
        return request.getRemoteAddr();
    }

}
