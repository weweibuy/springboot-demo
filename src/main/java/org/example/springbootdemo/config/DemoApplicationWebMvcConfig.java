package org.example.springbootdemo.config;

import org.example.springbootdemo.config.properties.*;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @date 2024/4/13
 **/
@Configuration
@EnableConfigurationProperties({WebLogProperties.class, WebJacksonProperties.class,
        WebRSAProperties.class, WebSignProperties.class, WebJwtProperties.class})
public class DemoApplicationWebMvcConfig {
}
