package org.example.springbootdemo.support;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

/**
 * @date 2024/4/4
 **/
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Slf4j
public final class HttpRespLogger {


    public static void logResp(ContentCachingResponseWrapper response, long reqTimeMillis) {
        try {
            logRespInner(response, reqTimeMillis);
        } catch (IOException e) {
            log.error("输出响应日志异常: ", e);
        }
    }

    private static void logRespInner(ContentCachingResponseWrapper response, long reqTimeMillis) throws IOException {

        String body = readRespBody(response);

        log.info("Http响应 Status: {}, Body: {}, 耗时: {}",
                response.getStatus(),
                body,
                System.currentTimeMillis() - reqTimeMillis);
    }


    private static String readRespBody(ContentCachingResponseWrapper response) throws IOException {
        InputStream contentInputStream = response.getContentInputStream();
        return IOUtils.toString(contentInputStream, StandardCharsets.UTF_8);
    }

}
