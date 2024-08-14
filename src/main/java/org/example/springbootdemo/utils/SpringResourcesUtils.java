package org.example.springbootdemo.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.bind.BindResult;
import org.springframework.boot.context.properties.bind.Bindable;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.core.env.Environment;

/**
 * @date 2024/8/5
 **/
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class SpringResourcesUtils {


    /**
     * 绑定配置文件
     *
     * @param prefix      配置prefix (前缀)
     * @param clazz
     * @param environment
     * @param <T>
     * @return
     */
    public static <T> T bindConfig(String prefix, Class<T> clazz, Environment environment) {
        BindResult<T> restServiceBindResult = Binder.get(environment)
                .bind(prefix, clazz);
        return restServiceBindResult.get();
    }


    /**
     * 绑定配置文件 到对象
     *
     * @param prefix      配置prefix (前缀)
     * @param object
     * @param environment
     * @return
     */
    public static Object bindConfig(String prefix, Object object, Environment environment) {
        BindResult<Object> bind = Binder.get(environment)
                .bind(prefix, Bindable.ofInstance(object));
        return bind.get();
    }

}