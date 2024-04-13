package org.example.springbootdemo.support;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.example.springbootdemo.config.properties.WebLogProperties;
import org.example.springbootdemo.utils.HttpRequestUtils;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;

/**
 * @date 2024/4/4
 **/
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Slf4j
public final class HttpRespLogger {


    public static void logResp(ContentCachingResponseWrapper response, long reqTimeMillis, WebLogProperties.LogProperties logProperties) {
        // 判断配置是否输出响应日志
        if (logProperties != null && Boolean.TRUE.equals(logProperties.getDisableResp())) {
            return;
        }

        try {
            logRespInner(response, reqTimeMillis, logProperties);
        } catch (Exception e) {
            log.error("输出响应日志异常: ", e);
        }
    }

    private static void logRespInner(ContentCachingResponseWrapper response, long reqTimeMillis, WebLogProperties.LogProperties logProperties) throws IOException {

        // 输出响应header
        String header = StringUtils.EMPTY;
        if (logProperties != null && CollectionUtils.isNotEmpty(logProperties.getLogRespHeader())) {
            header = HttpRequestUtils.logHeaderStr(logProperties.getLogRespHeader(), response::getHeader);
        }

        String body = WebLogProperties.DISABLED_BODY_LOG_STR;
        // 是否输出响应body
        if (logProperties == null || !Boolean.TRUE.equals(logProperties.getDisableRespBody())) {
            body = HttpRequestUtils.readRespBody(response);
        }

        // 输出日志
        logRespInner(response.getStatus(), header, body, reqTimeMillis);

    }

    private static void logRespInner(int status, String header, String body, long reqTimeMillis) {
        long timeUse = System.currentTimeMillis() - reqTimeMillis;
        if (StringUtils.isNotEmpty(header)) {
            log.info("Http响应 Status: {}, Header: {}, Body: {}, 耗时: {}",
                    status,
                    header,
                    body,
                    timeUse);
        } else {
            log.info("Http响应 Status: {}, Body: {}, 耗时: {}",
                    status,
                    body,
                    timeUse);
        }


    }

}
