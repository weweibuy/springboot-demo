package org.example.springbootdemo.servelt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.springbootdemo.support.CopyContentCachingRequestWrapper;
import org.example.springbootdemo.support.HttpReqLogger;
import org.example.springbootdemo.support.HttpRespLogger;
import org.example.springbootdemo.utils.HttpRequestUtils;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;

/**
 * @date 2024/4/4
 * requestContextFilter urls=[/*] order=-105
 **/
@Component
@Order(-103)
public class LogRequestFilter extends OncePerRequestFilter {

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

        // 输出请求日志
        HttpReqLogger.logReq(request);
        long reqTimeMillis = System.currentTimeMillis();

        try {
            filterChain.doFilter(request, responseWrapper);
            // 输出响应日志
            HttpRespLogger.logResp(responseWrapper, reqTimeMillis);
        } finally {
            // 缓存流写入到原始响应流中
            responseWrapper.copyBodyToResponse();
        }

    }
}
