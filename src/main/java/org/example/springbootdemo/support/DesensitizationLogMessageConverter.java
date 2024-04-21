package org.example.springbootdemo.support;

import ch.qos.logback.classic.pattern.ClassicConverter;
import ch.qos.logback.classic.spi.ILoggingEvent;
import org.apache.commons.lang3.StringUtils;
import org.example.springbootdemo.config.properties.WebLogProperties;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 日志 message脱敏转化器
 *
 * @date 2024/4/20
 **/
public class DesensitizationLogMessageConverter extends ClassicConverter {


    @Override
    public String convert(ILoggingEvent event) {
        return LogSensitizationContext.getContext()
                .map(m -> m.get(event.getLoggerName()))
                .map(p -> convertSensitizationMsg(p, event))
                .orElseGet(event::getFormattedMessage);
    }

    private String convertSensitizationMsg(WebLogProperties.LoggerSensitizationProperties properties, ILoggingEvent event) {
        // 脱敏规则
        List<WebLogProperties.SensitizationFieldPatten> fieldPattenList = properties.getSensitizationFields();

        String msg = event.getFormattedMessage();
        for (WebLogProperties.SensitizationFieldPatten fieldPatten : fieldPattenList) {
            if (fieldPatten.getPatten() == null || StringUtils.isBlank(fieldPatten.getReplace())) {
                continue;
            }
            // 正则替换
            msg = matchAndReplaceMsg(fieldPatten, msg);
        }
        return msg;
    }

    private String matchAndReplaceMsg(WebLogProperties.SensitizationFieldPatten fieldPatten, String msg) {
        Pattern patten = fieldPatten.getPatten();
        Matcher matcher = patten.matcher(msg);
        return matcher.replaceAll(fieldPatten.getReplace());
    }


}
