package org.example.springbootdemo.support.multipleds.aspect;

import org.springframework.aop.support.StaticMethodMatcherPointcut;

import java.lang.reflect.Method;

/**
 * @date 2024/8/5
 **/
public class SpecDatasourcePointcut extends StaticMethodMatcherPointcut {


    @Override
    public boolean matches(Method method, Class<?> aClass) {
        return method.getAnnotation(SpecDatasource.class) != null;
    }

}

