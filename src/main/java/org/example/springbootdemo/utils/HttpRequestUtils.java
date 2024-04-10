package org.example.springbootdemo.utils;

import io.micrometer.common.util.StringUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpHeaders;

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

    /**
     * 是否包含请体
     *
     * @param request
     * @return
     */
    public static boolean isIncludePayload(HttpServletRequest request) {
        return request.getContentLength() > 0
                || "chunked".equals(request.getHeader(HttpHeaders.TRANSFER_ENCODING));
    }

}
