package org.example.springbootdemo.support.multipleds.aspect;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.example.springbootdemo.support.multipleds.SpecDataSourceContext;

/**
 * 指定数据源切面
 *
 * @date 2024/8/5
 **/
public class SpecDatasourceAspect implements MethodInterceptor {


    /**
     * 通知
     *
     * @param methodInvocation
     * @return
     * @throws Throwable
     */
    @Override
    public Object invoke(MethodInvocation methodInvocation) throws Throwable {
        SpecDatasource annotation = methodInvocation.getMethod()
                .getAnnotation(SpecDatasource.class);
        String datasourceName = annotation.value();

        SpecDataSourceContext.setSpecDatasource(datasourceName);

        try {
            return methodInvocation.proceed();
        } finally {
            SpecDataSourceContext.clear();
        }
    }

}
