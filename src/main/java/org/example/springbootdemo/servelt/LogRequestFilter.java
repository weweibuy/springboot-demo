package org.example.springbootdemo.servelt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.example.springbootdemo.config.properties.WebLogProperties;
import org.example.springbootdemo.support.CopyContentCachingRequestWrapper;
import org.example.springbootdemo.support.HttpReqLogger;
import org.example.springbootdemo.support.HttpRespLogger;
import org.example.springbootdemo.utils.HttpRequestUtils;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;
import java.util.Optional;

/**
 * @date 2024/4/4
 * requestContextFilter urls=[/*] order=-105
 **/
@Component
@Order(-103)
@RequiredArgsConstructor
public class LogRequestFilter extends OncePerRequestFilter {

    private final WebLogProperties webLogProperties;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        // 判断是否有请求体
        boolean includePayload = HttpRequestUtils.isIncludePayload(request);
        if (includePayload) {
            // 包装请求对象
            request = new CopyContentCachingRequestWrapper(request);
        }

        // 包装响应对象
        ContentCachingResponseWrapper responseWrapper = new ContentCachingResponseWrapper(response);

        // 根据请求匹配配置
        Optional<WebLogProperties.HttpPathProperties> httpPathProperties =
                matchLogProperties(request);

        // 日志输出配置
        WebLogProperties.LogProperties logProperties =
                httpPathProperties.map(WebLogProperties.HttpPathProperties::getLog)
                        .orElse(null);

        // 输出请求日志
        HttpReqLogger.logReq(request, logProperties);
        long reqTimeMillis = System.currentTimeMillis();

        try {
            filterChain.doFilter(request, responseWrapper);
            // 输出响应日志
            HttpRespLogger.logResp(responseWrapper, reqTimeMillis, logProperties);
        } finally {
            // 缓存流写入到原始响应流中
            responseWrapper.copyBodyToResponse();
        }
    }

    private Optional<WebLogProperties.HttpPathProperties> matchLogProperties(HttpServletRequest request) {
        String path = request.getRequestURI();
        String method = request.getMethod();

        return Optional.ofNullable(webLogProperties.getHttp())
                // 迭代配置
                .flatMap(hl -> hl.stream()
                        .filter(h -> StringUtils.isNotBlank(h.getPath()))
                        // 匹配请求method
                        .filter(h -> Optional.ofNullable(h.getMethods())
                                .orElse(WebLogProperties.HttpPathProperties.SUPPORT_METHOD)
                                .contains(method))
                        // 路径匹配
                        .filter(h -> HttpRequestUtils.isMatchPath(h.getPath(), path))
                        .findFirst());
    }


}
