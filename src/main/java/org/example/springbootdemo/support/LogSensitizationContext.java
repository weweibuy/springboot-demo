package org.example.springbootdemo.support;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.example.springbootdemo.config.properties.WebLogProperties;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 脱敏上下文
 *
 * @date 2024/4/20
 **/
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class LogSensitizationContext {

    private static ThreadLocal<Map<String, WebLogProperties.LoggerSensitizationProperties>> loggerSensitizationThreadLocal = new ThreadLocal<>();


    public static void setContext(List<WebLogProperties.LoggerSensitizationProperties> sensitizationProperties) {
        if (CollectionUtils.isEmpty(sensitizationProperties)) {
            return;
        }

        // list 转为 logger 的map
        Map<String, WebLogProperties.LoggerSensitizationProperties> propertiesMap =
                sensitizationPropertiesMap(sensitizationProperties);

        if (MapUtils.isNotEmpty(propertiesMap)) {
            loggerSensitizationThreadLocal.set(propertiesMap);
        }
    }

    private static Map<String, WebLogProperties.LoggerSensitizationProperties> sensitizationPropertiesMap(List<WebLogProperties.LoggerSensitizationProperties> sensitizationProperties) {
        return sensitizationProperties.stream()
                .filter(p -> StringUtils.isNotBlank(p.getLogger()))
                .filter(p -> CollectionUtils.isNotEmpty(p.getSensitizationFields()))
                .filter(p -> p.getSensitizationFields().stream()
                        .anyMatch(sf -> sf.getPatten() != null && StringUtils.isNotBlank(sf.getReplace())))
                .collect(Collectors.toMap(WebLogProperties.LoggerSensitizationProperties::getLogger,
                        Function.identity(), (o, n) -> n));
    }


    public static Optional<Map<String, WebLogProperties.LoggerSensitizationProperties>> getContext() {
        return Optional.ofNullable(loggerSensitizationThreadLocal.get());
    }


    public static void clearContext() {
        loggerSensitizationThreadLocal.remove();
    }




}
