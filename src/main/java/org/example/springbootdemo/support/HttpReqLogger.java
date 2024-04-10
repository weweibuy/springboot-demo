package org.example.springbootdemo.support;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;

/**
 * @date 2024/4/4
 **/
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Slf4j
public final class HttpReqLogger {

    private static final String LINE_SEPARATOR = System.getProperty("line.separator");

    public static void logReq(HttpServletRequest request) {
        try {
            logReqInner(request);
        } catch (IOException e) {
            log.error("输出请求: {} {}, 日志异常: ", request.getMethod(),
                    request.getRequestURI(), e);
        }
    }

    private static void logReqInner(HttpServletRequest request) throws IOException {
        String path = request.getRequestURI();
        String method = request.getMethod();
        String body = readRequestBody(request);

        String queryString = request.getQueryString();

        log.info("Http请求 Path: {}, Method: {}, Params: {}, Body: {}",
                path,
                method,
                queryString,
                body);
    }


    private static String readRequestBody(HttpServletRequest request) throws IOException {

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

}
