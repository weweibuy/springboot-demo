package org.example.springbootdemo.model.exception;

import lombok.Getter;
import org.example.springbootdemo.mvc.ResponseCodeAndMsg;
import org.springframework.http.HttpStatus;

/**
 * @date 2024/5/2
 **/
@Getter
public class CustomerResponseStatusException extends RuntimeException {

    private final HttpStatus status;

    private final ResponseCodeAndMsg codeAndMsg;

    public CustomerResponseStatusException(HttpStatus status, ResponseCodeAndMsg codeAndMsg) {
        super(codeAndMsg.getMsg());
        this.status = status;
        this.codeAndMsg = codeAndMsg;
    }


    public CustomerResponseStatusException(Throwable cause, HttpStatus status, ResponseCodeAndMsg codeAndMsg) {
        super(codeAndMsg.getMsg(), cause);
        this.status = status;
        this.codeAndMsg = codeAndMsg;
    }
}