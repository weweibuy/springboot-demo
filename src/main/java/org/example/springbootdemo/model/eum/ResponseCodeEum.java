package org.example.springbootdemo.model.eum;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.example.springbootdemo.mvc.ResponseCodeAndMsg;

/**
 * 响应码
 *
 * @date 2024/5/2
 **/
@Getter
@RequiredArgsConstructor
public enum ResponseCodeEum implements ResponseCodeAndMsg {

    SUCCESS("0", "请求成功"),

    UNKNOWN_EXCEPTION("1", "未知异常"),

    BAD_REQUEST_PARAM("2", "请求参数错误"),

    PAYLOAD_TOO_LARGE("3", "上传文件或请求报文过大"),

    UNSUPPORTED_MEDIA_TYPE("4", "不支持的请求类型"),

    NOT_ACCEPTABLE("5", "不支持的响应类型"),

    NOT_FOUND("6", "请求地址不存在"),

    ;

    private final String code;

    private final String msg;

}
