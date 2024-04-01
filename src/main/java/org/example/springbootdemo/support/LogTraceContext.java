package org.example.springbootdemo.support;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.slf4j.MDC;


/**
 * 日志 TraceId 与 UserId 上下文工具
 *
 * @date 2024/3/31
 **/
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class LogTraceContext {

    /**
     * mdc TraceId 字段key
     */
    private static final String TID = "tid";

    /**
     * mdc UserId 字段key
     */
    private static final String UID = "uid";


    public static void setTraceId(String traceId) {
        MDC.put(TID, traceId);
    }

    public static void setUserId(String userId) {
        MDC.put(UID, userId);
    }

    public static void setTraceIdAndUserId(String traceId, String userId) {
        setTraceId(traceId);
        setUserId(userId);
    }

    public static void removeTraceId() {
        MDC.remove(TID);
    }

    public static void removeUserId() {
        MDC.remove(UID);
    }

    public static void removeTraceIdAndUserId() {
        removeTraceId();
        removeUserId();
    }


}
