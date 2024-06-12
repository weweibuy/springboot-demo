package org.example.springbootdemo.model.exception;

import lombok.Getter;
import org.example.springbootdemo.model.eum.ResponseCodeEum;
import org.example.springbootdemo.mvc.ResponseCodeAndMsg;
import org.example.springbootdemo.utils.HttpResponseUtils;

/**
 * @date 2024/5/2
 **/
@Getter
public class BusinessException extends RuntimeException {

    private final ResponseCodeAndMsg codeAndMsg;

    public BusinessException(ResponseCodeAndMsg codeAndMsg) {
        super(codeAndMsg.getMsg());
        this.codeAndMsg = codeAndMsg;
    }

    public BusinessException(String code, String msg) {
        super(msg);
        this.codeAndMsg = HttpResponseUtils.newResponseCodeAndMsg(code, msg);
    }

    public BusinessException(String code, String msg, Exception e) {
        super(msg, e);
        this.codeAndMsg = HttpResponseUtils.newResponseCodeAndMsg(code, msg);
    }

    public BusinessException(ResponseCodeAndMsg codeAndMsg, Throwable cause) {
        super(codeAndMsg.getMsg(), cause);
        this.codeAndMsg = codeAndMsg;
    }

    public ResponseCodeAndMsg getCodeAndMsg() {
        return codeAndMsg;
    }

    public static BusinessException badParam(String msg) {
        return new BusinessException(ResponseCodeEum.BAD_REQUEST_PARAM.getCode(), msg);
    }

    public static BusinessException badParam(String msg, Exception e) {
        return new BusinessException(ResponseCodeEum.BAD_REQUEST_PARAM.getCode(), msg, e);
    }
}