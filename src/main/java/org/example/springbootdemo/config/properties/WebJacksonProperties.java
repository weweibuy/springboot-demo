package org.example.springbootdemo.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "web.jackson")
public class WebJacksonProperties {

    /**
     * 日期格式化
     */
    private String localDateFormat = "yyyy-MM-dd";

    /**
     * 时间格式化
     */
    private String localTimeFormat = "HH:mm:ss";


    /**
     * 时间日期格式化
     */
    private String localDateTimeFormat =  "yyyy-MM-dd HH:mm:ss";

}
