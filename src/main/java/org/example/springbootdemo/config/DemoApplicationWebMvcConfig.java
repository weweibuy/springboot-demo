package org.example.springbootdemo.config;

import org.example.springbootdemo.config.properties.WebJacksonProperties;
import org.example.springbootdemo.config.properties.WebLogProperties;
import org.example.springbootdemo.config.properties.WebRSAProperties;
import org.example.springbootdemo.config.properties.WebSignProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @date 2024/4/13
 **/
@Configuration
@EnableConfigurationProperties({WebLogProperties.class, WebJacksonProperties.class, WebRSAProperties.class, WebSignProperties.class})
public class DemoApplicationWebMvcConfig {
}
