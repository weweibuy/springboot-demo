package org.example.springbootdemo.utils;

import io.micrometer.common.util.StringUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.io.IOUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.PathContainer;
import org.springframework.web.util.ContentCachingResponseWrapper;
import org.springframework.web.util.pattern.PathPatternParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @date 2024/3/30
 **/
@NoArgsConstructor(access = AccessLevel.PACKAGE)
public final class HttpRequestUtils {

    private static final String LINE_SEPARATOR = System.getProperty("line.separator");

    private static final PathPatternParser PATH_MATCHER = PathPatternParser.defaultInstance;


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


    /**
     * 读取请求体
     *
     * @param request
     * @return
     * @throws IOException
     */
    public static String readRequestBody(HttpServletRequest request) throws IOException {

        BufferedReader reader = request.getReader();
        StringBuilder sb = new StringBuilder();
        String line = null;
        while ((line = reader.readLine()) != null) {
            sb.append(line).append(LINE_SEPARATOR);
        }
        if (sb.length() > 0) {
            return sb.substring(0, sb.length() - LINE_SEPARATOR.length());
        }
        return "";
    }

    /**
     * 读取响应体
     *
     * @param response
     * @return
     * @throws IOException
     */
    public static String readRespBody(ContentCachingResponseWrapper response) throws IOException {
        InputStream contentInputStream = response.getContentInputStream();
        return IOUtils.toString(contentInputStream, StandardCharsets.UTF_8);
    }

    /**
     * 路径是否匹配
     *
     * @param pattern
     * @param path
     * @return
     */
    public static boolean isMatchPath(String pattern, String path) {
        return PATH_MATCHER.parse(pattern)
                .matches(PathContainer.parsePath(path));
    }


    /**
     * 日志输出log字符
     *
     * @param headerKeyList
     * @param getHeaderFunc
     * @return
     */
    public static String logHeaderStr(Set<String> headerKeyList, Function<String, String> getHeaderFunc) {
        return headerKeyList.stream()
                .map(k -> k + "=" + Optional.ofNullable(getHeaderFunc.apply(k))
                        .orElse(""))
                .collect(Collectors.joining(", ", "[", "]"));
    }


}
