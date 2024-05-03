package org.example.springbootdemo.model.exception;

import lombok.Getter;
import org.example.springbootdemo.model.eum.ResponseCodeEum;
import org.example.springbootdemo.mvc.ResponseCodeAndMsg;
import org.example.springbootdemo.utils.HttpResponseUtils;

/**
 * @date 2024/5/2
 **/
@Getter
public class SystemException extends RuntimeException {

    private final ResponseCodeAndMsg codeAndMsg;

    public SystemException(Throwable cause) {
        super(cause);
        this.codeAndMsg = ResponseCodeEum.UNKNOWN_EXCEPTION;
    }

    public SystemException(String code, String msg) {
        super(msg);
        this.codeAndMsg = HttpResponseUtils.newResponseCodeAndMsg(code, msg);
    }

    public SystemException(String msg) {
        super(msg);
        this.codeAndMsg = HttpResponseUtils.newResponseCodeAndMsg(ResponseCodeEum.UNKNOWN_EXCEPTION.getCode(), msg);
    }

    public SystemException(String msg, Throwable cause) {
        super(msg, cause);
        this.codeAndMsg = HttpResponseUtils.newResponseCodeAndMsg(ResponseCodeEum.UNKNOWN_EXCEPTION.getCode(), msg);
    }

    public SystemException(ResponseCodeAndMsg codeAndMsg) {
        super(codeAndMsg.getMsg());
        this.codeAndMsg = codeAndMsg;
    }

    public SystemException(ResponseCodeAndMsg codeAndMsg, Throwable cause) {
        super(codeAndMsg.getMsg(), cause);
        this.codeAndMsg = codeAndMsg;
    }


    public ResponseCodeAndMsg getCodeAndMsg() {
        return codeAndMsg;
    }
}
