package org.example.springbootdemo.model.exception;

import lombok.Getter;
import org.example.springbootdemo.mvc.ResponseCodeAndMsg;

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

    public BusinessException(ResponseCodeAndMsg codeAndMsg, Throwable cause) {
        super(codeAndMsg.getMsg(), cause);
        this.codeAndMsg = codeAndMsg;
    }

    public ResponseCodeAndMsg getCodeAndMsg() {
        return codeAndMsg;
    }
}