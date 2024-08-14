package org.example.springbootdemo.support.multipleds.aspect;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 指定数据源
 *
 * @date 2024/8/5
 **/
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface SpecDatasource {

    /**
     * 数据源名称
     *
     * @return
     */
    String value();


}
