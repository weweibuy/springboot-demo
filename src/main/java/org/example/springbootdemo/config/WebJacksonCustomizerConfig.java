package org.example.springbootdemo.config;

import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;
import lombok.RequiredArgsConstructor;
import org.example.springbootdemo.config.properties.WebJacksonProperties;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * @date 2024/5/12
 **/
@Configuration
@RequiredArgsConstructor
public class WebJacksonCustomizerConfig {

    private final WebJacksonProperties webJacksonProperties;

    @Bean
    public Jackson2ObjectMapperBuilderCustomizer jsr310Jackson2ObjectMapperBuilderCustomizer() {
        String localDateTimeFormat = webJacksonProperties.getLocalDateTimeFormat();
        DateTimeFormatter localDateTimeFormatter = DateTimeFormatter.ofPattern(localDateTimeFormat);

        String localDateFormat = webJacksonProperties.getLocalDateFormat();
        DateTimeFormatter localDateFormatter = DateTimeFormatter.ofPattern(localDateFormat);

        String localTimeFormat = webJacksonProperties.getLocalTimeFormat();
        DateTimeFormatter localTimeFormatter = DateTimeFormatter.ofPattern(localTimeFormat);
        return builder ->
                builder.serializerByType(LocalDateTime.class, new LocalDateTimeSerializer(localDateTimeFormatter))
                        .deserializerByType(LocalDateTime.class, new LocalDateTimeDeserializer(localDateTimeFormatter))
                        .serializerByType(LocalDate.class, new LocalDateSerializer(localDateFormatter))
                        .deserializerByType(LocalDate.class, new LocalDateDeserializer(localDateFormatter))
                        .serializerByType(LocalTime.class, new LocalTimeSerializer(localTimeFormatter))
                        .deserializerByType(LocalTime.class, new LocalTimeDeserializer(localTimeFormatter));
    }


}
