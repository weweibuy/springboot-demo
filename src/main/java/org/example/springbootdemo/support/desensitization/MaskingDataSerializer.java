package org.example.springbootdemo.support.desensitization;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import org.example.springbootdemo.support.desensitization.annotation.MaskingData;

import java.io.IOException;
import java.util.Objects;
import java.util.regex.Pattern;

/**
 * @date 2024/5/25
 **/
public class MaskingDataSerializer extends JsonSerializer<String> implements ContextualSerializer {

    private Pattern patten;

    private String replace;

    public MaskingDataSerializer() {
    }

    public MaskingDataSerializer(Pattern patten, String replace) {
        this.patten = patten;
        this.replace = replace;
    }

    @Override
    public void serialize(String value, JsonGenerator jsonGenerator, SerializerProvider serializers) throws IOException {
        if (patten != null && replace != null) {
            // 写出数据
            jsonGenerator.writeString(replaceStr(value));
        } else {
            jsonGenerator.writeString(value);
        }

    }

    /**
     * @param serializerProvider
     * @param beanProperty
     * @return
     * @throws JsonMappingException
     */
    @Override
    public JsonSerializer<?> createContextual(SerializerProvider serializerProvider, BeanProperty beanProperty) throws JsonMappingException {
        if (beanProperty == null) {
            return this;
        }
        // 只处理String 类型
        if (Objects.equals(beanProperty.getType().getRawClass(), String.class)) {
            MaskingData sensitiveInfo = beanProperty.getAnnotation(MaskingData.class);
            if (sensitiveInfo == null) {
                sensitiveInfo = beanProperty.getContextAnnotation(MaskingData.class);
            }
            // 如果能得到注解，就将注解的就构造一个新的 serializer
            if (sensitiveInfo != null) {
                return new MaskingDataSerializer(Pattern.compile(sensitiveInfo.patten()), sensitiveInfo.replace());
            }
        }
        return serializerProvider.findValueSerializer(beanProperty.getType(), beanProperty);
    }

    private String replaceStr(String str) {
        if (str == null) {
            return null;
        }
        return patten.matcher(str)
                .replaceAll(replace);
    }
}