package org.example.springbootdemo.support;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.example.springbootdemo.config.properties.WebLogProperties;
import org.example.springbootdemo.utils.HttpRequestUtils;

import java.io.IOException;

/**
 * @date 2024/4/4
 **/
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Slf4j
public final class HttpReqLogger {

    public static void logReq(HttpServletRequest request, WebLogProperties.LogProperties logProperties) {
        // 判断配置是否输出请求日志
        if (logProperties != null && Boolean.TRUE.equals(logProperties.getDisableReq())) {
            return;
        }

        try {
            logReqInner(request, logProperties);
        } catch (Exception e) {
            log.error("输出请求: {} {}, 日志异常: ", request.getMethod(),
                    request.getRequestURI(), e);
        }
    }

    private static void logReqInner(HttpServletRequest request, WebLogProperties.LogProperties logProperties) throws IOException {
        String path = request.getRequestURI();
        String method = request.getMethod();
        String query = request.getQueryString();

        // 输出请求header
        String header = StringUtils.EMPTY;
        if (logProperties != null && CollectionUtils.isNotEmpty(logProperties.getLogReqHeader())) {
            header = HttpRequestUtils.logHeaderStr(logProperties.getLogReqHeader(), request::getHeader);
        }

        String body = WebLogProperties.DISABLED_BODY_LOG_STR;
        // 是否输出请求body
        if (logProperties == null || !Boolean.TRUE.equals(logProperties.getDisableReqBody())) {
            body = HttpRequestUtils.readRequestBody(request);
        }

        // 输出日志
        logReqInner(path, method, query, header, body);
    }

    private static void logReqInner(String path, String method, String query, String header, String body) {
        if (StringUtils.isNotEmpty(header)) {
            log.info("Http请求 Path: {}, Method: {}, Params: {}, Header: {}, Body: {}",
                    path,
                    method,
                    query,
                    header,
                    body);
        } else {
            log.info("Http请求 Path: {}, Method: {}, Params: {}, Body: {}",
                    path,
                    method,
                    query,
                    body);
        }
    }


}
