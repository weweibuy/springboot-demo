package org.example.springbootdemo.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.http.HttpMethod;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 日志相关配置
 *
 * @date 2024/4/13
 **/
@Data
@ConfigurationProperties(prefix = "web.log")
public class WebLogProperties {

    /**
     * body 被禁用输出的body
     */
    public static final String DISABLED_BODY_LOG_STR = "Binary data";

    /**
     * 接口的请求配置
     */
    private List<HttpPathProperties> http;


    @Data
    public static class HttpPathProperties {

        public static final Set<String> SUPPORT_METHOD =
                Stream.of(HttpMethod.GET.name(), HttpMethod.POST.name(), HttpMethod.PUT.name(), HttpMethod.DELETE.name()).collect(Collectors.toSet());

        /**
         * 请求路径, 支持 pattern 匹配模式, 如 /**
         */
        private String path;

        /**
         * 请求 method
         */
        private Set<String> methods = SUPPORT_METHOD;

        /**
         * 日志配置
         */
        private LogProperties log;


    }


    /**
     * 日志输出相关配置
     */
    @Data
    public static class LogProperties {

        /**
         * 是否禁止输出请求日志
         */
        private Boolean disableReq;

        /**
         * 禁止输出请求体
         */
        private Boolean disableReqBody;

        /**
         * 需要输出的请求头
         */
        private Set<String> logReqHeader;

        /**
         * 是否禁止输出响应日志
         */
        private Boolean disableResp;

        /**
         * 禁止输出响应体
         */
        private Boolean disableRespBody;

        /**
         * 需要输出的响应请头
         */
        private Set<String> logRespHeader;


    }

}
