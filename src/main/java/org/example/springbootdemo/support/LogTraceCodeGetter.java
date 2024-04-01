package org.example.springbootdemo.support;

/**
 * 获取日志的 UserId 与 TraceId
 *
 * @date 2024/3/31
 **/
public interface LogTraceCodeGetter<T> {


    /**
     * 获取 traceId
     *
     * @param t
     * @return
     */
    String getTraceId(T t);

    /**
     * 获取 userId
     *
     * @param t
     * @return
     */
    String getUserId(T t);



}
