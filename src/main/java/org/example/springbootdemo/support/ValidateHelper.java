package org.example.springbootdemo.support;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import org.example.springbootdemo.model.eum.ResponseCodeEum;
import org.example.springbootdemo.model.exception.BusinessException;
import org.example.springbootdemo.utils.HttpResponseUtils;
import org.springframework.stereotype.Component;

import java.util.Set;

/**
 * @date 2024/5/18
 **/
@Component
@RequiredArgsConstructor
public class ValidateHelper {

    private final Validator validator;

    /**
     * 校验对象
     *
     * @param bean
     * @param groups 分组
     */
    public void validate(Object bean, Class... groups) {
        Set<ConstraintViolation<Object>> validate = validator.validate(bean, groups);
        validate.stream()
                .map(ConstraintViolation::getMessage)
                .findFirst()
                .ifPresent(m -> {
                    throw new BusinessException(
                            HttpResponseUtils.newResponseCodeAndMsg(
                                    ResponseCodeEum.BAD_REQUEST_PARAM.getCode(),
                                    m));
                });

    }
}
