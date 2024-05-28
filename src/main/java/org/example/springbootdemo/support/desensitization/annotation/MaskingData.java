package org.example.springbootdemo.support.desensitization.annotation;

import com.fasterxml.jackson.annotation.JacksonAnnotationsInside;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.example.springbootdemo.support.desensitization.MaskingDataSerializer;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @date 2024/5/25
 **/
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@JacksonAnnotationsInside
@JsonSerialize(using = MaskingDataSerializer.class)
public @interface MaskingData {

    /**
     * 匹配正则
     *
     * @return
     */
    String patten();

    /**
     * 替换字符
     *
     * @return
     */
    String replace() default "*";


}
