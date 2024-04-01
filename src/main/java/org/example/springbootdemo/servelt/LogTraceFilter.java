package org.example.springbootdemo.servelt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.example.springbootdemo.support.LogTraceCodeGetter;
import org.example.springbootdemo.support.LogTraceContext;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * @date 2024/3/31
 * <p>
 * springBoot 内置过滤器的执行顺序:
 * characterEncodingFilter urls=[/*] order=-2147483648
 * formContentFilter urls=[/*] order=-9900
 * requestContextFilter urls=[/*] order=-105
 **/
@Component
@Order(Integer.MIN_VALUE + 1000)
@RequiredArgsConstructor
public class LogTraceFilter extends OncePerRequestFilter {

    private final LogTraceCodeGetter<HttpServletRequest> logTraceCodeGetter;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // 1. 生成id并绑定
        String traceId = logTraceCodeGetter.getTraceId(request);
        String userId = logTraceCodeGetter.getUserId(request);
        LogTraceContext.setTraceIdAndUserId(traceId, userId);

        // 2. 执行请求
        try {
            filterChain.doFilter(request, response);
        } finally {
            // 3. 清除绑定
            LogTraceContext.removeTraceIdAndUserId();
        }


    }
}
