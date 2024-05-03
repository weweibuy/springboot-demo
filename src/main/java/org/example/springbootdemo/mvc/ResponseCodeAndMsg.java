package org.example.springbootdemo.mvc;

/**
 * 影响 code与msg
 *
 * @date 2024/5/2
 **/
public interface ResponseCodeAndMsg {

    /**
     * 获取响应的code
     *
     * @return
     */
    String getCode();

    /**
     * 获取响应的msg
     *
     * @return
     */
    String getMsg();


}