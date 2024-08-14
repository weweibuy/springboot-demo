package org.example.springbootdemo.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

/**
 * @date 2024/8/5
 **/
@Data
@ConfigurationProperties(prefix = DatasourceConfigProperties.PREFIX)
public class MultipleDatasourceAndMybatisProperties {

    /**
     * 数据源配置
     */
    private List<DatasourceConfigProperties> datasource;

    /**
     * mybatis 配置
     */
    private List<MybatisAndDatasourceProperties> mybatis;

    /**
     * 开启指定数据源切面的执行顺序
     */
    private Integer specDatasourceAspectOrder = Integer.MAX_VALUE;


}
