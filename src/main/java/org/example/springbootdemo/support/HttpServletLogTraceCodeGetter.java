package org.example.springbootdemo.support;

import jakarta.servlet.http.HttpServletRequest;
import org.example.springbootdemo.utils.HttpRequestUtils;
import org.example.springbootdemo.utils.IdWorker;
import org.springframework.stereotype.Component;

/**
 * 从 HttpServletRequest 请求中获取 TraceId 与  UserId
 *
 * @date 2024/3/31
 **/
@Component
public class HttpServletLogTraceCodeGetter implements LogTraceCodeGetter<HttpServletRequest> {

    @Override
    public String getTraceId(HttpServletRequest httpServletRequest) {
        return IdWorker.nextStringId();
    }

    @Override
    public String getUserId(HttpServletRequest httpServletRequest) {
        return HttpRequestUtils.getRequestIp(httpServletRequest);
    }


}
