package org.example.springbootdemo.mvc;

import lombok.extern.slf4j.Slf4j;
import org.example.springbootdemo.model.dto.resp.CommonCodeResponseDTO;
import org.example.springbootdemo.model.eum.ResponseCodeEum;
import org.example.springbootdemo.model.exception.BusinessException;
import org.example.springbootdemo.model.exception.CustomerResponseStatusException;
import org.example.springbootdemo.model.exception.SystemException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

/**
 * 全局异常处理
 *
 * @date 2024/5/2
 **/
@Slf4j
@ControllerAdvice
public class MvcExceptionAdvice {


    /**
     * 业务异常
     *
     * @param e
     * @return
     */
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<CommonCodeResponseDTO> handler(BusinessException e) {
        log.warn("业务异常: ", e);
        return builderResponse(HttpStatus.BAD_REQUEST)
                .body(CommonCodeResponseDTO.response(e.getCodeAndMsg()));
    }

    /**
     * 系统异常
     *
     * @param e
     * @return
     */
    @ExceptionHandler(SystemException.class)
    public ResponseEntity<CommonCodeResponseDTO> handler(SystemException e) {
        log.error("系统异常: ", e);
        return builderResponse(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(CommonCodeResponseDTO.response(e.getCodeAndMsg()));
    }

    /**
     * HTTP ResponseStatusException
     *
     * @param e
     * @return
     */
    @ExceptionHandler(CustomerResponseStatusException.class)
    public ResponseEntity<CommonCodeResponseDTO> handler(CustomerResponseStatusException e) {

        log.warn("请求异常: ", e);
        return builderResponse(e.getStatus())
                .body(CommonCodeResponseDTO.response(e.getCodeAndMsg()));
    }


    /**
     * 参数绑定到对象上异常
     * 1. 参数格式错误
     * 2. 参数校验失败
     *
     * @param e
     * @return
     */
    @ExceptionHandler(BindException.class)
    public ResponseEntity<CommonCodeResponseDTO> handler(BindException e) {

        log.warn("输入参数错误: {}", e.getMessage());
        FieldError fieldError = e.getFieldError();
        String msg = "";
        if (fieldError.isBindingFailure()) {
            msg = "输入参数错误格式错误";
        } else {
            msg = e.getFieldError().getDefaultMessage();
        }
        return builderResponse(HttpStatus.BAD_REQUEST)
                .body(CommonCodeResponseDTO.response(ResponseCodeEum.BAD_REQUEST_PARAM.getCode(), msg));
    }

    /**
     *
     * @param e
     * @return
     */
    @ExceptionHandler(HandlerMethodValidationException.class)
    public ResponseEntity<CommonCodeResponseDTO> handler(HandlerMethodValidationException e) {
        Object[] detailMessageArguments = e.getDetailMessageArguments();
        String msg = detailMessageArguments[0].toString();
        log.warn("输入参数错误: {}", msg);

        return builderResponse(HttpStatus.BAD_REQUEST)
                .body(CommonCodeResponseDTO.response(ResponseCodeEum.BAD_REQUEST_PARAM.getCode(), msg));
    }

    /**
     * 字段类型错误
     * eg: form请求中, 输入参数类型与接收参数类型不匹配
     *
     * @param e
     * @return
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<CommonCodeResponseDTO> handler(MethodArgumentTypeMismatchException e) {

        log.warn("输入参数字段类型错误:", e);
        return builderResponse(HttpStatus.BAD_REQUEST)
                .body(CommonCodeResponseDTO.response(ResponseCodeEum.BAD_REQUEST_PARAM.getCode(), "输入参数字段:[" + e.getName() + "]类型错误"));
    }

    /**
     * Content-Type错误
     * eg: 例如form 请求json接口
     *
     * @param e
     * @return
     */
    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<CommonCodeResponseDTO> handler(HttpMediaTypeNotSupportedException e) {

        log.warn("接口输入Content-Type错误: {}", e.getMessage());
        return builderResponse(HttpStatus.BAD_REQUEST)
                .body(CommonCodeResponseDTO.response(ResponseCodeEum.BAD_REQUEST_PARAM.getCode(),
                        "不支持的请求Content-Type: " + e.getContentType()));
    }


    /**
     * 输入参数格式错误, 无法读取请求报文
     * eg: json请求, 输入参数类型与接收参数类型不匹配,
     * 请求json格式错误等
     *
     * @param e
     * @return
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<CommonCodeResponseDTO> handler(HttpMessageNotReadableException e) {

        log.warn("输入参数格式错误: {}", e.getMessage());
        return builderResponse(HttpStatus.BAD_REQUEST)
                .body(CommonCodeResponseDTO.response(ResponseCodeEum.BAD_REQUEST_PARAM.getCode(), "输入参数格式错误"));
    }


    /**
     * http请求method不支持
     *
     * @param e
     * @return
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<CommonCodeResponseDTO> handler(HttpRequestMethodNotSupportedException e) {

        log.warn("请求 HttpMethod 错误: {}", e.getMessage());
        return builderResponse(HttpStatus.BAD_REQUEST)
                .body(CommonCodeResponseDTO.response(ResponseCodeEum.BAD_REQUEST_PARAM.getCode(), "请求HttpMethod错误"));
    }

    /**
     * 缺少请求参数
     * eg: @RequestParam 要求必传请求参数, 但实际未传
     *
     * @param e
     * @return
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<CommonCodeResponseDTO> handler(MissingServletRequestParameterException e) {

        log.warn("缺少请求参数: {}", e.getMessage());
        return builderResponse(HttpStatus.BAD_REQUEST)
                .body(CommonCodeResponseDTO.response(ResponseCodeEum.BAD_REQUEST_PARAM.getCode(), "缺少请求参数: " + e.getParameterName()));
    }

    /**
     * 请求数据过大
     *
     * @param e
     * @return
     */
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<CommonCodeResponseDTO> handler(MaxUploadSizeExceededException e) {

        log.warn("上传文件或请求报文过大: ", e);
        return builderResponse(HttpStatus.PAYLOAD_TOO_LARGE)
                .body(CommonCodeResponseDTO.response(ResponseCodeEum.PAYLOAD_TOO_LARGE));
    }


    /**
     * 不支持的响应类型
     *
     * @param e
     * @return
     */
    @ExceptionHandler(HttpMediaTypeNotAcceptableException.class)
    public ResponseEntity<CommonCodeResponseDTO> handler(HttpMediaTypeNotAcceptableException e) {

        log.warn("不支持的响应类型: ", e);
        return builderResponse(HttpStatus.NOT_ACCEPTABLE)
                .body(CommonCodeResponseDTO.response(ResponseCodeEum.NOT_ACCEPTABLE));
    }

    /**
     * 请求的资源不存在 springWeb 6 引入
     *
     * @param e
     * @return
     */
    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<CommonCodeResponseDTO> handler(NoResourceFoundException e) {

        log.warn("请求的资源不存在: ", e.getMessage());
        return builderResponse(HttpStatus.NOT_FOUND)
                .body(CommonCodeResponseDTO.response(ResponseCodeEum.NOT_FOUND));
    }

    /**
     * 未知异常
     *
     * @param e
     * @return
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<CommonCodeResponseDTO> handler(Exception e) {

        log.error("未知异常: ", e);

        return builderResponse(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(CommonCodeResponseDTO.response(ResponseCodeEum.UNKNOWN_EXCEPTION));
    }


    /**
     * 构建响应
     *
     * @param httpStatus
     * @return
     */
    private ResponseEntity.BodyBuilder builderResponse(HttpStatus httpStatus) {
        return builderResponse(httpStatus.value());
    }

    private ResponseEntity.BodyBuilder builderResponse(int status) {
        return ResponseEntity.status(status);
    }

}
